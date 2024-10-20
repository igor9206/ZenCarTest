# ZenCar Test

Это мобильное приложение, созданное на Kotlin с использованием Jetpack Compose для управления электронной очередью пользователей.

## Описание проекта

Приложение состоит из двух экранов:

1. **Экран регистрации**
    
2. **Экран со списком пользователей**
    

### Экран регистрации

- **Регистрация**: Поля для имени, даты рождения, пароля и аватарки.
    
- **Авторизация**: Поля для имени и пароля.
    
- Переключение между регистрацией и авторизацией.
    
- После успешной регистрации или авторизации пользователь попадает на экран со списком пользователей и больше не видит экран регистрации до выхода из аккаунта.
    

### Экран со списком пользователей

- Отображает информацию об авторизованном пользователе.
    
- Кнопка для выхода из аккаунта.
    
- Список всех зарегистрированных пользователей.
    
- Список отсортирован по дате регистрации.
    
- Авторизованный пользователь может удалять аккаунты других пользователей, зарегистрированных позже него.
    
## Технические требования

- Язык: Kotlin
    
- UI: Jetpack Compose
    
- Сборка: Gradle (kts)
    
- База данных: Room
    
- Многопоточность: Coroutines
    
- Архитектура: MVVM

## Использованные библиотеки

- **AndroidX Navigation Compose**: Библиотека для навигации в Jetpack Compose.
    
- **AndroidX Lifecycle ViewModel Compose**: Интеграция ViewModel с Compose.
    
- **AndroidX Runtime LiveData**: Работа с LiveData в Compose.
    
- **Hilt Android**: Инструмент для внедрения зависимостей.
    
- **Hilt Compiler**: Компилятор для Hilt.
    
- **AndroidX Hilt Navigation Compose**: Интеграция Hilt с Compose навигацией.
    
- **Gson**: Библиотека для работы с JSON.
    
- **AndroidX Room Runtime**: Компоненты для работы с Room.
    
- **Room Compiler**: Компилятор для Room.
    
- **AndroidX Room KTX**: Kotlin расширения для Room.
    
- **Coil Compose**: Библиотека для загрузки изображений в Compose.