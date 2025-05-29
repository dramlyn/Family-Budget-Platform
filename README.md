Платформа для планирования семейного бюджета (Серверная часть)
Для запуска:
- запустите Docker
- перейти в директорию fbp-parent/env/fbp-service-environment и запустить docker-compose файл (docker-compose up)
- дождитесь запуска всех под в Docker
- далее запустите сервис пользователей. Для этого перейдите в директорию fbp-parent/fbp-user-service и выполните следующие команды: 1) mvn clean package 2) java -jar target/fbp-user-service-1.0-SNAPSHOT.jar
- далее запустите сервис транзакций. Для этого перейдите в директорию fbp-parent/fbp-transaction-service и выполните следующие команды: 1) mvn clean package 2) java -jar target/fbp-transaction-service-1.0-SNAPSHOT.jar
- далее запустите сервис семейного бюджета. Для этого перейдите в директорию fbp-parent/fbp-family-budget-service и выполните следующие команды: 1) mvn clean package 2) java -jar target/fbp-family-budget-service-1.0-SNAPSHOT.jar
