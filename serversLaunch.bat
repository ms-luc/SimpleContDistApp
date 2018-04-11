@echo off
echo.
::everything is executed in background

:: compile and dns
:: run DNS servers
start /b /d .\"DNS"\ cmd /c javac -d . *.java
timeout 2 >nul
start /b /d .\"DNS"\ cmd /c java dns.LocalDNS
echo "Local DNS launched"
start /b /d .\"DNS"\ cmd /c java dns.HisDNS
echo "His DNS launched"
start /b /d .\"DNS"\ cmd /c java dns.HerDNS
echo "Her DNS launched"

:: compile and run his web server
start /b /d .\"Web Server His"\ cmd /c javac -d . *.java
timeout 2 >nul
start /b /d .\"Web Server His"\ cmd /c java serverhis.Server
echo "Server His launched"


timeout 2 >nul
echo.
echo Web Server, Local DNS, His DNS launched
