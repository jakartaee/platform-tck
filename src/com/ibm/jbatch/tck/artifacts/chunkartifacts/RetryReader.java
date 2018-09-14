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
import java.util.Properties;
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


@javax.inject.Named("retryReader")
public class RetryReader extends AbstractItemReader {

	private static final String CLASSNAME = NumbersReader.class.getName();
	private final static Logger logger = Logger.getLogger(CLASSNAME);
	

	protected DataSource dataSource = null;
	
	private static final int STATE_NORMAL = 0;
	private static final int STATE_RETRY = 1;
	private static final int STATE_SKIP = 2;
	private static final int STATE_EXCEPTION = 3;
	
	private int testState = STATE_NORMAL;
	
    @Inject
    StepContext stepCtx;
    
    @Inject    
    @BatchProperty(name="forced.fail.count.read")
	String forcedFailCountProp;
    
    @Inject
    @BatchProperty(name="rollback")
	String rollbackProp;
    

	int forcedFailCount, expectedReaderChkp = -1;
	boolean rollback;
	boolean didRetry;

	int readerIndex = 1;
	
	int failindex = 0;
	
	NumbersCheckpointData numbersCheckpoint = new NumbersCheckpointData();
	
	public void open(Serializable cpd) throws NamingException {
	    NumbersCheckpointData numbersCheckpointData = (NumbersCheckpointData)cpd;
	    
		forcedFailCount = Integer.parseInt(forcedFailCountProp);
		rollback = Boolean.parseBoolean(rollbackProp);
		
		InitialContext ctx = new InitialContext();
		dataSource = (DataSource) ctx.lookup(RetryConnectionHelper.jndiName);
		
		initializeUserDataWithProperties();
		
		if (cpd != null) {
			this.readerIndex = numbersCheckpointData.getCount();
			((Properties)stepCtx.getTransientUserData()).setProperty("init.checkpoint", this.readerIndex + "");
		} 	
	}

	@Override
	public NumbersRecord readItem() throws Exception {
		int i = readerIndex;
		
		TestUtil.logMsg("Reading item: " + readerIndex + "...<br>");

		// Throw an exception when forcedFailCount is reached
		if (forcedFailCount != 0 && (readerIndex >= forcedFailCount) && (testState == STATE_NORMAL)) {
			    //forcedFailCount = 0;
			    failindex = readerIndex;
			    testState = STATE_RETRY;
			    TestUtil.logMsg("Fail on purpose NumbersRecord.readItem<p>");
				throw new MyParentException("Fail on purpose in NumbersRecord.readItem()");	
				
		} else if (forcedFailCount != 0 && (readerIndex >= forcedFailCount) && (testState == STATE_EXCEPTION)) {
			failindex = readerIndex;
			testState = STATE_SKIP;
			forcedFailCount = 0;
			TestUtil.logMsg("Test skip -- Fail on purpose NumbersRecord.readItem<p>");
			throw new MyParentException("Test skip -- Fail on purpose in NumbersRecord.readItem()");	
		}
		
		
		if (testState == STATE_RETRY)
		{
			// should be retrying at same index with no rollback
			/*if(!rollback) {
					if(failindex != readerIndex)
						throw new Exception("Error reading data.  Expected to be at index " + failindex + " but got index " + readerIndex);
			}
						
			// should be retrying at last checkpoint with rollback
			else {
						
					int checkpointIndex = Integer.parseInt((Properties)stepCtx.getTransientUserData()).getProperty("checkpoint.index"));
							
					if(checkpointIndex != readerIndex)
						throw new Exception("Error reading data.  Expected to be at index " + checkpointIndex + " but got index " + readerIndex);
			}*/
			
			if (((Properties)stepCtx.getTransientUserData()).getProperty("retry.read.exception.invoked") != "true") {
				TestUtil.logMsg("onRetryReadException not invoked<p>");
				throw new Exception("onRetryReadException not invoked");
			} else {
				TestUtil.logMsg("onRetryReadException was invoked<p>");
			}
			
			if (((Properties)stepCtx.getTransientUserData()).getProperty("retry.read.exception.match") != "true") {
				TestUtil.logMsg("retryable exception does not match<p>");
				throw new Exception("retryable exception does not match");
			} else {
				TestUtil.logMsg("Retryable exception matches<p>");
			}
			testState = STATE_EXCEPTION;
			//TestUtil.logMsg("Test skip after retry -- Fail on purpose in NumbersRecord.readItem<p>");
			//throw new MyParentException("Test skip after retry -- Fail on purpose in NumbersRecord.readItem()");	
		}
		else if(testState == STATE_SKIP) {
			if (((Properties)stepCtx.getTransientUserData()).getProperty("skip.read.item.invoked") != "true") {
				TestUtil.logMsg("onSkipReadItem not invoked<p>");
				throw new Exception("onSkipReadItem not invoked");
			} else {
				TestUtil.logMsg("onSkipReadItem was invoked<p>");
			}
			
			if (((Properties)stepCtx.getTransientUserData()).getProperty("skip.read.item.match") != "true") {
				TestUtil.logMsg("skippable exception does not match<p>");
				throw new Exception("skippable exception does not match");
			} else {
				TestUtil.logMsg("skippable exception matches<p>");
			}
			testState = STATE_NORMAL;
		}
		

		if (readerIndex > 20) {
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
			TestUtil.logMsg("Read [item: " + i + " quantity: " + quantity + "]<p>");
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
		((Properties)stepCtx.getTransientUserData()).setProperty("checkpoint.index", Integer.toString(readerIndex));
		return _chkptData; 
	}

	// Since this is a late change to the TCK, we'll be a bit more lenient than we might and not worry
	// about when exactly the Properties object should or shouldn't already be in the userdata.  We'll just 
	// create if needed and if not continue on.
	private void initializeUserDataWithProperties() {
		if (stepCtx.getTransientUserData() == null) {
			stepCtx.setTransientUserData(new Properties());
		}
	}

}

