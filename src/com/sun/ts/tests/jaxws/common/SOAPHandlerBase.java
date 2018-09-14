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

public class SOAPHandlerBase
    implements javax.xml.ws.handler.soap.SOAPHandler<SOAPMessageContext> {

  private int doingHandlerWork = 0;

  private int destroyCalled = 0;

  private static final String NAMESPACEURI = "http://dlhandlerservice.org/wsdl";

  private static final String PORT_NAME1 = "HelloPort";

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

  private static final String JAXB_OBJECT_CLIENT_FACTORY = "com.sun.ts.tests.jaxws.sharedclients.dlhandlerclient.ObjectFactory";

  private static final String JAXB_OBJECT_SERVER_FACTORY = "com.sun.ts.tests.jaxws.sharedwebservices.dlhandlerservice.ObjectFactory";

  private JAXBContext createJAXBContext() {
    Class c = null;
    JAXBContext jbc = null;
    try {
      if (whichHandlerType.equals("Client")) {
        TestUtil.logTrace(
            "Getting the object factory:" + JAXB_OBJECT_CLIENT_FACTORY);
        c = Class.forName(JAXB_OBJECT_CLIENT_FACTORY);
      } else {
        TestUtil.logTrace(
            "Getting the object factory:" + JAXB_OBJECT_SERVER_FACTORY);
        c = Class.forName(JAXB_OBJECT_SERVER_FACTORY);
      }
      jbc = JAXBContext.newInstance(c);
    } catch (Exception e) {
      throw new WebServiceException(e);
    }
    return jbc;
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
        if (Handler_Util.checkForMsg(this, context, "SOAPTest</testType>")) {
          if (Handler_Util.checkForMsg(this, context, "MessageContextTest")) {
            doSOAPMessageContext(context, direction);
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

  private void doContextProperties(SOAPMessageContext context,
      String direction) {
    TestUtil.logTrace("in doSOAPMessageContext");
    TestUtil.logTrace("direction=" + direction);
    TestUtil.logTrace("handlerName=" + handlerName);
    if (handlerName.equals("ClientSOAPHandler2")
        || handlerName.equals("ServerSOAPHandler2")) {
      Map<String, DataHandler> m1 = null;
      String attachmentProp = "";
      if (direction.equals(Constants.OUTBOUND)) {
        m1 = (Map<String, DataHandler>) context
            .get(SOAPMessageContext.OUTBOUND_MESSAGE_ATTACHMENTS);
        attachmentProp = "SOAPMessageContext.OUTBOUND_MESSAGE_ATTACHMENTS";
      } else {
        m1 = (Map<String, DataHandler>) context
            .get(SOAPMessageContext.INBOUND_MESSAGE_ATTACHMENTS);
        attachmentProp = "SOAPMessageContext.INBOUND_MESSAGE_ATTACHMENTS";
      }
      String sTmp = "";
      int cnt = 0;
      Iterator iterator = null;
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

      String ss = (String) context.get(SOAPMessageContext.HTTP_REQUEST_METHOD);
      if (ss != null) {
        HandlerTracker.reportProperties(this, direction,
            "SOAPMessageContext.HTTP_REQUEST_METHOD", ss);
      } else {
        HandlerTracker.reportProperties(this, direction,
            "SOAPMessageContext.HTTP_REQUEST_METHOD", "null");
      }
      ss = (String) context.get(MessageContext.HTTP_REQUEST_METHOD);
      if (ss != null) {
        HandlerTracker.reportProperties(this, direction,
            "MessageContext.HTTP_REQUEST_METHOD", ss);
      } else {
        HandlerTracker.reportProperties(this, direction,
            "MessageContext.HTTP_REQUEST_METHOD", "null");
      }
      Integer ii = (Integer) context.get(SOAPMessageContext.HTTP_RESPONSE_CODE);
      if (ii != null) {
        HandlerTracker.reportProperties(this, direction,
            "SOAPMessageContext.HTTP_RESPONSE_CODE", ii.toString());
      } else {
        HandlerTracker.reportProperties(this, direction,
            "SOAPMessageContext.HTTP_RESPONSE_CODE", "null");
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
          .get(SOAPMessageContext.HTTP_REQUEST_HEADERS);
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
          "SOAPMessageContext.HTTP_REQUEST_HEADERS", sb.toString());

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
          .get(SOAPMessageContext.HTTP_RESPONSE_HEADERS);
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
          "SOAPMessageContext.HTTP_RESPONSE_HEADERS", sb.toString());

      m2 = (Map<String, List<String>>) context
          .get(MessageContext.HTTP_RESPONSE_HEADERS);
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
          "MessageContext.HTTP_RESPONSE_HEADERS", sb.toString());

      Object oo = context.get(SOAPMessageContext.SERVLET_REQUEST);
      if (oo != null) {
        HandlerTracker.reportProperties(this, direction,
            "SOAPMessageContext.SERVLET_REQUEST", oo.toString());
      } else {
        HandlerTracker.reportProperties(this, direction,
            "SOAPMessageContext.SERVLET_REQUEST", "null");
      }
      oo = context.get(MessageContext.SERVLET_REQUEST);
      if (oo != null) {
        HandlerTracker.reportProperties(this, direction,
            "MessageContext.SERVLET_REQUEST", oo.toString());
      } else {
        HandlerTracker.reportProperties(this, direction,
            "MessageContext.SERVLET_REQUEST", "null");
      }
      oo = context.get(SOAPMessageContext.SERVLET_RESPONSE);
      if (oo != null) {
        HandlerTracker.reportProperties(this, direction,
            "SOAPMessageContext.SERVLET_RESPONSE", oo.toString());
      } else {
        HandlerTracker.reportProperties(this, direction,
            "SOAPMessageContext.SERVLET_RESPONSE", "null");
      }
      oo = context.get(MessageContext.SERVLET_RESPONSE);
      if (oo != null) {
        HandlerTracker.reportProperties(this, direction,
            "MessageContext.SERVLET_RESPONSE", oo.toString());
      } else {
        HandlerTracker.reportProperties(this, direction,
            "MessageContext.SERVLET_RESPONSE", "null");
      }
      oo = context.get(SOAPMessageContext.SERVLET_CONTEXT);
      if (oo != null) {
        HandlerTracker.reportProperties(this, direction,
            "SOAPMessageContext.SERVLET_CONTEXT", oo.toString());
      } else {
        HandlerTracker.reportProperties(this, direction,
            "SOAPMessageContext.SERVLET_CONTEXT", "null");
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

  private void doSOAPMessageContext(SOAPMessageContext context,
      String direction) {
    TestUtil.logTrace("in doSOAPMessageContext");
    TestUtil.logTrace("direction=" + direction);
    TestUtil.logTrace("handlerName" + handlerName);
    if (handlerName.equals("ClientSOAPHandler2")
        || handlerName.equals("ServerSOAPHandler2")) {
      TestUtil.logTrace("calling context.getMessage()");
      SOAPMessage sm = context.getMessage();

      if (sm != null) {
        HandlerTracker.reportSMCGetMessage(this, direction,
            JAXWS_Util.getSOAPMessageAsString(sm));
      } else {
        HandlerTracker.reportSMCGetMessage(this, direction, "null");
      }

      context.setMessage(sm);
      HandlerTracker.reportSMCSetMessage(this, direction);
      HandlerTracker.reportSMCGetRoles(this, direction, context.getRoles());
      JAXBContext jbc = createJAXBContext();
      if (jbc != null) {
        QName qname = new QName(NAMESPACEURI, PORT_NAME1);
        TestUtil.logTrace("qname=" + qname);
        HandlerTracker.reportSMCGetHeaders(this, direction,
            context.getHeaders(qname, jbc, true));
      } else {
        throw new WebServiceException(
            direction + this + "The JAXBContext returned was null");
      }
    }
  }

  private void doMessageContext(SOAPMessageContext context, String direction) {
    String whichDirection = direction.toUpperCase();
    TestUtil.logTrace("in doMessageContext");
    TestUtil.logTrace("whichDirection=" + whichDirection);
    TestUtil.logTrace("handlerName=" + handlerName);
    if (handlerName.equals("ClientSOAPHandler2")
        || handlerName.equals("ServerSOAPHandler2")) {
      HandlerTracker.reportComment(this,
          "BeginSOAPMessageContextCallbackInvocations");
      TestUtil.logTrace("whichHandlerType=" + whichHandlerType);

      // set a property that the everyone will add to.

      if (whichHandlerType.equals("Client")) {
        HandlerTracker.reportGet(this,
            whichHandlerType + "To" + whichHandlerType + "Prop",
            (String) context
                .get(whichHandlerType + "To" + whichHandlerType + "Prop"));

        String tmp = (String) context
            .get(whichHandlerType + "To" + whichHandlerType + "Prop");
        tmp = tmp + whichDirection + handlerName;
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
        tmp = tmp + whichDirection + handlerName;
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
    } else if (handlerName.equals("ClientSOAPHandler1")
        || handlerName.equals("ServerSOAPHandler1")) {
      HandlerTracker.reportComment(this,
          "BeginSOAPMessageContextCallbackInvocations");

      if (direction.equals(Constants.OUTBOUND)) {
        HandlerTracker.reportComment(this,
            "Set Properties and see that Handler3 can access them");
        // set a property and see that it can be referenced by a different
        // handler
        context.put(whichDirection + whichHandlerType
            + "SOAPCrossHandlerPropSetByHandler1", "SetByHandler1");
        HandlerTracker.reportPut(this, whichDirection + whichHandlerType
            + "SOAPCrossHandlerPropSetByHandler1", "SetByHandler1");

        // set various scoped properties and see that they can be referenced by
        // a different handler
        context.put(whichDirection + whichHandlerType
            + "SOAPMessageScopeAppPropSetByHandler1", "SetByHandler1");
        HandlerTracker.reportPut(this, whichDirection + whichHandlerType
            + "SOAPMessageScopeAppPropSetByHandler1", "SetByHandler1");
        context.setScope(
            whichDirection + whichHandlerType
                + "SOAPMessageScopeAppPropSetByHandler1",
            MessageContext.Scope.APPLICATION);
        HandlerTracker.reportSetScope(this,
            whichDirection + whichHandlerType
                + "SOAPMessageScopeAppPropSetByHandler1",
            MessageContext.Scope.APPLICATION.toString());

        context.put(whichDirection + whichHandlerType
            + "SOAPMessageScopeHandlerPropSetByHandler1", "SetByHandler1");
        HandlerTracker.reportPut(this, whichDirection + whichHandlerType
            + "SOAPMessageScopeHandlerPropSetByHandler1", "SetByHandler1");
        context.setScope(
            whichDirection + whichHandlerType
                + "SOAPMessageScopeHandlerPropSetByHandler1",
            MessageContext.Scope.HANDLER);
        HandlerTracker.reportSetScope(this,
            whichDirection + whichHandlerType
                + "SOAPMessageScopeHandlerPropSetByHandler1",
            MessageContext.Scope.HANDLER.toString());

      } else if (direction.equals(Constants.INBOUND)) {
        HandlerTracker.reportGet(this,
            whichDirection + whichHandlerType
                + "SOAPCrossHandlerPropSetByHandler3",
            (String) context.get(whichDirection + whichHandlerType
                + "SOAPCrossHandlerPropSetByHandler3"));
        HandlerTracker.reportGet(this,
            whichDirection + whichHandlerType
                + "SOAPMessageScopeAppPropSetByHandler3",
            (String) context.get(whichDirection + whichHandlerType
                + "SOAPMessageScopeAppPropSetByHandler3"));
        HandlerTracker.reportGet(this,
            whichDirection + whichHandlerType
                + "SOAPMessageScopeHandlerPropSetByHandler3",
            (String) context.get(whichDirection + whichHandlerType
                + "SOAPMessageScopeHandlerPropSetByHandler3"));
        HandlerTracker.reportGetScope(this,
            whichDirection + whichHandlerType
                + "SOAPCrossHandlerPropSetByHandler3",
            context.getScope(whichDirection + whichHandlerType
                + "SOAPCrossHandlerPropSetByHandler3").toString());
        HandlerTracker.reportGetScope(this,
            whichDirection + whichHandlerType
                + "SOAPMessageScopeAppPropSetByHandler3",
            context.getScope(whichDirection + whichHandlerType
                + "SOAPMessageScopeAppPropSetByHandler3").toString());
        HandlerTracker
            .reportGetScope(this,
                whichDirection + whichHandlerType
                    + "SOAPMessageScopeHandlerPropSetByHandler3",
                context
                    .getScope(whichDirection + whichHandlerType
                        + "SOAPMessageScopeHandlerPropSetByHandler3")
                    .toString());
      }
      HandlerTracker.reportComment(this,
          "EndMessageContextCallbackInvocations");

    } else if (handlerName.equals("ClientSOAPHandler3")
        || handlerName.equals("ServerSOAPHandler3")) {
      HandlerTracker.reportComment(this,
          "BeginSOAPMessageContextCallbackInvocations");
      if (direction.equals(Constants.OUTBOUND)) {
        TestUtil.logTrace("whichHandlerType=" + whichHandlerType);
        // get a property that was set by the endpoint
        HandlerTracker.reportGet(this,
            whichDirection + whichHandlerType
                + "SOAPCrossHandlerPropSetByHandler1",
            (String) context.get(whichDirection + whichHandlerType
                + "SOAPCrossHandlerPropSetByHandler1"));
        HandlerTracker.reportGet(this,
            whichDirection + whichHandlerType
                + "SOAPMessageScopeAppPropSetByHandler1",
            (String) context.get(whichDirection + whichHandlerType
                + "SOAPMessageScopeAppPropSetByHandler1"));
        HandlerTracker.reportGet(this,
            whichDirection + whichHandlerType
                + "SOAPMessageScopeHandlerPropSetByHandler1",
            (String) context.get(whichDirection + whichHandlerType
                + "SOAPMessageScopeHandlerPropSetByHandler1"));

        HandlerTracker.reportGetScope(this,
            whichDirection + whichHandlerType
                + "SOAPCrossHandlerPropSetByHandler1",
            context.getScope(whichDirection + whichHandlerType
                + "SOAPCrossHandlerPropSetByHandler1").toString());
        HandlerTracker.reportGetScope(this,
            whichDirection + whichHandlerType
                + "SOAPMessageScopeAppPropSetByHandler1",
            context.getScope(whichDirection + whichHandlerType
                + "SOAPMessageScopeAppPropSetByHandler1").toString());
        HandlerTracker
            .reportGetScope(this,
                whichDirection + whichHandlerType
                    + "SOAPMessageScopeHandlerPropSetByHandler1",
                context
                    .getScope(whichDirection + whichHandlerType
                        + "SOAPMessageScopeHandlerPropSetByHandler1")
                    .toString());

      } else if (direction.equals(Constants.INBOUND)) {

        // set a property and see that it can be referenced by a different
        // handler
        context.put(whichDirection + whichHandlerType
            + "SOAPCrossHandlerPropSetByHandler3", "SetByHandler3");
        HandlerTracker.reportPut(this, whichDirection + whichHandlerType
            + "SOAPCrossHandlerPropSetByHandler3", "SetByHandler3");
        // set various scoped properties and see that they can be referenced by
        // a different handler
        context.put(whichDirection + whichHandlerType
            + "SOAPMessageScopeAppPropSetByHandler3", "SetByHandler3");
        HandlerTracker.reportPut(this, whichDirection + whichHandlerType
            + "SOAPMessageScopeAppPropSetByHandler3", "SetByHandler3");
        context.setScope(
            whichDirection + whichHandlerType
                + "SOAPMessageScopeAppPropSetByHandler3",
            MessageContext.Scope.APPLICATION);
        HandlerTracker.reportSetScope(this,
            whichDirection + whichHandlerType
                + "SOAPMessageScopeAppPropSetByHandler3",
            MessageContext.Scope.APPLICATION.toString());

        context.put(whichDirection + whichHandlerType
            + "SOAPMessageScopeHandlerPropSetByHandler3", "SetByHandler3");
        HandlerTracker.reportPut(this, whichDirection + whichHandlerType
            + "SOAPMessageScopeHandlerPropSetByHandler3", "SetByHandler3");
        context.setScope(
            whichDirection + whichHandlerType
                + "SOAPMessageScopeHandlerPropSetByHandler3",
            MessageContext.Scope.HANDLER);
        HandlerTracker.reportSetScope(this,
            whichDirection + whichHandlerType
                + "SOAPMessageScopeHandlerPropSetByHandler3",
            MessageContext.Scope.HANDLER.toString());

      }
      HandlerTracker.reportComment(this,
          "EndSOAPMessageContextCallbackInvocations");
    }
  }

}
