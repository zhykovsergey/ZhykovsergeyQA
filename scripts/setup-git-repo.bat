@echo off
SETLOCAL ENABLEDELAYEDEXPANSION

REM Установка кодировки для корректного отображения кириллицы
chcp 65001 > nul

ECHO.
ECHO ==================================================
ECHO           Настройка Git репозитория
ECHO ==================================================
ECHO.

REM Проверяем, установлен ли Git
git --version >nul 2>&1
IF %ERRORLEVEL% NEQ 0 (
    ECHO ❌ Git не установлен. Пожалуйста, установите Git с https://git-scm.com/
    ECHO.
    ECHO После установки Git запустите этот скрипт снова.
    PAUSE
    GOTO :EOF
)

ECHO ✅ Git установлен
git --version
ECHO.

REM Проверяем, инициализирован ли уже репозиторий
IF EXIST ".git" (
    ECHO ⚠️ Git репозиторий уже инициализирован
    ECHO.
    ECHO Текущий статус:
    git status --porcelain
    ECHO.
    ECHO Хотите продолжить? (y/n)
    set /p continue="Введите y для продолжения: "
    IF /i NOT "!continue!"=="y" (
        ECHO Операция отменена
        GOTO :EOF
    )
) ELSE (
    ECHO 🔧 Инициализация Git репозитория...
    git init
    IF %ERRORLEVEL% NEQ 0 (
        ECHO ❌ Ошибка при инициализации Git репозитория
        GOTO :EOF
    )
    ECHO ✅ Git репозиторий инициализирован
)

ECHO.
ECHO 📝 Настройка Git конфигурации...
ECHO.

REM Настройка пользователя (если не настроен)
git config user.name >nul 2>&1
IF %ERRORLEVEL% NEQ 0 (
    ECHO Настройка пользователя Git:
    set /p username="Введите ваше имя: "
    git config user.name "!username!"
)

git config user.email >nul 2>&1
IF %ERRORLEVEL% NEQ 0 (
    ECHO Настройка email Git:
    set /p email="Введите ваш email: "
    git config user.email "!email!"
)

ECHO.
ECHO 📋 Текущая конфигурация Git:
git config --list | findstr user
ECHO.

REM Добавление всех файлов
ECHO 📁 Добавление файлов в репозиторий...
git add .
IF %ERRORLEVEL% NEQ 0 (
    ECHO ❌ Ошибка при добавлении файлов
    GOTO :EOF
)

ECHO ✅ Файлы добавлены
ECHO.

REM Проверяем статус
ECHO 📊 Статус репозитория:
git status --short
ECHO.

REM Создание первого commit
ECHO 💾 Создание первого commit...
git commit -m "feat: initial commit - professional test automation framework

- Add comprehensive test automation framework
- Include API, UI, E2E, and data-driven tests
- Add Allure reporting and monitoring
- Add CI/CD pipeline with GitHub Actions
- Add notification system (Slack, Telegram, Email)
- Add multi-browser support (Chrome, Firefox, Edge, Safari)
- Add performance monitoring and metrics
- Add comprehensive documentation and scripts"
IF %ERRORLEVEL% NEQ 0 (
    ECHO ❌ Ошибка при создании commit
    GOTO :EOF
)

ECHO ✅ Первый commit создан
ECHO.

REM Создание основной ветки
ECHO 🌿 Создание основной ветки...
git branch -M main
IF %ERRORLEVEL% NEQ 0 (
    ECHO ❌ Ошибка при создании основной ветки
    GOTO :EOF
)

ECHO ✅ Основная ветка 'main' создана
ECHO.

REM Информация о следующих шагах
ECHO ==================================================
ECHO           Следующие шаги
ECHO ==================================================
ECHO.
ECHO 1. Создайте репозиторий на GitHub:
ECHO    - Перейдите на https://github.com/new
ECHO    - Назовите репозиторий: TestNewApi
ECHO    - Сделайте его публичным или приватным
ECHO    - НЕ инициализируйте с README (у нас уже есть)
ECHO.
ECHO 2. Подключите локальный репозиторий к GitHub:
ECHO    git remote add origin https://github.com/YOUR_USERNAME/TestNewApi.git
ECHO.
ECHO 3. Загрузите код на GitHub:
ECHO    git push -u origin main
ECHO.
ECHO 4. Настройте GitHub Actions:
ECHO    - Перейдите в Settings → Actions → General
ECHO    - Включите Actions для этого репозитория
ECHO.
ECHO 5. Настройте GitHub Pages для Allure отчетов:
ECHO    - Перейдите в Settings → Pages
ECHO    - Выберите Source: GitHub Actions
ECHO.
ECHO 6. Настройте уведомления (опционально):
ECHO    - Добавьте Slack webhook в Secrets
ECHO    - Добавьте Telegram bot token в Secrets
ECHO.

REM Создание скрипта для загрузки на GitHub
ECHO 📝 Создание скрипта для загрузки на GitHub...
(
echo @echo off
echo SETLOCAL ENABLEDELAYEDEXPANSION
echo.
echo chcp 65001 ^> nul
echo.
echo ECHO.
echo ECHO ==================================================
echo ECHO           Загрузка на GitHub
echo ECHO ==================================================
echo ECHO.
echo.
echo set /p github_url="Введите URL вашего GitHub репозитория: "
echo.
echo ECHO 🔗 Подключение к GitHub репозиторию...
echo git remote add origin "%%github_url%%"
echo IF %%ERRORLEVEL%% NEQ 0 ^(
echo     ECHO ⚠️ Удаленный репозиторий уже существует, обновляем...
echo     git remote set-url origin "%%github_url%%"
echo ^)
echo.
echo ECHO 📤 Загрузка кода на GitHub...
echo git push -u origin main
echo IF %%ERRORLEVEL%% NEQ 0 ^(
echo     ECHO ❌ Ошибка при загрузке на GitHub
echo     ECHO Проверьте URL и права доступа
echo     PAUSE
echo     GOTO :EOF
echo ^)
echo.
echo ECHO ✅ Код успешно загружен на GitHub!
echo ECHO.
echo ECHO 🌐 Ваш репозиторий доступен по адресу:
echo ECHO %%github_url%%
echo ECHO.
echo ECHO 📊 Allure отчеты будут доступны по адресу:
echo ECHO %%github_url%%/actions
echo ECHO.
echo PAUSE
) > scripts/upload-to-github.bat

ECHO ✅ Скрипт upload-to-github.bat создан
ECHO.

ECHO ==================================================
ECHO           Завершено
ECHO ==================================================
ECHO.
ECHO ✅ Git репозиторий настроен успешно!
ECHO.
ECHO 📋 Что было сделано:
ECHO    - Инициализирован Git репозиторий
ECHO    - Настроена конфигурация пользователя
ECHO    - Добавлены все файлы проекта
ECHO    - Создан первый commit
ECHO    - Создана основная ветка 'main'
ECHO    - Создан скрипт для загрузки на GitHub
ECHO.
ECHO 🚀 Для загрузки на GitHub запустите:
ECHO    scripts/upload-to-github.bat
ECHO.

PAUSE
ENDLOCAL

