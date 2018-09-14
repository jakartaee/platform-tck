/*
 * Copyright (c) 2008, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jpa.core.annotations.access.field;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.SetupMethod;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jpa.common.PMClientBase;
import com.sun.ts.tests.jpa.core.types.common.Grade;

import javax.persistence.Query;
import java.util.Arrays;
import java.util.Properties;

public class Client extends PMClientBase {

  private DataTypes d1;

  private DataTypes2 d2;

  final java.util.Date dateId = getPKDate(2006, 04, 15);

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
      createTestData();
      TestUtil.logTrace("Done creating test data");

    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
      throw new Fault("Setup failed:", e);
    }
  }

  public void setup2(String[] args, Properties p) throws Fault {
    TestUtil.logTrace("setup2");
    try {

      super.setup(args, p);
      removeTestData();
      createTestData2();
      TestUtil.logTrace("Done creating test data");

    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
      throw new Fault("Setup failed:", e);
    }
  }

  public void setup3(String[] args, Properties p) throws Fault {
    TestUtil.logTrace("setup3");
    try {

      super.setup(args, p);
      removeTestData();
      createTestData3();
      TestUtil.logTrace("Done creating test data");

    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
      throw new Fault("Setup failed:", e);
    }
  }
  /*
   * @testName: fieldTypeTest1
   * 
   * @assertion_ids: PERSISTENCE:SPEC:511; PERSISTENCE:SPEC:524;
   * PERSISTENCE:SPEC:534; PERSISTENCE:SPEC:512; PERSISTENCE:SPEC:525;
   * PERSISTENCE:JAVADOC:14; PERSISTENCE:JAVADOC:203; PERSISTENCE:JAVADOC:300;
   * PERSISTENCE:SPEC:1239; PERSISTENCE:SPEC:1320; PERSISTENCE:SPEC:1327.4;
   * PERSISTENCE:SPEC:1155; PERSISTENCE:SPEC:1976; PERSISTENCE:SPEC:1977;
   * 
   * @test_Strategy: The persistent field of an entity may be of the following
   * type: Java primitive types: boolean
   */

  public void fieldTypeTest1() throws Fault {

    boolean pass = false;

    try {
      getEntityTransaction().begin();
      d1 = getEntityManager().find(DataTypes.class, 1);
      if ((null != d1) && (!d1.isProperty())) {
        d1.setBooleanData(true);

        getEntityManager().merge(d1);
        getEntityManager().flush();

        if (d1.isProperty()) {
          TestUtil.logTrace("Received expected result:" + d1.isProperty());
          pass = true;
        } else {
          TestUtil.logErr("Expected: true, actual:" + d1.isProperty());
        }

        getEntityTransaction().commit();
      } else {
        TestUtil.logErr("find returned null");
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
      pass = false;
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception re) {
        TestUtil.logErr("Unexpected Exception during Rollback:", re);
      }
    }

    if (!pass)
      throw new Fault("fieldTypeTest1 failed");
  }

  /*
   * @testName: fieldTypeTest2
   * 
   * @assertion_ids: PERSISTENCE:SPEC:524; PERSISTENCE:SPEC:525;
   * PERSISTENCE:SPEC:513; PERSISTENCE:SPEC:1319
   * 
   * @test_Strategy: The persistent field of an entity may be of the following
   * type: Java primitive types: byte
   */

  public void fieldTypeTest2() throws Fault {

    boolean pass = false;
    byte newByte = (byte) 111;

    try {
      getEntityTransaction().begin();
      d1 = getEntityManager().find(DataTypes.class, 1);
      if ((null != d1) && (d1.getByteData() == (byte) 100)) {
        d1.setByteData(newByte);

        getEntityManager().merge(d1);
        getEntityManager().flush();

        if (d1.getByteData() == newByte) {
          pass = true;
        } else {
          TestUtil.logErr("Expected: 100, actual:" + d1.getByteData());
        }

        getEntityTransaction().commit();
      } else {
        TestUtil.logErr("find returned null");
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
      pass = false;
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception re) {
        TestUtil.logErr("Unexpected Exception during Rollback:", re);
      }
    }

    if (!pass)
      throw new Fault("fieldTypeTest2 failed");
  }

  /*
   * @testName: fieldTypeTest3
   * 
   * @assertion_ids: PERSISTENCE:SPEC:524; PERSISTENCE:SPEC:525;
   * PERSISTENCE:SPEC:1319
   * 
   * @test_Strategy: The persistent field of an entity may be of the following
   * type: Java primitive types: char
   */

  public void fieldTypeTest3() throws Fault {

    boolean pass = false;
    final char newChar = 'b';

    try {
      getEntityTransaction().begin();
      d1 = getEntityManager().find(DataTypes.class, 1);

      if ((null != d1) && (d1.getCharacterData() == ('a'))) {
        d1.setCharacterData(newChar);

        getEntityManager().merge(d1);
        getEntityManager().flush();

        if (d1.getCharacterData() == newChar) {
          pass = true;
        } else {
          TestUtil.logErr("Expected: a, actual:" + d1.getCharacterData());
        }
        getEntityTransaction().commit();
      } else {
        TestUtil.logErr("find returned null");
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
      pass = false;
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception re) {
        TestUtil.logErr("Unexpected Exception during Rollback:", re);
      }
    }

    if (!pass)
      throw new Fault("fieldTypeTest3 failed");
  }

  /*
   * @testName: fieldTypeTest4
   * 
   * @assertion_ids: PERSISTENCE:SPEC:524; PERSISTENCE:SPEC:525;
   * PERSISTENCE:SPEC:1319
   * 
   * @test_Strategy: The persistent field of an entity may be of the following
   * type: Java primitive types: short
   */

  public void fieldTypeTest4() throws Fault {

    boolean pass = false;
    final short newShort = (short) 101;

    try {

      getEntityTransaction().begin();
      d1 = getEntityManager().find(DataTypes.class, 1);
      if ((null != d1) && (d1.getShortData() == (short) 100)) {
        d1.setShortData(newShort);

        getEntityManager().merge(d1);
        getEntityManager().flush();

        if (d1.getShortData() == newShort) {
          pass = true;
        } else {
          TestUtil.logErr("Expected: 100, actual:" + d1.getShortData());
        }
        getEntityTransaction().commit();
      } else {
        TestUtil.logErr("find returned null");
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception re) {
        TestUtil.logErr("Unexpected Exception during Rollback:", re);
        pass = false;
      }
    }

    if (!pass)
      throw new Fault("fieldTypeTest4 failed");
  }

  /*
   * @testName: fieldTypeTest5
   * 
   * @assertion_ids: PERSISTENCE:SPEC:524; PERSISTENCE:SPEC:525;
   * PERSISTENCE:SPEC:1319
   * 
   * @test_Strategy: The persistent field of an entity may be of the following
   * type: Java primitive types: int
   */

  public void fieldTypeTest5() throws Fault {

    boolean pass = false;
    final int newInt = 500;

    try {
      getEntityTransaction().begin();
      d1 = getEntityManager().find(DataTypes.class, 1);
      if ((null != d1) && (d1.getIntData() == 300)) {
        d1.setIntData(newInt);

        getEntityManager().merge(d1);
        getEntityManager().flush();

        if (d1.getIntData() == newInt) {
          pass = true;
        } else {
          TestUtil.logErr("Expected: 300, actual:" + d1.getIntData());
        }
        getEntityTransaction().commit();
      } else {
        TestUtil.logErr("find returned null");
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
      pass = false;
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception re) {
        TestUtil.logErr("Unexpected Exception during Rollback:", re);
      }
    }

    if (!pass)
      throw new Fault("fieldTypeTest5 failed");
  }

  /*
   * @testName: fieldTypeTest6
   * 
   * @assertion_ids: PERSISTENCE:SPEC:524; PERSISTENCE:SPEC:525;
   * PERSISTENCE:SPEC:1319
   * 
   * @test_Strategy: The persistent field of an entity may be of the following
   * type: Java primitive types: long
   */

  public void fieldTypeTest6() throws Fault {

    boolean pass = false;
    final long newLong = 600L;

    try {
      getEntityTransaction().begin();
      d1 = getEntityManager().find(DataTypes.class, 1);
      if ((null != d1) && (d1.getLongData() == 600L)) {
        d1.setLongData(newLong);

        getEntityManager().merge(d1);
        getEntityManager().flush();

        if (d1.getLongData() == newLong) {
          pass = true;
        } else {
          TestUtil.logErr("Expected: 600, actual:" + d1.getLongData());
        }

        getEntityTransaction().commit();
      } else {
        TestUtil.logErr("find returned null");
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
      pass = false;
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception re) {
        TestUtil.logErr("Unexpected Exception during Rollback:", re);
      }
    }

    if (!pass)
      throw new Fault("fieldTypeTest6 failed");
  }

  /*
   * @testName: fieldTypeTest7
   * 
   * @assertion_ids: PERSISTENCE:SPEC:524; PERSISTENCE:SPEC:525;
   * PERSISTENCE:SPEC:1319
   * 
   * @test_Strategy: The persistent field of an entity may be of the following
   * type: Java primitive types: double
   */

  public void fieldTypeTest7() throws Fault {

    boolean pass = false;
    final double newDbl = 80D;

    try {
      getEntityTransaction().begin();
      d1 = getEntityManager().find(DataTypes.class, 1);
      if ((null != d1) && (d1.getDoubleData() == (50D))) {
        d1.setDoubleData(newDbl);

        getEntityManager().merge(d1);
        getEntityManager().flush();

        if (d1.getDoubleData() == newDbl) {
          pass = true;
        } else {
          TestUtil.logErr("Expected: 50, actual:" + d1.getDoubleData());
        }
        getEntityTransaction().commit();
      } else {
        TestUtil.logErr("find returned null");
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
      pass = false;
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception re) {
        TestUtil.logErr("Unexpected Exception during Rollback:", re);
      }
    }

    if (!pass)
      throw new Fault("fieldTypeTest7 failed");
  }

  /*
   * @testName: fieldTypeTest8
   * 
   * @assertion_ids: PERSISTENCE:SPEC:524; PERSISTENCE:SPEC:525;
   * PERSISTENCE:SPEC:1319
   * 
   * @test_Strategy: The persistent field of an entity may be of the following
   * type: Java primitive types: float
   */

  public void fieldTypeTest8() throws Fault {

    boolean pass = false;
    final float expFloat = 1.0F;
    final float floatRange = 2.0F;
    final float newFloat = 6.0F;
    final float newfloatRange = 7.0F;

    try {
      getEntityTransaction().begin();
      d1 = getEntityManager().find(DataTypes.class, 1);
      if (null != d1) {

        TestUtil.logTrace("float value is: " + d1.getFloatData());

        if ((d1.getFloatData() >= expFloat)
            && (d1.getFloatData() < floatRange)) {
          d1.setFloatData(newFloat);

          getEntityManager().merge(d1);
          getEntityManager().flush();

          if ((d1.getFloatData() >= newFloat)
              && (d1.getFloatData() < newfloatRange)) {
            pass = true;
          } else {
            TestUtil.logErr("Expected: >= " + newFloat + " and < "
                + newfloatRange + ", actual:" + d1.getFloatData());
          }
        }
        getEntityTransaction().commit();
      } else {
        TestUtil.logErr("find returned null");
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
      pass = false;
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception re) {
        TestUtil.logErr("Unexpected Exception during Rollback:", re);
      }
    }

    if (!pass)
      throw new Fault("fieldTypeTest8 failed");
  }

  /*
   * @testName: fieldTypeTest9
   * 
   * @assertion_ids: PERSISTENCE:SPEC:524; PERSISTENCE:SPEC:529;
   * PERSISTENCE:SPEC:556; PERSISTENCE:SPEC:1319
   * 
   * @test_Strategy: The persistent field of an entity may be of the following
   * type: enums
   *
   * With the Enumerated annotation and EnumType.STRING.
   */

  public void fieldTypeTest9() throws Fault {

    boolean pass = false;

    try {
      getEntityTransaction().begin();
      TestUtil.logTrace("find DataTypes entity in fieldTypeTest9");
      d1 = getEntityManager().find(DataTypes.class, 1);

      if (null != d1) {
        TestUtil.logTrace("DataTypes is not null, setting enumData");
        d1.setEnumData(Grade.B);

        getEntityManager().merge(d1);
        getEntityManager().flush();

        TestUtil.logTrace("Update performed, check results");
        if ((null != d1) && (d1.getEnumData().equals(Grade.B))) {
          TestUtil.logTrace("Expected results received");
          pass = true;
        } else {
          TestUtil.logErr("Expected: " + Grade.B.toString() + ", actual:"
              + d1.getEnumData());
        }

        getEntityTransaction().commit();
      } else {
        TestUtil.logErr("find returned null");
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
      pass = false;
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception re) {
        TestUtil.logErr("Unexpected Exception during Rollback:", re);
      }
    }

    if (!pass)
      throw new Fault("fieldTypeTest9 failed");
  }

  /*
   * @testName: fieldTypeTest10
   * 
   * @assertion_ids: PERSISTENCE:SPEC:536; PERSISTENCE:SPEC:540;
   * PERSISTENCE:SPEC:550; PERSISTENCE:SPEC:1090.0; PERSISTENCE:JAVADOC:216;
   * PERSISTENCE:JAVADOC:217; PERSISTENCE:SPEC:1319
   * 
   * @test_Strategy: The primary key should be one of the following types:
   * java.util.Date
   *
   * The application must not change the value of the primary key. The behavior
   * is undefined if this occurs.
   *
   * Temporal.TemporalType.DATE
   */

  public void fieldTypeTest10() throws Fault {

    boolean pass = false;

    try {
      getEntityTransaction().begin();
      TestUtil.logTrace("FIND D2 IN fieldTypeTest10");
      d2 = getEntityManager().find(DataTypes2.class, dateId);
      if (null != d2) {

        TestUtil.logTrace("fieldTypeTest10:  Check results");
        if (d2.getId().equals(dateId)) {
          TestUtil.logTrace("Got expected PK of:" + d2.getId() + "received");
          pass = true;
        } else {
          TestUtil.logErr("Expected: " + dateId + ", actual: " + d2.getId());
        }

        getEntityTransaction().commit();
      } else {
        TestUtil.logErr("find returned null");
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
      pass = false;
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception re) {
        TestUtil.logErr("Unexpected Exception during Rollback:", re);
      }
    }

    if (!pass)
      throw new Fault("fieldTypeTest10 failed");
  }

  /*
   * @testName: fieldTypeTest11
   * 
   * @assertion_ids: PERSISTENCE:SPEC:524; PERSISTENCE:SPEC:528;
   * PERSISTENCE:SPEC:1089; PERSISTENCE:SPEC:1319
   * 
   * @test_Strategy: The persistent property of an entity may be of the
   * following type: byte[]
   *
   */

  public void fieldTypeTest11() throws Fault {

    boolean pass = false;
    final byte[] b = { 31, 32, 33, 63, 64, 65 };
    final byte bv = 5;
    byte[] a = null;

    try {
      getEntityTransaction().begin();
      TestUtil.logTrace("FIND D1 IN fieldTypeTest11");
      d1 = getEntityManager().find(DataTypes.class, 1);

      if (null != d1) {
        TestUtil.logTrace("DataType Entity is not null, setting byteData ");
        d1.setByteArrayData(b);
        a = d1.getByteArrayData();
        a[0] = (byte) (a[0] + bv);
        d1.setByteArrayData(b);

        getEntityManager().merge(d2);
        getEntityManager().flush();

        TestUtil.logTrace("fieldTypeTest11:  Check results");
        if ((null != d2) && (Arrays.equals(d1.getByteArrayData(), a))) {
          TestUtil.logTrace("fieldTypeTest11: Expected results received");
          pass = true;
        } else {
          TestUtil.logErr("Unexpected result in array comparison.");
          for (Byte aByte : a) {
            TestUtil.logErr("Array a in propertyTest9 equals: " + aByte);
          }
          for (Byte bByte : b) {
            TestUtil.logErr("Array b in propertyTest9 equals: " + bByte);
          }
          pass = false;
        }

        getEntityTransaction().commit();
      } else {
        TestUtil.logErr("find returned null");
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
      pass = false;
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception re) {
        TestUtil.logErr("Unexpected Exception during Rollback:", re);
      }
    }

    if (!pass)
      throw new Fault("fieldTypeTest11 failed");
  }

  /*
   * @testName: fieldTypeTest12
   * 
   * @assertion_ids: PERSISTENCE:SPEC:524; PERSISTENCE:SPEC:528;
   * PERSISTENCE:SPEC:1319
   * 
   * @test_Strategy: The persistent property of an entity may be of the
   * following type: char[]
   *
   */

  public void fieldTypeTest12() throws Fault {

    boolean pass = false;
    final char[] charData = new char[] { 'c', 't', 's' };

    try {
      getEntityTransaction().begin();
      TestUtil.logTrace("FIND D1 IN fieldTypeTest12");
      d1 = getEntityManager().find(DataTypes.class, 1);

      if (null != d1) {
        TestUtil.logTrace("DataType Entity is not null, setting charData ");
        d1.setCharArrayData(charData);

        getEntityManager().merge(d1);
        getEntityManager().flush();

        TestUtil.logTrace("fieldTypeTest12:  Check results");
        if ((null != d1) && (Arrays.equals(d1.getCharArrayData(), charData))) {
          TestUtil.logTrace("fieldTypeTest12: Expected Results Received");
          pass = true;
        } else {
          TestUtil.logErr("Expected " + Arrays.toString(charData) + ", actual: "
              + Arrays.toString(d1.getCharArrayData()));
        }

        getEntityTransaction().commit();
      } else {
        TestUtil.logErr("find returned null");
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
      pass = false;
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception re) {
        TestUtil.logErr("Unexpected Exception during Rollback:", re);
      }
    }

    if (!pass)
      throw new Fault("fieldTypeTest12 failed");
  }

  /*
   * @testName: fieldTypeTest13
   * 
   * @assertion_ids: PERSISTENCE:SPEC:524; PERSISTENCE:SPEC:527;
   * PERSISTENCE:SPEC:1319
   * 
   * @test_Strategy: The persistent property of an entity may be of the
   * following type: java.sql.Time
   */

  public void fieldTypeTest13() throws Fault {

    boolean pass = false;
    final java.sql.Time timeValue = getTimeData(18, 30, 15);

    try {
      getEntityTransaction().begin();
      TestUtil.logTrace("FIND D2 IN fieldTypeTest13");
      d2 = getEntityManager().find(DataTypes2.class, dateId);

      if (null != d2) {
        TestUtil.logTrace("DataType Entity is not null, setting TimeData ");
        d2.setTimeData(timeValue);

        getEntityManager().merge(d2);
        getEntityManager().flush();

        TestUtil.logTrace("fieldTypeTest13:  Check results");
        if ((null != d2) && (d2.getTimeData().equals(timeValue))) {
          TestUtil.logTrace("fieldTypeTest13: Expected Time Received");
          pass = true;
        } else {
          TestUtil.logErr(
              "Expected " + timeValue + " , actual: " + d2.getTimeData());
        }

        getEntityTransaction().commit();
      } else {
        TestUtil.logErr("find returned null");
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
      pass = false;
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception re) {
        TestUtil.logErr("Unexpected Exception during Rollback:", re);
      }
    }

    if (!pass)
      throw new Fault("fieldTypeTest13 failed");
  }

  /*
   * @testName: fieldTypeTest14
   * 
   * @assertion_ids: PERSISTENCE:SPEC:524; PERSISTENCE:SPEC:527;
   * PERSISTENCE:SPEC:1319
   * 
   * @test_Strategy: The persistent property of an entity may be of the
   * following type: java.sql.Timestamp
   */

  public void fieldTypeTest14() throws Fault {

    boolean pass = false;
    final java.sql.Timestamp tsValue = getTimestampData(2006, 02, 11);

    try {
      getEntityTransaction().begin();
      TestUtil.logTrace("FIND D2 IN fieldTypeTest14");
      d2 = getEntityManager().find(DataTypes2.class, dateId);

      if (null != d2) {
        TestUtil
            .logTrace("DataType Entity is not null, setting TimestampData ");
        d2.setTsData(tsValue);

        getEntityManager().merge(d2);
        getEntityManager().flush();

        TestUtil.logTrace("fieldTypeTest14:  Check results");
        if ((null != d2) && (d2.getTsData().equals(tsValue))) {
          TestUtil.logTrace("fieldTypeTest14: Expected Timestamp Received");
          pass = true;
        } else {
          TestUtil
              .logErr("Expected " + tsValue + " , actual: " + d2.getTsData());
        }

        getEntityTransaction().commit();
      } else {
        TestUtil.logErr("find returned null");
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
      pass = false;
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception re) {
        TestUtil.logErr("Unexpected Exception during Rollback:", re);
      }
    }

    if (!pass)
      throw new Fault("fieldTypeTest14 failed");
  }

  /*
   * @testName: fieldTypeTest15
   * 
   * @assertion_ids: PERSISTENCE:SPEC:428; PERSISTENCE:SPEC:529;
   * PERSISTENCE:SPEC:1090.1; PERSISTENCE:SPEC:1090.2; PERSISTENCE:SPEC:1319
   * 
   * @test_Strategy: enum_expression ::= enum_primary | (subquery) enum_primary
   * ::= statefield_path_expression | input parameter | enum_literal
   *
   * statefield_path_expression
   */

  public void fieldTypeTest15() throws Fault {

    boolean pass = false;
    Object result;
    Query q;

    try {

      getEntityTransaction().begin();
      d1 = getEntityManager().find(DataTypes.class, 1);

      if (null != d1) {
        d1.setEnumData(Grade.A);

        getEntityManager().merge(d1);
        getEntityManager().flush();

        q = getEntityManager().createQuery(
            "SELECT dt FROM DataTypes dt WHERE dt.enumData = com.sun.ts.tests.jpa.core.types.common.Grade.A");

        result = (DataTypes) q.getSingleResult();

        if (d1.equals(result)) {
          pass = true;
          TestUtil.logTrace("Received expected result:" + d1.toString());
        } else {
          TestUtil.logErr(
              "Expected:" + d1.toString() + ", actual:" + result.toString());
        }

        getEntityTransaction().commit();
      } else {
        TestUtil.logErr("find returned null");
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception re) {
        TestUtil.logErr("Unexpected Exception during Rollback:", re);
      }
    }

    if (!pass)
      throw new Fault("fieldTypeTest15 failed");
  }

  /*
   * @testName: fieldTypeTest16
   * 
   * @assertion_ids: PERSISTENCE:SPEC:428; PERSISTENCE:SPEC:529;
   * PERSISTENCE:SPEC:1090.1; PERSISTENCE:SPEC:1090.2; PERSISTENCE:SPEC:1319
   * 
   * @test_Strategy: enum_expression ::= enum_primary | (subquery) enum_primary
   * ::= state_field_path_expression | input parameter | enum_literal
   *
   * named parameter
   */

  public void fieldTypeTest16() throws Fault {

    boolean pass = false;
    Object result;
    Query q;

    try {

      getEntityTransaction().begin();
      d1 = getEntityManager().find(DataTypes.class, 1);

      if (null != d1) {
        d1.setEnumData(Grade.A);

        getEntityManager().merge(d1);
        getEntityManager().flush();

        q = getEntityManager()
            .createQuery(
                "SELECT dt FROM DataTypes dt WHERE dt.enumData = :grade")
            .setParameter("grade", Grade.A);

        result = (DataTypes) q.getSingleResult();

        if (d1.equals(result)) {
          pass = true;
          TestUtil.logTrace("Received expected result:" + d1.toString());
        } else {
          TestUtil.logErr(
              "Expected:" + d1.toString() + ", actual:" + result.toString());
        }
        getEntityTransaction().commit();
      } else {
        TestUtil.logErr("find returned null");
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception re) {
        TestUtil.logErr("Unexpected Exception during Rollback:", re);
      }
    }

    if (!pass)
      throw new Fault("fieldTypeTest16 failed");
  }

  /*
   * @testName: fieldTypeTest17
   * 
   * @assertion_ids: PERSISTENCE:SPEC:428; PERSISTENCE:SPEC:529;
   * PERSISTENCE:SPEC:1090.1; PERSISTENCE:SPEC:1090.2; PERSISTENCE:SPEC:1319
   * 
   * @test_Strategy: enum_expression ::= enum_primary | (subquery) enum_primary
   * ::= state_field_path_expression | input parameter | enum_literal
   *
   * positional parameters
   */

  public void fieldTypeTest17() throws Fault {

    boolean pass = false;
    Object result;
    Query q;
    final Grade failingGrade = com.sun.ts.tests.jpa.core.types.common.Grade.F;
    final Grade incompleteGrade = com.sun.ts.tests.jpa.core.types.common.Grade.INCOMPLETE;

    try {

      getEntityTransaction().begin();
      d1 = getEntityManager().find(DataTypes.class, 1);

      if (null != d1) {
        d1.setEnumData(Grade.C);

        getEntityManager().merge(d1);
        getEntityManager().flush();

        q = getEntityManager().createQuery(
            "SELECT dt FROM DataTypes dt WHERE (dt.enumData <> ?1) OR (dt.enumData <> ?2) ")
            .setParameter(1, failingGrade).setParameter(2, incompleteGrade);

        result = (DataTypes) q.getSingleResult();

        if (d1.equals(result)) {
          pass = true;
          TestUtil.logTrace("Received expected result:" + d1.toString());
        } else {
          TestUtil.logErr(
              "Expected:" + d1.toString() + ", actual:" + result.toString());
        }

        getEntityTransaction().commit();
      } else {
        TestUtil.logErr("find returned null");
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
      pass = false;
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception re) {
        TestUtil.logErr("Unexpected Exception during Rollback:", re);
      }
    }

    if (!pass)
      throw new Fault("fieldTypeTest17 failed");
  }

  /*
   * @testName: mixedAccessTest
   * 
   * @assertion_ids: PERSISTENCE:SPEC:1327.3;
   * 
   * @test_Strategy:
   */
  @SetupMethod(name = "setup2")
  public void mixedAccessTest() throws Fault {

    boolean pass = false;
    final int newInt = 500;

    try {
      getEntityTransaction().begin();
      d1 = getEntityManager().find(DataTypes.class, 1);
      if ((null != d1) && (d1.getIntData2() == 300)) {
        TestUtil.logMsg("Int value after find=" + d1.getIntData2());
        d1.setIntData2(newInt);
        TestUtil.logMsg("Int value after set=" + d1.getIntData2());
        getEntityManager().merge(d1);
        getEntityManager().flush();
        clearCache();
        d1 = null;
        d1 = getEntityManager().find(DataTypes.class, 1);

        if (d1.getIntData2() == newInt) {
          pass = true;
          TestUtil.logTrace("Received expected value:" + d1.getIntData2());
        } else {
          TestUtil
              .logErr("Expected:" + newInt + ", actual:" + d1.getIntData2());
        }
        getEntityTransaction().commit();
      } else {
        TestUtil.logErr("find returned null");
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
      pass = false;
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception re) {
        TestUtil.logErr("Unexpected Exception during Rollback:", re);
      }
    }

    if (!pass)
      throw new Fault("mixedAccessTest failed");
  }

  /*
   * @testName: transientTest
   * 
   * @assertion_ids: PERSISTENCE:SPEC:1327.2
   * 
   * @test_Strategy: when transient is specified, verify data isn't persisted
   */
  @SetupMethod(name = "setup3")
  public void transientTest() throws Fault {

    boolean pass = false;
    byte newByte = (byte) 111;

    try {
      getEntityTransaction().begin();
      clearCache();
      d1 = getEntityManager().find(DataTypes.class, 1);
      if (null != d1) {
        if ((d1.getTransient() == (byte) 0)) {
          TestUtil.logTrace(
              "First find returned expected result:" + d1.getTransient());
          d1.setTransient(newByte);

          getEntityManager().merge(d1);
          getEntityManager().flush();
          clearCache();
          d1 = null;
          d1 = getEntityManager().find(DataTypes.class, 1);

          if (d1.getTransient() == (byte) 0) {
            pass = true;
            TestUtil.logTrace(
                "Second find returned expected value:" + d1.getTransient());
          } else {
            TestUtil
                .logErr("Second find expected:0, actual:" + d1.getTransient());
          }
        } else {
          TestUtil.logErr(
              "Expected first find to return:0, actual:" + d1.getTransient());
        }
        getEntityTransaction().commit();
      } else {
        TestUtil.logErr("find returned null");
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
      pass = false;
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception re) {
        TestUtil.logErr("Unexpected Exception during Rollback:", re);
      }
    }

    if (!pass)
      throw new Fault("transientTest failed");
  }

  // Methods used for Tests

  public void createTestData() {
    TestUtil.logTrace("createTestData");

    try {
      getEntityTransaction().begin();
      char[] cArray = { 'a' };
      byte[] bArray = { (byte) 100 };
      d1 = new DataTypes(1, false, (byte) 100, 'a', (short) 100, 300, 600L, 50D,
          1.0F, cArray, bArray);

      TestUtil.logTrace("dateId is: " + dateId);
      d2 = new DataTypes2(dateId);

      getEntityManager().persist(d1);
      getEntityManager().persist(d2);
      getEntityTransaction().commit();

    } catch (Exception e) {
      TestUtil.logErr("Unexpected Exception in createTestData:", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception re) {
        TestUtil.logErr("Unexpected Exception during Rollback:", re);
      }
    }

  }

  public void createTestData2() {
    TestUtil.logTrace("createTestData2");

    try {
      getEntityTransaction().begin();
      d1 = new DataTypes(1, 300);

      getEntityManager().persist(d1);
      getEntityTransaction().commit();

    } catch (Exception e) {
      TestUtil.logErr("Unexpected Exception in createTestData:", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception re) {
        TestUtil.logErr("Unexpected Exception during Rollback:", re);
      }
    }

  }

  public void createTestData3() {
    TestUtil.logTrace("createTestData3");

    try {
      getEntityTransaction().begin();
      d1 = new DataTypes(1, (byte) 5);

      getEntityManager().persist(d1);
      getEntityTransaction().commit();

    } catch (Exception e) {
      TestUtil.logErr("Unexpected Exception in createTestData:", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception re) {
        TestUtil.logErr("Unexpected Exception during Rollback:", re);
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
      TestUtil.logMsg("isActive:" + getEntityTransaction().isActive());
      getEntityManager().createNativeQuery("Delete from DATATYPES")
          .executeUpdate();
      getEntityManager().createNativeQuery("Delete from DATATYPES2")
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
