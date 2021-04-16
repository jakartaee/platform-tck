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

package org.jboss.cdi.tck.tests.context.application.async;

import jakarta.servlet.AsyncContext;

import org.jboss.cdi.tck.util.SimpleLogger;
import org.jboss.cdi.tck.util.Timer;

/**
 * @author Martin Kouba
 */
public class AsyncRequestProcessor implements Runnable {

    private static final SimpleLogger logger = new SimpleLogger(AsyncRequestProcessor.class);

    private final AsyncContext actx;

    private final long timerValue;

    private final String dispatchPath;

    private final boolean useDispatch;

    public AsyncRequestProcessor(AsyncContext ctx, long timerValue, boolean useDispatch, String dispatchPath) {
        super();
        this.actx = ctx;
        this.timerValue = timerValue;
        this.dispatchPath = dispatchPath;
        this.useDispatch = useDispatch;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
        try {
            if (timerValue > 0) {
                // Simulate long running operation
                Timer.startNew(timerValue);
            }
        } catch (InterruptedException e) {
            throw new IllegalStateException("Interrupted");
        }

        // Dispatch or complete
        if (useDispatch) {
            if (dispatchPath != null) {
                actx.dispatch(dispatchPath);
            } else {
                actx.dispatch();
            }
        } else {
            actx.complete();
        }
        logger.log("Async processing finished");
    }

}
