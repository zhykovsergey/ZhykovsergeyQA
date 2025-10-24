# 🔧 Исправление GitHub Pages

## ❌ **ПРОБЛЕМА:**
```
remote: Permission to zhykovsergey/ZhykovsergeyQA.git denied to github-actions[bot].
fatal: unable to access 'https://github.com/zhykovsergey/ZhykovsergeyQA.git/': The requested URL returned error: 403
```

## ✅ **РЕШЕНИЕ:**

### **ШАГ 1: Настройте права доступа для GitHub Actions**

1. **Перейдите в настройки репозитория:**
   - https://github.com/zhykovsergey/ZhykovsergeyQA/settings

2. **Найдите раздел "Actions" → "General":**
   - https://github.com/zhykovsergey/ZhykovsergeyQA/settings/actions

3. **В разделе "Workflow permissions":**
   - Выберите **"Read and write permissions"**
   - Поставьте галочку **"Allow GitHub Actions to create and approve pull requests"**
   - Нажмите **"Save"**

### **ШАГ 2: Включите GitHub Pages**

1. **Перейдите в раздел "Pages":**
   - https://github.com/zhykovsergey/ZhykovsergeyQA/settings/pages

2. **В разделе "Source":**
   - Выберите **"GitHub Actions"**
   - Нажмите **"Save"**

### **ШАГ 3: Проверьте настройки**

После настройки:
- GitHub Pages будет доступен по адресу: https://zhykovsergey.github.io/ZhykovsergeyQA/
- Allure отчеты будут автоматически генерироваться при каждом push

## 🚀 **АЛЬТЕРНАТИВНОЕ РЕШЕНИЕ (РЕКОМЕНДУЕТСЯ):**

### **Используйте workflow без GitHub Pages:**

1. **Запустите "Stable Tests" workflow:**
   - https://github.com/zhykovsergey/ZhykovsergeyQA/actions
   - Найдите "Stable Tests (No GitHub Pages)"
   - Нажмите "Run workflow"

2. **Скачайте отчеты из артефактов:**
   - После завершения workflow
   - Скачайте артефакт "allure-report"
   - Распакуйте и откройте `index.html` в браузере

## 📊 **ДОСТУПНЫЕ WORKFLOW:**

### **✅ Гарантированно работают:**
- **Stable Tests** - без GitHub Pages, только артефакты
- **Working Tests** - только рабочие тесты
- **Simple API** - простые API тесты

### **⚠️ Могут иметь проблемы:**
- **API Only** - требует настройки GitHub Pages
- **Advanced CI** - сложная настройка
- **UI Tests** - может падать на WebDriver

## 🎯 **РЕКОМЕНДАЦИИ:**

1. **Начните с "Stable Tests"** - он гарантированно работает
2. **Настройте GitHub Pages** по инструкции выше
3. **После настройки** попробуйте "API Only" workflow
4. **Скачивайте отчеты** из артефактов для просмотра

## 📞 **ПОДДЕРЖКА:**

Если у вас возникли проблемы:

1. **Проверьте настройки** в разделе Settings
2. **Используйте "Stable Tests"** для начала
3. **Скачивайте отчеты** из артефактов
4. **Обратитесь к документации** GitHub Actions

---

**🎯 После настройки GitHub Pages ваши Allure отчеты будут доступны публично!** 🚀
