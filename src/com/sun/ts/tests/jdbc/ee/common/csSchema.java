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
 * %W% %E%
 */

/*
 * @(#)csSchema.java	1.15 02/04/22
 */
package com.sun.ts.tests.jdbc.ee.common;

import java.io.*;
import java.util.*;
import java.sql.*;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.harness.*;

import java.rmi.RemoteException;

/**
 * The csSchema class creates the database and procedures using Sun's J2EE
 * Reference Implementation.
 * 
 * @author
 * @version 1.7, 06/16/99
 */

public class csSchema extends ServiceEETest {
  private Properties props = null;

  public void dbUnConnect(Connection conn) throws RemoteException {
    TestUtil.logTrace("dbUnConnect");
    // Close the DB connections
    try {
      conn.close();
      TestUtil.logMsg("Closed database connection");
    } catch (Exception e) {
      TestUtil.logErr(
          "Exception occured while trying to close the DB connection", e);
      throw new RemoteException(e.getMessage());
    }
  }

  public boolean supportsType(String dataTypeParam, Connection conn) {

    boolean retValue = false;

    try {

      logTrace("Creating DBMetaData Object");
      DatabaseMetaData dbmeta = conn.getMetaData();
      ResultSet rs = dbmeta.getTypeInfo();

      while (rs.next() && retValue == false) {
        String typeName = rs.getString(1);
        if (typeName != null) {
          if (typeName.equalsIgnoreCase(dataTypeParam)
              || (dataTypeParam.equalsIgnoreCase("BOOLEAN")
                  && typeName.equalsIgnoreCase("BOOL"))) {
            // the typeName is usually "BOOLEAN" but for mysql, it
            // sometimes comes in as "BOOL" so we need to check both
            short dataType = rs.getShort(2);
            if (matchesExpectedType(dataTypeParam, dataType))
              retValue = true;
          }
        }
      }
      if (retValue == true)
        logTrace("DataType: " + dataTypeParam + " is supported");
      else
        logTrace("DataType: " + dataTypeParam + " is not supported");

    } catch (Exception ex2) {

      logErr("Exception in supportsType method ", ex2);
    }

    return retValue;
  }

  public static boolean matchesExpectedType(String dataTypeParam,
      short dataType) {
    boolean retValue = false;
    switch (dataType) {
    case java.sql.Types.BOOLEAN:
    case java.sql.Types.BIT:
      if (dataTypeParam.equalsIgnoreCase("BOOLEAN"))
        retValue = true;
      break;
    case java.sql.Types.TINYINT:
      if (dataTypeParam.equalsIgnoreCase("TINYINT"))
        retValue = true;
      break;
    case java.sql.Types.SMALLINT:
      if (dataTypeParam.equalsIgnoreCase("SMALLINT"))
        retValue = true;
      break;
    // Other types have a direct mapping.
    default:
      retValue = true;

    }
    return retValue;
  }

}
