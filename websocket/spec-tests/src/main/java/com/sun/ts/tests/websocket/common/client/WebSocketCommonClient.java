/*
 * Copyright (c) 2013, 2023 Oracle and/or its affiliates and others.
 * All rights reserved.
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.System.Logger;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import org.jboss.arquillian.container.test.api.OperateOnDeployment;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import com.sun.ts.lib.util.BASE64Encoder;
import com.sun.ts.tests.common.webclient.TestFailureException;
import com.sun.ts.tests.common.webclient.validation.CheckOneOfStatusesTokenizedValidator;
import com.sun.ts.tests.websocket.common.client.ClientEndpoint.ClientEndpointData;

import jakarta.websocket.ClientEndpointConfig;
import jakarta.websocket.ClientEndpointConfig.Configurator;
import jakarta.websocket.Endpoint;
import jakarta.websocket.Session;
import jakarta.websocket.WebSocketContainer;

/**
 * The common client that contains common methods
 */
public abstract class WebSocketCommonClient {

	private static final Logger logger = System.getLogger(WebSocketCommonClient.class.getName());

	private static final long serialVersionUID = 1L;

	/**
	 * The property that is set after invoke(), to let know the cleanup of previous
	 * test should be performed so that more invocations are possible
	 */
	boolean isTestCaseAfterInvocation;

	@ArquillianResource
	@OperateOnDeployment("_DEFAULT_")
	protected URL url;
	
	/**
	 * Webserver host property
	 */
	protected static final String SERVLETHOSTPROP = "webServerHost";

	/**
	 * Webserver port property
	 */
	protected static final String SERVLETPORTPROP = "webServerPort";

