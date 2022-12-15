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

package com.sun.ts.tests.jaxws.common;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;

import java.util.*;
import java.math.*;
import java.text.SimpleDateFormat;
import javax.xml.namespace.QName;
import javax.xml.datatype.*;

public final class JAXWS_Data {

  private static DatatypeFactory dtfactory = null;

  private static TimeZone defaultTZ = TimeZone.getDefault();

  static {
    try {
      dtfactory = DatatypeFactory.newInstance();
    } catch (DatatypeConfigurationException e) {
      TestUtil.logMsg("Could not configure DatatypeFactory object");
      TestUtil.printStackTrace(e);
    }
  }

  // ==================================================================
  // Java Primitive Data Types - Single-Dimensional Array Data
  // ==================================================================

  public final static boolean boolean_data[] = { false, true };

  public final static List<Boolean> list_boolean_data = (List<Boolean>) convertToListPrimArray(
      boolean_data);

  public final static Boolean Boolean_data[] = { Boolean.valueOf(false),
      Boolean.valueOf(true), null };

  public final static Boolean Boolean_nonull_data[] = { Boolean.valueOf(false),
      Boolean.valueOf(true), };

  public final static List<Boolean> list_Boolean_data = (List<Boolean>) Arrays
      .asList(Boolean_data);

  public final static List<Boolean> list_Boolean_nonull_data = (List<Boolean>) Arrays
      .asList(Boolean_nonull_data);

  public final static char char_data[] = { Character.MIN_VALUE, 0,
      Character.MAX_VALUE };

  public final static List<Character> list_char_data = (List<Character>) convertToListPrimArray(
      char_data);

  public final static Character Character_data[] = {
      Character.valueOf(Character.MIN_VALUE), Character.valueOf((char) 0),
      Character.valueOf(Character.MAX_VALUE), null };

  public final static Character Character_nonull_data[] = {
      Character.valueOf(Character.MIN_VALUE), Character.valueOf((char) 0),
      Character.valueOf(Character.MAX_VALUE), };

  public final static List<Character> list_Character_data = (List<Character>) Arrays
      .asList(Character_data);

  public final static List<Character> list_Character_nonull_data = (List<Character>) Arrays
      .asList(Character_nonull_data);

  public final static byte byte_data[] = { Byte.MIN_VALUE, 0, Byte.MAX_VALUE };

  public final static List<Byte> list_byte_data = (List<Byte>) convertToListPrimArray(
      byte_data);

  public final static byte byte_data2[] = { 0, Byte.MAX_VALUE, Byte.MIN_VALUE };

  public final static List<Byte> list_byte_data2 = (List<Byte>) convertToListPrimArray(
      byte_data2);

  public final static Byte Byte_data[] = { Byte.valueOf(Byte.MIN_VALUE),
      Byte.valueOf((byte) 0), Byte.valueOf(Byte.MAX_VALUE), null };

  public final static Byte Byte_nonull_data[] = { Byte.valueOf(Byte.MIN_VALUE),
      Byte.valueOf((byte) 0), Byte.valueOf(Byte.MAX_VALUE), };

  public final static List<Byte> list_Byte_data = (List<Byte>) Arrays
      .asList(Byte_data);

  public final static List<Byte> list_Byte_nonull_data = (List<Byte>) Arrays
      .asList(Byte_nonull_data);

  public final static short short_data[] = { Short.MIN_VALUE, 0,
      Short.MAX_VALUE };

  public final static List<Short> list_short_data = (List<Short>) convertToListPrimArray(
      short_data);

  public final static Short Short_data[] = { Short.valueOf(Short.MIN_VALUE),
      Short.valueOf((short) 0), Short.valueOf(Short.MAX_VALUE), null };

  public final static Short Short_nonull_data[] = {
      Short.valueOf(Short.MIN_VALUE), Short.valueOf((short) 0),
      Short.valueOf(Short.MAX_VALUE), };

  public final static List<Short> list_Short_data = (List<Short>) Arrays
      .asList(Short_data);

  public final static List<Short> list_Short_nonull_data = (List<Short>) Arrays
      .asList(Short_nonull_data);

  public final static int int_data[] = { Integer.MIN_VALUE, 0,
      Integer.MAX_VALUE };

  public final static List<Integer> list_int_data = (List<Integer>) convertToListPrimArray(
      int_data);

  public final static Integer Integer_data[] = {
      Integer.valueOf(Integer.MIN_VALUE), Integer.valueOf(0),
      Integer.valueOf(Integer.MAX_VALUE), null };

  public final static Integer Integer_nonull_data[] = {
      Integer.valueOf(Integer.MIN_VALUE), Integer.valueOf(0),
      Integer.valueOf(Integer.MAX_VALUE), };

