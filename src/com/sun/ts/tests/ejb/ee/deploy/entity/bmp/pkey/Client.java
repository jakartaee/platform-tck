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
 * @(#)Client.java	1.17 03/05/16
 */

package com.sun.ts.tests.ejb.ee.deploy.entity.bmp.pkey;

import java.util.Properties;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.lib.util.TSNamingContext;
import com.sun.ts.tests.common.dao.DAOFactory;
import com.sun.ts.tests.common.dao.coffee.variants.CompoundPK;
import com.sun.ts.tests.common.dao.coffee.variants.StringPKCoffeeDAO;
import com.sun.ts.tests.common.dao.coffee.variants.LongPKCoffeeDAO;
import com.sun.ts.tests.common.dao.coffee.variants.FloatPKCoffeeDAO;
import com.sun.ts.tests.common.dao.coffee.variants.CompoundPKCoffeeDAO;
import com.sun.javatest.Status;

public class Client extends EETest {

  private static final String pref = "java:comp/env/ejb/";

  private static final String stringLookup = pref + "StringBean";

  private static final String longLookup = pref + "LongBean";

  private static final String floatLookup = pref + "FloatBean";

  private static final String compoundLookup = pref + "CompoundBean";

  private TSNamingContext ctx = null;

  private Properties props = null;

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /**
   * @class.setup_props: org.omg.CORBA.ORBClass; java.naming.factory.initial;
   *
   * @class.testArgs: -ap tssql.stmt
   */
  public void setup(String[] args, Properties props) throws Fault {

    try {
      this.props = props;
      logTrace("Getting naming context...");
      ctx = new TSNamingContext();

      logMsg("Client: Initializing DB tables...");
      DAOFactory.getInstance().getStringPKCoffeeDAO().cleanup();
      DAOFactory.getInstance().getLongPKCoffeeDAO().cleanup();
      DAOFactory.getInstance().getFloatPKCoffeeDAO().cleanup();
      DAOFactory.getInstance().getCompoundPKCoffeeDAO().cleanup();

      logMsg("Client: Setup OK!");
    } catch (Exception e) {
      throw new Fault("Setup failed:", e);
    }
  }

  /**
   * @testName: testStringPK
   *
   * @assertion_ids: EJB:SPEC:10147
   *
   * @test_Strategy: Package a BMP bean with a String Primary Key.
   *
   *                 Check that you can: - Create bean instances - Discover
   *                 these instances with findByPrimaryKey() - Compare the bean
   *                 instances (EJBObject.isIdentical()) - Get the primary keys
   *                 using getPrimaryKey() - Remove the beans using
   *                 EJBHome.remove([Primary Key])
   */
  public void testStringPK() throws Fault {
    final String refPK1 = "cof001";
    final String refPK2 = "cof007";

    StringBeanHome home = null;
    StringBean bean1 = null;
    StringBean bean2 = null;
    StringBean bean3;
    StringBean bean4;
    String valPK1;
    String valPK2;
    boolean pass = true;

    try {
      logTrace("Looking up home...");
      home = (StringBeanHome) ctx.lookup(stringLookup, StringBeanHome.class);
      logTrace("Client: Creating bean1 and bean2 instance...");
      bean1 = home.create(props, refPK1, "Arabica", 10);
      bean2 = home.create(props, refPK2, "Java", 12);

      logTrace("Client: Locate beans using primary keys...");
      bean3 = home.findByPrimaryKey(refPK1);
      bean4 = home.findByPrimaryKey(refPK2);

      logMsg("Client: Check we can call the beans...");
      bean1.ping();
      bean2.ping();
      bean3.ping();
      bean4.ping();

      logMsg("Client: Check beans are identical...");
      pass = bean1.isIdentical(bean3) && bean3.isIdentical(bean1);
      if (!pass) {
        throw new Exception("bean1 and bean3 should be identical!");
      }
      pass = bean2.isIdentical(bean4) && bean4.isIdentical(bean2);
      if (!pass) {
        throw new Exception("bean2 and bean4 should be identical!");
      }

      logMsg("Client: Comparing primary keys...");
      valPK1 = (String) bean3.getPrimaryKey();
      valPK2 = (String) bean4.getPrimaryKey();
      pass = valPK1.equals(refPK1) && refPK1.equals(valPK1);
      if (!pass) {
        throw new Exception("bean1 and bean3 PK should match!");
      }
      pass = valPK2.equals(refPK2) && refPK2.equals(valPK2);
      if (!pass) {
        throw new Exception("bean2 and bean4 PK should match!");
      }

      logMsg("Client: Remove the beans...");
      home.remove(refPK1);
      bean1 = null;
      home.remove(refPK2);
      bean2 = null;

    } catch (Exception e) {
      logErr("Client: Caught exception: " + e);
      throw new Fault("String PKEY test failed: " + e, e);
    } finally {
      /*
       * Make sure we attempt to cleanup the beans even if the test fails
       */

      try {
        if (null != bean1) {
          home.remove(refPK1);
        }
      } catch (Exception e) {
        logTrace("Client: Ignoring exception on bean remove" + e);
      }
      try {
        if (null != bean2) {
          home.remove(refPK2);
        }
      } catch (Exception e) {
        logTrace("Client: Ignoring exception on bean2 remove" + e);
      }
    }

    /* Test pass */
  }

  /**
   * @testName: testLongPK
   *
   * @assertion_ids: EJB:SPEC:10147
   *
   * @test_Strategy: Package a BMP bean with a Long primary key
   *
   *                 Check that you can: - Create bean instances - Discover
   *                 these instances with findByPrimaryKey() - Compare the bean
   *                 instances (EJBObject.isIdentical()) - Get the primary keys
   *                 using getPrimaryKey() - Remove the beans using
   *                 EJBHome.remove([Primary Key])
   */
  public void testLongPK() throws Fault {
    final Long refPK1 = new Long(1515);
    final Long refPK2 = new Long(1789);

    LongBeanHome home = null;
    LongBean bean1 = null;
    LongBean bean2 = null;
    LongBean bean3;
    LongBean bean4;
    Long valPK1;
    Long valPK2;
    boolean pass = true;

    try {
      logTrace("Looking up home...");
      home = (LongBeanHome) ctx.lookup(longLookup, LongBeanHome.class);
      logTrace("Client: Creating bean1 and bean2 instance...");
      bean1 = home.create(props, refPK1.longValue(), "Arabica", 10);
      bean2 = home.create(props, refPK2.longValue(), "Java", 12);

      logTrace("Client: Locate beans using primary keys...");
      bean3 = home.findByPrimaryKey(refPK1);
      bean4 = home.findByPrimaryKey(refPK2);

      logMsg("Client: Check we can call the beans...");
      bean1.ping();
      bean2.ping();
      bean3.ping();
      bean4.ping();

      logMsg("Client: Check beans are identical...");
      pass = bean1.isIdentical(bean3) && bean3.isIdentical(bean1);
      if (!pass) {
        throw new Exception("bean1 and bean3 should be identical!");
      }
      pass = bean2.isIdentical(bean4) && bean4.isIdentical(bean2);
      if (!pass) {
        throw new Exception("bean2 and bean4 should be identical!");
      }

      logMsg("Client: Comparing primary keys...");
      valPK1 = (Long) bean3.getPrimaryKey();
      valPK2 = (Long) bean4.getPrimaryKey();
      pass = valPK1.equals(refPK1) && refPK1.equals(valPK1);
      if (!pass) {
        throw new Exception("bean1 and bean3 PK should match!");
      }
      pass = valPK2.equals(refPK2) && refPK2.equals(valPK2);
      if (!pass) {
        throw new Exception("bean2 and bean4 PK should match!");
      }

      logMsg("Client: Remove the beans...");
      home.remove(refPK1);
      bean1 = null;
      home.remove(refPK2);
      bean2 = null;

    } catch (Exception e) {
      logErr("Client: Caught exception: " + e);
      throw new Fault("Long PKEY test failed: " + e, e);
    } finally {
      /*
       * Make sure we attempt to cleanup the beans even if the test fails
       */

      try {
        if (null != bean1) {
          home.remove(refPK1);
        }
      } catch (Exception e) {
        logTrace("Client: Ignoring exception on bean remove" + e);
      }
      try {
        if (null != bean2) {
          home.remove(refPK2);
        }
      } catch (Exception e) {
        logTrace("Client: Ignoring exception on bean2 remove" + e);
      }
    }

    /* Test pass */
  }

  /**
   * @testName: testFloatPK
   *
   * @assertion_ids: EJB:SPEC:10147
   *
   * @test_Strategy: Package a BMP bean with a Float primary key
   *
   *                 Check that you can: - Create bean instances - Discover
   *                 these instances with findByPrimaryKey() - Compare the bean
   *                 instances (EJBObject.isIdentical()) - Get the primary keys
   *                 using getPrimaryKey() - Remove the beans using
   *                 EJBHome.remove([Primary Key])
   */
  public void testFloatPK() throws Fault {
    final Float refPK1 = new Float(37.2);
    final Float refPK2 = new Float(9.5);

    FloatBeanHome home = null;
    FloatBean bean1 = null;
    FloatBean bean2 = null;
    FloatBean bean3;
    FloatBean bean4;
    Float valPK1;
    Float valPK2;
    boolean pass = true;

    try {
      logTrace("Looking up home...");
      home = (FloatBeanHome) ctx.lookup(floatLookup, FloatBeanHome.class);
      logTrace("Client: Creating bean1 and bean2 instance...");
      bean1 = home.create(props, refPK1.floatValue(), "Arabica", 10);
      bean2 = home.create(props, refPK2.floatValue(), "Java", 12);

      logTrace("Client: Locate beans using primary keys...");
      bean3 = home.findByPrimaryKey(refPK1);
      bean4 = home.findByPrimaryKey(refPK2);

      logMsg("Client: Check we can call the beans...");
      bean1.ping();
      bean2.ping();
      bean3.ping();
      bean4.ping();

      logMsg("Client: Check beans are identical...");
      pass = bean1.isIdentical(bean3) && bean3.isIdentical(bean1);
      if (!pass) {
        throw new Exception("bean1 and bean3 should be identical!");
      }
      pass = bean2.isIdentical(bean4) && bean4.isIdentical(bean2);
      if (!pass) {
        throw new Exception("bean2 and bean4 should be identical!");
      }

      logMsg("Client: Comparing primary keys...");
      valPK1 = (Float) bean3.getPrimaryKey();
      valPK2 = (Float) bean4.getPrimaryKey();
      pass = valPK1.equals(refPK1) && refPK1.equals(valPK1);
      if (!pass) {
        throw new Exception("bean1 and bean3 PK should match!");
      }
      pass = valPK2.equals(refPK2) && refPK2.equals(valPK2);
      if (!pass) {
        throw new Exception("bean2 and bean4 PK should match!");
      }

      logMsg("Client: Remove the beans...");
      home.remove(refPK1);
      bean1 = null;
      home.remove(refPK2);
      bean2 = null;

    } catch (Exception e) {
      logErr("Client: Caught exception: " + e);
      throw new Fault("Float PKEY test failed: " + e, e);
    } finally {
      /*
       * Make sure we attempt to cleanup the beans even if the test fails
       */

      try {
        if (null != bean1) {
          home.remove(refPK1);
        }
      } catch (Exception e) {
        logTrace("Client: Ignoring exception on bean remove" + e);
      }
      try {
        if (null != bean2) {
          home.remove(refPK2);
        }
      } catch (Exception e) {
        logTrace("Client: Ignoring exception on bean2 remove" + e);
      }
    }

    /* Test pass */
  }

  /**
   * @testName: testCompoundPK
   *
   * @assertion_ids: EJB:SPEC:10147
   *
   * @test_Strategy: Package a BMP bean with a compound primary key
   *
   *                 Check that you can: - Create bean instances - Discover
   *                 these instances with findByPrimaryKey() - Compare the bean
   *                 instances (EJBObject.isIdentical()) - Get the primary keys
   *                 using getPrimaryKey() - Remove the beans using
   *                 EJBHome.remove([Primary Key])
   */
  public void testCompoundPK() throws Fault {
    final CompoundPK refPK1 = new CompoundPK(1, "cof0001", (float) 37.2);
    final CompoundPK refPK2 = new CompoundPK(1, "cof0001", (float) 7.5);

    CompoundBeanHome home = null;
    CompoundBean bean1 = null;
    CompoundBean bean2 = null;
    CompoundBean bean3;
    CompoundBean bean4;
    CompoundPK valPK1;
    CompoundPK valPK2;
    boolean pass = true;

    try {
      logTrace("Looking up home...");
      home = (CompoundBeanHome) ctx.lookup(compoundLookup,
          CompoundBeanHome.class);
      logTrace("Client: Creating bean1 and bean2 instance...");
      bean1 = home.create(props, refPK1, "Arabica", 10);
      bean2 = home.create(props, refPK2, "Java", 12);

      logTrace("Client: Locate beans using primary keys...");
      bean3 = home.findByPrimaryKey(refPK1);
      bean4 = home.findByPrimaryKey(refPK2);

      logMsg("Client: Check we can call the beans...");
      bean1.ping();
      bean2.ping();
      bean3.ping();
      bean4.ping();

      logMsg("Client: Check beans are identical...");
      pass = bean1.isIdentical(bean3) && bean3.isIdentical(bean1);
      if (!pass) {
        throw new Exception("bean1 and bean3 should be identical!");
      }
      pass = bean2.isIdentical(bean4) && bean4.isIdentical(bean2);
      if (!pass) {
        throw new Exception("bean2 and bean4 should be identical!");
      }

      logMsg("Client: Comparing primary keys...");
      valPK1 = (CompoundPK) bean3.getPrimaryKey();
      valPK2 = (CompoundPK) bean4.getPrimaryKey();
      pass = valPK1.equals(refPK1) && refPK1.equals(valPK1);
      if (!pass) {
        throw new Exception("bean1 and bean3 PK should match!");
      }
      pass = valPK2.equals(refPK2) && refPK2.equals(valPK2);
      if (!pass) {
        throw new Exception("bean2 and bean4 PK should match!");
      }

      logMsg("Client: Remove the beans...");
      home.remove(refPK1);
      bean1 = null;
      home.remove(refPK2);
      bean2 = null;

    } catch (Exception e) {
      logErr("Client: Caught exception: " + e);
      throw new Fault("Compound PKEY test failed: " + e, e);
    } finally {
      /*
       * Make sure we attempt to cleanup the beans even if the test fails
       */

      try {
        if (null != bean1) {
          home.remove(refPK1);
        }
      } catch (Exception e) {
        logTrace("Client: Ignoring exception on bean remove" + e);
      }
      try {
        if (null != bean2) {
          home.remove(refPK2);
        }
      } catch (Exception e) {
        logTrace("Client: Ignoring exception on bean2 remove" + e);
      }
    }

    /* Test pass */
  }

  public void cleanup() throws Fault {
    logMsg("Client: cleanup");
  }
}
