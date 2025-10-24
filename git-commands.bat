@echo off
REM Простой скрипт для работы с Git

if "%1"=="add" (
    "C:\Program Files\Git\bin\git.exe" add .
    goto :eof
)

if "%1"=="commit" (
    "C:\Program Files\Git\bin\git.exe" commit -m "fix: resolve compilation errors in UI and E2E tests"
    goto :eof
)

if "%1"=="push" (
    "C:\Program Files\Git\bin\git.exe" push origin main
    goto :eof
)

if "%1"=="status" (
    "C:\Program Files\Git\bin\git.exe" status
    goto :eof
)

if "%1"=="remote" (
    "C:\Program Files\Git\bin\git.exe" remote add origin %2
    goto :eof
)

echo Usage: git-commands.bat [add|commit|push|status|remote URL]