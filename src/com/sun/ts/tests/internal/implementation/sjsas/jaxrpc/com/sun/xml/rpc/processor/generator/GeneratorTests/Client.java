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
 * @(#)Client.java	1.3 04/16/04
 */

package com.sun.ts.tests.internal.implementation.sjsas.jaxrpc.com.sun.xml.rpc.processor.generator.GeneratorTests;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest;
import java.util.Properties;

import com.sun.xml.rpc.processor.generator.*;
import com.sun.xml.rpc.processor.model.Port;
import com.sun.xml.rpc.processor.model.java.*;
import com.sun.xml.rpc.processor.model.soap.*;
import com.sun.xml.rpc.processor.model.literal.*;
import com.sun.xml.rpc.processor.model.*;
import com.sun.xml.rpc.processor.util.ClientProcessorEnvironment;
import com.sun.xml.rpc.processor.util.IndentingWriter;
import com.sun.xml.rpc.processor.config.Configuration;
import com.sun.xml.rpc.wsdl.document.WSDLDocument;
import com.sun.xml.rpc.soap.SOAPVersion;
import com.sun.xml.rpc.util.localization.LocalizableMessage;

import java.io.ByteArrayOutputStream;
import java.io.PipedWriter;

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
   * @testName: testGeneratorException
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testGeneratorException() throws Fault {
    GeneratorException pe = new GeneratorException("string");
    pe.getResourceBundleName();
    pe = new GeneratorException("string", "string");
    final Object o[] = { "string", "string" };
    pe = new GeneratorException("string", o);
    pe = new GeneratorException("string",
        new LocalizableMessage("string", "string"));
  }

  /**
   * @testName: testEnumerationGenerator
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testEnumerationGenerator() throws Fault {
    EnumerationGenerator eg = new EnumerationGenerator();
    try {
      eg.visit(new SOAPCustomType());
    } catch (Exception e) {
    }
    try {
      eg.visit(new SOAPAnyType());
    } catch (Exception e) {
    }
    try {
      eg.visit(new SOAPListType());
    } catch (Exception e) {
    }
  }

  /**
   * @testName: testHolderGenerator
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testHolderGenerator() throws Fault {
    HolderGenerator hg = new HolderGenerator();
    ClientProcessorEnvironment env = new ClientProcessorEnvironment(
        new ByteArrayOutputStream(), null, null);
    Configuration config = new Configuration(env);
    try {
      hg.getGenerator(new Model(), config, new Properties());
    } catch (Exception e) {
    }
    try {
      hg.visit(new SOAPCustomType());
    } catch (Exception e) {
    }
    try {
      hg.visit(new SOAPAnyType());
    } catch (Exception e) {
    }
    try {
      hg.visit(new SOAPListType());
    } catch (Exception e) {
    }
  }

  /**
   * @testName: testInterfaceSerializerGenerator
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testInterfaceSerializerGenerator() throws Fault {
    InterfaceSerializerGenerator isg = new InterfaceSerializerGenerator();
    ClientProcessorEnvironment env = new ClientProcessorEnvironment(
        new ByteArrayOutputStream(), null, null);
    Configuration config = new Configuration(env);
    try {
      isg.getGenerator(new Model(), config, new Properties());
    } catch (Exception e) {
    }
    try {
      isg.preVisitLiteralAllType(new LiteralAllType());
    } catch (Exception e) {
    }
  }

  /**
   * @testName: testLiteralObjectSerializerGenerator
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testLiteralObjectSerializerGenerator() throws Fault {
    LiteralObjectSerializerGenerator losg = new LiteralObjectSerializerGenerator();
    ClientProcessorEnvironment env = new ClientProcessorEnvironment(
        new ByteArrayOutputStream(), null, null);
    Configuration config = new Configuration(env);
    try {
      losg.getGenerator(new Model(), config, new Properties());
    } catch (Exception e) {
    }
  }

  /**
   * @testName: testNames
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testNames() throws Fault {
    Names n = new Names();
    try {
      n.skeletonFor(new JavaInterface());
    } catch (Exception e) {
    }
    n.stripQualifier("string");
    try {
      n.typeClassName(new SOAPCustomType());
    } catch (Exception e) {
    }
    try {
      n.typeObjectArraySerializerClassName("string", new LiteralFragmentType());
    } catch (Exception e) {
    }
    n.isInJavaOrJavaxPackage("string");
    try {
      n.getTypeMemberName(new SOAPCustomType());
    } catch (Exception e) {
    }
    try {
      n.getCustomTypeSerializerMemberName(new SOAPCustomType());
    } catch (Exception e) {
    }
    try {
      n.getCustomTypeDeserializerMemberName(new SOAPCustomType());
    } catch (Exception e) {
    }
    try {
      n.getLiteralFragmentTypeSerializerMemberName(new LiteralFragmentType());
    } catch (Exception e) {
    }
    n.validJavaPackageName("string");
    n.getIDObjectResolverName("string");
    n.getAdjustedURI("string", "string");

  }

  /**
   * @testName: testNames101
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testNames101() throws Fault {
    Names101 n = new Names101();
    try {
      n.holderClassName(new Port(), new JavaSimpleType());
    } catch (Exception e) {
    }

  }

  /**
   * @testName: testNames103
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testNames103() throws Fault {
    Names103 n = new Names103();
    try {
      n.holderClassName(new Port(), new JavaSimpleType());
    } catch (Exception e) {
    }

  }

  /**
   * @testName: testSOAPEncoding
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testSOAPEncoding() throws Fault {
    SOAPEncoding se = new SOAPEncoding();
  }

  /**
   * @testName: testSOAPFaultSerializerGenerator
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testSOAPFaultSerializerGenerator() throws Fault {
    SOAPFaultSerializerGenerator sfsg = new SOAPFaultSerializerGenerator();
    ClientProcessorEnvironment env = new ClientProcessorEnvironment(
        new ByteArrayOutputStream(), null, null);
    Configuration config = new Configuration(env);
    try {
      sfsg.getGenerator(new Model(), config, new Properties());
    } catch (Exception e) {
    }
  }

  /**
   * @testName: testSOAPObjectSerializerGenerator
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testSOAPObjectSerializerGenerator() throws Fault {
    SOAPObjectSerializerGenerator sosg = new SOAPObjectSerializerGenerator();
    ClientProcessorEnvironment env = new ClientProcessorEnvironment(
        new ByteArrayOutputStream(), null, null);
    Configuration config = new Configuration(env);
    try {
      sosg.getGenerator(new Model(), config, new Properties());
    } catch (Exception e) {
    }
    try {
      sosg.writeDetailDoDeserializeMethod(
          new IndentingWriter(new PipedWriter()), new SOAPAnyType());
    } catch (Exception e) {
    }
  }

  /**
   * @testName: testSerializerRegistryGenerator
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testSerializerRegistryGenerator() throws Fault {
    SerializerRegistryGenerator srg = new SerializerRegistryGenerator();
    ClientProcessorEnvironment env = new ClientProcessorEnvironment(
        new ByteArrayOutputStream(), null, null);
    Configuration config = new Configuration(env);
    try {
      srg.getGenerator(new Model(), config, new Properties());
    } catch (Exception e) {
    }
    try {
      srg.visit(new SOAPAnyType());
    } catch (Exception e) {
    }
    try {
      srg.visit(new LiteralFragmentType());
    } catch (Exception e) {
    }
    try {
      srg.preVisitLiteralAllType(new LiteralAllType());
    } catch (Exception e) {
    }
    try {
      srg.visit(new SOAPListType());
    } catch (Exception e) {
    }
  }

  /**
   * @testName: testServletConfigGenerator
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testServletConfigGenerator() throws Fault {
    ServletConfigGenerator srg = new ServletConfigGenerator();
    ClientProcessorEnvironment env = new ClientProcessorEnvironment(
        new ByteArrayOutputStream(), null, null);
    Configuration config = new Configuration(env);
    try {
      srg.getGenerator(new Model(), config, new Properties());
    } catch (Exception e) {
    }
    try {
      srg.getGenerator(new Model(), config, new Properties(),
          SOAPVersion.SOAP_11);
    } catch (Exception e) {
    }
    try {
      srg.visit(new Port());
    } catch (Exception e) {
    }
  }

  /**
   * @testName: testSimpleToBoxedUtil
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testSimpleToBoxedUtil() throws Fault {
    SimpleToBoxedUtil s = new SimpleToBoxedUtil();
    try {
      s.convertExpressionFromTypeToType("a", "b", "c");
    } catch (Exception e) {
    }

  }

  /**
   * @testName: testStubGenerator
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testStubGenerator() throws Fault {
    StubGenerator sg = null;
    sg = new StubGenerator();
    sg = new StubGenerator(SOAPVersion.SOAP_11);
    ClientProcessorEnvironment env = new ClientProcessorEnvironment(
        new ByteArrayOutputStream(), null, null);
    Configuration config = new Configuration(env);
    try {
      sg.getGenerator(new Model(), config, new Properties(),
          SOAPVersion.SOAP_11);
    } catch (Exception e) {
    }

  }

  /**
   * @testName: testTieGenerator
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testTieGenerator() throws Fault {
    TieGenerator tg = null;
    tg = new TieGenerator();
    tg = new TieGenerator(SOAPVersion.SOAP_11);
    ClientProcessorEnvironment env = new ClientProcessorEnvironment(
        new ByteArrayOutputStream(), null, null);
    Configuration config = new Configuration(env);
    try {
      tg.getGenerator(new Model(), config, new Properties(),
          SOAPVersion.SOAP_11);
    } catch (Exception e) {
    }

  }

  /**
   * @testName: testWSDLTypeGenerator
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testWSDLTypeGenerator() throws Fault {
    WSDLTypeGenerator wtg = null;
    wtg = new WSDLTypeGenerator(new Model(), new WSDLDocument(),
        new Properties());
  }

  /**
   * @testName: testCustomClassGenerator
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testCustomClassGenerator() throws Fault {
    CustomClassGenerator ccg = null;
    ccg = new CustomClassGenerator();
    ClientProcessorEnvironment env = new ClientProcessorEnvironment(
        new ByteArrayOutputStream(), null, null);
    Configuration config = new Configuration(env);
    try {
      ccg.getGenerator(new Model(), config, new Properties());
    } catch (Exception e) {
    }
    try {
      ccg.preVisitLiteralAllType(new LiteralAllType());
    } catch (Exception e) {
    }
  }

  /**
   * @testName: testCustomExceptionGenerator
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testCustomExceptionGenerator() throws Fault {
    CustomExceptionGenerator ceg = null;
    ceg = new CustomExceptionGenerator();
    ClientProcessorEnvironment env = new ClientProcessorEnvironment(
        new ByteArrayOutputStream(), null, null);
    Configuration config = new Configuration(env);
    try {
      ceg.getGenerator(new Model(), config, new Properties());
    } catch (Exception e) {
    }
  }

  /**
   * @testName: testEnumerationEncoderGenerator
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testEnumerationEncoderGenerator() throws Fault {
    EnumerationEncoderGenerator eeg = null;
    eeg = new EnumerationEncoderGenerator();
    ClientProcessorEnvironment env = new ClientProcessorEnvironment(
        new ByteArrayOutputStream(), null, null);
    Configuration config = new Configuration(env);
    try {
      eeg.getGenerator(new Model(), config, new Properties());
    } catch (Exception e) {
    }
    try {
      eeg.visit(new SOAPAnyType());
    } catch (Exception e) {
    }
    try {
      eeg.visit(new SOAPListType());
    } catch (Exception e) {
    }
  }

  /**
   * @testName: testFaultExceptionBuilderGenerator
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testFaultExceptionBuilderGenerator() throws Fault {
    FaultExceptionBuilderGenerator febg = null;
    febg = new FaultExceptionBuilderGenerator();
    ClientProcessorEnvironment env = new ClientProcessorEnvironment(
        new ByteArrayOutputStream(), null, null);
    Configuration config = new Configuration(env);
    try {
      febg.getGenerator(new Model(), config, new Properties());
    } catch (Exception e) {
    }
  }

  /**
   * @testName: testLiteralEncoding
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testLiteralEncoding() throws Fault {
    LiteralEncoding le = null;
    le = new LiteralEncoding();
  }

  /**
   * @testName: testSOAPObjectBuilderGenerator
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testSOAPObjectBuilderGenerator() throws Fault {
    SOAPObjectBuilderGenerator sobg = null;
    sobg = new SOAPObjectBuilderGenerator();
    ClientProcessorEnvironment env = new ClientProcessorEnvironment(
        new ByteArrayOutputStream(), null, null);
    Configuration config = new Configuration(env);
    try {
      sobg.getGenerator(new Model(), config, new Properties());
    } catch (Exception e) {
    }
  }

}
