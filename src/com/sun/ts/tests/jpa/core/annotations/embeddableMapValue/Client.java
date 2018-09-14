/*
 * Copyright (c) 2010, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jpa.core.annotations.embeddableMapValue;

import com.sun.javatest.Status;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jpa.common.PMClientBase;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.HashMap;
import java.util.Map;
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
      TestUtil.logErr("Exception: ", e);
      throw new Fault("Setup failed:", e);

    }
  }

  /*
   * @testName: embeddableMapValue
   * 
   * @assertion_ids: PERSISTENCE:SPEC:1195;
   * 
   * @test_Strategy: Use Embeddable class in MapValue
   *
   */
  public void embeddableMapValue() throws Fault {
    TestUtil.logTrace("Begin embeddableMapValue");
    boolean pass1 = false;
    boolean pass2 = false;
    boolean pass3 = false;
    boolean pass4 = false;
    boolean pass5 = false;
    EntityManager em = getEntityManager();
    EntityTransaction et = getEntityTransaction();

    try {
      TestUtil.logTrace("New instances");

      final Address addr1 = new Address("1", "1 Network Drive", "Burlington",
          "MA", "01801");
      final Address addr2 = new Address("2", "Some Address", "Boston", "MA",
          "01803");

      Employee emp1 = new Employee(1, "Barack", "Obama");

      Map<String, Address> locationAddressMap = new HashMap<String, Address>();
      locationAddressMap.put("home", addr2);
      locationAddressMap.put("office", addr1);
      emp1.setLocationAddress(locationAddressMap);

      TestUtil.logTrace("Created new Employee");

      et.begin();
      em.persist(emp1);
      TestUtil.logTrace("persisted new Employee");
      em.flush();
      clearCache();

      TestUtil.logTrace("query for Employee");
      final Employee newEmployee = em.find(Employee.class, 1);

      final int newEmployeeId = newEmployee.getId();
      final String newEmployeeFirstName = newEmployee.getFirstName();
      final String newEmployeeLastName = newEmployee.getLastName();

      TestUtil.logTrace("Employee Id = " + newEmployeeId);
      TestUtil.logTrace("Employee First Name = " + newEmployeeFirstName);
      TestUtil.logTrace("Employee Last Name = " + newEmployeeLastName);

      if (newEmployeeId == 1) {
        pass1 = true;
        TestUtil.logTrace("Employee Id match");
      }

      if (newEmployeeFirstName.equals("Barack")) {
        TestUtil.logTrace("Employee First Name match");
        pass2 = true;
      }

      if (newEmployeeLastName.equals("Obama")) {
        TestUtil.logTrace("Employee Last Name match");
        pass3 = true;
      }

      final Map<String, Address> newLocationAddressMap = newEmployee
          .getLocationAddress();
      final Address homeAddress = newLocationAddressMap.get("home");
      final Address officeAddress = newLocationAddressMap.get("office");

      if (officeAddress.getStreet().equals("1 Network Drive")
          && officeAddress.getCity().equals("Burlington")
          && officeAddress.getState().equals("MA")
          && officeAddress.getZip().equals("01801")) {

        pass4 = true;
        TestUtil.logTrace("Employee officeAddress match");
      }

      if (homeAddress.getStreet().equals("Some Address")
          && homeAddress.getCity().equals("Boston")
          && homeAddress.getState().equals("MA")
          && homeAddress.getZip().equals("01803")) {

        pass5 = true;
        TestUtil.logTrace("Employee HomeAddress match");
      }

      et.commit();

    } catch (Exception e) {
      TestUtil.logErr("Unexpected Exception :", e);
    } finally {
      try {
        if (et.isActive()) {
          et.rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }

    }

    if (!pass1 || !pass2 || !pass3 || !pass4 || !pass5) {
      TestUtil.logErr("embeddableMapValue failed");
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
      getEntityManager()
          .createNativeQuery("Delete from COLTAB_EMP_EMBEDED_ADDRESS")
          .executeUpdate();
      getEntityManager()
          .createNativeQuery("Delete from EMPLOYEE_EMBEDED_ADDRESS")
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
