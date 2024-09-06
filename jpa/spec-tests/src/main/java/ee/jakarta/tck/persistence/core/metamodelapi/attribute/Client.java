/*
 * Copyright (c) 2009, 2020 Oracle and/or its affiliates. All rights reserved.
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

package ee.jakarta.tck.persistence.core.metamodelapi.attribute;



import java.util.Properties;

import com.sun.ts.lib.harness.Status;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ee.jakarta.tck.persistence.common.PMClientBase;
import jakarta.persistence.metamodel.Attribute;
import jakarta.persistence.metamodel.ManagedType;
import jakarta.persistence.metamodel.Metamodel;

public class Client extends PMClientBase {



	public Client() {
	}
	public static void main(String[] args) {
		Client theTests = new Client();
		Status s = theTests.run(args, System.out, System.err);
		s.exit();
	}

	public JavaArchive createDeployment() throws Exception {

		String pkgNameWithoutSuffix = Client.class.getPackageName();
		String pkgName = pkgNameWithoutSuffix + ".";
		String[] classes = { pkgName + "Order" };
		return createDeploymentJar("jpa_core_metamodelapi_attribute.jar", pkgNameWithoutSuffix, classes);

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
	 * @testName: getName
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1216
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
			ManagedType<Order> mTypeOrder = metaModel
					.managedType(ee.jakarta.tck.persistence.core.metamodelapi.attribute.Order.class);
			if (mTypeOrder != null) {
				logTrace( "Obtained Non-null ManagedType");
				Attribute<Order, ?> attrib = mTypeOrder.getDeclaredAttribute("total");
				if (attrib != null) {
					logTrace( "attribute Name = " + attrib.getName());
					if (attrib.getName() != null) {

						if (attrib.getName().equals("total")) {
							logTrace( "Received expected result:" + attrib.getName());
							pass = true;
						} else {
							logErr(
									"Expected: " + Attribute.PersistentAttributeType.BASIC.toString() + ", actual:"
											+ attrib.getName());
						}
					}
				}
			}
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getName Test  failed");
		}
	}

	/*
	 * @testName: getPersistentAttributeType
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1217
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
			ManagedType<Order> mTypeOrder = metaModel
					.managedType(ee.jakarta.tck.persistence.core.metamodelapi.attribute.Order.class);
			if (mTypeOrder != null) {
				logTrace( "Obtained Non-null ManagedType");
				Attribute<Order, ?> attrib = mTypeOrder.getDeclaredAttribute("total");
				if (attrib != null) {
					logTrace( "attribute Name = " + attrib.getName());
					Attribute.PersistentAttributeType pAttribType = attrib.getPersistentAttributeType();
					if (pAttribType == Attribute.PersistentAttributeType.BASIC) {
						logTrace( "Received expected result:" + pAttribType);
						pass = true;

					} else {
						logErr( "Expected: " + Attribute.PersistentAttributeType.BASIC.toString()
								+ ", actual:" + pAttribType);
					}
				}
			}
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getPersistentAttributeType Test  failed");
		}
	}

	/*
	 * @testName: getDeclaringType
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1213
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getDeclaringType() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logTrace( "Obtained Non-null Metamodel from EntityManager");
			ManagedType<Order> mTypeOrder = metaModel
					.managedType(ee.jakarta.tck.persistence.core.metamodelapi.attribute.Order.class);
			if (mTypeOrder != null) {
				logTrace( "Obtained Non-null ManagedType");
				Attribute<Order, ?> attrib = mTypeOrder.getDeclaredAttribute("total");
				if (attrib != null) {
					logTrace( "attribute Name = " + attrib.getName());
					ManagedType<Order> newTypeOrder = attrib.getDeclaringType();
					if (newTypeOrder != null) {
						Class javaType = newTypeOrder.getJavaType();
						if (javaType.getName().equals("ee.jakarta.tck.persistence.core.metamodelapi.attribute.Order")) {
							logTrace( "Received expected result:" + javaType.getName());
							pass = true;
						} else {
							logErr(
									"Expected: ee.jakarta.tck.persistence.core.metamodelapi.attribute.Order, actual:"
											+ javaType.getName());
						}
					}
				}
			}
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getDeclaringType Test  failed");
		}
	}

	/*
	 * @testName: getJavaType
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1215
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
					.managedType(ee.jakarta.tck.persistence.core.metamodelapi.attribute.Order.class);
			if (mTypeOrder != null) {
				logTrace( "Obtained Non-null ManagedType");
				Attribute<Order, ?> attrib = mTypeOrder.getDeclaredAttribute("total");
				if (attrib != null) {
					logTrace( "attribute JavaType = " + attrib.getJavaType());
					Class pAttribJavaType = attrib.getJavaType();
					if (pAttribJavaType.getName().equals("int")) {
						logTrace( "Received expected result:" + pAttribJavaType);
						pass = true;
					} else {
						logErr( "Expected: int, actual:" + pAttribJavaType);
					}
				}
			}
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getJavaType Test  failed");
		}
	}

	/*
	 * @testName: getJavaMember
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1214
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
			ManagedType<Order> mTypeOrder = metaModel
					.managedType(ee.jakarta.tck.persistence.core.metamodelapi.attribute.Order.class);
			if (mTypeOrder != null) {
				logTrace( "Obtained Non-null ManagedType");
				Attribute<Order, ?> attrib = mTypeOrder.getDeclaredAttribute("total");
				if (attrib != null) {
					logTrace( "attribute JavaMember = " + attrib.getJavaMember().getName());
					java.lang.reflect.Member javaMember = attrib.getJavaMember();
					if (javaMember.getName().equals("getTotal")) {
						logTrace( "Received expected result:" + javaMember.getName());
						pass = true;
					} else {
						logErr( "Expected: getTotal, actual:" + javaMember.getName());
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
	 * @testName: isAssociation
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1218
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
			ManagedType<Order> mTypeOrder = metaModel
					.managedType(ee.jakarta.tck.persistence.core.metamodelapi.attribute.Order.class);
			if (mTypeOrder != null) {
				logTrace( "Obtained Non-null ManagedType");
				Attribute<Order, ?> attrib = mTypeOrder.getDeclaredAttribute("total");
				if (attrib != null) {
					logTrace( "attribute IsAssociation = " + attrib.isAssociation());
					if (!attrib.isAssociation()) {
						logTrace( "Received expected result:" + attrib.isAssociation());
						pass = true;
					} else {
						logErr( "Received unexpected result: " + attrib.isAssociation());
					}
				}
			}
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("isAssociation Test  failed");
		}
	}

	/*
	 * @testName: isCollection
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1219
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
			ManagedType<Order> mTypeOrder = metaModel
					.managedType(ee.jakarta.tck.persistence.core.metamodelapi.attribute.Order.class);
			if (mTypeOrder != null) {
				logTrace( "Obtained Non-null ManagedType");
				Attribute<Order, ?> attrib = mTypeOrder.getDeclaredAttribute("total");
				if (attrib != null) {
					logTrace( "attribute IsCollection = " + attrib.isCollection());
					if (!attrib.isCollection()) {
						logTrace( "Received expected result:" + attrib.isCollection());
						pass = true;
					} else {
						logErr( "Received unexpected result: " + attrib.isCollection());
					}
				}
			}
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("isCollection Test  failed");
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
