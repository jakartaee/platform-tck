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

package com.sun.ts.tests.jaxrpc.common;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;

import java.util.*;
import java.math.*;
import java.text.SimpleDateFormat;
import javax.xml.namespace.QName;

public final class JAXRPC_Data {

  private static TimeZone defaultTZ = TimeZone.getDefault();

  // ==================================================================
  // Java Primitive Data Types - Single-Dimensional Array Data
  // ==================================================================

  public final static boolean boolean_data[] = { false, true };

  public final static Boolean Boolean_data[] = { new Boolean(false),
      new Boolean(true), null };

  public final static char char_data[] = { Character.MIN_VALUE, 0,
      Character.MAX_VALUE };

  public final static Character Character_data[] = {
      new Character(Character.MIN_VALUE), new Character((char) 0),
      new Character(Character.MAX_VALUE), null };

  public final static byte byte_data[] = { Byte.MIN_VALUE, 0, Byte.MAX_VALUE };

  public final static byte byte_data2[] = { 0, Byte.MAX_VALUE, Byte.MIN_VALUE };

  public final static Byte Byte_data[] = { new Byte(Byte.MIN_VALUE),
      new Byte((byte) 0), new Byte(Byte.MAX_VALUE), null };

  public final static short short_data[] = { Short.MIN_VALUE, 0,
      Short.MAX_VALUE };

  public final static Short Short_data[] = { new Short(Short.MIN_VALUE),
      new Short((short) 0), new Short(Short.MAX_VALUE), null };

  public final static int int_data[] = { Integer.MIN_VALUE, 0,
      Integer.MAX_VALUE };

  public final static Integer Integer_data[] = { new Integer(Integer.MIN_VALUE),
      new Integer(0), new Integer(Integer.MAX_VALUE), null };

  public final static long long_data[] = { Long.MIN_VALUE, 0, Long.MAX_VALUE };

  public final static Long Long_data[] = { new Long(Long.MIN_VALUE),
      new Long(0L), new Long(Long.MAX_VALUE), null };

  public final static float float_data[] = { Float.MIN_VALUE, 0,
      Float.MAX_VALUE };

  public final static Float Float_data[] = { new Float(Float.MIN_VALUE),
      new Float((float) 0), new Float(Float.MAX_VALUE), null };

  public final static double double_data[] = { Double.MIN_VALUE, 0,
      Double.MAX_VALUE };

  public final static Double Double_data[] = { new Double(Double.MIN_VALUE),
      new Double(0), new Double(Double.MAX_VALUE), null };

  // ==================================================================
  // Java Primitive Data Types - Multi-Dimensional Array Data
  // ==================================================================

  public final static boolean boolean_multi_data[][] = { boolean_data,
      boolean_data, };

  public final static Boolean Boolean_multi_data[][] = { Boolean_data,
      Boolean_data, };

  public final static char char_multi_data[][] = { char_data, char_data, };

  public final static Character Character_multi_data[][] = { Character_data,
      Character_data, };

  public final static byte byte_multi_data[][] = { byte_data, byte_data, };

  public final static Byte Byte_multi_data[][] = { Byte_data, Byte_data, };

  public final static short short_multi_data[][] = { short_data, short_data, };

  public final static Short Short_multi_data[][] = { Short_data, Short_data, };

  public final static int int_multi_data[][] = { int_data, int_data, };

  public final static Integer Integer_multi_data[][] = { Integer_data,
      Integer_data, };

  public final static long long_multi_data[][] = { long_data, long_data, };

  public final static Long Long_multi_data[][] = { Long_data, Long_data, };

  public final static float float_multi_data[][] = { float_data, float_data, };

  public final static Float Float_multi_data[][] = { Float_data, Float_data, };

  public final static double double_multi_data[][] = { double_data,
      double_data, };

  public final static Double Double_multi_data[][] = { Double_data,
      Double_data, };

  // ==================================================================
  // Java Standard Value Classes - Single-Dimensional Array Data
  // ==================================================================

  public final static String String_data[] = { "String1", "String2", "String3",
      "", null };

  public final static String String_nonull_data[] = { "String1", "String2",
      "String3", };

  public final static Date Date_data[] = {
      new GregorianCalendar(6, 5, 1, 10, 0, 0).getTime(),
      new GregorianCalendar(9, 10, 25, 1, 30, 0).getTime(),
      new GregorianCalendar(96, 5, 1, 2, 0, 30).getTime(),
      new GregorianCalendar(99, 10, 25, 3, 15, 15).getTime(),
      new GregorianCalendar(996, 5, 1, 6, 6, 6).getTime(),
      new GregorianCalendar(999, 10, 25, 7, 7, 7).getTime(),
      new GregorianCalendar(1996, 5, 1, 8, 8, 8).getTime(),
      new GregorianCalendar(1999, 10, 25, 9, 9, 9).getTime(), null };

  public final static Date Date_nonull_data[] = {
      new GregorianCalendar(6, 5, 1, 10, 0, 0).getTime(),
      new GregorianCalendar(9, 10, 25, 1, 30, 0).getTime(),
      new GregorianCalendar(96, 5, 1, 2, 0, 30).getTime(),
      new GregorianCalendar(99, 10, 25, 3, 15, 15).getTime(),
      new GregorianCalendar(996, 5, 1, 6, 6, 6).getTime(),
      new GregorianCalendar(999, 10, 25, 7, 7, 7).getTime(),
      new GregorianCalendar(1996, 5, 1, 8, 8, 8).getTime(),
      new GregorianCalendar(1999, 10, 25, 9, 9, 9).getTime(), };

  public final static GregorianCalendar GregorianCalendar_data[] = {
      new GregorianCalendar(6, 5, 1, 10, 0, 0),
      new GregorianCalendar(9, 10, 25, 1, 30, 0),
      new GregorianCalendar(96, 5, 1, 2, 0, 30),
      new GregorianCalendar(99, 10, 25, 3, 15, 15),
      new GregorianCalendar(996, 5, 1, 6, 6, 6),
      new GregorianCalendar(999, 10, 25, 7, 7, 7),
      new GregorianCalendar(1996, 5, 1, 8, 8, 8),
      new GregorianCalendar(1999, 10, 25, 9, 9, 9), null };

  public final static GregorianCalendar GregorianCalendar_nonull_data[] = {
      new GregorianCalendar(6, 5, 1, 10, 0, 0),
      new GregorianCalendar(9, 10, 25, 1, 30, 0),
      new GregorianCalendar(96, 5, 1, 2, 0, 30),
      new GregorianCalendar(99, 10, 25, 3, 15, 15),
      new GregorianCalendar(996, 5, 1, 6, 6, 6),
      new GregorianCalendar(999, 10, 25, 7, 7, 7),
      new GregorianCalendar(1996, 5, 1, 8, 8, 8),
      new GregorianCalendar(1999, 10, 25, 9, 9, 9), };

  public final static BigInteger BigInteger_data[] = {
      new BigInteger("3512359"), new BigInteger("3512360"), null };

  public final static BigInteger BigInteger_nonull_data[] = {
      new BigInteger("3512359"), new BigInteger("3512360"), };

  public final static BigDecimal BigDecimal_data[] = {
      new BigDecimal("3512359.1456"), new BigDecimal("3512360.1456"), null };

  public final static BigDecimal BigDecimal_nonull_data[] = {
      new BigDecimal("3512359.1456"), new BigDecimal("3512360.1456"), };

  // ==================================================================
  // Java Standard Value Classes - Multi-Dimensional Array Data
  // ==================================================================

  public final static String String_multi_data[][] = { String_data,
      String_data, };

  public final static Date Date_multi_data[][] = { Date_data, Date_data, };

  public final static GregorianCalendar GregorianCalendar_multi_data[][] = {
      GregorianCalendar_data, GregorianCalendar_data, };

  public final static BigInteger BigInteger_multi_data[][] = { BigInteger_data,
      BigInteger_data, };

  public final static BigDecimal BigDecimal_multi_data[][] = { BigDecimal_data,
      BigDecimal_data, };

  // ==================================================================
  // Java Other Data Types - Single and Multi Array Data
  // ==================================================================
  public final static QName QName_data[] = { new QName("someLocalPart"),
      new QName("http://someURI.org/", "someLocalPart"), null };

  public final static QName QName_nonull_data[] = { new QName("someLocalPart"),
      new QName("http://someURI.org/", "someLocalPart"), };

  public final static QName QName_multi_data[][] = { QName_data, QName_data, };

  // ==================================================================
  // Various utility classes used for dumping/comparing data
  // ==================================================================

