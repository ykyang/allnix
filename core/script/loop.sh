#!/bin/bash
# Usage example:
# loop.sh 1000 "Line Number:"
# prints
# Line Number:1
# Line Number:2
# ...
# Line Number:1000
for i in `seq 1 ${1}`;
do
  echo ${2}${i}
done
