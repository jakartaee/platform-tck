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
 * @(#)MapMessageTestImpl.java	1.5 03/05/16
 */

package com.sun.ts.tests.jms.common;

import java.util.*;
import java.io.*;
import javax.jms.*;

/**
 * Class Declaration.
 * 
 * 
 * @see
 *
 * @author
 * @version 1.2, 09/26/00
 */
public class MapMessageTestImpl extends MessageTestImpl implements MapMessage {
  private HashMap htable;

  /**
   * Class Constructor.
   * 
   * 
   * @see
   */
  public MapMessageTestImpl() {
    super();
    init();
  } // MapMessageTestImpl()

  /**
   * Initializes the object during construction. Put things that are common to
   * all constructors here
   */
  private void init() {
    htable = new HashMap();
  }

  /**
   * Return the boolean value with the given name.
   * 
   * @param name
   *          the name of the boolean
   * 
   * @return the boolean value with the given name.
   * 
   * @exception JMSException
   *              if JMS fails to read message due to some internal JMS error.
   * @exception MessageFormatException
   *              if this type conversion is invalid.
   */
  public boolean getBoolean(String name) throws JMSException {
    boolean ret = false;
    Object value = htable.get(name);

    if (value instanceof Boolean) {
      ret = ((Boolean) value).booleanValue();
    } else if (value instanceof String) {
      ret = Boolean.valueOf((String) value).booleanValue();
    } else {
      throw new MessageFormatException("type conversion is invalid");
    } // if .. else
    return ret;
  } // getBoolean()

  /**
   * Return the byte value with the given name.
   * 
   * @param name
   *          the name of the byte
   * 
   * @return the byte value with the given name.
   * 
   * @exception JMSException
   *              if JMS fails to read message due to some internal JMS error.
   * @exception MessageFormatException
   *              if this type conversion is invalid.
   */
  public byte getByte(String name) throws JMSException {
    byte ret = 0;
    Object value = htable.get(name);

    if (value instanceof Byte) {
      ret = ((Byte) value).byteValue();
    } else if (value instanceof String) {
      ret = Byte.valueOf((String) value).byteValue();
    } else {
      throw new MessageFormatException("type conversion is invalid");
    } // if .. else
    return ret;
  } // getByte()

  /**
   * Return the short value with the given name.
   * 
   * @param name
   *          the name of the short
   * 
   * @return the short value with the given name.
   * 
   * @exception JMSException
   *              if JMS fails to read message due to some internal JMS error.
   * @exception MessageFormatException
   *              if this type conversion is invalid.
   */
  public short getShort(String name) throws JMSException {
    short ret = 0;
    Object value = htable.get(name);

    if (value instanceof Byte) {
      ret = ((Byte) value).byteValue();
    } else if (value instanceof Short) {
      ret = ((Short) value).shortValue();
    } else if (value instanceof String) {
      ret = Short.valueOf((String) value).shortValue();
    } else {
      throw new MessageFormatException("type conversion is invalid");
    } // if .. else
    return ret;
  } // getShort()

  /**
   * Return the Unicode character value with the given name.
   * 
   * @param name
   *          the name of the Unicode character
   * 
   * @return the Unicode character value with the given name.
   * 
   * @exception JMSException
   *              if JMS fails to read message due to some internal JMS error.
   * @exception MessageFormatException
   *              if this type conversion is invalid.
   */
  public char getChar(String name) throws JMSException {
    char ret = 0;
    Object value = htable.get(name);

    if (value instanceof Character) {
      ret = ((Character) value).charValue();
    } else if (value instanceof String) {
      ret = ((String) value).charAt(0);
    } else {
      throw new MessageFormatException("type conversion is invalid");
    } // if .. else
    return ret;
  } // getChar()

  /**
   * Return the integer value with the given name.
   * 
   * @param name
   *          the name of the integer
   * 
   * @return the integer value with the given name.
   * 
   * @exception JMSException
   *              if JMS fails to read message due to some internal JMS error.
   * @exception MessageFormatException
   *              if this type conversion is invalid.
   */
  public int getInt(String name) throws JMSException {
    int ret = 0;
    Object value = htable.get(name);

    if (value instanceof Byte) {
      ret = ((Byte) value).byteValue();
    } else if (value instanceof Short) {
      ret = ((Short) value).shortValue();
    } else if (value instanceof Integer) {
      ret = ((Integer) value).intValue();
    } else if (value instanceof String) {
      ret = Integer.valueOf((String) value).intValue();
    } else {
      throw new MessageFormatException("type conversion is invalid");
    } // if .. else
    return ret;
  } // getInt()

  /**
   * Return the long value with the given name.
   * 
   * @param name
   *          the name of the long
   * 
   * @return the long value with the given name.
   * 
   * @exception JMSException
   *              if JMS fails to read message due to some internal JMS error.
   * @exception MessageFormatException
   *              if this type conversion is invalid.
   */
  public long getLong(String name) throws JMSException {
    long ret = 0;
    Object value = htable.get(name);

    if (value instanceof Byte) {
      ret = ((Byte) value).byteValue();
    } else if (value instanceof Short) {
      ret = ((Short) value).shortValue();
    } else if (value instanceof Integer) {
      ret = ((Integer) value).intValue();
    } else if (value instanceof Long) {
      ret = ((Long) value).longValue();
    } else if (value instanceof String) {
      ret = Long.valueOf((String) value).longValue();
    } else {
      throw new MessageFormatException("type conversion is invalid");
    } // if .. else
    return ret;
  } // getLong()

  /**
   * Return the float value with the given name.
   * 
   * @param name
   *          the name of the float
   * 
   * @return the float value with the given name.
   * 
   * @exception JMSException
   *              if JMS fails to read message due to some internal JMS error.
   * @exception MessageFormatException
   *              if this type conversion is invalid.
   */
  public float getFloat(String name) throws JMSException {
    float ret = 0;
    Object value = htable.get(name);

    if (value instanceof Float) {
      ret = ((Float) value).floatValue();
    } else if (value instanceof String) {
      ret = Float.valueOf((String) value).floatValue();
    } else {
      throw new MessageFormatException("type conversion is invalid");
    } // if .. else
    return ret;
  } // getFloat()

  /**
   * Return the double value with the given name.
   * 
   * @param name
   *          the name of the double
   * 
   * @return the double value with the given name.
   * 
   * @exception JMSException
   *              if JMS fails to read message due to some internal JMS error.
   * @exception MessageFormatException
   *              if this type conversion is invalid.
   */
  public double getDouble(String name) throws JMSException {
    double ret = 0;
    Object value = htable.get(name);

    if (value instanceof Float) {
      ret = ((Float) value).floatValue();
    } else if (value instanceof Double) {
      ret = ((Double) value).doubleValue();
    } else if (value instanceof String) {
      ret = Double.valueOf((String) value).doubleValue();
    } else {
      throw new MessageFormatException("type conversion is invalid");
    } // if .. else
    return ret;
  } // getDouble()

  /**
   * Return the String value with the given name.
   * 
   * @param name
   *          the name of the String
   * 
   * @return the String value with the given name. If there is no item by this
   *         name, a null value is returned.
   * 
   * @exception JMSException
   *              if JMS fails to read message due to some internal JMS error.
   * @exception MessageFormatException
   *              if this type conversion is invalid.
   */
  public String getString(String name) throws JMSException {
    String ret = null;
    Object value = htable.get(name);

    if ((value instanceof Boolean) || (value instanceof Byte)
        || (value instanceof Short) || (value instanceof Character)
        || (value instanceof Integer) || (value instanceof Long)
        || (value instanceof Float) || (value instanceof Double)
        || (value instanceof String)) {
      ret = String.valueOf(value);
    } else {
      throw new MessageFormatException("invalid type");
    } // if .. else
    return ret;
  } // getString()

  /**
   * Return the byte array value with the given name.
   * 
   * @param name
   *          the name of the byte array
   * 
   * @return the byte array value with the given name. If there is no item by
   *         this name, a null value is returned.
   * 
   * @exception JMSException
   *              if JMS fails to read message due to some internal JMS error.
   * @exception MessageFormatException
   *              if this type conversion is invalid.
   */
  public byte[] getBytes(String name) throws JMSException {
    byte[] ret = null;
    Object value = htable.get(name);

    if (value instanceof byte[]) {
      ret = (byte[]) value;
    } else {
      throw new MessageFormatException("invalid type");
    } // if .. else
    return ret;
  } // getBytes()

  /**
   * Return the Java object value with the given name.
   * 
   * <P>
   * Note that this method can be used to return in objectified format, an
   * object that had been stored in the Map with the equivalent
   * <CODE>setObject</CODE> method call, or it's equivalent primitive set<type>
   * method.
   * 
   * @param name
   *          the name of the Java object
   * 
   * @return the Java object value with the given name, in objectified format
   *         (ie. if it set as an int, then a Integer is returned). If there is
   *         no item by this name, a null value is returned.
   * 
   * @exception JMSException
   *              if JMS fails to read message due to some internal JMS error.
   */
  public Object getObject(String name) throws JMSException {
    return htable.get(name);
  } // getObject()

  /**
   * Return an Enumeration of all the Map message's names.
   * 
   * @return an enumeration of all the names in this Map message.
   * 
   * @exception JMSException
   *              if JMS fails to read message due to some internal JMS error.
   */
  public Enumeration getMapNames() throws JMSException {
    Vector v = new Vector(htable.keySet());

    return v.elements();
  } // getMapNames()

  /**
   * Set a boolean value with the given name, into the Map.
   * 
   * @param name
   *          the name of the boolean
   * @param value
   *          the boolean value to set in the Map.
   * 
   * @exception JMSException
   *              if JMS fails to write message due to some internal JMS error.
   * @exception MessageNotWriteableException
   *              if message in read-only mode.
   */
  public void setBoolean(String name, boolean value) throws JMSException {
    try {
      htable.put(name, Boolean.valueOf(value));
    } catch (NullPointerException e) {
      JMSException jmsEx = new JMSException("NullPointerException"); // I18N

      jmsEx.setLinkedException(e);
      throw jmsEx;
    } // try .. catch
  } // setBoolean()

  /**
   * Set a byte value with the given name, into the Map.
   * 
   * @param name
   *          the name of the byte
   * @param value
   *          the byte value to set in the Map.
   * 
   * @exception JMSException
   *              if JMS fails to write message due to some internal JMS error.
   * @exception MessageNotWriteableException
   *              if message in read-only mode.
   */
  public void setByte(String name, byte value) throws JMSException {
    try {
      htable.put(name, Byte.valueOf(value));
    } catch (NullPointerException e) {
      JMSException jmsEx = new JMSException("NullPointerException"); // I18N

      jmsEx.setLinkedException(e);
      throw jmsEx;
    } // try .. catch
  } // setByte()

  /**
   * Set a short value with the given name, into the Map.
   * 
   * @param name
   *          the name of the short
   * @param value
   *          the short value to set in the Map.
   * 
   * @exception JMSException
   *              if JMS fails to write message due to some internal JMS error.
   * @exception MessageNotWriteableException
   *              if message in read-only mode.
   */
  public void setShort(String name, short value) throws JMSException {
    try {
      htable.put(name, Short.valueOf(value));
    } catch (NullPointerException e) {
      JMSException jmsEx = new JMSException("NullPointerException"); // I18N

      jmsEx.setLinkedException(e);
      throw jmsEx;
    } // try .. catch
  } // setShort()

  /**
   * Set a Unicode character value with the given name, into the Map.
   * 
   * @param name
   *          the name of the Unicode character
   * @param value
   *          the Unicode character value to set in the Map.
   * 
   * @exception JMSException
   *              if JMS fails to write message due to some internal JMS error.
   * @exception MessageNotWriteableException
   *              if message in read-only mode.
   */
  public void setChar(String name, char value) throws JMSException {
    try {
      htable.put(name, Character.valueOf(value));
    } catch (NullPointerException e) {
      JMSException jmsEx = new JMSException("NullPointerException"); // I18N

      jmsEx.setLinkedException(e);
      throw jmsEx;
    } // try .. catch
  } // setChar()

  /**
   * Set an integer value with the given name, into the Map.
   * 
   * @param name
   *          the name of the integer
   * @param value
   *          the integer value to set in the Map.
   * 
   * @exception JMSException
   *              if JMS fails to write message due to some internal JMS error.
   * @exception MessageNotWriteableException
   *              if message in read-only mode.
   */
  public void setInt(String name, int value) throws JMSException {
    try {
      htable.put(name, Integer.valueOf(value));
    } catch (NullPointerException e) {
      JMSException jmsEx = new JMSException("NullPointerException"); // I18N

      jmsEx.setLinkedException(e);
      throw jmsEx;
    } // try .. catch
  } // setInt()

  /**
   * Set a long value with the given name, into the Map.
   * 
   * @param name
   *          the name of the long
   * @param value
   *          the long value to set in the Map.
   * 
   * @exception JMSException
   *              if JMS fails to write message due to some internal JMS error.
   * @exception MessageNotWriteableException
   *              if message in read-only mode.
   */
  public void setLong(String name, long value) throws JMSException {
    try {
      htable.put(name, Long.valueOf(value));
    } catch (NullPointerException e) {
      JMSException jmsEx = new JMSException("NullPointerException"); // I18N

      jmsEx.setLinkedException(e);
      throw jmsEx;
    } // try .. catch
  } // setLong()

  /**
   * Set a float value with the given name, into the Map.
   * 
   * @param name
   *          the name of the float
   * @param value
   *          the float value to set in the Map.
   * 
   * @exception JMSException
   *              if JMS fails to write message due to some internal JMS error.
   * @exception MessageNotWriteableException
   *              if message in read-only mode.
   */
  public void setFloat(String name, float value) throws JMSException {
    try {
      htable.put(name, Float.valueOf(value));
    } catch (NullPointerException e) {
      JMSException jmsEx = new JMSException("NullPointerException"); // I18N

      jmsEx.setLinkedException(e);
      throw jmsEx;
    } // try .. catch
  } // setFloat()

  /**
   * Set a double value with the given name, into the Map.
   * 
   * @param name
   *          the name of the double
   * @param value
   *          the double value to set in the Map.
   * 
   * @exception JMSException
   *              if JMS fails to write message due to some internal JMS error.
   * @exception MessageNotWriteableException
   *              if message in read-only mode.
   */
  public void setDouble(String name, double value) throws JMSException {
    try {
      htable.put(name, Double.valueOf(value));
    } catch (NullPointerException e) {
      JMSException jmsEx = new JMSException("NullPointerException"); // I18N

      jmsEx.setLinkedException(e);
      throw jmsEx;
    } // try .. catch
  } // setDouble()

  /**
   * Set a String value with the given name, into the Map.
   * 
   * @param name
   *          the name of the String
   * @param value
   *          the String value to set in the Map.
   * 
   * @exception JMSException
   *              if JMS fails to write message due to some internal JMS error.
   * @exception MessageNotWriteableException
   *              if message in read-only mode.
   */
  public void setString(String name, String value) throws JMSException {
    try {
      htable.put(name, value);
    } catch (NullPointerException e) {
      JMSException jmsEx = new JMSException("NullPointerException"); // I18N

      jmsEx.setLinkedException(e);
      throw jmsEx;
    } // try .. catch
  } // setString()

  /**
   * Set a byte array value with the given name, into the Map.
   * 
   * @param name
   *          the name of the byte array
   * @param value
   *          the byte array value to set in the Map.
   * 
   * @exception JMSException
   *              if JMS fails to write message due to some internal JMS error.
   * @exception MessageNotWriteableException
   *              if message in read-only mode.
   */
  public void setBytes(String name, byte[] value) throws JMSException {
    try {
      htable.put(name, value);
    } catch (NullPointerException e) {
      JMSException jmsEx = new JMSException("NullPointerException"); // I18N

      jmsEx.setLinkedException(e);
      throw jmsEx;
    } // try .. catch
  } // setBytes()

  /**
   * Set a portion of the byte array value with the given name, into the Map.
   * 
   * @param name
   *          the name of the byte array
   * @param value
   *          the byte array value to set in the Map.
   * @param offset
   *          the initial offset within the byte array.
   * @param length
   *          the number of bytes to use.
   * 
   * @exception JMSException
   *              if JMS fails to write message due to some internal JMS error.
   * @exception MessageNotWriteableException
   *              if message in read-only mode.
   */
  public void setBytes(String name, byte[] value, int offset, int length)
      throws JMSException {
    try {
      byte[] newValue = (byte[]) htable.get(name);

      System.arraycopy(value, 0, newValue, offset, length);
      htable.put(name, newValue);
    } catch (NullPointerException e) {
      JMSException jmsEx = new JMSException("NullPointerException"); // I18N

      jmsEx.setLinkedException(e);
      throw jmsEx;
    } // try .. catch
  } // setBytes()

  /**
   * Set a Java object value with the given name, into the Map.
   * 
   * <P>
   * Note that this method only works for the objectified primitive object types
   * (Integer, Double, Long ...), String's and byte arrays.
   * 
   * @param name
   *          the name of the Java object
   * @param value
   *          the Java object value to set in the Map.
   * 
   * @exception JMSException
   *              if JMS fails to write message due to some internal JMS error.
   * @exception MessageFormatException
   *              if object is invalid
   * @exception MessageNotWriteableException
   *              if message in read-only mode.
   */
  public void setObject(String name, Object value) throws JMSException {
    try {
      if ((value instanceof Boolean) || (value instanceof Byte)
          || (value instanceof Short) || (value instanceof Character)
          || (value instanceof Integer) || (value instanceof Long)
          || (value instanceof Float) || (value instanceof Double)
          || (value instanceof String) || (value instanceof byte[])) {
        htable.put(name, value);
      } else {
        throw new MessageFormatException("invalid type");
      } // if .. else
    } catch (NullPointerException e) {
      JMSException jmsEx = new JMSException("NullPointerException"); // I18N

      jmsEx.setLinkedException(e);
      throw jmsEx;
    } // try .. catch
  } // setObject()

  /**
   * Check if an item exists in this MapMessage.
   * 
   * @param name
   *          the name of the item to test
   * 
   * @return true if the item does exist.
   * 
   * @exception JMSException
   *              if a JMS error occurs.
   */
  public boolean itemExists(String name) throws JMSException {
    return htable.containsKey(name);
  } // itemExists()

}
