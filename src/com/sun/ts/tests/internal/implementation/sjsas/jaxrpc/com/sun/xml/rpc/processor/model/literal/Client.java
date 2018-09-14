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

package com.sun.ts.tests.internal.implementation.sjsas.jaxrpc.com.sun.xml.rpc.processor.model.literal;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest;
import java.util.Properties;

import com.sun.xml.rpc.processor.model.literal.*;
import com.sun.xml.rpc.processor.model.java.JavaStructureType;
import com.sun.xml.rpc.processor.model.java.JavaStructureMember;
import javax.xml.namespace.QName;
import java.util.*;
import com.sun.xml.rpc.processor.util.ClassNameCollector;

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
   * @testName: testLiteralAllType
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testLiteralAllType() throws Fault {
    LiteralAllType lat = null;
    lat = new LiteralAllType();
    lat = new LiteralAllType(new QName("string", "string"),
        new JavaStructureType());
    try {
      lat.accept(new ClassNameCollector());
    } catch (Exception e) {
    }
  }

  /**
   * @testName: testLiteralArrayType
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testLiteralArrayType() throws Fault {
    LiteralArrayType lat = null;
    lat = new LiteralArrayType();
    lat.setElementType(new LiteralSimpleType());
    lat.getElementType();
    try {
      lat.accept(new ClassNameCollector());
    } catch (Exception e) {
    }
  }

  /**
   * @testName: testLiteralArrayWrapperType
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testLiteralArrayWrapperType() throws Fault {
    LiteralArrayWrapperType lawt = null;
    lawt = new LiteralArrayWrapperType();
    lawt = new LiteralArrayWrapperType(new QName("string", "string"),
        new JavaStructureType());
    try {
      lawt.setElementMembersList(new Vector());
    } catch (Exception e) {
    }
    try {
      lawt.addSubtype(new LiteralSequenceType());
    } catch (Exception e) {
    }
    lawt.setSubtypesSet(new HashSet());
    try {
      lawt.setParentType(new LiteralSequenceType());
    } catch (Exception e) {
    }
    try {
      lawt.setContentMember(new LiteralContentMember());
    } catch (Exception e) {
    }
    try {
      lawt.accept(new ClassNameCollector());
    } catch (Exception e) {
    }
  }

  /**
   * @testName: testLiteralAttributeMember
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testLiteralAttributeMember() throws Fault {
    LiteralAttributeMember lam = null;
    lam = new LiteralAttributeMember();
    lam.setName(new QName("string", "string"));
    lam.setType(new LiteralSimpleType());
    lam.setInherited(true);
    lam.isInherited();
  }

  /**
   * @testName: testLiteralContentMember
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testLiteralContentMember() throws Fault {
    LiteralContentMember jcm = null;
    jcm = new LiteralContentMember();
    jcm.setType(new LiteralSimpleType());
  }

  /**
   * @testName: testLiteralElementMember
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testLiteralElementMember() throws Fault {
    LiteralElementMember lem = null;
    lem = new LiteralElementMember();
    lem.setName(new QName("string", "string"));
    lem.setType(new LiteralSimpleType());
    lem.setInherited(true);
    lem.isInherited();
  }

  /**
   * @testName: testLiteralEnumerationType
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testLiteralEnumerationType() throws Fault {
    LiteralEnumerationType let = null;
    let = new LiteralEnumerationType();
    let.setBaseType(new LiteralSimpleType());
    try {
      let.accept(new ClassNameCollector());
    } catch (Exception e) {
    }
  }

  /**
   * @testName: testLiteralFragmentType
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testLiteralFragmentType() throws Fault {
    LiteralFragmentType lft = null;
    lft = new LiteralFragmentType();
    try {
      lft.accept(new ClassNameCollector());
    } catch (Exception e) {
    }

  }

  /**
   * @testName: testLiteralIDType
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testLiteralIDType() throws Fault {
    LiteralIDType lit = null;
    lit = new LiteralIDType();
    try {
      lit.accept(new ClassNameCollector());
    } catch (Exception e) {
    }
  }

  /**
   * @testName: testLiteralListType
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testLiteralListType() throws Fault {
    LiteralListType llt = null;
    llt = new LiteralListType();
    llt.setItemType(new LiteralSimpleType());
    try {
      llt.accept(new ClassNameCollector());
    } catch (Exception e) {
    }
  }

  /**
   * @testName: testLiteralSequenceType
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testLiteralSequenceType() throws Fault {
    LiteralSequenceType lst = null;
    lst = new LiteralSequenceType();
    lst.setUnwrapped(true);
    lst.isUnwrapped();
    try {
      lst.accept(new ClassNameCollector());
    } catch (Exception e) {
    }
  }

  /**
   * @testName: testLiteralSimpleType
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testLiteralSimpleType() throws Fault {
    LiteralSimpleType lst = null;
    lst = new LiteralSimpleType();
  }

  /**
   * @testName: testLiteralWildcardMember
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testLiteralWildcardMember() throws Fault {
    LiteralWildcardMember lwm = null;
    lwm = new LiteralWildcardMember();
    lwm = new LiteralWildcardMember(new LiteralSimpleType(),
        new JavaStructureMember());
    lwm.setExcludedNamespaceName("string");
    lwm.getExcludedNamespaceName();
    lwm.isWildcard();
  }

  /**
   * @testName: testLiteralAttributeOwningType
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testLiteralAttributeOwningType() throws Fault {
    MyLiteralAttributeOwningType mlaot = new MyLiteralAttributeOwningType();
    ArrayList al = (ArrayList) mlaot.getAttributeMembersList();
    LiteralAttributeMember lam = new LiteralAttributeMember();
    lam.setName(new QName("string", "string"));
    al.add(lam);
    lam = new LiteralAttributeMember();
    lam.setName(new QName("string1", "string1"));
    al.add(lam);
    lam = new LiteralAttributeMember();
    lam.setName(new QName("string2", "string2"));
    al.add(lam);
    mlaot.setAttributeMembersList(al);
    mlaot.getAttributeMemberByName("string");
    mlaot.getAttributeMemberByName("string");

  }

}
