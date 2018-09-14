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
 *   $Id$
 *   @author Raja Perumal
 */

package com.sun.ts.tests.webservices12.sec.annotations.ejb.clientcert;

import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.lib.porting.TSURL;
import com.sun.javatest.Status;

import com.sun.ts.tests.webservices12.sec.annotations.ejb.clientcert.HelloService;
import com.sun.ts.tests.webservices12.sec.annotations.ejb.clientcert.Hello;

import javax.xml.ws.WebServiceRef;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.WebServiceException;
import java.util.Properties;
import java.util.Map;
import javax.net.ssl.*;

public class Client extends EETest {
  @WebServiceRef
  static HelloService service;

  private Hello port;

  private Properties props = null;

  private TSURL ctsurl = new TSURL();

  private String hostname = "localhost";

  private String PROTOCOL = "https";

  private String urlString = null;

  private int portnum = 8000;

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /*
   * @class.setup_props: webServerHost; securedWebServicePort;
   */
  public void setup(String[] args, Properties p) throws Fault {
    props = p;
    try {
      hostname = props.getProperty("webServerHost");
      portnum = Integer.parseInt(props.getProperty("securedWebServicePort"));
      urlString = ctsurl.getURLString(PROTOCOL, hostname, portnum,
          "/WSEjbClientCert/HelloService/Hello");
      HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession session) {
          return true;
        }
      });
    } catch (Exception e) {
      throw new Fault("Setup failed:", e);
    }

    TestUtil.logMsg("setup ok");
  }

  /*
   * @testName: sayHelloProtected
   * 
   * @assertion_ids: JAXWS:SPEC:4005; JAXWS:SPEC:7000; JAXWS:SPEC:7010;
   * JAXWS:SPEC:7011; JavaEE:SPEC:10087; WS4EE:SPEC:9000
   * 
   * @test_Strategy:
   */
  public void sayHelloProtected() throws Fault {

    try {
      TestUtil.logMsg("Getting port from the Service : " + service);
      Hello port = service.getHelloPort();

      TestUtil.logMsg(
          "Setting the target endpoint address on WS port: " + urlString);
      BindingProvider bindingProvider = (BindingProvider) port;
      Map<String, Object> map = bindingProvider.getRequestContext();
      map.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, urlString);

      TestUtil.logMsg("Invoking sayHelloProtected on Hello port");
      String text = port.sayHelloProtected("Raja");
      TestUtil.logMsg("Got Output : " + text);
      TestUtil.logMsg("Test sayHelloProtected passed");
    } catch (Exception e) {
      e.printStackTrace();
      throw new Fault("Test sayHelloProtected failed");
    }
    return;
  }

  /*
   * @testName: sayHelloPermitAll
   * 
   * @assertion_ids: JAXWS:SPEC:7010; JAXWS:SPEC:7011; JavaEE:SPEC:10087;
   * WS4EE:SPEC:9000
   * 
   * @test_Strategy:
   */
  public void sayHelloPermitAll() throws Fault {

    try {
      TestUtil.logMsg("Getting port from the Service : " + service);
      Hello port = service.getHelloPort();

      TestUtil.logMsg(
          "Setting the target endpoint address on WS port: " + urlString);
      BindingProvider bindingProvider = (BindingProvider) port;
      Map<String, Object> map = bindingProvider.getRequestContext();
      map.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, urlString);

      TestUtil.logMsg("Invoking sayHelloPermitAll on Hello port");
      String text = port.sayHelloPermitAll("Raja");
      TestUtil.logMsg("Got Output : " + text);
      TestUtil.logMsg("Test sayHelloPermitAll passed");
    } catch (Exception e) {
      e.printStackTrace();
      throw new Fault("Test sayHelloPermitAll failed");
    }
    return;
  }

  /*
   * @testName: sayHelloDenyAll
   * 
   * @assertion_ids: JAXWS:SPEC:6005; JAXWS:SPEC:7010; JAXWS:SPEC:7011;
   * JavaEE:SPEC:10087; WS4EE:SPEC:9000
   * 
   * @test_Strategy:
   */
  public void sayHelloDenyAll() throws Fault {

    try {
      TestUtil.logMsg("Getting port from the Service : " + service);
      Hello port = service.getHelloPort();

      TestUtil.logMsg(
          "Setting the target endpoint address on WS port: " + urlString);
      BindingProvider bindingProvider = (BindingProvider) port;
      Map<String, Object> map = bindingProvider.getRequestContext();
      map.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, urlString);

      TestUtil.logMsg("Invoking sayHelloDenyAll on Hello port");
      String text = port.sayHelloDenyAll("Raja");
      TestUtil.logErr(
          "Test sayHelloDenyAll failed, didn't get the expected exception");
      TestUtil.logErr("Got Output : " + text);
      throw new Fault("Test sayHelloDenyAll failed");
    } catch (WebServiceException e) {
      TestUtil.logMsg("Got expected WebServiceException");
      TestUtil.logMsg("Test sayHelloDenyAll Passed");
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected Exception " + e.getMessage());
      throw new Fault("Test sayHelloDenyAll failed", e);
    }
    return;
  }

  public void cleanup() throws Fault {
    logMsg("cleanup ok");
  }

}
