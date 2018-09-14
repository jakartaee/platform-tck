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

package com.sun.ts.tests.interop.rmiiiop.marshaltests;

import com.sun.ts.tests.rmiiiop.ee.marshaltests.*;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import com.sun.javatest.Status;
import java.math.*;

import java.util.*;
import java.rmi.*;
import javax.ejb.*;
import java.io.*;

public class Client extends ServiceEETest implements Serializable {
  private Properties testProps = null;

  private Properties beanProps = null;

  private static final String tBeanLookup = "java:comp/env/ejb/TestBeanEJB";

  private TSNamingContext nctx = null;

  private TestBean tBeanRef = null;

  private TestBeanHome tBeanHome = null;

  private CallBackBean cBeanRefs[] = new CallBackBean[Data.MAXOBJETS];

  public static void main(String[] args) {
    Client client = new Client();
    Status s = client.run(args, System.out, System.err);
    s.exit();
  }

  /*
   * @class.setup_props: org.omg.CORBA.ORBClass; java.naming.factory.initial;
   */

  public void setup(String[] args, Properties p) throws Fault {
    MyUtil.logTrace("Client.setup()");
    try {
      MyUtil.logTrace("Obtain Naming Context");
      this.nctx = new TSNamingContext();
      MyUtil.logMsg("Lookup the EJBHome of RMIIIOPTestBean: " + tBeanLookup);
      tBeanHome = (TestBeanHome) nctx.lookup(tBeanLookup, TestBeanHome.class);
      MyUtil.logMsg("Create an EJBObject instance of RMIIIOPTestBean");
      tBeanRef = tBeanHome.create(p);
    } catch (Exception e) {
      MyUtil.logErr("Exception: " + e.getMessage());
      MyUtil.printStackTrace(e);
      throw new Fault("Setup failed:", e);
    }
  }

  /* Test cleanup */

  public void cleanup() throws Fault {
    MyUtil.logMsg("Cleanup ok");
  }

  /* Run test */

  /*
   * @testName: PrimTest01
   * 
   * @assertion_ids: RMIIIOP:SPEC:1000;
   * 
   * @assertion: All of the standard Java primitive types are supported as part
   * of RMI/IDL for RMI-IIOP. These are: void, boolean, byte, char, short, int,
   * long, float, double. All of these Java primitives can be transmitted across
   * an RMI/IDL remote interface at run time. (Chapter 1.2.2 Java Language to
   * IDL Mapping) (Chapter 19 EJB Specification [See EJB assertions])
   * 
   * @test_Strategy: Test passing Java primitives as arguments. Test returning
   * Java primitives as return values.
   *
   */

  public void PrimTest01() throws Fault {
    String testname = "PrimTest01";
    int failures = 0;

    try {
      MyUtil.logTrace(testname);
      failures += pass_a_boolean();
      failures += pass_a_byte();
      failures += pass_a_char();
      failures += pass_a_short();
      failures += pass_a_int();
      failures += pass_a_long();
      failures += pass_a_float();
      failures += pass_a_double();
      MyUtil.logSubTestResultSummary();
      if (failures > 0)
        throw new Fault(testname + " failed");
    } catch (Exception e) {
      throw new Fault(testname + " failed", e);
    }
  }

  /*
   * @testName: PrimTest02
   * 
   * @assertion_ids: RMIIIOP:SPEC:1000; RMIIIOP:SPEC:1005;
   * 
   * @assertion: Arrays of any conforming RMI/IDL are also conforming RMI/IDL
   * types. Arrays of Java primitives can be transmitted across and RMI/IDL
   * remote interface at run time. (Chapter 1.2.2/1.2.5 Java Language to IDL
   * Mapping) (Chapter 19 EJB Specification [See EJB assertions])
   * 
   * @test_Strategy: Test passing arrays of Java primitives as arguments. Test
   * returning arrays of Java primitives as return values.
   *
   */
  public void PrimTest02() throws Fault {

    String testname = "PrimTest02";
    int failures = 0;

    try {
      MyUtil.logTrace(testname);
      failures += pass_a_boolean_array();
      failures += pass_a_byte_array();
      failures += pass_a_char_array();
      failures += pass_a_short_array();
      failures += pass_a_int_array();
      failures += pass_a_long_array();
      failures += pass_a_float_array();
      failures += pass_a_double_array();
      MyUtil.logSubTestResultSummary();
      if (failures > 0)
        throw new Fault(testname + " failed");
    } catch (Exception e) {
      throw new Fault(testname + " failed", e);
    }
  }

  /*
   * @testName: EXTest01
   * 
   * @assertion_ids: RMIIIOP:SPEC:1004;
   * 
   * @assertion: An RMI/IDL exception type inherits from java.lang. Exception. A
   * user should be able to throw any exception that inherits from
   * java.lang.Exception in RMI-IIOP. A user defined exception will be defined
   * in a remote interface and the throwing of this user defined exception will
   * be tested under RMI-IIOP. A conforming RMI/IDL exception class must have
   * the following:
   *
   * (1) The class must directly or indirectly inherit java. lang.Exception.
   *
   * (2) The class must meet the requirements of the RMI/IDL value types.
   * (Chapter 1.2.6 Java Language to IDL Mapping) (Chapter 19 EJB Specification
   * [See EJB assertions])
   * 
   * @test_Strategy: Test throwing a user defined exception.
   *
   */

  public void EXTest01() throws Fault {
    String testname = "EXTest01";
    int failures = 0;

    try {
      MyUtil.logTrace(testname);
      failures += throw_a_user_exception();
      if (failures > 0)
        throw new Fault(testname + " failed");
    } catch (Exception e) {
      throw new Fault(testname + " failed", e);
    }
  }

  /*
   * @testName: EXTest02a
   * 
   * @assertion_ids: RMIIIOP:SPEC:1004;
   * 
   * @assertion: An RMI/IDL exception type inherits from java.lang. Exception.
   * System and runtime exceptions are also RMI/IDL exception types. Throw an
   * EJBException. The container should catch the exception, log it, and then
   * throw a RemoteException back to the client. A conforming RMI/IDL exception
   * class must have the following:
   *
   * (1) The class must directly or indirectly inherit java. lang.Exception.
   *
   * (2) The class must meet the requirements of the RMI/IDL value types.
   * (Chapter 1.2.6 Java Language to IDL Mapping) (Chapter 19 EJB Specification
   * [See EJB assertions])
   * 
   * @test_Strategy: Test throwing system and runtime exceptions. The container
   * should catch the exception, log it, and then throw a RemoteException back
   * to client.
   *
   */

  public void EXTest02a() throws Fault {
    String testname = "EXTest02a";
    int failures = 0;

    try {
      MyUtil.logTrace(testname);
      failures += throw_exception("EJBException");
      if (failures > 0)
        throw new Fault(testname + " failed");
    } catch (Exception e) {
      throw new Fault(testname + " failed", e);
    }
  }

  /*
   * @testName: EXTest02b
   * 
   * @assertion_ids: RMIIIOP:SPEC:1004;
   * 
   * @assertion: An RMI/IDL exception type inherits from java.lang. Exception.
   * System and runtime exceptions are also RMI/IDL exception types. Throw a
   * NullPointerException. The container should catch the exception, log it, and
   * then throw a RemoteException back to the client. A conforming RMI/IDL
   * exception class must have the following:
   *
   * (1) The class must directly or indirectly inherit java. lang.Exception.
   *
   * (2) The class must meet the requirements of the RMI/IDL value types.
   * (Chapter 1.2.6 Java Language to IDL Mapping) (Chapter 19 EJB Specification
   * [See EJB assertions])
   * 
   * @test_Strategy: Test throwing system and runtime exceptions. The container
   * should catch the exception, log it, and then throw a RemoteException back
   * to client.
   *
   */

  public void EXTest02b() throws Fault {
    String testname = "EXTest02b";
    int failures = 0;

    try {
      MyUtil.logTrace(testname);
      failures += throw_exception("NullPointerException");
      if (failures > 0)
        throw new Fault(testname + " failed");
    } catch (Exception e) {
      throw new Fault(testname + " failed", e);
    }
  }

  /*
   * @testName: EXTest02c
   * 
   * @assertion_ids: RMIIIOP:SPEC:1004;
   * 
   * @assertion: An RMI/IDL exception type inherits from java.lang. Exception.
   * System and runtime exceptions are also RMI/IDL exception types. Throw an
   * Error. The container should catch the exception, log it, and then throw a
   * RemoteException back to the client. A conforming RMI/IDL exception class
   * must have the following:
   *
   * (1) The class must directly or indirectly inherit java. lang.Exception.
   *
   * (2) The class must meet the requirements of the RMI/IDL value types.
   * (Chapter 1.2.6 Java Language to IDL Mapping) (Chapter 19 EJB Specification
   * [See EJB assertions])
   * 
   * @test_Strategy: Test throwing system and runtime exceptions. The container
   * should catch the exception, log it, and then throw a RemoteException back
   * to client.
   *
   */

  public void EXTest02c() throws Fault {
    String testname = "EXTest02c";
    int failures = 0;

    try {
      MyUtil.logTrace(testname);
      failures += throw_exception("Error");
      if (failures > 0)
        throw new Fault(testname + " failed");
    } catch (Exception e) {
      throw new Fault(testname + " failed", e);
    }
  }

  /*
   * @testName: RITest01
   * 
   * @assertion_ids: RMIIIOP:SPEC:1001;
   * 
   * @assertion: An RMI remote interface defines a Java interface that can be
   * invoked remotely. A Java interface is a conforming RMI/IDL remote interface
   * if:
   *
   * (1) The interface inherits from java.rmi.Remote either directly or
   * indirectly.
   *
   * (2) The interface and interfaces that it extends (either directly or
   * indirectly) are public.
   *
   * (3) All methods in the interface must throw java.rmi.RemoteException.
   *
   * (4) Method arguments and result types can be of any types.
   *
   * (5) All specified method exceptions other than java.rmi.RemoteException are
   * conforming RMI/IDL exception types.
   *
   * (6) Method names may be overloaded. However, when an interface directly
   * inherits from several base interfaces, it is forbidden for there to be
   * method name conflicts between inherited interfaces.
   *
   * (7) Constant definitions in the form of interface variables are permitted.
   *
   * When passing a remote interface as an argument in a method call or returing
   * a remote interface as a return value of a method call the remote interface
   * is passed and returned by reference. The class of the actual object that is
   * passed or returned must be either the stub class or the tie class of the
   * remote interface. (Chapter 1.2.3 Java Language to IDL Mapping) (Chapter 19
   * EJB Specification [See EJB assertions])
   * 
   * @test_Strategy: Test passing a remote interface as an argument and verify
   * on the server side that you can call one of its methods. Test returning a
   * remote interface as a return value and verify on the client side that you
   * can call one of its methods.
   */

  public void RITest01() throws Fault {
    String testname = "RITest01";
    int failures = 0;

    try {
      MyUtil.logTrace(testname);
      failures += return_a_remote_interface();
      failures += pass_a_remote_interface();
      failures += return_a_remote_interface();
      if (failures > 0)
        throw new Fault(testname + " failed");
    } catch (Exception e) {
      throw new Fault(testname + " failed", e);
    }
  }

