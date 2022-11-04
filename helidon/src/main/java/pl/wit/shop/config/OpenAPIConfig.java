package pl.wit.shop.config;

import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                title = "Helidon Product API",
                description = "Managing product CRUD operations",
                version = "0.0.1"
        )
)
public class OpenAPIConfig {
}
