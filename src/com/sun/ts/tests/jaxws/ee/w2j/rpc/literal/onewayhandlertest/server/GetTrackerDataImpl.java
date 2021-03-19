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

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;

import java.util.Properties;
import com.sun.ts.tests.jaxws.common.*;

import jakarta.xml.ws.*;
import jakarta.xml.ws.handler.*;
import jakarta.xml.ws.handler.soap.*;

import jakarta.xml.ws.WebServiceException;

import jakarta.jws.WebService;

@WebService(portName = "GetTrackerDataPort", targetNamespace = "http://rlowhandlertestservice.org/wsdl", serviceName = "RLOWHandlerTestService", wsdlLocation = "WEB-INF/wsdl/WSRLOWHandlerTestService.wsdl", endpointInterface = "com.sun.ts.tests.jaxws.ee.w2j.rpc.literal.onewayhandlertest.server.GetTrackerData")

public class GetTrackerDataImpl implements GetTrackerData {
  public GetTrackerDataResult getTrackerData(GetTrackerDataAction action) {

    Handler_Util.setTraceFlag(action.getHarnesslogtraceflag());

    Handler_Util.initTestUtil("GetTrackerDataImpl", action.getHarnessloghost(),
        action.getHarnesslogport(), action.getHarnesslogtraceflag());

    TestUtil.logTrace("*** in GetTrackerDataImpl ***");
    String[] messages = null;

    String theAction = action.getAction();
    TestUtil.logTrace("*** action = " + theAction + " ***");

    if (theAction.equals("getArrayMessages1")) {
      messages = HandlerTracker.getArrayMessages1();
    } else if (theAction.equals("getArrayMessages2")) {
      messages = HandlerTracker.getArrayMessages2();
    } else if (theAction.equals("getArrayMessages3")) {
      messages = HandlerTracker.getArrayMessages3();
    } else if (theAction.equals("getArrayMessages4")) {
      messages = HandlerTracker.getArrayMessages4();
    } else if (theAction.equals("getArrayThrowables")) {
      messages = HandlerTracker.getArrayThrowables();
    } else if (theAction.equals("purge")) {
      HandlerTracker.purge();
      String m[] = { "purge complete" };
      messages = m;
    } else {
      String[] m = {
          "Did not get one of the excepted GetTrackerData action messages, the action message received was:"
              + theAction };
      messages = m;
    }

    GetTrackerDataResult r = null;
    try {
      TestUtil.logTrace("The endpoint is sending back the following data:");
      TestUtil.logTrace("action=" + action.getAction());
      TestUtil.logTrace("harnessloghost=" + action.getHarnessloghost());
      TestUtil.logTrace("harnesslogport=" + action.getHarnesslogport());
      TestUtil
          .logTrace("harnesslogtraceflag=" + action.getHarnesslogtraceflag());

      r = new GetTrackerDataResult();
      for (int i = 0; i < messages.length; i++) {
        r.getResult().add(messages[i]);
        TestUtil.logTrace("message[" + i + "]=" + messages[i]);
      }

      r.setHarnessloghost(action.getHarnessloghost());
      r.setHarnesslogport(action.getHarnesslogport());
      r.setHarnesslogtraceflag(action.getHarnesslogtraceflag());
    } catch (Exception e) {
      throw new WebServiceException(e.getMessage(), e);
    }
    return r;
  }
}
