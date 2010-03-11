#!/bin/sh

nxjc -cp src -d bin src/org/vu/aso/next/nxt/MindstormsBrains.java
echo Compiled...
nxjlink org.vu.aso.next.nxt.MindstormsBrains -cp bin -v -o bin/MindstormsBrains.nxj > debug.txt
echo Linked...
nxjupload -r -b -n $1 bin/MindstormsBrains.nxj