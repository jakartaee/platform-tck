/*
 * Copyright (c) 2012, 2019 Oracle and/or its affiliates and others.
 * All rights reserved.
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
 * $Id:$
 */
package com.sun.ts.tests.servlet.pluggability.api.javax_servlet_http.cookie;

import com.sun.javatest.Status;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.common.webclient.http.HttpRequest;
import com.sun.ts.tests.common.webclient.http.HttpResponse;
import com.sun.ts.tests.servlet.common.client.AbstractUrlClient;
import com.sun.ts.tests.servlet.common.util.Data;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.cookie.CookieSpec;

public class URLClient extends AbstractUrlClient {

  /**
   * Entry point for different-VM execution. It should delegate to method
   * run(String[], PrintWriter, PrintWriter), and this method should not contain
   * any test configuration.
   */
  public static void main(String[] args) {
    URLClient theTests = new URLClient();
    Status s = theTests.run(args, new PrintWriter(System.out),
        new PrintWriter(System.err));
    s.exit();
  }

  /**
   * Entry point for same-VM execution. In different-VM execution, the main
   * method delegates to this method.
   */
  public Status run(String args[], PrintWriter out, PrintWriter err) {

    setServletName("TestServlet");
    setContextRoot("/servlet_pluh_cookie_web");

    return super.run(args, out, err);
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   *
   */
  private int findCookie(Cookie[] cookie, String name) {
    boolean found = false;
    int i = 0;
    if (cookie != null) {
      while ((!found) && (i < cookie.length)) {
        if (cookie[i].getName().equals(name)) {
          found = true;
        } else {
          i++;
        }
      }
    } else {
      found = false;
    }
    if (found) {
      return i;
    } else {
      return -1;
    }
  }

  /* Run test */

  /*
   * @testName: cloneTest
   *
   * @assertion_ids: Servlet:JAVADOC:453
   *
   * @test_Strategy: Create a web application with no web.xml and one fragment;
   * Define everything in web-fragment.xml; Package everything in the fragment;
   * Servlet tests method Cookie.clone and verify it works;
   */
  public void cloneTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "cloneTest");
    invoke();
  }

  /*
   * @testName: constructorTest
   * 
   * @assertion_ids: Servlet:JAVADOC:434
   * 
   * @test_Strategy: Create a web application with no web.xml and one fragment;
   * Define everything in web-fragment.xml; Package everything in the fragment;
   * Servlet tests constructor method and verify it works;
   */
  public void constructorTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "constructorTest");
    invoke();
  }

  /*
   * @testName: constructorIllegalArgumentExceptionTest
   * 
   * @assertion_ids: Servlet:JAVADOC:628
   * 
   * @test_Strategy: Create a web application with no web.xml and one fragment;
   * Define everything in web-fragment.xml; Package everything in the fragment;
   * Servlet tests constructor method throws IllegalArgumentException when
   * invalid names are used(unsupported characters in name);
   */
  public void constructorIllegalArgumentExceptionTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /servlet_pluh_cookie_web/TestServlet?testname=constructorIllegalArgumentExceptionTest HTTP/1.1");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    invoke();
  }

  /*
   * @testName: getCommentTest
   * 
   * @assertion_ids: Servlet:JAVADOC:436
   * 
   * @test_Strategy: Create a web application with no web.xml and one fragment;
   * Define everything in web-fragment.xml; Package everything in the fragment;
   * Servlet tests method getComment;
   */
  public void getCommentTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "getCommentTest");
    invoke();
  }

  /*
   * @testName: getCommentNullTest
   * 
   * @assertion_ids: Servlet:JAVADOC:437
   * 
   * @test_Strategy: Create a web application with no web.xml and one fragment;
   * Define everything in web-fragment.xml; Package everything in the fragment;
   * Servlet tests method getComment when there is no comment;
   */
  public void getCommentNullTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "getCommentNullTest");
    invoke();
  }

  /*
   * @testName: getDomainTest
   * 
   * @assertion_ids: Servlet:JAVADOC:439
   * 
   * @test_Strategy: Create a web application with no web.xml and one fragment;
   * Define everything in web-fragment.xml; Package everything in the fragment;
   * Client sends a version 0 and 1 cookie to the servlet. Servlet verifies
   * values of the cookies;
   */
  public void getDomainTest() throws Fault {
    // version 1
    TEST_PROPS.setProperty(REQUEST_HEADERS,
        "Cookie: $Version=1; name1=value1; $Domain=" + _hostname
            + "; $Path=/servlet_pluh_cookie_web");
    TEST_PROPS.setProperty(APITEST, "getDomainTest");
    invoke();

  }

  /*
   * @testName: getMaxAgeTest
   * 
   * @assertion_ids: Servlet:JAVADOC:443
   * 
   * @test_Strategy: Create a web application with no web.xml and one fragment;
   * Define everything in web-fragment.xml; Package everything in the fragment;
   * Servlet tests method getMaxAge;
   */
  public void getMaxAgeTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "getMaxAgeTest");
    invoke();
  }

  /*
   * @testName: getNameTest
   * 
   * @assertion_ids: Servlet:JAVADOC:448
   * 
   * @test_Strategy: Create a web application with no web.xml and one fragment;
   * Define everything in web-fragment.xml; Package everything in the fragment;
   * Client sends a version 0 and 1 cookie to a servlet; Servlet tests method
   * Cookie.getName
   */
  public void getNameTest() throws Fault {
    // version 0 Cookie
    TEST_PROPS.setProperty(REQUEST_HEADERS, "Cookie: name1=value1; Domain="
        + _hostname + "; Path=/servlet_pluh_cookie_web");
    TEST_PROPS.setProperty(APITEST, "getNameTest");
    invoke();

    // version 1 Cookie
    TEST_PROPS.setProperty(REQUEST_HEADERS,
        "Cookie: $Version=1; name1=value1; $Domain=" + _hostname
            + "; $Path=/servlet_pluh_cookie_web");
    TEST_PROPS.setProperty(APITEST, "getNameTest");
    invoke();
  }

  /*
   * @testName: getPathTest
   * 
   * @assertion_ids: Servlet:JAVADOC:445
   * 
   * @test_Strategy: Create a web application with no web.xml and one fragment;
   * Define everything in web-fragment.xml; Package everything in the fragment;
   * Client sends a version 1 cookie to a servlet; Servlet tests method getPath
   * using the received Cookie
   */
  public void getPathTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST_HEADERS,
        "Cookie: $Version=1; name1=value1; $Domain=" + _hostname
            + "; $Path=/servlet_pluh_cookie_web");
    TEST_PROPS.setProperty(APITEST, "getPathTest");
    invoke();
  }

  /*
   * @testName: getSecureTest
   * 
   * @assertion_ids: Servlet:JAVADOC:447
   * 
   * @test_Strategy: Create a web application with no web.xml and one fragment;
   * Define everything in web-fragment.xml; Package everything in the fragment;
   * Servlet tests method Cookie.getSecure;
   */
  public void getSecureTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "getSecureTest");
    invoke();
  }

  /*
   * @testName: getValueTest
   * 
   * @assertion_ids: Servlet:JAVADOC:450
   * 
   * @test_Strategy: Create a web application with no web.xml and one fragment;
   * Define everything in web-fragment.xml; Package everything in the fragment;
   * Client sends a version 0 and 1 cookie to a servlet; Servlet tests method
   * getValue and verify the right cookie received
   */
  public void getValueTest() throws Fault {
    // version 0 Cookie
    TEST_PROPS.setProperty(REQUEST_HEADERS, "Cookie: name1=value1; Domain="
        + _hostname + "; Path=/servlet_pluh_cookie_web");
    TEST_PROPS.setProperty(APITEST, "getValueTest");
    invoke();
    // version 1 Cookie
    TEST_PROPS.setProperty(REQUEST_HEADERS,
        "Cookie: $Version=1; name1=value1; $Domain=" + _hostname
            + "; $Path=/servlet_pluh_cookie_web");
    TEST_PROPS.setProperty(APITEST, "getValueTest");
    invoke();
  }

  /*
   * @testName: getVersionTest
   * 
   * @assertion_ids: Servlet:JAVADOC:451
   * 
   * @test_Strategy: Create a web application with no web.xml and one fragment;
   * Define everything in web-fragment.xml; Package everything in the fragment;
   * Client sends a version 0 and 1 cookie to a servlet; Servlet tests method
   * Cookie.getVersion and verify the right cookie received
   */
  public void getVersionTest() throws Fault {
    // version 0 Cookie
    TEST_PROPS.setProperty(REQUEST_HEADERS, "Cookie: name1=value1; Domain="
        + _hostname + "; Path=/servlet_pluh_cookie_web");
    TEST_PROPS.setProperty(APITEST, "getVersionVer0Test");
    invoke();
    // version 1 Cookie
    TEST_PROPS.setProperty(REQUEST_HEADERS,
        "Cookie: $Version=1; name1=value1; $Domain=" + _hostname
            + "; $Path=/servlet_pluh_cookie_web");
    TEST_PROPS.setProperty(APITEST, "getVersionVer1Test");
    invoke();
  }

  /*
   * @testName: setCommentTest
   * 
   * @assertion_ids: Servlet:JAVADOC:435
   * 
   * @test_Strategy: Create a web application with no web.xml and one fragment;
   * Define everything in web-fragment.xml; Package everything in the fragment;
   * Servlet tests method Cookie.setComment
   */
  public void setCommentTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "setCommentVer0Test");
    invoke();
    TEST_PROPS.setProperty(APITEST, "setCommentVer1Test");
    invoke();
  }

  /*
   * @testName: setDomainTest
   * 
   * @assertion_ids: Servlet:JAVADOC:438
   * 
   * @test_Strategy: Create a web application with no web.xml and one fragment;
   * Define everything in web-fragment.xml; Package everything in the fragment;
   * Servlet tests method Cookie.setDomain
   */
  public void setDomainTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "setDomainVer0Test");
    invoke();
    TEST_PROPS.setProperty(APITEST, "setDomainVer1Test");
    invoke();
  }

  /*
   * @testName: setMaxAgePositiveTest
   * 
   * @assertion_ids: Servlet:JAVADOC:440
   * 
   * @test_Strategy: Create a web application with no web.xml and one fragment;
   * Define everything in web-fragment.xml; Package everything in the fragment;
   * Servlet create Cookie and sets values using Cookie.setMaxAge(2) Cookie is
   * sent back to client and client verifies them
   */
  public void setMaxAgePositiveTest() throws Fault {
    // version 0 cookie
    String testName = "setMaxAgeVer0PositiveTest";
    HttpResponse response = null;
    String dateHeader = null;
    int index = -1;
    Date expiryDate = null;
    String body = null;

    HttpRequest request = new HttpRequest("GET " + getContextRoot() + "/"
        + getServletName() + "?testname=" + testName + " HTTP/1.1", _hostname,
        _port);

    try {
      response = request.execute();
      dateHeader = response.getResponseHeader("testDate").toString();
      CookieSpec spec = CookiePolicy.getCookieSpec(CookiePolicy.NETSCAPE);

      TestUtil
          .logTrace("Found " + response.getResponseHeaders("Set-Cookie").length
              + " set-cookie entry");

      boolean foundcookie = false;
      Header[] CookiesHeader = response.getResponseHeaders("Set-Cookie");
      int i = 0;
      while (i < CookiesHeader.length) {
        TestUtil.logTrace("Checking set-cookiei " + i + ":" + CookiesHeader[i]);
        Cookie[] cookies = spec.parse(".eng.com", _port, getServletName(),
            false, CookiesHeader[i]);
        index = findCookie(cookies, "name1");
        if (index >= 0) {
          expiryDate = cookies[index].getExpiryDate();
          body = response.getResponseBodyAsString();
          foundcookie = true;
          break;
        }
        i++;
      }

      if (!foundcookie) {
        throw new Fault("The test cookie was not located in the response");
      }
    } catch (Throwable t) {
      throw new Fault("Exception occurred:" + t);
    }

    // put expiry date into GMT
    SimpleDateFormat sdf = new SimpleDateFormat();
    sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
    String resultStringDate = sdf.format(expiryDate);
    try {
      Date resultDate = sdf.parse(resultStringDate);
      Date expectedDate = sdf
          .parse(dateHeader.substring(dateHeader.indexOf(": ") + 2).trim());
      if (resultDate.before(expectedDate)) {
        throw new Fault("The expiry date was incorrect, expected ="
            + expectedDate + ", result = " + resultDate);
      }
    } catch (Throwable t) {
      throw new Fault("Exception occurred: " + t);
    }

    if (body.indexOf(Data.PASSED) == -1) {
      throw new Fault("The string: " + Data.PASSED + " not found in response");
    }

    // version 1 cookie
    TEST_PROPS.setProperty(APITEST, "setMaxAgeVer1PositiveTest");
    TEST_PROPS.setProperty(EXPECTED_HEADERS,
        "Set-Cookie:name1=value1##Version=1##Max-Age=2");
    invoke();
  }

  /*
   * @testName: setMaxAgeZeroTest
   * 
   * @assertion_ids: Servlet:JAVADOC:442
   * 
   * @test_Strategy: Create a web application with no web.xml and one fragment;
   * Define everything in web-fragment.xml; Package everything in the fragment;
   * Servlet create Cookie and sets values using Cookie.setMaxAge(0) Cookie is
   * sent back to client and client verifies them
   */
  public void setMaxAgeZeroTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "setMaxAgeZeroVer0Test");
    TEST_PROPS.setProperty(EXPECTED_HEADERS, "Set-Cookie:name1=value1");
    invoke();

    TEST_PROPS.setProperty(APITEST, "setMaxAgeZeroVer1Test");
    TEST_PROPS.setProperty(EXPECTED_HEADERS,
        "Set-Cookie:name1=value1##Version=1##Max-Age=0");
    invoke();
  }

  /*
   * @testName: setMaxAgeNegativeTest
   * 
   * @assertion_ids: Servlet:JAVADOC:441
   * 
   * @test_Strategy: Create a web application with no web.xml and one fragment;
   * Define everything in web-fragment.xml; Package everything in the fragment;
   * Servlet create Cookie and sets values using Cookie.setMaxAge(-1) Cookie is
   * sent back to client and client verifies them
   */
  public void setMaxAgeNegativeTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "setMaxAgeNegativeVer0Test");
    TEST_PROPS.setProperty(EXPECTED_HEADERS,
        "Set-Cookie:name1=value1##!Expire");
    invoke();
    TEST_PROPS.setProperty(APITEST, "setMaxAgeNegativeVer1Test");
    TEST_PROPS.setProperty(EXPECTED_HEADERS,
        "Set-Cookie:name1=value1##Version=1##!Max-Age");
    invoke();
  }

  /*
   * @testName: setSecureTest
   * 
   * @assertion_ids: Servlet:JAVADOC:446
   * 
   * @test_Strategy: Create a web application with no web.xml and one fragment;
   * Define everything in web-fragment.xml; Package everything in the fragment;
   * Servlet create Version 0 and Version 1 Cookie and sets values using
   * Cookie.setSecure Cookie is sent back to client and client verifies them
   */
  public void setSecureTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "setSecureVer0Test");
    invoke();
    TEST_PROPS.setProperty(APITEST, "setSecureVer1Test");
    invoke();
  }

  /*
   * @testName: setValueTest
   * 
   * @assertion_ids: Servlet:JAVADOC:449
   * 
   * @test_Strategy: Create a web application with no web.xml and one fragment;
   * Define everything in web-fragment.xml; Package everything in the fragment;
   * Servlet create Version 0 and Version 1 Cookie and sets values using
   * Cookie.setValue Cookie is sent back to client and client verifies them
   */
  public void setValueTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "setValueVer0Test");
    invoke();
    TEST_PROPS.setProperty(APITEST, "setValueVer1Test");
    invoke();
  }

  /*
   * @testName: setVersionTest
   * 
   * @assertion_ids: Servlet:JAVADOC:452
   * 
   * @test_Strategy: Create a web application with no web.xml and one fragment;
   * Define everything in web-fragment.xml; Package everything in the fragment;
   * Servlet create Version 0 and Version 1 Cookie and sets values using
   * Cookie.setVersion Cookie is sent back to client and client verifies them
   */
  public void setVersionTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "setVersionVer0Test");
    invoke();
    TEST_PROPS.setProperty(APITEST, "setVersionVer1Test");
    invoke();
  }
}
