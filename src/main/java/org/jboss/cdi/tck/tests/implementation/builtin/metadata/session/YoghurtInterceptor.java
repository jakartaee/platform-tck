package org.jboss.cdi.tck.tests.implementation.builtin.metadata.session;

import java.io.Serializable;

import jakarta.enterprise.inject.Intercepted;
import jakarta.enterprise.inject.spi.Bean;
import jakarta.enterprise.inject.spi.Interceptor;
import jakarta.inject.Inject;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.InvocationContext;

@SuppressWarnings("serial")
@jakarta.interceptor.Interceptor
@Frozen
public class YoghurtInterceptor implements Serializable {

    @Inject
    private Bean<YoghurtInterceptor> bean;
    @Inject
    private Interceptor<YoghurtInterceptor> interceptor;
    @Inject
    @Intercepted
    private Bean<?> interceptedBean;

    @AroundInvoke
    public Object intercept(InvocationContext ctx) throws Exception {
        return this;
    }

    public Bean<YoghurtInterceptor> getBean() {
        return bean;
    }

    public Interceptor<YoghurtInterceptor> getInterceptor() {
        return interceptor;
    }

    public Bean<?> getInterceptedBean() {
        return interceptedBean;
    }
}
