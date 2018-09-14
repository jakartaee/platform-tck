/*
 * Copyright (c) 2013, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.websocket.common.client;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.websocket.ClientEndpointConfig;
import javax.websocket.ClientEndpointConfig.Configurator;
import javax.websocket.Endpoint;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.ServiceEETest;
import com.sun.ts.lib.util.BASE64Encoder;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.common.webclient.TestFailureException;
import com.sun.ts.tests.common.webclient.validation.CheckOneOfStatusesTokenizedValidator;
import com.sun.ts.tests.websocket.common.client.ClientEndpoint.ClientEndpointData;

/**
 * The common client that contains common methods
 */
public abstract class WebSocketCommonClient extends ServiceEETest {

  private static final long serialVersionUID = 1L;

  /**
   * The property that is set after invoke(), to let know the cleanup of
   * previous test should be performed so that more invocations are possible
   */
  boolean isTestCaseAfterInvocation;

  /**
   * TS Webserver host property
   */
  protected static final String SERVLETHOSTPROP = "webServerHost";

  /**
   * TS Webserver port property
   */
  protected static final String SERVLETPORTPROP = "webServerPort";

  /**
   * TS home property
   */
  protected static final String TSHOME = "ts_home";

  /**
   * Single invocation incremental test properties
   */
  protected Hashtable<Property, String> TEST_PROPS;

  /**
   * Context root of target tests
   */
  protected String _contextRoot = null;

  /**
   * Target webserver hostname
   */
  protected String _hostname = null;

  /**
   * Target webserver port
   */
  protected int _port = 0;

  /**
   * The protocol to be used for invoking a request, ws, wss, http, https, ...
   */
  protected String requestProtocol = "ws";

  /**
   * location of _tsHome
   */
  protected String _tsHome = null;

  /**
   * Property from ts.jte
   */
  protected int _ws_wait = 0;

  /**
   * The test case of given specified websocket message type
   */
  protected WebSocketTestCase testCase;

  /**
   * disable logging stack trace of exception caused on invoke() for the tests
   * that expects the exception to be thrown
   */
  protected boolean logExceptionOnInvoke = true;

  /**
   * Entity of type T, where T is the type of WebSocketTestCase, e.g. String by
   * default. As websocket supports partial messages, this Entity is an array of
   * objects; For whole message, only one Object in the array is used. When more
   * objects are utilized, partial message is being sent.
   */
  public class Entity {
    private Object[] entities;

    public Entity(Object... partials) {
      entities = partials;
    }

    public Entity(Object entity) {
      this(new Object[] { entity });
    }

    public boolean isInstance(Class<?> clazz) {
      return clazz.isInstance(entities[0]);
    }

    public int length() {
      return entities.length;
    }

    @SuppressWarnings("unchecked")
    public <T> T getEntityAt(Class<T> clazz, int index) {
      if (ByteBuffer.class.isAssignableFrom(clazz))
        return (T) cloneByteBuffer(index);
      return (T) entities[index];
    }

    @SuppressWarnings("unchecked")
    public <T> T getEntityAt(int index) {
      if (ByteBuffer.class.isInstance(entities[index]))
        return (T) cloneByteBuffer(index);
      return (T) entities[index];
    }

    private ByteBuffer clone(ByteBuffer message) {
      byte[] orig = message.array();
      byte[] array = new byte[orig.length];
      System.arraycopy(orig, 0, array, 0, orig.length);
      return ByteBuffer.wrap(array);
    }

    private ByteBuffer cloneByteBuffer(int index) {
      ByteBuffer bb = (ByteBuffer) entities[index];
      return clone(bb);
    }

    @Override
    public String toString() {
      StringBuilder sb = new StringBuilder();
      sb.append(entities.length);
      if (entities.length == 1)
        sb.append(" part: ");
      else
        sb.append(" parts: ");
      sb.append(objectsToStringWithDelimiter(", ", entities));
      return sb.toString();
    }
  }

  protected Entity entity;

