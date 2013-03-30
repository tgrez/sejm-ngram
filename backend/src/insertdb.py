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
    db = DBTableWrapper(table_name)
    header = load_csv_header(inputfile)
        
    log.info("inserting records ("+str(header)+") from '"+str(inputfile)+"' into DB table '"+str(table_name)+"'...")
    db.begin()    
    for row in load_csv_rows(inputfile):
        record = combine_dictionary(header, row)
        db.insert_record(record)
    db.commit()


if __name__=="__main__":
    log.info("The script loads from stdin CSV file and inserts it into DB Table (table name is given as the first argument).")

    try: table_name = sys.argv[1]
    except: log.err("Argument expected: table_name to be updated."); sys.exit(-1)
    inputfile = INSERT_INPUT_FILE
    
    csv_insert_db(inputfile, table_name)
