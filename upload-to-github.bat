@echo off
SETLOCAL ENABLEDELAYEDEXPANSION

REM Установка кодировки для корректного отображения кириллицы
chcp 65001 > nul

ECHO.
ECHO ==================================================
ECHO           Загрузка TestNewApi на GitHub
ECHO ==================================================
ECHO.

REM Проверяем, установлен ли Git
git --version >nul 2>&1
IF %ERRORLEVEL% NEQ 0 (
    ECHO ❌ Git не установлен!
    ECHO.
    ECHO 📥 Установите Git:
    ECHO    1. Перейдите на https://git-scm.com/download/win
    ECHO    2. Скачайте и установите Git for Windows
    ECHO    3. Перезапустите командную строку
    ECHO    4. Запустите этот скрипт снова
    ECHO.
    PAUSE
    GOTO :EOF
)

ECHO ✅ Git установлен
git --version
ECHO.

REM Проверяем, инициализирован ли репозиторий
IF NOT EXIST ".git" (
    ECHO 🔧 Инициализация Git репозитория...
    git init
    IF %ERRORLEVEL% NEQ 0 (
        ECHO ❌ Ошибка при инициализации Git репозитория
        GOTO :EOF
    )
    ECHO ✅ Git репозиторий инициализирован
)

REM Настройка пользователя (если не настроен)
git config user.name >nul 2>&1
IF %ERRORLEVEL% NEQ 0 (
    ECHO.
    ECHO 👤 Настройка пользователя Git:
    set /p username="Введите ваше имя: "
    git config user.name "!username!"
)

git config user.email >nul 2>&1
IF %ERRORLEVEL% NEQ 0 (
    ECHO.
    ECHO 📧 Настройка email Git:
    set /p email="Введите ваш email: "
    git config user.email "!email!"
)

ECHO.
ECHO 📁 Добавление файлов в репозиторий...
git add .
IF %ERRORLEVEL% NEQ 0 (
    ECHO ❌ Ошибка при добавлении файлов
    GOTO :EOF
)

ECHO ✅ Файлы добавлены
ECHO.

REM Проверяем, есть ли изменения для commit
git diff --cached --quiet
IF %ERRORLEVEL% EQU 0 (
    ECHO ℹ️ Нет изменений для commit
) ELSE (
    ECHO 💾 Создание commit...
    git commit -m "feat: professional test automation framework

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
    ECHO ✅ Commit создан
)

REM Создание основной ветки
git branch -M main
IF %ERRORLEVEL% NEQ 0 (
    ECHO ❌ Ошибка при создании основной ветки
    GOTO :EOF
)

ECHO ✅ Основная ветка 'main' создана
ECHO.

REM Запрос URL GitHub репозитория
ECHO.
ECHO 🔗 Подключение к GitHub репозиторию...
ECHO.
ECHO 📋 Сначала создайте репозиторий на GitHub:
ECHO    1. Перейдите на https://github.com/new
ECHO    2. Назовите репозиторий: TestNewApi
ECHO    3. Сделайте его Public (для портфолио)
ECHO    4. НЕ инициализируйте с README
ECHO    5. Нажмите "Create repository"
ECHO.
set /p github_url="Введите URL вашего GitHub репозитория: "

REM Подключение к удаленному репозиторию
git remote add origin "%github_url%" 2>nul
IF %ERRORLEVEL% NEQ 0 (
    ECHO ⚠️ Удаленный репозиторий уже существует, обновляем...
    git remote set-url origin "%github_url%"
)

ECHO ✅ Подключение к GitHub репозиторию настроено
ECHO.

REM Загрузка на GitHub
ECHO 📤 Загрузка кода на GitHub...
git push -u origin main
IF %ERRORLEVEL% NEQ 0 (
    ECHO ❌ Ошибка при загрузке на GitHub
    ECHO.
    ECHO 🔍 Возможные причины:
    ECHO    - Неверный URL репозитория
    ECHO    - Нет прав доступа к репозиторию
    ECHO    - Репозиторий не создан на GitHub
    ECHO    - Проблемы с аутентификацией
    ECHO.
    ECHO 💡 Решения:
    ECHO    1. Проверьте URL репозитория
    ECHO    2. Убедитесь, что репозиторий создан на GitHub
    ECHO    3. Проверьте права доступа
    ECHO    4. Настройте аутентификацию GitHub
    ECHO.
    PAUSE
    GOTO :EOF
)

ECHO.
ECHO ==================================================
ECHO           🎉 УСПЕШНО ЗАГРУЖЕНО!
ECHO ==================================================
ECHO.
ECHO ✅ Код успешно загружен на GitHub!
ECHO.
ECHO 🌐 Ваш репозиторий доступен по адресу:
ECHO    %github_url%
ECHO.
ECHO 📊 GitHub Actions будет запущен автоматически
ECHO 📈 Allure отчеты будут доступны в Actions
ECHO.
ECHO 🎯 Следующие шаги:
ECHO    1. Перейдите в Settings → Actions → General
ECHO    2. Включите Actions для этого репозитория
ECHO    3. Перейдите в Settings → Pages
ECHO    4. Выберите Source: GitHub Actions
ECHO    5. Allure отчеты будут доступны по адресу:
ECHO       %github_url%/actions
ECHO.
ECHO 🏆 Поздравляем! Ваш проект готов для портфолио!
ECHO.

PAUSE
ENDLOCAL

