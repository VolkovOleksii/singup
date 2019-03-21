# Test Assignment - Distributed Signup

### Setting up

Start zookeeper:
```sh
docker run -d --name zookeeper \
  --network host \
  -e ALLOW_ANONYMOUS_LOGIN=yes \
  bitnami/zookeeper:latest
```

Start kafka:
```sh
docker run -d --name kafka \
  --network host \
  -e KAFKA_ZOOKEEPER_CONNECT=localhost:2181 \
  -e ALLOW_PLAINTEXT_LISTENER=yes \
  bitnami/kafka:latest
```

Start postgresql-server
```sh
docker run -d --name postgresql-server \
    --network host \
    bitnami/postgresql:latest
```

Building jars:

```sh
mvn clean install -Dmaven.test.skip=true
mvn clean package -f sing-up/pom.xml
mvn clean package -f persistence/pom.xml
```

Build Docker images:
```sh
docker build -t singup sing-up/
docker build -t persistence persistence/
```

Start Docker containers:
```sh
docker run -d --name singup --network host singup:latest
docker run -d --name persistence --network host persistence:latest
```

Exec end to end test:
```sh
mvn clean test -f test/pom.xml
```

### Using

```sh
curl -d '{"email":"user_email@email.com", "password":"user_password"}' -H "Content-Type: application/json" -X POST http://localhost:8080/users
```