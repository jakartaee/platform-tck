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

package com.sun.ts.tests.jpa.core.annotations.elementcollection;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.CleanupMethod;
import com.sun.ts.lib.harness.SetupMethod;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jpa.common.PMClientBase;

import java.util.*;

public class Client extends PMClientBase {

  public Client() {
  }

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  public void setupA(String[] args, Properties p) throws Fault {
    TestUtil.logTrace("setup");
    try {
      super.setup(args, p);
      removeATestData();
    } catch (Exception e) {
      TestUtil.logErr("Exception: ", e);
      throw new Fault("Setup failed:", e);

    }
  }

  public void setupCust(String[] args, Properties p) throws Fault {
    TestUtil.logTrace("setup");
    try {
      super.setup(args, p);
      removeCustTestData();
    } catch (Exception e) {
      TestUtil.logErr("Exception: ", e);
      throw new Fault("Setup failed:", e);

    }
  }

  /*
   * @testName: elementCollectionEmbeddableType
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:318; PERSISTENCE:JAVADOC:319;
   * PERSISTENCE:SPEC:2007;
   * 
   * @test_Strategy: ElementCollection of an embeddable class
   *
   */
  @SetupMethod(name = "setupA")
  @CleanupMethod(name = "cleanupA")
  public void elementCollectionEmbeddableType() throws Fault {
    boolean pass = false;
    A aRef = null;
    try {

      final Address addr1 = new Address("1 Network Drive", "Burlington", "MA",
          "01801");
      final Address addr2 = new Address("Some Address", "Boston", "MA",
          "01803");

      Set<Address> s1 = new HashSet<Address>();
      s1.add(addr1);
      s1.add(addr2);

      aRef = new A("1", "bean1", 1);
      aRef.setAddress(s1);
      TestUtil.logTrace("Persisting A");
      getEntityTransaction().begin();
      getEntityManager().persist(aRef);
      getEntityTransaction().commit();

      getEntityTransaction().begin();
      A newA = findA("1");
      final Set<Address> newAddressSet = newA.getAddress();

      dumpAddresses(newAddressSet);

      boolean pass1 = false;
      boolean pass2 = false;
      boolean pass3 = true;
      for (Address addr : newAddressSet) {
        if (addr != null) {
          if (addr.getStreet().equals("1 Network Drive")
              && addr.getCity().equals("Burlington")
              && addr.getState().equals("MA")
              && addr.getZip().equals("01801")) {
            pass1 = true;
            TestUtil.logTrace("pass1 = " + pass1);
          }
          if (addr.getStreet().equals("Some Address")
              && addr.getCity().equals("Boston") && addr.getState().equals("MA")
              && addr.getZip().equals("01803")) {
            pass2 = true;
            TestUtil.logTrace("pass2 = " + pass2);
          }
        } else {
          TestUtil.logTrace("address=null");
          pass3 = false;
        }
      }

      if (pass1 && pass2 && pass3) {
        pass = true;
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

    if (!pass) {
      throw new Fault("elementCollectionEmbeddableType failed");
    }
  }

  /*
   * @testName: elementCollectionBasicType
   * 
   * @assertion_ids: PERSISTENCE:SPEC:2007;
   * 
   * @test_Strategy: ElementCollection of a basic type
   */
  @SetupMethod(name = "setupCust")
  @CleanupMethod(name = "cleanupCust")
  public void elementCollectionBasicType() throws Fault {
    boolean pass = false;
    try {
      getEntityTransaction().begin();
      Customer expected = new Customer("1");
      List<String> expectedphones = new ArrayList<String>();
      expectedphones.add("781-442-2010");
      expectedphones.add("781-442-2011");
      expectedphones.add("781-442-2012");

      expected.setPhones(expectedphones);
      TestUtil.logTrace("Persisting Customer:" + expected.toString());
      getEntityManager().persist(expected);
      getEntityManager().flush();
      getEntityTransaction().commit();
      clearCache();
      getEntityTransaction().begin();
      TestUtil.logTrace(
          "find the previously persisted Customer and Country and verify them");
      Customer cust = getEntityManager().find(Customer.class, expected.getId());
      if (cust != null) {
        TestUtil.logTrace("Found Customer: " + cust.toString());
        if (cust.getPhones().containsAll(expectedphones)
            && expectedphones.containsAll(cust.getPhones())
            && cust.getPhones().size() == expectedphones.size()) {
          TestUtil.logTrace("Received expected Phones:");
          for (String s : cust.getPhones()) {
            TestUtil.logTrace("phone:" + s);
          }
          pass = true;
        } else {
          TestUtil.logErr("Did not get expected results.");
          for (String s : expectedphones) {
            TestUtil.logErr("expected:" + s);
          }
          TestUtil.logErr("actual:");
          for (String s : cust.getPhones()) {
            TestUtil.logErr("actual:" + s);
          }
        }
      } else {
        TestUtil.logErr("Find returned null Customer");
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred: ", e);
      pass = false;
    }
    if (!pass) {
      throw new Fault("elementCollectionBasicType failed");
    }
  }

  /*
   * @testName: elementCollectionBasicTypeXMLTest
   * 
   * @assertion_ids: PERSISTENCE:SPEC:2008;
   * 
   * @test_Strategy: ElementCollection of a basic type using mapping file to
   * define annotation
   */
  @SetupMethod(name = "setupCust")
  @CleanupMethod(name = "cleanupCust")
  public void elementCollectionBasicTypeXMLTest() throws Fault {
    boolean pass = false;
    try {
      getEntityTransaction().begin();
      CustomerXML expected = new CustomerXML("1");
      List<String> expectedphones = new ArrayList<String>();
      expectedphones.add("781-442-2010");
      expectedphones.add("781-442-2011");
      expectedphones.add("781-442-2012");

      expected.setPhones(expectedphones);
      TestUtil.logTrace("Persisting Customer:" + expected.toString());
      getEntityManager().persist(expected);
      getEntityManager().flush();
      getEntityTransaction().commit();
      clearCache();
      getEntityTransaction().begin();
      TestUtil.logTrace(
          "find the previously persisted Customer and Country and verify them");
      CustomerXML cust = getEntityManager().find(CustomerXML.class,
          expected.getId());
      if (cust != null) {
        TestUtil.logTrace("Found CustomerXML: " + cust.toString());
        if (cust.getPhones().containsAll(expectedphones)
            && expectedphones.containsAll(cust.getPhones())
            && cust.getPhones().size() == expectedphones.size()) {
          TestUtil.logTrace("Received expected Phones:");
          for (String s : cust.getPhones()) {
            TestUtil.logTrace("phone:" + s);
          }
          pass = true;
        } else {
          TestUtil.logErr("Did not get expected results.");
          for (String s : expectedphones) {
            TestUtil.logErr("expected:" + s);
          }
          TestUtil.logErr("actual:");
          for (String s : cust.getPhones()) {
            TestUtil.logErr("actual:" + s);
          }
        }
      } else {
        TestUtil.logErr("Find returned null Customer");
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred: ", e);
      pass = false;
    }
    if (!pass) {
      throw new Fault("elementCollectionBasicTypeXMLTest failed");
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
    if (addr != null) {
      TestUtil.logTrace("size=" + addr.size());
      int elem = 1;
      for (Address v : addr) {
        TestUtil.logTrace("- Element #" + elem++);
        TestUtil.logTrace("  street=" + v.getStreet() + ", city=" + v.getCity()
            + ", state=" + v.getState() + ", zip=" + v.getZip());
      }
    } else {
      TestUtil.logTrace("  address=NULL");
    }
  }

  public void cleanupA() throws Fault {
    TestUtil.logTrace("cleanup");
    removeATestData();
    TestUtil.logTrace("cleanup complete, calling super.cleanup");
    super.cleanup();
  }

  public void cleanupCust() throws Fault {
    TestUtil.logTrace("cleanup");
    removeCustTestData();
    TestUtil.logTrace("cleanup complete, calling super.cleanup");
    super.cleanup();
  }

  private void removeCustTestData() {
    TestUtil.logTrace("removeCustTestData");
    if (getEntityTransaction().isActive()) {
      getEntityTransaction().rollback();
    }
    try {
      getEntityTransaction().begin();
      getEntityManager().createNativeQuery("DELETE FROM CUST_TABLE")
          .executeUpdate();
      getEntityManager().createNativeQuery("DELETE FROM PHONES")
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

  private void removeATestData() {
    TestUtil.logTrace("removeATestData");
    if (getEntityTransaction().isActive()) {
      getEntityTransaction().rollback();
    }
    try {
      getEntityTransaction().begin();
      getEntityManager().createNativeQuery("Delete from A_ADDRESS")
          .executeUpdate();
      getEntityManager().createNativeQuery("Delete from AEC").executeUpdate();
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
