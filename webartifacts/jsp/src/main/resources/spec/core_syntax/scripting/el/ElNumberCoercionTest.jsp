<%--

    Copyright (c) 2003, 2018 Oracle and/or its affiliates. All rights reserved.

    This program and the accompanying materials are made available under the
    terms of the Eclipse Public License v. 2.0, which is available at
    http://www.eclipse.org/legal/epl-2.0.

    This Source Code may also be made available under the following Secondary
    Licenses when the conditions for such availability set forth in the
    Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
    version 2 with the GNU Classpath Exception, which is available at
    https://www.gnu.org/software/classpath/license.html.

    SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0

--%>

<%@ page
	import="java.math.BigInteger,java.math.BigDecimal"%>
<%@ page contentType="text/plain"%>
<%@ taglib uri="http://java.sun.com/tck/jsp/el" prefix="el"%>

<jsp:useBean id="type" scope="page"
	class="com.sun.ts.tests.jsp.spec.core_syntax.scripting.el.TypeBean" />

<%-- Validate EL Number coercions --%>

<%-- Validate boolean's cause an error condition --%>
<%
    try {
%>
<el:checkCoercion control='<%= new Boolean(true) %>'
	byte="${type.booln}" />
Test FAILED. Expected an error condition when a number (Byte) type is
required and the EL expression yields a Boolean.
<%
    } catch (Throwable t) {
    }
%>

<%
    try {
%>
<el:checkCoercion control='<%= new Boolean(true) %>'
	short="${type.booln}" />
Test FAILED. Expected an error condition when a number type (Short) is
required and the EL expression yields a Boolean.
<%
    } catch (Throwable t) {
    }
%>

<%
    try {
%>
<el:checkCoercion control='<%= new Boolean(true) %>' int="${type.booln}" />
Test FAILED. Expected an error condition when a number type (Integer) is
required and the EL expression yields a Boolean.
<%
    } catch (Throwable t) {
    }
%>

<%
    try {
%>
<el:checkCoercion control='<%= new Boolean(true) %>'
	long="${type.booln}" />
Test FAILED. Expected an error condition when a number type (Long) is
required and the EL expression yields a Boolean.
<%
    } catch (Throwable t) {
    }
%>

<%
    try {
%>
<el:checkCoercion control='<%= new Boolean(true) %>'
	float="${type.booln}" />
Test FAILED. Expected an error condition when a number type (Float) is
required and the EL expression yields a Boolean.
<%
    } catch (Throwable t) {
    }
%>

<%
    try {
%>
<el:checkCoercion control='<%= new Boolean(true) %>'
	double="${type.booln}" />
Test FAILED. Expected an error condition when a number type (Double) is
required and the EL expression yields a Boolean.
<%
    } catch (Throwable t) {
    }
%>

<%-- Validate failure in the case where strings can't be converted --%>

<%
    try {
%>
<el:checkCoercion control='<%= "a" %>' byte="${'a'}" />
Test FAILED. Expected an error condition when a number type (Byte) is
required and the EL expression is unable to conver the provided String
expression into a Number type.
<%
    } catch (Throwable t) {
    }
%>

<%
    try {
%>
<el:checkCoercion control='<%= "a" %>' short="${'a'}" />
Test FAILED. Expected an error condition when a number type (Short) is
required and the EL expression is unable to conver the provided String
expression into a Number type.
<%
    } catch (Throwable t) {
    }
%>

<%
    try {
%>
<el:checkCoercion control='<%= "a" %>' int="${'a'}" />
Test FAILED. Expected an error condition when a number type (Integer) is
required and the EL expression is unable to conver the provided String
expression into a Number type.
<%
    } catch (Throwable t) {
    }
%>

<%
    try {
%>
<el:checkCoercion control='<%= "a" %>' long="${'a'}" />
Test FAILED. Expected an error condition when a number type (Long) is
required and the EL expression is unable to conver the provided String
expression into a Number type.
<%
    } catch (Throwable t) {
    }
