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


curl http://localhost:8080/v1/serviceA
curl http://demo-circuitbreaker.local-k8s/api/actuator/health
curl http://demo-circuitbreaker.local-k8s/api/v1/serviceA

```
