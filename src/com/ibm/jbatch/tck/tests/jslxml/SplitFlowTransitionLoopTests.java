/*
 * Copyright 2013 International Business Machines Corp.
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
package com.ibm.jbatch.tck.tests.jslxml;

import com.sun.ts.lib.harness.*;
import com.sun.ts.lib.util.TestUtil;

import static com.ibm.jbatch.tck.utils.AssertionUtils.assertWithMessage;

import java.util.Properties;

import javax.batch.runtime.BatchStatus;
import javax.batch.runtime.JobExecution;







import com.ibm.jbatch.tck.utils.JobOperatorBridge;

public class SplitFlowTransitionLoopTests extends ServiceEETest {

	private JobOperatorBridge jobOp = null;

	/**
	 * @testName: testSplitFlowTransitionLoopSplitFlowSplit
	 * @assertion: Section 5.3 Flow
	 * @test_Strategy: 1. setup a job consisting of one split (w/ 2 flows) one of the inner flows has a split (w/ 2 more flows)
	 * 				   2. create a list of steps in job
	 * 				   3. start job 
	 *                 4. batchlet artifact saves step id in temp file
	 *                 5. read file and make sure all visited steps are in the list of steps
	 *                 
	 *                 ** split spawns jobs to multiple threads, therefore steps might not run in order listed.
	 * 
	 * <split id="split1">
	 *    <flow id="split1Flow" next="flow2">
	 *		<step id="split1FlowStep1" next="split1FlowSplit">
	 *			<batchlet ref="splitFlowTransitionLoopTestBatchlet"/>
	 *		</step>
	 *		<split id="split1FlowSplit" next="split1FlowStep2">
	 *			<flow id="split1FlowSplitFlow1">
	 *				<step id="split1FlowSplitFlow1Step">
	 *					<batchlet ref="splitFlowTransitionLoopTestBatchlet"/>
	 *				</step>
	 *			</flow>
	 *			<flow id="split1FlowSplitFlow2">
	 *				<step id="split1FlowSplitFlow2Step">
	 *					<batchlet ref="splitFlowTransitionLoopTestBatchlet"/>
	 *				</step>
	 *			</flow>
	 *		</split>
	 *		<step id="split1FlowStep2">
	 *			<batchlet ref="splitFlowTransitionLoopTestBatchlet"/>
	 *		</step>
	 *	</flow>
	 *  <flow id="flow2">
	 *		<step id="flow2step1" next="flow2step2">
	 *			<batchlet ref="splitFlowTransitionLoopTestBatchlet"/>
	 *		</step>
	 *		<step id="flow2step2">
	 *			<batchlet ref="splitFlowTransitionLoopTestBatchlet"/>
	 *		</step>
	 *	</flow>
	 * </split>
	 *
	 * @throws Fault
	 */
	
	
	public void testSplitFlowTransitionLoopSplitFlowSplit() throws Fault {

		String METHOD = "testSplitFlowTransitionLoopSplitFlowSplit";
TestUtil.logTrace(METHOD);

		try {
			TestUtil.logMsg("starting job");
			JobExecution jobExec = jobOp.startJobAndWaitForResult("split_flow_transition_loop_splitflowsplit", null);
			TestUtil.logMsg("Job Status = " + jobExec.getBatchStatus());

			assertWithMessage("Job completed", BatchStatus.COMPLETED, jobExec.getBatchStatus());
			TestUtil.logMsg("job completed");
		} catch (Exception e) {
			handleException(METHOD, e);
		}
	}

	private static void handleException(String methodName, Exception e) throws Fault {
TestUtil.logErr("Caught exception: "           + e.getMessage());
TestUtil.printStackTrace(e);
throw new Fault(methodName + " failed", e);}

	/* cleanup */
	public void  cleanup()
	{		

	}

	public void setup(String[] args, Properties props) throws Fault {

		String METHOD = "setup";
TestUtil.logTrace(METHOD);

		try {
			jobOp = new JobOperatorBridge();
		} catch (Exception e) {
			handleException(METHOD, e);
		}
	}

	
		public void beforeTest() throws ClassNotFoundException {
		jobOp = new JobOperatorBridge(); 
	}

	
	public void afterTest() {
		jobOp = null;
	}
}
