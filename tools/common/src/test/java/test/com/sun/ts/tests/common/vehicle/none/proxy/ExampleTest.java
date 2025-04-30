package test.com.sun.ts.tests.common.vehicle.none.proxy;

/**
 * Basic test class target that does not have a setup(String[]) method.
 */
public class ExampleTest {
    public void test1() {
        System.out.println("ExampleTest.test1");
    }
    public void test1Ex() throws Exception {
        System.out.println("ExampleTest.test1Ex, throwing exception");
        throw new Exception("ExampleTest.test1Ex");
    }
    public void test2() throws Exception {
        System.out.println("ExampleTest.test2");
    }
}
