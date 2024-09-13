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

/*
 * $Id: Client.java 63518 2011-09-16 11:36:26Z sdimilla $
 */

package ee.jakarta.tck.persistence.core.enums;


import java.util.Arrays;
import java.util.Collection;
import java.util.Properties;

import com.sun.ts.lib.harness.Status;





import ee.jakarta.tck.persistence.common.PMClientBase;
import jakarta.persistence.AccessType;
import jakarta.persistence.CacheRetrieveMode;
import jakarta.persistence.CacheStoreMode;
import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.EnumType;
import jakarta.persistence.FetchType;
import jakarta.persistence.FlushModeType;
import jakarta.persistence.GenerationType;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.LockModeType;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.PersistenceContextType;
import jakarta.persistence.PessimisticLockScope;
import jakarta.persistence.Query;
import jakarta.persistence.SharedCacheMode;
import jakarta.persistence.SynchronizationType;
import jakarta.persistence.TemporalType;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.ValidationMode;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.metamodel.Attribute.PersistentAttributeType;
import jakarta.persistence.metamodel.Bindable.BindableType;
import jakarta.persistence.metamodel.PluralAttribute.CollectionType;
import jakarta.persistence.metamodel.Type.PersistenceType;
import jakarta.persistence.spi.LoadState;
import jakarta.persistence.spi.PersistenceUnitTransactionType;

public class Client extends PMClientBase {



	public Client() {
	}
	public static void main(String[] args) {
		Client theTests = new Client();
		Status s = theTests.run(args, System.out, System.err);
		s.exit();
	}



	
	public void setup(String[] args, Properties p) throws Exception {
		logTrace( "setup");
		try {

			super.setup(args,p);
			

		} catch (Exception e) {
			logErr( "Exception: ", e);
			throw new Exception("Setup failed:", e);
		}
	}

	/*
	 * @testName: accessTypeValueOfTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:301
	 * 
	 * @test_Strategy:
	 *
	 */
		public void accessTypeValueOfTest() throws Exception {
		boolean pass = true;

		try {

			AccessType at = AccessType.valueOf(AccessType.FIELD.name());
			if (!at.equals(AccessType.FIELD)) {
				logErr( "expected:" + AccessType.FIELD.name() + ", actual:" + at.name());
				pass = false;
			}
			at = AccessType.valueOf(AccessType.PROPERTY.name());
			if (!at.equals(AccessType.PROPERTY)) {
				logErr( "expected:" + AccessType.PROPERTY.name() + ", actual:" + at.name());
				pass = false;
			}
			try {
				AccessType.valueOf("DOESNOTEXIST");
				logErr( "IllegalArgumentException was not thrown");
				pass = false;
			} catch (IllegalArgumentException iae) {
				logTrace( "Received expected IllegalArgumentException");
			}
			try {
				AccessType.valueOf(null);
				logErr( "NullPointerException was not thrown");
				pass = false;
			} catch (NullPointerException iae) {
				logTrace( "Received expected NullPointerException");
			}
		} catch (Exception e) {
			logErr( "Unexpected exception occurred", e);
			pass = false;
		}

		if (!pass) {
			throw new Exception("accessTypeValueOfTest failed");
		}
	}

	/*
	 * @testName: accessTypeValuesTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:302
	 * 
	 * @test_Strategy:
	 *
	 */
		public void accessTypeValuesTest() throws Exception {
		boolean pass = true;

		logTrace( "Begin accessTypeValuesTest");
		try {

			Collection<AccessType> at = Arrays.asList(AccessType.values());
			if (at.size() != 2) {
				logErr( "Number of AccessType expected:2, actual:" + at.size());
				pass = false;
			}

			if (at.contains(AccessType.FIELD)) {
				logTrace( "received:" + AccessType.FIELD.name());
			} else {
				logErr( "Expected value:" + AccessType.FIELD.name());
				pass = false;
			}
			if (at.contains(AccessType.PROPERTY)) {
				logTrace( "received:" + AccessType.PROPERTY.name());
			} else {
				logErr( "Expected value:" + AccessType.PROPERTY.name());
				pass = false;
			}

			for (AccessType a : at) {
				try {
					AccessType.valueOf(a.name());
				} catch (Exception e) {
					logErr( "Received exception for valueOf", e);
					pass = false;
				}
			}
		} catch (Exception e) {
			logErr( "Unexpected exception occurred", e);
			pass = false;
		}

		if (!pass) {
			throw new Exception("accessTypeValuesTest failed");
		}
	}

	/*
	 * @testName: cacheRetrieveModeValueOfTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:309
	 * 
	 * @test_Strategy:
	 *
	 */
		public void cacheRetrieveModeValueOfTest() throws Exception {
		boolean pass = true;

		logTrace( "Begin cacheRetrieveModeValueOfTest");
		try {

			CacheRetrieveMode crm = CacheRetrieveMode.valueOf(CacheRetrieveMode.USE.name());
			if (!crm.equals(CacheRetrieveMode.USE)) {
				logErr( "expected:" + CacheRetrieveMode.USE.name() + ", actual:" + crm.name());
				pass = false;
			}
			crm = CacheRetrieveMode.valueOf(CacheRetrieveMode.BYPASS.name());
			if (!crm.equals(CacheRetrieveMode.BYPASS)) {
				logErr(
						"expected:" + CacheRetrieveMode.BYPASS.name() + ", actual:" + crm.name());
				pass = false;
			}
			try {
				CacheRetrieveMode.valueOf("DOESNOTEXIST");
				logErr( "IllegalArgumentException was not thrown");
				pass = false;
			} catch (IllegalArgumentException iae) {
				logTrace( "Received expected IllegalArgumentException");
			}
			try {
				CacheRetrieveMode.valueOf(null);
				logErr( "NullPointerException was not thrown");
				pass = false;
			} catch (NullPointerException iae) {
				logTrace( "Received expected NullPointerException");
			}
		} catch (Exception e) {
			logErr( "Unexpected exception occurred", e);
			pass = false;
		}

		if (!pass) {
			throw new Exception("cacheRetrieveModeValueOfTest failed");
		}
	}

	/*
	 * @testName: cacheRetrieveModeValuesTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:310
	 * 
	 * @test_Strategy:
	 *
	 */
		public void cacheRetrieveModeValuesTest() throws Exception {
		boolean pass = true;

		logTrace( "Begin cacheRetrieveModeValuesTest");
		try {

			Collection<CacheRetrieveMode> crm = Arrays.asList(CacheRetrieveMode.values());
			if (crm.size() != 2) {
				logErr( "Number of CacheRetrieveMode expected:2, actual:" + crm.size());
				pass = false;
			}

			if (crm.contains(CacheRetrieveMode.USE)) {
				logTrace( "received:" + CacheRetrieveMode.USE);
			} else {
				logErr( "Expected value:" + CacheRetrieveMode.USE);
				pass = false;
			}
			if (crm.contains(CacheRetrieveMode.BYPASS)) {
				logTrace( "received:" + CacheRetrieveMode.BYPASS);
			} else {
				logErr( "Expected value:" + CacheRetrieveMode.BYPASS);
				pass = false;
			}

			for (CacheRetrieveMode c : crm) {
				try {
					CacheRetrieveMode.valueOf(c.name());
				} catch (Exception e) {
					logErr( "Received exception for valueOf", e);
					pass = false;
				}
			}
		} catch (Exception e) {
			logErr( "Unexpected exception occurred", e);
			pass = false;
		}

		if (!pass) {
			throw new Exception("cacheRetrieveModeValuesTest failed");
		}
	}

	/*
	 * @testName: cacheStoreModeValueOfTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:311
	 * 
	 * @test_Strategy:
	 *
	 */
		public void cacheStoreModeValueOfTest() throws Exception {
		boolean pass = true;

		logTrace( "Begin cacheStoreModeValueOfTest");
		try {

			CacheStoreMode csm = CacheStoreMode.valueOf(CacheStoreMode.USE.name());
			if (!csm.equals(CacheStoreMode.USE)) {
				logErr( "expected:" + CacheStoreMode.USE.name() + ", actual:" + csm.name());
				pass = false;
			}
			csm = CacheStoreMode.valueOf(CacheStoreMode.BYPASS.name());
			if (!csm.equals(CacheStoreMode.BYPASS)) {
				logErr( "expected:" + CacheStoreMode.BYPASS.name() + ", actual:" + csm.name());
				pass = false;
			}
			csm = CacheStoreMode.valueOf(CacheStoreMode.REFRESH.name());
			if (!csm.equals(CacheStoreMode.REFRESH)) {
				logErr( "expected:" + CacheStoreMode.REFRESH.name() + ", actual:" + csm.name());
				pass = false;
			}
			try {
				CacheStoreMode.valueOf("DOESNOTEXIST");
				logErr( "IllegalArgumentException was not thrown");
				pass = false;
			} catch (IllegalArgumentException iae) {
				logTrace( "Received expected IllegalArgumentException");
			}
			try {
				CacheStoreMode.valueOf(null);
				logErr( "NullPointerException was not thrown");
				pass = false;
			} catch (NullPointerException iae) {
				logTrace( "Received expected NullPointerException");
			}
		} catch (Exception e) {
			logErr( "Unexpected exception occurred", e);
			pass = false;
		}

		if (!pass) {
			throw new Exception("cacheStoreModeValueOfTest failed");
		}
	}

	/*
	 * @testName: cacheStoreModeValuesTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:312
	 * 
	 * @test_Strategy:
	 *
	 */
		public void cacheStoreModeValuesTest() throws Exception {
		boolean pass = true;

		logTrace( "Begin cacheStoreModeValuesTest");
		try {

			Collection<CacheStoreMode> csm = Arrays.asList(CacheStoreMode.values());
			if (csm.size() != 3) {
				logErr( "Number of CacheStoreMode expected:3, actual:" + csm.size());
				pass = false;
			}

			if (csm.contains(CacheStoreMode.USE)) {
				logTrace( "received:" + CacheStoreMode.USE);
			} else {
				logErr( "Expected value:" + CacheStoreMode.USE);
				pass = false;
			}
			if (csm.contains(CacheStoreMode.BYPASS)) {
				logTrace( "received:" + CacheStoreMode.BYPASS);
			} else {
				logErr( "Expected value:" + CacheStoreMode.BYPASS);
				pass = false;
			}
			if (csm.contains(CacheStoreMode.REFRESH)) {
				logTrace( "received:" + CacheStoreMode.REFRESH);
			} else {
				logErr( "Expected value:" + CacheStoreMode.REFRESH);
				pass = false;
			}

			for (CacheStoreMode c : csm) {
				try {
					CacheStoreMode.valueOf(c.name());
				} catch (Exception e) {
					logErr( "Received exception for valueOf", e);
					pass = false;
				}
			}
		} catch (Exception e) {
			logErr( "Unexpected exception occurred", e);
			pass = false;
		}

		if (!pass) {
			throw new Exception("cacheStoreModeValuesTest failed");
		}
	}

