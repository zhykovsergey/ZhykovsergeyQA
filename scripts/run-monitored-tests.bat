@echo off
SETLOCAL ENABLEDELAYEDEXPANSION

REM Установка кодировки для корректного отображения кириллицы
chcp 65001 > nul

ECHO.
ECHO ==================================================
ECHO           Запуск мониторинг тестов
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

REM Запуск мониторинг тестов
ECHO 🚀 Запуск мониторинг тестов...
ECHO 📋 Запускаем:
ECHO    - MonitoredUiTest (тесты с мониторингом)
ECHO    - MultiBrowserTest (тесты для разных браузеров)
ECHO    - ImprovedStableTest (стабильные тесты)
ECHO    - ImprovedQualityTest (тесты качества)
ECHO.

REM Устанавливаем настройки мониторинга
set NOTIFICATIONS_ENABLED=true
set SLACK_WEBHOOK_URL=
set TELEGRAM_BOT_TOKEN=
set TELEGRAM_CHAT_ID=

ECHO 🔧 Настройки мониторинга:
ECHO    Уведомления: %NOTIFICATIONS_ENABLED%
ECHO    Slack Webhook: %SLACK_WEBHOOK_URL%
ECHO    Telegram Bot: %TELEGRAM_BOT_TOKEN%
ECHO    Telegram Chat: %TELEGRAM_CHAT_ID%
ECHO.

REM Запуск тестов с мониторингом
mvn test -Dtest="*Monitored*" -Dnotifications.enabled=%NOTIFICATIONS_ENABLED% -Dslack.webhook.url=%SLACK_WEBHOOK_URL% -Dtelegram.bot.token=%TELEGRAM_BOT_TOKEN% -Dtelegram.chat.id=%TELEGRAM_CHAT_ID% -Dallure.results.directory="%CD%\target\allure-results"
IF %ERRORLEVEL% NEQ 0 (
    ECHO ⚠️ Мониторинг тесты завершились с ошибками или падениями. Продолжаем генерацию отчета.
) ELSE (
    ECHO ✅ Все мониторинг тесты успешно завершены.
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

