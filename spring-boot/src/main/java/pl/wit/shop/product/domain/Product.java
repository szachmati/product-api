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
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

import static java.util.Objects.requireNonNull;

@Entity
@Getter
@EqualsAndHashCode(of = "uuid", callSuper = false)
@NoArgsConstructor(access = AccessLevel.PACKAGE, force = true)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_seq_gen")
    @SequenceGenerator(name = "product_seq_gen", sequenceName = "product_seq", allocationSize = 1)
    @Getter(AccessLevel.PRIVATE)
    private final Long id;

    @NotNull
    @Column(updatable = false, unique = true)
    private final UUID uuid;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private ProductCategory category;

    @NotBlank
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

