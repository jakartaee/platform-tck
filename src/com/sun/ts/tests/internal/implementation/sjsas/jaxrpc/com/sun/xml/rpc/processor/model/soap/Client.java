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

package com.sun.ts.tests.internal.implementation.sjsas.jaxrpc.com.sun.xml.rpc.processor.model.soap;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest;
import java.util.Properties;

import com.sun.xml.rpc.processor.model.soap.*;
import javax.xml.namespace.QName;
import com.sun.xml.rpc.soap.SOAPVersion;
import com.sun.xml.rpc.processor.model.java.JavaStructureType;
import com.sun.xml.rpc.processor.model.java.JavaStructureMember;
import com.sun.xml.rpc.processor.model.java.JavaSimpleType;
import com.sun.xml.rpc.processor.model.ModelException;
import java.util.*;
import com.sun.xml.rpc.processor.util.ClassNameCollector;
import com.sun.xml.rpc.processor.util.IndentingWriter;
import java.io.*;

import com.sun.xml.rpc.processor.model.soap.MySOAPType;

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
   * @testName: testRPCRequestOrderedStructureType
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testRPCRequestOrderedStructureType() throws Fault {
    RPCRequestOrderedStructureType rrost = null;
    rrost = new RPCRequestOrderedStructureType();
    rrost = new RPCRequestOrderedStructureType(new QName("string", "string"));
    rrost = new RPCRequestOrderedStructureType(new QName("string", "string"),
        SOAPVersion.SOAP_11);
    try {
      rrost.accept(new ClassNameCollector());
    } catch (Exception e) {
    }

  }

  /**
   * @testName: testRPCRequestUnorderedStructureType
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testRPCRequestUnorderedStructureType() throws Fault {
    RPCRequestUnorderedStructureType rrust = null;
    rrust = new RPCRequestUnorderedStructureType();
    try {
      rrust.accept(new ClassNameCollector());
    } catch (Exception e) {
    }

  }

  /**
   * @testName: testRPCResponseStructureType
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testRPCResponseStructureType() throws Fault {
    RPCResponseStructureType rrst = null;
    rrst = new RPCResponseStructureType();
    try {
      rrst.accept(new ClassNameCollector());
    } catch (Exception e) {
    }

  }

  /**
   * @testName: testSOAPAnyType
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testSOAPAnyType() throws Fault {
    SOAPAnyType sat = null;
    sat = new SOAPAnyType();
    sat = new SOAPAnyType(new QName("string", "string"));
    sat = new SOAPAnyType(new QName("string", "string"),
        new JavaStructureType("string", true, "string"), SOAPVersion.SOAP_11);
    try {
      sat.accept(new ClassNameCollector());
    } catch (Exception e) {
    }

  }

  /**
   * @testName: testSOAPArrayType
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testSOAPArrayType() throws Fault {
    SOAPArrayType sat = null;
    sat = new SOAPArrayType();
    sat = new SOAPArrayType(new QName("string", "string"),
        new QName("string", "string"), new SOAPSimpleType(),
        new JavaStructureType("string", true, "string"));
    try {
      sat.accept(new ClassNameCollector());
    } catch (Exception e) {
    }

  }

  /**
   * @testName: testSOAPAttributeMember
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testSOAPAttributeMember() throws Fault {
    SOAPAttributeMember sam = null;
    sam = new SOAPAttributeMember();
    sam = new SOAPAttributeMember(new QName("string", "string"),
        new SOAPSimpleType());
    sam = new SOAPAttributeMember(new QName("string", "string"),
        new SOAPSimpleType(), new JavaStructureMember());
    sam.setName(new QName("string", "string"));
    sam.getName();
    sam.setType(new SOAPSimpleType());
    sam.getType();
    sam.setJavaStructureMember(new JavaStructureMember());
    sam.getJavaStructureMember();
    sam.setRequired(true);
    sam.isRequired();
    sam.setInherited(true);
    sam.isInherited();
  }

  /**
   * @testName: testSOAPCustomType
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testSOAPCustomType() throws Fault {
    SOAPCustomType sct = null;
    sct = new SOAPCustomType();
    sct = new SOAPCustomType(new QName("string", "string"));
    sct = new SOAPCustomType(new QName("string", "string"),
        SOAPVersion.SOAP_11);
    try {
      sct.accept(new ClassNameCollector());
    } catch (Exception e) {
    }

  }

  /**
   * @testName: testSOAPElementMember
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testSOAPElementMember() throws Fault {
    SOAPElementMember sem = null;
    sem = new SOAPElementMember();
    sem = new SOAPElementMember(new QName("string", "string"),
        new SOAPSimpleType());
    sem = new SOAPElementMember(new QName("string", "string"),
        new SOAPSimpleType(), new JavaStructureMember());
    sem.setName(new QName("string", "string"));
    sem.getName();
    sem.setType(new SOAPSimpleType());
    sem.getType();
    sem.setNillable(true);
    sem.isNillable();
    sem.setRequired(true);
    sem.isRequired();
    sem.setRepeated(true);
    sem.isRepeated();
    sem.setJavaStructureMember(new JavaStructureMember());
    sem.getJavaStructureMember();
    sem.isWildcard();
    sem.setInherited(true);
    sem.isInherited();
  }

  /**
   * @testName: testSOAPEnumerationType
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testSOAPEnumerationType() throws Fault {
    SOAPEnumerationType set = null;
    set = new SOAPEnumerationType();
    set.setBaseType(new SOAPSimpleType());
    try {
      set.accept(new ClassNameCollector());
    } catch (Exception e) {
    }

  }

  /**
   * @testName: testSOAPListType
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testSOAPListType() throws Fault {
    SOAPListType slt = null;
    slt = new SOAPListType();
    slt = new SOAPListType(new QName("string", "string"), new SOAPSimpleType(),
        new JavaStructureType("string", true, "string"));
    slt.setItemType(new SOAPSimpleType());
    slt.getItemType();
    try {
      slt.accept(new ClassNameCollector());
    } catch (Exception e) {
    }

  }

  /**
   * @testName: testSOAPOrderedStructureType
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testSOAPOrderedStructureType() throws Fault {
    SOAPOrderedStructureType sost = null;
    sost = new SOAPOrderedStructureType();
    try {
      sost.accept(new ClassNameCollector());
    } catch (Exception e) {
    }

  }

  /**
   * @testName: testSOAPSimpleType
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testSOAPSimpleType() throws Fault {
    SOAPSimpleType sst = null;
    sst = new SOAPSimpleType();
    sst = new SOAPSimpleType(new QName("string", "string"),
        new JavaSimpleType(), SOAPVersion.SOAP_11);
    try {
      sst.accept(new ClassNameCollector());
    } catch (Exception e) {
    }

  }

  /**
   * @testName: testSOAPStructureMember
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testSOAPStructureMember() throws Fault {
    SOAPStructureMember ssm = null;
    ssm = new SOAPStructureMember();
    ssm.setName(new QName("string", "string"));
    ssm.setType(new SOAPSimpleType());
  }

  /**
   * @testName: testSOAPUnorderedStructureType
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testSOAPUnorderedStructureType() throws Fault {
    SOAPUnorderedStructureType ssm = null;
    ssm = new SOAPUnorderedStructureType();
    try {
      ssm.accept(new ClassNameCollector());
    } catch (Exception e) {
    }

  }

  /**
   * @testName: testSOAPAttributeOwningType
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testSOAPAttributeOwningType() throws Fault {
    MySOAPAttributeOwningType msaot = new MySOAPAttributeOwningType();
    ArrayList al = (ArrayList) msaot.getAttributeMembersList();
    SOAPAttributeMember sam = new SOAPAttributeMember();
    sam.setName(new QName("string", "string"));
    al.add(sam);
    sam = new SOAPAttributeMember();
    sam.setName(new QName("string1", "string1"));
    al.add(sam);
    sam = new SOAPAttributeMember();
    sam.setName(new QName("string2", "string2"));
    al.add(sam);
    msaot.setAttributeMembersList(al);
    msaot.getAttributeMemberByName("string");
    msaot.getAttributeMemberByName("string");

    sam = new SOAPAttributeMember();
    sam.setName(new QName("string3", "string3"));
    msaot.add(sam);
    try {
      msaot.add(sam);
    } catch (ModelException me) {
    }
  }

  /**
   * @testName: testSOAPStructureType
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testSOAPStructureType() throws Fault {
    MySOAPStructureType msst = new MySOAPStructureType();
    ArrayList al = (ArrayList) msst.getAttributeMembersList();
    SOAPStructureMember ssm = new SOAPStructureMember();
    ssm.setName(new QName("string", "string"));
    al.add(ssm);
    ssm = new SOAPStructureMember();
    ssm.setName(new QName("string1", "string1"));
    al.add(ssm);
    ssm = new SOAPStructureMember();
    ssm.setName(new QName("string2", "string2"));
    al.add(ssm);
    msst.setMembersList(al);
    msst.getMemberByName(new QName("string", "string"));
    msst.getMemberByName(new QName("string1", "string1"));
  }

  /**
   * @testName: testSOAPType
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testSOAPType() throws Fault {
    MySOAPType mst = new MySOAPType(new QName("string", "string"));
  }

}
