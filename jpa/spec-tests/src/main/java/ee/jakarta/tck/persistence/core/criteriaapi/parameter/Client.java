/*
 * Copyright (c) 2011, 2023 Oracle and/or its affiliates. All rights reserved.
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

package ee.jakarta.tck.persistence.core.criteriaapi.parameter;






import java.util.Properties;

import ee.jakarta.tck.persistence.common.PMClientBase;

public abstract class Client extends PMClientBase {

	

	Employee[] empRef = new Employee[5];

	final java.sql.Date d1 = getSQLDate("2000-02-14");
	
	public void cleanup() throws Exception {
		try {
			logTrace( "calling super.cleanup");
			removeTestData();
			super.cleanup();
		} finally {

        }
	}

	protected void createTestData() {

		try {
			getEntityTransaction().begin();

			logMsg( "Creating Employees");

			final java.sql.Date d2 = getSQLDate("2001-06-27");
			final java.sql.Date d3 = getSQLDate("2002-07-07");
			final java.sql.Date d4 = getSQLDate("2003-03-03");
			final java.sql.Date d5 = getSQLDate();

			empRef[0] = new Employee(1, "Alan", "Frechette", d1, (float) 35000.0);
			empRef[1] = new Employee(2, "Arthur", "Frechette", d2, (float) 35000.0);
			empRef[2] = new Employee(3, "Shelly", "McGowan", d3, (float) 50000.0);
			empRef[3] = new Employee(4, "Robert", "Bissett", d4, (float) 55000.0);
			empRef[4] = new Employee(5, "Stephen", "DMilla", d5, (float) 25000.0);
			for (Employee e : empRef) {
				if (e != null) {
					getEntityManager().persist(e);
					logTrace( "persisted employee:" + e);
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
			} catch (Exception fe) {
				logErr( "Unexpected exception rolling back TX:", fe);
			}
		}
	}

	public void setupEmployee(String[] args, Properties p) throws Exception {
	   logTrace("setupEmployee");
	   try {
		 super.setup(args, p);
		 removeTestData();
		 createTestData();
	   } catch (Exception e) {
		 logErr("Exception: ", e);
		 throw new Exception("setupEmployee failed:", e);
	   }
	}

	protected void removeTestData() {
		logTrace( "removeTestData");
		if (getEntityTransaction().isActive()) {
			getEntityTransaction().rollback();
		}
		try {
			getEntityTransaction().begin();
			getEntityManager().createNativeQuery("DELETE FROM EMPLOYEE").executeUpdate();
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