  /*
   * @testName: RITest02
   * 
   * @assertion_ids: RMIIIOP:SPEC:1001; RMIIIOP:SPEC:1003;
   * 
   * @assertion: Arrays of any conforming RMI/IDL are also conforming RMI/IDL
   * types. Arrays of remote interfaces can be transmitted across and RMI/IDL
   * remote interface at run time. (Chapter 1.2.3/1.2.5 Java Language to IDL
   * Mapping) (Chapter 19 EJB Specification [See EJB assertions])
   * 
   * @test_Strategy: Test passing arrays of remote interfaces as arguments.
   * Verify on the server side that you can call one of the methods of each
   * remote interface. Test returning arrays of remote references as return
   * values. Verify on the client side that you can call one of the methods of
   * each remote interface.
   */

  public void RITest02() throws Fault {
    String testname = "RITest02";
    int failures = 0;

    try {
      MyUtil.logTrace(testname);
      failures += return_array_of_remote_interfaces();
      failures += pass_array_of_remote_interfaces();
      failures += return_array_of_remote_interfaces();
      if (failures > 0)
        throw new Fault(testname + " failed");
    } catch (Exception e) {
      throw new Fault(testname + " failed", e);
    }
  }

  /*
   * @testName: OBVTest01
   * 
   * @assertion_ids: RMIIIOP:SPEC:1002;
   * 
   * @assertion: Passing of objects by value is allowed in RMI-IIOP. An RMI/IDL
   * value type represents a class whose values can be moved between systems. So
   * rather than transmitting a reference between systems, the actual state of
   * the object is transmitted between systems. This requires that the receiving
   * system have an analogous class that can be used to hold the received value.
   * A Java class is a conforming RMI/IDL value type if:
   *
   * (1) The class must directly or indirectly implement the
   * java.io.Serializable interface.
   *
   * (2) The class may implement the java.io.Externalizable.
   *
   * (3) The class and its superclasses are public.
   *
   * (4) If the class is an inner class, then its containg class must also be a
   * conforming RMI/IDL value type.
   *
   * (5) A value type must not either directly or indirectly implement the
   * java.rmi.Remote interface.
   *
   * (6) A value type may implement any public interface except for
   * java.rmi.Remote.
   *
   * (7) There are no restrictions on method signatures for a value type.
   *
   * (8) There are no restrictions on static fields for a value type.
   *
   * (9) There are no restrictions on transient fields for a value type.
   *
   * (Chapter 1.2.4 Java Language to IDL Mapping) (Chapter 19 EJB Specification
   * [See EJB assertions])
   * 
   * @test_Strategy: Test passing objects by value as arguments. Test returning
   * objects by value as return values.
   *
   */

  public void OBVTest01() throws Fault {
    String testname = "OBVTest01";
    int failures = 0;

    try {
      MyUtil.logTrace(testname);
      failures += pass_a_date_object();
      failures += pass_any_object();
      failures += return_a_date_object();
      failures += return_any_object();
      failures += pass_return_a_string_object();
      failures += pass_return_a_class_object();
      failures += pass_return_a_vector_object();
      failures += pass_return_a_hashtable_object();
      if (failures > 0)
        throw new Fault(testname + " failed");
    } catch (Exception e) {
      throw new Fault(testname + " failed", e);
    }
  }

  /*
   * @testName: OBVTest02
   * 
   * @assertion_ids: RMIIIOP:SPEC:1002; RMIIIOP:SPEC:1003;
   * 
   * @assertion: Arrays of any conforming RMI/IDL are also conforming RMI/IDL
   * types. Arrays of objects by value can be transmitted across and RMI/IDL
   * remote interface at run time. (Chapter 1.2.4/1.2.5 Java Language to IDL
   * Mapping) (Chapter 19 EJB Specification [See EJB assertions])
   * 
   * @test_Strategy: Test passing arrays of objects by value as arguments. Test
   * returning arrays of objects by value as return values.
   *
   */

  public void OBVTest02() throws Fault {
    String testname = "OBVTest02";
    int failures = 0;

    try {
      MyUtil.logTrace(testname);
      failures += pass_a_date_object_array();
      failures += pass_any_object_array();
      failures += return_a_date_object_array();
      failures += return_any_object_array();
      failures += pass_return_string_object_array();
      failures += pass_return_class_object_array();
      if (failures > 0)
        throw new Fault(testname + " failed");
    } catch (Exception e) {
      throw new Fault(testname + " failed", e);
    }
  }

  /*
   * @testName: OBVTest03
   * 
   * @assertion_ids: RMIIIOP:SPEC:1002;
   * 
   * @assertion: Passing a graph of Java objects by value is allowed in
   * RMI-IIOP. An RMI/IDL value type represents a class whose values can be
   * moved between systems. So rather than transmitting a reference between
   * systems, the actual state of the object is transmitted between systems.
   * This requires that the receiving system have an analogous class that can be
   * used to hold the received value. When passing or returning a graph of Java
   * objects by value all of the Java objects in the graph are traversed and
   * transmitted across an RMI/IDL remote interface at run time.
   * 
   * Tests CORBA type code compatibility by sending a class with all basic field
   * types (including a recursive field) into a remote method taking a
   * Serializable as a parameter. It should map to a CORBA Any on the wire in
   * RMI-IIOP.
   *
   * (Chapter 1.2.4 Java Language to IDL Mapping) (Chapter 19 EJB Specification
   * [See EJB assertions])
   *
   * @test_Strategy: Test passing a graph of objects by value as an argument and
   * verify that the graph of objects is correct. Test returning a graph of
   * objects by value as a return value and verify that the graph of objects is
   * correct.
   *
   * Test passing a Java objects with all basic field types (including a
   * recursive field) by value as an argument and verify the Java Objects
   * returned is correct.
   */

  public void OBVTest03() throws Fault {
    String testname = "OBVTest03";
    int failures = 0;

    try {
      MyUtil.logTrace(testname);
      failures += pass_a_graph_of_objects();
      failures += return_a_graph_of_objects();
      failures += pass_return_serializable_object();
      if (failures > 0)
        throw new Fault(testname + " failed");
    } catch (Exception e) {
      throw new Fault(testname + " failed", e);
    }
  }

  /*
   * @testName: OBVTest04
   * 
   * @assertion_ids: RMIIIOP:SPEC:1002; RMIIIOP:SPEC:1003;
   * 
   * @assertion: Arrays of any conforming RMI/IDL are also conforming RMI/IDL
   * types. Arrays of Java graph objects can be transmitted across and RMI/IDL
   * remote interface at run time. (Chapter 1.2.4/1.2.5 Java Language to IDL
   * Mapping) (Chapter 19 EJB Specification [See EJB assertions])
   * 
   * @test_Strategy: Test passing an array of graph objects by value as an
   * argument and verify that the array of graph objects is correct. Test
   * returning an array of graph objects by value as a return value and verify
   * that the array of graph objects is correct.
   *
   */

  public void OBVTest04() throws Fault {
    String testname = "OBVTest04";
    int failures = 0;

    try {
      MyUtil.logTrace(testname);
      failures += pass_array_of_graph_objects();
      failures += return_array_of_graph_objects();
      if (failures > 0)
        throw new Fault(testname + " failed");
    } catch (Exception e) {
      throw new Fault(testname + " failed", e);
    }
  }

  /*
   * @testName: OBVTest05
   * 
   * @assertion_ids: RMIIIOP:SPEC:1002; RMIIIOP:SPEC:1003;
   * 
   * @assertion: Arrays of any conforming RMI/IDL are also conforming RMI/IDL
   * types. Arrays of objects by value can be transmitted across and RMI/IDL
   * remote interface at run time. (Chapter 1.2.4/1.2.5 Java Language to IDL
   * Mapping) (Chapter 19 EJB Specification [See EJB assertions])
   * 
   * @test_Strategy: Test passing and returning a three dimensional array of
   * objects by value and verify that the returned three dimensional array of
   * objects matches the three dimensional array of objects that was passed by
   * value.
   *
   */

  public void OBVTest05() throws Fault {
    String testname = "OBVTest05";
    int failures = 0;

    try {
      MyUtil.logTrace(testname);
      failures += pass_return_object3_array(5);
      if (failures > 0)
        throw new Fault(testname + " failed");
    } catch (Exception e) {
      throw new Fault(testname + " failed", e);
    }
  }

  /*
   * @testName: MultiTest01
   * 
   * @assertion_ids: RMIIIOP:SPEC:1002;
   * 
   * @assertion: Multi class types can be transmitted across and RMI/IDL remote
   * interface at run time. (Chapter 1.2.4 Java Language to IDL Mapping)
   * (Chapter 19 EJB Specification [See EJB assertions])
   * 
   * @test_Strategy: Test passing and returning multi class types.
   *
   */

  public void MultiTest01() throws Fault {
    String testname = "MultiTest01";
    int failures = 0;

    try {
      MyUtil.logTrace(testname);
      failures += pass_return_multiclass_types();
      if (failures > 0)
        throw new Fault(testname + " failed");
    } catch (Exception e) {
      throw new Fault(testname + " failed", e);
    }
  }

  /*
   * @testName: IDLEntityTest01
   * 
   * @assertion_ids: RMIIIOP:SPEC:1006;
   * 
   * @assertion: IDLEntity types can be transmitted across and RMI/IDL remote
   * interface at run time. (Chapter 1.2.8 Java Language to IDL Mapping)
   * (Chapter 19 EJB Specification [See EJB assertions])
   * 
   * @test_Strategy: Test passing and returning IDLEntity types.
   *
   */

  public void IDLEntityTest01() throws Fault {
    String testname = "IDLEntityTest01";
    int failures = 0;

    try {
      MyUtil.logTrace(testname);
      failures += pass_return_idl_entity_types("struct");
      failures += pass_return_idl_entity_types("sequence");
      if (failures > 0)
        throw new Fault(testname + " failed");
    } catch (Exception e) {
      throw new Fault(testname + " failed", e);
    }
  }

  /*
   * @testName: RecursiveTest01
   * 
   * @assertion_ids: RMIIIOP:SPEC:1002;
   * 
   * @assertion: Passing Java objects with recursive references by value is
   * allowed in RMI-IIOP. An RMI/IDL value type represents a class whose values
   * can be moved between systems. So rather than transmitting a reference
   * between systems, the actual state of the object is transmitted between
   * systems. This requires that the receiving system have an analogous class
   * that can be used to hold the received value. (Chapter 1.2.4 Java Language
   * to IDL Mapping) (Chapter 19 EJB Specification [See EJB assertions])
   * 
   * @test_Strategy: Test passing Java objects with recursive references by
   * value as an argument and verify the Java Objects returned is correct.
   *
   */

  public void RecursiveTest01() throws Fault {
    String testname = "RecursiveTest01";
    int failures = 0;

    try {
      MyUtil.logTrace(testname);
      failures += pass_return_recursive_vector_object();
      failures += pass_return_recursive_hashtable_object();
      if (failures > 0)
        throw new Fault(testname + " failed");
    } catch (Exception e) {
      throw new Fault(testname + " failed", e);
    }
  }

