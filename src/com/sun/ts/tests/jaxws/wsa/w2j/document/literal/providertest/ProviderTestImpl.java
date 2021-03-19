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

package com.sun.ts.tests.jaxws.wsa.w2j.document.literal.providertest;

import jakarta.xml.ws.*;
import java.io.ByteArrayInputStream;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;

import jakarta.xml.ws.soap.Addressing;
import jakarta.xml.ws.soap.MTOM;
import jakarta.xml.ws.RespectBinding;

import com.sun.ts.tests.jaxws.common.JAXWS_Util;

@WebServiceProvider(serviceName = "ProviderTestService", portName = "ProviderTestPort", targetNamespace = "http://providertestservice.org/wsdl", wsdlLocation = "WEB-INF/wsdl/ProviderTestService.wsdl")
@BindingType(value = "http://schemas.xmlsoap.org/wsdl/soap/http")
@Addressing(enabled = true)
@MTOM(enabled = true)
@RespectBinding(enabled = true)
@ServiceMode(value = jakarta.xml.ws.Service.Mode.PAYLOAD)

public class ProviderTestImpl implements Provider<Source> {

  private static final jakarta.xml.bind.JAXBContext jaxbContext = createJAXBContext();

  public jakarta.xml.bind.JAXBContext getJAXBContext() {
    return jaxbContext;
  }

  private static jakarta.xml.bind.JAXBContext createJAXBContext() {
    try {
      return jakarta.xml.bind.JAXBContext.newInstance(
          com.sun.ts.tests.jaxws.wsa.w2j.document.literal.providertest.ObjectFactory.class);
    } catch (jakarta.xml.bind.JAXBException e) {
      throw new WebServiceException(e.getMessage(), e);
    }
  }

  public Source invoke(Source req) {
    System.out.println("**** Received in Provider Impl ******");
    DOMResult dr = null;
    try {
      dr = JAXWS_Util.getSourceAsDOMResult(req);
      System.out.println("->    Source=" + JAXWS_Util.getDOMResultAsString(dr));

    } catch (Exception e) {
      System.out.println("Exception: failed getDOMResultAsString ... " + e);
    }
    try {
      HelloRequest request = recvBean(new DOMSource(dr.getNode()));
      String arg = request.getArgument();
      String response = "<HelloResponse xmlns=\"http://providertestservice.org/types\"><argument>"
          + arg + "</argument></HelloResponse>";
      System.out.println("Sending response=" + response);
      Source source = new StreamSource(
          new ByteArrayInputStream(response.getBytes()));
      return source;
    } catch (Exception e) {
      e.printStackTrace();
      throw new WebServiceException("Provider endpoint failed", e);
    }
  }

  private HelloRequest recvBean(Source req) {
    System.out.println("*** recvBean ***");
    HelloRequest helloReq = null;
    try {
      helloReq = (HelloRequest) jaxbContext.createUnmarshaller().unmarshal(req);
      System.out.println("argument=" + helloReq.getArgument());
    } catch (Exception e) {
      System.out.println("Received an exception while parsing the source");
      e.printStackTrace();
    }
    return helloReq;
  }

}
