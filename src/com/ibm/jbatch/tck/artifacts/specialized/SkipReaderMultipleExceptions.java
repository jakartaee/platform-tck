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
import java.util.logging.Logger;

import javax.batch.api.BatchProperty;
import javax.batch.api.chunk.AbstractItemReader;
import javax.inject.Inject;

import com.ibm.jbatch.tck.artifacts.chunktypes.ArrayIndexCheckpointData;
import com.ibm.jbatch.tck.artifacts.chunktypes.ReadRecord;
import com.ibm.jbatch.tck.artifacts.reusable.MyChildException;
import com.ibm.jbatch.tck.artifacts.reusable.MyGrandchildException;
import com.ibm.jbatch.tck.artifacts.reusable.MyParentException;

@javax.inject.Named("skipReaderMultipleExceptions")
public class SkipReaderMultipleExceptions extends AbstractItemReader {

	private final static Logger logger = Logger.getLogger(SkipReaderMultipleExceptions.class.getName());
	
    private int count = 0;
    private int exceptionCount = 0;
    private int[] readerDataArray;
    private int idx;
    private boolean threwSkipException = false;
    boolean throwChildEx = false;
    ArrayIndexCheckpointData _cpd = new ArrayIndexCheckpointData();

    @Inject    
    @BatchProperty(name = "readrecord.fail")
    String readrecordfailNumberString = null;

    @Inject    
    @BatchProperty(name = "execution.number")
    String executionNumberString;

    @Inject    
    @BatchProperty(name = "app.arraysize")
    String appArraySizeString;

    int[] failnum;
    int execnum;
    int arraysize;

    public SkipReaderMultipleExceptions() {

    }


    @Override
    public void open(Serializable cpd) {

        if (!(readrecordfailNumberString == null)) {
            String[] readFailPointsStrArr = readrecordfailNumberString.split(",");
            failnum = new int[readFailPointsStrArr.length];
            for (int i = 0; i < readFailPointsStrArr.length; i++) {
                failnum[i] = Integer.parseInt(readFailPointsStrArr[i]);
            }
        } else {
            failnum = new int[1];
            failnum[0] = -1;
        }

        execnum = Integer.parseInt(executionNumberString);

        arraysize = Integer.parseInt(appArraySizeString);
        readerDataArray = new int[arraysize];

        for (int i = 0; i < arraysize; i++) {
            // init the data array
            readerDataArray[i] = i;
        }

        if (cpd == null) {
            // position at the beginning
            idx = 0;
        } else {
            // position at index held in the cpd
            idx = ((ArrayIndexCheckpointData)cpd).getCurrentIndex() + 1;
        }
        logger.fine("READ: starting at index: " + idx);
    }

    @Override
    public ReadRecord readItem() throws Exception {

        if (threwSkipException) {
            count++;
            idx++;
            threwSkipException = false;
            exceptionCount++;
        }

        int i = idx;

        if (i == arraysize) {
            return null;
        }
        if (execnum == 2) {
            failnum[0] = -1;
        }

        if (isFailnum(idx)) {
            logger.fine("READ: got the fail num..." + failnum);
            threwSkipException = true;
            if (exceptionCount == 0) {
                throw new MyParentException("fail on purpose with MyParentException");
            } else if (exceptionCount == 1){
            	throw new MyGrandchildException("fail on purpose with MyGrandchildException");
            }
            else {
                throw new MyChildException("fail on purpose with MyChildException");
            }
        }
        count = count + 1;
        idx = idx + 1;
        _cpd.setCurrentIndex(i);
        return new ReadRecord(readerDataArray[i]);
    }

    @Override
    public ArrayIndexCheckpointData checkpointInfo() {

        logger.fine("READ: in getCPD cpd index from store: " + _cpd.getCurrentIndex());
        logger.fine("READ: in getCPD idx : " + idx);

        return _cpd;
    }

    private boolean isFailnum(int idxIn) {

        boolean ans = false;
        for (int i = 0; i < failnum.length; i++) {
            if (idxIn == failnum[i]) {
                ans = true;
            }
        }
        return ans;
    }
}
