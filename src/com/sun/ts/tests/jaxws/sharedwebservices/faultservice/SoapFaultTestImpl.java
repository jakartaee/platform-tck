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

package com.sun.ts.tests.jaxws.sharedwebservices.faultservice;

import jakarta.xml.ws.soap.SOAPFaultException;
import javax.xml.namespace.QName;
import jakarta.xml.soap.*;
import jakarta.xml.ws.WebServiceException;
import java.rmi.ServerException;

@jakarta.jws.WebService(targetNamespace = "http://faulttestservice.org/wsdl", portName = "SoapFaultTestPort", serviceName = "FaultTest", endpointInterface = "com.sun.ts.tests.jaxws.sharedwebservices.faultservice.SoapFaultTest")
public class SoapFaultTestImpl implements SoapFaultTest {
  public String alwaysThrowsException() throws DummyException {
    throw new DummyException("dummyString1", "dummyString2");
  }

  public String alwaysThrowsServerException() throws ServerException {
    throw new ServerException("alwaysThrowsServerException");
  }

  public String alwaysThrowsSOAPFaultExceptionDetailNoChildren() {
    try {
      SOAPFault sf = SOAPFactory.newInstance().createFault(
          "This always throws a SOAPFaultException", new QName("Server"));
      sf.setFaultActor("http://a.dummy.uri");
      sf.addDetail();
      throw new SOAPFaultException(sf);
    } catch (SOAPException se) {
      throw new WebServiceException(se.toString(), se);
    }
  }

  public String alwaysThrowsSOAPFaultExceptionDetailUnqualifiedChildren() {
    try {
      SOAPFault sf = SOAPFactory.newInstance().createFault(
          "This always throws a SOAPFaultException", new QName("Server"));
      sf.setFaultActor("http://a.dummy.uri");
      sf.addDetail();
      sf.getDetail().addDetailEntry(getName("dummyLocalName"));
      throw new SOAPFaultException(sf);
    } catch (SOAPException se) {
      throw new WebServiceException(se.toString(), se);
    }
  }

  public String alwaysThrowsSOAPFaultExceptionDetailQualifiedChildren() {
    try {
      SOAPFault sf = SOAPFactory.newInstance().createFault(
          "This always throws a SOAPFaultException", new QName("Server"));
      sf.setFaultActor("http://a.dummy.uri");
      sf.addDetail();
      sf.getDetail().addDetailEntry(
          getName("dummyLocalName", "dummyPrefix", "http://dummyURI.org"));
      throw new SOAPFaultException(sf);
    } catch (SOAPException se) {
      throw new WebServiceException(se.toString(), se);
    }
  }

  public String alwaysThrowsSOAPFaultExceptionDetailNoAttributes() {
    try {
      SOAPFault sf = SOAPFactory.newInstance().createFault(
          "This always throws a SOAPFaultException", new QName("Server"));
      sf.setFaultActor("http://a.dummy.uri");
      sf.addDetail();
      throw new SOAPFaultException(sf);
    } catch (SOAPException se) {
      throw new WebServiceException(se.toString(), se);
    }
  }

  public String alwaysThrowsSOAPFaultExceptionDetailQualifiedAttributes() {
    try {
      SOAPFault sf = SOAPFactory.newInstance().createFault(
          "This always throws a SOAPFaultException", new QName("Server"));
      sf.setFaultActor("http://a.dummy.uri");
      sf.addDetail();
      sf.getDetail().addAttribute(
          getName("dummyLocalName", "dummyPrefix", "http://dummyURI.org"),
          "dummyValue");
      throw new SOAPFaultException(sf);
    } catch (SOAPException se) {
      throw new WebServiceException(se.toString(), se);
    }
  }

  private Name getName(String localName) throws SOAPException {
    return getName(localName, null, null);
  }

  private Name getName(String localName, String prefix, String uri)
      throws SOAPException {
    if (prefix == null && uri == null) {
      return MessageFactory.newInstance().createMessage().getSOAPPart()
          .getEnvelope().createName(localName);
    } else {
      return MessageFactory.newInstance().createMessage().getSOAPPart()
          .getEnvelope().createName(localName, prefix, uri);
    }
  }
}
