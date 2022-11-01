package pl.wit.shop.product.test.transaction;


import jakarta.transaction.TransactionManager;


public interface TransactionOperations {

    TransactionManager getTransactionManager();

    default void inTransactionWithoutResult(ExecuteInTransaction executor) {
        TransactionManager tm = getTransactionManager();
        try {
            tm.begin();
            executor.execute();
            tm.commit();
        } catch (Exception e) {
            System.err.println(e);
        }
    }
}
