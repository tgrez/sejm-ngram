#!/usr/bin/python

import subprocess

proc = subprocess.Popen("php script.php", shell=True, stdout=subprocess.PIPE)
script_response = proc.stdout.read()

print script_response
