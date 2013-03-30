#!/usr/bin/python
# -*- coding: utf-8 -*-
"""Put your configuration and things that should be shared here."""



#ngrams_updatedb configuration
NGRAMS_LENGTHS = [1,2,3]
DBTABLE_NGRAMS_NAME = "ngrams" #DB table name
DBTABLE_NGRAMS_COL_NGRAM = "ngram" #DB table column with ngram
DBTABLE_NGRAMS_COL_ID = "id" #DB table column with id
DB_STATUS_NOT_PROCESSED_CODE = 0


#MySQL connection configuration (insertdb.py)
import MySQLdb as mdb
MYSQL_CONNECTION = mdb.connect("localhost", "testuser", "", "wystapienia", charset="utf8");

#wystapienia_parse configuration
import sys
WYSTAPIENIA_INPUT_FILE = sys.stdin
WYSTAPIENIA_OUTPUT_FILE = sys.stdout

#insertdb configuration
import sys
INSERT_INPUT_FILE = sys.stdin
WYSTAPIENIA_OUTPUT_FILE = sys.stdout

#general options
CSV_SEPARATOR = "\t;\t"
CSV_REPLACEMENT = "," #if CSV_SEPARATOR found in text replace it with CSV_REPLACEMENT
ID_COL_NAME = "id"

