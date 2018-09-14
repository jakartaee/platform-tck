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

import javax.batch.api.listener.AbstractStepListener;
import javax.batch.runtime.context.JobContext;
import javax.inject.Inject;
import javax.inject.Named;

@Named
public class ThreadTrackingStepListener extends AbstractStepListener {
	
	@Inject 
	private JobContext jobCtx = null; 

	@Override
	public void beforeStep() throws Exception {
		Thread t = (Thread)jobCtx.getTransientUserData();
		if (t == null) {
			throw new IllegalStateException("In beforeStep() expected job listener to have already set this.  Are we not on the same thread?");
		} else if (!t.equals(Thread.currentThread())) {
			throw new IllegalStateException("Current thread = " + Thread.currentThread().toString() + ", but in transient data found " + t);
		}
	}

	@Override
	public void afterStep() throws Exception {
		Thread t = (Thread)jobCtx.getTransientUserData();
		if (t == null) {
			throw new IllegalStateException("In afterStep() expected job listener to have already set this.  Are we not on the same thread?");
		} else if (!t.equals(Thread.currentThread())) {
			throw new IllegalStateException("Current thread = " + Thread.currentThread().toString() + ", but in transient data found " + t);
		}
		jobCtx.setTransientUserData(null);
	}
}