  /**
   * the list of properties to be put into a property table
   */
  protected enum Property {
    BASIC_AUTH_PASSWD, BASIC_AUTH_USER, //
    CONTENT, EXPECTED_HEADERS, //
    IGNORE_STATUS_CODE, REQUEST, //
    SEARCH_STRING, SEARCH_STRING_IGNORE_CASE, STATUS_CODE, //
    UNEXPECTED_HEADERS, UNEXPECTED_RESPONSE_MATCH, //
    UNORDERED_SEARCH_STRING;
  }

  // ////////////////////////////////////////////////////////////////////////
  public String getContextRoot() {
    return _contextRoot;
  }

  public void setContextRoot(String root) {
    TestUtil.logTrace("[WebSocketCommonClient] Contextroot set at " + root);
    _contextRoot = root;
  }

  /**
   * Set property values to a given key if none exists, or adds all property
   * values to a given key when one exists, unless the property is a content
   */
  protected void setProperty(Property key, String... values) {
    for (String singleValue : values)
      setProperty(key, singleValue);
  }

  /**
   * Set property value to a given key if none exists, or adds a property value
   * to a given key when one exists, unless the property is a content. When
   * content, only the last single content is sent as a part of request
   */
  protected void setProperty(Property key, String value) {
    switch (key) {
    case CONTENT:
      setEntity(value);
      break;
    default:
      String oldValue = TEST_PROPS.get(key);
      if (oldValue == null) {
        TEST_PROPS.put(key, value);
      } else {
        int len = value.length() + oldValue.length() + 1;
        StringBuilder combinedValue = new StringBuilder(len);
        combinedValue.append(oldValue).append("|").append(value);
        TEST_PROPS.put(key, combinedValue.toString());
      }
      break;
    }
  }

  protected String buildRequest(String... path) {
    StringBuilder sb = new StringBuilder();
    sb.append(getAbsoluteUrl()).append("/");
    for (String segment : path)
      sb.append(segment);
    return sb.toString();
  }

  protected String getAbsoluteUrl() {
    StringBuilder sb = new StringBuilder();
    sb.append(requestProtocol).append("://").append(_hostname).append(":")
        .append(_port).append("/").append(getContextRoot());
    return sb.toString();
  }

  /**
   * Shortcut to invoke() when endpoint, content, and search is known
   * 
   * @param endpoint
   *          the endpoint the request is built to go to.
   * @param content
   *          the content to be set as setEntity(content.name())
   * @param search
   *          a response would be searched for search.name()
   * @throws Fault
   *           when invocation fails
   */
  protected <T extends Enum<T>> //
  void invoke(String endpoint, Enum<T> content, Enum<T> search) throws Fault {
    invoke(endpoint, content.name(), search.name());
  }

  /**
   * Shortcut to invoke() when endpoint, content, and search is known
   * 
   * @param endpoint
   *          the endpoint the request is built to go to.
   * @param content
   *          the content to be set as setEntity(content.name())
   * @param search
   *          a String the response would be searched for
   * @throws Fault
   *           when invocation fails
   */
  protected//
  void invoke(String endpoint, Enum<?> content, String... search) throws Fault {
    invoke(endpoint, content.name(), search);
  }

  /**
   * Shortcut to invoke() when endpoint, content, and search is known
   * 
   * @param endpoint
   *          the endpoint the request is built to go to.
   * @param content
   *          the content to be set as setEntity(content)
   * @param search
   *          the content of a response to be searched, or "" when does not
   *          matter
   * @throws Fault
   *           when invocation fails
   */
  protected void //
      invoke(String endpoint, Object content, String... search) throws Fault {
    invoke(endpoint, content, search, true);
  }

  /**
   * Shortcut to invoke() when endpoint, content, and search is known
   * 
   * @param endpoint
   *          the endpoint the request is built to go to.
   * @param content
   *          the content to be set as setEntity(content)
   * @param search
   *          the content of a response to be searched, or "" when does not
   *          matter
   * @param cleanup
   *          see {@link #invoke(boolean)}
   * @throws Fault
   *           when invocation fails
   */
  protected void //
      invoke(String endpoint, Object content, String[] search, boolean cleanup)
          throws Fault {
    setProperty(Property.REQUEST, buildRequest(endpoint));
    setEntity(content);
    setProperty(Property.SEARCH_STRING, search);
    invoke(cleanup);
  }

