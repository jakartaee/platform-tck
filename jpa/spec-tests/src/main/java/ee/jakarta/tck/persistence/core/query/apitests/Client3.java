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

package ee.jakarta.tck.persistence.core.query.apitests;


import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Properties;

import com.sun.ts.lib.harness.Status;
import ee.jakarta.tck.persistence.common.PMClientBase;
import jakarta.persistence.Query;

public class Client3 extends PMClientBase {

	private final Employee empRef[] = new Employee[21];

	private final Date d1 = getSQLDate("2000-02-14");

	private final java.util.Date dateId = getUtilDate("2009-01-10");

	final Department deptRef[] = new Department[5];

	private static final DecimalFormat df = new DecimalFormat();

	public Client3() {
	}
	public static void main(String[] args) {
		Client3 theTests = new Client3();
		Status s = theTests.run(args, System.out, System.err);
		s.exit();
	}

	public void setupDataTypes2(String[] args, Properties p) throws Exception {
		logTrace( "setup");
		try {
			super.setup(args,p);
			removeTestData();
			createDataTypes2Data();
			logTrace( "Done creating test data");
		} catch (Exception e) {
			logErr( "Unexpected Exception caught in Setup: ", e);
			throw new Exception("Setup failed:", e);

		}
	}

	public void cleanupNoData() throws Exception {
		try {
			logTrace( "in cleanupNoData");
			super.cleanup();
		} finally {

        }
	}

	/*
	 * BEGIN Test Cases
	 */

	/*
	 * @testName: queryAPITest28
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:527;
	 * 
	 * @test_Strategy: Usage of Time literal in Query
	 *
	 */
	
