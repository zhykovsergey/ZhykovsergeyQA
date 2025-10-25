# 🤝 Руководство по вкладу в проект

Спасибо за интерес к проекту TestNewApi! Мы приветствуем вклад от сообщества.

## 📋 **Как внести вклад**

### **1. Fork и клонирование**
```bash
# Fork репозитория на GitHub
# Затем клонируйте ваш fork
git clone https://github.com/YOUR_USERNAME/TestNewApi.git
cd TestNewApi

# Добавьте upstream репозиторий
git remote add upstream https://github.com/ORIGINAL_OWNER/TestNewApi.git
```

### **2. Создание ветки**
```bash
# Создайте новую ветку для вашего feature
git checkout -b feature/amazing-feature

# Или для bugfix
git checkout -b bugfix/fix-issue-123
```

### **3. Внесение изменений**
- Следуйте существующему стилю кода
- Добавляйте тесты для новых функций
- Обновляйте документацию при необходимости
- Убедитесь, что все тесты проходят

### **4. Commit и push**
```bash
# Добавьте изменения
git add .

# Commit с описательным сообщением
git commit -m "feat: add amazing feature"

# Push в ваш fork
git push origin feature/amazing-feature
```

### **5. Создание Pull Request**
- Откройте Pull Request на GitHub
- Опишите изменения в деталях
- Укажите связанные issues
- Дождитесь review

## 🎯 **Типы вкладов**

### **🐛 Bug Reports**
- Используйте шаблон issue
- Опишите шаги для воспроизведения
- Укажите ожидаемое и фактическое поведение
- Приложите логи и скриншоты

### **✨ Feature Requests**
- Проверьте, что feature еще не существует
- Опишите use case
- Объясните, как это поможет проекту
- Предложите реализацию

### **📚 Документация**
- Исправления в README
- Добавление примеров
- Переводы на другие языки
- Улучшение комментариев в коде

### **🧪 Тесты**
- Добавление новых тестов
- Улучшение покрытия
- Исправление flaky тестов
- Добавление performance тестов

## 📏 **Стандарты кода**

### **Java Style Guide**
```java
// Используйте camelCase для методов и переменных
public void testAmazingFeature() {
    String testData = "example";
    // ...
}

// Используйте PascalCase для классов
public class AmazingFeatureTest {
    // ...
}

// Добавляйте JavaDoc для публичных методов
/**
 * Тестирует удивительную функцию
 * @param input входные данные
 * @return результат теста
 */
public boolean testAmazingFeature(String input) {
    // ...
}
```

### **Тестирование**
```java
// Используйте описательные имена тестов
@Test
@DisplayName("Should return success when valid data provided")
public void testValidDataReturnsSuccess() {
    // Arrange
    String validData = "test";
    
    // Act
    boolean result = service.process(validData);
    
    // Assert
    assertTrue(result);
}
```

### **Allure аннотации**
```java
@Epic("API Testing")
@Feature("User Management")
@Story("User Registration")
@Test
@DisplayName("Should register new user successfully")
@Description("Тест проверяет успешную регистрацию нового пользователя")
public void testUserRegistration() {
    // ...
}
```

## 🚀 **Запуск тестов**

### **Локальная разработка**
```bash
# Компиляция
mvn clean compile

# Запуск всех тестов
mvn test

# Запуск конкретных тестов
mvn test -Dtest="*Api*"

# Запуск с профилем
mvn test -Pfast
```

### **Проверка качества кода**
```bash
# Проверка стиля
mvn checkstyle:check

# Анализ кода
mvn spotbugs:check

# Проверка безопасности
mvn dependency:check
```

## 📝 **Commit Convention**

Используйте [Conventional Commits](https://www.conventionalcommits.org/):

```
<type>[optional scope]: <description>

[optional body]

[optional footer(s)]
```

### **Типы:**
- `feat`: новая функция
- `fix`: исправление бага
- `docs`: изменения в документации
- `style`: форматирование, отсутствующие точки с запятой и т.д.
- `refactor`: рефакторинг кода
- `test`: добавление тестов
- `chore`: изменения в build процессе или вспомогательных инструментах

### **Примеры:**
```
feat(api): add user registration endpoint
fix(ui): resolve login button click issue
docs: update README with new features
test: add unit tests for validation utils
```

## 🔍 **Review Process**

### **Что мы проверяем:**
- ✅ Код соответствует стандартам проекта
- ✅ Тесты покрывают новую функциональность
- ✅ Документация обновлена
- ✅ Нет breaking changes
- ✅ Performance не ухудшился

### **Время review:**
- Обычно 1-3 рабочих дня
- Для больших изменений может потребоваться больше времени
- Мы стараемся отвечать быстро

## 🏷️ **Labels**

Мы используем следующие labels:

### **Типы:**
- `bug` - что-то не работает
- `enhancement` - новая функция или улучшение
- `documentation` - улучшения документации
- `question` - вопрос или помощь

### **Приоритет:**
- `priority: high` - критично
- `priority: medium` - важно
- `priority: low` - не срочно

### **Статус:**
- `status: needs review` - требует review
- `status: in progress` - в работе
- `status: blocked` - заблокировано

## 💬 **Коммуникация**

### **Каналы:**
- 💬 **GitHub Issues** - для bug reports и feature requests
- 💬 **GitHub Discussions** - для общих вопросов
- 📧 **Email** - для приватных вопросов

### **Правила:**
- Будьте вежливы и уважительны
- Используйте английский язык для технических обсуждений
- Предоставляйте контекст и детали
- Помогайте другим участникам

## 🎉 **Признание вкладов**

Мы ценим всех участников! Ваш вклад будет отмечен:
- В README файле
- В release notes
- В GitHub contributors
- В специальном разделе благодарностей

## ❓ **Часто задаваемые вопросы**

### **Q: Как начать работу с проектом?**
A: Начните с изучения README и запуска тестов локально.

### **Q: Нужно ли писать тесты для каждого изменения?**
A: Да, мы требуем тесты для новой функциональности.

### **Q: Как долго ждать review?**
A: Обычно 1-3 дня, но может быть дольше для больших изменений.

### **Q: Можно ли работать над несколькими issues одновременно?**
A: Да, но создавайте отдельные ветки для каждого issue.

## 📞 **Поддержка**

Если у вас есть вопросы:
- 📧 Email: your.email@example.com
- 💬 GitHub Discussions
- 🐛 GitHub Issues

---

**Спасибо за ваш вклад в TestNewApi!** 🚀

