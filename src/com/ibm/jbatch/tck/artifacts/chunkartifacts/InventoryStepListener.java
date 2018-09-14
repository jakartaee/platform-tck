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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

import javax.batch.api.listener.AbstractStepListener;
import javax.batch.runtime.context.JobContext;
import javax.batch.runtime.context.StepContext;
import javax.inject.Inject;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.ibm.jbatch.tck.artifacts.common.StatusConstants;

@javax.inject.Named("inventoryStepListener")
public class InventoryStepListener extends AbstractStepListener implements StatusConstants {

    private final static String sourceClass = InventoryStepListener.class.getName();
    private final static Logger logger = Logger.getLogger(sourceClass);

    @Inject
    StepContext stepCtx;

    @Inject
    JobContext jobCtx;

    //protected String jndiName = "jdbc/orderDB";

    protected DataSource dataSource = null;

    private void init() throws NamingException {
        if (dataSource == null) {
            InitialContext ctx = new InitialContext();
            dataSource = (DataSource) ctx.lookup(ConnectionHelper.jndiName);
        }
    }

    @Override
    public void afterStep() throws Exception {
        logger.fine("afterStep");

        int finalInventoryCount = this.getInventoryCount();
        int orderCount = this.getOrderCount();
		// Fix for Bug 5490:    https://java.net/bugzilla/show_bug.cgi?id=5490
        String initCheckpoint = String.valueOf((Integer)stepCtx.getTransientUserData());

        String exitStatus = "Inventory=" + finalInventoryCount + " InitialCheckpoint=" + initCheckpoint + " OrderCount="+orderCount;
        jobCtx.setExitStatus(exitStatus);
    }

    
    private int getInventoryCount() throws Exception {

        this.init();

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

            return quantity;

        } catch (SQLException e) {
            throw e;
        } finally {
            ConnectionHelper.cleanupConnection(connection, rs, statement);
        }

    }

    private int getOrderCount() throws Exception {

        this.init();

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet rs = null;

        try {
            connection = ConnectionHelper.getConnection(dataSource);

            statement = connection.prepareStatement(ConnectionHelper.COUNT_ORDERS);
            rs = statement.executeQuery();

            int count = 0;
            while (rs.next()) {
                count = rs.getInt("rowcount");
            }

            return count;

        } catch (SQLException e) {
            throw e;
        } finally {
            ConnectionHelper.cleanupConnection(connection, rs, statement);
        }

    }
    
    @Override
    public void beforeStep() {
    }


}
