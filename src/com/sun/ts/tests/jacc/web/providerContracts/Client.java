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

package com.sun.ts.tests.jacc.web.providerContracts;

import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URLConnection;
import java.net.URL;
import java.util.Properties;
import java.util.Collection;
import java.util.Iterator;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.lib.util.BASE64Encoder;
import com.sun.ts.lib.util.TSNamingContext;
import com.sun.ts.lib.porting.TSURL;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.harness.EETest.Fault;
import com.sun.ts.lib.harness.ServiceEETest;

import com.sun.javatest.Status;
import com.sun.ts.tests.jacc.util.LogFileProcessor;
import com.sun.ts.tests.jacc.util.LogRecordEntry;

//import sun.misc.*;
public class Client extends ServiceEETest {

  private Properties props = null;

  private String contextId = "jacc_ctx";

  private String hostname = null;

  private int portnum = 0;

  private String pageBase = "/jacc_web_providerContracts_web";

  private String securedPage = "/secured.jsp";

  private String accessToAllPage = "/AccessToAll.jsp";

  private String anyAuthUserPage = "/anyauthuser.jsp";

  private String authusername = "";

  private String authpassword = "";

  private TSNamingContext nctx = null;

  LogFileProcessor logProcessor = null;

  public static void main(String args[]) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /**
   * @class.setup_props: log.file.location; webServerHost; webServerPort;
   *                     authuser; authpassword;
   * 
   */
  public void setup(String[] args, Properties p) throws Fault {
    props = p;
    boolean pass = true;

    try {
      hostname = p.getProperty("webServerHost");
      portnum = Integer.parseInt(p.getProperty("webServerPort"));
      authusername = p.getProperty("authuser");
      authpassword = p.getProperty("authpassword");

      nctx = new TSNamingContext();

      // create LogProcessor
      logProcessor = new LogFileProcessor(props);

      // Retrieve logs that matches with contextId
      logProcessor.fetchLogs(contextId);

    } catch (Exception e) {
      logErr("Error: got exception: ", e);
    }

  }

  public void cleanup() throws Fault {
    //
  }

