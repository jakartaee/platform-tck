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
import java.util.logging.Logger;

import javax.batch.api.BatchProperty;
import javax.batch.api.chunk.AbstractItemReader;
import javax.batch.runtime.context.StepContext;
import javax.inject.Inject;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;



import com.ibm.jbatch.tck.artifacts.chunktypes.NumbersCheckpointData;
import com.ibm.jbatch.tck.artifacts.chunktypes.NumbersRecord;
import com.ibm.jbatch.tck.artifacts.reusable.MyParentException;


@javax.inject.Named("numbersReader")
public class NumbersReader extends AbstractItemReader {

	private static final String CLASSNAME = NumbersReader.class.getName();
	private final static Logger logger = Logger.getLogger(CLASSNAME);
	

	protected DataSource dataSource = null;
	
	private static final int STATE_NORMAL = 0;
	private static final int STATE_RETRY = 1;
	private int testState = STATE_NORMAL;
	
    @Inject
    StepContext stepCtx;
    
    @Inject    
    @BatchProperty(name="forced.fail.count.read")
	String forcedFailCountProp;

	int forcedFailCount, dummyDelay, expectedReaderChkp = -1;

	int readerIndex = 1;
	
	int failindex = 0;
	
	NumbersCheckpointData numbersCheckpoint = new NumbersCheckpointData();
	
	public void open(Serializable cpd) throws NamingException {
		
	    NumbersCheckpointData numbersCheckpointData = (NumbersCheckpointData)cpd;
	    
		forcedFailCount = Integer.parseInt(forcedFailCountProp);
		
		InitialContext ctx = new InitialContext();
		dataSource = (DataSource) ctx.lookup(RetryConnectionHelper.jndiName);
		
		
		if (cpd != null) {
			forcedFailCount = 0;
			this.readerIndex = numbersCheckpointData.getCount();
			stepCtx.getProperties().setProperty("init.checkpoint", this.readerIndex + "");
		} 	
	}

	@Override
	public NumbersRecord readItem() throws Exception {
		int i = readerIndex;
		
		// Throw an exception when forcedFailCount is reached
		if (forcedFailCount != 0 && readerIndex >= forcedFailCount) {
			    forcedFailCount = 0;
			    failindex = readerIndex;
			    testState = STATE_RETRY;
			    TestUtil.logMsg("Fail on purpose NumbersRecord.readItem<p>");
				throw new MyParentException("Fail on purpose in NumbersRecord.readItem()");	
		} 
		
		if (testState == STATE_RETRY)
		{
			if(stepCtx.getProperties().getProperty("retry.read.exception.invoked") != "true") {
				TestUtil.logMsg("onRetryReadException not invoked<p>");
				throw new Exception("onRetryReadException not invoked");
			}
			
			if(stepCtx.getProperties().getProperty("retry.read.exception.match") != "true") {
				TestUtil.logMsg("retryable exception does not match");
				throw new Exception("retryable exception does not match");
			}
			
			testState = STATE_NORMAL;
		}

		if (readerIndex > 5) {
			return null;
		}

		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		
		try {
			connection = RetryConnectionHelper.getConnection(dataSource);

			statement = connection.prepareStatement(RetryConnectionHelper.SELECT_NUMBERS);
			statement.setInt(1, readerIndex);
			rs = statement.executeQuery();

			int quantity = -1;
			while (rs.next()) {
				quantity = rs.getInt("quantity");
			}
					
			readerIndex++;
			
			return new NumbersRecord(i, quantity);
		} catch (SQLException e) {
			throw e;
		} finally {
			RetryConnectionHelper.cleanupConnection(connection, rs, statement);
		}

	}

	@Override
	 public Serializable checkpointInfo() throws Exception {
		 NumbersCheckpointData _chkptData = new  NumbersCheckpointData();
		_chkptData.setCount(readerIndex);
		stepCtx.getProperties().setProperty("checkpoint.index", Integer.toString(readerIndex));
		return _chkptData; 
	}



}
