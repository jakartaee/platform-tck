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

package com.sun.ts.tests.jpa.core.annotations.tableGenerator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sun.ts.lib.util.TestUtil;

public class Client1IT extends Client {

  private DataTypes d0;

  public Client1IT() {
  }


  @BeforeEach
  public void setup() throws Exception {
    TestUtil.logTrace("setup");
    try {

      super.setup();
      removeTestData();
      createTestData();
    } catch (Exception e) {
      TestUtil.logErr("Exception: ", e);
      throw new Exception("Setup failed:", e);
    }
  }

    /*
   * @testName: generatorOnEntityTest
   * 
   * @assertion_ids: PERSISTENCE:SPEC:2111; PERSISTENCE:SPEC:2111.1;
   * PERSISTENCE:SPEC:2113;
   * 
   * @test_Strategy: use a generator specified on an entity
   */
@Test
  public void generatorOnEntityTest() throws Exception {

    boolean pass = false;

    try {
      getEntityTransaction().begin();
      int id = d0.getId();
      TestUtil.logTrace("find id: " + id);
      DataTypes d = getEntityManager().find(DataTypes.class, id);
      if (d != null) {
        if (d.getStringData().equals(d0.getStringData())) {
          pass = true;
        }

        getEntityTransaction().commit();
      } else {
        TestUtil.logErr("EntityManager.find returned null result");
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }

    if (!pass)
      throw new Exception("generatorOnEntityTest failed");
  }

  // Methods used for Tests

  public void createTestData() {
    try {
      getEntityTransaction().begin();

      d0 = new DataTypes();
      d0.setStringData("testData");
      TestUtil.logTrace("DataType:" + d0.toString());
      getEntityManager().persist(d0);

      getEntityManager().flush();
      getEntityTransaction().commit();

    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }
  }

}
