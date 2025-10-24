@echo off
echo ========================================
echo 🚀 TestNewApi - Запуск всех тестов
echo ========================================
echo.

REM Проверяем наличие Java
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ Java не найдена! Установите Java 17+
    pause
    exit /b 1
)

REM Проверяем наличие Maven
mvn -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ Maven не найден! Установите Maven 3.6+
    pause
    exit /b 1
)

echo ✅ Java и Maven найдены
echo.

REM Очищаем предыдущие результаты
echo 🧹 Очистка предыдущих результатов...
call mvn clean

REM Запускаем все тесты
echo 🧪 Запуск всех тестов...
call mvn test -Dheadless=true

REM Проверяем результат
if %errorlevel% equ 0 (
    echo.
    echo ✅ Все тесты выполнены успешно!
    echo.
    echo 📊 Генерация Allure отчета...
    call mvn allure:report
    
    echo.
    echo 🎉 Отчет сгенерирован в target/allure-report/
    echo 📂 Откройте target/allure-report/index.html в браузере
) else (
    echo.
    echo ❌ Некоторые тесты завершились с ошибками
    echo 📋 Проверьте логи в target/surefire-reports/
)

echo.
echo ========================================
echo 🏁 Выполнение завершено
echo ========================================
pause
