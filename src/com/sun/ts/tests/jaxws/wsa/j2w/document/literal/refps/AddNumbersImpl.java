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
 * $Id: AddNumbersImpl.java 51679 2006-10-30 20:53:11Z af70133 $
 */
package com.sun.ts.tests.jaxws.wsa.j2w.document.literal.refps;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import jakarta.jws.WebService;
import jakarta.xml.ws.Action;
import jakarta.jws.WebParam;
import jakarta.xml.ws.BindingType;
import jakarta.xml.ws.soap.Addressing;
import jakarta.xml.ws.soap.SOAPBinding;
import jakarta.jws.HandlerChain;

@WebService(name = "AddNumbersPortType", portName = "AddNumbersPort", serviceName = "AddNumbersService", targetNamespace = "http://example.com")
@HandlerChain(name = "", file = "server-handler.xml")
@BindingType(value = SOAPBinding.SOAP11HTTP_BINDING)
@Addressing(enabled = true, required = true)

public class AddNumbersImpl {
  @Action(input = "addInAction", output = "addOutAction")
  public int addNumbers(
      @WebParam(name = "number1", targetNamespace = "http://example.com") int number1,
      @WebParam(name = "number2", targetNamespace = "http://example.com") int number2)
      throws AddNumbersException {
    System.out
        .println("In addNumbers: number1=" + number1 + ", number2=" + number2);
    return doStuff(number1, number2);
  }

  public int addNumbersAndPassString(
      @WebParam(name = "number1", targetNamespace = "http://example.com") int number1,
      @WebParam(name = "number2", targetNamespace = "http://example.com") int number2,
      @WebParam(name = "testString", targetNamespace = "http://example.com") String testString)
      throws AddNumbersException {
    System.out.println("In addNumbersAndPassString: number1=" + number1
        + ", number2=" + number2 + ", testString=" + testString);
    return doStuff(number1, number2);
  }

  int doStuff(int number1, int number2) throws AddNumbersException {
    if (number1 < 0 || number2 < 0) {
      System.out.println("a negative number, throw AddNumbersException ...");
      throw new AddNumbersException("Negative numbers can't be added!",
          "Numbers: " + number1 + ", " + number2);
    }
    return number1 + number2;
  }
}