  private int pass_a_boolean() {
    String subtestname = "PrimTest01:(pass_a_boolean)";
    int failures = 0;

    MyUtil.logMsg(subtestname);
    try {
      boolean ret = false;
      boolean saveret = false;
      MyUtil.logMsg("Passing/Returning boolean to/from RMIIIOPTestBean");
      for (int i = 0; i < Data.boolean_data.length; i++) {
        ret = tBeanRef.pass_a_boolean(i, Data.boolean_data[i]);
        if (ret != false && ret != true)
          break;
        if (ret)
          saveret = true;
      }
      if (saveret == true)
        ret = saveret;
      if (ret != false && ret != true) {
        MyUtil.displayError("Invalid return", "false or true", "" + ret);
        failures++;
      } else if (ret != false) {
        MyUtil.displayError("Wrong return", "false", "" + ret);
        failures++;
      }
    } catch (Exception e) {
      MyUtil.logErr("Caught exception: " + e.getMessage());
      MyUtil.printStackTrace(e);
      MyUtil.logErr(e.toString());
      failures++;
    }
    if (failures > 0) {
      MyUtil.logMsg(subtestname + "failed");
    } else {
      MyUtil.logMsg(subtestname + "passed");
    }
    MyUtil.logDoneSubTest(subtestname);
    return failures;
  }

  private int pass_a_byte() {
    String subtestname = "PrimTest01:(pass_a_byte)";
    int failures = 0;

    MyUtil.logMsg(subtestname);
    try {
      byte ret = 0;
      byte saveret = 0;
      MyUtil.logMsg("Passing/Returning byte to/from RMIIIOPTestBean");
      for (int i = 0; i < Data.byte_data.length; i++) {
        ret = tBeanRef.pass_a_byte(i, Data.byte_data[i]);
        if (ret != 0 && ret != 1)
          break;
        if (ret == 1)
          saveret = 1;
      }
      if (saveret == 1)
        ret = saveret;
      if (ret != 0 && ret != 1) {
        MyUtil.displayError("Invalid return", "0 or 1", "" + ret);
        failures++;
      } else if (ret != 0) {
        MyUtil.displayError("Wrong return", "0", "" + ret);
        failures++;
      }
    } catch (Exception e) {
      MyUtil.logErr("Caught exception: " + e.getMessage());
      MyUtil.printStackTrace(e);
      MyUtil.logErr(e.toString());
      failures++;
    }
    if (failures > 0) {
      MyUtil.logMsg(subtestname + "failed");
    } else {
      MyUtil.logMsg(subtestname + "passed");
    }
    MyUtil.logDoneSubTest(subtestname);
    return failures;
  }

  private int pass_a_char() {
    String subtestname = "PrimTest01:(pass_a_char)";
    int failures = 0;

    MyUtil.logMsg(subtestname);
    try {
      char ret = '0';
      char saveret = '0';
      MyUtil.logMsg("Passing/Returning char to/from RMIIIOPTestBean");
      for (int i = 0; i < Data.char_data.length; i++) {
        ret = tBeanRef.pass_a_char(i, Data.char_data[i]);
        if (ret != '0' && ret != '1')
          break;
        if (ret == '1')
          saveret = '1';
      }
      if (saveret == '1')
        ret = saveret;
      if (ret != '0' && ret != '1') {
        MyUtil.displayError("Invalid return", "'0' or '1'", "'" + ret + "'");
        failures++;
      } else if (ret != '0') {
        MyUtil.displayError("Wrong return", "'0'", "" + "'" + ret + "'");
        failures++;
      }
    } catch (Exception e) {
      MyUtil.logErr("Caught exception: " + e.getMessage());
      MyUtil.printStackTrace(e);
      MyUtil.logErr(e.toString());
      failures++;
    }
    if (failures > 0) {
      MyUtil.logMsg(subtestname + "failed");
    } else {
      MyUtil.logMsg(subtestname + "passed");
    }
    MyUtil.logDoneSubTest(subtestname);
    return failures;
  }

  private int pass_a_short() {
    String subtestname = "PrimTest01:(pass_a_short)";
    int failures = 0;

    MyUtil.logMsg(subtestname);
    try {
      short ret = 0;
      short saveret = 0;
      MyUtil.logMsg("Passing/Returning short to/from RMIIIOPTestBean");
      for (int i = 0; i < Data.short_data.length; i++) {
        ret = tBeanRef.pass_a_short(i, Data.short_data[i]);
        if (ret != 0 && ret != 1)
          break;
        if (ret == 1)
          saveret = 1;
      }
      if (saveret == 1)
        ret = saveret;
      if (ret != 0 && ret != 1) {
        MyUtil.displayError("Invalid return", "0 or 1", "" + ret);
        failures++;
      } else if (ret != 0) {
        MyUtil.displayError("Wrong return", "0", "" + ret);
        failures++;
      }
    } catch (Exception e) {
      MyUtil.logErr("Caught exception: " + e.getMessage());
      MyUtil.printStackTrace(e);
      MyUtil.logErr(e.toString());
      failures++;
    }
    if (failures > 0) {
      MyUtil.logMsg(subtestname + "failed");
    } else {
      MyUtil.logMsg(subtestname + "passed");
    }
    MyUtil.logDoneSubTest(subtestname);
    return failures;
  }

  private int pass_a_int() {
    String subtestname = "PrimTest01:(pass_a_int)";
    int failures = 0;

    MyUtil.logMsg(subtestname);
    try {
      int ret = 0;
      int saveret = 0;
      MyUtil.logMsg("Passing/Returning int to/from RMIIIOPTestBean");
      for (int i = 0; i < Data.int_data.length; i++) {
        ret = tBeanRef.pass_a_int(i, Data.int_data[i]);
        if (ret != 0 && ret != 1)
          break;
        if (ret == 1)
          saveret = 1;
      }
      if (saveret == 1)
        ret = saveret;
      if (ret != 0 && ret != 1) {
        MyUtil.displayError("Invalid return", "0 or 1", "" + ret);
        failures++;
      } else if (ret != 0) {
        MyUtil.displayError("Wrong return", "0", "" + ret);
        failures++;
      }
    } catch (Exception e) {
      MyUtil.logErr("Caught exception: " + e.getMessage());
      MyUtil.printStackTrace(e);
      MyUtil.logErr(e.toString());
      failures++;
    }
    if (failures > 0) {
      MyUtil.logMsg(subtestname + "failed");
    } else {
      MyUtil.logMsg(subtestname + "passed");
    }
    MyUtil.logDoneSubTest(subtestname);
    return failures;
  }

  private int pass_a_long() {
    String subtestname = "PrimTest01:(pass_a_long)";
    int failures = 0;

    MyUtil.logMsg(subtestname);
    try {
      long ret = 0;
      long saveret = 0;
      MyUtil.logMsg("Passing/Returning long to/from RMIIIOPTestBean");
      for (int i = 0; i < Data.long_data.length; i++) {
        ret = tBeanRef.pass_a_long(i, Data.long_data[i]);
        if (ret != 0 && ret != 1)
          break;
        if (ret == 1)
          saveret = 1;
      }
      if (saveret == 1)
        ret = saveret;
      if (ret != 0 && ret != 1) {
        MyUtil.displayError("Invalid return", "0 or 1", "" + ret);
        failures++;
      } else if (ret != 0) {
        MyUtil.displayError("Wrong return", "0", "" + ret);
        failures++;
      }
    } catch (Exception e) {
      MyUtil.logErr("Caught exception: " + e.getMessage());
      MyUtil.printStackTrace(e);
      MyUtil.logErr(e.toString());
      failures++;
    }
    if (failures > 0) {
      MyUtil.logMsg(subtestname + "failed");
    } else {
      MyUtil.logMsg(subtestname + "passed");
    }
    MyUtil.logDoneSubTest(subtestname);
    return failures;
  }

  private int pass_a_float() {
    String subtestname = "PrimTest01:(pass_a_float)";
    int failures = 0;

    MyUtil.logMsg(subtestname);
    try {
      float ret = (float) 0.0;
      float saveret = (float) 0.0;
      MyUtil.logMsg("Passing/Returning float to/from RMIIIOPTestBean");
      for (int i = 0; i < Data.float_data.length; i++) {
        ret = tBeanRef.pass_a_float(i, Data.float_data[i]);
        if (ret != (float) 0.0 && ret != (float) 1.0)
          break;
        if (ret == (float) 1.0)
          saveret = (float) 1.0;
      }
      if (saveret == (float) 1.0)
        ret = saveret;
      if (ret != (float) 0.0 && ret != (float) 1.0) {
        MyUtil.displayError("Invalid return", "0.0 or 1.0", "" + ret);
        failures++;
      } else if (ret != (float) 0.0) {
        MyUtil.displayError("Wrong return", "0.0", "" + ret);
        failures++;
      }
    } catch (Exception e) {
      MyUtil.logErr("Caught exception: " + e.getMessage());
      MyUtil.printStackTrace(e);
      MyUtil.logErr(e.toString());
      failures++;
    }
    if (failures > 0) {
      MyUtil.logMsg(subtestname + "failed");
    } else {
      MyUtil.logMsg(subtestname + "passed");
    }
    MyUtil.logDoneSubTest(subtestname);
    return failures;
  }

  private int pass_a_double() {
    String subtestname = "PrimTest01:(pass_a_double)";
    int failures = 0;

    MyUtil.logMsg(subtestname);
    try {
      double ret = (double) 0.0;
      double saveret = (double) 0.0;
      MyUtil.logMsg("Passing/Returning double to/from RMIIIOPTestBean");
      for (int i = 0; i < Data.double_data.length; i++) {
        ret = tBeanRef.pass_a_double(i, Data.double_data[i]);
        if (ret != (double) 0.0 && ret != (double) 1.0)
          break;
        if (ret == (double) 1.0)
          saveret = (double) 1.0;
      }
      if (saveret == (double) 1.0)
        ret = saveret;
      if (ret != (double) 0.0 && ret != (double) 1.0) {
        MyUtil.displayError("Invalid return", "0.0 or 1.0", "" + ret);
        failures++;
      } else if (ret != (double) 0.0) {
        MyUtil.displayError("Wrong return", "0.0", "" + ret);
        failures++;
      }
    } catch (Exception e) {
      MyUtil.logErr("Caught exception: " + e.getMessage());
      MyUtil.printStackTrace(e);
      MyUtil.logErr(e.toString());
      failures++;
    }
    if (failures > 0) {
      MyUtil.logMsg(subtestname + "failed");
    } else {
      MyUtil.logMsg(subtestname + "passed");
    }
    MyUtil.logDoneSubTest(subtestname);
    return failures;
  }

  private int pass_a_boolean_array() {
    String subtestname = "PrimTest02:(pass_a_boolean_array)";
    int failures = 0;

    MyUtil.logMsg(subtestname);
    try {
      MyUtil.logMsg("Passing/Returning boolean array to/from RMIIIOPTestBean");
      boolean ret[] = tBeanRef.pass_a_boolean_array(Data.boolean_data);
      if (ret.length != Data.boolean_data.length) {
        MyUtil.displayError("Wrong length", "" + Data.boolean_data.length,
            "" + ret.length);
        failures++;
      } else if (ret[0] != false && ret[0] != true) {
        MyUtil.displayError("Invalid return", "false or true", "" + ret[0]);
        failures++;
      } else if (ret[0] != false) {
        MyUtil.displayError("Wrong return", "false", "" + ret[0]);
        failures++;
      }
    } catch (Exception e) {
      MyUtil.logErr("Caught exception: " + e.getMessage());
      MyUtil.printStackTrace(e);
      MyUtil.logErr(e.toString());
      failures++;
    }
    if (failures > 0) {
      MyUtil.logMsg(subtestname + "failed");
    } else {
      MyUtil.logMsg(subtestname + "passed");
    }
    MyUtil.logDoneSubTest(subtestname);
    return failures;
  }

