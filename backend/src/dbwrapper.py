#!/usr/bin/python
# -*- coding: utf-8 -*-

import log
import sys
from config import *
from utils import *
from calendar import datetime


def dbtype2castmethod(dbtype):
    """Converts db type name to python cast method e.g. varchar[...]->unicode."""
    dbtype = str(dbtype).strip()
    if dbtype.startswith("varchar"):    return lambda x: str(x).decode('UTF-8')
    if dbtype.startswith("text"):       return lambda x: str(x).decode('UTF-8')
    if dbtype.startswith("int"):        return int
    if dbtype.startswith("date"):       return str #datetime.datetime
    raise Exception('[dbtype2castmethod] unknown db type name:'+dbtype)

def map_values_types(record, key2type):
    """Replaces types in records according to mapper key2type = {key: cast_method} as returned by DBWrapper.get_column_types(...) ."""
    for k,cast_type in key2type.iteritems(): 
        if k not in record: continue
        record[k] = cast_type(record[k])
    return record


class DBWrapper():
    """Controls communication to MySQL database."""

    def __init__(self):
        self.con = MYSQL_CONNECTION
        self.cur = self.con.cursor()

    def get_columns(self, table_name):
        """Returns list of tuples describing columns from table of specified name."""
        self.cur.execute("SHOW COLUMNS FROM "+str(table_name))        
        return self.cur.fetchall()

    def get_column_names(self, table_name):
        """Returns list of colum names from table of specified name."""
        cols = self.get_columns(table_name)
        return list( col[0] for col in cols)

    def get_column_types(self, table_name):
        """Returns dictionary {column-name: type_casting_methods} for columns from table of specified name."""
        cols = self.get_columns(table_name)
        return dict( ( col[0], dbtype2castmethod(col[1]) ) for col in cols)

    def get_table_max_id(self, table_name):
        """Returns maximal value of id column in table of specified name."""
        sql = "SELECT max("+ID_COL_NAME+") FROM "+table_name
        log.dbg("executing sql="+sql[:100]+"...")
        self.cur.execute(sql)
        return self.cur.fetchall()[0][0]

    def get_ids(self, table_name, status_value=0):
        """Returns list of ids with status column no bigger than specified value."""
        sql = "SELECT "+ID_COL_NAME+" FROM "+str(table_name)+" WHERE status<="+str(status_value)
        log.dbg("executing sql="+sql[:100]+"...")
        self.cur.execute(sql)
        return list(e[0] for e in self.cur.fetchall())

    def get_record(self, table_name, idvalue):
        """Returns record of id=idvalue from table of specified name."""
        sql = "SELECT * FROM "+str(table_name)+" where id="+str(idvalue)
        self.cur.execute(sql)
        return self.cur.fetchall()[0]

    def insert_record(self, table_name, record):
        """Inserts record (dictionary {column-name: column-value}) into database."""
        try:
            cols = reduce(lambda c1,c2: c1+u", "+c2, ((colname) for colname in record) )
            vals = reduce(lambda v1,v2: v1+u", "+v2, (u"\""+unicode(val)+u"\"" for colname,val in record.iteritems()) )
            sql =  u"INSERT INTO "+table_name+u" ("+cols+u") VALUES ("+vals+")"
            self.cur.execute(sql)
            return True
        except mdb.Error, e:
            log.err("error %d: %s" % (e.args[0],e.args[1]))
            return False
    
    def begin(self):      
        log.dbg("transaction begin")
        self.con.begin()
                            
    def commit(self):      
        log.dbg("transaction commit")
        self.con.commit()




