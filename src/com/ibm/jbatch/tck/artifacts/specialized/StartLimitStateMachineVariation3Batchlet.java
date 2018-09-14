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

import java.util.logging.Logger;

import javax.batch.api.AbstractBatchlet;
import javax.batch.api.BatchProperty;
import javax.batch.runtime.context.JobContext;
import javax.batch.runtime.context.StepContext;
import javax.inject.Inject;

@javax.inject.Named("startLimitStateMachineVariation3Batchlet")
public class StartLimitStateMachineVariation3Batchlet extends AbstractBatchlet {

	private final static String sourceClass = StartLimitStateMachineVariation3Batchlet.class.getName();
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

		// This is the ongoing appending to the JOB exit status
		String exitStatus = null;
		exitStatus = contributeToExitStatus();
		appendToJobContext(exitStatus);

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

	private String contributeToExitStatus() {
		logger.fine(sourceClass + ".calculateExitStatus(), runNumberString = " + runNumberString);

		// runNumberString = runNumber.N
		int execNum = Integer.parseInt(runNumberString.substring(new String("runNumber.").length()));

		String stepId = stepCtx.getStepName();

		logger.fine(sourceClass + ".calculateExitStatus(), execution # = " + execNum + ", stepId = " + stepId);

		/*
		 * Run 1 - c1, c2, c3, c4, s5,restart=5
		 * Run 2 -                 f5
		 * Run 3 - c1, c2,     c4  c5 
		 */
		if (stepId.equals("step1")) {
			switch (execNum) {
			case 1: return "c1";
			case 3: return "c1";
			default: return "ILLEGAL.STATE";
			}
		} else if (stepId.equals("step2")) {
			switch (execNum) {
			case 1: return "c2";
			case 3: return "c2";
			default: return "ILLEGAL.STATE";
			}
		} else if (stepId.equals("step3")) {
			switch (execNum) {
			case 1: return "c3";
			default: return "ILLEGAL.STATE";
			}
		} else if (stepId.equals("step4")) {
			switch (execNum) {
			case 1: return "c4";
			case 3: return "c4";
			default: return "ILLEGAL.STATE";
			}
		} else if (stepId.equals("step5")) {
			switch (execNum) {
			case 1: return "s5";
			case 2: return "f5";
			case 3: return "c5";
			default: return "ILLEGAL.STATE";
			}
		} else {
			return "ILLEGAL.STATE";
		}
	}
}