%>

<%
    try {
%>
<el:checkCoercion control='<%= "a" %>' float="${'a'}" />
Test FAILED. Expected an error condition when a number type (Float) is
required and the EL expression is unable to conver the provided String
expression into a Number type.
<%
    } catch (Throwable t) {
    }
%>

<%
    try {
%>
<el:checkCoercion control='<%= "a" %>' double="${'a'}" />
Test FAILED. Expected an error condition when a number type (Double) is
required and the EL expression is unable to conver the provided String
expression into a Number type.
<%
    } catch (Throwable t) {
    }
%>

<%-- Validate error condition for any other type --%>

<%
    try {
%>
<el:checkCoercion control='<%= "a" %>' byte="${type}" />
Test FAILED. Expected an error condition when an Object that can't be
converted to a Number type is provided as a value for an attribute that
only accepts Number types (byte).
<%
    } catch (Throwable t) {
    }
%>

<%
    try {
%>
<el:checkCoercion control='<%= "a" %>' short="${type}" />
Test FAILED. Expected an error condition when an Object that can't be
converted to a Number type is provided as a value for an attribute that
only accepts Number types (Short).
<%
    } catch (Throwable t) {
    }
%>

<%
    try {
%>
<el:checkCoercion control='<%= "a" %>' int="${type}" />
Test FAILED. Expected an error condition when an Object that can't be
converted to a Number type is provided as a value for an attribute that
only accepts Number types (Integer).
<%
    } catch (Throwable t) {
    }
%>

<%
    try {
%>
<el:checkCoercion control='<%= "a" %>' long="${type}" />
Test FAILED. Expected an error condition when an Object that can't be
converted to a Number type is provided as a value for an attribute that
only accepts Number types (Long).
<%
    } catch (Throwable t) {
    }
%>

<%
    try {
%>
<el:checkCoercion control='<%= "a" %>' float="${type}" />
Test FAILED. Expected an error condition when an Object that can't be
converted to a Number type is provided as a value for an attribute that
only accepts Number types (Float).
<%
    } catch (Throwable t) {
    }
%>

<%
    try {
%>
<el:checkCoercion control='<%= "a" %>' double="${type}" />
Test FAILED. Expected an error condition when an Object that can't be
converted to a Number type is provided as a value for an attribute that
only accepts Number types (Double).
<%
    } catch (Throwable t) {
    }
%>

<%-- Validate Number coercions --%>

<el:checkCoercion name="ByteToByte" control='<%= new Byte((byte) 30) %>'
	byte="${type.bite}" />
<el:checkCoercion name="ShortToByte"
	control='<%= new Byte(new Short((short) 32).byteValue()) %>'
	byte="${type.shrt}" />
<el:checkCoercion name="IntToByte"
	control='<%= new Byte(new Integer(33).byteValue()) %>'
	byte="${type.inti}" />
<el:checkCoercion name="LongToByte"
	control='<%= new Byte(new Long(34L).byteValue()) %>' byte="${type.lng}" />
<el:checkCoercion name="FloatToByte"
	control='<%= new Byte(new Float(35.5f).byteValue()) %>'
	byte="${type.flote}" />
<el:checkCoercion name="DoubleToByte"
	control='<%= new Byte(new Double(36.5d).byteValue()) %>'
	byte="${type.dble}" />
<el:checkCoercion name="BigIntegerToByte"
	control='<%= new Byte(new BigInteger("125").byteValue()) %>'
	byte="${type.bigInt}" />
<el:checkCoercion name="BigDecimaToByte"
	control='<%= new Byte(new BigDecimal(100.5).byteValue()) %>'
	byte="${type.bigDec}" />
<el:checkCoercion name="ByteToShort"
	control='<%= new Short(new Byte((byte) 30).shortValue()) %>'
	short="${type.bite}" />
<el:checkCoercion name="ShortToShort"
	control='<%= new Short((short) 32) %>' short="${type.shrt}" />
