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

package com.sun.ts.tests.jpa.ee.packaging.web.scope;

import com.sun.javatest.Status;
import com.sun.ts.tests.jpa.ee.util.AbstractUrlClient;

import java.io.PrintWriter;

public class Client extends AbstractUrlClient {

  public static final String SERVLET_NAME = "ServletTest";

  public static final String CONTEXT_ROOT = "/jpa_ee_packaging_web_scope";

  /**
   * Entry point for different-VM execution. It should delegate to method
   * run(String[], PrintWriter, PrintWriter), and this method should not contain
   * any test configuration.
   */
  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, new PrintWriter(System.out),
        new PrintWriter(System.err));
    s.exit();
  }

  /**
   * Entry point for same-VM execution. In different-VM execution, the main
   * method delegates to this method.
   */
  public Status run(String args[], PrintWriter out, PrintWriter err) {
    setServletName(SERVLET_NAME);
    setContextRoot(CONTEXT_ROOT);
    return super.run(args, out, err);
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   */

  /*
   * @testName: test1
   * 
   * @assertion_ids: PERSISTENCE:SPEC:894; PERSISTENCE:SPEC:898;
   * PERSISTENCE:SPEC:900; PERSISTENCE:SPEC:902
   * 
   * @test_Strategy: In JavaEE environments, the root of the persistence unit
   * may be the WEB-INF classes directory of a WAR file. The persistence.xml
   * file is therefore contained in the resided in the WEB-INF/classes/META-INF
   * directory.
   *
   * A container mananaged entity managed is obtained by the application through
   * direct lookup of the entity manager in the JNDI namespace.
   *
   * Client -> SERVLET -> ENTITY -> DB
   *
   */

  public void test1() throws Fault {
    TEST_PROPS.setProperty(APITEST, "test1");
    invoke();
  }

}
