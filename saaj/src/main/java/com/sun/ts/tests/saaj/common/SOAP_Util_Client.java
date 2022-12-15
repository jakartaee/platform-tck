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
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.Iterator;

import com.sun.ts.lib.util.TestUtil;

import jakarta.xml.soap.MessageFactory;
import jakarta.xml.soap.SOAPConnection;
import jakarta.xml.soap.SOAPConnectionFactory;
import jakarta.xml.soap.SOAPConstants;
import jakarta.xml.soap.SOAPFactory;
import jakarta.xml.soap.SOAPMessage;

public final class SOAP_Util_Client {
  public static final String SOAP11 = "soap11";

  public static final String SOAP12 = "soap12";

  public static final String SOAPVERSION = "SOAPVERSION";

  private static String soapVersion = null;

  private static boolean debug = false;

  private static boolean SOAPConnectionFactorySupported = true;

  private static SOAPConnection scon = null;

  private static SOAPConnectionFactory sconfactory = null;

  private static MessageFactory mfactorySOAP11 = null;

  private static MessageFactory mfactorySOAP12 = null;

  private static SOAPFactory sfactorySOAP11 = null;

  private static SOAPFactory sfactorySOAP12 = null;

  public static void setup() throws Exception {
    System.out.println("SOAP_Util_Client:setup");
    try {
      System.out.println("Create SOAPConnectionFactory object");
      if (sconfactory == null)
        sconfactory = SOAPConnectionFactory.newInstance();
      System.out.println("Create SOAPConnection object");
      if (scon == null)
        scon = sconfactory.createConnection();
    } catch (UnsupportedOperationException e) {
      SOAPConnectionFactorySupported = false;
    } catch (Exception e) {
      System.err.println("Exception occurred: " + e.getMessage());
      e.printStackTrace(System.err);
      throw e;
    }
  }

  public static boolean isSOAPConnectionFactorySupported() {
    return SOAPConnectionFactorySupported;
  }

  public static SOAPConnection getSOAPConnection() {
    System.out.println("SOAP_Util_Client:getSOAPConnection");
    return scon;
  }

  public static String getSOAPVersion() {
    System.out.println("SOAP_Util_Client:getSOAPVersion");
    if (soapVersion == null || soapVersion.equals(SOAP_Util_Client.SOAP11))
      return SOAP_Util_Client.SOAP11;
    else
      return SOAP_Util_Client.SOAP12;
  }

  public static void setSOAPVersion(String s) {
    System.out.println("SOAP_Util_Client:setSOAPVersion");
    soapVersion = s;
    if (soapVersion == null || soapVersion.equals(SOAP_Util_Client.SOAP11))
      TestUtil.logMsg("Testing SOAP Version 1.1 Protocol");
    else
      TestUtil.logMsg("Testing SOAP Version 1.2 Protocol");
  }

  public static SOAPConnection openSOAPConnection() throws Exception {
    System.out.println("SOAP_Util_Client:openSOAPConnection");
    if (SOAPConnectionFactorySupported && sconfactory != null)
      scon = sconfactory.createConnection();
    return scon;
  }

  public static void closeSOAPConnection() {
    System.out.println("SOAP_Util_Client:closeSOAPConnection");
    try {
      if (SOAPConnectionFactorySupported && scon != null)
        scon.close();
    } catch (Exception e) {
    }
    scon = null;
  }

  public static SOAPConnectionFactory getSOAPConnectionFactory() {
    System.out.println("SOAP_Util_Client:getSOAPConnectionFactory");
    return sconfactory;
  }

  public static MessageFactory getMessageFactory() throws Exception {
    System.out.println("SOAP_Util_Client:getMessageFactory");
    if (soapVersion == null) {
      if (mfactorySOAP11 == null)
        mfactorySOAP11 = MessageFactory.newInstance();
      return mfactorySOAP11;
    } else if (soapVersion.equals(SOAP11)) {
      if (mfactorySOAP11 == null)
        mfactorySOAP11 = MessageFactory
            .newInstance(SOAPConstants.SOAP_1_1_PROTOCOL);
      return mfactorySOAP11;
    } else {
      if (mfactorySOAP12 == null)
        mfactorySOAP12 = MessageFactory
            .newInstance(SOAPConstants.SOAP_1_2_PROTOCOL);
      return mfactorySOAP12;
    }
  }

  public static SOAPFactory getSOAPFactory() throws Exception {
    System.out.println("SOAP_Util_Client:getSOAPFactory");
    if (soapVersion == null) {
      if (sfactorySOAP11 == null)
        sfactorySOAP11 = SOAPFactory.newInstance();
      return sfactorySOAP11;
    } else if (soapVersion.equals(SOAP11)) {
      if (sfactorySOAP11 == null)
        sfactorySOAP11 = SOAPFactory
            .newInstance(SOAPConstants.SOAP_1_1_PROTOCOL);
      return sfactorySOAP11;
    } else {
      if (sfactorySOAP12 == null)
        sfactorySOAP12 = SOAPFactory
            .newInstance(SOAPConstants.SOAP_1_2_PROTOCOL);
      return sfactorySOAP12;
    }
  }

  public static SOAPMessage createSOAPMessage() throws Exception {
    System.out.println("SOAP_Util_Client:createSOAPMessage");
    SOAPMessage message = getMessageFactory().createMessage();
    return message;
  }

  public static int getIteratorCount(Iterator i) {
    int count = 0;

    System.out.println("SOAP_Util_Client.getIteratorCount");

    while (i.hasNext()) {
      count++;
      i.next();
    }
    return count;
  }

  public static StringBuffer copyToBuffer(InputStream is) {
    System.out.println("SOAP_Util_Client.copyToBuffer");

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
    TestUtil.logMsg("***** Begin Dumping SOAPMessage *****");
    try {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      msg.writeTo(baos);
      TestUtil.logMsg(baos.toString());
    } catch (Exception e) {
      System.err.println("Exception occurred: " + e);
      e.printStackTrace();
    }
    TestUtil.logMsg("***** Done Dumping SOAPMessage *****");
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
    TestUtil
        .logMsg("***** Begin Dumping SOAPMessage Without Attachments *****");
    try {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      msg.writeTo(baos);
      SOAPMessage tmpMsg = getMessageFactory().createMessage(
          msg.getMimeHeaders(), new ByteArrayInputStream(baos.toByteArray()));
      tmpMsg.removeAllAttachments();
      tmpMsg.saveChanges();
      baos = new ByteArrayOutputStream();
      tmpMsg.writeTo(baos);
      TestUtil.logMsg(baos.toString());
    } catch (Exception e) {
      System.err.println("Exception occurred: " + e);
      e.printStackTrace();
    }
    TestUtil.logMsg("***** Done Dumping SOAPMessage Without Attachments *****");
  }
}
