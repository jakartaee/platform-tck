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

package com.sun.ts.tests.common.ejb.dba;

import java.util.Properties;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import jakarta.ejb.CreateException;
import com.sun.ts.lib.util.TestUtil;

/**
 * DB Support object for DB table using whose primary key is an 'long'.
 */
public class LongDBSupport extends DBSupport implements java.io.Serializable {

  /** Name of the property whose value is the DB table name */
  protected static final String longTablePrefix = "longPKTable";

  PreparedStatement pStmt = null;

  ResultSet result = null;

  /*
   * Cached data
   */
  protected long cofID = 0; /* Coffee ID (Primary Key) */

  protected String cofName = null; /* Coffee Name */

  protected float cofPrice = 0; /* Coffee Price */

  /**
   * Create a new DBSupport object. If called from an EJB or a Web component,
   * you must make sure to call TestUtil.init() before creating a new DBSupport
   * object.
   */
  public LongDBSupport() throws Exception {
    super(longTablePrefix);
  }

  public static void initTable(Properties props) throws Exception {
    DBSupport.initTable(longTablePrefix, props);
  }

  public boolean keyExists(long pkey) throws SQLException {
    try {
      TestUtil.logTrace("[LongDBSupport] keyExists(" + pkey + ")");

      getDBConnection();
      pStmt = getStmt("Select_PK");
      pStmt.setLong(1, pkey);
      result = pStmt.executeQuery();

      return result.next();
    } catch (SQLException e) {
      throw new SQLException("SQLException in keyExists: " + e);
    } finally {
      closeStmt(pStmt, result);
    }
  }

  public void createNewRow(long cofID, String cofName, float cofPrice)
      throws CreateException, SQLException {

    try {
      TestUtil.logTrace("[LongDBSupport] createNewRow(" + cofID + ", " + cofName
          + ", " + cofPrice + ")");

      pStmt = getStmt("Insert");
      pStmt.setLong(1, cofID);
      pStmt.setString(2, cofName);
      pStmt.setFloat(3, cofPrice);
      TestUtil.logTrace("[LongDBSupport] Execute stmt" + pStmt);
      if (1 != pStmt.executeUpdate()) {
        throw new CreateException("INSERT failed in createNewRow");
      } else {
        /* Keep cached state */
        this.cofID = cofID;
        this.cofName = cofName;
        this.cofPrice = cofPrice;
      }
    } catch (SQLException e) {
      TestUtil.printStackTrace(e);
      throw new SQLException("SQLException in createNewRow" + e);
    } finally {
      closeStmt(pStmt, null);
    }

    TestUtil.logTrace("[LongDBSupport] New row created !");
  }

  public float loadPrice(long pkey) throws SQLException {

    try {
      TestUtil.logTrace("[LongDBSupport] loadPrice(" + pkey + ")");

      pStmt = getStmt("Select_Price");
      pStmt.setLong(1, pkey);
      result = pStmt.executeQuery();
      if (!result.next()) {
        throw new SQLException("No record for PK = " + pkey);
      }

      return result.getFloat(1);
    } catch (SQLException e) {
      throw new SQLException("SQLException in loadPrice(): " + e);
    } finally {
      closeStmt(pStmt, result);
    }
  }

  public void storePrice(long pkey, float cofPrice) throws SQLException {

    try {
      TestUtil.logTrace("[LongDBSupport] storePrice()");
      pStmt = getStmt("Update");
      pStmt.setFloat(1, cofPrice);
      pStmt.setLong(2, pkey);
      if (1 != pStmt.executeUpdate()) {
        throw new SQLException("UPDATE failed in storePrice");
      }

      this.cofPrice = cofPrice;
    } catch (SQLException e) {
      throw new SQLException("SQLException in storePrice(): " + e);
    } finally {
      closeStmt(pStmt, null);
    }
  }

  public void removeRow(long pkey) throws SQLException {

    try {
      TestUtil.logTrace("[LongDBSupport] removeRow()");
      pStmt = getStmt("Delete");
      pStmt.setLong(1, pkey);
      if (1 != pStmt.executeUpdate()) {
        throw new SQLException("DELETE failed in removeRow");
      }
    } catch (SQLException e) {
      throw new SQLException("SQLException in removeRow(): " + e);
    } finally {
      closeStmt(pStmt, null);
    }
  }

}
