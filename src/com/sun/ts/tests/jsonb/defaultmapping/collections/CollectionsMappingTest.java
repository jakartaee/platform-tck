/*
 * Copyright (c) 2017, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jsonb.defaultmapping.collections;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.PriorityQueue;
import java.util.Properties;
import java.util.Queue;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.json.bind.JsonbException;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.harness.ServiceEETest;
import com.sun.ts.tests.jsonb.defaultmapping.collections.model.ArrayDequeContainer;
import com.sun.ts.tests.jsonb.defaultmapping.collections.model.ArrayListContainer;
import com.sun.ts.tests.jsonb.defaultmapping.collections.model.CollectionContainer;
import com.sun.ts.tests.jsonb.defaultmapping.collections.model.DequeContainer;
import com.sun.ts.tests.jsonb.defaultmapping.collections.model.EnumMapContainer;
import com.sun.ts.tests.jsonb.defaultmapping.collections.model.EnumSetContainer;
import com.sun.ts.tests.jsonb.defaultmapping.collections.model.HashSetContainer;
import com.sun.ts.tests.jsonb.defaultmapping.collections.model.LinkedHashMapContainer;
import com.sun.ts.tests.jsonb.defaultmapping.collections.model.LinkedHashSetContainer;
import com.sun.ts.tests.jsonb.defaultmapping.collections.model.LinkedListContainer;
import com.sun.ts.tests.jsonb.defaultmapping.collections.model.ListContainer;
import com.sun.ts.tests.jsonb.defaultmapping.collections.model.MapContainer;
import com.sun.ts.tests.jsonb.defaultmapping.collections.model.NavigableMapContainer;
import com.sun.ts.tests.jsonb.defaultmapping.collections.model.NavigableSetContainer;
import com.sun.ts.tests.jsonb.defaultmapping.collections.model.PriorityQueueContainer;
import com.sun.ts.tests.jsonb.defaultmapping.collections.model.QueueContainer;
import com.sun.ts.tests.jsonb.defaultmapping.collections.model.SetContainer;
import com.sun.ts.tests.jsonb.defaultmapping.collections.model.SortedMapContainer;
import com.sun.ts.tests.jsonb.defaultmapping.collections.model.SortedSetContainer;
import com.sun.ts.tests.jsonb.defaultmapping.collections.model.TreeMapContainer;
import com.sun.ts.tests.jsonb.defaultmapping.collections.model.TreeSetContainer;

/**
 * @test
 * @sources CollectionsMappingTest.java
 * @executeClass com.sun.ts.tests.jsonb.defaultmapping.collections.CollectionsMappingTest
 **/
public class CollectionsMappingTest extends ServiceEETest {
  private static final long serialVersionUID = 10L;

  private final Jsonb jsonb = JsonbBuilder.create();

  public static void main(String[] args) {
    EETest t = new CollectionsMappingTest();
    Status s = t.run(args, System.out, System.err);
    s.exit();
  }

  public void setup(String[] args, Properties p) throws Fault {
    logMsg("setup ok");
  }

  public void cleanup() throws Fault {
    logMsg("cleanup ok");
  }

  /*
   * @testName: testCollection
   *
   * @assertion_ids: JSONB:SPEC:JSB-3.10-1; JSONB:SPEC:JSB-3.10-3;
   * JSONB:SPEC:JSB-3.11-1; JSONB:SPEC:JSB-3.11-2
   *
   * @test_Strategy: Assert that Collection interface is correctly marshalled
   * using the runtime type and reports an error when unmarshalling
   */
  public Status testCollection() throws Fault {
    try {
      String jsonString = jsonb.toJson(new CollectionContainer() {{ setInstance(Arrays.asList("Test 1", "Test 2")); }});
      if (!jsonString.matches("\\{\\s*\"instance\"\\s*\\:\\s*\\[\\s*\"Test 1\"\\s*,\\s*\"Test 2\"\\s*\\]\\s*\\}")) {
        throw new Fault("Failed to get Collection attribute value.");
      }
    } catch (JsonbException x) {
      throw new Fault("An exception is not expected when marshalling a class with a Collection attribute.");
    }

    CollectionContainer unmarshalledObject = null;
    try {
      unmarshalledObject = jsonb.fromJson("{ \"instance\" : [ \"Test 1\", \"Test 2\" ] }", CollectionContainer.class);
      if (!unmarshalledObject.getInstance().contains("Test 1") || !unmarshalledObject.getInstance().contains("Test 2")) {
        throw new Fault("Failed to marshal object with Collection attribute.");
      }
    } catch (JsonbException x) {
      System.out.append("Received:").println(Arrays.toString(unmarshalledObject.getInstance().toArray()));
      throw new Fault("An exception is not expected when unmarshalling a class with a Collection attribute.");
    }
    return Status.passed("OK");
  }

