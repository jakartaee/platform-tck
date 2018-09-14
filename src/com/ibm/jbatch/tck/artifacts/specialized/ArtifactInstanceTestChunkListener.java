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

import javax.batch.api.BatchProperty;
import javax.batch.api.chunk.listener.ChunkListener;
import javax.batch.runtime.context.JobContext;
import javax.batch.runtime.context.StepContext;
import javax.inject.Inject;
import javax.inject.Named;

@Named
public class ArtifactInstanceTestChunkListener implements ChunkListener {

    @Inject 
    JobContext jobCtx;
    
    @Inject
    StepContext stepCtx;
    
    @Inject    
    @BatchProperty(name="chunk.property")
    String chunkPropertyString;
    
    static String prop1 = "chunkListenerA";
    static String prop2 = "chunkListenerB";
    
    int instance1Count = 0;
    int instance2Count = 0;

    boolean uniqueInstance1 = false;
    boolean uniqueInstance2 = false;
    
    private boolean saw2Listeners = false;
    
	@Override
	public void beforeChunk() throws Exception {
		
        Map<String, Boolean> instanceData = (Map<String, Boolean>) stepCtx.getTransientUserData();

        if (chunkPropertyString.equals(prop1)) {
            instanceData.put("sawChunkProp1", true);

            instance1Count++;

        } else if (chunkPropertyString.equals(prop2)) {
            instanceData.put("sawChunkProp2", true);
            instance2Count++;
        }
		
		
	}

	@Override
	public void afterChunk() throws Exception {
		
		
        Map<String, Boolean> instanceData = (Map<String, Boolean>) stepCtx.getTransientUserData();

        if (instanceData.get("sawChunkProp1") && instanceData.get("sawChunkProp2")) {
            saw2Listeners = true;
        }


        if ((chunkPropertyString.equals(prop1)) && instance1Count == 1) {
            uniqueInstance1 = true;
        }

        if ((chunkPropertyString.equals(prop2)) && instance2Count == 1) {
            uniqueInstance2 = true;
        }

        String currentStatus = jobCtx.getExitStatus();

        if (currentStatus != null && currentStatus.equals("BAD")) {
            return;
        }
        
        if (saw2Listeners && (uniqueInstance1 ^ uniqueInstance2)) {
            jobCtx.setExitStatus(jobCtx.getExitStatus() + "ChunkListener");
        } else {
            jobCtx.setExitStatus("CHUNK_BAD");
        }
		
	}

	@Override
	public void onError(Exception e) throws Exception {
		// TODO Auto-generated method stub

	}

}
