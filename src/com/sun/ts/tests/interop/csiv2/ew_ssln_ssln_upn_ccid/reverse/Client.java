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
 * @(#)Client.java	1.21 03/05/16
 */

package com.sun.ts.tests.interop.csiv2.ew_ssln_ssln_upn_ccid.reverse;

import java.util.*;
import jakarta.ejb.*;
import java.rmi.*;
import java.util.*;
import java.net.*;
import javax.naming.*;
import javax.rmi.*;
import com.sun.ts.tests.interop.csiv2.common.*;
import com.sun.ts.tests.interop.csiv2.common.parser.*;
import com.sun.ts.tests.interop.csiv2.common.validation.*;
import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import com.sun.javatest.Status;

public class Client extends EETest {
  private Properties props = null;

  private TSURL ctsurl = new TSURL();

  private URL url = null;

  private URLConnection urlConn = null;

  private TSHttpsURLConnection ctsHttpsURLConn = null;

  private static final String WEBSERVERHOSTPROP = "webServerHost";

  private static final String WEBSERVERPORTPROP = "securedWebServicePort";

  private String SERVLET = "/interop_csiv2_ew_ssln_ssln_upn_ccid_r_web/CSIv2ServletTest";

  private String webServerHost = "unknown";

  private int webServerPort = 8000;

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /*
   * @class.setup_props: org.omg.CORBA.ORBClass; java.naming.factory.initial;
   * generateSQL; webServerHost; securedWebServicePort; certLoginUserAlias;
   */
  public void setup(String[] args, Properties p) throws Fault {
    TestUtil.logMsg("Setup");
    boolean pass = true;
    props = p;
    try {
      webServerHost = p.getProperty(WEBSERVERHOSTPROP);
      if (webServerHost == null)
        pass = false;
      else if (webServerHost.equals(""))
        pass = false;
      try {
        webServerPort = Integer.parseInt(p.getProperty(WEBSERVERPORTPROP));
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        pass = false;
      }
      TestUtil.logMsg("webServerHost = " + webServerHost);
      TestUtil.logMsg("webServerPort = " + webServerPort);
      if (!pass) {
        TestUtil.logErr(
            "Please specify host & port of web server " + "in ts.jte file: "
                + WEBSERVERHOSTPROP + ", " + WEBSERVERPORTPROP);
        throw new Fault("Setup failed:");
      }
      TestUtil.logMsg("Setup ok");
    } catch (Exception e) {
      throw new Fault("Setup failed:", e);
    }
  }

  /*
   * @testName: ew_ssln_ssln_upn_ccid_sb_sb_testid4a
   *
   * @assertion_ids: EJB:SPEC:740; EJB:SPEC:742; EJB:SPEC:750; EJB:SPEC:751;
   * CSIv2:SPEC:43; EJB:SPEC:746; EJB:SPEC:741
   *
   * @test_Strategy: From EJB2.0 Spec Secsion 19.8.2.3 item 3: "3. Invocations
   * from Web/EJB clients to enterprise beans with a trust relationship between
   * the client container S1 and server container S2 (scenarios 1,3 and 4 in
   * section 19.3.3, interoperability requirements five and six in section
   * 19.4). S2 does not need to independently authenticate the initiating client
   * C. In this case the client container S1 must insert into the IIOP message a
   * CSIv2 security context with an identity token. The identity token contains
   * a principal name and realm (authentication domain). The principal may be
   * propagated as an X.509 certificate chain or as a X.501 distinguished name
   * or as a typed principal name encoded using the formats described in the
   * CSIv2 specification. The identity propagated is determined as follows: *.
   * If the client Web/EJB component is configured to use caller identity, and
   * the caller C authenticated itself to S1, then the identity token contains
   * the initiating client C s identity. *. If the client component is
   * configured to use caller identity, and the caller C did not authenticate
   * itself to S1, then the identity token contains the anonymous type. *. If
   * the client component is configured to use a run-as identity then the
   * identity token contains the run-as identity. J2EE containers are required
   * to support the stateless mode of propagating principal and authentication
   * information defined in CSIv2 (where the server does not store any state for
   * a particular client principal across invocations), and may optionally
   * support the stateful mode.
   *
   * Test ew_ssln_ssln_upn_ccid_sb_sb_testid4a tests session bean access remote
   * session bean using certificate chain or distinguished name identity token
   * without SSL.
   *
   * LocalSessionBean (RI) ===> RemoteSessionBean (VI)
   *
   * 1) Configuration (RI to VI): Refer to props files for appclient and IOR
   * configurations
   *
   * a)Components deployed in RI: 1) EJBs SessionBean(SB) 2) Logger servlet 3)
   * ApplicationClient(AC) and WebClient(WC)
   * 
   * b)Components deployed in VI(Vendor Implementation) 1) EJBs SessionBean(SB)
   *
   * c) Transport protection : no SSL
   * 
   * 
   * 2) Add the following to invocation chain a) LocalSessionBean b)
   * RemoteSessionBean
   * 
   * 3) Call invoke on LocalSessionBean Call invoke on RemoteSessionBean
   * 
   * 4) Verify the following a) SAS identity token =certificate chain or
   * distinguished name b) port !=0 c) IOR.4
   *
   */
  public void ew_ssln_ssln_upn_ccid_sb_sb_testid4a() throws Fault {

    boolean pass = true;
    String testName = "ew_ssln_ssln_upn_ccid_sb_sb_testid4a_reverse";

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

    chain.add("java:comp/env/ejb/LocalSession");
    chain.add("java:comp/env/ejb/RemoteSession");
    TestUtil.logMsg("Client initiated.");
    CSIv2AppClient appClient = new CSIv2AppClient();
    TestUtil.logMsg("LocalSessionBean (RI) ====> RemoteSessionBean (VI).");
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
        new IOR4ValidationStrategy(true,
            IORValidationStrategy.ITTX509CertChain
                | IORValidationStrategy.ITTDistinguishedName),
        null, // No request validation
        new ResponseValidationStrategy(true, // Expecting reply service context?
            false // Expecting error message (ContextError)?
        ));

    if (!pass)
      throw new Fault(testName + " failed");
  }

  /*
   * @testName: ew_ssln_ssln_upn_ccid_wb_sb_testid4a
   *
   * @assertion_ids: EJB:SPEC:740; EJB:SPEC:742; EJB:SPEC:750; EJB:SPEC:751;
   * CSIv2:SPEC:43; EJB:SPEC:746; EJB:SPEC:741
   *
   * @test_Strategy: From EJB2.0 Spec Secsion 19.8.2.3 item 3: "3. Invocations
   * from Web/EJB clients to enterprise beans with a trust relationship between
   * the client container S1 and server container S2 (scenarios 1,3 and 4 in
   * section 19.3.3, interoperability requirements five and six in section
   * 19.4). S2 does not need to independently authenticate the initiating client
   * C. In this case the client container S1 must insert into the IIOP message a
   * CSIv2 security context with an identity token. The identity token contains
   * a principal name and realm (authentication domain). The principal may be
   * propagated as an X.509 certificate chain or as a X.501 distinguished name
   * or as a typed principal name encoded using the formats described in the
   * CSIv2 specification. The identity propagated is determined as follows: *.
   * If the client Web/EJB component is configured to use caller identity, and
   * the caller C authenticated itself to S1, then the identity token contains
   * the initiating client C s identity. *. If the client component is
   * configured to use caller identity, and the caller C did not authenticate
   * itself to S1, then the identity token contains the anonymous type. *. If
   * the client component is configured to use a run-as identity then the
   * identity token contains the run-as identity. J2EE containers are required
   * to support the stateless mode of propagating principal and authentication
   * information defined in CSIv2 (where the server does not store any state for
   * a particular client principal across invocations), and may optionally
   * support the stateful mode.
   *
   * Test ew_ssln_ssln_upn_ccid_wb_sb_testid4a tests servlet access remote
   * session bean using certificate chain or distinguished name identity token
   * without SSL.
   *
   * WebClient (RI) ===> RemoteSessionBean (VI)
   *
   * 1) Configuration (RI to VI): Refer to props files for appclient and IOR
   * configurations
   *
   * a)Components deployed in RI: 1) EJBs SessionBean(SB) 2) Logger servlet 3)
   * ApplicationClient(AC) and WebClient(WC)
   * 
   * b)Components deployed in VI(Vendor Implementation) 1) EJBs SessionBean(SB)
   *
   * c) Transport protection : no SSL
   * 
   * 
   * 2) Add the following to invocation chain a) WebClient b) RemoteSessionBean
   * 
   * 3) Call invoke on WebClient Call invoke on RemoteSessionBean
   * 
   * 4) Verify the following a) SAS identity token =certificate chain or
   * distinguished name b) port !=0 c) IOR.4
   *
   */
  public void ew_ssln_ssln_upn_ccid_wb_sb_testid4a() throws Fault {

    boolean pass = true;
    String testName = "ew_ssln_ssln_upn_ccid_wb_sb_testid4a_reverse";

    TestUtil.logMsg(testName);

    // Get log bean:
    CSIv2Log log = CSIv2Log.getLog();

    // Purge any old results:
    log.purge();

    // Enable logging interceptor locally and on RI
    log.enableLoggingInterceptor(true, true);

    log.startLog();

    log.logStartAssertion(testName);

    TestUtil.logMsg("Client initiated.");
    TestUtil.logMsg("WebClient (RI) ====> RemoteSessionBean (VI).");
    try {
      url = ctsurl.getURL("https", webServerHost, webServerPort, SERVLET);
      props.setProperty("CHAIN", "java:comp/env/ejb/RemoteSession");
      TestUtil.logMsg("Send http request to WebClient");
      ctsHttpsURLConn = CSIv2Util.sendPostDataHttps(props, url);
      TestUtil.logMsg("Get http response from WebClient");
      Properties p = CSIv2Util.getResponseProperties(ctsHttpsURLConn);
      String passStr = p.getProperty("TESTRESULT");
      if (passStr.equals("false"))
        pass = false;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
    }
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
        new IOR4ValidationStrategy(true,
            IORValidationStrategy.ITTX509CertChain
                | IORValidationStrategy.ITTDistinguishedName),
        null, // No request validation
        new ResponseValidationStrategy(true, // Expecting reply service context?
            false // Expecting error message (ContextError)?
        ));

    if (!pass)
      throw new Fault(testName + " failed");
  }

  /*
   * cleanup
   */
  public void cleanup() throws Fault {
    TestUtil.logMsg("Cleanup ok");
  }
}
