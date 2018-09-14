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

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.batch.api.BatchProperty;
import javax.batch.api.chunk.AbstractCheckpointAlgorithm;
import javax.inject.Inject;

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



@javax.inject.Named("inventoryCheckpointAlgorithmOverride150")
public class InventoryCheckpointAlgorithmOverride150 extends AbstractCheckpointAlgorithm {

	private static final String className = InventoryCheckpointAlgorithmNoOverride.class.getName();
	private static Logger logger  = Logger.getLogger(InventoryCheckpointAlgorithmNoOverride.class.getPackage().getName());
		
	boolean inCheckpoint = false;
	int checkpointIterations;
	
   boolean init = false;
   
       @Inject    
    @BatchProperty(name="commitInterval")
   String commitIntervalString;
       int commitInterval;
   
	@Override
	public boolean isReadyToCheckpoint() throws Exception {
      	String method = "isReadyToCheckpoint";
      	if(logger.isLoggable(Level.FINER)) { logger.entering(className, method); }

      	if (!init){
      		
          	commitInterval = Integer.parseInt(commitIntervalString);
          	checkpointIterations = 0;
    		init = true;
      	}
      	
      	checkpointIterations++;
      	boolean ready = (checkpointIterations == commitInterval);
      	
       if ( ready) {
    	   checkpointIterations = 0;
       }
       
       return ready;
	}
	
    @Override
    public int checkpointTimeout() throws Exception {
        return 150;
    }

}

