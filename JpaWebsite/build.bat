@echo off
setlocal enabledelayedexpansion

REM ============================================================
REM   CBSE JPA Website - Build Script
REM   System: Windows + JDK 1.6 + WebLogic 12.1.1
REM ============================================================

set JDK=C:\Oracle\Middleware\jdk160_29
set WLS=C:\Oracle\Middleware\wlserver_12.1

set PROJ=D:\java\PROJECT\JpaWebsite
set SRC=%PROJ%\src\my
set WEB=%PROJ%\web
set CLS=%WEB%\WEB-INF\classes
set LIB=%WEB%\WEB-INF\lib

set PATH=%JDK%\bin;%PATH%

echo ============================================================
echo  1. Cleaning previous build
echo ============================================================
if exist "%CLS%\my"          rmdir /s /q "%CLS%\my"
if exist "%PROJ%\JpaWebsite.war" del  "%PROJ%\JpaWebsite.war"
mkdir "%CLS%\my"
mkdir "%CLS%\META-INF"

echo.
echo ============================================================
echo  2. Compiling Java sources
echo ============================================================
"%JDK%\bin\javac" -d "%CLS%" ^
  -cp "%LIB%\*;%WLS%\server\lib\servlet-api.jar" ^
  "%SRC%\*.java"

if errorlevel 1 (
    echo.
    echo *** COMPILATION FAILED - see errors above ***
    pause
    exit /b 1
)

echo.
echo ============================================================
echo  3. Compiled .class files
echo ============================================================
dir /b "%CLS%\my\*.class"

echo.
echo ============================================================
echo  4. Copying resources
echo ============================================================
if not exist "%CLS%\META-INF\persistence.xml" (
    echo ERROR: persistence.xml missing at %CLS%\META-INF\
    pause
    exit /b 1
)
echo persistence.xml: OK

if not exist "%WEB%\WEB-INF\web.xml" (
    echo ERROR: web.xml missing at %WEB%\WEB-INF\
    pause
    exit /b 1
)
echo web.xml: OK

if not exist "%WEB%\WEB-INF\weblogic.xml" (
    echo ERROR: weblogic.xml missing at %WEB%\WEB-INF\
    pause
    exit /b 1
)
echo weblogic.xml: OK

echo.
echo  JARs in lib:
dir /b "%LIB%\*.jar"

echo.
echo ============================================================
echo  5. Building WAR
echo ============================================================
cd /d "%WEB%"
"%JDK%\bin\jar" -cf "%PROJ%\JpaWebsite.war" ^
    index.html ^
    login.html ^
    register.html ^
    addStudent.html ^
    deleteStudent.html ^
    updateStudent.html ^
    searchStudent.html ^
    displayAll.html ^
    WEB-INF

if errorlevel 1 (
    echo *** WAR BUILD FAILED ***
    pause
    exit /b 1
)

echo.
echo ============================================================
echo  6. WAR contents
echo ============================================================
"%JDK%\bin\jar" -tf "%PROJ%\JpaWebsite.war"

echo.
echo ============================================================
echo  BUILD COMPLETE
echo  WAR: %PROJ%\JpaWebsite.war
echo ============================================================
echo.
echo  Next steps:
echo    1. Start WebLogic
echo    2. Admin Console - Deployments - Install
echo    3. Select %PROJ%\JpaWebsite.war
echo    4. Test: http://localhost:7002/JpaWebsite/
echo.
endlocal
pause