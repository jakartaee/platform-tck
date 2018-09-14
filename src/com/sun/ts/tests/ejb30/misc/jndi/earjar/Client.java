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

package com.sun.ts.tests.ejb30.misc.jndi.earjar;

import static com.sun.ts.tests.ejb30.common.helper.Helper.assertEquals;

import java.util.Properties;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.tests.ejb30.common.helloejbjar.HelloRemoteIF;
import com.sun.ts.tests.ejb30.common.helper.Helper;
import com.sun.ts.tests.ejb30.common.helper.ServiceLocator;
import com.sun.ts.tests.ejb30.lite.basic.common.GlobalJNDITest;

/**
 * This client tests java:global, java:app, java:module namespaces for an
 * application packaged as EAR that includes appclient jar and ejb jar.
 * 
 * All 3 namespaces can be used for look up by any ejb for other co-located ejb.
 * 
 * Appclient can use java:global and java:app to look up the remote view of
 * TestBean. Other ejbs are all local views or no-interface views and thus no
 * accessible to appclient.
 * 
 * ejb30/lite/basic contains similar tests that use WAR packaging with all local
 * beans.
 * 
 */
public class Client extends EETest {

  public static final String APPCLIENT_MODULE_NAME = "misc_jndi_earjar_client";

  public static final String TEST_BEAN_NAME = "TestBean";

  public static final String HELLO_EJB_MODULE_NAME = "ejb3_common_helloejbjar_standalone_component_ejb";

  public static final String HELLO_BEAN_NAME = "HelloBean";

  @EJB(lookup = "java:global/misc_jndi_earjar/misc_jndi_earjar_ejb/TestBean")
  private static TestIF testBean;

  @Resource(lookup = "java:module/ModuleName")
  private static String moduleNameInjected;

  @Resource(lookup = "java:app/AppName")
  private static String appNameInjected;

  @Resource
  private static Validator validator;

  @Resource
  private static ValidatorFactory validatorFactory;

  protected Properties props;

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  public void setup(String[] args, Properties p) {
    props = p;
  }

  public void cleanup() {
  }

  private String lookupTestBeanAndAdd(String lookupName) {
    Helper.getLogger().info("About to look up " + lookupName);
    TestIF b = (TestIF) ServiceLocator.lookupNoTry(lookupName);
    int x = 1;
    int y = 2;
    int expected = x + y;
    return Helper.assertEquals(null, expected, b.add(x, y));
  }

  /*
   * @testName: beanValidator
   * 
   * @test_Strategy:
   */
  public void beanValidator() {
    Helper.getLogger().info("Injected Validator: " + validator.toString());
    Helper.getLogger()
        .info("Injected ValidatorFactory: " + validatorFactory.toString());

    ValidatorFactory vf = (ValidatorFactory) ServiceLocator
        .lookupNoTry("java:comp/ValidatorFactory");
    Validator v = (Validator) ServiceLocator.lookupNoTry("java:comp/Validator");

    Helper.getLogger().info("Looked up Validator: " + v.toString());
    Helper.getLogger().info("Looked up ValidatorFactory: " + vf.toString());
  }

  /*
   * @testName: appNameModuleName
   * 
   * @test_Strategy:
   */
  public void appNameModuleName() {
    StringBuilder sb = new StringBuilder();
    String lookup = "java:module/ModuleName";
    String expected = APPCLIENT_MODULE_NAME;
    String actual = (String) ServiceLocator.lookupNoTry(lookup);
    assertEquals("Check " + lookup, expected, actual, sb);
    assertEquals("Check injected value ", expected, moduleNameInjected, sb);

    lookup = "java:app/AppName";
    expected = TestIF.APP_NAME;
    actual = (String) ServiceLocator.lookupNoTry(lookup);
    assertEquals("Check " + lookup, expected, actual, sb);
    assertEquals("Check injected value ", expected, appNameInjected, sb);

    Helper.getLogger().info(sb.toString());
  }

  /*
   * @testName: appNameModuleNameFromEJB
   * 
   * @test_Strategy:
   */
  public void appNameModuleNameFromEJB() {
    Helper.getLogger().info(testBean.appNameModuleName());
  }

