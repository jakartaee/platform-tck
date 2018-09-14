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
 * @(#)Client.java	1.14 03/05/16
 */

package com.sun.ts.tests.integration.sec.propagation;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import java.io.*;
import java.net.*;
import java.util.*;
import java.rmi.*;

import com.sun.javatest.Status;

public class Client extends EETest {
  // Configurable constants:
  private String protocol = "http";

  private String hostname = null;

  private int portnum = 0;

  private String pageBase = "/integration_sec_propagation_web";

  private String pageWebToEjbAuth = pageBase + "/web_to_ejb_auth.jsp";

  private String pageWebToEjbNoauth = pageBase + "/web_to_ejb_noauth.jsp";

  private String pageEjbToEjb = pageBase + "/ejb_to_ejb.jsp";

  private String username = "";

  private String password = "";

  private String authusername = "";

  private String authpassword = "";

  private String nobodyuser = "";

  // Constants:
  private final String WEBHOSTPROP = "webServerHost";

  private final String WEBPORTPROP = "webServerPort";

  private final String USERNAMEPROP = "user";

  private final String PASSWORDPROP = "password";

  private final String AUTHUSERNAMEPROP = "authuser";

  private final String AUTHPASSWORDPROP = "authpassword";

  private final String NOBODYUSERNAMEPROP = "nobodyuser";

  // Shared test variables:
  private Properties props = null;

  private String request = null;

  private WebUtil.Response response = null;

  private TSURL ctsurl = new TSURL();

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; user; password; authuser;
   * authpassword; nobodyuser;
   */

  public void setup(String[] args, Properties p) throws Fault {
    props = p;

    try {
      hostname = p.getProperty(WEBHOSTPROP);
      portnum = Integer.parseInt(p.getProperty(WEBPORTPROP));
      username = p.getProperty(USERNAMEPROP);
      password = p.getProperty(PASSWORDPROP);
      authusername = p.getProperty(AUTHUSERNAMEPROP);
      authpassword = p.getProperty(AUTHPASSWORDPROP);
      nobodyuser = p.getProperty(NOBODYUSERNAMEPROP);

      TestUtil.logMsg("setup complete");
    } catch (Exception e) {
      TestUtil.logErr("Error: got exception: ", e);
      throw new Fault("Got Exception " + e.getMessage() + " during setup.", e);
    }
  }

  /*
   * @testName: test_web_to_ejb_auth
   *
   * @assertion_ids: JavaEE:SPEC:25; JavaEE:SPEC:31
   *
   * @test_Strategy: 1. Send request for web_to_ejb_auth.jsp, a jsp page that is
   * set up for BASIC authentication. Pass in j2ee as username and password. 2.
   * web_to_ejb_auth.jsp looks up Bean1, a stateless session bean. 3.
   * web_to_ejb_auth.jsp calls bean1.getCallerPrincipalName(), which returns the
   * caller principal. 4. web_to_ejb_auth.jsp returns the caller principal as
   * the contents of the web page as follows. <value of
   * getUserPrincipal().getName()> <value of ejb.getCallerPrincipal().getName()>
   * <value of second call to getUserPrincipal()> 5. Receive response from
   * web_to_ejb_auth.jsp and ensure the principal output is "j2ee" for both the
   * jsp and the ejb.
   */

  public void test_web_to_ejb_auth() throws Fault {
    try {
      String web1Search = username;
      String ejbSearch = username;
      String web2Search = username;

      // Send request for web_to_ejb_auth.jsp, passing in j2ee as
      // username and password:
      request = pageWebToEjbAuth;
      TestUtil.logMsg("Sending request \"" + request + "\"");
      response = WebUtil.sendAuthenticatedRequest("GET",
          InetAddress.getByName(hostname), portnum, ctsurl.getRequest(request),
          null, null, username, password);

      // Check that the page was retrieved (no error)
      if (response.isError()) {
        TestUtil.logErr("Could not access " + request);
        throw new Fault("test failed.");
      }

      // Test to make sure we are authenticated properly by checking
      // the page content. The page should contain "j2ee"
      if (response.content.indexOf(web1Search) == -1) {
        TestUtil.logErr("Web1 Principal incorrect.  Page received: ");
        TestUtil.logErr(response.content);
        TestUtil.logErr("Should say: \"" + web1Search + "\")");
        throw new Fault("test failed.");
      }

      TestUtil.logMsg("Web User Principal correct.");

      // Test to make sure we are authenticated properly by checking
      // the page content. The page should contain "j2ee"
      if (response.content.indexOf(ejbSearch) == -1) {
        TestUtil.logErr("Ejb Principal incorrect.  Page received: ");
        TestUtil.logErr(response.content);
        TestUtil.logErr("Should say: \"" + ejbSearch + "\")");
        throw new Fault("test failed.");
      }
      TestUtil.logMsg("Ejb User Principal correct.");

      // Test to make sure we are still authenticated properly by
      // checking the page content. The page should contain "j2ee"
      if (response.content.indexOf(web2Search) == -1) {
        TestUtil.logErr("Web2 Principal incorrect.  Page received: ");
        TestUtil.logErr(response.content);
        TestUtil.logErr("Should say: \"" + web2Search + "\")");
        throw new Fault("test failed.");
      }

      TestUtil.logMsg("Web User Principal still correct.");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      throw new Fault("test failed: ", e);
    }
  }

  /*
   * @testName: test_web_to_ejb_noauth
   *
   * @assertion_ids: JavaEE:SPEC:25
   *
   * @test_Strategy: 1. Send request for web_to_ejb_noauth.jsp, a jsp page that
   * is set up with no authentication. 2. web_to_ejb_noauth.jsp looks up Bean1,
   * a stateless session bean. 3. web_to_ejb_noauth.jsp calls
   * bean1.getCallerPrincipalName(), which returns the caller principal. 4.
   * web_to_ejb_noauth.jsp returns the caller principal as the contents of the
   * web page as follows: <value of getUserPrincipal().getName()> call is
   * successful or not <value of second call to getUserPrincipal()> 5. Receive
   * response from web_to_ejb_noauth.jsp and ensure the principal output is null
   * for both calls in the jsp and the call to the ejb is successful.
   *
   * Note: The value returned for an unauthenticated caller from a call to
   * getRemoteUserPrincipal() in a servlet must be null, and an unauthenticated
   * caller can call an ejb successfully.
   */

  public void test_web_to_ejb_noauth() throws Fault {
    try {
      String web1Search = "null";
      String ejbSearch = "true";
      String web2Search = "null";

      // Send request for web_to_ejb_noauth.jsp
      request = pageWebToEjbNoauth;
      TestUtil.logMsg("Sending request \"" + request + "\"");
      response = WebUtil.sendRequest("GET", InetAddress.getByName(hostname),
          portnum, ctsurl.getRequest(request), null, null);

      // Check that the page was retrieved (no error)
      if (response.isError()) {
        TestUtil.logErr("Could not access " + request);
        throw new Fault("test failed.");
      }

      // Test to make sure we are authenticated properly by checking
      // the page content. The page should contain "null"
      if (response.content.indexOf(web1Search) == -1) {
        TestUtil.logErr("Web1 Principal incorrect.  Page received: ");
        TestUtil.logErr(response.content);
        TestUtil.logErr("Should say: \"" + web1Search + "\")");
        throw new Fault("test failed.");
      }

      TestUtil.logMsg("Web User Principal correct.");

      // Test to make sure we are authenticated properly by checking
      // the page content. The page should contain "guest"
      if (response.content.indexOf(ejbSearch) == -1) {
        TestUtil.logErr("Call to ejb Principal failed.  Page received: ");
        TestUtil.logErr(response.content);
        TestUtil.logErr("Should say: \"" + ejbSearch + "\")");
        throw new Fault("test failed.");
      }
      TestUtil.logMsg("Call to ejb successful.");

      // Test to make sure we are still authenticated properly by
      // checking the page content. The page should contain "null"
      if (response.content.indexOf(web2Search) == -1) {
        TestUtil.logErr("Web2 Principal incorrect.  Page received: ");
        TestUtil.logErr(response.content);
        TestUtil.logErr("Should say: \"" + web2Search + "\")");
        throw new Fault("test failed.");
      }

      TestUtil.logMsg("Web User Principal still correct.");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      throw new Fault("test failed: ", e);
    }
  }

  /*
   * @testName: test_ejb_to_ejb
   *
   * @assertion_ids: Servlet:SPEC:31
   *
   * @test_Strategy: 1. Send request for ejb_to_ejb.jsp, a jsp page that is set
   * up with HTTP-Basic authentication. This will establish the original caller
   * princial as javajoe. 2. ejb_to_ejb.jsp looks up Bean1, a stateless session
   * bean. 3. ejb_to_ejb.jsp calls bean1.getCallerPrincipalName(), which returns
   * the caller principal. 4. ejb_to_ejb.jsp calls
   * bean1.getPropagatedPrincipalName(), which looks up Bean2, another stateless
   * session bean, and makes a call to bean2.getCallerPrincipalName(), which
   * returns the caller principal after propagating. 5. ejb_to_ejb.jsp returns
   * the caller principal as the contents of the web page as follows: <value of
   * getUserPrincipal().getName()> <value of ejb.getCallerPrincipal().getName()>
   * <value of propagated principal> 6. Receive response from ejb_to_ejb.jsp and
   * ensure the principal output is javajoe for web, ejb1, and ejb2
   *
   * Note: The value returned from a call to getCallerPrincipal in an ejb should
   * match the value returned from a call to getCallerPrincipal in any ejb
   * called by the original ejb or in ejbs called by called ejbs.
   *
   */

  public void test_ejb_to_ejb() throws Fault {
    try {
      String webSearch = authusername;
      String ejb1Search = authusername;
      String ejb2Search = authusername;

      // Send request for ejb_to_ejb.jsp
      request = pageEjbToEjb;
      TestUtil.logMsg("Sending request \"" + request + "\"");
      response = WebUtil.sendAuthenticatedRequest("GET",
          InetAddress.getByName(hostname), portnum, ctsurl.getRequest(request),
          null, null, authusername, authpassword);

      // Check that the page was retrieved (no error)
      if (response.isError()) {
        TestUtil.logErr("Could not access " + request);
        throw new Fault("test failed.");
      }

      // Test to make sure we are authenticated properly by checking
      // the page content. The page should contain "javajoe"
      if (response.content.indexOf(webSearch) == -1) {
        TestUtil.logErr("Web Principal incorrect.  Page received: ");
        TestUtil.logErr(response.content);
        TestUtil.logErr("Should say: \"" + webSearch + "\")");
        throw new Fault("test failed.");
      }

      TestUtil.logMsg("Web User Principal correct.");

      // Test to make sure the caller principal propagated to the first
      // ejb. The page should contain "javajoe"
      if (response.content.indexOf(ejb1Search) == -1) {
        TestUtil.logErr("Principal did not propagate from web to "
            + "ejb.  Page received: ");
        TestUtil.logErr(response.content);
        TestUtil.logErr("Should say: \"" + ejb1Search + "\")");
        throw new Fault("test failed.");
      }
      TestUtil.logMsg("Principal propagated from web to ejb.");

      // Test to make sure the caller principal propagated from the first
      // ejb to the second ejb. The page should contain
      // "javajoe"
      if (response.content.indexOf(ejb2Search) == -1) {
        TestUtil.logErr("Principal did not propagate from ejb to "
            + "ejb.  Page received: ");
        TestUtil.logErr(response.content);
        TestUtil.logErr("Should say: \"" + ejb2Search + "\")");
        throw new Fault("test failed.");
      }

      TestUtil.logMsg("Principal propagated from ejb to ejb.");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      throw new Fault("test failed: ", e);
    }
  }

  public void cleanup() throws Fault {
    logMsg("cleanup complete");
  }

}