<el:checkCoercion name="IntToShort"
	control='<%= new Short(new Integer(33).shortValue()) %>'
	short="${type.inti}" />
<el:checkCoercion name="LongToShort"
	control='<%= new Short(new Long(34L).shortValue()) %>'
	short="${type.lng}" />
<el:checkCoercion name="FloatToShort"
	control='<%= new Short(new Float(35.5f).shortValue()) %>'
	short="${type.flote}" />
<el:checkCoercion name="BigIntegerToShort"
	control='<%= new Short(new BigInteger("125").shortValue()) %>'
	short="${type.bigInt}" />
<el:checkCoercion name="BigDecimalToShort"
	control='<%= new Short(new BigDecimal(100.5).shortValue()) %>'
	short="${type.bigDec}" />
<el:checkCoercion name="DoubleToShort"
	control='<%= new Short(new Double(36.5d).shortValue()) %>'
	short="${type.dble}" />
<el:checkCoercion name="ByteToInt"
	control='<%= new Integer(new Byte((byte) 30).intValue()) %>'
	int="${type.bite}" />
<el:checkCoercion name="ShortToInt"
	control='<%= new Integer(new Short((short) 32).intValue()) %>'
	int="${type.shrt}" />
<el:checkCoercion name="IntToInt" control='<%= new Integer(33) %>'
	int="${type.inti}" />
<el:checkCoercion name="LongToInt"
	control='<%= new Integer(new Long(34L).intValue()) %>'
	int="${type.lng}" />
<el:checkCoercion name="FloatToInt"
	control='<%= new Integer(new Float(35.5f).intValue()) %>'
	int="${type.flote}" />
<el:checkCoercion name="DoubleToInt"
	control='<%= new Integer(new Double(36.5d).intValue()) %>'
	int="${type.dble}" />
<el:checkCoercion name="BigIntegerToInt"
	control='<%= new Integer(new BigInteger("125").intValue()) %>'
	int="${type.bigInt}" />
<el:checkCoercion name="BigDecimalToInt"
	control='<%= new Integer(new BigDecimal(100.5).intValue()) %>'
	int="${type.bigDec}" />
<el:checkCoercion name="ByteToLong"
	control='<%= new Long(new Byte((byte) 30).longValue()) %>'
	long="${type.bite}" />
<el:checkCoercion name="ShortToLong"
	control='<%= new Long(new Short((short) 32).longValue()) %>'
	long="${type.shrt}" />
<el:checkCoercion name="IntToLong"
	control='<%= new Long(new Integer(33).longValue()) %>'
	long="${type.inti}" />
<el:checkCoercion name="LongToInt" control='<%= new Long(34L) %>'
	long="${type.lng}" />
<el:checkCoercion name="FloatToLong"
	control='<%= new Long(new Float(35.5f).longValue()) %>'
	long="${type.flote}" />
<el:checkCoercion name="DoubleToLong"
	control='<%= new Long(new Double(36.5d).longValue()) %>'
	long="${type.dble}" />
<el:checkCoercion name="BigIntegerToLong"
	control='<%= new Long(new BigInteger("125").longValue()) %>'
	long="${type.bigInt}" />
<el:checkCoercion name="BigDecimalToLong"
	control='<%= new Long(new BigDecimal(100.5).longValue()) %>'
	long="${type.bigDec}" />
<el:checkCoercion name="ByteToFloat"
	control='<%= new Float(new Byte((byte) 30).floatValue()) %>'
	float="${type.bite}" />
<el:checkCoercion name="ShortToFloat"
	control='<%= new Float(new Short((short) 32).floatValue()) %>'
	float="${type.shrt}" />
<el:checkCoercion name="IntToFloat"
	control='<%= new Float(new Integer(33).floatValue()) %>'
	float="${type.inti}" />
<el:checkCoercion name="LongToFloat"
	control='<%= new Float(new Long(34L).floatValue()) %>'
	float="${type.lng}" />
