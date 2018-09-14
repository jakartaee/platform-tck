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

import javax.batch.api.listener.AbstractJobListener;
import javax.batch.runtime.context.JobContext;
import javax.inject.Inject;
import javax.inject.Named;

@Named
public class ThreadTrackingJobListener extends AbstractJobListener {
	
	private final static Logger logger = Logger.getLogger(ThreadTrackingJobListener.class.getName());
	
	public final static String GOOD_EXIT = "GOOD_EXIT";
	
    @Inject 
    private JobContext jobCtx = null; 
    
	@Override
	public void beforeJob() throws Exception {
		jobCtx.setTransientUserData(Thread.currentThread());
	}

	@Override
	public void afterJob() throws Exception {
		Thread t = (Thread)jobCtx.getTransientUserData();
		if (t == null) {
			jobCtx.setExitStatus(GOOD_EXIT);
		} else {
			logger.warning("Failing test in current thread:" + Thread.currentThread());
			throw new IllegalStateException("Failing test in current thread:" + Thread.currentThread());
		}
	}
}