  /*
   * @testName: testMap
   *
   * @assertion_ids: JSONB:SPEC:JSB-3.10-1; JSONB:SPEC:JSB-3.11-1;
   * JSONB:SPEC:JSB-3.11-2
   *
   * @test_Strategy: Assert that Map interface is successfully marshalled and
   * unmarshalled
   */
  public Status testMap() throws Fault {
    @SuppressWarnings("serial")
    Map<String, String> instance = new HashMap<String, String>() {
      {
        put("string1", "Test 1");
        put("string2", "Test 2");
      }
    };
    String jsonString = jsonb.toJson(new MapContainer() {
      {
        setInstance(instance);
      }
    });
    if (!jsonString.matches(
        "\\{\\s*\"instance\"\\s*:\\s*\\{\\s*\"string1\"\\s*:\\s*\"Test 1\"\\s*,\\s*\"string2\"\\s*:\\s*\"Test 2\"\\s*\\}\\s*\\}")) {
      throw new Fault("Failed to get Map attribute value.");
    }

    MapContainer unmarshalledObject = jsonb.fromJson(
        "{ \"instance\" : { \"string1\" : \"Test 1\", \"string2\" : \"Test 2\" } }",
        MapContainer.class);
    if (!instance.equals(unmarshalledObject.getInstance())) {
      throw new Fault("Failed to marshal object with Map attribute.");
    }

    return Status.passed("OK");
  }

  /*
   * @testName: testSet
   *
   * @assertion_ids: JSONB:SPEC:JSB-3.10-1; JSONB:SPEC:JSB-3.11-1;
   * JSONB:SPEC:JSB-3.11-2
   *
   * @test_Strategy: Assert that Set interface is successfully marshalled and
   * unmarshalled
   */
  public Status testSet() throws Fault {
    @SuppressWarnings("serial")
    HashSet<String> instance = new HashSet<String>() {
      {
        add("Test 1");
        add("Test 2");
      }
    };
    String jsonString = jsonb.toJson(new SetContainer() {
      {
        setInstance(instance);
      }
    });
    if (!jsonString.matches(
        "\\{\\s*\"instance\"\\s*:\\s*\\[\\s*\"Test \\d\"\\s*,\\s*\"Test \\d\"\\s*\\]\\s*\\}")) {
      throw new Fault("Failed to get Set attribute value.");
    }

    SetContainer unmarshalledObject = jsonb.fromJson(
        "{ \"instance\" : [ \"Test 1\", \"Test 2\" ] }", SetContainer.class);
    if (!instance.equals(unmarshalledObject.getInstance())) {
      throw new Fault("Failed to marshal object with Set attribute.");
    }

    return Status.passed("OK");
  }

  /*
   * @testName: testHashSet
   *
   * @assertion_ids: JSONB:SPEC:JSB-3.10-1; JSONB:SPEC:JSB-3.11-1;
   * JSONB:SPEC:JSB-3.11-2
   *
   * @test_Strategy: Assert that HashSet is successfully marshalled and
   * unmarshalled
   */
  public Status testHashSet() throws Fault {
    @SuppressWarnings("serial")
    HashSet<String> instance = new HashSet<String>() {
      {
        add("Test 1");
        add("Test 2");
      }
    };
    String jsonString = jsonb.toJson(new HashSetContainer() {
      {
        setInstance(instance);
      }
    });
    if (!jsonString.matches(
        "\\{\\s*\"instance\"\\s*:\\s*\\[\\s*\"Test \\d\"\\s*,\\s*\"Test \\d\"\\s*\\]\\s*\\}")) {
      throw new Fault("Failed to get HashSet attribute value.");
    }

    HashSetContainer unmarshalledObject = jsonb.fromJson(
        "{ \"instance\" : [ \"Test 1\", \"Test 2\" ] }",
        HashSetContainer.class);
    if (!instance.equals(unmarshalledObject.getInstance())) {
      throw new Fault("Failed to marshal object with HashSet attribute.");
    }

    return Status.passed("OK");
  }

