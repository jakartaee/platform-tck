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

package com.sun.ts.tests.saaj.ee.SendSyncReqRespMsg;

import java.net.URL;
import java.net.URLConnection;
import java.util.Properties;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.porting.TSURL;
import com.sun.ts.lib.util.TestUtil;

public class URLClient extends EETest {
  private static final String PROTOCOL = "http";

  private static final String HOSTNAME = "localhost";

  private static final int PORTNUM = 8000;

  private static final String SERVLET = "/SendSyncReqRespMsg_web/SendingServlet";

  private static final String WEBSERVERHOSTPROP = "webServerHost";

  private static final String WEBSERVERPORTPROP = "webServerPort";

  private TSURL tsurl = new TSURL();

  private URL url = null;

  private URLConnection urlConn = null;

  private Properties props = null;

  private String hostname = HOSTNAME;

  private int portnum = PORTNUM;

  public static void main(String[] args) {
    URLClient theTests = new URLClient();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /* Test setup */

  /*
   * @class.setup_props: webServerHost; webServerPort;
   */

  public void setup(String[] args, Properties p) throws Fault {
    props = p;
    boolean pass = true;

    try {
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
    } catch (Exception e) {
      throw new Fault("setup failed: ", e);
    }
    if (!pass) {
      TestUtil.logErr(
          "Please specify host & port of web server " + "in config properties: "
              + WEBSERVERHOSTPROP + ", " + WEBSERVERPORTPROP);
      throw new Fault("setup failed");
    }
    logMsg("setup ok");
  }

  /*
   * @testName: SendSyncReqRespMsgSOAP11Test1
   *
   * @assertion_ids: SAAJ:SPEC:1; SAAJ:SPEC:2; SAAJ:SPEC:3; SAAJ:SPEC:4;
   * SAAJ:SPEC:5; SAAJ:SPEC:6; SAAJ:SPEC:7; SAAJ:SPEC:8; SAAJ:SPEC:9;
   * SAAJ:SPEC:10; SAAJ:SPEC:11; SAAJ:SPEC:12; SAAJ:SPEC:13; SAAJ:SPEC:14;
   * SAAJ:SPEC:15; SAAJ:SPEC:16; SAAJ:SPEC:17; SAAJ:SPEC:18;
   *
   * @test_Strategy: Create a soap message containing a soap message part with
   * no attachments and send it as a synchronous soap message. Sends a soap 1.1
   * protocol message.
   *
   * Description: Send synchronous soap message with no attachments.
   *
   */
  public void SendSyncReqRespMsgSOAP11Test1() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("SendSyncReqRespMsgSOAP11Test1");
      TestUtil.logMsg("Send synchronous message" + " with no attachments");
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SERVLET);
      TestUtil.logMsg(url.toString());
      TestUtil.logMsg("Sending post request to test servlet.....");
      props.setProperty("TESTNAME", "SendSyncReqRespMsgTest1");
      props.setProperty("SOAPVERSION", "soap11");
      urlConn = TestUtil.sendPostData(props, url);
      TestUtil.logMsg("Getting response from test servlet.....");
      Properties resProps = TestUtil.getResponseProperties(urlConn);
      if (!resProps.getProperty("TESTRESULT").equals("pass"))
        pass = false;

      if (!pass)
        throw new Fault("SendSyncReqRespMsgSOAP11Test1 failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("SendSyncReqRespMsgSOAP11Test1 failed", e);
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
   * single attachment and send it as a synchronous soap message. Sends a soap
   * 1.1 protocol message.
   *
   * Description: Send synchronous soap message with a single attachment.
   *
   */
  public void SendSyncReqRespMsgSOAP11Test2() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("SendSyncReqRespMsgSOAP11Test2");
      TestUtil.logMsg("Send synchronous message" + " with a single attachment");
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SERVLET);
      TestUtil.logMsg(url.toString());
      TestUtil.logMsg("Sending post request to test servlet.....");
      props.setProperty("TESTNAME", "SendSyncReqRespMsgTest2");
      props.setProperty("SOAPVERSION", "soap11");
      urlConn = TestUtil.sendPostData(props, url);
      TestUtil.logMsg("Getting response from test servlet.....");
      Properties resProps = TestUtil.getResponseProperties(urlConn);
      if (!resProps.getProperty("TESTRESULT").equals("pass"))
        pass = false;

