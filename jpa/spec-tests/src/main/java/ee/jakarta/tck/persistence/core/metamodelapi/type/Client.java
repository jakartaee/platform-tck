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

package ee.jakarta.tck.persistence.core.metamodelapi.type;



import java.util.Properties;

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ee.jakarta.tck.persistence.common.PMClientBase;
import jakarta.persistence.metamodel.EmbeddableType;
import jakarta.persistence.metamodel.ManagedType;
import jakarta.persistence.metamodel.Metamodel;
import jakarta.persistence.metamodel.Type;

public class Client extends PMClientBase {



	public Client() {
	}

	public JavaArchive createDeployment() throws Exception {

		String pkgNameWithoutSuffix = Client.class.getPackageName();
		String pkgName = pkgNameWithoutSuffix + ".";
		String[] classes = { pkgName + "Address", pkgName + "B", pkgName + "Order", pkgName + "ZipCode" };
		return createDeploymentJar("jpa_core_metamodelapi_type.jar", pkgNameWithoutSuffix, classes);

	}


	public void setup(String[] args, Properties p) throws Exception {
		logTrace( "setup");
		try {
			super.setup(args,p);
			createDeployment();
			removeTestData();
		} catch (Exception e) {
			logErr( "Exception: ", e);
			throw new Exception("Setup failed:", e);
		}
	}

	/*
	 * @testName: getPersistenceType
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1472
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getPersistenceType() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logTrace( "Obtained Non-null Metamodel from EntityManager");
			ManagedType<Order> mTypeOrder = metaModel
					.managedType(ee.jakarta.tck.persistence.core.metamodelapi.type.Order.class);
			if (mTypeOrder != null) {
				Type.PersistenceType type = mTypeOrder.getPersistenceType();
				logTrace( "Obtained Non-null ManagedType");
				if (type.equals(Type.PersistenceType.ENTITY)) {
					pass = true;
				} else {
					logTrace( "Persistence type = " + type.name());
				}
			}
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getPersistenceType Test  failed");
		}
	}

	/*
	 * @testName: getEmbeddablePersistenceType
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1472
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getEmbeddablePersistenceType() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logTrace( "Obtained Non-null Metamodel from EntityManager");
			EmbeddableType<Address> eTypeAddress = metaModel
					.embeddable(ee.jakarta.tck.persistence.core.metamodelapi.type.Address.class);
			if (eTypeAddress != null) {
				Type.PersistenceType type = eTypeAddress.getPersistenceType();
				logTrace( "Obtained Non-null Embeddable Type");
				if (type.equals(Type.PersistenceType.EMBEDDABLE)) {
					pass = true;
				} else {
					logTrace( "Persistence type = " + type);
				}
			}
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getEmbeddablePersistenceType Test  failed");
		}
	}

	/*
	 * @testName: getJavaType
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1471
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getJavaType() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logTrace( "Obtained Non-null Metamodel from EntityManager");
			ManagedType<Order> mTypeOrder = metaModel
					.managedType(ee.jakarta.tck.persistence.core.metamodelapi.type.Order.class);
			if (mTypeOrder != null) {
				Class javaType = mTypeOrder.getJavaType();
				logTrace( "Obtained Non-null ManagedType");
				if (javaType.getName().equals("ee.jakarta.tck.persistence.core.metamodelapi.type.Order")) {
					pass = true;
				} else {
					logTrace( "javaType name = " + javaType.getName());
				}
			}
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getJavaType Test  failed");
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
	}
}
