/*
 * Copyright (c) 2009, 2020 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.ts.tests.jsf.api.jakarta_faces.factoryfinder.webinf;

import com.sun.javatest.Status;
import com.sun.ts.tests.common.webclient.http.HttpRequest;
import com.sun.ts.tests.common.webclient.http.HttpResponse;
import com.sun.ts.tests.jsf.common.client.AbstractUrlClient;

import java.io.IOException;
import java.io.PrintWriter;

public class URLClient extends AbstractUrlClient {

  private static final String CONTEXT_ROOT = "/jsf_webinf_web";

  public static void main(String[] args) {
    URLClient theTests = new URLClient();
    Status s = theTests.run(args, new PrintWriter(System.out),
        new PrintWriter(System.err));
    s.exit();
  }

  public Status run(String args[], PrintWriter out, PrintWriter err) {
    setContextRoot(CONTEXT_ROOT);
    setServletName(DEFAULT_SERVLET_NAME);
    return super.run(args, out, err);
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   */
  /*
   * @testName: getFactoryWebinfTest
   * 
   * @assertion_ids: JSF:JAVADOC:7; JSF:JAVADOC:14
   * 
   * @test_Strategy: Validate that If the JavaServer Faces configuration file
   * bundled into the WEB-INF directory of the webapp contains a factory entry
   * of the given factory class name, that factory is used.
   * 
   */
  public void getFactoryWebinfTest() throws Fault {
    String factoryStyle = "Webinf";
    String request = GET + CONTEXT_ROOT + "/TestServlet?testname=getFactory"
        + factoryStyle + "Test " + HTTP10;

    System.out.println("REQUEST LINE: " + request);

    HttpRequest req = new HttpRequest(request, _hostname, _port);
    HttpResponse res;

    try {
      res = req.execute();
      if (!res.getResponseBodyAsString().contains("PASSED")) {
        System.out.println("RESPONSE: " + res.getResponseBodyAsString());
        throw new Fault("[BaseUrlClient] getFactory" + factoryStyle
            + "Test failed!  Check output for cause of failure.");
      } else {
        System.out.println("RESPONSE: " + res.getResponseBodyAsString());
      }

    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
  }
}
