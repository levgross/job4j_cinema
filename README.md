# job4j_cinema

![img.png](img.png)

![img_1.png](img_1.png)

![img_2.png](img_2.png)

![img_3.png](img_3.png)

## О проекте
Это сайт по покупке билетов в кинотеатр.
Пользователь должен быть зарегистрирован.
Авторизация происходит по проверке пары email-номер телефона.
Используется Spring boot.
При переходах со страницы на страницу данные записываются в HttpSession.
И только в самом конце записываются в БД.

## Использованные технологии
Java 17, Maven 4.0, Spring boot, PostgreSQL, Liquibase, Log4j, AssertJ,
JUnit 5, Mockito 4, H2 1.4

## Настройка окружения
Установить PostgreSQL: логин - postgres, пароль - password
Создать там БД cinema
Браузер желательно Chrome

## Запуск проекта
Запустить командой mvn spring-boot:run
перейти по ссылке http://localhost:8080/index