      if (!pass)
        throw new Fault("SendSyncReqRespMsgSOAP11Test2 failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("SendSyncReqRespMsgSOAP11Test2 failed", e);
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
   * multiple attachments and send it as a synchronous soap message. Sends a
   * soap 1.1 protocol message.
   *
   * Description: Send synchronous soap message with multiple attachments.
   *
   */
  public void SendSyncReqRespMsgSOAP11Test3() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("SendSyncReqRespMsgSOAP11Test3");
      TestUtil
          .logMsg("Send synchronous message" + " with multiple attachments");
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SERVLET);
      TestUtil.logMsg(url.toString());
      TestUtil.logMsg("Sending post request to test servlet.....");
      props.setProperty("TESTNAME", "SendSyncReqRespMsgTest3");
      props.setProperty("SOAPVERSION", "soap11");
      urlConn = TestUtil.sendPostData(props, url);
      TestUtil.logMsg("Getting response from test servlet.....");
      Properties resProps = TestUtil.getResponseProperties(urlConn);
      if (!resProps.getProperty("TESTRESULT").equals("pass"))
        pass = false;

      if (!pass)
        throw new Fault("SendSyncReqRespMsgSOAP11Test3 failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("SendSyncReqRespMsgSOAP11Test3 failed", e);
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
   * @test_Strategy: Create a soap message containing a soap message part with
   * no attachments and send it as a synchronous soap message. Sends a soap 1.2
   * protocol message.
   *
   * Description: Send synchronous soap message with no attachments.
   *
   */
  public void SendSyncReqRespMsgSOAP12Test1() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("SendSyncReqRespMsgSOAP12Test1");
      TestUtil.logMsg("Send synchronous message" + " with no attachments");
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SERVLET);
      TestUtil.logMsg(url.toString());
      TestUtil.logMsg("Sending post request to test servlet.....");
      props.setProperty("TESTNAME", "SendSyncReqRespMsgTest1");
      props.setProperty("SOAPVERSION", "soap12");
      urlConn = TestUtil.sendPostData(props, url);
      TestUtil.logMsg("Getting response from test servlet.....");
      Properties resProps = TestUtil.getResponseProperties(urlConn);
      if (!resProps.getProperty("TESTRESULT").equals("pass"))
        pass = false;

      if (!pass)
        throw new Fault("SendSyncReqRespMsgSOAP12Test1 failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("SendSyncReqRespMsgSOAP12Test1 failed", e);
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
   * single attachment and send it as a synchronous soap message. Sends a soap
   * 1.2 protocol message.
   *
   * Description: Send synchronous soap message with a single attachment.
   *
   */
  public void SendSyncReqRespMsgSOAP12Test2() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("SendSyncReqRespMsgSOAP12Test2");
      TestUtil.logMsg("Send synchronous message" + " with a single attachment");
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SERVLET);
      TestUtil.logMsg(url.toString());
      TestUtil.logMsg("Sending post request to test servlet.....");
      props.setProperty("TESTNAME", "SendSyncReqRespMsgTest2");
      props.setProperty("SOAPVERSION", "soap12");
      urlConn = TestUtil.sendPostData(props, url);
      TestUtil.logMsg("Getting response from test servlet.....");
      Properties resProps = TestUtil.getResponseProperties(urlConn);
      if (!resProps.getProperty("TESTRESULT").equals("pass"))
        pass = false;

      if (!pass)
        throw new Fault("SendSyncReqRespMsgSOAP12Test2 failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("SendSyncReqRespMsgSOAP12Test2 failed", e);
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
   * multiple attachments and send it as a synchronous soap message. Sends a
   * soap 1.2 protocol message.
   *
   * Description: Send synchronous soap message with multiple attachments.
   *
   */
  public void SendSyncReqRespMsgSOAP12Test3() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("SendSyncReqRespMsgSOAP12Test3");
      TestUtil
          .logMsg("Send synchronous message" + " with multiple attachments");
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SERVLET);
      TestUtil.logMsg(url.toString());
      TestUtil.logMsg("Sending post request to test servlet.....");
      props.setProperty("TESTNAME", "SendSyncReqRespMsgTest3");
      props.setProperty("SOAPVERSION", "soap12");
      urlConn = TestUtil.sendPostData(props, url);
      TestUtil.logMsg("Getting response from test servlet.....");
      Properties resProps = TestUtil.getResponseProperties(urlConn);
      if (!resProps.getProperty("TESTRESULT").equals("pass"))
        pass = false;

      if (!pass)
        throw new Fault("SendSyncReqRespMsgSOAP12Test3 failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("SendSyncReqRespMsgSOAP12Test3 failed", e);
    }
  }

  public void cleanup() throws Fault {
    logMsg("cleanup ok");
  }
}
