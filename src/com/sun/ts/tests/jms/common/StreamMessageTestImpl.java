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
public class StreamMessageTestImpl extends MessageTestImpl
    implements StreamMessage {
  static final private ObjectStreamField[] serialPersistentFields = {
      new ObjectStreamField("buf", byte[].class) };

  // every write method will first write the type byte,
  // and then the data with that type.
  public static final byte BOOLEAN_TYPE = 1;

  public static final byte BYTE_TYPE = 2;

  public static final byte CHAR_TYPE = 3;

  public static final byte DOUBLE_TYPE = 4;

  public static final byte FLOAT_TYPE = 5;

  public static final byte INT_TYPE = 6;

  public static final byte LONG_TYPE = 7;

  public static final byte SHORT_TYPE = 8;

  public static final byte STRING_TYPE = 9;

  public static final byte BYTES_TYPE = 10;

  byte[] buf = new byte[0];

  transient ByteArrayInputStream bais;

  transient ByteArrayOutputStream baos;

  transient DataInputStream dis;

  transient DataOutputStream dos;

  // for read/writeBytes
  private boolean first_time_readBytes = true;

  private int available_bytes = 0;

  /**
   * Class Constructor.
   * 
   * 
   * @see
   */
  public StreamMessageTestImpl() {
    super();
    init();
  } // StreamMessageTestImpl()

  /**
   * Method Declaration.
   * 
   * 
   * @see
   */
  private void init() {
    baos = new ByteArrayOutputStream();
    dos = new DataOutputStream(baos);
  }

  /**
   * Method Declaration.
   * 
   * 
   * @param oos
   *
   * @exception IOException
   *
   * @see
   */
  private void writeObject(ObjectOutputStream oos) throws IOException {
    dos.flush();
    buf = baos.toByteArray();
    oos.defaultWriteObject();
  }

  /**
   * Method Declaration.
   * 
   * 
   * @param ois
   *
   * @exception ClassNotFoundException
   * @exception IOException
   *
   * @see
   */
  private void readObject(ObjectInputStream ois)
      throws ClassNotFoundException, IOException {
    ois.defaultReadObject();
    baos = new ByteArrayOutputStream();
    dos = new DataOutputStream(baos);
    if (buf != null) {
      dos.write(buf);
      buf = null;
    }
  }

  /**
   * Method Declaration.
   * 
   * 
   * @return
   * 
   * @exception JMSException
   * 
   * @see
   */
  private byte getType() throws IOException {
    return dis.readByte();
  } // getType()

  /**
   * Method Declaration.
   * 
   * 
   * @return
   *
   * @exception IOException
   *
   * @see
   */
  private int getBytesLength() throws IOException {
    return dis.readInt();
  } // getType()

  /**
   * Read a <code>boolean</code> from the stream message.
   * 
   * @return the <code>boolean</code> value read.
   * 
   * @exception JMSException
   *              if JMS fails to read message due to some internal JMS error.
   * @exception MessageEOFException
   *              if an end of message stream
   * @exception MessageFormatException
   *              if this type conversion is invalid
   * @exception MessageNotReadableException
   *              if message in write-only mode.
   */
  public boolean readBoolean() throws JMSException {
    boolean ret = false;
    checkReadAccess();

    try {
      byte type = getType();

      switch (type) {
      case BOOLEAN_TYPE:
        ret = dis.readBoolean();
        break;
      case STRING_TYPE:
        String s = dis.readUTF();

        ret = Boolean.valueOf(s).booleanValue();
        break;
      default:
        throw new MessageFormatException("type conversion is invalid");
      } // switch
    } catch (EOFException e1) {
      throw new MessageEOFException("at end of message"); // I18N
    } catch (IOException e2) {
      JMSException jmsEx = new JMSException("IOException"); // I18N

      jmsEx.setLinkedException(e2);
      throw jmsEx;
    } // try .. catch
    return ret;
  } // readBoolean()

  /**
   * Read a byte value from the stream message.
   * 
   * @return the next byte from the stream message as a 8-bit <code>byte</code>.
   * 
   * @exception JMSException
   *              if JMS fails to read message due to some internal JMS error.
   * @exception MessageEOFException
   *              if an end of message stream
   * @exception MessageFormatException
   *              if this type conversion is invalid
   * @exception MessageNotReadableException
   *              if message in write-only mode.
   */
  public byte readByte() throws JMSException {
    byte ret = 0;
    checkReadAccess();

    try {
      byte type = getType();

      switch (type) {
      case BYTE_TYPE:
        ret = dis.readByte();
        break;
      case STRING_TYPE:
        String s = dis.readUTF();

        ret = Byte.valueOf(s).byteValue();
        break;
      default:
        throw new MessageFormatException("type conversion is invalid");
      } // switch
    } catch (EOFException e1) {
      throw new MessageEOFException("at end of message"); // I18N
    } catch (IOException e2) {
      JMSException jmsEx = new JMSException("IOException"); // I18N

      jmsEx.setLinkedException(e2);
      throw jmsEx;
    } // try .. catch
    return ret;
  } // readByte()

  /**
   * Read a 16-bit number from the stream message.
   * 
   * @return a 16-bit number from the stream message.
   * 
   * @exception JMSException
   *              if JMS fails to read message due to some internal JMS error.
   * @exception MessageEOFException
   *              if an end of message stream
   * @exception MessageFormatException
   *              if this type conversion is invalid
   * @exception MessageNotReadableException
   *              if message in write-only mode.
   */
  public short readShort() throws JMSException {
    short ret = 0;
    checkReadAccess();

    try {
      byte type = getType();

      switch (type) {
      case BYTE_TYPE:
        ret = dis.readByte();
        break;
      case SHORT_TYPE:
        ret = dis.readShort();
        break;
      case STRING_TYPE:
        String s = dis.readUTF();

        ret = Short.valueOf(s).shortValue();
        break;
      default:
        throw new MessageFormatException("type conversion is invalid");
      } // switch
    } catch (EOFException e1) {
      throw new MessageEOFException("at end of message"); // I18N
    } catch (IOException e2) {
      JMSException jmsEx = new JMSException("IOException"); // I18N

      jmsEx.setLinkedException(e2);
      throw jmsEx;
    } // try .. catch
    return ret;
  } // readShort()

  /**
   * Read a Unicode character value from the stream message.
   * 
   * @return a Unicode character from the stream message.
   * 
   * @exception JMSException
   *              if JMS fails to read message due to some internal JMS error.
   * @exception MessageEOFException
   *              if an end of message stream
   * @exception MessageFormatException
   *              if this type conversion is invalid
   * @exception MessageNotReadableException
   *              if message in write-only mode.
   */
  public char readChar() throws JMSException {
    char ret = 0;
    checkReadAccess();

    try {
      byte type = getType();

      switch (type) {
      case CHAR_TYPE:
        ret = dis.readChar();
        break;
      case STRING_TYPE:
        String s = dis.readUTF();

        ret = s.charAt(0); // ???
        break;
      default:
        throw new MessageFormatException("type conversion is invalid");
      } // switch
    } catch (EOFException e1) {
      throw new MessageEOFException("at end of message"); // I18N
    } catch (IOException e2) {
      JMSException jmsEx = new JMSException("IOException"); // I18N

      jmsEx.setLinkedException(e2);
      throw jmsEx;
    } // try .. catch
    return ret;
  } // readChar()

  /**
   * Read a 32-bit integer from the stream message.
   * 
   * @return a 32-bit integer value from the stream message, interpreted as a
   *         <code>int</code>.
   * 
   * @exception JMSException
   *              if JMS fails to read message due to some internal JMS error.
   * @exception MessageEOFException
   *              if an end of message stream
   * @exception MessageFormatException
   *              if this type conversion is invalid
   * @exception MessageNotReadableException
   *              if message in write-only mode.
   */
  public int readInt() throws JMSException {
    int ret = 0;
    checkReadAccess();

    try {
      byte type = getType();

      switch (type) {
      case BYTE_TYPE:
        ret = dis.readByte();
        break;
      case SHORT_TYPE:
        ret = dis.readShort();
        break;
      case INT_TYPE:
        ret = dis.readInt();
        break;
      case STRING_TYPE:
        String s = dis.readUTF();

        ret = Integer.valueOf(s).intValue();
        break;
      default:
        throw new MessageFormatException("type conversion is invalid");
      } // switch
    } catch (EOFException e1) {
      throw new MessageEOFException("at end of message"); // I18N
    } catch (IOException e2) {
      JMSException jmsEx = new JMSException("IOException"); // I18N

      jmsEx.setLinkedException(e2);
      throw jmsEx;
    } // try .. catch
    return ret;
  } // readInt()

  /**
   * Read a 64-bit integer from the stream message.
   * 
   * @return a 64-bit integer value from the stream message, interpreted as a
   *         <code>long</code>.
   * 
   * @exception JMSException
   *              if JMS fails to read message due to some internal JMS error.
   * @exception MessageEOFException
   *              if an end of message stream
   * @exception MessageFormatException
   *              if this type conversion is invalid
   * @exception MessageNotReadableException
   *              if message in write-only mode.
   */
  public long readLong() throws JMSException {
    long ret = 0;
    checkReadAccess();

    try {
      byte type = getType();

      switch (type) {
      case BYTE_TYPE:
        ret = dis.readByte();
        break;
      case SHORT_TYPE:
        ret = dis.readShort();
        break;
      case INT_TYPE:
        ret = dis.readInt();
        break;
      case LONG_TYPE:
        ret = dis.readLong();
        break;
      case STRING_TYPE:
        String s = dis.readUTF();

        ret = Long.valueOf(s).longValue();
        break;
      default:
        throw new MessageFormatException("type conversion is invalid");
      } // switch
    } catch (EOFException e1) {
      throw new MessageEOFException("at end of message"); // I18N
    } catch (IOException e2) {
      JMSException jmsEx = new JMSException("IOException"); // I18N

      jmsEx.setLinkedException(e2);
      throw jmsEx;
    } // try .. catch
    return ret;
  } // readLong()

  /**
   * Read a <code>float</code> from the stream message.
   * 
   * @return a <code>float</code> value from the stream message.
   * 
   * @exception JMSException
   *              if JMS fails to read message due to some internal JMS error.
   * @exception MessageEOFException
   *              if an end of message stream
   * @exception MessageFormatException
   *              if this type conversion is invalid
   * @exception MessageNotReadableException
   *              if message in write-only mode.
   */
  public float readFloat() throws JMSException {
    float ret = 0;
    checkReadAccess();

    try {
      byte type = getType();

      switch (type) {
      case FLOAT_TYPE:
        ret = dis.readFloat();
        break;
      case STRING_TYPE:
        String s = dis.readUTF();

        ret = Float.valueOf(s).floatValue();
        break;
      default:
        throw new MessageFormatException("type conversion is invalid");
      } // switch
    } catch (EOFException e1) {
      throw new MessageEOFException("at end of message"); // I18N
    } catch (IOException e2) {
      JMSException jmsEx = new JMSException("IOException"); // I18N

      jmsEx.setLinkedException(e2);
      throw jmsEx;
    } // try .. catch
    return ret;
  } // readFloat()

  /**
   * Read a <code>double</code> from the stream message.
   * 
   * @return a <code>double</code> value from the stream message.
   * 
   * @exception JMSException
   *              if JMS fails to read message due to some internal JMS error.
   * @exception MessageEOFException
   *              if an end of message stream
   * @exception MessageFormatException
   *              if this type conversion is invalid
   * @exception MessageNotReadableException
   *              if message in write-only mode.
   */
  public double readDouble() throws JMSException {
    double ret = 0;
    checkReadAccess();

    try {
      byte type = getType();

      switch (type) {
      case FLOAT_TYPE:
        ret = dis.readFloat();
        break;
      case DOUBLE_TYPE:
        ret = dis.readDouble();
        break;
      case STRING_TYPE:
        String s = dis.readUTF();

        ret = Double.valueOf(s).doubleValue();
        break;
      default:
        throw new MessageFormatException("type conversion is invalid");
      } // switch
    } catch (EOFException e1) {
      throw new MessageEOFException("at end of message"); // I18N
    } catch (IOException e2) {
      JMSException jmsEx = new JMSException("IOException"); // I18N

      jmsEx.setLinkedException(e2);
      throw jmsEx;
    } // try .. catch
    return ret;
  } // readDouble()

  /**
   * Read in a string from the stream message.
   * 
   * @return a Unicode string from the stream message.
   * 
   * @exception JMSException
   *              if JMS fails to read message due to some internal JMS error.
   * @exception MessageEOFException
   *              if an end of message stream
   * @exception MessageFormatException
   *              if this type conversion is invalid
   * @exception MessageNotReadableException
   *              if message in write-only mode.
   */
  public String readString() throws JMSException {
    String ret = null;
    checkReadAccess();

    try {
      byte type = getType();

      switch (type) {
      case BOOLEAN_TYPE:
        ret = String.valueOf(dis.readBoolean());
        break;
      case BYTE_TYPE:
        ret = String.valueOf(dis.readByte());
        break;
      case SHORT_TYPE:
        ret = String.valueOf(dis.readShort());
        break;
      case CHAR_TYPE:
        ret = String.valueOf(dis.readChar());
        break;
      case INT_TYPE:
        ret = String.valueOf(dis.readInt());
        break;
      case LONG_TYPE:
        ret = String.valueOf(dis.readLong());
        break;
      case FLOAT_TYPE:
        ret = String.valueOf(dis.readFloat());
        break;
      case DOUBLE_TYPE:
        ret = String.valueOf(dis.readDouble());
        break;
      case STRING_TYPE:
        ret = dis.readUTF();
        break;
      default:
        throw new MessageFormatException("type conversion is invalid");
      } // switch
    } catch (EOFException e1) {
      throw new MessageEOFException("at end of message"); // I18N
    } catch (IOException e2) {
      JMSException jmsEx = new JMSException("IOException"); // I18N

      jmsEx.setLinkedException(e2);
      throw jmsEx;
    } // try .. catch
    return ret;
  } // readString()

  /**
   * Read a byte array from the stream message.
   * 
   * @param value
   *          the buffer into which the data is read.
   * 
   * @return the total number of bytes read into the buffer, or -1 if there is
   *         no more data because the end of the stream has been reached.
   * 
   * @exception JMSException
   *              if JMS fails to read message due to some internal JMS error.
   * @exception MessageEOFException
   *              if an end of message stream
   * @exception MessageFormatException
   *              if this type conversion is invalid
   * @exception MessageNotReadableException
   *              if message in write-only mode.
   */
  public int readBytes(byte[] value) throws JMSException {
    int ret = -1;
    checkReadAccess();

    try {
      byte type = BYTES_TYPE;

      // get the type and the length of this bytes array field
      // if this is the first time read this bytes array field
      if (first_time_readBytes) {
        type = getType();
        available_bytes = getBytesLength();
      }
      switch (type) {
      case BYTES_TYPE:

        // bytes array field is empty
        if (first_time_readBytes && available_bytes == 0) {
          return 0;
        } else if (!first_time_readBytes && available_bytes == 0) {

          /*
           * this is the case that last time readBytes() read exactly same bytes
           * left in the bytes array field, spec requires an extra readBytes(),
           * and return -1 ;-(
           */
          return -1;
        }
        if (value.length > available_bytes) {

          // read all! (available_bytes won't be zero.)
          ret = dis.read(value, 0, available_bytes);
          available_bytes = 0;

          // initiate first_time_readBytes to true for next field
          first_time_readBytes = true;
        } else if (value.length <= available_bytes) {

          // read all, but needs readBytes again
          ret = dis.read(value, 0, value.length);
          available_bytes = available_bytes - value.length;
          first_time_readBytes = false;
        }
        break;
      default:
        throw new MessageFormatException("type conversion is invalid");
      } // switch
    } catch (EOFException e1) {
      throw new MessageEOFException("at end of message"); // I18N
    } catch (IOException e2) {
      JMSException jmsEx = new JMSException("IOException"); // I18N

      jmsEx.setLinkedException(e2);
      throw jmsEx;
    } // try .. catch
    return ret;
  } // readBytes()

  /**
   * Read a Java object from the stream message.
   * 
   * <P>
   * Note that this method can be used to return in objectified format, an
   * object that had been written to the Stream with the equivalent
   * <CODE>writeObject</CODE> method call, or it's equivalent primitive
   * write<type> method.
   * 
   * @return a Java object from the stream message, in objectified format (ie.
   *         if it set as an int, then a Integer is returned).
   * 
   * @exception JMSException
   *              if JMS fails to read message due to some internal JMS error.
   * @exception MessageEOFException
   *              if an end of message stream
   * @exception MessageNotReadableException
   *              if message in write-only mode.
   */
  public Object readObject() throws JMSException {
    Object ret = null;
    checkReadAccess();

    try {
      byte type = getType();

      switch (type) {
      case BOOLEAN_TYPE:
        ret = Boolean.valueOf(dis.readBoolean());
        break;
      case BYTE_TYPE:
        ret = Byte.valueOf(dis.readByte());
        break;
      case SHORT_TYPE:
        ret = Short.valueOf(dis.readShort());
        break;
      case CHAR_TYPE:
        ret = Character.valueOf(dis.readChar());
        break;
      case INT_TYPE:
        ret = Integer.valueOf(dis.readInt());
        break;
      case LONG_TYPE:
        ret = Long.valueOf(dis.readLong());
        break;
      case FLOAT_TYPE:
        ret = Float.valueOf(dis.readFloat());
        break;
      case DOUBLE_TYPE:
        ret = Double.valueOf(dis.readDouble());
        break;
      case STRING_TYPE:
        ret = dis.readUTF();
        break;
      default:
        throw new MessageFormatException("type conversion is invalid");
      } // switch
    } catch (EOFException e1) {
      throw new MessageEOFException("at end of message"); // I18N
    } catch (IOException e2) {
      JMSException jmsEx = new JMSException("IOException"); // I18N

      jmsEx.setLinkedException(e2);
      throw jmsEx;
    } // try .. catch
    return ret;
  } // readObject()

  /**
   * Write a <code>boolean</code> to the stream message. The value
   * <code>true</code> is written out as the value <code>(byte)1</code>; the
   * value <code>false</code> is written out as the value <code>(byte)0</code>.
   * 
   * @param value
   *          the <code>boolean</code> value to be written.
   * 
   * @exception JMSException
   *              if JMS fails to read message due to some internal JMS error.
   * @exception MessageNotWriteableException
   *              if message in read-only mode.
   */
  public void writeBoolean(boolean value) throws JMSException {
    try {
      dos.writeByte((int) BOOLEAN_TYPE);
      dos.writeBoolean(value);
      setBufferIsDirty(true);
    } catch (IOException e) {
      JMSException jmsEx = new JMSException("IOException"); // I18N

      jmsEx.setLinkedException(e);
      throw jmsEx;
    } // try .. catch
  } // writeBoolean()

  /**
   * Write out a <code>byte</code> to the stream message.
   * 
   * @param value
   *          the <code>byte</code> value to be written.
   * @exception JMSException
   *              if JMS fails to write message due to some internal JMS error.
   * @exception MessageNotWriteableException
   *              if message in read-only mode.
   */
  public void writeByte(byte value) throws JMSException {
    try {
      dos.writeByte((int) BYTE_TYPE);
      dos.writeByte((int) value);
      setBufferIsDirty(true);
    } catch (IOException e) {
      JMSException jmsEx = new JMSException("IOException"); // I18N

      jmsEx.setLinkedException(e);
      throw jmsEx;
    } // try .. catch
  } // writeByte()

  /**
   * Write a <code>short</code> to the stream message.
   * 
   * @param value
   *          the <code>short</code> to be written.
   * 
   * @exception JMSException
   *              if JMS fails to write message due to some internal JMS error.
   * @exception MessageNotWriteableException
   *              if message in read-only mode.
   */
  public void writeShort(short value) throws JMSException {
    try {
      dos.writeByte((int) SHORT_TYPE);
      dos.writeShort((int) value);
      setBufferIsDirty(true);
    } catch (IOException e) {
      JMSException jmsEx = new JMSException("IOException"); // I18N

      jmsEx.setLinkedException(e);
      throw jmsEx;
    } // try .. catch
  } // writeShort()

  /**
   * Write a <code>char</code> to the stream message.
   * 
   * @param value
   *          the <code>char</code> value to be written.
   * 
   * @exception JMSException
   *              if JMS fails to write message due to some internal JMS error.
   * @exception MessageNotWriteableException
   *              if message in read-only mode.
   */
  public void writeChar(char value) throws JMSException {
    try {
      dos.writeByte((int) CHAR_TYPE);
      dos.writeChar((int) value);
      setBufferIsDirty(true);
    } catch (IOException e) {
      JMSException jmsEx = new JMSException("IOException"); // I18N

      jmsEx.setLinkedException(e);
      throw jmsEx;
    } // try .. catch
  } // writeChar()

  /**
   * Write an <code>int</code> to the stream message.
   * 
   * @param value
   *          the <code>int</code> to be written.
   * 
   * @exception JMSException
   *              if JMS fails to write message due to some internal JMS error.
   * @exception MessageNotWriteableException
   *              if message in read-only mode.
   */
  public void writeInt(int value) throws JMSException {
    try {
      dos.writeByte((int) INT_TYPE);
      dos.writeInt(value);
      setBufferIsDirty(true);
    } catch (IOException e) {
      JMSException jmsEx = new JMSException("IOException"); // I18N

      jmsEx.setLinkedException(e);
      throw jmsEx;
    } // try .. catch
  } // writeInt()

  /**
   * Write a <code>long</code> to the stream message.
   * 
   * @param value
   *          the <code>long</code> to be written.
   * 
   * @exception JMSException
   *              if JMS fails to write message due to some internal JMS error.
   * @exception MessageNotWriteableException
   *              if message in read-only mode.
   */
  public void writeLong(long value) throws JMSException {
    try {
      dos.writeByte((int) LONG_TYPE);
      dos.writeLong(value);
      setBufferIsDirty(true);
    } catch (IOException e) {
      JMSException jmsEx = new JMSException("IOException"); // I18N

      jmsEx.setLinkedException(e);
      throw jmsEx;
    } // try .. catch
  } // writeLong()

  /**
   * Write a <code>float</code> to the stream message.
   * 
   * @param value
   *          the <code>float</code> value to be written.
   * 
   * @exception JMSException
   *              if JMS fails to write message due to some internal JMS error.
   * @exception MessageNotWriteableException
   *              if message in read-only mode.
   */
  public void writeFloat(float value) throws JMSException {
    try {
      dos.writeByte((int) FLOAT_TYPE);
      dos.writeFloat(value);
      setBufferIsDirty(true);
    } catch (IOException e) {
      JMSException jmsEx = new JMSException("IOException"); // I18N

      jmsEx.setLinkedException(e);
      throw jmsEx;
    } // try .. catch
  } // writeFloat()

  /**
   * Write a <code>double</code> to the stream message.
   * 
   * @param value
   *          the <code>double</code> value to be written.
   * 
   * @exception JMSException
   *              if JMS fails to write message due to some internal JMS error.
   * @exception MessageNotWriteableException
   *              if message in read-only mode.
   */
  public void writeDouble(double value) throws JMSException {
    try {
      dos.writeByte((int) DOUBLE_TYPE);
      dos.writeDouble(value);
      setBufferIsDirty(true);
    } catch (IOException e) {
      JMSException jmsEx = new JMSException("IOException"); // I18N

      jmsEx.setLinkedException(e);
      throw jmsEx;
    } // try .. catch
  } // writeDouble()

  /**
   * Write a string to the stream message.
   * 
   * @param value
   *          the <code>String</code> value to be written.
   * 
   * @exception JMSException
   *              if JMS fails to write message due to some internal JMS error.
   * @exception MessageNotWriteableException
   *              if message in read-only mode.
   */
  public void writeString(String value) throws JMSException {
    try {
      dos.writeByte((int) STRING_TYPE);
      dos.writeUTF(value);
      setBufferIsDirty(true);
    } catch (IOException e) {
      JMSException jmsEx = new JMSException("IOException"); // I18N

      jmsEx.setLinkedException(e);
      throw jmsEx;
    } // try .. catch
  } // writeString()

  /**
   * Write a byte array to the stream message.
   * 
   * @param value
   *          the byte array to be written.
   * 
   * @exception JMSException
   *              if JMS fails to write message due to some internal JMS error.
   * @exception MessageNotWriteableException
   *              if message in read-only mode.
   */
  public void writeBytes(byte[] value) throws JMSException {
    writeBytes(value, 0, value.length);
  } // writeBytes()

  /**
   * Write a portion of a byte array to the stream message.
   * 
   * @param value
   *          the byte array value to be written.
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
  public void writeBytes(byte[] value, int offset, int length)
      throws JMSException {

    /*
     * bytes array field format as following: TYPE LENGTH DATA
     */
    try {
      dos.writeByte((int) BYTES_TYPE);
      dos.writeInt(length);
      dos.write(value, offset, length);
      setBufferIsDirty(true);
    } catch (IOException e) {
      JMSException jmsEx = new JMSException("IOException"); // I18N

      jmsEx.setLinkedException(e);
      throw jmsEx;
    } // try .. catch
  } // writeBytes()

  /**
   * Write a Java object to the stream message.
   * 
   * <P>
   * Note that this method only works for the objectified primitive object types
   * (Integer, Double, Long ...), String's and byte arrays.
   * 
   * @param value
   *          the Java object to be written.
   * 
   * @exception JMSException
   *              if JMS fails to write message due to some internal JMS error.
   * @exception MessageNotWriteableException
   *              if message in read-only mode.
   * @exception MessageFormatException
   *              if the object is invalid
   */
  public void writeObject(Object value) throws JMSException {
    if (value instanceof Boolean) {
      writeBoolean(((Boolean) value).booleanValue());
    } else if (value instanceof Byte) {
      writeByte(((Byte) value).byteValue());
    } else if (value instanceof Character) {
      writeChar(((Character) value).charValue());
    } else if (value instanceof Double) {
      writeDouble(((Double) value).doubleValue());
    } else if (value instanceof Float) {
      writeFloat(((Float) value).floatValue());
    } else if (value instanceof Integer) {
      writeInt(((Integer) value).intValue());
    } else if (value instanceof Long) {
      writeLong(((Long) value).longValue());
    } else if (value instanceof Short) {
      writeShort(((Short) value).shortValue());
    } else if (value instanceof String) {
      writeString((String) value);
    } else if (value instanceof byte[]) {
      writeBytes((byte[]) value);
    } else {
      throw new MessageFormatException("Invalid type"); // I18N
    } // if .. else
  } // writeObject()

  /**
   * Put the message in read-only mode, and reposition the stream to the
   * beginning.
   * 
   * @exception JMSException
   *              if JMS fails to reset the message due to some internal JMS
   *              error.
   * @exception MessageFormatException
   *              if message has an invalid format
   */
  public void reset() throws JMSException {

    // forces any buffered output bytes to be written out to the stream
    // not really needed in this case, because the underlying output stream
    // is a ByteArrayOutputStream
    try {
      if (bufferIsDirty) {
        dos.flush();
        dos.close();
        baos.close();
      }
    } catch (IOException e) {
      JMSException jmsEx = new JMSException("IOException"); // I18N

      jmsEx.setLinkedException(e);
      throw jmsEx;
    } // try .. catch
    if (baos != null) {

      // copy the content of DataOutputStream dos to buf
      buf = baos.toByteArray();

    } else {
      if (buf == null) {
        buf = new byte[0];
      }
    }
    bais = new ByteArrayInputStream(buf);
    dis = new DataInputStream(bais);

    // initiate first_time_readBytes to true for readBytes()
    first_time_readBytes = true;
    setBufferIsDirty(false);
    readMode = true;
  } // reset()

  // overwrite methods in MessageImpl

  /**
   * Method Declaration.
   * 
   * 
   * @exception JMSException
   * 
   * @see
   */
  public void clearBody() throws JMSException {
    buf = null;
    bais = null;
    dis = null;
    readMode = false;
  } // clearBody()

}
