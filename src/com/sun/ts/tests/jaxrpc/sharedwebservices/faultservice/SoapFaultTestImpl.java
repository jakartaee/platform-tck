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

package com.sun.ts.tests.jaxrpc.sharedwebservices.faultservice;

import javax.xml.rpc.soap.SOAPFaultException;
import javax.xml.namespace.QName;
import javax.xml.soap.*;
import java.rmi.RemoteException;
import java.rmi.ServerException;

public class SoapFaultTestImpl implements SoapFaultTest {
  public String alwaysThrowsException() throws RemoteException, DummyException {
    throw new DummyException("dummyString1", "dummyString2");
  }

  public String alwaysThrowsServerException()
      throws RemoteException, ServerException {
    throw new ServerException("alwaysThrowsServerException");
  }

  public String alwaysThrowsSOAPFaultExceptionDetailNoChildren()
      throws RemoteException {
    try {
      throw new SOAPFaultException(new QName("Server"),
          "This always throws a SOAPFaultException", "http://a.dummy.uri",
          getDetailNoChildren());
    } catch (SOAPException se) {
      throw new RemoteException(se.toString(), se);
    }
  }

  public String alwaysThrowsSOAPFaultExceptionDetailUnqualifiedChildren()
      throws RemoteException {
    try {
      throw new SOAPFaultException(new QName("Server"),
          "This always throws a SOAPFaultException", "http://a.dummy.uri",
          getDetailUnqualifiedChildren());
    } catch (SOAPException se) {
      throw new RemoteException(se.toString(), se);
    }
  }

  public String alwaysThrowsSOAPFaultExceptionDetailQualifiedChildren()
      throws RemoteException {
    try {
      throw new SOAPFaultException(new QName("Server"),
          "This always throws a SOAPFaultException", "http://a.dummy.uri",
          getDetailQualifiedChildren());
    } catch (SOAPException se) {
      throw new RemoteException(se.toString(), se);
    }
  }

  public String alwaysThrowsSOAPFaultExceptionDetailNoAttributes()
      throws RemoteException {
    try {
      throw new SOAPFaultException(new QName("Server"),
          "This always throws a SOAPFaultException", "http://a.dummy.uri",
          getDetailNoAttributes());
    } catch (SOAPException se) {
      throw new RemoteException(se.toString(), se);
    }
  }

  public String alwaysThrowsSOAPFaultExceptionDetailQualifiedAttributes()
      throws RemoteException {
    try {
      throw new SOAPFaultException(new QName("Server"),
          "This always throws a SOAPFaultException", "http://a.dummy.uri",
          getDetailQualifiedAttributes());
    } catch (SOAPException se) {
      throw new RemoteException(se.toString(), se);
    }
  }

  private Detail getDetailNoChildren() throws SOAPException {
    return SOAPFactory.newInstance().createDetail();
  }

  private Detail getDetailNoAttributes() throws SOAPException {
    return SOAPFactory.newInstance().createDetail();
  }

  private Detail getDetailQualifiedAttributes() throws SOAPException {
    Detail detail = SOAPFactory.newInstance().createDetail();
    detail.addAttribute(
        getName("dummyLocalName", "dummyPrefix", "http://dummyURI.org"),
        "dummyValue");
    return detail;
  }

  private Detail getDetailUnqualifiedChildren() throws SOAPException {
    Detail d = SOAPFactory.newInstance().createDetail();
    d.addDetailEntry(getName("dummyLocalName"));
    return d;
  }

  private Detail getDetailQualifiedChildren() throws SOAPException {
    Detail detail = SOAPFactory.newInstance().createDetail();
    detail.addDetailEntry(
        getName("dummyLocalName", "dummyPrefix", "http://dummyURI.org"));
    return detail;
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
