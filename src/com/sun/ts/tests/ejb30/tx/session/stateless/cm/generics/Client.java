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

package com.sun.ts.tests.ejb30.tx.session.stateless.cm.generics;

import java.io.PrintWriter;
import java.util.List;

import com.sun.javatest.Status;
import com.sun.ts.tests.servlet.common.client.AbstractUrlClient;

public class Client extends AbstractUrlClient {
  public static final String CONTEXT_ROOT = "/tx_stateless_generics_web";

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
   * @testName: genericsTxMandatoryRemote
   * 
   * @test_Strategy: httpclient -> servlet -> RemoteGreetingBean
   */
  public void genericsTxMandatoryRemote() throws Fault {
    TEST_PROPS.setProperty(APITEST, "genericsTxMandatoryRemote");
    invoke();
  }

  /*
   * @testName: genericsTxMandatoryLocal
   * 
   * @test_Strategy: httpclient -> servlet -> RemoteGreetingBean
   */
  public void genericsTxMandatoryLocal() throws Fault {
    TEST_PROPS.setProperty(APITEST, "genericsTxMandatoryLocal");
    invoke();
  }

  /*
   * @testName: genericLocalBusinessInterfaceTxMandatory
   * 
   * @test_Strategy: httpclient -> servlet -> Local DateGreetingBean.
   * DateGreetingBean has a local business interface
   * GenericGreetingIF<java.util.Date>
   */
  public void genericLocalBusinessInterfaceTxMandatory() throws Fault {
    TEST_PROPS.setProperty(APITEST, "genericLocalBusinessInterfaceTxMandatory");
    invoke();
  }

  /*
   * @testName: parameterizedParamLocalTxMandatory
   * 
   * @test_Strategy: httpclient -> servlet -> Local GreetingBean. verify method
   * like parameterizedParam(List<String> ls) can be invoked with specified
   * transaction attribute (Mandatory).
   */
  public void parameterizedParamLocalTxMandatory() throws Fault {
    TEST_PROPS.setProperty(APITEST, "parameterizedParamLocalTxMandatory");
    invoke();
  }

  /*
   * @testName: parameterizedReturnLocalTxMandatory
   * 
   * @test_Strategy: httpclient -> servlet -> Local GreetingBean. verify method
   * like List<String> parameterizedReturn(int i) can be invoked with specified
   * transaction attribute (Mandatory).
   */
  public void parameterizedReturnLocalTxMandatory() throws Fault {
    TEST_PROPS.setProperty(APITEST, "parameterizedReturnLocalTxMandatory");
    invoke();
  }

  /*
   * @testName: parameterizedParamRemoteTxMandatory
   * 
   * @test_Strategy: httpclient -> servlet -> Remote GreetingBean. verify method
   * like parameterizedParam(List<String> ls) can be invoked with specified
   * transaction attribute (Mandatory).
   */
  public void parameterizedParamRemoteTxMandatory() throws Fault {
    TEST_PROPS.setProperty(APITEST, "parameterizedParamRemoteTxMandatory");
    invoke();
  }

  /*
   * @testName: parameterizedReturnRemoteTxMandatory
   * 
   * @test_Strategy: httpclient -> servlet -> Remote GreetingBean. verify method
   * like List<String> parameterizedReturn(int i) can be invoked with specified
   * transaction attribute (Mandatory).
   */
  public void parameterizedReturnRemoteTxMandatory() throws Fault {
    TEST_PROPS.setProperty(APITEST, "parameterizedReturnRemoteTxMandatory");
    invoke();
  }

  /*
   * @testName: rolesAllowedLocalDateGreeting
   * 
   * @test_Strategy: Verify @RolesAllowed in methods with generic params and
   * return types are processed properly.
   */
  public void rolesAllowedLocalDateGreeting() throws Fault {
    TEST_PROPS.setProperty(APITEST, "rolesAllowedLocalDateGreeting");
    invoke();
  }

  /*
   * @testName: rolesAllowedRemoteIntGreet
   * 
   * @test_Strategy: Verify @RolesAllowed in methods with generic params and
   * return types are processed properly.
   */
  public void rolesAllowedRemoteIntGreet() throws Fault {
    TEST_PROPS.setProperty(APITEST, "rolesAllowedRemoteIntGreet");
    invoke();
  }

  /*
   * @testName: rolesAllowedLocalIntGreet
   * 
   * @test_Strategy: Verify @RolesAllowed in methods with generic params and
   * return types are processed properly.
   */
  public void rolesAllowedLocalIntGreet() throws Fault {
    TEST_PROPS.setProperty(APITEST, "rolesAllowedLocalIntGreet");
    invoke();
  }

  /*
   * @testName: rolesAllowedNoArgLocalDateGreeting
   * 
   * @test_Strategy: Verify @RolesAllowed in methods with generic return types
   * are processed properly.
   */
  public void rolesAllowedNoArgLocalDateGreeting() throws Fault {
    TEST_PROPS.setProperty(APITEST, "rolesAllowedNoArgLocalDateGreeting");
    invoke();
  }

  /*
   * @testName: rolesAllowedNoArgRemoteIntGreet
   * 
   * @test_Strategy: Verify @RolesAllowed in methods with generic return types
   * are processed properly.
   */
  public void rolesAllowedNoArgRemoteIntGreet() throws Fault {
    TEST_PROPS.setProperty(APITEST, "rolesAllowedNoArgRemoteIntGreet");
    invoke();
  }

  /*
   * @testName: rolesAllowedNoArgLocalIntGreet
   * 
   * @test_Strategy: Verify @RolesAllowed in methods with generic return types
   * are processed properly.
   */
  public void rolesAllowedNoArgLocalIntGreet() throws Fault {
    TEST_PROPS.setProperty(APITEST, "rolesAllowedNoArgLocalIntGreet");
    invoke();
  }

}
