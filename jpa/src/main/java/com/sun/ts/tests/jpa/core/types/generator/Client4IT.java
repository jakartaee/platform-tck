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

package com.sun.ts.tests.jpa.core.types.generator;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.sun.ts.lib.util.TestUtil;

public class Client4IT extends Client {


  private DataTypes4 d12;

  private boolean supports_sequence = false;

  public Client4IT() {
  }



  /*
   * @class.setup_props: db.supports.sequence;
   */
  @BeforeAll
  public void setupDataTypes4() throws Exception {
    TestUtil.logTrace("setupDataTypes4");
    try {

      super.setup();
      String s = System.getProperty("db.supports.sequence");
      if (s != null) {
        supports_sequence = Boolean.parseBoolean(s);
        TestUtil.logMsg("db.supports.sequence:" + supports_sequence);
        if (supports_sequence) {
          removeTestData();
          createDataTypes4Data();
        }
      } else {
        TestUtil.logErr(
            "The property db.supports.sequence is not defined in the ts.jte, this must be corrected before running tests");
        throw new Exception("setupDataTypes4 failed");

      }

    } catch (Exception e) {
      TestUtil.logErr("Exception: ", e);
      throw new Exception("setupDataTypes4 failed:", e);
    }
  }

  /*
   * @testName: sequenceGeneratorOnPropertyTest
   * 
   * @assertion_ids: PERSISTENCE:SPEC:2107; PERSISTENCE:SPEC:2107.3;
   * 
   * @test_Strategy: A sequence generator may be specified on the primary key
   * property
   */
  @Test
  public void sequenceGeneratorOnPropertyTest() throws Exception {

    boolean pass = true;
    if (supports_sequence) {
      final Integer newInt = 1000;

      try {
        getEntityTransaction().begin();
        clearCache();
        TestUtil.logMsg("Doing a find of id: " + d12.getId());
        DataTypes4 d = getEntityManager().find(DataTypes4.class, d12.getId());

        if (d != null) {
          Integer i = d.getIntegerData();
          if (i.equals(d12.getIntegerData())) {
            TestUtil.logTrace("find returned correct Integer value:" + i);
            d.setIntegerData(newInt);
          } else {
            TestUtil
                .logErr("find did not return correct Integer value, expected: "
                    + d12.getIntegerData() + ", actual:" + i);
            pass = false;
          }

          getEntityManager().merge(d);
          getEntityManager().flush();
          clearCache();
          TestUtil.logMsg("Doing a find of merged data for id: " + d.getId());
          DataTypes4 d2 = getEntityManager().find(DataTypes4.class, d.getId());
          i = d2.getIntegerData();
          if (i.equals(d2.getIntegerData())) {
            TestUtil
                .logTrace("find returned correct merged Integer value:" + i);
          } else {
            TestUtil
                .logErr("find did not return correct Integer value, expected: "
                    + d.getIntegerData() + ", actual:" + i);
            pass = false;
          }

          getEntityTransaction().commit();
        } else {
          TestUtil.logErr("find returned null result");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.logErr("Unexpected exception occurred", e);
        pass = false;
      }
    } else {
      TestUtil.logMsg(
          "WARNING: Test not run because db.supports.sequence set to false in ts.jte");
    }
    if (!pass)
      throw new Exception("sequenceGeneratorOnEntityTest failed");

  }

  // Methods used for Tests

  public void createDataTypes4Data() {
    try {
      getEntityTransaction().begin();

      TestUtil.logTrace("in createDataTypes4Data");

      TestUtil.logTrace("new DataType4");
      d12 = new DataTypes4(500);
      TestUtil.logTrace("Persist DataType4");
      getEntityManager().persist(d12);
      TestUtil.logTrace("DataType4 id:" + d12.getId());

      getEntityManager().flush();
      getEntityTransaction().commit();

    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }
  }

}
