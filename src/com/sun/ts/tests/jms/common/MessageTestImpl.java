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
public class MessageTestImpl implements Message {
  public boolean dummy = false;

  private String JMSMessageID;

  private long JMSTimestamp;

  private byte[] JMSCorrelationIDAsBytes;

  private String JMSCorrelationID;

  private Destination JMSReplyTo;

  private Destination JMSDestination;

  private int JMSDeliveryMode;

  private boolean JMSRedelivered;

  private String JMSType;

  private long JMSExpiration;

  private long JMSDeliveryTime;

  private long JMSDeliveryDelay;

  private int JMSPriority;

  private Hashtable properties;

  protected boolean bufferIsDirty = false;

  protected boolean readMode = false;

  /**
   * Constructor
   */
  public MessageTestImpl() {
    properties = new Hashtable();
    this.JMSPriority = javax.jms.Message.DEFAULT_PRIORITY;
    this.JMSDeliveryMode = javax.jms.Message.DEFAULT_DELIVERY_MODE;
    this.JMSDeliveryDelay = 0L;
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
  public String getJMSMessageID() throws JMSException {
    return JMSMessageID;
  }

  /**
   * Method Declaration.
   * 
   * 
   * @param id
   *
   * @exception JMSException
   *
   * @see
   */
  public void setJMSMessageID(String id) throws JMSException {
    JMSMessageID = id;
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
  public long getJMSTimestamp() throws JMSException {
    return JMSTimestamp;
  }

  /**
   * Method Declaration.
   * 
   * 
   * @param timestamp
   *
   * @exception JMSException
   *
   * @see
   */
  public void setJMSTimestamp(long timestamp) throws JMSException {
    JMSTimestamp = timestamp;
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
  public byte[] getJMSCorrelationIDAsBytes() throws JMSException {
    return JMSCorrelationIDAsBytes;
  }

  /**
   * Method Declaration.
   * 
   * 
   * @param correlationID
   *
   * @exception JMSException
   *
   * @see
   */
  public void setJMSCorrelationIDAsBytes(byte[] correlationID)
      throws JMSException {
    JMSCorrelationIDAsBytes = correlationID;
  }

  /**
   * Method Declaration.
   * 
   * 
   * @param correlationID
   *
   * @exception JMSException
   *
   * @see
   */
  public void setJMSCorrelationID(String correlationID) throws JMSException {
    JMSCorrelationID = correlationID;
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
  public String getJMSCorrelationID() throws JMSException {
    return JMSCorrelationID;
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
  public Destination getJMSReplyTo() throws JMSException {
    return JMSReplyTo;
  }

  /**
   * Method Declaration.
   * 
   * 
   * @param replyTo
   *
   * @exception JMSException
   *
   * @see
   */
  public void setJMSReplyTo(Destination replyTo) throws JMSException {
    JMSReplyTo = replyTo;
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
  public Destination getJMSDestination() throws JMSException {
    return JMSDestination;
  }

  /**
   * Method Declaration.
   * 
   * 
   * @param destination
   *
   * @exception JMSException
   *
   * @see
   */
  public void setJMSDestination(Destination destination) throws JMSException {
    JMSDestination = destination;
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
  public int getJMSDeliveryMode() throws JMSException {
    return JMSDeliveryMode;
  }

  /**
   * Method Declaration.
   * 
   * 
   * @param deliveryTime
   *
   * @exception JMSException
   *
   * @see
   */
  public void setJMSDeliveryTime(long deliveryTime) throws JMSException {
    JMSDeliveryTime = deliveryTime;
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
  public long getJMSDeliveryTime() throws JMSException {
    return JMSDeliveryTime;
  }

  /**
   * Method Declaration.
   * 
   * 
   * @param deliveryMode
   *
   * @exception JMSException
   *
   * @see
   */
  public void setJMSDeliveryMode(int deliveryMode) throws JMSException {
    JMSDeliveryMode = deliveryMode;
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
  public boolean getJMSRedelivered() throws JMSException {
    return JMSRedelivered;
  }

  /**
   * Method Declaration.
   * 
   * 
   * @param redelivered
   *
   * @exception JMSException
   *
   * @see
   */
  public void setJMSRedelivered(boolean redelivered) throws JMSException {
    JMSRedelivered = redelivered;
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
  public String getJMSType() throws JMSException {
    return JMSType;
  }

  /**
   * Method Declaration.
   * 
   * 
   * @param type
   *
   * @exception JMSException
   *
   * @see
   */
  public void setJMSType(String type) throws JMSException {
    JMSType = type;
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
  public long getJMSExpiration() throws JMSException {
    return JMSExpiration;
  }

  /**
   * Method Declaration.
   * 
   * 
   * @param expiration
   *
   * @exception JMSException
   *
   * @see
   */
  public void setJMSExpiration(long expiration) throws JMSException {
    JMSExpiration = expiration;
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
  public int getJMSPriority() throws JMSException {
    return JMSPriority;
  }

  /**
   * Method Declaration.
   * 
   * 
   * @param priority
   *
   * @exception JMSException
   *
   * @see
   */
  public void setJMSPriority(int priority) throws JMSException {
    JMSPriority = priority;
  }

  /**
   * Method Declaration.
   * 
   * 
   * @exception JMSException
   *
   * @see
   */
  public void clearProperties() throws JMSException {
    properties.clear();
  }

  /**
   * Method Declaration.
   * 
   * 
   * @param name
   *
   * @return
   *
   * @exception JMSException
   *
   * @see
   */
  public boolean propertyExists(String name) throws JMSException {
    return properties.containsKey(name);
  }

  /**
   * Method Declaration.
   * 
   * 
   * @param name
   *
   * @return
   *
   * @exception JMSException
   *
   * @see
   */
  public boolean getBooleanProperty(String name) throws JMSException {
    if (propertyExists(name)) {
      return ((Boolean) properties.get(name)).booleanValue();
    } else {
      throw new JMSException("property does not exist: " + name);
    }
  }

  /**
   * Method Declaration.
   * 
   * 
   * @param name
   *
   * @return
   *
   * @exception JMSException
   *
   * @see
   */
  public byte getByteProperty(String name) throws JMSException {
    if (propertyExists(name)) {
      return ((Byte) properties.get(name)).byteValue();
    } else {
      throw new JMSException("property does not exist: " + name);
    }
  }

  /**
   * Method Declaration.
   * 
   * 
   * @param name
   *
   * @return
   *
   * @exception JMSException
   *
   * @see
   */
  public short getShortProperty(String name) throws JMSException {
    if (propertyExists(name)) {
      return ((Short) properties.get(name)).shortValue();
    } else {
      throw new JMSException("property does not exist: " + name);
    }
  }

  /**
   * Method Declaration.
   * 
   * 
   * @param name
   *
   * @return
   *
   * @exception JMSException
   *
   * @see
   */
  public int getIntProperty(String name) throws JMSException {
    if (propertyExists(name)) {
      return ((Integer) properties.get(name)).intValue();
    } else {
      throw new JMSException("property does not exist: " + name);
    }
  }

  /**
   * Method Declaration.
   * 
   * 
   * @param name
   *
   * @return
   *
   * @exception JMSException
   *
   * @see
   */
  public long getLongProperty(String name) throws JMSException {
    if (propertyExists(name)) {
      return ((Long) properties.get(name)).longValue();
    } else {
      throw new JMSException("property does not exist: " + name);
    }
  }

  /**
   * Method Declaration.
   * 
   * 
   * @param name
   *
   * @return
   *
   * @exception JMSException
   *
   * @see
   */
  public float getFloatProperty(String name) throws JMSException {
    if (propertyExists(name)) {
      return ((Float) properties.get(name)).floatValue();
    } else {
      throw new JMSException("property does not exist: " + name);
    }
  }

  /**
   * Method Declaration.
   * 
   * 
   * @param name
   *
   * @return
   *
   * @exception JMSException
   *
   * @see
   */
  public double getDoubleProperty(String name) throws JMSException {
    if (propertyExists(name)) {
      return ((Double) properties.get(name)).doubleValue();
    } else {
      throw new JMSException("property does not exist: " + name);
    }
  }

  /**
   * Method Declaration.
   * 
   * 
   * @param name
   *
   * @return
   *
   * @exception JMSException
   *
   * @see
   */
  public String getStringProperty(String name) throws JMSException {
    if (propertyExists(name)) {
      return (String) properties.get(name);
    } else {
      throw new JMSException("property does not exist: " + name);
    }
  }

  /**
   * Method Declaration.
   * 
   * 
   * @param name
   *
   * @return
   *
   * @exception JMSException
   *
   * @see
   */
  public Object getObjectProperty(String name) throws JMSException {
    if (propertyExists(name)) {
      return properties.get(name);
    } else {
      throw new JMSException("property does not exist: " + name);
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
  public Enumeration getPropertyNames() throws JMSException {
    return properties.keys();

    // Vector v = new Vector();
    // return v.elements();
  }

  /**
   * Method Declaration.
   * 
   * 
   * @param name
   * @param value
   *
   * @exception JMSException
   *
   * @see
   */
  public void setBooleanProperty(String name, boolean value)
      throws JMSException {
    properties.put(name, Boolean.valueOf(value));
  }

  /**
   * Method Declaration.
   * 
   * 
   * @param name
   * @param value
   *
   * @exception JMSException
   *
   * @see
   */
  public void setByteProperty(String name, byte value) throws JMSException {
    properties.put(name, Byte.valueOf(value));
  }

  /**
   * Method Declaration.
   * 
   * 
   * @param name
   * @param value
   *
   * @exception JMSException
   *
   * @see
   */
  public void setShortProperty(String name, short value) throws JMSException {
    properties.put(name, Short.valueOf(value));
  }

  /**
   * Method Declaration.
   * 
   * 
   * @param name
   * @param value
   *
   * @exception JMSException
   *
   * @see
   */
  public void setIntProperty(String name, int value) throws JMSException {
    properties.put(name, Integer.valueOf(value));
  }

  /**
   * Method Declaration.
   * 
   * 
   * @param name
   * @param value
   *
   * @exception JMSException
   *
   * @see
   */
  public void setLongProperty(String name, long value) throws JMSException {
    properties.put(name, Long.valueOf(value));
  }

  /**
   * Method Declaration.
   * 
   * 
   * @param name
   * @param value
   *
   * @exception JMSException
   *
   * @see
   */
  public void setFloatProperty(String name, float value) throws JMSException {
    properties.put(name, Float.valueOf(value));
  }

  /**
   * Method Declaration.
   * 
   * 
   * @param name
   * @param value
   *
   * @exception JMSException
   *
   * @see
   */
  public void setDoubleProperty(String name, double value) throws JMSException {
    properties.put(name, Double.valueOf(value));
  }

  /**
   * Method Declaration.
   * 
   * 
   * @param name
   * @param value
   *
   * @exception JMSException
   *
   * @see
   */
  public void setStringProperty(String name, String value) throws JMSException {
    properties.put(name, value);
  }

  /**
   * Method Declaration.
   * 
   * 
   * @param name
   * @param value
   *
   * @exception JMSException
   *
   * @see
   */
  public void setObjectProperty(String name, Object value) throws JMSException {
    properties.put(name, value);
  }

  /**
   * Dummy method for acknowledge
   */
  public void acknowledge() throws JMSException {
  }

  /**
   * Dummy method for clear
   */
  public void clearBody() throws JMSException {
  }

  protected void setBufferIsDirty(boolean state) {
    bufferIsDirty = state;
  }

  protected void checkReadAccess() throws JMSException {
    if (!readMode) {
      throw new MessageNotReadableException("Message is not Readable");
    }
  }

  /**
   * Returns the message body as an object of the specified type.
   * 
   * @param c
   *          - The type to which the message body will be assigned.
   * 
   * @return the message body
   * 
   * @exception JMSException
   *              if JMS fails to read message due to some internal JMS error.
   * @exception MessageFormatException
   *              if this type conversion is invalid.
   */
  public <T> T getBody(Class<T> c) throws JMSException {
    return (T) c;
  }

  /**
   * Returns whether the message body is capable of being assigned to the
   * specified type.
   * 
   * @param c
   *          - The specified type.
   * 
   * @return whether the message body is capable of being assigned to the
   *         specified type
   * 
   * @exception JMSException
   *              if a JMS error occurs.
   */
  public boolean isBodyAssignableTo(Class c) throws JMSException {
    return true;
  }
}
