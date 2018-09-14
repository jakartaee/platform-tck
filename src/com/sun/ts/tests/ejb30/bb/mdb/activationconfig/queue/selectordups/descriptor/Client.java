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

package com.sun.ts.tests.ejb30.bb.mdb.activationconfig.queue.selectordups.descriptor;

import com.sun.javatest.Status;

public class Client
    extends com.sun.ts.tests.ejb30.bb.mdb.activationconfig.common.ClientBase {

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /*
   * @class.setup_props: jms_timeout; user; password; harness.log.traceflag;
   * harness.log.port;
   */

  /*
   * @testName: test1
   * 
   * @assertion_ids: EJB:SPEC:520; EJB:SPEC:521; EJB:SPEC:524; EJB:JAVADOC:4;
   * EJB:JAVADOC:5
   * 
   * @test_Strategy: test activation-config related elements in deployment
   * descriptors, and their annotation counterparts.
   */

  /*
   * @testName: negativeTest1
   * 
   * @assertion_ids: EJB:SPEC:520; EJB:SPEC:521; EJB:SPEC:524; EJB:JAVADOC:4;
   * EJB:JAVADOC:5
   * 
   * @test_Strategy: test activation-config related elements in deployment
   * descriptors, and their annotation counterparts.
   */

  /*
   * @testName: negativeTest2
   * 
   * @assertion_ids: EJB:SPEC:520; EJB:SPEC:521; EJB:SPEC:524; EJB:JAVADOC:4;
   * EJB:JAVADOC:5
   * 
   * @test_Strategy: test activation-config related elements in deployment
   * descriptors, and their annotation counterparts.
   */
}
