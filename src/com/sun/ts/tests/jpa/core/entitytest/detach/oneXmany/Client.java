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

package com.sun.ts.tests.jpa.core.entitytest.detach.oneXmany;

import com.sun.javatest.Status;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jpa.common.PMClientBase;

import javax.persistence.EntityExistsException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Properties;
import java.util.Vector;

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
      removeTestData();
    } catch (Exception e) {
      TestUtil.logErr("Exception: ", e);
      throw new Fault("Setup failed:", e);

    }
  }

  /*
   * BEGIN Test Cases
   */

  /*
   * @testName: detach1XMTest1
   * 
   * @assertion_ids: PERSISTENCE:SPEC:625; PERSISTENCE:SPEC:742;
   * PERSISTENCE:JAVADOC:31
   * 
   * @test_Strategy: The new entity bean instance becomes both managed and
   * persistent by invoking the persist method on it. The semantics of the
   * persist operation as applied to entity X is as follows:
   *
   * If X is a detached object and the persist method is invoked on it, an
   * IllegalArgumentException is thrown or the commit() will fail. Check for an
   * IllegalArgumentException, or an EntityExistsException. Invoke persist on a
   * detached entity.
   *
   */

  public void detach1XMTest1() throws Fault {
    TestUtil.logTrace("Begin detach1XMTest1");
    boolean pass = false;
    final A aRef = new A("1", "a1", 1);

    try {
      createA(aRef);
      clearCache();

      getEntityTransaction().begin();
      TestUtil.logTrace("Persist Instance");

      TestUtil
          .logTrace("Call contains to determined if the instance is detached");

      if (getEntityManager().contains(aRef)) {
        TestUtil.logTrace("entity is not detached, cannot proceed with test.");
        pass = false;
      } else {
        try {
          TestUtil.logTrace("Status is false as expected, try perist()");
          getEntityManager().persist(aRef);
        } catch (IllegalArgumentException iae) {
          TestUtil.logTrace("IllegalArgumentException thrown trying to"
              + " persist a detached entity", iae);
          pass = true;
        } catch (EntityExistsException eee) {
          TestUtil.logTrace("entityExistsException thrown trying to"
              + " persist a detached entity", eee);
          pass = true;
        }
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logTrace("or, Transaction commit will fail. "
          + " Test the commit failed by testing"
          + " the transaction is marked for rollback");

      if ((!pass)
          && (e instanceof javax.transaction.TransactionRolledbackException
              || e instanceof javax.persistence.PersistenceException)) {
        pass = true;
      }

    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }

    }

    if (!pass)
      throw new Fault("detach1XMTest1 failed");
  }

  /*
   * @testName: detach1XMTest2
   * 
   * @assertion_ids: PERSISTENCE:SPEC:625; PERSISTENCE:SPEC:635
   * 
   * @test_Strategy: If X is a detached entity, invoking the remove method on it
   * will cause an IllegalArgumentException to be thrown or the transaction
   * commit will fail. Invoke remove on a detached entity.
   *
   */

  public void detach1XMTest2() throws Fault {
    TestUtil.logTrace("Begin detach1XMTest2");
    boolean pass = false;

    try {
      B b1 = new B("1", "b1", 2);
      B b2 = new B("2", "b2", 2);
      B b3 = new B("3", "b3", 2);
      B b4 = new B("4", "b4", 2);
      Vector v1 = new Vector();
      v1.add(b1);
      v1.add(b2);
      v1.add(b3);
      v1.add(b4);
      A aRef = new A("2", "bean2", 2, v1);
      createA(aRef);

      Collection newCol = aRef.getBCol();
      dumpCollectionDataB(newCol);

      clearCache();

      TestUtil.logTrace(
          "Begin Transaction and make sure instance is detached prior to remove");
      getEntityTransaction().begin();

      if ((!getEntityManager().contains(aRef)) && (newCol.contains(b1))
          && (newCol.contains(b2)) && (newCol.contains(b3))
          && (newCol.contains(b4))) {

        try {
          TestUtil.logTrace("aref is detached, Try remove");
          getEntityManager().remove(aRef);

        } catch (IllegalArgumentException iae) {
          TestUtil.logTrace(
              "IllegalArgumentException thrown trying to remove a detached entity",
              iae);
          pass = true;
        }
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logTrace(
          "or, Transaction commit will fail.  Test the commit failed by testing"
              + " the transaction is marked for rollback");

      if ((!pass)
          && (e instanceof javax.transaction.TransactionRolledbackException
              || e instanceof javax.persistence.PersistenceException)) {
        pass = true;
      }

    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception caught trying to "
            + " remove entity instance :" + fe);
      }
    }

    if (!pass)
      throw new Fault("detach1XMTest2 failed");

  }

  /*
   *
   * Business Methods to set up data for Test Cases
   *
   */

  private void createA(final A a) {
    TestUtil.logTrace("Entered createA method");
    getEntityTransaction().begin();
    getEntityManager().persist(a);
    getEntityTransaction().commit();
  }

  private void dumpCollectionDataB(Collection c) {
    TestUtil.logTrace("collection Data");
    TestUtil.logTrace("---------------");
    TestUtil.logTrace("- size=" + c.size());
    Iterator i = c.iterator();
    int elem = 1;
    while (i.hasNext()) {
      B v = (B) i.next();
      TestUtil.logTrace("- Element #" + elem++);
      TestUtil.logTrace("  id=" + v.getBId() + ", name=" + v.getBName()
          + ", value=" + v.getBValue());
    }
  }

  public void cleanup() throws Fault {
    TestUtil.logTrace("Cleanup data");
    removeTestData();
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
      getEntityManager().createNativeQuery("DELETE FROM AEJB_1XM_BI_BTOB")
          .executeUpdate();
      getEntityManager().createNativeQuery("DELETE FROM BEJB_1XM_BI_BTOB")
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
