#!/usr/bin/python
# -*- coding: utf-8 -*-

from HTMLParser import HTMLParser
from datetime import date
import sys
import MySQLdb as mdb

class WystapienieHTMLParser(HTMLParser):
    """
    Extracts main text out of HTML pages downloaded from http://sejmometr.pl/sejm_wystapienia/#id (#id=some number).
    """
    divcounter  = 0
    text        = "NoTextFound!"
    textfound   = False
    datefound	= False
    dateparsed	= date.min


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
            print "date not found"
            return None
        dateparts = data[13:data.index(" r.")].split(" ")
        return date(int(dateparts[2]), self.parse_month(dateparts[1]), int(dateparts[0]))

        
    def parse_month(self, month):
        return {
            "stycznia"      :   1,
            "lutego"        :   2,
            "marca"         :   3,
            "kwietnia"      :   4,
            "maja"          :   5,
            "czerwca"       :   6,
            "lipca"         :   7,
            "sierpnia"      :   8,
            "września"      :   9,
            "października"  :   10,
            "listopada"	    :	11,
            "grudnia"	    :	12,
        }[month]

    
    def insert_ngrams(self):
        #TODO extract posel name
        self.db_insert(self.text.split())

    def db_insert(self, ngram):
        print "ngram: " + ", ".join(ngram)
        con = None
        try:
            con = mdb.connect("localhost", "testuser", "", "wystapienia");
            cur = con.cursor()
            con.query("SELECT VERSION()")
            result = con.use_result()
            print "MySQL version: %s" % result.fetch_row()[0]

            #cur.execute("INSERT INTO wystapienia VALUES (%s,%s)", (ngram, datefound))
            #con.commit()
                            
        except mdb.Error, e:
            print "Error %d: %s" % (e.args[0],e.args[1])
            sys.exit(1)

        finally:    
            if con:    
                con.close()


if __name__=="__main__":

    try:
        # read HTML code
        htmlcode = sys.stdin.read()   
    
        if len(htmlcode)<5: 
            raise KeyboardInterrupt()

        # instantiate the parser and fed it some HTML
        parser = WystapienieHTMLParser()
        parser.feed(htmlcode)

        # print extracted text
        txt = parser.text        
        print txt.replace("\n"," ").strip()
        print parser.dateparsed
        parser.insert_ngrams()

    except KeyboardInterrupt:
        sys.stderr.write("###############################################################################\n");
        sys.stderr.write("The script reads from standard input source HTML-file (wystapienie) to be parsed and\n")    
        sys.stderr.write("prints to standard output text extracted out of <div id=main_doc_cont class=_obj_main_div>.\n")
        sys.stderr.write("###############################################################################\n");

