@echo off
call nxjc -cp src src\org\vu\aso\next\nxt\MindstormsBrains.java
echo Compiled...
call nxjlink org.vu.advselforg.nxt.MindstormsBrains -cp src -o MindstormsBrains.nxj
echo Linked...
call nxjupload -b -n JOEY MindstormsBrains.nxj