  protected void //
      invoke(String endpoint, Object content, String search, boolean cleanup)
          throws Fault {
    invoke(endpoint, content, new String[] { search }, cleanup);
  }

  /**
   * Shortcut to invokeAgain(cleanup) when endpoint, content, and search is
   * known
   * 
   * @param content
   *          the content to be set as setEntity(content)
   * @param search
   *          the content of a response to be searched, or "" when does not
   *          matter
   * @param cleanup
   *          see {@link #invokeAgain(boolean)}
   * @throws Fault
   *           when invocation fails
   */
  protected void //
      invokeAgain(Object content, String search, boolean cleanup) throws Fault {
    setEntity(content);
    setProperty(Property.SEARCH_STRING, search);
    invokeAgain(cleanup);
  }

  /**
   * <PRE>
   * Invokes a test based on the properties
   * stored in TEST_PROPS.  Once the test has completed,
   * the properties in TEST_PROPS will be cleared.
   * </PRE>
   * 
   * @throws Fault
   *           If an error occurs during the test run
   */
  protected void invoke() throws Fault {
    invoke(true);
  }

  /**
   * <PRE>
   * Invokes a test based on the properties
   * stored in TEST_PROPS.  Once the test has completed,
   * the properties in TEST_PROPS will be cleared if cleanUp says so.
   * </PRE>
   * 
   * @throws Fault
   *           If an error occurs during the test run
   * @param cleanUp
   *          Defines whether the test has ended
   * 
   */
  protected void invoke(boolean cleanUp) throws Fault {
    invoke(cleanUp, false);
  }

  /**
   * Invoke additional request with new entity and hold open
   * {@link javax.websocket.Session} to a server {@link Endpoint}, the response
   * is caught by client {@link Endpoint} set by current
   * {@link WebSocketTestCase}
   * <p/>
   * 
   * @throws Fault
   */
  protected void invokeAgain(boolean cleanUp) throws Fault {
    invoke(cleanUp, true);
  }

  /**
   * <PRE>
   * Invokes a test based on the properties
   * stored in TEST_PROPS. The current session is used. Once the test has completed,
   * the properties in TEST_PROPS will be cleared if cleanUp says so.
   * </PRE>
   * 
   * @throws Fault
   *           If an error occurs during the test run
   * @param cleanUp
   *          Defines whether the test has ended
   * @param again
   *          the request to server endpoint is performed on currently
   *          established session, when the session has not been closed after
   *          previous {@link #invoke(false)};
   */
  protected void invoke(boolean cleanUp, boolean again) throws Fault {
    TestUtil.logTrace("[WebSocketCommonClient] invoke");
    TestUtil.logTrace("[WebSocketCommonClient] EXECUTING");
    setTestProperties(getTestCase());
    try {
      executeTestCase(again);
      testCase.validate();
      assertCountDownLatchCount();
    } catch (TestFailureException tfe) {
      dealWithTestFailureException(tfe);
    } finally {
      isTestCaseAfterInvocation = cleanUp;
      if (cleanUp) {
        cleanup();
      }
    }
  }

  /**
   * Executes test case for the first time, or once again on existing
   * {@link WebSocketTestCase} and existing {@link Session} for a new
   * {@link Entity}
   * 
   * @param again
   * @throws TestFailureException
   */
  protected//
  void executeTestCase(boolean again) throws TestFailureException, Fault {
    if (again) {
      testCase.newCountDown();
      SendMessageCallback callback = new SendMessageCallback(entity);
      callback.onOpen(getSession(), null);
      testCase.awaitCountDown();
    } else
      testCase.execute();
  }

  private//
  void dealWithTestFailureException(TestFailureException tfe) throws Fault {
    Throwable t = tfe.getRootCause();
    if (t != null && logExceptionOnInvoke) {
      TestUtil.logErr("Root cause of Failure: " + t.getMessage(), t);
    } else
      logExceptionOnInvoke = true;
    fault(tfe, "[WebSocketCommonClient]", sTestCase,
        "failed!  Check output for cause of failure.");
  }

