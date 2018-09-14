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

package com.sun.ts.tests.jpa.ee.pluggability.contracts.resource_local;

import com.sun.javatest.Status;
import com.sun.ts.tests.jpa.common.pluggability.altprovider.implementation.EntityManagerFactoryImpl;
import com.sun.ts.tests.jpa.common.pluggability.altprovider.implementation.EntityManagerImpl;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jpa.common.PMClientBase;

import javax.persistence.SharedCacheMode;
import javax.persistence.ValidationMode;
import javax.persistence.spi.PersistenceUnitInfo;
import javax.persistence.spi.PersistenceUnitTransactionType;

import com.sun.ts.tests.jpa.common.pluggability.util.LogFileProcessor;

import java.net.URL;
import java.util.*;

public class Client extends PMClientBase {

  private transient EntityManagerImpl emImpl;

  private transient EntityManagerFactoryImpl emfImpl;

  private PersistenceUnitInfo puInfo;

  private long currentSeqNum = 0L;

  private static LogFileProcessor logProcessor = null;

  private String LOGMESSAGE_PREFIX = "JPA_ALTERNATE_PROVIDER : ";

  public Client() {
  }

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /*
   * @class.setup_props: log.file.location;
   */
  public void setup(String[] args, Properties props) throws Fault {
    TestUtil.logTrace("setup");
    super.setup(args, props);
    logProcessor = new LogFileProcessor(props);

    if (logProcessor.fetchLog()) {
      currentSeqNum = logProcessor.getCurrentSequenceNumber();
      getEntityManager();
    } else {
      throw new Fault("Could not fetch log file");
    }
  }

  /**
   * @testName: createEMF
   * @assertion_ids: PERSISTENCE:JAVADOC:1479; PERSISTENCE:SPEC:981;
   *                 PERSISTENCE:SPEC:982
   * @test_Strategy: Specify a third Party Persistence Provider through
   *                 persistence.xml file
   */
  public void createEMF() throws Fault {
    String expected[] = { LOGMESSAGE_PREFIX
        + "Called createContainerEntityManagerFactory(PersistenceUnitInfo, Map)" };
    logProcessor.fetchLog();
    boolean pass = logProcessor.verifyLogContains(expected);
    if (!pass) {
      throw new Fault("createEMF failed");

    }
  }

  /**
   * @testName: getPersistenceProviderClassName
   * @assertion_ids: PERSISTENCE:JAVADOC:1496
   * @test_Strategy: Specify a third Party Persistence Provider through
   *                 persistence.xml file
   */
  public void getPersistenceProviderClassName() throws Fault {
    boolean pass1 = false;

    String expected1 = "com.sun.ts.tests.jpa.common.pluggability.altprovider.implementation.PersistenceProvider";
    getClassObjects();
    String actual = puInfo.getPersistenceProviderClassName();
    if (actual.equals(expected1)) {
      TestUtil.logTrace("Received expected result:" + actual);
      pass1 = true;
    } else {
      TestUtil.logErr("Expected: " + expected1 + ", actual:" + actual);
    }

    String expected2[] = { LOGMESSAGE_PREFIX
        + "Called PersistenceUnitInfoImpl.getPersistenceProviderClassName()" };
    logProcessor.fetchLog();
    // verify log does NOT contain the string
    boolean pass2 = logProcessor.verifyLogContains(expected2, currentSeqNum);

    if (!pass1 || pass2) {
      throw new Fault("getPersistenceProviderClassName failed");

    }
  }

