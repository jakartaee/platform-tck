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

import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;

import javax.batch.api.chunk.ItemWriter;
import javax.batch.runtime.context.JobContext;
import javax.inject.Inject;

@javax.inject.Named("nullChkPtInfoWriter")
public class NullChkPtInfoWriter implements ItemWriter {

	private final static Logger logger = Logger.getLogger(DoSomethingSimpleArrayWriter.class.getName());
	
    @Inject 
    JobContext jobCtx;
	
	@Override
	public void open(Serializable checkpoint) throws Exception {
		logger.fine("AJM: writer.open(checkpoint)");

		
		if (checkpoint == null){
			jobCtx.setExitStatus(jobCtx.getExitStatus()+"...checkpointInfo is null in writer.open");
		}
		
	}

	@Override
	public void close() throws Exception {
		// TODO Auto-generated method stub
		logger.fine("AJM: writer.close()");
		
	}

	@Override
	public Serializable checkpointInfo() throws Exception {
		// TODO Auto-generated method stub
		logger.fine("AJM: returing null from writer.checkpointInfo()");
		return null;
	}

	@Override
	public void writeItems(List<Object> items) throws Exception {
		// TODO Auto-generated method stub
		logger.fine("AJM: writer.writeItems()");
	}

}