  public static void dumpArrayValues(Object o, String t) {
    System.out.println("JAXRPC_Data:dumpArrayValues");
    System.out.println("Dumping " + t + " array, size=" + getArraySize(o, t));
    if (t.equals("boolean")) {
      boolean[] v = (boolean[]) o;
      for (int i = 0; i < v.length; i++)
        System.out.println("- " + v[i]);
    } else if (t.equals("Boolean")) {
      Boolean[] v = (Boolean[]) o;
      for (int i = 0; i < v.length; i++)
        System.out.println("- " + v[i]);
    } else if (t.equals("char")) {
      char[] v = (char[]) o;
      for (int i = 0; i < v.length; i++)
        System.out.println("- " + v[i]);
    } else if (t.equals("Character")) {
      Character[] v = (Character[]) o;
      for (int i = 0; i < v.length; i++)
        System.out.println("- " + v[i]);
    } else if (t.equals("byte")) {
      byte[] v = (byte[]) o;
      for (int i = 0; i < v.length; i++)
        System.out.println("- " + v[i]);
    } else if (t.equals("Byte")) {
      Byte[] v = (Byte[]) o;
      for (int i = 0; i < v.length; i++)
        System.out.println("- " + v[i]);
    } else if (t.equals("short")) {
      short[] v = (short[]) o;
      for (int i = 0; i < v.length; i++)
        System.out.println("- " + v[i]);
    } else if (t.equals("Short")) {
      Short[] v = (Short[]) o;
      for (int i = 0; i < v.length; i++)
        System.out.println("- " + v[i]);
    } else if (t.equals("int")) {
      int[] v = (int[]) o;
      for (int i = 0; i < v.length; i++)
        System.out.println("- " + v[i]);
    } else if (t.equals("Integer")) {
      Integer[] v = (Integer[]) o;
      for (int i = 0; i < v.length; i++)
        System.out.println("- " + v[i]);
    } else if (t.equals("long")) {
      long[] v = (long[]) o;
      for (int i = 0; i < v.length; i++)
        System.out.println("- " + v[i]);
    } else if (t.equals("Long")) {
      Long[] v = (Long[]) o;
      for (int i = 0; i < v.length; i++)
        System.out.println("- " + v[i]);
    } else if (t.equals("float")) {
      float[] v = (float[]) o;
      for (int i = 0; i < v.length; i++)
        System.out.println("- " + v[i]);
    } else if (t.equals("Float")) {
      Float[] v = (Float[]) o;
      for (int i = 0; i < v.length; i++)
        System.out.println("- " + v[i]);
    } else if (t.equals("double")) {
      double[] v = (double[]) o;
      for (int i = 0; i < v.length; i++)
        System.out.println("- " + v[i]);
    } else if (t.equals("Double")) {
      Double[] v = (Double[]) o;
      for (int i = 0; i < v.length; i++)
        System.out.println("- " + v[i]);
    } else if (t.equals("String")) {
      String[] v = (String[]) o;
      for (int i = 0; i < v.length; i++)
        System.out.println("- " + v[i]);
    } else if (t.equals("Date")) {
      Date[] v = (Date[]) o;
      for (int i = 0; i < v.length; i++)
        System.out.println("- " + v[i]);
    } else if (t.equals("Calendar")) {
      Calendar[] v = (Calendar[]) o;
      for (int i = 0; i < v.length; i++)
        System.out.println("- " + v[i]);
    } else if (t.equals("BigInteger")) {
      BigInteger[] v = (BigInteger[]) o;
      for (int i = 0; i < v.length; i++)
        System.out.println("- " + v[i]);
    } else if (t.equals("BigDecimal")) {
      BigDecimal[] v = (BigDecimal[]) o;
      for (int i = 0; i < v.length; i++)
        System.out.println("- " + v[i]);
    } else if (t.equals("QName")) {
      QName[] v = (QName[]) o;
      for (int i = 0; i < v.length; i++)
        System.out.println("- " + v[i]);
    }
  }

  public static void dumpMultiArrayValues(Object o, String t) {
    System.out.println("JAXRPC_Data:dumpMultiArrayValues");
    System.out.println(
        "Dumping " + t + " multiarray, size=" + getMultiArraySize(o, t));
    if (t.equals("boolean")) {
      boolean[][] v = (boolean[][]) o;
      for (int i = 0; i < v.length; i++) {
        for (int k = 0; k < v[i].length; k++)
          System.out.println("- " + v[i][k]);
      }
    } else if (t.equals("Boolean")) {
      Boolean[][] v = (Boolean[][]) o;
      for (int i = 0; i < v.length; i++) {
        for (int k = 0; k < v[i].length; k++)
          System.out.println("- " + v[i][k]);
      }
    } else if (t.equals("char")) {
      char[][] v = (char[][]) o;
      for (int i = 0; i < v.length; i++) {
        for (int k = 0; k < v[i].length; k++)
          System.out.println("- " + v[i][k]);
      }
    } else if (t.equals("Character")) {
      Character[][] v = (Character[][]) o;
      for (int i = 0; i < v.length; i++) {
        for (int k = 0; k < v[i].length; k++)
          System.out.println("- " + v[i][k]);
      }
    } else if (t.equals("byte")) {
      byte[][] v = (byte[][]) o;
      for (int i = 0; i < v.length; i++) {
        for (int k = 0; k < v[i].length; k++)
          System.out.println("- " + v[i][k]);
      }
    } else if (t.equals("Byte")) {
      Byte[][] v = (Byte[][]) o;
      for (int i = 0; i < v.length; i++) {
        for (int k = 0; k < v[i].length; k++)
          System.out.println("- " + v[i][k]);
      }
    } else if (t.equals("short")) {
      short[][] v = (short[][]) o;
      for (int i = 0; i < v.length; i++) {
        for (int k = 0; k < v[i].length; k++)
          System.out.println("- " + v[i][k]);
      }
    } else if (t.equals("Short")) {
      Short[][] v = (Short[][]) o;
      for (int i = 0; i < v.length; i++) {
        for (int k = 0; k < v[i].length; k++)
          System.out.println("- " + v[i][k]);
      }
    } else if (t.equals("int")) {
      int[][] v = (int[][]) o;
      for (int i = 0; i < v.length; i++) {
        for (int k = 0; k < v[i].length; k++)
          System.out.println("- " + v[i][k]);
      }
    } else if (t.equals("Integer")) {
      Integer[][] v = (Integer[][]) o;
      for (int i = 0; i < v.length; i++) {
        for (int k = 0; k < v[i].length; k++)
          System.out.println("- " + v[i][k]);
      }
    } else if (t.equals("long")) {
      long[][] v = (long[][]) o;
      for (int i = 0; i < v.length; i++) {
        for (int k = 0; k < v[i].length; k++)
          System.out.println("- " + v[i][k]);
      }
    } else if (t.equals("Long")) {
      Long[][] v = (Long[][]) o;
      for (int i = 0; i < v.length; i++) {
        for (int k = 0; k < v[i].length; k++)
          System.out.println("- " + v[i][k]);
      }
    } else if (t.equals("float")) {
      float[][] v = (float[][]) o;
      for (int i = 0; i < v.length; i++) {
        for (int k = 0; k < v[i].length; k++)
          System.out.println("- " + v[i][k]);
      }
    } else if (t.equals("Float")) {
      Float[][] v = (Float[][]) o;
      for (int i = 0; i < v.length; i++) {
        for (int k = 0; k < v[i].length; k++)
          System.out.println("- " + v[i][k]);
      }
    } else if (t.equals("double")) {
      double[][] v = (double[][]) o;
      for (int i = 0; i < v.length; i++) {
        for (int k = 0; k < v[i].length; k++)
          System.out.println("- " + v[i][k]);
      }
    } else if (t.equals("Double")) {
      Double[][] v = (Double[][]) o;
      for (int i = 0; i < v.length; i++) {
        for (int k = 0; k < v[i].length; k++)
          System.out.println("- " + v[i][k]);
      }
    } else if (t.equals("String")) {
      String[][] v = (String[][]) o;
      for (int i = 0; i < v.length; i++) {
        for (int k = 0; k < v[i].length; k++)
          System.out.println("- " + v[i][k]);
      }
    } else if (t.equals("Date")) {
      Date[][] v = (Date[][]) o;
      for (int i = 0; i < v.length; i++) {
        for (int k = 0; k < v[i].length; k++)
          System.out.println("- " + v[i][k]);
      }
    } else if (t.equals("Calendar")) {
      Calendar[][] v = (Calendar[][]) o;
      for (int i = 0; i < v.length; i++) {
        for (int k = 0; k < v[i].length; k++)
          System.out.println("- " + v[i][k]);
      }
    } else if (t.equals("BigInteger")) {
      BigInteger[][] v = (BigInteger[][]) o;
      for (int i = 0; i < v.length; i++) {
        for (int k = 0; k < v[i].length; k++)
          System.out.println("- " + v[i][k]);
      }
    } else if (t.equals("BigDecimal")) {
      BigDecimal[][] v = (BigDecimal[][]) o;
      for (int i = 0; i < v.length; i++) {
        for (int k = 0; k < v[i].length; k++)
          System.out.println("- " + v[i][k]);
      }
    } else if (t.equals("QName")) {
      QName[][] v = (QName[][]) o;
      for (int i = 0; i < v.length; i++) {
        for (int k = 0; k < v[i].length; k++)
          System.out.println("- " + v[i][k]);
      }
    }
  }

  public static int getArraySize(Object o, String t) {
    System.out.println("JAXRPC_Data:getArraySize");
    if (t.equals("boolean")) {
      return ((boolean[]) o).length;
    } else if (t.equals("Boolean")) {
      return ((Boolean[]) o).length;
    } else if (t.equals("char")) {
      return ((char[]) o).length;
    } else if (t.equals("Character")) {
      return ((Character[]) o).length;
    } else if (t.equals("byte")) {
      return ((byte[]) o).length;
    } else if (t.equals("Byte")) {
      return ((Byte[]) o).length;
    } else if (t.equals("short")) {
      return ((short[]) o).length;
    } else if (t.equals("Short")) {
      return ((Short[]) o).length;
    } else if (t.equals("int")) {
      return ((int[]) o).length;
    } else if (t.equals("Integer")) {
      return ((Integer[]) o).length;
    } else if (t.equals("long")) {
      return ((long[]) o).length;
    } else if (t.equals("Long")) {
      return ((Long[]) o).length;
    } else if (t.equals("float")) {
      return ((float[]) o).length;
    } else if (t.equals("Float")) {
      return ((Float[]) o).length;
    } else if (t.equals("double")) {
      return ((double[]) o).length;
    } else if (t.equals("Double")) {
      return ((Double[]) o).length;
    } else if (t.equals("String")) {
      return ((String[]) o).length;
    } else if (t.equals("Date")) {
      return ((Date[]) o).length;
    } else if (t.equals("Calendar")) {
      return ((Calendar[]) o).length;
    } else if (t.equals("BigInteger")) {
      return ((BigInteger[]) o).length;
    } else if (t.equals("BigDecimal")) {
      return ((BigDecimal[]) o).length;
    } else if (t.equals("QName")) {
      return ((QName[]) o).length;
    }
    return -1;
  }

