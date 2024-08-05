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

package org.jboss.cdi.tck.tests.decorators.builtin.http.session;

import java.io.Serializable;

import jakarta.decorator.Decorator;
import jakarta.decorator.Delegate;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpSession;

/**
 * @author Martin Kouba
 * 
 */
@Decorator
public abstract class HttpSessionDecorator1 implements HttpSession, Serializable {

    @Inject
    @Delegate
    HttpSession delegate;

    @Inject
    HttpSessionObserver httpSessionObserver;

    @Override
    public long getLastAccessedTime() {
        return delegate.getLastAccessedTime() * 3;
    }

    @Override
    public void invalidate() {
        delegate.invalidate();
        httpSessionObserver.setDecorated(true);
    }

    

}
