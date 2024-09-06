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

package ee.jakarta.tck.persistence.core.metamodelapi.collectionattribute;



import java.util.Properties;

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ee.jakarta.tck.persistence.common.PMClientBase;
import jakarta.persistence.metamodel.CollectionAttribute;
import jakarta.persistence.metamodel.ManagedType;
import jakarta.persistence.metamodel.Metamodel;
import jakarta.persistence.metamodel.PluralAttribute;

public class Client extends PMClientBase {



	public Client() {
	}

	public JavaArchive createDeployment() throws Exception {

		String pkgNameWithoutSuffix = Client.class.getPackageName();
		String pkgName = pkgNameWithoutSuffix + ".";
		String[] classes = { pkgName + "Uni1XMPerson", pkgName + "Uni1XMProject" };
		return createDeploymentJar("jpa_core_metamodelapi_collectionattribute.jar", pkgNameWithoutSuffix, classes);

	}


	public void setup(String[] args, Properties p) throws Exception {
		logTrace( "setup");
		try {
			super.setup(args,p);
			createDeployment();
		} catch (Exception e) {
			logErr( "Exception: ", e);
			throw new Exception("Setup failed:", e);
		}
	}

	/*
	 * @testName: getCollectionType
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1228;
	 * 
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getCollectionType() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logTrace( "Obtained Non-null Metamodel from EntityManager");
			ManagedType<Uni1XMPerson> mType = metaModel
					.managedType(ee.jakarta.tck.persistence.core.metamodelapi.collectionattribute.Uni1XMPerson.class);
			if (mType != null) {
				logTrace( "Obtained Non-null ManagedType");
				CollectionAttribute<? super Uni1XMPerson, Uni1XMProject> colAttrib = mType.getCollection("projects",
						ee.jakarta.tck.persistence.core.metamodelapi.collectionattribute.Uni1XMProject.class);

				PluralAttribute.CollectionType pluralColType = colAttrib.getCollectionType();
				logTrace( "collection Type = " + colAttrib.getCollectionType());
				if (pluralColType == PluralAttribute.CollectionType.COLLECTION) {
					logTrace( "Received Expected Collection type = " + pluralColType);
					pass = true;
				} else {
					logTrace( "Received UnExpected Collection type = " + pluralColType);
				}

				/*
				 * Type t = colAttrib.getElementType(); if (t != null) {
				 * logTrace("element Type  = " + t.getJavaType()); pass =
				 * true; }
				 */
			}
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getCollectionType Test  failed");
		}
	}

	/*
	 * @testName: getElementType
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1229;
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getElementType() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logTrace( "Obtained Non-null Metamodel from EntityManager");
			ManagedType<Uni1XMPerson> mType = metaModel
					.managedType(ee.jakarta.tck.persistence.core.metamodelapi.collectionattribute.Uni1XMPerson.class);
			if (mType != null) {
				logTrace( "Obtained Non-null ManagedType");
				CollectionAttribute<? super Uni1XMPerson, Uni1XMProject> colAttrib = mType.getCollection("projects",
						ee.jakarta.tck.persistence.core.metamodelapi.collectionattribute.Uni1XMProject.class);

				logTrace(
						"collection Element Type = " + colAttrib.getElementType().getJavaType().getName());
				String elementTypeName = colAttrib.getElementType().getJavaType().getName();
				if (elementTypeName
						.equals("ee.jakarta.tck.persistence.core.metamodelapi.collectionattribute.Uni1XMProject")) {
					logTrace( "Received Expected Element type = " + elementTypeName);
					pass = true;
				} else {
					logTrace( "Received UnExpected Element type = " + elementTypeName);
				}
			}
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getElementType Test  failed");
		}
	}

	@AfterEach
	public void cleanup() throws Exception {
		try {
			logTrace( "in cleanup");
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception fe) {
				logErr( "Unexpected exception rolling back TX:", fe);
			}
			logTrace( "done cleanup, calling super.cleanup");
			super.cleanup();
		} finally {

        }
	}
}