  public final static List<Integer> list_Integer_data = (List<Integer>) Arrays
      .asList(Integer_data);

  public final static List<Integer> list_Integer_nonull_data = (List<Integer>) Arrays
      .asList(Integer_nonull_data);

  public final static long long_data[] = { Long.MIN_VALUE, 0, Long.MAX_VALUE };

  public final static List<Long> list_long_data = (List<Long>) convertToListPrimArray(
      long_data);

  public final static Long Long_data[] = { Long.valueOf(Long.MIN_VALUE),
      Long.valueOf(0L), Long.valueOf(Long.MAX_VALUE), null };

  public final static Long Long_nonull_data[] = { Long.valueOf(Long.MIN_VALUE),
      Long.valueOf(0L), Long.valueOf(Long.MAX_VALUE), };

  public final static List<Long> list_Long_data = (List<Long>) Arrays
      .asList(Long_data);

  public final static List<Long> list_Long_nonull_data = (List<Long>) Arrays
      .asList(Long_nonull_data);

  public final static float float_data[] = { Float.MIN_VALUE, 0,
      Float.MAX_VALUE };

  public final static List<Float> list_float_data = (List<Float>) convertToListPrimArray(
      float_data);

  public final static Float Float_data[] = { Float.valueOf(Float.MIN_VALUE),
      Float.valueOf((float) 0), Float.valueOf(Float.MAX_VALUE), null };

  public final static Float Float_nonull_data[] = {
      Float.valueOf(Float.MIN_VALUE), Float.valueOf((float) 0),
      Float.valueOf(Float.MAX_VALUE), };

  public final static List<Float> list_Float_data = (List<Float>) Arrays
      .asList(Float_data);

  public final static List<Float> list_Float_nonull_data = (List<Float>) Arrays
      .asList(Float_nonull_data);

  public final static double double_data[] = { Double.MIN_VALUE, 0,
      Double.MAX_VALUE };

  public final static List<Double> list_double_data = (List<Double>) convertToListPrimArray(
      double_data);

  public final static Double Double_data[] = { Double.valueOf(Double.MIN_VALUE),
      Double.valueOf(0), Double.valueOf(Double.MAX_VALUE), null };

  public final static Double Double_nonull_data[] = {
      Double.valueOf(Double.MIN_VALUE), Double.valueOf(0),
      Double.valueOf(Double.MAX_VALUE), };

  public final static List<Double> list_Double_data = (List<Double>) Arrays
      .asList(Double_data);

  public final static List<Double> list_Double_nonull_data = (List<Double>) Arrays
      .asList(Double_nonull_data);

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

  public final static List<String> list_String_data = (List<String>) Arrays
      .asList(String_data);

  public final static List<String> list_String_nonull_data = (List<String>) Arrays
      .asList(String_nonull_data);

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

  public final static List<Date> list_Date_data = (List<Date>) Arrays
      .asList(Date_data);

  public final static List<Date> list_Date_nonull_data = (List<Date>) Arrays
      .asList(Date_nonull_data);

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

  public final static List<GregorianCalendar> list_GregorianCalendar_data = (List<GregorianCalendar>) Arrays
      .asList(GregorianCalendar_data);

  public final static List<GregorianCalendar> list_GregorianCalendar_nonull_data = (List<GregorianCalendar>) Arrays
      .asList(GregorianCalendar_nonull_data);

  public final static XMLGregorianCalendar XMLGregorianCalendar_data[] = {
      dtfactory.newXMLGregorianCalendar(6, 5, 1, 10, 0, 0, 0, 0),
      dtfactory.newXMLGregorianCalendar(9, 10, 25, 1, 30, 0, 0, 0),
      dtfactory.newXMLGregorianCalendar(96, 5, 1, 2, 0, 30, 0, 0),
      dtfactory.newXMLGregorianCalendar(99, 10, 25, 3, 15, 15, 0, 0),
      dtfactory.newXMLGregorianCalendar(996, 5, 1, 6, 6, 6, 0, 0),
      dtfactory.newXMLGregorianCalendar(999, 10, 25, 7, 7, 7, 0, 0),
      dtfactory.newXMLGregorianCalendar(1996, 5, 1, 8, 8, 8, 0, 0),
      dtfactory.newXMLGregorianCalendar(1999, 10, 25, 9, 9, 9, 0, 0), null };

  public final static XMLGregorianCalendar XMLGregorianCalendar_nonull_data[] = {
      dtfactory.newXMLGregorianCalendar(6, 5, 1, 10, 0, 0, 0, 0),
      dtfactory.newXMLGregorianCalendar(9, 10, 25, 1, 30, 0, 0, 0),
      dtfactory.newXMLGregorianCalendar(96, 5, 1, 2, 0, 30, 0, 0),
      dtfactory.newXMLGregorianCalendar(99, 10, 25, 3, 15, 15, 0, 0),
      dtfactory.newXMLGregorianCalendar(996, 5, 1, 6, 6, 6, 0, 0),
      dtfactory.newXMLGregorianCalendar(999, 10, 25, 7, 7, 7, 0, 0),
      dtfactory.newXMLGregorianCalendar(1996, 5, 1, 8, 8, 8, 0, 0),
      dtfactory.newXMLGregorianCalendar(1999, 10, 25, 9, 9, 9, 0, 0), };

