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

package com.sun.ts.tests.webservices.handler.HandlerSec;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import com.sun.ts.tests.jaxrpc.common.*;
import com.sun.javatest.Status;

import com.sun.ts.tests.jaxrpc.common.HandlerBase;
import com.sun.ts.tests.jaxrpc.common.HandlerTracker;
import java.io.*;
import java.net.*;
import java.util.*;
import java.rmi.*;
import javax.xml.soap.*;
import javax.xml.rpc.soap.*;
import javax.xml.rpc.handler.*;
import javax.xml.rpc.handler.soap.*;
import java.util.*;
import javax.xml.soap.*;
import javax.xml.rpc.JAXRPCException;
import javax.xml.rpc.handler.*;
import javax.xml.rpc.handler.soap.*;

public class ServerHandler extends HandlerBase {
  private boolean debug = true;

  public boolean handleRequest(MessageContext context) {
    // System.out.println("pre-request");
    // printMsg(context);
    boolean result = false;
    try {
      preinvoke();
      HandlerTracker.put("HandlerClassLoader",
          this.getClass().getClassLoader());

      updateMessage(context, true);

      // System.out.println("post-request");
      // printMsg(context);
      result = super.handleRequest(context);
    } finally {
      postinvoke();
    }
    return result;
  }

  public boolean handleResponse(MessageContext context) {
    // System.out.println("pre-response");
    // printMsg(context);

    updateMessage(context, false);

    // System.out.println("post-response");
    // printMsg(context);
    return super.handleResponse(context);
  }

