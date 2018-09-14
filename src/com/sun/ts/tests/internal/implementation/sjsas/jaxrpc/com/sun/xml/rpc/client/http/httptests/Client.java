/*
 * Copyright (c) 2007, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.internal.implementation.sjsas.jaxrpc.com.sun.xml.rpc.client.http.httptests;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import java.io.*;
import java.net.*;
import java.util.*;
import java.rmi.*;

import com.sun.javatest.Status;

// Import implementation specific classes to test
import com.sun.xml.rpc.client.http.*;

public class Client extends EETest {
  private Properties props = null;

  private static final String MYURL = "http://localhost:8080/mydir/myfile";

  private static final String DATE_STRING = "Thu Apr 29 10:39:57 EDT 2004";

  private URL myURL;

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /* Test setup */

  /*
   * @class.setup_props:
   */

  public void setup(String[] args, Properties p) throws Fault {
    try {
      myURL = new URL(MYURL);
    } catch (Exception e) {
      throw new Fault("setup failed", e);
    }
    logMsg("setup ok");
  }

  public void cleanup() throws Fault {
    logMsg("cleanup ok");
  }

  private void dumpCookieValues(HttpCookie hc) {
    TestUtil.logMsg("nameandvalue=" + hc.getNameValue());
    TestUtil.logMsg("name=" + hc.getName());
    TestUtil.logMsg("domain=" + hc.getDomain());
    TestUtil.logMsg("path=" + hc.getPath());
    TestUtil.logMsg("date=" + hc.getExpirationDate());
    TestUtil.logMsg("secure=" + hc.isSecure());
  }

  /*
   * @testName: HttpCookieTest
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void HttpCookieTest() throws Fault {
    TestUtil.logTrace("HttpCookieTest");
    boolean pass = true;
    String cookie;
    String namevalue = "MyCookie=MyValue";
    Date date = new GregorianCalendar(1996, 5, 1, 8, 8, 8).getTime();
    String path = "/MyPath";
    String domain = "MyDomain";
    boolean secure = true;
    HttpCookie hc;
    try {
      TestUtil.logMsg(
          "Call HttpCookie(Date, String, String, String , boolean) constructor");
      hc = new HttpCookie(date, namevalue, path, domain, secure);
      dumpCookieValues(hc);
      cookie = hc.toString();
      TestUtil.logMsg("Call HttpCookie(String) constructor");
      hc = new HttpCookie(cookie);
      dumpCookieValues(hc);
      hc = new HttpCookie(date, namevalue, null, "", secure);
      cookie = hc.toString();
      TestUtil.logMsg("Call HttpCookie(URL, String) constructor");
      hc = new HttpCookie(myURL, cookie);
      dumpCookieValues(hc);
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("HttpCookieTest failed", e);
    }

    if (!pass)
      throw new Fault("HttpCookieTest failed");
  }

  /*
   * @testName: RfcDateParserTest
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void RfcDateParserTest() throws Fault {
    TestUtil.logTrace("RfcDateParserTest");
    boolean pass = true;
    try {
      TestUtil.logMsg("Call RfcDateParser(String) constructor");
      RfcDateParser rdp = new RfcDateParser(DATE_STRING);
      TestUtil.logMsg("Call getDate() method");
      Date date = rdp.getDate();
      TestUtil.logMsg("date=" + date);
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("RfcDateParserTest failed", e);
    }

    if (!pass)
      throw new Fault("RfcDateParserTest failed");
  }
}
