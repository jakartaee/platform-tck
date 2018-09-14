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

package com.sun.ts.tests.jacc.web.containerContracts;

import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URLConnection;
import java.net.URL;
import java.util.Properties;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.lib.util.BASE64Encoder;
import com.sun.ts.lib.porting.TSHttpsURLConnection;
import com.sun.ts.lib.util.TSNamingContext;
import com.sun.ts.lib.porting.TSURL;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.harness.EETest.Fault;
import com.sun.ts.lib.harness.ServiceEETest;

import com.sun.javatest.Status;
import com.sun.ts.tests.jacc.util.LogFileProcessor;

public class Client extends ServiceEETest {

  private Properties props = null;

  private String hostname = null;

  private int portnum = 0;

  private String pageBase = "/jacc_web_containerContracts_web";

  private String securedPage = "/secured.jsp";

  private String sslProtectedPage = "/sslprotected.jsp";

  private String authusername = "";

  private String authpassword = "";

  private String username = "";

  private String password = "";

  private int securedPortNum = 0;

  private TSNamingContext nctx = null;

  private LogFileProcessor logProcessor = null;

  public static void main(String args[]) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /**
   * @class.setup_props: log.file.location; webServerHost; webServerPort;
   *                     authuser; authpassword; user; password;
   *                     securedWebServicePort; platform.mode;
   *                     porting.ts.HttpsURLConnection.class.1;
   *
   */
  public void setup(String[] args, Properties p) throws Fault {
    props = p;

    try {
      hostname = p.getProperty("webServerHost");
      portnum = Integer.parseInt(p.getProperty("webServerPort"));
      securedPortNum = Integer.parseInt(p.getProperty("securedWebServicePort"));
      authusername = p.getProperty("authuser");
      authpassword = p.getProperty("authpassword");
      username = p.getProperty("user");
      password = p.getProperty("password");

      nctx = new TSNamingContext();

      // create LogFileProcessor
      logProcessor = new LogFileProcessor(props);
      // logProcessor = new LogFileProcessor();

      // Retrieve logs
      logProcessor.fetchLogs("pullAllLogRecords|fullLog");

    } catch (Exception e) {
      logErr("Error: got exception: ", e);
    }

  }

  public void cleanup() throws Fault { //
  }

