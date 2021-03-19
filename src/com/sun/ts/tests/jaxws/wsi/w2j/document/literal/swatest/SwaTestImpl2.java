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

package com.sun.ts.tests.jaxws.wsi.w2j.document.literal.swatest;

import jakarta.xml.ws.WebServiceException;
import jakarta.xml.soap.*;
import jakarta.activation.*;
import javax.xml.transform.stream.*;
import javax.xml.transform.*;
import java.net.*;
import java.awt.*;
import jakarta.jws.WebService;

@WebService(portName = "SwaTestTwoPort", serviceName = "WSIDLSwaTestService", targetNamespace = "http://SwaTestService.org/wsdl", wsdlLocation = "WEB-INF/wsdl/WSW2JDLSwaTestService.wsdl", endpointInterface = "com.sun.ts.tests.jaxws.wsi.w2j.document.literal.swatest.SwaTest2")

public class SwaTestImpl2 implements SwaTest2 {
  public com.sun.ts.tests.jaxws.wsi.w2j.document.literal.swatest.OutputResponseString putMultipleAttachments(
      com.sun.ts.tests.jaxws.wsi.w2j.document.literal.swatest.InputRequestPut request,
      jakarta.activation.DataHandler attach1,
      jakarta.activation.DataHandler attach2) {
    try {
      OutputResponseString theResponse = new OutputResponseString();
      theResponse.setMyString("ok");
      System.out.println("Enter putMultipleAttachments() ......");
      if (attach1 == null) {
        System.err.println("attach1 is null (unexpected)");
        theResponse.setMyString("not ok");
      }
      if (attach2 == null) {
        System.err.println("attach2 is null (unexpected)");
        theResponse.setMyString("not ok");
      }
      System.out.println("Leave putMultipleAttachments() ......");
      return theResponse;
    } catch (Exception e) {
      throw new WebServiceException(e.getMessage());
    }
  }

  public com.sun.ts.tests.jaxws.wsi.w2j.document.literal.swatest.OutputResponseString echoNoAttachments(
      com.sun.ts.tests.jaxws.wsi.w2j.document.literal.swatest.InputRequestString request) {
    try {
      System.out.println("Enter echoNoAttachments() ......");
      OutputResponseString theResponse = new OutputResponseString();
      theResponse.setMyString(request.getMyString());
      System.out.println("Leave echoNoAttachments() ......");
      return theResponse;
    } catch (Exception e) {
      throw new WebServiceException(e.getMessage());
    }
  }
}
