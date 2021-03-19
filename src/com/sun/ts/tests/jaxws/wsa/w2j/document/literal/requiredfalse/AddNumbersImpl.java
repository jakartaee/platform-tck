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

package com.sun.ts.tests.jaxws.wsa.w2j.document.literal.requiredfalse;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import jakarta.jws.WebService;

@WebService(portName = "AddNumbersPort", serviceName = "AddNumbersService", targetNamespace = "http://example.com/", wsdlLocation = "WEB-INF/wsdl/WSAW2JDLRequiredFalseTest.wsdl", endpointInterface = "com.sun.ts.tests.jaxws.wsa.w2j.document.literal.requiredfalse.AddNumbersPortType")
public class AddNumbersImpl {

  public int addNumbers(int number1, int number2)
      throws AddNumbersFault_Exception {
    return doStuff(number1, number2);
  }

  public int addNumbers2(int number1, int number2)
      throws AddNumbersFault_Exception {
    return doStuff(number1, number2);
  }

  public int addNumbers3(int number1, int number2)
      throws AddNumbersFault_Exception {
    return doStuff(number1, number2);
  }

  public int addNumbers4(int number1, int number2)
      throws AddNumbersFault_Exception {
    return doStuff(number1, number2);
  }

  public int addNumbers5(int number1, int number2)
      throws AddNumbersFault_Exception {
    return doStuff(number1, number2);
  }

  public int addNumbers6(int number1, int number2)
      throws AddNumbersFault_Exception {
    return doStuff(number1, number2);
  }

  public int addNumbers7(int number1, int number2)
      throws AddNumbersFault_Exception {
    return doStuff(number1, number2);
  }

  public int addNumbers8(int number1, int number2)
      throws AddNumbersFault_Exception {
    return doStuff(number1, number2);
  }

  int doStuff(int number1, int number2) throws AddNumbersFault_Exception {
    if (number1 < 0 || number2 < 0) {
      ObjectFactory of = new ObjectFactory();
      AddNumbersFault fb = of.createAddNumbersFault();
      fb.setDetail("Negative numbers cant be added!");
      fb.setMessage("Numbers: " + number1 + ", " + number2);
      throw new AddNumbersFault_Exception(fb.getMessage(), fb);
    }
    return number1 + number2;
  }
}
