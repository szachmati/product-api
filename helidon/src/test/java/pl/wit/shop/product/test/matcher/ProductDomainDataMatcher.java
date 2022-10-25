package pl.wit.shop.product.test.matcher;

import io.github.marmer.annotationprocessing.MatcherConfiguration;

@MatcherConfiguration({
        "pl.wit.shop.product.domain.Product",
        "pl.wit.shop.product.domain.ProductCategory"
})
public class ProductDomainDataMatcher {
}
