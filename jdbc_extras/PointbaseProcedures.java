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
import com.pointbase.jdbc.*;

/**
 * Stored procedures for output parameters.
 * 
 * Note that if a null might occur, the Java class form is used rather than the
 * Java built-in form. i.e., Integer instead of int. This is so that the null
 * can be transmitted back to the caller. If we know it won't be null, we just
 * use the built-in type directly.
 * 
 **/

public class PointbaseProcedures {

  private Connection con = null;

  public PointbaseProcedures(Connection p_conn) throws SQLException {
    con = p_conn;
  }

  /*****************
   * Defining methods for CallableStatement OUT parameter
   *******************/

  public void Numeric_Proc(jdbcInOutBigDecimalWrapper max_param,
      jdbcInOutBigDecimalWrapper min_param,
      jdbcInOutBigDecimalWrapper null_param) throws SQLException {

    // System.out.println("Inside Numeric_Proc");
    Statement stmt = con.createStatement();
    ResultSet rs = stmt
        .executeQuery("select max_val, min_val, null_val from numeric_tab");

    if (rs.next()) {

      max_param.set(rs.getBigDecimal(1));
      min_param.set(rs.getBigDecimal(2));
      null_param.set(rs.getBigDecimal(3));
      if (rs.wasNull())
        null_param.set(null);
    } else {

      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    stmt.close();
    stmt = null;
    // System.out.println("Exiting Numeric_Proc");

  }

  public void Decimal_Proc(jdbcInOutBigDecimalWrapper max_param,
      jdbcInOutBigDecimalWrapper min_param,
      jdbcInOutBigDecimalWrapper null_param) throws SQLException {

    Statement stmt = con.createStatement();
    ResultSet rs = stmt
        .executeQuery("select max_val,min_val,null_val from Decimal_Tab");

    if (rs.next()) {

      max_param.set(rs.getBigDecimal(1));
      min_param.set(rs.getBigDecimal(2));
      null_param.set(rs.getBigDecimal(3));
      if (rs.wasNull())
        null_param.set(null);
    } else {

      throw new SQLException("Data not found");
    }
    rs.close();
    rs = null;
    stmt.close();
    stmt = null;

  }

  public void Double_Proc(jdbcInOutDoubleWrapper max_param,
      jdbcInOutDoubleWrapper min_param, jdbcInOutDoubleWrapper null_param)
      throws SQLException {

    Statement stmt = con.createStatement();
    ResultSet rs = stmt
        .executeQuery("select max_val, min_val, null_val from double_tab");

    if (rs.next()) {

      max_param.set(rs.getDouble(1));
      min_param.set(rs.getDouble(2));
      null_param.set(rs.getDouble(3));
      if (rs.wasNull())
        null_param.set(null);
    } else {

      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    stmt.close();
    stmt = null;

  }

  public void Float_Proc(jdbcInOutDoubleWrapper max_param,
      jdbcInOutDoubleWrapper min_param, jdbcInOutDoubleWrapper null_param)
      throws SQLException {

    Statement stmt = con.createStatement();
    ResultSet rs = stmt
        .executeQuery("select max_val, min_val, null_val from float_tab");

    if (rs.next()) {

      max_param.set(rs.getDouble(1));
      min_param.set(rs.getDouble(2));
      null_param.set(rs.getDouble(3));
      if (rs.wasNull())
        null_param.set(null);
    } else {

      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    stmt.close();
    stmt = null;

  }

  public void Real_Proc(jdbcInOutFloatWrapper max_param,
      jdbcInOutFloatWrapper min_param, jdbcInOutFloatWrapper null_param)
      throws SQLException {

    Statement stmt = con.createStatement();
    ResultSet rs = stmt
        .executeQuery("select max_val,min_val,null_val from Real_Tab");

    if (rs.next()) {

      max_param.set(rs.getFloat(1));
      min_param.set(rs.getFloat(2));
      null_param.set(rs.getFloat(3));
      if (rs.wasNull())
        null_param.set(null);
    } else {

      throw new SQLException("Data not found");
    }
    rs.close();
    rs = null;
    stmt.close();
    stmt = null;

  }

  public void Bit_Proc(jdbcInOutBooleanWrapper max_param,
      jdbcInOutBooleanWrapper min_param, jdbcInOutBooleanWrapper null_param)
      throws SQLException {

    Statement stmt = con.createStatement();
    ResultSet rs = stmt
        .executeQuery("select max_val, min_val, null_val from bit_tab");

    if (rs.next()) {

      max_param.set(rs.getBoolean(1));
      min_param.set(rs.getBoolean(2));
      null_param.set(rs.getBoolean(3));
      if (rs.wasNull())
        null_param.set(null);
    } else {

      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    stmt.close();
    stmt = null;

  }

  public void Smallint_Proc(jdbcInOutShortWrapper max_param,
      jdbcInOutShortWrapper min_param, jdbcInOutShortWrapper null_param)
      throws SQLException {

    Statement stmt = con.createStatement();
    ResultSet rs = stmt
        .executeQuery("select max_val, min_val, null_val from smallint_tab");

    if (rs.next()) {

      max_param.set(rs.getShort(1));
      min_param.set(rs.getShort(2));
      null_param.set(rs.getShort(3));
      if (rs.wasNull())
        null_param.set(null);
    } else {
      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    stmt.close();
    stmt = null;

  }

  public void Tinyint_Proc(jdbcInOutShortWrapper max_param,
      jdbcInOutShortWrapper min_param, jdbcInOutShortWrapper null_param)
      throws SQLException {

    Statement stmt = con.createStatement();
    ResultSet rs = stmt
        .executeQuery("select max_val, min_val, null_val from tinyint_tab");

    if (rs.next()) {

      max_param.set(new Short(rs.getShort(1)));
      min_param.set(new Short(rs.getShort(2)));
      null_param.set(new Short(rs.getShort(3)));
      if (rs.wasNull())
        null_param.set(null);
    } else {

      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    stmt.close();
    stmt = null;

  }

  public void Integer_Proc(jdbcInOutIntWrapper max_param,
      jdbcInOutIntWrapper min_param, jdbcInOutIntWrapper null_param)
      throws SQLException {

    Statement stmt = con.createStatement();
    ResultSet rs = stmt
        .executeQuery("select max_val, min_val, null_val from integer_tab");

    if (rs.next()) {

      max_param.set(new Integer(rs.getInt(1)));
      min_param.set(new Integer(rs.getInt(2)));
      null_param.set(new Integer(rs.getInt(3)));
      if (rs.wasNull())
        null_param.set(null);
    } else {

      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    stmt.close();
    stmt = null;

  }

  public void Bigint_Proc(jdbcInOutLongWrapper max_param,
      jdbcInOutLongWrapper min_param, jdbcInOutLongWrapper null_param)
      throws SQLException {

    Statement stmt = con.createStatement();
    ResultSet rs = stmt
        .executeQuery("select max_val, min_val, null_val from bigint_tab");

    if (rs.next()) {

      String maxValue = rs.getString(1);
      // System.out.println("The Max Value is: " + maxValue);
      if (rs.wasNull())
        max_param.set(null);
      else
        max_param.set(new Long(maxValue));

      String minValue = rs.getString(2);
      // System.out.println("The Min Value is: " + minValue);
      if (rs.wasNull())
        min_param.set(null);
      else
        min_param.set(new Long(minValue));

      String nullValue = rs.getString(3);
      // System.out.println("The Null Value is: " + nullValue);
      if (rs.wasNull())
        null_param.set(null);
      else
        null_param.set(new Long(nullValue));

    } else {

      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    stmt.close();
    stmt = null;

  }

  public void Char_Proc(jdbcInOutStringWrapper coffee_param,
      jdbcInOutStringWrapper null_param) throws SQLException {

    Statement stmt = con.createStatement();
    ResultSet rs = stmt
        .executeQuery("select coffee_name, null_val from char_tab");

    if (rs.next()) {

      coffee_param.set(rs.getString(1));
      null_param.set(rs.getString(2));
      if (rs.wasNull())
        null_param.set(null);
    } else {

      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    stmt.close();
    stmt = null;

  }

  public void Varchar_Proc(jdbcInOutStringWrapper coffee_param,
      jdbcInOutStringWrapper null_param) throws SQLException {

    Statement stmt = con.createStatement();
    ResultSet rs = stmt
        .executeQuery("select coffee_name, null_val from varchar_tab");

    if (rs.next()) {

      coffee_param.set(rs.getString(1));
      null_param.set(rs.getString(2));
      if (rs.wasNull())
        null_param.set(null);
    } else {

      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    stmt.close();
    stmt = null;

  }

  public void Longvarchar_Proc(jdbcInOutStringWrapper coffee_param)
      throws SQLException {

    Statement stmt = con.createStatement();
    ResultSet rs = stmt.executeQuery("select coffee_name from longvarchar_tab");

    if (rs.next()) {

      coffee_param.set(rs.getString(1));
    } else {

      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    stmt.close();
    stmt = null;

  }

  public void Longvarcharnull_Proc(jdbcInOutStringWrapper null_param)
      throws SQLException {

    Statement stmt = con.createStatement();
    ResultSet rs = stmt
        .executeQuery("select null_val from longvarcharnull_tab");

    if (rs.next()) {

      null_param.set(rs.getString(1));
      if (rs.wasNull())
        null_param.set(null);
    } else {

      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    stmt.close();
    stmt = null;

  }

  public void Date_Proc(jdbcInOutDateWrapper mfg_param,
      jdbcInOutDateWrapper null_param) throws SQLException {

    Statement stmt = con.createStatement();
    ResultSet rs = stmt.executeQuery("select mfg_date, null_val from date_tab");

    if (rs.next()) {

      mfg_param.set(rs.getDate(1));
      null_param.set(rs.getDate(2));
      if (rs.wasNull())
        null_param.set(null);
    } else {

      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    stmt.close();
    stmt = null;

  }

  public void Time_Proc(jdbcInOutTimeWrapper brk_param,
      jdbcInOutTimeWrapper null_param) throws SQLException {

    Statement stmt = con.createStatement();
    ResultSet rs = stmt.executeQuery("select brk_time, null_val from time_tab");

    if (rs.next()) {

      brk_param.set(rs.getTime(1));
      null_param.set(rs.getTime(2));
      if (rs.wasNull())
        null_param.set(null);
    } else {

      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    stmt.close();
    stmt = null;

  }

  public void Timestamp_Proc(jdbcInOutTimestampWrapper in_param,
      jdbcInOutTimestampWrapper null_param) throws SQLException {

    Statement stmt = con.createStatement();
    ResultSet rs = stmt
        .executeQuery("select in_time, null_val from timestamp_tab");

    if (rs.next()) {

      in_param.set(rs.getTimestamp(1));
      null_param.set(rs.getTimestamp(2));
      if (rs.wasNull())
        null_param.set(null);
    } else {

      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    stmt.close();
    stmt = null;

  }

  public void Binary_Proc(jdbcInOutByteArrayWrapper binary_param)
      throws SQLException {

    Statement stmt = con.createStatement();
    ResultSet rs = stmt.executeQuery("select binary_val from binary_tab");

    if (rs.next()) {

      byte[] x = rs.getBytes(1);
      if (x == null) {
        System.out.println("***EMPTY RESULTSET*****");
        binary_param.set(null);
      } else {
        System.out.println("***GOT A RESULTSET*****");
        binary_param.set(rs.getBytes(1));
        System.out.println(
            "The Length of byte[] in Binary_Proc: " + binary_param.size());
      }

    } else {
      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    stmt.close();
    stmt = null;

  }

  public void Varbinary_Proc(jdbcInOutByteArrayWrapper varbinary_param)
      throws SQLException {

    Statement stmt = con.createStatement();
    ResultSet rs = stmt.executeQuery("select varbinary_val from varbinary_tab");

    if (rs.next()) {

      varbinary_param.set(rs.getBytes(1));

    } else {

      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    stmt.close();
    stmt = null;

  }

  public void Longvarbinary_Proc(jdbcInOutByteArrayWrapper longvarbinary_param)
      throws SQLException {

    Statement stmt = con.createStatement();
    ResultSet rs = stmt
        .executeQuery("select longvarbinary_val from longvarbinary_tab");

    if (rs.next()) {

      longvarbinary_param.set(rs.getBytes(1));

    } else {

      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    stmt.close();
    stmt = null;

  }

  public void Integer_In_Proc(Integer in_param) throws SQLException {

    PreparedStatement ps = con
        .prepareStatement("update Integer_Tab set MAX_VAL=?");

    ps.setInt(1, in_param.intValue());
    ps.executeUpdate();

    ps.close();
    ps = null;

  }

  public void Integer_InOut_Proc(jdbcInOutIntWrapper inout_param)
      throws SQLException {

    PreparedStatement ps = con
        .prepareStatement("select max_val from integer_Tab where min_val=?");

    ps.setInt(1, inout_param.get());
    ResultSet rs = ps.executeQuery();

    if (rs.next()) {

      inout_param.set(rs.getInt(1));
    } else {

      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    ps.close();
    ps = null;

  }

  public void UpdCoffee_Proc(BigDecimal type_param) throws SQLException {

    PreparedStatement ps = con.prepareStatement(
        "update ctstable2 set PRICE=PRICE*20 where TYPE_ID=?");

    ps.setBigDecimal(1, type_param);
    ps.executeUpdate();

    ps.close();
    ps = null;

  }

  public void UpdCoffee_Proc(int type_param) throws SQLException {

    PreparedStatement ps = con.prepareStatement(
        "update ctstable2 set PRICE=PRICE*20 where TYPE_ID=?");

    ps.setInt(1, type_param);
    ps.executeUpdate();

    ps.close();
    ps = null;

  }

  public void UpdCoffee_Proc(String type_param) throws SQLException {
    PreparedStatement preparedstatement = con.prepareStatement(
        "update ctstable2 set PRICE=PRICE*20 where TYPE_ID=?");
    preparedstatement.setString(1, type_param);
    preparedstatement.executeUpdate();
    preparedstatement.close();
    preparedstatement = null;
  }

  public void SelCoffee_Proc(jdbcInOutStringWrapper keyid_param)
      throws SQLException {

    Statement stmt = con.createStatement();
    ResultSet rs = stmt
        .executeQuery("select KEY_ID from CTSTABLE2 where TYPE_ID=1");

    if (rs.next()) {

      keyid_param.set(rs.getString(1));
    } else {

      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    stmt.close();
    stmt = null;

  }

  public void IOCoffee_Proc(jdbcInOutDoubleWrapper price_param)
      throws SQLException {

    PreparedStatement ps = con
        .prepareStatement("select price*2 from CTSTABLE2 where price=?");

    if (price_param.isNull() == true)
      ps.setNull(1, Types.FLOAT);
    else
      ps.setFloat(1, (float) price_param.get());

    ResultSet rs = ps.executeQuery();
    if (rs.next()) {

      price_param.set(new Double(rs.getDouble(1)));
      if (rs.wasNull())
        price_param.set(null);
    } else {

      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    ps.close();
    ps = null;

  }

  public void Coffee_Proc(BigDecimal bigdecimal) throws SQLException {
    PreparedStatement preparedstatement = con
        .prepareStatement("update CTSTABLE2 set PRICE=Price*2 where TYPE_ID=?");
    preparedstatement.setBigDecimal(1, bigdecimal);
    preparedstatement.executeUpdate();
    preparedstatement.close();
    preparedstatement = null;
    PreparedStatement preparedstatement1 = con
        .prepareStatement("delete from CTSTABLE2 where TYPE_ID=?-1");
    preparedstatement1.setBigDecimal(1, bigdecimal);
    preparedstatement1.executeUpdate();
    preparedstatement1.close();
    preparedstatement1 = null;
  }

  public void Coffee_Proc(int type_param) throws SQLException {

    PreparedStatement ps1, ps2;

    ps1 = con
        .prepareStatement("update CTSTABLE2 set PRICE=Price*2 where TYPE_ID=?");
    ps1.setInt(1, type_param);
    ps1.executeUpdate();
    ps1.close();
    ps1 = null;

    ps2 = con.prepareStatement("delete from CTSTABLE2 where TYPE_ID=?-1");
    ps2.setInt(1, type_param);
    ps2.executeUpdate();
    ps2.close();
    ps2 = null;

  }

  /****************
   * Defining methods for CallableStatement INOUT parameter
   ******************/

  public void Numeric_Io_Max(jdbcInOutBigDecimalWrapper inout_param)
      throws SQLException {

    PreparedStatement ps = con
        .prepareStatement("update Numeric_Tab set max_val =?");
    ps.setBigDecimal(1, inout_param.get());
    ps.executeUpdate();

    Statement stmt = con.createStatement();
    ResultSet rs = stmt.executeQuery("Select max_val from Numeric_Tab");

    if (rs.next()) {

      inout_param.set(rs.getBigDecimal(1));
    } else {

      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    ps.close();
    ps = null;
    stmt.close();
    stmt = null;

  }

  public void Numeric_Io_Min(jdbcInOutBigDecimalWrapper inout_param)
      throws SQLException {

    PreparedStatement ps = con
        .prepareStatement("update Numeric_Tab set min_val =?");
    ps.setBigDecimal(1, inout_param.get());
    ps.executeUpdate();

    Statement stmt = con.createStatement();
    ResultSet rs = stmt.executeQuery("Select min_val from Numeric_Tab");

    if (rs.next()) {

      inout_param.set(rs.getBigDecimal(1));
    } else {

      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    ps.close();
    ps = null;
    stmt.close();
    stmt = null;

  }

  public void Numeric_Io_Null(jdbcInOutBigDecimalWrapper inout_param)
      throws SQLException {

    PreparedStatement ps = con
        .prepareStatement("update Numeric_Tab set null_val =?");
    ps.setBigDecimal(1, inout_param.get());
    ps.executeUpdate();

    Statement stmt = con.createStatement();
    ResultSet rs = stmt.executeQuery("Select null_val from Numeric_Tab");

    if (rs.next()) {

      inout_param.set(rs.getBigDecimal(1));
    } else {

      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    ps.close();
    ps = null;
    stmt.close();
    stmt = null;

  }

  public void Decimal_Io_Max(jdbcInOutBigDecimalWrapper inout_param)
      throws SQLException {

    PreparedStatement ps = con
        .prepareStatement("update Decimal_Tab set max_val =?");
    ps.setBigDecimal(1, inout_param.get());
    ps.executeUpdate();

    Statement stmt = con.createStatement();
    ResultSet rs = stmt.executeQuery("Select max_val from Decimal_Tab");

    if (rs.next()) {

      inout_param.set(rs.getBigDecimal(1));
    } else {

      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    ps.close();
    ps = null;
    stmt.close();
    stmt = null;

  }

  public void Decimal_Io_Min(jdbcInOutBigDecimalWrapper inout_param)
      throws SQLException {

    PreparedStatement ps = con
        .prepareStatement("update Decimal_Tab set min_val =?");
    ps.setBigDecimal(1, inout_param.get());
    ps.executeUpdate();

    Statement stmt = con.createStatement();
    ResultSet rs = stmt.executeQuery("Select min_val from Decimal_Tab");

    if (rs.next()) {

      inout_param.set(rs.getBigDecimal(1));
    } else {

      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    ps.close();
    ps = null;
    stmt.close();
    stmt = null;

  }

  public void Decimal_Io_Null(jdbcInOutBigDecimalWrapper inout_param)
      throws SQLException {

    PreparedStatement ps = con
        .prepareStatement("update Decimal_Tab set null_val =?");
    ps.setBigDecimal(1, inout_param.get());
    ps.executeUpdate();

    Statement stmt = con.createStatement();
    ResultSet rs = stmt.executeQuery("Select null_val from Decimal_Tab");

    if (rs.next()) {

      inout_param.set(rs.getBigDecimal(1));
    } else {

      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    ps.close();
    ps = null;
    stmt.close();
    stmt = null;

  }

  public void Double_Io_Max(jdbcInOutDoubleWrapper inout_param)
      throws SQLException {

    PreparedStatement ps = con
        .prepareStatement("update Double_Tab set max_val =?");
    ps.setDouble(1, inout_param.get());
    ps.executeUpdate();

    Statement stmt = con.createStatement();
    ResultSet rs = stmt.executeQuery("Select max_val from Double_Tab");

    if (rs.next()) {

      inout_param.set(new Double(rs.getDouble(1)));
    } else {

      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    ps.close();
    ps = null;
    stmt.close();
    stmt = null;

  }

  public void Double_Io_Min(jdbcInOutDoubleWrapper inout_param)
      throws SQLException {

    PreparedStatement ps = con
        .prepareStatement("update Double_Tab set min_val =?");
    ps.setDouble(1, inout_param.get());
    ps.executeUpdate();

    Statement stmt = con.createStatement();
    ResultSet rs = stmt.executeQuery("Select min_val from Double_Tab");

    if (rs.next()) {

      inout_param.set(new Double(rs.getDouble(1)));
    } else {

      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    ps.close();
    ps = null;
    stmt.close();
    stmt = null;

  }

  public void Double_Io_Null(jdbcInOutDoubleWrapper inout_param)
      throws SQLException {

    PreparedStatement ps = con
        .prepareStatement("update Double_Tab set null_val =?");
    ps.setDouble(1, inout_param.get());
    ps.executeUpdate();

    Statement stmt = con.createStatement();
    ResultSet rs = stmt.executeQuery("Select null_val from Double_Tab");

    if (rs.next()) {

      inout_param.set(new Double(rs.getDouble(1)));
    } else {

      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    ps.close();
    ps = null;
    stmt.close();
    stmt = null;

  }

  public void Float_Io_Max(jdbcInOutDoubleWrapper inout_param)
      throws SQLException {

    PreparedStatement ps = con
        .prepareStatement("update Float_Tab set max_val =?");
    ps.setDouble(1, inout_param.get());
    ps.executeUpdate();

    Statement stmt = con.createStatement();
    ResultSet rs = stmt.executeQuery("Select max_val from Float_Tab");

    if (rs.next()) {

      inout_param.set(new Double(rs.getDouble(1)));
    } else {

      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    ps.close();
    ps = null;
    stmt.close();
    stmt = null;

  }

  public void Float_Io_Min(jdbcInOutDoubleWrapper inout_param)
      throws SQLException {

    PreparedStatement ps = con
        .prepareStatement("update Float_Tab set min_val =?");
    ps.setDouble(1, inout_param.get());
    ps.executeUpdate();

    Statement stmt = con.createStatement();
    ResultSet rs = stmt.executeQuery("Select min_val from Float_Tab");

    if (rs.next()) {

      inout_param.set(new Double(rs.getDouble(1)));
    } else {

      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    ps.close();
    ps = null;
    stmt.close();
    stmt = null;

  }

  public void Float_Io_Null(jdbcInOutDoubleWrapper inout_param)
      throws SQLException {

    PreparedStatement ps = con
        .prepareStatement("update Float_Tab set null_val =?");
    ps.setDouble(1, inout_param.get());
    ps.executeUpdate();

    Statement stmt = con.createStatement();
    ResultSet rs = stmt.executeQuery("Select null_val from Float_Tab");

    if (rs.next()) {

      inout_param.set(new Double(rs.getDouble(1)));
    } else {

      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    ps.close();
    ps = null;
    stmt.close();
    stmt = null;

  }

  public void Real_Io_Max(jdbcInOutFloatWrapper inout_param)
      throws SQLException {

    PreparedStatement ps = con
        .prepareStatement("update Real_Tab set max_val =?");
    ps.setFloat(1, inout_param.get());
    ps.executeUpdate();

    Statement stmt = con.createStatement();
    ResultSet rs = stmt.executeQuery("Select max_val from Real_Tab");

    if (rs.next()) {

      inout_param.set(rs.getFloat(1));
    } else {

      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    ps.close();
    ps = null;
    stmt.close();
    stmt = null;

  }

  public void Real_Io_Max(jdbcInOutDoubleWrapper inout_param)
      throws SQLException {

    PreparedStatement ps = con
        .prepareStatement("update Real_Tab set max_val =?");
    ps.setDouble(1, inout_param.get());
    ps.executeUpdate();

    Statement stmt = con.createStatement();
    ResultSet rs = stmt.executeQuery("Select max_val from Real_Tab");

    if (rs.next()) {

      inout_param.set(rs.getDouble(1));
    } else {

      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    ps.close();
    ps = null;
    stmt.close();
    stmt = null;

  }

  public void Real_Io_Min(jdbcInOutFloatWrapper inout_param)
      throws SQLException {

    PreparedStatement ps = con
        .prepareStatement("update Real_Tab set min_val =?");
    ps.setFloat(1, inout_param.get());
    ps.executeUpdate();

    Statement stmt = con.createStatement();
    ResultSet rs = stmt.executeQuery("Select min_val from Real_Tab");

    if (rs.next()) {

      inout_param.set(rs.getFloat(1));
    } else {

      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    ps.close();
    ps = null;
    stmt.close();
    stmt = null;

  }

  public void Real_Io_Min(jdbcInOutDoubleWrapper inout_param)
      throws SQLException {

    PreparedStatement ps = con
        .prepareStatement("update Real_Tab set min_val =?");
    ps.setDouble(1, inout_param.get());
    ps.executeUpdate();

    Statement stmt = con.createStatement();
    ResultSet rs = stmt.executeQuery("Select min_val from Real_Tab");

    if (rs.next()) {

      inout_param.set(rs.getDouble(1));
    } else {

      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    ps.close();
    ps = null;
    stmt.close();
    stmt = null;

  }

  public void Real_Io_Null(jdbcInOutFloatWrapper inout_param)
      throws SQLException {

    PreparedStatement ps = con
        .prepareStatement("update Real_Tab set null_val =?");
    ps.setFloat(1, inout_param.get());
    ps.executeUpdate();

    Statement stmt = con.createStatement();
    ResultSet rs = stmt.executeQuery("Select null_val from Real_Tab");

    if (rs.next()) {

      inout_param.set(rs.getFloat(1));
    } else {

      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    ps.close();
    ps = null;
    stmt.close();
    stmt = null;

  }

  public void Real_Io_Null(jdbcInOutDoubleWrapper inout_param)
      throws SQLException {

    PreparedStatement ps = con
        .prepareStatement("update Real_Tab set null_val =?");
    ps.setDouble(1, inout_param.get());
    ps.executeUpdate();

    Statement stmt = con.createStatement();
    ResultSet rs = stmt.executeQuery("Select null_val from Real_Tab");

    if (rs.next()) {

      inout_param.set(rs.getDouble(1));
    } else {

      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    ps.close();
    ps = null;
    stmt.close();
    stmt = null;

  }

  public void Bit_Io_Max(jdbcInOutBooleanWrapper inout_param)
      throws SQLException {

    PreparedStatement ps = con
        .prepareStatement("update Bit_Tab set max_val =?");
    ps.setBoolean(1, inout_param.get());
    ps.executeUpdate();

    Statement stmt = con.createStatement();
    ResultSet rs = stmt.executeQuery("Select max_val from Bit_Tab");

    if (rs.next()) {

      inout_param.set(rs.getBoolean(1));
    } else {

      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    ps.close();
    ps = null;
    stmt.close();
    stmt = null;

  }

  public void Bit_Io_Min(jdbcInOutBooleanWrapper inout_param)
      throws SQLException {

    PreparedStatement ps = con
        .prepareStatement("update Bit_Tab set min_val =?");
    ps.setBoolean(1, inout_param.get());
    ps.executeUpdate();

    Statement stmt = con.createStatement();
    ResultSet rs = stmt.executeQuery("Select min_val from Bit_Tab");

    if (rs.next()) {

      inout_param.set(rs.getBoolean(1));
    } else {

      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    ps.close();
    ps = null;
    stmt.close();
    stmt = null;

  }

  public void Bit_Io_Null(jdbcInOutBooleanWrapper inout_param)
      throws SQLException {

    PreparedStatement ps = con
        .prepareStatement("update Bit_Tab set null_val =?");
    ps.setBoolean(1, inout_param.get());
    ps.executeUpdate();

    Statement stmt = con.createStatement();
    ResultSet rs = stmt.executeQuery("Select null_val from Bit_Tab");

    if (rs.next()) {

      inout_param.set(rs.getBoolean(1));
    } else {

      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    ps.close();
    ps = null;
    stmt.close();
    stmt = null;

  }

  public void Smallint_Io_Max(jdbcInOutShortWrapper inout_param)
      throws SQLException {

    PreparedStatement ps = con
        .prepareStatement("update Smallint_Tab set max_val =?");
    ps.setShort(1, inout_param.get());
    ps.executeUpdate();

    Statement stmt = con.createStatement();
    ResultSet rs = stmt.executeQuery("Select max_val from Smallint_Tab");

    if (rs.next()) {

      inout_param.set(rs.getShort(1));
      if (rs.wasNull())
        inout_param.set(null);
    } else {

      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    ps.close();
    ps = null;
    stmt.close();
    stmt = null;

  }

  public void Smallint_Io_Min(jdbcInOutShortWrapper inout_param)
      throws SQLException {

    PreparedStatement ps = con
        .prepareStatement("update Smallint_Tab set min_val =?");
    ps.setShort(1, inout_param.get());
    ps.executeUpdate();

    Statement stmt = con.createStatement();
    ResultSet rs = stmt.executeQuery("Select min_val from Smallint_Tab");

    if (rs.next()) {

      inout_param.set(rs.getShort(1));
    } else {

      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    ps.close();
    ps = null;
    stmt.close();
    stmt = null;

  }

  public void Smallint_Io_Null(jdbcInOutShortWrapper inout_param)
      throws SQLException {

    PreparedStatement ps = con
        .prepareStatement("update Smallint_Tab set null_val =?");
    ps.setShort(1, inout_param.get());
    ps.executeUpdate();

    Statement stmt = con.createStatement();
    ResultSet rs = stmt.executeQuery("Select null_val from Smallint_Tab");

    if (rs.next()) {

      inout_param.set(rs.getShort(1));
    } else {

      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    ps.close();
    ps = null;
    stmt.close();
    stmt = null;

  }

  public void Tinyint_Io_Max(jdbcInOutShortWrapper inout_param)
      throws SQLException {

    PreparedStatement ps = con
        .prepareStatement("update Tinyint_Tab set max_val =?");
    ps.setByte(1, (byte) inout_param.get());
    ps.executeUpdate();

    Statement stmt = con.createStatement();
    ResultSet rs = stmt.executeQuery("Select max_val from Tinyint_Tab");

    if (rs.next()) {

      inout_param.set(rs.getByte(1));
    } else {

      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    ps.close();
    ps = null;
    stmt.close();
    stmt = null;

  }

  public void Tinyint_Io_Min(jdbcInOutShortWrapper inout_param)
      throws SQLException {

    PreparedStatement ps = con
        .prepareStatement("update Tinyint_Tab set min_val =?");
    ps.setByte(1, (byte) inout_param.get());
    ps.executeUpdate();

    Statement stmt = con.createStatement();
    ResultSet rs = stmt.executeQuery("Select min_val from Tinyint_Tab");

    if (rs.next()) {

      inout_param.set(new Short(rs.getByte(1)));
    } else {

      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    ps.close();
    ps = null;
    stmt.close();
    stmt = null;

  }

  public void Tinyint_Io_Null(jdbcInOutShortWrapper inout_param)
      throws SQLException {

    PreparedStatement ps = con
        .prepareStatement("update Tinyint_Tab set null_val =?");
    ps.setByte(1, (byte) inout_param.get());
    ps.executeUpdate();

    Statement stmt = con.createStatement();
    ResultSet rs = stmt.executeQuery("Select null_val from Tinyint_Tab");

    if (rs.next()) {

      inout_param.set(new Short(rs.getByte(1)));
    } else {

      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    ps.close();
    ps = null;
    stmt.close();
    stmt = null;

  }

  public void Integer_Io_Max(jdbcInOutIntWrapper max_param)
      throws SQLException {

    PreparedStatement ps = con
        .prepareStatement("update Integer_Tab set MAX_VAL=?");
    ps.setInt(1, max_param.get());
    ps.executeUpdate();

    Statement stmt = con.createStatement();
    ResultSet rs = stmt.executeQuery("select max_val from Integer_Tab");
    if (rs.next()) {

      max_param.set(rs.getInt(1));
    } else {

      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    ps.close();
    ps = null;
    stmt.close();
    stmt = null;

  }

  public void Integer_Io_Min(jdbcInOutIntWrapper min_param)
      throws SQLException {

    PreparedStatement ps = con
        .prepareStatement("update Integer_Tab set MIN_VAL=?");
    ps.setInt(1, min_param.get());
    ps.executeUpdate();

    Statement stmt = con.createStatement();
    ResultSet rs = stmt.executeQuery("select min_val from Integer_Tab");
    if (rs.next()) {

      min_param.set(rs.getInt(1));
    } else {

      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    ps.close();
    ps = null;
    stmt.close();
    stmt = null;

  }

  public void Integer_Io_Null(jdbcInOutIntWrapper null_param)
      throws SQLException {

    PreparedStatement ps = con
        .prepareStatement("update Integer_Tab set NULL_VAL=?");
    ps.setInt(1, null_param.get());
    ps.executeUpdate();

    Statement stmt = con.createStatement();
    ResultSet rs = stmt.executeQuery("select null_val from Integer_Tab");
    if (rs.next()) {

      null_param.set(rs.getInt(1));
      if (rs.wasNull())
        null_param.set(null);
    } else {

      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    ps.close();
    ps = null;
    stmt.close();
    stmt = null;

  }

  public void Bigint_Io_Max(jdbcInOutLongWrapper max_param)
      throws SQLException {

    PreparedStatement ps = con
        .prepareStatement("update Bigint_Tab set MAX_VAL=?");
    ps.setLong(1, max_param.get());
    ps.executeUpdate();

    Statement stmt = con.createStatement();
    ResultSet rs = stmt.executeQuery("select max_val from Bigint_Tab");
    if (rs.next()) {

      String maxValue = rs.getString(1);
      if (rs.wasNull())
        max_param.set(null);
      else
        max_param.set(new Long(maxValue));
    } else {

      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    ps.close();
    ps = null;
    stmt.close();
    stmt = null;

  }

  public void Bigint_Io_Min(jdbcInOutLongWrapper min_param)
      throws SQLException {

    PreparedStatement ps = con
        .prepareStatement("update Bigint_Tab set MIN_VAL=?");
    ps.setLong(1, min_param.get());
    ps.executeUpdate();

    Statement stmt = con.createStatement();
    ResultSet rs = stmt.executeQuery("select min_val from Bigint_Tab");
    if (rs.next()) {

      String minValue = rs.getString(1);
      if (rs.wasNull())
        min_param.set(null);
      else
        min_param.set(new Long(minValue));

    } else {

      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    ps.close();
    ps = null;
    stmt.close();
    stmt = null;

  }

  public void Bigint_Io_Null(jdbcInOutLongWrapper null_param)
      throws SQLException {

    PreparedStatement ps = con
        .prepareStatement("update Bigint_Tab set NULL_VAL=?");
    ps.setLong(1, null_param.get());
    ps.executeUpdate();

    Statement stmt = con.createStatement();
    ResultSet rs = stmt.executeQuery("select null_val from Bigint_Tab");
    if (rs.next()) {

      String nullValue = rs.getString(1);
      if (rs.wasNull())
        null_param.set(null);
      else
        null_param.set(new Long(nullValue));

    } else {

      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    ps.close();
    ps = null;
    stmt.close();
    stmt = null;

  }

  public void Bigint_Io_Mull(jdbcInOutLongWrapper null_param)
      throws SQLException {

    PreparedStatement ps = con
        .prepareStatement("update Bigint_Tab set NULL_VAL=?");
    ps.setLong(1, null_param.get());
    ps.executeUpdate();

    Statement stmt = con.createStatement();
    ResultSet rs = stmt.executeQuery("select null_val from Bigint_Tab");
    if (rs.next()) {

      String nullValue = rs.getString(1);
      if (rs.wasNull())
        null_param.set(null);
      else
        null_param.set(new Long(nullValue));

    } else {

      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    ps.close();
    ps = null;
    stmt.close();
    stmt = null;

  }

  public void Char_Io_Name(jdbcInOutStringWrapper name_param)
      throws SQLException {

    PreparedStatement ps = con
        .prepareStatement("update char_Tab set coffee_name=?");
    ps.setString(1, name_param.get());
    ps.executeUpdate();

    Statement stmt = con.createStatement();
    ResultSet rs = stmt.executeQuery("select coffee_name from char_Tab");
    if (rs.next()) {

      name_param.set(rs.getString(1));
    } else {

      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    ps.close();
    ps = null;
    stmt.close();
    stmt = null;

  }

  public void Char_Io_Null(jdbcInOutStringWrapper null_param)
      throws SQLException {

    PreparedStatement ps = con
        .prepareStatement("update char_Tab set null_val=?");
    ps.setString(1, null_param.get());
    ps.executeUpdate();

    Statement stmt = con.createStatement();
    ResultSet rs = stmt.executeQuery("select null_val from char_Tab");
    if (rs.next()) {

      null_param.set(rs.getString(1));
      if (rs.wasNull())
        null_param.set(null);
    } else {

      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    ps.close();
    ps = null;
    stmt.close();
    stmt = null;

  }

  public void Varchar_Io_Name(jdbcInOutStringWrapper name_param)
      throws SQLException {

    PreparedStatement ps = con
        .prepareStatement("update varchar_Tab set coffee_name=?");
    ps.setString(1, name_param.get());
    ps.executeUpdate();

    Statement stmt = con.createStatement();
    ResultSet rs = stmt.executeQuery("select coffee_name from varchar_Tab");
    if (rs.next()) {

      name_param.set(rs.getString(1));
    } else {

      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    ps.close();
    ps = null;
    stmt.close();
    stmt = null;

  }

  public void Varchar_Io_Null(jdbcInOutStringWrapper null_param)
      throws SQLException {

    PreparedStatement ps = con
        .prepareStatement("update varchar_Tab set null_val=?");
    ps.setString(1, null_param.get());
    ps.executeUpdate();

    Statement stmt = con.createStatement();
    ResultSet rs = stmt.executeQuery("select null_val from varchar_Tab");
    if (rs.next()) {

      null_param.set(rs.getString(1));
      if (rs.wasNull())
        null_param.set(null);
    } else {

      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    ps.close();
    ps = null;
    stmt.close();
    stmt = null;

  }

  public void Longvarchar_Io_Name(jdbcInOutStringWrapper name_param)
      throws SQLException {

    PreparedStatement ps = con
        .prepareStatement("update Longvarchar_Tab set coffee_name=?");
    ps.setString(1, name_param.get());
    ps.executeUpdate();

    Statement stmt = con.createStatement();
    ResultSet rs = stmt.executeQuery("select coffee_name from Longvarchar_Tab");
    if (rs.next()) {

      name_param.set(rs.getString(1));
    } else {

      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    ps.close();
    ps = null;
    stmt.close();
    stmt = null;

  }

  public void Longvarchar_Io_Null(jdbcInOutStringWrapper name_param)
      throws SQLException {

    PreparedStatement ps = con
        .prepareStatement("update Longvarcharnull_Tab set null_val=?");
    ps.setString(1, name_param.get());
    ps.executeUpdate();

    Statement stmt = con.createStatement();
    ResultSet rs = stmt
        .executeQuery("select null_val from Longvarcharnull_Tab");
    if (rs.next()) {

      name_param.set(rs.getString(1));
    } else {

      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    ps.close();
    ps = null;
    stmt.close();
    stmt = null;

  }

  public void Date_Io_Mfg(jdbcInOutDateWrapper mfg_param) throws SQLException {

    PreparedStatement ps = con
        .prepareStatement("update Date_Tab set mfg_date=?");
    ps.setDate(1, mfg_param.get());
    ps.executeUpdate();

    Statement stmt = con.createStatement();
    ResultSet rs = stmt.executeQuery("select mfg_date from Date_Tab");
    if (rs.next()) {

      mfg_param.set(rs.getDate(1));
    } else {

      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    ps.close();
    ps = null;
    stmt.close();
    stmt = null;

  }

  public void Date_Io_Null(jdbcInOutDateWrapper null_param)
      throws SQLException {

    PreparedStatement ps = con
        .prepareStatement("update Date_Tab set null_val=?");
    ps.setDate(1, null_param.get());
    ps.executeUpdate();

    Statement stmt = con.createStatement();
    ResultSet rs = stmt.executeQuery("select null_val from Date_Tab");
    if (rs.next()) {

      null_param.set(rs.getDate(1));
      if (rs.wasNull())
        null_param.set(null);
    } else {

      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    ps.close();
    ps = null;
    stmt.close();
    stmt = null;

  }

  public void Time_Io_Brk(jdbcInOutTimeWrapper brk_param) throws SQLException {

    PreparedStatement ps = con
        .prepareStatement("update Time_Tab set brk_time=?");
    ps.setTime(1, brk_param.get());
    ps.executeUpdate();

    Statement stmt = con.createStatement();
    ResultSet rs = stmt.executeQuery("select brk_time from Time_Tab");
    if (rs.next()) {

      brk_param.set(rs.getTime(1));

    } else {

      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    ps.close();
    ps = null;
    stmt.close();
    stmt = null;

  }

  public void Time_Io_Null(jdbcInOutTimeWrapper null_param)
      throws SQLException {

    PreparedStatement ps = con
        .prepareStatement("update Time_Tab set null_val=?");
    ps.setTime(1, null_param.get());
    ps.executeUpdate();

    Statement stmt = con.createStatement();

    ResultSet rs = stmt.executeQuery("select null_val from Time_Tab");
    if (rs.next()) {

      null_param.set(rs.getTime(1));
      if (rs.wasNull())
        null_param.set(null);
    } else {

      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    ps.close();
    ps = null;
    stmt.close();
    stmt = null;

  }

  public void Timestamp_Io_Intime(jdbcInOutTimestampWrapper intime_param)
      throws SQLException {

    PreparedStatement ps = con
        .prepareStatement("update Timestamp_Tab set in_time=?");
    ps.setTimestamp(1, intime_param.get());
    ps.executeUpdate();

    Statement stmt = con.createStatement();
    ResultSet rs = stmt.executeQuery("select in_time from Timestamp_Tab");
    if (rs.next()) {

      intime_param.set(rs.getTimestamp(1));
    } else {

      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    ps.close();
    ps = null;
    stmt.close();
    stmt = null;

  }

  public void Timestamp_Io_Null(jdbcInOutTimestampWrapper null_param)
      throws SQLException {

    PreparedStatement ps = con
        .prepareStatement("update Timestamp_Tab set null_val=?");
    ps.setTimestamp(1, null_param.get());
    ps.executeUpdate();

    Statement stmt = con.createStatement();
    ResultSet rs = stmt.executeQuery("select null_val from Timestamp_Tab");

    if (rs.next()) {

      null_param.set(rs.getTimestamp(1));
      if (rs.wasNull())
        null_param.set(null);
    } else {

      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    ps.close();
    ps = null;
    stmt.close();
    stmt = null;

  }

  public void Binary_Proc_Io(jdbcInOutByteArrayWrapper binary_param)
      throws SQLException {

    System.out.println(
        "The Length of byte[] in Binary_Proc_Io: " + binary_param.size());

    PreparedStatement ps = con
        .prepareStatement("update Binary_Tab set binary_val=?");
    ps.setBytes(1, binary_param.get());
    ps.executeUpdate();

    Statement stmt = con.createStatement();
    ResultSet rs = stmt.executeQuery("select binary_val from Binary_Tab");
    if (rs.next()) {

      binary_param.set(rs.getBytes(1));
    } else {
      throw new SQLException("Data not found, length=" + binary_param.size());
    }

    rs.close();
    rs = null;
    ps.close();
    ps = null;
    stmt.close();
    stmt = null;

  }

  public void Varbinary_Proc_Io(jdbcInOutByteArrayWrapper varbinary_param)
      throws SQLException {

    PreparedStatement ps = con
        .prepareStatement("update Varbinary_Tab set varbinary_val=?");
    ps.setBytes(1, varbinary_param.get());
    ps.executeUpdate();

    Statement stmt = con.createStatement();
    ResultSet rs = stmt.executeQuery("select varbinary_val from Varbinary_Tab");
    if (rs.next()) {

      varbinary_param.set(rs.getBytes(1));
    } else {

      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    ps.close();
    ps = null;
    stmt.close();
    stmt = null;

  }

  public void Longvarbinary_Io(jdbcInOutByteArrayWrapper longvarbinary_param)
      throws SQLException {

    PreparedStatement ps = con
        .prepareStatement("update Longvarbinary_Tab set Longvarbinary_val=?");
    ps.setBytes(1, longvarbinary_param.get());
    ps.executeUpdate();

    Statement stmt = con.createStatement();
    ResultSet rs = stmt
        .executeQuery("select longvarbinary_val from Longvarbinary_Tab");
    if (rs.next()) {
      longvarbinary_param.set(rs.getBytes(1));
    } else {

      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    ps.close();
    ps = null;
    stmt.close();
    stmt = null;

  }

  /*****************
   * Defining methods for CallableStatement IN parameter
   *******************/

  public void Numeric_In_Max(BigDecimal max_param) throws SQLException {

    PreparedStatement ps = con
        .prepareStatement("update Numeric_Tab set MAX_VAL=?");

    ps.setBigDecimal(1, max_param);
    ps.executeUpdate();

    ps.close();
    ps = null;

  }

  public void Numeric_In_Min(BigDecimal min_param) throws SQLException {

    PreparedStatement ps = con
        .prepareStatement("update Numeric_Tab set MIN_VAL=?");

    ps.setBigDecimal(1, min_param);
    ps.executeUpdate();

    ps.close();
    ps = null;

  }

  public void Numeric_In_Null(BigDecimal null_param) throws SQLException {

    PreparedStatement ps = con
        .prepareStatement("update Numeric_Tab set NULL_VAL=?");

    ps.setBigDecimal(1, null_param);
    ps.executeUpdate();

    ps.close();
    ps = null;

  }

  public void Decimal_In_Max(BigDecimal max_param) throws SQLException {

    PreparedStatement ps = con
        .prepareStatement("update Decimal_Tab set MAX_VAL=?");

    ps.setBigDecimal(1, max_param);
    ps.executeUpdate();

    ps.close();
    ps = null;

  }

  public void Decimal_In_Min(BigDecimal min_param) throws SQLException {

    PreparedStatement ps = con
        .prepareStatement("update Decimal_Tab set MIN_VAL=?");

    ps.setBigDecimal(1, min_param);
    ps.executeUpdate();

    ps.close();
    ps = null;

  }

  public void Decimal_In_Null(BigDecimal null_param) throws SQLException {

    PreparedStatement ps = con
        .prepareStatement("update Decimal_Tab set NULL_VAL=?");

    ps.setBigDecimal(1, null_param);
    ps.executeUpdate();

    ps.close();
    ps = null;

  }

  public void Double_In_Max(double max_param) throws SQLException {

    PreparedStatement ps = con
        .prepareStatement("update Double_Tab set MAX_VAL=?");

    ps.setDouble(1, max_param);
    ps.executeUpdate();

    ps.close();
    ps = null;

  }

  public void Double_In_Min(double min_param) throws SQLException {

    PreparedStatement ps = con
        .prepareStatement("update Double_Tab set MIN_VAL=?");

    ps.setDouble(1, min_param);
    ps.executeUpdate();

    ps.close();
    ps = null;

  }

  public void Double_In_Null(double null_param) throws SQLException {

    PreparedStatement ps = con
        .prepareStatement("update Double_Tab set NULL_VAL=?");

    ps.setDouble(1, null_param);
    ps.executeUpdate();

    ps.close();
    ps = null;

  }

  public void Float_In_Max(double max_param) throws SQLException {

    PreparedStatement ps = con
        .prepareStatement("update Float_Tab set MAX_VAL=?");

    ps.setDouble(1, max_param);
    ps.executeUpdate();

    ps.close();
    ps = null;

  }

  public void Float_In_Min(double min_param) throws SQLException {

    PreparedStatement ps = con
        .prepareStatement("update Float_Tab set MIN_VAL=?");

    ps.setDouble(1, min_param);
    ps.executeUpdate();

    ps.close();
    ps = null;

  }

  public void Float_In_Null(double null_param) throws SQLException {

    PreparedStatement ps = con
        .prepareStatement("update Float_Tab set NULL_VAL=?");

    ps.setDouble(1, null_param);
    ps.executeUpdate();

    ps.close();
    ps = null;

  }

  public void Real_In_Max(float max_param) throws SQLException {

    PreparedStatement ps = con
        .prepareStatement("update Real_Tab set MAX_VAL=?");

    ps.setFloat(1, max_param);
    ps.executeUpdate();

    ps.close();
    ps = null;

  }

  public void Real_In_Max(double max_param) throws SQLException {

    PreparedStatement ps = con
        .prepareStatement("update Real_Tab set MAX_VAL=?");

    ps.setDouble(1, max_param);
    ps.executeUpdate();

    ps.close();
    ps = null;

  }

  public void Real_In_Min(float min_param) throws SQLException {

    PreparedStatement ps = con
        .prepareStatement("update Real_Tab set MIN_VAL=?");

    ps.setFloat(1, min_param);
    ps.executeUpdate();

    ps.close();
    ps = null;

  }

  public void Real_In_Min(double min_param) throws SQLException {

    PreparedStatement ps = con
        .prepareStatement("update Real_Tab set MIN_VAL=?");

    ps.setDouble(1, min_param);
    ps.executeUpdate();

    ps.close();
    ps = null;

  }

  public void Real_In_Null(float null_param) throws SQLException {

    PreparedStatement ps = con
        .prepareStatement("update Real_Tab set NULL_VAL=?");

    ps.setFloat(1, null_param);
    ps.executeUpdate();

    ps.close();
    ps = null;

  }

  public void Real_In_Null(double null_param) throws SQLException {

    PreparedStatement ps = con
        .prepareStatement("update Real_Tab set NULL_VAL=?");

    ps.setDouble(1, null_param);
    ps.executeUpdate();

    ps.close();
    ps = null;

  }

  public void Bit_In_Max(boolean max_param) throws SQLException {

    PreparedStatement ps = con.prepareStatement("update Bit_Tab set MAX_VAL=?");

    ps.setBoolean(1, max_param);
    ps.executeUpdate();

    ps.close();
    ps = null;

  }

  public void Bit_In_Min(boolean min_param) throws SQLException {

    PreparedStatement ps = con.prepareStatement("update Bit_Tab set MIN_VAL=?");

    ps.setBoolean(1, min_param);
    ps.executeUpdate();

    ps.close();
    ps = null;

  }

  public void Bit_In_Null(boolean null_param) throws SQLException {

    PreparedStatement ps = con
        .prepareStatement("update Bit_Tab set NULL_VAL=?");

    ps.setBoolean(1, null_param);
    ps.executeUpdate();

    ps.close();
    ps = null;

  }

  public void Smallint_In_Max(short max_param) throws SQLException {

    PreparedStatement ps = con
        .prepareStatement("update Smallint_Tab set MAX_VAL=?");

    ps.setShort(1, max_param);
    ps.executeUpdate();

    ps.close();
    ps = null;

  }

  public void Smallint_In_Min(int min_param) throws SQLException {

    PreparedStatement ps = con
        .prepareStatement("update Smallint_Tab set MIN_VAL=?");

    ps.setShort(1, (short) min_param);
    ps.executeUpdate();

    ps.close();
    ps = null;

  }

  public void Smallint_In_Min(short min_param) throws SQLException {

    PreparedStatement ps = con
        .prepareStatement("update Smallint_Tab set MIN_VAL=?");

    ps.setShort(1, min_param);
    ps.executeUpdate();

    ps.close();
    ps = null;

  }

  public void Smallint_In_Null(int null_param) throws SQLException {

    PreparedStatement ps = con
        .prepareStatement("update Smallint_Tab set NULL_VAL=?");

    ps.setShort(1, (short) null_param);
    ps.executeUpdate();

    ps.close();
    ps = null;

  }

  public void Smallint_In_Null(short null_param) throws SQLException {

    PreparedStatement ps = con
        .prepareStatement("update Smallint_Tab set NULL_VAL=?");

    ps.setShort(1, null_param);
    ps.executeUpdate();

    ps.close();
    ps = null;

  }

  public void Tinyint_In_Max(short in_param) throws SQLException {

    PreparedStatement ps = con
        .prepareStatement("update Tinyint_Tab set MAX_VAL=?");

    ps.setByte(1, (byte) in_param);
    ps.executeUpdate();

    ps.close();
    ps = null;

  }

  public void Tinyint_In_Min(int in_param) throws SQLException {

    PreparedStatement ps = con
        .prepareStatement("update Tinyint_Tab set MIN_VAL=?");

    ps.setByte(1, (byte) in_param);
    ps.executeUpdate();

    ps.close();
    ps = null;

  }

  public void Tinyint_In_Min(short in_param) throws SQLException {

    PreparedStatement ps = con
        .prepareStatement("update Tinyint_Tab set MIN_VAL=?");

    ps.setByte(1, (byte) in_param);
    ps.executeUpdate();

    ps.close();
    ps = null;

  }

  public void Tinyint_In_Null(short in_param) throws SQLException {

    PreparedStatement ps = con
        .prepareStatement("update Tinyint_Tab set NULL_VAL=?");

    ps.setByte(1, (byte) in_param);
    ps.executeUpdate();

    ps.close();
    ps = null;

  }

  public void Tinyint_In_Null(int in_param) throws SQLException {

    PreparedStatement ps = con
        .prepareStatement("update Tinyint_Tab set NULL_VAL=?");

    ps.setByte(1, (byte) in_param);
    ps.executeUpdate();

    ps.close();
    ps = null;

  }

  public void Integer_In_Max(int in_param) throws SQLException {

    PreparedStatement ps = con
        .prepareStatement("update Integer_Tab set MAX_VAL=?");

    ps.setInt(1, in_param);
    ps.executeUpdate();

    ps.close();
    ps = null;

  }

  public void Integer_In_Min(int in_param) throws SQLException {

    PreparedStatement ps = con
        .prepareStatement("update Integer_Tab set MIN_VAL=?");

    ps.setInt(1, in_param);
    ps.executeUpdate();

    ps.close();
    ps = null;

  }

  public void Integer_In_Null(int in_param) throws SQLException {

    PreparedStatement ps = con
        .prepareStatement("update Integer_Tab set NULL_VAL=?");

    ps.setInt(1, in_param);
    ps.executeUpdate();

    ps.close();
    ps = null;

  }

  public void Bigint_In_Max(long in_param) throws SQLException {

    PreparedStatement ps = con
        .prepareStatement("update Bigint_Tab set MAX_VAL=?");

    ps.setLong(1, in_param);
    ps.executeUpdate();

    ps.close();
    ps = null;

  }

  public void Bigint_In_Min(long in_param) throws SQLException {

    PreparedStatement ps = con
        .prepareStatement("update Bigint_Tab set MIN_VAL=?");

    // ps.setLong(1,in_param.longValue());
    ps.setLong(1, in_param);
    ps.executeUpdate();

    ps.close();
    ps = null;

  }

  /*
   * public void Bigint_In_Min (BigDecimal in_param) throws SQLException {
   * 
   * 
   * PreparedStatement ps =
   * con.prepareStatement("update Bigint_Tab set MIN_VAL=?");
   * 
   * ps.setLong(1,in_param.longValue()); ps.executeUpdate();
   * 
   * ps.close();ps=null;
   * 
   * }
   */
  public void Bigint_In_Null(long in_param) throws SQLException {

    PreparedStatement ps = con
        .prepareStatement("update Bigint_Tab set NULL_VAL=?");

    ps.setLong(1, in_param);
    ps.executeUpdate();

    ps.close();
    ps = null;

  }

  /*
   * public void Bigint_In_Null (BigDecimal in_param) throws SQLException {
   * 
   * 
   * PreparedStatement ps =
   * con.prepareStatement("update Bigint_Tab set NULL_VAL=?");
   * 
   * ps.setLong(1,in_param.longValue()); ps.executeUpdate();
   * 
   * ps.close();ps=null;
   * 
   * }
   */
  public void Char_In_Name(String in_param) throws SQLException {

    PreparedStatement ps = con
        .prepareStatement("update Char_Tab set coffee_name=?");

    ps.setString(1, in_param);
    ps.executeUpdate();

    ps.close();
    ps = null;

  }

  public void Char_In_Null(String in_param) throws SQLException {

    PreparedStatement ps = con
        .prepareStatement("update Char_Tab set NULL_VAL=?");

    ps.setString(1, in_param);
    ps.executeUpdate();

    ps.close();
    ps = null;

  }

  public void Varchar_In_Name(String in_param) throws SQLException {

    PreparedStatement ps = con
        .prepareStatement("update Varchar_Tab set coffee_name=?");

    ps.setString(1, in_param);
    ps.executeUpdate();

    ps.close();
    ps = null;

  }

  public void Varchar_In_Null(String in_param) throws SQLException {

    PreparedStatement ps = con
        .prepareStatement("update Varchar_Tab set NULL_VAL=?");

    ps.setString(1, in_param);
    ps.executeUpdate();

    ps.close();
    ps = null;

  }

  public void Longvarchar_In_Name(String in_param) throws SQLException {

    PreparedStatement ps = con
        .prepareStatement("update Longvarchar_Tab set coffee_name=?");

    ps.setString(1, in_param);
    ps.executeUpdate();

    ps.close();
    ps = null;

  }

  public void Longvarchar_In_Null(String in_param) throws SQLException {

    PreparedStatement ps = con
        .prepareStatement("update Longvarcharnull_Tab set NULL_VAL=?");

    ps.setString(1, in_param);
    ps.executeUpdate();

    ps.close();
    ps = null;

  }

  public void Date_In_Mfg(java.sql.Date in_param) throws SQLException {

    PreparedStatement ps = con
        .prepareStatement("update Date_Tab set mfg_date=?");

    ps.setDate(1, in_param);
    ps.executeUpdate();

    ps.close();
    ps = null;

  }

  public void Date_In_Null(java.sql.Date in_param) throws SQLException {

    PreparedStatement ps = con
        .prepareStatement("update Date_Tab set NULL_VAL=?");

    ps.setDate(1, in_param);
    ps.executeUpdate();

    ps.close();
    ps = null;

  }

  public void Time_In_Brk(Time in_param) throws SQLException {

    PreparedStatement ps = con
        .prepareStatement("update Time_Tab set BRK_TIME=?");

    ps.setTime(1, in_param);
    ps.executeUpdate();

    ps.close();
    ps = null;

  }

  public void Time_In_Null(Time in_param) throws SQLException {

    PreparedStatement ps = con
        .prepareStatement("update Time_Tab set NULL_VAL=?");

    ps.setTime(1, in_param);
    ps.executeUpdate();

    ps.close();
    ps = null;

  }

  public void Timestamp_In_Intime(Timestamp in_param) throws SQLException {

    PreparedStatement ps = con
        .prepareStatement("update Timestamp_Tab set IN_TIME=?");

    ps.setTimestamp(1, in_param);
    ps.executeUpdate();

    ps.close();
    ps = null;

  }

  public void Timestamp_In_Null(Timestamp in_param) throws SQLException {

    PreparedStatement ps = con
        .prepareStatement("update Timestamp_Tab set NULL_VAL=?");

    ps.setTimestamp(1, in_param);
    ps.executeUpdate();

    ps.close();
    ps = null;

  }

  public void Binary_Proc_In(byte[] in_param) throws SQLException {

    PreparedStatement ps = con
        .prepareStatement("update Binary_Tab set BINARY_VAL=?");

    System.out
        .println("The Length of byte[] in Binary_Proc_In: " + in_param.length);

    ps.setBytes(1, in_param);
    ps.executeUpdate();

    ps.close();
    ps = null;

  }

  public void Varbinary_Proc_In(byte[] in_param) throws SQLException {

    PreparedStatement ps = con
        .prepareStatement("update Varbinary_Tab set VARBINARY_VAL=?");

    // System.out.println("The Length of byte[] in Varbinary_Proc_In: "+
    // in_param.length);

    ps.setBytes(1, in_param);
    ps.executeUpdate();

    ps.close();
    ps = null;

  }

  public void Longvarbinary_Proc_In(byte[] in_param) throws SQLException {

    PreparedStatement ps = con
        .prepareStatement("update Longvarbinary_Tab set LONGVARBINARY_VAL=?");

    // System.out.println("The Length of byte[] in Longvarbinary_Proc_In: "+
    // in_param.length);

    ps.setBytes(1, in_param);
    ps.executeUpdate();

    ps.close();
    ps = null;

  }

  public void Longvarbinary_In(byte[] in_param) throws SQLException {

    PreparedStatement ps = con
        .prepareStatement("update Longvarbinary_Tab set LONGVARBINARY_VAL=?");

    // System.out.println("The Length of byte[] in Longvarbinary_In: "+
    // in_param.length);

    ps.setBytes(1, in_param);
    ps.executeUpdate();

    ps.close();
    ps = null;

  }

}
