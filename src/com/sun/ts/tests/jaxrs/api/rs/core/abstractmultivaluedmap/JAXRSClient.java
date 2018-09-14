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

package com.sun.ts.tests.jaxrs.api.rs.core.abstractmultivaluedmap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.ws.rs.core.AbstractMultivaluedMap;
import javax.ws.rs.core.MultivaluedHashMap;

import com.sun.ts.tests.jaxrs.common.JAXRSCommonClient;

/*
 * @class.setup_props: webServerHost;
 *                     webServerPort;
 *                     ts_home;
 */
public class JAXRSClient extends JAXRSCommonClient {

  private static final long serialVersionUID = -6248642901050437887L;

  protected static final String KEY = "key";

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
   * @testName: constructorTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:726;
   * 
   * @test_Strategy: Initialize the backing store in the abstract parent
   * multivalued map implementation.
   */
  public void constructorTest() throws Fault {
    AbstractMultivaluedMap<String, Object> map = createMap();
    assertContainsDefaultValues(map);
  }

  /*
   * @testName: addTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:727; JAXRS:JAVADOC:96;
   * 
   * @test_Strategy: Add a value to the current list of values for the supplied
   * key. NOTE: This implementation ignores values; A supplied value of is
   * ignored and not added to the value list. Overriding implementations may
   * modify this behavior by redefining the #addNull method
   */
  public void addTest() throws Fault {
    AbstractMultivaluedMap<String, Object> map = createMap();
    map.add("key2", VALUES[3]);
    map.add("key2", VALUES[4]);
    assertContains(map, "key2", VALUES[3], VALUES[4]);
  }

  /*
   * @testName: addNullKeyTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:727; JAXRS:JAVADOC:96;
   * 
   * @test_Strategy: Add a value to the current list of values for the supplied
   * key. NOTE: This implementation ignores values; A supplied value of is
   * ignored and not added to the value list. Overriding implementations may
   * modify this behavior by redefining the #addNull method
   */
  public void addNullKeyTest() throws Fault {
    AbstractMultivaluedMap<String, Object> map = createMap();
    map.add(null, VALUES[3]);
    assertContains(map, null, VALUES[3]);
  }

  /*
   * @testName: addNullValuesTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:727; JAXRS:JAVADOC:96;
   * 
   * @test_Strategy: Add a value to the current list of values for the supplied
   * key. NOTE: This implementation ignores values; A supplied value of is
   * ignored and not added to the value list. Overriding implementations may
   * modify this behavior by redefining the #addNull method
   */
  public void addNullValuesTest() throws Fault {
    AbstractMultivaluedMap<String, Object> map = createMap();
    map.add("key2", null);
    map.add("key3", null);
    assertNotContains(map, "key2", (Object) null);
    assertNotContains(map, "key3", (Object) null);
  }

  /*
   * @testName: addAllValuesTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:728; JAXRS:JAVADOC:832;
   * 
   * @test_Strategy: Add multiple values to the current list of values for the
   * supplied key. If the supplied array of new values is empty, method returns
   * immediately.
   */
  public void addAllValuesTest() throws Fault {
    AbstractMultivaluedMap<String, Object> map = createMap();
    map.addAll(KEY, VALUES[3], VALUES[4]);
    assertContainsDefaultValues(map);
    assertContains(map, KEY, VALUES[3], VALUES[4]);
  }

  /*
   * @testName: addAllNullValuesTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:728; JAXRS:JAVADOC:832;
   * 
   * @test_Strategy: Add multiple values to the current list of values for the
   * supplied key. If the supplied array of new values is empty, method returns
   * immediately.
   */
  public void addAllNullValuesTest() throws Fault {
    AbstractMultivaluedMap<String, Object> map = createMap();
    map.addAll(KEY, VALUES[3], null);
    assertContainsDefaultValues(map);
    assertContains(map, KEY, VALUES[3]);
    assertNotContains(map, KEY, (Object) null);
  }

  /*
   * @testName: addAllEmptyValuesTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:728; JAXRS:JAVADOC:832;
   * 
   * @test_Strategy: Add multiple values to the current list of values for the
   * supplied key. If the supplied array of new values is empty, method returns
   * immediately.
   */
  public void addAllEmptyValuesTest() throws Fault {
    AbstractMultivaluedMap<String, Object> map = createMap();
    map.addAll("key2", new Object[] {});
    assertContainsDefaultValues(map);
    assertFault(!map.containsKey("key2"),
        "The method didn't returned immediatelly when empty array");
  }

