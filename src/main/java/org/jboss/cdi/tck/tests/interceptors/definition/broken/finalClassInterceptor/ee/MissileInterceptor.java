package org.jboss.cdi.tck.tests.interceptors.definition.broken.finalClassInterceptor.ee;

import java.io.Serializable;

import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;

@Interceptor
@FooBinding
public class MissileInterceptor implements Serializable {
    public static boolean intercepted = false;

    @AroundInvoke
    public Object alwaysReturnThis(InvocationContext ctx) throws Exception {
        intercepted = true;
        return ctx.proceed();
    }
}