	/*
	 * @testName: cascadeTypeValueOfTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:9
	 * 
	 * @test_Strategy:
	 *
	 */
		public void cascadeTypeValueOfTest() throws Exception {
		boolean pass = true;

		logTrace( "Begin cascadeTypeValueOfTest");
		try {

			CascadeType ct = CascadeType.valueOf(CascadeType.ALL.name());
			if (!ct.equals(CascadeType.ALL)) {
				logErr( "expected:" + CascadeType.ALL.name() + ", actual:" + ct.name());
				pass = false;
			}
			ct = CascadeType.valueOf(CascadeType.DETACH.name());
			if (!ct.equals(CascadeType.DETACH)) {
				logErr( "expected:" + CascadeType.DETACH.name() + ", actual:" + ct.name());
				pass = false;
			}
			ct = CascadeType.valueOf(CascadeType.MERGE.name());
			if (!ct.equals(CascadeType.MERGE)) {
				logErr( "expected:" + CascadeType.MERGE.name() + ", actual:" + ct.name());
				pass = false;
			}
			ct = CascadeType.valueOf(CascadeType.PERSIST.name());
			if (!ct.equals(CascadeType.PERSIST)) {
				logErr( "expected:" + CascadeType.PERSIST.name() + ", actual:" + ct.name());
				pass = false;
			}
			ct = CascadeType.valueOf(CascadeType.REFRESH.name());
			if (!ct.equals(CascadeType.REFRESH)) {
				logErr( "expected:" + CascadeType.REFRESH.name() + ", actual:" + ct.name());
				pass = false;
			}
			ct = CascadeType.valueOf(CascadeType.REMOVE.name());
			if (!ct.equals(CascadeType.REMOVE)) {
				logErr( "expected:" + CascadeType.REMOVE.name() + ", actual:" + ct.name());
				pass = false;
			}
			try {
				CascadeType.valueOf("DOESNOTEXIST");
				logErr( "IllegalArgumentException was not thrown");
				pass = false;
			} catch (IllegalArgumentException iae) {
				logTrace( "Received expected IllegalArgumentException");
			}
			try {
				CascadeType.valueOf(null);
				logErr( "NullPointerException was not thrown");
				pass = false;
			} catch (NullPointerException iae) {
				logTrace( "Received expected NullPointerException");
			}
		} catch (Exception e) {
			logErr( "Unexpected exception occurred", e);
			pass = false;
		}

		if (!pass) {
			throw new Exception("cascadeTypeValueOfTest failed");
		}
	}

	/*
	 * @testName: cascadeTypeValuesTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:10
	 * 
	 * @test_Strategy:
	 *
	 */
		public void cascadeTypeValuesTest() throws Exception {
		boolean pass = true;

		logTrace( "Begin cascadeTypeValuesTest");
		try {

			Collection<CascadeType> ct = Arrays.asList(CascadeType.values());
			if (ct.size() != 6) {
				logErr( "Number of CascadeType expected:6, actual:" + ct.size());
				pass = false;
			}

			if (ct.contains(CascadeType.ALL)) {
				logTrace( "received:" + CascadeType.ALL);
			} else {
				logErr( "Expected value:" + CascadeType.ALL);
				pass = false;
			}
			if (ct.contains(CascadeType.DETACH)) {
				logTrace( "received:" + CascadeType.DETACH);
			} else {
				logErr( "Expected value:" + CascadeType.DETACH);
				pass = false;
			}
			if (ct.contains(CascadeType.MERGE)) {
				logTrace( "received:" + CascadeType.MERGE);
			} else {
				logErr( "Expected value:" + CascadeType.MERGE);
				pass = false;
			}
			if (ct.contains(CascadeType.PERSIST)) {
				logTrace( "received:" + CascadeType.PERSIST);
			} else {
				logErr( "Expected value:" + CascadeType.PERSIST);
				pass = false;
			}
			if (ct.contains(CascadeType.REFRESH)) {
				logTrace( "received:" + CascadeType.REFRESH);
			} else {
				logErr( "Expected value:" + CascadeType.REFRESH);
				pass = false;
			}
			if (ct.contains(CascadeType.REMOVE)) {
				logTrace( "received:" + CascadeType.REMOVE);
			} else {
				logErr( "Expected value:" + CascadeType.REMOVE);
				pass = false;
			}

			for (CascadeType c : ct) {
				try {
					CascadeType.valueOf(c.name());
				} catch (Exception e) {
					logErr( "Received exception for valueOf", e);
					pass = false;
				}
			}
		} catch (Exception e) {
			logErr( "Unexpected exception occurred", e);
			pass = false;
		}

		if (!pass) {
			throw new Exception("cascadeTypeValuesTest failed");
		}
	}

	/*
	 * @testName: discriminatorTypeValueOfTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:26
	 * 
	 * @test_Strategy:
	 *
	 */
		public void discriminatorTypeValueOfTest() throws Exception {
		boolean pass = true;

		logTrace( "Begin discriminatorTypeValueOfTest");
		try {

			DiscriminatorType dt = DiscriminatorType.valueOf(DiscriminatorType.CHAR.name());
			if (!dt.equals(DiscriminatorType.CHAR)) {
				logErr( "expected:" + DiscriminatorType.CHAR.name() + ", actual:" + dt.name());
				pass = false;
			}
			dt = DiscriminatorType.valueOf(DiscriminatorType.INTEGER.name());
			if (!dt.equals(DiscriminatorType.INTEGER)) {
				logErr(
						"expected:" + DiscriminatorType.INTEGER.name() + ", actual:" + dt.name());
				pass = false;
			}
			dt = DiscriminatorType.valueOf(DiscriminatorType.STRING.name());
			if (!dt.equals(DiscriminatorType.STRING)) {
				logErr( "expected:" + DiscriminatorType.STRING.name() + ", actual:" + dt.name());
				pass = false;
			}
			try {
				DiscriminatorType.valueOf("DOESNOTEXIST");
				logErr( "IllegalArgumentException was not thrown");
				pass = false;
			} catch (IllegalArgumentException iae) {
				logTrace( "Received expected IllegalArgumentException");
			}
			try {
				DiscriminatorType.valueOf(null);
				logErr( "NullPointerException was not thrown");
				pass = false;
			} catch (NullPointerException iae) {
				logTrace( "Received expected NullPointerException");
			}
		} catch (Exception e) {
			logErr( "Unexpected exception occurred", e);
			pass = false;
		}

		if (!pass) {
			throw new Exception("discriminatorTypeValueOfTest failed");
		}
	}

	/*
	 * @testName: discriminatorTypeValuesTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:27
	 * 
	 * @test_Strategy:
	 *
	 */
		public void discriminatorTypeValuesTest() throws Exception {
		boolean pass = true;

		logTrace( "Begin discriminatorTypeValuesTest");
		try {

			Collection<DiscriminatorType> dt = Arrays.asList(DiscriminatorType.values());
			if (dt.size() != 3) {
				logErr( "Number of DiscriminatorType expected:3, actual:" + dt.size());
				pass = false;
			}

			if (dt.contains(DiscriminatorType.CHAR)) {
				logTrace( "received:" + DiscriminatorType.CHAR);
			} else {
				logErr( "Expected value:" + DiscriminatorType.CHAR);
				pass = false;
			}
			if (dt.contains(DiscriminatorType.INTEGER)) {
				logTrace( "received:" + DiscriminatorType.INTEGER);
			} else {
				logErr( "Expected value:" + DiscriminatorType.INTEGER);
				pass = false;
			}
			if (dt.contains(DiscriminatorType.STRING)) {
				logTrace( "received:" + DiscriminatorType.STRING);
			} else {
				logErr( "Expected value:" + DiscriminatorType.STRING);
				pass = false;
			}

			for (DiscriminatorType d : dt) {
				try {
					DiscriminatorType.valueOf(d.name());
				} catch (Exception e) {
					logErr( "Received exception for valueOf", e);
					pass = false;
				}
			}

		} catch (Exception e) {
			logErr( "Unexpected exception occurred", e);
			pass = false;
		}

		if (!pass) {
			throw new Exception("discriminatorTypeValuesTest failed");
		}
	}

	/*
	 * @testName: enumTypeValueOfTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:74
	 * 
	 * @test_Strategy:
	 *
	 */
		public void enumTypeValueOfTest() throws Exception {
		boolean pass = true;

		logTrace( "Begin enumTypeValueOfTest");
		try {

			EnumType et = EnumType.valueOf(EnumType.ORDINAL.name());
			if (!et.equals(EnumType.ORDINAL)) {
				logErr( "expected:" + EnumType.ORDINAL.name() + ", actual:" + et.name());
				pass = false;
			}
			et = EnumType.valueOf(EnumType.STRING.name());
			if (!et.equals(EnumType.STRING)) {
				logErr( "expected:" + EnumType.STRING.name() + ", actual:" + et.name());
				pass = false;
			}
			try {
				EnumType.valueOf("DOESNOTEXIST");
				logErr( "IllegalArgumentException was not thrown");
				pass = false;
			} catch (IllegalArgumentException iae) {
				logTrace( "Received expected IllegalArgumentException");
			}
			try {
				EnumType.valueOf(null);
				logErr( "NullPointerException was not thrown");
				pass = false;
			} catch (NullPointerException iae) {
				logTrace( "Received expected NullPointerException");
			}
		} catch (Exception e) {
			logErr( "Unexpected exception occurred", e);
			pass = false;
		}

		if (!pass) {
			throw new Exception("enumTypeValueOfTest failed");
		}
	}

	/*
	 * @testName: enumTypeValuesTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:73
	 * 
	 * @test_Strategy:
	 *
	 */
		public void enumTypeValuesTest() throws Exception {
		boolean pass = true;

		logTrace( "Begin enumTypeValuesTest");
		try {

			Collection<EnumType> et = Arrays.asList(EnumType.values());
			if (et.size() != 2) {
				logErr( "Number of EnumType expected:2, actual:" + et.size());
				pass = false;
			}

			if (et.contains(EnumType.ORDINAL)) {
				logTrace( "received:" + EnumType.ORDINAL);
			} else {
				logErr( "Expected value:" + EnumType.ORDINAL);
				pass = false;
			}
			if (et.contains(EnumType.STRING)) {
				logTrace( "received:" + EnumType.STRING);
			} else {
				logErr( "Expected value:" + EnumType.STRING);
				pass = false;
			}

			for (EnumType e : et) {
				try {
					EnumType.valueOf(e.name());
				} catch (Exception ex) {
					logErr( "Received exception for valueOf", ex);
					pass = false;
				}
			}
		} catch (Exception e) {
			logErr( "Unexpected exception occurred", e);
			pass = false;
		}

		if (!pass) {
			throw new Exception("enumTypeValuesTest failed");
		}
	}

	/*
	 * @testName: fetchTypeValueOfTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:75
	 * 
	 * @test_Strategy:
	 *
	 */
		public void fetchTypeValueOfTest() throws Exception {
		boolean pass = true;

		logTrace( "Begin fetchTypeValueOfTest");
		try {

			FetchType ft = FetchType.valueOf(FetchType.EAGER.name());
			if (!ft.equals(FetchType.EAGER)) {
				logErr( "expected:" + FetchType.EAGER.name() + ", actual:" + ft.name());
				pass = false;
			}
			ft = FetchType.valueOf(FetchType.LAZY.name());
			if (!ft.equals(FetchType.LAZY)) {
				logErr( "expected:" + FetchType.LAZY.name() + ", actual:" + ft.name());
				pass = false;
			}
			try {
				FetchType.valueOf("DOESNOTEXIST");
				logErr( "IllegalArgumentException was not thrown");
				pass = false;
			} catch (IllegalArgumentException iae) {
				logTrace( "Received expected IllegalArgumentException");
			}
			try {
				FetchType.valueOf(null);
				logErr( "NullPointerException was not thrown");
				pass = false;
			} catch (NullPointerException iae) {
				logTrace( "Received expected NullPointerException");
			}
		} catch (Exception e) {
			logErr( "Unexpected exception occurred", e);
			pass = false;
		}

		if (!pass) {
			throw new Exception("fetchTypeValueOfTest failed");
		}
	}

	/*
	 * @testName: fetchTypeValuesTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:76
	 * 
	 * @test_Strategy:
	 *
	 */
		public void fetchTypeValuesTest() throws Exception {
		boolean pass = true;

		logTrace( "Begin fetchTypeValuesTest");
		try {

			Collection<FetchType> ft = Arrays.asList(FetchType.values());
			if (ft.size() != 2) {
				logErr( "Number of FetchType expected:2, actual:" + ft.size());
				pass = false;
			}

			if (ft.contains(FetchType.EAGER)) {
				logTrace( "received:" + FetchType.EAGER);
			} else {
				logErr( "Expected value:" + FetchType.EAGER);
				pass = false;
			}
			if (ft.contains(FetchType.LAZY)) {
				logTrace( "received:" + FetchType.LAZY);
			} else {
				logErr( "Expected value:" + FetchType.LAZY);
				pass = false;
			}

			for (FetchType f : ft) {
				try {
					FetchType.valueOf(f.name());
				} catch (Exception e) {
					logErr( "Received exception for valueOf", e);
					pass = false;
				}
			}
		} catch (Exception e) {
			logErr( "Unexpected exception occurred", e);
			pass = false;
		}

		if (!pass) {
			throw new Exception("fetchTypeValuesTest failed");
		}
	}

