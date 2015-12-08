import json
from os import listdir
from os.path import isfile, join


def countWords(file):
	fileWithPath = mypath + file
	with open(fileWithPath) as data_file:    
	    data = json.load(data_file)
	
	wordCount = 0
	for wystapienie in data:
		if (wystapienie['tresc'] is not None):
			wordCount = wordCount + len(wystapienie['tresc'].split())
	return wordCount


datesCountDict = {}

for nr in range(1,5):
	mypath = "./wystapienia/" + str(nr) + "/"
	onlyfiles = [f for f in listdir(mypath) if isfile(join(mypath, f))]

	for (file) in onlyfiles:
		if (fileWithPath[-4:] == 'json'):
			datesCountDict[str(file[:-5])] = countWords(file)


print json.dumps(datesCountDict, sort_keys=True)