# Demo resilience4j

## Build & run

### local run
```shell script
./gradlew bootJar
```

### build
```shell script
./gradlew build
```

### test
```shell script
cd service-a
npm install
export PORT=8081 
node server.js

cd ../service-b
npm install
export PORT=8082
node server.js

cd ..


curl http://localhost:8080/api/v1/serviceA
```