  /*
   * @testName: addAllThrowsNPETest
   * 
   * @assertion_ids: JAXRS:JAVADOC:728; JAXRS:JAVADOC:832;
   * 
   * @test_Strategy: Method throws a NullPointerException if the supplied array
   * of values is null.
   */
  public void addAllThrowsNPETest() throws Fault {
    AbstractMultivaluedMap<String, Object> map = createMap();
    try {
      map.addAll("key2", (Object[]) null);
      throw new Fault("NullPointerException has not been thrown");
    } catch (NullPointerException e) {
      logMsg("NullPointerException has been thrown as expected", e);
    }
  }

  /*
   * @testName: addAllListTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:729; JAXRS:JAVADOC:833;
   * 
   * @test_Strategy: Add all the values from the supplied value list to the
   * current list of values for the supplied key. If the supplied value list is
   * empty, method returns immediately.
   */
  public void addAllListTest() throws Fault {
    AbstractMultivaluedMap<String, Object> map = createMap();
    List<Object> list = new ArrayList<Object>();
    list.add(VALUES[3]);
    list.add(VALUES[4]);
    map.addAll(KEY, list);
    assertContainsDefaultValues(map);
    assertContains(map, KEY, VALUES[3], VALUES[4]);
  }

  /*
   * @testName: addAllListNullValuesTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:729; JAXRS:JAVADOC:833;
   * 
   * @test_Strategy: Add all the values from the supplied value list to the
   * current list of values for the supplied key. If the supplied value list is
   * empty, method returns immediately.
   * 
   */
  public void addAllListNullValuesTest() throws Fault {
    AbstractMultivaluedMap<String, Object> map = createMap();
    List<Object> list = new ArrayList<Object>();
    list.add(VALUES[3]);
    list.add(null);
    map.addAll(KEY, list);
    assertContainsDefaultValues(map);
    assertContains(map, KEY, VALUES[3]);
    assertNotContains(map, KEY, (Object) null);
  }

  /*
   * @testName: addAllListEmptyValuesTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:729; JAXRS:JAVADOC:833;
   * 
   * @test_Strategy: Add all the values from the supplied value list to the
   * current list of values for the supplied key. If the supplied value list is
   * empty, method returns immediately.
   */
  public void addAllListEmptyValuesTest() throws Fault {
    AbstractMultivaluedMap<String, Object> map = createMap();
    map.addAll("key2", new ArrayList<Object>());
    assertContainsDefaultValues(map);
    assertFault(!map.containsKey("key2"),
        "The method didn't returned immediatelly when empty array");
  }

  /*
   * @testName: addAllListThrowsNPETest
   * 
   * @assertion_ids: JAXRS:JAVADOC:729; JAXRS:JAVADOC:833;
   * 
   * @test_Strategy: Throws: NullPointerException - if the supplied value list
   * is null.
   * 
   */
  public void addAllListThrowsNPETest() throws Fault {
    AbstractMultivaluedMap<String, Object> map = createMap();
    try {
      map.addAll("key2", (List<Object>) null);
      throw new Fault("NullPointerException has not been thrown");
    } catch (NullPointerException e) {
      logMsg("NullPointerException has been thrown as expected", e);
    }
  }

  /*
   * @testName: addFirstTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:730; JAXRS:JAVADOC:738; JAXRS:JAVADOC:834;
   * JAXRS:JAVADOC:97;
   * 
   * @test_Strategy: Add a value to the first position in the current list of
   * values for the supplied key.
   * 
   * A shortcut to get the first value of the supplied key.
   */
  public void addFirstTest() throws Fault {
    AbstractMultivaluedMap<String, Object> map = createMap();
    map.addFirst(KEY, VALUES[3]);
    Object first = map.getFirst(KEY);
    assertFault(first.equals(VALUES[3]), "First is unexpected item", first);
    logMsg("Found first item", first);
  }

  /*
   * @testName: addFirstNullTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:730; JAXRS:JAVADOC:834;
   * 
   * @test_Strategy: Add a value to the first position in the current list of
   * values for the supplied key.
   * 
   */
  public void addFirstNullTest() throws Fault {
    AbstractMultivaluedMap<String, Object> map = createMap();
    map.addFirst(KEY, null);
    assertContainsDefaultValues(map);
    assertNotContains(map, KEY, (Object) null);
  }