  /**
   * <PRE>
   * Sets the appropriate test properties based
   * on the values stored in TEST_PROPS
   * </PRE>
   */
  protected void setTestProperties(WebSocketTestCase testCase) {
    TestUtil.logTrace("[WebSocketCommonClient] setTestProperties");

    if (TEST_PROPS.get(Property.STATUS_CODE) == null)
      setProperty(Property.STATUS_CODE, "200");
    setWebTestCaseProperties(testCase);
  }

  protected void setWebTestCaseProperties(WebSocketTestCase testCase) {
    Property key = null;
    String value = null;
    // process the remainder of the properties
    for (Enumeration<Property> e = TEST_PROPS.keys(); e.hasMoreElements();) {
      key = e.nextElement();
      value = TEST_PROPS.get(key);
      switch (key) {
      case BASIC_AUTH_USER:
        break;
      case BASIC_AUTH_PASSWD:
        final String user = TEST_PROPS.get(Property.BASIC_AUTH_USER);
        final String password = TEST_PROPS.get(Property.BASIC_AUTH_PASSWD);
        Configurator configurator = new Configurator() {
          @Override
          public void beforeRequest(Map<String, List<String>> headers) {
            headers.putAll(basicAuthenticationAsHeaderMap(user, password));
            super.beforeRequest(headers);
          }
        };
        addClientConfigurator(configurator);
        break;
      case CONTENT:
        setEntity(value);
        break;
      case EXPECTED_HEADERS:
        testCase.addExpectedHeader(value);
        TEST_PROPS.remove(Property.EXPECTED_HEADERS);
        break;
      case IGNORE_STATUS_CODE:
        testCase.setExpectedStatusCode("-1");
        break;
      case REQUEST:
        break;
      case SEARCH_STRING:
        value = testCase.getTextCaser().getCasedText(value);
        testCase.setResponseSearchString(value);
        TEST_PROPS.remove(Property.SEARCH_STRING);
        break;
      case SEARCH_STRING_IGNORE_CASE:
        testCase.setResponseSearchStringIgnoreCase(value);
        TEST_PROPS.remove(Property.SEARCH_STRING_IGNORE_CASE);
        break;
      case STATUS_CODE:
        if (value.contains("|"))
          testCase.setStrategy(
              CheckOneOfStatusesTokenizedValidator.class.getName());
        testCase.setExpectedStatusCode(value);
        break;
      case UNEXPECTED_HEADERS:
        testCase.addUnexpectedHeader(value);
        TEST_PROPS.remove(Property.UNEXPECTED_HEADERS);
        break;
      case UNEXPECTED_RESPONSE_MATCH:
        testCase.setUnexpectedResponseSearchString(value);
        TEST_PROPS.remove(Property.UNEXPECTED_RESPONSE_MATCH);
        break;
      case UNORDERED_SEARCH_STRING:
        value = testCase.getTextCaser().getCasedText(value);
        testCase.setUnorderedSearchString(value);
        TEST_PROPS.remove(Property.UNORDERED_SEARCH_STRING);
        break;
      }
    }
  }

  protected WebSocketTestCase getTestCase() {
    if (testCase == null || isTestCaseAfterInvocation) {
      testCase = new WebSocketTestCase(this);
      isTestCaseAfterInvocation = false;
    }
    return testCase;
  }

  protected <MSGTYPE> //
  void setEndPointCase(Class<ClientEndpoint<MSGTYPE>> endpoint) {
    getTestCase().setClientEndpoint(endpoint);
  }

  protected Entity setEntity(Object... partials) {
    this.entity = new Entity(partials);
    return this.entity;
  }

  // /////////////////////////// JavaTest methods ///////////////////////////
  /**
   * This pattern is used in all subclasses
   */
  protected Status run(String[] args) {
    Status s;
    s = run(args, new PrintWriter(System.out), new PrintWriter(System.err));
    s.exit();
    return s;
  }

