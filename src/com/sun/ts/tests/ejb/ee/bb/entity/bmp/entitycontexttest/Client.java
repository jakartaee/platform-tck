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

package com.sun.ts.tests.ejb.ee.bb.entity.bmp.entitycontexttest;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import java.io.*;
import java.util.*;
import javax.ejb.*;
import javax.transaction.*;
import java.rmi.*;
import com.sun.ts.tests.common.dao.DAOFactory;

import com.sun.javatest.Status;

//*****************************************************************************
//EntityContext test for entity bean access to it's runtime container context
//BMP entity bean
//*****************************************************************************

public class Client extends EETest {
  private static final String testName = "EntityContextTest";

  // JNDI names of test beans
  private static final String testBean = "java:comp/env/ejb/TestBean";

  private static final String testProps = "entitycontexttest.properties";

  private static final String testDir = System.getProperty("user.dir");

  private Properties props = null;

  private TestBean beanRef = null;

  private TestBeanHome beanHome = null;

  private TSNamingContext nctx = null;

  private static final String user = "user", password = "password";

  private String user_value, password_value, role1_value = "Employee",
      role2_value = "Manager";

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /* Test setup */

  /*
   * @class.setup_props: org.omg.CORBA.ORBClass; java.naming.factory.initial;
   * user; password;
   * 
   * @class.testArgs: -ap tssql.stmt
   */

  public void setup(String[] args, Properties p) throws Fault {
    props = p;
    user_value = props.getProperty(user);
    password_value = props.getProperty(password);

    logMsg("user_value=" + user_value);
    logMsg("password_value=" + password_value);
    logMsg("role1_value=" + role1_value);
    logMsg("role2_value=" + role2_value);

    try {
      logMsg("Obtain naming context");
      nctx = new TSNamingContext();

      logMsg("Obtain login context and login as: " + user_value);
      TSLoginContext lc = new TSLoginContext();
      lc.login(user_value, password_value);

      logTrace("Client: Initializing BMP table...");
      DAOFactory.getInstance().getCoffeeDAO().cleanup();

      // Get EJB Home ...
      logMsg("Looking up home interface for EJB: " + testBean);
      beanHome = (TestBeanHome) nctx.lookup(testBean, TestBeanHome.class);
      logMsg("Setup ok");
    } catch (Exception e) {
      throw new Fault("Setup failed:", e);
    }
  }

  /* Run test */

  /*
   * @testName: test1
   * 
   * @assertion_ids: EJB:SPEC:450.1
   * 
   * @test_Strategy: Create an Entity BMP Bean. Deploy it on the J2EE server.
   * Verify EJBObject reference returned.
   *
   */

