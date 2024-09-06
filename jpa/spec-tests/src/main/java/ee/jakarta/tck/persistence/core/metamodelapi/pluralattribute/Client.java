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

package ee.jakarta.tck.persistence.core.metamodelapi.pluralattribute;



import java.util.Properties;

import com.sun.ts.lib.harness.Status;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ee.jakarta.tck.persistence.common.PMClientBase;
import jakarta.persistence.metamodel.Bindable;
import jakarta.persistence.metamodel.ManagedType;
import jakarta.persistence.metamodel.Metamodel;
import jakarta.persistence.metamodel.PluralAttribute;
import jakarta.persistence.metamodel.Type;

public class Client extends PMClientBase {

	

	public Client() {
	}

	public JavaArchive createDeployment() throws Exception {

		String pkgNameWithoutSuffix = Client.class.getPackageName();
		String pkgName = pkgNameWithoutSuffix + ".";
		String[] classes = { pkgName + "Uni1XMPerson", pkgName + "Uni1XMProject" };
		return createDeploymentJar("jpa_core_metamodelapi_pluralattribute.jar", pkgNameWithoutSuffix, classes);

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
			createDeployment();
		} catch (Exception e) {
			logErr( "Exception: ", e);
			throw new Exception("Setup failed:", e);
		}
	}

	/*
	 * @testName: getCollectionType
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1442
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
			ManagedType<Uni1XMPerson> mType = metaModel.managedType(Uni1XMPerson.class);
			if (mType != null) {
				logTrace( "Obtained Non-null ManagedType");
				PluralAttribute pluralAttrib = mType.getCollection("projects", Uni1XMProject.class);

				PluralAttribute.CollectionType pluralColType = pluralAttrib.getCollectionType();
				logTrace( "collection Type = " + pluralAttrib.getCollectionType());
				if (pluralColType == PluralAttribute.CollectionType.COLLECTION) {
					logTrace( "Received Expected Collection type = " + pluralColType);
					pass = true;
				} else {
					logTrace( "Received UnExpected Collection type = " + pluralColType);
				}

				/*
				 * Type t = pluralAttrib.getElementType(); if (t != null) {
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
	 * @assertion_ids: PERSISTENCE:JAVADOC:1443
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
			ManagedType<Uni1XMPerson> mType = metaModel.managedType(Uni1XMPerson.class);
			if (mType != null) {
				logTrace( "Obtained Non-null ManagedType");
				PluralAttribute pluralAttrib = mType.getCollection("projects", Uni1XMProject.class);

				logTrace(
						"collection Element Type = " + pluralAttrib.getElementType().getJavaType().getName());
				String elementTypeName = pluralAttrib.getElementType().getJavaType().getName();
				if (elementTypeName.equals("ee.jakarta.tck.persistence.core.metamodelapi.pluralattribute.Uni1XMProject")) {
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

	/*
	 * @testName: isCollection
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1450;
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void isCollection() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logTrace( "Obtained Non-null Metamodel from EntityManager");
			ManagedType<Uni1XMPerson> mTypeUni1XMPerson = metaModel.managedType(Uni1XMPerson.class);
			if (mTypeUni1XMPerson != null) {
				logTrace( "Obtained Non-null ManagedType");
				PluralAttribute pluralAttrib = mTypeUni1XMPerson.getCollection("projects", Uni1XMProject.class);

				boolean b = pluralAttrib.isCollection();
				if (b) {
					logTrace( "Received expected result:" + b);
					pass = true;
				} else {
					logErr( "Expected: true, actual: " + b);
				}
			} else {
				logErr( "managedType() returned null");
			}
		} else {
			logErr( "getMetamodel() returned null");
		}
		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("isCollection Test failed");
		}
	}

	/*
	 * @testName: isAssociation
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1449;
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void isAssociation() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logTrace( "Obtained Non-null Metamodel from EntityManager");
			ManagedType<Uni1XMPerson> mTypeUni1XMPerson = metaModel.managedType(Uni1XMPerson.class);
			if (mTypeUni1XMPerson != null) {
				logTrace( "Obtained Non-null ManagedType");
				PluralAttribute pluralAttrib = mTypeUni1XMPerson.getCollection("projects", Uni1XMProject.class);

				boolean b = pluralAttrib.isAssociation();
				if (b) {
					logTrace( "Received expected result:" + b);
					pass = true;
				} else {
					logErr( "Expected: true, actual: " + b);
				}
			} else {
				logErr( "managedType() returned null");
			}
		} else {
			logErr( "getMetamodel() returned null");
		}
		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("isAssociation Test failed");
		}
	}

	/*
	 * @testName: getPersistentAttributeType
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1448;
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getPersistentAttributeType() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logTrace( "Obtained Non-null Metamodel from EntityManager");
			ManagedType<Uni1XMPerson> mTypeUni1XMPerson = metaModel.managedType(Uni1XMPerson.class);
			if (mTypeUni1XMPerson != null) {
				logTrace( "Obtained Non-null ManagedType");
				PluralAttribute pluralAttrib = mTypeUni1XMPerson.getCollection("projects", Uni1XMProject.class);
				if (pluralAttrib != null) {
					PluralAttribute.PersistentAttributeType pAttribType = pluralAttrib.getPersistentAttributeType();
					if (pAttribType == PluralAttribute.PersistentAttributeType.ONE_TO_MANY) {
						logTrace( "Received expected result " + pAttribType);
						pass = true;

					} else {
						logErr(
								"Expected: " + PluralAttribute.PersistentAttributeType.ONE_TO_MANY.toString()
										+ ", actual:" + pAttribType);
					}
				}
			}
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getPersistentAttributeType Test failed");
		}
	}

	/*
	 * @testName: getName
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1447;
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getName() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logTrace( "Obtained Non-null Metamodel from EntityManager");
			ManagedType<Uni1XMPerson> mTypeUni1XMPerson = metaModel.managedType(Uni1XMPerson.class);
			if (mTypeUni1XMPerson != null) {
				logTrace( "Obtained Non-null ManagedType");
				PluralAttribute pluralAttrib = mTypeUni1XMPerson.getCollection("projects", Uni1XMProject.class);
				if (pluralAttrib != null) {
					String name = pluralAttrib.getName();
					if (name.equals("projects")) {
						logTrace( "Received expected result" + name);
						pass = true;

					} else {
						logErr( "Expected: projects, actual:" + name);
					}
				}
			}
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getName Test failed");
		}
	}

	/*
	 * @testName: getJavaType
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1446;
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
			ManagedType<Uni1XMPerson> mTypeUni1XMPerson = metaModel.managedType(Uni1XMPerson.class);
			if (mTypeUni1XMPerson != null) {
				logTrace( "Obtained Non-null ManagedType");
				PluralAttribute pluralAttrib = mTypeUni1XMPerson.getCollection("projects", Uni1XMProject.class);
				if (pluralAttrib != null) {
					Class pPluralAttribJavaType = pluralAttrib.getJavaType();
					if (pPluralAttribJavaType.getName().equals("java.util.Collection")) {
						logTrace( "Received expected result " + pPluralAttribJavaType);
						pass = true;
					} else {
						logErr(
								"Expected: java.util.Collection, actual:" + pPluralAttribJavaType);
					}
				}
			}
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getJavaType Test failed");
		}
	}

	/*
	 * @testName: getJavaMember
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1445;
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getJavaMember() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logTrace( "Obtained Non-null Metamodel from EntityManager");
			ManagedType<Uni1XMPerson> mTypeUni1XMPerson = metaModel.managedType(Uni1XMPerson.class);
			if (mTypeUni1XMPerson != null) {
				logTrace( "Obtained Non-null ManagedType");
				PluralAttribute pluralAttrib = mTypeUni1XMPerson.getCollection("projects", Uni1XMProject.class);
				if (pluralAttrib != null) {
					logTrace(
							"Singular attribute JavaMember = " + pluralAttrib.getJavaMember().getName());
					java.lang.reflect.Member javaMember = pluralAttrib.getJavaMember();
					if (javaMember.getName().equals("projects")) {
						logTrace( "Received expected result " + javaMember.getName());
						pass = true;
					} else {
						logErr( "Expected: projects, actual:" + javaMember.getName());
					}
				}
			}
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getJavaMember Test  failed");
		}
	}

	/*
	 * @testName: getBindableType
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1452;
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getBindableType() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logTrace( "Obtained Non-null Metamodel from EntityManager");
			ManagedType<Uni1XMPerson> mTypeUni1XMPerson = metaModel.managedType(Uni1XMPerson.class);
			if (mTypeUni1XMPerson != null) {
				logTrace( "Obtained Non-null ManagedType");
				PluralAttribute pluralAttrib = mTypeUni1XMPerson.getCollection("projects", Uni1XMProject.class);
				if (pluralAttrib != null) {
					logTrace( "attribute Name = " + pluralAttrib.getName());
					Bindable.BindableType bType = pluralAttrib.getBindableType();
					if (bType != null) {

						if (bType.name().equals(Bindable.BindableType.PLURAL_ATTRIBUTE.name())) {
							logTrace( "Received expected result:" + bType.name());
							pass = true;
						} else {
							logErr( "Expected: " + Bindable.BindableType.PLURAL_ATTRIBUTE.name()
									+ ", actual:" + bType.name());
						}
					}
				}
			}
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getBindableType Test  failed");
		}
	}

	/*
	 * @testName: getBindableJavaType
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1451;
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getBindableJavaType() throws Exception {
		boolean pass = false;
		String expected = "ee.jakarta.tck.persistence.core.metamodelapi.pluralattribute.Uni1XMProject";
		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logTrace( "Obtained Non-null Metamodel from EntityManager");
			ManagedType<Uni1XMPerson> mTypeUni1XMPerson = metaModel.managedType(Uni1XMPerson.class);
			if (mTypeUni1XMPerson != null) {
				logTrace( "Obtained Non-null ManagedType");
				PluralAttribute pluralAttrib = mTypeUni1XMPerson.getCollection("projects", Uni1XMProject.class);
				if (pluralAttrib != null) {
					logTrace( "attribute Name = " + pluralAttrib.getName());
					Class cType = pluralAttrib.getBindableJavaType();
					if (cType != null) {
						if (cType.getName().equals(expected)) {
							logTrace( "Received expected result:" + cType.getName());
							pass = true;
						} else {
							logErr( "Expected: " + expected + ", actual:" + cType.getName());
						}
					}
				}
			}
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getBindableJavaType Test  failed");
		}
	}

	/*
	 * @testName: getDeclaringType
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1444;
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getDeclaringType() throws Exception {
		boolean pass = false;
		String expected = "ee.jakarta.tck.persistence.core.metamodelapi.pluralattribute.Uni1XMPerson";
		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logTrace( "Obtained Non-null Metamodel from EntityManager");
			ManagedType<Uni1XMPerson> mTypeUni1XMPerson = metaModel.managedType(Uni1XMPerson.class);
			if (mTypeUni1XMPerson != null) {
				logTrace( "Obtained Non-null ManagedType");
				PluralAttribute pluralAttrib = mTypeUni1XMPerson.getCollection("projects", Uni1XMProject.class);
				if (pluralAttrib != null) {
					Type type = pluralAttrib.getDeclaringType();
					if (type != null) {
						String name = type.getJavaType().getName();
						if (name.equals(expected)) {
							logTrace( "Received expected result:" + name);
							pass = true;
						} else {
							logErr( "Expected: " + expected + ", actual:" + name);
						}
					} else {
						logErr( "getDeclaringType() returned null");
					}
				} else {
					logErr( "getCollection(...) returned null");
				}
			} else {
				logErr( "managedType() returned null");
			}
		} else {
			logErr( "getMetamodel() returned null");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getDeclaringType Test  failed");
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

		try {
			if (getEntityTransaction().isActive()) {
				getEntityTransaction().rollback();
			}
		} catch (Exception re) {
			logErr( "Unexpected Exception in removeTestData:", re);
		}

	}
}
