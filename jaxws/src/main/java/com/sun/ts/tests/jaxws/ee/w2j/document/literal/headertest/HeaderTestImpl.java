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
 * @(#)HeaderTestImpl.java	1.3 04/10/25
 */

package com.sun.ts.tests.jaxws.ee.w2j.document.literal.headertest;

import jakarta.xml.ws.WebServiceException;
import jakarta.jws.WebService;

@WebService(portName = "HeaderTestPort", serviceName = "HeaderTestService", targetNamespace = "http://headertestservice.org/HeaderTestService.wsdl", wsdlLocation = "WEB-INF/wsdl/WSW2JDLHeaderTestService.wsdl", endpointInterface = "com.sun.ts.tests.jaxws.ee.w2j.document.literal.headertest.HeaderTest")

public class HeaderTestImpl implements HeaderTest {

  public ProductOrderResponse submitOrder(ProductOrderRequest poRequest,
      ConfigHeader configHeader) throws BadOrderFault, ConfigFault {
    ProductOrderResponse poResponse = null;
    poResponse = new ProductOrderResponse();
    String testName = configHeader.getTestName();
    ConfigFaultType cft = new ConfigFaultType();
    cft.setMessage(testName);
    cft.setMustUnderstand(true);
    if (testName.equals("GoodOrderTestWithSoapHeaderAndMUFalse")) {
      if (!ValidHeader(configHeader, false, "Config Header", testName))
        throw new ConfigFault("Invalid ConfigHeader: mustUnderstand="
            + configHeader.isMustUnderstand() + ", message="
            + configHeader.getMessage() + ", testName=" + testName, cft);
      poResponse.getItem().addAll(poRequest.getItem());
    } else if (testName.equals("GoodOrderTestWithSoapHeaderAndMUTrue")) {
      if (!ValidHeader(configHeader, true, "Config Header", testName))
        throw new ConfigFault("Invalid ConfigHeader: mustUnderstand="
            + configHeader.isMustUnderstand() + ", message="
            + configHeader.getMessage() + ", testName=" + testName, cft);
      poResponse.getItem().addAll(poRequest.getItem());
    } else if (testName.equals("SoapHeaderFaultTest")) {
      throw new ConfigFault("This is a soap header fault ConfigFault", cft);
    } else if (testName.equals("SoapFaultTest")) {
      BadOrderFaultType bft = new BadOrderFaultType();
      bft.setMessage(testName);
      throw new BadOrderFault("This is a soap fault BadOrderFault", bft);
    } else {
      throw new ConfigFault("Invalid ConfigHeader: mustUnderstand="
          + configHeader.isMustUnderstand() + ", message="
          + configHeader.getMessage() + ", testName=" + testName, cft);
    }
    return poResponse;
  }

  private boolean ValidHeader(ConfigHeader ch, boolean mu, String msg,
      String test) {
    if (ch.isMustUnderstand() == mu && ch.getMessage().equals(msg)
        && ch.getTestName().equals(test))
      return true;
    else
      return false;
  }
}
