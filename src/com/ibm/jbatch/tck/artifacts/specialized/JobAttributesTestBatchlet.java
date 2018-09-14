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

import javax.batch.api.AbstractBatchlet;
import javax.batch.api.BatchProperty;
import javax.inject.Inject;

@javax.inject.Named("jobAttributesTestBatchlet")
public class JobAttributesTestBatchlet extends AbstractBatchlet {

    private final static String sourceClass = JobAttributesTestBatchlet.class.getName();
    private final static Logger logger = Logger.getLogger(sourceClass);
    
	public static String GOOD_EXIT_STATUS = "VERY GOOD INVOCATION"; 
	public static String BAD_EXIT_STATUS = "VERY BAD INVOCATION"; 
	
    @Inject    
    @BatchProperty(name="execution.number")
    String executionNumberString;
    
	
    @Inject    
    @BatchProperty(name="execution")
    String executionString;
    
	@Override
	public String process() throws Exception {

		int execNum = Integer.parseInt(executionNumberString);
		
		if (execNum == 1) {
			logger.fine(sourceClass + ".process(); Purposefully failing on execution == 1");
			throw new IllegalArgumentException("Purposefully failing on execution == 1");
		} else if (execNum == 2) {
			logger.fine(sourceClass + ".process(); Success...exit normally");
			return GOOD_EXIT_STATUS;
		} else {
			logger.fine(sourceClass + ".process(); Unexpected count, return bad exit status");
			return BAD_EXIT_STATUS;
		}
	}
	
	@Override
	public void stop() throws Exception {
		super.stop();
	}
}
