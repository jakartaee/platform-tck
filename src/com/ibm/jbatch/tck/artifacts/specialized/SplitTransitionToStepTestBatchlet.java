/*
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

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;

import javax.batch.api.AbstractBatchlet;
import javax.batch.runtime.context.JobContext;
import javax.batch.runtime.context.StepContext;
import javax.inject.Inject;

@javax.inject.Named("splitTransitionToStepTestBatchlet")
public class SplitTransitionToStepTestBatchlet extends AbstractBatchlet {

	public static String GOOD_EXIT_STATUS = "VERY GOOD INVOCATION"; 

    @Inject
	JobContext jobCtx;
    
    @Inject
	StepContext stepCtx = null;
    
	@Override
	public String process() throws Exception {

		TransitionListPersistent data = (TransitionListPersistent) jobCtx.getTransientUserData();
		if( data != null) {
			data.getTransitionList().add(stepCtx.getStepName());
		} else {
			jobCtx.setTransientUserData(new TransitionListPersistent(stepCtx.getStepName()));
		}
		
		String transitionList = ((TransitionListPersistent)jobCtx.getTransientUserData()).getTransitionList().toString();
				
		// toString() returns string surrounded by brackets, remove them.
		jobCtx.setExitStatus(transitionList.substring(1, transitionList.length()-1));
		
		return GOOD_EXIT_STATUS;
	}
	
	private class TransitionListPersistent implements Externalizable {
		
		ArrayList<String> transitionList = new ArrayList<String>();
		
		public TransitionListPersistent() {}
		
		public TransitionListPersistent(String stepId) {
			transitionList.add(stepId);
		}
		
		public ArrayList<String> getTransitionList() {
			return transitionList;
		}

		@Override
		public void writeExternal(ObjectOutput out) throws IOException {
			out.writeObject(transitionList);
		}

		@Override
		public void readExternal(ObjectInput in) throws IOException,
				ClassNotFoundException {
			transitionList = (ArrayList<String>) in.readObject();
		}
		
		
	}

}
