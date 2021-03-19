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

package com.sun.ts.tests.jaxws.wsa.common;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import java.util.Iterator;
import java.util.Map;
import java.util.List;
import java.util.Set;

import jakarta.xml.ws.WebServiceException;
import jakarta.xml.ws.handler.soap.SOAPMessageContext;
import jakarta.xml.ws.handler.soap.SOAPHandler;
import jakarta.xml.ws.handler.MessageContext;
import jakarta.xml.soap.SOAPMessage;
import jakarta.xml.soap.SOAPHeader;
import jakarta.xml.soap.SOAPBody;
import jakarta.xml.soap.SOAPElement;
import jakarta.xml.soap.SOAPException;
import jakarta.xml.soap.Text;
import javax.xml.namespace.QName;

import com.sun.ts.tests.jaxws.common.*;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;

public class WsaBaseSOAPHandler implements SOAPHandler<SOAPMessageContext> {

  public WsaBaseSOAPHandler() {
  }

  public boolean handleMessage(SOAPMessageContext context) {
    boolean outbound = (Boolean) context
        .get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
    if (outbound) {
      TestUtil.logMsg("Direction=outbound");
      if (whichHandler().equals("ServerSOAPHandler"))
        System.out.println("Direction=outbound");
    } else {
      TestUtil.logMsg("Direction=inbound");
      if (whichHandler().equals("ServerSOAPHandler"))
        System.out.println("Direction=inbound");
    }
    try {
      SOAPMessage msg = ((SOAPMessageContext) context).getMessage();
      JAXWS_Util.dumpSOAPMessage(msg, true);
      if (whichHandler().equals("ServerSOAPHandler"))
        JAXWS_Util.dumpSOAPMessage(msg, false);

      SOAPBody soapBody = getSOAPBody(context);
      if (soapBody != null && soapBody.getFirstChild() != null) {
        String oper = getOperationName(soapBody);
        String testName = getTestName(soapBody);
        context.put("op.name", oper);
        context.put("test.name", testName);
        if (!outbound) {
          checkInboundAction(context, oper, getAction(context));
          checkInboundTo(context);
          checkInboundReplyTo(context);
          checkInboundMessageId(context);
          checkInboundRelationship(context);
          checkInboundRelatesTo(context);
          processInboundMessage(context, oper, testName);
        } else {
          processOutboundMessage(context, oper, testName);
        }
      }
    } catch (SOAPException e) {
      e.printStackTrace();
    }
    return true;
  }

  public boolean handleFault(SOAPMessageContext context) {
    boolean outbound = (Boolean) context
        .get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
    if (outbound) {
      TestUtil.logMsg("Direction=outbound");
      if (whichHandler().equals("ServerSOAPHandler"))
        System.out.println("Direction=outbound");
    } else {
      TestUtil.logMsg("Direction=inbound");
      if (whichHandler().equals("ServerSOAPHandler"))
        System.out.println("Direction=inbound");
    }
    if (!outbound) {
      try {
        SOAPMessage msg = ((SOAPMessageContext) context).getMessage();
        JAXWS_Util.dumpSOAPMessage(msg, true);
        if (whichHandler().equals("ServerSOAPHandler"))
          JAXWS_Util.dumpSOAPMessage(msg, false);
        if (context.getMessage().getSOAPBody().getFault() != null) {
          String detailName = null;
          try {
            detailName = context.getMessage().getSOAPBody().getFault()
                .getDetail().getFirstChild().getLocalName();
          } catch (Exception e) {
          }
          checkFaultActions((String) context.get("op.name"), detailName,
              getAction(context));
        }
      } catch (SOAPException e) {
        e.printStackTrace();
      }
    }
    return true;
  }

  public Set<QName> getHeaders() {
    return null;
  }

  public void close(MessageContext messageContext) {
  }

  protected SOAPBody getSOAPBody(SOAPMessageContext context)
      throws SOAPException {
    SOAPBody soapBody = context.getMessage().getSOAPBody();
    return soapBody;
  }

