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

package com.sun.ts.tests.jsf.api.javax_faces.model.selectitem;

import java.io.PrintWriter;

import com.sun.javatest.Status;
import com.sun.ts.tests.jsf.common.client.AbstractUrlClient;

public class URLClient extends AbstractUrlClient {

  private static final String CONTEXT_ROOT = "/jsf_model_selitem_web";

  public static void main(String[] args) {
    URLClient theTests = new URLClient();
    Status s = theTests.run(args, new PrintWriter(System.out, true),
        new PrintWriter(System.err, true));
    s.exit();
  }

  public Status run(String[] args, PrintWriter out, PrintWriter err) {
    if (getContextRoot() == null)
      setContextRoot(CONTEXT_ROOT);
    if (getServletName() == null)
      setServletName(DEFAULT_SERVLET_NAME);
    return super.run(args, out, err);
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   */

  /* Test Declarations */

  /**
   * @testName: selectItemCtor0Test
   * @assertion_ids: JSF:JAVADOC:2031
   * @test_Strategy: Verify a new SelectItem instance can be created providing
   *                 no arguments.
   */
  public void selectItemCtor0Test() throws Fault {
    TEST_PROPS.setProperty(APITEST, "selectItemCtor0Test");
    invoke();
  }

  /**
   * @testName: selectItemCtor1Test
   * @assertion_ids: JSF:JAVADOC:2032
   * @test_Strategy: Verify a new SelectItem instance can be created providing a
   *                 new value at construction time. Verify this value can be
   *                 obtained via getValue(). Verify the label, description,
   *                 disabled and escape properties have the default values.
   */
  public void selectItemCtor1Test() throws Fault {
    TEST_PROPS.setProperty(APITEST, "selectItemCtor1Test");
    invoke();
  }

  /**
   * @testName: selectItemCtor2Test
   * @assertion_ids: JSF:JAVADOC:2033
   * @test_Strategy: Verify a new SelectItem instance can be created providing a
   *                 new value and label at construction time. Verify these
   *                 values can be obtained via getValue() and getLabel()
   *                 respectively. Verify the description, disabled and escape
   *                 properties have the default values.
   */
  public void selectItemCtor2Test() throws Fault {
    TEST_PROPS.setProperty(APITEST, "selectItemCtor2Test");
    invoke();
  }

  /**
   * @testName: selectItemCtor3Test
   * @assertion_ids: JSF:JAVADOC:2034
   * @test_Strategy: Verify a new SelectItem instance can be created providing a
   *                 new value, label, and description at construction time.
   *                 Verify these values can be obtained via getValue(),
   *                 getLabel(), and getDescription() respectively. Verify the
   *                 disabled and escape properties have the default values.
   */
  public void selectItemCtor3Test() throws Fault {
    TEST_PROPS.setProperty(APITEST, "selectItemCtor3Test");
    invoke();
  }

  /**
   * @testName: selectItemCtor4Test
   * @assertion_ids: JSF:JAVADOC:2035
   * @test_Strategy: Verify a new SelectItem instance can be created providing a
   *                 new value, label, description, and disabled status at
   *                 construction time. Verify these values can be obtained via
   *                 getValue(), getLabel(), getDescription(), and isDisabled()
   *                 respectively. Verify the escape property has the default
   *                 value.
   */
  public void selectItemCtor4Test() throws Fault {
    TEST_PROPS.setProperty(APITEST, "selectItemCtor4Test");
    invoke();
  }

  /**
   * @testName: selectItemCtor5Test
   * @assertion_ids: JSF:JAVADOC:2036
   * @test_Strategy: Verify a new SelectItem instance can be created providing a
   *                 new value, label, description, disabled status, and escape
   *                 status at construction time. Verify these values can be
   *                 obtained via getValue(), getLabel(), getDescription(),
   *                 isDisabled() and isEscape() respectively.
   */
  public void selectItemCtor5Test() throws Fault {
    TEST_PROPS.setProperty(APITEST, "selectItemCtor5Test");
    invoke();
  }

  /**
   * @testName: selectItemGetSetValueTest
   * @assertion_ids: JSF:JAVADOC:2027; JSF:JAVADOC:2043
   * @test_Strategy: Verify the value set via setValue() is returned by
   *                 getValue().
   */
  public void selectItemGetSetValueTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "selectItemGetSetValueTest");
    invoke();
  }

  /**
   * @testName: selectItemGetSetLabelTest
   * @assertion_ids: JSF:JAVADOC:2026; JSF:JAVADOC:2041
   * @test_Strategy: Verify the value set via setLabel() is returned by
   *                 getLabel().
   */
  public void selectItemGetSetLabelTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "selectItemGetSetLabelTest");
    invoke();
  }

  /**
   * @testName: selectItemGetSetDescriptionTest
   * @assertion_ids: JSF:JAVADOC:2025; JSF:JAVADOC:2041
   * @test_Strategy: Verify the value set via setDescription() is returned by
   *                 getDescription().
   */
  public void selectItemGetSetDescriptionTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "selectItemGetSetDescriptionTest");
    invoke();
  }

  /**
   * @testName: selectItemIsSetDisabledTest
   * @assertion_ids: JSF:JAVADOC:2028; JSF:JAVADOC:2039
   * @test_Strategy: Verify the default return value for isDisabled() is false.
   *                 Verify the value set via setDisabled() is returned by
   *                 isDisabled().
   */
  public void selectItemIsSetDisabledTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "selectItemIsSetDisabledTest");
    invoke();
  }

  /**
   * @testName: selectItemIsSetEscapeTest
   * @assertion_ids: JSF:JAVADOC:2029; JSF:JAVADOC:2040
   * @test_Strategy: Verify the value set via setEscape() is returned by
   *                 isEscape().
   */
  public void selectItemIsSetEscapeTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "selectItemIsSetEscapeTest");
    invoke();
  }
} // end of URLClient
