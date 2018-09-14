/*
 * Copyright (c) 2007, 2018 Oracle and/or its affiliates. All rights reserved.
 * Copyright (c) 2002 International Business Machines Corp. All rights reserved.
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

package com.sun.ts.tests.webservices.sec.war.certificate;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import com.sun.ts.tests.jaxrpc.common.*;
import com.sun.javatest.Status;
import java.rmi.RemoteException;
import java.util.Iterator;
import java.rmi.Remote;
import javax.xml.rpc.Service;
import javax.xml.rpc.ServiceException;
import javax.xml.rpc.Stub;
import javax.xml.namespace.QName;
import javax.naming.InitialContext;
import java.util.Properties;

public class Client extends EETest {
  Service certificateService;

  HelloCertificate certificatePort;

  private TSURL ctsurl = new TSURL();

  private String hostname = "localhost";

  private String PROTOCOL = "https";

  private String urlString = null;

  private int portnum = 8000;

  private static final String ENDPOINTURL = "/SecWarCertificate_web/ws4ee/SecWarCertificate";

  InitialContext ctx;

  private void getStub() throws Exception {
    ctx = new InitialContext();

    certificateService = (javax.xml.rpc.Service) ctx
        .lookup("java:comp/env/service/secWar/certificate");
    TestUtil.logMsg("Get port from certificate Service");
    certificatePort = (HelloCertificate) certificateService
        .getPort(HelloCertificate.class);
    TestUtil.logMsg("certificate port obtained");
    Stub stub = (javax.xml.rpc.Stub) certificatePort;
    TestUtil
        .logMsg("Setting the target endpoint address on WS port: " + urlString);
    stub._setProperty(Stub.ENDPOINT_ADDRESS_PROPERTY, urlString);
  }

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /* Test setup */

  /*
   * @class.testArgs: -ap jaxrpc-url-props.dat
   * 
   * @class.setup_props: webServerHost; securedWebServicePort;
   */

  public void setup(String[] args, Properties p) throws Fault {
    try {
      hostname = p.getProperty("webServerHost");
      portnum = Integer.parseInt(p.getProperty("securedWebServicePort"));
      urlString = ctsurl.getURLString(PROTOCOL, hostname, portnum, ENDPOINTURL);
      getStub();
    } catch (Exception e) {
      throw new Fault("setup failed:", e);
    }
    logMsg("setup ok");
  }

  public void cleanup() throws Fault {
    logMsg("cleanup ok");
  }

  private void printSeperationLine() {
    TestUtil.logMsg("---------------------------");
  }

  /*
   * @testName: secWarCertificate
   *
   * @assertion_ids: WS4EE:SPEC:193; WS4EE:SPEC:196; WS4EE:SPEC:194;
   * WS4EE:SPEC:195; WS4EE:SPEC:9002;
   * 
   * @test_Strategy: Call EJB with client certificate authentication
   */
  public void secWarCertificate() throws Fault {
    TestUtil.logMsg("secWarCertificate");
    try {
      String ret1 = certificatePort.sayHelloCertificate("secWarCertificate");
      if (!ret1.equals("'secWarCertificate' from HelloCertificateImpl!")) {
        TestUtil.logMsg("test secWarCertificate failed: return value: " + ret1);
        throw new Fault("SecWarCertificate failed");
      }
      TestUtil.logMsg("SecWarCertificate passed");
    } catch (Throwable t) {
      TestUtil.logMsg(
          "test SecWarCertificate failed: got exception " + t.toString());
      throw new Fault("SecWarCertificate failed");
    }
    return;
  }
}
