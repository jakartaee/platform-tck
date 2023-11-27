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

package com.sun.ts.tests.saaj.ee.SendVariousMimeAttachments;

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

	private static final String SERVLET = "/SendVariousMimeAttachments_web/SendingServlet";

	private static final Logger logger = (Logger) System.getLogger(URLClient.class.getName());

	@Deployment(testable = false)
	public static WebArchive createDeployment() throws IOException {
		WebArchive archive = ShrinkWrap.create(WebArchive.class, "SendVariousMimeAttachments_web.war");
		archive.addPackages(false, Filters.exclude(URLClient.class),
				"com.sun.ts.tests.saaj.ee.SendVariousMimeAttachments");
		archive.addPackages(false, "com.sun.ts.tests.saaj.common");
		final String CONTENT_ROOT = URLClient.class.getPackageName().replace(".", "/") + "/contentRoot/";
		String[] attachement = { "attach.gif", "attach.html", "attach.jpeg", "attach.txt", "attach.xml" };
		addFilesToArchive(CONTENT_ROOT, attachement, archive);
		archive.addAsWebInfResource(URLClient.class.getPackage(), "standalone.web.xml", "web.xml");
		return archive;
	};

	/*
	 * @testName: SendVariousMimeAttachmentsSOAP11Test
	 *
	 * @assertion_ids: SAAJ:SPEC:1; SAAJ:SPEC:2; SAAJ:SPEC:3; SAAJ:SPEC:4;
	 * SAAJ:SPEC:5; SAAJ:SPEC:6; SAAJ:SPEC:7; SAAJ:SPEC:8; SAAJ:SPEC:9;
	 * SAAJ:SPEC:10; SAAJ:SPEC:11; SAAJ:SPEC:12; SAAJ:SPEC:13; SAAJ:SPEC:14;
	 * SAAJ:SPEC:15; SAAJ:SPEC:16; SAAJ:SPEC:17; SAAJ:SPEC:18;
	 *
	 * @test_Strategy: Create a soap message with various MIME attachments and then
	 * send the soap message. Verify that all MIME attachments are received
	 * correctly. Sends a soap 1.1 protocol message.
	 *
	 * Description: Send soap message with various MIME attachments.
	 *
	 */
	@Test
	public void SendVariousMimeAttachmentsSOAP11Test() throws Exception {
		try {
			boolean pass = true;

			logger.log(Logger.Level.INFO, "SendVariousMimeAttachmentsSOAP11Test");
			logger.log(Logger.Level.INFO, "Send SOAP message with various MIME attachments");
			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, SERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			props.setProperty("TESTNAME", "SendVariousMimeAttachmentsTest");
			props.setProperty("SOAPVERSION", "soap11");
			logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
			urlConn = TestUtil.sendPostData(props, url);
			logger.log(Logger.Level.INFO, "Getting response from test servlet.....");
			Properties resProps = TestUtil.getResponseProperties(urlConn);
			if (!resProps.getProperty("TESTRESULT").equals("pass"))
				pass = false;

			if (!pass)
				throw new Exception("SendVariousMimeAttachmentsSOAP11Test failed");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("SendVariousMimeAttachmentsSOAP11Test failed", e);
		}
	}

	/*
	 * @testName: SendVariousMimeAttachmentsSOAP12Test
	 *
	 * @assertion_ids: SAAJ:SPEC:1; SAAJ:SPEC:2; SAAJ:SPEC:3; SAAJ:SPEC:4;
	 * SAAJ:SPEC:5; SAAJ:SPEC:6; SAAJ:SPEC:7; SAAJ:SPEC:8; SAAJ:SPEC:9;
	 * SAAJ:SPEC:10; SAAJ:SPEC:11; SAAJ:SPEC:12; SAAJ:SPEC:13; SAAJ:SPEC:14;
	 * SAAJ:SPEC:15; SAAJ:SPEC:16; SAAJ:SPEC:17; SAAJ:SPEC:18;
	 *
	 * @test_Strategy: Create a soap message with various MIME attachments and then
	 * send the soap message. Verify that all MIME attachments are received
	 * correctly. Sends a soap 1.2 protocol message.
	 *
	 * Description: Send soap message with various MIME attachments.
	 *
	 */
	@Test
	public void SendVariousMimeAttachmentsSOAP12Test() throws Exception {
		try {
			boolean pass = true;

			logger.log(Logger.Level.INFO, "SendVariousMimeAttachmentsSOAP12Test");
			logger.log(Logger.Level.INFO, "Send SOAP message with various MIME attachments");
			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, SERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			props.setProperty("TESTNAME", "SendVariousMimeAttachmentsTest");
			props.setProperty("SOAPVERSION", "soap12");
			logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
			urlConn = TestUtil.sendPostData(props, url);
			logger.log(Logger.Level.INFO, "Getting response from test servlet.....");
			Properties resProps = TestUtil.getResponseProperties(urlConn);
			if (!resProps.getProperty("TESTRESULT").equals("pass"))
				pass = false;

			if (!pass)
				throw new Exception("SendVariousMimeAttachmentsSOAP12Test failed");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("SendVariousMimeAttachmentsSOAP12Test failed", e);
		}
	}

	public void cleanup() throws Exception {
		logger.log(Logger.Level.INFO, "cleanup ok");
	}
}
