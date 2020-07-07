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

### test services
```shell script
kubectl get nodes -o wide
# external-ip = 172.18.0.2
curl -X POST http://172.18.0.2:30080/message -H "Content-type: application/json" -d '{"sample-attr": "test"}'
curl -X POST http://172.18.0.2:30081/message -H "Content-type: application/json" -d '{"sample-attr": "test"}'
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

curl http://demo-circuitbreaker.local-k8s/api/actuator/health
curl http://demo-circuitbreaker.local-k8s/api/actuator/prometheus

#service A
curl http://demo-circuitbreaker.local-k8s/api/v1/serviceA
curl -X POST http://demo-circuitbreaker.local-k8s/api/v1/serviceA -H "Content-type: application/json" -d '{"sample-attr": "test"}'

#service A
curl http://demo-circuitbreaker.local-k8s/api/v1/serviceB
curl -X POST http://demo-circuitbreaker.local-k8s/api/v1/serviceB -H "Content-type: application/json" -d '{"sample-attr": "test"}'

```
