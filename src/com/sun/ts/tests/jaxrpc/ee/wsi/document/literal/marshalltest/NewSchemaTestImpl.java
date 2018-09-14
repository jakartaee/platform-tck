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

package com.sun.ts.tests.jaxrpc.ee.wsi.document.literal.marshalltest;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.tests.jaxrpc.common.*;

import com.sun.ts.tests.jaxrpc.ee.wsi.document.literal.marshalltest.*;

import java.rmi.Remote;
import java.rmi.RemoteException;

import java.math.BigInteger;
import java.math.BigDecimal;

import javax.xml.soap.*;

import java.util.*;

// Service Implementation Class - as outlined in JAX-RPC Specification

public class NewSchemaTestImpl implements NewSchemaTest {

  public FooStringResponse fooFaultTest(FooStringRequest fooRequest)
      throws FooFaultException, RemoteException {
    FooStringResponse f = new FooStringResponse();
    f.setVarString(fooRequest.getVarString());
    if (fooRequest.getVarString().equals("FooBad1")) {
      throw new FooFaultException(WhyTheFault.fromString("FooBad1"));
    } else if (fooRequest.getVarString().equals("FooBad2")) {
      throw new FooFaultException(WhyTheFault.fromString("FooBad2"));
    } else if (fooRequest.getVarString().equals("FooBad3")) {
      throw new FooFaultException(WhyTheFault.fromString("FooBad3"));
    } else if (fooRequest.getVarString().equals("FooBad4")) {
      throw new FooFaultException(WhyTheFault.fromString("FooBad4"));
    } else if (fooRequest.getVarString().equals("FooBad5")) {
      throw new FooFaultException(WhyTheFault.fromString("FooBad5"));
    }
    return f;
  }

  public com.sun.ts.tests.jaxrpc.ee.wsi.document.literal.marshalltest.IncludedStringResponse echoIncludedStringTest(
      com.sun.ts.tests.jaxrpc.ee.wsi.document.literal.marshalltest.IncludedStringRequest request)
      throws RemoteException {
    com.sun.ts.tests.jaxrpc.ee.wsi.document.literal.marshalltest.IncludedStringResponse ret = null;
    String sret = request.getMyString();
    ret = new IncludedStringResponse();
    ret.setMyString(sret);
    return ret;
  }

  public String echoFooStringTypeTest(String request) throws RemoteException {
    return request;
  }

  public java.math.BigInteger echoFooIntegerTypeTest(
      java.math.BigInteger request) throws RemoteException {
    return request;
  }

  public int echoFooIntTypeTest(int request) throws RemoteException {
    return request;
  }

  public long echoFooLongTypeTest(long request) throws RemoteException {
    return request;
  }

  public short echoFooShortTypeTest(short request) throws RemoteException {
    return request;
  }

  public BigDecimal echoFooDecimalTypeTest(BigDecimal request)
      throws RemoteException {
    return request;
  }

  public float echoFooFloatTypeTest(float request) throws RemoteException {
    return request;
  }

  public double echoFooDoubleTypeTest(double request) throws RemoteException {
    return request;
  }

  public boolean echoFooBooleanTypeTest(boolean request)
      throws RemoteException {
    return request;
  }

  public byte echoFooByteTypeTest(byte request) throws RemoteException {
    return request;
  }

  public javax.xml.namespace.QName echoFooQNameTypeTest(
      javax.xml.namespace.QName request) throws RemoteException {
    return request;
  }

  public FooStatusType sendFoo1Test(FooType fooRequest) throws RemoteException {
    InitExpectedFooTypeData();
    FooStatusType fooStatus = new FooStatusType();
    fooStatus.setFooA(CompareWithExpectedFooTypeData(fooRequest));
    return fooStatus;
  }

  public FooType sendFoo2Test(FooType fooRequest) throws RemoteException {
    return fooRequest;
  }

  public String echoNormalizedStringTypeTest(String v) throws RemoteException {
    return v;
  }

  public FooVariousSchemaTypes echoVariousSchemaTypesTest(
      FooVariousSchemaTypes v) throws RemoteException {
    return v;
  }

  public FooVariousSchemaTypesListType echoVariousSchemaTypesListTypeTest(
      FooVariousSchemaTypesListType v) throws RemoteException {
    return v;
  }

  public BigInteger echoIntegerRangeTypeTest(BigInteger v)
      throws RemoteException {
    return v;
  }

  public FooStringEnumType echoStringEnumTypeTest(FooStringEnumType v)
      throws RemoteException {
    return v;
  }

