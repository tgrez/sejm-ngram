#! /usr/bin/python

import os, shutil, sys

if len(sys.argv) < 3:
    maindir = ".tmp"
    print
for arg in sys.argv:
    print arg
for root, dirs, files in os.walk("."):
    if not os.path.exists(maindir):
        os.makedirs(maindir)
    if "kadencja" in root:
        for f in files:
            if ("header.xml" in f or "text_structure.xml" in f) and not "PSC_header.xml" in f:
                relativedir = root.split('/')[-1]
                pathdir = maindir + "/" + relativedir
                if not os.path.exists(pathdir):
                    os.makedirs(pathdir)
                fullpath = root + '/' + f
                path = pathdir + "/" + f
                shutil.copy(fullpath, path)