/*
 * Copyright (c) 2008, 2023 Oracle and/or its affiliates. All rights reserved.
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

package ee.jakarta.tck.persistence.core.annotations.access.property;


import java.util.Arrays;
import java.util.Properties;
import com.sun.ts.lib.harness.Status;





import ee.jakarta.tck.persistence.core.types.common.Grade;

public class Client1 extends Client {

	public static void main(String[] args) {
		Client1 theTests = new Client1();
		Status s = theTests.run(args, System.out, System.err);
		s.exit();
	  }

	public Client1() {
	}


	public void setup(String[] args, Properties p) throws Exception {
		logTrace( "setup");
		try {

			super.setup(args,p);
			
			removeTestData();
			createTestData();
			logTrace( "Done creating test data");

		} catch (Exception e) {
			logErr( "Exception: ", e);
			throw new Exception("Setup failed:", e);
		}
	}

	/*
	 * @testName: propertyTypeTest1
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:514; PERSISTENCE:SPEC:515;
	 * PERSISTENCE:SPEC:516; PERSISTENCE:SPEC:524; PERSISTENCE:SPEC:527;
	 * PERSISTENCE:SPEC:534; PERSISTENCE:SPEC:1327.4; PERSISTENCE:SPEC:1153;
	 * 
	 * @test_Strategy: The persistent property of an entity may be of the following
	 * type: wrappers of the primitive types: Character
	 */
		public void propertyTypeTest1() throws Exception {

		boolean pass = false;
		final Character newChar = 'b';

		try {
			getEntityTransaction().begin();
			clearCache();
			d1 = null;
			d1 = getEntityManager().find(DataTypes.class, 1);

			if ((null != d1) && (d1.getCharacterData().equals((char) 'a'))) {
				d1.setCharacterData(newChar);

				getEntityManager().merge(d1);
				getEntityManager().flush();

				if (d1.getCharacterData().equals(newChar)) {
					pass = true;
				}

				getEntityTransaction().commit();
			} else {
				logErr( "find returned null");
			}
		} catch (Exception e) {
			logErr( "Unexpected exception occurred", e);
			pass = false;
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logErr( "Unexpected Exception during Rollback:", re);
			}
		}

		if (!pass)
			throw new Exception("propertyTypeTest1 failed");
	}

	/*
	 * @testName: propertyTypeTest2
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:524; PERSISTENCE:SPEC:527;
	 * PERSISTENCE:SPEC:533; PERSISTENCE:SPEC:534
	 * 
	 * @test_Strategy: The persistent property of an entity may be of the following
	 * type: wrappers of the primitive types: Short
	 */
		public void propertyTypeTest2() throws Exception {

		boolean pass = false;
		final Short newShort = 101;

		try {
			getEntityTransaction().begin();
			clearCache();
			d1 = null;
			d1 = getEntityManager().find(DataTypes.class, 1);

			if ((null != d1) && (d1.getShortData().equals((short) 100))) {
				d1.setShortData(newShort);

				getEntityManager().merge(d1);
				getEntityManager().flush();

				if (d1.getShortData().equals(newShort)) {
					pass = true;
				}

				getEntityTransaction().commit();
			} else {
				logErr( "find returned null");
			}
		} catch (Exception e) {
			logErr( "Unexpected exception occurred", e);
			pass = false;
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logErr( "Unexpected Exception during Rollback:", re);
			}
		}

		if (!pass)
			throw new Exception("propertyTypeTest2 failed");
	}

	/*
	 * @testName: propertyTypeTest3
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:524; PERSISTENCE:SPEC:527
	 * 
	 * @test_Strategy: The persistent property of an entity may be of the following
	 * type: wrappers of the primitive types: Integer
	 */
		public void propertyTypeTest3() throws Exception {

		boolean pass = false;
		final Integer newInt = 500;

		try {
			getEntityTransaction().begin();
			clearCache();
			d1 = null;
			d1 = getEntityManager().find(DataTypes.class, 1);

			if ((null != d1) && (d1.getIntegerData().equals(500))) {
				d1.setIntegerData(newInt);

				getEntityManager().merge(d1);
				getEntityManager().flush();

				if (d1.getIntegerData().equals(newInt)) {
					pass = true;
				}
				getEntityTransaction().commit();
			} else {
				logErr( "find returned null");
			}
		} catch (Exception e) {
			logErr( "Unexpected exception occurred", e);
			pass = false;
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logErr( "Unexpected Exception during Rollback:", re);
			}
		}

		if (!pass)
			throw new Exception("propertyTypeTest3 failed");
	}

	/*
	 * @testName: propertyTypeTest4
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:524; PERSISTENCE:SPEC:527
	 * 
	 * @test_Strategy: The persistent property of an entity may be of the following
	 * type: wrappers of the primitive types: Long
	 */
		public void propertyTypeTest4() throws Exception {

		boolean pass = false;
		final Long newLong = 600L;

		try {
			getEntityTransaction().begin();
			clearCache();
			d1 = null;
			d1 = getEntityManager().find(DataTypes.class, 1);

			if ((null != d1) && (d1.getLongData().equals(300L))) {
				d1.setLongData(newLong);

				getEntityManager().merge(d1);
				getEntityManager().flush();

				if (d1.getLongData().equals(newLong)) {
					pass = true;
				}

				getEntityTransaction().commit();
			} else {
				logErr( "find returned null");
			}
		} catch (Exception e) {
			logErr( "Unexpected exception occurred", e);
			pass = false;
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logErr( "Unexpected Exception during Rollback:", re);
			}
		}

		if (!pass)
			throw new Exception("propertyTypeTest4 failed");
	}

	/*
	 * @testName: propertyTypeTest5
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:524; PERSISTENCE:SPEC:527
	 * 
	 * @test_Strategy: The persistent property of an entity may be of the following
	 * type: wrappers of the primitive types: Double
	 */
		public void propertyTypeTest5() throws Exception {

		boolean pass = false;
		final Double newDbl = 80D;

		try {
			getEntityTransaction().begin();
			clearCache();
			d1 = null;
			d1 = getEntityManager().find(DataTypes.class, 1);

			if ((null != d1) && (d1.getDoubleData().equals(50D))) {
				d1.setDoubleData(newDbl);

				getEntityManager().merge(d1);
				getEntityManager().flush();

				if (d1.getDoubleData().equals(newDbl)) {
					pass = true;
				}

				getEntityTransaction().commit();
			} else {
				logErr( "find returned null");
			}
		} catch (Exception e) {
			logErr( "Unexpected exception occurred", e);
			pass = false;
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logErr( "Unexpected Exception during Rollback:", re);
			}
		}

		if (!pass)
			throw new Exception("propertyTypeTest5 failed");

	}

	/*
	 * @testName: propertyTypeTest6
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:524; PERSISTENCE:SPEC:527
	 * 
	 * @test_Strategy: The persistent property of an entity may be of the following
	 * type: wrappers of the primitive types: Float
	 */
		public void propertyTypeTest6() throws Exception {

		boolean pass = false;
		final Float newFloat = 3.0F;

		try {
			getEntityTransaction().begin();
			clearCache();
			d1 = null;
			d1 = getEntityManager().find(DataTypes.class, 1);

			if ((null != d1) && (d1.getFloatData().equals(1.0F))) {
				d1.setFloatData(newFloat);

				getEntityManager().merge(d1);
				getEntityManager().flush();

				if (d1.getFloatData().equals(newFloat)) {
					pass = true;
				}

				getEntityTransaction().commit();
			} else {
				logErr( "find returned null");
			}
		} catch (Exception e) {
			logErr( "Unexpected exception occurred", e);
			pass = false;
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logErr( "Unexpected Exception during Rollback:", re);
			}
		}

		if (!pass)
			throw new Exception("propertyTypeTest6 failed");

	}

	/*
	 * @testName: propertyTypeTest7
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:524; PERSISTENCE:SPEC:529;
	 * PERSISTENCE:SPEC:1090.1; PERSISTENCE:SPEC:1090.2; PERSISTENCE:JAVADOC:72;
	 * PERSISTENCE:JAVADOC:73
	 * 
	 * @test_Strategy: The persistent property of an entity may be of the following
	 * type: enums
	 *
	 * Using the Enumerated annotation, with EnumType.ORDINAL.
	 */
		public void propertyTypeTest7() throws Exception {

		boolean pass = false;

		try {
			getEntityTransaction().begin();
			clearCache();
			d1 = null;
			d1 = getEntityManager().find(DataTypes.class, 1);

			if (null != d1) {
				logTrace( "DataType Entity is not null, setting enumData ");
				d1.setEnumData(Grade.C);

				getEntityManager().merge(d1);
				getEntityManager().flush();

				logTrace( "Check results");
				if ((null != d1) && (d1.getEnumData().equals(Grade.C))) {
					logTrace( "Expected Grade of:" + d1.getEnumData() + "received");
					pass = true;
				} else {
					logErr(
							"Did not get expected results.  Expected C, got: " + d1.getEnumData());
				}

				getEntityTransaction().commit();
			} else {
				logErr( "find returned null");
			}
		} catch (Exception e) {
			logErr( "Unexpected exception occurred", e);
			pass = false;
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logErr( "Unexpected Exception during Rollback:", re);
			}
		}

		if (!pass)
			throw new Exception("propertyTypeTest7 failed");
	}

	/*
	 * @testName: propertyTypeTest8
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:536; PERSISTENCE:SPEC:540;
	 * PERSISTENCE:SPEC:550; PERSISTENCE:SPEC:1090.0; PERSISTENCE:SPEC:1079
	 * 
	 * @test_Strategy: The primary key should be one of the following types:
	 * java.util.Date
	 * 
	 * The application must not change the value of the primary key. The behavior is
	 * undefined if this occurs.
	 *
	 * Temporal.TemporalType.DATE
	 */
		public void propertyTypeTest8() throws Exception {

		boolean pass = false;

		try {
			getEntityTransaction().begin();
			clearCache();
			d2 = null;
			d2 = getEntityManager().find(DataTypes2.class, dateId);
			if (null != d2) {

				logTrace( "Check results");
				if (d2.getId().equals(dateId)) {
					logTrace( "Got expected PK of:" + d2.getId() + "received");
					pass = true;
				} else {
					logErr(
							"Did not get expected results. " + "Expected " + dateId + ", got: " + d2.getId());
				}

				getEntityTransaction().commit();
			} else {
				logErr( "find returned null");
			}
		} catch (Exception e) {
			logErr( "Unexpected exception occurred", e);
			pass = false;
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logErr( "Unexpected Exception during Rollback:", re);
			}
		}

		if (!pass)
			throw new Exception("propertyTypeTest8 failed");
	}

	/*
	 * @testName: propertyTypeTest9
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:524; PERSISTENCE:SPEC:528
	 * 
	 * @test_Strategy: The persistent property of an entity may be of the following
	 * type: byte[]
	 *
	 */
		public void propertyTypeTest9() throws Exception {

		boolean pass = false;
		final byte[] b = { 31, 32, 33, 63, 64, 65 };
		final byte bv = 5;
		byte[] a = null;

		try {
			getEntityTransaction().begin();
			clearCache();
			d1 = null;
			d1 = getEntityManager().find(DataTypes.class, 1);

			if (null != d1) {
				logTrace( "DataType Entity is not null, setting byteData ");
				d1.setByteArrayData(b);
				a = d1.getByteArrayData();
				a[0] = (byte) (a[0] + bv);
				d1.setByteArrayData(b);

				getEntityManager().merge(d1);
				getEntityManager().flush();

				logTrace( "Check results");
				if ((null != d1) && (Arrays.equals(d1.getByteArrayData(), a))) {
					logTrace( "propertyTypeTest9: Expected results received");
					pass = true;
				} else {
					logErr( "Unexpected result in array comparison.");
					for (byte aByte : a) {
						logTrace( "Array a in propertyTest9 equals: " + aByte);
					}
					for (byte bByte : b) {
						logTrace( "Array b in propertyTest9 equals: " + bByte);
					}
					pass = false;
				}

				getEntityTransaction().commit();
			} else {
				logErr( "find returned null");
			}
		} catch (Exception e) {
			logErr( "Unexpected exception occurred", e);
			pass = false;
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logErr( "Unexpected Exception during Rollback:", re);
			}
		}

		if (!pass)
			throw new Exception("propertyTypeTest9 failed");
	}

	/*
	 * @testName: propertyTypeTest10
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:524; PERSISTENCE:SPEC:528
	 * 
	 * @test_Strategy: The persistent property of an entity may be of the following
	 * type: char[]
	 *
	 */
		public void propertyTypeTest10() throws Exception {

		boolean pass = false;

		try {
			getEntityTransaction().begin();
			char[] charData = new char[] { (char) 'C', (char) 'T', (char) 'S' };
			clearCache();
			d1 = null;
			d1 = getEntityManager().find(DataTypes.class, 1);

			if (null != d1) {
				logTrace( "DataType Entity is not null, setting CharacterData ");
				d1.setCharArrayData(charData);

				getEntityManager().merge(d1);
				getEntityManager().flush();

				logTrace( "propertyTypeTest10:  Check results");
				if ((null != d1) && (Arrays.equals(d1.getCharArrayData(), charData))) {
					logTrace( "propertyTypeTest10: Expected Results Received");
					pass = true;
				} else {
					logErr( "Did not get expected results. " + "Expected "
							+ Arrays.toString(charData) + ", got: " + Arrays.toString(d1.getCharArrayData()));
				}

				getEntityTransaction().commit();
			} else {
				logErr( "find returned null");
			}
		} catch (Exception e) {
			logErr( "Unexpected exception occurred", e);
			pass = false;
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logErr( "Unexpected Exception during Rollback:", re);
			}
		}

		if (!pass)
			throw new Exception("propertyTypeTest10 failed");
	}

	/*
	 * @testName: propertyTypeTest11
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:524; PERSISTENCE:SPEC:527
	 * 
	 * @test_Strategy: The persistent property of an entity may be of the following
	 * type: java.sql.Time
	 */
		public void propertyTypeTest11() throws Exception {

		boolean pass = false;
		final java.sql.Time timeValue = getTimeData(18, 30, 15);

		try {
			getEntityTransaction().begin();
			clearCache();
			d2 = null;
			d2 = getEntityManager().find(DataTypes2.class, dateId);

			if (null != d2) {
				logTrace( "DataType Entity is not null, setting TimeData ");
				d2.setTimeData(timeValue);

				getEntityManager().merge(d2);
				getEntityManager().flush();

				logTrace( "propertyTypeTest11:  Check results");
				if ((null != d2) && (d2.getTimeData().equals(timeValue))) {
					logTrace( "propertyTypeTest11: Expected Time Received");
					pass = true;
				} else {
					logErr( "Did not get expected results. " + " Expected " + timeValue
							+ " , got: " + d2.getTimeData());
				}

				getEntityTransaction().commit();
			} else {
				logErr( "find returned null");
			}
		} catch (Exception e) {
			logErr( "Unexpected exception occurred", e);
			pass = false;
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logErr( "Unexpected Exception during Rollback:", re);
			}
		}

		if (!pass)
			throw new Exception("propertyTypeTest11 failed");
	}

	/*
	 * @testName: propertyTypeTest12
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:524; PERSISTENCE:SPEC:527
	 * 
	 * @test_Strategy: The persistent property of an entity may be of the following
	 * type: java.sql.Timestamp
	 */
		public void propertyTypeTest12() throws Exception {

		boolean pass = false;
		final java.sql.Timestamp tsValue = getTimestampData(2006, 02, 11);

		try {
			getEntityTransaction().begin();
			clearCache();
			d2 = null;
			d2 = getEntityManager().find(DataTypes2.class, dateId);

			if (null != d2) {
				logTrace( "DataType Entity is not null, setting TimestampData ");
				d2.setTsData(tsValue);

				getEntityManager().merge(d2);
				getEntityManager().flush();

				logTrace( "propertyTypeTest12:  Check results");
				if ((null != d2) && (d2.getTsData().equals(tsValue))) {
					logTrace( "propertyTypeTest12: Expected Timestamp Received");
					pass = true;
				} else {
					logErr(
							"Did not get expected results. " + " Expected " + tsValue + " , got: " + d2.getTsData());
				}

				getEntityTransaction().commit();
			} else {
				logErr( "find returned null");
			}
		} catch (Exception e) {
			logErr( "Unexpected exception occurred", e);
			pass = false;
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logErr( "Unexpected Exception during Rollback:", re);
			}
		}

		if (!pass)
			throw new Exception("propertyTypeTest12 failed");
	}

	/*
	 * @testName: propertyTypeTest13
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:524; PERSISTENCE:SPEC:527;
	 * 
	 * @test_Strategy: The persistent property of an entity may be of the following
	 * type: wrappers of the primitive types: Boolean
	 */
		public void propertyTypeTest13() throws Exception {

		boolean pass = false;
		final Boolean newBoolean = false;

		try {
			getEntityTransaction().begin();
			clearCache();
			d1 = null;
			d1 = getEntityManager().find(DataTypes.class, 1);

			if ((null != d1) && (d1.getBooleanData().equals(true))) {
				d1.setBooleanData(newBoolean);

				getEntityManager().merge(d1);
				getEntityManager().flush();

				if (d1.getBooleanData().equals(newBoolean)) {
					pass = true;
				}

				getEntityTransaction().commit();
			} else {
				logErr( "find returned null");
			}
		} catch (Exception e) {
			logErr( "Unexpected exception occurred", e);
			pass = false;
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logErr( "Unexpected Exception during Rollback:", re);
			}
		}

		if (!pass)
			throw new Exception("propertyTypeTest13 failed");
	}

	// Methods used for Tests

	public void createTestData() {
		logTrace( "createTestData");

		try {
			getEntityTransaction().begin();
			char[] cArray = { 'a' };
			byte[] bArray = { (byte) 100 };
			d1 = new DataTypes(1, true, 'a', (short) 100, 500, 300L, 50D, 1.0F, cArray, bArray);

			d2 = new DataTypes2(dateId);

			getEntityManager().persist(d1);
			getEntityManager().persist(d2);

			getEntityManager().flush();

			getEntityTransaction().commit();

		} catch (Exception e) {
			logErr( "Unexpected Exception in createTestData:", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logErr( "Unexpected Exception during Rollback:", re);
			}
		}

	}

}
