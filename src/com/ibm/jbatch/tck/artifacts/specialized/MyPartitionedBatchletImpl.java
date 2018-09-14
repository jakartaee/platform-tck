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

@javax.inject.Named("myPartitionedBatchletImpl")
public class MyPartitionedBatchletImpl extends AbstractBatchlet {

	private final static Logger logger = Logger.getLogger(MyPartitionedBatchletImpl.class.getName());
	
    private static int count = 1;

    @Inject    
    @BatchProperty(name="good.partition.status")
    private String good_partition_status;

    @Inject    
    @BatchProperty(name="fail.this.partition")
    private String fail_this_partition;
    
    @Inject
    private JobContext jobCtx;
    
    @Inject 
    private StepContext stepCtx = null; 
    
    @Override
    public String process() throws Exception {
        
        if ("true".equals(fail_this_partition)){
            throw new Exception("Fail this partition on purpose in MyPartitionedBatchlet.process()");
        }
        
        //Check that job level properties were propagated to the partitioned or split step's context
        if (!jobCtx.getProperties().getProperty("job.level.prop").equals("job.prop.value")) {
            jobCtx.setExitStatus("Job level properties not set from parent context");
            throw new Exception("This test will not pass because the job level properties are not set in the partition or split level jobcontext");
        }
        
        //Check that step level properties were propagated to the partitioned or split step's context
        if (!stepCtx.getProperties().getProperty("step.level.prop").equals("step.prop.value")) {
            stepCtx.setExitStatus("Step level properties not set from parent context");
            throw new Exception("This test will not pass because the step level properties are not set in the partition or split level stepcontext");
        }

        return this.good_partition_status;
            
    }

    @Override
    public void stop() throws Exception {
        logger.fine("MyPartitionedBatchletImpl() - @Cancel #" + count);
    }


}
