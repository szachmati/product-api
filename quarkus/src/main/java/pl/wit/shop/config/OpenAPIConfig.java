package pl.wit.shop.config;

import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Info;

import javax.ws.rs.core.Application;

@OpenAPIDefinition(
        info = @Info(
                title = "Quarkus Product API",
                description = "Simple product API",
                version = "1.0-SNAPSHOT"
        )
)
public class OpenAPIConfig extends Application {
}
