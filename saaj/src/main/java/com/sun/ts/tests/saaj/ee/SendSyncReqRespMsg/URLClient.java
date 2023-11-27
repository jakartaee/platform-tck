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

package com.sun.ts.tests.saaj.ee.SendSyncReqRespMsg;

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

	private static final String SERVLET = "/SendSyncReqRespMsg_web/SendingServlet";

	private static final Logger logger = (Logger) System.getLogger(URLClient.class.getName());

	@Deployment(testable = false)
	public static WebArchive createDeployment() throws IOException {
		WebArchive archive = ShrinkWrap.create(WebArchive.class, "SendSyncReqRespMsg_web.war");
		archive.addPackages(false, Filters.exclude(URLClient.class), "com.sun.ts.tests.saaj.ee.SendSyncReqRespMsg");
		archive.addPackages(false, "com.sun.ts.tests.saaj.common");
		final String CONTENT_ROOT = URLClient.class.getPackageName().replace(".", "/") + "/contentRoot/";
		String[] attachement = { "attach.jpeg", "attach.txt", "attach.xml" };
		addFilesToArchive(CONTENT_ROOT, attachement, archive);
		archive.addAsWebInfResource(URLClient.class.getPackage(), "standalone.web.xml", "web.xml");
		return archive;
	};

	/*
	 * @testName: SendSyncReqRespMsgSOAP11Test1
	 *
	 * @assertion_ids: SAAJ:SPEC:1; SAAJ:SPEC:2; SAAJ:SPEC:3; SAAJ:SPEC:4;
	 * SAAJ:SPEC:5; SAAJ:SPEC:6; SAAJ:SPEC:7; SAAJ:SPEC:8; SAAJ:SPEC:9;
	 * SAAJ:SPEC:10; SAAJ:SPEC:11; SAAJ:SPEC:12; SAAJ:SPEC:13; SAAJ:SPEC:14;
	 * SAAJ:SPEC:15; SAAJ:SPEC:16; SAAJ:SPEC:17; SAAJ:SPEC:18;
	 *
	 * @test_Strategy: Create a soap message containing a soap message part with no
	 * attachments and send it as a synchronous soap message. Sends a soap 1.1
	 * protocol message.
	 *
	 * Description: Send synchronous soap message with no attachments.
	 *
	 */
	@Test
	public void SendSyncReqRespMsgSOAP11Test1() throws Exception {
		try {
			boolean pass = true;

			logger.log(Logger.Level.INFO, "SendSyncReqRespMsgSOAP11Test1");
			logger.log(Logger.Level.INFO, "Send synchronous message" + " with no attachments");
			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, SERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
			props.setProperty("TESTNAME", "SendSyncReqRespMsgTest1");
			props.setProperty("SOAPVERSION", "soap11");
			urlConn = TestUtil.sendPostData(props, url);
			logger.log(Logger.Level.INFO, "Getting response from test servlet.....");
			Properties resProps = TestUtil.getResponseProperties(urlConn);
			if (!resProps.getProperty("TESTRESULT").equals("pass"))
				pass = false;

			if (!pass)
				throw new Exception("SendSyncReqRespMsgSOAP11Test1 failed");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("SendSyncReqRespMsgSOAP11Test1 failed", e);
		}
	}

	/*
	 * @testName: SendSyncReqRespMsgSOAP11Test2
	 *
	 * @assertion_ids: SAAJ:SPEC:1; SAAJ:SPEC:2; SAAJ:SPEC:3; SAAJ:SPEC:4;
	 * SAAJ:SPEC:5; SAAJ:SPEC:6; SAAJ:SPEC:7; SAAJ:SPEC:8; SAAJ:SPEC:9;
	 * SAAJ:SPEC:10; SAAJ:SPEC:11; SAAJ:SPEC:12; SAAJ:SPEC:13; SAAJ:SPEC:14;
	 * SAAJ:SPEC:15; SAAJ:SPEC:16; SAAJ:SPEC:17; SAAJ:SPEC:18;
	 *
	 * @test_Strategy: Create a soap message containing a soap message part with a
	 * single attachment and send it as a synchronous soap message. Sends a soap 1.1
	 * protocol message.
	 *
	 * Description: Send synchronous soap message with a single attachment.
	 *
	 */
	@Test
	public void SendSyncReqRespMsgSOAP11Test2() throws Exception {
		try {
			boolean pass = true;

			logger.log(Logger.Level.INFO, "SendSyncReqRespMsgSOAP11Test2");
			logger.log(Logger.Level.INFO, "Send synchronous message" + " with a single attachment");
			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, SERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
			props.setProperty("TESTNAME", "SendSyncReqRespMsgTest2");
			props.setProperty("SOAPVERSION", "soap11");
			urlConn = TestUtil.sendPostData(props, url);
			logger.log(Logger.Level.INFO, "Getting response from test servlet.....");
			Properties resProps = TestUtil.getResponseProperties(urlConn);
			if (!resProps.getProperty("TESTRESULT").equals("pass"))
				pass = false;

			if (!pass)
				throw new Exception("SendSyncReqRespMsgSOAP11Test2 failed");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("SendSyncReqRespMsgSOAP11Test2 failed", e);
		}
	}

	/*
	 * @testName: SendSyncReqRespMsgSOAP11Test3
	 *
	 * @assertion_ids: SAAJ:SPEC:1; SAAJ:SPEC:2; SAAJ:SPEC:3; SAAJ:SPEC:4;
	 * SAAJ:SPEC:5; SAAJ:SPEC:6; SAAJ:SPEC:7; SAAJ:SPEC:8; SAAJ:SPEC:9;
	 * SAAJ:SPEC:10; SAAJ:SPEC:11; SAAJ:SPEC:12; SAAJ:SPEC:13; SAAJ:SPEC:14;
	 * SAAJ:SPEC:15; SAAJ:SPEC:16; SAAJ:SPEC:17; SAAJ:SPEC:18;
	 *
	 * @test_Strategy: Create a soap message containing a soap message part with
	 * multiple attachments and send it as a synchronous soap message. Sends a soap
	 * 1.1 protocol message.
	 *
	 * Description: Send synchronous soap message with multiple attachments.
	 *
	 */
	@Test
	public void SendSyncReqRespMsgSOAP11Test3() throws Exception {
		try {
			boolean pass = true;

			logger.log(Logger.Level.INFO, "SendSyncReqRespMsgSOAP11Test3");
			logger.log(Logger.Level.INFO, "Send synchronous message" + " with multiple attachments");
			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, SERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
			props.setProperty("TESTNAME", "SendSyncReqRespMsgTest3");
			props.setProperty("SOAPVERSION", "soap11");
			urlConn = TestUtil.sendPostData(props, url);
			logger.log(Logger.Level.INFO, "Getting response from test servlet.....");
			Properties resProps = TestUtil.getResponseProperties(urlConn);
			if (!resProps.getProperty("TESTRESULT").equals("pass"))
				pass = false;

			if (!pass)
				throw new Exception("SendSyncReqRespMsgSOAP11Test3 failed");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("SendSyncReqRespMsgSOAP11Test3 failed", e);
		}
	}

	/*
	 * @testName: SendSyncReqRespMsgSOAP12Test1
	 *
	 * @assertion_ids: SAAJ:SPEC:1; SAAJ:SPEC:2; SAAJ:SPEC:3; SAAJ:SPEC:4;
	 * SAAJ:SPEC:5; SAAJ:SPEC:6; SAAJ:SPEC:7; SAAJ:SPEC:8; SAAJ:SPEC:9;
	 * SAAJ:SPEC:10; SAAJ:SPEC:11; SAAJ:SPEC:12; SAAJ:SPEC:13; SAAJ:SPEC:14;
	 * SAAJ:SPEC:15; SAAJ:SPEC:16; SAAJ:SPEC:17; SAAJ:SPEC:18;
	 *
	 * @test_Strategy: Create a soap message containing a soap message part with no
	 * attachments and send it as a synchronous soap message. Sends a soap 1.2
	 * protocol message.
	 *
	 * Description: Send synchronous soap message with no attachments.
	 *
	 */
	@Test
	public void SendSyncReqRespMsgSOAP12Test1() throws Exception {
		try {
			boolean pass = true;

			logger.log(Logger.Level.INFO, "SendSyncReqRespMsgSOAP12Test1");
			logger.log(Logger.Level.INFO, "Send synchronous message" + " with no attachments");
			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, SERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
			props.setProperty("TESTNAME", "SendSyncReqRespMsgTest1");
			props.setProperty("SOAPVERSION", "soap12");
			urlConn = TestUtil.sendPostData(props, url);
			logger.log(Logger.Level.INFO, "Getting response from test servlet.....");
			Properties resProps = TestUtil.getResponseProperties(urlConn);
			if (!resProps.getProperty("TESTRESULT").equals("pass"))
				pass = false;

			if (!pass)
				throw new Exception("SendSyncReqRespMsgSOAP12Test1 failed");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("SendSyncReqRespMsgSOAP12Test1 failed", e);
		}
	}

	/*
	 * @testName: SendSyncReqRespMsgSOAP12Test2
	 *
	 * @assertion_ids: SAAJ:SPEC:1; SAAJ:SPEC:2; SAAJ:SPEC:3; SAAJ:SPEC:4;
	 * SAAJ:SPEC:5; SAAJ:SPEC:6; SAAJ:SPEC:7; SAAJ:SPEC:8; SAAJ:SPEC:9;
	 * SAAJ:SPEC:10; SAAJ:SPEC:11; SAAJ:SPEC:12; SAAJ:SPEC:13; SAAJ:SPEC:14;
	 * SAAJ:SPEC:15; SAAJ:SPEC:16; SAAJ:SPEC:17; SAAJ:SPEC:18;
	 *
	 * @test_Strategy: Create a soap message containing a soap message part with a
	 * single attachment and send it as a synchronous soap message. Sends a soap 1.2
	 * protocol message.
	 *
	 * Description: Send synchronous soap message with a single attachment.
	 *
	 */
	@Test
	public void SendSyncReqRespMsgSOAP12Test2() throws Exception {
		try {
			boolean pass = true;

			logger.log(Logger.Level.INFO, "SendSyncReqRespMsgSOAP12Test2");
			logger.log(Logger.Level.INFO, "Send synchronous message" + " with a single attachment");
			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, SERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
			props.setProperty("TESTNAME", "SendSyncReqRespMsgTest2");
			props.setProperty("SOAPVERSION", "soap12");
			urlConn = TestUtil.sendPostData(props, url);
			logger.log(Logger.Level.INFO, "Getting response from test servlet.....");
			Properties resProps = TestUtil.getResponseProperties(urlConn);
			if (!resProps.getProperty("TESTRESULT").equals("pass"))
				pass = false;

			if (!pass)
				throw new Exception("SendSyncReqRespMsgSOAP12Test2 failed");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("SendSyncReqRespMsgSOAP12Test2 failed", e);
		}
	}

	/*
	 * @testName: SendSyncReqRespMsgSOAP12Test3
	 *
	 * @assertion_ids: SAAJ:SPEC:1; SAAJ:SPEC:2; SAAJ:SPEC:3; SAAJ:SPEC:4;
	 * SAAJ:SPEC:5; SAAJ:SPEC:6; SAAJ:SPEC:7; SAAJ:SPEC:8; SAAJ:SPEC:9;
	 * SAAJ:SPEC:10; SAAJ:SPEC:11; SAAJ:SPEC:12; SAAJ:SPEC:13; SAAJ:SPEC:14;
	 * SAAJ:SPEC:15; SAAJ:SPEC:16; SAAJ:SPEC:17; SAAJ:SPEC:18;
	 *
	 * @test_Strategy: Create a soap message containing a soap message part with
	 * multiple attachments and send it as a synchronous soap message. Sends a soap
	 * 1.2 protocol message.
	 *
	 * Description: Send synchronous soap message with multiple attachments.
	 *
	 */
	@Test
	public void SendSyncReqRespMsgSOAP12Test3() throws Exception {
		try {
			boolean pass = true;

			logger.log(Logger.Level.INFO, "SendSyncReqRespMsgSOAP12Test3");
			logger.log(Logger.Level.INFO, "Send synchronous message" + " with multiple attachments");
			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, SERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
			props.setProperty("TESTNAME", "SendSyncReqRespMsgTest3");
			props.setProperty("SOAPVERSION", "soap12");
			urlConn = TestUtil.sendPostData(props, url);
			logger.log(Logger.Level.INFO, "Getting response from test servlet.....");
			Properties resProps = TestUtil.getResponseProperties(urlConn);
			if (!resProps.getProperty("TESTRESULT").equals("pass"))
				pass = false;

			if (!pass)
				throw new Exception("SendSyncReqRespMsgSOAP12Test3 failed");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("SendSyncReqRespMsgSOAP12Test3 failed", e);
		}
	}

	public void cleanup() throws Exception {
		logger.log(Logger.Level.INFO, "cleanup ok");
	}
}
