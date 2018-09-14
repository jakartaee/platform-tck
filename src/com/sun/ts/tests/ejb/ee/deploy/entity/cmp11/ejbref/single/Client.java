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
 * @(#)Client.java	1.15 03/05/16
 */

package com.sun.ts.tests.ejb.ee.deploy.entity.cmp11.ejbref.single;

import java.util.Properties;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.lib.util.TSNamingContext;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.tests.common.dao.DAOFactory;
import com.sun.javatest.Status;

public class Client extends EETest {

  private static final String testLookup = "java:comp/env/ejb/TestBean";

  private static final int beanPK = 1;

  private TestBeanHome home = null;

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

      logTrace("[Client] Getting Naming Context...");
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
   * @test_Strategy: Deploy a CMP 1.1 Entity bean (TestBean) referencing a
   *                 Stateless Session bean (StatelessInternal) which is part of
   *                 the same JAR file. The EJB reference is declared without
   *                 using the optional ejb-link element in the DD.
   *
   *                 Check at runtime that TestBean can do a lookup for the EJB
   *                 reference and use it to create a bean. Then invoke on that
   *                 instance a business method to be found only in
   *                 StatelessInternal beans (to check that the EJB reference
   *                 was resolved consistently with the DD).
   */
  public void testStatelessInternal() throws Fault {
    TestBean bean = null;
    boolean pass;

    try {
      logTrace("[Client] Creating TestBean...");
      bean = home.create(props, beanPK, "coffee-1", 10);
      logTrace("[Client] Checking Stateless internal references...");
      pass = bean.testStatelessInternal(props);

      if (!pass) {
        throw new Fault("Stateless internal EJB ref test failed!");
      }
    } catch (Exception e) {
      logErr("[Client] Caught exception: ", e);
      throw new Fault("Stateless internal EJB ref test failed!" + e, e);
    } finally {
      if (null != bean) {
        try {
          bean.remove();
        } catch (Exception e) {
          TestUtil.logErr("[Client] Ignoring exception on " + "bean removal!",
              e);
        }
      }
    }
  }

  /**
   * @testName: testStatelessExternal
   *
   * @assertion_ids: EJB:SPEC:765
   *
   * @test_Strategy: Deploy a CMP 1.1 Entity bean (TestBean) referencing another
   *                 Stateless Session bean (StatelessExternal) which is part of
   *                 another JAR file. The EJB reference is declared without
   *                 using the optional ejb-link element in the DD.
   *
   *                 Check at runtime that TestBean can do a lookup for the EJB
   *                 reference and use it to create a bean. Then invoke on that
   *                 instance a business method to be found only in
   *                 StatelessExternal beans (to check that the EJB reference
   *                 was resolved consistently with the DD).
   */
  public void testStatelessExternal() throws Fault {
    TestBean bean = null;
    boolean pass;

    try {
      logTrace("[Client] Creating TestBean...");
      bean = home.create(props, beanPK, "coffee-1", 1);
      logTrace("[Client] Checking Stateless external references...");
      pass = bean.testStatelessExternal(props);

      if (!pass) {
        throw new Fault("Stateless external EJB ref test failed!");
      }
    } catch (Exception e) {
      logErr("[Client] Caught exception: ", e);
      throw new Fault("Stateless external EJB ref test failed!" + e, e);
    } finally {
      if (null != bean) {
        try {
          bean.remove();
        } catch (Exception e) {
          TestUtil.logErr("[Client] Ignoring exception on " + "bean removal!",
              e);
        }
      }
    }
  }

  /**
   * @testName: testStatefulInternal
   *
   * @assertion_ids: EJB:SPEC:765
   *
   * @test_Strategy: Deploy a CMP 1.1 Entity bean (TestBean) referencing a
   *                 Stateful Session bean (StatefulInternal) which is part of
   *                 the same JAR file. The EJB reference is declared without
   *                 using the optional ejb-link element in the DD.
   *
   *                 Check at runtime that TestBean can do a lookup for the EJB
   *                 reference and use it to create a bean. Then invoke on that
   *                 instance a business method to be found only in
   *                 StatefulInternal beans (to check that the EJB reference was
   *                 resolved consistently with the DD).
   */
  public void testStatefulInternal() throws Fault {
    TestBean bean = null;
    boolean pass;

    try {
      logTrace("[Client] Creating TestBean...");
      bean = home.create(props, beanPK, "coffee-1", 10);
      logTrace("[Client] Checking Stateful internal references...");
      pass = bean.testStatefulInternal(props);

      if (!pass) {
        throw new Fault("Stateful internal EJB ref test failed!");
      }
    } catch (Exception e) {
      logErr("[Client] Caught exception: ", e);
      throw new Fault("Stateful internal EJB ref test failed!" + e, e);
    } finally {
      if (null != bean) {
        try {
          bean.cleanUpBean();
          bean.remove();
        } catch (Exception e) {
          TestUtil.logErr("[Client] Ignoring exception on " + "bean removal!",
              e);
        }
      }
    }
  }

  /**
   * @testName: testStatefulExternal
   *
   * @assertion_ids: EJB:SPEC:765
   *
   * @test_Strategy: Deploy a CMP 1.1 Entity bean (TestBean) referencing a
   *                 Stateful Session bean (StatefulExternal) which is part of
   *                 another JAR file. The EJB reference is declared without
   *                 using the optional ejb-link element in the DD.
   *
   *                 Check at runtime that TestBean can do a lookup for the EJB
   *                 reference and use it to create a bean. Then invoke on that
   *                 instance a business method to be found only in
   *                 StatefulExternal beans (to check that the EJB reference was
   *                 resolved consistently with the DD).
   */
  public void testStatefulExternal() throws Fault {
    TestBean bean = null;
    boolean pass;

    try {
      logTrace("[Client] Creating TestBean...");
      bean = home.create(props, beanPK, "coffee-1", 10);
      logTrace("[Client] Checking Stateful external references...");
      pass = bean.testStatefulExternal(props);

      if (!pass) {
        throw new Fault("Stateful external EJB ref test failed!");
      }
    } catch (Exception e) {
      logErr("[Client] Caught exception: ", e);
      throw new Fault("Stateful external EJB ref test failed!" + e, e);
    } finally {
      if (null != bean) {
        try {
          bean.cleanUpBean();
          bean.remove();
        } catch (Exception e) {
          TestUtil.logErr("[Client] Ignoring exception on " + "bean removal!",
              e);
        }
      }
    }
  }

  /**
   * @testName: testBMPInternal
   *
   * @assertion_ids: EJB:SPEC:765
   *
   * @test_Strategy: Deploy a CMP 1.1 Entity bean (TestBean) referencing a BMP
   *                 Entity bean (BMPInternal) which is part of the same JAR
   *                 file. The EJB reference is declared without using the
   *                 optional ejb-link element in the DD.
   *
   *                 Check at runtime that TestBean can do a lookup for the EJB
   *                 reference and use it to create a bean. Then invoke on that
   *                 instance a business method to be found only in BMPInternal
   *                 beans (to check that the EJB reference was resolved
   *                 consistently with the DD).
   */
  public void testBMPInternal() throws Fault {
    TestBean bean = null;
    boolean pass;

    try {
      logTrace("[Client] Creating TestBean...");
      bean = home.create(props, beanPK, "coffee-1", 10);
      logTrace("[Client] Checking BMP internal references...");
      pass = bean.testBMPInternal(props);

      if (!pass) {
        throw new Fault("BMP internal EJB ref test failed!");
      }
    } catch (Exception e) {
      logErr("[Client] Caught exception: ", e);
      throw new Fault("BMP internal EJB ref test failed!" + e, e);
    } finally {
      if (null != bean) {
        try {
          bean.remove();
        } catch (Exception e) {
          TestUtil.logErr("[Client] Ignoring exception on " + "bean removal!",
              e);
        }
      }
    }
  }

  /**
   * @testName: testBMPExternal
   *
   * @assertion_ids: EJB:SPEC:765
   *
   * @test_Strategy: Deploy a CMP 1.1 Entity bean (TestBean) referencing a BMP
   *                 Entity bean (BMPExternal) which is part of another JAR
   *                 file. The EJB reference is declared without using the
   *                 optional ejb-link element in the DD.
   *
   *                 Check at runtime that TestBean can do a lookup for the EJB
   *                 reference and use it to create a bean. Then invoke on that
   *                 instance a business method to be found only in BMPExternal
   *                 beans (to check that the EJB reference was resolved
   *                 consistently with the DD).
   */
  public void testBMPExternal() throws Fault {
    TestBean bean = null;
    boolean pass;

    try {
      logTrace("[Client] Creating TestBean...");
      bean = home.create(props, beanPK, "coffee-1", 10);
      logTrace("[Client] Checking BMP external references...");
      pass = bean.testBMPExternal(props);

      if (!pass) {
        throw new Fault("BMP external EJB ref test failed!");
      }
    } catch (Exception e) {
      logErr("[Client] Caught exception: ", e);
      throw new Fault("BMP external EJB ref test failed!" + e, e);
    } finally {
      if (null != bean) {
        try {
          bean.remove();
        } catch (Exception e) {
          TestUtil.logErr("[Client] Ignoring exception on " + "bean removal!",
              e);
        }
      }
    }
  }

  /**
   * @testName: testCMP11Internal
   *
   * @assertion_ids: EJB:SPEC:765
   *
   * @test_Strategy: Deploy a CMP 1.1 Entity bean (TestBean) referencing a CMP
   *                 1.1 Entity bean (CMP11Internal) which is part of the same
   *                 JAR file. The EJB reference is declared without using the
   *                 optional ejb-link element in the DD.
   *
   *                 Check at runtime that TestBean can do a lookup for the EJB
   *                 reference and use it to create a bean. Then invoke on that
   *                 instance a business method to be found only in
   *                 CMP11Internal beans (to check that the EJB reference was
   *                 resolved consistently with the DD).
   */
  public void testCMP11Internal() throws Fault {
    TestBean bean = null;
    boolean pass = false;

    try {
      logTrace("Client: Creating TestBean...");
      bean = home.create(props, beanPK, "coffee-1", 10);
      logTrace("[Client] Checking CMP11 internal references...");
      pass = bean.testCMP11Internal(props);

      if (!pass) {
        throw new Fault("CMP11 internal EJB ref test failed!");
      }
    } catch (Exception e) {
      logErr("[Client] Caught exception: ", e);
      throw new Fault("CMP11 internal EJB ref test failed!" + e, e);
    } finally {
      if (null != bean) {
        try {
          bean.remove();
        } catch (Exception e) {
          TestUtil.logErr("[Client] Ignoring exception on " + "bean removal!",
              e);
        }
      }
    }
  }

  /**
   * @testName: testCMP11External
   *
   * @assertion_ids: EJB:SPEC:765
   *
   * @test_Strategy: Deploy a CMP 1.1 Entity bean (TestBean) referencing a CMP
   *                 1.1 Entity bean (CMP11External) which is part of another
   *                 JAR file. The EJB reference is declared without using the
   *                 optional ejb-link element in the DD.
   *
   *                 Check at runtime that TestBean can do a lookup for the EJB
   *                 reference and use it to create a bean. Then invoke on that
   *                 instance a business method to be found only in
   *                 CMP11External beans (to check that the EJB reference was
   *                 resolved consistently with the DD).
   */
  public void testCMP11External() throws Fault {
    TestBean bean = null;
    boolean pass = false;

    try {
      logTrace("[Client] Creating TestBean...");
      bean = home.create(props, beanPK, "coffee-1", 10);
      logTrace("[Client] Checking CMP11 external references...");
      pass = bean.testCMP11External(props);

      if (!pass) {
        throw new Fault("CMP11 external EJB ref test failed!");
      }
    } catch (Exception e) {
      logErr("[Client] Caught exception: ", e);
      throw new Fault("CMP11 external EJB ref test failed!" + e, e);
    } finally {
      if (null != bean) {
        try {
          bean.remove();
        } catch (Exception e) {
          TestUtil.logErr("[Client] Ignoring exception on " + "bean removal!",
              e);
        }
      }
    }
  }

  /**
   * @testName: testCMP20Internal
   *
   * @assertion_ids: EJB:SPEC:765
   *
   * @test_Strategy: Deploy a CMP 1.1 Entity bean (TestBean) referencing a CMP
   *                 2.0 Entity bean (CMP20Internal) which is part of the same
   *                 JAR file. The EJB reference is declared without using the
   *                 optional ejb-link element in the DD.
   *
   *                 Check at runtime that TestBean can do a lookup for the EJB
   *                 reference and use it to create a bean. Then invoke on that
   *                 instance a business method to be found only in
   *                 CMP20Internal beans (to check that the EJB reference was
   *                 resolved consistently with the DD).
   */
  public void testCMP20Internal() throws Fault {
    TestBean bean = null;
    boolean pass = false;

    try {
      logTrace("[Client] Creating TestBean...");
      bean = home.create(props, beanPK, "coffee-1", 10);
      logTrace("[Client] Checking CMP 2.0 internal references...");
      pass = bean.testCMP20Internal(props);

      if (!pass) {
        throw new Fault("CMP2.0 internal EJB ref test failed!");
      }
    } catch (Exception e) {
      logErr("[Client] Caught exception: ", e);
      throw new Fault("CMP2.0 internal EJB ref test failed!" + e, e);
    } finally {
      if (null != bean) {
        try {
          bean.remove();
        } catch (Exception e) {
          TestUtil.logErr("[Client] Ignoring exception on " + "bean removal!",
              e);
        }
      }
    }
  }

  /**
   * @testName: testCMP20External
   *
   * @assertion_ids: EJB:SPEC:765
   *
   * @test_Strategy: Deploy a CMP 1.1 Entity bean (TestBean) referencing a CMP
   *                 2.0 Entity bean (CMP20External) which is part of another
   *                 JAR file. The EJB reference is declared without using the
   *                 optional ejb-link element in the DD.
   *
   *                 Check at runtime that TestBean can do a lookup for the EJB
   *                 reference and use it to create a bean. Then invoke on that
   *                 instance a business method to be found only in
   *                 CMP20External beans (to check that the EJB reference was
   *                 resolved consistently with the DD).
   */
  public void testCMP20External() throws Fault {
    TestBean bean = null;
    boolean pass = false;

    try {
      logTrace("[Client] Creating TestBean...");
      bean = home.create(props, beanPK, "coffee-1", 10);
      logTrace("[Client] Checking CMP 2.0 external references...");
      pass = bean.testCMP20External(props);

      if (!pass) {
        throw new Fault("CMP2.0 external EJB ref test failed!");
      }
    } catch (Exception e) {
      logErr("[Client] Caught exception: ", e);
      throw new Fault("CMP2.0 external EJB ref test failed!" + e, e);
    } finally {
      if (null != bean) {
        try {
          bean.remove();
        } catch (Exception e) {
          TestUtil.logErr("[Client] Ignoring exception on " + "bean removal!",
              e);
        }
      }
    }
  }

  public void cleanup() {
    logTrace("[Client] cleanup()");
  }
}
