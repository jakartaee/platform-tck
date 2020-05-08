/*
 * Copyright (c) 2012, 2020 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jaxrs.ee.rs.core.securitycontext;

import java.util.Properties;

import jakarta.ws.rs.core.Response;

import org.apache.commons.httpclient.Header;

import com.sun.ts.tests.common.webclient.http.HttpResponse;
import com.sun.ts.tests.jaxrs.common.JAXRSCommonClient;

/*
 * @class.setup_props: webServerHost;
 *                     webServerPort;
 *                     ts_home;
 */
public abstract class JAXRSClient extends JAXRSCommonClient {
  private static final long serialVersionUID = 1L;

  protected static final String URL = "Context";

  protected HttpResponse response;

  protected String wwwAuthenticate;

  protected String user;

  protected String password;

  protected String authuser;

  protected String authpassword;

  public void setup(String[] args, Properties p) throws Fault {
    user = p.getProperty("user");
    password = p.getProperty("password");
    authuser = p.getProperty("authuser");
    authpassword = p.getProperty("authpassword");
    assertFault(!isNullOrEmpty(user), "user was not in build.proerties");
    assertFault(!isNullOrEmpty(password),
        "password was not in build.proerties");
    assertFault(!isNullOrEmpty(authuser),
        "authuser was not in build.proerties");
    assertFault(!isNullOrEmpty(authpassword),
        "authpassword was not in build.proerties");
    super.setup(args, p);
  }

  public void noAuthorizationTest() throws Fault {
    setProperty(STATUS_CODE, getStatusCode(Response.Status.UNAUTHORIZED));
    invokeRequest();
    assertFault(wwwAuthenticate != null,
        "Expected authentication request missing!");
  }

  protected void invokeRequest() throws Fault {
    setProperty(REQUEST, buildRequest("GET", URL));
    invoke();
    response = _testCase.getResponse();
    Header header = response.getResponseHeader("WWW-Authenticate");
    wwwAuthenticate = header == null ? null : header.getValue();
  }
}
