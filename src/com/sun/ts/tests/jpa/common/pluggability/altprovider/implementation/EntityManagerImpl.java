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

import java.util.List;
import java.util.Map;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.metamodel.Metamodel;

public class EntityManagerImpl implements javax.persistence.EntityManager {
  /**
   * Store if this entity manager has been closed for test
   * closeAfterCloseCausesISException
   */
  protected boolean isOpen;

  // state required for public methods
  public EntityManagerFactoryImpl emf;

  public Map properties;

  protected TSLogger logger;

  public EntityManagerImpl() {
    logger = TSLogger.getInstance();
    isOpen = true;
    logger.log("Called EntityManagerImpl()");

  }

  @Override
  public void clear() {
    logger.log("Called EntityManagerImpl.clear()");

  }

  @Override
  public void close() {
    logger.log("Called EntityManagerImpl.close()");
    verifyOpen();
    isOpen = false;
  }

  @Override
  public boolean contains(Object arg0) {
    return false;
  }

  @Override
  public Query createNamedQuery(String arg0) {
    QueryImpl query = new QueryImpl();
    query.name = arg0;
    return query;
  }

  @Override
  public <T> TypedQuery<T> createNamedQuery(String arg0, Class<T> arg1) {
    QueryImpl query = new QueryImpl();
    query.name = arg0;
    query.queryOnClass = arg1;
    return (TypedQuery<T>) query;
  }

  @Override
  public Query createNativeQuery(String arg0) {
    QueryImpl query = new QueryImpl();
    query.nativeSQL = arg0;
    return query;
  }

  @Override
  public Query createNativeQuery(String arg0, Class arg1) {
    QueryImpl query = new QueryImpl();
    query.nativeSQL = arg0;
    query.queryOnClass = arg1;
    return query;
  }

  @Override
  public Query createNativeQuery(String arg0, String arg1) {
    QueryImpl query = new QueryImpl();
    query.nativeSQL = arg0;
    query.resultsetMapping = arg1;
    return query;
  }

  @Override
  public Query createQuery(String arg0) {
    QueryImpl query = new QueryImpl();
    query.jpQL = arg0;
    return query;
  }

  @Override
  public <T> TypedQuery<T> createQuery(CriteriaQuery<T> arg0) {
    return null;
  }

  @Override
  public <T> TypedQuery<T> createQuery(String arg0, Class<T> arg1) {
    QueryImpl query = new QueryImpl();
    query.jpQL = arg0;
    query.queryOnClass = arg1;
    return (TypedQuery<T>) query;
  }

  @Override
  public Query createQuery(CriteriaDelete arg) {
    return null;
  }

  @Override
  public Query createQuery(CriteriaUpdate arg) {
    return null;
  }

  @Override
  public StoredProcedureQuery createNamedStoredProcedureQuery(String name) {
    return null;
  }

  @Override
  public StoredProcedureQuery createStoredProcedureQuery(String name) {
    return null;
  }

  @Override
  public StoredProcedureQuery createStoredProcedureQuery(String name,
      java.lang.Class[] resultClasses) {
    return null;
  }

  @Override
  public StoredProcedureQuery createStoredProcedureQuery(String name,
      String[] resultSetMappings) {
    return null;
  }

  @Override
  public void detach(Object arg0) {

  }

  @Override
  public <T> T find(Class<T> arg0, Object arg1) {
    return null;
  }

  @Override
  public <T> T find(Class<T> arg0, Object arg1, Map<String, Object> arg2) {
    return null;
  }

  @Override
  public <T> T find(Class<T> arg0, Object arg1, LockModeType arg2) {
    return null;
  }

  @Override
  public <T> T find(Class<T> arg0, Object arg1, LockModeType arg2,
      Map<String, Object> arg3) {
    return null;
  }

  @Override
  public void flush() {

  }

  @Override
  public CriteriaBuilder getCriteriaBuilder() {
    return null;
  }

  @Override
  public Object getDelegate() {
    verifyOpen();
    return this;
  }

  @Override
  public EntityManagerFactory getEntityManagerFactory() {
    return emf;
  }

  @Override
  public FlushModeType getFlushMode() {
    return null;
  }

  @Override
  public LockModeType getLockMode(Object arg0) {
    return null;
  }

  @Override
  public Metamodel getMetamodel() {
    return null;
  }

  @Override
  public Map<String, Object> getProperties() {
    return null;
  }

  @Override
  public <T> T getReference(Class<T> arg0, Object arg1) {
    return null;
  }

  @Override
  public EntityTransaction getTransaction() {
    logger.log("Called EntityManagerImpl.getTransaction()");

    return new EntityTransactionImpl();
  }

  @Override
  public boolean isOpen() {
    logger.log("Called EntityManagerImpl.isOpen()");

    return isOpen;
  }

  @Override
  public boolean isJoinedToTransaction() {
    return false;
  }

  @Override
  public void joinTransaction() {

  }

  @Override
  public void lock(Object arg0, LockModeType arg1) {

  }

  @Override
  public void lock(Object arg0, LockModeType arg1, Map<String, Object> arg2) {

  }

  @Override
  public <T> T merge(T arg0) {
    return arg0;// not cloning it in case the object can't be cloned
  }

  @Override
  public void persist(Object arg0) {

  }

  @Override
  public void refresh(Object arg0) {

  }

  @Override
  public void refresh(Object arg0, Map<String, Object> arg1) {

  }

  @Override
  public void refresh(Object arg0, LockModeType arg1) {

  }

  @Override
  public void refresh(Object arg0, LockModeType arg1,
      Map<String, Object> arg2) {

  }

  @Override
  public void remove(Object arg0) {

  }

  @Override
  public void setFlushMode(FlushModeType arg0) {

  }

  @Override
  public void setProperty(String arg0, Object arg1) {

  }

  @Override
  public <T> T unwrap(Class<T> arg0) {
    if (EntityManagerImpl.class == arg0) {
      return (T) this;
    }
    return null;
  }

  public <T> List<EntityGraph<? super T>> getEntityGraphs(
      Class<T> entityClass) {
    return null;
  }

  public EntityGraph<?> getEntityGraph(String graphName) {
    return null;
  }

  public <T> EntityGraph<T> createEntityGraph(Class<T> rootType) {
    return null;
  }

  public EntityGraph<?> createEntityGraph(String graphName) {
    return null;
  }

  /**
   * verify if this entity manager has been closed for test
   * closeAfterCloseCausesISException
   */
  public void verifyOpen() {
    if (!this.isOpen /* || !this.factory.isOpen() */) {
      throw new IllegalStateException(
          "Attempting to execute an operation on a closed EntityManager.");
    }
  }

}
