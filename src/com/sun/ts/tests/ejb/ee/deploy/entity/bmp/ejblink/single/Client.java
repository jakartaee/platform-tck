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
 * @(#)Client.java	1.20 03/05/16
 */

package com.sun.ts.tests.ejb.ee.deploy.entity.bmp.ejblink.single;

import java.util.Properties;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.lib.util.TSNamingContext;
import com.sun.ts.tests.common.dao.DAOFactory;
import com.sun.javatest.Status;

public class Client extends EETest {

  private static final String testLookup = "java:comp/env/ejb/TestBean";

  private TestBeanHome home = null;

  private TestBean bean = null;

  private Properties props = null;

  private TSNamingContext nctx = null;

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /**
   * @class.setup_props: org.omg.CORBA.ORBClass; java.naming.factory.initial;
   *                     generateSQL;
   *
   * @class.testArgs: -ap tssql.stmt
   */
  public void setup(String[] args, Properties props) throws Fault {

    try {
      this.props = props;
      logTrace("[Client] getting Naming Context...");
      nctx = new TSNamingContext();

      logTrace("[Client] Initializing BMP table...");
      DAOFactory.getInstance().getCoffeeDAO().cleanup();

      logTrace("[Client] Looking up the Home...");
      home = (TestBeanHome) nctx.lookup(testLookup, TestBeanHome.class);
      logTrace("[Client] Looked up Home!");
    } catch (Exception e) {
      throw new Fault("Setup failed:", e);
    }
  }

  /**
   * @testName: testStatelessInternal
   *
   * @assertion_ids: EJB:SPEC:765
   *
   * @test_Strategy: Deploy a BMP Entity bean (TestBean) referencing a Stateless
   *                 Session bean (StatelessInternal) which is part of the same
   *                 JAR file.
   *
   *                 Check at runtime that TestBean can do a lookup for the EJB
   *                 reference and use it to create a bean. Then invoke on that
   *                 instance a business method to be found only in
   *                 StatelessInternal beans, to check that the EJB reference
   *                 was resolved consistently with the DD.
   */
  public void testStatelessInternal() throws Fault {
    try {
      logTrace("[Client] Creating TestBean...");
      bean = home.create(props, 1, "coffee-1", 10);

      logTrace("[Client] Checking stateless internal references...");
      boolean pass = bean.testStatelessInternal(props);
      bean.remove();

      if (!pass) {
        throw new Fault("Stateless internal EJB ref test failed!");
      }
    } catch (Exception e) {
      logErr("[Client] Caught exception: ", e);
      throw new Fault("Stateless internal EJB ref test failed!" + e, e);
    }
  }

  /**
   * @testName: testStatelessExternal
   *
   * @assertion_ids: EJB:SPEC:765
   *
   * @test_Strategy: Deploy a BMP Entity bean (TestBean) referencing another
   *                 Stateless Session bean (StatelessExternal) which is part of
   *                 another JAR file.
   *
   *                 Check at runtime that TestBean can do a lookup for the EJB
   *                 reference and use it to create a bean. Then invoke on that
   *                 instance a business method to be found only in
   *                 StatelessExternal beans, to check that the EJB reference
   *                 was resolved consistently with the DD.
   */
  public void testStatelessExternal() throws Fault {
    try {
      logTrace("[Client] Creating TestBean...");
      bean = home.create(props, 1, "coffee-1", 10);

      logTrace("[Client] Checking stateless external references...");
      boolean pass = bean.testStatelessExternal(props);
      bean.remove();

      if (!pass) {
        throw new Fault("Stateless internal EJB ref test failed!");
      }
    } catch (Exception e) {
      logErr("[Client] Caught exception: ", e);
      throw new Fault("Stateless external EJB ref test failed!" + e, e);
    }
  }

  /**
   * @testName: testStatefulInternal
   *
   * @assertion_ids: EJB:SPEC:765
   *
   * @test_Strategy: Deploy a BMP Entity bean (TestBean) referencing a Stateful
   *                 Session bean (StatefulInternal) which is part of the same
   *                 JAR file.
   *
   *                 Check at runtime that TestBean can do a lookup for the EJB
   *                 reference and use it to create a bean. Then invoke on that
   *                 instance a business method to be found only in
   *                 StatefulInternal beans, to check that the EJB reference was
   *                 resolved consistently with the DD.
   */
  public void testStatefulInternal() throws Fault {
    try {
      logTrace("[Client] Creating TestBean...");
      bean = home.create(props, 1, "coffee-1", 10);

      logTrace("[Client] Checking stateful internal references...");
      boolean pass = bean.testStatefulInternal(props);
      bean.cleanUpBean();
      bean.remove();

      if (!pass) {
        throw new Fault("Stateful internal EJB ref test failed!");
      }
    } catch (Exception e) {
      logErr("[Client] Caught exception: ", e);
      throw new Fault("Stateful internal EJB ref test failed!" + e, e);
    }
  }

  /**
   * @testName: testStatefulExternal
   *
   * @assertion_ids: EJB:SPEC:765
   *
   * @test_Strategy: Deploy a BMP Entity bean (TestBean) referencing a Stateful
   *                 Session bean (StatefulExternal) which is part of another
   *                 JAR file.
   *
   *                 Check at runtime that TestBean can do a lookup for the EJB
   *                 reference and use it to create a bean. Then invoke on that
   *                 instance a business method to be found only in
   *                 StatefulExternal beans, to check that the EJB reference was
   *                 resolved consistently with the DD.
   */
  public void testStatefulExternal() throws Fault {
    try {
      logTrace("[Client] Creating TestBean...");
      bean = home.create(props, 1, "coffee-1", 10);

      logTrace("[Client] Checking stateful external references...");
      boolean pass = bean.testStatefulExternal(props);
      bean.cleanUpBean();
      bean.remove();

      if (!pass) {
        throw new Fault("Stateful external EJB ref test failed!");
      }
    } catch (Exception e) {
      logErr("[Client] Caught exception: ", e);
      throw new Fault("Stateful external EJB ref test failed!" + e, e);
    }
  }

  /**
   * @testName: testBMPInternal
   *
   * @assertion_ids: EJB:SPEC:765
   *
   * @test_Strategy: Deploy a BMP Entity bean (TestBean) referencing a BMP
   *                 Entity bean (BMPInternal) which is part of the same JAR
   *                 file.
   *
   *                 Check at runtime that TestBean can do a lookup for the EJB
   *                 reference and use it to create a bean. Then invoke on that
   *                 instance a business method to be found only in BMPInternal
   *                 beans, to check that the EJB reference was resolved
   *                 consistently with the DD.
   */
  public void testBMPInternal() throws Fault {
    try {
      logTrace("[Client] Creating TestBean...");
      bean = home.create(props, 1, "coffee-1", 10);

      logTrace("[Client] Checking BMP internal references...");
      boolean pass = bean.testBMPInternal(props);
      bean.remove();

      if (!pass) {
        throw new Fault("BMP internal EJB ref test failed!");
      }
    } catch (Exception e) {
      logErr("[Client] Caught exception: ", e);
      throw new Fault("BMP internal EJB ref test failed!" + e, e);
    }
  }

  /**
   * @testName: testBMPExternal
   *
   * @assertion_ids: EJB:SPEC:765
   *
   * @test_Strategy: Deploy a BMP Entity bean (TestBean) referencing a BMP
   *                 Entity bean (BMPExternal) which is part of another JAR
   *                 file.
   *
   *                 Check at runtime that TestBean can do a lookup for the EJB
   *                 reference and use it to create a bean. Then invoke on that
   *                 instance a business method to be found only in BMPExternal
   *                 beans to check that the EJB reference was resolved
   *                 consistently with the DD.
   */
  public void testBMPExternal() throws Fault {
    try {
      logTrace("[Client] Creating TestBean...");
      bean = home.create(props, 1, "coffee-1", 10);

      logTrace("[Client] Checking BMP external references...");
      boolean pass = bean.testBMPExternal(props);
      bean.remove();

      if (!pass) {
        throw new Fault("BMP external EJB ref test failed!");
      }
    } catch (Exception e) {
      logErr("[Client] Caught exception: ", e);
      throw new Fault("BMP external EJB ref test failed!" + e, e);
    }
  }

  /**
   * @testName: testCMP11Internal
   *
   * @assertion_ids: EJB:SPEC:765
   *
   * @test_Strategy: Deploy a BMP Entity bean (TestBean) referencing a CMP 1.1
   *                 Entity bean (CMP11Internal) which is part of the same JAR
   *                 file.
   *
   *                 Check at runtime that TestBean can do a lookup for the EJB
   *                 reference and use it to create a bean. Then invoke on that
   *                 instance a business method to be found only in
   *                 CMP11Internal beans, to check that the EJB reference was
   *                 resolved consistently with the DD.
   */
  public void testCMP11Internal() throws Fault {
    boolean pass = false;

    try {
      logTrace("[Client] Creating TestBean...");
      bean = home.create(props, 1, "coffee-1", 10);

      logTrace("Client: Checking CMP11 internal references...");
      pass = bean.testCMP11Internal(props);
      bean.remove();

      if (!pass) {
        throw new Fault("CMP11 internal EJB ref test failed!");
      }
    } catch (Exception e) {
      logErr("[Client] Caught exception: ", e);
      throw new Fault("CMP11 internal EJB ref test failed!" + e, e);
    }
  }

  /**
   * @testName: testCMP11External
   *
   * @assertion_ids: EJB:SPEC:765
   *
   * @test_Strategy: Deploy a BMP Entity bean (TestBean) referencing a CMP 1.1
   *                 Entity bean (CMP11External) which is part of another JAR
   *                 file.
   *
   *                 Check at runtime that TestBean can do a lookup for the EJB
   *                 reference and use it to create a bean. Then invoke on that
   *                 instance a business method to be found only in
   *                 CMP11External beans, to check that the EJB reference was
   *                 resolved consistently with the DD.
   */
  public void testCMP11External() throws Fault {
    boolean pass = false;

    try {
      logTrace("[Client] Creating TestBean...");
      bean = home.create(props, 1, "coffee-1", 10);

      logTrace("[Client] Checking CMP11 external references...");
      pass = bean.testCMP11External(props);
      bean.remove();

      if (!pass) {
        throw new Fault("CMP11 external EJB ref test failed!");
      }
    } catch (Exception e) {
      logErr("[Client] Caught exception: ", e);
      throw new Fault("CMP11 external EJB ref test failed!" + e, e);
    }
  }

  /**
   * @testName: testCMP20Internal
   *
   * @assertion_ids: EJB:SPEC:765
   *
   * @test_Strategy: Deploy a BMP Entity bean (TestBean) referencing a CMP 2.0
   *                 Entity bean (CMP20Internal) which is part of the same JAR
   *                 file.
   *
   *                 Check at runtime that TestBean can do a lookup for the EJB
   *                 reference and use it to create a bean. Then invoke on that
   *                 instance a business method to be found only in
   *                 CMP20Internal beans, to check that the EJB reference was
   *                 resolved consistently with the DD.
   */
  public void testCMP20Internal() throws Fault {
    boolean pass = false;

    try {
      logTrace("Client: Creating TestBean...");
      bean = home.create(props, 1, "coffee-1", 10);

      logTrace("[Client] Checking CMP 2.0 internal references...");
      pass = bean.testCMP20Internal(props);
      bean.remove();

      if (!pass) {
        throw new Fault("CMP 2.0 internal EJB ref test failed!");
      }
    } catch (Exception e) {
      logErr("[Client] Caught exception: ", e);
      throw new Fault("CMP 2.0 internal EJB ref test failed!" + e, e);
    }
  }

  /**
   * @testName: testCMP20External
   *
   * @assertion_ids: EJB:SPEC:765
   *
   * @test_Strategy: Deploy a BMP Entity bean (TestBean) referencing a CMP 2.0
   *                 Entity bean (CMP20External) which is part of another JAR
   *                 file.
   *
   *                 Check at runtime that TestBean can do a lookup for the EJB
   *                 reference and use it to create a bean. Then invoke on that
   *                 instance a business method to be found only in
   *                 CMP20External beans, to check that the EJB reference was
   *                 resolved consistently with the DD.
   */
  public void testCMP20External() throws Fault {
    boolean pass = false;

    try {
      logTrace("Client: Creating TestBean...");
      bean = home.create(props, 1, "coffee-1", 10);

      logTrace("[Client] Checking CMP 2.0 external references...");
      pass = bean.testCMP20External(props);
      bean.remove();

      if (!pass) {
        throw new Fault("CMP 2.0 external EJB ref test failed!");
      }
    } catch (Exception e) {
      logErr("[Client] Caught exception: ", e);
      throw new Fault("CMP 2.0 external EJB ref test failed!" + e, e);
    }
  }

  public void cleanup() {
    logTrace("[Client] Cleanup.");
  }
}