  public void test1() throws Fault {
    boolean pass = false;
    try {
      // create EJB instance
      logMsg("Create EJB instance");
      beanRef = (TestBean) beanHome.create(props, 1, "coffee-1", 1);
      pass = beanRef.getEJBObjectTest();
    } catch (Exception e) {
      throw new Fault("test1 failed", e);
    } finally {
      try {
        if (null != beanRef) {
          beanRef.remove();
        }
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
    }

    if (!pass)
      throw new Fault("test1 failed");
  }

  /*
   * @testName: test2
   * 
   * @assertion_ids: EJB:SPEC:450.2
   * 
   * @test_Strategy: Create an Entity BMP Bean. Deploy it on the J2EE server.
   * Verify EJBHome reference returned.
   *
   */

  public void test2() throws Fault {
    boolean pass = false;
    try {
      // create EJB instance
      logMsg("Create EJB instance");
      beanRef = (TestBean) beanHome.create(props, 1, "coffee-1", 1);
      pass = beanRef.getEJBHomeTest();
    } catch (Exception e) {
      throw new Fault("test2 failed", e);
    } finally {
      try {
        if (null != beanRef) {
          beanRef.remove();
        }
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
    }

    if (!pass)
      throw new Fault("test2 failed");
  }

  /*
   * @testName: test3
   * 
   * @assertion_ids: EJB:SPEC:777
   * 
   * @test_Strategy: Create an Entity BMP Bean. Deploy it on the J2EE server.
   * Verify Properies object received.
   *
   */

  public void test3() throws Fault {
    boolean pass = false;
    try {
      // create EJB instance
      logMsg("Create EJB instance");
      beanRef = (TestBean) beanHome.create(props, 1, "coffee-1", 1);
      pass = beanRef.getEnvironmentTest();
    } catch (Exception e) {
      throw new Fault("test3 failed", e);
    } finally {
      try {
        if (null != beanRef) {
          beanRef.remove();
        }
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
    }

    if (!pass)
      throw new Fault("test3 failed");
  }

  /*
   * @testName: test4
   * 
   * @assertion_ids: EJB:SPEC:450.5
   * 
   * @test_Strategy: Create an Entity BMP Bean. Deploy it on the J2EE server.
   * Verify Principal reference returned.
   *
   */

  public void test4() throws Fault {
    boolean pass = false;
    try {
      // create EJB instance
      logMsg("Create EJB instance");
      beanRef = (TestBean) beanHome.create(props, 1, "coffee-1", 1);
      pass = beanRef.getCallerPrincipalTest(user_value);
    } catch (Exception e) {
      throw new Fault("test4 failed", e);
    } finally {
      try {
        if (null != beanRef) {
          beanRef.remove();
        }
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
    }

    if (!pass)
      throw new Fault("test4 failed");
  }

  /*
   * @testName: test5
   * 
   * @assertion_ids: EJB:SPEC:450.6
   * 
   * @test_Strategy: Create an Entity BMP Bean. Deploy it on the J2EE server.
   * Verify correct identity role. This is a POSITIVE test where caller is in
   * role1.
   *
   */

  public void test5() throws Fault {
    boolean pass = false;
    try {
      // create EJB instance
      logMsg("Create EJB instance");
      beanRef = (TestBean) beanHome.create(props, 1, "coffee-1", 1);
      pass = beanRef.isCallerInRoleTest(role1_value);
    } catch (Exception e) {
      throw new Fault("test5 failed", e);
    } finally {
      try {
        if (null != beanRef) {
          beanRef.remove();
        }
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
    }

    if (!pass)
      throw new Fault("test5 failed");
  }

  /*
   * @testName: test5b
   * 
   * @assertion_ids: EJB:SPEC:450.6
   * 
   * @test_Strategy: Create an Entity BMP Bean. Deploy it on the J2EE server.
   * Verify correct identity role. This is a NEGATIVE test where caller is not
   * in role2.
   *
   */

  public void test5b() throws Fault {
    boolean pass = false;
    try {
      // create EJB instance
      logMsg("Create EJB instance");
      beanRef = (TestBean) beanHome.create(props, 1, "coffee-1", 1);
      pass = beanRef.isCallerInRoleTest(role2_value);
    } catch (Exception e) {
      throw new Fault("test5b failed", e);
    } finally {
      try {
        if (null != beanRef) {
          beanRef.remove();
        }
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
    }

    if (pass)
      throw new Fault("test5b failed");
  }

  /*
   * @testName: test6
   * 
   * @assertion_ids: EJB:SPEC:450.7
   * 
   * @test_Strategy: Create an Entity BMP Bean. Deploy it on the J2EE server.
   * Verify setRollback functionality since entity beans are always
   * container-managed.
   *
   */

  public void test6() throws Fault {
    boolean pass = false;
    try {
      // create EJB instance
      logMsg("Create EJB instance");
      beanRef = (TestBean) beanHome.create(props, 1, "coffee-1", 1);
      pass = beanRef.setRollbackOnlyTest();
    } catch (Exception e) {
      throw new Fault("test6 failed", e);
    } finally {
      try {
        if (null != beanRef) {
          beanRef.remove();
        }
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
    }

    if (!pass)
      throw new Fault("test6 failed");
  }

  /*
   * @testName: test7
   * 
   * @assertion_ids: EJB:SPEC:450.8
   * 
   * @test_Strategy: Create an Entity BMP Bean. Deploy it on the J2EE server.
   * Verify getRollback functionality since entity beans are always
   * container-managed.
   *
   */

  public void test7() throws Fault {
    boolean pass = false;
    try {
      // create EJB instance
      logMsg("Create EJB instance");
      beanRef = (TestBean) beanHome.create(props, 1, "coffee-1", 1);
      pass = beanRef.getRollbackOnlyTest();
    } catch (Exception e) {
      throw new Fault("test7 failed", e);
    } finally {
      try {
        if (null != beanRef) {
          beanRef.remove();
        }
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
    }

    if (!pass)
      throw new Fault("test7 failed");
  }

  /*
   * @testName: test8
   * 
   * @assertion_ids: EJB:SPEC:450.11
   * 
   * @test_Strategy: Create an Entity BMP Bean. Deploy it on the J2EE server.
   * Verify entity bean cannot obtain UserTransaction.
   *
   */

  public void test8() throws Fault {
    boolean pass = false;
    try {
      // create EJB instance
      logMsg("Create EJB instance");
      beanRef = (TestBean) beanHome.create(props, 1, "coffee-1", 1);
      logMsg("EntityBeans cannot obtain UserTransaction interface");
      pass = beanRef.getUserTransactionTest();
    } catch (Exception e) {
      throw new Fault("test8 failed", e);
    } finally {
      try {
        if (null != beanRef) {
          beanRef.remove();
        }
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
    }

    if (!pass)
      throw new Fault("test8 failed");
  }

  /*
   * @testName: test9
   * 
   * @assertion_ids: EJB:SPEC:450.9
   * 
   * @test_Strategy: Create an Entity BMP Bean. Deploy it on the J2EE server.
   * Verify entity bean can obtain its primary key.
   *
   */

  public void test9() throws Fault {
    boolean pass = false;
    try {
      // create EJB instance
      logMsg("Create EJB instance");
      beanRef = (TestBean) beanHome.create(props, 1, "coffee-1", 1);
      logMsg("Entity BMP bean can obtain primary key");
      pass = beanRef.getPrimaryKeyTest(new Integer(1));
    } catch (Exception e) {
      throw new Fault("test9 failed", e);
    } finally {
      try {
        if (null != beanRef) {
          beanRef.remove();
        }
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
    }

    if (!pass)
      throw new Fault("test9 failed");
  }

  /*
   * @testName: test10
   * 
   * @assertion_ids: EJB:SPEC:450.10; EJB:JAVADOC:27
   * 
   * @test_Strategy: Create an Entity BMP Bean. Deploy it on the J2EE server.
   * Verify entity bean can obtain timer service.
   * 
   */

  public void test10() throws Fault {
    boolean pass = false;
    try {
      // create EJB instance
      logMsg("Create EJB instance");
      beanRef = (TestBean) beanHome.create(props, 1, "coffee-1", 1);
      logMsg("Entity BMP Bean can obtain Timer Service");
      pass = beanRef.getTimerServiceTest();
    } catch (Exception e) {
      throw new Fault("test10 failed", e);
    } finally {
      try {
        if (null != beanRef) {
          beanRef.remove();
        }
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
    }

    if (!pass)
      throw new Fault("test10 failed");
  }

  public void cleanup() throws Fault {
    logMsg("cleanup ok");
  }
}
