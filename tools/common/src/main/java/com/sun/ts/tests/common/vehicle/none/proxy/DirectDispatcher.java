package com.sun.ts.tests.common.vehicle.none.proxy;

import com.sun.ts.lib.harness.RemoteStatus;
import com.sun.ts.lib.harness.Status;

import java.lang.reflect.Method;
import java.util.function.Function;

/**
 * A function that dispatches method calls to a local instance using reflection. This is useful for testing.
 */
public class DirectDispatcher implements Function<Object[], RemoteStatus> {
    private final Object testCase;
    public DirectDispatcher(Object testCase) {
        this.testCase = testCase;
    }

    @Override
    public RemoteStatus apply(Object[] args) {
        String methodName = args[args.length - 1].toString();
        System.out.println("Invoking " + methodName);
        Method testMethod;
        try {
            testMethod = testCase.getClass().getDeclaredMethod(methodName);
        } catch (NoSuchMethodException e) {
            return new RemoteStatus(Status.failed("Method not found: " + methodName));
        }
        try {
            testMethod.invoke(testCase);
        } catch (Throwable e) {
            return new RemoteStatus(Status.failed("Test failed: "+methodName), e);
        }
        return new RemoteStatus(Status.passed("Test passed"));
    }
}
