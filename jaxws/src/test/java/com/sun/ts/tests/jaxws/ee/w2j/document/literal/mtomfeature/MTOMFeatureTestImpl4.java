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

package com.sun.ts.tests.jaxws.ee.w2j.document.literal.mtomfeature;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import jakarta.jws.WebService;
import jakarta.xml.ws.BindingType;
import jakarta.xml.ws.Holder;
import jakarta.xml.ws.WebServiceException;
import jakarta.xml.ws.soap.SOAPBinding;
import jakarta.xml.ws.soap.MTOM;

import java.awt.Image;
import com.sun.ts.tests.jaxws.common.AttachmentHelper;
import java.net.URL;
import jakarta.activation.DataHandler;
import javax.xml.transform.Source;

@WebService(portName = "MTOMFeatureTest4Port", serviceName = "MTOMFeatureTestService", targetNamespace = "http://mtomfeatureservice.org/wsdl", wsdlLocation = "WEB-INF/wsdl/MTOMFeatureTestService.wsdl", endpointInterface = "com.sun.ts.tests.jaxws.ee.w2j.document.literal.mtomfeature.MTOMFeatureTest4")
@BindingType(value = SOAPBinding.SOAP11HTTP_BINDING)
@MTOM(enabled = false, threshold = 2000)

public class MTOMFeatureTestImpl4 implements MTOMFeatureTest4 {

  public com.sun.ts.tests.jaxws.ee.w2j.document.literal.mtomfeature.DataType33 threshold2000(
      com.sun.ts.tests.jaxws.ee.w2j.document.literal.mtomfeature.DataType3 data) {

    System.out.println("--------------------------");
    System.out.println("In MTOMFeatureTestImpl4:threshold2000");

    DataType33 data33 = new DataType33();
    data33.setTestName(data.getTestName());
    data33.setDoc(data.getDoc());

    return data33;
  }
}
