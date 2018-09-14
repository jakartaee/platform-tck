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
 * @(#)TestBeanEJB.java	1.20 03/05/28
 */

package com.sun.ts.tests.rmiiiop.ee.marshaltests;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;

import java.util.*;
import java.io.*;
import java.rmi.*;
import javax.rmi.*;
import javax.ejb.*;
import javax.transaction.*;
import javax.naming.*;

public class TestBeanEJB implements SessionBean {
  private SessionContext sctx = null;

  private Properties harnessProps = null;

  private TSNamingContext nctx = null;

  private boolean verbose = false;

  private int errors = 0;

  private static final String cBeanLookup = "java:comp/env/ejb/CallBackBeanEJB";

  private CallBackBean cBeanRefs[] = new CallBackBean[Data.MAXOBJETS];

  private CallBackBeanHome cBeanHome = null;

  public void ejbCreate(Properties p) throws CreateException {
    TestUtil.logTrace("ejbCreate");
    harnessProps = p;
    try {
      TestUtil.logMsg("Initialize remote logging");
      TestUtil.init(p);
      TestUtil.logMsg("Obtain naming context");
      nctx = new TSNamingContext();
      MyUtil.logMsg("Lookup the EJBHome of CallBackBean: " + cBeanLookup);
      cBeanHome = (CallBackBeanHome) nctx.lookup(cBeanLookup,
          CallBackBeanHome.class);
      TestUtil.logMsg("Create CallBackBean EJBObjects");
      createCallBackBeans(p);
    } catch (RemoteLoggingInitException e) {
      TestUtil.printStackTrace(e);
      throw new CreateException(e.getMessage());
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new CreateException("unable to obtain naming context");
    }
  }

  public void setSessionContext(SessionContext sc) {
    TestUtil.logTrace("setSessionContext");
    sctx = sc;
  }

  public void ejbRemove() {
    TestUtil.logTrace("ejbRemove");
  }

  public void ejbActivate() {
    TestUtil.logTrace("ejbActivate");
  }

  public void ejbPassivate() {
    TestUtil.logTrace("ejbPassivate");
  }

  // ===========================================================
  // TestBean interface (our business methods)

  private void createCallBackBeans(Properties p) {
    String methodName = "createCallBackBeans";

    try {
      if (verbose)
        MyUtil.logMsg(methodName + ": Create CallBackBean EJBObjects");
      for (int i = 0; i < Data.MAXOBJETS; i++) {
        cBeanRefs[i] = cBeanHome.create(p);
        if (verbose)
          MyUtil.logMsg(
              methodName + ": Created CallBackBean[" + i + "] EJBObject");
      }
    } catch (Exception e) {
      MyUtil.displayError(methodName + ": Caught exception: " + e.getMessage());
      MyUtil.printStackTrace(e);
      throw new EJBException("createCallBackBeanEJBs(): failed");
    }
  }

  public boolean pass_a_boolean(int idx, boolean p1) {
    String methodName = "pass_a_boolean";

    if (verbose)
      MyUtil.logTrace(methodName);
    boolean ret = false;
    if (p1 != Data.boolean_data[idx]) {
      errors++;
      ret = true;
      MyUtil.displayError(methodName + ": p1 failed",
          "" + Data.boolean_data[idx], "" + p1);
    }
    return (ret);
  }

  public byte pass_a_byte(int idx, byte p1) {
    String methodName = "pass_a_byte";

    if (verbose)
      MyUtil.logTrace(methodName);
    byte ret = 0;
    if (p1 != Data.byte_data[idx]) {
      errors++;
      ret = 1;
      MyUtil.displayError(methodName + ": p1 failed for idx=" + idx,
          "" + Data.byte_data[idx], "" + p1);
    }
    return (ret);
  }

  public char pass_a_char(int idx, char p1) {
    String methodName = "pass_a_char";

    if (verbose)
      MyUtil.logTrace(methodName);
    char ret = '0';
    if (p1 != Data.char_data[idx]) {
      errors++;
      ret = '1';
      MyUtil.displayError(methodName + ": p1 failed for idx=" + idx,
          "" + Data.char_data[idx], "" + p1);
    }
    return (ret);
  }

  public short pass_a_short(int idx, short p1) {
    String methodName = "pass_a_short";

    if (verbose)
      MyUtil.logTrace(methodName);
    short ret = 0;
    if (p1 != Data.short_data[idx]) {
      errors++;
      ret = 1;
      MyUtil.displayError(methodName + ": p1 failed for idx=" + idx,
          "" + Data.short_data[idx], "" + p1);
    }
    return (ret);
  }

  public int pass_a_int(int idx, int p1) {
    String methodName = "pass_a_int";

    if (verbose)
      MyUtil.logTrace(methodName);
    int ret = 0;
    if (p1 != Data.int_data[idx]) {
      errors++;
      ret = 1;
      MyUtil.displayError(methodName + ": p1 failed for idx=" + idx,
          "" + Data.int_data[idx], "" + p1);
    }
    return (ret);
  }

  public long pass_a_long(int idx, long p1) {
    String methodName = "pass_a_long";

    if (verbose)
      MyUtil.logTrace(methodName);
    long ret = 0;
    if (p1 != Data.long_data[idx]) {
      errors++;
      ret = 1;
      MyUtil.displayError(methodName + ": p1 failed for idx=" + idx,
          "" + Data.long_data[idx], "" + p1);
    }
    return (ret);
  }

  public float pass_a_float(int idx, float p1) {
    String methodName = "pass_a_float";

    if (verbose)
      MyUtil.logTrace(methodName);
    float ret = (float) 0.0;
    if (p1 != Data.float_data[idx]) {
      errors++;
      ret = (float) 1.0;
      MyUtil.displayError(methodName + ": p1 failed for idx=" + idx,
          "" + Data.float_data[idx], "" + p1);
    }
    return (ret);
  }

  public double pass_a_double(int idx, double p1) {
    String methodName = "pass_a_double";

    if (verbose)
      MyUtil.logTrace(methodName);
    double ret = (double) 0.0;
    if (p1 != Data.double_data[idx]) {
      errors++;
      ret = (double) 1.0;
      MyUtil.displayError(methodName + ": p1 failed for idx=" + idx,
          "" + Data.double_data[idx], "" + p1);
    }
    return (ret);
  }

  public boolean[] pass_a_boolean_array(boolean p1[]) {
    String methodName = "pass_a_boolean_array";

    if (verbose)
      MyUtil.logTrace(methodName);
    boolean ret[] = new boolean[Data.boolean_data.length];
    ret[0] = false;
    if (p1.length != Data.boolean_data.length) {
      errors++;
      ret[0] = true;
      MyUtil.displayError(
          methodName + ": p1.length != Data.boolean_data.length",
          "" + Data.boolean_data.length, "" + p1.length);
    }
    for (int i = 0; i < p1.length; i++) {
      if (p1[i] != Data.boolean_data[i]) {
        errors++;
        ret[0] = true;
        MyUtil.displayError(methodName + ": p1[" + i + "] failed",
            "" + Data.boolean_data[i], "" + p1[i]);
      }
    }
    return (ret);
  }

  public byte[] pass_a_byte_array(byte p1[]) {
    String methodName = "pass_a_byte_array";

    if (verbose)
      MyUtil.logTrace(methodName);
    byte ret[] = new byte[Data.byte_data.length];
    for (int i = 0; i < ret.length; i++)
      ret[i] = Data.byte_data[i];
    ret[0] = 0;
    if (p1.length != Data.byte_data.length) {
      errors++;
      ret[0] = 1;
      MyUtil.displayError(methodName + ": p1.length != Data.byte_data.length",
          "" + Data.byte_data.length, "" + p1.length);
    }
    for (int i = 0; i < p1.length; i++) {
      if (p1[i] != Data.byte_data[i]) {
        errors++;
        ret[0] = 1;
        MyUtil.displayError(methodName + ": p1[" + i + "] failed",
            "" + Data.byte_data[i], "" + p1[i]);
      }
    }
    return (ret);
  }

  public char[] pass_a_char_array(char p1[]) {
    String methodName = "pass_a_char_array";

    if (verbose)
      MyUtil.logTrace(methodName);
    char ret[] = new char[Data.char_data.length];
    for (int i = 0; i < ret.length; i++)
      ret[i] = Data.char_data[i];
    ret[0] = '0';
    if (p1.length != Data.char_data.length) {
      errors++;
      ret[0] = '1';
      MyUtil.displayError(methodName + ": p1.length != Data.char_data.length",
          "" + Data.char_data.length, "" + p1.length);
    }
    for (int i = 0; i < p1.length; i++) {
      if (p1[i] != Data.char_data[i]) {
        errors++;
        ret[0] = '1';
        MyUtil.displayError(methodName + ": p1[" + i + "] failed",
            "" + Data.char_data[i], "" + p1[i]);
      }
    }
    return (ret);
  }

  public short[] pass_a_short_array(short p1[]) {
    String methodName = "pass_a_short_array";

    if (verbose)
      MyUtil.logTrace(methodName);
    short ret[] = new short[Data.short_data.length];
    for (int i = 0; i < ret.length; i++)
      ret[i] = Data.short_data[i];
    ret[0] = 0;
    if (p1.length != Data.short_data.length) {
      errors++;
      ret[0] = 1;
      MyUtil.displayError(methodName + ": p1.length != Data.short_data.length",
          "" + Data.short_data.length, "" + p1.length);
    }
    for (int i = 0; i < p1.length; i++) {
      if (p1[i] != Data.short_data[i]) {
        errors++;
        ret[0] = 1;
        MyUtil.displayError(methodName + ": p1[" + i + "] failed",
            "" + Data.short_data[i], "" + p1[i]);
      }
    }
    return (ret);
  }

  public int[] pass_a_int_array(int p1[]) {
    String methodName = "pass_a_int_array";

    if (verbose)
      MyUtil.logTrace(methodName);
    int ret[] = new int[Data.int_data.length];
    for (int i = 0; i < ret.length; i++)
      ret[i] = Data.int_data[i];
    ret[0] = 0;
    if (p1.length != Data.int_data.length) {
      errors++;
      ret[0] = 1;
      MyUtil.displayError(methodName + ": p1.length != Data.int_data.length",
          "" + Data.int_data.length, "" + p1.length);
    }
    for (int i = 0; i < p1.length; i++) {
      if (p1[i] != Data.int_data[i]) {
        errors++;
        ret[0] = 1;
        MyUtil.displayError(methodName + ": p1[" + i + "] failed",
            "" + Data.int_data[i], "" + p1[i]);
      }
    }
    return (ret);
  }

  public long[] pass_a_long_array(long p1[]) {
    String methodName = "pass_a_long_array";

    if (verbose)
      MyUtil.logTrace(methodName);
    long ret[] = new long[Data.long_data.length];
    for (int i = 0; i < ret.length; i++)
      ret[i] = Data.long_data[i];
    ret[0] = 0;
    if (p1.length != Data.long_data.length) {
      errors++;
      ret[0] = 1;
      MyUtil.displayError(methodName + ": p1.length != Data.long_data.length",
          "" + Data.long_data.length, "" + p1.length);
    }
    for (int i = 0; i < p1.length; i++) {
      if (p1[i] != Data.long_data[i]) {
        errors++;
        ret[0] = 1;
        MyUtil.displayError(methodName + ": p1[" + i + "] failed",
            "" + Data.long_data[i], "" + p1[i]);
      }
    }
    return (ret);
  }

  public float[] pass_a_float_array(float p1[]) {
    String methodName = "pass_a_float_array";

    if (verbose)
      MyUtil.logTrace(methodName);
    float ret[] = new float[Data.float_data.length];
    for (int i = 0; i < ret.length; i++)
      ret[i] = Data.float_data[i];
    ret[0] = (float) 0.0;
    if (p1.length != Data.float_data.length) {
      errors++;
      ret[0] = (float) 1.0;
      MyUtil.displayError(methodName + ": p1.length != Data.float_data.length",
          "" + Data.float_data.length, "" + p1.length);
    }
    for (int i = 0; i < p1.length; i++) {
      if (p1[i] != Data.float_data[i]) {
        errors++;
        ret[0] = (float) 1.0;
        MyUtil.displayError(methodName + ": p1[" + i + "] failed",
            "" + Data.float_data[i], "" + p1[i]);
      }
    }
    return (ret);
  }

  public double[] pass_a_double_array(double p1[]) {
    String methodName = "pass_a_double_array";

    if (verbose)
      MyUtil.logTrace(methodName);
    double ret[] = new double[Data.double_data.length];
    for (int i = 0; i < ret.length; i++)
      ret[i] = Data.double_data[i];
    ret[0] = (double) 0.0;
    if (p1.length != Data.double_data.length) {
      errors++;
      ret[0] = (double) 1.0;
      MyUtil.displayError(methodName + ": p1.length != Data.double_data.length",
          "" + Data.double_data.length, "" + p1.length);
    }
    for (int i = 0; i < p1.length; i++) {
      if (p1[i] != Data.double_data[i]) {
        errors++;
        ret[0] = (double) 1.0;
        MyUtil.displayError(methodName + ": p1[" + i + "] failed",
            "" + Data.double_data[i], "" + p1[i]);
      }
    }
    return (ret);
  }

  public int pass_a_date_object(Date p1) {
    String methodName = "pass_a_date_object";

    if (verbose)
      MyUtil.logTrace(methodName);
    int ret = 0;
    if (!p1.equals(Data.date)) {
      errors++;
      ret = 1;
      MyUtil.displayError(methodName + ": p1 is not expected Date object");
    }
    return 0;
  }

  public int pass_any_object(Object p1) {
    String methodName = "pass_any_object";

    if (verbose)
      MyUtil.logTrace(methodName);
    int ret = 0;
    if (!(p1 instanceof BitSet)) {
      errors++;
      ret = 1;
      MyUtil.displayError(methodName + ": p1 is not a BitSet object");
    }
    if (!p1.equals(Data.bitset)) {
      errors++;
      ret = 1;
      MyUtil.displayError(methodName + ": p1 is not expected BitSet object");
    }
    return 0;
  }

  public Date return_a_date_object() {
    String methodName = "return_a_date_object";

    if (verbose)
      MyUtil.logTrace(methodName);
    return Data.date;
  }

  public Object return_any_object() {
    String methodName = "return_any_object";

    if (verbose)
      MyUtil.logTrace(methodName);
    return Data.bitset;
  }

  public int pass_a_date_object_array(Date p1[]) {
    String methodName = "pass_a_date_object_array";

    if (verbose)
      MyUtil.logTrace(methodName);
    int ret = 0;
    if (p1.length != Data.date_array.length) {
      errors++;
      ret = 1;
      MyUtil.displayError(methodName + ": p1.length != Data.date_array.length",
          "" + Data.date_array.length, "" + p1.length);
    }
    for (int i = 0; i < p1.length; i++) {
      if (!p1[i].equals(Data.date_array[i])) {
        errors++;
        ret = 1;
        MyUtil.displayError(
            methodName + ": p1[" + i + "] is not expected Date object");
      }
    }
    return ret;
  }

  public int pass_any_object_array(Object p1[]) {
    String methodName = "pass_any_object_array";

    if (verbose)
      MyUtil.logTrace(methodName);
    int ret = 0;
    if (p1.length != Data.bitset_array.length) {
      errors++;
      ret = 1;
      MyUtil.displayError(
          methodName + ": p1.length != Data.bitset_array.length",
          "" + Data.bitset_array.length, "" + p1.length);
    }
    for (int i = 0; i < p1.length; i++) {
      if (!(p1[i] instanceof BitSet)) {
        errors++;
        ret = 1;
        MyUtil.displayError(
            methodName + ": p1[" + i + "] is not a BitSet object");
      }
      if (!p1[i].equals(Data.bitset_array[i])) {
        errors++;
        ret = 1;
        MyUtil.displayError(
            methodName + ": p1[" + i + "] is not expected BitSet object");
      }
    }
    return ret;
  }

  public Date[] return_a_date_object_array() {
    String methodName = "return_a_date_object_array";

    if (verbose)
      MyUtil.logTrace(methodName);
    return Data.date_array;
  }

  public Object[] return_any_object_array() {
    String methodName = "return_any_object_array";

    if (verbose)
      MyUtil.logTrace(methodName);
    return Data.bitset_array;
  }

  public Object[][][] pass_return_object3_array(Object[][][] obj3Array) {
    String methodName = "pass_return_object3_array";
    if (verbose)
      MyUtil.logTrace(methodName);
    return obj3Array;
  }

  public int pass_a_remote_interface(CallBackBean p1) {
    String methodName = "pass_a_remote_interface";

    if (verbose)
      MyUtil.logTrace(methodName);
    int ret = 0;
    if (p1 == null) {
      MyUtil.displayError(
          methodName + ": Could not receive a Remote Interface (p1 is null)");
      ret = 1;
      errors++;
    } else {
      try {
        p1.method1();
        p1.method2();
      } catch (Exception e) {
        MyUtil.displayError(methodName
            + ": Unable to invoke the methods of the Remote Interface");
        MyUtil
            .displayError(methodName + ": Caught exception: " + e.getMessage());
        MyUtil.printStackTrace(e);
        ret = 1;
        errors++;
      }
    }
    return ret;
  }

  public CallBackBean return_a_remote_interface() {
    String methodName = "return_a_remote_interface";

    if (verbose)
      MyUtil.logTrace(methodName);
    return cBeanRefs[0];
  }

  public int pass_array_of_remote_interfaces(CallBackBean p1[]) {
    String methodName = "pass_array_of_remote_interfaces";

    if (verbose)
      MyUtil.logTrace(methodName);
    int ret = 0;
    if (p1 == null) {
      MyUtil.displayError(methodName
          + ": Could not receive array of Remote Interfaces (p1 is null)");
      ret = 1;
      errors++;
    } else if (p1.length != Data.MAXOBJETS) {
      MyUtil.displayError(methodName + ": p1.length != Data.MAXOBJETS",
          "" + Data.MAXOBJETS, "" + p1.length);
      ret = 1;
      errors++;
    }
    for (int i = 0; i < p1.length; i++) {
      try {
        p1[i].method1();
        p1[i].method2();
      } catch (Exception e) {
        MyUtil.displayError(methodName
            + ": Unable to invoke the methods of the Remote Interface p1[" + i
            + "]");
        MyUtil
            .displayError(methodName + ": Caught exception: " + e.getMessage());
        MyUtil.printStackTrace(e);
        ret = 1;
        errors++;
      }
    }
    return ret;
  }

  public CallBackBean[] return_array_of_remote_interfaces() {
    String methodName = "return_array_of_remote_interfaces";

    if (verbose)
      MyUtil.logTrace(methodName);
    return cBeanRefs;
  }

  public void throw_a_user_exception() throws UserException {
    String methodName = "throw_a_user_exception";

    if (verbose)
      MyUtil.logTrace(methodName);
    throw new UserException("This is a UserException test");
  }

  public void throw_exception(String s) {
    String methodName = "throw_exception";

    if (verbose)
      MyUtil.logTrace(methodName);

    if (s.equals("EJBException"))
      throw new EJBException("This is an EJBException test");
    else if (s.equals("Error"))
      throw new Error("This is an Error test");
    else if (s.equals("NullPointerException"))
      throw new NullPointerException("This is a NullPointerException test");
  }

  public int pass_a_graph_of_objects(Graph p1) {
    String methodName = "pass_a_graph_of_objects";

    int ret = 0;
    if (verbose)
      MyUtil.logTrace(methodName);
    StringBuffer result = new StringBuffer("{ ");
    for (Graph list = p1, list2 = Data.graph; list != null
        && list2 != null; list = list.next(), list2 = list2.next()) {
      result.append(list.data()).append(" ");
      if (!list.CONSTANT_STRING.equals(list2.CONSTANT_STRING)) {
        ret = 1;
        errors++;
        MyUtil.displayError(
            methodName + ": p1.CONSTANT_STRING != list2.CONSTANT_STRING");
        MyUtil.displayError(methodName + ": p1.CONSTANT_STRING",
            "" + list2.CONSTANT_STRING, "" + p1.CONSTANT_STRING);
      }
      if (list.CONSTANT_INT != list2.CONSTANT_INT) {
        ret = 1;
        errors++;
        MyUtil.displayError(
            methodName + ": p1.CONSTANT_INT != list2.CONSTANT_INT");
        MyUtil.displayError(methodName + ": p1.CONSTANT_INT",
            "" + list2.CONSTANT_INT, "" + p1.CONSTANT_INT);
      }
      if (!list.CONSTANT_BITSET.equals(list2.CONSTANT_BITSET)) {
        ret = 1;
        errors++;
        MyUtil.displayError(
            methodName + ": p1.CONSTANT_BITSET != list2.CONSTANT_BITSET");
        MyUtil.displayError(methodName + ": p1.CONSTANT_BITSET",
            "" + list2.CONSTANT_BITSET, "" + p1.CONSTANT_BITSET);
      }
    }
    result.append("}").toString();
    if (!result.toString().equals(Data.graph_expected_string)) {
      errors++;
      ret = 1;
      MyUtil.displayError(
          methodName + ": p1 graph string != Data.graph_expected_string");
      MyUtil.displayError(methodName + ": p1 graph string",
          "" + Data.graph_expected_string, "" + result);
    }
    return ret;
  }

  public Graph return_a_graph_of_objects() {
    String methodName = "return_a_graph_of_objects";

    if (verbose)
      MyUtil.logTrace(methodName);
    return Data.graph;
  }

  public int pass_array_of_graph_objects(Graph p1[]) {
    String methodName = "pass_array_of_graph_objects";

    if (verbose)
      MyUtil.logTrace(methodName);
    int ret = 0;
    if (p1 == null) {
      MyUtil.displayError(methodName
          + ": Could not receive array of Graph Objects (p1 is null)");
      ret = 1;
      errors++;
    } else if (p1.length != Data.MAXOBJETS) {
      ret = 1;
      errors++;
      MyUtil.displayError(methodName + ": p1.length != Data.MAXOBJETS",
          "" + Data.MAXOBJETS, "" + p1.length);
    }
    for (int i = 0; i < p1.length; i++) {

      StringBuffer result = new StringBuffer("{ ");
      for (Graph list = p1[i], list2 = Data.graph_array[i]; list != null
          && list2 != null; list = list.next(), list2 = list2.next()) {
        result.append(list.data()).append(" ");
        if (!list.CONSTANT_STRING.equals(list2.CONSTANT_STRING)) {
          errors++;
          ret = 1;
          MyUtil.displayError(methodName + ": p1[" + i
              + "].CONSTANT_STRING != list2.CONSTANT_STRING");
          MyUtil.displayError(methodName + ": p1[" + i + "].CONSTANT_STRING",
              "" + list2.CONSTANT_STRING, "" + p1[i].CONSTANT_STRING);
        }
        if (list.CONSTANT_INT != list2.CONSTANT_INT) {
          errors++;
          ret = 1;
          MyUtil.displayError(methodName + ": p1[" + i
              + "].CONSTANT_INT != list2.CONSTANT_INT");
          MyUtil.displayError(methodName + ": p1[" + i + "].CONSTANT_INT",
              "" + list2.CONSTANT_INT, "" + p1[i].CONSTANT_INT);
        }
        if (!list.CONSTANT_BITSET.equals(list2.CONSTANT_BITSET)) {
          errors++;
          ret = 1;
          MyUtil.displayError(methodName + ": p1[" + i
              + "].CONSTANT_BITSET != list2.CONSTANT_BITSET");
          MyUtil.displayError(methodName + ": p1[" + i + "].CONSTANT_BITSET",
              "" + list2.CONSTANT_BITSET, "" + p1[i].CONSTANT_BITSET);
        }
      }
      result.append("}").toString();
      if (!result.toString().equals(Data.graph_expected_string)) {
        errors++;
        ret = 1;
        MyUtil.displayError(methodName + ": p1[" + i
            + "] graph string != Data.graph_expected_string");
        MyUtil.displayError(methodName + ": p1[" + i + "] graph string",
            "" + Data.graph_expected_string, "" + result);
      }
    }
    return ret;
  }

  public Graph[] return_array_of_graph_objects() {
    String methodName = "return_array_of_graph_objects";

    if (verbose)
      MyUtil.logTrace(methodName);
    return Data.graph_array;
  }

  public Multi pass_return_multiclass_types(Multi p1) {
    String methodName = "pass_return_multiclass_types";

    if (verbose)
      MyUtil.logTrace(methodName);
    return p1;
  }

  public IDLStruct pass_return_idl_entity_types(IDLStruct p1) {
    String methodName = "pass_return_idl_entity_types:struct";
    if (verbose)
      MyUtil.logTrace(methodName);
    return p1;
  }

  public IDLStruct[] pass_return_idl_entity_types(IDLStruct[] p1) {
    String methodName = "pass_return_idl_entity_types:sequence";
    if (verbose)
      MyUtil.logTrace(methodName);
    return p1;
  }

  public String pass_return_a_string_object(String p1) {
    String methodName = "pass_return_a_string_object";

    if (verbose)
      MyUtil.logTrace(methodName);

    return p1;
  }

  public String[] pass_return_arrays_of_string_objects(String p1[]) {
    String methodName = "pass_return_arrays_of_string_objects";

    if (verbose)
      MyUtil.logTrace(methodName);

    return p1;
  }

  public Class pass_return_a_class_object(Class p1) {
    String methodName = "pass_return_a_class_object";

    if (verbose)
      MyUtil.logTrace(methodName);

    return p1;
  }

  public Class[] pass_return_arrays_of_class_objects(Class p1[]) {
    String methodName = "pass_return_arrays_of_class_objects";

    if (verbose)
      MyUtil.logTrace(methodName);

    return p1;
  }

  public Vector pass_return_a_vector_object(Vector p1) {
    String methodName = "pass_return_a_vector_object";

    if (verbose)
      MyUtil.logTrace(methodName);

    return p1;
  }

  public Hashtable pass_return_a_hashtable_object(Hashtable p1) {
    String methodName = "pass_return_a_hashtable_object";

    if (verbose)
      MyUtil.logTrace(methodName);

    return p1;
  }

  public Vector pass_return_recursive_vector_object(Vector p1) {
    String methodName = "pass_return_recursive_vector_object";

    if (verbose)
      MyUtil.logTrace(methodName);

    return p1;
  }

  public Hashtable pass_return_recursive_hashtable_object(Hashtable p1) {
    String methodName = "pass_return_recursive_hashtable_object";

    if (verbose)
      MyUtil.logTrace(methodName);

    return p1;
  }

  public Serializable pass_return_serializable_object(Serializable p1) {
    String methodName = "pass_return_serializable_object";

    if (verbose)
      MyUtil.logTrace(methodName);

    return p1;
  }

}
