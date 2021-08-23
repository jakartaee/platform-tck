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

package com.sun.ts.tests.jaxws.ee.w2j.rpc.literal.marshalltest;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.tests.jaxws.common.*;

import com.sun.ts.tests.jaxws.ee.w2j.rpc.literal.marshalltest.*;

import jakarta.xml.ws.WebServiceException;

import java.math.BigInteger;
import java.math.BigDecimal;
import jakarta.xml.soap.*;
import javax.xml.datatype.*;

import java.util.*;

// Service Implementation Class - as outlined in JAX-WS Specification

import jakarta.jws.WebService;

@WebService(targetNamespace = "http://marshalltestservice.org/MarshallTestService.wsdl", portName = "MarshallTestPort2", serviceName = "MarshallTestService", wsdlLocation = "WEB-INF/wsdl/WSW2JRLMarshallTestService.wsdl", endpointInterface = "com.sun.ts.tests.jaxws.ee.w2j.rpc.literal.marshalltest.NewSchemaTest")

public class NewSchemaTestImpl implements NewSchemaTest {

  public FooStringResponse fooFaultTest(FooStringRequest fooRequest)
      throws FooFault {
    FooStringResponse f = null;
    FooFaultException ffe = null;
    try {
      f = new FooStringResponse();
      f.setVarString(fooRequest.getVarString());
      ffe = new FooFaultException();
      ffe.setWhyTheFault(fooRequest.getVarString());
    } catch (Exception e) {
      throw new WebServiceException("failed on object creation: " + e);
    }
    if (fooRequest.getVarString().equals("FooBad1")) {
      throw new FooFault("FooBad1", ffe);
    } else if (fooRequest.getVarString().equals("FooBad2")) {
      throw new FooFault("FooBad2", ffe);
    } else if (fooRequest.getVarString().equals("FooBad3")) {
      throw new FooFault("FooBad3", ffe);
    } else if (fooRequest.getVarString().equals("FooBad4")) {
      throw new FooFault("FooBad4", ffe);
    } else if (fooRequest.getVarString().equals("FooBad5")) {
      throw new FooFault("FooBad5", ffe);
    }
    return f;
  }

  public IncludedStringResponse echoIncludedStringTest(
      IncludedStringRequest request) {
    IncludedStringResponse ret = null;
    try {
      String sret = request.getMyString();
      ret = new IncludedStringResponse();
      ret.setMyString(sret);
    } catch (Exception e) {
      throw new WebServiceException("failed on object creation: " + e);
    }
    return ret;
  }

  public String echoFooStringTypeTest(String request) {
    return request;
  }

  public java.math.BigInteger echoFooIntegerTypeTest(
      java.math.BigInteger request) {
    return request;
  }

  public int echoFooIntTypeTest(int request) {
    return request;
  }

  public long echoFooLongTypeTest(long request) {
    return request;
  }

  public short echoFooShortTypeTest(short request) {
    return request;
  }

  public BigDecimal echoFooDecimalTypeTest(BigDecimal request) {
    return request;
  }

  public float echoFooFloatTypeTest(float request) {
    return request;
  }

  public double echoFooDoubleTypeTest(double request) {
    return request;
  }

  public boolean echoFooBooleanTypeTest(boolean request) {
    return request;
  }

  public byte echoFooByteTypeTest(byte request) {
    return request;
  }

  public javax.xml.namespace.QName echoFooQNameTypeTest(
      javax.xml.namespace.QName request) {
    return request;
  }

  public FooStatusType sendFoo1Test(FooType fooRequest) {
    InitExpectedFooTypeData();
    FooStatusType fooStatus;

    try {
      fooStatus = new FooStatusType();
      fooStatus.setFooA(CompareWithExpectedFooTypeData(fooRequest));
    } catch (Exception e) {
      throw new WebServiceException("failed on object creation: " + e);
    }
    return fooStatus;
  }

  public FooType sendFoo2Test(FooType fooRequest) {
    return fooRequest;
  }

  public String echoNormalizedStringTypeTest(String v) {
    return v;
  }

