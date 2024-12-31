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
package org.jboss.cdi.tck.tests.implementation.builtin;

import java.security.Principal;
import java.util.HashMap;

import jakarta.ejb.Stateful;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;

import javax.security.auth.login.AppConfigurationEntry;
import javax.security.auth.login.AppConfigurationEntry.LoginModuleControlFlag;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

/**
 * @author pmuir
 * 
 */
@Stateful
public class PrincipalInjectedBean implements PrincipalInjectedBeanLocal {

    static final String DEFAULT_JAAS_CONFIG_NAME = "default";

    @Inject
    Instance<Principal> principal;

    public Principal getPrincipal() {
        return principal.get();
    }

    public void login() throws LoginException {
        LoginContext lc = new LoginContext(DEFAULT_JAAS_CONFIG_NAME, null, null, createConfiguration());
        lc.login();
    }

    protected AppConfigurationEntry createAppConfigurationEntry() {
        return new AppConfigurationEntry(MockLoginModule.class.getName(), LoginModuleControlFlag.REQUIRED,
                                         new HashMap<String, String>());
    }

    protected javax.security.auth.login.Configuration createConfiguration() {
        return new javax.security.auth.login.Configuration() {
            private AppConfigurationEntry[] aces = { createAppConfigurationEntry() };

            @Override
            public AppConfigurationEntry[] getAppConfigurationEntry(String name) {
                return DEFAULT_JAAS_CONFIG_NAME.equals(name) ? aces : null;
            }

            @Override
            public void refresh() {
            }
        };
    }

}
