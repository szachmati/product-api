package gatling;

import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;
import org.apache.commons.lang3.RandomStringUtils;

import java.time.Duration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static io.gatling.javaapi.core.CoreDsl.StringBody;
import static io.gatling.javaapi.core.CoreDsl.rampUsers;
import static io.gatling.javaapi.core.CoreDsl.scenario;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class UpdateProductSimulation extends Simulation {

    private static final List<String> CATEGORIES = List.of(
            "HOME", "ELECTRONICS", "CARS", "FOOD", "FURNITURE",
            "MOBILE PHONES", "FASHION", "MUSIC", "SPORT", "CHILD", "HEALTH"
    );
    HttpProtocolBuilder httpProtocol = http
            .baseUrl("http://localhost:8010")
            .acceptHeader("application/json");

    Iterator<Map<String, Object>> feeder =
            Stream.generate((Supplier<Map<String, Object>>) () ->
                    Map.of("productId", UUID.randomUUID(),
                            "name", "name-" + RandomStringUtils.randomAlphanumeric(20),
                            "category", getRandomCategory(),
                            "price", 20.0
                    )
            ).iterator();


    ScenarioBuilder scenario = scenario("Create product Scenario")
            .feed(feeder)
            .exec(http("post-product")
                    .put("/api/products/#{productId}")
                    .header("Content-Type", "application/json")
                    .body(StringBody("{ \"name\": \"#{name}\", \"category\": \"#{category}\", \"price\": \"#{price}\" }"))
                    .check(status().is(201))
            );
    {
        setUp(
                scenario.injectOpen(
                        rampUsers(1000)
                                .during(Duration.ofSeconds(10))
                )
        ).protocols(httpProtocol);
    }

    private String getRandomCategory() {
        return CATEGORIES.get(new Random().nextInt(CATEGORIES.size()));
    }

}
