#!/usr/bin/python
# -*- coding: utf-8 -*-

from HTMLParser import HTMLParser
import sys

from wystapienia_consts import *
 
from datetime import date

import log
import utils
from config import *


class WystapienieHTMLParser(HTMLParser):
    """
    Extracts main text out of HTML pages downloaded from http://sejmometr.pl/sejm_wystapienia/#id (#id=some number).
    """

    def __init__(self):
        HTMLParser.__init__(self)
        self.divcounter  = 0
        self.text        = "[WystapienieHTMLParser]NoTextFound!"
        self.textfound   = False
        self.dateparsed	 = date.min
        self.datefound	 = False

    def handle_starttag(self, tag, attrs):
        attrs = dict(attrs)

        if tag=="title":
            self.datefound	 = True
            self.dateparsed  = ""
        #print "Encountered a start tag:", tag," -> ", attrs
        if tag=="div" and attrs.get("class", "")=="_obj_main_div" and attrs.get("id", "")=="main_doc_cont":
            #print "Starting point found!"
            self.divcounter	 = 0
            self.textfound   = True
            self.text        = ""
        if tag=="div": self.divcounter = self.divcounter + 1
        

    def handle_endtag(self, tag):
        #print "Encountered an end tag :", tag
        if tag=="title": self.datefound = False
        if tag=="div": self.divcounter = self.divcounter - 1
        if tag=="div" and self.textfound and self.divcounter<=0: 
            #print "Ending point found!"
            self.textfound = False


    def handle_data(self, data):
        #print "Encountered some data  :", data[:50]+"..."
        if self.textfound: self.text = self.text + data
        if self.datefound: self.dateparsed = self.parse_date(data)


    def parse_date(self, data):
        # 13 is the index just after "Wystapienie "
        if not " r." in data:
            # print "date not found"
            return None
        dateparts = data[13:data.index(" r.")].split(" ")
        return date(int(dateparts[2]), self.parse_month(dateparts[1]), int(dateparts[0]))

        
    def parse_month(self, month):
        return MONTHS[month]

    def extract_fields(self):
        """Parses additional data and returns triple: member-of-parliament, his-position, what-he-said."""
        txt = self.text        
        posel, tekst = txt.split(" - ", 1)
        stanowisko = ""

        for prefix in POSITION_PREFIXES:
            if tekst.startswith(prefix):
                tekst = tekst[len(prefix):]
                stanowisko = prefix

        return posel.strip(), stanowisko.strip(), tekst.strip()



if __name__=="__main__":
    #setup configuration
    inputfile   = WYSTAPIENIA_INPUT_FILE
    outputfile  = WYSTAPIENIA_OUTPUT_FILE

    try:
        # read HTML code
        htmlcode = inputfile.read()   
    except KeyboardInterrupt:
        log.err("###############################################################################\n");
        log.err("The script reads from standard input source HTML-file (wystapienie) to be parsed and\n")    
        log.err("prints to standard output text extracted out of <div id=main_doc_cont class=_obj_main_div>.\n")
        log.err("###############################################################################\n");
        sys.exit(-2)

    try:
        if len(htmlcode)<5: 
            raise KeyboardInterrupt()

        # instantiate the parser and fed it some HTML
        parser = WystapienieHTMLParser()
        parser.feed(htmlcode)

        # print extracted text
        member, position, text = parser.extract_fields()        
        text = text.replace(CSV_SEPARATOR.strip(), CSV_REPLACEMENT) #Replace delimieters in source text
        outputfile.write(str(member)+CSV_SEPARATOR+str(position)+CSV_SEPARATOR+str(text)+"\n")

    except Exception as e:
        log.err("Exception: "+str(e)+" for htmlcode="+utils.text_filter(str(htmlcode)[:200])+"...\n")    
        sys.exit(-1)
    except:
        log.err("Unknown exception for htmlcode="+utils.text_filter(str(htmlcode)[:200])+"...\n")    
        sys.exit(-1)
                



