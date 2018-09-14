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

package com.sun.ts.tests.jaxrpc.ee.w2j.rpc.encoded.marshalltest;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import java.io.*;
import java.net.*;
import java.rmi.*;

import javax.xml.rpc.*;

import java.util.Properties;
import java.util.GregorianCalendar;
import java.util.Calendar;

import java.math.BigInteger;
import java.math.BigDecimal;

import javax.xml.namespace.QName;

import com.sun.javatest.Status;

import com.sun.ts.tests.jaxrpc.common.*;

import javax.naming.InitialContext;

public class Client extends ServiceEETest {

  // The webserver defaults (overidden by harness properties)
  private static final String PROTOCOL = "http";

  private static final String HOSTNAME = "localhost";

  private static final int PORTNUM = 8000;

  // The webserver host and port property names (harness properties)
  private static final String WEBSERVERHOSTPROP = "webServerHost";

  private static final String WEBSERVERPORTPROP = "webServerPort";

  private static final String MODEPROP = "platform.mode";

  String modeProperty = null; // platform.mode -> (standalone|javaEE)

  private static final String PKG_NAME = "com.sun.ts.tests.jaxrpc.ee.w2j.rpc.encoded.marshalltest.";

  // A Struct value class
  private NilTrueStruct NilTrueStruct_data[];

  private NilFalseStruct NilFalseStruct_data[];

  private AllStruct AllStruct_data[];

  private AllStruct2 AllStruct2_data[];

  private SequenceStruct SequenceStruct_data[];

  private SequenceStruct2 SequenceStruct2_data[];

  public EnumString[] EnumString_data = { null, null };

  public EnumInteger[] EnumInteger_data = { null, null };

  public EnumInt[] EnumInt_data = { null, null };

  public EnumLong[] EnumLong_data = { null, null };

  public EnumShort[] EnumShort_data = { null, null };

  public EnumDecimal[] EnumDecimal_data = { null, null };

  public EnumFloat[] EnumFloat_data = { null, null };

  public EnumDouble[] EnumDouble_data = { null, null };

  public EnumByte[] EnumByte_data = { null, null };

  private TSURL ctsurl = new TSURL();

  private Properties props = null;

  private String hostname = HOSTNAME;

  private int portnum = PORTNUM;

  // URL properties used by the test
  private static final String ENDPOINT_URL = "w2jmarshalltest.endpoint.1";

  private static final String WSDLLOC_URL = "w2jmarshalltest.wsdlloc.1";

  private String url = null;

  private URL wsdlurl = null;

  private void getTestURLs() throws Exception {
    TestUtil.logMsg("Get URL's used by the test");
    String file = JAXRPC_Util.getURLFromProp(ENDPOINT_URL);
    url = ctsurl.getURLString(PROTOCOL, hostname, portnum, file);
    file = JAXRPC_Util.getURLFromProp(WSDLLOC_URL);
    wsdlurl = ctsurl.getURL(PROTOCOL, hostname, portnum, file);
    TestUtil.logMsg("Service Endpoint URL: " + url);
    TestUtil.logMsg("WSDL Location URL:    " + wsdlurl);
  }

  // Get Port and Stub access via porting layer interface
  MarshallTest port = null;

  Stub stub = null;

  private void getStubStandalone() throws Exception {
    TestUtil.logMsg("Get stub from service implementation class"
        + " using JAXRPC porting instance");
    port = (MarshallTest) JAXRPC_Util
        .getStub("com.sun.ts.tests.jaxrpc.ee.w2j.rpc.encoded."
            + "marshalltest.MarshallTestService", "getMarshallTestPort");
    TestUtil.logMsg("Cast stub to base Stub class ...");
    stub = (javax.xml.rpc.Stub) port;
  }

  private void getStub() throws Exception {
    TestUtil.logMsg("JNDI lookup for Service1");
    InitialContext ctx = new InitialContext();
    TestUtil.logMsg("Lookup java:comp/env/service/w2jremarshalltest");
    Service svc = (javax.xml.rpc.Service) ctx
        .lookup("java:comp/env/service/w2jremarshalltest");
    TestUtil.logMsg("Get port from Service1");
    port = (MarshallTest) svc.getPort(
        com.sun.ts.tests.jaxrpc.ee.w2j.rpc.encoded.marshalltest.MarshallTest.class);
    TestUtil.logMsg("Port obtained");
  }

  private void init_structs() {
    NilTrueStruct_data = new NilTrueStruct[2];
    NilTrueStruct_data[0] = new NilTrueStruct();
    NilTrueStruct_data[1] = new NilTrueStruct();
    NilTrueStruct_data[0].setNilint(new Integer(Integer.MIN_VALUE));
    NilTrueStruct_data[0].setNillong(new Long(Long.MIN_VALUE));
    NilTrueStruct_data[0].setNilshort(new Short(Short.MIN_VALUE));
    NilTrueStruct_data[0].setNilfloat(new Float(Float.MIN_VALUE));
    NilTrueStruct_data[0].setNildouble(new Double(Double.MIN_VALUE));
    NilTrueStruct_data[0].setNilboolean(new Boolean("false"));
    NilTrueStruct_data[0].setNilbyte(new Byte(Byte.MIN_VALUE));

    NilTrueStruct_data[1].setNilint(null);
    NilTrueStruct_data[1].setNillong(null);
    NilTrueStruct_data[1].setNilshort(null);
    NilTrueStruct_data[1].setNilfloat(null);
    NilTrueStruct_data[1].setNildouble(null);
    NilTrueStruct_data[1].setNilboolean(null);
    NilTrueStruct_data[1].setNilbyte(null);

    NilFalseStruct_data = new NilFalseStruct[1];
    NilFalseStruct_data[0] = new NilFalseStruct();
    NilFalseStruct_data[0].setNilint(Integer.MIN_VALUE);
    NilFalseStruct_data[0].setNillong(Long.MIN_VALUE);
    NilFalseStruct_data[0].setNilshort(Short.MIN_VALUE);
    NilFalseStruct_data[0].setNilfloat(Float.MIN_VALUE);
    NilFalseStruct_data[0].setNildouble(Double.MIN_VALUE);
    NilFalseStruct_data[0].setNilboolean(false);
    NilFalseStruct_data[0].setNilbyte(Byte.MIN_VALUE);

    AllStruct_data = new AllStruct[2];
    AllStruct_data[0] = new AllStruct();
    AllStruct_data[1] = new AllStruct();
    AllStruct_data[0].setVarString("String1");
    AllStruct_data[0].setVarInteger(new BigInteger("3512359"));
    AllStruct_data[0].setVarInt((int) Integer.MIN_VALUE);
    AllStruct_data[0].setVarLong((long) Long.MIN_VALUE);
    AllStruct_data[0].setVarShort((short) Short.MIN_VALUE);
    AllStruct_data[0].setVarDecimal(new BigDecimal("3512359.1456"));
    AllStruct_data[0].setVarFloat((float) Float.MIN_VALUE);
    AllStruct_data[0].setVarDouble((double) Double.MIN_VALUE);
    AllStruct_data[0].setVarBoolean(false);
    AllStruct_data[0].setVarByte((byte) Byte.MIN_VALUE);
    AllStruct_data[0].setVarQName(new QName("String2"));
    AllStruct_data[0].setVarDateTime(new GregorianCalendar(96, 5, 1));
    AllStruct_data[0].setVarSoapString("String3");
    AllStruct_data[0].setVarSoapBoolean(new Boolean("false"));
    AllStruct_data[0].setVarSoapFloat(new Float(Float.MIN_VALUE));
    AllStruct_data[0].setVarSoapDouble(new Double(Double.MIN_VALUE));
    AllStruct_data[0].setVarSoapDecimal(new BigDecimal("3512359.1111"));
    AllStruct_data[0].setVarSoapInt(new Integer(Integer.MIN_VALUE));
    AllStruct_data[0].setVarSoapShort(new Short(Short.MIN_VALUE));
    AllStruct_data[0].setVarSoapByte(new Byte(Byte.MIN_VALUE));
    AllStruct_data[0].setVarBase64Binary(JAXRPC_Data.byte_data);
    AllStruct_data[0].setVarHexBinary(JAXRPC_Data.byte_data);
    AllStruct_data[0].setVarSoapBase64(JAXRPC_Data.byte_data);

    AllStruct_data[1].setVarString("String4");
    AllStruct_data[1].setVarInteger(new BigInteger("3512360"));
    AllStruct_data[1].setVarInt((int) Integer.MAX_VALUE);
    AllStruct_data[1].setVarLong((long) Long.MAX_VALUE);
    AllStruct_data[1].setVarShort((short) Short.MAX_VALUE);
    AllStruct_data[1].setVarDecimal(new BigDecimal("3512360.1456"));
    AllStruct_data[1].setVarFloat((float) Float.MAX_VALUE);
    AllStruct_data[1].setVarDouble((double) Double.MAX_VALUE);
    AllStruct_data[1].setVarBoolean(true);
    AllStruct_data[1].setVarByte((byte) Byte.MAX_VALUE);
    AllStruct_data[1].setVarQName(new QName("String5"));
    AllStruct_data[1].setVarDateTime(new GregorianCalendar(96, 5, 2));
    AllStruct_data[1].setVarSoapString("String6");
    AllStruct_data[1].setVarSoapBoolean(new Boolean("true"));
    AllStruct_data[1].setVarSoapFloat(new Float(Float.MAX_VALUE));
    AllStruct_data[1].setVarSoapDouble(new Double(Double.MAX_VALUE));
    AllStruct_data[1].setVarSoapDecimal(new BigDecimal("3512360.1111"));
    AllStruct_data[1].setVarSoapInt(new Integer(Integer.MAX_VALUE));
    AllStruct_data[1].setVarSoapShort(new Short(Short.MAX_VALUE));
    AllStruct_data[1].setVarSoapByte(new Byte(Byte.MAX_VALUE));
    AllStruct_data[1].setVarBase64Binary(JAXRPC_Data.byte_data);
    AllStruct_data[1].setVarHexBinary(JAXRPC_Data.byte_data);
    AllStruct_data[1].setVarSoapBase64(JAXRPC_Data.byte_data);

    AllStruct2_data = new AllStruct2[2];
    AllStruct2_data[0] = new AllStruct2();
    AllStruct2_data[1] = new AllStruct2();
    AllStruct2_data[0].setVarString("String1");
    AllStruct2_data[0].setVarInteger(new BigInteger("3512359"));
    AllStruct2_data[0].setVarInt((int) Integer.MIN_VALUE);
    AllStruct2_data[0].setVarLong((long) Long.MIN_VALUE);
    AllStruct2_data[0].setVarShort((short) Short.MIN_VALUE);
    AllStruct2_data[0].setVarDecimal(new BigDecimal("3512359.1456"));
    AllStruct2_data[0].setVarFloat((float) Float.MIN_VALUE);
    AllStruct2_data[0].setVarDouble((double) Double.MIN_VALUE);
    AllStruct2_data[0].setVarBoolean(false);
    AllStruct2_data[0].setVarByte((byte) Byte.MIN_VALUE);
    AllStruct2_data[0].setVarQName(new QName("String2"));
    AllStruct2_data[0].setVarDateTime(new GregorianCalendar(96, 5, 3));
    AllStruct2_data[0].setVarSoapString("String3");
    AllStruct2_data[0].setVarSoapBoolean(new Boolean("false"));
    AllStruct2_data[0].setVarSoapFloat(new Float(Float.MIN_VALUE));
    AllStruct2_data[0].setVarSoapDouble(new Double(Double.MIN_VALUE));
    AllStruct2_data[0].setVarSoapDecimal(new BigDecimal("3512359.1111"));
    AllStruct2_data[0].setVarSoapInt(new Integer(Integer.MIN_VALUE));
    AllStruct2_data[0].setVarSoapShort(new Short(Short.MIN_VALUE));
    AllStruct2_data[0].setVarSoapByte(new Byte(Byte.MIN_VALUE));
    AllStruct2_data[0].setVarBase64Binary(JAXRPC_Data.byte_data);
    AllStruct2_data[0].setVarHexBinary(JAXRPC_Data.byte_data);
    AllStruct2_data[0].setVarSoapBase64(JAXRPC_Data.byte_data);
    AllStruct2_data[0].setVarAllStruct(AllStruct_data[0]);

    AllStruct2_data[1].setVarString("String4");
    AllStruct2_data[1].setVarInteger(new BigInteger("3512360"));
    AllStruct2_data[1].setVarInt((int) Integer.MAX_VALUE);
    AllStruct2_data[1].setVarLong((long) Long.MAX_VALUE);
    AllStruct2_data[1].setVarShort((short) Short.MAX_VALUE);
    AllStruct2_data[1].setVarDecimal(new BigDecimal("3512360.1456"));
    AllStruct2_data[1].setVarFloat((float) Float.MAX_VALUE);
    AllStruct2_data[1].setVarDouble((double) Double.MAX_VALUE);
    AllStruct2_data[1].setVarBoolean(true);
    AllStruct2_data[1].setVarByte((byte) Byte.MAX_VALUE);
    AllStruct2_data[1].setVarQName(new QName("String5"));
    AllStruct2_data[1].setVarDateTime(new GregorianCalendar(96, 5, 5));
    AllStruct2_data[1].setVarSoapString("String6");
    AllStruct2_data[1].setVarSoapBoolean(new Boolean("false"));
    AllStruct2_data[1].setVarSoapFloat(new Float(Float.MAX_VALUE));
    AllStruct2_data[1].setVarSoapDouble(new Double(Double.MAX_VALUE));
    AllStruct2_data[1].setVarSoapDecimal(new BigDecimal("3512360.1111"));
    AllStruct2_data[1].setVarSoapInt(new Integer(Integer.MAX_VALUE));
    AllStruct2_data[1].setVarSoapShort(new Short(Short.MAX_VALUE));
    AllStruct2_data[1].setVarSoapByte(new Byte(Byte.MAX_VALUE));
    AllStruct2_data[1].setVarBase64Binary(JAXRPC_Data.byte_data);
    AllStruct2_data[1].setVarHexBinary(JAXRPC_Data.byte_data);
    AllStruct2_data[1].setVarSoapBase64(JAXRPC_Data.byte_data);
    AllStruct2_data[1].setVarAllStruct(AllStruct_data[1]);

    SequenceStruct_data = new SequenceStruct[2];
    SequenceStruct_data[0] = new SequenceStruct();
    SequenceStruct_data[1] = new SequenceStruct();
    SequenceStruct_data[0].setVarString("String1");
    SequenceStruct_data[0].setVarInteger(new BigInteger("3512359"));
    SequenceStruct_data[0].setVarInt((int) Integer.MIN_VALUE);
    SequenceStruct_data[0].setVarLong((long) Long.MIN_VALUE);
    SequenceStruct_data[0].setVarShort((short) Short.MIN_VALUE);
    SequenceStruct_data[0].setVarDecimal(new BigDecimal("3512359.1456"));
    SequenceStruct_data[0].setVarFloat((float) Float.MIN_VALUE);
    SequenceStruct_data[0].setVarDouble((double) Double.MIN_VALUE);
    SequenceStruct_data[0].setVarBoolean(false);
    SequenceStruct_data[0].setVarByte((byte) Byte.MIN_VALUE);
    SequenceStruct_data[0].setVarQName(new QName("String2"));
    SequenceStruct_data[0].setVarDateTime(new GregorianCalendar(96, 5, 1));
    SequenceStruct_data[0].setVarSoapString("String3");
    SequenceStruct_data[0].setVarSoapBoolean(new Boolean("false"));
    SequenceStruct_data[0].setVarSoapFloat(new Float(Float.MIN_VALUE));
    SequenceStruct_data[0].setVarSoapDouble(new Double(Double.MIN_VALUE));
    SequenceStruct_data[0].setVarSoapDecimal(new BigDecimal("3512359.1111"));
    SequenceStruct_data[0].setVarSoapInt(new Integer(Integer.MIN_VALUE));
    SequenceStruct_data[0].setVarSoapShort(new Short(Short.MIN_VALUE));
    SequenceStruct_data[0].setVarSoapByte(new Byte(Byte.MIN_VALUE));
    SequenceStruct_data[0].setVarBase64Binary(JAXRPC_Data.byte_data);
    SequenceStruct_data[0].setVarHexBinary(JAXRPC_Data.byte_data);
    SequenceStruct_data[0].setVarSoapBase64(JAXRPC_Data.byte_data);
    SequenceStruct_data[1].setVarString("String4");
    SequenceStruct_data[1].setVarInteger(new BigInteger("3512360"));
    SequenceStruct_data[1].setVarInt((int) Integer.MAX_VALUE);
    SequenceStruct_data[1].setVarLong((long) Long.MAX_VALUE);
    SequenceStruct_data[1].setVarShort((short) Short.MAX_VALUE);
    SequenceStruct_data[1].setVarDecimal(new BigDecimal("3512360.1456"));
    SequenceStruct_data[1].setVarFloat((float) Float.MAX_VALUE);
    SequenceStruct_data[1].setVarDouble((double) Double.MAX_VALUE);
    SequenceStruct_data[1].setVarBoolean(true);
    SequenceStruct_data[1].setVarByte((byte) Byte.MAX_VALUE);
    SequenceStruct_data[1].setVarQName(new QName("String5"));
    SequenceStruct_data[1].setVarDateTime(new GregorianCalendar(96, 5, 2));
    SequenceStruct_data[1].setVarSoapString("String6");
    SequenceStruct_data[1].setVarSoapBoolean(new Boolean("true"));
    SequenceStruct_data[1].setVarSoapFloat(new Float(Float.MAX_VALUE));
    SequenceStruct_data[1].setVarSoapDouble(new Double(Double.MAX_VALUE));
    SequenceStruct_data[1].setVarSoapDecimal(new BigDecimal("3512360.1111"));
    SequenceStruct_data[1].setVarSoapInt(new Integer(Integer.MAX_VALUE));
    SequenceStruct_data[1].setVarSoapShort(new Short(Short.MAX_VALUE));
    SequenceStruct_data[1].setVarSoapByte(new Byte(Byte.MAX_VALUE));
    SequenceStruct_data[1].setVarBase64Binary(JAXRPC_Data.byte_data);
    SequenceStruct_data[1].setVarHexBinary(JAXRPC_Data.byte_data);
    SequenceStruct_data[1].setVarSoapBase64(JAXRPC_Data.byte_data);

    SequenceStruct2_data = new SequenceStruct2[2];
    SequenceStruct2_data[0] = new SequenceStruct2();
    SequenceStruct2_data[1] = new SequenceStruct2();
    SequenceStruct2_data[0].setVarString("String1");
    SequenceStruct2_data[0].setVarInteger(new BigInteger("3512359"));
    SequenceStruct2_data[0].setVarInt((int) Integer.MIN_VALUE);
    SequenceStruct2_data[0].setVarLong((long) Long.MIN_VALUE);
    SequenceStruct2_data[0].setVarShort((short) Short.MIN_VALUE);
    SequenceStruct2_data[0].setVarDecimal(new BigDecimal("3512359.1456"));
    SequenceStruct2_data[0].setVarFloat((float) Float.MIN_VALUE);
    SequenceStruct2_data[0].setVarDouble((double) Double.MIN_VALUE);
    SequenceStruct2_data[0].setVarBoolean(false);
    SequenceStruct2_data[0].setVarByte((byte) Byte.MIN_VALUE);
    SequenceStruct2_data[0].setVarQName(new QName("String2"));
    SequenceStruct2_data[0].setVarDateTime(new GregorianCalendar(96, 5, 3));
    SequenceStruct2_data[0].setVarSoapString("String3");
    SequenceStruct2_data[0].setVarSoapBoolean(new Boolean("false"));
    SequenceStruct2_data[0].setVarSoapFloat(new Float(Float.MIN_VALUE));
    SequenceStruct2_data[0].setVarSoapDouble(new Double(Double.MIN_VALUE));
    SequenceStruct2_data[0].setVarSoapDecimal(new BigDecimal("3512359.1111"));
    SequenceStruct2_data[0].setVarSoapInt(new Integer(Integer.MIN_VALUE));
    SequenceStruct2_data[0].setVarSoapShort(new Short(Short.MIN_VALUE));
    SequenceStruct2_data[0].setVarSoapByte(new Byte(Byte.MIN_VALUE));
    SequenceStruct2_data[0].setVarBase64Binary(JAXRPC_Data.byte_data);
    SequenceStruct2_data[0].setVarHexBinary(JAXRPC_Data.byte_data);
    SequenceStruct2_data[0].setVarSoapBase64(JAXRPC_Data.byte_data);
    SequenceStruct2_data[0].setVarSequenceStruct(SequenceStruct_data[0]);

    SequenceStruct2_data[1].setVarString("String4");
    SequenceStruct2_data[1].setVarInteger(new BigInteger("3512360"));
    SequenceStruct2_data[1].setVarInt((int) Integer.MAX_VALUE);
    SequenceStruct2_data[1].setVarLong((long) Long.MAX_VALUE);
    SequenceStruct2_data[1].setVarShort((short) Short.MAX_VALUE);
    SequenceStruct2_data[1].setVarDecimal(new BigDecimal("3512360.1456"));
    SequenceStruct2_data[1].setVarFloat((float) Float.MAX_VALUE);
    SequenceStruct2_data[1].setVarDouble((double) Double.MAX_VALUE);
    SequenceStruct2_data[1].setVarBoolean(true);
    SequenceStruct2_data[1].setVarByte((byte) Byte.MAX_VALUE);
    SequenceStruct2_data[1].setVarQName(new QName("String5"));
    SequenceStruct2_data[1].setVarDateTime(new GregorianCalendar(96, 5, 5));
    SequenceStruct2_data[1].setVarSoapString("String6");
    SequenceStruct2_data[1].setVarSoapBoolean(new Boolean("false"));
    SequenceStruct2_data[1].setVarSoapFloat(new Float(Float.MAX_VALUE));
    SequenceStruct2_data[1].setVarSoapDouble(new Double(Double.MAX_VALUE));
    SequenceStruct2_data[1].setVarSoapDecimal(new BigDecimal("3512360.1111"));
    SequenceStruct2_data[1].setVarSoapInt(new Integer(Integer.MAX_VALUE));
    SequenceStruct2_data[1].setVarSoapShort(new Short(Short.MAX_VALUE));
    SequenceStruct2_data[1].setVarSoapByte(new Byte(Byte.MAX_VALUE));
    SequenceStruct2_data[1].setVarBase64Binary(JAXRPC_Data.byte_data);
    SequenceStruct2_data[1].setVarHexBinary(JAXRPC_Data.byte_data);
    SequenceStruct2_data[1].setVarSoapBase64(JAXRPC_Data.byte_data);
    SequenceStruct2_data[1].setVarSequenceStruct(SequenceStruct_data[1]);
  }

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /* Test setup */