class DBTableWrapper(DBWrapper):
    """Controls communication to single table in MySQL database."""

    def __init__(self, table_name):
        DBWrapper.__init__(self)
        self.table_name = str(table_name)
        self.columns = DBWrapper.get_columns(self, table_name)
        self.column_names = list(c[0] for c in self.columns)
        self.key2type = dict( ( col[0], dbtype2castmethod(col[1]) ) for col in self.columns)
        log.dbg("[DBTableWrapper.__init__] table_name="+self.table_name+" columns="+str(self.columns)+
                " column_names="+str(self.column_names)+" key2type ="+str(self.key2type))

    def get_table_max_id(self):
        """Returns maximal value of id column in table of specified name."""
        return DBWrapper.get_table_max_id(self, self.table_name)

    def insert_record(self, record):
        """Inserts record (dictionary {column-name: column-value}) into table (casting and columns name matching are done)."""
        shared_colnames = set(record.keys()).intersection(self.column_names)
        record = filterout_dictionary(record, shared_colnames) 
        record = map_values_types(record, self.key2type)
        DBWrapper.insert_record(self, self.table_name, record)

    def __str__(self):
        return "table '"+self.table_name+"'"



class DBDictionaryTable(DBTableWrapper):
    """Controls communication to single table with two columns (id, value) that represents dictionary in MySQL database."""        
    def __init__(self, table_name):
        DBTableWrapper.__init__(self, table_name)

        if not len(self.columns) == 2: #guarantee that we have two columns
            raise Exception("[DBTableDictionary.__init__] Table "+str(table_name)+
                            " should have two columns but has "+str(len(self.columns)) )
        if ID_COL_NAME not in self.column_names: #guarantee that one of columns is ID
            raise Exception("[DBTableDictionary.__init__] Table "+str(table_name)+
                            " must containe column of name id!")

        #extract value column name
        for colname in self.column_names: 
            if colname!=ID_COL_NAME: 
                self.value_column = colname
        log.dbg("[DBDictionaryTable.__init__] id_column_name="+ID_COL_NAME+" value_column_name="+self.value_column)
            
        #retrieve dictionary content:
        sql = "SELECT id, %s FROM %s" % (self.value_column, self.table_name)
        self.cur.execute(sql)
        rows = self.cur.fetchall()
        self.ids    = set(idd for idd, val in rows)       #set of id of elements that are already in DB
        self.id2val = dict((idd,val) for idd,val in rows) #dictionary {id: value}
        self.val2id = dict((val,idd) for idd,val in rows) #dictionary {value: id}
        log.dbg("[DBDictionaryTable.__init__] dictionaries: len(id2val)=%i len(val2id)=%i" % (len(self.id2val),len(self.val2id)) )

    def get_value(self, idd):
        """Returns value assigned to given id or None if not found."""
        return self.id2val.get(idd, None)
        
    def get_id(self, value):
        """Returns id assigned to given value or None if not found."""
        return self.val2id.get(value, None)

    def set_pair(self, idd, value):
        """Updates dictionary with new pair (id, value)."""            
        self.begin()

        self.cur.execute( ("DELETE FROM `%s` WHERE id=%s" % (self.table_name, str(idd))) )
        self.insert_record( {"id": idd, self.value_column: value} )

        self.id2val[idd] = value
        self.val2id[value] = idd

        self.commit()

    def _get_max_id_(self):
        return max(self.id2val.keys())

    def retrieve_id(self, value):
        """Returns id assigned to given value. If not found than new entry is created and dictionary is updated."""
        idd = self.get_id(value)
        if idd is None:
           self.set_pair(self._get_max_id_()+1, value)             
        idd = self.get_id(value)
        return idd
    
    def __str__(self):
        return "table '"+self.table_name+"'"



class DBStructureWrapper(DBTableWrapper):
    """Represents group of DB Tables where the central table contains data and others are dictionaries of column values."""

    def __init__(self, table_name):
        DBTableWrapper.__init__(self, table_name)
        self.column2dictionary = {}

    def add_column_dictionary(self, column_name, db_dictionary_table):
        """Sets mapping dictionary for column of given name."""
        self.column2dictionary[column_name] = db_dictionary_table

    #TODO
    

    
#if __name__=="__main__":
#    log.set_output_level(log.LOG_LEVEL.DBG)
#    table = DBDictionaryTable("posel_dictionary")
#    print "posel3 -> id",table.retrieve_id("posel3")
#    print "posel33 -> id",table.retrieve_id("posel33")
    


    

