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

import com.ibm.jbatch.tck.artifacts.common.StatusConstants;

@javax.inject.Named("deciderTestsBatchlet")
public class DeciderTestsBatchlet extends AbstractBatchlet implements StatusConstants {

	public final static String NORMAL_VALUE = "21";
	public final static String SPECIAL_VALUE = "25";
	public final static String ACTUAL_VALUE = "actualValue";
	public final static String ACTION = "action";
	public final static String SPECIAL_EXIT_STATUS = "SpecialExitStatus";
	
    private final static String sourceClass = DeciderTestsBatchlet.class.getName();
    private final static Logger logger = Logger.getLogger(sourceClass);

    @Inject
    StepContext stepCtx;

    @Inject
    JobContext jobCtx;

    @Inject    
    @BatchProperty(name=ACTION)
    String action;
    
    // Take a shortcut and use a String rather than an int.
    @Inject    
    @BatchProperty(name=ACTUAL_VALUE)
    String value1; 
        
    /**
     * The idea here is that the decider will act on a combination of the 'action'
     * property and the exit status.
     */
    @Override
    public String process() {
    	if (action != null) {
    		stepCtx.setPersistentUserData(action);
    	}
    	if (value1.equals(SPECIAL_VALUE)) {
    		after();
    		return SPECIAL_EXIT_STATUS;
    	} else {
    		after(); 
    		return GOOD_STEP_EXIT_STATUS;
    	}
    }
    
    @Override
    public void stop(){
        // Do nothing since this is too quick to bother canceling.
        logger.fine(sourceClass + ".cancel()");		
    }
    
    /*
     * Allow us to count how many times this step has run.
     */
    private void after() {
    	Integer count = (Integer)jobCtx.getTransientUserData();
    	if (count == null) {
    		count = new Integer(1);
    	} else {
    		count++;
    	}
    	jobCtx.setTransientUserData(count);    	
    }
}
