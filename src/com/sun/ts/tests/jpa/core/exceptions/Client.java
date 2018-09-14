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

package com.sun.ts.tests.jpa.core.exceptions;

import com.sun.javatest.Status;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jpa.common.PMClientBase;

import javax.persistence.*;
import java.util.Properties;

public class Client extends PMClientBase {

  public Client() {
  }

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  public void setup(String[] args, Properties p) throws Fault {
    TestUtil.logTrace("setup");
    try {
      super.setup(args, p);
      TestUtil.logTrace("Cleanup data");
      removeTestData();
    } catch (Exception e) {
      TestUtil.logErr("Exception: ", e);
      throw new Fault("Setup failed:", e);
    }
  }

  /*
   * @testName: TransactionRequiredExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:SPEC:606; PERSISTENCE:JAVADOC:220;
   * JavaEE:SPEC:10006;
   * 
   * @test_Strategy: persist throws a TransactionRequiredException if invoked on
   * a container-managed entity manager of type
   * PersistenceContextType.TRANSACTION and there is no transaction
   *
   * When an EntityManager with an extended persistence context is used (as in
   * Java SE environments) the persist operation may be called regardless
   * whether a transaction is active.
   */

  public void TransactionRequiredExceptionTest() throws Fault {
    boolean pass = false;
    final Coffee newCoffee = new Coffee(1, "hazelnut", 1.0F);

    try {
      TestUtil.logTrace("Invoked persist without an active transaction");
      getEntityManager().persist(newCoffee);

    } catch (javax.persistence.TransactionRequiredException tre) {
      pass = true;
      TestUtil.logTrace("In JavaEE, Exception Caught as Expected: " + tre);
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }

    if ((!pass) && (isStandAloneMode())) {
      TestUtil.logTrace("In JavaSE, Exception Not Thrown as Expected");
      pass = true;
    }

    if (!pass)
      throw new Fault("TransactionRequiredExceptionTest failed");
  }
  /*
   * @testName: TransactionRequiredException2Test
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:219; PERSISTENCE:JAVADOC:157
   * 
   * @test_Strategy: create and throw TransactionRequiredException() and
   * validate contents of Exception
   */

  public void TransactionRequiredException2Test() throws Fault {

    TestUtil.logTrace("Test TransactionRequiredExceptionNullMsg");
    boolean pass = true;

    try {
      throw new TransactionRequiredException();

    } catch (TransactionRequiredException eee) {
      TestUtil.logTrace("TransactionRequiredException Caught as Expected");
      if (eee.getMessage() != null) {
        TestUtil.logErr(
            "TransactionRequiredException should have had null message, actual message="
                + eee.getMessage());
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected Exception Caught", e);
      pass = false;
    }

    TestUtil.logMsg("Test TransactionRequiredExceptionStringMsg");

    String expected = "This is the String message";
    try {
      throw new TransactionRequiredException(expected);

    } catch (TransactionRequiredException eee) {
      TestUtil.logTrace("TransactionRequiredException Caught as Expected");
      String msg = eee.getMessage();
      if (msg != null) {
        if (!msg.equals(expected)) {
          TestUtil.logErr("Expected=" + msg + ", Actual=" + msg);
          pass = false;
        }
      } else {
        TestUtil.logErr("TransactionRequiredException returned null message");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected Exception Caught", e);
      pass = false;
    }

    if (!pass)
      throw new Fault("TransactionRequiredException2Test failed");
  }

  /*
   * @testName: exceptionTest2
   * 
   * @assertion_ids: PERSISTENCE:SPEC:606; PERSISTENCE:SPEC:738;
   * PERSISTENCE:SPEC:740; JavaEE:SPEC:10006
   * 
   * @test_Strategy: flush() throws a
   * javax.persistence.TransactionRequiredException if there is no transaction
   */

  public void exceptionTest2() throws Fault {

    TestUtil.logTrace("Begin exceptionTest2");
    final Coffee newCoffee = new Coffee(2, "french roast", 9.0F);
    boolean pass = false;

    try {
      getEntityTransaction().begin();
      getEntityManager().persist(newCoffee);
      getEntityTransaction().commit();

      TestUtil.logTrace("try flush");
      getEntityManager().flush();

    } catch (javax.persistence.TransactionRequiredException tre) {
      pass = true;
      TestUtil.logTrace("Exception Caught as Expected: " + tre);
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception re) {
        TestUtil.logErr("Unexpected Exception :", re);
      }

    }

    if (!pass)
      throw new Fault("exceptionTest2 failed");

  }

  /*
   * @testName: exceptionTest3
   * 
   * @assertion_ids: PERSISTENCE:SPEC:606; JavaEE:SPEC:10006
   * 
   * @test_Strategy: refresh throws a TransactionRequiredException if invoked on
   * a container-managed entity manager of type
   * PersistenceContextType.TRANSACTION and there is no transaction
   *
   * When an EntityManager with an extended persistence context is used (as in
   * Java SE environments) the refresh operation may be called regardless
   * whether a transaction is active.
   */

  public void exceptionTest3() throws Fault {

    TestUtil.logTrace("Begin exceptionTest3");
    boolean pass = false;
    final Coffee newCoffee = new Coffee(3, "french roast", 9.0F);

    try {

      getEntityTransaction().begin();
      TestUtil.logTrace("Persist Coffee ");
      getEntityManager().persist(newCoffee);
      getEntityTransaction().commit();

      TestUtil.logTrace("Call refresh without an active transaction");
      getEntityManager().refresh(newCoffee);

    } catch (TransactionRequiredException tre) {
      pass = true;
      TestUtil.logTrace("Exception Caught as Expected: " + tre);
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }

      } catch (Exception fe) {
        TestUtil.logErr("Unexpected Exception Caught rolling back TX:", fe);
      }
    }

    if ((!pass) && (isStandAloneMode())) {
      TestUtil.logTrace("In JavaSE, Exception Not Thrown as Expected");
      pass = true;
    }

