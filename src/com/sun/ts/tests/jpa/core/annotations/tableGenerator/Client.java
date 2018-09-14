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

package com.sun.ts.tests.jpa.core.annotations.tableGenerator;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.SetupMethod;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jpa.common.PMClientBase;

import java.util.Properties;

public class Client extends PMClientBase {

  private DataTypes d0;

  private DataTypes2 d2;

  private DataTypes3 d3;

  private DataTypes4 d4;

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
    } catch (Exception e) {
      TestUtil.logErr("Exception: ", e);
      throw new Fault("Setup failed:", e);
    }
  }

  public void setup2(String[] args, Properties p) throws Fault {
    TestUtil.logTrace("setup2");
    try {

      super.setup(args, p);
      removeTestData();
      createTestData2();
    } catch (Exception e) {
      TestUtil.logErr("Exception: ", e);
      throw new Fault("Setup failed:", e);
    }
  }

  public void setup3(String[] args, Properties p) throws Fault {
    TestUtil.logTrace("setup3");
    try {

      super.setup(args, p);
      removeTestData();
      createTestData3();
    } catch (Exception e) {
      TestUtil.logErr("Exception: ", e);
      throw new Fault("Setup failed:", e);
    }
  }

  public void setup4(String[] args, Properties p) throws Fault {
    TestUtil.logTrace("setup4");
    try {

      super.setup(args, p);
      removeTestData();
      createTestData4();
    } catch (Exception e) {
      TestUtil.logErr("Exception: ", e);
      throw new Fault("Setup failed:", e);
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

  public void generatorOnEntityTest() throws Fault {

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
      throw new Fault("generatorOnEntityTest failed");
  }

  /*
   * @testName: generatorOnFieldTest
   * 
   * @assertion_ids: PERSISTENCE:SPEC:2111; PERSISTENCE:SPEC:2111.2;
   * PERSISTENCE:SPEC:2113;
   * 
   * @test_Strategy: use a generator specified on a field
   */
  @SetupMethod(name = "setup3")
  public void generatorOnFieldTest() throws Fault {

    boolean pass = false;

    try {
      getEntityTransaction().begin();
      int id = d3.getId();
      TestUtil.logTrace("find id: " + id);
      DataTypes3 d = getEntityManager().find(DataTypes3.class, id);
      if (d != null) {
        if (d.getStringData().equals(d3.getStringData())) {
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
      throw new Fault("generatorOnFieldTest failed");
  }

  /*
   * @testName: generatorOnPropertyTest
   * 
   * @assertion_ids: PERSISTENCE:SPEC:2111; PERSISTENCE:SPEC:2111.3;
   * PERSISTENCE:SPEC:2113;
   * 
   * @test_Strategy: use a generator specified on a property
   */
  @SetupMethod(name = "setup2")
  public void generatorOnPropertyTest() throws Fault {

    boolean pass = false;

    try {
      getEntityTransaction().begin();
      int id = d2.getId();
      TestUtil.logTrace("find id: " + id);
      DataTypes2 d = getEntityManager().find(DataTypes2.class, id);
      if (d != null) {
        if (d.getStringData().equals(d2.getStringData())) {
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
      throw new Fault("generatorOnPropertyTest failed");
  }

  /*
   * @testName: generatorGlobalTest
   * 
   * @assertion_ids: PERSISTENCE:SPEC:2112; PERSISTENCE:SPEC:2113;
   * 
   * @test_Strategy: Use the generator defined by another entity
   */
  @SetupMethod(name = "setup4")
  public void generatorGlobalTest() throws Fault {

    boolean pass = false;

    try {
      getEntityTransaction().begin();
      int id = d4.getId();
      TestUtil.logTrace("find id: " + id);
      DataTypes4 d = getEntityManager().find(DataTypes4.class, id);
      if (d != null) {
        if (d.getStringData().equals(d4.getStringData())) {
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
      throw new Fault("generatorGlobalTest failed");
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

  public void createTestData2() {
    try {
      getEntityTransaction().begin();
      d2 = new DataTypes2();
      d2.setStringData("testData2");
      TestUtil.logTrace("DataType2:" + d2.toString());
      getEntityManager().persist(d2);

      getEntityManager().flush();
      getEntityTransaction().commit();

    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }
  }

  public void createTestData3() {
    try {
      getEntityTransaction().begin();

      d3 = new DataTypes3();
      d3.setStringData("testData3");
      TestUtil.logTrace("DataType3:" + d3.toString());
      getEntityManager().persist(d3);

      getEntityManager().flush();
      getEntityTransaction().commit();

    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }
  }

  public void createTestData4() {
    try {
      getEntityTransaction().begin();

      d4 = new DataTypes4();
      d4.setStringData("testData4");
      TestUtil.logTrace("DataType4:" + d4.toString());
      getEntityManager().persist(d4);

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