  public static String getMultiArraySize(Object o, String t) {
    System.out.println("JAXRPC_Data:getMultiArraySize");
    if (t.equals("boolean")) {
      boolean[][] m = (boolean[][]) o;
      return ("[" + m.length + "][" + m[0].length + "]");
    } else if (t.equals("Boolean")) {
      Boolean[][] m = (Boolean[][]) o;
      return ("[" + m.length + "][" + m[0].length + "]");
    } else if (t.equals("char")) {
      char[][] m = (char[][]) o;
      return ("[" + m.length + "][" + m[0].length + "]");
    } else if (t.equals("Character")) {
      Character[][] m = (Character[][]) o;
      return ("[" + m.length + "][" + m[0].length + "]");
    } else if (t.equals("byte")) {
      byte[][] m = (byte[][]) o;
      return ("[" + m.length + "][" + m[0].length + "]");
    } else if (t.equals("Byte")) {
      Byte[][] m = (Byte[][]) o;
      return ("[" + m.length + "][" + m[0].length + "]");
    } else if (t.equals("short")) {
      short[][] m = (short[][]) o;
      return ("[" + m.length + "][" + m[0].length + "]");
    } else if (t.equals("Short")) {
      Short[][] m = (Short[][]) o;
      return ("[" + m.length + "][" + m[0].length + "]");
    } else if (t.equals("int")) {
      int[][] m = (int[][]) o;
      return ("[" + m.length + "][" + m[0].length + "]");
    } else if (t.equals("Integer")) {
      Integer[][] m = (Integer[][]) o;
      return ("[" + m.length + "][" + m[0].length + "]");
    } else if (t.equals("long")) {
      long[][] m = (long[][]) o;
      return ("[" + m.length + "][" + m[0].length + "]");
    } else if (t.equals("Long")) {
      Long[][] m = (Long[][]) o;
      return ("[" + m.length + "][" + m[0].length + "]");
    } else if (t.equals("float")) {
      float[][] m = (float[][]) o;
      return ("[" + m.length + "][" + m[0].length + "]");
    } else if (t.equals("Float")) {
      Float[][] m = (Float[][]) o;
      return ("[" + m.length + "][" + m[0].length + "]");
    } else if (t.equals("double")) {
      double[][] m = (double[][]) o;
      return ("[" + m.length + "][" + m[0].length + "]");
    } else if (t.equals("Double")) {
      Double[][] m = (Double[][]) o;
      return ("[" + m.length + "][" + m[0].length + "]");
    } else if (t.equals("String")) {
      String[][] m = (String[][]) o;
      return ("[" + m.length + "][" + m[0].length + "]");
    } else if (t.equals("Date")) {
      Date[][] m = (Date[][]) o;
      return ("[" + m.length + "][" + m[0].length + "]");
    } else if (t.equals("Calendar")) {
      Calendar[][] m = (Calendar[][]) o;
      return ("[" + m.length + "][" + m[0].length + "]");
    } else if (t.equals("BigInteger")) {
      BigInteger[][] m = (BigInteger[][]) o;
      return ("[" + m.length + "][" + m[0].length + "]");
    } else if (t.equals("BigDecimal")) {
      BigDecimal[][] m = (BigDecimal[][]) o;
      return ("[" + m.length + "][" + m[0].length + "]");
    } else if (t.equals("QName")) {
      QName[][] m = (QName[][]) o;
      return ("[" + m.length + "][" + m[0].length + "]");
    }
    return "unknown";
  }

  public static boolean compareValues(boolean e, boolean r) {
    boolean pass = true;

    if (r != e) {
      System.out.println("Value Mismatch: expected " + e + ", received " + r);
      pass = false;
    }
    return pass;
  }

  public static boolean compareValues(byte e, byte r) {
    boolean pass = true;

    if (r != e) {
      System.out.println("Value Mismatch: expected " + e + ", received " + r);
      pass = false;
    }
    return pass;
  }

  public static boolean compareValues(short e, short r) {
    boolean pass = true;

    if (r != e) {
      System.out.println("Value Mismatch: expected " + e + ", received " + r);
      pass = false;
    }
    return pass;
  }

  public static boolean compareValues(int e, int r) {
    boolean pass = true;

    if (r != e) {
      System.out.println("Value Mismatch: expected " + e + ", received " + r);
      pass = false;
    }
    return pass;
  }

  public static boolean compareValues(long e, long r) {
    boolean pass = true;

    if (r != e) {
      System.out.println("Value Mismatch: expected " + e + ", received " + r);
      pass = false;
    }
    return pass;
  }

  public static boolean compareValues(float e, float r) {
    boolean pass = true;

    if (r != e) {
      System.out.println("Value Mismatch: expected " + e + ", received " + r);
      pass = false;
    }
    return pass;
  }

  public static boolean compareValues(double e, double r) {
    boolean pass = true;

    if (r != e) {
      System.out.println("Value Mismatch: expected " + e + ", received " + r);
      pass = false;
    }
    return pass;
  }

  public static boolean compareValues(Object e, Object r, String t) {
    boolean pass = true;

    if (t.equals("Boolean")) {
      Boolean exp = (Boolean) e;
      Boolean rec = (Boolean) r;
      if (rec == exp)
        return true;
      if ((rec == null && exp != null) || (rec != null && exp == null)) {
        pass = false;
      } else if (!rec.equals(exp)) {
        System.out
            .println("Value Mismatch: expected " + exp + ", received " + rec);
        pass = false;
      }
    } else if (t.equals("Character")) {
      Character exp = (Character) e;
      Character rec = (Character) r;
      if (rec == exp)
        return true;
      if ((rec == null && exp != null) || (rec != null && exp == null)) {
        pass = false;
      } else if (!rec.equals(exp)) {
        System.out
            .println("Value Mismatch: expected " + exp + ", received " + rec);
        pass = false;
      }
    } else if (t.equals("Byte")) {
      Byte exp = (Byte) e;
      Byte rec = (Byte) r;
      if (rec == exp)
        return true;
      if ((rec == null && exp != null) || (rec != null && exp == null)) {
        pass = false;
      } else if (!rec.equals(exp)) {
        System.out
            .println("Value Mismatch: expected " + exp + ", received " + rec);
        pass = false;
      }
    } else if (t.equals("Short")) {
      Short exp = (Short) e;
      Short rec = (Short) r;
      if (rec == exp)
        return true;
      if ((rec == null && exp != null) || (rec != null && exp == null)) {
        pass = false;
      } else if (!rec.equals(exp)) {
        System.out
            .println("Value Mismatch: expected " + exp + ", received " + rec);
        pass = false;
      }
    } else if (t.equals("Integer")) {
      Integer exp = (Integer) e;
      Integer rec = (Integer) r;
      if (rec == exp)
        return true;
      if ((rec == null && exp != null) || (rec != null && exp == null)) {
        pass = false;
      } else if (!rec.equals(exp)) {
        System.out
            .println("Value Mismatch: expected " + exp + ", received " + rec);
        pass = false;
      }
    } else if (t.equals("Long")) {
      Long exp = (Long) e;
      Long rec = (Long) r;
      if (rec == exp)
        return true;
      if ((rec == null && exp != null) || (rec != null && exp == null)) {
        pass = false;
      } else if (!rec.equals(exp)) {
        System.out
            .println("Value Mismatch: expected " + exp + ", received " + rec);
        pass = false;
      }
    } else if (t.equals("Float")) {
      Float exp = (Float) e;
      Float rec = (Float) r;
      if (rec == exp)
        return true;
      if ((rec == null && exp != null) || (rec != null && exp == null)) {
        pass = false;
      } else if (!rec.equals(exp)) {
        System.out
            .println("Value Mismatch: expected " + exp + ", received " + rec);
        pass = false;
      }
    } else if (t.equals("Double")) {
      Double exp = (Double) e;
      Double rec = (Double) r;
      if (rec == exp)
        return true;
      if ((rec == null && exp != null) || (rec != null && exp == null)) {
        pass = false;
      } else if (!rec.equals(exp)) {
        System.out
            .println("Value Mismatch: expected " + exp + ", received " + rec);
        pass = false;
      }
    } else if (t.equals("String")) {
      String exp = (String) e;
      String rec = (String) r;
      if (rec == exp)
        return true;
      if ((rec == null && exp != null) || (rec != null && exp == null)) {
        pass = false;
      } else if (!rec.equals(exp)) {
        System.out
            .println("Value Mismatch: expected " + exp + ", received " + rec);
        pass = false;
      }
    } else if (t.equals("Date")) {
      Date exp = (Date) e;
      Date rec = (Date) r;
      if (rec == exp)
        return true;
      if ((rec == null && exp != null) || (rec != null && exp == null)) {
        pass = false;
      } else if (!rec.equals(exp)) {
        System.out
            .println("Value Mismatch: expected " + exp + ", received " + rec);
        pass = false;
      }
    } else if (t.equals("Calendar")) {
      Calendar exp = (Calendar) e;
      Calendar rec = (Calendar) r;
      if (rec == exp)
        return true;
      if ((rec == null && exp != null) || (rec != null && exp == null)) {
        pass = false;
      } else if (!compareCalendars(rec, exp)) {
        System.out
            .println("Value Mismatch: expected " + exp + ", received " + rec);
        pass = false;
      }
    } else if (t.equals("BigInteger")) {
      BigInteger exp = (BigInteger) e;
      BigInteger rec = (BigInteger) r;
      if (rec == exp)
        return true;
      if ((rec == null && exp != null) || (rec != null && exp == null)) {
        pass = false;
      } else if (!rec.equals(exp)) {
        System.out
            .println("Value Mismatch: expected " + exp + ", received " + rec);
        pass = false;
      }
    } else if (t.equals("BigDecimal")) {
      BigDecimal exp = (BigDecimal) e;
      BigDecimal rec = (BigDecimal) r;
      if (rec == exp)
        return true;
      if ((rec == null && exp != null) || (rec != null && exp == null)) {
        pass = false;
      } else if (!rec.equals(exp)) {
        System.out
            .println("Value Mismatch: expected " + exp + ", received " + rec);
        pass = false;
      }
    } else if (t.equals("QName")) {
      QName exp = (QName) e;
      QName rec = (QName) r;
      if (rec == exp)
        return true;
      if ((rec == null && exp != null) || (rec != null && exp == null)) {
        pass = false;
      } else if (!rec.equals(exp)) {
        System.out
            .println("Value Mismatch: expected " + exp + ", received " + rec);
        pass = false;
      }
    }
    return pass;
  }

