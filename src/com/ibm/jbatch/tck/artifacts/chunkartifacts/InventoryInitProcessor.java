/**
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
package com.ibm.jbatch.tck.artifacts.chunkartifacts;

import com.sun.ts.lib.harness.*;
import com.sun.ts.lib.util.TestUtil;

import javax.batch.api.BatchProperty;
import javax.batch.api.chunk.ItemProcessor;
import javax.inject.Inject;

import com.ibm.jbatch.tck.artifacts.chunktypes.InventoryRecord;


@javax.inject.Named("inventoryInitProcessor")
public class InventoryInitProcessor implements ItemProcessor {

	
    @Inject    
    @BatchProperty(name="init.inventory.quantity")
	String quantityProp = null;
	
	@Override
	public InventoryRecord processItem(Object record) throws Exception {
		
		//we don't care what quantity is in the db, we just want to reset it 

		int itemID = ((InventoryRecord)record).getItemID();
		int quantity = Integer.parseInt(quantityProp);		
		
		//reset the database to initial state, this is for test setup purposes only 
		
		return new InventoryRecord(itemID, quantity);
	}
	
}
