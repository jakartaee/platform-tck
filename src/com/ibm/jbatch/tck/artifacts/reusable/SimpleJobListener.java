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
package com.ibm.jbatch.tck.artifacts.reusable;

import com.sun.ts.lib.harness.*;
import com.sun.ts.lib.util.TestUtil;

import java.util.logging.Logger;

import javax.batch.api.BatchProperty;
import javax.batch.api.listener.AbstractJobListener;
import javax.batch.runtime.context.JobContext;
import javax.inject.Inject;
import javax.inject.Named;

@Named
public class SimpleJobListener extends AbstractJobListener {
	
	private final static Logger logger = Logger.getLogger(SimpleJobListener.class.getName());
	
    @Inject 
    private JobContext jobCtx = null; 
    
    @Inject    
    @BatchProperty(name="setTransientData")
    String setTransientDataString;
    
	@Override
	public void beforeJob() throws Exception {
		if (setTransientDataString.equals("true")) {
			logger.fine("Setting transient data: ");
			jobCtx.setTransientUserData("FROM_BEFORE_JOB");
		} else {
			logger.fine("setTransientDataString prop = " + setTransientDataString + "; Not setting transient data.");
		}
	}

	@Override
	public void afterJob() throws Exception {
		Object transientData = jobCtx.getTransientUserData();
		if (transientData instanceof String) {
			logger.fine("Found String transient data, setting into exit status");
			jobCtx.setExitStatus((String)transientData);
		} else {
			logger.fine("Found non-String transient data, leaving exit status alone");
		}
	}
}