  /*
   * @testName: testNavigableSet
   *
   * @assertion_ids: JSONB:SPEC:JSB-3.10-1; JSONB:SPEC:JSB-3.11-1;
   * JSONB:SPEC:JSB-3.11-2
   *
   * @test_Strategy: Assert that NavigableSet is successfully marshalled and
   * unmarshalled
   */
  public Status testNavigableSet() throws Fault {
    @SuppressWarnings("serial")
    NavigableSet<String> instance = new TreeSet<String>() {
      {
        add("Test 1");
        add("Test 2");
      }
    };
    String jsonString = jsonb.toJson(new NavigableSetContainer() {
      {
        setInstance(instance);
      }
    });
    if (!jsonString.matches(
        "\\{\\s*\"instance\"\\s*:\\s*\\[\\s*\"Test \\d\"\\s*,\\s*\"Test \\d\"\\s*\\]\\s*\\}")) {
      throw new Fault("Failed to get NavigableSet attribute value.");
    }

    NavigableSetContainer unmarshalledObject = jsonb.fromJson(
        "{ \"instance\" : [ \"Test 1\", \"Test 2\" ] }",
        NavigableSetContainer.class);
    if (!instance.equals(unmarshalledObject.getInstance())) {
      throw new Fault("Failed to marshal object with NavigableSet attribute.");
    }

    return Status.passed("OK");
  }

  /*
   * @testName: testSortedSet
   *
   * @assertion_ids: JSONB:SPEC:JSB-3.10-1; JSONB:SPEC:JSB-3.11-1;
   * JSONB:SPEC:JSB-3.11-2
   *
   * @test_Strategy: Assert that SortedSet interface is successfully marshalled
   * and unmarshalled
   */
  public Status testSortedSet() throws Fault {
    @SuppressWarnings("serial")
    SortedSet<String> instance = new TreeSet<String>() {
      {
        add("Test 1");
        add("Test 2");
      }
    };
    String jsonString = jsonb.toJson(new SortedSetContainer() {
      {
        setInstance(instance);
      }
    });
    if (!jsonString.matches(
        "\\{\\s*\"instance\"\\s*:\\s*\\[\\s*\"Test \\d\"\\s*,\\s*\"Test \\d\"\\s*\\]\\s*\\}")) {
      throw new Fault("Failed to get SortedSet attribute value.");
    }

    SortedSetContainer unmarshalledObject = jsonb.fromJson(
        "{ \"instance\" : [ \"Test 1\", \"Test 2\" ] }",
        SortedSetContainer.class);
    if (!instance.equals(unmarshalledObject.getInstance())) {
      throw new Fault("Failed to marshal object with SortedSet attribute.");
    }

    return Status.passed("OK");
  }

  /*
   * @testName: testTreeSet
   *
   * @assertion_ids: JSONB:SPEC:JSB-3.10-1; JSONB:SPEC:JSB-3.11-1;
   * JSONB:SPEC:JSB-3.11-2
   *
   * @test_Strategy: Assert that TreeSet is successfully marshalled and
   * unmarshalled
   */
  public Status testTreeSet() throws Fault {
    @SuppressWarnings("serial")
    TreeSet<String> instance = new TreeSet<String>() {
      {
        add("Test 1");
        add("Test 2");
      }
    };
    String jsonString = jsonb.toJson(new TreeSetContainer() {
      {
        setInstance(instance);
      }
    });
    if (!jsonString.matches(
        "\\{\\s*\"instance\"\\s*:\\s*\\[\\s*\"Test \\d\"\\s*,\\s*\"Test \\d\"\\s*\\]\\s*\\}")) {
      throw new Fault("Failed to get TreeSet attribute value.");
    }

    TreeSetContainer unmarshalledObject = jsonb.fromJson(
        "{ \"instance\" : [ \"Test 1\", \"Test 2\" ] }",
        TreeSetContainer.class);
    if (!instance.equals(unmarshalledObject.getInstance())) {
      throw new Fault("Failed to marshal object with TreeSet attribute.");
    }

    return Status.passed("OK");
  }

