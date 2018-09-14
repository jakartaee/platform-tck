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

package com.sun.ts.tests.jpa.core.entityManager2;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.CleanupMethod;
import com.sun.ts.lib.harness.SetupMethod;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jpa.common.PMClientBase;

import javax.persistence.*;
import javax.persistence.criteria.*;
import javax.persistence.metamodel.Metamodel;
import java.util.*;

public class Client extends PMClientBase {

  Employee[] empRef = new Employee[5];

  Order[] orders = new Order[5];

  Properties props = null;

  Map map = new HashMap<String, Object>();

  Employee emp = new Employee(1, "foo", "bar", getUtilDate("2000-02-14"),
      (float) 35000.0);

  String dataBaseName = null;

  final static String ORACLE = "oracle";

  public Client() {
  }

  public static void main(String[] args) {
    Client theTests = new Client();

    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /*
   * setup() is called before each test
   *
   * @class.setup_props: jdbc.db;
   */
  public void setup(String[] args, Properties p) throws Fault {
    TestUtil.logTrace("setup");
    this.props = p;
    try {
      super.setup(args, p);
      map.putAll(getEntityManager().getProperties());
      map.put("foo", "bar");
      displayMap(map);
      dataBaseName = p.getProperty("jdbc.db");
    } catch (Exception e) {
      TestUtil.logErr("Exception: ", e);
      throw new Fault("Setup failed:", e);
    }
  }

  /*
   * setupOrderData() is called before each test
   *
   * @class.setup_props: jdbc.db;
   */
  public void setupOrderData(String[] args, Properties p) throws Fault {
    TestUtil.logTrace("setupOrderData");
    this.props = p;
    try {
      super.setup(args, p);
      removeTestData();
      createOrderData();
      map.putAll(getEntityManager().getProperties());
      map.put("foo", "bar");
      displayMap(map);
      dataBaseName = p.getProperty("jdbc.db");
    } catch (Exception e) {
      TestUtil.logErr("Exception: ", e);
      throw new Fault("Setup failed:", e);
    }
  }

  public void cleanup() throws Fault {
    TestUtil.logTrace("cleanup complete, calling super.cleanup");
    super.cleanup();
  }

  public void cleanupData() throws Fault {
    TestUtil.logTrace("Cleanup data");
    removeTestData();
    cleanup();
  }

  /*
   * @testName: findExceptionsTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:465; PERSISTENCE:JAVADOC:466;
   * PERSISTENCE:JAVADOC:467; PERSISTENCE:JAVADOC:468; PERSISTENCE:JAVADOC:474;
   * PERSISTENCE:JAVADOC:474; PERSISTENCE:SPEC:592;
   * 
   * @test_Strategy: Call EntityManager.find() method with various invalid
   * argument combinations and verify various exceptions are thrown
   */
  public void findExceptionsTest() throws Fault {
    int pass = 0;
    TestUtil.logMsg("Testing findClassObjectIllegalArgumentException");

    TestUtil.logMsg("Invalid Object test");
    try {
      getEntityTransaction().begin();
      getEntityManager().find(Client.class, 1);
      getEntityTransaction().commit();
      TestUtil.logErr("IllegalArgumentException not thrown");
    } catch (IllegalArgumentException e) {
      TestUtil.logTrace("IllegalArgumentException Caught as Expected.");
      pass += 1;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }
    TestUtil.logMsg("Invalid PK test");

    try {
      getEntityTransaction().begin();
      getEntityManager().find(Order.class, "PK");
      getEntityTransaction().commit();
      TestUtil.logErr("IllegalArgumentException not thrown");
    } catch (IllegalArgumentException e) {
      TestUtil.logTrace("IllegalArgumentException Caught as Expected.");
      pass += 1;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }
    TestUtil.logMsg("Null PK test");

    try {
      getEntityTransaction().begin();
      getEntityManager().find(Order.class, null);
      getEntityTransaction().commit();
      TestUtil.logErr("IllegalArgumentException not thrown");
    } catch (IllegalArgumentException e) {
      TestUtil.logTrace("IllegalArgumentException Caught as Expected.");
      pass += 1;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }

    TestUtil.logMsg("Testing findClassObjectMapIllegalArgumentException");

    Map<String, Object> myMap = new HashMap<String, Object>();
    myMap.put("some.cts.specific.property", "nothing.in.particular");

    TestUtil.logTrace("Invalid Object test");
    try {
      getEntityTransaction().begin();
      getEntityManager().find(Client.class, 1, myMap);
      getEntityTransaction().commit();
      TestUtil.logErr("IllegalArgumentException not thrown");
    } catch (IllegalArgumentException e) {
      TestUtil.logTrace("IllegalArgumentException Caught as Expected.");
      pass += 1;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }
    TestUtil.logMsg("Invalid PK test");

    try {
      getEntityTransaction().begin();
      getEntityManager().find(Order.class, "PK", myMap);
      getEntityTransaction().commit();
      TestUtil.logErr("IllegalArgumentException not thrown");
    } catch (IllegalArgumentException e) {
      TestUtil.logTrace("IllegalArgumentException Caught as Expected.");
      pass += 1;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }
    TestUtil.logMsg("Null PK test");

    try {
      getEntityTransaction().begin();
      getEntityManager().find(Order.class, null, myMap);
      getEntityTransaction().commit();
      TestUtil.logErr("IllegalArgumentException not thrown");
    } catch (IllegalArgumentException e) {
      TestUtil.logTrace("IllegalArgumentException Caught as Expected.");
      pass += 1;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }

    TestUtil
        .logMsg("Testing findClassObjectLockModeTypeIllegalArgumentException");

    TestUtil.logMsg("Invalid Object test");
    try {
      getEntityTransaction().begin();
      getEntityManager().find(Client.class, 1, LockModeType.NONE);
      getEntityTransaction().commit();
      TestUtil.logErr("IllegalArgumentException not thrown");
    } catch (IllegalArgumentException e) {
      TestUtil.logTrace("IllegalArgumentException Caught as Expected.");
      pass += 1;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }
    TestUtil.logMsg("Invalid PK test");

    try {
      getEntityTransaction().begin();
      getEntityManager().find(Order.class, "PK", LockModeType.NONE);
      getEntityTransaction().commit();
      TestUtil.logErr("IllegalArgumentException not thrown");
    } catch (IllegalArgumentException e) {
      TestUtil.logTrace("IllegalArgumentException Caught as Expected.");
      pass += 1;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }
    TestUtil.logMsg("Null PK test");

    try {
      getEntityTransaction().begin();
      getEntityManager().find(Order.class, null, LockModeType.NONE);
      getEntityTransaction().commit();
      TestUtil.logErr("IllegalArgumentException not thrown");
    } catch (IllegalArgumentException e) {
      TestUtil.logTrace("IllegalArgumentException Caught as Expected.");
      pass += 1;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }

    TestUtil.logMsg(
        "Testing findClassObjectLockModeTypeTransactionRequiredException");
    // Make sure there is no transaction active
    try {
      if (getEntityTransaction().isActive()) {
        TestUtil.logTrace("A transaction is active, execute rollback");
        getEntityTransaction().rollback();
      } else {
        TestUtil.logTrace("No transaction is active");
      }
    } catch (Exception fe) {
      TestUtil.logErr("Unexpected exception rolling back TX:", fe);
    }
    try {
      getEntityManager().find(Order.class, 1, LockModeType.PESSIMISTIC_READ);
      TestUtil.logErr("TransactionRequiredException not thrown");
    } catch (TransactionRequiredException tre) {
      TestUtil.logTrace("TransactionRequiredException Caught as Expected.");
      pass += 1;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }

    TestUtil.logMsg(
        "Testing findClassObjectLockModeTypeMapIllegalArgumentException");

    TestUtil.logMsg("Invalid Object test");
    try {
      getEntityTransaction().begin();
      getEntityManager().find(Client.class, 1, LockModeType.NONE, myMap);
      getEntityTransaction().commit();
      TestUtil.logErr("IllegalArgumentException not thrown");
    } catch (IllegalArgumentException e) {
      TestUtil.logTrace("IllegalArgumentException Caught as Expected.");
      pass += 1;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }
    TestUtil.logMsg("Invalid PK test");

    try {
      getEntityTransaction().begin();
      getEntityManager().find(Order.class, "PK", LockModeType.NONE, myMap);
      getEntityTransaction().commit();
      TestUtil.logErr("IllegalArgumentException not thrown");
    } catch (IllegalArgumentException e) {
      TestUtil.logTrace("IllegalArgumentException Caught as Expected.");
      pass += 1;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }
    TestUtil.logMsg("Null PK test");

    try {
      getEntityTransaction().begin();
      getEntityManager().find(Order.class, null, LockModeType.NONE, myMap);
      getEntityTransaction().commit();
      TestUtil.logErr("IllegalArgumentException not thrown");
    } catch (IllegalArgumentException e) {
      TestUtil.logTrace("IllegalArgumentException Caught as Expected.");
      pass += 1;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }

    TestUtil.logMsg(
        "Testing findClassObjectLockModeTypeMapTransactionRequiredException");
    // Make sure there is no transaction active
    try {
      if (getEntityTransaction().isActive()) {
        TestUtil.logTrace("A transaction is active, execute rollback");
        getEntityTransaction().rollback();
      }
    } catch (Exception fe) {
      TestUtil.logErr("Unexpected exception rolling back TX:", fe);
    }
    try {
      getEntityManager().find(Order.class, 0, LockModeType.PESSIMISTIC_READ,
          myMap);
      TestUtil.logErr("TransactionRequiredException not thrown");

    } catch (TransactionRequiredException tre) {
      TestUtil.logTrace("TransactionRequiredException Caught as Expected.");
      pass += 1;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);

    }

    if (pass != 14) {
      throw new Fault("findExceptionsTest failed");
    }
  }

