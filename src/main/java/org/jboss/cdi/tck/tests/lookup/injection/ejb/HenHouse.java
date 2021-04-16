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
package org.jboss.cdi.tck.tests.lookup.injection.ejb;

import jakarta.annotation.Resource;
import jakarta.ejb.EJB;
import jakarta.inject.Inject;
import org.jboss.cdi.tck.tests.lookup.injection.Fox;
import org.jboss.cdi.tck.tests.lookup.injection.Hen;

public class HenHouse {

    @Resource(name = "greeting")
    protected String superGreeting;

    protected String superGame;

    @Inject
    public Fox fox;

    protected Hen hen;

    protected SessionBean superSessionBean;

    @Inject
    public void initializeHenHouse() {
        this.hen = (fox != null ? new Hen() : null);
    }

    @Resource(name = "game")
    private void setSuperGame(String superGame) {
        this.superGame = superGame;
    }

    @EJB
    public void setSuperSessionBean(SessionBean superSessionBean) {
        this.superSessionBean = superSessionBean;
    }
}
