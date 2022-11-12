package pl.wit.shop.config;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Initialized;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import org.flywaydb.core.Flyway;

import javax.sql.DataSource;

@ApplicationScoped
public class ApplicationStartupEventListener {

    @Inject
    private DataSource dataSource;

    private void migrateOnStartup(@Observes @Initialized(ApplicationScoped.class) Object event) {
        Flyway flyway = new Flyway(Flyway.configure().dataSource(dataSource));
        if (flyway.info().current() == null) {
            flyway.migrate();
        }
    }

}
