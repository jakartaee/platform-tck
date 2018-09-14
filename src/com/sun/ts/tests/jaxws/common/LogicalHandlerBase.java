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

import java.util.Iterator;
import java.util.Map;
import java.util.List;
import javax.xml.soap.*;
import javax.xml.ws.soap.*;
import javax.xml.ws.handler.*;
import javax.xml.ws.LogicalMessage;
import javax.xml.ws.WebServiceContext;

import javax.activation.DataHandler;

import javax.annotation.PreDestroy;
import javax.annotation.PostConstruct;

import com.sun.ts.tests.jaxws.common.Handler_Util;

public class LogicalHandlerBase
    implements javax.xml.ws.handler.LogicalHandler<LogicalMessageContext> {
  private int destroyCalled = 0;

  private int doingHandlerWork = 0;

  private static final String NAMESPACEURI = "http://handlerservice.org/wsdl";

  private static final String PORT_NAME1 = "handlerservice.portname.1";

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
    System.out.println("in " + this + ":myInit");
    HandlerTracker.reportInit(this, "myInit");
  }

  @PreDestroy
  public void myDestroy() {
    System.out.println("in " + this + ":myDestroy");
    if (doingHandlerWork > 0)
      HandlerTracker.reportThrowable(this, new Exception(
          "Violation of Handler Lifecycle - destroy called during handler usage"));

    destroyCalled++;
    HandlerTracker.reportDestroy(this, "myDestroy");
  }

  public boolean handleMessage(LogicalMessageContext context) {
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

      if (!Handler_Util.checkForMsg(this, context, "GetTrackerData")) {
        String direction = Handler_Util.getDirection(context);
        HandlerTracker.reportHandleMessage(this, direction);
        if (Handler_Util.checkForMsg(this, context, "LogicalTest</testType>")) {
          if (Handler_Util.checkForMsg(this, context, "MessageContextTest")) {
            doLogicalMessageContext(context, direction);
            doMessageContext(context, direction);
          } else if (Handler_Util.checkForMsg(this, context,
              "ContextPropertiesTest")) {
            doContextProperties(context, direction);
          }
        }
      } else {
        TestUtil.logTrace("found GetTrackerData message, handler will ignore");
      }
    } catch (Exception e) {
      HandlerTracker.reportThrowable(this, e);
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

  public boolean handleFault(LogicalMessageContext context) {
    System.out.println("in " + this + ":handleFault");
    TestUtil.logTrace("in " + this + ":handleFault");
    try {
      preinvoke();
      HandlerTracker.reportHandleFault(this);
    } finally {
      postinvoke();
    }
    TestUtil.logTrace("exiting " + this + ":handleFault");
    System.out.println("exiting " + this + ":handleFault");
    return true;
  }

  private void doContextProperties(LogicalMessageContext context,
      String direction) {
    TestUtil.logTrace("in doLogicalMessageContext");
    TestUtil.logTrace("direction=" + direction);
    TestUtil.logTrace("handlerName=" + handlerName);
    if (handlerName.equals("ClientLogicalHandler2")
        || handlerName.equals("ServerLogicalHandler2")) {
      Map<String, DataHandler> m1 = null;
      String attachmentProp = "";
      if (direction.equals(Constants.OUTBOUND)) {
        m1 = (Map<String, DataHandler>) context
            .get(LogicalMessageContext.OUTBOUND_MESSAGE_ATTACHMENTS);
        attachmentProp = "LogicalMessageContext.OUTBOUND_MESSAGE_ATTACHMENTS";
      } else {
        m1 = (Map<String, DataHandler>) context
            .get(LogicalMessageContext.INBOUND_MESSAGE_ATTACHMENTS);
        attachmentProp = "LogicalMessageContext.INBOUND_MESSAGE_ATTACHMENTS";
      }
      int cnt = 0;
      Iterator iterator = null;
      String sTmp = "";
      try {
        if (m1 != null) {
          if (m1.size() > 0) {
            iterator = m1.keySet().iterator();
            while (iterator.hasNext()) {
              String key = (String) iterator.next();
              TestUtil.logTrace("request attachments key[" + cnt + "]=" + key);
              sTmp = "key[" + cnt + "]=" + key;
              HandlerTracker.reportProperties(this, direction, attachmentProp,
                  sTmp);
              cnt++;
            }
          } else {
            TestUtil.logTrace("empty");
            HandlerTracker.reportProperties(this, direction, attachmentProp,
                "empty");
          }
        } else {
          TestUtil.logTrace("null");
          HandlerTracker.reportProperties(this, direction, attachmentProp,
              "null");
        }
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }

      if (direction.equals(Constants.OUTBOUND)) {
        m1 = (Map<String, DataHandler>) context
            .get(MessageContext.OUTBOUND_MESSAGE_ATTACHMENTS);
        attachmentProp = "MessageContext.OUTBOUND_MESSAGE_ATTACHMENTS";
      } else {
        m1 = (Map<String, DataHandler>) context
            .get(MessageContext.INBOUND_MESSAGE_ATTACHMENTS);
        attachmentProp = "MessageContext.INBOUND_MESSAGE_ATTACHMENTS";
      }
      cnt = 0;
      iterator = null;
      try {
        if (m1 != null) {
          if (m1.size() > 0) {
            iterator = m1.keySet().iterator();
            while (iterator.hasNext()) {
              String key = (String) iterator.next();
              TestUtil.logTrace("request attachments key[" + cnt + "]=" + key);
              sTmp = "key[" + cnt + "]=" + key;
              HandlerTracker.reportProperties(this, direction, attachmentProp,
                  sTmp);
              cnt++;
            }
          } else {
            TestUtil.logTrace("empty");
            HandlerTracker.reportProperties(this, direction, attachmentProp,
                "empty");
          }
        } else {
          TestUtil.logTrace("null");
          HandlerTracker.reportProperties(this, direction, attachmentProp,
              "null");
        }
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }

      String ss = (String) context
          .get(LogicalMessageContext.HTTP_REQUEST_METHOD);
      if (ss != null) {
        HandlerTracker.reportProperties(this, direction,
            "LogicalMessageContext.HTTP_REQUEST_METHOD", ss);
      } else {
        HandlerTracker.reportProperties(this, direction,
            "LogicalMessageContext.HTTP_REQUEST_METHOD", "null");
      }
      ss = (String) context.get(MessageContext.HTTP_REQUEST_METHOD);
      if (ss != null) {
        HandlerTracker.reportProperties(this, direction,
            "MessageContext.HTTP_REQUEST_METHOD", ss);
      } else {
        HandlerTracker.reportProperties(this, direction,
            "MessageContext.HTTP_REQUEST_METHOD", "null");
      }
      Integer ii = (Integer) context
          .get(LogicalMessageContext.HTTP_RESPONSE_CODE);
      if (ii != null) {
        HandlerTracker.reportProperties(this, direction,
            "LogicalMessageContext.HTTP_RESPONSE_CODE", ii.toString());
      } else {
        HandlerTracker.reportProperties(this, direction,
            "LogicalMessageContext.HTTP_RESPONSE_CODE", "null");
      }
      ii = (Integer) context.get(MessageContext.HTTP_RESPONSE_CODE);
      if (ii != null) {
        HandlerTracker.reportProperties(this, direction,
            "MessageContext.HTTP_RESPONSE_CODE", ii.toString());
      } else {
        HandlerTracker.reportProperties(this, direction,
            "MessageContext.HTTP_RESPONSE_CODE", "null");
      }

      Map<String, List<String>> m2 = (Map<String, List<String>>) context
          .get(LogicalMessageContext.HTTP_REQUEST_HEADERS);
      StringBuffer sb = new StringBuffer();
      cnt = 0;
      iterator = null;
      try {
        if (m2 != null) {
          if (m2.size() > 0) {
            iterator = m2.keySet().iterator();
            while (iterator.hasNext()) {
              String key = (String) iterator.next();
              TestUtil.logTrace("request headers key[" + cnt + "]=" + key);
              sb.append("key[" + cnt + "]=" + key + "|");
              cnt++;
            }
          } else {
            TestUtil.logTrace("empty");
            sb.append("empty");
          }
        } else {
          TestUtil.logTrace("null");
          sb.append("null");
        }
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
      HandlerTracker.reportProperties(this, direction,
          "LogicalMessageContext.HTTP_REQUEST_HEADERS", sb.toString());

      m2 = (Map<String, List<String>>) context
          .get(MessageContext.HTTP_REQUEST_HEADERS);
      sb = new StringBuffer();
      cnt = 0;
      iterator = null;
      try {
        if (m2 != null) {
          if (m2.size() > 0) {
            iterator = m2.keySet().iterator();
            while (iterator.hasNext()) {
              String key = (String) iterator.next();
              TestUtil.logTrace("request headers key[" + cnt + "]=" + key);
              sb.append("key[" + cnt + "]=" + key + "|");
              cnt++;
            }
          } else {
            TestUtil.logTrace("empty");
            sb.append("empty");
          }
        } else {
          TestUtil.logTrace("null");
          sb.append("null");
        }
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
      HandlerTracker.reportProperties(this, direction,
          "MessageContext.HTTP_REQUEST_HEADERS", sb.toString());

      m2 = (Map<String, List<String>>) context
          .get(LogicalMessageContext.HTTP_RESPONSE_HEADERS);
      sb = new StringBuffer();
      cnt = 0;
      iterator = null;
      try {
        if (m2 != null) {
          if (m2.size() > 0) {
            iterator = m2.keySet().iterator();
            while (iterator.hasNext()) {
              String key = (String) iterator.next();
              TestUtil.logTrace("response headers key[" + cnt + "]=" + key);
              sb.append("key[" + cnt + "]=" + key + "|");
              cnt++;
            }
          } else {
            TestUtil.logTrace("empty");
            sb.append("empty");
          }
        } else {
          TestUtil.logTrace("null");
          sb.append("null");
        }
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
      HandlerTracker.reportProperties(this, direction,
          "LogicalMessageContext.HTTP_RESPONSE_HEADERS", sb.toString());

      m2 = (Map<String, List<String>>) context
          .get(MessageContext.HTTP_RESPONSE_HEADERS);
      sb = new StringBuffer();
      cnt = 0;
      try {
        if (m2 != null) {
          if (m2.size() > 0) {
            iterator = m2.keySet().iterator();
            while (iterator.hasNext()) {
              String key = (String) iterator.next();
              TestUtil.logTrace("response headers key[" + cnt + "]=" + key);
              sb.append("key[" + cnt + "]=" + key + "|");
              cnt++;
            }
          } else {
            TestUtil.logTrace("empty");
            sb.append("empty");
          }
        } else {
          TestUtil.logTrace("null");
          sb.append("null");
        }
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
      HandlerTracker.reportProperties(this, direction,
          "MessageContext.HTTP_RESPONSE_HEADERS", sb.toString());

      Object oo = context.get(LogicalMessageContext.SERVLET_REQUEST);
      if (oo != null) {
        HandlerTracker.reportProperties(this, direction,
            "LogicalMessageContext.SERVLET_REQUEST", oo.toString());
      } else {
        HandlerTracker.reportProperties(this, direction,
            "LogicalMessageContext.SERVLET_REQUEST", "null");
      }
      oo = context.get(MessageContext.SERVLET_REQUEST);
      if (oo != null) {
        HandlerTracker.reportProperties(this, direction,
            "MessageContext.SERVLET_REQUEST", oo.toString());
      } else {
        HandlerTracker.reportProperties(this, direction,
            "MessageContext.SERVLET_REQUEST", "null");
      }
      oo = context.get(LogicalMessageContext.SERVLET_RESPONSE);
      if (oo != null) {
        HandlerTracker.reportProperties(this, direction,
            "LogicalMessageContext.SERVLET_RESPONSE", oo.toString());
      } else {
        HandlerTracker.reportProperties(this, direction,
            "LogicalMessageContext.SERVLET_RESPONSE", "null");
      }
      oo = context.get(MessageContext.SERVLET_RESPONSE);
      if (oo != null) {
        HandlerTracker.reportProperties(this, direction,
            "MessageContext.SERVLET_RESPONSE", oo.toString());
      } else {
        HandlerTracker.reportProperties(this, direction,
            "MessageContext.SERVLET_RESPONSE", "null");
      }
      oo = context.get(LogicalMessageContext.SERVLET_CONTEXT);
      if (oo != null) {
        HandlerTracker.reportProperties(this, direction,
            "LogicalMessageContext.SERVLET_CONTEXT", oo.toString());
      } else {
        HandlerTracker.reportProperties(this, direction,
            "LogicalMessageContext.SERVLET_CONTEXT", "null");
      }
      oo = context.get(MessageContext.SERVLET_CONTEXT);
      if (oo != null) {
        HandlerTracker.reportProperties(this, direction,
            "MessageContext.SERVLET_CONTEXT", oo.toString());
      } else {
        HandlerTracker.reportProperties(this, direction,
            "MessageContext.SERVLET_CONTEXT", "null");
      }

    }
  }

  private void doLogicalMessageContext(LogicalMessageContext context,
      String direction) {
    TestUtil.logTrace("in doLogicalMessageContext");
    TestUtil.logTrace("direction=" + direction);
    TestUtil.logTrace("handlerName=" + handlerName);
    if (handlerName.equals("ClientLogicalHandler2")
        || handlerName.equals("ServerLogicalHandler2")) {
      TestUtil.logTrace("calling context.getMessage()");
      LogicalMessage lm = context.getMessage();
      if (lm != null) {
        HandlerTracker.reportLMCGetMessage(this, direction, "returned nonnull");
      } else {
        HandlerTracker.reportLMCGetMessage(this, direction, "returned null");
      }
    }
  }

  private void doMessageContext(LogicalMessageContext context,
      String direction) {
    String whichDirection = direction.toUpperCase();
    TestUtil.logTrace("in doMessageContext");
    TestUtil.logTrace("whichDirection=" + whichDirection);
    TestUtil.logTrace("handlerName=" + handlerName);
    if (handlerName.equals("ClientLogicalHandler2")
        || handlerName.equals("ServerLogicalHandler2")) {
      HandlerTracker.reportComment(this,
          "BeginMessageContextCallbackInvocations");
      TestUtil.logTrace("whichHandlerType=" + whichHandlerType);

      // set a property that the everyone will add to.

      if (whichHandlerType.equals("Client")) {
        HandlerTracker.reportGet(this,
            whichHandlerType + "To" + whichHandlerType + "Prop",
            (String) context
                .get(whichHandlerType + "To" + whichHandlerType + "Prop"));

        String tmp = (String) context
            .get(whichHandlerType + "To" + whichHandlerType + "Prop");
        tmp = tmp + whichDirection + whichHandlerType + "LogicalHandler2";
        context.put(whichHandlerType + "To" + whichHandlerType + "Prop", tmp);
        HandlerTracker.reportPut(this,
            whichHandlerType + "To" + whichHandlerType + "Prop", tmp);

        context.setScope(whichHandlerType + "To" + whichHandlerType + "Prop",
            MessageContext.Scope.APPLICATION);
        HandlerTracker.reportSetScope(this,
            whichHandlerType + "To" + whichHandlerType + "Prop",
            MessageContext.Scope.APPLICATION.toString());

      } else {
        // server
        String tmp = "";
        if (direction.equals(Constants.OUTBOUND)) {
          HandlerTracker.reportGet(this,
              "Handler" + whichHandlerType + "HandlerProp", (String) context
                  .get("Handler" + whichHandlerType + "HandlerProp"));

          tmp = (String) context
              .get("Handler" + whichHandlerType + "HandlerProp");
        }
        tmp = tmp + whichDirection + whichHandlerType + "LogicalHandler2";
        context.put("Handler" + whichHandlerType + "HandlerProp", tmp);
        HandlerTracker.reportPut(this,
            "Handler" + whichHandlerType + "HandlerProp", tmp);

        context.setScope("Handler" + whichHandlerType + "HandlerProp",
            MessageContext.Scope.APPLICATION);
        HandlerTracker.reportSetScope(this,
            "Handler" + whichHandlerType + "HandlerProp",
            MessageContext.Scope.APPLICATION.toString());
      }
      HandlerTracker.reportComment(this,
          "EndMessageContextCallbackInvocations");

    } else if (handlerName.equals("ClientLogicalHandler1")
        || handlerName.equals("ServerLogicalHandler1")) {
      HandlerTracker.reportComment(this,
          "BeginMessageContextCallbackInvocations");
      if (direction.equals(Constants.OUTBOUND)) {
        HandlerTracker.reportComment(this,
            "Set Properties and see that Handler3 can access them");
        // set a property and see that it can be referenced by a different
        // handler
        context.put(whichDirection + whichHandlerType
            + "LogicalCrossHandlerPropSetByHandler1", "SetByHandler1");
        HandlerTracker.reportPut(this, whichDirection + whichHandlerType
            + "LogicalCrossHandlerPropSetByHandler1", "SetByHandler1");

        // set various scoped properties and see that they can be referenced by
        // a different handler
        context.put(whichDirection + whichHandlerType
            + "LogicalMessageScopeAppPropSetByHandler1", "SetByHandler1");
        HandlerTracker.reportPut(this, whichDirection + whichHandlerType
            + "LogicalMessageScopeAppPropSetByHandler1", "SetByHandler1");
        context.setScope(
            whichDirection + whichHandlerType
                + "LogicalMessageScopeAppPropSetByHandler1",
            MessageContext.Scope.APPLICATION);
        HandlerTracker.reportSetScope(this,
            whichDirection + whichHandlerType
                + "LogicalMessageScopeAppPropSetByHandler1",
            MessageContext.Scope.APPLICATION.toString());

        context.put(
            whichDirection + whichHandlerType
                + "LogicalMessageScopeHandlerPropSetByHandler1",
            "SetByHandler1");
        HandlerTracker.reportPut(this,
            whichDirection + whichHandlerType
                + "LogicalMessageScopeHandlerPropSetByHandler1",
            "SetByHandler1");
        context.setScope(
            whichDirection + whichHandlerType
                + "LogicalMessageScopeHandlerPropSetByHandler1",
            MessageContext.Scope.HANDLER);
        HandlerTracker.reportSetScope(this,
            whichDirection + whichHandlerType
                + "LogicalMessageScopeHandlerPropSetByHandler1",
            MessageContext.Scope.HANDLER.toString());

      } else if (direction.equals(Constants.INBOUND)) {
        HandlerTracker.reportGet(this,
            whichDirection + whichHandlerType
                + "LogicalCrossHandlerPropSetByHandler3",
            (String) context.get(whichDirection + whichHandlerType
                + "LogicalCrossHandlerPropSetByHandler3"));
        HandlerTracker.reportGet(this,
            whichDirection + whichHandlerType
                + "LogicalMessageScopeAppPropSetByHandler3",
            (String) context.get(whichDirection + whichHandlerType
                + "LogicalMessageScopeAppPropSetByHandler3"));
        HandlerTracker.reportGet(this,
            whichDirection + whichHandlerType
                + "LogicalMessageScopeHandlerPropSetByHandler3",
            (String) context.get(whichDirection + whichHandlerType
                + "LogicalMessageScopeHandlerPropSetByHandler3"));
        HandlerTracker.reportGetScope(this,
            whichDirection + whichHandlerType
                + "LogicalCrossHandlerPropSetByHandler3",
            context.getScope(whichDirection + whichHandlerType
                + "LogicalCrossHandlerPropSetByHandler3").toString());
        HandlerTracker
            .reportGetScope(this,
                whichDirection + whichHandlerType
                    + "LogicalMessageScopeAppPropSetByHandler3",
                context
                    .getScope(whichDirection + whichHandlerType
                        + "LogicalMessageScopeAppPropSetByHandler3")
                    .toString());
        HandlerTracker.reportGetScope(this,
            whichDirection + whichHandlerType
                + "LogicalMessageScopeHandlerPropSetByHandler3",
            context
                .getScope(whichDirection + whichHandlerType
                    + "LogicalMessageScopeHandlerPropSetByHandler3")
                .toString());
      }
      HandlerTracker.reportComment(this,
          "EndMessageContextCallbackInvocations");

    } else if (handlerName.equals("ClientLogicalHandler3")
        || handlerName.equals("ServerLogicalHandler3")) {
      HandlerTracker.reportComment(this,
          "BeginMessageContextCallbackInvocations");
      if (direction.equals(Constants.OUTBOUND)) {
        TestUtil.logTrace("whichHandlerType=" + whichHandlerType);
        // get a property that was set by the endpoint
        HandlerTracker.reportGet(this,
            whichDirection + whichHandlerType
                + "LogicalCrossHandlerPropSetByHandler1",
            (String) context.get(whichDirection + whichHandlerType
                + "LogicalCrossHandlerPropSetByHandler1"));
        HandlerTracker.reportGet(this,
            whichDirection + whichHandlerType
                + "LogicalMessageScopeAppPropSetByHandler1",
            (String) context.get(whichDirection + whichHandlerType
                + "LogicalMessageScopeAppPropSetByHandler1"));
        HandlerTracker.reportGet(this,
            whichDirection + whichHandlerType
                + "LogicalMessageScopeHandlerPropSetByHandler1",
            (String) context.get(whichDirection + whichHandlerType
                + "LogicalMessageScopeHandlerPropSetByHandler1"));

        HandlerTracker.reportGetScope(this,
            whichDirection + whichHandlerType
                + "LogicalCrossHandlerPropSetByHandler1",
            context.getScope(whichDirection + whichHandlerType
                + "LogicalCrossHandlerPropSetByHandler1").toString());
        HandlerTracker
            .reportGetScope(this,
                whichDirection + whichHandlerType
                    + "LogicalMessageScopeAppPropSetByHandler1",
                context
                    .getScope(whichDirection + whichHandlerType
                        + "LogicalMessageScopeAppPropSetByHandler1")
                    .toString());
        HandlerTracker.reportGetScope(this,
            whichDirection + whichHandlerType
                + "LogicalMessageScopeHandlerPropSetByHandler1",
            context
                .getScope(whichDirection + whichHandlerType
                    + "LogicalMessageScopeHandlerPropSetByHandler1")
                .toString());

      } else if (direction.equals(Constants.INBOUND)) {

        // set a property and see that it can be referenced by a different
        // handler
        context.put(whichDirection + whichHandlerType
            + "LogicalCrossHandlerPropSetByHandler3", "SetByHandler3");
        HandlerTracker.reportPut(this, whichDirection + whichHandlerType
            + "LogicalCrossHandlerPropSetByHandler3", "SetByHandler3");
        // set various scoped properties and see that they can be referenced by
        // a different handler
        context.put(whichDirection + whichHandlerType
            + "LogicalMessageScopeAppPropSetByHandler3", "SetByHandler3");
        HandlerTracker.reportPut(this, whichDirection + whichHandlerType
            + "LogicalMessageScopeAppPropSetByHandler3", "SetByHandler3");
        context.setScope(
            whichDirection + whichHandlerType
                + "LogicalMessageScopeAppPropSetByHandler3",
            MessageContext.Scope.APPLICATION);
        HandlerTracker.reportSetScope(this,
            whichDirection + whichHandlerType
                + "LogicalMessageScopeAppPropSetByHandler3",
            MessageContext.Scope.APPLICATION.toString());

        context.put(
            whichDirection + whichHandlerType
                + "LogicalMessageScopeHandlerPropSetByHandler3",
            "SetByHandler3");
        HandlerTracker.reportPut(this,
            whichDirection + whichHandlerType
                + "LogicalMessageScopeHandlerPropSetByHandler3",
            "SetByHandler3");
        context.setScope(
            whichDirection + whichHandlerType
                + "LogicalMessageScopeHandlerPropSetByHandler3",
            MessageContext.Scope.HANDLER);
        HandlerTracker.reportSetScope(this,
            whichDirection + whichHandlerType
                + "LogicalMessageScopeHandlerPropSetByHandler3",
            MessageContext.Scope.HANDLER.toString());

      }
      HandlerTracker.reportComment(this,
          "EndMessageContextCallbackInvocations");
    }
  }

}
