/*
 * JBoss, Home of Professional Open Source
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

package org.jboss.cdi.tck.tests.context.session.async;

import java.io.IOException;

import jakarta.enterprise.context.SessionScoped;
import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.inject.Inject;
import jakarta.servlet.AsyncEvent;
import jakarta.servlet.AsyncListener;

import org.jboss.cdi.tck.util.SimpleLogger;

/**
 * @author Martin Kouba
 */
public class SimpleAsyncListener implements AsyncListener {

    public static boolean isSessionContextActive = false;
    private static final SimpleLogger logger = new SimpleLogger(SimpleAsyncListener.class);

    @Inject
    SimpleSessionBean simpleSessionBean;

    @Inject
    BeanManager beanManager;

    @Inject
    StatusBean statusBean;

    /*
     * (non-Javadoc)
     *
     * @see jakarta.servlet.AsyncListener#onComplete(jakarta.servlet.AsyncEvent)
     */
    @Override
    public void onComplete(AsyncEvent event) throws IOException {
        logger.log("onComplete");

        if (!statusBean.isOnTimeout() && !statusBean.isOnError()) {
            // Do not check and write info in case of post timeout/error action
            statusBean.setOnComplete(checkSessionContextAvailability());
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see jakarta.servlet.AsyncListener#onTimeout(jakarta.servlet.AsyncEvent)
     */
    @Override
    public void onTimeout(AsyncEvent event) throws IOException {
        logger.log("onTimeout");
        statusBean.setOnTimeout(checkSessionContextAvailability());
        event.getAsyncContext().complete();
    }

    /*
     * (non-Javadoc)
     *
     * @see jakarta.servlet.AsyncListener#onError(jakarta.servlet.AsyncEvent)
     */
    @Override
    public void onError(AsyncEvent event) throws IOException {
        logger.log("onError");
        statusBean.setOnError(checkSessionContextAvailability());
        event.getAsyncContext().complete();
    }

    /*
     * (non-Javadoc)
     *
     * @see jakarta.servlet.AsyncListener#onStartAsync(jakarta.servlet.AsyncEvent)
     */
    @Override
    public void onStartAsync(AsyncEvent event) throws IOException {
        logger.log("onStartAsync");
        statusBean.setOnStartAsync(checkSessionContextAvailability());
    }

    private boolean checkSessionContextAvailability() throws IOException {
        try {
            statusBean.setSessionBeanId(simpleSessionBean.getId());
            isSessionContextActive = beanManager.getContext(SessionScoped.class).isActive();
        } catch (Throwable e) {
            logger.log("Problem while checking request scope: " + e.getMessage());
        }
        if (!isSessionContextActive || statusBean.getSessionBeanId() == null) {
            return false;
        }
        return true;
    }
}
