/*
 * Copyright (c) 2012, 2018 Oracle and/or its affiliates. All rights reserved.
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
 * $Id:$
 */
package com.sun.ts.tests.servlet.pluggability.api.javax_servlet.singlethreadmodel;

import java.io.PrintWriter;
import com.sun.javatest.Status;
import com.sun.ts.tests.servlet.common.client.AbstractUrlClient;

import java.util.Properties;

public class URLClient extends AbstractUrlClient {

  private static final String CTHREADS = "ServletClientThreads";

  private static int NTHREADS = 0;

  /**
   * Entry point for different-VM execution. It should delegate to method
   * run(String[], PrintWriter, PrintWriter), and this method should not contain
   * any test configuration.
   */
  public static void main(String[] args) {
    URLClient theTests = new URLClient();
    Status s = theTests.run(args, new PrintWriter(System.out),
        new PrintWriter(System.err));
    s.exit();
  }

  /**
   * Entry point for same-VM execution. In different-VM execution, the main
   * method delegates to this method.
   */
  public Status run(String args[], PrintWriter out, PrintWriter err) {

    return super.run(args, out, err);
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   * ServletClientThreads;
   */

  /* Run test */
  /**
   * <code>setup</code> is by the test harness to initialize the tests.
   *
   * @param args
   *          a <code>String[]</code> value
   * @param p
   *          a <code>Properties</code> value
   * @exception Fault
   *              if an error occurs
   */
  public void setup(String[] args, Properties p) throws Fault {
    boolean pass = true;
    StringBuffer sb = new StringBuffer(100);

    try {

      try {
        NTHREADS = new Integer(p.getProperty(CTHREADS)).intValue();
      } catch (Throwable t) {
        sb.append("\nUnable to set the number of client threads!\nReason: ");
        sb.append(t.toString());
        pass = false;
      }
    } catch (Throwable t) {
      throw new Fault("Unexpected Exception - Setup failed:" + t.toString());
    }
    super.setup(args, p);
  }

  /*
   * @testName: singleModelTest
   *
   * @assertion_ids: Servlet:SPEC:200; Servlet:SPEC:10;
   *
   * @test_Strategy: This test will use a multi-threaded client to validate that
   * SingleThreadModel servlets are handled properly. To do this, the following
   * jte property needs to be set: ServletClientThreads. This configures the
   * number of threads that the client will use.
   *
   * If a servlet implements this interface, you are guaranteed that no two
   * threads will execute concurrently in the servlet's service method.
   *
   * If the container implementation is one that doesn't pool SingleThreadModel
   * servlets, then the value can be left at the default of 2. However, if the
   * container does pool SingleThreadModel servlets, the ServletClientThreads
   * property should be set to twice the size of the instance pool. For example,
   * if the container's pool size for SingleThreadModel servlets is 10, then set
   * the ServletClientThreads property to 20.
   *
   * Also take note, that each thread will make 3 requests to the test servlet.
   * The outcome of this test for both pooled and non-pooled implementations
   * will be the same, however by configuring the threads, we can validate
   * implementations which pool these type of servlets.
   */
  public void singleModelTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /servlet_plu_singlethreadmodel_web/STMClient?count=" + NTHREADS
            + " HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, OK);
    invoke();
  }
}
