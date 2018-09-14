/**
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

import javax.batch.api.BatchProperty;
import javax.batch.api.Decider;
import javax.batch.runtime.StepExecution;
import javax.batch.runtime.context.JobContext;
import javax.inject.Inject;

@javax.inject.Named("transitionDecider")
public class TransitionDecider implements Decider {

    @Inject
    JobContext jobCtx;
    
    @Inject    
    @BatchProperty(name="is.restart")
    String isRestart; 
    
    @Override
    public String decide(StepExecution[] executions) throws Exception {

        String stepExitStatus = "";
        String stepName = "";
        
        
        for (StepExecution stepExec : executions) {
            String tempExitStatus = stepExec.getExitStatus();
            String tempStepName = stepExec.getStepName();
            
            //Always choose the alphabetically later step name and exit status so we can end the test deterministically
            if (stepExitStatus.compareTo(tempExitStatus) < 0) {
                stepExitStatus = tempExitStatus;
            }
            
            //
            if (stepName.compareTo(tempStepName) < 0){
                stepName = tempStepName;
            }
        }
        
        Integer deciderCount = jobCtx.getTransientUserData() == null ? 0 : (Integer)jobCtx.getTransientUserData();
        //This will provide a count for how many times the decider is called.
        deciderCount++;
        jobCtx.setTransientUserData(deciderCount);
        
        String deciderExitStatus = null;
        
        //On a restart we always want everything to continue to the end.
        if ("true".equals(isRestart)){
            deciderExitStatus = deciderCount + ":" + stepName + "_CONTINUE";
        } else{
            deciderExitStatus = deciderCount + ":" + stepExitStatus;    
        }
        
        return deciderExitStatus;
        
    }

}