  /*
   * @class.testArgs: -ap jaxrpc-url-props.dat
   * 
   * @class.setup_props: webServerHost; webServerPort; platform.mode;
   */

  public void setup(String[] args, Properties p) throws Fault {
    props = p;
    boolean pass = true;

    try {
      hostname = p.getProperty(WEBSERVERHOSTPROP);

      if (hostname == null)
        pass = false;
      else if (hostname.equals(""))
        pass = false;

      try {
        portnum = Integer.parseInt(p.getProperty(WEBSERVERPORTPROP));
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        pass = false;
      }

      modeProperty = p.getProperty(MODEPROP);
      if (modeProperty.equals("standalone")) {
        getTestURLs();
        getStubStandalone();
        TestUtil.logMsg("Setting target endpoint to " + url + " ...");
        stub._setProperty(Stub.ENDPOINT_ADDRESS_PROPERTY, url);
      } else {
        getStub();
      }

    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("setup failed:", e);
    }

    if (!pass) {
      TestUtil.logErr(
          "Please specify host & port of web server " + "in config properties: "
              + WEBSERVERHOSTPROP + ", " + WEBSERVERPORTPROP);
      throw new Fault("setup failed:");
    }

    init_structs();

    logMsg("setup ok");
  }

  public void cleanup() throws Fault {
    logMsg("cleanup ok");
  }

  private void printSeperationLine() {
    TestUtil.logMsg("---------------------------");
  }

  private String AllStructToString(AllStruct s) {
    return "varString=" + s.getVarString() + ", varInteger=" + s.getVarInteger()
        + ", varInt=" + s.getVarInt() + ", varLong=" + s.getVarLong()
        + ", varShort=" + s.getVarShort() + ", varDecimal=" + s.getVarDecimal()
        + ", varFloat=" + s.getVarFloat() + ", varDouble=" + s.getVarDouble()
        + ", varBoolean=" + s.isVarBoolean() + ", varByte=" + s.getVarByte()
        + ", varQName=" + s.getVarQName() + ", varDateTime="
        + s.getVarDateTime() + ", varSoapString=" + s.getVarSoapString()
        + ", varSoapBoolean=" + s.getVarSoapBoolean() + ", varSoapFloat="
        + s.getVarSoapFloat() + ", varSoapDouble=" + s.getVarSoapDouble()
        + ", varSoapDecimal=" + s.getVarSoapDecimal() + ", varSoapInt="
        + s.getVarSoapInt() + ", varSoapShort=" + s.getVarSoapShort()
        + ", varSoapByte=" + s.getVarSoapByte() + ", varBase64Binary="
        + JAXRPC_Data.returnArrayValues(s.getVarBase64Binary(), "byte")
        + ", varHexBinary="
        + JAXRPC_Data.returnArrayValues(s.getVarHexBinary(), "byte")
        + ", varSoapBase64="
        + JAXRPC_Data.returnArrayValues(s.getVarSoapBase64(), "byte");

  }

  private String AllStruct2ToString(AllStruct2 s) {
    return "varString=" + s.getVarString() + ", varInteger=" + s.getVarInteger()
        + ", varInt=" + s.getVarInt() + ", varLong=" + s.getVarLong()
        + ", varShort=" + s.getVarShort() + ", varDecimal=" + s.getVarDecimal()
        + ", varFloat=" + s.getVarFloat() + ", varDouble=" + s.getVarDouble()
        + ", varBoolean=" + s.isVarBoolean() + ", varByte=" + s.getVarByte()
        + ", varQName=" + s.getVarQName() + ", varDateTime="
        + s.getVarDateTime() + ", varSoapString=" + s.getVarSoapString()
        + ", varSoapBoolean=" + s.getVarSoapBoolean() + ", varSoapFloat="
        + s.getVarSoapFloat() + ", varSoapDouble=" + s.getVarSoapDouble()
        + ", varSoapDecimal=" + s.getVarSoapDecimal() + ", varSoapInt="
        + s.getVarSoapInt() + ", varSoapShort=" + s.getVarSoapShort()
        + ", varSoapByte=" + s.getVarSoapByte() + ", varBase64Binary="
        + JAXRPC_Data.returnArrayValues(s.getVarBase64Binary(), "byte")
        + ", varHexBinary="
        + JAXRPC_Data.returnArrayValues(s.getVarHexBinary(), "byte")
        + ", varSoapBase64="
        + JAXRPC_Data.returnArrayValues(s.getVarSoapBase64(), "byte")
        + ", varAllStruct=" + AllStructToString(s.getVarAllStruct());
  }

  private String SequenceStructToString(SequenceStruct s) {
    return "varString=" + s.getVarString() + ", varInteger=" + s.getVarInteger()
        + ", varInt=" + s.getVarInt() + ", varLong=" + s.getVarLong()
        + ", varShort=" + s.getVarShort() + ", varDecimal=" + s.getVarDecimal()
        + ", varFloat=" + s.getVarFloat() + ", varDouble=" + s.getVarDouble()
        + ", varBoolean=" + s.isVarBoolean() + ", varByte=" + s.getVarByte()
        + ", varQName=" + s.getVarQName() + ", varDateTime="
        + s.getVarDateTime() + ", varSoapString=" + s.getVarSoapString()
        + ", varSoapBoolean=" + s.getVarSoapBoolean() + ", varSoapFloat="
        + s.getVarSoapFloat() + ", varSoapDouble=" + s.getVarSoapDouble()
        + ", varSoapDecimal=" + s.getVarSoapDecimal() + ", varSoapInt="
        + s.getVarSoapInt() + ", varSoapShort=" + s.getVarSoapShort()
        + ", varSoapByte=" + s.getVarSoapByte() + ", varBase64Binary="
        + JAXRPC_Data.returnArrayValues(s.getVarBase64Binary(), "byte")
        + ", varHexBinary="
        + JAXRPC_Data.returnArrayValues(s.getVarHexBinary(), "byte")
        + ", varSoapBase64="
        + JAXRPC_Data.returnArrayValues(s.getVarSoapBase64(), "byte");
  }

  private String SequenceStruct2ToString(SequenceStruct2 s) {
    return "varString=" + s.getVarString() + ", varInteger=" + s.getVarInteger()
        + ", varInt=" + s.getVarInt() + ", varLong=" + s.getVarLong()
        + ", varShort=" + s.getVarShort() + ", varDecimal=" + s.getVarDecimal()
        + ", varFloat=" + s.getVarFloat() + ", varDouble=" + s.getVarDouble()
        + ", varBoolean=" + s.isVarBoolean() + ", varByte=" + s.getVarByte()
        + ", varQName=" + s.getVarQName() + ", varDateTime="
        + s.getVarDateTime() + ", varSoapString=" + s.getVarSoapString()
        + ", varSoapBoolean=" + s.getVarSoapBoolean() + ", varSoapFloat="
        + s.getVarSoapFloat() + ", varSoapDouble=" + s.getVarSoapDouble()
        + ", varSoapDecimal=" + s.getVarSoapDecimal() + ", varSoapInt="
        + s.getVarSoapInt() + ", varSoapShort=" + s.getVarSoapShort()
        + ", varSoapByte=" + s.getVarSoapByte() + ", varBase64Binary="
        + JAXRPC_Data.returnArrayValues(s.getVarBase64Binary(), "byte")
        + ", varHexBinary="
        + JAXRPC_Data.returnArrayValues(s.getVarHexBinary(), "byte")
        + ", varSoapBase64="
        + JAXRPC_Data.returnArrayValues(s.getVarSoapBase64(), "byte")
        + ", varSequenceStruct="
        + SequenceStructToString(s.getVarSequenceStruct());
  }