  protected String getAction(SOAPMessageContext context) throws SOAPException {
    String action = null;
    try {
      SOAPMessage message = context.getMessage();
      SOAPHeader header = message.getSOAPHeader();
      Iterator iter = header
          .getChildElements(W3CAddressingConstants.WSA_ACTION_QNAME);
      if (!iter.hasNext()) {
        throw new AddressingHeaderException(
            "WsaBaseSOAPHandler:getAction: Element not found:"
                + W3CAddressingConstants.WSA_ACTION_QNAME);
      }
      Node node = (Node) iter.next();
      action = node.getFirstChild().getNodeValue();
    } catch (SOAPException e) {
      throw new AddressingHeaderException(
          "WsaBaseSOAPHandler:getAction: Element not found:"
              + W3CAddressingConstants.WSA_ACTION_QNAME);
    }
    return action;
  }

  protected String getTo(SOAPMessageContext context) throws SOAPException {
    String to = null;
    SOAPMessage message = context.getMessage();
    SOAPHeader header = message.getSOAPHeader();
    Iterator iter = header
        .getChildElements(W3CAddressingConstants.WSA_TO_QNAME);
    if (iter.hasNext()) {
      to = "invalid";
      NodeList nodes = ((Element) iter.next()).getChildNodes();
      for (int i = 0; i < nodes.getLength(); i++) {
        Node node = (Node) nodes.item(i);
        return node.getNodeValue();
      }
    } else {
      throw new AddressingHeaderException(
          "WsaBaseSOAPHandler:getTo: Element not found:"
              + W3CAddressingConstants.WSA_TO_QNAME);
    }
    return to;
  }

  protected String getReplyTo(SOAPMessageContext context) throws SOAPException {
    String replyTo = null;
    SOAPMessage message = context.getMessage();
    SOAPHeader header = message.getSOAPHeader();
    Iterator iter = header
        .getChildElements(W3CAddressingConstants.WSA_REPLYTO_QNAME);
    if (iter.hasNext()) {
      replyTo = "invalid";
      NodeList nodes = ((Element) iter.next()).getChildNodes();
      for (int i = 0; i < nodes.getLength(); i++) {
        Node node = (Node) nodes.item(i);
        if (node instanceof Text) {
          continue;
        }
        if (node.getLocalName().equals("Address") && node.getNamespaceURI()
            .equals(W3CAddressingConstants.WSA_NAMESPACE_NAME)) {
          return node.getFirstChild().getNodeValue();
        }
      }
    } else {
      throw new AddressingHeaderException(
          "WsaBaseSOAPHandler:getReplyTo: Element not found:"
              + W3CAddressingConstants.WSA_REPLYTO_QNAME);
    }
    return replyTo;
  }

  protected String getMessageId(SOAPMessageContext context)
      throws SOAPException {
    SOAPMessage message = context.getMessage();
    SOAPHeader header = message.getSOAPHeader();
    Iterator iter = header
        .getChildElements(W3CAddressingConstants.WSA_MESSAGEID_QNAME);
    if (!iter.hasNext()) {
      throw new AddressingHeaderException(
          "WsaBaseSOAPHandler:getMessageId: Element not found:"
              + W3CAddressingConstants.WSA_MESSAGEID_QNAME);
    }
    Node node = (Node) iter.next();
    String mid = node.getFirstChild().getNodeValue();
    return mid;
  }

  protected String getRelatesTo(SOAPMessageContext context)
      throws SOAPException {
    SOAPMessage message = context.getMessage();
    SOAPHeader header = message.getSOAPHeader();
    Iterator iter = header
        .getChildElements(W3CAddressingConstants.WSA_RELATESTO_QNAME);
    if (!iter.hasNext()) {
      throw new AddressingHeaderException(
          "WsaBaseSOAPHandler:getRelatesTo: Element not found:"
              + W3CAddressingConstants.WSA_RELATESTO_QNAME);
    }
    Node node = (Node) iter.next();
    String relatesTo = node.getFirstChild().getNodeValue();
    return relatesTo;
  }

  protected String getActionDoesNotExist(SOAPMessageContext context)
      throws SOAPException {
    String action = null;
    try {
      SOAPMessage message = context.getMessage();
      SOAPHeader header = message.getSOAPHeader();
      Iterator iter = header
          .getChildElements(W3CAddressingConstants.WSA_ACTION_QNAME);
      if (iter.hasNext()) {
        Node node = (Node) iter.next();
        action = node.getFirstChild().getNodeValue();
      }
    } catch (SOAPException e) {
      action = null;
    }
    return action;
  }

