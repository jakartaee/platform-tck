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
package org.jboss.cdi.tck.tests.implementation.builtin;

import java.security.Principal;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;

public class MockLoginModule implements LoginModule {

    protected Set<String> roles = new HashSet<String>();

    protected Subject subject;
    protected Map<String, ?> options;
    protected CallbackHandler callbackHandler;

    public MockLoginModule() {
    }

    public boolean abort() throws LoginException {
        return true;
    }

    public boolean commit() throws LoginException {
        subject.getPrincipals().add(new Principal() {

            public String getName() {
                return "default";
            }
        });
        return true;
    }

    public void initialize(Subject subject, CallbackHandler callbackHandler, Map<String, ?> sharedState, Map<String, ?> options) {
        this.subject = subject;
        this.options = options;
        this.callbackHandler = callbackHandler;
    }

    public boolean login() throws LoginException {
        return true;
    }

    public boolean logout() throws LoginException {
        return true;
    }
}