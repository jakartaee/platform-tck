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

package com.sun.ts.tests.saaj.api.jakarta_xml_soap.SOAPMessage;

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

	private static final String TESTSERVLET = "/SOAPMessage_web/SOAPMessageTestServlet";

	private static final Logger logger = (Logger) System.getLogger(URLClient.class.getName());

	@Deployment(testable = false)
	public static WebArchive createDeployment() throws IOException {
		WebArchive archive = ShrinkWrap.create(WebArchive.class, "SOAPMessage_web.war");
		archive.addPackages(false, Filters.exclude(URLClient.class),
				"com.sun.ts.tests.saaj.api.jakarta_xml_soap.SOAPMessage");
		archive.addPackages(false, "com.sun.ts.tests.saaj.common");
		final String CONTENT_ROOT = URLClient.class.getPackageName().replace(".", "/") + "/contentRoot/";
		String[] attachement = { "attach.gif", "attach.html", "attach.jpeg", "attach.txt", "attach.xml" };
		addFilesToArchive(CONTENT_ROOT, attachement, archive);
		archive.addAsWebInfResource(URLClient.class.getPackage(), "standalone.web.xml", "web.xml");
		return archive;
	};

	/*
	 * @testName: addAttachmentPartTest
	 *
	 * @assertion_ids: SAAJ:JAVADOC:34;
	 *
	 * @test_Strategy: Call SOAPMessage.addAttachmentPart(AttachmentPart) and verify
	 * attachment part was added.
	 *
	 * Description: add an attachment object to the message
	 *
	 */
	@Test
	public void addAttachmentPartTest() throws Exception {
		boolean pass = true;
		try {
			logger.log(Logger.Level.INFO, "addAttachmentPartTest: add an attachmentpart to a SOAPMessage");
			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "addAttachmentPartTest");
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
			throw new Exception("addAttachmentPartTest failed", e);
		}

		if (!pass)
			throw new Exception("addAttachmentPartTest failed");
	}

	/*
	 * @testName: countAttachmentsTest
	 *
	 * @assertion_ids: SAAJ:JAVADOC:29;
	 *
	 * @test_Strategy: Call SOAPMessage.countAttachments() and verify correct count
	 * of attachments added.
	 *
	 * Description: count number of attachments in the message
	 *
	 */
	@Test
	public void countAttachmentsTest() throws Exception {
		boolean pass = true;
		try {
			logger.log(Logger.Level.INFO, "countAttachmentsTest: count number of attachments in SOAPMessage");
			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "countAttachmentsTest");
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
			throw new Exception("countAttachmentsTest failed", e);
		}

		if (!pass)
			throw new Exception("countAttachmentsTest failed");
	}

	/*
	 * @testName: getAttachmentsTest1
	 *
	 * @assertion_ids: SAAJ:JAVADOC:30;
	 *
	 * @test_Strategy: Call SOAPMessage.getAttachments() and verify correct get of
	 * attachments.
	 *
	 * Description: get number of attachments in the message
	 *
	 */
	@Test
	public void getAttachmentsTest1() throws Exception {
		boolean pass = true;
		try {
			logger.log(Logger.Level.INFO, "getAttachmentsTest1: get number of attachments in SOAPMessage");
			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "getAttachmentsTest1");
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
			throw new Exception("getAttachmentsTest1 failed", e);
		}

		if (!pass)
			throw new Exception("getAttachmentsTest1 failed");
	}

	/*
	 * @testName: getAttachmentsTest2
	 *
	 * @assertion_ids: SAAJ:JAVADOC:31;
	 *
	 * @test_Strategy: Call SOAPMessage.getAttachments(MimeHeaders) and verify
	 * correct get of attachments.
	 *
	 * Description: get number of attachments in the message
	 *
	 */
	@Test
	public void getAttachmentsTest2() throws Exception {
		boolean pass = true;
		try {
			logger.log(Logger.Level.INFO, "getAttachmentsTest2: get number of attachments in SOAPMessage");
			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "getAttachmentsTest2");
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
			throw new Exception("getAttachmentsTest2 failed", e);
		}

		if (!pass)
			throw new Exception("getAttachmentsTest2 failed");
	}

	/*
	 * @testName: removeAllAttachmentsTest
	 *
	 * @assertion_ids: SAAJ:JAVADOC:28;
	 *
	 * @test_Strategy: Call SOAPMessage.removeAllAttachments() and verify correct
	 * remove of attachments.
	 *
	 * Description: remove attachments in the message
	 *
	 */
	@Test
	public void removeAllAttachmentsTest() throws Exception {
		boolean pass = true;
		try {
			logger.log(Logger.Level.INFO, "removeAllAttachmentsTest: remove attachments in SOAPMessage");
			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "removeAllAttachmentsTest");
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
			throw new Exception("removeAllAttachmentsTest failed", e);
		}

		if (!pass)
			throw new Exception("removeAllAttachmentsTest failed");
	}

	/*
	 * @testName: removeAttachmentsTest
	 *
	 * @assertion_ids: SAAJ:JAVADOC:28;
	 *
	 * @test_Strategy: Call SOAPMessage.removeAttachments() and verify correct
	 * attachments were removed.
	 *
	 * Description: remove attachments in the message
	 *
	 */
	@Test
	public void removeAttachmentsTest() throws Exception {
		boolean pass = true;
		try {
			logger.log(Logger.Level.INFO, "removeAttachmentsTest: remove attachments in SOAPMessage");
			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "removeAttachmentsTest");
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
			throw new Exception("removeAttachmentsTest failed", e);
		}

		if (!pass)
			throw new Exception("removeAttachmentsTest failed");
	}

	/*
	 * @testName: setContentDescriptionTest
	 *
	 * @assertion_ids: SAAJ:JAVADOC:21;
	 *
	 * @test_Strategy: Call SOAPMessage.setContentDecription(String) and verify
	 * correct setting of description.
	 *
	 * Description: Set the description of this SOAPMessage object's content with
	 * the given description.
	 *
	 */
	@Test
	public void setContentDescriptionTest() throws Exception {
		boolean pass = true;
		try {
			logger.log(Logger.Level.INFO, "setContentDescriptionTest: set content description of SOAPMessage");
			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "setContentDescriptionTest");
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
			throw new Exception("setContentDescriptionTest failed", e);
		}

		if (!pass)
			throw new Exception("setContentDescriptionTest failed");
	}

	/*
	 * @testName: getContentDescriptionTest
	 *
	 * @assertion_ids: SAAJ:JAVADOC:22;
	 *
	 * @test_Strategy: Call SOAPMessage.getContentDecription() and verify correct
	 * description is returned.
	 *
	 * Description: Get the description of this SOAPMessage object's content.
	 *
	 */
	@Test
	public void getContentDescriptionTest() throws Exception {
		boolean pass = true;
		try {
			logger.log(Logger.Level.INFO, "getContentDescriptionTest: get content description of SOAPMessage");
			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "getContentDescriptionTest");
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
			throw new Exception("getContentDescriptionTest failed", e);
		}

		if (!pass)
			throw new Exception("getContentDescriptionTest failed");
	}

	/*
	 * @testName: createAttachmentPartTest1
	 *
	 * @assertion_ids: SAAJ:JAVADOC:35;
	 *
	 * @test_Strategy: Call SOAPMessage.createAttachmentPart() and verify an empty
	 * AttachmentPart object is returned.
	 *
	 * Description: Create a new empty AttachmentPart object
	 *
	 */
	@Test
	public void createAttachmentPartTest1() throws Exception {
		boolean pass = true;
		try {
			logger.log(Logger.Level.INFO, "createAttachmentPartTest1: create empty attachment part");
			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "createAttachmentPartTest1");
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
			throw new Exception("createAttachmentPartTest1 failed", e);
		}

		if (!pass)
			throw new Exception("createAttachmentPartTest1 failed");
	}

	/*
	 * @testName: createAttachmentPartTest2
	 *
	 * @assertion_ids: SAAJ:JAVADOC:36;
	 *
	 * @test_Strategy: Call SOAPMessage.createAttachmentPart(DataHandler) and verify
	 * a non-empty AttachmentPart object is returned.
	 *
	 * Description: Create an AttachmentPart object and populate it with a given
	 * DataHandler object.
	 *
	 */
	@Test
	public void createAttachmentPartTest2() throws Exception {
		boolean pass = true;
		try {
			logger.log(Logger.Level.INFO,
					"createAttachmentPartTest2: create attachment part and populate with given DataHandler");
			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "createAttachmentPartTest2");
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
			throw new Exception("createAttachmentPartTest2 failed", e);
		}

		if (!pass)
			throw new Exception("createAttachmentPartTest2 failed");
	}

	/*
	 * @testName: createAttachmentPartTest3
	 *
	 * @assertion_ids: SAAJ:JAVADOC:38;
	 *
	 * @test_Strategy: Call SOAPMessage.createAttachmentPart(Object,String) and
	 * verify non-empty AttachmentPart object is returned.
	 *
	 * Description: Create an AttachmentPart object and populate it with the
	 * specified data of the specified content-type.
	 *
	 */
	@Test
	public void createAttachmentPartTest3() throws Exception {
		boolean pass = true;
		try {
			logger.log(Logger.Level.INFO,
					"createAttachmentPartTest3: create attachment part and populate wth given object and content type");
			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "createAttachmentPartTest3");
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
			throw new Exception("createAttachmentPartTest3 failed", e);
		}

		if (!pass)
			throw new Exception("createAttachmentPartTest3 failed");
	}

	/*
	 * @testName: writeToTest1
	 *
	 * @assertion_ids: SAAJ:JAVADOC:42; SAAJ:JAVADOC:43; SAAJ:JAVADOC:44;
	 *
	 * @test_Strategy: Call SOAPMessage.writeTo(OutputStream) and verify SOAPMessage
	 * was written to stream.
	 *
	 * Description: Write a SOAPMessage without attachments to output stream
	 *
	 */
	@Test
	public void writeToTest1() throws Exception {
		boolean pass = true;
		try {
			logger.log(Logger.Level.INFO, "writeToTest1: write a SOAPMessage without attachments to OutputStream");
			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "writeToTest1");
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
			throw new Exception("writeToTest1 failed", e);
		}

		if (!pass)
			throw new Exception("writeToTest1 failed");
	}

	/*
	 * @testName: writeToTest2
	 *
	 * @assertion_ids: SAAJ:JAVADOC:42; SAAJ:JAVADOC:43; SAAJ:JAVADOC:44;
	 *
	 * @test_Strategy: Call SOAPMessage.writeTo(OutputStream) and verify SOAPMessage
	 * was written to stream.
	 *
	 * Description: Write a SOAPMessage with attachments to output stream
	 *
	 */
	@Test
	public void writeToTest2() throws Exception {
		boolean pass = true;
		try {
			logger.log(Logger.Level.INFO, "writeToTest2: write SOAPMessage with attachments to OutputStream");
			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "writeToTest2");
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
			throw new Exception("writeToTest2 failed", e);
		}

		if (!pass)
			throw new Exception("writeToTest2 failed");
	}

	/*
	 * @testName: saveRequiredTest1
	 *
	 * @assertion_ids: SAAJ:JAVADOC:41;
	 *
	 * @test_Strategy: Call SOAPMessage.saveRequired() and verify false is returned
	 * since saveChanges has not been called.
	 *
	 * Description: Test SOAPMessage object to see that saveChanges has not been
	 * called on it.
	 *
	 */
	@Test
	public void saveRequiredTest1() throws Exception {
		boolean pass = true;
		try {
			logger.log(Logger.Level.INFO, "saveRequiredTest1: verify SOAPMessage.saveRequired() is false");
			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "saveRequiredTest1");
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
			throw new Exception("saveRequiredTest1 failed", e);
		}

		if (!pass)
			throw new Exception("saveRequiredTest1 failed");
	}

	/*
	 * @testName: saveRequiredTest2
	 *
	 * @assertion_ids: SAAJ:JAVADOC:41; SAAJ:JAVADOC:40;
	 *
	 * @test_Strategy: Call SOAPMessage.saveRequired() after calling
	 * SOAPMessage.saveChanges() and verify true is returned.
	 *
	 * Description: Test SOAPMessage object to see that saveChanges has been called
	 * on it.
	 *
	 */
	@Test
	public void saveRequiredTest2() throws Exception {
		boolean pass = true;
		try {
			logger.log(Logger.Level.INFO, "saveRequiredTest2: verify SOAPMessage.saveRequired() is true");
			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "saveRequiredTest2");
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
			throw new Exception("saveRequiredTest2 failed", e);
		}

		if (!pass)
			throw new Exception("saveRequiredTest2 failed");
	}

	/*
	 * @testName: getMimeHeadersTest
	 *
	 * @assertion_ids: SAAJ:JAVADOC:37;
	 *
	 * @test_Strategy: Call SOAPMessage.getMimeHeaders() to return the
	 * transport-specific MimeHeaders for this SOAPMessage.
	 *
	 * Description: Test get of transport-specific MimeHeaders for this SOAPMessage.
	 *
	 */
	@Test
	public void getMimeHeadersTest() throws Exception {
		boolean pass = true;
		try {
			logger.log(Logger.Level.INFO, "getMimeHeadersTest: verify SOAPMessage.saveRequired() is true");
			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "getMimeHeadersTest");
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
			throw new Exception("getMimeHeadersTest failed", e);
		}

		if (!pass)
			throw new Exception("getMimeHeadersTest failed");
	}

	/*
	 * @testName: getSOAPPartTest
	 *
	 * @assertion_ids: SAAJ:JAVADOC:23;
	 *
	 * @test_Strategy: Call SOAPMessage.getSOAPPart() method to retrieve the
	 * SOAPPart of the message.
	 *
	 * Description: Get the SOAPPart for this soap message.
	 *
	 */
	@Test
	public void getSOAPPartTest() throws Exception {
		boolean pass = true;
		try {
			logger.log(Logger.Level.INFO, "getSOAPPartTest");

			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "getSOAPPartTest");
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
			throw new Exception("getSOAPPartTest failed", e);
		}

		if (!pass)
			throw new Exception("getSOAPPartTest failed");
	}

	/*
	 * @testName: setPropertyTest
	 *
	 * @assertion_ids: SAAJ:JAVADOC:45; SAAJ:JAVADOC:46;
	 *
	 * @test_Strategy: Call SOAPMessage.setProperty(String, String) method and
	 * verify that the property was set correctly.
	 *
	 * Description: Set a property for this soap message.
	 *
	 */
	@Test
	public void setPropertyTest() throws Exception {
		boolean pass = true;
		try {
			logger.log(Logger.Level.INFO, "setPropertyTest");

			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "setPropertyTest");
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
			throw new Exception("setPropertyTest failed", e);
		}

		if (!pass)
			throw new Exception("setPropertyTest failed");
	}

	/*
	 * @testName: getPropertyTest
	 *
	 * @assertion_ids: SAAJ:JAVADOC:47; SAAJ:JAVADOC:48;
	 *
	 * @test_Strategy: Call SOAPMessage.getProperty(String) method and verify that
	 * the correct property was retrieved.
	 *
	 * Description: Set a property for this soap message.
	 *
	 */
	@Test
	public void getPropertyTest() throws Exception {
		boolean pass = true;
		try {
			logger.log(Logger.Level.INFO, "getPropertyTest");

			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "getPropertyTest");
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
			throw new Exception("getPropertyTest failed", e);
		}

		if (!pass)
			throw new Exception("getPropertyTest failed");
	}

	/*
	 * @testName: getSOAPBodyTest
	 *
	 * @assertion_ids: SAAJ:JAVADOC:24; SAAJ:JAVADOC:25;
	 *
	 * @test_Strategy: Call SOAPMessage.getSOAPBody() method to retrieve the
	 * SOAPBody of the message.
	 *
	 * Description: Get the SOAPBody for this soap message.
	 *
	 */
	@Test
	public void getSOAPBodyTest() throws Exception {
		boolean pass = true;
		try {
			logger.log(Logger.Level.INFO, "getSOAPBodyTest");

			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "getSOAPBodyTest");
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
			throw new Exception("getSOAPBodyTest failed", e);
		}

		if (!pass)
			throw new Exception("getSOAPBodyTest failed");
	}

	/*
	 * @testName: getSOAPHeaderTest
	 *
	 * @assertion_ids: SAAJ:JAVADOC:26; SAAJ:JAVADOC:27;
	 *
	 * @test_Strategy: Call SOAPMessage.getSOAPHeader() method to retrieve the
	 * SOAPHeader of the message.
	 *
	 * Description: Get the SOAPHeader for this soap message.
	 *
	 */
	@Test
	public void getSOAPHeaderTest() throws Exception {
		boolean pass = true;
		try {
			logger.log(Logger.Level.INFO, "getSOAPHeaderTest");

			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "getSOAPHeaderTest");
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
			throw new Exception("getSOAPHeaderTest failed", e);
		}

		if (!pass)
			throw new Exception("getSOAPHeaderTest failed");
	}

	/*
	 * @testName: getAttachmentBySwaRefTest1
	 *
	 * @assertion_ids: SAAJ:JAVADOC:32; SAAJ:JAVADOC:33;
	 *
	 * @test_Strategy: Call SOAPMessage.getAttachment() method to retrieve an
	 * attachment from the message. Retrieve attachments using SwaRef "cid:uri"
	 * scheme. Create a SOAP message with a Body element and a text node of SwaRef
	 * "cid:uri".
	 *
	 */
	@Test
	public void getAttachmentBySwaRefTest1() throws Exception {
		boolean pass = true;
		try {
			logger.log(Logger.Level.INFO, "getAttachmentBySwaRefTest1");

			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "getAttachmentBySwaRefTest1");
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
			throw new Exception("getAttachmentBySwaRefTest1 failed", e);
		}

		if (!pass)
			throw new Exception("getAttachmentBySwaRefTest1 failed");
	}

	/*
	 * @testName: getAttachmentBySwaRefTest2
	 *
	 * @assertion_ids: SAAJ:JAVADOC:32; SAAJ:JAVADOC:33;
	 *
	 * @test_Strategy: Call SOAPMessage.getAttachment() method to retrieve an
	 * attachment from the message. Retrieve attachments using SwaRef "cid:uri"
	 * scheme. Create a SOAP message with a Body element and a Child element with a
	 * text node of SwaRef "cid:uri".
	 *
	 */
	@Test
	public void getAttachmentBySwaRefTest2() throws Exception {
		boolean pass = true;
		try {
			logger.log(Logger.Level.INFO, "getAttachmentBySwaRefTest2");

			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "getAttachmentBySwaRefTest2");
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
			throw new Exception("getAttachmentBySwaRefTest2 failed", e);
		}

		if (!pass)
			throw new Exception("getAttachmentBySwaRefTest2 failed");
	}

	/*
	 * @testName: getAttachmentBySwaRefTest3
	 *
	 * @assertion_ids: SAAJ:JAVADOC:32; SAAJ:JAVADOC:33;
	 *
	 * @test_Strategy: Call SOAPMessage.getAttachment() method to retrieve an
	 * attachment from the message. Retrieve attachments using SwaRef "uri" scheme.
	 * Create a SOAP message with a Body element and a text node of SwaRef "uri".
	 * This uses URI and Content-Location.
	 *
	 */
	@Test
	public void getAttachmentBySwaRefTest3() throws Exception {
		boolean pass = true;
		try {
			logger.log(Logger.Level.INFO, "getAttachmentBySwaRefTest3");

			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "getAttachmentBySwaRefTest3");
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
			throw new Exception("getAttachmentBySwaRefTest3 failed", e);
		}

		if (!pass)
			throw new Exception("getAttachmentBySwaRefTest3 failed");
	}

	/*
	 * @testName: getAttachmentByHrefTest1
	 *
	 * @assertion_ids: SAAJ:JAVADOC:32; SAAJ:JAVADOC:33;
	 *
	 * @test_Strategy: Call SOAPMessage.getAttachment() method to retrieve an
	 * attachment from the message. Retrieve attachments using "href=cid:uri"
	 * attribute scheme. This deals with using the Content-ID and URI setting to
	 * retrieve the attachment.
	 *
	 */
	@Test
	public void getAttachmentByHrefTest1() throws Exception {
		boolean pass = true;
		try {
			logger.log(Logger.Level.INFO, "getAttachmentByHrefTest1");

			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "getAttachmentByHrefTest1");
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
			throw new Exception("getAttachmentByHrefTest1 failed", e);
		}

		if (!pass)
			throw new Exception("getAttachmentByHrefTest1 failed");
	}

	/*
	 * @testName: getAttachmentByHrefTest2
	 *
	 * @assertion_ids: SAAJ:JAVADOC:32; SAAJ:JAVADOC:33;
	 *
	 * @test_Strategy: Call SOAPMessage.getAttachment() method to retrieve an
	 * attachment from the message. Retrieve attachments using "href=uri" attribute
	 * scheme. This deals with using the Content-Location and URI setting to
	 * retrieve the attachment.
	 */
	@Test
	public void getAttachmentByHrefTest2() throws Exception {
		boolean pass = true;
		try {
			logger.log(Logger.Level.INFO, "getAttachmentByHrefTest2");

			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "getAttachmentByHrefTest2");
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
			throw new Exception("getAttachmentByHrefTest2 failed", e);
		}

		if (!pass)
			throw new Exception("getAttachmentByHrefTest2 failed");
	}
}
