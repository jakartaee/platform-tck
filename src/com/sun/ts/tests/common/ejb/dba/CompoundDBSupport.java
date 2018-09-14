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

package com.sun.ts.tests.common.ejb.dba;

import java.util.Properties;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.ejb.CreateException;
import com.sun.ts.lib.util.TestUtil;

/**
 * DB Support object for DB table using whose primary key is an 'float'.
 */
public class CompoundDBSupport extends DBSupport
    implements java.io.Serializable {

  /** Name of the property whose value is the DB table name */
  protected static final String compoundTablePrefix = "compoundPKTable";

  PreparedStatement pStmt = null;

  ResultSet result = null;

  /*
   * Cached data
   */
  protected CompoundPK cofID = null; /* Coffee ID (Primary Key) */

  protected String cofName = null; /* Coffee Name */

  protected float cofPrice = 0; /* Coffee Price */

  /**
   * Create a new DBSupport object. If called from an EJB or a Web component,
   * you must make sure to call TestUtil.init() before creating a new DBSupport
   * object.
   */
  public CompoundDBSupport() throws Exception {
    super(compoundTablePrefix);
  }

  public static void initTable(Properties props) throws Exception {
    DBSupport.initTable(compoundTablePrefix, props);
  }

  public boolean keyExists(CompoundPK pkey) throws SQLException {
    try {
      TestUtil.logTrace("[CompoundDBSupport] keyExists(" + pkey + ")");

      getDBConnection();
      pStmt = getStmt("Select_PK");
      pStmt.setInt(1, pkey.pmIDInteger.intValue());
      pStmt.setString(2, pkey.pmIDString);
      pStmt.setFloat(3, pkey.pmIDFloat.floatValue());
      result = pStmt.executeQuery();

      return result.next();
    } catch (SQLException e) {
      throw new SQLException("SQL Exception in keyExists:" + e);
    } finally {
      closeStmt(pStmt, result);
    }
  }

  public void createNewRow(CompoundPK cofID, String cofName, float cofPrice)
      throws CreateException, SQLException {

    try {
      TestUtil.logTrace("[CompoundDBSupport] createNewRow(" + cofID + ", "
          + cofName + ", " + cofPrice + ")");

      pStmt = getStmt("Insert");
      pStmt.setInt(1, cofID.pmIDInteger.intValue());
      pStmt.setString(2, cofID.pmIDString);
      pStmt.setFloat(3, cofID.pmIDFloat.floatValue());
      pStmt.setString(4, cofName);
      pStmt.setFloat(5, cofPrice);
      TestUtil.logTrace("[CompoundDBSupport] Execute stmt" + pStmt);
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
      throw new SQLException("SQL Exception in createNewRow" + e);
    } finally {
      closeStmt(pStmt, null);
    }

    TestUtil.logTrace("[CompoundDBSupport] New row created !");
  }

  public float loadPrice(CompoundPK pkey) throws SQLException {

    try {
      TestUtil.logTrace("[CompoundDBSupport] loadPrice(" + pkey + ")");

      pStmt = getStmt("Select_Price");
      pStmt.setInt(1, pkey.pmIDInteger.intValue());
      pStmt.setString(2, pkey.pmIDString);
      pStmt.setFloat(3, pkey.pmIDFloat.floatValue());
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

  public void storePrice(CompoundPK pkey, float cofPrice) throws SQLException {

    try {
      TestUtil.logTrace("[CompoundDBSupport] storePrice()");
      pStmt = getStmt("Update");
      pStmt.setFloat(1, cofPrice);
      pStmt.setInt(2, pkey.pmIDInteger.intValue());
      pStmt.setString(3, pkey.pmIDString);
      pStmt.setFloat(4, pkey.pmIDFloat.floatValue());
      if (1 != pStmt.executeUpdate()) {
        throw new SQLException("SQL UPDATE failed in storePrice");
      }

      this.cofPrice = cofPrice;
    } catch (SQLException e) {
      throw new SQLException("SQL Exception in storePrice(): " + e);
    } finally {
      closeStmt(pStmt, null);
    }
  }

  public void removeRow(CompoundPK pkey) throws SQLException {

    try {
      TestUtil.logTrace("[CompoundDBSupport] removeRow()");
      pStmt = getStmt("Delete");
      pStmt.setInt(1, pkey.pmIDInteger.intValue());
      pStmt.setString(2, pkey.pmIDString);
      pStmt.setFloat(3, pkey.pmIDFloat.floatValue());
      if (1 != pStmt.executeUpdate()) {
        throw new SQLException("DELETE failed in removeRow");
      }
    } catch (SQLException e) {
      throw new SQLException("SQL Exception in removeRow(): " + e);
    } finally {
      closeStmt(pStmt, null);
    }
  }

}
