@echo off
chcp 65001 >nul
echo ========================================
echo   Allure Report Generator
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

echo Генерация статического отчета...
call mvn allure:report

if exist "target\allure-report\index.html" (
    echo.
    echo ✓ Отчет успешно сгенерирован!
    echo.
    echo 📁 Отчет сохранен в: target/allure-report/
    echo 🌐 Откройте файл: target/allure-report/index.html
    echo.
    
    set /p open_report="Открыть отчет в браузере? (y/n): "
    if /i "%open_report%"=="y" (
        start "" "target\allure-report\index.html"
    )
) else (
    echo ❌ Ошибка генерации отчета
)

echo.
pause
