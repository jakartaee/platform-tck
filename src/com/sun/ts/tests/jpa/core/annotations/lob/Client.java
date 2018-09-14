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

package com.sun.ts.tests.jpa.core.annotations.lob;

import com.sun.javatest.Status;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jpa.common.PMClientBase;

import java.util.Arrays;
import java.util.Properties;

public class Client extends PMClientBase {

  private DataTypes dataTypes;

  private Byte[] smallByteArray = null;

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
      TestUtil.logErr("Exception: ", e);
      throw new Fault("Setup failed:", e);
    }
  }

  /*
   * @testName: lobTest
   * 
   * @assertion_ids: PERSISTENCE:SPEC:524; PERSISTENCE:SPEC:528
   * 
   * @test_Strategy: The persistent property of an entity may be of the
   * following type: Byte[]
   *
   */
  public void lobTest() throws Fault {

    boolean pass1 = false;
    boolean pass2 = false;

    Byte[] largeByteArray = null;

    try {
      getEntityTransaction().begin();
      TestUtil.logMsg("FIND DataTypes and verify initial value");
      dataTypes = getEntityManager().find(DataTypes.class, 1);
      if ((null != dataTypes)
          && (Arrays.equals(smallByteArray, dataTypes.getByteArrayData()))) {
        TestUtil.logTrace("Expected results received");
        pass1 = true;
        TestUtil.logTrace("DataType Entity is not null, setting byteData ");
        largeByteArray = createLargeByteArray();
        dataTypes.setByteArrayData(largeByteArray);
        getEntityManager().merge(dataTypes);
        getEntityManager().flush();

        TestUtil.logMsg("FIND DataTypes again and verify updated value");
        dataTypes = getEntityManager().find(DataTypes.class, 1);

        TestUtil.logTrace("Check results");
        if ((null != dataTypes)
            && (Arrays.equals(largeByteArray, dataTypes.getByteArrayData()))) {
          TestUtil.logTrace("Expected results received");
          pass2 = true;
        } else {
          TestUtil.logErr("Unexpected result in array comparison.");
        }
      } else {
        TestUtil.logErr("Unexpected result in array comparison.");
      }
      getEntityTransaction().commit();
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

    if (!pass1 || !pass2) {
      throw new Fault("lobTest failed");
    }
  }

  // Methods used for Tests
  public void createTestData() {
    TestUtil.logTrace("createTestData");

    try {
      getEntityTransaction().begin();
      smallByteArray = createSmallByteArray();
      dataTypes = new DataTypes(1, smallByteArray);
      getEntityManager().persist(dataTypes);

      getEntityManager().flush();

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

  private Byte[] createSmallByteArray() {

    // Create a String of size 1MB
    StringBuffer strbuf = new StringBuffer();
    for (int i = 0; i < 1024; i++) {
      strbuf.append(i);
    }

    String value = strbuf.toString();
    System.out.println("String Buffer :" + value);

    // get byte array from the string
    final byte myByte[] = value.getBytes();

    // store primitive byte array to array of Byte objects
    Byte convertedByte[] = new Byte[myByte.length];
    for (int i = 0; i < myByte.length; i++) {
      convertedByte[i] = Byte.valueOf(myByte[i]);
    }

    return convertedByte;

  }

  private Byte[] createLargeByteArray() {

    // Create a String of size 4MB
    StringBuffer strbuf = new StringBuffer();
    for (int i = 0; i < 4096; i++) {
      strbuf.append(i);
    }

    String value = strbuf.toString();
    System.out.println("String Buffer :" + value);

    // get byte array from the string
    final byte myByte[] = value.getBytes();

    // store primitive byte array to array of Byte objects
    Byte convertedByte[] = new Byte[myByte.length];
    for (int i = 0; i < myByte.length; i++) {
      convertedByte[i] = Byte.valueOf(myByte[i]);
    }

    return convertedByte;

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
