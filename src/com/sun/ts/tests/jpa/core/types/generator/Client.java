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

package com.sun.ts.tests.jpa.core.types.generator;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.SetupMethod;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jpa.common.PMClientBase;

import java.util.Properties;

public class Client extends PMClientBase {

  private DataTypes d0;

  private DataTypes d1;

  private DataTypes d2;

  private DataTypes d3;

  private DataTypes d4;

  private DataTypes d5;

  private DataTypes d6;

  private DataTypes2 d10;

  private DataTypes3 d11;

  private DataTypes4 d12;

  private boolean supports_sequence = false;

  public Client() {
  }

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /*
   * @class.setup_props: db.supports.sequence;
   */

  public void setup(String[] args, Properties p) throws Fault {
    TestUtil.logTrace("setup");
    try {

      super.setup(args, p);
      String s = p.getProperty("db.supports.sequence");
      if (s != null) {
        supports_sequence = Boolean.parseBoolean(s);
        TestUtil.logMsg("db.supports.sequence:" + supports_sequence);
        if (supports_sequence) {
          removeTestData();
          createTestData();
        }
      } else {
        TestUtil.logErr(
            "The property db.supports.sequence is not defined in the ts.jte, this must be corrected before running tests");
        throw new Fault("setupDataTypes2 failed");

      }
    } catch (Exception e) {
      TestUtil.logErr("Exception: ", e);
      throw new Fault("Setup failed:", e);
    }
  }

  /*
   * @class.setup_props: db.supports.sequence;
   */

