package pl.wit.shop.product.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.UUID;

import static java.util.Objects.requireNonNull;

@Entity
@Getter
@EqualsAndHashCode(of = "uuid", callSuper = false)
@NoArgsConstructor(access = AccessLevel.PACKAGE, force = true)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "product_seq", sequenceName = "product_seq" , allocationSize = 1)
    @Getter(AccessLevel.PRIVATE)
    private final Long id;

    @NotNull
    @Column(updatable = false, unique = true)
    private final UUID uuid;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private ProductCategory category;

    @NotNull
    private String name;

    @NotNull
    private BigDecimal price;

    Product(ProductCategory category, String name, BigDecimal price) {
        this.id = null;
        this.uuid = UUID.randomUUID();
        this.category = requireNonNull(category);
        this.name = requireNonNull(name);
        this.price = requireNonNull(price);
    }

    Product update(ProductCategory category, String name, BigDecimal price) {
        this.category = requireNonNull(category);
        this.name = requireNonNull(name);
        this.price = requireNonNull(price);
        return this;
    }
}