  /**
   * @testName: getPersistenceUnitNameTest
   * @assertion_ids: PERSISTENCE:JAVADOC:1497; PERSISTENCE:JAVADOC:153;
   * @test_Strategy: Specify a third Party Persistence Provider through
   *                 persistence.xml file
   */
  public void getPersistenceUnitNameTest() throws Fault {
    boolean pass1 = false;

    List<String> expected1 = new ArrayList<String>();
    expected1.add("CTS-EM-NOTX");

    getClassObjects();
    String actual = puInfo.getPersistenceUnitName();
    if (expected1.contains(actual)) {
      TestUtil.logTrace("Received expected result:" + actual);
      pass1 = true;
    } else {
      TestUtil.logErr("Did not get one of the expected results");

      TestUtil.logErr("Expected:");
      for (String s : expected1) {
        TestUtil.logErr(s);
      }
      TestUtil.logErr("actual:" + actual);
    }

    String expected2[] = { LOGMESSAGE_PREFIX
        + "Called PersistenceUnitInfoImpl.getPersistenceUnitName()" };
    logProcessor.fetchLog();
    // verify log does NOT contain the string
    boolean pass2 = logProcessor.verifyLogContains(expected2, currentSeqNum);

    if (!pass1 || pass2) {
      throw new Fault("getPersistenceUnitNameTest failed");

    }
  }

  /**
   * @testName: getTransactionType
   * @assertion_ids: PERSISTENCE:JAVADOC:1502; PERSISTENCE:SPEC:1909;
   * @test_Strategy: Specify a third Party Persistence Provider through
   *                 persistence.xml file
   */
  public void getTransactionType() throws Fault {
    boolean pass1 = false;

    getClassObjects();

    PersistenceUnitTransactionType expected1 = PersistenceUnitTransactionType.RESOURCE_LOCAL;

    PersistenceUnitTransactionType actual = puInfo.getTransactionType();
    if (actual.equals(expected1)) {
      pass1 = true;
      TestUtil.logTrace("Received expected result:" + actual.toString());
    } else {
      TestUtil.logErr(
          "Expected: " + expected1.name() + ", actual:" + actual.name());
    }

    String expected2[] = { LOGMESSAGE_PREFIX
        + "Called PersistenceUnitInfoImpl.getTransactionType()" };
    logProcessor.fetchLog();
    // verify log does NOT contain the string
    boolean pass2 = logProcessor.verifyLogContains(expected2, currentSeqNum);

    if (!pass1 || pass2) {
      throw new Fault("getTransactionType failed");

    }
  }

  /**
   * @testName: getManagedClassNames
   * @assertion_ids: PERSISTENCE:JAVADOC:1492
   * @test_Strategy: Specify a third Party Persistence Provider through
   *                 persistence.xml file
   */
  public void getManagedClassNames() throws Fault {
    boolean pass1 = false;
    List<String> expected = new ArrayList<String>();
    expected.add(
        "com.sun.ts.tests.jpa.ee.pluggability.contracts.resource_local.Order");

    getClassObjects();
    List<String> actual = puInfo.getManagedClassNames();
    if (expected.containsAll(actual) && actual.containsAll(expected)
        && expected.size() == actual.size()) {

      pass1 = true;
      TestUtil.logTrace("Received expected result:" + actual.get(0));
    } else {
      TestUtil.logErr("Did not get expected ManagedClassNames");
      TestUtil.logErr("Expected:");
      for (String s : expected) {
        TestUtil.logErr(s);
      }
      TestUtil.logErr("Actual:");
      for (String s : actual) {
        TestUtil.logErr(s);
      }
    }

    String expected1[] = { LOGMESSAGE_PREFIX
        + "Called PersistenceUnitInfoImpl.getManagedClassNames()" };
    logProcessor.fetchLog();
    // verify log does NOT contain the string
    boolean pass2 = logProcessor.verifyLogContains(expected1, currentSeqNum);

    if (!pass1 || pass2) {
      throw new Fault("getManagedClassNames failed");

    }
  }

