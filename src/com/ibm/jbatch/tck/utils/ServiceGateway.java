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
package com.ibm.jbatch.tck.utils;

import com.sun.ts.lib.harness.*;
import com.sun.ts.lib.util.TestUtil;

import java.util.ServiceLoader;
import java.util.logging.Level;
import java.util.logging.Logger;



import com.ibm.jbatch.tck.spi.JobExecutionWaiterFactory;

public class ServiceGateway {
    private final static Logger logger = Logger.getLogger(ServiceGateway.class.getName());

    public static JobExecutionWaiterFactory getJobExecutionWaiterFactoryService() { 
    	JobExecutionWaiterFactory services = null;
        ServiceLoader<JobExecutionWaiterFactory> loader = 
            ServiceLoader.load(JobExecutionWaiterFactory.class);

        for (JobExecutionWaiterFactory provider : loader) {
            if (provider != null) {
                if (logger.isLoggable(Level.FINE)) {
                    logger.fine("Loaded JobExecutionWaiterFactory with className = " + provider.getClass().getCanonicalName());
                }
                TestUtil.logMsg("Loaded JobExecutionWaiterFactory with className = " + provider.getClass().getCanonicalName() + "<p>");
                // Use first one
                services = provider;
                break;
            }
        }

        if (services == null) {
            throw new IllegalStateException("Service loader didn't find resource found on classpath for service: META-INF/services/com.ibm.jbatch.tck.spi.JobExecutionWaiterFactory");
        }
        return services;
    } 
}
