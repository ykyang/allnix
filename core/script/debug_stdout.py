#!/usr/bin/env python

"""
Script to print counters to stdout for testing redirection to/from Java
"""

import sys
import time

if len(sys.argv) <= 2:
    print 'Usage: <num> <string> to print <string> to stdout <num> times'
    sys.exit(-1)

num = int(sys.argv[1])
str_ = sys.argv[2]

for ii in xrange(1,num+1,1):
    print '%s%d' % (str_, ii)
#    time.sleep(0.001)
