#!/bin/sh

nxjc -cp src -d bin src/org/vu/aso/next/nxt/MindstormsBrains.java
echo Compiled...
nxjlink org.vu.aso.next.nxt.MindstormsBrains -cp bin -o bin/MindstormsBrains.nxj
echo Linked...
nxjupload -r -b -n JOEY bin/MindstormsBrains.nxj