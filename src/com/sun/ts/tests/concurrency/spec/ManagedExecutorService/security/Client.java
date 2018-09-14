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

package com.sun.ts.tests.concurrency.spec.ManagedExecutorService.security;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.util.TestUtil;
import java.util.*;
import java.net.*;
import com.sun.ts.tests.concurrency.api.common.*;

public class Client extends EETest {
  private static final String urlString = "/SecurityTest_web/testServlet";

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

  /*
   * @class.setup_props: webServerHost; webServerPort;
   */
  public void setup(String[] args, Properties p) throws Fault {
    boolean pass = true;

    try {
      hostname = p.getProperty(WEBSERVERHOSTPROP);
      if (hostname == null)
        pass = false;
      else if (hostname.equals(""))
        pass = false;
      try {
        portnum = Integer.parseInt(p.getProperty(WEBSERVERPORTPROP));
      } catch (Exception e) {
        pass = false;
      }
    } catch (Exception e) {
      throw new Fault("setup failed:", e);
    }
    if (!pass) {
      TestUtil.logErr(
          "Please specify host & port of web server " + "in config properties: "
              + WEBSERVERHOSTPROP + ", " + WEBSERVERPORTPROP);
      throw new Fault("setup failed:");
    }
  }

  public void cleanup() throws Fault {
    TestUtil.logTrace("cleanup");
  }

  /*
   * @testName: managedExecutorServiceAPISecurityTest
   * 
   * @assertion_ids: CONCURRENCY:SPEC:4.3; CONCURRENCY:SPEC:50;
   * CONCURRENCY:SPEC:85; CONCURRENCY:SPEC:96.6; CONCURRENCY:SPEC:106;
   * CONCURRENCY:SPEC:22;
   * 
   * @test_Strategy: login in a servlet with username
   * "javajoe(in role manager)", then submit a task by ManagedExecutorService in
   * which call a ejb that requires role manager.
   */
  public void managedExecutorServiceAPISecurityTest() throws Fault {

    try {
      TSURL ctsurl = new TSURL();
      URL url = ctsurl.getURL(PROTOCOL, hostname, portnum, urlString);
      URLConnection urlConn = TestUtil.sendPostData(new Properties(), url);
      String s = TestUtil.getResponse(urlConn);
      Util.assertEquals(Util.SERVLET_RETURN_SUCCESS, s.trim());
    } catch (Exception e) {
      throw new Fault(e);
    }
  }

}
