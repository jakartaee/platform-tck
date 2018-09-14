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

package com.sun.ts.tests.ejb30.zombie;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.harness.EETest.Fault;
import java.util.Properties;

public class Client extends EETest {

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /*
   * @class.setup_props: jms_timeout; user; password; harness.log.traceflag;
   * harness.log.port;
   */
  public void setup(String[] args, Properties p) throws Fault {
  }

  public void cleanup() throws Fault {
  }

  /*
   * @testName: test1
   * 
   * @assertion_ids:
   * 
   * @test_Strategy: wait for the mdb to consume messages that may have been
   * left in the queue.
   *
   */
  public void test1() throws Fault {
    final int minutes = 2;
    System.out.println("Please wait for " + minutes + " minutes...");
    try {
      Thread.sleep(minutes * 60 * 1000);
    } catch (InterruptedException e) {
      // ignore
    }
  }

}
