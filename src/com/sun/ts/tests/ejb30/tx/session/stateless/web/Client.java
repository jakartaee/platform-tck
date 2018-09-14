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

package com.sun.ts.tests.ejb30.tx.session.stateless.web;

import java.io.PrintWriter;
import com.sun.javatest.Status;
import static com.sun.ts.tests.ejb30.tx.common.web.Constants.*;

public class Client extends com.sun.ts.tests.ejb30.tx.common.web.ClientBase {
  public static final String CONTEXT_ROOT = "/tx_stateless_web_web";

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
   * @testName: servletRemoteCmt
   * 
   * @assertion_ids: JavaEE:SPEC:54; JavaEE:SPEC:54.1
   * 
   * @test_Strategy:client -> TestServlet -> TxServlet -> Remote bean TxServlet
   * begins tx, invokes remote bean, whic setRollbackOnly. The tx status must
   * not be active in TestServlet at the end of this request processing
   */

  /*
   * @testName: servletLocalCmt
   * 
   * @assertion_ids: JavaEE:SPEC:54; JavaEE:SPEC:54.1
   * 
   * @test_Strategy:client -> TestServlet -> TxServlet -> local bean TxServlet
   * begins tx, invokes local bean, whic setRollbackOnly. The tx status must not
   * be active in TestServlet at the end of this request processing
   */

  /*
   * @testName: servletRemoteCmtRequiresNew
   * 
   * @assertion_ids: JavaEE:SPEC:54; JavaEE:SPEC:54.1; JavaEE:SPEC:39;
   * JavaEE:SPEC:40
   * 
   * @test_Strategy: client -> TestServlet -> TxServlet -> Remote bean
   * TestServlet begins UserTransaction, calls
   * RequestDispatcher.include(TxServlet). TxServlet invokes the remote bean,
   * which requires new tx and setRollbackOnly. TestServlet verifies tx status
   * is active after the include call.
   *
   * This test verifies the tx propagation between servlets, and between servlet
   * and EJB. Since the EJB has tx attribute RequiresNew, the tx started in
   * servlet is not propagated to EJB.
   */

  /*
   * @testName: servletLocalCmtRequiresNew
   * 
   * @assertion_ids: JavaEE:SPEC:54; JavaEE:SPEC:54.1; JavaEE:SPEC:39;
   * JavaEE:SPEC:40
   * 
   * @test_Strategy: client -> TestServlet -> TxServlet -> local bean
   * TestServlet begins UserTransaction, calls
   * RequestDispatcher.include(TxServlet). TxServlet invokes the local bean,
   * which requires new tx and setRollbackOnly. TestServlet verifies tx status
   * is active after the include call.
   *
   * This test verifies the tx propagation between servlets, and between servlet
   * and EJB. Since the EJB has tx attribute RequiresNew, the tx started in
   * servlet is not propagated to EJB.
   */

  /*
   * @testName: servletRemoteCmtMandatory
   * 
   * @assertion_ids: JavaEE:SPEC:54; JavaEE:SPEC:54.1
   * 
   * @test_Strategy: client -> TestServlet -> TxServlet -> Remote bean TxServlet
   * begins UserTransaction, and invokes the remote bean, which setRollbackOnly.
   * At the end of the http request, the tx must not be active in TestServlet.
   */

  /*
   * @testName: servletLocalCmtMandatory
   * 
   * @assertion_ids: JavaEE:SPEC:54; JavaEE:SPEC:54.1
   * 
   * @test_Strategy:client -> TestServlet -> TxServlet -> local bean TxServlet
   * begins UserTransaction, and invokes the local bean, which setRollbackOnly.
   * At the end of the http request, the tx must not be active in TestServlet.
   */

  /*
   * @testName: servletRemoteCmtNever
   * 
   * @assertion_ids: JavaEE:SPEC:54; JavaEE:SPEC:54.1; JavaEE:SPEC:39;
   * JavaEE:SPEC:40
   * 
   * @test_Strategy: client -> TestServlet -> TxServlet -> Remote bean
   * TestServlet begins UserTransaction, calls
   * RequestDispatcher.include(TxServlet). TxServlet invokes the remote bean,
   * which requires tx attribute Never and setRollbackOnly.
   *
   * TestServlet verifies tx status is active after the include call.
   *
   * This test verifies the tx propagation between servlets, and between servlet
   * and EJB. Since the EJB has tx attribute Never, the tx started in servlet is
   * not propagated to EJB.
   */

  /*
   * @testName: servletLocalCmtNever
   * 
   * @assertion_ids: JavaEE:SPEC:54; JavaEE:SPEC:54.1; JavaEE:SPEC:39;
   * JavaEE:SPEC:40
   * 
   * @test_Strategy: client -> TestServlet -> TxServlet -> local bean
   * TestServlet begins UserTransaction, calls
   * RequestDispatcher.include(TxServlet). TxServlet invokes the local bean,
   * which requires tx attribute Never and setRollbackOnly.
   *
   * TestServlet verifies tx status is active after the include call.
   *
   * This test verifies the tx propagation between servlets, and between servlet
   * and EJB. Since the EJB has tx attribute Never, the tx started in servlet is
   * not propagated to EJB.
   */

  /*
   * @testName: servletTxTerminate
   * 
   * @assertion_ids: JavaEE:SPEC:54; JavaEE:SPEC:54.1
   * 
   * @test_Strategy:
   */

  /*
   * @testName: interServletTxPropagation
   * 
   * @assertion_ids: JavaEE:SPEC:53
   * 
   * @test_Strategy:
   */

  /*
   * @testName: interServletTxPropagation2
   * 
   * @assertion_ids: JavaEE:SPEC:53
   * 
   * @test_Strategy: start UserTransaction in TestServlet, dispatch to
   * TxServlet, check the status should be active in TxServlet. TxServlet
   * rollbacks UserTransaction, and make sure the status in TestServlet is not
   * active.
   */
}
