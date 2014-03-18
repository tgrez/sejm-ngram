#!/usr/bin/python

with open("daty.txt") as f:
    for line in f:
        print "\"" + line.replace(",\n","") + "\","