  /**
   * <code>cleanup</code> is called by the test harness to cleanup after text
   * execution
   * 
   * @exception Fault
   *              if an error occurs
   */
  public void cleanup() throws Fault {
    TEST_PROPS.clear();
    isTestCaseAfterInvocation = true;
    try {
      if (testCase != null && testCase.session != null) {
        if (testCase.session.isOpen()) {
          ClientEndpointData.newOnCloseCountDown();
          logTrace("[WebSocketCommonClient] session.close() on session id",
              testCase.session.getId());
          testCase.session.close();
          ClientEndpointData.awaitOnClose();
        }
        testCase.session = null;
      }
      synchronized (ClientEndpointData.LOCK) {
        ClientEndpointData.callback = null;
      }
    } catch (IOException e) {
      fault(e);
    }
    TestUtil.logMsg("[WebSocketCommonClient] Test cleanup OK");
  }

  /**
   * <code>setup</code> is by the test harness to initialize the tests.
   * 
   * @param args
   *          a <code>String[]</code> value
   * @param p
   *          a <code>Properties</code> value
   * @exception Fault
   *              if an error occurs
   */
  public void setup(String[] args, Properties p) throws Fault {
    TestUtil.logTrace("setup method WebSocketCommonClient");

    String hostname = p.getProperty(SERVLETHOSTPROP);
    String portnum = p.getProperty(SERVLETPORTPROP);
    String tshome = p.getProperty(TSHOME);
    String wswait = p.getProperty("ws_wait");

    assertFalse(isNullOrEmpty(hostname),
        "[WebSocketCommonClient] 'webServerHost' was not set in the build.properties.");
    _hostname = hostname.trim();
    assertFalse(isNullOrEmpty(portnum),
        "[WebSocketCommonClient] 'webServerPort' was not set in the build.properties.");
    _port = Integer.parseInt(portnum.trim());
    assertFalse(isNullOrEmpty(tshome),
        "[WebSocketCommonClient] 'tshome' was not set in the build.properties.");
    _tsHome = tshome.trim();
    assertFalse(isNullOrEmpty(wswait),
        "[WebSocketCommonClient] 'ws_wait' must be set in ts.jte");
    _ws_wait = Integer.parseInt(wswait.trim());
    assertTrue(_ws_wait > 0,
        "[WebSocketCommonClient] 'ws_wait' (in seconds) ts.jte must be set greater than 0");

    TestUtil.logMsg("[WebSocketCommonClient] Test setup OK");
    TEST_PROPS = new Hashtable<Property, String>();
  }

  // ///////////////////////////// Utility methods
  // //////////////////////////////

  protected void assertCountDownLatchCount() throws Fault {
    long hits = testCase.getCountDownLatchRemainingHits();
    int count = testCase.getCountDownLatchTotalCount();
    WebSocketCommonClient.assertEqualsLong(0, hits,
        "The countDownLatch has been hit only", count - hits, "was expected",
        count, "times");
    logTrace("[WebSocketCommonClient] CountDownLatch has been hit", count,
        "times as expected");
  }

  /**
   * Asserts that a condition is true.
   * 
   * @param condition
   *          tested condition
   * @param message
   *          a space separated message[i].toString() compilation for
   *          i=<0,message.length)
   * @throws Fault
   *           when conditionTrue is not met with message provided
   */
  public static void //
      assertTrue(boolean condition, Object... message) throws Fault {
    if (!condition)
      fault(message);
  }

  /**
   * Asserts that a condition is false.
   * 
   * @param condition
   *          tested condition
   * @param message
   *          a space separated message[i].toString() compilation for
   *          i=<0,message.length)
   * @throws Fault
   *           when condition is not false with message provided
   */
  public static void //
      assertFalse(boolean condition, Object... message) throws Fault {
    assertTrue(!condition, message);
  }

  /**
   * Asserts that two objects are equal. When instances of Comparable, such as
   * String, compareTo is used.
   * 
   * @param first
   *          first object
   * @param second
   *          second object
   * @param message
   *          a space separated message[i].toString() compilation for
   *          i=<0,message.length)
   * @throws Fault
   *           when objects are not equal with message provided
   */
  @SuppressWarnings("unchecked")
  public static <T> void //
      assertEquals(T first, T second, Object... message) throws Fault {
    if (first == null && second == null)
      return;
    assertFalse(first == null && second != null, message);
    assertFalse(first != null && second == null, message);
    if (first instanceof Comparable)
      assertTrue(((Comparable<T>) first).compareTo(second) == 0, message);
    else
      assertTrue(first.equals(second), message);
  }

