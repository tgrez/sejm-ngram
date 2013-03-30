#!/usr/bin/python
# -*- coding: utf-8 -*-

import inspect
import sys


def enum(**enums):
    """Enumerator implementation."""
    return type('Enum', (), enums)

LOG_LEVEL = enum(DBG=0, INFO=1, WARN=2, ERR=3)
output_level = LOG_LEVEL.INFO


def set_output_level(new_output_level):
    """Sets logging filter level."""
    global output_level        
    output_level = new_output_level


def dbg(txt):
    """Log debug information."""
    if output_level <= LOG_LEVEL.DBG:
        print "[DBG][",inspect.stack()[1][3],"]",txt


def info(txt):
    """Log information."""
    if output_level <= LOG_LEVEL.INFO:
        print "[INF][",inspect.stack()[1][3],"]",txt


def warn(txt):
    """Log warning."""
    if output_level <= LOG_LEVEL.WARN:
        if not txt.endswith("\n"): txt = txt+"\n"
        sys.stderr.write( "[WRN][ "+str(inspect.stack()[1][3])+" ] "+str(txt) )

def warning(txt):
    """Log warning."""
    if output_level <= LOG_LEVEL.WARN:
        if not txt.endswith("\n"): txt = txt+"\n"
        sys.stderr.write( "[WRN][ "+str(inspect.stack()[1][3])+" ] "+str(txt) )



def err(txt):
    """Log error."""
    if output_level <= LOG_LEVEL.ERR:
        if not txt.endswith("\n"): txt = txt+"\n"
        sys.stderr.write( "[ERR][ "+str(inspect.stack()[1][3])+" ] "+str(txt) )

def error(txt):
    """Log error."""
    if output_level <= LOG_LEVEL.ERR:
        if not txt.endswith("\n"): txt = txt+"\n"
        sys.stderr.write( "[ERR][ "+str(inspect.stack()[1][3])+" ] "+str(txt) )

if __name__=="__main__":
    set_output_level(LOG_LEVEL.INFO)
    dbg("You can't see this.")    
    info("But can see this.")
    set_output_level(LOG_LEVEL.ERR)
    info("Now you can't see this.")
    error("But still you will see this.")

