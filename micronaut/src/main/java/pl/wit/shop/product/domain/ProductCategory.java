package pl.wit.shop.product.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PACKAGE, force = true)
public class ProductCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_category_seq_gen")
    @SequenceGenerator(name = "product_category_seq_gen", sequenceName = "product_category_seq", allocationSize = 1)
    //TODO @Getter(AccessLevel.PRIVATE) causing problems
    private final Long id;

    @Column(unique = true)
    private String name;
}
