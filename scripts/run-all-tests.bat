@echo off
echo ========================================
echo    Complete Test Suite Runner
echo ========================================
echo.

echo Starting Complete Test Suite...
echo.

REM Set environment variables
set MAVEN_OPTS=-Xmx4g -Xms2g
set PARALLEL_ENABLED=true
set THREAD_COUNT=4

echo Running API Tests...
mvn test -Dtest="api.*" ^
    -Dallure.results.directory=target/allure-results

echo.
echo Running UI Tests...
mvn test -Dtest="ui.*" ^
    -Dheadless=true ^
    -Dallure.results.directory=target/allure-results

echo.
echo Running E2E Tests...
mvn test -Dtest="e2e.*" ^
    -Dheadless=true ^
    -Dallure.results.directory=target/allure-results

echo.
echo Running Performance Tests...
mvn test -Dtest="performance.*" ^
    -Dperformance.enabled=true ^
    -Dallure.results.directory=target/allure-results

echo.
echo Running Security Tests...
mvn test -Dtest="security.*" ^
    -Dsecurity.enabled=true ^
    -Dallure.results.directory=target/allure-results

echo.
echo Running Data-Driven Tests...
mvn test -Dtest="datadriven.*" ^
    -Dallure.results.directory=target/allure-results

echo.
echo All tests completed!
echo.

REM Generate comprehensive Allure report
if exist "target\allure-results" (
    echo Generating comprehensive Allure report...
    allure generate target/allure-results -o target/allure-report --clean
    echo Allure report generated in target/allure-report
    echo.
    echo To view the report, run: allure serve target/allure-results
)

echo ========================================
echo    Complete Test Suite Finished
echo ========================================
pause