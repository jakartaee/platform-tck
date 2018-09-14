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

package com.sun.ts.tests.jaxrpc.common;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;

import java.io.Serializable;

/**
 * One property bean used for pluggable serializer/deserializer.
 */
public class OnePropBean implements Serializable {
  boolean firstProperty;

  /** No-args default constructor */
  public OnePropBean() {
  }

  /**
   * Constructs a OnePropBean with the property
   */
  public OnePropBean(boolean firstProperty) {
    this.firstProperty = firstProperty;
  }

  /** Setter for firstProperty */
  public void setFirstProperty(boolean firstProperty) {
    this.firstProperty = firstProperty;
  }

  /** Getter for firstProperty */
  public boolean getFirstProperty() {
    return firstProperty;
  }

  /**
   * Returns a string representation of this OnePropBean
   */
  public String toString() {
    return new String((new Boolean(firstProperty)).toString());
  }

  /**
   * Compares this OnePropBean object to the specified object
   */
  public boolean equals(OnePropBean that) {
    if (that == null)
      return false;

    if (this.getFirstProperty() == that.getFirstProperty())
      return true;
    else
      return false;
  }
}