  /**
   * print the message in the soap body
   * 
   * @param context
   *          MessageContext
   */
  protected void printMsg(MessageContext context) {
    try {
      SOAPMessage msg = ((SOAPMessageContext) context).getMessage();
      SOAPEnvelope env = msg.getSOAPPart().getEnvelope();
      SOAPBody body = env.getBody();
      Iterator it = body.getChildElements();
      while (it.hasNext()) {
        SOAPElement elem = (SOAPElement) it.next();
        printElem(elem, "  ");
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * print a SOAPElement
   * 
   * @param elem
   *          SOAPElement to print
   * @param indent
   *          String
   */
  protected void printElem(SOAPElement elem, String indent) {
    try {
      String elemName = elem.getElementName().getQualifiedName();
      System.out.println(indent + "<" + elemName + " ");
      Iterator it = elem.getAllAttributes();
      while (it.hasNext()) {
        Name attr = (Name) it.next();
        System.out.println(indent + "    " + attr.getQualifiedName() + "="
            + elem.getAttributeValue(attr));
      }
      System.out.println(indent + ">");

      it = elem.getChildElements();
      while (it.hasNext()) {
        Node child = (Node) it.next();
        if (child instanceof SOAPElement) {
          printElem((SOAPElement) child, indent + "  ");
        } else if (child instanceof Text) {
          System.out.println(indent + "  " + child.getValue());
        }
      }
      System.out.println(indent + "</" + elemName + ">");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Update the request/response message for ChangeMessageTest1 thru 9
   * 
   * @param context
   *          MessageContext
   * @param forRequest
   *          boolean indicating if forRequest
   */
  protected void updateMessage(MessageContext context, boolean forRequest) {
    try {
      SOAPMessage msg = ((SOAPMessageContext) context).getMessage();
      SOAPEnvelope env = msg.getSOAPPart().getEnvelope();
      SOAPBody body = env.getBody();
      String value = getEchoStringValue(body);

      // Determine how to change the message based on the value,
      // System.out.println("Update Message Value = " + value);

      if (value != null) {
        if (forRequest && value.indexOf("request") >= 0) {
          if (debug) {
            System.out.println("-----------------------------");
            System.out.println("Original SOAP Message Request");
            System.out.println("-----------------------------");
            msg.writeTo(System.out);
            System.out.println("\n");
          }
          if (value.indexOf("change_value") >= 0) {
            updateValue(body, value + "_PASS");
          } else if (value.indexOf("add_element") >= 0) {
            addElement(body);
          } else if (value.indexOf("change_element") >= 0) {
            changeElementName(body);
          } else if (value.indexOf("change_message") >= 0) {
            changeMessage(body);
          }
          if (debug) {
            System.out.println("-----------------------------");
            System.out.println("Modified SOAP Message Request");
            System.out.println("-----------------------------");
            msg.writeTo(System.out);
            System.out.println("\n");
          }
        } else if (!forRequest && value.indexOf("response") >= 0) {
          if (debug) {
            System.out.println("------------------------------");
            System.out.println("Original SOAP Message Response");
            System.out.println("------------------------------");
            msg.writeTo(System.out);
            System.out.println("\n");
          }
          if (value.indexOf("change_value") >= 0) {
            updateValue(body, value + "_PASS");
          } else if (value.indexOf("add_element") >= 0) {
            addElement(body);
          } else if (value.indexOf("change_element") >= 0) {
            changeElementName(body);
          } else if (value.indexOf("change_message") >= 0) {
            changeMessage(body);
          }
          if (debug) {
            System.out.println("------------------------------");
            System.out.println("Modified SOAP Message Response");
            System.out.println("------------------------------");
            msg.writeTo(System.out);
            System.out.println("\n");
          }
        }
      }
      msg.saveChanges();
    } catch (Exception e) {
      throw new JAXRPCException("updateMessage() through exception: ", e);
    }
  }

  /**
   * get the value of the echoString request or response Utility routine of
   * updateMessage.
   * 
   * @param body
   *          SOAPBody
   */
  protected String getEchoStringValue(SOAPBody body) {
    String value = null;
    try {
      // Get request/response wrapper
      Iterator it = body.getChildElements();
      SOAPElement elem = (SOAPElement) it.next();
      if (elem.getElementName().getLocalName().indexOf("echoString") >= 0) {
        // Get argument
        Iterator it2 = elem.getChildElements();
        SOAPElement arg = (SOAPElement) it2.next();
        value = arg.getValue();
      }
    } catch (Exception e) {
      throw new JAXRPCException("getEchoStringValue() through exception: ", e);
    }
    return value;
  }

  /**
   * update the value of the echoString request/response (used for
   * ChangeMessageTest2-3)
   * 
   * @param body
   *          SOAPBody
   * @param newValue
   *          is a String with the new value
   */
  protected void updateValue(SOAPBody body, String newValue) {
    try {
      // Get request/response wrapper
      Iterator it = body.getChildElements();
      SOAPElement elem = (SOAPElement) it.next();
      if (elem.getElementName().getLocalName().indexOf("echoString") >= 0) {
        // Get argument
        Iterator it2 = elem.getChildElements();
        SOAPElement arg = (SOAPElement) it2.next();

        // Get text node and detach it
        Iterator it3 = arg.getChildElements();
        Text text = (Text) it3.next();
        text.detachNode();

        // Add a new text node to the argument
        arg.addTextNode(newValue);

      }
    } catch (Exception e) {
      throw new JAXRPCException("updateValue() through exception: ", e);
    }
  }

  /**
   * add a new element to the echoString request/response (used for
   * ChangeMessageTest8-9)
   * 
   * @param body
   *          SOAPBody
   */
  protected void addElement(SOAPBody body) {
    try {
      // Get request/response wrapper
      Iterator it = body.getChildElements();
      SOAPElement elem = (SOAPElement) it.next();
      if (elem.getElementName().getLocalName().indexOf("echoString") >= 0) {
        // Get argument
        Iterator it2 = elem.getChildElements();
        SOAPElement arg = (SOAPElement) it2.next();

        // Get the value of the text node
        Iterator it3 = arg.getChildElements();
        Text text = (Text) it3.next();
        String value = text.getValue();

        // Add a new argument with the same value
        SOAPElement newArg = elem.addChildElement("badParm");
        newArg.addTextNode(value);

      }
    } catch (Exception e) {
      throw new JAXRPCException("addElement() through exception: ", e);
    }
  }

  /**
   * change the element name in the echoString request/response (used for
   * ChangeMessageTest6-7)
   * 
   * @param body
   *          SOAPBody
   */
  protected void changeElementName(SOAPBody body) {
    try {
      // Get request/response wrapper
      Iterator it = body.getChildElements();
      SOAPElement elem = (SOAPElement) it.next();
      if (elem.getElementName().getLocalName().indexOf("echoString") >= 0) {
        // Get argument and detach it
        Iterator it2 = elem.getChildElements();
        SOAPElement arg = (SOAPElement) it2.next();
        arg.detachNode();

        // Get the value of the text node
        Iterator it3 = arg.getChildElements();
        Text text = (Text) it3.next();
        String value = text.getValue();

        // Add a new argument with the same value
        SOAPElement newArg = elem.addChildElement("badParm");
        newArg.addTextNode(value);

      }
    } catch (Exception e) {
      throw new JAXRPCException("changeElementName() through exception: ", e);
    }
  }

  /**
   * change the message of the echoString request/response to locate the
   * badEchoString method. (used for ChangeMessageTest4-5)
   * 
   * @param body
   *          SOAPBody
   */
  protected void changeMessage(SOAPBody body) {
    try {
      // Get request/response wrapper
      Iterator it = body.getChildElements();
      SOAPElement elem = (SOAPElement) it.next();
      if (elem.getElementName().getLocalName().indexOf("echoString") >= 0) {

        // Detach the operation/response wrapper
        elem.detachNode();

        // Get the argument and detach it
        Iterator it2 = elem.getChildElements();
        SOAPElement arg = (SOAPElement) it2.next();
        arg.detachNode();

        // Use a localName that will locate
        // the badEchoString method
        String localName = "badEchoString";
        if (elem.getElementName().getLocalName().indexOf("Response") >= 0) {
          localName += "Response";
        }

        // Add the new element to the body
        SOAPElement newElem = body.addChildElement(localName,
            elem.getElementName().getPrefix());

        // Attach the detached argument to the new element
        newElem.addChildElement(arg);
      }
    } catch (Exception e) {
      throw new JAXRPCException("changeMessage() through exception: ", e);
    }
  }
}