  private boolean compareNilTrueStruct(NilTrueStruct response,
      NilTrueStruct expected) {
    boolean pass = true;

    if (!(expected.getNilint() == null && response.getNilint() == null)) {
      if (!(response.getNilint().equals(expected.getNilint())))
        pass = false;
    }
    if (!(expected.getNillong() == null && response.getNillong() == null)) {
      if (!(response.getNillong().equals(expected.getNillong())))
        pass = false;
    }
    if (!(expected.getNilshort() == null && response.getNilshort() == null)) {
      if (!(response.getNilshort().equals(expected.getNilshort())))
        pass = false;
    }
    if (!(expected.getNilfloat() == null && response.getNilfloat() == null)) {
      if (!(response.getNilfloat().equals(expected.getNilfloat())))
        pass = false;
    }
    if (!(expected.getNildouble() == null && response.getNildouble() == null)) {
      if (!(response.getNildouble().equals(expected.getNildouble())))
        pass = false;
    }
    if (!(expected.getNilboolean() == null
        && response.getNilboolean() == null)) {
      if (!(response.getNilboolean().equals(expected.getNilboolean())))
        pass = false;
    }
    if (!(expected.getNilbyte() == null && response.getNilbyte() == null)) {
      if (!(response.getNilbyte().equals(expected.getNilbyte())))
        pass = false;
    }
    return pass;
  }

  private boolean compareNilFalseStruct(NilFalseStruct response,
      NilFalseStruct expected) {
    boolean pass = true;
    if (response.getNilint() != expected.getNilint())
      pass = false;
    if (response.getNillong() != expected.getNillong())
      pass = false;
    if (response.getNilshort() != expected.getNilshort())
      pass = false;
    if (response.getNilfloat() != expected.getNilfloat())
      pass = false;
    if (response.getNildouble() != expected.getNildouble())
      pass = false;
    if (response.isNilboolean() != expected.isNilboolean())
      pass = false;
    if (response.getNilbyte() != expected.getNilbyte())
      pass = false;
    return pass;
  }

  private boolean compareAllStruct(AllStruct response, AllStruct expected) {
    boolean pass = true;
    if (!response.getVarString().equals(expected.getVarString())) {
      TestUtil.logMsg("String failed comparison");
      pass = false;
    }
    if (!response.getVarInteger().equals(expected.getVarInteger())) {
      TestUtil.logMsg("Integer failed comparison");
      pass = false;
    }
    if (response.getVarInt() != expected.getVarInt()) {
      TestUtil.logMsg("Int failed comparison");
      pass = false;
    }
    if (response.getVarLong() != expected.getVarLong()) {
      TestUtil.logMsg("Long failed comparison");
      pass = false;
    }
    if (response.getVarShort() != expected.getVarShort()) {
      TestUtil.logMsg("Short failed comparison");
      pass = false;
    }
    if (!response.getVarDecimal().equals(expected.getVarDecimal())) {
      TestUtil.logMsg("Decimal failed comparison");
      pass = false;
    }
    if (response.getVarFloat() != expected.getVarFloat()) {
      TestUtil.logMsg("Float failed comparison");
      pass = false;
    }
    if (response.getVarDouble() != expected.getVarDouble()) {
      TestUtil.logMsg("Double failed comparison");
      pass = false;
    }
    if (response.isVarBoolean() != expected.isVarBoolean()) {
      TestUtil.logMsg("Boolean failed comparison");
      pass = false;
    }
    if (response.getVarByte() != expected.getVarByte()) {
      TestUtil.logMsg("Byte failed comparison");
      pass = false;
    }
    if (!response.getVarQName().equals(expected.getVarQName())) {
      TestUtil.logMsg("QName failed comparison");
      pass = false;
    }
    if (!JAXRPC_Data.compareCalendars(response.getVarDateTime(),
        expected.getVarDateTime())) {
      TestUtil.logMsg("Calendar failed comparison");
      pass = false;
    }
    if (!response.getVarSoapString().equals(expected.getVarSoapString())) {
      TestUtil.logMsg("String failed comparison");
      pass = false;
    }
    if (!response.getVarSoapBoolean().equals(expected.getVarSoapBoolean())) {
      TestUtil.logMsg("SoapBoolean failed comparison");
      pass = false;
    }
    if (!response.getVarSoapFloat().equals(expected.getVarSoapFloat())) {
      TestUtil.logMsg("SoapFLoat failed comparison");
      pass = false;
    }
    if (!response.getVarSoapDouble().equals(expected.getVarSoapDouble())) {
      TestUtil.logMsg("SoapDouble failed comparison");
      pass = false;
    }
    if (!response.getVarSoapDecimal().equals(expected.getVarSoapDecimal())) {
      TestUtil.logMsg("SoapDecimal failed comparison");
      pass = false;
    }
    if (!response.getVarSoapInt().equals(expected.getVarSoapInt())) {
      TestUtil.logMsg("SoapInt failed comparison");
      pass = false;
    }
    if (!response.getVarSoapShort().equals(expected.getVarSoapShort())) {
      TestUtil.logMsg("SoapShort failed comparison");
      pass = false;
    }
    if (!response.getVarSoapByte().equals(expected.getVarSoapByte())) {
      TestUtil.logMsg("SoapByte failed comparison");
      pass = false;
    }
    if (!JAXRPC_Data.compareArrayValues(expected.getVarBase64Binary(),
        response.getVarBase64Binary(), "byte")) {
      TestUtil.logMsg("Base64Binary failed comparison");
      pass = false;
    }
    if (!JAXRPC_Data.compareArrayValues(expected.getVarHexBinary(),
        response.getVarHexBinary(), "byte")) {
      TestUtil.logMsg("HexBinary failed comparison");
      pass = false;
    }
    if (!JAXRPC_Data.compareArrayValues(expected.getVarSoapBase64(),
        response.getVarSoapBase64(), "byte")) {
      TestUtil.logMsg("SoapBase64 failed comparison");
      pass = false;
    }
    if (pass)
      return true;
    else {
      return false;
    }
  }

  private boolean compareAllStruct2(AllStruct2 response, AllStruct2 expected) {
    boolean pass = true;
    if (!response.getVarString().equals(expected.getVarString())) {
      TestUtil.logMsg("String failed comparison");
      pass = false;
    }
    if (!response.getVarInteger().equals(expected.getVarInteger())) {
      TestUtil.logMsg("Integer failed comparison");
      pass = false;
    }
    if (response.getVarInt() != expected.getVarInt()) {
      TestUtil.logMsg("Int failed comparison");
      pass = false;
    }
    if (response.getVarLong() != expected.getVarLong()) {
      TestUtil.logMsg("Long failed comparison");
      pass = false;
    }
    if (response.getVarShort() != expected.getVarShort()) {
      TestUtil.logMsg("Short failed comparison");
      pass = false;
    }
    if (!response.getVarDecimal().equals(expected.getVarDecimal())) {
      TestUtil.logMsg("Decimal failed comparison");
      pass = false;
    }
    if (response.getVarFloat() != expected.getVarFloat()) {
      TestUtil.logMsg("Float failed comparison");
      pass = false;
    }
    if (response.getVarDouble() != expected.getVarDouble()) {
      TestUtil.logMsg("Double failed comparison");
      pass = false;
    }
    if (response.isVarBoolean() != expected.isVarBoolean()) {
      TestUtil.logMsg("Boolean failed comparison");
      pass = false;
    }
    if (response.getVarByte() != expected.getVarByte()) {
      TestUtil.logMsg("Byte failed comparison");
      pass = false;
    }
    if (!response.getVarQName().equals(expected.getVarQName())) {
      TestUtil.logMsg("QName failed comparison");
      pass = false;
    }
    if (!JAXRPC_Data.compareCalendars(response.getVarDateTime(),
        expected.getVarDateTime())) {
      TestUtil.logMsg("Calendar failed comparison");
      pass = false;
    }
    if (!response.getVarSoapString().equals(expected.getVarSoapString())) {
      TestUtil.logMsg("String failed comparison");
      pass = false;
    }
    if (!response.getVarSoapBoolean().equals(expected.getVarSoapBoolean())) {
      TestUtil.logMsg("SoapBoolean failed comparison");
      pass = false;
    }
    if (!response.getVarSoapFloat().equals(expected.getVarSoapFloat())) {
      TestUtil.logMsg("SoapFLoat failed comparison");
      pass = false;
    }
    if (!response.getVarSoapDouble().equals(expected.getVarSoapDouble())) {
      TestUtil.logMsg("SoapDouble failed comparison");
      pass = false;
    }
    if (!response.getVarSoapDecimal().equals(expected.getVarSoapDecimal())) {
      TestUtil.logMsg("SoapDecimal failed comparison");
      pass = false;
    }
    if (!response.getVarSoapInt().equals(expected.getVarSoapInt())) {
      TestUtil.logMsg("SoapInt failed comparison");
      pass = false;
    }
    if (!response.getVarSoapShort().equals(expected.getVarSoapShort())) {
      TestUtil.logMsg("SoapShort failed comparison");
      pass = false;
    }
    if (!response.getVarSoapByte().equals(expected.getVarSoapByte())) {
      TestUtil.logMsg("SoapByte failed comparison");
      pass = false;
    }
    if (!JAXRPC_Data.compareArrayValues(expected.getVarBase64Binary(),
        response.getVarBase64Binary(), "byte")) {
      TestUtil.logMsg("Base64Binary failed comparison");
      pass = false;
    }
    if (!JAXRPC_Data.compareArrayValues(expected.getVarHexBinary(),
        response.getVarHexBinary(), "byte")) {
      TestUtil.logMsg("HexBinary failed comparison");
      pass = false;
    }
    if (!JAXRPC_Data.compareArrayValues(expected.getVarSoapBase64(),
        response.getVarSoapBase64(), "byte")) {
      TestUtil.logMsg("SoapBase64 failed comparison");
      pass = false;
    }
    if (!compareAllStruct(response.getVarAllStruct(),
        expected.getVarAllStruct())) {
      TestUtil.logMsg("Struct within a struct failed comparison");
      pass = false;
    }
    if (pass) {
      return true;
    } else {
      return false;
    }
  }

  private boolean compareSequenceStruct(SequenceStruct response,
      SequenceStruct expected) {

    boolean pass = true;
    if (!response.getVarString().equals(expected.getVarString())) {
      TestUtil.logMsg("String failed comparison");
      pass = false;
    }
    if (!response.getVarInteger().equals(expected.getVarInteger())) {
      TestUtil.logMsg("Integer failed comparison");
      pass = false;
    }
    if (response.getVarInt() != expected.getVarInt()) {
      TestUtil.logMsg("Int failed comparison");
      pass = false;
    }
    if (response.getVarLong() != expected.getVarLong()) {
      TestUtil.logMsg("Long failed comparison");
      pass = false;
    }
    if (response.getVarShort() != expected.getVarShort()) {
      TestUtil.logMsg("Short failed comparison");
      pass = false;
    }
    if (!response.getVarDecimal().equals(expected.getVarDecimal())) {
      TestUtil.logMsg("Decimal failed comparison");
      pass = false;
    }
    if (response.getVarFloat() != expected.getVarFloat()) {
      TestUtil.logMsg("Float failed comparison");
      pass = false;
    }
    if (response.getVarDouble() != expected.getVarDouble()) {
      TestUtil.logMsg("Double failed comparison");
      pass = false;
    }
    if (response.isVarBoolean() != expected.isVarBoolean()) {
      TestUtil.logMsg("Boolean failed comparison");
      pass = false;
    }
    if (response.getVarByte() != expected.getVarByte()) {
      TestUtil.logMsg("Byte failed comparison");
      pass = false;
    }
    if (!response.getVarQName().equals(expected.getVarQName())) {
      TestUtil.logMsg("QName failed comparison");
      pass = false;
    }
    if (!JAXRPC_Data.compareCalendars(response.getVarDateTime(),
        expected.getVarDateTime())) {
      TestUtil.logMsg("Calendar failed comparison");
      pass = false;
    }
    if (!response.getVarSoapString().equals(expected.getVarSoapString())) {
      TestUtil.logMsg("String failed comparison");
      pass = false;
    }
    if (!response.getVarSoapBoolean().equals(expected.getVarSoapBoolean())) {
      TestUtil.logMsg("SoapBoolean failed comparison");
      pass = false;
    }
    if (!response.getVarSoapFloat().equals(expected.getVarSoapFloat())) {
      TestUtil.logMsg("SoapFLoat failed comparison");
      pass = false;
    }
    if (!response.getVarSoapDouble().equals(expected.getVarSoapDouble())) {
      TestUtil.logMsg("SoapDouble failed comparison");
      pass = false;
    }
    if (!response.getVarSoapDecimal().equals(expected.getVarSoapDecimal())) {
      TestUtil.logMsg("SoapDecimal failed comparison");
      pass = false;
    }
    if (!response.getVarSoapInt().equals(expected.getVarSoapInt())) {
      TestUtil.logMsg("SoapInt failed comparison");
      pass = false;
    }
    if (!response.getVarSoapShort().equals(expected.getVarSoapShort())) {
      TestUtil.logMsg("SoapShort failed comparison");
      pass = false;
    }
    if (!response.getVarSoapByte().equals(expected.getVarSoapByte())) {
      TestUtil.logMsg("SoapByte failed comparison");
      pass = false;
    }
    if (!JAXRPC_Data.compareArrayValues(expected.getVarBase64Binary(),
        response.getVarBase64Binary(), "byte")) {
      TestUtil.logMsg("Base64Binary failed comparison");
      pass = false;
    }
    if (!JAXRPC_Data.compareArrayValues(expected.getVarHexBinary(),
        response.getVarHexBinary(), "byte")) {
      TestUtil.logMsg("HexBinary failed comparison");
      pass = false;
    }
    if (!JAXRPC_Data.compareArrayValues(expected.getVarSoapBase64(),
        response.getVarSoapBase64(), "byte")) {
      TestUtil.logMsg("SoapBase64 failed comparison");
      pass = false;
    }
    if (pass)
      return true;
    else {
      return false;
    }
  }

