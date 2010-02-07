#!/bin/sh

nxjc -cp src src/org/vu/aso/next/nxt/MindstormsBrains.java
echo Compiled...
nxjlink org.vu.aso.next.nxt.MindstormsBrains -cp src -o MindstormsBrains.nxj
echo Linked...
nxjupload -b -n JOEY MindstormsBrains.nxj