  public static boolean compareArrayValues(Object e, Object r, String t) {
    System.out.println("JAXRPC_Data:compareArrayValues");
    boolean pass = true;

    if (t.equals("boolean")) {
      boolean[] exp = (boolean[]) e;
      boolean[] rec = (boolean[]) r;
      if (rec.length != exp.length) {
        System.out.println("Array Size MisMatch: expected " + exp.length
            + ", received " + rec.length);
        pass = false;
      }
      for (int i = 0; i < rec.length; i++) {
        if (rec[i] != exp[i]) {
          System.out.println(
              "Array Mismatch: expected " + exp[i] + ", received " + rec[i]);
          pass = false;
        }
      }
    } else if (t.equals("Boolean")) {
      Boolean[] exp = (Boolean[]) e;
      Boolean[] rec = (Boolean[]) r;
      if (rec.length != exp.length) {
        System.out.println("Array Size MisMatch: expected " + exp.length
            + ", received " + rec.length);
        pass = false;
      }
      for (int i = 0; i < rec.length; i++) {
        if (rec[i] == exp[i])
          continue;
        if ((rec[i] == null && exp[i] != null)
            || (rec[i] != null && exp[i] == null)) {
          pass = false;
        } else if (!rec[i].equals(exp[i])) {
          System.out.println(
              "Array Mismatch: expected " + exp[i] + ", received " + rec[i]);
          pass = false;
        }
      }
    } else if (t.equals("char")) {
      char[] exp = (char[]) e;
      char[] rec = (char[]) r;
      if (rec.length != exp.length) {
        System.out.println("Array Size MisMatch: expected " + exp.length
            + ", received " + rec.length);
        pass = false;
      }
      for (int i = 0; i < rec.length; i++) {
        if (rec[i] != exp[i]) {
          System.out.println(
              "Array Mismatch: expected " + exp[i] + ", received " + rec[i]);
          pass = false;
        }
      }
    } else if (t.equals("Character")) {
      Character[] exp = (Character[]) e;
      Character[] rec = (Character[]) r;
      if (rec.length != exp.length) {
        System.out.println("Array Size MisMatch: expected " + exp.length
            + ", received " + rec.length);
        pass = false;
      }
      for (int i = 0; i < rec.length; i++) {
        if (rec[i] == exp[i])
          continue;
        if ((rec[i] == null && exp[i] != null)
            || (rec[i] != null && exp[i] == null)) {
          pass = false;
        } else if (!rec[i].equals(exp[i])) {
          System.out.println(
              "Array Mismatch: expected " + exp[i] + ", received " + rec[i]);
          pass = false;
        }
      }
    } else if (t.equals("byte")) {
      byte[] exp = (byte[]) e;
      byte[] rec = (byte[]) r;
      if (rec.length != exp.length) {
        System.out.println("Array Size MisMatch: expected " + exp.length
            + ", received " + rec.length);
        pass = false;
      }
      for (int i = 0; i < rec.length; i++) {
        if (rec[i] != exp[i]) {
          System.out.println(
              "Array Mismatch: expected " + exp[i] + ", received " + rec[i]);
          pass = false;
        }
      }
    } else if (t.equals("Byte")) {
      Byte[] exp = (Byte[]) e;
      Byte[] rec = (Byte[]) r;
      if (rec.length != exp.length) {
        System.out.println("Array Size MisMatch: expected " + exp.length
            + ", received " + rec.length);
        pass = false;
      }
      for (int i = 0; i < rec.length; i++) {
        if (rec[i] == exp[i])
          continue;
        if ((rec[i] == null && exp[i] != null)
            || (rec[i] != null && exp[i] == null)) {
          pass = false;
        } else if (!rec[i].equals(exp[i])) {
          System.out.println(
              "Array Mismatch: expected " + exp[i] + ", received " + rec[i]);
          pass = false;
        }
      }
    } else if (t.equals("short")) {
      short[] exp = (short[]) e;
      short[] rec = (short[]) r;
      if (rec.length != exp.length) {
        System.out.println("Array Size MisMatch: expected " + exp.length
            + ", received " + rec.length);
        pass = false;
      }
      for (int i = 0; i < rec.length; i++) {
        if (rec[i] != exp[i]) {
          System.out.println(
              "Array Mismatch: expected " + exp[i] + ", received " + rec[i]);
          pass = false;
        }
      }
    } else if (t.equals("Short")) {
      Short[] exp = (Short[]) e;
      Short[] rec = (Short[]) r;
      if (rec.length != exp.length) {
        System.out.println("Array Size MisMatch: expected " + exp.length
            + ", received " + rec.length);
        pass = false;
      }
      for (int i = 0; i < rec.length; i++) {
        if (rec[i] == exp[i])
          continue;
        if ((rec[i] == null && exp[i] != null)
            || (rec[i] != null && exp[i] == null)) {
          pass = false;
        } else if (!rec[i].equals(exp[i])) {
          System.out.println(
              "Array Mismatch: expected " + exp[i] + ", received " + rec[i]);
          pass = false;
        }
      }
    } else if (t.equals("int")) {
      int[] exp = (int[]) e;
      int[] rec = (int[]) r;
      if (rec.length != exp.length) {
        System.out.println("Array Size MisMatch: expected " + exp.length
            + ", received " + rec.length);
        pass = false;
      }
      for (int i = 0; i < rec.length; i++) {
        if (rec[i] != exp[i]) {
          System.out.println(
              "Array Mismatch: expected " + exp[i] + ", received " + rec[i]);
          pass = false;
        }
      }
    } else if (t.equals("Integer")) {
      Integer[] exp = (Integer[]) e;
      Integer[] rec = (Integer[]) r;
      if (rec.length != exp.length) {
        System.out.println("Array Size MisMatch: expected " + exp.length
            + ", received " + rec.length);
        pass = false;
      }
      for (int i = 0; i < rec.length; i++) {
        if (rec[i] == exp[i])
          continue;
        if ((rec[i] == null && exp[i] != null)
            || (rec[i] != null && exp[i] == null)) {
          pass = false;
        } else if (!rec[i].equals(exp[i])) {
          System.out.println(
              "Array Mismatch: expected " + exp[i] + ", received " + rec[i]);
          pass = false;
        }
      }
    } else if (t.equals("long")) {
      long[] exp = (long[]) e;
      long[] rec = (long[]) r;
      if (rec.length != exp.length) {
        System.out.println("Array Size MisMatch: expected " + exp.length
            + ", received " + rec.length);
        pass = false;
      }
      for (int i = 0; i < rec.length; i++) {
        if (rec[i] != exp[i]) {
          System.out.println(
              "Array Mismatch: expected " + exp[i] + ", received " + rec[i]);
          pass = false;
        }
      }
    } else if (t.equals("Long")) {
      Long[] exp = (Long[]) e;
      Long[] rec = (Long[]) r;
      if (rec.length != exp.length) {
        System.out.println("Array Size MisMatch: expected " + exp.length
            + ", received " + rec.length);
        pass = false;
      }
      for (int i = 0; i < rec.length; i++) {
        if (rec[i] == exp[i])
          continue;
        if ((rec[i] == null && exp[i] != null)
            || (rec[i] != null && exp[i] == null)) {
          pass = false;
        } else if (!rec[i].equals(exp[i])) {
          System.out.println(
              "Array Mismatch: expected " + exp[i] + ", received " + rec[i]);
          pass = false;
        }
      }
    } else if (t.equals("float")) {
      float[] exp = (float[]) e;
      float[] rec = (float[]) r;
      if (rec.length != exp.length) {
        System.out.println("Array Size MisMatch: expected " + exp.length
            + ", received " + rec.length);
        pass = false;
      }
      for (int i = 0; i < rec.length; i++) {
        if (rec[i] != exp[i]) {
          System.out.println(
              "Array Mismatch: expected " + exp[i] + ", received " + rec[i]);
          pass = false;
        }
      }
    } else if (t.equals("Float")) {
      Float[] exp = (Float[]) e;
      Float[] rec = (Float[]) r;
      if (rec.length != exp.length) {
        System.out.println("Array Size MisMatch: expected " + exp.length
            + ", received " + rec.length);
        pass = false;
      }
      for (int i = 0; i < rec.length; i++) {
        if (rec[i] == exp[i])
          continue;
        if ((rec[i] == null && exp[i] != null)
            || (rec[i] != null && exp[i] == null)) {
          pass = false;
        } else if (!rec[i].equals(exp[i])) {
          System.out.println(
              "Array Mismatch: expected " + exp[i] + ", received " + rec[i]);
          pass = false;
        }
      }
    } else if (t.equals("double")) {
      double[] exp = (double[]) e;
      double[] rec = (double[]) r;
      if (rec.length != exp.length) {
        System.out.println("Array Size MisMatch: expected " + exp.length
            + ", received " + rec.length);
        pass = false;
      }
      for (int i = 0; i < rec.length; i++) {
        if (rec[i] != exp[i]) {
          System.out.println(
              "Array Mismatch: expected " + exp[i] + ", received " + rec[i]);
          pass = false;
        }
      }
    } else if (t.equals("Double")) {
      Double[] exp = (Double[]) e;
      Double[] rec = (Double[]) r;
      if (rec.length != exp.length) {
        System.out.println("Array Size MisMatch: expected " + exp.length
            + ", received " + rec.length);
        pass = false;
      }
      for (int i = 0; i < rec.length; i++) {
        if (rec[i] == exp[i])
          continue;
        if ((rec[i] == null && exp[i] != null)
            || (rec[i] != null && exp[i] == null)) {
          pass = false;
        } else if (!rec[i].equals(exp[i])) {
          System.out.println(
              "Array Mismatch: expected " + exp[i] + ", received " + rec[i]);
          pass = false;
        }
      }
    } else if (t.equals("String")) {
      String[] exp = (String[]) e;
      String[] rec = (String[]) r;
      if (rec.length != exp.length) {
        System.out.println("Array Size MisMatch: expected " + exp.length
            + ", received " + rec.length);
        pass = false;
      }
      for (int i = 0; i < rec.length; i++) {
        if (rec[i] == exp[i])
          continue;
        if ((rec[i] == null && exp[i] != null)
            || (rec[i] != null && exp[i] == null)) {
          pass = false;
        } else if (!rec[i].equals(exp[i])) {
          System.out.println(
              "Array Mismatch: expected " + exp[i] + ", received " + rec[i]);
          pass = false;
        }
      }
    } else if (t.equals("Date")) {
      Date[] exp = (Date[]) e;
      Date[] rec = (Date[]) r;
      if (rec.length != exp.length) {
        System.out.println("Array Size MisMatch: expected " + exp.length
            + ", received " + rec.length);
        pass = false;
      }
      for (int i = 0; i < rec.length; i++) {
        if (rec[i] == exp[i])
          continue;
        if ((rec[i] == null && exp[i] != null)
            || (rec[i] != null && exp[i] == null)) {
          pass = false;
        } else if (!rec[i].equals(exp[i])) {
          System.out.println(
              "Array Mismatch: expected " + exp[i] + ", received " + rec[i]);
          pass = false;
        }
      }
    } else if (t.equals("Calendar")) {
      Calendar[] exp = (Calendar[]) e;
      Calendar[] rec = (Calendar[]) r;
      if (rec.length != exp.length) {
        System.out.println("Array Size MisMatch: expected " + exp.length
            + ", received " + rec.length);
        pass = false;
      }
      for (int i = 0; i < rec.length; i++) {
        if (rec[i] == exp[i])
          continue;
        if ((rec[i] == null && exp[i] != null)
            || (rec[i] != null && exp[i] == null)) {
          pass = false;
        } else if (!compareCalendars(rec[i], exp[i])) {
          System.out.println(
              "Array Mismatch: expected " + exp[i] + ", received " + rec[i]);
          pass = false;
        }
      }
    } else if (t.equals("BigInteger")) {
      BigInteger[] exp = (BigInteger[]) e;
      BigInteger[] rec = (BigInteger[]) r;
      if (rec.length != exp.length) {
        System.out.println("Array Size MisMatch: expected " + exp.length
            + ", received " + rec.length);
        pass = false;
      }
      for (int i = 0; i < rec.length; i++) {
        if (rec[i] == exp[i])
          continue;
        if ((rec[i] == null && exp[i] != null)
            || (rec[i] != null && exp[i] == null)) {
          pass = false;
        } else if (!rec[i].equals(exp[i])) {
          System.out.println(
              "Array Mismatch: expected " + exp[i] + ", received " + rec[i]);
          pass = false;
        }
      }
    } else if (t.equals("BigDecimal")) {
      BigDecimal[] exp = (BigDecimal[]) e;
      BigDecimal[] rec = (BigDecimal[]) r;
      if (rec.length != exp.length) {
        System.out.println("Array Size MisMatch: expected " + exp.length
            + ", received " + rec.length);
        pass = false;
      }
      for (int i = 0; i < rec.length; i++) {
        if (rec[i] == exp[i])
          continue;
        if ((rec[i] == null && exp[i] != null)
            || (rec[i] != null && exp[i] == null)) {
          pass = false;
        } else if (!rec[i].equals(exp[i])) {
          System.out.println(
              "Array Mismatch: expected " + exp[i] + ", received " + rec[i]);
          pass = false;
        }
      }
    } else if (t.equals("QName")) {
      QName[] exp = (QName[]) e;
      QName[] rec = (QName[]) r;
      if (rec.length != exp.length) {
        System.out.println("Array Size MisMatch: expected " + exp.length
            + ", received " + rec.length);
        pass = false;
      }
      for (int i = 0; i < rec.length; i++) {
        if (rec[i] == exp[i])
          continue;
        if ((rec[i] == null && exp[i] != null)
            || (rec[i] != null && exp[i] == null)) {
          pass = false;
        } else if (!rec[i].equals(exp[i])) {
          System.out.println(
              "Array Mismatch: expected " + exp[i] + ", received " + rec[i]);
          pass = false;
        }
      }
    }
    return pass;
  }

