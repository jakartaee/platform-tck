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

/*
 * $Id$
 */

package com.sun.ts.tests.saaj.api.jakarta_xml_soap.SOAPHeaderElement;

import java.io.IOException;
import java.lang.System.Logger;
import java.util.Properties;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.Filters;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.Test;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.saaj.common.Client;

public class URLClient extends Client {

	private static final String SOAPHEADERELEMENT_TESTSERVLET = "/SOAPHeaderElement_web/SOAPHeaderElementTestServlet";

	private static final Logger logger = (Logger) System.getLogger(URLClient.class.getName());

	@Deployment(testable = false)
	public static WebArchive createDeployment() throws IOException {
		WebArchive archive = ShrinkWrap.create(WebArchive.class, "SOAPHeaderElement_web.war");
		archive.addPackages(false, Filters.exclude(URLClient.class),
				"com.sun.ts.tests.saaj.api.jakarta_xml_soap.SOAPHeaderElement");
		archive.addPackages(false, "com.sun.ts.tests.saaj.common");
		archive.addAsWebInfResource(URLClient.class.getPackage(), "standalone.web.xml", "web.xml");
		return archive;
	};

	/*
	 * @testName: setMustUnderstandTrueTest
	 *
	 * @assertion_ids: SAAJ:JAVADOC:162;
	 *
	 * @test_Strategy: Call SOAPHeaderElement.setMustUnderstand(true) method and
	 * verify mustunderstand attribute is set to true
	 *
	 * Description: Set the mustunderstand attribute associated with the soap header
	 * element to true
	 *
	 */
	@Test
	public void setMustUnderstandTrueTest() throws Exception {
		boolean pass = true;
		try {
			logger.log(Logger.Level.INFO, "setMustUnderstandTrueTest");

			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPHEADERELEMENT_TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "setMustUnderstandTrueTest");
				if (i == 0)
					props.setProperty("SOAPVERSION", "soap11");
				else
					props.setProperty("SOAPVERSION", "soap12");
				urlConn = TestUtil.sendPostData(props, url);
				logger.log(Logger.Level.INFO, "Getting response from test servlet.....");
				Properties resProps = TestUtil.getResponseProperties(urlConn);
				if (!resProps.getProperty("TESTRESULT").equals("pass"))
					pass = false;
			}

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("setMustUnderstandTrueTest failed", e);
		}

		if (!pass)
			throw new Exception("setMustUnderstandTrueTest failed");
	}

	/*
	 * @testName: setMustUnderstandFalseTest
	 *
	 * @assertion_ids: SAAJ:JAVADOC:162;
	 *
	 * @test_Strategy: Call SOAPHeaderElement.setMustUnderstand(false) method and
	 * verify mustunderstand attribute is set to false
	 *
	 * Description: Set the mustunderstand attribute associated with the soap header
	 * element to false
	 *
	 */
	@Test
	public void setMustUnderstandFalseTest() throws Exception {
		boolean pass = true;
		try {
			logger.log(Logger.Level.INFO, "setMustUnderstandFalseTest");

			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPHEADERELEMENT_TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "setMustUnderstandFalseTest");
				if (i == 0)
					props.setProperty("SOAPVERSION", "soap11");
				else
					props.setProperty("SOAPVERSION", "soap12");
				urlConn = TestUtil.sendPostData(props, url);
				logger.log(Logger.Level.INFO, "Getting response from test servlet.....");
				Properties resProps = TestUtil.getResponseProperties(urlConn);
				if (!resProps.getProperty("TESTRESULT").equals("pass"))
					pass = false;
			}

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("setMustUnderstandFalseTest failed", e);
		}

		if (!pass)
			throw new Exception("setMustUnderstandFalseTest failed");
	}

	/*
	 * @testName: getMustUnderstandTrueTest
	 *
	 * @assertion_ids: SAAJ:JAVADOC:163;
	 *
	 * @test_Strategy: Call SOAPHeaderElement.getMustUnderstand() method and verify
	 * mustunderstand attribute is set to true
	 *
	 * Description: Get the mustunderstand attribute associated with the soap header
	 * element when it is true
	 *
	 */
	@Test
	public void getMustUnderstandTrueTest() throws Exception {
		boolean pass = true;
		try {
			logger.log(Logger.Level.INFO, "getMustUnderstandTrueTest");

			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPHEADERELEMENT_TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "getMustUnderstandTrueTest");
				if (i == 0)
					props.setProperty("SOAPVERSION", "soap11");
				else
					props.setProperty("SOAPVERSION", "soap12");
				urlConn = TestUtil.sendPostData(props, url);
				logger.log(Logger.Level.INFO, "Getting response from test servlet.....");
				Properties resProps = TestUtil.getResponseProperties(urlConn);
				if (!resProps.getProperty("TESTRESULT").equals("pass"))
					pass = false;
			}

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("getMustUnderstandTrueTest failed", e);
		}

		if (!pass)
			throw new Exception("getMustUnderstandTrueTest failed");
	}

	/*
	 * @testName: getMustUnderstandFalseTest
	 *
	 * @assertion_ids: SAAJ:JAVADOC:163;
	 *
	 * @test_Strategy: Call SOAPHeaderElement.getMustUnderstand() method and verify
	 * mustunderstand attribute is set to false
	 *
	 * Description: Get the mustunderstand attribute associated with the soap header
	 * element when it is false
	 *
	 */
	@Test
	public void getMustUnderstandFalseTest() throws Exception {
		boolean pass = true;
		try {
			logger.log(Logger.Level.INFO, "getMustUnderstandFalseTest");

			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPHEADERELEMENT_TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "getMustUnderstandFalseTest");
				if (i == 0)
					props.setProperty("SOAPVERSION", "soap11");
				else
					props.setProperty("SOAPVERSION", "soap12");
				urlConn = TestUtil.sendPostData(props, url);
				logger.log(Logger.Level.INFO, "Getting response from test servlet.....");
				Properties resProps = TestUtil.getResponseProperties(urlConn);
				if (!resProps.getProperty("TESTRESULT").equals("pass"))
					pass = false;
			}

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("getMustUnderstandFalseTest failed", e);
		}

		if (!pass)
			throw new Exception("getMustUnderstandFalseTest failed");
	}

	/*
	 * @testName: setActorTest
	 *
	 * @assertion_ids: SAAJ:JAVADOC:157;
	 *
	 * @test_Strategy: Call SOAPHeaderElement.setActor(actor) method and verify
	 * actor associated is set properly.
	 *
	 * Description: Set the actor associated with the soap header element. For a
	 * SOAP1.1 or SOAP1.2 message this should succeed.
	 *
	 */
	@Test
	public void setActorTest() throws Exception {
		boolean pass = true;
		try {
			logger.log(Logger.Level.INFO, "setActorTest");

			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPHEADERELEMENT_TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				if (i == 0) {
					props.setProperty("TESTNAME", "setActorTest");
					props.setProperty("SOAPVERSION", "soap11");
				} else {
					props.setProperty("TESTNAME", "setActorTest");
					props.setProperty("SOAPVERSION", "soap12");
				}
				urlConn = TestUtil.sendPostData(props, url);
				logger.log(Logger.Level.INFO, "Getting response from test servlet.....");
				Properties resProps = TestUtil.getResponseProperties(urlConn);
				if (!resProps.getProperty("TESTRESULT").equals("pass"))
					pass = false;
			}

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("setActorTest failed", e);
		}

		if (!pass)
			throw new Exception("setActorTest failed");
	}

	/*
	 * @testName: getActorTest
	 *
	 * @assertion_ids: SAAJ:JAVADOC:160;
	 *
	 * @test_Strategy: Call SOAPHeaderElement.getActor() method and verify actor
	 * associated is set properly.
	 *
	 * Description: Get the actor associated with the soap header element. For a
	 * SOAP1.1 or SOAP1.2 message this should succeed.
	 *
	 */
	@Test
	public void getActorTest() throws Exception {
		boolean pass = true;
		try {
			logger.log(Logger.Level.INFO, "getActorTest");

			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPHEADERELEMENT_TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				if (i == 0) {
					props.setProperty("TESTNAME", "getActorTest");
					props.setProperty("SOAPVERSION", "soap11");
				} else {
					props.setProperty("TESTNAME", "getActorTest");
					props.setProperty("SOAPVERSION", "soap12");
				}
				urlConn = TestUtil.sendPostData(props, url);
				logger.log(Logger.Level.INFO, "Getting response from test servlet.....");
				Properties resProps = TestUtil.getResponseProperties(urlConn);
				if (!resProps.getProperty("TESTRESULT").equals("pass"))
					pass = false;
			}

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("getActorTest failed", e);
		}

		if (!pass)
			throw new Exception("getActorTest failed");
	}

	/*
	 * @testName: setRoleTest
	 *
	 * @assertion_ids: SAAJ:JAVADOC:158; SAAJ:JAVADOC:159;
	 *
	 * @test_Strategy: Call SOAPHeaderElement.setRole(role) method and verify role
	 * associated is set properly.
	 *
	 * Description: Set the role associated with the soap header element. For a
	 * SOAP1.2 message this should succeed and for a SOAP1.1 message it must throw
	 * UnsupportedOperation- Exception.
	 *
	 */
	@Test
	public void setRoleTest() throws Exception {
		boolean pass = true;
		try {
			logger.log(Logger.Level.INFO, "setRoleTest");

			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPHEADERELEMENT_TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				if (i == 0) {
					props.setProperty("TESTNAME", "setRoleSOAP11Test");
					props.setProperty("SOAPVERSION", "soap11");
				} else {
					props.setProperty("TESTNAME", "setRoleSOAP12Test");
					props.setProperty("SOAPVERSION", "soap12");
				}
				urlConn = TestUtil.sendPostData(props, url);
				logger.log(Logger.Level.INFO, "Getting response from test servlet.....");
				Properties resProps = TestUtil.getResponseProperties(urlConn);
				if (!resProps.getProperty("TESTRESULT").equals("pass"))
					pass = false;
			}

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("setRoleTest failed", e);
		}

		if (!pass)
			throw new Exception("setRoleTest failed");
	}

	/*
	 * @testName: getRoleTest
	 *
	 * @assertion_ids: SAAJ:JAVADOC:161;
	 *
	 * @test_Strategy: Call SOAPHeaderElement.getRole() method and verify role
	 * associated is set properly.
	 *
	 * Description: Get the role associated with the soap header element. For a
	 * SOAP1.2 message this should succeed and for a SOAP1.1 message it must throw
	 * UnsupportedOperation- Exception.
	 *
	 */
	@Test
	public void getRoleTest() throws Exception {
		boolean pass = true;
		try {
			logger.log(Logger.Level.INFO, "getRoleTest");

			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPHEADERELEMENT_TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				if (i == 0) {
					props.setProperty("TESTNAME", "getRoleSOAP11Test");
					props.setProperty("SOAPVERSION", "soap11");
				} else {
					props.setProperty("TESTNAME", "getRoleSOAP12Test");
					props.setProperty("SOAPVERSION", "soap12");
				}
				urlConn = TestUtil.sendPostData(props, url);
				logger.log(Logger.Level.INFO, "Getting response from test servlet.....");
				Properties resProps = TestUtil.getResponseProperties(urlConn);
				if (!resProps.getProperty("TESTRESULT").equals("pass"))
					pass = false;
			}

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("getRoleTest failed", e);
		}

		if (!pass)
			throw new Exception("getRoleTest failed");
	}

	/*
	 * @testName: setRelayTest
	 *
	 * @assertion_ids: SAAJ:JAVADOC:164; SAAJ:JAVADOC:165;
	 *
	 * @test_Strategy: Call SOAPHeaderElement.setRelay(relay) method and verify
	 * relay attribute is set properly.
	 *
	 * Description: Set the relay attribute for this soap header element. For a
	 * SOAP1.2 message this should succeed and for a SOAP1.1 message it must throw
	 * UnsupportedOperation- Exception.
	 *
	 */
	@Test
	public void setRelayTest() throws Exception {
		boolean pass = true;
		try {
			logger.log(Logger.Level.INFO, "setRelayTest");

			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPHEADERELEMENT_TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				if (i == 0) {
					props.setProperty("TESTNAME", "setRelaySOAP11Test");
					props.setProperty("SOAPVERSION", "soap11");
				} else {
					props.setProperty("TESTNAME", "setRelaySOAP12Test");
					props.setProperty("SOAPVERSION", "soap12");
				}
				urlConn = TestUtil.sendPostData(props, url);
				logger.log(Logger.Level.INFO, "Getting response from test servlet.....");
				Properties resProps = TestUtil.getResponseProperties(urlConn);
				if (!resProps.getProperty("TESTRESULT").equals("pass"))
					pass = false;
			}

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("setRelayTest failed", e);
		}

		if (!pass)
			throw new Exception("setRelayTest failed");
	}

	/*
	 * @testName: getRelayTest
	 *
	 * @assertion_ids: SAAJ:JAVADOC:166;
	 *
	 * @test_Strategy: Call SOAPHeaderElement.getRelay() method and verify relay
	 * attribute is set properly.
	 *
	 * Description: Get the relay attribute for this soap header element. For a
	 * SOAP1.2 message this should succeed and for a SOAP1.1 message it must throw
	 * UnsupportedOperation- Exception.
	 *
	 */
	@Test
	public void getRelayTest() throws Exception {
		boolean pass = true;
		try {
			logger.log(Logger.Level.INFO, "getRelayTest");

			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPHEADERELEMENT_TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				if (i == 0) {
					props.setProperty("TESTNAME", "getRelaySOAP11Test");
					props.setProperty("SOAPVERSION", "soap11");
				} else {
					props.setProperty("TESTNAME", "getRelaySOAP12Test");
					props.setProperty("SOAPVERSION", "soap12");
				}
				urlConn = TestUtil.sendPostData(props, url);
				logger.log(Logger.Level.INFO, "Getting response from test servlet.....");
				Properties resProps = TestUtil.getResponseProperties(urlConn);
				if (!resProps.getProperty("TESTRESULT").equals("pass"))
					pass = false;
			}

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("getRelayTest failed", e);
		}

		if (!pass)
			throw new Exception("getRelayTest failed");
	}
}
