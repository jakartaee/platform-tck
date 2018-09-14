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
package com.ibm.jbatch.tck.artifacts.reusable;

import com.sun.ts.lib.harness.*;
import com.sun.ts.lib.util.TestUtil;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

    public class MyPersistentRestartUserData implements Externalizable {
        int executionNumber = 0;
        String nextWritePoints = null;
        long serialVersionUID = 0L;
        
        public MyPersistentRestartUserData() {
            
        }
        
        public MyPersistentRestartUserData(int execution, String nextWritePts) {
            this.executionNumber = execution;
            this.nextWritePoints = nextWritePts;
        }
        
        public int getExecutionNumber(){
        	return this.executionNumber;
        }
        
        public String getNextWritePoints(){
        	return this.nextWritePoints;
        }
        
        @Override
        public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        	this.executionNumber = in.readInt();
        	this.nextWritePoints = (String)in.readObject();
        }
        
        @Override
        public void writeExternal(ObjectOutput out) throws IOException {
            out.writeInt(this.executionNumber);
            out.writeObject(this.nextWritePoints);
        }
        
    }