package pl.wit.shop.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                title = "Product API",
                version = "0.1",
                description = "Simple product API"
        )
)
public class OpenAPIConfig {
}
