/*
 * Copyright (c) 2023 Oracle and/or its affiliates. All rights reserved.
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
 * $Id$
 */

package ee.jakarta.tck.persistence.core.annotations.mapkey;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import com.sun.ts.lib.harness.Status;

public class Client1 extends Client {

	public Client1() {
	}
	
	public static void main(String[] args) {
		Client1 theTests = new Client1();
		Status s = theTests.run(args, System.out, System.err);
		s.exit();
	}

	public void setup(String args[], Properties p) throws Exception {
		logTrace( "setup");
		try {
			super.setup(args,p);
			removeTestData();
			createTestData();
		} catch (Exception e) {
			logErr( "Exception: ", e);
			throw new Exception("Setup failed:", e);
		}
	}

	/*
	 * @testName: annotationMapKeyTest1
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:114; PERSISTENCE:SPEC:1100;
	 * PERSISTENCE:SPEC:1101; PERSISTENCE:SPEC:518; PERSISTENCE:SPEC:1980;
	 * 
	 * @test_Strategy: The MapKey annotation is used to specify the map key for
	 * associations of type java.util.Map.
	 *
	 * The name element designates the name of the persistence property or field of
	 * the associated entity that is used as the map key.
	 * 
	 * Execute a query returning Employees objects.
	 * 
	 */
	
	public void annotationMapKeyTest1() throws Exception {

		boolean pass = true;

		try {
			List<Integer> expected = new ArrayList<Integer>();
			List<Integer> actual = new ArrayList<Integer>();

			expected.add(empRef[0].getId());
			expected.add(empRef[2].getId());
			expected.add(empRef[4].getId());

			getEntityTransaction().begin();

			logTrace( "find Employees belonging to Department: Marketing");
			List l = getEntityManager().createQuery("Select e from Employee e where e.department.name = 'Marketing'")
					.getResultList();

			for (Object o : l) {
				Employee e = (Employee) o;
				actual.add(e.getId());
			}

			Collections.sort(actual);
			if (expected.equals(actual)) {
				logTrace( "Received expected employees");
			} else {
				logErr( "Expected id values were:");
				for (Integer i : expected) {
					logErr( "id: " + i);
				}
				logErr( "actual id values were:");
				Collections.sort(actual);
				for (Integer i : actual) {
					logErr( "id: " + i);
				}
			}

			getEntityTransaction().commit();

		} catch (Exception ex) {
			logErr( "Unexpected exception occurred", ex);
			pass = false;
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logErr( "Unexpected Exception in rollback:", re);
			}
		}

		if (!pass)
			throw new Exception("annotationMapKeyTest1 failed");
	}

	/*
	 * @testName: annotationMapKeyTest2
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:114; PERSISTENCE:SPEC:1100;
	 * PERSISTENCE:SPEC:1101
	 * 
	 * @test_Strategy: The MapKey annotation is used to specify the map key for
	 * associations of type java.util.Map.
	 *
	 * The name element designates the name of the persistence property or field of
	 * the associated entity that is used as the map key.
	 *
	 * Execute a query returning Employee IDs.
	 */
	
	public void annotationMapKeyTest2() throws Exception {

		boolean pass = true;

		try {
			List<Integer> expected = new ArrayList<Integer>();
			List<Integer> actual = new ArrayList<Integer>();

			expected.add(empRef[1].getId());
			expected.add(empRef[3].getId());

			getEntityTransaction().begin();

			logTrace( "find Employees belonging to Department: Marketing");
			List l = getEntityManager()
					.createQuery(
							"Select e.id from Employee e where e.department.name = 'Administration' ORDER BY e.id DESC")
					.getResultList();

			for (Object o : l) {
				Integer i = (Integer) o;
				actual.add(i);
			}

			Collections.sort(actual);
			if (expected.equals(actual)) {
				logTrace( "Received expected employees");
			} else {
				logErr( "Expected id values were:");
				for (Integer i : expected) {
					logErr( "id: " + i);
				}
				logErr( "actual id values were:");
				Collections.sort(actual);
				for (Integer i : actual) {
					logErr( "id: " + i);
				}
			}

			getEntityTransaction().commit();

		} catch (Exception ex) {
			logErr( "Unexpected exception occurred", ex);
			pass = false;
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logErr( "Unexpected Exception in rollback:", re);
			}
		}

		if (!pass)
			throw new Exception("annotationMapKeyTest2 failed");
	}
}
