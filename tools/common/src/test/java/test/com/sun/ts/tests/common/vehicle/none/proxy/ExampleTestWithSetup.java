package test.com.sun.ts.tests.common.vehicle.none.proxy;

/**
 * An example test class target that has a setup(String[]) method.
 */
public class ExampleTestWithSetup {
    public void setup(String[] args) {
        System.out.println("ExampleTestWithSetup.setup");
        for (String arg : args) {
            System.out.println("arg: " + arg);
        }
    }
    public void test1() {
        System.out.println("ExampleTestWithSetup.test1");
    }
    public void test1Ex() throws Exception {
        System.out.println("ExampleTestWithSetup.test1Ex, throwing exception");
        throw new Exception("ExampleTestWithSetup.test1Ex");
    }
    public void test2() throws Exception {
        System.out.println("ExampleTestWithSetup.test2");
    }
}
