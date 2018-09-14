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
package com.ibm.jbatch.tck.artifacts.specialized;

import com.sun.ts.lib.harness.*;
import com.sun.ts.lib.util.TestUtil;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;

import javax.batch.api.BatchProperty;
import javax.batch.api.chunk.ItemWriter;
import javax.inject.Inject;

import com.ibm.jbatch.tck.artifacts.chunktypes.ArrayIndexCheckpointData;
import com.ibm.jbatch.tck.artifacts.chunktypes.ReadRecord;
import com.ibm.jbatch.tck.artifacts.reusable.MyParentException;

@javax.inject.Named("skipWriter")
public class SkipWriter implements ItemWriter {

	private final static Logger logger = Logger.getLogger(SkipWriter.class.getName());
	
	private int[] writerDataArray = new int[30];
	private int idx = 0;

	int chunkWriteIteration = 0;
	
    @Inject    
    @BatchProperty(name="app.arraysize")
    String appArraySizeString;
	
    @Inject    
    @BatchProperty(name="writerecord.fail")
    String writeRecordFailNumberString = null;
	
	int arraysize;
	int [] failnum;
	int [] writePoints;
	
	@Override
	public void open(Serializable cpd) throws Exception {
		logger.fine("openWriter");
		
		ArrayIndexCheckpointData checkpointData = (ArrayIndexCheckpointData)cpd;
		
		arraysize = Integer.parseInt(appArraySizeString);
		
		if (! (writeRecordFailNumberString == null)) {
			String[] writeFailPointsStrArr = writeRecordFailNumberString.split(",");
			failnum = new int[writeFailPointsStrArr.length];
			for (int i = 0; i < writeFailPointsStrArr.length; i++) {
				failnum[i] = Integer.parseInt(writeFailPointsStrArr[i]);
			}
		}
		else {
			failnum = new int[1];
			failnum[0] = -1;
		}

		if (checkpointData == null){
			//position at the beginning
			idx = 0;
			logger.fine("WRITE: chkpt data = null, so idx = " + idx);
		}
		else {
			// position at index held in the cpd
			idx = checkpointData.getCurrentIndex();
			//for (int i = 0; i<writePoints.length; i++){
			//	if (idx <= writePoints[i]){
			//		chunkWriteIteration++;
			//	}
			//}
			
			logger.fine("WRITE: chkpt data was valid, so idx = " + idx);
			logger.fine("WRITE: chunkWriteIteration = " + chunkWriteIteration);
		}
		//for (int n=0; n<chkArraySize;n++){
		//	logger.fine("WRITE: chunk write point[" + n + " ]: " + checkArray[n]);
		//}
		
		
		for (int i = 0; i<arraysize; i++) {
			writerDataArray[i] = 0;
		}
		//idx = checkpointData.getCurrentIndex();
		//logger.fine("WRITE: chkpt data was valid, so idx = " + idx);
	}
	
	
	@Override
	public void close() throws Exception {
		//logger.fine("closeWriter - writerDataArray:\n");
		for (int i = 0; i < arraysize; i++){
			logger.fine("WRITE: writerDataArray[" + i + "] = " + writerDataArray[i]);
		}
	}
	
	@Override
	public void writeItems(List<Object> myData) throws Exception {
		
		logger.fine("writeMyData receives chunk size=" + myData.size());
		int i;
		logger.fine("WRITE: before writing, idx = " + idx);
		logger.fine("WRITE: before writing, chunkWriteIteration = " + chunkWriteIteration);
		
		if (!(writeRecordFailNumberString == null)) {
			if (chunkWriteIteration == 2) {
				chunkWriteIteration++;
				throw new MyParentException(
						"fail on purpose on write iteration = 2");
			} else if (chunkWriteIteration == 5) {
				chunkWriteIteration++;
				throw new MyParentException(
						"fail on purpose on write iteration = 5");
			} else if (chunkWriteIteration == 8) {
				chunkWriteIteration++;
				throw new MyParentException(
						"fail on purpose on write iteration = 8");
			} else {
				chunkWriteIteration++;
			}
		} else {
			chunkWriteIteration++;
		}
		
		for  (i = 0; i < myData.size(); i++) {
			
			writerDataArray[idx] = ((ReadRecord)myData.get(i)).getCount();
			idx++;
		}
		for (i = 0; i < arraysize; i++){
			logger.fine("WRITE: writerDataArray[" + i + "] = " + writerDataArray[i]);
		}
		logger.fine("WRITE: idx = " + idx + " and i = " + i);
		logger.fine("WRITE: chunkWriteIteration= "+ chunkWriteIteration);
		//if (checkArray[chunkWriteIteration] == (chunkWriteIteration+1)*chunksize ) {
	}
	
	@Override
	public ArrayIndexCheckpointData checkpointInfo() throws Exception {
			ArrayIndexCheckpointData _chkptData = new ArrayIndexCheckpointData();
			_chkptData.setCurrentIndex(idx);
		return _chkptData;
	}

}

