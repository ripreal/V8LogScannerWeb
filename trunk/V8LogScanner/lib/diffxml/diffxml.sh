#!/bin/bash
# This script will set up the java classpath with the required libraries
# then call diffxml with the given arguments.

#First find out where we are relative to the user dir
callPath=${0%/*}

if [[ -n "${callPath}" ]]; then
  callPath=${callPath}/
fi

java -cp ${callPath}lib/xpp3-1.1.3.4.C:${callPath}build:${callPath}lib/diffxml.jar org.diffxml.diffxml.DiffXML "$@"
