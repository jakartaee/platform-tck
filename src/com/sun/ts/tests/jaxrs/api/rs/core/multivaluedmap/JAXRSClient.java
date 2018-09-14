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

package com.sun.ts.tests.jaxrs.api.rs.core.multivaluedmap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
import java.util.Vector;

import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import com.sun.ts.tests.jaxrs.common.JAXRSCommonClient;

/*
 * @class.setup_props: webServerHost;
 *                     webServerPort;
 *                     ts_home;
 */
public class JAXRSClient extends JAXRSCommonClient {

  private static final long serialVersionUID = 5073014935147496028L;

  static final String[] KEYS = { "key0", "key1", "key2" };

  private long id;

  protected MultivaluedMap<String, Object> map;

  protected Vector<Response> vec;

  public JAXRSClient() {
    this.id = serialVersionUID;
    Response r = Response.ok().build();
    map = r.getMetadata();
    vec = new Vector<Response>();
    vec.add(r);
  }

  /**
   * Entry point for different-VM execution. It should delegate to method
   * run(String[], PrintWriter, PrintWriter), and this method should not contain
   * any test configuration.
   */
  public static void main(String[] args) {
    JAXRSClient theTests = new JAXRSClient();
    theTests.run(args);
  }

  /*
   * @testName: getFirstWhenEmptyTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:97;
   * 
   * @test_Strategy: Ensure getFirst works for empty map
   */
  public void getFirstWhenEmptyTest() throws Fault {
    assertFault(map.getFirst(KEYS[0]) == null,
        "EmptyMultivaluedMap#getFirst() should return null");
    assertFault(map.getFirst(null) == null,
        "EmptyMultivaluedMap#getFirst() should return null");
  }

  /*
   * @testName: getFirstWhenSingleItemTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:96; JAXRS:JAVADOC:97;
   * 
   * @test_Strategy: Ensure getFirst works for a single item
   */
  public void getFirstWhenSingleItemTest() throws Fault {
    map.add(KEYS[0], this);
    assertFault(map.size() == 1,
        "MultivaluedMap contains nothing when item added");
    JAXRSClient that = ((JAXRSClient) map.getFirst(KEYS[0]));
    assertFault(that.id == serialVersionUID,
        "MultivaluedMap#getFirst() should contain what added first");
  }

  /*
   * @testName: getFirstWhenMoreItemsTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:96; JAXRS:JAVADOC:97;
   * 
   * @test_Strategy: Ensure getFirst works for more items
   */
  public void getFirstWhenMoreItemsTest() throws Fault {
    map.add(KEYS[0], this);
    map.add(KEYS[0], vec);
    assertFault(map.size() == 1,
        "There is more items in the map, but only a single key added");
    JAXRSClient that = ((JAXRSClient) map.getFirst(KEYS[0]));
    assertFault(that.id == serialVersionUID,
        "MultivaluedMap#getFirst() should contain what added first");
    assertFault(map.get(KEYS[0]).size() == 2,
        "There shall be two items in a list when added for a single key");
  }

  /*
   * @testName: getFirstWhenFirstErasedTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:96; JAXRS:JAVADOC:97;
   * 
   * @test_Strategy: Ensure getFirst works getFirst() item is erased
   */
  public void getFirstWhenFirstErasedTest() throws Fault {
    map.add(KEYS[0], this);
    map.add(KEYS[0], vec);
    ListIterator<Object> i = map.get(KEYS[0]).listIterator();
    while (i.hasNext()) {
      Object o = i.next(); // check the items in a list are the inserted
      assertFault(
          o.getClass() == Vector.class || o.getClass() == JAXRSClient.class,
          "Only items added to a map shall be there");
      if (o.getClass() == JAXRSClient.class) // remove first
        i.remove();
    }
    @SuppressWarnings("unchecked")
    Vector<Response> anotherVec = ((Vector<Response>) map.getFirst(KEYS[0]));
    assertFault(anotherVec.getClass() == Vector.class,
        "MultivaluedMap#getFirst() should not point to a removed instance");
  }

  /*
   * @testName: getFirstWhenListClearedTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:96; JAXRS:JAVADOC:97;
   * 
   * @test_Strategy: Try what to happen when remove all items in a list
   */
  public void getFirstWhenListClearedTest() throws Fault {
    map.add(KEYS[0], this);
    map.add(KEYS[0], vec);
    map.get(KEYS[0]).clear();
    assertFault(map.getFirst(KEYS[0]) == null,
        "MultivaluedMap#getFirst() should be null when list is empty");
  }

