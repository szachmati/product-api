package pl.wit.shop.example;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/greet")
@RequestScoped
public class GreetingResource {

    private final GreetingService greetingService;

    @Inject
    public GreetingResource(GreetingService greetingService) {
        this.greetingService = greetingService;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<GreetingOutput> greet() {
        return greetingService.getAll().stream()
                .map(GreetingOutput::from)
                .toList();
    }

    record GreetingOutput(String name) {
        static GreetingOutput from(Greeting greeting) {
            return new GreetingOutput(greeting.getName());
        }
    }
}
