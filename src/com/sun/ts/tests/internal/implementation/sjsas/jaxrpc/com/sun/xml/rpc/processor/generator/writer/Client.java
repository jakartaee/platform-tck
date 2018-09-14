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
 *
 */

package com.sun.ts.tests.internal.implementation.sjsas.jaxrpc.com.sun.xml.rpc.processor.generator.writer;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest;
import java.util.Properties;

import com.sun.xml.rpc.processor.generator.writer.*;
import com.sun.xml.rpc.processor.generator.Names;
import com.sun.xml.rpc.processor.generator.GeneratorConstants;
import com.sun.xml.rpc.processor.model.soap.*;
import com.sun.xml.rpc.processor.model.literal.*;
import com.sun.xml.rpc.processor.model.java.JavaSimpleType;
import com.sun.xml.rpc.processor.util.IndentingWriter;
import com.sun.xml.rpc.wsdl.document.schema.BuiltInTypes;
import com.sun.xml.rpc.encoding.InternalEncodingConstants;
import java.io.PipedWriter;

import javax.xml.namespace.QName;

public class Client extends EETest {

  JavaSimpleType jst = null;

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
    jst = new JavaSimpleType("int", "0");

  }

  public void cleanup() {
    logMsg("cleanup");
  }

  /**
   * @testName: testCollectionSerializerWriter
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testCollectionSerializerWriter() throws Fault {
    CollectionSerializerWriter csw = null;
    csw = new CollectionSerializerWriter(
        new SOAPArrayType(InternalEncodingConstants.QNAME_TYPE_ARRAY_LIST),
        new Names());
    try {
      csw.createSerializer(new IndentingWriter(new PipedWriter()),
          new StringBuffer(), "string", true, true, "string");
    } catch (Exception e) {
    }
    csw.getBaseElementType();
  }

  /**
   * @testName: testCustomSerializerWriter
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testCustomSerializerWriter() throws Fault {
    SOAPAnyType sat = new SOAPAnyType(new QName("string", "string"), jst);
    CustomSerializerWriter csw = null;
    csw = new CustomSerializerWriter(sat, new Names());
    try {
      csw.createSerializer(new IndentingWriter(new PipedWriter()),
          new StringBuffer(), "string", true, true, "string");
    } catch (Exception e) {
    }
    csw.serializerName();
    csw.deserializerName();
  }

  /**
   * @testName: testDynamicSerializerWriter
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testDynamicSerializerWriter() throws Fault {
    SOAPAnyType sat = new SOAPAnyType(new QName("string", "string"), jst);
    DynamicSerializerWriter dsw = null;
    dsw = new DynamicSerializerWriter(sat, new Names());
    try {
      dsw.createSerializer(new IndentingWriter(new PipedWriter()),
          new StringBuffer(), "string", true, true, "string");
    } catch (Exception e) {
    }
    try {
      dsw.declareSerializer(new IndentingWriter(new PipedWriter()), false,
          false);
    } catch (Exception e) {
    }
    dsw.serializerMemberName();
    dsw.deserializerMemberName();
  }

  /**
   * @testName: testLiteralFragmentSerializerWriter
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testLiteralFragmentSerializerWriter() throws Fault {
    LiteralFragmentSerializerWriter lfsw = null;
    LiteralFragmentType lft = new LiteralFragmentType();
    lft.setName(BuiltInTypes.INT);
    lfsw = new LiteralFragmentSerializerWriter(lft, new Names());
    try {
      lfsw.createSerializer(new IndentingWriter(new PipedWriter()),
          new StringBuffer(), "string", true, true, "string");
    } catch (Exception e) {
    }
    try {
      lfsw.declareSerializer(new IndentingWriter(new PipedWriter()), false,
          false);
    } catch (Exception e) {
    }
    lfsw.serializerMemberName();
    lfsw.deserializerMemberName();
  }

  /**
   * @testName: testLiteralInterfaceSerializerWriter
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testLiteralInterfaceSerializerWriter() throws Fault {
    LiteralSimpleType lst = new LiteralSimpleType(new QName("string", "string"),
        jst);
    LiteralInterfaceSerializerWriter lisw = null;
    lisw = new LiteralInterfaceSerializerWriter("javax.xml.rpc", lst,
        new Names());
  }

  /**
   * @testName: testLiteralSequenceSerializerWriter
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testLiteralSequenceSerializerWriter() throws Fault {
    LiteralSequenceSerializerWriter lssw = null;
    lssw = new LiteralSequenceSerializerWriter("javax.xml.rpc",
        new LiteralSimpleType(BuiltInTypes.INT, jst), new Names());
    try {
      lssw.createSerializer(new IndentingWriter(new PipedWriter()),
          new StringBuffer(), "string", true, true, "string");
    } catch (Exception e) {
    }
    lssw.serializerName();
  }

  /**
   * @testName: testSOAPObjectSerializerWriter
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testSOAPObjectSerializerWriter() throws Fault {
    SOAPObjectSerializerWriter sosw = null;

    SOAPAnyType sat = new SOAPAnyType(new QName("string", "string"), jst);
    sosw = new SOAPObjectSerializerWriter("javax.xml.rpc", sat, new Names());
    sosw.serializerName();
    sosw.deserializerName();
  }

  /**
   * @testName: testSimpleTypeSerializerWriter
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testSimpleTypeSerializerWriter() throws Fault {
    SimpleTypeSerializerWriter sosw = null;
    sosw = new SimpleTypeSerializerWriter(
        new SOAPSimpleType(BuiltInTypes.INT, jst), new Names());
    sosw.getTypeEncoder(BuiltInTypes.INT);
    sosw.deserializerName();
  }

}
