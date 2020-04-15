/**
 * Copyright 2013, 2020 International Business Machines Corp.
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

import jakarta.batch.api.BatchProperty;
import jakarta.batch.api.chunk.ItemReader;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import com.ibm.jbatch.tck.artifacts.chunktypes.ReadRecord;

@Named
public class ListenerOnErrorReader implements ItemReader {

	@Inject    
    @BatchProperty(name="read.fail.immediate")
    String failImmediateString;
	boolean failimmediate = false;
	
	@Override
	public void open(Serializable checkpoint) throws Exception {
		if (failImmediateString!=null){
			failimmediate = Boolean.parseBoolean(failImmediateString);
		}
	}

	@Override
	public void close() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ReadRecord readItem() throws Exception {
		// TODO Auto-generated method stub
		if (failimmediate){
			throw new Exception("read fail immediate");
		}
		else {
			return new ReadRecord();
		}
	}

	@Override
	public Serializable checkpointInfo() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
