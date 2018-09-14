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

package com.sun.ts.tests.rmiiiop.ee.marshaltests;

import java.util.*;
import java.math.*;

import java.io.Serializable;

public class TypeCodeTester implements Serializable {
  public byte byteField;

  public boolean booleanField;

  public short shortField;

  public int intField;

  public long longField;

  public float floatField;

  public double doubleField;

  public char charField;

  public String stringField;

  public byte[] byteArrayField;

  public boolean[] booleanArrayField;

  public short[] shortArrayField;

  public int[] intArrayField;

  public long[] longArrayField;

  public float[] floatArrayField;

  public double[] doubleArrayField;

  public char[] charArrayField;

  public String[] stringArrayField;

  public TypeCodeTester recursiveField;

  public TypeCodeTester() {
    byteField = (byte) 83;
    booleanField = true;
    shortField = (short) 5912;
    intField = 9035;
    longField = (long) 949241;
    floatField = (float) 35.2;
    doubleField = (double) 3590.421;

    charField = '\u6D77';
    stringField = "\u6D77\u6D77\u6D77";

    byteArrayField = new byte[] { (byte) 241, (byte) 59, (byte) 59, (byte) 0,
        (byte) 53 };

    booleanArrayField = new boolean[] { false, true };

    shortArrayField = new short[] { (short) 943, (short) 0, (short) 3512,
        (short) 35 };

    intArrayField = new int[] { 35123, 943, -203012, 0, 2312 };

    longArrayField = new long[] { (long) 2412, (long) -203, (long) 0,
        (long) 241 };

    floatArrayField = new float[] { (float) 32.3, (float) 912.231, (float) 0.0,
        (float) 234.11 };

    doubleArrayField = new double[] { (double) 3412.21, (double) 243.22,
        (double) 0.0, (double) 23.1 };

    charArrayField = new char[] { 'A', '\u6D77', 'x' };

    stringArrayField = new String[] { "This is a test", stringField,
        "This is another test" };

    recursiveField = this;
  }
}
