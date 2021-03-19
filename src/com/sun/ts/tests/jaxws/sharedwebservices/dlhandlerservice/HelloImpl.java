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
 * @(#)HelloImpl.java	1.16 05/09/14
 */

package com.sun.ts.tests.jaxws.sharedwebservices.dlhandlerservice;

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
import java.util.Map.Entry;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import jakarta.xml.ws.handler.MessageContext;
import jakarta.activation.DataHandler;
import java.util.Properties;

// Service Implementation Class - as outlined in JAX-WS Specification

import jakarta.jws.WebService;
import jakarta.annotation.Resource;

@WebService(portName = "HelloPort", targetNamespace = "http://dlhandlerservice.org/wsdl", serviceName = "DLHandlerService", wsdlLocation = "WEB-INF/wsdl/DLHandlerService.wsdl", endpointInterface = "com.sun.ts.tests.jaxws.sharedwebservices.dlhandlerservice.Hello")

public class HelloImpl implements Hello {

  @Resource
  WebServiceContext wscontext;

  public com.sun.ts.tests.jaxws.sharedwebservices.dlhandlerservice.MyResultType doHandlerTest1(
      com.sun.ts.tests.jaxws.sharedwebservices.dlhandlerservice.MyActionType action) {

    Handler_Util.setTraceFlag(action.getHarnesslogtraceflag());

    Handler_Util.initTestUtil("HelloImpl", action.getHarnessloghost(),
        action.getHarnesslogport(), action.getHarnesslogtraceflag());

    TestUtil.logTrace("*** in HelloImpl:doHandlerTest1 ***");
    String theAction = action.getAction();
    TestUtil.logTrace("*** action = " + theAction + " ***");
    String testType = action.getTestType();
    TestUtil.logTrace("*** testType = " + testType + " ***");
    TestUtil.logTrace("*** wscontext = " + wscontext + " ***");

    String errors = "";

    if (theAction.equals("ServerMessageContextTest")) {
      if ((testType.equals("LogicalTest")) || (testType.equals("SOAPTest"))) {
        String contextType = null;
        if (testType.equals("LogicalTest")) {
          contextType = "Logical";
        } else {
          contextType = "SOAP";
        }
        MessageContext mc = null;
        try {
          mc = wscontext.getMessageContext();
          TestUtil.logTrace("MessageContext=" + mc);
          if (mc != null) {
            if (mc.containsKey("INBOUNDServer" + contextType
                + "MessageScopeAppPropSetByHandler3")) {

              TestUtil.logTrace("Found INBOUNDServer" + contextType
                  + "MessageScopeAppPropSetByHandler3");
            } else {
              errors = errors + ", The property INBOUNDServer" + contextType
                  + "MessageScopeAppPropSetByHandler3 was not accessible by the endpoint";
            }
            if (mc.containsKey("INBOUNDServer" + contextType
                + "MessageScopeHandlerPropSetByHandler3")) {
              errors = errors + ", The property INBOUNDServer" + contextType
                  + "MessageScopeHandlerPropSetByHandler3 was accessible by the endpoint";
            } else {
              TestUtil.logTrace("Did not find Server" + contextType
                  + "MessageScopeHandlerPropSetByHandler3");
            }

            if (mc.containsKey("HandlerServerHandlerProp")) {
              TestUtil.logTrace("Found HandlerServerHandlerProp");
              TestUtil.logTrace(
                  "Getting the value of HandlerServerHandlerProp from the MessageContext");
              String tmp = (String) mc.get("HandlerServerHandlerProp");
              TestUtil.logTrace("The value of HandlerServerHandlerProp=" + tmp);
              TestUtil
                  .logTrace("Setting the value of HandlerServerHandlerProp to:"
                      + tmp + "server");
              mc.put("HandlerServerHandlerProp", tmp + "server");
            } else {
              errors = errors
                  + ", The property HandlerServerHandlerProp was accessible by the endpoint";
            }

          } else {
            errors = errors
                + ", The MessageContext did not contain any key-value pairs in the endpoint";
          }

        } catch (Exception e) {
          throw new WebServiceException("Error occurred in endpoint" + e);
        }
      } else {
        errors = errors
            + "The testType did not contain either LogicalTest or SOAPTest, it was:"
            + testType;
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
      TestUtil.logTrace("errors=|" + errors + "|");

      r = new MyResultType();
      r.setAction(action.getAction());
      r.setTestType(action.getTestType());
      r.setErrors(errors);
      r.setHarnessloghost(action.getHarnessloghost());
      r.setHarnesslogport(action.getHarnesslogport());
      r.setHarnesslogtraceflag(action.getHarnesslogtraceflag());
    } catch (Exception e) {
      throw new WebServiceException(e.getMessage(), e);
    }
    return r;
  }

  public MyResultType doHandlerHeaderTest1(MyActionType action,
      Holder<MyHeaderType> myHeader) {

    Handler_Util.setTraceFlag(action.getHarnesslogtraceflag());

    Handler_Util.initTestUtil("HelloImpl", action.getHarnessloghost(),
        action.getHarnesslogport(), action.getHarnesslogtraceflag());

    TestUtil.logTrace("*** in HelloImpl:doHandlerTest1 ***");
    String theAction = action.getAction();
    TestUtil.logTrace("*** action = " + theAction + " ***");
    String header = myHeader.value.getHeader();
    TestUtil.logTrace("*** header = " + header + " ***");

    TestUtil.logTrace("*** wscontext = " + wscontext + " ***");

    String errors = "";

    MyResultType r = null;
    try {
      TestUtil.logTrace("The endpoint is sending back the following data:");
      TestUtil.logTrace("action=" + action.getAction());
      TestUtil.logTrace("getTestType=" + action.getTestType());
      TestUtil.logTrace("harnessloghost=" + action.getHarnessloghost());
      TestUtil.logTrace("harnesslogport=" + action.getHarnesslogport());
      TestUtil
          .logTrace("harnesslogtraceflag=" + action.getHarnesslogtraceflag());
      TestUtil.logTrace("errors=|" + errors + "|");
      r = new MyResultType();
      r.setAction(action.getAction());
      r.setTestType(action.getTestType());
      r.setErrors(errors);
      r.setHarnessloghost(action.getHarnessloghost());
      r.setHarnesslogport(action.getHarnesslogport());
      r.setHarnesslogtraceflag(action.getHarnesslogtraceflag());
    } catch (Exception e) {
      throw new WebServiceException(e.getMessage(), e);
    }
    return r;
  }

  public com.sun.ts.tests.jaxws.sharedwebservices.dlhandlerservice.MyResult2 doHandlerAttachmentTest(
      com.sun.ts.tests.jaxws.sharedwebservices.dlhandlerservice.MyActionType action,
      jakarta.xml.ws.Holder<jakarta.activation.DataHandler> attach1) {

    Handler_Util.setTraceFlag(action.getHarnesslogtraceflag());

    Handler_Util.initTestUtil("HelloImpl", action.getHarnessloghost(),
        action.getHarnesslogport(), action.getHarnesslogtraceflag());

    TestUtil.logTrace("*** in HelloImpl:doHandlerAttachmentTest ***");
    String theAction = action.getAction();
    TestUtil.logTrace("*** action = " + theAction + " ***");
    String testType = action.getTestType();
    TestUtil.logTrace("*** testType = " + testType + " ***");
    TestUtil.logTrace("*** wscontext = " + wscontext + " ***");

    String errors = "";
    Vector<String> results = new Vector<String>();

    if (theAction.equals("ContextPropertiesTest")) {
      if ((testType.equals("LogicalTest")) || (testType.equals("SOAPTest"))) {
        MessageContext mc = null;
        try {
          mc = wscontext.getMessageContext();
          TestUtil.logTrace("MessageContext=" + mc);
          if (mc != null) {
            Map<String, DataHandler> m1 = (Map<String, DataHandler>) mc
                .get(MessageContext.INBOUND_MESSAGE_ATTACHMENTS);
            StringBuffer sb = new StringBuffer();
            int cnt = 0;
            try {
              if (m1 != null) {
                if (m1.size() > 0) {
                  Set<Map.Entry<String, DataHandler>> m1Set = m1.entrySet();
                  Iterator<Map.Entry<String, DataHandler>> iterator = m1Set
                      .iterator();
                  while (iterator.hasNext()) {
                    Map.Entry<String, DataHandler> elem = iterator.next();
                    String key = elem.getKey();
                    DataHandler value = elem.getValue();
                    TestUtil.logTrace(
                        "request attachments key[" + cnt + "]=" + key);
                    sb.append("key[" + cnt + "]=" + key + "_" + value + "|");
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
            results.add("Endpoint:MessageContext.INBOUND_MESSAGE_ATTACHMENTS="
                + sb.toString());

            String ss = (String) mc.get(MessageContext.HTTP_REQUEST_METHOD);
            if (ss != null) {
              results.add("Endpoint:MessageContext.HTTP_REQUEST_METHOD=" + ss);
            } else {
              results.add("Endpoint:MessageContext.HTTP_REQUEST_METHOD=null");
            }
            Integer ii = (Integer) mc.get(MessageContext.HTTP_RESPONSE_CODE);
            if (ii != null) {
              results.add("Endpoint:MessageContext.HTTP_RESPONSE_CODE="
                  + ii.toString());
            } else {
              results.add("Endpoint:MessageContext.HTTP_RESPONSE_CODE=null");
            }

            Map<String, List<String>> m2 = (Map<String, List<String>>) mc
                .get(MessageContext.HTTP_REQUEST_HEADERS);
            sb = new StringBuffer();
            cnt = 0;
            try {
              if (m2 != null) {
                if (m2.size() > 0) {
                  Set<Map.Entry<String, List<String>>> m2Set = m2.entrySet();
                  Iterator<Map.Entry<String, List<String>>> iterator = m2Set
                      .iterator();
                  while (iterator.hasNext()) {
                    Map.Entry<String, List<String>> elem = iterator.next();
                    String key = elem.getKey();
                    TestUtil
                        .logTrace("request headers key[" + cnt + "]=" + key);
                    List<String> values = elem.getValue();
                    Iterator<String> iterator2 = values.iterator();
                    TestUtil.logTrace("request headers value[" + cnt + "]=");
                    sb.append("value[" + cnt + "]=");
                    while (iterator2.hasNext()) {
                      String value = iterator2.next();
                      TestUtil.logTrace(value);
                      sb.append("_" + value);
                    }
                    sb.append("|");
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
            results.add("Endpoint:MessageContext.HTTP_REQUEST_HEADERS="
                + sb.toString());

            m2 = (Map<String, List<String>>) mc
                .get(MessageContext.HTTP_RESPONSE_HEADERS);
            sb = new StringBuffer();
            cnt = 0;
            try {
              if (m2 != null) {
                if (m2.size() > 0) {
                  Set<Map.Entry<String, List<String>>> m2Set = m2.entrySet();
                  Iterator<Map.Entry<String, List<String>>> iterator = m2Set
                      .iterator();
                  while (iterator.hasNext()) {
                    Map.Entry<String, List<String>> elem = iterator.next();
                    String key = elem.getKey();
                    TestUtil
                        .logTrace("response headers key[" + cnt + "]=" + key);
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
            results.add("Endpoint:MessageContext.HTTP_RESPONSE_HEADERS="
                + sb.toString());

            Object oo = mc.get(MessageContext.SERVLET_REQUEST);
            if (oo != null) {
              results.add(
                  "Endpoint:MessageContext.SERVLET_REQUEST=" + oo.toString());
            } else {
              results.add("Endpoint:MessageContext.SERVLET_REQUEST=null");
            }
            oo = mc.get(MessageContext.SERVLET_RESPONSE);
            if (oo != null) {
              results.add(
                  "Endpoint:MessageContext.SERVLET_RESPONSE=" + oo.toString());
            } else {
              results.add("Endpoint:MessageContext.SERVLET_RESPONSE=null");
            }
            oo = mc.get(MessageContext.SERVLET_CONTEXT);
            if (oo != null) {
              results.add(
                  "Endpoint:MessageContext.SERVLET_CONTEXT=" + oo.toString());
            } else {
              results.add("Endpoint:MessageContext.SERVLET_CONTEXT=null");
            }

          } else {
            errors = errors
                + ", The MessageContext did not contain any key-value pairs in the endpoint";
          }

        } catch (Exception e) {
          throw new WebServiceException("Error occurred in endpoint" + e);
        }
      } else {
        errors = errors
            + "The testType did not contain either LogicalTest or SOAPTest, it was:"
            + testType;
      }
    }

    MyResult2 r = null;
    try {
      TestUtil.logTrace("The endpoint is sending back the following data:");
      TestUtil.logTrace("action=" + action.getAction());
      TestUtil.logTrace("getTestType=" + action.getTestType());
      TestUtil.logTrace("harnessloghost=" + action.getHarnessloghost());
      TestUtil.logTrace("harnesslogport=" + action.getHarnesslogport());
      TestUtil
          .logTrace("harnesslogtraceflag=" + action.getHarnesslogtraceflag());
      TestUtil.logTrace("attach1=" + attach1.value);
      TestUtil.logTrace("errors=|" + errors + "|");
      r = new MyResult2();
      r.setAction(action.getAction());
      r.setTestType(action.getTestType());
      r.setErrors(errors);
      r.setHarnessloghost(action.getHarnessloghost());
      r.setHarnesslogport(action.getHarnesslogport());
      r.setHarnesslogtraceflag(action.getHarnesslogtraceflag());

      for (int i = 0; i < results.size(); i++) {
        r.getResult().add(results.get(i));
        TestUtil.logTrace("results[" + i + "]=" + results.get(i));
      }

    } catch (Exception e) {
      throw new WebServiceException(e.getMessage(), e);
    }
    return r;
  }
}
