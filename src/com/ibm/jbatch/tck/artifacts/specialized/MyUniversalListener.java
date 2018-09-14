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

import javax.batch.api.BatchProperty;
import javax.batch.api.listener.JobListener;
import javax.batch.api.listener.StepListener;
import javax.batch.runtime.context.JobContext;
import javax.inject.Inject;

@javax.inject.Named("myUniversalListener")
public class MyUniversalListener implements JobListener, StepListener {
    
	private final static Logger logger = Logger.getLogger(MyUniversalListener.class.getName());
	
    @Inject 
    private JobContext jobCtx = null; 
    
    @Inject    
    @BatchProperty(name="app.timeinterval")
    String timeintervalString;
    
    int timeinterval = 0;
    
    @Override 
    public void beforeJob() {
        String cur = jobCtx.getExitStatus();
        String status = (cur == null ? "BeforeJob" : cur + "BeforeJob");
        jobCtx.setExitStatus(status);
    }
    
    @Override 
    public void afterJob() {
        String cur = jobCtx.getExitStatus();
        jobCtx.setExitStatus(cur + "AfterJob");
    }
    
    @Override 
    public void beforeStep() {
    	timeinterval = Integer.parseInt(timeintervalString);
        String cur = jobCtx.getExitStatus();
        jobCtx.setExitStatus(cur + "BeforeStep");
    }
    
    @Override
    public void afterStep() {
    	logger.fine("AJM: gonna sleep for " + timeinterval);
    	try {
    		Thread.sleep(timeinterval);
    	} catch (Exception e){
    		e.printStackTrace();
    	}
        String cur = jobCtx.getExitStatus();
        jobCtx.setExitStatus(cur + "AfterStep");
    }
}
