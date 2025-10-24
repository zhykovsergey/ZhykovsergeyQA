@echo off
SETLOCAL ENABLEDELAYEDEXPANSION

REM Установка кодировки для корректного отображения кириллицы
chcp 65001 > nul

ECHO.
ECHO ==================================================
ECHO           Запуск тестов во всех браузерах
ECHO ==================================================
ECHO.

REM Очистка предыдущих результатов Allure
ECHO 🧹 Очистка предыдущих результатов Allure...
call mvn allure:clean > nul
IF %ERRORLEVEL% NEQ 0 (
    ECHO ❌ Ошибка при очистке Allure результатов.
    GOTO :EOF
)
ECHO ✅ Предыдущие результаты Allure очищены.
ECHO.

REM Массив браузеров для тестирования
set BROWSERS=CHROME FIREFOX EDGE
set TOTAL_BROWSERS=3
set CURRENT_BROWSER=0

REM Переменные для статистики
set TOTAL_TESTS=0
set PASSED_TESTS=0
set FAILED_TESTS=0

ECHO 🚀 Запуск тестов во всех браузерах...
ECHO 📋 Браузеры для тестирования: %BROWSERS%
ECHO.

REM Запуск тестов в каждом браузере
for %%B in (%BROWSERS%) do (
    set /a CURRENT_BROWSER+=1
    ECHO.
    ECHO ==================================================
    ECHO           Тестирование в %%B (%CURRENT_BROWSER%/%TOTAL_BROWSERS%)
    ECHO ==================================================
    ECHO.
    
    ECHO 🚀 Запуск тестов в %%B...
    
    REM Запуск тестов в текущем браузере
    mvn test -Dtest="*MultiBrowser*" -Dbrowser.type=%%B -Dui.headless=false -Dallure.results.directory="%CD%\target\allure-results"
    
    REM Проверяем результат
    if %ERRORLEVEL% equ 0 (
        ECHO ✅ Тесты в %%B успешно завершены.
        set /a PASSED_TESTS+=1
    ) else (
        ECHO ⚠️ Тесты в %%B завершились с ошибками.
        set /a FAILED_TESTS+=1
    )
    
    set /a TOTAL_TESTS+=1
    
    ECHO.
    ECHO 📊 Статистика после %%B:
    ECHO    Успешно: %PASSED_TESTS%
    ECHO    С ошибками: %FAILED_TESTS%
    ECHO    Всего: %TOTAL_TESTS%
    ECHO.
    
    REM Пауза между браузерами
    ECHO ⏳ Пауза 5 секунд перед следующим браузером...
    timeout /t 5 /nobreak > nul
)

ECHO.
ECHO ==================================================
ECHO           Итоговая статистика
ECHO ==================================================
ECHO.
ECHO 📊 Результаты тестирования:
ECHO    Всего браузеров: %TOTAL_TESTS%
ECHO    Успешно: %PASSED_TESTS%
ECHO    С ошибками: %FAILED_TESTS%
ECHO.

REM Генерация Allure отчета
ECHO 📊 Генерация итогового Allure отчета...
call mvn allure:report
IF %ERRORLEVEL% NEQ 0 (
    ECHO ❌ Ошибка при генерации Allure отчета.
    GOTO :EOF
)
ECHO ✅ Allure отчет сгенерирован в папке target/allure-report.
ECHO.

REM Открытие Allure отчета в браузере
ECHO 🌐 Открытие Allure отчета в браузере...
call allure serve "%CD%\target\allure-results"
IF %ERRORLEVEL% NEQ 0 (
    ECHO ❌ Ошибка при открытии Allure отчета. Убедитесь, что Allure Commandline установлен и добавлен в PATH.
    ECHO    Вы можете открыть отчет вручную, перейдя в папку target/allure-report и открыв index.html.
) ELSE (
    ECHO ✅ Allure отчет открыт в браузере.
)
ECHO.

ECHO ==================================================
ECHO           Завершено
ECHO ==================================================
ECHO.

PAUSE
ENDLOCAL
