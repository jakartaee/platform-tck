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
import java.util.Vector;

import org.junit.jupiter.api.Test;

import com.sun.ts.lib.harness.SetupMethod;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jpa.common.schema30.CreditCard;
import com.sun.ts.tests.jpa.common.schema30.Customer;
import com.sun.ts.tests.jpa.common.schema30.Order;
import com.sun.ts.tests.jpa.common.schema30.Product;
import com.sun.ts.tests.jpa.common.schema30.Spouse;
import com.sun.ts.tests.jpa.common.schema30.UtilProductData;

import jakarta.persistence.EntityManager;
import jakarta.persistence.FlushModeType;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;

public class ClientIT3 extends UtilProductData {


  public ClientIT3() {
  }

  /*
   * @testName: secondaryTablesValueTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:192; PERSISTENCE:JAVADOC:193;
   * 
   * @test_Strategy:
   */
  @Test
  public void secondaryTablesValueTest() throws Exception {
    boolean pass = false;

    String[] expected = new String[4];
    expected[0] = "20";
    expected[1] = "24";
    expected[2] = "31";
    expected[3] = "37";

    try {
      getEntityTransaction().begin();

      List<Product> result = getEntityManager()
          .createQuery("SELECT p FROM Product p WHERE p.wareHouse = 'Lowell' ")
          .getResultList();

      if (!checkEntityPK(result, expected)) {
        TestUtil.logErr("Did not get expected results. Expected "
            + expected.length + " references, got: " + result.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }

      getEntityTransaction().rollback();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }

    if (!pass)
      throw new Exception("secondaryTablesValueTest failed");
  }

}
