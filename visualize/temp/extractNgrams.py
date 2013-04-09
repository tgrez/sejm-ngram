import sys


if _name_=="__main__":
   try: l = int(sys.argv[1])
   except:
       sys.stderr.write("The script reads from stdin file with table of ngrams"+
                        " and prints out these ngrams of selected length.\n"); 
       sys.stderr.write("Argument expected: ngrams length!\n"); 
       sys.exit(-1);

   f = sys.stdin
   o = sys.stdout


   for line in f.xreadlines():
       if not line.startswith("|"): continue
       ngram = line.split("|")[1]
       if ngram.strip() == "ngrams": continue
       if len(ngram.strip().split())==l: o.write(ngram.strip()+"\n")