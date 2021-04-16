/*
 * JBoss, Home of Professional Open Source
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
package org.jboss.cdi.tck.interceptors.tests.contract.aroundTimeout;

import static org.testng.Assert.assertTrue;

import java.util.concurrent.atomic.AtomicLong;

import jakarta.annotation.Resource;
import jakarta.annotation.security.RolesAllowed;
import jakarta.annotation.security.RunAs;
import jakarta.ejb.SessionContext;
import jakarta.ejb.Stateless;
import jakarta.ejb.Timeout;
import jakarta.ejb.Timer;
import jakarta.interceptor.Interceptors;

@Stateless
@Interceptors(AlarmSecurityInterceptor.class)
@RunAs("alarm")
@RolesAllowed("student")
public class Alarm {

    public static AtomicLong timeoutAt = null;

    @Resource
    private SessionContext ctx;

    public void createTimer() {
        ctx.getTimerService().createTimer(1l, null);
    }

    @Timeout
    public void timeout(Timer timer) {
        timeoutAt = new AtomicLong(System.currentTimeMillis());
        assertTrue(!this.ctx.isCallerInRole("student"));
        assertTrue(!this.ctx.isCallerInRole("alarm"));
    }
}