  public FooByteEnumType echoByteEnumTypeTest(FooByteEnumType v)
      throws RemoteException {
    return v;
  }

  public FooShortEnumType echoShortEnumTypeTest(FooShortEnumType v)
      throws RemoteException {
    return v;
  }

  public FooIntegerEnumType echoIntegerEnumTypeTest(FooIntegerEnumType v)
      throws RemoteException {
    return v;
  }

  public FooIntEnumType echoIntEnumTypeTest(FooIntEnumType v)
      throws RemoteException {
    return v;
  }

  public FooLongEnumType echoLongEnumTypeTest(FooLongEnumType v)
      throws RemoteException {
    return v;
  }

  public FooDecimalEnumType echoDecimalEnumTypeTest(FooDecimalEnumType v)
      throws RemoteException {
    return v;
  }

  public FooFloatEnumType echoFloatEnumTypeTest(FooFloatEnumType v)
      throws RemoteException {
    return v;
  }

  public FooDoubleEnumType echoDoubleEnumTypeTest(FooDoubleEnumType v)
      throws RemoteException {
    return v;
  }

  public FooAnonymousType echoAnonymousTypeTest(FooAnonymousType v)
      throws RemoteException {
    return v;
  }

  public FooAnnotationType echoAnnotationTypeTest(FooAnnotationType v)
      throws RemoteException {
    return v;
  }

  public String echoAnySimpleTypeTest(String v) throws RemoteException {
    return v;
  }

  public FooAnyURIType echoAnyURITypeTest(FooAnyURIType v)
      throws RemoteException {
    return v;
  }

  public String echoLanguageTypeTest(String v) throws RemoteException {
    return v;
  }

  public String echoTokenTypeTest(String v) throws RemoteException {
    return v;
  }

  public String echoNameTypeTest(String v) throws RemoteException {
    return v;
  }

  public String echoNCNameTypeTest(String v) throws RemoteException {
    return v;
  }

  public String echoIDTypeTest(String v) throws RemoteException {
    return v;
  }

  public int echoUnsignedShortTest(int v) throws RemoteException {
    return v;
  }

  public long echoUnsignedIntTest(long v) throws RemoteException {
    return v;
  }

  public short echoUnsignedByteTest(short v) throws RemoteException {
    return v;
  }

  public BigInteger echoUnsignedLongTest(BigInteger v) throws RemoteException {
    return v;
  }

  public BigInteger echoNonPositiveIntegerTest(BigInteger v)
      throws RemoteException {
    return v;
  }

  public BigInteger echoNonNegativeIntegerTest(BigInteger v)
      throws RemoteException {
    return v;
  }

  public BigInteger echoPositiveIntegerTest(BigInteger v)
      throws RemoteException {
    return v;
  }

  public BigInteger echoNegativeIntegerTest(BigInteger v)
      throws RemoteException {
    return v;
  }

  public Calendar echoTimeTest(Calendar v) throws RemoteException {
    return v;
  }

  public Calendar echoDateTest(Calendar v) throws RemoteException {
    return v;
  }

  public String[] echoStringListTypeTest(String[] v) throws RemoteException {
    return v;
  }

  public float[] echoFloatListTypeTest(float[] v) throws RemoteException {
    return v;
  }

  public int[] echoIntListTypeTest(int[] v) throws RemoteException {
    return v;
  }

  public java.math.BigDecimal[] echoDecimalListTypeTest(
      java.math.BigDecimal[] v) throws RemoteException {
    return v;
  }

  public double[] echoDoubleListTypeTest(double[] v) throws RemoteException {
    return v;
  }

  public java.math.BigInteger[] echoIntegerListTypeTest(
      java.math.BigInteger[] v) throws RemoteException {
    return v;
  }

  public long[] echoLongListTypeTest(long[] v) throws RemoteException {
    return v;
  }

  public short[] echoShortListTypeTest(short[] v) throws RemoteException {
    return v;
  }

  public byte[] echoByteListTypeTest(byte[] v) throws RemoteException {
    return v;
  }

  /**********************************************************************
   * Private data methods to setup and compare expected FooType data
   *********************************************************************/

  private FooType FooType_data = null;

  private FooType FooType_array_data[] = null;

  private FooVariousSchemaTypes FooVariousSchemaTypes_data = null;

  private FooVariousSchemaTypes FooVariousSchemaTypes_array_data[] = null;

  private FooVariousSchemaTypesListType FooVariousSchemaTypesListType_data = null;