  /*
   * @testName: testLinkedHashSet
   *
   * @assertion_ids: JSONB:SPEC:JSB-3.10-1; JSONB:SPEC:JSB-3.11-1;
   * JSONB:SPEC:JSB-3.11-2
   *
   * @test_Strategy: Assert that LinkedHashSet is successfully marshalled and
   * unmarshalled
   */
  public Status testLinkedHashSet() throws Fault {
    @SuppressWarnings("serial")
    LinkedHashSet<String> instance = new LinkedHashSet<String>() {
      {
        add("Test 1");
        add("Test 2");
      }
    };
    String jsonString = jsonb.toJson(new LinkedHashSetContainer() {
      {
        setInstance(instance);
      }
    });
    if (!jsonString.matches(
        "\\{\\s*\"instance\"\\s*:\\s*\\[\\s*\"Test \\d\"\\s*,\\s*\"Test \\d\"\\s*\\]\\s*\\}")) {
      throw new Fault("Failed to get LinkedHashSet attribute value.");
    }

    LinkedHashSetContainer unmarshalledObject = jsonb.fromJson(
        "{ \"instance\" : [ \"Test 1\", \"Test 2\" ] }",
        LinkedHashSetContainer.class);
    if (!instance.equals(unmarshalledObject.getInstance())) {
      throw new Fault("Failed to marshal object with LinkedHashSet attribute.");
    }

    return Status.passed("OK");
  }

  /*
   * @testName: testHashMap
   *
   * @assertion_ids: JSONB:SPEC:JSB-3.10-1; JSONB:SPEC:JSB-3.11-1;
   * JSONB:SPEC:JSB-3.11-2
   *
   * @test_Strategy: Assert that HashMap is successfully marshalled and
   * unmarshalled
   */
  public Status testHashMap() throws Fault {
    @SuppressWarnings("serial")
    HashMap<String, String> instance = new HashMap<String, String>() {
      {
        put("string1", "Test 1");
        put("string2", "Test 2");
      }
    };
    String jsonString = jsonb.toJson(new MapContainer() {
      {
        setInstance(instance);
      }
    });
    if (!jsonString.matches(
        "\\{\\s*\"instance\"\\s*:\\s*\\{\\s*\"string1\"\\s*:\\s*\"Test 1\"\\s*,\\s*\"string2\"\\s*:\\s*\"Test 2\"\\s*\\}\\s*\\}")) {
      throw new Fault("Failed to get HashMap attribute value.");
    }

    MapContainer unmarshalledObject = jsonb.fromJson(
        "{ \"instance\" : { \"string1\" : \"Test 1\", \"string2\" : \"Test 2\" } }",
        MapContainer.class);
    if (!instance.equals(unmarshalledObject.getInstance())) {
      throw new Fault("Failed to marshal object with HashMap attribute.");
    }

    return Status.passed("OK");
  }

  /*
   * @testName: testNavigableMap
   *
   * @assertion_ids: JSONB:SPEC:JSB-3.10-1; JSONB:SPEC:JSB-3.11-1;
   * JSONB:SPEC:JSB-3.11-2
   *
   * @test_Strategy: Assert that NavigableMap interface is successfully
   * marshalled and unmarshalled
   */
  public Status testNavigableMap() throws Fault {
    @SuppressWarnings("serial")
    NavigableMap<String, String> instance = new TreeMap<String, String>() {
      {
        put("string1", "Test 1");
        put("string2", "Test 2");
      }
    };
    String jsonString = jsonb.toJson(new NavigableMapContainer() {
      {
        setInstance(instance);
      }
    });
    if (!jsonString.matches(
        "\\{\\s*\"instance\"\\s*:\\s*\\{\\s*\"string1\"\\s*:\\s*\"Test 1\"\\s*,\\s*\"string2\"\\s*:\\s*\"Test 2\"\\s*\\}\\s*\\}")) {
      throw new Fault("Failed to get NavigableMap attribute value.");
    }

    NavigableMapContainer unmarshalledObject = jsonb.fromJson(
        "{ \"instance\" : { \"string1\" : \"Test 1\", \"string2\" : \"Test 2\" } }",
        NavigableMapContainer.class);
    if (!instance.equals(unmarshalledObject.getInstance())) {
      throw new Fault("Failed to marshal object with NavigableMap attribute.");
    }

    return Status.passed("OK");
  }

