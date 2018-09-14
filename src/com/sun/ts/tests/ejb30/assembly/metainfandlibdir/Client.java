/*
 * Copyright (c) 2007, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.ejb30.assembly.metainfandlibdir;

import java.io.PrintWriter;
import com.sun.javatest.Status;
import com.sun.ts.tests.servlet.common.client.AbstractUrlClient;
import static com.sun.ts.tests.servlet.common.util.Data.FAILED;
import static com.sun.ts.tests.servlet.common.util.Data.PASSED;

public class Client extends AbstractUrlClient {
  public static final String CONTEXT_ROOT = "/ejb3_assembly_metainfandlibdir";

  public static final String SERVLET_NAME = "TestServlet";

  /**
   * Entry point for different-VM execution. It should delegate to method
   * run(String[], PrintWriter, PrintWriter), and this method should not contain
   * any test configuration.
   */
  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, new PrintWriter(System.out),
        new PrintWriter(System.err));
    s.exit();
  }

  /**
   * Entry point for same-VM execution. In different-VM execution, the main
   * method delegates to this method.
   */
  public Status run(String args[], PrintWriter out, PrintWriter err) {
    setServletName(SERVLET_NAME);
    setContextRoot(CONTEXT_ROOT);
    return super.run(args, out, err);
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   */

  /*
   * @testName: remoteAdd
   * 
   * @test_Strategy:
   */
  public void remoteAdd() throws Fault {
    TEST_PROPS.setProperty(APITEST, "remoteAdd");
    invoke();
  }

  /*
   * @testName: remoteAddByHelloEJB
   * 
   * @assertion_ids:
   * 
   * @test_Strategy: hello ejb is packaged as a standalone ejb module and
   * deployed separately. It client view jar is packaged inside current ear and
   * referenced by both war and ejb jar thru MANIFEST.MF httpclient ->
   * TestServlet -> helloBean
   */
  public void remoteAddByHelloEJB() throws Fault {
    TEST_PROPS.setProperty(APITEST, "remoteAddByHelloEJB");
    invoke();
  }

  /*
   * @testName: remoteAddByHelloEJBFromAssemblyBean
   * 
   * @assertion_ids:
   * 
   * @test_Strategy: hello ejb is packaged as a standalone ejb module and
   * deployed separately. It client view jar is packaged inside current ear and
   * referenced by both war and ejb jar thru MANIFEST.MF httpclient ->
   * TestServlet -> assemblyBean -> helloBean
   */
  public void remoteAddByHelloEJBFromAssemblyBean() throws Fault {
    TEST_PROPS.setProperty(APITEST, "remoteAddByHelloEJBFromAssemblyBean");
    invoke();
  }

  /*
   * @testName: ejbInjectionInFilterTest
   * 
   * @assertion_ids:
   * 
   * @test_Strategy: hello ejb is packaged as a standalone ejb module and
   * deployed separately. It client view jar is packaged inside current ear and
   * referenced by both war and ejb jar thru MANIFEST.MF. The ejb-ref to
   * assemblyBean is a local ejb ref, and the ejb-ref to helloBean is remote ejb
   * ref. httpclient -> filter -> TestServlet
   * 
   * @todo See bug 6589464 (Missing optional ejb-ref-type leaves web app in
   * unusable state) Due to this bug, the filter is not initialized and put into
   * service. At request time, this filter is silently ignored.
   */
  public void ejbInjectionInFilterTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "ejbInjectionInFilterTest");
    invoke();
  }

  /*
   * @testName: libSubdirNotScanned
   * 
   * @assertion_ids:
   * 
   * @test_Strategy: EAR/lib/sub-directory/*.jar are not included in classpath
   */
  public void libSubdirNotScanned() throws Fault {
    TEST_PROPS.setProperty(APITEST, "libSubdirNotScanned");
    invoke();
  }

}
