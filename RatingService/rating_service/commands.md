### WE USE ActiveMQ (RabbitMQ-compatible broker)
- docker exec -it rabbitmq  rabbitmq-plugins enable rabbitmq_amqp1_0 (enable plugin in docker)

### DOCKER

- docker-compose down -v
- docker-compose build --no-cache
- docker-compose up

### START

- mvn quarkus:dev
- http://localhost:8080/ratings