    if (!pass)
      throw new Fault("exceptionTest3 failed");
  }

  /*
   * @testName: exceptionTest4
   * 
   * @assertion_ids: PERSISTENCE:SPEC:606; JavaEE:SPEC:10006
   * 
   * @test_Strategy: remove throws a TransactionRequiredException if there
   * invoked on a container-managed entity manager of type
   * PersistenceContextType.TRANSACTION and there is no transaction
   *
   * When an EntityManager with an extended persistence context is used (as in
   * Java SE environments) the remove operation may be called regardless whether
   * a transaction is active.
   * 
   */

  public void exceptionTest4() throws Fault {

    TestUtil.logTrace("Begin exceptionTest4");
    final Coffee newCoffee = new Coffee(5, "breakfast blend", 3.0F);
    boolean pass = false;

    try {
      getEntityTransaction().begin();
      getEntityManager().persist(newCoffee);
      getEntityTransaction().commit();

      TestUtil.logTrace("Call remove without an active transaction");
      getEntityManager().remove(newCoffee);

    } catch (javax.persistence.TransactionRequiredException tre) {
      pass = true;
      TestUtil.logTrace("Exception Caught as Expected: " + tre);
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }

      } catch (Exception fe) {
        TestUtil.logErr("Unexpected Exception Caught rolling back TX:", fe);
      }

    }

    if ((!pass) && (isStandAloneMode())) {
      TestUtil.logTrace("In JavaSE, Exception Not Thrown as Expected");
      pass = true;
    }

    if (!pass)
      throw new Fault("exceptionTest4 failed");
  }

  /*
   * @testName: exceptionTest5
   * 
   * @assertion_ids: PERSISTENCE:SPEC:606; PERSISTENCE:JAVADOC:36;
   * JavaEE:SPEC:10006
   * 
   * @test_Strategy: close throws an IllegalStateException will be thrown if the
   * EntityManager is container-managed.
   */

  public void exceptionTest5() throws Fault {

    TestUtil.logTrace("Begin exceptionTest5");
    boolean pass = false;
    try {
      getEntityTransaction().begin();
      getEntityManager().close();
      getEntityTransaction().commit();

    } catch (IllegalStateException ise) {
      pass = true;
      TestUtil.logTrace("Exception Caught as Expected: " + ise);
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }

      } catch (Exception fe) {
        TestUtil.logErr("Unexpected Exception Caught rolling back TX:", fe);
      }

    }

    if ((!pass) && (isStandAloneMode())) {
      TestUtil.logTrace("In JavaSE, Exception Not Thrown as Expected");
      pass = true;
    }

    if (!pass)
      throw new Fault("exceptionTest5 failed");
  }

  /*
   * @testName: exceptionTest6
   * 
   * @assertion_ids: PERSISTENCE:SPEC:606; JavaEE:SPEC:10006
   * 
   * @test_Strategy: refresh throws an IllegalArgumentException if the entity is
   * not managed
   */

  public void exceptionTest6() throws Fault {

    TestUtil.logTrace("Begin exceptionTest6");
    boolean pass = false;
    final Coffee newCoffee = new Coffee(7, "cinnamon", 7.0F);

    try {
      getEntityTransaction().begin();
      getEntityManager().persist(newCoffee);
      clearCache();

      if (!getEntityManager().contains(newCoffee)) {
        getEntityManager().refresh(newCoffee);
      } else {
        TestUtil.logTrace("Entity is managed, cannot proceed with test");
      }
      getEntityTransaction().commit();

    } catch (IllegalArgumentException iae) {
      pass = true;
      TestUtil.logTrace("Exception Caught as Expected: " + iae);
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception re) {
        TestUtil.logErr("Unexpected Exception in rollback:", re);
      }

    }

    if (!pass)
      throw new Fault("exceptionTest6 failed");
  }

  /*
   * @testName: RollbackExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:184; PERSISTENCE:JAVADOC:185;
   * PERSISTENCE:JAVADOC:186; PERSISTENCE:JAVADOC:187
   * 
   * @test_Strategy: create and throw RollbackException() and validate contents
   * of Exception
   */

  public void RollbackExceptionTest() throws Fault {

    TestUtil.logMsg("Test RollbackExceptionNullMsg");
    boolean pass = true;

    try {
      throw new RollbackException();
    } catch (RollbackException re) {
      TestUtil.logTrace("RollbackException Caught as Expected");
      if (re.getMessage() != null) {
        pass = false;
        TestUtil.logErr(
            "RollbackException should have had null message, actual message="
                + re.getMessage());
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected Exception Caught", e);
      pass = false;
    }

    TestUtil.logMsg("Test RollbackExceptionStringMsg");

    String expected = "This is the String message";
    try {
      throw new RollbackException(expected);
    } catch (RollbackException re) {
      TestUtil.logTrace("RollbackException Caught as Expected");
      String msg = re.getMessage();
      if (msg != null) {
        if (!msg.equals(expected)) {
          pass = false;
          TestUtil.logErr("Expected=" + msg + ", Actual=" + msg);
        }
      } else {
        pass = false;
        TestUtil.logErr("RollbackException returned null message");
      }
    } catch (Exception e) {
      pass = false;
      TestUtil.logErr("Unexpected Exception Caught", e);
    }

    TestUtil.logMsg("Test RollbackExceptionStringThrowable");

    expected = "This is the String message";
    String expected2 = "This is the Throwable message";

    try {
      throw new RollbackException(expected,
          new IllegalAccessException(expected2));

    } catch (RollbackException re) {
      TestUtil.logTrace("RollbackException Caught as Expected");
      String msg = re.getMessage();
      if (msg != null) {
        if (msg.equals(expected)) {
          Throwable t = re.getCause();
          if (t instanceof IllegalAccessException) {
            msg = t.getMessage();
            if (!msg.equals(expected2)) {
              pass = false;
              TestUtil
                  .logErr("Expected Throwable msg=" + msg + ", Actual=" + msg);
            }
          } else {
            pass = false;
            TestUtil.logErr(
                "getCause did not return an instance of IllegalAccessException, instead got "
                    + t);
          }

        } else {
          pass = false;
          TestUtil.logErr("Expected=" + msg + ", Actual=" + msg);
        }
      } else {
        pass = false;
        TestUtil.logErr("RollbackException returned null message");
      }
    } catch (Exception e) {
      pass = false;
      TestUtil.logErr("Unexpected Exception Caught", e);
    }

    TestUtil.logMsg("Test RollbackExceptionThrowable");
    expected = "This is the Throwable message";

    try {
      throw new RollbackException(new IllegalAccessException(expected));

    } catch (RollbackException re) {
      TestUtil.logTrace("RollbackException Caught as Expected");
      Throwable t = re.getCause();
      if (t instanceof IllegalAccessException) {
        String msg = t.getMessage();
        if (!msg.equals(expected)) {
          pass = false;
          TestUtil.logErr("Expected Throwable msg=" + msg + ", Actual=" + msg);
        }
      } else {
        pass = false;
        TestUtil.logErr(
            "getCause did not return an instance of IllegalAccessException, instead got "
                + t);
      }
    } catch (Exception e) {
      pass = false;
      TestUtil.logErr("Unexpected Exception Caught", e);
    }

    if (!pass)
      throw new Fault("RollbackExceptionTest failed");
  }

  /*
   * @testName: EntityExistsExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:30; PERSISTENCE:JAVADOC:31;
   * PERSISTENCE:JAVADOC:32; PERSISTENCE:JAVADOC:33
   * 
   * @test_Strategy: create and throw EntityExistsException() and validate
   * contents of Exception
   */

  public void EntityExistsExceptionTest() throws Fault {

    TestUtil.logMsg("Test EntityExistsExceptionNullMsg");
    boolean pass = true;

    try {
      throw new EntityExistsException();

    } catch (EntityExistsException eee) {
      TestUtil.logTrace("EntityExistsException Caught as Expected");
      if (eee.getMessage() != null) {
        pass = false;
        TestUtil.logErr(
            "EntityExistsException should have had null message, actual message="
                + eee.getMessage());
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected Exception Caught", e);
      pass = false;
    }

    TestUtil.logMsg("Test EntityExistskExceptionStringMsg");

    String expected = "This is the String message";
    try {
      throw new EntityExistsException(expected);

    } catch (EntityExistsException eee) {
      TestUtil.logTrace("EntityExistsException Caught as Expected");
      String msg = eee.getMessage();
      if (msg != null) {
        if (!msg.equals(expected)) {
          pass = false;
          TestUtil.logErr("Expected=" + msg + ", Actual=" + msg);
        }
      } else {
        pass = false;
        TestUtil.logErr("EntityExistsException returned null message");
      }
    } catch (Exception e) {
      pass = false;
      TestUtil.logErr("Unexpected Exception Caught", e);
    }

    TestUtil.logMsg("Test EntityExistsExceptionStringThrowable");

    expected = "This is the String message";
    String expected2 = "This is the Throwable message";

    try {
      throw new EntityExistsException(expected,
          new IllegalAccessException(expected2));

    } catch (EntityExistsException eee) {
      TestUtil.logTrace("EntityExistsException Caught as Expected");
      String msg = eee.getMessage();
      if (msg != null) {
        if (msg.equals(expected)) {
          Throwable t = eee.getCause();
          if (t instanceof IllegalAccessException) {
            msg = t.getMessage();
            if (!msg.equals(expected2)) {
              pass = false;
              TestUtil
                  .logErr("Expected Throwable msg=" + msg + ", Actual=" + msg);
            }
          } else {
            pass = false;
            TestUtil.logErr(
                "getCause did not return an instance of IllegalAccessException, instead got "
                    + t);
          }

        } else {
          pass = false;
          TestUtil.logErr("Expected=" + msg + ", Actual=" + msg);
        }
      } else {
        pass = false;
        TestUtil.logErr("EntityExistsException returned null message");
      }
    } catch (Exception e) {
      pass = false;
      TestUtil.logErr("Unexpected Exception Caught", e);
    }

    TestUtil.logMsg("Test EntityExistsExceptionThrowable");

    expected = "This is the Throwable message";

    try {
      throw new EntityExistsException(new IllegalAccessException(expected));

    } catch (EntityExistsException eee) {
      TestUtil.logTrace("EntityExistsException Caught as Expected");
      Throwable t = eee.getCause();
      if (t instanceof IllegalAccessException) {
        String msg = t.getMessage();
        if (!msg.equals(expected)) {
          pass = false;
          TestUtil.logErr("Expected Throwable msg=" + msg + ", Actual=" + msg);
        }
      } else {
        pass = false;
        TestUtil.logErr(
            "getCause did not return an instance of IllegalAccessException, instead got "
                + t);
      }

    } catch (Exception e) {
      pass = false;
      TestUtil.logErr("Unexpected Exception Caught", e);
    }

    if (!pass)
      throw new Fault("EntityExistsExceptionTest failed");
  }

  /*
   * @testName: EntityNotFoundExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:61; PERSISTENCE:JAVADOC:62
   * 
   * @test_Strategy: create and throw EntityNotFoundException() and validate
   * contents of Exception
   */

  public void EntityNotFoundExceptionTest() throws Fault {

    TestUtil.logMsg("Test EntityNotFoundExceptionNullMsg");
    boolean pass = true;

    try {
      throw new EntityNotFoundException();

    } catch (EntityNotFoundException enf) {
      TestUtil.logTrace("EntityNotFoundException Caught as Expected");
      if (enf.getMessage() != null) {
        pass = false;
        TestUtil.logErr(
            "EntityNotFoundException should have had null message, actual message="
                + enf.getMessage());
      }
    } catch (Exception e) {
      pass = false;
      TestUtil.logErr("Unexpected Exception Caught", e);
    }

    TestUtil.logMsg("Test EntityNotFoundExceptionStringMsg");

    String expected = "This is the String message";
    try {
      throw new EntityNotFoundException(expected);

    } catch (EntityNotFoundException enf) {
      TestUtil.logTrace("EntityNotFoundException Caught as Expected");
      String msg = enf.getMessage();
      if (msg != null) {
        if (!msg.equals(expected)) {
          pass = false;
          TestUtil.logErr("Expected=" + msg + ", Actual=" + msg);
        }
      } else {
        pass = false;
        TestUtil.logErr("EntityNotFoundException returned null message");
      }
    } catch (Exception e) {
      pass = false;
      TestUtil.logErr("Unexpected Exception Caught", e);
    }

    if (!pass)
      throw new Fault("EntityNotFoundExceptionTest failed");
  }

  /*
   * @testName: OptimisticLockExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:139; PERSISTENCE:JAVADOC:140;
   * PERSISTENCE:JAVADOC:141; PERSISTENCE:JAVADOC:142; PERSISTENCE:JAVADOC:143;
   * PERSISTENCE:JAVADOC:138; PERSISTENCE:JAVADOC:144
   * 
   * @test_Strategy: create and throw OptimisticLockException() and validate
   * contents of Exception
   */

  public void OptimisticLockExceptionTest() throws Fault {

    TestUtil.logMsg("Test OptimisticLockExceptionNullMsg");
    boolean pass = true;

    try {
      throw new OptimisticLockException();

    } catch (OptimisticLockException ole) {
      TestUtil.logTrace("OptimisticLockException Caught as Expected");
      if (ole.getMessage() != null) {
        pass = false;
        TestUtil.logErr(
            "OptimisticLockException should have had null message, actual message="
                + ole.getMessage());
      }
    } catch (Exception e) {
      pass = false;
      TestUtil.logErr("Unexpected Exception Caught", e);
    }

    TestUtil.logMsg("Test OptimisticLockExceptionStringMsg");

    String expected = "This is the String message";
    try {
      throw new OptimisticLockException(expected);

    } catch (OptimisticLockException ole) {
      TestUtil.logTrace("OptimisticLockException Caught as Expected");
      String msg = ole.getMessage();
      if (msg != null) {
        if (!msg.equals(expected)) {
          pass = false;
          TestUtil.logErr("Expected=" + msg + ", Actual=" + msg);
        }
      } else {
        pass = false;
        TestUtil.logErr("OptimisticLockException returned null message");
      }
    } catch (Exception e) {
      pass = false;
      TestUtil.logErr("Unexpected Exception Caught", e);
    }

    TestUtil.logMsg("Test OptimisticLockExceptionStringThrowable");

    expected = "This is the String message";
    String expected2 = "This is the Throwable message";

    try {
      throw new OptimisticLockException(expected,
          new IllegalAccessException(expected2));

    } catch (OptimisticLockException ole) {
      TestUtil.logTrace("OptimisticLockException Caught as Expected");
      String msg = ole.getMessage();
      if (msg != null) {
        if (msg.equals(expected)) {
          Throwable t = ole.getCause();
          if (t instanceof IllegalAccessException) {
            msg = t.getMessage();
            if (!msg.equals(expected2)) {
              pass = false;
              TestUtil
                  .logErr("Expected Throwable msg=" + msg + ", Actual=" + msg);
            }
          } else {
            pass = false;
            TestUtil.logErr(
                "getCause did not return an instance of IllegalAccessException, instead got "
                    + t);
          }

        } else {
          pass = false;
          TestUtil.logErr("Expected=" + msg + ", Actual=" + msg);
        }
      } else {
        pass = false;
        TestUtil.logErr("OptimisticLockException returned null message");
      }
    } catch (Exception e) {
      pass = false;
      TestUtil.logErr("Unexpected Exception Caught", e);
    }

    TestUtil.logMsg("Test OptimisticLockExceptionThrowable");

    expected = "This is the Throwable message";

    try {
      throw new OptimisticLockException(new IllegalAccessException(expected));

    } catch (OptimisticLockException ole) {
      TestUtil.logTrace("OptimisticLockException Caught as Expected");
      Throwable t = ole.getCause();
      if (t instanceof IllegalAccessException) {
        String msg = t.getMessage();
        if (!msg.equals(expected)) {
          pass = false;
          TestUtil.logErr("Expected Throwable msg=" + msg + ", Actual=" + msg);
        }
      } else {
        pass = false;
        TestUtil.logErr(
            "getCause did not return an instance of IllegalAccessException, instead got "
                + t);
      }

    } catch (Exception e) {
      pass = false;
      TestUtil.logErr("Unexpected Exception Caught", e);
    }

    TestUtil.logMsg("Test OptimisticLockExceptionObjectMsg");

    Coffee cExpected = new Coffee(1, "hazelnut", 1.0F);

    try {
      throw new OptimisticLockException(cExpected);

    } catch (OptimisticLockException ol) {
      TestUtil.logTrace("OptimisticLockException Caught as Expected");
      Coffee c = (Coffee) ol.getEntity();
      if (c != null) {
        if (!c.equals(cExpected)) {
          pass = false;
          TestUtil.logErr("Expected=" + cExpected + ", Actual=" + c);
        }
      } else {
        pass = false;
        TestUtil.logErr("OptimisticLockException returned null message");
      }
    } catch (Exception e) {
      pass = false;
      TestUtil.logErr("Unexpected Exception Caught", e);
    }

    TestUtil.logMsg("Test OptimisticLockExceptionStringThrowableObject");

    expected = "This is the String message";
    expected2 = "This is the Throwable message";
    cExpected = new Coffee(1, "hazelnut", 1.0F);

    try {
      throw new OptimisticLockException(expected,
          new IllegalAccessException(expected2), cExpected);

    } catch (OptimisticLockException ole) {
      TestUtil.logTrace("OptimisticLockException Caught as Expected");
      String msg = ole.getMessage();
      if (msg != null) {
        if (msg.equals(expected)) {
          Throwable t = ole.getCause();
          if (t instanceof IllegalAccessException) {
            msg = t.getMessage();
            if (msg.equals(expected2)) {
              Coffee c = (Coffee) ole.getEntity();
              if (!c.equals(cExpected)) {
                pass = false;
                TestUtil
                    .logErr("Expected Entity=" + cExpected + ", Actual=" + c);
              }
            } else {
              pass = false;
              TestUtil
                  .logErr("Expected Throwable msg=" + msg + ", Actual=" + msg);
            }
          } else {
            pass = false;
            TestUtil.logErr(
                "getCause did not return an instance of IllegalAccessException, instead got "
                    + t);
          }

        } else {
          pass = false;
          TestUtil.logErr("Expected=" + msg + ", Actual=" + msg);
        }
      } else {
        pass = false;
        TestUtil.logErr("OptimisticLockException returned null message");
      }
    } catch (Exception e) {
      pass = false;
      TestUtil.logErr("Unexpected Exception Caught", e);
    }

    if (!pass)
      throw new Fault("OptimisticLockExceptionTest failed");
  }

  /*
   * @testName: PersistenceExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:156; PERSISTENCE:JAVADOC:157;
   * PERSISTENCE:JAVADOC:158; PERSISTENCE:JAVADOC:159
   * 
   * @test_Strategy: create and throw PersistenceException() and validate
   * contents of Exception
   */

  public void PersistenceExceptionTest() throws Fault {

    TestUtil.logMsg("Test PersistenceExceptionNullMsg");
    boolean pass = true;

    try {
      throw new PersistenceException();

    } catch (PersistenceException eee) {
      TestUtil.logTrace("PersistenceException Caught as Expected");
      if (eee.getMessage() != null) {
        pass = false;
        TestUtil.logErr(
            "PersistenceException should have had null message, actual message="
                + eee.getMessage());
      }
    } catch (Exception e) {
      pass = false;
      TestUtil.logErr("Unexpected Exception Caught", e);
    }

    TestUtil.logMsg("Test EntityExistskExceptionStringMsg");

    String expected = "This is the String message";
    try {
      throw new PersistenceException(expected);

    } catch (PersistenceException eee) {
      TestUtil.logTrace("PersistenceException Caught as Expected");
      String msg = eee.getMessage();
      if (msg != null) {
        if (!msg.equals(expected)) {
          pass = false;
          TestUtil.logErr("Expected=" + msg + ", Actual=" + msg);
        }
      } else {
        pass = false;
        TestUtil.logErr("PersistenceException returned null message");
      }
    } catch (Exception e) {
      pass = false;
      TestUtil.logErr("Unexpected Exception Caught", e);
    }

    TestUtil.logMsg("Test PersistenceExceptionStringThrowable");

    expected = "This is the String message";
    String expected2 = "This is the Throwable message";

    try {
      throw new PersistenceException(expected,
          new IllegalAccessException(expected2));

    } catch (PersistenceException eee) {
      TestUtil.logTrace("PersistenceException Caught as Expected");
      String msg = eee.getMessage();
      if (msg != null) {
        if (msg.equals(expected)) {
          Throwable t = eee.getCause();
          if (t instanceof IllegalAccessException) {
            msg = t.getMessage();
            if (!msg.equals(expected2)) {
              pass = false;
              TestUtil
                  .logErr("Expected Throwable msg=" + msg + ", Actual=" + msg);
            }
          } else {
            pass = false;
            TestUtil.logErr(
                "getCause did not return an instance of IllegalAccessException, instead got "
                    + t);
          }

        } else {
          pass = false;
          TestUtil.logErr("Expected=" + msg + ", Actual=" + msg);
        }
      } else {
        pass = false;
        TestUtil.logErr("PersistenceException returned null message");
      }
    } catch (Exception e) {
      pass = false;
      TestUtil.logErr("Unexpected Exception Caught", e);
    }

    TestUtil.logMsg("Test PersistenceExceptionThrowable");

    expected = "This is the Throwable message";

    try {
      throw new PersistenceException(new IllegalAccessException(expected));

    } catch (PersistenceException eee) {
      TestUtil.logTrace("PersistenceException Caught as Expected");
      Throwable t = eee.getCause();
      if (t instanceof IllegalAccessException) {
        String msg = t.getMessage();
        if (!msg.equals(expected)) {
          pass = false;
          TestUtil.logErr("Expected Throwable msg=" + msg + ", Actual=" + msg);
        }
      } else {
        pass = false;
        TestUtil.logErr(
            "getCause did not return an instance of IllegalAccessException, instead got "
                + t);
      }

    } catch (Exception e) {
      pass = false;
      TestUtil.logErr("Unexpected Exception Caught", e);
    }

    if (!pass)
      throw new Fault("PersistenceExceptionTest failed");
  }

  /*
   * @testName: LockTimeoutExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:344; PERSISTENCE:JAVADOC:345;
   * PERSISTENCE:JAVADOC:346; PERSISTENCE:JAVADOC:347; PERSISTENCE:JAVADOC:348;
   * PERSISTENCE:JAVADOC:343; PERSISTENCE:JAVADOC:349
   * 
   * @test_Strategy: create and throw LockTimeoutException() and validate
   * contents of Exception
   */

  public void LockTimeoutExceptionTest() throws Fault {

    TestUtil.logMsg("Test LockTimeoutExceptionNullMsg");
    boolean pass = true;

    try {
      throw new LockTimeoutException();

    } catch (LockTimeoutException ole) {
      TestUtil.logTrace("LockTimeoutException Caught as Expected");
      if (ole.getMessage() != null) {
        pass = false;
        TestUtil.logErr(
            "LockTimeoutException should have had null message, actual message="
                + ole.getMessage());
      }
    } catch (Exception e) {
      pass = false;
      TestUtil.logErr("Unexpected Exception Caught", e);
    }

    TestUtil.logMsg("Test LockTimeoutExceptionStringMsg");

    String expected = "This is the String message";
    try {
      throw new LockTimeoutException(expected);

    } catch (LockTimeoutException ole) {
      TestUtil.logTrace("LockTimeoutException Caught as Expected");
      String msg = ole.getMessage();
      if (msg != null) {
        if (!msg.equals(expected)) {
          pass = false;
          TestUtil.logErr("Expected=" + msg + ", Actual=" + msg);
        }
      } else {
        pass = false;
        TestUtil.logErr("LockTimeoutException returned null message");
      }
    } catch (Exception e) {
      pass = false;
      TestUtil.logErr("Unexpected Exception Caught", e);
    }

    TestUtil.logMsg("Test LockTimeoutExceptionStringThrowable");

    expected = "This is the String message";
    String expected2 = "This is the Throwable message";

    try {
      throw new LockTimeoutException(expected,
          new IllegalAccessException(expected2));

    } catch (LockTimeoutException ole) {
      TestUtil.logTrace("LockTimeoutException Caught as Expected");
      String msg = ole.getMessage();
      if (msg != null) {
        if (msg.equals(expected)) {
          Throwable t = ole.getCause();
          if (t instanceof IllegalAccessException) {
            msg = t.getMessage();
            if (!msg.equals(expected2)) {
              pass = false;
              TestUtil
                  .logErr("Expected Throwable msg=" + msg + ", Actual=" + msg);
            }
          } else {
            pass = false;
            TestUtil.logErr(
                "getCause did not return an instance of IllegalAccessException, instead got "
                    + t);
          }

        } else {
          pass = false;
          TestUtil.logErr("Expected=" + msg + ", Actual=" + msg);
        }
      } else {
        pass = false;
        TestUtil.logErr("LockTimeoutException returned null message");
      }
    } catch (Exception e) {
      pass = false;
      TestUtil.logErr("Unexpected Exception Caught", e);
    }

    TestUtil.logMsg("Test LockTimeoutExceptionThrowable");

    expected = "This is the Throwable message";

    try {
      throw new LockTimeoutException(new IllegalAccessException(expected));

    } catch (LockTimeoutException ole) {
      TestUtil.logTrace("LockTimeoutException Caught as Expected");
      Throwable t = ole.getCause();
      if (t instanceof IllegalAccessException) {
        String msg = t.getMessage();
        if (!msg.equals(expected)) {
          pass = false;
          TestUtil.logErr("Expected Throwable msg=" + msg + ", Actual=" + msg);
        }
      } else {
        pass = false;
        TestUtil.logErr(
            "getCause did not return an instance of IllegalAccessException, instead got "
                + t);
      }

    } catch (Exception e) {
      pass = false;
      TestUtil.logErr("Unexpected Exception Caught", e);
    }

    TestUtil.logMsg("Test LockTimeoutExceptionObjectMsg");

    Coffee cExpected = new Coffee(1, "hazelnut", 1.0F);

    try {
      throw new LockTimeoutException(cExpected);

    } catch (LockTimeoutException ol) {
      TestUtil.logTrace("LockTimeoutException Caught as Expected");
      Coffee c = (Coffee) ol.getObject();
      if (c != null) {
        if (!c.equals(cExpected)) {
          pass = false;
          TestUtil.logErr("Expected=" + cExpected + ", Actual=" + c);
        }
      } else {
        pass = false;
        TestUtil.logErr("LockTimeoutException returned null message");
      }
    } catch (Exception e) {
      pass = false;
      TestUtil.logErr("Unexpected Exception Caught", e);
    }

    TestUtil.logMsg("Test LockTimeoutExceptionStringThrowableObject");

    expected = "This is the String message";
    expected2 = "This is the Throwable message";
    cExpected = new Coffee(1, "hazelnut", 1.0F);

    try {
      throw new LockTimeoutException(expected,
          new IllegalAccessException(expected2), cExpected);

    } catch (LockTimeoutException ole) {
      TestUtil.logTrace("LockTimeoutException Caught as Expected");
      String msg = ole.getMessage();
      if (msg != null) {
        if (msg.equals(expected)) {
          Throwable t = ole.getCause();
          if (t instanceof IllegalAccessException) {
            msg = t.getMessage();
            if (msg.equals(expected2)) {
              Coffee c = (Coffee) ole.getObject();
              if (!c.equals(cExpected)) {
                pass = false;
                TestUtil
                    .logErr("Expected Entity=" + cExpected + ", Actual=" + c);
              }
            } else {
              pass = false;
              TestUtil
                  .logErr("Expected Throwable msg=" + msg + ", Actual=" + msg);
            }
          } else {
            pass = false;
            TestUtil.logErr(
                "getCause did not return an instance of IllegalAccessException, instead got "
                    + t);
          }

        } else {
          pass = false;
          TestUtil.logErr("Expected=" + msg + ", Actual=" + msg);
        }
      } else {
        pass = false;
        TestUtil.logErr("LockTimeoutException returned null message");
      }
    } catch (Exception e) {
      pass = false;
      TestUtil.logErr("Unexpected Exception Caught", e);
    }

    if (!pass)
      throw new Fault("LockTimeoutExceptionTest failed");
  }

  /*
   * @testName: PessimisticLockExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:391; PERSISTENCE:JAVADOC:392;
   * PERSISTENCE:JAVADOC:393; PERSISTENCE:JAVADOC:394; PERSISTENCE:JAVADOC:395;
   * PERSISTENCE:JAVADOC:390; PERSISTENCE:JAVADOC:396
   * 
   * @test_Strategy: create and throw PessimisticLockException() and validate
   * contents of Exception
   */

  public void PessimisticLockExceptionTest() throws Fault {

    TestUtil.logMsg("Test PessimisticLockExceptionNullMsg");
    boolean pass = true;

    try {
      throw new PessimisticLockException();

    } catch (PessimisticLockException ole) {
      TestUtil.logTrace("PessimisticLockException Caught as Expected");
      if (ole.getMessage() != null) {
        pass = false;
        TestUtil.logErr(
            "PessimisticLockException should have had null message, actual message="
                + ole.getMessage());
      }
    } catch (Exception e) {
      pass = false;
      TestUtil.logErr("Unexpected Exception Caught", e);
    }

    TestUtil.logMsg("Test PessimisticLockExceptionStringMsg");

    String expected = "This is the String message";
    try {
      throw new PessimisticLockException(expected);

    } catch (PessimisticLockException ole) {
      TestUtil.logTrace("PessimisticLockException Caught as Expected");
      String msg = ole.getMessage();
      if (msg != null) {
        if (!msg.equals(expected)) {
          pass = false;
          TestUtil.logErr("Expected=" + msg + ", Actual=" + msg);
        }
      } else {
        pass = false;
        TestUtil.logErr("PessimisticLockException returned null message");
      }
    } catch (Exception e) {
      pass = false;
      TestUtil.logErr("Unexpected Exception Caught", e);
    }

    TestUtil.logMsg("Test PessimisticLockExceptionStringThrowable");

    expected = "This is the String message";
    String expected2 = "This is the Throwable message";

    try {
      throw new PessimisticLockException(expected,
          new IllegalAccessException(expected2));

    } catch (PessimisticLockException ole) {
      TestUtil.logTrace("PessimisticLockException Caught as Expected");
      String msg = ole.getMessage();
      if (msg != null) {
        if (msg.equals(expected)) {
          Throwable t = ole.getCause();
          if (t instanceof IllegalAccessException) {
            msg = t.getMessage();
            if (!msg.equals(expected2)) {
              pass = false;
              TestUtil
                  .logErr("Expected Throwable msg=" + msg + ", Actual=" + msg);
            }
          } else {
            pass = false;
            TestUtil.logErr(
                "getCause did not return an instance of IllegalAccessException, instead got "
                    + t);
          }

        } else {
          pass = false;
          TestUtil.logErr("Expected=" + msg + ", Actual=" + msg);
        }
      } else {
        pass = false;
        TestUtil.logErr("PessimisticLockException returned null message");
      }
    } catch (Exception e) {
      pass = false;
      TestUtil.logErr("Unexpected Exception Caught", e);
    }

    TestUtil.logMsg("Test PessimisticLockExceptionThrowable");

    expected = "This is the Throwable message";

    try {
      throw new PessimisticLockException(new IllegalAccessException(expected));

    } catch (PessimisticLockException ole) {
      TestUtil.logTrace("PessimisticLockException Caught as Expected");
      Throwable t = ole.getCause();
      if (t instanceof IllegalAccessException) {
        String msg = t.getMessage();
        if (!msg.equals(expected)) {
          pass = false;
          TestUtil.logErr("Expected Throwable msg=" + msg + ", Actual=" + msg);
        }
      } else {
        pass = false;
        TestUtil.logErr(
            "getCause did not return an instance of IllegalAccessException, instead got "
                + t);
      }
    } catch (Exception e) {
      pass = false;
      TestUtil.logErr("Unexpected Exception Caught", e);
    }

    TestUtil.logMsg("Test PessimisticLockExceptionObjectMsg");

    Coffee cExpected = new Coffee(1, "hazelnut", 1.0F);

    try {
      throw new PessimisticLockException(cExpected);

    } catch (PessimisticLockException ol) {
      TestUtil.logTrace("PessimisticLockException Caught as Expected");
      Coffee c = (Coffee) ol.getEntity();
      if (c != null) {
        if (!c.equals(cExpected)) {
          pass = false;
          TestUtil.logErr("Expected=" + cExpected + ", Actual=" + c);
        }
      } else {
        pass = false;
        TestUtil.logErr("PessimisticLockException returned null message");
      }
    } catch (Exception e) {
      pass = false;
      TestUtil.logErr("Unexpected Exception Caught", e);
    }

    TestUtil.logMsg("Test PessimisticLockExceptionStringThrowableObject");

    expected = "This is the String message";
    expected2 = "This is the Throwable message";
    cExpected = new Coffee(1, "hazelnut", 1.0F);

    try {
      throw new PessimisticLockException(expected,
          new IllegalAccessException(expected2), cExpected);

    } catch (PessimisticLockException ole) {
      TestUtil.logTrace("PessimisticLockException Caught as Expected");
      String msg = ole.getMessage();
      if (msg != null) {
        if (msg.equals(expected)) {
          Throwable t = ole.getCause();
          if (t instanceof IllegalAccessException) {
            msg = t.getMessage();
            if (msg.equals(expected2)) {
              Coffee c = (Coffee) ole.getEntity();
              if (!c.equals(cExpected)) {
                pass = false;
                TestUtil
                    .logErr("Expected Entity=" + cExpected + ", Actual=" + c);
              }
            } else {
              pass = false;
              TestUtil
                  .logErr("Expected Throwable msg=" + msg + ", Actual=" + msg);
            }
          } else {
            pass = false;
            TestUtil.logErr(
                "getCause did not return an instance of IllegalAccessException, instead got "
                    + t);
          }

        } else {
          pass = false;
          TestUtil.logErr("Expected=" + msg + ", Actual=" + msg);
        }
      } else {
        pass = false;
        TestUtil.logErr("PessimisticLockException returned null message");
      }
    } catch (Exception e) {
      pass = false;
      TestUtil.logErr("Unexpected Exception Caught", e);
    }

    if (!pass)
      throw new Fault("PessimisticLockExceptionTest failed");
  }

  /*
   * @testName: QueryTimeoutExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:419; PERSISTENCE:JAVADOC:420;
   * PERSISTENCE:JAVADOC:421; PERSISTENCE:JAVADOC:422; PERSISTENCE:JAVADOC:423;
   * PERSISTENCE:JAVADOC:418; PERSISTENCE:JAVADOC:424
   * 
   * @test_Strategy: create and throw QueryTimeoutException() and validate
   * contents of Exception
   */

  public void QueryTimeoutExceptionTest() throws Fault {

    TestUtil.logMsg("Begin QueryTimeoutExceptionNullMsgTest");
    boolean pass = true;

    try {
      throw new QueryTimeoutException();

    } catch (QueryTimeoutException ole) {
      TestUtil.logTrace("QueryTimeoutException Caught as Expected");
      if (ole.getMessage() != null) {
        TestUtil.logErr(
            "QueryTimeoutException should have had null message, actual message="
                + ole.getMessage());
        pass = false;
      }
    }

    TestUtil.logMsg("Test QueryTimeoutExceptionStringMsg");

    String expected = "This is the String message";
    try {
      throw new QueryTimeoutException(expected);

    } catch (QueryTimeoutException ole) {
      TestUtil.logTrace("QueryTimeoutException Caught as Expected");
      String msg = ole.getMessage();
      if (msg != null) {
        if (!msg.equals(expected)) {
          TestUtil.logErr("Expected=" + msg + ", Actual=" + msg);
          pass = false;
        }
      } else {
        TestUtil.logErr("QueryTimeoutException returned null message");
        pass = false;
      }
    }

    TestUtil.logMsg("Test QueryTimeoutExceptionStringThrowable");

    expected = "This is the String message";
    String expected2 = "This is the Throwable message";

    try {
      throw new QueryTimeoutException(expected,
          new IllegalAccessException(expected2));

    } catch (QueryTimeoutException ole) {
      TestUtil.logTrace("QueryTimeoutException Caught as Expected");
      String msg = ole.getMessage();
      if (msg != null) {
        if (msg.equals(expected)) {
          Throwable t = ole.getCause();
          if (t instanceof IllegalAccessException) {
            msg = t.getMessage();
            if (!msg.equals(expected2)) {
              TestUtil
                  .logErr("Expected Throwable msg=" + msg + ", Actual=" + msg);
              pass = false;
            }
          } else {
            TestUtil.logErr(
                "getCause did not return an instance of IllegalAccessException, instead got "
                    + t);
            pass = false;
          }

        } else {
          TestUtil.logErr("Expected=" + msg + ", Actual=" + msg);
          pass = false;
        }
      } else {
        TestUtil.logErr("QueryTimeoutException returned null message");
        pass = false;
      }
    }

    TestUtil.logMsg("Test QueryTimeoutExceptionThrowable");
    expected = "This is the Throwable message";

    try {
      throw new QueryTimeoutException(new IllegalAccessException(expected));

    } catch (QueryTimeoutException qte) {
      TestUtil.logTrace("QueryTimeoutException Caught as Expected");
      Throwable t = qte.getCause();
      if (t instanceof IllegalAccessException) {
        String msg = t.getMessage();
        if (msg != null) {
          if (!msg.equals(expected)) {
            TestUtil
                .logErr("Expected Throwable msg=" + msg + ", Actual=" + msg);
            pass = false;
          }
        } else {
          TestUtil.logErr("t.getMessage() returned null");
          pass = false;
        }
      } else {
        TestUtil.logErr(
            "getCause did not return an instance of IllegalAccessException, instead got "
                + t);
        pass = false;
      }
    }

    TestUtil.logMsg("Begin QueryTimeoutExceptionObjectMsgTest");

    Query qExpected = getEntityManager().createQuery("select c from Coffee c");
    try {
      throw new QueryTimeoutException(qExpected);

    } catch (QueryTimeoutException qte) {
      TestUtil.logTrace("QueryTimeoutException Caught as Expected");
      Query q = qte.getQuery();
      if (q != null) {
        if (q.equals(qExpected)) {
          pass = true;
        } else {
          TestUtil.logErr("Expected=" + expected + ", Actual=" + q);
          pass = false;
        }
      } else {
        TestUtil.logErr("QueryTimeoutException returned null message");
        pass = false;
      }

    }

    TestUtil.logMsg("Test QueryTimeoutExceptionStringThrowableObject");

    expected = "This is the String message";
    expected2 = "This is the Throwable message";
    qExpected = getEntityManager().createQuery("select c from Coffee c");

    try {
      throw new QueryTimeoutException(expected,
          new IllegalAccessException(expected2), qExpected);

    } catch (QueryTimeoutException qte) {
      TestUtil.logTrace("QueryTimeoutException Caught as Expected");
      String msg = qte.getMessage();
      if (msg != null) {
        if (msg.equals(expected)) {
          Throwable t = qte.getCause();
          if (t instanceof IllegalAccessException) {
            msg = t.getMessage();
            if (msg != null) {
              if (msg.equals(expected2)) {
                Query q = qte.getQuery();
                if (q != null) {
                  if (!q.equals(qExpected)) {
                    TestUtil.logErr(
                        "Expected Entity=" + qExpected + ", Actual=" + q);
                    pass = false;
                  }
                } else {
                  TestUtil.logErr("getQuery returned null");
                  pass = false;
                }
              } else {
                TestUtil.logErr(
                    "Expected Throwable msg=" + msg + ", Actual=" + msg);
                pass = false;
              }
            } else {
              TestUtil.logErr("t.getMessage returned null");
              pass = false;
            }
          } else {
            TestUtil.logErr(
                "getCause did not return an instance of IllegalAccessException, instead got "
                    + t);
            pass = false;
          }

        } else {
          TestUtil.logErr("Expected=" + msg + ", Actual=" + msg);
          pass = false;
        }
      } else {
        TestUtil.logErr("QueryTimeoutException returned null message");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected Exception Caught", e);
      pass = false;
    }

    if (!pass)
      throw new Fault("QueryTimeoutExceptionTest failed");
  }

  /*
   * @testName: NonUniqueResultExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:125; PERSISTENCE:JAVADOC:126
   * 
   * @test_Strategy: create and throw NonUniqueResultException() and validate
   * contents of Exception
   */

  public void NonUniqueResultExceptionTest() throws Fault {

    boolean pass = true;
    TestUtil.logMsg("Begin NonUniqueResultExceptionNullMsgTest");

    try {
      throw new NonUniqueResultException();
    } catch (NonUniqueResultException nure) {
      TestUtil.logTrace("NonUniqueResultException Caught as Expected");
      if (nure.getMessage() != null) {
        TestUtil.logErr(
            "NonUniqueResultException should have had null message, actual message="
                + nure.getMessage());
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected Exception Caught", e);
      pass = false;
    }

    TestUtil.logTrace("Testing NonUniqueResultExceptionStringMsg");

    String expected = "This is the String message";
    try {
      throw new NonUniqueResultException(expected);

    } catch (NonUniqueResultException ole) {
      TestUtil.logTrace("NonUniqueResultException Caught as Expected");
      String msg = ole.getMessage();
      if (msg != null) {
        if (!msg.equals(expected)) {
          TestUtil.logErr("Expected=" + msg + ", Actual=" + msg);
          pass = false;
        }
      } else {
        TestUtil.logErr("NonUniqueResultException returned null message");
        pass = false;
      }

    } catch (Exception e) {
      TestUtil.logErr("Unexpected Exception Caught", e);
      pass = false;
    }

    if (!pass)
      throw new Fault("NonUniqueResultExceptionTest failed");
  }
  /*
   * @testName: NoResultExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:127; PERSISTENCE:JAVADOC:128
   * 
   * @test_Strategy: create and throw NonUniqueResultException() and validate
   * contents of Exception
   */

  public void NoResultExceptionTest() throws Fault {

    boolean pass = true;
    TestUtil.logMsg("Testing NoResultExceptionNullMsg");
    try {
      throw new NoResultException();
    } catch (NoResultException nre) {
      TestUtil.logTrace("NoResultException Caught as Expected");
      if (nre.getMessage() != null) {
        TestUtil.logErr(
            "NoResultException should have had null message, actual message="
                + nre.getMessage());
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected Exception Caught", e);
      pass = false;
    }

    TestUtil.logMsg("Testing NoResultExceptionStringMsgTest");

    String expected = "This is the String message";
    try {
      throw new NoResultException(expected);

    } catch (NoResultException nre) {
      TestUtil.logTrace("NoResultException Caught as Expected");
      String msg = nre.getMessage();
      if (msg != null) {
        if (!msg.equals(expected)) {
          TestUtil.logErr("Expected=" + msg + ", Actual=" + msg);
          pass = false;
        }
      } else {
        TestUtil.logErr("NoResultException returned null message");
        pass = false;
      }
    }

    if (!pass)
      throw new Fault("NoResultExceptionTest failed");
  }

  public void cleanup() throws Fault {
    TestUtil.logTrace("Cleanup data");
    if (getEntityManager().isOpen()) {
      removeTestData();
    }
    TestUtil.logTrace("cleanup complete, calling super.cleanup");
    super.cleanup();
  }

  private void removeTestData() {
    TestUtil.logTrace("removeTestData");
    if (getEntityTransaction().isActive()) {
      getEntityTransaction().rollback();
    }
    try {
      getEntityTransaction().begin();
      getEntityManager().createNativeQuery("DELETE FROM COFFEE")
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
