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
package com.ibm.jbatch.tck.artifacts.specialized;

import com.sun.ts.lib.harness.*;
import com.sun.ts.lib.util.TestUtil;

import java.util.Random;
import java.util.logging.Logger;

import javax.batch.api.AbstractBatchlet;
import javax.batch.api.BatchProperty;
import javax.batch.runtime.context.JobContext;
import javax.batch.runtime.context.StepContext;
import javax.inject.Inject;

@javax.inject.Named("startLimitStateMachineVariation2Batchlet")
public class StartLimitStateMachineVariation2Batchlet extends AbstractBatchlet {

	private final static String sourceClass = StartLimitStateMachineVariation2Batchlet.class.getName();
	private final static Logger logger = Logger.getLogger(sourceClass);

	private final static String JSL_STOP_TRANSITION = "JSL.STOP";
	private final static String JSL_FAIL_TRANSITION = "JSL.FAIL";
	
	@Inject
	JobContext jobCtx;

	@Inject
	StepContext stepCtx;
	
	@Inject    
	@BatchProperty(name="runNumber")
	String runNumberString;

	@Override
	public String process() throws Exception {
		logger.fine(sourceClass + ".process()");

		// Do something a bit "compute-intensive".
		Random r = new Random();
		int x = r.nextInt(); int y = r.nextInt();
		for (int i = 0 ; i < 10; i++) {
			x = (y * x) % 3469;
			y = (x * y) % 3491;
		}
		
		// This is the ongoing appending to the JOB exit status
		String exitStatus = null;
		try {
			exitStatus = contributeToExitStatus();
			appendToJobContext(exitStatus);
		} catch (FailViaException e) {
			logger.fine(sourceClass + ".process(); Exiting with exitStatus = " + exitStatus);
			String errorAppend = (String)jobCtx.getTransientUserData();
			appendToJobContext(errorAppend);
			throw new RuntimeException("Throwing exception on purpose");
		}
		
		logger.fine(sourceClass + ".process(); exitStatus = " + exitStatus);
		
		// Now we're going to switch gears and consider the step exit Status
		if (exitStatus.startsWith("s")) {
			logger.fine("For step exitStatus for step: " + stepCtx.getStepName() + " , return: " + JSL_STOP_TRANSITION);
			return JSL_STOP_TRANSITION;
		} else if (exitStatus.startsWith("f")) {
			logger.fine("For step exitStatus for step: " + stepCtx.getStepName() + " , return: " + JSL_FAIL_TRANSITION);
			return JSL_FAIL_TRANSITION;
		} else {
			logger.fine("For step exitStatus for step: " + stepCtx.getStepName() + " , don't return value");
			return null;
		}
	}

	private void appendToJobContext(String exitStatus) {
		String es = jobCtx.getExitStatus();
		if (es == null) {
			logger.fine("First addition to Job ExitStatus = " + es);
			jobCtx.setExitStatus(exitStatus);
		} else {
			String newExitStatus = es.concat(",").concat(exitStatus);
			logger.fine("Existing Job ExitStatus = " + es + " ; Updating to : " + newExitStatus);
			jobCtx.setExitStatus(newExitStatus);
		}
	}

	@Override
	public void stop() throws Exception {
		// Do nothing since this is too quick to bother canceling.
		logger.fine(sourceClass + ".cancel()");		
	}

	private String contributeToExitStatus() throws FailViaException {
		logger.fine(sourceClass + ".calculateExitStatus(), runNumberString = " + runNumberString);

		// runNumberString = runNumber.N
		int execNum = Integer.parseInt(runNumberString.substring(new String("runNumber.").length()));

		String stepId = stepCtx.getStepName();

		logger.fine(sourceClass + ".calculateExitStatus(), execution # = " + execNum + ", stepId = " + stepId);

		if (stepId.equals("step1")) {
			switch (execNum) {
			case 1: return "c1";
			case 2: return "c1";
			case 3: return "c1";
			case 4: return "c1";
			case 5: return "ILLEGAL.STATE";
			case 6: return "c1";
			case 7: return "c1";
			default: return "ILLEGAL.STATE";
			}
		} else if (stepId.equals("step2")) {
			switch (execNum) {
			case 1: return "c2";
			case 2: return "c2";
			case 3: return "c2";
			case 4: return "c2";
			case 5: return "ILLEGAL.STATE";
			case 6: return "c2";
			case 7: return "c2";
			default: return "ILLEGAL.STATE";
			}
		} else if (stepId.equals("step3")) {
			switch (execNum) {
			case 1: throw new FailViaException();
			case 2: throw new FailViaException();
			case 3: return "s3";
			case 4: return "ILLEGAL.STATE";
			case 5: return "ILLEGAL.STATE";
			case 6: return "ILLEGAL.STATE";
			case 7: return "ILLEGAL.STATE";
			default: return "ILLEGAL.STATE";
			}
		} else if (stepId.equals("step4")) {
			switch (execNum) {
			case 1: return "ILLEGAL.STATE";
			case 2: return "ILLEGAL.STATE";
			case 3: return "ILLEGAL.STATE";
			case 4: return "s4";
			case 5: return "f4";
			case 6: return "c4";
			// Should not be able to rexecute a fourth time on run 7
			default: return "ILLEGAL.STATE";
			}
		} else if (stepId.equals("step5")) {
			switch (execNum) {
			// Only gets to run on run #6
			case 6: return "f5";
			default: return "ILLEGAL.STATE";
			}
		} else {
			return "ILLEGAL.STATE";
		}
	}
	
	private class FailViaException extends Exception {
		private FailViaException() {
			super();
		}
	}
}
