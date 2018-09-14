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

package com.sun.ts.tests.jsf.api.javax_faces.convert.enumconverter;

import java.io.PrintWriter;

import com.sun.javatest.Status;
import com.sun.ts.tests.jsf.common.client.AbstractUrlClient;

public final class URLClient extends AbstractUrlClient {

  private static final String CONTEXT_ROOT = "/jsf_convert_enum_web";

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
   * @testName: enumConverterIsSetTransientTest
   * @assertion_ids: JSF:JAVADOC:1621; JSF:JAVADOC:1630; JSF:JAVADOC:1634
   * @test_Strategy: Ensure the default value of isTransient() is indeed false.
   *                 Then call setTransient() and validate isTransient() returns
   *                 true.
   */
  public void enumConverterIsSetTransientTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "enumConverterIsSetTransientTest");
    invoke();
  }

  /**
   * @testName: enumConverterGetAsObjectTest
   * @assertion_ids: JSF:JAVADOC:1621; JSF:JAVADOC:1623
   * @test_Strategy: Ensure that the EnumConverter successfully converts a
   *                 string value into the proper enum object and converts a
   *                 null value or a String of zero length to null.
   */
  public void enumConverterGetAsObjectTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "enumConverterGetAsObjectTest");
    invoke();
  }

  /**
   * @testName: enumConverterGetAsObjectCETest
   * @assertion_ids: JSF:JAVADOC:1621; JSF:JAVADOC:1624
   * @test_Strategy: Ensure that the EnumConverter throws a ConverterException
   *                 when attempting to convert an invalid string value or when
   *                 the target class is null.
   */
  public void enumConverterGetAsObjectCETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "enumConverterGetAsObjectCETest");
    invoke();
  }

  /**
   * @testName: enumConverterGetAsObjectNPE1Test
   * @assertion_ids: JSF:JAVADOC:1621; JSF:JAVADOC:1625
   * @test_Strategy: Ensure that the EnumConverter throws a NullPointerException
   *                 when attempting to convert with a null FacesContext.
   */
  public void enumConverterGetAsObjectNPE1Test() throws Fault {
    TEST_PROPS.setProperty(APITEST, "enumConverterGetAsObjectNPE1Test");
    invoke();
  }

  /**
   * @testName: enumConverterGetAsObjectNPE2Test
   * @assertion_ids: JSF:JAVADOC:1621; JSF:JAVADOC:1625
   * @test_Strategy: Ensure that the EnumConverter throws a NullPointerException
   *                 when attempting to convert with a null Component.
   */
  public void enumConverterGetAsObjectNPE2Test() throws Fault {
    TEST_PROPS.setProperty(APITEST, "enumConverterGetAsObjectNPE2Test");
    invoke();
  }

  /**
   * @testName: enumConverterGetAsStringTest
   * @assertion_ids: JSF:JAVADOC:1621; JSF:JAVADOC:1626
   * @test_Strategy: Ensure that the EnumConverter successfully converts an enum
   *                 object into the proper String value, and that a null enum
   *                 object converts to a String of zero length.
   */
  public void enumConverterGetAsStringTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "enumConverterGetAsStringTest");
    invoke();
  }

  /**
   * @testName: enumConverterGetAsStringCETest
   * @assertion_ids: JSF:JAVADOC:1621; JSF:JAVADOC:1627
   * @test_Strategy: Ensure that the EnumConverter throws a ConverterException
   *                 when attempting to convert an object that is not a member
   *                 of an enum class to a String, when attempting to convert a
   *                 object of a different enum class to a String, or when
   *                 attempting to convert an object where the target class is
   *                 null.
   */
  public void enumConverterGetAsStringCETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "enumConverterGetAsStringCETest");
    invoke();
  }

  /**
   * @testName: enumConverterGetAsStringNPE1Test
   * @assertion_ids: JSF:JAVADOC:1621; JSF:JAVADOC:1628
   * @test_Strategy: Ensure that the EnumConverter throws a NullPointerException
   *                 when attempting to convert an enum object to a String with
   *                 a null FacesContext.
   */
  public void enumConverterGetAsStringNPE1Test() throws Fault {
    TEST_PROPS.setProperty(APITEST, "enumConverterGetAsStringNPE1Test");
    invoke();
  }

  /**
   * @testName: enumConverterGetAsStringNPE2Test
   * @assertion_ids: JSF:JAVADOC:1621; JSF:JAVADOC:1628
   * @test_Strategy: Ensure that the EnumConverter throws a NullPointerException
   *                 when attempting to convert an enum object to a String with
   *                 a null Component.
   */
  public void enumConverterGetAsStringNPE2Test() throws Fault {
    TEST_PROPS.setProperty(APITEST, "enumConverterGetAsStringNPE2Test");
    invoke();
  }
}
