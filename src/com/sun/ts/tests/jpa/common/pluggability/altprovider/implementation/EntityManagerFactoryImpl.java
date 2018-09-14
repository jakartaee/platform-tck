/*
 * Copyright (c) 2011, 2018 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

package com.sun.ts.tests.jpa.common.pluggability.altprovider.implementation;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.metamodel.Metamodel;
import javax.persistence.spi.PersistenceUnitInfo;
import java.util.Map;

public class EntityManagerFactoryImpl
    implements javax.persistence.EntityManagerFactory {

  public Map properties;

  public boolean isOpen;

  public PersistenceUnitInfo puInfo;

  public boolean containerFactory = false;

  public ClassTransformerImpl transformer;

  public ClassLoader newTempClassloader;

  protected TSLogger logger;

  public EntityManagerFactoryImpl() {
    isOpen = true;
    logger = TSLogger.getInstance();
    logger.log("Called EntityManagerFactoryImpl()");
  }

  public EntityManagerFactoryImpl(boolean containerFactory) {
    super();
    logger = TSLogger.getInstance();
    logger.log("Called EntityManagerFactoryImpl(boolean)");

    isOpen = true;
    this.containerFactory = containerFactory;
  }

  public void addNamedQuery(String s, Query q) {
  }

  public void close() {
    verifyOpen();
    isOpen = false;
  }

  public EntityManager createEntityManager() {
    logger.log("Called EntityManagerFactoryImpl.createEntityManager()");
    verifyOpen();
    EntityManagerImpl em = new EntityManagerImpl();
    em.emf = this;

    return em;
  }

  public EntityManager createEntityManager(Map properties) {
    logger.log("Called EntityManagerFactoryImpl.createEntityManager(Map)");
    verifyOpen();
    EntityManagerImpl em = new EntityManagerImpl();
    em.emf = this;
    em.properties = new java.util.HashMap(properties);
    return em;
  }

  public EntityManager createEntityManager(
      javax.persistence.SynchronizationType st) {
    logger.log("Called EntityManagerFactoryImpl.createEntityManager(Map)");
    verifyOpen();
    EntityManagerImpl em = new EntityManagerImpl();
    em.emf = this;
    em.properties = new java.util.HashMap(properties);
    return em;
  }

  public EntityManager createEntityManager(
      javax.persistence.SynchronizationType st, Map map) {
    logger.log("Called EntityManagerFactoryImpl.createEntityManager(Map)");
    verifyOpen();
    EntityManagerImpl em = new EntityManagerImpl();
    em.emf = this;
    em.properties = new java.util.HashMap(properties);
    return em;
  }

  public Cache getCache() {
    return new CacheImpl();
  }

  public CriteriaBuilder getCriteriaBuilder() {
    return null;
  }

  public Metamodel getMetamodel() {
    return null;
  }

  public PersistenceUnitUtil getPersistenceUnitUtil() {
    return null;
  }

  public Map<String, Object> getProperties() {
    return null;
  }

  public <T> T unwrap(Class<T> arg0) {
    if (EntityManagerImpl.class == arg0) {
      return (T) this;
    }
    return null;
  }

  public <T> void addNamedEntityGraph(String graphName,
      EntityGraph<T> entityGraph) {

  }

  public boolean isOpen() {
    return isOpen;
  }

  // added to check isOpen/closed etc
  protected void verifyOpen() {
    if (!this.isOpen) {
      throw new IllegalStateException(
          "operation_on_closed_entity_manager_factory");
    }
  }
}