  protected String getRelationship(SOAPMessageContext context)
      throws SOAPException {
    SOAPMessage message = context.getMessage();
    SOAPHeader header = message.getSOAPHeader();
    Iterator iter = header
        .getChildElements(W3CAddressingConstants.WSA_RELATIONSHIPTYPE_QNAME);
    if (!iter.hasNext()) {
      throw new AddressingHeaderException(
          "WsaBaseSOAPHandler:getRelationship: Element not found:"
              + W3CAddressingConstants.WSA_RELATIONSHIPTYPE_QNAME);
    }
    Node node = (Node) iter.next();
    String relationship = node.getFirstChild().getNodeValue();
    return relationship;
  }

  protected String getOperationName(SOAPBody soapBody) throws SOAPException {
    return soapBody.getFirstChild().getLocalName();
  }

  protected String getTestName(SOAPBody soapbody) throws SOAPException {
    String testName = null;
    SOAPElement se = null;
    Iterator i = soapbody.getChildElements();
    if (i.hasNext()) {
      se = (SOAPElement) i.next();
      i = se.getChildElements();
      while (i.hasNext()) {
        se = (SOAPElement) i.next();
        String elementName = se.getElementName().getLocalName();
        Node node = (Node) se;
        String elementValue = node.getFirstChild().getNodeValue();
        if (elementName.equals("testName")) {
          testName = elementValue;
          break;
        }
      }
    }
    return testName;
  }

  protected void checkFaultActions(String requestName, String detailName,
      String action) {
  }

  protected void checkInboundAction(SOAPMessageContext context, String oper,
      String action) {
  };

  protected void checkInboundTo(SOAPMessageContext context) {
  }

  protected void checkInboundReplyTo(SOAPMessageContext context) {
  }

  protected void checkInboundMessageId(SOAPMessageContext context) {
  }

  protected void checkInboundRelationship(SOAPMessageContext context) {
  }

  protected void checkInboundRelatesTo(SOAPMessageContext context) {
  }

  protected void processOutboundMessage(SOAPMessageContext context, String oper,
      String testName) {
  }

  protected void processInboundMessage(SOAPMessageContext context, String oper,
      String testName) {
  }

  protected String whichHandler() {
    return "WsaBaseSOAPHandler";
  }

  protected void checkInboundToExist(SOAPMessageContext context) {
    String to = null;
    try {
      to = getTo(context);
      TestUtil.logMsg(whichHandler() + ".checkInboundToExist: [To=" + to + "]");
      if (whichHandler().equals("ServerSOAPHandler"))
        System.out
            .println(whichHandler() + ".checkInboundToExist: [To=" + to + "]");
    } catch (Exception e) {
      return;
    }
    if (to == null) {
      throw new AddressingPropertyException("wsa:To contains null");
    }
    if (to.equals("invalid")) {
      throw new AddressingPropertyException("wsa:To contains an invalid value");
    }
    if (to.equals("")) {
      throw new AddressingPropertyException("wsa:To contains an empty value");
    }
  }

  protected void checkInboundRelatesToExist(SOAPMessageContext context) {
    String relatesto = null;
    String mid = null;
    try {
      relatesto = getRelatesTo(context);
      TestUtil.logMsg(whichHandler()
          + ".checkInboundRelatesToExist: [RelatesTo=" + relatesto + "]");
      if (whichHandler().equals("ServerSOAPHandler"))
        System.out.println(whichHandler()
            + ".checkInboundRelatesToExist: [RelatesTo=" + relatesto + "]");
    } catch (Exception e) {
      throw new WebServiceException(e);
    }
    if (relatesto == null) {
      throw new AddressingPropertyException("wsa:RelatesTo contains null");
    }
    if (relatesto.equals("")) {
      throw new AddressingPropertyException(
          "wsa:RelatesTo contains an empty value");
    }
  }

