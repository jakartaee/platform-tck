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

package com.sun.ts.tests.jaxws.wsa.j2w.document.literal.action;

import jakarta.annotation.Resource;
import jakarta.jws.HandlerChain;
import jakarta.jws.WebService;
import jakarta.jws.WebMethod;
import jakarta.xml.ws.WebServiceContext;
import jakarta.xml.ws.Action;
import jakarta.xml.ws.FaultAction;
import jakarta.xml.ws.BindingType;
import jakarta.xml.ws.soap.Addressing;
import jakarta.xml.ws.soap.SOAPBinding;

@HandlerChain(name = "", file = "server-handler.xml")
@WebService(name = "AddNumbers", portName = "AddNumbersPort", targetNamespace = "http://foobar.org/", serviceName = "AddNumbersService")
@BindingType(value = SOAPBinding.SOAP11HTTP_BINDING)
@Addressing(enabled = true, required = true)
public class AddNumbersImpl {

  @Resource
  WebServiceContext wsc;

  public int addNumbersNoAction(int number1, int number2)
      throws AddNumbersException {
    return doStuff(number1, number2);
  }

  @Action(input = "", output = "")
  public int addNumbersEmptyAction(int number1, int number2)
      throws AddNumbersException {
    return doStuff(number1, number2);
  }

  // Specify input action via the WebMethod annotation and the Action annotation
  // (MUST WORK)
  @Action(input = "http://example.com/input", output = "http://example.com/output")
  @WebMethod(action = "http://example.com/input")
  public int addNumbers(int number1, int number2) throws AddNumbersException {
    return doStuff(number1, number2);
  }

  @Action(input = "http://example.com/input2", output = "http://example.com/output2")
  public int addNumbers2(int number1, int number2) throws AddNumbersException {
    return doStuff(number1, number2);
  }

  // Specify input action via the WebMethod annotation only
  @WebMethod(action = "http://example.com/input3")
  public int addNumbers3(int number1, int number2) throws AddNumbersException {
    return doStuff(number1, number2);
  }

  @Action(input = "http://example.com/input4")
  public int addNumbers4(int number1, int number2) throws AddNumbersException {
    return doStuff(number1, number2);
  }

  @Action(input = "finput1", output = "foutput1", fault = {
      @FaultAction(className = AddNumbersException.class, value = "http://fault1") })
  public int addNumbersFault1(int number1, int number2)
      throws AddNumbersException {
    return doStuff(number1, number2);
  }

  @Action(input = "finput2", output = "foutput2", fault = {
      @FaultAction(className = AddNumbersException.class, value = "http://fault2/addnumbers"),
      @FaultAction(className = TooBigNumbersException.class, value = "http://fault2/toobignumbers") })
  public int addNumbersFault2(int number1, int number2)
      throws AddNumbersException, TooBigNumbersException {
    throwTooBigException(number1, number2);

    return doStuff(number1, number2);
  }

  @Action(input = "finput3", output = "foutput3", fault = {
      @FaultAction(className = AddNumbersException.class, value = "http://fault3/addnumbers") })
  public int addNumbersFault3(int number1, int number2)
      throws AddNumbersException, TooBigNumbersException {
    throwTooBigException(number1, number2);

    return doStuff(number1, number2);
  }

  @Action(fault = {
      @FaultAction(className = AddNumbersException.class, value = "http://fault4/addnumbers") })
  public int addNumbersFault4(int number1, int number2)
      throws AddNumbersException, TooBigNumbersException {
    throwTooBigException(number1, number2);

    return doStuff(number1, number2);
  }

  @Action(fault = {
      @FaultAction(className = TooBigNumbersException.class, value = "http://fault5/toobignumbers") })
  public int addNumbersFault5(int number1, int number2)
      throws AddNumbersException, TooBigNumbersException {
    throwTooBigException(number1, number2);

    return doStuff(number1, number2);
  }

  @Action(fault = {
      @FaultAction(className = AddNumbersException.class, value = "http://fault6/addnumbers"),
      @FaultAction(className = TooBigNumbersException.class, value = "http://fault6/toobignumbers") })
  public int addNumbersFault6(int number1, int number2)
      throws AddNumbersException, TooBigNumbersException {
    throwTooBigException(number1, number2);

    return doStuff(number1, number2);
  }

  @Action(fault = {
      @FaultAction(className = AddNumbersException.class, value = ""),
      @FaultAction(className = TooBigNumbersException.class, value = "") })
  public int addNumbersFault7(int number1, int number2)
      throws AddNumbersException, TooBigNumbersException {
    throwTooBigException(number1, number2);

    return doStuff(number1, number2);
  }

  int doStuff(int number1, int number2) throws AddNumbersException {
    if (number1 < 0 || number2 < 0) {
      throw new AddNumbersException("Negative numbers can't be added!",
          "Numbers: " + number1 + ", " + number2);
    }
    return number1 + number2;
  }

  void throwTooBigException(int number1, int number2)
      throws TooBigNumbersException {
    if (number1 > 10 || number2 > 10)
      throw new TooBigNumbersException("Too big numbers can't be added!",
          "Numbers: " + number1 + ", " + number2);
  }

}
