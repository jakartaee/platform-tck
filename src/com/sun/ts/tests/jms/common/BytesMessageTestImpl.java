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

import java.io.*;
import javax.jms.*;

/**
 * Class Declaration.
 *
 * @see
 *
 * @author
 */
public class BytesMessageTestImpl extends MessageTestImpl
    implements BytesMessage {
  static final private ObjectStreamField[] serialPersistentFields = {
      new ObjectStreamField("buf", byte[].class) };

  long bodyLength = 0;

  transient ByteArrayInputStream bais;

  transient DataInputStream dis;

  byte[] buf;

  transient ByteArrayOutputStream baos;

  transient DataOutputStream dos;

  /**
   * Class Constructor.
   *
   * @see
   */
  public BytesMessageTestImpl() {
    super();
    init();
  }

  /**
   * Method Declaration.
   *
   *
   * @see
   */
  private void init() {
    buf = new byte[0];
    baos = new ByteArrayOutputStream();
    dos = new DataOutputStream(baos);
  }

  /**
   * Method Declaration.
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
   * @exception JMSException
   *
   * @see
   */
  public void clearBody() throws JMSException {
    buf = null;
    bais = null;
    dis = null;
    readMode = false;
  }

  /**
   * Read a <code>boolean</code> from the BytesMessage.
   *
   * @return the <code>boolean</code> value read.
   *
   * @exception MessageNotReadableException
   *              if message in write-only mode.
   * @exception JMSException
   *              if JMS fails to read message due to some internal JMS error.
   * @exception MessageEOFException
   *              if end of message stream
   */
  public boolean readBoolean() throws JMSException {
    boolean ret = false;
    checkReadAccess();

    try {
      ret = dis.readBoolean();
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
   * Read a signed 8-bit value from the BytesMessage.
   *
   * @return the next byte from the BytesMessage as a signed 8-bit
   *         <code>byte</code>.
   *
   * @exception MessageNotReadableException
   *              if message in write-only mode.
   * @exception MessageEOFException
   *              if end of message stream
   * @exception JMSException
   *              if JMS fails to read message due to some internal JMS error.
   */
  public byte readByte() throws JMSException {
    com.sun.ts.lib.util.TestUtil.logTrace("readByte");
    checkReadAccess();

    byte ret = 0;
    try {
      ret = dis.readByte();
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
   * Read an unsigned 8-bit number from the BytesMessage.
   *
   * @return the next byte from the BytesMessage, interpreted as an unsigned
   *         8-bit number.
   *
   * @exception MessageNotReadableException
   *              if message in write-only mode.
   * @exception MessageEOFException
   *              if end of message stream
   * @exception JMSException
   *              if JMS fails to read message due to some internal JMS error.
   */
  public int readUnsignedByte() throws JMSException {
    com.sun.ts.lib.util.TestUtil.logTrace("readUnsignedByte");
    int ret = 0;
    checkReadAccess();

    try {
      ret = dis.readUnsignedByte();
    } catch (EOFException e1) {
      throw new MessageEOFException("at end of message"); // I18N
    } catch (IOException e2) {
      JMSException jmsEx = new JMSException("IOException"); // I18N

      jmsEx.setLinkedException(e2);
      throw jmsEx;
    } // try .. catch
    return ret;
  } // readUnsignedByte()

  /**
   * Read a signed 16-bit number from the BytesMessage.
   *
   * @return the next two bytes from the BytesMessage, interpreted as a signed
   *         16-bit number.
   *
   * @exception MessageNotReadableException
   *              if message in write-only mode.
   * @exception MessageEOFException
   *              if end of message stream
   * @exception JMSException
   *              if JMS fails to read message due to some internal JMS error.
   */
  public short readShort() throws JMSException {
    com.sun.ts.lib.util.TestUtil.logTrace("readShort");
    checkReadAccess();
    short ret = 0;

    try {
      ret = dis.readShort();
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
   * Read an unsigned 16-bit number from the BytesMessage.
   *
   * @return the next two bytes from the BytesMessage, interpreted as an
   *         unsigned 16-bit integer.
   *
   * @exception MessageNotReadableException
   *              if message in write-only mode.
   * @exception MessageEOFException
   *              if end of message stream
   * @exception JMSException
   *              if JMS fails to read message due to some internal JMS error.
   */
  public int readUnsignedShort() throws JMSException {
    com.sun.ts.lib.util.TestUtil.logTrace("readUnsignedShort");
    checkReadAccess();
    int ret = 0;

    try {
      ret = dis.readUnsignedShort();
    } catch (EOFException e1) {
      throw new MessageEOFException("at end of message"); // I18N
    } catch (IOException e2) {
      JMSException jmsEx = new JMSException("IOException"); // I18N

      jmsEx.setLinkedException(e2);
      throw jmsEx;
    } // try .. catch
    return ret;
  } // readUnsignedShort()

  /**
   * Read a Unicode character value from the BytesMessage.
   *
   * @return the next two bytes from the BytesMessage as a Unicode character.
   *
   * @exception MessageNotReadableException
   *              if message in write-only mode.
   * @exception MessageEOFException
   *              if end of message stream
   * @exception JMSException
   *              if JMS fails to read message due to some internal JMS error.
   */
  public char readChar() throws JMSException {
    com.sun.ts.lib.util.TestUtil.logTrace("readChar");
    checkReadAccess();
    char ret = 0;

    try {
      ret = dis.readChar();
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
   * Read a signed 32-bit integer from the BytesMessage.
   *
   * @return the next four bytes from the BytesMessage, interpreted as an
   *         <code>int</code>.
   *
   * @exception MessageNotReadableException
   *              if message in write-only mode.
   * @exception MessageEOFException
   *              if end of message stream
   * @exception JMSException
   *              if JMS fails to read message due to some internal JMS error.
   */
  public int readInt() throws JMSException {
    com.sun.ts.lib.util.TestUtil.logTrace("readInt");
    checkReadAccess();
    int ret = 0;

    try {
      ret = dis.readInt();
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
   * Read a signed 64-bit integer from the BytesMessage.
   *
   * @return the next eight bytes from the BytesMessage, interpreted as a
   *         <code>long</code>.
   *
   * @exception MessageNotReadableException
   *              if message in write-only mode.
   * @exception MessageEOFException
   *              if end of message stream
   * @exception JMSException
   *              if JMS fails to read message due to some internal JMS error.
   */
  public long readLong() throws JMSException {
    com.sun.ts.lib.util.TestUtil.logTrace("readLong");
    checkReadAccess();
    long ret = 0;

    try {
      ret = dis.readLong();
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
   * Read a <code>float</code> from the BytesMessage.
   *
   * @return the next four bytes from the BytesMessage, interpreted as a
   *         <code>float</code>.
   *
   * @exception MessageNotReadableException
   *              if message in write-only mode.
   * @exception MessageEOFException
   *              if end of message stream
   * @exception JMSException
   *              if JMS fails to read message due to some internal JMS error.
   */
  public float readFloat() throws JMSException {
    com.sun.ts.lib.util.TestUtil.logTrace("readFloat");
    checkReadAccess();
    float ret = 0;

    try {
      ret = dis.readFloat();
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
   * Read a <code>double</code> from the BytesMessage.
   *
   * @return the next eight bytes from the BytesMessage, interpreted as a
   *         <code>double</code>.
   *
   * @exception MessageNotReadableException
   *              if message in write-only mode.
   * @exception MessageEOFException
   *              if end of message stream
   * @exception JMSException
   *              if JMS fails to read message due to some internal JMS error.
   */
  public double readDouble() throws JMSException {
    com.sun.ts.lib.util.TestUtil.logTrace("readDouble");
    checkReadAccess();
    double ret = 0;

    try {
      ret = dis.readDouble();
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
   * Read in a string that has been encoded using a modified UTF-8 format from
   * the BytesMessage.
   *
   * <P>
   * For more information on the UTF-8 format, see "File System Safe UCS
   * Transformation Format (FSS_UFT)", X/Open Preliminary Specification, X/Open
   * Company Ltd., Document Number: P316. This information also appears in
   * ISO/IEC 10646, Annex P.
   *
   * @return a Unicode string from the BytesMessage.
   *
   * @exception MessageNotReadableException
   *              if message in write-only mode.
   * @exception MessageEOFException
   *              if end of message stream
   * @exception JMSException
   *              if JMS fails to read message due to some internal JMS error.
   */
  public String readUTF() throws JMSException {
    com.sun.ts.lib.util.TestUtil.logTrace("readUTF");
    checkReadAccess();
    String ret = null;

    try {
      ret = dis.readUTF();
    } catch (EOFException e1) {
      throw new MessageEOFException("at end of message"); // I18N
    } catch (IOException e2) {
      JMSException jmsEx = new JMSException("IOException"); // I18N

      jmsEx.setLinkedException(e2);
      throw jmsEx;
    } // try .. catch
    return ret;
  } // readUTF()

  /**
   * Read a byte array from the BytesMessage.
   *
   * @param value
   *          the buffer into which the data is read.
   *
   * @return the total number of bytes read into the buffer, or -1 if there is
   *         no more data because the end of the stream has been reached.
   *
   * @exception MessageNotReadableException
   *              if message in write-only mode.
   * @exception MessageEOFException
   *              if end of message stream
   * @exception JMSException
   *              if JMS fails to read message due to some internal JMS error.
   */
  public int readBytes(byte[] value) throws JMSException {
    com.sun.ts.lib.util.TestUtil.logTrace("readBytes");
    checkReadAccess();
    int ret = -1;

    try {
      ret = dis.read(value);
    } catch (IOException e2) {
      JMSException jmsEx = new JMSException("IOException"); // I18N

      jmsEx.setLinkedException(e2);
      throw jmsEx;
    } // try .. catch
    return ret;
  } // readBytes()

  /**
   * Read a portion of the bytes message.
   *
   * @param value
   *          the buffer into which the data is read.
   * @param length
   *          the number of bytes to read.
   *
   * @return the total number of bytes read into the buffer, or -1 if there is
   *         no more data because the end of the stream has been reached.
   *
   * @exception MessageNotReadableException
   *              if message in write-only mode.
   * @exception MessageEOFException
   *              if end of message stream
   * @exception JMSException
   *              if JMS fails to read message due to some internal JMS error.
   */
  public int readBytes(byte[] value, int length) throws JMSException {
    com.sun.ts.lib.util.TestUtil.logTrace("readBytes");
    checkReadAccess();
    int ret = -1;

    if ((length < 0) || (length > value.length)) {
      throw new IndexOutOfBoundsException();
    }
    try {
      ret = dis.read(value, 0, length);
    } catch (IOException e2) {
      JMSException jmsEx = new JMSException("IOException"); // I18N

      jmsEx.setLinkedException(e2);
      throw jmsEx;
    } // try .. catch
    return ret;
  } // readBytes()

  /**
   * Write a <code>boolean</code> to the BytesMessage as a 1-byte value. The
   * value <code>true</code> is written out as the value <code>(byte)1</code>;
   * the value <code>false</code> is written out as the value
   * <code>(byte)0</code>.
   *
   * @param value
   *          the <code>boolean</code> value to be written.
   *
   * @exception MessageNotWriteableException
   *              if message in read-only mode.
   * @exception JMSException
   *              if JMS fails to write message due to some internal JMS error.
   */
  public void writeBoolean(boolean writeBoolean) throws JMSException {

    try {
      dos.writeBoolean(writeBoolean);
    } catch (IOException e) {
      JMSException jmsEx = new JMSException("IOException"); // I18N

      jmsEx.setLinkedException(e);
      throw jmsEx;
    } // try .. catch
  } // writeBoolean()

  /**
   * Write out a <code>byte</code> to the BytesMessage as a 1-byte value.
   *
   * @param value
   *          the <code>byte</code> value to be written.
   *
   * @exception MessageNotWriteableException
   *              if message in read-only mode.
   * @exception JMSException
   *              if JMS fails to write message due to some internal JMS error.
   */
  public void writeByte(byte value) throws JMSException {

    try {
      dos.writeByte((int) value);
      setBufferIsDirty(true);
    } catch (IOException e) {
      JMSException jmsEx = new JMSException("IOException"); // I18N

      jmsEx.setLinkedException(e);
      throw jmsEx;
    }
  } // writeByte()

  /**
   * Write a <code>short</code> to the BytesMessage as two bytes, high byte
   * first.
   *
   * @param value
   *          the <code>short</code> to be written.
   *
   * @exception MessageNotWriteableException
   *              if message in read-only mode.
   * @exception JMSException
   *              if JMS fails to write message due to some internal JMS error.
   */
  public void writeShort(short value) throws JMSException {

    try {
      dos.writeShort((int) value);
      setBufferIsDirty(true);
    } catch (IOException e) {
      JMSException jmsEx = new JMSException("IOException"); // I18N

      jmsEx.setLinkedException(e);
      throw jmsEx;
    }
  } // writeShort()

  /**
   * Write a <code>char</code> to the BytesMessage as a 2-byte value, high byte
   * first.
   *
   * @param value
   *          the <code>char</code> value to be written.
   *
   * @exception MessageNotWriteableException
   *              if message in read-only mode.
   * @exception JMSException
   *              if JMS fails to write message due to some internal JMS error.
   */
  public void writeChar(char value) throws JMSException {

    try {
      dos.writeChar((int) value);
      setBufferIsDirty(true);
    } catch (IOException e) {
      JMSException jmsEx = new JMSException("IOException"); // I18N

      jmsEx.setLinkedException(e);
      throw jmsEx;
    }
  } // writeChar()

  /**
   * Write an <code>int</code> to the BytesMessage as four bytes, high byte
   * first.
   *
   * @param value
   *          the <code>int</code> to be written.
   *
   * @exception MessageNotWriteableException
   *              if message in read-only mode.
   * @exception JMSException
   *              if JMS fails to write message due to some internal JMS error.
   */
  public void writeInt(int value) throws JMSException {
    try {
      dos.writeInt(value);
      setBufferIsDirty(true);
    } catch (IOException e) {
      JMSException jmsEx = new JMSException("IOException"); // I18N

      jmsEx.setLinkedException(e);
      throw jmsEx;
    }
  } // writeInt()

  /**
   * Write a <code>long</code> to the BytesMessage as eight bytes, high byte
   * first.
   *
   * @param value
   *          the <code>long</code> to be written.
   *
   * @exception MessageNotWriteableException
   *              if message in read-only mode.
   * @exception JMSException
   *              if JMS fails to write message due to some internal JMS error.
   */
  public void writeLong(long value) throws JMSException {
    try {
      dos.writeLong(value);
      setBufferIsDirty(true);
    } catch (IOException e) {
      JMSException jmsEx = new JMSException("IOException"); // I18N

      jmsEx.setLinkedException(e);
      throw jmsEx;
    }
  } // writeLong()

  /**
   * Convert the float argument to an <code>int</code> using the
   * <code>floatToIntBits</code> method in class <code>Float</code>, and then
   * writes that <code>int</code> value to the stream message as a 4-byte
   * quantity, high byte first.
   *
   * @param value
   *          the <code>float</code> value to be written.
   *
   * @exception MessageNotWriteableException
   *              if message in read-only mode.
   * @exception JMSException
   *              if JMS fails to write message due to some internal JMS error.
   */
  public void writeFloat(float value) throws JMSException {
    try {
      dos.writeFloat(value);
      setBufferIsDirty(true);
    } catch (IOException e) {
      JMSException jmsEx = new JMSException("IOException"); // I18N

      jmsEx.setLinkedException(e);
      throw jmsEx;
    }
  } // writeFloat()

  /**
   * Convert the double argument to a <code>long</code> using the
   * <code>doubleToLongBits</code> method in class <code>Double</code>, and then
   * writes that <code>long</code> value to the stream message as an 8-byte
   * quantity, high byte first.
   *
   * @param value
   *          the <code>double</code> value to be written.
   *
   * @exception MessageNotWriteableException
   *              if message in read-only mode.
   * @exception JMSException
   *              if JMS fails to write message due to some internal JMS error.
   */
  public void writeDouble(double value) throws JMSException {
    try {
      dos.writeDouble(value);
      setBufferIsDirty(true);
    } catch (IOException e) {
      JMSException jmsEx = new JMSException("IOException"); // I18N

      jmsEx.setLinkedException(e);
      throw jmsEx;
    }
  } // writeDouble()

  /**
   * Write a string to the BytesMessage using UTF-8 encoding in a
   * machine-independent manner.
   *
   * <P>
   * For more information on the UTF-8 format, see "File System Safe UCS
   * Transformation Format (FSS_UFT)", X/Open Preliminary Specification, X/Open
   * Company Ltd., Document Number: P316. This information also appears in
   * ISO/IEC 10646, Annex P.
   *
   * @param value
   *          the <code>String</code> value to be written.
   *
   * @exception MessageNotWriteableException
   *              if message in read-only mode.
   * @exception JMSException
   *              if JMS fails to write message due to some internal JMS error.
   */
  public void writeUTF(String value) throws JMSException {
    try {
      dos.writeUTF(value);
      setBufferIsDirty(true);
    } catch (IOException e) {
      JMSException jmsEx = new JMSException("IOException"); // I18N

      jmsEx.setLinkedException(e);
      throw jmsEx;
    }
  } // writeUTF()

  /**
   * Write a byte array to the BytesMessage.
   *
   * @param value
   *          the byte array to be written.
   *
   * @exception MessageNotWriteableException
   *              if message in read-only mode.
   * @exception JMSException
   *              if JMS fails to write message due to some internal JMS error.
   */
  public void writeBytes(byte[] value) throws JMSException {
    try {
      dos.write(value);
      setBufferIsDirty(true);
    } catch (IOException e) {
      JMSException jmsEx = new JMSException("IOException"); // I18N

      jmsEx.setLinkedException(e);
      throw jmsEx;
    }
  } // writeBytes()

  /**
   * Write a portion of a byte array to the BytesMessage.
   *
   * @param value
   *          the byte array value to be written.
   * @param offset
   *          the initial offset within the byte array.
   * @param length
   *          the number of bytes to use.
   *
   * @exception MessageNotWriteableException
   *              if message in read-only mode.
   * @exception JMSException
   *              if JMS fails to write message due to some internal JMS error.
   */
  public void writeBytes(byte[] value, int offset, int length)
      throws JMSException {
    try {
      dos.write(value, offset, length);
      setBufferIsDirty(true);
    } catch (IOException e) {
      JMSException jmsEx = new JMSException("IOException"); // I18N

      jmsEx.setLinkedException(e);
      throw jmsEx;
    }
  } // writeBytes()

  /**
   * Write a Java object to the BytesMessage.
   *
   * <P>
   * Note that this method only works for the objectified primitive object types
   * (Integer, Double, Long ...), String's and byte arrays.
   *
   * @param value
   *          the Java object to be written.
   *
   * @exception MessageNotWriteableException
   *              if message in read-only mode.
   * @exception MessageFormatException
   *              if object is invalid type.
   * @exception JMSException
   *              if JMS fails to write message due to some internal JMS error.
   */
  public void writeObject(Object value) throws JMSException {
    if (value == null) {
      throw new NullPointerException();
    }
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
      writeUTF((String) value);
    } else if (value instanceof byte[]) {
      writeBytes((byte[]) value);
    } else {
      throw new MessageFormatException("Invalid type"); // I18N
    } // if .. else
  } // writeObject()

  /**
   * Put the message in read-only mode, and reposition the stream of bytes to
   * the beginning.
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
        com.sun.ts.lib.util.TestUtil.logTrace("flush dos");
        dos.flush();
        dos.close();
        baos.close();
      }

    } catch (IOException e) {
      JMSException jmsEx = new JMSException("IOException");
      jmsEx.setLinkedException(e);
      throw jmsEx;
    }

    if (baos != null) {

      // copy the content of DataOutputStream dos to buf
      buf = baos.toByteArray();
      com.sun.ts.lib.util.TestUtil.logTrace("baos.toByteArray");

    } else {
      if (buf == null) {
        buf = new byte[0];
        com.sun.ts.lib.util.TestUtil.logTrace("buf = new byte[0]");
      }
    }
    bais = new ByteArrayInputStream(buf);
    com.sun.ts.lib.util.TestUtil.logTrace("dis = new DataInputStream(bais)");
    dis = new DataInputStream(bais);

    setBufferIsDirty(false);
    readMode = true;
  }

  public long getBodyLength() {
    return bodyLength;
  }

  public void setBodyLength(long l) {
    bodyLength = l;
  }

}
