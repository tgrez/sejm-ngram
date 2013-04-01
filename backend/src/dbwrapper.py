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
    if dbtype.startswith("varchar"):    return unicode
    if dbtype.startswith("text"):       return unicode
    if dbtype.startswith("int"):        return int
    if dbtype.startswith("float"):      return float
    if dbtype.startswith("date"):       return str #datetime.datetime
    raise Exception('[dbtype2castmethod] unknown db type name:'+dbtype)

def map_values_types(record, key2type):
    """Replaces types in records according to mapper key2type = {key: cast_method} as returned by DBWrapper.get_column_types(...) ."""
    for k,cast_type in key2type.iteritems(): 
        if k not in record: continue
        #print "[map_values_types]",type(record[k]),"->",cast_type
        if   type(record[k])==str and cast_type==unicode:       
            record[k] = record[k].decode('UTF-8')
        else: record[k] = cast_type(record[k])
    return record


class DBTransactionManager():
    """Manages DB transactions."""        

    def __init__(self, connection=MYSQL_CONNECTION, name="Anonymous DBTransactionManager"):
        self.con = connection
        self.cur = self.con.cursor()
        self.in_transaction = False 
        self.name = str(name)
        
    def begin(self):      
        if self.in_transaction: return 
        log.dbg("[%s] transaction begin" % self.name)
        self.con.begin()
        self.in_transaction = True
                            
    def commit(self):      
        if not self.in_transaction: return
        log.dbg("[%s] transaction commit" % self.name)
        self.con.commit()
        self.in_transaction = False

    def rollback(self):
        log.dbg("[%s] transaction rollback" % self.name)
        self.con.rollback()

    def get_cursor(self):
        return self.cur



class DBWrapper():
    """Controls communication to database."""

    def __init__(self, transaction_manager = DBTransactionManager()):
        self.transaction = transaction_manager
        self.cur = self.transaction.get_cursor()

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
        sql = "SELECT "+ID_COL_NAME+" FROM "+str(table_name)+" WHERE "+STATUS_COL_NAME+"<="+str(status_value)
        log.dbg("executing sql="+sql[:100]+"...")
        self.cur.execute(sql)
        return list(e[0] for e in self.cur.fetchall())

    def get_record(self, table_name, idvalue):
        """Returns record of id=idvalue from table of specified name."""
        sql = "SELECT * FROM "+str(table_name)+" where id="+str(idvalue)
        self.cur.execute(sql)
        return self.cur.fetchall()[0]

    def get_all_rows(self, table_name, colnames):
        """Retrieve all rows from table."""
        cols = reduce(lambda c1,c2: c1+", "+c2, (colname for colname in colnames) )
        sql = "SELECT %s FROM %s" % (cols, table_name)
        self.cur.execute(sql)
        return self.cur.fetchall()


    def insert_record(self, table_name, record):
        """Inserts record (dictionary {column-name: column-value}) into database."""
        try:
            cols = reduce(lambda c1,c2: c1+u", "+c2, ((colname) for colname in record) )
            vals = reduce(lambda v1,v2: v1+u", "+v2, (u"\""+unicode(val)+u"\"" for colname,val in record.iteritems()) )
            sql =  u"INSERT INTO "+table_name+u" ("+cols+u") VALUES ("+vals+")"
            log.dbg("inserting %s into table %s" % (sql[:100], table_name))
            self.cur.execute(sql)
            return True
        except mdb.Error, e:
            log.err("error %d: %s" % (e.args[0],e.args[1]))
            return False
    
    def exec_sql(self,sql):
        """Executes SQL code."""
        return self.cur.execute(sql)

    def update(self, table_name, idd, column_name, value):
        """Updates column value for row of selected id."""
        sql = u"UPDATE %s SET %s = %s WHERE %s = %s" % (table_name, column_name, unicode(value), ID_COL_NAME, str(idd))
        log.dbg("executing sql = %s" % sql[:100])
        return self.cur.execute(sql)
    
    def begin(self):      
        """Starts transaction."""
        return self.transaction.begin()
                            
    def commit(self):      
        """Finalizes transaction."""
        return self.transaction.commit()

    def rollback(self):
        """Rollback transaction."""
        return self.transaction.rollback()


class DBTableWrapper(DBWrapper):
    """Controls communication to single table in  database."""

    def __init__(self, table_name, transaction_manager = DBTransactionManager()):
        DBWrapper.__init__(self, transaction_manager)
        self.table_name = str(table_name)
        self.columns = DBWrapper.get_columns(self, table_name)
        self.column_names = list(c[0] for c in self.columns)
        self.key2type = dict( ( col[0], dbtype2castmethod(col[1]) ) for col in self.columns)
        log.dbg("[DBTableWrapper.__init__] table_name="+self.table_name+" columns="+str(self.columns)+
                " column_names="+str(self.column_names)+" key2type ="+str(self.key2type))

    def get_column_names(self):
        return self.column_names

    def get_all_rows(self, colnames):
        """Retrieve all rows from table."""
        return DBWrapper.get_all_rows(self, self.table_name, colnames)

    def get_table_max_id(self):
        """Returns maximal value of id column in table of specified name."""
        return DBWrapper.get_table_max_id(self, self.table_name)

    def insert_record(self, record):
        """Inserts record (dictionary {column-name: column-value}) into table (casting and columns name matching are done)."""
        shared_colnames = set(record.keys()).intersection(self.column_names)
        record = filterout_dictionary(record, shared_colnames) 
        record = map_values_types(record, self.key2type)
        DBWrapper.insert_record(self, self.table_name, record)

    def update(self, idd, column_name, value):
        """Updates column value for row of selected id."""
        if column_name not in self.column_names: 
            raise Exception("[update] There is no column %s in table %s!" % column_name, self.table_name)
        record = map_values_types({column_name: value}, self.key2type) #cast types
        return DBWrapper.update(self, self.table_name, idd, column_name, record[column_name])


    def __str__(self):
        return "table '"+self.table_name+"'"

    def get_record(self, idvalue):        
        """Returns table record for given id."""
        row = DBWrapper.get_record(self, self.table_name, idvalue)
        return combine_dictionary(self.column_names, row)

    def get_ids(self, status_value=0):
        """Returns list of ids with status column no bigger than specified value."""
        return DBWrapper.get_ids(self, self.table_name, status_value)


