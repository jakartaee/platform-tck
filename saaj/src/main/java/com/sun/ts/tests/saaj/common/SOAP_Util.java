/*
 * Copyright (c) 2007, 2020 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.saaj.common;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.lang.System.Logger;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Properties;

import com.sun.ts.lib.util.TestUtil;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.xml.soap.MessageFactory;
import jakarta.xml.soap.SOAPConnection;
import jakarta.xml.soap.SOAPConnectionFactory;
import jakarta.xml.soap.SOAPConstants;
import jakarta.xml.soap.SOAPFactory;
import jakarta.xml.soap.SOAPMessage;

public final class SOAP_Util {

	private static final Logger logger = (Logger) System.getLogger(SOAP_Util.class.getName());

	public static final String SOAP11 = "soap11";

	public static final String SOAP12 = "soap12";

	public static final String SOAPVERSION = "SOAPVERSION";

	private static String soapVersion = null;

	private static final String HTTPPROXYHOST = "httpProxyHost";

	private static final String HTTPPROXYPORT = "httpProxyPort";

	private static final String PLATFORMMODE = "platform.mode";

	private static String hostname = "localhost";

	private static int portnum = 8080;

	private static String httpProxyHost = null;

	private static String httpProxyPort = null;

	private static boolean debug = false;

	private static boolean SOAPConnectionFactorySupported = true;

	private static Properties harnessProps = null;

	private static SOAPConnection scon = null;

	private static SOAPConnectionFactory sconfactory = null;

	private static String platformMode = null;

	private static MessageFactory mfactorySOAP11 = null;

	private static MessageFactory mfactorySOAP12 = null;

	private static SOAPFactory sfactorySOAP11 = null;

	private static SOAPFactory sfactorySOAP12 = null;

	public static void doServletInit(ServletConfig servletConfig) throws ServletException {
		logger.log(Logger.Level.TRACE,"SOAP_Util:doServletInit");
		setup();
	}

	public static void setup() throws ServletException {
		logger.log(Logger.Level.TRACE,"SOAP_Util:setup");
		try {
			logger.log(Logger.Level.TRACE,"Create SOAPConnectionFactory object");
			if (sconfactory == null)
				sconfactory = SOAPConnectionFactory.newInstance();
			logger.log(Logger.Level.TRACE,"Create SOAPConnection object");
			if (scon == null)
				scon = sconfactory.createConnection();
		} catch (UnsupportedOperationException e) {
			SOAPConnectionFactorySupported = false;
		} catch (Exception e) {
			System.err.println("Exception occurred: " + e.getMessage());
			e.printStackTrace(System.err);
			throw new ServletException("Exception occurred: " + e.getMessage());
		}
	}

	public static void doServletPost(HttpServletRequest req, HttpServletResponse res) throws ServletException {
		logger.log(Logger.Level.TRACE,"SOAP_Util:doServletPost");

		harnessProps = new Properties();

		Enumeration enumlist = req.getParameterNames();
		while (enumlist.hasMoreElements()) {
			String name = (String) enumlist.nextElement();
			String value = req.getParameter(name);
			harnessProps.setProperty(name, value);
		}

		// new file object
		File file = new File("/tmp/harnessmap.out");

		BufferedWriter bf = null;

		try {

			// create new BufferedWriter for the output file
			bf = new BufferedWriter(new FileWriter(file));

			// iterate map entries
			for (Object key : harnessProps.keySet()) {
				bf.write(key + ":" + harnessProps.getProperty((String) key));
				bf.newLine();
			}

			bf.flush();
			bf.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			// TestUtil.init(harnessProps);
			logger.log(Logger.Level.TRACE, "Remote logging intialized for Servlet");
			if (debug) {
				logger.log(Logger.Level.TRACE, "Here are the harness props");
				// TestUtil.list(harnessProps);
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "SOAP_Util:doServletPost Exception: " + e);
			TestUtil.printStackTrace(e);
			System.err.println("SOAP_Util:doServletPost Exception: " + e);
			e.printStackTrace(System.err);
			throw new ServletException("Unable to initialize remote logging");
		}

		try {
			soapVersion = null;
			hostname = null;
			portnum = -1;
			if (harnessProps.getProperty(SOAPVERSION) != null)
				soapVersion = harnessProps.getProperty(SOAPVERSION);
			if (harnessProps.getProperty(Client.WEBSERVERHOSTPROP) != null)
				hostname = harnessProps.getProperty(Client.WEBSERVERHOSTPROP);
			if (harnessProps.getProperty(Client.WEBSERVERPORTPROP) != null)
				portnum = Integer.parseInt(harnessProps.getProperty(Client.WEBSERVERPORTPROP));
			if (harnessProps.getProperty(HTTPPROXYHOST) != null)
				httpProxyHost = harnessProps.getProperty(HTTPPROXYHOST);
			if (harnessProps.getProperty(HTTPPROXYPORT) != null)
				httpProxyPort = harnessProps.getProperty(HTTPPROXYPORT);
			if (harnessProps.getProperty(PLATFORMMODE) != null)
				platformMode = harnessProps.getProperty(PLATFORMMODE);
		} catch (Exception e) {
			logger.log(Logger.Level.INFO, "Caught exception: " + e);
			e.printStackTrace();
		}

		if (hostname == null || portnum == -1) {
			String s = null;
			if (hostname == null) {
				logger.log(Logger.Level.ERROR, "FATAL:" + Client.WEBSERVERHOSTPROP + " property not set!");
				s = "FATAL: " + Client.WEBSERVERHOSTPROP + " property not set!";
			}
			if (portnum == -1) {
				logger.log(Logger.Level.ERROR, "FATAL: " + Client.WEBSERVERPORTPROP + " property not set!");
				s = s + "\nFATAL: " + Client.WEBSERVERPORTPROP + " property not set!";
			}
			throw new ServletException(s);
		}

		if (soapVersion == null || soapVersion.equals(SOAP_Util.SOAP11))
			logger.log(Logger.Level.INFO, "Testing SOAP Version 1.1 Protocol");
		else
			logger.log(Logger.Level.INFO, "Testing SOAP Version 1.2 Protocol");
	}

	public static Properties getHarnessProps() {
		return harnessProps;
	}

	public static String getHostname() {
		return hostname;
	}

	public static int getPortnum() {
		return portnum;
	}

	public static String getHttpProxyHost() {
		return httpProxyHost;
	}

	public static String getHttpProxyPort() {
		return httpProxyPort;
	}

	public static String getPlatformMode() {
		return platformMode;
	}

	public static boolean isSOAPConnectionFactorySupported() {
		return SOAPConnectionFactorySupported;
	}

	public static SOAPConnection getSOAPConnection() {
		logger.log(Logger.Level.TRACE,"SOAP_Util:getSOAPConnection");
		return scon;
	}

	public static String getSOAPVersion() {
		logger.log(Logger.Level.TRACE,"SOAP_Util:getSOAPVersion");
		if (soapVersion == null || soapVersion.equals(SOAP_Util.SOAP11))
			return SOAP_Util.SOAP11;
		else
			return SOAP_Util.SOAP12;
	}

	public static void setSOAPVersion(String s) {
		logger.log(Logger.Level.TRACE,"SOAP_Util:setSOAPVersion");
		soapVersion = s;
		if (soapVersion == null || soapVersion.equals(SOAP_Util.SOAP11))
			logger.log(Logger.Level.INFO, "Testing SOAP Version 1.1 Protocol");
		else
			logger.log(Logger.Level.INFO, "Testing SOAP Version 1.2 Protocol");
	}

	public static SOAPConnection openSOAPConnection() throws Exception {
		logger.log(Logger.Level.TRACE,"SOAP_Util:openSOAPConnection");
		if (SOAPConnectionFactorySupported && sconfactory != null)
			scon = sconfactory.createConnection();
		return scon;
	}

	public static void closeSOAPConnection() {
		logger.log(Logger.Level.TRACE,"SOAP_Util:closeSOAPConnection");
		try {
			if (SOAPConnectionFactorySupported && scon != null)
				scon.close();
		} catch (Exception e) {
		}
		scon = null;
	}

	public static SOAPConnectionFactory getSOAPConnectionFactory() {
		logger.log(Logger.Level.TRACE,"SOAP_Util:getSOAPConnectionFactory");
		return sconfactory;
	}

	public static MessageFactory getMessageFactory() throws Exception {
		logger.log(Logger.Level.TRACE,"SOAP_Util:getMessageFactory");
		if (soapVersion == null) {
			if (mfactorySOAP11 == null)
				mfactorySOAP11 = MessageFactory.newInstance();
			return mfactorySOAP11;
		} else if (soapVersion.equals(SOAP11)) {
			if (mfactorySOAP11 == null)
				mfactorySOAP11 = MessageFactory.newInstance(SOAPConstants.SOAP_1_1_PROTOCOL);
			return mfactorySOAP11;
		} else {
			if (mfactorySOAP12 == null)
				mfactorySOAP12 = MessageFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL);
			return mfactorySOAP12;
		}
	}

	public static SOAPFactory getSOAPFactory() throws Exception {
		logger.log(Logger.Level.TRACE,"SOAP_Util:getSOAPFactory");
		if (soapVersion == null) {
			if (sfactorySOAP11 == null)
				sfactorySOAP11 = SOAPFactory.newInstance();
			return sfactorySOAP11;
		} else if (soapVersion.equals(SOAP11)) {
			if (sfactorySOAP11 == null)
				sfactorySOAP11 = SOAPFactory.newInstance(SOAPConstants.SOAP_1_1_PROTOCOL);
			return sfactorySOAP11;
		} else {
			if (sfactorySOAP12 == null)
				sfactorySOAP12 = SOAPFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL);
			return sfactorySOAP12;
		}
	}

	public static SOAPMessage createSOAPMessage() throws Exception {
		logger.log(Logger.Level.TRACE,"SOAP_Util:createSOAPMessage");
		SOAPMessage message = getMessageFactory().createMessage();
		return message;
	}

	public static int getIteratorCount(Iterator i) {
		int count = 0;

		logger.log(Logger.Level.TRACE,"SOAP_Util.getIteratorCount");

		while (i.hasNext()) {
			count++;
			i.next();
		}
		return count;
	}

	public static StringBuffer copyToBuffer(InputStream is) {
		logger.log(Logger.Level.TRACE,"SOAP_Util.copyToBuffer");

		if (is == null)
			return null;

		StringWriter sw = new StringWriter();
		BufferedReader br = new BufferedReader(new InputStreamReader(is));

		try {
			String s;
			while ((s = br.readLine()) != null)
				sw.write(s);
		} catch (Exception e) {
		}
		return sw.getBuffer();
	}

	public static void dumpSOAPMessage(SOAPMessage msg) {
		logger.log(Logger.Level.INFO, "***** Begin Dumping SOAPMessage *****");
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			msg.writeTo(baos);
			logger.log(Logger.Level.INFO, baos.toString());
		} catch (Exception e) {
			System.err.println("Exception occurred: " + e);
			e.printStackTrace();
		}
		logger.log(Logger.Level.INFO, "***** Done Dumping SOAPMessage *****");
	}

	public static String returnSOAPMessageAsString(SOAPMessage msg) {
		ByteArrayOutputStream baos = null;
		String s = null;
		try {
			baos = new ByteArrayOutputStream();
			msg.writeTo(baos);
			s = baos.toString();
		} catch (Exception e) {
			System.err.println("Exception occurred: " + e);
			e.printStackTrace();
		}
		return s;
	}

	public static void dumpSOAPMessageWOA(SOAPMessage msg) {
		logger.log(Logger.Level.INFO, "***** Begin Dumping SOAPMessage Without Attachments *****");
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			msg.writeTo(baos);
			SOAPMessage tmpMsg = getMessageFactory().createMessage(msg.getMimeHeaders(),
					new ByteArrayInputStream(baos.toByteArray()));
			tmpMsg.removeAllAttachments();
			tmpMsg.saveChanges();
			baos = new ByteArrayOutputStream();
			tmpMsg.writeTo(baos);
			logger.log(Logger.Level.INFO, baos.toString());
		} catch (Exception e) {
			System.err.println("Exception occurred: " + e);
			e.printStackTrace();
		}
		logger.log(Logger.Level.INFO, "***** Done Dumping SOAPMessage Without Attachments *****");
	}
}