  private FooAnonymous FooAnonymous_array_data[] = null;

  private FooAnonymousType FooAnonymousType_data = null;

  private void InitExpectedFooTypeData() {
    init_FooVariousSchemaTypes_Data();
    init_FooAnonymousType_Data();
    init_FooType_Data();
  }

  private boolean CompareWithExpectedFooTypeData(FooType f) {
    boolean valid = true;

    if (f.isFooA() != true) {
      System.err.println(
          "isFooA() returned " + f.isFooA() + ", expected " + !f.isFooA());
      valid = false;
    }
    if (f.getFooB() != Byte.MAX_VALUE) {
      System.err.println(
          "getFooB() returned " + f.getFooB() + ", expected " + Byte.MAX_VALUE);
      valid = false;
    }
    if (f.getFooC() != Short.MAX_VALUE) {
      System.err.println("getFooC() returned " + f.getFooC() + ", expected "
          + Short.MAX_VALUE);
      valid = false;
    }
    if (f.getFooD() != Integer.MAX_VALUE) {
      System.err.println("getFooD() returned " + f.getFooD() + ", expected "
          + Integer.MAX_VALUE);
      valid = false;
    }
    if (f.getFooE() != Long.MAX_VALUE) {
      System.err.println(
          "getFooE() returned " + f.getFooE() + ", expected " + Long.MAX_VALUE);
      valid = false;
    }
    if (f.getFooF() != Float.MAX_VALUE) {
      System.err.println("getFooF() returned " + f.getFooF() + ", expected "
          + Float.MAX_VALUE);
      valid = false;
    }
    if (f.getFooG() != Double.MAX_VALUE) {
      System.err.println("getFooG() returned " + f.getFooG() + ", expected "
          + Double.MAX_VALUE);
      valid = false;
    }
    if (!f.getFooH().equals("foostringH")) {
      System.err.println(
          "getFooH() returned " + f.getFooH() + ", expected foostringH");
      valid = false;
    }
    if (!f.getFooI().equals("123-ABC12")) {
      System.err.println(
          "getFooI() returned " + f.getFooI() + ", expected 123-ABC12");
      valid = false;
    }
    FooVariousSchemaTypes fnst = f.getFooJ();
    if (fnst == null) {
      System.err.println(
          "getFooJ() returned null, " + "expected FooVariousSchemaTypes");
      valid = false;
    }
    if (fnst != null) {
      System.out.println("Send: " + FooVariousSchemaTypes_data.getFooA() + "|"
          + FooVariousSchemaTypes_data.getFooB() + "|"
          + FooVariousSchemaTypes_data.getFooC() + "|"
          + FooVariousSchemaTypes_data.getFooD() + "|"
          + FooVariousSchemaTypes_data.getFooE() + "|"
          + FooVariousSchemaTypes_data.getFooF());
      System.out.println("Recv: " + fnst.getFooA() + "|" + fnst.getFooB() + "|"
          + fnst.getFooC() + "|" + fnst.getFooD() + "|" + fnst.getFooE() + "|"
          + fnst.getFooF());
      if (fnst.getFooA() == FooVariousSchemaTypes_data.getFooA()
          && fnst.getFooB().equals(FooVariousSchemaTypes_data.getFooB())
          && fnst.getFooC().equals(FooVariousSchemaTypes_data.getFooC())
          && fnst.getFooD().equals(FooVariousSchemaTypes_data.getFooD())
          && fnst.getFooE() == FooVariousSchemaTypes_data.getFooE()
          && fnst.getFooF() == FooVariousSchemaTypes_data.getFooF()) {
        System.out.println("Result match");
      } else {
        System.err.println("Result mismatch");
        valid = false;
      }
    }
    if (!f.getFooK().equals(new BigInteger("101"))) {
      System.err
          .println("getFooK() returned " + f.getFooK() + ", expected 101");
      valid = false;
    }
    if (!(f.getFooM().equals("hello,there"))) {
      System.err.println(
          "getFooM() returned " + f.getFooM() + ", expected hello,there");
      valid = false;
    }
    if (!compareFooAnonymousTypeData(f.getFooN(), FooAnonymousType_data))
      valid = false;
    return valid;
  }

