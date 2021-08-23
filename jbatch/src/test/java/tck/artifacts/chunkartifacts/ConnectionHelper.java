/**
 * Copyright 2012 International Business Machines Corp.
 *
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership. Licensed under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ibm.jbatch.tck.artifacts.chunkartifacts;


import com.sun.ts.lib.harness.*;
import com.sun.ts.lib.util.TestUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

import javax.sql.DataSource;

public class ConnectionHelper {
	
	private static final String CLASSNAME = ConnectionHelper.class.getName();
	private final static Logger logger = Logger.getLogger(CLASSNAME);
	
	public static final String jndiName = "java:module/env/jdbc/orderDB";
	
	public static final String INSERT_INVENTORY = "insert into inventory values(?, ?)";

	public static final String UPDATE_INVENTORY = "update inventory set quantity = ? where itemID = ?";

	public static final String SELECT_INVENTORY = "select itemID, quantity from inventory where itemID = ?";

	public static final String DELETE_INVENTORY = "delete from Inventory where itemID = ?";
	
	public static final String DELETE_ALL_ORDERS = "delete from Orders where orderID > 0";
	
	public static final String INSERT_ORDER = "insert into Orders values(DEFAULT, ?, ?)";
	
	public static final String COUNT_ORDERS = "select COUNT(*) AS rowcount from Orders";
	
	/*
	 * Connection where autoCommit defaults to true unless we are in a global tran where it gets ignored
	 */
	public static Connection getConnection(DataSource dataSource) throws SQLException {

		Connection conn = dataSource.getConnection();
		return conn;
	}
	
	
	public static Connection getConnection(DataSource dataSource, boolean autoCommit) throws SQLException {
		Connection conn = dataSource.getConnection();
		conn.setAutoCommit(autoCommit);
		
		return conn;
	}
	
	/**
	 * closes connection to DB
	 * 
	 * @param conn - connection object to close
	 * @param rs - result set object to close
	 * @param statement - statement object to close
	 */
	public static void cleanupConnection(Connection conn, ResultSet rs, PreparedStatement statement) {
		logger.entering(CLASSNAME, "cleanupConnection", new Object[] {conn, rs, statement});
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
		
		if (statement != null) {
			try {
				statement.close();
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}

		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				throw new RuntimeException(e);
			} finally {
				try {
					conn.close();
				} catch (SQLException e) {
					throw new RuntimeException(e);
				}
			}
		}
		logger.exiting(CLASSNAME, "cleanupConnection");
	}
}
