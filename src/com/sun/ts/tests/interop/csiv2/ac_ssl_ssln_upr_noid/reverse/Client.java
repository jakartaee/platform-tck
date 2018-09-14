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

package com.sun.ts.tests.interop.csiv2.ac_ssl_ssln_upr_noid.reverse;

import java.util.*;
import javax.ejb.*;
import java.rmi.*;
import java.util.*;
import javax.naming.*;
import javax.rmi.*;
import com.sun.ts.tests.interop.csiv2.common.*;
import com.sun.ts.tests.interop.csiv2.common.parser.*;
import com.sun.ts.tests.interop.csiv2.common.validation.*;
import com.sun.ts.lib.util.*;
import com.sun.ts.lib.harness.*;
import com.sun.javatest.Status;
import com.sun.ts.lib.porting.*;

public class Client extends EETest {
  private Properties props = null;

  private static final String UserNameProp = "user";

  private static final String UserPasswordProp = "password";

  private String username = "";

  private String password = "";

  private TSLoginContext lc = null;

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /*
   * @class.setup_props: org.omg.CORBA.ORBClass; java.naming.factory.initial;
   * generateSQL; user; password;
   */
  public void setup(String[] args, Properties p) throws Fault {
    TestUtil.logMsg("setup...");
    props = p;

    try {
      username = props.getProperty(UserNameProp);
      password = props.getProperty(UserPasswordProp);
      lc = new TSLoginContext();
      lc.login(username, password);
    } catch (Exception e) {
      throw new Fault("Login failed:", e);
    }
  }

  /*
   * @testName: ac_ssl_ssln_upr_noid_sb_testid2
   *
   * @assertion_ids: EJB:SPEC:740; EJB:SPEC:742; EJB:SPEC:750; EJB:SPEC:751;
   * CSIv2:SPEC:43; EJB:SPEC:745; EJB:SPEC:741
   *
   * @test_Strategy: From EJB2.0 Spec Section 19.8.2.3 item 2:
   *
   * "Application Client invocations on enterprise beans with server-only
   * authentication between the application client and EJB container (C and S1)
   * at the SSL layer. This usually happens when the client cannot authenticate
   * in the transport. In this case, the client container must be capable of
   * inserting into the IIOP message a CSIv2 security a context with a client
   * authentication token that contains the client C's authentication data. Once
   * the EJB container S1 has authenticated the client, it may or may not
   * maintain state about the client, so subsequent invocations from the client
   * on the same network connection may need to be authenticated again. The
   * client and server containers must follow the Conformance Level 0 rules in
   * the CSIv2 specification for client authentication. In particular, support
   * for the GSSUP username-password authentication mechanism is required. "
   *
   * (Note: ac_ssl_ssln_upr_noid_sb_testid2 tests server-only authentication of
   * client over a secure transport protected by SSL. The client sends valid
   * username and password along to be authenticated by EJB server.)
   *
   * AppClient (RI) ===> RemoteSessionBean (VI)
   *
   * 1) Configuration (RI to VI): Refer to props files for appclient and IOR
   * configurations
   *
   * a)Components deployed in RI: 1) ApplicationClient(AC) 2) Logger servlet
   * 
   * b)Components deployed in VI(Vendor Implementation) 1) EJBs SessionBean(SB)
   *
   * c) Transport protection : SSL
   * 
   * 
   * 2) Add the following to invocation chain a) RemoteSessionBean
   * 
   * 3) Call invoke on RemoteSessionBean
   * 
   * 4) Verify the following IOR.1 and port=0
   *
   */
  public void ac_ssl_ssln_upr_noid_sb_testid2() throws Fault {

    boolean pass = true;
    String testName = "ac_ssl_ssln_upr_noid_sb_testid2_reverse";

    TestUtil.logMsg(testName);

    // Get log bean:
    CSIv2Log log = CSIv2Log.getLog();

    // Purge any old results:
    log.purge();

    // Enable logging interceptor locally and on RI
    log.enableLoggingInterceptor(true, true);

    log.startLog();

    log.logStartAssertion(testName);

    ArrayList chain = new ArrayList();

    chain.add("java:comp/env/ejb/RemoteSession");
    TestUtil.logMsg("Client initiated.");
    CSIv2AppClient appClient = new CSIv2AppClient();
    TestUtil.logMsg("AppClient (RI) ====> RemoteSessionBean (VI).");
    appClient.invoke(chain, props);
    TestUtil.logMsg("Client completed.");

    log.logEndAssertion();
    log.endLog();

    // Disable logging interceptor locally and on RI
    log.enableLoggingInterceptor(false, false);

    String logContents = log.getLogContents();
    TestUtil.logMsg("Log contents:");
    TestUtil.logMsg(logContents);

    // Purge any results so we don't interfere with future tests:
    log.purge();

    // Perform output validation:
    pass = CSIv2LogValidator.validate(testName, logContents,
        new IOR1ValidationStrategy(false), null, // No request validation
        new ResponseValidationStrategy(true, // Expecting reply service context?
            false // Expecting error (ContextError) reply?
        ));

    if (!pass)
      throw new Fault(testName + " failed");
  }

  /*
   * cleanup
   */
  public void cleanup() throws Fault {
    TestUtil.logMsg("cleanup...");
  }
}
