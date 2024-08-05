/*
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
package org.jboss.cdi.tck.tests.context.application;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class Result {
    private boolean applicationScopeActiveForServletContextListener = false;
    private boolean applicationScopeActiveForHttpSessionListener = false;
    private boolean applicationScopeActiveForServletRequestListener = false;

    public boolean isApplicationScopeActiveForServletContextListener() {
        return applicationScopeActiveForServletContextListener;
    }

    public void setApplicationScopeActiveForServletContextListener(boolean applicationScopeActiveForServletContextListener) {
        this.applicationScopeActiveForServletContextListener = applicationScopeActiveForServletContextListener;
    }

    public boolean isApplicationScopeActiveForHttpSessionListener() {
        return applicationScopeActiveForHttpSessionListener;
    }

    public void setApplicationScopeActiveForHttpSessionListener(boolean applicationScopeActiveForHttpSessionListener) {
        this.applicationScopeActiveForHttpSessionListener = applicationScopeActiveForHttpSessionListener;
    }

    public boolean isApplicationScopeActiveForServletRequestListener() {
        return applicationScopeActiveForServletRequestListener;
    }

    public void setApplicationScopeActiveForServletRequestListener(boolean applicationScopeActiveForServletRequestListener) {
        this.applicationScopeActiveForServletRequestListener = applicationScopeActiveForServletRequestListener;
    }
}