<el:checkCoercion name="FloatToFloat" control='<%= new Float(35.5f) %>'
	float="${type.flote}" />
<el:checkCoercion name="DoubleToFloat"
	control='<%= new Float(new Double(36.5d).floatValue()) %>'
	float="${type.dble}" />
<el:checkCoercion name="BigIntegerToFloat"
	control='<%= new Float(new BigInteger("125").floatValue()) %>'
	float="${type.bigInt}" />
<el:checkCoercion name="BigDecimalToFloat"
	control='<%= new Float(new BigDecimal(100.5).floatValue()) %>'
	float="${type.bigDec}" />
<el:checkCoercion name="ByteToDouble"
	control='<%= new Double(new Byte((byte) 30).doubleValue()) %>'
	double="${type.bite}" />
<el:checkCoercion name="ShortToDouble"
	control='<%= new Double(new Short((short) 32).doubleValue()) %>'
	double="${type.shrt}" />
<el:checkCoercion name="IntToDouble"
	control='<%= new Double(new Integer(33).doubleValue()) %>'
	double="${type.inti}" />
<el:checkCoercion name="LongToDouble"
	control='<%= new Double(new Long(34L).doubleValue()) %>'
	double="${type.lng}" />
<el:checkCoercion name="FloatToDouble"
	control='<%= new Double(new Float(35.5f).doubleValue()) %>'
	double="${type.flote}" />
<el:checkCoercion name="DoubleToDouble"
	control='<%= new Double(36.5d) %>' double="${type.dble}" />
<el:checkCoercion name="BigIntegerToDouble"
	control='<%= new Double(new BigInteger("125").doubleValue()) %>'
	double="${type.bigInt}" />
<el:checkCoercion name="BigDecimalToDouble"
	control='<%= new Double(new BigDecimal(100.5).doubleValue()) %>'
	double="${type.bigDec}" />
<el:checkCoercion name="StringToByte" control='<%= Byte.valueOf("7") %>'
	byte="${'7'}" />
<el:checkCoercion name="StringToShort"
	control='<%= Short.valueOf("9") %>' short="${'9'}" />
<el:checkCoercion name="StringToInt"
	control='<%= Integer.valueOf("10") %>' int="${'10'}" />
<el:checkCoercion name="StringToLong"
	control='<%= Long.valueOf("11") %>' long="${'11'}" />
<el:checkCoercion name="StringToFloat"
	control='<%= Float.valueOf("12.5") %>' float="${'12.5'}" />
<el:checkCoercion name="StringToDouble"
	control='<%= Double.valueOf("13.5") %>' double="${'13.5'}" />
<el:checkCoercion name="StringToBigInteger"
	control='<%= new BigInteger("125") %>' bigInteger="${'125'}" />
<el:checkCoercion name="StringToBigDecimal" control='<%= new BigDecimal("100.5") %>' 
	bigDecimal="${'100.5'}" />
<el:checkCoercion name="EmptyStringToByte" control='<%= new Byte((byte) 0) %>' 
	byte="${''}" />
<el:checkCoercion name="EmptyStringToShort" control='<%= new Short((short) 0) %>' 
	short="${''}" />
<el:checkCoercion name="EmptyToInt" control='<%= new Integer(0) %>'
	int="${''}" />
<el:checkCoercion name="EmptyToLong" control='<%= new Long(0L) %>'
	long="${''}" />
<el:checkCoercion name="EmptyToFloat" control='<%= new Float(0f) %>'
	float="${''}" />
<el:checkCoercion name="EmptyToDouble" control='<%= new Double(0d) %>'
	double="${''}" />
<el:checkCoercion name="EmptyToBigInteger"
	control='<%= new BigInteger("0") %>' bigInteger="${''}" />
<el:checkCoercion name="EmptyToBigDecimal"
	control='<%= new BigDecimal(0) %>' bigDecimal="${''}" />
<el:checkCoercion name="CharToByte"
	control="<%= new Byte(new Short((short) new Character((char) 31).charValue()).byteValue()) %>"
	byte="${type.chr}" />
