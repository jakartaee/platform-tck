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

package com.sun.ts.tests.internal.implementation.sjsas.jaxrpc.com.sun.xml.rpc.processor.schema;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest;
import java.util.Properties;
import java.util.HashSet;

import com.sun.xml.rpc.processor.schema.*;
import com.sun.xml.rpc.processor.util.ComponentWriter;
import com.sun.xml.rpc.processor.util.IndentingWriter;
import com.sun.xml.rpc.wsdl.document.schema.SchemaElement;
import com.sun.xml.rpc.wsdl.document.schema.SchemaDocument;
import com.sun.xml.rpc.wsdl.document.soap.SOAPConstants;
import com.sun.xml.rpc.wsdl.parser.Constants;
import javax.xml.namespace.QName;
import java.io.*;

import com.sun.xml.rpc.processor.schema.MyInternalSchemaBuilderBase;

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
   * @testName: testAnnotationComponent
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testAnnotationComponent() throws Fault {
    AnnotationComponent ac = null;
    ac = new AnnotationComponent();
    ac.addApplicationInformation(new SchemaElement());
    ac.addUserInformation(new SchemaElement());
    try {
      ac.accept(new ComponentWriter(new IndentingWriter(new PipedWriter())));
    } catch (Exception e) {
    }

  }

  /**
   * @testName: testAttributeDeclarationComponent
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testAttributeDeclarationComponent() throws Fault {
    AttributeDeclarationComponent adc = null;
    adc = new AttributeDeclarationComponent();
    adc.getScope();
    adc.getAnnotation();
    try {
      adc.accept(new ComponentWriter(new IndentingWriter(new PipedWriter())));
    } catch (Exception e) {
    }

  }

  /**
   * @testName: testAttributeGroupDefinitionComponent
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testAttributeGroupDefinitionComponent() throws Fault {
    AttributeGroupDefinitionComponent agdc = null;
    agdc = new AttributeGroupDefinitionComponent();
    agdc.attributeUses();
    agdc.addAttributeGroup(new AttributeGroupDefinitionComponent());
    try {
      agdc.accept(new ComponentWriter(new IndentingWriter(new PipedWriter())));
    } catch (Exception e) {
    }

  }

  /**
   * @testName: testAttributeUseComponent
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testAttributeUseComponent() throws Fault {
    AttributeUseComponent auc = null;
    auc = new AttributeUseComponent();
    auc.setValue("string");
    // auc.setValueKind(new SYMBOL("default"));
    try {
      auc.accept(new ComponentWriter(new IndentingWriter(new PipedWriter())));
    } catch (Exception e) {
    }

  }

  /**
   * @testName: testComplexTypeDefinitionComponent
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testComplexTypeDefinitionComponent() throws Fault {
    ComplexTypeDefinitionComponent ctdc = null;
    ctdc = new ComplexTypeDefinitionComponent();
    ctdc.isAbstract();
    ctdc.hasNoAttributeUses();
    ctdc.addAttributeGroup(new AttributeGroupDefinitionComponent());
    ctdc.getSimpleTypeContent();
    try {
      ctdc.accept(new ComponentWriter(new IndentingWriter(new PipedWriter())));
    } catch (Exception e) {
    }

  }

  /**
   * @testName: testElementDeclarationComponent
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testElementDeclarationComponent() throws Fault {
    ElementDeclarationComponent edc = null;
    edc = new ElementDeclarationComponent();
    edc.getScope();
    edc.setValue("string");
    try {
      edc.accept(new ComponentWriter(new IndentingWriter(new PipedWriter())));
    } catch (Exception e) {
    }

  }

  /**
   * @testName: testEnumerationFacet
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testEnumerationFacet() throws Fault {
    EnumerationFacet edc = null;
    edc = new EnumerationFacet();
    edc.addPrefix("string", "string");
    edc.getNamespaceURI("string");
    edc.getPrefixes();
  }

  /**
   * @testName: testFundamentalFacet
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testFundamentalFacet() throws Fault {
    MyFundamentalFacet ff = new MyFundamentalFacet(
        new QName("string", "string"));
  }

  /**
   * @testName: testIdentityConstraintDefinitionComponent
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testIdentityConstraintDefinitionComponent() throws Fault {
    IdentityConstraintDefinitionComponent iddc = null;
    iddc = new IdentityConstraintDefinitionComponent();
    try {
      iddc.accept(new ComponentWriter(new IndentingWriter(new PipedWriter())));
    } catch (Exception e) {
    }

  }

  /**
   * @testName: testInternalSchema
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testInternalSchema() throws Fault {
    InternalSchema is = null;
    is = new InternalSchema(
        new InternalSchemaBuilder11(new SchemaDocument(), new Properties()));
    AttributeGroupDefinitionComponent a = new AttributeGroupDefinitionComponent();
    a.setName(new QName("string", "string"));
    is.add(a);
    is.findAttributeGroupDefinition(new QName("string", "string"));
    is.add(new ModelGroupDefinitionComponent());
    // is.findModelGroupDefinition(new QName("string", "string"));
    is.add(new NotationDeclarationComponent());
    is.add(new AnnotationComponent());
  }

  /**
   * @testName: testInternalSchemaBuilder
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testInternalSchemaBuilder() throws Fault {
    InternalSchemaBuilder isb = null;
    isb = new InternalSchemaBuilder(new SchemaDocument(), new Properties());
  }

  /**
   * @testName: testInternalSchemaBuilder101
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testInternalSchemaBuilder101() throws Fault {
    InternalSchemaBuilder101 isb = null;
    isb = new InternalSchemaBuilder101(new SchemaDocument(), new Properties());
  }

  /**
   * @testName: testInternalSchemaBuilder103
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testInternalSchemaBuilder103() throws Fault {
    InternalSchemaBuilder103 isb = null;
    isb = new InternalSchemaBuilder103(new SchemaDocument(), new Properties());
  }

  /**
   * @testName: testInternalSchemaBuilderBase
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testInternalSchemaBuilderBase() throws Fault {
    MyInternalSchemaBuilderBase misbb = null;
    misbb = new MyInternalSchemaBuilderBase(new SchemaDocument(),
        new Properties());
    // misbb.buildAttributeGroupDefinition(SOAPConstants.QNAME_ATTR_ARRAY_TYPE);
    try {
      misbb.buildModelGroupDefinition(new QName("string", "string"));
    } catch (Exception e) {
    }
    misbb.processRestrictionSimpleTypeDefinition(
        new SchemaElement(new QName("string", "string")),
        new ComplexTypeDefinitionComponent(), new InternalSchema(misbb));
    misbb.buildAnyWildcard(new SchemaElement(new QName("string", "string")),
        new ComplexTypeDefinitionComponent(), new InternalSchema(misbb));
    misbb.parseSymbolSet(Constants.ATTRVALUE_ALL, new HashSet());
    misbb.parseSymbolSet(Constants.ATTRVALUE_ANY, new HashSet());
    try {
      misbb.failUnimplemented("string");
    } catch (Exception e) {
    }
    try {
      misbb.failValidation("string", "string");
    } catch (Exception e) {
    }
    try {
      misbb.failValidation("string", "string", "string");
    } catch (Exception e) {
    }
  }

  /**
   * @testName: testModelGroupComponent
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testModelGroupComponent() throws Fault {
    ModelGroupComponent mgc = null;
    mgc = new ModelGroupComponent();
    try {
      mgc.accept(new ComponentWriter(new IndentingWriter(new PipedWriter())));
    } catch (Exception e) {
    }

  }

  /**
   * @testName: testModelGroupDefinitionComponent
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testModelGroupDefinitionComponent() throws Fault {
    ModelGroupDefinitionComponent mgdc = null;
    mgdc = new ModelGroupDefinitionComponent();
    mgdc.getName();
    try {
      mgdc.accept(new ComponentWriter(new IndentingWriter(new PipedWriter())));
    } catch (Exception e) {
    }

  }

  /**
   * @testName: testNotationDeclarationComponent
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testNotationDeclarationComponent() throws Fault {
    NotationDeclarationComponent ndc = null;
    ndc = new NotationDeclarationComponent();
    try {
      ndc.accept(new ComponentWriter(new IndentingWriter(new PipedWriter())));
    } catch (Exception e) {
    }
  }

  /**
   * @testName: testParticleComponent
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testParticleComponent() throws Fault {
    ParticleComponent pc = null;
    pc = new ParticleComponent();
    pc.getMaxOccurs();
    pc.occursAtMostOnce();
    pc.occursZeroOrMore();
    pc.occursOnceOrMore();
    pc.getWildcardTerm();
  }

  /**
   * @testName: testSimpleTypeDefinitionComponent
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testSimpleTypeDefinitionComponent() throws Fault {
    SimpleTypeDefinitionComponent stdc = null;
    stdc = new SimpleTypeDefinitionComponent();
    try {
      stdc.accept(new ComponentWriter(new IndentingWriter(new PipedWriter())));
    } catch (Exception e) {
    }

  }

  /**
   * @testName: testUnimplementedFeatureException
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testUnimplementedFeatureException() throws Fault {
    UnimplementedFeatureException ufe = null;
    ufe = new UnimplementedFeatureException("string");
  }

  /**
   * @testName: testWildcardComponent
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testWildcardComponent() throws Fault {
    WildcardComponent wcc = null;
    wcc = new WildcardComponent();
    wcc.getNamespaceConstraintTag();
    wcc.setNamespaceName("string");
    wcc.getNamespaceName();
    try {
      wcc.accept(new ComponentWriter(new IndentingWriter(new PipedWriter())));
    } catch (Exception e) {
    }
  }

}
