/*
 * Copyright (c) 2016, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jsonp.api.common;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.harness.ServiceEETest;
import com.sun.ts.tests.jsonp.api.patchtests.PatchTests;
import java.util.Properties;

// $Id$
/**
 * Common JSON-P test.
 */
public class JsonPTest extends ServiceEETest {

  /**
   * Java VM code execution entry point.
   * 
   * @param args
   *          Command line arguments.
   */
  public static void main(String[] args) {
    PatchTests theTests = new PatchTests();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /**
   * Test setup.
   * 
   * @param args
   *          Command line arguments.
   * @param p
   *          Test properties.
   * @throws Fault
   */
  public void setup(String[] args, Properties p) throws EETest.Fault {
    // logMsg("setup ok");
  }

  /**
   * Test cleanup.
   * 
   * @throws Fault
   */
  public void cleanup() throws EETest.Fault {
    // logMsg("cleanup ok");
  }

}
