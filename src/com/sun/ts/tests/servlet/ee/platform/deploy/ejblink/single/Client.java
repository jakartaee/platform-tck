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

package com.sun.ts.tests.servlet.ee.platform.deploy.ejblink.single;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.tests.common.dao.DAOFactory;
import com.sun.ts.tests.common.web.WebServer;
import java.util.Properties;

public class Client extends EETest {

  private static final String webAlias = "/servlet_ee_platform_deploy_ejblink_single_web/testDriver";

  protected WebServer webServer;

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /**
   * Test setup
   *
   * @class.setup_props: org.omg.CORBA.ORBClass; java.naming.factory.initial;
   *                     webServerHost, the web server host; webServerPort, the
   *                     web server port; generateSQL;
   *
   * @class.testArgs: -ap tssql.stmt
   */
  public void setup(String[] args, Properties props) throws Fault {
    boolean ok;

    try {
      logMsg("[Client] setup()");
      webServer = WebServer.newInstance(props);

    } catch (Exception e) {
      throw new Fault("Setup failed:", e);
    }
  }

  /**
   * @testName: testStateless
   *
   * @assertion_ids: Servlet:SPEC:116.2; JavaEE:SPEC:10118
   *
   * @test_Strategy: Package a servlet in a WAR file whose Deployment Descriptor
   *                 declares an EJB reference to a Stateless Session bean.
   *                 Deploy the WAR and have the servlet lookup the bean home
   *                 interface. Use it to create a bean. Then invoke on that
   *                 bean instance a business method to be found only in this
   *                 particular bean (to check that the EJB reference was
   *                 resolved consistently with the DD).
   */
  public void testStateless() throws Fault {
    try {
      boolean pass = true;

      pass = webServer.test(webAlias, "testStateless");
      if (!pass) {
        throw new Fault("testStateless failed");
      }
    } catch (Exception e) {
      logErr("Caught unexpected exception: " + e.getMessage());
      throw new Fault("testStateless failed", e);
    }
  }

  /**
   * @testName: testStateful
   *
   * @assertion_ids: Servlet:SPEC:116.2; JavaEE:SPEC:10118
   *
   * @test_Strategy: Package a servlet in a WAR file whose Deployment Descriptor
   *                 declares an EJB reference to a Stateful Session bean.
   *                 Deploy the WAR and have the servlet lookup the bean home
   *                 interface. Use it to create a bean. Then invoke on that
   *                 bean instance a business method to be found only in this
   *                 particular bean (to check that the EJB reference was
   *                 resolved consistently with the DD).
   */
  public void testStateful() throws Fault {
    try {
      boolean pass = true;

      pass = webServer.test(webAlias, "testStateful");
      webServer.test(webAlias, "cleanUpBean");
      if (!pass) {
        throw new Fault("testStateful failed");
      }
    } catch (Exception e) {
      logErr("Caught unexpected exception: " + e.getMessage());
      throw new Fault("testStateful failed", e);
    }
  }

  /**
   * @testName: testBMP
   *
   * @assertion_ids: Servlet:SPEC:116.2; JavaEE:SPEC:10118
   *
   * @test_Strategy: Package a servlet in a WAR file whose Deployment Descriptor
   *                 declares an EJB reference to a BMP Session bean. Deploy the
   *                 WAR and have the servlet lookup the bean home interface.
   *                 Use it to create a bean. Then invoke on that bean instance
   *                 a business method to be found only in this particular bean
   *                 (to check that the EJB reference was resolved consistently
   *                 with the DD).
   */
  public void testBMP() throws Fault {
    boolean pass = true;

    try {
      logTrace("[Client] Initializing BMP table...");
      DAOFactory.getInstance().getCoffeeDAO().cleanup();

      pass = webServer.test(webAlias, "testBMP");
      if (!pass) {
        throw new Fault("testBMP failed");
      }
    } catch (Exception e) {
      logErr("Caught unexpected exception: " + e.getMessage());
      throw new Fault("testBMP failed", e);
    }
  }

  /**
   * @testName: testCMP11
   *
   * @assertion_ids: Servlet:SPEC:116.2; JavaEE:SPEC:10118
   *
   * @test_Strategy: Package a servlet in a WAR file whose Deployment Descriptor
   *                 declares an EJB reference to a CMP 1.1 Session bean. Deploy
   *                 the WAR and have the servlet lookup the bean home
   *                 interface. Use it to create a bean. Then invoke on that
   *                 bean instance a business method to be found only in this
   *                 particular bean (to check that the EJB reference was
   *                 resolved consistently with the DD).
   */
  public void testCMP11() throws Fault {
    try {
      boolean pass = true;

      pass = webServer.test(webAlias, "testCMP11");
      if (!pass) {
        throw new Fault("testCMP11 failed");
      }
    } catch (Exception e) {
      logErr("Caught unexpected exception: " + e.getMessage());
      throw new Fault("testCMP11 failed", e);
    }
  }

  /**
   * @testName: testCMP20
   *
   * @assertion_ids: Servlet:SPEC:116.2; JavaEE:SPEC:10118
   *
   * @test_Strategy: Package a servlet in a WAR file whose Deployment Descriptor
   *                 declares an EJB reference to a CMP 2.0 Session bean. Deploy
   *                 the WAR and have the servlet lookup the bean home
   *                 interface. Use it to create a bean. Then invoke on that
   *                 bean instance a business method to be found only in this
   *                 particular bean (to check that the EJB reference was
   *                 resolved consistently with the DD).
   */
  public void testCMP20() throws Fault {
    try {
      boolean pass = true;

      pass = webServer.test(webAlias, "testCMP20");
      if (!pass) {
        throw new Fault("testCMP20 failed");
      }
    } catch (Exception e) {
      logErr("Caught unexpected exception: " + e.getMessage());
      throw new Fault("testCMP20 failed", e);
    }
  }

  public void cleanup() throws Fault {
    logMsg("[Client] cleanup()");
  }

}
