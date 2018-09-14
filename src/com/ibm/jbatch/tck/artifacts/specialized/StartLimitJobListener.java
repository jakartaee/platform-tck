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
package com.ibm.jbatch.tck.artifacts.specialized;

import com.sun.ts.lib.harness.*;
import com.sun.ts.lib.util.TestUtil;

import java.util.logging.Logger;

import javax.batch.api.BatchProperty;
import javax.batch.api.listener.AbstractJobListener;
import javax.batch.runtime.context.JobContext;
import javax.inject.Inject;
import javax.inject.Named;

@Named
public class StartLimitJobListener extends AbstractJobListener {

	private final static String sourceClass = StartLimitJobListener.class.getName();
	private final static Logger logger = Logger.getLogger(sourceClass);

	@Inject
	JobContext jobCtx;
	
	@Inject
	@BatchProperty(name="ExitCodeAppend")
	String errorExitStatusCode;

	@Override
	public void beforeJob() throws Exception {
		jobCtx.setTransientUserData(errorExitStatusCode);
		logger.fine("Setting error exit status Code = " + errorExitStatusCode);
	}

}
