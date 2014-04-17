#!/bin/bash

echo "################################################################"
echo "The script downloads 'wystapienia' from http://sejmometr.pl/sejm_wystapienia/id"
echo "Three arguments expected: first-id last-id outputfile"
echo " [first-id last-id] gives range of ids to be downloaded "
echo " outputfile denotes json file prefix for download result to be stored in"
echo " default value for outputfile is wystapienie_"
echo "Example:  sh wystapienia_get.sh 10 20 wystapienie_"
echo "          then the first file will be saved as wystapienie_10.json"
echo "################################################################"

timestamp=$(date +%Y%m%d_%H%M%S)
dir=./wystapienia_$timestamp
default_filename="wystapienie_"
filename=${3:-$default_filename}

mkdir $dir


for i in $(seq $1 $2)
do
    fullpath=$dir/$filename$i.json
    echo "--------------------------------------------------------------------"
    echo "Downloading with id=$i to $fullpath"
    wget http://sejmometr.pl/sejm_wystapienia/$i.json -q -O $fullpath

done


