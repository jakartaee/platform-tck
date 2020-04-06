/*
 * Copyright (c) 2007, 2020 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.rmiiiop.ee.marshaltests;

import java.util.*;
import java.rmi.*;
import jakarta.ejb.*;
import jakarta.transaction.*;
import javax.rmi.*;

public interface TestBean extends EJBObject {
  // Test #1: test passing and returning Java primitive types.
  public boolean pass_a_boolean(int idx, boolean p1) throws RemoteException;

  public byte pass_a_byte(int idx, byte p1) throws RemoteException;

  public char pass_a_char(int idx, char p1) throws RemoteException;

  public short pass_a_short(int idx, short p1) throws RemoteException;

  public int pass_a_int(int idx, int p1) throws RemoteException;

  public long pass_a_long(int idx, long p1) throws RemoteException;

  public float pass_a_float(int idx, float p1) throws RemoteException;

  public double pass_a_double(int idx, double p1) throws RemoteException;

  // Test #2: test passing and returning arrays of Java primitive types.
  public boolean[] pass_a_boolean_array(boolean p1[]) throws RemoteException;

  public byte[] pass_a_byte_array(byte p1[]) throws RemoteException;

  public char[] pass_a_char_array(char p1[]) throws RemoteException;

  public short[] pass_a_short_array(short p1[]) throws RemoteException;

  public int[] pass_a_int_array(int p1[]) throws RemoteException;

  public long[] pass_a_long_array(long p1[]) throws RemoteException;

  public float[] pass_a_float_array(float p1[]) throws RemoteException;

  public double[] pass_a_double_array(double p1[]) throws RemoteException;

  // Test #3: test passing and returning objects by value (OBV).
  public int pass_a_date_object(Date p1) throws RemoteException;

  public int pass_any_object(java.lang.Object p1) throws RemoteException;

  public Date return_a_date_object() throws RemoteException;

  public java.lang.Object return_any_object() throws RemoteException;

  public String pass_return_a_string_object(String p1) throws RemoteException;

  public Class pass_return_a_class_object(Class p1) throws RemoteException;

  public Vector pass_return_a_vector_object(Vector p1) throws RemoteException;

  public Hashtable pass_return_a_hashtable_object(Hashtable p1)
      throws RemoteException;

  // Test #4: test passing and returning arrays of objects by value (OBV).
  public int pass_a_date_object_array(Date p1[]) throws RemoteException;

  public int pass_any_object_array(java.lang.Object p1[])
      throws RemoteException;

  public Date[] return_a_date_object_array() throws RemoteException;

  public java.lang.Object[] return_any_object_array() throws RemoteException;

  public String[] pass_return_arrays_of_string_objects(String p1[])
      throws RemoteException;

  public Class[] pass_return_arrays_of_class_objects(Class p1[])
      throws RemoteException;

  // Test #5: test passing and returning a remote interface and verify
  // that you can call a method on this remote interface.
  public int pass_a_remote_interface(CallBackBean p1) throws RemoteException;

  public CallBackBean return_a_remote_interface() throws RemoteException;

  // Test #6: test passing and returning arrays of remote interfaces and
  // verify that you can call a method on each remote interface.
  public int pass_array_of_remote_interfaces(CallBackBean p1[])
      throws RemoteException;

  public CallBackBean[] return_array_of_remote_interfaces()
      throws RemoteException;

  // Test #7: test throwing a user defined exception, system exceptions,
  // and runtime exceptions.
  public void throw_a_user_exception() throws RemoteException, UserException;

  public void throw_exception(String s) throws RemoteException;

  // Test #8: test passing and returning a graph of objects by value
  // and verify that the entire graph is passed/returned
  public int pass_a_graph_of_objects(Graph p1) throws RemoteException;

  public Graph return_a_graph_of_objects() throws RemoteException;

  public java.io.Serializable pass_return_serializable_object(
      java.io.Serializable p1) throws RemoteException;

  // Test #9: test passing and returning an array of graph of objects
  // by value and verify that the each graph is passed/returned
  public int pass_array_of_graph_objects(Graph p1[]) throws RemoteException;

  public Graph[] return_array_of_graph_objects() throws RemoteException;

  // Test #10: test passing and returning an array of graph of objects
  // by value and verify that the each graph is passed/returned
  public java.lang.Object[][][] pass_return_object3_array(
      java.lang.Object[][][] o) throws RemoteException;

  // Test #11: test passing and returning multi class types by
  // value and verify that the multi class types are passed/returned
  public Multi pass_return_multiclass_types(Multi p1) throws RemoteException;

  // Test #12: test passing and returning a IDLEntity types
  // by value and verify that the IDLEntity types is passed/returned
  public IDLStruct pass_return_idl_entity_types(IDLStruct p1)
      throws RemoteException;

  public IDLStruct[] pass_return_idl_entity_types(IDLStruct[] p1)
      throws RemoteException;

  // Test #13: test passing and returning recursive vector and hashtable
  // by value and verify that the vector and hashtable passed/returned
  public Vector pass_return_recursive_vector_object(Vector p1)
      throws RemoteException;

  public Hashtable pass_return_recursive_hashtable_object(Hashtable p1)
      throws RemoteException;

}
