package pl.wit.shop.product.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.With;
import org.springframework.test.util.ReflectionTestUtils;
import pl.wit.shop.product.test.data.ProductCategoryTestDataIdentifiers;

@With
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductCategoryBuilder implements ProductCategoryTestDataIdentifiers {
    private final Long id;
    private final String name;

    private ProductCategoryBuilder() {
        this.id = PRODUCT_CATEGORY_1_ID;
        this.name = "HOME";
    }

    public static ProductCategoryBuilder aHomeProductCategory() {
        return new ProductCategoryBuilder();
    }

    public ProductCategory build() {
        ProductCategory productCategory = new ProductCategory();
        ReflectionTestUtils.setField(productCategory, "id", id);
        ReflectionTestUtils.setField(productCategory, "name", name);
        return productCategory;
    }
}