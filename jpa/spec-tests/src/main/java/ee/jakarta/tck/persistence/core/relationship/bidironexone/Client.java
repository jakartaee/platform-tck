/*
 * Copyright (c) 2009, 2023 Oracle and/or its affiliates. All rights reserved.
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

package ee.jakarta.tck.persistence.core.relationship.bidironexone;

import java.util.Properties;


import com.sun.ts.lib.harness.Status;
import ee.jakarta.tck.persistence.common.PMClientBase;

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
			removeTestData();
		} catch (Exception e) {
			logErr( "Exception: ", e);
			throw new Exception("Setup failed:", e);
		}
	}

	/*
	 * @testName: biDir1X1Test1
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:1094; PERSISTENCE:JAVADOC:135;
	 * PERSISTENCE:JAVADOC:91; PERSISTENCE:SPEC:561; PERSISTENCE:SPEC:562;
	 * PERSISTENCE:SPEC:567; PERSISTENCE:SPEC:570; PERSISTENCE:SPEC:571;
	 * PERSISTENCE:SPEC:573; PERSISTENCE:SPEC:961; PERSISTENCE:SPEC:1028;
	 * PERSISTENCE:SPEC:1037; PERSISTENCE:SPEC:1038; PERSISTENCE:SPEC:1039
	 * 
	 * @test_Strategy: RelationShip OneToOne Mapping
	 *
	 */
	public void biDir1X1Test1() throws Exception {
		logTrace( "Begin biDir1X1Test1");
		boolean pass = false;
		try {
			getEntityTransaction().begin();

			BiDir1X1Project project = new BiDir1X1Project(1L, "JavaEE", 500.0F);
			BiDir1X1Person person = new BiDir1X1Person(1L, "Duke");
			project.setBiDir1X1Person(person);
			person.setProject(project);

			// persist project
			getEntityManager().persist(project);
			getEntityTransaction().commit();
			logTrace( "persisted Project this in turn must persist Person too..");

			getEntityTransaction().begin();

			// since this is a bi-directional relationship with cascade=ALL
			// persisting project must have persisted person too
			BiDir1X1Person newPerson = getEntityManager().find(BiDir1X1Person.class, 1L);
			if (newPerson != null) {
				logTrace( "Found Searched Person Entity");
				if ((getEntityManager().contains(newPerson)) && (newPerson.getName().equals("Duke"))) {
					logTrace( "biDir1X1Test1: Expected results received");
					pass = true;
				}
			} else {
				logTrace( "searched Person not found");
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
				logErr( "Unexpected Exception in rollback:", re);
			}
		}

		if (!pass) {
			throw new Exception("biDir1X1Test1 failed");
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
			getEntityManager().createNativeQuery("DELETE FROM BIDIR1X1PERSON").executeUpdate();
			getEntityManager().createNativeQuery("DELETE FROM BIDIR1X1PROJECT").executeUpdate();
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
