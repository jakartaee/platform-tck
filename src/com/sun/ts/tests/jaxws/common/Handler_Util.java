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

import javax.xml.soap.*;
import javax.xml.ws.soap.*;
import javax.xml.ws.*;
import javax.xml.ws.handler.*;
import javax.xml.ws.handler.soap.*;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMResult;

import java.util.*;

import com.sun.ts.lib.util.*;

import java.io.File;

public final class Handler_Util {

  private static boolean traceFlag = false;

  public static void setTraceFlag(boolean b) {
    traceFlag = b;
  }

  public static void setTraceFlag(String s) {
    traceFlag = Boolean.valueOf(s).booleanValue();
    // traceFlag = new Boolean(s).booleanValue();;
  }

  public static boolean getTraceFlag(boolean b) {
    return traceFlag;
  }

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

  public static boolean checkForMsg(Handler handler, MessageContext context,
      String whichMsg) {
    boolean foundIt = false;
    TestUtil.logTrace("--------------------------------------------");
    TestUtil.logTrace("Checking if this is a '" + whichMsg + "' message");
    try {
      if (context instanceof LogicalMessageContext) {
        LogicalMessage lm = ((LogicalMessageContext) context).getMessage();
        if (lm != null) {
          Source source = lm.getPayload();
          if (source != null) {
            String msg = JAXWS_Util
                .getDOMResultAsString(JAXWS_Util.getSourceAsDOMResult(source));
            TestUtil.logTrace("msg=" + msg);
            TestUtil.logTrace("whichMsg=" + whichMsg);

            if (msg.indexOf(whichMsg) > -1) {
              foundIt = true;
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
        // JAXWS_Util.dumpSOAPMessageWOA(soapmsg);
        String msg = JAXWS_Util.getMsgAsString(soapmsg);
        if (msg != null) {
          if (msg.indexOf(whichMsg) > -1) {
            foundIt = true;
          }
        }
      }
    } catch (Exception e) {
      if (handler != null) {
        HandlerTracker.reportThrowable(handler, e);
      } else {
        e.printStackTrace();
      }
    }
    TestUtil.logTrace("foundIt=" + foundIt);
    TestUtil.logTrace("--------------------------------------------");
    return foundIt;
  }

  public static boolean checkForMsg(MessageContext context, String whichMsg) {
    return checkForMsg(null, context, whichMsg);
  }

  public static void dumpMsg(MessageContext context) {
    boolean foundIt = false;
    TestUtil.logTrace("--------------------------------------------");
    if (context instanceof LogicalMessageContext) {
      try {
        LogicalMessage lm = ((LogicalMessageContext) context).getMessage();
        if (lm != null) {
          Source source = lm.getPayload();
          if (source != null) {
            String msg = JAXWS_Util
                .getDOMResultAsString(JAXWS_Util.getSourceAsDOMResult(source));
            TestUtil.logTrace("msg=" + msg);
          } else {
            TestUtil.logTrace(
                "LogicalHandlerBase(checkForMsg): No message payload was present");
          }
        } else {
          TestUtil.logTrace(
              "LogicalHandlerBase(checkForMsg): No message was present");
        }
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
    } else {
      try {
        SOAPMessage soapmsg = ((SOAPMessageContext) context).getMessage();
        String msg = JAXWS_Util.getMsgAsString(soapmsg);
        TestUtil.logTrace("msg=" + msg);
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
    }
    TestUtil.logTrace("--------------------------------------------");
    return;
  }

  public static void initTestUtil(Handler handler, String host, String port,
      String flag) {
    /*
     * System.out.println("initTestUtil:handler="+handler);
     * System.out.println("initTestUtil:host="+host);
     * System.out.println("initTestUtil:port="+port);
     * System.out.println("initTestUtil:flag="+flag);
     */
    initTestUtil(host, port, flag);
  }

  public static void initTestUtil(String impl, String host, String port,
      String flag) {
    /*
     * System.out.println("initTestUtil:impl="+impl);
     * System.out.println("initTestUtil:host="+host);
     * System.out.println("initTestUtil:port="+port);
     * System.out.println("initTestUtil:flag="+flag);
     */
    initTestUtil(host, port, flag);
  }

  public static void initTestUtil(String host, String port, String flag) {
    if ((host != null) && (!host.equals(""))) {
      if ((port != null) && (!port.equals(""))) {
        if ((flag != null) && (!flag.equals(""))) {
          Properties p = new Properties();
          p.setProperty("harness.host", host);
          p.setProperty("harness.log.port", port);
          p.setProperty("harness.log.traceflag", flag);
          try {
            TestUtil.init(p);
          } catch (Exception e) {
            System.out.println(e.getMessage());
          }
        } else {
          System.out.println(
              "Can't init TestUtil, harnesslogtraceflag was either null or empty");
        }
      } else {
        System.out.println(
            "Can't init TestUtil, harnesslogport was either null or empty");
      }
    } else {
      System.out.println(
          "Can't init TestUtil, harnessloghost was either null or empty");
    }
  }

  public static String getValueFromMsg(Handler handler, MessageContext context,
      String name) {
    String value = "";
    TestUtil
        .logTrace("Getting the value for item '" + name + "' from the message");
    try {
      if (context instanceof LogicalMessageContext) {
        LogicalMessage lm = ((LogicalMessageContext) context).getMessage();
        if (lm != null) {
          Source source = lm.getPayload();
          if (source != null) {
            DOMResult dr = JAXWS_Util.getSourceAsDOMResult(source);
            if (dr != null) {
              String tmp = XMLUtils.getNodeValue_(dr.getNode(), name);
              if (traceFlag) {
                TestUtil.logTrace("name=" + name);
                TestUtil.logTrace("value=" + tmp);
              }
              if (tmp != null) {
                value = tmp;
              }
            }
          } else {
            TestUtil.logErr(
                "LogicalHandlerBase(getValueFromMsg): No message payload was present");
          }
        } else {
          TestUtil.logTrace(
              "LogicalHandlerBase(getValueFromMsg): No message was present");
        }
      } else {
        SOAPMessage soapmsg = ((SOAPMessageContext) context).getMessage();
        String tmp = XMLUtils.getNodeValue_(soapmsg.getSOAPPart(), name);
        if (traceFlag) {
          TestUtil.logTrace("name=" + name);
          TestUtil.logTrace("value=" + tmp);
        }
        if (tmp != null) {
          value = tmp;
        }
      }
    } catch (Exception e) {
      HandlerTracker.reportThrowable(handler, e);
    }

    return value;
  }

  public static String getMessageAsString(MessageContext context) {
    String msg = "";
    if (context instanceof LogicalMessageContext) {
      LogicalMessage lm = ((LogicalMessageContext) context).getMessage();
      if (lm != null) {
        try {
          Source source = lm.getPayload();
          if (source != null) {
            msg = JAXWS_Util
                .getDOMResultAsString(JAXWS_Util.getSourceAsDOMResult(source));
          } else {
            TestUtil.logTrace(
                "LogicalHandlerBase(getMessageAsString): No message payload was present");
          }
        } catch (Exception e) {
          TestUtil.printStackTrace(e);
        }
      } else {
        TestUtil.logTrace(
            "LogicalHandlerBase(getMessageAsString): No message was present");
      }
    } else {
      try {
        SOAPMessage soapmsg = ((SOAPMessageContext) context).getMessage();
        msg = JAXWS_Util.getMsgAsString(soapmsg);
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
    }
    return msg;
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
      String contextType, boolean exception, String direction) {
    boolean pass = true;

    if (calls == null) {
      TestUtil.logErr("Callback string is null (unexpected)");
      return false;
    }

    TestUtil.logMsg(
        "Testing that close() was called and the order in which it was called");
    if (exception) {
      TestUtil.logMsg("Testing the close callbacks for exceptions");
      int close6 = calls.indexOf(who + contextType + "Handler6.close()");
      int close4 = calls.indexOf(who + contextType + "Handler4.close()");
      int close5 = calls.indexOf(who + contextType + "Handler5.close()");
      TestUtil.logTrace("close6=" + close6);
      TestUtil.logTrace("close4=" + close4);
      TestUtil.logTrace("close5=" + close5);
      if (who.equals("Client")) {
        if (direction.equals(Constants.INBOUND)) {
          if (close6 == -1) {
            TestUtil
                .logErr(who + contextType + "Handler6.close() was not called");
            pass = false;
          }
        } else {
          if (close6 > -1) {
            TestUtil.logErr(who + contextType + "Handler6.close() was called");
            pass = false;
          }
        }
        if (close4 == -1) {
          TestUtil
              .logErr(who + contextType + "Handler4.close() was not called");
          pass = false;
        }
        if (close5 == -1) {
          TestUtil
              .logErr(who + contextType + "Handler5.close() was not called");
          pass = false;
        }
        if (close6 > -1) {
          if (close4 > -1) {
            if (close6 > close4) {
              TestUtil.logErr("The close method for handler " + who
                  + contextType + "Handler4 was not called");
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
              TestUtil.logErr("The close method for handler " + who
                  + contextType + "Handler5 was not called");
              TestUtil.logErr("after the close method for handler " + who
                  + contextType + "Handler6");
              pass = false;
            }
          }
        } else if (close4 > -1) {
          if (close5 > -1) {
            if (close4 > close5) {
              TestUtil.logErr("The close method for handler " + who
                  + contextType + "Handler5 was not called");
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
        // server
        if (close6 == -1) {
          TestUtil
              .logErr(who + contextType + "Handler6.close() was not called");
          pass = false;
        }
        if (close4 == -1) {
          TestUtil
              .logErr(who + contextType + "Handler4.close() was not called");
          pass = false;
        }
        if (direction.equals(Constants.INBOUND)) {
          if (close5 > -1) {
            TestUtil.logErr(who + contextType + "Handler5.close() was called");
            pass = false;
          }
        } else {
          if (close5 == -1) {
            TestUtil
                .logErr(who + contextType + "Handler5.close() was not called");
            pass = false;
          }
        }

        if (close6 > -1) {
          if (close4 > -1) {
            if (close6 < close4) {
              TestUtil.logErr("The close method for handler " + who
                  + contextType + "Handler4 was not called");
              TestUtil.logErr("after the close method for handler " + who
                  + contextType + "Handler6");
              pass = false;
            }
            if (close5 > -1) {
              if (close4 < close5) {
                TestUtil.logErr("The close method for handler " + who
                    + contextType + "Handler5 was not called");
                TestUtil.logErr("after the close method for handler " + who
                    + contextType + "Handler4");
                pass = false;
              }
            }
          }
          if (close5 > -1) {
            if (close6 < close5) {
              TestUtil.logErr("The close method for handler " + who
                  + contextType + "Handler5 was not called");
              TestUtil.logErr("after the close method for handler " + who
                  + contextType + "Handler6");
              pass = false;
            }
          }
        } else if (close4 > -1) {
          if (close5 > -1) {
            if (close4 < close5) {
              TestUtil.logErr("The close method for handler " + who
                  + contextType + "Handler5 was not called");
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
      }
    } else {
      TestUtil.logMsg("Testing the close callbacks for regular situations");
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
      if (close2 == -1) {
        TestUtil.logErr(who + contextType + "Handler2.close() was not called");
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
      if (who.equals("Client")) {
        if (close1 > close5) {
          TestUtil.logErr("The close method for handler " + who + contextType
              + "Handler5 was not called");
          TestUtil.logErr("after the close method for handler " + who
              + contextType + "Handler1");
          pass = false;
        }
        if (close2 > close1) {
          TestUtil.logErr("The close method for handler " + who + contextType
              + "Handler1 was not called");
          TestUtil.logErr("after the close method for handler " + who
              + contextType + "Handler2");
          pass = false;
        }
        if (close3 > close2) {
          TestUtil.logErr("The close method for handler " + who + contextType
              + "Handler2 was not called");
          TestUtil.logErr("after the close method for handler " + who
              + contextType + "Handler3");
          pass = false;
        }
        if (close6 > close3) {
          TestUtil.logErr("The close method for handler " + who + contextType
              + "Handler3 was not called");
          TestUtil.logErr("after the close method for handler " + who
              + contextType + "Handler6");
          pass = false;
        }
      } else {
        if (close6 < close3) {
          TestUtil.logErr("The close method for handler " + who + contextType
              + "Handler6 was not called");
          TestUtil.logErr("after the close method for handler " + who
              + contextType + "Handler3");
          pass = false;
        }
        if (close3 < close2) {
          TestUtil.logErr("The close method for handler " + who + contextType
              + "Handler3 was not called");
          TestUtil.logErr("after the close method for handler " + who
              + contextType + "Handler2");
          pass = false;
        }
        if (close2 < close1) {
          TestUtil.logErr("The close method for handler " + who + contextType
              + "Handler2 was not called");
          TestUtil.logErr("after the close method for handler " + who
              + contextType + "Handler1");
          pass = false;
        }
        if (close1 < close5) {
          TestUtil.logErr("The close method for handler " + who + contextType
              + "Handler1 was not called");
          TestUtil.logErr("after the close method for handler " + who
              + contextType + "Handler5");
          pass = false;
        }
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
    JAXWS_Util.dumpList(calls);

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

  public static boolean VerifyHandlerInitCallBacks(String who,
      String contextType, List<String> calls) {
    boolean pass = true;

    if (calls == null) {
      TestUtil.logErr("Callback string is null (unexpected)");
      return false;
    }
    if (who.equals("Client")) {
      TestUtil.logMsg("Testing handlers went through init phase");
      if (calls.indexOf(who + contextType + "Handler5.myInit()") > -1) {
        TestUtil.logErr(who + contextType + "Handler5.myInit() was called");
        pass = false;
      }
      if (calls.indexOf(who + contextType + "Handler1.myInit()") > -1) {
        TestUtil.logErr(who + contextType + "Handler1.myInit() was called");
        pass = false;
      }
      if (calls.indexOf(who + contextType + "Handler2.myInit()") > -1) {
        TestUtil.logErr(who + contextType + "Handler2.myInit() was called");
        pass = false;
      }
      if (calls.indexOf(who + contextType + "Handler3.myInit()") > -1) {
        TestUtil.logErr(who + contextType + "Handler3.myInit() was called");
        pass = false;
      }
      if (calls.indexOf(who + contextType + "Handler6.myInit()") > -1) {
        TestUtil.logErr(who + contextType + "Handler6.myInit() was called");
        pass = false;
      }

    }
    return pass;
  }

  public static boolean VerifyHandlerInitCallBacks2(String who,
      String contextType, List<String> calls) {
    boolean pass = true;

    if (calls == null) {
      TestUtil.logErr("Callback string is null (unexpected)");
      return false;
    }
    if (who.equals("Client")) {
      TestUtil.logMsg("Testing handlers went through init phase");
      if (calls.indexOf(who + contextType + "Handler5.myInit()") == -1) {
        TestUtil.logErr(who + contextType + "Handler5.myInit() was not called");
        pass = false;
      }
      if (calls.indexOf(who + contextType + "Handler1.myInit()") == -1) {
        TestUtil.logErr(who + contextType + "Handler1.myInit() was not called");
        pass = false;
      }
      if (calls.indexOf(who + contextType + "Handler2.myInit()") > -1) {
        TestUtil.logErr(who + contextType + "Handler2.myInit() was called");
        pass = false;
      }
      if (calls.indexOf(who + contextType + "Handler3.myInit()") > -1) {
        TestUtil.logErr(who + contextType + "Handler3.myInit() was called");
        pass = false;
      }
      if (calls.indexOf(who + contextType + "Handler6.myInit()") > -1) {
        TestUtil.logErr(who + contextType + "Handler6.myInit() was called");
        pass = false;
      }

    }
    return pass;
  }

  public static boolean VerifyHandlerInitCallBacks3(String who,
      String contextType, List<String> calls) {
    boolean pass = true;

    if (calls == null) {
      TestUtil.logErr("Callback string is null (unexpected)");
      return false;
    }
    if (who.equals("Client")) {
      TestUtil.logMsg("Testing handlers went through init phase");
      if (calls.indexOf(who + contextType + "Handler2.myInit()") > -1) {
        TestUtil.logErr(who + contextType + "Handler2.myInit() was called");
        pass = false;
      }
      if (calls.indexOf(who + contextType + "Handler3.myInit()") > -1) {
        TestUtil.logErr(who + contextType + "Handler3.myInit() was called");
        pass = false;
      }
      if (calls.indexOf(who + contextType + "Handler6.myInit()") > -1) {
        TestUtil.logErr(who + contextType + "Handler6.myInit() was called");
        pass = false;
      }

    }
    return pass;
  }

  public static boolean VerifyHandlerCallBacks(String who, String contextType,
      List<String> calls) {
    boolean pass = true;

    if (calls == null) {
      TestUtil.logErr("Callback string is null (unexpected)");
      return false;
    }
    TestUtil.logTrace("who=" + who);
    TestUtil.logTrace("\n");

    TestUtil.logTrace("The complete list of callbacks are:");
    JAXWS_Util.dumpList(calls);
    if (who.equals("Client")) {
      if (!VerifyHandlerInitCallBacks(who, contextType, calls)) {
        pass = false;
      }
    }
    if (!VerifyOutboundInboundCallBacks(who, contextType, calls)) {
      pass = false;
    }
    if (!VerifyCloseCallBacks(who, calls, contextType, false, "")) {
      pass = false;
    }
    return pass;
  }

  public static boolean VerifyHandlerCallBacks2(String who, String contextType,
      List<String> calls) {
    boolean pass = true;

    if (calls == null) {
      TestUtil.logErr("Callback string is null (unexpected)");
      return false;
    }
    TestUtil.logTrace("who=" + who);
    TestUtil.logTrace("\n");

    TestUtil.logTrace("The complete list of callbacks are:");
    JAXWS_Util.dumpList(calls);
    if (who.equals("Client")) {
      if (!VerifyHandlerInitCallBacks2(who, contextType, calls)) {
        pass = false;
      }
    }
    if (!VerifyOutboundInboundCallBacks(who, contextType, calls)) {
      pass = false;
    }
    if (!VerifyCloseCallBacks(who, calls, contextType, false, "")) {
      pass = false;
    }
    return pass;
  }

  public static boolean VerifyHandlerCallBacks3(String who, String contextType,
      List<String> calls) {
    boolean pass = true;

    if (calls == null) {
      TestUtil.logErr("Callback string is null (unexpected)");
      return false;
    }
    TestUtil.logTrace("who=" + who);
    TestUtil.logTrace("\n");

    TestUtil.logTrace("The complete list of callbacks are:");
    JAXWS_Util.dumpList(calls);
    if (who.equals("Client")) {
      if (!VerifyHandlerInitCallBacks3(who, contextType, calls)) {
        pass = false;
      }
    }
    if (!VerifyOutboundInboundCallBacks(who, contextType, calls)) {
      pass = false;
    }
    if (!VerifyCloseCallBacks(who, calls, contextType, false, "")) {
      pass = false;
    }
    return pass;
  }

  public static boolean VerifyOutboundInboundCallBacks(String who,
      String contextType, List<String> calls) {
    boolean pass = true;

    if (calls == null) {
      TestUtil.logErr("Callback string is null (unexpected)");
      return false;
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
    return pass;
  }

  public static boolean VerifyHandleMessageFalseCallBacks(String who,
      String contextType, List<String> calls, String direction) {
    boolean pass = true;
    String packageName = null;

    if (calls == null) {
      TestUtil.logErr("Callback string is null (unexpected)");
      return false;
    }
    TestUtil.logTrace("who=" + who);
    TestUtil.logTrace("\n");

    TestUtil.logTrace("The complete list of callbacks are:");
    JAXWS_Util.dumpList(calls);
    if (who.equals("Client")) {
      if (direction.equals(Constants.OUTBOUND)) {
        int i = calls.indexOf(
            who + contextType + "Handler5.handleMessage().doOutbound()");
        if (i == -1) {
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
        if (calls.indexOf(
            who + contextType + "Handler6.handleMessage().doOutbound()") > -1) {
          TestUtil.logErr(who + contextType
              + "Handler6.handleMessage().doOutbound() was called");
          pass = false;
        }
        int j = calls.lastIndexOf(
            who + contextType + "Handler5.handleMessage().doInbound()");
        if (i == j) {
          TestUtil.logErr("The call to " + who + contextType
              + "Handler5.handleMessage().doInbound() was not called");
          pass = false;
        }
        if (calls.indexOf(who + contextType + "Handler4.close()") == -1) {
          TestUtil
              .logErr(who + contextType + "Handler4.close() was not called");
          pass = false;
        }
        if (calls.indexOf(who + contextType + "Handler5.close()") == -1) {
          TestUtil
              .logErr(who + contextType + "Handler5.close() was not called");
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
        if (calls.indexOf(
            who + contextType + "Handler5.handleMessage().doInbound()") > -1) {
          TestUtil.logErr(who + contextType
              + "Handler5.handleMessage().doInbound() was called");
          pass = false;
        }
        if (calls.indexOf(who + contextType + "Handler6.close()") == -1) {
          TestUtil
              .logErr(who + contextType + "Handler6.close() was not called");
          pass = false;
        }
        if (calls.indexOf(who + contextType + "Handler4.close()") == -1) {
          TestUtil
              .logErr(who + contextType + "Handler4.close() was not called");
          pass = false;
        }
        if (calls.indexOf(who + contextType + "Handler5.close()") == -1) {
          TestUtil
              .logErr(who + contextType + "Handler5.close() was not called");
          pass = false;
        }
      }
    } else {
      // server
      if (direction.equals(Constants.OUTBOUND)) {
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
        if (calls.indexOf(
            who + contextType + "Handler6.handleMessage().doOutbound()") > -1) {
          TestUtil.logErr(who + contextType
              + "Handler6.handleMessage().doOutbound() was called");
          pass = false;
        }
        if (calls.indexOf(who + contextType + "Handler6.close()") == -1) {
          TestUtil
              .logErr(who + contextType + "Handler6.close() was not called");
          pass = false;
        }
        if (calls.indexOf(who + contextType + "Handler4.close()") == -1) {
          TestUtil
              .logErr(who + contextType + "Handler4.close() was not called");
          pass = false;
        }
        if (calls.indexOf(who + contextType + "Handler5.close()") == -1) {
          TestUtil
              .logErr(who + contextType + "Handler5.close() was not called");
          pass = false;
        }
      } else {
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
            who + contextType + "Handler5.handleMessage().doInbound()") > -1) {
          TestUtil.logErr(who + contextType
              + "Handler5.handleMessage().doInbound() was called");
          pass = false;
        }
        if (calls.indexOf(who + contextType
            + "Handler6.handleMessage().doOutbound()") == -1) {
          TestUtil.logErr(who + contextType
              + "Handler6.handleMessage().doOutbound() was not called");
          pass = false;
        }
        if (calls.indexOf(who + contextType + "Handler6.close()") == -1) {
          TestUtil
              .logErr(who + contextType + "Handler6.close() was not called");
          pass = false;
        }
        if (calls.indexOf(who + contextType + "Handler4.close()") == -1) {
          TestUtil
              .logErr(who + contextType + "Handler4.close() was not called");
          pass = false;
        }
        if (calls.indexOf(who + contextType + "Handler5.close()") > -1) {
          TestUtil.logErr(who + contextType + "Handler5.close() was called");
          pass = false;
        }
      }
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
    JAXWS_Util.dumpList(calls);

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

    JAXWS_Util.dumpList(calls);

    if (calls.indexOf("Inbound" + who + contextType + "Handler2." + contextType
        + "MessageContext.getMessage()=null") != -1) {
      TestUtil.logErr("Inbound" + who + contextType + "Handler2." + contextType
          + "MessageContext.getMessage()=null was called");
      pass = false;
    }
    if (calls.indexOf("Outbound" + who + contextType + "Handler2." + contextType
        + "MessageContext.getMessage()=null") != -1) {
      TestUtil.logErr("Outbound" + who + contextType + "Handler2." + contextType
          + "MessageContext.getMessage()=null was called");
      pass = false;
    }

    if (contextType.equals("SOAP")) {
      if (calls.indexOf("Inbound" + who + contextType + "Handler2."
          + contextType + "MessageContext.setMessage() was called") == -1) {
        TestUtil.logErr("Inbound" + who + contextType + "Handler2."
            + contextType + "MessageContext.setMessage() was not called");
        pass = false;
      }
      if (calls.indexOf("Inbound" + who + contextType + "Handler2."
          + contextType + "MessageContext.getRoles()=null") != -1) {
        TestUtil.logErr("Inbound" + who + contextType + "Handler2."
            + contextType + "MessageContext.getRoles()=null was called");
        pass = false;
      }
      if (calls.indexOf("Inbound" + who + contextType + "Handler2."
          + contextType + "MessageContext.getHeaders()=||") == -1) {
        TestUtil.logErr("Inbound" + who + contextType + "Handler2."
            + contextType + "MessageContext.getHeaders()=|| was not returned");
        pass = false;
      }
      if (calls.indexOf("Outbound" + who + contextType + "Handler2."
          + contextType + "MessageContext.setMessage() was called") == -1) {
        TestUtil.logErr("Outbound" + who + contextType + "Handler2."
            + contextType + "MessageContext.setMessage() was not called");
        pass = false;
      }
      if (calls.indexOf("Outbound" + who + contextType + "Handler2."
          + contextType + "MessageContext.getRoles()=null") != -1) {
        TestUtil.logErr("Outbound" + who + contextType + "Handler2."
            + contextType + "MessageContext.getRoles()=null was called");
        pass = false;
      }
      if (calls.indexOf("Outbound" + who + contextType + "Handler2."
          + contextType + "MessageContext.getHeaders()=||") == -1) {
        TestUtil.logErr("Outbound" + who + contextType + "Handler2."
            + contextType + "MessageContext.getHeaders()=|| was not returned");
        pass = false;
      }
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

    JAXWS_Util.dumpList(calls);

    if (who.equals("Client")) {

      if (JAXWS_Util.looseIndexOf(calls,
          "OutboundClient" + contextType
              + "Handler2.MessageContext.getProperty(" + contextType
              + "MessageContext.OUTBOUND_MESSAGE_ATTACHMENTS)") == -1) {
        TestUtil.logErr("OutboundClient" + contextType
            + "Handler2.MessageContext.getProperty(" + contextType
            + "MessageContext.OUTBOUND_MESSAGE_ATTACHMENTS)=key[0] - expected attachment was not found");
        pass = false;
      }
      if (JAXWS_Util.looseIndexOf(calls,
          "OutboundClient" + contextType
              + "Handler2.MessageContext.getProperty(" + contextType
              + "MessageContext.OUTBOUND_MESSAGE_ATTACHMENTS)=key[1]") != -1) {
        TestUtil.logErr("OutboundClient" + contextType
            + "Handler2.MessageContext.getProperty(" + contextType
            + "MessageContext.OUTBOUND_MESSAGE_ATTACHMENTS)=key[1] - an unexpected attachment was found");
        pass = false;
      }
      if (JAXWS_Util.looseIndexOf(calls, "OutboundClient" + contextType
          + "Handler2.MessageContext.getProperty(MessageContext.OUTBOUND_MESSAGE_ATTACHMENTS)=key[0]") == -1) {
        TestUtil.logErr("OutboundClient" + contextType
            + "Handler2.MessageContext.getProperty(MessageContext.OUTBOUND_MESSAGE_ATTACHMENTS)=key[0] - expected attachment was not found");
        pass = false;
      }
      if (JAXWS_Util.looseIndexOf(calls, "OutboundClient" + contextType
          + "Handler2.MessageContext.getProperty(MessageContext.OUTBOUND_MESSAGE_ATTACHMENTS)=key[1]") != -1) {
        TestUtil.logErr("OutboundClient" + contextType
            + "Handler2.MessageContext.getProperty(MessageContext.OUTBOUND_MESSAGE_ATTACHMENTS)=key[1] - an unexpected attachment was found");

        pass = false;
      }
      if (calls.indexOf("OutboundClient" + contextType
          + "Handler2.MessageContext.getProperty(" + contextType
          + "MessageContext.HTTP_RESPONSE_HEADERS)=null") == -1) {
        TestUtil.logErr("OutboundClient" + contextType
            + "Handler2.MessageContext.getProperty(" + contextType
            + "MessageContext.HTTP_RESPONSE_HEADERS)=null was not found");
        pass = false;
      }
      if (calls.indexOf("OutboundClient" + contextType
          + "Handler2.MessageContext.getProperty(MessageContext.HTTP_RESPONSE_HEADERS)=null") == -1) {
        TestUtil.logErr("OutboundClient" + contextType
            + "Handler2.MessageContext.getProperty(MessageContext.HTTP_RESPONSE_HEADERS)=null was not found");
        pass = false;
      }
      if (calls.indexOf("OutboundClient" + contextType
          + "Handler2.MessageContext.getProperty(" + contextType
          + "MessageContext.SERVLET_REQUEST)=null") == -1) {
        TestUtil.logErr("OutboundClient" + contextType
            + "Handler2.MessageContext.getProperty(" + contextType
            + "MessageContext.SERVLET_REQUEST)=null was not found");
        pass = false;
      }
      if (calls.indexOf("OutboundClient" + contextType
          + "Handler2.MessageContext.getProperty(MessageContext.SERVLET_REQUEST)=null") == -1) {
        TestUtil.logErr("OutboundClient" + contextType
            + "Handler2.MessageContext.getProperty(MessageContext.SERVLET_REQUEST)=null was not found");
        pass = false;
      }
      if (calls.indexOf("OutboundClient" + contextType
          + "Handler2.MessageContext.getProperty(" + contextType
          + "MessageContext.SERVLET_RESPONSE)=null") == -1) {
        TestUtil.logErr("OutboundClient" + contextType
            + "Handler2.MessageContext.getProperty(" + contextType
            + "MessageContext.SERVLET_RESPONSE)=null was not found");
        pass = false;
      }
      if (calls.indexOf("OutboundClient" + contextType
          + "Handler2.MessageContext.getProperty(MessageContext.SERVLET_RESPONSE)=null") == -1) {
        TestUtil.logErr("OutboundClient" + contextType
            + "Handler2.MessageContext.getProperty(MessageContext.SERVLET_RESPONSE)=null was not found");
        pass = false;
      }
      if (calls.indexOf("OutboundClient" + contextType
          + "Handler2.MessageContext.getProperty(" + contextType
          + "MessageContext.SERVLET_CONTEXT)=null") == -1) {
        TestUtil.logErr("OutboundClient" + contextType
            + "Handler2.MessageContext.getProperty(" + contextType
            + "MessageContext.SERVLET_CONTEXT)=null was not found");
        pass = false;
      }
      if (calls.indexOf("OutboundClient" + contextType
          + "Handler2.MessageContext.getProperty(MessageContext.SERVLET_CONTEXT)=null") == -1) {
        TestUtil.logErr("OutboundClient" + contextType
            + "Handler2.MessageContext.getProperty(MessageContext.SERVLET_CONTEXT)=null was not found");
        pass = false;
      }

      if (JAXWS_Util.looseIndexOf(calls,
          "InboundClient" + contextType + "Handler2.MessageContext.getProperty("
              + contextType
              + "MessageContext.INBOUND_MESSAGE_ATTACHMENTS)=key[0]") == -1) {
        TestUtil.logErr("InboundClient" + contextType
            + "Handler2.MessageContext.getProperty(" + contextType
            + "MessageContext.INBOUND_MESSAGE_ATTACHMENTS)=key[0] - expected attachment was not found");
        pass = false;
      }
      if (JAXWS_Util.looseIndexOf(calls,
          "InboundClient" + contextType + "Handler2.MessageContext.getProperty("
              + contextType
              + "MessageContext.INBOUND_MESSAGE_ATTACHMENTS)=key[1]") != -1) {
        TestUtil.logErr("InboundClient" + contextType
            + "Handler2.MessageContext.getProperty(" + contextType
            + "MessageContext.INBOUND_MESSAGE_ATTACHMENTS)=key[1] - an unexpected attachment was found");
        pass = false;
      }
      if (JAXWS_Util.looseIndexOf(calls, "InboundClient" + contextType
          + "Handler2.MessageContext.getProperty(MessageContext.INBOUND_MESSAGE_ATTACHMENTS)=key[0]") == -1) {
        TestUtil.logErr("InboundClient" + contextType
            + "Handler2.MessageContext.getProperty(MessageContext.INBOUND_MESSAGE_ATTACHMENTS)=key[0] - expected attachment was not found");
        pass = false;
      }
      if (JAXWS_Util.looseIndexOf(calls, "InboundClient" + contextType
          + "Handler2.MessageContext.getProperty(MessageContext.INBOUND_MESSAGE_ATTACHMENTS)=key[1]") != -1) {
        TestUtil.logErr("InboundClient" + contextType
            + "Handler2.MessageContext.getProperty(MessageContext.INBOUND_MESSAGE_ATTACHMENTS)=key[1] - an unexpected attachment was found");
        pass = false;
      }

      if (calls.indexOf(
          "InboundClient" + contextType + "Handler2.MessageContext.getProperty("
              + contextType + "MessageContext.HTTP_RESPONSE_CODE)=200") == -1) {
        TestUtil.logErr("InboundClient" + contextType
            + "Handler2.MessageContext.getProperty(" + contextType
            + "MessageContext.HTTP_RESPONSE_CODE)=200 was not found");
        pass = false;
      }
      if (calls.indexOf("InboundClient" + contextType
          + "Handler2.MessageContext.getProperty(MessageContext.HTTP_RESPONSE_CODE)=200") == -1) {
        TestUtil.logErr("InboundClient" + contextType
            + "Handler2.MessageContext.getProperty(MessageContext.HTTP_RESPONSE_CODE)=200 was not found");
        pass = false;
      }
      if (calls.indexOf("InboundClient" + contextType
          + "Handler2.MessageContext.getProperty(" + contextType
          + "MessageContext.HTTP_REQUEST_HEADERS)=null") == -1) {
        TestUtil.logErr("InboundClient" + contextType
            + "Handler2.MessageContext.getProperty(" + contextType
            + "MessageContext.HTTP_REQUEST_HEADERS)=null was not found");
        pass = false;
      }
      if (calls.indexOf("InboundClient" + contextType
          + "Handler2.MessageContext.getProperty(MessageContext.HTTP_REQUEST_HEADERS)=null") == -1) {
        TestUtil.logErr("InboundClient" + contextType
            + "Handler2.MessageContext.getProperty(MessageContext.HTTP_REQUEST_HEADERS)=null was not found");
        pass = false;
      }
      if (calls.indexOf("InboundClient" + contextType
          + "Handler2.MessageContext.getProperty(" + contextType
          + "MessageContext.HTTP_RESPONSE_HEADERS)=null") != -1) {
        TestUtil.logErr("InboundClient" + contextType
            + "Handler2.MessageContext.getProperty(" + contextType
            + "MessageContext.HTTP_RESPONSE_HEADERS)=null was found");
        pass = false;
      }
      if (calls.indexOf("InboundClient" + contextType
          + "Handler2.MessageContext.getProperty(MessageContext.HTTP_RESPONSE_HEADERS)=null") != -1) {
        TestUtil.logErr("InboundClient" + contextType
            + "Handler2.MessageContext.getProperty(MessageContext.HTTP_RESPONSE_HEADERS)=null was found");
        pass = false;
      }
      if (calls.indexOf(
          "InboundClient" + contextType + "Handler2.MessageContext.getProperty("
              + contextType + "MessageContext.SERVLET_REQUEST)=null") == -1) {
        TestUtil.logErr("InboundClient" + contextType
            + "Handler2.MessageContext.getProperty(" + contextType
            + "MessageContext.SERVLET_REQUEST)=null was not found");
        pass = false;
      }
      if (calls.indexOf("InboundClient" + contextType
          + "Handler2.MessageContext.getProperty(MessageContext.SERVLET_REQUEST)=null") == -1) {
        TestUtil.logErr("InboundClient" + contextType
            + "Handler2.MessageContext.getProperty(MessageContext.SERVLET_REQUEST)=null was not found");
        pass = false;
      }
      if (calls.indexOf(
          "InboundClient" + contextType + "Handler2.MessageContext.getProperty("
              + contextType + "MessageContext.SERVLET_RESPONSE)=null") == -1) {
        TestUtil.logErr("InboundClient" + contextType
            + "Handler2.MessageContext.getProperty(" + contextType
            + "MessageContext.SERVLET_RESPONSE)=null was not found");
        pass = false;
      }
      if (calls.indexOf("InboundClient" + contextType
          + "Handler2.MessageContext.getProperty(MessageContext.SERVLET_RESPONSE)=null") == -1) {
        TestUtil.logErr("InboundClient" + contextType
            + "Handler2.MessageContext.getProperty(MessageContext.SERVLET_RESPONSE)=null was not found");
        pass = false;
      }
      if (calls.indexOf(
          "InboundClient" + contextType + "Handler2.MessageContext.getProperty("
              + contextType + "MessageContext.SERVLET_CONTEXT)=null") == -1) {
        TestUtil.logErr("InboundClient" + contextType
            + "Handler2.MessageContext.getProperty(" + contextType
            + "MessageContext.SERVLET_CONTEXT)=null was not found");
        pass = false;
      }
      if (calls.indexOf("InboundClient" + contextType
          + "Handler2.MessageContext.getProperty(MessageContext.SERVLET_CONTEXT)=null") == -1) {
        TestUtil.logErr("InboundClient" + contextType
            + "Handler2.MessageContext.getProperty(MessageContext.SERVLET_CONTEXT)=null was not found");
        pass = false;
      }

    } else {
      // server
      if (JAXWS_Util.looseIndexOf(calls,
          "InboundServer" + contextType + "Handler2.MessageContext.getProperty("
              + contextType
              + "MessageContext.INBOUND_MESSAGE_ATTACHMENTS)=key[0]") == -1) {
        TestUtil.logErr("InboundServer" + contextType
            + "Handler2.MessageContext.getProperty(" + contextType
            + "MessageContext.INBOUND_MESSAGE_ATTACHMENTS)=key[0] - expected attachment was not found");
        pass = false;
      }
      if (JAXWS_Util.looseIndexOf(calls,
          "InboundServer" + contextType + "Handler2.MessageContext.getProperty("
              + contextType
              + "MessageContext.INBOUND_MESSAGE_ATTACHMENTS)=key[1]") != -1) {
        TestUtil.logErr("InboundServer" + contextType
            + "Handler2.MessageContext.getProperty(" + contextType
            + "MessageContext.INBOUND_MESSAGE_ATTACHMENTS)=key[1] - an unexpected attachment was found");
        pass = false;
      }
      if (JAXWS_Util.looseIndexOf(calls, "InboundServer" + contextType
          + "Handler2.MessageContext.getProperty(MessageContext.INBOUND_MESSAGE_ATTACHMENTS)=key[0]") == -1) {
        TestUtil.logErr("InboundServer" + contextType
            + "Handler2.MessageContext.getProperty(MessageContext.INBOUND_MESSAGE_ATTACHMENTS)=key[0] - expected attachment was not found");
        pass = false;
      }
      if (JAXWS_Util.looseIndexOf(calls, "InboundServer" + contextType
          + "Handler2.MessageContext.getProperty(MessageContext.INBOUND_MESSAGE_ATTACHMENTS)=key[1]") != -1) {
        TestUtil.logErr("InboundServer" + contextType
            + "Handler2.MessageContext.getProperty(MessageContext.INBOUND_MESSAGE_ATTACHMENTS)=key[1] - an unexpected attachment was found");
        pass = false;
      }

      if (calls.indexOf("InboundServer" + contextType
          + "Handler2.MessageContext.getProperty(" + contextType
          + "MessageContext.HTTP_REQUEST_METHOD)=POST") == -1) {
        TestUtil.logErr("InboundServer" + contextType
            + "Handler2.MessageContext.getProperty(" + contextType
            + "MessageContext.HTTP_REQUEST_METHOD)=POST was not found");
        pass = false;
      }
      if (calls.indexOf("InboundServer" + contextType
          + "Handler2.MessageContext.getProperty(MessageContext.HTTP_REQUEST_METHOD)=POST") == -1) {
        TestUtil.logErr("InboundServer" + contextType
            + "Handler2.MessageContext.getProperty(MessageContext.HTTP_REQUEST_METHOD)=POST was not found");
        pass = false;
      }
      if (calls.indexOf("InboundServer" + contextType
          + "Handler2.MessageContext.getProperty(" + contextType
          + "MessageContext.HTTP_REQUEST_HEADERS)=null") != -1) {
        TestUtil.logErr("InboundServer" + contextType
            + "Handler2.MessageContext.getProperty(" + contextType
            + "MessageContext.HTTP_REQUEST_HEADERS)=null was found");
        pass = false;
      }
      if (calls.indexOf("InboundServer" + contextType
          + "Handler2.MessageContext.getProperty(MessageContext.HTTP_REQUEST_HEADERS)=null") != -1) {
        TestUtil.logErr("InboundServer" + contextType
            + "Handler2.MessageContext.getProperty(MessageContext.HTTP_REQUEST_HEADERS)=null was found");
        pass = false;
      }
      if (calls.indexOf("InboundServer" + contextType
          + "Handler2.MessageContext.getProperty(" + contextType
          + "MessageContext.HTTP_RESPONSE_HEADERS)=null") == -1) {
        TestUtil.logErr("InboundServer" + contextType
            + "Handler2.MessageContext.getProperty(" + contextType
            + "MessageContext.HTTP_RESPONSE_HEADERS)=null was not found");
        pass = false;
      }
      if (calls.indexOf("InboundServer" + contextType
          + "Handler2.MessageContext.getProperty(MessageContext.HTTP_RESPONSE_HEADERS)=null") == -1) {
        TestUtil.logErr("InboundServer" + contextType
            + "Handler2.MessageContext.getProperty(MessageContext.HTTP_RESPONSE_HEADERS)=null was not found");
        pass = false;
      }
      if (calls.indexOf(
          "InboundServer" + contextType + "Handler2.MessageContext.getProperty("
              + contextType + "MessageContext.SERVLET_REQUEST)=null") != -1) {
        TestUtil.logErr("InboundServer" + contextType
            + "Handler2.MessageContext.getProperty(" + contextType
            + "MessageContext.SERVLET_REQUEST)=null was found");
        pass = false;
      }
      if (calls.indexOf("InboundServer" + contextType
          + "Handler2.MessageContext.getProperty(MessageContext.SERVLET_REQUEST)=null") != -1) {
        TestUtil.logErr("InboundServer" + contextType
            + "Handler2.MessageContext.getProperty(MessageContext.SERVLET_REQUEST)=null was found");
        pass = false;
      }
      if (calls.indexOf(
          "InboundServer" + contextType + "Handler2.MessageContext.getProperty("
              + contextType + "MessageContext.SERVLET_RESPONSE)=null") != -1) {
        TestUtil.logErr("InboundServer" + contextType
            + "Handler2.MessageContext.getProperty(" + contextType
            + "MessageContext.SERVLET_RESPONSE)=null was found");
        pass = false;
      }
      if (calls.indexOf("InboundServer" + contextType
          + "Handler2.MessageContext.getProperty(MessageContext.SERVLET_RESPONSE)=null") != -1) {
        TestUtil.logErr("InboundServer" + contextType
            + "Handler2.MessageContext.getProperty(MessageContext.SERVLET_RESPONSE)=null was found");
        pass = false;
      }
      if (calls.indexOf(
          "InboundServer" + contextType + "Handler2.MessageContext.getProperty("
              + contextType + "MessageContext.SERVLET_CONTEXT)=null") != -1) {
        TestUtil.logErr("InboundServer" + contextType
            + "Handler2.MessageContext.getProperty(" + contextType
            + "MessageContext.SERVLET_CONTEXT)=null was found");
        pass = false;
      }
      if (calls.indexOf("InboundServer" + contextType
          + "Handler2.MessageContext.getProperty(MessageContext.SERVLET_CONTEXT)=null") != -1) {
        TestUtil.logErr("InboundServer" + contextType
            + "Handler2.MessageContext.getProperty(MessageContext.SERVLET_CONTEXT)=null was found");
        pass = false;
      }

      if (JAXWS_Util.looseIndexOf(calls,
          "OutboundServer" + contextType
              + "Handler2.MessageContext.getProperty(" + contextType
              + "MessageContext.OUTBOUND_MESSAGE_ATTACHMENTS)=key[0]") == -1) {
        TestUtil.logErr("OutboundServer" + contextType
            + "Handler2.MessageContext.getProperty(" + contextType
            + "MessageContext.OUTBOUND_MESSAGE_ATTACHMENTS)=key[0] - expected attachment was not found");
        pass = false;
      }
      if (JAXWS_Util.looseIndexOf(calls,
          "OutboundServer" + contextType
              + "Handler2.MessageContext.getProperty(" + contextType
              + "MessageContext.OUTBOUND_MESSAGE_ATTACHMENTS)=key[1]") != -1) {
        TestUtil.logErr("OutboundServer" + contextType
            + "Handler2.MessageContext.getProperty(" + contextType
            + "MessageContext.OUTBOUND_MESSAGE_ATTACHMENTS)=key[1] - an unexpected attachment was found");
        pass = false;
      }
      if (JAXWS_Util.looseIndexOf(calls, "OutboundServer" + contextType
          + "Handler2.MessageContext.getProperty(MessageContext.OUTBOUND_MESSAGE_ATTACHMENTS)=key[0]") == -1) {
        TestUtil.logErr("OutboundServer" + contextType
            + "Handler2.MessageContext.getProperty(MessageContext.OUTBOUND_MESSAGE_ATTACHMENTS)=key[0] - expected attachment was not found");
        pass = false;
      }
      if (JAXWS_Util.looseIndexOf(calls, "OutboundServer" + contextType
          + "Handler2.MessageContext.getProperty(MessageContext.OUTBOUND_MESSAGE_ATTACHMENTS)=key[1]") != -1) {
        TestUtil.logErr("OutboundServer" + contextType
            + "Handler2.MessageContext.getProperty(MessageContext.OUTBOUND_MESSAGE_ATTACHMENTS)=key[1] - an unexpected attachment was found");
        pass = false;
      }
      if (calls.indexOf("OutboundServer" + contextType
          + "Handler2.MessageContext.getProperty(" + contextType
          + "MessageContext.HTTP_REQUEST_METHOD)=POST") == -1) {
        TestUtil.logErr("OutboundServer" + contextType
            + "Handler2.MessageContext.getProperty(" + contextType
            + "MessageContext.HTTP_REQUEST_METHOD)=POST was not found");
        pass = false;
      }
      if (calls.indexOf("OutboundServer" + contextType
          + "Handler2.MessageContext.getProperty(MessageContext.HTTP_REQUEST_METHOD)=POST") == -1) {
        TestUtil.logErr("OutboundServer" + contextType
            + "Handler2.MessageContext.getProperty(MessageContext.HTTP_REQUEST_METHOD)=POST was not found");
        pass = false;
      }
      if (calls.indexOf("OutboundServer" + contextType
          + "Handler2.MessageContext.getProperty(" + contextType
          + "MessageContext.HTTP_REQUEST_HEADERS)=null") != -1) {
        TestUtil.logErr("OutboundServer" + contextType
            + "Handler2.MessageContext.getProperty(" + contextType
            + "MessageContext.HTTP_REQUEST_HEADERS)=null was found");
        pass = false;
      }
      if (calls.indexOf("OutboundServer" + contextType
          + "Handler2.MessageContext.getProperty(MessageContext.HTTP_REQUEST_HEADERS)=null") != -1) {
        TestUtil.logErr("OutboundServer" + contextType
            + "Handler2.MessageContext.getProperty(MessageContext.HTTP_REQUEST_HEADERS)=null was found");
        pass = false;
      }
      if (calls.indexOf("OutboundServer" + contextType
          + "Handler2.MessageContext.getProperty(" + contextType
          + "MessageContext.SERVLET_REQUEST)=null") != -1) {
        TestUtil.logErr("OutboundServer" + contextType
            + "Handler2.MessageContext.getProperty(" + contextType
            + "MessageContext.SERVLET_REQUEST)=null was found");
        pass = false;
      }
      if (calls.indexOf("OutboundServer" + contextType
          + "Handler2.MessageContext.getProperty(MessageContext.SERVLET_REQUEST)=null") != -1) {
        TestUtil.logErr("OutboundServer" + contextType
            + "Handler2.MessageContext.getProperty(MessageContext.SERVLET_REQUEST)=null was found");
        pass = false;
      }
      if (calls.indexOf("OutboundServer" + contextType
          + "Handler2.MessageContext.getProperty(" + contextType
          + "MessageContext.SERVLET_RESPONSE)=null") != -1) {
        TestUtil.logErr("OutboundServer" + contextType
            + "Handler2.MessageContext.getProperty(" + contextType
            + "MessageContext.SERVLET_RESPONSE)=null was found");
        pass = false;
      }
      if (calls.indexOf("OutboundServer" + contextType
          + "Handler2.MessageContext.getProperty(MessageContext.SERVLET_RESPONSE)=null") != -1) {
        TestUtil.logErr("OutboundServer" + contextType
            + "Handler2.MessageContext.getProperty(MessageContext.SERVLET_RESPONSE)=null was found");
        pass = false;
      }
      if (calls.indexOf("OutboundServer" + contextType
          + "Handler2.MessageContext.getProperty(" + contextType
          + "MessageContext.SERVLET_CONTEXT)=null") != -1) {
        TestUtil.logErr("OutboundServer" + contextType
            + "Handler2.MessageContext.getProperty(" + contextType
            + "MessageContext.SERVLET_CONTEXT)=null was found");
        pass = false;
      }
      if (calls.indexOf("OutboundServer" + contextType
          + "Handler2.MessageContext.getProperty(MessageContext.SERVLET_CONTEXT)=null") != -1) {
        TestUtil.logErr("OutboundServer" + contextType
            + "Handler2.MessageContext.getProperty(MessageContext.SERVLET_CONTEXT)=null was found");
        pass = false;
      }
    }
    return pass;
  }

  public static boolean VerifyHandleFaultFalseCallBacks(String who,
      String contextType, List<String> calls, String direction) {
    boolean pass = true;
    String packageName = null;

    if (calls == null) {
      TestUtil.logErr("Callback string is null (unexpected)");
      return false;
    }
    TestUtil.logTrace("who=" + who);
    TestUtil.logTrace("\n");

    TestUtil.logTrace("The complete list of callbacks are:");
    JAXWS_Util.dumpList(calls);
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
        if (calls.indexOf(who + contextType
            + "Handler6.handleMessage().doOutbound()") == -1) {
          TestUtil.logErr(who + contextType
              + "Handler6.handleMessage().doOutbound() was not called");
          pass = false;
        }
        if (calls.indexOf(who + contextType
            + "Handler6 Throwing an outbound SOAPFaultException") == -1) {
          TestUtil.logErr(who + contextType
              + "Handler6 Throwing an outbound SOAPFaultException was not found");
          pass = false;
        }
        if (calls.indexOf(who + contextType + "Handler4.handleFault()") == -1) {
          TestUtil.logErr(
              who + contextType + "Handler4.handleFault() was not called");
          pass = false;
        }
        if (calls.indexOf(who + contextType + "Handler5.handleFault()") > -1) {
          TestUtil
              .logErr(who + contextType + "Handler5.handleFault() was called");
          pass = false;
        }
        if (calls.indexOf(who + contextType + "Handler6.close()") == -1) {
          TestUtil
              .logErr(who + contextType + "Handler6.close() was not called");
          pass = false;
        }
        if (calls.indexOf(who + contextType + "Handler4.close()") == -1) {
          TestUtil
              .logErr(who + contextType + "Handler4.close() was not called");
          pass = false;
        }
        if (calls.indexOf(who + contextType + "Handler5.close()") == -1) {
          TestUtil
              .logErr(who + contextType + "Handler5.close() was not called");
          pass = false;
        }
      }
    } else {
      // server
      if (direction.equals(Constants.INBOUND)) {
        // inbound
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
        if (calls.indexOf(who + contextType
            + "Handler5 Throwing an inbound SOAPFaultException") == -1) {
          TestUtil.logErr(who + contextType
              + "Handler5 Throwing an inbound SOAPFaultException was not found");
          pass = false;
        }
        if (calls.indexOf(who + contextType + "Handler4.handleFault()") == -1) {
          TestUtil.logErr(
              who + contextType + "Handler4.handleFault() was not called");
          pass = false;
        }
        if (calls.indexOf(who + contextType + "Handler6.handleFault()") > -1) {
          TestUtil
              .logErr(who + contextType + "Handler4.handleFault() was called");
          pass = false;
        }
        if (calls.indexOf(who + contextType + "Handler6.close()") == -1) {
          TestUtil
              .logErr(who + contextType + "Handler6.close() was not called");
          pass = false;
        }
        if (calls.indexOf(who + contextType + "Handler4.close()") == -1) {
          TestUtil
              .logErr(who + contextType + "Handler4.close() was not called");
          pass = false;
        }
        if (calls.indexOf(who + contextType + "Handler5.close()") == -1) {
          TestUtil
              .logErr(who + contextType + "Handler5.close() was not called");
          pass = false;
        }
      }
    }
    return pass;
  }

  public static boolean VerifyHandleMessageExceptionCallBacks(String who,
      String contextType, List<String> calls, String direction) {
    boolean pass = true;
    String packageName = null;

    if (calls == null) {
      TestUtil.logErr("Callback string is null (unexpected)");
      return false;
    }
    TestUtil.logTrace("who=" + who);
    TestUtil.logTrace("\n");

    TestUtil.logTrace("The complete list of callbacks are:");
    JAXWS_Util.dumpList(calls);
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
        if (calls.indexOf(
            who + contextType + "Handler6.handleMessage().doOutbound()") > -1) {
          TestUtil.logErr(who + contextType
              + "Handler6.handleMessage().doOutbound() was  called");
          pass = false;
        }
        if (calls.indexOf(who + contextType
            + "Handler4 Throwing an outbound SOAPFaultException") == -1) {
          TestUtil.logErr(who + contextType
              + "Handler4 Throwing an outinbound SOAPFaultException was not found");
          pass = false;
        }
        if (calls.indexOf(who + contextType + "Handler4.close()") == -1) {
          TestUtil
              .logErr(who + contextType + "Handler4.close() was not called");
          pass = false;
        }
        if (calls.indexOf(who + contextType + "Handler5.close()") == -1) {
          TestUtil
              .logErr(who + contextType + "Handler5.close() was not called");
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
        if (calls.indexOf(who + contextType
            + "Handler4 Throwing an inbound SOAPFaultException") == -1) {
          TestUtil.logErr(who + contextType
              + "Handler4 Throwing an inbound SOAPFaultException was not found");
          pass = false;
        }
        if (calls.indexOf(
            who + contextType + "Handler5.handleMessage().doInbound()") > -1) {
          TestUtil.logErr(who + contextType
              + "Handler5.handleMessage().doInbound() was called");
          pass = false;
        }
        if (calls.indexOf(who + contextType + "Handler6.close()") == -1) {
          TestUtil
              .logErr(who + contextType + "Handler6.close() was not called");
          pass = false;
        }
        if (calls.indexOf(who + contextType + "Handler4.close()") == -1) {
          TestUtil
              .logErr(who + contextType + "Handler4.close() was not called");
          pass = false;
        }
        if (calls.indexOf(who + contextType + "Handler5.close()") == -1) {
          TestUtil
              .logErr(who + contextType + "Handler5.close() was not called");
          pass = false;
        }

      }
    } else {
      // server
      if (direction.equals(Constants.INBOUND)) {
        // inbound
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
        if (calls.indexOf(who + contextType
            + "Handler4 Throwing an inbound SOAPFaultException") == -1) {
          TestUtil.logErr(who + contextType
              + "Handler4 Throwing an inbound SOAPFaultException was not found");
          pass = false;
        }
        if (calls.indexOf(
            who + contextType + "Handler5.handleMessage().doInbound()") > -1) {
          TestUtil.logErr(who + contextType
              + "Handler5.handleMessage().doInbound() was called");
          pass = false;
        }
        if (calls.indexOf(who + contextType + "Handler6.close()") == -1) {
          TestUtil
              .logErr(who + contextType + "Handler6.close() was not called");
          pass = false;
        }
        if (calls.indexOf(who + contextType + "Handler4.close()") == -1) {
          TestUtil
              .logErr(who + contextType + "Handler4.close() was not called");
          pass = false;
        }
      } else {
        // outbound
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
        if (calls.indexOf(
            who + contextType + "Handler6.handleMessage().doOutbound()") > -1) {
          TestUtil.logErr(who + contextType
              + "Handler6.handleMessage().doOutbound() was called");
          pass = false;
        }
        if (calls.indexOf(who + contextType
            + "Handler4 Throwing an outbound SOAPFaultException") == -1) {
          TestUtil.logErr(who + contextType
              + "Handler4 Throwing an outbound SOAPFaultException was not found");
          pass = false;
        }
        if (calls.indexOf(who + contextType + "Handler6.close()") == -1) {
          TestUtil
              .logErr(who + contextType + "Handler6.close() was not called");
          pass = false;
        }
        if (calls.indexOf(who + contextType + "Handler4.close()") == -1) {
          TestUtil
              .logErr(who + contextType + "Handler4.close() was not called");
          pass = false;
        }
        if (calls.indexOf(who + contextType + "Handler5.close()") == -1) {
          TestUtil
              .logErr(who + contextType + "Handler5.close() was not called");
          pass = false;
        }
      }
    }
    return pass;
  }

  public static boolean VerifyHandleFaultRuntimeExceptionCallBacks(String who,
      String contextType, List<String> calls, String direction) {
    boolean pass = true;
    String packageName = null;

    if (calls == null) {
      TestUtil.logErr("Callback string is null (unexpected)");
      return false;
    }
    TestUtil.logTrace("who=" + who);
    TestUtil.logTrace("\n");

    TestUtil.logTrace("The complete list of callbacks are:");
    JAXWS_Util.dumpList(calls);
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
        if (calls.indexOf(who + contextType
            + "Handler6.handleMessage().doOutbound()") == -1) {
          TestUtil.logErr(who + contextType
              + "Handler6.handleMessage().doOutbound() was not called");
          pass = false;
        }
        if (calls.indexOf(who + contextType
            + "Handler6 Throwing an outbound SOAPFaultException") == -1) {
          TestUtil.logErr(who + contextType
              + "Handler6 Throwing an outbound SOAPFaultException was not found");
          pass = false;
        }
        if (calls.indexOf(who + contextType + "Handler4.handleFault()") == -1) {
          TestUtil.logErr(
              who + contextType + "Handler4.handleFault() was not called");
          pass = false;
        }
        if (calls.indexOf(who + contextType
            + "Handler4 Throwing an inbound RuntimeException") == -1) {
          TestUtil.logErr(who + contextType
              + "Handler4 Throwing an inbound RuntimeException was not found");
          pass = false;
        }
        if (calls.indexOf(who + contextType + "Handler6.close()") == -1) {
          TestUtil
              .logErr(who + contextType + "Handler6.close() was not called");
          pass = false;
        }
        if (calls.indexOf(who + contextType + "Handler4.close()") == -1) {
          TestUtil
              .logErr(who + contextType + "Handler4.close() was not called");
          pass = false;
        }
        if (calls.indexOf(who + contextType + "Handler5.close()") == -1) {
          TestUtil
              .logErr(who + contextType + "Handler5.close() was not called");
          pass = false;
        }
      }
    } else {
      // server
      if (direction.equals(Constants.INBOUND)) {
        // inbound
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
        if (calls.indexOf(who + contextType
            + "Handler5 Throwing an inbound SOAPFaultException") == -1) {
          TestUtil.logErr(who + contextType
              + "Handler5 Throwing an inbound SOAPFaultException was not found");
          pass = false;
        }
        if (calls.indexOf(who + contextType + "Handler4.handleFault()") == -1) {
          TestUtil.logErr(
              who + contextType + "Handler4.handleFault() was not called");
          pass = false;
        }
        if (calls.indexOf(who + contextType
            + "Handler4 Throwing an outbound RuntimeException") == -1) {
          TestUtil.logErr(who + contextType
              + "Handler4 Throwing an outbound RuntimeException was not found");
          pass = false;
        }
        if (calls.indexOf(who + contextType + "Handler6.close()") == -1) {
          TestUtil
              .logErr(who + contextType + "Handler6.close() was not called");
          pass = false;
        }
        if (calls.indexOf(who + contextType + "Handler4.close()") == -1) {
          TestUtil
              .logErr(who + contextType + "Handler4.close() was not called");
          pass = false;
        }
        if (calls.indexOf(who + contextType + "Handler5.close()") == -1) {
          TestUtil
              .logErr(who + contextType + "Handler5.close() was not called");
          pass = false;
        }
      }
    }
    return pass;
  }

  public static boolean VerifyHandleFaultSOAPFaultExceptionCallBacks(String who,
      String contextType, List<String> calls, String direction) {
    boolean pass = true;
    String packageName = null;

    if (calls == null) {
      TestUtil.logErr("Callback string is null (unexpected)");
      return false;
    }
    TestUtil.logTrace("who=" + who);
    TestUtil.logTrace("\n");

    TestUtil.logTrace("The complete list of callbacks are:");
    JAXWS_Util.dumpList(calls);
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
        if (calls.indexOf(who + contextType
            + "Handler6.handleMessage().doOutbound()") == -1) {
          TestUtil.logErr(who + contextType
              + "Handler6.handleMessage().doOutbound() was not called");
          pass = false;
        }
        if (calls.indexOf(who + contextType
            + "Handler6 Throwing an outbound SOAPFaultException") == -1) {
          TestUtil.logErr(who + contextType
              + "Handler6 Throwing an outbound SOAPFaultException was not found");
          pass = false;
        }
        if (calls.indexOf(who + contextType + "Handler4.handleFault()") == -1) {
          TestUtil.logErr(
              who + contextType + "Handler4.handleFault() was not called");
          pass = false;
        }
        if (calls.indexOf(who + contextType
            + "Handler4 Throwing an inbound SOAPFaultException") == -1) {
          TestUtil.logErr(who + contextType
              + "Handler4 Throwing an inbound SOAPFaultException was not found");
          pass = false;
        }
        if (calls.indexOf(who + contextType + "Handler6.close()") == -1) {
          TestUtil
              .logErr(who + contextType + "Handler6.close() was not called");
          pass = false;
        }
        if (calls.indexOf(who + contextType + "Handler4.close()") == -1) {
          TestUtil
              .logErr(who + contextType + "Handler4.close() was not called");
          pass = false;
        }
        if (calls.indexOf(who + contextType + "Handler5.close()") == -1) {
          TestUtil
              .logErr(who + contextType + "Handler5.close() was not called");
          pass = false;
        }
      }
    } else {
      // server
      if (direction.equals(Constants.INBOUND)) {
        // inbound
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
        if (calls.indexOf(who + contextType
            + "Handler5 Throwing an inbound SOAPFaultException") == -1) {
          TestUtil.logErr(who + contextType
              + "Handler5 Throwing an inbound SOAPFaultException was not found");
          pass = false;
        }
        if (calls.indexOf(who + contextType + "Handler4.handleFault()") == -1) {
          TestUtil.logErr(
              who + contextType + "Handler4.handleFault() was not called");
          pass = false;
        }
        if (calls.indexOf(who + contextType
            + "Handler4 Throwing an outbound SOAPFaultException") == -1) {
          TestUtil.logErr(who + contextType
              + "Handler4 Throwing an outbound SOAPFaultException was not found");
          pass = false;
        }
        if (calls.indexOf(who + contextType + "Handler6.close()") == -1) {
          TestUtil
              .logErr(who + contextType + "Handler6.close() was not called");
          pass = false;
        }
        if (calls.indexOf(who + contextType + "Handler4.close()") == -1) {
          TestUtil
              .logErr(who + contextType + "Handler4.close() was not called");
          pass = false;
        }
        if (calls.indexOf(who + contextType + "Handler5.close()") == -1) {
          TestUtil
              .logErr(who + contextType + "Handler5.close() was not called");
          pass = false;
        }
      }
    }
    return pass;
  }

  public static boolean VerifyLogicalVerseSOAPHandlerOrder(List<String> calls) {
    boolean pass = true;

    TestUtil.logTrace("The complete list of callbacks are the following:");
    JAXWS_Util.dumpList(calls);

    TestUtil.logTrace(
        "Getting just the handleMessage().doOutbound/doInbound messages");
    List<String> inHandlers = new Vector<String>();
    List<String> outHandlers = new Vector<String>();
    TestUtil.logTrace("\n");
    TestUtil
        .logTrace("The list of doOutbound/doInbound callbacks are as follows:");
    if (!calls.isEmpty()) {
      Iterator i = calls.iterator();
      while (i.hasNext()) {
        Object o = i.next();
        if (o != null) {
          if (o instanceof String) {
            String item = (String) o;
            if (item.indexOf("handleMessage().doInbound") > -1) {
              TestUtil.logTrace(item);
              inHandlers.add(item);
            } else if (item.indexOf("handleMessage().doOutbound") > -1) {
              TestUtil.logTrace(item);
              outHandlers.add(item);
            }
          } else {
            TestUtil.logErr(
                "An unexpected object was returned while iterating through");
            TestUtil.logErr("the list of callbacks, expected String got:" + o);
          }
        } else {
          TestUtil.logErr("A null object was returned while iterating through");
          TestUtil.logErr("the list of callbacks");
        }
      }
    }

    if (outHandlers.size() > 0) {
      TestUtil.logTrace("The list of doOutbound callbacks are as follows:");
      JAXWS_Util.dumpList(outHandlers);

      int lastLogical = JAXWS_Util.looseLastIndexOf(outHandlers,
          "LogicalHandler");
      int firstSOAP = JAXWS_Util.looseIndexOf(outHandlers, "SOAPHandler");
      TestUtil.logTrace("lastLogical=" + lastLogical);
      TestUtil.logTrace("firstSOAP=" + firstSOAP);
      if ((lastLogical == -1) || (firstSOAP == -1)) {
        TestUtil.logErr(
            "Both Logical and SOAP request handlers were not in the Callback string");
        pass = false;
      }

      if (lastLogical > firstSOAP) {
        TestUtil.logErr(
            "The first SOAPHandler (" + outHandlers.get(firstSOAP) + ")");
        TestUtil.logErr("for an outbound message did not occur after the");
        TestUtil.logErr(
            "last LogicalHandler (" + outHandlers.get(lastLogical) + ")");
        pass = false;
      }
    } else {
      TestUtil.logErr("The list of handleMessage() callbacks was empty");
      pass = false;
    }

    if (inHandlers.size() > 0) {
      TestUtil.logTrace("The list of doInbound callbacks are as follows:");
      JAXWS_Util.dumpList(inHandlers);

      int lastSOAP = JAXWS_Util.looseLastIndexOf(inHandlers, "SOAPHandler");
      int firstLogical = JAXWS_Util.looseIndexOf(inHandlers, "LogicalHandler");
      TestUtil.logTrace("lastSOAP=" + lastSOAP);
      TestUtil.logTrace("firstLogical=" + firstLogical);
      if ((lastSOAP == -1) || (firstLogical == -1)) {
        TestUtil.logErr(
            "Both Logical and SOAP response handlers were not in the Callback string");
        pass = false;
      }
      if (lastSOAP > firstLogical) {
        TestUtil.logErr(
            "The first LogicalHandler (" + inHandlers.get(firstLogical) + ")");
        TestUtil.logErr("for an inbound message did not occur after the");
        TestUtil.logErr("last SOAPHandler (" + inHandlers.get(lastSOAP) + ")");
        pass = false;
      }
    } else {
      TestUtil.logErr("The list of handleMessage() callbacks was empty");
      pass = false;
    }
    return pass;

  }

  public static boolean VerifySOAPVerseLogicalHandlerOrder(List<String> calls) {
    boolean pass = true;

    TestUtil.logTrace("The complete list of callbacks are the following:");
    JAXWS_Util.dumpList(calls);

    TestUtil.logTrace(
        "Getting just the handleMessage().doOutbound/doInbound messages");
    List<String> inHandlers = new Vector<String>();
    List<String> outHandlers = new Vector<String>();
    TestUtil.logTrace("\n");
    TestUtil
        .logTrace("The list of doOutbound/doInbound callbacks are as follows:");
    if (!calls.isEmpty()) {
      Iterator i = calls.iterator();
      while (i.hasNext()) {
        Object o = i.next();
        if (o != null) {
          if (o instanceof String) {
            String item = (String) o;
            if (item.indexOf("handleMessage().doInbound") > -1) {
              TestUtil.logTrace(item);
              inHandlers.add(item);
            } else if (item.indexOf("handleMessage().doOutbound") > -1) {
              TestUtil.logTrace(item);
              outHandlers.add(item);
            }
          } else {
            TestUtil.logErr(
                "An unexpected object was returned while iterating through");
            TestUtil.logErr("the list of callbacks, expected String got:" + o);
          }
        } else {
          TestUtil.logErr("A null object was returned while iterating through");
          TestUtil.logErr("the list of callbacks");
        }
      }
    }

    if (outHandlers.size() > 0) {
      TestUtil.logTrace("The list of doOutbound callbacks are as follows:");
      JAXWS_Util.dumpList(outHandlers);

      int lastLogical = JAXWS_Util.looseLastIndexOf(outHandlers,
          "LogicalHandler");
      int firstSOAP = JAXWS_Util.looseIndexOf(outHandlers, "SOAPHandler");
      TestUtil.logTrace("lastLogical=" + lastLogical);
      TestUtil.logTrace("firstSOAP=" + firstSOAP);
      if ((lastLogical == -1) || (firstSOAP == -1)) {
        TestUtil.logErr(
            "Both Logical and SOAP request handlers were not in the Callback string");
        pass = false;
      }

      if (lastLogical > firstSOAP) {
        TestUtil.logErr(
            "The first SOAPHandler (" + outHandlers.get(firstSOAP) + ")");
        TestUtil.logErr("for an outbound message did not occur after the");
        TestUtil.logErr(
            "last LogicalHandler (" + outHandlers.get(lastLogical) + ")");
        pass = false;
      }
    } else {
      TestUtil.logErr("The list of handleMessage() callbacks was empty");
      pass = false;
    }

    if (inHandlers.size() > 0) {
      TestUtil.logTrace("The list of doInbound callbacks are as follows:");
      JAXWS_Util.dumpList(inHandlers);

      int lastSOAP = JAXWS_Util.looseLastIndexOf(inHandlers, "SOAPHandler");
      int firstLogical = JAXWS_Util.looseIndexOf(inHandlers, "LogicalHandler");
      TestUtil.logTrace("lastSOAP=" + lastSOAP);
      TestUtil.logTrace("firstLogical=" + firstLogical);
      if ((lastSOAP == -1) || (firstLogical == -1)) {
        TestUtil.logErr(
            "Both Logical and SOAP response handlers were not in the Callback string");
        pass = false;
      }
      if (lastSOAP > firstLogical) {
        TestUtil.logErr(
            "The first LogicalHandler (" + inHandlers.get(firstLogical) + ")");
        TestUtil.logErr("for an inbound message did not occur after the");
        TestUtil.logErr("last SOAPHandler (" + inHandlers.get(lastSOAP) + ")");
        pass = false;
      }
    } else {
      TestUtil.logErr("The list of handleMessage() callbacks was empty");
      pass = false;
    }
    return pass;

  }

  public static boolean VerifyServerToClientHandlerExceptionCallBacks(
      String who, String contextType, String direction, List<String> calls) {
    boolean pass = true;

    if (calls == null) {
      TestUtil.logErr("Callback string is null (unexpected)");
      return false;
    }
    TestUtil.logTrace("who=" + who);
    TestUtil.logTrace("contextType=" + contextType);
    TestUtil.logTrace("direction=" + direction);
    TestUtil.logTrace("\n");

    TestUtil.logTrace("The complete list of callbacks are:");
    JAXWS_Util.dumpList(calls);

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
        if (calls.indexOf(who + contextType
            + "Handler6.handleMessage().doOutbound()") == -1) {
          TestUtil.logErr(who + contextType
              + "Handler6.handleMessage().doOutbound() was not called");
          pass = false;
        }
        if (calls.indexOf(who + contextType + "Handler6.handleFault()") == -1) {
          TestUtil.logErr(
              who + contextType + "Handler6.handleFault() was not called");
          pass = false;
        }
        if (calls.indexOf(who + contextType
            + "Handler6 received SOAPFault from Inbound Server" + contextType
            + "Handler6") == -1) {
          TestUtil.logErr(who + contextType
              + "Handler6 received SOAPFault from Inbound Server" + contextType
              + "Handler6 was not found");
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
        if (calls.indexOf(who + contextType + "Handler6.handleFault()") == -1) {
          TestUtil.logErr(
              who + contextType + "Handler6.handleFault() was not called");
          pass = false;
        }
        if (calls.indexOf(who + contextType
            + "Handler6 received SOAPFault from Outbound Server" + contextType
            + "Handler6") == -1) {
          TestUtil.logErr(who + contextType
              + "Handler6 received SOAPFault from Outbound Server" + contextType
              + "Handler6 was not found");
          pass = false;
        }
      }
      if (!VerifyCloseCallBacks(who, calls, contextType, true,
          Constants.INBOUND)) {
        pass = false;
      }
    } else {
      // server
      if (direction.equals(Constants.INBOUND)) {
        if (calls.indexOf(
            who + contextType + "Handler6.handleMessage().doInbound()") == -1) {
          TestUtil.logErr(who + contextType
              + "Handler6.handleMessage().doInbound() was not called");
          pass = false;
        }
        if (calls.indexOf(who + contextType
            + "Handler6 Throwing an inbound SOAPFaultException") == -1) {
          TestUtil.logErr(who + contextType
              + "Handler6 Throwing an inbound SOAPFaultException was not called");
          pass = false;
        }
        if (calls.indexOf(who + contextType + "Handler6.close()") == -1) {
          TestUtil
              .logErr(who + contextType + "Handler6.close() was not called");
          pass = false;
        }
      } else {
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
        if (calls.indexOf(who + contextType
            + "Handler6 Throwing an outbound SOAPFaultException") == -1) {
          TestUtil.logErr(who + contextType
              + "Handler6 Throwing an outbound SOAPFaultException was not called");
          pass = false;
        }
        if (!VerifyCloseCallBacks(who, calls, contextType, true,
            Constants.OUTBOUND)) {
          pass = false;
        }
      }
    }
    return pass;
  }

  public static boolean VerifyOneWayCallbacks(String who, String direction,
      List<String> calls) {
    boolean pass = true;

    if (calls == null) {
      TestUtil.logErr("Callback string is null (unexpected)");
      return false;
    }
    TestUtil.logTrace("who=" + who);
    TestUtil.logTrace("direction=" + direction);
    TestUtil.logTrace("\n");

    TestUtil.logTrace("The complete list of callbacks are:");
    JAXWS_Util.dumpList(calls);

    if (who.equals("Client")) {
      if (direction.equals(Constants.OUTBOUND)) {
        if (calls.indexOf(
            who + "LogicalHandler1.handleMessage().doOutbound()") == -1) {
          TestUtil.logErr(who
              + "LogicalHandler1.handleMessage().doOutbound() was not called");
          pass = false;
        }
        if (calls.indexOf(
            who + "LogicalHandler2.handleMessage().doOutbound()") == -1) {
          TestUtil.logErr(who
              + "LogicalHandler2.handleMessage().doOutbound() was not called");
          pass = false;
        }
        if (calls.indexOf(
            who + "LogicalHandler3.handleMessage().doOutbound()") == -1) {
          TestUtil.logErr(who
              + "LogicalHandler3.handleMessage().doOutbound() was not called");
          pass = false;
        }
        if (calls
            .indexOf(who + "SOAPHandler1.handleMessage().doOutbound()") == -1) {
          TestUtil.logErr(
              who + "SOAPHandler1.handleMessage().doOutbound() was not called");
          pass = false;
        }
        if (calls
            .indexOf(who + "SOAPHandler2.handleMessage().doOutbound()") == -1) {
          TestUtil.logErr(
              who + "SOAPHandler2.handleMessage().doOutbound() was not called");
          pass = false;
        }
        if (calls
            .indexOf(who + "SOAPHandler3.handleMessage().doOutbound()") == -1) {
          TestUtil.logErr(
              who + "SOAPHandler3.handleMessage().doOutbound() was not called");
          pass = false;
        }
        if (calls.indexOf(who + "SOAPHandler3.close()") == -1) {
          TestUtil.logErr(who + "SOAPHandler3.close() was not called");
          pass = false;
        }
        if (calls.indexOf(who + "SOAPHandler2.close()") == -1) {
          TestUtil.logErr(who + "SOAPHandler2.close() was not called");
          pass = false;
        }
        if (calls.indexOf(who + "SOAPHandler1.close()") == -1) {
          TestUtil.logErr(who + "SOAPHandler1.close() was not called");
          pass = false;
        }
        if (calls.indexOf(who + "LogicalHandler3.close()") == -1) {
          TestUtil.logErr(who + "LogicalHandler3.close() was not called");
          pass = false;
        }
        if (calls.indexOf(who + "LogicalHandler2.close()") == -1) {
          TestUtil.logErr(who + "LogicalHandler2.close() was not called");
          pass = false;
        }
        if (calls.indexOf(who + "LogicalHandler1.close()") == -1) {
          TestUtil.logErr(who + "LogicalHandler1.close() was not called");
          pass = false;
        }

      }
    } else {
      // server
      if (direction.equals(Constants.INBOUND)) {
        if (calls
            .indexOf(who + "SOAPHandler3.handleMessage().doInbound()") == -1) {
          TestUtil.logErr(
              who + "SOAPHandler3.handleMessage().doInbound() was not called");
          pass = false;
        }
        if (calls
            .indexOf(who + "SOAPHandler2.handleMessage().doInbound()") == -1) {
          TestUtil.logErr(
              who + "SOAPHandler2.handleMessage().doInbound() was not called");
          pass = false;
        }
        if (calls
            .indexOf(who + "SOAPHandler1.handleMessage().doInbound()") == -1) {
          TestUtil.logErr(
              who + "SOAPHandler1.handleMessage().doInbound() was not called");
          pass = false;
        }
        if (calls.indexOf(
            who + "LogicalHandler3.handleMessage().doInbound()") == -1) {
          TestUtil.logErr(who
              + "LogicalHandler3.handleMessage().doInbound() was not called");
          pass = false;
        }
        if (calls.indexOf(
            who + "LogicalHandler2.handleMessage().doInbound()") == -1) {
          TestUtil.logErr(who
              + "LogicalHandler2.handleMessage().doInbound() was not called");
          pass = false;
        }
        if (calls.indexOf(
            who + "LogicalHandler1.handleMessage().doInbound()") == -1) {
          TestUtil.logErr(who
              + "LogicalHandler1.handleMessage().doInbound() was not called");
          pass = false;
        }
        if (calls.indexOf(who + "LogicalHandler1.close()") == -1) {
          TestUtil.logErr(who + "LogicalHandler1.close() was not called");
          pass = false;
        }
        if (calls.indexOf(who + "LogicalHandler2.close()") == -1) {
          TestUtil.logErr(who + "LogicalHandler2.close() was not called");
          pass = false;
        }
        if (calls.indexOf(who + "LogicalHandler3.close()") == -1) {
          TestUtil.logErr(who + "LogicalHandler3.close() was not called");
          pass = false;
        }
        if (calls.indexOf(who + "SOAPHandler1.close()") == -1) {
          TestUtil.logErr(who + "SOAPHandler1.close() was not called");
          pass = false;
        }
        if (calls.indexOf(who + "SOAPHandler2.close()") == -1) {
          TestUtil.logErr(who + "SOAPHandler2.close() was not called");
          pass = false;
        }
        if (calls.indexOf(who + "SOAPHandler3.close()") == -1) {
          TestUtil.logErr(who + "SOAPHandler3.close() was not called");
          pass = false;
        }
      }
    }
    return pass;
  }

  public static boolean VerifyOneWaySOAPFaultCallbacks(String who,
      String direction, String contextType, List<String> calls) {
    boolean pass = true;

    if (calls == null) {
      TestUtil.logErr("Callback string is null (unexpected)");
      return false;
    }
    TestUtil.logTrace("who=" + who);
    TestUtil.logTrace("direction=" + direction);
    TestUtil.logTrace("\n");

    TestUtil.logTrace("The complete list of callbacks are:");
    JAXWS_Util.dumpList(calls);

    if (who.equals("Client")) {
      if (direction.equals(Constants.OUTBOUND)) {
        if (calls.indexOf(
            who + "LogicalHandler1.handleMessage().doOutbound()") == -1) {
          TestUtil.logErr(who
              + "LogicalHandler1.handleMessage().doOutbound() was not called");
          pass = false;
        }
        if (calls.indexOf(
            who + "LogicalHandler2.handleMessage().doOutbound()") == -1) {
          TestUtil.logErr(who
              + "LogicalHandler2.handleMessage().doOutbound() was not called");
          pass = false;
        }
        if (contextType.equals("SOAP")) {
          if (calls.indexOf(
              who + "LogicalHandler3.handleMessage().doOutbound()") == -1) {
            TestUtil.logErr(who
                + "LogicalHandler3.handleMessage().doOutbound() was not called");
            pass = false;
          }
          if (calls.indexOf(
              who + "SOAPHandler1.handleMessage().doOutbound()") == -1) {
            TestUtil.logErr(who
                + "SOAPHandler1.handleMessage().doOutbound() was not called");
            pass = false;
          }
          if (calls.indexOf(
              who + "SOAPHandler2.handleMessage().doOutbound()") == -1) {
            TestUtil.logErr(who
                + "SOAPHandler2.handleMessage().doOutbound() was not called");
            pass = false;
          }
          if (calls.indexOf(who
              + "SOAPHandler2 Throwing an outbound SOAPFaultException") == -1) {
            TestUtil.logErr(who
                + "SOAPHandler2 Throwing an outbound SOAPFaultException was not called");
            pass = false;
          }
          if (calls.indexOf(who + "SOAPHandler2.close()") == -1) {
            TestUtil.logErr(who + "SOAPHandler2.close() was not called");
            pass = false;
          }
          if (calls.indexOf(who + "SOAPHandler1.close()") == -1) {
            TestUtil.logErr(who + "SOAPHandler1.close() was not called");
            pass = false;
          }
          if (calls.indexOf(who + "LogicalHandler3.close()") == -1) {
            TestUtil.logErr(who + "LogicalHandler3.close() was not called");
            pass = false;
          }
        } else {
          if (calls.indexOf(who
              + "LogicalHandler2 Throwing an outbound SOAPFaultException") == -1) {
            TestUtil.logErr(who
                + "LogicalHandler2 Throwing an outbound SOAPFaultException was not called");
            pass = false;
          }
        }
        if (calls.indexOf(who + "LogicalHandler2.close()") == -1) {
          TestUtil.logErr(who + "LogicalHandler2.close() was not called");
          pass = false;
        }
        if (calls.indexOf(who + "LogicalHandler1.close()") == -1) {
          TestUtil.logErr(who + "LogicalHandler1.close() was not called");
          pass = false;
        }

      }
    } else {
      // server
      if (direction.equals(Constants.INBOUND)) {
        if (calls
            .indexOf(who + "SOAPHandler3.handleMessage().doInbound()") == -1) {
          TestUtil.logErr(
              who + "SOAPHandler3.handleMessage().doInbound() was not called");
          pass = false;
        }
        if (calls
            .indexOf(who + "SOAPHandler2.handleMessage().doInbound()") == -1) {
          TestUtil.logErr(
              who + "SOAPHandler2.handleMessage().doInbound() was not called");
          pass = false;
        }
        if (!contextType.equals("SOAP")) {
          if (calls.indexOf(
              who + "SOAPHandler1.handleMessage().doInbound()") == -1) {
            TestUtil.logErr(who
                + "SOAPHandler1.handleMessage().doInbound() was not called");
            pass = false;
          }
          if (calls.indexOf(
              who + "LogicalHandler3.handleMessage().doInbound()") == -1) {
            TestUtil.logErr(who
                + "LogicalHandler3.handleMessage().doInbound() was not called");
            pass = false;
          }
          if (calls.indexOf(
              who + "LogicalHandler2.handleMessage().doInbound()") == -1) {
            TestUtil.logErr(who
                + "LogicalHandler2.handleMessage().doInbound() was not called");
            pass = false;
          }
          if (calls.indexOf(who
              + "LogicalHandler2 Throwing an inbound SOAPFaultException") == -1) {
            TestUtil.logErr(who
                + "LogicalHandler2 Throwing an inbound SOAPFaultException was not called");
            pass = false;

          }
          if (calls.indexOf(who + "LogicalHandler2.close()") == -1) {
            TestUtil.logErr(who + "LogicalHandler2.close() was not called");
            pass = false;
          }
          if (calls.indexOf(who + "LogicalHandler3.close()") == -1) {
            TestUtil.logErr(who + "LogicalHandler3.close() was not called");
            pass = false;
          }
          if (calls.indexOf(who + "SOAPHandler1.close()") == -1) {
            TestUtil.logErr(who + "SOAPHandler1.close() was not called");
            pass = false;
          }
        } else {
          if (calls.indexOf(who
              + "SOAPHandler2 Throwing an inbound SOAPFaultException") == -1) {
            TestUtil.logErr(who
                + "SOAPHandler2 Throwing an inbound SOAPFaultException was not called");
            pass = false;
          }
        }
        if (calls.indexOf(who + "SOAPHandler2.close()") == -1) {
          TestUtil.logErr(who + "SOAPHandler2.close() was not called");
          pass = false;
        }
        if (calls.indexOf(who + "SOAPHandler3.close()") == -1) {
          TestUtil.logErr(who + "SOAPHandler3.close() was not called");
          pass = false;
        }
      }
    }
    return pass;
  }

  public static boolean VerifyOneWayHandleMessageFalseCallbacks(String who,
      String direction, String contextType, List<String> calls) {
    boolean pass = true;

    if (calls == null) {
      TestUtil.logErr("Callback string is null (unexpected)");
      return false;
    }
    TestUtil.logTrace("who=" + who);
    TestUtil.logTrace("direction=" + direction);
    TestUtil.logTrace("\n");

    TestUtil.logTrace("The complete list of callbacks are:");
    JAXWS_Util.dumpList(calls);

    if (who.equals("Client")) {
      if (direction.equals(Constants.OUTBOUND)) {
        if (calls.indexOf(
            who + "LogicalHandler1.handleMessage().doOutbound()") == -1) {
          TestUtil.logErr(who
              + "LogicalHandler1.handleMessage().doOutbound() was not called");
          pass = false;
        }
        if (calls.indexOf(
            who + "LogicalHandler2.handleMessage().doOutbound()") == -1) {
          TestUtil.logErr(who
              + "LogicalHandler2.handleMessage().doOutbound() was not called");
          pass = false;
        }
        if (contextType.equals("SOAP")) {
          if (calls.indexOf(
              who + "LogicalHandler3.handleMessage().doOutbound()") == -1) {
            TestUtil.logErr(who
                + "LogicalHandler3.handleMessage().doOutbound() was not called");
            pass = false;
          }
          if (calls.indexOf(
              who + "SOAPHandler1.handleMessage().doOutbound()") == -1) {
            TestUtil.logErr(who
                + "SOAPHandler1.handleMessage().doOutbound() was not called");
            pass = false;
          }
          if (calls.indexOf(
              who + "SOAPHandler2.handleMessage().doOutbound()") == -1) {
            TestUtil.logErr(who
                + "SOAPHandler2.handleMessage().doOutbound() was not called");
            pass = false;
          }
          if (calls.indexOf(
              who + "SOAPHandler2 HandleMessage returns false") == -1) {
            TestUtil.logErr(who
                + "SOAPHandler2 HandleMessage returns false was not called");
            pass = false;
          }
          if (calls.indexOf(who + "SOAPHandler2.close()") == -1) {
            TestUtil.logErr(who + "SOAPHandler2.close() was not called");
            pass = false;
          }
          if (calls.indexOf(who + "SOAPHandler1.close()") == -1) {
            TestUtil.logErr(who + "SOAPHandler1.close() was not called");
            pass = false;
          }
          if (calls.indexOf(who + "LogicalHandler3.close()") == -1) {
            TestUtil.logErr(who + "LogicalHandler3.close() was not called");
            pass = false;
          }
        } else {
          if (calls.indexOf(
              who + "LogicalHandler2 HandleMessage returns false") == -1) {
            TestUtil.logErr(who
                + "LogicalHandler2 HandleMessage returns false was not called");
            pass = false;
          }
        }
        if (calls.indexOf(who + "LogicalHandler2.close()") == -1) {
          TestUtil.logErr(who + "LogicalHandler2.close() was not called");
          pass = false;
        }
        if (calls.indexOf(who + "LogicalHandler1.close()") == -1) {
          TestUtil.logErr(who + "LogicalHandler1.close() was not called");
          pass = false;
        }

      }
    } else {
      // server
      if (direction.equals(Constants.INBOUND)) {
        if (calls
            .indexOf(who + "SOAPHandler3.handleMessage().doInbound()") == -1) {
          TestUtil.logErr(
              who + "SOAPHandler3.handleMessage().doInbound() was not called");
          pass = false;
        }
        if (calls
            .indexOf(who + "SOAPHandler2.handleMessage().doInbound()") == -1) {
          TestUtil.logErr(
              who + "SOAPHandler2.handleMessage().doInbound() was not called");
          pass = false;
        }
        if (!contextType.equals("SOAP")) {
          if (calls.indexOf(
              who + "SOAPHandler1.handleMessage().doInbound()") == -1) {
            TestUtil.logErr(who
                + "SOAPHandler1.handleMessage().doInbound() was not called");
            pass = false;
          }
          if (calls.indexOf(
              who + "LogicalHandler3.handleMessage().doInbound()") == -1) {
            TestUtil.logErr(who
                + "LogicalHandler3.handleMessage().doInbound() was not called");
            pass = false;
          }
          if (calls.indexOf(
              who + "LogicalHandler2.handleMessage().doInbound()") == -1) {
            TestUtil.logErr(who
                + "LogicalHandler2.handleMessage().doInbound() was not called");
            pass = false;
          }
          if (calls.indexOf(
              who + "LogicalHandler2 HandleMessage returns false") == -1) {
            TestUtil.logErr(who
                + "LogicalHandler2 HandleMessage returns false was not called");
            pass = false;

          }
          if (calls.indexOf(who + "LogicalHandler2.close()") == -1) {
            TestUtil.logErr(who + "LogicalHandler2.close() was not called");
            pass = false;
          }
          if (calls.indexOf(who + "LogicalHandler3.close()") == -1) {
            TestUtil.logErr(who + "LogicalHandler3.close() was not called");
            pass = false;
          }
          if (calls.indexOf(who + "SOAPHandler1.close()") == -1) {
            TestUtil.logErr(who + "SOAPHandler1.close() was not called");
            pass = false;
          }
        } else {
          if (calls.indexOf(
              who + "SOAPHandler2 HandleMessage returns false") == -1) {
            TestUtil.logErr(who
                + "SOAPHandler2 HandleMessage returns false was not called");
            pass = false;
          }
        }
        if (calls.indexOf(who + "SOAPHandler2.close()") == -1) {
          TestUtil.logErr(who + "SOAPHandler2.close() was not called");
          pass = false;
        }
        if (calls.indexOf(who + "SOAPHandler3.close()") == -1) {
          TestUtil.logErr(who + "SOAPHandler3.close() was not called");
          pass = false;
        }
      }
    }
    return pass;
  }

  public static boolean VerifyHandlerDoesNotGetCalled(String who,
      List<String> calls) {
    boolean pass = true;

    if (calls == null) {
      TestUtil.logErr("Callback string is null (unexpected)");
      return false;
    }
    TestUtil.logTrace("who=" + who);
    TestUtil.logTrace("\n");

    TestUtil.logTrace("The complete list of callbacks are:");
    JAXWS_Util.dumpList(calls);

    if (calls
        .indexOf(who + "LogicalHandler5.handleMessage().doOutbound()") == -1) {
      TestUtil.logErr(
          who + "LogicalHandler5.handleMessage().doOutbound() was not called");
      pass = false;
    }
    if (calls
        .indexOf(who + "LogicalHandler1.handleMessage().doOutbound()") == -1) {
      TestUtil.logErr(
          who + "LogicalHandler1.handleMessage().doOutbound() was not called");
      pass = false;
    }
    if (who.equals("Server")) {
      if (calls.indexOf(
          who + "LogicalHandler2.handleMessage().doOutbound()") == -1) {
        TestUtil.logErr(who
            + "LogicalHandler2.handleMessage().doOutbound() was not called");
        pass = false;
      }
      if (calls.indexOf(
          who + "LogicalHandler3.handleMessage().doOutbound()") == -1) {
        TestUtil.logErr(who
            + "LogicalHandler3.handleMessage().doOutbound() was not called");
        pass = false;
      }
    }
    if (calls
        .indexOf(who + "LogicalHandler6.handleMessage().doOutbound()") == -1) {
      TestUtil.logErr(
          who + "LogicalHandler6.handleMessage().doOutbound() was not called");
      pass = false;
    }
    if (calls
        .indexOf(who + "SOAPHandler5.handleMessage().doOutbound()") == -1) {
      TestUtil.logErr(
          who + "SOAPHandler5.handleMessage().doOutbound() was not called");
      pass = false;
    }
    if (calls
        .indexOf(who + "SOAPHandler1.handleMessage().doOutbound()") == -1) {
      TestUtil.logErr(
          who + "SOAPHandler1.handleMessage().doOutbound() was not called");
      pass = false;
    }
    if (who.equals("Server")) {
      if (calls
          .indexOf(who + "SOAPHandler2.handleMessage().doOutbound()") == -1) {
        TestUtil.logErr(
            who + "SOAPHandler2.handleMessage().doOutbound() was not called");
        pass = false;
      }
      if (calls
          .indexOf(who + "SOAPHandler3.handleMessage().doOutbound()") == -1) {
        TestUtil.logErr(
            who + "SOAPHandler3.handleMessage().doOutbound() was not called");
        pass = false;
      }
    }
    if (calls
        .indexOf(who + "SOAPHandler6.handleMessage().doOutbound()") == -1) {
      TestUtil.logErr(
          who + "SOAPHandler6.handleMessage().doOutbound() was not called");
      pass = false;
    }
    if (calls.indexOf(who + "SOAPHandler6.handleMessage().doInbound()") == -1) {
      TestUtil.logErr(
          who + "SOAPHandler6.handleMessage().doInbound() was not called");
      pass = false;
    }
    if (who.equals("Server")) {
      if (calls
          .indexOf(who + "SOAPHandler3.handleMessage().doInbound()") == -1) {
        TestUtil.logErr(
            who + "SOAPHandler3.handleMessage().doInbound() was not called");
        pass = false;
      }
      if (calls
          .indexOf(who + "SOAPHandler2.handleMessage().doInbound()") == -1) {
        TestUtil.logErr(
            who + "SOAPHandler2.handleMessage().doInbound() was not called");
        pass = false;
      }
    }
    if (calls.indexOf(who + "SOAPHandler1.handleMessage().doInbound()") == -1) {
      TestUtil.logErr(
          who + "SOAPHandler1.handleMessage().doInbound() was not called");
      pass = false;
    }
    if (calls.indexOf(who + "SOAPHandler5.handleMessage().doInbound()") == -1) {
      TestUtil.logErr(
          who + "SOAPHandler5.handleMessage().doInbound() was not called");
      pass = false;
    }
    if (calls
        .indexOf(who + "LogicalHandler6.handleMessage().doInbound()") == -1) {
      TestUtil.logErr(
          who + "LogicalHandler6.handleMessage().doInbound() was not called");
      pass = false;
    }
    if (who.equals("Server")) {
      if (calls
          .indexOf(who + "LogicalHandler3.handleMessage().doInbound()") == -1) {
        TestUtil.logErr(
            who + "LogicalHandler3.handleMessage().doInbound() was not called");
        pass = false;
      }
      if (calls
          .indexOf(who + "LogicalHandler2.handleMessage().doInbound()") == -1) {
        TestUtil.logErr(
            who + "LogicalHandler2.handleMessage().doInbound() was not called");
        pass = false;
      }
    }
    if (calls
        .indexOf(who + "LogicalHandler1.handleMessage().doInbound()") == -1) {
      TestUtil.logErr(
          who + "LogicalHandler1.handleMessage().doInbound() was not called");
      pass = false;
    }
    if (calls
        .indexOf(who + "LogicalHandler5.handleMessage().doInbound()") == -1) {
      TestUtil.logErr(
          who + "LogicalHandler5.handleMessage().doInbound() was not called");
      pass = false;
    }
    if (calls.indexOf(who + "SOAPHandler6.close()") == -1) {
      TestUtil.logErr(who + "SOAPHandler6.close() was not called");
      pass = false;
    }
    if (who.equals("Server")) {
      if (calls.indexOf(who + "SOAPHandler3.close()") == -1) {
        TestUtil.logErr(who + "SOAPHandler3.close() was not called");
        pass = false;
      }
      if (calls.indexOf(who + "SOAPHandler2.close()") == -1) {
        TestUtil.logErr(who + "SOAPHandler2.close() was not called");
        pass = false;
      }
    }
    if (calls.indexOf(who + "SOAPHandler1.close()") == -1) {
      TestUtil.logErr(who + "SOAPHandler1.close() was not called");
      pass = false;
    }
    if (calls.indexOf(who + "SOAPHandler5.close()") == -1) {
      TestUtil.logErr(who + "SOAPHandler5.close() was not called");
      pass = false;
    }
    if (calls.indexOf(who + "LogicalHandler6.close()") == -1) {
      TestUtil.logErr(who + "LogicalHandler6.close() was not called");
      pass = false;
    }
    if (who.equals("Server")) {
      if (calls.indexOf(who + "LogicalHandler3.close()") == -1) {
        TestUtil.logErr(who + "LogicalHandler3.close() was not called");
        pass = false;
      }
      if (calls.indexOf(who + "LogicalHandler2.close()") == -1) {
        TestUtil.logErr(who + "LogicalHandler2.close() was not called");
        pass = false;
      }
    }
    if (calls.indexOf(who + "LogicalHandler1.close()") == -1) {
      TestUtil.logErr(who + "LogicalHandler1.close() was not called");
      pass = false;
    }
    if (calls.indexOf(who + "LogicalHandler5.close()") == -1) {
      TestUtil.logErr(who + "LogicalHandler5.close() was not called");
      pass = false;
    }
    if (calls.indexOf(
        who + "SNPSNBCLogicalHandler.handleMessage().doOutbound()") > -1) {
      TestUtil.logErr(who
          + "SNPSNBCLogicalHandler.handleMessage().doOutbound() was called");
      pass = false;
    }
    if (calls.indexOf(
        who + "SNPSNBCSOAPHandler.handleMessage().doOutbound()") > -1) {
      TestUtil.logErr(
          who + "SNPSNBCSOAPHandler.handleMessage().doOutbound() was called");
      pass = false;
    }
    if (calls.indexOf(
        who + "SNPSNBCLogicalHandler.handleMessage().doInbound()") > -1) {
      TestUtil.logErr(
          who + "SNPSNBCLogicalHandler.handleMessage().doInbound() was called");
      pass = false;
    }
    if (calls
        .indexOf(who + "SNPSNBCSOAPHandler.handleMessage().doInbound()") > -1) {
      TestUtil.logErr(
          who + "SNPSNBCSOAPHandler.handleMessage().doInbound() was called");
      pass = false;
    }
    if (calls.indexOf(who + "SNPSNBCLogicalHandler.close()") > -1) {
      TestUtil.logErr(who + "SNPSNBCLogicalHandler.close() was called");
      pass = false;
    }
    if (calls.indexOf(who + "SNPSNBCSOAPHandler.close()") > -1) {
      TestUtil.logErr(who + "SNPSNBCSOAPHandler.close() was called");
      pass = false;
    }

    if (calls.indexOf(
        who + "PNPSNBCLogicalHandler.handleMessage().doOutbound()") > -1) {
      TestUtil.logErr(who
          + "PNPSNBCLogicalHandler.handleMessage().doOutbound() was called");
      pass = false;
    }
    if (calls.indexOf(
        who + "PNPSNBCSOAPHandler.handleMessage().doOutbound()") > -1) {
      TestUtil.logErr(
          who + "PNPSNBCSOAPHandler.handleMessage().doOutbound() was called");
      pass = false;
    }
    if (calls.indexOf(
        who + "PNPSNBCLogicalHandler.handleMessage().doInbound()") > -1) {
      TestUtil.logErr(
          who + "PNPSNBCLogicalHandler.handleMessage().doInbound() was called");
      pass = false;
    }
    if (calls
        .indexOf(who + "PNPSNBCSOAPHandler.handleMessage().doInbound()") > -1) {
      TestUtil.logErr(
          who + "PNPSNBCSOAPHandler.handleMessage().doInbound() was called");
      pass = false;
    }
    if (calls.indexOf(who + "PNPSNBCLogicalHandler.close()") > -1) {
      TestUtil.logErr(who + "PNPSNBCLogicalHandler.close() was called");
      pass = false;
    }
    if (calls.indexOf(who + "PNPSNBCSOAPHandler.close()") > -1) {
      TestUtil.logErr(who + "PNPSNBCSOAPHandler.close() was called");
      pass = false;
    }

    return pass;
  }
}
