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
 * $Id$
 */

package com.sun.ts.tests.jaxws.ee.w2j.rpc.literal.onewayhandlertest.server;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jaxws.common.Handler_Util;
import com.sun.ts.tests.samples.ejb.ee.simpleHello.Hello;

// Service Implementation Class - as outlined in JAX-WS Specification

import jakarta.jws.WebService;

@WebService(portName = "HelloPort", targetNamespace = "http://rlowhandlertestservice.org/wsdl", serviceName = "RLOWHandlerTestService", wsdlLocation = "WEB-INF/wsdl/WSRLOWHandlerTestService.wsdl", endpointInterface = "com.sun.ts.tests.jaxws.ee.w2j.rpc.literal.onewayhandlertest.server.Hello")

public class HelloImpl implements Hello {

  public void doHandlerTest1(
      com.sun.ts.tests.jaxws.ee.w2j.rpc.literal.onewayhandlertest.server.MyActionType action) {

    Handler_Util.setTraceFlag(action.getHarnesslogtraceflag());

    Handler_Util.initTestUtil("HelloImpl", action.getHarnessloghost(),
        action.getHarnesslogport(), action.getHarnesslogtraceflag());

    TestUtil.logTrace("*** in HelloImpl:doHandlerTest1 ***");
    String theAction = action.getAction();
    TestUtil.logTrace("*** action = " + theAction + " ***");
    String testType = action.getTestType();
    TestUtil.logTrace("*** testType = " + testType + " ***");
  }
}
