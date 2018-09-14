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

/*
 * $Id$
 */

package com.sun.ts.tests.jaxws.common;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;

import javax.xml.soap.*;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.soap.*;
import javax.xml.ws.handler.*;
import javax.xml.ws.handler.soap.*;
import javax.xml.transform.Source;
import javax.xml.bind.JAXBContext;
import javax.xml.namespace.QName;

import javax.annotation.PreDestroy;
import javax.annotation.PostConstruct;

import java.util.HashSet;
import java.util.Set;
import java.util.Map;
import java.util.List;
import java.util.Iterator;
import javax.activation.DataHandler;

import com.sun.ts.tests.jaxws.common.Handler_Util;
import com.sun.ts.tests.jaxws.common.JAXWS_Util;

public class SOAPHandlerBase2
    implements javax.xml.ws.handler.soap.SOAPHandler<SOAPMessageContext> {

  private int doingHandlerWork = 0;

  private int destroyCalled = 0;

  private String whichHandlerType = null;

  private String handlerName = null;

  public void setWhichHandlerType(String w) {
    this.whichHandlerType = w;
  }

  public String getWhichHandlerType() {
    return this.whichHandlerType;
  }

  public void setHandlerName(String h) {
    this.handlerName = h;
  }

  public String getHandlerName() {
    return this.handlerName;
  }

  public void preinvoke() {
    doingHandlerWork++;
    if (destroyCalled > 0)
      HandlerTracker.reportThrowable(this, new Exception(
          "Violation of Handler Lifecycle - Handler used after destroy called"));
  }

  public void postinvoke() {
    doingHandlerWork = 0;
  }

  @PostConstruct
  public void myInit() {
    TestUtil.logTrace("in " + this + ":myInit");
    HandlerTracker.reportInit(this, "myInit");
  }

  @PreDestroy
  public void myDestroy() {
    TestUtil.logTrace("in " + this + ":myDestroy");
    if (doingHandlerWork > 0)
      HandlerTracker.reportThrowable(this, new Exception(
          "Violation of Handler Lifecycle - destroy called during handler usage"));
    HandlerTracker.reportDestroy(this, "myDestroy");
    destroyCalled++;
  }

  public Set<QName> getHeaders() {
    HandlerTracker.reportGetHeaders(this);
    return new HashSet<QName>();
  }

  public boolean handleMessage(SOAPMessageContext context) {
    System.out.println("in " + this + ":handleMessage");
    TestUtil.logTrace("in " + this + ":handleMessage");

    try {
      preinvoke();
      Handler_Util.setTraceFlag(
          Handler_Util.getValueFromMsg(this, context, "harnesslogtraceflag"));

      Handler_Util.initTestUtil(this,
          Handler_Util.getValueFromMsg(this, context, "harnessloghost"),
          Handler_Util.getValueFromMsg(this, context, "harnesslogport"),
          Handler_Util.getValueFromMsg(this, context, "harnesslogtraceflag"));

      String direction = Handler_Util.getDirection(context);
      if (!Handler_Util.checkForMsg(this, context, "GetTrackerData")) {
        HandlerTracker.reportHandleMessage(this, direction);
      } else {
        TestUtil.logTrace("found GetTrackerData message, handler will ignore");
      }
    } finally {
      postinvoke();
    }
    System.out.println("exiting " + this + ":handleMessage");
    TestUtil.logTrace("exiting " + this + ":handleMessage");
    return true;
  }

  public void close(MessageContext context) {
    TestUtil.logTrace("in " + this + ":close");
    try {
      preinvoke();
      HandlerTracker.reportClose(this);
    } finally {
      postinvoke();
    }
  }

  public boolean handleFault(SOAPMessageContext context) {
    System.out.println("in " + this + ":handleFault");
    TestUtil.logTrace("in " + this + ":handleFault");
    try {
      preinvoke();
      HandlerTracker.reportHandleFault(this);
    } finally {
      postinvoke();
    }
    System.out.println("exiting " + this + ":handleFault");
    TestUtil.logTrace("exiting " + this + ":handleFault");
    return true;
  }

}
