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
 *  $Id$
 */

package com.sun.ts.tests.servlet.ee.spec.security.runAs;

import com.sun.ts.lib.util.WebUtil;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.porting.TSURL;
import com.sun.javatest.Status;
import java.util.Properties;
import java.net.InetAddress;

public class Client extends EETest {
  // Configurable constants:
  private String protocol = "http";

  private String hostname = null;

  private int portnum = 0;

  private String ctxroot = "/first_module_cntxt_root";

  private String ctxroot1 = "/second_module_cntxt_root";

  private String pageWebToEjbDDRunAs = ctxroot + "/ServletOneTest";

  private String pageWebToEjbAnnotationRunAs = ctxroot1 + "/ServletTwoTest";

  private String username = "";

  private String password = "";

  private String authusername = "";

  private String authpassword = "";

  // Constants:
  private final String WEBHOSTPROP = "webServerHost";

  private final String WEBPORTPROP = "webServerPort";

  private final String USERNAMEPROP = "user";

  private final String PASSWORDPROP = "password";

  private final String AUTHUSERNAMEPROP = "authuser";

  private final String AUTHPASSWORDPROP = "authpassword";

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
   * authpassword;
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

      TestUtil.logMsg("setup complete");
    } catch (Exception e) {
      TestUtil.logErr("Error: got exception: ", e);
      throw new Fault("Got Exception " + e.getMessage() + " during setup.", e);
    }
  }

  /*
   * @testName: web_to_ejb_dd_runAs
   *
   * @assertion_ids: Servlet:SPEC:190; JavaEE:SPEC:31; JavaEE:SPEC:30
   *
   * @test_Strategy: 1. Configure a servlet(ServletOne) with the following
   * configurations a) BASIC authentication b) accessible only by rolename
   * "Administrator" c) use deployment descriptor in ServletOne to configure it
   * to runAs rolename "Manager" (use principal name javajoe for this)
   *
   * 2. Configure SecTestEJB.getCallerPrincipalName() to be be accessible only
   * by role name "Manager" using annotation.
   *
   * 3. Send a http request and invoke ServletOne with j2ee as username and
   * password. ( Note: j2ee is mapped to rolename "Administrator" )
   *
   * 4. From ServletOne look up SecTestEJB a stateless session bean and invoke a
   * method getCallerPrincipalName() on it
   *
   * 5. From within ServletOne obtain the CallerPrincipal using
   * request.getUserPrincipal().getName()
   *
   * 6. From within SecTestEJB obtain the CallerPrincipal using
   * sessionContext.getCallerPrincipal().getName()
   *
   * 7. From the client make sure ServletOne is accessed as user "j2ee" and
   * SecTestEJB accessed as user "javajoe" ( Note:User "javajoe" is mapped to
   * rolename "Manager")
   */

  public void web_to_ejb_dd_runAs() throws Fault {
    try {
      String webSearch = username;
      String ejbSearch = authusername;

      TestUtil.logMsg("Invoking ServletOne as User :" + username);

      // Send request for ServletOne, passing in j2ee as
      // username and password:
      request = pageWebToEjbDDRunAs;
      TestUtil.logMsg("Sending request \"" + request + "\"");
      response = WebUtil.sendAuthenticatedRequest("GET",
          InetAddress.getByName(hostname), portnum, ctsurl.getRequest(request),
          null, null, username, password);

      // Check that the page was retrieved (no error)
      if (response.isError()) {
        TestUtil.logErr("Could not access " + request);
        throw new Fault("test failed.");
      }

      TestUtil.logMsg("Received following Response \n");
      TestUtil.logMsg(response.content);

      // Test to make sure we are authenticated properly by checking
      // the page content. The page should contain "j2ee"
      if (response.content.indexOf(webSearch) == -1) {
        TestUtil.logErr("Web User Principal incorrect");
        throw new Fault("test failed.");
      }
      TestUtil.logMsg("Web User Principal correct.");

      // Test to make sure SecTestEJB accessed as user "javajoe"
      if (response.content.indexOf(ejbSearch) == -1) {
        TestUtil.logErr("Ejb User Principal incorrect");
        throw new Fault("test failed.");
      }
      TestUtil.logMsg("Ejb User Principal correct.");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      throw new Fault("test failed: ", e);
    }
  }

  /*
   * @testName: web_to_ejb_annotation_runAs
   *
   * @assertion_ids: Servlet:SPEC:199.7; JavaEE:SPEC:31; JavaEE:SPEC:30
   *
   * @test_Strategy: 1. Configure a servlet(ServletTwo) with the following
   * configurations a) BASIC authentication b) accessible only by rolename
   * "Administrator" c) use annotation in ServletTwo to configure it to runAs
   * rolename "Manager" (use principal name javajoe for this)
   *
   * 2. Configure SecTestEJB.getCallerPrincipalName() to be be accessible only
   * by role name "Manager" using annotation.
   *
   * 3. Send a http request and invoke ServletTwo with j2ee as username and
   * password. ( Note: j2ee is mapped to rolename "Administrator" )
   *
   * 4. From ServletTwo look up SecTestEJB a stateless session bean and invoke a
   * method getCallerPrincipalName() on it
   *
   * 5. From within ServletTwo obtain the CallerPrincipal using
   * request.getUserPrincipal().getName()
   *
   * 6. From within SecTestEJB obtain the CallerPrincipal using
   * sessionContext.getCallerPrincipal().getName()
   *
   * 7. From the client make sure ServletTwo is accessed as user "j2ee" and
   * SecTestEJB accessed as user "javajoe" ( Note:User "javajoe" is mapped to
   * rolename "Manager")
   */

  public void web_to_ejb_annotation_runAs() throws Fault {
    try {
      String webSearch = username;
      String ejbSearch = authusername;

      TestUtil.logMsg("Invoking ServletTwo as User :" + username);

      // Send request for ServletTwo, passing in j2ee as
      // username and password:
      request = pageWebToEjbAnnotationRunAs;
      TestUtil.logMsg("Sending request \"" + request + "\"");
      response = WebUtil.sendAuthenticatedRequest("GET",
          InetAddress.getByName(hostname), portnum, ctsurl.getRequest(request),
          null, null, username, password);

      // Check that the page was retrieved (no error)
      if (response.isError()) {
        TestUtil.logErr("Could not access " + request);
        throw new Fault("test failed.");
      }

      TestUtil.logMsg("Received following Response \n");
      TestUtil.logMsg(response.content);

      // Test to make sure we are authenticated properly by checking
      // the page content. The page should contain "j2ee"
      if (response.content.indexOf(webSearch) == -1) {
        TestUtil.logErr("Web User Principal incorrect");
        throw new Fault("test failed.");
      }

      TestUtil.logMsg("Web User Principal correct.");

      // Test to make sure SecTestEJB accessed as user "javajoe"
      if (response.content.indexOf(ejbSearch) == -1) {
        TestUtil.logErr("Ejb Principal incorrect");
        throw new Fault("test failed.");
      }
      TestUtil.logMsg("Ejb User Principal correct.");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      throw new Fault("test failed: ", e);
    }
  }

  public void cleanup() throws Fault {
    logMsg("cleanup complete");
  }

}
