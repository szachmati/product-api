package pl.wit.shop.product.domain;




import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Getter
@EqualsAndHashCode(of = "uuid", callSuper = false)
@NoArgsConstructor(access = AccessLevel.PACKAGE, force = true)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter(AccessLevel.PRIVATE)
    private final Long id;

    @NotNull
    @Column(updatable = false, unique = true)
    private final UUID uuid;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private ProductCategory category;

    @NotNull
    @Column(unique = true)
    private String name;

    @NotNull
    private BigDecimal price;

    Product(ProductCategory category, String name, BigDecimal price) {
        this.id = null;
        this.uuid = UUID.randomUUID();
        this.category = category;
        this.name = name;
        this.price = price;
    }

    Product update(ProductCategory productCategory, String name, BigDecimal price) {
        this.category = productCategory;
        this.name = name;
        this.price = price;
        return this;
    }
}

