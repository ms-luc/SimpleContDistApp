@echo off
::everything is executed in background

::compile the client
start /b /d .\"Client"\ cmd /c javac -d . *.java

:: compile his web server
start /b /d .\"Web Server His"\ cmd /c javac -d . *.java

:: compile local dns server
start /b /d .\"DNS"\ cmd /c javac -d . *.java


timeout 2 > nul
echo.
echo Compilation DONE
echo.
