# Demo resilience

Demo resilience is a demo project showing how to use resilience4j & spring boot.
 
## Build
* Spring Boot Project
```shell script
./gradlew bootJar
```
* External service image 
```shell script
docker build external-service -t external-service
```

## Local Run
* Run external services 
```shell script
docker stop service-a
docker rm service-a
docker run -d \
-p 8081:80 \
-v "$(pwd)/src/main/k8s/config/service-a":"/usr/src/app/config" \
--name service-a \
external-service:latest

docker stop service-b
docker rm service-b
docker run -d \
-p 8082:80 \
-v "$(pwd)/src/main/k8s/config/service-b":"/usr/src/app/config" \
--name service-b \
external-service:latest
```

* Run Spring Boot Project
```shell script
./gradlew bootRun --args="services.backendA.base-url=http://localhost:8888"
```

* Test
```shell script
curl -X POST http://localhost:8081/message -H "Content-type: application/json" -d '{"sample-attr": "test"}'
curl -X POST http://localhost:8082/message -H "Content-type: application/json" -d '{"sample-attr": "test"}'
curl -X POST http://localhost:8080/serviceA -H "Content-type: application/json" -d '{"sample-attr": "test"}'
curl -X POST http://localhost:8080/serviceB -H "Content-type: application/json" -d '{"sample-attr": "test"}'
```

### Local Run (skaffold)
```shell script
skaffold dev --trigger notify
```


### Prometheus
Check the Prometheus server.
- Open http://demo-resilience.local-k8s/prometheus/
- Access status -> Targets, both endpoints must be "UP"

### Grafana
Configure the Grafana.
- Open http://demo-resilience.local-k8s/grafana/
- **Configure integration with Prometheus**
    - Access configuration
    - Add data source
    - Select Prometheus
    - Use url "http://prometheus:9090" and access with value "Server"
- **Configure dashboard**
    - Access "home"
    - Import dashboard
    - Upload dashboard.json from /docker


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

curl http://demo-resilience.local-k8s/api/actuator/health
curl http://demo-resilience.local-k8s/api/actuator/prometheus

#service A
curl http://demo-resilience.local-k8s/api/serviceA
curl -X POST http://demo-resilience.local-k8s/api/serviceA -H "Content-type: application/json" -d '{"sample-attr": "test"}'

#service A
curl http://demo-resilience.local-k8s/api/serviceB
curl -X POST http://demo-resilience.local-k8s/api/serviceB -H "Content-type: application/json" -d '{"sample-attr": "test"}'

```

### monitoring
* Actuator: http://demo-resilience.local-k8s/api/actuator/health
* Prometheus: http://demo-resilience.local-k8s/prometheus/
* Grafana: http://demo-resilience.local-k8s/grafana/


```shell script
#disable service a
kubectl patch service service-a -p '{"spec":{"selector":{"name": "service-unknown"}}}'
#enable service a
kubectl patch service service-a -p '{"spec":{"selector":{"name": "service-a"}}}'
```