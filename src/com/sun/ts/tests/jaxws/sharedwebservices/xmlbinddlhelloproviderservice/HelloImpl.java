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

package com.sun.ts.tests.jaxws.sharedwebservices.xmlbinddlhelloproviderservice;

import jakarta.xml.ws.*;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import java.io.StringReader;
import java.io.OutputStream;
import java.io.InputStream;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.ws.http.HTTPBinding;

import com.sun.ts.tests.jaxws.common.JAXWS_Util;

@WebServiceProvider
@BindingType(value = HTTPBinding.HTTP_BINDING)
@ServiceMode(value = jakarta.xml.ws.Service.Mode.MESSAGE)
public class HelloImpl implements Provider<Source> {
  private static final jakarta.xml.bind.JAXBContext jaxbContext = createJAXBContext();

  private static int combo = -1;

  public jakarta.xml.bind.JAXBContext getJAXBContext() {
    return jaxbContext;
  }

  private static jakarta.xml.bind.JAXBContext createJAXBContext() {
    try {
      return jakarta.xml.bind.JAXBContext.newInstance(
          com.sun.ts.tests.jaxws.sharedwebservices.xmlbinddlhelloproviderservice.ObjectFactory.class);
    } catch (jakarta.xml.bind.JAXBException e) {
      throw new WebServiceException(e.getMessage(), e);
    }
  }

  public Source invoke(Source source) {
    System.out.println("**** Received in Provider Impl ******");
    try {
      recvBean(source);
    } catch (Exception e) {
      e.printStackTrace();
      throw new WebServiceException("Provider endpoint failed", e);
    }
    return sendSource();
  }

  private Source sendSource() {
    System.out.println("*** sendSource ***");
    String body = "<HelloResponse xmlns=\"http://helloservice.org/types\"><argument>foo</argument></HelloResponse>";
    Source source = new StreamSource(new StringReader(body));
    return source;
  }

  private void recvBean(Source req) throws Exception {
    System.out.println("*** recvBean ***");
    HelloRequest helloReq = null;
    try {
      helloReq = (HelloRequest) jaxbContext.createUnmarshaller().unmarshal(req);
      System.out.println("argument=" + helloReq.getArgument());
    } catch (Exception e) {
      System.out.println("Received an exception while parsing the source");
      e.printStackTrace();
      throw e;
    }
  }
}
