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

package com.sun.ts.tests.jaxws.wsi.w2j.rpc.literal.R2751;

import jakarta.xml.ws.WebServiceException;
import jakarta.jws.WebService;

@WebService(portName = "W2JRLR2751TestPort", serviceName = "W2JRLR2751TestService", targetNamespace = "http://w2jrlr2751testservice.org/W2JRLR2751TestService.wsdl", wsdlLocation = "WEB-INF/wsdl/W2JRLR2751TestService.wsdl", endpointInterface = "com.sun.ts.tests.jaxws.wsi.w2j.rpc.literal.R2751.W2JRLR2751Test")

public class W2JRLR2751TestImpl implements W2JRLR2751Test {
  public String echoIt(String item, ConfigHeader ch, ConfigHeader2 ch2,
      ConfigHeader3 ch3) {

    System.out.println("*** in W2JRLR2751TestImpl ***");
    String result = null;
    if ((ch.getMessage().equals("ConfigHeader"))
        && (ch2.getMessage().equals("ConfigHeader2"))
        && (ch3.getMessage().equals("ConfigHeader3"))) {
      result = "PASSED";
    } else {
      throw new WebServiceException(
          "FAILED - configHeader=" + ch.getMessage() + ", configHeader2="
              + ch2.getMessage() + ", configHeader3=" + ch3.getMessage());
    }
    return result;
  }
}
