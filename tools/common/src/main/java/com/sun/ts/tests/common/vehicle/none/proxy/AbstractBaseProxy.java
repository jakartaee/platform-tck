package com.sun.ts.tests.common.vehicle.none.proxy;

import com.sun.ts.lib.harness.RemoteStatus;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Properties;
import java.util.function.Function;

/**
 * An abstract base class for creating dynamic proxies used to dispatch method calls to a remote server.
 * @param <I> - The interface type of the proxy used to represent the test methods
 * @param <IP> - The invocation proxy type used to dispatch the method calls
 */
public abstract class AbstractBaseProxy<I, IP extends InvocationProxy> implements InvocationProxy<I> {

    private Function<Object[], RemoteStatus> dispatcher;
    private I lastProxy;

    /**
     * Optional setup method that can be overridden by subclasses to perform any necessary setup.
     * @param args - the command line arguments
     */
    public void setup(String[] args) {}
    /**
     * Optional setup method that can be overridden by subclasses to perform any necessary setup.
     * @param args - the command line arguments
     * @param props - test properties
     */
    public void setup(String[] args, Properties props) {}

    /**
     * Implemented by the IP class to dispatch the method call to the remote server.
     * @param testName - a test method name
     * @return - the status of the test method result
     */
    public abstract RemoteStatus runTest(String testName);

    /**
     * Creates a new proxy instance of the interface type I that is not bound to a specific dispatcher.
     * @return - a new proxy instance
     */
    public I newProxy() {
        ClassLoader cl = AbstractBaseProxy.class.getClassLoader();
        Class<I> interfaceClass = getInterfaceClass();
        Class<?>[] interfaces = {interfaceClass};
        I proxy = interfaceClass.cast(Proxy.newProxyInstance(cl, interfaces, this));
        lastProxy = proxy;
        return proxy;
    }

    /**
     * Creates a new proxy instance of the interface type I that is bound to the direct dispatcher.
     * Useful for testing, but not tck tests.
     * @param testCase - the test case instance
     * @return - a new proxy instance
     */
    public I newProxy(Object testCase) {
        I proxy = newProxy();
        useDirectDispatcher(testCase);
        return proxy;
    }

    /**
     * Creates a new proxy instance of the interface type I that is bound to the servlet dispatcher.
     * @param host - servlet host
     * @param port - servlet port
     * @return - a new proxy instance
     */
    public I newProxy(String host, String port) {
        I proxy = newProxy();
        useServletDispatcher(host, port);
        return proxy;
    }

    /**
     * Returns the last proxy instance created.
     * @return the last proxy instance
     */
    public I getLastProxy() {
        return lastProxy;
    }
    public Class<I> getInterfaceClass() {
        Class<?>[] typeArguments = getReifiedTypeArguments();
        return (Class<I>) typeArguments[0];
    }
    /**
     * Returns the invocation proxy class.
     * @return the invocation proxy class
     */
    public Class<IP> getInvocationProxyClass() {
        Class<?>[] typeArguments = getReifiedTypeArguments();
        return (Class<IP>) typeArguments[1];
    }

    /**
     * Utility method to get the reified type arguments of the proxy.
     * @return [0] - the interface type, [1] - the proxy type
     */
    public Class<?>[] getReifiedTypeArguments() {
        Class<?> currentClass = getClass();
        ParameterizedType genericType = (ParameterizedType) currentClass.getGenericSuperclass();
        // Get the actual type arguments
        Type[] typeArguments = genericType.getActualTypeArguments();
        Class<?>[] reifiedTypeArguments = { (Class<?>) typeArguments[0], (Class<?>) typeArguments[1] };
        return reifiedTypeArguments;
    }

    /**
     * Utility method to get the generic type information of the proxy.
     * @return - a string representation of the generic type information
     */
    public String getGenericInfo() {
        Class<?>[] typeArguments = getReifiedTypeArguments();
        StringBuilder genericInfo = new StringBuilder();
        genericInfo.append("I.class: ");
        genericInfo.append(typeArguments[0].toString());
        genericInfo.append("\nIH.class: ");
        genericInfo.append(typeArguments[1].toString());
        return genericInfo.toString();
    }

    /**
     * Get the dispatcher that handles the method calls.
     * @return - the dispatcher
     */
    public Function<Object[], RemoteStatus> getDispatcher() {
        return dispatcher;
    }

    /**
     * Provide a custom dispatcher to handle the method calls.
     * @param dispatcher - a function that takes an array of objects and returns a RemoteStatus. The last
     *                   element in the array is the test method name.
     */
    public void setDispatcher(Function<Object[], RemoteStatus> dispatcher) {
        this.dispatcher = dispatcher;
    }

    /**
     * Use the DirectDispatcher to handle the method calls.
     * @param testCase - the test case instance
     */
    public void useDirectDispatcher(Object testCase) {
        setDispatcher(new DirectDispatcher(testCase));
    }

    /**
     * Use the ServletDispatcher to handle the method calls.
     * @param host - servlet host
     * @param port - servlet port
     */
    public void useServletDispatcher(String host, String port) {
        setDispatcher(new ServletDispatcher(host, port));
    }

    /**
     *
     * @param proxy the proxy instance that the method was invoked on
     *
     * @param method the {@code Method} instance corresponding to
     * the interface method invoked on the proxy instance.
     *
     * @param args an array of objects containing the values of the
     * arguments passed in the method invocation on the proxy instance,
     * or {@code null} if interface method takes no arguments.
     *
     *   This method handles the Object methods toString, hashCode, and equals.
     *   All other methods are dispatched to the dispatcher.
     *
     * @return - the result of the method call
     * @throws Throwable on failure
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String methodName = method.getName();

        // Handle Object methods
        switch (methodName) {
            case "toString" -> {
                return this.getClass().getName() + "-proxy";
            }
            case "hashCode" -> {
                return this.hashCode();
            }
            case "equals" -> {
                return this.equals(proxy);
            }
        }
        //
        if (dispatcher != null) {
            Object[] newArgs = copyOf(args);
            newArgs[newArgs.length-1] = methodName;
            return dispatcher.apply(newArgs);
        }
        return null;
    }
    private Object[] copyOf(Object[] args) {
        Object[] newArgs;
        if(args != null) {
            newArgs = Arrays.copyOf(args, args.length + 1);
        } else {
            newArgs = new Object[1];
        }
        return newArgs;
    }
}
