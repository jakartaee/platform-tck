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

import java.util.*;
import java.io.*;
import java.rmi.*;
import java.net.*;
import javax.rmi.*;
import javax.rmi.CORBA.*;
import javax.naming.*;
import org.omg.CORBA.ORB;
import org.omg.PortableServer.*;

public class RMIIIOPServer extends PortableRemoteObject
    implements RMIIIOPTests {
  private static final String RMIIIOPServerJNDIName = "RMIIIOPServer";

  private static final int DEFAULT_RMIIIOP_HTTP_SERVER_PORT = 10000;

  private static CallBackInterface callbackRefs[] = new CallBackInterface[Data.MAXOBJETS];

  private int errors = 0;

  private static boolean verbose = false;

  public RMIIIOPServer() throws RemoteException {
    super();
  }

  public boolean pass_a_boolean(int idx, boolean p1) throws RemoteException {
    String methodName = "pass_a_boolean";

    if (verbose)
      PrintUtil.logTrace(methodName);
    boolean ret = false;
    if (p1 != Data.boolean_data[idx]) {
      errors++;
      ret = true;
      PrintUtil.displayError(methodName + ": p1 failed",
          "" + Data.boolean_data[idx], "" + p1);
    }
    return (ret);
  }

  public byte pass_a_byte(int idx, byte p1) throws RemoteException {
    String methodName = "pass_a_byte";

    if (verbose)
      PrintUtil.logTrace(methodName);
    byte ret = 0;
    if (p1 != Data.byte_data[idx]) {
      errors++;
      ret = 1;
      PrintUtil.displayError(methodName + ": p1 failed for idx=" + idx,
          "" + Data.byte_data[idx], "" + p1);
    }
    return (ret);
  }

  public char pass_a_char(int idx, char p1) throws RemoteException {
    String methodName = "pass_a_char";

    if (verbose)
      PrintUtil.logTrace(methodName);
    char ret = '0';
    if (p1 != Data.char_data[idx]) {
      errors++;
      ret = '1';
      PrintUtil.displayError(methodName + ": p1 failed for idx=" + idx,
          "" + Data.char_data[idx], "" + p1);
    }
    return (ret);
  }

  public short pass_a_short(int idx, short p1) throws RemoteException {
    String methodName = "pass_a_short";

    if (verbose)
      PrintUtil.logTrace(methodName);
    short ret = 0;
    if (p1 != Data.short_data[idx]) {
      errors++;
      ret = 1;
      PrintUtil.displayError(methodName + ": p1 failed for idx=" + idx,
          "" + Data.short_data[idx], "" + p1);
    }
    return (ret);
  }

  public int pass_a_int(int idx, int p1) throws RemoteException {
    String methodName = "pass_a_int";

    if (verbose)
      PrintUtil.logTrace(methodName);
    int ret = 0;
    if (p1 != Data.int_data[idx]) {
      errors++;
      ret = 1;
      PrintUtil.displayError(methodName + ": p1 failed for idx=" + idx,
          "" + Data.int_data[idx], "" + p1);
    }
    return (ret);
  }

  public long pass_a_long(int idx, long p1) throws RemoteException {
    String methodName = "pass_a_long";

    if (verbose)
      PrintUtil.logTrace(methodName);
    long ret = 0;
    if (p1 != Data.long_data[idx]) {
      errors++;
      ret = 1;
      PrintUtil.displayError(methodName + ": p1 failed for idx=" + idx,
          "" + Data.long_data[idx], "" + p1);
    }
    return (ret);
  }

  public float pass_a_float(int idx, float p1) throws RemoteException {
    String methodName = "pass_a_float";

    if (verbose)
      PrintUtil.logTrace(methodName);
    float ret = (float) 0.0;
    if (p1 != Data.float_data[idx]) {
      errors++;
      ret = (float) 1.0;
      PrintUtil.displayError(methodName + ": p1 failed for idx=" + idx,
          "" + Data.float_data[idx], "" + p1);
    }
    return (ret);
  }

  public double pass_a_double(int idx, double p1) throws RemoteException {
    String methodName = "pass_a_double";

    if (verbose)
      PrintUtil.logTrace(methodName);
    double ret = (double) 0.0;
    if (p1 != Data.double_data[idx]) {
      errors++;
      ret = (double) 1.0;
      PrintUtil.displayError(methodName + ": p1 failed for idx=" + idx,
          "" + Data.double_data[idx], "" + p1);
    }
    return (ret);
  }

  public boolean[] pass_a_boolean_array(boolean p1[]) throws RemoteException {
    String methodName = "pass_a_boolean_array";

    if (verbose)
      PrintUtil.logTrace(methodName);
    boolean ret[] = new boolean[Data.boolean_data.length];
    ret[0] = false;
    if (p1.length != Data.boolean_data.length) {
      errors++;
      ret[0] = true;
      PrintUtil.displayError(
          methodName + ": p1.length != Data.boolean_data.length",
          "" + Data.boolean_data.length, "" + p1.length);
    }
    for (int i = 0; i < p1.length; i++) {
      if (p1[i] != Data.boolean_data[i]) {
        errors++;
        ret[0] = true;
        PrintUtil.displayError(methodName + ": p1[" + i + "] failed",
            "" + Data.boolean_data[i], "" + p1[i]);
      }
    }
    return (ret);
  }

  public byte[] pass_a_byte_array(byte p1[]) throws RemoteException {
    String methodName = "pass_a_byte_array";

    if (verbose)
      PrintUtil.logTrace(methodName);
    byte ret[] = new byte[Data.byte_data.length];
    for (int i = 0; i < ret.length; i++)
      ret[i] = Data.byte_data[i];
    ret[0] = 0;
    if (p1.length != Data.byte_data.length) {
      errors++;
      ret[0] = 1;
      PrintUtil.displayError(
          methodName + ": p1.length != Data.byte_data.length",
          "" + Data.byte_data.length, "" + p1.length);
    }
    for (int i = 0; i < p1.length; i++) {
      if (p1[i] != Data.byte_data[i]) {
        errors++;
        ret[0] = 1;
        PrintUtil.displayError(methodName + ": p1[" + i + "] failed",
            "" + Data.byte_data[i], "" + p1[i]);
      }
    }
    return (ret);
  }

  public char[] pass_a_char_array(char p1[]) throws RemoteException {
    String methodName = "pass_a_char_array";

    if (verbose)
      PrintUtil.logTrace(methodName);
    char ret[] = new char[Data.char_data.length];
    for (int i = 0; i < ret.length; i++)
      ret[i] = Data.char_data[i];
    ret[0] = '0';
    if (p1.length != Data.char_data.length) {
      errors++;
      ret[0] = '1';
      PrintUtil.displayError(
          methodName + ": p1.length != Data.char_data.length",
          "" + Data.char_data.length, "" + p1.length);
    }
    for (int i = 0; i < p1.length; i++) {
      if (p1[i] != Data.char_data[i]) {
        errors++;
        ret[0] = '1';
        PrintUtil.displayError(methodName + ": p1[" + i + "] failed",
            "" + Data.char_data[i], "" + p1[i]);
      }
    }
    return (ret);
  }

  public short[] pass_a_short_array(short p1[]) throws RemoteException {
    String methodName = "pass_a_short_array";

    if (verbose)
      PrintUtil.logTrace(methodName);
    short ret[] = new short[Data.short_data.length];
    for (int i = 0; i < ret.length; i++)
      ret[i] = Data.short_data[i];
    ret[0] = 0;
    if (p1.length != Data.short_data.length) {
      errors++;
      ret[0] = 1;
      PrintUtil.displayError(
          methodName + ": p1.length != Data.short_data.length",
          "" + Data.short_data.length, "" + p1.length);
    }
    for (int i = 0; i < p1.length; i++) {
      if (p1[i] != Data.short_data[i]) {
        errors++;
        ret[0] = 1;
        PrintUtil.displayError(methodName + ": p1[" + i + "] failed",
            "" + Data.short_data[i], "" + p1[i]);
      }
    }
    return (ret);
  }

  public int[] pass_a_int_array(int p1[]) throws RemoteException {
    String methodName = "pass_a_int_array";

    if (verbose)
      PrintUtil.logTrace(methodName);
    int ret[] = new int[Data.int_data.length];
    for (int i = 0; i < ret.length; i++)
      ret[i] = Data.int_data[i];
    ret[0] = 0;
    if (p1.length != Data.int_data.length) {
      errors++;
      ret[0] = 1;
      PrintUtil.displayError(methodName + ": p1.length != Data.int_data.length",
          "" + Data.int_data.length, "" + p1.length);
    }
    for (int i = 0; i < p1.length; i++) {
      if (p1[i] != Data.int_data[i]) {
        errors++;
        ret[0] = 1;
        PrintUtil.displayError(methodName + ": p1[" + i + "] failed",
            "" + Data.int_data[i], "" + p1[i]);
      }
    }
    return (ret);
  }

  public long[] pass_a_long_array(long p1[]) throws RemoteException {
    String methodName = "pass_a_long_array";

    if (verbose)
      PrintUtil.logTrace(methodName);
    long ret[] = new long[Data.long_data.length];
    for (int i = 0; i < ret.length; i++)
      ret[i] = Data.long_data[i];
    ret[0] = 0;
    if (p1.length != Data.long_data.length) {
      errors++;
      ret[0] = 1;
      PrintUtil.displayError(
          methodName + ": p1.length != Data.long_data.length",
          "" + Data.long_data.length, "" + p1.length);
    }
    for (int i = 0; i < p1.length; i++) {
      if (p1[i] != Data.long_data[i]) {
        errors++;
        ret[0] = 1;
        PrintUtil.displayError(methodName + ": p1[" + i + "] failed",
            "" + Data.long_data[i], "" + p1[i]);
      }
    }
    return (ret);
  }

  public float[] pass_a_float_array(float p1[]) throws RemoteException {
    String methodName = "pass_a_float_array";

    if (verbose)
      PrintUtil.logTrace(methodName);
    float ret[] = new float[Data.float_data.length];
    for (int i = 0; i < ret.length; i++)
      ret[i] = Data.float_data[i];
    ret[0] = (float) 0.0;
    if (p1.length != Data.float_data.length) {
      errors++;
      ret[0] = (float) 1.0;
      PrintUtil.displayError(
          methodName + ": p1.length != Data.float_data.length",
          "" + Data.float_data.length, "" + p1.length);
    }
    for (int i = 0; i < p1.length; i++) {
      if (p1[i] != Data.float_data[i]) {
        errors++;
        ret[0] = (float) 1.0;
        PrintUtil.displayError(methodName + ": p1[" + i + "] failed",
            "" + Data.float_data[i], "" + p1[i]);
      }
    }
    return (ret);
  }

  public double[] pass_a_double_array(double p1[]) throws RemoteException {
    String methodName = "pass_a_double_array";

    if (verbose)
      PrintUtil.logTrace(methodName);
    double ret[] = new double[Data.double_data.length];
    for (int i = 0; i < ret.length; i++)
      ret[i] = Data.double_data[i];
    ret[0] = (double) 0.0;
    if (p1.length != Data.double_data.length) {
      errors++;
      ret[0] = (double) 1.0;
      PrintUtil.displayError(
          methodName + ": p1.length != Data.double_data.length",
          "" + Data.double_data.length, "" + p1.length);
    }
    for (int i = 0; i < p1.length; i++) {
      if (p1[i] != Data.double_data[i]) {
        errors++;
        ret[0] = (double) 1.0;
        PrintUtil.displayError(methodName + ": p1[" + i + "] failed",
            "" + Data.double_data[i], "" + p1[i]);
      }
    }
    return (ret);
  }

  public int pass_a_date_object(Date p1) throws RemoteException {
    String methodName = "pass_a_date_object";

    if (verbose)
      PrintUtil.logTrace(methodName);
    int ret = 0;
    if (p1 == null) {
      errors++;
      ret = 1;
      PrintUtil.displayError(methodName + ": p1 is null (unexpected)");
    } else if (!p1.equals(Data.date)) {
      errors++;
      ret = 1;
      PrintUtil.displayError(methodName + ": p1 is not expected Date object");
    }
    return ret;
  }

  public int pass_any_object(java.lang.Object p1) throws RemoteException {
    String methodName = "pass_any_object";

    if (verbose)
      PrintUtil.logTrace(methodName);
    int ret = 0;
    if (!(p1 instanceof BitSet)) {
      errors++;
      ret = 1;
      PrintUtil.displayError(methodName + ": p1 is not a BitSet object");
    }
    if (!p1.equals(Data.bitset)) {
      errors++;
      ret = 1;
      PrintUtil.displayError(methodName + ": p1 is not expected BitSet object");
    }
    return 0;
  }

  public Date return_a_date_object() throws RemoteException {
    String methodName = "return_a_date_object";

    if (verbose)
      PrintUtil.logTrace(methodName);
    return Data.date;
  }

  public java.lang.Object return_any_object() throws RemoteException {
    String methodName = "return_any_object";

    if (verbose)
      PrintUtil.logTrace(methodName);
    return Data.bitset;
  }

  public int pass_a_date_object_array(Date p1[]) throws RemoteException {
    String methodName = "pass_a_date_object_array";

    if (verbose)
      PrintUtil.logTrace(methodName);
    int ret = 0;
    if (p1 == null) {
      errors++;
      ret = 1;
      PrintUtil.displayError(methodName + ": p1 is null (unexpected)");
    } else {
      if (p1.length != Data.date_array.length) {
        errors++;
        ret = 1;
        PrintUtil.displayError(
            methodName + ": p1.length != Data.date_array.length",
            "" + Data.date_array.length, "" + p1.length);
      }
      for (int i = 0; i < p1.length; i++) {
        if (p1[i] == null) {
          errors++;
          ret = 1;
          PrintUtil.displayError(
              methodName + ": p1[" + i + "] is not null (unexpected)");
        } else if (!p1[i].equals(Data.date_array[i])) {
          errors++;
          ret = 1;
          PrintUtil.displayError(
              methodName + ": p1[" + i + "] is not expected Date object");
        }
      }
    }
    return ret;
  }

  public int pass_any_object_array(java.lang.Object p1[])
      throws RemoteException {
    String methodName = "pass_any_object_array";

    if (verbose)
      PrintUtil.logTrace(methodName);
    int ret = 0;
    if (p1 == null) {
      errors++;
      ret = 1;
      PrintUtil.displayError(methodName + ": p1 is null (unexpected)");
    } else {
      if (p1.length != Data.bitset_array.length) {
        errors++;
        ret = 1;
        PrintUtil.displayError(
            methodName + ": p1.length != Data.bitset_array.length",
            "" + Data.bitset_array.length, "" + p1.length);
      }
      for (int i = 0; i < p1.length; i++) {
        if (!(p1[i] instanceof BitSet)) {
          errors++;
          ret = 1;
          PrintUtil.displayError(
              methodName + ": p1[" + i + "] is not a BitSet object");
        }
        if (!p1[i].equals(Data.bitset_array[i])) {
          errors++;
          ret = 1;
          PrintUtil.displayError(
              methodName + ": p1[" + i + "] is not expected BitSet object");
        }
      }
    }
    return ret;
  }

  public Date[] return_a_date_object_array() throws RemoteException {
    String methodName = "return_a_date_object_array";

    if (verbose)
      PrintUtil.logTrace(methodName);
    return Data.date_array;
  }

  public java.lang.Object[] return_any_object_array() throws RemoteException {
    String methodName = "return_any_object_array";

    if (verbose)
      PrintUtil.logTrace(methodName);
    return Data.bitset_array;
  }

  public java.lang.Object[][][] pass_return_object3_array(
      java.lang.Object[][][] obj3Array) throws RemoteException {
    String methodName = "pass_return_object3_array";
    if (verbose)
      PrintUtil.logTrace(methodName);
    return obj3Array;
  }

  public int pass_a_remote_interface(CallBackInterface p1)
      throws RemoteException {
    String methodName = "pass_a_remote_interface";

    if (verbose)
      PrintUtil.logTrace(methodName);
    int ret = 0;
    if (p1 == null) {
      PrintUtil.displayError(
          methodName + ": Could not receive a Remote Interface (p1 is null)");
      ret = 1;
      errors++;
    } else {
      try {
        p1.method1();
        p1.method2();
      } catch (Exception e) {
        PrintUtil.displayError(methodName
            + ": Unable to invoke the methods of the Remote Interface");
        PrintUtil
            .displayError(methodName + ": Caught exception: " + e.getMessage());
        PrintUtil.printStackTrace(e);
        ret = 1;
        errors++;
      }
    }
    return ret;
  }

  public CallBackInterface return_a_remote_interface() throws RemoteException {
    String methodName = "return_a_remote_interface";

    if (verbose)
      PrintUtil.logTrace(methodName);
    return callbackRefs[0];
  }

  public int pass_array_of_remote_interfaces(CallBackInterface p1[])
      throws RemoteException {
    String methodName = "pass_array_of_remote_interfaces";

    if (verbose)
      PrintUtil.logTrace(methodName);
    int ret = 0;
    if (p1 == null) {
      PrintUtil.displayError(methodName
          + ": Could not receive array of Remote Interfaces (p1 is null)");
      ret = 1;
      errors++;
    } else if (p1.length != Data.MAXOBJETS) {
      PrintUtil.displayError(methodName + ": p1.length != Data.MAXOBJETS",
          "" + Data.MAXOBJETS, "" + p1.length);
      ret = 1;
      errors++;
      for (int i = 0; i < p1.length; i++) {
        try {
          p1[i].method1();
          p1[i].method2();
        } catch (Exception e) {
          PrintUtil.displayError(methodName
              + ": Unable to invoke the methods of the Remote Interface p1[" + i
              + "]");
          PrintUtil.displayError(
              methodName + ": Caught exception: " + e.getMessage());
          PrintUtil.printStackTrace(e);
          ret = 1;
          errors++;
        }
      }
    }
    return ret;
  }

  public CallBackInterface[] return_array_of_remote_interfaces()
      throws RemoteException {
    String methodName = "return_array_of_remote_interfaces";

    if (verbose)
      PrintUtil.logTrace(methodName);
    return callbackRefs;
  }

  public int pass_verify_stub(CallBackInterface p1) throws RemoteException {
    String methodName = "pass_verify_stub";

    if (verbose)
      PrintUtil.logTrace(methodName);
    int ret = 0;
    if (p1 == null) {
      PrintUtil.displayError(
          methodName + ": Could not receive a Remote Interface (p1 is null)");
      ret = 1;
      errors++;
    } else {
      if (p1 instanceof javax.rmi.CORBA.Stub) {
        PrintUtil.logMsg(
            methodName + ": Passed reference is of type javax.rmi.CORBA.Stub");
        ;
      } else {
        PrintUtil.displayError(methodName
            + ": Passed reference is not of type javax.rmi.CORBA.Stub");
        PrintUtil.displayError(methodName + ": A portable RMIIIOP"
            + " stub must inherit from javax.rmi.CORBA.Stub");
        ret = 1;
        errors++;
        ;
      }
    }
    return ret;
  }

  public CallBackInterface return_verify_stub() throws RemoteException {
    String methodName = "return_verify_stub";

    if (verbose)
      PrintUtil.logTrace(methodName);
    return callbackRefs[0];
  }

  public void throw_a_user_exception() throws RemoteException, UserException {
    String methodName = "throw_a_user_exception";

    if (verbose)
      PrintUtil.logTrace(methodName);
    throw new UserException("This is a UserException test");
  }

  public int pass_a_graph_of_objects(Graph p1) throws RemoteException {
    String methodName = "pass_a_graph_of_objects";

    int ret = 0;
    if (p1 == null) {
      ret = 1;
      PrintUtil.displayError(methodName + ": p1 is null (unexpected)");
    }

    if (verbose)
      PrintUtil.logTrace(methodName);
    StringBuffer result = new StringBuffer("{ ");
    for (Graph list = p1, list2 = Data.graph; list != null
        && list2 != null; list = list.next(), list2 = list2.next()) {
      result.append(list.data()).append(" ");
      if (!list.CONSTANT_STRING.equals(list2.CONSTANT_STRING)) {
        ret = 1;
        errors++;
        PrintUtil.displayError(
            methodName + ": p1.CONSTANT_STRING != list2.CONSTANT_STRING");
        PrintUtil.displayError(methodName + ": p1.CONSTANT_STRING",
            "" + list2.CONSTANT_STRING, "" + p1.CONSTANT_STRING);
      }
      if (list.CONSTANT_INT != list2.CONSTANT_INT) {
        ret = 1;
        errors++;
        PrintUtil.displayError(
            methodName + ": p1.CONSTANT_INT != list2.CONSTANT_INT");
        PrintUtil.displayError(methodName + ": p1.CONSTANT_INT",
            "" + list2.CONSTANT_INT, "" + p1.CONSTANT_INT);
      }
      if (!list.CONSTANT_BITSET.equals(list2.CONSTANT_BITSET)) {
        ret = 1;
        errors++;
        PrintUtil.displayError(
            methodName + ": p1.CONSTANT_BITSET != list2.CONSTANT_BITSET");
        PrintUtil.displayError(methodName + ": p1.CONSTANT_BITSET",
            "" + list2.CONSTANT_BITSET, "" + p1.CONSTANT_BITSET);
      }
    }
    result.append("}").toString();
    if (!result.toString().equals(Data.graph_expected_string)) {
      errors++;
      ret = 1;
      PrintUtil.displayError(
          methodName + ": p1 graph string != Data.graph_expected_string");
      PrintUtil.displayError(methodName + ": p1 graph string",
          "" + Data.graph_expected_string, "" + result);
    }
    return ret;
  }

  public Graph return_a_graph_of_objects() throws RemoteException {
    String methodName = "return_a_graph_of_objects";

    if (verbose)
      PrintUtil.logTrace(methodName);
    return Data.graph;
  }

  public int pass_array_of_graph_objects(Graph p1[]) throws RemoteException {
    String methodName = "pass_array_of_graph_objects";

    if (verbose)
      PrintUtil.logTrace(methodName);
    int ret = 0;
    if (p1 == null) {
      PrintUtil.displayError(methodName
          + ": Could not receive array of Graph Objects (p1 is null)");
      ret = 1;
      errors++;
    } else if (p1.length != Data.MAXOBJETS) {
      ret = 1;
      errors++;
      PrintUtil.displayError(methodName + ": p1.length != Data.MAXOBJETS",
          "" + Data.MAXOBJETS, "" + p1.length);
      for (int i = 0; i < p1.length; i++) {
        if (p1[i] == null) {
          ret = 1;
          errors++;
          PrintUtil.displayError(
              methodName + ": p1[" + i + "] is null (unexpected)");
          return ret;
        }

        StringBuffer result = new StringBuffer("{ ");
        for (Graph list = p1[i], list2 = Data.graph_array[i]; list != null
            && list2 != null; list = list.next(), list2 = list2.next()) {
          result.append(list.data()).append(" ");
          if (!list.CONSTANT_STRING.equals(list2.CONSTANT_STRING)) {
            errors++;
            ret = 1;
            PrintUtil.displayError(methodName + ": p1[" + i
                + "].CONSTANT_STRING != list2.CONSTANT_STRING");
            PrintUtil.displayError(
                methodName + ": p1[" + i + "].CONSTANT_STRING",
                "" + list2.CONSTANT_STRING, "" + p1[i].CONSTANT_STRING);
          }
          if (list.CONSTANT_INT != list2.CONSTANT_INT) {
            errors++;
            ret = 1;
            PrintUtil.displayError(methodName + ": p1[" + i
                + "].CONSTANT_INT != list2.CONSTANT_INT");
            PrintUtil.displayError(methodName + ": p1[" + i + "].CONSTANT_INT",
                "" + list2.CONSTANT_INT, "" + p1[i].CONSTANT_INT);
          }
          if (!list.CONSTANT_BITSET.equals(list2.CONSTANT_BITSET)) {
            errors++;
            ret = 1;
            PrintUtil.displayError(methodName + ": p1[" + i
                + "].CONSTANT_BITSET != list2.CONSTANT_BITSET");
            PrintUtil.displayError(
                methodName + ": p1[" + i + "].CONSTANT_BITSET",
                "" + list2.CONSTANT_BITSET, "" + p1[i].CONSTANT_BITSET);
          }
        }
        result.append("}").toString();
        if (!result.toString().equals(Data.graph_expected_string)) {
          errors++;
          ret = 1;
          PrintUtil.displayError(methodName + ": p1[" + i
              + "] graph string != Data.graph_expected_string");
          PrintUtil.displayError(methodName + ": p1[" + i + "] graph string",
              "" + Data.graph_expected_string, "" + result);
        }
      }
    }
    return ret;
  }

  public Graph[] return_array_of_graph_objects() throws RemoteException {
    String methodName = "return_array_of_graph_objects";

    if (verbose)
      PrintUtil.logTrace(methodName);
    return Data.graph_array;
  }

  public Multi pass_return_multiclass_types(Multi p1) throws RemoteException {
    String methodName = "pass_return_multiclass_types";

    if (verbose)
      PrintUtil.logTrace(methodName);
    return p1;
  }

  public IDLStruct pass_return_idl_entity_types(IDLStruct p1)
      throws RemoteException {
    String methodName = "pass_return_idl_entity_types:struct";
    if (verbose)
      PrintUtil.logTrace(methodName);
    return p1;
  }

  public IDLStruct[] pass_return_idl_entity_types(IDLStruct[] p1)
      throws RemoteException {
    String methodName = "pass_return_idl_entity_types:sequence";
    if (verbose)
      PrintUtil.logTrace(methodName);
    return p1;
  }

  public void shutdown_rmiiiop_server() throws RemoteException {
    String methodName = "shutdown_rmiiiop_server";

    if (verbose)
      PrintUtil.logTrace(methodName);
    if (verbose)
      PrintUtil.logMsg("RMIIIOPServer is shutting down.....");
    if (verbose)
      PrintUtil.logMsg("RMIIIOPServer recorded " + errors + " errors.....");
    if (errors > 0) {
      if (verbose)
        PrintUtil.logMsg(
            "FINALSTATUS:RMIIIOPServer: .................... TEST_FAILED");
    } else {
      if (verbose)
        PrintUtil.logMsg(
            "FINALSTATUS:RMIIIOPServer: .................... TEST_PASSED");
    }
    System.exit(errors);
  }

  public static void main(String[] args) {
    int port = DEFAULT_RMIIIOP_HTTP_SERVER_PORT;
    String iorString = null;

    try {
      for (int i = 0; i < args.length; i++) {
        if (args[i].equals("-v"))
          verbose = true;
        else if (args[i].equals("-p")) {
          ++i;
          port = Integer.parseInt(args[i]);
        } else if (args[i].equals("-h")) {
          Usage(0);
        } else {
          PrintUtil.logMsg("Invalid option [" + args[i] + "] specified.");
          Usage(1);
        }
      }
      String version = System.getProperty("java.version");
      if (verbose)
        PrintUtil.logMsg("J2SE Version = " + version + "....");
      if (verbose)
        PrintUtil.logMsg("Create default J2SE ORB using ORB.init()....");
      ORB orb = ORB.init(new String[0], null);
      if (verbose)
        PrintUtil.logMsg("ORB=" + orb);
      if (verbose)
        PrintUtil.logMsg("Lookup the RootPOA....");
      POA rootPOA = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
      rootPOA.the_POAManager().activate();
      if (verbose)
        PrintUtil.logMsg("Creating RMIIIOPServer object....");
      RMIIIOPServer rmiiiopServer = new RMIIIOPServer();
      if (verbose)
        PrintUtil.logMsg("Get Stub for RMIIIOPServer object....");
      javax.rmi.CORBA.Stub rmiiiopStub = (javax.rmi.CORBA.Stub) PortableRemoteObject
          .toStub(rmiiiopServer);
      if (verbose)
        PrintUtil.logMsg("Connect/Attach RMIIIOPServer object to ORB....");
      rmiiiopStub.connect(orb);
      if (verbose)
        PrintUtil.logMsg("Creating CallBackImpl object....");
      CallBackImpl callbackImpl = new CallBackImpl("mycallback");
      if (verbose)
        PrintUtil.logMsg("Get Stub for CallBackImpl object....");
      javax.rmi.CORBA.Stub callbackStub = (javax.rmi.CORBA.Stub) PortableRemoteObject
          .toStub(callbackImpl);
      if (verbose)
        PrintUtil.logMsg("Connect/Attach CallBackImpl object to ORB....");
      callbackStub.connect(orb);
      for (int i = 0; i < Data.MAXOBJETS; i++)
        callbackRefs[i] = (CallBackInterface) callbackImpl;
      if (verbose)
        PrintUtil.logMsg("Convert RMIIIOPServer object to IOR string....");
      iorString = orb.object_to_string((org.omg.CORBA.Object) rmiiiopStub);
      if (verbose) {
        PrintUtil.logMsg("Using stringified IOR to store "
            + "RMIIIOPServer object reference....");
        PrintUtil.logMsg("IOR = " + iorString);
        PrintUtil.logMsg("Creating RMIIIOPHttpServer Thread....");
        PrintUtil.logMsg("Pass IOR to RMIIIOPHttpServer Thread....");
      }
      RMIIIOPHttpServer r = new RMIIIOPHttpServer(iorString, port, verbose);
      r.start();
      if (verbose)
        PrintUtil.logMsg("RMIIIOPServer ready and waiting....");
      orb.run();
    } catch (Exception e) {
      PrintUtil.logErr("RMIIIOPServer exception: " + e.getMessage());
      PrintUtil.printStackTrace(e);
    }
  }

  private static void Usage(int status) {
    PrintUtil.logMsg("Usage: java RMIIIOPServer [-h] [-v] [-p httpport].");
    PrintUtil.logMsg("    h\t\t\tPrint help message.");
    PrintUtil.logMsg("    v\t\t\tSpecify verbose output.");
    PrintUtil.logMsg("    p httpport\t\tSpecify http port.");
    System.exit(status);
  }
}
