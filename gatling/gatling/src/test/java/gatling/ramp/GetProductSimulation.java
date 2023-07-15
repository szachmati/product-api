package gatling.ramp;

import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import java.time.Duration;

import static io.gatling.javaapi.core.CoreDsl.rampUsersPerSec;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.core.CoreDsl.scenario;
import static io.gatling.javaapi.http.HttpDsl.status;

public class GetProductSimulation extends Simulation {
    HttpProtocolBuilder httpProtocol = http
            .baseUrl("http://localhost:8010")
            .acceptHeader("application/json");

    ScenarioBuilder scenario = scenario("Create product")
            .exec(http("GET products")
                    .get("/api/products")
                    .header("Content-Type", "application/json")
                    .queryParam("category", "HOME")
                    .queryParam("page", 0)
                    .queryParam("size", 50)
                    .queryParam("sort", "ASC")
                    .check(status().is(200))
            );

    {
        setUp(
                scenario.injectOpen(
                    rampUsersPerSec(10)
                            .to(10000)
                            .during(Duration.ofMinutes(5))
                )
        ).protocols(httpProtocol);
    }

}
