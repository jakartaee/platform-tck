/*
 * Copyright (c) 2007, 2018 Oracle and/or its affiliates. All rights reserved.
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
 * $URL$ $LastChangedDate$
 */

package com.sun.ts.tests.internal.implementation.sjsas.jaxrpc.com.sun.xml.rpc.processor.model.java;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest;
import java.util.Properties;

import com.sun.xml.rpc.processor.model.java.*;
import com.sun.xml.rpc.processor.model.Parameter;
import com.sun.xml.rpc.processor.config.TypeMappingInfo;

import java.util.*;

public class Client extends EETest {

  /**
   * Test entry.
   * 
   * @param args
   *          the command-line arguments.
   */
  public static void main(String[] args) {
    Client client = new Client();
    Status status = client.run(args, System.out, System.err);
    status.exit();
  }

  /*
   * @class.setup_props:
   */
  public void setup(String[] args, Properties props) throws Fault {
    logMsg("setup ok");
  }

  public void cleanup() {
    logMsg("cleanup");
  }

  /**
   * @testName: testJavaArrayType
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testJavaArrayType() throws Fault {
    JavaArrayType jat = null;
    jat = new JavaArrayType();
    jat = new JavaArrayType("string", "string", new JavaSimpleType());
    jat.setElementName("string");
    jat.getElementName();
    jat.getElementType();
  }

  /**
   * @testName: testJavaCustomType
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testJavaCustomType() throws Fault {
    JavaCustomType jct = null;
    jct = new JavaCustomType();
    jct = new JavaCustomType("string");
    jct = new JavaCustomType("string", new TypeMappingInfo());
    jct.setTypeMappingInfo(new TypeMappingInfo());
    jct.getTypeMappingInfo();
  }

  /**
   * @testName: testJavaEnumerationEntry
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testJavaEnumerationEntry() throws Fault {
    JavaEnumerationEntry jee = null;
    jee = new JavaEnumerationEntry();
    jee.setValue(new Object());
    jee.getValue();
    jee.setLiteralValue("string");
  }

  /**
   * @testName: testJavaEnumerationType
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testJavaEnumerationType() throws Fault {
    JavaEnumerationType jet = null;
    jet = new JavaEnumerationType();
    jet.setBaseType(new JavaSimpleType());
    jet.getEntriesCount();
    jet.setEntriesList(new Vector());
    jet.getEntriesList();
  }

  /**
   * @testName: testJavaException
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testJavaException() throws Fault {
    JavaException je = null;
    je = new JavaException();
  }

  /**
   * @testName: testJavaInterface
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testJavaInterface() throws Fault {
    JavaInterface ji = null;
    ji = new JavaInterface();
    ji.setFormalName("string");
    ji.getFormalName();
    ji.setRealName("string");
    ji.setImpl("string");
    ji.setMethodsList(new Vector());
    ji.getMethodsList();
    ji.hasInterface("string");
    ji.addInterface("string");
    ji.setInterfacesList(new Vector());
    ji.getInterfacesList();
  }

  /**
   * @testName: testJavaMethod
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testJavaMethod() throws Fault {
    JavaMethod ji = null;
    ji = new JavaMethod();
    ji.setName("string");
    ji.setParametersList(new Vector());
    ji.getParametersList();
    ji.setExceptionsList(new Vector());
    ji.getExceptionsList();
    ji.setDeclaringClass("string");
  }

  /**
   * @testName: testJavaParameter
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testJavaParameter() throws Fault {
    JavaParameter jp = null;
    jp = new JavaParameter();
    jp = new JavaParameter("string", new JavaSimpleType(), new Parameter());
    jp.setName("string");
    jp.setType(new JavaSimpleType());
    jp.setParameter(new Parameter());
    jp.setHolder(true);
    jp.setHolderName("string");
  }

  /**
   * @testName: testJavaSimpleType
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testJavaSimpleType() throws Fault {
    JavaSimpleType jst = null;
    jst = new JavaSimpleType();
    jst.doSetName("string");
    jst.setRealName("string");
    jst.setFormalName("string");
    jst.setPresent(true);
    jst.setInitString("string");
    jst.setHolderName("string");
  }

  /**
   * @testName: testJavaStructureMember
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testJavaStructureMember() throws Fault {
    JavaStructureMember jsm = null;
    jsm = new JavaStructureMember();
    jsm.setName("string");
    jsm.setType(new JavaSimpleType());
    jsm.setPublic(true);
    jsm.setDeclaringClass("string");
    jsm.setOwner(new Object());
  }

  /**
   * @testName: testJavaStructureType
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testJavaStructureType() throws Fault {
    JavaStructureType jst = null;
    jst = new JavaStructureType();
    jst.setMembersList(new Vector());
    jst.getMembersList();
    jst.setAbstract(true);
    JavaStructureType jst2 = new JavaStructureType();
    jst.setSuperclass(jst2);
    jst.addSubclass(jst2);
    HashSet hs = new HashSet();
    jst.setSubclassesSet(hs);
    jst.getSubclassesSet();
  }

}