  /**
   * @testName: IsUserInRole
   * 
   * @assertion_ids: JACC:SPEC:65; JACC:SPEC:32; JACC:SPEC:75
   * 
   * @test_Strategy: 1. Register TS provider with the AppServer.
   * 
   *                 (Note: TSProvider is the delegating policy provider
   *                 supplied with compatibility test suite. See User guide for
   *                 Registering TS Provider with your AppServer ).
   *
   *                 2. Deploy a jsp secured.jsp which is accessible by a role
   *                 Administrator.
   *
   *                 3. Assign javajoe to role Administrator and access the jsp
   * 
   *                 4. verify the rolename by calling isUserInRole() inside
   *                 secured.jsp
   * 
   */
  public void IsUserInRole() throws Fault {

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

      TestUtil.logMsg("HttpURLconnection established to :" + newURL.toString());

      // set request property
      urlConn.setRequestProperty("Authorization",
          "Basic " + encodedAuthData.trim());

      InputStream content = (InputStream) urlConn.getInputStream();
      BufferedReader in = new BufferedReader(new InputStreamReader(content));

      String output = "";
      String line;
      while ((line = in.readLine()) != null) {
        output = output + line;
        TestUtil.logMsg(line);
      }

      TestUtil.logMsg("Data received" + output);

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
      TestUtil.logErr(e.getMessage(), e);
      TestUtil.printStackTrace(e);
      throw new Fault("IsUserInRole : FAILED", e);
    }
  }

  /**
   * @testName: WebUserDataPermission
   *
   * @assertion_ids: JACC:SPEC:71; JACC:SPEC:117; JACC:SPEC:104; JACC:SPEC:113
   *
   * @test_Strategy: 1. Register TS provider with the AppServer. (See User guide
   *                 for Registering TS Provider with your AppServer ).
   *
   *                 2. Deploy a jsp called (sslprotected.jsp) with a security
   *                 constraint that has a user-data-constraint
   *                 <transport-guarantee>CONFIDENTIAL</transport-guarantee>
   *
   *                 3. Send https request to sslprotected.jsp, access the
   *                 content of sslprotected.jsp
   *
   *                 JSPName URL ---------------------------------
   *                 sslprotecd.jsp /sslprotected.jsp
   *
   */
  public void WebUserDataPermission() throws Fault {

    boolean verified = false;

    TSURL ctsurl = new TSURL();

    String url = ctsurl.getURLString("https", hostname, securedPortNum,
        pageBase + sslProtectedPage);
    try {
      URL newURL = new URL(url);

      // Encode authData
      String authData = authusername + ":" + authpassword;
      TestUtil.logMsg("authData : " + authData);

      BASE64Encoder encoder = new BASE64Encoder();

      String encodedAuthData = encoder.encode(authData.getBytes());
      TestUtil.logMsg("encoded authData : " + encodedAuthData);

      // open HttpsURLConnection
      TSHttpsURLConnection httpsURLConn = new TSHttpsURLConnection();

      if (httpsURLConn != null) {
        TestUtil.logMsg("WebUserDataPermission():  hostname = " + hostname);
        TestUtil.logMsg(
            "WebUserDataPermission():  securedPortNum = " + securedPortNum);
        TestUtil.logMsg("WebUserDataPermission():  pageBase = " + pageBase);
        TestUtil.logMsg(
            "WebUserDataPermission():  sslProtectedPage = " + sslProtectedPage);
        TestUtil.logMsg(
            "WebUserDataPermission():  url.toString() = " + url.toString());
        TestUtil.logMsg("WebUserDataPermission():  newURL.toString() = "
            + newURL.toString());

        TestUtil
            .logMsg("Opening https url connection to: " + newURL.toString());
        httpsURLConn.init(newURL);
        httpsURLConn.setDoInput(true);
        httpsURLConn.setDoOutput(true);
        httpsURLConn.setUseCaches(false);

      } else {
        throw new Fault("Error opening httsURLConnection"); // set request
                                                            // property
      }
      httpsURLConn.setRequestProperty("Authorization",
          "Basic " + encodedAuthData.trim());

      InputStream content = (InputStream) httpsURLConn.getInputStream();

      BufferedReader in = new BufferedReader(new InputStreamReader(content));

      String output = "";
      String line;
      while ((line = in.readLine()) != null) {
        output = output + line;
        TestUtil.logMsg(line);
      }

      // check for the occurance of the authusername
      // in the output this ensures that the authorized user is
      // able to access the resource secured.jsp
      String stringToSearch = authusername;
      if (output.indexOf(stringToSearch) == -1) {
        throw new Fault("PermissionToRole: getRemoteUser(): "
            + "- did not find \"" + stringToSearch + "\" in log.");
      } else {
        TestUtil.logMsg("User javajoe accessed sslprotected.jsp");
      }
    } catch (Exception e) {
      throw new Fault("WebUserDataPermission : FAILED", e);
    }
  }

  /**
   * @testName: WebResourcePermission
   *
   * @assertion_ids: JACC:SPEC:73; JACC:SPEC:117; JACC:SPEC:76
   *
   * @test_Strategy: 1. Register TS provider with the AppServer. (See User guide
   *                 for Registering TS Provider with your AppServer ).
   *
   *                 2. Deploy a jsp called (secured.jsp) configure it to be
   *                 accessible only by Role Administrator
   *
   *                 3. Access secured.jsp with a user(j2ee) who is not in role
   *                 Administrator
   *
   *                 4. expect proper Http error message.
   *
   *                 JSPName URL --------------------------------- secured.jsp
   *                 /secured.jsp
   *
   */
  public void WebResourcePermission() throws Fault {

    TSURL ctsurl = new TSURL();

    String url = ctsurl.getURLString("http", hostname, portnum,
        pageBase + securedPage);
    try {
      URL newURL = new URL(url);

      // Encode authData
      String authData = username + ":" + password;
      TestUtil.logMsg("authData : " + authData);

      BASE64Encoder encoder = new BASE64Encoder();

      String encodedAuthData = encoder.encode(authData.getBytes());
      TestUtil.logMsg("encoded authData : " + encodedAuthData);

      // open URLConnection
      URLConnection urlConn = newURL.openConnection();

      // set request property with user j2ee
      // who is not in the Role Administrator
      urlConn.setRequestProperty("Authorization",
          "Basic " + encodedAuthData.trim());

      InputStream content = (InputStream) urlConn.getInputStream();

      BufferedReader in = new BufferedReader(new InputStreamReader(content));

      String output = "";
      String line;
      while ((line = in.readLine()) != null) {
        output = output + line;
        TestUtil.logMsg(line);
      }

      // Control shouldn't reach this point
      throw new Fault("WebResourcePermission test failed :"
          + "User j2ee allowed to access secured.jsp");

    } catch (IOException e) {
      TestUtil.printStackTrace(e);

      TestUtil.logMsg("Got expected IOException : "
          + "user j2ee not allowed to access secured.jsp ");
      TestUtil.logMsg("WebResourcePermission test passed");
    }

  }
}
