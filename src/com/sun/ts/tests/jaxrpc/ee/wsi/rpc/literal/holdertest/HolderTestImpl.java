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

package com.sun.ts.tests.jaxrpc.ee.wsi.rpc.literal.holdertest;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.tests.jaxrpc.common.*;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.AccessException;

import java.util.GregorianCalendar;
import java.util.Calendar;
import java.math.BigInteger;
import java.math.BigDecimal;
import javax.xml.namespace.QName;

import javax.xml.rpc.holders.*;
import com.sun.ts.tests.jaxrpc.ee.wsi.rpc.literal.holdertest.holders.*;

// Service Implementation Class - as outlined in JAX-RPC Specification

public class HolderTestImpl implements HolderTest {

  public void echoInOutSimpleTypes(javax.xml.rpc.holders.StringHolder varString,
      javax.xml.rpc.holders.BigIntegerHolder varInteger,
      javax.xml.rpc.holders.IntHolder varInt,
      javax.xml.rpc.holders.LongHolder varLong,
      javax.xml.rpc.holders.ShortHolder varShort,
      javax.xml.rpc.holders.BigDecimalHolder varDecimal,
      javax.xml.rpc.holders.FloatHolder varFloat,
      javax.xml.rpc.holders.DoubleHolder varDouble,
      javax.xml.rpc.holders.BooleanHolder varBoolean,
      javax.xml.rpc.holders.ByteHolder varByte,
      javax.xml.rpc.holders.QNameHolder varQName,
      javax.xml.rpc.holders.CalendarHolder varDateTime) throws RemoteException {

    String v1 = "String1";
    BigInteger v2 = new BigInteger("3512359");
    int v3 = (int) Integer.MIN_VALUE;
    long v4 = (long) Long.MIN_VALUE;
    short v5 = (short) Short.MIN_VALUE;
    BigDecimal v6 = new BigDecimal("3512359.1456");
    float v7 = (float) Float.MIN_VALUE;
    double v8 = (double) Double.MIN_VALUE;
    boolean v9 = false;
    byte v10 = (byte) Byte.MIN_VALUE;
    QName v11 = new QName("String2");
    Calendar v12 = (Calendar) new GregorianCalendar(96, 5, 1);
    boolean pass = true;
    String result = "";
    if (!v1.equals(varString.value)) {
      result = "compare of data failed - received " + varString.value
          + ",  expected: " + v1;
      pass = false;
    }
    if (!v2.equals(varInteger.value)) {
      result = result + "\ncompare of data failed - received "
          + varInteger.value + ",  expected: " + v2;
      pass = false;
    }
    if (v3 != varInt.value) {
      result = result + "\ncompare of data failed - received " + varInt.value
          + ",  expected: " + v3;
      pass = false;
    }
    if (v4 != varLong.value) {
      result = result + "\ncompare of data failed - received " + varLong.value
          + ",  expected: " + v4;
      pass = false;
    }
    if (v5 != varShort.value) {
      result = result + "\ncompare of data failed - received " + varShort.value
          + ",  expected: " + v5;
      pass = false;
    }
    if (!v6.equals(varDecimal.value)) {
      result = result + "\ncompare of data failed - received "
          + varDecimal.value + ",  expected: " + v6;
      pass = false;
    }
    if (v7 != varFloat.value) {
      result = result + "\ncompare of data failed - received " + varFloat.value
          + ",  expected: " + v7;
      pass = false;
    }
    if (v8 != varDouble.value) {
      result = result + "\ncompare of data failed - received " + varDouble.value
          + ",  expected: " + v8;
      pass = false;
    }
    if (v9 != varBoolean.value) {
      result = result + "\ncompare of data failed - received "
          + varBoolean.value + ",  expected: " + v9;
      pass = false;
    }
    if (v10 != varByte.value) {
      result = result + "\ncompare of data failed - received " + varByte.value
          + ",  expected: " + v10;
      pass = false;
    }
    if (!v11.equals(varQName.value)) {
      result = result + "\ncompare of data failed - received " + varQName.value
          + ",  expected: " + v11;
      pass = false;
    }
    if (!JAXRPC_Data.compareCalendars(v12, varDateTime.value)) {
      result = result + "\ncompare of data failed - received "
          + varDateTime.value + ",  expected: " + v12;
      pass = false;
    }
    if (!pass) {
      throw new RemoteException(result);
    }

    varString.value = new String("String4");
    varInteger.value = new BigInteger("3512360");
    varInt.value = Integer.MAX_VALUE;
    varLong.value = Long.MAX_VALUE;
    varShort.value = Short.MAX_VALUE;
    varDecimal.value = new BigDecimal("3512360.1456");
    varFloat.value = Float.MAX_VALUE;
    varDouble.value = Double.MAX_VALUE;
    varBoolean.value = true;
    varByte.value = Byte.MAX_VALUE;
    varQName.value = new QName("String5");
    varDateTime.value = new GregorianCalendar(96, 5, 2);
  }