  protected void checkInboundMessageIdExist(SOAPMessageContext context) {
    String mid = null;
    try {
      mid = getMessageId(context);
      TestUtil.logMsg(whichHandler()
          + ".checkInboundMessageIdExist: [MessageId=" + mid + "]");
      if (whichHandler().equals("ServerSOAPHandler"))
        System.out.println(whichHandler()
            + ".checkInboundMessageIdExist: [MessageId=" + mid + "]");
    } catch (Exception e) {
      throw new WebServiceException(e);
    }
    if (mid == null) {
      throw new AddressingPropertyException("wsa:MessageId contains null");
    }
    if (mid.equals("")) {
      throw new AddressingPropertyException(
          "wsa:MessageId contains an empty value");
    }
  }

  protected void checkInboundReplyToExist(SOAPMessageContext context) {
    String replyto = null;
    try {
      replyto = getReplyTo(context);
      TestUtil.logMsg(whichHandler() + ".checkInboundReplyToExist: [ReplyTo="
          + replyto + "]");
      if (whichHandler().equals("ServerSOAPHandler"))
        System.out.println(whichHandler()
            + ".checkInboundReplyToExist: [ReplyTo=" + replyto + "]");
    } catch (Exception e) {
      return;
    }
    if (replyto == null) {
      throw new AddressingPropertyException("wsa:ReplyTo contains null");
    }
    if (replyto.equals("invalid")) {
      throw new AddressingPropertyException(
          "wsa:ReplyTo contains an invalid value");
    }
    if (replyto.equals("")) {
      throw new AddressingPropertyException(
          "wsa:ReplyTo contains an empty value");
    }
  }

  protected void checkActionDoesNotExist(String action) {
    TestUtil.logMsg(whichHandler() + ".checkActionDoesNotExist");
    if (whichHandler().equals("ServerSOAPHandler"))
      System.out.println(whichHandler() + ".checkActionDoesNotExist");
    if (action != null) {
      throw new AddressingPropertyException(
          "wsa:Action header exists (unexpected)");
    }
  }

  protected void checkInboundToDoesNotExist(SOAPMessageContext context) {
    TestUtil.logMsg(whichHandler() + ".checkInboundToDoesNotExist");
    if (whichHandler().equals("ServerSOAPHandler"))
      System.out.println(whichHandler() + ".checkInboundToDoesNotExist");
    boolean pass = false;
    try {
      getTo(context);
    } catch (Exception e) {
      pass = true;
    }
    if (!pass)
      throw new AddressingPropertyException(
          "wsa:To header exists (unexpected)");
  }

  protected void checkInboundRelatesToDoesNotExist(SOAPMessageContext context) {
    TestUtil.logMsg(whichHandler() + ".checkInboundRelatesToDoesNotExist");
    if (whichHandler().equals("ServerSOAPHandler"))
      System.out.println(whichHandler() + ".checkInboundRelatesToDoesNotExist");
    boolean pass = false;
    try {
      getRelatesTo(context);
    } catch (Exception e) {
      pass = true;
    }
    if (!pass)
      throw new AddressingPropertyException(
          "wsa:RelatesTo header exists (unexpected)");
  }

  protected void checkInboundMessageIdDoesNotExist(SOAPMessageContext context) {
    TestUtil.logMsg(whichHandler() + ".checkInboundMessageIdDoesNotExist");
    if (whichHandler().equals("ServerSOAPHandler"))
      System.out.println(whichHandler() + ".checkInboundMessageIdDoesNotExist");
    boolean pass = false;
    try {
      getMessageId(context);
    } catch (Exception e) {
      pass = true;
    }
    if (!pass)
      throw new AddressingPropertyException(
          "wsa:MessageId header exists (unexpected)");
  }

  protected void checkInboundReplyToDoesNotExist(SOAPMessageContext context) {
    TestUtil.logMsg(whichHandler() + ".checkInboundReplyToDoesNotExist");
    if (whichHandler().equals("ServerSOAPHandler"))
      System.out.println(whichHandler() + ".checkInboundReplyToDoesNotExist");
    boolean pass = false;
    try {
      getReplyTo(context);
    } catch (Exception e) {
      pass = true;
    }
    if (!pass)
      throw new AddressingPropertyException(
          "wsa:ReplyTo header exists (unexpected)");
  }
}
