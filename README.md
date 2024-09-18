# Wallet-service

[Описание заданий](Task.md)
Посмотреть выполнение заданий, можно переключившись на соответствующий коммит.
Каждый коммит соответствует одной полной таске (кроме первого коммита, он единственный незасквошенный)

Порядок запуска:


1. docker compose up -d  (запускает постгрес)
2. mvn clean package && java -jar application/target/application-1.0-SNAPSHOT.jar

Приложение будет запущено на порте 8080  

Swagger docs: http://localhost:8080/v3/api-docs  
Swagger UI: http://localhost:8080/swagger-ui.html

audit-starter подключается автоматически. Для его отключения: audit.enabled=false 

execution-time-logging-starter подключается с помощью аннотации @EnableExecutionTimeLogging

Описание API:

- Создание пользователя:  
POST /users  
Пароль должен быть не менее 6 символов  
Пример запроса:  
```json
{
  "username": "user1",
  "password": "123456"
}
```
- Аутентификация пользователя:  
POST /auth  
Пароль должен быть не менее 6 символов  
Пример запроса:  
```json
{
  "username": "ivan",
  "password": "123456"
}
```
Пример ответа:
```json
{
  "user_id": 25,
  "token": "eyJhbGciOiJIUzM4NCJ9.eyJ1c2VybmFtZSI6InVzZXIxIiwiaXNzIjoieWxhYiIsInN1YiI6IjEiLCJpYXQiOjE2OTgyNDM5NjEsImp0aSI6ImQxMjZlYWI4LWVkYTQtNDc2NC1hMGYwLTI4YWMyMWM3NzRkNiIsImV4cCI6MTY5ODI1MTE2MX0.PVNL3Z7DDGWL2MIKioinYD3SPSCwwNb4PlPojeMwCcySK5Kd6FG1cthVbq9fAkel",
  "issued_at": "2023-10-25 14:26:01",
  "expires_at": "2023-10-25 16:26:01"
}
```
- Баланс пользователя:  
  GET /users/{id}/balance  
  id пользователя в пути запроса должно совпадать с id авторизованного пользователя. Этот user_id приходит вместе 
  токеном после успешной аутентификации      
- Транзакциии пользователя:  
  GET /users/{id}/transactions  
  id пользователя в пути запроса должно совпадать с id авторизованного пользователя. Этот user_id приходит вместе
  токеном после успешной аутентификации      
- Операция со счетом:  
  PUT /users/{id}/transaction   
  id пользователя в пути запроса должно совпадать с id авторизованного пользователя. Этот user_id приходит вместе
  токеном после успешной аутентификации  
  Примеры запроса:  
```json
{
    "id": "35c4dec5-0a60-4b92-a0b1-e82c4d03c84",
    "type": "deposit",
    "amount": "555"
}
```
```json
{
  "id": "35c4dec5-0a60-4b92-a0b1-e82c4d03c84",
  "type": "withdraw",
  "amount": "555"
}
```
id транзакции должен быть уникален и быть в формате UUID. Можно сгенерировать тут - https://www.uuidgenerator.net/version4