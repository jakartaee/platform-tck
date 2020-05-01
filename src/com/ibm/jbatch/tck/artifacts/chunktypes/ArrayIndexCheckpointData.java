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
package com.ibm.jbatch.tck.artifacts.chunktypes;

import com.sun.ts.lib.harness.*;
import com.sun.ts.lib.util.TestUtil;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.logging.Logger;

@jakarta.inject.Named("arrayIndexCheckpointData")
public class ArrayIndexCheckpointData implements Externalizable {

	private final static Logger logger = Logger.getLogger(ArrayIndexCheckpointData.class.getName());
	
    private final static long serialVersionUID = 1L;
    private int i = 0;
    
    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
    	i = in.readInt();
    	logger.fine("AJM: reading in the chkpt data: " + i);
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
    	logger.fine("AJM: must have been checkpointed, writing out array index: " + i);
        out.writeInt(i);

    }
    
    public int getCurrentIndex(){
    	logger.fine("AJM: current index = " + i);
    	return i;
    }
    
    public void setCurrentIndex(int incoming_idx){
    	i = incoming_idx;
    }
}
