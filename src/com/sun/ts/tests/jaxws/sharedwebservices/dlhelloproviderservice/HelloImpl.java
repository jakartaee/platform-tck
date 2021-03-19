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
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.OutputStream;
import java.io.InputStream;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;

import com.sun.ts.tests.jaxws.common.JAXWS_Util;

/*
 * Provider<Source> - req/res a Source in Payload Mode (use of JAXB objects)
 */
@WebServiceProvider(serviceName = "HelloService", portName = "HelloPort", targetNamespace = "http://helloservice.org/wsdl", wsdlLocation = "WEB-INF/wsdl/WSDLHelloProviderService.wsdl")
@BindingType(value = jakarta.xml.ws.soap.SOAPBinding.SOAP11HTTP_BINDING)
@ServiceMode(value = jakarta.xml.ws.Service.Mode.PAYLOAD)
public class HelloImpl implements Provider<Source> {
  private static final jakarta.xml.bind.JAXBContext jaxbContext = createJAXBContext();

  public jakarta.xml.bind.JAXBContext getJAXBContext() {
    return jaxbContext;
  }

  private static jakarta.xml.bind.JAXBContext createJAXBContext() {
    try {
      return jakarta.xml.bind.JAXBContext.newInstance(
          com.sun.ts.tests.jaxws.sharedwebservices.dlhelloproviderservice.ObjectFactory.class);
    } catch (jakarta.xml.bind.JAXBException e) {
      throw new WebServiceException(e.getMessage(), e);
    }
  }

  public Source invoke(Source req) {
    System.out.println("**** in Provider Impl invoke HelloImpl ******");
    HelloRequest helloRequest = null;
    HelloOneWayRequest helloOneWayRequest = null;
    String arg = null;
    boolean oneWay = false;
    try {
      Object request = recvBean(req);
      if (request instanceof HelloRequest) {
        helloRequest = (HelloRequest) request;
        arg = helloRequest.getArgument();
        oneWay = false;
      } else if (request instanceof HelloOneWayRequest) {
        helloOneWayRequest = (HelloOneWayRequest) request;
        arg = helloOneWayRequest.getArgument();
        oneWay = true;
      }
      System.out.println("arg=" + arg);
      if (arg == null)
        throw new WebServiceException("arg is null");
      if (!oneWay) {
        if (arg.equals("sendBean"))
          return sendBean();
        else if (arg.equals("sendSource"))
          return sendSource();
        else if (arg.equals("sendEmptyStreamSource"))
          return sendEmptyStreamSource();
        else
          throw new WebServiceException("Unexpected Argument: " + arg);
      } else {
        if (arg.equals("sendEmptyStreamSource"))
          return null;
        else
          throw new WebServiceException("Unexpected Argument: " + arg);
      }
    } catch (Exception e) {
      e.printStackTrace();
      throw new WebServiceException("Provider endpoint failed", e);
    }
  }

  private Source sendSource() {
    System.out.println("*** sendSource ***");
    String body = "<HelloResponse xmlns=\"http://helloservice.org/types\"><argument>sendSource</argument></HelloResponse>";
    Source source = new StreamSource(new StringReader(body));
    return source;
  }

  private Source sendEmptyStreamSource() {
    System.out.println("*** sendEmptyStreamSource ***");
    Source source = new StreamSource();
    return source;
  }

  private Object recvBean(Source req) {
    System.out.println("*** recvBean ***");
    Object request = null;
    try {
      request = jaxbContext.createUnmarshaller().unmarshal(req);
      // System.out.println("argument="+helloReq.getArgument());
    } catch (Exception e) {
      System.out.println("Received an exception while parsing the source");
      e.printStackTrace();
    }
    return request;
  }

  private Source sendBean() {
    System.out.println("*** sendBean ***");
    StreamSource ss = null;
    HelloResponse helloRes = new HelloResponse();
    helloRes.setArgument("sendBean");
    try {
      Marshaller m = jaxbContext.createMarshaller();
      m.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
      StringWriter writer = new StringWriter();
      m.marshal(helloRes, writer);
      StringReader reader = new StringReader(writer.toString());
      ss = new StreamSource(reader);
    } catch (jakarta.xml.bind.JAXBException jbe) {
      System.out.println("Catch Exception while Marshalling bean:");
      jbe.printStackTrace();
    }
    return ss;
  }
}
