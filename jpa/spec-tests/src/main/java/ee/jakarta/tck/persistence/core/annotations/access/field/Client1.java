package ee.jakarta.tck.persistence.core.annotations.access.field;


import java.util.Arrays;
import java.util.Properties;





import ee.jakarta.tck.persistence.core.types.common.Grade;
import jakarta.persistence.Query;

public class Client1 extends Client {

	public void setup(String[] args, Properties p) throws Exception {

		logTrace( "setup");

		try {
			super.setup(args,p);
			
			removeTestData();
			createTestData();
			logTrace( "Done creating test data");
		} catch (Exception e) {
			logErr( "Unexpected exception occurred", e);
			throw new Exception("Setup failed:", e);
		}
	}

	/*
	 * @testName: fieldTypeTest1
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:511; PERSISTENCE:SPEC:524;
	 * PERSISTENCE:SPEC:534; PERSISTENCE:SPEC:512; PERSISTENCE:SPEC:525;
	 * PERSISTENCE:JAVADOC:14; PERSISTENCE:JAVADOC:203; PERSISTENCE:JAVADOC:300;
	 * PERSISTENCE:SPEC:1239; PERSISTENCE:SPEC:1320; PERSISTENCE:SPEC:1327.4;
	 * PERSISTENCE:SPEC:1155; PERSISTENCE:SPEC:1976; PERSISTENCE:SPEC:1977;
	 * 
	 * @test_Strategy: The persistent field of an entity may be of the following
	 * type: Java primitive types: boolean
	 */

		public void fieldTypeTest1() throws Exception {

		boolean pass = false;

		try {
			getEntityTransaction().begin();
			d1 = getEntityManager().find(DataTypes.class, 1);
			if ((null != d1) && (!d1.isProperty())) {
				d1.setBooleanData(true);

				getEntityManager().merge(d1);
				getEntityManager().flush();

				if (d1.isProperty()) {
					logTrace( "Received expected result:" + d1.isProperty());
					pass = true;
				} else {
					logErr( "Expected: true, actual:" + d1.isProperty());
				}

				getEntityTransaction().commit();
			} else {
				logErr( "find returned null");
			}
		} catch (Exception e) {
			logErr( "Unexpected exception occurred", e);
			e.printStackTrace();
			pass = false;
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logErr( "Unexpected Exception during Rollback:", re);
				re.printStackTrace();
			}
		}

