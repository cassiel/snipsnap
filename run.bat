@echo off
rem $Id$
rem SnipSnap Startscript for Win32 by Bernd 'Nexos' Dutkowski <bernd@dutkowski.net>
rem Adapted to new version by Matthias L. Jugel <leo@mud.de>
rem Version 0.0.1 2002/07/29
rem Version 0.0.2 2002/09/18

set BASE=.

if "%JAVA_HOME%" == "" goto missing_java_home
goto ok_java_home
:missing_java_home
echo "Please set JAVA_HOME environment variable!"
echo "A Java SDK of at least version 1.3 is required!"
exit
:ok_java_home

if exist "%JAVA_HOME%/lib/tools.jar" goto ok_tools_jar
echo "$JAVA_HOME/lib/tools.jar not found, cannot compile jsp files"
echo "Make sure tools.jar from the Java SDK is in the classpath!"
exit
:ok_tools_jar

if exist "%BASE%/lib/snipsnap.jar" goto ok_snipsnap_jar
echo "lib/snipsnap.jar missing, please compile application first"
exit
:ok_snipsnap_jar

rem put classpath together
set CLASSPATH=%CLASSPATH%;lib/xmlrpc-1.1.jar;lib/jakarta.jar;lib/javax.servlet.jar;lib/mckoidb.jar;lib/org.apache.jasper.jar;lib/org.mortbay.jetty.jar;lib/jdbcpool.jar;lib/lucene-1.2.jar;lib/jython.jar;%JAVA_HOME%/lib/tools.jar

if $1 == "stop" set cmdline="-admin shutdown"

rem execute application server
%JAVA_HOME%/bin/java -cp %CLASSPATH%;lib/snipsnap.jar org.snipsnap.server.AppServer %cmdline% 2> server.log
