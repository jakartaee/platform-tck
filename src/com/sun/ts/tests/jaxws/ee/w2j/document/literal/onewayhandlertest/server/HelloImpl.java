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

package com.sun.ts.tests.jaxws.ee.w2j.document.literal.onewayhandlertest.server;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;

import com.sun.ts.tests.jaxws.common.*;

import jakarta.xml.ws.*;
import jakarta.xml.ws.handler.*;
import jakarta.xml.ws.handler.soap.*;

import jakarta.xml.ws.WebServiceException;

import java.io.*;
import java.util.Iterator;
import java.util.Map;
import java.util.List;
import java.util.Vector;
import jakarta.xml.ws.handler.MessageContext;
import jakarta.activation.DataHandler;
import java.util.Properties;

// Service Implementation Class - as outlined in JAX-WS Specification

import jakarta.jws.WebService;
import jakarta.annotation.Resource;

@WebService(portName = "HelloPort", targetNamespace = "http://dlowhandlertestservice.org/wsdl", serviceName = "DLOWHandlerTestService", wsdlLocation = "WEB-INF/wsdl/WSDLOWHandlerTestService.wsdl", endpointInterface = "com.sun.ts.tests.jaxws.ee.w2j.document.literal.onewayhandlertest.server.Hello")

public class HelloImpl implements Hello {

  public void doHandlerTest1(
      com.sun.ts.tests.jaxws.ee.w2j.document.literal.onewayhandlertest.server.MyAction action) {

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
