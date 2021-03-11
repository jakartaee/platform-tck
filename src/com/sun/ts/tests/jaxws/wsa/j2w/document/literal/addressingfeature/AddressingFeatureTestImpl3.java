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
package com.sun.ts.tests.jaxws.wsa.j2w.document.literal.addressingfeature;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import jakarta.jws.WebService;
import jakarta.jws.WebParam;
import jakarta.jws.HandlerChain;
import jakarta.xml.ws.BindingType;
import jakarta.xml.ws.soap.SOAPBinding;
import jakarta.xml.ws.Holder;
import jakarta.xml.ws.Action;
import jakarta.xml.ws.soap.Addressing;

@WebService(name = "AddressingFeatureTest3", portName = "AddressingFeatureTest3Port", targetNamespace = "http://addressingfeatureservice.org/wsdl", serviceName = "AddressingFeatureTest3Service")

@BindingType(value = SOAPBinding.SOAP11HTTP_BINDING)
@Addressing
@HandlerChain(name = "", file = "server-handler.xml")

public class AddressingFeatureTestImpl3 {
  @Action(input = "inputAction", output = "outputAction")
  public int addNumbers3(
      @WebParam(name = "testName", mode = WebParam.Mode.INOUT) Holder<String> testName,
      @WebParam(name = "number1") int number1,
      @WebParam(name = "number2") int number2) {
    if (number1 < 0 || number2 < 0) {
      throw new AddressingFeatureException(
          "One of the numbers received was negative:" + number1 + ", "
              + number2);
    }
    System.out.printf("Adding %s and %s\n", number1, number2);
    return number1 + number2;
  }

}
