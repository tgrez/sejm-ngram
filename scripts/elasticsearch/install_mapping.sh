#!/bin/bash
bin/plugin install elasticsearch/elasticsearch-analysis-stempel/2.6.0

curl -XDELETE "http://127.0.0.1:9200/sejmngram"

curl -XPUT "http://127.0.0.1:9200/sejmngram" -d '
{
  "settings": {
     "analysis": {
         "analyzer": {
             "custom_polish": {
                 "type": "polish",
                 "tokenizer": "standard",
                 "filter": ["lowercase", "polish_stem"]
             }
         }
     }
  }  
}'

curl -XPUT "http://127.0.0.1:9200/sejmngram/_mapping/wystapienie" -d '
{
   "wystapienie": {
      "properties": {
         "data": {
            "type": "date",
            "format": "dateOptionalTime"
         },
         "id": {
            "type": "string"
         },
         "partia": {
            "type": "string",
            "index": "not_analyzed"
         },
        "posel": {
            "type": "string",
            "index": "not_analyzed"
         },
         "stanowisko": {
            "type": "string",
            "index": "not_analyzed"
         },
         "tresc": {
            "type": "string",
            "analyzer": "custom_polish" 
         },
         "tytul": {
            "type": "string",
            "analyzer": "custom_polish" 
         }
      }
   }
}'