  private boolean compareSequenceStruct2(SequenceStruct2 response,
      SequenceStruct2 expected) {

    boolean pass = true;
    if (!response.getVarString().equals(expected.getVarString())) {
      TestUtil.logMsg("String failed comparison");
      pass = false;
    }
    if (!response.getVarInteger().equals(expected.getVarInteger())) {
      TestUtil.logMsg("Integer failed comparison");
      pass = false;
    }
    if (response.getVarInt() != expected.getVarInt()) {
      TestUtil.logMsg("Int failed comparison");
      pass = false;
    }
    if (response.getVarLong() != expected.getVarLong()) {
      TestUtil.logMsg("Long failed comparison");
      pass = false;
    }
    if (response.getVarShort() != expected.getVarShort()) {
      TestUtil.logMsg("Short failed comparison");
      pass = false;
    }
    if (!response.getVarDecimal().equals(expected.getVarDecimal())) {
      TestUtil.logMsg("Decimal failed comparison");
      pass = false;
    }
    if (response.getVarFloat() != expected.getVarFloat()) {
      TestUtil.logMsg("Float failed comparison");
      pass = false;
    }
    if (response.getVarDouble() != expected.getVarDouble()) {
      TestUtil.logMsg("Double failed comparison");
      pass = false;
    }
    if (response.isVarBoolean() != expected.isVarBoolean()) {
      TestUtil.logMsg("Boolean failed comparison");
      pass = false;
    }
    if (response.getVarByte() != expected.getVarByte()) {
      TestUtil.logMsg("Byte failed comparison");
      pass = false;
    }
    if (!response.getVarQName().equals(expected.getVarQName())) {
      TestUtil.logMsg("QName failed comparison");
      pass = false;
    }
    if (!JAXRPC_Data.compareCalendars(response.getVarDateTime(),
        expected.getVarDateTime())) {
      TestUtil.logMsg("Calendar failed comparison");
      pass = false;
    }
    if (!response.getVarSoapString().equals(expected.getVarSoapString())) {
      TestUtil.logMsg("String failed comparison");
      pass = false;
    }
    if (!response.getVarSoapBoolean().equals(expected.getVarSoapBoolean())) {
      TestUtil.logMsg("SoapBoolean failed comparison");
      pass = false;
    }
    if (!response.getVarSoapFloat().equals(expected.getVarSoapFloat())) {
      TestUtil.logMsg("SoapFLoat failed comparison");
      pass = false;
    }
    if (!response.getVarSoapDouble().equals(expected.getVarSoapDouble())) {
      TestUtil.logMsg("SoapDouble failed comparison");
      pass = false;
    }
    if (!response.getVarSoapDecimal().equals(expected.getVarSoapDecimal())) {
      TestUtil.logMsg("SoapDecimal failed comparison");
      pass = false;
    }
    if (!response.getVarSoapInt().equals(expected.getVarSoapInt())) {
      TestUtil.logMsg("SoapInt failed comparison");
      pass = false;
    }
    if (!response.getVarSoapShort().equals(expected.getVarSoapShort())) {
      TestUtil.logMsg("SoapShort failed comparison");
      pass = false;
    }
    if (!response.getVarSoapByte().equals(expected.getVarSoapByte())) {
      TestUtil.logMsg("SoapByte failed comparison");
      pass = false;
    }
    if (!JAXRPC_Data.compareArrayValues(expected.getVarBase64Binary(),
        response.getVarBase64Binary(), "byte")) {
      TestUtil.logMsg("Base64Binary failed comparison");
      pass = false;
    }
    if (!JAXRPC_Data.compareArrayValues(expected.getVarHexBinary(),
        response.getVarHexBinary(), "byte")) {
      TestUtil.logMsg("HexBinary failed comparison");
      pass = false;
    }
    if (!JAXRPC_Data.compareArrayValues(expected.getVarSoapBase64(),
        response.getVarSoapBase64(), "byte")) {
      TestUtil.logMsg("SoapBase64 failed comparison");
      pass = false;
    }
    if (!compareSequenceStruct(response.getVarSequenceStruct(),
        expected.getVarSequenceStruct())) {
      TestUtil.logMsg("Struct within a struct failed comparison");
      pass = false;
    }
    if (pass) {
      return true;
    } else {
      return false;
    }
  }

  private boolean setupEnumData(String s) {
    boolean init = true;
    if (s.equals("EnumString")) {
      try {
        EnumString_data[0] = EnumString.fromValue("String1");
        EnumString_data[1] = EnumString.fromString("String2");
      } catch (IllegalArgumentException e) {
        init = false;
      }
    } else if (s.equals("EnumInteger")) {
      try {
        EnumInteger_data[0] = EnumInteger.fromValue(new BigInteger("3512359"));
        EnumInteger_data[1] = EnumInteger.fromString("3512360");
      } catch (IllegalArgumentException e) {
        init = false;
      }
    } else if (s.equals("EnumInt")) {
      try {
        EnumInt_data[0] = EnumInt.fromValue(Integer.MIN_VALUE);
        EnumInt_data[1] = EnumInt
            .fromString(new Integer(Integer.MAX_VALUE).toString());
      } catch (IllegalStateException e) {
        init = false;
      }
    } else if (s.equals("EnumLong")) {
      try {
        EnumLong_data[0] = EnumLong.fromValue(Long.MIN_VALUE);
        EnumLong_data[1] = EnumLong
            .fromString(new Long(Long.MAX_VALUE).toString());
      } catch (IllegalStateException e) {
        init = false;
      }
    } else if (s.equals("EnumShort")) {
      try {
        EnumShort_data[0] = EnumShort.fromValue(Short.MIN_VALUE);
        EnumShort_data[1] = EnumShort
            .fromString(new Short(Short.MAX_VALUE).toString());
      } catch (IllegalStateException e) {
        init = false;
      }
    } else if (s.equals("EnumDecimal")) {
      try {
        EnumDecimal_data[0] = EnumDecimal
            .fromValue(new BigDecimal("3512359.1456"));
        EnumDecimal_data[1] = EnumDecimal.fromString("3512360.1456");
      } catch (IllegalStateException e) {
        init = false;
      }
    } else if (s.equals("EnumFloat")) {
      try {
        EnumFloat_data[0] = EnumFloat.fromValue((float) -1.00000000);
        EnumFloat_data[1] = EnumFloat.fromString("3.00000000");
      } catch (IllegalStateException e) {
        init = false;
      }
    } else if (s.equals("EnumDouble")) {
      try {
        EnumDouble_data[0] = EnumDouble.fromValue((double) -1.0000000000000);
        EnumDouble_data[1] = EnumDouble.fromString("3.0000000000000");
      } catch (IllegalStateException e) {
        init = false;
      }
    } else if (s.equals("EnumByte")) {
      try {
        EnumByte_data[0] = EnumByte.fromValue(Byte.MIN_VALUE);
        EnumByte_data[1] = EnumByte
            .fromString(new Byte(Byte.MAX_VALUE).toString());
      } catch (IllegalStateException e) {
        init = false;
      }
    } else {
      init = false;
    }

    return init;
  }

  /*
   * @testName: MarshallAllSimpleXMLDataTypesTest
   *
   * @assertion_ids: JAXRPC:SPEC:50; JAXRPC:SPEC:52; JAXRPC:SPEC:55;
   * JAXRPC:SPEC:124; JAXRPC:SPEC:443; JAXRPC:SPEC:444; JAXRPC:SPEC:445;
   * JAXRPC:SPEC:449; JAXRPC:SPEC:450; JAXRPC:SPEC:500; JAXRPC:SPEC:502;
   * WS4EE:SPEC:35; WS4EE:SPEC:36; WS4EE:SPEC:70; WS4EE:SPEC:137;
   * 
   * @test_Strategy: Create a stub instance to our service definition interface,
   * set the target endpoint to the servlet, and invoke the RPC methods for each
   * primitive type. For each type pass its value as input to the corresponding
   * RPC method and receive it back as the return value. Compare results of each
   * value/type of what was sent and what was returned. Verify they are equal.
   *
   * Description All the simple XML data types can be marshalled.
   */
  public void MarshallAllSimpleXMLDataTypesTest() throws Fault {
    TestUtil.logMsg("MarshallAllSimpleXMLDataTypesTest");
    boolean pass = true;

    if (!StringTest())
      pass = false;
    printSeperationLine();
    if (!IntegerTest())
      pass = false;
    printSeperationLine();
    if (!IntTest())
      pass = false;
    printSeperationLine();
    if (!LongTest())
      pass = false;
    printSeperationLine();
    if (!ShortTest())
      pass = false;
    printSeperationLine();
    if (!DecimalTest())
      pass = false;
    printSeperationLine();
    if (!FloatTest())
      pass = false;
    printSeperationLine();
    if (!DoubleTest())
      pass = false;
    printSeperationLine();
    if (!BooleanTest())
      pass = false;
    printSeperationLine();
    if (!ByteTest())
      pass = false;
    printSeperationLine();
    if (!QNameTest())
      pass = false;
    printSeperationLine();
    if (!DateTimeTest())
      pass = false;
    printSeperationLine();
    if (!SoapStringTest())
      pass = false;
    printSeperationLine();
    if (!SoapBooleanTest())
      pass = false;
    printSeperationLine();
    if (!SoapFloatTest())
      pass = false;
    printSeperationLine();
    if (!SoapDoubleTest())
      pass = false;
    printSeperationLine();
    if (!SoapDecimalTest())
      pass = false;
    printSeperationLine();
    if (!SoapIntTest())
      pass = false;
    printSeperationLine();
    if (!SoapShortTest())
      pass = false;
    printSeperationLine();
    if (!SoapByteTest())
      pass = false;
    printSeperationLine();
    if (!Base64BinaryTest())
      pass = false;
    printSeperationLine();
    if (!HexBinaryTest())
      pass = false;
    printSeperationLine();
    if (!SoapBase64Test())
      pass = false;
    printSeperationLine();
    if (!VoidTest())
      pass = false;
    printSeperationLine();

    if (!pass)
      throw new Fault("MarshallAllSimpleXMLDataTypesTest failed");
  }

  /*
   * @testName: MarshallAllStructsTest
   *
   * @assertion_ids: JAXRPC:SPEC:61; JAXRPC:SPEC:62; JAXRPC:SPEC:63;
   * JAXRPC:SPEC:124; JAXRPC:SPEC:505; JAXRPC:SPEC:506; JAXRPC:SPEC:520;
   * WS4EE:SPEC:35; WS4EE:SPEC:36; WS4EE:SPEC:70; WS4EE:SPEC:137;
   *
   * 
   * @test_Strategy: Create a stub instance to our service definition interface,
   * set the target endpoint to the servlet, and invoke the RPC methods for each
   * primitive type. For each primitive type pass its value as input to the
   * corresponding RPC method and receive it back as the return value. Compare
   * results of each value/type of what was sent and what was returned. Verify
   * they are equal.
   *
   * Description XML structs that are defined using xsd:complexType and xsd:all
   * with simple and complex types can be marshalled.
   */
  public void MarshallAllStructsTest() throws Fault {
    TestUtil.logMsg("MarshallAllStructsTest");
    boolean pass = true;

    if (!AllStructTest())
      pass = false;
    printSeperationLine();
    if (!AllStructArrayTest())
      pass = false;
    printSeperationLine();
    if (!AllStruct2Test())
      pass = false;
    printSeperationLine();
    if (!AllStruct2ArrayTest())
      pass = false;
    printSeperationLine();

    if (!pass)
      throw new Fault("MarshallAllStructsTest failed");
  }

  /*
   * @testName: MarshallSequenceStructsTest
   *
   * @assertion_ids: JAXRPC:SPEC:59; JAXRPC:SPEC:60; JAXRPC:SPEC:63;
   * JAXRPC:SPEC:124; JAXRPC:SPEC:517; WS4EE:SPEC:35; WS4EE:SPEC:36;
   * WS4EE:SPEC:70; WS4EE:SPEC:137;
   * 
   * @test_Strategy: Create a stub instance to our service definition interface,
   * set the target endpoint to the servlet, and invoke the RPC methods for each
   * primitive type. For each primitive type pass its value as input to the
   * corresponding RPC method and receive it back as the return value. Compare
   * results of each value/type of what was sent and what was returned. Verify
   * they are equal.
   *
   * Description XML structs that are defined using xsd:complexType and
   * xsd:sequence with simple and complex types can be marshalled.
   */
  public void MarshallSequenceStructsTest() throws Fault {
    TestUtil.logMsg("MarshallSequenceStructsTest");
    boolean pass = true;

    if (!SequenceStructTest())
      pass = false;
    printSeperationLine();
    if (!SequenceStructArrayTest())
      pass = false;
    printSeperationLine();
    if (!SequenceStruct2Test())
      pass = false;
    printSeperationLine();
    if (!SequenceStruct2ArrayTest())
      pass = false;
    printSeperationLine();

    if (!pass)
      throw new Fault("MarshallSequenceStructsTest failed");
  }

  /*
   * @testName: MarshallWSDLRestrictedArrayTest
   *
   * @assertion_ids: JAXRPC:SPEC:56; JAXRPC:SPEC:124; JAXRPC:SPEC:504;
   * JAXRPC:SPEC:514; WS4EE:SPEC:35; WS4EE:SPEC:36; WS4EE:SPEC:70;
   * WS4EE:SPEC:137;
   * 
   * @test_Strategy: Create a stub instance to our service definition interface,
   * set the target endpoint to the servlet, and invoke the RPC methods for each
   * primitive type. For each primitive type pass its value as input to the
   * corresponding RPC method and receive it back as the return value. Compare
   * results of each value/type of what was sent and what was returned. Verify
   * they are equal.
   *
   * Description WSDL restricted arrays of simple XML data types can be
   * marshalled.
   */
  public void MarshallWSDLRestrictedArrayTest() throws Fault {
    TestUtil.logMsg("MarshallWSDLRestrictedArrayTest");
    boolean pass = true;

    if (!StringArrayTest())
      pass = false;
    printSeperationLine();
    if (!IntegerArrayTest())
      pass = false;
    printSeperationLine();
    if (!IntArrayTest())
      pass = false;
    printSeperationLine();
    if (!LongArrayTest())
      pass = false;
    printSeperationLine();
    if (!ShortArrayTest())
      pass = false;
    printSeperationLine();
    if (!DecimalArrayTest())
      pass = false;
    printSeperationLine();
    if (!FloatArrayTest())
      pass = false;
    printSeperationLine();
    if (!DoubleArrayTest())
      pass = false;
    printSeperationLine();
    if (!BooleanArrayTest())
      pass = false;
    printSeperationLine();
    if (!ByteArrayTest())
      pass = false;
    printSeperationLine();
    if (!QNameArrayTest())
      pass = false;
    printSeperationLine();
    if (!DateTimeArrayTest())
      pass = false;
    printSeperationLine();
    if (!SoapStringArrayTest())
      pass = false;
    printSeperationLine();
    if (!SoapBooleanArrayTest())
      pass = false;
    printSeperationLine();
    if (!SoapFloatArrayTest())
      pass = false;
    printSeperationLine();
    if (!SoapDoubleArrayTest())
      pass = false;
    printSeperationLine();
    if (!SoapDecimalArrayTest())
      pass = false;
    printSeperationLine();
    if (!SoapIntArrayTest())
      pass = false;
    printSeperationLine();
    if (!SoapShortArrayTest())
      pass = false;
    printSeperationLine();
    if (!SoapByteArrayTest())
      pass = false;
    printSeperationLine();
    if (!Base64BinaryArrayTest())
      pass = false;
    printSeperationLine();
    if (!HexBinaryArrayTest())
      pass = false;
    printSeperationLine();
    if (!SoapBase64ArrayTest())
      pass = false;
    printSeperationLine();

    if (!pass)
      throw new Fault("MarshallWSDLRestrictedArrayTest failed");
  }

