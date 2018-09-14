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

import javax.batch.api.AbstractBatchlet;
import javax.batch.api.BatchProperty;
import javax.batch.runtime.context.StepContext;
import javax.inject.Inject;

@javax.inject.Named("multipleExitStatusBatchlet")
public class MultipleExitStatusBatchlet extends AbstractBatchlet {

    @Inject
    StepContext stepCtx;

    @Inject
    @BatchProperty(name = "stop.job.after.this.step")
    String stop_job_after_this_step;

    @Inject
    @BatchProperty(name = "stop.job.after.this.step2")
    String stop_job_after_this_step2;

    @Inject
    @BatchProperty(name = "fail.job.after.this.step")
    String fail_job_after_this_step;

    @Inject
    @BatchProperty(name = "step.complete.but.force.job.stopped.status")
    String step_complete_but_force_job_stopped_status;

    @Inject
    @BatchProperty(name = "step.complete.but.force.job.failed.status")
    String step_complete_but_force_job_failed_status;

    @Override
    public String process() throws Exception {

        if (stepCtx.getStepName().equalsIgnoreCase(stop_job_after_this_step)
                || stepCtx.getStepName().equalsIgnoreCase(stop_job_after_this_step2)) {
            
            stepCtx.setExitStatus(step_complete_but_force_job_stopped_status);
            return step_complete_but_force_job_stopped_status;
        }

        if (stepCtx.getStepName().equalsIgnoreCase(fail_job_after_this_step)) {
            stepCtx.setExitStatus(step_complete_but_force_job_failed_status);
            return step_complete_but_force_job_failed_status;
        }

        return stepCtx.getStepName() + "_CONTINUE";

    }

}
