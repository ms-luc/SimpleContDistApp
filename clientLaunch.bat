
::everything is executed in background

::compile the client
@start /b /d .\"Client"\ cmd /c javac -d . *.java
@timeout 1 > nul

::run the client, with output
@start /b /d .\"Client"\ cmd /c java client.Client
