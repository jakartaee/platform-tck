/*
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

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.batch.api.chunk.AbstractItemReader;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.ibm.jbatch.tck.artifacts.chunktypes.NumbersCheckpointData;
import com.ibm.jbatch.tck.artifacts.chunktypes.NumbersRecord;

@javax.inject.Named("retryInitReader")
public class RetryInitReader extends AbstractItemReader {

	protected DataSource dataSource = null;

	private int count = 0;

	public void open(Serializable cpd) throws NamingException {
		InitialContext ctx = new InitialContext();
		dataSource = (DataSource) ctx.lookup(RetryConnectionHelper.jndiName);
	}

	@Override
    public NumbersRecord readItem() throws SQLException {
        if (count > 19) {
            return null;
        }

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet rs = null;

        try {
            connection = RetryConnectionHelper.getConnection(dataSource);

            statement = connection.prepareStatement(RetryConnectionHelper.SELECT_NUMBERS);
            statement.setInt(1, 1);
            rs = statement.executeQuery();

            int quantity = -1;
            while (rs.next()) {
                quantity = rs.getInt("quantity");
                count++;
            }

            return new NumbersRecord(count, quantity);

        } catch (SQLException e) {
        	e.printStackTrace();
            throw e;
        } finally {
            RetryConnectionHelper.cleanupConnection(connection, rs, statement);
        }

    }

	@Override
	public Serializable checkpointInfo() {
		NumbersCheckpointData chkpData = new NumbersCheckpointData();
		chkpData.setCount(count);
		return chkpData;
	}
}

