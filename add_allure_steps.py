#!/usr/bin/env python3
"""
Скрипт для автоматического добавления Allure steps к API тестам
"""

import os
import re
import glob

def add_allure_steps_to_file(file_path):
    """Добавляет Allure steps к файлу с API тестами"""
    
    with open(file_path, 'r', encoding='utf-8') as f:
        content = f.read()
    
    # Паттерн для поиска методов тестов
    method_pattern = r'(public void test\w+\([^)]*\) \{[^}]+(?:\{[^}]*\}[^}]*)*\})'
    
    def replace_method(match):
        method_content = match.group(1)
        
        # Извлекаем название метода
        method_name_match = re.search(r'public void (test\w+)\(', method_content)
        if not method_name_match:
            return method_content
        
        method_name = method_name_match.group(1)
        
        # Генерируем описание шага
        step_description = generate_step_description(method_name)
        
        # Находим содержимое метода (между { и })
        method_body_match = re.search(r'public void test\w+\([^)]*\) \{([^}]+(?:\{[^}]*\}[^}]*)*)\}', method_content, re.DOTALL)
        if not method_body_match:
            return method_content
        
        method_body = method_body_match.group(1).strip()
        
        # Оборачиваем содержимое в Step
        indented_body = '\n'.join('            ' + line for line in method_body.split('\n'))
        
        new_method = f"""public void {method_name}() {{
        Step("{step_description}", () -> {{
{indented_body}
        }});
    }}"""
        
        return new_method
    
    # Заменяем все методы
    new_content = re.sub(method_pattern, replace_method, content, flags=re.DOTALL)
    
    # Записываем обновленный файл
    with open(file_path, 'w', encoding='utf-8') as f:
        f.write(new_content)
    
    print(f"Обработан файл: {file_path}")

def generate_step_description(method_name):
    """Генерирует описание шага на основе названия метода"""
    
    if 'Get' in method_name and 'All' in method_name:
        return "Отправляем GET запрос для получения всех данных"
    elif 'Get' in method_name and 'ById' in method_name:
        return "Отправляем GET запрос для получения данных по ID"
    elif 'Create' in method_name or 'Post' in method_name:
        return "Отправляем POST запрос для создания нового ресурса"
    elif 'Update' in method_name or 'Put' in method_name:
        return "Отправляем PUT запрос для обновления данных"
    elif 'Delete' in method_name:
        return "Отправляем DELETE запрос для удаления данных"
    elif 'Login' in method_name:
        return "Выполняем аутентификацию пользователя"
    elif 'Register' in method_name:
        return "Регистрируем нового пользователя"
    elif 'Performance' in method_name:
        return "Проверяем производительность API"
    elif 'Validation' in method_name:
        return "Выполняем валидацию данных"
    elif 'Error' in method_name or 'Negative' in method_name:
        return "Проверяем обработку ошибок"
    elif 'Retry' in method_name:
        return "Выполняем тест с возможностью повторных попыток"
    else:
        return "Выполняем API запрос"

def main():
    """Основная функция"""
    
    # Находим все API тесты
    api_test_files = glob.glob('src/test/java/api/*Test.java')
    
    print(f"Найдено {len(api_test_files)} API тестовых файлов")
    
    for file_path in api_test_files:
        try:
            add_allure_steps_to_file(file_path)
        except Exception as e:
            print(f"Ошибка при обработке файла {file_path}: {e}")
    
    print("Обработка завершена!")

if __name__ == "__main__":
    main()
