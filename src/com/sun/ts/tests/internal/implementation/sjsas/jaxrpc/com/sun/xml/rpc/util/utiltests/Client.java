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

package com.sun.ts.tests.internal.implementation.sjsas.jaxrpc.com.sun.xml.rpc.util.utiltests;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import java.io.*;
import java.net.*;
import java.util.*;
import java.rmi.*;

import com.sun.javatest.Status;

// Import implementation specific classes to test
import com.sun.xml.rpc.util.*;
import com.sun.xml.rpc.util.xml.*;
import com.sun.xml.rpc.util.localization.*;

public class Client extends EETest {
  private Properties props = null;

  private String bundleName = "com.sun.ts.tests.internal.implementation.sjsas.jaxrpc.com.sun.xml.messaging.jaxrpc.util.utiltests.MyResourceBundle";

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /* Test setup */

  /*
   * @class.setup_props:
   */

  public void setup(String[] args, Properties p) throws Fault {
    TestUtil.logMsg("setup ok");
  }

  public void cleanup() throws Fault {
    TestUtil.logMsg("cleanup ok");
  }

  /*
   * @testName: CachedStackTest
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void CachedStackTest() throws Fault {
    TestUtil.logMsg("CachedStackTest");
    boolean pass = true;
    try {
      TestUtil.logMsg("Create a CachedStack object");
      MyCachedStack mcs = new MyCachedStack();
      TestUtil.logMsg("Call CachedStack.push() method");
      mcs.push();
      mcs.push();
      TestUtil.logMsg("Call CachedStack.peek() method");
      Object obj = mcs.peek();
      TestUtil.logMsg("Object=" + obj);
      TestUtil.logMsg("Call CachedStack.pop() method");
      mcs.pop();
      TestUtil.logMsg("Call CachedStack.depth() method");
      int depth = mcs.depth();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("CachedStackTest failed", e);
    }

    if (!pass)
      throw new Fault("CachedStackTest failed");
  }

  /*
   * @testName: DebugTest
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void DebugTest() throws Fault {
    TestUtil.logMsg("DebugTest");
    boolean pass = true;
    try {
      TestUtil.logMsg("Call Debug() constructor");
      Debug debug = new Debug();
      TestUtil.logMsg("Call Debug.println() method");
      debug.println("This is a debug statement.");
      TestUtil.logMsg("Call Debug.enabled() method");
      boolean enabled = debug.enabled();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("DebugTest failed", e);
    }

    if (!pass)
      throw new Fault("DebugTest failed");
  }

  /*
   * @testName: HolderExceptionTest
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void HolderExceptionTest() throws Fault {
    TestUtil.logMsg("HolderExceptionTest");
    boolean pass = true;
    try {
      TestUtil.logMsg("Call HolderException(String) constructor");
      HolderException he = new HolderException("My HolderException!");
      TestUtil.logMsg("Call HolderException(String, String) constructor");
      he = new HolderException("My HolderException!", "So what!");
      TestUtil.logMsg("Call HolderException(String, Object[]) constructor");
      Object args[] = { "My arg1", "My arg2", "My arg3" };
      he = new HolderException("My HolderException!", args);
      TestUtil.logMsg("Call HolderException(String, Localizable) constructor");
      HolderException he1 = new HolderException("My HolderException1!");
      HolderException he2 = new HolderException("Key1", he1);
      TestUtil.logMsg("Call HolderException.getResourceBundleName() method");
      String rbn = he.getResourceBundleName();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("HolderExceptionTest failed", e);
    }

    if (!pass)
      throw new Fault("HolderExceptionTest failed");
  }

  /*
   * @testName: WSDLParseExceptionTest
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void WSDLParseExceptionTest() throws Fault {
    TestUtil.logMsg("WSDLParseExceptionTest");
    boolean pass = true;
    try {
      TestUtil.logMsg("Call WSDLParseException(String) constructor");
      WSDLParseException ex = new WSDLParseException("My WSDLParseException!");
      TestUtil.logMsg("Call WSDLParseException(String, String) constructor");
      ex = new WSDLParseException("My WSDLParseException!", "So what!");
      TestUtil.logMsg("Call WSDLParseException(String, Object[]) constructor");
      Object args[] = { "My arg1", "My arg2", "My arg3" };
      ex = new WSDLParseException("My WSDLParseException!", args);
      TestUtil
          .logMsg("Call WSDLParseException(String, Localizable) constructor");
      WSDLParseException ex1 = new WSDLParseException(
          "My WSDLParseException1!");
      WSDLParseException ex2 = new WSDLParseException("Key1", ex1);
      TestUtil.logMsg("Call WSDLParseException.getResourceBundleName() method");
      String rbn = ex.getResourceBundleName();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("WSDLParseExceptionTest failed", e);
    }

    if (!pass)
      throw new Fault("WSDLParseExceptionTest failed");
  }

  /*
   * @testName: IdentityMapTest
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void IdentityMapTest() throws Fault {
    TestUtil.logMsg("IdentityMapTest");
    boolean pass = true;
    try {
      TestUtil.logMsg("Call IdentityMap() constructor");
      IdentityMap im = new IdentityMap();
      TestUtil.logMsg("Call IdentityMap(int) constructor");
      im = new IdentityMap(100);
      TestUtil.logMsg("Call IdentityMap(Map) constructor");
      Hashtable ht = new Hashtable();
      ht.put("Key1", "Value1");
      ht.put("Key2", "Value2");
      ht.put("Key3", "Value3");
      im = new IdentityMap(ht);
      TestUtil.logMsg("Call IdentityMap.size() method");
      int size = im.size();
      TestUtil.logMsg("Call IdentityMap.isEmpty() method");
      boolean empty = im.isEmpty();
      TestUtil.logMsg("Call IdentityMap.get(Object) method");
      Object obj = im.get("Key1");
      TestUtil.logMsg("Call IdentityMap.containsKey(Object) method");
      if (!im.containsKey("Key1")) {
        TestUtil.logErr("Returned false but expected true");
        pass = false;
      }
      if (im.containsKey("BadKey")) {
        TestUtil.logErr("Returned true but expected false");
        pass = false;
      }
      TestUtil.logMsg("Call IdentityMap.containsValue(Object) method");
      if (!im.containsValue("Value1")) {
        TestUtil.logErr("Returned false but expected true");
        pass = false;
      }
      if (im.containsValue("BadValue")) {
        TestUtil.logErr("Returned true but expected false");
        pass = false;
      }
      TestUtil.logMsg("Call IdentityMap.put(Object, Object) method");
      im.put("Key4", "Value4");
      TestUtil.logMsg("Call IdentityMap.putAll(Map) method");
      ht = new Hashtable();
      ht.put("Key5", "Value5");
      ht.put("Key6", "Value6");
      ht.put("Key7", "Value7");
      im.putAll(ht);
      TestUtil.logMsg("Call IdentityMap.equals(Object) method");
      if (im.equals("boo hoo")) {
        TestUtil.logErr("Returned true but expected false");
        pass = false;
      }
      Object clone = im.clone();
      if (!im.equals(clone)) {
        TestUtil.logErr("Returned false but expected true");
        pass = false;
      }
      TestUtil.logMsg("Call IdentityMap.hashCode() method");
      int hashcode = im.hashCode();
      TestUtil.logMsg("Call IdentityMap.remove(Object) method");
      im.remove("Key7");
      TestUtil.logMsg("Call IdentityMap.clear() method");
      im.clear();
      ht = new Hashtable();
      ht.put("Key1", "Value1");
      ht.put("Key2", "Value2");
      ht.put("Key3", "Value3");
      ht.put("Key4", "Value4");
      ht.put("Key5", "Value5");
      im.putAll(ht);
      TestUtil.logMsg("Call IdentityMap.keySet() method");
      Set keyset = im.keySet();
      TestUtil.logMsg("Call KeySet.size() method");
      size = keyset.size();
      TestUtil.logMsg("Call KeySet.contains(Object) method");
      boolean b = keyset.contains("Key1");
      TestUtil.logMsg("Call KeySet.remove(Object) method");
      b = keyset.remove("Key1");
      TestUtil.logMsg("Call KeySet.iterator() method");
      Iterator keyiterator = keyset.iterator();
      TestUtil.logMsg("Call KeyIterator.hasNext() method");
      while (keyiterator.hasNext()) {
        TestUtil.logMsg("Call KeyIterator.next() method");
        obj = keyiterator.next();
        TestUtil.logMsg("Call KeyIterator.remove() method");
        keyiterator.remove();
      }
      TestUtil.logMsg("Call KeySet.clear() method");
      keyset.clear();
      im.clear();
      ht = new Hashtable();
      ht.put("Key1", "Value1");
      ht.put("Key2", "Value2");
      ht.put("Key3", "Value3");
      ht.put("Key4", "Value4");
      ht.put("Key5", "Value5");
      im.putAll(ht);
      TestUtil.logMsg("Call IdentityMap.values() method");
      Collection collection = im.values();
      TestUtil.logMsg("Call Values.size() method");
      size = collection.size();
      TestUtil.logMsg("Call Values.contains(Object) method");
      b = collection.contains("Key1");
      TestUtil.logMsg("Call Values.remove(Object) method");
      b = collection.remove("Key1");
      TestUtil.logMsg("Call Values.iterator() method");
      Iterator valueiterator = collection.iterator();
      TestUtil.logMsg("Call ValueIterator.hasNext() method");
      while (valueiterator.hasNext()) {
        TestUtil.logMsg("Call ValueIterator.next() method");
        obj = valueiterator.next();
        TestUtil.logMsg("Call ValueIterator.remove() method");
        valueiterator.remove();
      }
      TestUtil.logMsg("Call Values.clear() method");
      collection.clear();
      im.clear();
      ht = new Hashtable();
      ht.put("Key1", "Value1");
      ht.put("Key2", "Value2");
      ht.put("Key3", "Value3");
      ht.put("Key4", "Value4");
      ht.put("Key5", "Value5");
      im.putAll(ht);
      TestUtil.logMsg("Call IdentityMap.entrySet() method");
      Set entryset = im.entrySet();
      TestUtil.logMsg("Call EntrySet.size() method");
      size = entryset.size();
      TestUtil.logMsg("Call EntrySet.toArray() method");
      Object objarray[] = entryset.toArray();
      TestUtil.logMsg("Call EntrySet.contains(Object) method");
      b = entryset.contains("Key1");
      TestUtil.logMsg("Call EntrySet.remove(Object) method");
      b = entryset.remove("Key1");
      TestUtil.logMsg("Call EntrySet.iterator() method");
      Iterator entryiterator = entryset.iterator();
      TestUtil.logMsg("Call EntryIterator.hasNext() method");
      while (entryiterator.hasNext()) {
        TestUtil.logMsg("Call EntryIterator.next() method");
        obj = entryiterator.next();
        TestUtil.logMsg("Call EntryIterator.remove() method");
        entryiterator.remove();
      }
      TestUtil.logMsg("Call EntrySet.clear() method");
      entryset.clear();
      im.clear();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("IdentityMapTest failed", e);
    }

    if (!pass)
      throw new Fault("IdentityMapTest failed");
  }

  /*
   * @testName: LongStackTest
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void LongStackTest() throws Fault {
    TestUtil.logMsg("LongStackTest");
    boolean pass = true;
    try {
      TestUtil.logMsg("Call LongStack() constructor");
      LongStack ls = new LongStack();
      TestUtil.logMsg("Call LongStack(int) constructor");
      ls = new LongStack(32);
      TestUtil.logMsg("Call LongStack.push() method");
      ls.push((long) 100);
      ls.push((long) 200);
      TestUtil.logMsg("Call LongStack.peek() method");
      long value = ls.peek();
      TestUtil.logMsg("value=" + value);
      TestUtil.logMsg("Call LongStack.pop() method");
      value = ls.pop();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("LongStackTest failed", e);
    }

    if (!pass)
      throw new Fault("LongStackTest failed");
  }

  /*
   * @testName: NamespaceTest
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void NamespaceTest() throws Fault {
    TestUtil.logMsg("NamespaceTest");
    boolean pass = true;
    try {
      TestUtil.logMsg("Call Namespace.getNamespace(String, String) method");
      Namespace ns = Namespace.getNamespace("prefix", "http://mynamespace.com");
      TestUtil.logMsg("Call Namespace.hashcode() method");
      int hashcode = ns.hashCode();
      TestUtil.logMsg("Call Namespace.getPrefix() method");
      if (!ns.getPrefix().equals("prefix")) {
        TestUtil.logErr("Unexpected prefix, expected [prefix], got ["
            + ns.getPrefix() + "]");
        pass = false;
      }
      TestUtil.logMsg("Call Namespace.getURI() method");
      if (!ns.getURI().equals("http://mynamespace.com")) {
        TestUtil.logErr("Unexpected URI, expected [http://mynamespace.com],"
            + " got [" + ns.getURI() + "]");
        pass = false;
      }
      Namespace ns2 = Namespace.getNamespace("prefix2",
          "http://mynamespace2.com");
      TestUtil.logMsg("Call Namespace.equals(Object) method");
      if (ns.equals(ns2)) {
        TestUtil.logErr("Unexpected return, expected false, got true");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("NamespaceTest failed", e);
    }

    if (!pass)
      throw new Fault("NamespaceTest failed");
  }

  /*
   * @testName: ResourcesTest
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void ResourcesTest() throws Fault {
    TestUtil.logMsg("ResourcesTest");
    boolean pass = true;
    try {
      TestUtil.logMsg("Call Resources(String) constructor");
      Resources res = new Resources(bundleName);
      TestUtil.logMsg("Call Resources.getString(String) method");
      String string = res.getString("message1");
      if (!string.equals("This is message1.")) {
        TestUtil.logErr("Unexpected string returned");
        TestUtil.logErr("Expected: This is message1.");
        TestUtil.logErr("Got: " + string);
        pass = false;
      }
      String[] args = { "value1", "value2" };
      TestUtil.logMsg("Call Resources.getString(String, String[]) method");
      string = res.getString("message3", args);
      if (!string.equals("This is message3 with arg value1 and arg value2.")) {
        TestUtil.logErr("Unexpected string returned");
        TestUtil.logErr(
            "Expected: This is message3 with arg value1 and arg value2.");
        TestUtil.logErr("Got: " + string);
        pass = false;
      }
      String[] arg = { "hello" };
      TestUtil.logMsg("Call Resources.getString(String, String) method");
      string = res.getString("message4", arg);
      if (!string.equals("This is message4 with just arg hello.")) {
        TestUtil.logErr("Unexpected string returned");
        TestUtil.logErr("Expected: This is message4 with just arg hello.");
        TestUtil.logErr("Got: " + string);
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("ResourcesTest failed", e);
    }

    if (!pass)
      throw new Fault("ResourcesTest failed");
  }

  /*
   * @testName: StructMapTest
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void StructMapTest() throws Fault {
    TestUtil.logMsg("StructMapTest");
    boolean pass = true;
    try {
      TestUtil.logMsg("Call StructMap() constructor");
      StructMap sm = new StructMap();
      sm.put("Key1", "Value1");
      sm.put("Key2", "Value2");
      sm.put("Key3", "Value3");
      TestUtil.logMsg("Call StructMap.size() method");
      int size = sm.size();
      TestUtil.logMsg("Call StructMap.isEmpty() method");
      boolean empty = sm.isEmpty();
      TestUtil.logMsg("Call StructMap.get(Object) method");
      Object obj = sm.get("Key1");
      TestUtil.logMsg("Call StructMap.containsKey(Object) method");
      if (!sm.containsKey("Key1")) {
        TestUtil.logErr("Returned false but expected true");
        pass = false;
      }
      if (sm.containsKey("BadKey")) {
        TestUtil.logErr("Returned true but expected false");
        pass = false;
      }
      TestUtil.logMsg("Call StructMap.containsValue(Object) method");
      if (!sm.containsValue("Value1")) {
        TestUtil.logErr("Returned false but expected true");
        pass = false;
      }
      if (sm.containsValue("BadValue")) {
        TestUtil.logErr("Returned true but expected false");
        pass = false;
      }
      TestUtil.logMsg("Call StructMap.put(Object, Object) method");
      sm.put("Key4", "Value4");
      TestUtil.logMsg("Call StructMap.putAll(Map) method");
      StructMap sm2 = new StructMap();
      sm2.put("Key5", "Value5");
      sm2.put("Key6", "Value6");
      sm2.put("Key7", "Value7");
      sm.putAll(sm2);
      TestUtil.logMsg("Call StructMap.equals(Object) method");
      if (sm.equals("boo hoo")) {
        TestUtil.logErr("Returned true but expected false");
        pass = false;
      }
      TestUtil.logMsg("Call StructMap.hashcode() method");
      int hashcode = sm.hashCode();
      TestUtil.logMsg("Call StructMap.remove(Object) method");
      sm.remove("Key7");
      TestUtil.logMsg("Call StructMap.clear() method");
      sm.clear();
      sm = new StructMap();
      sm2 = new StructMap();
      sm2.put("Key1", "Value1");
      sm2.put("Key2", "Value2");
      sm2.put("Key3", "Value3");
      sm2.put("Key4", "Value4");
      sm2.put("Key5", "Value5");
      sm.putAll(sm2);
      TestUtil.logMsg("Call StructMap.keySet() method");
      Set keyset = sm.keySet();
      TestUtil.logMsg("Call Set.size() method");
      size = keyset.size();
      TestUtil.logMsg("Call Set.contains(Object) method");
      boolean b = keyset.contains("Key1");
      TestUtil.logMsg("Call Set.remove(Object) method");
      b = keyset.remove("Key1");
      TestUtil.logMsg("Call Set.iterator() method");
      Iterator keyiterator = keyset.iterator();
      TestUtil.logMsg("Call Iterator.hasNext() method");
      while (keyiterator.hasNext()) {
        TestUtil.logMsg("Call Iterator.next() method");
        obj = keyiterator.next();
        TestUtil.logMsg("Call Iterator.remove() method");
        keyiterator.remove();
      }
      TestUtil.logMsg("Call Set.clear() method");
      keyset.clear();
      sm.clear();
      sm = new StructMap();
      sm2 = new StructMap();
      sm2.put("Key1", "Value1");
      sm2.put("Key2", "Value2");
      sm2.put("Key3", "Value3");
      sm2.put("Key4", "Value4");
      sm2.put("Key5", "Value5");
      sm.putAll(sm2);
      TestUtil.logMsg("Call StructMap.keys() method");
      Collection collection = sm.keys();
      TestUtil.logMsg("Call StructMap.values() method");
      collection = sm.values();
      TestUtil.logMsg("Call Collection.size() method");
      size = collection.size();
      TestUtil.logMsg("Call Collection.contains(Object) method");
      b = collection.contains("Key1");
      try {
        TestUtil.logMsg("Call Collection.remove(Object) method");
        b = collection.remove("Key1");
      } catch (UnsupportedOperationException e) {
      }
      TestUtil.logMsg("Call Collection.iterator() method");
      Iterator valueiterator = collection.iterator();
      TestUtil.logMsg("Call Iterator.hasNext() method");
      while (valueiterator.hasNext()) {
        TestUtil.logMsg("Call Iterator.next() method");
        obj = valueiterator.next();
        try {
          TestUtil.logMsg("Call Iterator.remove() method");
          valueiterator.remove();
        } catch (UnsupportedOperationException e) {
        }
      }
      TestUtil.logMsg("Call Collections.clear() method");
      try {
        collection.clear();
      } catch (UnsupportedOperationException e) {
      }
      sm.clear();
      sm = new StructMap();
      sm2 = new StructMap();
      sm2.put("Key1", "Value1");
      sm2.put("Key2", "Value2");
      sm2.put("Key3", "Value3");
      sm2.put("Key4", "Value4");
      sm2.put("Key5", "Value5");
      sm.putAll(sm2);
      TestUtil.logMsg("Call StructMap.entrySet() method");
      Set entryset = sm.entrySet();
      TestUtil.logMsg("Call Set.size() method");
      size = entryset.size();
      TestUtil.logMsg("Call Set.toArray() method");
      Object objarray[] = entryset.toArray();
      TestUtil.logMsg("Call Set.contains(Object) method");
      b = entryset.contains("Key1");
      TestUtil.logMsg("Call Set.remove(Object) method");
      b = entryset.remove("Key1");
      TestUtil.logMsg("Call Set.iterator() method");
      Iterator entryiterator = entryset.iterator();
      TestUtil.logMsg("Call Iterator.hasNext() method");
      while (entryiterator.hasNext()) {
        TestUtil.logMsg("Call Iterator.next() method");
        obj = entryiterator.next();
        TestUtil.logMsg("Call Iterator.remove() method");
        entryiterator.remove();
      }
      TestUtil.logMsg("Call Set.clear() method");
      entryset.clear();
      sm.clear();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("StructMapTest failed", e);
    }

    if (!pass)
      throw new Fault("StructMapTest failed");
  }

  /*
   * @testName: ToolBaseTest
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void ToolBaseTest() throws Fault {
    TestUtil.logMsg("ToolBaseTest");
    boolean pass = true;
    try {
      TestUtil.logMsg("Create a ToolBase object");
      PrintStream saveOutStream = System.out;
      System.setOut(new PrintStream(new ByteArrayOutputStream()));
      MyToolBase mtb = new MyToolBase(System.out);
      TestUtil.logMsg("Call ToolBase.wasSuccessful() method");
      boolean b = mtb.wasSuccessful();
      TestUtil.logMsg("Call ToolBase.getString(String, String) method");
      Localizable l = mtb.getMessage("message4", "hello");
      Object args[] = l.getArguments();
      TestUtil
          .logMsg("Call ToolBase.getMessage(String, String, String) method");
      l = mtb.getMessage("message3", "value1", "value2");
      args = l.getArguments();
      TestUtil.logMsg(
          "Call ToolBase.getMessage(String, String, String, String) method");
      l = mtb.getMessage("message5", "Hey you", "get lost", "you fool");
      args = l.getArguments();
      Object[] myargs = { "value1", "value2" };
      TestUtil.logMsg("Call ToolBase.getMessage(String, Object[]) method");
      l = mtb.getMessage("message3", myargs);
      args = l.getArguments();
      Exception ex = new Exception("This is an Exception!");
      TestUtil.logMsg("Call ToolBase.printStackTrace(Throwable) method");
      mtb.printStackTrace(ex);
      System.setOut(saveOutStream);
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("ToolBaseTest failed", e);
    }

    if (!pass)
      throw new Fault("ToolBaseTest failed");
  }
}
