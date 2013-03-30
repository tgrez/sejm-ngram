#!/bin/bash

echo "################################################################"
echo "The script downloads and parses 'wystapienia' from http://sejmometr.pl/sejm_wystapienia/id"
echo "Three arguments expected: first-id last-id outputfile"
echo " [first-id last-id] gives range of ids to be downloaded "
echo " outputfile denotes CSV-file for results to be stored in"
echo "Example: sh wystapienia_get.sh 10 20 wystapienia10_20.txt"
echo "################################################################"

rm -rf $3
echo "id\t;\tposel\t;\tstanowisko\t;\ttekst" > $3  #write CSV header
mkdir /tmp/wystapienia

for i in $(seq $1 $2)
do
    echo "-------------------------"
    echo "Downloading with id=$i..."
    if wget http://sejmometr.pl/sejm_wystapienia/$i -q -O /tmp/wystapienia/wystapienie$i.html; then

        echo "Parsing HTML with id=$i to CSV file..."
        echo -n "$i\t;\t" >> $3 # append id
        if python wystapienia_parse.py < /tmp/wystapienia/wystapienie$i.html >> $3; then #append parsed data
            echo "HTML for id=$i parsed properly."
        else
            echo "Failure while parsing HTML for id=$i. Breaking..."
            break
        fi

        rm -f /tmp/wystapienia/wystapienie$i.html

    else
        echo "Failure while downloading id=$i. Breaking..."
        break            
    fi

done


