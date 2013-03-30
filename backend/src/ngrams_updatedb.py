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



if __name__=="__main__":

    db = DBWrapper()

    ###########################################################

    sejm_wystapienia_colnames = db.get_column_names(DBTABLE_SEJM_WYSTAPIENIA_NAME)
    log.info("sejm_wystapienia_colnames = "+str(sejm_wystapienia_colnames))    

    html_wystapienia_colnames = db.get_column_names(DBTABLE_HTML_WYSTAPIENIA_NAME)
    log.info("html_wystapienia_colnames = "+str(html_wystapienia_colnames))

    for text_col_ix in xrange(len(html_wystapienia_colnames)): #in which column full text is found
        if html_wystapienia_colnames[text_col_ix] == DBTABLE_HTML_WYSTAPIENIA_COL_TEXT: break
    log.info("text_col_ix = "+str(text_col_ix))

    ngrams_colnames = db.get_column_names(DBTABLE_NGRAMS_NAME)
    log.info("ngrams_colnames = "+str(ngrams_colnames))

    ngrams_coltypes = db.get_column_types(DBTABLE_NGRAMS_NAME)
    log.info("ngrams_coltypes = "+str(ngrams_coltypes))

    shared_colnames = set(html_wystapienia_colnames).union(sejm_wystapienia_colnames).intersection(ngrams_colnames)
    log.info("shared_colnames = "+str(shared_colnames))

    ###########################################################

    sejm_wystapienia_not_processed_ids = db.get_ids(DBTABLE_SEJM_WYSTAPIENIA_NAME, DB_STATUS_NOT_PROCESSED_CODE)
    log.info(str(len(sejm_wystapienia_not_processed_ids))+" records is to be processed in table "+DBTABLE_SEJM_WYSTAPIENIA_NAME)

    html_wystapienia_not_processed_ids = db.get_ids(DBTABLE_SEJM_WYSTAPIENIA_NAME, DB_STATUS_NOT_PROCESSED_CODE)
    log.info(str(len(html_wystapienia_not_processed_ids))+" records is to be processed in table "+DBTABLE_SEJM_WYSTAPIENIA_NAME)

    ids = sorted( list( set(sejm_wystapienia_not_processed_ids).intersection(html_wystapienia_not_processed_ids) ) )
    log.info(str(len(ids))+" records is to be processed in common (both tables)")

    ###########################################################

    for idd in ids:
        #build record data
        sejm_tuple = db.get_record(DBTABLE_SEJM_WYSTAPIENIA_NAME, idd) #get half of the record 
        sejm_record = filterout_dictionary( combine_dictionary(sejm_wystapienia_colnames, sejm_tuple), shared_colnames) 

        html_tuple = db.get_record(DBTABLE_HTML_WYSTAPIENIA_NAME, idd) #get second half of the record 
        record = filterout_dictionary( combine_dictionary(html_wystapienia_colnames, html_tuple), shared_colnames) 

        record.update(sejm_record)        
        record.pop(DBTABLE_NGRAMS_COL_ID) #remove id column that should be assigned automatically
        record = map_values_types(record, ngrams_coltypes)  #prepare record values to be stored into database

        #extract ngrams
        ngrams = build_many_ngrams(html_tuple[text_col_ix], NGRAMS_LENGTHS)

        db.begin()
        #insert ngrams
        for ngram in ngrams:        
            record.update({DBTABLE_NGRAMS_COL_NGRAM: ngram}) #update record with ngram value
            db.insert_record(DBTABLE_NGRAMS_NAME, record)    #store into db

        #TODO mark coresponding records in DB as already proccessed
        db.commit()
        
    ###########################################################
    
