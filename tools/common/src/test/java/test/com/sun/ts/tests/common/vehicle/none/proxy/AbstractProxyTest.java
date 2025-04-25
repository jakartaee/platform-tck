package test.com.sun.ts.tests.common.vehicle.none.proxy;

import com.sun.ts.lib.harness.RemoteStatus;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

/**
 * Tests of the AbstractBaseProxy and related classes.
 */
public class AbstractProxyTest {
    @Test
    public void testGenericTypes() {
        ExampleProxyHandler proxyHandler = new ExampleProxyHandler();
        System.out.println(proxyHandler.getGenericInfo());
    }
    @Test
    public void testProxyEquals() {
        ExampleProxyHandler proxyHandler = new ExampleProxyHandler();
        IExample proxy = proxyHandler.newProxy(new ExampleTest());
        boolean equals = proxyHandler.equals(this);
        System.out.println("testProxyEquals, "+equals);
    }
    @Test
    public void testProxyHashCode() {
        ExampleProxyHandler proxyHandler = new ExampleProxyHandler();
        IExample proxy = proxyHandler.newProxy(new ExampleTest());
        int hashCode = proxyHandler.hashCode();
        System.out.println("testProxyHashCode, "+hashCode);
    }
    @Test
    public void testProxyToString() {
        ExampleProxyHandler proxyHandler = new ExampleProxyHandler();
        IExample proxy = proxyHandler.newProxy(new ExampleTest());
        String toString = proxyHandler.toString();
        System.out.println("testProxyToString, "+toString);
    }
    @Test
    public void methodHandleTest() throws Throwable {
        ExampleProxyHandler proxyHandler = new ExampleProxyHandler();
        IExample proxy = proxyHandler.newProxy(new ExampleTest());

        MethodHandles.Lookup publicLookup = MethodHandles.publicLookup();
        MethodType mt = MethodType.methodType(RemoteStatus.class);

        MethodHandle test1 = publicLookup.findVirtual(IExample.class, "test1", mt);
        RemoteStatus status = (RemoteStatus) test1.invoke(proxy);
        System.out.println(status);

        MethodHandle test2 = publicLookup.findVirtual(IExample.class, "test2", mt);
        RemoteStatus status2 = (RemoteStatus) test2.invoke(proxy);
        System.out.println(status2);
    }

    @Test
    public void testDirectRunnerTest() {
        TestRunner testRunner = new TestRunner();
        testRunner.runTests();
    }

    @Test
    public void testHttpRunnerTest() throws IOException {
        TestHttpRunner testRunner = new TestHttpRunner();
        testRunner.runTests();
    }

    @Test
    public void testHttpRunnerTestWithSetup() throws IOException {
        TestHttpRunner testRunner = new TestHttpRunner();
        String[] args = new String[]{"-p", "tmp/tstest.jte"};
        testRunner.runTests(args);
    }

    @Test
    public void testHttpRunnerTestWithSetupProps() throws IOException {
        TestHttpRunner testRunner = new TestHttpRunner();
        String[] args = new String[]{"-p", "tmp/tstest.jte"};
        testRunner.runTests(args, System.getProperties());
    }
}
