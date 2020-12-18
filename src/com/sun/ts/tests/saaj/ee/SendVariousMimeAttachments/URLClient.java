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

package com.sun.ts.tests.saaj.ee.SendVariousMimeAttachments;

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

  private static final String SERVLET = "/SendVariousMimeAttachments_web/SendingServlet";

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
   * @testName: SendVariousMimeAttachmentsSOAP11Test
   *
   * @assertion_ids: SAAJ:SPEC:1; SAAJ:SPEC:2; SAAJ:SPEC:3; SAAJ:SPEC:4;
   * SAAJ:SPEC:5; SAAJ:SPEC:6; SAAJ:SPEC:7; SAAJ:SPEC:8; SAAJ:SPEC:9;
   * SAAJ:SPEC:10; SAAJ:SPEC:11; SAAJ:SPEC:12; SAAJ:SPEC:13; SAAJ:SPEC:14;
   * SAAJ:SPEC:15; SAAJ:SPEC:16; SAAJ:SPEC:17; SAAJ:SPEC:18;
   *
   * @test_Strategy: Create a soap message with various MIME attachments and
   * then send the soap message. Verify that all MIME attachments are received
   * correctly. Sends a soap 1.1 protocol message.
   *
   * Description: Send soap message with various MIME attachments.
   *
   */
  public void SendVariousMimeAttachmentsSOAP11Test() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("SendVariousMimeAttachmentsSOAP11Test");
      TestUtil.logMsg("Send SOAP message with various MIME attachments");
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SERVLET);
      TestUtil.logMsg(url.toString());
      props.setProperty("TESTNAME", "SendVariousMimeAttachmentsTest");
      props.setProperty("SOAPVERSION", "soap11");
      TestUtil.logMsg("Sending post request to test servlet.....");
      urlConn = TestUtil.sendPostData(props, url);
      TestUtil.logMsg("Getting response from test servlet.....");
      Properties resProps = TestUtil.getResponseProperties(urlConn);
      if (!resProps.getProperty("TESTRESULT").equals("pass"))
        pass = false;

      if (!pass)
        throw new Fault("SendVariousMimeAttachmentsSOAP11Test failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("SendVariousMimeAttachmentsSOAP11Test failed", e);
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
   * @test_Strategy: Create a soap message with various MIME attachments and
   * then send the soap message. Verify that all MIME attachments are received
   * correctly. Sends a soap 1.2 protocol message.
   *
   * Description: Send soap message with various MIME attachments.
   *
   */
  public void SendVariousMimeAttachmentsSOAP12Test() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("SendVariousMimeAttachmentsSOAP12Test");
      TestUtil.logMsg("Send SOAP message with various MIME attachments");
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SERVLET);
      TestUtil.logMsg(url.toString());
      props.setProperty("TESTNAME", "SendVariousMimeAttachmentsTest");
      props.setProperty("SOAPVERSION", "soap12");
      TestUtil.logMsg("Sending post request to test servlet.....");
      urlConn = TestUtil.sendPostData(props, url);
      TestUtil.logMsg("Getting response from test servlet.....");
      Properties resProps = TestUtil.getResponseProperties(urlConn);
      if (!resProps.getProperty("TESTRESULT").equals("pass"))
        pass = false;

      if (!pass)
        throw new Fault("SendVariousMimeAttachmentsSOAP12Test failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("SendVariousMimeAttachmentsSOAP12Test failed", e);
    }
  }

  public void cleanup() throws Fault {
    logMsg("cleanup ok");
  }
}
