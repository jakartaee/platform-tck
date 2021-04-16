package org.jboss.cdi.tck.interceptors.tests.contract.aroundInvoke.ee;

import static org.testng.Assert.assertEquals;

import jakarta.interceptor.Interceptors;
import javax.naming.InitialContext;
import jakarta.transaction.TransactionSynchronizationRegistry;
import org.testng.Assert;

public class Baz {

    private static Object key;

    public static Object getKey() {
        return key;
    }

    @Interceptors(BazInterceptor.class)
    void ping() throws Exception {
        TransactionSynchronizationRegistry tsr = (TransactionSynchronizationRegistry) InitialContext
                .doLookup("java:comp/TransactionSynchronizationRegistry");
        key = tsr.getTransactionKey();
        Assert.assertEquals(key, Bar.getKey());
    }
}
