/*
 * Copyright (c) 2009, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jsf.api.javax_faces.el.valuebinding;

import com.sun.javatest.Status;
import com.sun.ts.tests.jsf.common.client.AbstractUrlClient;

import java.io.PrintWriter;

public final class URLClient extends AbstractUrlClient {

  private static final String CONTEXT_ROOT = "/jsf_el_valbinding_web";

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

  /* Test Declarations */

  /**
   * @testName: valBindingGetValueTest
   * @assertion_ids: JSF:JAVADOC:1769; JSF:JAVADOC:1757
   * @test_Strategy: Ensure the expected value is returned when calling
   *                 getValue(FacesContext).
   */
  public void valBindingGetValueTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "valBindingGetValueTest");
    invoke();
  }

  /**
   * @testName: valBindingGetValueNPETest
   * @assertion_ids: JSF:JAVADOC:1769; JSF:JAVADOC:1760
   * @test_Strategy: Ensure a NullPointerException is thrown if a null
   *                 FacesContext argument is passed to getValue(FacesContext).
   */
  public void valBindingGetValueNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "valBindingGetValueNPETest");
    invoke();
  }

  /**
   * @testName: valBindingGetValuePNFETest
   * @assertion_ids: JSF:JAVADOC:1769; JSF:JAVADOC:1759
   * @test_Strategy: Ensure a PropertyNotFoundException is thrown if the
   *                 resolved property doesn't exist, or is not readable.
   */
  public void valBindingGetValuePNFETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "valBindingGetValuePNFETest");
    invoke();
  }

  /**
   * @testName: valBindingGetTypeNPETest
   * @assertion_ids: JSF:JAVADOC:1769; JSF:JAVADOC:1756
   * @test_Strategy: Ensure a NullPointerException is thrown if a a null
   *                 FacesContext argument is passed to getType(FacesContext).
   */
  public void valBindingGetTypeNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "valBindingGetTypeNPETest");
    invoke();
  }

  /**
   * @testName: valBindingGetTypePNFETest
   * @assertion_ids: JSF:JAVADOC:1769; JSF:JAVADOC:1755
   * @test_Strategy: Ensure a PropertyNotFoundException is thrown if the propery
   *                 on the resolved object cannot be found.
   */
  public void valBindingGetTypePNFETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "valBindingGetTypePNFETest");
    invoke();
  }

  /**
   * @testName: valBindingIsReadOnlyTest
   * @assertion_ids: JSF:JAVADOC:1769; JSF:JAVADOC:1761
   * @test_Strategy: Ensure isReadOnly(FacesContext) returns the expected values
   *                 (i.e. returns true if the property only has a getter and
   *                 false if the property has both getters and setters).
   */
  public void valBindingIsReadOnlyTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "valBindingIsReadOnlyTest");
    invoke();
  }

  /**
   * @testName: valBindingIsReadOnlyNPETest
   * @assertion_ids: JSF:JAVADOC:1769; JSF:JAVADOC:1763
   * @test_Strategy: Ensure a NullPointerException is thrown by
   *                 isReadOnly(FacesContext) if a null FacesContext is passed.
   */
  public void valBindingIsReadOnlyNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "valBindingIsReadOnlyNPETest");
    invoke();
  }

  /**
   * @testName: valBindingSetValueTest
   * @assertion_ids: JSF:JAVADOC:1769; JSF:JAVADOC:1765
   * @test_Strategy: Ensure a property value can be set via
   *                 setValue(FacesContext, Object) by setting the value and
   *                 then verifying the value was set on the target object.
   */
  public void valBindingSetValueTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "valBindingSetValueTest");
    invoke();
  }

  /**
   * @testName: valBindingSetValueNPETest
   * @assertion_ids: JSF:JAVADOC:1769; JSF:JAVADOC:1768
   * @test_Strategy: Ensure a NullPointerException is thrown if a null
   *                 FacesContext object is passed to setValue(FacesContext,
   *                 Object).
   */
  public void valBindingSetValueNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "valBindingSetValueNPETest");
    invoke();
  }

  /**
   * @testName: valBindingSetValuePNFETest
   * @assertion_ids: JSF:JAVADOC:1769; JSF:JAVADOC:1767
   * @test_Strategy: Ensure a PropertyNotFoundException is thrown if the target
   *                 property doesn't exist, or is read only.
   */
  public void valBindingSetValuePNFETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "valBindingSetValuePNFETest");
    invoke();
  }

  /**
   * @testName: valBindingGetExpressionStringTest
   * @assertion_ids: JSF:JAVADOC:1769; JSF:JAVADOC:1752
   * @test_Strategy: Ensure getExpressionString() returns the expected value
   *                 delimited by the '#{' and '}' characters.
   */
  public void valBindingGetExpressionStringTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "valBindingGetExpressionStringTest");
    invoke();
  }

} // end of URLClient
