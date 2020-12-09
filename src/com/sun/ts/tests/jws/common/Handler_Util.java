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
 * @(#)Handler_Util.java	1.6 05/09/28
 */

package com.sun.ts.tests.jws.common;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.xml.transform.Source;

import com.sun.ts.lib.util.TestUtil;

import jakarta.xml.soap.SOAPMessage;
import jakarta.xml.ws.Binding;
import jakarta.xml.ws.LogicalMessage;
import jakarta.xml.ws.handler.Handler;
import jakarta.xml.ws.handler.LogicalMessageContext;
import jakarta.xml.ws.handler.MessageContext;
import jakarta.xml.ws.handler.soap.SOAPMessageContext;

public final class Handler_Util {

  public static String getDirection(MessageContext context) {
    boolean result = false;
    if (context instanceof LogicalMessageContext) {
      TestUtil.logTrace("MESSAGE_OUTBOUND_PROPERTY="
          + context.get(LogicalMessageContext.MESSAGE_OUTBOUND_PROPERTY));
      result = ((Boolean) context
          .get(LogicalMessageContext.MESSAGE_OUTBOUND_PROPERTY)).booleanValue();
    } else {
      TestUtil.logTrace("MESSAGE_OUTBOUND_PROPERTY="
          + context.get(SOAPMessageContext.MESSAGE_OUTBOUND_PROPERTY));
      result = ((Boolean) context
          .get(SOAPMessageContext.MESSAGE_OUTBOUND_PROPERTY)).booleanValue();
    }

    TestUtil.logTrace("MESSAGE_OUTBOUND_PROPERTY=" + result);
    String direction = null;
    if (result) {
      direction = Constants.OUTBOUND;
      TestUtil.logTrace("direction = (outbound) - " + direction);
    } else {
      direction = Constants.INBOUND;
      TestUtil.logTrace("direction = (inbound) - " + direction);
    }
    return direction;
  }

  public static boolean checkForMsg(MessageContext context, String whichMsg) {
    boolean foundIt = false;
    TestUtil.logTrace("Checking if this is a '" + whichMsg + "' message");
    if (context instanceof LogicalMessageContext) {
      LogicalMessage lm = ((LogicalMessageContext) context).getMessage();
      if (lm != null) {
        Source source = lm.getPayload();
        if (source != null) {
          try {
            String msg = JWS_Util.getSourceAsString(source);
            TestUtil.logTrace("msg=" + msg);
            TestUtil.logTrace("whichMsg=" + whichMsg);

            if (msg.indexOf(whichMsg) > -1) {
              foundIt = true;
            }
          } catch (Exception e) {
            e.printStackTrace();
          }
        } else {
          TestUtil.logTrace(
              "LogicalHandlerBase(checkForMsg): No message payload was present");
        }
      } else {
        TestUtil.logTrace(
            "LogicalHandlerBase(checkForMsg): No message was present");
      }
    } else {
      SOAPMessage soapmsg = ((SOAPMessageContext) context).getMessage();
      String msg = JWS_Util.getMsgAsString(soapmsg);
      TestUtil.logTrace("msg=" + msg);
      TestUtil.logTrace("which msg=" + whichMsg);
      if (msg != null) {
        if (msg.indexOf(whichMsg) > -1) {
          foundIt = true;
        }
      }
    }
    TestUtil.logTrace("foundIt=" + foundIt);
    return foundIt;
  }

  public static void initTestUtil(Handler handler, String host, String port,
      String flag) {
    System.out.println("initTestUtil:handler=" + handler);
    System.out.println("initTestUtil:host=" + host);
    System.out.println("initTestUtil:port=" + port);
    System.out.println("initTestUtil:flag=" + flag);
    initTestUtil(host, port, flag);
  }

  public static void initTestUtil(String impl, String host, String port,
      String flag) {
    System.out.println("initTestUtil:impl=" + impl);
    System.out.println("initTestUtil:host=" + host);
    System.out.println("initTestUtil:port=" + port);
    System.out.println("initTestUtil:flag=" + flag);
    initTestUtil(host, port, flag);
  }

  public static void initTestUtil(String host, String port, String flag) {
    Properties p = new Properties();
    p.setProperty("harness.host", host);
    p.setProperty("harness.log.port", port);
    p.setProperty("harness.log.traceflag", flag);
    try {
      TestUtil.init(p);
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  public static String getValueFromMsg(MessageContext context, String name,
      String whichHandlerType) {
    String value = "";
    TestUtil
        .logTrace("Getting the value for item '" + name + "' from the message");
    if (context instanceof LogicalMessageContext) {
      LogicalMessage lm = ((LogicalMessageContext) context).getMessage();
      if (lm != null) {
        Source source = lm.getPayload();
        if (source != null) {
          try {
            String msg = JWS_Util.getSourceAsString(source);
            TestUtil.logTrace("msg=" + msg);
            TestUtil.logTrace("name=" + name);

            if (msg.indexOf(name) > -1) {
              int startValueIndex = msg.indexOf(name) + name.length() + 1;
              int endValueIndex = msg.indexOf(name, startValueIndex) - 2;
              value = msg.substring(startValueIndex, endValueIndex);
            }
          } catch (Exception e) {
            e.printStackTrace();
          }
        } else {
          TestUtil.logTrace(
              "LogicalHandlerBase(checkForMsg): No message payload was present");
        }
      } else {
        TestUtil.logTrace(
            "LogicalHandlerBase(checkForMsg): No message was present");
      }
    } else {
      SOAPMessage soapmsg = ((SOAPMessageContext) context).getMessage();
      String msg = JWS_Util.getMsgAsString(soapmsg);
      TestUtil.logTrace("msg=" + msg);
      TestUtil.logTrace("name=" + name);
      if (msg != null) {
        if (msg.indexOf(name) > -1) {
          int startValueIndex = msg.indexOf(name) + name.length() + 1;
          int endValueIndex = msg.indexOf(name, startValueIndex) - 2;
          value = msg.substring(startValueIndex, endValueIndex);
        }
      }
    }
    return value;
  }

  public static List<Handler> removeHandler(List<Handler> l, Class c) {
    TestUtil.logTrace("removing client-side handler:" + c);
    List<Handler> lh = new ArrayList<Handler>();
    Iterator i = l.iterator();
    while (i.hasNext()) {
      Object o = i.next();
      if (!c.isInstance(o)) {
        lh.add(l.get(l.indexOf(o)));
      }
    }
    return lh;
  }

  public static void clearHandlers(List<Binding> l) {
    Iterator i = l.iterator();
    while (i.hasNext()) {
      Binding b = (Binding) i.next();
      clearHandlers(b);
    }
  }

  public static void clearHandlers(Binding b) {
    TestUtil.logMsg("Clearing client-side handlers on binding:" + b);
    b.setHandlerChain(new ArrayList<Handler>());
    List<Handler> handlerList = b.getHandlerChain();
    TestUtil.logTrace("HandlerChain=" + handlerList);
    TestUtil.logTrace("HandlerChain size = " + handlerList.size());
  }

  public static boolean VerifyCloseCallBacks(String who, List<String> calls,
      String contextType, boolean expection, String direction) {
    boolean pass = true;

    if (calls == null) {
      TestUtil.logErr("Callback string is null (unexpected)");
      return false;
    }

    if (expection) {
      TestUtil.logMsg("Testing that close method of the handers was called");
      if (who.equals("Client")) {
        if (direction.equals(Constants.INBOUND)) {
          if (calls.indexOf(who + contextType + "Handler6.close()") == -1) {
            TestUtil
                .logErr(who + contextType + "Handler6.close() was not called");
            pass = false;
          }
        } else {
          if (calls.indexOf(who + contextType + "Handler6.close()") > -1) {
            TestUtil.logErr(who + contextType + "Handler6.close() was called");
            pass = false;
          }
        }
      } else {
        if (calls.indexOf(who + contextType + "Handler6.close()") == -1) {
          TestUtil
              .logErr(who + contextType + "Handler6.close() was not called");
          pass = false;
        }
      }
      if (calls.indexOf(who + contextType + "Handler4.close()") == -1) {
        TestUtil.logErr(who + contextType + "Handler4.close() was not called");
        pass = false;
      }
      if (who.equals("Client")) {
        if (calls.indexOf(who + contextType + "Handler5.close()") == -1) {
          TestUtil
              .logErr(who + contextType + "Handler5.close() was not called");
          pass = false;
        }
      } else {
        if (direction.equals(Constants.INBOUND)) {
          if (calls.indexOf(who + contextType + "Handler5.close()") > -1) {
            TestUtil.logErr(who + contextType + "Handler5.close() was called");
            pass = false;
          }
        } else {
          if (calls.indexOf(who + contextType + "Handler5.close()") == -1) {
            TestUtil
                .logErr(who + contextType + "Handler5.close() was not called");
            pass = false;
          }
        }
      }
      TestUtil.logMsg("Testing the order in which close() was called");
      int close6 = calls.indexOf(who + contextType + "Handler6.close()");
      int close4 = calls.indexOf(who + contextType + "Handler4.close()");
      int close5 = calls.indexOf(who + contextType + "Handler5.close()");
      TestUtil.logTrace("close6=" + close6);
      TestUtil.logTrace("close4=" + close4);
      TestUtil.logTrace("close5=" + close5);
      if (close6 > -1) {
        if (close4 > -1) {
          if (close6 > close4) {
            TestUtil.logErr("The close method for handler " + who + contextType
                + "Handler4 was not called");
            TestUtil.logErr("after the close method for handler " + who
                + contextType + "Handler6");
            pass = false;
          }
          if (close5 > -1) {
            if (close4 > close5) {
              TestUtil.logErr("The close method for handler " + who
                  + contextType + "Handler5 was not called");
              TestUtil.logErr("after the close method for handler " + who
                  + contextType + "Handler4");
              pass = false;
            }
          }
        }
        if (close5 > -1) {
          if (close6 > close5) {
            TestUtil.logErr("The close method for handler " + who + contextType
                + "Handler5 was not called");
            TestUtil.logErr("after the close method for handler " + who
                + contextType + "Handler6");
            pass = false;
          }
        }
      } else if (close4 > -1) {
        if (close5 > -1) {
          if (close4 > close5) {
            TestUtil.logErr("The close method for handler " + who + contextType
                + "Handler5 was not called");
            TestUtil.logErr("after the close method for handler " + who
                + contextType + "Handler4");
            pass = false;
          }
        }
      } else {
        TestUtil.logErr("The close method for handler " + who + contextType
            + "Handler4 was not found");
        pass = false;

      }
    } else {
      TestUtil.logMsg(
          "Testing that close() was called and the order in which it was called");
      int close6 = calls.indexOf(who + contextType + "Handler6.close()");
      int close3 = calls.indexOf(who + contextType + "Handler3.close()");
      int close2 = calls.indexOf(who + contextType + "Handler2.close()");
      int close1 = calls.indexOf(who + contextType + "Handler1.close()");
      int close5 = calls.indexOf(who + contextType + "Handler5.close()");
      TestUtil.logTrace("close6=" + close6);
      TestUtil.logTrace("close3=" + close3);
      TestUtil.logTrace("close2=" + close2);
      TestUtil.logTrace("close1=" + close1);
      TestUtil.logTrace("close5=" + close5);

      if (close6 == -1) {
        TestUtil.logErr(who + contextType + "Handler6.close() was not called");
        pass = false;
      }
      if (close3 == -1) {
        TestUtil.logErr(who + contextType + "Handler3.close() was not called");
        pass = false;
      }
      if (close6 > close3) {
        TestUtil.logErr("The close method for handler " + who + contextType
            + "Handler3 was not called");
        TestUtil.logErr("after the close method for handler " + who
            + contextType + "Handler6");
        pass = false;
      }
      if (close2 == -1) {
        TestUtil.logErr(who + contextType + "Handler2.close() was not called");
        pass = false;
      }
      if (close3 > close2) {
        TestUtil.logErr("The close method for handler " + who + contextType
            + "Handler2 was not called");
        TestUtil.logErr("after the close method for handler " + who
            + contextType + "Handler3");
        pass = false;
      }
      if (close1 == -1) {
        TestUtil.logErr(who + contextType + "Handler1.close() was not called");
        pass = false;
      }
      if (close5 == -1) {
        TestUtil.logErr(who + contextType + "Handler5.close() was not called");
        pass = false;
      }
      if (close1 > close5) {
        TestUtil.logErr("The close method for handler " + who + contextType
            + "Handler5 was not called");
        TestUtil.logErr("after the close method for handler " + who
            + contextType + "Handler1");
        pass = false;
      }
    }
    return pass;
  }

  public static boolean VerifyHandlerExceptionCallBacks(String who,
      String contextType, boolean remoteException, String direction,
      List<String> calls) {
    boolean pass = true;

    if (calls == null) {
      TestUtil.logErr("Callback string is null (unexpected)");
      return false;
    }
    TestUtil.logTrace("who=" + who);
    TestUtil.logTrace("contextType=" + contextType);
    TestUtil.logTrace("remoteException=" + remoteException);
    TestUtil.logTrace("direction=" + direction);
    TestUtil.logTrace("\n");

    TestUtil.logTrace("The complete list of callbacks are:");
    JWS_Util.dumpList(calls);

    if (who.equals("Client")) {
      if (direction.equals(Constants.OUTBOUND)) {
        if (calls.indexOf(who + contextType
            + "Handler5.handleMessage().doOutbound()") == -1) {
          TestUtil.logErr(who + contextType
              + "Handler5.handleMessage().doOutbound() was not called");
          pass = false;
        }
        if (calls.indexOf(who + contextType
            + "Handler4.handleMessage().doOutbound()") == -1) {
          TestUtil.logErr(who + contextType
              + "Handler4.handleMessage().doOutbound() was not called");
          pass = false;
        }
      } else {
        if (calls.indexOf(who + contextType
            + "Handler5.handleMessage().doOutbound()") == -1) {
          TestUtil.logErr(who + contextType
              + "Handler5.handleMessage().doOutbound() was not called");
          pass = false;
        }
        if (calls.indexOf(who + contextType
            + "Handler4.handleMessage().doOutbound()") == -1) {
          TestUtil.logErr(who + contextType
              + "Handler4.handleMessage().doOutbound() was not called");
          pass = false;
        }
        if (calls.indexOf(who + contextType
            + "Handler6.handleMessage().doOutbound()") == -1) {
          TestUtil.logErr(who + contextType
              + "Handler6.handleMessage().doOutbound() was not called");
          pass = false;
        }
        if (calls.indexOf(
            who + contextType + "Handler6.handleMessage().doInbound()") == -1) {
          TestUtil.logErr(who + contextType
              + "Handler6.handleMessage().doInbound() was not called");
          pass = false;
        }
        if (calls.indexOf(
            who + contextType + "Handler4.handleMessage().doInbound()") == -1) {
          TestUtil.logErr(who + contextType
              + "Handler4.handleMessage().doInbound() was not called");
          pass = false;
        }
      }
    } else {
      // server
      if (remoteException) {
        if (calls.indexOf(
            who + contextType + "Handler6.handleMessage().doInbound()") == -1) {
          TestUtil.logErr(who + contextType
              + "Handler6.handleMessage().doInbound() was not called");
          pass = false;
        }
        if (calls.indexOf(
            who + contextType + "Handler4.handleMessage().doInbound()") == -1) {
          TestUtil.logErr(who + contextType
              + "Handler4.handleMessage().doInbound() was not called");
          pass = false;
        }
        if (calls.indexOf(
            who + contextType + "Handler5.handleMessage().doInbound()") == -1) {
          TestUtil.logErr(who + contextType
              + "Handler5.handleMessage().doInbound() was not called");
          pass = false;
        }
        if (calls.indexOf(who + contextType + "Handler6.handleFault()") == -1) {
          TestUtil.logErr(
              who + contextType + "Handler6.handleFault() was not called");
          pass = false;
        }
        if (calls.indexOf(who + contextType + "Handler4.handleFault()") == -1) {
          TestUtil.logErr(
              who + contextType + "Handler4.handleFault() was not called");
          pass = false;
        }
        if (calls.indexOf(who + contextType + "Handler5.handleFault()") == -1) {
          TestUtil.logErr(
              who + contextType + "Handler5.handleFault() was not called");
          pass = false;
        }
        if (calls.indexOf(
            who + contextType + "Handler5.handleMessage().doOutbound()") > -1) {
          TestUtil.logErr(who + contextType
              + "Handler5.handleMessage().doOutbound() was called");
          pass = false;
        }
        if (calls.indexOf(
            who + contextType + "Handler4.handleMessage().doOutbound()") > -1) {
          TestUtil.logErr(who + contextType
              + "Handler4.handleMessage().doOutbound() was called");
          pass = false;
        }
        if (calls.indexOf(
            who + contextType + "Handler6.handleMessage().doOutbound()") > -1) {
          TestUtil.logErr(who + contextType
              + "Handler6.handleMessage().doOutbound() was called");
          pass = false;
        }
      } else {
        if (direction.equals(Constants.OUTBOUND)) {
          if (calls.indexOf(who + contextType
              + "Handler6.handleMessage().doInbound()") == -1) {
            TestUtil.logErr(who + contextType
                + "Handler6.handleMessage().doInbound() was not called");
            pass = false;
          }
          if (calls.indexOf(who + contextType
              + "Handler4.handleMessage().doInbound()") == -1) {
            TestUtil.logErr(who + contextType
                + "Handler4.handleMessage().doInbound() was not called");
            pass = false;
          }
          if (calls.indexOf(who + contextType
              + "Handler5.handleMessage().doInbound()") == -1) {
            TestUtil.logErr(who + contextType
                + "Handler5.handleMessage().doInbound() was not called");
            pass = false;
          }
          if (calls.indexOf(who + contextType
              + "Handler5.handleMessage().doOutbound()") == -1) {
            TestUtil.logErr(who + contextType
                + "Handler5.handleMessage().doOutbound() was not called");
            pass = false;
          }
          if (calls.indexOf(who + contextType
              + "Handler4.handleMessage().doOutbound()") == -1) {
            TestUtil.logErr(who + contextType
                + "Handler4.handleMessage().doOutbound() was not called");
            pass = false;
          }
          if (calls.indexOf(who + contextType
              + "Handler6.handleMessage().doOutbound()") > -1) {
            TestUtil.logErr(who + contextType
                + "Handler6.handleMessage().doOutbound() was called");
            pass = false;
          }
        } else {
          if (calls.indexOf(who + contextType
              + "Handler6.handleMessage().doInbound()") == -1) {
            TestUtil.logErr(who + contextType
                + "Handler6.handleMessage().doInbound() was not called");
            pass = false;
          }
          if (calls.indexOf(who + contextType
              + "Handler4.handleMessage().doInbound()") == -1) {
            TestUtil.logErr(who + contextType
                + "Handler4.handleMessage().doInbound() was not called");
            pass = false;
          }
          if (calls.indexOf(who + contextType
              + "Handler5.handleMessage().doInbound()") > -1) {
            TestUtil.logErr(who + contextType
                + "Handler5.handleMessage().doInbound() was called");
            pass = false;
          }
        }
      }
    }
    if (!VerifyCloseCallBacks(who, calls, contextType, true, direction)) {
      pass = false;
    }

    return pass;
  }

  public static boolean VerifyHandlerCallBacks(String who, String contextType,
      List<String> calls) {
    boolean pass = true;
    String packageName = null;

    if (calls == null) {
      TestUtil.logErr("Callback string is null (unexpected)");
      return false;
    }
    TestUtil.logTrace("who=" + who);
    TestUtil.logTrace("\n");

    TestUtil.logTrace("The complete list of callbacks are:");
    JWS_Util.dumpList(calls);
    if (who.equals("Client")) {
      TestUtil.logMsg("Testing handlers went through init phase");
      if (calls.indexOf(who + contextType + "Handler5.init()") == -1) {
        TestUtil.logErr(who + contextType + "Handler5.init() was not called");
        pass = false;
      }
      if (calls.indexOf(who + contextType + "Handler1.init()") == -1) {
        TestUtil.logErr(who + contextType + "Handler1.init() was not called");
        pass = false;
      }
      if (calls.indexOf(who + contextType + "Handler2.init()") > -1) {
        TestUtil.logErr(who + contextType + "Handler2.init() was called");
        pass = false;
      }
      if (calls.indexOf(who + contextType + "Handler3.init()") > -1) {
        TestUtil.logErr(who + contextType + "Handler3.init() was called");
        pass = false;
      }
      if (calls.indexOf(who + contextType + "Handler6.init()") > -1) {
        TestUtil.logErr(who + contextType + "Handler6.init() was called");
        pass = false;
      }

    }
    TestUtil.logMsg("Testing the order of the outbound phase");

    int outhandler4 = calls
        .indexOf(who + contextType + "Handler4.handleMessage().doInbound()");
    if (outhandler4 > -1) {
      TestUtil.logErr(who + contextType
          + "Handler4.handleMessage().doOutbound() was called and should not have");
      pass = false;
    }
    int outhandler5 = calls
        .indexOf(who + contextType + "Handler5.handleMessage().doOutbound()");
    if (outhandler5 == -1) {
      TestUtil.logErr(who + contextType
          + "Handler5.handleMessage().doOutbound() was not called");
      pass = false;
    }
    int outhandler1 = calls
        .indexOf(who + contextType + "Handler1.handleMessage().doOutbound()");
    if (outhandler1 == -1) {
      TestUtil.logErr(who + contextType
          + "Handler1.handleMessage().doOutbound() was not called");
      pass = false;
    }
    int outhandler2 = calls
        .indexOf(who + contextType + "Handler2.handleMessage().doOutbound()");
    if (outhandler2 == -1) {
      TestUtil.logErr(who + contextType
          + "Handler2.handleMessage().doOutbound() was not called");
      pass = false;
    }
    int outhandler3 = calls
        .indexOf(who + contextType + "Handler3.handleMessage().doOutbound()");
    if (outhandler3 == -1) {
      TestUtil.logErr(who + contextType
          + "Handler3.handleMessage().doOutbound() was not called");
      pass = false;
    }
    int outhandler6 = calls
        .indexOf(who + contextType + "Handler6.handleMessage().doOutbound()");
    if (outhandler6 == -1) {
      TestUtil.logErr(who + contextType
          + "Handler6.handleMessage().doOutbound() was not called");
      pass = false;
    }
    if (outhandler5 > outhandler1) {
      TestUtil.logErr(who + contextType
          + "Handler1.handleMessage().doOutbound() was not called after " + who
          + contextType + "Handler5.handleMessage().doOutbound()");
      pass = false;
    }
    if (outhandler1 > outhandler2) {
      TestUtil.logErr(who + contextType
          + "Handler2.handleMessage().doOutbound() was not called after " + who
          + contextType + "Handler1.handleMessage().doOutbound()");
      pass = false;
    }
    if (outhandler2 > outhandler3) {
      TestUtil.logErr(who + contextType
          + "Handler3.handleMessage().doOutbound() was not called after " + who
          + contextType + "Handler2.handleMessage().doOutbound()");
      pass = false;
    }
    if (outhandler3 > outhandler6) {
      TestUtil.logErr(who + contextType
          + "Handler6.handleMessage().doOutbound() was not called after " + who
          + contextType + "Handler3.handleMessage().doOutbound()");
      pass = false;
    }

    TestUtil.logMsg("Testing the order of the inbound phase");

    int inhandler4 = calls
        .indexOf(who + contextType + "Handler4.handleMessage().doInbound()");
    if (inhandler4 > -1) {
      TestUtil.logErr(who + contextType
          + "Handler4.handleMessage().doInbound() was called and should not have");
      pass = false;
    }
    int inhandler6 = calls
        .indexOf(who + contextType + "Handler6.handleMessage().doInbound()");
    if (inhandler6 == -1) {
      TestUtil.logErr(who + contextType
          + "Handler6.handleMessage().doInbound() was not called");
      pass = false;
    }
    int inhandler3 = calls
        .indexOf(who + contextType + "Handler3.handleMessage().doInbound()");
    if (inhandler3 == -1) {
      TestUtil.logErr(who + contextType
          + "Handler3.handleMessage().doInbound() was not called");
      pass = false;
    }
    int inhandler2 = calls
        .indexOf(who + contextType + "Handler2.handleMessage().doInbound()");
    if (inhandler2 == -1) {
      TestUtil.logErr(who + contextType
          + "Handler2.handleMessage().doInbound() was not called");
      pass = false;
    }
    int inhandler1 = calls
        .indexOf(who + contextType + "Handler1.handleMessage().doInbound()");
    if (inhandler1 == -1) {
      TestUtil.logErr(who + contextType
          + "Handler1.handleMessage().doInbound() was not called");
      pass = false;
    }
    int inhandler5 = calls
        .indexOf(who + contextType + "Handler5.handleMessage().doInbound()");
    if (inhandler5 == -1) {
      TestUtil.logErr(who + contextType
          + "Handler5.handleMessage().doInbound() was not called");
      pass = false;
    }
    if (inhandler6 > inhandler3) {
      TestUtil.logErr(who + contextType
          + "Handler3.handleMessage().doInbound() was not called after " + who
          + contextType + "Handler6.handleMessage().doInbound()");
      pass = false;
    }
    if (inhandler3 > inhandler2) {
      TestUtil.logErr(who + contextType
          + "Handler2.handleMessage().doInbound() was not called after " + who
          + contextType + "Handler3.handleMessage().doInbound()");
      pass = false;
    }
    if (inhandler2 > inhandler1) {
      TestUtil.logErr(who + contextType
          + "Handler1.handleMessage().doInbound() was not called after " + who
          + contextType + "Handler2.handleMessage().doInbound()");
      pass = false;
    }
    if (inhandler1 > inhandler5) {
      TestUtil.logErr(who + contextType
          + "Handler5.handleMessage().doInbound() was not called after " + who
          + contextType + "Handler1.handleMessage().doInbound()");
      pass = false;
    }

    TestUtil
        .logMsg("Testing the order of the inbound verses outbound messages");
    if (who.equals("Client")) {
      if (outhandler6 > inhandler6) {
        TestUtil.logErr(who + contextType
            + "Handler6.handleMessage().doInbound() was not called after " + who
            + contextType + "Handler6.handleMessage().doOutbound()");
        pass = false;
      }
    } else {
      if (outhandler6 < inhandler6) {
        TestUtil.logErr(who + contextType
            + "Handler6.handleMessage().doOutbound() was not called after "
            + who + contextType + "Handler6.handleMessage().doInbound()");
        pass = false;
      }
    }
    TestUtil.logMsg("Testing the close callbacks");
    if (!VerifyCloseCallBacks(who, calls, contextType, false, "")) {
      pass = false;
    }
    return pass;
  }

  public static boolean VerifyMessageContextCallBacks(String who,
      String contextType, List<String> calls) {
    boolean pass = true;

    if (calls == null) {
      TestUtil.logErr("Callback string is null (unexpected)");
      return false;
    }
    JWS_Util.dumpList(calls);

    if (calls.indexOf(who + contextType + "Handler1.MessageContext.put(OUTBOUND"
        + who + contextType
        + "CrossHandlerPropSetByHandler1,SetByHandler1)") == -1) {
      TestUtil.logErr(who + contextType + "Handler1.MessageContext.put(OUTBOUND"
          + who + contextType
          + "CrossHandlerPropSetByHandler1,SetByHandler1) was not found");
      pass = false;
    }
    if (calls.indexOf(who + contextType + "Handler1.MessageContext.put(OUTBOUND"
        + who + contextType
        + "MessageScopeAppPropSetByHandler1,SetByHandler1)") == -1) {
      TestUtil.logErr(who + contextType + "Handler1.MessageContext.put(OUTBOUND"
          + who + contextType
          + "MessageScopeAppPropSetByHandler1,SetByHandler1) was not found");
      pass = false;
    }
    if (calls.indexOf(
        who + contextType + "Handler1.MessageContext.setPropertyScope(OUTBOUND"
            + who + contextType
            + "MessageScopeAppPropSetByHandler1,APPLICATION)") == -1) {
      TestUtil.logErr(who + contextType
          + "Handler1.MessageContext.setPropertyScope(OUTBOUND" + who
          + contextType
          + "MessageScopeAppPropSetByHandler1,APPLICATION) was not found");
      pass = false;
    }
    if (calls.indexOf(who + contextType + "Handler1.MessageContext.put(OUTBOUND"
        + who + contextType
        + "MessageScopeHandlerPropSetByHandler1,SetByHandler1)") == -1) {
      TestUtil.logErr(who + contextType + "Handler1.MessageContext.put(OUTBOUND"
          + who + contextType
          + "MessageScopeAppPropSetByHandler1,SetByHandler1) was not found");
      pass = false;
    }
    if (calls.indexOf(
        who + contextType + "Handler1.MessageContext.setPropertyScope(OUTBOUND"
            + who + contextType
            + "MessageScopeHandlerPropSetByHandler1,HANDLER)") == -1) {
      TestUtil.logErr(who + contextType
          + "Handler1.MessageContext.setPropertyScope(OUTBOUND" + who
          + contextType
          + "MessageScopeHandlerPropSetByHandler1,HANDLER) was not found");
      pass = false;
    }
    if (who.equals("Client")) {
      if (calls
          .indexOf(who + contextType + "Handler2.MessageContext.getProperty("
              + who + "To" + who + "Prop)=client") == -1) {
        TestUtil
            .logErr(who + contextType + "Handler2.MessageContext.getProperty("
                + who + "To" + who + "Prop)=client was not found");
        pass = false;
      }
      if (calls.indexOf(who + contextType + "Handler2.MessageContext.put(" + who
          + "To" + who + "Prop,clientOUTBOUNDClient" + contextType
          + "Handler2)") == -1) {
        TestUtil.logErr(who + contextType + "Handler2.MessageContext.put(" + who
            + "To" + who + "Prop,clientOUTBOUNDClient" + contextType
            + "Handler2) was not found");
        pass = false;
      }
      if (calls.indexOf(
          who + contextType + "Handler2.MessageContext.setPropertyScope(" + who
              + "To" + who + "Prop,APPLICATION)") == -1) {
        TestUtil.logErr(
            who + contextType + "Handler2.MessageContext.setPropertyScope("
                + who + "To" + who + "Prop,APPLICATION) was not found");
        pass = false;
      }
    } else {
      if (calls.indexOf(who + contextType
          + "Handler2.MessageContext.put(Handler" + who
          + "HandlerProp,INBOUNDServer" + contextType + "Handler2)") == -1) {
        TestUtil.logErr(who + contextType + "Handler2.MessageContext.put("
            + "Handler" + who + "HandlerProp,INBOUNDServer" + contextType
            + "Handler2) was not found");
        pass = false;
      }
      if (calls.indexOf(
          who + contextType + "Handler2.MessageContext.setPropertyScope(Handler"
              + who + "HandlerProp,APPLICATION)") == -1) {
        TestUtil.logErr(
            who + contextType + "Handler2.MessageContext.setPropertyScope("
                + "Handler" + who + "HandlerProp,APPLICATION) was not found");
        pass = false;
      }
    }

    if (calls.indexOf(who + contextType
        + "Handler3.MessageContext.getProperty(OUTBOUND" + who + contextType
        + "CrossHandlerPropSetByHandler1)=SetByHandler1") == -1) {
      TestUtil.logErr(who + contextType
          + "Handler3.MessageContext.getProperty(OUTBOUND" + who + contextType
          + "CrossHandlerPropSetByHandler1)=SetByHandler1 was not found");
      pass = false;
    }
    if (calls.indexOf(who + contextType
        + "Handler3.MessageContext.getProperty(OUTBOUND" + who + contextType
        + "MessageScopeAppPropSetByHandler1)=SetByHandler1") == -1) {
      TestUtil.logErr(who + contextType
          + "Handler3.MessageContext.getProperty(OUTBOUND" + who + contextType
          + "MessageScopeAppPropSetByHandler1)=SetByHandler1 was not found");
      pass = false;
    }
    if (calls.indexOf(who + contextType
        + "Handler3.MessageContext.getProperty(OUTBOUND" + who + contextType
        + "MessageScopeHandlerPropSetByHandler1)=SetByHandler1") == -1) {
      TestUtil.logErr(who + contextType
          + "Handler3.MessageContext.getProperty(OUTBOUND" + who + contextType
          + "MessageScopeHandlerPropSetByHandler1)=SetByHandler1 was not found");
      pass = false;
    }
    if (calls.indexOf(who + contextType
        + "Handler3.MessageContext.getPropertyScope(OUTBOUND" + who
        + contextType + "CrossHandlerPropSetByHandler1)=HANDLER") == -1) {
      TestUtil.logErr(who + contextType
          + "Handler3.MessageContext.getPropertyScope(OUTBOUND" + who
          + contextType
          + "CrossHandlerPropSetByHandler1)=HANDLER was not found");
      pass = false;
    }
    if (calls.indexOf(
        who + contextType + "Handler3.MessageContext.getPropertyScope(OUTBOUND"
            + who + contextType
            + "MessageScopeAppPropSetByHandler1)=APPLICATION") == -1) {
      TestUtil.logErr(who + contextType
          + "Handler3.MessageContext.getPropertyScope(OUTBOUND" + who
          + contextType
          + "MessageScopeAppPropSetByHandler1)=APPLICATION was not found");
      pass = false;
    }
    if (calls.indexOf(
        who + contextType + "Handler3.MessageContext.getPropertyScope(OUTBOUND"
            + who + contextType
            + "MessageScopeHandlerPropSetByHandler1)=HANDLER") == -1) {
      TestUtil.logErr(who + contextType
          + "Handler3.MessageContext.getPropertyScope(OUTBOUND" + who
          + contextType
          + "MessageScopeHandlerPropSetByHandler1)=HANDLER was not found");
      pass = false;
    }

    if (calls.indexOf(who + contextType + "Handler3.MessageContext.put(INBOUND"
        + who + contextType
        + "CrossHandlerPropSetByHandler3,SetByHandler3)") == -1) {
      TestUtil.logErr(who + contextType + "Handler3.MessageContext.put(OUTBOUND"
          + who + contextType
          + "CrossHandlerPropSetByHandler3,SetByHandler3) was not found");
      pass = false;
    }
    if (calls.indexOf(who + contextType + "Handler3.MessageContext.put(INBOUND"
        + who + contextType
        + "MessageScopeAppPropSetByHandler3,SetByHandler3)") == -1) {
      TestUtil.logErr(who + contextType + "Handler3.MessageContext.put(OUTBOUND"
          + who + contextType
          + "MessageScopeAppPropSetByHandler3,SetByHandler3) was not found");
      pass = false;
    }
    if (calls.indexOf(who + contextType
        + "Handler3.MessageContext.setPropertyScope(INBOUND" + who + contextType
        + "MessageScopeAppPropSetByHandler3,APPLICATION)") == -1) {
      TestUtil.logErr(who + contextType
          + "Handler3.MessageContext.setPropertyScope(OUTBOUND" + who
          + contextType
          + "MessageScopeAppPropSetByHandler3,APPLICATION) was not found");
      pass = false;
    }
    if (calls.indexOf(who + contextType + "Handler3.MessageContext.put(INBOUND"
        + who + contextType
        + "MessageScopeHandlerPropSetByHandler3,SetByHandler3)") == -1) {
      TestUtil.logErr(who + contextType + "Handler3.MessageContext.put(OUTBOUND"
          + who + contextType
          + "MessageScopeHandlerPropSetByHandler3,SetByHandler3) was not found");
      pass = false;
    }
    if (calls.indexOf(who + contextType
        + "Handler3.MessageContext.setPropertyScope(INBOUND" + who + contextType
        + "MessageScopeHandlerPropSetByHandler3,HANDLER)") == -1) {
      TestUtil.logErr(who + contextType
          + "Handler3.MessageContext.setPropertyScope(OUTBOUND" + who
          + contextType
          + "MessageScopeHandlerPropSetByHandler3,HANDLER) was not found");
      pass = false;
    }
    if (who.equals("Client")) {
      if (calls.indexOf(who + contextType
          + "Handler2.MessageContext.getProperty(" + who + "To" + who
          + "Prop)=clientOUTBOUNDClient" + contextType + "Handler2") == -1) {
        TestUtil
            .logErr(who + contextType + "Handler2.MessageContext.getProperty("
                + who + "To" + who + "Prop)=clientOUTBOUNDClient" + contextType
                + "Handler2 was not found");
        pass = false;
      }
      if (calls.indexOf(who + contextType + "Handler2.MessageContext.put(" + who
          + "To" + who + "Prop,clientOUTBOUNDClient" + contextType
          + "Handler2INBOUNDClient" + contextType + "Handler2)") == -1) {
        TestUtil.logErr(who + contextType + "Handler2.MessageContext.put(" + who
            + "To" + who + "Prop,clientOUTBOUNDClient" + contextType
            + "Handler2INBOUNDClient" + contextType
            + "Handler2) was not found");
        pass = false;
      }
      if (calls.indexOf(
          who + contextType + "Handler2.MessageContext.setPropertyScope(" + who
              + "To" + who + "Prop,APPLICATION)") == -1) {
        TestUtil.logErr(
            who + contextType + "Handler2.MessageContext.setPropertyScope("
                + who + "To" + who + "Prop,APPLICATION) was not found");
        pass = false;
      }
    } else {
      if (calls.indexOf(who + contextType
          + "Handler2.MessageContext.put(Handler" + who
          + "HandlerProp,INBOUNDServer" + contextType
          + "Handler2serverOUTBOUNDServer" + contextType + "Handler2)") == -1) {
        TestUtil.logErr(who + contextType + "Handler2.MessageContext.put("
            + "Handler" + who + "HandlerProp,INBOUNDServer" + contextType
            + "Handler2serverOUTBOUNDServer" + contextType
            + "Handler2) was not found");
        pass = false;
      }
      if (calls.indexOf(
          who + contextType + "Handler2.MessageContext.setPropertyScope(Handler"
              + who + "HandlerProp,APPLICATION)") == -1) {
        TestUtil.logErr(
            who + contextType + "Handler2.MessageContext.setPropertyScope("
                + "Handler" + who + "HandlerProp,APPLICATION) was not found");
        pass = false;
      }
    }

    if (calls.indexOf(who + contextType
        + "Handler1.MessageContext.getProperty(INBOUND" + who + contextType
        + "CrossHandlerPropSetByHandler3)=SetByHandler3") == -1) {
      TestUtil.logErr(who + contextType
          + "Handler1.MessageContext.getProperty(INBOUND" + who + contextType
          + "CrossHandlerPropSetByHandler3)=SetByHandler3 was not found");
      pass = false;
    }
    if (calls.indexOf(who + contextType
        + "Handler1.MessageContext.getProperty(INBOUND" + who + contextType
        + "MessageScopeAppPropSetByHandler3)=SetByHandler3") == -1) {
      TestUtil.logErr(who + contextType
          + "Handler1.MessageContext.getProperty(INBOUND" + who + contextType
          + "MessageScopeAppPropSetByHandler3)=SetByHandler3 was not found");
      pass = false;
    }
    if (calls.indexOf(who + contextType
        + "Handler1.MessageContext.getProperty(INBOUND" + who + contextType
        + "MessageScopeHandlerPropSetByHandler3)=SetByHandler3") == -1) {
      TestUtil.logErr(who + contextType
          + "Handler1.MessageContext.getProperty(INBOUND" + who + contextType
          + "MessageScopeHandlerPropSetByHandler3)=SetByHandler3 was not found");
      pass = false;
    }
    if (calls.indexOf(who + contextType
        + "Handler1.MessageContext.getPropertyScope(INBOUND" + who + contextType
        + "CrossHandlerPropSetByHandler3)=HANDLER") == -1) {
      TestUtil.logErr(
          who + contextType + "Handler1.MessageContext.getPropertyScope(INBOUND"
              + who + contextType
              + "CrossHandlerPropSetByHandler3)=HANDLER was not found");
      pass = false;
    }
    if (calls.indexOf(who + contextType
        + "Handler1.MessageContext.getPropertyScope(INBOUND" + who + contextType
        + "MessageScopeAppPropSetByHandler3)=APPLICATION") == -1) {
      TestUtil.logErr(
          who + contextType + "Handler1.MessageContext.getPropertyScope(INBOUND"
              + who + contextType
              + "MessageScopeAppPropSetByHandler3)=APPLICATION was not found");
      pass = false;
    }
    if (calls.indexOf(who + contextType
        + "Handler1.MessageContext.getPropertyScope(INBOUND" + who + contextType
        + "MessageScopeHandlerPropSetByHandler3)=HANDLER") == -1) {
      TestUtil.logErr(
          who + contextType + "Handler1.MessageContext.getPropertyScope(INBOUND"
              + who + contextType
              + "MessageScopeHandlerPropSetByHandler3)=HANDLER was not found");
      pass = false;
    }

    return pass;
  }

  public static boolean VerifyLogicalOrSOAPMessageContextCallBacks(String who,
      String contextType, List<String> calls) {
    boolean pass = true;

    if (calls == null) {
      TestUtil.logErr("Callback string is null (unexpected)");
      return false;
    }

    JWS_Util.dumpList(calls);

    if (calls.indexOf("Inbound" + who + contextType + "Handler2." + contextType
        + "MessageContext.getMessage()=returned nonnull") == -1) {
      TestUtil.logErr("Inbound" + who + contextType + "Handler2." + contextType
          + "MessageContext.getMessage()=returned nonnull was not called");
      pass = false;
    }
    if (calls.indexOf("Outbound" + who + contextType + "Handler2." + contextType
        + "MessageContext.getMessage()=returned nonnull") == -1) {
      TestUtil.logErr("Outbound" + who + contextType + "Handler2." + contextType
          + "MessageContext.getMessage()=returned nonnull was not called");
      pass = false;
    }

    return pass;
  }

  public static boolean VerifyStandardMessageContextPropertiesCallBacks(
      String who, String contextType, List<String> calls) {
    boolean pass = true;

    if (calls == null) {
      TestUtil.logErr("Callback string is null (unexpected)");
      return false;
    }

    JWS_Util.dumpList(calls);
    if (who.equals("Client")) {

      if (calls.indexOf("Outbound" + who + contextType
          + "Handler2.MessageContext.getProperty(" + contextType
          + "MessageContext.HTTP_REQUEST_METHOD)=null") == -1) {
        TestUtil.logErr("Outbound" + who + contextType
            + "Handler2.MessageContext.getProperty(" + contextType
            + "MessageContext.HTTP_REQUEST_METHOD)=null was not found");
        pass = false;
      }
      if (calls.indexOf("Outbound" + who + contextType
          + "Handler2.MessageContext.getProperty(MessageContext.HTTP_REQUEST_METHOD)=null") == -1) {
        TestUtil.logErr("Outbound" + who + contextType
            + "Handler2.MessageContext.getProperty(MessageContext.HTTP_REQUEST_METHOD)=null was not found");
        pass = false;
      }
      if (calls.indexOf("Outbound" + who + contextType
          + "Handler2.MessageContext.getProperty(" + contextType
          + "MessageContext.HTTP_RESPONSE_CODE)=null") == -1) {
        TestUtil.logErr("Outbound" + who + contextType
            + "Handler2.MessageContext.getProperty(" + contextType
            + "MessageContext.HTTP_RESPONSE_CODE)=null was not found");
        pass = false;
      }
      if (calls.indexOf("Outbound" + who + contextType
          + "Handler2.MessageContext.getProperty(MessageContext.HTTP_RESPONSE_CODE)=null") == -1) {
        TestUtil.logErr("Outbound" + who + contextType
            + "Handler2.MessageContext.getProperty(MessageContext.HTTP_RESPONSE_CODE)=null was not found");
        pass = false;
      }
      if (calls.indexOf("Outbound" + who + contextType
          + "Handler2.MessageContext.getProperty(" + contextType
          + "MessageContext.HTTP_REQUEST_HEADERS)=null") == -1) {
        TestUtil.logErr("Outbound" + who + contextType
            + "Handler2.MessageContext.getProperty(" + contextType
            + "MessageContext.HTTP_REQUEST_HEADERS)=null was not found");
        pass = false;
      }
      if (calls.indexOf("Outbound" + who + contextType
          + "Handler2.MessageContext.getProperty(MessageContext.HTTP_REQUEST_HEADERS)=null") == -1) {
        TestUtil.logErr("Outbound" + who + contextType
            + "Handler2.MessageContext.getProperty(MessageContext.HTTP_REQUEST_HEADERS)=null was not found");
        pass = false;
      }
      if (calls.indexOf("Outbound" + who + contextType
          + "Handler2.MessageContext.getProperty(" + contextType
          + "MessageContext.HTTP_RESPONSE_HEADERS)=null") == -1) {
        TestUtil.logErr("Outbound" + who + contextType
            + "Handler2.MessageContext.getProperty(" + contextType
            + "MessageContext.HTTP_RESPONSE_HEADERS)=null was not found");
        pass = false;
      }
      if (calls.indexOf("Outbound" + who + contextType
          + "Handler2.MessageContext.getProperty(MessageContext.HTTP_RESPONSE_HEADERS)=null") == -1) {
        TestUtil.logErr("Outbound" + who + contextType
            + "Handler2.MessageContext.getProperty(MessageContext.HTTP_RESPONSE_HEADERS)=null was not found");
        pass = false;
      }
      if (calls.indexOf("Outbound" + who + contextType
          + "Handler2.MessageContext.getProperty(" + contextType
          + "MessageContext.SERVLET_REQUEST)=null") == -1) {
        TestUtil.logErr("Outbound" + who + contextType
            + "Handler2.MessageContext.getProperty(" + contextType
            + "MessageContext.SERVLET_REQUEST)=null was not found");
        pass = false;
      }
      if (calls.indexOf("Outbound" + who + contextType
          + "Handler2.MessageContext.getProperty(MessageContext.SERVLET_REQUEST)=null") == -1) {
        TestUtil.logErr("Outbound" + who + contextType
            + "Handler2.MessageContext.getProperty(MessageContext.SERVLET_REQUEST)=null was not found");
        pass = false;
      }
      if (calls.indexOf("Outbound" + who + contextType
          + "Handler2.MessageContext.getProperty(" + contextType
          + "MessageContext.SERVLET_RESPONSE)=null") == -1) {
        TestUtil.logErr("Outbound" + who + contextType
            + "Handler2.MessageContext.getProperty(" + contextType
            + "MessageContext.SERVLET_RESPONSE)=null was not found");
        pass = false;
      }
      if (calls.indexOf("Outbound" + who + contextType
          + "Handler2.MessageContext.getProperty(MessageContext.SERVLET_RESPONSE)=null") == -1) {
        TestUtil.logErr("Outbound" + who + contextType
            + "Handler2.MessageContext.getProperty(MessageContext.SERVLET_RESPONSE)=null was not found");
        pass = false;
      }
      if (calls.indexOf("Outbound" + who + contextType
          + "Handler2.MessageContext.getProperty(" + contextType
          + "MessageContext.SERVLET_CONTEXT)=null") == -1) {
        TestUtil.logErr("Outbound" + who + contextType
            + "Handler2.MessageContext.getProperty(" + contextType
            + "MessageContext.SERVLET_CONTEXT)=null was not found");
        pass = false;
      }
      if (calls.indexOf("Outbound" + who + contextType
          + "Handler2.MessageContext.getProperty(MessageContext.SERVLET_CONTEXT)=null") == -1) {
        TestUtil.logErr("Outbound" + who + contextType
            + "Handler2.MessageContext.getProperty(MessageContext.SERVLET_CONTEXT)=null was not found");
        pass = false;
      }

      if (calls.indexOf("Inbound" + who + contextType
          + "Handler2.MessageContext.getProperty(" + contextType
          + "MessageContext.HTTP_REQUEST_METHOD)=null") == -1) {
        TestUtil.logErr("Inbound" + who + contextType
            + "Handler2.MessageContext.getProperty(" + contextType
            + "MessageContext.HTTP_REQUEST_METHOD)=null was not found");
        pass = false;
      }
      if (calls.indexOf("Inbound" + who + contextType
          + "Handler2.MessageContext.getProperty(MessageContext.HTTP_REQUEST_METHOD)=null") == -1) {
        TestUtil.logErr("Inbound" + who + contextType
            + "Handler2.MessageContext.getProperty(MessageContext.HTTP_REQUEST_METHOD)=null was not found");
        pass = false;
      }
      if (calls.indexOf(
          "Inbound" + who + contextType + "Handler2.MessageContext.getProperty("
              + contextType + "MessageContext.HTTP_RESPONSE_CODE)=200") == -1) {
        TestUtil.logErr("Inbound" + who + contextType
            + "Handler2.MessageContext.getProperty(" + contextType
            + "MessageContext.HTTP_RESPONSE_CODE)=200 was not found");
        pass = false;
      }
      if (calls.indexOf("Inbound" + who + contextType
          + "Handler2.MessageContext.getProperty(MessageContext.HTTP_RESPONSE_CODE)=200") == -1) {
        TestUtil.logErr("Inbound" + who + contextType
            + "Handler2.MessageContext.getProperty(MessageContext.HTTP_RESPONSE_CODE)=200 was not found");
        pass = false;
      }
      if (calls.indexOf("Inbound" + who + contextType
          + "Handler2.MessageContext.getProperty(" + contextType
          + "MessageContext.HTTP_REQUEST_HEADERS)=null") == -1) {
        TestUtil.logErr("Inbound" + who + contextType
            + "Handler2.MessageContext.getProperty(" + contextType
            + "MessageContext.HTTP_REQUEST_HEADERS)=null was not found");
        pass = false;
      }
      if (calls.indexOf("Inbound" + who + contextType
          + "Handler2.MessageContext.getProperty(MessageContext.HTTP_REQUEST_HEADERS)=null") == -1) {
        TestUtil.logErr("Inbound" + who + contextType
            + "Handler2.MessageContext.getProperty(MessageContext.HTTP_REQUEST_HEADERS)=null was not found");
        pass = false;
      }
      if (calls.indexOf("Inbound" + who + contextType
          + "Handler2.MessageContext.getProperty(" + contextType
          + "MessageContext.HTTP_RESPONSE_HEADERS)=null") != -1) {
        TestUtil.logErr("Inbound" + who + contextType
            + "Handler2.MessageContext.getProperty(" + contextType
            + "MessageContext.HTTP_RESPONSE_HEADERS)=null was found");
        pass = false;
      }
      if (calls.indexOf("Inbound" + who + contextType
          + "Handler2.MessageContext.getProperty(MessageContext.HTTP_RESPONSE_HEADERS)=null") != -1) {
        TestUtil.logErr("Inbound" + who + contextType
            + "Handler2.MessageContext.getProperty(MessageContext.HTTP_RESPONSE_HEADERS)=null was found");
        pass = false;
      }
      if (calls.indexOf(
          "Inbound" + who + contextType + "Handler2.MessageContext.getProperty("
              + contextType + "MessageContext.SERVLET_REQUEST)=null") == -1) {
        TestUtil.logErr("Inbound" + who + contextType
            + "Handler2.MessageContext.getProperty(" + contextType
            + "MessageContext.SERVLET_REQUEST)=null was not found");
        pass = false;
      }
      if (calls.indexOf("Inbound" + who + contextType
          + "Handler2.MessageContext.getProperty(MessageContext.SERVLET_REQUEST)=null") == -1) {
        TestUtil.logErr("Inbound" + who + contextType
            + "Handler2.MessageContext.getProperty(MessageContext.SERVLET_REQUEST)=null was not found");
        pass = false;
      }
      if (calls.indexOf(
          "Inbound" + who + contextType + "Handler2.MessageContext.getProperty("
              + contextType + "MessageContext.SERVLET_RESPONSE)=null") == -1) {
        TestUtil.logErr("Inbound" + who + contextType
            + "Handler2.MessageContext.getProperty(" + contextType
            + "MessageContext.SERVLET_RESPONSE)=null was not found");
        pass = false;
      }
      if (calls.indexOf("Inbound" + who + contextType
          + "Handler2.MessageContext.getProperty(MessageContext.SERVLET_RESPONSE)=null") == -1) {
        TestUtil.logErr("Inbound" + who + contextType
            + "Handler2.MessageContext.getProperty(MessageContext.SERVLET_RESPONSE)=null was not found");
        pass = false;
      }
      if (calls.indexOf(
          "Inbound" + who + contextType + "Handler2.MessageContext.getProperty("
              + contextType + "MessageContext.SERVLET_CONTEXT)=null") == -1) {
        TestUtil.logErr("Inbound" + who + contextType
            + "Handler2.MessageContext.getProperty(" + contextType
            + "MessageContext.SERVLET_CONTEXT)=null was not found");
        pass = false;
      }
      if (calls.indexOf("Inbound" + who + contextType
          + "Handler2.MessageContext.getProperty(MessageContext.SERVLET_CONTEXT)=null") == -1) {
        TestUtil.logErr("Inbound" + who + contextType
            + "Handler2.MessageContext.getProperty(MessageContext.SERVLET_CONTEXT)=null was not found");
        pass = false;
      }

    } else {
      // server

      if (calls.indexOf("Inbound" + who + contextType
          + "Handler2.MessageContext.getProperty(" + contextType
          + "MessageContext.HTTP_REQUEST_METHOD)=POST") == -1) {
        TestUtil.logErr("Inbound" + who + contextType
            + "Handler2.MessageContext.getProperty(" + contextType
            + "MessageContext.HTTP_REQUEST_METHOD)=POST was not found");
        pass = false;
      }
      if (calls.indexOf("Inbound" + who + contextType
          + "Handler2.MessageContext.getProperty(MessageContext.HTTP_REQUEST_METHOD)=POST") == -1) {
        TestUtil.logErr("Inbound" + who + contextType
            + "Handler2.MessageContext.getProperty(MessageContext.HTTP_REQUEST_METHOD)=POST was not found");
        pass = false;
      }
      if (calls.indexOf("Inbound" + who + contextType
          + "Handler2.MessageContext.getProperty(" + contextType
          + "MessageContext.HTTP_RESPONSE_CODE)=null") == -1) {
        TestUtil.logErr("Inbound" + who + contextType
            + "Handler2.MessageContext.getProperty(" + contextType
            + "MessageContext.HTTP_RESPONSE_CODE)=null was not found");
        pass = false;
      }
      if (calls.indexOf("Inbound" + who + contextType
          + "Handler2.MessageContext.getProperty(MessageContext.HTTP_RESPONSE_CODE)=null") == -1) {
        TestUtil.logErr("Inbound" + who + contextType
            + "Handler2.MessageContext.getProperty(MessageContext.HTTP_RESPONSE_CODE)=null was not found");
        pass = false;
      }
      if (calls.indexOf("Inbound" + who + contextType
          + "Handler2.MessageContext.getProperty(" + contextType
          + "MessageContext.HTTP_REQUEST_HEADERS)=null") != -1) {
        TestUtil.logErr("Inbound" + who + contextType
            + "Handler2.MessageContext.getProperty(" + contextType
            + "MessageContext.HTTP_REQUEST_HEADERS)=null was found");
        pass = false;
      }
      if (calls.indexOf("Inbound" + who + contextType
          + "Handler2.MessageContext.getProperty(MessageContext.HTTP_REQUEST_HEADERS)=null") != -1) {
        TestUtil.logErr("Inbound" + who + contextType
            + "Handler2.MessageContext.getProperty(MessageContext.HTTP_REQUEST_HEADERS)=null was found");
        pass = false;
      }
      if (calls.indexOf("Inbound" + who + contextType
          + "Handler2.MessageContext.getProperty(" + contextType
          + "MessageContext.HTTP_RESPONSE_HEADERS)=null") == -1) {
        TestUtil.logErr("Inbound" + who + contextType
            + "Handler2.MessageContext.getProperty(" + contextType
            + "MessageContext.HTTP_RESPONSE_HEADERS)=null was not found");
        pass = false;
      }
      if (calls.indexOf("Inbound" + who + contextType
          + "Handler2.MessageContext.getProperty(MessageContext.HTTP_RESPONSE_HEADERS)=null") == -1) {
        TestUtil.logErr("Inbound" + who + contextType
            + "Handler2.MessageContext.getProperty(MessageContext.HTTP_RESPONSE_HEADERS)=null was not found");
        pass = false;
      }
      if (calls.indexOf(
          "Inbound" + who + contextType + "Handler2.MessageContext.getProperty("
              + contextType + "MessageContext.SERVLET_REQUEST)=null") != -1) {
        TestUtil.logErr("Inbound" + who + contextType
            + "Handler2.MessageContext.getProperty(" + contextType
            + "MessageContext.SERVLET_REQUEST)=null was found");
        pass = false;
      }
      if (calls.indexOf("Inbound" + who + contextType
          + "Handler2.MessageContext.getProperty(MessageContext.SERVLET_REQUEST)=null") != -1) {
        TestUtil.logErr("Inbound" + who + contextType
            + "Handler2.MessageContext.getProperty(MessageContext.SERVLET_REQUEST)=null was found");
        pass = false;
      }
      if (calls.indexOf(
          "Inbound" + who + contextType + "Handler2.MessageContext.getProperty("
              + contextType + "MessageContext.SERVLET_RESPONSE)=null") != -1) {
        TestUtil.logErr("Inbound" + who + contextType
            + "Handler2.MessageContext.getProperty(" + contextType
            + "MessageContext.SERVLET_RESPONSE)=null was found");
        pass = false;
      }
      if (calls.indexOf("Inbound" + who + contextType
          + "Handler2.MessageContext.getProperty(MessageContext.SERVLET_RESPONSE)=null") != -1) {
        TestUtil.logErr("Inbound" + who + contextType
            + "Handler2.MessageContext.getProperty(MessageContext.SERVLET_RESPONSE)=null was found");
        pass = false;
      }
      if (calls.indexOf(
          "Inbound" + who + contextType + "Handler2.MessageContext.getProperty("
              + contextType + "MessageContext.SERVLET_CONTEXT)=null") != -1) {
        TestUtil.logErr("Inbound" + who + contextType
            + "Handler2.MessageContext.getProperty(" + contextType
            + "MessageContext.SERVLET_CONTEXT)=null was found");
        pass = false;
      }
      if (calls.indexOf("Inbound" + who + contextType
          + "Handler2.MessageContext.getProperty(MessageContext.SERVLET_CONTEXT)=null") != -1) {
        TestUtil.logErr("Inbound" + who + contextType
            + "Handler2.MessageContext.getProperty(MessageContext.SERVLET_CONTEXT)=null was found");
        pass = false;
      }

      if (calls.indexOf("Outbound" + who + contextType
          + "Handler2.MessageContext.getProperty(" + contextType
          + "MessageContext.HTTP_REQUEST_METHOD)=POST") == -1) {
        TestUtil.logErr("Outbound" + who + contextType
            + "Handler2.MessageContext.getProperty(" + contextType
            + "MessageContext.HTTP_REQUEST_METHOD)=POST was not found");
        pass = false;
      }
      if (calls.indexOf("Outbound" + who + contextType
          + "Handler2.MessageContext.getProperty(MessageContext.HTTP_REQUEST_METHOD)=POST") == -1) {
        TestUtil.logErr("Outbound" + who + contextType
            + "Handler2.MessageContext.getProperty(MessageContext.HTTP_REQUEST_METHOD)=POST was not found");
        pass = false;
      }
      if (calls.indexOf("Outbound" + who + contextType
          + "Handler2.MessageContext.getProperty(" + contextType
          + "MessageContext.HTTP_RESPONSE_CODE)=null") == -1) {
        TestUtil.logErr("Outbound" + who + contextType
            + "Handler2.MessageContext.getProperty(" + contextType
            + "MessageContext.HTTP_RESPONSE_CODE)=null was not found");
        pass = false;
      }
      if (calls.indexOf("Outbound" + who + contextType
          + "Handler2.MessageContext.getProperty(MessageContext.HTTP_RESPONSE_CODE)=null") == -1) {
        TestUtil.logErr("Outbound" + who + contextType
            + "Handler2.MessageContext.getProperty(MessageContext.HTTP_RESPONSE_CODE)=null was not found");
        pass = false;
      }
      if (calls.indexOf("Outbound" + who + contextType
          + "Handler2.MessageContext.getProperty(" + contextType
          + "MessageContext.HTTP_REQUEST_HEADERS)=null") != -1) {
        TestUtil.logErr("Outbound" + who + contextType
            + "Handler2.MessageContext.getProperty(" + contextType
            + "MessageContext.HTTP_REQUEST_HEADERS)=null was found");
        pass = false;
      }
      if (calls.indexOf("Outbound" + who + contextType
          + "Handler2.MessageContext.getProperty(MessageContext.HTTP_REQUEST_HEADERS)=null") != -1) {
        TestUtil.logErr("Outbound" + who + contextType
            + "Handler2.MessageContext.getProperty(MessageContext.HTTP_REQUEST_HEADERS)=null was found");
        pass = false;
      }
      if (calls.indexOf("Outbound" + who + contextType
          + "Handler2.MessageContext.getProperty(" + contextType
          + "MessageContext.HTTP_RESPONSE_HEADERS)=null") == -1) {
        TestUtil.logErr("Outbound" + who + contextType
            + "Handler2.MessageContext.getProperty(" + contextType
            + "MessageContext.HTTP_RESPONSE_HEADERS)=null was not found");
        pass = false;
      }
      if (calls.indexOf("Outbound" + who + contextType
          + "Handler2.MessageContext.getProperty(MessageContext.HTTP_RESPONSE_HEADERS)=null") == -1) {
        TestUtil.logErr("Outbound" + who + contextType
            + "Handler2.MessageContext.getProperty(MessageContext.HTTP_RESPONSE_HEADERS)=null was not found");
        pass = false;
      }
      if (calls.indexOf("Outbound" + who + contextType
          + "Handler2.MessageContext.getProperty(" + contextType
          + "MessageContext.SERVLET_REQUEST)=null") != -1) {
        TestUtil.logErr("Outbound" + who + contextType
            + "Handler2.MessageContext.getProperty(" + contextType
            + "MessageContext.SERVLET_REQUEST)=null was found");
        pass = false;
      }
      if (calls.indexOf("Outbound" + who + contextType
          + "Handler2.MessageContext.getProperty(MessageContext.SERVLET_REQUEST)=null") != -1) {
        TestUtil.logErr("Outbound" + who + contextType
            + "Handler2.MessageContext.getProperty(MessageContext.SERVLET_REQUEST)=null was found");
        pass = false;
      }
      if (calls.indexOf("Outbound" + who + contextType
          + "Handler2.MessageContext.getProperty(" + contextType
          + "MessageContext.SERVLET_RESPONSE)=null") != -1) {
        TestUtil.logErr("Outbound" + who + contextType
            + "Handler2.MessageContext.getProperty(" + contextType
            + "MessageContext.SERVLET_RESPONSE)=null was found");
        pass = false;
      }
      if (calls.indexOf("Outbound" + who + contextType
          + "Handler2.MessageContext.getProperty(MessageContext.SERVLET_RESPONSE)=null") != -1) {
        TestUtil.logErr("Outbound" + who + contextType
            + "Handler2.MessageContext.getProperty(MessageContext.SERVLET_RESPONSE)=null was found");
        pass = false;
      }
      if (calls.indexOf("Outbound" + who + contextType
          + "Handler2.MessageContext.getProperty(" + contextType
          + "MessageContext.SERVLET_CONTEXT)=null") != -1) {
        TestUtil.logErr("Outbound" + who + contextType
            + "Handler2.MessageContext.getProperty(" + contextType
            + "MessageContext.SERVLET_CONTEXT)=null was found");
        pass = false;
      }
      if (calls.indexOf("Outbound" + who + contextType
          + "Handler2.MessageContext.getProperty(MessageContext.SERVLET_CONTEXT)=null") != -1) {
        TestUtil.logErr("Outbound" + who + contextType
            + "Handler2.MessageContext.getProperty(MessageContext.SERVLET_CONTEXT)=null was found");
        pass = false;
      }

    }

    return pass;
  }

}
