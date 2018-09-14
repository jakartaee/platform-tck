/*
 * Copyright (c) 2012, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jaxrs.api.rs.core.multivaluedhashmap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.AbstractMultivaluedMap;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

import com.sun.ts.tests.jaxrs.common.JAXRSCommonClient;

/*
 * @class.setup_props: webServerHost;
 *                     webServerPort;
 *                     ts_home;
 */
public class JAXRSClient extends JAXRSCommonClient {

  private static final long serialVersionUID = -4827869994556677693L;

  /**
   * Entry point for different-VM execution. It should delegate to method
   * run(String[], PrintWriter, PrintWriter), and this method should not contain
   * any test configuration.
   */
  public static void main(String[] args) {
    new JAXRSClient().run(args);
  }

  /* Run test */

  /*
   * @testName: defaultConstructorTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:827;
   * 
   * @test_Strategy: Constructs an empty multivalued hash map with the default
   * initial capacity (16) and the default load factor (0.75)
   */
  public void defaultConstructorTest() throws Fault {
    MultivaluedHashMap<String, String> map = new MultivaluedHashMap<String, String>();
    map.addAll("key", VALUES[0], VALUES[1], VALUES[2]);
    assertContainsDefaultValues(map);
    assertNotContains(map, "key", VALUES[3], VALUES[4]);
  }

  /*
   * @testName: constructorWithInitialCapacityTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:828;
   * 
   * @test_Strategy: Constructs an empty multivalued hash map with the specified
   * initial capacity and the default load factor (0.75)
   */
  public void constructorWithInitialCapacityTest() throws Fault {
    MultivaluedHashMap<String, String> map = new MultivaluedHashMap<String, String>(
        0);
    map.addAll("key", VALUES[0], VALUES[1], VALUES[2]);
    assertContainsDefaultValues(map);
    assertNotContains(map, "key", VALUES[3], VALUES[4]);
  }

  /*
   * @testName: constructorWithInitialCapacityThrowsIllegalArgumentExceptionTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:828;
   * 
   * @test_Strategy: IllegalArgumentException - if the initial capacity is
   * negative.
   */
  public void constructorWithInitialCapacityThrowsIllegalArgumentExceptionTest()
      throws Fault {
    try {
      new MultivaluedHashMap<String, String>(-1);
      throw new Fault("IllegalArgumentException has not been thrown");
    } catch (IllegalArgumentException e) {
      logMsg("IllegalArgumentException has been thrown as expected");
    }
  }

  /*
   * @testName: constructorWithCapacityAndLoadTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:829;
   * 
   * @test_Strategy: Constructs an empty multivalued hash map with the specified
   * initial capacity and load factor
   */
  public void constructorWithCapacityAndLoadTest() throws Fault {
    MultivaluedHashMap<String, String> map = new MultivaluedHashMap<String, String>(
        0, 0.99f);
    map.addAll("key", VALUES[0], VALUES[1], VALUES[2]);
    assertContainsDefaultValues(map);
    assertNotContains(map, "key", VALUES[3], VALUES[4]);
  }

  /*
   * @testName: constructorWithCapacityAndLoadThrowsExceptionForCapacityTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:829;
   * 
   * @test_Strategy: IllegalArgumentException - if the initial capacity is
   * negative or the load factor is nonpositive
   */
  public void constructorWithCapacityAndLoadThrowsExceptionForCapacityTest()
      throws Fault {
    try {
      new MultivaluedHashMap<String, String>(Integer.MIN_VALUE, 0.99f);
      throw new Fault("IllegalArgumentException has not been thrown");
    } catch (IllegalArgumentException e) {
      logMsg("IllegalArgumentException has been thrown as expected");
    }
  }

  /*
   * @testName: constructorWithCapacityAndLoadThrowsExceptionForLoadTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:829;
   * 
   * @test_Strategy: IllegalArgumentException - if the initial capacity is
   * negative or the load factor is nonpositive
   */
  public void constructorWithCapacityAndLoadThrowsExceptionForLoadTest()
      throws Fault {
    try {
      new MultivaluedHashMap<String, String>(16, 0f);
      throw new Fault("IllegalArgumentException has not been thrown");
    } catch (IllegalArgumentException e) {
      logMsg("IllegalArgumentException has been thrown as expected");
    }
  }

