#!/usr/bin/python
# -*- coding: utf-8 -*-

from dbwrapper import *
from config import *
from utils import *
import string

def text_filterout(text, replace_with = ''):
    """Removes specific characters from text."""
    text_nopunct = (text).lower()    
    #text_nopunct = str(text).translate(string.maketrans("",""), string.punctuation)
    for c in string.punctuation:
        text_nopunct = text_nopunct.replace(c, replace_with)
    #text_nopunct = text_nopunct.translate(string.maketrans("",""), string.digits)
    #for c in string.digits:
    #    text_nopunct = text_nopunct.replace(c, replace_with) 

    text_nopunct = text_nopunct.replace('(', replace_with)
    text_nopunct = text_nopunct.replace(')', replace_with)
    text_nopunct = text_nopunct.replace('"', replace_with)
    text_nopunct = text_nopunct.replace('\'', replace_with)
    text_nopunct = text_nopunct.replace('~', replace_with)
    text_nopunct = text_nopunct.replace('`', replace_with)
    text_nopunct = text_nopunct.replace('\'', replace_with)
    text_nopunct = text_nopunct.replace('\t', replace_with)
    text_nopunct = text_nopunct.replace('\n', replace_with)
    return text_nopunct 

def build_ngrams(text, length=1):
    """Returns ngrams of specified length build out of text."""
    text = text_filterout(text, replace_with = ' ')
    words = text.split()
    ngrams = []
    for ix in xrange( len(words)-length+1 ):
        ngram = reduce(lambda w1,w2: w1+" "+w2, (words[j] for j in xrange(ix, ix+length)) )
        ngrams.append(ngram)
    return ngrams

def build_many_ngrams(text, lengths = [1,2,3]):
    """Returns list ngrams of specified lengths build out of text."""
    ngrams = []
    for length in lengths:
        ngrams.extend( build_ngrams(text, length) )
    return ngrams

#######################################################################################################

def _extract_not_processed_ids_(db_sejm, db_html):
    """Extract ids of not processed yet 'wystapienia' using both tables."""
    db_sejm.begin()
    ids1 = set(db_sejm.get_ids(DB_STATUS_NOT_PROCESSED_CODE))
    ids2 = set(db_html.get_ids(DB_STATUS_NOT_PROCESSED_CODE))
    not_processed_ids = sorted( list( ids1.intersection(ids2) ) )
    log.info(str(len(not_processed_ids))+" records to be processed")
    db_sejm.commit()
    return not_processed_ids

def _retrieve_wystapienie_(idd, db_sejm, db_html, db_posel_dict):
    """Constructs full record 'wystopienie' for given id. """
    wystapienie = db_html.get_record(idd)               #get half of the record 
    wystapienie.update( db_sejm.get_record(idd) )       #get second half of the record 
    wystapienie.pop(ID_COL_NAME)                        #remove column with wystapienie_id
    wystapienie[DBTABLE_WYSTAPIENIEID_COL_NAME] = idd   #insert wystapienie_id into proper column
    wystapienie[DBTABLE_POSELID_COL_NAME] = db_posel_dict.retrieve_id( wystapienie[DBTABLE_HTML_WYSTAPIENIA_COL_MEMBER] )
    return wystapienie

def _mark_wystapienie_processed_(idd, db_sejm, db_html):
    """Marks coresponding records in DB as already proccessed."""
    db_html.update(idd, STATUS_COL_NAME, DB_STATUS_PROCESSED_CODE)
    db_sejm.update(idd, STATUS_COL_NAME, DB_STATUS_PROCESSED_CODE)



