package pl.wit.shop;

import io.helidon.config.Config;
import io.helidon.microprofile.server.Server;

import static io.helidon.config.ConfigSources.classpath;

public class Main {

    private Main() {}

    public static void main(String[] args) {
        Server.builder()
                .config(buildConfig())
                .build()
                .start();
    }

    private static Config buildConfig() {
        return Config.builder()
                .sources(
                        classpath("application.yaml"),
                        classpath("META-INF/microprofile-config.properties")
                )
                .build();
    }
}
