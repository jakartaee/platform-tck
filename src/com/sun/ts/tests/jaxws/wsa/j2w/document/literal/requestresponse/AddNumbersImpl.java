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
 * $Id: AddNumbersImpl.java 52501 2007-01-24 02:29:49Z lschwenk $
 */
package com.sun.ts.tests.jaxws.wsa.j2w.document.literal.requestresponse;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import jakarta.jws.WebService;
import jakarta.jws.WebParam;
import jakarta.jws.HandlerChain;
import jakarta.xml.ws.soap.Addressing;
import jakarta.xml.ws.Action;
import jakarta.xml.ws.Holder;

import com.sun.ts.tests.jaxws.wsa.common.AddressingHeaderException;

@WebService(name = "AddNumbersPortType", portName = "AddNumbersPort", serviceName = "AddNumbersService", targetNamespace = "http://example.com")
@HandlerChain(file = "server-handler.xml")
@Addressing(enabled = true, required = true)
public class AddNumbersImpl {

  public int addNumbers(@WebParam(name = "number1") int number1,
      @WebParam(name = "number2") int number2,
      @WebParam(name = "testName", mode = WebParam.Mode.INOUT) Holder<String> testName) {
    if (number1 < 0 || number2 < 0) {
      throw new AddressingHeaderException(
          "One of the numbers received was negative:" + number1 + ", "
              + number2);
    }
    System.out.printf("Adding %s and %s\n", number1, number2);
    return number1 + number2;
  }

  @Action(input = "inputAction", output = "outputAction")
  public int addNumbers2(@WebParam(name = "number1") int number1,
      @WebParam(name = "number2") int number2,
      @WebParam(name = "testName", mode = WebParam.Mode.INOUT) Holder<String> testName) {
    if (number1 < 0 || number2 < 0) {
      throw new AddressingHeaderException(
          "One of the numbers received was negative:" + number1 + ", "
              + number2);
    }
    System.out.printf("Adding %s and %s\n", number1, number2);
    return number1 + number2;
  }
}