  /**
   * @testName: getJarFileUrls
   * @assertion_ids: PERSISTENCE:JAVADOC:1490
   * @test_Strategy: Specify a third Party Persistence Provider through
   *                 persistence.xml file
   */
  public void getJarFileUrls() throws Fault {
    boolean pass1 = false;

    String expected1 = "pluggability_contracts_resource_local.jar";
    getClassObjects();
    List<URL> actual = puInfo.getJarFileUrls();
    if (actual.size() > 0) {
      for (URL u : actual) {
        String sURL = u.toString();
        if (sURL.contains(expected1)) {
          TestUtil.logTrace("actual:" + sURL);
          pass1 = true;
        } else {
          TestUtil.logErr("getJarFileUrls() did not return a URL that contains:"
              + expected1);
          TestUtil.logTrace("actual:" + sURL);
        }
      }
    } else {
      TestUtil.logErr("getJarFileUrls() returned no results");
    }

    String expected2[] = {
        LOGMESSAGE_PREFIX + "Called PersistenceUnitInfoImpl.getJarFileUrls()" };
    logProcessor.fetchLog();
    // verify log does NOT contain the string
    boolean pass2 = logProcessor.verifyLogContains(expected2, currentSeqNum);

    if (!pass1 || pass2) {
      throw new Fault("getJarFileUrls failed");
    }
  }

  /**
   * @testName: getPersistenceUnitRootUrl
   * @assertion_ids: PERSISTENCE:JAVADOC:1498
   * @test_Strategy: Specify a third Party Persistence Provider through
   *                 persistence.xml file
   */
  public void getPersistenceUnitRootUrl() throws Fault {
    boolean pass1 = false;

    String expected1 = "pluggability_contracts_resource_local.jar";
    getClassObjects();
    URL actual = puInfo.getPersistenceUnitRootUrl();
    if (actual.toString().contains(expected1)) {
      TestUtil.logTrace("Received expected result:" + actual.toString());
      pass1 = true;
    } else {
      TestUtil.logErr("Expected the actual to contain:" + expected1);
      TestUtil.logErr("actual: " + actual.toString());
    }

    String expected2[] = { LOGMESSAGE_PREFIX
        + "Called PersistenceUnitInfoImpl.getPersistenceUnitRootUrl()" };
    logProcessor.fetchLog();
    // verify log does NOT contain the string
    boolean pass2 = logProcessor.verifyLogContains(expected2, currentSeqNum);

    if (!pass1 || pass2) {
      throw new Fault("getPersistenceUnitRootUrl failed");
    }
  }

  /**
   * @testName: getPersistenceXMLSchemaVersion
   * @assertion_ids: PERSISTENCE:JAVADOC:1499
   * @test_Strategy: Specify a third Party Persistence Provider through
   *                 persistence.xml file
   */
  public void getPersistenceXMLSchemaVersion() throws Fault {
    boolean pass1 = false;

    String expected1 = "2.1";
    getClassObjects();
    String actual = puInfo.getPersistenceXMLSchemaVersion();
    if (actual.toString().equals(expected1)) {
      TestUtil.logTrace("Received expected result:" + actual.toString());
      pass1 = true;
    } else {
      TestUtil.logErr("expected: " + expected1);
      TestUtil.logErr("actual: " + actual.toString());
    }

    String expected2[] = { LOGMESSAGE_PREFIX
        + "Called PersistenceUnitInfoImpl.getPersistenceXMLSchemaVersion()" };
    logProcessor.fetchLog();
    // verify log does NOT contain the string
    boolean pass2 = logProcessor.verifyLogContains(expected2, currentSeqNum);

    if (!pass1 || pass2) {
      throw new Fault("getPersistenceXMLSchemaVersion failed");
    }
  }

