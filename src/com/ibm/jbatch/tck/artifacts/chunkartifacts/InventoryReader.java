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

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

import javax.batch.api.chunk.AbstractItemReader;
import javax.batch.runtime.context.JobContext;
import javax.batch.runtime.context.StepContext;
import javax.inject.Inject;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.ibm.jbatch.tck.artifacts.chunktypes.InventoryCheckpointData;
import com.ibm.jbatch.tck.artifacts.chunktypes.InventoryRecord;

@javax.inject.Named("inventoryReader")
public class InventoryReader extends AbstractItemReader {

	private static final String CLASSNAME = InventoryReader.class.getName();
	private final static Logger logger = Logger.getLogger(CLASSNAME);
	

	protected DataSource dataSource = null;
	
    @Inject
    JobContext jobCtx;
	
    @Inject
    StepContext stepCtx;
    

	int readerIndex = 0; //the number of items that have already been read
	InventoryCheckpointData inventoryCheckpoint = new InventoryCheckpointData();
	
	
	public void open(Serializable cpd) throws NamingException {

	    InventoryCheckpointData checkpointData = (InventoryCheckpointData)cpd;
	    

		
		InitialContext ctx = new InitialContext();
		dataSource = (DataSource) ctx.lookup(ConnectionHelper.jndiName);
		
		if (cpd != null) {
			this.readerIndex = checkpointData.getInventoryCount();
			// Fix for Bug 5490:    https://java.net/bugzilla/show_bug.cgi?id=5490
			stepCtx.setTransientUserData(this.readerIndex);
		} 	
	}

	public InventoryRecord readItem() throws Exception {


		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		
		try {
			connection = ConnectionHelper.getConnection(dataSource);

			statement = connection.prepareStatement(ConnectionHelper.SELECT_INVENTORY);
			statement.setInt(1, 1);
			rs = statement.executeQuery();

			int quantity = -1;
			while (rs.next()) {
				quantity = rs.getInt("quantity");
			}
			
			//If we run out of items we are done so stop processing orders
			if (quantity < 1) {
				return null;
			}
			
			//decrement the quantity and update the table
			
			InventoryRecord ir = new InventoryRecord(1, --quantity);
			decrementInventory(connection, ir);
			
			readerIndex++;			
			this.inventoryCheckpoint.setInventoryCount(readerIndex);

			return new InventoryRecord(1, 1); //Every order only orders 1 item
		} catch (SQLException e) {
			throw e;
		} finally {
			ConnectionHelper.cleanupConnection(connection, rs, statement);
		}

	}


    @Override
    public Serializable checkpointInfo() throws Exception {
        logger.finer("InventoryReader.getInventoryCheckpoint() index = " +this.inventoryCheckpoint.getInventoryCount());
        
        return this.inventoryCheckpoint;
    }


    

    private void decrementInventory(Connection connection, InventoryRecord record) throws SQLException {
        
        int itemID = record.getItemID();
        int quantity = record.getQuantity();

        PreparedStatement statement = null;

        statement = connection.prepareStatement(ConnectionHelper.UPDATE_INVENTORY);
        statement.setInt(2, itemID);
        statement.setInt(1, quantity);
        int rs = statement.executeUpdate();            
        
    }



}
