# 🚀 TestNewApi - Professional Test Automation Framework

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![Maven](https://img.shields.io/badge/Maven-3.8+-blue.svg)](https://maven.apache.org/)
[![JUnit](https://img.shields.io/badge/JUnit-5-green.svg)](https://junit.org/junit5/)
[![Selenium](https://img.shields.io/badge/Selenium-4.0+-red.svg)](https://selenium.dev/)
[![RestAssured](https://img.shields.io/badge/RestAssured-5.0+-purple.svg)](https://rest-assured.io/)
[![Allure](https://img.shields.io/badge/Allure-2.29+-pink.svg)](https://allure.qameta.io/)

## 📋 **ОПИСАНИЕ ПРОЕКТА**

**TestNewApi** - это профессиональный фреймворк для автоматизации тестирования, включающий:

- 🧪 **API тестирование** с RestAssured
- 🌐 **UI тестирование** с Selenium WebDriver
- 🔄 **E2E тестирование** (API → UI, UI → API)
- 📊 **Data-driven тестирование** с JUnit 5
- 📈 **Allure отчеты** с детальной аналитикой
- 🚀 **CI/CD интеграция** с GitHub Actions
- 📢 **Система уведомлений** (Slack, Telegram, Email)
- 📊 **Мониторинг и метрики** производительности
- 🌐 **Кроссплатформенное тестирование** (Chrome, Firefox, Edge, Safari)

## 🏗️ **АРХИТЕКТУРА ПРОЕКТА**

```
src/test/java/
├── api/                    # API тесты
├── ui/                     # UI тесты
├── e2e/                    # E2E тесты
├── datadriven/             # Data-driven тесты
├── utils/                  # Утилиты и хелперы
├── pages/                  # Page Object Model
├── models/                 # Модели данных
├── config/                 # Конфигурация
├── filters/                # Фильтры
├── steps/                  # Шаги тестов
├── exceptions/             # Кастомные исключения
├── unit/                   # Unit тесты
└── performance/            # Performance тесты

src/test/resources/
├── schemas/                # JSON схемы
├── data/                   # Тестовые данные
├── responses/              # Примеры ответов
├── config.properties       # Конфигурация
└── notifications.properties # Настройки уведомлений

scripts/                    # Скрипты запуска
.github/workflows/          # CI/CD pipeline
```

## 🚀 **БЫСТРЫЙ СТАРТ**

### **Предварительные требования:**
- Java 17+
- Maven 3.8+
- Chrome/Firefox/Edge браузер
- Allure Commandline (опционально)

### **Клонирование и запуск:**
```bash
# Клонируем репозиторий
git clone https://github.com/YOUR_USERNAME/TestNewApi.git
cd TestNewApi

# Компилируем проект
mvn clean compile

# Запускаем все тесты
mvn test

# Генерируем Allure отчет
mvn allure:report
allure serve target/allure-results
```

## 🧪 **ТИПЫ ТЕСТОВ**

### **API Тесты:**
```bash
# Запуск API тестов
mvn test -Dtest="*Api*"

# Запуск с конкретным браузером
mvn test -Dtest="*Api*" -Dbrowser.type=CHROME
```

### **UI Тесты:**
```bash
# Запуск UI тестов
mvn test -Dtest="*Ui*"

# Запуск в headless режиме
mvn test -Dtest="*Ui*" -Dui.headless=true
```

### **E2E Тесты:**
```bash
# Запуск E2E тестов
mvn test -Dtest="*E2E*"
```

### **Data-driven Тесты:**
```bash
# Запуск data-driven тестов
mvn test -Dtest="*DataDriven*"
```

## 📊 **ОТЧЕТЫ И АНАЛИТИКА**

### **Allure отчеты:**
```bash
# Генерация отчета
mvn allure:report

# Просмотр отчета
allure serve target/allure-results

# Генерация статического отчета
allure generate target/allure-results -o allure-report
```

### **Метрики и мониторинг:**
- 📈 Системные метрики (CPU, память, потоки)
- 🧪 Метрики тестов (время выполнения, успешность)
- 🌐 Метрики браузеров (время загрузки, производительность)
- 📊 Метрики API (время ответа, количество запросов)

## 🔧 **КОНФИГУРАЦИЯ**

### **Основные настройки (config.properties):**
```properties
# API настройки
api.base.url=https://jsonplaceholder.typicode.com
api.timeout=30

# UI настройки
ui.headless=false
ui.browser.type=CHROME
ui.timeout=10

# Allure настройки
allure.results.directory=target/allure-results
allure.report.directory=target/allure-report
```

### **Настройки уведомлений (notifications.properties):**
```properties
# Включение уведомлений
notifications.enabled=true

# Slack настройки
slack.webhook.url=YOUR_SLACK_WEBHOOK
slack.channel=#test-automation

# Telegram настройки
telegram.bot.token=YOUR_BOT_TOKEN
telegram.chat.id=YOUR_CHAT_ID
```

## 🚀 **CI/CD PIPELINE**

### **GitHub Actions:**
- ✅ Автоматическая сборка и тестирование
- ✅ Матричное тестирование в разных браузерах
- ✅ Генерация Allure отчетов
- ✅ Деплой отчетов на GitHub Pages
- ✅ Уведомления в Slack/Telegram

### **Запуск pipeline:**
```bash
# Ручной запуск с параметрами
gh workflow run advanced-ci.yml -f browser_type=CHROME -f test_suite=ALL
```

## 📱 **УВЕДОМЛЕНИЯ**

### **Поддерживаемые каналы:**
- 📢 **Slack** - Webhook интеграция
- 📱 **Telegram** - Bot API
- 📧 **Email** - SMTP поддержка
- 🔗 **Webhook** - Универсальные уведомления

### **Типы уведомлений:**
- ✅ Результаты тестов
- ❌ Ошибки и падения
- 📊 Метрики производительности
- 🚀 Статус CI/CD pipeline

## 🛠️ **РАЗРАБОТКА**

### **Создание нового теста:**
```java
@Epic("API Testing")
@Feature("New Feature")
public class NewApiTest extends BaseApiTest {
    
    @Test
    @DisplayName("New API test")
    public void testNewFeature() {
        // Ваш тест
    }
}
```

### **Создание Page Object:**
```java
public class NewPage extends BasePage {
    
    public NewPage(WebDriver driver) {
        super(driver);
    }
    
    // Методы страницы
}
```

## 📚 **ДОКУМЕНТАЦИЯ**

- 📖 [Архитектура проекта](docs/architecture.md)
- 🧪 [Руководство по тестированию](docs/testing-guide.md)
- 🚀 [CI/CD настройка](docs/cicd-setup.md)
- 📊 [Allure отчеты](docs/allure-reports.md)
- 🔧 [Конфигурация](docs/configuration.md)

## 🤝 **ВКЛАД В ПРОЕКТ**

1. Fork репозитория
2. Создайте feature branch (`git checkout -b feature/amazing-feature`)
3. Commit изменения (`git commit -m 'Add amazing feature'`)
4. Push в branch (`git push origin feature/amazing-feature`)
5. Откройте Pull Request

## 📄 **ЛИЦЕНЗИЯ**

Этот проект лицензирован под MIT License - см. файл [LICENSE](LICENSE) для деталей.

## 👥 **АВТОРЫ**

- **Sergey Zhukov** - *Основной разработчик* - [GitHub](https://github.com/yourusername)

## 🙏 **БЛАГОДАРНОСТИ**

- [Selenium](https://selenium.dev/) - за отличный инструмент для UI тестирования
- [RestAssured](https://rest-assured.io/) - за простоту API тестирования
- [Allure](https://allure.qameta.io/) - за красивые отчеты
- [JUnit 5](https://junit.org/junit5/) - за современный тестовый фреймворк

## 📞 **ПОДДЕРЖКА**

Если у вас есть вопросы или предложения:
- 📧 Email: Zhykovsergey@gmail.com
- 💬 Slack: #test-automation
- 🐛 Issues: [GitHub Issues](https://github.com/yourusername/TestNewApi/issues)

---

⭐ **Если проект вам понравился, поставьте звезду!** ⭐