  private int pass_a_byte_array() {
    String subtestname = "PrimTest02:(pass_a_byte_array)";
    int failures = 0;

    MyUtil.logMsg(subtestname);
    try {
      MyUtil.logMsg("Passing/Returning byte array to/from RMIIIOPTestBean");
      byte ret[] = tBeanRef.pass_a_byte_array(Data.byte_data);
      if (ret.length != Data.byte_data.length) {
        MyUtil.displayError("Wrong length", "" + Data.byte_data.length,
            "" + ret.length);
        failures++;
      } else if (ret[0] != 0 && ret[0] != 1) {
        MyUtil.displayError("Invalid return", "0 or 1", "" + ret[0]);
        failures++;
      } else if (ret[0] != 0) {
        MyUtil.displayError("Wrong return", "0", "" + ret[0]);
        failures++;
      }
    } catch (Exception e) {
      MyUtil.logErr("Caught exception: " + e.getMessage());
      MyUtil.printStackTrace(e);
      MyUtil.logErr(e.toString());
      failures++;
    }
    if (failures > 0) {
      MyUtil.logMsg(subtestname + "failed");
    } else {
      MyUtil.logMsg(subtestname + "passed");
    }
    MyUtil.logDoneSubTest(subtestname);
    return failures;
  }

  private int pass_a_char_array() {
    String subtestname = "PrimTest02:(pass_a_char_array)";
    int failures = 0;

    MyUtil.logMsg(subtestname);
    try {
      MyUtil.logMsg("Passing/Returning char array to/from RMIIIOPTestBean");
      char ret[] = tBeanRef.pass_a_char_array(Data.char_data);
      if (ret.length != Data.char_data.length) {
        MyUtil.displayError("Wrong length", "" + Data.char_data.length,
            "" + ret.length);
        failures++;
      } else if (ret[0] != '0' && ret[0] != '1') {
        MyUtil.displayError("Invalid return", "'0' or '1'", "'" + ret[0] + "'");
        failures++;
      } else if (ret[0] != '0') {
        MyUtil.displayError("Wrong return", "'0'", "'" + ret[0] + "'");
        failures++;
      }
    } catch (Exception e) {
      MyUtil.logErr("Caught exception: " + e.getMessage());
      MyUtil.printStackTrace(e);
      MyUtil.logErr(e.toString());
      failures++;
    }
    if (failures > 0) {
      MyUtil.logMsg(subtestname + "failed");
    } else {
      MyUtil.logMsg(subtestname + "passed");
    }
    MyUtil.logDoneSubTest(subtestname);
    return failures;
  }

  private int pass_a_short_array() {
    String subtestname = "PrimTest02:(pass_a_short_array)";
    int failures = 0;

    MyUtil.logMsg(subtestname);
    try {
      MyUtil.logMsg("Passing/Returning short array to/from RMIIIOPTestBean");
      short ret[] = tBeanRef.pass_a_short_array(Data.short_data);
      if (ret.length != Data.short_data.length) {
        MyUtil.displayError("Invalid length", "" + Data.short_data.length,
            "" + ret.length);
        failures++;
      } else if (ret[0] != 0 && ret[0] != 1) {
        MyUtil.displayError("Invalid return", "0 or 1", "" + ret[0]);
        failures++;
      } else if (ret[0] != 0) {
        MyUtil.displayError("Wrong return", "0", "" + ret[0]);
        failures++;
      }
    } catch (Exception e) {
      MyUtil.logErr("Caught exception: " + e.getMessage());
      MyUtil.printStackTrace(e);
      MyUtil.logErr(e.toString());
      failures++;
    }
    if (failures > 0) {
      MyUtil.logMsg(subtestname + "failed");
    } else {
      MyUtil.logMsg(subtestname + "passed");
    }
    MyUtil.logDoneSubTest(subtestname);
    return failures;
  }

  private int pass_a_int_array() {
    String subtestname = "PrimTest02:(pass_a_int_array)";
    int failures = 0;

    MyUtil.logMsg(subtestname);
    try {
      MyUtil.logMsg("Passing/Returning int array to/from RMIIIOPTestBean");
      int ret[] = tBeanRef.pass_a_int_array(Data.int_data);
      if (ret.length != Data.int_data.length) {
        MyUtil.displayError("Wrong length", "" + Data.int_data.length,
            "" + ret.length);
        failures++;
      } else if (ret[0] != 0 && ret[0] != 1) {
        MyUtil.displayError("Invalid return", "0 or 1", "" + ret[0]);
        failures++;
      } else if (ret[0] != 0) {
        MyUtil.displayError("Wrong return", "0", "" + ret[0]);
        failures++;
      }
    } catch (Exception e) {
      MyUtil.logErr("Caught exception: " + e.getMessage());
      MyUtil.printStackTrace(e);
      MyUtil.logErr(e.toString());
      failures++;
    }
    if (failures > 0) {
      MyUtil.logMsg(subtestname + "failed");
    } else {
      MyUtil.logMsg(subtestname + "passed");
    }
    MyUtil.logDoneSubTest(subtestname);
    return failures;
  }

  private int pass_a_long_array() {
    String subtestname = "PrimTest02:(pass_a_long_array)";
    int failures = 0;

    MyUtil.logMsg(subtestname);
    try {
      MyUtil.logMsg("Passing/Returning long array to/from RMIIIOPTestBean");
      long ret[] = tBeanRef.pass_a_long_array(Data.long_data);
      if (ret.length != Data.long_data.length) {
        MyUtil.displayError("Wrong length", "" + Data.long_data.length,
            "" + ret.length);
        failures++;
      } else if (ret[0] != 0 && ret[0] != 1) {
        MyUtil.displayError("Invalid return", "0 or 1", "" + ret[0]);
        failures++;
      } else if (ret[0] != 0) {
        MyUtil.displayError("Wrong return", "0", "" + ret[0]);
        failures++;
      }
    } catch (Exception e) {
      MyUtil.logErr("Caught exception: " + e.getMessage());
      MyUtil.printStackTrace(e);
      MyUtil.logErr(e.toString());
      failures++;
    }
    if (failures > 0) {
      MyUtil.logMsg(subtestname + "failed");
    } else {
      MyUtil.logMsg(subtestname + "passed");
    }
    MyUtil.logDoneSubTest(subtestname);
    return failures;
  }

  private int pass_a_float_array() {
    String subtestname = "PrimTest02:(pass_a_float_array)";
    int failures = 0;

    MyUtil.logMsg(subtestname);
    try {
      MyUtil.logMsg("Passing/Returning float array to/from RMIIIOPTestBean");
      float ret[] = tBeanRef.pass_a_float_array(Data.float_data);
      if (ret.length != Data.float_data.length) {
        MyUtil.displayError("Wrong length", "" + Data.float_data.length,
            "" + ret.length);
        failures++;
      } else if (ret[0] != (float) 0.0 && ret[0] != (float) 1.0) {
        MyUtil.displayError("Invalid return", "0.0 or 1.0", "" + ret[0]);
        failures++;
      } else if (ret[0] != (float) 0.0) {
        MyUtil.displayError("Wrong return", "0.0", "" + ret[0]);
        failures++;
      }
    } catch (Exception e) {
      MyUtil.logErr("Caught exception: " + e.getMessage());
      MyUtil.printStackTrace(e);
      MyUtil.logErr(e.toString());
      failures++;
    }
    if (failures > 0) {
      MyUtil.logMsg(subtestname + "failed");
    } else {
      MyUtil.logMsg(subtestname + "passed");
    }
    MyUtil.logDoneSubTest(subtestname);
    return failures;
  }

  private int pass_a_double_array() {
    String subtestname = "PrimTest02:(pass_a_double_array)";
    int failures = 0;

    MyUtil.logMsg(subtestname);
    try {
      MyUtil.logMsg("Passing/Returning double array to/from RMIIIOPTestBean");
      double ret[] = tBeanRef.pass_a_double_array(Data.double_data);
      if (ret.length != Data.double_data.length) {
        MyUtil.displayError("Wrong length", "" + Data.double_data.length,
            "" + ret.length);
        failures++;
      } else if (ret[0] != (double) 0.0 && ret[0] != (double) 1.0) {
        MyUtil.displayError("Invalid return", "0.0 or 1.0", "" + ret[0]);
        failures++;
      } else if (ret[0] != (double) 0.0) {
        MyUtil.displayError("Wrong return", "0.0", "" + ret[0]);
        failures++;
      }
    } catch (Exception e) {
      MyUtil.logErr("Caught exception: " + e.getMessage());
      MyUtil.printStackTrace(e);
      MyUtil.logErr(e.toString());
      failures++;
    }
    if (failures > 0) {
      MyUtil.logMsg(subtestname + "failed");
    } else {
      MyUtil.logMsg(subtestname + "passed");
    }
    MyUtil.logDoneSubTest(subtestname);
    return failures;
  }

  private int throw_a_user_exception() {
    String subtestname = "EXTest01:(throw_a_user_exception)";
    int failures = 0;

    MyUtil.logMsg(subtestname);
    try {
      MyUtil.logMsg("Throw a user defined exception from RMIIIOPTestBean");
      tBeanRef.throw_a_user_exception();
      MyUtil.logErr("No UserException was thrown");
      failures++;
    } catch (UserException e) {
      MyUtil.logMsg("Caught UserException as expected");
      MyUtil.logMsg("Checking UserException message");
      if (!(e.getMessage().equals("This is a UserException test"))) {
        MyUtil.displayError("Wrong message", "This is a UserException test",
            e.getMessage());
        failures++;
      }
    } catch (Exception e) {
      MyUtil.logErr("Caught exception: " + e.getMessage());
      MyUtil.printStackTrace(e);
      MyUtil.logErr(e.toString());
      failures++;
    }
    if (failures > 0) {
      MyUtil.logMsg(subtestname + "failed");
    } else {
      MyUtil.logMsg(subtestname + "passed");
    }
    MyUtil.logDoneSubTest(subtestname);
    return failures;
  }

  private int throw_exception(String s) {
    String subtestname = "EXTest02a:(throw_exception)";
    int failures = 0;

    if (s.equals("EJBException"))
      subtestname = "EXTest02a:(throw_exception)";
    else if (s.equals("NullPointerException"))
      subtestname = "EXTest02b:(throw_exception)";
    else if (s.equals("Error"))
      subtestname = "EXTest02c:(throw_exception)";
    MyUtil.logMsg(subtestname);
    try {
      if (s.equals("EJBException")) {
        MyUtil.logMsg("Throw an EJBException from RMIIIOPTestBean");
        try {
          tBeanRef.throw_exception("EJBException");
          MyUtil.logErr("No EJBException was thrown");
          failures++;
        } catch (RemoteException e) {
          MyUtil.logMsg("Caught RemoteException as expected");
        } catch (Exception e) {
          MyUtil.logErr("Caught unexpected exception: " + e.getMessage(), e);
          failures++;
        }
      } else if (s.equals("Error")) {
        MyUtil.logMsg("Throw an Error from RMIIIOPTestBean");
        try {
          tBeanRef.throw_exception("Error");
          MyUtil.logErr("No Error was thrown");
          failures++;
        } catch (RemoteException e) {
          MyUtil.logMsg("Caught RemoteException as expected");
        } catch (Exception e) {
          MyUtil.logErr("Caught unexpected exception: " + e.getMessage(), e);
          failures++;
        }
      } else if (s.equals("NullPointerException")) {
        MyUtil.logMsg("Throw a NullPointerException from RMIIIOPTestBean");
        try {
          tBeanRef.throw_exception("NullPointerException");
          MyUtil.logErr("No NullPointerException was thrown");
          failures++;
        } catch (RemoteException e) {
          MyUtil.logMsg("Caught RemoteException as expected");
        } catch (Exception e) {
          MyUtil.logErr("Caught unexpected exception: " + e.getMessage(), e);
          failures++;
        }
      }
    } catch (Exception e) {
      MyUtil.logErr("Caught exception: " + e.getMessage());
      MyUtil.printStackTrace(e);
      MyUtil.logErr(e.toString());
      failures++;
    }
    if (failures > 0) {
      MyUtil.logMsg(subtestname + "failed");
    } else {
      MyUtil.logMsg(subtestname + "passed");
    }
    MyUtil.logDoneSubTest(subtestname);
    return failures;
  }

