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

package com.sun.ts.tests.jpa.core.entitytest.detach.manyXmany;

import com.sun.javatest.Status;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jpa.common.PMClientBase;

import java.util.Collection;
import java.util.Iterator;
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
      removeTestData();
    } catch (Exception e) {
      throw new Fault("Setup failed:", e);
    }
  }

  /*
   * BEGIN Test Cases
   */

  /*
   * @testName: detachMXMTest1
   * 
   * @assertion_ids: PERSISTENCE:SPEC:659; PERSISTENCE:SPEC:662;
   * 
   * @test_Strategy: The merge operation allows for the propagation of state
   * from detached entities onto persistence entities managed by the
   * EntityManager. The semantics of the merge operation applied to entity X are
   * as follows:
   *
   * If X is a detached entity, the state of X is copied onto a pre-existing
   * managed entity instance X1 of the same identity or a new managed copy of X1
   * is created.
   *
   * If X is a managed entity, it is ignored by the merge operation however, the
   * merge operation is cascaded to entities referenced by relationships from X
   * if these relationships have been annotated with the cascade element value
   *
   */

  public void detachMXMTest1() throws Fault {

    final A aRef = new A("1", "a1", 1);
    final B b1 = new B("1", "b1", 1);
    final B b2 = new B("2", "b2", 2);
    int foundB = 0;
    final String[] expectedResults = new String[] { "1", "2" };
    boolean pass1 = true;
    boolean pass2 = false;

    try {

      TestUtil.logTrace("Begin detachMXMTest1");
      createA(aRef);

      TestUtil.logTrace("Call clean to detach");
      clearCache();

      getEntityTransaction().begin();

      if (!getEntityManager().contains(aRef)) {
        TestUtil.logTrace("Status is false as expected, try merge");
        getEntityManager().merge(aRef);
        aRef.getBCol().add(b1);
        aRef.getBCol().add(b2);
        getEntityManager().merge(aRef);

        TestUtil.logTrace("findA and getBCol");
        A a1 = getEntityManager().find(A.class, "1");
        Collection newCol = a1.getBCol();

        if (newCol.size() != 2) {
          TestUtil.logErr("detachMXMTest1: Did not get expected results."
              + "Expected Collection Size of 2 B entities, got: "
              + newCol.size());
          pass1 = false;
        } else if (pass1) {

          Iterator i1 = newCol.iterator();
          while (i1.hasNext()) {
            TestUtil.logTrace("Check Collection B entities");
            B c1 = (B) i1.next();

            for (int l = 0; l < 2; l++) {
              if (expectedResults[l].equals((String) c1.getBId())) {
                TestUtil.logTrace("Found B Entity : " + (String) c1.getBName());
                foundB++;
                break;
              }
            }
          }
        }

      } else {
        TestUtil.logTrace("entity is not detached, cannot proceed with test.");
        pass1 = false;
        pass2 = false;
      }

      getEntityTransaction().commit();

    } catch (Exception e) {
      TestUtil.logErr("Unexpected Exception caught during commit:", e);
      pass1 = false;
      pass2 = false;
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }

    if (foundB != 2) {
      TestUtil.logErr("detachMXMTest1: Did not get expected results");
      pass2 = false;
    } else {
      TestUtil.logTrace("Expected results received");
      pass2 = true;
    }

    if (!pass1 || !pass2)
      throw new Fault("detachMXMTest1 failed");
  }

  /*
   *
   * Business Methods to set up data for Test Cases
   */

  private void createA(final A a) {
    TestUtil.logTrace("Entered createA method");
    getEntityTransaction().begin();
    getEntityManager().persist(a);
    getEntityTransaction().commit();
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
      getEntityManager().createNativeQuery("DELETE FROM FKEYS_MXM_BI_BTOB")
          .executeUpdate();
      getEntityManager().createNativeQuery("DELETE FROM AEJB_MXM_BI_BTOB")
          .executeUpdate();
      getEntityManager().createNativeQuery("DELETE FROM BEJB_MXM_BI_BTOB")
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
