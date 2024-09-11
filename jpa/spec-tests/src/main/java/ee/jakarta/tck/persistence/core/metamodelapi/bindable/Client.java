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

package ee.jakarta.tck.persistence.core.metamodelapi.bindable;


import java.util.Properties;
import java.util.Set;

import com.sun.ts.lib.harness.Status;





import ee.jakarta.tck.persistence.common.PMClientBase;
import jakarta.persistence.metamodel.Bindable;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.Metamodel;

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
	 * @testName: getBindableType
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1438; PERSISTENCE:JAVADOC:1225
	 *
	 * @test_Strategy:
	 * 
	 */
		public void getBindableType() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logTrace( "Obtained Non-null Metamodel from EntityManager");
			Set<EntityType<?>> aSet = metaModel.getEntities();
			if (aSet != null) {
				logTrace( "Obtained Non-null Set of EntityType");
				for (EntityType eType : aSet) {
					logTrace( "entity's BindableType is  = " + eType.getBindableType());
					if (eType.getBindableType().equals(Bindable.BindableType.ENTITY_TYPE)) {
						logTrace( "as Expected BindableType is ENTITY_TYPE");
						pass = true;
					} else {
						logTrace( "bindableType is non ENTITY_TYPE");
					}
				}
			}
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getBindableType failed");
		}
	}

	/*
	 * @testName: getBindableJavaType
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1224
	 *
	 * @test_Strategy:
	 *
	 */
		public void getBindableJavaType() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logTrace( "Obtained Non-null Metamodel from EntityManager");
			Set<EntityType<?>> aSet = metaModel.getEntities();
			if (aSet != null) {
				logTrace( "Obtained Non-null Set of EntityType");
				for (EntityType eType : aSet) {
					logTrace(
							"entity's BindableJavaType is  = " + eType.getBindableJavaType().getName());
					String bindableJavaType = eType.getBindableJavaType().getName();

					if (bindableJavaType != null) {
						if (bindableJavaType.equals("ee.jakarta.tck.persistence.core.metamodelapi.bindable.A")) {
							logTrace( "as Expected BindableJavaType for A is " + bindableJavaType);
							pass = true;
						} else {
							logTrace( "bindableJavaType is incorrect");
						}
					}
				}
			}
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getBindableJavaType failed");
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
