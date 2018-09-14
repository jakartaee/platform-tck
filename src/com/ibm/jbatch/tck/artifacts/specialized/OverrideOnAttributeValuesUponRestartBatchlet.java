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

@javax.inject.Named("overrideOnAttributeValuesUponRestartBatchlet")
public class OverrideOnAttributeValuesUponRestartBatchlet extends AbstractBatchlet{

    private final static String sourceClass = OverrideOnAttributeValuesUponRestartBatchlet.class.getName();
    private final static Logger logger = Logger.getLogger(sourceClass);

    @Inject
    StepContext stepCtx;

    @Inject
    JobContext jobCtx;
    
    @Inject    
    @BatchProperty(name="execution.number")
    String executionNumberString;

    int execNum;
    String stepName;
    
    /*
     * Appends "intended.exit.status" property to the current Job-level ExitStatus
     */
    @Override
    public String process() throws Exception {
    	execNum = Integer.parseInt(executionNumberString);
    	stepName = stepCtx.getStepName();
        
        logger.fine("execution # = " + execNum + ", step = " + stepName);
        
        String exitStatus = calculateExitStatus();
        
        logger.fine("Exiting with exitStatus = " + exitStatus);
        return exitStatus;
    }
    
    private String calculateExitStatus() {
        /*
         * Tests that stop @on and end @on don't result in re-running already completed steps.
         */
        if (stepName.equals("step1")) {
            switch (execNum) {
                case 1: return "ES.STEP1";
                default: return "ILLEGAL.STATE";
            }
        } else if (stepName.equals("step2")) {
            switch (execNum) {
                case 2: return "ES.STEP2";
                default: return "ILLEGAL.STATE";
            }
        } else if (stepName.equals("step3")) {
            switch (execNum) {
                case 3: return "ES.STEP3";
                default: return "ILLEGAL.STATE";
            }
        } else {
            return "ILLEGAL.STATE";
        }
    }

    @Override
    public void stop() throws Exception {
        // Do nothing since this is too quick to bother canceling.
        logger.fine(sourceClass + ".cancel()");		
    }

}
