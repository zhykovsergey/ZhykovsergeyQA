# 🚀 Руководство по загрузке проекта на GitHub

## 📋 **ПОШАГОВАЯ ИНСТРУКЦИЯ**

### **ШАГ 1: Установка Git**

#### **Windows:**
1. Перейдите на https://git-scm.com/download/win
2. Скачайте и установите Git for Windows
3. При установке выберите опции по умолчанию
4. Перезапустите командную строку

#### **Проверка установки:**
```bash
git --version
```

### **ШАГ 2: Настройка Git (первый раз)**

```bash
# Настройка имени пользователя
git config --global user.name "Ваше Имя"

# Настройка email
git config --global user.email "your.email@example.com"

# Проверка настроек
git config --list
```

### **ШАГ 3: Инициализация локального репозитория**

```bash
# Перейдите в папку проекта
cd C:\Users\zhyko\IdeaProjects\TestNewApi

# Инициализация Git репозитория
git init

# Добавление всех файлов
git add .

# Создание первого commit
git commit -m "feat: initial commit - professional test automation framework

- Add comprehensive test automation framework
- Include API, UI, E2E, and data-driven tests
- Add Allure reporting and monitoring
- Add CI/CD pipeline with GitHub Actions
- Add notification system (Slack, Telegram, Email)
- Add multi-browser support (Chrome, Firefox, Edge, Safari)
- Add performance monitoring and metrics
- Add comprehensive documentation and scripts"

# Создание основной ветки
git branch -M main
```

### **ШАГ 4: Создание репозитория на GitHub**

1. **Перейдите на GitHub:**
   - Откройте https://github.com
   - Войдите в свой аккаунт (или создайте новый)

2. **Создайте новый репозиторий:**
   - Нажмите кнопку "New" или "+" → "New repository"
   - Название: `TestNewApi`
   - Описание: `Professional Test Automation Framework for Java`
   - Сделайте репозиторий **Public** (для портфолио)
   - **НЕ** инициализируйте с README (у нас уже есть)
   - Нажмите "Create repository"

### **ШАГ 5: Подключение к GitHub**

```bash
# Добавление удаленного репозитория
git remote add origin https://github.com/YOUR_USERNAME/TestNewApi.git

# Загрузка кода на GitHub
git push -u origin main
```

### **ШАГ 6: Настройка GitHub Actions**

1. **Включите Actions:**
   - Перейдите в Settings → Actions → General
   - Выберите "Allow all actions and reusable workflows"
   - Нажмите "Save"

2. **Настройте Secrets (опционально):**
   - Перейдите в Settings → Secrets and variables → Actions
   - Добавьте следующие secrets:
     - `SLACK_WEBHOOK` - для Slack уведомлений
     - `TELEGRAM_BOT_TOKEN` - для Telegram уведомлений
     - `TELEGRAM_CHAT_ID` - для Telegram уведомлений

### **ШАГ 7: Настройка GitHub Pages**

1. **Включите Pages:**
   - Перейдите в Settings → Pages
   - Source: "GitHub Actions"
   - Нажмите "Save"

2. **Allure отчеты будут доступны по адресу:**
   ```
   https://YOUR_USERNAME.github.io/TestNewApi/
   ```

## 🚀 **АЛЬТЕРНАТИВНЫЙ СПОСОБ (через GitHub Desktop)**

### **Если предпочитаете GUI:**

1. **Установите GitHub Desktop:**
   - Скачайте с https://desktop.github.com/
   - Установите и войдите в аккаунт

2. **Создайте репозиторий:**
   - File → New Repository
   - Name: `TestNewApi`
   - Local Path: `C:\Users\zhyko\IdeaProjects\TestNewApi`
   - Нажмите "Create Repository"

3. **Загрузите на GitHub:**
   - Publish repository
   - Сделайте репозиторий Public
   - Нажмите "Publish Repository"

## 📋 **ПРОВЕРКА ЗАГРУЗКИ**

### **После загрузки проверьте:**

1. **Репозиторий доступен:**
   ```
   https://github.com/YOUR_USERNAME/TestNewApi
   ```

2. **Файлы загружены:**
   - README.md отображается
   - Все папки и файлы присутствуют
   - .gitignore работает

3. **GitHub Actions работает:**
   - Перейдите в Actions tab
   - Должен запуститься workflow

4. **GitHub Pages настроен:**
   - В Settings → Pages видно "Your site is live"

## 🎯 **ОПТИМИЗАЦИЯ РЕПОЗИТОРИЯ**

### **Добавьте темы:**
- Перейдите в Settings → General
- В разделе "About" добавьте:
  - Website: `https://YOUR_USERNAME.github.io/TestNewApi/`
  - Topics: `java`, `testing`, `automation`, `selenium`, `rest-assured`, `allure`, `maven`, `junit5`

### **Настройте описание:**
```markdown
🚀 Professional Test Automation Framework for Java
- API, UI, E2E, and Data-driven testing
- Allure reporting and monitoring
- CI/CD with GitHub Actions
- Multi-browser support
- Notification system
```

## 📊 **СТАТИСТИКА РЕПОЗИТОРИЯ**

### **После загрузки у вас будет:**
- ✅ **50+** файлов
- ✅ **5000+** строк кода
- ✅ **25+** тестов
- ✅ **15+** утилит
- ✅ **10+** скриптов
- ✅ **Полная** документация

## 🎉 **ГОТОВО!**

После выполнения всех шагов ваш проект будет:

- 🌐 **Доступен** на GitHub
- 🚀 **Автоматически** тестироваться
- 📊 **Генерировать** отчеты
- 📢 **Отправлять** уведомления
- 🎯 **Готов** для портфолио

## 📞 **ПОМОЩЬ**

Если возникли проблемы:

1. **Проверьте установку Git:**
   ```bash
   git --version
   ```

2. **Проверьте настройки:**
   ```bash
   git config --list
   ```

3. **Проверьте статус:**
   ```bash
   git status
   ```

4. **Очистите и повторите:**
   ```bash
   git reset --hard HEAD
   git clean -fd
   ```

---

**🎯 Удачи с загрузкой проекта на GitHub!** 🚀

