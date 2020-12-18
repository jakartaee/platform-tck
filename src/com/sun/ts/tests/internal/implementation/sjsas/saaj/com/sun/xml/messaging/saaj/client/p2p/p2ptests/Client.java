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

package com.sun.ts.tests.internal.implementation.sjsas.saaj.com.sun.xml.messaging.saaj.client.p2p.p2ptests;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Properties;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.porting.TSURL;
import com.sun.ts.lib.util.TestUtil;
// Import implementation specific classes to test
import com.sun.xml.messaging.saaj.client.p2p.*;

import jakarta.xml.soap.MessageFactory;
import jakarta.xml.soap.SOAPBody;
import jakarta.xml.soap.SOAPBodyElement;
import jakarta.xml.soap.SOAPEnvelope;
import jakarta.xml.soap.SOAPHeader;
import jakarta.xml.soap.SOAPHeaderElement;
import jakarta.xml.soap.SOAPMessage;
import jakarta.xml.soap.SOAPPart;

public class Client extends EETest {
  private String srcDir = "src/com/sun/ts/tests/internal/implementation/sjsas/saaj/com/sun/xml/messaging/saaj/client/p2p/p2ptests";

  private String testDir = null;

  private static final String PROTOCOL = "http";

  private static final String HOSTNAME = "localhost";

  private static final int PORTNUM = 8000;

  private static final String WEBSERVERHOSTPROP = "webServerHost";

  private static final String WEBSERVERPORTPROP = "webServerPort";

  private TSURL tsurl = new TSURL();

  private URL url = null;

  private URLConnection urlConn = null;

  private Properties props = null;

  private String hostname = HOSTNAME;

  private int portnum = PORTNUM;

  private String username = "";

  private String password = "";

  private String authUsername = "";

  private String authPassword = "";

  private static final String UserNameProp = "user";

  private static final String PasswordProp = "password";

  private static final String authUserNameProp = "authuser";

  private static final String authPasswordProp = "authpassword";

  private static final String SERVLET = "/HttpSOAPConnection/ReceivingServlet";

  private static final String GoodSoapMessage = "<?xml version=\"1.0\" encoding=\"utf-8\"?><soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:enc=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:ns=\"http://httptestservice.org/wsdl\"><soap:Body><ns:hello><parameters><string>World</string></parameters></ns:hello></soap:Body></soap:Envelope>";

  private HttpURLConnection conn = null;

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /*
   * @class.setup_props: ts_home; webServerHost; webServerPort; user; password;
   * authuser; authpassword;
   */
  public void setup(String[] args, Properties p) throws Fault {
    props = p;
    boolean pass = true;
    try {
      String tsHome = p.getProperty("ts_home");
      testDir = tsHome + "/" + srcDir;
      hostname = p.getProperty(WEBSERVERHOSTPROP);
      if (hostname == null)
        pass = false;
      else if (hostname.equals(""))
        pass = false;
      try {
        portnum = Integer.parseInt(p.getProperty(WEBSERVERPORTPROP));
      } catch (Exception e) {
        pass = false;
      }
      username = p.getProperty(UserNameProp);
      password = p.getProperty(PasswordProp);
      authUsername = p.getProperty(authUserNameProp);
      authPassword = p.getProperty(authPasswordProp);
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SERVLET);
      TestUtil.logMsg("username=" + username);
      TestUtil.logMsg("password=" + password);
      TestUtil.logMsg("authUsername=" + authUsername);
      TestUtil.logMsg("authPassword=" + authPassword);
      TestUtil.logMsg("url=" + url);
    } catch (Exception e) {
      throw new Fault("setup failed:", e);
    }
    if (!pass) {
      TestUtil.logErr(
          "Please specify host & port of web server " + "in config properties: "
              + WEBSERVERHOSTPROP + ", " + WEBSERVERPORTPROP);
      throw new Fault("setup failed:");
    }
    TestUtil.logMsg("setup ok");
  }

  public void cleanup() throws Fault {
    TestUtil.logMsg("cleanup ok");
  }

  /*
   * @testName: HttpSOAPConnectionTest
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void HttpSOAPConnectionTest() throws Fault {
    TestUtil.logTrace("HttpSOAPConnectionTest");
    boolean pass = true;
    try {
      TestUtil.logMsg("Call HttpSOAPConnection() constructor");
      HttpSOAPConnection conn = new HttpSOAPConnection();
      TestUtil.logMsg("Call HttpSOAPConnection.setProxy(String, int) method");
      conn.setProxy(hostname, portnum);
      TestUtil.logMsg("Call HttpSOAPConnection.getProxyHost() method");
      String host = conn.getProxyHost();
      if (!host.equals(hostname)) {
        TestUtil.logErr("Unexpected proxy hostname: expected " + hostname
            + ", got " + host);
        pass = false;
      }
      TestUtil.logMsg("Call HttpSOAPConnection.getProxyPort() method");
      int port = conn.getProxyPort();
      if (port != portnum) {
        TestUtil.logErr(
            "Unexpected proxy port: expected " + portnum + ", got " + port);
        pass = false;
      }
      TestUtil
          .logMsg("Call HttpSOAPConnection.call(SOAPMessage, Object) method");
      SOAPMessage sendmsg = createSOAPMessage();
      SOAPMessage replymsg = conn.call(sendmsg, url);
      TestUtil.logMsg("Call HttpSOAPConnection.close() method");
      conn.close();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("HttpSOAPConnectionTest failed", e);
    }

    if (!pass)
      throw new Fault("HttpSOAPConnectionTest failed");
  }

  private SOAPMessage createSOAPMessage() throws Exception {
    // Create and populate a SOAP message for test purposes
    MessageFactory mf = MessageFactory.newInstance();
    SOAPMessage msg = mf.createMessage();
    SOAPPart sp = msg.getSOAPPart();
    SOAPEnvelope envelope = sp.getEnvelope();
    SOAPHeader hdr = envelope.getHeader();
    SOAPBody bdy = envelope.getBody();
    SOAPHeaderElement transaction = hdr.addHeaderElement(
        envelope.createName("MyTransaction", "t", "request-uri"));
    transaction.setMustUnderstand(true);
    transaction.addTextNode("5");
    SOAPBodyElement gltp = bdy.addBodyElement(envelope
        .createName("GetLastTradePrice", "ztrade", "http://wombat.ztrade.com"));
    gltp.addChildElement(
        envelope.createName("symbol", "ztrade", "http://wombat.ztrade.com"))
        .addTextNode("SUNW");
    return msg;
  }
}
