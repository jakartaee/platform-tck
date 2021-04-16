/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
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

package org.jboss.cdi.tck.tests.implementation.simple.resource.persistenceContext.staticProducer;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

/**
 * @author Marius Bogoevici
 */
@Stateless
public class ServiceBeanImpl implements ServiceBean {
    @Inject
    ManagedBean managedBean;

    public ManagedBean getManagedBean() {
        return managedBean;
    }

    public boolean validateEntityManager() {
        boolean entityManagerValid = true;
        EntityManager entityManager = managedBean.getPersistenceContext();
        assert entityManager != null;
        Object delegate = entityManager.getDelegate();
        assert delegate != null;
        try {
            entityManager.getTransaction();
            entityManagerValid = false;
        } catch (IllegalStateException e) {
            // an IllegalStateException is the expected result if this is a JTA entityManager
        }
        try {
            entityManager.close();
            entityManagerValid = false;
        } catch (IllegalStateException e) {
            // an IllegalStateException is the expected result if the entityManager is container-managed
        }
        return entityManagerValid;
    }
}