  /*
   * @testName: MarshallSOAPRestrictedArrayTest
   *
   * @assertion_ids: JAXRPC:SPEC:57; JAXRPC:SPEC:124; JAXRPC:SPEC:514;
   * WS4EE:SPEC:35; WS4EE:SPEC:36; WS4EE:SPEC:70; WS4EE:SPEC:137;
   * 
   * @test_Strategy: Create a stub instance to our service definition interface,
   * set the target endpoint to the servlet, and invoke the RPC methods for each
   * primitive type. For each primitive type pass its value as input to the
   * corresponding RPC method and receive it back as the return value. Compare
   * results of each value/type of what was sent and what was returned. Verify
   * they are equal.
   *
   * Description SOAP restricted arrays of simple XML data types can be
   * marshalled.
   */
  public void MarshallSOAPRestrictedArrayTest() throws Fault {
    TestUtil.logMsg("MarshallSOAPRestrictedArrayTest");
    boolean pass = true;

    if (!String1ArrayTest())
      pass = false;
    printSeperationLine();
    if (!Integer1ArrayTest())
      pass = false;
    printSeperationLine();
    if (!Int1ArrayTest())
      pass = false;
    printSeperationLine();
    if (!Long1ArrayTest())
      pass = false;
    printSeperationLine();
    if (!Short1ArrayTest())
      pass = false;
    printSeperationLine();
    if (!Decimal1ArrayTest())
      pass = false;
    printSeperationLine();
    if (!Float1ArrayTest())
      pass = false;
    printSeperationLine();
    if (!Double1ArrayTest())
      pass = false;
    printSeperationLine();
    if (!Boolean1ArrayTest())
      pass = false;
    printSeperationLine();
    if (!Byte1ArrayTest())
      pass = false;
    printSeperationLine();
    if (!QName1ArrayTest())
      pass = false;
    printSeperationLine();
    if (!DateTime1ArrayTest())
      pass = false;
    printSeperationLine();
    if (!SoapString1ArrayTest())
      pass = false;
    printSeperationLine();
    if (!SoapBoolean1ArrayTest())
      pass = false;
    printSeperationLine();
    if (!SoapFloat1ArrayTest())
      pass = false;
    printSeperationLine();
    if (!SoapDouble1ArrayTest())
      pass = false;
    printSeperationLine();
    if (!SoapDecimal1ArrayTest())
      pass = false;
    printSeperationLine();
    if (!SoapInt1ArrayTest())
      pass = false;
    printSeperationLine();
    if (!SoapShort1ArrayTest())
      pass = false;
    printSeperationLine();
    if (!SoapByte1ArrayTest())
      pass = false;
    printSeperationLine();
    if (!Base64Binary1ArrayTest())
      pass = false;
    printSeperationLine();
    if (!HexBinary1ArrayTest())
      pass = false;
    printSeperationLine();
    if (!SoapBase641ArrayTest())
      pass = false;
    printSeperationLine();

    if (!pass)
      throw new Fault("MarshallSOAPRestrictedArrayTest failed");
  }

  /*
   * @testName: MarshallExceptionsTest
   *
   * @assertion_ids: JAXRPC:SPEC:111; JAXRPC:SPEC:124; JAXRPC:SPEC:452;
   * JAXRPC:SPEC:453; JAXRPC:SPEC:454; JAXRPC:SPEC:456; JAXRPC:SPEC:457;
   * WS4EE:SPEC:35; WS4EE:SPEC:36;
   * 
   * @test_Strategy:
   *
   * Description An exception which is a subclass of java.rmi.RemoteException or
   * a service specific exception can be marshalled.
   */
  public void MarshallExceptionsTest() throws Fault {
    TestUtil.logMsg("MarshallExceptionsTest");
    boolean pass = true;

    // if ( !ServiceExceptionTest() ) pass = false; printSeperationLine();
    if (!RemoteExceptionTest())
      pass = false;
    printSeperationLine();
    if (!SubClassRemoteExceptionTest())
      pass = false;
    printSeperationLine();

    if (!pass)
      throw new Fault("MarshallExceptionsTest failed");
  }

  /*
   * @testName: MarshallEnumerationsTest
   *
   * @assertion_ids: JAXRPC:SPEC:68; JAXRPC:SPEC:69; JAXRPC:SPEC:124;
   * JAXRPC:SPEC:503; JAXRPC:SPEC:511; WS4EE:SPEC:35; WS4EE:SPEC:36;
   * WS4EE:SPEC:70; WS4EE:SPEC:137;
   * 
   * @test_Strategy:
   *
   * Description All simple XML type except for boolean can be marshalled as an
   * enumeration.
   */
  public void MarshallEnumerationsTest() throws Fault {
    TestUtil.logMsg("MarshallEnumerationsTest");
    boolean pass = true;

    if (!StringEnumerationsTest())
      pass = false;
    printSeperationLine();
    if (!IntegerEnumerationsTest())
      pass = false;
    printSeperationLine();
    if (!IntEnumerationsTest())
      pass = false;
    printSeperationLine();
    if (!LongEnumerationsTest())
      pass = false;
    printSeperationLine();
    if (!ShortEnumerationsTest())
      pass = false;
    printSeperationLine();
    if (!DecimalEnumerationsTest())
      pass = false;
    printSeperationLine();
    if (!FloatEnumerationsTest())
      pass = false;
    printSeperationLine();
    if (!DoubleEnumerationsTest())
      pass = false;
    printSeperationLine();
    if (!ByteEnumerationsTest())
      pass = false;
    printSeperationLine();
    if (!pass)
      throw new Fault("MarshallEnumerationsTest failed");
  }

  /*
   * @testName: MarshallSimpleXMLNillableDataTypesTest
   *
   * @assertion_ids: JAXRPC:SPEC:54; WS4EE:SPEC:35; WS4EE:SPEC:36;
   * WS4EE:SPEC:70; WS4EE:SPEC:137;
   * 
   * @test_Strategy: Create a stub instance to our service definition interface,
   * set the target endpoint to the servlet, and invoke the RPC methods for each
   * primitive type. For each type pass its value as input to the corresponding
   * RPC method and receive it back as the return value. Compare results of each
   * value/type of what was sent and what was returned. Verify they are equal.
   *
   * Description All the simple XML nillable data types can be marshalled.
   */
  public void MarshallSimpleXMLNillableDataTypesTest() throws Fault {
    TestUtil.logMsg("MarshallSimpleXMLNillableDataTypesTest");
    boolean pass = true;

    if (!nilTrueTest())
      pass = false;
    printSeperationLine();
    if (!nilFalseTest())
      pass = false;
    printSeperationLine();

    if (!pass)
      throw new Fault("MarshallSimpleXMLNillableDataTypesTest failed");
  }

  private boolean printTestStatus(boolean pass, String test) {
    if (pass)
      TestUtil.logMsg("" + test + " ... PASSED");
    else
      TestUtil.logErr("" + test + " ... FAILED");

    return pass;
  }

