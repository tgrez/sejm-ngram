#!/usr/bin/env python

import os
import json
import sys
from elasticsearch import Elasticsearch

es = Elasticsearch()

if not (len(sys.argv) == 2):
    print "USAGE:", sys.argv[0], "<path to dir with .json files>"
    sys.exit(1)

for base, subdirs, files in os.walk(sys.argv[1]):
    for name in files:
        if name.endswith('.json'):
            path = base + '/' + name
            with open(path, 'r') as fp:
                dzien_wystapien_json = json.load(fp)
            for wystapienie in dzien_wystapien_json:
                es.index(index='sejmngram', doc_type='wystapienie', id=wystapienie['id'], body=wystapienie)
                print 'Processed wystapienie id:', wystapienie['id']
