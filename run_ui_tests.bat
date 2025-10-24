@echo off
echo 🖥️ Запуск всех UI тестов...
echo.

mvn test -Dtest="ui_tests.tests.YouTubeSingleAuthTest,ui_tests.tests.YouTubeUITest,ui_tests.tests.YouTubeExactPasswordTest,ui_tests.tests.GoogleAuthTest,ui_tests.tests.GoogleSearchUITest,ui_tests.tests.GitHubUITest,ui_tests.tests.SimpleUITest"

echo.
echo ✅ UI тесты завершены!
