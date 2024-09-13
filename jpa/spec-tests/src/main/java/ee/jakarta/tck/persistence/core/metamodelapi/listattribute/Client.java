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

package ee.jakarta.tck.persistence.core.metamodelapi.listattribute;



import java.util.Properties;

import com.sun.ts.lib.harness.Status;





import ee.jakarta.tck.persistence.common.PMClientBase;
import jakarta.persistence.metamodel.ListAttribute;
import jakarta.persistence.metamodel.ManagedType;
import jakarta.persistence.metamodel.Metamodel;
import jakarta.persistence.metamodel.PluralAttribute;
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
			
		} catch (Exception e) {
			logErr( "Exception: ", e);
			throw new Exception("Setup failed:", e);
		}
	}

	/*
	 * @testName: getList
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1395; PERSISTENCE:JAVADOC:1362;
	 * 
	 * @test_Strategy:
	 */
		public void getList() throws Exception {
		boolean pass = false;

		try {
			getEntityTransaction().begin();
			Metamodel metaModel = getEntityManager().getMetamodel();
			if (metaModel != null) {
				logTrace( "Obtained Non-null Metamodel from EntityManager");
				ManagedType<BiDirMX1Project> mType = metaModel.managedType(BiDirMX1Project.class);
				if (mType != null) {
					logTrace( "Obtained Non-null ManagedType");
					ListAttribute<? super BiDirMX1Project, BiDirMX1Person> listAttrib = mType.getList("biDirMX1Persons",
							BiDirMX1Person.class);
					Type t = listAttrib.getElementType();
					if (t != null) {
						logTrace( "element Type  = " + t.getJavaType());
						String name = t.getJavaType().getName();
						if (name.equals("ee.jakarta.tck.persistence.core.metamodelapi.listattribute.BiDirMX1Person")) {
							pass = true;
						} else {
							logErr( "Expected: BiDirMX1Person, actual:" + name);
						}
					}
				}
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logErr( "Received unexpected exception", e);
		}
		if (!pass) {
			throw new Exception("getList Test  failed");
		}
	}

	/*
	 * @testName: getCollectionType
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1361;
	 *
	 * @test_Strategy:
	 *
	 */
		public void getCollectionType() throws Exception {
		boolean pass = false;

		String expected = PluralAttribute.CollectionType.LIST.name();
		try {
			getEntityTransaction().begin();
			Metamodel metaModel = getEntityManager().getMetamodel();
			if (metaModel != null) {
				logTrace( "Obtained Non-null Metamodel from EntityManager");
				ManagedType<BiDirMX1Project> mType = metaModel.managedType(BiDirMX1Project.class);
				if (mType != null) {
					logTrace( "Obtained Non-null ManagedType");
					ListAttribute<? super BiDirMX1Project, BiDirMX1Person> listAttrib = mType.getList("biDirMX1Persons",
							BiDirMX1Person.class);
					PluralAttribute.CollectionType t = listAttrib.getCollectionType();
					if (t != null) {
						String sType = t.name();
						if (sType.equals(expected)) {
							logTrace( "Received expected: " + expected);
							pass = true;
						} else {
							logErr( "Expected: " + expected + ", actual:" + sType);
						}
					} else {
						logErr( "getCollectionType() returned null");
					}
				} else {
					logErr( "managedType(...) returned null");
				}
			} else {
				logErr( "getMetamodel() returned null");
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logErr( "Received unexpected exception", e);
		}
		if (!pass) {
			throw new Exception("getCollectionType Test failed");
		}
	}

	/*
	 * @testName: getDeclaredList
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1377
	 *
	 * @test_Strategy:
	 *
	 */
		public void getDeclaredList() throws Exception {
		boolean pass = false;

		try {
			getEntityTransaction().begin();
			Metamodel metaModel = getEntityManager().getMetamodel();
			if (metaModel != null) {
				logTrace( "Obtained Non-null Metamodel from EntityManager");
				ManagedType<BiDirMX1Project> mType = metaModel.managedType(BiDirMX1Project.class);
				if (mType != null) {
					logTrace( "Obtained Non-null ManagedType");
					ListAttribute<BiDirMX1Project, BiDirMX1Person> listAttrib = mType.getDeclaredList("biDirMX1Persons",
							BiDirMX1Person.class);
					Type t = listAttrib.getElementType();
					if (t != null) {
						logTrace( "element Type  = " + t.getJavaType());
						String name = t.getJavaType().getName();
						if (name.equals("ee.jakarta.tck.persistence.core.metamodelapi.listattribute.BiDirMX1Person")) {
							pass = true;
						} else {
							logErr( "Expected: BiDirMX1Person, actual:" + name);
						}
					}
				}
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logErr( "Received unexpected exception", e);
		}
		if (!pass) {
			throw new Exception("getDeclaredList Test  failed");
		}
	}

	/*
	 * @testName: getList2
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1397;
	 *
	 * @test_Strategy:
	 *
	 */
		public void getList2() throws Exception {
		boolean pass = false;

		try {
			getEntityTransaction().begin();
			Metamodel metaModel = getEntityManager().getMetamodel();
			if (metaModel != null) {
				logTrace( "Obtained Non-null Metamodel from EntityManager");
				ManagedType<BiDirMX1Project> mType = metaModel.managedType(BiDirMX1Project.class);
				if (mType != null) {
					logTrace( "Obtained Non-null ManagedType");
					ListAttribute<? super BiDirMX1Project, ?> listAttrib = mType.getList("biDirMX1Persons");
					Type t = listAttrib.getElementType();
					if (t != null) {
						logTrace( "element Type  = " + t.getJavaType());
						String name = t.getJavaType().getName();
						if (name.equals("ee.jakarta.tck.persistence.core.metamodelapi.listattribute.BiDirMX1Person")) {
							pass = true;
						} else {
							logErr( "Expected: BiDirMX1Person, actual:" + name);
						}
					}
				}
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logErr( "Received unexpected exception", e);
		}
		if (!pass) {
			throw new Exception("getList2 Test  failed");
		}
	}

	/*
	 * @testName: getDeclaredList2
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1246
	 *
	 * @test_Strategy:
	 *
	 */
		public void getDeclaredList2() throws Exception {
		boolean pass = false;
		try {

			getEntityTransaction().begin();
			Metamodel metaModel = getEntityManager().getMetamodel();
			if (metaModel != null) {
				logTrace( "Obtained Non-null Metamodel from EntityManager");
				ManagedType<BiDirMX1Project> mType = metaModel.managedType(BiDirMX1Project.class);
				if (mType != null) {
					logTrace( "Obtained Non-null ManagedType");
					ListAttribute<BiDirMX1Project, ?> listAttrib = mType.getDeclaredList("biDirMX1Persons");
					Type t = listAttrib.getElementType();
					if (t != null) {
						logTrace( "element Type  = " + t.getJavaType());
						String name = t.getJavaType().getName();
						if (name.equals("ee.jakarta.tck.persistence.core.metamodelapi.listattribute.BiDirMX1Person")) {
							pass = true;
						} else {
							logErr( "Expected: BiDirMX1Person, actual:" + name);
						}
					}
				}
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logErr( "Received unexpected exception", e);
		}
		if (!pass) {
			throw new Exception("getDeclaredList2 Test  failed");
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

		try {
			if (getEntityTransaction().isActive()) {
				getEntityTransaction().rollback();
			}
		} catch (Exception re) {
			logErr( "Unexpected Exception in removeTestData:", re);
		}

	}
}
