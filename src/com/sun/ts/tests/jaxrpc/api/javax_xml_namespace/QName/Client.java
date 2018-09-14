/*
 * Copyright (c) 2007, 2018 Oracle and/or its affiliates. All rights reserved.
 * Copyright (c) 2002 International Business Machines Corp. All rights reserved.
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

package com.sun.ts.tests.jaxrpc.api.javax_xml_namespace.QName;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import java.io.*;
import java.net.*;
import java.util.*;
import java.rmi.*;

import javax.xml.namespace.QName;

import com.sun.javatest.Status;

public class Client extends ServiceEETest {
  private Properties props = null;

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /* Test setup */

  /*
   * @class.testArgs: -ap jaxrpc-url-props.dat
   * 
   * @class.setup_props:
   */

  public void setup(String[] args, Properties p) throws Fault {
    logMsg("setup ok");
  }

  public void cleanup() throws Fault {
    logMsg("cleanup ok");
  }

  /*
   * @testName: QNameConstructorTest1
   *
   * @assertion_ids: JAXRPC:JAVADOC:231; WS4EE:SPEC:70
   *
   * @test_Strategy: Create instance via QName(String) constructor. Verify QName
   * object created successfully.
   */
  public void QNameConstructorTest1() throws Fault {
    TestUtil.logTrace("QNameConstructorTest1");
    boolean pass = true;
    try {
      TestUtil.logMsg("Create instance via QName(String localPart) ...");
      QName n = new QName("localPart");
      if (n != null) {
        TestUtil.logMsg("QName object created successfully");
      } else {
        TestUtil.logErr("QName object not created");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("QNameConstructorTest1 failed", e);
    }

    if (!pass)
      throw new Fault("QNameConstructorTest1 failed");
  }

  /*
   * @testName: QNameConstructorTest2
   *
   * @assertion_ids: JAXRPC:JAVADOC:232; WS4EE:SPEC:70
   *
   * @test_Strategy: Create instance via QName(String, String) constructor.
   * Verify QName object created successfully.
   */
  public void QNameConstructorTest2() throws Fault {
    TestUtil.logTrace("QNameConstructorTest2");
    boolean pass = true;
    try {
      TestUtil.logMsg(
          "Create instance via QName(String namespaceURI, String localPart) ...");
      QName n = new QName("http://foo.bar.com", "localPart");
      if (n != null) {
        TestUtil.logMsg("QName object created successfully");
      } else {
        TestUtil.logErr("QName object not created");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("QNameConstructorTest2 failed", e);
    }

    if (!pass)
      throw new Fault("QNameConstructorTest2 failed");
  }

  /*
   * @testName: getLocalPartTest
   *
   * @assertion_ids: JAXRPC:JAVADOC:234; WS4EE:SPEC:70
   *
   * @test_Strategy: Create instance via QName(String, String) constructor.
   * Verify Local part if equal to what was set.
   */
  public void getLocalPartTest() throws Fault {
    TestUtil.logTrace("getLocalPartTest");
    String localPart = "localPart";
    boolean pass = true;
    try {
      TestUtil.logMsg(
          "Create instance via QName(String namespaceURI, String localPart) ...");
      QName n = new QName("http://foo.bar.com", "localPart");
      if (n != null) {
        String s = n.getLocalPart();
        if (s.equals(localPart))
          TestUtil.logMsg("Local Part received as expected: " + localPart);
        else {
          TestUtil.logErr(
              "Local Part: expected - " + localPart + ", received - " + s);
          pass = false;
        }
      } else {
        TestUtil.logErr("QName object not created");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("getLocalPartTest failed", e);
    }

    if (!pass)
      throw new Fault("getLocalPartTest failed");
  }

  /*
   * @testName: getNamespaceURITest
   *
   * @assertion_ids: JAXRPC:JAVADOC:233; WS4EE:SPEC:70
   *
   * @test_Strategy: Create instance via QName(String, String) constructor.
   * Verify Namespace URI if equal to what was set.
   */
  public void getNamespaceURITest() throws Fault {
    TestUtil.logTrace("getNamespaceURITest");
    String namespaceURI = "http://foo.bar.com";
    boolean pass = true;
    try {
      TestUtil.logMsg(
          "Create instance via QName(String namespaceURI, String localPart) ...");
      QName n = new QName("http://foo.bar.com", "localPart");
      if (n != null) {
        String s = n.getNamespaceURI();
        if (s.equals(namespaceURI))
          TestUtil.logMsg("Local Part received as expected: " + namespaceURI);
        else {
          TestUtil.logErr(
              "Local Part: expected - " + namespaceURI + ", received - " + s);
          pass = false;
        }
      } else {
        TestUtil.logErr("QName object not created");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("getNamespaceURITest failed", e);
    }

    if (!pass)
      throw new Fault("getNamespaceURITest failed");
  }

  /*
   * @testName: equalsTest
   *
   * @assertion_ids: JAXRPC:JAVADOC:238; WS4EE:SPEC:70
   *
   * @test_Strategy: Test for both equality same QName instances and
   * non-equality of different QName instances.
   */
  public void equalsTest() throws Fault {
    TestUtil.logTrace("equalsTest");
    boolean pass = true;

    if (!equalsTest1())
      pass = false;
    if (!equalsTest2())
      pass = false;

    if (!pass)
      throw new Fault("equalsTest failed");
  }

  /*
   * Test for equality of same QName instances.
   */
  private boolean equalsTest1() {
    TestUtil.logTrace("equalsTest1");
    boolean pass = true;
    try {
      TestUtil.logMsg(
          "Create instance via QName(String namespaceURI, String localPart) ...");
      QName n = new QName("http://foo.bar.com", "localPart");
      if (n != null) {
        if (n.equals(n))
          TestUtil.logMsg("QName objects are equal - expected");
        else {
          TestUtil.logErr("QName objects are not equal - unexpected");
          pass = false;
        }
      } else {
        TestUtil.logErr("QName object not created");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    return pass;
  }

  /*
   * Test for non-equality of different QName instances.
   */
  private boolean equalsTest2() {
    TestUtil.logTrace("equalsTest2");
    boolean pass = true;
    try {
      TestUtil.logMsg(
          "Create instance via QName(String namespaceURI, String localPart) ...");
      QName n1 = new QName("http://foo.bar.com", "localPart1");
      QName n2 = new QName("http://foo.bar.com", "localPart2");
      if (n1 != null && n2 != null) {
        if (!n1.equals(n2))
          TestUtil.logMsg("QName objects are not equal - expected");
        else {
          TestUtil.logErr("QName objects are equal - unexpected");
          pass = false;
        }
      } else {
        TestUtil.logErr("QName object not created");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }

    return pass;
  }

  /*
   * @testName: hashCodeTest
   *
   * @assertion_ids: JAXRPC:JAVADOC:237; WS4EE:SPEC:70;
   *
   * @test_Strategy: Test for hashCode returned for same QName objects are
   * identical and hashCode returned for different QName objects are not
   * identical.
   */
  public void hashCodeTest() throws Fault {
    TestUtil.logTrace("hashCodeTest");
    boolean pass = true;

    if (!hashCodeTest1())
      pass = false;
    if (!hashCodeTest2())
      pass = false;

    if (!pass)
      throw new Fault("hashCodeTest failed");
  }

  /*
   * The hashCode returned for same QName objects should be identical
   */
  private boolean hashCodeTest1() {
    TestUtil.logTrace("hashCodeTest1");
    boolean pass = true;
    try {
      TestUtil.logMsg(
          "Create instance via QName(String namespaceURI, String localPart) ...");
      QName n1 = new QName("http://foo.bar.com", "localPart");
      QName n2 = new QName("http://foo.bar.com", "localPart");
      if (n1 != null) {
        int hashCode1 = n1.hashCode();
        int hashCode2 = n2.hashCode();
        if (hashCode1 == hashCode2)
          TestUtil.logMsg("QName objects hashCodes are equal - expected");
        else {
          TestUtil.logErr("QName objects hashCodes are not equal - unexpected");
          pass = false;
        }
      } else {
        TestUtil.logErr("QName object not created");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    return pass;
  }

  /*
   * The hashCode returned for different QName objects should not be identical
   */
  private boolean hashCodeTest2() {
    TestUtil.logTrace("hashCodeTest2");
    boolean pass = true;
    try {
      TestUtil.logMsg(
          "Create instance via QName(String namespaceURI, String localPart) ...");
      QName n1 = new QName("http://foo.bar.com", "localPart1");
      QName n2 = new QName("http://foo.bar.com", "localPart2");
      if (n1 != null && n2 != null) {
        int hashCode1 = n1.hashCode();
        int hashCode2 = n2.hashCode();
        if (hashCode1 != hashCode2)
          TestUtil.logMsg("QName objects hashCodes are not equal - expected");
        else {
          TestUtil.logErr("QName objects hashCodes are equal - unexpected");
          pass = false;
        }
      } else {
        TestUtil.logErr("QName object not created");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    return pass;
  }

  /*
   * @testName: toStringTest
   *
   * @assertion_ids: JAXRPC:JAVADOC:235; WS4EE:SPEC:70;
   *
   * @test_Strategy: Create instance via QName(String, String) constructor. Call
   * toString() method to return String representation.
   */
  public void toStringTest() throws Fault {
    TestUtil.logTrace("toStringTest");
    boolean pass = true;
    try {
      TestUtil.logMsg(
          "Create instance via QName(String namespaceURI, String localPart) ...");
      QName n = new QName("http://foo.bar.com", "localPart");
      if (n != null) {
        String s = n.toString();
        TestUtil.logMsg("String representation of QName is: " + s);
      } else {
        TestUtil.logErr("QName object not created");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("toStringTest failed", e);
    }

    if (!pass)
      throw new Fault("toStringTest failed");
  }
}
