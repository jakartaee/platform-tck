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

package ee.jakarta.tck.persistence.core.metamodelapi.basictype;



import java.util.Properties;

import com.sun.ts.lib.harness.Status;





import ee.jakarta.tck.persistence.common.PMClientBase;
import jakarta.persistence.metamodel.Attribute;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.ManagedType;
import jakarta.persistence.metamodel.Metamodel;
import jakarta.persistence.metamodel.Type;

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
	 * @testName: getJavaType
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1222
	 *
	 * @test_Strategy: Get the javaType of the ID
	 *
	 */
		public void getJavaType() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logTrace( "Obtained Non-null Metamodel from EntityManager");
			ManagedType<Order> mTypeOrder = metaModel.managedType(Order.class);
			if (mTypeOrder != null) {
				logTrace( "Obtained Non-null ManagedType");
				Attribute attrib = mTypeOrder.getDeclaredAttribute("id");
				if (attrib != null) {
					String type = attrib.getJavaType().getName();
					if (type.equals("int")) {
						logTrace( "Received expected type: int");
						pass = true;
					} else {
						logErr( "Expected: int, actual:" + type);
					}
				} else {
					logErr( "getDeclaredAttribute(...) returned null");
				}
			} else {
				logErr( "managedType(...) returned null");
			}
		} else {
			logErr( "getMetamodel() returned null");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getJavaType Test failed");
		}
	}

	/*
	 * @testName: getPersistenceType
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1223
	 *
	 * @test_Strategy: Get the persistent type of the Order class
	 *
	 */
		public void getPersistenceType() throws Exception {
		boolean pass = false;

		String expected = Type.PersistenceType.ENTITY.name();
		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logTrace( "Obtained Non-null Metamodel from EntityManager");
			EntityType<Order> eType = metaModel.entity(Order.class);
			if (eType != null) {
				Type.PersistenceType type = eType.getPersistenceType();
				if (type != null) {
					String sType = type.name();
					if (sType.equals(expected)) {
						logTrace( "Received:" + sType);
						pass = true;
					} else {
						logErr( "Expected: " + expected + ", actual:" + sType);
					}
				} else {
					logErr( "getPersistenceType() returned null");
				}
			} else {
				logErr( "entity(...) returned null");
			}
		} else {
			logErr( "getMetamodel() returned null");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getPersistenceType failed");
		}
	}

	
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
	}
}
