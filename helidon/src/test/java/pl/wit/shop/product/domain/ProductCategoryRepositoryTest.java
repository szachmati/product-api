package pl.wit.shop.product.domain;

import io.helidon.microprofile.tests.junit5.Configuration;
import io.helidon.microprofile.tests.junit5.HelidonTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import pl.wit.shop.product.test.base.BaseDatabaseTest;
import pl.wit.shop.product.test.transaction.TransactionOperations;

import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static pl.wit.shop.product.domain.ProductCategoryBuilder.anElectronicsProductCategory;
import static pl.wit.shop.product.domain.ProductCategoryMatcher.isProductCategory;

@HelidonTest
@Configuration(useExisting = true)
public class ProductCategoryRepositoryTest extends BaseDatabaseTest implements TransactionOperations {

    @Inject
    private ProductCategoryRepository productCategoryRepository;

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
