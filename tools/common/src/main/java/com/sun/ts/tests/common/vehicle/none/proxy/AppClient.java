package com.sun.ts.tests.common.vehicle.none.proxy;

import com.sun.ts.lib.harness.RemoteStatus;
import com.sun.ts.lib.harness.Status;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Properties;

/**
 * The appclient main entry point that creates a proxy to the server side client test. Used by the
 * non-vehicle appclient tests that were previously invoking remote ejbs.
 */
public class AppClient<IC, ICP extends InvocationProxy<IC>> {
    private static final Class[] appClasses = {
            com.sun.ts.tests.common.vehicle.none.proxy.AbstractBaseProxy.class,
            com.sun.ts.tests.common.vehicle.none.proxy.AppClient.class,
            com.sun.ts.tests.common.vehicle.none.proxy.InvocationProxy.class,
            com.sun.ts.tests.common.vehicle.none.proxy.ServletDispatcher.class,
    };

    /**
     * Classes used by the AppClient that needs to be added to the proxy deployment.
     * @return the classes used by the AppClient
     */
    public static Class[] getAppClasses() {
        return appClasses;
    }

    /* appclient entry point */
    public void run(String[] args) throws IOException{
        String testName = "";
        boolean exit = true;

        // Get the test to run and the test properties
        Properties testProps = null;
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-t")) {
                testName = args[++i];
            } else if (args[i].equals("-p")) {
                testProps = readProperties(args[++i]);
            } else if (args[i].equals("-x")) {
                exit = false;
            }
        }
        // Validate props
        if (testProps == null) {
            throw new IllegalArgumentException("Test properties not provided, use -p <properties file>");
        }
        String webServerHost = testProps.getProperty("webServerHost", "localhost");
        String webServerPort = testProps.getProperty("webServerPort", "8080");

        // Invoke the setup

        // Invoke the test
        RemoteStatus status = runTest(testName, webServerHost, webServerPort);
        if(status.toStatus().isFailed()) {
            System.out.println("FAILED: " + testName);
            System.err.printf("%s failed\n, additional stuats:\n", testName);
            System.err.println(status);
        } else {
            System.out.println("PASSED: " + testName);
        }
        if (exit) {
            status.toStatus().exit();
        }
    }

    /**
     * Run the test by invoking the client proxy
     * @param testName - name from the -t command line argument
     * @param webServerHost - host from the ts.jte file
     * @param webServerPort - port from the ts.jte file
     * @return the status of the test
     */
    RemoteStatus runTest(String testName, String webServerHost, String webServerPort) {
        RemoteStatus status;
        try {
            ICP clientProxy = getInvocationProxy(webServerHost, webServerPort);
            status = clientProxy.runTest(testName);
        } catch (Throwable e) {
            status = new RemoteStatus(Status.failed(testName+"  failure in main"), e);
        }
        return status;
    }

    /**
     * Read the properties file specified on the command line
     * @param fileName - the name of the properties file
     * @return the properties
     * @throws IOException - on failure
     */
    private static Properties readProperties(String fileName) throws IOException {
        Properties props = new Properties();
        props.load(new FileReader(fileName));
        return props;
    }

    protected ICP getInvocationProxy(String webServerHost, String webServerPort) throws NoSuchMethodException,
            InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<ICP> clientProxyClass = getInvocationProxyClass();
        Constructor<ICP> clientProxyCtor = clientProxyClass.getDeclaredConstructor();
        ICP clientProxy = clientProxyCtor.newInstance();
        clientProxy.newProxy(webServerHost, webServerPort);
        return clientProxy;
    }

    /**
     * Get the invocation proxy class from the reified type arguments
     * @return the invocation proxy class
     */
    protected Class<ICP> getInvocationProxyClass() {
        Class<?>[] typeArguments = getReifiedTypeArguments();
        return (Class<ICP>) typeArguments[1];
    }

    /**
     * Get the reified type arguments bound by the subclass
     * @return - the reified type arguments
     */
    protected Class<?>[] getReifiedTypeArguments() {
        Class<?> currentClass = getClass();
        ParameterizedType genericType = (ParameterizedType) currentClass.getGenericSuperclass();
        // Get the actual type arguments
        Type[] typeArguments = genericType.getActualTypeArguments();
        Class<?>[] reifiedTypeArguments = { (Class<?>) typeArguments[0], (Class<?>) typeArguments[1] };
        return reifiedTypeArguments;
    }
}
