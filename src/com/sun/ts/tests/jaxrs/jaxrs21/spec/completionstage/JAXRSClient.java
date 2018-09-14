/*
 * Copyright (c) 2017, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jaxrs.jaxrs21.spec.completionstage;

import java.util.concurrent.Future;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;

import com.sun.ts.tests.jaxrs.common.client.JaxrsCommonClient;
import com.sun.ts.tests.jaxrs.common.client.JdkLoggingFilter;

/*
 * @class.setup_props: webServerHost;
 *                     webServerPort;
 *                     ts_home;
 */
public class JAXRSClient extends JaxrsCommonClient {

  private static final long serialVersionUID = 21L;

  public JAXRSClient() {
    setContextRoot("/jaxrs_jaxrs21_spec_completionstage_web");
  }

  public static void main(String[] args) {
    new JAXRSClient().run(args);
  }

  /* Run test */

  /*
   * @testName: completionStageReturnedTest
   * 
   * @assertion_ids: JAXRS:SPEC:128;
   * 
   * @test_Strategy:
   */
  public void completionStageReturnedTest() throws Fault {
    Future<Response> f = ClientBuilder.newClient()
        .register(new JdkLoggingFilter(false)).target(getAbsoluteUrl("async"))
        .request().async().get();
    assertFalse(f.isDone());
    try (Response r = f.get()) {
      String msg = r.readEntity(String.class);
      assertEquals(CompletionStageResource.MESSAGE, msg);
    } catch (Exception e) {
      fault(e);
    }
  }
}
