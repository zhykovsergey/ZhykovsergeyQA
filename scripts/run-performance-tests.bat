@echo off
echo ========================================
echo    Performance Tests Runner
echo ========================================
echo.

echo Starting Performance Tests...
echo.

REM Set environment variables
set MAVEN_OPTS=-Xmx2g -Xms1g
set PERFORMANCE_ENABLED=true

REM Run performance tests
mvn test -Dtest="performance.*" ^
    -Dheadless=true ^
    -Dperformance.enabled=true ^
    -Dthread.count=2 ^
    -Dallure.results.directory=target/allure-results

echo.
echo Performance tests completed!
echo.

REM Generate Allure report
if exist "target\allure-results" (
    echo Generating Allure report...
    allure generate target/allure-results -o target/allure-report --clean
    echo Allure report generated in target/allure-report
    echo.
    echo To view the report, run: allure serve target/allure-results
)

echo ========================================
echo    Performance Tests Finished
echo ========================================
pause