  /*
   * @testName: flushExceptionsTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:479
   * 
   * @test_Strategy: Call EntityManager.flush() method with various invalid
   * argument combinations and verify various exceptions are thrown
   */
  public void flushExceptionsTest() throws Fault {
    boolean pass = false;
    try {

      getEntityManager().flush();
      TestUtil.logErr("TransactionRequiredException was not thrown");
    } catch (TransactionRequiredException e) {
      TestUtil.logTrace("TransactionRequiredException Caught as Expected.");
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }

    if (!pass) {
      throw new Fault("flushExceptionsTest failed");
    }
  }

  /*
   * @testName: lockTransactionRequiredExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:491; PERSISTENCE:JAVADOC:492;
   * PERSISTENCE:JAVADOC:498; PERSISTENCE:JAVADOC:499
   * 
   * @test_Strategy: Call EntityManager.lock() method
   */
  @SetupMethod(name = "setupOrderData")
  @CleanupMethod(name = "cleanupData")
  public void lockTransactionRequiredExceptionTest() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;
    Map<String, Object> myMap = new HashMap<String, Object>();

    myMap.put("some.cts.specific.property", "nothing.in.particular");

    TestUtil.logMsg(
        "Testing TransactionRequiredException for lock(Object, LockModeType)");
    try {
      Order o = getEntityManager().find(Order.class, 1);
      getEntityManager().lock(o, LockModeType.WRITE);
      TestUtil.logErr("TransactionRequiredException not thrown");
    } catch (TransactionRequiredException e) {
      TestUtil.logTrace("TransactionRequiredException Caught as Expected.");
      pass1 = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }

    TestUtil.logMsg(
        "Testing TransactionRequiredException for lock(Object, LockModeType, Map)");
    try {
      Order o = getEntityManager().find(Order.class, 1);
      getEntityManager().lock(o, LockModeType.WRITE, myMap);
      TestUtil.logErr("TransactionRequiredException not thrown");
    } catch (TransactionRequiredException e) {
      TestUtil.logTrace("TransactionRequiredException Caught as Expected.");
      pass2 = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }

    if (!pass1 || !pass2) {
      throw new Fault("lockTransactionRequiredExceptionTest failed");
    }
  }

  /*
   * @testName: entityManagerMethodsRuntimeExceptionsCauseRollback1Test
   * 
   * @assertion_ids: PERSISTENCE:SPEC:611; PERSISTENCE:SPEC:592;
   * 
   * @test_Strategy: Call EntityManager.contains(Class) that causes
   * RuntimeException and verify Transaction is set for rollback
   */
  public void entityManagerMethodsRuntimeExceptionsCauseRollback1Test()
      throws Fault {
    boolean pass = false;
    try {
      getEntityTransaction().begin();
      getEntityManager().contains(PMClientBase.class);
      TestUtil.logErr("RuntimeException not thrown");
    } catch (RuntimeException e) {
      TestUtil.logTrace("RuntimeException Caught as Expected.");
      if (!getEntityTransaction().getRollbackOnly()) {
        TestUtil.logErr("Transaction was not marked for rollback");
      } else {
        TestUtil.logTrace("Transaction was marked for rollback");
        pass = true;
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }

    if (!pass) {
      throw new Fault(
          "entityManagerMethodsRuntimeExceptionsCauseRollback1Test failed");
    }
  }

  /*
   * @testName: entityManagerMethodsRuntimeExceptionsCauseRollback2Test
   * 
   * @assertion_ids: PERSISTENCE:SPEC:611;
   * 
   * @test_Strategy: Call EntityManager.createNamedQuery(String) that causes
   * RuntimeException and verify Transaction is set for rollback
   */
  public void entityManagerMethodsRuntimeExceptionsCauseRollback2Test()
      throws Fault {
    boolean pass = false;
    try {
      getEntityTransaction().begin();
      getEntityManager().createNamedQuery("doesnotexist");
      TestUtil.logErr("RuntimeException not thrown");
    } catch (RuntimeException e) {
      TestUtil.logTrace("RuntimeException Caught as Expected.");
      if (!getEntityTransaction().getRollbackOnly()) {
        TestUtil.logErr("Transaction was not marked for rollback");
      } else {
        TestUtil.logTrace("Transaction was marked for rollback");
        pass = true;
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
      pass = false;
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }

    if (!pass) {
      throw new Fault(
          "entityManagerMethodsRuntimeExceptionsCauseRollback2Test failed");
    }
  }

  /*
   * @testName: entityManagerMethodsRuntimeExceptionsCauseRollback3Test
   * 
   * @assertion_ids: PERSISTENCE:SPEC:611;
   * 
   * @test_Strategy: Call EntityManager.createNamedQuery(String,Class) that
   * causes RuntimeException and verify Transaction is set for rollback
   */
  public void entityManagerMethodsRuntimeExceptionsCauseRollback3Test()
      throws Fault {
    boolean pass = false;

    try {
      getEntityTransaction().begin();
      getEntityManager().createNamedQuery("doesnotexist", PMClientBase.class);
      TestUtil.logErr("RuntimeException not thrown");
    } catch (RuntimeException e) {
      TestUtil.logTrace("RuntimeException Caught as Expected.");
      if (!getEntityTransaction().getRollbackOnly()) {
        TestUtil.logErr("Transaction was not marked for rollback");
      } else {
        TestUtil.logTrace("Transaction was marked for rollback");
        pass = true;
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
      pass = false;
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }

    if (!pass) {
      throw new Fault(
          "entityManagerMethodsRuntimeExceptionsCauseRollback3Test failed");
    }
  }

  /*
   * @testName: entityManagerMethodsRuntimeExceptionsCauseRollback4Test
   * 
   * @assertion_ids: PERSISTENCE:SPEC:611;
   * 
   * @test_Strategy: Call EntityManager.createNamedStoredProcedureQuery(String)
   * that causes RuntimeException and verify Transaction is set for rollback
   */
  public void entityManagerMethodsRuntimeExceptionsCauseRollback4Test()
      throws Fault {
    boolean pass = false;
    try {
      getEntityTransaction().begin();
      getEntityManager().createNamedStoredProcedureQuery("doesnotexist");
      TestUtil.logErr("RuntimeException not thrown");
    } catch (RuntimeException e) {
      TestUtil.logTrace("RuntimeException Caught as Expected.");
      if (!getEntityTransaction().getRollbackOnly()) {
        TestUtil.logErr("Transaction was not marked for rollback");
      } else {
        TestUtil.logTrace("Transaction was marked for rollback");
        pass = true;
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
      pass = false;
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }

    if (!pass) {
      throw new Fault(
          "entityManagerMethodsRuntimeExceptionsCauseRollback4Test failed");
    }
  }

  /*
   * @testName: entityManagerMethodsRuntimeExceptionsCauseRollback5Test
   * 
   * @assertion_ids: PERSISTENCE:SPEC:611;
   * 
   * @test_Strategy: Call EntityManager.createQuery(CriteriaDelete) that causes
   * RuntimeException and verify Transaction is set for rollback
   */
  public void entityManagerMethodsRuntimeExceptionsCauseRollback5Test()
      throws Fault {
    boolean pass = false;
    try {
      getEntityTransaction().begin();
      CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
      CriteriaDelete<DoesNotExist> cd = cbuilder
          .createCriteriaDelete(DoesNotExist.class);
      cd.from(DoesNotExist.class);
      try {
        Query q = getEntityManager().createQuery(cd);
        TestUtil.logMsg("RuntimeException wasn't thrown, try executing it");
        q.executeUpdate();
        TestUtil.logErr("RuntimeException not thrown");
      } catch (RuntimeException e) {
        TestUtil.logTrace("RuntimeException Caught as Expected.", e);
        if (!getEntityTransaction().getRollbackOnly()) {
          TestUtil.logErr("Transaction was not marked for rollback");
        } else {
          TestUtil.logTrace("Transaction was marked for rollback");
          pass = true;
        }
      } catch (Exception e) {
        TestUtil.logErr("Unexpected exception occurred", e);
      }
    } catch (Exception e) {
      TestUtil.logErr(
          "Unexpected exception occurred while creating CriteriaDelete", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }

    if (!pass) {
      throw new Fault(
          "entityManagerMethodsRuntimeExceptionsCauseRollback5Test failed");
    }
  }

  /*
   * @testName: entityManagerMethodsRuntimeExceptionsCauseRollback6Test
   * 
   * @assertion_ids: PERSISTENCE:SPEC:611;
   * 
   * @test_Strategy: Call EntityManager.createQuery(CriteriaQuery) that causes
   * RuntimeException and verify Transaction is set for rollback
   */
  public void entityManagerMethodsRuntimeExceptionsCauseRollback6Test()
      throws Fault {
    boolean pass = false;
    try {
      getEntityTransaction().begin();
      CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
      CriteriaQuery<PMClientBase> cquery = cbuilder
          .createQuery(PMClientBase.class);
      Query q = getEntityManager().createQuery(cquery);
      TestUtil.logMsg("RuntimeException wasn't thrown, try executing it");
      q.executeUpdate();
      TestUtil.logErr("RuntimeException not thrown");
    } catch (RuntimeException e) {
      TestUtil.logTrace("RuntimeException Caught as Expected.");
      if (!getEntityTransaction().getRollbackOnly()) {
        TestUtil.logErr("Transaction was not marked for rollback");
      } else {
        TestUtil.logTrace("Transaction was marked for rollback");
        pass = true;
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }

    if (!pass) {
      throw new Fault(
          "entityManagerMethodsRuntimeExceptionsCauseRollback6Test failed");
    }
  }

  /*
   * @testName: entityManagerMethodsRuntimeExceptionsCauseRollback7Test
   * 
   * @assertion_ids: PERSISTENCE:SPEC:611;
   * 
   * @test_Strategy: Call EntityManager.createQuery(CriteriaUpdate) that causes
   * RuntimeException and verify Transaction is set for rollback
   */
  public void entityManagerMethodsRuntimeExceptionsCauseRollback7Test()
      throws Fault {
    boolean pass = false;
    try {
      getEntityTransaction().begin();
      CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

      CriteriaUpdate<DoesNotExist> cu = cbuilder
          .createCriteriaUpdate(DoesNotExist.class);
      Root<DoesNotExist> root = cu.from(DoesNotExist.class);
      cu.where(cbuilder.equal(root.get("id"), 1));
      cu.set(root.get("firstName"), "foobar");
      try {
        Query q = getEntityManager().createQuery(cu);
        TestUtil.logMsg("RuntimeException wasn't thrown, try executing it");
        q.executeUpdate();
        TestUtil.logErr("RuntimeException not thrown");
      } catch (RuntimeException e) {
        TestUtil.logTrace("RuntimeException Caught as Expected.");
        if (!getEntityTransaction().getRollbackOnly()) {
          TestUtil.logErr("Transaction was not marked for rollback");
        } else {
          TestUtil.logTrace("Transaction was marked for rollback");
          pass = true;
        }
      } catch (Exception e) {
        TestUtil.logErr("Unexpected exception occurred", e);
      }
    } catch (Exception e) {
      TestUtil.logErr(
          "Unexpected exception occurred while creating CriteriaUpdate", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }

    if (!pass) {
      throw new Fault(
          "entityManagerMethodsRuntimeExceptionsCauseRollback7Test failed");
    }
  }

  /*
   * @testName: entityManagerMethodsRuntimeExceptionsCauseRollback8Test
   * 
   * @assertion_ids: PERSISTENCE:SPEC:611;
   * 
   * @test_Strategy: Call EntityManager.createQuery(String) that causes
   * RuntimeException and verify Transaction is set for rollback
   */
  public void entityManagerMethodsRuntimeExceptionsCauseRollback8Test()
      throws Fault {
    boolean pass = false;
    try {
      getEntityTransaction().begin();
      getEntityManager().createQuery("invalid");
      TestUtil.logErr("RuntimeException not thrown");
    } catch (RuntimeException e) {
      TestUtil.logTrace("RuntimeException Caught as Expected.");
      if (!getEntityTransaction().getRollbackOnly()) {
        TestUtil.logErr("Transaction was not marked for rollback");
      } else {
        TestUtil.logTrace("Transaction was marked for rollback");
        pass = true;
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }

    if (!pass) {
      throw new Fault(
          "entityManagerMethodsRuntimeExceptionsCauseRollback8Test failed");
    }
  }

  /*
   * @testName: entityManagerMethodsRuntimeExceptionsCauseRollback9Test
   * 
   * @assertion_ids: PERSISTENCE:SPEC:611; PERSISTENCE:SPEC:592;
   * 
   * @test_Strategy: Call EntityManager.createQuery(String,Class) that causes
   * RuntimeException and verify Transaction is set for rollback
   */
  public void entityManagerMethodsRuntimeExceptionsCauseRollback9Test()
      throws Fault {
    boolean pass = false;
    try {
      getEntityTransaction().begin();
      getEntityManager().createQuery("invalid", PMClientBase.class);
      TestUtil.logErr("RuntimeException not thrown");
    } catch (RuntimeException e) {
      TestUtil.logTrace("RuntimeException Caught as Expected.");
      if (!getEntityTransaction().getRollbackOnly()) {
        TestUtil.logErr("Transaction was not marked for rollback");
      } else {
        TestUtil.logTrace("Transaction was marked for rollback");
        pass = true;
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }

    if (!pass) {
      throw new Fault(
          "entityManagerMethodsRuntimeExceptionsCauseRollback9Test failed");
    }
  }

  /*
   * @testName: entityManagerMethodsRuntimeExceptionsCauseRollback10Test
   * 
   * @assertion_ids: PERSISTENCE:SPEC:611;
   * 
   * @test_Strategy: Call EntityManager.createStoredProcedureQuery(String) that
   * causes RuntimeException and verify Transaction is set for rollback
   */
  public void entityManagerMethodsRuntimeExceptionsCauseRollback10Test()
      throws Fault {
    boolean pass = false;
    try {
      getEntityTransaction().begin();
      StoredProcedureQuery spq = getEntityManager()
          .createStoredProcedureQuery("doesnotexist");
      TestUtil.logMsg("RuntimeException not thrown, try executing it");
      spq.execute();
      TestUtil.logErr(
          "RuntimeException was not thrown, after trying to executing it");
    } catch (RuntimeException e) {
      TestUtil.logTrace("RuntimeException Caught as Expected.");
      if (!getEntityTransaction().getRollbackOnly()) {
        TestUtil.logErr("Transaction was not marked for rollback");
      } else {
        TestUtil.logTrace("Transaction was marked for rollback");
        pass = true;
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }

    if (!pass) {
      throw new Fault(
          "entityManagerMethodsRuntimeExceptionsCauseRollback10Test failed");
    }
  }

  /*
   * @testName: entityManagerMethodsRuntimeExceptionsCauseRollback11Test
   * 
   * @assertion_ids: PERSISTENCE:SPEC:611; PERSISTENCE:SPEC:592;
   * 
   * @test_Strategy: Call EntityManager.createStoredProcedureQuery(String,Class)
   * that causes RuntimeException and verify Transaction is set for rollback
   */
  public void entityManagerMethodsRuntimeExceptionsCauseRollback11Test()
      throws Fault {
    boolean pass = false;
    try {
      getEntityTransaction().begin();
      StoredProcedureQuery spq = getEntityManager()
          .createStoredProcedureQuery("doesnotexist", PMClientBase.class);
      TestUtil.logMsg("RuntimeException not thrown, try executing it");
      spq.execute();
      TestUtil.logErr(
          "RuntimeException was not thrown, after trying to executing it");
    } catch (RuntimeException e) {
      TestUtil.logTrace("RuntimeException Caught as Expected.");
      if (!getEntityTransaction().getRollbackOnly()) {
        TestUtil.logErr("Transaction was not marked for rollback");
      } else {
        TestUtil.logTrace("Transaction was marked for rollback");
        pass = true;
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }

    if (!pass) {
      throw new Fault(
          "entityManagerMethodsRuntimeExceptionsCauseRollback11Test failed");
    }
  }

  /*
   * @testName: entityManagerMethodsRuntimeExceptionsCauseRollback12Test
   * 
   * @assertion_ids: PERSISTENCE:SPEC:611;
   * 
   * @test_Strategy: Call
   * EntityManager.createStoredProcedureQuery(String,String) that causes
   * RuntimeException and verify Transaction is set for rollback
   */
  public void entityManagerMethodsRuntimeExceptionsCauseRollback12Test()
      throws Fault {
    boolean pass = false;
    try {
      getEntityTransaction().begin();
      StoredProcedureQuery spq = getEntityManager()
          .createStoredProcedureQuery("doesnotexist", "doesnotexist");
      TestUtil.logMsg("RuntimeException not thrown, try executing it");
      spq.execute();
      TestUtil.logErr(
          "RuntimeException was not thrown, after trying to executing it");
    } catch (RuntimeException e) {
      TestUtil.logTrace("RuntimeException Caught as Expected.");
      if (!getEntityTransaction().getRollbackOnly()) {
        TestUtil.logErr("Transaction was not marked for rollback");
      } else {
        TestUtil.logTrace("Transaction was marked for rollback");
        pass = true;
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }

    if (!pass) {
      throw new Fault(
          "entityManagerMethodsRuntimeExceptionsCauseRollback12Test failed");
    }
  }

  /*
   * @testName: entityManagerMethodsRuntimeExceptionsCauseRollback13Test
   * 
   * @assertion_ids: PERSISTENCE:SPEC:611; PERSISTENCE:SPEC:592;
   * 
   * @test_Strategy: Call EntityManager.detach(Object) that causes
   * RuntimeException and verify Transaction is set for rollback
   */
  public void entityManagerMethodsRuntimeExceptionsCauseRollback13Test()
      throws Fault {
    boolean pass = false;
    try {
      getEntityTransaction().begin();
      getEntityManager().detach(PMClientBase.class);
      TestUtil.logErr("RuntimeException not thrown");
    } catch (RuntimeException e) {
      TestUtil.logTrace("RuntimeException Caught as Expected.");
      if (!getEntityTransaction().getRollbackOnly()) {
        TestUtil.logErr("Transaction was not marked for rollback");
      } else {
        TestUtil.logTrace("Transaction was marked for rollback");
        pass = true;
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
      pass = false;
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }

    if (!pass) {
      throw new Fault(
          "entityManagerMethodsRuntimeExceptionsCauseRollback13Test failed");
    }
  }

  /*
   * @testName: entityManagerMethodsRuntimeExceptionsCauseRollback14Test
   * 
   * @assertion_ids: PERSISTENCE:SPEC:611;
   * 
   * @test_Strategy: Call EntityManager.find(Class,Object) that causes
   * RuntimeException and verify Transaction is set for rollback
   */
  public void entityManagerMethodsRuntimeExceptionsCauseRollback14Test()
      throws Fault {
    boolean pass = false;
    try {
      getEntityTransaction().begin();
      getEntityManager().find(PMClientBase.class, "doesnotexist");
      TestUtil.logErr("RuntimeException not thrown");
    } catch (RuntimeException e) {
      TestUtil.logTrace("RuntimeException Caught as Expected.");
      if (!getEntityTransaction().getRollbackOnly()) {
        TestUtil.logErr("Transaction was not marked for rollback");
      } else {
        TestUtil.logTrace("Transaction was marked for rollback");
        pass = true;
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }

    if (!pass) {
      throw new Fault(
          "entityManagerMethodsRuntimeExceptionsCauseRollback14Test failed");
    }
  }

  /*
   * @testName: entityManagerMethodsRuntimeExceptionsCauseRollback15Test
   * 
   * @assertion_ids: PERSISTENCE:SPEC:611;
   * 
   * @test_Strategy: Call EntityManager.find(Class,Object,LockModeType) that
   * causes RuntimeException and verify Transaction is set for rollback
   */
  public void entityManagerMethodsRuntimeExceptionsCauseRollback15Test()
      throws Fault {
    boolean pass = false;
    try {
      getEntityTransaction().begin();
      getEntityManager().find(PMClientBase.class, "doesnotexist",
          LockModeType.NONE);
      TestUtil.logErr("RuntimeException not thrown");
    } catch (RuntimeException e) {
      TestUtil.logTrace("RuntimeException Caught as Expected.");
      if (!getEntityTransaction().getRollbackOnly()) {
        TestUtil.logErr("Transaction was not marked for rollback");
      } else {
        TestUtil.logTrace("Transaction was marked for rollback");
        pass = true;
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }

    if (!pass) {
      throw new Fault(
          "entityManagerMethodsRuntimeExceptionsCauseRollback15Test failed");
    }
  }

  /*
   * @testName: entityManagerMethodsRuntimeExceptionsCauseRollback16Test
   * 
   * @assertion_ids: PERSISTENCE:SPEC:611;
   * 
   * @test_Strategy: Call EntityManager.find(Class,Object,LockModeType,Map) that
   * causes RuntimeException and verify Transaction is set for rollback
   */
  public void entityManagerMethodsRuntimeExceptionsCauseRollback16Test()
      throws Fault {
    boolean pass = false;
    try {
      getEntityTransaction().begin();
      getEntityManager().find(PMClientBase.class, "doesnotexist",
          LockModeType.NONE, map);
      TestUtil.logErr("RuntimeException not thrown");
    } catch (RuntimeException e) {
      TestUtil.logTrace("RuntimeException Caught as Expected.");
      if (!getEntityTransaction().getRollbackOnly()) {
        TestUtil.logErr("Transaction was not marked for rollback");
      } else {
        TestUtil.logTrace("Transaction was marked for rollback");
        pass = true;
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }

    if (!pass) {
      throw new Fault(
          "entityManagerMethodsRuntimeExceptionsCauseRollback16Test failed");
    }
  }

  /*
   * @testName: entityManagerMethodsRuntimeExceptionsCauseRollback17Test
   * 
   * @assertion_ids: PERSISTENCE:SPEC:611;
   * 
   * @test_Strategy: Call EntityManager.find(Class,Object,Map) that causes
   * RuntimeException and verify Transaction is set for rollback
   */
  public void entityManagerMethodsRuntimeExceptionsCauseRollback17Test()
      throws Fault {
    boolean pass = false;
    try {
      getEntityTransaction().begin();
      getEntityManager().find(PMClientBase.class, "doesnotexist", map);
      TestUtil.logErr("RuntimeException not thrown");
    } catch (RuntimeException e) {
      TestUtil.logTrace("RuntimeException Caught as Expected.");
      if (!getEntityTransaction().getRollbackOnly()) {
        TestUtil.logErr("Transaction was not marked for rollback");
      } else {
        TestUtil.logTrace("Transaction was marked for rollback");
        pass = true;
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }

    if (!pass) {
      throw new Fault(
          "entityManagerMethodsRuntimeExceptionsCauseRollback17Test failed");
    }
  }

  /*
   * @testName: entityManagerMethodsRuntimeExceptionsCauseRollback21Test
   * 
   * @assertion_ids: PERSISTENCE:SPEC:611;
   * 
   * @test_Strategy: Call EntityManager. getLockMode(Object) that causes
   * RuntimeException and verify Transaction is set for rollback
   */
  public void entityManagerMethodsRuntimeExceptionsCauseRollback21Test()
      throws Fault {
    boolean pass = false;
    try {
      getEntityTransaction().begin();
      getEntityManager().getLockMode(PMClientBase.class);
      TestUtil.logErr("RuntimeException not thrown");
    } catch (RuntimeException e) {
      TestUtil.logTrace("RuntimeException Caught as Expected.");
      if (!getEntityTransaction().getRollbackOnly()) {
        TestUtil.logErr("Transaction was not marked for rollback");
      } else {
        TestUtil.logTrace("Transaction was marked for rollback");
        pass = true;
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }

    if (!pass) {
      throw new Fault(
          "entityManagerMethodsRuntimeExceptionsCauseRollback21Test failed");
    }
  }

  /*
   * @testName: entityManagerMethodsRuntimeExceptionsCauseRollback23Test
   * 
   * @assertion_ids: PERSISTENCE:SPEC:611;
   * 
   * @test_Strategy: Call EntityManager.getReference(Class,Object) that causes
   * RuntimeException and verify Transaction is set for rollback
   */
  public void entityManagerMethodsRuntimeExceptionsCauseRollback23Test()
      throws Fault {
    boolean pass = false;
    try {
      getEntityTransaction().begin();
      getEntityManager().getReference(PMClientBase.class, "doesnotexist");
      TestUtil.logErr("RuntimeException not thrown");
    } catch (RuntimeException e) {
      TestUtil.logTrace("RuntimeException Caught as Expected.");
      if (!getEntityTransaction().getRollbackOnly()) {
        TestUtil.logErr("Transaction was not marked for rollback");
      } else {
        TestUtil.logTrace("Transaction was marked for rollback");
        pass = true;
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }

    if (!pass) {
      throw new Fault(
          "entityManagerMethodsRuntimeExceptionsCauseRollback23Test failed");
    }
  }

  /*
   * @testName: entityManagerMethodsRuntimeExceptionsCauseRollback24Test
   * 
   * @assertion_ids: PERSISTENCE:SPEC:611; PERSISTENCE:SPEC:592;
   * 
   * @test_Strategy: Call EntityManager.lock(Object,LockModeType) that causes
   * RuntimeException and verify Transaction is set for rollback
   */
  public void entityManagerMethodsRuntimeExceptionsCauseRollback24Test()
      throws Fault {
    boolean pass = false;
    try {
      getEntityTransaction().begin();
      getEntityManager().lock(PMClientBase.class, LockModeType.NONE);
      TestUtil.logErr("RuntimeException not thrown");
    } catch (RuntimeException e) {
      TestUtil.logTrace("RuntimeException Caught as Expected.");
      if (!getEntityTransaction().getRollbackOnly()) {
        TestUtil.logErr("Transaction was not marked for rollback");
      } else {
        TestUtil.logTrace("Transaction was marked for rollback");
        pass = true;
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }

    if (!pass) {
      throw new Fault(
          "entityManagerMethodsRuntimeExceptionsCauseRollback24Test failed");
    }
  }

  /*
   * @testName: entityManagerMethodsRuntimeExceptionsCauseRollback25Test
   * 
   * @assertion_ids: PERSISTENCE:SPEC:611;
   * 
   * @test_Strategy: Call EntityManager.lock(Object,LockModeType,Map() that
   * causes RuntimeException and verify Transaction is set for rollback
   */
  public void entityManagerMethodsRuntimeExceptionsCauseRollback25Test()
      throws Fault {
    boolean pass = false;
    try {
      getEntityTransaction().begin();
      getEntityManager().lock(PMClientBase.class, LockModeType.NONE, map);
      TestUtil.logErr("RuntimeException not thrown");
    } catch (RuntimeException e) {
      TestUtil.logTrace("RuntimeException Caught as Expected.");
      if (!getEntityTransaction().getRollbackOnly()) {
        TestUtil.logErr("Transaction was not marked for rollback");
      } else {
        TestUtil.logTrace("Transaction was marked for rollback");
        pass = true;
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }

    if (!pass) {
      throw new Fault(
          "entityManagerMethodsRuntimeExceptionsCauseRollback25Test failed");
    }
  }

  /*
   * @testName: entityManagerMethodsRuntimeExceptionsCauseRollback26Test
   * 
   * @assertion_ids: PERSISTENCE:SPEC:611; PERSISTENCE:SPEC:592;
   * 
   * @test_Strategy: Call EntityManager.merge(Class) that causes
   * RuntimeException and verify Transaction is set for rollback
   */
  public void entityManagerMethodsRuntimeExceptionsCauseRollback26Test()
      throws Fault {
    boolean pass = false;
    try {
      getEntityTransaction().begin();
      getEntityManager().merge(PMClientBase.class);
      TestUtil.logErr("RuntimeException not thrown");
    } catch (RuntimeException e) {
      TestUtil.logTrace("RuntimeException Caught as Expected.");
      if (!getEntityTransaction().getRollbackOnly()) {
        TestUtil.logErr("Transaction was not marked for rollback");
      } else {
        TestUtil.logTrace("Transaction was marked for rollback");
        pass = true;
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }

    if (!pass) {
      throw new Fault(
          "entityManagerMethodsRuntimeExceptionsCauseRollback26Test failed");
    }
  }

  /*
   * @testName: entityManagerMethodsRuntimeExceptionsCauseRollback27Test
   * 
   * @assertion_ids: PERSISTENCE:SPEC:611; PERSISTENCE:SPEC:592;
   * 
   * @test_Strategy: Call EntityManager.persist(Object) that causes
   * RuntimeException and verify Transaction is set for rollback
   */
  public void entityManagerMethodsRuntimeExceptionsCauseRollback27Test()
      throws Fault {
    boolean pass = false;
    try {
      getEntityTransaction().begin();
      getEntityManager().persist(PMClientBase.class);
      TestUtil.logErr("RuntimeException not thrown");
    } catch (RuntimeException e) {
      TestUtil.logTrace("RuntimeException Caught as Expected.");
      if (!getEntityTransaction().getRollbackOnly()) {
        TestUtil.logErr("Transaction was not marked for rollback");
      } else {
        TestUtil.logTrace("Transaction was marked for rollback");
        pass = true;
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }

    if (!pass) {
      throw new Fault(
          "entityManagerMethodsRuntimeExceptionsCauseRollback27Test failed");
    }
  }

  /*
   * @testName: entityManagerMethodsRuntimeExceptionsCauseRollback28Test
   * 
   * @assertion_ids: PERSISTENCE:SPEC:611; PERSISTENCE:SPEC:592;
   * 
   * @test_Strategy: Call EntityManager.refresh(Object) that causes
   * RuntimeException and verify Transaction is set for rollback
   */
  public void entityManagerMethodsRuntimeExceptionsCauseRollback28Test()
      throws Fault {
    boolean pass = false;
    try {
      getEntityTransaction().begin();
      getEntityManager().refresh(PMClientBase.class);
      TestUtil.logErr("RuntimeException not thrown");
    } catch (RuntimeException e) {
      TestUtil.logTrace("RuntimeException Caught as Expected.");
      if (!getEntityTransaction().getRollbackOnly()) {
        TestUtil.logErr("Transaction was not marked for rollback");
      } else {
        TestUtil.logTrace("Transaction was marked for rollback");
        pass = true;
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }

    if (!pass) {
      throw new Fault(
          "entityManagerMethodsRuntimeExceptionsCauseRollback28Test failed");
    }
  }

  /*
   * @testName: entityManagerMethodsRuntimeExceptionsCauseRollback29Test
   * 
   * @assertion_ids: PERSISTENCE:SPEC:611; PERSISTENCE:SPEC:592;
   * 
   * @test_Strategy: Call EntityManager.refresh(Object,LockModeType) that causes
   * RuntimeException and verify Transaction is set for rollback
   */
  public void entityManagerMethodsRuntimeExceptionsCauseRollback29Test()
      throws Fault {
    boolean pass = false;
    try {
      getEntityTransaction().begin();
      getEntityManager().refresh(PMClientBase.class, LockModeType.NONE);
      TestUtil.logErr("RuntimeException not thrown");
    } catch (RuntimeException e) {
      TestUtil.logTrace("RuntimeException Caught as Expected.");
      if (!getEntityTransaction().getRollbackOnly()) {
        TestUtil.logErr("Transaction was not marked for rollback");
      } else {
        TestUtil.logTrace("Transaction was marked for rollback");
        pass = true;
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }

    if (!pass) {
      throw new Fault(
          "entityManagerMethodsRuntimeExceptionsCauseRollback29Test failed");
    }
  }

  /*
   * @testName: entityManagerMethodsRuntimeExceptionsCauseRollback30Test
   * 
   * @assertion_ids: PERSISTENCE:SPEC:611; PERSISTENCE:SPEC:592;
   * 
   * @test_Strategy: Call EntityManager.refresh(Object,LockModeType,Map) that
   * causes RuntimeException and verify Transaction is set for rollback
   */
  public void entityManagerMethodsRuntimeExceptionsCauseRollback30Test()
      throws Fault {
    boolean pass = false;
    try {
      getEntityTransaction().begin();
      getEntityManager().refresh(PMClientBase.class, LockModeType.NONE, map);
      TestUtil.logErr("RuntimeException not thrown");
    } catch (RuntimeException e) {
      TestUtil.logTrace("RuntimeException Caught as Expected.");
      if (!getEntityTransaction().getRollbackOnly()) {
        TestUtil.logErr("Transaction was not marked for rollback");
      } else {
        TestUtil.logTrace("Transaction was marked for rollback");
        pass = true;
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }

    if (!pass) {
      throw new Fault(
          "entityManagerMethodsRuntimeExceptionsCauseRollback30Test failed");
    }
  }

  /*
   * @testName: entityManagerMethodsRuntimeExceptionsCauseRollback31Test
   * 
   * @assertion_ids: PERSISTENCE:SPEC:611; PERSISTENCE:SPEC:592;
   * 
   * @test_Strategy: Call EntityManager.refresh(Object,Map) that causes
   * RuntimeException and verify Transaction is set for rollback
   */
  public void entityManagerMethodsRuntimeExceptionsCauseRollback31Test()
      throws Fault {
    boolean pass = false;
    try {
      getEntityTransaction().begin();
      getEntityManager().refresh(PMClientBase.class, map);
      TestUtil.logErr("RuntimeException not thrown");
    } catch (RuntimeException e) {
      TestUtil.logTrace("RuntimeException Caught as Expected.");
      if (!getEntityTransaction().getRollbackOnly()) {
        TestUtil.logErr("Transaction was not marked for rollback");
      } else {
        TestUtil.logTrace("Transaction was marked for rollback");
        pass = true;
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }

    if (!pass) {
      throw new Fault(
          "entityManagerMethodsRuntimeExceptionsCauseRollback31Test failed");
    }
  }

  /*
   * @testName: entityManagerMethodsRuntimeExceptionsCauseRollback32Test
   * 
   * @assertion_ids: PERSISTENCE:SPEC:611; PERSISTENCE:SPEC:592;
   * 
   * @test_Strategy: Call EntityManager.remove(Object) that causes
   * RuntimeException and verify Transaction is set for rollback
   */
  public void entityManagerMethodsRuntimeExceptionsCauseRollback32Test()
      throws Fault {
    boolean pass = false;
    try {
      getEntityTransaction().begin();
      getEntityManager().remove(PMClientBase.class);
      TestUtil.logErr("RuntimeException not thrown");
    } catch (RuntimeException e) {
      TestUtil.logTrace("RuntimeException Caught as Expected.");
      if (!getEntityTransaction().getRollbackOnly()) {
        TestUtil.logErr("Transaction was not marked for rollback");
      } else {
        TestUtil.logTrace("Transaction was marked for rollback");
        pass = true;
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }

    if (!pass) {
      throw new Fault(
          "entityManagerMethodsRuntimeExceptionsCauseRollback32Test failed");
    }

  }

  /*
   * @testName: refreshTransactionRequiredExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:SPEC:1313;
   *
   * @test_Strategy: Call EntityManager.refresh() method without a transaction
   */
  @SetupMethod(name = "setupOrderData")
  @CleanupMethod(name = "cleanupData")
  public void refreshTransactionRequiredExceptionTest() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;
    TestUtil.logMsg("Testing refresh(Object, LockModeType)");

    try {
      getEntityTransaction().begin();
      Order o = getEntityManager().find(Order.class, 4);
      getEntityTransaction().commit();
      o.setdescription("FOOBAR");
      getEntityManager().refresh(o, LockModeType.PESSIMISTIC_READ);
      TestUtil.logErr("TransactionRequiredException not thrown");
    } catch (TransactionRequiredException tre) {
      TestUtil.logTrace("TransactionRequiredException Caught as Expected.");
      pass1 = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }
    TestUtil.logMsg("Testing refresh(Object, LockModeType, Map)");
    Map<String, Object> myMap = new HashMap<String, Object>();
    myMap.put("some.cts.specific.property", "nothing.in.particular");
    try {
      getEntityTransaction().begin();
      Order o = getEntityManager().find(Order.class, 4);
      getEntityTransaction().commit();
      o.setdescription("FOOBAR");
      getEntityManager().refresh(o, LockModeType.PESSIMISTIC_READ, myMap);
      TestUtil.logErr("TransactionRequiredException not thrown");
    } catch (TransactionRequiredException tre) {
      TestUtil.logTrace("TransactionRequiredException Caught as Expected.");
      pass2 = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }

    if (!pass1 || !pass2) {
      throw new Fault("refreshTransactionRequiredExceptionTest failed");
    }
  }

  /*
   * @testName: lockTransactionRequiredException2Test
   * 
   * @assertion_ids: PERSISTENCE:SPEC:1313;
   *
   * @test_Strategy: Call EntityManager.lock() method without a transaction
   */
  @SetupMethod(name = "setupOrderData")
  @CleanupMethod(name = "cleanupData")
  public void lockTransactionRequiredException2Test() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;
    TestUtil.logMsg("Testing lock(Object, LockModeType)");
    try {
      getEntityTransaction().begin();
      Order o = getEntityManager().find(Order.class, 4);
      getEntityTransaction().commit();
      removeTestData();
      getEntityManager().lock(o, LockModeType.PESSIMISTIC_READ);
      TestUtil.logErr("TransactionRequiredException not thrown");
    } catch (TransactionRequiredException tre) {
      TestUtil.logTrace("TransactionRequiredException Caught as Expected.");
      pass1 = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }
    TestUtil.logMsg("Testing lock(Object, LockModeType, Map)");
    Map<String, Object> myMap = new HashMap<String, Object>();
    myMap.put("some.cts.specific.property", "nothing.in.particular");
    try {
      getEntityTransaction().begin();
      Order o = getEntityManager().find(Order.class, 4);
      getEntityTransaction().commit();
      removeTestData();
      getEntityManager().lock(o, LockModeType.PESSIMISTIC_READ, myMap);
      TestUtil.logErr("TransactionRequiredException not thrown");
    } catch (TransactionRequiredException tre) {
      TestUtil.logTrace("TransactionRequiredException Caught as Expected.");
      pass2 = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }

    if (!pass1 || !pass2) {
      throw new Fault("lockTransactionRequiredException2Test failed");
    }
  }

  private void createOrderData() {

    try {
      getEntityTransaction().begin();
      TestUtil.logMsg("Creating Orders");
      orders[0] = new Order(1, 111, "desc1");
      orders[1] = new Order(2, 222, "desc2");
      orders[2] = new Order(3, 333, "desc3");
      orders[3] = new Order(4, 444, "desc4");
      orders[4] = new Order(5, 555, "desc5");
      for (Order o : orders) {
        TestUtil.logTrace("Persisting order:" + o.toString());
        getEntityManager().persist(o);
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }
  }

  private void removeTestData() {
    TestUtil.logTrace("removeTestData");
    if (getEntityTransaction().isActive()) {
      getEntityTransaction().rollback();
    }
    try {
      getEntityTransaction().begin();
      getEntityManager().createNativeQuery("DELETE FROM PURCHASE_ORDER")
          .executeUpdate();
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Exception encountered while removing entities:", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception re) {
        TestUtil.logErr("Unexpected Exception in removeTestData:", re);
      }
    }
  }
}
