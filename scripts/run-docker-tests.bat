@echo off
echo ========================================
echo 🐳 TestNewApi - Запуск тестов в Docker
echo ========================================
echo.

REM Проверяем наличие Docker
docker --version >nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ Docker не найден! Установите Docker Desktop
    pause
    exit /b 1
)

echo ✅ Docker найден
echo.

REM Останавливаем и удаляем предыдущие контейнеры
echo 🧹 Очистка предыдущих контейнеров...
docker-compose down -v

REM Собираем и запускаем тесты
echo 🚀 Запуск тестов в Docker...
docker-compose up --build test-runner

REM Проверяем результат
if %errorlevel% equ 0 (
    echo.
    echo ✅ Тесты выполнены успешно!
    echo.
    echo 📊 Запуск Allure сервера...
    docker-compose up -d allure-generator allure-ui
    
    echo.
    echo 🎉 Allure отчеты доступны по адресам:
    echo 📊 Генератор: http://localhost:5050
    echo 🖥️ UI: http://localhost:5252
    echo.
    echo 💡 Для остановки сервисов выполните: docker-compose down
) else (
    echo.
    echo ❌ Тесты завершились с ошибками
    echo 📋 Проверьте логи выше
)

echo.
echo ========================================
echo 🏁 Выполнение завершено
echo ========================================
pause