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

package org.jboss.cdi.tck.tests.decorators.builtin.http.request;

import java.io.Serializable;
import java.util.Locale;

import jakarta.decorator.Decorator;
import jakarta.decorator.Delegate;
import jakarta.inject.Inject;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

@Decorator
@SuppressWarnings("serial")
public abstract class HttpServletRequestDecorator2 implements HttpServletRequest, Serializable {

    @Inject
    @Delegate
    HttpServletRequest delegate;

    @Override
    public Cookie[] getCookies() {
        return null;
    }

    @Override
    public Locale getLocale() {
        return Locale.SIMPLIFIED_CHINESE;
    }

    @Override
    public boolean isUserInRole(String role) {
        return delegate.isUserInRole(role);
    }

}
