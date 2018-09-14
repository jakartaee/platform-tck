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

package com.sun.ts.tests.jacc.web.principal2role;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.lib.util.BASE64Encoder;
import com.sun.ts.lib.util.TSNamingContext;
import com.sun.ts.lib.porting.TSURL;
import com.sun.ts.lib.harness.ServiceEETest;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URLConnection;
import java.net.URL;
import java.util.Properties;
import javax.naming.InitialContext;
import javax.naming.Context;
import com.sun.javatest.Status;

//import sun.misc.BASE64Encoder;
public class Client extends ServiceEETest {
  // Shared test variables:
  private Properties props = null;

  private String pageSec = null;

  private String pageSec1 = null;

  private String ctxroot = "/jacc_web_principal2role_first_module_web";

  private String ctxroot1 = "/jacc_web_principal2role_second_module_web";

  // two different web resources
  private String pageJspSec = ctxroot + "/first_resource.jsp";

  private String pageJspSec1 = ctxroot1 + "/second_resource.jsp";

  private String hostname = null;

  private int portnum = 0;

  private String unauthusername = null;

  private String unauthpassword = null;

  private String username = null;

  private String password = null;

  private TSNamingContext nctx = null;

  public static void main(String[] args) {

    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; user; password; authuser;
   * authpassword;
   *
   */
  public void setup(String[] args, Properties p) throws Fault {

    try {
      hostname = p.getProperty("webServerHost");
      portnum = Integer.parseInt(p.getProperty("webServerPort"));

      // j2ee is an authorized user
      username = p.getProperty("user");
      password = p.getProperty("password");

      // javajoe is an unauthorized user
      unauthusername = p.getProperty("authuser");
      unauthpassword = p.getProperty("authpassword");

      pageSec = pageJspSec;
      pageSec1 = pageJspSec1;

      nctx = new TSNamingContext();

    } catch (Exception e) {
      logErr("Error in setup: ", e);
    }
  }

  /*
   * @testName: PrincipalToRoleMapping
   *
   * @assertion_ids: JACC:SPEC:29; JACC:SPEC:124
   *
   * @test_Strategy: 1) Create a application ear file with two web modules
   * containing one webresource each. 2) In web module one
   * (jacc_principal2role_first_module_web.war) allow role-name Administrator to
   * access the resource(first_resource.jsp) 3) In web module two
   * (jacc_principal2role_second_module_web.war) allow role-name Administrator
   * to access the resource(second_resource.jsp) 4) Set the following
   * principal-to-role mapping for the application <security-role-mapping>
   * <role-name>Administrator</role-name> <principal-name>j2ee</principal-name>
   * </security-role-mapping> <security-role-mapping>
   * <role-name>Manager</role-name> <principal-name>javajoe</principal-name>
   * </security-role-mapping> 5) Verify the same principal-to-role mapping is
   * applied to both web modules by performing step6 and 7 6) Verify this by
   * accessing webresource one(first_resource.jsp) from module one using
   * authorized user(j2ee), make sure user j2ee is allowed access. Try accessing
   * webresource one (first_resource.jsp) using unauthorized user(javajoe), make
   * sure user javajoe is not allowed access this resource. 7) Repeat the step 6
   * for webresource two(second_resource.jsp)
   *
   */
  public void PrincipalToRoleMapping() throws Fault {

    TSURL ctsurl = new TSURL();

    String firstURLstr = ctsurl.getURLString("http", hostname, portnum,
        pageSec);
    String secondURLstr = ctsurl.getURLString("http", hostname, portnum,
        pageSec1);

    try {
      URL firstURL = new URL(firstURLstr);
      URL secondURL = new URL(secondURLstr);

      // Checking accessibility for a valid user to firstURL
      TestUtil.logMsg("Verifying access rights");
      TestUtil.logMsg("***********************");
      TestUtil.logMsg("Authorized user " + username + " invoking " + firstURL);
      if (isAccessible(firstURL, username, password)) {
        TestUtil.logMsg("Access allowed");
      } else {
        throw new Fault("Authorized user acesss denied");
      }

      // Checking accessibility for an unauthorized user to firstURL
      TestUtil.logMsg(
          "Unauthorized user " + unauthusername + " invoking " + firstURL);
      if (!isAccessible(firstURL, unauthusername, unauthpassword)) {
        TestUtil.logMsg("Access denied");
      } else {
        throw new Fault("Unauthorized user access allowed");
      }

      // Checking accessibility for a valid user to SecondURL
      TestUtil.logMsg("Authorized user " + username + " invoking " + secondURL);
      if (isAccessible(secondURL, username, password)) {
        TestUtil.logMsg("Access allowed");
      } else {
        throw new Fault("Authorized user acesss denied");
      }

      // Checking accessibility for an unauthorized user to secondURL
      TestUtil.logMsg(
          "Unauthorized user " + unauthusername + " invoking " + secondURL);
      if (!isAccessible(secondURL, unauthusername, unauthpassword)) {
        TestUtil.logMsg("Access denied");
      } else {
        throw new Fault("Unauthorized user access allowed");
      }

      TestUtil.logMsg(
          "Same PrincipalToRoleMapping applied for both " + "web modules");

    } catch (Exception e) {
      TestUtil.logMsg("Test PrincipalToRoleMapping failed");
      e.printStackTrace();
      throw new Fault("Test PrincipalToRoleMapping failed");
    }
  }

  public void cleanup() throws Fault {
    //
  }

  public boolean isAccessible(URL url, String user, String pwd) {

    try {
      // Encode authData
      String authData = user + ":" + pwd;
      // TestUtil.logMsg("authData : "+authData);

      BASE64Encoder encoder = new BASE64Encoder();
      String encodedAuthData = encoder.encode(authData.getBytes());
      // TestUtil.logMsg("encoded authData : "+ encodedAuthData);

      // open URLConnection
      URLConnection urlConn = url.openConnection();

      // set request property
      urlConn.setRequestProperty("Authorization",
          "Basic " + encodedAuthData.trim());

      InputStream content = (InputStream) urlConn.getInputStream();

      BufferedReader in = new BufferedReader(new InputStreamReader(content));

      String output = "";
      String line;
      // TestUtil.logMsg("Output from "+url);
      while ((line = in.readLine()) != null) {
        output = output + line;
        // TestUtil.logMsg(line);
      }

      // check for the occurance of the user
      // in the output this ensures that the authorized user is
      // able to access the given url
      String stringToSearch = user;
      if (output.indexOf(stringToSearch) == -1) {
        return false;
      }
      return true;
    } catch (Exception e) {
      // e.printStackTrace();
      return false;
    }
  }
}