  public static <T> void //
      assertEqualsInt(int first, int second, Object... message) throws Fault {
    assertTrue(first == second, message);
  }

  public static <T> void //
      assertEqualsLong(long first, long second, Object... message)
          throws Fault {
    assertTrue(first == second, message);
  }

  public static <T> void //
      assertEqualsBool(boolean first, boolean second, Object... message)
          throws Fault {
    assertTrue(first == second, message);
  }

  /**
   * Asserts that an object is null.
   * 
   * @param object
   *          Assert that object is not null
   * @param message
   *          a space separated message[i].toString() compilation for
   *          i=<0,message.length)
   * @throws Fault
   *           when condition is not met with message provided
   */
  public static void //
      assertNull(Object object, Object... message) throws Fault {
    assertTrue(object == null, message);
  }

  /**
   * Asserts that an object is not null.
   * 
   * @param object
   *          Assert that object is not null
   * @param message
   *          a space separated message[i].toString() compilation for
   *          i=<0,message.length)
   * @throws Fault
   *           when condition is not met with message provided
   */
  public static void //
      assertNotNull(Object object, Object... message) throws Fault {
    assertTrue(object != null, message);
  }

  /**
   * Throws Fault with space separated objects[1],object[2],...,object[n]
   * message
   * 
   * @param objects
   *          objects whose toString() results will be added to Fault message
   * @throws Fault
   *           fault with space separated objects.toString values
   */
  public static void fault(Object... objects) throws Fault {
    throw new Fault(objectsToString(objects));
  }

  /**
   * Throws Fault with space separated objects[1],object[2],...,object[n]
   * message
   * 
   * @param t
   *          Throwable to pass to Fault
   * @param objects
   *          objects whose toString() results will be added to Fault message
   * @throws Fault
   *           fault with space separated objects.toString values
   */
  public static void fault(Throwable t, Object... objects) throws Fault {
    throw new Fault(objectsToString(objects), t);
  }

  /**
   * Assert that given substring is a substring of given string
   * 
   * @param string
   *          the string to search substring in
   * @param substring
   *          the substring to be searched in a given string
   * @param message
   *          space separated message values to be thrown
   * @throws Fault
   *           throws
   */
  public static void assertContains(String string, String substring,
      Object... message) throws Fault {
    assertTrue(string.contains(substring), message);
  }

  /**
   * Assert that given substring is a substring of given string, case
   * insensitive
   * 
   * @param string
   *          the string to search substring in
   * @param substring
   *          the substring to be searched in a given string
   * @param message
   *          space separated message values to be thrown
   * @throws Fault
   */
  public static void assertContainsIgnoreCase(String string, String substring,
      Object... message) throws Fault {
    assertTrue(string.toLowerCase().contains(substring.toLowerCase()), message);
  }

  /**
   * Assert that given substring is not a substring of given string
   * 
   * @param string
   *          the string to search substring in
   * @param substring
   *          the substring to be searched in a given string
   * @param message
   *          space separated message values to be thrown
   * @throws Fault
   *           throws
   */
  public static void assertNotContains(String string, String substring,
      Object... message) throws Fault {
    assertFalse(string.contains(substring), message);
  }

  /**
   * Assert that given substring is not a substring of given string, case
   * insensitive
   * 
   * @param string
   *          the string to search substring in
   * @param substring
   *          the substring to be searched in a given string
   * @param message
   *          space separated message values to be thrown
   * @throws Fault
   */
  public static void assertNotContainsIgnoreCase(String string,
      String substring, Object... message) throws Fault {
    assertFalse(string.toLowerCase().contains(substring.toLowerCase()),
        message);
  }