  /*
   * @testName: clearTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:731;
   * 
   * @test_Strategy: clears all items
   */
  public void clearTest() throws Fault {
    AbstractMultivaluedMap<String, Object> map = createMap();
    map.clear();
    assertNotContains(map, KEY, VALUES[0], VALUES[1], VALUES[2]);
  }

  /*
   * @testName: containsKeyTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:732;
   * 
   * @test_Strategy: informs about containing key
   */
  public void containsKeyTest() throws Fault {
    AbstractMultivaluedMap<String, Object> map = createMap();
    map.add(null, VALUES[3]);
    assertFault(map.containsKey(KEY), "#containsKey has not found given key");
    assertFault(map.containsKey(null), "#containsKey has not found NULL key");
    logMsg("#containsKey found non-null key and also the null key");
  }

  /*
   * @testName: containsValueTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:733;
   * 
   * @test_Strategy: informs about containing value
   */
  public void containsValueTest() throws Fault {
    AbstractMultivaluedMap<String, Object> map = createMap();
    List<Object> list = map.get(KEY);
    assertFault(map.containsValue(list), "#containsValue has not found", list);
    assertFault(!map.containsValue(VALUES[3]), "#containsValue has found",
        VALUES[3]);
    assertFault(!map.containsValue(KEY), "#containsValue has found key");
    logMsg("#containsValue() found list of items as expected");
  }

  /*
   * @testName: entrySetTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:734;
   * 
   * @test_Strategy: gets the entry Set
   */
  public void entrySetTest() throws Fault {
    AbstractMultivaluedMap<String, Object> map = createMap();
    Set<Entry<String, List<Object>>> set = map.entrySet();
    for (Entry<String, List<Object>> entry : set) {
      assertFault(entry.getKey().equals(KEY),
          "no key has been found in #entrySet()");
      List<Object> list = entry.getValue();
      assertFault(list.contains(VALUES[0]), VALUES[0],
          "was not found in #entrySet()");
      assertFault(list.contains(VALUES[1]), VALUES[1],
          "was not found in #entrySet()");
      assertFault(list.contains(VALUES[2]),
          "VALUES[2] was not found in #entrySet()");
    }
    logMsg("#entrySet() has correct values");
  }

  /*
   * @testName: equalsTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:735;
   * 
   * @test_Strategy: This implementation delegates the method call to to the the
   * underlying [key, multi-value] store.
   */
  public void equalsTest() throws Fault {
    ArrayList<Object> list = new ArrayList<Object>();
    list.add(VALUES[0]);
    Map<String, List<Object>> hashMap = new HashMap<String, List<Object>>();
    hashMap.put(KEY, list);

    AbstractMultivaluedMap<String, Object> map = new AbstractMultivaluedMap<String, Object>(
        hashMap) {
    };
    assertFault(map.equals(hashMap), "delegation of #equals() not sucessfull");
    hashMap.put("key2", list);
    assertFault(map.equals(hashMap), "delegation of #equals() not sucessfull");

    logMsg("#equals() is delegated to underlaying map");
  }

  /*
   * @testName: getTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:737;
   * 
   * @test_Strategy: get the list of items
   */
  public void getTest() throws Fault {
    ArrayList<Object> list = new ArrayList<Object>();
    list.add(VALUES[0]);
    Map<String, List<Object>> hashMap = new HashMap<String, List<Object>>();
    hashMap.put(KEY, list);

    AbstractMultivaluedMap<String, Object> map = new AbstractMultivaluedMap<String, Object>(
        hashMap) {
    };
    assertFault(map.get(KEY) == list, "#get() not sucessfull");
    logMsg("#get() is sucessfull");
  }

  /*
   * @testName: hashCodeTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:739;
   * 
   * @test_Strategy: This implementation delegates the method call to to the the
   * underlying [key, multi-value] store.
   */
  public void hashCodeTest() throws Fault {
    ArrayList<Object> list = new ArrayList<Object>();
    list.add(VALUES[0]);
    Map<String, List<Object>> hashMap = new HashMap<String, List<Object>>();
    hashMap.put(KEY, list);

    AbstractMultivaluedMap<String, Object> map = new AbstractMultivaluedMap<String, Object>(
        hashMap) {
    };
    assertFault(map.hashCode() == map.hashCode(),
        "delegation of #hashCode() not sucessfull");
    hashMap.put("key2", list);
    assertFault(map.hashCode() == map.hashCode(),
        "delegation of #hashCode() not sucessfull");

    logMsg("#hashCode() is delegated to underlaying map");
  }

