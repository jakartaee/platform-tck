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

import java.util.Properties;

import javax.batch.api.BatchProperty;
import javax.batch.api.chunk.ItemProcessor;
import javax.batch.runtime.context.StepContext;
import javax.inject.Inject;



import com.ibm.jbatch.tck.artifacts.chunktypes.NumbersRecord;
import com.ibm.jbatch.tck.artifacts.reusable.MyParentException;


@javax.inject.Named("retryProcessor")
public class RetryProcessor implements ItemProcessor {
	
	
    @Inject
    StepContext stepCtx;
	
    @Inject    
    @BatchProperty(name="forced.fail.count.process")
	String forcedFailCountProp;
    
    @Inject
    @BatchProperty(name="rollback")
	String rollbackProp;
	
    private static final int STATE_NORMAL = 0;
	private static final int STATE_RETRY = 1;
	private static final int STATE_SKIP = 2;
	private static final int STATE_EXCEPTION = 3;
	
	private int testState = STATE_NORMAL;
	
	int failitem = 0;
	int count = 1;
	int forcedFailCount;
	
	boolean isInited = false;
	boolean rollback;
	
	int failindex = 0;
	
	public NumbersRecord processItem(Object record) throws Exception {
		int item = ((NumbersRecord)record).getItem();
		int quantity = ((NumbersRecord)record).getQuantity();
		TestUtil.logMsg("Processing item: " + item + "...<br>");
		TestUtil.logMsg("Processing quantity: " + quantity + "...<p>");
		
		if(!isInited) {
			forcedFailCount = Integer.parseInt(forcedFailCountProp);
			rollback = Boolean.parseBoolean(rollbackProp);
			isInited = true;
		}
		
		// Throw an exception when forcedFailCount is reached
		if (forcedFailCount != 0 && count >= forcedFailCount && (testState == STATE_NORMAL)) {
				   //forcedFailCount = 0;
					failindex = count;
					testState = STATE_RETRY;
					TestUtil.logMsg("Fail on purpose in NumbersRecord.processItem<p>");
					throw new MyParentException("Fail on purpose in NumbersRecord.processItem()");	
		} else if (forcedFailCount != 0 && (count >= forcedFailCount) && (testState == STATE_EXCEPTION)) {
			failindex = count;
			testState = STATE_SKIP;
			forcedFailCount = 0;
			TestUtil.logMsg("Test skip -- Fail on purpose NumbersRecord.readItem<p>");
			throw new MyParentException("Test skip -- Fail on purpose in NumbersRecord.readItem()");	
		}
		
		if (testState == STATE_RETRY)
		{
			if (((Properties)stepCtx.getTransientUserData()).getProperty("retry.process.exception.invoked") != "true") {
				TestUtil.logMsg("onRetryProcessException not invoked<p>");
				throw new Exception("onRetryProcessException not invoked");
			} else {
				TestUtil.logMsg("onRetryProcessException was invoked<p>");
			}
			
			if (((Properties)stepCtx.getTransientUserData()).getProperty("retry.process.exception.match") != "true") {
				TestUtil.logMsg("retryable exception does not match<p>");
				throw new Exception("retryable exception does not match");
			} else {
				TestUtil.logMsg("retryable exception matches<p>");
			}
			
			testState = STATE_EXCEPTION;
		} else if(testState == STATE_SKIP) {
			if (((Properties)stepCtx.getTransientUserData()).getProperty("skip.process.item.invoked") != "true") {
				TestUtil.logMsg("onSkipProcessItem not invoked<p>");
				throw new Exception("onSkipProcessItem not invoked");
			} else {
				TestUtil.logMsg("onSkipProcessItem was invoked<p>");
			}
			
			if (((Properties)stepCtx.getTransientUserData()).getProperty("skip.process.item.match") != "true") {
				TestUtil.logMsg("skippable exception does not match<p>");
				throw new Exception("skippable exception does not match");
			} else {
				TestUtil.logMsg("skippable exception matches<p>");
			}
			testState = STATE_NORMAL;
		}
		
		
		quantity = quantity + 1;
		TestUtil.logMsg("Process [item: " + item + " -- new quantity: " + quantity + "]<p>");
		count++;
		
		return new NumbersRecord(item, quantity);
	}
	
}

