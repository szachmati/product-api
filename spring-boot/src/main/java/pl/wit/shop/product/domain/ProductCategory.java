package pl.wit.shop.product.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PACKAGE, force = true)
public class ProductCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_category_seq_gen")
    @SequenceGenerator(name = "product_category_seq_gen", sequenceName = "product_category_seq", allocationSize = 1)
    @Getter(AccessLevel.PRIVATE)
    private final Long id;

    @NotBlank
    @Column(unique = true)
    private String name;
}
