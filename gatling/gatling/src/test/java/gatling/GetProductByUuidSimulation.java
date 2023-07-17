package gatling;

import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;


import java.util.List;
import java.util.Map;
import java.util.UUID;

import static io.gatling.javaapi.core.CoreDsl.listFeeder;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.core.CoreDsl.scenario;
import static io.gatling.javaapi.http.HttpDsl.status;

public class GetProductByUuidSimulation extends Simulation {
    private static final UUID PRODUCT_UUID = UUID.fromString("9a764ac0-75d3-4b3e-93f9-e372a56a3a7c");
    HttpProtocolBuilder httpProtocol = http
            .baseUrl("http://localhost:8010")
            .acceptHeader("application/json");

    ScenarioBuilder scenario = scenario("GET product Scenario")
            .feed(listFeeder(List.of(Map.of("uuid", PRODUCT_UUID))))
            .exec(http("get-products")
                    .get("/api/products/#{uuid}")
                    .check(status().is(200))
            );

    {
        setUp(Scenarios.peakScenario(scenario))
                .protocols(httpProtocol);
    }
}
