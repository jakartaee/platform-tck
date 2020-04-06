/**
 * Copyright 2012, 2020 International Business Machines Corp.
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
import java.sql.SQLException;
import java.util.List;

import jakarta.batch.api.chunk.AbstractItemWriter;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.ibm.jbatch.tck.artifacts.chunktypes.InventoryRecord;

@javax.inject.Named("inventoryInitWriter")
public class InventoryInitWriter extends AbstractItemWriter {
	
	
	protected DataSource dataSource = null;
	
    public void open(Serializable cpd) throws NamingException {

        InitialContext ctx = new InitialContext();
        dataSource = (DataSource) ctx.lookup(ConnectionHelper.jndiName);

    }

	
    @Override
    public void writeItems(List<Object> records) throws Exception {
        int itemID = -1;
        int quantity = -1;
        
        
        
        for (Object record : records) {
            itemID = ((InventoryRecord)record).getItemID();
            quantity = ((InventoryRecord)record).getQuantity();
        }
        
        Connection connection = null;   
        PreparedStatement statement = null;
        
        try {
            
            //Clear all orders from the orders table
            connection = ConnectionHelper.getConnection(dataSource);
            statement = connection.prepareStatement(ConnectionHelper.DELETE_ALL_ORDERS);
            int rs = statement.executeUpdate();

            
            ConnectionHelper.cleanupConnection(connection, null, statement);
            
            //Reset the inventory table
            connection = ConnectionHelper.getConnection(dataSource);
            statement = connection.prepareStatement(ConnectionHelper.UPDATE_INVENTORY);
            statement.setInt(2, itemID);
            statement.setInt(1, quantity);
            rs = statement.executeUpdate();
            
        } catch (SQLException e) {
            throw e;
        } finally {
            ConnectionHelper.cleanupConnection(connection, null, statement);
        }
        
        
    }

}
