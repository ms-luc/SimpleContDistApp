:: cr -> COMPILE AND RUN

@echo off

if "%1" == "" (
  echo This program compiles and executes your package on Windows
  echo syntax: cr 'file'
  goto :EOF
)

::javac -d . *.java 2> out.txt
::set /p var=<out.txt

::echo VAR: %var%

::if [%var%] == [/?] (  java localdns.%1 )

javac -d . *.java
java dns.%1
