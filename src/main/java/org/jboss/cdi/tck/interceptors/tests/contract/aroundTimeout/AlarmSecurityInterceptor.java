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

import jakarta.annotation.Resource;
import jakarta.ejb.EJB;
import jakarta.ejb.SessionContext;
import jakarta.interceptor.AroundTimeout;
import jakarta.interceptor.InvocationContext;

public class AlarmSecurityInterceptor {

    @EJB
    private Bell bell;

    @Resource
    private SessionContext sc;

    @AroundTimeout
    public Object interceptTimeout(InvocationContext ic) throws Exception {
        Object obj = ic.proceed();

        // Since a timeout callback method is an internal method of the bean class, it has no client security context
        assertTrue(!this.sc.isCallerInRole("student"));
        assertTrue(!this.sc.isCallerInRole("alarm"));

        TestData.securityContextOk.set(true);
        bell.callFromInterceptor();
        return obj;
    }

}
