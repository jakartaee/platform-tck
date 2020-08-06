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
 * $Id: SwaRefTestImpl.java 52876 2007-03-12 14:51:28Z af70133 $
 */

package com.sun.ts.tests.jaxws.wsi.w2j.document.literal.swareftest;

import jakarta.xml.ws.WebServiceException;
import jakarta.xml.soap.*;
import jakarta.activation.*;
import javax.xml.transform.stream.*;
import javax.xml.transform.*;
import java.net.*;
import java.awt.*;

import jakarta.jws.WebService;

@WebService(portName = "SwaRefTestPort", serviceName = "WSIDLSwaRefTestService", targetNamespace = "http://SwaRefTestService.org/wsdl", wsdlLocation = "WEB-INF/wsdl/WSW2JDLSwaRefTestService.wsdl", endpointInterface = "com.sun.ts.tests.jaxws.wsi.w2j.document.literal.swareftest.SwaRefTest")

public class SwaRefTestImpl implements SwaRefTest {
  public com.sun.ts.tests.jaxws.wsi.w2j.document.literal.swareftest.SwaRefTypeResponse echoSingleSwaRefAttachment(
      com.sun.ts.tests.jaxws.wsi.w2j.document.literal.swareftest.SwaRefTypeRequest request) {
    System.out.println("Enter echoSingleSwaRefAttachment() ......");
    SwaRefTypeResponse response = new SwaRefTypeResponse();
    response.setAttachment(request.getAttachment());
    System.out.println("Leave echoSingleSwaRefAttachment() ......");
    return response;
  }

  public com.sun.ts.tests.jaxws.wsi.w2j.document.literal.swareftest.SwaRefTypeResponse2 echoMultipleSwaRefAttachments(
      com.sun.ts.tests.jaxws.wsi.w2j.document.literal.swareftest.SwaRefTypeRequest2 request) {
    System.out.println("Enter echoMultipleSwaRefAttachments() ......");
    SwaRefTypeResponse2 response = new SwaRefTypeResponse2();
    response.setAttachment1(request.getAttachment1());
    response.setAttachment2(request.getAttachment2());
    response.setAttachment3(request.getAttachment3());
    System.out.println("Leave echoMultipleSwaRefAttachments() ......");
    return response;
  }
}
