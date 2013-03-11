#!/usr/bin/python
# -*- coding: utf-8 -*-

import inspect
import sys

def info(txt):
    print "[INF][",inspect.stack()[1][3],"]",txt

def dbg(txt):
    print "[DBG][",inspect.stack()[1][3],"]",txt

def err(txt):
    if not txt.endswith("\n"): txt = txt+"\n"
    sys.stderr.write( "[ERR]["+str(inspect.stack()[1][3])+"]"+str(txt) )

def error(txt):
    if not txt.endswith("\n"): txt = txt+"\n"
    sys.stderr.write( "[ERR]["+str(inspect.stack()[1][3])+"]"+str(txt) )


def warn(txt):
    if not txt.endswith("\n"): txt = txt+"\n"
    sys.stderr.write( "[WRN]["+str(inspect.stack()[1][3])+"]"+str(txt) )

def warning(txt):
    if not txt.endswith("\n"): txt = txt+"\n"
    sys.stderr.write( "[WRN]["+str(inspect.stack()[1][3])+"]"+str(txt) )