  /**
   * @testName: getProperties
   * @assertion_ids: PERSISTENCE:JAVADOC:1500
   * @test_Strategy: Specify a third Party Persistence Provider through
   *                 persistence.xml file
   */
  public void getProperties() throws Fault {
    boolean pass1 = true;

    Properties expected1 = new Properties();
    expected1.put("eclipselink.logging.level", "INFO");
    expected1.put("eclipselink.logging.logger", "DefaultLogger");
    getClassObjects();
    Properties actual = puInfo.getProperties();
    if (!actual.isEmpty()) {
      for (Map.Entry<Object, Object> e : expected1.entrySet()) {
        String eKey = (String) e.getKey();
        String eVal = (String) e.getValue();
        TestUtil.logTrace("key:" + eKey + ", value:" + eVal);

        if (actual.containsKey(eKey)) {
          String aVal = (String) actual.get(eKey);
          if (aVal.equals(eVal)) {
            TestUtil.logTrace("Found expected result:" + eKey + "," + eVal);
          } else {
            pass1 = false;
            TestUtil.logErr("Actual did not contain expected property:" + eKey
                + "," + eVal);
          }
        }

      }
    } else {
      TestUtil.logErr("getProperties() returned no properties");

    }
    if (!pass1) {
      TestUtil.logErr("Expected:");
      for (Map.Entry<Object, Object> e : expected1.entrySet()) {
        String key = (String) e.getKey();
        String value = (String) e.getValue();
        TestUtil.logErr("key:" + key + ", value:" + value);
      }
      TestUtil.logErr("Actual:");
      for (Map.Entry<Object, Object> e : actual.entrySet()) {
        String key = (String) e.getKey();
        String value = (String) e.getValue();
        TestUtil.logErr("key:" + key + ", value:" + value);
      }
    }

    String expected[] = {
        LOGMESSAGE_PREFIX + "Called PersistenceUnitInfoImpl.getProperties()" };
    logProcessor.fetchLog();
    // verify log does NOT contain the string
    boolean pass2 = logProcessor.verifyLogContains(expected, currentSeqNum);
    if (!pass1 || pass2) {
      throw new Fault("getProperties failed");
    }
  }

  /**
   * @testName: getSharedCacheMode
   * @assertion_ids: PERSISTENCE:JAVADOC:1501
   * @test_Strategy: Specify a third Party Persistence Provider through
   *                 persistence.xml file
   */
  public void getSharedCacheMode() throws Fault {
    boolean pass1 = false;

    SharedCacheMode expected1 = SharedCacheMode.NONE;
    getClassObjects();
    SharedCacheMode actual = puInfo.getSharedCacheMode();
    if (actual.equals(expected1)) {
      TestUtil.logTrace("Received expected result:" + actual.name());
      pass1 = true;
    } else {
      TestUtil.logErr("expected: " + expected1.name());
      TestUtil.logErr("actual: " + actual.name());
    }

    String expected2[] = { LOGMESSAGE_PREFIX
        + "Called PersistenceUnitInfoImpl.getSharedCacheMode()" };
    logProcessor.fetchLog();
    // verify log does NOT contain the string
    boolean pass2 = logProcessor.verifyLogContains(expected2, currentSeqNum);

    if (!pass1 || pass2) {
      throw new Fault("getSharedCacheMode failed");
    }
  }

  /**
   * @testName: getValidationMode
   * @assertion_ids: PERSISTENCE:JAVADOC:1503
   * @test_Strategy: Specify a third Party Persistence Provider through
   *                 persistence.xml file
   */
  public void getValidationMode() throws Fault {
    boolean pass1 = false;

    ValidationMode expected1 = ValidationMode.NONE;
    getClassObjects();
    ValidationMode actual = puInfo.getValidationMode();
    if (actual.equals(expected1)) {
      TestUtil.logTrace("Received expected result:" + actual.name());
      pass1 = true;
    } else {
      TestUtil.logErr("expected: " + expected1.name());
      TestUtil.logErr("actual: " + actual.name());
    }
    String expected2[] = { LOGMESSAGE_PREFIX
        + "Called PersistenceUnitInfoImpl.getValidationMode()" };
    logProcessor.fetchLog();
    // verify log does NOT contain the string
    boolean pass2 = logProcessor.verifyLogContains(expected2, currentSeqNum);

    if (!pass1 || pass2) {
      throw new Fault("getValidationMode failed");
    }
  }