  /**
   * @testName: WildCardAuthConstraint
   *
   * @assertion_ids: JACC:SPEC:35; JACC:SPEC:10; JACC:JAVADOC:19;
   *                 JACC:JAVADOC:25; JACC:JAVADOC:26; JACC:JAVADOC:27;
   *                 JACC:JAVADOC:30; JACC:JAVADOC:31; JACC:SPEC:129;
   *
   * @test_Strategy: 1. Register TS provider with the AppServer. (See User guide
   *                 for Registering TS Provider with your AppServer ).
   *
   *                 2. Deploy a jsp AccessToAll.jsp which contains a wildcard
   *                 auth constraint (i.e "*" as shown below) in its security
   *                 constraint. <auth-constraint> <role-name>*</role-name>
   *                 </auth-constraint>
   *
   *                 3. Access the jsp /AccessToAll.jsp from the client.
   *
   *                 4. Make sure the login user javajoe is able to access the
   *                 jsp /AccessToAll.jsp
   *
   *                 4. Make sure the login user is mapped to all roles defined
   *                 in the application (i.e. ADM, EMP and MGR) i.e a)
   *                 isUserInRole("ADM") should return true and b)
   *                 isUserInRole("MGR") should return true and c)
   *                 isUserInRole("EMP") should return true
   *
   *                 5. Make sure the login user is not in the role that is not
   *                 defined in the application. i.e isUserInRole("VP") should
   *                 return false
   */
  public void WildCardAuthConstraint() throws Fault {

    TSURL ctsurl = new TSURL();

    String url = ctsurl.getURLString("http", hostname, portnum,
        pageBase + accessToAllPage);
    try {
      URL newURL = new URL(url);

      // Encode authData
      String authData = authusername + ":" + authpassword;
      TestUtil.logMsg("authData : " + authData);

      BASE64Encoder encoder = new BASE64Encoder();

      String encodedAuthData = encoder.encode(authData.getBytes());
      TestUtil.logMsg("encoded authData : " + encodedAuthData);

      // open URLConnection
      URLConnection urlConn = newURL.openConnection();

      // set request property
      urlConn.setRequestProperty("Authorization",
          "Basic " + encodedAuthData.trim());

      InputStream content = (InputStream) urlConn.getInputStream();

      String cookies = null;

      for (int i = 0;; i++) {
        String header = urlConn.getHeaderField(i);
        // TestUtil.logMsg("Header "+i+"= "+header);

        if (header == null) {
          break;
        }
        if ((header.indexOf("JSESSIONIDSSO") != -1)
            || (header.indexOf("JSESSIONID") != -1)) {
          cookies = addCookies(header, cookies);
        }
      }

      TestUtil.logMsg("cookies received = " + cookies);

      BufferedReader in = new BufferedReader(new InputStreamReader(content));

      String output = "";
      String line;
      while ((line = in.readLine()) != null) {
        output = output + line;
        TestUtil.logMsg(line);
      }

      // check for the occurance of the authusername
      // in the output this ensures that the authorized user is
      // able to access the resource AccessToAll.jsp
      String stringToSearch = authusername;
      if (output.indexOf(stringToSearch) == -1) {
        throw new Fault("AccessToAll.jsp: getRemoteUser(): "
            + "- did not find \"" + stringToSearch + "\" in log.");
      } else {
        TestUtil.logMsg("User javajoe accessed  AccessToAll.jsp"); // look for
                                                                   // String
                                                                   // "USR_IN_ROLE_ADM"
      }
      stringToSearch = "USR_IN_ROLE_ADM";
      if (output.indexOf(stringToSearch) == -1) {
        throw new Fault(
            "AccessToAll.jsp: getRemoteUser(): " + "- not mapped to role ADM");
      } else {
        TestUtil.logMsg("User in Role ADM"); // look for String
                                             // "USR_IN_ROLE_MGR"
      }
      stringToSearch = "USR_IN_ROLE_MGR";
      if (output.indexOf(stringToSearch) == -1) {
        throw new Fault(
            "AccessToAll.jsp: getRemoteUser(): " + "- not mapped to role MGR");
      } else {
        TestUtil.logMsg("User in Role MGR"); // look for String
                                             // "USR_IN_ROLE_EMP"
      }
      stringToSearch = "USR_IN_ROLE_EMP";
      if (output.indexOf(stringToSearch) == -1) {
        throw new Fault(
            "AccessToAll.jsp: getRemoteUser(): " + "- not mapped to role EMP");
      } else {
        TestUtil.logMsg("User in Role EMP"); // look for String
                                             // "USR_NOT_IN_ROLE_VP"
      }
      stringToSearch = "USR_NOT_IN_ROLE_VP";
      if (output.indexOf(stringToSearch) == -1) {
        throw new Fault(
            "AccessToAll.jsp: getRemoteUser(): " + "- mapped to role VP");
      } else {
        TestUtil.logMsg("User not in Role VP");
      }

      // to test assertion JACC:SPEC:129
      stringToSearch = "USR_IN_ROLE_STARSTAR";
      if (output.indexOf(stringToSearch) == -1) {
        // should get here since we did not create any role-ref or role named
        // "**".
        TestUtil.logMsg("User is correctly not in any Role named '**'");
      } else {
        throw new Fault(
            "anyauthuser.jsp: getRemoteUser() - incorrectly mapped to role named '**'");
      }

    } catch (Exception e) {
      throw new Fault("Test WildCardAuthConstraint : FAILED", e);
    }

  }

