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

package com.sun.ts.tests.internal.implementation.sjsas.jaxrpc.com.sun.xml.rpc.tools.wscompile.wsdl_import_test;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import java.io.*;
import java.net.*;
import java.util.*;
import java.rmi.*;

import com.sun.javatest.Status;

public class Client extends EETest {
  private ImportTest importTest;

  private ImportTest2 importTest2;

  private ImportTest3 importTest3;

  private ImportTest4 importTest4;

  private ImportTest5 importTest5;

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
    String tsHome;
    try {
      tsHome = p.getProperty("ts_home");
      logMsg("tsHome=" + tsHome);
    } catch (Exception e) {
      throw new Fault("setup failed", e);
    }
    importTest = new ImportTest(tsHome);
    importTest2 = new ImportTest2(tsHome);
    importTest3 = new ImportTest3(tsHome);
    importTest4 = new ImportTest4(tsHome);
    importTest5 = new ImportTest5(tsHome);
    logMsg("setup ok");
  }

  public void cleanup() throws Fault {
    logMsg("cleanup ok");
  }

  /*
   * @testName: runImportTests
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void runImportTests() throws Fault {
    TestUtil.logTrace("runImportTests");
    boolean pass = true;
    boolean status;
    try {
      importTest.runTests();
      importTest2.runTests();
      importTest3.runTests();
      importTest4.runTests();
      importTest5.runTests();
    } catch (Exception e) {
      throw new Fault("runImportTests failed", e);
    }

    if (!pass)
      throw new Fault("runImportTests failed");
  }
}