		if (!pass)
			throw new Exception("fieldTypeTest1 failed");
	}

	/*
	 * @testName: fieldTypeTest2
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:524; PERSISTENCE:SPEC:525;
	 * PERSISTENCE:SPEC:513; PERSISTENCE:SPEC:1319
	 * 
	 * @test_Strategy: The persistent field of an entity may be of the following
	 * type: Java primitive types: byte
	 */
		public void fieldTypeTest2() throws Exception {

		boolean pass = false;
		byte newByte = (byte) 111;

		try {
			getEntityTransaction().begin();
			d1 = getEntityManager().find(DataTypes.class, 1);
			if ((null != d1) && (d1.getByteData() == (byte) 100)) {
				d1.setByteData(newByte);

				getEntityManager().merge(d1);
				getEntityManager().flush();

				if (d1.getByteData() == newByte) {
					pass = true;
				} else {
					logErr( "Expected: 100, actual:" + d1.getByteData());
				}

				getEntityTransaction().commit();
			} else {
				logErr( "find returned null");
			}
		} catch (Exception e) {
			logErr( "Unexpected exception occurred", e);
			e.printStackTrace();
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
			throw new Exception("fieldTypeTest2 failed");
	}

	/*
	 * @testName: fieldTypeTest3
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:524; PERSISTENCE:SPEC:525;
	 * PERSISTENCE:SPEC:1319
	 * 
	 * @test_Strategy: The persistent field of an entity may be of the following
	 * type: Java primitive types: char
	 */
		public void fieldTypeTest3() throws Exception {

		boolean pass = false;
		final char newChar = 'b';

		try {
			getEntityTransaction().begin();
			d1 = getEntityManager().find(DataTypes.class, 1);

			if ((null != d1) && (d1.getCharacterData() == ('a'))) {
				d1.setCharacterData(newChar);

				getEntityManager().merge(d1);
				getEntityManager().flush();

				if (d1.getCharacterData() == newChar) {
					pass = true;
				} else {
					logErr( "Expected: a, actual:" + d1.getCharacterData());
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
			throw new Exception("fieldTypeTest3 failed");
	}

	/*
	 * @testName: fieldTypeTest4
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:524; PERSISTENCE:SPEC:525;
	 * PERSISTENCE:SPEC:1319
	 * 
	 * @test_Strategy: The persistent field of an entity may be of the following
	 * type: Java primitive types: short
	 */
		public void fieldTypeTest4() throws Exception {

		boolean pass = false;
		final short newShort = (short) 101;

		try {

			getEntityTransaction().begin();
			d1 = getEntityManager().find(DataTypes.class, 1);
			if ((null != d1) && (d1.getShortData() == (short) 100)) {
				d1.setShortData(newShort);

				getEntityManager().merge(d1);
				getEntityManager().flush();

				if (d1.getShortData() == newShort) {
					pass = true;
				} else {
					logErr( "Expected: 100, actual:" + d1.getShortData());
				}
				getEntityTransaction().commit();
			} else {
				logErr( "find returned null");
			}
		} catch (Exception e) {
			logErr( "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logErr( "Unexpected Exception during Rollback:", re);
				pass = false;
			}
		}

		if (!pass)
			throw new Exception("fieldTypeTest4 failed");
	}

	/*
	 * @testName: fieldTypeTest5
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:524; PERSISTENCE:SPEC:525;
	 * PERSISTENCE:SPEC:1319
	 * 
	 * @test_Strategy: The persistent field of an entity may be of the following
	 * type: Java primitive types: int
	 */
		public void fieldTypeTest5() throws Exception {

		boolean pass = false;
		final int newInt = 500;

		try {
			getEntityTransaction().begin();
			d1 = getEntityManager().find(DataTypes.class, 1);
			if ((null != d1) && (d1.getIntData() == 300)) {
				d1.setIntData(newInt);

				getEntityManager().merge(d1);
				getEntityManager().flush();

				if (d1.getIntData() == newInt) {
					pass = true;
				} else {
					logErr( "Expected: 300, actual:" + d1.getIntData());
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
			throw new Exception("fieldTypeTest5 failed");
	}

	/*
	 * @testName: fieldTypeTest6
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:524; PERSISTENCE:SPEC:525;
	 * PERSISTENCE:SPEC:1319
	 * 
	 * @test_Strategy: The persistent field of an entity may be of the following
	 * type: Java primitive types: long
	 */
		public void fieldTypeTest6() throws Exception {

		boolean pass = false;
		final long newLong = 600L;

		try {
			getEntityTransaction().begin();
			d1 = getEntityManager().find(DataTypes.class, 1);
			if ((null != d1) && (d1.getLongData() == 600L)) {
				d1.setLongData(newLong);

				getEntityManager().merge(d1);
				getEntityManager().flush();

				if (d1.getLongData() == newLong) {
					pass = true;
				} else {
					logErr( "Expected: 600, actual:" + d1.getLongData());
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
			throw new Exception("fieldTypeTest6 failed");
	}

	/*
	 * @testName: fieldTypeTest7
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:524; PERSISTENCE:SPEC:525;
	 * PERSISTENCE:SPEC:1319
	 * 
	 * @test_Strategy: The persistent field of an entity may be of the following
	 * type: Java primitive types: double
	 */
		public void fieldTypeTest7() throws Exception {

		boolean pass = false;
		final double newDbl = 80D;

		try {
			getEntityTransaction().begin();
			d1 = getEntityManager().find(DataTypes.class, 1);
			if ((null != d1) && (d1.getDoubleData() == (50D))) {
				d1.setDoubleData(newDbl);

				getEntityManager().merge(d1);
				getEntityManager().flush();

				if (d1.getDoubleData() == newDbl) {
					pass = true;
				} else {
					logErr( "Expected: 50, actual:" + d1.getDoubleData());
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
			throw new Exception("fieldTypeTest7 failed");
	}

	/*
	 * @testName: fieldTypeTest8
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:524; PERSISTENCE:SPEC:525;
	 * PERSISTENCE:SPEC:1319
	 * 
	 * @test_Strategy: The persistent field of an entity may be of the following
	 * type: Java primitive types: float
	 */
		public void fieldTypeTest8() throws Exception {

		boolean pass = false;
		final float expFloat = 1.0F;
		final float floatRange = 2.0F;
		final float newFloat = 6.0F;
		final float newfloatRange = 7.0F;

		try {
			getEntityTransaction().begin();
			d1 = getEntityManager().find(DataTypes.class, 1);
			if (null != d1) {

				logTrace( "float value is: " + d1.getFloatData());

				if ((d1.getFloatData() >= expFloat) && (d1.getFloatData() < floatRange)) {
					d1.setFloatData(newFloat);

					getEntityManager().merge(d1);
					getEntityManager().flush();

					if ((d1.getFloatData() >= newFloat) && (d1.getFloatData() < newfloatRange)) {
						pass = true;
					} else {
						logErr( "Expected: >= " + newFloat + " and < " + newfloatRange
								+ ", actual:" + d1.getFloatData());
					}
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
			throw new Exception("fieldTypeTest8 failed");
	}

	/*
	 * @testName: fieldTypeTest9
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:524; PERSISTENCE:SPEC:529;
	 * PERSISTENCE:SPEC:556; PERSISTENCE:SPEC:1319
	 * 
	 * @test_Strategy: The persistent field of an entity may be of the following
	 * type: enums
	 *
	 * With the Enumerated annotation and EnumType.STRING.
	 */
		public void fieldTypeTest9() throws Exception {

		boolean pass = false;

		try {
			getEntityTransaction().begin();
			logTrace( "find DataTypes entity in fieldTypeTest9");
			d1 = getEntityManager().find(DataTypes.class, 1);

			if (null != d1) {
				logTrace( "DataTypes is not null, setting enumData");
				d1.setEnumData(Grade.B);

				getEntityManager().merge(d1);
				getEntityManager().flush();

				logTrace( "Update performed, check results");
				if ((null != d1) && (d1.getEnumData().equals(Grade.B))) {
					logTrace( "Expected results received");
					pass = true;
				} else {
					logErr( "Expected: " + Grade.B.toString() + ", actual:" + d1.getEnumData());
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
			throw new Exception("fieldTypeTest9 failed");
	}

	/*
	 * @testName: fieldTypeTest10
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:536; PERSISTENCE:SPEC:540;
	 * PERSISTENCE:SPEC:550; PERSISTENCE:SPEC:1090.0; PERSISTENCE:JAVADOC:216;
	 * PERSISTENCE:JAVADOC:217; PERSISTENCE:SPEC:1319
	 * 
	 * @test_Strategy: The primary key should be one of the following types:
	 * java.util.Date
	 *
	 * The application must not change the value of the primary key. The behavior is
	 * undefined if this occurs.
	 *
	 * Temporal.TemporalType.DATE
	 */
		public void fieldTypeTest10() throws Exception {

		boolean pass = false;

		try {
			getEntityTransaction().begin();
			logTrace( "FIND D2 IN fieldTypeTest10");
			d2 = getEntityManager().find(DataTypes2.class, dateId);
			if (null != d2) {

				logTrace( "fieldTypeTest10:  Check results");
				if (d2.getId().equals(dateId)) {
					logTrace( "Got expected PK of:" + d2.getId() + "received");
					pass = true;
				} else {
					logErr( "Expected: " + dateId + ", actual: " + d2.getId());
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
			throw new Exception("fieldTypeTest10 failed");
	}

	/*
	 * @testName: fieldTypeTest11
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:524; PERSISTENCE:SPEC:528;
	 * PERSISTENCE:SPEC:1089; PERSISTENCE:SPEC:1319
	 * 
	 * @test_Strategy: The persistent property of an entity may be of the following
	 * type: byte[]
	 *
	 */
		public void fieldTypeTest11() throws Exception {

		boolean pass = false;
		final byte[] b = { 31, 32, 33, 63, 64, 65 };
		final byte bv = 5;
		byte[] a = null;

		try {
			getEntityTransaction().begin();
			logTrace( "FIND D1 IN fieldTypeTest11");
			d1 = getEntityManager().find(DataTypes.class, 1);

			if (null != d1) {
				logTrace( "DataType Entity is not null, setting byteData ");
				d1.setByteArrayData(b);
				a = d1.getByteArrayData();
				a[0] = (byte) (a[0] + bv);
				d1.setByteArrayData(b);

				getEntityManager().merge(d2);
				getEntityManager().flush();

				logTrace( "fieldTypeTest11:  Check results");
				if ((null != d2) && (Arrays.equals(d1.getByteArrayData(), a))) {
					logTrace( "fieldTypeTest11: Expected results received");
					pass = true;
				} else {
					logErr( "Unexpected result in array comparison.");
					for (Byte aByte : a) {
						logErr( "Array a in propertyTest9 equals: " + aByte);
					}
					for (Byte bByte : b) {
						logErr( "Array b in propertyTest9 equals: " + bByte);
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
			throw new Exception("fieldTypeTest11 failed");
	}

	/*
	 * @testName: fieldTypeTest12
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:524; PERSISTENCE:SPEC:528;
	 * PERSISTENCE:SPEC:1319
	 * 
	 * @test_Strategy: The persistent property of an entity may be of the following
	 * type: char[]
	 *
	 */
		public void fieldTypeTest12() throws Exception {

		boolean pass = false;
		final char[] charData = new char[] { 'c', 't', 's' };

		try {
			getEntityTransaction().begin();
			logTrace( "FIND D1 IN fieldTypeTest12");
			d1 = getEntityManager().find(DataTypes.class, 1);

			if (null != d1) {
				logTrace( "DataType Entity is not null, setting charData ");
				d1.setCharArrayData(charData);

				getEntityManager().merge(d1);
				getEntityManager().flush();

				logTrace( "fieldTypeTest12:  Check results");
				if ((null != d1) && (Arrays.equals(d1.getCharArrayData(), charData))) {
					logTrace( "fieldTypeTest12: Expected Results Received");
					pass = true;
				} else {
					logErr( "Expected " + Arrays.toString(charData) + ", actual: "
							+ Arrays.toString(d1.getCharArrayData()));
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
			throw new Exception("fieldTypeTest12 failed");
	}

	/*
	 * @testName: fieldTypeTest13
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:524; PERSISTENCE:SPEC:527;
	 * PERSISTENCE:SPEC:1319
	 * 
	 * @test_Strategy: The persistent property of an entity may be of the following
	 * type: java.sql.Time
	 */
		public void fieldTypeTest13() throws Exception {

		boolean pass = false;
		final java.sql.Time timeValue = getTimeData(18, 30, 15);

		try {
			getEntityTransaction().begin();
			logTrace( "FIND D2 IN fieldTypeTest13");
			d2 = getEntityManager().find(DataTypes2.class, dateId);

			if (null != d2) {
				logTrace( "DataType Entity is not null, setting TimeData ");
				d2.setTimeData(timeValue);

				getEntityManager().merge(d2);
				getEntityManager().flush();

				logTrace( "fieldTypeTest13:  Check results");
				if ((null != d2) && (d2.getTimeData().equals(timeValue))) {
					logTrace( "fieldTypeTest13: Expected Time Received");
					pass = true;
				} else {
					logErr( "Expected " + timeValue + " , actual: " + d2.getTimeData());
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
			throw new Exception("fieldTypeTest13 failed");
	}

	/*
	 * @testName: fieldTypeTest14
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:524; PERSISTENCE:SPEC:527;
	 * PERSISTENCE:SPEC:1319
	 * 
	 * @test_Strategy: The persistent property of an entity may be of the following
	 * type: java.sql.Timestamp
	 */
		public void fieldTypeTest14() throws Exception {

		boolean pass = false;
		final java.sql.Timestamp tsValue = getTimestampData(2006, 02, 11);

		try {
			getEntityTransaction().begin();
			logTrace( "FIND D2 IN fieldTypeTest14");
			d2 = getEntityManager().find(DataTypes2.class, dateId);

			if (null != d2) {
				logTrace( "DataType Entity is not null, setting TimestampData ");
				d2.setTsData(tsValue);

				getEntityManager().merge(d2);
				getEntityManager().flush();

				logTrace( "fieldTypeTest14:  Check results");
				if ((null != d2) && (d2.getTsData().equals(tsValue))) {
					logTrace( "fieldTypeTest14: Expected Timestamp Received");
					pass = true;
				} else {
					logErr( "Expected " + tsValue + " , actual: " + d2.getTsData());
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
			throw new Exception("fieldTypeTest14 failed");
	}

	/*
	 * @testName: fieldTypeTest15
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:428; PERSISTENCE:SPEC:529;
	 * PERSISTENCE:SPEC:1090.1; PERSISTENCE:SPEC:1090.2; PERSISTENCE:SPEC:1319
	 * 
	 * @test_Strategy: enum_expression ::= enum_primary | (subquery) enum_primary
	 * ::= statefield_path_expression | input parameter | enum_literal
	 *
	 * statefield_path_expression
	 */
		public void fieldTypeTest15() throws Exception {

		boolean pass = false;
		Object result;
		Query q;

		try {

			getEntityTransaction().begin();
			d1 = getEntityManager().find(DataTypes.class, 1);

			if (null != d1) {
				d1.setEnumData(Grade.A);

				getEntityManager().merge(d1);
				getEntityManager().flush();

				q = getEntityManager().createQuery(
						"SELECT dt FROM DataTypes dt WHERE dt.enumData = ee.jakarta.tck.persistence.core.types.common.Grade.A");

				result = (DataTypes) q.getSingleResult();

				if (d1.equals(result)) {
					pass = true;
					logTrace( "Received expected result:" + d1.toString());
				} else {
					logErr( "Expected:" + d1.toString() + ", actual:" + result.toString());
				}

				getEntityTransaction().commit();
			} else {
				logErr( "find returned null");
			}
		} catch (Exception e) {
			logErr( "Unexpected exception occurred", e);
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
			throw new Exception("fieldTypeTest15 failed");
	}

	/*
	 * @testName: fieldTypeTest16
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:428; PERSISTENCE:SPEC:529;
	 * PERSISTENCE:SPEC:1090.1; PERSISTENCE:SPEC:1090.2; PERSISTENCE:SPEC:1319
	 * 
	 * @test_Strategy: enum_expression ::= enum_primary | (subquery) enum_primary
	 * ::= state_field_path_expression | input parameter | enum_literal
	 *
	 * named parameter
	 */
		public void fieldTypeTest16() throws Exception {

		boolean pass = false;
		Object result;
		Query q;

		try {

			getEntityTransaction().begin();
			d1 = getEntityManager().find(DataTypes.class, 1);

			if (null != d1) {
				d1.setEnumData(Grade.A);

				getEntityManager().merge(d1);
				getEntityManager().flush();

				q = getEntityManager().createQuery("SELECT dt FROM DataTypes dt WHERE dt.enumData = :grade")
						.setParameter("grade", Grade.A);

				result = (DataTypes) q.getSingleResult();

				if (d1.equals(result)) {
					pass = true;
					logTrace( "Received expected result:" + d1.toString());
				} else {
					logErr( "Expected:" + d1.toString() + ", actual:" + result.toString());
				}
				getEntityTransaction().commit();
			} else {
				logErr( "find returned null");
			}
		} catch (Exception e) {
			logErr( "Unexpected exception occurred", e);
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
			throw new Exception("fieldTypeTest16 failed");
	}

	/*
	 * @testName: fieldTypeTest17
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:428; PERSISTENCE:SPEC:529;
	 * PERSISTENCE:SPEC:1090.1; PERSISTENCE:SPEC:1090.2; PERSISTENCE:SPEC:1319
	 * 
	 * @test_Strategy: enum_expression ::= enum_primary | (subquery) enum_primary
	 * ::= state_field_path_expression | input parameter | enum_literal
	 *
	 * positional parameters
	 */
		public void fieldTypeTest17() throws Exception {

		boolean pass = false;
		Object result;
		Query q;
		final Grade failingGrade = ee.jakarta.tck.persistence.core.types.common.Grade.F;
		final Grade incompleteGrade = ee.jakarta.tck.persistence.core.types.common.Grade.INCOMPLETE;

		try {

			getEntityTransaction().begin();
			d1 = getEntityManager().find(DataTypes.class, 1);

			if (null != d1) {
				d1.setEnumData(Grade.C);

				getEntityManager().merge(d1);
				getEntityManager().flush();

				q = getEntityManager()
						.createQuery("SELECT dt FROM DataTypes dt WHERE (dt.enumData <> ?1) OR (dt.enumData <> ?2) ")
						.setParameter(1, failingGrade).setParameter(2, incompleteGrade);

				result = (DataTypes) q.getSingleResult();

				if (d1.equals(result)) {
					pass = true;
					logTrace( "Received expected result:" + d1.toString());
				} else {
					logErr( "Expected:" + d1.toString() + ", actual:" + result.toString());
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
			throw new Exception("fieldTypeTest17 failed");
	}

	// Methods used for Tests

	public void createTestData() {
		logTrace( "createTestData");

		try {
			getEntityTransaction().begin();
			char[] cArray = { 'a' };
			byte[] bArray = { (byte) 100 };
			d1 = new DataTypes(1, false, (byte) 100, 'a', (short) 100, 300, 600L, 50D, 1.0F, cArray, bArray);

			logTrace( "dateId is: " + dateId);
			d2 = new DataTypes2(dateId);

			getEntityManager().persist(d1);
			getEntityManager().persist(d2);
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
