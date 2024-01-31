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

package org.jboss.cdi.tck.interceptors.tests.bindings.resolution.ejb;

import jakarta.annotation.Resource;
import jakarta.ejb.Stateless;
import jakarta.ejb.Timeout;
import jakarta.ejb.Timer;
import jakarta.ejb.TimerService;

import org.jboss.cdi.tck.util.SimpleLogger;

@MessageBinding
@Stateless
public class MessageService extends LoggedService {

    private static final SimpleLogger logger = new SimpleLogger(MessageService.class);

    @Resource
    private TimerService timerService;

    @PingBinding
    public void ping() {
    }

    public void start() {
        timerService.createTimer(100, "test");
        logger.log("Timer created");
    }

    @PingBinding
    @Timeout
    public void onTimeout(Timer timer) {
        logger.log("Timeouted");
    }

}
