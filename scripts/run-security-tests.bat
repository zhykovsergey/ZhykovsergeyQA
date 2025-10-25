@echo off
echo ========================================
echo    Security Tests Runner
echo ========================================
echo.

echo Starting Security Tests...
echo.

REM Set environment variables
set SECURITY_ENABLED=true
set DEBUG_MODE=false

REM Run security tests
mvn test -Dtest="security.*" ^
    -Dsecurity.enabled=true ^
    -Dheadless=true ^
    -Dallure.results.directory=target/allure-results

echo.
echo Security tests completed!
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
echo    Security Tests Finished
echo ========================================
pause