<el:checkCoercion name="CharToShort"
	control="<%= new Short((short) new Character((char) 31).charValue()) %>"
	short="${type.chr}" />
<el:checkCoercion name="CharToInt"
	control="<%= new Integer(new Short((short) new Character((char) 31).charValue()).intValue()) %>"
	int="${type.chr}" />
<el:checkCoercion name="CharToLong"
	control="<%= new Long(new Short((short) new Character((char) 31).charValue()).longValue()) %>"
	long="${type.chr}" />
<el:checkCoercion name="CharToFloat"
	control="<%= new Float(new Short((short) new Character((char) 31).charValue()).floatValue()) %>"
	float="${type.chr}" />
<el:checkCoercion name="CharToBigInteger"
	control="<%= BigInteger.valueOf(new Short((short) new Character((char) 31).charValue()).longValue()) %>"
	bigInteger="${type.chr}" />
<el:checkCoercion name="CharToBigDecimal"
	control="<%= new BigDecimal(new Short((short) new Character((char) 31).charValue()).doubleValue()) %>"
	bigDecimal="${type.chr}" />
<el:checkCoercion name="CharToDouble"
	control="<%= new Double(new Short((short) new Character((char) 31).charValue()).doubleValue()) %>"
	double="${type.chr}" />
<el:checkCoercion name="ByteToBigInteger"
	control='<%= BigInteger.valueOf(new Byte((byte) 30).longValue()) %>'
	bigInteger="${type.bite}" />
<el:checkCoercion name="ShortToBigInteger"
	control='<%= BigInteger.valueOf(new Short((short) 32).longValue()) %>'
	bigInteger="${type.shrt}" />
<el:checkCoercion name="IntToBigInteger"
	control='<%= BigInteger.valueOf(new Integer(33).longValue()) %>'
	bigInteger="${type.inti}" />
<el:checkCoercion name="LongToBigInteger"
	control='<%= BigInteger.valueOf(new Long(34L).longValue()) %>'
	bigInteger="${type.lng}" />
<el:checkCoercion name="FloatToBigInteger"
	control='<%= BigInteger.valueOf(new Float(35.5f).longValue()) %>'
	bigInteger="${type.flote}" />
<el:checkCoercion name="DoubleToBigInteger"
	control='<%= BigInteger.valueOf(new Double(36.5).longValue()) %>'
	bigInteger="${type.dble}" />
<el:checkCoercion name="BigDecimalToBigInteger"
	control='<%= new BigDecimal("100.5").toBigInteger() %>'
	bigInteger="${type.bigDec}" />
<el:checkCoercion name="ByteToBigDecimal"
	control='<%= new BigDecimal(new Byte((byte) 30).doubleValue()) %>'
	bigDecimal="${type.bite}" />
<el:checkCoercion name="ShortToBigDecimal"
	control='<%= new BigDecimal(new Short((short) 32).doubleValue()) %>'
	bigDecimal="${type.shrt}" />
<el:checkCoercion name="IntToBigDecimal"
	control='<%= new BigDecimal(new Integer(33).doubleValue()) %>'
	bigDecimal="${type.inti}" />
<el:checkCoercion name="LongToBigDecimal"
	control='<%= new BigDecimal(new Long(34L).doubleValue()) %>'
	bigDecimal="${type.lng}" />
<el:checkCoercion name="FloatToBigDecimal"
	control='<%= new BigDecimal(new Float(35.5f).doubleValue()) %>'
	bigDecimal="${type.flote}" />
<el:checkCoercion name="DoubleToBigDecimal"
	control='<%= new BigDecimal(new Double(36.5).doubleValue()) %>'
	bigDecimal="${type.dble}" />
<el:checkCoercion name="BigIntegerToBigDecimal"
	control='<%= new BigDecimal(new BigInteger("125")) %>'
	bigDecimal="${type.bigInt}" display="true" />
