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


### Prometheus
Check the Prometheus server.
- Open http://demo-circuitbreaker.local-k8s/prometheus/
- Access status -> Targets, both endpoints must be "UP"

### Grafana
Configure the Grafana.
- Open http://demo-circuitbreaker.local-k8s/grafana/
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

curl http://demo-circuitbreaker.local-k8s/api/actuator/health
curl http://demo-circuitbreaker.local-k8s/api/actuator/prometheus

#service A
curl http://demo-circuitbreaker.local-k8s/api/v1/serviceA
curl -X POST http://demo-circuitbreaker.local-k8s/api/serviceA -H "Content-type: application/json" -d '{"sample-attr": "test"}'

#service A
curl http://demo-circuitbreaker.local-k8s/api/v1/serviceB
curl -X POST http://demo-circuitbreaker.local-k8s/api/serviceB -H "Content-type: application/json" -d '{"sample-attr": "test"}'

```

### monitoring
* Actuator: http://demo-circuitbreaker.local-k8s/api/actuator/health
* Prometheus: http://demo-circuitbreaker.local-k8s/prometheus/
* Grafana: http://demo-circuitbreaker.local-k8s/grafana/


```shell script
#disable service a
kubectl patch service service-a -p '{"spec":{"selector":{"name": "service-unknown"}}}'
#enable service a
kubectl patch service service-a -p '{"spec":{"selector":{"name": "service-a"}}}'
```