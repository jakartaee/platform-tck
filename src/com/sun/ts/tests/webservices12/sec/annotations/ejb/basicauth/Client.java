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

package com.sun.ts.tests.webservices12.sec.annotations.ejb.basicauth;

import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.lib.porting.TSURL;
import com.sun.javatest.Status;

import com.sun.ts.tests.webservices12.sec.annotations.ejb.basicauth.HelloService;
import com.sun.ts.tests.webservices12.sec.annotations.ejb.basicauth.Hello;

import javax.xml.ws.WebServiceRef;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.WebServiceException;
import java.util.Properties;
import java.util.Map;

public class Client extends EETest {
  @WebServiceRef
  static HelloService service;

  private Hello port;

  private Properties props = null;

  private static final String UserNameProp = "user";

  private static final String UserPasswordProp = "password";

  private static final String UNAUTH_USERNAME = "authuser";

  private static final String UNAUTH_PASSWORD = "authpassword";

  private String username = "";

  private String password = "";

  private String unauthorizedUser = "";

  private String unauthorizedUserPassword = "";

  private TSURL ctsurl = new TSURL();

  private String hostname = "localhost";

  private String PROTOCOL = "http";

  private String urlString = null;

  private int portnum = 8000;

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
      username = props.getProperty(UserNameProp);
      password = props.getProperty(UserPasswordProp);
      unauthorizedUser = props.getProperty(UNAUTH_USERNAME);
      unauthorizedUserPassword = props.getProperty(UNAUTH_PASSWORD);
      hostname = props.getProperty("webServerHost");
      portnum = Integer.parseInt(props.getProperty("webServerPort"));
      urlString = ctsurl.getURLString(PROTOCOL, hostname, portnum,
          "/WSEjbBasicAuth/HelloService/Hello");
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

      TestUtil.logMsg("Setting username and password in WS port");
      TestUtil.logMsg("username=" + username + " password=" + password);

      BindingProvider bindingProvider = (BindingProvider) port;
      Map<String, Object> map = bindingProvider.getRequestContext();
      map.put(BindingProvider.USERNAME_PROPERTY, username);
      map.put(BindingProvider.PASSWORD_PROPERTY, password);

      TestUtil.logMsg(
          "Setting the target endpoint address on WS port: " + urlString);
      map.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, urlString);

      TestUtil.logMsg(
          "Invoking sayHelloProtected with authorized user on Hello port");
      TestUtil.logMsg("Invocation must be allowed");
      String text = port.sayHelloProtected("Raja");
      TestUtil.logMsg("Test sayHelloProtected passed");
      TestUtil.logMsg("Got Output : " + text);
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

      TestUtil.logMsg("Setting username and password in WS port");
      TestUtil.logMsg("username=" + username + " password=" + password);

      BindingProvider bindingProvider = (BindingProvider) port;
      Map<String, Object> map = bindingProvider.getRequestContext();
      map.put(BindingProvider.USERNAME_PROPERTY, username);
      map.put(BindingProvider.PASSWORD_PROPERTY, password);

      TestUtil.logMsg(
          "Setting the target endpoint address on WS port: " + urlString);
      map.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, urlString);

      TestUtil.logMsg("Invoking sayHelloPermitAll with any user on Hello port");
      TestUtil.logMsg("Invocation must be allowed");
      String text = port.sayHelloPermitAll("Raja");
      TestUtil.logMsg("Test sayHelloPermitAll passed");
      TestUtil.logMsg("Got Output : " + text);
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

      TestUtil.logMsg("Setting username and password in WS port");
      TestUtil.logMsg("username=" + username + " password=" + password);

      BindingProvider bindingProvider = (BindingProvider) port;
      Map<String, Object> map = bindingProvider.getRequestContext();
      map.put(BindingProvider.USERNAME_PROPERTY, username);
      map.put(BindingProvider.PASSWORD_PROPERTY, password);

      TestUtil.logMsg(
          "Setting the target endpoint address on WS port: " + urlString);
      map.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, urlString);

      TestUtil.logMsg("Invoking sayHelloDenyAll with any user on Hello port");
      TestUtil.logMsg("Invocation must not be allowed (throw exception)");
      port.sayHelloDenyAll("Raja");
      TestUtil.logErr("Test sayHelloDenyAll did not throw expected exception");
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

  /*
   * @testName: sayHelloProtectedUnauthorizedUser
   * 
   * @assertion_ids: JAXWS:SPEC:4005; JAXWS:SPEC:7000; JAXWS:SPEC:7010;
   * JAXWS:SPEC:7011; JavaEE:SPEC:10087; WS4EE:SPEC:9000
   * 
   * @test_Strategy: This will test that a user/password which is valid in the
   * system but which is NOT a member of the Administrator role will/should
   * yeild an access denied error when an attempt is made to access a method
   * that has Administrator role access restriction.
   */
  public void sayHelloProtectedUnauthorizedUser() throws Fault {

    try {
      TestUtil.logMsg("Getting port from the Service : " + service);
      Hello port = service.getHelloPort();

      // we want to use a valid username/password that are recognized
      // by the system but that do NOT exist in the Administrator
      // role such that using these should yeild an access denied error
      TestUtil.logMsg("Setting username and password in WS port");
      TestUtil.logMsg("username=" + unauthorizedUser + " password="
          + unauthorizedUserPassword);

      BindingProvider bindingProvider = (BindingProvider) port;
      Map<String, Object> map = bindingProvider.getRequestContext();
      map.put(BindingProvider.USERNAME_PROPERTY, unauthorizedUser);
      map.put(BindingProvider.PASSWORD_PROPERTY, unauthorizedUserPassword);

      TestUtil.logMsg(
          "Setting the target endpoint address on WS port: " + urlString);
      map.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, urlString);

      TestUtil.logMsg(
          "Invoking sayHelloProtected with unauthorized user on Hello port");
      TestUtil.logMsg("Invocation must not be allowed (throw exception)");
      port.sayHelloProtected("Raja");
      TestUtil.logErr(
          "Test sayHelloProtectedUnauthorizedUser did not throw expected exception");
      throw new Fault("Test sayHelloProtectedUnauthorizedUser failed");
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
