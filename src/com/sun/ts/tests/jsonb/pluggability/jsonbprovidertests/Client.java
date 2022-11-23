/*
 * Copyright (c) 2022 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

/*
 * $Id$
 */
package com.sun.ts.tests.jsonb.pluggability.jsonbprovidertests;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.ServiceEETest;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.spi.JsonbProvider;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.ServiceLoader;

public class Client extends ServiceEETest {
    private static final String MY_JSONBROVIDER_CLASS = "com.sun.ts.tests.jsonb.provider.MyJsonbProvider";
    private static final String MY_JSONBBUILDER_CLASS = "com.sun.ts.tests.jsonb.provider.MyJsonbBuilder";

    public static void main(String[] args) {
        Client theTests = new Client();
        Status s = theTests.run(args, System.out, System.err);
        s.exit();
    }

    /* Test setup */

    /*
     * @class.setup_props:
     */
    public void setup(String[] args, Properties p) throws Fault {
        logMsg("setup ok");
    }

    public void cleanup() throws Fault {
        logMsg("cleanup ok");
    }

    /* Tests */

    /*
     * @testName: jsonbProviderTest1
     *
     * @test_Strategy: Test call of SPI provider method with signature: o public
     * static JsonbProvider provider()
     */
    public void jsonbProviderTest1() throws Fault {
        try {
            // Load any provider
            JsonbProvider provider = JsonbProvider.provider();
            String providerClass = provider.getClass().getName();
            logMsg("provider class=" + providerClass);
        } catch (Exception e) {
            throw new Fault("jsonbProviderTest1 Failed: ", e);
        }
    }

    /*
     * @testName: jsonbProviderTest2
     *
     * @test_Strategy: Test call of SPI provider method with signature: o public
     * static JsonbProvider provider(String provider)
     */
    public void jsonbProviderTest2() throws Fault {
        boolean pass = true;
        try {
            // Load my provider
            JsonbProvider provider = JsonbProvider.provider(MY_JSONBROVIDER_CLASS);
            String providerClass = provider.getClass().getName();
            logMsg("provider class=" + providerClass);
            if (providerClass.equals(MY_JSONBROVIDER_CLASS)) logMsg("Current provider is my provider - expected.");
            else {
                logErr("Current provider is not my provider - unexpected.");
                pass = false;
                ServiceLoader<JsonbProvider> loader = ServiceLoader.load(JsonbProvider.class);
                Iterator<JsonbProvider> it = loader.iterator();
                List<JsonbProvider> providers = new ArrayList<>();
                while (it.hasNext()) {
                    providers.add(it.next());
                }
                logMsg("Providers: " + providers);
            }
        } catch (Exception e) {
            throw new Fault("jsonbProviderTest2 Failed: ", e);
        }
        if (!pass) throw new Fault("jsonbProviderTest2 Failed");
    }

    /*
     * @testName: jsonbProviderTest3
     *
     * @test_Strategy: Test call of provider method with signature: o public JsonbBuilder create()
     */
    public void jsonbProviderTest3() throws Fault {
        try {
            // Load my provider
            JsonbBuilder builder = JsonbProvider.provider(MY_JSONBROVIDER_CLASS).create();
            String providerClass = builder.getClass().getName();
            logMsg("jsonb builder class=" + providerClass);
            if (providerClass.equals(MY_JSONBBUILDER_CLASS)) logMsg("Current jsonb builder is my builder - expected.");
            else {
                logErr("Current jsonb builder is not my builder - unexpected.");
                throw new Fault("jsonbProviderTest3 Failed");
            }
        } catch (Exception e) {
            throw new Fault("jsonbProviderTest3 Failed: ", e);
        }
    }
}
