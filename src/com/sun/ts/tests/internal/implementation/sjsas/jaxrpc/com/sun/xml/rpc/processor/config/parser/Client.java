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

package com.sun.ts.tests.internal.implementation.sjsas.jaxrpc.com.sun.xml.rpc.processor.config.parser;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest;
import java.util.Properties;

import com.sun.xml.rpc.processor.config.parser.*;
import com.sun.xml.rpc.processor.config.HandlerChainInfo;
import com.sun.xml.rpc.streaming.XMLReaderFactory;
import com.sun.xml.rpc.streaming.XMLReader;
import com.sun.xml.rpc.processor.util.ClientProcessorEnvironment;

import java.io.PipedInputStream;
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
   * @testName: testHandlerChainInfoData
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testHandlerChainInfoData() throws Fault {
    HandlerChainInfoData hcid = null;
    hcid = new HandlerChainInfoData();
    hcid.setClientHandlerChainInfo(new HandlerChainInfo());
    hcid.getClientHandlerChainInfo();
    hcid.setServerHandlerChainInfo(new HandlerChainInfo());
    hcid.getServerHandlerChainInfo();
  }

  /**
   * @testName: testParserUtil
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testParserUtil() throws Fault {
    ParserUtil pu = null;
    pu = new ParserUtil();
    // XMLReader xr = new XMLReaderFactoryImpl().createXMLReader(new
    // PipedInputStream());
    XMLReader xr = XMLReaderFactory.newInstance()
        .createXMLReader(new PipedInputStream());
    try {
      pu.getNonEmptyAttribute(xr, "string");
    } catch (Exception e) {
    }
    try {
      pu.getQNameAttribute(xr, "string");
    } catch (Exception e) {
    }
    try {
      pu.fail("string", xr);
    } catch (Exception e) {
    }
    try {
      pu.failWithFullName("string", xr);
    } catch (Exception e) {
    }
    try {
      pu.failWithLocalName("string", xr);
    } catch (Exception e) {
    }
    try {
      pu.failWithLocalName("string", xr, "string");
    } catch (Exception e) {
    }
  }

  /**
   * @testName: testJ2EEModelInfoParser
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testJ2EEModelInfoParser() throws Fault {
    ClientProcessorEnvironment env = new ClientProcessorEnvironment(
        new ByteArrayOutputStream(), null, null);
    // Configuration config = new Configuration(env);

    J2EEModelInfoParser jmip = null;
    jmip = new J2EEModelInfoParser(env);
    XMLReader xr = XMLReaderFactory.newInstance()
        .createXMLReader(new PipedInputStream());
    try {
      jmip.parse(xr);
    } catch (Exception e) {
    }
  }

  /**
   * @testName: testModelFileModelInfoParser
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testModelFileModelInfoParser() throws Fault {
    ClientProcessorEnvironment env = new ClientProcessorEnvironment(
        new ByteArrayOutputStream(), null, null);

    ModelFileModelInfoParser mfmip = null;
    mfmip = new ModelFileModelInfoParser(env);
    XMLReader xr = XMLReaderFactory.newInstance()
        .createXMLReader(new PipedInputStream());
    try {
      mfmip.parse(xr);
    } catch (Exception e) {
    }
  }

  /**
   * @testName: testNoMetadataModelInfoParser
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testNoMetadataModelInfoParser() throws Fault {
    ClientProcessorEnvironment env = new ClientProcessorEnvironment(
        new ByteArrayOutputStream(), null, null);

    NoMetadataModelInfoParser nmmip = null;
    nmmip = new NoMetadataModelInfoParser(env);
    XMLReader xr = XMLReaderFactory.newInstance()
        .createXMLReader(new PipedInputStream());
    try {
      nmmip.parse(xr);
    } catch (Exception e) {
    }
  }

}
