/*
 * Copyright (c) 2013, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.concurrency.spec.ManagedExecutorService.inheritedapi;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.util.TestUtil;

public class Client extends EETest {

  private String host = null;

  private int port;

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /*
   * @class.setup_props: webServerHost; webServerPort;
   */
  public void setup(String[] args, Properties p) throws Fault {
    TestUtil.logMsg("setup");
    try {
      // get props
      port = Integer.parseInt(p.getProperty("webServerPort"));
      host = p.getProperty("webServerHost");

      // check props for errors
      if (port < 1) {
        throw new Exception("'port' in ts.jte must be > 0");
      }
      if (host == null) {
        throw new Exception("'host' in ts.jte must not be null ");
      }

    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("Setup failed!", e);
    }
  }

  /*
   * @testName: testBasicManagedExecutorService
   * 
   * @assertion_ids:
   * CONCURRENCY:SPEC:10.2;CONCURRENCY:SPEC:13;CONCURRENCY:SPEC:13.1;CONCURRENCY
   * :SPEC:13.2;
   * CONCURRENCY:SPEC:14;CONCURRENCY:SPEC:14.1;CONCURRENCY:SPEC:14.2;CONCURRENCY
   * :SPEC:14.3;
   * CONCURRENCY:SPEC:14.4;CONCURRENCY:SPEC:6.1;CONCURRENCY:SPEC:6.2;CONCURRENCY
   * :SPEC:8;
   * CONCURRENCY:SPEC:8.1;CONCURRENCY:SPEC:9;CONCURRENCY:SPEC:10;CONCURRENCY:
   * SPEC:10.2; CONCURRENCY:SPEC:12;CONCURRENCY:SPEC:19;CONCURRENCY:SPEC:27;
   * 
   * @test_Strategy: test basic function for ManagedExecutorService include
   * execute, submit, invokeAny, invokeAll, atMostOnce
   */

  public void testBasicManagedExecutorService() throws Fault {
    URL url;
    String resp = null;
    try {
      url = new URL(Util.getUrl(Constants.COMMON_SERVLET_URI, host, port));
      resp = TestUtil.getResponse(url.openConnection());
    } catch (MalformedURLException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    if (!Message.SUCCESSMESSAGE.equals(resp)) {
      throw new Fault(
          "testManagedExecutorService fail to get successful result.");
    }
  }

  public void cleanup() throws Fault {
    TestUtil.logMsg("test cleanup ok");
  }

}