  /*
   * @testName: isEmptyTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:740;
   * 
   * @test_Strategy: check whether it is empty
   */
  public void isEmptyTest() throws Fault {
    AbstractMultivaluedMap<String, Object> map = createMap();
    assertFault(!map.isEmpty(), "#isEmpty returns true for one key and a list");
    map.clear();
    assertFault(map.isEmpty(), "#isEmpty returns false after #clear call");
    map.add(null, VALUES[0]);
    assertFault(!map.isEmpty(),
        "#isEmpty returns true for null key and a list");

    logMsg("#isEmpty returns correct values");
  }

  /*
   * @testName: keySetTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:741;
   * 
   * @test_Strategy: keySet test
   */
  public void keySetTest() throws Fault {
    AbstractMultivaluedMap<String, Object> map = createMap();
    assertFault(map.keySet().size() == 1, "#keySet returns set of size",
        map.keySet().size(), "for one key");
    map.add("key2", VALUES[3]);
    assertFault(map.keySet().size() == 2, "#keySet returns set of size",
        map.keySet().size(), "for two keys");
    assertFault(map.keySet().contains(KEY), "key was not found");
    assertFault(map.keySet().contains("key2"), "key2 was not found");

    map.clear();
    assertFault(map.keySet().size() == 0, "#keySet returns set of size",
        map.keySet().size(), "after #clear()");
    map.add(null, VALUES[3]);
    assertFault(map.keySet().size() == 1, "#keySet returns set of size",
        map.keySet().size(), "for null key");
    logMsg("#keySet returns correct values");
  }

  /*
   * @testName: putTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:742;
   * 
   * @test_Strategy: put
   */
  public void putTest() throws Fault {
    ArrayList<Object> list = new ArrayList<Object>();
    list.add(VALUES[0]);
    list.add(VALUES[1]);
    list.add(VALUES[2]);

    Map<String, List<Object>> hashMap = new HashMap<String, List<Object>>();
    AbstractMultivaluedMap<String, Object> map = new AbstractMultivaluedMap<String, Object>(
        hashMap) {
    };
    map.put(KEY, list);
    assertContainsDefaultValues(map);

    logMsg("#put() put the value");
  }

  /*
   * @testName: putAllTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:743;
   * 
   * @test_Strategy: putAll
   */
  public void putAllTest() throws Fault {
    ArrayList<Object> list = new ArrayList<Object>();
    list.add(VALUES[0]);
    list.add(VALUES[1]);
    list.add(VALUES[2]);
    Map<String, List<Object>> hashMap = new HashMap<String, List<Object>>();
    hashMap.put(KEY, list);
    hashMap.put("key2", list);

    AbstractMultivaluedMap<String, Object> map = createMap();
    map.clear();
    assertFault(map.isEmpty(), "#clear did not emptied map");
    map.putAll(hashMap);
    assertContainsDefaultValues(map);
    assertContains(map, "key2", VALUES[0], VALUES[1], VALUES[2]);

    logMsg("#putAll() put all the values of both keys");
  }

  /*
   * @testName: putSingleTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:744; JAXRS:JAVADOC:98;
   * 
   * @test_Strategy: Set the value for the key to be a one item list consisting
   * of the supplied value. Any existing values will be replaced. NOTE: This
   * implementation ignores values; A supplied value of is ignored and not added
   * to the purged value list. As a result of such operation, empty value list
   * would be registered for the supplied key
   */
  public void putSingleTest() throws Fault {
    AbstractMultivaluedMap<String, Object> map = createMap();
    map.putSingle(KEY, VALUES[4]);
    assertContains(map, KEY, VALUES[4]);
    assertNotContains(map, KEY, VALUES[0], VALUES[1], VALUES[2]);

    logMsg("#putSingle() sucessfully replaced the values by a new one");
  }

  /*
   * @testName: putSingleNullValueTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:744; JAXRS:JAVADOC:98;
   * 
   * @test_Strategy: Set the value for the key to be a one item list consisting
   * of the supplied value. Any existing values will be replaced. NOTE: This
   * implementation ignores null values; A supplied value of is ignored and not
   * added to the purged value list. As a result of such operation, empty value
   * list would be registered for the supplied key
   */
  public void putSingleNullValueTest() throws Fault {
    AbstractMultivaluedMap<String, Object> map = createMap();
    map.putSingle(KEY, null);
    assertNotContains(map, KEY, (Object) null);
    logMsg("#putSingle(key, null) did replaced the values as expected");
  }