  private int pass_a_remote_interface() {
    String subtestname = "RITest01:(pass_a_remote_interface)";
    int failures = 0;

    MyUtil.logMsg(subtestname);
    try {
      MyUtil.logMsg("Passing a Remote Interface to RMIIIOPTestBean");
      int ret = tBeanRef.pass_a_remote_interface(cBeanRefs[0]);
      if (ret != 0) {
        MyUtil.logErr("Could not pass a Remote Interface to RMIIIOPTestBean");
        MyUtil.displayError("Wrong return", "0", "" + ret);
        failures++;
      }
    } catch (Exception e) {
      MyUtil.logErr("Caught exception: " + e.getMessage());
      MyUtil.printStackTrace(e);
      MyUtil.logErr(e.toString());
      failures++;
    }
    if (failures > 0) {
      MyUtil.logMsg(subtestname + "failed");
    } else {
      MyUtil.logMsg(subtestname + "passed");
    }
    MyUtil.logDoneSubTest(subtestname);
    return failures;
  }

  private int return_a_remote_interface() {
    String subtestname = "RITest01:(return_a_remote_interface)";
    int failures = 0;

    MyUtil.logMsg(subtestname);
    try {
      MyUtil.logMsg("Returning a Remote Interface from RMIIIOPTestBean");
      cBeanRefs[0] = tBeanRef.return_a_remote_interface();
      if (cBeanRefs[0] == null) {
        MyUtil
            .logErr("Could not return a Remote Interface from RMIIIOPTestBean");
        failures++;
      } else {
        try {
          MyUtil
              .logMsg("Invoking the methods of the returned Remote Interface");
          cBeanRefs[0].method1();
          cBeanRefs[0].method2();
        } catch (Exception e) {
          MyUtil.logErr("Unable to invoke the methods of the Remote Interface");
          MyUtil.logErr("Caught exception: " + e.getMessage());
          MyUtil.printStackTrace(e);
          MyUtil.logErr(e.toString());
          failures++;
        }
      }
    } catch (Exception e) {
      MyUtil.logErr("Caught exception: " + e.getMessage());
      MyUtil.printStackTrace(e);
      MyUtil.logErr(e.toString());
      failures++;
    }
    if (failures > 0) {
      MyUtil.logMsg(subtestname + "failed");
    } else {
      MyUtil.logMsg(subtestname + "passed");
    }
    MyUtil.logDoneSubTest(subtestname);
    return failures;
  }

  private int pass_array_of_remote_interfaces() {
    String subtestname = "RITest02:(pass_array_of_remote_interfaces)";
    int failures = 0;

    MyUtil.logMsg(subtestname);
    try {
      MyUtil.logMsg("Passing array of Remote Interfaces to RMIIIOPTestBean");
      int ret = tBeanRef.pass_array_of_remote_interfaces(cBeanRefs);
      if (ret != 0) {
        MyUtil.logErr(
            "Could not pass array of Remote Interfaces to RMIIIOPTestBean");
        MyUtil.displayError("Wrong return", "0", "" + ret);
        failures++;
      }
    } catch (Exception e) {
      MyUtil.logErr("Caught exception: " + e.getMessage());
      MyUtil.printStackTrace(e);
      MyUtil.logErr(e.toString());
      failures++;
    }
    if (failures > 0) {
      MyUtil.logMsg(subtestname + "failed");
    } else {
      MyUtil.logMsg(subtestname + "passed");
    }
    MyUtil.logDoneSubTest(subtestname);
    return failures;
  }

  private int return_array_of_remote_interfaces() {
    String subtestname = "RITest02:(return_array_of_remote_interfaces)";
    int failures = 0;

    MyUtil.logMsg(subtestname);
    try {
      MyUtil
          .logMsg("Returning array of Remote Interfaces from RMIIIOPTestBean");
      cBeanRefs = tBeanRef.return_array_of_remote_interfaces();
      if (cBeanRefs == null) {
        MyUtil.logErr("Could not return array of Remote Interfaces"
            + " from RMIIIOPTestBean");
        failures++;
      } else if (cBeanRefs.length != Data.MAXOBJETS) {
        MyUtil.displayError("Wrong length", "" + Data.MAXOBJETS,
            "" + cBeanRefs.length);
        failures++;
        for (int i = 0; i < cBeanRefs.length; i++) {
          try {
            MyUtil
                .logMsg("Invoking the methods of the returned Remote Interface"
                    + " refs[" + i + "]");
            cBeanRefs[i].method1();
            cBeanRefs[i].method2();
          } catch (Exception e) {
            MyUtil.logErr("Unable to invoke the methods of the Remote Interface"
                + " refs[" + i + "]");
            MyUtil.logErr("Caught exception: " + e.getMessage());
            MyUtil.printStackTrace(e);
            MyUtil.logErr(e.toString());
            failures++;
          }
        }
      }
    } catch (Exception e) {
      MyUtil.logErr("Caught exception: " + e.getMessage());
      MyUtil.printStackTrace(e);
      MyUtil.logErr(e.toString());
      failures++;
    }
    if (failures > 0) {
      MyUtil.logMsg(subtestname + "failed");
    } else {
      MyUtil.logMsg(subtestname + "passed");
    }
    MyUtil.logDoneSubTest(subtestname);
    return failures;
  }

  private int pass_a_date_object() {
    String subtestname = "OBVTest01:(pass_a_date_object)";
    int failures = 0;

    MyUtil.logMsg(subtestname);
    try {
      MyUtil.logMsg("Passing a Date object to RMIIIOPTestBean");
      int ret = tBeanRef.pass_a_date_object(Data.date);
      if (ret != 0) {
        MyUtil.logErr("Could not pass a Date object to RMIIIOPTestBean");
        MyUtil.displayError("Wrong return", "0", "" + ret);
        failures++;
      }
    } catch (Exception e) {
      MyUtil.logErr("Caught exception: " + e.getMessage());
      MyUtil.printStackTrace(e);
      MyUtil.logErr(e.toString());
      failures++;
    }
    if (failures > 0) {
      MyUtil.logMsg(subtestname + "failed");
    } else {
      MyUtil.logMsg(subtestname + "passed");
    }
    MyUtil.logDoneSubTest(subtestname);
    return failures;
  }

  private int pass_any_object() {
    String subtestname = "OBVTest01:(pass_any_object)";
    int failures = 0;

    MyUtil.logMsg(subtestname);
    try {
      MyUtil.logMsg("Passing any Java object to RMIIIOPTestBean");
      int ret = tBeanRef.pass_any_object(Data.bitset);
      if (ret != 0) {
        MyUtil.logErr("Could not pass a BitSet object to RMIIIOPTestBean");
        MyUtil.displayError("Wrong return", "0", "" + ret);
        failures++;
      }
    } catch (Exception e) {
      MyUtil.logErr("Caught exception: " + e.getMessage());
      MyUtil.printStackTrace(e);
      MyUtil.logErr(e.toString());
      failures++;
    }
    if (failures > 0) {
      MyUtil.logMsg(subtestname + "failed");
    } else {
      MyUtil.logMsg(subtestname + "passed");
    }
    MyUtil.logDoneSubTest(subtestname);
    return failures;
  }

  private int return_a_date_object() {
    String subtestname = "OBVTest01:(return_a_date_object)";
    int failures = 0;

    MyUtil.logMsg(subtestname);
    try {
      MyUtil.logMsg("Returning a Date object from RMIIIOPTestBean");
      Date d = tBeanRef.return_a_date_object();
      if (!(d instanceof Date)) {
        MyUtil.logErr("Could not return a Date object from RMIIIOPTestBean");
        failures++;
      }
      if (!d.equals(Data.date)) {
        MyUtil
            .logErr("Returned Date object doesn't match expected Date object");
        failures++;
      }
    } catch (Exception e) {
      MyUtil.logErr("Caught exception: " + e.getMessage());
      MyUtil.printStackTrace(e);
      MyUtil.logErr(e.toString());
      failures++;
    }
    if (failures > 0) {
      MyUtil.logMsg(subtestname + "failed");
    } else {
      MyUtil.logMsg(subtestname + "passed");
    }
    MyUtil.logDoneSubTest(subtestname);
    return failures;
  }

  private int return_any_object() {
    String subtestname = "OBVTest01:(return_any_object)";
    int failures = 0;

    MyUtil.logMsg(subtestname);
    try {
      MyUtil.logMsg("Returning any Java object from RMIIIOPTestBean");
      java.lang.Object o = tBeanRef.return_any_object();
      if (!(o instanceof BitSet)) {
        MyUtil.logErr("Could not return a BitSet object from RMIIIOPTestBean");
        failures++;
      }
      if (!o.equals(Data.bitset)) {
        MyUtil.logErr(
            "Returned BitSet object doesn't match expected BitSet object");
        failures++;
      }
    } catch (Exception e) {
      MyUtil.logErr("Caught exception: " + e.getMessage());
      MyUtil.printStackTrace(e);
      MyUtil.logErr(e.toString());
      failures++;
    }
    if (failures > 0) {
      MyUtil.logMsg(subtestname + "failed");
    } else {
      MyUtil.logMsg(subtestname + "passed");
    }
    MyUtil.logDoneSubTest(subtestname);
    return failures;
  }

  private int pass_return_a_string_object() {
    String subtestname = "OBVTest01:(pass_return_a_string_object)";
    int failures = 0;

    MyUtil.logMsg(subtestname);
    try {
      MyUtil.logMsg("Returning a String object from RMIIIOPTestBean");
      java.lang.Object o = tBeanRef
          .pass_return_a_string_object(Data.string_data[0]);
      if (!(o instanceof String)) {
        MyUtil.logErr("Could not return a String object from RMIIIOPTestBean");
        failures++;
      }

      if (!o.equals(Data.string_data[0])) {
        MyUtil.logErr(
            "Returned String object doesn't match expected String object");

        failures++;
      }

    } catch (Exception e) {
      MyUtil.logErr("Caught exception: " + e.getMessage());
      MyUtil.printStackTrace(e);
      MyUtil.logErr(e.toString());
      failures++;
    }
    if (failures > 0) {
      MyUtil.logMsg(subtestname + "failed");
    } else {
      MyUtil.logMsg(subtestname + "passed");
    }
    MyUtil.logDoneSubTest(subtestname);
    return failures;
  }

