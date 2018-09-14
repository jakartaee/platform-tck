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

package com.sun.ts.tests.internal.implementation.sjsas.jaxrpc.com.sun.xml.rpc.wsdl.document.documenttests;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import java.io.*;
import java.net.*;
import java.util.*;
import java.rmi.*;

import org.xml.sax.InputSource;
import com.sun.xml.rpc.wsdl.framework.*;
import com.sun.xml.rpc.wsdl.document.*;
import com.sun.xml.rpc.wsdl.parser.*;

import javax.xml.namespace.QName;

import com.sun.javatest.Status;

public class Client extends EETest {
  private final static String FS = System.getProperty("file.separator");

  private final static String WSDL_DOC = "MultiInterfaceTestService.wsdl";

  private final static String SERVICE_NAME = "MultiInterfaceTestService";

  private final static String DOCUMENTATION = "THIS IS DOCUMENTATION";

  private static final String baseDir = "src/com/sun/ts/tests/internal/implementation/sjsas/jaxrpc/com/sun/xml/rpc/wsdl/document/documenttests/";

  WSDLDocument document;

  Definitions definitions;

  MyDocumentVisitor mdv = new MyDocumentVisitor();

  Documentation myDocumentation;

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /* Test setup */

  /*
   * @class.setup_props: ts_home;
   */

  public void setup(String[] args, Properties p) throws Fault {
    String tsHome, tsBase;
    try {
      tsHome = p.getProperty("ts_home");
      logMsg("tsHome=" + tsHome);
      tsBase = tsHome + "/" + baseDir.replaceAll("/", FS);
      logMsg("tsBase=" + tsBase);
      WSDLParser parser = new WSDLParser();
      boolean followImports = parser.getFollowImports();
      TestUtil.logMsg("followImports=" + followImports);
      if (!followImports)
        parser.setFollowImports(true);
      InputSource inputSource = new InputSource(
          new File(tsBase + WSDL_DOC).toURL().toString());
      document = parser.parse(inputSource);
      myDocumentation = new Documentation(DOCUMENTATION);
    } catch (Exception e) {
      throw new Fault("setup failed", e);
    }
    logMsg("setup ok");
  }

  public void cleanup() throws Fault {
    logMsg("cleanup ok");
  }

  private void dumpQNames(QName[] q) {
    for (int i = 0; i < q.length; i++)
      TestUtil.logMsg("QName: " + q[i]);
  }

  private void dumpIterator(Iterator i) {
    while (i.hasNext()) {
      TestUtil.logMsg("Iterator Dump: " + i.next());
    }
  }

  private Object getFirstObject(Iterator i) {
    if (i.hasNext())
      return i.next();
    else
      return null;
  }

