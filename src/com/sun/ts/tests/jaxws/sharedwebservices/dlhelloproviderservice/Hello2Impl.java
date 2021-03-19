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

package com.sun.ts.tests.jaxws.sharedwebservices.dlhelloproviderservice;

import jakarta.xml.ws.*;
import jakarta.xml.soap.*;

import java.util.Iterator;

import com.sun.ts.tests.jaxws.common.JAXWS_Util;

/*
 * Provider<SOAPMessage> - req/res a SOAPMessage in Message Mode
 */
@WebServiceProvider(serviceName = "HelloService", portName = "Hello2Port", targetNamespace = "http://helloservice.org/wsdl", wsdlLocation = "WEB-INF/wsdl/WSDLHelloProviderService.wsdl")
@BindingType(value = jakarta.xml.ws.soap.SOAPBinding.SOAP11HTTP_BINDING)
@ServiceMode(value = jakarta.xml.ws.Service.Mode.MESSAGE)
public class Hello2Impl implements Provider<SOAPMessage> {

  public SOAPMessage invoke(SOAPMessage req) {
    System.out.println("**** Received in Provider Impl Hello2Impl ******");
    System.out.println("->    SOAPMessage received=" + req);
    JAXWS_Util.dumpSOAPMessage(req, false);
    SOAPMessage respMsg = null;
    try {

      String helloResp = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\"><soapenv:Body><HelloResponse xmlns=\"http://helloservice.org/types\"><argument>responseBean</argument></HelloResponse></soapenv:Body></soapenv:Envelope>";

      respMsg = JAXWS_Util.makeSOAPMessage(helloResp);
      respMsg.saveChanges();
    } catch (Exception e) {
      System.out.println("Exception: occurred " + e);
    }
    System.out.println("->    SOAPMessage being returned=" + respMsg);
    JAXWS_Util.dumpSOAPMessage(respMsg, false);

    return respMsg;
  }

}
