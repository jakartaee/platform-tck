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
import javax.batch.api.listener.JobListener;
import javax.batch.runtime.context.JobContext;
import javax.inject.Inject;
import javax.inject.Named;

@Named
public class ArtifactInstanceTestJobListener implements JobListener {

    @Inject 
    JobContext jobCtx;
    
    @Inject    
    @BatchProperty(name="job.property")
    String jobPropertyString;
    
    public final static String prop1 = "jobListenerA";
    public final static String prop2 = "jobListenerB";
    
    int instance1Count = 0;
    int instance2Count = 0;
    
    boolean saw2Listeners = false;
    
    boolean uniqueInstance1 = false;
    boolean uniqueInstance2 = false;
    
	@Override
	public void beforeJob() throws Exception {
	    
        synchronized(jobCtx) {
            //initialize map
            if (jobCtx.getTransientUserData() == null) {
                
                Map<String, Boolean> dataMap = new ConcurrentHashMap<String, Boolean>();
                dataMap.put("sawProp1", false);
                dataMap.put("sawProp2", false);
                
                jobCtx.setTransientUserData(dataMap);
            }
        }
		
        Map<String, Boolean> instanceData = (Map<String, Boolean>) jobCtx.getTransientUserData();

        if (jobPropertyString.equals(prop1)) {
            instanceData.put("sawProp1", true);
            instance1Count++;

        } else if (jobPropertyString.equals(prop2)) {
            instanceData.put("sawProp2", true);
            instance2Count++;
        }
		
	}

	@Override
	public void afterJob() throws Exception {
	    Map<String, Boolean> instanceData = (Map<String, Boolean>) jobCtx.getTransientUserData();
	    
	
        if (instanceData.get("sawProp1") && instanceData.get("sawProp2")) {
            saw2Listeners = true;
        }

        if ((jobPropertyString.equals(prop1)) && instance1Count == 1) {
            uniqueInstance1 = true;
        }

        if ((jobPropertyString.equals(prop2)) && instance2Count == 1) {
            uniqueInstance2 = true;
        }

        String currentStatus = jobCtx.getExitStatus();

        if (currentStatus != null && currentStatus.equals("BAD")) {
            return;
        }
        
        if (saw2Listeners && (uniqueInstance1 ^ uniqueInstance2)) {
            jobCtx.setExitStatus(jobCtx.getExitStatus() + "JobListener");
        } else {
            jobCtx.setExitStatus("JOB_BAD");
        }
		
	}

}