  /*
   * @testName: testSortedMap
   *
   * @assertion_ids: JSONB:SPEC:JSB-3.10-1; JSONB:SPEC:JSB-3.11-1;
   * JSONB:SPEC:JSB-3.11-2
   *
   * @test_Strategy: Assert that SortedMap interface is successfully marshalled
   * and unmarshalled
   */
  public Status testSortedMap() throws Fault {
    @SuppressWarnings("serial")
    SortedMap<String, String> instance = new TreeMap<String, String>() {
      {
        put("string1", "Test 1");
        put("string2", "Test 2");
      }
    };
    String jsonString = jsonb.toJson(new SortedMapContainer() {
      {
        setInstance(instance);
      }
    });
    if (!jsonString.matches(
        "\\{\\s*\"instance\"\\s*:\\s*\\{\\s*\"string1\"\\s*:\\s*\"Test 1\"\\s*,\\s*\"string2\"\\s*:\\s*\"Test 2\"\\s*\\}\\s*\\}")) {
      throw new Fault("Failed to get SortedMap attribute value.");
    }

    SortedMapContainer unmarshalledObject = jsonb.fromJson(
        "{ \"instance\" : { \"string1\" : \"Test 1\", \"string2\" : \"Test 2\" } }",
        SortedMapContainer.class);
    if (!instance.equals(unmarshalledObject.getInstance())) {
      throw new Fault("Failed to marshal object with SortedMap attribute.");
    }

    return Status.passed("OK");
  }

  /*
   * @testName: testTreeMap
   *
   * @assertion_ids: JSONB:SPEC:JSB-3.10-1; JSONB:SPEC:JSB-3.11-1;
   * JSONB:SPEC:JSB-3.11-2
   *
   * @test_Strategy: Assert that TreeMap is successfully marshalled and
   * unmarshalled
   */
  public Status testTreeMap() throws Fault {
    @SuppressWarnings("serial")
    TreeMap<String, String> instance = new TreeMap<String, String>() {
      {
        put("string1", "Test 1");
        put("string2", "Test 2");
      }
    };
    String jsonString = jsonb.toJson(new TreeMapContainer() {
      {
        setInstance(instance);
      }
    });
    if (!jsonString.matches(
        "\\{\\s*\"instance\"\\s*:\\s*\\{\\s*\"string1\"\\s*:\\s*\"Test 1\"\\s*,\\s*\"string2\"\\s*:\\s*\"Test 2\"\\s*\\}\\s*\\}")) {
      throw new Fault("Failed to get TreeMap attribute value.");
    }

    TreeMapContainer unmarshalledObject = jsonb.fromJson(
        "{ \"instance\" : { \"string1\" : \"Test 1\", \"string2\" : \"Test 2\" } }",
        TreeMapContainer.class);
    if (!instance.equals(unmarshalledObject.getInstance())) {
      throw new Fault("Failed to marshal object with TreeMap attribute.");
    }

    return Status.passed("OK");
  }

  /*
   * @testName: testLinkedHashMap
   *
   * @assertion_ids: JSONB:SPEC:JSB-3.10-1; JSONB:SPEC:JSB-3.11-1;
   * JSONB:SPEC:JSB-3.11-2
   *
   * @test_Strategy: Assert that LinkedHashMap is successfully marshalled and
   * unmarshalled
   */
  public Status testLinkedHashMap() throws Fault {
    @SuppressWarnings("serial")
    LinkedHashMap<String, String> instance = new LinkedHashMap<String, String>() {
      {
        put("string1", "Test 1");
        put("string2", "Test 2");
      }
    };
    String jsonString = jsonb.toJson(new LinkedHashMapContainer() {
      {
        setInstance(instance);
      }
    });
    if (!jsonString.matches(
        "\\{\\s*\"instance\"\\s*:\\s*\\{\\s*\"string1\"\\s*:\\s*\"Test 1\"\\s*,\\s*\"string2\"\\s*:\\s*\"Test 2\"\\s*\\}\\s*\\}")) {
      throw new Fault("Failed to get LinkedHashMap attribute value.");
    }

    LinkedHashMapContainer unmarshalledObject = jsonb.fromJson(
        "{ \"instance\" : { \"string1\" : \"Test 1\", \"string2\" : \"Test 2\" } }",
        LinkedHashMapContainer.class);
    if (!instance.equals(unmarshalledObject.getInstance())) {
      throw new Fault("Failed to marshal object with LinkedHashMap attribute.");
    }

    return Status.passed("OK");
  }

