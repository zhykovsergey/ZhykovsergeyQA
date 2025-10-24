#!/bin/bash

echo "========================================"
echo "🐳 TestNewApi - Запуск тестов в Docker"
echo "========================================"
echo

# Проверяем наличие Docker
if ! command -v docker &> /dev/null; then
    echo "❌ Docker не найден! Установите Docker"
    exit 1
fi

echo "✅ Docker найден"
echo

# Останавливаем и удаляем предыдущие контейнеры
echo "🧹 Очистка предыдущих контейнеров..."
docker-compose down -v

# Собираем и запускаем тесты
echo "🚀 Запуск тестов в Docker..."
docker-compose up --build test-runner

# Проверяем результат
if [ $? -eq 0 ]; then
    echo
    echo "✅ Тесты выполнены успешно!"
    echo
    echo "📊 Запуск Allure сервера..."
    docker-compose up -d allure-generator allure-ui
    
    echo
    echo "🎉 Allure отчеты доступны по адресам:"
    echo "📊 Генератор: http://localhost:5050"
    echo "🖥️ UI: http://localhost:5252"
    echo
    echo "💡 Для остановки сервисов выполните: docker-compose down"
else
    echo
    echo "❌ Тесты завершились с ошибками"
    echo "📋 Проверьте логи выше"
fi

echo
echo "========================================"
echo "🏁 Выполнение завершено"
echo "========================================"