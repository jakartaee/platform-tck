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

package org.jboss.cdi.tck.tests.context.conversation.inactive;

import jakarta.annotation.Resource;
import jakarta.ejb.Stateless;
import jakarta.ejb.Timeout;
import jakarta.ejb.Timer;
import jakarta.ejb.TimerService;
import jakarta.enterprise.context.ContextNotActiveException;
import jakarta.enterprise.context.Conversation;
import jakarta.inject.Inject;

@Stateless
public class FooBean {

    @Inject
    private Bar bar;

    @Inject
    private Conversation conversation;

    @Resource
    private TimerService timerService;

    public void createTimer() {
        timerService.createTimer(5l, null);
    }

    @Timeout
    public void handleTimeout(Timer timer) {
        try {
            conversation.begin();
        } catch (ContextNotActiveException e) {
            // Should throw ContextNotActiveException
            bar.setContextNotActiveExceptionThrown(true);
        }
    }

}
