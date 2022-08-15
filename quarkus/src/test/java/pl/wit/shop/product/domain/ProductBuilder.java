package pl.wit.shop.product.domain;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.With;
import org.apache.commons.lang3.reflect.FieldUtils;
import pl.wit.shop.product.test.data.ProductTestDataIdentifiers;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.UUID;

import static pl.wit.shop.product.domain.ProductCategoryBuilder.aHomeProductCategory;
import static pl.wit.shop.product.domain.ProductCategoryBuilder.anElectronicsProductCategory;

@With
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductBuilder implements ProductTestDataIdentifiers {
   private final Long id;
   private final UUID uuid;
   private final String name;
   private final ProductCategory category;
   private final BigDecimal price;

    private ProductBuilder() {
        this.id = PRODUCT_1_ID;
        this.uuid = PRODUCT_1_UUID;
        this.name = "Home product";
        this.category = aHomeProductCategory().build();
        this.price = BigDecimal.ONE;
    }

    public static ProductBuilder aFirstHomeProduct() {
        return new ProductBuilder();
    }

    public static ProductBuilder aSecondHomeProduct() {
        return new ProductBuilder()
                .withId(PRODUCT_2_ID)
                .withUuid(PRODUCT_2_UUID)
                .withName("Second home product")
                .withCategory(aHomeProductCategory().build())
                .withPrice(new BigDecimal("30.87"));
    }

    public static ProductBuilder aMonitorProduct() {
        return new ProductBuilder()
                .withId(PRODUCT_3_ID)
                .withUuid(PRODUCT_3_UUID)
                .withName("Monitor")
                .withCategory(anElectronicsProductCategory().build())
                .withPrice(new BigDecimal("20.44"));
    }

    public Product build() {
       Product product = new Product(category, name, price);
       Field uuid = FieldUtils.getField(Product.class, "uuid", true);
        try {
            FieldUtils.writeField(uuid, product, this.uuid);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return product;
    }


}