  /**
   * Assert that given subtext.toString() subject is a substring of given text
   * 
   * @param text
   *          the text.toString() object to search subtext.toString() in
   * @param subtext
   *          the subtext.toString() to be searched in a given text.toString()
   * @param message
   *          space separated message values to be thrown
   * @throws Fault
   */
  public static <T> void assertContains(T text, T subtext, Object... message)
      throws Fault {
    assertContains(text.toString(), subtext.toString(), message);
  }

  /**
   * Assert that given subtext.toString() subject is a substring of given text,
   * case insensitive
   * 
   * @param text
   *          the text.toString() object to search subtext.toString() in
   * @param subtext
   *          the subtext.toString() to be searched in a given text.toString()
   * @param message
   *          space separated message values to be thrown
   * @throws Fault
   */
  public static <T> void assertContainsIgnoreCase(T text, T subtext,
      Object... message) throws Fault {
    assertContainsIgnoreCase(text.toString(), subtext.toString(), message);
  }

  public static//
  String assertProperty(Properties p, String propertyName) throws Fault {
    String value = p.getProperty(propertyName);
    logTrace(propertyName, value);
    assertTrue(!isNullOrEmpty(value), propertyName,
        "was not set in build.properties");
    return value;
  }

  /**
   * Searches an encapsulated exception cause in parent exception
   */
  protected static <T extends Throwable> T assertCause(Throwable parent,
      Class<T> wrapped, Object... msg) throws Fault {
    T t = hasCause(parent, wrapped);
    assertNotNull(t, msg);
    return t;
  }

  /**
   * Check whether the string is either null or blank
   */
  protected static boolean isNullOrEmpty(String val) {
    return val == null || val.trim().equals("");
  }

  @SuppressWarnings("unchecked")
  private static <T extends Throwable> T //
      hasCause(Throwable parent, Class<? extends Throwable> cause) {
    while (parent != null) {
      if (cause.isInstance(parent))
        return (T) parent;
      parent = parent.getCause();
    }
    return null;
  }

  public static String getCauseMessage(Throwable t) {
    String msg = null;
    while (t != null) {
      msg = t.getMessage();
      t = t.getCause();
    }
    return msg;
  }

  public static void logMsg(Object... msg) {
    TestUtil.logMsg(objectsToString(msg));
  }

  public static void logTrace(Object... msg) {
    TestUtil.logTrace(objectsToString(msg));
  }

  /**
   * @param objects
   *          to be put in a sentence
   * @return objects in a single string , each object separated by " "
   */
  protected static String objectsToString(Object... objects) {
    return objectsToStringWithDelimiter(" ", objects);
  }

  protected static String objectsToStringWithDelimiter(String delimiter,
      Object... objects) {
    StringBuilder sb = new StringBuilder();
    if (objects != null)
      for (Object o : objects) {
        if (o != null && o.getClass().isArray()
            && o.toString().startsWith("[L"))
          sb.append(objectsToStringWithDelimiter(delimiter, (Object[]) o));
        else
          sb.append(o).append(delimiter);
      }
    return sb.toString().trim();
  }

  /**
   * Creates basic authentication header for given username and password, to be
   * simply recalled in client Configurator, for instance
   * 
   * <pre>
   * Configurator configurator = new Configurator() {
   *   public void beforeRequest(Map&lt;String, List&lt;String&gt;&gt; headers) {
   *     headers.putAll(basicAuthenticationAsHeaderMap(USER, PASSWORD));
   *   };
   * };
   * setClientConfigurator(configurator);
   * </pre>
   * 
   * </p>
   * Note that this is done by client framework automatically when
   * 
   * <pre>
   * setProperty(Property.BASIC_AUTH_USER, USER);
   * setProperty(Property.BASIC_AUTH_PASSWD, PASSWORD);
   * </pre>
   * 
   * @param userName
   * @param password
   * @return a header map containing just Authorization header
   */
  protected static//
  Map<String, List<String>> basicAuthenticationAsHeaderMap(String userName,
      String password) {
    Map<String, List<String>> headers = new HashMap<String, List<String>>();
    String toEncode = new StringBuilder().append(userName).append(':')
        .append(password).toString();
    String base64 = new BASE64Encoder().encode(toEncode.getBytes());
    headers.put("Authorization", Arrays.asList("Basic " + base64));
    return headers;
  }

