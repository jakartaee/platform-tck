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

package com.sun.ts.tests.ejb30.tx.common.web;

import java.io.PrintWriter;

import com.sun.ts.tests.servlet.common.client.AbstractUrlClient;
import static com.sun.ts.tests.servlet.common.util.Data.FAILED;
import static com.sun.ts.tests.servlet.common.util.Data.PASSED;
import com.sun.javatest.Status;
import static com.sun.ts.tests.ejb30.tx.common.web.Constants.*;

abstract public class ClientBase extends AbstractUrlClient {

  /*
   * testName: servletRemoteCmt
   * 
   * @assertion_ids: JavaEE:SPEC:54; JavaEE:SPEC:54.1
   * 
   * @test_Strategy:client -> TestServlet -> TxServlet -> Remote bean TxServlet
   * begins tx, invokes remote bean, whic setRollbackOnly. The tx status must
   * not be active in TestServlet at the end of this request processing
   */

  public void servletRemoteCmt() throws Fault {
    TEST_PROPS.setProperty(APITEST, servletRemoteCmt);
    invoke();
  }

  /*
   * testName: servletLocalCmt
   * 
   * @assertion_ids: JavaEE:SPEC:54; JavaEE:SPEC:54.1
   * 
   * @test_Strategy:client -> TestServlet -> TxServlet -> local bean TxServlet
   * begins tx, invokes local bean, whic setRollbackOnly. The tx status must not
   * be active in TestServlet at the end of this request processing
   */

  public void servletLocalCmt() throws Fault {
    TEST_PROPS.setProperty(APITEST, servletLocalCmt);
    invoke();
  }

  /*
   * testName: servletRemoteCmtRequiresNew
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

  public void servletRemoteCmtRequiresNew() throws Fault {
    TEST_PROPS.setProperty(APITEST, servletRemoteCmtRequiresNew);
    invoke();
  }

  /*
   * testName: servletLocalCmtRequiresNew
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

  public void servletLocalCmtRequiresNew() throws Fault {
    TEST_PROPS.setProperty(APITEST, servletLocalCmtRequiresNew);
    invoke();
  }

  /*
   * testName: servletRemoteCmtMandatory
   * 
   * @assertion_ids: JavaEE:SPEC:54; JavaEE:SPEC:54.1
   * 
   * @test_Strategy: client -> TestServlet -> TxServlet -> Remote bean TxServlet
   * begins UserTransaction, and invokes the remote bean, which setRollbackOnly.
   * At the end of the http request, the tx must not be active in TestServlet.
   */

  public void servletRemoteCmtMandatory() throws Fault {
    TEST_PROPS.setProperty(APITEST, servletRemoteCmtMandatory);
    invoke();
  }

  /*
   * testName: servletLocalCmtMandatory
   * 
   * @assertion_ids: JavaEE:SPEC:54; JavaEE:SPEC:54.1
   * 
   * @test_Strategy:client -> TestServlet -> TxServlet -> local bean TxServlet
   * begins UserTransaction, and invokes the local bean, which setRollbackOnly.
   * At the end of the http request, the tx must not be active in TestServlet.
   */

  public void servletLocalCmtMandatory() throws Fault {
    TEST_PROPS.setProperty(APITEST, servletLocalCmtMandatory);
    invoke();
  }

  /*
   * testName: servletRemoteCmtNever
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

  public void servletRemoteCmtNever() throws Fault {
    TEST_PROPS.setProperty(APITEST, servletRemoteCmtNever);
    TEST_PROPS.setProperty(SEARCH_STRING, PASSED);
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, FAILED);
    invoke();
  }

  /*
   * testName: servletLocalCmtNever
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
  public void servletLocalCmtNever() throws Fault {
    TEST_PROPS.setProperty(APITEST, servletLocalCmtNever);
    TEST_PROPS.setProperty(SEARCH_STRING, PASSED);
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, FAILED);
    invoke();
  }

  /*
   * testName: servletTxTerminate
   * 
   * @assertion_ids: JavaEE:SPEC:54; JavaEE:SPEC:54.1
   * 
   * @test_Strategy: repeat the request to make sure the transaction in previous
   * request has been properly terminated by the container. If you see server
   * errors about nested transaction not supported, most likely the container
   * has not properly cleared previous transaction.
   */

  public void servletTxTerminate() throws Fault {
    for (int i = 0; i < LOOP_COUNT; i++) {
      TEST_PROPS.setProperty(APITEST, servletTxTerminate);
      TEST_PROPS.setProperty(SEARCH_STRING, PASSED);
      TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, FAILED);
      System.out.println("request number " + i);
      invoke();
    }
  }

  /*
   * testName: interServletTxPropagation
   * 
   * @assertion_ids: JavaEE:SPEC:53
   * 
   * @test_Strategy:
   */

  public void interServletTxPropagation() throws Fault {
    TEST_PROPS.setProperty(APITEST, interServletTxPropagation);
    TEST_PROPS.setProperty(SEARCH_STRING, PASSED);
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, FAILED);
    invoke();
  }

  /*
   * testName: interServletTxPropagation2
   * 
   * @assertion_ids: JavaEE:SPEC:53
   * 
   * @test_Strategy: start UserTransaction in TestServlet, dispatch to
   * TxServlet, check the status should be active in TxServlet. TxServlet
   * rollbacks UserTransaction, and make sure the status in TestServlet is not
   * active.
   */

  public void interServletTxPropagation2() throws Fault {
    TEST_PROPS.setProperty(APITEST, interServletTxPropagation2);
    TEST_PROPS.setProperty(SEARCH_STRING, PASSED);
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, FAILED);
    invoke();
  }

  /*
   * testName: newThreadNoTx
   * 
   * @assertion_ids: JavaEE:SPEC:50
   * 
   * @test_Strategy: start UserTransaction in TestServlet, spawn a new thread,
   * and check this new thread is not associated with any jta transaction.
   */

  public void newThreadNoTx() throws Fault {
    TEST_PROPS.setProperty(APITEST, newThreadNoTx);
    TEST_PROPS.setProperty(SEARCH_STRING, PASSED);
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, FAILED);
    invoke();
  }
}
