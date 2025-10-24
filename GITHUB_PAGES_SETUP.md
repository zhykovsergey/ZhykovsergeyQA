# 🌐 Настройка GitHub Pages для Allure отчетов

## 📋 **ПОШАГОВАЯ ИНСТРУКЦИЯ**

### **ШАГ 1: Включение GitHub Pages**

1. **Перейдите в настройки репозитория:**
   - Откройте https://github.com/zhykovsergey/ZhykovsergeyQA
   - Нажмите на вкладку "Settings"

2. **Найдите раздел Pages:**
   - В левом меню найдите "Pages"
   - Или перейдите напрямую: https://github.com/zhykovsergey/ZhykovsergeyQA/settings/pages

3. **Настройте источник:**
   - В разделе "Source" выберите "GitHub Actions"
   - Нажмите "Save"

### **ШАГ 2: Настройка прав доступа**

1. **Перейдите в Actions settings:**
   - В настройках репозитория найдите "Actions" → "General"
   - Или перейдите: https://github.com/zhykovsergey/ZhykovsergeyQA/settings/actions

2. **Настройте права для Actions:**
   - В разделе "Workflow permissions" выберите "Read and write permissions"
   - Поставьте галочку "Allow GitHub Actions to create and approve pull requests"
   - Нажмите "Save"

### **ШАГ 3: Проверка настройки**

После настройки:
- GitHub Pages будет доступен по адресу: https://zhykovsergey.github.io/ZhykovsergeyQA/
- Allure отчеты будут автоматически генерироваться при каждом push
- Отчеты будут доступны в разделе "Actions" → "Artifacts"

## 🔧 **АЛЬТЕРНАТИВНЫЕ СПОСОБЫ**

### **Способ 1: Ручная загрузка отчетов**

Если GitHub Pages не работает, вы можете:

1. **Скачать отчеты из Actions:**
   - Перейдите в https://github.com/zhykovsergey/ZhykovsergeyQA/actions
   - Выберите последний успешный workflow
   - Скачайте артефакт "allure-report"
   - Распакуйте и откройте `index.html` в браузере

### **Способ 2: Локальная генерация отчетов**

```bash
# Установите Allure локально
npm install -g allure-commandline

# Сгенерируйте отчет
allure generate target/allure-results -o allure-report

# Откройте отчет
allure open allure-report
```

### **Способ 3: Использование GitHub Codespaces**

1. **Откройте репозиторий в Codespaces:**
   - Нажмите "Code" → "Codespaces" → "Create codespace"

2. **Запустите тесты и сгенерируйте отчет:**
   ```bash
   mvn test
   allure generate target/allure-results -o allure-report
   allure serve target/allure-results
   ```

## 🚀 **БЫСТРЫЙ СТАРТ**

### **Для немедленного просмотра отчетов:**

1. **Запустите simple-api.yml workflow:**
   - Перейдите в Actions
   - Найдите "Simple API Tests"
   - Нажмите "Run workflow"

2. **Скачайте отчет:**
   - После завершения скачайте артефакт "allure-report"
   - Откройте `index.html` в браузере

## 📊 **ОЖИДАЕМЫЕ РЕЗУЛЬТАТЫ**

После настройки GitHub Pages:

- ✅ **Автоматическая генерация** отчетов при каждом push
- ✅ **Публичный доступ** к отчетам через GitHub Pages
- ✅ **История отчетов** с возможностью сравнения
- ✅ **Интеграция** с GitHub Actions

## 🔍 **УСТРАНЕНИЕ ПРОБЛЕМ**

### **Проблема: "Permission denied"**

**Решение:**
1. Проверьте настройки прав в Actions
2. Убедитесь, что GitHub Pages включен
3. Проверьте, что workflow имеет права `pages: write`

### **Проблема: "No pages found"**

**Решение:**
1. Убедитесь, что workflow успешно завершился
2. Проверьте, что отчеты генерируются
3. Подождите несколько минут (GitHub Pages может обновляться с задержкой)

### **Проблема: "Workflow failed"**

**Решение:**
1. Используйте `simple-api.yml` для начала
2. Проверьте логи в Actions
3. Убедитесь, что все тесты проходят

## 📞 **ПОДДЕРЖКА**

Если у вас возникли проблемы:

1. **Проверьте логи** в GitHub Actions
2. **Используйте simple-api.yml** для тестирования
3. **Скачайте отчеты** из артефактов
4. **Обратитесь к документации** GitHub Pages

---

**🎯 После настройки GitHub Pages ваши Allure отчеты будут доступны публично!** 🚀