  private int pass_return_a_class_object() {
    String subtestname = "OBVTest01:(pass_return_a_class_object)";
    int failures = 0;

    MyUtil.logMsg(subtestname);
    try {
      MyUtil.logMsg("Returning a Class object from RMIIIOPTestBean");
      java.lang.Object o = tBeanRef
          .pass_return_a_class_object(Data.class_data[0]);
      if (!(o instanceof Class)) {
        MyUtil.logErr("Could not return a Class object from RMIIIOPTestBean");
        failures++;
      }

      if (!o.equals(Data.class_data[0])) {
        MyUtil.logErr(
            "Returned Class object doesn't match expected Class object");

        failures++;
      }

    } catch (Exception e) {
      MyUtil.logErr("Caught exception: " + e.getMessage());
      MyUtil.printStackTrace(e);
      MyUtil.logErr(e.toString());
      failures++;
    }
    if (failures > 0) {
      MyUtil.logMsg(subtestname + "failed");
    } else {
      MyUtil.logMsg(subtestname + "passed");
    }
    MyUtil.logDoneSubTest(subtestname);
    return failures;
  }

  private int pass_return_a_vector_object() {
    String subtestname = "OBVTest01:(pass_return_a_vector_object)";
    int failures = 0;

    MyUtil.logMsg(subtestname);
    try {
      MyUtil.logMsg("Returning a Vector object from RMIIIOPTestBean");
      java.lang.Object o = tBeanRef.pass_return_a_vector_object(Data.vector);
      if (!(o instanceof Vector)) {
        MyUtil.logErr("Could not return a Vector object from RMIIIOPTestBean");
        failures++;
      }

      else if (((Vector) o).size() != Data.vector.size()) {
        MyUtil.logErr("Vector has wrong size" + "" + Data.vector.size() + ""
            + ((Vector) o).size());
        failures++;
      }

      else {
        Object o1 = ((Vector) o).elementAt(0);
        if (!(o1 instanceof BigInteger)) {
          MyUtil.logErr("o1 is not a BigInteger");
          failures++;
        }
        if (!o1.equals(new BigInteger("3512359"))) {
          MyUtil
              .logErr("Returned BigInteger doesn't match expected BigInteger");
          failures++;
        }

        Object o2 = ((Vector) o).elementAt(1);
        if (!(o2 instanceof String)) {
          MyUtil.logErr("o2 is not a String");
          failures++;
        }
        if (!o2.equals("two")) {
          MyUtil.logErr("Returned string doesn't match expected string");
          failures++;
        }

      }

    } catch (Exception e) {
      MyUtil.logErr("Caught exception: " + e.getMessage());
      MyUtil.printStackTrace(e);
      MyUtil.logErr(e.toString());
      failures++;
    }
    if (failures > 0) {
      MyUtil.logMsg(subtestname + "failed");
    } else {
      MyUtil.logMsg(subtestname + "passed");
    }
    MyUtil.logDoneSubTest(subtestname);
    return failures;
  }

  private int pass_return_a_hashtable_object() {
    String subtestname = "OBVTest01:(pass_return_a_hashtable_object)";
    int failures = 0;

    MyUtil.logMsg(subtestname);
    try {
      MyUtil.logMsg("Returning a Hashtable object from RMIIIOPTestBean");
      java.lang.Object o = tBeanRef.pass_return_a_hashtable_object(Data.table);
      if (!(o instanceof Hashtable)) {
        MyUtil
            .logErr("Could not return a Hashtable object from RMIIIOPTestBean");
        failures++;
      }

      else if (((Hashtable) o).size() != Data.table.size()) {
        MyUtil.logErr("Hashtable has wrong size" + "" + Data.table.size() + ""
            + ((Hashtable) o).size());
        failures++;
      }

      else {
        Object o1 = ((Hashtable) o).get("one");
        if (!(o1 instanceof BigInteger)) {
          MyUtil.logErr("o1 is not a BigInteger");
          failures++;
        }
        if (!o1.equals(new BigInteger("3512359"))) {
          MyUtil
              .logErr("Returned BigInteger doesn't match expected BigInteger");
          failures++;
        }

        Object o2 = ((Hashtable) o).get("two");
        if (!(o2 instanceof String)) {
          MyUtil.logErr("o2 is not a String");
          failures++;
        }
        if (!o2.equals("two")) {
          MyUtil.logErr(
              "Returned String object doesn't match expected String object");
          failures++;
        }

      }
    } catch (Exception e) {
      MyUtil.logErr("Caught exception: " + e.getMessage());
      MyUtil.printStackTrace(e);
      MyUtil.logErr(e.toString());
      failures++;
    }
    if (failures > 0) {
      MyUtil.logMsg(subtestname + "failed");
    } else {
      MyUtil.logMsg(subtestname + "passed");
    }
    MyUtil.logDoneSubTest(subtestname);
    return failures;
  }

  private int pass_return_recursive_vector_object() {
    String subtestname = "RecursiveTest01:(pass_return_recursive_vector_object)";
    int failures = 0;

    MyUtil.logMsg(subtestname);
    try {
      MyUtil.logMsg("Returning a Vector object from RMIIIOPTestBean");
      java.lang.Object o = tBeanRef.pass_return_a_vector_object(Data.vector);
      if (!(o instanceof Vector)) {
        MyUtil.logErr("Could not return a Vector object from RMIIIOPTestBean");
        failures++;
      }

      else if (((Vector) o).size() != Data.vector.size()) {
        MyUtil.logErr("Vector has wrong size" + "" + Data.vector.size() + ""
            + ((Vector) o).size());
        failures++;
      }

      else {
        Object o3 = ((Vector) o).elementAt(2);
        if (!(o3 instanceof Vector)) {
          MyUtil.logErr("o3 is not a vector");
          failures++;
        }
        if (!o3.equals(o)) {
          MyUtil.logErr("Returned vector doesn't match expected vector object");
          MyUtil.logErr("o3 is unexpected vector object");
          failures++;
        }
      }
    } catch (Exception e) {
      MyUtil.logErr("Caught exception: " + e.getMessage());
      MyUtil.printStackTrace(e);
      MyUtil.logErr(e.toString());
      failures++;
    }
    if (failures > 0) {
      MyUtil.logMsg(subtestname + "failed");
    } else {
      MyUtil.logMsg(subtestname + "passed");
    }
    MyUtil.logDoneSubTest(subtestname);
    return failures;
  }

  private int pass_return_recursive_hashtable_object() {
    String subtestname = "RecursiveTest01:(pass_return_recursive_hashtable_object)";
    int failures = 0;

    MyUtil.logMsg(subtestname);
    try {
      MyUtil.logMsg("Returning a Hashtable object from RMIIIOPTestBean");
      java.lang.Object o = tBeanRef.pass_return_a_hashtable_object(Data.table2);
      if (!(o instanceof Hashtable)) {
        MyUtil
            .logErr("Could not return a Hashtable object from RMIIIOPTestBean");
        failures++;
      } else if (((Hashtable) o).size() != Data.table2.size()) {
        MyUtil
            .logErr("Could not return a Hashtable object from RMIIIOPTestBean");
        MyUtil.logErr("Table has wrong size" + "" + Data.table.size() + ""
            + ((Hashtable) o).size());
        failures++;
      } else {
        Object o3 = ((Hashtable) o).get("three");
        if (!(o3 instanceof Hashtable)) {
          MyUtil.logErr(
              "Could not return a Hashtable object from RMIIIOPTestBean");
          MyUtil.logErr("o3 is not a Hashtable");
          failures++;
        }
        if (!o3.equals(o)) {
          MyUtil.logErr(
              "Returned Hashtable doesn't match expected Hashtable object");
          MyUtil.logErr("o3 is unexpected Hashtable object");
          failures++;
        }
      }
    } catch (Exception e) {
      MyUtil.logErr("Caught exception: " + e.getMessage());
      MyUtil.printStackTrace(e);
      MyUtil.logErr(e.toString());
      failures++;
    }
    if (failures > 0) {
      MyUtil.logMsg(subtestname + "failed");
    } else {
      MyUtil.logMsg(subtestname + "passed");
    }
    MyUtil.logDoneSubTest(subtestname);
    return failures;
  }

  private int pass_a_date_object_array() {
    String subtestname = "OBVTest02:(pass_a_date_object_array)";
    int failures = 0;

    MyUtil.logMsg(subtestname);
    try {
      MyUtil.logMsg("Passing a Date object array to RMIIIOPTestBean");
      int ret = tBeanRef.pass_a_date_object_array(Data.date_array);
      if (ret != 0) {
        MyUtil.logErr("Could not pass a Date object array to RMIIIOPTestBean");
        MyUtil.displayError("Wrong return", "0", "" + ret);
        failures++;
      }
    } catch (Exception e) {
      MyUtil.logErr("Caught exception: " + e.getMessage());
      MyUtil.printStackTrace(e);
      MyUtil.logErr(e.toString());
      failures++;
    }
    if (failures > 0) {
      MyUtil.logMsg(subtestname + "failed");
    } else {
      MyUtil.logMsg(subtestname + "passed");
    }
    MyUtil.logDoneSubTest(subtestname);
    return failures;
  }

  private int pass_return_string_object_array() {
    String subtestname = "OBVTest02:(pass_return_string_object_array)";
    int failures = 0;

    MyUtil.logMsg(subtestname);
    try {
      MyUtil.logMsg("Returning a String object array from RMIIIOPTestBean");
      String s[] = tBeanRef
          .pass_return_arrays_of_string_objects(Data.string_data);
      if (s.length != Data.string_data.length) {
        MyUtil.logErr(
            "Could not return a String object array from RMIIIOPTestBean");
        MyUtil.displayError("Wrong length", "" + Data.string_data.length,
            "" + s.length);
        failures++;
      }
      for (int i = 0; i < s.length; i++) {
        if (!(s[i] instanceof String)) {
          MyUtil.logErr(
              "Could not return a String object array from RMIIIOPTestBean");
          MyUtil.logErr("s[" + i + "] not a String object");
          failures++;
        }
        if (!s[i].equals(Data.string_data[i])) {
          MyUtil.logErr("Returned String doesn't match expected String object");
          MyUtil.logErr("s[" + i + "] is unexpected String object");
          failures++;
        }
      }

    } catch (Exception e) {
      MyUtil.logErr("Caught exception: " + e.getMessage());
      MyUtil.printStackTrace(e);
      MyUtil.logErr(e.toString());
      failures++;
    }
    if (failures > 0) {
      MyUtil.logMsg(subtestname + "failed");
    } else {
      MyUtil.logMsg(subtestname + "passed");
    }
    MyUtil.logDoneSubTest(subtestname);
    return failures;
  }

