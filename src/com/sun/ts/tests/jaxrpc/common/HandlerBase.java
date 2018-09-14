/*
 * Copyright (c) 2007, 2018 Oracle and/or its affiliates. All rights reserved.
 * Copyright (c) 2002 International Business Machines Corp. All rights reserved.
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

package com.sun.ts.tests.jaxrpc.common;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;

import javax.xml.soap.*;

import javax.xml.rpc.soap.*;
import javax.xml.namespace.QName;
import javax.xml.rpc.handler.*;
import javax.xml.rpc.handler.soap.*;

public class HandlerBase implements javax.xml.rpc.handler.Handler {
  public int initCalled = 0;

  public int destroyCalled = 0;

  public int getHeadersCalled = 0;

  public int handleFaultCalled = 0;

  public int handleRequestCalled = 0;

  public int handleResponseCalled = 0;

  public int doingHandlerWork = 0;

  private QName[] headers = null;

  public void preinvoke() {
    doingHandlerWork = 1;
    if (destroyCalled > 0)
      throw new javax.xml.rpc.JAXRPCException(
          "Violation of WS4EE Assertion 91 - Handler used after destroy called");
  }

  public void postinvoke() {
    doingHandlerWork = 0;
  }

  public void init(HandlerInfo config) {
    initCalled++;
    HandlerTracker.reportInit(this);
  }

  public void destroy() {
    if (doingHandlerWork > 0)
      throw new javax.xml.rpc.JAXRPCException(
          "Violation of WS4EE Assertion 90 - destroy called during handler usage");
    destroyCalled++;
    HandlerTracker.reportDestroy(this);
  }

  public QName[] getHeaders() {
    getHeadersCalled++;
    HandlerTracker.reportGetHeaders(this);
    return headers;
  }

  public boolean handleFault(MessageContext context) {
    System.out.println("handleFault");
    try {
      preinvoke();
      handleFaultCalled++;
      HandlerTracker.reportHandleFault(this);
      doFault((SOAPMessageContext) context);
    } finally {
      postinvoke();
    }
    return true;
  }

  public boolean handleRequest(MessageContext context) {
    System.out.println("handleRequest");
    try {
      preinvoke();
      handleRequestCalled++;
      HandlerTracker.reportHandleRequest(this);
      setRequestMessageContextProperties(context);
      doRequest((SOAPMessageContext) context);
    } finally {
      postinvoke();
    }
    return true;
  }

  public boolean handleResponse(MessageContext context) {
    System.out.println("handleResponse");
    try {
      preinvoke();
      handleResponseCalled++;
      HandlerTracker.reportHandleResponse(this);
      setResponseMessageContextProperties(context);
      doResponse((SOAPMessageContext) context);
    } finally {
      postinvoke();
    }
    return true;
  }

  private void doRequest(SOAPMessageContext context) {
    if (this.getClass().getName().indexOf("ClientHandler1") != -1) {
      SOAPMessage s = context.getMessage();
      HandlerTracker.reportGetMessage("ClientHandler1Request");
      context.setMessage(s);
      HandlerTracker.reportSetMessage("ClientHandler1Request");
    }
    if (this.getClass().getName().indexOf("ServerHandler1") != -1) {
      SOAPMessage s = context.getMessage();
      HandlerTracker.reportGetMessage("ServerHandler1Request");
      context.setMessage(s);
      HandlerTracker.reportSetMessage("ServerHandler1Request");
    }
  }

  private void doResponse(SOAPMessageContext context) {
    if (this.getClass().getName().indexOf("ClientHandler1") != -1) {
      SOAPMessage s = context.getMessage();
      HandlerTracker.reportGetMessage("ClientHandler1Response");
      context.setMessage(s);
      HandlerTracker.reportSetMessage("ClientHandler1Response");
    }
    if (this.getClass().getName().indexOf("ServerHandler1") != -1) {
      SOAPMessage s = context.getMessage();
      HandlerTracker.reportGetMessage("ServerHandler1Response");
      context.setMessage(s);
      HandlerTracker.reportSetMessage("ServerHandler1Response");
    }
  }

  public void doFault(SOAPMessageContext context) {
  }

  private void setRequestMessageContextProperties(MessageContext context) {
    try {
      if (this.getClass().getName().indexOf("ClientHandler1") != -1) {
        context.setProperty("ClientReqProp1", "Value1");
        HandlerTracker.reportSetProperty("ClientReqProp1", "Value1");
        context.setProperty("ClientReqProp2", "Value2");
        HandlerTracker.reportSetProperty("ClientReqProp2", "Value2");
        HandlerTracker.reportGetProperty("ClientReqProp1",
            (String) context.getProperty("ClientReqProp1"));
        HandlerTracker.reportGetProperty("ClientReqProp2",
            (String) context.getProperty("ClientReqProp2"));
        HandlerTracker.reportGetPropertyNames(context.getPropertyNames());
        HandlerTracker.reportContainsProperty("ClientReqProp1",
            context.containsProperty("ClientReqProp1"));
        HandlerTracker.reportContainsProperty("ClientReqProp2",
            context.containsProperty("ClientReqProp2"));
        context.removeProperty("ClientReqProp1");
        HandlerTracker.reportRemoveProperty("ClientReqProp1",
            !context.containsProperty("ClientReqProp1"));
        context.removeProperty("ClientReqProp2");
        HandlerTracker.reportRemoveProperty("ClientReqProp2",
            !context.containsProperty("ClientReqProp2"));
      }
      if (this.getClass().getName().indexOf("ServerHandler1") != -1) {
        context.setProperty("ServerReqProp1", "Value1");
        HandlerTracker.reportSetProperty("ServerReqProp1", "Value1");
        context.setProperty("ServerReqProp2", "Value2");
        HandlerTracker.reportSetProperty("ServerReqProp2", "Value2");
        HandlerTracker.reportGetProperty("ServerReqProp1",
            (String) context.getProperty("ServerReqProp1"));
        HandlerTracker.reportGetProperty("ServerReqProp2",
            (String) context.getProperty("ServerReqProp2"));
        HandlerTracker.reportGetPropertyNames(context.getPropertyNames());
        HandlerTracker.reportContainsProperty("ServerReqProp1",
            context.containsProperty("ServerReqProp1"));
        HandlerTracker.reportContainsProperty("ServerReqProp2",
            context.containsProperty("ServerReqProp2"));
        context.removeProperty("ServerReqProp1");
        HandlerTracker.reportRemoveProperty("ServerReqProp1",
            !context.containsProperty("ServerReqProp1"));
        context.removeProperty("ServerReqProp2");
        HandlerTracker.reportRemoveProperty("ServerReqProp2",
            !context.containsProperty("ServerReqProp2"));
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
    }
  }

  private void setResponseMessageContextProperties(MessageContext context) {
    try {
      if (this.getClass().getName().indexOf("ClientHandler1") != -1) {
        context.setProperty("ClientRespProp1", "Value1");
        HandlerTracker.reportSetProperty("ClientRespProp1", "Value1");
        context.setProperty("ClientRespProp2", "Value2");
        HandlerTracker.reportSetProperty("ClientRespProp2", "Value2");
        HandlerTracker.reportGetProperty("ClientRespProp1",
            (String) context.getProperty("ClientRespProp1"));
        HandlerTracker.reportGetProperty("ClientRespProp2",
            (String) context.getProperty("ClientRespProp2"));
        HandlerTracker.reportGetPropertyNames(context.getPropertyNames());
        HandlerTracker.reportContainsProperty("ClientRespProp1",
            context.containsProperty("ClientRespProp1"));
        HandlerTracker.reportContainsProperty("ClientRespProp2",
            context.containsProperty("ClientRespProp2"));
        context.removeProperty("ClientRespProp1");
        HandlerTracker.reportRemoveProperty("ClientRespProp1",
            !context.containsProperty("ClientRespProp1"));
        context.removeProperty("ClientRespProp2");
        HandlerTracker.reportRemoveProperty("ClientRespProp2",
            !context.containsProperty("ClientRespProp2"));
      }
      if (this.getClass().getName().indexOf("ServerHandler1") != -1) {
        context.setProperty("ServerRespProp1", "Value1");
        HandlerTracker.reportSetProperty("ServerRespProp1", "Value1");
        context.setProperty("ServerRespProp2", "Value2");
        HandlerTracker.reportSetProperty("ServerRespProp2", "Value2");
        HandlerTracker.reportGetProperty("ServerRespProp1",
            (String) context.getProperty("ServerRespProp1"));
        HandlerTracker.reportGetProperty("ServerRespProp2",
            (String) context.getProperty("ServerRespProp2"));
        HandlerTracker.reportGetPropertyNames(context.getPropertyNames());
        HandlerTracker.reportContainsProperty("ServerRespProp1",
            context.containsProperty("ServerRespProp1"));
        HandlerTracker.reportContainsProperty("ServerRespProp2",
            context.containsProperty("ServerRespProp2"));
        context.removeProperty("ServerRespProp1");
        HandlerTracker.reportRemoveProperty("ServerRespProp1",
            !context.containsProperty("ServerRespProp1"));
        context.removeProperty("ServerRespProp2");
        HandlerTracker.reportRemoveProperty("ServerRespProp2",
            !context.containsProperty("ServerRespProp2"));
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
    }
  }
}
