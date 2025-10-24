@echo off
chcp 65001 >nul
echo ========================================
echo   Запуск тестов TestNewApi
echo ========================================
echo.

:menu
echo Выберите тип тестов:
echo.
echo [1] Все тесты
echo [2] API тесты
echo [3] UI тесты (видимый браузер)
echo [4] E2E тесты
echo [5] Data-Driven тесты
echo [6] Простой API тест
echo [7] Простой UI тест
echo [8] Генерировать Allure отчет
echo [9] Очистить и собрать проект
echo [0] Выход
echo.
set /p choice="Введите номер: "

if "%choice%"=="1" goto all_tests
if "%choice%"=="2" goto api_tests
if "%choice%"=="3" goto ui_tests
if "%choice%"=="4" goto e2e_tests
if "%choice%"=="5" goto datadriven_tests
if "%choice%"=="6" goto simple_api
if "%choice%"=="7" goto simple_ui
if "%choice%"=="8" goto allure_report
if "%choice%"=="9" goto clean_build
if "%choice%"=="0" goto end
goto menu

:all_tests
echo.
echo Запуск всех тестов...
call mvn clean test
goto after_test

:api_tests
echo.
echo Запуск API тестов...
call mvn test -Dtest="api.*Test"
goto after_test

:ui_tests
echo.
echo Запуск UI тестов...
call mvn test -Dtest="ui.*Test"
goto after_test

:e2e_tests
echo.
echo Запуск E2E тестов...
call mvn test -Dtest="e2e.*Test"
goto after_test

:datadriven_tests
echo.
echo Запуск Data-Driven тестов...
call mvn test -Dtest="datadriven.*Test"
goto after_test

:simple_api
echo.
echo Запуск простого API теста...
call mvn test -Dtest="api.SimpleApiTest"
goto after_test

:simple_ui
echo.
echo Запуск простого UI теста...
call mvn test -Dtest="ui.SimpleUiTest"
goto after_test

:allure_report
echo.
echo ========================================
echo   Allure Reports Menu
echo ========================================
echo.
echo [1] Генерировать и открыть отчет (serve)
echo [2] Генерировать отчет (generate)
echo [3] Очистить результаты
echo [4] Показать статистику
echo [5] Назад в главное меню
echo.
set /p allure_choice="Выберите действие: "

if "%allure_choice%"=="1" goto allure_serve
if "%allure_choice%"=="2" goto allure_generate
if "%allure_choice%"=="3" goto allure_clean
if "%allure_choice%"=="4" goto allure_stats
if "%allure_choice%"=="5" goto menu
goto allure_report

:allure_serve
echo.
echo Генерация и открытие Allure отчета...
call mvn allure:serve
goto menu

:allure_generate
echo.
echo Генерация Allure отчета...
call mvn allure:report
echo.
echo ✓ Отчет сгенерирован в target/allure-report/
echo.
pause
goto menu

:allure_clean
echo.
echo Очистка Allure результатов...
if exist "target\allure-results" (
    rmdir /s /q "target\allure-results"
    echo ✓ Результаты очищены
) else (
    echo ℹ Результаты не найдены
)
if exist "target\allure-report" (
    rmdir /s /q "target\allure-report"
    echo ✓ Отчеты очищены
) else (
    echo ℹ Отчеты не найдены
)
echo.
pause
goto menu

:allure_stats
echo.
echo Статистика Allure результатов...
if exist "target\allure-results" (
    echo ✓ Результаты найдены в target/allure-results/
    dir /b "target\allure-results" | find /c ".json" > temp_count.txt
    set /p result_count=<temp_count.txt
    del temp_count.txt
    echo Количество тестов: %result_count%
) else (
    echo ❌ Результаты не найдены
)
echo.
pause
goto menu

:clean_build
echo.
echo Очистка и сборка проекта...
call mvn clean compile test-compile
echo.
echo ✓ Проект собран успешно!
pause
goto menu

:after_test
echo.
echo ========================================
echo   Тесты завершены!
echo ========================================
echo.
set /p show_report="Показать Allure отчет? (y/n): "
if /i "%show_report%"=="y" (
    call mvn allure:serve
)
goto menu

:end
echo.
echo До свидания!
exit /b 0



