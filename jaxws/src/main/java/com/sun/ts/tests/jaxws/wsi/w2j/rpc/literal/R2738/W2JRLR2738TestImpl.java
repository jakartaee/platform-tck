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
package com.sun.ts.tests.jaxws.wsi.w2j.rpc.literal.R2738;

import jakarta.xml.ws.Holder;

import jakarta.xml.ws.WebServiceException;
import jakarta.jws.WebService;

@WebService(portName = "W2JRLR2738TestPort", serviceName = "W2JRLR2738TestService", targetNamespace = "http://w2jrlr2738testservice.org/W2JRLR2738TestService.wsdl", wsdlLocation = "WEB-INF/wsdl/W2JRLR2738TestService.wsdl", endpointInterface = "com.sun.ts.tests.jaxws.wsi.w2j.rpc.literal.R2738.W2JRLR2738Test")

public class W2JRLR2738TestImpl implements W2JRLR2738Test {
  public void echoIt(String text, ConfigHeader ch1, Holder<ConfigHeader> ghch2,
      Holder<String> ghresult, Holder<ConfigHeader> ghch3) {

    String result = "";
    String expected = "IncludeAllSoapHeadersTest";
    if (text.equals(expected)) {
      if ((ch1.getMessage().equals("ConfigHeader1"))
          && (ghch2.value.getMessage().equals("ConfigHeader2"))) {
        if ((ghch3.value == null)
            || (!ghch3.value.getMessage().equals("ConfigHeader3"))) {
          result = "PASSED";
        } else {
          throw new WebServiceException(
              "FAILED in endpoint - expected configHeader3 not to be ConfigHeader3");
        }
      } else {
        throw new WebServiceException("FAILED in endpoint - ch1="
            + ch1.getMessage() + ", ghch2=" + ghch2.value.getMessage());
      }
    } else {
      throw new WebServiceException(
          "FAILED in endpoint - the body text was not correct: expected="
              + expected + ", got=" + text);
    }
    ch1.setMessage("ConfigHeader11");
    ConfigHeader ch2 = new ConfigHeader();
    ch2.setMessage("ConfigHeader22");
    ghch2.value = ch2;
    ConfigHeader ch3 = new ConfigHeader();
    ch3.setMessage("ConfigHeader33");
    ghch3.value = ch3;
    ghresult.value = result;
  }
}
