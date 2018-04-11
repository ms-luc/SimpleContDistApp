@echo off
echo.
::everything is executed in background

:: compile and dns
:: run DNS servers
start /b /d .\"DNS"\ cmd /c javac -d . *.java
timeout 2 >nul
start /b /d .\"DNS"\ cmd /c java dns.LocalDNS
echo Local DNS launched
start /b /d .\"DNS"\ cmd /c java dns.HisDNS
echo His DNS launched
start /b /d .\"DNS"\ cmd /c java dns.HerDNS
echo Her DNS launched

:: compile and run his web server
start /b /d .\"Web Server His"\ cmd /c javac -d . *.java
timeout 2 >nul
start /b /d .\"Web Server His"\ cmd /c java serverhis.Server
echo Server His launched

:: compile and run her web server
start /b /d .\"Content Server Her"\ cmd /c javac -d . *.java
timeout 2 >nul
start /b /d .\"Content Server Her"\ cmd /c java hercdn.Server
echo Server Her launched


timeout 2 >nul
echo.
