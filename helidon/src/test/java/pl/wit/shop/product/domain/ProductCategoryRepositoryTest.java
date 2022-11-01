package pl.wit.shop.product.domain;

import io.helidon.microprofile.tests.junit5.HelidonTest;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.TransactionManager;
import lombok.Getter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.wit.shop.product.test.transaction.TransactionOperations;

import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static pl.wit.shop.product.domain.ProductCategoryBuilder.anElectronicsProductCategory;
import static pl.wit.shop.product.domain.ProductCategoryMatcher.isProductCategory;

@HelidonTest
public class ProductCategoryRepositoryTest implements TransactionOperations {

    @Inject
    private ProductCategoryRepository productCategoryRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Getter
    @Inject
    private TransactionManager transactionManager;

    @BeforeEach
    void init() {
       inTransactionWithoutResult(() -> {
           entityManager
                   .createNativeQuery("TRUNCATE TABLE product, product_category RESTART IDENTITY")
                   .executeUpdate();
       });
    }

    @Test
    void findByName_shouldReturnEmptyOptional_whenCategoryDoesNotExist() {
        assertThat(productCategoryRepository.findByName("HOME"), is(Optional.empty()));
    }

    @Test
    void getByName_shouldProductCategory_whenCategoryExist() {
        inTransactionWithoutResult(() -> {
            productCategoryRepository.save(anElectronicsProductCategory().build());
        });

        assertThat(productCategoryRepository.getByName("ELECTRONICS"), isProductCategory().withName("ELECTRONICS"));
    }

}
