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

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.batch.api.BatchProperty;
import javax.batch.api.listener.StepListener;
import javax.batch.runtime.context.JobContext;
import javax.batch.runtime.context.StepContext;
import javax.inject.Inject;
import javax.inject.Named;

@Named
public class ArtifactInstanceTestStepListener implements StepListener {

    @Inject
    JobContext jobCtx;

    @Inject
    StepContext stepCtx;

    @Inject
    @BatchProperty(name = "step.property")
    String stepPropertyString;

    public static final String prop1 = "stepListenerA";
    public static final String prop2 = "stepListenerB";

    int instance1Count = 0;
    int instance2Count = 0;

    boolean uniqueInstance1 = false;
    boolean uniqueInstance2 = false;
    
    private boolean saw2Listeners = false;


    String passedInProp;

    @Override
    public void beforeStep() throws Exception {
        
        synchronized(stepCtx) {
            if (stepCtx.getTransientUserData() == null) {
                Map<String, Boolean> dataMap = new ConcurrentHashMap<String, Boolean>();
                dataMap.put("sawProp1", false);
                dataMap.put("sawProp2", false);
                
                //init the chunk listener props here too. We init here because we know we 
                //are the first listener since the transient data is null. 
                dataMap.put("sawChunkProp1", false);
                dataMap.put("sawChunkProp2", false);

                stepCtx.setTransientUserData(dataMap);
            }
        }

        Map<String, Boolean> instanceData = (Map<String, Boolean>) stepCtx.getTransientUserData();

        if (stepPropertyString.equals(prop1)) {
            instanceData.put("sawProp1", true);

            instance1Count++;

        } else if (stepPropertyString.equals(prop2)) {
            instanceData.put("sawProp2", true);
            instance2Count++;
        }
    }

    @Override
    public void afterStep() throws Exception {
        Map<String, Boolean> instanceData = (Map<String, Boolean>) stepCtx.getTransientUserData();

        if (instanceData.get("sawProp1") && instanceData.get("sawProp2")) {
            saw2Listeners = true;
        }


        if ((stepPropertyString.equals(prop1)) && instance1Count == 1) {
            uniqueInstance1 = true;
        }

        if ((stepPropertyString.equals(prop2)) && instance2Count == 1) {
            uniqueInstance2 = true;
        }

        String currentStatus = jobCtx.getExitStatus();

        if (currentStatus != null && currentStatus.equals("BAD")) {
            return;
        }
        
        if (saw2Listeners && (uniqueInstance1 ^ uniqueInstance2)) {
            jobCtx.setExitStatus(jobCtx.getExitStatus() + "StepListener");
        } else {
            jobCtx.setExitStatus("STEP_BAD");
        }
        
    }

}
