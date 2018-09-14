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

package com.sun.ts.tests.internal.implementation.sjsas.jaxrpc.com.sun.xml.rpc.processor.model.exporter;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest;
import java.util.Properties;

import com.sun.xml.rpc.processor.model.Model;
import com.sun.xml.rpc.processor.model.exporter.*;
import com.sun.xml.rpc.util.localization.LocalizableMessage;
import com.sun.xml.rpc.soap.SOAPVersion;
import javax.xml.namespace.QName;

import java.util.*;
import java.io.*;

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
   * @testName: testModelExporter
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testModelExporter() throws Fault {
    ModelExporter me = null;
    try {
      me = new ModelExporter(new PipedOutputStream());
    } catch (Exception e) {
    }
    try {
      me.doExport(new Model());
    } catch (Exception e) {
    }
  }

  /**
   * @testName: testModelImporter
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testModelImporter() throws Fault {
    ModelImporter mi = null;
    try {
      mi = new ModelImporter(new PipedInputStream());
    } catch (Exception e) {
    }
    try {
      mi.doImport();
    } catch (Exception e) {
    }
  }

  /**
   * @testName: testPGraph
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testPGraph() throws Fault {
    PGraph pg = null;
    pg = new PGraph();
    pg.setRoot(new PObject());
    pg.getRoot();
    pg.setVersion("string");
    pg.getVersion();
    QName qn = new QName("string", "string");
    pg.setName(qn);
    pg.getName();
  }

  /**
   * @testName: testPGraphExporter
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testPGraphExporter() throws Fault {
    PGraphExporter me = null;
    try {
      me = new PGraphExporter(new PipedOutputStream());
    } catch (Exception e) {
    }
    try {
      me.doExport(new PGraph());
    } catch (Exception e) {
    }
  }

  /**
   * @testName: testPGraphImporter
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testPGraphImporter() throws Fault {
    PGraphImporter mi = null;
    try {
      mi = new PGraphImporter(new PipedInputStream());
    } catch (Exception e) {
    }
    try {
      mi.doImport();
    } catch (Exception e) {
    }
  }

  /**
   * @testName: testPObject
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testPObject() throws Fault {
    PObject po = null;
    po = new PObject();
    po.setType("string");
    po.getType();
    po.setProperty("string", "string");
    po.getProperty("string");
    po.getProperties();
    po.getPropertyNames();
  }

}
