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
package com.ibm.jbatch.tck.artifacts.specialized;

import com.sun.ts.lib.harness.*;
import com.sun.ts.lib.util.TestUtil;

import java.util.Properties;

import javax.batch.api.BatchProperty;
import javax.batch.api.partition.PartitionMapper;
import javax.batch.api.partition.PartitionPlan;
import javax.inject.Inject;

import com.ibm.jbatch.tck.artifacts.specialized.MyPartitionPlan;

@javax.inject.Named
public class MyPartitionMapper implements PartitionMapper {

	
	private static final String GOOD_EXIT_STATUS = "good.partition.status";

    @Inject    
    @BatchProperty
	private String numPartitionsProp = null;
    private int numPartitions;

    
    @Inject
    @BatchProperty
    private String failThisPartition = "-1";

    @Inject
    @BatchProperty
    private String partitionsOverride = null;

	
	@Override
	public PartitionPlan mapPartitions() throws Exception {
		
		numPartitions = Integer.parseInt(numPartitionsProp);
		
		Properties[] props = new Properties[numPartitions];
		
		Integer i;
		for (i = 0; i < numPartitions; i++) {
			props[i] = new Properties();
			props[i].setProperty(GOOD_EXIT_STATUS, "MapperProp" + Integer.toString(i));
			
			if (i.toString().equals(failThisPartition)) {
			    props[i].setProperty("fail.this.partition", "true");
			} else{
			    props[i].setProperty("fail.this.partition", "false");
			}
		}
		
		PartitionPlan partitionPlan = new MyPartitionPlan();
		partitionPlan.setPartitions(numPartitions);
		partitionPlan.setPartitionProperties(props);
		if ("true".equals(partitionsOverride)) {
		    partitionPlan.setPartitionsOverride(true);
		} else {
		    partitionPlan.setPartitionsOverride(false);
		}
		
		return partitionPlan;
		
	}
	

}
