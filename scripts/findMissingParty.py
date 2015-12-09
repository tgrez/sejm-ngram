import json
from os import listdir
from os.path import isfile, join


def printIfPartyMissing(file):
	fileWithPath = mypath + file
	# print fileWithPath

	if (fileWithPath[-4:] == 'json'):
		with open(fileWithPath) as data_file:    
		    data = json.load(data_file)
		

		for wystapienie in data:
			if (wystapienie['partia'] is None):
				print "is none: " + fileWithPath + " " + wystapienie['stanowisko'] + " " + wystapienie["posel"]






mypath = "./wystapienia/4/"
onlyfiles = [f for f in listdir(mypath) if isfile(join(mypath, f))]





for (file) in onlyfiles:
	a = 5
	printIfPartyMissing(file)






# j = json.loads('{"one" : "1", "two" : "2", "three" : "3"}')
# print j['two']