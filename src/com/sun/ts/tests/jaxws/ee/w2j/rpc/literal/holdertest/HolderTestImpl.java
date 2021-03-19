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

package com.sun.ts.tests.jaxws.ee.w2j.rpc.literal.holdertest;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.tests.jaxws.common.*;

import jakarta.xml.ws.WebServiceException;
import java.rmi.AccessException;

import java.util.Calendar;
import java.math.BigInteger;
import java.math.BigDecimal;
import javax.xml.namespace.QName;
import javax.xml.datatype.*;
import jakarta.xml.ws.Holder;

import com.sun.ts.tests.jaxws.ee.w2j.rpc.literal.holdertest.*;

// Service Implementation Class - as outlined in JAX-WS Specification

import jakarta.jws.WebService;

@WebService(portName = "HolderTestPort", serviceName = "HolderTestService", targetNamespace = "http://holdertest.org/wsdl", wsdlLocation = "WEB-INF/wsdl/WSW2JRLHolderTestService.wsdl", endpointInterface = "com.sun.ts.tests.jaxws.ee.w2j.rpc.literal.holdertest.HolderTest")
public class HolderTestImpl implements HolderTest {
  private static DatatypeFactory dtfactory = null;

  static {
    try {
      dtfactory = DatatypeFactory.newInstance();
    } catch (DatatypeConfigurationException e) {
      TestUtil.logMsg("Could not configure DatatypeFactory object");
      TestUtil.printStackTrace(e);
    }
  }

  public void echoInOutSimpleTypes(
      jakarta.xml.ws.Holder<java.lang.String> varString,
      jakarta.xml.ws.Holder<java.math.BigInteger> varInteger,
      jakarta.xml.ws.Holder<java.lang.Integer> varInt,
      jakarta.xml.ws.Holder<java.lang.Long> varLong,
      jakarta.xml.ws.Holder<java.lang.Short> varShort,
      jakarta.xml.ws.Holder<java.math.BigDecimal> varDecimal,
      jakarta.xml.ws.Holder<java.lang.Float> varFloat,
      jakarta.xml.ws.Holder<java.lang.Double> varDouble,
      jakarta.xml.ws.Holder<java.lang.Boolean> varBoolean,
      jakarta.xml.ws.Holder<java.lang.Byte> varByte,
      jakarta.xml.ws.Holder<javax.xml.namespace.QName> varQName,
      jakarta.xml.ws.Holder<javax.xml.datatype.XMLGregorianCalendar> varDateTime) {
    System.out.println("Entering echoInOutSimpleTypes()");
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
    XMLGregorianCalendar v12 = dtfactory.newXMLGregorianCalendar(96, 5, 1, 0,
        30, 0, 0, 0);
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
    if (!JAXWS_Data.compareXMLGregorianCalendars(v12, varDateTime.value)) {
      result = result + "\ncompare of data failed - received "
          + varDateTime.value + ",  expected: " + v12;
      pass = false;
    }
    if (!pass) {
      throw new WebServiceException(result);
    }

    varString.value = "String4";
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
    varDateTime.value = dtfactory.newXMLGregorianCalendar(96, 5, 2, 0, 30, 0, 0,
        0);
    System.out.println("Leaving echoInOutSimpleTypes()");
  }

