@echo off
SETLOCAL ENABLEDELAYEDEXPANSION

REM Установка кодировки для корректного отображения кириллицы
chcp 65001 > nul

ECHO.
ECHO ==================================================
ECHO           Запуск тестов в Firefox
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

REM Запуск тестов в Firefox
ECHO 🚀 Запуск тестов в Firefox...
ECHO 📋 Запускаем:
ECHO    - MultiBrowserTest (тесты для разных браузеров)
ECHO    - ImprovedStableTest (стабильные тесты)
ECHO    - ImprovedQualityTest (тесты качества)
ECHO.

REM Устанавливаем тип браузера и режим
set BROWSER_TYPE=FIREFOX
set HEADLESS_MODE=false

mvn test -Dtest="*MultiBrowser*" -Dbrowser.type=%BROWSER_TYPE% -Dui.headless=%HEADLESS_MODE% -Dallure.results.directory="%CD%\target\allure-results"
IF %ERRORLEVEL% NEQ 0 (
    ECHO ⚠️ Тесты в Firefox завершились с ошибками или падениями. Продолжаем генерацию отчета.
) ELSE (
    ECHO ✅ Все тесты в Firefox успешно завершены.
)
ECHO.

REM Генерация Allure отчета
ECHO 📊 Генерация Allure отчета...
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

