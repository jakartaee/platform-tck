package com.sun.ts.tests.jta.ee.common;

public interface TransactionStatus {

    public static final String[] transStatusArray = {
        "STATUS_ACTIVE",
        "STATUS_MARKED_ROLLBACK",
        "STATUS_PREPARED",
        "STATUS_COMMITED",
        "STATUS_ROLLBACK",
        "STATUS_UNKNOWN",
        "STATUS_NO_TRANSACTION",
        "STATUS_PREPARING",
        "STATUS_COMMITTING",
        "STATUS_ROLLING_BACK" };

}
