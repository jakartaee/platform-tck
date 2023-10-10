/*
 * Copyright (c) 2007, 2023 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jpa.core.entitytest.detach.oneXone;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jpa.common.PMClientBase;

import jakarta.persistence.EntityExistsException;

@ExtendWith(ArquillianExtension.class)
@TestInstance(Lifecycle.PER_CLASS)

public class ClientIT extends PMClientBase {

  public ClientIT() {
  }

  
  @Deployment(testable = false, managed = false)
	public static JavaArchive createDeployment() throws Exception {

		String pkgNameWithoutSuffix = ClientIT.class.getPackageName();
		String pkgName = ClientIT.class.getPackageName() + ".";
		String[] classes = { pkgName + "A", pkgName + "B"};
		return createDeploymentJar("jpa_core_entitytest_detach_oneXone.jar", pkgNameWithoutSuffix, classes);

	}


  @BeforeAll
  public void setup() throws Exception {
    TestUtil.logTrace("Entering Setup");
    try {
      super.setup();
      removeTestData();
    } catch (Exception e) {
      TestUtil.logErr("Exception: ", e);
      throw new Exception("Setup failed:", e);

    }
  }

  /*
   * BEGIN Test Cases
   */

  /*
   * @testName: detach1X1Test1
   * 
   * @assertion_ids: PERSISTENCE:SPEC:625; PERSISTENCE:SPEC:742
   * 
   * @test_Strategy: The new entity bean instance becomes both managed and
   * persistent by invoking the persist method on it. The semantics of the
   * persist operation as applied to entity X is as follows:
   *
   * If X is a detached object and the persist method is invoked on it, an
   * IllegalArgumentException is thrown or an EntityExistsException, or the
   * transaction commit will fail. Invoke persist on a detached entity.
   */
@Test
  public void detach1X1Test1() throws Exception {

    TestUtil.logTrace("Begin detach1X1Test1");
    boolean pass = false;
    final A aRef = new A("1", "a1", 1);

    try {

      TestUtil.logTrace("Persist Instance");
      createA(aRef);

      clearCache();

      getEntityTransaction().begin();
      TestUtil
          .logTrace("Call contains to determine if the instance is detached");

      if (getEntityManager().contains(aRef)) {
        TestUtil.logTrace("entity is not detached, cannot proceed with test.");
        pass = false;
      } else {
        try {
          TestUtil.logTrace("Status is false as expected, try perist()");
          getEntityManager().persist(aRef);
        } catch (IllegalArgumentException iae) {
          TestUtil.logTrace("IllegalArgumentException thrown trying to persist"
              + " a detached entity", iae);
          pass = true;
        } catch (EntityExistsException eee) {
          TestUtil.logTrace("entityExistsException thrown trying to persist "
              + "an existing entity", eee);
          pass = true;
        }
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logTrace("or, Transaction commit will fail."
          + " Test the commit failed by testing"
          + " the transaction is marked for rollback");
      if ((!pass)
          && (e instanceof jakarta.transaction.TransactionRolledbackException
              || e instanceof jakarta.persistence.PersistenceException)) {
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
      throw new Exception("detach1X1Test1 failed");
  }

  /*
   * @testName: detach1X1Test2
   * 
   * @assertion_ids: PERSISTENCE:SPEC:635
   * 
   * @test_Strategy: If X is a detached entity, invoking the remove method on it
   * will cause an IllegalArgumentException to be thrown or the transaction
   * commit will fail. Invoke remove on a detached entity.
   *
   */
@Test
  public void detach1X1Test2() throws Exception {
    TestUtil.logTrace("Begin detach1X1Test2");
    boolean pass = false;
    final A a1 = new A("2", "a2", 2);
    final B bRef = new B("2", "a2", 2, a1);

    try {
      TestUtil.logTrace("Persist B");
      createB(bRef);
      clearCache();

      getEntityTransaction().begin();
      TestUtil.logTrace("get Instance Status ");

      if (getEntityManager().contains(bRef)) {
        TestUtil
            .logTrace("contains method returned true; unexpected, test fails.");
        pass = false;
      } else {

        try {
          TestUtil.logTrace("Status is false as expected, try remove");
          getEntityManager().remove(bRef);
        } catch (IllegalArgumentException iae) {
          TestUtil.logTrace("IllegalArgumentException caught as expected "
              + " trying to remove a detached entity", iae);
          pass = true;
        }
      }

      getEntityTransaction().commit();

    } catch (Exception e) {
      TestUtil.logTrace("or, Transaction commit will fail.  "
          + " Test the commit failed by testing"
          + " the transaction is marked for rollback");

      if ((!pass)
          && (e instanceof jakarta.transaction.TransactionRolledbackException
              || e instanceof jakarta.persistence.PersistenceException)) {
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
      throw new Exception("detach1X1Test2 failed");
  }

  /*
   * Business Methods to set up data for Test Cases
   */

  private void createA(final A a) {
    TestUtil.logTrace("Entered createA method");
    getEntityTransaction().begin();
    getEntityManager().persist(a);
    getEntityTransaction().commit();
  }

  private void createB(final B b) {
    TestUtil.logTrace("Entered createB method");
    getEntityTransaction().begin();
    getEntityManager().persist(b);
    getEntityTransaction().commit();
  }

  @AfterAll
  public void cleanup() throws Exception {
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
      getEntityManager().createNativeQuery("DELETE FROM BEJB_1X1_BI_BTOB")
          .executeUpdate();
      getEntityManager().createNativeQuery("DELETE FROM AEJB_1X1_BI_BTOB")
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
