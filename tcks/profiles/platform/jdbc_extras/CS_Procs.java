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

package com.sun.ts.lib.tests.jdbc;

import java.sql.*;
import java.math.*;

/**
 * Stored procedures for output parameters.
 * 
 * Note that if a null might occur, the Java class form is used rather than the
 * Java built-in form. i.e., Integer instead of int. This is so that the null
 * can be transmitted back to the caller. If we know it won't be null, we just
 * use the built-in type directly.
 * 
 **/

public class CS_Procs {

  /*****************
   * Defining methods for CallableStatement OUT parameter
   *******************/

  public static void Numeric_Proc(BigDecimal[] max_param,
      BigDecimal[] min_param, BigDecimal[] null_param) throws SQLException {

    Connection con = DriverManager.getConnection("jdbc:default:connection");
    Statement stmt = con.createStatement();
    ResultSet rs = stmt
        .executeQuery("select max_val, min_val, null_val from numeric_tab");

    if (rs.next()) {

      max_param[0] = rs.getBigDecimal(1);
      min_param[0] = rs.getBigDecimal(2);
      null_param[0] = rs.getBigDecimal(3);
    } else {

      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    stmt.close();
    stmt = null;
    con.close();
    con = null;
  }

  public static void Decimal_Proc(BigDecimal[] max_param,
      BigDecimal[] min_param, BigDecimal[] null_param) throws SQLException {

    Connection con = DriverManager.getConnection("jdbc:default:connection");
    Statement stmt = con.createStatement();
    ResultSet rs = stmt
        .executeQuery("select max_val,min_val,null_val from Decimal_Tab");

    if (rs.next()) {

      max_param[0] = rs.getBigDecimal(1);
      min_param[0] = rs.getBigDecimal(2);
      null_param[0] = rs.getBigDecimal(3);
    } else {

      throw new SQLException("Data not found");
    }
    rs.close();
    rs = null;
    stmt.close();
    stmt = null;
    con.close();
    con = null;

  }

  public static void Double_Proc(double[] max_param, double[] min_param,
      BigDecimal[] null_param) throws SQLException {

    Connection con = DriverManager.getConnection("jdbc:default:connection");
    Statement stmt = con.createStatement();
    ResultSet rs = stmt
        .executeQuery("select max_val, min_val, null_val from double_tab");

    if (rs.next()) {

      max_param[0] = rs.getDouble(1);
      min_param[0] = rs.getDouble(2);
      null_param[0] = rs.getBigDecimal(3);
      if (rs.wasNull())
        null_param[0] = null;
    } else {

      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    stmt.close();
    stmt = null;
    con.close();
    con = null;
  }

  public static void Float_Proc(double[] max_param, double[] min_param,
      BigDecimal[] null_param) throws SQLException {

    Connection con = DriverManager.getConnection("jdbc:default:connection");
    Statement stmt = con.createStatement();
    ResultSet rs = stmt
        .executeQuery("select max_val, min_val, null_val from float_tab");

    if (rs.next()) {

      max_param[0] = rs.getDouble(1);
      min_param[0] = rs.getDouble(2);
      null_param[0] = rs.getBigDecimal(3);
      if (rs.wasNull())
        null_param[0] = null;
    } else {

      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    stmt.close();
    stmt = null;
    con.close();
    con = null;

  }

  public static void Real_Proc(float[] max_param, float[] min_param,
      BigDecimal[] null_param) throws SQLException {

    Connection con = DriverManager.getConnection("jdbc:default:connection");
    Statement stmt = con.createStatement();
    ResultSet rs = stmt
        .executeQuery("select max_val,min_val,null_val from Real_Tab");

    if (rs.next()) {

      max_param[0] = rs.getFloat(1);
      min_param[0] = rs.getFloat(2);
      null_param[0] = rs.getBigDecimal(3);
      if (rs.wasNull())
        null_param[0] = null;
    } else {

      throw new SQLException("Data not found");
    }
    rs.close();
    rs = null;
    stmt.close();
    stmt = null;
    con.close();
    con = null;
  }

  public static void Bit_Proc(boolean[] max_param, boolean[] min_param,
      BigDecimal[] null_param) throws SQLException {

    Connection con = DriverManager.getConnection("jdbc:default:connection");
    Statement stmt = con.createStatement();
    ResultSet rs = stmt
        .executeQuery("select max_val, min_val, null_val from bit_tab");

    if (rs.next()) {

      max_param[0] = rs.getBoolean(1);
      min_param[0] = rs.getBoolean(2);
      null_param[0] = rs.getBigDecimal(3);
      if (rs.wasNull())
        null_param[0] = null;
    } else {

      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    stmt.close();
    stmt = null;
    con.close();
    con = null;
  }

  public static void Smallint_Proc(short[] max_param, short[] min_param,
      BigDecimal[] null_param) throws SQLException {

    Connection con = DriverManager.getConnection("jdbc:default:connection");
    Statement stmt = con.createStatement();
    ResultSet rs = stmt
        .executeQuery("select max_val, min_val, null_val from smallint_tab");

    if (rs.next()) {

      max_param[0] = rs.getShort(1);
      min_param[0] = rs.getShort(2);
      null_param[0] = rs.getBigDecimal(3);
      if (rs.wasNull())
        null_param[0] = null;
    } else {
      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    stmt.close();
    stmt = null;
    con.close();
    con = null;
  }

  public static void Tinyint_Proc(int[] max_param, int[] min_param,
      BigDecimal[] null_param) throws SQLException {

    Connection con = DriverManager.getConnection("jdbc:default:connection");
    Statement stmt = con.createStatement();
    ResultSet rs = stmt
        .executeQuery("select max_val, min_val, null_val from tinyint_tab");

    if (rs.next()) {

      max_param[0] = rs.getInt(1);
      min_param[0] = rs.getInt(2);
      null_param[0] = rs.getBigDecimal(3);
      if (rs.wasNull())
        null_param[0] = null;
    } else {

      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    stmt.close();
    stmt = null;
    con.close();
    con = null;
  }

  public static void Integer_Proc(int[] max_param, int[] min_param,
      BigDecimal[] null_param) throws SQLException {

    Connection con = DriverManager.getConnection("jdbc:default:connection");
    Statement stmt = con.createStatement();
    ResultSet rs = stmt
        .executeQuery("select max_val, min_val, null_val from integer_tab");

    if (rs.next()) {

      max_param[0] = rs.getInt(1);
      min_param[0] = rs.getInt(2);
      null_param[0] = rs.getBigDecimal(3);
      if (rs.wasNull())
        null_param[0] = null;
    } else {

      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    stmt.close();
    stmt = null;
    con.close();
    con = null;
  }

  public static void Bigint_Proc(long[] max_param, long[] min_param,
      BigDecimal[] null_param) throws SQLException {

    Connection con = DriverManager.getConnection("jdbc:default:connection");
    Statement stmt = con.createStatement();
    ResultSet rs = stmt
        .executeQuery("select max_val, min_val, null_val from bigint_tab");

    if (rs.next()) {

      max_param[0] = rs.getLong(1);
      min_param[0] = rs.getLong(2);
      null_param[0] = rs.getBigDecimal(3);
      if (rs.wasNull())
        null_param[0] = null;
    } else {

      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    stmt.close();
    stmt = null;
    con.close();
    con = null;
  }

  public static void Char_Proc(String[] coffee_param, String[] null_param)
      throws SQLException {

    Connection con = DriverManager.getConnection("jdbc:default:connection");
    Statement stmt = con.createStatement();
    ResultSet rs = stmt
        .executeQuery("select coffee_name, null_val from char_tab");

    if (rs.next()) {

      coffee_param[0] = rs.getString(1);
      null_param[0] = rs.getString(2);
    } else {

      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    stmt.close();
    stmt = null;
    con.close();
    con = null;
  }

  public static void Varchar_Proc(String[] coffee_param, String[] null_param)
      throws SQLException {

    Connection con = DriverManager.getConnection("jdbc:default:connection");
    Statement stmt = con.createStatement();
    ResultSet rs = stmt
        .executeQuery("select coffee_name, null_val from varchar_tab");

    if (rs.next()) {

      coffee_param[0] = rs.getString(1);
      null_param[0] = rs.getString(2);
    } else {

      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    stmt.close();
    stmt = null;
    con.close();
    con = null;
  }

  public static void Longvarchar_Proc(String[] coffee_param)
      throws SQLException {

    Connection con = DriverManager.getConnection("jdbc:default:connection");
    Statement stmt = con.createStatement();
    ResultSet rs = stmt.executeQuery("select coffee_name from longvarchar_tab");

    if (rs.next()) {

      coffee_param[0] = rs.getString(1);
    } else {

      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    stmt.close();
    stmt = null;
    con.close();
    con = null;
  }

  public static void Longvarcharnull_Proc(String[] null_param)
      throws SQLException {

    Connection con = DriverManager.getConnection("jdbc:default:connection");
    Statement stmt = con.createStatement();
    ResultSet rs = stmt
        .executeQuery("select null_val from longvarcharnull_tab");

    if (rs.next()) {

      null_param[0] = rs.getString(1);
    } else {

      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    stmt.close();
    stmt = null;
    con.close();
    con = null;
  }

  public static void Date_Proc(Date[] mfg_param, Date[] null_param)
      throws SQLException {

    Connection con = DriverManager.getConnection("jdbc:default:connection");
    Statement stmt = con.createStatement();
    ResultSet rs = stmt.executeQuery("select mfg_date, null_val from date_tab");

    if (rs.next()) {

      mfg_param[0] = rs.getDate(1);
      null_param[0] = rs.getDate(2);
    } else {

      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    stmt.close();
    stmt = null;
    con.close();
    con = null;
  }

  public static void Time_Proc(Time[] brk_param, Time[] null_param)
      throws SQLException {

    Connection con = DriverManager.getConnection("jdbc:default:connection");
    Statement stmt = con.createStatement();
    ResultSet rs = stmt.executeQuery("select brk_time, null_val from time_tab");

    if (rs.next()) {

      brk_param[0] = rs.getTime(1);
      null_param[0] = rs.getTime(2);
    } else {

      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    stmt.close();
    stmt = null;
    con.close();
    con = null;
  }

  public static void Timestamp_Proc(Timestamp[] in_param,
      Timestamp[] null_param) throws SQLException {

    Connection con = DriverManager.getConnection("jdbc:default:connection");
    Statement stmt = con.createStatement();
    ResultSet rs = stmt
        .executeQuery("select in_time, null_val from timestamp_tab");

    if (rs.next()) {

      in_param[0] = rs.getTimestamp(1);
      null_param[0] = rs.getTimestamp(2);
    } else {

      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    stmt.close();
    stmt = null;
    con.close();
    con = null;
  }

  public static void Binary_Proc(byte[][] binary_param) throws SQLException {

    Connection con = DriverManager.getConnection("jdbc:default:connection");
    Statement stmt = con.createStatement();
    ResultSet rs = stmt.executeQuery("select binary_val from binary_tab");

    if (rs.next()) {

      binary_param[0] = rs.getBytes(1);

    } else {

      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    stmt.close();
    stmt = null;
    con.close();
    con = null;
  }

  public static void Varbinary_Proc(byte[][] varbinary_param)
      throws SQLException {

    Connection con = DriverManager.getConnection("jdbc:default:connection");
    Statement stmt = con.createStatement();
    ResultSet rs = stmt.executeQuery("select varbinary_val from varbinary_tab");

    if (rs.next()) {

      varbinary_param[0] = rs.getBytes(1);

    } else {

      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    stmt.close();
    stmt = null;
    con.close();
    con = null;
  }

  public static void Longvarbinary_Proc(byte[][] longvarbinary_param)
      throws SQLException {

    Connection con = DriverManager.getConnection("jdbc:default:connection");
    Statement stmt = con.createStatement();
    ResultSet rs = stmt
        .executeQuery("select longvarbinary_val from longvarbinary_tab");

    if (rs.next()) {

      longvarbinary_param[0] = rs.getBytes(1);

    } else {

      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    stmt.close();
    stmt = null;
    con.close();
    con = null;
  }

  public static void Integer_In_Proc(int in_param) throws SQLException {

    Connection con = DriverManager.getConnection("jdbc:default:connection");
    PreparedStatement ps = con
        .prepareStatement("update Integer_Tab set MAX_VAL=?");

    ps.setInt(1, in_param);
    ps.executeUpdate();

    ps.close();
    ps = null;
    con.close();
    con = null;
  }

  public static void Integer_InOut_Proc(int[] inout_param) throws SQLException {

    Connection con = DriverManager.getConnection("jdbc:default:connection");
    PreparedStatement ps = con
        .prepareStatement("select max_val from integer_Tab where min_val=?");

    ps.setInt(1, inout_param[0]);
    ResultSet rs = ps.executeQuery();

    if (rs.next()) {

      inout_param[0] = rs.getInt(1);
    } else {

      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    ps.close();
    ps = null;
    con.close();
    con = null;
  }

  public static void UpdCoffee_Proc(BigDecimal type_param) throws SQLException {

    Connection con = DriverManager.getConnection("jdbc:default:connection");
    PreparedStatement ps = con.prepareStatement(
        "update ctstable2 set PRICE=PRICE*20 where TYPE_ID=?");

    ps.setBigDecimal(1, type_param);
    ps.executeUpdate();

    ps.close();
    ps = null;
    con.close();
    con = null;
  }

  public static void SelCoffee_Proc(String[] keyid_param) throws SQLException {

    Connection con = DriverManager.getConnection("jdbc:default:connection");
    Statement stmt = con.createStatement();
    ResultSet rs = stmt
        .executeQuery("select KEY_ID from CTSTABLE2 where TYPE_ID=1");

    if (rs.next()) {

      keyid_param[0] = rs.getString(1);
    } else {

      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    stmt.close();
    stmt = null;
    con.close();
    con = null;
  }

  public static void IOCoffee_Proc(float[] price_param) throws SQLException {

    Connection con = DriverManager.getConnection("jdbc:default:connection");
    PreparedStatement ps = con
        .prepareStatement("select price*2 from CTSTABLE2 where price=?");

    // if (price_param[0]==null)
    // ps.setNull(1,Types.FLOAT);
    // else
    ps.setFloat(1, price_param[0]);

    ResultSet rs = ps.executeQuery();
    if (rs.next()) {

      price_param[0] = rs.getFloat(1);
      // if (rs.wasNull()) price_param[0]=null;
    } else {

      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    ps.close();
    ps = null;
    con.close();
    con = null;
  }

  public static void Coffee_Proc(BigDecimal type_param) throws SQLException {

    Connection con = DriverManager.getConnection("jdbc:default:connection");
    PreparedStatement ps1, ps2;

    ps1 = con
        .prepareStatement("update CTSTABLE2 set PRICE=Price*2 where TYPE_ID=?");
    ps1.setBigDecimal(1, type_param);
    ps1.executeUpdate();
    ps1.close();
    ps1 = null;

    ps2 = con.prepareStatement("delete from CTSTABLE2 where TYPE_ID=?-1");
    ps2.setBigDecimal(1, type_param);
    ps2.executeUpdate();
    ps2.close();
    ps2 = null;

    con.close();
    con = null;
  }

  /****************
   * Defining methods for CallableStatement INOUT parameter
   ******************/

  public static void Numeric_Io_Max(BigDecimal[] inout_param)
      throws SQLException {

    Connection con = DriverManager.getConnection("jdbc:default:connection");
    PreparedStatement ps = con
        .prepareStatement("update Numeric_Tab set max_val =?");
    ps.setBigDecimal(1, inout_param[0]);
    ps.executeUpdate();

    Statement stmt = con.createStatement();
    ResultSet rs = stmt.executeQuery("Select max_val from Numeric_Tab");

    if (rs.next()) {

      inout_param[0] = rs.getBigDecimal(1);
    } else {

      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    ps.close();
    ps = null;
    stmt.close();
    stmt = null;
    con.close();
    con = null;
  }

  public static void Numeric_Io_Min(BigDecimal[] inout_param)
      throws SQLException {

    Connection con = DriverManager.getConnection("jdbc:default:connection");
    PreparedStatement ps = con
        .prepareStatement("update Numeric_Tab set min_val =?");
    ps.setBigDecimal(1, inout_param[0]);
    ps.executeUpdate();

    Statement stmt = con.createStatement();
    ResultSet rs = stmt.executeQuery("Select min_val from Numeric_Tab");

    if (rs.next()) {

      inout_param[0] = rs.getBigDecimal(1);
    } else {

      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    ps.close();
    ps = null;
    stmt.close();
    stmt = null;
    con.close();
    con = null;
  }

  public static void Numeric_Io_Null(BigDecimal[] inout_param)
      throws SQLException {

    Connection con = DriverManager.getConnection("jdbc:default:connection");
    PreparedStatement ps = con
        .prepareStatement("update Numeric_Tab set null_val =?");
    ps.setBigDecimal(1, inout_param[0]);
    ps.executeUpdate();

    Statement stmt = con.createStatement();
    ResultSet rs = stmt.executeQuery("Select null_val from Numeric_Tab");

    if (rs.next()) {

      inout_param[0] = rs.getBigDecimal(1);
    } else {

      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    ps.close();
    ps = null;
    stmt.close();
    stmt = null;
    con.close();
    con = null;
  }

  public static void Decimal_Io_Max(BigDecimal[] inout_param)
      throws SQLException {

    Connection con = DriverManager.getConnection("jdbc:default:connection");
    PreparedStatement ps = con
        .prepareStatement("update Decimal_Tab set max_val =?");
    ps.setBigDecimal(1, inout_param[0]);
    ps.executeUpdate();

    Statement stmt = con.createStatement();
    ResultSet rs = stmt.executeQuery("Select max_val from Decimal_Tab");

    if (rs.next()) {

      inout_param[0] = rs.getBigDecimal(1);
    } else {

      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    ps.close();
    ps = null;
    stmt.close();
    stmt = null;
    con.close();
    con = null;
  }

  public static void Decimal_Io_Min(BigDecimal[] inout_param)
      throws SQLException {

    Connection con = DriverManager.getConnection("jdbc:default:connection");
    PreparedStatement ps = con
        .prepareStatement("update Decimal_Tab set min_val =?");
    ps.setBigDecimal(1, inout_param[0]);
    ps.executeUpdate();

    Statement stmt = con.createStatement();
    ResultSet rs = stmt.executeQuery("Select min_val from Decimal_Tab");

    if (rs.next()) {

      inout_param[0] = rs.getBigDecimal(1);
    } else {

      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    ps.close();
    ps = null;
    stmt.close();
    stmt = null;
    con.close();
    con = null;
  }

  public static void Decimal_Io_Null(BigDecimal[] inout_param)
      throws SQLException {

    Connection con = DriverManager.getConnection("jdbc:default:connection");
    PreparedStatement ps = con
        .prepareStatement("update Decimal_Tab set null_val =?");
    ps.setBigDecimal(1, inout_param[0]);
    ps.executeUpdate();

    Statement stmt = con.createStatement();
    ResultSet rs = stmt.executeQuery("Select null_val from Decimal_Tab");

    if (rs.next()) {

      inout_param[0] = rs.getBigDecimal(1);
    } else {

      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    ps.close();
    ps = null;
    stmt.close();
    stmt = null;
    con.close();
    con = null;
  }

  public static void Double_Io_Max(double[] inout_param) throws SQLException {

    Connection con = DriverManager.getConnection("jdbc:default:connection");
    PreparedStatement ps = con
        .prepareStatement("update Double_Tab set max_val =?");
    ps.setDouble(1, inout_param[0]);
    ps.executeUpdate();

    Statement stmt = con.createStatement();
    ResultSet rs = stmt.executeQuery("Select max_val from Double_Tab");

    if (rs.next()) {

      inout_param[0] = rs.getDouble(1);
    } else {

      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    ps.close();
    ps = null;
    stmt.close();
    stmt = null;
    con.close();
    con = null;
  }

  public static void Double_Io_Min(double[] inout_param) throws SQLException {

    Connection con = DriverManager.getConnection("jdbc:default:connection");
    PreparedStatement ps = con
        .prepareStatement("update Double_Tab set min_val =?");
    ps.setDouble(1, inout_param[0]);
    ps.executeUpdate();

    Statement stmt = con.createStatement();
    ResultSet rs = stmt.executeQuery("Select min_val from Double_Tab");

    if (rs.next()) {

      inout_param[0] = rs.getDouble(1);
    } else {

      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    ps.close();
    ps = null;
    stmt.close();
    stmt = null;
    con.close();
    con = null;
  }

  public static void Double_Io_Null(double[] inout_param) throws SQLException {

    Connection con = DriverManager.getConnection("jdbc:default:connection");
    PreparedStatement ps = con
        .prepareStatement("update Double_Tab set null_val =?");
    ps.setDouble(1, inout_param[0]);
    ps.executeUpdate();

    Statement stmt = con.createStatement();
    ResultSet rs = stmt.executeQuery("Select null_val from Double_Tab");

    if (rs.next()) {

      inout_param[0] = rs.getDouble(1);
    } else {

      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    ps.close();
    ps = null;
    stmt.close();
    stmt = null;
    con.close();
    con = null;
  }

  public static void Float_Io_Max(double[] inout_param) throws SQLException {

    Connection con = DriverManager.getConnection("jdbc:default:connection");
    PreparedStatement ps = con
        .prepareStatement("update Float_Tab set max_val =?");
    ps.setDouble(1, inout_param[0]);
    ps.executeUpdate();

    Statement stmt = con.createStatement();
    ResultSet rs = stmt.executeQuery("Select max_val from Float_Tab");

    if (rs.next()) {

      inout_param[0] = rs.getDouble(1);
    } else {

      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    ps.close();
    ps = null;
    stmt.close();
    stmt = null;
    con.close();
    con = null;
  }

  public static void Float_Io_Min(double[] inout_param) throws SQLException {

    Connection con = DriverManager.getConnection("jdbc:default:connection");
    PreparedStatement ps = con
        .prepareStatement("update Float_Tab set min_val =?");
    ps.setDouble(1, inout_param[0]);
    ps.executeUpdate();

    Statement stmt = con.createStatement();
    ResultSet rs = stmt.executeQuery("Select min_val from Float_Tab");

    if (rs.next()) {

      inout_param[0] = rs.getDouble(1);
    } else {

      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    ps.close();
    ps = null;
    stmt.close();
    stmt = null;
    con.close();
    con = null;
  }

  public static void Float_Io_Null(double[] inout_param) throws SQLException {

    Connection con = DriverManager.getConnection("jdbc:default:connection");
    PreparedStatement ps = con
        .prepareStatement("update Float_Tab set null_val =?");
    ps.setDouble(1, inout_param[0]);
    ps.executeUpdate();

    Statement stmt = con.createStatement();
    ResultSet rs = stmt.executeQuery("Select null_val from Float_Tab");

    if (rs.next()) {

      inout_param[0] = rs.getDouble(1);
    } else {

      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    ps.close();
    ps = null;
    stmt.close();
    stmt = null;
    con.close();
    con = null;
  }

  public static void Real_Io_Max(float[] inout_param) throws SQLException {

    Connection con = DriverManager.getConnection("jdbc:default:connection");
    PreparedStatement ps = con
        .prepareStatement("update Real_Tab set max_val =?");
    ps.setFloat(1, inout_param[0]);
    ps.executeUpdate();

    Statement stmt = con.createStatement();
    ResultSet rs = stmt.executeQuery("Select max_val from Real_Tab");

    if (rs.next()) {

      inout_param[0] = rs.getFloat(1);
    } else {

      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    ps.close();
    ps = null;
    stmt.close();
    stmt = null;
    con.close();
    con = null;
  }

  public static void Real_Io_Min(float[] inout_param) throws SQLException {

    Connection con = DriverManager.getConnection("jdbc:default:connection");
    PreparedStatement ps = con
        .prepareStatement("update Real_Tab set min_val =?");
    ps.setFloat(1, inout_param[0]);
    ps.executeUpdate();

    Statement stmt = con.createStatement();
    ResultSet rs = stmt.executeQuery("Select min_val from Real_Tab");

    if (rs.next()) {

      inout_param[0] = rs.getFloat(1);
    } else {

      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    ps.close();
    ps = null;
    stmt.close();
    stmt = null;
    con.close();
    con = null;
  }

  public static void Real_Io_Null(float[] inout_param) throws SQLException {

    Connection con = DriverManager.getConnection("jdbc:default:connection");
    PreparedStatement ps = con
        .prepareStatement("update Real_Tab set null_val =?");
    ps.setFloat(1, inout_param[0]);
    ps.executeUpdate();

    Statement stmt = con.createStatement();
    ResultSet rs = stmt.executeQuery("Select null_val from Real_Tab");

    if (rs.next()) {

      inout_param[0] = rs.getFloat(1);
    } else {

      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    ps.close();
    ps = null;
    stmt.close();
    stmt = null;
    con.close();
    con = null;
  }

  public static void Bit_Io_Max(boolean[] inout_param) throws SQLException {

    Connection con = DriverManager.getConnection("jdbc:default:connection");
    PreparedStatement ps = con
        .prepareStatement("update Bit_Tab set max_val =?");
    ps.setBoolean(1, inout_param[0]);
    ps.executeUpdate();

    Statement stmt = con.createStatement();
    ResultSet rs = stmt.executeQuery("Select max_val from Bit_Tab");

    if (rs.next()) {

      inout_param[0] = rs.getBoolean(1);
    } else {

      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    ps.close();
    ps = null;
    stmt.close();
    stmt = null;
    con.close();
    con = null;
  }

  public static void Bit_Io_Min(boolean[] inout_param) throws SQLException {

    Connection con = DriverManager.getConnection("jdbc:default:connection");
    PreparedStatement ps = con
        .prepareStatement("update Bit_Tab set min_val =?");
    ps.setBoolean(1, inout_param[0]);
    ps.executeUpdate();

    Statement stmt = con.createStatement();
    ResultSet rs = stmt.executeQuery("Select min_val from Bit_Tab");

    if (rs.next()) {

      inout_param[0] = rs.getBoolean(1);
    } else {

      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    ps.close();
    ps = null;
    stmt.close();
    stmt = null;
    con.close();
    con = null;
  }

  public static void Bit_Io_Null(boolean[] inout_param) throws SQLException {

    Connection con = DriverManager.getConnection("jdbc:default:connection");
    PreparedStatement ps = con
        .prepareStatement("update Bit_Tab set null_val =?");
    ps.setBoolean(1, inout_param[0]);
    ps.executeUpdate();

    Statement stmt = con.createStatement();
    ResultSet rs = stmt.executeQuery("Select null_val from Bit_Tab");

    if (rs.next()) {

      inout_param[0] = rs.getBoolean(1);
    } else {

      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    ps.close();
    ps = null;
    stmt.close();
    stmt = null;
    con.close();
    con = null;
  }

  public static void Smallint_Io_Max(short[] inout_param) throws SQLException {

    Connection con = DriverManager.getConnection("jdbc:default:connection");
    PreparedStatement ps = con
        .prepareStatement("update Smallint_Tab set max_val =?");
    ps.setShort(1, inout_param[0]);
    ps.executeUpdate();

    Statement stmt = con.createStatement();
    ResultSet rs = stmt.executeQuery("Select max_val from Smallint_Tab");

    if (rs.next()) {

      inout_param[0] = rs.getShort(1);
    } else {

      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    ps.close();
    ps = null;
    stmt.close();
    stmt = null;
    con.close();
    con = null;
  }

  public static void Smallint_Io_Min(short[] inout_param) throws SQLException {

    Connection con = DriverManager.getConnection("jdbc:default:connection");
    PreparedStatement ps = con
        .prepareStatement("update Smallint_Tab set min_val =?");
    ps.setShort(1, inout_param[0]);
    ps.executeUpdate();

    Statement stmt = con.createStatement();
    ResultSet rs = stmt.executeQuery("Select min_val from Smallint_Tab");

    if (rs.next()) {

      inout_param[0] = rs.getShort(1);
    } else {

      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    ps.close();
    ps = null;
    stmt.close();
    stmt = null;
    con.close();
    con = null;
  }

  public static void Smallint_Io_Null(short[] inout_param) throws SQLException {

    Connection con = DriverManager.getConnection("jdbc:default:connection");
    PreparedStatement ps = con
        .prepareStatement("update Smallint_Tab set null_val =?");
    ps.setShort(1, inout_param[0]);
    ps.executeUpdate();

    Statement stmt = con.createStatement();
    ResultSet rs = stmt.executeQuery("Select null_val from Smallint_Tab");

    if (rs.next()) {

      inout_param[0] = rs.getShort(1);
    } else {

      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    ps.close();
    ps = null;
    stmt.close();
    stmt = null;
    con.close();
    con = null;
  }

  public static void Tinyint_Io_Max(short[] inout_param) throws SQLException {

    Connection con = DriverManager.getConnection("jdbc:default:connection");
    PreparedStatement ps = con
        .prepareStatement("update Tinyint_Tab set max_val =?");
    ps.setShort(1, inout_param[0]);
    ps.executeUpdate();

    Statement stmt = con.createStatement();
    ResultSet rs = stmt.executeQuery("Select max_val from Tinyint_Tab");

    if (rs.next()) {

      inout_param[0] = rs.getShort(1);
    } else {

      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    ps.close();
    ps = null;
    stmt.close();
    stmt = null;
    con.close();
    con = null;
  }

  public static void Tinyint_Io_Min(int[] inout_param) throws SQLException {

    Connection con = DriverManager.getConnection("jdbc:default:connection");
    PreparedStatement ps = con
        .prepareStatement("update Tinyint_Tab set min_val =?");
    ps.setInt(1, inout_param[0]);
    ps.executeUpdate();

    Statement stmt = con.createStatement();
    ResultSet rs = stmt.executeQuery("Select min_val from Tinyint_Tab");

    if (rs.next()) {

      inout_param[0] = rs.getInt(1);
    } else {

      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    ps.close();
    ps = null;
    stmt.close();
    stmt = null;
    con.close();
    con = null;
  }

  public static void Tinyint_Io_Null(short[] inout_param) throws SQLException {

    Connection con = DriverManager.getConnection("jdbc:default:connection");
    PreparedStatement ps = con
        .prepareStatement("update Tinyint_Tab set null_val =?");
    ps.setShort(1, inout_param[0]);
    ps.executeUpdate();

    Statement stmt = con.createStatement();
    ResultSet rs = stmt.executeQuery("Select null_val from Tinyint_Tab");

    if (rs.next()) {

      inout_param[0] = rs.getShort(1);
    } else {

      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    ps.close();
    ps = null;
    stmt.close();
    stmt = null;
    con.close();
    con = null;
  }

  public static void Integer_Io_Max(int[] max_param) throws SQLException {

    Connection con = DriverManager.getConnection("jdbc:default:connection");
    PreparedStatement ps = con
        .prepareStatement("update Integer_Tab set MAX_VAL=?");
    ps.setInt(1, max_param[0]);
    ps.executeUpdate();

    Statement stmt = con.createStatement();
    ResultSet rs = stmt.executeQuery("select max_val from Integer_Tab");
    if (rs.next()) {

      max_param[0] = rs.getInt(1);
    } else {

      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    ps.close();
    ps = null;
    stmt.close();
    stmt = null;
    con.close();
    con = null;
  }

  public static void Integer_Io_Min(int[] min_param) throws SQLException {

    Connection con = DriverManager.getConnection("jdbc:default:connection");
    PreparedStatement ps = con
        .prepareStatement("update Integer_Tab set MIN_VAL=?");
    ps.setInt(1, min_param[0]);
    ps.executeUpdate();

    Statement stmt = con.createStatement();
    ResultSet rs = stmt.executeQuery("select min_val from Integer_Tab");
    if (rs.next()) {

      min_param[0] = rs.getInt(1);
    } else {

      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    ps.close();
    ps = null;
    stmt.close();
    stmt = null;
    con.close();
    con = null;
  }

  public static void Integer_Io_Null(int[] null_param) throws SQLException {

    Connection con = DriverManager.getConnection("jdbc:default:connection");
    PreparedStatement ps = con
        .prepareStatement("update Integer_Tab set NULL_VAL=?");
    ps.setInt(1, null_param[0]);
    ps.executeUpdate();

    Statement stmt = con.createStatement();
    ResultSet rs = stmt.executeQuery("select null_val from Integer_Tab");
    if (rs.next()) {

      null_param[0] = rs.getInt(1);
    } else {

      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    ps.close();
    ps = null;
    stmt.close();
    stmt = null;
    con.close();
    con = null;
  }

  public static void Bigint_Io_Max(long[] max_param) throws SQLException {

    Connection con = DriverManager.getConnection("jdbc:default:connection");
    PreparedStatement ps = con
        .prepareStatement("update Bigint_Tab set MAX_VAL=?");
    ps.setLong(1, max_param[0]);
    ps.executeUpdate();

    Statement stmt = con.createStatement();
    ResultSet rs = stmt.executeQuery("select max_val from Bigint_Tab");
    if (rs.next()) {

      max_param[0] = rs.getLong(1);
    } else {

      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    ps.close();
    ps = null;
    stmt.close();
    stmt = null;
    con.close();
    con = null;
  }

  public static void Bigint_Io_Min(long[] min_param) throws SQLException {

    Connection con = DriverManager.getConnection("jdbc:default:connection");
    PreparedStatement ps = con
        .prepareStatement("update Bigint_Tab set MIN_VAL=?");
    ps.setLong(1, min_param[0]);
    ps.executeUpdate();

    Statement stmt = con.createStatement();
    ResultSet rs = stmt.executeQuery("select min_val from Bigint_Tab");
    if (rs.next()) {

      min_param[0] = rs.getLong(1);
    } else {

      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    ps.close();
    ps = null;
    stmt.close();
    stmt = null;
    con.close();
    con = null;
  }

  public static void Bigint_Io_Null(long[] null_param) throws SQLException {

    Connection con = DriverManager.getConnection("jdbc:default:connection");
    PreparedStatement ps = con
        .prepareStatement("update Bigint_Tab set NULL_VAL=?");
    ps.setLong(1, null_param[0]);
    ps.executeUpdate();

    Statement stmt = con.createStatement();
    ResultSet rs = stmt.executeQuery("select null_val from Bigint_Tab");
    if (rs.next()) {

      null_param[0] = rs.getLong(1);
    } else {

      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    ps.close();
    ps = null;
    stmt.close();
    stmt = null;
    con.close();
    con = null;
  }

  public static void Char_Io_Name(String[] name_param) throws SQLException {

    Connection con = DriverManager.getConnection("jdbc:default:connection");
    PreparedStatement ps = con
        .prepareStatement("update char_Tab set coffee_name=?");
    ps.setString(1, name_param[0]);
    ps.executeUpdate();

    Statement stmt = con.createStatement();
    ResultSet rs = stmt.executeQuery("select coffee_name from char_Tab");
    if (rs.next()) {

      name_param[0] = rs.getString(1);
    } else {

      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    ps.close();
    ps = null;
    stmt.close();
    stmt = null;
    con.close();
    con = null;
  }

  public static void Char_Io_Null(String[] null_param) throws SQLException {

    Connection con = DriverManager.getConnection("jdbc:default:connection");
    PreparedStatement ps = con
        .prepareStatement("update char_Tab set null_val=?");
    ps.setString(1, null_param[0]);
    ps.executeUpdate();

    Statement stmt = con.createStatement();
    ResultSet rs = stmt.executeQuery("select null_val from char_Tab");
    if (rs.next()) {

      null_param[0] = rs.getString(1);
    } else {

      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    ps.close();
    ps = null;
    stmt.close();
    stmt = null;
    con.close();
    con = null;
  }

  public static void Varchar_Io_Name(String[] name_param) throws SQLException {

    Connection con = DriverManager.getConnection("jdbc:default:connection");
    PreparedStatement ps = con
        .prepareStatement("update varchar_Tab set coffee_name=?");
    ps.setString(1, name_param[0]);
    ps.executeUpdate();

    Statement stmt = con.createStatement();
    ResultSet rs = stmt.executeQuery("select coffee_name from varchar_Tab");
    if (rs.next()) {

      name_param[0] = rs.getString(1);
    } else {

      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    ps.close();
    ps = null;
    stmt.close();
    stmt = null;
    con.close();
    con = null;
  }

  public static void Varchar_Io_Null(String[] null_param) throws SQLException {

    Connection con = DriverManager.getConnection("jdbc:default:connection");
    PreparedStatement ps = con
        .prepareStatement("update varchar_Tab set null_val=?");
    ps.setString(1, null_param[0]);
    ps.executeUpdate();

    Statement stmt = con.createStatement();
    ResultSet rs = stmt.executeQuery("select null_val from varchar_Tab");
    if (rs.next()) {

      null_param[0] = rs.getString(1);
    } else {

      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    ps.close();
    ps = null;
    stmt.close();
    stmt = null;
    con.close();
    con = null;
  }

  public static void Longvarchar_Io_Name(String[] name_param)
      throws SQLException {

    Connection con = DriverManager.getConnection("jdbc:default:connection");
    PreparedStatement ps = con
        .prepareStatement("update Longvarchar_Tab set coffee_name=?");
    ps.setString(1, name_param[0]);
    ps.executeUpdate();

    Statement stmt = con.createStatement();
    ResultSet rs = stmt.executeQuery("select coffee_name from Longvarchar_Tab");
    if (rs.next()) {

      name_param[0] = rs.getString(1);
    } else {

      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    ps.close();
    ps = null;
    stmt.close();
    stmt = null;
    con.close();
    con = null;
  }

  public static void Longvarchar_Io_Null(String[] name_param)
      throws SQLException {

    Connection con = DriverManager.getConnection("jdbc:default:connection");
    PreparedStatement ps = con
        .prepareStatement("update Longvarcharnull_Tab set null_val=?");
    ps.setString(1, name_param[0]);
    ps.executeUpdate();

    Statement stmt = con.createStatement();
    ResultSet rs = stmt
        .executeQuery("select null_val from Longvarcharnull_Tab");
    if (rs.next()) {

      name_param[0] = rs.getString(1);
    } else {

      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    ps.close();
    ps = null;
    stmt.close();
    stmt = null;
    con.close();
    con = null;
  }

  public static void Date_Io_Mfg(Date[] mfg_param) throws SQLException {

    Connection con = DriverManager.getConnection("jdbc:default:connection");
    PreparedStatement ps = con
        .prepareStatement("update Date_Tab set mfg_date=?");
    ps.setDate(1, mfg_param[0]);
    ps.executeUpdate();

    Statement stmt = con.createStatement();
    ResultSet rs = stmt.executeQuery("select mfg_date from Date_Tab");
    if (rs.next()) {

      mfg_param[0] = rs.getDate(1);
    } else {

      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    ps.close();
    ps = null;
    stmt.close();
    stmt = null;
    con.close();
    con = null;
  }

  public static void Date_Io_Null(Date[] null_param) throws SQLException {

    Connection con = DriverManager.getConnection("jdbc:default:connection");
    PreparedStatement ps = con
        .prepareStatement("update Date_Tab set null_val=?");
    ps.setDate(1, null_param[0]);
    ps.executeUpdate();

    Statement stmt = con.createStatement();
    ResultSet rs = stmt.executeQuery("select null_val from Date_Tab");
    if (rs.next()) {

      null_param[0] = rs.getDate(1);
    } else {

      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    ps.close();
    ps = null;
    stmt.close();
    stmt = null;
    con.close();
    con = null;
  }

  public static void Time_Io_Brk(Time[] brk_param) throws SQLException {

    Connection con = DriverManager.getConnection("jdbc:default:connection");
    PreparedStatement ps = con
        .prepareStatement("update Time_Tab set brk_time=?");
    ps.setTime(1, brk_param[0]);
    ps.executeUpdate();

    Statement stmt = con.createStatement();
    ResultSet rs = stmt.executeQuery("select brk_time from Time_Tab");
    if (rs.next()) {

      brk_param[0] = rs.getTime(1);

    } else {

      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    ps.close();
    ps = null;
    stmt.close();
    stmt = null;
    con.close();
    con = null;
  }

  public static void Time_Io_Null(Time[] null_param) throws SQLException {

    Connection con = DriverManager.getConnection("jdbc:default:connection");
    PreparedStatement ps = con
        .prepareStatement("update Time_Tab set null_val=?");
    ps.setTime(1, null_param[0]);
    ps.executeUpdate();

    Statement stmt = con.createStatement();

    ResultSet rs = stmt.executeQuery("select null_val from Time_Tab");
    if (rs.next()) {

      null_param[0] = rs.getTime(1);
    } else {

      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    ps.close();
    ps = null;
    stmt.close();
    stmt = null;
    con.close();
    con = null;
  }

  public static void Timestamp_Io_Intime(Timestamp[] intime_param)
      throws SQLException {

    Connection con = DriverManager.getConnection("jdbc:default:connection");
    PreparedStatement ps = con
        .prepareStatement("update Timestamp_Tab set in_time=?");
    ps.setTimestamp(1, intime_param[0]);
    ps.executeUpdate();

    Statement stmt = con.createStatement();
    ResultSet rs = stmt.executeQuery("select in_time from Timestamp_Tab");
    if (rs.next()) {

      intime_param[0] = rs.getTimestamp(1);
    } else {

      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    ps.close();
    ps = null;
    stmt.close();
    stmt = null;
    con.close();
    con = null;
  }

  public static void Timestamp_Io_Null(Timestamp[] null_param)
      throws SQLException {

    Connection con = DriverManager.getConnection("jdbc:default:connection");
    PreparedStatement ps = con
        .prepareStatement("update Timestamp_Tab set null_val=?");
    ps.setTimestamp(1, null_param[0]);
    ps.executeUpdate();

    Statement stmt = con.createStatement();
    ResultSet rs = stmt.executeQuery("select null_val from Timestamp_Tab");

    if (rs.next()) {

      null_param[0] = rs.getTimestamp(1);
    } else {

      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    ps.close();
    ps = null;
    stmt.close();
    stmt = null;
    con.close();
    con = null;
  }

  public static void Binary_Proc_Io(byte[][] binary_param) throws SQLException {

    Connection con = DriverManager.getConnection("jdbc:default:connection");
    PreparedStatement ps = con
        .prepareStatement("update Binary_Tab set binary_val=?");
    ps.setBytes(1, binary_param[0]);
    ps.executeUpdate();

    Statement stmt = con.createStatement();
    ResultSet rs = stmt.executeQuery("select binary_val from Binary_Tab");
    if (rs.next()) {

      binary_param[0] = rs.getBytes(1);
    } else {

      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    ps.close();
    ps = null;
    stmt.close();
    stmt = null;
    con.close();
    con = null;
  }

  public static void Varbinary_Proc_Io(byte[][] varbinary_param)
      throws SQLException {

    Connection con = DriverManager.getConnection("jdbc:default:connection");
    PreparedStatement ps = con
        .prepareStatement("update Varbinary_Tab set varbinary_val=?");
    ps.setBytes(1, varbinary_param[0]);
    ps.executeUpdate();

    Statement stmt = con.createStatement();
    ResultSet rs = stmt.executeQuery("select varbinary_val from Varbinary_Tab");
    if (rs.next()) {

      varbinary_param[0] = rs.getBytes(1);
    } else {

      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    ps.close();
    ps = null;
    stmt.close();
    stmt = null;
    con.close();
    con = null;
  }

  public static void Longvarbinary_Io(byte[][] longvarbinary_param)
      throws SQLException {

    Connection con = DriverManager.getConnection("jdbc:default:connection");
    PreparedStatement ps = con
        .prepareStatement("update Longvarbinary_Tab set Longvarbinary_val=?");
    ps.setBytes(1, longvarbinary_param[0]);
    ps.executeUpdate();

    Statement stmt = con.createStatement();
    ResultSet rs = stmt
        .executeQuery("select longvarbinary_val from Longvarbinary_Tab");
    if (rs.next()) {

      longvarbinary_param[0] = rs.getBytes(1);
    } else {

      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    ps.close();
    ps = null;
    stmt.close();
    stmt = null;
    con.close();
    con = null;
  }

  /*****************
   * Defining methods for CallableStatement IN parameter
   *******************/

  public static void Numeric_In_Max(BigDecimal max_param) throws SQLException {

    Connection con = DriverManager.getConnection("jdbc:default:connection");
    PreparedStatement ps = con
        .prepareStatement("update Numeric_Tab set MAX_VAL=?");

    ps.setBigDecimal(1, max_param);
    ps.executeUpdate();

    ps.close();
    ps = null;
    con.close();
    con = null;
  }

  public static void Numeric_In_Min(BigDecimal min_param) throws SQLException {

    Connection con = DriverManager.getConnection("jdbc:default:connection");
    PreparedStatement ps = con
        .prepareStatement("update Numeric_Tab set MIN_VAL=?");

    ps.setBigDecimal(1, min_param);
    ps.executeUpdate();

    ps.close();
    ps = null;
    con.close();
    con = null;
  }

  public static void Numeric_In_Null(BigDecimal null_param)
      throws SQLException {

    Connection con = DriverManager.getConnection("jdbc:default:connection");
    PreparedStatement ps = con
        .prepareStatement("update Numeric_Tab set NULL_VAL=?");

    ps.setBigDecimal(1, null_param);
    ps.executeUpdate();

    ps.close();
    ps = null;
    con.close();
    con = null;
  }

  public static void Decimal_In_Max(BigDecimal max_param) throws SQLException {

    Connection con = DriverManager.getConnection("jdbc:default:connection");
    PreparedStatement ps = con
        .prepareStatement("update Decimal_Tab set MAX_VAL=?");

    ps.setBigDecimal(1, max_param);
    ps.executeUpdate();

    ps.close();
    ps = null;
    con.close();
    con = null;
  }

  public static void Decimal_In_Min(BigDecimal min_param) throws SQLException {

    Connection con = DriverManager.getConnection("jdbc:default:connection");
    PreparedStatement ps = con
        .prepareStatement("update Decimal_Tab set MIN_VAL=?");

    ps.setBigDecimal(1, min_param);
    ps.executeUpdate();

    ps.close();
    ps = null;
    con.close();
    con = null;
  }

  public static void Decimal_In_Null(BigDecimal null_param)
      throws SQLException {

    Connection con = DriverManager.getConnection("jdbc:default:connection");
    PreparedStatement ps = con
        .prepareStatement("update Decimal_Tab set NULL_VAL=?");

    ps.setBigDecimal(1, null_param);
    ps.executeUpdate();

    ps.close();
    ps = null;
    con.close();
    con = null;
  }

  public static void Double_In_Max(double max_param) throws SQLException {

    Connection con = DriverManager.getConnection("jdbc:default:connection");
    PreparedStatement ps = con
        .prepareStatement("update Double_Tab set MAX_VAL=?");

    ps.setDouble(1, max_param);
    ps.executeUpdate();

    ps.close();
    ps = null;
    con.close();
    con = null;
  }

  public static void Double_In_Min(double min_param) throws SQLException {

    Connection con = DriverManager.getConnection("jdbc:default:connection");
    PreparedStatement ps = con
        .prepareStatement("update Double_Tab set MIN_VAL=?");

    ps.setDouble(1, min_param);
    ps.executeUpdate();

    ps.close();
    ps = null;
    con.close();
    con = null;
  }

  public static void Double_In_Null(double null_param) throws SQLException {

    Connection con = DriverManager.getConnection("jdbc:default:connection");
    PreparedStatement ps = con
        .prepareStatement("update Double_Tab set NULL_VAL=?");

    ps.setDouble(1, null_param);
    ps.executeUpdate();

    ps.close();
    ps = null;
    con.close();
    con = null;
  }

  public static void Float_In_Max(double max_param) throws SQLException {

    Connection con = DriverManager.getConnection("jdbc:default:connection");
    PreparedStatement ps = con
        .prepareStatement("update Float_Tab set MAX_VAL=?");

    ps.setDouble(1, max_param);
    ps.executeUpdate();

    ps.close();
    ps = null;
    con.close();
    con = null;
  }

  public static void Float_In_Min(double min_param) throws SQLException {

    Connection con = DriverManager.getConnection("jdbc:default:connection");
    PreparedStatement ps = con
        .prepareStatement("update Float_Tab set MIN_VAL=?");

    ps.setDouble(1, min_param);
    ps.executeUpdate();

    ps.close();
    ps = null;
    con.close();
    con = null;
  }

  public static void Float_In_Null(double null_param) throws SQLException {

    Connection con = DriverManager.getConnection("jdbc:default:connection");
    PreparedStatement ps = con
        .prepareStatement("update Float_Tab set NULL_VAL=?");

    ps.setDouble(1, null_param);
    ps.executeUpdate();

    ps.close();
    ps = null;
    con.close();
    con = null;
  }

  public static void Real_In_Max(float max_param) throws SQLException {

    Connection con = DriverManager.getConnection("jdbc:default:connection");
    PreparedStatement ps = con
        .prepareStatement("update Real_Tab set MAX_VAL=?");

    ps.setFloat(1, max_param);
    ps.executeUpdate();

    ps.close();
    ps = null;
    con.close();
    con = null;
  }

  public static void Real_In_Min(float min_param) throws SQLException {

    Connection con = DriverManager.getConnection("jdbc:default:connection");
    PreparedStatement ps = con
        .prepareStatement("update Real_Tab set MIN_VAL=?");

    ps.setFloat(1, min_param);
    ps.executeUpdate();

    ps.close();
    ps = null;
    con.close();
    con = null;
  }

  public static void Real_In_Null(float null_param) throws SQLException {

    Connection con = DriverManager.getConnection("jdbc:default:connection");
    PreparedStatement ps = con
        .prepareStatement("update Real_Tab set NULL_VAL=?");

    ps.setFloat(1, null_param);
    ps.executeUpdate();

    ps.close();
    ps = null;
    con.close();
    con = null;
  }

  public static void Bit_In_Max(boolean max_param) throws SQLException {

    Connection con = DriverManager.getConnection("jdbc:default:connection");
    PreparedStatement ps = con.prepareStatement("update Bit_Tab set MAX_VAL=?");

    ps.setBoolean(1, max_param);
    ps.executeUpdate();

    ps.close();
    ps = null;
    con.close();
    con = null;
  }

  public static void Bit_In_Min(boolean min_param) throws SQLException {

    Connection con = DriverManager.getConnection("jdbc:default:connection");
    PreparedStatement ps = con.prepareStatement("update Bit_Tab set MIN_VAL=?");

    ps.setBoolean(1, min_param);
    ps.executeUpdate();

    ps.close();
    ps = null;
    con.close();
    con = null;
  }

  public static void Bit_In_Null(boolean null_param) throws SQLException {

    Connection con = DriverManager.getConnection("jdbc:default:connection");
    PreparedStatement ps = con
        .prepareStatement("update Bit_Tab set NULL_VAL=?");

    ps.setBoolean(1, null_param);
    ps.executeUpdate();

    ps.close();
    ps = null;
    con.close();
    con = null;
  }

  public static void Smallint_In_Max(short max_param) throws SQLException {

    Connection con = DriverManager.getConnection("jdbc:default:connection");
    PreparedStatement ps = con
        .prepareStatement("update Smallint_Tab set MAX_VAL=?");

    ps.setShort(1, max_param);
    ps.executeUpdate();

    ps.close();
    ps = null;
    con.close();
    con = null;
  }

  public static void Smallint_In_Min(short min_param) throws SQLException {

    Connection con = DriverManager.getConnection("jdbc:default:connection");
    PreparedStatement ps = con
        .prepareStatement("update Smallint_Tab set MIN_VAL=?");

    ps.setShort(1, min_param);
    ps.executeUpdate();

    ps.close();
    ps = null;
    con.close();
    con = null;
  }

  public static void Smallint_In_Null(short null_param) throws SQLException {

    Connection con = DriverManager.getConnection("jdbc:default:connection");
    PreparedStatement ps = con
        .prepareStatement("update Smallint_Tab set NULL_VAL=?");

    ps.setShort(1, null_param);
    ps.executeUpdate();

    ps.close();
    ps = null;
    con.close();
    con = null;
  }

  public static void Tinyint_In_Max(int in_param) throws SQLException {

    Connection con = DriverManager.getConnection("jdbc:default:connection");
    PreparedStatement ps = con
        .prepareStatement("update Tinyint_Tab set MAX_VAL=?");

    ps.setInt(1, in_param);
    ps.executeUpdate();

    ps.close();
    ps = null;
    con.close();
    con = null;
  }

  public static void Tinyint_In_Min(int in_param) throws SQLException {

    Connection con = DriverManager.getConnection("jdbc:default:connection");
    PreparedStatement ps = con
        .prepareStatement("update Tinyint_Tab set MIN_VAL=?");

    ps.setInt(1, in_param);
    ps.executeUpdate();

    ps.close();
    ps = null;
    con.close();
    con = null;
  }

  public static void Tinyint_In_Null(int in_param) throws SQLException {

    Connection con = DriverManager.getConnection("jdbc:default:connection");
    PreparedStatement ps = con
        .prepareStatement("update Tinyint_Tab set NULL_VAL=?");

    ps.setInt(1, in_param);
    ps.executeUpdate();

    ps.close();
    ps = null;
    con.close();
    con = null;
  }

  public static void Integer_In_Max(int in_param) throws SQLException {

    Connection con = DriverManager.getConnection("jdbc:default:connection");
    PreparedStatement ps = con
        .prepareStatement("update Integer_Tab set MAX_VAL=?");

    ps.setInt(1, in_param);
    ps.executeUpdate();

    ps.close();
    ps = null;
    con.close();
    con = null;
  }

  public static void Integer_In_Min(int in_param) throws SQLException {

    Connection con = DriverManager.getConnection("jdbc:default:connection");
    PreparedStatement ps = con
        .prepareStatement("update Integer_Tab set MIN_VAL=?");

    ps.setInt(1, in_param);
    ps.executeUpdate();

    ps.close();
    ps = null;
    con.close();
    con = null;
  }

  public static void Integer_In_Null(int in_param) throws SQLException {

    Connection con = DriverManager.getConnection("jdbc:default:connection");
    PreparedStatement ps = con
        .prepareStatement("update Integer_Tab set NULL_VAL=?");

    ps.setInt(1, in_param);
    ps.executeUpdate();

    ps.close();
    ps = null;
    con.close();
    con = null;
  }

  public static void Bigint_In_Max(long in_param) throws SQLException {

    Connection con = DriverManager.getConnection("jdbc:default:connection");
    PreparedStatement ps = con
        .prepareStatement("update Bigint_Tab set MAX_VAL=?");

    ps.setLong(1, in_param);
    ps.executeUpdate();

    ps.close();
    ps = null;
    con.close();
    con = null;
  }

  public static void Bigint_In_Min(long in_param) throws SQLException {

    Connection con = DriverManager.getConnection("jdbc:default:connection");
    PreparedStatement ps = con
        .prepareStatement("update Bigint_Tab set MIN_VAL=?");

    ps.setLong(1, in_param);
    ps.executeUpdate();

    ps.close();
    ps = null;
    con.close();
    con = null;
  }

  public static void Bigint_In_Null(long in_param) throws SQLException {

    Connection con = DriverManager.getConnection("jdbc:default:connection");
    PreparedStatement ps = con
        .prepareStatement("update Bigint_Tab set NULL_VAL=?");

    ps.setLong(1, in_param);
    ps.executeUpdate();

    ps.close();
    ps = null;
    con.close();
    con = null;
  }

  public static void Char_In_Name(String in_param) throws SQLException {

    Connection con = DriverManager.getConnection("jdbc:default:connection");
    PreparedStatement ps = con
        .prepareStatement("update Char_Tab set coffee_name=?");

    ps.setString(1, in_param);
    ps.executeUpdate();

    ps.close();
    ps = null;
    con.close();
    con = null;
  }

  public static void Char_In_Null(String in_param) throws SQLException {

    Connection con = DriverManager.getConnection("jdbc:default:connection");
    PreparedStatement ps = con
        .prepareStatement("update Char_Tab set NULL_VAL=?");

    ps.setString(1, in_param);
    ps.executeUpdate();

    ps.close();
    ps = null;
    con.close();
    con = null;
  }

  public static void Varchar_In_Name(String in_param) throws SQLException {

    Connection con = DriverManager.getConnection("jdbc:default:connection");
    PreparedStatement ps = con
        .prepareStatement("update Varchar_Tab set coffee_name=?");

    ps.setString(1, in_param);
    ps.executeUpdate();

    ps.close();
    ps = null;
    con.close();
    con = null;
  }

  public static void Varchar_In_Null(String in_param) throws SQLException {

    Connection con = DriverManager.getConnection("jdbc:default:connection");
    PreparedStatement ps = con
        .prepareStatement("update Varchar_Tab set NULL_VAL=?");

    ps.setString(1, in_param);
    ps.executeUpdate();

    ps.close();
    ps = null;
    con.close();
    con = null;
  }

  public static void Longvarchar_In_Name(String in_param) throws SQLException {

    Connection con = DriverManager.getConnection("jdbc:default:connection");
    PreparedStatement ps = con
        .prepareStatement("update Longvarchar_Tab set coffee_name=?");

    ps.setString(1, in_param);
    ps.executeUpdate();

    ps.close();
    ps = null;
    con.close();
    con = null;
  }

  public static void Longvarchar_In_Null(String in_param) throws SQLException {

    Connection con = DriverManager.getConnection("jdbc:default:connection");
    PreparedStatement ps = con
        .prepareStatement("update Longvarcharnull_Tab set NULL_VAL=?");

    ps.setString(1, in_param);
    ps.executeUpdate();

    ps.close();
    ps = null;
    con.close();
    con = null;
  }

  public static void Date_In_Mfg(java.sql.Date in_param) throws SQLException {

    Connection con = DriverManager.getConnection("jdbc:default:connection");
    PreparedStatement ps = con
        .prepareStatement("update Date_Tab set mfg_date=?");

    ps.setDate(1, in_param);
    ps.executeUpdate();

    ps.close();
    ps = null;
    con.close();
    con = null;
  }

  public static void Date_In_Null(java.sql.Date in_param) throws SQLException {

    Connection con = DriverManager.getConnection("jdbc:default:connection");
    PreparedStatement ps = con
        .prepareStatement("update Date_Tab set NULL_VAL=?");

    ps.setDate(1, in_param);
    ps.executeUpdate();

    ps.close();
    ps = null;
    con.close();
    con = null;
  }

  public static void Time_In_Brk(Time in_param) throws SQLException {

    Connection con = DriverManager.getConnection("jdbc:default:connection");
    PreparedStatement ps = con
        .prepareStatement("update Time_Tab set BRK_TIME=?");

    ps.setTime(1, in_param);
    ps.executeUpdate();

    ps.close();
    ps = null;
    con.close();
    con = null;
  }

  public static void Time_In_Null(Time in_param) throws SQLException {

    Connection con = DriverManager.getConnection("jdbc:default:connection");
    PreparedStatement ps = con
        .prepareStatement("update Time_Tab set NULL_VAL=?");

    ps.setTime(1, in_param);
    ps.executeUpdate();

    ps.close();
    ps = null;
    con.close();
    con = null;
  }

  public static void Timestamp_In_Intime(Timestamp in_param)
      throws SQLException {

    Connection con = DriverManager.getConnection("jdbc:default:connection");
    PreparedStatement ps = con
        .prepareStatement("update Timestamp_Tab set IN_TIME=?");

    ps.setTimestamp(1, in_param);
    ps.executeUpdate();

    ps.close();
    ps = null;
    con.close();
    con = null;
  }

  public static void Timestamp_In_Null(Timestamp in_param) throws SQLException {

    Connection con = DriverManager.getConnection("jdbc:default:connection");
    PreparedStatement ps = con
        .prepareStatement("update Timestamp_Tab set NULL_VAL=?");

    ps.setTimestamp(1, in_param);
    ps.executeUpdate();

    ps.close();
    ps = null;
    con.close();
    con = null;
  }

  public static void Binary_Proc_In(byte[] in_param) throws SQLException {

    Connection con = DriverManager.getConnection("jdbc:default:connection");
    PreparedStatement ps = con
        .prepareStatement("update Binary_Tab set BINARY_VAL=?");

    ps.setBytes(1, in_param);
    ps.executeUpdate();

    ps.close();
    ps = null;
    con.close();
    con = null;
  }

  public static void Varbinary_Proc_In(byte[] in_param) throws SQLException {

    Connection con = DriverManager.getConnection("jdbc:default:connection");
    PreparedStatement ps = con
        .prepareStatement("update Varbinary_Tab set VARBINARY_VAL=?");

    ps.setBytes(1, in_param);
    ps.executeUpdate();

    ps.close();
    ps = null;
    con.close();
    con = null;
  }

  public static void Longvarbinary_Proc_In(byte[] in_param)
      throws SQLException {

    Connection con = DriverManager.getConnection("jdbc:default:connection");
    PreparedStatement ps = con
        .prepareStatement("update Longvarbinary_Tab set LONGVARBINARY_VAL=?");

    ps.setBytes(1, in_param);
    ps.executeUpdate();

    ps.close();
    ps = null;
    con.close();
    con = null;
  }

}
