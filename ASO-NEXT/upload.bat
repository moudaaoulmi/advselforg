@echo off
call nxjc src\org\vu\advselforg\nxt\*.java
echo Compiled...
call nxjlink org.vu.advselforg.nxt.MindstormsBrains -cp src -o MindstormsBrains.nxj
echo Linked...
call nxjupload -b -n JOEY MindstormsBrains.nxj