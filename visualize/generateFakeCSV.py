# coding=UTF-8
import csv, sys

"""
 Generates CSV file with random data.
 CSV contains three headers: Ngram, Data, Partia.
 Ngram - some string containing ngram, whatever.
 Data - in format YYYY-MM-DD HH:MM
 Partia - Some random partia out of PIS/PO/RP/SLD/PSL
 Ngram, Data, Partia
 Polska jest;1990-05-01 12:34;PIS
 Balcerowicz musi odejść;1991-06-01 15:34, PIS
 
"""

if __name__ == "__main__":
	print "Hello, World!"	
	for arg in sys.argv: #(1)
		print arg
	"""
	with open('eggs.csv', 'wb') as csvfile:
	    spamwriter = csv.writer(csvfile, delimiter=' ',
				    quotechar='|', quoting=csv.QUOTE_MINIMAL)
	    spamwriter.writerow(['Spam'] * 5 + ['Baked Beans'])
	    spamwriter.writerow(['Spam', 'Lovely Spam', 'Wonderful Spam'])
	"""
