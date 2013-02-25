# coding=UTF-8
import csv, sys
import random, time

"""
 Generates CSV file with random data.
 CSV contains three headers: Ngram, Data, Partia.
 Ngram - some string containing ngram, whatever.
 Data - in format YYYY-MM-DD HH:MM
 Partia - Some random partia out of PIS/PO/RP/SLD/PSL
 Ngram, Data, Partia
 Polska jest;1990-05-01 12:34;PIS
 Balcerowicz musi odejść;1991-06-01 15:34;PIS
 
"""


def strTimeProp(start, end, format, prop):
    """Get a time at a proportion of a range of two formatted times.

    start and end should be strings specifying times formated in the
    given format (strftime-style), giving an interval [start, end].
    prop specifies how a proportion of the interval to be taken after
    start.  The returned time will be in the specified format.
    """

    stime = time.mktime(time.strptime(start, format))
    etime = time.mktime(time.strptime(end, format))

    ptime = stime + prop * (etime - stime)

    return time.strftime(format, time.localtime(ptime))


def randomDate(start, end, prop):
    return strTimeProp(start, end, '%d-%m-%Y %H:%M', prop)



if __name__ == "__main__":


	outputFileName = "fake_data.csv"
	sampleFileName = "sample-ngrams.txt"
	listNgrams = None
	fakeNGramsNr = 10
	startDateFormatted = "1-1-2008 00:00" # follows python "time" format '%d-%m-%Y %H:%M'
	endDateFormatted = "1-1-2009 00:00"

	parties = ["PIS","PO","RP","SLD","PSL"]

	# read the list of sample ngrams
	listNgrams = open(sampleFileName).read().splitlines()

	nrSampleNgrams = len(listNgrams)
	nrParties = len(parties)

	with open(outputFileName, 'wb') as csvfile:
		outputWriter = csv.writer(csvfile, delimiter=';')
 		
 		# write header row
 		outputWriter.writerow(['Ngram', 'Data', 'Partia'])

 		# write rest of the rows
		for i in range(0, fakeNGramsNr):
			#generate random nr from list
			chosenNgram = listNgrams[random.randint(0, nrSampleNgrams-1)]

			#generate fake date within gicen scope
			# format: YYYY-MM-DD HH:MM
			fakeDate = randomDate(startDateFormatted, endDateFormatted, random.random())

			# randomly choose one of the parties: 
			fakeParty = parties[random.randint(0, nrParties-1)]

			outputWriter.writerow([chosenNgram, fakeDate, fakeParty])


	"""
	with open('eggs.csv', 'wb') as csvfile:
	    spamwriter = csv.writer(csvfile, delimiter=' ',
				    quotechar='|', quoting=csv.QUOTE_MINIMAL)
	    spamwriter.writerow(['Spam'] * 5 + ['Baked Beans'])
	    spamwriter.writerow(['Spam', 'Lovely Spam', 'Wonderful Spam'])
	"""


