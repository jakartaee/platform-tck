/*
 * Copyright (c) 2007, 2023 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jpa.ee.pluggability.contracts.jta;

import java.lang.System.Logger;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sun.ts.tests.jpa.common.PMClientBase;
import com.sun.ts.tests.jpa.common.pluggability.altprovider.implementation.EntityManagerFactoryImpl;
import com.sun.ts.tests.jpa.common.pluggability.altprovider.implementation.EntityManagerImpl;
import com.sun.ts.tests.jpa.common.pluggability.util.LogFileProcessor;

import jakarta.persistence.SharedCacheMode;
import jakarta.persistence.ValidationMode;
import jakarta.persistence.spi.PersistenceUnitInfo;
import jakarta.persistence.spi.PersistenceUnitTransactionType;

public class Client extends PMClientBase {

	private static final Logger logger = (Logger) System.getLogger(Client.class.getName());

	private transient EntityManagerImpl emImpl;

	private transient EntityManagerFactoryImpl emfImpl;

	private PersistenceUnitInfo puInfo;

	private long currentSeqNum = 0L;

	private static LogFileProcessor logProcessor = null;

	private String LOGMESSAGE_PREFIX = "JPA_ALTERNATE_PROVIDER : ";

	public Client() {
	}

	/*
	 * @class.setup_props: log.file.location;
	 */
	@BeforeEach
	public void setup() throws Exception {
		logger.log(Logger.Level.TRACE, "setup");
		super.setup();
		logProcessor = new LogFileProcessor();

		if (logProcessor.fetchLog()) {
			currentSeqNum = logProcessor.getCurrentSequenceNumber();
			getEntityManager();
		} else {
			throw new Exception("Could not fetch log file");
		}
	}

	/**
	 * @testName: createEMF
	 * @assertion_ids: PERSISTENCE:JAVADOC:1479; PERSISTENCE:SPEC:981;
	 *                 PERSISTENCE:SPEC:982; PERSISTENCE:SPEC:1882;
	 *                 PERSISTENCE:SPEC:1883; PERSISTENCE:SPEC:1883.1;
	 * @test_Strategy: Specify a third Party Persistence Provider through
	 *                 persistence.xml file
	 */
	@Test
	public void createEMF() throws Exception {
		String expected[] = {
				LOGMESSAGE_PREFIX + "Called createContainerEntityManagerFactory(PersistenceUnitInfo, Map)" };
		logProcessor.fetchLog();
		boolean pass = logProcessor.verifyLogContains(expected);
		if (!pass) {
			throw new Exception("createEMF failed");

		}
	}

	/**
	 * @testName: getPersistenceProviderClassName
	 * @assertion_ids: PERSISTENCE:JAVADOC:1496
	 * @test_Strategy: Specify a third Party Persistence Provider through
	 *                 persistence.xml file
	 */
	@Test
	public void getPersistenceProviderClassName() throws Exception {
		boolean pass1 = false;

		String expected1 = "com.sun.ts.tests.jpa.common.pluggability.altprovider.implementation.PersistenceProvider";
		getClassObjects();
		String actual = puInfo.getPersistenceProviderClassName();
		if (actual.equals(expected1)) {
			logger.log(Logger.Level.TRACE, "Received expected result:" + actual);
			pass1 = true;
		} else {
			logger.log(Logger.Level.ERROR, "Expected: " + expected1 + ", actual:" + actual);
		}

		String expected2[] = { LOGMESSAGE_PREFIX + "Called PersistenceUnitInfoImpl.getPersistenceProviderClassName()" };
		logProcessor.fetchLog();
		// verify log does NOT contain the string
		boolean pass2 = logProcessor.verifyLogContains(expected2, currentSeqNum);

		if (!pass1 || pass2) {
			throw new Exception("getPersistenceProviderClassName failed");

		}
	}

	/**
	 * @testName: getPersistenceUnitNameTest
	 * @assertion_ids: PERSISTENCE:JAVADOC:1497; PERSISTENCE:JAVADOC:153;
	 * @test_Strategy: Specify a third Party Persistence Provider through
	 *                 persistence.xml file
	 */
	@Test
	public void getPersistenceUnitNameTest() throws Exception {
		boolean pass1 = false;

		List<String> expected1 = new ArrayList<String>();
		expected1.add("CTS-EM");
		expected1.add("CTS-EM2");
		expected1.add("CTS-EM-NOTX");

		getClassObjects();
		String actual = puInfo.getPersistenceUnitName();
		if (expected1.contains(actual)) {
			logger.log(Logger.Level.TRACE, "Received expected result:" + actual);
			pass1 = true;
		} else {
			logger.log(Logger.Level.ERROR, "Did not get one of the expected results");

			logger.log(Logger.Level.ERROR, "Expected:");
			for (String s : expected1) {
				logger.log(Logger.Level.ERROR, s);
			}
			logger.log(Logger.Level.ERROR, "actual:" + actual);
		}

		String expected2[] = { LOGMESSAGE_PREFIX + "Called PersistenceUnitInfoImpl.getPersistenceUnitName()" };
		logProcessor.fetchLog();
		// verify log does NOT contain the string
		boolean pass2 = logProcessor.verifyLogContains(expected2, currentSeqNum);

		if (!pass1 || pass2) {
			throw new Exception("getPersistenceUnitNameTest failed");

		}
	}

	/**
	 * @testName: getTransactionType
	 * @assertion_ids: PERSISTENCE:JAVADOC:1502; PERSISTENCE:SPEC:1909;
	 * @test_Strategy: Specify a third Party Persistence Provider through
	 *                 persistence.xml file
	 */
	@Test
	public void getTransactionType() throws Exception {
		boolean pass1 = false;

		getClassObjects();

		PersistenceUnitTransactionType expected1 = PersistenceUnitTransactionType.JTA;

		PersistenceUnitTransactionType actual = puInfo.getTransactionType();
		if (actual.equals(expected1)) {
			pass1 = true;
			logger.log(Logger.Level.TRACE, "Received expected result:" + actual);
		} else {
			logger.log(Logger.Level.ERROR, "Expected: " + expected1.name() + ", actual:" + actual.name());
		}

		String expected2[] = { LOGMESSAGE_PREFIX + "Called PersistenceUnitInfoImpl.getTransactionType()" };
		logProcessor.fetchLog();
		// verify log does NOT contain the string
		boolean pass2 = logProcessor.verifyLogContains(expected2, currentSeqNum);

		if (!pass1 || pass2) {
			throw new Exception("getTransactionType failed");

		}
	}

	/**
	 * @testName: getManagedClassNames
	 * @assertion_ids: PERSISTENCE:JAVADOC:1492
	 * @test_Strategy: Specify a third Party Persistence Provider through
	 *                 persistence.xml file
	 */
	@Test
	public void getManagedClassNames() throws Exception {
		boolean pass1 = false;
		List<String> expected = new ArrayList<String>();
		expected.add("com.sun.ts.tests.jpa.ee.pluggability.contracts.jta.Order");

		getClassObjects();
		List<String> actual = puInfo.getManagedClassNames();
		if (expected.containsAll(actual) && actual.containsAll(expected) && expected.size() == actual.size()) {

			pass1 = true;
			logger.log(Logger.Level.TRACE, "Received expected result:" + actual.get(0));
		} else {
			logger.log(Logger.Level.ERROR, "Did not get expected ManagedClassNames");
			logger.log(Logger.Level.ERROR, "Expected:");
			for (String s : expected) {
				logger.log(Logger.Level.ERROR, s);
			}
			logger.log(Logger.Level.ERROR, "Actual:");
			for (String s : actual) {
				logger.log(Logger.Level.ERROR, s);
			}
		}

		String expected1[] = { LOGMESSAGE_PREFIX + "Called PersistenceUnitInfoImpl.getManagedClassNames()" };
		logProcessor.fetchLog();
		// verify log does NOT contain the string
		boolean pass2 = logProcessor.verifyLogContains(expected1, currentSeqNum);

		if (!pass1 || pass2) {
			throw new Exception("getManagedClassNames failed");

		}
	}

	/**
	 * @testName: getJarFileUrls
	 * @assertion_ids: PERSISTENCE:JAVADOC:1490
	 * @test_Strategy: Specify a third Party Persistence Provider through
	 *                 persistence.xml file
	 */
	@Test
	public void getJarFileUrls() throws Exception {
		boolean pass1 = false;

		String expected1 = "pluggability_contracts_jta.jar";
		getClassObjects();
		List<URL> actual = puInfo.getJarFileUrls();
		if (actual.size() > 0) {
			for (URL u : actual) {
				String sURL = u.toString();
				if (sURL.contains(expected1)) {
					logger.log(Logger.Level.TRACE, "actual:" + sURL);
					pass1 = true;
				} else {
					logger.log(Logger.Level.ERROR, "getJarFileUrls() did not return a URL that contains:" + expected1);
					logger.log(Logger.Level.TRACE, "actual:" + sURL);
				}
			}
		} else {
			logger.log(Logger.Level.ERROR, "getJarFileUrls() returned no results");
		}

		String expected2[] = { LOGMESSAGE_PREFIX + "Called PersistenceUnitInfoImpl.getJarFileUrls()" };
		logProcessor.fetchLog();
		// verify log does NOT contain the string
		boolean pass2 = logProcessor.verifyLogContains(expected2, currentSeqNum);

		if (!pass1 || pass2) {
			throw new Exception("getJarFileUrls failed");
		}
	}

	/**
	 * @testName: getPersistenceUnitRootUrl
	 * @assertion_ids: PERSISTENCE:JAVADOC:1498
	 * @test_Strategy: Specify a third Party Persistence Provider through
	 *                 persistence.xml file
	 */
	@Test
	public void getPersistenceUnitRootUrl() throws Exception {
		boolean pass1 = false;

		String expected1 = "pluggability_contracts_jta.jar";
		getClassObjects();
		URL actual = puInfo.getPersistenceUnitRootUrl();
		if (actual.toString().contains(expected1)) {
			logger.log(Logger.Level.TRACE, "Received expected result:" + actual.toString());
			pass1 = true;
		} else {
			logger.log(Logger.Level.ERROR, "Expected the actual to contain:" + expected1);
			logger.log(Logger.Level.ERROR, "actual: " + actual.toString());
		}

		String expected2[] = { LOGMESSAGE_PREFIX + "Called PersistenceUnitInfoImpl.getPersistenceUnitRootUrl()" };
		logProcessor.fetchLog();
		// verify log does NOT contain the string
		boolean pass2 = logProcessor.verifyLogContains(expected2, currentSeqNum);

		if (!pass1 || pass2) {
			throw new Exception("getPersistenceUnitRootUrl failed");
		}
	}

	/**
	 * @testName: getPersistenceXMLSchemaVersion
	 * @assertion_ids: PERSISTENCE:JAVADOC:1499
	 * @test_Strategy: Specify a third Party Persistence Provider through
	 *                 persistence.xml file
	 */
	@Test
	public void getPersistenceXMLSchemaVersion() throws Exception {
		boolean pass1 = false;

		String expected1 = "3.0";
		getClassObjects();
		String actual = puInfo.getPersistenceXMLSchemaVersion();
		if (actual.toString().equals(expected1)) {
			logger.log(Logger.Level.TRACE, "Received expected result:" + actual.toString());
			pass1 = true;
		} else {
			logger.log(Logger.Level.ERROR, "expected: " + expected1);
			logger.log(Logger.Level.ERROR, "actual: " + actual.toString());
		}

		String expected2[] = { LOGMESSAGE_PREFIX + "Called PersistenceUnitInfoImpl.getPersistenceXMLSchemaVersion()" };
		logProcessor.fetchLog();
		// verify log does NOT contain the string
		boolean pass2 = logProcessor.verifyLogContains(expected2, currentSeqNum);

		if (!pass1 || pass2) {
			throw new Exception("getPersistenceXMLSchemaVersion failed");
		}
	}

	/**
	 * @testName: getProperties
	 * @assertion_ids: PERSISTENCE:JAVADOC:1500
	 * @test_Strategy: Specify a third Party Persistence Provider through
	 *                 persistence.xml file
	 */
	@Test
	public void getProperties() throws Exception {
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
				logger.log(Logger.Level.TRACE, "key:" + eKey + ", value:" + eVal);

				if (actual.containsKey(eKey)) {
					String aVal = (String) actual.get(eKey);
					if (aVal.equals(eVal)) {
						logger.log(Logger.Level.TRACE, "Found expected result:" + eKey + "," + eVal);
					} else {
						pass1 = false;
						logger.log(Logger.Level.ERROR, "Actual did not contain expected property:" + eKey + "," + eVal);
					}
				}

			}
		} else {
			logger.log(Logger.Level.ERROR, "getProperties() returned no properties");

		}
		if (!pass1) {
			logger.log(Logger.Level.ERROR, "Expected:");
			for (Map.Entry<Object, Object> e : expected1.entrySet()) {
				String key = (String) e.getKey();
				String value = (String) e.getValue();
				logger.log(Logger.Level.ERROR, "key:" + key + ", value:" + value);
			}
			logger.log(Logger.Level.ERROR, "Actual:");
			for (Map.Entry<Object, Object> e : actual.entrySet()) {
				String key = (String) e.getKey();
				String value = (String) e.getValue();
				logger.log(Logger.Level.ERROR, "key:" + key + ", value:" + value);
			}
		}

		String expected[] = { LOGMESSAGE_PREFIX + "Called PersistenceUnitInfoImpl.getProperties()" };
		logProcessor.fetchLog();
		// verify log does NOT contain the string
		boolean pass2 = logProcessor.verifyLogContains(expected, currentSeqNum);

		if (!pass1 || pass2) {
			throw new Exception("getProperties failed");
		}
	}

	/**
	 * @testName: getSharedCacheMode
	 * @assertion_ids: PERSISTENCE:JAVADOC:1501; PERSISTENCE:SPEC:1910;
	 * @test_Strategy: Specify a third Party Persistence Provider through
	 *                 persistence.xml file
	 */
	@Test
	public void getSharedCacheMode() throws Exception {
		boolean pass1 = false;

		SharedCacheMode expected1 = SharedCacheMode.NONE;
		getClassObjects();
		SharedCacheMode actual = puInfo.getSharedCacheMode();
		if (actual.equals(expected1)) {
			logger.log(Logger.Level.TRACE, "Received expected result:" + actual.name());
			pass1 = true;
		} else {
			logger.log(Logger.Level.ERROR, "expected: " + expected1.name());
			logger.log(Logger.Level.ERROR, "actual: " + actual.name());
		}

		String expected2[] = { LOGMESSAGE_PREFIX + "Called PersistenceUnitInfoImpl.getSharedCacheMode()" };
		logProcessor.fetchLog();
		// verify log does NOT contain the string
		boolean pass2 = logProcessor.verifyLogContains(expected2, currentSeqNum);

		if (!pass1 || pass2) {
			throw new Exception("getSharedCacheMode failed");
		}
	}

	/**
	 * @testName: getValidationMode
	 * @assertion_ids: PERSISTENCE:JAVADOC:1503
	 * @test_Strategy: Specify a third Party Persistence Provider through
	 *                 persistence.xml file
	 */
	@Test
	public void getValidationMode() throws Exception {
		boolean pass1 = false;

		ValidationMode expected1 = ValidationMode.NONE;
		getClassObjects();
		ValidationMode actual = puInfo.getValidationMode();
		if (actual.equals(expected1)) {
			logger.log(Logger.Level.TRACE, "Received expected result:" + actual.name());
			pass1 = true;
		} else {
			logger.log(Logger.Level.ERROR, "expected: " + expected1.name());
			logger.log(Logger.Level.ERROR, "actual: " + actual.name());
		}
		String expected2[] = { LOGMESSAGE_PREFIX + "Called PersistenceUnitInfoImpl.getValidationMode()" };
		logProcessor.fetchLog();
		// verify log does NOT contain the string
		boolean pass2 = logProcessor.verifyLogContains(expected2, currentSeqNum);

		if (!pass1 || pass2) {
			throw new Exception("getValidationMode failed");
		}
	}

	/**
	 * @testName: getClassLoader
	 * @assertion_ids: PERSISTENCE:JAVADOC:1489
	 * @test_Strategy: Specify a third Party Persistence Provider through
	 *                 persistence.xml file
	 */
	@Test
	public void getClassLoader() throws Exception {
		boolean pass1 = false;
		getClassObjects();
		java.lang.ClassLoader actual = puInfo.getClassLoader();
		if (actual != null) {
			pass1 = true;
			logger.log(Logger.Level.TRACE, "Received expected non null ClassLoader:" + actual.toString());
		} else {
			logger.log(Logger.Level.ERROR, "Received null ClassLoader");
		}

		String expected[] = { LOGMESSAGE_PREFIX + "Called PersistenceUnitInfoImpl.getClassLoader()" };
		logProcessor.fetchLog();
		// verify log does NOT contain the string
		boolean pass2 = logProcessor.verifyLogContains(expected, currentSeqNum);

		if (!pass1 || pass2) {
			throw new Exception("getClassLoader failed");
		}
	}

	/**
	 * @testName: getNewTempClassLoader
	 * @assertion_ids: PERSISTENCE:JAVADOC:1494
	 * @test_Strategy: Specify a third Party Persistence Provider through
	 *                 persistence.xml file
	 */
	@Test
	public void getNewTempClassLoader() throws Exception {
		boolean pass1 = false;
		getClassObjects();
		java.lang.ClassLoader actual = emfImpl.newTempClassloader;
		if (actual != null) {
			pass1 = true;
			logger.log(Logger.Level.TRACE, "Received expected non null ClassLoader:" + actual.toString());
		} else {
			logger.log(Logger.Level.ERROR, "Received null ClassLoader");
		}

		String expected[] = { LOGMESSAGE_PREFIX + "Called PersistenceUnitInfoImpl.getNewTempClassLoader()" };
		logProcessor.fetchLog();
		// verify log does NOT contain the string
		boolean pass2 = logProcessor.verifyLogContains(expected, currentSeqNum);

		if (!pass1 || pass2) {
			throw new Exception("getNewTempClassLoader failed");
		}
	}

	/**
	 * @testName: getMappingFileNames
	 * @assertion_ids: PERSISTENCE:JAVADOC:1493
	 * @test_Strategy: Specify a third Party Persistence Provider through
	 *                 persistence.xml file
	 */
	@Test
	public void getMappingFileNames() throws Exception {
		boolean pass1 = false;
		List<String> expected = new ArrayList<String>();
		expected.add("META-INF/myMappingFile1.xml");
		expected.add("META-INF/myMappingFile2.xml");

		getClassObjects();
		List<String> actual = puInfo.getMappingFileNames();
		if (expected.containsAll(actual) && actual.containsAll(expected) && expected.size() == actual.size()) {

			pass1 = true;
			logger.log(Logger.Level.TRACE, "Received expected result:");
			for (String s : actual) {
				logger.log(Logger.Level.TRACE, "result:" + s);
			}
		} else {
			logger.log(Logger.Level.ERROR, "Did not get expected ManagedClassNames");
			logger.log(Logger.Level.ERROR, "Expected:");
			for (String s : expected) {
				logger.log(Logger.Level.ERROR, s);
			}
			logger.log(Logger.Level.ERROR, "Actual:");
			for (String s : actual) {
				logger.log(Logger.Level.ERROR, s);
			}
		}

		String expected1[] = { LOGMESSAGE_PREFIX + "Called PersistenceUnitInfoImpl.getMappingFileNames()" };
		logProcessor.fetchLog();
		// verify log does NOT contain the string
		boolean pass2 = logProcessor.verifyLogContains(expected1, currentSeqNum);

		if (!pass1 || pass2) {
			throw new Exception("getMappingFileNames failed");
		}
	}

	/**
	 * @testName: getJtaDataSource
	 * @assertion_ids: PERSISTENCE:JAVADOC:1491
	 * @test_Strategy: Specify a third Party Persistence Provider through
	 *                 persistence.xml file
	 */
	@Test
	public void getJtaDataSource() throws Exception {

		boolean pass1 = false;
		String expected[] = { LOGMESSAGE_PREFIX + "Called PersistenceUnitInfoImpl.getJtaDataSource()" };
		getClassObjects();
		if (puInfo.getJtaDataSource() != null) {
			logger.log(Logger.Level.TRACE,
					"getJtaDataSource() returned non-null result:" + puInfo.getJtaDataSource().toString());
			pass1 = true;
		} else {
			logger.log(Logger.Level.TRACE, "getJtaDataSource() returned null result");
		}
		logProcessor.fetchLog();
		// verify log does NOT contain the string
		boolean pass2 = logProcessor.verifyLogContains(expected, currentSeqNum);

		if (!pass1 || pass2) {
			throw new Exception("getJtaDataSource failed");
		}
	}

	/**
	 * @testName: excludeUnlistedClasses
	 * @assertion_ids: PERSISTENCE:JAVADOC:1488
	 * @test_Strategy: Specify a third Party Persistence Provider through
	 *                 persistence.xml file
	 */
	@Test
	public void excludeUnlistedClasses() throws Exception {
		boolean pass1 = false;

		String expected[] = { LOGMESSAGE_PREFIX + "Called PersistenceUnitInfoImpl.excludeUnlistedClasses()" };
		getClassObjects();
		if (puInfo.excludeUnlistedClasses() == false) {
			logger.log(Logger.Level.TRACE,
					"excludeUnlistedClasses() returned expected result:" + puInfo.excludeUnlistedClasses());
			pass1 = true;
		} else {
			logger.log(Logger.Level.TRACE, "excludeUnlistedClasses() returned null result");
		}
		logProcessor.fetchLog();
		// verify log does NOT contain the string
		boolean pass2 = logProcessor.verifyLogContains(expected, currentSeqNum);

		if (!pass1 || pass2) {
			throw new Exception("excludeUnlistedClasses failed");
		}
	}

	/**
	 * @testName: getProviderUtil
	 * @assertion_ids: PERSISTENCE:JAVADOC:1481
	 * @test_Strategy: Specify a third Party Persistence Provider through
	 *                 persistence.xml file
	 */
	@Test
	public void getProviderUtil() throws Exception {

		String expected[] = { LOGMESSAGE_PREFIX + "Called getProviderUtil()" };

		jakarta.persistence.Persistence.getPersistenceUtil().isLoaded("Order");
		logProcessor.fetchLog();
		boolean pass = logProcessor.verifyLogContains(expected, currentSeqNum);
		if (!pass) {
			throw new Exception("getProviderUtil failed");
		}
	}

	/**
	 * @testName: isLoaded
	 * @assertion_ids: PERSISTENCE:JAVADOC:1506
	 * @test_Strategy: Specify a third Party Persistence Provider through
	 *                 persistence.xml file
	 */
	@Test
	public void isLoaded() throws Exception {

		String expected[] = { LOGMESSAGE_PREFIX + "Called isLoaded()" };

		jakarta.persistence.Persistence.getPersistenceUtil().isLoaded("Order");
		logProcessor.fetchLog();
		boolean pass = logProcessor.verifyLogContains(expected, currentSeqNum);

		if (!pass) {
			throw new Exception("isLoaded failed");
		}
	}

	public void getClassObjects() {
		emImpl = getEntityManager(false)
				.unwrap(com.sun.ts.tests.jpa.common.pluggability.altprovider.implementation.EntityManagerImpl.class);
		emfImpl = (com.sun.ts.tests.jpa.common.pluggability.altprovider.implementation.EntityManagerFactoryImpl) emImpl
				.getEntityManagerFactory();
		puInfo = emfImpl.puInfo;
	}

	@AfterEach
	public void cleanup() throws Exception {
		logger.log(Logger.Level.TRACE, "cleanup");
		logger.log(Logger.Level.TRACE, "calling super.cleanup");
		super.cleanup();

	}
}
