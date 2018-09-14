/*
 * Copyright (c) 2007, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jpa.common;

import com.sun.ts.lib.harness.ServiceEETest;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager;
import com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory;

import javax.persistence.*;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

abstract public class PMClientBase extends ServiceEETest
    implements UseEntityManager, UseEntityManagerFactory, java.io.Serializable {

  protected Properties myProps;

  transient private EntityManager em;

  transient private EntityTransaction et;

  transient private boolean inContainer;

  // The following are properties specific to standalone TCK,
  // not used when running tests in JavaEE environment
  transient private EntityManagerFactory emf;

  EntityManagerFactory javaEEemf;

  public boolean cachingSupported = true;

  transient public static final String PROVIDER_PROP = "javax.persistence.provider";

  transient public static final String TRANSACTION_TYPE_PROP = "javax.persistence.transactionType";

  transient public static final String JTA_DATASOURCE_PROP = "javax.persistence.jtaDataSource";

  transient public static final String NON_JTA_DATASOURCE_PROP = "javax.persistence.nonJtaDataSource";

  transient public static final String RESOURCE_LOCAL = "RESOURCE_LOCAL";

  /**
   * Name of a property defined in ts.jte, to denote whether tests run in JavaEE
   * or Java SE mode.
   */
  transient public static final String MODE_PROP = "platform.mode";

  /**
   * Denotes that tests are running in Java EE mode. This is the only valid
   * non-null value for this property.
   */
  transient public static final String JAVAEE_MODE = "javaEE";

  /**
   * Denotes that tests are running in Java SE mode. This is the only valid
   * non-null value for this property.
   */
  transient public static final String STANDALONE_MODE = "standalone";

  /**
   * Name of a property defined in ts.jte, to specify the name of the
   * persistence unit used in the testsuite. It must be consistent with the
   * value in persistence.xml
   */
  transient public static final String PERSISTENCE_UNIT_NAME_PROP = "persistence.unit.name";

  transient public static final String SECOND_PERSISTENCE_UNIT_NAME_PROP = "persistence.unit.name.2";

  /**
   * Name of the property in ts.jte that specifies an absolute path to the
   * properties file that contains properties for initializing
   * EntityManagerFactory, including both standard and provider-specific
   * properties.
   */
  transient public static final String JAVAX_PERSISTENCE_PROVIDER = "javax.persistence.provider";

  transient public static final String JAVAX_PERSISTENCE_JDBC_DRIVER = "javax.persistence.jdbc.driver";

  transient public static final String JAVAX_PERSISTENCE_JDBC_URL = "javax.persistence.jdbc.url";

  transient public static final String JAVAX_PERSISTENCE_JDBC_USER = "javax.persistence.jdbc.user";

  transient public static final String JAVAX_PERSISTENCE_JDBC_PASSWORD = "javax.persistence.jdbc.password";

  transient public static final String JPA_PROVIDER_IMPLEMENTATION_SPECIFIC_PROPERTIES = "jpa.provider.implementation.specific.properties";

  transient public static final String PERSISTENCE_SECOND_LEVEL_CACHING_SUPPORTED = "persistence.second.level.caching.supported";

  /**
   * The current test mode. The only valid non-null value is "standalone".
   */
  transient private String mode;

  /**
   * Persistence unit name, as defined in ts.jte.
   */
  private String persistenceUnitName;

  protected String secondPersistenceUnitName;

  protected PMClientBase() {
    super();
  }

  protected void removeEntity(Object o) {
    if (o != null) {
      try {
        getEntityManager().remove(o);
      } catch (Exception e) {
        TestUtil.logErr("removeEntity: Exception caught when removing entity: ",
            e);
      }
    }
  }

  public String getPersistenceUnitName() {
    TestUtil.logTrace("getPersistenceUnitName() - Persistence Unit Name:"
        + this.persistenceUnitName);
    return this.persistenceUnitName;
  }

  public String getSecondPersistenceUnitName() {
    TestUtil.logTrace(
        "getSecondPersistenceUnitName() - Second Persistence Unit Name:"
            + this.secondPersistenceUnitName);
    return secondPersistenceUnitName;
  }

  /**
   * If a subclass overrides this method, the overriding implementation must
   * call super.setup() at the beginning.
   */
  public void setup(String[] args, Properties p) throws Fault {
    TestUtil.logTrace("PMClientBase.setup");
    myProps = p;
    mode = p.getProperty(MODE_PROP);
    persistenceUnitName = p.getProperty(PERSISTENCE_UNIT_NAME_PROP);
    TestUtil.logTrace("Persistence Unit Name =" + persistenceUnitName);
    secondPersistenceUnitName = p
        .getProperty(SECOND_PERSISTENCE_UNIT_NAME_PROP);
    TestUtil
        .logTrace("Second Persistence Unit Name =" + secondPersistenceUnitName);
    if (JAVAEE_MODE.equalsIgnoreCase(mode)) {
      TestUtil.logTrace(MODE_PROP + " is set to " + mode
          + ", so tests are running in JavaEE environment.");
    } else if (STANDALONE_MODE.equalsIgnoreCase(mode)) {
      TestUtil.logTrace(MODE_PROP + " is set to " + mode
          + ", so tests are running in J2SE environment standalone mode."
          + PERSISTENCE_UNIT_NAME_PROP + " is set to " + persistenceUnitName);
    } else {
      TestUtil.logMsg("WARNING: " + MODE_PROP + " is set to " + mode
          + ", an invalid value.");
    }

    cachingSupported = Boolean.parseBoolean(
        p.getProperty(PERSISTENCE_SECOND_LEVEL_CACHING_SUPPORTED, "true"));
    if (TestUtil.traceflag) {
      displayProperties(p);
    }
  }

  public void displayProperties(Properties props) {
    TestUtil.logMsg("Current properties:");

    for (Object entry : props.keySet()) {
      if (props.get(entry) instanceof String) {
        TestUtil.logTrace(
            "Key:" + (String) entry + ", value:[" + props.get(entry) + "]");
      } else {
        TestUtil.logTrace("Key:" + (String) entry + ", value:"
            + props.get(entry).getClass().getSimpleName());

      }
    }
  }

  /**
   * In JavaEE environment, does nothing. In Java SE environment, closes the
   * EntityManager if its open, and closes the EntityManagerFactory if its open.
   * If a subclass overrides this method, the overriding implementation must
   * call super.cleanup() at the end. Also, the cache cleared.
   */
  public void cleanup() throws Fault {
    closeEMAndEMF();
  }

  /*
   * This method is inherited by clients and is used when no super.cleanup()
   * call is needed
   */
  public void cleanupNoSuper() throws Fault {
  }

  /**
   * In JavaEE environment, does nothing. In Java SE environment, closes the
   * EntityManager if its open, and closes the EntityManagerFactory if its open.
   * Also, the cache cleared.
   */
  public void closeEMAndEMF() throws Fault {

    try {
      TestUtil.logTrace(
          "Rolling back any existing transaction before closing EMF and EM if one exists.");
      if (getEntityTransaction(false) != null
          && getEntityTransaction(false).isActive()) {
        TestUtil.logTrace("An active transaction was found, rolling it back.");
        getEntityTransaction(false).rollback();
      }
    } catch (Exception fe) {
      TestUtil.logMsg("Unexpected exception rolling back TX:", fe);
    }

    clearCache();

    if (isStandAloneMode()) {
      TestUtil.logTrace("Closing EM and EMF");
      if (getEntityManager(false) != null && getEntityManager(false).isOpen()) {
        getEntityManager(false).close();
      }

      if (getEntityManagerFactory() != null
          && getEntityManagerFactory().isOpen()) {
        getEntityManagerFactory().close();
      }
    }
  }

  public void clearEMAndEMF() throws Fault {
    closeEMAndEMF();
    clearEntityManager();
    clearEntityManagerFactory();
  }

  public void clearEntityManager() {
    if (isStandAloneMode()) {
      this.em = null;
    }
  }

  public void clearEntityManagerFactory() {
    if (isStandAloneMode()) {
      this.emf = null;
    }
  }

  public void clearCache() throws Fault {

    if (cachingSupported) {
      if (getEntityManager() != null && getEntityManager().isOpen()) {
        if (getEntityManager().getEntityManagerFactory() != null
            && getEntityManager().getEntityManagerFactory().isOpen()) {

          // if the EntityManager is open, clear the context causing all
          // managed entities to become detached
          TestUtil.logTrace("Clearing context");
          getEntityManager().clear();

          TestUtil.logTrace(
              "Trying to clear cache via call to EMF.getCache().evictAll().");
          Cache cache = getEntityManager().getEntityManagerFactory().getCache();
          if (cache != null) {
            cache.evictAll();
            TestUtil.logTrace("EMF.getCache().evictAll() was executed.");
          } else {
            TestUtil.logErr(
                "Cache supported is true in ts.jte but getCache() is returning null");
          }

        } else {
          TestUtil.logTrace(
              "Clearing of cache did not occur because either EntityManagerFactory was null or closed");
        }
      } else {
        TestUtil.logTrace(
            "Clearing of cache did not occur because either EntityManager was null or closed");
      }
    } else {
      TestUtil.logTrace(
          "Clearing of cache did not occur because it is not supposed.");
    }
  }

  public void setEntityManager(javax.persistence.EntityManager em) {
    this.em = em;
  }

  public void setEntityManagerFactory(EntityManagerFactory emf) {
    this.javaEEemf = emf;
  }

  public EntityManager getEntityManager() {
    // get exist entity manager and use the default persistence unit name
    return getEntityManager(false);
  }

  public EntityManager getEntityManager(boolean reInit) {
    // If reInit=false then it will return existing EntityManager
    // otherwise this returns new EntityManager
    if (!reInit) {
      if (this.em != null) {
        TestUtil.logTrace("Using existing entity manager class:"
            + em.getClass().getName() + " isOpen:" + em.isOpen());
        return this.em;
      }
      TestUtil.logTrace("getEntityManager: false was specified and EM is null");
    }
    TestUtil.logTrace("Need to Initialize EntityManager");
    if (isStandAloneMode()) {
      initEntityManager(persistenceUnitName, true);
      TestUtil.logTrace("EntityManager class:" + em.getClass().getName()
          + " isOpen:" + em.isOpen());
      return this.em;
    }
    throw new IllegalStateException(
        "The test is running in JavaEE environment, "
            + "but PMClientBase.em has not been initialized from the vehicle component.");
  }

  public EntityManagerFactory getEntityManagerFactory() {
    if (isStandAloneMode()) {
      if (emf != null) {
        TestUtil.logTrace("EntityManagerFactory class:"
            + emf.getClass().getName() + " isOpen:" + emf.isOpen());
      }
      return emf;
    } else {
      if (javaEEemf != null) {
        TestUtil.logTrace("EntityManagerFactory class:"
            + javaEEemf.getClass().getName() + " isOpen:" + javaEEemf.isOpen());
      }
      return javaEEemf;
    }
  }

  public void clearEntityTransaction() {
    this.et = null;
  }

  public void setEntityTransaction(EntityTransaction et) {
    this.et = et;
  }

  public EntityTransaction getEntityTransaction() {
    if (this.et != null) {
      TestUtil.logTrace("Transaction class:" + et.getClass().getName()
          + " isActive():" + et.isActive());
      return this.et;
    }
    TestUtil.logTrace("Need to get Transaction");
    if (isStandAloneMode()) {
      initEntityTransaction();
      if (et != null) {
        TestUtil.logTrace("Transaction class:" + et.getClass().getName()
            + " isActive():" + et.isActive());
      }
      return this.et;
    }
    throw new IllegalStateException(
        "The test is running in JavaEE environment, "
            + "but PMClientBase.et has not been initialized from the vehicle component.");
  }

  public EntityTransaction getEntityTransaction(boolean reInit,
      EntityManager em) {
    // If reInit=false then it will return existing EntityTransaction
    // otherwise this returns new EntityTransaction
    if (!reInit) {
      if (this.et != null) {
        TestUtil.logTrace("Transaction class:" + et.getClass().getName()
            + " isActive():" + et.isActive());
        return this.et;
      }
    }
    if (isStandAloneMode()) {
      initEntityTransaction(em);
      if (et != null) {
        TestUtil.logTrace("Transaction class:" + et.getClass().getName()
            + " isActive():" + et.isActive());
      }
      return this.et;
    }
    throw new IllegalStateException(
        "The test is running in JavaEE environment, "
            + "but PMClientBase.et has not been initialized from the vehicle component.");
  }

  public EntityTransaction getEntityTransaction(boolean reInit) {
    // If reInit=false then it will return existing EntityTransaction
    // otherwise this returns new EntityTransaction
    if (!reInit) {
      if (this.et != null) {
        TestUtil.logTrace("Transaction class:" + et.getClass().getName()
            + " isActive():" + et.isActive());
        return this.et;
      }
    }
    if (isStandAloneMode()) {
      initEntityTransaction();
      if (et != null) {
        TestUtil.logTrace("Transaction class:" + et.getClass().getName()
            + " isActive():" + et.isActive());
      }
      return this.et;
    }
    throw new IllegalStateException(
        "The test is running in JavaEE environment, "
            + "but PMClientBase.et has not been initialized from the vehicle component.");
  }

  /**
   * Creates EntityManager in JavaSE environment. In JavaEE environment,
   * EntityManager should already have been set from within the vehicle.
   */
  protected void initEntityManager(String persistenceUnitName,
      boolean useProps) {
    if (isStandAloneMode()) {
      TestUtil.logTrace(
          "in initEntityManager(String, boolean): " + persistenceUnitName);
      if (useProps) {
        Properties propsMap = getPersistenceUnitProperties();
        TestUtil.logTrace("createEntityManagerFactory(String,Map)");
        emf = Persistence.createEntityManagerFactory(persistenceUnitName,
            propsMap);
      } else {
        TestUtil.logTrace("createEntityManagerFactory(String)");
        emf = Persistence.createEntityManagerFactory(persistenceUnitName);
      }
      Map<java.lang.String, java.lang.Object> emfMap = emf.getProperties();
      if (emfMap != null) {
        displayMap(emfMap);
      }
      this.em = emf.createEntityManager();
    } else {
      TestUtil.logMsg("The test is running in JavaEE environment, "
          + "the EntityManager is initialized in the vehicle component.");
    }
  }

  /**
   * Creates EntityTransaction in JavaSE environment. In JavaEE environment,
   * EntityManager should already have been set from within the vehicle.
   */
  protected void initEntityTransaction() {
    EntityTransaction delegate = getEntityManager().getTransaction();
    this.et = delegate;
  }

  /**
   * Creates EntityTransaction in JavaSE environment. In JavaEE environment,
   * EntityManager should already have been set from within the vehicle.
   */
  protected void initEntityTransaction(EntityManager em) {
    EntityTransaction delegate = em.getTransaction();
    this.et = delegate;
  }

  public boolean isInContainer() {
    return inContainer;
  }

  public void setInContainer(boolean inContainer) {
    this.inContainer = inContainer;
  }

  /*
   * Properties needed for Standalone TCK persistence.xml
   */
  protected Properties getPersistenceUnitProperties() {
    Properties jpaProps = new Properties();
    jpaProps.put(JAVAX_PERSISTENCE_PROVIDER,
        myProps.get(JAVAX_PERSISTENCE_PROVIDER));
    jpaProps.put(JAVAX_PERSISTENCE_JDBC_DRIVER,
        myProps.get(JAVAX_PERSISTENCE_JDBC_DRIVER));
    jpaProps.put(JAVAX_PERSISTENCE_JDBC_URL,
        myProps.get(JAVAX_PERSISTENCE_JDBC_URL));
    jpaProps.put(JAVAX_PERSISTENCE_JDBC_USER,
        myProps.get(JAVAX_PERSISTENCE_JDBC_USER));
    jpaProps.put(JAVAX_PERSISTENCE_JDBC_PASSWORD,
        myProps.get(JAVAX_PERSISTENCE_JDBC_PASSWORD));
    String provider_specific_props = (String) myProps
        .get(JPA_PROVIDER_IMPLEMENTATION_SPECIFIC_PROPERTIES);

    StringTokenizer st = new StringTokenizer(provider_specific_props, ":");
    while (st.hasMoreTokens()) {
      StringTokenizer st1 = new StringTokenizer(st.nextToken(), "=");
      String pspName, pspValue;
      pspName = pspValue = null;
      if (st1.hasMoreTokens()) {
        pspName = st1.nextToken();
      }
      if (st1.hasMoreTokens()) {
        pspValue = st1.nextToken();
      }
      jpaProps.put(pspName, pspValue);

    }
    checkPersistenceUnitProperties(jpaProps);
    return jpaProps;
  }

  public boolean isStandAloneMode() {
    if (STANDALONE_MODE.equalsIgnoreCase(mode)) {
      return true;
    }
    return false;
  }

  /**
   * Verifies certain properties that are not applicable in Java SE environment
   * are not filtered out, and not passed to
   * Persistence.createEntityManagerFactory.
   */
  private void checkPersistenceUnitProperties(Properties jpaProps) {
    TestUtil.logTrace(
        "persistence unit properites from user: " + jpaProps.toString());
    String provider = jpaProps.getProperty(PROVIDER_PROP);
    if (provider == null) {
      throw new IllegalStateException(
          PROVIDER_PROP + " not specified in persistence unit properties file");
    }
    String transactionType = jpaProps.getProperty(TRANSACTION_TYPE_PROP);
    if (transactionType != null && !RESOURCE_LOCAL.equals(transactionType)) {
      throw new IllegalStateException(TRANSACTION_TYPE_PROP
          + " is set to an unsupported value: " + transactionType
          + ".  The only portably supported type is " + RESOURCE_LOCAL
          + ".  Please correct it in persistence unit properties file.");
    }
    String jtaDataSource = jpaProps.getProperty(JTA_DATASOURCE_PROP);
    if (jtaDataSource != null) {
      TestUtil.logMsg("WARNING: " + JTA_DATASOURCE_PROP + " is specified as "
          + jtaDataSource + ", and it will be passed to the persistence "
          + "provider.  However, this is in general not supported in "
          + "Java SE environment");
      // jpaProps.remove(JTA_DATASOURCE_PROP);
    }
    // String nonJtaDataSource = jpaProps.getProperty(NON_JTA_DATASOURCE_PROP);
    // if(nonJtaDataSource == null) {
    // throw new IllegalStateException(NON_JTA_DATASOURCE_PROP + " is required "
    // +
    // "in Java SE environment. It has not been specified. Please " +
    // "set it in persistence unit properties file.");
    // }
    TestUtil.logTrace(
        "persistence unit properites verified: " + jpaProps.toString());
  }

  public Calendar getCalDate() {
    return Calendar.getInstance();
  }

  public Calendar getCalDate(final int yy, final int mm, final int dd) {
    Calendar cal = new GregorianCalendar(yy, mm, dd);
    TestUtil.logTrace("returning date:" + cal);
    return cal;
  }

  public java.sql.Date getSQLDate(final String sDate) {
    Date d = java.sql.Date.valueOf(sDate);
    TestUtil.logTrace("returning date:" + d);
    return d;
  }

  public java.sql.Date getSQLDate(final int yy, final int mm, final int dd) {
    Calendar newCal = getCalDate();
    newCal.clear();
    newCal.set(yy, mm, dd);
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    String sDate = sdf.format(newCal.getTime());
    TestUtil.logTrace("returning date:" + java.sql.Date.valueOf(sDate));
    return java.sql.Date.valueOf(sDate);
  }

  public java.sql.Date getSQLDate() {
    Calendar calDate = getCalDate();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    String sDate = sdf.format(calDate.getTime());
    java.sql.Date d = java.sql.Date.valueOf(sDate);
    TestUtil.logTrace("returning date:" + d);
    return d;
  }

  public java.util.Date getUtilDate() {
    java.util.Date d = new java.util.Date();
    TestUtil.logTrace("getPKDate: returning date:" + d);
    return d;
  }

  public java.util.Date getUtilDate(final String sDate) {
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    java.util.Date d = null;
    try {
      d = formatter.parse(sDate);
    } catch (ParseException pe) {
      TestUtil.logErr("Received unexpected exception:" + pe);
    }
    TestUtil.logTrace("getPKDate: returning date:" + d);
    return d;
  }

  public java.sql.Time getTimeData(final String sTime) {
    java.sql.Time t = java.sql.Time.valueOf(sTime);
    TestUtil.logTrace("getTimeData: returning Time:" + t);
    return t;
  }

  public java.sql.Time getTimeData(final int hh, final int mm, final int ss) {
    Calendar newCal = Calendar.getInstance();
    newCal.clear();
    newCal.set(hh, mm, ss);
    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
    String sDate = sdf.format(newCal.getTime());
    TestUtil.logTrace(
        "getTimeData: returning Time:" + java.sql.Time.valueOf(sDate));
    return java.sql.Time.valueOf(sDate);
  }

  public java.sql.Timestamp getTimestampData(final String sDate) {
    String tFormat = sDate + " " + "10:10:10";
    java.sql.Timestamp ts = java.sql.Timestamp.valueOf(tFormat);
    TestUtil.logTrace("getTimestampData: returning TimeStamp:" + ts);
    return ts;
  }

  public Timestamp getTimestampData(final int yy, final int mm, final int dd) {
    TestUtil.logTrace("Entering getTimestampData");
    Calendar newCal = Calendar.getInstance();
    newCal.clear();
    newCal.set(yy, mm, dd);
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String sDate = sdf.format(newCal.getTime());
    TestUtil.logTrace("getTimestampData: returning TimeStamp:"
        + java.sql.Timestamp.valueOf(sDate));
    return java.sql.Timestamp.valueOf(sDate);
  }

  public java.util.Date getPKDate(final int yy, final int mm, final int dd) {
    Calendar newCal = Calendar.getInstance();
    newCal.clear();
    newCal.set(yy, mm, dd);
    TestUtil.logTrace("getPKDate: returning date:" + newCal.getTime());
    return newCal.getTime();
  }

  public void doFlush() throws PersistenceException {
    // TestUtil.logTrace("Entering doFlush method");
    try {
      getEntityManager().flush();
    } catch (PersistenceException pe) {
      TestUtil.logErr("Unexpected Exception caught while flushing: ", pe);
      throw new PersistenceException(
          "Unexpected Exception caught while flushing: " + pe);
    }
  }

  public <T> boolean checkEntityPK(Collection<T> actualPKS,
      Collection<T> expectedPKS) {
    TestUtil
        .logTrace("PMClientBase.checkEntityPK(Collection<T>,Collection<T>)");
    return checkEntityPK(actualPKS, expectedPKS, false);
  }

  public <T> boolean checkEntityPK(final Collection<T> actualPKS,
      final Collection<T> expectedPKS, final boolean allowDups) {
    Integer epks2[] = new Integer[expectedPKS.size()];
    TestUtil.logTrace(
        "PMClientBase.checkEntityPK(Collection<T>,Collection<T>, boolean)");
    if (expectedPKS.size() != 0) {
      int i = 0;
      for (T o : expectedPKS) {
        T ref = (T) o;

        try {
          if (ref instanceof Integer) {
            epks2[i++] = (Integer) ref;
          } else if (ref instanceof String) {
            epks2[i++] = Integer.valueOf((String) ref);
          } else {
            Method m = ref.getClass().getMethod("getId");
            Object oo = m.invoke(ref);
            if (oo instanceof String) {
              epks2[i++] = Integer.valueOf((String) oo);
            } else {
              epks2[i++] = (Integer) oo;
            }
          }
        } catch (NoSuchMethodException nsme) {
          TestUtil.logErr("Unexpected exception thrown", nsme);
        } catch (IllegalAccessException iae) {
          TestUtil.logErr("Unexpected exception thrown", iae);
        } catch (InvocationTargetException ite) {
          TestUtil.logErr("Unexpected exception thrown", ite);
        }
      }
    }
    return checkEntityPK(actualPKS, epks2, allowDups, true);
  }

  public <T> boolean checkEntityPK(final Collection<T> actualPKS,
      final String expectedPKS[]) {
    TestUtil.logTrace("PMClientBase.checkEntityPK(Collection<T>,String[])");
    return checkEntityPK(actualPKS, expectedPKS, false);
  }

  public <T> boolean checkEntityPK(Collection<T> actualPKS,
      String expectedPKS[], boolean allowDups) {
    Integer epks2[] = new Integer[expectedPKS.length];
    TestUtil.logTrace(
        "PMClientBase.checkEntityPK(Collection<T>,String[], boolean)");
    if (expectedPKS.length != 0) {
      int i = 0;
      for (String s : expectedPKS) {
        epks2[i++] = Integer.valueOf(s);
      }
    }
    return checkEntityPK(actualPKS, epks2, allowDups, true);
  }

  public <T> boolean checkEntityPK(final Collection<T> actualPKS,
      final Integer expectedPKS[]) {
    TestUtil.logTrace("PMClientBase.checkEntityPK(Collection<T>,Integer[])");
    return checkEntityPK(actualPKS, expectedPKS, false, true);
  }

  public <T> boolean checkEntityPK(final Collection<T> actualPKS,
      final Integer expectedPKS[], final boolean allowDups, boolean sortLists) {
    Integer cpks2[] = new Integer[actualPKS.size()];
    String cpks;
    String epks;
    TestUtil.logTrace(
        "PMClientBase.checkEntityPK(Collection<T>,Integer[], boolean)");
    try {
      if (expectedPKS.length == 0) {
        epks = "()";
      } else {
        if (sortLists) {
          Arrays.sort(expectedPKS);
        }
        epks = createStringVersionOfPKS(expectedPKS);
      }
      if (actualPKS.size() == 0) {
        cpks = "()";
      } else {

        int k = 0;
        for (T o : actualPKS) {
          T ref = (T) o;

          try {
            if (ref instanceof Integer) {
              cpks2[k++] = (Integer) ref;
            } else if (ref instanceof String) {
              cpks2[k++] = Integer.valueOf((String) ref);
            } else {
              Method m = ref.getClass().getMethod("getId");
              Object oo = m.invoke(ref);
              if (oo instanceof String) {
                cpks2[k++] = Integer.valueOf((String) oo);
              } else {
                cpks2[k++] = (Integer) oo;
              }
            }
            // TestUtil.logTrace("cpks2[" + k + "]=" + cpks2[k]);
          } catch (NoSuchMethodException nsme) {
            TestUtil.logErr("Unexpected exception thrown", nsme);
          } catch (IllegalAccessException iae) {
            TestUtil.logErr("Unexpected exception thrown", iae);
          } catch (InvocationTargetException ite) {
            TestUtil.logErr("Unexpected exception thrown", ite);
          }
        }
      }
      if (sortLists) {
        Arrays.sort(cpks2);
      }
      cpks = createStringVersionOfPKS(cpks2);

      if (checkWrongSize(expectedPKS, cpks2)) {
        TestUtil.logErr("Wrong size returned, expected " + "PKs of " + epks
            + ", got PKs of " + cpks);
        return false;
      }
      if (!allowDups) {
        if (checkDuplicates(cpks2)) {
          TestUtil.logErr("Duplicate values returned, expected " + "PKs of "
              + epks + ", got PKs of " + cpks);
          return false;
        }
      }
      if (!Arrays.equals(expectedPKS, cpks2)) {
        TestUtil.logErr("Wrong values returned, expected PKs of " + epks
            + ", got PKs of " + cpks);
        return false;
      }

    } catch (Exception e) {
      TestUtil.logErr(
          "Exception in PMClientBase.checkEntityPK(Collection<T>,Integer[]: ",
          e);
      return false;
    }

    return true;
  }

  public String createStringVersionOfPKS(final Integer[] iArray) {
    StringBuilder sb = new StringBuilder(2);
    sb.append("(");

    for (int i = 0; i < iArray.length; i++) {
      sb.append(iArray[i]);
      if (i < iArray.length - 1) {
        sb.append(", ");
      }
    }
    sb.append(")");
    return sb.toString();
  }

  public boolean checkWrongSize(final Integer[] expected,
      final Integer[] actual) {
    TestUtil.logTrace("PMClientbase.checkWrongSize");
    if (expected.length != actual.length) {
      TestUtil.logErr("Wrong collection size returned (expected "
          + expected.length + ", got " + actual.length + ")");
      return true;
    }
    return false;
  }

  public boolean checkDuplicates(final Integer[] iArray) {
    TestUtil.logTrace("PMClientbase.checkDuplicates");
    boolean duplicates = false;
    for (int ii = 0; ii < iArray.length; ii++) {
      for (int j = 0; j < iArray.length; j++) {
        if (ii == j)
          continue;
        if (iArray[ii].equals(iArray[j])) {
          duplicates = true;
          break;
        }
      }
    }
    if (duplicates) {
      TestUtil.logErr("Wrong collection contents returned "
          + "(contains duplicate entries)");
      return true;
    }
    return false;
  }

  public String compareResultList(final List expected, final List actual) {
    String reason;
    if (expected.equals(actual)) {
      reason = "Got expected result list: " + expected;
      TestUtil.logTrace(reason);
    } else {
      reason = "Expecting result list: " + expected + "  , actual: " + actual;
      throw new RuntimeException(reason);
    }
    return reason;
  }

  public void displayMap(Map map) {

    if (map != null) {
      Set<Map.Entry<String, Object>> set = map.entrySet();

      for (Map.Entry<String, Object> me : set) {
        if (me.getValue() instanceof String) {
          TestUtil.logTrace(
              "Map - name:" + me.getKey() + ", value:" + me.getValue());
        } else {
          TestUtil.logTrace("Map - name:" + me.getKey() + ", value:"
              + me.getValue().getClass().getName());
        }
      }
    } else {
      TestUtil.logTrace("Map passed in to displayMap was null");
    }
  }

  public int convertToInt(Object o) {
    if (o instanceof Integer) {
      return (Integer) o;
    } else if (o instanceof BigDecimal) {
      return ((BigDecimal) o).intValue();
    } else if (o instanceof Long) {
      return ((Long) o).intValue();
    }
    throw new IllegalArgumentException(
        "convertToInt(): Received type not coded for:"
            + o.getClass().getName());
  }

  public boolean findDataInFile(File file, String searchString) {
    List<String> list = new ArrayList<String>();
    list.add(searchString);
    return findDataInFile(file, list);
  }

  public boolean findDataInFile(File file, List<String> searchStrings) {
    boolean found = false;

    List<String> fileContents = getFileContent(file);
    int fileContentLineCount = 0;
    int foundCount = 0;

    // cycle through all the search strings and all the file contents
    // until it is found or we exhaust them all
    while (!found && foundCount != searchStrings.size()
        && fileContentLineCount != fileContents.size()) {

      for (String fileContent : fileContents) {
        fileContentLineCount++;
        foundCount = 0;
        // TestUtil.logTrace("----------------");
        // TestUtil.logTrace("line:"+fileContent);

        for (String searchString : searchStrings) {
          TestUtil.logTrace("searchString:" + searchString);

          if (fileContent.toLowerCase()
              .indexOf(searchString.toLowerCase()) >= 0) {
            TestUtil.logTrace("Found string[" + searchString + "] in line ["
                + fileContent + "]");
            foundCount++;
            // TestUtil.logTrace("foundCount:"+foundCount);
            // } else {
            // TestUtil.logTrace("^^^^^^^^^^^^^^^^^^");
            //
            // TestUtil.logTrace("index="+fileContent.toLowerCase().indexOf(searchString.toLowerCase()));
            // TestUtil.logTrace("fileContent.toLowerCase["+fileContent.toLowerCase()+"],
            // searchString.toLowerCase["+searchString.toLowerCase()+"]");
            // TestUtil.logTrace("^^^^^^^^^^^^^^^^^^");

          }
        }
        // TestUtil.logTrace("----------------");
        //
        // TestUtil.logTrace("foundCount:"+foundCount+",
        // searchStrings.size():"+searchStrings.size());

        if (foundCount == searchStrings.size()) {
          found = true;
          break;
        }
      }
    }
    // TestUtil.logTrace("foundCount:"+foundCount+",
    // searchStrings.size():"+searchStrings.size());
    // TestUtil.logTrace("fileContentLineCount:"+fileContentLineCount+",
    // fileContents.size():"+fileContents.size());
    // TestUtil.logTrace("found:"+found);

    if (!found) {
      if (searchStrings.size() > 1) {
        StringBuffer sb = new StringBuffer();
        int count = 0;
        for (String s : searchStrings) {
          sb.append(s);
          count++;
          if (count != searchStrings.size()) {
            sb.append(" and ");
          }
        }
        TestUtil.logErr("Entries:" + sb.toString() + ", not found in file:"
            + file.toString());
        for (String s : fileContents) {
          TestUtil.logErr("File line[" + s + "]");
        }

      } else {
        TestUtil.logErr("Entry:" + searchStrings.get(0) + ", not found in file:"
            + file.toString());

      }
    }

    return found;
  }

  public List<String> getFileContent(File file) {
    List<String> list = new ArrayList<String>();
    try {
      if (file.exists()) {
        TestUtil.logTrace("found file:" + file.getAbsolutePath());
        if (file.length() > 0) {
          BufferedReader input = new BufferedReader(new FileReader(file));
          try {
            String line = null;
            while ((line = input.readLine()) != null) {
              TestUtil.logTrace("read line:" + line);
              line = line.trim();
              list.add(line);
            }

          } finally {
            input.close();
          }
        } else {
          TestUtil.logErr("File is empty: " + file.getAbsolutePath());
        }
      } else {
        TestUtil.logErr(
            "Specified file " + file.getAbsolutePath() + " does not exist");
      }
    } catch (IOException ex) {
      ex.printStackTrace();
    }
    return list;
  }

  public void deleteItem(File file) {
    if (file.exists()) {
      TestUtil.logTrace("item:" + file.getAbsolutePath() + " exists");
      if (file.isDirectory()) {
        // item is a directory
        File[] files = file.listFiles();
        if (files != null) {
          for (File f : files) {
            deleteItem(f);
          }
          // delete the empty directory
          if (file.delete()) {
            TestUtil
                .logTrace("directory: " + file.getAbsolutePath() + " deleted");
            if (file.exists()) {
              TestUtil
                  .logErr("directory still exists even after calling delete");
            }
          } else {
            TestUtil.logErr(
                "Could not delete directory: " + file.getAbsolutePath());
          }

        } else {
          TestUtil.logErr("listFiles returned null");
        }
      } else {
        // item is a file
        if (file.delete()) {
          TestUtil.logTrace("file: " + file.getAbsolutePath() + " deleted");
          if (file.exists()) {
            TestUtil.logErr("file still exists even after calling delete");
          }
        } else {
          TestUtil.logErr("Could not delete file: " + file.getAbsolutePath());
        }
      }
    } else {
      TestUtil.logTrace("file:" + file.getAbsolutePath() + " does not exist");
    }
  }

  public String convertToURI(String path) {
    String sURI = null;
    try {
      sURI = new File(path).toURI().toASCIIString();
      TestUtil.logTrace("URI=" + sURI);
    } catch (Exception ue) {
      TestUtil.logErr("Received unexpected exception for path:" + path, ue);
    }
    return sURI;
  }
}
