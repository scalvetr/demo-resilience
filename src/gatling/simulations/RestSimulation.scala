import io.gatling.core.Predef._
import io.gatling.http.Predef._

class RestSimulation extends Simulation {
  val httpProtocol = http.baseUrl("http://demo-resilience.local-k8s/api")
  object ServiceA {
    val getService = exec(
      http("GET Service A").get("/serviceA")
    )
    val postService = exec(
      http("POST Service A").post("/serviceA").header("Content-Type", "application/json")
        .body(StringBody("""{ "name": "test name", "message": "test message" }""")).asJson
    )
  }
  object ServiceB {
    val getService = exec(
      http("GET Service B").get("/serviceB")
    )
    val postService = exec(
      http("POST Service B").post("/serviceB").header("Content-Type", "application/json")
        .body(StringBody("""{ "name": "test name", "message": "test message" }""")).asJson
    )
  }
  val scnServiceA = scenario("ServiceA").exec(ServiceA.getService, ServiceA.postService)
  val scnServiceB = scenario("ServiceB").exec(ServiceB.getService, ServiceB.postService)

  setUp(
    scnServiceA.inject(atOnceUsers(100)),
    scnServiceB.inject(atOnceUsers(100)),
  ).protocols(httpProtocol)
}