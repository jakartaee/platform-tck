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

package com.sun.ts.tests.internal.implementation.sjsas.jaxrpc.com.sun.xml.rpc.processor.generator.nodes;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest;
import java.util.Properties;

import com.sun.xml.rpc.processor.generator.nodes.*;
import com.sun.xml.rpc.processor.model.*;
import com.sun.xml.rpc.processor.model.literal.*;
import com.sun.xml.rpc.processor.model.java.*;
import com.sun.xml.rpc.processor.model.soap.*;
import com.sun.xml.rpc.processor.util.ClientProcessorEnvironment;
import com.sun.xml.rpc.processor.config.Configuration;

import javax.imageio.metadata.*;
import java.io.ByteArrayOutputStream;

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
   * @testName: testExceptionMappingNode
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testExceptionMappingNode() throws Fault {
    ExceptionMappingNode emn = new ExceptionMappingNode();
    try {
      emn.write(new IIOMetadataNode(), "string", new HeaderFault());
    } catch (Exception e) {
    }
  }

  /**
   * @testName: testJavaWsdlMappingNode
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testJavaWsdlMappingNode() throws Fault {
    JavaWsdlMappingNode jwmn = new JavaWsdlMappingNode();
  }

  /**
   * @testName: testJavaXmlTypeMappingNode
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testJavaXmlTypeMappingNode() throws Fault {
    JavaXmlTypeMappingNode jxtmn = new JavaXmlTypeMappingNode();
    ClientProcessorEnvironment env = new ClientProcessorEnvironment(
        new ByteArrayOutputStream(), null, null);
    Configuration config = new Configuration(env);
    try {
      jxtmn.writeAnonymousArrayType(new IIOMetadataNode(), "string",
          new LiteralAllType(), config, true);
    } catch (Exception e) {
    }
  }

  /**
   * @testName: testPackageMappingNode
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testPackageMappingNode() throws Fault {
    PackageMappingNode pmn = new PackageMappingNode();
  }

  /**
   * @testName: testServiceEndpointInterfaceMappingNode
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testServiceEndpointInterfaceMappingNode() throws Fault {
    ServiceEndpointInterfaceMappingNode seimn = new ServiceEndpointInterfaceMappingNode();
  }

  /**
   * @testName: testServiceInterfaceMappingNode
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testServiceInterfaceMappingNode() throws Fault {
    ServiceInterfaceMappingNode seimn = new ServiceInterfaceMappingNode();
  }

  /**
   * @testName: testTypeVisitor
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testTypeVisitor() throws Fault {
    ClientProcessorEnvironment env = new ClientProcessorEnvironment(
        new ByteArrayOutputStream(), null, null);
    Configuration config = new Configuration(env);
    TypeVisitor tv = new TypeVisitor(config);
    try {
      tv.visit(new LiteralSimpleType());
    } catch (Exception e) {
    }
    try {
      tv.visit(new LiteralSequenceType());
    } catch (Exception e) {
    }
    try {
      tv.visit(new LiteralAllType());
    } catch (Exception e) {
    }
    try {
      tv.visit(new LiteralEnumerationType());
    } catch (Exception e) {
    }
    try {
      tv.visit(new LiteralListType());
    } catch (Exception e) {
    }
    try {
      tv.visit(new LiteralIDType());
    } catch (Exception e) {
    }
    try {
      tv.visit(new LiteralArrayWrapperType());
    } catch (Exception e) {
    }
    try {
      tv.visit(new LiteralArrayType());
    } catch (Exception e) {
    }
    try {
      tv.visit(new LiteralFragmentType());
    } catch (Exception e) {
    }
    try {
      tv.visit(new SOAPArrayType());
    } catch (Exception e) {
    }
    try {
      tv.visit(new SOAPCustomType());
    } catch (Exception e) {
    }
    try {
      tv.visit(new SOAPEnumerationType());
    } catch (Exception e) {
    }
    try {
      tv.visit(new SOAPSimpleType());
    } catch (Exception e) {
    }
    try {
      tv.visit(new SOAPAnyType());
    } catch (Exception e) {
    }
    try {
      tv.visit(new SOAPOrderedStructureType());
    } catch (Exception e) {
    }
    try {
      tv.visit(new SOAPUnorderedStructureType());
    } catch (Exception e) {
    }
    try {
      tv.visit(new SOAPListType());
    } catch (Exception e) {
    }
    try {
      tv.visit(new RPCRequestOrderedStructureType());
    } catch (Exception e) {
    }

  }

}
