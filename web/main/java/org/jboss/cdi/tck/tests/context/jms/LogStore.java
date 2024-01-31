/*
 * Copyright 2010, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.context.jms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class LogStore {

    private final List<LogMessage> logMessages = Collections.synchronizedList(new ArrayList<LogMessage>());

    public void recordLogMessage(String text, String serviceId) {
        logMessages.add(new LogMessage(Thread.currentThread().getId(), text, serviceId));
    }

    /**
     * @return read-only view of logged messages
     */
    public List<LogMessage> getLogMessages() {
        return Collections.unmodifiableList(new ArrayList<LogMessage>(this.logMessages));
    }

    /**
     * Immutable log message.
     */
    public class LogMessage {

        private final long threadId;

        private final String text;

        private final String serviceId;

        public LogMessage(long threadId, String text, String serviceId) {
            super();
            this.threadId = threadId;
            this.text = text;
            this.serviceId = serviceId;
        }

        public long getThreadId() {
            return threadId;
        }

        public String getText() {
            return text;
        }

        public String getServiceId() {
            return serviceId;
        }

    }

}
