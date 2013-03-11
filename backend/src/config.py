#!/usr/bin/python
# -*- coding: utf-8 -*-
"""Put your configuration and things that should be shared here."""

#used in wystapienia_updatedb

PHP_SHELL_COMMAND = "php"
PHP_SEJM_GETMAXID_FILENAME = "sejm_getmaxid.php -d"

DATASETNAME_SEJM_WYSTAPIENIA = "sejm_wystapienia" #sejmometr API dataset name
DBTABLE_SEJM_WYSTAPIENIA_NAME = "sejm_wystapienia" #DB table name
PHP_SEJM_GET_TMPFILE = "/tmp/wystapienia.csv"
PHP_SEJM_GET_FILENAME = "sejm_get.php -d "+DATASETNAME_SEJM_WYSTAPIENIA+" -o "+PHP_SEJM_GET_TMPFILE
DBTABLE_SEJM_PACKAGE_SIZE = 200 #how many should be downloaded and inserted at once

URL_SEJM_WYSTAPIENIA = "http://sejmometr.pl/sejm_wystapienia/" #URL where to find html 
DBTABLE_HTML_WYSTAPIENIA_NAME = "html_wystapienia" #DB table tame
DBTABLE_HTML_WYSTAPIENIA_COL_ID = "id" #DB table column id
DBTABLE_HTML_WYSTAPIENIA_COL_MEMBER = "posel" #DB table column name
DBTABLE_HTML_WYSTAPIENIA_COL_POSITION = "stanowisko" #DB table column name
DBTABLE_HTML_WYSTAPIENIA_COL_TEXT = "tekst" #DB table column name

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

#ngrams_updatedb configuration
NGRAMS_LENGTHS = [1,2,3]
DBTABLE_NGRAMS_NAME = "ngrams" #DB table name
DBTABLE_NGRAMS_COL_NGRAM = "ngram" #DB table column with ngram
DBTABLE_NGRAMS_COL_ID = "id" #DB table column with id
DB_STATUS_NOT_PROCESSED_CODE = 0

