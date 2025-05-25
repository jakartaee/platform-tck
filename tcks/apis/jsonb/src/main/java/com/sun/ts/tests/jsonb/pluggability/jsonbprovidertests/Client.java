package com.sun.ts.tests.jsonb.pluggability.jsonbprovidertests;

import com.sun.ts.lib.harness.Status;
import com.sun.ts.tests.common.base.ServiceEETest;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.spi.JsonbProvider;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.ServiceLoader;

public class Client extends ServiceEETest {

    static final System.Logger logger = System.getLogger(Client.class.getName());


    private static final String MY_JSONBROVIDER_CLASS = "com.sun.ts.tests.jsonb.provider.MyJsonbProvider";
    private static final String MY_JSONBBUILDER_CLASS = "com.sun.ts.tests.jsonb.provider.MyJsonbBuilder";

    public static void main(String[] args) {
        Client theTests = new Client();
        Status s = theTests.run(args, System.out, System.err);
        s.exit();
    }

    /* Test setup */
    /*
     * @class.setup_props: This is needed by the vehicle base classes
     */
    public void setup(String[] args, Properties p) throws Exception {
        //super.setup(args, p);
    }
    public void cleanup() {

    }

    /* Tests */

    /*
     * @testName: jsonbProviderTest1
     *
     * @test_Strategy: Test call of SPI provider method with signature: o public static JsonbProvider provider()
     */
    public void jsonbProviderTest1() throws Exception {
        try {
            // Load any provider
            JsonbProvider provider = JsonbProvider.provider();
            String providerClass = provider.getClass().getName();
            logger.log(System.Logger.Level.INFO, "provider class=" + providerClass);
        } catch (Exception e) {
            throw new Exception("jsonbProviderTest1 Failed: ", e);
        }
    }

    /*
     * @testName: jsonbProviderTest2
     *
     * @test_Strategy: Test call of SPI provider method with signature: o public static JsonbProvider provider(String
     * provider)
     */
    public void jsonbProviderTest2() throws Exception {
        boolean pass = true;
        try {
            // Load my provider
            JsonbProvider provider = JsonbProvider.provider(MY_JSONBROVIDER_CLASS);
            String providerClass = provider.getClass().getName();
            logger.log(System.Logger.Level.INFO, "provider class=" + providerClass);
            if (providerClass.equals(MY_JSONBROVIDER_CLASS)) {
                logger.log(System.Logger.Level.INFO, "Current provider is my provider - expected.");
            } else {
                logger.log(System.Logger.Level.ERROR, "Current provider is not my provider - unexpected.");
                pass = false;
                ServiceLoader<JsonbProvider> loader = ServiceLoader.load(JsonbProvider.class);
                Iterator<JsonbProvider> it = loader.iterator();
                List<JsonbProvider> providers = new ArrayList<>();
                while (it.hasNext()) {
                    providers.add(it.next());
                }
                logger.log(System.Logger.Level.INFO, "Providers: " + providers);
            }
        } catch (Exception e) {
            throw new Exception("jsonbProviderTest2 Failed: ", e);
        }
        if (!pass) {
            throw new Exception("jsonbProviderTest2 Failed");
        }
    }

    /*
     * @testName: jsonbProviderTest3
     *
     * @test_Strategy: Test call of provider method with signature: o public JsonbBuilder create()
     */
    public void jsonbProviderTest3() throws Exception {
        try {
            // Load my provider
            JsonbBuilder builder = JsonbProvider.provider(MY_JSONBROVIDER_CLASS).create();
            String providerClass = builder.getClass().getName();
            logger.log(System.Logger.Level.INFO, "jsonb builder class=" + providerClass);
            if (providerClass.equals(MY_JSONBBUILDER_CLASS)) {
                logger.log(System.Logger.Level.INFO, "Current jsonb builder is my builder - expected.");
            } else {
                logger.log(System.Logger.Level.ERROR, "Current jsonb builder is not my builder - unexpected.");
                throw new Exception("jsonbProviderTest3 Failed");
            }
        } catch (Exception e) {
            throw new Exception("jsonbProviderTest3 Failed: ", e);
        }
    }

}
