#!/usr/bin/python
# -*- coding: utf-8 -*-

import log
from config import *
from dbwrapper import *
import sys
from utils import *
from wystapienia_parse import *
from insertdb import *

def _get_max_available_id_():
    """Returns max possible id in wystapienia."""
    shellcmd_get_maxid = PHP_SHELL_COMMAND+" "+PHP_SEJM_GETMAXID_FILENAME+" "+DATASETNAME_SEJM_WYSTAPIENIA
    code, out, err = shellcmd(shellcmd_get_maxid)
    if not code == 0:
        log.error("Failure while running shellcmd_get_maxid="+shellcmd_get_maxid+" error code="+code)
        sys.exit(-1)        
    maxid_line = list(line for line in out.split("\n") if line.strip().startswith("maxid"))[0]
    maxid = int(maxid_line.split("=")[1])
    return maxid



def update_html_wystapienia(db, start_id, last_id, urlbase=URL_SEJM_WYSTAPIENIA):
    """Retrieves and parses (html_)'wystapienia' from urlbase/id for id=start_id...last_id."""
    log.dbg("updating start_id="+str(start_id)+" last_id="+str(last_id))
    db.begin() 
    for idd in xrange(start_id, last_id+1):
        
        #retrieve www code
        htmlcode = download_file(urlbase+str(idd))
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
            db.insert_record(DBTABLE_HTML_WYSTAPIENIA_NAME, record)
        else: log.dbg("empty data extracted. omitting")
    db.commit()
        

def update_sejm_wystapienia(db, start_id, last_id):
    """Retrieves and inserts (sejm_)'wystapienia' using sejm API for id=start_id...last_id."""                
    log.dbg("updating start_id="+str(start_id)+" last_id="+str(last_id))
    shellcmd_sejm_get = PHP_SHELL_COMMAND+" "+PHP_SEJM_GET_FILENAME+" -s "+str(start_id)+" -l "+str(last_id)
    code, out, err = shellcmd(shellcmd_sejm_get)
    if not code == 0:
        log.error("Failure while running shellcmd_sejm_get="+shellcmd_sejm_get+" error code="+code)
        sys.exit(-1)        
    log.dbg("==========<sejm_get.php>==========\n"+out+"\n=========</sejm_get.php>==========")
    csv_insert_db(open(PHP_SEJM_GET_TMPFILE ), DBTABLE_SEJM_WYSTAPIENIA_NAME )

if __name__=="__main__":
    log.info("The script automatically updates content of "+DBTABLE_SEJM_WYSTAPIENIA_NAME+" & "+DBTABLE_HTML_WYSTAPIENIA_NAME+" tables in default (see config.py) DB.")

    db = DBWrapper()

    while True:

        maxid = _get_max_available_id_()
        log.info("max available id of wystapienia = "+str(maxid))

        max_sejm_wystapienia_id = db.get_table_max_id(DBTABLE_SEJM_WYSTAPIENIA_NAME)
        if max_sejm_wystapienia_id is None: max_sejm_wystapienia_id = 0
        log.info("max id in table "+DBTABLE_SEJM_WYSTAPIENIA_NAME+" = "+str(max_sejm_wystapienia_id))

        if maxid>max_sejm_wystapienia_id:
            log.info("retrieving missing entries in "+DBTABLE_SEJM_WYSTAPIENIA_NAME)
            update_sejm_wystapienia(db,  max_sejm_wystapienia_id+1, min(maxid, max_sejm_wystapienia_id+DBTABLE_SEJM_PACKAGE_SIZE))
        
        max_html_wystapienia_id = db.get_table_max_id(DBTABLE_HTML_WYSTAPIENIA_NAME)
        if max_html_wystapienia_id is None: max_html_wystapienia_id = 0
        log.info("max id in table "+DBTABLE_HTML_WYSTAPIENIA_NAME+" = "+str(max_html_wystapienia_id))

        if max_sejm_wystapienia_id>max_html_wystapienia_id:
            log.info("retrieving missing entries in "+DBTABLE_HTML_WYSTAPIENIA_NAME)
            update_html_wystapienia(db, max_html_wystapienia_id+1, max_sejm_wystapienia_id)

        if max_sejm_wystapienia_id==maxid: 
            log.info("database is up to date.")
            break
        

    


    
    
