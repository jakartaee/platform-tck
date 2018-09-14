/*
 * Copyright (c) 2013, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jaxrs.platform.container.asyncejb;

import javax.ws.rs.core.MediaType;

import com.sun.ts.tests.jaxrs.common.client.JaxrsCommonClient;

/*
 * @class.setup_props: webServerHost;
 *                     webServerPort;
 *                     ts_home;
 */
/*
 * These test are in the platform package since async is not available in 
 * Servlet 2.5 spec by default
 */
public class JAXRSClient extends JaxrsCommonClient {

  private static final long serialVersionUID = 8849202370030024015L;

  public JAXRSClient() {
    setContextRoot("/jaxrs_platform_container_asyncejb_web/resource");
  }

  public static void main(String[] args) {
    new JAXRSClient().run(args);
  }

  /*
   * @testName: asynchronousTest
   * 
   * @assertion_ids: JAXRS:SPEC:106;
   * 
   * @test_Strategy: When an EJB method is annotated with @Asynchronous, the EJB
   * container automatically allocates the necessary resources for its
   * execution.
   * 
   * //Check this does not break build
   */
  public void asynchronousTest() throws Fault {
    setProperty(Property.REQUEST, buildRequest(Request.GET, "check"));
    setProperty(Property.REQUEST_HEADERS,
        buildAccept(MediaType.TEXT_PLAIN_TYPE));
    invoke();
    String response = getResponseBody();
    Long milis = Long.parseLong(response);
    logMsg(
        "@Asynchronous did executued longTimeOperation asynchronously, response was",
        milis, "milis");
  }
}
