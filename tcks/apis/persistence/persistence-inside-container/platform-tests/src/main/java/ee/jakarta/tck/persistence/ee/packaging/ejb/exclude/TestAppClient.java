package ee.jakarta.tck.persistence.ee.packaging.ejb.exclude;

import com.sun.ts.tests.common.vehicle.none.proxy.AppClient;

/**
 * The appclient main entry point that creates a proxy to the server side client test
 */
public class TestAppClient extends AppClient<IClient, IClientProxy> {
    public static void main(String[] args) throws Exception {
        new TestAppClient().run(args);
    }
}
