## Тестирования веб-сервиса покупки путешествий.
## Инструкция по запуску приложения и тестов.
1. Скачать в локальный репозиторий все файлы. 
2. Создаnm контейнер Docker для работы с БД командой ```docker-compose up``` .
```
mysql_1  | 2020-08-08T09:02:44.453814Z 0 [System] [MY-010931] [Server] /usr/sbin/mysqld: ready for connections. Version: '8.0.19'  socket: '/var/run/mysqld/mysqld.sock'  port: 3306  MySQL Community Server - GPL.
mysql_1  | 2020-08-08T09:02:44.761363Z 0 [System] [MY-011323] [Server] X Plugin ready for connections. Socket: '/var/run/mysqld/mysqlx.sock' bind-address: '::' port: 33060
```
3. Запустить работу базы mySQL в контейнере командой ```docker-compose exec mysql mysql -u app -p app -v```. 
В открывшееся поле ввода пароля введите пароль: pass. Проверить то, что база запустилась, нужно командой ```show tables```.
В базе таблиц не будет и команда покажет данную информацию в логах.
4. Запустить приложение командой ```java -jar aqa-shop.jar```.
5. Запустить симулятор банковских серверов (для имитации ответа на запросы) командой ```npm start```.
В логах отобразится информация об имеющихся в БД картах:
```
  { number: '4444 4444 4444 4441', status: 'APPROVED' },
  { number: '4444 4444 4444 4442', status: 'DECLINED' }
  ```
6. Тесты запускаются командой ``` ./gradlew clean test```.
7. Для генерации отчета о тестировании в Allure запустить команду ``` ./gradlew allureReport```. 
