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

package com.sun.ts.tests.jaxrs.jaxrs21.ee.priority;

import com.sun.ts.tests.jaxrs.common.JAXRSCommonClient;

/*
 * @class.setup_props: webServerHost;
 *                     webServerPort;
 *                     ts_home;
 */
public class JAXRSClient extends JAXRSCommonClient {

  private static final long serialVersionUID = 21L;

  public JAXRSClient() {
    setContextRoot("/jaxrs_jaxrs21_ee_priority_web");
  }

  public static void main(String[] args) {
    new JAXRSClient().run(args);
  }

  /* Run test */

  /*
   * @testName: exceptionMapperPriorityTest
   * 
   * @assertion_ids: JAXRS:SPEC:127;
   * 
   * @test_Strategy:
   */
  public void exceptionMapperPriorityTest() throws Fault {
    setProperty(Property.REQUEST, buildRequest(Request.GET, "exception"));
    setProperty(Property.SEARCH_STRING, ExceptionMapperOne.class.getName());
    invoke();
  }

  /*
   * @testName: paramConverterPriorityTest
   * 
   * @assertion_ids: JAXRS:SPEC:127;
   * 
   * @test_Strategy:
   */
  public void paramConverterPriorityTest() throws Fault {
    setProperty(Property.REQUEST,
        buildRequest(Request.GET, "converter?id=something"));
    setProperty(Property.SEARCH_STRING,
        ParamConverterProviderOne.class.getName());
    invoke();
  }

}
