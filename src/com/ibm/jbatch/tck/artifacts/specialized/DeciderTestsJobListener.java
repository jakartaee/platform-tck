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

import javax.batch.api.listener.AbstractJobListener;
import javax.batch.runtime.context.JobContext;
import javax.inject.Inject;

import com.ibm.jbatch.tck.artifacts.common.StatusConstants;

@javax.inject.Named
public class DeciderTestsJobListener extends AbstractJobListener implements StatusConstants {

	public static final String SUCCESS = "SUCCESS";
	
    @Inject
    JobContext jobCtx;

	@Override
	public void afterJob() {
		Integer count = (Integer)jobCtx.getTransientUserData();
		if (!count.equals(new Integer(2))) {
			jobCtx.setExitStatus(UNEXPECTED);
			throw new IllegalStateException("TCK Test Failure: Expecting two steps to have run.");
		} else {
			jobCtx.setExitStatus(GOOD_JOB_EXIT_STATUS);
		}
	}
}