class DBTableInsterterCache(DBTableWrapper):
    """Instead of online instering records stores it in memory and insterts on flush."""
    
    def __init__(self, table_name, transaction_manager = DBTransactionManager()):
        DBTableWrapper.__init__(self, table_name, transaction_manager)
        self.insert_cache = []
        self.update_cache = []

    def insert_record(self, record):
        """Stores record in memory for further writining (on flush)."""
        shared_colnames = set(record.keys()).intersection(self.column_names)
        record = filterout_dictionary(record, shared_colnames) 
        record = map_values_types(record, self.key2type)
        self.insert_cache.append(record)

    def update(self, idd, column_name, value):
        if column_name not in self.column_names: 
            raise Exception("[update] There is no column %s in table %s!" % column_name, self.table_name)
        record = map_values_types({column_name: value}, self.key2type) #cast types
        self.update_cache.append( (idd,column_name,record[column_name]) )

    def flush(self):
        """Writes cached records into DB."""
        try:
            self.begin()

            log.info("Inserting %i records into table '%s'" % (len(self.insert_cache),self.table_name))
            for record in self.insert_cache:
                DBWrapper.insert_record(self, self.table_name, record)
            self.insert_cache = []

            log.info("Updating %i records in table '%s'" % (len(self.update_cache),self.table_name))
            for idd, column_name, val in self.update_cache:
                DBWrapper.update(self, self.table_name, idd, column_name, val)
            self.update_cache = []

            self.commit()
        except:
            self.rollback()
            log.err("Failed flushing. Rollback!")
            raise

    

class DBDictionaryTable(DBTableWrapper):
    """Controls communication to single table with two columns (id, value) that represents dictionary in  database. 

      WARNING: Multiprocess Unsafe (but fast ;) ) Implementation!"""        
    def __init__(self, table_name, db_autosynchronization = False, transaction_manager = DBTransactionManager()):
        """Whether DB should be updated as soon as it is possible or by db_autosynchronization() method execution."""
        transaction_manager.begin()

        DBTableWrapper.__init__(self, table_name, transaction_manager)        
        self.db_autosynchronization = db_autosynchronization

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
        log.info("[DBDictionaryTable.__init__] table %s dictionaries: len(id2val)=%i len(val2id)=%i" 
            % (self.table_name, len(self.id2val),len(self.val2id)) )

        transaction_manager.commit()

    def get_size(self):
        """Returns number of entries in dictionary."""
        return len(self.id2val)
   
    def get_value(self, idd):
        """Returns value assigned to given id or None if not found."""
        return self.id2val.get(idd, None)
        
    def get_id(self, value):
        """Returns id assigned to given value or None if not found."""
        return self.val2id.get(value, None)

    def set_pair(self, idd, value):
        """Updates dictionary with new pair (id, value)."""            
        
        log.dbg("table=%s id=%s val=%s" % (self.table_name, (idd), (value)))
        if idd in self.id2val: 
            log.warn("table=%s id=%s val=%s already in dictionary!" % (self.table_name, (idd), (value)))

        self.id2val[idd] = value
        self.val2id[value] = idd

        if self.db_autosynchronization:
            self.begin()
            self.cur.execute( (u"DELETE FROM `%s` WHERE id=%s" % (self.table_name, str(idd))) )
            self.insert_record( {"id": idd, self.value_column: value} )
            self.ids.add(idd)
            self.commit()

    def synchronize_db(self):
        """Writes dicionary data into DB."""
        if self.db_autosynchronization: 
            log.info("assuming that table %s is already synchronized with dictionary" % (self.table_name))
            return
        log.info("synchronizing table %s (%i rows to be stored)" % (self.table_name,len(self.id2val)) )
        self.begin()
        self.cur.execute( u"DELETE FROM `%s`" % self.table_name )     #TODO maybe less 'invasive' implementation?
        for idd, value in self.id2val.iteritems():
            self.insert_record( {"id": idd, self.value_column: value} )
        self.commit()

    def _get_max_id_(self):
        if self.db_autosynchronization:
            return self.get_table_max_id()
        else:
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



    
#if __name__=="__main__":
#    log.set_output_level(log.LOG_LEVEL.DBG)
#    table = DBDictionaryTable("posel_dictionary")
#    print "posel3 -> id",table.retrieve_id("posel3")
#    print "posel33 -> id",table.retrieve_id("posel33")
    


    