def _store_ngrams_(wystapienie, db_ngram):
    """Extracts ngrams from wystapienie and inserts into db_ngram."""
    ngrams = build_many_ngrams(wystapienie[DBTABLE_HTML_WYSTAPIENIA_COL_TEXT], NGRAMS_LENGTHS) 
    log.dbg("inserting %i ngrams for wystopienie id=%i" % (len(ngrams),wystapienie[DBTABLE_WYSTAPIENIEID_COL_NAME]) )      
    for ngram in ngrams:        
        wystapienie[DBTABLE_NGRAMID_COL_NAME] = db_ngram_dict.retrieve_id(ngram)  #set ngram number (from dictionary)
        db_ngram.insert_record(wystapienie) #insert ngram occurrence    
    return len(ngrams)
        


if __name__=="__main__":
    log.set_output_level(log.LOG_LEVEL.INFO)
    log.info("The script loads 'wystapienia' from tables "+DBTABLE_SEJM_WYSTAPIENIA_NAME+
             " & "+DBTABLE_HTML_WYSTAPIENIA_NAME+" and updates table "+DBTABLE_NGRAMS_NAME)

    if NGRAMS_DBACCESS_AUTOSYNCH: 
        log.info("WARNING: Internal dictionaries will be synchronized continously with DB."+
                 " It may slow down processing."+
                 " For off-line mode see NGRAMS_DBACCESS_AUTOSYNCH in config.py")
    else:
        log.info("WARNING: Internal dictionaries are synchronized with DB only at the end of the script."+
                 " It may cause incoherence in data when executed in parallel manner."+
                 " For continuos synchronization see NGRAMS_DBACCESS_AUTOSYNCH in config.py")

    #######################################################################################################

    #Prepare tables
    transaction_man = DBTransactionManager(name="Ngrams Transaction Manager")
    db_html         = DBTableWrapper(DBTABLE_HTML_WYSTAPIENIA_NAME, transaction_manager=transaction_man)
    db_sejm         = DBTableWrapper(DBTABLE_SEJM_WYSTAPIENIA_NAME, transaction_manager=transaction_man)
    db_ngram        = DBTableWrapper(DBTABLE_NGRAMS_NAME, transaction_manager=transaction_man)
    db_ngram_dict   = DBDictionaryTable(DBTABLE_NGRAMS_DICT_NAME, 
                                        db_autosynchronization=NGRAMS_DBACCESS_AUTOSYNCH, 
                                        transaction_manager=transaction_man)
    db_posel_dict   = DBDictionaryTable(DBTABLE_POSEL_DICT_NAME, 
                                        db_autosynchronization=NGRAMS_DBACCESS_AUTOSYNCH, 
                                        transaction_manager=transaction_man)

    #Retrieve 'wystapienia' ids to be processed
    not_processed_ids = _extract_not_processed_ids_(db_sejm, db_html)

    #Update ngrams
    try:
        ngram_counter = 0
        for progress, idd in enumerate(not_processed_ids):
            if progress%10 == 0: log.info("%i records processed out ouf %i (%i ngram occurrences inserted, %i already known ngrams, current id=%i)" 
                                            % (progress, len(not_processed_ids), ngram_counter, db_ngram_dict.get_size(), idd))
            db_ngram.begin()  
            wystapienie = _retrieve_wystapienie_(idd, db_sejm, db_html, db_posel_dict)
            if wystapienie[STATUS_COL_NAME] <= DB_STATUS_NOT_PROCESSED_CODE:
                ngram_counter += _store_ngrams_(wystapienie, db_ngram)
                _mark_wystapienie_processed_(idd, db_sejm, db_html)
            else: log.warn("'wystapienie' of id=%i has changed its status in meantime (parallel manner execution?).")                
            db_ngram.commit()
            
    except KeyboardInterrupt:
        db_ngram.rollback()
        log.error("interrupted by user...")

    #Store dictionaries into DB:    
    while True:
        try:
            transaction_man.begin()
            db_ngram_dict.synchronize_db()
            db_posel_dict.synchronize_db()
            transaction_man.commit()
            break
        except KeyboardInterrupt:
            transaction_man.rollback()
            log.info("synchronizing! please wait!")


