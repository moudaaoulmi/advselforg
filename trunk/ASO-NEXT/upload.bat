@echo off
call nxjc -cp src -d bin_nxt src\org\vu\aso\next\nxt\MindstormsBrains.java
echo Compiled...
call nxjlink org.vu.aso.next.nxt.MindstormsBrains -cp bin_nxt -o bin_nxt\MindstormsBrains.nxj
echo Linked...
call nxjupload -r -b -n JOEY bin_nxt\MindstormsBrains.nxj