  /*
   * @testName: removeTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:745;
   * 
   * @test_Strategy: remove the correct key
   */
  public void removeTest() throws Fault {
    AbstractMultivaluedMap<String, Object> map = createMap();
    map.add("key2", VALUES[3]);
    List<Object> list = map.remove(KEY);
    assertFault(list.size() == 3, "removed a list of size", list.size());
    assertNotContains(map, KEY, VALUES[0], VALUES[1], VALUES[2]);
    assertContains(map, "key2", VALUES[3]);
    logMsg(
        "#removeTest(key) removed the correct key and the value as expected");
  }

  /*
   * @testName: sizeTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:746;
   * 
   * @test_Strategy: check correct size
   */
  public void sizeTest() throws Fault {
    AbstractMultivaluedMap<String, Object> map = createMap();
    map.add("key2", VALUES[3]);
    assertFault(map.size() == 2, "returned unexpected size", map.size());
    map.clear();
    assertFault(map.size() == 0, "returned unexpected size after clear",
        map.size());
    logMsg("#size() returns correct values");
  }

  /*
   * @testName: toStringTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:747;
   * 
   * @test_Strategy: check toString does something
   */
  public void toStringTest() throws Fault {
    AbstractMultivaluedMap<String, Object> map = createMap();
    assertFault(map.toString() != null, "#toString isNull");
    logMsg("#toString returned", map.toString());
  }

  /*
   * @testName: valuesTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:748;
   * 
   * @test_Strategy: check correct values
   */
  public void valuesTest() throws Fault {
    AbstractMultivaluedMap<String, Object> map = createMap();
    map.add("key2", VALUES[3]);
    Collection<List<Object>> values = map.values();

    for (List<Object> list : values)
      assertFault(list.size() == 1 || list.size() == 3,
          "Got unexpected values of size", list.size());
    assertFault(values.size() == 2, "Unexpected #values of size",
        values.size());
    logMsg("#values returned", values.size(), "lists");
  }

  /*
   * @testName: equalsIgnoreValueOrderTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:736;
   * 
   * @test_Strategy: Compare the specified map with this map for equality modulo
   * the order of values for each key. Specifically, the values associated with
   * each key are compared as if they were ordered lists.
   */
  public void equalsIgnoreValueOrderTest() throws Fault {
    AbstractMultivaluedMap<String, Object> map = createMap();
    MultivaluedHashMap<String, Object> map2 = new MultivaluedHashMap<String, Object>();
    map2.addAll(KEY, VALUES[2], VALUES[0], VALUES[1]);

    assertTrue(map.equalsIgnoreValueOrder(map2), map, "and", map2,
        "are not equalIgnoreValueOrder");
    assertFalse(map.equals(map2), map, "and", map2, "are equal");
    logMsg("#equalsIgnoreValueOrder compared maps", map, "and", map2,
        "as expected");
  }

  // ////////////////////////////////////////////////////////////////////////
  static final String[] VALUES = { "value1", "value2", "value3", "value4",
      "value5" };

  protected static AbstractMultivaluedMap<String, Object> createMap() {
    return createMap(KEY, VALUES[0], VALUES[1], VALUES[2]);
  }

  protected static AbstractMultivaluedMap<String, Object> createMap(String key,
      Object... values) {
    Map<String, List<Object>> hashMap = new HashMap<String, List<Object>>();
    ArrayList<Object> list = new ArrayList<Object>();
    list.addAll(Arrays.asList(values));
    hashMap.put(key, list);
    AbstractMultivaluedMap<String, Object> map = new AbstractMultivaluedMap<String, Object>(
        hashMap) {
    };
    return map;
  }

  protected static void assertContainsDefaultValues(
      AbstractMultivaluedMap<String, Object> map) throws Fault {
    assertContains(map, KEY, VALUES[0], VALUES[1], VALUES[2]);
  }

  protected static void assertContains(
      AbstractMultivaluedMap<String, Object> map, String key, Object... values)
      throws Fault {
    List<Object> list = map.get(key);
    for (Object item : values)
      assertFault(list.contains(item), "Given map does not contain value",
          item);
    logMsg("Found key", key, "with following values:");
    logMsg(values);
  }

  protected static void assertNotContains(
      AbstractMultivaluedMap<String, Object> map, String key, Object... values)
      throws Fault {
    List<Object> list = map.get(key);
    if (list != null)
      for (Object item : values)
        assertFault(!list.contains(item), "Given map does contain value", item);
    logMsg("Given key", key, "does not contain following values as expected:");
    logMsg(values);
  }

}