  public FooVariousSchemaTypes echoVariousSchemaTypesTest(
      FooVariousSchemaTypes v) {
    return v;
  }

  public FooVariousSchemaTypesListType echoVariousSchemaTypesListTypeTest(
      FooVariousSchemaTypesListType v) {
    return v;
  }

  public BigInteger echoIntegerRangeTypeTest(BigInteger v) {
    return v;
  }

  public FooStringEnumType echoStringEnumTypeTest(FooStringEnumType v) {
    return v;
  }

  public byte echoByteEnumTypeTest(byte v) {
    return v;
  }

  public short echoShortEnumTypeTest(short v) {
    return v;
  }

  public BigInteger echoIntegerEnumTypeTest(BigInteger v) {
    return v;
  }

  public int echoIntEnumTypeTest(int v) {
    return v;
  }

  public long echoLongEnumTypeTest(long v) {
    return v;
  }

  public BigDecimal echoDecimalEnumTypeTest(BigDecimal v) {
    return v;
  }

  public float echoFloatEnumTypeTest(float v) {
    return v;
  }

  public double echoDoubleEnumTypeTest(double v) {
    return v;
  }

  public FooAnonymousType echoAnonymousTypeTest(FooAnonymousType v) {
    return v;
  }

  public FooAnnotationType echoAnnotationTypeTest(FooAnnotationType v) {
    return v;
  }

  public FooAnySimpleType echoAnySimpleTypeTest(FooAnySimpleType v) {
    return v;
  }

  public FooAnyURIType echoAnyURITypeTest(FooAnyURIType v) {
    return v;
  }

  public LanguageElem echoLanguageTypeTest(LanguageElem v) {
    return v;
  }

  public TokenElem echoTokenTypeTest(TokenElem v) {
    return v;
  }

  public NameElem echoNameTypeTest(NameElem v) {
    return v;
  }

  public NCNameElem echoNCNameTypeTest(NCNameElem v) {
    return v;
  }

  public IDElem echoIDTypeTest(IDElem v) {
    return v;
  }

  public int echoUnsignedShortTest(int v) {
    return v;
  }

  public long echoUnsignedIntTest(long v) {
    return v;
  }

  public short echoUnsignedByteTest(short v) {
    return v;
  }

  public BigInteger echoUnsignedLongTest(BigInteger v) {
    return v;
  }

  public BigInteger echoNonPositiveIntegerTest(BigInteger v) {
    return v;
  }

  public BigInteger echoNonNegativeIntegerTest(BigInteger v) {
    return v;
  }

  public BigInteger echoPositiveIntegerTest(BigInteger v) {
    return v;
  }

  public BigInteger echoNegativeIntegerTest(BigInteger v) {
    return v;
  }

  public XMLGregorianCalendar echoTimeTest(XMLGregorianCalendar v) {
    return v;
  }

  public XMLGregorianCalendar echoDateTest(XMLGregorianCalendar v) {
    return v;
  }

  public String[] echoStringListTypeTest(String[] v) {
    return v;
  }

  public Float[] echoFloatListTypeTest(Float[] v) {
    return v;
  }

  public Integer[] echoIntListTypeTest(Integer[] v) {
    return v;
  }

  public java.math.BigDecimal[] echoDecimalListTypeTest(
      java.math.BigDecimal[] v) {
    return v;
  }

  public Double[] echoDoubleListTypeTest(Double[] v) {
    return v;
  }

  public java.math.BigInteger[] echoIntegerListTypeTest(
      java.math.BigInteger[] v) {
    return v;
  }

  public Long[] echoLongListTypeTest(Long[] v) {
    return v;
  }

  public Short[] echoShortListTypeTest(Short[] v) {
    return v;
  }

  public Byte[] echoByteListTypeTest(Byte[] v) {
    return v;
  }

  /**********************************************************************
   * Private data methods to setup and compare expected FooType data
   *********************************************************************/

  private FooType FooType_data = null;

  private FooVariousSchemaTypes FooVariousSchemaTypes_data = null;