  /*
   * @testName: testList
   *
   * @assertion_ids: JSONB:SPEC:JSB-3.10-1; JSONB:SPEC:JSB-3.11-1;
   * JSONB:SPEC:JSB-3.11-2
   *
   * @test_Strategy: Assert that List interface is successfully marshalled and
   * unmarshalled
   */
  public Status testList() throws Fault {
    @SuppressWarnings("serial")
    List<String> instance = new ArrayList<String>() {
      {
        add("Test 1");
        add("Test 2");
      }
    };
    String jsonString = jsonb.toJson(new ListContainer() {
      {
        setInstance(instance);
      }
    });
    if (!jsonString.matches(
        "\\{\\s*\"instance\"\\s*:\\s*\\[\\s*\"Test \\d\"\\s*,\\s*\"Test \\d\"\\s*\\]\\s*\\}")) {
      throw new Fault("Failed to get List attribute value.");
    }

    ListContainer unmarshalledObject = jsonb.fromJson(
        "{ \"instance\" : [ \"Test 1\", \"Test 2\" ] }", ListContainer.class);
    if (!instance.equals(unmarshalledObject.getInstance())) {
      throw new Fault("Failed to marshal object with List attribute.");
    }

    return Status.passed("OK");
  }

  /*
   * @testName: testArrayList
   *
   * @assertion_ids: JSONB:SPEC:JSB-3.10-1; JSONB:SPEC:JSB-3.11-1;
   * JSONB:SPEC:JSB-3.11-2
   *
   * @test_Strategy: Assert that ArrayList is successfully marshalled and
   * unmarshalled
   */
  public Status testArrayList() throws Fault {
    @SuppressWarnings("serial")
    ArrayList<String> instance = new ArrayList<String>() {
      {
        add("Test 1");
        add("Test 2");
      }
    };
    String jsonString = jsonb.toJson(new ArrayListContainer() {
      {
        setInstance(instance);
      }
    });
    if (!jsonString.matches(
        "\\{\\s*\"instance\"\\s*:\\s*\\[\\s*\"Test \\d\"\\s*,\\s*\"Test \\d\"\\s*\\]\\s*\\}")) {
      throw new Fault("Failed to get ArrayList attribute value.");
    }

    ArrayListContainer unmarshalledObject = jsonb.fromJson(
        "{ \"instance\" : [ \"Test 1\", \"Test 2\" ] }",
        ArrayListContainer.class);
    if (!instance.equals(unmarshalledObject.getInstance())) {
      throw new Fault("Failed to marshal object with ArrayList attribute.");
    }

    return Status.passed("OK");
  }

  /*
   * @testName: testLinkedList
   *
   * @assertion_ids: JSONB:SPEC:JSB-3.10-1; JSONB:SPEC:JSB-3.11-1;
   * JSONB:SPEC:JSB-3.11-2
   *
   * @test_Strategy: Assert that LinkedList is successfully marshalled and
   * unmarshalled
   */
  public Status testLinkedList() throws Fault {
    @SuppressWarnings("serial")
    LinkedList<String> instance = new LinkedList<String>() {
      {
        add("Test 1");
        add("Test 2");
      }
    };
    String jsonString = jsonb.toJson(new LinkedListContainer() {
      {
        setInstance(instance);
      }
    });
    if (!jsonString.matches(
        "\\{\\s*\"instance\"\\s*:\\s*\\[\\s*\"Test \\d\"\\s*,\\s*\"Test \\d\"\\s*\\]\\s*\\}")) {
      throw new Fault("Failed to get LinkedList attribute value.");
    }

    LinkedListContainer unmarshalledObject = jsonb.fromJson(
        "{ \"instance\" : [ \"Test 1\", \"Test 2\" ] }",
        LinkedListContainer.class);
    if (!instance.equals(unmarshalledObject.getInstance())) {
      throw new Fault("Failed to marshal object with LinkedList attribute.");
    }

    return Status.passed("OK");
  }

