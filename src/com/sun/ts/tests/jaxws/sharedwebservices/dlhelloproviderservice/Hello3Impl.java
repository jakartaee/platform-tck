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
 * $Id: Hello3Impl.java 52501 2007-01-24 02:29:49Z lschwenk $
 */

package com.sun.ts.tests.jaxws.sharedwebservices.dlhelloproviderservice;

import jakarta.xml.ws.*;
import jakarta.xml.soap.*;

import java.util.Iterator;

import com.sun.ts.tests.jaxws.common.JAXWS_Util;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamSource;

/*
 * Provider<Source> - req/res a Source in Payload Mode
 */
@WebServiceProvider(serviceName = "HelloService", portName = "Hello3Port", targetNamespace = "http://helloservice.org/wsdl", wsdlLocation = "WEB-INF/wsdl/WSDLHelloProviderService.wsdl")
@BindingType(value = jakarta.xml.ws.soap.SOAPBinding.SOAP11HTTP_BINDING)
@ServiceMode(value = jakarta.xml.ws.Service.Mode.PAYLOAD)
public class Hello3Impl implements Provider<Source> {

  String helloResp = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\"><soapenv:Body><HelloResponse xmlns=\"http://helloservice.org/types\"><argument>response</argument></HelloResponse></soapenv:Body></soapenv:Envelope>";

  public Source invoke(Source req) {
    System.out.println("**** Received in Provider Impl Hello3Impl ******");
    String str = null;
    try {
      str = JAXWS_Util.getSourceAsString(req);
      System.out.println("->    Source received=" + str);
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (str.indexOf("sendEmptyStreamSource") >= 0)
      return new StreamSource();
    else if (str.indexOf("sendEmptyDOMSource") >= 0)
      return new DOMSource();
    else if (str.indexOf("sendEmptySAXSource") >= 0)
      return new SAXSource();
    else
      return JAXWS_Util.makeSource(helloResp, "StreamSource");
  }

}
