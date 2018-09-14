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
package com.sun.ts.tests.jsf.api.javax_faces.application.application;

import java.io.PrintWriter;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest.Fault;
import com.sun.ts.tests.jsf.common.client.AbstractUrlClient;

public final class URLClient extends AbstractUrlClient {

  private static final String CONTEXT_ROOT = "/jsf_appl_application_web";

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
   * @testName: applicationAddComponentTest
   * @assertion_ids: JSF:JAVADOC:30; JSF:JAVADOC:19
   * @test_Strategy: Validate components can be added to the Application and
   *                 created.
   */
  public void applicationAddComponentTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "applicationAddComponentTest");
    invoke();
  }

  /**
   * @testName: applicationAddComponentNPETest
   * @assertion_ids: JSF:JAVADOC:30; JSF:JAVADOC:20
   * @test_Strategy: Validate an NPE is thrown if any arguments are null.
   */
  public void applicationAddComponentNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "applicationAddComponentNPETest");
    invoke();
  }

  /**
   * @testName: applicationCreateComponentTest
   * @assertion_ids: JSF:JAVADOC:30; JSF:JAVADOC:34
   * @test_Strategy: Validate components that have been added to the Application
   *                 can be created.
   */
  public void applicationCreateComponentTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "applicationCreateComponentTest");
    invoke();
  }

  /**
   * @testName: applicationCreateComponentFRNPETest
   * @assertion_ids: JSF:JAVADOC:30; JSF:JAVADOC:51
   * @test_Strategy: Validate that a NullPointerException is thrown if any
   *                 parameter is null.
   * 
   *                 Signature tested: Application.createComponent(null,
   *                 Resource) Application.createComponent(FacesContext, null)
   * 
   * @since 2.0
   */
  public void applicationCreateComponentFRNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "applicationCreateComponentFRNPETest");
    invoke();
  }

  /**
   * @testName: applicationCreateComponentFSSTest
   * @assertion_ids: JSF:JAVADOC:30; JSF:JAVADOC:46
   * @test_Strategy: Validate components that have been added to the Application
   *                 can be created.
   * 
   *                 Signature tested: Application.createComponent(FacesContext,
   *                 String, String)
   * 
   * @since 2.0
   */
  public void applicationCreateComponentFSSTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "applicationCreateComponentFSSTest");
    invoke();
  }

  /**
   * @testName: applicationCreateComponentFSSNullTest
   * @assertion_ids: JSF:JAVADOC:30; JSF:JAVADOC:48
   * @test_Strategy: Validate components that have been added to the Application
   *                 can be created.
   * 
   *                 Signature tested: Application.createComponent(FacesContext,
   *                 String, 'null')
   * 
   * @since 2.0
   */
  public void applicationCreateComponentFSSNullTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "applicationCreateComponentFSSTest");
    invoke();
  }

  /**
   * @testName: applicationCreateComponentFSSNPETest
   * @assertion_ids: JSF:JAVADOC:30; JSF:JAVADOC:48
   * @test_Strategy: Validate components that a NullointerException is thrown
   *                 when - ComponentType is null. - FacesContext is null.
   * 
   * 
   *                 Signature tested: Application.createComponent(FacesContext,
   *                 String, String)
   * 
   * @since 2.0
   */
  public void applicationCreateComponentFSSNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "applicationCreateComponentFSSNPETest");
    invoke();
  }

  /**
   * @testName: applicationCreateComponentFETest
   * @assertion_ids: JSF:JAVADOC:30; JSF:JAVADOC:35; JSF:JAVADOC:38;
   *                 JSF:JAVADOC:41; JSF:JAVADOC:47
   * @test_Strategy: Validate a FacesException is thrown if createComponent is
   *                 unable to create the specified component.
   */
  public void applicationCreateComponentFETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "applicationCreateComponentFETest");
    invoke();
  }

  /**
   * @testName: applicationCreateComponentNPETest
   * @assertion_ids: JSF:JAVADOC:30; JSF:JAVADOC:36
   * @test_Strategy: Validate an NPE is thrown if any arguments are null.
   */
  public void applicationCreateComponentNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "applicationCreateComponentNPETest");
    invoke();
  }

  /**
   * @testName: applicationCreateComponentBindingTest
   * @assertion_ids: JSF:JAVADOC:30; JSF:JAVADOC:37
   * @test_Strategy: Validate that a component can be returned from
   *                 createComponent when a ValueBinding is provided that will
   *                 resolve to a UIComponent. Additionally ensure that if the
   *                 ValueBinding doesn't resolve to a UIComponent, then the
   *                 String component ID will be used as a fallback to create
   *                 the component.
   */
  public void applicationCreateComponentBindingTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "applicationCreateComponentBindingTest");
    invoke();
  }

  /**
   * @testName: applicationGetDefaultValidatorInfoTest
   * @assertion_ids: JSF:JAVADOC:30; JSF:JAVADOC:77
   * @test_Strategy: Validate that call to getDefaultValidatorInfo returns a non
   *                 null object.
   */
  public void applicationGetDefaultValidatorInfoTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "applicationGetDefaultValidatorInfoTest");
    invoke();
  }

  /**
   * @testName: applicationAddCreateConverterByStringTest
   * @assertion_ids: JSF:JAVADOC:30; JSF:JAVADOC:21; JSF:JAVADOC:53
   * @test_Strategy: Validate converters can be added to the Application and
   *                 then subsequently created.
   */
  public void applicationAddCreateConverterByStringTest() throws Fault {
    TEST_PROPS.setProperty(APITEST,
        "applicationAddCreateConverterByStringTest");
    invoke();
  }

  /**
   * @testName: applicationAddCreateConverterByStringNPETest
   * @assertion_ids: JSF:JAVADOC:30; JSF:JAVADOC:22
   * @test_Strategy: Validate that the following signatures cause a
   *                 NullPointerException to be thrown.
   * 
   *                 Application.addConverter(null, String)
   *                 Application.addConverter(String, null)
   */
  public void applicationAddCreateConverterByStringNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST,
        "applicationAddCreateConverterByStringNPETest");
    invoke();
  }

  /**
   * @testName: applicationAddCreateConverterByClassTest
   * @assertion_ids: JSF:JAVADOC:30; JSF:JAVADOC:23; JSF:JAVADOC:56
   * @test_Strategy: Validate converters can be added by class to the
   *                 Application and then subsequently created. Ensure that
   *                 creation does inheritance checking.
   */
  public void applicationAddCreateConverterByClassTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "applicationAddCreateConverterByClassTest");
    invoke();
  }

  /**
   * @testName: applicationAddCreateConverterByClassNPETest
   * @assertion_ids: JSF:JAVADOC:30; JSF:JAVADOC:24
   * @test_Strategy: Validate that the following signatures cause a
   *                 NullPointerException to be thrown.
   * 
   *                 Application.addConverter(Class, null)
   *                 Application.addConverter(null, String)
   */
  public void applicationAddCreateConverterByClassNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST,
        "applicationAddCreateConverterByClassNPETest");
    invoke();
  }

  /**
   * @testName: applicationCreateConverterByStringNPETest
   * @assertion_ids: JSF:JAVADOC:30; JSF:JAVADOC:55
   * @test_Strategy: Validate an NPE is thrown if any arguments are null.
   */
  public void applicationCreateConverterByStringNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST,
        "applicationCreateConverterByStringNPETest");
    invoke();
  }

  /**
   * @testName: applicationCreateConverterByClassNPETest
   * @assertion_ids: JSF:JAVADOC:30; JSF:JAVADOC:58
   * @test_Strategy: Validate an NPE is thrown if any arguments are null.
   */
  public void applicationCreateConverterByClassNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "applicationCreateConverterByClassNPETest");
    invoke();
  }

  /**
   * @testName: applicationCreateConverterFETest
   * @assertion_ids: JSF:JAVADOC:30; JSF:JAVADOC:54; JSF:JAVADOC:57
   * @test_Strategy: Validate an IllegalArgumentException is thrown by
   *                 createConverter if the Class argument is an Interface.
   */
  public void applicationCreateConverterFETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "applicationCreateConverterFETest");
    invoke();
  }

  /**
   * @testName: applicationGetConverterIdsTest
   * @assertion_ids: JSF:JAVADOC:30; JSF:JAVADOC:73
   * @test_Strategy: Validate the Iterator returned contains the expected
   *                 values.
   */
  public void applicationGetConverterIdsTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "applicationGetConverterIdsTest");
    invoke();
  }

  /**
   * @testName: applicationGetConverterTypesTest
   * @assertion_ids: JSF:JAVADOC:30; JSF:JAVADOC:74
   * @test_Strategy: Validate the Iterator returned contains the expected
   *                 values.
   */
  public void applicationGetConverterTypesTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "applicationGetConverterTypesTest");
    invoke();
  }

  /**
   * @testName: applicationAddCreateValidatorTest
   * @assertion_ids: JSF:JAVADOC:30; JSF:JAVADOC:28; JSF:JAVADOC:62
   * @test_Strategy: Validate we can add Validators to the Application and
   *                 subsequently create the specified Validator.
   */
  public void applicationAddCreateValidatorTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "applicationAddCreateValidatorTest");
    invoke();
  }

  /**
   * @testName: applicationAddValidatorNPETest
   * @assertion_ids: JSF:JAVADOC:30; JSF:JAVADOC:29
   * @test_Strategy: Validate an NPE is thrown if any arguments are null.
   */
  public void applicationAddValidatorNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "applicationAddValidatorNPETest");
    invoke();
  }

  /**
   * @testName: applicationCreateValidatorNPETest
   * @assertion_ids: JSF:JAVADOC:30; JSF:JAVADOC:64
   * @test_Strategy: Validate a NullPointerException is thrown if any arguments
   *                 are null.
   */
  public void applicationCreateValidatorNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "applicationCreateValidatorNPETest");
    invoke();
  }

  /**
   * @testName: applicationCreateValidatorFETest
   * @assertion_ids: JSF:JAVADOC:30; JSF:JAVADOC:63
   * @test_Strategy: Validate a FacesException if a Validator of the specified
   *                 id cannot be created.
   */
  public void applicationCreateValidatorFETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "applicationCreateValidatorFETest");
    invoke();
  }

  /**
   * @testName: applicationGetValidatorIdsTest
   * @assertion_ids: JSF:JAVADOC:30; JSF:JAVADOC:91
   * @test_Strategy: Validate the expected validator IDs can be found in the
   *                 returned Iterator.
   */
  public void applicationGetValidatorIdsTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "applicationGetValidatorIdsTest");
    invoke();
  }

  /**
   * @testName: applicationGetActionListenerTest
   * @assertion_ids: JSF:JAVADOC:30; JSF:JAVADOC:70
   * @test_Strategy: Validate the default ActionListener is returned.
   */
  public void applicationGetActionListenerTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "applicationGetActionListenerTest");
    invoke();
  }

  /**
   * @testName: applicationSetActionListenerTest
   * @assertion_ids: JSF:JAVADOC:30; JSF:JAVADOC:99
   * @test_Strategy: Validate an ActionListener can be set in the Application
   *                 and that Application.getActionListener() returns the same
   *                 ActionListener we set.
   */
  public void applicationSetActionListenerTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "applicationSetActionListenerTest");
    invoke();
  }

  /**
   * @testName: applicationSetActionListenerNPETest
   * @assertion_ids: JSF:JAVADOC:30; JSF:JAVADOC:100
   * @test_Strategy: Validate an NPE is thrown if any arguments are null.
   */
  public void applicationSetActionListenerNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "applicationSetActionListenerNPETest");
    invoke();
  }

  /**
   * @testName: applicationGetSetNavigationHandlerTest
   * @assertion_ids: JSF:JAVADOC:30; JSF:JAVADOC:82; JSF:JAVADOC:106
   * @test_Strategy: Ensure a NavigationHandler is returned if not previously
   *                 set. Ensure the same NavigationHandler that is provided to
   *                 Application.setNavigationHandler(NavigationHandler) is
   *                 returned by Application.getNavigationHandler().
   */
  public void applicationGetSetNavigationHandlerTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "applicationGetSetNavigationHandlerTest");
    invoke();
  }

  /**
   * @testName: applicationSetNavigationHandlerNPETest
   * @assertion_ids: JSF:JAVADOC:30; JSF:JAVADOC:107
   * @test_Strategy: Validate an NPE is thrown if any arguments are null.
   */
  public void applicationSetNavigationHandlerNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "applicationSetNavigationHandlerNPETest");
    invoke();
  }

  /**
   * @testName: applicationCreateValueBindingTest
   * @assertion_ids: JSF:JAVADOC:30; JSF:JAVADOC:65
   * @test_Strategy: Validate a ValueBinding is returned when provided a valid
   *                 value reference expression.
   */
  public void applicationCreateValueBindingTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "applicationCreateValueBindingTest");
    invoke();
  }

  /**
   * @testName: applicationCreateValueBindingRSETest
   * @assertion_ids: JSF:JAVADOC:30; JSF:JAVADOC:66
   * @test_Strategy: Validate a RefereceSyntaxException is thrown if
   *                 Application.getValueBinding(String) is provided a
   *                 syntactically invalid reference expression.
   */
  public void applicationCreateValueBindingRSETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "applicationCreateValueBindingRSETest");
    invoke();
  }

  /**
   * @testName: applicationCreateValueBindingNPETest
   * @assertion_ids: JSF:JAVADOC:30; JSF:JAVADOC:67
   * @test_Strategy: Validate an NPE is thrown if any arguments are null.
   */
  public void applicationCreateValueBindingNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "applicationCreateValueBindingNPETest");
    invoke();
  }

  /**
   * @testName: applicationGetSetViewHandlerTest
   * @assertion_ids: JSF:JAVADOC:30; JSF:JAVADOC:93; JSF:JAVADOC:122
   * @test_Strategy: Ensure a that the default implementation provide that
   *                 performs the functions described in the StateManager
   *                 description in the JavaServer Faces Specification.
   */
  public void applicationGetSetViewHandlerTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "applicationGetSetViewHandlerTest");
    invoke();
  }

  /**
   * @testName: applicationGetSetStateManagerTest
   * @assertion_ids: JSF:JAVADOC:30; JSF:JAVADOC:89; JSF:JAVADOC:114
   * @test_Strategy: Ensure that: - A default StateManager is provided that
   *                 performs the functions described in the StateManager
   *                 description in the JavaServer Faces Specification. - Set
   *                 the StateManager instance that will be utilized during the
   *                 Restore View and Render Response phases of the request
   *                 processing lifecycle.
   */
  public void applicationGetSetStateManagerTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "applicationGetSetStateManagerTest");
    invoke();
  }

  /**
   * @testName: applicationGetSetMessageBundleTest
   * @assertion_ids: JSF:JAVADOC:30; JSF:JAVADOC:81; JSF:JAVADOC:104
   * @test_Strategy: Ensure that the MessageBundle that is returned from the
   *                 Application.getMessageBundle() equals the MessageBundle
   *                 previous call to Application,setMessageBundle().
   */
  public void applicationGetSetMessageBundleTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "applicationGetSetMessageBundleTest");
    invoke();
  }

  /**
   * @testName: applicationSetMessageBundleNPETest
   * @assertion_ids: JSF:JAVADOC:30; JSF:JAVADOC:105
   * @test_Strategy: Ensure that a NullPointerException is thrown when null is
   *                 passed in for bundle.
   */
  public void applicationSetMessageBundleNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "applicationSetMessageBundleNPETest");
    invoke();
  }

  /**
   * @testName: applicationGetSetDefaultRenderKitIDTest
   * @assertion_ids: JSF:JAVADOC:30; JSF:JAVADOC:76; JSF:JAVADOC:103
   * @test_Strategy: Ensure that we can set and receive back the correct
   *                 DefaultRenderKit ID.
   */
  public void applicationGetSetDefaultRenderKitIDTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "applicationGetSetDefaultRenderKitIDTest");
    invoke();
  }

  /**
   * @testName: applicationGetResourceBundleTest
   * @assertion_ids: JSF:JAVADOC:30; JSF:JAVADOC:85
   * @test_Strategy: Ensure that we are able to lookup the configured
   *                 ResourceBundle.
   */
  public void applicationGetResourceBundleTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "applicationGetResourceBundleTest");
    invoke();
  }

  /**
   * @testName: applicationGetResourceBundleNPETest
   * @assertion_ids: JSF:JAVADOC:30; JSF:JAVADOC:87
   * @test_Strategy: Ensure we throw a NullPointerException if ctx == null ||
   *                 name == null.
   */
  public void applicationGetResourceBundleNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "applicationGetResourceBundleNPETest");
    invoke();
  }

  /**
   * @testName: applicationSetViewHandlerNPETest
   * @assertion_ids: JSF:JAVADOC:30; JSF:JAVADOC:124
   * @test_Strategy: Validate an NPE is thrown if any arguments are null.
   */
  public void applicationSetViewHandlerNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "applicationSetViewHandlerNPETest");
    invoke();
  }

  /**
   * @testName: applicationStateManagerNPETest
   * @assertion_ids: JSF:JAVADOC:30; JSF:JAVADOC:116
   * @test_Strategy: Ensure that: - A NullPointerException is thrown if manager
   *                 is null.
   */
  public void applicationStateManagerNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "applicationStateManagerNPETest");
    invoke();
  }

  /**
   * @testName: applicationCreateComponentResNPETest
   * @assertion_ids: JSF:JAVADOC:30; JSF:JAVADOC:52
   * @test_Strategy: Ensure that: - A NullPointerException is thrown if
   *                 FaceContext is null. - A NullPointerException is thrown if
   *                 Resource is null.
   */
  public void applicationCreateComponentResNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "applicationCreateComponentResNPETest");
    invoke();
  }

  /**
   * @testName: applicationCreateMethodBindingTest
   * @assertion_ids: JSF:JAVADOC:30; JSF:JAVADOC:59
   * @test_Strategy: Validate a MethodBinding instance can be obtained from the
   *                 Application object.
   */
  public void applicationCreateMethodBindingTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "applicationCreateMethodBindingTest");
    invoke();
  }

  /**
   * @testName: applicationCreateMethodBindingNPETest
   * @assertion_ids: JSF:JAVADOC:61
   * @test_Strategy: Validate an NPE is thrown if a null method reference is
   *                 passed to createMethodBinding().
   */
  public void applicationCreateMethodBindingNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "applicationCreateMethodBindingNPETest");
    invoke();
  }

  /**
   * @testName: applicationCreateMethodBindingRSETest
   * @assertion_ids: JSF:JAVADOC:60
   * @test_Strategy: Validate createMethodBinding() throws a
   *                 ReferenceSyntaxException if an invalid method reference is
   *                 provided.
   */
  public void applicationCreateMethodBindingRSETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "applicationCreateMethodBindingRSETest");
    invoke();
  }

  /**
   * @testName: applicationSetGetSupportedLocalesTest
   * @assertion_ids: JSF:JAVADOC:30; JSF:JAVADOC:90; JSF:JAVADOC:117
   * @test_Strategy: Validate {get,set}SupportedLocales() - unsure the values
   *                 set are properly returned.
   */
  public void applicationSetGetSupportedLocalesTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "applicationSetGetSupportedLocalesTest");
    invoke();
  }

  /**
   * @testName: applicationSetGetDefaultLocaleTest
   * @assertion_ids: JSF:JAVADOC:30; JSF:JAVADOC:75; JSF:JAVADOC:101
   * @test_Strategy: Validate Application.getDefaultLocale() returns null if
   *                 Locale has not been explicitly set by
   *                 Application.setDefaultLocale(). Then set the default Locale
   *                 and call getDefaultLocale() to ensure the expected value is
   *                 returned.
   */
  public void applicationSetGetDefaultLocaleTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "applicationSetGetDefaultLocaleTest");
    invoke();
  }

  /**
   * @testName: applicationSetDefaultLocaleNPETest
   * @assertion_ids: JSF:JAVADOC:102
   * @test_Strategy: Validate Application.setDefaultLocale() throws a
   *                 NullPointerException if a null argument is provided.
   */
  public void applicationSetDefaultLocaleNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "applicationSetDefaultLocaleNPETest");
    invoke();
  }

  /**
   * @testName: applicationSetSupportedLocalesNPETest
   * @assertion_ids: JSF:JAVADOC:30; JSF:JAVADOC:118
   * @test_Strategy: Validate Application.setSupportedLocales() throws a
   *                 NullPointerException if a null argument is provided.
   */
  public void applicationSetSupportedLocalesNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "applicationSetSupportedLocalesNPETest");
    invoke();
  }

  /**
   * @testName: applicationGetELContextListenersTest
   * @assertion_ids: JSF:JAVADOC:30; JSF:JAVADOC:78
   * @test_Strategy: Validate that we get a non null object returned.
   * 
   * @since 1.2
   */
  public void applicationGetELContextListenersTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "applicationGetELContextListenersTest");
    invoke();
  }

  /**
   * @testName: applicationGetELResolverTest
   * @assertion_ids: JSF:JAVADOC:30; JSF:JAVADOC:79
   * @test_Strategy: Validate a CompositeELResolver instanced is returned when
   *                 calling Application.getELResolver().
   * @since 1.2
   */
  public void applicationGetELResolverTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "applicationGetELResolverTest");
    invoke();
  }

  /**
   * @testName: applicationGetExpressionFactoryTest
   * @assertion_ids: JSF:JAVADOC:30; JSF:JAVADOC:80
   * @test_Strategy: Validate Application.getExpressionFactory() returns an
   *                 instance of ExpressionFactory.
   * @since 1.2
   */
  public void applicationGetExpressionFactoryTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "applicationGetExpressionFactoryTest");
    invoke();
  }

  /**
   * @testName: applicationEvaluationExpressionGetTest
   * @assertion_ids: JSF:JAVADOC:30; JSF:JAVADOC:68
   * @test_Strategy: Validate the convenience method, evaluationExpressionGet()
   *                 returns the expected result of a ValueExpression
   *                 evaluation.
   * @since 1.2
   */
  public void applicationEvaluationExpressionGetTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "applicationEvaluationExpressionGetTest");
    invoke();
  }

  /**
   * @testName: applicationCreateComponentExpressionTest
   * @assertion_ids: JSF:JAVADOC:30; JSF:JAVADOC:40
   * @test_Strategy: Validate that a component can be returned from
   *                 createComponent when a ValueExpression is provided that
   *                 will resolve to a UIComponent. Additionally ensure that if
   *                 the ValueExpression doesn't resolve to a UIComponent, then
   *                 the String component ID will be used as a fallback to
   *                 create the component.
   * @since 1.2
   */
  public void applicationCreateComponentExpressionTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "applicationCreateComponentExpressionTest");
    invoke();
  }

  /**
   * @testName: applicationCreateComponentExpressionFSSTest
   * @assertion_ids: JSF:JAVADOC:44
   * @test_Strategy: Validate that a component can be returned from
   *                 createComponent when a ValueExpression is provided that
   *                 will resolve to a UIComponent. Additionally ensure that if
   *                 the ValueExpression doesn't resolve to a UIComponent, then
   *                 the String component ID will be used as a fallback to
   *                 create the component.
   * 
   *                 Signature used for test:
   *                 createComponent(javax.el.ValueExpression
   *                 componentExpression, FacesContext context, String
   *                 componentType String renderer)
   * 
   * @since 2.0
   */
  public void applicationCreateComponentExpressionFSSTest() throws Fault {
    TEST_PROPS.setProperty(APITEST,
        "applicationCreateComponentExpressionFSSTest");
    invoke();
  }

  /**
   * @testName: applicationCreateComponentExpressionFSSNullTest
   * @assertion_ids: JSF:JAVADOC:43
   * @test_Strategy: Validate that a component can be returned from
   *                 createComponent when a ValueExpression is provided that
   *                 will resolve to a UIComponent. Additionally ensure that if
   *                 the ValueExpression doesn't resolve to a UIComponent, then
   *                 the String component ID will be used as a fallback to
   *                 create the component.
   * 
   *                 Signature used for test:
   *                 createComponent(javax.el.ValueExpression
   *                 componentExpression, FacesContext context, String
   *                 componentType String renderer) <-- Must receive 'null'
   * 
   * @since 2.0
   */
  public void applicationCreateComponentExpressionFSSNullTest() throws Fault {
    TEST_PROPS.setProperty(APITEST,
        "applicationCreateComponentExpressionFSSTest");
    invoke();
  }

  /**
   * @testName: applicationCreateComponentExpressionNPETest
   * @assertion_ids: JSF:JAVADOC:42
   * @test_Strategy: Validate an NPE is thrown if any arguments are null.
   * @since 1.2
   */
  public void applicationCreateComponentExpressionNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST,
        "applicationCreateComponentExpressionNPETest");
    invoke();
  }

  /**
   * @testName: applicationCreateComponentExpressionFSSNPETest
   * @assertion_ids: JSF:JAVADOC:45
   * @test_Strategy: Validate an NPE is thrown if any arguments are null.
   * 
   * @since 2.0
   */
  public void applicationCreateComponentExpressionFSSNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST,
        "applicationCreateComponentExpressionFSSNPETest");
    invoke();
  }

  /**
   * @testName: applicationAddGetRemoveELContextListenerTest
   * @assertion_ids: JSF:JAVADOC:30; JSF:JAVADOC:26;JSF:JAVADOC:98
   * @test_Strategy: Ensure Application.addELContextListener(),
   *                 Application.getELContextListeners(), and
   *                 Application.removeELContextListeners() behave as expected
   *                 when adding and removing ELContextListener instances.
   * @since 1.2
   */
  public void applicationAddGetRemoveELContextListenerTest() throws Fault {
    TEST_PROPS.setProperty(APITEST,
        "applicationAddGetRemoveELContextListenerTest");
    invoke();
  }

  /**
   * @testName: applicationGetResourceHandlerTest
   * @assertion_ids: JSF:JAVADOC:30; JSF:JAVADOC:88
   * @test_Strategy: Ensure Application.getResourceHandler() returns a default
   *                 ResourceHandler.
   * 
   * @since 2.0
   */
  public void applicationGetResourceHandlerTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "applicationGetResourceHandlerTest");
    invoke();
  }

  /**
   * @testName: applicationSetResourceHandlerTest
   * @assertion_ids: JSF:JAVADOC:30; JSF:JAVADOC:111
   * @test_Strategy: Ensure Application.setResourceHandler() can set a none
   *                 default ResourceHandler.
   * 
   * @since 2.0
   */
  public void applicationSetResourceHandlerTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "applicationSetResourceHandlerTest");
    invoke();
  }

  /**
   * @testName: applicationSetResourceHandlerNPETest
   * @assertion_ids: JSF:JAVADOC:113
   * @test_Strategy: Ensure Application.setResourceHandler() throws a
   *                 NullPointerException , if resourceHandler is null
   * 
   * @since 2.0
   */
  public void applicationSetResourceHandlerNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "applicationSetResourceHandlerNPETest");
    invoke();
  }

  /**
   * @testName: applicationGetProjectStageTest
   * @assertion_ids: JSF:JAVADOC:30; JSF:JAVADOC:83
   * @test_Strategy: Ensure Application.getProjectStage can retrieve the current
   *                 ProjectStage.
   * 
   * @since 2.0
   */
  public void applicationGetProjectStageTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "applicationGetProjectStageTest");
    invoke();
  }

  /**
   * @testName: applicationPublishEventNPETest1
   * @assertion_ids: JSF:JAVADOC:95;
   * @test_Strategy: Ensure Application.publishEvent throws a
   *                 NullPointerException if either systemEventClass or source
   *                 is null.
   * 
   *                 Test cases: Application.publishEvent(FacesContext, class,
   *                 null) Application.publishEvent(FacesContext, null, source)
   *                 Application.publishEvent(FacesContext, null, null)
   *                 Application.publishEvent(null, class, source)
   * @since 2.0
   */
  public void applicationPublishEventNPETest1() throws Fault {
    TEST_PROPS.setProperty(APITEST, "applicationPublishEventNPETest1");
    invoke();
  }

  /**
   * @testName: applicationPublishEventTest1
   * @assertion_ids: JSF:JAVADOC:30; JSF:JAVADOC:94
   * @test_Strategy: Ensure Application.publishEvent Does not throw any
   *                 Exceptions when called correctly.
   * 
   *                 Test cases: Application.publishEvent(FacesContext, class,
   *                 source)
   * @since 2.0
   */
  public void applicationPublishEventTest1() throws Fault {
    TEST_PROPS.setProperty(APITEST, "applicationPublishEventTest1");
    invoke();
  }

  /**
   * @testName: applicationPublishEventNPETest2
   * @assertion_ids: JSF:JAVADOC:97
   * @test_Strategy: Ensure Application.publishEvent throws a
   *                 NullPointerException if either systemEventClass or source
   *                 is null.
   * 
   *                 Test cases: Application.publishEvent(FacesConext, class,
   *                 ,null, null) Application.publishEvent(FacesContext, null,
   *                 null, source) Application.publishEvent(FacesContext, null,
   *                 class, null) Application.publishEvent(null, class, class,
   *                 null)
   * @since 2.0
   */
  public void applicationPublishEventNPETest2() throws Fault {
    TEST_PROPS.setProperty(APITEST, "applicationPublishEventNPETest2");
    invoke();
  }

  /**
   * @testName: applicationPublishEventTest2
   * @assertion_ids: JSF:JAVADOC:30; JSF:JAVADOC:96
   * @test_Strategy: Ensure Application.publishEvent Does not throw any
   *                 Exceptions when called correctly.
   * 
   *                 Test cases: Application.publishEvent(FacesContext, class,
   *                 class, source)
   * @since 2.0
   */
  public void applicationPublishEventTest2() throws Fault {
    TEST_PROPS.setProperty(APITEST, "applicationPublishEventTest2");
    invoke();
  }

  /**
   * @testName: applicationSubscribeToEventTest
   * @assertion_ids: JSF:JAVADOC:30; JSF:JAVADOC:125
   * @test_Strategy: Ensure Application.subscribeToEvent Does not throw any
   *                 Exceptions when called correctly.
   * 
   *                 Test cases: Application.subscribeToEvent(systemEventClass,
   *                 sourceClass, listener)
   * @since 2.0
   */
  public void applicationSubscribeToEventTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "applicationSubscribeToEventTest");
    invoke();
  }

  /**
   * @testName: applicationSubscribeToEventNullTest
   * @assertion_ids: JSF:JAVADOC:125
   * @test_Strategy: Ensure Application.subscribeToEvent Does not throw any
   *                 Exceptions when called with null as sourceClass.
   * 
   *                 Test cases: Application.subscribeToEvent(Class, null,
   *                 SystemEventListener)
   * @since 2.0
   */
  public void applicationSubscribeToEventNullTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "applicationSubscribeToEventNullTest");
    invoke();
  }

  /**
   * @testName: applicationSubscribeToEventNoSrcClassTest
   * @assertion_ids: JSF:JAVADOC:30; JSF:JAVADOC:127
   * @test_Strategy: Ensure Application.subscribeToEvent Does not throw any
   *                 Exceptions.
   * 
   *                 Test cases: Application.subscribeToEvent(Class,
   *                 SystemEventListener)
   * @since 2.0
   */
  public void applicationSubscribeToEventNoSrcClassTest() throws Fault {
    TEST_PROPS.setProperty(APITEST,
        "applicationSubscribeToEventNoSrcClassTest");
    invoke();
  }

  /**
   * @testName: applicationSubscribeToEventNoSrcClassNPETest
   * @assertion_ids: JSF:JAVADOC:128
   * @test_Strategy: Ensure Ensure Application.subscribeToEvent throws a
   *                 NullPointerException in any of the below test cases.
   * 
   *                 Test cases: Application.subscribeToEvent(null, null)
   *                 Application.subscribeToEvent(null, SystemEventListener)
   *                 Application.subscribeToEvent(Class, null)
   * @since 2.0
   */
  public void applicationSubscribeToEventNoSrcClassNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST,
        "applicationSubscribeToEventNoSrcClassNPETest");
    invoke();
  }

  /**
   * @testName: applicationSubscribeToEventNPETest
   * @assertion_ids: JSF:JAVADOC:126
   * @test_Strategy: Ensure Application.subscribeToEvent throws a
   *                 NullPointerException in any of the below test cases.
   * 
   *                 Test cases: Application.subscribeToEvent(null, null,
   *                 SystemEventListener) Application.subscribeToEvent(null,
   *                 null, null) Application.subscribeToEvent(null, Class,
   *                 SystemEventListener) Application.subscribeToEvent(Class,
   *                 null, null) Application.subscribeToEvent(Class, Class,
   *                 null)
   * @since 2.0
   */
  public void applicationSubscribeToEventNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "applicationSubscribeToEventNPETest");
    invoke();
  }

  /**
   * @testName: applicationUnsubscribeFromEventTest
   * @assertion_ids: JSF:JAVADOC:30; JSF:JAVADOC:129
   * @test_Strategy: Ensure Application.unSubscribeFromEvent Does not throw any
   *                 Exceptions when called.
   * 
   *                 Test cases: Application.unsubscribeFromEvent(Class, Class
   *                 SystemEventListener)
   * @since 2.0
   */
  public void applicationUnsubscribeFromEventTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "applicationUnsubscribeFromEventTest");
    invoke();
  }

  /**
   * @testName: applicationUnsubscribeFromEventSLTest
   * @assertion_ids: JSF:JAVADOC:30; JSF:JAVADOC:131
   * @test_Strategy: Ensure Application.unSubscribeFromEvent Does not throw any
   *                 Exceptions when called.
   * 
   *                 Test cases: Application.unsubscribeFromEvent(Class,
   *                 SystemEventListener)
   * @since 2.0
   */
  public void applicationUnsubscribeFromEventSLTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "applicationUnsubscribeFromEventSLTest");
    invoke();
  }

  /**
   * @testName: applicationUnsubscribeFromEventNPETest
   * @assertion_ids: JSF:JAVADOC:130; JSF:JAVADOC:132
   * @test_Strategy: Ensure Application.unsubscribeFromEvent throws a
   *                 NullPointerException in any of the below test cases.
   * 
   *                 Test cases: unsubscribeFromEvent(null, SystemEventListener)
   *                 unsubscribeFromEvent(Class, null)
   * 
   *                 unsubscribeFromEvent(null, Class, SystemEventListener)
   *                 unsubscribeFromEvent(Class, Class, null)
   * @since 2.0
   */
  public void applicationUnsubscribeFromEventNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "applicationUnsubscribeFromEventNPETest");
    invoke();
  }

  /**
   * @testName: applicationUnsubscribeFromEventNoSrcClassTest
   * @assertion_ids: JSF:JAVADOC:132
   * @test_Strategy: Ensure Application.UnsubscribeToEvent throws a
   *                 NullPointerException in any of the below test cases.
   * 
   *                 Test cases: Application.unsubscribeToEvent(Class,
   *                 SystemEventListener)
   * @since 2.0
   */
  public void applicationUnsubscribeFromEventNoSrcClassTest() throws Fault {
    TEST_PROPS.setProperty(APITEST,
        "applicationUnsubscribeFromEventNoSrcClassTest");
    invoke();
  }

  /**
   * @testName: applicationAddBehaviorTest
   * @assertion_ids: JSF:JAVADOC:17; JSF:JAVADOC:71
   * @test_Strategy: Ensure Application.AddBehavior does not throw an exception
   *                 and the correct Behavior is returned when checked with
   *                 Application.getBehaviorIds();
   * 
   *                 Test cases: Application.addBehavior(String, String)
   *                 Application.getBehaviorIds()
   * 
   * @since 2.0
   */
  public void applicationAddBehaviorTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "applicationAddBehaviorTest");
    invoke();
  }

  /**
   * @testName: applicationAddBehaviorNPETest
   * @assertion_ids: JSF:JAVADOC:18
   * @test_Strategy: Ensure Application.AddBehavior throws a
   *                 NullPointerException in any of the below test cases.
   * 
   *                 Test cases: Application.addBehavior(null, String)
   *                 Application.addBehavior(String, null)
   * 
   * @since 2.0
   */
  public void applicationAddBehaviorNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "applicationAddBehaviorNPETest");
    invoke();
  }

  /**
   * @testName: applicationCreateBehaviorTest
   * @assertion_ids: JSF:JAVADOC:31
   * @test_Strategy: Ensure Application.CreateBehavior does not throw an
   *                 exception and the correct Behavior is returned when checked
   *                 with Application.getBehaviorIds();
   * 
   *                 Test cases: Application.createBehavior(String)
   * 
   * @since 2.0
   */
  public void applicationCreateBehaviorTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "applicationCreateBehaviorTest");
    invoke();
  }

  /**
   * @testName: applicationCreateBehaviorNPETest
   * @assertion_ids: JSF:JAVADOC:33
   * @test_Strategy: Ensure Application.AddBehavior throws a
   *                 NullPointerException in the below test case.
   * 
   *                 Test cases: Application.createBehavior(null)
   * 
   * @since 2.0
   */
  public void applicationCreateBehaviorNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "applicationCreateBehaviorNPETest");
    invoke();
  }

  /**
   * @testName: applicationCreateBehaviorFETest
   * @assertion_ids: JSF:JAVADOC:32
   * @test_Strategy: If the Behavior cannot be created throw a FacesException.
   * 
   * @since 2.0
   */
  public void applicationCreateBehaviorFETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "applicationCreateBehaviorFETest");
    invoke();
  }

  /**
   * @testName: applicationAddDefaultValidatorIdTest
   * @assertion_ids: JSF:JAVADOC:25
   * @test_Strategy: Verify that when we set a default ValidatorId that we get
   *                 the same Id back.
   * 
   * @since 2.0
   */
  public void applicationAddDefaultValidatorIdTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "applicationAddDefaultValidatorIdTest");
    invoke();
  }

  /**
   * @testName: applicationAddELResolverTest
   * @assertion_ids: JSF:JAVADOC:30; JSF:JAVADOC:27
   * @test_Strategy: Verify we can add an ELResolver to the application.
   * 
   * @since 1.2
   */
  public void applicationAddELResolverTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "applicationAddELResolverTest");
    invoke();
  }

  /**
   * @testName: applicationGetSearchExpressionHandlerTest
   * @assertion_ids: PENDING
   * @test_Strategy:
   * 
   * @since 2.3
   */
  public void applicationGetSearchExpressionHandlerTest() throws Fault {
    TEST_PROPS.setProperty(APITEST,
        "applicationGetSearchExpressionHandlerTest");
    invoke();
  }

  /**
   * @testName: applicationSetSearchExpressionHandlerTest
   * @assertion_ids: PENDING
   * @test_Strategy:
   * 
   * @since 2.3
   */
  public void applicationSetSearchExpressionHandlerTest() throws Fault {
    TEST_PROPS.setProperty(APITEST,
        "applicationSetSearchExpressionHandlerTest");
    invoke();
  }

} // end of URLClient