	/**
	 * ws_wait wait for the response.
	 */
	protected static final String WSWAIT = "ws_wait";

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
		logger.log(Logger.Level.TRACE, "[WebSocketCommonClient] Contextroot set at " + root);
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
	 * Set property value to a given key if none exists, or adds a property value to
	 * a given key when one exists, unless the property is a content. When content,
	 * only the last single content is sent as a part of request
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
		for (String segment : path) {
			sb.append(segment);
		}
		return sb.toString();
	}

	protected String getAbsoluteUrl() {
		StringBuilder sb = new StringBuilder();
		sb.append(requestProtocol).append("://").append(_hostname).append(":").append(_port).append("/")
				.append(getContextRoot());
		return sb.toString();
	}

	/**
	 * Shortcut to invoke() when endpoint, content, and search is known
	 * 
	 * @param endpoint the endpoint the request is built to go to.
	 * @param content  the content to be set as setEntity(content.name())
	 * @param search   a response would be searched for search.name()
	 * @throws Exception when invocation fails
	 */
	protected <T extends Enum<T>> //
	void invoke(String endpoint, Enum<T> content, Enum<T> search) throws Exception {
		invoke(endpoint, content.name(), search.name());
	}

	/**
	 * Shortcut to invoke() when endpoint, content, and search is known
	 * 
	 * @param endpoint the endpoint the request is built to go to.
	 * @param content  the content to be set as setEntity(content.name())
	 * @param search   a String the response would be searched for
	 * @throws Exception when invocation fails
	 */
	protected//
	void invoke(String endpoint, Enum<?> content, String... search) throws Exception {
		invoke(endpoint, content.name(), search);
	}

	/**
	 * Shortcut to invoke() when endpoint, content, and search is known
	 * 
	 * @param endpoint the endpoint the request is built to go to.
	 * @param content  the content to be set as setEntity(content)
	 * @param search   the content of a response to be searched, or "" when does not
	 *                 matter
	 * @throws Exception when invocation fails
	 */
	protected void //
			invoke(String endpoint, Object content, String... search) throws Exception {
		invoke(endpoint, content, search, true);
	}

	/**
	 * Shortcut to invoke() when endpoint, content, and search is known
	 * 
	 * @param endpoint the endpoint the request is built to go to.
	 * @param content  the content to be set as setEntity(content)
	 * @param search   the content of a response to be searched, or "" when does not
	 *                 matter
	 * @param cleanup  see {@link #invoke(boolean)}
	 * @throws Exception when invocation fails
	 */
	protected void //
			invoke(String endpoint, Object content, String[] search, boolean cleanup) throws Exception {
		setProperty(Property.REQUEST, buildRequest(endpoint));
		setEntity(content);
		setProperty(Property.SEARCH_STRING, search);
		invoke(cleanup);
	}

	protected void //
			invoke(String endpoint, Object content, String search, boolean cleanup) throws Exception {
		invoke(endpoint, content, new String[] { search }, cleanup);
	}

	/**
	 * Shortcut to invokeAgain(cleanup) when endpoint, content, and search is known
	 * 
	 * @param content the content to be set as setEntity(content)
	 * @param search  the content of a response to be searched, or "" when does not
	 *                matter
	 * @param cleanup see {@link #invokeAgain(boolean)}
	 * @throws Exception when invocation fails
	 */
	protected void //
			invokeAgain(Object content, String search, boolean cleanup) throws Exception {
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
	 * @throws Exception If an error occurs during the test run
	 */
	protected void invoke() throws Exception {
		invoke(true);
	}

	/**
	 * <PRE>
	 * Invokes a test based on the properties
	 * stored in TEST_PROPS.  Once the test has completed,
	 * the properties in TEST_PROPS will be cleared if cleanUp says so.
	 * </PRE>
	 * 
	 * @throws Exception If an error occurs during the test run
	 * @param cleanUp Defines whether the test has ended
	 * 
	 */
	protected void invoke(boolean cleanUp) throws Exception {
		invoke(cleanUp, false);
	}

	/**
	 * Invoke additional request with new entity and hold open
	 * {@link jakarta.websocket.Session} to a server {@link Endpoint}, the response
	 * is caught by client {@link Endpoint} set by current {@link WebSocketTestCase}
	 * <p/>
	 * 
	 * @throws Exception
	 */
	protected void invokeAgain(boolean cleanUp) throws Exception {
		invoke(cleanUp, true);
	}

	/**
	 * <PRE>
	 * Invokes a test based on the properties
	 * stored in TEST_PROPS. The current session is used. Once the test has completed,
	 * the properties in TEST_PROPS will be cleared if cleanUp says so.
	 * </PRE>
	 * 
	 * @throws Exception If an error occurs during the test run
	 * @param cleanUp Defines whether the test has ended
	 * @param again   the request to server endpoint is performed on currently
	 *                established session, when the session has not been closed
	 *                after previous {@link #invoke(false)};
	 */
	protected void invoke(boolean cleanUp, boolean again) throws Exception {
		logger.log(Logger.Level.TRACE, "[WebSocketCommonClient] invoke");
		logger.log(Logger.Level.TRACE, "[WebSocketCommonClient] EXECUTING");
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
	void executeTestCase(boolean again) throws TestFailureException, Exception {
		if (again) {
			testCase.newCountDown();
			SendMessageCallback callback = new SendMessageCallback(entity);
			callback.onOpen(getSession(), null);
			testCase.awaitCountDown();
		} else
			testCase.execute();
	}

	private//
	void dealWithTestFailureException(TestFailureException tfe) throws Exception {
		Throwable t = tfe.getRootCause();
		if (t != null && logExceptionOnInvoke) {
			logger.log(Logger.Level.ERROR, "Root cause of Failure: " + t.getMessage(), t);
		} else
			logExceptionOnInvoke = true;
		throw new Exception(
				tfe.getMessage() + "[WebSocketCommonClient] TestCase failed!  Check output for cause of failure.", t);
	}

	/**
	 * <PRE>
	 * Sets the appropriate test properties based
	 * on the values stored in TEST_PROPS
	 * </PRE>
	 */
	protected void setTestProperties(WebSocketTestCase testCase) {
		logger.log(Logger.Level.TRACE, "[WebSocketCommonClient] setTestProperties");

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
					testCase.setStrategy(CheckOneOfStatusesTokenizedValidator.class.getName());
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

	/**
	 * <code>cleanup</code> is called to cleanup after text execution
	 * 
	 * @exception Exception if an error occurs
	 */
	@AfterEach
	public void cleanup() throws Exception {
		TEST_PROPS.clear();
		isTestCaseAfterInvocation = true;
		if (testCase != null && testCase.session != null) {
			if (testCase.session.isOpen()) {
				ClientEndpointData.newOnCloseCountDown();
				logTrace("[WebSocketCommonClient] session.close() on session id", testCase.session.getId());
				testCase.session.close();
				ClientEndpointData.awaitOnClose();
			}
			testCase.session = null;
		}
		synchronized (ClientEndpointData.LOCK) {
			ClientEndpointData.callback = null;
		}
		logger.log(Logger.Level.DEBUG, "[WebSocketCommonClient] Test cleanup OK");
	}

	/**
	 * <code>setup</code> to initialize the tests.
	 * 
	 * @param args a <code>String[]</code> value
	 * @param p    a <code>Properties</code> value
	 * @exception Exception if an error occurs
	 */
	@BeforeEach
	public void setup() throws Exception {
		logger.log(Logger.Level.TRACE, "setup method WebSocketCommonClient");
		
		String hostname = System.getProperty(SERVLETHOSTPROP);
		String portnum = System.getProperty(SERVLETPORTPROP);

		hostname = isNullOrEmpty(hostname) ? url.getHost() : hostname;
		portnum = isNullOrEmpty(portnum) ? Integer.toString(url.getPort()) : portnum;
		String wswait = System.getProperty(WSWAIT);

		assertFalse(isNullOrEmpty(hostname), "[WebSocketCommonClient] 'webServerHost' was not set.");
		_hostname = hostname.trim();
		assertFalse(isNullOrEmpty(portnum), "[WebSocketCommonClient] 'webServerPort' was not set.");
		_port = Integer.parseInt(portnum.trim());
		assertFalse(isNullOrEmpty(wswait), "[WebSocketCommonClient] 'ws_wait' must be set in the properties");
		_ws_wait = Integer.parseInt(wswait.trim());
		assertTrue(_ws_wait > 0, "[WebSocketCommonClient] 'ws_wait' (in seconds) must be set greater than 0");

		logger.log(Logger.Level.DEBUG, "[WebSocketCommonClient] Test setup OK");
		TEST_PROPS = new Hashtable<>();
	}

	// ///////////////////////////// Utility methods
	// //////////////////////////////

	protected void assertCountDownLatchCount() throws Exception {
		long hits = testCase.getCountDownLatchRemainingHits();
		int count = testCase.getCountDownLatchTotalCount();
		WebSocketCommonClient.assertEqualsLong(0, hits, "The countDownLatch has been hit only", count - hits,
				"was expected", count, "times");
		logTrace("[WebSocketCommonClient] CountDownLatch has been hit", count, "times as expected");
	}

	/**
	 * Asserts that a condition is true.
	 * 
	 * @param condition tested condition
	 * @param message   a space separated message[i].toString() compilation for
	 *                  i=<0,message.length)
	 * @throws Exception when conditionTrue is not met with message provided
	 */
	public static void //
			assertTrue(boolean condition, Object... message) throws Exception {
		if (!condition)
			throw new Exception(objectsToString(message));
	}

	/**
	 * Asserts that a condition is false.
	 * 
	 * @param condition tested condition
	 * @param message   a space separated message[i].toString() compilation for
	 *                  i=<0,message.length)
	 * @throws Exception when condition is not false with message provided
	 */
	public static void //
			assertFalse(boolean condition, Object... message) throws Exception {
		assertTrue(!condition, message);
	}

	/**
	 * Asserts that two objects are equal. When instances of Comparable, such as
	 * String, compareTo is used.
	 * 
	 * @param first   first object
	 * @param second  second object
	 * @param message a space separated message[i].toString() compilation for
	 *                i=<0,message.length)
	 * @throws Exception when objects are not equal with message provided
	 */
	@SuppressWarnings("unchecked")
	public static <T> void //
			assertEquals(T first, T second, Object... message) throws Exception {
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
			assertEqualsInt(int first, int second, Object... message) throws Exception {
		assertTrue(first == second, message);
	}

	public static <T> void //
			assertEqualsLong(long first, long second, Object... message) throws Exception {
		assertTrue(first == second, message);
	}

	public static <T> void //
			assertEqualsBool(boolean first, boolean second, Object... message) throws Exception {
		assertTrue(first == second, message);
	}

	/**
	 * Asserts that an object is null.
	 * 
	 * @param object  Assert that object is not null
	 * @param message a space separated message[i].toString() compilation for
	 *                i=<0,message.length)
	 * @throws Exception when condition is not met with message provided
	 */
	public static void //
			assertNull(Object object, Object... message) throws Exception {
		assertTrue(object == null, message);
	}

	/**
	 * Asserts that an object is not null.
	 * 
	 * @param object  Assert that object is not null
	 * @param message a space separated message[i].toString() compilation for
	 *                i=<0,message.length)
	 * @throws Exception when condition is not met with message provided
	 */
	public static void //
			assertNotNull(Object object, Object... message) throws Exception {
		assertTrue(object != null, message);
	}

	/**
	 * Assert that given substring is a substring of given string
	 * 
	 * @param string    the string to search substring in
	 * @param substring the substring to be searched in a given string
	 * @param message   space separated message values to be thrown
	 * @throws Exception throws
	 */
	public static void assertContains(String string, String substring, Object... message) throws Exception {
		assertTrue(string.contains(substring), message);
	}

	/**
	 * Assert that given substring is a substring of given string, case insensitive
	 * 
	 * @param string    the string to search substring in
	 * @param substring the substring to be searched in a given string
	 * @param message   space separated message values to be thrown
	 * @throws Exception
	 */
	public static void assertContainsIgnoreCase(String string, String substring, Object... message) throws Exception {
		assertTrue(string.toLowerCase().contains(substring.toLowerCase()), message);
	}

	/**
	 * Assert that given substring is not a substring of given string
	 * 
	 * @param string    the string to search substring in
	 * @param substring the substring to be searched in a given string
	 * @param message   space separated message values to be thrown
	 * @throws Exception throws
	 */
	public static void assertNotContains(String string, String substring, Object... message) throws Exception {
		assertFalse(string.contains(substring), message);
	}

	/**
	 * Assert that given substring is not a substring of given string, case
	 * insensitive
	 * 
	 * @param string    the string to search substring in
	 * @param substring the substring to be searched in a given string
	 * @param message   space separated message values to be thrown
	 * @throws Exception
	 */
	public static void assertNotContainsIgnoreCase(String string, String substring, Object... message)
			throws Exception {
		assertFalse(string.toLowerCase().contains(substring.toLowerCase()), message);
	}

	/**
	 * Assert that given subtext.toString() subject is a substring of given text
	 * 
	 * @param text    the text.toString() object to search subtext.toString() in
	 * @param subtext the subtext.toString() to be searched in a given
	 *                text.toString()
	 * @param message space separated message values to be thrown
	 * @throws Exception
	 */
	public static <T> void assertContains(T text, T subtext, Object... message) throws Exception {
		assertContains(text.toString(), subtext.toString(), message);
	}

	/**
	 * Assert that given subtext.toString() subject is a substring of given text,
	 * case insensitive
	 * 
	 * @param text    the text.toString() object to search subtext.toString() in
	 * @param subtext the subtext.toString() to be searched in a given
	 *                text.toString()
	 * @param message space separated message values to be thrown
	 * @throws Exception
	 */
	public static <T> void assertContainsIgnoreCase(T text, T subtext, Object... message) throws Exception {
		assertContainsIgnoreCase(text.toString(), subtext.toString(), message);
	}

	public static//
	String assertProperty(Properties p, String propertyName) throws Exception {
		String value = System.getProperty(propertyName);
		logTrace(propertyName, value);
		assertTrue(!isNullOrEmpty(value), propertyName, "was not set in build.properties");
		return value;
	}

	/**
	 * Searches an encapsulated exception cause in parent exception
	 */
	protected static <T extends Throwable> T assertCause(Throwable parent, Class<T> wrapped, Object... msg)
			throws Exception {
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
		logger.log(Logger.Level.INFO, objectsToString(msg));
	}

	public static void logTrace(Object... msg) {
		logger.log(Logger.Level.TRACE, objectsToString(msg));
	}

	/**
	 * @param objects to be put in a sentence
	 * @return objects in a single string , each object separated by " "
	 */
	protected static String objectsToString(Object... objects) {
		return objectsToStringWithDelimiter(" ", objects);
	}

	protected static String objectsToStringWithDelimiter(String delimiter, Object... objects) {
		StringBuilder sb = new StringBuilder();
		if (objects != null)
			for (Object o : objects) {
				if (o != null && o.getClass().isArray() && o.toString().startsWith("[L"))
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
	 * 	public void beforeRequest(Map&lt;String, List&lt;String&gt;&gt; headers) {
	 * 		headers.putAll(basicAuthenticationAsHeaderMap(USER, PASSWORD));
	 * 	};
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
	Map<String, List<String>> basicAuthenticationAsHeaderMap(String userName, String password) {
		Map<String, List<String>> headers = new HashMap<>();
		String toEncode = new StringBuilder().append(userName).append(':').append(password).toString();
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
	 * @param clientContainer The container used for e.g.
	 *                        {@link WebSocketContainer#connectToServer(Class, java.net.URI)}
	 *                        </p>
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
	protected void setClientEndpoint(Class<? extends ClientEndpoint<?>> endpoint) {
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
	 * The annotated client endpoint alternative to ClientEndpoint instance. It has
	 * precedence over ClientEndpoint class set by {@link #setClientEndpoint}
	 */
	protected void setAnnotatedClientEndpoint(AnnotatedClientEndpoint<?> annotatedEndpoint) {
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
	 * This method sets the main {@link EndpointCallback} to be used by a test. If a
	 * callback is to be used in conjunction with default
	 * {@link SendMessageCallback}, use {@link #addClientCallback(EndpointCallback)}
	 * method.
	 * </p>
	 * Note that when no entity is sent to a server, no clientCallback is used.
	 */
	protected void setClientCallback(EndpointCallback callback) {
		getTestCase().setCallback(callback);
	}

	/**
	 * Add additional callback functionality to a main callback. The main callback
	 * could either be set by {@link #setClientCallback(EndpointCallback)} method or
	 * it is the default {@link SendMessageCallback} client callback.
	 * </p>
	 * Note that when no entity is sent to a server, no clientCallback is used.
	 */
	protected void addClientCallback(EndpointCallback callback) {
		getTestCase().addClientCallback(callback);
	}

	protected Session getSession() throws Exception {
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

	public static String toString(InputStream inStream) throws IOException {
		try (BufferedReader bufReader = new BufferedReader(new InputStreamReader(inStream, StandardCharsets.UTF_8))) {
			return bufReader.lines().collect(Collectors.joining(System.lineSeparator()));
		}
	}

}
