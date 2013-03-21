#!/usr/bin/python
# -*- coding: utf-8 -*-

from config import *
from itertools import izip
import log


def text_filter(text, replace_with = ''):
    """Removes specific characters from text."""
    #print type(text), text
    text_nopunct = str(text)
    #text_nopunct = str(text).translate(string.maketrans("",""), string.punctuation)
    #for c in string.punctuation:
    #    text_nopunct = text_nopunct.replace(c, replace_with)
    #text_nopunct = text_nopunct.translate(string.maketrans("",""), string.digits)
    #for c in string.digits:
    #    text_nopunct = text_nopunct.replace(c, replace_with)        
    text_nopunct = text_nopunct.replace('”', replace_with)
    text_nopunct = text_nopunct.replace('“', replace_with)
    text_nopunct = text_nopunct.replace('‘', replace_with)
    text_nopunct = text_nopunct.replace('’', replace_with)
    text_nopunct = text_nopunct.replace('\'', replace_with)
    text_nopunct = text_nopunct.replace('  ', replace_with)
    text_nopunct = text_nopunct.replace('\n', replace_with)
    return text_nopunct 



def load_csv_columns(f, csvseparator=CSV_SEPARATOR, cast_method=str):
    """Loads CSV file and returns dictionary {column-name: list-of-values in this column}."""
    import csv
    dmsoreader = csv.reader(f, delimiter=csvseparator.strip())            
    
    header = dmsoreader.next()
    
    col2vals = {}
    for row in dmsoreader: 
        for col,val in zip(header,row):
             col2vals[col] = col2vals.get(col,[])+[cast_method(val)]

    return col2vals

def load_csv_header(f, csvseparator=CSV_SEPARATOR, cast_method=lambda x: str(x).strip()):
    """Loads CSV file and returns just header."""
    import csv
    dmsoreader = csv.reader(f, delimiter=csvseparator.strip())            
    header = dmsoreader.next()    
    return list(cast_method(e) for e in header)


def load_csv_rows(f, csvseparator=CSV_SEPARATOR, cast_method=lambda x: str(x).strip()):
    """Loads CSV file and yields rows (assumes that header is already loaded with load_csv_header)."""
    import csv
    dmsoreader = csv.reader(f, delimiter=csvseparator.strip())            
    for row in dmsoreader: 
        yield list(cast_method(e) for e in row)

def combine_dictionary(header, values):
    """Takes two lists: list-column-names and list-of-values and returns dictionary {column-name: value}."""
    return dict( (col,val) for col, val in izip(header, values) )

def filterout_dictionary(dct, keyset):
    """Takes dictionary dct and returns dictionary only with keys from keyset."""
    keyset = set(keyset)
    return dict( (k,v) for k,v in dct.iteritems() if k in keyset)
        

def download_file(URL=None):
    log.dbg("downloading url="+str(URL))
    import httplib2
    h = httplib2.Http(".cache")
    resp, content = h.request(URL, "GET")
    return content

def shellcmd(cmd):
    """Executes shell comands and returns tuple return-code, output, errors."""
    import subprocess, log
    log.dbg("shellcmd = "+cmd)
    process = subprocess.Popen(cmd, shell=True, stdout=subprocess.PIPE, stderr=subprocess.PIPE)
    out, err = process.communicate()
    return process.returncode, out, err


