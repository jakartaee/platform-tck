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

package com.sun.ts.tests.concurrency.spec.ManagedExecutorService.managed_servlet.forbiddenapi;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.*;
import com.sun.ts.lib.porting.TSURL;
import com.sun.ts.lib.util.TestUtil;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

public class Client extends EETest {

  private static final String APP_URL = "/forbiddenapiTest_web";

  private static final String PROTOCOL = "http";

  private static final String WEBSERVERHOSTPROP = "webServerHost";

  private static final String WEBSERVERPORTPROP = "webServerPort";

  private String hostname;

  private int portnum;

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /**
   * @class.setup_props: all.props; all properties;
   */
  public void setup(String[] args, Properties p) throws Fault {
    hostname = p.getProperty(WEBSERVERHOSTPROP);
    portnum = Integer.parseInt(p.getProperty(WEBSERVERPORTPROP));
    TestUtil.logTrace("setup");
  }

  public void cleanup() throws Fault {
    TestUtil.logTrace("cleanup");
  }

  /*
   * @testName: testAwaitTermination
   * 
   * @assertion_ids:
   * CONCURRENCY:SPEC:23;CONCURRENCY:SPEC:24;CONCURRENCY:SPEC:24.1;
   * 
   * @test_Strategy:
   */
  public void testAwaitTermination() throws Fault {
    String res = request(Constants.OP_AWAITTERMINATION);
    checkResponse(res);
  }

  /*
   * @testName: testIsShutdown
   * 
   * @assertion_ids:
   * CONCURRENCY:SPEC:23;CONCURRENCY:SPEC:24;CONCURRENCY:SPEC:24.2;
   * 
   * @test_Strategy:
   */
  public void testIsShutdown() throws Fault {
    String res = request(Constants.OP_ISSHUTDOWN);
    checkResponse(res);
  }

  /*
   * @testName: testIsTerminated
   * 
   * @assertion_ids:
   * CONCURRENCY:SPEC:23;CONCURRENCY:SPEC:24;CONCURRENCY:SPEC:24.3;
   * 
   * @test_Strategy:
   */
  public void testIsTerminated() throws Fault {
    String res = request(Constants.OP_ISTERMINATED);
    checkResponse(res);
  }

  /*
   * @testName: testShutdown
   * 
   * @assertion_ids:
   * CONCURRENCY:SPEC:23;CONCURRENCY:SPEC:24;CONCURRENCY:SPEC:24.4;
   * 
   * @test_Strategy:
   */
  public void testShutdown() throws Fault {
    String res = request(Constants.OP_SHUTDOWN);
    checkResponse(res);
  }

  /*
   * @testName: testShutdownNow
   * 
   * @assertion_ids:
   * CONCURRENCY:SPEC:23;CONCURRENCY:SPEC:24;CONCURRENCY:SPEC:24.5;
   * 
   * @test_Strategy:
   */
  public void testShutdownNow() throws Fault {
    String res = request(Constants.OP_SHUTDOWNNOW);
    checkResponse(res);
  }

  private void checkResponse(String responseStr) throws Fault {
    if (Constants.SUCCESSMESSAGE.equals(responseStr)) {
    } else {
      throw new Fault("failed to get expected successful response");
    }
  }

  private String request(String operation) {
    String result = "";
    TSURL ctsurl = new TSURL();
    Properties prop = new Properties();
    URL url;
    try {
      url = ctsurl.getURL(PROTOCOL, hostname, portnum,
          APP_URL + Constants.SERVLET_TEST_URL);
      prop.put(Constants.OP_NAME, operation);
      URLConnection urlConn = TestUtil.sendPostData(prop, url);
      result = TestUtil.getResponse(urlConn);
    } catch (MalformedURLException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }

    return result;
  }

}