  /*
   * @testName: WSDLDocumentTests
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void WSDLDocumentTests() throws Fault {
    TestUtil.logTrace("WSDLDocumentTests");
    boolean pass = true;
    boolean status;
    try {
      TestUtil.logMsg("Get WSDL Definitions");
      definitions = document.getDefinitions();
      TestUtil.logMsg("Definitions=" + definitions);
      TestUtil.logMsg("Set WSDL Definitions");
      document.setDefinitions(definitions);
      TestUtil.logMsg("Get All Service QNames");
      QName[] serviceQNames = document.getAllServiceQNames();
      dumpQNames(serviceQNames);
      TestUtil.logMsg("Get All Port QNames");
      QName[] portQNames = document.getAllPortQNames();
      dumpQNames(portQNames);
      TestUtil.logMsg("Get Port QNames for serviceName");
      portQNames = document.getPortQNames(SERVICE_NAME);
      dumpQNames(portQNames);
      TestUtil.logMsg("Collect all namespaces ");
      Set namespaces = document.collectAllNamespaces();
      dumpIterator(namespaces.iterator());
    } catch (Exception e) {
      throw new Fault("WSDLDocumentTests failed", e);
    }

    if (!pass)
      throw new Fault("WSDLDocumentTests failed");
  }

  /*
   * @testName: DefinitionsTests
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void DefinitionsTests() throws Fault {
    TestUtil.logTrace("DefinitionsTests");
    boolean pass = true;
    boolean status;
    try {
      TestUtil.logMsg("Get WSDL Definitions");
      definitions = document.getDefinitions();
      TestUtil.logMsg("Get Types");
      Types types = definitions.getTypes();
      TestUtil.logMsg("Types=" + types);
      TestUtil.logMsg("Get messages");
      Iterator messages = definitions.messages();
      dumpIterator(messages);
      TestUtil.logMsg("Get portTypes");
      Iterator portTypes = definitions.portTypes();
      dumpIterator(portTypes);
      TestUtil.logMsg("Get bindings");
      Iterator bindings = definitions.bindings();
      dumpIterator(bindings);
      TestUtil.logMsg("Get extensions");
      Iterator extensions = definitions.extensions();
      dumpIterator(extensions);
      TestUtil.logMsg("Get Documentation");
      Documentation documentation = definitions.getDocumentation();
      TestUtil.logMsg("Documentation=" + documentation);
      TestUtil.logMsg("Call accept method");
      definitions.accept(mdv);
    } catch (Exception e) {
      throw new Fault("DefinitionsTests failed", e);
    }

    if (!pass)
      throw new Fault("DefinitionsTests failed");
  }

  /*
   * @testName: DocumentationTests
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void DocumentationTests() throws Fault {
    TestUtil.logTrace("DocumentationTests");
    boolean pass = true;
    boolean status;
    String newContent = "new content";
    try {
      TestUtil.logMsg("Get WSDL Definitions");
      definitions = document.getDefinitions();
      Documentation documentation = definitions.getDocumentation();
      TestUtil.logMsg("Documentation=" + documentation);
      TestUtil.logMsg("Get content");
      String content = documentation.getContent();
      TestUtil.logMsg("content=" + content);
      TestUtil.logMsg("set content");
      documentation.setContent(newContent);
      content = documentation.getContent();
      TestUtil.logMsg("content=" + content);
      TestUtil.logMsg("Call accept method");
      documentation.accept(mdv);

    } catch (Exception e) {
      throw new Fault("DocumentationTests failed", e);
    }

    if (!pass)
      throw new Fault("DocumentationTests failed");
  }

  /*
   * @testName: MessageTests
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void MessageTests() throws Fault {
    TestUtil.logTrace("MessageTests");
    boolean pass = true;
    boolean status;
    try {
      TestUtil.logMsg("Get WSDL Definitions");
      definitions = document.getDefinitions();
      TestUtil.logMsg("Get messages");
      Iterator messages = definitions.messages();
      Message message = (Message) getFirstObject(messages);
      TestUtil.logMsg("Get number of parts: " + message.numParts());
      TestUtil.logMsg("Get element name: " + message.getElementName());
      Documentation documentation = message.getDocumentation();
      TestUtil.logMsg("content:" + documentation.getContent());
      TestUtil.logMsg("Set Documentation");
      message.setDocumentation(documentation);
      TestUtil.logMsg("Call accept method");
      message.accept(mdv);
    } catch (Exception e) {
      throw new Fault("MessageTests failed", e);
    }

    if (!pass)
      throw new Fault("MessageTests failed");
  }

  /*
   * @testName: TypesTests
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void TypesTests() throws Fault {
    TestUtil.logTrace("TypesTests");
    boolean pass = true;
    boolean status;
    try {
      TestUtil.logMsg("Get WSDL Definitions");
      definitions = document.getDefinitions();
      TestUtil.logMsg("Get Types");
      Types types = definitions.getTypes();
      TestUtil.logMsg("Types=" + types);
      TestUtil.logMsg("Get Documentation");
      Documentation documentation = types.getDocumentation();
      TestUtil.logMsg("Documentation=" + documentation);
      TestUtil.logMsg("content:" + documentation.getContent());
      TestUtil.logMsg("Set Documentation");
      types.setDocumentation(documentation);
      Iterator extensions = types.extensions();
      dumpIterator(extensions);
      TestUtil.logMsg("Call accept method");
      types.accept(mdv);
    } catch (Exception e) {
      throw new Fault("TypesTests failed", e);
    }

    if (!pass)
      throw new Fault("TypesTests failed");
  }

  /*
   * @testName: PortTypeTests
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void PortTypeTests() throws Fault {
    TestUtil.logTrace("PortTypeTests");
    boolean pass = true;
    boolean status;
    try {
      TestUtil.logMsg("Get WSDL Definitions");
      definitions = document.getDefinitions();
      TestUtil.logMsg("Get messages");
      Iterator portTypes = definitions.portTypes();
      PortType portType = (PortType) getFirstObject(portTypes);
      TestUtil.logMsg("Get element name: " + portType.getElementName());
      Documentation documentation = portType.getDocumentation();
      portType.setDocumentation(documentation);
      try {
        portType.validateThis();
      } catch (Exception e) {
      }
      TestUtil.logMsg("Kind: " + portType.getKind());
      TestUtil.logMsg("Get operations");
      Iterator operations = portType.operations();
      dumpIterator(operations);
      TestUtil.logMsg("Call accept method");
      portType.accept(mdv);
    } catch (Exception e) {
      throw new Fault("PortTypeTests failed", e);
    }

    if (!pass)
      throw new Fault("PortTypeTests failed");
  }

  /*
   * @testName: ServiceTests
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void ServiceTests() throws Fault {
    TestUtil.logTrace("ServiceTests");
    boolean pass = true;
    boolean status;
    try {
      TestUtil.logMsg("Get WSDL Definitions");
      definitions = document.getDefinitions();
      TestUtil.logMsg("Get services");
      Iterator services = definitions.services();
      Service service = (Service) getFirstObject(services);
      TestUtil.logMsg("Kind: " + service.getKind());
      TestUtil.logMsg("Call accept method");
      service.accept(mdv);
      TestUtil.logMsg("Get element name: " + service.getElementName());
      Iterator extensions = service.extensions();
      Extension extension = (Extension) getFirstObject(extensions);
      TestUtil.logMsg("Extension: " + extension);
    } catch (Exception e) {
      throw new Fault("ServiceTests failed", e);
    }

    if (!pass)
      throw new Fault("ServiceTests failed");
  }

  /*
   * @testName: PortTests
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void PortTests() throws Fault {
    TestUtil.logTrace("PortTests");
    boolean pass = true;
    boolean status;
    try {
      TestUtil.logMsg("Get WSDL Definitions");
      definitions = document.getDefinitions();
      TestUtil.logMsg("Get services");
      Iterator services = definitions.services();
      Service service = (Service) getFirstObject(services);
      Iterator ports = service.ports();
      Port port = (Port) getFirstObject(ports);
      TestUtil.logMsg("Kind: " + port.getKind());
      Service service2 = port.getService();
      TestUtil.logMsg("service=" + service + " , service2=" + service2);
      TestUtil.logMsg("Get Documentation");
      Documentation documentation = port.getDocumentation();
      TestUtil.logMsg("Documentation=" + documentation);
      TestUtil.logMsg("Set Documentation");
      port.setDocumentation(myDocumentation);
      TestUtil.logMsg("Call accept method");
      port.accept(mdv);
    } catch (Exception e) {
      throw new Fault("PortTests failed", e);
    }

    if (!pass)
      throw new Fault("PortTests failed");
  }

  /*
   * @testName: OutputTests
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void OutputTests() throws Fault {
    TestUtil.logTrace("OutputTests");
    boolean pass = true;
    boolean status;
    try {
      TestUtil.logMsg("Get WSDL Definitions");
      definitions = document.getDefinitions();
      TestUtil.logMsg("Get portTypes");
      Iterator portTypes = definitions.portTypes();
      dumpIterator(definitions.portTypes());
      PortType portType = (PortType) getFirstObject(portTypes);
      TestUtil.logMsg("Get operations");
      Iterator operations = portType.operations();
      dumpIterator(portType.operations());
      Operation operation = (Operation) getFirstObject(operations);
      Output output = operation.getOutput();
      TestUtil.logMsg("getName=" + output.getName());
      TestUtil.logMsg("getMessage=" + output.getMessage());
      TestUtil.logMsg("getElementName=" + output.getElementName());
      Documentation documentation = output.getDocumentation();
      TestUtil.logMsg("Documentation=" + documentation);
      TestUtil.logMsg("Set Documentation");
      output.setDocumentation(myDocumentation);
      TestUtil.logMsg("Call accept method");
      output.accept(mdv);
    } catch (Exception e) {
      throw new Fault("OutputTests failed", e);
    }

    if (!pass)
      throw new Fault("OutputTests failed");
  }

  /*
   * @testName: InputTests
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void InputTests() throws Fault {
    TestUtil.logTrace("InputTests");
    boolean pass = true;
    boolean status;
    try {
      TestUtil.logMsg("Get WSDL Definitions");
      definitions = document.getDefinitions();
      TestUtil.logMsg("Get portTypes");
      Iterator portTypes = definitions.portTypes();
      dumpIterator(definitions.portTypes());
      PortType portType = (PortType) getFirstObject(portTypes);
      TestUtil.logMsg("Get operations");
      Iterator operations = portType.operations();
      dumpIterator(portType.operations());
      Operation operation = (Operation) getFirstObject(operations);
      Input input = operation.getInput();
      TestUtil.logMsg("getName=" + input.getName());
      TestUtil.logMsg("getMessage=" + input.getMessage());
      TestUtil.logMsg("getElementName=" + input.getElementName());
      Documentation documentation = input.getDocumentation();
      TestUtil.logMsg("Documentation=" + documentation);
      TestUtil.logMsg("Set Documentation");
      input.setDocumentation(myDocumentation);
      TestUtil.logMsg("Call accept method");
      input.accept(mdv);
    } catch (Exception e) {
      throw new Fault("InputTests failed", e);
    }

    if (!pass)
      throw new Fault("InputTests failed");
  }

  /*
   * @testName: BindingTests
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void BindingTests() throws Fault {
    TestUtil.logTrace("BindingTests");
    boolean pass = true;
    boolean status;
    try {
      TestUtil.logMsg("Get WSDL Definitions");
      definitions = document.getDefinitions();
      TestUtil.logMsg("Get bindings");
      Iterator bindings = definitions.bindings();
      dumpIterator(definitions.bindings());
      Binding binding = (Binding) getFirstObject(bindings);
      TestUtil.logMsg("getPortType=" + binding.getPortType());
      TestUtil.logMsg("getKind=" + binding.getKind());
      TestUtil.logMsg("getName=" + binding.getName());
      TestUtil.logMsg("getElementName=" + binding.getElementName());
      Documentation documentation = binding.getDocumentation();
      TestUtil.logMsg("Documentation=" + documentation);
      TestUtil.logMsg("Set Documentation");
      binding.setDocumentation(myDocumentation);
      documentation = binding.getDocumentation();
      TestUtil.logMsg("Documentation=" + documentation);
      TestUtil.logMsg("Call validate method");
      binding.validateThis();
      TestUtil.logMsg("Call accept method");
      binding.accept(mdv);
    } catch (Exception e) {
      throw new Fault("BindingTests failed", e);
    }

    if (!pass)
      throw new Fault("BindingTests failed");
  }

  /*
   * @testName: BindingOutputTests
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void BindingOutputTests() throws Fault {
    TestUtil.logTrace("BindingOutputTests");
    boolean pass = true;
    boolean status;
    try {
      TestUtil.logMsg("Get WSDL Definitions");
      definitions = document.getDefinitions();
      TestUtil.logMsg("Get bindings");
      Iterator bindings = definitions.bindings();
      dumpIterator(definitions.bindings());
      Binding binding = (Binding) getFirstObject(bindings);
      TestUtil.logMsg("Get operations");
      Iterator operations = binding.operations();
      dumpIterator(binding.operations());
      BindingOperation operation = (BindingOperation) getFirstObject(
          operations);
      BindingOutput output = operation.getOutput();
      TestUtil.logMsg("getName=" + output.getName());
      TestUtil.logMsg("getElementName=" + output.getElementName());
      Documentation documentation = output.getDocumentation();
      TestUtil.logMsg("Documentation=" + documentation);
      TestUtil.logMsg("Set Documentation");
      output.setDocumentation(myDocumentation);
      TestUtil.logMsg("Call accept method");
      output.accept(mdv);
    } catch (Exception e) {
      throw new Fault("BindingOutputTests failed", e);
    }

    if (!pass)
      throw new Fault("BindingOutputTests failed");
  }

  /*
   * @testName: BindingInputTests
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void BindingInputTests() throws Fault {
    TestUtil.logTrace("BindingInputTests");
    boolean pass = true;
    boolean status;
    try {
      TestUtil.logMsg("Get WSDL Definitions");
      definitions = document.getDefinitions();
      TestUtil.logMsg("Get bindings");
      Iterator bindings = definitions.bindings();
      dumpIterator(definitions.bindings());
      Binding binding = (Binding) getFirstObject(bindings);
      TestUtil.logMsg("Get operations");
      Iterator operations = binding.operations();
      dumpIterator(binding.operations());
      BindingOperation operation = (BindingOperation) getFirstObject(
          operations);
      BindingInput input = operation.getInput();
      TestUtil.logMsg("getName=" + input.getName());
      TestUtil.logMsg("getElementName=" + input.getElementName());
      Documentation documentation = input.getDocumentation();
      TestUtil.logMsg("Documentation=" + documentation);
      TestUtil.logMsg("Set Documentation");
      input.setDocumentation(myDocumentation);
      TestUtil.logMsg("Call accept method");
      input.accept(mdv);
    } catch (Exception e) {
      throw new Fault("BindingInputTests failed", e);
    }

    if (!pass)
      throw new Fault("BindingInputTests failed");
  }

  /*
   * @testName: MessagePartTests
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void MessagePartTests() throws Fault {
    TestUtil.logTrace("MessagePartTests");
    boolean pass = true;
    boolean status;
    try {
      TestUtil.logMsg("Get WSDL Definitions");
      definitions = document.getDefinitions();
      TestUtil.logMsg("Get messages");
      Iterator messages = definitions.messages();
      Message message = (Message) getFirstObject(messages);
      Iterator parts = message.parts();
      MessagePart messagePart = (MessagePart) getFirstObject(parts);
      TestUtil.logMsg("getName=" + messagePart.getName());
      TestUtil.logMsg("getElementName=" + messagePart.getElementName());
      TestUtil.logMsg("Call accept method");
      messagePart.accept(mdv);
    } catch (Exception e) {
      throw new Fault("MessagePartTests failed", e);
    }

    if (!pass)
      throw new Fault("MessagePartTests failed");
  }

  /*
   * @testName: FaultTests
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void FaultTests() throws Fault {
    TestUtil.logTrace("FaultTests");
    boolean pass = true;
    boolean status;
    try {
      TestUtil.logMsg("Get WSDL Definitions");
      definitions = document.getDefinitions();
      TestUtil.logMsg("Get messages");
      Iterator portTypes = definitions.portTypes();
      PortType portType = (PortType) getFirstObject(portTypes);
      TestUtil.logMsg("Get operations");
      Iterator operations = portType.operations();
      Operation operation = (Operation) getFirstObject(operations);
      Iterator faults = operation.faults();
      com.sun.xml.rpc.wsdl.document.Fault fault = (com.sun.xml.rpc.wsdl.document.Fault) getFirstObject(
          faults);
      Documentation documentation = fault.getDocumentation();
      TestUtil.logMsg("Documentation=" + documentation);
      TestUtil.logMsg("Set Documentation");
      fault.setDocumentation(myDocumentation);
    } catch (Exception e) {
      throw new Fault("FaultTests failed", e);
    }

    if (!pass)
      throw new Fault("FaultTests failed");
  }

  /*
   * @testName: BindingOperationTests
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void BindingOperationTests() throws Fault {
    TestUtil.logTrace("BindingOperationTests");
    boolean pass = true;
    boolean status;
    try {
      TestUtil.logMsg("Get WSDL Definitions");
      definitions = document.getDefinitions();
      TestUtil.logMsg("Get bindings");
      Iterator bindings = definitions.bindings();
      dumpIterator(definitions.bindings());
      Binding binding = (Binding) getFirstObject(bindings);
      TestUtil.logMsg("Get operations");
      Iterator operations = binding.operations();
      dumpIterator(binding.operations());
      BindingOperation operation = (BindingOperation) getFirstObject(
          operations);
      TestUtil.logMsg("getName=" + operation.getName());
      TestUtil.logMsg("getElementName=" + operation.getElementName());
      TestUtil.logMsg("getInput=" + operation.getInput());
      TestUtil.logMsg("getOutput=" + operation.getOutput());
      TestUtil.logMsg("getStyle=" + operation.getStyle());
      TestUtil.logMsg("getUniqueKey=" + operation.getUniqueKey());
      Documentation documentation = operation.getDocumentation();
      TestUtil.logMsg("Documentation=" + documentation);
      TestUtil.logMsg("Set Documentation");
      operation.setDocumentation(myDocumentation);
      TestUtil.logMsg("Call accept method");
      operation.accept(mdv);
    } catch (Exception e) {
      throw new Fault("BindingOperationTests failed", e);
    }

    if (!pass)
      throw new Fault("BindingOperationTests failed");
  }
}