  /**
   * @testName: getClassLoader
   * @assertion_ids: PERSISTENCE:JAVADOC:1489
   * @test_Strategy: Specify a third Party Persistence Provider through
   *                 persistence.xml file
   */
  public void getClassLoader() throws Fault {
    boolean pass1 = false;
    getClassObjects();
    java.lang.ClassLoader actual = puInfo.getClassLoader();
    if (actual != null) {
      pass1 = true;
      TestUtil.logTrace(
          "Received expected non null ClassLoader:" + actual.toString());
    } else {
      TestUtil.logErr("Received null ClassLoader");
    }

    String expected[] = {
        LOGMESSAGE_PREFIX + "Called PersistenceUnitInfoImpl.getClassLoader()" };
    logProcessor.fetchLog();
    // verify log does NOT contain the string
    boolean pass2 = logProcessor.verifyLogContains(expected, currentSeqNum);

    if (!pass1 || pass2) {
      throw new Fault("getClassLoader failed");
    }
  }

  /**
   * @testName: getNewTempClassLoader
   * @assertion_ids: PERSISTENCE:JAVADOC:1494
   * @test_Strategy: Specify a third Party Persistence Provider through
   *                 persistence.xml file
   */
  public void getNewTempClassLoader() throws Fault {
    boolean pass1 = false;
    getClassObjects();
    java.lang.ClassLoader actual = emfImpl.newTempClassloader;
    if (actual != null) {
      pass1 = true;
      TestUtil.logTrace(
          "Received expected non null ClassLoader:" + actual.toString());
    } else {
      TestUtil.logErr("Received null ClassLoader");
    }

    String expected[] = { LOGMESSAGE_PREFIX
        + "Called PersistenceUnitInfoImpl.getNewTempClassLoader()" };
    logProcessor.fetchLog();
    // verify log does NOT contain the string
    boolean pass2 = logProcessor.verifyLogContains(expected, currentSeqNum);

    if (!pass1 || pass2) {
      throw new Fault("getNewTempClassLoader failed");
    }
  }

  /**
   * @testName: getMappingFileNames
   * @assertion_ids: PERSISTENCE:JAVADOC:1493
   * @test_Strategy: Specify a third Party Persistence Provider through
   *                 persistence.xml file
   */
  public void getMappingFileNames() throws Fault {
    boolean pass1 = false;
    List<String> expected = new ArrayList<String>();
    expected.add("META-INF/myMappingFile1.xml");
    expected.add("META-INF/myMappingFile2.xml");

    getClassObjects();
    List<String> actual = puInfo.getMappingFileNames();
    if (expected.containsAll(actual) && actual.containsAll(expected)
        && expected.size() == actual.size()) {

      pass1 = true;
      TestUtil.logTrace("Received expected result:");
      for (String s : actual) {
        TestUtil.logTrace("result:" + s);
      }
    } else {
      TestUtil.logErr("Did not get expected ManagedClassNames");
      TestUtil.logErr("Expected:");
      for (String s : expected) {
        TestUtil.logErr(s);
      }
      TestUtil.logErr("Actual:");
      for (String s : actual) {
        TestUtil.logErr(s);
      }
    }

    String expected1[] = { LOGMESSAGE_PREFIX
        + "Called PersistenceUnitInfoImpl.getMappingFileNames()" };
    logProcessor.fetchLog();
    // verify log does NOT contain the string
    boolean pass2 = logProcessor.verifyLogContains(expected1, currentSeqNum);

    if (!pass1 || pass2) {
      throw new Fault("getMappingFileNames failed");
    }
  }

  /**
   * @testName: getNonJtaDataSource
   * @assertion_ids: PERSISTENCE:JAVADOC:1495
   * @test_Strategy: Specify a third Party Persistence Provider through
   *                 persistence.xml file
   */
  public void getNonJtaDataSource() throws Fault {
    boolean pass1 = false;
    String expected[] = { LOGMESSAGE_PREFIX
        + "Called PersistenceUnitInfoImpl.getNonJtaDataSource()" };
    getClassObjects();
    if (puInfo.getNonJtaDataSource() != null) {
      TestUtil.logTrace("getNonJtaDataSource() returned non-null result:"
          + puInfo.getNonJtaDataSource().toString());
      pass1 = true;
    } else {
      TestUtil.logErr("getNonJtaDataSource() returned null result");
    }
    logProcessor.fetchLog();
    boolean pass2 = logProcessor.verifyLogContains(expected, currentSeqNum);

    if (!pass1 || pass2) {
      throw new Fault("getNonJtaDataSource failed");
    }
  }

