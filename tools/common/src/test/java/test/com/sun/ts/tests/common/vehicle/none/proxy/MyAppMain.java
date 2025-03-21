package test.com.sun.ts.tests.common.vehicle.none.proxy;

import com.sun.ts.tests.common.vehicle.none.proxy.AppClient;

import java.io.IOException;

public class MyAppMain extends AppClient<IExample, ExampleProxyHandler> {
    public static void main(String[] args) throws IOException {
        new MyAppMain().run(args);
    }
}
