/*
 * Copyright (c) 2007, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.webservices12.servlet.WSMTOMSBFullDDsTest;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import javax.xml.ws.WebServiceException;
import javax.jws.WebService;

import javax.xml.ws.Holder;
import java.awt.Image;
import com.sun.ts.tests.jaxws.common.AttachmentHelper;
import java.net.URL;
import javax.activation.DataHandler;
import javax.xml.transform.Source;

@WebService(portName = "MTOMTestTwoPort", serviceName = "MTOMTestService", targetNamespace = "http://mtomtestservice.org/wsdl", wsdlLocation = "WEB-INF/wsdl/MTOMTestService.wsdl", endpointInterface = "com.sun.ts.tests.webservices12.servlet.WSMTOMSBFullDDsTest.MTOMTestTwo")

public class MTOMTestTwoImpl implements MTOMTestTwo {

  public String mtomIn2(
      com.sun.ts.tests.webservices12.servlet.WSMTOMSBFullDDsTest.DataType3 data) {
    System.out.println("--------------------------");
    System.out.println("In mtomIn2");

    return "PASSED";
  }

}
