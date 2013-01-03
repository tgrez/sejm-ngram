#!/bin/bash

echo "################################################################"
echo "The script downloads and parses 'wystapienia' from http://sejmometr.pl/sejm_wystapienia/id"
echo "Three arguments expected: first-id last-id outputfile"
echo " [first-id last-id] gives range of ids to be downloaded "
echo " outputfile denotes CSV-file for results to be stored in"
echo "Example: sh wystapienia_get.sh 10 20 wystapienia10_20.txt"
echo "################################################################"


for i in $(seq $1 $2)
do
    echo "Downloading id=$i..."
    wget http://sejmometr.pl/sejm_wystapienia/$i -q -O /tmp/wystapienie.html
    echo -n $i >> $3
    echo -n "\t;\t" >> $3
    cat /tmp/wystapienie.html | python wystapienia_parse.py >> $3
done
