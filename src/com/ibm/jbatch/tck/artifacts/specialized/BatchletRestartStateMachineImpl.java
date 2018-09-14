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

import java.util.Random;
import java.util.logging.Logger;

import javax.batch.api.AbstractBatchlet;
import javax.batch.api.BatchProperty;
import javax.batch.runtime.context.StepContext;
import javax.inject.Inject;

@javax.inject.Named("batchletRestartStateMachineImpl")
public class BatchletRestartStateMachineImpl extends AbstractBatchlet {

    private final static String sourceClass = BatchletRestartStateMachineImpl.class.getName();
    private final static Logger logger = Logger.getLogger(sourceClass);
 
    @Inject
    StepContext stepCtx;

    @Inject    
    @BatchProperty(name="execution.number")
    String executionNumberString;

    @Override
    public String process() throws Exception {
        logger.fine(sourceClass + ".process()");

        ClassLoader cl = BatchletRestartStateMachineImpl.class.getClassLoader();
        
        // Do something a bit "compute-intensive".
        Random r = new Random();
        int x = r.nextInt();
        int y = r.nextInt();
        logger.fine(sourceClass + ".process(); Before (x,y)=(" + x + "," + y + ")");
        for (int i = 0 ; i < 10; i++) {
            x = (y * x) % 3469;
            y = (x * y) % 3491;
        }
        logger.fine(sourceClass + ".process(); After (x,y)=(" + x + "," + y + ")");

        String exitStatus = calculateExitStatus();
        
        logger.fine(sourceClass + ".process(); Exiting with exitStatus = " + exitStatus);
        
        return exitStatus;
    }

    @Override
    public void stop() throws Exception {
        // Do nothing since this is too quick to bother canceling.
        logger.fine(sourceClass + ".cancel()");		
    }


    private String calculateExitStatus() {

        logger.fine(sourceClass + ".calculateExitStatus(), executionNumberString = " + executionNumberString);
        
        int execNum = Integer.parseInt(executionNumberString);

        String stepId = stepCtx.getStepName();
        
        logger.fine(sourceClass + ".calculateExitStatus(), execution # = " + execNum + ", stepId = " + stepId);
        
        if (stepId.equals("step1")) {
            switch (execNum) {
                case 1: return "STOP.1";
                case 2: return "STOP.2";
                case 4: return "GO";
                case 5: return "STOP.5";
                default: return "ILLEGAL.STATE";
            }
        } else if (stepId.equals("step2")) {
            switch (execNum) {
                case 3: return "GO";
                default: return "ILLEGAL.STATE";
            }
        } else if (stepId.equals("step3")) {
            switch (execNum) {
                case 3: return "STOP.3";
                case 4: return "STOP.4";
                case 6: return "GO";
                default: return "ILLEGAL.STATE";
            }
        } else if (stepId.equals("step4")) {
            switch (execNum) {
                case 6: return "GO";
                default: return "ILLEGAL.STATE";
            }
        } else {
            return "ILLEGAL.STATE";
        }
    }
}
