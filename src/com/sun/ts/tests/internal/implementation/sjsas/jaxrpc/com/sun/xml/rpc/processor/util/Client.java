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
 * $Id$
 */

package com.sun.ts.tests.internal.implementation.sjsas.jaxrpc.com.sun.xml.rpc.processor.util;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest;
import java.util.Properties;

import com.sun.xml.rpc.processor.schema.*;
import com.sun.xml.rpc.processor.util.*;
import com.sun.xml.rpc.processor.util.IndentingWriter;
import com.sun.xml.rpc.processor.model.*;
import com.sun.xml.rpc.processor.model.literal.*;
import com.sun.xml.rpc.processor.model.soap.*;
import com.sun.xml.rpc.util.xml.CDATA;
import com.sun.xml.rpc.util.localization.LocalizableMessage;

import javax.xml.namespace.QName;

import java.io.*;
import java.net.URL;

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
   * @testName: testCanonicalModelWriter
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testCanonicalModelWriter() throws Fault {
    CanonicalModelWriter cmw = null;
    File file = null;
    cmw = new CanonicalModelWriter(new IndentingWriter(new PipedWriter()));
    cmw = new CanonicalModelWriter(new PipedOutputStream());
    QName q1 = new QName("string", "string");
    cmw.compareNames(q1, q1);
    cmw.compareNames("String1", "String1");
    try {
      cmw.visit(new Model());
    } catch (Exception e) {
    }
    try {
      file = new File("pathdoesnotexist");
      cmw = new CanonicalModelWriter(file);
    } catch (Exception e) {
    } finally {
      try {
        file.delete();
      } catch (Exception e) {
      }
    }
  }

  /**
   * @testName: testClientProcessorEnvironment
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testClientProcessorEnvironment() throws Fault {
    Throwable t = new Throwable("this is a throwable");
    LocalizableMessage lm = new LocalizableMessage("bundle", "key");
    ClientProcessorEnvironment cpe = null;
    cpe = new ClientProcessorEnvironment(new PipedOutputStream(), "string",
        null);
    cpe.error(lm);
    cpe.info(lm);
    MyProcessorNotificationListener mpnl = new MyProcessorNotificationListener();
    cpe = new ClientProcessorEnvironment(new PipedOutputStream(), "string",
        mpnl);
    cpe.error(lm);
    cpe.info(lm);
    cpe.printStackTrace(t);
    cpe.printStackTrace(t);
    cpe.getWarningCount();
  }

  /**
   * @testName: testClassNameCollector
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testClassNameCollector() throws Fault {
    ClassNameCollector cnc = null;
    cnc = new ClassNameCollector();
    try {
      cnc.visit(new LiteralSimpleType());
    } catch (Exception e) {
    }
    try {
      cnc.visit(new LiteralSequenceType());
    } catch (Exception e) {
    }
    try {
      cnc.visit(new LiteralAllType());
    } catch (Exception e) {
    }
    try {
      cnc.visit(new LiteralArrayType());
    } catch (Exception e) {
    }
    try {
      cnc.visit(new LiteralArrayWrapperType());
    } catch (Exception e) {
    }
    try {
      cnc.visit(new LiteralFragmentType());
    } catch (Exception e) {
    }
    try {
      cnc.visit(new LiteralListType());
    } catch (Exception e) {
    }
    try {
      cnc.visit(new SOAPListType());
    } catch (Exception e) {
    }
    try {
      cnc.visit(new LiteralIDType());
    } catch (Exception e) {
    }
    try {
      cnc.visit(new LiteralEnumerationType());
    } catch (Exception e) {
    }
    try {
      cnc.visit(new SOAPArrayType());
    } catch (Exception e) {
    }
    try {
      cnc.visit(new SOAPCustomType());
    } catch (Exception e) {
    }
    try {
      cnc.visit(new SOAPEnumerationType());
    } catch (Exception e) {
    }
    try {
      cnc.visit(new SOAPAnyType());
    } catch (Exception e) {
    }
    try {
      cnc.visit(new SOAPUnorderedStructureType());
    } catch (Exception e) {
    }
    try {
      cnc.visit(new RPCRequestOrderedStructureType());
    } catch (Exception e) {
    }
  }

  /**
   * @testName: testComponentWriter
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testComponentWriter() throws Fault {
    ComponentWriter cw = null;
    cw = new ComponentWriter(new IndentingWriter(new PipedWriter()));
    try {
      cw.visit(new AnnotationComponent());
    } catch (Exception e) {
    }
    AttributeDeclarationComponent adc = new AttributeDeclarationComponent();
    adc.setName(new QName("string", "string"));
    adc.setTypeDefinition(new SimpleTypeDefinitionComponent());
    try {
      cw.visit(adc);
    } catch (Exception e) {
    }
    try {
      cw.visit(new AttributeGroupDefinitionComponent());
    } catch (Exception e) {
    }
    AttributeUseComponent auc = new AttributeUseComponent();
    auc.setAttributeDeclaration(adc);
    try {
      cw.visit(auc);
    } catch (Exception e) {
    }
    ComplexTypeDefinitionComponent ctdc = new ComplexTypeDefinitionComponent();
    ctdc.setName(new QName("string", "string"));
    MyTypeDefinitionComponent mtdc = new MyTypeDefinitionComponent();
    ctdc.setBaseTypeDefinition(mtdc);
    try {
      cw.visit(ctdc);
    } catch (Exception e) {
    }
    ElementDeclarationComponent edc = new ElementDeclarationComponent();
    edc.setName(new QName("string", "string"));
    try {
      cw.visit(edc);
    } catch (Exception e) {
    }
    try {
      cw.visit(new IdentityConstraintDefinitionComponent());
    } catch (Exception e) {
    }
    ModelGroupComponent mgc = new ModelGroupComponent();
    mgc.setCompositor(Symbol.DEFAULT);
    try {
      cw.visit(mgc);
    } catch (Exception e) {
    }
    try {
      cw.visit(new ModelGroupDefinitionComponent());
    } catch (Exception e) {
    }
    try {
      cw.visit(new NotationDeclarationComponent());
    } catch (Exception e) {
    }
    ParticleComponent pc = new ParticleComponent();
    pc.setMinOccurs(1);
    pc.setModelGroupTerm(mgc);
    pc.setElementTerm(new ElementDeclarationComponent());
    try {
      cw.visit(pc);
    } catch (Exception e) {
    }
    SimpleTypeDefinitionComponent stdc = new SimpleTypeDefinitionComponent();
    stdc.setName(new QName("string", "string"));
    try {
      cw.visit(stdc);
    } catch (Exception e) {
    }
    try {
      cw.visit(new WildcardComponent());
    } catch (Exception e) {
    }
  }

  /**
   * @testName: testDirectoryUtil
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testDirectoryUtil() throws Fault {
    DirectoryUtil du = null;
    du = new DirectoryUtil();
  }

  /**
   * @testName: testIndentingWriter
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testIndentingWriter() throws Fault {
    IndentingWriter ir = null;
    ir = new IndentingWriter(new PipedWriter(), 1);
    try {
      ir.write(1);
    } catch (Exception e) {
    }
    char c[] = { Character.MIN_VALUE, 0, Character.MAX_VALUE };
    try {
      ir.write(c, 0, 1);
    } catch (Exception e) {
    }
    try {
      ir.p("string", "string");
    } catch (Exception e) {
    }
    try {
      ir.p("string", "string", "string");
    } catch (Exception e) {
    }
    try {
      ir.p("string", "string", "string", "string");
    } catch (Exception e) {
    }
    try {
      ir.p("string", "string", "string", "string", "string");
    } catch (Exception e) {
    }
    try {
      ir.pln("string", "string");
    } catch (Exception e) {
    }
    try {
      ir.pln("string", "string", "string");
    } catch (Exception e) {
    }
    try {
      ir.pln("string", "string", "string", "string");
    } catch (Exception e) {
    }
    try {
      ir.pln("string", "string", "string", "string", "string");
    } catch (Exception e) {
    }
    Object o = "string";
    try {
      ir.pln(o);
    } catch (Exception e) {
    }
    try {
      ir.plnI(o);
    } catch (Exception e) {
    }
    try {
      ir.pO(o);
    } catch (Exception e) {
    }
    try {
      ir.pOln(o);
    } catch (Exception e) {
    }
    try {
      ir.pOlnI(o);
    } catch (Exception e) {
    }
    try {
      ir.pM("string");
    } catch (Exception e) {
    }
    try {
      ir.pMln("string");
    } catch (Exception e) {
    }
    try {
      ir.pMlnI("string");
    } catch (Exception e) {
    }
    try {
      ir.pMO("string");
    } catch (Exception e) {
    }
    try {
      ir.pMOln("string");
    } catch (Exception e) {
    }
    Object obuf[] = { "string", "string" };
    try {
      ir.pF("{0}, {1}", obuf);
    } catch (Exception e) {
    }
    try {
      ir.pFln("{0}, {1}", obuf);
    } catch (Exception e) {
    }
  }

  /**
   * @testName: testModelWriter
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testModelWriter() throws Fault {
    ModelWriter mw = null;
    File file = null;
    mw = new ModelWriter(new IndentingWriter(new PipedWriter()));
    mw = new ModelWriter(new PipedOutputStream());
    mw.write(new Model());
    try {
      mw.visit(new LiteralEnumerationType());
    } catch (Exception e) {
    }
    try {
      mw.visit(new LiteralSimpleType());
    } catch (Exception e) {
    }
    try {
      mw.visit(new LiteralIDType());
    } catch (Exception e) {
    }
    try {
      mw.visit(new LiteralSequenceType());
    } catch (Exception e) {
    }
    try {
      mw.visit(new LiteralAllType());
    } catch (Exception e) {
    }
    try {
      mw.visit(new LiteralArrayType());
    } catch (Exception e) {
    }
    try {
      mw.visit(new LiteralArrayWrapperType());
    } catch (Exception e) {
    }
    try {
      mw.visit(new LiteralListType());
    } catch (Exception e) {
    }
    try {
      mw.visit(new SOAPListType());
    } catch (Exception e) {
    }
    try {
      mw.visit(new LiteralFragmentType());
    } catch (Exception e) {
    }
    try {
      mw.visit(new SOAPArrayType());
    } catch (Exception e) {
    }
    try {
      mw.visit(new SOAPCustomType());
    } catch (Exception e) {
    }
    try {
      mw.visit(new SOAPEnumerationType());
    } catch (Exception e) {
    }
    try {
      mw.visit(new SOAPSimpleType());
    } catch (Exception e) {
    }
    try {
      mw.visit(new SOAPAnyType());
    } catch (Exception e) {
    }
    try {
      mw.visit(new SOAPOrderedStructureType());
    } catch (Exception e) {
    }
    try {
      mw.visit(new SOAPUnorderedStructureType());
    } catch (Exception e) {
    }
    try {
      mw.visit(new RPCRequestOrderedStructureType());
    } catch (Exception e) {
    }
    try {
      mw.visit(new RPCRequestUnorderedStructureType());
    } catch (Exception e) {
    }
    try {
      mw.visit(new RPCResponseStructureType());
    } catch (Exception e) {
    }
    try {
      file = new File("pathdoesnotexist");
      mw = new ModelWriter(file);
    } catch (Exception e) {
    } finally {
      try {
        file.delete();
      } catch (Exception e) {
      }
    }
  }

  /**
   * @testName: testPrettyPrintingXMLWriterImpl
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testPrettyPrintingXMLWriterImpl() throws Fault {
    PrettyPrintingXMLWriterImpl ppxw = null;
    ppxw = new PrettyPrintingXMLWriterImpl(new PipedOutputStream(), "UTF-8",
        true);
    ppxw.startElement("string", "string", "string");
    try {
      ppxw.writeNamespaceDeclaration("string");
    } catch (Exception e) {
    }
    ppxw.writeAttributeUnquoted("string", "string", "string");
    ppxw.writeChars(new CDATA("string"));
    ppxw.writeChars("string");
    ppxw.writeCharsUnquoted("string");
    char c[] = { Character.MIN_VALUE, 0, Character.MAX_VALUE };
    ppxw.writeCharsUnquoted(c, 0, 1);
    ppxw.getPrefixFactory();
    ppxw.getURI("string");
    ppxw.getPrefix("string");
    try {
      ppxw.flush();
    } catch (Exception e) {
    }
  }

  /**
   * @testName: testStringUtils
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testStringUtils() throws Fault {
    StringUtils su = null;
    su = new StringUtils();
  }

  /**
   * @testName: testXMLModelFileFilter
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testXMLModelFileFilter() throws Fault {
    XMLModelFileFilter xmff = null;
    xmff = new XMLModelFileFilter();
    xmff.isModelFile(new File("filedoesnotexist"));
    URL u = null;
    try {
      u = new URL("http", "localhost", "index.html");
    } catch (Exception e) {
    }
    xmff.isModelFile(u);
  }

}