  private int pass_return_class_object_array() {
    String subtestname = "OBVTest02:(pass_return_class_object_array)";
    int failures = 0;

    MyUtil.logMsg(subtestname);
    try {
      MyUtil.logMsg("Returning a Class object array from RMIIIOPTestBean");
      Class c[] = tBeanRef.pass_return_arrays_of_class_objects(Data.class_data);
      if (c.length != Data.class_data.length) {
        MyUtil.logErr(
            "Could not return a Class object array from RMIIIOPTestBean");
        MyUtil.displayError("Wrong length", "" + Data.class_data.length,
            "" + c.length);
        failures++;
      }
      for (int i = 0; i < c.length; i++) {
        if (!(c[i] instanceof Class)) {
          MyUtil.logErr(
              "Could not return a Class object array from RMIIIOPTestBean");
          MyUtil.logErr("c[" + i + "] not a Class object");
          failures++;
        }
        if (!c[i].equals(Data.class_data[i])) {
          MyUtil.logErr(
              "Returned Class object doesn't match expected Class object");
          MyUtil.logErr("c[" + i + "] is unexpected Class object");
          failures++;
        }
      }

    } catch (Exception e) {
      MyUtil.logErr("Caught exception: " + e.getMessage());
      MyUtil.printStackTrace(e);
      MyUtil.logErr(e.toString());
      failures++;
    }
    if (failures > 0) {
      MyUtil.logMsg(subtestname + "failed");
    } else {
      MyUtil.logMsg(subtestname + "passed");
    }
    MyUtil.logDoneSubTest(subtestname);
    return failures;
  }

  private int pass_any_object_array() {
    String subtestname = "OBVTest02:(pass_any_object_array)";
    int failures = 0;

    MyUtil.logMsg(subtestname);
    try {
      MyUtil.logMsg("Passing any Java object array to RMIIIOPTestBean");
      int ret = tBeanRef.pass_any_object_array(Data.bitset_array);
      if (ret != 0) {
        MyUtil
            .logErr("Could not pass a BitSet object array to RMIIIOPTestBean");
        MyUtil.displayError("Wrong return", "0", "" + ret);
        failures++;
      }
    } catch (Exception e) {
      MyUtil.logErr("Caught exception: " + e.getMessage());
      MyUtil.printStackTrace(e);
      MyUtil.logErr(e.toString());
      failures++;
    }
    if (failures > 0) {
      MyUtil.logMsg(subtestname + "failed");
    } else {
      MyUtil.logMsg(subtestname + "passed");
    }
    MyUtil.logDoneSubTest(subtestname);
    return failures;
  }

  private int return_a_date_object_array() {
    String subtestname = "OBVTest02:(return_a_date_object_array)";
    int failures = 0;

    MyUtil.logMsg(subtestname);
    try {
      MyUtil.logMsg("Returning a Date object array from RMIIIOPTestBean");
      Date d[] = tBeanRef.return_a_date_object_array();
      if (d.length != Data.date_array.length) {
        MyUtil.logErr(
            "Could not return a Date object array from RMIIIOPTestBean");
        MyUtil.displayError("Wrong length", "" + Data.date_array.length,
            "" + d.length);
        failures++;
      }
      for (int i = 0; i < d.length; i++) {
        if (!(d[i] instanceof Date)) {
          MyUtil.logErr(
              "Could not return a Date object array from RMIIIOPTestBean");
          MyUtil.logErr("d[" + i + "] not a Date object");
          failures++;
        }
        if (!d[i].equals(Data.date_array[i])) {
          MyUtil.logErr(
              "Returned Date object doesn't match expected Date object");
          MyUtil.logErr("d[" + i + "] is unexpected Date object");
          failures++;
        }
      }
    } catch (Exception e) {
      MyUtil.logErr("Caught exception: " + e.getMessage());
      MyUtil.printStackTrace(e);
      MyUtil.logErr(e.toString());
      failures++;
    }
    if (failures > 0) {
      MyUtil.logMsg(subtestname + "failed");
    } else {
      MyUtil.logMsg(subtestname + "passed");
    }
    MyUtil.logDoneSubTest(subtestname);
    return failures;
  }

  private int return_any_object_array() {
    String subtestname = "OBVTest02:(return_any_object_array)";
    int failures = 0;

    MyUtil.logMsg(subtestname);
    try {
      MyUtil.logMsg("Returning any Java object array from RMIIIOPTestBean");
      java.lang.Object o[] = tBeanRef.return_any_object_array();
      if (o.length != Data.bitset_array.length) {
        MyUtil.logErr(
            "Could not return BitSet object array from RMIIIOPTestBean");
        MyUtil.displayError("Wrong length", "" + Data.bitset_array.length,
            "" + o.length);
        failures++;
      }
      for (int i = 0; i < o.length; i++) {
        if (!(o[i] instanceof BitSet)) {
          MyUtil.logErr(
              "Could not return a BitSet object array from RMIIIOPTestBean");
          MyUtil.logErr("o[" + i + "] not a BitSet object");
          failures++;
        }
        if (!o[i].equals(Data.bitset_array[i])) {
          MyUtil.logErr(
              "Returned BitSet object doesn't match expected BitSet object");
          MyUtil.logErr("o[" + i + "] is unexpected BitSet object");
          failures++;
        }
      }
    } catch (Exception e) {
      MyUtil.logErr("Caught exception: " + e.getMessage());
      MyUtil.printStackTrace(e);
      MyUtil.logErr(e.toString());
      failures++;
    }
    if (failures > 0) {
      MyUtil.logMsg(subtestname + "failed");
    } else {
      MyUtil.logMsg(subtestname + "passed");
    }
    MyUtil.logDoneSubTest(subtestname);
    return failures;
  }

  private int pass_a_graph_of_objects() {
    String subtestname = "OBVTest03:(pass_a_graph_of_objects)";
    int failures = 0;

    MyUtil.logMsg(subtestname);
    try {
      MyUtil.logMsg("Passing a Graph object to RMIIIOPTestBean");
      int ret = tBeanRef.pass_a_graph_of_objects(Data.graph);
      if (ret != 0) {
        MyUtil.logErr("Could not pass a Graph object to RMIIIOPTestBean");
        MyUtil.displayError("Wrong return", "0", "" + ret);
        failures++;
      }
    } catch (Exception e) {
      MyUtil.logErr("Caught exception: " + e.getMessage());
      MyUtil.printStackTrace(e);
      MyUtil.logErr(e.toString());
      failures++;
    }
    if (failures > 0) {
      MyUtil.logMsg(subtestname + "failed");
    } else {
      MyUtil.logMsg(subtestname + "passed");
    }
    MyUtil.logDoneSubTest(subtestname);
    return failures;
  }

  private int return_a_graph_of_objects() {
    String subtestname = "OBVTest03:(return_a_graph_of_objects)";
    int failures = 0;

    MyUtil.logMsg(subtestname);
    try {
      MyUtil.logMsg("Returning a Graph object from RMIIIOPTestBean");
      Graph g = tBeanRef.return_a_graph_of_objects();
      if (!(g instanceof Graph)) {
        MyUtil.logMsg("Could not return a Graph object from RMIIIOPTestBean");
        failures++;
      }

      StringBuffer result = new StringBuffer("{ ");
      for (Graph list = g, list2 = Data.graph; list != null
          && list2 != null; list = list.next(), list2 = list2.next()) {
        result.append(list.data()).append(" ");
        if (!list.CONSTANT_STRING.equals(list2.CONSTANT_STRING)) {
          failures++;
          MyUtil.displayError("list.CONSTANT_STRING != list2.CONSTANT_STRING");
          MyUtil.displayError("list.CONSTANT_STRING",
              "" + list2.CONSTANT_STRING, "" + list.CONSTANT_STRING);
        }
        if (list.CONSTANT_INT != list2.CONSTANT_INT) {
          failures++;
          MyUtil.displayError(": list.CONSTANT_INT != list2.CONSTANT_INT");
          MyUtil.displayError("list.CONSTANT_INT", "" + list2.CONSTANT_INT,
              "" + list.CONSTANT_INT);
        }
        if (!list.CONSTANT_BITSET.equals(list2.CONSTANT_BITSET)) {
          failures++;
          MyUtil.displayError("list.CONSTANT_BITSET != list2.CONSTANT_BITSET");
          MyUtil.displayError("list.CONSTANT_BITSET",
              "" + list2.CONSTANT_BITSET, "" + list.CONSTANT_BITSET);
        }
      }
      result.append("}").toString();
      if (!result.toString().equals(Data.graph_expected_string)) {
        failures++;
        MyUtil.displayError("graph string != Data.graph_expected_string");
        MyUtil.displayError("graph string", "" + Data.graph_expected_string,
            "" + result);
      }
    } catch (Exception e) {
      MyUtil.logErr("Caught exception: " + e.getMessage());
      MyUtil.printStackTrace(e);
      MyUtil.logErr(e.toString());
      failures++;
    }
    if (failures > 0) {
      MyUtil.logMsg(subtestname + "failed");
    } else {
      MyUtil.logMsg(subtestname + "passed");
    }
    MyUtil.logDoneSubTest(subtestname);
    return failures;
  }

  private int pass_array_of_graph_objects() {
    String subtestname = "OBVTest04:(pass_array_of_graph_objects)";
    int failures = 0;

    MyUtil.logMsg(subtestname);
    try {
      MyUtil.logMsg("Passing array of Graph objects to RMIIIOPTestBean");
      int ret = tBeanRef.pass_array_of_graph_objects(Data.graph_array);
      if (ret != 0) {
        MyUtil
            .logErr("Could not pass array of Graph objects to RMIIIOPTestBean");
        MyUtil.displayError("Wrong return", "0", "" + ret);
        failures++;
      }
    } catch (Exception e) {
      MyUtil.logErr("Caught exception: " + e.getMessage());
      MyUtil.printStackTrace(e);
      MyUtil.logErr(e.toString());
      failures++;
    }
    if (failures > 0) {
      MyUtil.logMsg(subtestname + "failed");
    } else {
      MyUtil.logMsg(subtestname + "passed");
    }
    MyUtil.logDoneSubTest(subtestname);
    return failures;
  }

  private int return_array_of_graph_objects() {
    String subtestname = "OBVTest04:(return_array_of_graph_objects)";
    int failures = 0;

    MyUtil.logMsg(subtestname);
    try {
      MyUtil.logMsg("Returning array of Graph objects from RMIIIOPTestBean");
      Graph g[] = tBeanRef.return_array_of_graph_objects();
      for (int i = 0; i < g.length; i++) {
        if (!(g[i] instanceof Graph)) {
          MyUtil.displayError("g[" + i + "] is not a Graph object");
          failures++;
        }

        StringBuffer result = new StringBuffer("{ ");
        for (Graph list = g[i], list2 = Data.graph_array[i]; list != null
            && list2 != null; list = list.next(), list2 = list2.next()) {
          result.append(list.data()).append(" ");
          if (!list.CONSTANT_STRING.equals(list2.CONSTANT_STRING)) {
            failures++;
            MyUtil.displayError(
                "g[" + i + "].CONSTANT_STRING != list2.CONSTANT_STRING");
            MyUtil.displayError("g[" + i + "].CONSTANT_STRING",
                "" + list2.CONSTANT_STRING, "" + g[i].CONSTANT_STRING);
          }
          if (list.CONSTANT_INT != list2.CONSTANT_INT) {
            failures++;
            MyUtil.displayError(
                "g[" + i + "].CONSTANT_INT != list2.CONSTANT_INT");
            MyUtil.displayError("g[" + i + "].CONSTANT_INT",
                "" + list2.CONSTANT_INT, "" + g[i].CONSTANT_INT);
          }
          if (!list.CONSTANT_BITSET.equals(list2.CONSTANT_BITSET)) {
            failures++;
            MyUtil.displayError(
                "g[" + i + "].CONSTANT_BITSET != list2.CONSTANT_BITSET");
            MyUtil.displayError("g[" + i + "].CONSTANT_BITSET",
                "" + list2.CONSTANT_BITSET, "" + g[i].CONSTANT_BITSET);
          }
        }
        result.append("}").toString();
        if (!result.toString().equals(Data.graph_expected_string)) {
          failures++;
          MyUtil.displayError(
              "g[" + i + "] graph string != Data.graph_expected_string");
          MyUtil.displayError("g[" + i + "] graph string",
              "" + Data.graph_expected_string, "" + result);
        }
      }
    } catch (Exception e) {
      MyUtil.logErr("Caught exception: " + e.getMessage());
      MyUtil.printStackTrace(e);
      MyUtil.logErr(e.toString());
      failures++;
    }
    if (failures > 0) {
      MyUtil.logMsg(subtestname + "failed");
    } else {
      MyUtil.logMsg(subtestname + "passed");
    }
    MyUtil.logDoneSubTest(subtestname);
    return failures;
  }