	/*
	 * @testName: flushModeTypeValueOfTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:79
	 * 
	 * @test_Strategy: Verify the various values of FlushModeType can verified set
	 * using valueOf
	 *
	 */
		public void flushModeTypeValueOfTest() throws Exception {
		boolean pass = true;
		try {

			FlushModeType fmt = FlushModeType.valueOf(FlushModeType.AUTO.name());
			if (!fmt.equals(FlushModeType.AUTO)) {
				logErr( "expected:" + FlushModeType.AUTO.name() + ", actual:" + fmt.name());
				pass = false;
			}
			fmt = FlushModeType.valueOf(FlushModeType.COMMIT.name());
			if (!fmt.equals(FlushModeType.COMMIT)) {
				logErr( "expected:" + FlushModeType.COMMIT.name() + ", actual:" + fmt.name());
				pass = false;
			}
			try {
				FlushModeType.valueOf("DOESNOTEXIST");
				logErr( "IllegalArgumentException was not thrown");
				pass = false;
			} catch (IllegalArgumentException iae) {
				logTrace( "Received expected IllegalArgumentException");
			}
			try {
				FlushModeType.valueOf(null);
				logErr( "NullPointerException was not thrown");
				pass = false;
			} catch (NullPointerException iae) {
				logTrace( "Received expected NullPointerException");
			}
		} catch (Exception e) {
			logErr( "Caught unexpected exception: ", e);
			pass = false;
		}

		if (!pass)
			throw new Exception("flushModeTypeValueOfTest failed");
	}

	/*
	 * @testName: flushModeTypeValuesTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:80
	 * 
	 * @test_Strategy: Verify the various values of FlushModeType using Values then
	 * try valueOf for the returned values
	 *
	 */
		public void flushModeTypeValuesTest() throws Exception {
		boolean pass = true;
		int count = 0;
		try {
			Collection<FlushModeType> fmt = Arrays.asList(FlushModeType.values());
			if (fmt.size() != 2) {
				logErr( "Number of FlushModeType expected:2, actual:" + fmt.size());
				pass = false;
			}

			if (fmt.contains(FlushModeType.COMMIT)) {
				logTrace( "received:" + FlushModeType.COMMIT);
			} else {
				logErr( "Expected value:" + FlushModeType.COMMIT);
				pass = false;
			}
			if (fmt.contains(FlushModeType.AUTO)) {
				logTrace( "received:" + FlushModeType.AUTO);
			} else {
				logErr( "Expected value:" + FlushModeType.AUTO);
				pass = false;
			}

			for (FlushModeType f : fmt) {
				try {
					FlushModeType.valueOf(f.name());
				} catch (Exception e) {
					logErr( "Received exception for valueOf", e);
					pass = false;
				}
			}

		} catch (Exception e) {
			logErr( "Caught unexpected exception: ", e);
			pass = false;
		}

		if (!pass)
			throw new Exception("flushModeTypeValuesTest failed");
	}

	/*
	 * @testName: setgetFlushModeEntityManagerTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:173
	 * 
	 * @test_Strategy: Set and Get the various flushModes of the EntityManager
	 */
		public void setgetFlushModeEntityManagerTest() throws Exception {
		boolean pass = true;
		try {
			EntityTransaction t = getEntityTransaction();
			t.begin();
			EntityManager em = getEntityManager();
			logTrace( "Checking Default mode");
			FlushModeType fmt = em.getFlushMode();
			if (fmt.equals(FlushModeType.AUTO)) {
				logTrace( "Checking COMMIT");
				em.setFlushMode(FlushModeType.COMMIT);
				fmt = em.getFlushMode();
				if (fmt.equals(FlushModeType.COMMIT)) {
					logTrace( "Checking AUTO");
					em.setFlushMode(FlushModeType.AUTO);
					fmt = em.getFlushMode();
					if (!fmt.equals(FlushModeType.AUTO)) {
						logErr(
								"Expected a value of:" + FlushModeType.AUTO.name() + ", actual:" + fmt.name());
						pass = false;
					}
				} else {
					logErr(
							"Expected a value of:" + FlushModeType.COMMIT.name() + ", actual:" + fmt.name());
					pass = false;
				}
			} else {
				logErr(
						"Expected a default value of:" + FlushModeType.AUTO.name() + ", actual:" + fmt.name());
				pass = false;
			}

		} catch (Exception e) {
			logErr( "Caught exception: ", e);
			pass = false;

		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception fe) {
				logErr( "Unexpected exception rolling back TX:", fe);
			}
		}