  /*
   * @testName: getFirstWhenKeyIsNullTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:96; JAXRS:JAVADOC:97;
   * 
   * @test_Strategy: Try the null key
   */
  public void getFirstWhenKeyIsNullTest() throws Fault {
    map.add(KEYS[0], this);
    map.add(null, vec);
    map.add(null, this);
    assertFault(map.get(null).size() == 2,
        "There shall be two items in a list when added for a null key");
    assertFault(map.size() == 2,
        "There shall be two items in a list when added items for two keys");
    assertFault(map.getFirst(null).getClass() == Vector.class,
        "The first item for a null key was not the one retreived");
  }

  /*
   * @testName: putSingleTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:98; JAXRS:JAVADOC:97;
   * 
   * @test_Strategy: Try putSingle with a key and null key
   */
  public void putSingleTest() throws Fault {
    map.putSingle(KEYS[0], this);
    map.putSingle(KEYS[0], vec);
    map.putSingle(null, vec);
    map.putSingle(null, this);
    assertFault(map.get(null).size() == 1,
        "There shall be one item in a list when putSingle() several times");
    assertFault(map.size() == 2,
        "There shall be two items in a list when added items for two keys");
    assertFault(map.getFirst(KEYS[0]).getClass() == Vector.class,
        "The first item for a null key was not the last put by putSingle()");
    assertFault(map.getFirst(null).getClass() == JAXRSClient.class,
        "The first item for a null key was not the last put by putSingle()");
  }

  /*
   * @testName: addAllValuesTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:832;
   * 
   * @test_Strategy: Add multiple values to the current list of values for the
   * supplied key. If the supplied array of new values is empty, method returns
   * immediately.
   */
  public void addAllValuesTest() throws Fault {
    JAXRSClient client1 = new JAXRSClient();
    map.addAll(KEYS[0], this, vec, client1);
    assertFault(map.get(KEYS[0]).size() == 3,
        "Not all objects were added by #addAll");
    assertFault(map.get(KEYS[0]).contains(vec),
        "given Vector has not been found in a map");
    assertFault(map.get(KEYS[0]).contains(client1),
        "given JAXRSClient has not been found in a map");
    assertFault(map.get(KEYS[0]).contains(this),
        "this class has not been found in a map");
    logMsg("All values have been added for key", KEYS[0]);
  }

  /*
   * @testName: addAllValuesNullKeyTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:832;
   * 
   * @test_Strategy: Add multiple values to the current list of values for the
   * supplied key. If the supplied array of new values is empty, method returns
   * immediately.
   */
  public void addAllValuesNullKeyTest() throws Fault {
    JAXRSClient client1 = new JAXRSClient();
    map.addAll(null, this, vec, client1);
    assertFault(map.get(null).size() == 3,
        "Not all objects were added by #addAll");
    assertFault(map.get(null).contains(vec),
        "given Vector has not been found in a map");
    assertFault(map.get(null).contains(client1),
        "given JAXRSClient has not been found in a map");
    assertFault(map.get(null).contains(this),
        "this class has not been found in a map");
    logMsg("All values have been added for null key");
  }

  /*
   * @testName: addAllValuesEmptyReturnsImmediatellyTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:832;
   * 
   * @test_Strategy: Add multiple values to the current list of values for the
   * supplied key. If the supplied array of new values is empty, method returns
   * immediately.
   */
  public void addAllValuesEmptyReturnsImmediatellyTest() throws Fault {
    map.addAll(KEYS[0], new Object[0]);
    assertFault(!map.containsKey(KEYS[0]),
        "Method did not return immediatelly for empty array");
    logMsg("Method did return immediatelly for empty array");
  }

  /*
   * @testName: addAllValuesThrowNPETest
   * 
   * @assertion_ids: JAXRS:JAVADOC:832;
   * 
   * @test_Strategy: Method throws a NullPointerException if the supplied array
   * of values is null.
   */
  public void addAllValuesThrowNPETest() throws Fault {
    try {
      map.addAll(KEYS[0], (Object[]) null);
      throw new Fault("NullPointerException has not been thrown");
    } catch (NullPointerException e) {
      logMsg("NullPointerException has been thrown as expected");
    }
  }

