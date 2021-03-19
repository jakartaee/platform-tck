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
 * $Id: AddNumbersImpl3.java 52501 2007-01-24 02:29:49Z adf $
 */

package com.sun.ts.tests.jaxws.wsa.j2w.document.literal.anonymous;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import jakarta.jws.WebService;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.HandlerChain;
import jakarta.xml.ws.Action;
import jakarta.xml.ws.soap.Addressing;
import jakarta.xml.ws.soap.AddressingFeature;

@WebService(name = "AddNumbersPortType3", portName = "AddNumbersPort3", serviceName = "AddNumbersService3", targetNamespace = "http://example.com/")

@Addressing(enabled = true, required = true, responses = AddressingFeature.Responses.ANONYMOUS)
@HandlerChain(name = "", file = "server-handler.xml")
public class AddNumbersImpl3 {

  @Action(input = "http://example.com/AddNumbersPortType3/add", output = "http://example.com/AddNumbersPortType3/addResponse")
  @WebMethod
  public int addNumbers(
      @WebParam(name = "number1", targetNamespace = "http://example.com/") int number1,
      @WebParam(name = "number2", targetNamespace = "http://example.com/") int number2,
      @WebParam(name = "testName", targetNamespace = "http://example.com/") String testName)
      throws AddNumbersException {

    if (number1 < 0 || number2 < 0) {
      throw new AddNumbersException("Negative numbers can't be added!",
          "Numbers: " + number1 + ", " + number2);
    }
    return number1 + number2;
  }
}
