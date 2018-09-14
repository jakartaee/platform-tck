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

package com.sun.ts.tests.jsf.api.javax_faces.el.propertyresolver;

import com.sun.javatest.Status;
import com.sun.ts.tests.jsf.common.client.AbstractUrlClient;

import java.io.PrintWriter;

public final class URLClient extends AbstractUrlClient {

  private static final String CONTEXT_ROOT = "/jsf_el_propresolver_web";

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
   * @testName: propResolverGetValueIndexTest
   * @assertion_ids: JSF:JAVADOC:1741; JSF:JAVADOC:1732
   * @test_Strategy: Ensure getValue(Object, int) returns the expected value at
   *                 the specified index.
   */
  public void propResolverGetValueIndexTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "propResolverGetValueIndexTest");
    invoke();
  }

  /**
   * @testName: propResolverGetValueIndexNullTest
   * @assertion_ids: JSF:JAVADOC:1741; JSF:JAVADOC:1732
   * @test_Strategy: Ensure null is returned if getValue(Object, int) is passed
   *                 a null Object.
   */
  public void propResolverGetValueIndexNullTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "propResolverGetValueIndexNullTest");
    invoke();
  }

  /**
   * @testName: propResolverGetValueIndexIOBNullTest
   * @assertion_ids: JSF:JAVADOC:1741; JSF:JAVADOC:1733
   * @test_Strategy: Ensure an IndexOutOfBoundsException is thrown if an invalid
   *                 index is passed to getValue(Object, int).
   */
  public void propResolverGetValueIndexIOBNullTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "propResolverGetValueIndexIOBNullTest");
    invoke();
  }

  /**
   * @testName: propResolverGetValueTest
   * @assertion_ids: JSF:JAVADOC:1741; JSF:JAVADOC:1730
   * @test_Strategy: Ensure the expected value is returned by getValue(Object,
   *                 String) when passing in a valid property name.
   */
  public void propResolverGetValueTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "propResolverGetValueTest");
    invoke();
  }

  /**
   * @testName: propResolverGetValueNullTest
   * @assertion_ids: JSF:JAVADOC:1741; JSF:JAVADOC:1730
   * @test_Strategy: Ensure null is returned by getValue(Object, String) when a
   *                 null Object or property is passed.
   */
  public void propResolverGetValueNullTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "propResolverGetValueNullTest");
    invoke();
  }

  /**
   * @testName: propResolverGetValuePNFETest
   * @assertion_ids: JSF:JAVADOC:1741; JSF:JAVADOC:1731
   * @test_Strategy: Ensure a PropertyNotFoundException is thrown if the
   *                 property passed to getValue(Object, String) doesn't exist
   *                 in the specified Object.
   */
  public void propResolverGetValuePNFETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "propResolverGetValuePNFETest");
    invoke();
  }

  /**
   * @testName: propResolverSetValueIndexTest
   * @assertion_ids: JSF:JAVADOC:1741; JSF:JAVADOC:1745
   * @test_Strategy: Ensure that indexed values on objects can be set via
   *                 setValue(Object, index, Object) by setting the value and
   *                 then calling getValue(Object, index) to ensure it was
   *                 indeed set.
   */
  public void propResolverSetValueIndexTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "propResolverSetValueIndexTest");
    invoke();
  }

  /**
   * @testName: propResolverSetValueIndexPNFETest
   * @assertion_ids: JSF:JAVADOC:1741; JSF:JAVADOC:1747
   * @test_Strategy: Ensure PropertyNotFoundException is thrown if the index
   *                 passed is out of range, or the object to set the value on
   *                 is null.
   */
  public void propResolverSetValueIndexPNFETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "propResolverSetValueIndexPNFETest");
    invoke();
  }

  /**
   * @testName: propResolverSetValueTest
   * @assertion_ids: JSF:JAVADOC:1741; JSF:JAVADOC:1742
   * @test_Strategy: Ensure properties on Objects can be set via
   *                 setValue(Object, String, Object) and can be obtained via
   *                 getValue(Object, String).
   */
  public void propResolverSetValueTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "propResolverSetValueTest");
    invoke();
  }

  /**
   * @testName: propResolverSetValuePNFETest
   * @assertion_ids: JSF:JAVADOC:1741; JSF:JAVADOC:1744
   * @test_Strategy: Ensure a PropertyNotFoundException is thrown if the target
   *                 property is not writable, doesn't exist, or the target
   *                 object or property names are null.
   */
  public void propResolverSetValuePNFETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "propResolverSetValuePNFETest");
    invoke();
  }

  /**
   * @testName: propResolverGetTypeIndexPNFETest
   * @assertion_ids: JSF:JAVADOC:1741; JSF:JAVADOC:1728
   * @test_Strategy: Ensure a PropertyNotFoundException is thrown if an invalid
   *                 index is provided or a null target is provided to
   *                 getType(Object, int).
   */
  public void propResolverGetTypeIndexPNFETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "propResolverGetTypeIndexPNFETest");
    invoke();
  }

  /**
   * @testName: propResolverGetTypePNFETest
   * @assertion_ids: JSF:JAVADOC:1741; JSF:JAVADOC:1725
   * @test_Strategy: Ensure a PropertyNotFoundException is thrown if the target
   *                 property doesn't exist or null target object is passed.
   */
  public void propResolverGetTypePNFETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "propResolverGetTypePNFETest");
    invoke();
  }

} // end of URLClient
