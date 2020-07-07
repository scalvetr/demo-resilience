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

### run dev
```shell script
skaffold dev --trigger notify
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
curl -X POST http://demo-circuitbreaker.local-k8s/api/v1/serviceA -H "Content-type: application/json" -d '{"sample-attr": "test"}'

curl http://demo-circuitbreaker.local-k8s/api/v1/serviceB
curl -X POST http://demo-circuitbreaker.local-k8s/api/v1/serviceB -H "Content-type: application/json" -d '{"sample-attr": "test"}'

```
