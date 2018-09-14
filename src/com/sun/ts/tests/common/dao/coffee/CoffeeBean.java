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

package com.sun.ts.tests.common.dao.coffee;

import java.util.Properties;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.ejb.CreateException;
import com.sun.ts.lib.util.TestUtil;

/**
 * DB Support object for DB table using whose primary key is an 'int'.
 */
public class CoffeeBean implements java.io.Serializable {

  private int id = 0; /* Coffee ID (Primary Key) */

  private String name = null; /* Coffee Name */

  private float price = 0; /* Coffee Price */

  public CoffeeBean() throws Exception {
    this(0, "", 0);
  }

  public CoffeeBean(int id, String name, float price) {
    if (null == name) {
      throw new IllegalArgumentException("null name");
    }

    this.id = id;
    this.name = name;
    this.price = price;
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public float getPrice() {
    return price;
  }

  public void setId(int id) {
    this.id = id;
  }

  public void setName(String name) {
    if (null == name) {
      throw new IllegalArgumentException("null name");
    }
    this.name = name;
  }

  public void setPrice(float price) {
    this.price = price;
  }

}