  /**
   * @testName: excludeUnlistedClasses
   * @assertion_ids: PERSISTENCE:JAVADOC:1488
   * @test_Strategy: Specify a third Party Persistence Provider through
   *                 persistence.xml file
   */
  public void excludeUnlistedClasses() throws Fault {
    boolean pass1 = false;

    String expected[] = { LOGMESSAGE_PREFIX
        + "Called PersistenceUnitInfoImpl.excludeUnlistedClasses()" };
    getClassObjects();
    if (puInfo.excludeUnlistedClasses() == false) {
      TestUtil.logTrace("excludeUnlistedClasses() returned expected result:"
          + puInfo.excludeUnlistedClasses());
      pass1 = true;
    } else {
      TestUtil.logErr("excludeUnlistedClasses() returned null result");
    }
    logProcessor.fetchLog();
    // verify log does NOT contain the string
    boolean pass2 = logProcessor.verifyLogContains(expected, currentSeqNum);

    if (!pass1 || pass2) {
      throw new Fault("excludeUnlistedClasses failed");
    }
  }

  /**
   * @testName: getProviderUtil
   * @assertion_ids: PERSISTENCE:JAVADOC:1481
   * @test_Strategy: Specify a third Party Persistence Provider through
   *                 persistence.xml file
   */
  public void getProviderUtil() throws Fault {

    String expected[] = { LOGMESSAGE_PREFIX + "Called getProviderUtil()" };

    javax.persistence.Persistence.getPersistenceUtil().isLoaded("Order");
    logProcessor.fetchLog();
    boolean pass = logProcessor.verifyLogContains(expected, currentSeqNum);

    if (!pass) {
      throw new Fault("getProviderUtil failed");
    }
  }

  /**
   * @testName: isLoaded
   * @assertion_ids: PERSISTENCE:JAVADOC:1506
   * @test_Strategy: Specify a third Party Persistence Provider through
   *                 persistence.xml file
   */
  public void isLoaded() throws Fault {

    String expected[] = { LOGMESSAGE_PREFIX + "Called isLoaded()" };

    javax.persistence.Persistence.getPersistenceUtil().isLoaded(Order.class);
    logProcessor.fetchLog();
    boolean pass = logProcessor.verifyLogContains(expected, currentSeqNum);

    if (!pass) {
      throw new Fault("isLoaded failed");
    }
  }

  /**
   * @testName: isLoadedWithoutReference
   * @assertion_ids: PERSISTENCE:JAVADOC:1507
   * @test_Strategy: Specify a third Party Persistence Provider through
   *                 persistence.xml file
   */
  public void isLoadedWithoutReference() throws Fault {

    String expected[] = {
        LOGMESSAGE_PREFIX + "Called isLoadedWithoutReference()" };

    javax.persistence.Persistence.getPersistenceUtil().isLoaded(Order.class,
        "attribute");
    logProcessor.fetchLog();
    boolean pass = logProcessor.verifyLogContains(expected, currentSeqNum);

    if (!pass) {
      throw new Fault("isLoadedWithoutReference failed");
    }

  }

  public void getClassObjects() {
    emImpl = getEntityManager(false).unwrap(EntityManagerImpl.class);
    emfImpl = (EntityManagerFactoryImpl) emImpl.getEntityManagerFactory();
    puInfo = emfImpl.puInfo;
  }

  public void cleanup() throws Fault {
    TestUtil.logTrace("cleanup");
    TestUtil.logTrace("calling super.cleanup");
    super.cleanup();

  }
}
