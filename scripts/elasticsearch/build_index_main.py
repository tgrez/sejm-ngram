#!/usr/bin/env python

import os
import json
from elasticsearch import Elasticsearch

es = Elasticsearch()

for base, subdirs, files in os.walk('/home/tomasz/workspace/sejmAPI/jsonModifiedData/processed_psc/1'):
    for name in files:
        if name.endswith('.json'):
            path = base + '/' + name
            with open(path, 'r') as fp:
                dzien_wystapien_json = json.load(fp)
            for wystapienie in dzien_wystapien_json:
                es.index(index='sejmngram', doc_type='wystapienie', id=wystapienie['id'], body=wystapienie)
                print 'Processed wystapienie id:', wystapienie['id']
