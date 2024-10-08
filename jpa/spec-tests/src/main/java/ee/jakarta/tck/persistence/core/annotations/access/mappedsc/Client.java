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

package ee.jakarta.tck.persistence.core.annotations.access.mappedsc;


import java.sql.Date;
import java.util.Properties;

import com.sun.ts.lib.harness.Status;





import ee.jakarta.tck.persistence.common.PMClientBase;

public class Client extends PMClientBase {



	private static FullTimeEmployee ftRef[] = new FullTimeEmployee[5];

	private static PartTimeEmployee ptRef[] = new PartTimeEmployee[5];

	final private Date d1 = getSQLDate(2000, 2, 14);

	final private Date d2 = getSQLDate(2001, 6, 27);

	final private Date d3 = getSQLDate(2002, 7, 7);

	final private Date d4 = getSQLDate(2003, 3, 3);

	final private Date d5 = getSQLDate(2004, 4, 10);

	final private Date d6 = getSQLDate(2005, 2, 18);

	final private Date d7 = getSQLDate(2000, 9, 17);

	final private Date d8 = getSQLDate(2001, 11, 14);

	final private Date d9 = getSQLDate(2002, 10, 4);

	final private Date d10 = getSQLDate(2003, 1, 25);

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
			

			removeTestData();
			createTestData();
			logTrace( "Done creating test data");

		} catch (Exception e) {
			logErr( "Exception: ", e);
			throw new Exception("Setup failed:", e);
		}
	}

	/*
	 * @testName: test1
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:593; PERSISTENCE:SPEC:596;
	 * PERSISTENCE:SPEC:597; PERSISTENCE:SPEC:598; PERSISTENCE:SPEC:599;
	 * PERSISTENCE:SPEC:1130; PERSISTENCE:SPEC:1131; PERSISTENCE:SPEC:1132;
	 * PERSISTENCE:SPEC:1133; PERSISTENCE:SPEC:1061; PERSISTENCE:SPEC:1062;
	 * PERSISTENCE:JAVADOC:4; PERSISTENCE:JAVADOC:5; PERSISTENCE:JAVADOC:6;
	 * PERSISTENCE:SPEC:1239;
	 * 
	 * @test_Strategy: An entity may have a mapped superclass which provides
	 * persistent entity state and mapping information
	 */
		public void test1() throws Exception {

		logTrace( "Begin test1");
		boolean pass = false;

		try {
			FullTimeEmployee ftEmp1 = getEntityManager().find(FullTimeEmployee.class, 1);

			if (ftEmp1.getFullTimeRep().equals("Mabel Murray")) {
				pass = true;
			}

		} catch (Exception e) {
			logErr( "Unexpected exception occurred", e);
		}

		if (!pass)
			throw new Exception("test1 failed");
	}

	/*
	 * @testName: test2
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:593; PERSISTENCE:SPEC:596;
	 * PERSISTENCE:SPEC:597; PERSISTENCE:SPEC:598; PERSISTENCE:SPEC:599;
	 * PERSISTENCE:SPEC:1130; PERSISTENCE:SPEC:1131; PERSISTENCE:SPEC:1132;
	 * PERSISTENCE:SPEC:1133; PERSISTENCE:SPEC:1061; PERSISTENCE:SPEC:1062;
	 * PERSISTENCE:JAVADOC:4; PERSISTENCE:JAVADOC:5; PERSISTENCE:JAVADOC:6;
	 * PERSISTENCE:SPEC:1239;
	 * 
	 * @test_Strategy: An entity may have a mapped superclass which provides
	 * persistent entity state and mapping information but is not an entity itself.
	 */
		public void test2() throws Exception {

		logTrace( "Begin test2");
		boolean pass = false;

		try {
			PartTimeEmployee ptEmp1 = getEntityManager().find(PartTimeEmployee.class, 6);

			if (ptEmp1.getPartTimeRep().equals("John Cleveland")) {
				pass = true;
			}

		} catch (Exception e) {
			e.printStackTrace();
			logErr( "Unexpected exception occurred", e);
		}

		if (!pass)
			throw new Exception("test2 failed");
	}

	public void createTestData() {
		logTrace( "createTestData");

		try {

			getEntityTransaction().begin();
			ftRef[0] = new FullTimeEmployee(1, "Jonathan", "Smith", d10, 40000.0F);
			ftRef[1] = new FullTimeEmployee(2, "Mary", "Macy", d9, 40000.0F);
			ftRef[2] = new FullTimeEmployee(3, "Sid", "Nee", d8, 40000.0F);
			ftRef[3] = new FullTimeEmployee(4, "Julie", "OClaire", d7, 60000.0F);
			ftRef[4] = new FullTimeEmployee(5, "Steven", "Rich", d6, 60000.0F);

			logTrace( "Persist full time employees ");
			for (FullTimeEmployee fte : ftRef) {
				getEntityManager().persist(fte);
				logTrace( "persisted employee " + fte);
			}

			ptRef[0] = new PartTimeEmployee(6, "Kellie", "Lee", d5, 60000.0F);
			ptRef[1] = new PartTimeEmployee(7, "Nicole", "Martin", d4, 60000.0F);
			ptRef[2] = new PartTimeEmployee(8, "Mark", "Francis", d3, 60000.0F);
			ptRef[3] = new PartTimeEmployee(9, "Will", "Forrest", d2, 60000.0F);
			ptRef[4] = new PartTimeEmployee(10, "Katy", "Hughes", d1, 60000.0F);

			logTrace( "Persist part time employees ");
			for (PartTimeEmployee pte : ptRef) {
				getEntityManager().persist(pte);
				logTrace( "persisted employee " + pte);
			}
			getEntityTransaction().commit();
		} catch (Exception re) {
			logErr( "Unexpected Exception creating test data:", re);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logErr( "Unexpected Exception rolling back TX:", re);
			}
		}
	}

	
	public void cleanup() throws Exception {
		try {
			logTrace( "cleanup");
			removeTestData();
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
			getEntityManager().createNativeQuery("Delete from DEPARTMENT").executeUpdate();
			getEntityManager().createNativeQuery("Delete from PARTTIMEEMPLOYEE").executeUpdate();
			getEntityManager().createNativeQuery("Delete from EMPLOYEE").executeUpdate();
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
