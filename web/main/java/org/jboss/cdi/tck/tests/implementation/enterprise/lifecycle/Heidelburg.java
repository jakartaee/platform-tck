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
package org.jboss.cdi.tck.tests.implementation.enterprise.lifecycle;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.ejb.Remove;
import jakarta.ejb.Stateful;
import jakarta.inject.Inject;

@Stateful
@Important
public class Heidelburg implements KleinStadt, SchoeneStadt {
    private String name;

    @Inject
    private GrossStadt grossStadt;

    @PostConstruct
    public void begruendet() {
        grossStadt.kleinStadtCreated();
        name = "Heidelburg";
    }

    @Remove
    public void zustandVergessen() {
    }

    @PreDestroy
    public void zustandVerloren() {
        grossStadt.kleinStadtDestroyed();
    }

    public void ping() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
