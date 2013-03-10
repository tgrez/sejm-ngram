"""Put your configuration and things that should be shared here."""

CSV_SEPARATOR = "\t;\t"
CSV_REPLACEMENT = "," #if CSV_SEPARATOR found in text replace it with CSV_REPLACEMENT

#MySQL connection configuration
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
