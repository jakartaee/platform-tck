/*
 * Copyright 2012, 2020 International Business Machines Corp.
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

import jakarta.batch.api.chunk.listener.SkipReadListener;
import jakarta.batch.runtime.context.JobContext;
import jakarta.batch.runtime.context.StepContext;
import jakarta.inject.Inject;



import com.ibm.jbatch.tck.artifacts.reusable.MyParentException;

@jakarta.inject.Named("mySkipReaderExceedListener")
public class MySkipReaderExceedListener implements SkipReadListener {

    @Inject
    JobContext jobCtx;

    @Inject
    StepContext stepCtx;

    private final static String sourceClass = MySkipReadListener.class.getName();
    private final static Logger logger = Logger.getLogger(sourceClass);

    public static final String GOOD_EXIT_STATUS = "MySkipReadListener: GOOD STATUS";
    public static final String BAD_EXIT_STATUS_INCORRECT_NUMBER_SKIPS = "MySkipReadListener: BAD STATUS_INCORRECT_NUMBER_SKIPS";
    public static final String BAD_EXIT_STATUS_WRONG_EXCEPTION = "MySkipReadListener: BAD STATUS_WRONG_EXCEPTION";

    int count = 0;
    
    @Override
    public void onSkipReadItem(Exception e) {
        TestUtil.logMsg("In onSkipReadItem" + e + "<p>");
        
        count++;
        
        if (e instanceof MyParentException) {
        	if (count == 1){
        		TestUtil.logMsg("SKIPLISTENER: onSkipReadItem, exception is an instance of: MyParentException and number of skips is equal to 1<p>");
        		jobCtx.setExitStatus(GOOD_EXIT_STATUS);
        	}
        	else {
        		TestUtil.logMsg("SKIPLISTENER: onSkipReadItem invoked more than expected and/or wrong exception skipped");
                jobCtx.setExitStatus(BAD_EXIT_STATUS_INCORRECT_NUMBER_SKIPS);
        	}
        } 
    }
}
