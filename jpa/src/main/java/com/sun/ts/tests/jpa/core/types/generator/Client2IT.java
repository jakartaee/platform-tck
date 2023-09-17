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

import java.util.Properties;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sun.ts.lib.util.TestUtil;

public class Client2IT extends Client {

 
  private DataTypes2 d10;

  public Client2IT() {
  }


 
  /*
   * @class.setup_props: db.supports.sequence;
   */
  @BeforeEach
  public void setupDataTypes2() throws Exception {
    TestUtil.logTrace("setupDataTypes2");
    try {

      super.setup();
      String s = System.getProperty("db.supports.sequence");
      if (s != null) {
        supports_sequence = Boolean.parseBoolean(s);
        TestUtil.logMsg("db.supports.sequence:" + supports_sequence);
        if (supports_sequence) {
          removeTestData();
          createDataTypes2Data();
        }
      } else {
        TestUtil.logErr(
            "The property db.supports.sequence is not defined in the ts.jte, this must be corrected before running tests");
        throw new Exception("setupDataTypes2 failed");

      }

    } catch (Exception e) {
      TestUtil.logErr("Exception: ", e);
      throw new Exception("setupDataTypes2 failed:", e);
    }
  }

 
  /*
   * @testName: generatorTypeSequenceTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:194; PERSISTENCE:JAVADOC:195;
   * PERSISTENCE:JAVADOC:196; PERSISTENCE:SPEC:2107.2; PERSISTENCE:SPEC:2106;
   * 
   * @test_Strategy: The GeneratedValue annotation provides for the
   * specification of generation strategies for the values of primary keys.
   * GenerationType.SEQUENCE, indicates the persistence provider must assign
   * primary keys for the entity using an underlying database sequence generator
   * to ensure uniqueness.
   *
   * Using GenerationType.SEQUENCE, access a persisted entity and modify its'
   * data.
   */
  @Test
  public void generatorTypeSequenceTest() throws Exception {

    boolean pass = true;
    if (supports_sequence) {
      final Float newFloat = 3.0F;

      try {
        getEntityTransaction().begin();
        int id = d10.getId();
        TestUtil.logTrace("Doing a find of id: " + id);
        DataTypes2 d = getEntityManager().find(DataTypes2.class, id);

        if (null != d) {
          Float f = d.getFloatData();
          if (f.equals(d10.getFloatData())) {
            TestUtil.logTrace("find returned correct float value:" + f);
            d.setFloatData(newFloat);
          } else {
            TestUtil.logErr(
                "find did not return correct float value, expected: 1.0, actual:"
                    + f);
            pass = false;
          }

          getEntityManager().merge(d);
          getEntityManager().flush();
          f = d.getFloatData();
          if (f.equals(newFloat)) {
            TestUtil.logTrace("Successfully set float value to:" + newFloat);
          } else {
            TestUtil.logErr("Could not update float value, expected: "
                + newFloat + ", actual:" + f);
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
      throw new Exception("generatorTypeSequenceTest failed");

  }


  public void createDataTypes2Data() {
    try {
      getEntityTransaction().begin();

      TestUtil.logTrace("in createDataTypes2Data");

      TestUtil.logTrace("new DataType2");
      d10 = new DataTypes2('a', (short) 100, 500, 300L, 50D, 1.0F);
      TestUtil.logTrace("Persist DataType2");
      getEntityManager().persist(d10);
      TestUtil.logTrace("DataType2 id:" + d10.getId());

      getEntityManager().flush();
      getEntityTransaction().commit();

    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }
  }


}
