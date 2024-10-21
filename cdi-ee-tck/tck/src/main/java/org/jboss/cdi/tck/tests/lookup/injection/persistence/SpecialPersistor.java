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
package org.jboss.cdi.tck.tests.lookup.injection.persistence;

import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceUnit;

@Dependent
public class SpecialPersistor extends Persistor {

    @PersistenceContext
    EntityManager persistenceContextField;
    
    EntityManager persistenceContext;

    @PersistenceUnit
    EntityManagerFactory persistenceUnitField;

    EntityManagerFactory persistenceUnit;

    public boolean initializerCalledAfterResourceInjection = false;

    @Inject
    public void initialize() {
        initializerCalledAfterResourceInjection = (persistenceContextField != null && persistenceContext != null
                && superPersistenceContextField != null && superPersistenceContext != null
                && persistenceUnitField != null && persistenceUnit != null
                && superPersistenceUnitField != null && superPersistenceUnit != null);
    }

    @PersistenceContext
    private void setPersistenceContext(EntityManager persistenceContext) {
        this.persistenceContext = persistenceContext;
    }

    @PersistenceUnit
    private void setPersistenceUnit(EntityManagerFactory persistenceUnit) {
        this.persistenceUnit = persistenceUnit;
    }
}
