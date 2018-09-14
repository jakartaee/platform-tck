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

package com.sun.ts.tests.ejb30.webservice.wscontext;

import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.lib.porting.TSURL;
import com.sun.javatest.Status;

import javax.xml.ws.WebServiceRef;
import javax.xml.ws.BindingProvider;
import java.lang.Exception;
import javax.xml.ws.WebServiceException;
import java.util.Properties;
import java.util.Map;
import java.net.URL;

/**
 *
 * @author Raja Perumal
 */
public class Client extends EETest {
  @WebServiceRef(name = "service/HelloService")
  static HelloService service;

  private Hello port;

  private Properties props = null;

  private TSURL ctsurl = new TSURL();

  private String hostname = "localhost";

  private String PROTOCOL = "http";

  private String urlString = null;

  private int portnum = 8000;

  private String username = "";

  private String password = "";

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; user; password;
   */
  public void setup(String[] args, Properties p) throws Fault {
    props = p;
    try {
      hostname = props.getProperty("webServerHost");
      portnum = Integer.parseInt(props.getProperty("webServerPort"));
      username = props.getProperty("user");
      password = props.getProperty("password");
      urlString = ctsurl.getURLString(PROTOCOL, hostname, portnum,
          "/service/HelloService");

    } catch (Exception e) {
      throw new Fault("Setup failed:", e);
    }

    TestUtil.logMsg("setup ok");
  }

  /*
   * @testName: WebServiceContextTest
   * 
   * @assertion_ids:
   * 
   * @test_Strategy:
   *
   * 1) During webservice invocation, the WebServiceContext injected to the
   * webservice should have the following.
   *
   * 1) The getMessageContext() on WebServiceContext should return an instance
   * of JAX-WS Message context. i.e. javax.xml.ws.handler.MessageContext
   *
   * 2) The getUserPrincipal() Returns the Principal that identifies the sender
   * of the request.
   *
   * 3) The isUserInRole("Administrator") method should return true Note: The
   * user j2ee is mapped to role Adminstrator
   *
   * 2) If any of the above values are incorrect, then the sayHelloProtected()
   * method throws exception and the WebServiceContextTest fails.
   *
   */
  public void WebServiceContextTest() throws Fault {

    try {
      Hello port = null;

      port = (Hello) getJavaEEPort();

      BindingProvider bindingProvider = (BindingProvider) port;
      Map<String, Object> map = bindingProvider.getRequestContext();

      TestUtil.logMsg(
          "Setting the target endpoint address on WS port: " + urlString);
      map.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, urlString);

      // set Username
      map.put(BindingProvider.USERNAME_PROPERTY, username);

      // set password
      map.put(BindingProvider.PASSWORD_PROPERTY, password);

      TestUtil.logMsg("Invoking sayHelloProtected on Hello port");
      String text = port.sayHelloProtected("Raja");
      TestUtil.logMsg("Got Output : " + text);

    } catch (WebServiceException we) {
      we.printStackTrace();
      throw new Fault("WebServiceContext values not correct");
    } catch (Exception e) {
      e.printStackTrace();
      throw new Fault("WebServiceContextTest failed");
    }

  }

  public Object getJavaEEPort() throws Exception {
    TestUtil.logMsg("Get Hello Port from HelloService");
    Object port = service.getPort(Hello.class);
    return port;
  }

  public void cleanup() throws Fault {
    logMsg("cleanup ok");
  }
}
