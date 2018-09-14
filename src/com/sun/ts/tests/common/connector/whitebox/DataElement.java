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

/*
 * @(#)DataElement.java	1.0 06/06/02
 */

package com.sun.ts.tests.common.connector.whitebox;

import javax.transaction.xa.*;

/**
 * A DataElement in the Enterprise Information System(TSeis).
 *
 * @version 1.0, 06/06/02
 * @author Binod P.G
 */
public class DataElement {

  /** Status indicating that the DataElement object is committed **/
  static final int COMMITTED = 1;

  /** Status indicating that the DataElement object prepared for 2PC **/
  static final int PREPARED = 2;

  /** Status indicating that the DataElement object is deleted **/
  static final int DELETED = 3;

  /**
   * Status indicating that the DataElement object is in the uncommitted state
   **/
  static final int INSERTED = 4;

  /**
   * Status indicating that the DataElement object is in the uncommitted state
   **/
  static final int UPDATED = 5;

  /** Current status of DataElement **/
  private int status;

  /** Key of this DataElement **/
  private String key;

  /** Actual Data of this DataElement **/
  private String data;

  /** Prepared Data of this DataElement **/
  private DataElement preparedData;

  /** Xid used to prepare this element **/
  private Xid xid;

  /** Version of this DataElement **/
  private int version;

  /**
   * If key and value is inserted, this constructor will be used to construct
   * the <code> DataElement </code> object.
   * 
   * @param key
   *          Key of the <code> DataElement </code> object.
   * @param data
   *          Value of the <code> DataElement </code> object.
   */
  public DataElement(String key, String data) {
    this.key = key;
    this.data = data;
    version = 1;
  }

  /**
   * If only key is inserted, this constructor will be used to construct the
   * <code> DataElement </code> object.
   * 
   * @param key
   *          Key of the <code> DataElement </code> object.
   */
  public DataElement(String key) {
    this.key = key;
    version = 1;
  }

  /**
   * Sets the status of the object to the value provided.
   *
   * @param status
   *          Status to be set.
   */
  public void setStatus(int status) {
    this.status = status;
  }

  /**
   * Get the status of the element.
   *
   * @return Current status of the <code> DataElement </code> object.
   */
  public int getStatus() {
    return status;
  }

  /**
   * Get the key of the <code> DataElement <code> object.
   * 
   * @retrun	Key of the <code> DataElement </code> object.
   */
  public String getKey() {
    return key;
  }

  /**
   * Get the data in the <code>DataElement</code> object.
   * 
   * @retrun Value of the <code> DataElement </code> object.
   */
  public String getValue() {
    return data;
  }

  /**
   * Set the value of <code> DataElement </code> object.
   * 
   * @param value
   *          Value.
   */
  public void setValue(String value) {
    this.data = value;
  }

  /**
   * Prepare the value for 2PC commit.
   *
   * @param value
   *          Value to be prepared.
   */
  public void prepare(DataElement value, Xid xid) {
    this.preparedData = value;
    this.xid = xid;
    this.status = PREPARED;
  }

  /**
   * Get the prepared data in the <code>DataElement</code> object.
   * 
   * @retrun Prepared of the <code> DataElement </code> object.
   */
  public DataElement getPreparedValue() {
    return preparedData;
  }

  /**
   * Get the version of the data.
   *
   * @return version.
   */
  public int getVersion() {
    return version;
  }

  /**
   * Updates the version of the element.
   */
  public void updateVersion() {
    version++;
  }

  /**
   * Get the xid used to do the prepare.
   *
   * @return Xid.
   */
  public Xid getXid() {
    return xid;
  }

}
