/*
 * Copyright (c) 2009, 2020 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.webservices13.servlet.WSMTOMFeaturesTestUsingDDs;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import jakarta.jws.WebService;
import jakarta.xml.ws.BindingType;
import jakarta.xml.ws.WebServiceException;
import jakarta.xml.ws.soap.SOAPBinding;
import jakarta.xml.ws.soap.MTOM;

import com.sun.ts.tests.jaxws.common.AttachmentHelper;
import java.net.URL;
import javax.xml.transform.Source;

@WebService(portName = "MTOMTest1Port", serviceName = "MTOMTestService", targetNamespace = "http://mtomservice.org/wsdl", wsdlLocation = "WEB-INF/wsdl/MTOMTestService.wsdl", endpointInterface = "com.sun.ts.tests.webservices13.servlet.WSMTOMFeaturesTestUsingDDs.MTOMTest1")
@BindingType(value = SOAPBinding.SOAP11HTTP_BINDING)
@MTOM(enabled = true)

public class MTOMTestImpl1 implements MTOMTest1 {

  public String mtomIn(
      com.sun.ts.tests.webservices13.servlet.WSMTOMFeaturesTestUsingDDs.DataType data) {
    System.out.println("--------------------------");
    System.out.println("In MTOMTestImpl1:mtomIn");

    String result = "";

    try {

      String docName = data.getDocName();
      System.out.println("docName=" + docName);

      URL docURL = new URL(data.getDocUrl());
      System.out.println("docURL=" + docURL.toString());

      Source doc = AttachmentHelper.getSourceDoc(docURL);
      String tmpRes = AttachmentHelper.validateAttachmentData(doc,
          data.getDoc(), docName);
      if (tmpRes != null) {
        result = result + tmpRes;
      }
    } catch (Exception e) {
      throw new WebServiceException(e.toString());
    }
    return result;
  }

}