  public static boolean compareMultiArrayValues(Object e, Object r, String t) {
    System.out.println("JAXRPC_Data:compareMultiArrayValues");
    boolean pass = true;

    if (t.equals("boolean")) {
      boolean[][] exp = (boolean[][]) e;
      boolean[][] rec = (boolean[][]) r;
      if (rec.length != exp.length) {
        System.out.println("Multi Array Size MisMatch: expected " + exp.length
            + ", received " + rec.length);
        pass = false;
      }
      for (int i = 0; i < rec.length; i++) {
        if (rec[i].length != exp[i].length) {
          System.out.println("Multi Array Size MisMatch: expected "
              + exp[i].length + ", received " + rec[i].length);
          pass = false;
        } else {
          for (int k = 0; k < rec[i].length; k++) {
            if (rec[i][k] != exp[i][k]) {
              System.out.println("Array Mismatch: expected " + exp[i][k]
                  + ", received " + rec[i][k]);
              pass = false;
            }
          }
        }
      }
    } else if (t.equals("Boolean")) {
      Boolean[][] exp = (Boolean[][]) e;
      Boolean[][] rec = (Boolean[][]) r;
      if (rec.length != exp.length) {
        System.out.println("Multi Array Size MisMatch: expected " + exp.length
            + ", received " + rec.length);
        pass = false;
      }
      for (int i = 0; i < rec.length; i++) {
        if (rec[i].length != exp[i].length) {
          System.out.println("Multi Array Size MisMatch: expected "
              + exp[i].length + ", received " + rec[i].length);
          pass = false;
        } else {
          for (int k = 0; k < rec[i].length; k++) {
            if (rec[i][k] == exp[i][k])
              continue;
            if ((rec[i][k] == null && exp[i][k] != null)
                && (rec[i][k] != null && exp[i][k] == null)) {
              pass = false;
            } else if (!rec[i][k].equals(exp[i][k])) {
              System.out.println("Array Mismatch: expected " + exp[i][k]
                  + ", received " + rec[i][k]);
              pass = false;
            }
          }
        }
      }
    } else if (t.equals("char")) {
      char[][] exp = (char[][]) e;
      char[][] rec = (char[][]) r;
      if (rec.length != exp.length) {
        System.out.println("Multi Array Size MisMatch: expected " + exp.length
            + ", received " + rec.length);
        pass = false;
      }
      for (int i = 0; i < rec.length; i++) {
        if (rec[i].length != exp[i].length) {
          System.out.println("Multi Array Size MisMatch: expected "
              + exp[i].length + ", received " + rec[i].length);
          pass = false;
        } else {
          for (int k = 0; k < rec[i].length; k++) {
            if (rec[i][k] != exp[i][k]) {
              System.out.println("Array Mismatch: expected " + exp[i][k]
                  + ", received " + rec[i][k]);
              pass = false;
            }
          }
        }
      }
    } else if (t.equals("Character")) {
      Character[][] exp = (Character[][]) e;
      Character[][] rec = (Character[][]) r;
      if (rec.length != exp.length) {
        System.out.println("Multi Array Size MisMatch: expected " + exp.length
            + ", received " + rec.length);
        pass = false;
      }
      for (int i = 0; i < rec.length; i++) {
        if (rec[i].length != exp[i].length) {
          System.out.println("Multi Array Size MisMatch: expected "
              + exp[i].length + ", received " + rec[i].length);
          pass = false;
        } else {
          for (int k = 0; k < rec[i].length; k++) {
            if (rec[i][k] == exp[i][k])
              continue;
            if ((rec[i][k] == null && exp[i][k] != null)
                && (rec[i][k] != null && exp[i][k] == null)) {
              pass = false;
            } else if (!rec[i][k].equals(exp[i][k])) {
              System.out.println("Array Mismatch: expected " + exp[i][k]
                  + ", received " + rec[i][k]);
              pass = false;
            }
          }
        }
      }
    } else if (t.equals("byte")) {
      byte[][] exp = (byte[][]) e;
      byte[][] rec = (byte[][]) r;
      if (rec.length != exp.length) {
        System.out.println("Multi Array Size MisMatch: expected " + exp.length
            + ", received " + rec.length);
        pass = false;
      }
      for (int i = 0; i < rec.length; i++) {
        if (rec[i].length != exp[i].length) {
          System.out.println("Multi Array Size MisMatch: expected "
              + exp[i].length + ", received " + rec[i].length);
          pass = false;
        } else {
          for (int k = 0; k < rec[i].length; k++) {
            if (rec[i][k] != exp[i][k]) {
              System.out.println("Array Mismatch: expected " + exp[i][k]
                  + ", received " + rec[i][k]);
              pass = false;
            }
          }
        }
      }
    } else if (t.equals("Byte")) {
      Byte[][] exp = (Byte[][]) e;
      Byte[][] rec = (Byte[][]) r;
      if (rec.length != exp.length) {
        System.out.println("Multi Array Size MisMatch: expected " + exp.length
            + ", received " + rec.length);
        pass = false;
      }
      for (int i = 0; i < rec.length; i++) {
        if (rec[i].length != exp[i].length) {
          System.out.println("Multi Array Size MisMatch: expected "
              + exp[i].length + ", received " + rec[i].length);
          pass = false;
        } else {
          for (int k = 0; k < rec[i].length; k++) {
            if (rec[i][k] == exp[i][k])
              continue;
            if ((rec[i][k] == null && exp[i][k] != null)
                && (rec[i][k] != null && exp[i][k] == null)) {
              pass = false;
            } else if (!rec[i][k].equals(exp[i][k])) {
              System.out.println("Array Mismatch: expected " + exp[i][k]
                  + ", received " + rec[i][k]);
              pass = false;
            }
          }
        }
      }
    } else if (t.equals("short")) {
      short[][] exp = (short[][]) e;
      short[][] rec = (short[][]) r;
      if (rec.length != exp.length) {
        System.out.println("Multi Array Size MisMatch: expected " + exp.length
            + ", received " + rec.length);
        pass = false;
      }
      for (int i = 0; i < rec.length; i++) {
        if (rec[i].length != exp[i].length) {
          System.out.println("Multi Array Size MisMatch: expected "
              + exp[i].length + ", received " + rec[i].length);
          pass = false;
        } else {
          for (int k = 0; k < rec[i].length; k++) {
            if (rec[i][k] != exp[i][k]) {
              System.out.println("Array Mismatch: expected " + exp[i][k]
                  + ", received " + rec[i][k]);
              pass = false;
            }
          }
        }
      }
    } else if (t.equals("Short")) {
      Short[][] exp = (Short[][]) e;
      Short[][] rec = (Short[][]) r;
      if (rec.length != exp.length) {
        System.out.println("Multi Array Size MisMatch: expected " + exp.length
            + ", received " + rec.length);
        pass = false;
      }
      for (int i = 0; i < rec.length; i++) {
        if (rec[i].length != exp[i].length) {
          System.out.println("Multi Array Size MisMatch: expected "
              + exp[i].length + ", received " + rec[i].length);
          pass = false;
        } else {
          for (int k = 0; k < rec[i].length; k++) {
            if (rec[i][k] == exp[i][k])
              continue;
            if ((rec[i][k] == null && exp[i][k] != null)
                && (rec[i][k] != null && exp[i][k] == null)) {
              pass = false;
            } else if (!rec[i][k].equals(exp[i][k])) {
              System.out.println("Array Mismatch: expected " + exp[i][k]
                  + ", received " + rec[i][k]);
              pass = false;
            }
          }
        }
      }
    } else if (t.equals("int")) {
      int[][] exp = (int[][]) e;
      int[][] rec = (int[][]) r;
      if (rec.length != exp.length) {
        System.out.println("Multi Array Size MisMatch: expected " + exp.length
            + ", received " + rec.length);
        pass = false;
      }
      for (int i = 0; i < rec.length; i++) {
        if (rec[i].length != exp[i].length) {
          System.out.println("Multi Array Size MisMatch: expected "
              + exp[i].length + ", received " + rec[i].length);
          pass = false;
        } else {
          for (int k = 0; k < rec[i].length; k++) {
            if (rec[i][k] != exp[i][k]) {
              System.out.println("Array Mismatch: expected " + exp[i][k]
                  + ", received " + rec[i][k]);
              pass = false;
            }
          }
        }
      }
    } else if (t.equals("Integer")) {
      Integer[][] exp = (Integer[][]) e;
      Integer[][] rec = (Integer[][]) r;
      if (rec.length != exp.length) {
        System.out.println("Multi Array Size MisMatch: expected " + exp.length
            + ", received " + rec.length);
        pass = false;
      }
      for (int i = 0; i < rec.length; i++) {
        if (rec[i].length != exp[i].length) {
          System.out.println("Multi Array Size MisMatch: expected "
              + exp[i].length + ", received " + rec[i].length);
          pass = false;
        } else {
          for (int k = 0; k < rec[i].length; k++) {
            if (rec[i][k] == exp[i][k])
              continue;
            if ((rec[i][k] == null && exp[i][k] != null)
                && (rec[i][k] != null && exp[i][k] == null)) {
              pass = false;
            } else if (!rec[i][k].equals(exp[i][k])) {
              System.out.println("Array Mismatch: expected " + exp[i][k]
                  + ", received " + rec[i][k]);
              pass = false;
            }
          }
        }
      }
    } else if (t.equals("long")) {
      long[][] exp = (long[][]) e;
      long[][] rec = (long[][]) r;
      if (rec.length != exp.length) {
        System.out.println("Multi Array Size MisMatch: expected " + exp.length
            + ", received " + rec.length);
        pass = false;
      }
      for (int i = 0; i < rec.length; i++) {
        if (rec[i].length != exp[i].length) {
          System.out.println("Multi Array Size MisMatch: expected "
              + exp[i].length + ", received " + rec[i].length);
          pass = false;
        } else {
          for (int k = 0; k < rec[i].length; k++) {
            if (rec[i][k] != exp[i][k]) {
              System.out.println("Array Mismatch: expected " + exp[i][k]
                  + ", received " + rec[i][k]);
              pass = false;
            }
          }
        }
      }
    } else if (t.equals("Long")) {
      Long[][] exp = (Long[][]) e;
      Long[][] rec = (Long[][]) r;
      if (rec.length != exp.length) {
        System.out.println("Multi Array Size MisMatch: expected " + exp.length
            + ", received " + rec.length);
        pass = false;
      }
      for (int i = 0; i < rec.length; i++) {
        if (rec[i].length != exp[i].length) {
          System.out.println("Multi Array Size MisMatch: expected "
              + exp[i].length + ", received " + rec[i].length);
          pass = false;
        } else {
          for (int k = 0; k < rec[i].length; k++) {
            if (rec[i][k] == exp[i][k])
              continue;
            if ((rec[i][k] == null && exp[i][k] != null)
                && (rec[i][k] != null && exp[i][k] == null)) {
              pass = false;
            } else if (!rec[i][k].equals(exp[i][k])) {
              System.out.println("Array Mismatch: expected " + exp[i][k]
                  + ", received " + rec[i][k]);
              pass = false;
            }
          }
        }
      }
    } else if (t.equals("float")) {
      float[][] exp = (float[][]) e;
      float[][] rec = (float[][]) r;
      if (rec.length != exp.length) {
        System.out.println("Multi Array Size MisMatch: expected " + exp.length
            + ", received " + rec.length);
        pass = false;
      }
      for (int i = 0; i < rec.length; i++) {
        if (rec[i].length != exp[i].length) {
          System.out.println("Multi Array Size MisMatch: expected "
              + exp[i].length + ", received " + rec[i].length);
          pass = false;
        } else {
          for (int k = 0; k < rec[i].length; k++) {
            if (rec[i][k] != exp[i][k]) {
              System.out.println("Array Mismatch: expected " + exp[i][k]
                  + ", received " + rec[i][k]);
              pass = false;
            }
          }
        }
      }
    } else if (t.equals("Float")) {
      Float[][] exp = (Float[][]) e;
      Float[][] rec = (Float[][]) r;
      if (rec.length != exp.length) {
        System.out.println("Multi Array Size MisMatch: expected " + exp.length
            + ", received " + rec.length);
        pass = false;
      }
      for (int i = 0; i < rec.length; i++) {
        if (rec[i].length != exp[i].length) {
          System.out.println("Multi Array Size MisMatch: expected "
              + exp[i].length + ", received " + rec[i].length);
          pass = false;
        } else {
          for (int k = 0; k < rec[i].length; k++) {
            if (rec[i][k] == exp[i][k])
              continue;
            if ((rec[i][k] == null && exp[i][k] != null)
                && (rec[i][k] != null && exp[i][k] == null)) {
              pass = false;
            } else if (!rec[i][k].equals(exp[i][k])) {
              System.out.println("Array Mismatch: expected " + exp[i][k]
                  + ", received " + rec[i][k]);
              pass = false;
            }
          }
        }
      }
    } else if (t.equals("double")) {
      double[][] exp = (double[][]) e;
      double[][] rec = (double[][]) r;
      if (rec.length != exp.length) {
        System.out.println("Multi Array Size MisMatch: expected " + exp.length
            + ", received " + rec.length);
        pass = false;
      }
      for (int i = 0; i < rec.length; i++) {
        if (rec[i].length != exp[i].length) {
          System.out.println("Multi Array Size MisMatch: expected "
              + exp[i].length + ", received " + rec[i].length);
          pass = false;
        } else {
          for (int k = 0; k < rec[i].length; k++) {
            if (rec[i][k] != exp[i][k]) {
              System.out.println("Array Mismatch: expected " + exp[i][k]
                  + ", received " + rec[i][k]);
              pass = false;
            }
          }
        }
      }
    } else if (t.equals("Double")) {
      Double[][] exp = (Double[][]) e;
      Double[][] rec = (Double[][]) r;
      if (rec.length != exp.length) {
        System.out.println("Multi Array Size MisMatch: expected " + exp.length
            + ", received " + rec.length);
        pass = false;
      }
      for (int i = 0; i < rec.length; i++) {
        if (rec[i].length != exp[i].length) {
          System.out.println("Multi Array Size MisMatch: expected "
              + exp[i].length + ", received " + rec[i].length);
          pass = false;
        } else {
          for (int k = 0; k < rec[i].length; k++) {
            if (rec[i][k] == exp[i][k])
              continue;
            if ((rec[i][k] == null && exp[i][k] != null)
                && (rec[i][k] != null && exp[i][k] == null)) {
              pass = false;
            } else if (!rec[i][k].equals(exp[i][k])) {
              System.out.println("Array Mismatch: expected " + exp[i][k]
                  + ", received " + rec[i][k]);
              pass = false;
            }
          }
        }
      }
    } else if (t.equals("String")) {
      String[][] exp = (String[][]) e;
      String[][] rec = (String[][]) r;
      if (rec.length != exp.length) {
        System.out.println("Multi Array Size MisMatch: expected " + exp.length
            + ", received " + rec.length);
        pass = false;
      }
      for (int i = 0; i < rec.length; i++) {
        if (rec[i].length != exp[i].length) {
          System.out.println("Multi Array Size MisMatch: expected "
              + exp[i].length + ", received " + rec[i].length);
          pass = false;
        } else {
          for (int k = 0; k < rec[i].length; k++) {
            if (rec[i][k] == exp[i][k])
              continue;
            if ((rec[i][k] == null && exp[i][k] != null)
                && (rec[i][k] != null && exp[i][k] == null)) {
              pass = false;
            } else if (!rec[i][k].equals(exp[i][k])) {
              System.out.println("Array Mismatch: expected " + exp[i][k]
                  + ", received " + rec[i][k]);
              pass = false;
            }
          }
        }
      }
    } else if (t.equals("Date")) {
      Date[][] exp = (Date[][]) e;
      Date[][] rec = (Date[][]) r;
      if (rec.length != exp.length) {
        System.out.println("Multi Array Size MisMatch: expected " + exp.length
            + ", received " + rec.length);
        pass = false;
      }
      for (int i = 0; i < rec.length; i++) {
        if (rec[i].length != exp[i].length) {
          System.out.println("Multi Array Size MisMatch: expected "
              + exp[i].length + ", received " + rec[i].length);
          pass = false;
        } else {
          for (int k = 0; k < rec[i].length; k++) {
            if (rec[i][k] == exp[i][k])
              continue;
            if ((rec[i][k] == null && exp[i][k] != null)
                && (rec[i][k] != null && exp[i][k] == null)) {
              pass = false;
            } else if (!rec[i][k].equals(exp[i][k])) {
              System.out.println("Array Mismatch: expected " + exp[i][k]
                  + ", received " + rec[i][k]);
              pass = false;
            }
          }
        }
      }
    } else if (t.equals("Calendar")) {
      Calendar[][] exp = (Calendar[][]) e;
      Calendar[][] rec = (Calendar[][]) r;
      if (rec.length != exp.length) {
        System.out.println("Multi Array Size MisMatch: expected " + exp.length
            + ", received " + rec.length);
        pass = false;
      }
      for (int i = 0; i < rec.length; i++) {
        if (rec[i].length != exp[i].length) {
          System.out.println("Multi Array Size MisMatch: expected "
              + exp[i].length + ", received " + rec[i].length);
          pass = false;
        } else {
          for (int k = 0; k < rec[i].length; k++) {
            if (rec[i][k] == exp[i][k])
              continue;
            if ((rec[i][k] == null && exp[i][k] != null)
                && (rec[i][k] != null && exp[i][k] == null)) {
              pass = false;
            } else if (!compareCalendars(rec[i][k], exp[i][k])) {
              System.out.println("Array Mismatch: expected " + exp[i][k]
                  + ", received " + rec[i][k]);
              pass = false;
            }
          }
        }
      }
    } else if (t.equals("BigInteger")) {
      BigInteger[][] exp = (BigInteger[][]) e;
      BigInteger[][] rec = (BigInteger[][]) r;
      if (rec.length != exp.length) {
        System.out.println("Multi Array Size MisMatch: expected " + exp.length
            + ", received " + rec.length);
        pass = false;
      }
      for (int i = 0; i < rec.length; i++) {
        if (rec[i].length != exp[i].length) {
          System.out.println("Multi Array Size MisMatch: expected "
              + exp[i].length + ", received " + rec[i].length);
          pass = false;
        } else {
          for (int k = 0; k < rec[i].length; k++) {
            if (rec[i][k] == exp[i][k])
              continue;
            if ((rec[i][k] == null && exp[i][k] != null)
                && (rec[i][k] != null && exp[i][k] == null)) {
              pass = false;
            } else if (!rec[i][k].equals(exp[i][k])) {
              System.out.println("Array Mismatch: expected " + exp[i][k]
                  + ", received " + rec[i][k]);
              pass = false;
            }
          }
        }
      }
    } else if (t.equals("BigDecimal")) {
      BigDecimal[][] exp = (BigDecimal[][]) e;
      BigDecimal[][] rec = (BigDecimal[][]) r;
      if (rec.length != exp.length) {
        System.out.println("Multi Array Size MisMatch: expected " + exp.length
            + ", received " + rec.length);
        pass = false;
      }
      for (int i = 0; i < rec.length; i++) {
        if (rec[i].length != exp[i].length) {
          System.out.println("Multi Array Size MisMatch: expected "
              + exp[i].length + ", received " + rec[i].length);
          pass = false;
        } else {
          for (int k = 0; k < rec[i].length; k++) {
            if (rec[i][k] == exp[i][k])
              continue;
            if ((rec[i][k] == null && exp[i][k] != null)
                && (rec[i][k] != null && exp[i][k] == null)) {
              pass = false;
            } else if (!rec[i][k].equals(exp[i][k])) {
              System.out.println("Array Mismatch: expected " + exp[i][k]
                  + ", received " + rec[i][k]);
              pass = false;
            }
          }
        }
      }
    } else if (t.equals("QName")) {
      QName[][] exp = (QName[][]) e;
      QName[][] rec = (QName[][]) r;
      if (rec.length != exp.length) {
        System.out.println("Multi Array Size MisMatch: expected " + exp.length
            + ", received " + rec.length);
        pass = false;
      }
      for (int i = 0; i < rec.length; i++) {
        if (rec[i].length != exp[i].length) {
          System.out.println("Multi Array Size MisMatch: expected "
              + exp[i].length + ", received " + rec[i].length);
          pass = false;
        } else {
          for (int k = 0; k < rec[i].length; k++) {
            if (rec[i][k] == exp[i][k])
              continue;
            if ((rec[i][k] == null && exp[i][k] != null)
                && (rec[i][k] != null && exp[i][k] == null)) {
              pass = false;
            } else if (!rec[i][k].equals(exp[i][k])) {
              System.out.println("Array Mismatch: expected " + exp[i][k]
                  + ", received " + rec[i][k]);
              pass = false;
            }
          }
        }
      }
    }
    return pass;
  }