	public void queryAPITest28() throws Exception {

		Collection<Time> result;

		boolean pass1 = false;
		boolean pass2 = false;
		boolean pass3 = false;
		boolean pass4 = true;
		java.sql.Time timeValue = getTimeData("10:30:15");
		logTrace( "time Value = " + timeValue.toString());

		try {
			getEntityTransaction().begin();
			logTrace( "FIND D2: " + dateId);
			DataTypes2 dataTypes2 = getEntityManager().find(DataTypes2.class, dateId);

			if (null != dataTypes2) {
				logTrace( "DataType Entity is not null, setting TimeData ");
				dataTypes2.setTimeData(timeValue);
				pass1 = true;
			} else {
				logErr( "Null returned during initial find");
			}

			getEntityManager().merge(dataTypes2);
			doFlush();
			clearCache();

			logTrace( "Make sure update occurred");
			logTrace( "FIND D2 again:");
			dataTypes2 = getEntityManager().find(DataTypes2.class, dateId);

			if (null != dataTypes2) {
				if (dataTypes2.getTimeData().equals(timeValue)) {
					logTrace( "Update occurred properly:" + dataTypes2);
					pass2 = true;
				} else {
					logErr( "Update did not occur properly");
				}
			} else {
				logErr( "Find returned null after update");
			}

			logTrace( "Retrieving all results first");

			Collection<DataTypes2> cDataTypes2 = getEntityManager().createQuery("select d from DataTypes2 d")
					.getResultList();
			for (DataTypes2 d : cDataTypes2) {
				logTrace( "result:" + d.toString());
			}

			logTrace( "Check results when testing for Time");
			result = getEntityManager().createQuery("select d.timeData from DataTypes2 d where d.timeData = :time")
					.setParameter("time", timeValue).getResultList();

			int result_size = result.size();
			logTrace( "Result Size = " + result_size);

			if (result_size == 1) {
				pass3 = true;
				logTrace( "Received expected result size");
				for (Time t : result) {
					logTrace( "time=" + t);
					if (t.equals(timeValue)) {
						logTrace( "Received expected Time ");
					} else {
						pass4 = false;
						logErr( "Received unexpected Time = " + t.toString());
					}
				}

			} else {
				logErr( "Expected 1 result, instead got: " + result_size);
			}

			getEntityTransaction().commit();
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

		if (!pass1 || !pass2 || !pass3 || !pass4) {
			throw new Exception("queryAPITest28 failed");
		}
	}

	/*
	 * @testName: queryAPITest29
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:527;
	 * 
	 * @test_Strategy: Usage of TimeStamp literal in Query
	 *
	 */
	
	public void queryAPITest29() throws Exception {

		logTrace( "Begin queryAPITest29");
		Query q;
		Collection<Timestamp> result;
		int result_size = 0;

		boolean pass1 = false;
		boolean pass2 = true;

		java.sql.Timestamp tsValue = getTimestampData("2006-11-11");
		logTrace( "timestamp Value = " + tsValue.toString());

		try {
			getEntityTransaction().begin();
			logTrace( "FIND D2");

			DataTypes2 dataTypes2 = getEntityManager().find(DataTypes2.class, dateId);

			if (null != dataTypes2) {
				logTrace( "DataType Entity is not null, setting TimeData ");
				dataTypes2.setTsData(tsValue);
			}

			getEntityManager().merge(dataTypes2);
			doFlush();
			clearCache();

			logTrace( "Check results");
			if ((null != dataTypes2)) {
				// && (dataTypes2.getTimeData().equals(timeValue))

				q = getEntityManager()
						.createQuery("select d.tsData from DataTypes2 d where d.tsData = {ts '2006-11-11 10:10:10'}");

				result = q.getResultList();
				result_size = result.size();
				logTrace( "Result Size = " + result_size);

				if (result_size == 1) {
					pass1 = true;
					logTrace( "Received expected result size");

					for (Timestamp t : result) {
						logTrace( "time=" + t);
						if (t.equals(tsValue)) {
							logTrace( "Received expected TimeStamp ");
						} else {
							logErr( "Received unexpected TimeStamp = " + t.toString());
							pass2 = false;
						}
					}
				} else {
					logErr( "Did not get expected results. " + " Expected " + tsValue
							+ " , got: " + dataTypes2.getTsData());
				}
			}
			getEntityTransaction().commit();
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

		if (!pass1 || !pass2) {
			throw new Exception("queryAPITest29 failed");
		}
	}

	private void createDataTypes2Data() throws Exception {
		logTrace( "createDataTypes2Data");
		try {

			getEntityTransaction().begin();

			DataTypes2 dT2 = new DataTypes2(dateId);
			dT2.setDateData(dateId);
			dT2.setTimeData(getTimeData("01:01:01"));
			getEntityManager().persist(dT2);

			java.util.Date d = getUtilDate("2010-02-11");
			dT2 = new DataTypes2(d);
			dT2.setDateData(d);
			dT2.setTimeData(getTimeData("02:02:02"));
			getEntityManager().persist(dT2);

			d = getUtilDate("2011-03-12");
			dT2 = new DataTypes2(d);
			dT2.setDateData(d);
			dT2.setTimeData(getTimeData("03:03:03"));
			getEntityManager().persist(dT2);

			d = getUtilDate("2012-04-01");
			dT2 = new DataTypes2(d);
			dT2.setDateData(d);
			dT2.setTimeData(getTimeData("04:04:04"));
			getEntityManager().persist(dT2);

			getEntityTransaction().commit();
			logTrace( "Created TestData");

		} catch (Exception re) {
			logErr( "Unexpected Exception in createTestData:", re);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logErr( "Unexpected Exception in createTestData while rolling back TX:", re);
			}
		}
	}

	private void removeTestData() {
		logTrace( "removeTestData");
		if (getEntityTransaction().isActive()) {
			getEntityTransaction().rollback();
		}
		try {
			getEntityTransaction().begin();
			getEntityManager().createNativeQuery("DELETE FROM EMPLOYEE").executeUpdate();
			getEntityManager().createNativeQuery("DELETE FROM DEPARTMENT").executeUpdate();
			getEntityManager().createNativeQuery("DELETE FROM INSURANCE").executeUpdate();
			getEntityManager().createNativeQuery("DELETE FROM DATATYPES2").executeUpdate();
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
