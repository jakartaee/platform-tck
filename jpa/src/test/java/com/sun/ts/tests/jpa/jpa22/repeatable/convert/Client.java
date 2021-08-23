/*
 * Copyright (c) 2017, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jpa.jpa22.repeatable.convert;

import java.util.Properties;

import com.sun.javatest.Status;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jpa.common.PMClientBase;

public class Client extends PMClientBase {

  private static final long serialVersionUID = 22L;

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
   * @testName: convertsTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:3316;
   * 
   * @test_Strategy: try @Convert works when annotated multiple times
   * without @Converts
   */
  public void convertsTest() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;
    boolean pass3 = false;
    try {
      getEntityTransaction().begin();
      String street = "500.Oracle.Parkway";
      String city = "Redwood Shores";
      // 1 - MA , 2 - CA
      B b = new B("1", "name1", 1000, new Address(street, city, 1));
      getEntityManager().persist(b);
      getEntityManager().flush();
      getEntityTransaction().commit();
      clearCache();
      getEntityTransaction().begin();
      B b1 = getEntityManager().find(B.class, b.id);
      TestUtil.logTrace("B:" + b1.toString());
      if (b1.getBValue().equals(1000)) {
        TestUtil.logTrace("Received expected value:" + b1.getBValue());
        pass1 = true;
      } else {
        TestUtil.logErr(
            "Converter was not properly applied, expected value:1000, actual"
                + b1.getBValue());
      }
      Address a = b1.getAddress();
      if (a.getStreet().equals(street.replace(".", "_"))) {
        TestUtil.logTrace("Received expected street:" + a.getStreet());
        pass2 = true;
      } else {
        TestUtil.logErr("Converter was not properly applied, expected street:"
            + street + ", actual:" + a.getStreet());
      }
      if (a.getState() == 1) {
        TestUtil.logTrace("Received expected state:" + a.getState());
        pass3 = true;
      } else {
        TestUtil.logErr(
            "Converter was not properly applied, expected state: 1, actual: "
                + a.getState());
      }
      getEntityTransaction().rollback();

    } catch (Exception ex) {
      TestUtil.logErr("Unexpected exception received:", ex);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception re) {
        TestUtil.logErr("Unexpected Exception while rolling back TX:", re);
      }
    }

    if (!pass1 || !pass2 || !pass3) {
      throw new Fault("convertsTest failed");
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
      getEntityManager().createNativeQuery("DELETE FROM EMPLOYEE")
          .executeUpdate();
      getEntityManager().createNativeQuery("DELETE FROM DEPARTMENT")
          .executeUpdate();
      getEntityManager().createNativeQuery("DELETE FROM B_EMBEDDABLE")
          .executeUpdate();
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
}
