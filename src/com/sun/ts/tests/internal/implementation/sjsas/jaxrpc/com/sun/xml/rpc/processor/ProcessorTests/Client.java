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

package com.sun.ts.tests.internal.implementation.sjsas.jaxrpc.com.sun.xml.rpc.processor.ProcessorTests;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest;
import java.util.Properties;

import com.sun.xml.rpc.processor.*;
import com.sun.xml.rpc.util.localization.LocalizableMessage;

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
   * @testName: testProcessorConstants
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testProcessorConstants() throws Fault {
    ProcessorConstants pc = null;
    pc = new ProcessorConstants();
  }

  /**
   * @testName: testProcessorOptions
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testProcessorOptions() throws Fault {
    ProcessorOptions po = null;
    po = new ProcessorOptions();
  }

  /**
   * @testName: testProcessorException
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void testProcessorException() throws Fault {
    ProcessorException pe = new ProcessorException("string");
    pe.getResourceBundleName();
    pe = new ProcessorException("string", "string");
    final Object o[] = { "string", "string" };
    pe = new ProcessorException("string", o);
    pe = new ProcessorException("string",
        new LocalizableMessage("string", "string"));
  }

}
