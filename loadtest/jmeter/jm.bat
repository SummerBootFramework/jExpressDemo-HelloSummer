set REPORT_NAME=%1_%2_%4users_in%5s_eachloop%6delay%7ms
set FOLDER=reports

md %FOLDER%
rd /S %FOLDER%\%REPORT_NAME%
del %FOLDER%\%REPORT_NAME%.txt

C:\Apps\apache-jmeter-5.5\bin\jmeter.bat -n -t %1 -Jserver=%2 -Jport=%3 -Jusers=%4 -JrampUp=%5 -Jloop=%6 -Jdelay=%7 -l "%FOLDER%/%REPORT_NAME%.log" -e -o "%FOLDER%/%REPORT_NAME%" 

rem 10000 users in 10 seconds each repeat 100 times deply 0: jm.bat ping.jmx 192.168.1.17 8211 10000 10 100 0