  private void init_FooVariousSchemaTypes_Data() {
    FooVariousSchemaTypes_data = new FooVariousSchemaTypes();
    FooVariousSchemaTypes_data.setFooA(1);
    FooVariousSchemaTypes_data.setFooB(new BigInteger("1000"));
    FooVariousSchemaTypes_data.setFooC("NORMALIZEDSTRING");
    FooVariousSchemaTypes_data.setFooD("NMTOKEN");
    FooVariousSchemaTypes_data.setFooE(1);
    FooVariousSchemaTypes_data.setFooF((short) 1);

    FooVariousSchemaTypes_array_data = new FooVariousSchemaTypes[2];
    FooVariousSchemaTypesListType_data = new FooVariousSchemaTypesListType();

    FooVariousSchemaTypes_array_data[0] = new FooVariousSchemaTypes();
    FooVariousSchemaTypes_array_data[1] = new FooVariousSchemaTypes();
    FooVariousSchemaTypes_array_data[0].setFooA(256);
    FooVariousSchemaTypes_array_data[1].setFooA(0);
    FooVariousSchemaTypes_array_data[0].setFooB(JAXRPC_Data.BigInteger_data[0]);
    FooVariousSchemaTypes_array_data[1].setFooB(JAXRPC_Data.BigInteger_data[1]);
    FooVariousSchemaTypes_array_data[0].setFooC("NORMALIZEDSTRING1");
    FooVariousSchemaTypes_array_data[1].setFooC("NORMALIZEDSTRING2");
    FooVariousSchemaTypes_array_data[0].setFooD("NMTOKEN1");
    FooVariousSchemaTypes_array_data[1].setFooD("NMTOKEN2");
    FooVariousSchemaTypes_array_data[0].setFooE(0);
    FooVariousSchemaTypes_array_data[1].setFooE(1);
    FooVariousSchemaTypes_array_data[0].setFooF((short) 0);
    FooVariousSchemaTypes_array_data[1].setFooF((short) 1);
    FooVariousSchemaTypesListType_data
        .setFooA(FooVariousSchemaTypes_array_data);
  }

  private void init_FooAnonymousType_Data() {
    FooAnonymous_array_data = new FooAnonymous[2];
    FooAnonymousType_data = new FooAnonymousType();

    FooAnonymous_array_data[0] = new FooAnonymous();
    FooAnonymous_array_data[1] = new FooAnonymous();
    FooAnonymous_array_data[0].setFooA("foo");
    FooAnonymous_array_data[1].setFooA("bar");
    FooAnonymous_array_data[0].setFooB(1);
    FooAnonymous_array_data[1].setFooB(0);
    FooAnonymous_array_data[0].setFooC(true);
    FooAnonymous_array_data[1].setFooC(false);
    FooAnonymousType_data.setFooAnonymous(FooAnonymous_array_data);
  }

  private boolean compareFooAnonymousTypeData(FooAnonymousType request,
      FooAnonymousType response) {
    boolean valid = true;

    FooAnonymous[] req = request.getFooAnonymous();
    FooAnonymous[] res = response.getFooAnonymous();
    if (req.length == res.length) {
      System.out.println("Array length match - checking array elements");
      for (int i = 0; i < req.length; i++) {
        FooAnonymous exp = req[i];
        FooAnonymous rec = res[i];
        System.out.println("Request: " + exp.getFooA() + "|" + exp.getFooB()
            + "|" + exp.isFooC());
        System.out.println("Response: " + rec.getFooA() + "|" + rec.getFooB()
            + "|" + rec.isFooC());
        if (!exp.getFooA().equals(rec.getFooA())
            || exp.getFooB() != rec.getFooB() || exp.isFooC() != rec.isFooC()) {
          valid = false;
          System.err.println("Element results mismatch ...");
          break;
        } else
          System.out.println("Element results match ...");
      }
    } else {
      System.err.println("Array length mismatch - expected: " + req.length
          + ", received: " + res.length);
    }
    return valid;
  }

  private void init_FooType_Data() {
    FooType_data = new FooType();

    FooType_data.setFooA(true);
    FooType_data.setFooB(Byte.MAX_VALUE);
    FooType_data.setFooC(Short.MAX_VALUE);
    FooType_data.setFooD(Integer.MAX_VALUE);
    FooType_data.setFooE(Long.MAX_VALUE);
    FooType_data.setFooF(Float.MAX_VALUE);
    FooType_data.setFooG(Double.MAX_VALUE);
    FooType_data.setFooH("foostringH");
    FooType_data.setFooI("123-ABC12");
    FooType_data.setFooJ(FooVariousSchemaTypes_data);
    FooType_data.setFooK(new BigInteger("101"));
    FooType_data.setFooM("hello,there");
    FooType_data.setFooN(FooAnonymousType_data);
  }
}