		if (!pass)
			throw new Exception("setgetFlushModeEntityManagerTest failed");
	}

	/*
	 * @testName: setgetFlushModeTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:46; PERSISTENCE:JAVADOC:56
	 * 
	 * @test_Strategy: Set and Get the various flushModes of a Query
	 */
		public void setgetFlushModeTest() throws Exception {
		boolean pass = true;
		try {
			EntityManager em = getEntityManager();
			Query q = em.createQuery("SELECT o FROM Order o WHERE o.id = 1");
			logTrace( "Getting mode from query");
			FlushModeType fmt = q.getFlushMode();
			if (fmt.equals(em.getFlushMode())) {
				logTrace( "Setting mode to return default mode");
				q.setFlushMode(fmt);
				logTrace( "Setting mode to FlushModeType.COMMIT");
				q.setFlushMode(FlushModeType.COMMIT);
				fmt = q.getFlushMode();
				if (fmt.equals(FlushModeType.COMMIT)) {
					logTrace( "Setting mode to FlushModeType.AUTO");
					q.setFlushMode(FlushModeType.AUTO);
					fmt = q.getFlushMode();
					if (fmt.equals(FlushModeType.AUTO)) {
						logTrace( "Received expected FlushModeType:" + fmt.name());
					} else {
						logErr(
								"Expected a value of:" + FlushModeType.AUTO.name() + ", actual:" + fmt.name());
						pass = false;
					}
				} else {
					logErr(
							"Expected a default value of:" + FlushModeType.COMMIT.name() + ", actual:" + fmt.name());
					pass = false;
				}
			} else {
				logErr(
						"Expected EntityManager value of:" + em.getFlushMode() + ", actual:" + fmt.name());
				pass = false;
			}

		} catch (Exception e) {
			logErr( "Caught exception: ", e);
			pass = false;
		}

		if (!pass)
			throw new Exception("setgetFlushModeTest failed");
	}

	/*
	 * @testName: setgetFlushModeTQTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:441; PERSISTENCE:JAVADOC:56
	 * 
	 * @test_Strategy: Set and Get the various flushModes of a TypedQuery
	 */
		public void setgetFlushModeTQTest() throws Exception {
		boolean pass = true;
		try {
			EntityManager em = getEntityManager();
			TypedQuery<Order> q = em.createQuery("SELECT o FROM Order o WHERE o.id = 1", Order.class);

			FlushModeType fmt = q.getFlushMode();
			if (fmt.equals(em.getFlushMode())) {
				logTrace( "Setting mode to returned default mode");
				q.setFlushMode(fmt);
				logTrace( "Setting mode to FlushModeType.COMMIT");
				q.setFlushMode(FlushModeType.COMMIT);
				fmt = q.getFlushMode();
				if (fmt.equals(FlushModeType.COMMIT)) {
					logTrace( "Setting mode to FlushModeType.AUTO");
					q.setFlushMode(FlushModeType.AUTO);
					fmt = q.getFlushMode();
					if (!fmt.equals(FlushModeType.AUTO)) {
						logErr(
								"Expected a value of:" + FlushModeType.AUTO.name() + ", actual:" + fmt.name());
						pass = false;
					}
				} else {
					logErr(
							"Expected a default value of:" + FlushModeType.COMMIT.name() + ", actual:" + fmt.name());
					pass = false;
				}
			} else {
				logErr(
						"Expected EntityManager value of:" + em.getFlushMode().name() + ", actual:" + fmt.name());
				pass = false;
			}

		} catch (Exception e) {
			logErr( "Caught exception: ", e);
			pass = false;
		}

		if (!pass)
			throw new Exception("setgetFlushModeTQTest failed");
	}

	/*
	 * @testName: generationTypeValueOfTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:83
	 * 
	 * @test_Strategy:
	 *
	 */
		public void generationTypeValueOfTest() throws Exception {
		boolean pass = true;

		try {

			GenerationType ft = GenerationType.valueOf(GenerationType.AUTO.name());
			if (!ft.equals(GenerationType.AUTO)) {
				logErr( "expected:" + GenerationType.AUTO.name() + ", actual:" + ft.name());
				pass = false;
			}
			ft = GenerationType.valueOf(GenerationType.IDENTITY.name());
			if (!ft.equals(GenerationType.IDENTITY)) {
				logErr( "expected:" + GenerationType.IDENTITY.name() + ", actual:" + ft.name());
				pass = false;
			}
			ft = GenerationType.valueOf(GenerationType.SEQUENCE.name());

			if (!ft.equals(GenerationType.SEQUENCE)) {
				logErr( "expected:" + GenerationType.SEQUENCE.name() + ", actual:" + ft.name());
				pass = false;
			}
			ft = GenerationType.valueOf(GenerationType.TABLE.name());
			if (!ft.equals(GenerationType.TABLE)) {
				logErr( "expected:" + GenerationType.TABLE.name() + ", actual:" + ft.name());
				pass = false;
			}
			ft = GenerationType.valueOf(GenerationType.UUID.name());
			if (!ft.equals(GenerationType.UUID)) {
				logErr( "expected:" + GenerationType.UUID.name() + ", actual:" + ft.name());
				pass = false;
			}
			try {
				GenerationType.valueOf("DOESNOTEXIST");
				logErr( "IllegalArgumentException was not thrown");
				pass = false;
			} catch (IllegalArgumentException iae) {
				logTrace( "Received expected IllegalArgumentException");
			}
			try {
				GenerationType.valueOf(null);
				logErr( "NullPointerException was not thrown");
				pass = false;
			} catch (NullPointerException iae) {
				logTrace( "Received expected NullPointerException");
			}
		} catch (Exception e) {
			logErr( "Unexpected exception occurred", e);
			pass = false;
		}

		if (!pass) {
			throw new Exception("generationTypeValueOfTest failed");
		}
	}

	/*
	 * @testName: generationTypeValuesTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:84
	 * 
	 * @test_Strategy:
	 *
	 */
		public void generationTypeValuesTest() throws Exception {
		boolean pass = true;

		try {

			Collection<GenerationType> gt = Arrays.asList(GenerationType.values());
			if (gt.size() != 5) {
				logErr( "Number of GenerationType expected:5, actual:" + gt.size());
				pass = false;
			}

			if (gt.contains(GenerationType.AUTO)) {
				logTrace( "received:" + GenerationType.AUTO);
			} else {
				logErr( "Expected value:" + GenerationType.AUTO);
				pass = false;
			}
			if (gt.contains(GenerationType.IDENTITY)) {
				logTrace( "received:" + GenerationType.IDENTITY);
			} else {
				logErr( "Expected value:" + GenerationType.IDENTITY);
				pass = false;
			}
			if (gt.contains(GenerationType.SEQUENCE)) {
				logTrace( "received:" + GenerationType.SEQUENCE);
			} else {
				logErr( "Expected value:" + GenerationType.SEQUENCE);
				pass = false;
			}
			if (gt.contains(GenerationType.TABLE)) {
				logTrace( "received:" + GenerationType.TABLE);
			} else {
				logErr( "Expected value:" + GenerationType.TABLE);
				pass = false;
			}
			if (gt.contains(GenerationType.UUID)) {
				logTrace( "received:" + GenerationType.UUID);
			} else {
				logErr( "Expected value:" + GenerationType.UUID);
				pass = false;
			}

			for (GenerationType g : gt) {
				try {
					GenerationType.valueOf(g.name());
				} catch (Exception e) {
					logErr( "Received exception for valueOf", e);
					pass = false;
				}
			}
		} catch (Exception e) {
			logErr( "Unexpected exception occurred", e);
			pass = false;
		}

		if (!pass) {
			throw new Exception("generationTypeValuesTest failed");
		}
	}

	/*
	 * @testName: inheritanceTypeValueOfTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:87
	 * 
	 * @test_Strategy:
	 *
	 */
		public void inheritanceTypeValueOfTest() throws Exception {
		boolean pass = true;

		try {

			InheritanceType ft = InheritanceType.valueOf(InheritanceType.JOINED.name());
			if (!ft.equals(InheritanceType.JOINED)) {
				logErr( "expected:" + InheritanceType.JOINED.name() + ", actual:" + ft.name());
				pass = false;
			}
			ft = InheritanceType.valueOf(InheritanceType.SINGLE_TABLE.name());
			if (!ft.equals(InheritanceType.SINGLE_TABLE)) {
				logErr(
						"expected:" + InheritanceType.SINGLE_TABLE.name() + ", actual:" + ft.name());
				pass = false;
			}
			ft = InheritanceType.valueOf(InheritanceType.TABLE_PER_CLASS.name());

			if (!ft.equals(InheritanceType.TABLE_PER_CLASS)) {
				logErr(
						"expected:" + InheritanceType.TABLE_PER_CLASS.name() + ", actual:" + ft.name());
				pass = false;
			}
			try {
				InheritanceType.valueOf("DOESNOTEXIST");
				logErr( "IllegalArgumentException was not thrown");
				pass = false;
			} catch (IllegalArgumentException iae) {
				logTrace( "Received expected IllegalArgumentException");
			}
			try {
				InheritanceType.valueOf(null);
				logErr( "NullPointerException was not thrown");
				pass = false;
			} catch (NullPointerException iae) {
				logTrace( "Received expected NullPointerException");
			}
		} catch (Exception e) {
			logErr( "Unexpected exception occurred", e);
			pass = false;
		}

		if (!pass) {
			throw new Exception("inheritanceTypeValueOfTest failed");
		}
	}

	/*
	 * @testName: inheritanceTypeValuesTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:88
	 * 
	 * @test_Strategy:
	 *
	 */
		public void inheritanceTypeValuesTest() throws Exception {
		boolean pass = true;

		try {

			Collection<InheritanceType> it = Arrays.asList(InheritanceType.values());
			if (it.size() != 3) {
				logErr( "Number of InheritanceType expected:3, actual:" + it.size());
				pass = false;
			}

			if (it.contains(InheritanceType.JOINED)) {
				logTrace( "received:" + InheritanceType.JOINED);
			} else {
				logErr( "Expected value:" + InheritanceType.JOINED);
				pass = false;
			}
			if (it.contains(InheritanceType.SINGLE_TABLE)) {
				logTrace( "received:" + InheritanceType.SINGLE_TABLE);
			} else {
				logErr( "Expected value:" + InheritanceType.SINGLE_TABLE);
				pass = false;
			}
			if (it.contains(InheritanceType.TABLE_PER_CLASS)) {
				logTrace( "received:" + InheritanceType.TABLE_PER_CLASS);
			} else {
				logErr( "Expected value:" + InheritanceType.TABLE_PER_CLASS);
				pass = false;
			}

			for (InheritanceType i : it) {
				try {
					InheritanceType.valueOf(i.name());
				} catch (Exception e) {
					logErr( "Received exception for valueOf", e);
					pass = false;
				}
			}
		} catch (Exception e) {
			logErr( "Unexpected exception occurred", e);
			pass = false;
		}

		if (!pass) {
			throw new Exception("inheritanceTypeValuesTest failed");
		}
	}

	/*
	 * @testName: lockModeTypeValueOfTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:104
	 * 
	 * @test_Strategy:
	 *
	 */
		public void lockModeTypeValueOfTest() throws Exception {
		boolean pass = true;

		try {

			LockModeType lm = LockModeType.valueOf(LockModeType.NONE.name());
			if (!lm.equals(LockModeType.NONE)) {
				logErr( "expected:" + LockModeType.NONE + ", actual:" + lm.name());
				pass = false;
			}
			lm = LockModeType.valueOf(LockModeType.OPTIMISTIC.name());
			if (!lm.equals(LockModeType.OPTIMISTIC)) {
				logErr( "expected:" + LockModeType.OPTIMISTIC.name() + ", actual:" + lm.name());
				pass = false;
			}
			lm = LockModeType.valueOf(LockModeType.OPTIMISTIC_FORCE_INCREMENT.name());
			if (!lm.equals(LockModeType.OPTIMISTIC_FORCE_INCREMENT)) {
				logErr(
						"expected:" + LockModeType.OPTIMISTIC_FORCE_INCREMENT.name() + ", actual:" + lm.name());
				pass = false;
			}
			lm = LockModeType.valueOf(LockModeType.PESSIMISTIC_READ.name());
			if (!lm.equals(LockModeType.PESSIMISTIC_READ)) {
				logErr(
						"expected:" + LockModeType.PESSIMISTIC_READ.name() + ", actual:" + lm.name());
				pass = false;
			}
			lm = LockModeType.valueOf(LockModeType.PESSIMISTIC_WRITE.name());
			if (!lm.equals(LockModeType.PESSIMISTIC_WRITE)) {
				logErr(
						"expected:" + LockModeType.PESSIMISTIC_WRITE.name() + ", actual:" + lm.name());
				pass = false;
			}
			lm = LockModeType.valueOf(LockModeType.READ.name());
			if (!lm.equals(LockModeType.READ)) {
				logErr( "expected:" + LockModeType.READ.name() + ", actual:" + lm.name());
				pass = false;
			}
			lm = LockModeType.valueOf(LockModeType.WRITE.name());
			if (!lm.equals(LockModeType.WRITE)) {
				logErr( "expected:" + LockModeType.WRITE.name() + ", actual:" + lm.name());
				pass = false;
			}

			try {
				LockModeType.valueOf("DOESNOTEXIST");
				logErr( "IllegalArgumentException was not thrown");
				pass = false;
			} catch (IllegalArgumentException iae) {
				logTrace( "Received expected IllegalArgumentException");
			}
			try {
				LockModeType.valueOf(null);
				logErr( "NullPointerException was not thrown");
				pass = false;
			} catch (NullPointerException iae) {
				logTrace( "Received expected NullPointerException");
			}
		} catch (Exception e) {
			logErr( "Unexpected exception occurred", e);
			pass = false;
		}

		if (!pass) {
			throw new Exception("lockModeTypeValueOfTest failed");
		}
	}

	/*
	 * @testName: lockModeTypeValuesTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:105
	 * 
	 * @test_Strategy: Test each LockModeType value
	 *
	 */
		public void lockModeTypeValuesTest() throws Exception {
		boolean pass = true;

		try {

			Collection<LockModeType> lm = Arrays.asList(LockModeType.values());
			if (lm.size() != 8) {
				logErr( "Number of LockModeTypes expected:8, actual:" + lm.size());
				pass = false;
			}

			if (lm.contains(LockModeType.NONE)) {
				logTrace( "received:" + LockModeType.NONE);
			} else {
				logErr( "Expected value:" + LockModeType.NONE);
				pass = false;
			}
			if (lm.contains(LockModeType.OPTIMISTIC)) {
				logTrace( "received:" + LockModeType.OPTIMISTIC);
			} else {
				logErr( "Expected value:" + LockModeType.OPTIMISTIC);
				pass = false;
			}
			if (lm.contains(LockModeType.OPTIMISTIC_FORCE_INCREMENT)) {
				logTrace( "received:" + LockModeType.OPTIMISTIC_FORCE_INCREMENT);
			} else {
				logErr( "Expected value:" + LockModeType.OPTIMISTIC_FORCE_INCREMENT);
				pass = false;
			}
			if (lm.contains(LockModeType.PESSIMISTIC_FORCE_INCREMENT)) {
				logTrace( "received:" + LockModeType.PESSIMISTIC_FORCE_INCREMENT);
			} else {
				logErr( "Expected value:" + LockModeType.PESSIMISTIC_FORCE_INCREMENT);
				pass = false;
			}
			if (lm.contains(LockModeType.PESSIMISTIC_READ)) {
				logTrace( "received:" + LockModeType.PESSIMISTIC_READ);
			} else {
				logErr( "Expected value:" + LockModeType.PESSIMISTIC_READ);
				pass = false;
			}
			if (lm.contains(LockModeType.PESSIMISTIC_WRITE)) {
				logTrace( "received:" + LockModeType.PESSIMISTIC_WRITE);
			} else {
				logErr( "Expected value:" + LockModeType.PESSIMISTIC_WRITE);
				pass = false;
			}
			if (lm.contains(LockModeType.READ)) {
				logTrace( "received:" + LockModeType.READ);
			} else {
				logErr( "Expected value:" + LockModeType.READ);
				pass = false;
			}
			if (lm.contains(LockModeType.WRITE)) {
				logTrace( "received:" + LockModeType.WRITE);
			} else {
				logErr( "Expected value:" + LockModeType.WRITE);
				pass = false;
			}

			for (LockModeType l : lm) {
				try {
					LockModeType.valueOf(l.name());
				} catch (Exception e) {
					logErr( "Received exception for valueOf", e);
					pass = false;
				}
			}

		} catch (Exception e) {
			logErr( "Unexpected exception occurred", e);
			pass = false;
		}

		if (!pass) {
			throw new Exception("lockModeTypeValuesTest failed");
		}
	}

	/*
	 * @testName: persistenceContextTypeValueOfTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:154
	 * 
	 * @test_Strategy:
	 *
	 */
		public void persistenceContextTypeValueOfTest() throws Exception {
		boolean pass = true;

		try {

			PersistenceContextType pct = PersistenceContextType.valueOf(PersistenceContextType.EXTENDED.name());
			if (!pct.equals(PersistenceContextType.EXTENDED)) {
				logErr(
						"expected:" + PersistenceContextType.EXTENDED.name() + ", actual:" + pct.name());
				pass = false;
			}
			pct = PersistenceContextType.valueOf(PersistenceContextType.TRANSACTION.name());
			if (!pct.equals(PersistenceContextType.TRANSACTION)) {
				logErr(
						"expected:" + PersistenceContextType.TRANSACTION.name() + ", actual:" + pct.name());
				pass = false;
			}
			try {
				PersistenceContextType.valueOf("DOESNOTEXIST");
				logErr( "IllegalArgumentException was not thrown");
				pass = false;
			} catch (IllegalArgumentException iae) {
				logTrace( "Received expected IllegalArgumentException");
			}
			try {
				PersistenceContextType.valueOf(null);
				logErr( "NullPointerException was not thrown");
				pass = false;
			} catch (NullPointerException iae) {
				logTrace( "Received expected NullPointerException");
			}
		} catch (Exception e) {
			logErr( "Unexpected exception occurred", e);
			pass = false;
		}

		if (!pass) {
			throw new Exception("persistenceContextTypeValueOfTest failed");
		}
	}

	/*
	 * @testName: persistenceContextTypeValuesTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:155
	 * 
	 * @test_Strategy:
	 *
	 */
		public void persistenceContextTypeValuesTest() throws Exception {
		boolean pass = true;

		try {

			Collection<PersistenceContextType> pct = Arrays.asList(PersistenceContextType.values());
			if (pct.size() != 2) {
				logErr( "Number of PersistenceContextType expected:2, actual:" + pct.size());
				pass = false;
			}

			if (pct.contains(PersistenceContextType.EXTENDED)) {
				logTrace( "received:" + PersistenceContextType.EXTENDED);
			} else {
				logErr( "Expected value:" + PersistenceContextType.EXTENDED);
				pass = false;
			}
			if (pct.contains(PersistenceContextType.TRANSACTION)) {
				logTrace( "received:" + PersistenceContextType.TRANSACTION);
			} else {
				logErr( "Expected value:" + PersistenceContextType.TRANSACTION);
				pass = false;
			}

			for (PersistenceContextType p : pct) {
				try {
					PersistenceContextType.valueOf(p.name());
				} catch (Exception e) {
					logErr( "Received exception for valueOf", e);
					pass = false;
				}
			}
		} catch (Exception e) {
			logErr( "Unexpected exception occurred", e);
			pass = false;
		}

		if (!pass) {
			throw new Exception("persistenceContextTypeValuesTest failed");
		}
	}

	/*
	 * @testName: pessimisticLockScopeValueOfTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:397
	 * 
	 * @test_Strategy:
	 *
	 */
		public void pessimisticLockScopeValueOfTest() throws Exception {
		boolean pass = true;

		try {

			PessimisticLockScope pls = PessimisticLockScope.valueOf(PessimisticLockScope.EXTENDED.name());
			if (!pls.equals(PessimisticLockScope.EXTENDED)) {
				logErr(
						"expected:" + PessimisticLockScope.EXTENDED.name() + ", actual:" + pls.name());
				pass = false;
			}
			pls = PessimisticLockScope.valueOf(PessimisticLockScope.NORMAL.name());
			if (!pls.equals(PessimisticLockScope.NORMAL)) {
				logErr(
						"expected:" + PessimisticLockScope.NORMAL.name() + ", actual:" + pls.name());
				pass = false;
			}
			try {
				PessimisticLockScope.valueOf("DOESNOTEXIST");
				logErr( "IllegalArgumentException was not thrown");
				pass = false;
			} catch (IllegalArgumentException iae) {
				logTrace( "Received expected IllegalArgumentException");
			}
			try {
				PessimisticLockScope.valueOf(null);
				logErr( "NullPointerException was not thrown");
				pass = false;
			} catch (NullPointerException iae) {
				logTrace( "Received expected NullPointerException");
			}
		} catch (Exception e) {
			logErr( "Unexpected exception occurred", e);
			pass = false;
		}

		if (!pass) {
			throw new Exception("pessimisticLockScopeValueOfTest failed");
		}
	}

	/*
	 * @testName: pessimisticLockScopeValuesTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:398
	 * 
	 * @test_Strategy:
	 *
	 */
		public void pessimisticLockScopeValuesTest() throws Exception {
		boolean pass = true;

		try {

			Collection<PessimisticLockScope> pls = Arrays.asList(PessimisticLockScope.values());
			if (pls.size() != 2) {
				logErr( "Number of PessimisticLockScope expected:2, actual:" + pls.size());
				pass = false;
			}

			if (pls.contains(PessimisticLockScope.EXTENDED)) {
				logTrace( "received:" + PessimisticLockScope.EXTENDED);
			} else {
				logErr( "Expected value:" + PessimisticLockScope.EXTENDED);
				pass = false;
			}
			if (pls.contains(PessimisticLockScope.NORMAL)) {
				logTrace( "received:" + PessimisticLockScope.NORMAL);
			} else {
				logErr( "Expected value:" + PessimisticLockScope.NORMAL);
				pass = false;
			}

			for (PessimisticLockScope p : pls) {
				try {
					PessimisticLockScope.valueOf(p.name());
				} catch (Exception e) {
					logErr( "Received exception for valueOf", e);
					pass = false;
				}
			}

		} catch (Exception e) {
			logErr( "Unexpected exception occurred", e);
			pass = false;
		}

		if (!pass) {
			throw new Exception("pessimisticLockScopeValuesTest failed");
		}
	}

	/*
	 * @testName: sharedCacheModeValueOfTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:427; PERSISTENCE:SPEC:1910;
	 * 
	 * @test_Strategy:
	 *
	 */
		public void sharedCacheModeValueOfTest() throws Exception {
		boolean pass = true;

		try {

			SharedCacheMode scm = SharedCacheMode.valueOf(SharedCacheMode.ALL.name());
			if (!scm.equals(SharedCacheMode.ALL)) {
				logErr( "expected:" + SharedCacheMode.ALL.name() + ", actual:" + scm.name());
				pass = false;
			}
			scm = SharedCacheMode.valueOf(SharedCacheMode.DISABLE_SELECTIVE.name());
			if (!scm.equals(SharedCacheMode.DISABLE_SELECTIVE)) {
				logErr(
						"expected:" + SharedCacheMode.DISABLE_SELECTIVE.name() + ", actual:" + scm.name());
				pass = false;
			}
			scm = SharedCacheMode.valueOf(SharedCacheMode.ENABLE_SELECTIVE.name());
			if (!scm.equals(SharedCacheMode.ENABLE_SELECTIVE)) {
				logErr(
						"expected:" + SharedCacheMode.ENABLE_SELECTIVE.name() + ", actual:" + scm.name());
				pass = false;
			}
			scm = SharedCacheMode.valueOf(SharedCacheMode.NONE.name());
			if (!scm.equals(SharedCacheMode.NONE)) {
				logErr( "expected:" + SharedCacheMode.NONE.name() + ", actual:" + scm.name());
				pass = false;
			}
			scm = SharedCacheMode.valueOf(SharedCacheMode.UNSPECIFIED.name());
			if (!scm.equals(SharedCacheMode.UNSPECIFIED)) {
				logErr(
						"expected:" + SharedCacheMode.UNSPECIFIED.name() + ", actual:" + scm.name());
				pass = false;
			}
			try {
				SharedCacheMode.valueOf("DOESNOTEXIST");
				logErr( "IllegalArgumentException was not thrown");
				pass = false;
			} catch (IllegalArgumentException iae) {
				logTrace( "Received expected IllegalArgumentException");
			}
			try {
				SharedCacheMode.valueOf(null);
				logErr( "NullPointerException was not thrown");
				pass = false;
			} catch (NullPointerException iae) {
				logTrace( "Received expected NullPointerException");
			}
		} catch (Exception e) {
			logErr( "Unexpected exception occurred", e);
			pass = false;
		}

		if (!pass) {
			throw new Exception("sharedCacheModeValueOfTest failed");
		}
	}

	/*
	 * @testName: sharedCacheModeValuesTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:428; PERSISTENCE:SPEC:1910;
	 * 
	 * @test_Strategy:
	 *
	 */
		public void sharedCacheModeValuesTest() throws Exception {
		boolean pass = true;

		try {

			Collection<SharedCacheMode> scm = Arrays.asList(SharedCacheMode.values());
			if (scm.size() != 5) {
				logErr( "Number of SharedCacheMode expected:5, actual:" + scm.size());
				pass = false;
			}

			if (scm.contains(SharedCacheMode.ALL)) {
				logTrace( "received:" + SharedCacheMode.ALL);
			} else {
				logErr( "Expected value:" + SharedCacheMode.ALL);
				pass = false;
			}
			if (scm.contains(SharedCacheMode.DISABLE_SELECTIVE)) {
				logTrace( "received:" + SharedCacheMode.DISABLE_SELECTIVE);
			} else {
				logErr( "Expected value:" + SharedCacheMode.DISABLE_SELECTIVE);
				pass = false;
			}
			if (scm.contains(SharedCacheMode.ENABLE_SELECTIVE)) {
				logTrace( "received:" + SharedCacheMode.ENABLE_SELECTIVE);
			} else {
				logErr( "Expected value:" + SharedCacheMode.ENABLE_SELECTIVE);
				pass = false;
			}
			if (scm.contains(SharedCacheMode.NONE)) {
				logTrace( "received:" + SharedCacheMode.NONE);
			} else {
				logErr( "Expected value:" + SharedCacheMode.NONE);
				pass = false;
			}
			if (scm.contains(SharedCacheMode.UNSPECIFIED)) {
				logTrace( "received:" + SharedCacheMode.UNSPECIFIED);
			} else {
				logErr( "Expected value:" + SharedCacheMode.UNSPECIFIED);
				pass = false;
			}

			for (SharedCacheMode s : scm) {
				try {
					SharedCacheMode.valueOf(s.name());
				} catch (Exception e) {
					logErr( "Received exception for valueOf", e);
					pass = false;
				}
			}

		} catch (Exception e) {
			logErr( "Unexpected exception occurred", e);
			pass = false;
		}

		if (!pass) {
			throw new Exception("sharedCacheModeValuesTest failed");
		}
	}

	/*
	 * @testName: validationModeValueOfTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:455
	 * 
	 * @test_Strategy:
	 *
	 */
		public void validationModeValueOfTest() throws Exception {
		boolean pass = true;

		try {

			ValidationMode vm = ValidationMode.valueOf(ValidationMode.CALLBACK.name());
			if (!vm.equals(ValidationMode.CALLBACK)) {
				logErr( "expected:" + ValidationMode.CALLBACK.name() + ", actual:" + vm.name());
				pass = false;
			}
			vm = ValidationMode.valueOf(ValidationMode.AUTO.name());
			if (!vm.equals(ValidationMode.AUTO)) {
				logErr( "expected:" + ValidationMode.AUTO.name() + ", actual:" + vm.name());
				pass = false;
			}
			vm = ValidationMode.valueOf(ValidationMode.NONE.name());
			if (!vm.equals(ValidationMode.NONE)) {
				logErr( "expected:" + ValidationMode.NONE.name() + ", actual:" + vm.name());
				pass = false;
			}
			try {
				ValidationMode.valueOf("DOESNOTEXIST");
				logErr( "IllegalArgumentException was not thrown");
				pass = false;
			} catch (IllegalArgumentException iae) {
				logTrace( "Received expected IllegalArgumentException");
			}
			try {
				ValidationMode.valueOf(null);
				logErr( "NullPointerException was not thrown");
				pass = false;
			} catch (NullPointerException iae) {
				logTrace( "Received expected NullPointerException");
			}
		} catch (Exception e) {
			logErr( "Unexpected exception occurred", e);
			pass = false;
		}

		if (!pass) {
			throw new Exception("validationModeValueOfTest failed");
		}
	}

	/*
	 * @testName: validationModeValuesTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:456
	 * 
	 * @test_Strategy:
	 *
	 */
		public void validationModeValuesTest() throws Exception {
		boolean pass = true;

		try {

			Collection<ValidationMode> vm = Arrays.asList(ValidationMode.values());
			if (vm.size() != 3) {
				logErr( "Number of ValidationMode expected:3, actual:" + vm.size());
				pass = false;
			}

			if (vm.contains(ValidationMode.CALLBACK)) {
				logTrace( "received:" + ValidationMode.CALLBACK);
			} else {
				logErr( "Expected value:" + ValidationMode.CALLBACK);
				pass = false;
			}
			if (vm.contains(ValidationMode.AUTO)) {
				logTrace( "received:" + ValidationMode.AUTO);
			} else {
				logErr( "Expected value:" + ValidationMode.AUTO);
				pass = false;
			}
			if (vm.contains(ValidationMode.NONE)) {
				logTrace( "received:" + ValidationMode.NONE);
			} else {
				logErr( "Expected value:" + ValidationMode.NONE);
				pass = false;
			}

			for (ValidationMode v : vm) {
				try {
					ValidationMode.valueOf(v.name());
				} catch (Exception e) {
					logErr( "Received exception for valueOf", e);
					pass = false;
				}
			}

		} catch (Exception e) {
			logErr( "Unexpected exception occurred", e);
			pass = false;
		}

		if (!pass) {
			throw new Exception("validationModeValuesTest failed");
		}
	}

	/*
	 * @testName: temporalTypeValuesTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:218
	 * 
	 * @test_Strategy:
	 *
	 */
		public void temporalTypeValuesTest() throws Exception {
		boolean pass = true;

		Collection<TemporalType> tt = Arrays.asList(TemporalType.values());
		if (tt.size() != 3) {
			logErr( "Number of TemporalType expected:3, actual:" + tt.size());
			pass = false;
		}

		if (tt.contains(TemporalType.DATE)) {
			logTrace( "received:" + TemporalType.DATE);
		} else {
			logErr( "Expected value:" + TemporalType.DATE);
			pass = false;
		}
		if (tt.contains(TemporalType.TIME)) {
			logTrace( "received:" + TemporalType.TIME);
		} else {
			logErr( "Expected value:" + TemporalType.TIME);
			pass = false;
		}
		if (tt.contains(TemporalType.TIMESTAMP)) {
			logTrace( "received:" + TemporalType.TIMESTAMP);
		} else {
			logErr( "Expected value:" + TemporalType.TIMESTAMP);
			pass = false;
		}

		for (TemporalType t : tt) {
			try {
				TemporalType.valueOf(t.name());
			} catch (Exception e) {
				logErr( "Received exception for valueOf", e);
				pass = false;
			}
		}

		if (!pass) {
			throw new Exception("temporalTypeValuesTest failed");
		}

	}

	/*
	 * @testName: temporalTypeValueOfTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:217
	 * 
	 * @test_Strategy:
	 *
	 */
		public void temporalTypeValueOfTest() throws Exception {
		boolean pass = true;

		try {

			TemporalType tt = TemporalType.valueOf(TemporalType.DATE.name());
			if (!tt.equals(TemporalType.DATE)) {
				logErr( "expected:" + TemporalType.DATE.name() + ", actual:" + tt.name());
				pass = false;
			}
			tt = TemporalType.valueOf(TemporalType.TIME.name());
			if (!tt.equals(TemporalType.TIME)) {
				logErr( "expected:" + TemporalType.TIME.name() + ", actual:" + tt.name());
				pass = false;
			}
			tt = TemporalType.valueOf(TemporalType.TIMESTAMP.name());
			if (!tt.equals(TemporalType.TIMESTAMP)) {
				logErr( "expected:" + TemporalType.TIMESTAMP.name() + ", actual:" + tt.name());
				pass = false;
			}

			try {
				TemporalType.valueOf("DOESNOTEXIST");
				logErr( "IllegalArgumentException was not thrown");
				pass = false;
			} catch (IllegalArgumentException iae) {
				logTrace( "Received expected IllegalArgumentException");
			}
			try {
				TemporalType.valueOf(null);
				logErr( "NullPointerException was not thrown");
				pass = false;
			} catch (NullPointerException iae) {
				logTrace( "Received expected NullPointerException");
			}
		} catch (Exception e) {
			pass = false;
			logErr( "Unexpected exception occurred", e);
		}

		if (!pass) {
			throw new Exception("temporalTypeValueOfTest failed");
		}
	}

	/*
	 * @testName: joinTypeValuesTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1073
	 * 
	 * @test_Strategy:
	 *
	 */
		public void joinTypeValuesTest() throws Exception {
		boolean pass = true;

		Collection<JoinType> jt = Arrays.asList(JoinType.values());
		if (jt.size() != 3) {
			logErr( "Number of TemporalType expected:3, actual:" + jt.size());
			pass = false;
		}

		if (jt.contains(JoinType.LEFT)) {
			logTrace( "received:" + JoinType.LEFT);
		} else {
			logErr( "Expected value:" + JoinType.LEFT);
			pass = false;
		}
		if (jt.contains(JoinType.INNER)) {
			logTrace( "received:" + JoinType.INNER);
		} else {
			logErr( "Expected value:" + JoinType.INNER);
			pass = false;
		}
		if (jt.contains(JoinType.RIGHT)) {
			logTrace( "received:" + JoinType.RIGHT);
		} else {
			logErr( "Expected value:" + JoinType.RIGHT);
			pass = false;
		}

		for (JoinType j : jt) {
			try {
				JoinType.valueOf(j.name());
			} catch (Exception e) {
				logErr( "Received exception for valueOf", e);
				pass = false;
			}
		}

		if (!pass) {
			throw new Exception("joinTypeValuesTest failed");
		}
	}

	/*
	 * @testName: joinTypeValueOfTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1072
	 * 
	 * @test_Strategy:
	 *
	 */
		public void joinTypeValueOfTest() throws Exception {
		boolean pass = true;

		try {

			JoinType jt = JoinType.valueOf(JoinType.LEFT.name());
			if (!jt.equals(JoinType.LEFT)) {
				logErr( "expected:" + JoinType.LEFT.name() + ", actual:" + jt.name());
				pass = false;
			}
			jt = JoinType.valueOf(JoinType.INNER.name());
			if (!jt.equals(JoinType.INNER)) {
				logErr( "expected:" + JoinType.INNER.name() + ", actual:" + jt.name());
				pass = false;
			}
			jt = JoinType.valueOf(JoinType.RIGHT.name());
			if (!jt.equals(JoinType.RIGHT)) {
				logErr( "expected:" + JoinType.RIGHT.name() + ", actual:" + jt.name());
				pass = false;
			}

			try {
				JoinType.valueOf("DOESNOTEXIST");
				logErr( "IllegalArgumentException was not thrown");
				pass = false;
			} catch (IllegalArgumentException iae) {
				logTrace( "Received expected IllegalArgumentException");
			}
			try {
				JoinType.valueOf(null);
				logErr( "NullPointerException was not thrown");
				pass = false;
			} catch (NullPointerException iae) {
				logTrace( "Received expected NullPointerException");
			}
		} catch (Exception e) {
			pass = false;
			logErr( "Unexpected exception occurred", e);
		}

		if (!pass) {
			throw new Exception("joinTypeValueOfTest failed");
		}
	}

	/*
	 * @testName: persistentAttributeTypeValuesTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1221
	 * 
	 * @test_Strategy:
	 *
	 */
		public void persistentAttributeTypeValuesTest() throws Exception {
		boolean pass = true;

		Collection<PersistentAttributeType> pat = Arrays.asList(PersistentAttributeType.values());

		if (pat.size() != 7) {
			logErr( "Number of TemporalType expected:7, actual:" + pat.size());
			pass = false;
		}

		if (pat.contains(PersistentAttributeType.BASIC)) {
			logTrace( "received:" + PersistentAttributeType.BASIC);
		} else {
			logErr( "Expected value:" + PersistentAttributeType.BASIC);
			pass = false;
		}
		if (pat.contains(PersistentAttributeType.ELEMENT_COLLECTION)) {
			logTrace( "received:" + PersistentAttributeType.ELEMENT_COLLECTION);
		} else {
			logErr( "Expected value:" + PersistentAttributeType.ELEMENT_COLLECTION);
			pass = false;
		}
		if (pat.contains(PersistentAttributeType.EMBEDDED)) {
			logTrace( "received:" + PersistentAttributeType.EMBEDDED);
		} else {
			logErr( "Expected value:" + PersistentAttributeType.EMBEDDED);
			pass = false;
		}
		if (pat.contains(PersistentAttributeType.MANY_TO_MANY)) {
			logTrace( "received:" + PersistentAttributeType.MANY_TO_MANY);
		} else {
			logErr( "Expected value:" + PersistentAttributeType.MANY_TO_MANY);
			pass = false;
		}
		if (pat.contains(PersistentAttributeType.MANY_TO_ONE)) {
			logTrace( "received:" + PersistentAttributeType.MANY_TO_ONE);
		} else {
			logErr( "Expected value:" + PersistentAttributeType.MANY_TO_ONE);
			pass = false;
		}
		if (pat.contains(PersistentAttributeType.ONE_TO_MANY)) {
			logTrace( "received:" + PersistentAttributeType.ONE_TO_MANY);
		} else {
			logErr( "Expected value:" + PersistentAttributeType.ONE_TO_MANY);
			pass = false;
		}
		if (pat.contains(PersistentAttributeType.ONE_TO_ONE)) {
			logTrace( "received:" + PersistentAttributeType.ONE_TO_ONE);
		} else {
			logErr( "Expected value:" + PersistentAttributeType.ONE_TO_ONE);
			pass = false;
		}

		for (PersistentAttributeType p : pat) {
			try {
				PersistentAttributeType.valueOf(p.name());
			} catch (Exception e) {
				logErr( "Received exception for valueOf", e);
				pass = false;
			}
		}

		if (!pass) {
			throw new Exception("persistentAttributeTypeValuesTest failed");
		}
	}

	/*
	 * @testName: persistentAttributeTypeValueOfTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1220
	 * 
	 * @test_Strategy:
	 *
	 */
		public void persistentAttributeTypeValueOfTest() throws Exception {
		boolean pass = true;

		try {

			PersistentAttributeType pat = PersistentAttributeType.valueOf(PersistentAttributeType.BASIC.name());
			if (!pat.equals(PersistentAttributeType.BASIC)) {
				logErr(
						"expected:" + PersistentAttributeType.BASIC.name() + ", actual:" + pat.name());
				pass = false;
			}
			pat = PersistentAttributeType.valueOf(PersistentAttributeType.ELEMENT_COLLECTION.name());
			if (!pat.equals(PersistentAttributeType.ELEMENT_COLLECTION)) {
				logErr(
						"expected:" + PersistentAttributeType.ELEMENT_COLLECTION.name() + ", actual:" + pat.name());
				pass = false;
			}
			pat = PersistentAttributeType.valueOf(PersistentAttributeType.EMBEDDED.name());
			if (!pat.equals(PersistentAttributeType.EMBEDDED)) {
				logErr(
						"expected:" + PersistentAttributeType.EMBEDDED.name() + ", actual:" + pat.name());
				pass = false;
			}
			pat = PersistentAttributeType.valueOf(PersistentAttributeType.MANY_TO_MANY.name());
			if (!pat.equals(PersistentAttributeType.MANY_TO_MANY)) {
				logErr(
						"expected:" + PersistentAttributeType.MANY_TO_MANY.name() + ", actual:" + pat.name());
				pass = false;
			}
			pat = PersistentAttributeType.valueOf(PersistentAttributeType.MANY_TO_ONE.name());
			if (!pat.equals(PersistentAttributeType.MANY_TO_ONE)) {
				logErr(
						"expected:" + PersistentAttributeType.MANY_TO_ONE.name() + ", actual:" + pat.name());
				pass = false;
			}
			pat = PersistentAttributeType.valueOf(PersistentAttributeType.ONE_TO_MANY.name());
			if (!pat.equals(PersistentAttributeType.ONE_TO_MANY)) {
				logErr(
						"expected:" + PersistentAttributeType.ONE_TO_MANY.name() + ", actual:" + pat.name());
				pass = false;
			}
			pat = PersistentAttributeType.valueOf(PersistentAttributeType.ONE_TO_ONE.name());
			if (!pat.equals(PersistentAttributeType.ONE_TO_ONE)) {
				logErr(
						"expected:" + PersistentAttributeType.ONE_TO_ONE.name() + ", actual:" + pat.name());
				pass = false;
			}
			try {
				PersistentAttributeType.valueOf("DOESNOTEXIST");
				logErr( "IllegalArgumentException was not thrown");
				pass = false;
			} catch (IllegalArgumentException iae) {
				logTrace( "Received expected IllegalArgumentException");
			}
			try {
				PersistentAttributeType.valueOf(null);
				logErr( "NullPointerException was not thrown");
				pass = false;
			} catch (NullPointerException iae) {
				logTrace( "Received expected NullPointerException");
			}
		} catch (Exception e) {
			pass = false;
			logErr( "Unexpected exception occurred", e);
		}

		if (!pass) {
			throw new Exception("persistentAttributeTypeValueOfTest failed");
		}
	}

	/*
	 * @testName: bindableTypeValuesTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1227
	 * 
	 * @test_Strategy:
	 *
	 */
		public void bindableTypeValuesTest() throws Exception {
		boolean pass = true;

		Collection<BindableType> bt = Arrays.asList(BindableType.values());

		if (bt.size() != 3) {
			logErr( "Number of TemporalType expected:3, actual:" + bt.size());
			pass = false;
		}

		if (bt.contains(BindableType.ENTITY_TYPE)) {
			logTrace( "received:" + BindableType.ENTITY_TYPE.name());
		} else {
			logErr( "Expected value:" + BindableType.ENTITY_TYPE.name());
			pass = false;
		}
		if (bt.contains(BindableType.PLURAL_ATTRIBUTE)) {
			logTrace( "received:" + BindableType.PLURAL_ATTRIBUTE.name());
		} else {
			logErr( "Expected value:" + BindableType.PLURAL_ATTRIBUTE.name());
			pass = false;
		}
		if (bt.contains(BindableType.SINGULAR_ATTRIBUTE)) {
			logTrace( "received:" + BindableType.SINGULAR_ATTRIBUTE.name());
		} else {
			logErr( "Expected value:" + BindableType.SINGULAR_ATTRIBUTE.name());
			pass = false;
		}

		for (BindableType b : bt) {
			try {
				BindableType.valueOf(b.name());
			} catch (Exception e) {
				logErr( "Received exception for valueOf", e);
				pass = false;
			}
		}

		if (!pass) {
			throw new Exception("bindableTypeValuesTest failed");
		}
	}

	/*
	 * @testName: bindableTypeValueOfTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1226
	 * 
	 * @test_Strategy:
	 *
	 */
		public void bindableTypeValueOfTest() throws Exception {
		boolean pass = true;

		try {

			BindableType bt = BindableType.valueOf(BindableType.ENTITY_TYPE.name());
			if (!bt.equals(BindableType.ENTITY_TYPE)) {
				logErr( "expected:" + BindableType.ENTITY_TYPE.name() + ", actual:" + bt.name());
				pass = false;
			}
			bt = BindableType.valueOf(BindableType.PLURAL_ATTRIBUTE.name());
			if (!bt.equals(BindableType.PLURAL_ATTRIBUTE)) {
				logErr(
						"expected:" + BindableType.PLURAL_ATTRIBUTE.name() + ", actual:" + bt.name());
				pass = false;
			}
			bt = BindableType.valueOf(BindableType.SINGULAR_ATTRIBUTE.name());
			if (!bt.equals(BindableType.SINGULAR_ATTRIBUTE)) {
				logErr(
						"expected:" + BindableType.SINGULAR_ATTRIBUTE.name() + ", actual:" + bt.name());
				pass = false;
			}
			try {
				BindableType.valueOf("DOESNOTEXIST");
				logErr( "IllegalArgumentException was not thrown");
				pass = false;
			} catch (IllegalArgumentException iae) {
				logTrace( "Received expected IllegalArgumentException");
			}
			try {
				BindableType.valueOf(null);
				logErr( "NullPointerException was not thrown");
				pass = false;
			} catch (NullPointerException iae) {
				logTrace( "Received expected NullPointerException");
			}
		} catch (Exception e) {
			pass = false;
			logErr( "Unexpected exception occurred", e);
		}

		if (!pass) {
			throw new Exception("bindableTypeValueOfTest failed");
		}
	}

	/*
	 * @testName: collectionTypeValuesTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1454
	 * 
	 * @test_Strategy:
	 *
	 */
		public void collectionTypeValuesTest() throws Exception {
		boolean pass = true;

		Collection<CollectionType> ct = Arrays.asList(CollectionType.values());

		if (ct.size() != 4) {
			logErr( "Number of TemporalType expected:4, actual:" + ct.size());
			pass = false;
		}

		if (ct.contains(CollectionType.COLLECTION)) {
			logTrace( "received:" + CollectionType.COLLECTION);
		} else {
			logErr( "Expected value:" + CollectionType.COLLECTION);
			pass = false;
		}
		if (ct.contains(CollectionType.LIST)) {
			logTrace( "received:" + CollectionType.LIST);
		} else {
			logErr( "Expected value:" + CollectionType.LIST);
			pass = false;
		}
		if (ct.contains(CollectionType.MAP)) {
			logTrace( "received:" + CollectionType.MAP);
		} else {
			logErr( "Expected value:" + CollectionType.MAP);
			pass = false;
		}
		if (ct.contains(CollectionType.SET)) {
			logTrace( "received:" + CollectionType.SET);
		} else {
			logErr( "Expected value:" + CollectionType.SET);
			pass = false;
		}

		for (CollectionType c : ct) {
			try {
				CollectionType.valueOf(c.name());
			} catch (Exception e) {
				logErr( "Received exception for valueOf", e);
				pass = false;
			}
		}

		if (!pass) {
			throw new Exception("collectionTypeValuesTest failed");
		}
	}

	/*
	 * @testName: collectionTypeValueOfTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1453
	 * 
	 * @test_Strategy:
	 *
	 */
		public void collectionTypeValueOfTest() throws Exception {
		boolean pass = true;

		try {

			CollectionType ct = CollectionType.valueOf(CollectionType.COLLECTION.name());
			if (!ct.equals(CollectionType.COLLECTION)) {
				logErr(
						"expected:" + CollectionType.COLLECTION.name() + ", actual:" + ct.name());
				pass = false;
			}
			ct = CollectionType.valueOf(CollectionType.LIST.name());
			if (!ct.equals(CollectionType.LIST)) {
				logErr( "expected:" + CollectionType.LIST.name() + ", actual:" + ct.name());
				pass = false;
			}
			ct = CollectionType.valueOf(CollectionType.MAP.name());
			if (!ct.equals(CollectionType.MAP)) {
				logErr( "expected:" + CollectionType.MAP.name() + ", actual:" + ct.name());
				pass = false;
			}
			ct = CollectionType.valueOf(CollectionType.SET.name());
			if (!ct.equals(CollectionType.SET)) {
				logErr( "expected:" + CollectionType.SET.name() + ", actual:" + ct.name());
				pass = false;
			}
			try {
				CollectionType.valueOf("DOESNOTEXIST");
				logErr( "IllegalArgumentException was not thrown");
				pass = false;
			} catch (IllegalArgumentException iae) {
				logTrace( "Received expected IllegalArgumentException");
			}
			try {
				CollectionType.valueOf(null);
				logErr( "NullPointerException was not thrown");
				pass = false;
			} catch (NullPointerException iae) {
				logTrace( "Received expected NullPointerException");
			}
		} catch (Exception e) {
			pass = false;
			logErr( "Unexpected exception occurred", e);
		}

		if (!pass) {
			throw new Exception("collectionTypeValueOfTest failed");
		}
	}

	/*
	 * @testName: persistenceTypeValuesTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1474
	 * 
	 * @test_Strategy:
	 *
	 */
		public void persistenceTypeValuesTest() throws Exception {
		boolean pass = true;

		Collection<PersistenceType> pt = Arrays.asList(PersistenceType.values());

		if (pt.size() != 4) {
			logErr( "Number of TemporalType expected:4, actual:" + pt.size());
			pass = false;
		}
		if (pt.contains(PersistenceType.BASIC)) {
			logTrace( "received:" + PersistenceType.BASIC);
		} else {
			logErr( "Expected value:" + PersistenceType.BASIC);
			pass = false;
		}
		if (pt.contains(PersistenceType.EMBEDDABLE)) {
			logTrace( "received:" + PersistenceType.EMBEDDABLE);
		} else {
			logErr( "Expected value:" + PersistenceType.EMBEDDABLE);
			pass = false;
		}
		if (pt.contains(PersistenceType.ENTITY)) {
			logTrace( "received:" + PersistenceType.ENTITY);
		} else {
			logErr( "Expected value:" + PersistenceType.ENTITY);
			pass = false;
		}
		if (pt.contains(PersistenceType.MAPPED_SUPERCLASS)) {
			logTrace( "received:" + PersistenceType.MAPPED_SUPERCLASS);
		} else {
			logErr( "Expected value:" + PersistenceType.MAPPED_SUPERCLASS);
			pass = false;
		}

		for (PersistenceType p : pt) {
			try {
				PersistenceType.valueOf(p.name());
			} catch (Exception e) {
				logErr( "Received exception for valueOf", e);
				pass = false;
			}
		}

		if (!pass) {
			throw new Exception("persistenceTypeValuesTest failed");
		}
	}

	/*
	 * @testName: persistenceTypeValueOfTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1473
	 * 
	 * @test_Strategy:
	 *
	 */
		public void persistenceTypeValueOfTest() throws Exception {
		boolean pass = true;

		try {

			PersistenceType pt = PersistenceType.valueOf(PersistenceType.BASIC.name());
			if (!pt.equals(PersistenceType.BASIC)) {
				logErr( "expected:" + PersistenceType.BASIC.name() + ", actual:" + pt.name());
				pass = false;
			}
			pt = PersistenceType.valueOf(PersistenceType.EMBEDDABLE.name());
			if (!pt.equals(PersistenceType.EMBEDDABLE)) {
				logErr(
						"expected:" + PersistenceType.EMBEDDABLE.name() + ", actual:" + pt.name());
				pass = false;
			}
			pt = PersistenceType.valueOf(PersistenceType.ENTITY.name());
			if (!pt.equals(PersistenceType.ENTITY)) {
				logErr( "expected:" + PersistenceType.ENTITY.name() + ", actual:" + pt.name());
				pass = false;
			}
			pt = PersistenceType.valueOf(PersistenceType.MAPPED_SUPERCLASS.name());
			if (!pt.equals(PersistenceType.MAPPED_SUPERCLASS)) {
				logErr(
						"expected:" + PersistenceType.MAPPED_SUPERCLASS.name() + ", actual:" + pt.name());
				pass = false;
			}
			try {
				PersistenceType.valueOf("DOESNOTEXIST");
				logErr( "IllegalArgumentException was not thrown");
				pass = false;
			} catch (IllegalArgumentException iae) {
				logTrace( "Received expected IllegalArgumentException");
			}
			try {
				PersistenceType.valueOf(null);
				logErr( "NullPointerException was not thrown");
				pass = false;
			} catch (NullPointerException iae) {
				logTrace( "Received expected NullPointerException");
			}
		} catch (Exception e) {
			pass = false;
			logErr( "Unexpected exception occurred", e);
		}

		if (!pass) {
			throw new Exception("persistenceTypeValueOfTest failed");
		}
	}

	/*
	 * @testName: loadStateValuesTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1478
	 * 
	 * @test_Strategy:
	 *
	 */
		public void loadStateValuesTest() throws Exception {
		boolean pass = true;

		Collection<LoadState> ls = Arrays.asList(LoadState.values());

		if (ls.size() != 3) {
			logErr( "Number of TemporalType expected:3, actual:" + ls.size());
			pass = false;
		}

		if (ls.contains(LoadState.LOADED)) {
			logTrace( "received:" + LoadState.LOADED);
		} else {
			logErr( "Expected value:" + LoadState.LOADED);
			pass = false;
		}
		if (ls.contains(LoadState.NOT_LOADED)) {
			logTrace( "received:" + LoadState.NOT_LOADED);
		} else {
			logErr( "Expected value:" + LoadState.NOT_LOADED);
			pass = false;
		}
		if (ls.contains(LoadState.UNKNOWN)) {
			logTrace( "received:" + LoadState.UNKNOWN);
		} else {
			logErr( "Expected value:" + LoadState.UNKNOWN);
			pass = false;
		}

		for (LoadState l : ls) {
			try {
				LoadState.valueOf(l.name());
			} catch (Exception e) {
				logErr( "Received exception for valueOf", e);
				pass = false;
			}
		}

		if (!pass) {
			throw new Exception("loadStateValuesTest failed");
		}
	}

	/*
	 * @testName: loadStateValueOfTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1477
	 * 
	 * @test_Strategy:
	 *
	 */
		public void loadStateValueOfTest() throws Exception {
		boolean pass = true;

		try {

			LoadState ls = LoadState.valueOf(LoadState.LOADED.name());
			if (!ls.equals(LoadState.LOADED)) {
				logErr( "expected:" + LoadState.LOADED.name() + ", actual:" + ls.name());
				pass = false;
			}
			ls = LoadState.valueOf(LoadState.NOT_LOADED.name());
			if (!ls.equals(LoadState.NOT_LOADED)) {
				logErr( "expected:" + LoadState.NOT_LOADED.name() + ", actual:" + ls.name());
				pass = false;
			}
			ls = LoadState.valueOf(LoadState.UNKNOWN.name());
			if (!ls.equals(LoadState.UNKNOWN)) {
				logErr( "expected:" + LoadState.UNKNOWN.name() + ", actual:" + ls.name());
				pass = false;
			}
			try {
				LoadState.valueOf("DOESNOTEXIST");
				logErr( "IllegalArgumentException was not thrown");
				pass = false;
			} catch (IllegalArgumentException iae) {
				logTrace( "Received expected IllegalArgumentException");
			}
			try {
				LoadState.valueOf(null);
				logErr( "NullPointerException was not thrown");
				pass = false;
			} catch (NullPointerException iae) {
				logTrace( "Received expected NullPointerException");
			}
		} catch (Exception e) {
			pass = false;
			logErr( "Unexpected Exception occurred", e);
		}

		if (!pass) {
			throw new Exception("loadStateValueOfTest failed");
		}
	}

	/*
	 * @testName: persistenceUnitTransactionTypeValuesTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1505; PERSISTENCE:SPEC:1909;
	 * 
	 * @test_Strategy:
	 *
	 */
		public void persistenceUnitTransactionTypeValuesTest() throws Exception {
		boolean pass = true;

		Collection<PersistenceUnitTransactionType> putt = Arrays.asList(PersistenceUnitTransactionType.values());

		if (putt.size() != 2) {
			logErr( "Number of TemporalType expected:2, actual:" + putt.size());
			pass = false;
		}

		if (putt.contains(PersistenceUnitTransactionType.JTA)) {
			logTrace( "received:" + PersistenceUnitTransactionType.JTA);
		} else {
			logErr( "Expected value:" + PersistenceUnitTransactionType.JTA);
			pass = false;
		}
		if (putt.contains(PersistenceUnitTransactionType.RESOURCE_LOCAL)) {
			logTrace( "received:" + PersistenceUnitTransactionType.RESOURCE_LOCAL);
		} else {
			logErr( "Expected value:" + PersistenceUnitTransactionType.RESOURCE_LOCAL);
			pass = false;
		}

		for (PersistenceUnitTransactionType p : putt) {
			try {
				PersistenceUnitTransactionType.valueOf(p.name());
			} catch (Exception e) {
				logErr( "Received exception for valueOf", e);
				pass = false;
			}
		}

		if (!pass) {
			throw new Exception("persistenceUnitTransactionTypeValuesTest failed");
		}
	}

	/*
	 * @testName: persistenceUnitTransactionTypeValueOfTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1504
	 * 
	 * @test_Strategy:
	 *
	 */
		public void persistenceUnitTransactionTypeValueOfTest() throws Exception {
		boolean pass = true;

		try {

			PersistenceUnitTransactionType putt = PersistenceUnitTransactionType
					.valueOf(PersistenceUnitTransactionType.JTA.name());
			if (!putt.equals(PersistenceUnitTransactionType.JTA)) {
				logErr(
						"expected:" + PersistenceUnitTransactionType.JTA.name() + ", actual:" + putt.name());
				pass = false;
			}
			putt = PersistenceUnitTransactionType.valueOf(PersistenceUnitTransactionType.RESOURCE_LOCAL.name());
			if (!putt.equals(PersistenceUnitTransactionType.RESOURCE_LOCAL)) {
				logErr(
						"expected:" + PersistenceUnitTransactionType.RESOURCE_LOCAL.name() + ", actual:" + putt.name());
				pass = false;
			}
			try {
				PersistenceUnitTransactionType.valueOf("DOESNOTEXIST");
				logErr( "IllegalArgumentException was not thrown");
				pass = false;
			} catch (IllegalArgumentException iae) {
				logTrace( "Received expected IllegalArgumentException");
			}
			try {
				PersistenceUnitTransactionType.valueOf(null);
				logErr( "NullPointerException was not thrown");
				pass = false;
			} catch (NullPointerException iae) {
				logTrace( "Received expected NullPointerException");
			}
		} catch (Exception e) {
			pass = false;
			logErr( "Unexpected Exception occurred", e);
		}

		if (!pass) {
			throw new Exception("persistenceUnitTransactionTypeValueOfTest failed");
		}
	}

	/*
	 * @testName: parameterModeValuesTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1538
	 * 
	 * @test_Strategy:
	 *
	 */
		public void parameterModeValuesTest() throws Exception {
		boolean pass = true;

		Collection<ParameterMode> cpm = Arrays.asList(ParameterMode.values());

		if (cpm.size() != 4) {
			logErr( "Number of TemporalType expected:4, actual:" + cpm.size());
			pass = false;
		}

		if (cpm.contains(ParameterMode.IN)) {
			logTrace( "received:" + ParameterMode.IN);
		} else {
			logErr( "Expected value:" + ParameterMode.IN);
			pass = false;
		}
		if (cpm.contains(ParameterMode.INOUT)) {
			logTrace( "received:" + ParameterMode.INOUT);
		} else {
			logErr( "Expected value:" + ParameterMode.INOUT);
			pass = false;
		}
		if (cpm.contains(ParameterMode.OUT)) {
			logTrace( "received:" + ParameterMode.OUT);
		} else {
			logErr( "Expected value:" + ParameterMode.OUT);
			pass = false;
		}
		if (cpm.contains(ParameterMode.REF_CURSOR)) {
			logTrace( "received:" + ParameterMode.REF_CURSOR);
		} else {
			logErr( "Expected value:" + ParameterMode.REF_CURSOR);
			pass = false;
		}

		for (ParameterMode pm : cpm) {
			try {
				ParameterMode.valueOf(pm.name());
			} catch (Exception e) {
				logErr( "Received Exception for valueOf", e);
				pass = false;
			}
		}

		if (!pass) {
			throw new Exception("parameterModeValuesTest failed");
		}
	}

	/*
	 * @testName: parameterModeValueOfTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1537
	 * 
	 * @test_Strategy:
	 *
	 */
		public void parameterModeValueOfTest() throws Exception {
		boolean pass = true;

		try {

			ParameterMode pm = ParameterMode.valueOf(ParameterMode.IN.name());
			if (!pm.equals(ParameterMode.IN)) {
				logErr( "expected:" + ParameterMode.IN.name() + ", actual:" + pm.name());
				pass = false;
			}
			pm = ParameterMode.valueOf(ParameterMode.INOUT.name());
			if (!pm.equals(ParameterMode.INOUT)) {
				logErr( "expected:" + ParameterMode.INOUT.name() + ", actual:" + pm.name());
				pass = false;
			}
			pm = ParameterMode.valueOf(ParameterMode.OUT.name());
			if (!pm.equals(ParameterMode.OUT)) {
				logErr( "expected:" + ParameterMode.OUT.name() + ", actual:" + pm.name());
				pass = false;
			}
			pm = ParameterMode.valueOf(ParameterMode.REF_CURSOR.name());
			if (!pm.equals(ParameterMode.REF_CURSOR)) {
				logErr( "expected:" + ParameterMode.REF_CURSOR.name() + ", actual:" + pm.name());
				pass = false;
			}
			try {
				ParameterMode.valueOf("DOESNOTEXIST");
				logErr( "IllegalArgumentException was not thrown");
				pass = false;
			} catch (IllegalArgumentException iae) {
				logTrace( "Received expected IllegalArgumentException");
			}
			try {
				ParameterMode.valueOf(null);
				logErr( "NullPointerException was not thrown");
				pass = false;
			} catch (NullPointerException npe) {
				logTrace( "Received expected NullPointerException");
			}
		} catch (Exception e) {
			pass = false;
			logErr( "Unexpected Exception occurred", e);
		}

		if (!pass) {
			throw new Exception("parameterModeValueOfTest failed");
		}
	}

	/*
	 * @testName: synchronizationTypeValueOfTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1660;
	 * 
	 * @test_Strategy:
	 *
	 */
		public void synchronizationTypeValueOfTest() throws Exception {
		boolean pass = true;

		try {

			SynchronizationType st = SynchronizationType.valueOf(SynchronizationType.SYNCHRONIZED.name());
			if (!st.equals(SynchronizationType.SYNCHRONIZED)) {
				logErr(
						"expected:" + SynchronizationType.SYNCHRONIZED.name() + ", actual:" + st.name());
				pass = false;
			}
			st = SynchronizationType.valueOf(SynchronizationType.UNSYNCHRONIZED.name());
			if (!st.equals(SynchronizationType.UNSYNCHRONIZED)) {
				logErr(
						"expected:" + SynchronizationType.UNSYNCHRONIZED.name() + ", actual:" + st.name());
				pass = false;
			}
			try {
				SynchronizationType.valueOf("DOESNOTEXIST");
				logErr( "IllegalArgumentException was not thrown");
				pass = false;
			} catch (IllegalArgumentException iae) {
				logTrace( "Received expected IllegalArgumentException");
			}
			try {
				SynchronizationType.valueOf(null);
				logErr( "NullPointerException was not thrown");
				pass = false;
			} catch (NullPointerException iae) {
				logTrace( "Received expected NullPointerException");
			}
		} catch (Exception e) {
			logErr( "Unexpected exception occurred", e);
			pass = false;
		}

		if (!pass) {
			throw new Exception("synchronizationTypeValueOfTest failed");
		}
	}

	/*
	 * @testName: synchronizationTypeValuesTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1661;
	 * 
	 * @test_Strategy:
	 *
	 */
		public void synchronizationTypeValuesTest() throws Exception {
		boolean pass = true;

		try {

			Collection<SynchronizationType> st = Arrays.asList(SynchronizationType.values());
			if (st.size() != 2) {
				logErr( "Number of SynchronizationType expected:2, actual:" + st.size());
				pass = false;
			}

			if (st.contains(SynchronizationType.SYNCHRONIZED)) {
				logTrace( "received:" + SynchronizationType.SYNCHRONIZED);
			} else {
				logErr( "Expected value:" + SynchronizationType.SYNCHRONIZED);
				pass = false;
			}
			if (st.contains(SynchronizationType.UNSYNCHRONIZED)) {
				logTrace( "received:" + SynchronizationType.UNSYNCHRONIZED);
			} else {
				logErr( "Expected value:" + SynchronizationType.UNSYNCHRONIZED);
				pass = false;
			}

			for (SynchronizationType s : st) {
				try {
					SynchronizationType.valueOf(s.name());
				} catch (Exception e) {
					logErr( "Received exception for valueOf", e);
					pass = false;
				}
			}
		} catch (Exception e) {
			logErr( "Unexpected exception occurred", e);
			pass = false;
		}

		if (!pass) {
			throw new Exception("synchronizationTypeValuesTest failed");
		}
	}


	public void cleanup() throws Exception {
		try {
			logTrace( "Cleanup data");
			if (getEntityManager().isOpen()) {
				removeTestData();
			}
			logTrace( "cleanup complete, calling super.cleanup");
			super.cleanup();
		} finally {

        }
	}

	private void removeTestData() {
		logTrace( "removeTestData");
		if (getEntityTransaction().isActive()) {
			getEntityTransaction().rollback();
		}
		try {
			getEntityTransaction().begin();
			getEntityManager().createNativeQuery("DELETE FROM PURCHASE_ORDER").executeUpdate();
			getEntityTransaction().commit();
		} catch (Exception e) {
			logErr( "Exception encountered while removing entities:", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logErr( "Unexpected Exception in removeTestData:", re);
			}
		}
	}

}