  /*
   * @testName: addAllListTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:833;
   * 
   * @test_Strategy: Add all the values from the supplied value list to the
   * current list of values for the supplied key. If the supplied value list is
   * empty, method returns immediately.
   */
  public void addAllListTest() throws Fault {
    JAXRSClient client1 = new JAXRSClient();
    map.addAll(KEYS[0], Arrays.asList((Object) this, vec, client1));
    assertFault(map.get(KEYS[0]).size() == 3,
        "Not all objects were added by #addAll");
    assertFault(map.get(KEYS[0]).contains(vec),
        "given Vector has not been found in a map");
    assertFault(map.get(KEYS[0]).contains(client1),
        "given JAXRSClient has not been found in a map");
    assertFault(map.get(KEYS[0]).contains(this),
        "this class has not been found in a map");
    logMsg("All values have been added for key", KEYS[0]);
  }

  /*
   * @testName: addAllListNullKeyTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:833;
   * 
   * @test_Strategy: Add all the values from the supplied value list to the
   * current list of values for the supplied key. If the supplied value list is
   * empty, method returns immediately.
   */
  public void addAllListNullKeyTest() throws Fault {
    JAXRSClient client1 = new JAXRSClient();
    map.addAll(null, Arrays.asList((Object) this, vec, client1));
    assertFault(map.get(null).size() == 3,
        "Not all objects were added by #addAll");
    assertFault(map.get(null).contains(vec),
        "given Vector has not been found in a map");
    assertFault(map.get(null).contains(client1),
        "given JAXRSClient has not been found in a map");
    assertFault(map.get(null).contains(this),
        "this class has not been found in a map");
    logMsg("All values have been added for null key");
  }

  /*
   * @testName: addAllListEmptyReturnsImmediatellyTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:833;
   * 
   * @test_Strategy: Add all the values from the supplied value list to the
   * current list of values for the supplied key. If the supplied value list is
   * empty, method returns immediately.
   */
  public void addAllListEmptyReturnsImmediatellyTest() throws Fault {
    map.addAll(KEYS[0], new ArrayList<Object>());
    assertFault(!map.containsKey(KEYS[0]),
        "Method did not return immediatelly for empty array");
    logMsg("Method did return immediatelly for empty array");
  }

  /*
   * @testName: addAllListThrowNPETest
   * 
   * @assertion_ids: JAXRS:JAVADOC:833;
   * 
   * @test_Strategy: throws NullPointerException - if the supplied value list is
   * null.
   */
  public void addAllListThrowNPETest() throws Fault {
    try {
      map.addAll(KEYS[0], (List<Object>) null);
      throw new Fault("NullPointerException has not been thrown");
    } catch (NullPointerException e) {
      logMsg("NullPointerException has been thrown as expected");
    }
  }

  /*
   * @testName: addFirstTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:834;
   * 
   * @test_Strategy: Add a value to the first position in the current list of
   * values for the supplied key.
   */
  public void addFirstTest() throws Fault {
    map.addAll(null, vec, this);
    map.addFirst(null, new StringBuilder().append("test"));
    assertFault(map.getFirst(null).getClass() == StringBuilder.class,
        "#addFirst didn't add new StringBuilder, but",
        map.getFirst(null).getClass());
    logMsg("#addFirst added new item to a first position");
  }

  /*
   * @testName: equalsIgnoreValueOrderTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:835;
   * 
   * @test_Strategy: Compare the specified map with this map for equality modulo
   * the order of values for each key. Specifically, the values associated with
   * each key are compared as if they were ordered lists.
   */
  public void equalsIgnoreValueOrderTest() throws Fault {
    Object o1 = new StringBuilder().append(KEYS[0]);
    Object o2 = new StringBuffer().append(KEYS[1]);
    Object o3 = new Object() {
      @Override
      public String toString() {
        return KEYS[2];
      }
    };
    map.add(KEYS[0], o1);
    map.add(KEYS[0], o2);
    map.add(KEYS[0], o3);

    MultivaluedHashMap<String, Object> map2 = new MultivaluedHashMap<String, Object>();
    map2.addAll(KEYS[0], o3, o1, o2);

    assertTrue(map.equalsIgnoreValueOrder(map2), map, "and", map2,
        "are not equalIgnoreValueOrder");
    assertFalse(map.equals(map2), map, "and", map2, "are equal");
    logMsg("#equalsIgnoreValueOrder compared maps", map, "and", map2,
        "as expected");
  }

}