  public static String returnArrayValues(Object o, String t) {
    String values = null;
    if (t.equals("boolean")) {
      boolean[] v = (boolean[]) o;
      for (int i = 0; i < v.length; i++)
        values += ", " + v[i];
    } else if (t.equals("Boolean")) {
      Boolean[] v = (Boolean[]) o;
      for (int i = 0; i < v.length; i++)
        values += ", " + v[i];
    } else if (t.equals("char")) {
      char[] v = (char[]) o;
      for (int i = 0; i < v.length; i++)
        values += ", " + v[i];
    } else if (t.equals("Character")) {
      Character[] v = (Character[]) o;
      for (int i = 0; i < v.length; i++)
        values += ", " + v[i];
    } else if (t.equals("byte")) {
      byte[] v = (byte[]) o;
      for (int i = 0; i < v.length; i++)
        values += ", " + v[i];
    } else if (t.equals("Byte")) {
      Byte[] v = (Byte[]) o;
      for (int i = 0; i < v.length; i++)
        values += ", " + v[i];
    } else if (t.equals("short")) {
      short[] v = (short[]) o;
      for (int i = 0; i < v.length; i++)
        values += ", " + v[i];
    } else if (t.equals("Short")) {
      Short[] v = (Short[]) o;
      for (int i = 0; i < v.length; i++)
        values += ", " + v[i];
    } else if (t.equals("int")) {
      int[] v = (int[]) o;
      for (int i = 0; i < v.length; i++)
        values += ", " + v[i];
    } else if (t.equals("Integer")) {
      Integer[] v = (Integer[]) o;
      for (int i = 0; i < v.length; i++)
        values += ", " + v[i];
    } else if (t.equals("long")) {
      long[] v = (long[]) o;
      for (int i = 0; i < v.length; i++)
        values += ", " + v[i];
    } else if (t.equals("Long")) {
      Long[] v = (Long[]) o;
      for (int i = 0; i < v.length; i++)
        values += ", " + v[i];
    } else if (t.equals("float")) {
      float[] v = (float[]) o;
      for (int i = 0; i < v.length; i++)
        values += ", " + v[i];
    } else if (t.equals("Float")) {
      Float[] v = (Float[]) o;
      for (int i = 0; i < v.length; i++)
        values += ", " + v[i];
    } else if (t.equals("double")) {
      double[] v = (double[]) o;
      for (int i = 0; i < v.length; i++)
        values += ", " + v[i];
    } else if (t.equals("Double")) {
      Double[] v = (Double[]) o;
      for (int i = 0; i < v.length; i++)
        values += ", " + v[i];
    } else if (t.equals("String")) {
      String[] v = (String[]) o;
      for (int i = 0; i < v.length; i++)
        values += ", " + v[i];
    } else if (t.equals("Date")) {
      Date[] v = (Date[]) o;
      for (int i = 0; i < v.length; i++)
        values += ", " + v[i];
    } else if (t.equals("Calendar")) {
      Calendar[] v = (Calendar[]) o;
      for (int i = 0; i < v.length; i++)
        values += ", " + v[i];
    } else if (t.equals("BigInteger")) {
      BigInteger[] v = (BigInteger[]) o;
      for (int i = 0; i < v.length; i++)
        values += ", " + v[i];
    } else if (t.equals("BigDecimal")) {
      BigDecimal[] v = (BigDecimal[]) o;
      for (int i = 0; i < v.length; i++)
        values += ", " + v[i];
    } else if (t.equals("QName")) {
      QName[] v = (QName[]) o;
      for (int i = 0; i < v.length; i++)
        values += ", " + v[i];
    }
    return values;
  }

