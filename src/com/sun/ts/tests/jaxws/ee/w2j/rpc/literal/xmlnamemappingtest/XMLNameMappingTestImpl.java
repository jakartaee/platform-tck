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

package com.sun.ts.tests.jaxws.ee.w2j.rpc.literal.xmlnamemappingtest;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.tests.jaxws.common.*;

import java.rmi.*;

// Service Implementation Class - as outlined in JAX-WS Specification

import jakarta.jws.WebService;

@WebService(portName = "XMLNameMappingTestPort", serviceName = "xMLNameMappingTest", targetNamespace = "http://XMLNameMappingTest.org/wsdl", wsdlLocation = "WEB-INF/wsdl/WSW2JXMLNameMappingTestService.wsdl", endpointInterface = "com.sun.ts.tests.jaxws.ee.w2j.rpc.literal.xmlnamemappingtest.XMLNameMappingTest")
public class XMLNameMappingTestImpl implements XMLNameMappingTest {
  public EyeColor echoEyeColor(EyeColor inputEyeColor)

  {
    return inputEyeColor;
  }

  public XMLNameMappingTest_Type echoXMLNameMapping(
      XMLNameMappingTest_Type inputXMLNameMappingTest)
      throws XMLNameMappingTest_Exception {
    return inputXMLNameMappingTest;
  }

  public void nameWithdash() {
  }

  public void nameWithunderscore() {
  }

  public void nameWithcolon() {
  }

  public void nameWithdot() {
  }

  public void nullTest() {
  }

  public void trueTest() {
  }

  public void falseTest() {
  }

  public void abstractTest() {
  }

  public void booleanTest() {
  }

  public void breakTest() {
  }

  public void byteTest() {
  }

  public void caseTest() {
  }

  public void catchTest() {
  }

  public void charTest() {
  }

  public void classTest() {
  }

  public void constTest() {
  }

  public void continueTest() {
  }

  public void defaultTest() {
  }

  public void doTest() {
  }

  public void doubleTest() {
  }

  public void elseTest() {
  }

  public void extendsTest() {
  }

  public void finalTest() {
  }

  public void finallyTest() {
  }

  public void floatTest() {
  }

  public void forTest() {
  }

  public void gotoTest() {
  }

  public void ifTest() {
  }

  public void implementsTest() {
  }

  public void importTest() {
  }

  public void instanceofTest() {
  }

  public void intTest() {
  }

  public void interfaceTest() {
  }

  public void longTest() {
  }

  public void nativeTest() {
  }

  public void newTest() {
  }

  public void packageTest() {
  }

  public void privateTest() {
  }

  public void protectedTest() {
  }

  public void publicTest() {
  }

  public void returnTest() {
  }

  public void shortTest() {
  }

  public void staticTest() {
  }

  public void superTest() {
  }

  public void switchTest() {
  }

  public void synchronizedTest() {
  }

  public void thisTest() {
  }

  public void throwTest() {
  }

  public void throwsTest() {
  }

  public void transientTest() {
  }

  public void tryTest() {
  }

  public void voidTest() {
  }

  public void volatileTest() {
  }

  public void whileTest() {
  }
}
