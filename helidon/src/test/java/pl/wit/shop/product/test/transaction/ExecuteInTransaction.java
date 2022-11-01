package pl.wit.shop.product.test.transaction;

/**
 * Allows executing multiple operations within single transaction
 * */
@FunctionalInterface
public interface ExecuteInTransaction {
    void execute();
}