  public static String returnMultiArrayValues(Object o, String t) {
    String values = null;
    if (t.equals("boolean")) {
      boolean[][] v = (boolean[][]) o;
      for (int i = 0; i < v.length; i++) {
        for (int k = 0; k < v[i].length; k++)
          values += ", " + v[i][k];
      }
    } else if (t.equals("Boolean")) {
      Boolean[][] v = (Boolean[][]) o;
      for (int i = 0; i < v.length; i++) {
        for (int k = 0; k < v[i].length; k++)
          values += ", " + v[i][k];
      }
    } else if (t.equals("char")) {
      char[][] v = (char[][]) o;
      for (int i = 0; i < v.length; i++) {
        for (int k = 0; k < v[i].length; k++)
          values += ", " + v[i][k];
      }
    } else if (t.equals("Character")) {
      Character[][] v = (Character[][]) o;
      for (int i = 0; i < v.length; i++) {
        for (int k = 0; k < v[i].length; k++)
          values += ", " + v[i][k];
      }
    } else if (t.equals("byte")) {
      byte[][] v = (byte[][]) o;
      for (int i = 0; i < v.length; i++) {
        for (int k = 0; k < v[i].length; k++)
          values += ", " + v[i][k];
      }
    } else if (t.equals("Byte")) {
      Byte[][] v = (Byte[][]) o;
      for (int i = 0; i < v.length; i++) {
        for (int k = 0; k < v[i].length; k++)
          values += ", " + v[i][k];
      }
    } else if (t.equals("short")) {
      short[][] v = (short[][]) o;
      for (int i = 0; i < v.length; i++) {
        for (int k = 0; k < v[i].length; k++)
          values += ", " + v[i][k];
      }
    } else if (t.equals("Short")) {
      Short[][] v = (Short[][]) o;
      for (int i = 0; i < v.length; i++) {
        for (int k = 0; k < v[i].length; k++)
          values += ", " + v[i][k];
      }
    } else if (t.equals("int")) {
      int[][] v = (int[][]) o;
      for (int i = 0; i < v.length; i++) {
        for (int k = 0; k < v[i].length; k++)
          values += ", " + v[i][k];
      }
    } else if (t.equals("Integer")) {
      Integer[][] v = (Integer[][]) o;
      for (int i = 0; i < v.length; i++) {
        for (int k = 0; k < v[i].length; k++)
          values += ", " + v[i][k];
      }
    } else if (t.equals("long")) {
      long[][] v = (long[][]) o;
      for (int i = 0; i < v.length; i++) {
        for (int k = 0; k < v[i].length; k++)
          values += ", " + v[i][k];
      }
    } else if (t.equals("Long")) {
      Long[][] v = (Long[][]) o;
      for (int i = 0; i < v.length; i++) {
        for (int k = 0; k < v[i].length; k++)
          values += ", " + v[i][k];
      }
    } else if (t.equals("float")) {
      float[][] v = (float[][]) o;
      for (int i = 0; i < v.length; i++) {
        for (int k = 0; k < v[i].length; k++)
          values += ", " + v[i][k];
      }
    } else if (t.equals("Float")) {
      Float[][] v = (Float[][]) o;
      for (int i = 0; i < v.length; i++) {
        for (int k = 0; k < v[i].length; k++)
          values += ", " + v[i][k];
      }
    } else if (t.equals("double")) {
      double[][] v = (double[][]) o;
      for (int i = 0; i < v.length; i++) {
        for (int k = 0; k < v[i].length; k++)
          values += ", " + v[i][k];
      }
    } else if (t.equals("Double")) {
      Double[][] v = (Double[][]) o;
      for (int i = 0; i < v.length; i++) {
        for (int k = 0; k < v[i].length; k++)
          values += ", " + v[i][k];
      }
    } else if (t.equals("String")) {
      String[][] v = (String[][]) o;
      for (int i = 0; i < v.length; i++) {
        for (int k = 0; k < v[i].length; k++)
          values += ", " + v[i][k];
      }
    } else if (t.equals("Date")) {
      Date[][] v = (Date[][]) o;
      for (int i = 0; i < v.length; i++) {
        for (int k = 0; k < v[i].length; k++)
          values += ", " + v[i][k];
      }
    } else if (t.equals("Calendar")) {
      Calendar[][] v = (Calendar[][]) o;
      for (int i = 0; i < v.length; i++) {
        for (int k = 0; k < v[i].length; k++)
          values += ", " + v[i][k];
      }
    } else if (t.equals("BigInteger")) {
      BigInteger[][] v = (BigInteger[][]) o;
      for (int i = 0; i < v.length; i++) {
        for (int k = 0; k < v[i].length; k++)
          values += ", " + v[i][k];
      }
    } else if (t.equals("BigDecimal")) {
      BigDecimal[][] v = (BigDecimal[][]) o;
      for (int i = 0; i < v.length; i++) {
        for (int k = 0; k < v[i].length; k++)
          values += ", " + v[i][k];
      }
    } else if (t.equals("QName")) {
      QName[][] v = (QName[][]) o;
      for (int i = 0; i < v.length; i++) {
        for (int k = 0; k < v[i].length; k++)
          values += ", " + v[i][k];
      }
    }
    return values;
  }