  private FooVariousSchemaTypes FooVariousSchemaTypes_array_data[] = null;

  private FooVariousSchemaTypesListType FooVariousSchemaTypesListType_data = null;

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
    try {
      FooVariousSchemaTypes_data = new FooVariousSchemaTypes();
      FooVariousSchemaTypes_data.setFooA(1);
      FooVariousSchemaTypes_data.setFooB(new BigInteger("1000"));
      FooVariousSchemaTypes_data.setFooC("NORMALIZEDSTRING");
      FooVariousSchemaTypes_data.setFooD("NMTOKEN");
      FooVariousSchemaTypes_data.setFooE(1);
      FooVariousSchemaTypes_data.setFooF((short) 1);

      FooVariousSchemaTypes_array_data = new FooVariousSchemaTypes[2];

      FooVariousSchemaTypes_array_data[0] = new FooVariousSchemaTypes();
      FooVariousSchemaTypes_array_data[1] = new FooVariousSchemaTypes();
      FooVariousSchemaTypes_array_data[0].setFooA(256);
      FooVariousSchemaTypes_array_data[1].setFooA(0);
      FooVariousSchemaTypes_array_data[0]
          .setFooB(JAXWS_Data.BigInteger_data[0]);
      FooVariousSchemaTypes_array_data[1]
          .setFooB(JAXWS_Data.BigInteger_data[1]);
      FooVariousSchemaTypes_array_data[0].setFooC("NORMALIZEDSTRING1");
      FooVariousSchemaTypes_array_data[1].setFooC("NORMALIZEDSTRING2");
      FooVariousSchemaTypes_array_data[0].setFooD("NMTOKEN1");
      FooVariousSchemaTypes_array_data[1].setFooD("NMTOKEN2");
      FooVariousSchemaTypes_array_data[0].setFooE(0);
      FooVariousSchemaTypes_array_data[1].setFooE(1);
      FooVariousSchemaTypes_array_data[0].setFooF((short) 0);
      FooVariousSchemaTypes_array_data[1].setFooF((short) 1);

      FooVariousSchemaTypesListType_data = new FooVariousSchemaTypesListType();

      for (int i = 0; i < FooVariousSchemaTypes_array_data.length; i++) {
        FooVariousSchemaTypesListType_data.getFooA()
            .add(FooVariousSchemaTypes_array_data[i]);
      }
    } catch (Exception e) {
      throw new WebServiceException("failed on object creation: " + e);
    }
  }

  private void init_FooAnonymousType_Data() {
    try {
      FooAnonymousType.FooAnonymousElement fe1 = new FooAnonymousType.FooAnonymousElement();
      FooAnonymousType.FooAnonymousElement fe2 = new FooAnonymousType.FooAnonymousElement();
      fe1.setFooA("foo");
      fe1.setFooB(1);
      fe1.setFooC(true);
      fe2.setFooA("bar");
      fe2.setFooB(0);
      fe2.setFooC(false);

      FooAnonymousType_data = new FooAnonymousType();
      FooAnonymousType_data.getFooAnonymousElement().add(fe1);
      FooAnonymousType_data.getFooAnonymousElement().add(fe2);
    } catch (Exception e) {
      throw new WebServiceException("failed on object creation: " + e);
    }
  }

  private boolean compareFooAnonymousTypeData(FooAnonymousType request,
      FooAnonymousType response) {
    boolean valid = true;

    Object[] req = request.getFooAnonymousElement().toArray();
    Object[] res = response.getFooAnonymousElement().toArray();
    if (req.length == res.length) {
      System.out.println("Array length match - checking array elements");
      for (int i = 0; i < req.length; i++) {
        FooAnonymousType.FooAnonymousElement exp = (FooAnonymousType.FooAnonymousElement) req[i];
        FooAnonymousType.FooAnonymousElement rec = (FooAnonymousType.FooAnonymousElement) res[i];
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
    try {
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
    } catch (Exception e) {
      throw new WebServiceException("failed on object creation: " + e);
    }
  }
}