  /*
   * @testName: globalJNDIHelloEJB
   * 
   * @test_Strategy: lookup portable global jndi names of HelloBean from
   * application client. helloejb is deployed as a standalone ejb module. Its
   * jndi name must have FQN of the remote interface since HelloBean exposes
   * both a remote and a local business interface, though the local intf won't
   * be accessible.
   */
  public void globalJNDIHelloEJB() {
    String lookupName = GlobalJNDITest.getGlobalJNDIName(null,
        HELLO_EJB_MODULE_NAME, HELLO_BEAN_NAME, HelloRemoteIF.class);
    HelloRemoteIF h = (HelloRemoteIF) ServiceLocator.lookupNoTry(lookupName);
    Helper.getLogger().info(h.getMessage().toString());
  }

  /*
   * @testName: ejbRefHello
   * 
   * @test_Strategy: declare a ejb-ref in application-client.xml, and look up it
   * java:comp/env namespace.
   */
  public void ejbRefHello() {
    String lookupName = "java:comp/env/ejb/hello";
    HelloRemoteIF h = (HelloRemoteIF) ServiceLocator.lookupNoTry(lookupName);
    Helper.getLogger().info(h.getMessage().toString());
  }

  /*
   * @testName: globalJNDIHelloEJB2
   * 
   * @test_Strategy: lookup portable global jndi names of HelloBean from
   * TestBean. helloejb is deployed as a standalone ejb module.
   */
  public void globalJNDIHelloEJB2() {
    Helper.getLogger().info(testBean.globalJNDIHelloEJB(null,
        HELLO_EJB_MODULE_NAME, HELLO_BEAN_NAME, HelloRemoteIF.class));
  }

  /*
   * @testName: globalJNDI
   * 
   * @test_Strategy: lookup portable global jndi names of various beans from
   * application client.
   */
  public void globalJNDI() {
    String lookupName = GlobalJNDITest.getGlobalJNDIName(TestIF.APP_NAME,
        TestIF.EJB_MODULE_NAME, TEST_BEAN_NAME);
    Helper.getLogger().info(lookupTestBeanAndAdd(lookupName));

    lookupName = GlobalJNDITest.getGlobalJNDIName(TestIF.APP_NAME,
        TestIF.EJB_MODULE_NAME, TEST_BEAN_NAME, TestIF.class);
    Helper.getLogger().info(lookupTestBeanAndAdd(lookupName));
  }

  /*
   * @testName: appJNDI
   * 
   * @test_Strategy: lookup portable app jndi names of various beans from
   * application client.
   */
  public void appJNDI() {
    String lookupName = GlobalJNDITest.getAppJNDIName(TestIF.EJB_MODULE_NAME,
        TEST_BEAN_NAME);
    Helper.getLogger().info(lookupTestBeanAndAdd(lookupName));

    lookupName = GlobalJNDITest.getAppJNDIName(TestIF.EJB_MODULE_NAME,
        TEST_BEAN_NAME, TestIF.class);
    Helper.getLogger().info(lookupTestBeanAndAdd(lookupName));
  }

  /*
   * @testName: globalJNDI2
   * 
   * @test_Strategy: lookup portable global jndi names of various beans from ejb
   * bean class
   */
  public void globalJNDI2() {
    Helper.getLogger()
        .info((testBean.globalJNDI(TestIF.APP_NAME, TestIF.EJB_MODULE_NAME)));
  }

  /*
   * @testName: appJNDI2
   * 
   * @test_Strategy: lookup portable app jndi names of various beans from ejb
   * bean class
   */
  public void appJNDI2() {
    Helper.getLogger().info((testBean.appJNDI(TestIF.EJB_MODULE_NAME)));
  }

  /*
   * @testName: moduleJNDI2
   * 
   * @test_Strategy: lookup portable module jndi names of various beans from ejb
   * bean class
   */
  public void moduleJNDI2() {
    Helper.getLogger().info((testBean.moduleJNDI()));
  }

}
