@echo off
echo.
::everything is executed in background

:: compile and run local dns server
start /b /d .\"Local DNS"\ cmd /c javac -d . *.java
timeout 2 >nul
start /b /d .\"Local DNS"\ cmd /c java localdns.DNSServer
echo "Local DNS launched"

:: compile and run his web server
start /b /d .\"Web Server His"\ cmd /c javac -d . *.java
timeout 2 >nul
start /b /d .\"Web Server His"\ cmd /c java serverhis.Server
echo "Server His launched"


timeout 2 >nul
echo.
echo Web Server and Local DNS launched
