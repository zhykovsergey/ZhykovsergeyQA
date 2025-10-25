# TestNewApi - Запуск тестов в Docker
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "🐳 TestNewApi - Запуск тестов в Docker" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Проверяем наличие Docker
try {
    $dockerVersion = docker --version
    Write-Host "✅ Docker найден: $dockerVersion" -ForegroundColor Green
} catch {
    Write-Host "❌ Docker не найден! Установите Docker Desktop" -ForegroundColor Red
    Read-Host "Нажмите Enter для выхода"
    exit 1
}

Write-Host ""

# Останавливаем и удаляем предыдущие контейнеры
Write-Host "🧹 Очистка предыдущих контейнеров..." -ForegroundColor Yellow
docker-compose down -v

# Собираем и запускаем тесты
Write-Host "🚀 Запуск тестов в Docker..." -ForegroundColor Yellow
docker-compose up --build test-runner

# Проверяем результат
if ($LASTEXITCODE -eq 0) {
    Write-Host ""
    Write-Host "✅ Тесты выполнены успешно!" -ForegroundColor Green
    Write-Host ""
    Write-Host "📊 Запуск Allure сервера..." -ForegroundColor Yellow
    docker-compose up -d allure-generator allure-ui
    
    Write-Host ""
    Write-Host "🎉 Allure отчеты доступны по адресам:" -ForegroundColor Green
    Write-Host "📊 Генератор: http://localhost:5050" -ForegroundColor Cyan
    Write-Host "🖥️ UI: http://localhost:5252" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "💡 Для остановки сервисов выполните: docker-compose down" -ForegroundColor Yellow
} else {
    Write-Host ""
    Write-Host "❌ Тесты завершились с ошибками" -ForegroundColor Red
    Write-Host "📋 Проверьте логи выше" -ForegroundColor Yellow
}

Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "🏁 Выполнение завершено" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Read-Host "Нажмите Enter для выхода"