  public void setupDataTypes2(String[] args, Properties p) throws Fault {
    TestUtil.logTrace("setupDataTypes2");
    try {

      super.setup(args, p);
      String s = p.getProperty("db.supports.sequence");
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
        throw new Fault("setupDataTypes2 failed");

      }

    } catch (Exception e) {
      TestUtil.logErr("Exception: ", e);
      throw new Fault("setupDataTypes2 failed:", e);
    }
  }

  /*
   * @class.setup_props: db.supports.sequence;
   */

  public void setupDataTypes3(String[] args, Properties p) throws Fault {
    TestUtil.logTrace("setupDataTypes3");
    try {

      super.setup(args, p);
      String s = p.getProperty("db.supports.sequence");
      if (s != null) {
        supports_sequence = Boolean.parseBoolean(s);
        TestUtil.logMsg("db.supports.sequence:" + supports_sequence);
        if (supports_sequence) {
          removeTestData();
          createDataTypes3Data();
        }
      } else {
        TestUtil.logErr(
            "The property db.supports.sequence is not defined in the ts.jte, this must be corrected before running tests");
        throw new Fault("setupDataTypes3 failed");

      }

    } catch (Exception e) {
      TestUtil.logErr("Exception: ", e);
      throw new Fault("setupDataTypes3 failed:", e);
    }
  }

  /*
   * @class.setup_props: db.supports.sequence;
   */

  public void setupDataTypes4(String[] args, Properties p) throws Fault {
    TestUtil.logTrace("setupDataTypes4");
    try {

      super.setup(args, p);
      String s = p.getProperty("db.supports.sequence");
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
        throw new Fault("setupDataTypes4 failed");

      }

    } catch (Exception e) {
      TestUtil.logErr("Exception: ", e);
      throw new Fault("setupDataTypes4 failed:", e);
    }
  }
  /*
   * @testName: generatorTypeTest1
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:82; PERSISTENCE:JAVADOC:83;
   * PERSISTENCE:SPEC:1051; PERSISTENCE:SPEC:1052; PERSISTENCE:SPEC:1057;
   * PERSISTENCE:JAVADOC:206; PERSISTENCE:JAVADOC:209; PERSISTENCE:JAVADOC:210;
   * PERSISTENCE:JAVADOC:211; PERSISTENCE:JAVADOC:213; PERSISTENCE:JAVADOC:215;
   * PERSISTENCE:JAVADOC:81; PERSISTENCE:SPEC:1136; PERSISTENCE:JAVADOC:208;
   * PERSISTENCE:SPEC:2158; PERSISTENCE:SPEC:2189; PERSISTENCE:SPEC:2023;
   * 
   * @test_Strategy: The GeneratedValue annotation provides for the
   * specification of generation strategies for the values of primary keys.
   * GenerationType.TABLE, indicates the persistence provider must assign
   * primary keys for the entity using an underlying database strategy table to
   * ensure uniqueness.
   *
   * Using GenerationType.TABLE, access a persisted entity and modify its' data.
   */

  public void generatorTypeTest1() throws Fault {

    boolean pass = false;
    if (supports_sequence) {

      final Character newChar = 'b';

      try {
        getEntityTransaction().begin();
        int id = d0.getId();
        TestUtil.logTrace("Doing a find of id: " + id);
        d1 = getEntityManager().find(DataTypes.class, id);

        if (null != d1) {
          if (d1.getCharacterData().equals('a')) {
            d1.setCharacterData(newChar);
          }

          getEntityManager().merge(d1);
          getEntityManager().flush();

          if (d1.getCharacterData().equals(newChar)) {
            pass = true;
          }

          getEntityTransaction().commit();
        } else {
          TestUtil.logErr("EntityManager.find returned null result");
        }
      } catch (Exception e) {
        TestUtil.logErr("Unexpected exception occurred", e);
      }
    } else {
      TestUtil.logMsg(
          "WARNING: Test not run because db.supports.sequence set to false in ts.jte");
      pass = true;
    }
    if (!pass)
      throw new Fault("generatorTypeTest1 failed");
  }

  /*
   * @testName: generatorTypeTest2
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:82; PERSISTENCE:JAVADOC:83;
   * PERSISTENCE:SPEC:1051; PERSISTENCE:SPEC:1052; PERSISTENCE:SPEC:1057
   * 
   * @test_Strategy: The GeneratedValue annotation provides for the
   * specification of generation strategies for the values of primary keys.
   * GenerationType.TABLE, indicates the persistence provider must assign
   * primary keys for the entity using an underlying database strategy table to
   * ensure uniqueness.
   *
   * Using GenerationType.TABLE, access a persisted entity and modify its' data.
   */

  public void generatorTypeTest2() throws Fault {

    boolean pass = false;
    if (supports_sequence) {

      final Short newShort = (short) 101;

      try {
        getEntityTransaction().begin();
        int id = d0.getId();
        TestUtil.logTrace("Doing a find of id: " + id);
        d2 = getEntityManager().find(DataTypes.class, id);

        if (null != d2) {
          if (d2.getShortData().equals((short) 100)) {
            d2.setShortData(newShort);
          }

          getEntityManager().merge(d2);
          getEntityManager().flush();

          if (d2.getShortData().equals(newShort)) {
            pass = true;
          }

          getEntityTransaction().commit();
        } else {
          TestUtil.logErr("EntityManager.find returned null result");
        }
      } catch (Exception e) {
        TestUtil.logErr("Unexpected exception occurred", e);
      }
    } else {
      TestUtil.logMsg(
          "WARNING: Test not run because db.supports.sequence set to false in ts.jte");
      pass = true;
    }
    if (!pass)
      throw new Fault("generatorTypeTest2 failed");
  }

  /*
   * @testName: generatorTypeTest3
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:82; PERSISTENCE:JAVADOC:83;
   * PERSISTENCE:SPEC:1051; PERSISTENCE:SPEC:1052; PERSISTENCE:SPEC:1057
   * 
   * @test_Strategy: The GeneratedValue annotation provides for the
   * specification of generation strategies for the values of primary keys.
   * GenerationType.TABLE, indicates the persistence provider must assign
   * primary keys for the entity using an underlying database strategy table to
   * ensure uniqueness.
   *
   * Using GenerationType.TABLE, access a persisted entity and modify its' data.
   */

  public void generatorTypeTest3() throws Fault {

    boolean pass = false;
    if (supports_sequence) {

      final Integer newInt = 500;

      try {
        getEntityTransaction().begin();
        int id = d0.getId();
        TestUtil.logTrace("Doing a find of id: " + id);
        d3 = getEntityManager().find(DataTypes.class, id);

        if (null != d3) {
          if (d3.getIntegerData().equals(500)) {
            d3.setIntegerData(newInt);
          }

          getEntityManager().merge(d3);
          getEntityManager().flush();

          if (d3.getIntegerData().equals(newInt)) {
            pass = true;
          }
          getEntityTransaction().commit();
        } else {
          TestUtil.logErr("EntityManager.find returned null result");
        }
      } catch (Exception e) {
        TestUtil.logErr("Unexpected exception occurred", e);
      }
    } else {
      TestUtil.logMsg(
          "WARNING: Test not run because db.supports.sequence set to false in ts.jte");
      pass = true;
    }
    if (!pass)
      throw new Fault("generatorTypeTest3 failed");
  }

  /*
   * @testName: generatorTypeTest4
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:82; PERSISTENCE:JAVADOC:83;
   * PERSISTENCE:SPEC:1051; PERSISTENCE:SPEC:1052; PERSISTENCE:SPEC:1057
   * 
   * @test_Strategy: The GeneratedValue annotation provides for the
   * specification of generation strategies for the values of primary keys.
   * GenerationType.TABLE, indicates the persistence provider must assign
   * primary keys for the entity using an underlying database strategy table to
   * ensure uniqueness.
   *
   * Using GenerationType.TABLE, access a persisted entity and modify its' data.
   */

  public void generatorTypeTest4() throws Fault {

    boolean pass = false;
    if (supports_sequence) {

      final Long newLong = 600L;

      try {
        getEntityTransaction().begin();
        int id = d0.getId();
        TestUtil.logTrace("Doing a find of id: " + id);
        d4 = getEntityManager().find(DataTypes.class, id);

        if (null != d4) {
          if (d4.getLongData().equals(300L)) {
            d4.setLongData(newLong);
          }

          getEntityManager().merge(d4);
          getEntityManager().flush();

          if (d4.getLongData().equals(newLong)) {
            pass = true;
          }

          getEntityTransaction().commit();
        } else {
          TestUtil.logErr("EntityManager.find returned null result");
        }
      } catch (Exception e) {
        TestUtil.logErr("Unexpected exception occurred", e);
      }

    } else {
      TestUtil.logMsg(
          "WARNING: Test not run because db.supports.sequence set to false in ts.jte");
      pass = true;
    }
    if (!pass)
      throw new Fault("generatorTypeTest4 failed");
  }

  /*
   * @testName: generatorTypeTest5
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:82; PERSISTENCE:JAVADOC:83;
   * PERSISTENCE:SPEC:1051; PERSISTENCE:SPEC:1052; PERSISTENCE:SPEC:1057
   * 
   * @test_Strategy: The GeneratedValue annotation provides for the
   * specification of generation strategies for the values of primary keys.
   * GenerationType.TABLE, indicates the persistence provider must assign
   * primary keys for the entity using an underlying database strategy table to
   * ensure uniqueness.
   *
   * Using GenerationType.TABLE, access a persisted entity and modify its' data.
   */

  public void generatorTypeTest5() throws Fault {

    boolean pass = false;
    if (supports_sequence) {

      final Double newDbl = 80D;

      try {
        getEntityTransaction().begin();
        int id = d0.getId();
        TestUtil.logTrace("Doing a find of id: " + id);
        d5 = getEntityManager().find(DataTypes.class, id);

        if (null != d5) {
          if (d5.getDoubleData().equals(50D)) {
            d5.setDoubleData(newDbl);
          }

          getEntityManager().merge(d5);
          getEntityManager().flush();

          if (d5.getDoubleData().equals(newDbl)) {
            pass = true;
          }

          getEntityTransaction().commit();
        } else {
          TestUtil.logErr("EntityManager.find returned null result");
        }
      } catch (Exception e) {
        TestUtil.logErr("Unexpected exception occurred", e);
      }
    } else {
      TestUtil.logMsg(
          "WARNING: Test not run because db.supports.sequence set to false in ts.jte");
      pass = true;
    }
    if (!pass)
      throw new Fault("generatorTypeTest5 failed");
  }

  /*
   * @testName: generatorTypeTest6
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:82; PERSISTENCE:JAVADOC:83;
   * PERSISTENCE:SPEC:1051; PERSISTENCE:SPEC:1052; PERSISTENCE:SPEC:1057
   * 
   * @test_Strategy: The GeneratedValue annotation provides for the
   * specification of generation strategies for the values of primary keys.
   * GenerationType.TABLE, indicates the persistence provider must assign
   * primary keys for the entity using an underlying database strategy table to
   * ensure uniqueness.
   *
   * Using GenerationType.TABLE, access a persisted entity and modify its' data.
   */

  public void generatorTypeTest6() throws Fault {

    boolean pass = false;
    if (supports_sequence) {

      final Float newFloat = 3.0F;

      try {
        getEntityTransaction().begin();
        int id = d0.getId();
        TestUtil.logTrace("Doing a find of id: " + id);
        d6 = getEntityManager().find(DataTypes.class, id);

        if (null != d6) {
          if (d6.getFloatData().equals(1.0F)) {
            d6.setFloatData(newFloat);
          }

          getEntityManager().merge(d6);
          getEntityManager().flush();

          if (d6.getFloatData().equals(newFloat)) {
            pass = true;
          }

          getEntityTransaction().commit();
        } else {
          TestUtil.logErr("EntityManager.find returned null result");
        }
      } catch (Exception e) {
        TestUtil.logErr("Unexpected exception occurred", e);
      }
    } else {
      TestUtil.logMsg(
          "WARNING: Test not run because db.supports.sequence set to false in ts.jte");
      pass = true;
    }
    if (!pass)
      throw new Fault("generatorTypeTest6 failed");

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
  @SetupMethod(name = "setupDataTypes2")
  public void generatorTypeSequenceTest() throws Fault {

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
      throw new Fault("generatorTypeSequenceTest failed");

  }

  /*
   * @testName: sequenceGeneratorOnEntityTest
   * 
   * @assertion_ids: PERSISTENCE:SPEC:2107; PERSISTENCE:SPEC:2107.1;
   * 
   * @test_Strategy: A sequence generator may be specified on the entity class
   */
  @SetupMethod(name = "setupDataTypes3")

  public void sequenceGeneratorOnEntityTest() throws Fault {

    boolean pass = true;
    if (supports_sequence) {
      final Integer newInt = 1000;

      try {
        getEntityTransaction().begin();
        clearCache();
        TestUtil.logMsg("Doing a find of id: " + d11.getId());
        DataTypes3 d = getEntityManager().find(DataTypes3.class, d11.getId());

        if (d != null) {
          Integer i = d.getIntegerData();
          if (i.equals(d11.getIntegerData())) {
            TestUtil.logTrace("find returned correct Integer value:" + i);
            d.setIntegerData(newInt);
          } else {
            TestUtil
                .logErr("find did not return correct Integer value, expected: "
                    + d11.getIntegerData() + ", actual:" + i);
            pass = false;
          }

          getEntityManager().merge(d);
          getEntityManager().flush();
          clearCache();
          TestUtil.logMsg("Doing a find of merged data for id: " + d.getId());
          DataTypes3 d2 = getEntityManager().find(DataTypes3.class, d.getId());
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
      throw new Fault("sequenceGeneratorOnEntityTest failed");

  }

  /*
   * @testName: sequenceGeneratorOnPropertyTest
   * 
   * @assertion_ids: PERSISTENCE:SPEC:2107; PERSISTENCE:SPEC:2107.3;
   * 
   * @test_Strategy: A sequence generator may be specified on the primary key
   * property
   */
  @SetupMethod(name = "setupDataTypes4")
  public void sequenceGeneratorOnPropertyTest() throws Fault {

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
      throw new Fault("sequenceGeneratorOnEntityTest failed");

  }

  // Methods used for Tests

  public void createTestData() {
    try {
      getEntityTransaction().begin();

      TestUtil.logTrace("in createTestData");

      TestUtil.logTrace("new DataType");
      d0 = new DataTypes('a', (short) 100, 500, 300L, 50D, 1.0F);
      TestUtil.logTrace("Persist DataType");
      getEntityManager().persist(d0);
      TestUtil.logTrace("DataType id:" + d0.getId());

      getEntityManager().flush();
      getEntityTransaction().commit();

    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }
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

  public void createDataTypes3Data() {
    try {
      getEntityTransaction().begin();

      TestUtil.logTrace("in createDataTypes3Data");

      TestUtil.logTrace("new DataType3");
      d11 = new DataTypes3(500);
      TestUtil.logTrace("Persist DataType3");
      getEntityManager().persist(d11);
      TestUtil.logTrace("DataType3 id:" + d11.getId());

      getEntityManager().flush();
      getEntityTransaction().commit();

    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }
  }

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
      getEntityManager().createNativeQuery("DELETE FROM DATATYPES")
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
