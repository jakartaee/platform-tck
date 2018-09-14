/*
 * Copyright (c) 2013, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.ejb30.lite.packaging.embed.classloader.annotated;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import com.sun.ts.lib.util.TestUtil;

public class TSDbUtil {

  private Connection databaseConnection;

  private String databaseURL;

  private String databaseUser;

  private String databasePassword;

  private String driverClassName;

  public TSDbUtil(String url, String user, String password, String driverName) {

    databaseURL = url;
    databaseUser = user;
    databasePassword = password;
    driverClassName = driverName;

    databaseConnection = getDatabaseConnection(databaseURL, databaseUser,
        databasePassword, driverClassName);
    TestUtil.logMsg("TSDbUtil constructore called");

  }

  private Connection getDatabaseConnection(String url, String user,
      String password, String driverClassName) {

    Connection con = null;

    try {
      Class.forName(driverClassName);
      con = DriverManager.getConnection(url, user, password);
      TestUtil.logMsg("Got datbase connection");

    } catch (Exception e) {
      TestUtil.logErr("Failed to load database driver");
      e.printStackTrace();
      return null;
    }

    return con;

  }

  public Connection getConnection() {
    return this.databaseConnection;
  }

  public int writeToDatabase(Connection con, String key, String value) {

    int updateCount = 0;
    Statement statement;
    try {

      statement = con.createStatement();
      updateCount = statement
          .executeUpdate("INSERT INTO EJB_AUTOCLOSE_TAB (NAME, MESSAGE) "
              + "VALUES ('" + key + "'" + ",'" + value + "')");
      TestUtil.logMsg(
          "Database write successful : wrote " + updateCount + " record");
    } catch (Exception e) {
      TestUtil.logErr("Error updating database");
      e.printStackTrace();
    }

    return updateCount;
  }

  public int deleteRecordFromDatabase(Connection con, String key) {

    int updateCount = 0;
    Statement statement;
    try {

      statement = con.createStatement();
      updateCount = statement.executeUpdate(
          "DELETE FROM EJB_AUTOCLOSE_TAB WHERE NAME ='" + key + "'");
      TestUtil.logMsg("Deleted " + updateCount + " record");
    } catch (Exception e) {
      TestUtil.logErr("Error deleting record from database");
      e.printStackTrace();
    }

    return updateCount;
  }

  public String readFromDatabase(Connection con, String key) {

    String result = null;
    Statement statement;

    try {
      statement = con.createStatement();
      ResultSet resultSet = statement.executeQuery(
          "SELECT MESSAGE FROM EJB_AUTOCLOSE_TAB WHERE NAME='" + key + "'");

      while (resultSet.next()) {
        result = resultSet.getString(1);
      }

      TestUtil.logMsg("Read record from Database");
    } catch (Exception e) {
      TestUtil.logErr("Error reading database");
      e.printStackTrace();

    }

    return result;
  }

}
