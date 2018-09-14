/*
 * Copyright (c) 2013, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.el.spec.collectionoperators;

import java.lang.reflect.Array;
import java.util.Properties;

import javax.el.ELProcessor;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.ServiceEETest;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.el.common.util.DataBase;
import com.sun.ts.tests.el.common.util.ELTestUtil;

public class ELClient extends ServiceEETest {

  private ELProcessor elp;

  private DataBase database;

  Properties testProps;

  public static void main(String[] args) {
    ELClient theTests = new ELClient();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  public void setup(String[] args, Properties p) throws Fault {
    TestUtil.logTrace("Setup method called");
    this.testProps = p;
  }

  public void cleanup() throws Fault {
    // does nothing at this point
  }

  /**
   * @testName: elCollectionMapFilterTest
   * 
   * @assertion_ids: EL:SPEC:53; EL:SPEC:53.1; EL:SPEC:53.2; EL:SPEC:53.3;
   *                 EL:SPEC:59; EL:SPEC:59.1; EL:SPEC:58.1; EL:SPEC:58.2
   * 
   * @test_Strategy: Test to make sure the 'map'& "filter' operator works
   *                 properly.
   * 
   * @since: 3.0
   * 
   */
  public void elCollectionMapFilterTest() throws Fault {
    buildELContext();

    String[] expr1 = { "[Toy Story, 10.0]", "[History of Golf, 11.0]" };

    String[] expr2 = { "20", "30", "40" };

    testIterable("map-filter-one",
        "products.stream().filter(p->p.unitPrice >= 10 && "
            + "p.unitPrice < 12).sorted("
            + "(p,q)->p.unitPrice-q.unitPrice).map("
            + "p->[p.name,p.unitPrice]).toList()",
        expr1);

    testIterable("map-filter-two",
        "[1,2,3,4].stream().filter(i->i > 1).map(i->i*10).toList()", expr2);

  } // End elCollectionMapFilterTest

  /**
   * @testName: elCollectionMinTest
   * 
   * @assertion_ids: EL:SPEC:72; EL:SPEC:72.1; EL:SPEC:72.2
   * 
   * @test_Strategy: Test to make sure the 'min' operator works properly.
   * 
   * @since: 3.0
   * 
   */
  public void elCollectionMinTest() throws Fault {
    buildELContext();

    testStream("min", "[2,3,1,5].stream().min().get()", Long.valueOf(1));
    elp.eval("comparing = map->(x,y)->map(x).compareTo(map(y))");
    testStream("min", "[3,2,1].stream().min((i,j)->i-j).get()",
        Long.valueOf(1));
    testStream("min",
        "customers.stream().min((x,y)->x.orders.size()-y.orders.size()).get().name",
        "Charlie Yeh");
    elp.eval("comparing = map->(x,y)->map(x).compareTo(map(y))");
    testStream("min",
        "customers.stream().min(comparing(c->c.orders.size())).get().name",
        "Charlie Yeh");

  } // End elCollectionMinTest()

  /**
   * @testName: elCollectionMaxTest
   * 
   * @assertion_ids: EL:SPEC:71; EL:SPEC:71.1; EL:SPEC:71.2
   * 
   * @test_Strategy: Test to make sure the 'max' operator works properly.
   * 
   * @since: 3.0
   * 
   */
  public void elCollectionMaxTest() throws Fault {
    buildELContext();

    testStream("max", "[2,3,1,5].stream().max().get()", Long.valueOf(5));
    testStream("max", "['xy', 'xyz', 'abc'].stream().max().get()", "xyz");
    testStream("max", "[2].stream().max((i,j)->i-j).get()", Long.valueOf(2));
    elp.eval("comparing = map->(x,y)->map(x).compareTo(map(y))");
    testStream("max",
        "customers.stream().max((x,y)->x.orders.size()-y.orders.size()).get().name",
        "John Doe");
    testStream("max",
        "customers.stream().max(comparing(c->c.orders.size())).get().name",
        "John Doe");

  } // End elCollectionMaxTest()

  /**
   * @testName: elCollectionDistinctTest
   * 
   * @assertion_ids: EL:SPEC:61; EL:SPEC:61.1
   * 
   * @test_Strategy: Test to make sure the 'distinct' operator works properly.
   * 
   * @since: 3.0
   * 
   */
  public void elCollectionDistinctTest() throws Fault {
    buildELContext();

    this.testIterable("distinct",
        " ['a', 'b', 'b', 'c'].stream().distinct().toList()",
        new String[] { "a", "b", "c" });

  } // End elCollectionDistinctTest()

  /**
   * @testName: elCollectionForEachTest
   * 
   * @assertion_ids: EL:SPEC:63; EL:SPEC:63.1
   * 
   * @test_Strategy: Test to make sure the 'forEach' operator works properly.
   * 
   * @since: 3.0
   * 
   */
  public void elCollectionForEachTest() throws Fault {
    buildELContext();

    String expr1[] = { "Eagle", "Coming Home", "Greatest Hits",
        "History of Golf", "Toy Story", "iSee" };

    testIterable("forEach",
        "lst = []; products.stream().forEach(p->lst.add(p.name)); lst", expr1);

  } // End elCollectionForEachTest()

  /**
   * @testName: elCollectionFindFirstTest
   * 
   * @assertion_ids: EL:SPEC:78; EL:SPEC:78.1; EL:SPEC:62; EL:SPEC:62.1;
   *                 EL:SPEC:62.2
   * 
   * @test_Strategy: Test to make sure the 'findFirst' operator works properly.
   * 
   * @since: 3.0
   * 
   */
  public void elCollectionFindFirstTest() throws Fault {
    buildELContext();

    String[] expr1 = { "Product: 200, Eagle, book, 12.5, 100" };

    testIterable("findFirst",
        "products.stream().sorted(p->p.unitPrice).findFirst().get()", expr1);

  } // End elCollectionFindFirstTest()

  /**
   * @testName: elCollectionLimitTest
   * 
   * @assertion_ids: EL:SPEC:66; EL:SPEC:66.1
   * 
   * @test_Strategy: Test to make sure the 'limit' operator works properly.
   * 
   * @since: 3.0
   * 
   */
  public void elCollectionLimitTest() throws Fault {
    buildELContext();

    String[] expr1 = { "[Product: 202, Greatest Hits, cd, 6.5, 200]" };

    String[] expr2 = { "Product: 202, Greatest Hits, cd, 6.5, 200",
        "Product: 201, Coming Home, dvd, 8.0, 50", };

    testIterable("limit",
        "products.stream().sorted((p,q)->p.unitPrice-q.unitPrice)."
            + "limit(1).toList()",
        expr1);

    testIterable("limit",
        "products.stream().sorted((p,q)->p.unitPrice-q.unitPrice)."
            + "limit(2).toList()",
        expr2);

  } // End elCollectionLimitTest()

  /**
   * testName: elCollectionAnyMatchTest
   * 
   * @assertion_ids: EL:SPEC:75; EL:SPEC:75.1
   * 
   * @test_Strategy: Test to make sure the 'anyMatch' operator works properly.
   * 
   * @since: 3.0
   * 
   *         Bug 20438221 : Commented the test for now
   */
  public void elCollectionAnyMatchTest() throws Fault {
    buildELContext();

    testIterable("anyMatch_True",
        "products.stream().anyMatch(p->p.unitPrice >= 10)", true);

    testIterable("anyMatch_False",
        "products.stream().anyMatch(p->p.unitPrice == 100)", false);

  } // End elCollectionAnyMatchTest()

  /**
   * testName: elCollectionNoneMatchTest
   * 
   * @assertion_ids: EL:SPEC:77; EL:SPEC:77.1
   * 
   * @test_Strategy: Test to make sure the 'noneMatch' operator works properly.
   * 
   * @since: 3.0
   * 
   *         Bug 20438221 : Commented the test for now
   */
  public void elCollectionNoneMatchTest() throws Fault {
    buildELContext();

    testIterable("noneMatch_False",
        "products.stream().noneMatch(p->p.unitPrice >= 10)", false);

    testIterable("noneMatch_True",
        "products.stream().noneMatch(p->p.unitPrice == 100)", true);

  } // End elCollectionNoneMatchTest()

  /**
   * testName: elCollectionAllMatchTest
   * 
   * @assertion_ids: EL:SPEC:76; EL:SPEC:76.1
   * 
   * @test_Strategy: Test to make sure the 'allMatch' operator works properly.
   * 
   * @since: 3.0
   * 
   *         Bug 20438221 : Commented the test for now.
   */
  public void elCollectionAllMatchTest() throws Fault {
    buildELContext();

    testIterable("allMatch_true",
        "products.stream().allMatch(p->p.unitPrice >= 1)", true);

    testIterable("allMatch_false",
        "products.stream().allMatch(p->p.unitPrice == 100)", false);

  } // End elCollectionAllMatchTest()

  /**
   * @testName: elCollectionSumTest
   * 
   * @assertion_ids: EL:SPEC:74; EL:SPEC:74.1
   * 
   * @test_Strategy: Test to make sure the 'sum' operator works properly.
   * 
   * @since: 3.0
   * 
   */
  public void elCollectionSumTest() throws Fault {
    buildELContext();

    testIterable("sum", "['10', '12', '13'].stream().sum()",
        new String[] { "35" });

    testIterable("sum_Empty", "[].stream().sum()", new String[] { "0" });

  } // End elCollectionSumTest()

  /**
   * @testName: elCollectionCountTest
   * 
   * @assertion_ids: EL:SPEC:79; EL:SPEC:79.1
   * 
   * @test_Strategy: Test to make sure the 'count' operator works properly.
   * 
   * @since: 3.0
   * 
   */
  public void elCollectionCountTest() throws Fault {
    buildELContext();

    testIterable("count", "[1,2,3,4,5].stream().count()", new String[] { "5" });

  } // End elCollectionCountTest()

  /**
   * @testName: elCollectionAverageTest
   * 
   * @assertion_ids: EL:SPEC:73; EL:SPEC:73.1
   * 
   * @test_Strategy: Test to make sure the 'average' operator works properly.
   * 
   * @since: 3.0
   * 
   */
  public void elCollectionAverageTest() throws Fault {

    buildELContext();

    this.testStream("average_ints", "ints.stream().average().get()",
        Double.valueOf(4.5));

  } // End elCollectionAverageTest()

  /**
   * @testName: elCollectionToArrayTest
   * 
   * @assertion_ids: EL:SPEC:68; EL:SPEC:68.1
   * 
   * @test_Strategy: Test to make sure the 'toArray' operator works properly.
   * 
   * @since: 3.0
   * 
   */
  public void elCollectionToArrayTest() throws Fault {
    buildELContext();

    String[] expr1 = { "Product: 200, Eagle, book, 12.5, 100",
        "Product: 205, iSee, book, 12.5, 150",
        "Product: 203, History of Golf, book, 11.0, 30",
        "Product: 202, Greatest Hits, cd, 6.5, 200",
        "Product: 204, Toy Story, dvd, 10.0, 1000",
        "Product: 201, Coming Home, dvd, 8.0, 50" };

    this.testArray("toArray",
        "products.stream().sorted(p->p.unitPrice).toArray()", expr1);

  }// End elCollectionToArrayTest()

  /**
   * @testName: elCollectionReduceTest
   * 
   * @assertion_ids: EL:SPEC:70; EL:SPEC:70.1; EL:SPEC:70.2; EL:SPEC:56.2;
   *                 EL:SPEC:56.3
   * 
   * @test_Strategy: Test to make sure the 'reduce' operator works properly.
   * 
   * @since: 3.0
   * 
   */
  public void elCollectionReduceTest() throws Fault {
    buildELContext();

    testStream("reduce-one", "[1,2,3,4,5].stream().reduce(0, (l,r)->l+r)",
        Long.valueOf(15));
    testStream("reduce-two", "[1,2,3,4,5].stream().reduce((l,r)->l+r).get()",
        Long.valueOf(15));
    testStream("reduce-three", "[].stream().reduce((l,r)->l+r).orElse(101)",
        Long.valueOf(101));
    testStream("reduce-four",
        "[].stream().reduce((l,r)->l+r).orElseGet(()->101)", Long.valueOf(101));
    testStream("reduce-five",
        "c = 0; [1,2,3,4,5,6].stream().reduce(0, (l,r)->(c = c+1; "
            + "c % 2 == 0? l+r: l-r))",
        Long.valueOf(3));

  }// End elCollectionReduceTest()

  /**
   * @testName: elCollectionSubStreamTest
   * 
   * @assertion_ids: EL:SPEC:67; EL:SPEC:67.1; EL:SPEC:67.2
   * 
   * @test_Strategy: Test to make sure the 'substream' operator works properly.
   * 
   * @since: 3.0
   * 
   */
  public void elCollectionSubStreamTest() throws Fault {
    buildELContext();

    String[] expr1 = { "2", "3", "4" };

    testIterable("substream-one", "[0,1,2,3,4].stream().substream(2).toList()",
        expr1);
    testIterable("substream-two",
        "[0,1,2,3,4,5,6].stream().substream(2,5).toList()", expr1);

  }// End elCollectionSubStreamTest()

  /**
   * @testName: elCollectionPeekTest
   * 
   * @assertion_ids: EL:SPEC:4; EL:SPEC:64.1
   * 
   * @test_Strategy: Test to make sure the 'peek' operator works properly.
   * 
   * @since: 3.0
   * 
   */
  public void elCollectionPeekTest() throws Fault {
    buildELContext();

    String expr1[] = { "1", "2", "3", "4" };

    testIterable("peek-one",
        "lst = []; [1,2,3,4].stream().peek(i->lst.add(i)).toList()", expr1);
    testIterable("peek-two", "lst.stream().toList()", expr1);

  }// End elCollectionPeekTest()

  /**
   * @testName: elCollectionFlatMapTest
   * 
   * @assertion_ids: EL:SPEC:60; EL:SPEC:60.1
   * 
   * @test_Strategy: Test to make sure the 'flatMap' operator works properly.
   * 
   * @since: 3.0
   * 
   */
  public void elCollectionFlatMapTest() throws Fault {
    buildELContext();

    String[] expr1 = { "Order: 10, 100, 2/18/2010, 20.8",
        "Order: 11, 100, 5/3/2011, 34.5", "Order: 12, 100, 8/2/2011, 210.75",
        "Order: 13, 101, 1/15/2011, 50.23",
        "Order: 14, 101, 1/3/2012, 126.77" };

    testIterable("flatMap",
        "customers.stream().filter(c->c.country=='USA').flatMap("
            + "c->c.orders.stream()).toList()",
        expr1);

  }// End elCollectionFlatMapTest()

  // --------------------------- private methods

  private void logLine(String s) {
    TestUtil.logMsg(s);
  }

  private void buildELContext() {
    elp = new ELProcessor();
    database = new DataBase();

    database.init();
    elp.defineBean("customers", database.getCustomers());
    elp.defineBean("products", database.getProducts());
    elp.defineBean("orders", database.getOrders());
    elp.defineBean("ints", database.getInts());
  }

  /**
   * Test a Collection query that returns an Iterable.
   * 
   * @param name
   *          The Name of the test
   * @param query
   *          The EL query string
   * @param expected
   *          The expected result of the Iterable. The array element should
   *          equal the Iterable element, when enumerated.
   */
  private void testIterable(String name, String query, String[] expected)
      throws Fault {
    String result;
    String golden;
    int explength = 0;

    if (expected != null) {
      explength = expected.length;
    }

    logLine("=== Testing " + name + " ===");
    logLine(query);
    Object ret = elp.eval(query);
    int indx = 0;
    logLine(" = returns =");

    if (expected == null) {

      if (!(ret == null)) {
        throw new Fault("TEST FAILED, Unexpected Value!" + ELTestUtil.NL
            + "Expected: null");

      } else {
        logLine(" null ");
      }

    } else if (expected.length == 1) {
      golden = expected[0];
      result = ret.toString();
      logLine(" " + result);

      if (!golden.equals(result)) {
        throw new Fault("TEST FAILED, Unexpected Value!" + ELTestUtil.NL
            + "Expected: " + golden + ELTestUtil.NL + "Received: " + result);
      }

    } else {
      for (Object item : (Iterable<?>) ret) {
        result = item.toString();
        golden = expected[indx++];
        logLine(" " + result);

        if (!golden.equals(result)) {
          throw new Fault("TEST FAILED, Unexpected Value!" + ELTestUtil.NL
              + "Expected: " + golden + ELTestUtil.NL + "Received: " + result);
        }
      }

      if (indx != explength) {
        throw new Fault(ELTestUtil.FAIL + " lenght incorrect!" + ELTestUtil.NL
            + "Expected: " + explength + ELTestUtil.NL + "Found: " + indx);
      }
    }
  }

  /**
   * 
   * @param name
   *          The Name of the test.
   * 
   * @param query
   *          The EL query string
   * @param expected
   *          The expected boolean value for the given query.
   * 
   * @throws Fault
   */
  private void testIterable(String name, String query, Boolean expected)
      throws Fault {

    this.testIterable(name, query, new String[] { expected.toString() });
  }

  /**
   * Test a Collection query that returns a Array
   * 
   * @param name
   *          The Name of the test
   * @param query
   *          The EL query string
   * @param expected
   *          The expected result of the Array. The element of the expected
   *          array should equals the resulted array.
   */
  private void testArray(String name, String query, String[] expected)
      throws Fault {
    logLine("=== Test " + name + "===");
    logLine(query);

    Object arry = elp.eval(query);
    String item;
    String golden;

    int gldLength = Array.getLength(arry);
    logLine(" = returns =");

    if (gldLength != expected.length) {
      throw new Fault(
          ELTestUtil.FAIL + " lenght incorrect!" + ELTestUtil.NL + "Expected: "
              + expected.length + ELTestUtil.NL + "Found: " + gldLength);
    }

    for (int indx = 0; gldLength < indx; indx++) {
      item = (String) Array.get(arry, indx);
      golden = expected[indx];

      if (item.equals(golden)) {
        logLine(" " + item);

      } else {
        throw new Fault(ELTestUtil.FAIL + ELTestUtil.NL + "Expected: " + item
            + ELTestUtil.NL + "Received: " + golden);
      }
    }

  }

  private void testStream(String name, String query, Object exp_value)
      throws Fault {

    logLine("=== Testing " + name + " ===");

    Object result = elp.eval(query);
    logLine("EL Quuery String: '" + query + "' -returns: " + result + "("
        + result.getClass() + ")");

    // Test expected value.
    if (!exp_value.equals(result)) {
      throw new Fault(ELTestUtil.FAIL + ELTestUtil.NL + "Expected: " + exp_value
          + ELTestUtil.NL + "Received: " + result);
    }
  }
}
