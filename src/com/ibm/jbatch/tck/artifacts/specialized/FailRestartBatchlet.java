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
import javax.batch.api.Batchlet;
import javax.inject.Inject;

@javax.inject.Named("failRestartBatchlet")
public class FailRestartBatchlet implements Batchlet {

    @Inject    
    @BatchProperty(name="execution.number")
    String executionNumberString;
    
    @Inject    
    @BatchProperty(name="sleep.time")
    String sleepTimeString;
	
    boolean init = false;
    int executionNum = 0;
    int sleeptime;
    
	@Override
	public String process() throws Exception {

		if (!init) {
			init();
		}
		
		if (executionNum == 1){
			throw new Exception("fail on purpose, execution1");
		}
		else if (executionNum == 2){
			Thread.sleep(sleeptime);
		}
		return "FailRestartBatchlet Done";
	}

	@Override
	public void stop() throws Exception {
		// TODO Auto-generated method stub

	}
	
	private void init(){
		if (executionNumberString != null) {
			executionNum = Integer.parseInt(executionNumberString);
		}
		if (executionNumberString != null) {
			sleeptime = Integer.parseInt(sleepTimeString);
		}
		
		init = true;
	}

}
