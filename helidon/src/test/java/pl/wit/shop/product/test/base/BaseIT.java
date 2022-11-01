package pl.wit.shop.product.test.base;

import io.helidon.microprofile.tests.junit5.HelidonTest;
import jakarta.inject.Inject;
import jakarta.transaction.TransactionManager;
import lombok.Getter;
import org.junit.jupiter.api.Tag;
import pl.wit.shop.product.test.transaction.TransactionOperations;

@Tag("integration")
@HelidonTest
public class BaseIT implements TransactionOperations {

    @Getter
    @Inject
    private TransactionManager transactionManager;
}
