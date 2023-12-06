/*
 * Copyright (c) 2023 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.javamail.pluggability;

import java.io.*;
import java.util.*;
import jakarta.mail.*;
import com.sun.javatest.*;
import com.sun.javatest.lib.MultiTest;
import com.sun.ts.lib.harness.ServiceEETest;


import jakarta.mail.util.StreamProvider;

/**
 * Checks that a custom implementation of can be loaded by SPI added for MAIL 2.1.
 */
public class StreamProviderTest extends ServiceEETest {

    private static final String MY_STREAM_PROVIDER_CLASS = "com.sun.ts.tests.javamail.provider.MyMailProvider";

      /* Test setup: */
    public void setup(String[] args, Properties props) throws Fault {

    }


    public static void main(String argv[]) {
        StreamProviderTest lTest = new StreamProviderTest();
        Status lStatus = lTest.mailProviderTest();
        lStatus.exit();
    }

     /* Tests */

  /*
   * @testName: mailProviderTest
   * 
   * 
   * @test_Strategy: Test call of SPI provider method 
   */

    public Status mailProviderTest() {
        boolean pass = true;
        try {
          // Load my provider
          StreamProvider provider = StreamProvider.provider();
          String providerClass = provider.getClass().getName();
          System.out.println("provider class=" + providerClass);
          if (providerClass.equals(MY_STREAM_PROVIDER_CLASS))
            System.out.println("Current provider is my provider - expected.");
          else {
            System.out.println("Current provider is not my provider - unexpected.");
            pass = false;
            ServiceLoader<StreamProvider> loader = ServiceLoader.load(StreamProvider.class);
            Iterator<StreamProvider> it = loader.iterator();
            List<StreamProvider> providers = new ArrayList<>();
            while(it.hasNext()) {
                providers.add(it.next());
            }
            System.out.println("Providers: "+providers);
          }
        } catch (Exception e) {
            e.printStackTrace();
          return Status.failed(e.getMessage());
        }
        if (!pass)
            return Status.failed("Stream Provider test failed");
        else
            return Status.passed("Stream Provider test Passed");
    }

    /* cleanup */
    public void cleanup() throws Fault {
     
    }

}