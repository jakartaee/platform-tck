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

package com.sun.ts.tests.jsf.api.javax_faces.model.selectitemgroup;

import java.io.PrintWriter;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest;

public final class URLClient
    extends com.sun.ts.tests.jsf.api.javax_faces.model.selectitem.URLClient {

  private static final String CONTEXT_ROOT = "/jsf_model_selitemgrp_web";

  public static void main(String[] args) {
    URLClient theTests = new URLClient();
    Status s = theTests.run(args, new PrintWriter(System.out, true),
        new PrintWriter(System.err, true));
    s.exit();
  }

  public Status run(String[] args, PrintWriter out, PrintWriter err) {
    setContextRoot(CONTEXT_ROOT);
    setServletName(DEFAULT_SERVLET_NAME);
    return super.run(args, out, err);
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   */

  /* Test Declarations */

  /**
   * @testName: selectItemGroupCtor0Test
   * @assertion_ids: JSF:JAVADOC:2045
   * @test_Strategy: Verify a SelectItemGroup can be created with no args.
   */
  public void selectItemGroupCtor0Test() throws Fault {
    TEST_PROPS.setProperty(APITEST, "selectItemGroupCtor0Test");
    invoke();
  }

  /**
   * @testName: selectItemGroupCtor1Test
   * @assertion_ids: JSF:JAVADOC:2046
   * @test_Strategy: Verify a new SelectItemGroup instance can be created
   *                 providing a new label at construction time. Verify this
   *                 value can be obtained via getLabel().
   */
  public void selectItemGroupCtor1Test() throws Fault {
    TEST_PROPS.setProperty(APITEST, "selectItemGroupCtor1Test");
    invoke();
  }

  /**
   * @testName: selectItemGroupCtor4Test
   * @assertion_ids: JSF:JAVADOC:2047
   * @test_Strategy: Verify a new SelectItemGroup instance can be created
   *                 providing a new label, description, disabled qualifier, and
   *                 an array of SelectItem instances at construction time.
   *                 Verify these values can be obtained via getLabel(),
   *                 getDescription(), isDisabled(), and getSelectItems()
   *                 respectively.
   */
  public void selectItemGroupCtor4Test() throws Fault {
    TEST_PROPS.setProperty(APITEST, "selectItemGroupCtor4Test");
    invoke();
  }

  /**
   * @testName: selectItemGroupGetSetSelectItemsTest
   * @assertion_ids: JSF:JAVADOC:2044; JSF:JAVADOC:2048
   * @test_Strategy: Verify the SelectItems set via setSelectItems() is properly
   *                 returned by getSelectItems().
   */
  public void selectItemGroupGetSetSelectItemsTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "selectItemGroupGetSetSelectItemsTest");
    invoke();
  }

  /**
   * @testName: selectItemGetSetDescriptionTest
   * @assertion_ids: JSF:JAVADOC:2025; JSF:JAVADOC:2038
   * @test_Strategy: Verify the value set via setDescription() is returned by
   *                 getDescription().
   */
  public void selectItemGetSetDescriptionTest() throws EETest.Fault {
    super.selectItemGetSetDescriptionTest();
  }

  /**
   * @testName: selectItemGetSetLabelTest
   * @assertion_ids: JSF:JAVADOC:2026; JSF:JAVADOC:2041
   * @test_Strategy: Verify the value set via setLabel() is returned by
   *                 getLabel().
   */
  public void selectItemGetSetLabelTest() throws EETest.Fault {
    super.selectItemGetSetLabelTest();
  }

  /**
   * @testName: selectItemGetSetValueTest
   * @assertion_ids: JSF:JAVADOC:2027; JSF:JAVADOC:2043
   * @test_Strategy: Verify the value set via setValue() is returned by
   *                 getValue().
   */
  public void selectItemGetSetValueTest() throws EETest.Fault {
    super.selectItemGetSetValueTest();
  }

  /**
   * @testName: selectItemIsSetDisabledTest
   * @assertion_ids: JSF:JAVADOC:2028
   * @test_Strategy: Verify the default return value for isDisabled() is false.
   *                 Verify the value set via setDisabled() is returned by
   *                 isDisabled().
   */
  public void selectItemIsSetDisabledTest() throws EETest.Fault {
    super.selectItemIsSetDisabledTest();
  }

  /**
   * @testName: selectItemSetSelectItemsNPETest
   * @assertion_ids: JSF:JAVADOC:2049
   * @test_Strategy: Verify a NullPointerException is thrown if selectItems is
   *                 null
   */
  public void selectItemSetSelectItemsNPETest() throws EETest.Fault {
    TEST_PROPS.setProperty(APITEST, "selectItemSetSelectItemsNPETest");
    invoke();
  }

} // end of URLClient
