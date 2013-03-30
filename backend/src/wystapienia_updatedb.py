#!/usr/bin/python
# -*- coding: utf-8 -*-

import log
from config import *
from dbwrapper import *
import sys
from utils import *
from wystapienia_parse import *
from insertdb import *


PHP_SHELL_COMMAND               = "php"
PHP_SEJM_GETMAXID_FILENAME      = "sejm_getmaxid.php"

DATASETNAME_SEJM_WYSTAPIENIA    = "sejm_wystapienia" #sejmometr API dataset name
DBTABLE_SEJM_WYSTAPIENIA_NAME   = "sejm_wystapienia" #DB table name
PHP_SEJM_GET_TMPFILE            = "/tmp/wystapienia.csv"
PHP_SEJM_GET_FILENAME           = "sejm_get.php -d "+DATASETNAME_SEJM_WYSTAPIENIA+" -o "+PHP_SEJM_GET_TMPFILE
DBTABLE_SEJM_PACKAGE_SIZE       = 200 #how many should be downloaded and inserted at once

URL_SEJM_WYSTAPIENIA                    = "http://sejmometr.pl/sejm_wystapienia/" #URL where to find html 
DBTABLE_HTML_WYSTAPIENIA_NAME           = "html_wystapienia" #DB table tame
DBTABLE_HTML_WYSTAPIENIA_COL_ID         = "id" #DB table column id
DBTABLE_HTML_WYSTAPIENIA_COL_MEMBER     = "posel" #DB table column name
DBTABLE_HTML_WYSTAPIENIA_COL_POSITION   = "stanowisko" #DB table column name
DBTABLE_HTML_WYSTAPIENIA_COL_TEXT       = "tekst" #DB table column name



def _get_max_available_id_():
    """Returns max available (on the sejmometr.pl) id in wystapienia."""
    shellcmd_get_maxid = PHP_SHELL_COMMAND+" "+PHP_SEJM_GETMAXID_FILENAME+" -d "+DATASETNAME_SEJM_WYSTAPIENIA
    code, out, err = shellcmd(shellcmd_get_maxid)
    if not code == 0:
        log.error("Failure while running shellcmd_get_maxid="+str(shellcmd_get_maxid)+" error code="+str(code))
        sys.exit(-1)        
    maxid_line = list(line for line in out.split("\n") if line.strip().startswith("maxid"))[0]
    maxid = int(maxid_line.split("=")[1])
    return maxid



def update_html_wystapienia(db, start_id, last_id):
    """Retrieves and parses (html_)'wystapienia' from URL_SEJM_WYSTAPIENIA/id for id=start_id...last_id."""
    log.info("retrieving and inserting records of ids in range ["+str(start_id)+", "+str(last_id)+"] into "+str(db)+"...")
    db.begin() 
    for idd in xrange(start_id, last_id+1):
        
        #retrieve www code
        htmlcode = download_file(URL_SEJM_WYSTAPIENIA+str(idd))
        log.dbg("id="+str(idd)+" htmlcode="+str(htmlcode.replace("\n",""))[:100]+"...")        

        #extract fields
        parser = WystapienieHTMLParser()
        parser.feed(htmlcode)
        member, position, text = parser.extract_fields()  
        
        if len(text)>0 or len(position)>0 or len(member)>0:
            #pack as a record
            record = {  DBTABLE_HTML_WYSTAPIENIA_COL_ID: str(idd), 
                        DBTABLE_HTML_WYSTAPIENIA_COL_MEMBER: str(member),
                        DBTABLE_HTML_WYSTAPIENIA_COL_POSITION: str(position),
                        DBTABLE_HTML_WYSTAPIENIA_COL_TEXT: str(text)}

            #insert into db
            db.insert_record(record)
        else: log.dbg("empty data extracted. skipping")
    db.commit()
        

def update_sejm_wystapienia(start_id, last_id):
    """Retrieves and inserts (sejm_)'wystapienia' using sejm API for id=start_id...last_id."""                
    log.dbg("updating start_id="+str(start_id)+" last_id="+str(last_id))
    shellcmd_sejm_get = PHP_SHELL_COMMAND+" "+PHP_SEJM_GET_FILENAME+" -s "+str(start_id)+" -l "+str(last_id)
    code, out, err = shellcmd(shellcmd_sejm_get)
    if not code == 0:
        log.error("Failure while running shellcmd_sejm_get="+str(shellcmd_sejm_get)+" error code="+str(code))
        sys.exit(-1)        
    log.dbg("==========<sejm_get.php>==========\n"+out+"\n=========</sejm_get.php>==========")
    csv_insert_db( open(PHP_SEJM_GET_TMPFILE), DBTABLE_SEJM_WYSTAPIENIA_NAME )




if __name__=="__main__":
    log.info("The script automatically updates content of "+DBTABLE_SEJM_WYSTAPIENIA_NAME+
             " & "+DBTABLE_HTML_WYSTAPIENIA_NAME+" tables in default (see config.py) DB.")

    db_html = DBTableWrapper(DBTABLE_HTML_WYSTAPIENIA_NAME)
    db_sejm = DBTableWrapper(DBTABLE_SEJM_WYSTAPIENIA_NAME)

    try:
        while True:
        
            maxid = _get_max_available_id_()
            log.info("max available id of wystapienia = "+str(maxid))

            max_sejm_wystapienia_id = db_sejm.get_table_max_id()
            if max_sejm_wystapienia_id is None: max_sejm_wystapienia_id = 0
            log.info("max id in table '"+DBTABLE_SEJM_WYSTAPIENIA_NAME+"' = "+str(max_sejm_wystapienia_id))

            if maxid>max_sejm_wystapienia_id:
                log.info("retrieving missing entries in '"+DBTABLE_SEJM_WYSTAPIENIA_NAME+"'")
                update_sejm_wystapienia(max_sejm_wystapienia_id+1, min(maxid, max_sejm_wystapienia_id+DBTABLE_SEJM_PACKAGE_SIZE))
            
            max_html_wystapienia_id = db_html.get_table_max_id()
            if max_html_wystapienia_id is None: max_html_wystapienia_id = 0
            log.info("max id in table '"+DBTABLE_HTML_WYSTAPIENIA_NAME+"' = "+str(max_html_wystapienia_id))

            if max_sejm_wystapienia_id>max_html_wystapienia_id:
                log.info("retrieving missing entries in '"+DBTABLE_HTML_WYSTAPIENIA_NAME+"'")
                update_html_wystapienia(db_html, max_html_wystapienia_id+1, max_sejm_wystapienia_id)

            if max_sejm_wystapienia_id==maxid: 
                log.info("database is up to date.")
                break
            
            log.info("UPDATE NEXT ROUND=============================")
    except KeyboardInterrupt:
        log.error("interrupted by user...")
    


    
    
