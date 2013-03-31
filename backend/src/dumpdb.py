#!/usr/bin/python
# -*- coding: utf-8 -*-

import log
import sys
from config import *
from utils import *
from dbwrapper import *

import codecs
        

if __name__=="__main__":
    log.info("The script takes table from DB file and dumps it into CSV file.")
    log.info("Table name is given as the first argument and output file as a second one.")

    try: table_name = sys.argv[1]
    except: log.err("Argument expected: table_name to be dumped."); sys.exit(-1)

    try: output = sys.argv[2]
    except: log.err("Argument expected: output file to be written."); sys.exit(-1)
    
    log.info("Opening table = %s" % table_name)
    db = DBTableWrapper(table_name)
    colnames = db.get_column_names()
    rows = db.get_all_rows(colnames)
    log.info("%i rows retrieved" % len(rows))

    log.info("Opening file = %s" % output)
    f = codecs.open(output, mode="w", encoding="utf-8")
    write_row(f, colnames)

    for row in rows:
        write_row(f, row)