  private boolean StringTest() {
    TestUtil.logMsg("StringTest");
    boolean pass = true;
    String values[] = JAXRPC_Data.String_data;
    String response;
    TestUtil.logMsg("Passing/Returning data to/from JAXRPC Service");
    try {
      for (int i = 0; i < values.length; i++) {
        response = port.echoString(values[i]);

        if (values[i] == null && response == null) {
          continue;
        } else if (!response.equals(values[i])) {
          TestUtil.logErr("StringTest failed - expected " + values[i]
              + ",  received: " + response);
          pass = false;
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }

    printTestStatus(pass, "StringTest");
    return pass;
  }

  private boolean IntegerTest() {
    TestUtil.logMsg("IntegerTest");
    boolean pass = true;
    BigInteger values[] = JAXRPC_Data.BigInteger_data;
    BigInteger response;

    TestUtil.logMsg("Passing/Returning data to/from JAXRPC Service");
    try {
      for (int i = 0; i < values.length; i++) {
        response = port.echoInteger(values[i]);

        if (values[i] == null && response == null) {
          continue;
        }

        if (!response.equals(values[i])) {
          TestUtil.logErr("IntegerTest failed - expected " + values[i]
              + ",  received: " + response);
          pass = false;
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }

    printTestStatus(pass, "IntegerTest");
    return pass;
  }

  private boolean IntTest() {
    TestUtil.logMsg("IntTest");
    boolean pass = true;
    int values[] = JAXRPC_Data.int_data;
    int response;

    TestUtil.logMsg("Passing/Returning data to/from JAXRPC Service");
    try {
      for (int i = 0; i < values.length; i++) {
        response = port.echoInt(values[i]);

        if (response != values[i]) {
          TestUtil.logErr("IntTest failed - expected " + values[i]
              + ",  received: " + response);
          pass = false;
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }

    printTestStatus(pass, "IntTest");
    return pass;
  }

  private boolean LongTest() {
    TestUtil.logMsg("LongTest");
    boolean pass = true;
    long values[] = JAXRPC_Data.long_data;
    long response;

    TestUtil.logMsg("Passing/Returning data to/from JAXRPC Service");
    try {
      for (int i = 0; i < values.length; i++) {
        response = port.echoLong(values[i]);

        if (response != values[i]) {
          TestUtil.logErr("LongTest failed - expected " + values[i]
              + ",  received: " + response);
          pass = false;
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }

    printTestStatus(pass, "LongTest");
    return pass;
  }

  private boolean ShortTest() {
    TestUtil.logMsg("ShortTest");
    boolean pass = true;
    short values[] = JAXRPC_Data.short_data;
    short response;

    TestUtil.logMsg("Passing/Returning data to/from JAXRPC Service");
    try {
      for (int i = 0; i < values.length; i++) {
        response = port.echoShort(values[i]);

        if (response != values[i]) {
          TestUtil.logErr("ShortTest failed - expected " + values[i]
              + ",  received: " + response);
          pass = false;
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }

    printTestStatus(pass, "ShortTest");
    return pass;
  }

  private boolean DecimalTest() {
    TestUtil.logMsg("DecimalTest");
    boolean pass = true;
    BigDecimal values[] = JAXRPC_Data.BigDecimal_data;
    BigDecimal response;

    TestUtil.logMsg("Passing/Returning data to/from JAXRPC Service");
    try {
      for (int i = 0; i < values.length; i++) {
        response = port.echoDecimal(values[i]);

        if (values[i] == null && response == null) {
          continue;
        } else if (!response.equals(values[i])) {
          TestUtil.logErr("DecimalTest failed - expected " + values[i]
              + ",  received: " + response);
          pass = false;
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }

    printTestStatus(pass, "DecimalTest");
    return pass;
  }

  private boolean FloatTest() {
    TestUtil.logMsg("FloatTest");
    boolean pass = true;
    float values[] = JAXRPC_Data.float_data;
    float response;

    TestUtil.logMsg("Passing/Returning data to/from JAXRPC Service");
    try {
      for (int i = 0; i < values.length; i++) {
        response = port.echoFloat(values[i]);

        if (response != values[i]) {
          TestUtil.logErr("FloatTest failed - expected " + values[i]
              + ",  received: " + response);
          pass = false;
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }

    printTestStatus(pass, "FloatTest");
    return pass;
  }

  private boolean DoubleTest() {
    TestUtil.logMsg("DoubleTest");
    boolean pass = true;
    double values[] = JAXRPC_Data.double_data;
    double response;

    TestUtil.logMsg("Passing/Returning data to/from JAXRPC Service");
    try {
      for (int i = 0; i < values.length; i++) {
        response = port.echoDouble(values[i]);

        if (response != values[i]) {
          TestUtil.logErr("DoubleTest failed - expected " + values[i]
              + ",  received: " + response);
          pass = false;
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }

    printTestStatus(pass, "DoubleTest");
    return pass;
  }

  private boolean BooleanTest() {
    TestUtil.logMsg("BooleanTest");
    boolean pass = true;
    boolean values[] = JAXRPC_Data.boolean_data;
    boolean response;

    TestUtil.logMsg("Passing/Returning data to/from JAXRPC Service");
    try {
      for (int i = 0; i < values.length; i++) {
        response = port.echoBoolean(values[i]);

        if (!response == values[i]) {
          TestUtil.logErr("BooleanTest failed - expected " + values[i]
              + ",  received: " + response);
          pass = false;
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }

    printTestStatus(pass, "BooleanTest");
    return pass;
  }

  private boolean ByteTest() {
    TestUtil.logMsg("ByteTest");
    boolean pass = true;
    byte values[] = JAXRPC_Data.byte_data;
    byte response;

    TestUtil.logMsg("Passing/Returning data to/from JAXRPC Service");
    try {
      for (int i = 0; i < values.length; i++) {
        response = port.echoByte(values[i]);

        if (response != values[i]) {
          TestUtil.logErr("ByteTest failed - expected " + values[i]
              + ",  received: " + response);
          pass = false;
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }

    printTestStatus(pass, "ByteTest");
    return pass;
  }

  private boolean QNameTest() {
    TestUtil.logMsg("QNameTest");
    boolean pass = true;
    QName values[] = JAXRPC_Data.QName_data;
    QName response;

    TestUtil.logMsg("Passing/Returning data to/from JAXRPC Service");
    try {
      for (int i = 0; i < values.length; i++) {
        response = port.echoQName(values[i]);
        if (values[i] == null && response == null) {
          continue;
        } else if (!response.equals(values[i])) {
          TestUtil.logErr("QNameTest failed - expected " + values[i]
              + ",  received: " + response);
          pass = false;
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }

    printTestStatus(pass, "QNameTest");
    return pass;
  }

  private boolean DateTimeTest() {
    TestUtil.logMsg("DateTimeTest");
    boolean pass = true;
    Calendar values[] = JAXRPC_Data.GregorianCalendar_data;
    Calendar response;

    TestUtil.logMsg("Passing/Returning data to/from JAXRPC Service");
    try {
      for (int i = 0; i < values.length; i++) {
        response = port.echoDateTime(values[i]);
        if (!JAXRPC_Data.compareValues(values[i], response, "Calendar"))
          pass = false;

      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }

    printTestStatus(pass, "DateTimeTest");
    return pass;
  }

  private boolean SoapStringTest() {
    TestUtil.logMsg("SoapStringTest");
    boolean pass = true;
    String values[] = JAXRPC_Data.String_data;
    String response;

    TestUtil.logMsg("Passing/Returning data to/from JAXRPC Service");
    try {
      for (int i = 0; i < values.length; i++) {
        response = port.echoSoapString(values[i]);

        if (values[i] == null && response == null) {
          continue;
        } else if (!response.equals(values[i])) {
          TestUtil.logErr("SoapStringTest failed - expected " + values[i]
              + ",  received: " + response);
          pass = false;
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }

    printTestStatus(pass, "SoapStringTest");
    return pass;
  }

  private boolean SoapBooleanTest() {
    TestUtil.logMsg("SoapBooleanTest");
    boolean pass = true;
    Boolean values[] = JAXRPC_Data.Boolean_data;
    Boolean response;
    TestUtil.logMsg("Passing/Returning data to/from JAXRPC Service");
    try {
      for (int i = 0; i < values.length; i++) {
        response = port.echoSoapBoolean(values[i]);
        if (values[i] == null && response == null) {
          continue;
        } else if (!response.equals(values[i])) {
          TestUtil.logErr("SoapBooleanTest failed - expected " + values[i]
              + ",  received: " + response);
          pass = false;
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "SoapBooleanTest");
    return pass;
  }

  private boolean SoapFloatTest() {
    TestUtil.logMsg("SoapFloatTest");
    boolean pass = true;
    Float values[] = JAXRPC_Data.Float_data;
    Float response;
    TestUtil.logMsg("Passing/Returning data to/from JAXRPC Service");
    try {
      for (int i = 0; i < values.length; i++) {
        response = port.echoSoapFloat(values[i]);
        if (values[i] == null && response == null) {
          continue;
        } else if (!response.equals(values[i])) {
          TestUtil.logErr("SoapFloatTest failed - expected " + values[i]
              + ",  received: " + response);
          pass = false;
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "SoapFloatTest");
    return pass;
  }

  private boolean SoapDoubleTest() {
    TestUtil.logMsg("SoapDoubleTest");
    boolean pass = true;
    Double values[] = JAXRPC_Data.Double_data;
    Double response;
    TestUtil.logMsg("Passing/Returning data to/from JAXRPC Service");
    try {
      for (int i = 0; i < values.length; i++) {
        response = port.echoSoapDouble(values[i]);
        if (values[i] == null && response == null) {
          continue;
        } else if (!response.equals(values[i])) {
          TestUtil.logErr("SoapDoubleTest failed - expected " + values[i]
              + ",  received: " + response);
          pass = false;
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "SoapDoubleTest");
    return pass;
  }

  private boolean SoapDecimalTest() {
    TestUtil.logMsg("SoapDecimalTest");
    boolean pass = true;
    BigDecimal values[] = JAXRPC_Data.BigDecimal_data;
    BigDecimal response;
    TestUtil.logMsg("Passing/Returning data to/from JAXRPC Service");
    try {
      for (int i = 0; i < values.length; i++) {
        response = port.echoSoapDecimal(values[i]);
        if (values[i] == null && response == null) {
          continue;
        } else if (!response.equals(values[i])) {
          TestUtil.logErr("SoapDecimalTest failed - expected " + values[i]
              + ",  received: " + response);
          pass = false;
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "SoapDecimalTest");
    return pass;
  }

  private boolean SoapIntTest() {
    TestUtil.logMsg("SoapIntTest");
    boolean pass = true;
    Integer values[] = JAXRPC_Data.Integer_data;
    Integer response;
    TestUtil.logMsg("Passing/Returning data to/from JAXRPC Service");
    try {
      for (int i = 0; i < values.length; i++) {
        response = port.echoSoapInt(values[i]);
        if (values[i] == null && response == null) {
          continue;
        } else if (!response.equals(values[i])) {
          TestUtil.logErr("SoapIntTest failed - expected " + values[i]
              + ",  received: " + response);
          pass = false;
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "SoapIntTest");
    return pass;
  }

  private boolean SoapShortTest() {
    TestUtil.logMsg("SoapShortTest");
    boolean pass = true;
    Short values[] = JAXRPC_Data.Short_data;
    Short response;
    TestUtil.logMsg("Passing/Returning data to/from JAXRPC Service");
    try {
      for (int i = 0; i < values.length; i++) {
        response = port.echoSoapShort(values[i]);
        if (values[i] == null && response == null) {
          continue;
        } else if (!response.equals(values[i])) {
          TestUtil.logErr("SoapShortTest failed - expected " + values[i]
              + ",  received: " + response);
          pass = false;
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "SoapShortTest");
    return pass;
  }

  private boolean SoapByteTest() {
    TestUtil.logMsg("SoapByteTest");
    boolean pass = true;
    Byte values[] = JAXRPC_Data.Byte_data;
    Byte response;
    TestUtil.logMsg("Passing/Returning data to/from JAXRPC Service");
    try {
      for (int i = 0; i < values.length; i++) {
        response = port.echoSoapByte(values[i]);
        if (values[i] == null && response == null) {
          continue;
        } else if (!response.equals(values[i])) {
          TestUtil.logErr("SoapByteTest failed - expected " + values[i]
              + ",  received: " + response);
          pass = false;
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "SoapByteTest");
    return pass;
  }

  private boolean Base64BinaryTest() {
    TestUtil.logMsg("Base64BinaryTest");
    boolean pass = false;
    byte values[] = JAXRPC_Data.byte_data;
    byte[] response;

    TestUtil.logMsg("Passing/Returning data to/from JAXRPC Service");
    try {
      response = port.echoBase64Binary(values);
      pass = JAXRPC_Data.compareArrayValues(values, response, "byte");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }

    printTestStatus(pass, "Base64BinaryTest");
    return pass;
  }

  private boolean HexBinaryTest() {
    TestUtil.logMsg("HexBinaryTest");
    boolean pass = false;
    byte values[] = JAXRPC_Data.byte_data;
    byte[] response;

    TestUtil.logMsg("Passing/Returning data to/from JAXRPC Service");
    try {
      response = port.echoHexBinary(values);
      pass = JAXRPC_Data.compareArrayValues(values, response, "byte");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }

    printTestStatus(pass, "HexBinaryTest");
    return pass;
  }

  private boolean SoapBase64Test() {
    TestUtil.logMsg("SoapBase64Test");
    boolean pass = false;
    byte values[] = JAXRPC_Data.byte_data;
    byte[] response;
    TestUtil.logMsg("Passing/Returning data to/from JAXRPC Service");
    try {
      response = port.echoSoapBase64(values);
      pass = JAXRPC_Data.compareArrayValues(values, response, "byte");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "SoapBase64Test");
    return pass;
  }

  private boolean StringArrayTest() {
    TestUtil.logMsg("StringArrayTest");
    boolean pass = false;
    String values[] = JAXRPC_Data.String_data;
    String[] response;
    TestUtil.logMsg("Passing/Returning array data to/from JAXRPC Service");
    try {
      response = port.echoStringArray(values);
      pass = JAXRPC_Data.compareArrayValues(values, response, "String");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }

    printTestStatus(pass, "StringArrayTest");
    return pass;
  }

  private boolean IntegerArrayTest() {
    TestUtil.logMsg("IntegerArrayTest");
    boolean pass = false;
    BigInteger values[] = JAXRPC_Data.BigInteger_data;
    BigInteger[] response;
    TestUtil.logMsg("Passing/Returning array data to/from JAXRPC Service");
    try {
      response = port.echoIntegerArray(values);
      pass = JAXRPC_Data.compareArrayValues(values, response, "BigInteger");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }

    printTestStatus(pass, "IntegerArrayTest");
    return pass;
  }

  private boolean IntArrayTest() {
    TestUtil.logMsg("IntArrayTest");
    boolean pass = false;
    int values[] = JAXRPC_Data.int_data;
    int[] response;

    TestUtil.logMsg("Passing/Returning array data to/from JAXRPC Service");
    try {
      response = port.echoIntArray(values);
      pass = JAXRPC_Data.compareArrayValues(values, response, "int");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }

    printTestStatus(pass, "IntArrayTest");
    return pass;
  }

  private boolean LongArrayTest() {
    TestUtil.logMsg("LongArrayTest");
    boolean pass = false;
    long values[] = JAXRPC_Data.long_data;
    long[] response;

    TestUtil.logMsg("Passing/Returning array data to/from JAXRPC Service");
    try {
      response = port.echoLongArray(values);
      pass = JAXRPC_Data.compareArrayValues(values, response, "long");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }

    printTestStatus(pass, "LongArrayTest");
    return pass;
  }

  private boolean ShortArrayTest() {
    TestUtil.logMsg("ShortArrayTest");
    boolean pass = false;
    short values[] = JAXRPC_Data.short_data;
    short[] response;

    TestUtil.logMsg("Passing/Returning array data to/from JAXRPC Service");
    try {
      response = port.echoShortArray(values);
      pass = JAXRPC_Data.compareArrayValues(values, response, "short");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }

    printTestStatus(pass, "ShortArrayTest");
    return pass;
  }

  private boolean FloatArrayTest() {
    TestUtil.logMsg("FloatArrayTest");
    boolean pass = false;
    float values[] = JAXRPC_Data.float_data;
    float[] response;

    TestUtil.logMsg("Passing/Returning array data to/from JAXRPC Service");
    try {
      response = port.echoFloatArray(values);
      pass = JAXRPC_Data.compareArrayValues(values, response, "float");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }

    printTestStatus(pass, "FloatArrayTest");
    return pass;
  }

  private boolean DoubleArrayTest() {
    TestUtil.logMsg("DoubleArrayTest");
    boolean pass = false;
    double values[] = JAXRPC_Data.double_data;
    double[] response;

    TestUtil.logMsg("Passing/Returning array data to/from JAXRPC Service");
    try {
      response = port.echoDoubleArray(values);
      pass = JAXRPC_Data.compareArrayValues(values, response, "double");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }

    printTestStatus(pass, "DoubleArrayTest");
    return pass;
  }

  private boolean DecimalArrayTest() {
    TestUtil.logMsg("DecimalArrayTest");
    boolean pass = false;
    BigDecimal values[] = JAXRPC_Data.BigDecimal_data;
    BigDecimal[] response;

    TestUtil.logMsg("Passing/Returning array data to/from JAXRPC Service");
    try {
      response = port.echoDecimalArray(values);
      pass = JAXRPC_Data.compareArrayValues(values, response, "BigDecimal");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }

    printTestStatus(pass, "DecimalArrayTest");
    return pass;
  }

  private boolean BooleanArrayTest() {
    TestUtil.logMsg("BooleanArrayTest");
    boolean pass = false;
    boolean values[] = JAXRPC_Data.boolean_data;
    boolean[] response;

    TestUtil.logMsg("Passing/Returning array data to/from JAXRPC Service");
    try {
      response = port.echoBooleanArray(values);
      pass = JAXRPC_Data.compareArrayValues(values, response, "boolean");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }

    printTestStatus(pass, "BooleanArrayTest");
    return pass;
  }

  private boolean ByteArrayTest() {
    TestUtil.logMsg("ByteArrayTest");
    boolean pass = false;
    byte values[] = JAXRPC_Data.byte_data;
    byte[] response;

    TestUtil.logMsg("Passing/Returning array data to/from JAXRPC Service");
    try {
      response = port.echoByteArray(values);
      pass = JAXRPC_Data.compareArrayValues(values, response, "byte");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }

    printTestStatus(pass, "ByteArrayTest");
    return pass;
  }

  private boolean QNameArrayTest() {
    TestUtil.logMsg("QNameArrayTest");
    boolean pass = false;
    QName values[] = JAXRPC_Data.QName_data;
    QName[] response;

    TestUtil.logMsg("Passing/Returning array data to/from JAXRPC Service");
    try {
      response = port.echoQNameArray(values);
      pass = JAXRPC_Data.compareArrayValues(values, response, "QName");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }

    printTestStatus(pass, "QNameArrayTest");
    return pass;
  }

  private boolean DateTimeArrayTest() {
    TestUtil.logMsg("DateTimeArrayTest");
    boolean pass = false;
    Calendar values[] = JAXRPC_Data.GregorianCalendar_data;
    Calendar response[];

    TestUtil.logMsg("Passing/Returning array data to/from JAXRPC Service");
    try {
      response = port.echoDateTimeArray(values);
      pass = JAXRPC_Data.compareArrayValues(values, response, "Calendar");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }

    printTestStatus(pass, "DateTimeTest");
    return pass;
  }

  private boolean SoapStringArrayTest() {
    TestUtil.logMsg("SoapStringArrayTest");
    boolean pass = false;
    String values[] = JAXRPC_Data.String_data;
    String[] response;
    TestUtil.logMsg("Passing/Returning array data to/from JAXRPC Service");
    try {
      response = port.echoSoapStringArray(values);
      pass = JAXRPC_Data.compareArrayValues(values, response, "String");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "SoapStringTest");
    return pass;
  }

  private boolean SoapBooleanArrayTest() {
    TestUtil.logMsg("SoapBooleanArrayTest");
    boolean pass = false;
    Boolean values[] = JAXRPC_Data.Boolean_data;
    Boolean[] response;
    TestUtil.logMsg("Passing/Returning array data to/from JAXRPC Service");
    try {
      response = port.echoSoapBooleanArray(values);
      pass = JAXRPC_Data.compareArrayValues(values, response, "Boolean");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "SoapBooleanTest");
    return pass;
  }

  private boolean SoapFloatArrayTest() {
    TestUtil.logMsg("SoapFloatArrayTest");
    boolean pass = false;
    Float values[] = JAXRPC_Data.Float_data;
    Float[] response;
    TestUtil.logMsg("Passing/Returning array data to/from JAXRPC Service");
    try {
      response = port.echoSoapFloatArray(values);
      pass = JAXRPC_Data.compareArrayValues(values, response, "Float");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "SoapFloatTest");
    return pass;
  }

  private boolean SoapDoubleArrayTest() {
    TestUtil.logMsg("SoapDoubleArrayTest");
    boolean pass = false;
    Double values[] = JAXRPC_Data.Double_data;
    Double[] response;
    TestUtil.logMsg("Passing/Returning array data to/from JAXRPC Service");
    try {
      response = port.echoSoapDoubleArray(values);
      pass = JAXRPC_Data.compareArrayValues(values, response, "Double");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "SoapDoubleTest");
    return pass;
  }

  private boolean SoapDecimalArrayTest() {
    TestUtil.logMsg("SoapDecimalrrayTest");
    boolean pass = false;
    BigDecimal values[] = JAXRPC_Data.BigDecimal_data;
    BigDecimal[] response;
    TestUtil.logMsg("Passing/Returning array data to/from JAXRPC Service");
    try {
      response = port.echoSoapDecimalArray(values);
      pass = JAXRPC_Data.compareArrayValues(values, response, "BigDecimal");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "SoapDecimalest");
    return pass;
  }

  private boolean SoapIntArrayTest() {
    TestUtil.logMsg("SoapIntArrayTest");
    boolean pass = false;
    Integer values[] = JAXRPC_Data.Integer_data;
    Integer[] response;
    TestUtil.logMsg("Passing/Returning array data to/from JAXRPC Service");
    try {
      response = port.echoSoapIntArray(values);
      pass = JAXRPC_Data.compareArrayValues(values, response, "Integer");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "SoapIntTest");
    return pass;
  }

  private boolean SoapShortArrayTest() {
    TestUtil.logMsg("SoapShortArrayTest");
    boolean pass = false;
    Short values[] = JAXRPC_Data.Short_data;
    Short[] response;
    TestUtil.logMsg("Passing/Returning array data to/from JAXRPC Service");
    try {
      response = port.echoSoapShortArray(values);
      pass = JAXRPC_Data.compareArrayValues(values, response, "Short");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "SoapShortTest");
    return pass;
  }

  private boolean SoapByteArrayTest() {
    TestUtil.logMsg("SoapByteArrayTest");
    boolean pass = false;
    Byte values[] = JAXRPC_Data.Byte_data;
    Byte[] response;
    TestUtil.logMsg("Passing/Returning array data to/from JAXRPC Service");
    try {
      response = port.echoSoapByteArray(values);
      pass = JAXRPC_Data.compareArrayValues(values, response, "Byte");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "SoapByteTest");
    return pass;
  }

  private boolean Base64BinaryArrayTest() {
    TestUtil.logMsg("Base64BinaryArrayTest");
    boolean pass = false;
    byte values[][] = JAXRPC_Data.byte_multi_data;
    byte[][] response;

    TestUtil.logMsg("Passing/Returning array data to/from JAXRPC Service");
    try {
      response = port.echoBase64BinaryArray(values);
      pass = JAXRPC_Data.compareMultiArrayValues(values, response, "byte");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }

    printTestStatus(pass, "Base64BinaryArrayTest");
    return pass;
  }

  private boolean HexBinaryArrayTest() {
    TestUtil.logMsg("HexBinaryArrayTest");
    boolean pass = false;
    byte values[][] = JAXRPC_Data.byte_multi_data;
    byte[][] response;

    TestUtil.logMsg("Passing/Returning array data to/from JAXRPC Service");
    try {
      response = port.echoHexBinaryArray(values);
      pass = JAXRPC_Data.compareMultiArrayValues(values, response, "byte");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }

    printTestStatus(pass, "HexBinaryArrayTest");
    return pass;
  }

  private boolean SoapBase64ArrayTest() {
    TestUtil.logMsg("SoapBase64ArrayTest");
    boolean pass = false;
    byte values[][] = JAXRPC_Data.byte_multi_data;
    byte[][] response;
    TestUtil.logMsg("Passing/Returning array data to/from JAXRPC Service");
    try {
      response = port.echoSoapBase64Array(values);
      pass = JAXRPC_Data.compareMultiArrayValues(values, response, "byte");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "SoapBase64ArrayTest");
    return pass;
  }

  private boolean String1ArrayTest() {
    TestUtil.logMsg("String1ArrayTest");
    boolean pass = false;
    String values[] = JAXRPC_Data.String_data;
    String[] response;

    TestUtil.logMsg("Passing/Returning array data to/from JAXRPC Service");
    try {
      response = port.echoString1Array(values);
      pass = JAXRPC_Data.compareArrayValues(values, response, "String");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }

    printTestStatus(pass, "String1ArrayTest");
    return pass;
  }

  private boolean Integer1ArrayTest() {
    TestUtil.logMsg("Integer1ArrayTest");
    boolean pass = false;
    BigInteger values[] = JAXRPC_Data.BigInteger_data;
    BigInteger[] response;

    TestUtil.logMsg("Passing/Returning array data to/from JAXRPC Service");
    try {
      response = port.echoInteger1Array(values);
      pass = JAXRPC_Data.compareArrayValues(values, response, "BigInteger");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }

    printTestStatus(pass, "Integer1ArrayTest");
    return pass;
  }

  private boolean Int1ArrayTest() {
    TestUtil.logMsg("Int1ArrayTest");
    boolean pass = false;
    int values[] = JAXRPC_Data.int_data;
    int[] response;

    TestUtil.logMsg("Passing/Returning array data to/from JAXRPC Service");
    try {
      response = port.echoInt1Array(values);
      pass = JAXRPC_Data.compareArrayValues(values, response, "int");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }

    printTestStatus(pass, "Int1ArrayTest");
    return pass;
  }

  private boolean Long1ArrayTest() {
    TestUtil.logMsg("Long1ArrayTest");
    boolean pass = false;
    long values[] = JAXRPC_Data.long_data;
    long[] response;

    TestUtil.logMsg("Passing/Returning array data to/from JAXRPC Service");
    try {
      response = port.echoLong1Array(values);
      pass = JAXRPC_Data.compareArrayValues(values, response, "long");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }

    printTestStatus(pass, "Long1ArrayTest");
    return pass;
  }

  private boolean Short1ArrayTest() {
    TestUtil.logMsg("Short1ArrayTest");
    boolean pass = false;
    short values[] = JAXRPC_Data.short_data;
    short[] response;

    TestUtil.logMsg("Passing/Returning array data to/from JAXRPC Service");
    try {
      response = port.echoShort1Array(values);
      pass = JAXRPC_Data.compareArrayValues(values, response, "short");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }

    printTestStatus(pass, "Short1ArrayTest");
    return pass;
  }

  private boolean Float1ArrayTest() {
    TestUtil.logMsg("Float1ArrayTest");
    boolean pass = false;
    float values[] = JAXRPC_Data.float_data;
    float[] response;

    TestUtil.logMsg("Passing/Returning array data to/from JAXRPC Service");
    try {
      response = port.echoFloat1Array(values);
      pass = JAXRPC_Data.compareArrayValues(values, response, "float");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }

    printTestStatus(pass, "Float1ArrayTest");
    return pass;
  }

  private boolean Double1ArrayTest() {
    TestUtil.logMsg("Double1ArrayTest");
    boolean pass = false;
    double values[] = JAXRPC_Data.double_data;
    double[] response;

    TestUtil.logMsg("Passing/Returning array data to/from JAXRPC Service");
    try {
      response = port.echoDouble1Array(values);
      pass = JAXRPC_Data.compareArrayValues(values, response, "double");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }

    printTestStatus(pass, "Double1ArrayTest");
    return pass;
  }

  private boolean Decimal1ArrayTest() {
    TestUtil.logMsg("Decimal1ArrayTest");
    boolean pass = false;
    BigDecimal values[] = JAXRPC_Data.BigDecimal_data;
    BigDecimal[] response;

    TestUtil.logMsg("Passing/Returning array data to/from JAXRPC Service");
    try {
      response = port.echoDecimal1Array(values);
      pass = JAXRPC_Data.compareArrayValues(values, response, "BigDecimal");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }

    printTestStatus(pass, "Decimal1ArrayTest");
    return pass;
  }

  private boolean Boolean1ArrayTest() {
    TestUtil.logMsg("Boolean1ArrayTest");
    boolean pass = false;
    boolean values[] = JAXRPC_Data.boolean_data;
    boolean[] response;

    TestUtil.logMsg("Passing/Returning array data to/from JAXRPC Service");
    try {
      response = port.echoBoolean1Array(values);
      pass = JAXRPC_Data.compareArrayValues(values, response, "boolean");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }

    printTestStatus(pass, "Boolean1ArrayTest");
    return pass;
  }

  private boolean Byte1ArrayTest() {
    TestUtil.logMsg("Byte1ArrayTest");
    boolean pass = false;
    byte values[] = JAXRPC_Data.byte_data;
    byte[] response;

    TestUtil.logMsg("Passing/Returning array data to/from JAXRPC Service");
    try {
      response = port.echoByte1Array(values);
      pass = JAXRPC_Data.compareArrayValues(values, response, "byte");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }

    printTestStatus(pass, "Byte1ArrayTest");
    return pass;
  }

  private boolean QName1ArrayTest() {
    TestUtil.logMsg("QName1ArrayTest");
    boolean pass = false;
    QName values[] = JAXRPC_Data.QName_data;
    QName[] response;

    TestUtil.logMsg("Passing/Returning array data to/from JAXRPC Service");
    try {
      response = port.echoQName1Array(values);
      pass = JAXRPC_Data.compareArrayValues(values, response, "QName");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }

    printTestStatus(pass, "QName1ArrayTest");
    return pass;
  }

  private boolean DateTime1ArrayTest() {
    TestUtil.logMsg("DateTime1ArrayTest");
    boolean pass = false;
    Calendar values[] = JAXRPC_Data.GregorianCalendar_data;
    Calendar response[];

    TestUtil.logMsg("Passing/Returning array data to/from JAXRPC Service");
    try {
      response = port.echoDateTime1Array(values);
      pass = JAXRPC_Data.compareArrayValues(values, response, "Calendar");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }

    printTestStatus(pass, "DateTimeTest");
    return pass;
  }

  private boolean SoapString1ArrayTest() {
    TestUtil.logMsg("SoapString1ArrayTest");
    boolean pass = false;
    String values[] = JAXRPC_Data.String_data;
    String[] response;
    TestUtil.logMsg("Passing/Returning array data to/from JAXRPC Service");
    try {
      response = port.echoSoapString1Array(values);
      pass = JAXRPC_Data.compareArrayValues(values, response, "String");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "SoapStringTest");
    return pass;
  }

  private boolean SoapBoolean1ArrayTest() {
    TestUtil.logMsg("SoapBoolean1ArrayTest");
    boolean pass = false;
    Boolean values[] = JAXRPC_Data.Boolean_data;
    Boolean[] response;
    TestUtil.logMsg("Passing/Returning array data to/from JAXRPC Service");
    try {
      response = port.echoSoapBoolean1Array(values);
      pass = JAXRPC_Data.compareArrayValues(values, response, "Boolean");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "SoapBooleanTest");
    return pass;
  }

  private boolean SoapFloat1ArrayTest() {
    TestUtil.logMsg("SoapFloat1ArrayTest");
    boolean pass = false;
    Float values[] = JAXRPC_Data.Float_data;
    Float[] response;
    TestUtil.logMsg("Passing/Returning array data to/from JAXRPC Service");
    try {
      response = port.echoSoapFloat1Array(values);
      pass = JAXRPC_Data.compareArrayValues(values, response, "Float");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "SoapFloatTest");
    return pass;
  }

  private boolean SoapDouble1ArrayTest() {
    TestUtil.logMsg("SoapDouble1ArrayTest");
    boolean pass = false;
    Double values[] = JAXRPC_Data.Double_data;
    Double[] response;
    TestUtil.logMsg("Passing/Returning array data to/from JAXRPC Service");
    try {
      response = port.echoSoapDouble1Array(values);
      pass = JAXRPC_Data.compareArrayValues(values, response, "Double");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "SoapDoubleTest");
    return pass;
  }

  private boolean SoapDecimal1ArrayTest() {
    TestUtil.logMsg("SoapDecimalrrayTest");
    boolean pass = false;
    BigDecimal values[] = JAXRPC_Data.BigDecimal_data;
    BigDecimal[] response;
    TestUtil.logMsg("Passing/Returning array data to/from JAXRPC Service");
    try {
      response = port.echoSoapDecimal1Array(values);
      pass = JAXRPC_Data.compareArrayValues(values, response, "BigDecimal");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "SoapDecimalest");
    return pass;
  }

  private boolean SoapInt1ArrayTest() {
    TestUtil.logMsg("SoapInt1ArrayTest");
    boolean pass = false;
    Integer values[] = JAXRPC_Data.Integer_data;
    Integer[] response;
    TestUtil.logMsg("Passing/Returning array data to/from JAXRPC Service");
    try {
      response = port.echoSoapInt1Array(values);
      pass = JAXRPC_Data.compareArrayValues(values, response, "Integer");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "SoapIntTest");
    return pass;
  }

  private boolean SoapShort1ArrayTest() {
    TestUtil.logMsg("SoapShort1ArrayTest");
    boolean pass = false;
    Short values[] = JAXRPC_Data.Short_data;
    Short[] response;
    TestUtil.logMsg("Passing/Returning array data to/from JAXRPC Service");
    try {
      response = port.echoSoapShort1Array(values);
      pass = JAXRPC_Data.compareArrayValues(values, response, "Short");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "SoapShortTest");
    return pass;
  }

  private boolean SoapByte1ArrayTest() {
    TestUtil.logMsg("SoapByte1ArrayTest");
    boolean pass = false;
    Byte values[] = JAXRPC_Data.Byte_data;
    Byte[] response;
    TestUtil.logMsg("Passing/Returning array data to/from JAXRPC Service");
    try {
      response = port.echoSoapByte1Array(values);
      pass = JAXRPC_Data.compareArrayValues(values, response, "Byte");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "SoapByteTest");
    return pass;
  }

  private boolean Base64Binary1ArrayTest() {
    TestUtil.logMsg("Base64Binary1ArrayTest");
    boolean pass = false;
    byte values[][] = JAXRPC_Data.byte_multi_data;
    byte[][] response;

    TestUtil.logMsg("Passing/Returning array data to/from JAXRPC Service");
    try {
      response = port.echoBase64Binary1Array(values);
      pass = JAXRPC_Data.compareMultiArrayValues(values, response, "byte");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }

    printTestStatus(pass, "Base64Binary1ArrayTest");
    return pass;
  }

  private boolean HexBinary1ArrayTest() {
    TestUtil.logMsg("HexBinary1ArrayTest");
    boolean pass = false;
    byte values[][] = JAXRPC_Data.byte_multi_data;
    byte[][] response;

    TestUtil.logMsg("Passing/Returning array data to/from JAXRPC Service");
    try {
      response = port.echoHexBinary1Array(values);
      pass = JAXRPC_Data.compareMultiArrayValues(values, response, "byte");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }

    printTestStatus(pass, "HexBinary1ArrayTest");
    return pass;
  }

  private boolean SoapBase641ArrayTest() {
    TestUtil.logMsg("SoapBase641ArrayTest");
    boolean pass = false;
    byte values[][] = JAXRPC_Data.byte_multi_data;
    byte[][] response;
    TestUtil.logMsg("Passing/Returning array data to/from JAXRPC Service");
    try {
      response = port.echoSoapBase641Array(values);
      pass = JAXRPC_Data.compareMultiArrayValues(values, response, "byte");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "SoapBase641ArrayTest");
    return pass;
  }

  private boolean AllStructTest() {
    TestUtil.logMsg("AllStructTest");
    boolean pass = true;
    AllStruct values[] = AllStruct_data;
    AllStruct response;

    TestUtil.logMsg("Passing/Returning struct data to/from JAXRPC Service");
    try {
      for (int i = 0; i < values.length; i++) {
        response = port.echoAllStruct(values[i]);

        if (values[i] == null && response == null) {
          continue;
        } else if (!compareAllStruct(response, values[i])) {
          TestUtil.logErr("AllStructTest failed - \nexpected: "
              + AllStructToString(values[i]) + "\n  received: "
              + AllStructToString(response));
          pass = false;
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }

    printTestStatus(pass, "AllStructTest");
    return pass;
  }

  private boolean AllStructArrayTest() {
    TestUtil.logMsg("AllStructArrayTest");
    boolean pass = true;
    AllStruct values[] = AllStruct_data;
    AllStruct[] response;

    TestUtil
        .logMsg("Passing/Returning struct array data to/from JAXRPC Service");
    try {
      response = port.echoAllStructArray(values);
      if (values.length != response.length) {
        TestUtil.logErr("Array Size MisMatch: expected " + values.length
            + "\n received " + response.length);
        pass = false;
      }
      for (int i = 0; i < values.length; i++) {
        if (values[i] == null && response == null) {
          continue;
        } else if (!compareAllStruct(response[i], values[i])) {
          TestUtil.logErr("AllStructArrayTest failed - \nexpected: "
              + AllStructToString(values[i]) + "\nreceived: "
              + AllStructToString(response[i]));
          pass = false;
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }

    printTestStatus(pass, "AllStructArrayTest");
    return pass;
  }

  private boolean AllStruct2Test() {
    TestUtil.logMsg("AllStruct2Test");
    boolean pass = true;
    AllStruct2 values[] = AllStruct2_data;
    AllStruct2 response;

    TestUtil.logMsg("Passing/Returning struct data to/from JAXRPC Service");
    try {
      for (int i = 0; i < values.length; i++) {
        AllStruct2ToString(values[i]);
        response = port.echoAllStruct2(values[i]);

        if (values[i] == null && response == null) {
          continue;
        } else if (!compareAllStruct2(response, values[i])) {
          TestUtil.logErr("AllStruct2Test failed - \nexpected: "
              + AllStruct2ToString(values[i]) + "\nreceived: "
              + AllStruct2ToString(response));
          pass = false;
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }

    printTestStatus(pass, "AllStruct2Test");
    return pass;
  }

  private boolean AllStruct2ArrayTest() {
    TestUtil.logMsg("AllStruct2ArrayTest");
    boolean pass = true;
    AllStruct2 values[] = AllStruct2_data;
    AllStruct2[] response;

    TestUtil
        .logMsg("Passing/Returning struct array data to/from JAXRPC Service");
    try {
      response = port.echoAllStruct2Array(values);
      if (values.length != response.length) {
        TestUtil.logErr("Array Size MisMatch: expected " + values.length
            + "\nreceived " + response.length);
        pass = false;
      }
      for (int i = 0; i < values.length; i++) {
        if (values[i] == null && response == null) {
          continue;
        } else if (!compareAllStruct2(response[i], values[i])) {
          TestUtil.logErr("AllStruct2ArrayTest failed - \nexpected: "
              + AllStruct2ToString(values[i]) + "\nreceived: "
              + AllStruct2ToString(response[i]));
          pass = false;
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }

    printTestStatus(pass, "AllStruct2ArrayTest");
    return pass;
  }

  private boolean SequenceStructTest() {
    TestUtil.logMsg("SequenceStructTest");
    boolean pass = true;
    SequenceStruct values[] = SequenceStruct_data;
    SequenceStruct response;
    TestUtil.logMsg("Passing/Returning struct data to/from JAXRPC Service");
    try {
      for (int i = 0; i < values.length; i++) {
        SequenceStructToString(values[i]);
        response = port.echoSequenceStruct(values[i]);

        if (values[i] == null && response == null) {
          continue;
        } else if (!compareSequenceStruct(response, values[i])) {
          TestUtil.logErr("SequenceStructTest failed - \nexpected: "
              + SequenceStructToString(values[i]) + "\nreceived: "
              + SequenceStructToString(response));
          pass = false;
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }

    printTestStatus(pass, "SequenceStructTest");
    return pass;
  }

  private boolean SequenceStructArrayTest() {
    TestUtil.logMsg("SequenceStructArrayTest");
    boolean pass = true;
    SequenceStruct values[] = SequenceStruct_data;
    SequenceStruct[] response;

    TestUtil
        .logMsg("Passing/Returning struct array data to/from JAXRPC Service");
    try {
      response = port.echoSequenceStructArray(values);
      if (values.length != response.length) {
        TestUtil.logErr("Array Size MisMatch: expected " + values.length
            + "\nreceived " + response.length);
        pass = false;
      }
      for (int i = 0; i < values.length; i++) {
        if (values[i] == null && response == null) {
          continue;
        } else if (!compareSequenceStruct(response[i], values[i])) {
          TestUtil.logErr("SequenceStructArrayTest failed - \nexpected: "
              + SequenceStructToString(values[i]) + "\nreceived: "
              + SequenceStructToString(response[i]));
          pass = false;
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }

    printTestStatus(pass, "SequenceStructArrayTest");
    return pass;
  }

  private boolean SequenceStruct2Test() {
    TestUtil.logMsg("SequenceStruct2Test");
    boolean pass = true;
    SequenceStruct2 values[] = SequenceStruct2_data;
    SequenceStruct2 response;

    TestUtil.logMsg("Passing/Returning struct data to/from JAXRPC Service");
    try {
      for (int i = 0; i < values.length; i++) {
        SequenceStruct2ToString(values[i]);
        response = port.echoSequenceStruct2(values[i]);
        if (values[i] == null && response == null) {
          continue;
        } else if (!compareSequenceStruct2(response, values[i])) {
          TestUtil.logErr("SequenceStruct2Test failed - \nexpected: "
              + SequenceStruct2ToString(values[i]) + "\nreceived: "
              + SequenceStruct2ToString(response));
          pass = false;
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }

    printTestStatus(pass, "SequenceStruct2Test");
    return pass;
  }

  private boolean SequenceStruct2ArrayTest() {
    TestUtil.logMsg("SequenceStruct2ArrayTest");
    boolean pass = true;
    SequenceStruct2 values[] = SequenceStruct2_data;
    SequenceStruct2[] response;

    TestUtil
        .logMsg("Passing/Returning struct array data to/from JAXRPC Service");
    try {
      response = port.echoSequenceStruct2Array(values);
      if (values.length != response.length) {
        TestUtil.logErr("Array Size MisMatch: expected " + values.length
            + "\nreceived " + response.length);
        pass = false;
      }
      for (int i = 0; i < values.length; i++) {
        if (values[i] == null && response == null) {
          continue;
        } else if (!compareSequenceStruct2(response[i], values[i])) {
          TestUtil.logErr("SequenceStruct2ArrayTest failed - \nexpected: "
              + SequenceStruct2ToString(values[i]) + "\nreceived: "
              + SequenceStruct2ToString(response[i]));
          pass = false;
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }

    printTestStatus(pass, "SequenceStruct2ArrayTest");
    return pass;
  }

  private boolean VoidTest() {
    TestUtil.logMsg("VoidTest");
    boolean pass = true;

    try {
      port.echoVoid();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }

    printTestStatus(pass, "VoidTest");
    return pass;
  }

  /*
   * private boolean ServiceExceptionTest() { TestUtil.logMsg(
   * "ServiceExceptionTest" ); boolean pass = true; try { TestUtil.logMsg(
   * "Marshalling data expecting an exception" ); port.echoServiceException(
   * "serviceexception_foo_error" ); // test failed if it got to here
   * TestUtil.logErr( "Error: expected an exception to be thrown"); pass =
   * false; } catch ( MarshallTestEchoServiceExceptionServiceException se ) {
   * pass=true; } catch ( Exception e ) { TestUtil.logErr( "Caught exception: "
   * + e.getMessage() ); TestUtil.printStackTrace(e); pass = false; }
   * printTestStatus( pass, "ServiceExceptionTest" ); return pass; }
   */

  private boolean RemoteExceptionTest() {
    TestUtil.logMsg("RemoteExceptionTest");
    boolean pass = true;
    try {
      TestUtil.logMsg("Marshalling data expecting an exception");
      port.echoRemoteException("remoteexception_foo_error");
      // test failed if it got to here
      TestUtil.logErr("Error: expected an exception to be thrown");
      pass = false;
    } catch (java.rmi.RemoteException e) {
      TestUtil.logMsg("Caught RemoteException: " + e);
      TestUtil.logMsg(" - message: " + e.getMessage());
    } catch (Exception e) {
      TestUtil.logMsg("Caught Exception: " + e);
      TestUtil.logMsg(" - message: " + e.getMessage());
    }

    printTestStatus(pass, "RemoteExceptionTest");
    return pass;
  }

  private boolean SubClassRemoteExceptionTest() {
    TestUtil.logMsg("SubClassRemoteExceptionTest");
    boolean pass = true;
    try {
      TestUtil.logMsg("Marshalling data expecting an exception");
      port.echoSubClassRemoteException("remoteexception_foo_error");
      // test failed if it got to here
      TestUtil.logErr("Error: expected an exception to be thrown");
      pass = false;
    } catch (java.rmi.RemoteException e) {
      TestUtil.logMsg("Caught RemoteException: " + e);
      TestUtil.logMsg(" - message: " + e.getMessage());
    } catch (Exception e) {
      TestUtil.logMsg("Caught Exception: " + e);
      TestUtil.logMsg(" - message: " + e.getMessage());
    }

    printTestStatus(pass, "SubClassRemoteExceptionTest");
    return pass;
  }

  private boolean StringEnumerationsTest() {
    TestUtil.logMsg("StringEnumerationsTest");
    boolean pass = true;
    if (!setupEnumData("EnumString")) {
      TestUtil.logErr(
          "StringEnumerationsTest failed - data could not be initialized");
      pass = false;
      printTestStatus(pass, "StringEnumerationsTest");
      return pass;
    }
    EnumString values[] = EnumString_data;
    EnumString response;

    try {
      for (int i = 0; i < values.length; i++) {
        response = port.echoEnumString(values[i]);

        if (!response.getValue().equals(values[i].getValue())) {
          TestUtil.logErr("StringEnumerationsTest failed - expected "
              + values[i].getValue() + ",  received: " + response.getValue());
          pass = false;
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }

    printTestStatus(pass, "StringEnumerationsTest");
    return pass;
  }

  private boolean IntegerEnumerationsTest() {
    TestUtil.logMsg("IntegerEnumerationsTest");
    boolean pass = true;

    if (!setupEnumData("EnumInteger")) {
      TestUtil.logErr(
          "IntegerEnumerationsTest failed - data could not be initialized");
      pass = false;
      printTestStatus(pass, "IntegerEnumerationsTest");
      return pass;
    }
    EnumInteger values[] = EnumInteger_data;
    EnumInteger response;

    try {
      for (int i = 0; i < values.length; i++) {
        response = port.echoEnumInteger(values[i]);

        if (!response.getValue().equals(values[i].getValue())) {
          TestUtil.logErr("IntegerEnumerationsTest failed - expected "
              + values[i].getValue() + ",  received: " + response.getValue());
          pass = false;
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }

    printTestStatus(pass, "IntegerEnumerationsTest");
    return pass;
  }

  private boolean IntEnumerationsTest() {
    TestUtil.logMsg("IntEnumerationsTest");
    boolean pass = true;

    if (!setupEnumData("EnumInt")) {
      TestUtil
          .logErr("IntEnumerationsTest failed - data could not be initialized");
      pass = false;
      printTestStatus(pass, "IntEnumerationsTest");
      return pass;
    }
    EnumInt values[] = EnumInt_data;
    EnumInt response;

    try {
      for (int i = 0; i < values.length; i++) {
        response = port.echoEnumInt(values[i]);

        if (response.getValue() != values[i].getValue()) {
          TestUtil.logErr("IntEnumerationsTest failed - expected "
              + values[i].getValue() + ",  received: " + response.getValue());
          pass = false;
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "IntEnumerationsTest");
    return pass;
  }

  private boolean LongEnumerationsTest() {
    TestUtil.logMsg("LongEnumerationsTest");
    boolean pass = true;
    if (!setupEnumData("EnumLong")) {
      TestUtil.logErr(
          "LongEnumerationsTest failed - data could not be initialized");
      pass = false;
      printTestStatus(pass, "LongEnumerationsTest");
      return pass;
    }
    EnumLong values[] = EnumLong_data;
    EnumLong response;

    try {
      for (int i = 0; i < values.length; i++) {
        response = port.echoEnumLong(values[i]);

        if (response.getValue() != values[i].getValue()) {
          TestUtil.logErr("LongEnumerationsTest failed - expected "
              + values[i].getValue() + ",  received: " + response.getValue());
          pass = false;
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "LongEnumerationsTest");
    return pass;
  }

  private boolean ShortEnumerationsTest() {
    TestUtil.logMsg("ShortEnumerationsTest");
    boolean pass = true;

    if (!setupEnumData("EnumShort")) {
      TestUtil.logErr(
          "ShortEnumerationsTest failed - data could not be initialized");
      pass = false;
      printTestStatus(pass, "ShortEnumerationsTest");
      return pass;
    }
    EnumShort values[] = EnumShort_data;
    EnumShort response;

    try {
      for (int i = 0; i < values.length; i++) {
        response = port.echoEnumShort(values[i]);

        if (response.getValue() != values[i].getValue()) {
          TestUtil.logErr("ShortEnumerationsTest failed - expected "
              + values[i].getValue() + ",  received: " + response.getValue());
          pass = false;
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "ShortEnumerationsTest");
    return pass;
  }

  private boolean DecimalEnumerationsTest() {
    TestUtil.logMsg("DecimalEnumerationsTest");
    boolean pass = true;

    if (!setupEnumData("EnumDecimal")) {
      TestUtil.logErr(
          "DecimalEnumerationsTest failed - data could not be initialized");
      pass = false;
      printTestStatus(pass, "DecimalEnumerationsTest");
      return pass;
    }
    EnumDecimal values[] = EnumDecimal_data;
    EnumDecimal response;

    try {
      for (int i = 0; i < values.length; i++) {
        response = port.echoEnumDecimal(values[i]);

        if (!response.getValue().equals(values[i].getValue())) {
          TestUtil.logErr("DecimalEnumerationsTest failed - expected "
              + values[i].getValue() + ",  received: " + response.getValue());
          pass = false;
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "DecimalEnumerationsTest");
    return pass;
  }

  private boolean FloatEnumerationsTest() {
    TestUtil.logMsg("FloatEnumerationsTest");
    boolean pass = true;

    if (!setupEnumData("EnumFloat")) {
      TestUtil.logErr(
          "FloatEnumerationsTest failed - data could not be initialized");
      pass = false;
      printTestStatus(pass, "FloatEnumerationsTest");
      return pass;
    }
    EnumFloat values[] = EnumFloat_data;
    EnumFloat response;

    try {
      for (int i = 0; i < values.length; i++) {
        response = port.echoEnumFloat(values[i]);

        if (response.getValue() != values[i].getValue()) {
          TestUtil.logErr("FloatEnumerationsTest failed - expected "
              + values[i].getValue() + ",  received: " + response.getValue());
          pass = false;
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "FloatEnumerationsTest");
    return pass;
  }

  private boolean DoubleEnumerationsTest() {
    TestUtil.logMsg("DoubleEnumerationsTest");
    boolean pass = true;

    if (!setupEnumData("EnumDouble")) {
      TestUtil.logErr(
          "DoubleEnumerationsTest failed - data could not be initialized");
      pass = false;
      printTestStatus(pass, "DoubleEnumerationsTest");
      return pass;
    }
    EnumDouble values[] = EnumDouble_data;
    EnumDouble response;

    try {
      for (int i = 0; i < values.length; i++) {
        response = port.echoEnumDouble(values[i]);

        if (response.getValue() != values[i].getValue()) {
          TestUtil.logErr("DoubleEnumerationsTest failed - expected "
              + values[i].getValue() + ",  received: " + response.getValue());
          pass = false;
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "DoubleEnumerationsTest");
    return pass;
  }

  private boolean ByteEnumerationsTest() {
    TestUtil.logMsg("ByteEnumerationsTest");
    boolean pass = true;

    if (!setupEnumData("EnumByte")) {
      TestUtil.logErr(
          "ByteEnumerationsTest failed - data could not be initialized");
      pass = false;
      printTestStatus(pass, "ByteEnumerationsTest");
      return pass;
    }
    EnumByte values[] = EnumByte_data;
    EnumByte response;

    try {
      for (int i = 0; i < values.length; i++) {
        response = port.echoEnumByte(values[i]);

        if (response.getValue() != values[i].getValue()) {
          TestUtil.logErr("ByteEnumerationsTest failed - expected "
              + values[i].getValue() + ",  received: " + response.getValue());
          pass = false;
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "ByteEnumerationsTest");
    return pass;
  }

  private boolean nilTrueTest() {
    TestUtil.logMsg("nilTrueTest");
    boolean pass = true;
    NilTrueStruct values[] = NilTrueStruct_data;
    NilTrueStruct response;
    TestUtil.logMsg("Passing/Returning data to/from JAXRPC Service");
    try {
      for (int i = 0; i < values.length; i++) {
        response = port.echonilTrue(values[i]);
        if (values[i] == null && response == null) {
          continue;
        } else if (!compareNilTrueStruct(response, values[i])) {
          TestUtil.logErr("nilTrueTest failed - expected " + values[i]
              + ",  received: " + response);
          pass = false;
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "nilTrueTest");
    return pass;
  }

  private boolean nilFalseTest() {
    TestUtil.logMsg("nilFalseTest");
    boolean pass = true;
    NilFalseStruct values[] = NilFalseStruct_data;
    NilFalseStruct response;
    TestUtil.logMsg("Passing/Returning data to/from JAXRPC Service");
    try {
      for (int i = 0; i < values.length; i++) {
        response = port.echonilFalse(values[i]);
        if (values[i] == null && response == null) {
          continue;
        } else if (!compareNilFalseStruct(response, values[i])) {
          TestUtil.logErr("nilFalseTest failed - expected " + values[i]
              + ",  received: " + response);
          pass = false;
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "nilFalseTest");
    return pass;
  }
}