  // ---------------------Callbacks ---------------------------------
  /**
   * <p>
   * Possible cast to VI specific container, e.g. tyrus ClientManager and set
   * properties for debug purposes.
   * </p>
   * <p>
   * For instance, to delay the connection timeout, use
   * 
   * <pre>
   * ClientManager cm = (ClientManager) clientContainer;
   * cm.getProperties().put(ClientProperties.HANDSHAKE_TIMEOUT, 500000);
   * </pre>
   * 
   * @param clientContainer
   *          The container used for e.g.
   *          {@link WebSocketContainer#connectToServer(Class, java.net.URI)}
   *          </p>
   */
  protected void //
      setupWebSocketContainerBeforeConnect(WebSocketContainer clientContainer) {
  }

  // ---------------------Setters ---------------------------------
  /**
   * Sets the protocol the request starts with Default "ws"
   * 
   * @param protocol
   */
  protected void setRequestProtocol(String protocol) {
    this.requestProtocol = protocol;
  }

  protected void setCountDownLatchCount(int countDownLatchCount) {
    getTestCase().setCountDownLatchCount(countDownLatchCount);
  }

  /**
   * set ClientEndpoint class. This endpoint class can be overriden by client
   * endpoint instance set by {@link #setClientEndpointInstance}
   */
  protected void setClientEndpoint(
      Class<? extends ClientEndpoint<?>> endpoint) {
    getTestCase().setClientEndpoint(endpoint);
  }

  /**
   * The ClientEndpoint instance. It holds precedence over ClientEndpoint class
   * set by {@link #setClientEndpoint}
   */
  protected void setClientEndpointInstance(ClientEndpoint<?> endpointInstance) {
    getTestCase().setClientEndpointInstance(endpointInstance);
  }

  /**
   * The annotated client endpoint alternative to ClientEndpoint instance. It
   * has precedence over ClientEndpoint class set by {@link #setClientEndpoint}
   */
  protected void setAnnotatedClientEndpoint(
      AnnotatedClientEndpoint<?> annotatedEndpoint) {
    getTestCase().setAnnotatedClientEndpoint(annotatedEndpoint);
  }

  /**
   * Ensures given Configurator to be registered to any given or default
   * ClientEndpointConfig
   */
  protected void //
      addClientConfigurator(Configurator configurator) {
    getTestCase().addClientConfigurator(configurator);
  }

  protected void //
      setClientEndpointConfig(ClientEndpointConfig clientEndpointConfig) {
    getTestCase().setClientEndpointConfig(clientEndpointConfig);
  }

  /**
   * This method sets the main {@link EndpointCallback} to be used by a test. If
   * a callback is to be used in conjunction with default
   * {@link SendMessageCallback}, use
   * {@link #addClientCallback(EndpointCallback)} method.
   * </p>
   * Note that when no entity is sent to a server, no clientCallback is used.
   */
  protected void setClientCallback(EndpointCallback callback) {
    getTestCase().setCallback(callback);
  }

  /**
   * Add additional callback functionality to a main callback. The main callback
   * could either be set by {@link #setClientCallback(EndpointCallback)} method
   * or it is the default {@link SendMessageCallback} client callback.
   * </p>
   * Note that when no entity is sent to a server, no clientCallback is used.
   */
  protected void addClientCallback(EndpointCallback callback) {
    getTestCase().addClientCallback(callback);
  }

  protected Session getSession() throws Fault {
    Session session = getTestCase().getSession();
    assertNotNull(session, "Session is null! Has been closed?");
    return session;
  }

  protected String getResponseAsString() {
    return getTestCase().getResponseAsString();
  }

  protected <T> T getLastResponse(Class<T> type) {
    return getTestCase().getLastResponse(type);
  }

  protected void printClientCall(boolean printClientCall) {
    getTestCase().printClientCall(printClientCall);
  }

  protected void logExceptionOnInvocation(boolean log) {
    logExceptionOnInvoke = log;
  }

}