  /*
   * @testName: testDeque
   *
   * @assertion_ids: JSONB:SPEC:JSB-3.10-1; JSONB:SPEC:JSB-3.11-1;
   * JSONB:SPEC:JSB-3.11-2
   *
   * @test_Strategy: Assert that Deque interface is successfully marshalled and
   * unmarshalled
   */
  public Status testDeque() throws Fault {
    @SuppressWarnings("serial")
    Deque<String> instance = new ArrayDeque<String>() {
      {
        add("Test 1");
        add("Test 2");
      }
    };
    String jsonString = jsonb.toJson(new DequeContainer() {
      {
        setInstance(instance);
      }
    });
    if (!jsonString.matches(
        "\\{\\s*\"instance\"\\s*:\\s*\\[\\s*\"Test \\d\"\\s*,\\s*\"Test \\d\"\\s*\\]\\s*\\}")) {
      throw new Fault("Failed to get Deque attribute value.");
    }

    DequeContainer unmarshalledObject = jsonb.fromJson(
        "{ \"instance\" : [ \"Test 1\", \"Test 2\" ] }", DequeContainer.class);
    if (!Arrays.asList(instance.toArray())
        .equals(Arrays.asList(unmarshalledObject.getInstance().toArray()))) {
      throw new Fault("Failed to marshal object with Deque attribute.");
    }

    return Status.passed("OK");
  }

  /*
   * @testName: testArrayDeque
   *
   * @assertion_ids: JSONB:SPEC:JSB-3.10-1; JSONB:SPEC:JSB-3.11-1;
   * JSONB:SPEC:JSB-3.11-2
   *
   * @test_Strategy: Assert that ArrayDeque is successfully marshalled and
   * unmarshalled
   */
  public Status testArrayDeque() throws Fault {
    @SuppressWarnings("serial")
    ArrayDeque<String> instance = new ArrayDeque<String>() {
      {
        add("Test 1");
        add("Test 2");
      }
    };
    String jsonString = jsonb.toJson(new ArrayDequeContainer() {
      {
        setInstance(instance);
      }
    });
    if (!jsonString.matches(
        "\\{\\s*\"instance\"\\s*:\\s*\\[\\s*\"Test \\d\"\\s*,\\s*\"Test \\d\"\\s*\\]\\s*\\}")) {
      throw new Fault("Failed to get ArrayDeque attribute value.");
    }

    ArrayDequeContainer unmarshalledObject = jsonb.fromJson(
        "{ \"instance\" : [ \"Test 1\", \"Test 2\" ] }",
        ArrayDequeContainer.class);
    if (!Arrays.asList(instance.toArray())
        .equals(Arrays.asList(unmarshalledObject.getInstance().toArray()))) {
      throw new Fault("Failed to marshal object with ArrayDeque attribute.");
    }

    return Status.passed("OK");
  }

  /*
   * @testName: testQueue
   *
   * @assertion_ids: JSONB:SPEC:JSB-3.10-1; JSONB:SPEC:JSB-3.11-1;
   * JSONB:SPEC:JSB-3.11-2
   *
   * @test_Strategy: Assert that Queue interface is successfully marshalled and
   * unmarshalled
   */
  public Status testQueue() throws Fault {
    @SuppressWarnings("serial")
    Queue<String> instance = new LinkedList<String>() {
      {
        add("Test 1");
        add("Test 2");
      }
    };
    String jsonString = jsonb.toJson(new QueueContainer() {
      {
        setInstance(instance);
      }
    });
    if (!jsonString.matches(
        "\\{\\s*\"instance\"\\s*:\\s*\\[\\s*\"Test \\d\"\\s*,\\s*\"Test \\d\"\\s*\\]\\s*\\}")) {
      throw new Fault("Failed to get Queue attribute value.");
    }

    QueueContainer unmarshalledObject = jsonb.fromJson(
        "{ \"instance\" : [ \"Test 1\", \"Test 2\" ] }", QueueContainer.class);
    if (!Arrays.asList(instance.toArray())
        .equals(Arrays.asList(unmarshalledObject.getInstance().toArray()))) {
      throw new Fault("Failed to marshal object with Queue attribute.");
    }

    return Status.passed("OK");
  }

