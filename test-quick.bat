@echo off
chcp 65001 >nul
echo Быстрая проверка проекта...
echo.

echo [1/3] Компиляция...
call mvn clean compile test-compile -q
if %errorlevel% neq 0 (
    echo ❌ Ошибка компиляции!
    pause
    exit /b 1
)
echo ✓ Компиляция успешна

echo.
echo [2/3] Запуск простого API теста...
call mvn test -Dtest="api.SimpleApiTest" -q
if %errorlevel% neq 0 (
    echo ❌ API тест упал!
) else (
    echo ✓ API тест прошел
)

echo.
echo [3/3] Запуск простого UI теста...
call mvn test -Dtest="ui.SimpleUiTest" -q
if %errorlevel% neq 0 (
    echo ❌ UI тест упал!
) else (
    echo ✓ UI тест прошел
)

echo.
echo ========================================
echo Проверка завершена!
echo ========================================
pause



