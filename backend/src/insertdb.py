
#!/usr/bin/python
# -*- coding: utf-8 -*-

import log
import sys
from config import *
from utils import *
from calendar import datetime
from dbwrapper import *

def csv_insert_db(inputfile, table_name):
    """Loads CSV from input file and stores it into table of specified name."""

    log.info("Retrieving columns from DB table "+table_name)
    db = DBWrapper()
    column_names = db.get_column_names(table_name)
    log.info("DB table columns="+str(column_names))

    log.info("Retrieving columns from CSV file "+str(inputfile))
    header = load_csv_header(inputfile)
    log.info("CSV file columns="+str(header))
    
    common_columns = set(header).intersection(column_names)
    log.info("Common columns between CSV and DB: " + str( common_columns ) )
    
    log.info("Loading CSV records into DB table...")
    db.begin()    
    for row in load_csv_rows(inputfile):
        record = filterout_dictionary( combine_dictionary(header, row), common_columns)                    
        db.insert_record(table_name, record)
    db.commit()


if __name__=="__main__":
    log.info("The script loads from stdin CSV file and inserts it into DB Table (table name is given as the first argument).")

    try: table_name = sys.argv[1]
    except: log.err("Argument expected: table_name to be updated."); sys.exit(-1)
    inputfile = INSERT_INPUT_FILE
    
    csv_insert_db(inputfile, table_name)
