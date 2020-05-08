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

package com.sun.ts.tests.webservices12.ejb.annotations.WSEjbWebServiceProviderTest;

import jakarta.ejb.Stateless;
import jakarta.jws.WebService;
import javax.xml.ws.Provider;
import javax.xml.ws.WebServiceProvider;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.ws.WebServiceException;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;

@WebServiceProvider(portName = "HelloPort", serviceName = "HelloService", targetNamespace = "http://hello.org/wsdl", wsdlLocation = "META-INF/wsdl/HelloService.wsdl")
@Stateless(name = "WSEjbWebServiceProviderTest")
public class HelloBean implements Provider<Source> {
  private static final JAXBContext jaxbContext = createJAXBContext();

  public javax.xml.bind.JAXBContext getJAXBContext() {
    return jaxbContext;
  }

  private static javax.xml.bind.JAXBContext createJAXBContext() {
    try {
      return javax.xml.bind.JAXBContext.newInstance(ObjectFactory.class);
    } catch (javax.xml.bind.JAXBException e) {
      throw new WebServiceException(e.getMessage(), e);
    }
  }

  public Source invoke(Source request) {
    try {
      recvBean(request);
      return sendBean();
    } catch (Exception e) {
      e.printStackTrace();
      throw new WebServiceException("Provider endpoint failed", e);
    }
  }

  private void recvBean(Source source) throws Exception {
    System.out.println("**** recvBean ******");
    JAXBElement element = (JAXBElement) jaxbContext.createUnmarshaller()
        .unmarshal(source);
    System.out
        .println("name=" + element.getName() + " value=" + element.getValue());
    if (element.getValue() instanceof SayHello) {
      SayHello hello = (SayHello) element.getValue();
      System.out.println("Say Hello from " + hello.getArg0());
    }

  }

  private Source sendBean() throws Exception {
    System.out.println("**** sendBean ******");
    SayHelloResponse resp = new SayHelloResponse();
    resp.setReturn("WSEjbWebServiceProvider-SayHello");
    ByteArrayOutputStream bout = new ByteArrayOutputStream();
    ObjectFactory factory = new ObjectFactory();
    Marshaller m = jaxbContext.createMarshaller();
    m.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
    m.marshal(factory.createSayHelloResponse(resp), bout);
    return new StreamSource(new ByteArrayInputStream(bout.toByteArray()));
  }
}
