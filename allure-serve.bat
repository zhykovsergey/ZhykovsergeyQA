@echo off
chcp 65001 >nul
echo ========================================
echo   Allure Report Server
echo ========================================
echo.

echo Проверка результатов...
if not exist "target\allure-results" (
    echo ❌ Результаты тестов не найдены в target/allure-results/
    echo.
    echo Запустите тесты сначала:
    echo   mvn test
    echo.
    pause
    exit /b 1
)

echo ✓ Результаты найдены
echo.

echo Запуск Allure сервера...
echo Отчет будет доступен по адресу: http://localhost:8080
echo.
echo Для остановки нажмите Ctrl+C
echo.

call mvn allure:serve

echo.
echo Allure сервер остановлен.
pause
