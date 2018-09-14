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

public final class Data {
  public final static int MAXOBJETS = 10;

  public final static boolean boolean_data[] = { false, true };

  public final static byte byte_data[] = { -128, -127, -126, -125, -65, -64,
      -63, -33, -32, -31, -17, -16, -15, -9, -8, -7, -6, -5, -4, -3, -2, -1, 0,
      1, 2, 3, 4, 5, 6, 7, 8, 9, 15, 16, 17, 31, 32, 33, 63, 64, 65, 125, 126,
      127 };

  public final static char char_data[] = { 'A', 'B', 'C', 'D', 'E', 'F', 'G',
      'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V',
      'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k',
      'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
      '~', '!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '_', '+', '{', '}',
      '|', ':', '"', '<', '>', '?', '`', '1', '2', '3', '4', '5', '6', '7', '8',
      '9', '0', '-', '=', '[', ']', '\\', ';', '\'', ',', '.', '/' };

  public final static short short_data[] = { -32768, -32767, -16384, -16383,
      -8192, -8191, -4096, -4095, -2048, -2047, -1024, -1023, -512, -513, -256,
      -255, -128, -127, -64, -63, -32, -31, -16, -15, -8, -7, -4, -3, -2, -1, 0,
      1, 2, 3, 4, 7, 8, 15, 16, 31, 32, 63, 64, 127, 128, 255, 256, 511, 512,
      1023, 1024, 2047, 2048, 4095, 4096, 8191, 8192, 16383, 16384, 32767 };

  public final static int int_data[] = { -2147483648, -2147483647, -1073741824,
      -1073741823, -536870912, -536870911, -268435456, -268435455, -134217728,
      -134217727, -67108864, -67108863, -33554432, -33554431, -16777216,
      -16777215, -8388608, -8388607, -4194304, -4194303, -2001152, -2097151,
      -1048576, -1048575, -524288, -524287, -262144, -262143, -131072, -131071,
      -65536, -65535, -32768, -32767, -16384, -16383, -8192, -8191, -4096,
      -4095, -2048, -2047, -1024, -1023, -512, -513, -256, -255, -128, -127,
      -64, -63, -32, -31, -16, -15, -8, -7, -4, -3, -2, -1, 0, 1, 2, 3, 4, 7, 8,
      15, 16, 31, 32, 63, 64, 127, 128, 255, 256, 511, 512, 1023, 1024, 2047,
      2048, 4095, 4096, 8191, 8192, 16383, 16384, 32767, 32768, 65535, 65536,
      131071, 131072, 262143, 262144, 524287, 524288, 1048575, 1048576, 2097151,
      2097152, 4194303, 4194304, 8388607, 8388608, 16777215, 16777216, 33554431,
      33554432, 67108863, 67108864, 134217727, 134217728, 268435455, 268435456,
      536870911, 536870912, 1073741823, 1073741824, 2147483646, 2147483647 };

