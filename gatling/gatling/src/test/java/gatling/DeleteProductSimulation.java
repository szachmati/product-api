package gatling;

import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import java.time.Duration;
import java.util.UUID;

import static io.gatling.javaapi.core.CoreDsl.rampUsers;
import static io.gatling.javaapi.core.CoreDsl.scenario;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class DeleteProductSimulation extends Simulation {

    HttpProtocolBuilder httpProtocol = http
            .baseUrl("http://localhost:8010")
            .acceptHeader("application/json");

    ScenarioBuilder scenario = scenario("Delete product scenario")
            .exec(http("delete-product")
                    .delete("/api/products/" + UUID.randomUUID())
                    .header("Content-Type", "application/json")
                    .check(status().is(404))
            );
    {
        setUp(
                scenario.injectOpen(
                        rampUsers(1000)
                                .during(Duration.ofSeconds(10))
                )
        ).protocols(httpProtocol);
    }
}