  public void echoInOutSimpleTypesArray(
      jakarta.xml.ws.Holder<com.sun.ts.tests.jaxws.ee.w2j.rpc.literal.holdertest.ArrayOfstring> varString,
      jakarta.xml.ws.Holder<com.sun.ts.tests.jaxws.ee.w2j.rpc.literal.holdertest.ArrayOfinteger> varInteger,
      jakarta.xml.ws.Holder<com.sun.ts.tests.jaxws.ee.w2j.rpc.literal.holdertest.ArrayOfint> varInt,
      jakarta.xml.ws.Holder<com.sun.ts.tests.jaxws.ee.w2j.rpc.literal.holdertest.ArrayOflong> varLong,
      jakarta.xml.ws.Holder<com.sun.ts.tests.jaxws.ee.w2j.rpc.literal.holdertest.ArrayOfshort> varShort,
      jakarta.xml.ws.Holder<com.sun.ts.tests.jaxws.ee.w2j.rpc.literal.holdertest.ArrayOfdecimal> varDecimal,
      jakarta.xml.ws.Holder<com.sun.ts.tests.jaxws.ee.w2j.rpc.literal.holdertest.ArrayOffloat> varFloat,
      jakarta.xml.ws.Holder<com.sun.ts.tests.jaxws.ee.w2j.rpc.literal.holdertest.ArrayOfdouble> varDouble,
      jakarta.xml.ws.Holder<com.sun.ts.tests.jaxws.ee.w2j.rpc.literal.holdertest.ArrayOfboolean> varBoolean,
      jakarta.xml.ws.Holder<com.sun.ts.tests.jaxws.ee.w2j.rpc.literal.holdertest.ArrayOfbyte> varByte,
      jakarta.xml.ws.Holder<com.sun.ts.tests.jaxws.ee.w2j.rpc.literal.holdertest.ArrayOfQName> varQName,
      jakarta.xml.ws.Holder<com.sun.ts.tests.jaxws.ee.w2j.rpc.literal.holdertest.ArrayOfdateTime> varDateTime) {
    boolean pass = true;
    StringBuffer result = new StringBuffer();
    System.out.println("Entering echoInOutSimpleTypesArray()");
    if (varString.value.getArrayOfstring()
        .size() != JAXWS_Data.String_nonull_data.length) {
      pass = false;
      result.append("\nUnexpected size for Holder<ArrayOfstring>, expected: "
          + JAXWS_Data.String_nonull_data.length + " got: "
          + varString.value.getArrayOfstring().size());
    } else {
      for (int i = 0; i < JAXWS_Data.String_nonull_data.length; i++) {
        if (!varString.value.getArrayOfstring().get(i)
            .equals(JAXWS_Data.String_nonull_data[i])) {
          pass = false;
          result.append("\nArrayOfstring Value mismatch - expected:"
              + JAXWS_Data.String_nonull_data[i]);
          result.append("actual:" + varString.value.getArrayOfstring().get(i));
        }
      }
    }
    if (varInteger.value.getArrayOfinteger()
        .size() != JAXWS_Data.BigInteger_nonull_data.length) {
      pass = false;
      result.append("\nUnexpected size for Holder<ArrayOfinteger>, expected: "
          + JAXWS_Data.BigInteger_nonull_data.length + " got: "
          + varInteger.value.getArrayOfinteger().size());
    } else {
      for (int i = 0; i < JAXWS_Data.BigInteger_nonull_data.length; i++) {
        if (!varInteger.value.getArrayOfinteger().get(i)
            .equals(JAXWS_Data.BigInteger_nonull_data[i])) {
          pass = false;
          result.append("\nArrayOfinteger Value mismatch - expected:"
              + JAXWS_Data.BigInteger_nonull_data[i]);
          result
              .append("actual:" + varInteger.value.getArrayOfinteger().get(i));
        }
      }
    }
    if (varInt.value.getArrayOfint()
        .size() != JAXWS_Data.Integer_nonull_data.length) {
      pass = false;
      result.append("\nUnexpected size for Holder<ArrayOfint>, expected: "
          + JAXWS_Data.Integer_nonull_data.length + " got: "
          + varInt.value.getArrayOfint().size());
    } else {
      for (int i = 0; i < JAXWS_Data.Integer_nonull_data.length; i++) {
        if (!varInt.value.getArrayOfint().get(i)
            .equals(JAXWS_Data.Integer_nonull_data[i])) {
          pass = false;
          result.append("\nArrayOfint Value mismatch - expected:"
              + JAXWS_Data.Integer_nonull_data[i]);
          result.append("actual:" + varInt.value.getArrayOfint().get(i));
        }
      }
    }
    if (varLong.value.getArrayOflong()
        .size() != JAXWS_Data.Long_nonull_data.length) {
      pass = false;
      result.append("\nUnexpected size for Holder<ArrayOflong>, expected: "
          + JAXWS_Data.Long_nonull_data.length + " got: "
          + varLong.value.getArrayOflong().size());
    } else {
      for (int i = 0; i < JAXWS_Data.Long_nonull_data.length; i++) {
        if (!varLong.value.getArrayOflong().get(i)
            .equals(JAXWS_Data.Long_nonull_data[i])) {
          pass = false;
          result.append("\nArrayOflong Value mismatch - expected:"
              + JAXWS_Data.Long_nonull_data[i]);
          result.append("actual:" + varLong.value.getArrayOflong().get(i));
        }
      }
    }
    if (varShort.value.getArrayOfshort()
        .size() != JAXWS_Data.Short_nonull_data.length) {
      pass = false;
      result.append("\nUnexpected size for Holder<ArrayOfshort>, expected: "
          + JAXWS_Data.Short_nonull_data.length + " got: "
          + varShort.value.getArrayOfshort().size());
    } else {
      for (int i = 0; i < JAXWS_Data.Short_nonull_data.length; i++) {
        if (!varShort.value.getArrayOfshort().get(i)
            .equals(JAXWS_Data.Short_nonull_data[i])) {
          pass = false;
          result.append("\nArrayOfshort Value mismatch - expected:"
              + JAXWS_Data.Short_nonull_data[i]);
          result.append("actual:" + varShort.value.getArrayOfshort().get(i));
        }
      }
    }
    if (varDecimal.value.getArrayOfdecimal()
        .size() != JAXWS_Data.BigDecimal_nonull_data.length) {
      pass = false;
      result.append("\nUnexpected size for Holder<ArrayOfdecimal>, expected: "
          + JAXWS_Data.BigDecimal_nonull_data.length + " got: "
          + varDecimal.value.getArrayOfdecimal().size());
    } else {
      for (int i = 0; i < JAXWS_Data.BigDecimal_nonull_data.length; i++) {
        if (!varDecimal.value.getArrayOfdecimal().get(i)
            .equals(JAXWS_Data.BigDecimal_nonull_data[i])) {
          pass = false;
          result.append("\nArrayOfdecimal Value mismatch - expected:"
              + JAXWS_Data.BigDecimal_nonull_data[i]);
          result
              .append("actual:" + varDecimal.value.getArrayOfdecimal().get(i));
        }
      }
    }
    if (varFloat.value.getArrayOffloat()
        .size() != JAXWS_Data.Float_nonull_data.length) {
      pass = false;
      result.append("\nUnexpected size for Holder<ArrayOffloat>, expected: "
          + JAXWS_Data.Float_nonull_data.length + " got: "
          + varFloat.value.getArrayOffloat().size());
    } else {
      for (int i = 0; i < JAXWS_Data.Float_nonull_data.length; i++) {
        if (!varFloat.value.getArrayOffloat().get(i)
            .equals(JAXWS_Data.Float_nonull_data[i])) {
          pass = false;
          result.append("\nArrayOffloat Value mismatch - expected:"
              + JAXWS_Data.Float_nonull_data[i]);
          result.append("actual:" + varFloat.value.getArrayOffloat().get(i));
        }
      }
    }
    if (varDouble.value.getArrayOfdouble()
        .size() != JAXWS_Data.Double_nonull_data.length) {
      pass = false;
      result.append("\nUnexpected size for Holder<ArrayOfdouble>, expected: "
          + JAXWS_Data.Double_nonull_data.length + " got: "
          + varDouble.value.getArrayOfdouble().size());
    } else {
      for (int i = 0; i < JAXWS_Data.Double_nonull_data.length; i++) {
        if (!varDouble.value.getArrayOfdouble().get(i)
            .equals(JAXWS_Data.Double_nonull_data[i])) {
          pass = false;
          result.append("\nArrayOfdouble Value mismatch - expected:"
              + JAXWS_Data.Double_nonull_data[i]);
          result.append("actual:" + varDouble.value.getArrayOfdouble().get(i));
        }
      }
    }
    if (varBoolean.value.getArrayOfboolean()
        .size() != JAXWS_Data.Boolean_nonull_data.length) {
      pass = false;
      result.append("\nUnexpected size for Holder<ArrayOfboolean>, expected: "
          + JAXWS_Data.Boolean_nonull_data.length + " got: "
          + varBoolean.value.getArrayOfboolean().size());
    } else {
      for (int i = 0; i < JAXWS_Data.Boolean_nonull_data.length; i++) {
        if (!varBoolean.value.getArrayOfboolean().get(i)
            .equals(JAXWS_Data.Boolean_nonull_data[i])) {
          pass = false;
          result.append("\nArrayOfboolean Value mismatch - expected:"
              + JAXWS_Data.Boolean_nonull_data[i]);
          result
              .append("actual:" + varBoolean.value.getArrayOfboolean().get(i));
        }
      }
    }
    if (varByte.value.getArrayOfbyte()
        .size() != JAXWS_Data.Byte_nonull_data.length) {
      pass = false;
      result.append("\nUnexpected size for Holder<ArrayOfbyte>, expected: "
          + JAXWS_Data.Byte_nonull_data.length + " got: "
          + varByte.value.getArrayOfbyte().size());
    } else {
      for (int i = 0; i < JAXWS_Data.Byte_nonull_data.length; i++) {
        if (!varByte.value.getArrayOfbyte().get(i)
            .equals(JAXWS_Data.Byte_nonull_data[i])) {
          pass = false;
          result.append("\nArrayOfbyte Value mismatch - expected:"
              + JAXWS_Data.Byte_nonull_data[i]);
          result.append("actual:" + varByte.value.getArrayOfbyte().get(i));
        }
      }
    }
    if (varQName.value.getArrayOfQName()
        .size() != JAXWS_Data.QName_nonull_data.length) {
      pass = false;
      result.append("\nUnexpected size for Holder<ArrayOfQName>, expected: "
          + JAXWS_Data.QName_nonull_data.length + " got: "
          + varQName.value.getArrayOfQName().size());
    } else {
      for (int i = 0; i < JAXWS_Data.QName_nonull_data.length; i++) {
        if (!varQName.value.getArrayOfQName().get(i)
            .equals(JAXWS_Data.QName_nonull_data[i])) {
          pass = false;
          result.append("\nArrayOfQName Value mismatch - expected:"
              + JAXWS_Data.QName_nonull_data[i]);
          result.append("actual:" + varQName.value.getArrayOfQName().get(i));
        }
      }
    }
    if (varDateTime.value.getArrayOfdateTime()
        .size() != JAXWS_Data.XMLGregorianCalendar_nonull_data.length) {
      pass = false;
      result.append("\nUnexpected size for Holder<ArrayOfdateTime>, expected: "
          + JAXWS_Data.XMLGregorianCalendar_nonull_data.length + " got: "
          + varDateTime.value.getArrayOfdateTime().size());
    } else {
      for (int i = 0; i < JAXWS_Data.XMLGregorianCalendar_nonull_data.length; i++) {
        if (!JAXWS_Data.compareXMLGregorianCalendars(
            varDateTime.value.getArrayOfdateTime().get(i),
            JAXWS_Data.XMLGregorianCalendar_nonull_data[i])) {
          pass = false;
          result.append("\nArrayOfdateTime Value mismatch - expected:"
              + JAXWS_Data.XMLGregorianCalendar_nonull_data[i]);
          result.append(
              "actual:" + varDateTime.value.getArrayOfdateTime().get(i));
        }
      }
    }
    if (!pass) {
      throw new WebServiceException(result.toString());
    }
    System.out.println("Leaving echoInOutSimpleTypesArray()");
  }