  /*
   * @testName: constructorWithMapTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:830;
   * 
   * @test_Strategy: Constructs a new multivalued hash map with the same
   * mappings as the specified MultivaluedMap. The List instances holding the
   * values of each key are created anew instead of being reused.
   */
  public void constructorWithMapTest() throws Fault {
    MultivaluedHashMap<String, String> mapWithMap;
    MultivaluedHashMap<String, String> map;

    map = new MultivaluedHashMap<String, String>();
    map.addAll("key", VALUES[0], VALUES[1], VALUES[2]);

    mapWithMap = new MultivaluedHashMap<String, String>(map);
    assertContainsDefaultValues(mapWithMap);

    mapWithMap.clear(); // clear map, but check the original is untouched
    assertContainsDefaultValues(map);

    mapWithMap = new MultivaluedHashMap<String, String>(map);
    map.clear(); // clear original, but check the new is untouched
    assertContainsDefaultValues(mapWithMap);
  }

  /*
   * @testName: constructorWithMapThrowsNPETest
   * 
   * @assertion_ids: JAXRS:JAVADOC:830;
   * 
   * @test_Strategy: NullPointerException - if the specified map is null
   */
  public void constructorWithMapThrowsNPETest() throws Fault {
    try {
      new MultivaluedHashMap<String, String>(
          (MultivaluedMap<String, String>) null);
      throw new Fault("NullPointerException has NOT been thrown");
    } catch (NullPointerException e) {
      logMsg("NullPointerException has been thrown as expected");
    }
  }

  /*
   * @testName: constructorWithSingleValuedMapTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:831;
   * 
   * @test_Strategy: Constructs a new multivalued hash map with the same
   * mappings as the specified single-valued Map.
   */
  public void constructorWithSingleValuedMapTest() throws Fault {
    MultivaluedHashMap<String, String> mapWithMap;
    Map<String, String> map;

    map = new HashMap<String, String>();
    map.put("key", VALUES[0]);

    mapWithMap = new MultivaluedHashMap<String, String>(map);
    assertContains(mapWithMap, "key", VALUES[0]);

    mapWithMap.clear(); // clear map, but check the original is untouched
    assertFault(map.size() == 1,
        "The original map is empty after MultivaluedHashMap#clear()");

    mapWithMap = new MultivaluedHashMap<String, String>(map);
    map.clear(); // clear original, but check the new is untouched
    assertContains(mapWithMap, "key", VALUES[0]);
  }

  /*
   * @testName: constructorWithSingleValuedMapThrowsNPETest
   * 
   * @assertion_ids: JAXRS:JAVADOC:831;
   * 
   * @test_Strategy: NullPointerException - if the specified map is null
   */
  public void constructorWithSingleValuedMapThrowsNPETest() throws Fault {
    try {
      new MultivaluedHashMap<String, String>((Map<String, String>) null);
      throw new Fault("NullPointerException has NOT been thrown");
    } catch (NullPointerException e) {
      logMsg("NullPointerException has been thrown as expected");
    }
  }

  // ////////////////////////////////////////////////////////////////////////
  static final String[] VALUES = { "value1", "value2", "value3", "value4",
      "value5" };

  protected static void assertContainsDefaultValues(
      AbstractMultivaluedMap<String, String> map) throws Fault {
    assertContains(map, "key", VALUES[0], VALUES[1], VALUES[2]);
  }

  protected static void assertContains(MultivaluedMap<String, String> map,
      String key, String... values) throws Fault {
    List<String> list = map.get(key);
    for (String item : values)
      assertFault(list.contains(item), "Given map does not contain value",
          item);
    logMsg("Found key", key, "with following values:");
    logMsg((Object[]) values);
  }

  protected static void assertNotContains(
      AbstractMultivaluedMap<String, String> map, String key, String... values)
      throws Fault {
    List<String> list = map.get(key);
    if (list != null)
      for (String item : values)
        assertFault(!list.contains(item), "Given map does contain value", item);
    logMsg("For given", key,
        "the map does not contain following values as expected:");
    logMsg((Object[]) values);
  }

}
