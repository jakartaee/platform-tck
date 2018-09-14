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
package com.sun.ts.tests.jsf.api.javax_faces.application.applicationwrapper;

import java.io.PrintWriter;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest.Fault;
import com.sun.ts.tests.jsf.common.client.AbstractUrlClient;

public final class URLClient extends AbstractUrlClient {

  private static final String CONTEXT_ROOT = "/jsf_appl_applicationwrapper_web";

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
   * @testName: applicationWrapperAddComponentTest
   * 
   * @assertion_ids: JSF:JAVADOC:190; JSF:JAVADOC:146; JSF:JAVADOC:139
   * 
   * @test_Strategy: Validate that components that are add to are the same
   *                 components received will called via the wrapper.
   */
  public void applicationWrapperAddComponentTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "applicationWrapperAddComponentTest");
    invoke();
  }

  /**
   * @testName: applicationWrapperCreateComponentTest
   * 
   * @assertion_ids: JSF:JAVADOC:190; JSF:JAVADOC:146; JSF:JAVADOC:149
   * 
   * @test_Strategy: Validate components that have been added to the
   *                 applicationwrapper can be created.
   */
  public void applicationWrapperCreateComponentTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "applicationWrapperCreateComponentTest");
    invoke();
  }

  /**
   * @testName: applicationWrapperCreateComponentFSSTest
   * 
   * @assertion_ids: JSF:JAVADOC:190; JSF:JAVADOC:146; JSF:JAVADOC:156
   * 
   * @test_Strategy: Validate components that have been added to the
   *                 applicationWrapper can be created.
   * 
   *                 Signature tested:
   *                 applicationWrapper.createComponent(FacesContext, String,
   *                 String)
   * 
   * @since 2.0
   */
  public void applicationWrapperCreateComponentFSSTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "applicationWrapperCreateComponentFSSTest");
    invoke();
  }

  /**
   * @testName: applicationWrapperCreateComponentFSSNullTest
   * 
   * @assertion_ids: JSF:JAVADOC:190; JSF:JAVADOC:146; JSF:JAVADOC:156
   * 
   * @test_Strategy: Validate components that have been added to the
   *                 applicationWrapper can be created.
   * 
   *                 Signature tested:
   *                 applicationWrapper.createComponent(FacesContext, String,
   *                 'null')
   * 
   * @since 2.0
   */
  public void applicationWrapperCreateComponentFSSNullTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "applicationWrapperCreateComponentFSSTest");
    invoke();
  }

  /**
   * @testName: applicationWrapperCreateComponentBindingTest
   * 
   * @assertion_ids: JSF:JAVADOC:190; JSF:JAVADOC:146; JSF:JAVADOC:160
   * 
   * @test_Strategy: Validate that a component can be returned from
   *                 createComponent when a ValueBinding is provided that will
   *                 resolve to a UIComponent. Additionally ensure that if the
   *                 ValueBinding doesn't resolve to a UIComponent, then the
   *                 String component ID will be used as a fallback to create
   *                 the component.
   */
  public void applicationWrapperCreateComponentBindingTest() throws Fault {
    TEST_PROPS.setProperty(APITEST,
        "applicationWrapperCreateComponentBindingTest");
    invoke();
  }

  /**
   * @testName: applicationWrapperGetComponentTypesTest
   * 
   * @assertion_ids: JSF:JAVADOC:190; JSF:JAVADOC:146; JSF:JAVADOC:170
   * 
   * @test_Strategy: Validate an Iterator is returned and we can find the
   *                 expected component types within the Iterator.
   */
  public void applicationWrapperGetComponentTypesTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "applicationWrapperGetComponentTypesTest");
    invoke();
  }

  /**
   * @testName: applicationWrapperAddCreateConverterByStringTest
   * 
   * @assertion_ids: JSF:JAVADOC:190; JSF:JAVADOC:146; JSF:JAVADOC:140;
   *                 JSF:JAVADOC:158
   * 
   * @test_Strategy: Validate converters can be added to the applicationWrapper
   *                 and then subsequently created.
   */
  public void applicationWrapperAddCreateConverterByStringTest() throws Fault {
    TEST_PROPS.setProperty(APITEST,
        "applicationWrapperAddCreateConverterByStringTest");
    invoke();
  }

  /**
   * @testName: applicationWrapperAddCreateConverterByClassTest
   * @assertion_ids: JSF:JAVADOC:190; JSF:JAVADOC:146; JSF:JAVADOC:141;
   *                 JSF:JAVADOC:159
   * @test_Strategy: Validate converters can be added by class to the
   *                 applicationWrapper and then subsequently created. Ensure
   *                 that creation does inheritance checking.
   */
  public void applicationWrapperAddCreateConverterByClassTest() throws Fault {
    TEST_PROPS.setProperty(APITEST,
        "applicationWrapperAddCreateConverterByClassTest");
    invoke();
  }

  /**
   * @testName: applicationWrapperGetConverterIdsTest
   * 
   * @assertion_ids: JSF:JAVADOC:190; JSF:JAVADOC:146; JSF:JAVADOC:171
   * 
   * @test_Strategy: Validate the Iterator returned contains the expected
   *                 values.
   */
  public void applicationWrapperGetConverterIdsTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "applicationWrapperGetConverterIdsTest");
    invoke();
  }

  /**
   * @testName: applicationWrapperGetConverterTypesTest
   * @assertion_ids: JSF:JAVADOC:190; JSF:JAVADOC:146; JSF:JAVADOC:172
   * @test_Strategy: Validate the Iterator returned contains the expected
   *                 values.
   */
  public void applicationWrapperGetConverterTypesTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "applicationWrapperGetConverterTypesTest");
    invoke();
  }

  /**
   * @testName: applicationWrapperGetDefaultValidatorInfoTest
   * @assertion_ids: JSF:JAVADOC:190; JSF:JAVADOC:146; JSF:JAVADOC:175
   * @test_Strategy: Validate that call to getDefaultValidatorInfo returns a non
   *                 null object.
   */
  public void applicationWrapperGetDefaultValidatorInfoTest() throws Fault {
    TEST_PROPS.setProperty(APITEST,
        "applicationWrapperGetDefaultValidatorInfoTest");
    invoke();
  }

  /**
   * @testName: applicationWrapperAddCreateValidatorTest
   * @assertion_ids: JSF:JAVADOC:190; JSF:JAVADOC:146; JSF:JAVADOC:145;
   *                 JSF:JAVADOC:162
   * @test_Strategy: Validate we can add Validators to the applicationWrapper
   *                 and subsequently create the specified Validator.
   */
  public void applicationWrapperAddCreateValidatorTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "applicationWrapperAddCreateValidatorTest");
    invoke();
  }

  /**
   * @testName: applicationWrapperGetValidatorIdsTest
   * 
   * @assertion_ids: JSF:JAVADOC:190; JSF:JAVADOC:30; JSF:JAVADOC:91
   * 
   * @test_Strategy: Validate the expected validator IDs can be found in the
   *                 returned Iterator.
   */
  public void applicationWrapperGetValidatorIdsTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "applicationWrapperGetValidatorIdsTest");
    invoke();
  }

  /**
   * @testName: applicationWrapperGetActionListenerTest
   * 
   * @assertion_ids: JSF:JAVADOC:190; JSF:JAVADOC:146; JSF:JAVADOC:187
   * 
   * @test_Strategy: Validate the default ActionListener is returned.
   */
  public void applicationWrapperGetActionListenerTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "applicationWrapperGetActionListenerTest");
    invoke();
  }

  /**
   * @testName: applicationWrapperSetActionListenerTest
   * 
   * @assertion_ids: JSF:JAVADOC:190; JSF:JAVADOC:146; JSF:JAVADOC:168;
   *                 JSF:JAVADOC:194
   * 
   * @test_Strategy: Validate an ActionListener can be set in the
   *                 applicationWrapper and that
   *                 applicationWrapper.getActionListener() returns the same
   *                 ActionListener we set.
   */
  public void applicationWrapperSetActionListenerTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "applicationWrapperSetActionListenerTest");
    invoke();
  }

  /**
   * @testName: applicationWrapperGetSetDefaultRenderKitIDTest
   * @assertion_ids: JSF:JAVADOC:190; JSF:JAVADOC:146; JSF:JAVADOC:174;
   *                 JSF:JAVADOC:196
   * @test_Strategy: Ensure that we can set and receive back the correct
   *                 DefaultRenderKit ID.
   */
  public void applicationWrapperGetSetDefaultRenderKitIDTest() throws Fault {
    TEST_PROPS.setProperty(APITEST,
        "applicationWrapperGetSetDefaultRenderKitIDTest");
    invoke();
  }

  /**
   * @testName: applicationWrapperGetSetMessageBundleTest
   * @assertion_ids: JSF:JAVADOC:190; JSF:JAVADOC:146; JSF:JAVADOC:179;
   *                 JSF:JAVADOC:197
   * @test_Strategy: Ensure that the MessageBundle that is returned from the
   *                 ApplicationWrapper.getMessageBundle() equals the
   *                 MessageBundle previous call to
   *                 ApplicationWrapper.setMessageBundle().
   */
  public void applicationWrapperGetSetMessageBundleTest() throws Fault {
    TEST_PROPS.setProperty(APITEST,
        "applicationWrapperGetSetMessageBundleTest");
    invoke();
  }

  /**
   * @testName: applicationWrapperGetSetNavigationHandlerTest
   * 
   * @assertion_ids: JSF:JAVADOC:190; JSF:JAVADOC:146; JSF:JAVADOC:180;
   *                 JSF:JAVADOC:198
   * 
   * @test_Strategy: Ensure a NavigationHandler is returned if not previously
   *                 set. Ensure the same NavigationHandler that is provided to
   *                 applicationWrapper.setNavigationHandler(NavigationHandler )
   *                 is returned by applicationWrapper.getNavigationHandler().
   */
  public void applicationWrapperGetSetNavigationHandlerTest() throws Fault {
    TEST_PROPS.setProperty(APITEST,
        "applicationWrapperGetSetNavigationHandlerTest");
    invoke();
  }

  /**
   * @testName: applicationWrapperCreateValueBindingTest
   * 
   * @assertion_ids: JSF:JAVADOC:190; JSF:JAVADOC:146; JSF:JAVADOC:164
   * 
   * @test_Strategy: Validate a ValueBinding is returned when provided a valid
   *                 value reference expression.
   */
  public void applicationWrapperCreateValueBindingTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "applicationWrapperCreateValueBindingTest");
    invoke();
  }

  /**
   * @testName: applicationWrapperCreateValidatorFETest
   * @assertion_ids: JSF:JAVADOC:146; JSF:JAVADOC:163
   * @test_Strategy: Validate a FacesException if a Validator of the specified
   *                 id cannot be created.
   */
  public void applicationWrapperCreateValidatorFETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "applicationWrapperCreateValidatorFETest");
    invoke();
  }

  /**
   * @testName: applicationWrapperGetSetViewHandlerTest
   * @assertion_ids: JSF:JAVADOC:190; JSF:JAVADOC:146; JSF:JAVADOC:189;
   *                 JSF:JAVADOC:204
   * 
   * @test_Strategy: Ensure a ViewHandler is returned if not previously set.
   *                 Ensure the same ViewHandler that is provided to
   *                 applicationWrapper.setViewHandler(ViewHandler) is returned
   *                 by applicationWrapper.getViewHandler().
   */
  public void applicationWrapperGetSetViewHandlerTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "applicationWrapperGetSetViewHandlerTest");
    invoke();
  }

  /**
   * @testName: applicationWrapperCreateMethodBindingTest
   * 
   * @assertion_ids: JSF:JAVADOC:190; JSF:JAVADOC:146; JSF:JAVADOC:160
   * 
   * @test_Strategy: Validate a MethodBinding instance can be obtained from the
   *                 applicationWrapper object.
   */
  public void applicationWrapperCreateMethodBindingTest() throws Fault {
    TEST_PROPS.setProperty(APITEST,
        "applicationWrapperCreateMethodBindingTest");
    invoke();
  }

  /**
   * @testName: applicationWrapperSetGetSupportedLocalesTest
   * 
   * @assertion_ids: JSF:JAVADOC:190; JSF:JAVADOC:146; JSF:JAVADOC:186;
   *                 JSF:JAVADOC:202
   * 
   * @test_Strategy: Validate {get,set}SupportedLocales() - unsure the values
   *                 set are properly returned.
   */
  public void applicationWrapperSetGetSupportedLocalesTest() throws Fault {
    TEST_PROPS.setProperty(APITEST,
        "applicationWrapperSetGetSupportedLocalesTest");
    invoke();
  }

  /**
   * @testName: applicationWrapperSetGetDefaultLocaleTest
   * 
   * @assertion_ids: JSF:JAVADOC:190; JSF:JAVADOC:146; JSF:JAVADOC:173;
   *                 JSF:JAVADOC:195
   * 
   * @test_Strategy: Validate applicationWrapper.getDefaultLocale() returns null
   *                 if Locale has not been explicitly set by
   *                 applicationWrapper.setDefaultLocale(). Then set the default
   *                 Locale and call getDefaultLocale() to ensure the expected
   *                 value is returned.
   */
  public void applicationWrapperSetGetDefaultLocaleTest() throws Fault {
    TEST_PROPS.setProperty(APITEST,
        "applicationWrapperSetGetDefaultLocaleTest");
    invoke();
  }

  /**
   * @testName: applicationWrapperGetELResolverTest
   * 
   * @assertion_ids: JSF:JAVADOC:190; JSF:JAVADOC:146; JSF:JAVADOC:177
   * 
   * @test_Strategy: Validate a CompositeELResolver instanced is returned when
   *                 calling applicationWrapper.getELResolver().
   * 
   * @since 1.2
   */
  public void applicationWrapperGetELResolverTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "applicationWrapperGetELResolverTest");
    invoke();
  }

  /**
   * @testName: applicationWrapperGetExpressionFactoryTest
   * 
   * @assertion_ids: JSF:JAVADOC:190; JSF:JAVADOC:146; JSF:JAVADOC:178
   * 
   * @test_Strategy: Validate applicationWrapper.getExpressionFactory() returns
   *                 an instance of ExpressionFactory.
   * 
   * @since 1.2
   */
  public void applicationWrapperGetExpressionFactoryTest() throws Fault {
    TEST_PROPS.setProperty(APITEST,
        "applicationWrapperGetExpressionFactoryTest");
    invoke();
  }

  /**
   * @testName: applicationWrapperEvaluationExpressionGetTest
   * 
   * @assertion_ids: JSF:JAVADOC:190; JSF:JAVADOC:146; JSF:JAVADOC:166
   * 
   * @test_Strategy: Validate the convenience method, evaluationExpressionGet()
   *                 returns the expected result of a ValueExpression
   *                 evaluation.
   * 
   * @since 1.2
   */
  public void applicationWrapperEvaluationExpressionGetTest() throws Fault {
    TEST_PROPS.setProperty(APITEST,
        "applicationWrapperEvaluationExpressionGetTest");
    invoke();
  }

  /**
   * @testName: applicationWrapperCreateComponentExpressionTest
   * 
   * @assertion_ids: JSF:JAVADOC:190; JSF:JAVADOC:146; JSF:JAVADOC:153
   * 
   * @test_Strategy: Validate that a component can be returned from
   *                 createComponent when a ValueExpression is provided that
   *                 will resolve to a UIComponent. Additionally ensure that if
   *                 the ValueExpression doesn't resolve to a UIComponent, then
   *                 the String component ID will be used as a fallback to
   *                 create the component.
   * 
   * @since 1.2
   */
  public void applicationWrapperCreateComponentExpressionTest() throws Fault {
    TEST_PROPS.setProperty(APITEST,
        "applicationWrapperCreateComponentExpressionTest");
    invoke();
  }

  /**
   * @testName: applicationWrapperCreateComponentExpressionFSSTest
   * 
   * @assertion_ids: JSF:JAVADOC:190; JSF:JAVADOC:146; JSF:JAVADOC:155
   * 
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
  public void applicationWrapperCreateComponentExpressionFSSTest()
      throws Fault {
    TEST_PROPS.setProperty(APITEST,
        "applicationWrapperCreateComponentExpressionFSSTest");
    invoke();
  }

  /**
   * @testName: applicationWrapperCreateComponentExpressionFETest
   * @assertion_ids: JSF:JAVADOC:190; JSF:JAVADOC:154
   * @test_Strategy: Validate a FacesException if createComponent cannot create
   *                 the component specified by the provided ID.
   * 
   * @since 1.2
   */
  public void applicationWrapperCreateComponentExpressionFETest() throws Fault {
    TEST_PROPS.setProperty(APITEST,
        "applicationWrapperCreateComponentExpressionFETest");
    invoke();
  }

  /**
   * @testName: applicationWrapperCreateComponentExpressionFSSNullTest
   * 
   * @assertion_ids: JSF:JAVADOC:190; JSF:JAVADOC:146; JSF:JAVADOC:155
   * 
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
  public void applicationWrapperCreateComponentExpressionFSSNullTest()
      throws Fault {
    TEST_PROPS.setProperty(APITEST,
        "applicationWrapperCreateComponentExpressionFSSTest");
    invoke();
  }

  /**
   * @testName: applicationWrapperAddELResolverTest
   * @assertion_ids: JSF:JAVADOC:190; JSF:JAVADOC:144
   * @test_Strategy: Verify we can add an ELResolver to the application.
   * 
   * @since 1.2
   */
  public void applicationWrapperAddELResolverTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "applicationWrapperAddELResolverTest");
    invoke();
  }

  /**
   * @testName: applicationWrapperAddGetRemoveELContextListenerTest
   * 
   * @assertion_ids: JSF:JAVADOC:190; JSF:JAVADOC:146; JSF:JAVADOC:143;
   *                 JSF:JAVADOC:176; JSF:JAVADOC:193
   * 
   * @test_Strategy: Ensure applicationWrapper.addELContextListener(),
   *                 applicationWrapper.getELContextListeners(), and
   *                 applicationWrapper.removeELContextListeners() behave as
   *                 expected when adding and removing ELContextListener
   *                 instances.
   * 
   * @since 1.2
   */
  public void applicationWrapperAddGetRemoveELContextListenerTest()
      throws Fault {
    TEST_PROPS.setProperty(APITEST,
        "applicationWrapperAddGetRemoveELContextListenerTest");
    invoke();
  }

  /**
   * @testName: applicationWrapperGetResourceHandlerTest
   * @assertion_ids: JSF:JAVADOC:190; JSF:JAVADOC:146; JSF:JAVADOC:184
   * @test_Strategy: Ensure applicationWrapper.getResourceHandler() returns a
   *                 default ResourceHandler.
   * @since 2.0
   */
  public void applicationWrapperGetResourceHandlerTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "applicationWrapperGetResourceHandlerTest");
    invoke();
  }

  /**
   * @testName: applicationWrapperGetResourceBundleTest
   * @assertion_ids: JSF:JAVADOC:190; JSF:JAVADOC:146; JSF:JAVADOC:183
   * @test_Strategy: Ensure that we are able to lookup the configured
   *                 ResourceBundle.
   */
  public void applicationWrapperGetResourceBundleTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "applicationWrapperGetResourceBundleTest");
    invoke();
  }

  /**
   * @testName: applicationWrapperSetResourceHandlerTest
   * @assertion_ids: JSF:JAVADOC:190; JSF:JAVADOC:146; JSF:JAVADOC:200
   * @test_Strategy: Ensure applicationWrapper.setResourceHandler() can set a
   *                 none default ResourceHandler.
   * 
   * @since 2.0
   */
  public void applicationWrapperSetResourceHandlerTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "applicationWrapperSetResourceHandlerTest");
    invoke();
  }

  /**
   * @testName: applicationWrapperSetResourceHandlerNPETest
   * @assertion_ids: JSF:JAVADOC:190; JSF:JAVADOC:2680
   * @test_Strategy: Ensure ApplicationWrapper.setResourceHandler() throws a
   *                 NullPointerException , if resourceHandler is null
   * 
   * @since 2.0
   */
  public void applicationWrapperSetResourceHandlerNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST,
        "applicationWrapperSetResourceHandlerNPETest");
    invoke();
  }

  /**
   * @testName: applicationWrapperGetProjectStageTest
   * @assertion_ids: JSF:JAVADOC:190; JSF:JAVADOC:146; JSF:JAVADOC:181
   * @test_Strategy: Ensure applicationWrapper.getProjectStage can retrieve the
   *                 current ProjectStage.
   * 
   * @since 2.0
   */
  public void applicationWrapperGetProjectStageTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "applicationWrapperGetProjectStageTest");
    invoke();
  }

  /**
   * @testName: applicationWrapperPublishEventTest1
   * @assertion_ids: JSF:JAVADOC:190; JSF:JAVADOC:146; JSF:JAVADOC:191
   * @test_Strategy: Ensure applicationWrapper.publishEvent Does not throw any
   *                 Exceptions when called correctly. Test cases:
   *                 applicationWrapper.publishEvent(FacesContext, class,
   *                 source)
   * 
   * @since 2.0
   */
  public void applicationWrapperPublishEventTest1() throws Fault {
    TEST_PROPS.setProperty(APITEST, "applicationWrapperPublishEventTest1");
    invoke();
  }

  /**
   * @testName: applicationWrapperPublishEventTest2
   * 
   * @assertion_ids: JSF:JAVADOC:190; JSF:JAVADOC:146; JSF:JAVADOC:192
   * 
   * @test_Strategy: Ensure applicationWrapper.publishEvent Does not throw any
   *                 Exceptions when called correctly.
   * 
   *                 Test cases: applicationWrapper.publishEvent(FacesContext,
   *                 class, class, source)
   * 
   * @since 2.0
   */
  public void applicationWrapperPublishEventTest2() throws Fault {
    TEST_PROPS.setProperty(APITEST, "applicationWrapperPublishEventTest2");
    invoke();
  }

  /**
   * @testName: applicationWrapperSubscribeToEventTest
   * 
   * @assertion_ids: JSF:JAVADOC:190; JSF:JAVADOC:146; JSF:JAVADOC:205
   * 
   * @test_Strategy: Ensure applicationWrapper.subscribeToEvent Does not throw
   *                 any Exceptions when called correctly.
   * 
   *                 Test cases:
   *                 applicationWrapper.subscribeToEvent(systemEventClass,
   *                 sourceClass, listener)
   * 
   * @since 2.0
   */
  public void applicationWrapperSubscribeToEventTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "applicationWrapperSubscribeToEventTest");
    invoke();
  }

  /**
   * @testName: applicationWrapperSubscribeToEventNullTest
   * 
   * @assertion_ids: JSF:JAVADOC:190; JSF:JAVADOC:146; JSF:JAVADOC:205
   * 
   * @test_Strategy: Ensure applicationWrapper.subscribeToEvent Does not throw
   *                 any Exceptions when called with null as sourceClass.
   * 
   *                 Test cases: applicationWrapper.subscribeToEvent(Class,
   *                 null, SystemEventListener)
   * 
   * @since 2.0
   */
  public void applicationWrapperSubscribeToEventNullTest() throws Fault {
    TEST_PROPS.setProperty(APITEST,
        "applicationWrapperSubscribeToEventNullTest");
    invoke();
  }

  /**
   * @testName: applicationWrapperSubscribeToEventNoSrcClassTest
   * 
   * @assertion_ids: JSF:JAVADOC:190; JSF:JAVADOC:146; JSF:JAVADOC:206
   * 
   * @test_Strategy: Ensure applicationWrapper.subscribeToEvent Does not throw
   *                 any Exceptions.
   * 
   *                 Test cases: applicationWrapper.subscribeToEvent(Class,
   *                 SystemEventListener)
   * 
   * @since 2.0
   */
  public void applicationWrapperSubscribeToEventNoSrcClassTest() throws Fault {
    TEST_PROPS.setProperty(APITEST,
        "applicationWrapperSubscribeToEventNoSrcClassTest");
    invoke();
  }

  /**
   * @testName: applicationWrapperUnsubscribeFromEventTest
   * 
   * @assertion_ids: JSF:JAVADOC:190; JSF:JAVADOC:146; JSF:JAVADOC:207
   * 
   * @test_Strategy: Ensure applicationWrapper.unSubscribeFromEvent Does not
   *                 throw any Exceptions when called.
   * 
   *                 Test cases: applicationWrapper.unsubscribeFromEvent(Class,
   *                 Class SystemEventListener)
   * 
   * @since 2.0
   */
  public void applicationWrapperUnsubscribeFromEventTest() throws Fault {
    TEST_PROPS.setProperty(APITEST,
        "applicationWrapperUnsubscribeFromEventTest");
    invoke();
  }

  /**
   * @testName: applicationWrapperUnsubscribeFromEventNoSrcClassTest
   * 
   * @assertion_ids: JSF:JAVADOC:190; JSF:JAVADOC:146; JSF:JAVADOC:208
   * 
   * @test_Strategy: Ensure applicationWrapper.UnsubscribeToEvent throws a
   *                 NullPointerException in any of the below test cases.
   * 
   *                 Test cases: applicationWrapper.unsubscribeToEvent(Class,
   *                 SystemEventListener)
   * 
   * @since 2.0
   */
  public void applicationWrapperUnsubscribeFromEventNoSrcClassTest()
      throws Fault {
    TEST_PROPS.setProperty(APITEST,
        "applicationWrapperUnsubscribeFromEventNoSrcClassTest");
    invoke();
  }

  /**
   * @testName: applicationWrapperAddBehaviorTest
   * 
   * @assertion_ids: JSF:JAVADOC:190; JSF:JAVADOC:146; JSF:JAVADOC:138
   * 
   * @test_Strategy: Ensure applicationWrapper.AddBehavior does not throw an
   *                 exception and the correct Behavior is returned when checked
   *                 with applicationWrapper.getBehaviorIds();
   * 
   *                 Test cases: applicationWrapper.addBehavior(String, String)
   * 
   * @since 2.0
   */
  public void applicationWrapperAddBehaviorTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "applicationWrapperAddBehaviorTest");
    invoke();
  }

  /**
   * @testName: applicationWrapperCreateBehaviorTest
   * 
   * @assertion_ids: JSF:JAVADOC:190; JSF:JAVADOC:146; JSF:JAVADOC:147
   * 
   * @test_Strategy: Ensure applicationWrapper.CreateBehavior does not throw an
   *                 exception and the correct Behavior is returned when checked
   *                 with applicationWrapper.getBehaviorIds();
   * 
   *                 Test cases: applicationWrapper.createBehavior(String)
   * 
   * @since 2.0
   */
  public void applicationWrapperCreateBehaviorTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "applicationWrapperCreateBehaviorTest");
    invoke();
  }

  /**
   * @testName: applicationWrapperCreateBehaviorFETest
   * @assertion_ids: JSF:JAVADOC:190; JSF:JAVADOC:148
   * @test_Strategy: If the Behavior cannot be created throw a FacesException.
   * 
   * @since 2.0
   */
  public void applicationWrapperCreateBehaviorFETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "applicationWrapperCreateBehaviorFETest");
    invoke();
  }

  /**
   * @testName: applicationWrapperCreateComponentFETest
   * @assertion_ids: JSF:JAVADOC:190; JSF:JAVADOC:150
   * @test_Strategy: Validate a FacesException is thrown if createComponent is
   *                 unable to create the specified component.
   */
  public void applicationWrapperCreateComponentFETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "applicationWrapperCreateComponentFETest");
    invoke();
  }

  /**
   * @testName: applicationWrapperAddDefaultValidatorIdTest
   * @assertion_ids: JSF:JAVADOC:190; JSF:JAVADOC:146; JSF:JAVADOC:142
   * @test_Strategy: Verify that when we set a default ValidatorId that we get
   *                 the same Id back.
   * @since 2.0
   */
  public void applicationWrapperAddDefaultValidatorIdTest() throws Fault {
    TEST_PROPS.setProperty(APITEST,
        "applicationWrapperAddDefaultValidatorIdTest");
    invoke();
  }

  /**
   * @testName: applicationWrapperGetBehaviorIdsTest
   * @assertion_ids: JSF:JAVADOC:190; JJSF:JAVADOC:146; JSF:JAVADOC:169
   * @test_Strategy: Verify that we get back an Iterator of BehaviorIds.
   * @since 2.0
   */
  public void applicationWrapperGetBehaviorIdsTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "applicationWrapperGetBehaviorIdsTest");
    invoke();
  }

  /**
   * @testName: applicationWrapperGetSetStateManagerTest
   * @assertion_ids: JSF:JAVADOC:190; JSF:JAVADOC:146; JSF:JAVADOC:185;
   *                 JSF:JAVADOC:201
   * @test_Strategy: Ensure that: - A default StateManager is provided that
   *                 performs the functions described in the StateManager
   *                 description in the JavaServer Faces Specification. - Set
   *                 the StateManager instance that will be utilized during the
   *                 Restore View and Render Response phases of the request
   *                 processing lifecycle.
   */
  public void applicationWrapperGetSetStateManagerTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "applicationWrapperGetSetStateManagerTest");
    invoke();
  }

  /**
   * @testName: applicationWrapperSetStateManagerNPETest
   * @assertion_ids: JSF:JAVADOC:190; JSF:JAVADOC:2682
   * @test_Strategy: Ensure ApplicationWrapper.setStateManager() throws a
   *                 NullPointerException , if StateManager is null
   * 
   * @since 2.0
   */
  public void applicationWrapperSetStateManagerNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "applicationWrapperSetStateManagerNPETest");
    invoke();
  }

  /**
   * @testName: applicationWrapperSetViewHandlerNPETest
   * @assertion_ids: JSF:JAVADOC:190; JSF:JAVADOC:2684
   * @test_Strategy: Ensure ApplicationWrapper.setViewHandler() throws a
   *                 NullPointerException , if ViewHandler is null
   * 
   * @since 2.0
   */
  public void applicationWrapperSetViewHandlerNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "applicationWrapperSetViewHandlerNPETest");
    invoke();
  }

  /**
   * @testName: applicationWrapperCreateValueBindingRSETest
   * @assertion_ids: JSF:JAVADOC:190; JSF:JAVADOC:165
   * @test_Strategy: Validate a RefereceSyntaxException is thrown if
   *                 ApplicationWrapper.getValueBinding(String) is provided a
   *                 syntactically invalid reference expression.
   */
  public void applicationWrapperCreateValueBindingRSETest() throws Fault {
    TEST_PROPS.setProperty(APITEST,
        "applicationWrapperCreateValueBindingRSETest");
    invoke();
  }

} // end of URLClient
