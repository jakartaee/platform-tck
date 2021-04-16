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
package org.jboss.cdi.tck.tests.context.request.ejb;

import jakarta.annotation.Resource;
import jakarta.ejb.Stateless;
import jakarta.ejb.Timeout;
import jakarta.ejb.Timer;
import jakarta.ejb.TimerService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.inject.Inject;

@Stateless
public class FMSModelIII implements FMS {

    private static final String CLIMB_COMMAND = "ClimbCommand";
    private static final String DESCEND_COMMAND = "DescendCommand";

    @Resource
    private TimerService timerService;

    @Inject
    private BeanManager beanManager;

    @Inject
    Instance<SimpleRequestBean> simpleRequestBean;

    private static volatile boolean requestScopeActive = false;
    private static volatile String beanId = null;
    private static volatile boolean sameBean = false;

    private static volatile boolean climbed;
    private static volatile boolean descended;

    public void climb() {
        timerService.createTimer(200, CLIMB_COMMAND);
    }

    public void descend() {
        timerService.createTimer(100, DESCEND_COMMAND);
    }

    @Timeout
    public void timeout(Timer timer) {
        if (beanManager.getContext(RequestScoped.class).isActive()) {
            requestScopeActive = true;
            if (beanId != null && beanId.equals(simpleRequestBean.get().getId())) {
                sameBean = true;
            } else {
                beanId = simpleRequestBean.get().getId();
            }
        }
        // CDITCK-221 - applicationScopeActive, beanId and sameBean have been set and are testable
        if (timer.getInfo().equals(CLIMB_COMMAND)) {
            climbed = true;
        }
        if (timer.getInfo().equals(DESCEND_COMMAND)) {
            descended = true;
        }
    }

    public boolean isRequestScopeActive() {
        return requestScopeActive;
    }

    public static void reset() {
        beanId = null;
        climbed = false;
        descended = false;
        requestScopeActive = false;
        sameBean = false;
    }

    public boolean isSameBean() {
        return sameBean;
    }

    public static boolean isClimbed() {
        return climbed;
    }

    public static boolean isDescended() {
        return descended;
    }

}
