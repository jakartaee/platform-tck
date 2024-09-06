/*
 * Copyright (c) 2018, 2023 Oracle and/or its affiliates. All rights reserved.
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

package ee.jakarta.tck.persistence.core.override.attributeoverride;


import java.util.List;
import java.util.Properties;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ee.jakarta.tck.persistence.common.PMClientBase;

public class Client extends PMClientBase {



	private static final int ENTITY_ID = 3039;

	private static final String NAME = "Cheese";

	private static final String PUBLISHER = "Johnson";

	private static final int COST = 20;

	public Client() {
	}


	public void setup(String[] args, Properties p) throws Exception {
		logTrace( "setup");
		try {
			super.setup(args,p);
			removeTestData();
		} catch (Exception e) {
			logErr( "Exception:test failed ", e);
		}
	}

	/*
	 * @testName: testNoAttributeOverrideAnnotation
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:594; PERSISTENCE:SPEC:596;
	 * PERSISTENCE:SPEC:597; PERSISTENCE:SPEC:598; PERSISTENCE:SPEC:599;
	 * PERSISTENCE:SPEC:600; PERSISTENCE:SPEC:601;
	 * 
	 * @test_Strategy: LawBook is an entity which extends a class Book. A column
	 * "name" is overriden in Orm.xml as "BOOK_NAME". The following test tests for
	 * the same.
	 */
	@Test
	public void testNoAttributeOverrideAnnotation() throws Exception {

		LawBook book = new LawBook();
		getEntityTransaction().begin();
		book.setCategory("Motivational");
		book.setId(ENTITY_ID);
		book.setName(NAME);
		book.setPublisher(PUBLISHER);
		book.setCost(COST);
		getEntityManager().persist(book);
		getEntityManager().flush();
		try {
			List result = getEntityManager().createQuery("SELECT b FROM LawBook b where b.name= " + ":name")
					.setParameter("name", NAME).getResultList();
			if (result.size() == 1) {
				logTrace( "test Overriding Attributes passed");
			} else {
				throw new Exception("Expected the size to be 1 " + " but it is -" + result.size());
			}
		} catch (Exception e) {
			throw new Exception("Exception thrown while testing testNoAttributeOverrideAnnotation" + e);
		} finally {
			getEntityManager().remove(book);
			getEntityTransaction().commit();
		}
	}

	@AfterEach
	public void cleanup() throws Exception {
		try {
			logTrace( "Cleanup data");
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
			getEntityManager().createNativeQuery("DELETE FROM LAWBOOK").executeUpdate();
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
