/*
 * Copyright \(c\) "2022" Red Hat and others
 *
 * This program and the accompanying materials are made available under the Apache Software License 2.0 which is available at:
 *  https://www.apache.org/licenses/LICENSE-2.0.
 *
 *  SPDX-License-Identifier: Apache-2.0
 */

package ee.jakarta.tck.coreprofile.rs;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.InvocationInterceptor;
import org.junit.jupiter.api.extension.ReflectiveInvocationContext;

/**
 * JUnit 5 does not have a method level exclusion pattern, so we have to intercept each
 * test method to check for the XML binding methods that were not correctly tagged with
 * xml_binding.
 */
public class CoreProfileRestTCKSuiteExtension implements InvocationInterceptor {
    /**
     * These are tests that make use of XML binding but do not have the xml_binding tag on them
     */
    static final String[] SKIPPED = {
            "ee.jakarta.tck.ws.rs.jaxrs21.ee.sse.sseeventsource.JAXRSClientIT.jaxbElementTest",
            "ee.jakarta.tck.ws.rs.jaxrs21.ee.sse.sseeventsource.JAXRSClientIT.xmlTest",
            "ee.jakarta.tck.ws.rs.jaxrs21.ee.sse.sseeventsink.JAXRSClientIT.jaxbElementTest",
    };
    static final HashSet<String> SKIPPED_METHODS = new HashSet<>(Arrays.asList(SKIPPED));

    @Override
    public void interceptTestMethod(Invocation<Void> invocation, ReflectiveInvocationContext<Method> invocationContext, ExtensionContext extensionContext) throws Throwable {
        Method testMethod = invocationContext.getExecutable();
        String name = testMethod.getName();
        String key = testMethod.getDeclaringClass().getName() + "." + name;
        if(SKIPPED_METHODS.contains(key)) {
            System.out.printf("+++ CoreProfileRestTCKSuite.interceptTestMethod(%s) skipped\n", invocationContext.getExecutable());
            invocation.skip();
        } else {
            invocation.proceed();
        }
    }
}
