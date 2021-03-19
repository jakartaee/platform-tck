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

package com.sun.ts.tests.jaxws.sharedwebservices.dlhandlerservice;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;

import jakarta.xml.ws.WebServiceException;

import jakarta.xml.ws.soap.SOAPFaultException;
import javax.xml.namespace.QName;
import jakarta.xml.soap.Detail;
import jakarta.xml.soap.SOAPFactory;
import jakarta.xml.soap.Name;
import jakarta.xml.soap.SOAPFault;

import com.sun.ts.tests.jaxws.common.*;
import java.util.Properties;

// Service Implementation Class - as outlined in JAX-WS Specification

import jakarta.jws.WebService;

@WebService(portName = "Hello3Port", targetNamespace = "http://dlhandlerservice.org/wsdl", serviceName = "DLHandlerService", wsdlLocation = "WEB-INF/wsdl/DLHandlerService.wsdl", endpointInterface = "com.sun.ts.tests.jaxws.sharedwebservices.dlhandlerservice.Hello3")

public class Hello3Impl implements Hello3 {

  private static final String NAMESPACEURI = "http://dlhandlerservice.org/wsdl";

  private QName faultCode = new QName(NAMESPACEURI, "ItsASoapFault", "tns");

  private Name name = null;

  private String faultActor = "faultActor";

  public com.sun.ts.tests.jaxws.sharedwebservices.dlhandlerservice.MyResultType doHandlerTest3(
      MyActionType action) {
    Handler_Util.setTraceFlag(action.getHarnesslogtraceflag());

    Handler_Util.initTestUtil("Hello3Impl", action.getHarnessloghost(),
        action.getHarnesslogport(), action.getHarnesslogtraceflag());

    TestUtil.logTrace("*** in Hello3Impl ***");

    String theAction = action.getAction();
    TestUtil.logTrace("*** action = " + theAction + " ***");
    String testType = action.getTestType();
    TestUtil.logTrace("*** testType = " + testType + " ***");

    if (theAction.equals("EndpointRemoteRuntimeExceptionTest")) {
      TestUtil
          .logTrace("Throwing a RuntimeException nested in a RemoteException");
      RuntimeException re = new RuntimeException(
          "Hello3Impl:EndpointRemoteRuntimeExceptionTest");
      throw new WebServiceException(
          "RemoteException with nested RuntimeException", re);
    } else if (theAction.equals("EndpointRemoteSOAPFaultExceptionTest")) {
      TestUtil.logTrace(
          "Throwing a SOAPFaultException nested in a RemoteException");
      String faultString = "Hello3Impl:EndpointRemoteSOAPFaultExceptionTest";
      try {
        name = SOAPFactory.newInstance().createName("somefaultentry");
        SOAPFault sf = SOAPFactory.newInstance().createFault(faultString,
            faultCode);
        sf.setFaultActor(faultActor);
        sf.addDetail();
        sf.getDetail().addDetailEntry(name);
        SOAPFaultException sfe = new SOAPFaultException(sf);
        throw new WebServiceException(
            "WebServiceException with nested SOAPFaultException", sfe);
      } catch (Exception e) {
        throw new WebServiceException(
            "Unexpected error occurred in Hello3Impl.doHandlerTest3:" + e);
      }
    }

    MyResultType r = null;
    try {
      TestUtil.logTrace("The endpoint is sending back the following data:");
      TestUtil.logTrace("action=" + action.getAction());
      TestUtil.logTrace("getTestType=" + action.getTestType());
      TestUtil.logTrace("harnessloghost=" + action.getHarnessloghost());
      TestUtil.logTrace("harnesslogport=" + action.getHarnesslogport());
      TestUtil
          .logTrace("harnesslogtraceflag=" + action.getHarnesslogtraceflag());

      r = new MyResultType();
      r.setAction(action.getAction());
      r.setTestType(action.getTestType());
      r.setHarnessloghost(action.getHarnessloghost());
      r.setHarnesslogport(action.getHarnesslogport());
      r.setHarnesslogtraceflag(action.getHarnesslogtraceflag());
    } catch (Exception e) {
      throw new WebServiceException(e.getMessage(), e);
    }
    return r;
  }
}
