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
 * $Id: WsaSOAPUtils.java 51075 2003-03-27 10:44:21Z lschwenk $
 */

package com.sun.ts.tests.jaxws.wsa.common;

import com.sun.ts.tests.jaxws.common.XMLUtils;

import jakarta.xml.soap.MessageFactory;
import jakarta.xml.soap.SOAPConstants;
import jakarta.xml.soap.SOAPElement;
import jakarta.xml.soap.SOAPMessage;
import jakarta.xml.soap.SOAPException;
import jakarta.xml.soap.SOAPBody;
import jakarta.xml.soap.SOAPHeader;
import jakarta.xml.soap.SOAPFault;
import jakarta.xml.soap.Detail;
import jakarta.xml.soap.DetailEntry;
import jakarta.xml.soap.Text;
import jakarta.xml.ws.soap.*;
import jakarta.xml.ws.handler.*;
import jakarta.xml.ws.handler.soap.*;
import javax.xml.namespace.QName;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import java.util.Iterator;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import jakarta.xml.ws.Dispatch;

import com.sun.ts.tests.jaxws.wsa.common.W3CAddressingConstants;
import com.sun.ts.tests.jaxws.wsa.common.MemberSubmissionAddressingConstants;

public class WsaSOAPUtils implements jakarta.xml.soap.SOAPConstants,
    com.sun.ts.tests.jaxws.wsa.common.W3CAddressingConstants {

  public static final String UUID = "uuid:" + java.util.UUID.randomUUID();

  public static final String W3C_WSA_NS = W3CAddressingConstants.WSA_NAMESPACE_NAME;

  public static final String MS_WSA_NS = MemberSubmissionAddressingConstants.WSA_NAMESPACE_NAME;

  public static final String S11_NS = SOAPConstants.URI_NS_SOAP_1_1_ENVELOPE;

  public static final String S12_NS = SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE;

  public static final String FAULT_DETAIL = "detail";

  public static final String FAULT_ACTOR = "faultactor";

  public static final String FAULT_STRING = "faultstring";

  public static final String FAULT_CODE = "faultcode";

  public static final String[] FAULT_CHILDREN = { FAULT_ACTOR, FAULT_CODE,
      FAULT_DETAIL, FAULT_STRING };

  private static String AddressingVersionNSURI = W3CAddressingConstants.WSA_NAMESPACE_NAME;

  private static String AddressingVersionAnonymousURI = W3CAddressingConstants.WSA_ANONYMOUS_ADDRESS;

  public static void setAddrVerNSUri(String nsuri, String anonuri) {
    AddressingVersionNSURI = nsuri;
    AddressingVersionAnonymousURI = anonuri;
  }

  public static String getAddrVerNSUri() {
    return AddressingVersionNSURI;
  }

  public static String getAddrVerAnonUri() {
    return AddressingVersionAnonymousURI;
  }

  public static boolean isValidSoapFaultChildName(SOAPElement element) {
    for (int i = 0; i < FAULT_CHILDREN.length; i++) {
      if (FAULT_CHILDREN[i].equals(element.getElementName().getLocalName())) {
        return true;
      }
    }
    return false;
  }

  public static boolean isMustUnderstandFaultCode(SOAPFaultException se)
      throws SOAPException {
    return isFaultCode(se,
        W3CAddressingConstants.SOAP11_MUSTUNDERSTAND_FAULT.getLocalPart());
  }

  public static boolean isVersionMismatchFaultCode(SOAPFaultException se)
      throws SOAPException {
    return isFaultCode(se,
        W3CAddressingConstants.SOAP11_VERSIONMISMATCH_FAULT.getLocalPart());
  }

  public static boolean isClientFaultCode(SOAPFaultException se)
      throws SOAPException {
    return isFaultCode(se,
        W3CAddressingConstants.SOAP11_CLIENT_FAULT.getLocalPart());
  }

  public static boolean isServerFaultCode(SOAPFaultException se)
      throws SOAPException {
    return isFaultCode(se,
        W3CAddressingConstants.SOAP11_SERVER_FAULT.getLocalPart());
  }

  public static boolean isMessageAddressingHeaderRequiredFaultCode(
      SOAPMessage message) throws SOAPException {
    return isFaultCode(message,
        W3CAddressingConstants.MAP_REQUIRED_QNAME.getLocalPart());
  }

  public static boolean isMessageAddressingHeaderRequiredFaultCode(
      SOAPFaultException se) {
    return isFaultCode(se,
        W3CAddressingConstants.MAP_REQUIRED_QNAME.getLocalPart());
  }

  public static boolean isInvalidAddressingHeaderFaultCode(SOAPMessage message)
      throws SOAPException {
    return isFaultCode(message,
        W3CAddressingConstants.INVALID_MAP_QNAME.getLocalPart());
  }

  public static boolean isInvalidAddressingHeaderFaultCode(
      SOAPFaultException se) {
    return isFaultCode(se,
        W3CAddressingConstants.INVALID_MAP_QNAME.getLocalPart());
  }

  public static boolean isInvalidEPRFaultCode(SOAPMessage message)
      throws SOAPException {
    return isFaultCode(message,
        W3CAddressingConstants.INVALID_EPR.getLocalPart());
  }

  public static boolean isInvalidEPRFaultCode(SOAPFaultException se) {
    return isFaultCode(se, W3CAddressingConstants.INVALID_EPR.getLocalPart());
  }

  public static boolean isInvalidCARDINALITYFaultCode(SOAPMessage message)
      throws SOAPException {
    return isFaultCode(message,
        W3CAddressingConstants.INVALID_CARDINALITY.getLocalPart());
  }

  public static boolean isInvalidCARDINALITYFaultCode(SOAPFaultException se) {
    return isFaultCode(se,
        W3CAddressingConstants.INVALID_CARDINALITY.getLocalPart());
  }

  public static boolean isMissingAddressInEPRFaultCode(SOAPMessage message)
      throws SOAPException {
    return isFaultCode(message,
        W3CAddressingConstants.MISSING_ADDRESS_IN_EPR.getLocalPart());
  }

  public static boolean isMissingAddressInEPRFaultCode(SOAPFaultException se) {
    return isFaultCode(se,
        W3CAddressingConstants.MISSING_ADDRESS_IN_EPR.getLocalPart());
  }

  public static boolean isDuplicateMessageIDFaultCode(SOAPMessage message)
      throws SOAPException {
    return isFaultCode(message,
        W3CAddressingConstants.DUPLICATE_MESSAGEID.getLocalPart());
  }

  public static boolean isDuplicateMessageIDFaultCode(SOAPFaultException se) {
    return isFaultCode(se,
        W3CAddressingConstants.DUPLICATE_MESSAGEID.getLocalPart());
  }

  public static boolean isActionMismatchFaultCode(SOAPMessage message)
      throws SOAPException {
    return isFaultCode(message,
        W3CAddressingConstants.ACTION_MISMATCH.getLocalPart());
  }

  public static boolean isActionMismatchFaultCode(SOAPFaultException se) {
    return isFaultCode(se,
        W3CAddressingConstants.ACTION_MISMATCH.getLocalPart());
  }

  public static boolean isOnlyAnonymousAddressSupportedFaultCode(
      SOAPMessage message) throws SOAPException {
    return isFaultCode(message,
        W3CAddressingConstants.ONLY_ANONYMOUS_ADDRESS_SUPPORTED.getLocalPart());
  }

  public static boolean isOnlyAnonymousAddressSupportedFaultCode(
      SOAPFaultException se) {
    return isFaultCode(se,
        W3CAddressingConstants.ONLY_ANONYMOUS_ADDRESS_SUPPORTED.getLocalPart());
  }

  public static boolean isOnlyNonAnonymousAddressSupportedFaultCode(
      SOAPMessage message) throws SOAPException {
    return isFaultCode(message,
        W3CAddressingConstants.ONLY_NON_ANONYMOUS_ADDRESS_SUPPORTED
            .getLocalPart());
  }

  public static boolean isOnlyNonAnonymousAddressSupportedFaultCode(
      SOAPFaultException se) {
    return isFaultCode(se,
        W3CAddressingConstants.ONLY_NON_ANONYMOUS_ADDRESS_SUPPORTED
            .getLocalPart());
  }

  public static boolean isDestinationUnreachableFaultCode(SOAPMessage message)
      throws SOAPException {
    return isFaultCode(message,
        W3CAddressingConstants.DESTINATION_UNREACHABLE_QNAME.getLocalPart());
  }

  public static boolean isDestinationUnreachableFaultCode(
      SOAPFaultException se) {
    return isFaultCode(se,
        W3CAddressingConstants.DESTINATION_UNREACHABLE_QNAME.getLocalPart());
  }

  public static boolean isActionNotSupportedFaultCode(SOAPMessage message)
      throws SOAPException {
    return isFaultCode(message,
        W3CAddressingConstants.ACTION_NOT_SUPPORTED_QNAME.getLocalPart());
  }

  public static boolean isActionNotSupportedFaultCode(SOAPFaultException se) {
    return isFaultCode(se,
        W3CAddressingConstants.ACTION_NOT_SUPPORTED_QNAME.getLocalPart());
  }

  public static boolean isEndpointUnavailableFaultCode(SOAPMessage message)
      throws SOAPException {
    return isFaultCode(message,
        W3CAddressingConstants.ENDPOINT_UNAVAILABLE_QNAME.getLocalPart());
  }

  public static boolean isEndpointUnavailableFaultCode(SOAPFaultException se) {
    return isFaultCode(se,
        W3CAddressingConstants.ENDPOINT_UNAVAILABLE_QNAME.getLocalPart());
  }

  public static boolean isFaultCode(SOAPFaultException se, String faultcode) {
    return se.getFault().getFaultCode().endsWith(faultcode);
  }

  public static boolean isFaultCode(SOAPMessage message, String faultcode)
      throws SOAPException {
    SOAPBody body = message.getSOAPPart().getEnvelope().getBody();
    if (body.hasFault()) {
      return body.getFault().getFaultCode().endsWith(faultcode);
    } else {
      return false;
    }
  }

  public static String getFaultCode(SOAPFaultException se)
      throws SOAPException {
    return se.getFault().getFaultCodeAsQName().getLocalPart();
  }

  public static String getFaultCode(SOAPMessage message) throws SOAPException {
    SOAPBody body = message.getSOAPPart().getEnvelope().getBody();
    return body.getFault().getFaultCodeAsQName().getLocalPart();
  }

  public static String getFaultString(SOAPFaultException se)
      throws SOAPException {
    return se.getFault().getFaultString();
  }

  public static String getFaultString(SOAPMessage message)
      throws SOAPException {
    SOAPBody body = message.getSOAPPart().getEnvelope().getBody();
    return body.getFault().getFaultString();
  }

  public static String[] getFaultDetail(SOAPFaultException se)
      throws SOAPException {
    String faultDetail[] = new String[10];
    int i = 0;

    SOAPFault sf = se.getFault();
    Detail d = sf.getDetail();
    if (d != null) {
      Iterator iter = d.getDetailEntries();
      Object obj;
      while (iter.hasNext()) {
        DetailEntry de = (DetailEntry) iter.next();
        StringBuffer s = new StringBuffer(de.getElementName().getLocalName());
        if (de instanceof Text)
          s.append(":" + de.getValue());
        faultDetail[i++] = s.toString();
      }
    }
    if (i == 0)
      return null;
    else
      return faultDetail;
  }

  public static String[] getFaultDetail(SOAPMessage message)
      throws SOAPException {
    SOAPElement se = null;
    SOAPElement se2 = null;
    Object obj = null;
    String faultDetail[] = new String[10];
    Text text = null;
    int i = 0;

    SOAPHeader header = message.getSOAPHeader();
    Iterator iter = header
        .getChildElements(W3CAddressingConstants.FAULT_DETAIL_QNAME);
    if (!iter.hasNext())
      return null;
    obj = iter.next();
    if (obj instanceof SOAPElement) {
      se = (SOAPElement) obj;
      iter = se.getChildElements();
    }
    while (iter.hasNext()) {
      obj = iter.next();
      if (obj instanceof SOAPElement) {
        se = (SOAPElement) obj;
        faultDetail[i++] = se.getElementQName().getLocalPart();
        iter = se.getChildElements();
      } else if (obj instanceof Text) {
        text = (Text) obj;
        faultDetail[i++] = text.getValue();
      }
    }
    if (i == 0)
      return null;
    else
      return faultDetail;
  }

  public static String[] getFaultDetail(SOAPMessageContext context)
      throws SOAPException {
    return getFaultDetail(context.getMessage());
  }

  public static boolean isProblemHeaderQNameFaultDetail(String faultdetail) {
    if (faultdetail == null)
      return false;
    if (faultdetail.equals(PROBLEM_HEADER_QNAME_QNAME.getLocalPart()))
      return true;
    else
      return false;
  }

  public static boolean isProblemHeaderFaultDetail(String faultdetail) {
    if (faultdetail == null)
      return false;
    if (faultdetail.equals(PROBLEM_HEADER_QNAME.getLocalPart()))
      return true;
    else
      return false;
  }

  public static boolean isProblemActionFaultDetail(String faultdetail) {
    if (faultdetail == null)
      return false;
    if (faultdetail.equals(PROBLEM_ACTION_QNAME.getLocalPart()))
      return true;
    else
      return false;
  }

  private static final Source makeStreamSource(String msg) {
    byte[] bytes = msg.getBytes();
    ByteArrayInputStream sinputStream = new ByteArrayInputStream(bytes);
    return new StreamSource(sinputStream);
  }

  private static final SOAPMessage getSOAPMessage(Source msg) throws Exception {
    MessageFactory factory = MessageFactory.newInstance();
    SOAPMessage message = factory.createMessage();
    message.getSOAPPart().setContent(msg);
    message.saveChanges();
    return message;
  }

  private static final SOAPMessage getSOAP12Message(Source msg)
      throws Exception {
    MessageFactory factory = MessageFactory
        .newInstance(SOAPConstants.SOAP_1_2_PROTOCOL);
    SOAPMessage message = factory.createMessage();
    message.getSOAPPart().setContent(msg);
    message.saveChanges();
    return message;
  }

  public static String fileToXMLString(String filename) {
    return sourceToXMLString(new StreamSource(new File(filename)));
  }

  public static String sourceToXMLString(Source result) {
    String xmlResult = null;
    try {
      TransformerFactory factory = TransformerFactory.newInstance();
      Transformer transformer = factory.newTransformer();
      transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
      transformer.setOutputProperty(OutputKeys.METHOD, "xml");
      OutputStream out = new ByteArrayOutputStream();
      StreamResult streamResult = new StreamResult();
      streamResult.setOutputStream(out);
      transformer.transform(result, streamResult);
      xmlResult = streamResult.getOutputStream().toString();
    } catch (TransformerException e) {
      e.printStackTrace();
    }
    return xmlResult;
  }

  public static final SOAPMessage invoke(Dispatch<SOAPMessage> dispatch,
      String request, String... args) throws Exception {
    String fRequest = String.format(request, (Object[]) args);

    return dispatch.invoke(getSOAPMessage(makeStreamSource(fRequest)));
  }

  public static final void invokeOneWay(Dispatch<SOAPMessage> dispatch,
      String request, String... args) throws Exception {
    String fRequest = String.format(request, (Object[]) args);

    dispatch.invokeOneWay(getSOAPMessage(makeStreamSource(fRequest)));
  }

  public static final SOAPMessage invoke12(Dispatch<SOAPMessage> dispatch,
      String request, String... args) throws Exception {
    String fRequest = String.format(request, (Object[]) args);

    return dispatch.invoke(getSOAP12Message(makeStreamSource(fRequest)));
  }

  public static final void invokeOneWay12(Dispatch<SOAPMessage> dispatch,
      String request, String... args) throws Exception {
    String fRequest = String.format(request, (Object[]) args);

    dispatch.invokeOneWay(getSOAP12Message(makeStreamSource(fRequest)));
  }

  public static final String BAD_ACTION_MESSAGE = "<S:Envelope xmlns:S=\"%s\" "
      + "xmlns:wsa=\"%s\">\n" + "<S:Header>\n" + "<wsa:To>%s</wsa:To>\n"
      + "<wsa:MessageID>" + UUID + "</wsa:MessageID>\n" + "<wsa:ReplyTo>\n"
      + "  <wsa:Address>%s</wsa:Address>\n" + "</wsa:ReplyTo>\n"
      + "<wsa:Action>badSOAPAction</wsa:Action>\n" + "</S:Header>\n"
      + "<S:Body>\n" + "<addNumbers xmlns=\"http://example.com/\">\n"
      + "  <number1>10</number1>\n" + "  <number2>10</number2>\n"
      + "</addNumbers>\n" + "</S:Body></S:Envelope>";

  public static final String MISSING_ACTION_MESSAGE = "<S:Envelope xmlns:S=\"%s\" "
      + "xmlns:wsa=\"%s\">\n" + "<S:Header>\n" + "<wsa:To>%s</wsa:To>\n"
      + "<wsa:MessageID>" + UUID + "</wsa:MessageID>\n" + "<wsa:ReplyTo>\n"
      + "  <wsa:Address>%s</wsa:Address>\n" + "</wsa:ReplyTo>\n"
      + "</S:Header>\n" + "<S:Body>\n"
      + "<addNumbers xmlns=\"http://example.com/\">\n"
      + "  <number1>10</number1>\n" + "  <number2>10</number2>\n"
      + "</addNumbers>\n" + "</S:Body></S:Envelope>";

  public static final String REPLY_TO_REFPS_MESSAGE = "<S:Envelope xmlns:S=\"%s\" "
      + "xmlns:wsa=\"%s\">\n" + "<S:Header>\n" + "<wsa:To>%s</wsa:To>\n"
      + "<wsa:MessageID>" + UUID + "</wsa:MessageID>\n" + "<wsa:ReplyTo>\n"
      + "  <wsa:Address>%s</wsa:Address>\n" + "  <wsa:ReferenceParameters>\n"
      + "    <ck:CustomerKey xmlns:ck=\"http://example.org/customer\">Key#123456789</ck:CustomerKey>\n"
      + "  </wsa:ReferenceParameters>" + "</wsa:ReplyTo>\n"
      + "<wsa:Action>%s</wsa:Action>\n" + "</S:Header>\n" + "<S:Body>\n"
      + "<addNumbers xmlns=\"http://example.com/\">\n"
      + "  <number1>10</number1>\n" + "  <number2>10</number2>\n"
      + "</addNumbers>\n" + "</S:Body></S:Envelope>";

  public static final String FAULT_TO_REFPS_MESSAGE = "<S:Envelope xmlns:S=\"%s\" "
      + "xmlns:wsa=\"%s\">\n" + "<S:Header>\n" + "<wsa:To>%s</wsa:To>\n"
      + "<wsa:MessageID>" + UUID + "</wsa:MessageID>\n" + "<wsa:ReplyTo>\n"
      + "  <wsa:Address>%s</wsa:Address>\n" + "  <wsa:ReferenceParameters>\n"
      + "    <ck:CustomerKey xmlns:ck=\"http://example.org/customer\">Key#123456789</ck:CustomerKey>\n"
      + "  </wsa:ReferenceParameters>" + "</wsa:ReplyTo>\n" + "<wsa:FaultTo>\n"
      + "  <wsa:Address>%s</wsa:Address>\n" + "  <wsa:ReferenceParameters>\n"
      + "    <ck:CustomerKey xmlns:ck=\"http://example.org/customer\">Fault#123456789</ck:CustomerKey>\n"
      + "  </wsa:ReferenceParameters>" + "</wsa:FaultTo>\n"
      + "<wsa:Action>%s</wsa:Action>\n" + "</S:Header>\n" + "<S:Body>\n"
      + "<addNumbers xmlns=\"http://example.com/\">\n"
      + "  <number1>-10</number1>\n" + "  <number2>10</number2>\n"
      + "</addNumbers>\n" + "</S:Body></S:Envelope>";

  public static final String DUPLICATE_TO_MESSAGE = "<S:Envelope xmlns:S=\"%s\" "
      + "xmlns:wsa=\"%s\">\n" + "<S:Header>\n" + "<wsa:To>%s</wsa:To>\n"
      + "</S:Header>\n" + "<S:Body>\n"
      + "<addNumbers xmlns=\"http://example.com/\">\n"
      + "  <number1>10</number1>\n" + "  <number2>10</number2>\n"
      + "</addNumbers>\n" + "</S:Body></S:Envelope>";

  public static final String DUPLICATE_REPLY_TO_MESSAGE = "<S:Envelope xmlns:S=\"%s\" "
      + "xmlns:wsa=\"%s\">\n" + "<S:Header>\n"
      + "<wsa:ReplyTo><wsa:Address>%s</wsa:Address></wsa:ReplyTo>"
      + "</S:Header>\n" + "<S:Body>\n"
      + "<addNumbers xmlns=\"http://example.com/\">\n"
      + "  <number1>10</number1>\n" + "  <number2>10</number2>\n"
      + "</addNumbers>\n" + "</S:Body></S:Envelope>";

  public static final String DUPLICATE_FAULT_TO_MESSAGE = "<S:Envelope xmlns:S=\"%s\" "
      + "xmlns:wsa=\"%s\">\n" + "<S:Header>\n"
      + "<wsa:FaultTo><wsa:Address>%s</wsa:Address></wsa:FaultTo>"
      + "<wsa:FaultTo><wsa:Address>%s</wsa:Address></wsa:FaultTo>"
      + "</S:Header>\n" + "<S:Body>\n"
      + "<addNumbers xmlns=\"http://example.com/\">\n"
      + "  <number1>10</number1>\n" + "  <number2>10</number2>\n"
      + "</addNumbers>\n" + "</S:Body></S:Envelope>";

  public static final String DUPLICATE_ACTION_MESSAGE = "<S:Envelope xmlns:S=\"%s\" "
      + "xmlns:wsa=\"%s\">\n" + "<S:Header>\n" + "<wsa:Action>%s</wsa:Action>"
      + "</S:Header>\n" + "<S:Body>\n"
      + "<addNumbers xmlns=\"http://example.com/\">\n"
      + "  <number1>10</number1>\n" + "  <number2>10</number2>\n"
      + "</addNumbers>\n" + "</S:Body></S:Envelope>";

  public static final String DUPLICATE_MESSAGE_ID_MESSAGE = "<S:Envelope xmlns:S=\"%s\" "
      + "xmlns:wsa=\"%s\">\n" + "<S:Header>\n" + "<wsa:MessageID>" + UUID
      + "</wsa:MessageID>" + "</S:Header>\n" + "<S:Body>\n"
      + "<addNumbers xmlns=\"http://example.com/\">\n"
      + "  <number1>10</number1>\n" + "  <number2>10</number2>\n"
      + "</addNumbers>\n" + "</S:Body></S:Envelope>";

  public static final String DUPLICATE_MESSAGE_ID_MESSAGE_ONEWAY = "<S:Envelope xmlns:S=\"%s\" "
      + "xmlns:wsa=\"%s\">\n" + "<S:Header>\n" + "<wsa:MessageID>" + UUID
      + "</wsa:MessageID>" + "</S:Header>\n" + "<S:Body>\n"
      + "<addNumbers5 xmlns=\"http://example.com/\">\n"
      + "  <number1>10</number1>\n" + "  <number2>10</number2>\n"
      + "</addNumbers5>\n" + "</S:Body></S:Envelope>";

  /*****
   * COMMENT OUT public static final void assertInvalidHeaderFaultCode(QName
   * got, AddressingVersion av) { try {
   * TestCase.assertEquals(av.invalidCardinalityTag, got); } catch
   * (AssertionFailedError e) { TestCase.assertEquals(av.invalidMapTag, got); }
   * }
   * 
   * public static final void assertHeaderRequiredFaultCode(QName got) {
   * TestCase.assertEquals(W3CAddressingConstants.MAP_REQUIRED_QNAME, got); }
   * 
   * public static final void assertHeaderRequiredFaultCode12(SOAPFault f) {
   * TestCase.assertNotNull("Fault element is null", f); QName faultcode =
   * f.getFaultCodeAsQName();
   * TestCase.assertEquals(SOAPConstants.SOAP_SENDER_FAULT, faultcode); Iterator
   * iter = f.getFaultSubcodes(); TestCase.assertNotNull("Subcode iterator is
   * null", iter); TestCase.assertTrue("Subcode iterator has no elements",
   * iter.hasNext()); TestCase.assertEquals(iter.next(),
   * W3CAddressingConstants.MAP_REQUIRED_QNAME); TestCase.assertTrue("No
   * subsubcode on the fault", iter.hasNext());
   * TestCase.assertEquals(iter.next(),
   * W3CAddressingConstants.INVALID_CARDINALITY); }
   * 
   * public static final void assertInvalidCardinalityCode12(SOAPFault f,
   * AddressingVersion av) { TestCase.assertNotNull("Fault element is null", f);
   * QName faultcode = f.getFaultCodeAsQName();
   * TestCase.assertEquals(SOAPConstants.SOAP_SENDER_FAULT, faultcode); Iterator
   * iter = f.getFaultSubcodes(); TestCase.assertNotNull("Subcode iterator is
   * null", iter); TestCase.assertTrue("Subcode iterator has no elements",
   * iter.hasNext()); TestCase.assertEquals(iter.next(), av.invalidMapTag);
   * TestCase.assertTrue("No subsubcode on the fault", iter.hasNext());
   * TestCase.assertEquals(iter.next(), av.invalidCardinalityTag); } COMMENT OUT
   */
}
