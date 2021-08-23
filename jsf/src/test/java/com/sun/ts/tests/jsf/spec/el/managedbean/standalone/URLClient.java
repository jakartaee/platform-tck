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

package com.sun.ts.tests.jsf.spec.el.managedbean.standalone;

import java.io.PrintWriter;

import com.sun.javatest.Status;
import com.sun.ts.tests.jsf.common.client.AbstractUrlClient;

public final class URLClient extends AbstractUrlClient {

  private static final String CONTEXT_ROOT = "/jsf_el_mgbean_web";

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
   * @testName: managedBeanScopeTest
   * @assertion_ids: JSF:SPEC:72; JSF:SPEC:73; JSF:SPEC:74; JSF:SPEC:83;
   *                 JSF:SPEC:85
   * @test_Strategy: Ensure beans described in the faces-config files are placed
   *                 into the specified scope.
   */
  public void managedBeanScopeTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "managedBeanScopeTest");
    invoke();
  }

  /**
   * @testName: managedBeanNoScopeTest
   * @assertion_ids: JSF:SPEC:72; JSF:SPEC:73; JSF:SPEC:74; JSF:SPEC:85
   * @test_Strategy: Ensure beans that are configured to have no scope are not
   *                 placed in the request, session, or application scopes after
   *                 being created.
   */
  public void managedBeanNoScopeTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "managedBeanNoScopeTest");
    invoke();
  }

  /**
   * @testName: managedBeanNullManagedPropertyTest
   * @assertion_ids: JSF:SPEC:77
   * @test_Strategy: Ensure that bean managed property values configured using
   *                 the <null-value/> element have the setter explicitly called
   *                 with a null value.
   */
  public void managedBeanNullManagedPropertyTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "managedBeanNullManagedPropertyTest");
    invoke();
  }

  /**
   * @testName: managedBeanTrimmedManagedPropertyValueTest
   * @assertion_ids: JSF:SPEC:78.4
   * @test_Strategy: Ensure any managed property values specified in the
   *                 faces-config are trimmed before being pushed to the bean.
   */
  public void managedBeanTrimmedManagedPropertyValueTest() throws Fault {
    TEST_PROPS.setProperty(APITEST,
        "managedBeanTrimmedManagedPropertyValueTest");
    invoke();
  }

  /**
   * @testName: managedBeanMapEntriesManagedPropertyTest
   * @assertion_ids: JSF:SPEC:78.2; JSF:SPEC:78.3; JSF:SPEC:78.4
   * @test_Strategy: Ensure the following behavior with regards to map-entries:
   *                 - <null-value/> elements result in a null value for the
   *                 specified key - the value of the <value> element is trimmed
   *                 before putting the value into the Map.
   */
  public void managedBeanMapEntriesManagedPropertyTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "managedBeanMapEntriesManagedPropertyTest");
    invoke();
  }

  /**
   * @testName: managedBeanListEntriesManagedPropertyTest
   * @assertion_ids: JSF:SPEC:78.5; JSF:SPEC:78.3; JSF:SPEC:78.4
   * @test_Strategy: Ensure the following behavior with regards to list-entries:
   *                 - <null-value/> elements result in a null value in the
   *                 List. - the value of the <value> element is trimmed before
   *                 putting the value into the List.
   */
  public void managedBeanListEntriesManagedPropertyTest() throws Fault {
    TEST_PROPS.setProperty(APITEST,
        "managedBeanListEntriesManagedPropertyTest");
    invoke();
  }

  /**
   * @testName: managedBeanMapKeyValueConversionTest
   * @assertion_ids: JSF:SPEC:79; JSF:SPEC:79.1; JSF:SPEC:79.2
   * @test_Strategy: Ensure the JSF implementation under test performs the
   *                 proper conversion of key and values based on the values
   *                 specified in the <key-class> and <value-class> elements.
   *                 This test uses standard boxed types for validation.
   */
  public void managedBeanMapKeyValueConversionTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "managedBeanMapKeyValueConversionTest");
    invoke();
  }

  /**
   * @testName: managedBeanListValueConversionTest
   * @assertion_ids: JSF:SPEC:80; JSF:SPEC:80.1; JSF:SPEC:80.2
   * @test_Strategy: Ensure the JSF implementation under test performs the
   *                 proper conversion of values based on the value specified in
   *                 the <value-class> element. This test uses standard boxed
   *                 types for validation. This test has the side effect of
   *                 ensuring managed propertys are set in the order they appear
   *                 in the config file.
   */
  public void managedBeanListValueConversionTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "managedBeanListValueConversionTest");
    invoke();
  }

  /**
   * @testName: managedBeanPropertyValueConversionTest
   * @assertion_ids: JSF:SPEC:69
   * @test_Strategy: Ensure the JSF implementation under test performs type
   *                 conversion (boxed to prim included) based on the rules of
   *                 the JSP 1.2 specification. This test has the side effect of
   *                 ensuring managed properties are set in the order they
   *                 appear in the config file.
   */
  public void managedBeanPropertyValueConversionTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "managedBeanPropertyValueConversionTest");
    invoke();
  }

  /**
   * @testName: managedBeanNoClassExistsTest
   * @assertion_ids: JSF:SPEC:67
   * @test_Strategy: Ensure an error is raised if an attempt is made to
   *                 instantiate a managed bean and the class associated with
   *                 this bean does not exist.
   */
  public void managedBeanNoClassExistsTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "managedBeanNoClassExistsTest");
    invoke();
  }

  /**
   * @testName: managedBeanNoZeroArgCtorTest
   * @assertion_ids: JSF:SPEC:67
   * @test_Strategy: Ensure an error is rasied if the managed bean being
   *                 instantiated does not have a zero-arg constructor.
   */
  public void managedBeanNoZeroArgCtorTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "managedBeanNoZeroArgCtorTest");
    invoke();
  }

  /**
   * @testName: managedBeanTypeConversionErrorTest
   * @assertion_ids: JSF:SPEC:69
   * @test_Strategy: Ensure an error is raised if a type conversion fails for
   *                 some managed property.
   */
  public void managedBeanTypeConversionErrorTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "managedBeanTypeConversionErrorTest");
    invoke();
  }

  /**
   * @testName: managedBeanNoSetterTest
   * @assertion_ids: JSF:SPEC:68
   * @test_Strategy: Ensure an error is raised if there is no 'setter' method
   *                 for a specified managed property.
   */
  public void managedBeanNoSetterTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "managedBeanNoSetterTest");
    invoke();
  }

  /**
   * @testName: managedBeanPrivateSetterTest
   * @assertion_ids: JSF:SPEC:68
   * @test_Strategy: Ensure an error is raised if the managed property's setter
   *                 method is not public.
   */
  public void managedBeanPrivateSetterTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "managedBeanPrivateSetterTest");
    invoke();
  }

  /**
   * @testName: managedBeanCyclicReferenceTest
   * @assertion_ids: JSF:SPEC:75
   * @test_Strategy: Ensure an error is raised of cyclic bean references exist
   *                 when attempting to instantiate a managed bean.
   */
  public void managedBeanCyclicReferenceTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "managedBeanCyclicReferenceTest");
    invoke();
  }

  /**
   * @testName: managedBeanScopedReferencesTest
   * @assertion_ids: JSF:SPEC:71
   * @test_Strategy: Ensure an error is raised when attempting to instantiate a
   *                 managed bean who's particular scope is potentially longer
   *                 than that of a referenced object. This test also validates
   *                 no error is raised for valid scope combinations.
   */
  public void managedBeanScopedReferencesTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "managedBeanScopedReferencesTest");
    invoke();
  }

  /**
   * @testName: managedBeanCreateStoreListTypeTest
   * @assertion_ids: JSF:SPEC:80;JSF:SPEC:80.1
   * @test_Strategy: Ensure the managed bean facility creates an ArrayList when
   *                 the getter for the List managed property in question
   *                 returns null.
   */
  public void managedBeanCreateStoreListTypeTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "managedBeanCreateStoreListTypeTest");
    invoke();
  }

  /**
   * @testName: managedBeanCreateStoreMapTypeTest
   * @assertion_ids: JSF:SPEC:79; JSF:SPEC:79.1
   * @test_Strategy: Ensure the managed bean facitlity creates a HashMap when
   *                 the getter for the Map managed property in question returns
   *                 null.
   */
  public void managedBeanCreateStoreMapTypeTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "managedBeanCreateStoreMapTypeTest");
    invoke();
  }

  /**
   * @testName: managedBeanPropertyMapEntryOrderTest
   * @assertion_ids: JSF:SPEC:79; JSF:SPEC:79.1
   * @test_Strategy: Ensure that the map-entry key/values are added to the Map
   *                 in the order they appear in the config file.
   */
  public void managedBeanPropertyMapEntryOrderTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "managedBeanPropertyMapEntryOrderTest");
    invoke();
  }

  /**
   * @testName: managedBeanPropertyListEntryOrderTest
   * @assertion_ids: JSF:SPEC:80; JSF:SPEC:80.2
   * @test_Strategy: Ensure the list-entries values are added to the List in the
   *                 order they appear in the config file.
   */
  public void managedBeanPropertyListEntryOrderTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "managedBeanPropertyListEntryOrderTest");
    invoke();
  }

  /**
   * @testName: managedBeanPropertyArrayTest
   * @assertion_ids: JSF:SPEC:80; JSF:SPEC:80.4
   * @test_Strategy: Ensure the managed bean facility can handle the use case of
   *                 a managed list propery being an array in the underlying
   *                 model. This test makes sure that the facility doesn't throw
   *                 an error when the getter returns an array, and that when
   *                 the values or added to the List created by the facility, it
   *                 properly converts the list (which incldues the original
   *                 value returned) back to an array of the expected type and
   *                 calls the setter appropriately.
   */
  public void managedBeanPropertyArrayTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "managedBeanPropertyArrayTest");
    invoke();
  }

  /**
   * @testName: managedBeanMapBeanTest
   * @assertion_ids: JSF:SPEC:79; JSF:SPEC:79.4
   * @test_Strategy: Ensure the managed bean facility can set map-entries
   *                 key/value pairs if the bean itself is a Map.
   */
  public void managedBeanMapBeanTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "managedBeanMapBeanTest");
    invoke();
  }

  /**
   * @testName: managedBeanListBeanTest
   * @assertion_ids: JSF:SPEC:80; JSF:SPEC:80.5
   * @test_Strategy: Ensure the managed bean facility can set list-entries if
   *                 the bean itself is a List.
   */
  public void managedBeanListBeanTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "managedBeanListBeanTest");
    invoke();
  }

} // end of URLClient