  /**
   * @testName: PermissionsToRole
   * 
   * @assertion_ids: JACC:SPEC:65; JACC:SPEC:10; JACC:JAVADOC:19;
   *                 JACC:JAVADOC:25; JACC:JAVADOC:26; JACC:JAVADOC:27;
   *                 JACC:JAVADOC:30; JACC:JAVADOC:31
   * 
   * @test_Strategy: 1. Register TS provider with the AppServer. (See User guide
   *                 for Registering TS Provider with your AppServer ).
   *
   *                 2. Deploy a jsp secured.jsp which is accessible by a role
   *                 Administrator.
   *
   *                 3. Assign javajoe to role Administrator and access the jsp
   * 
   *                 4. If javajoe can access secured.jsp this implies all users
   *                 mapped to Administrator can access secured.jsp
   * 
   *                 Note: Invoking PermissionsToRole assumes that the
   *                 TSProvider was alreaded loaded and this triggers indirectly
   *                 invoking other JAVADOC APIs
   */
  public void PermissionsToRole() throws Fault {

    TSURL ctsurl = new TSURL();

    String url = ctsurl.getURLString("http", hostname, portnum,
        pageBase + securedPage);
    try {
      URL newURL = new URL(url);

      // Encode authData
      String authData = authusername + ":" + authpassword;
      TestUtil.logMsg("authData : " + authData);

      BASE64Encoder encoder = new BASE64Encoder();

      String encodedAuthData = encoder.encode(authData.getBytes());
      TestUtil.logMsg("encoded authData : " + encodedAuthData);

      // open URLConnection
      URLConnection urlConn = newURL.openConnection();

      // set request property
      urlConn.setRequestProperty("Authorization",
          "Basic " + encodedAuthData.trim());

      InputStream content = (InputStream) urlConn.getInputStream();

      String cookies = null;

      for (int i = 0;; i++) {
        String header = urlConn.getHeaderField(i);
        // TestUtil.logMsg("Header "+i+"= "+header);

        if (header == null) {
          break;
        }
        if ((header.indexOf("JSESSIONIDSSO") != -1)
            || (header.indexOf("JSESSIONID") != -1)) {
          cookies = addCookies(header, cookies);
        }
      }

      TestUtil.logMsg("cookies received = " + cookies);

      BufferedReader in = new BufferedReader(new InputStreamReader(content));

      String output = "";
      String line;
      while ((line = in.readLine()) != null) {
        output = output + line;
        // TestUtil.logMsg (line);
      }

      // check for the occurance of the authusername
      // in the output this ensures that the authorized user is
      // able to access the resource secured.jsp
      String stringToSearch = authusername;
      if (output.indexOf(stringToSearch) == -1) {
        throw new Fault("PermissionToRole: getRemoteUser(): "
            + "- did not find \"" + stringToSearch + "\" in log.");
      } else {
        TestUtil.logMsg("User javajoe accessed secured.jsp");
      }
    } catch (Exception e) {
      throw new Fault("Test PermissionToRole : FAILED", e);
    }

  }

  /**
   * @testName: anyAuthUserWebResPermAddedToRole
   * 
   * @assertion_ids: JACC:SPEC:128;
   * 
   * @test_Strategy: 1. Register TS provider with the AppServer. (See User guide
   *                 for Registering TS Provider with your AppServer ).
   *
   *                 2. deploy contents of this test dir (this test is most
   *                 concnerned with deployment of anyauthuser.jsp which
   *                 specifies role "**" in the auth-constraint element. This
   *                 should cause a WebResourcePermission to be added to the
   *                 "**" role.)
   * 
   *                 3. attempt to access the deployed anyauthuser.jsp
   * 
   *                 4. At this point, there should be an entry in the
   *                 JACCLog.txt file that begins with "MSG_TAG" and it shold
   *                 contain a message about the WebResourcePermission being
   *                 added for role "**" with page anyauthuser being referenced.
   *                 We will parse the entries of the JACCLog.txt and search for
   *                 our string info that indicates proper message info was
   *                 dumped for the servlet that had an auth-constraint = "**".
   * 
   */
  public void anyAuthUserWebResPermAddedToRole() throws Fault {

    boolean bSuccess = false;

    try {

      // lets invoke our doc to make sure deploymenbt did occur and that
      // translation of the DD security elements did occur. If they did
      // not properly occur, we should not be able to invoke the servlet
      // and that means the rest of our tests is going to fail.
      String servletPath = pageBase + anyAuthUserPage;
      invokeServlet(servletPath, "POST");

      //
      // ok, now validate the log file had content we expected to see
      // this is done in the following steps:
      //

      // 1. get all log messages needed to test assertion 128 (they all
      // shoudl start with "MSG_TAG". the ones containing info about
      // WebResourcePermission are specific to assertion JACC:SPEC:128
      String searchStr = "MSG_TAG :: WebResourcePermission :: **";
      Collection<LogRecordEntry> col = logProcessor.getMsgTagRecordCollection();
      TestUtil.logMsg("col.size() = " + col.size());

      // 2. find one of these log messages that matches our "anyauthuser"
      // servlet
      // and was that resulted from this appContext/pageBase
      Iterator iterator = col.iterator();

      while (iterator.hasNext()) {
        LogRecordEntry recordEntry = (LogRecordEntry) iterator.next();
        String msg = recordEntry.getMessage();
        if ((msg != null) && msg.startsWith(searchStr)
            && (msg.indexOf("anyauthuser") > 0)) {
          // SUCCESS: we found a matching record entry which means
          // a WebResourcePermission was added to Role "**" for our
          // "anyauthuser" servlet that specifies "**" in its auth-constraint
          bSuccess = true;
        }
      }
    } catch (Exception ex) {
      ex.printStackTrace();
      throw new Fault("Test anyAuthUserWebResPermAddedToRole : FAILED", ex);
    }

    if (bSuccess) {
      TestUtil.logMsg("Test anyAuthUserWebResPermAddedToRole Passed.");
    } else {
      throw new Fault("Test anyAuthUserWebResPermAddedToRole : FAILED");
    }
  }

