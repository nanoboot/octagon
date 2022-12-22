#!/bin/sh

PRINT_TIME="date +%d.%m.%y_%H:%M:%S"
echo Starting Docker container at `${PRINT_TIME}`...

OCT_CONFPATH="/usr/local/oct.confpath"

export JAVA_OPTS="$JAVA_OPTS -Doct.datapath=${OCT_CONFPATH}"

catalina.sh run


