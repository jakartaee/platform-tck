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

package com.sun.ts.tests.jpa.core.annotations.nativequery;

import com.sun.javatest.Status;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jpa.common.PMClientBase;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class Client extends PMClientBase {

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
   * @testName: nativeQueryTest1
   * 
   * @assertion_ids: PERSISTENCE:SPEC:1009; PERSISTENCE:JAVADOC:199;
   * PERSISTENCE:JAVADOC:64; PERSISTENCE:JAVADOC:41; PERSISTENCE:SPEC:736;
   * 
   * @test_Strategy: When multiple entities are returned by a SQL query, the
   * entities must be mapped to the column results of the SQL statement in a
   * SqlResultSetMapping metadata definition.
   *
   * The query with the associated SqlResultSetMapping metadata returns multiple
   * entities and uses default metadata and column name defaults.
   */

  public void nativeQueryTest1() throws Fault {

    boolean pass = false;
    int passCounter = 0;
    List q;

    try {
      getEntityTransaction().begin();

      TestUtil.logTrace("Create Items");
      final Item i1 = new Item(4, "WaterShoes");
      final Item i2 = new Item(5, "FlipFlops");
      final Item i3 = new Item(6, "Sandals");

      TestUtil.logTrace("Create Order1s");
      Order1 o4 = new Order1(4, 25.0D);
      o4.setItem(i1);
      getEntityManager().persist(o4);
      Order1 o5 = new Order1(5, 125.0D);
      o5.setItem(i2);
      getEntityManager().persist(o5);
      Order1 o6 = new Order1(6, 150.0D);
      o6.setItem(i3);
      getEntityManager().persist(o6);

      TestUtil.logTrace("Execute Query ");
      q = getEntityManager().createNativeQuery(
          "Select o.\"ID\", o.\"TOTALPRICE\", "
              + " o.\"FK1_FOR_ITEM\", i.\"ID\", i.\"ITEMNAME\" from \"ORDER1\" o, \"ITEM\" i "
              + "WHERE (o.\"TOTALPRICE\" > 140) AND (o.\"FK1_FOR_ITEM\" = i.\"ID\")",
          "Order1ItemResults").getResultList();

      if (q.size() != 1) {
        TestUtil.logErr(" Did not get expected results.  Expected: 1, "
            + "got: " + q.size());
      } else {
        TestUtil.logTrace("Expected size received, verify contents . . . ");
        for (Object obj : q) {

          // each element in the query result list should be an Object[], which
          // has exactly 2 elements. The first element is of type Order1, and
          // the
          // second of type Item.

          Object[] objectArray = (Object[]) obj;

          // make sure this object array has exactly 2 elements.
          if (objectArray.length != 2) {
            TestUtil.logErr("Expecting the object array have 2 elements, "
                + "but the object array is " + Arrays.toString(objectArray));
          }

          for (Object o : objectArray) {

            if (o instanceof Order1) {
              Order1 orderReturned = (Order1) o;

              if (!orderReturned.equals(o6)) {
                TestUtil.logErr("Expected:" + o6 + ", actual:" + orderReturned);
              } else {
                TestUtil.logTrace("Received expected order");
                passCounter++;
              }
            } else if (o instanceof Item) {
              Item itemReturned = (Item) o;
              if (!itemReturned.equals(i3)) {
                TestUtil.logErr("Expected:" + i3 + ", actual:" + itemReturned);
              } else {
                TestUtil.logTrace("Received expected item");

                passCounter++;
              }

            } else {
              TestUtil.logErr("Received unexpected object:" + o);

            }
          }
        }
      }

      if (passCounter == 2) {
        pass = true;
      }
      getEntityTransaction().commit();

    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }

    if (!pass)
      throw new Fault("nativeQueryTest1 failed");
  }

  /*
   * @testName: nativeQueryTest2
   * 
   * @assertion_ids: PERSISTENCE:SPEC:1010; PERSISTENCE:SPEC:1011;
   * PERSISTENCE:JAVADOC:198; PERSISTENCE:JAVADOC:64; PERSISTENCE:JAVADOC:77;
   * PERSISTENCE:JAVADOC:78; PERSISTENCE:JAVADOC:21; PERSISTENCE:JAVADOC:65;
   * PERSISTENCE:SPEC:1370.2; PERSISTENCE:SPEC:1370.3;
   * 
   * @test_Strategy: When multiple entities are returned by a SQL query, the
   * entities must be mapped to the column results of the SQL statement in a
   * SqlResultSetMapping metadata definition.
   *
   * Scalar result types can be included in the query result by specifying the
   * ColumnResult annotation in the metadata.
   *
   * 
   */

  public void nativeQueryTest2() throws Fault {

    boolean pass = false;
    int passCounter = 0;
    List q;

    try {
      getEntityTransaction().begin();

      TestUtil.logTrace("Create Items");
      final Item i1 = new Item(7, "Loafers");
      final Item i2 = new Item(8, "High Heels");
      final Item i3 = new Item(9, "Socks");

      TestUtil.logTrace("Create Orders");

      Order1 o7 = new Order1(7, 25.0D);
      o7.setItem(i1);
      getEntityManager().persist(o7);
      Order1 o8 = new Order1(8, 125.0D);
      o8.setItem(i2);
      getEntityManager().persist(o8);
      Order1 o9 = new Order1(9, 150.0D);
      o9.setItem(i3);
      getEntityManager().persist(o9);

      TestUtil.logTrace("Execute Query");
      q = getEntityManager().createNativeQuery(
          "Select o.\"ID\" AS \"OID\", o.\"TOTALPRICE\" AS \"OPRICE\", "
              + "o.\"FK1_FOR_ITEM\" AS \"OITEM\", i.\"ITEMNAME\" AS \"INAME\" from \"ORDER1\" o, \"ITEM\" i "
              + "WHERE (o.\"TOTALPRICE\" < 100) AND (o.\"FK1_FOR_ITEM\" = i.\"ID\")",
          "Order2ItemResults").getResultList();

      if (q.size() != 1) {
        TestUtil.logErr(" Did not get expected results.  Expected: 1, "
            + "got: " + q.size());
      } else {
        TestUtil.logTrace("Expected size received, verify contents . . . ");

        for (Object obj : q) {
          // each element in the query result list should be an Object[], which
          // has exactly 2 elements. The first element is of type Order1, and
          // the
          // second of type String.
          if (obj instanceof Object[]) {
            Object[] objectArray = (Object[]) obj;

            for (Object o : objectArray) {

              if (o instanceof Order1) {
                Order1 orderReturned = (Order1) o;

                if (!orderReturned.equals(o7)) {
                  TestUtil
                      .logErr("Expected:" + o7 + ", actual:" + orderReturned);
                } else {
                  TestUtil.logTrace("Received expected order");
                  passCounter++;
                }
                if (orderReturned.getItem() != i1) {
                  TestUtil.logErr(
                      "Expected:" + i1 + ", actual:" + orderReturned.getItem());
                } else {
                  TestUtil.logTrace("Received expected item");
                  passCounter++;
                }
              } else if (o instanceof String) {

                String itemName = (String) o;
                if (!itemName.equals(i1.getItemName())) {
                  TestUtil.logErr(
                      "Expected:" + i1.getItemName() + ", actual:" + itemName);
                } else {
                  TestUtil.logTrace("Received expected itemName");
                  passCounter++;
                }

              } else {
                TestUtil.logErr("Received unexpected object:" + o);
              }
            }

          }
        }
      }
      if (passCounter == 3) {
        pass = true;
      }

      getEntityTransaction().commit();

    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }

    if (!pass)
      throw new Fault("nativeQueryTest2 failed");
  }

  /*
   * @testName: nativeQueryTest3
   * 
   * @assertion_ids: PERSISTENCE:SPEC:1012; PERSISTENCE:JAVADOC:200;
   * PERSISTENCE:SPEC:1014; PERSISTENCE:SPEC:1015; PERSISTENCE:SPEC:1016;
   * PERSISTENCE:JAVADOC:201
   * 
   * @test_Strategy: When multiple entities are returned by a SQL query, the
   * entities must be mapped to the column results of the SQL statement in a
   * SqlResultSetMapping metadata definition.
   *
   * When multiple entity types that include aliases in the SQL statement are
   * used, it is required that the column names be explicitly mapped to the
   * entity fields. The FieldResult annotation is used for this purpose.
   *
   */

  public void nativeQueryTest3() throws Fault {
    boolean pass = false;
    List q;
    int passCounter = 0;

    try {
      getEntityTransaction().begin();

      TestUtil.logTrace("Create Items");
      final Item i1 = new Item(1, "Boots");
      final Item i2 = new Item(2, "Sneakers");
      final Item i3 = new Item(3, "Slippers");

      TestUtil.logTrace("Create Orders");
      Order1 o1 = new Order1(1, 25.0D);
      o1.setItem(i1);
      getEntityManager().persist(o1);
      Order1 o2 = new Order1(2, 125.0D);
      o2.setItem(i2);
      getEntityManager().persist(o2);
      Order1 o3 = new Order1(3, 150.0D);
      o3.setItem(i3);
      getEntityManager().persist(o3);

      TestUtil.logTrace("Execute Query");
      q = getEntityManager().createNativeQuery(
          "Select o.\"ID\" AS \"THISID\", o.\"TOTALPRICE\" AS \"THISPRICE\", "
              + "o.\"FK1_FOR_ITEM\" AS \"THISITEM\", i.\"ID\", i.\"ITEMNAME\" from \"ORDER1\" o, \"ITEM\" i "
              + "WHERE (o.\"TOTALPRICE\" > 140) AND (o.\"FK1_FOR_ITEM\" = i.\"ID\")",
          "Order3ItemResults").getResultList();

      if (q.size() != 1) {
        TestUtil.logErr(" Did not get expected results.  Expected: 1, "
            + "got: " + q.size());
      } else {
        TestUtil.logTrace("Expected size received, verify contents . . . ");

        for (Object obj : q) {
          // each element in the query result list should be an Object[], which
          // has exactly 2 elements. The first element is of type Order1, and
          // the
          // second of type String.
          if (obj instanceof Object[]) {
            Object[] objectArray = (Object[]) obj;

            for (Object o : objectArray) {

              if (o instanceof Order1) {
                Order1 orderReturned = (Order1) o;

                if (!orderReturned.equals(o3)) {
                  TestUtil
                      .logErr("Expected:" + o3 + ", actual:" + orderReturned);
                } else {
                  TestUtil.logTrace("Received expected order");
                  passCounter++;
                }
                if (orderReturned.getItem() != i3) {
                  TestUtil.logErr(
                      "Expected:" + i3 + ", actual:" + orderReturned.getItem());
                } else {
                  TestUtil.logTrace("Received expected item");
                  passCounter++;
                }
              } else if (o instanceof Item) {
                Item item = (Item) o;
                if (item != i3) {
                  TestUtil.logErr("Expected:" + i3 + ", actual:" + item);
                } else {
                  TestUtil.logTrace("Received expected item");
                  passCounter++;
                }

              } else {
                TestUtil.logErr("Received unexpected object:" + o);
              }
            }

          }
        }
      }
      if (passCounter == 3) {
        pass = true;
      }

      getEntityTransaction().commit();

    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }

    if (!pass)
      throw new Fault("nativeQueryTest3 failed");
  }

  /*
   * @testName: nativeQueryColumnResultTypeTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1511; PERSISTENCE:SPEC:1556;
   * PERSISTENCE:SPEC:1954;
   * 
   * @test_Strategy: Using the javax.persistence.ColumnResult.type attribute,
   * return the totalprice as a String
   *
   */

  public void nativeQueryColumnResultTypeTest() throws Fault {

    boolean pass = false;
    List q;
    int passCounter = 0;

    try {
      getEntityTransaction().begin();

      TestUtil.logTrace("Create Items");
      final Item i1 = new Item(7, "Loafers");
      final Item i2 = new Item(8, "High Heels");
      final Item i3 = new Item(9, "Socks");

      TestUtil.logTrace("Create Orders");

      Order1 o7 = new Order1(7, 25.0D);
      o7.setItem(i1);
      getEntityManager().persist(o7);
      Order1 o8 = new Order1(8, 125.0D);
      o8.setItem(i2);
      getEntityManager().persist(o8);
      Order1 o9 = new Order1(9, 150.0D);
      o9.setItem(i3);
      getEntityManager().persist(o9);

      TestUtil.logTrace("Execute Query");
      q = getEntityManager().createNativeQuery(
          "Select o.\"ID\" AS \"OID\", o.\"TOTALPRICE\" AS \"OPRICE\", "
              + "o.\"FK1_FOR_ITEM\" AS \"OITEM\", i.\"ITEMNAME\" AS \"INAME\" from \"ORDER1\" o, \"ITEM\" i "
              + "WHERE (o.\"TOTALPRICE\" < 100) AND (o.\"FK1_FOR_ITEM\" = i.\"ID\")",
          "Order4ItemResults").getResultList();

      if (q.size() != 1) {
        TestUtil.logErr(" Did not get expected results.  Expected: 1, "
            + "got: " + q.size());
      } else {
        TestUtil.logTrace("Expected size received, verify contents . . . ");

        for (Object obj : q) {
          // each element in the query result list should be an Object[], which
          // has exactly 2 elements. The first element is of type Order1, and
          // the
          // second of type String.
          if (obj instanceof Object[]) {
            Object[] objectArray = (Object[]) obj;

            for (Object o : objectArray) {

              if (o instanceof Order1) {
                Order1 orderReturned = (Order1) o;

                if (!orderReturned.equals(o7)) {
                  TestUtil
                      .logErr("Expected:" + o7 + ", actual:" + orderReturned);
                } else {
                  TestUtil.logTrace("Received expected order");
                  passCounter++;
                }
                if (orderReturned.getItem() != i1) {
                  TestUtil.logErr(
                      "Expected:" + i1 + ", actual:" + orderReturned.getItem());
                } else {
                  TestUtil.logTrace("Received expected item");
                  passCounter++;
                }
              } else if (o instanceof String) {

                String itemName = (String) o;
                if (!itemName.equals(i1.getItemName())) {
                  TestUtil.logErr(
                      "Expected:" + i1.getItemName() + ", actual:" + itemName);
                } else {
                  TestUtil.logTrace("Received expected itemName");
                  passCounter++;
                }

              } else {
                TestUtil.logErr("Received unexpected object:" + o);
              }
            }

          }
        }
      }
      if (passCounter == 3) {
        pass = true;
      }

      getEntityTransaction().commit();

    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }

    if (!pass)
      throw new Fault("nativeQueryColumnResultTypeTest failed");
  }

  /*
   * @testName: nativeQueryTestConstructorResult
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1512; PERSISTENCE:JAVADOC:1513;
   * PERSISTENCE:SPEC:1951; PERSISTENCE:SPEC:1952; PERSISTENCE:SPEC:1953;
   * PERSISTENCE:SPEC:1955;
   * 
   * @test_Strategy: Verify javax.persistence.ConstructorResult can call a class
   * constructor passing in arg data and return the class.
   */
  public void nativeQueryTestConstructorResult() throws Fault {

    boolean pass = false;
    List<Order2> q;

    try {
      getEntityTransaction().begin();

      TestUtil.logTrace("Create Items");
      final Item i1 = new Item(7, "Loafers");
      final Item i2 = new Item(8, "High Heels");
      final Item i3 = new Item(9, "Socks");

      TestUtil.logTrace("Create Orders");

      Order1 o7 = new Order1(7, 25.0D);
      o7.setItem(i1);
      getEntityManager().persist(o7);
      Order1 o8 = new Order1(8, 125.0D);
      o8.setItem(i2);
      getEntityManager().persist(o8);
      Order1 o9 = new Order1(9, 150.0D);
      o9.setItem(i3);
      getEntityManager().persist(o9);

      TestUtil.logTrace("Execute Query");
      q = getEntityManager().createNativeQuery(
          "Select o.\"ID\" AS \"OID\", o.\"TOTALPRICE\" AS \"OPRICE\", "
              + "i.\"ITEMNAME\" AS \"OITEMNAME\" from \"ORDER1\" o, \"ITEM\" i "
              + "WHERE (o.\"TOTALPRICE\" < 100) AND (o.\"FK1_FOR_ITEM\" = i.\"ID\")",
          "Order5ItemResults").getResultList();

      if (q.size() != 1) {
        TestUtil.logErr(
            "Did not get expected results.  Expected 1 references, got: "
                + q.size());
      } else {
        TestUtil.logTrace("Expected size received, verify contents . . . ");
        Order2 o = q.get(0);
        if (o.getId() == o7.getId()) {
          if (o.getTotalPrice() == o7.getTotalPrice()) {
            if (o.getItemName().equals(o7.getItem().getItemName())) {
              pass = true;
            } else {
              TestUtil.logErr("Expected:" + o7.getItem().getItemName()
                  + ", actual:" + o.getItemName());
            }
          } else {
            TestUtil.logErr("Expected:" + o7.getTotalPrice() + ", actual:"
                + o.getTotalPrice());
          }
        } else {
          TestUtil.logErr("Expected:" + o7.getId() + ", actual:" + o.getId());
        }
      }

      getEntityTransaction().commit();

    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }

    if (!pass)
      throw new Fault("nativeQueryTestConstructorResult failed");

  }
  /*
   * @testName: nativeQueryTestConstructorResultWithId
   * 
   * @assertion_ids: PERSISTENCE:SPEC:1557;
   * 
   * @test_Strategy: Verify javax.persistence.ConstructorResult can call a class
   * constructor passing in arg data and return the class.
   *
   */

  public void nativeQueryTestConstructorResultWithId() throws Fault {

    boolean pass1 = false;
    boolean pass2 = false;
    List<PurchaseOrder> q;

    try {
      getEntityTransaction().begin();

      TestUtil.logTrace("Create Purchase Orders");

      PurchaseOrder p1 = new PurchaseOrder(7, 25);
      getEntityManager().persist(p1);
      PurchaseOrder p2 = new PurchaseOrder(8, 50);
      getEntityManager().persist(p2);
      PurchaseOrder p3 = new PurchaseOrder(9, 75);
      getEntityManager().persist(p3);

      TestUtil.logTrace("Execute Query");
      q = getEntityManager().createNativeQuery(
          "Select o.\"ID\" AS \"OID\", o.\"TOTAL\" AS \"PTOTAL\" from \"PURCHASE_ORDER\" o "
              + "WHERE (o.\"TOTAL\" < 50)",
          "PurchaseOrder1Results").getResultList();

      if (q.size() != 1) {
        TestUtil.logErr(
            "Did not get expected results.  Expected 1 references, got: "
                + q.size());
      } else {
        TestUtil.logTrace("Expected size received, verify contents . . . ");
        PurchaseOrder p = q.get(0);
        if (p.getId() == p1.getId()) {
          if (p.getTotal() == p1.getTotal()) {
            pass1 = true;
          } else {
            TestUtil.logErr(
                "Expected:" + p1.getTotal() + ", actual:" + p.getTotal());
          }
        } else {
          TestUtil.logErr("Expected: " + p1.getId() + ", actual:" + p.getId());
        }
        if (!getEntityManager().contains(p)) {
          TestUtil.logMsg("Entity contains id and is managed.");
          pass2 = true;
        } else {
          TestUtil.logErr("Entity contains an id and is not managed.");
        }
      }

      getEntityTransaction().commit();

    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }

    if (!pass1 || !pass2)
      throw new Fault("nativeQueryTestConstructorResultWithId failed");

  }

  /*
   * @testName: nativeQueryTestConstructorResultNoId
   * 
   * @assertion_ids: PERSISTENCE:SPEC:1557;
   * 
   * @test_Strategy: Verify javax.persistence.ConstructorResult can call a class
   * constructor passing in arg data except the id and return the class.
   *
   */

  public void nativeQueryTestConstructorResultNoId() throws Fault {

    boolean pass1 = false;
    boolean pass2 = false;
    List<PurchaseOrder> q;

    try {
      getEntityTransaction().begin();

      TestUtil.logTrace("Create Purchase Orders");

      PurchaseOrder p1 = new PurchaseOrder(7, 25);
      getEntityManager().persist(p1);
      PurchaseOrder p2 = new PurchaseOrder(8, 50);
      getEntityManager().persist(p2);
      PurchaseOrder p3 = new PurchaseOrder(9, 75);
      getEntityManager().persist(p3);

      TestUtil.logTrace("Execute Query");
      q = getEntityManager()
          .createNativeQuery(
              "Select o.\"TOTAL\" AS \"PTOTAL\" from \"PURCHASE_ORDER\" o "
                  + "WHERE (o.\"TOTAL\" < 50)",
              "PurchaseOrder2Results")
          .getResultList();

      if (q.size() != 1) {
        TestUtil.logErr(
            "Did not get expected results.  Expected 1 references, got: "
                + q.size());
      } else {
        TestUtil.logTrace("Expected size received, verify contents . . . ");
        PurchaseOrder p = q.get(0);
        if (p.getId() == 0) {
          if (p.getTotal() == p1.getTotal()) {
            pass1 = true;
          } else {
            TestUtil.logErr(
                "Expected:" + p1.getTotal() + ", actual:" + p.getTotal());
          }
        } else {
          TestUtil.logErr("Expected: 0, actual:" + p.getId());
        }
        if (!getEntityManager().contains(p)) {
          TestUtil.logMsg("Entity does not contain id and is detached.");
          pass2 = true;
        } else {
          TestUtil.logErr("Entity does not contain an id and is not detached.");
        }
      }

      getEntityTransaction().commit();

    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }

    if (!pass1 || !pass2)
      throw new Fault("nativeQueryTestConstructorResultNoId failed");

  }

  /*
   * @testName: createNativeQueryStringTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:39; PERSISTENCE:SPEC:1511;
   * 
   * @test_Strategy:
   *
   */

  public void createNativeQueryStringTest() throws Fault {
    boolean pass = false;
    List q = null;
    List<Integer> expected = new ArrayList<Integer>();
    expected.add(2);

    try {
      getEntityTransaction().begin();

      TestUtil.logTrace("Create Items");
      final Item i1 = new Item(1, "Boots");
      final Item i2 = new Item(2, "Sneakers");
      final Item i3 = new Item(3, "Slippers");

      TestUtil.logTrace("Create Orders");
      Order1 o1 = new Order1(1, 25.0D);
      o1.setItem(i1);
      getEntityManager().persist(o1);
      Order1 o2 = new Order1(2, 125.0D);
      o2.setItem(i2);
      getEntityManager().persist(o2);
      Order1 o3 = new Order1(3, 150.0D);
      o3.setItem(i3);
      getEntityManager().persist(o3);

      TestUtil.logTrace("Execute Query");
      q = getEntityManager()
          .createNativeQuery(
              "Select o.\"ID\" from \"ORDER1\" o WHERE o.\"ID\" = 2 ")
          .getResultList();

      List<Integer> actual = new ArrayList<Integer>();
      for (Object o : q) {
        actual.add(convertToInt(o));
      }
      if (expected.containsAll(actual) && actual.containsAll(expected)
          && expected.size() == actual.size()) {
        TestUtil.logTrace("Received expected results");
        pass = true;
      } else {
        TestUtil.logErr("Did not get expected results");
        for (Integer i : expected) {
          TestUtil.logErr("expected:" + i);
        }
        for (Integer i : actual) {
          TestUtil.logErr("actual:" + i);
        }
      }

      getEntityTransaction().commit();

    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }

    if (!pass)
      throw new Fault("createNativeQueryStringTest failed");
  }

  /*
   * @testName: createNativeQueryResultClassTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:40
   * 
   * @test_Strategy: Use the createNativeQuery(String,Class)
   *
   */

  public void createNativeQueryResultClassTest() throws Fault {
    boolean pass = false;
    List<Order1> q;

    try {
      getEntityTransaction().begin();

      TestUtil.logTrace("Create Items");
      final Item i1 = new Item(1, "Boots");
      final Item i2 = new Item(2, "Sneakers");
      final Item i3 = new Item(3, "Slippers");

      TestUtil.logTrace("Create Orders");
      Order1 o1 = new Order1(1, 25.0D);
      o1.setItem(i1);
      getEntityManager().persist(o1);
      Order1 o2 = new Order1(2, 125.0D);
      o2.setItem(i2);
      getEntityManager().persist(o2);
      Order1 o3 = new Order1(3, 150.0D);
      o3.setItem(i3);
      getEntityManager().persist(o3);

      TestUtil.logTrace("Execute Query");
      q = getEntityManager()
          .createNativeQuery("Select * from \"ORDER1\" o WHERE o.\"ID\" = 2 ",
              Order1.class)
          .getResultList();

      if (q.size() != 1) {
        TestUtil.logErr(
            " Did not get expected results.  Expected: 1, got: " + q.size());
      } else {
        int id = q.get(0).getId();
        if (id != 2) {
          TestUtil.logErr("id expected:2, actual:" + id);

        } else {
          pass = true;
        }

      }

      getEntityTransaction().commit();

    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }

    if (!pass)
      throw new

      Fault("createNativeQueryResultClassTest failed");

  }

  /*
   * @testName: createNativeQueryResultClassTQTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:320
   * 
   * @test_Strategy: Use the createNativeQuery(String,Class) a TypedQuery
   *
   */
  public void createNativeQueryResultClassTQTest() throws Fault {
    boolean pass = false;
    List<Order1> q;

    try {
      getEntityTransaction().begin();

      TestUtil.logTrace("Create Items");
      Item i1 = new Item(1, "Boots");
      Item i2 = new Item(2, "Sneakers");
      Item i3 = new Item(3, "Slippers");

      TestUtil.logTrace("Create Orders");
      Order1 o1 = new Order1(1, 25.0D);
      o1.setItem(i1);
      getEntityManager().persist(o1);
      Order1 o2 = new Order1(2, 125.0D);
      o2.setItem(i2);
      getEntityManager().persist(o2);
      Order1 o3 = new Order1(3, 150.0D);
      o3.setItem(i3);
      getEntityManager().persist(o3);

      TestUtil.logTrace("Execute Query");
      q = getEntityManager()
          .createNativeQuery("Select * from \"ORDER1\" o WHERE o.\"ID\" = 2 ",
              Order1.class)
          .getResultList();

      if (q.size() != 1) {
        TestUtil.logErr(
            " Did not get expected results.  Expected: 1, got: " + q.size());
      } else {
        int id = q.get(0).getId();
        if (id != 2) {
          TestUtil.logErr("id expected:2, actual:" + id);

        } else {
          pass = true;
        }

      }

      getEntityTransaction().commit();

    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }

    if (!pass)
      throw new Fault("createNativeQueryResultClassTQTest failed");
  }

  /*
   * @testName: executeUpdateTransactionRequiredExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:552
   * 
   * @test_Strategy: Use the createNativeQuery(String).executeUpdate() when no
   * transaction is in effect and verify TransactionRequiredException is thrown
   *
   */
  public void executeUpdateTransactionRequiredExceptionTest() throws Fault {
    boolean pass = false;
    try {
      getEntityManager().createNativeQuery("Delete from \"ORDER1\"")
          .executeUpdate();
      TestUtil.logErr("TransactionRequiredException was not thrown");
    } catch (TransactionRequiredException ise) {
      TestUtil.logTrace("Received expected TransactionRequiredException");
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception re) {
        TestUtil.logErr("Unexpected Exception in rollback:", re);
      }
    }
    if (!pass)
      throw new Fault("executeUpdateTransactionRequiredExceptionTest failed");
  }

  /*
   * @testName: setParameterTest
   * 
   * @assertion_ids: PERSISTENCE:SPEC:1308; PERSISTENCE:SPEC:1309;
   * 
   * @test_Strategy: Use the createNativeQuery(String,Class) a TypedQuery
   *
   */
  public void setParameterTest() throws Fault {
    boolean pass = false;
    List<Order1> q;

    try {
      getEntityTransaction().begin();

      TestUtil.logTrace("Create Items");
      Item i1 = new Item(1, "Boots");
      Item i2 = new Item(2, "Sneakers");
      Item i3 = new Item(3, "Slippers");

      TestUtil.logTrace("Create Orders");
      Order1 o1 = new Order1(1, 25.0D);
      o1.setItem(i1);
      getEntityManager().persist(o1);
      Order1 o2 = new Order1(2, 125.0D);
      o2.setItem(i2);
      getEntityManager().persist(o2);
      Order1 o3 = new Order1(3, 150.0D);
      o3.setItem(i3);
      getEntityManager().persist(o3);

      TestUtil.logTrace("Execute Query");
      q = getEntityManager()
          .createNativeQuery("Select * from \"ORDER1\" o WHERE o.\"ID\" = ? ",
              Order1.class)
          .setParameter(1, 2).getResultList();

      if (q.size() != 1) {
        TestUtil.logErr(
            " Did not get expected results.  Expected: 1, got: " + q.size());
      } else {
        int id = q.get(0).getId();
        if (id != 2) {
          TestUtil.logErr("id expected:2, actual:" + id);

        } else {
          TestUtil.logTrace("Received expected result");
          pass = true;
        }

      }

      getEntityTransaction().commit();

    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }

    if (!pass)
      throw new Fault("setParameterTest failed");
  }

  /*
   * @testName: getSingleResultTest
   * 
   * @assertion_ids: PERSISTENCE:SPEC:1512;
   * 
   * @test_Strategy:
   *
   */
  public void getSingleResultTest() throws Fault {
    boolean pass = false;
    Object q;

    try {
      getEntityTransaction().begin();

      TestUtil.logTrace("Create Items");
      Item i1 = new Item(1, "Boots");
      Item i2 = new Item(2, "Sneakers");
      Item i3 = new Item(3, "Slippers");

      TestUtil.logTrace("Create Orders");
      Order1 o1 = new Order1(1, 25.0D);
      o1.setItem(i1);
      getEntityManager().persist(o1);
      Order1 o2 = new Order1(2, 125.0D);
      o2.setItem(i2);
      getEntityManager().persist(o2);
      Order1 o3 = new Order1(3, 150.0D);
      o3.setItem(i3);
      getEntityManager().persist(o3);

      TestUtil.logTrace("Execute Query");
      q = getEntityManager()
          .createNativeQuery("Select COUNT(*) from \"ORDER1\" o")
          .getSingleResult();
      int i = 0;
      i = convertToInt(q);
      if (i == 3) {
        TestUtil.logTrace("Received expected result:" + i);
        pass = true;
      } else {
        TestUtil.logErr("Expected: 3, actual:" + i);
      }

      getEntityTransaction().commit();

    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }

    if (!pass)
      throw new Fault("getSingleResultTest failed");
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
      getEntityManager().createNativeQuery("Delete FROM ORDER2")
          .executeUpdate();
      getEntityManager().createNativeQuery("Delete FROM \"ORDER1\"")
          .executeUpdate();
      getEntityManager().createNativeQuery("Delete FROM \"ITEM\"")
          .executeUpdate();
      getEntityManager().createNativeQuery("Delete FROM \"PURCHASE_ORDER\"")
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