  public static final long long_data[] = { (long) -9223372036854775808L,
      (long) -9223372036854775807L, (long) -4611686018427387904L,
      (long) -4611686018427387903L, (long) -2305843009213693952L,
      (long) -2305843009213693951L, (long) -1152921504606846976L,
      (long) -1152921504606846975L, (long) -576460752303423488L,
      (long) -576460752303423487L, (long) -288230376151711744L,
      (long) -288230376151711743L, (long) -144115188075855872L,
      (long) -144115188075855871L, (long) -72057594037927936L,
      (long) -72057594037927935L, (long) -36028797018963968L,
      (long) -36028797018963967L, (long) -18014398509481984L,
      (long) -18014398509481983L, (long) -9007199254740992L,
      (long) -9007199254740991L, (long) -4503599627370496L,
      (long) -4503599627370495L, (long) -2251799813685248L,
      (long) -2251799813685247L, (long) -1125899906842624L,
      (long) -1125899906842623L, (long) -562949953421312L,
      (long) -562949953421311L, (long) -281474976710656L,
      (long) -281474976710655L, (long) -140737488355328L,
      (long) -140737488355327L, (long) -70368744177664L,
      (long) -70368744177663L, (long) -35184372088832L, (long) -35184372088831L,
      (long) -17592186044416L, (long) -17592186044415L, (long) -8796093022208L,
      (long) -8796093022207L, (long) -4398046511104L, (long) -4398046511103L,
      (long) -2199023255552L, (long) -2199023255551L, (long) -2001511627776L,
      (long) -1099511627775L, (long) -549755813888L, (long) -549755813887L,
      (long) -274877906944L, (long) -274877906943L, (long) -137438953472L,
      (long) -137438953471L, (long) -68719476736L, (long) -68719476735L,
      (long) -34359738368L, (long) -34359738367L, (long) -17179869184L,
      (long) -17179869183L, (long) -8589934592L, (long) -8589934591L,
      (long) -2147483648L, (long) -2147483647L, (long) -1073741824L,
      (long) -1073741823L, (long) -536870912L, (long) -536870911L,
      (long) -268435456L, (long) -268435455L, (long) -134217728L,
      (long) -134217727L, (long) -67108864L, (long) -67108863L,
      (long) -33554432L, (long) -33554431L, (long) -16777216L,
      (long) -16777215L, (long) -8388608L, (long) -8388607L, (long) -4194304L,
      (long) -4194303L, (long) -2001152L, (long) -2097151L, (long) -1048576L,
      (long) -1048575L, (long) -524288L, (long) -524287L, (long) -262144L,
      (long) -262143L, (long) -131072L, (long) -131071L, (long) -65536L,
      (long) -65535L, (long) -32768L, (long) -32767L, (long) -16384L,
      (long) -16383L, (long) -8192L, (long) -8191L, (long) -4096L,
      (long) -4095L, (long) -2048L, (long) -2047L, (long) -1024L, (long) -1023L,
      (long) -512L, (long) -511L, (long) -256L, (long) -255L, (long) -128L,
      (long) -127L, (long) -64L, (long) -63L, (long) -32L, (long) -31L,
      (long) -16L, (long) -15L, (long) -8L, (long) -7L, (long) -4L, (long) -3L,
      (long) -2L, (long) -1L, (long) 0L, (long) 1L, (long) 2L, (long) 3L,
      (long) 4L, (long) 7L, (long) 8L, (long) 15L, (long) 16L, (long) 31L,
      (long) 32L, (long) 63L, (long) 64L, (long) 127L, (long) 128L, (long) 255L,
      (long) 256L, (long) 511L, (long) 512L, (long) 1023L, (long) 1024L,
      (long) 2047L, (long) 2048L, (long) 4095L, (long) 4096L, (long) 8191L,
      (long) 8192L, (long) 16383L, (long) 16384L, (long) 32767L, (long) 32768L,
      (long) 65535L, (long) 65536L, (long) 131071L, (long) 131072L,
      (long) 262143L, (long) 262144L, (long) 524287L, (long) 524288L,
      (long) 1048575L, (long) 1048576L, (long) 2097151L, (long) 2097152L,
      (long) 4194303L, (long) 4194304L, (long) 8388607L, (long) 8388608L,
      (long) 16777215L, (long) 16777216L, (long) 33554431L, (long) 33554432L,
      (long) 67108863L, (long) 67108864L, (long) 134217727L, (long) 134217728L,
      (long) 268435455L, (long) 268435456L, (long) 536870911L,
      (long) 536870912L, (long) 1073741823L, (long) 1073741824L,
      (long) 2147483647L, (long) 2147483648L, (long) 4294967295L,
      (long) 4294967296L, (long) 8589934591L, (long) 8589934592L,
      (long) 17179869183L, (long) 17179869184L, (long) 34359738367L,
      (long) 34359738368L, (long) 68719476735L, (long) 68719476736L,
      (long) 137438953471L, (long) 137438953472L, (long) 274877906943L,
      (long) 274877906944L, (long) 549755813887L, (long) 549755813888L,
      (long) 1099511627775L, (long) 1099511627776L, (long) 2199023255551L,
      (long) 2199023255552L, (long) 4398046511103L, (long) 4398046511104L,
      (long) 8796093022207L, (long) 8796093022208L, (long) 17592186044415L,
      (long) 17592186044416L, (long) 35184372088831L, (long) 35184372088832L,
      (long) 70368744177663L, (long) 70368744177664L, (long) 140737488355327L,
      (long) 140737488355328L, (long) 281474976710655L, (long) 281474976710656L,
      (long) 562949953421311L, (long) 562949953421312L,
      (long) 1125899906842623L, (long) 1125899906842624L,
      (long) 2251799813685247L, (long) 2251799813685248L,
      (long) 4503599627370495L, (long) 4503599627370496L,
      (long) 9007199254740991L, (long) 9007199254740992L,
      (long) 18014398509481983L, (long) 18014398509481984L,
      (long) 36028797018963967L, (long) 36028797018963968L,
      (long) 72057594037927935L, (long) 72057594037927936L,
      (long) 144115188075855871L, (long) 144115188075855872L,
      (long) 288230376151711743L, (long) 288230376151711744L,
      (long) 576460752303423487L, (long) 576460752303423488L,
      (long) 1152921504606846975L, (long) 1152921504606846976L,
      (long) 2305843009213693951L, (long) 2305843009213693952L,
      (long) 4611686018427387903L, (long) 4611686018427387904L,
      (long) 9223372036854775806L, (long) 9223372036854775807L };

  public final static float float_data[] = { (float) -1.40239846E-45,
      (float) 1.40239846E-45, (float) -3.40282347E+38, (float) 3.40282347E+38 };

  public final static double double_data[] = {
      (double) -4.94065645841246544E-324, (double) 4.94065645841246544E-324,
      (double) -1.79769313486231570E+308, (double) 1.79769313486231570E+308 };

  public final static String string_data[] = { "String1", "String2", "String3",
      "String4" };

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
    // table.put("three", table);
  }

  public final static Hashtable table2 = new Hashtable();

  static {
    table2.put("three", table2);
  }

  public final static Date date = new Date(12345678);

  public final static BitSet bitset = new BitSet(64);

  public final static Graph graph = new Graph("This",
      new Graph("is", new Graph("a",
          new Graph("graph", new Graph("object", new Graph("test.", null))))));

  public final static String graph_expected_string = "{ This is a graph object test. }";

  public static Date date_array[] = new Date[Data.MAXOBJETS];

  public static BitSet bitset_array[] = new BitSet[Data.MAXOBJETS];

  public static Graph graph_array[] = new Graph[Data.MAXOBJETS];

  public static int bear1Size = 10;

  public static int bear2Size = 20;

  public static int bear3Size = 30;

  public static short idlType1_value = 22;

  public static IDLStruct idlType1 = new IDLStruct(Data.idlType1_value);

  public static IDLStruct[] idlType2 = new IDLStruct[Data.MAXOBJETS];

  public static Multi multiTypes = new Multi();

  static {
    bitset.set(10);
    bitset.set(20);
    bitset.set(30);
    bitset.set(40);
    bitset.set(50);
    bitset.set(60);
    for (int i = 0; i < Data.MAXOBJETS; i++) {
      bitset_array[i] = new BitSet(64);
      bitset_array[i].set(i + 10);
      bitset_array[i].set(i + 20);
      bitset_array[i].set(1 + 30);
      date_array[i] = new Date(i * 1000);
      graph_array[i] = graph;
      idlType2[i] = new IDLStruct((short) i);
    }
  }
}
