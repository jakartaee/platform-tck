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

package org.jboss.cdi.tck.tests.context.application.async;

import java.io.IOException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.inject.Inject;
import jakarta.servlet.AsyncEvent;
import jakarta.servlet.AsyncListener;
import org.jboss.cdi.tck.util.SimpleLogger;

/**
 * @author Martin Kouba
 * @author Tomas Remes
 */
public class SimpleAsyncListener implements AsyncListener {

    public static Long onStartAsync = null;
    public static Long onError = null;
    public static Long onTimeout = null;
    public static Long onComplete = null;
    public static boolean isApplicationContextActive = false;

    private static final SimpleLogger logger = new SimpleLogger(SimpleAsyncListener.class);

    @Inject
    SimpleApplicationBean simpleApplicationBean;

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
            statusBean.setOnComplete(checkApplicationContextAvailability());
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
        statusBean.setOnTimeout(checkApplicationContextAvailability());
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
        statusBean.setOnError(checkApplicationContextAvailability());
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
        statusBean.setOnStartAsync(checkApplicationContextAvailability());
    }

    private boolean checkApplicationContextAvailability() throws IOException {
        try {
            statusBean.setApplicationBeanAvailable(simpleApplicationBean.ping());
            isApplicationContextActive = beanManager.getContext(ApplicationScoped.class).isActive();
        } catch (Throwable e) {
            logger.log("Problem while checking application scope: " + e.getMessage());
        }
        if (!isApplicationContextActive || !statusBean.isApplicationBeanAvailable()) {
            return false;
        }
        return true;
    }

}