  /*
   * @testName: testPriorityQueue
   *
   * @assertion_ids: JSONB:SPEC:JSB-3.10-1; JSONB:SPEC:JSB-3.11-1;
   * JSONB:SPEC:JSB-3.11-2
   *
   * @test_Strategy: Assert that PriorityQueue is successfully marshalled and
   * unmarshalled
   */
  public Status testPriorityQueue() throws Fault {
    @SuppressWarnings("serial")
    PriorityQueue<String> instance = new PriorityQueue<String>() {
      {
        add("Test 1");
        add("Test 2");
      }
    };
    String jsonString = jsonb.toJson(new PriorityQueueContainer() {
      {
        setInstance(instance);
      }
    });
    if (!jsonString.matches(
        "\\{\\s*\"instance\"\\s*:\\s*\\[\\s*\"Test \\d\"\\s*,\\s*\"Test \\d\"\\s*\\]\\s*\\}")) {
      throw new Fault("Failed to get PriorityQueue attribute value.");
    }

    PriorityQueueContainer unmarshalledObject = jsonb.fromJson(
        "{ \"instance\" : [ \"Test 1\", \"Test 2\" ] }",
        PriorityQueueContainer.class);
    if (!Arrays.asList(instance.toArray())
        .equals(Arrays.asList(unmarshalledObject.getInstance().toArray()))) {
      throw new Fault("Failed to marshal object with PriorityQueue attribute.");
    }

    return Status.passed("OK");
  }

  /*
   * @testName: testEnumSet
   *
   * @assertion_ids: JSONB:SPEC:JSB-3.10-1; JSONB:SPEC:JSB-3.11-1;
   * JSONB:SPEC:JSB-3.11-2
   *
   * @test_Strategy: Assert that EnumSet is successfully marshalled and reports
   * an error when unmarshalling
   */
  public Status testEnumSet() throws Fault {
    EnumSet<EnumSetContainer.Enum> instance = EnumSet
        .allOf(EnumSetContainer.Enum.class);
    String jsonString = jsonb.toJson(new EnumSetContainer() {
      {
        setInstance(instance);
      }
    });
    if (!jsonString.matches(
        "\\{\\s*\"instance\"\\s*:\\s*\\[\\s*\"ONE\"\\s*,\\s*\"TWO\"\\s*\\]\\s*\\}")) {
      throw new Fault("Failed to get EnumSet attribute value.");
    }

    try {
      jsonb.fromJson("{ \"instance\" : [ \"ONE\", \"TWO\" ] }",
          EnumSetContainer.class);
      throw new Fault(
          "An exception is expected when unmarshalling a class with an EnumSet attribute.");
    } catch (JsonbException x) {
      return Status.passed("OK");
    }
  }

  /*
   * @testName: testEnumMap
   *
   * @assertion_ids: JSONB:SPEC:JSB-3.10-1; JSONB:SPEC:JSB-3.11-1;
   * JSONB:SPEC:JSB-3.11-2
   *
   * @test_Strategy: Assert that EnumMap is successfully marshalled and reports
   * an error when unmarshalling
   */
  public Status testEnumMap() throws Fault {
    @SuppressWarnings("serial")
    EnumMap<EnumSetContainer.Enum, String> instance = new EnumMap<EnumSetContainer.Enum, String>(
        EnumSetContainer.Enum.class) {
      {
        put(EnumSetContainer.Enum.ONE, "Test 1");
        put(EnumSetContainer.Enum.TWO, "Test 2");
      }
    };
    String jsonString = jsonb.toJson(new EnumMapContainer() {
      {
        setInstance(instance);
      }
    });
    if (!jsonString.matches(
        "\\{\\s*\"instance\"\\s*:\\s*\\{\\s*\"ONE\"\\s*:\\s*\"Test 1\"\\s*,\\s*\"TWO\"\\s*:\\s*\"Test 2\"\\s*\\}\\s*\\}")) {
      throw new Fault("Failed to get EnumMap attribute value.");
    }

    try {
      jsonb.fromJson(
          "{ \"instance\" : { \"ONE\" : \"Test 1\", \"TWO\" : \"Test 2\" } }",
          EnumMapContainer.class);
      throw new Fault(
          "An exception is expected when unmarshalling a class with an EnumMap attribute.");
    } catch (JsonbException x) {
      return Status.passed("OK");
    }
  }
}
