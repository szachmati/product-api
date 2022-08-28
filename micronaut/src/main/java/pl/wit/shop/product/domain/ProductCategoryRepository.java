package pl.wit.shop.product.domain;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

@Repository
interface ProductCategoryRepository extends CrudRepository<ProductCategory, Long> {

}
