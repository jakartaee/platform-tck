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
 * @(#)Client.java	1.33 03/05/16
 */

package com.sun.ts.tests.interop.csiv2.ac_ssl_sslr_upn_noid.reverse;

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

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /*
   * @class.setup_props: org.omg.CORBA.ORBClass; java.naming.factory.initial;
   * generateSQL; certLoginUserAlias;
   */
  public void setup(String[] args, Properties p) throws Fault {
    TestUtil.logMsg("setup...");
    props = p;
  }

  /*
   * @testName: ac_ssl_sslr_upn_noid_sb_testid0
   *
   * @assertion_ids: EJB:SPEC:740; EJB:SPEC:742; EJB:SPEC:750; EJB:SPEC:751;
   * CSIv2:SPEC:43; EJB:SPEC:744; EJB:SPEC:741
   *
   * @test_Strategy: From EJB2.0 Spec Section 19.8.2.3 item 1
   *
   * "Application client invocations on enterprise beans with mutual
   * authentication between the application client and EJB container (C and S1)
   * at the SSL layer. This is possible when the enterprise has a Kerberos-based
   * authentication infrastructure of when client-side certificates have been
   * installed. In this case no additional information is required to be
   * included in the security context of the IIOP message sent from C to S1.
   *
   * Note: ac_ssl_sslr_upn_noid_sb_testid0 tests when the client is accessing
   * remote entity bean, with SSL Mutual Authentication using certificates
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
   * 4) Verify the following IOR.0 and port=0
   *
   */
  public void ac_ssl_sslr_upn_noid_sb_testid0() throws Fault {
    boolean pass = true;
    String testName = "ac_ssl_sslr_upn_noid_sb_testid0_reverse";

    TestUtil.logMsg(testName);

    // Certificate based login
    try {

      TSLoginContext clc = new TSLoginContext();
      clc.login(props.getProperty("certLoginUserAlias"),
          System.getProperty("javax.net.ssl.keyStore"),
          System.getProperty("javax.net.ssl.keyStorePassword"));
    } catch (Exception e) {
      TestUtil.logMsg(e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + " TSCertLogin failed");
    }

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
        new IOR0ValidationStrategy(false), null, // No request validation
        new ResponseValidationStrategy(false, // Expecting reply service
                                              // context?
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
