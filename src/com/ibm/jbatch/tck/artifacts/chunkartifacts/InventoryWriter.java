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
import java.sql.SQLException;
import java.util.List;

import javax.batch.api.BatchProperty;
import javax.batch.api.chunk.AbstractItemWriter;
import javax.inject.Inject;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.ibm.jbatch.tck.artifacts.chunktypes.InventoryRecord;

@javax.inject.Named("inventoryWriter")
public class InventoryWriter extends AbstractItemWriter {

    protected DataSource dataSource = null;

    @Inject
    @BatchProperty(name = "forced.fail.count")
    String forcedFailCountProp;

    @Inject
    @BatchProperty(name = "dummy.delay.seconds")
    String dummyDelayProp;

    int forcedFailCount, dummyDelay = -1;

    int writerIndex = 0; // the number of items that have already been written

    @Override
    public void open(Serializable cpd) throws NamingException {

        InitialContext ctx = new InitialContext();
        dataSource = (DataSource) ctx.lookup(ConnectionHelper.jndiName);

        forcedFailCount = Integer.parseInt(forcedFailCountProp);
        dummyDelay = Integer.parseInt(dummyDelayProp);

    }

    @Override
    public void writeItems(List<Object> records) throws Exception {


        int itemID = -1;
        int quantity = -1;

        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = ConnectionHelper.getConnection(dataSource);

            for (Object record : records) {
                itemID = ((InventoryRecord)record).getItemID();
                quantity = ((InventoryRecord)record).getQuantity();

                statement = connection.prepareStatement(ConnectionHelper.INSERT_ORDER);
                statement.setInt(1, itemID);
                statement.setInt(2, quantity);
                int rs = statement.executeUpdate();

                writerIndex++;
                
                if (forcedFailCount != 0 && writerIndex >= forcedFailCount) {
                    // after writing up to the forced fail number force a dummy delay
                    if (dummyDelay > 0) {
                        Thread.sleep(dummyDelay); // sleep for dummyDelay seconds to
                                                  // force a tran timeout - or to show that one does not hit?
                        forcedFailCount = 0;
                        dummyDelay = 0;
                    } else {
                        throw new Exception("Fail on purpose in InventoryRecord.readItem()");
                    }
                }
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            ConnectionHelper.cleanupConnection(connection, null, statement);
        }
    }

}