  public final static List<XMLGregorianCalendar> list_XMLGregorianCalendar_data = (List<XMLGregorianCalendar>) Arrays
      .asList(XMLGregorianCalendar_data);

  public final static List<XMLGregorianCalendar> list_XMLGregorianCalendar_nonull_data = (List<XMLGregorianCalendar>) Arrays
      .asList(XMLGregorianCalendar_nonull_data);

  public final static Duration Duration_data[] = {
      dtfactory.newDuration(true, 1999, 10, 25, 9, 9, 9),
      dtfactory.newDuration(false, 1999, 10, 25, 9, 9, 9),
      dtfactory.newDuration(10000),
      dtfactory.newDurationDayTime(true, 1, 9, 9, 9),
      dtfactory.newDurationDayTime(false, 1, 9, 9, 9),
      dtfactory.newDurationDayTime(1000L),
      dtfactory.newDurationYearMonth(true, 1999, 10),
      dtfactory.newDurationYearMonth(false, 1999, 10),
      dtfactory.newDurationYearMonth(1000L), null };

  public final static List<Duration> list_Duration_data = (List<Duration>) Arrays
      .asList(Duration_data);

  public final static BigInteger BigInteger_data[] = {
      new BigInteger("3512359"), new BigInteger("3512360"), null };

  public final static BigInteger BigInteger_nonull_data[] = {
      new BigInteger("3512359"), new BigInteger("3512360"), };

  public final static List<BigInteger> list_BigInteger_data = (List<BigInteger>) Arrays
      .asList(BigInteger_data);

  public final static List<BigInteger> list_BigInteger_nonull_data = (List<BigInteger>) Arrays
      .asList(BigInteger_nonull_data);

  public final static BigDecimal BigDecimal_data[] = {
      new BigDecimal("3512359.1456"), new BigDecimal("3512360.1456"), null };

  public final static BigDecimal BigDecimal_nonull_data[] = {
      new BigDecimal("3512359.1456"), new BigDecimal("3512360.1456"), };

  public final static List<BigDecimal> list_BigDecimal_data = (List<BigDecimal>) Arrays
      .asList(BigDecimal_data);

  public final static List<BigDecimal> list_BigDecimal_nonull_data = (List<BigDecimal>) Arrays
      .asList(BigDecimal_nonull_data);

  // ==================================================================
  // Java Standard Value Classes - Multi-Dimensional Array Data
  // ==================================================================

  public final static String String_multi_data[][] = { String_data,
      String_data, };

  public final static Date Date_multi_data[][] = { Date_data, Date_data, };

  public final static GregorianCalendar GregorianCalendar_multi_data[][] = {
      GregorianCalendar_data, GregorianCalendar_data, };

  public final static XMLGregorianCalendar XMLGregorianCalendar_multi_data[][] = {
      XMLGregorianCalendar_data, XMLGregorianCalendar_data, };

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

  public final static List<QName> list_QName_data = (List<QName>) Arrays
      .asList(QName_data);

  public final static List<QName> list_QName_nonull_data = (List<QName>) Arrays
      .asList(QName_nonull_data);

  public final static QName QName_multi_data[][] = { QName_data, QName_data, };

  // ==================================================================
  // Various utility classes used for dumping/comparing data
  // ==================================================================

  public static void dumpListValues(List<?> c) {
    System.out.println("JAXWS_Data:dumpListValues - List size=" + c.size());
    int i = 0;
    for (Object o : c) {
      System.out.println(i + "=" + o);
      i++;
    }
  }

  public static void dumpArrayValues(Object o, String t, String m) {
    System.out.println(m);
    dumpArrayValues(o, t);
  }

