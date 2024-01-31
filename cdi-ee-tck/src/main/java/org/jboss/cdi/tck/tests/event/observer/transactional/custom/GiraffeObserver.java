/*
 * Copyright 2012, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jboss.cdi.tck.tests.event.observer.transactional.custom;

import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.event.TransactionPhase;

import org.jboss.cdi.tck.util.ActionSequence;
import org.jboss.cdi.tck.util.SimpleLogger;

@Dependent
public class GiraffeObserver {

    private static final SimpleLogger logger = new SimpleLogger(GiraffeObserver.class);

    /**
     * 
     * @param giraffe
     * @throws Exception
     */
    public void withdrawAfterCompletion(@Observes(during = TransactionPhase.AFTER_COMPLETION) Giraffe giraffe) throws Exception {
        logEventFired(TransactionPhase.AFTER_COMPLETION);
    }

    /**
     * 
     * @param giraffe
     * @throws Exception
     */
    public void withdrawBeforeCompletion(@Observes(during = TransactionPhase.BEFORE_COMPLETION) Giraffe giraffe)
            throws Exception {
        logEventFired(TransactionPhase.BEFORE_COMPLETION);
    }

    private void logEventFired(TransactionPhase phase) {
        logger.log(phase.toString());
        ActionSequence.addAction(phase.toString());
    }
}
