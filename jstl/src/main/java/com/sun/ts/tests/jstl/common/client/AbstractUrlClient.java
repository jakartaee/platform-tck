/*
 * Copyright (c) 2007 Oracle and/or its affiliates. All rights reserved.
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


package com.sun.ts.tests.jstl.common.client;

import static org.junit.jupiter.api.Assertions.assertFalse;

import java.io.InputStream;
import java.lang.System.Logger;
import java.net.URL;

import com.sun.ts.tests.jstl.common.tags.TestTag;
import com.sun.ts.tests.jstl.common.resources.Resources_en;

import org.jboss.arquillian.container.test.api.OperateOnDeployment;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.UrlAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;

import com.sun.ts.tests.common.webclient.BaseUrlClient;
import com.sun.ts.tests.common.webclient.WebTestCase;
import com.sun.ts.tests.common.webclient.http.HttpRequest;

public class AbstractUrlClient extends BaseUrlClient {

  private static final Logger logger = System.getLogger(AbstractUrlClient.class.getName());

  @BeforeEach
  void logStartTest(TestInfo testInfo) {
    logger.log(Logger.Level.INFO, "STARTING TEST : "+testInfo.getDisplayName());
  }

  @AfterEach
  void logFinishTest(TestInfo testInfo) {
    logger.log(Logger.Level.INFO, "FINISHED TEST : "+testInfo.getDisplayName());
  }

  @ArquillianResource
  @OperateOnDeployment("_DEFAULT_")
  public URL url;


  protected InputStream goldenFileStream = null;

  public InputStream getGoldenFileStream() {
    return goldenFileStream;
  }

  public void setGoldenFileStream(InputStream gfStream) {
    goldenFileStream = gfStream;
  }

  protected static final String STANDARD_COMPAT = "standardCompat";


  private void setGoldenFileStreamProperty(WebTestCase testCase, InputStream gfStream) {
    testCase.setGoldenFileStream(gfStream);
  }

  protected boolean isNullOrEmpty(String val) {
    if (val == null || val.equals("")) {
      return true;
    }
    return false;
  }

  protected static JavaArchive getCommonJarArchive() {
    String packagePathTags = TestTag.class.getPackageName().replace(".", "/");
    String packagePathResources = Resources_en.class.getPackageName().replace(".", "/");

    JavaArchive jstlTCKCommonJar = ShrinkWrap.create(JavaArchive.class, "jstltck-common.jar");
    jstlTCKCommonJar.addAsResource(new UrlAsset(TestTag.class.getClassLoader().getResource(packagePathTags+"/tlds/jstltck-util.tld")), "META-INF/jstltck-util.tld");
    jstlTCKCommonJar.addAsResource(new UrlAsset(TestTag.class.getClassLoader().getResource(packagePathTags+"/tlds/permitted.tld")), "META-INF/permitted.tld");
    jstlTCKCommonJar.addAsResource(new UrlAsset(TestTag.class.getClassLoader().getResource(packagePathTags+"/tlds/scrfree_nodecl.tld")), "META-INF/scrfree_nodecl.tld");
    jstlTCKCommonJar.addAsResource(new UrlAsset(TestTag.class.getClassLoader().getResource(packagePathTags+"/tlds/scrfree_noexpr.tld")), "META-INF/scrfree_noexpr.tld");
    jstlTCKCommonJar.addAsResource(new UrlAsset(TestTag.class.getClassLoader().getResource(packagePathTags+"/tlds/scrfree_nortexpr.tld")), "META-INF/scrfree_nortexpr.tld");
    jstlTCKCommonJar.addAsResource(new UrlAsset(TestTag.class.getClassLoader().getResource(packagePathTags+"/tlds/scrfree_noscr.tld")), "META-INF/scrfree_noscr.tld");
        
    jstlTCKCommonJar.addPackages(true,"com.sun.ts.tests.jstl.common");

    jstlTCKCommonJar.add(new UrlAsset(Resources_en.class.getClassLoader().getResource(packagePathResources+"/AlgoResources_en_IE_EURO.properties")), packagePathResources+"/AlgoResources_en_IE_EURO.properties");
    jstlTCKCommonJar.add(new UrlAsset(Resources_en.class.getClassLoader().getResource(packagePathResources+"/AlgoResources.properties")), packagePathResources+"/AlgoResources.properties");

    return jstlTCKCommonJar;
  }

  @BeforeEach
  public void setup() throws Exception {

    logger.log(Logger.Level.INFO, "setup method AbstractUrlClient");

    if (url == null){
      throw new Exception(
          "[AbstractUrlClient] The url was not injected");
    }

		String hostname = url.getHost();
		String portnum = Integer.toString(url.getPort());

		assertFalse(isNullOrEmpty(hostname), "[AbstractUrlClient] 'webServerHost' was not set in the properties.");
		_hostname = hostname.trim();
		assertFalse(isNullOrEmpty(portnum), "[AbstractUrlClient] 'webServerPort' was not set in the properties.");
		_port = Integer.parseInt(portnum.trim());

    logger.log(Logger.Level.INFO, "[AbstractUrlClient] Test setup OK");
  }


  /**
   * Sets the test properties for this testCase.
   *
   * @param testCase
   *          - the current test case
   */
  protected void setTestProperties(WebTestCase testCase) {
    setStandardProperties(TEST_PROPS.getProperty(STANDARD), testCase);
    setStandardCompatProperties(TEST_PROPS.getProperty(STANDARD_COMPAT),
        testCase);
    if (testCase.getRequest() == null) {
      String test = TEST_PROPS.getProperty(REQUEST);
      if (test.indexOf("HTTP/") < 0) {
        StringBuffer sb = new StringBuffer(25);
        sb.append(GET).append(_contextRoot);
        sb.append(SL).append(test).append(HTTP11);
        HttpRequest req = new HttpRequest(sb.toString(), _hostname, _port);
        testCase.setRequest(req);
      }

      setGoldenFileStreamProperty(testCase, goldenFileStream);
    }

    super.setTestProperties(testCase);
  }

  /**
   * Sets the goldenfile directory
   * 
   * @param goldenDir
   *          goldenfile directory based off test directory
   */
  public void setGoldenFileDir(String goldenDir) {
    GOLDENFILEDIR = goldenDir;
  }

  /**
   * Consists of a test name, a request, and a goldenfile.
   * 
   * @param testValue
   *          - a logical test identifier
   * @param testCase
   *          - the current test case
   */
  private void setStandardProperties(String testValue, WebTestCase testCase) {

    if (testValue == null) {
      return;
    }
    // A standard test sets consists of a testname
    // a request, and a goldenfile. The URI is not used
    // in this case since the JSP's are assumed to be located
    // at the top of the contextRoot

    // A standard test sets consists of a testname
    // a request, and a goldenfile
    StringBuffer sb = new StringBuffer(50);

    // set the testname
    _testName = testValue;

    // set the request
    sb.append(GET).append(_contextRoot).append(SL);
    sb.append(testValue).append(JSP_SUFFIX).append(HTTP11);
    // setRequest(sb.toString());
    HttpRequest req = new HttpRequest(sb.toString(), _hostname, _port);
    testCase.setRequest(req);

  }

  /**
   * Consists of a test name, a request, and a goldenfile.
   * 
   * @param testValue
   *          - a logical test identifier
   * @param testCase
   *          - the current test case
   */
  private void setStandardCompatProperties(String testValue,
      WebTestCase testCase) {

    if (testValue == null) {
      return;
    }
    // A standard test sets consists of a testname
    // a request, and a goldenfile. The URI is not used
    // in this case since the JSP's are assumed to be located
    // at the top of the contextRoot

    // A standard test sets consists of a testname
    // a request, and a goldenfile
    StringBuffer sb = new StringBuffer(50);

    // set the testname
    _testName = testValue;

    // set the request
    sb.append(GET).append(_contextRoot).append(SL);
    sb.append(testValue).append(JSP_SUFFIX).append(HTTP11);
    // setRequest(sb.toString());
    HttpRequest req = new HttpRequest(sb.toString(), _hostname, _port);
    testCase.setRequest(req);

  }

  public boolean isJavaVersion20OrGreater() {
    boolean isJavaVersion20OrGreater = false;

    String version = System.getProperty("java.version");
    int majorVersionDot = version.indexOf(".");

    version = version.substring(0, majorVersionDot);

    if (Integer.parseInt(version) >= 20) {
        isJavaVersion20OrGreater = true;
    }

    return isJavaVersion20OrGreater;
  }

}
