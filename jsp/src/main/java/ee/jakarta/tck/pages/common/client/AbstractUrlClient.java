/*
 * Copyright (c) 2007, 2023 Oracle and/or its affiliates. All rights reserved.
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

package ee.jakarta.tck.pages.common.client;

import com.sun.ts.tests.common.webclient.BaseUrlClient;
import com.sun.ts.tests.common.webclient.WebTestCase;
import com.sun.ts.tests.common.webclient.http.HttpRequest;

import java.net.URL;

import java.util.stream.Collectors;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import org.jboss.arquillian.container.test.api.OperateOnDeployment;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import static org.junit.jupiter.api.Assertions.assertFalse;
import java.lang.System.Logger;

/**
 * Base client for JSP tests.
 */
public abstract class AbstractUrlClient extends BaseUrlClient {

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

  /**
   * Identifier for a set of properties to be used for API Tests.
   */
  protected static final String APITEST = "apitest";

  /**
   * Alternate API test identifier,
   */
  protected static final String APITEST1 = "apitest1";

  /**
   * Test pass notification.
   */
  private static final String PASSED = "Test PASSED";

  /**
   * Test failure notification.
   */
  private static final String FAILED = "Test FAILED";

  /**
   * The test JSP.
   */
  private String _jspName = null;

  protected InputStream goldenFileStream = null;

  public InputStream getGoldenFileStream() {
    return goldenFileStream;
  }

  public void setGoldenFileStream(InputStream gfStream) {
    goldenFileStream = gfStream;
  }

  /**
   * Sets the test properties for this testCase.
   *
   * @param testCase
   *          - the current test case
   */
  protected void setTestProperties(WebTestCase testCase) {

    setStandardProperties(TEST_PROPS.getProperty(STANDARD), testCase);
    setGoldenFileStreamProperty(testCase, goldenFileStream);
    setApiTestProperties(TEST_PROPS.getProperty(APITEST), testCase);
    setApiTest1Properties(TEST_PROPS.getProperty(APITEST1), testCase);
    super.setTestProperties(testCase);
  }

  private void setGoldenFileStreamProperty(WebTestCase testCase, InputStream gfStream) {
    testCase.setGoldenFileStream(gfStream);
  }

  protected boolean isNullOrEmpty(String val) {
    if (val == null || val.equals("")) {
      return true;
    }
    return false;
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

		assertFalse(isNullOrEmpty(hostname), "[AbstractUrlClient] 'webServerHost' was not set.");
		_hostname = hostname.trim();
		assertFalse(isNullOrEmpty(portnum), "[AbstractUrlClient] 'webServerPort' was not set.");
		_port = Integer.parseInt(portnum.trim());

    logger.log(Logger.Level.INFO, "[AbstractUrlClient] Test setup OK");
  }

  /**
   * Sets the name of the JSP when building the request.
   *
   * @param jsp
   *          - JSP name
   */
  public void setTestJsp(String jsp) {
    _jspName = jsp;
  }

  /**
   * Sets the request and test name properties.
   * 
   * @param testValue
   *          - a logical test identifier
   * @param testCase
   *          - the current test case
   */
  private void setApiTest1Properties(String testValue, WebTestCase testCase) {
    if (testValue == null) {
      return;
    }

    // This altername API test just sets the test name and the request.
    _testName = testValue;
    String request = getApiTestRequest(_contextRoot, _jspName, testValue);
    HttpRequest req = new HttpRequest(request, _hostname, _port);
    testCase.setRequest(req);
  }

  /**
   * Sets the request, testname, and a search string for test passed. A search
   * is also added for test failure. If found, the test will fail.
   *
   * @param testValue
   *          - a logical test identifier
   * @param testCase
   *          - the current test case
   */
  private void setApiTestProperties(String testValue, WebTestCase testCase) {
    if (testValue == null) {
      return;
    }

    // An API test consists of a request with a request parameter of
    // testname, a search string of Test PASSED, and a logical test name.

    // set the testname
    _testName = testValue;

    // set the request
    String request = getApiTestRequest(_contextRoot, _jspName, testValue);

    HttpRequest req = new HttpRequest(request, _hostname, _port);
    testCase.setRequest(req);

    testCase.setResponseSearchString(PASSED);
    testCase.setUnexpectedResponseSearchString(FAILED);

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
    StringBuffer sb = new StringBuffer(50);

    // set the testname
    _testName = testValue;

    // set the request
    sb.append(GET).append(_contextRoot).append(SL);
    sb.append(testValue).append(JSP_SUFFIX).append(HTTP10);
    // setRequest(sb.toString());
    HttpRequest req = new HttpRequest(sb.toString(), _hostname, _port);
    testCase.setRequest(req);

    // set the goldenfile
    sb = new StringBuffer(50);
    sb.append(_tsHome).append(GOLDENFILEDIR);
    sb.append(_generalURI).append(SL);
    sb.append(testValue).append(GF_SUFFIX);
    // setGoldenFilePath(sb.toString());
    testCase.setGoldenFilePath(sb.toString());
  }

  /**
   * Returns full request based of provided context root, jsp name, and test
   * name.
   * 
   * @param contextRoot
   *          - request context root
   * @param jspName
   *          - JSP name
   * @param testName
   *          - test name to execute.
   * @return - Absolution URL to invoke the specified test.
   */
  private static String getApiTestRequest(String contextRoot, String jspName,
      String testName) {
    StringBuffer sb = new StringBuffer(50);
    sb.append(GET).append(contextRoot).append(SL);
    sb.append(jspName).append(JSP_SUFFIX).append("?testname=");
    sb.append(testName).append(HTTP11);
    return sb.toString();
  }

  public static String toString(InputStream inStream) throws IOException{
    try (BufferedReader bufReader = new BufferedReader(new InputStreamReader(inStream, StandardCharsets.UTF_8))) {
      return bufReader.lines().collect(Collectors.joining(System.lineSeparator()));
    }
  }

}
