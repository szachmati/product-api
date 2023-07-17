package gatling;

import io.gatling.javaapi.core.PopulationBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;

import java.time.Duration;

import static io.gatling.javaapi.core.CoreDsl.atOnceUsers;
import static io.gatling.javaapi.core.CoreDsl.constantUsersPerSec;
import static io.gatling.javaapi.core.CoreDsl.rampUsersPerSec;

class Scenarios {
    static PopulationBuilder peakScenario(ScenarioBuilder scenario) {
        return scenario.injectOpen(
                constantUsersPerSec(200).during(Duration.ofSeconds(30)),
                atOnceUsers(2000),
                constantUsersPerSec(200).during(Duration.ofSeconds(30)),
                atOnceUsers(2000),
                constantUsersPerSec(200).during(Duration.ofSeconds(30))
        );
    }
    static PopulationBuilder enduranceScenario(ScenarioBuilder scenario) {
        return scenario.injectOpen(
                constantUsersPerSec(200).during(Duration.ofMinutes(5))
        );
    }
    static PopulationBuilder rampUpScenario(ScenarioBuilder scenario) {
        return scenario.injectOpen(
                rampUsersPerSec(5).to(1000).during(Duration.ofMinutes(2))
        );
    }
}
