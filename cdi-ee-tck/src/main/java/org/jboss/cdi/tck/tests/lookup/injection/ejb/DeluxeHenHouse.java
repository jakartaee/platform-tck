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
package org.jboss.cdi.tck.tests.lookup.injection.ejb;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import org.jboss.cdi.tck.tests.lookup.injection.Wolf;

@Dependent
public class DeluxeHenHouse extends HenHouse {

    @Resource(name = "greeting")
    String greeting;

    @Inject
    Wolf wolf;

    private String game;

    private SessionBean sessionBean;

    public boolean initializerCalledAfterInjectionPointsInit = false;

    public boolean postConstructCalledAfterInitializers = false;

    @Inject
    public void initialize() {
        initializerCalledAfterInjectionPointsInit = (fox != null && wolf != null && "Hello".equals(greeting) && "Hello".equals(superGreeting)
                && game.equals("poker") && superGame.equals("poker") && sessionBean.ping() && superSessionBean.ping());
    }

    @PostConstruct
    public void postConstruct() {
        postConstructCalledAfterInitializers = (initializerCalledAfterInjectionPointsInit && hen != null);
    }
    
    @Resource(name = "game")
    private void setGame(String game) {
        this.game = game;
    }

    @EJB
    private void setSessionBean(SessionBean sessionBean) {
        this.sessionBean = sessionBean;
    }
}
