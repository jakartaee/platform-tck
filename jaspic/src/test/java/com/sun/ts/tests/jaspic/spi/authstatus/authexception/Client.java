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

package com.sun.ts.tests.jaspic.spi.authstatus.authexception;

import java.net.URL;
import java.util.Map;
import java.util.Properties;

import javax.xml.namespace.QName;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.porting.TSURL;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jaspic.util.LogFileProcessor;
import com.sun.ts.tests.jaspic.util.WebServiceUtils;

import jakarta.security.auth.message.AuthException;
import jakarta.xml.ws.BindingProvider;
import jakarta.xml.ws.WebServiceRef;

/**
 *
 * @author Raja Perumal
 */
public class Client extends EETest {
  @WebServiceRef(name = "AuthExceptionHelloService")
  static AuthExceptionHelloService service;

  private AuthExceptionHello port;

  private Properties props = null;

  private static final String UserNameProp = "user";

  private static final String UserPasswordProp = "password";

  private String username = "";

  private String password = "";

  private TSURL ctsurl = new TSURL();

  private String hostname = "localhost";

  private String logicalHostName = "server";

  private String PROTOCOL = "http";

  private String urlString = null;

  private int portnum = 8000;

  private String platformMode = null;

  private LogFileProcessor logProcessor = null;

  // ServiceName and PortName mapping configuration going java-to-wsdl
  private static final String SERVICE_NAME = "AuthExceptionHelloService";

  private static final String PORT_NAME = "AuthExceptionHelloPort";

  private static final String NAMESPACEURI = "http://authexception.authstatus.spi.jaspic.tests.ts.sun.com/";

  private QName SERVICE_QNAME = new QName(NAMESPACEURI, SERVICE_NAME);

  private QName PORT_QNAME = new QName(NAMESPACEURI, PORT_NAME);

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /*
   * @class.setup_props: log.file.location; webServerHost; webServerPort; user;
   * password; logical.hostname.soap; platform.mode;
   */
  public void setup(String[] args, Properties p) throws Fault {
    props = p;
    try {
      username = props.getProperty(UserNameProp);
      password = props.getProperty(UserPasswordProp);
      hostname = props.getProperty("webServerHost");
      platformMode = props.getProperty("platform.mode");
      logicalHostName = props.getProperty("logical.hostname.soap");
      portnum = Integer.parseInt(props.getProperty("webServerPort"));
      urlString = ctsurl.getURLString(PROTOCOL, hostname, portnum,
          "/AuthExceptionHello_web/AuthExceptionHello");

      // create LogProcessor
      logProcessor = new LogFileProcessor(props);

      // retrieve logs based on application Name
      logProcessor.fetchLogs("pullAllLogRecords|fullLog");

    } catch (Exception e) {
      throw new Fault("Setup failed:", e);
    }

    TestUtil.logMsg("setup ok");
  }

  /*
   * sayHelloProtected
   *
   * This method is invoked first by all tests, as a result of this client and
   * server soap runtime generates corresponding messages in the log and the
   * other tests verify those messages.
   */
  private void sayHelloProtected() throws AuthException, Exception {

    AuthExceptionHello port = null;

    if (platformMode.equals("jakartaEE")) {
      port = (AuthExceptionHello) getJavaEEPort();
    } else {
      port = (AuthExceptionHello) getStandAlonePort();
    }

    BindingProvider bindingProvider = (BindingProvider) port;
    Map<String, Object> map = bindingProvider.getRequestContext();

    TestUtil
        .logMsg("Setting the target endpoint address on WS port: " + urlString);
    map.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, urlString);

    TestUtil.logMsg("Invoking sayHelloProtected on Hello port");
    String text = port.sayHelloProtected("Raja");
    TestUtil.logMsg("Got Output : " + text);
  }

  /**
   * @keywords: jaspic_soap
   *
   * @testName: AuthStatusAuthException
   *
   * @assertion_ids: JASPIC:SPEC:138; JASPIC:SPEC:144; JASPIC:SPEC:167;
   *                 JASPIC:JAVADOC:6
   *
   *
   * @test_Strategy: 1. Register TSSV with the AppServer. (See User guide for
   *                 Registering TSSV with your AppServer ).
   *
   *                 2. Invoke sayHelloProtected and make sure AuthException is
   *                 received in the client
   *
   *                 Description: Throws an AuthException The runtime should use
   *                 the exception to convey to the client runtime that the
   *                 request failed
   *
   *
   */
  public void AuthStatusAuthException() throws Fault {
    boolean verified = false;

    try {
      sayHelloProtected();
    } catch (AuthException ae) {
      TestUtil.logMsg("Got expected Authexception :" + ae.getMessage());
      TestUtil.printStackTrace(ae);
      return;
    } catch (Exception e) {
      TestUtil.logMsg("Got expected exception :" + e.getMessage());
      TestUtil.printStackTrace(e);
      return;
    }

    // Control shouldn't come here.
    throw new Fault("AuthStatusAuthException failed");
  }

  public Object getJavaEEPort() throws Exception {
    TestUtil.logMsg("Get AuthException Port from AuthExceptionHelloService");
    Object port = service.getPort(AuthExceptionHello.class);
    return port;
  }

  public Object getStandAlonePort() throws Exception {
    URL wsdlurl = new URL(urlString + "?WSDL");
    return WebServiceUtils.getPort(wsdlurl, SERVICE_QNAME,
        AuthExceptionHelloService.class, PORT_QNAME, AuthExceptionHello.class);
  }

  public void cleanup() throws Fault {
    logMsg("cleanup ok");
  }
}