  public void echoInOutBook(
      jakarta.xml.ws.Holder<com.sun.ts.tests.jaxws.ee.w2j.rpc.literal.holdertest.Book> varBook) {
    boolean pass = true;
    StringBuffer result = new StringBuffer();
    System.out.println("Entering echoInOutBook()");
    Book b = varBook.value;
    if (!b.getAuthor().equals("author0")) {
      pass = false;
      result.append("\nUnexpected author was received:");
      result.append("\nExpected=author0" + ", actual=" + b.getAuthor());
    }
    if (!b.getTitle().equals("title0")) {
      pass = false;
      result.append("\nUnexpected title was received:");
      result.append("\nExpected=title0" + ", actual=" + b.getTitle());
    }
    if (b.getIsbn() != 0) {
      pass = false;
      result.append("\nUnexpected isbn was received:");
      result.append("\nExpected=0" + ", actual=" + b.getIsbn());
    }
    if (!pass) {
      throw new WebServiceException(result.toString());
    }
    b.setAuthor("author1");
    b.setTitle("title1");
    b.setIsbn(1);
    varBook.value = b;
    System.out.println("Leaving echoInOutBook()");
  }

  public void echoInOutBookArray(
      jakarta.xml.ws.Holder<com.sun.ts.tests.jaxws.ee.w2j.rpc.literal.holdertest.ArrayOfBook> varBook) {
    boolean pass = true;
    StringBuffer result = new StringBuffer();
    System.out.println("Entering echoInOutBookArray()");
    java.util.List<Book> blist = varBook.value.getArrayOfBook();
    if (blist.size() != 2)
      throw new WebServiceException("list size not 2, it was" + blist.size());
    Book bentry0 = blist.get(0);
    Book bentry1 = blist.get(1);
    if (!bentry0.getAuthor().equals("author0")) {
      pass = false;
      result.append("\nUnexpected author was received:");
      result.append("\nExpected=author0" + ", actual=" + bentry0.getAuthor());
    }
    if (!bentry0.getTitle().equals("title0")) {
      pass = false;
      result.append("\nUnexpected title was received:");
      result.append("\nExpected=title0" + ", actual=" + bentry0.getTitle());
    }
    if (bentry0.getIsbn() != 0) {
      pass = false;
      result.append("\nUnexpected isbn was received:");
      result.append("\nExpected=0" + ", actual=" + bentry0.getIsbn());
    }
    if (!bentry1.getAuthor().equals("author1")) {
      pass = false;
      result.append("\nUnexpected author was received:");
      result.append("\nExpected=author1" + ", actual=" + bentry1.getAuthor());
    }
    if (!bentry1.getTitle().equals("title1")) {
      pass = false;
      result.append("\nUnexpected title was received:");
      result.append("\nExpected=title1" + ", actual=" + bentry1.getTitle());
    }
    if (bentry1.getIsbn() != 1) {
      pass = false;
      result.append("\nUnexpected isbn was received:");
      result.append("\nExpected=1" + ", actual=" + bentry1.getIsbn());
    }
    if (!pass) {
      throw new WebServiceException(result.toString());
    }
    varBook.value.getArrayOfBook().clear();
    String author = bentry0.getAuthor();
    String title = bentry0.getTitle();
    int isbn = bentry0.getIsbn();
    bentry0.setAuthor(bentry1.getAuthor());
    bentry0.setTitle(bentry1.getTitle());
    bentry0.setIsbn(bentry1.getIsbn());
    bentry1.setAuthor(author);
    bentry1.setTitle(title);
    bentry1.setIsbn(isbn);
    varBook.value.getArrayOfBook().add(bentry0);
    varBook.value.getArrayOfBook().add(bentry1);
    System.out.println("Leaving echoInOutBookArray()");
  }
}
