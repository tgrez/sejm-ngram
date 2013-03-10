
import log
import sys
from config import *
from utils import *
from calendar import datetime

def dbtype2castmethod(dbtype):
    """Converts db type name to python cast method e.g. varchar[...]->str, int(...)->int"""
    dbtype = str(dbtype).strip()
    if dbtype.startswith("varchar"): return str
    if dbtype.startswith("int"): return int
    if dbtype.startswith("date"): return str#datetime
    raise Exception('[dbtype2castmethod] unknown db type name:'+dbtype)

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

    def insert_record(self, table_name, record):
        """Inserts record (dictionary {column-name: column-value}) into database."""
        try:
            cols = reduce(lambda c1,c2: c1+", "+c2, (colname for colname in record) )
            vals = reduce(lambda v1,v2: v1+", "+v2, ("\""+val+"\"" for colname,val in record.iteritems()) )
            sql =  "INSERT INTO "+table_name+" ("+cols+") VALUES ("+vals+")"
            log.dbg("executing "+sql)
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


if __name__=="__main__":
    log.info("The script loads from stdin CSV file and inserts it into DB Table (table name given as first argument).")

    try: table_name = sys.argv[1]
    except: log.err("Argument expected: table_name to be updated."); sys.exit(-1)
    inputfile = INSERT_INPUT_FILE
    
    log.info("Retrieving columns from DB table "+table_name)
    db = DBWrapper()
    column_names = db.get_column_names(table_name)
    log.info("DB table columns="+str(column_names))

    log.info("Retrieving columns from CSV file "+str(inputfile))
    header = load_csv_header(inputfile)
    log.info("CSV file columns="+str(header))
    
    common_columns = set(header).intersection(column_names)
    log.info("Common columns between CSV and DB: " + str( common_columns ) )
    
    db.begin()    
    for row in load_csv_rows(inputfile):
        record = filterout_dictionary( combine_dictionary(header, row), common_columns)                    
        db.insert_record(table_name, record)
    db.commit()