  public void echoInOutSimpleTypesArray(
      com.sun.ts.tests.jaxrpc.ee.wsi.rpc.literal.holdertest.holders.ArrayOfstringHolder varString,
      com.sun.ts.tests.jaxrpc.ee.wsi.rpc.literal.holdertest.holders.ArrayOfintegerHolder varInteger,
      com.sun.ts.tests.jaxrpc.ee.wsi.rpc.literal.holdertest.holders.ArrayOfintHolder varInt,
      com.sun.ts.tests.jaxrpc.ee.wsi.rpc.literal.holdertest.holders.ArrayOflongHolder varLong,
      com.sun.ts.tests.jaxrpc.ee.wsi.rpc.literal.holdertest.holders.ArrayOfshortHolder varShort,
      com.sun.ts.tests.jaxrpc.ee.wsi.rpc.literal.holdertest.holders.ArrayOfdecimalHolder varDecimal,
      com.sun.ts.tests.jaxrpc.ee.wsi.rpc.literal.holdertest.holders.ArrayOffloatHolder varFloat,
      com.sun.ts.tests.jaxrpc.ee.wsi.rpc.literal.holdertest.holders.ArrayOfdoubleHolder varDouble,
      com.sun.ts.tests.jaxrpc.ee.wsi.rpc.literal.holdertest.holders.ArrayOfbooleanHolder varBoolean,
      com.sun.ts.tests.jaxrpc.ee.wsi.rpc.literal.holdertest.holders.ArrayOfbyteHolder varByte,
      com.sun.ts.tests.jaxrpc.ee.wsi.rpc.literal.holdertest.holders.ArrayOfQNameHolder varQName,
      com.sun.ts.tests.jaxrpc.ee.wsi.rpc.literal.holdertest.holders.ArrayOfdateTimeHolder varDateTime)
      throws java.rmi.RemoteException {
  }

  public void echoInOutBook(
      com.sun.ts.tests.jaxrpc.ee.wsi.rpc.literal.holdertest.holders.BookHolder varBook)
      throws java.rmi.RemoteException {
    Book b = varBook.value;
    b.setAuthor("author1");
    b.setTitle("title1");
    b.setIsbn(1);
    varBook.value = b;
  }

  public void echoInOutBookArray(
      com.sun.ts.tests.jaxrpc.ee.wsi.rpc.literal.holdertest.holders.ArrayOfBookHolder varBook)
      throws java.rmi.RemoteException {
    ArrayOfBook v = varBook.value;
    Book[] b = v.getArrayOfBook();
    if (b.length != 2)
      throw new RemoteException("array size not 2");
    String author = b[0].getAuthor();
    String title = b[0].getTitle();
    int isbn = b[0].getIsbn();
    b[0].setAuthor(b[1].getAuthor());
    b[0].setTitle(b[1].getTitle());
    b[0].setIsbn(b[1].getIsbn());
    b[1].setAuthor(author);
    b[1].setTitle(title);
    b[1].setIsbn(isbn);
    v.setArrayOfBook(b);
    varBook.value = v;
  }
}