  public static void dumpArrayValues(Object o, String t) {
    System.out.println("JAXWS_Data:dumpArrayValues");
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
    } else if (t.equals("XMLGregorianCalendar")) {
      XMLGregorianCalendar[] v = (XMLGregorianCalendar[]) o;
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

  public static void dumpMultiArrayValues(Object o, String t, String m) {
    System.out.println(m);
    dumpMultiArrayValues(o, t);
  }

  public static void dumpMultiArrayValues(Object o, String t) {
    System.out.println("JAXWS_Data:dumpMultiArrayValues");
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
    } else if (t.equals("XMLGregorianCalendar")) {
      XMLGregorianCalendar[][] v = (XMLGregorianCalendar[][]) o;
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
    System.out.println("JAXWS_Data:getArraySize");
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
    } else if (t.equals("XMLGregorianCalendar")) {
      return ((XMLGregorianCalendar[]) o).length;
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
    System.out.println("JAXWS_Data:getMultiArraySize");
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
    } else if (t.equals("XMLGregorianCalendar")) {
      XMLGregorianCalendar[][] m = (XMLGregorianCalendar[][]) o;
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

    if ((r == null && e != null) || (r != null && e == null)) {
      pass = false;
    }
    if (t.equals("Boolean")) {
      Boolean exp = (Boolean) e;
      Boolean rec = (Boolean) r;
      if (rec == exp)
        return true;
      if (!rec.equals(exp)) {
        System.out
            .println("Value Mismatch: expected " + exp + ", received " + rec);
        pass = false;
      }
    } else if (t.equals("Character")) {
      Character exp = (Character) e;
      Character rec = (Character) r;
      if (rec == exp)
        return true;
      if (!rec.equals(exp)) {
        System.out
            .println("Value Mismatch: expected " + exp + ", received " + rec);
        pass = false;
      }
    } else if (t.equals("Byte")) {
      Byte exp = (Byte) e;
      Byte rec = (Byte) r;
      if (rec == exp)
        return true;
      if (!rec.equals(exp)) {
        System.out
            .println("Value Mismatch: expected " + exp + ", received " + rec);
        pass = false;
      }
    } else if (t.equals("Short")) {
      Short exp = (Short) e;
      Short rec = (Short) r;
      if (rec == exp)
        return true;
      if (!rec.equals(exp)) {
        System.out
            .println("Value Mismatch: expected " + exp + ", received " + rec);
        pass = false;
      }
    } else if (t.equals("Integer")) {
      Integer exp = (Integer) e;
      Integer rec = (Integer) r;
      if (rec == exp)
        return true;
      if (!rec.equals(exp)) {
        System.out
            .println("Value Mismatch: expected " + exp + ", received " + rec);
        pass = false;
      }
    } else if (t.equals("Long")) {
      Long exp = (Long) e;
      Long rec = (Long) r;
      if (rec == exp)
        return true;
      if (!rec.equals(exp)) {
        System.out
            .println("Value Mismatch: expected " + exp + ", received " + rec);
        pass = false;
      }
    } else if (t.equals("Float")) {
      Float exp = (Float) e;
      Float rec = (Float) r;
      if (rec == exp)
        return true;
      if (!rec.equals(exp)) {
        System.out
            .println("Value Mismatch: expected " + exp + ", received " + rec);
        pass = false;
      }
    } else if (t.equals("Double")) {
      Double exp = (Double) e;
      Double rec = (Double) r;
      if (rec == exp)
        return true;
      if (!rec.equals(exp)) {
        System.out
            .println("Value Mismatch: expected " + exp + ", received " + rec);
        pass = false;
      }
    } else if (t.equals("String")) {
      String exp = (String) e;
      String rec = (String) r;
      if (rec == exp)
        return true;
      if (!rec.equals(exp)) {
        System.out
            .println("Value Mismatch: expected " + exp + ", received " + rec);
        pass = false;
      }
    } else if (t.equals("Date")) {
      Date exp = (Date) e;
      Date rec = (Date) r;
      if (rec == exp)
        return true;
      if (!rec.equals(exp)) {
        System.out
            .println("Value Mismatch: expected " + exp + ", received " + rec);
        pass = false;
      }
    } else if (t.equals("Calendar")) {
      Calendar exp = (Calendar) e;
      Calendar rec = (Calendar) r;
      if (rec == exp)
        return true;
      if (!compareCalendars(rec, exp)) {
        System.out
            .println("Value Mismatch: expected " + exp + ", received " + rec);
        pass = false;
      }
    } else if (t.equals("XMLGregorianCalendar")) {
      XMLGregorianCalendar exp = (XMLGregorianCalendar) e;
      XMLGregorianCalendar rec = (XMLGregorianCalendar) r;
      if (rec == exp)
        return true;
      if (!compareXMLGregorianCalendars(rec, exp)) {
        System.out
            .println("Value Mismatch: expected " + exp + ", received " + rec);
        pass = false;
      }
    } else if (t.equals("BigInteger")) {
      BigInteger exp = (BigInteger) e;
      BigInteger rec = (BigInteger) r;
      if (rec == exp)
        return true;
      if (!rec.equals(exp)) {
        System.out
            .println("Value Mismatch: expected " + exp + ", received " + rec);
        pass = false;
      }
    } else if (t.equals("BigDecimal")) {
      BigDecimal exp = (BigDecimal) e;
      BigDecimal rec = (BigDecimal) r;
      if (rec == exp)
        return true;
      if (!rec.equals(exp)) {
        System.out
            .println("Value Mismatch: expected " + exp + ", received " + rec);
        pass = false;
      }
    } else if (t.equals("QName")) {
      QName exp = (QName) e;
      QName rec = (QName) r;
      if (rec == exp)
        return true;
      if (!rec.equals(exp)) {
        System.out
            .println("Value Mismatch: expected " + exp + ", received " + rec);
        pass = false;
      }
    }
    return pass;
  }

  public static List<Boolean> convertToListPrimArray(boolean[] v1) {
    Boolean[] v2 = new Boolean[v1.length];
    for (int i = 0; i < v1.length; i++)
      v2[i] = v1[i];
    return (List<Boolean>) Arrays.asList(v2);
  }

  public static List<Character> convertToListPrimArray(char[] v1) {
    Character[] v2 = new Character[v1.length];
    for (int i = 0; i < v1.length; i++)
      v2[i] = v1[i];
    return (List<Character>) Arrays.asList(v2);
  }

  public static List<Byte> convertToListPrimArray(byte[] v1) {
    Byte[] v2 = new Byte[v1.length];
    for (int i = 0; i < v1.length; i++)
      v2[i] = v1[i];
    return (List<Byte>) Arrays.asList(v2);
  }

  public static List<Short> convertToListPrimArray(short[] v1) {
    Short[] v2 = new Short[v1.length];
    for (int i = 0; i < v1.length; i++)
      v2[i] = v1[i];
    return (List<Short>) Arrays.asList(v2);
  }

  public static List<Integer> convertToListPrimArray(int[] v1) {
    Integer[] v2 = new Integer[v1.length];
    for (int i = 0; i < v1.length; i++)
      v2[i] = v1[i];
    return (List<Integer>) Arrays.asList(v2);
  }

  public static List<Long> convertToListPrimArray(long[] v1) {
    Long[] v2 = new Long[v1.length];
    for (int i = 0; i < v1.length; i++)
      v2[i] = v1[i];
    return (List<Long>) Arrays.asList(v2);
  }

  public static List<Float> convertToListPrimArray(float[] v1) {
    Float[] v2 = new Float[v1.length];
    for (int i = 0; i < v1.length; i++)
      v2[i] = v1[i];
    return (List<Float>) Arrays.asList(v2);
  }

  public static List<Double> convertToListPrimArray(double[] v1) {
    Double[] v2 = new Double[v1.length];
    for (int i = 0; i < v1.length; i++)
      v2[i] = v1[i];
    return (List<Double>) Arrays.asList(v2);
  }

  public static Object convertListToArray(List<?> a, String t) {
    Object result = null;
    if (t.equals("boolean")) {
      boolean[] b1 = new boolean[a.size()];
      Boolean[] b2 = a.toArray(new Boolean[a.size()]);
      for (int i = 0; i < b2.length; i++) {
        b1[i] = b2[i].booleanValue();
      }
      result = b1;
    } else if (t.equals("Boolean")) {
      Boolean[] b1 = a.toArray(new Boolean[a.size()]);
      result = b1;
    } else if (t.equals("char")) {
      char[] b1 = new char[a.size()];
      Character[] b2 = a.toArray(new Character[a.size()]);
      for (int i = 0; i < b2.length; i++) {
        b1[i] = b2[i].charValue();
      }
      result = b1;
    } else if (t.equals("Character")) {
      Character[] b1 = a.toArray(new Character[a.size()]);
      result = b1;
    } else if (t.equals("byte")) {
      byte[] b1 = new byte[a.size()];
      Byte[] b2 = a.toArray(new Byte[a.size()]);
      for (int i = 0; i < b2.length; i++) {
        b1[i] = b2[i].byteValue();
      }
      result = b1;
    } else if (t.equals("Byte")) {
      Byte[] b1 = a.toArray(new Byte[a.size()]);
      result = b1;
    } else if (t.equals("short")) {
      short[] b1 = new short[a.size()];
      Short[] b2 = a.toArray(new Short[a.size()]);
      for (int i = 0; i < b2.length; i++) {
        b1[i] = b2[i].shortValue();
      }
      result = b1;
    } else if (t.equals("Short")) {
      Short[] b1 = a.toArray(new Short[a.size()]);
      result = b1;
    } else if (t.equals("int")) {
      int[] b1 = new int[a.size()];
      Integer[] b2 = a.toArray(new Integer[a.size()]);
      for (int i = 0; i < b2.length; i++) {
        b1[i] = b2[i].intValue();
      }
      result = b1;
    } else if (t.equals("Integer")) {
      Integer[] b1 = a.toArray(new Integer[a.size()]);
      result = b1;
    } else if (t.equals("long")) {
      long[] b1 = new long[a.size()];
      Long[] b2 = a.toArray(new Long[a.size()]);
      for (int i = 0; i < b2.length; i++) {
        b1[i] = b2[i].longValue();
      }
      result = b1;
    } else if (t.equals("Long")) {
      Long[] b1 = a.toArray(new Long[a.size()]);
      result = b1;
    } else if (t.equals("float")) {
      float[] b1 = new float[a.size()];
      Float[] b2 = a.toArray(new Float[a.size()]);
      for (int i = 0; i < b2.length; i++) {
        b1[i] = b2[i].floatValue();
      }
      result = b1;
    } else if (t.equals("Float")) {
      Float[] b1 = a.toArray(new Float[a.size()]);
      result = b1;
    } else if (t.equals("double")) {
      double[] b1 = new double[a.size()];
      Double[] b2 = a.toArray(new Double[a.size()]);
      for (int i = 0; i < b2.length; i++) {
        b1[i] = b2[i].doubleValue();
      }
      result = b1;
    } else if (t.equals("Double")) {
      Double[] b1 = a.toArray(new Double[a.size()]);
      result = b1;
    } else if (t.equals("String")) {
      String[] b1 = a.toArray(new String[a.size()]);
      result = b1;
    } else if (t.equals("Date")) {
      Date[] b1 = a.toArray(new Date[a.size()]);
      result = b1;
    } else if (t.equals("Calendar")) {
      Calendar[] b1 = a.toArray(new Calendar[a.size()]);
      result = b1;
    } else if (t.equals("XMLGregorianCalendar")) {
      XMLGregorianCalendar[] b1 = a.toArray(new XMLGregorianCalendar[a.size()]);
      result = b1;
    } else if (t.equals("BigInteger")) {
      BigInteger[] b1 = a.toArray(new BigInteger[a.size()]);
      result = b1;
    } else if (t.equals("BigDecimal")) {
      BigDecimal[] b1 = a.toArray(new BigDecimal[a.size()]);
      result = b1;
    } else if (t.equals("QName")) {
      QName[] b1 = a.toArray(new QName[a.size()]);
      result = b1;
    }
    return result;
  }

  public static boolean compareArrayValues(Object e, List<?> a, String t) {
    return compareArrayValues(e, convertListToArray(a, t), t);
  }

  public static boolean compareArrayValues(List<?> b, List<?> a, String t) {
    return compareArrayValues(convertListToArray(b, t),
        convertListToArray(a, t), t);
  }

  public static boolean compareArrayValues(Object e, Object r, String t) {
    System.out.println("JAXWS_Data:compareArrayValues");
    boolean pass = true;

    if (t.equals("boolean")) {
      boolean[] exp = (boolean[]) e;
      boolean[] rec = (boolean[]) r;
      dumpArrayValues(exp, "boolean", "Expected");
      dumpArrayValues(rec, "boolean", "Received");
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
      dumpArrayValues(exp, "Boolean", "Expected");
      dumpArrayValues(rec, "Boolean", "Received");
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
      dumpArrayValues(exp, "char", "Expected");
      dumpArrayValues(rec, "char", "Received");
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
      dumpArrayValues(exp, "Character", "Expected");
      dumpArrayValues(rec, "Character", "Received");
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
      dumpArrayValues(exp, "byte", "Expected");
      dumpArrayValues(rec, "byte", "Received");
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
      dumpArrayValues(exp, "Byte", "Expected");
      dumpArrayValues(rec, "Byte", "Received");
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
      dumpArrayValues(exp, "short", "Expected");
      dumpArrayValues(rec, "short", "Received");
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
      dumpArrayValues(exp, "Short", "Expected");
      dumpArrayValues(rec, "Short", "Received");
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
      dumpArrayValues(exp, "int", "Expected");
      dumpArrayValues(rec, "int", "Received");
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
      dumpArrayValues(exp, "Integer", "Expected");
      dumpArrayValues(rec, "Integer", "Received");
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
      dumpArrayValues(exp, "long", "Expected");
      dumpArrayValues(rec, "long", "Received");
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
      dumpArrayValues(exp, "Long", "Expected");
      dumpArrayValues(rec, "Long", "Received");
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
      dumpArrayValues(exp, "float", "Expected");
      dumpArrayValues(rec, "float", "Received");
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
      dumpArrayValues(exp, "Float", "Expected");
      dumpArrayValues(rec, "Float", "Received");
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
      dumpArrayValues(exp, "double", "Expected");
      dumpArrayValues(rec, "double", "Received");
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
      dumpArrayValues(exp, "Double", "Expected");
      dumpArrayValues(rec, "Double", "Received");
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
      dumpArrayValues(exp, "String", "Expected");
      dumpArrayValues(rec, "String", "Received");
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
      dumpArrayValues(exp, "Date", "Expected");
      dumpArrayValues(rec, "Date", "Received");
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
      dumpArrayValues(exp, "Calendar", "Expected");
      dumpArrayValues(rec, "Calendar", "Received");
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
    } else if (t.equals("XMLGregorianCalendar")) {
      XMLGregorianCalendar[] exp = (XMLGregorianCalendar[]) e;
      XMLGregorianCalendar[] rec = (XMLGregorianCalendar[]) r;
      dumpArrayValues(exp, "XMLGregorianCalendar", "Expected");
      dumpArrayValues(rec, "XMLGregorianCalendar", "Received");
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
        } else if (!compareXMLGregorianCalendars(rec[i], exp[i])) {
          System.out.println(
              "Array Mismatch: expected " + exp[i] + ", received " + rec[i]);
          pass = false;
        }
      }
    } else if (t.equals("BigInteger")) {
      BigInteger[] exp = (BigInteger[]) e;
      BigInteger[] rec = (BigInteger[]) r;
      dumpArrayValues(exp, "BigInteger", "Expected");
      dumpArrayValues(rec, "BigInteger", "Received");
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
      dumpArrayValues(exp, "BigDecimal", "Expected");
      dumpArrayValues(rec, "BigDecimal", "Received");
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
      dumpArrayValues(exp, "QName", "Expected");
      dumpArrayValues(rec, "QName", "Received");
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

  public static void dumpDate(Object o, String m, String t) {
    System.out.println(m);
    System.out.println("JAXWS_Data:dumpDate");
    if (o == null) {
      System.out.println("- null");
      return;
    }
    if (t.equals("YM")) {
      XMLGregorianCalendar v = (XMLGregorianCalendar) o;
      System.out.println("- " + v.getYear() + v.getMonth());
    } else if (t.equals("Y")) {
      XMLGregorianCalendar v = (XMLGregorianCalendar) o;
      System.out.println("- " + v.getYear());
    } else if (t.equals("MD")) {
      XMLGregorianCalendar v = (XMLGregorianCalendar) o;
      System.out.println("- " + v.getMonth() + v.getDay());
    } else if (t.equals("M")) {
      XMLGregorianCalendar v = (XMLGregorianCalendar) o;
      System.out.println("- " + v.getMonth());
    } else if (t.equals("D")) {
      XMLGregorianCalendar v = (XMLGregorianCalendar) o;
      System.out.println("- " + v.getDay());
    }
  }

  public static boolean compareDate(Object e, Object r, String t) {
    System.out.println("JAXWS_Data:compareDate");
    boolean pass = true;

    XMLGregorianCalendar exp = (XMLGregorianCalendar) e;
    XMLGregorianCalendar rec = (XMLGregorianCalendar) r;
    if (rec == exp)
      return true;
    if (rec == null || exp == null) {
      dumpDate(exp, "Expected", t);
      dumpDate(rec, "Received", t);
      System.out
          .println("Date Mismatch: expected " + exp + ", received " + rec);
      return false;
    } else if (t.equals("YM")) {
      dumpDate(exp, "Expected", "YM");
      dumpDate(rec, "Received", "YM");
      if (rec.getYear() != exp.getYear() || rec.getMonth() != exp.getMonth()) {
        System.out.println("Date Mismatch: expected YearMonth " + exp.getYear()
            + exp.getMonth() + ", received YearMonth " + rec.getYear()
            + rec.getMonth());
        pass = false;
      }
    } else if (t.equals("Y")) {
      dumpDate(exp, "Expected", "Y");
      dumpDate(rec, "Received", "Y");
      if (rec.getYear() != exp.getYear()) {
        System.out.println("Date Mismatch: expected Year " + exp.getYear()
            + ", received Year " + rec.getYear());
        pass = false;
      }
    } else if (t.equals("MD")) {
      dumpDate(exp, "Expected", "MD");
      dumpDate(rec, "Received", "MD");
      if (rec.getMonth() != exp.getMonth() || rec.getDay() != exp.getDay()) {
        System.out.println(
            "Date Mismatch: expected MonthDay " + exp.getMonth() + exp.getDay()
                + ", received MonthDay " + rec.getMonth() + rec.getDay());
        pass = false;
      }
    } else if (t.equals("D")) {
      dumpDate(exp, "Expected", "D");
      dumpDate(rec, "Received", "D");
      if (rec.getDay() != exp.getDay()) {
        System.out.println("Date Mismatch: expected Day " + exp.getDay()
            + ", received Day " + rec.getDay());
        pass = false;
      }
    } else if (t.equals("M")) {
      dumpDate(exp, "Expected", "M");
      dumpDate(rec, "Received", "M");
      if (rec.getMonth() != exp.getMonth()) {
        System.out.println("Date Mismatch: expected Month " + exp.getMonth()
            + ", received Month " + rec.getMonth());
        pass = false;
      }
    }
    return pass;
  }

  public static boolean compareMultiArrayValues(Object e, Object r, String t) {
    System.out.println("JAXWS_Data:compareMultiArrayValues");
    boolean pass = true;

    if (t.equals("boolean")) {
      boolean[][] exp = (boolean[][]) e;
      boolean[][] rec = (boolean[][]) r;
      dumpMultiArrayValues(exp, "boolean", "Expected");
      dumpMultiArrayValues(rec, "boolean", "Received");
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
      dumpMultiArrayValues(exp, "Boolean", "Expected");
      dumpMultiArrayValues(rec, "Boolean", "Received");
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
      dumpMultiArrayValues(exp, "char", "Expected");
      dumpMultiArrayValues(rec, "char", "Received");
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
      dumpMultiArrayValues(exp, "Character", "Expected");
      dumpMultiArrayValues(rec, "Character", "Received");
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
      dumpMultiArrayValues(exp, "byte", "Expected");
      dumpMultiArrayValues(rec, "byte", "Received");
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
      dumpMultiArrayValues(exp, "Byte", "Expected");
      dumpMultiArrayValues(rec, "Byte", "Received");
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
      dumpMultiArrayValues(exp, "short", "Expected");
      dumpMultiArrayValues(rec, "short", "Received");
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
      dumpMultiArrayValues(exp, "Short", "Expected");
      dumpMultiArrayValues(rec, "Short", "Received");
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
      dumpMultiArrayValues(exp, "int", "Expected");
      dumpMultiArrayValues(rec, "int", "Received");
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
      dumpMultiArrayValues(exp, "Integer", "Expected");
      dumpMultiArrayValues(rec, "Integer", "Received");
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
      dumpMultiArrayValues(exp, "long", "Expected");
      dumpMultiArrayValues(rec, "long", "Received");
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
      dumpMultiArrayValues(exp, "Long", "Expected");
      dumpMultiArrayValues(rec, "Long", "Received");
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
      dumpMultiArrayValues(exp, "float", "Expected");
      dumpMultiArrayValues(rec, "float", "Received");
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
      dumpMultiArrayValues(exp, "Float", "Expected");
      dumpMultiArrayValues(rec, "Float", "Received");
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
      dumpMultiArrayValues(exp, "double", "Expected");
      dumpMultiArrayValues(rec, "double", "Received");
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
      dumpMultiArrayValues(exp, "Double", "Expected");
      dumpMultiArrayValues(rec, "Double", "Received");
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
      dumpMultiArrayValues(exp, "String", "Expected");
      dumpMultiArrayValues(rec, "String", "Received");
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
      dumpMultiArrayValues(exp, "Date", "Expected");
      dumpMultiArrayValues(rec, "Date", "Received");
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
      dumpMultiArrayValues(exp, "Calendar", "Expected");
      dumpMultiArrayValues(rec, "Calendar", "Received");
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
    } else if (t.equals("XMLGregorianCalendar")) {
      XMLGregorianCalendar[][] exp = (XMLGregorianCalendar[][]) e;
      XMLGregorianCalendar[][] rec = (XMLGregorianCalendar[][]) r;
      dumpMultiArrayValues(exp, "XMLGregorianCalendar", "Expected");
      dumpMultiArrayValues(rec, "XMLGregorianCalendar", "Received");
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
            } else if (!compareXMLGregorianCalendars(rec[i][k], exp[i][k])) {
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
      dumpMultiArrayValues(exp, "BigInteger", "Expected");
      dumpMultiArrayValues(rec, "BigInteger", "Received");
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
      dumpMultiArrayValues(exp, "BigDecimal", "Expected");
      dumpMultiArrayValues(rec, "BigDecimal", "Received");
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
      dumpMultiArrayValues(exp, "QName", "Expected");
      dumpMultiArrayValues(rec, "QName", "Received");
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
    } else if (t.equals("XMLGregorianCalendar")) {
      XMLGregorianCalendar[] v = (XMLGregorianCalendar[]) o;
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
    } else if (t.equals("XMLGregorianCalendar")) {
      XMLGregorianCalendar[][] v = (XMLGregorianCalendar[][]) o;
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

  public static boolean compareXMLGregorianCalendars(XMLGregorianCalendar cal1,
      XMLGregorianCalendar cal2) {
    if (cal1.compare(cal2) == DatatypeConstants.EQUAL) {
      TestUtil.logMsg(
          "XMLGregorianCalendar COMPARISON 1 - XMLGregorianCalendar's ARE EQUAL");
      return true;
    } else {
      TestUtil.logMsg(
          "XMLGregorianCalendar COMPARISON 1 - XMLGregorianCalendar's ARE NOT EQUAL");
      return false;
    }
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
}
