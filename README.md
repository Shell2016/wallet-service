# Wallet-service

Перед запуском приложения нужно запустить postgres в докере.
Команда из корневой папки проекта:

docker compose -f docker-compose/docker-compose.yml up -d

Способы запуска приложения:
 - Через метод main() находящийся по пути: ./application/src/main/java/io/ylab/wallet/application/Application
 - Команда: mvn package && java -jar application/target/application-1.0-SNAPSHOT.jar

Чтобы проверить работу аудита введите спец команду 'getAudit' в главном меню(при разлогиненном пользователе)
