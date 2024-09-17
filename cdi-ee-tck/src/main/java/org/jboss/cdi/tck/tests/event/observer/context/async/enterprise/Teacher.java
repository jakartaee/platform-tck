/*
 * Copyright 2014, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.event.observer.context.async.enterprise;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import jakarta.annotation.security.PermitAll;
import jakarta.ejb.Stateful;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;

@Stateful
@PermitAll
public class Teacher {

    public static String TEACHER_MESSAGE = "teacher message";

    @Inject
    Event<Text> printer;

    public Throwable print() throws InterruptedException {
        BlockingQueue<Throwable> sync = new LinkedBlockingQueue<>();
        // this expects jakarta.ejb.EJBAccessException so the queue accepts only Throwable instance
        printer.fireAsync(new Text(TEACHER_MESSAGE)).whenComplete((text, throwable) -> sync.offer(throwable));
        return sync.poll(2l, TimeUnit.SECONDS);
    }

}
