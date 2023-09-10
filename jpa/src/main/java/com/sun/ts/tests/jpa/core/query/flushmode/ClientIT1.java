/*
 * Copyright (c) 2007, 2020 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jpa.core.query.flushmode;

import java.sql.Date;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jpa.common.schema30.Customer;
import com.sun.ts.tests.jpa.common.schema30.UtilCustomerData;

import jakarta.persistence.EntityManager;
import jakarta.persistence.FlushModeType;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;

public class ClientIT1 extends UtilCustomerData {

  public ClientIT1() {
  }


   /* Run test */

  /*
   * @testName: flushModeTest1
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:173; PERSISTENCE:JAVADOC:400;
   * PERSISTENCE:JAVADOC:637; PERSISTENCE:JAVADOC:684;
   * 
   * @test_Strategy: Query accessing a simple field The following updates the
   * name of a customer and then executes an EJBQL query selecting customers
   * having the updated name.
   *
   * TypedQuery accessing a simple field The following updates the name of a
   * customer and then executes an EJBQL query selecting customers having the
   * updated name.*
   *
   */
  @Test
  public void flushModeTest1() throws Exception {
    boolean pass1 = false;
    boolean pass2 = false;
    boolean pass3 = false;
    boolean pass4 = false;
    boolean pass5 = false;
    boolean pass6 = false;
    String expectedPKs[];
    TestUtil.logTrace("Testing TypedQuery version");

    try {
      getEntityTransaction().begin();
      EntityManager em = getEntityManager();
      TestUtil.logTrace("Calling find");
      Customer cust1 = em.find(Customer.class, "1");
      cust1.setName("Michael Bouschen");
      Query q = em.createQuery(
          "SELECT c FROM Customer c WHERE c.name = 'Michael Bouschen'");
      TestUtil.logTrace(
          "EntityManager.getFlushMode() returned:" + em.getFlushMode());
      TestUtil.logTrace("Calling Query.getFlushMode()");
      FlushModeType fmt = q.getFlushMode();
      if (!fmt.equals(em.getFlushMode())) {
        TestUtil.logErr("getFlushMode() called when no mode set expected:"
            + em.getFlushMode() + ", actual:" + fmt);
      } else {
        pass1 = true;

        TestUtil.logTrace("Setting mode to FlushModeType.AUTO");
        q.setFlushMode(FlushModeType.AUTO);
        fmt = q.getFlushMode();
        if (!fmt.equals(FlushModeType.AUTO)) {
          TestUtil.logErr("getFlushMode() called when no mode set expected:"
              + FlushModeType.AUTO + ", actual:" + fmt);
        } else {
          pass2 = true;

          List<Customer> c = q.getResultList();
          expectedPKs = new String[1];
          expectedPKs[0] = "1";

          if (!checkEntityPK(c, expectedPKs)) {
            TestUtil.logErr(
                "Did not get expected results.  Expected 1 references, got: "
                    + c.size());
          } else {
            Customer newCust = em.find(Customer.class, "1");
            if (newCust.getName().equals("Michael Bouschen")) {
              TestUtil.logTrace("Expected results received");

              pass3 = true;
            }
          }
        }
      }
      getEntityTransaction().rollback();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: ", e);
    }

    TestUtil.logTrace("Testing TypedQuery version");

    try {
      getEntityTransaction().begin();
      EntityManager em = getEntityManager();
      TestUtil.logTrace("Calling find");
      Customer cust1 = em.find(Customer.class, "1");
      cust1.setName("Michael Bouschen");
      TypedQuery<Customer> q = em.createQuery(
          "SELECT c FROM Customer c WHERE c.name = 'Michael Bouschen'",
          Customer.class);
      TestUtil.logTrace("Calling getFlushMode()");
      FlushModeType fmt = q.getFlushMode();
      if (!fmt.equals(em.getFlushMode())) {
        TestUtil.logErr("getFlushMode() called when no mode set expected:"
            + em.getFlushMode() + ", actual:" + fmt);
      } else {
        pass4 = true;

        TestUtil.logTrace("Setting mode to FlushModeType.AUTO");
        q.setFlushMode(FlushModeType.AUTO);
        fmt = q.getFlushMode();
        if (!fmt.equals(FlushModeType.AUTO)) {
          TestUtil.logErr("getFlushMode() called when no mode set expected:"
              + FlushModeType.AUTO + ", actual:" + fmt);
        } else {
          pass5 = true;

          List<Customer> c = q.getResultList();
          expectedPKs = new String[1];
          expectedPKs[0] = "1";

          if (!checkEntityPK(c, expectedPKs)) {
            TestUtil.logErr(
                "Did not get expected results.  Expected 1 references, got: "
                    + c.size());
          } else {
            Customer newCust = em.find(Customer.class, "1");
            if (newCust.getName().equals("Michael Bouschen")) {
              pass6 = true;

              TestUtil.logTrace("Expected results received");
            }
          }
        }
      }
      getEntityTransaction().rollback();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: ", e);
    }

    if (!pass1 || !pass2 || !pass3 || !pass4 || !pass5 || !pass6)
      throw new Exception("flushModeTest1 failed");
  }

}
