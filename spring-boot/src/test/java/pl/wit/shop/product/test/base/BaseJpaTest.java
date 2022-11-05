package pl.wit.shop.product.test.base;

import lombok.Getter;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.support.TransactionOperations;

import javax.persistence.EntityManager;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public abstract class BaseJpaTest extends PostgresContainer implements DbCleaner {

    @Getter
    @Autowired
    private TransactionOperations transactionOperations;

    @Getter
    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    void clearEntities() {
        clearAllEntities();
    }
}
