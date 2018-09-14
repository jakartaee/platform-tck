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

package com.sun.ts.tests.internal.implementation.sjsas.jaxrpc.com.sun.xml.rpc.processor.config.ConfigTests;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest;
import java.util.Properties;

import com.sun.xml.rpc.processor.config.*;
import com.sun.xml.rpc.util.localization.LocalizableMessage;
import com.sun.xml.rpc.soap.SOAPVersion;
import javax.xml.namespace.QName;

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
   * @testName: testConfigurationException
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testConfigurationException() throws Fault {
    ConfigurationException pe = null;
    pe = new ConfigurationException("string");
    pe = new ConfigurationException("string");
    pe.getResourceBundleName();
    pe = new ConfigurationException("string", "string");
    final Object o[] = { "string", "string" };
    pe = new ConfigurationException("string", o);
    pe = new ConfigurationException("string",
        new LocalizableMessage("string", "string"));
  }

  /**
   * @testName: testHandlerInfo
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testHandlerInfo() throws Fault {
    HandlerInfo hi = new HandlerInfo();
    HashMap hm = new HashMap();
    hi.setProperties(hm);
    HashSet hs = new HashSet();
    hi.setHeaderNames(hs);
  }

  /**
   * @testName: testHandlerChainInfo
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testHandlerChainInfo() throws Fault {
    HandlerInfo hi = new HandlerInfo();
    HandlerChainInfo hci = new HandlerChainInfo();
    hci.add(hi);
    hci.getHandlersCount();
    List l = hci.getHandlersList();
    HashSet hs = new HashSet();
    hci.setRoles(hs);
  }

  /**
   * @testName: testImportedDocumentInfo
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testImportedDocumentInfo() throws Fault {
    int i = 1;
    ImportedDocumentInfo idi1 = new ImportedDocumentInfo();
    idi1.setType(i);
    idi1.getType();
    ImportedDocumentInfo idi2 = new ImportedDocumentInfo(i);
    idi2.setNamespace("string");
    idi2.getNamespace();
    idi2.setLocation("string");
    idi2.getLocation();
  }

  /**
   * @testName: testModelFileModelInfo
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testModelFileModelInfo() throws Fault {
    ModelFileModelInfo mfmi = new ModelFileModelInfo();
    mfmi.setLocation("string");
    mfmi.getLocation();
  }

  /**
   * @testName: testRmiInterfaceInfo
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testRmiInterfaceInfo() throws Fault {
    RmiInterfaceInfo rii = new RmiInterfaceInfo();
    RmiModelInfo rmi = new RmiModelInfo();
    rii.setParent(rmi);
    rii.getParent();
    rii.setName("string");
    rii.getName();
    rii.setServantName("string");
    rii.getServantName();
    rii.setSOAPAction("string");
    rii.getSOAPAction();
    rii.setSOAPActionBase("string");
    rii.getSOAPActionBase();
    rii.setSOAPVersion(SOAPVersion.SOAP_11);
    rii.getSOAPVersion();
    HandlerChainInfo hci = new HandlerChainInfo();
    rii.setClientHandlerChainInfo(hci);
    rii.getClientHandlerChainInfo();
    rii.setServerHandlerChainInfo(hci);
    rii.getServerHandlerChainInfo();
  }

  /**
   * @testName: testRmiModelInfo
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testRmiModelInfo() throws Fault {
    RmiModelInfo rmi = new RmiModelInfo();
    rmi.setTargetNamespaceURI("string");
    rmi.getTargetNamespaceURI();
    rmi.setTypeNamespaceURI("string");
    rmi.getTypeNamespaceURI();
    rmi.setJavaPackageName("string");
    rmi.getJavaPackageName();
    RmiInterfaceInfo rii = new RmiInterfaceInfo();
    rmi.add(rii);
    rmi.getInterfaces();
  }

  /**
   * @testName: testNamespaceMappingRegistryInfo
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testNamespaceMappingRegistryInfo() throws Fault {
    NamespaceMappingRegistryInfo nmri = new NamespaceMappingRegistryInfo();
    nmri.getNamespaceMappings();
  }

  /**
   * @testName: testNoMetadataModelInfo
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testNoMetadataModelInfo() throws Fault {
    NoMetadataModelInfo nmmi = new NoMetadataModelInfo();
    nmmi.setServiceInterfaceName("string");
    nmmi.setServantName("string");
    nmmi.getServantName();
    nmmi.setServiceName(new QName("string", "string"));
  }

  /**
   * @testName: testTypeMappingInfo
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testTypeMappingInfo() throws Fault {
    TypeMappingInfo tmi = new TypeMappingInfo();
    tmi.setEncodingStyle("string");
    tmi.setXMLType(new QName("string", "string"));
    tmi.setJavaTypeName("string");
    tmi.setSerializerFactoryName("string");
    tmi.setDeserializerFactoryName("string");
  }

  /**
   * @testName: testTypeMappingRegistryInfo
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testTypeMappingRegistryInfo() throws Fault {
    TypeMappingRegistryInfo tmri = new TypeMappingRegistryInfo();
    tmri.getTypeMappingInfo("string", new QName("string", "string"));
    tmri.getExtraTypeNamesCount();
  }

}
