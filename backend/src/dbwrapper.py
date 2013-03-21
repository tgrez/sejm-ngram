#!/usr/bin/python
# -*- coding: utf-8 -*-

import log
import sys
from config import *
from utils import *
from calendar import datetime


def dbtype2castmethod(dbtype):
    """Converts db type name to python cast method e.g. varchar[...]->str, int(...)->int"""
    dbtype = str(dbtype).strip()
    if dbtype.startswith("varchar"): return unicode
    if dbtype.startswith("int"): return str
    if dbtype.startswith("date"): return str#datetime
    raise Exception('[dbtype2castmethod] unknown db type name:'+dbtype)

def map_values_types(record, key2type):
    """Replaces types in records according to mapper key2type = {key: cast_method} as returned by DBWrapper.get_column_types(...) ."""
    for k,cast_type in key2type.iteritems(): 
        if k not in record: continue
        #if record[k] is datetime.date: record[k] = "data"
        #else:
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
        sql = "SELECT max(id) FROM "+table_name
        log.dbg("executing "+sql[:100]+"...")
        self.cur.execute(sql)
        return self.cur.fetchall()[0][0]

    def get_ids(self, table_name, status_value=0):
        """Returns list of ids with status column no bigger than specified value."""
        sql = "SELECT id FROM "+str(table_name)+" where status<="+str(status_value)
        log.dbg("executing "+sql[:100]+"...")
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
            cols = reduce(lambda c1,c2: c1+", "+c2, ((colname) for colname in record) )
            vals = reduce(lambda v1,v2: v1+", "+v2, ("\""+(val)+"\"" for colname,val in record.iteritems()) )
            sql =  "INSERT INTO "+table_name+" ("+cols+") VALUES ("+vals+")"
            log.dbg("executing "+sql[:150]+"...")
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
        log.dbg("table_name="+self.table_name+" columns="+str(self.columns)+" column_names="+str(self.column_names)+" key2type ="+str(self.key2type))

    def get_table_max_id(self):
        """Returns maximal value of id column in table of specified name."""
        return DBWrapper.get_table_max_id(self, self.table_name)

    def insert_record(self, record):
        """Inserts record (dictionary {column-name: column-value}) into table (casting and columns name matching are done)."""
        shared_colnames = set(record.keys()).intersection(self.column_names)
        record = filterout_dictionary(record, shared_colnames) 
        record = map_values_types(record, self.key2type)
        DBWrapper.insert_record(self, self.table_name, record)




#if __name__=="__main__":
#    log.set_output_level(log.LOG_LEVEL.DBG)
#    table = DBTableWrapper("ngrams")
#    table.begin()
#    print "table.get_table_max_id =",table.get_table_max_id()
#    table.insert_record({"id":100000001, "ngram": "dupa"})
#    table.commit()
    

