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

import javax.batch.api.Decider;
import javax.batch.runtime.BatchStatus;
import javax.batch.runtime.StepExecution;

import com.ibm.jbatch.tck.artifacts.common.StatusConstants;

@javax.inject.Named
public class SplitTransitionToDecisionTestDecider implements Decider, StatusConstants {

    public final static String DECIDER_EXIT_STATUS = "DECIDER_EXIT_STATUS";

    @Override
    public String decide(StepExecution[] stepExecutions) throws Exception {

        if (stepExecutions.length != 2) {
            throw new IllegalStateException("Expecting stepExecutions array of size 2, found one of size = " + stepExecutions.length);
        }

        for (StepExecution stepExec : stepExecutions) {
            if (stepExec == null) {
                throw new Exception("Null StepExecution after split.");
            }

            if (!stepExec.getBatchStatus().equals(BatchStatus.COMPLETED)) {
                throw new Exception("All step executions must be compelete before transitioning to a decider.");
            }

        }

        // <end exit-status="ThatsAllFolks" on="DECIDER_EXIT_STATUS*2" />
        return DECIDER_EXIT_STATUS + "*" + stepExecutions.length;
    }

}