  /*
   * Convenience method that will establish a url connections and perform a
   * get/post request. A username and password will be passed in the request
   * header and they will be encoded using the BASE64Encoder class.
   */
  private int invokeServlet(String sContext, String requestMethod) {
    int code = 200;

    TSURL ctsurl = new TSURL();
    if (!sContext.startsWith("/")) {
      sContext = "/" + sContext;
    }

    String url = ctsurl.getURLString("http", hostname, portnum, sContext);
    try {
      URL newURL = new URL(url);

      // Encode authData
      // hint: make sure username and password are valid for your
      // (J2EE) security realm otherwise you recieve http 401 error.
      String authData = authusername + ":" + authpassword;
      TestUtil.logMsg("authData : " + authData);

      BASE64Encoder encoder = new BASE64Encoder();

      String encodedAuthData = encoder.encode(authData.getBytes());
      TestUtil.logMsg("encoded authData : " + encodedAuthData);

      // open URLConnection
      HttpURLConnection conn = (HttpURLConnection) newURL.openConnection();

      // set request property
      conn.setDoOutput(true);
      conn.setDoInput(true);
      conn.setRequestProperty("Authorization",
          "Basic " + encodedAuthData.trim());
      conn.setRequestMethod(requestMethod); // POST or GET etc
      conn.connect();

      TestUtil.logMsg("called HttpURLConnection.connect() for url: " + url);
      code = conn.getResponseCode();
      TestUtil.logMsg("Got response code of: " + code);
      String str = conn.getResponseMessage();
      TestUtil.logMsg("Got response string of: " + str);

      /*
       * // DEBUG AID InputStream content = (InputStream)conn.getInputStream();
       * BufferedReader in = new BufferedReader(new InputStreamReader(content));
       * try { String line; while ((line = in.readLine()) != null) {
       * TestUtil.logMsg(line); } } finally { in.close(); }
       */
    } catch (Exception e) {
      TestUtil.logMsg(
          "Abnormal return status encountered while invoking " + sContext);
      TestUtil.logMsg("Exception Message was:  " + e.getMessage());
      e.printStackTrace();
    }

    return code;
  } // invokeServlet()

  public String addCookies(String cookieHeader, String cookies) {

    String cookie;

    if (cookieHeader == null) {
      return null;
    }

    int j = cookieHeader.indexOf(";");

    if (j != -1) {
      String cValue = cookieHeader.substring(0, j);
      cookie = cValue.trim();
    } else {
      cookie = cookieHeader.trim(); // append cookie with existing cookies
    }
    if (cookies == null) {
      cookies = cookie;
    } else {
      cookies += ";" + cookie;
    }
    return cookies;
  }
}
