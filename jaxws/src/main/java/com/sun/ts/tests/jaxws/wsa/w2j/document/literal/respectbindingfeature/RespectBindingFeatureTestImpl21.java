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
 * $Id: RespectBindingFeatureTestImpl21.java 52501 2007-01-24 02:29:49Z lschwenk $
 */
package com.sun.ts.tests.jaxws.wsa.w2j.document.literal.respectbindingfeature;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import jakarta.jws.WebService;
import jakarta.xml.ws.BindingType;
import jakarta.xml.ws.soap.SOAPBinding;
import jakarta.xml.ws.Holder;
import jakarta.xml.ws.RespectBinding;

@WebService(portName = "RespectBindingFeatureTest21Port", serviceName = "RespectBindingFeatureTestService", targetNamespace = "http://respectbindingfeatureservice.org/wsdl", wsdlLocation = "WEB-INF/wsdl/RespectBindingFeatureTestService.wsdl", endpointInterface = "com.sun.ts.tests.jaxws.wsa.w2j.document.literal.respectbindingfeature.RespectBindingFeatureTest21")
@BindingType(value = SOAPBinding.SOAP11HTTP_BINDING)
@RespectBinding(enabled = false)

public class RespectBindingFeatureTestImpl21
    implements RespectBindingFeatureTest21 {

  public int addNumbers(Holder<String> testname, int number1, int number2) {
    if (number1 < 0 || number2 < 0) {
      throw new RespectBindingFeatureException(
          "One of the numbers received was negative:" + number1 + ", "
              + number2);
    }
    System.out.printf("Adding %s and %s\n", number1, number2);
    return number1 + number2;
  }

}
