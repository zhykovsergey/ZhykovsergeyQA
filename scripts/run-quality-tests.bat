@echo off
SETLOCAL ENABLEDELAYEDEXPANSION

REM Установка кодировки для корректного отображения кириллицы
chcp 65001 > nul

ECHO.
ECHO ==================================================
ECHO           Запуск тестов качества
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

REM Запуск unit тестов
ECHO 🧪 Запуск unit тестов...
ECHO 📋 Запускаем:
ECHO    - ValidationUtilsTest (тесты валидации)
ECHO    - LoggerUtilsTest (тесты логирования)
ECHO.

mvn test -Dtest="*ValidationUtilsTest,*LoggerUtilsTest" -Dallure.results.directory="%CD%\target\allure-results"
IF %ERRORLEVEL% NEQ 0 (
    ECHO ⚠️ Unit тесты завершились с ошибками. Продолжаем.
) ELSE (
    ECHO ✅ Unit тесты успешно завершены.
)
ECHO.

REM Запуск тестов качества
ECHO 🚀 Запуск тестов качества...
ECHO 📋 Запускаем:
ECHO    - ImprovedQualityTest (UI тесты с валидацией)
ECHO    - ImprovedQualityApiTest (API тесты с валидацией)
ECHO.

mvn test -Dtest="*ImprovedQuality*" -Dui.headless=false -Dallure.results.directory="%CD%\target\allure-results"
IF %ERRORLEVEL% NEQ 0 (
    ECHO ⚠️ Тесты качества завершились с ошибками или падениями. Продолжаем генерацию отчета.
) ELSE (
    ECHO ✅ Все тесты качества успешно завершены.
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

