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

package com.sun.ts.tests.rmiiiop.ee.standalone;

import com.sun.ts.tests.rmiiiop.ee.marshaltests.*;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import com.sun.javatest.Status;

import java.util.*;
import java.rmi.*;
import javax.ejb.*;
import javax.rmi.*;
import javax.rmi.CORBA.*;
import java.io.*;
import java.net.*;
import org.omg.CORBA.ORB;
import org.omg.CORBA.Object;

public class Client extends ServiceEETest implements Serializable {
  private Properties testProps = null;

  private static final String RMIIIOPServerLookup = "RMIIIOPServer";

  private TSNamingContext nctx = null;

  private RMIIIOPTests testsRef = null;

  private CallBackInterface callbackRefs[] = new CallBackInterface[Data.MAXOBJETS];

  public static void main(String[] args) {
    Client client = new Client();
    Status s = client.run(args, System.out, System.err);
    s.exit();
  }

  /*
   * @class.setup_props: org.omg.CORBA.ORBClass; java.naming.factory.initial;
   * rmiiiopHttpServerHost; rmiiiopHttpServerPort;
   */

  public void setup(String[] args, Properties p) throws Fault {
    MyUtil.logTrace("Client.setup()");
    String ior = null;
    try {
      testProps = p;
      MyUtil.logTrace("Obtain Naming Context");
      this.nctx = new TSNamingContext();
      MyUtil.logMsg("Create HTTP URL Connection to RMIIIOPHttpServer");
      String urlString = "http://" + p.getProperty("rmiiiopHttpServerHost")
          + ":" + p.getProperty("rmiiiopHttpServerPort");
      MyUtil.logMsg("Create HTTP URL [" + urlString + "] to RMIIIOPHttpServer");
      URL url = new URL(urlString);
      MyUtil.logMsg("Open URL stream [" + urlString + "] to RMIIIOPHttpServer");
      InputStream in = url.openStream();
      MyUtil.logMsg("Read IOR from URL stream of RMIIIOPHttpServer");
      byte[] buffer = new byte[2048];
      int bytes_read = in.read(buffer);
      in.close();
      ior = new String(buffer, 0, bytes_read);
      MyUtil.logMsg("IOR = " + ior);
    } catch (Exception e) {
      MyUtil.logErr("Caught unexpected exception: " + e.getMessage());
      MyUtil.printStackTrace(e);
      MyUtil
          .logErr("FATAL-ERROR: Cannot contact the standalone RMIIIOPServer.");
      MyUtil.logErr("The standalone rmiiiop tests require that you"
          + " start the standalone");
      MyUtil.logErr("RMIIIOPServer. You start the standalone"
          + "  RMIIIOPServer by executing");
      MyUtil.logErr("the startRMIIIOPServer script in $TS_HOME/bin."
          + " Refer to the TS");
      MyUtil.logErr("Users Guide which has more info on the"
          + " standalone rmiiiop tests.");
      throw new Fault("Setup failed:");
    }
    try {
      MyUtil.logMsg("Create default J2EE ORB using ORB.init()");
      ORB orb = (ORB) ORB.init(new String[0], null);
      MyUtil.logMsg("ORB = " + orb);
      MyUtil.logMsg("Using stringified IOR to get object reference");
      MyUtil.logMsg("Convert stringified IOR to a CORBA object");
      org.omg.CORBA.Object obj = orb.string_to_object(ior);
      MyUtil.logMsg("Narrow CORBA object to get object reference");
      testsRef = (RMIIIOPTests) PortableRemoteObject.narrow(obj,
          RMIIIOPTests.class);
    } catch (Exception e) {
      MyUtil.logErr(
          "FATAL-ERROR: Could not create ORB via [ORB.init()] or could not");
      MyUtil.logErr("convert stringified IOR to a CORBA object.");
      MyUtil.logErr("Exception: " + e.getMessage());
      MyUtil.printStackTrace(e);
      throw new Fault("Setup failed:");
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
   * @assertion_ids: RMIIIOP:SPEC:1000; JavaEE:SPEC:200;
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
   * @assertion_ids: RMIIIOP:SPEC:1000; RMIIIOP:SPEC:1003; JavaEE:SPEC:200;
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
   * @assertion_ids: RMIIIOP:SPEC:1004; JavaEE:SPEC:200;
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
   * @testName: RITest01
   * 
   * @assertion_ids: RMIIIOP:SPEC:1001; JavaEE:SPEC:200;
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
   * @assertion_ids: RMIIIOP:SPEC:1001; RMIIIOP:SPEC:1003; JavaEE:SPEC:200;
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
   *
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
   * @testName: RITest03
   * 
   * @assertion_ids: RMIIIOP:SPEC:1005; JavaEE:SPEC:200;
   * 
   * @assertion: When passing a remote interface as an argument in a method call
   * or returning a remote interface as a return value from a method call the
   * actual object that is passed is a stub object. (Chapter 1.2.3.1 Java
   * Language to IDL Mapping) (Chapter 19 EJB Specification [See EJB
   * assertions])
   * 
   * @test_Strategy: Test passing a remote interface as an argument and verify
   * on the server side that the object is stub object. Test returning a remote
   * interface as an argument and verify on the client side that the object is a
   * stub object.
   *
   */

  public void RITest03() throws Fault {
    String testname = "RITest03";
    int failures = 0;

    try {
      MyUtil.logTrace(testname);
      failures += return_verify_stub();
      failures += pass_verify_stub();
      if (failures > 0)
        throw new Fault(testname + " failed");
    } catch (Exception e) {
      throw new Fault(testname + " failed", e);
    }
  }

  /*
   * @testName: OBVTest01
   * 
   * @assertion_ids: RMIIIOP:SPEC:1002; JavaEE:SPEC:200;
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
      if (failures > 0)
        throw new Fault(testname + " failed");
    } catch (Exception e) {
      throw new Fault(testname + " failed", e);
    }
  }

  /*
   * @testName: OBVTest02
   * 
   * @assertion_ids: RMIIIOP:SPEC:1002; RMIIIOP:SPEC:1003; JavaEE:SPEC:200;
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
      if (failures > 0)
        throw new Fault(testname + " failed");
    } catch (Exception e) {
      throw new Fault(testname + " failed", e);
    }
  }

  /*
   * @testName: OBVTest03
   * 
   * @assertion_ids: RMIIIOP:SPEC:1002; JavaEE:SPEC:200;
   * 
   * @assertion: Passing a graph of Java objects by value is allowed in
   * RMI-IIOP. An RMI/IDL value type represents a class whose values can be
   * moved between systems. So rather than transmitting a reference between
   * systems, the actual state of the object is transmitted between systems.
   * This requires that the receiving system have an analogous class that can be
   * used to hold the received value. When passing or returning a graph of Java
   * objects by value all of the Java objects in the graph are traversed and
   * transmitted across an RMI/IDL remote interface at run time. (Chapter 1.2.4
   * Java Language to IDL Mapping) (Chapter 19 EJB Specification [See EJB
   * assertions])
   * 
   * @test_Strategy: Test passing a graph of objects by value as an argument and
   * verify that the graph of objects is correct. Test returning a graph of
   * objects by value as a return value and verify that the graph of objects is
   * correct.
   *
   */

  public void OBVTest03() throws Fault {
    String testname = "OBVTest03";
    int failures = 0;

    try {
      MyUtil.logTrace(testname);
      failures += pass_a_graph_of_objects();
      failures += return_a_graph_of_objects();
      if (failures > 0)
        throw new Fault(testname + " failed");
    } catch (Exception e) {
      throw new Fault(testname + " failed", e);
    }
  }

  /*
   * @testName: OBVTest04
   * 
   * @assertion_ids: RMIIIOP:SPEC:1002; RMIIIOP:SPEC:1003; JavaEE:SPEC:200;
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
   * @assertion_ids: RMIIIOP:SPEC:1002; RMIIIOP:SPEC:1003; JavaEE:SPEC:200;
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

  private int pass_a_boolean() {
    String subtestname = "PrimTest01:(pass_a_boolean)";
    int failures = 0;

    MyUtil.logMsg(subtestname);
    try {
      boolean ret = false;
      boolean saveret = false;
      MyUtil.logMsg("Passing/Returning boolean to/from RMIIIOPServer");
      for (int i = 0; i < Data.boolean_data.length; i++) {
        ret = testsRef.pass_a_boolean(i, Data.boolean_data[i]);
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
      MyUtil.logMsg("Passing/Returning byte to/from RMIIIOPServer");
      for (int i = 0; i < Data.byte_data.length; i++) {
        ret = testsRef.pass_a_byte(i, Data.byte_data[i]);
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
      MyUtil.logMsg("Passing/Returning char to/from RMIIIOPServer");
      for (int i = 0; i < Data.char_data.length; i++) {
        ret = testsRef.pass_a_char(i, Data.char_data[i]);
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
      MyUtil.logMsg("Passing/Returning short to/from RMIIIOPServer");
      for (int i = 0; i < Data.short_data.length; i++) {
        ret = testsRef.pass_a_short(i, Data.short_data[i]);
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
      MyUtil.logMsg("Passing/Returning int to/from RMIIIOPServer");
      for (int i = 0; i < Data.int_data.length; i++) {
        ret = testsRef.pass_a_int(i, Data.int_data[i]);
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
      MyUtil.logMsg("Passing/Returning long to/from RMIIIOPServer");
      for (int i = 0; i < Data.long_data.length; i++) {
        ret = testsRef.pass_a_long(i, Data.long_data[i]);
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
      MyUtil.logMsg("Passing/Returning float to/from RMIIIOPServer");
      for (int i = 0; i < Data.float_data.length; i++) {
        ret = testsRef.pass_a_float(i, Data.float_data[i]);
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
      MyUtil.logMsg("Passing/Returning double to/from RMIIIOPServer");
      for (int i = 0; i < Data.double_data.length; i++) {
        ret = testsRef.pass_a_double(i, Data.double_data[i]);
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
      MyUtil.logMsg("Passing/Returning boolean array to/from RMIIIOPServer");
      boolean ret[] = testsRef.pass_a_boolean_array(Data.boolean_data);
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
      MyUtil.logMsg("Passing/Returning byte array to/from RMIIIOPServer");
      byte ret[] = testsRef.pass_a_byte_array(Data.byte_data);
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
      MyUtil.logMsg("Passing/Returning char array to/from RMIIIOPServer");
      char ret[] = testsRef.pass_a_char_array(Data.char_data);
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
      MyUtil.logMsg("Passing/Returning short array to/from RMIIIOPServer");
      short ret[] = testsRef.pass_a_short_array(Data.short_data);
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
      MyUtil.logMsg("Passing/Returning int array to/from RMIIIOPServer");
      int ret[] = testsRef.pass_a_int_array(Data.int_data);
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
      MyUtil.logMsg("Passing/Returning long array to/from RMIIIOPServer");
      long ret[] = testsRef.pass_a_long_array(Data.long_data);
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
      MyUtil.logMsg("Passing/Returning float array to/from RMIIIOPServer");
      float ret[] = testsRef.pass_a_float_array(Data.float_data);
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
      MyUtil.logMsg("Passing/Returning double array to/from RMIIIOPServer");
      double ret[] = testsRef.pass_a_double_array(Data.double_data);
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
      MyUtil.logMsg("Throw a user defined exception from RMIIIOPServer");
      testsRef.throw_a_user_exception();
      MyUtil.logMsg("No UserException was thrown");
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

  private int pass_a_remote_interface() {
    String subtestname = "RITest01:(pass_a_remote_interface)";
    int failures = 0;

    MyUtil.logMsg(subtestname);
    try {
      MyUtil.logMsg("Passing a Remote Interface to RMIIIOPServer");
      int ret = testsRef.pass_a_remote_interface(callbackRefs[0]);
      if (ret != 0) {
        MyUtil.logErr("Could not pass a Remote Interface to RMIIIOPServer");
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
      MyUtil.logMsg("Returning a Remote Interface from RMIIIOPServer");
      callbackRefs[0] = testsRef.return_a_remote_interface();
      if (callbackRefs[0] == null) {
        MyUtil.logErr("Could not return a Remote Interface from RMIIIOPServer");
        failures++;
      } else {
        try {
          MyUtil
              .logMsg("Invoking the methods of the returned Remote Interface");
          callbackRefs[0].method1();
          callbackRefs[0].method2();
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
      MyUtil.logMsg("Passing array of Remote Interfaces to RMIIIOPServer");
      int ret = testsRef.pass_array_of_remote_interfaces(callbackRefs);
      if (ret != 0) {
        MyUtil.logErr(
            "Could not pass array of Remote Interfaces to RMIIIOPServer");
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
      MyUtil.logMsg("Returning array of Remote Interfaces from RMIIIOPServer");
      callbackRefs = testsRef.return_array_of_remote_interfaces();
      if (callbackRefs == null) {
        MyUtil.logErr("Could not return array of Remote Interfaces"
            + " from RMIIIOPServer");
        failures++;
      } else if (callbackRefs.length != Data.MAXOBJETS) {
        MyUtil.displayError("Wrong length", "" + Data.MAXOBJETS,
            "" + callbackRefs.length);
        failures++;
      }
      if (callbackRefs != null) {
        for (int i = 0; i < callbackRefs.length; i++) {
          try {
            MyUtil
                .logMsg("Invoking the methods of the returned Remote Interface"
                    + " refs[" + i + "]");
            callbackRefs[i].method1();
            callbackRefs[i].method2();
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

  private int pass_verify_stub() {
    String subtestname = "RITest03:(pass_verify_stub)";
    int failures = 0;

    MyUtil.logMsg(subtestname);
    try {
      MyUtil.logMsg("Passing a Remote Interface to RMIIIOPServer");
      MyUtil.logMsg("Verify that the object passed is a stub object");
      int ret = testsRef.pass_verify_stub(callbackRefs[0]);
      if (ret != 0) {
        MyUtil.logErr("Could not pass a Remote Interface to RMIIIOPServer");
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

  private int return_verify_stub() {
    String subtestname = "RITest03:(return_verify_stub)";
    int failures = 0;

    MyUtil.logMsg(subtestname);
    try {
      MyUtil.logMsg("Returning a Remote Interface from RMIIIOPServer");
      MyUtil.logMsg("Verify that the object returned is a stub object");
      callbackRefs[0] = testsRef.return_verify_stub();
      MyUtil.logMsg("Returned reference=" + callbackRefs[0]);
      if (callbackRefs[0] == null) {
        MyUtil.logErr("Could not return a Remote Interface from RMIIIOPServer");
        failures++;
      } else {
        try {
          MyUtil.logMsg("Invoking the methods of the returned Stub Object");
          callbackRefs[0].method1();
          callbackRefs[0].method2();
        } catch (Exception e) {
          MyUtil.logErr(
              "Unable to invoke the methods of the returned Stub Object");
          MyUtil.logErr("Caught exception: " + e.getMessage());
          MyUtil.printStackTrace(e);
          MyUtil.logErr(e.toString());
          failures++;
        }
        if (callbackRefs[0] instanceof javax.rmi.CORBA.Stub) {
          MyUtil.logMsg("Returned reference is of type javax.rmi.CORBA.Stub");
        } else {
          MyUtil.logMsg("Returned reference is of type CallBackInterface");
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
      MyUtil.logMsg("Passing a Date object to RMIIIOPServer");
      int ret = testsRef.pass_a_date_object(Data.date);
      if (ret != 0) {
        MyUtil.logErr("Could not pass a Date object to RMIIIOPServer");
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
      MyUtil.logMsg("Passing any Java object to RMIIIOPServer");
      int ret = testsRef.pass_any_object(Data.bitset);
      if (ret != 0) {
        MyUtil.logErr("Could not pass a BitSet object to RMIIIOPServer");
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
      MyUtil.logMsg("Returning a Date object from RMIIIOPServer");
      Date d = testsRef.return_a_date_object();
      if (d == null) {
        MyUtil.logErr("Could not return a Date object from RMIIIOPServer");
        failures++;
      } else {
        if (!d.equals(Data.date)) {
          MyUtil.logErr(
              "Returned Date object doesn't match expected Date object");
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

  private int return_any_object() {
    String subtestname = "OBVTest01:(return_any_object)";
    int failures = 0;

    MyUtil.logMsg(subtestname);
    try {
      MyUtil.logMsg("Returning any Java object from RMIIIOPServer");
      java.lang.Object o = testsRef.return_any_object();
      if (!(o instanceof BitSet)) {
        MyUtil.logErr("Could not return a BitSet object from RMIIIOPServer");
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

  private int pass_a_date_object_array() {
    String subtestname = "OBVTest02:(pass_a_date_object_array)";
    int failures = 0;

    MyUtil.logMsg(subtestname);
    try {
      MyUtil.logMsg("Passing a Date object array to RMIIIOPServer");
      int ret = testsRef.pass_a_date_object_array(Data.date_array);
      if (ret != 0) {
        MyUtil.logErr("Could not pass a Date object array to RMIIIOPServer");
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

  private int pass_any_object_array() {
    String subtestname = "OBVTest02:(pass_any_object_array)";
    int failures = 0;

    MyUtil.logMsg(subtestname);
    try {
      MyUtil.logMsg("Passing any Java object array to RMIIIOPServer");
      int ret = testsRef.pass_any_object_array(Data.bitset_array);
      if (ret != 0) {
        MyUtil.logErr("Could not pass a BitSet object array to RMIIIOPServer");
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
      MyUtil.logMsg("Returning a Date object array from RMIIIOPServer");
      Date d[] = testsRef.return_a_date_object_array();
      if (d.length != Data.date_array.length) {
        MyUtil
            .logErr("Could not return a Date object array from RMIIIOPServer");
        MyUtil.displayError("Wrong length", "" + Data.date_array.length,
            "" + d.length);
        failures++;
      }
      for (int i = 0; i < d.length; i++) {
        if (d[i] == null) {
          MyUtil.logErr(
              "Could not return a Date object array from RMIIIOPServer");
          MyUtil.logErr("d[" + i + "] is null (unexpected)");
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
      MyUtil.logMsg("Returning any Java object array from RMIIIOPServer");
      java.lang.Object o[] = testsRef.return_any_object_array();
      if (o.length != Data.bitset_array.length) {
        MyUtil
            .logErr("Could not return BitSet object array from RMIIIOPServer");
        MyUtil.displayError("Wrong length", "" + Data.bitset_array.length,
            "" + o.length);
        failures++;
      }
      for (int i = 0; i < o.length; i++) {
        if (!(o[i] instanceof BitSet)) {
          MyUtil.logErr(
              "Could not return a BitSet object array from RMIIIOPServer");
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
      MyUtil.logMsg("Passing a Graph object to RMIIIOPServer");
      int ret = testsRef.pass_a_graph_of_objects(Data.graph);
      if (ret != 0) {
        MyUtil.logErr("Could not pass a Graph object to RMIIIOPServer");
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
      MyUtil.logMsg("Returning a Graph object from RMIIIOPServer");
      Graph g = testsRef.return_a_graph_of_objects();
      if (g == null) {
        MyUtil.logErr("Could not return a Graph object from RMIIIOPServer");
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
      MyUtil.logMsg("Passing array of Graph objects to RMIIIOPServer");
      int ret = testsRef.pass_array_of_graph_objects(Data.graph_array);
      if (ret != 0) {
        MyUtil.logErr("Could not pass array of Graph objects to RMIIIOPServer");
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
      MyUtil.logMsg("Returning array of Graph objects from RMIIIOPServer");
      Graph g[] = testsRef.return_array_of_graph_objects();
      for (int i = 0; i < g.length; i++) {
        if (g[i] == null) {
          MyUtil.logErr(
              "Could not return a array of Graph objects from RMIIIOPServer");
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
          + "array to RMIIIOPServer");
      MyUtil.logMsg("Please wait this method call can take a long time ....");
      timer.start();
      w = testsRef.pass_return_object3_array(main);
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
      MyUtil.logMsg("Passing/Returning Multi Class Types to RMIIIOPServer");
      Multi ret = null;
      ret = testsRef.pass_return_multiclass_types(Data.multiTypes);
      if (ret == null) {
        MyUtil.logErr("Could not return Multi Class Types from RMIIIOPServer");
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
      MyUtil.logMsg("Passing/Returning IDLEntity Types to RMIIIOPServer");
      if (idltype.equals("struct")) {
        MyUtil
            .logMsg("Passing/Returning struct IDLEntity type to RMIIIOPServer");
        IDLStruct ret = null;
        ret = testsRef.pass_return_idl_entity_types(Data.idlType1);
        if (ret == null) {
          MyUtil.logErr(
              "Could not return struct IDLEntity type from RMIIIOPServer");
          failures++;
        } else if (ret.x != Data.idlType1_value) {
          MyUtil.displayError("Wrong value", "" + Data.idlType1_value,
              "" + ret.x);
          failures++;
        }
      } else if (idltype.equals("sequence")) {
        MyUtil.logMsg(
            "Passing/Returning sequence IDLEntity type to RMIIIOPServer");
        IDLStruct[] ret = null;
        ret = testsRef.pass_return_idl_entity_types(Data.idlType2);
        if (ret == null) {
          MyUtil.logErr(
              "Could not return sequence IDLEntity type from RMIIIOPServer");
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
}