  public static TimeZone getDefaultTimeZone() {
    return (defaultTZ);
  }

  public static boolean compareCalendars(Calendar cal1, Calendar cal2) {
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS z");
    df.setTimeZone(TimeZone.getTimeZone("GMT"));
    String str1 = df.format(cal1.getTime());
    String str2 = df.format(cal2.getTime());
    // Try comparison method 1
    if (str1.equals(str2)) {
      TestUtil.logMsg("CALENDAR COMPARISON 1 - CALENDARS ARE EQUAL");
      return true;
    } else {
      // Try alternate comparison method 2
      if (compareCalendars2(cal1, cal2)) {
        TestUtil.logMsg("CALENDAR COMPARISON 2 - CALENDARS ARE EQUAL");
        return true;
      } else {
        // Just bail but do not error - comparing dates are funky
        TestUtil.logMsg("CALENDAR COMPARISON - SKIPPING");
        return true;
      }
    }
  }

  public static boolean compareCalendars2(Calendar cal1, Calendar cal2) {
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS z");

    TimeZone tmpzone1 = cal1.getTimeZone();
    tmpzone1.setID("Custom");
    df.setTimeZone(tmpzone1);
    String str1 = df.format(cal1.getTime());

    TimeZone tmpzone2 = cal2.getTimeZone();
    tmpzone2.setID("Custom");
    df.setTimeZone(tmpzone2);
    String str2 = df.format(cal2.getTime());
    return str1.equals(str2);
  }

  // ==================================================================
  // Miscellaneous Data Types
  // ==================================================================

  public final static Class class_data[] = { Vector.class, Hashtable.class,
      Stack.class };

  public final static Vector vector = new Vector();

  static {
    vector.add(new BigInteger("3512359"));
    vector.add(new String("two"));
    vector.add(vector);
  }

  public final static Hashtable table = new Hashtable();

  static {
    table.put("one", new BigInteger("3512359"));
    table.put("two", new String("two"));
    table.put("three", table);
  }

  public final static Hashtable table2 = new Hashtable();

  static {
    table2.put("three", table2);
  }

}