  private int pass_return_object3_array(int vectorsize) {
    String subtestname = "OBVTest05:(pass_return_object3_array)";
    int failures = 0;

    java.lang.Object[][][] main = new Vector[vectorsize][][];
    java.lang.Object[][][] w = null;

    com.sun.ts.tests.rmiiiop.ee.marshaltests.Timer timer = new com.sun.ts.tests.rmiiiop.ee.marshaltests.Timer();
    MyUtil.logMsg(subtestname);
    MyUtil.logMsg("Initialize 3 dimensional object array with vectorsize = "
        + vectorsize);
    for (int m = 0; m < main.length; m++) {
      main[m] = new Vector[vectorsize][];
      for (int k = 0; k < main[m].length; k++) {
        main[m][k] = new Vector[vectorsize];
        for (int j = 0; j < main[m][k].length; j++) {
          main[m][k][j] = new Vector(vectorsize);
          for (int i = 0; i < vectorsize; i++) {
            ((Vector) main[m][k][j]).insertElementAt(Integer.valueOf(i * j), i);
          }
        } /* end for each Vector */
      } /* end for every Vector[] */
    } /* end for every Vector[][] */

    try {
      MyUtil.logMsg("Pass and return the 3 dimensional object "
          + "array to RMIIIOPTestBean");
      MyUtil.logMsg("Please wait this method call can take a long time ....");
      timer.start();
      w = tBeanRef.pass_return_object3_array(main);
      timer.stop();
      MyUtil.logMsg(
          "Method call took ....[" + timer.getStringFormattedTime() + "]");
    } catch (Exception e) {
      MyUtil.logErr("Caught exception: " + e.getMessage());
      MyUtil.printStackTrace(e);
      MyUtil.logErr(e.toString());
      MyUtil.logMsg(subtestname + "failed");
      MyUtil.logDoneSubTest(subtestname);
      failures++;
      return failures;
    }

    MyUtil.logMsg(
        "Compare returned 3 dimensional object array with the one passed");
    MyUtil.logMsg("Checking lengths");
    if (main.length != w.length) {
      MyUtil.displayError("Wrong Length",
          " main array length of " + main.length,
          " is different from the returned " + "array length of " + w.length);
      failures++;
    }

    MyUtil.logMsg("Checking data");
    for (int m = 0; m < main.length; m++) {
      if (failures != 0)
        break;
      if (main[m].length != w[m].length) {
        MyUtil.displayError("Wrong Data1",
            " main [" + m + "] array length of " + main[m].length,
            " is different from the returned " + "array[" + m + "] length of "
                + w[m].length);
        failures++;
        break;
      }

      for (int k = 0; k < main[m].length; k++) {
        if (main[m][k].length != w[m][k].length) {
          MyUtil.displayError("Wrong Data2",
              " main [" + m + "][" + k + "] array length of "
                  + main[m][k].length,
              " is different from the returned " + "array[" + m + "][" + k
                  + "] length of " + w[m][k].length);
          failures++;
          break;
        }
        if (failures != 0)
          break;

        for (int j = 0; j < main[m][k].length; j++) {
          if (((Vector) main[m][k][j]).size() != ((Vector) w[m][k][j]).size()) {
            MyUtil.displayError("Wrong Data3",
                " main [" + m + "][" + k + "][" + j + "] vector length of "
                    + ((Vector) main[m][k][j]).size(),
                " is different from the returned " + "vector [" + m + "][" + k
                    + "][" + j + "] length of " + ((Vector) w[m][k][j]).size());
            failures++;
            break;
          }

          /* examine object, test for identity */
          Enumeration e = ((Vector) main[m][k][j]).elements();
          Enumeration f = ((Vector) w[m][k][j]).elements();

          int counter = 0;
          while (e.hasMoreElements()) {
            if (!((Integer) e.nextElement())
                .equals((Integer) f.nextElement())) {
              MyUtil.displayError("Wrong Data4: element " + counter
                  + " in Vector (" + m + "," + k + "," + j + ")");
              failures++;
              break;
            }
            counter++;
          } /* end for each element in Vector */
          if (failures != 0)
            break;
          if (f.hasMoreElements()) {
            MyUtil.displayError("Wrong Data5: " + " returned Vector (" + m + ","
                + k + "," + j + ") ");
            failures++;
            break;
          }
          if (failures != 0)
            break;
        } /* end for each Vector */
        if (failures != 0)
          break;
      } /* end for each Vector[] */
      if (failures != 0)
        break;
    } /* end for each Vector[][] */

    if (failures > 0) {
      MyUtil.logMsg(subtestname + "failed");
    } else {
      MyUtil.logMsg(subtestname + "passed");
    }

    MyUtil.logDoneSubTest(subtestname);
    return failures;
  }

  public int pass_return_multiclass_types() {
    String subtestname = "MultiTest01:(pass_return_multiclass_types)";
    int failures = 0;
    MyUtil.logMsg(subtestname);
    try {
      MyUtil.logMsg("Passing/Returning Multi Class Types to RMIIIOPTestBean");
      Multi ret = null;
      ret = tBeanRef.pass_return_multiclass_types(Data.multiTypes);
      if (!(ret instanceof Multi)) {
        MyUtil.logErr("Could not pass Multi Class Types to RMIIIOPTestBean");
        failures++;
      } else {
        if (ret.getBear1().getSize() != Data.bear1Size) {
          MyUtil.displayError("Wrong bear1Size", "" + Data.bear1Size,
              "" + ret.getBear1().getSize());
          failures++;
        } else if (ret.getBear2().getSize() != Data.bear2Size) {
          MyUtil.displayError("Wrong bear2Size", "" + Data.bear2Size,
              "" + ret.getBear2().getSize());
          failures++;
        } else if (ret.getBear3().getSize() != Data.bear3Size) {
          MyUtil.displayError("Wrong bear3Size", "" + Data.bear3Size,
              "" + ret.getBear3().getSize());
          failures++;
        }
      }
    } catch (Exception e) {
      MyUtil.logErr("Caught exception: " + e.getMessage());
      MyUtil.printStackTrace(e);
      MyUtil.logErr(e.toString());
      failures++;
    }
    if (failures > 0) {
      MyUtil.logMsg(subtestname + "failed");
    } else {
      MyUtil.logMsg(subtestname + "passed");
    }
    MyUtil.logDoneSubTest(subtestname);
    return failures;
  }

  public int pass_return_idl_entity_types(String idltype) {
    String subtestname = "IDLEntityTest01:(pass_return_idl_entity_types)";
    int failures = 0;
    MyUtil.logMsg(subtestname);
    try {
      MyUtil.logMsg("Passing/Returning IDLEntity Types to RMIIIOPTestBean");
      if (idltype.equals("struct")) {
        MyUtil.logMsg(
            "Passing/Returning struct IDLEntity type to RMIIIOPTestBean");
        IDLStruct ret = null;
        ret = tBeanRef.pass_return_idl_entity_types(Data.idlType1);
        if (!(ret instanceof IDLStruct)) {
          MyUtil.logErr(
              "Could not pass struct IDLEntity type to RMIIIOPTestBean");
          failures++;
        } else if (ret.x != Data.idlType1_value) {
          MyUtil.displayError("Wrong value", "" + Data.idlType1_value,
              "" + ret.x);
          failures++;
        }
      } else if (idltype.equals("sequence")) {
        MyUtil.logMsg(
            "Passing/Returning sequence IDLEntity type to RMIIIOPTestBean");
        IDLStruct[] ret = null;
        ret = tBeanRef.pass_return_idl_entity_types(Data.idlType2);
        if (!(ret instanceof IDLStruct[])) {
          MyUtil.logErr(
              "Could not pass sequence IDLEntity type to RMIIIOPTestBean");
          failures++;
        } else {
          for (int i = 0; i < Data.MAXOBJETS; i++) {
            if (ret[i].x != (short) i) {
              MyUtil.displayError("Wrong value", "" + i, "" + ret[i].x);
              failures++;
            }
          }
        }
      }
    } catch (Exception e) {
      MyUtil.logErr("Caught exception: " + e.getMessage());
      MyUtil.printStackTrace(e);
      MyUtil.logErr(e.toString());
      failures++;
    }
    if (failures > 0) {
      MyUtil.logMsg(subtestname + "failed");
    } else {
      MyUtil.logMsg(subtestname + "passed");
    }
    MyUtil.logDoneSubTest(subtestname);
    return failures;
  }

  /**
   * Tests CORBA type code compatibility by sending a class with all basic field
   * types (including a recursive field) into a remote method taking a
   * Serializable as a parameter. It should map to a CORBA Any on the wire in
   * RMI-IIOP.
   */

  private int pass_return_serializable_object() {
    String subtestname = "OBVTest03:(pass_return_serializable_object)";
    int failures = 0;

    MyUtil.logMsg(subtestname);
    try {
      MyUtil.logMsg("Returning a serializable from RMIIIOPTestBean");
      TypeCodeTester tct = new TypeCodeTester();
      Serializable obj = tBeanRef.pass_return_serializable_object(tct);
      TypeCodeTester o = (TypeCodeTester) obj;
      if (o.byteField != tct.byteField || o.booleanField != tct.booleanField
          || o.shortField != tct.shortField || o.intField != tct.intField
          || o.longField != tct.longField || o.floatField != tct.floatField
          || o.doubleField != tct.doubleField || o.charField != tct.charField
          || !o.stringField.equals(tct.stringField)
          || !Arrays.equals(o.byteArrayField, tct.byteArrayField)
          || !Arrays.equals(o.booleanArrayField, tct.booleanArrayField)
          || !Arrays.equals(o.shortArrayField, tct.shortArrayField)
          || !Arrays.equals(o.intArrayField, tct.intArrayField)
          || !Arrays.equals(o.longArrayField, tct.longArrayField)
          || !Arrays.equals(o.doubleArrayField, tct.doubleArrayField)
          || !Arrays.equals(o.charArrayField, tct.charArrayField)
          || !Arrays.equals(o.stringArrayField, tct.stringArrayField)
          || o.recursiveField != o) {

        MyUtil.logErr(
            "Returned serializable doesn't match expected serializable");
        failures++;
      }

    } catch (Exception e) {
      MyUtil.logErr("Caught exception: " + e.getMessage());
      MyUtil.printStackTrace(e);
      MyUtil.logErr(e.toString());
      failures++;
    }
    if (failures > 0) {
      MyUtil.logMsg(subtestname + "failed");
    } else {
      MyUtil.logMsg(subtestname + "passed");
    }
    MyUtil.logDoneSubTest(subtestname);
    return failures;
  }

}
