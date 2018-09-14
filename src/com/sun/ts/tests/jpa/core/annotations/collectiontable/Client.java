/*
 * Copyright (c) 2009, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jpa.core.annotations.collectiontable;

import com.sun.javatest.Status;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jpa.common.PMClientBase;

import java.util.Collection;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

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
   * @testName: collectionTable1
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:314; PERSISTENCE:JAVADOC:315;
   * PERSISTENCE:SPEC:1246;
   * 
   * @test_Strategy:
   *
   */
  public void collectionTable1() throws Fault {
    TestUtil.logTrace("Begin collectionTable1");
    boolean pass = false;
    A aRef = null;
    Collection bCol = null;
    Collection newCol = null;

    try {
      TestUtil.logTrace("New instances");

      final Address addr1 = new Address("1 Network Drive", "Burlington", "MA",
          "01801");
      final Address addr2 = new Address("Some Address", "Boston", "MA",
          "01803");

      Set<Address> s1 = new HashSet();
      s1.add(addr1);
      s1.add(addr2);

      aRef = new A("1", "bean1", 1);
      aRef.setAddress(s1);
      getEntityTransaction().begin();
      getEntityManager().persist(aRef);
      getEntityTransaction().commit();

      getEntityTransaction().begin();
      A newA = findA("1");
      final Set<Address> newAddressSet = newA.getAddress();

      dumpAddresses(newAddressSet);

      boolean pass1 = false;
      boolean pass2 = false;

      for (Address addr : newAddressSet) {
        if (addr.getStreet().equals("1 Network Drive")
            && addr.getCity().equals("Burlington")
            && addr.getState().equals("MA") && addr.getZip().equals("01801")) {
          pass1 = true;
          TestUtil.logTrace("pass1 = " + pass1);
        }
        if (addr.getStreet().equals("Some Address")
            && addr.getCity().equals("Boston") && addr.getState().equals("MA")
            && addr.getZip().equals("01803")) {
          pass2 = true;
          TestUtil.logTrace("pass2 = " + pass2);
        }
      }

      if (pass1 && pass2)
        pass = true;

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

    if (!pass) {
      throw new Fault("collectionTable1 failed");
    }
  }

  /*
   *
   * Business Methods to set up data for Test Cases
   *
   */

  private A findA(final String id) {
    return getEntityManager().find(A.class, id);
  }

  private void dumpAddresses(final Set<Address> addr) {
    TestUtil.logTrace("address Data");
    TestUtil.logTrace("---------------");
    TestUtil.logTrace("size=" + addr.size());
    int elem = 1;
    for (Address v : addr) {
      TestUtil.logTrace("- Element #" + elem++);
      if (v != null) {
        TestUtil.logTrace("  street=" + v.getStreet() + ", city=" + v.getCity()
            + ", state=" + v.getState() + ", zip=" + v.getZip());
      } else {
        TestUtil.logTrace("  address=numm");
      }
    }
  }

  public void cleanup() throws Fault {
    TestUtil.logTrace("cleanup");
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
      getEntityManager().createNativeQuery("Delete from COLTAB_ADDRESS")
          .executeUpdate();
      getEntityManager().createNativeQuery("Delete from COLTAB")
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
