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

    public class MyPersistentUserData implements Externalizable {
        int data = 0;
        boolean fail = false;
        long serialVersionUID = 0L;
        
        public MyPersistentUserData() {
            
        }
        
        public MyPersistentUserData(int x, boolean fail) {
            data = x;
            this.fail = fail;
        }
        
        @Override
        public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        	data = in.readInt();
        	fail = in.readBoolean();
            
        }
        @Override
        public void writeExternal(ObjectOutput out) throws IOException {
            out.writeInt(data);
            out.writeBoolean(this.fail);
            
        }

		public int getData() {
			return data;
		}
		
		public boolean getFail(){
			return this.fail;
		}
        
    }