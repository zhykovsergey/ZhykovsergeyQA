@echo off
echo 🚀 SimpleTestRunner - Test Runner with Annotations
echo.

if "%1"=="" (
    java SimpleTestRunner help
    goto :end
)

if "%1"=="scan" (
    java SimpleTestRunner scan
    goto :end
)

if "%1"=="list" (
    java SimpleTestRunner list %2
    goto :end
)

if "%1"=="run" (
    java SimpleTestRunner run %2
    goto :end
)

java SimpleTestRunner %*

:end
pause
