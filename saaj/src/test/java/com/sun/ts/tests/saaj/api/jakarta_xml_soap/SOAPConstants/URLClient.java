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

package com.sun.ts.tests.saaj.api.jakarta_xml_soap.SOAPConstants;

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

  private static final String SOAPCONSTANTS_TESTSERVLET = "/SOAPConstants_web/SOAPConstantsTestServlet";

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
      throw new Fault("setup failed:", e);
    }
    if (!pass) {
      TestUtil.logErr(
          "Please specify host & port of web server " + "in config properties: "
              + WEBSERVERHOSTPROP + ", " + WEBSERVERPORTPROP);
      throw new Fault("setup failed:");
    }
    logMsg("setup ok");
  }

  public void cleanup() throws Fault {
    logMsg("cleanup ok");
  }

  /*
   * @testName: SOAPConstantsTest
   *
   * @assertion_ids: SAAJ:JAVADOC:0;
   *
   * @test_Strategy: Verify the SOAP1.1 and SOAP1.2 protocol constants
   *
   * Description: Verify the SOAP1.1 and SOAP1.2 protocol constants
   *
   */
  public void SOAPConstantsTest() throws Fault {
    boolean pass = true;
    try {

      TestUtil.logMsg(
          "SOAPConstantsTest: verify SOAP1.1 and SOAP1.2 protocol constants");
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum,
          SOAPCONSTANTS_TESTSERVLET);
      TestUtil.logMsg(url.toString());
      TestUtil.logMsg("Sending post request to test servlet.....");
      props.setProperty("TESTNAME", "SOAPConstantsTest");
      urlConn = TestUtil.sendPostData(props, url);
      TestUtil.logMsg("Getting response from test servlet.....");
      Properties resProps = TestUtil.getResponseProperties(urlConn);
      if (!resProps.getProperty("TESTRESULT").equals("pass"))
        pass = false;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("SOAPConstantsTest failed", e);
    }

    if (!pass)
      throw new Fault("SOAPConstantsTest failed");
  }
}
