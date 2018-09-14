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

package com.sun.ts.tests.internal.implementation.sjsas.jaxrpc.com.sun.xml.rpc.processor.model.ModelTests;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest;
import java.util.Properties;

import com.sun.xml.rpc.processor.model.*;
import com.sun.xml.rpc.processor.model.java.JavaInterface;
import com.sun.xml.rpc.processor.model.literal.LiteralSimpleType;
import com.sun.xml.rpc.util.localization.LocalizableMessage;
import com.sun.xml.rpc.soap.SOAPVersion;
import javax.xml.namespace.QName;
import com.sun.xml.rpc.processor.generator.SOAPFaultSerializerGenerator;

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
   * @testName: testAbstractType
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testAbstractType() throws Fault {
    LiteralSimpleType lst = null;
    lst = new LiteralSimpleType();
    lst.setPropertiesMap(new HashMap());
    lst.isNillable();
    lst.removeProperty("string");
    lst.getProperties();
  }

  /**
   * @testName: testModelException
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testModelException() throws Fault {
    ModelException me = null;
    me = new ModelException("string");
    me = new ModelException("string");
    me.getResourceBundleName();
    me = new ModelException("string", "string");
    final Object o[] = { "string", "string" };
    me = new ModelException("string", o);
    me = new ModelException("string",
        new LocalizableMessage("string", "string"));
  }

  /**
   * @testName: testBlock
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testBlock() throws Fault {
    Block b = null;
    b = new Block();
    b = new Block(new QName("string", "string"));
    b.setName(new QName("string", "string"));
    try {
      b.accept(new SOAPFaultSerializerGenerator());
    } catch (Exception e) {
    }
  }

  /**
   * @testName: testFault
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testFault() throws Fault {
    com.sun.xml.rpc.processor.model.Fault f = null;
    f = new com.sun.xml.rpc.processor.model.Fault();
    f.setName("string");
    com.sun.xml.rpc.processor.model.Fault f2 = new com.sun.xml.rpc.processor.model.Fault();
    f.setParentFault(f2);
    f.addSubfault(f2);
    f.getSubfaultsSet();
    HashSet hs = new HashSet();
    f.setSubfaultsSet(hs);
    f.getAllFaults();
    try {
      f.accept(new SOAPFaultSerializerGenerator());
    } catch (Exception e) {
    }
  }

  /**
   * @testName: testHeaderFault
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testHeaderFault() throws Fault {
    com.sun.xml.rpc.processor.model.HeaderFault f = null;
    f = new com.sun.xml.rpc.processor.model.HeaderFault();
  }

  /**
   * @testName: testOperation
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testOperation() throws Fault {
    Operation o = null;
    o = new Operation();
    QName q = new QName("string", "string");
    o.setName(q);
    o.setUniqueName("string");
    HashSet hs = new HashSet();
    o.setFaultsSet(hs);
    o.getFaultCount();
    try {
      o.accept(new SOAPFaultSerializerGenerator());
    } catch (Exception e) {
    }
  }

  /**
   * @testName: testParameter
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testParameter() throws Fault {
    Parameter p = null;
    p = new Parameter();
    p.setName("string");
    try {
      p.accept(new SOAPFaultSerializerGenerator());
    } catch (Exception e) {
    }
  }

  /**
   * @testName: testPort
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testPort() throws Fault {
    Port p = null;
    p = new Port();
    QName q = new QName("string", "string");
    p.setName(q);
    p.getOperationByUniqueName("string");
    p.setSOAPVersion(SOAPVersion.SOAP_11);
    try {
      p.accept(new SOAPFaultSerializerGenerator());
    } catch (Exception e) {
    }
  }

  /**
   * @testName: testResponse
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testResponse() throws Fault {
    Response r = null;
    r = new Response();
    r.getFaultBlocks();
    r.getFaultBlockCount();
    r.getFaultBlocksMap();
    HashMap hm = new HashMap();
    r.setFaultBlocksMap(hm);
    try {
      r.accept(new SOAPFaultSerializerGenerator());
    } catch (Exception e) {
    }
  }

  /**
   * @testName: testService
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testService() throws Fault {
    Service s = null;
    s = new Service();
    QName q = new QName("string", "string");
    s.setName(q);
    s.getPortByName(q);
    Vector v = new Vector();
    s.setPortsList(v);
    s.getPortsList();
    JavaInterface ji = new JavaInterface();
    s.setJavaInterface(ji);
    try {
      s.accept(new SOAPFaultSerializerGenerator());
    } catch (Exception e) {
    }
  }

  /**
   * @testName: testModelObject
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testModelObject() throws Fault {
    MyModelObject mmo = new MyModelObject();
    mmo.getProperties();
    HashMap hm = new HashMap();
    hm.put("string", "string");
    mmo.setPropertiesMap(hm);
    mmo.getProperties();
    mmo.removeProperty("string");
    mmo.removeProperty(null);
  }

}
