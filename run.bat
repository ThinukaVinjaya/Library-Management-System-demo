@echo off
title Library Management System
echo ====================================================
echo Starting Library Management System...
echo ====================================================

if not exist "bin" mkdir bin

echo Compiling source files...
powershell -NoProfile -Command "javac -d bin (Get-ChildItem -Path src -Recurse -Filter *.java).FullName"

if %ERRORLEVEL% neq 0 (
    echo.
    echo Compilation failed! Please check your Java installation.
    pause
    exit /b %ERRORLEVEL%
)

echo Launching GUI...
start javaw -cp bin com.library.Main
echo Application started successfully!
