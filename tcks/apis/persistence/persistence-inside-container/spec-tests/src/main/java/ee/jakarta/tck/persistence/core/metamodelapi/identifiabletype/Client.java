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

package ee.jakarta.tck.persistence.core.metamodelapi.identifiabletype;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import com.sun.ts.lib.harness.Status;

import ee.jakarta.tck.persistence.common.PMClientBase;
import jakarta.persistence.metamodel.Attribute;
import jakarta.persistence.metamodel.CollectionAttribute;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.IdentifiableType;
import jakarta.persistence.metamodel.ListAttribute;
import jakarta.persistence.metamodel.MapAttribute;
import jakarta.persistence.metamodel.Metamodel;
import jakarta.persistence.metamodel.SetAttribute;
import jakarta.persistence.metamodel.SingularAttribute;
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
	 * @testName: getId
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1301
	 *
	 * @test_Strategy:
	 * 
	 */
		public void getId() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logTrace( "Obtained Non-null Metamodel from EntityManager");
			IdentifiableType<A> iType = metaModel.entity(A.class);
			if (iType != null) {
				logTrace( "Obtained Non-null EntityType");
				SingularAttribute<? super A, String> idAttrib = iType.getId(String.class);
				String name = idAttrib.getType().getJavaType().getName();
				if (name.equals("java.lang.String")) {
					logTrace( "Received expected: " + name);
					pass = true;
				} else {
					logErr( "Expected java.lang.String, actual:" + name);
				}
			} else {
				logErr( "entity(...) returned null");
			}
		} else {
			logErr( "getMetamodel() returned null");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getId failed");
		}
	}

	/*
	 * @testName: getIdIllegalArgumentException
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1302
	 *
	 * @test_Strategy:
	 *
	 */
		public void getIdIllegalArgumentException() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logTrace( "Obtained Non-null Metamodel from EntityManager");
			IdentifiableType<A> iType = metaModel.entity(A.class);
			if (iType != null) {
				logTrace( "Obtained A Entity");
				try {
					iType.getId(Date.class);
					logTrace( "Did not receive IllegalArgumentException");
				} catch (IllegalArgumentException iae) {
					logTrace( "Received expected IllegalArgumentException");
					pass = true;
				} catch (Exception e) {
					logErr( "Received unexpected exception", e);
				}

			} else {
				logErr( "entity(...) returned null");
			}
		} else {
			logErr( "getMetamodel() returned null");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getIdIllegalArgumentException failed");
		}
	}

	/*
	 * @testName: getVersion
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1307
	 *
	 * @test_Strategy:
	 *
	 */
		public void getVersion() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logTrace( "Obtained Non-null Metamodel from EntityManager");
			IdentifiableType<A> iType = metaModel.entity(A.class);
			if (iType != null) {
				logTrace( "Obtained Non-null EntityType");
				SingularAttribute<? super A, Integer> idAttrib = iType.getVersion(java.lang.Integer.class);
				String name = idAttrib.getType().getJavaType().getName();
				if (name.equals("java.lang.Integer")) {
					pass = true;
				} else {
					logErr( "Expected java.lang.Integer, actual:" + name);
				}
			} else {
				logErr( "entity(...) returned null");
			}
		} else {
			logErr( "getMetamodel() returned null");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getVersion failed");
		}
	}

	/*
	 * @testName: getVersionIllegalArgumentException
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1308
	 *
	 * @test_Strategy:
	 *
	 */
		public void getVersionIllegalArgumentException() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logTrace( "Obtained Non-null Metamodel from EntityManager");
			IdentifiableType<A> iType = metaModel.entity(A.class);
			if (iType != null) {
				logTrace( "Obtained A Entity");
				try {
					iType.getVersion(Date.class);
					logTrace( "Did not receive IllegalArgumentException");
				} catch (IllegalArgumentException iae) {
					logTrace( "Received expected IllegalArgumentException");
					pass = true;
				} catch (Exception e) {
					logErr( "Received unexpected exception", e);
				}
			} else {
				logErr( "entity(...) returned null");
			}
		} else {
			logErr( "getMetamodel() returned null");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getVersionIllegalArgumentException failed");
		}
	}

	/*
	 * @testName: getDeclaredId
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1297
	 * 
	 * @test_Strategy:
	 *
	 */
		public void getDeclaredId() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logTrace( "Obtained Non-null Metamodel from EntityManager");
			IdentifiableType<A> iType = metaModel.entity(A.class);
			if (iType != null) {
				logTrace( "Obtained Non-null EntityType");
				logTrace( "entityType Name = A");
				SingularAttribute<A, String> idAttrib = iType.getDeclaredId(java.lang.String.class);
				String name = idAttrib.getType().getJavaType().getName();
				if (name.equals("java.lang.String")) {
					logTrace( "Received expected name:" + name);
					pass = true;
				} else {
					logErr( "Expected java.lang.String, actual:" + name);
				}
			} else {
				logErr( "entity(...) returned null");
			}
		} else {
			logErr( "getMetamodel() returned null");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getDeclaredId failed");
		}
	}

	/*
	 * @testName: getDeclaredIdIllegalArgumentException
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1298
	 *
	 * @test_Strategy:
	 *
	 */
		public void getDeclaredIdIllegalArgumentException() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logTrace( "Obtained Non-null Metamodel from EntityManager");
			IdentifiableType<A> iType = metaModel.entity(A.class);
			if (iType != null) {
				logTrace( "Obtained A IdentifiableType");
				try {
					iType.getDeclaredId(Date.class);
					logTrace( "Did not receive IllegalArgumentException");
				} catch (IllegalArgumentException iae) {
					logTrace( "Received expected IllegalArgumentException");
					pass = true;
				} catch (Exception e) {
					logErr( "Received unexpected exception", e);
				}
			} else {
				logErr( "entity(...) returned null");
			}
		} else {
			logErr( "getMetamodel() returned null");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getDeclaredIdIllegalArgumentException failed");
		}
	}

	/*
	 * @testName: getDeclaredVersion
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1299
	 *
	 * @test_Strategy:
	 *
	 */
		public void getDeclaredVersion() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logTrace( "Obtained Non-null Metamodel from EntityManager");
			Set<EntityType<?>> aSet = metaModel.getEntities();
			if (aSet != null) {
				logTrace( "Obtained Non-null Set of EntityType");
				IdentifiableType<A> iType = metaModel.entity(A.class);

				logTrace( "entityType Name = " + ((EntityType) iType).getName());
				SingularAttribute<A, Integer> idAttrib = iType.getDeclaredVersion(Integer.class);
				String name = idAttrib.getName();
				if (name.equals("value")) {
					logTrace( "Received:" + name);
					pass = true;
				} else {
					logErr( "Expected: value, actual:" + name);
				}
			} else {
				logErr( "getEntities(...) returned null");
			}
		} else {
			logErr( "getMetamodel() returned null");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getDeclaredVersion failed");
		}
	}

	/*
	 * @testName: getDeclaredVersionIllegalArgumentException
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1300
	 *
	 * @test_Strategy:
	 *
	 */
		public void getDeclaredVersionIllegalArgumentException() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logTrace( "Obtained Non-null Metamodel from EntityManager");
			Set<EntityType<?>> aSet = metaModel.getEntities();
			if (aSet != null) {
				logTrace( "Obtained Non-null Set of EntityType");
				IdentifiableType<A> iType = metaModel.entity(A.class);
				logTrace( "entityType Name = " + ((EntityType) iType).getName());
				try {
					iType.getDeclaredVersion(Date.class);
					logTrace( "Did not receive IllegalArgumentException");
				} catch (IllegalArgumentException iae) {
					logTrace( "Received expected IllegalArgumentException");
					pass = true;
				} catch (Exception e) {
					logErr( "Received unexpected exception", e);
				}

			} else {
				logErr( "getEntities(...) returned null");
			}
		} else {
			logErr( "getMetamodel() returned null");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getDeclaredVersionIllegalArgumentException failed");
		}
	}

	/*
	 * @testName: getSupertype
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1306
	 *
	 * @test_Strategy:
	 *
	 */
		public void getSupertype() throws Exception {
		boolean pass = false;
		String expected = "ee.jakarta.tck.persistence.core.metamodelapi.identifiabletype.B";

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logTrace( "Obtained Non-null Metamodel from EntityManager");
			IdentifiableType<A> iType = metaModel.entity(A.class);
			if (iType != null) {
				logTrace( "entityType Name = " + ((EntityType) iType).getName());
				IdentifiableType<? super A> idType = iType.getSupertype();
				String name = idType.getJavaType().getName();
				if (name.equals(expected)) {
					logTrace( "getSuperType() returned:" + name);
					pass = true;
				} else {
					logErr( "Expected: " + expected + ", actual:" + name);
				}
			} else {
				logErr( "entity(...) returned null");
			}
		} else {
			logErr( "getMetamodel() returned null");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getSupertype failed");
		}
	}

	/*
	 * @testName: hasSingleIdAttribute
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1309
	 *
	 * @test_Strategy:
	 *
	 */
		public void hasSingleIdAttribute() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logTrace( "Obtained Non-null Metamodel from EntityManager");
			IdentifiableType<A> iType = metaModel.entity(A.class);
			if (iType != null) {

				logTrace( "entityType Name = " + ((EntityType) iType).getName());
				boolean hasSingleIdAttribute = iType.hasSingleIdAttribute();
				if (hasSingleIdAttribute) {
					pass = true;
					logTrace( "hasSingleIdAttribute() returned" + hasSingleIdAttribute);
				} else {
					logErr( "Expected: false, actual:" + hasSingleIdAttribute);

				}

			} else {
				logErr( "entity(...) returned null");
			}
		} else {
			logErr( "getMetamodel() returned null");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("hasSingleIdAttribute failed");
		}
	}

	/*
	 * @testName: hasVersionAttribute
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1310
	 *
	 * @test_Strategy:
	 *
	 */
		public void hasVersionAttribute() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logTrace( "Obtained Non-null Metamodel from EntityManager");
			IdentifiableType<A> iType = metaModel.entity(A.class);
			if (iType != null) {

				logTrace( "entityType Name = " + ((EntityType) iType).getName());
				boolean hasVersionAttribute = iType.hasVersionAttribute();
				if (hasVersionAttribute) {
					pass = true;
					logTrace( "hasSingleIdAttribute() returned" + hasVersionAttribute);
				} else {
					logErr( "Expected: false, actual:" + hasVersionAttribute);

				}
			} else {
				logErr( "entity(...) returned null");
			}
		} else {
			logErr( "getMetamodel() returned null");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("hasVersionAttribute failed");
		}
	}

	/*
	 * @testName: getIdClassAttributes
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1303
	 *
	 * @test_Strategy:
	 *
	 */
		public void getIdClassAttributes() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logTrace( "Obtained Non-null Metamodel from EntityManager");
			// Set<EntityType<?>> aSet = metaModel.getEntities();
			IdentifiableType<DID2Employee> iType = metaModel.entity(DID2Employee.class);
			if (iType != null) {

				Set<SingularAttribute<? super DID2Employee, ?>> idClassAttribSet = iType.getIdClassAttributes();
				if (idClassAttribSet != null) {
					for (SingularAttribute<? super DID2Employee, ?> attrib : idClassAttribSet) {
						logTrace( "attribute Name = " + attrib.getName());
					}
					pass = true;
				}
			} else {
				logErr( "entity(...) returned null");
			}
		} else {
			logErr( "getMetamodel() returned null");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getIdClassAttributes failed");
		}
	}

	/*
	 * @testName: getIdClassAttributesIllegalArgumentException
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1304
	 *
	 * @test_Strategy:
	 *
	 */
		public void getIdClassAttributesIllegalArgumentException() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logTrace( "Obtained A Entity");
			IdentifiableType<A> iType = metaModel.entity(A.class);
			if (iType != null) {

				try {
					iType.getIdClassAttributes();
					logTrace( "Did not receive IllegalArgumentException");
				} catch (IllegalArgumentException iae) {
					logTrace( "Received expected IllegalArgumentException");
					pass = true;
				} catch (Exception e) {
					logErr( "Received unexpected exception", e);
				}
			} else {
				logErr( "entity(...) returned null");
			}
		} else {
			logErr( "getMetamodel() returned null");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getIdClassAttributesIllegalArgumentException failed");
		}
	}

	/*
	 * @testName: getIdType
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1305
	 *
	 * @test_Strategy:
	 *
	 */
		public void getIdType() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logTrace( "Obtained Non-null Metamodel from EntityManager");
			IdentifiableType<A> iType = metaModel.entity(A.class);
			if (iType != null) {
				logTrace( "Obtained Non-null Entity A");
				Type idType = iType.getIdType();
				if (idType != null) {
					logTrace( "idType Name = " + idType.getJavaType());
					String name = idType.getJavaType().getName();

					if (name.equals("java.lang.String")) {
						logTrace( "Received expected: " + name);
						pass = true;
					} else {
						logErr( "Expected java.lang.String, actual:" + name);
					}

				} else {
					logErr( "getIdType() returned null");
				}
			} else {
				logErr( "entity(...) returned null");
			}
		} else {
			logErr( "getMetamodel() returned null");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getIdType failed");
		}
	}

	/*
	 * @testName: getAttribute
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1311;
	 *
	 * @test_Strategy:
	 *
	 */
		public void getAttribute() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logTrace( "Obtained Non-null Metamodel from EntityManager");
			IdentifiableType<A> iType = metaModel.entity(A.class);
			if (iType != null) {
				logTrace( "Obtained Non-null Entity A");
				Attribute attrib = iType.getAttribute("id");
				if (attrib != null) {
					logTrace( "attribute Name = " + attrib.getName());
					String name = attrib.getName();
					if (name.equals("id")) {
						logTrace( "Received expected result:" + name);
						pass = true;
					} else {
						logErr( "Expected: id, actual:" + name);
					}
				} else {
					logErr( "getAttribute(...) returned null");
				}
			} else {
				logErr( "entity(...) returned null");
			}
		} else {
			logErr( "getMetamodel() returned null");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getAttribute failed");
		}
	}

	/*
	 * @testName: getAttributeIllegalArgumentException
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1312;
	 *
	 * @test_Strategy:
	 *
	 */
		public void getAttributeIllegalArgumentException() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logTrace( "Obtained Non-null Metamodel from EntityManager");
			IdentifiableType<A> iType = metaModel.entity(A.class);
			if (iType != null) {
				logTrace( "Obtained Non-null Entity A");
				try {
					iType.getAttribute("doesnotexist");
					logErr( "Did not receive expected IllegalArgumentException");
				} catch (IllegalArgumentException iae) {
					logTrace( "Received expected IllegalArgumentException");
					pass = true;
				} catch (Exception ex) {
					logErr( "Received unexpected exception:", ex);
				}
			} else {
				logErr( "entity(...) returned null");
			}
		} else {
			logErr( "getMetamodel() returned null");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getAttributeIllegalArgumentException failed");
		}
	}

	/*
	 * @testName: getAttributes
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1313;
	 *
	 * @test_Strategy:
	 *
	 */
		public void getAttributes() throws Exception {
		boolean pass = false;

		List<String> expected = new ArrayList<String>();

		expected.add("cAddress");
		expected.add("cAddress_inherited");
		expected.add("id");
		expected.add("lAddress");
		expected.add("lAddress_inherited");
		expected.add("mAddress");
		expected.add("mAddress_inherited");
		expected.add("name");
		expected.add("sAddress");
		expected.add("sAddress_inherited");
		expected.add("value");
		Collections.sort(expected);

		List<String> actual = new ArrayList<String>();

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logTrace( "Obtained Non-null Metamodel from EntityManager");
			IdentifiableType<A> iType = metaModel.entity(A.class);
			if (iType != null) {
				logTrace( "Obtained Non-null Entity A");
				Set set = iType.getAttributes();
				if (set != null) {
					if (set.size() > 0) {
						for (Iterator i = set.iterator(); i.hasNext();) {
							Attribute attrib = (Attribute) i.next();
							actual.add(attrib.getName());
						}
						Collections.sort(actual);

						if (expected.containsAll(actual) && actual.containsAll(expected)
								&& expected.size() == actual.size()) {

							logTrace( "Received expected attributes");
							for (String attribName : expected) {
								logTrace( "attrib:" + attribName);
							}
							pass = true;
						} else {
							logErr( "Received unexpected attributes");
							logErr( "Expected(" + expected.size() + "):");
							for (String attribName : expected) {
								logErr( "attrib:" + attribName);
							}
							logErr( "Actual(" + actual.size() + "):");
							for (String attribName : actual) {
								logErr( "attrib:" + attribName);
							}
						}
					} else {
						logErr( "getAttributes() returned 0 results");
					}
				} else {
					logErr( "getAttributes() returned null");
				}
			} else {
				logErr( "entity(...) returned null");
			}
		} else {
			logErr( "getMetamodel() returned null");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getAttributes failed");
		}
	}

	/*
	 * @testName: getCollectionStringClass
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1314;
	 *
	 * @test_Strategy:
	 *
	 */
		public void getCollectionStringClass() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logTrace( "Obtained Non-null Metamodel from EntityManager");
			IdentifiableType<A> iType = metaModel.entity(A.class);
			if (iType != null) {
				logTrace( "Obtained Non-null Entity A");
				CollectionAttribute cAttrib = iType.getCollection("cAddress", Address.class);
				if (cAttrib != null) {
					logTrace( "attribute Name = " + cAttrib.getName());
					String name = cAttrib.getName();
					if (name.equals("cAddress")) {
						logTrace( "Received expected result:" + name);
						pass = true;
					} else {
						logErr( "Expected: cAddress, actual:" + name);
					}
				} else {
					logErr( "getCollection(...) returned null");
				}
			} else {
				logErr( "entity(...) returned null");
			}
		} else {
			logErr( "getMetamodel() returned null");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getCollectionStringClass failed");
		}
	}

	/*
	 * @testName: getCollectionStringClassIllegalArgumentException
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1315;
	 *
	 * @test_Strategy:
	 *
	 */
		public void getCollectionStringClassIllegalArgumentException() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logTrace( "Obtained Non-null Metamodel from EntityManager");
			IdentifiableType<A> iType = metaModel.entity(A.class);
			if (iType != null) {
				try {
					iType.getCollection("doesnotexist", Address.class);
					logErr( "Did not receive expected IllegalArgumentException");
				} catch (IllegalArgumentException iae) {
					logTrace( "Received expected IllegalArgumentException");
					pass = true;
				} catch (Exception ex) {
					logErr( "Received unexpected exception:", ex);
				}
			} else {
				logErr( "entity(...) returned null");
			}
		} else {
			logErr( "getMetamodel() returned null");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getCollectionStringClassIllegalArgumentException failed");
		}
	}

	/*
	 * @testName: getCollectionString
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1316;
	 *
	 * @test_Strategy:
	 *
	 */
		public void getCollectionString() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logTrace( "Obtained Non-null Metamodel from EntityManager");
			IdentifiableType<A> iType = metaModel.entity(A.class);
			if (iType != null) {
				logTrace( "Obtained Non-null Entity A");
				CollectionAttribute cAttrib = iType.getCollection("cAddress");
				if (cAttrib != null) {
					logTrace( "attribute Name = " + cAttrib.getName());
					String name = cAttrib.getName();
					if (name.equals("cAddress")) {
						logTrace( "Received expected result:" + name);
						pass = true;
					} else {
						logErr( "Expected: cAddress, actual:" + name);
					}
				} else {
					logErr( "getCollection(...) returned null");
				}
			} else {
				logErr( "entity(...) returned null");
			}
		} else {
			logErr( "getMetamodel() returned null");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getCollectionString failed");
		}
	}

	/*
	 * @testName: getCollectionStringIllegalArgumentException
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1317;
	 *
	 * @test_Strategy:
	 *
	 */
		public void getCollectionStringIllegalArgumentException() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logTrace( "Obtained Non-null Metamodel from EntityManager");
			IdentifiableType<A> iType = metaModel.entity(A.class);
			if (iType != null) {
				try {
					iType.getCollection("doesnotexist");
					logErr( "Did not receive expected IllegalArgumentException");
				} catch (IllegalArgumentException iae) {
					logTrace( "Received expected IllegalArgumentException");
					pass = true;
				} catch (Exception ex) {
					logErr( "Received unexpected exception:", ex);
				}
			} else {
				logErr( "entity(...) returned null");
			}
		} else {
			logErr( "getMetamodel() returned null");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getCollectionStringIllegalArgumentException failed");
		}
	}

	/*
	 * @testName: getDeclaredAttribute
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1318;
	 *
	 * @test_Strategy:
	 *
	 */
		public void getDeclaredAttribute() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logTrace( "Obtained Non-null Metamodel from EntityManager");
			IdentifiableType<A> iType = metaModel.entity(A.class);
			if (iType != null) {
				logTrace( "Obtained Non-null Entity A");
				Attribute attrib = iType.getDeclaredAttribute("cAddress");
				if (attrib != null) {
					logTrace( "attribute Name = " + attrib.getName());
					String name = attrib.getName();
					if (name.equals("cAddress")) {
						logTrace( "Received expected result:" + name);
						pass = true;
					} else {
						logErr( "Expected: cAddress, actual:" + name);
					}
				} else {
					logErr( "getDeclaredAttribute(...) returned null");
				}
			} else {
				logErr( "entity(...) returned null");
			}
		} else {
			logErr( "getMetamodel() returned null");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getDeclaredAttribute failed");
		}
	}

	/*
	 * @testName: getDeclaredAttributeIllegalArgumentException
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1319;
	 *
	 * @test_Strategy:
	 *
	 */
		public void getDeclaredAttributeIllegalArgumentException() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logTrace( "Obtained Non-null Metamodel from EntityManager");
			IdentifiableType<A> iType = metaModel.entity(A.class);
			if (iType != null) {
				try {
					iType.getDeclaredAttribute("cAddress_inherited");
					logErr( "Did not receive expected IllegalArgumentException");
				} catch (IllegalArgumentException iae) {
					logTrace( "Received expected IllegalArgumentException");
					pass = true;
				} catch (Exception ex) {
					logErr( "Received unexpected exception:", ex);
				}
			} else {
				logErr( "entity(...) returned null");
			}
		} else {
			logErr( "getMetamodel() returned null");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getDeclaredAttributeIllegalArgumentException failed");
		}
	}

	/*
	 * @testName: getDeclaredAttributes
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1320;
	 *
	 * @test_Strategy:
	 *
	 */
		public void getDeclaredAttributes() throws Exception {
		boolean pass = false;

		List<String> expected = new ArrayList<String>();

		expected.add("id");
		expected.add("cAddress");
		expected.add("lAddress");
		expected.add("mAddress");
		expected.add("sAddress");
		expected.add("value");
		Collections.sort(expected);

		List<String> actual = new ArrayList<String>();

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logTrace( "Obtained Non-null Metamodel from EntityManager");
			IdentifiableType<A> iType = metaModel.entity(A.class);
			if (iType != null) {
				logTrace( "Obtained Non-null Entity A");
				Set set = iType.getDeclaredAttributes();
				if (set != null) {
					if (set.size() > 0) {
						for (Iterator i = set.iterator(); i.hasNext();) {
							Attribute attrib = (Attribute) i.next();
							actual.add(attrib.getName());
						}
						Collections.sort(actual);

						if (expected.containsAll(actual) && actual.containsAll(expected)
								&& expected.size() == actual.size()) {

							logTrace( "Received expected attributes");
							for (String attribName : expected) {
								logTrace( "attrib:" + attribName);
							}
							pass = true;
						} else {
							logErr( "Received unexpected attributes");
							logErr( "Expected(" + expected.size() + "):");
							for (String attribName : expected) {
								logErr( "attrib:" + attribName);
							}
							logErr( "Actual(" + actual.size() + "):");
							for (String attribName : actual) {
								logErr( "attrib:" + attribName);
							}
						}
					} else {
						logErr( "getAttributes() returned 0 results");
					}
				} else {
					logErr( "getDeclaredAttributes() returned null");
				}
			} else {
				logErr( "entity(...) returned null");
			}
		} else {
			logErr( "getMetamodel() returned null");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getDeclaredAttributes failed");
		}
	}

	/*
	 * @testName: getDeclaredCollectionStringClass
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1321;
	 *
	 * @test_Strategy:
	 *
	 */
		public void getDeclaredCollectionStringClass() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logTrace( "Obtained Non-null Metamodel from EntityManager");
			IdentifiableType<A> iType = metaModel.entity(A.class);
			if (iType != null) {
				logTrace( "Obtained Non-null Entity A");
				CollectionAttribute cAttrib = iType.getCollection("cAddress", Address.class);
				if (cAttrib != null) {
					logTrace( "attribute Name = " + cAttrib.getName());
					String name = cAttrib.getName();
					if (name.equals("cAddress")) {
						logTrace( "Received expected result:" + name);
						pass = true;
					} else {
						logErr( "Expected: cAddress, actual:" + name);
					}
				} else {
					logErr( "getCollection(...) returned null");
				}
			} else {
				logErr( "entity(...) returned null");
			}
		} else {
			logErr( "getMetamodel() returned null");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getDeclaredCollectionStringClass failed");
		}
	}

	/*
	 * @testName: getDeclaredCollectionStringClassIllegalArgumentException
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1322;
	 *
	 * @test_Strategy:
	 *
	 */
		public void getDeclaredCollectionStringClassIllegalArgumentException() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logTrace( "Obtained Non-null Metamodel from EntityManager");
			IdentifiableType<A> iType = metaModel.entity(A.class);
			if (iType != null) {
				try {
					iType.getDeclaredCollection("cAddress_inherited", Address.class);
					logErr( "Did not receive expected IllegalArgumentException");
				} catch (IllegalArgumentException iae) {
					logTrace( "Received expected IllegalArgumentException");
					pass = true;
				} catch (Exception ex) {
					logErr( "Received unexpected exception:", ex);
				}
			} else {
				logErr( "entity(...) returned null");
			}
		} else {
			logErr( "getMetamodel() returned null");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getDeclaredCollectionStringClassIllegalArgumentException failed");
		}
	}

	/*
	 * @testName: getDeclaredCollectionString
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1323;
	 *
	 * @test_Strategy:
	 *
	 */
		public void getDeclaredCollectionString() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logTrace( "Obtained Non-null Metamodel from EntityManager");
			IdentifiableType<A> iType = metaModel.entity(A.class);
			if (iType != null) {
				logTrace( "Obtained Non-null Entity A");
				CollectionAttribute cAttrib = iType.getCollection("cAddress", Address.class);
				if (cAttrib != null) {
					logTrace( "attribute Name = " + cAttrib.getName());
					String name = cAttrib.getName();
					if (name.equals("cAddress")) {
						logTrace( "Received expected result:" + name);
						pass = true;
					} else {
						logErr( "Expected: cAddress, actual:" + name);
					}
				} else {
					logErr( "getCollection(...) returned null");
				}
			} else {
				logErr( "entity(...) returned null");
			}
		} else {
			logErr( "getMetamodel() returned null");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getDeclaredCollectionString failed");
		}
	}

	/*
	 * @testName: getDeclaredCollectionStringIllegalArgumentException
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1324;
	 *
	 * @test_Strategy:
	 *
	 */
		public void getDeclaredCollectionStringIllegalArgumentException() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logTrace( "Obtained Non-null Metamodel from EntityManager");
			IdentifiableType<A> iType = metaModel.entity(A.class);
			if (iType != null) {
				try {
					iType.getDeclaredCollection("cAddress_inherited", Address.class);
					logErr( "Did not receive expected IllegalArgumentException");
				} catch (IllegalArgumentException iae) {
					logTrace( "Received expected IllegalArgumentException");
					pass = true;
				} catch (Exception ex) {
					logErr( "Received unexpected exception:", ex);
				}
			} else {
				logErr( "entity(...) returned null");
			}
		} else {
			logErr( "getMetamodel() returned null");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getDeclaredCollectionStringIllegalArgumentException failed");
		}
	}

	/*
	 * @testName: getDeclaredListStringClass
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1325;
	 *
	 * @test_Strategy:
	 *
	 */
		public void getDeclaredListStringClass() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logTrace( "Obtained Non-null Metamodel from EntityManager");
			IdentifiableType<A> iType = metaModel.entity(A.class);
			if (iType != null) {
				logTrace( "Obtained Non-null Entity A");
				ListAttribute lAttrib = iType.getDeclaredList("lAddress", Address.class);
				if (lAttrib != null) {
					logTrace( "attribute Name = " + lAttrib.getName());
					String name = lAttrib.getName();
					if (name.equals("lAddress")) {
						logTrace( "Received expected result:" + name);
						pass = true;
					} else {
						logErr( "Expected: lAddress, actual:" + name);
					}
				} else {
					logErr( "getDeclaredList(...) returned null");
				}
			} else {
				logErr( "entity(...) returned null");
			}
		} else {
			logErr( "getMetamodel() returned null");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getDeclaredListStringClass failed");
		}
	}

	/*
	 * @testName: getDeclaredListStringClassIllegalArgumentException
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1326;
	 *
	 * @test_Strategy:
	 *
	 */
		public void getDeclaredListStringClassIllegalArgumentException() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logTrace( "Obtained Non-null Metamodel from EntityManager");
			IdentifiableType<A> iType = metaModel.entity(A.class);
			if (iType != null) {
				try {
					iType.getDeclaredList("lAddress_inherited", Address.class);
					logErr( "Did not receive expected IllegalArgumentException");
				} catch (IllegalArgumentException iae) {
					logTrace( "Received expected IllegalArgumentException");
					pass = true;
				} catch (Exception ex) {
					logErr( "Received unexpected exception:", ex);
				}
			} else {
				logErr( "entity(...) returned null");
			}
		} else {
			logErr( "getMetamodel() returned null");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getDeclaredListStringClassIllegalArgumentException failed");
		}
	}

	/*
	 * @testName: getDeclaredListString
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1327;
	 *
	 * @test_Strategy:
	 *
	 */
		public void getDeclaredListString() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logTrace( "Obtained Non-null Metamodel from EntityManager");
			IdentifiableType<A> iType = metaModel.entity(A.class);
			if (iType != null) {
				logTrace( "Obtained Non-null Entity A");
				ListAttribute lAttrib = iType.getDeclaredList("lAddress");
				if (lAttrib != null) {
					logTrace( "attribute Name = " + lAttrib.getName());
					String name = lAttrib.getName();
					if (name.equals("lAddress")) {
						logTrace( "Received expected result:" + name);
						pass = true;
					} else {
						logErr( "Expected: lAddress, actual:" + name);
					}
				} else {
					logErr( "getDeclaredList(...) returned null");
				}
			} else {
				logErr( "entity(...) returned null");
			}
		} else {
			logErr( "getMetamodel() returned null");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getDeclaredListString failed");
		}
	}

	/*
	 * @testName: getDeclaredListStringIllegalArgumentException
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1328;
	 *
	 * @test_Strategy:
	 *
	 */
		public void getDeclaredListStringIllegalArgumentException() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logTrace( "Obtained Non-null Metamodel from EntityManager");
			IdentifiableType<A> iType = metaModel.entity(A.class);
			if (iType != null) {
				try {
					iType.getDeclaredList("lAddress_inherited");
					logErr( "Did not receive expected IllegalArgumentException");
				} catch (IllegalArgumentException iae) {
					logTrace( "Received expected IllegalArgumentException");
					pass = true;
				} catch (Exception ex) {
					logErr( "Received unexpected exception:", ex);
				}
			} else {
				logErr( "entity(...) returned null");
			}
		} else {
			logErr( "getMetamodel() returned null");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getDeclaredListStringIllegalArgumentException failed");
		}
	}

	/*
	 * @testName: getDeclaredMapStringClassClass
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1329;
	 *
	 * @test_Strategy:
	 *
	 */
		public void getDeclaredMapStringClassClass() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logTrace( "Obtained Non-null Metamodel from EntityManager");
			IdentifiableType<A> iType = metaModel.entity(A.class);
			if (iType != null) {
				logTrace( "Obtained Non-null Entity A");
				MapAttribute mAttrib = iType.getDeclaredMap("mAddress", Address.class, String.class);
				if (mAttrib != null) {
					logTrace( "attribute Name = " + mAttrib.getName());
					String name = mAttrib.getName();
					if (name.equals("mAddress")) {
						logTrace( "Received expected result:" + name);
						pass = true;
					} else {
						logErr( "Expected: mAddress, actual:" + name);
					}
				} else {
					logErr( "getDeclaredMap(...) returned null");
				}
			} else {
				logErr( "entity(...) returned null");
			}
		} else {
			logErr( "getMetamodel() returned null");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getDeclaredMapStringClassClass failed");
		}
	}

	/*
	 * @testName: getDeclaredMapStringClassClassIllegalArgumentException
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1330;
	 *
	 * @test_Strategy:
	 *
	 */
		public void getDeclaredMapStringClassClassIllegalArgumentException() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logTrace( "Obtained Non-null Metamodel from EntityManager");
			IdentifiableType<A> iType = metaModel.entity(A.class);
			if (iType != null) {
				try {
					iType.getDeclaredMap("mAddress_inherited", Address.class, String.class);
					logErr( "Did not receive expected IllegalArgumentException");
				} catch (IllegalArgumentException iae) {
					logTrace( "Received expected IllegalArgumentException");
					pass = true;
				} catch (Exception ex) {
					logErr( "Received unexpected exception:", ex);
				}
			} else {
				logErr( "entity(...) returned null");
			}
		} else {
			logErr( "getMetamodel() returned null");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getDeclaredMapStringClassClassIllegalArgumentException failed");
		}
	}

	/*
	 * @testName: getDeclaredMapString
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1331;
	 *
	 * @test_Strategy:
	 *
	 */
		public void getDeclaredMapString() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logTrace( "Obtained Non-null Metamodel from EntityManager");
			IdentifiableType<A> iType = metaModel.entity(A.class);
			if (iType != null) {
				logTrace( "Obtained Non-null Entity A");
				MapAttribute mAttrib = iType.getDeclaredMap("mAddress");
				if (mAttrib != null) {
					logTrace( "attribute Name = " + mAttrib.getName());
					String name = mAttrib.getName();
					if (name.equals("mAddress")) {
						logTrace( "Received expected result:" + name);
						pass = true;
					} else {
						logErr( "Expected: mAddress, actual:" + name);
					}
				} else {
					logErr( "getDeclaredMap(...) returned null");
				}
			} else {
				logErr( "entity(...) returned null");
			}
		} else {
			logErr( "getMetamodel() returned null");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getDeclaredMapString failed");
		}
	}

	/*
	 * @testName: getDeclaredMapStringIllegalArgumentException
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1332;
	 *
	 * @test_Strategy:
	 *
	 */
		public void getDeclaredMapStringIllegalArgumentException() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logTrace( "Obtained Non-null Metamodel from EntityManager");
			IdentifiableType<A> iType = metaModel.entity(A.class);
			if (iType != null) {
				try {
					iType.getDeclaredMap("mAddress_inherited");
					logErr( "Did not receive expected IllegalArgumentException");
				} catch (IllegalArgumentException iae) {
					logTrace( "Received expected IllegalArgumentException");
					pass = true;
				} catch (Exception ex) {
					logErr( "Received unexpected exception:", ex);
				}
			} else {
				logErr( "entity(...) returned null");
			}
		} else {
			logErr( "getMetamodel() returned null");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getDeclaredMapStringIllegalArgumentException failed");
		}
	}

	/*
	 * @testName: getDeclaredSetStringClass
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1334;
	 *
	 * @test_Strategy:
	 *
	 */
		public void getDeclaredSetStringClass() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logTrace( "Obtained Non-null Metamodel from EntityManager");
			IdentifiableType<A> iType = metaModel.entity(A.class);
			if (iType != null) {
				logTrace( "Obtained Non-null Entity A");
				SetAttribute sAttrib = iType.getDeclaredSet("sAddress", Address.class);
				if (sAttrib != null) {
					logTrace( "attribute Name = " + sAttrib.getName());
					String name = sAttrib.getName();
					if (name.equals("sAddress")) {
						logTrace( "Received expected result:" + name);
						pass = true;
					} else {
						logErr( "Expected: sAddress, actual:" + name);
					}
				} else {
					logErr( "getDeclaredSet(...) returned null");
				}
			} else {
				logErr( "entity(...) returned null");
			}
		} else {
			logErr( "getMetamodel() returned null");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getDeclaredSetStringClass failed");
		}
	}

	/*
	 * @testName: getDeclaredSetStringClassIllegalArgumentException
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1335;
	 *
	 * @test_Strategy:
	 *
	 */
		public void getDeclaredSetStringClassIllegalArgumentException() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logTrace( "Obtained Non-null Metamodel from EntityManager");
			IdentifiableType<A> iType = metaModel.entity(A.class);
			if (iType != null) {
				try {
					iType.getDeclaredSet("sAddress_inherited", Address.class);
					logErr( "Did not receive expected IllegalArgumentException");
				} catch (IllegalArgumentException iae) {
					logTrace( "Received expected IllegalArgumentException");
					pass = true;
				} catch (Exception ex) {
					logErr( "Received unexpected exception:", ex);
				}
			} else {
				logErr( "entity(...) returned null");
			}
		} else {
			logErr( "getMetamodel() returned null");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getDeclaredSetStringClassIllegalArgumentException failed");
		}
	}

	/*
	 * @testName: getDeclaredSetString
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1336;
	 *
	 * @test_Strategy:
	 *
	 */
		public void getDeclaredSetString() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logTrace( "Obtained Non-null Metamodel from EntityManager");
			IdentifiableType<A> iType = metaModel.entity(A.class);
			if (iType != null) {
				logTrace( "Obtained Non-null Entity A");
				SetAttribute sAttrib = iType.getDeclaredSet("sAddress");
				if (sAttrib != null) {
					logTrace( "attribute Name = " + sAttrib.getName());
					String name = sAttrib.getName();
					if (name.equals("sAddress")) {
						logTrace( "Received expected result:" + name);
						pass = true;
					} else {
						logErr( "Expected: sAddress, actual:" + name);
					}
				} else {
					logErr( "getDeclaredSet(...) returned null");
				}
			} else {
				logErr( "entity(...) returned null");
			}
		} else {
			logErr( "getMetamodel() returned null");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getDeclaredSetString failed");
		}
	}

	/*
	 * @testName: getDeclaredSetStringIllegalArgumentException
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1337;
	 *
	 * @test_Strategy:
	 *
	 */
		public void getDeclaredSetStringIllegalArgumentException() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logTrace( "Obtained Non-null Metamodel from EntityManager");
			IdentifiableType<A> iType = metaModel.entity(A.class);
			if (iType != null) {
				try {
					iType.getDeclaredSet("sAddress_inherited");
					logErr( "Did not receive expected IllegalArgumentException");
				} catch (IllegalArgumentException iae) {
					logTrace( "Received expected IllegalArgumentException");
					pass = true;
				} catch (Exception ex) {
					logErr( "Received unexpected exception:", ex);
				}
			} else {
				logErr( "entity(...) returned null");
			}
		} else {
			logErr( "getMetamodel() returned null");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getDeclaredSetStringIllegalArgumentException failed");
		}
	}

	/*
	 * @testName: getDeclaredSingularAttributeStringClass
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1338;
	 *
	 * @test_Strategy:
	 *
	 */
		public void getDeclaredSingularAttributeStringClass() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logTrace( "Obtained Non-null Metamodel from EntityManager");
			IdentifiableType<A> iType = metaModel.entity(A.class);
			if (iType != null) {
				logTrace( "Obtained Non-null Entity A");
				SingularAttribute<A, Integer> singAttrib = iType.getDeclaredSingularAttribute("value", Integer.class);
				if (singAttrib != null) {
					logTrace( "attribute Name = " + singAttrib.getName());
					String name = singAttrib.getName();
					if (name.equals("value")) {
						logTrace( "Received expected result:" + name);
						pass = true;
					} else {
						logErr( "Expected: value, actual:" + name);
					}
				} else {
					logErr( "getDeclaredSingularAttribute(...) returned null");
				}
			} else {
				logErr( "entity(...) returned null");
			}
		} else {
			logErr( "getMetamodel() returned null");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getDeclaredSingularAttributeStringClass failed");
		}
	}

	/*
	 * @testName: getDeclaredSingularAttributeStringClassIllegalArgumentException
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1339;
	 *
	 * @test_Strategy:
	 *
	 */
		public void getDeclaredSingularAttributeStringClassIllegalArgumentException() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logTrace( "Obtained Non-null Metamodel from EntityManager");
			IdentifiableType<A> iType = metaModel.entity(A.class);
			if (iType != null) {
				logTrace( "Obtained Non-null Entity A");
				try {
					iType.getDeclaredSingularAttribute("name", String.class);
					logErr( "Did not receive expected IllegalArgumentException");
				} catch (IllegalArgumentException iae) {
					logTrace( "Received expected IllegalArgumentException");
					pass = true;
				} catch (Exception ex) {
					logErr( "Received unexpected exception:", ex);
				}
			} else {
				logErr( "entity(...) returned null");
			}
		} else {
			logErr( "getMetamodel() returned null");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getDeclaredSingularAttributeStringClassIllegalArgumentException failed");
		}
	}

	/*
	 * @testName: getDeclaredSingularAttributeString
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1340;
	 *
	 * @test_Strategy:
	 *
	 */
		public void getDeclaredSingularAttributeString() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logTrace( "Obtained Non-null Metamodel from EntityManager");
			IdentifiableType<A> iType = metaModel.entity(A.class);
			if (iType != null) {
				logTrace( "Obtained Non-null Entity A");
				SingularAttribute singAttrib = iType.getDeclaredSingularAttribute("value");
				if (singAttrib != null) {
					logTrace( "attribute Name = " + singAttrib.getName());
					String name = singAttrib.getName();
					if (name.equals("value")) {
						logTrace( "Received expected result:" + name);
						pass = true;
					} else {
						logErr( "Expected: value, actual:" + name);
					}
				} else {
					logErr( "getDeclaredSingularAttribute(...) returned null");
				}
			} else {
				logErr( "entity(...) returned null");
			}
		} else {
			logErr( "getMetamodel() returned null");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getDeclaredSingularAttributeString failed");
		}
	}

	/*
	 * @testName: getDeclaredSingularAttributeStringIllegalArgumentException
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1341;
	 *
	 * @test_Strategy:
	 *
	 */
		public void getDeclaredSingularAttributeStringIllegalArgumentException() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logTrace( "Obtained Non-null Metamodel from EntityManager");
			IdentifiableType<A> iType = metaModel.entity(A.class);
			if (iType != null) {
				logTrace( "Obtained Non-null Entity A");
				try {
					iType.getDeclaredSingularAttribute("name");
					logErr( "Did not receive expected IllegalArgumentException");
				} catch (IllegalArgumentException iae) {
					logTrace( "Received expected IllegalArgumentException");
					pass = true;
				} catch (Exception ex) {
					logErr( "Received unexpected exception:", ex);
				}
			} else {
				logErr( "entity(...) returned null");
			}
		} else {
			logErr( "getMetamodel() returned null");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getDeclaredSingularAttributeStringIllegalArgumentException failed");
		}
	}

	/*
	 * @testName: getDeclaredSingularAttributes
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1342;
	 * Updated for https://github.com/jakartaee/platform-tck/issues/2497
	 *
	 * @test_Strategy:
	 *
	 */
	public void getDeclaredSingularAttributes() throws Exception {
		boolean pass = false;

		List<String> expected = new ArrayList<String>();

		expected.add("id");
		expected.add("value");
		Collections.sort(expected);

		List<String> actual = new ArrayList<String>();

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logTrace( "Obtained Non-null Metamodel from EntityManager");
			IdentifiableType<A> iType = metaModel.entity(A.class);
			if (iType != null) {
				logTrace( "Obtained Non-null Entity A");
				Set set = iType.getDeclaredSingularAttributes();
				if (set != null) {
					if (set.size() > 0) {
						for (Iterator i = set.iterator(); i.hasNext();) {
							Attribute attrib = (Attribute) i.next();
							actual.add(attrib.getName());
						}
						Collections.sort(actual);

						if (actual.containsAll(expected)) {
							logTrace( "Received expected attributes");
							for (String attribName : expected) {
								logTrace( "attrib:" + attribName);
							}
							pass = true;
						} else {
							logErr( "Received unexpected attributes");
							logErr( "Expected(" + expected.size() + "):");
							for (String attribName : expected) {
								logErr( "attrib:" + attribName);
							}
							logErr( "Actual(" + actual.size() + "):");
							for (String attribName : actual) {
								logErr( "attrib:" + attribName);
							}
						}
					} else {
						logErr( "getDeclaredSingularAttributes() returned 0 results");
					}
				} else {
					logErr( "getDeclaredSingularAttributes() returned null");
				}
			} else {
				logErr( "entity(...) returned null");
			}
		} else {
			logErr( "getMetamodel() returned null");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getDeclaredSingularAttributes failed");
		}
	}

	/*
	 * @testName: getListStringClass
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1343;
	 *
	 * @test_Strategy:
	 *
	 */
		public void getListStringClass() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logTrace( "Obtained Non-null Metamodel from EntityManager");
			IdentifiableType<A> iType = metaModel.entity(A.class);
			if (iType != null) {
				logTrace( "Obtained Non-null Entity A");
				ListAttribute lAttrib = iType.getList("lAddress", Address.class);
				if (lAttrib != null) {
					logTrace( "attribute Name = " + lAttrib.getName());
					String name = lAttrib.getName();
					if (name.equals("lAddress")) {
						logTrace( "Received expected result:" + name);
						pass = true;
					} else {
						logErr( "Expected: lAddress, actual:" + name);
					}
				} else {
					logErr( "getList(...) returned null");
				}
			} else {
				logErr( "entity(...) returned null");
			}
		} else {
			logErr( "getMetamodel() returned null");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getListStringClass failed");
		}
	}

	/*
	 * @testName: getListStringClassIllegalArgumentException
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1344;
	 *
	 * @test_Strategy:
	 *
	 */
		public void getListStringClassIllegalArgumentException() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logTrace( "Obtained Non-null Metamodel from EntityManager");
			IdentifiableType<A> iType = metaModel.entity(A.class);
			if (iType != null) {
				try {
					iType.getList("doesnotexist", Address.class);
					logErr( "Did not receive expected IllegalArgumentException");
				} catch (IllegalArgumentException iae) {
					logTrace( "Received expected IllegalArgumentException");
					pass = true;
				} catch (Exception ex) {
					logErr( "Received unexpected exception:", ex);
				}
			} else {
				logErr( "entity(...) returned null");
			}
		} else {
			logErr( "getMetamodel() returned null");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getListStringClassIllegalArgumentException failed");
		}
	}

	/*
	 * @testName: getListString
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1345;
	 *
	 * @test_Strategy:
	 *
	 */
		public void getListString() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logTrace( "Obtained Non-null Metamodel from EntityManager");
			IdentifiableType<A> iType = metaModel.entity(A.class);
			if (iType != null) {
				logTrace( "Obtained Non-null Entity A");
				ListAttribute lAttrib = iType.getList("lAddress");
				if (lAttrib != null) {
					logTrace( "attribute Name = " + lAttrib.getName());
					String name = lAttrib.getName();
					if (name.equals("lAddress")) {
						logTrace( "Received expected result:" + name);
						pass = true;
					} else {
						logErr( "Expected: lAddress, actual:" + name);
					}
				} else {
					logErr( "getList(...) returned null");
				}
			} else {
				logErr( "entity(...) returned null");
			}
		} else {
			logErr( "getMetamodel() returned null");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getListString failed");
		}
	}

	/*
	 * @testName: getListStringIllegalArgumentException
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1346;
	 *
	 * @test_Strategy:
	 *
	 */
		public void getListStringIllegalArgumentException() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logTrace( "Obtained Non-null Metamodel from EntityManager");
			IdentifiableType<A> iType = metaModel.entity(A.class);
			if (iType != null) {
				try {
					iType.getDeclaredList("doesnotexist");
					logErr( "Did not receive expected IllegalArgumentException");
				} catch (IllegalArgumentException iae) {
					logTrace( "Received expected IllegalArgumentException");
					pass = true;
				} catch (Exception ex) {
					logErr( "Received unexpected exception:", ex);
				}
			} else {
				logErr( "entity(...) returned null");
			}
		} else {
			logErr( "getMetamodel() returned null");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getListStringIllegalArgumentException failed");
		}
	}

	/*
	 * @testName: getMapStringClassClass
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1347;
	 *
	 * @test_Strategy:
	 *
	 */
		public void getMapStringClassClass() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logTrace( "Obtained Non-null Metamodel from EntityManager");
			IdentifiableType<A> iType = metaModel.entity(A.class);
			if (iType != null) {
				logTrace( "Obtained Non-null Entity A");
				MapAttribute mAttrib = iType.getMap("mAddress", Address.class, String.class);
				if (mAttrib != null) {
					logTrace( "attribute Name = " + mAttrib.getName());
					String name = mAttrib.getName();
					if (name.equals("mAddress")) {
						logTrace( "Received expected result:" + name);
						pass = true;
					} else {
						logErr( "Expected: mAddress, actual:" + name);
					}
				} else {
					logErr( "getMap(...) returned null");
				}
			} else {
				logErr( "entity(...) returned null");
			}
		} else {
			logErr( "getMetamodel() returned null");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getMapStringClassClass failed");
		}
	}

	/*
	 * @testName: getMapStringClassClassIllegalArgumentException
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1348;
	 *
	 * @test_Strategy:
	 *
	 */
		public void getMapStringClassClassIllegalArgumentException() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logTrace( "Obtained Non-null Metamodel from EntityManager");
			IdentifiableType<A> iType = metaModel.entity(A.class);
			if (iType != null) {
				try {
					iType.getMap("doesnotexist", Address.class, String.class);
					logErr( "Did not receive expected IllegalArgumentException");
				} catch (IllegalArgumentException iae) {
					logTrace( "Received expected IllegalArgumentException");
					pass = true;
				} catch (Exception ex) {
					logErr( "Received unexpected exception:", ex);
				}
			} else {
				logErr( "entity(...) returned null");
			}
		} else {
			logErr( "getMetamodel() returned null");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getMapStringClassClassIllegalArgumentException failed");
		}
	}

	/*
	 * @testName: getMapString
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1349;
	 *
	 * @test_Strategy:
	 *
	 */
		public void getMapString() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logTrace( "Obtained Non-null Metamodel from EntityManager");
			IdentifiableType<A> iType = metaModel.entity(A.class);
			if (iType != null) {
				logTrace( "Obtained Non-null Entity A");
				MapAttribute mAttrib = iType.getMap("mAddress");
				if (mAttrib != null) {
					logTrace( "attribute Name = " + mAttrib.getName());
					String name = mAttrib.getName();
					if (name.equals("mAddress")) {
						logTrace( "Received expected result:" + name);
						pass = true;
					} else {
						logErr( "Expected: mAddress, actual:" + name);
					}
				} else {
					logErr( "getMap(...) returned null");
				}
			} else {
				logErr( "entity(...) returned null");
			}
		} else {
			logErr( "getMetamodel() returned null");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getMapString failed");
		}
	}

	/*
	 * @testName: getMapStringIllegalArgumentException
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1350;
	 *
	 * @test_Strategy:
	 *
	 */
		public void getMapStringIllegalArgumentException() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logTrace( "Obtained Non-null Metamodel from EntityManager");
			IdentifiableType<A> iType = metaModel.entity(A.class);
			if (iType != null) {
				try {
					iType.getMap("doesnotexist");
					logErr( "Did not receive expected IllegalArgumentException");
				} catch (IllegalArgumentException iae) {
					logTrace( "Received expected IllegalArgumentException");
					pass = true;
				} catch (Exception ex) {
					logErr( "Received unexpected exception:", ex);
				}
			} else {
				logErr( "entity(...) returned null");
			}
		} else {
			logErr( "getMetamodel() returned null");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getMapStringIllegalArgumentException failed");
		}
	}

	/*
	 * @testName: getPluralAttributes
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1351;
	 *
	 * @test_Strategy:
	 *
	 */
		public void getPluralAttributes() throws Exception {
		boolean pass = false;

		List<String> expected = new ArrayList<String>();

		expected.add("cAddress");
		expected.add("cAddress_inherited");
		expected.add("lAddress");
		expected.add("lAddress_inherited");
		expected.add("mAddress");
		expected.add("mAddress_inherited");
		expected.add("sAddress");
		expected.add("sAddress_inherited");
		Collections.sort(expected);

		List<String> actual = new ArrayList<String>();

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logTrace( "Obtained Non-null Metamodel from EntityManager");
			IdentifiableType<A> iType = metaModel.entity(A.class);
			if (iType != null) {
				logTrace( "Obtained Non-null Entity A");
				Set set = iType.getPluralAttributes();
				if (set != null) {
					if (set.size() > 0) {
						for (Iterator i = set.iterator(); i.hasNext();) {
							Attribute attrib = (Attribute) i.next();
							actual.add(attrib.getName());
						}
						Collections.sort(actual);

						if (expected.containsAll(actual) && actual.containsAll(expected)
								&& expected.size() == actual.size()) {

							logTrace( "Received expected attributes");
							for (String attribName : expected) {
								logTrace( "attrib:" + attribName);
							}
							pass = true;
						} else {
							logErr( "Received unexpected attributes");
							logErr( "Expected(" + expected.size() + "):");
							for (String attribName : expected) {
								logErr( "attrib:" + attribName);
							}
							logErr( "Actual(" + actual.size() + "):");
							for (String attribName : actual) {
								logErr( "attrib:" + attribName);
							}
						}
					} else {
						logErr( "getPluralAttributes() returned 0 results");
					}
				} else {
					logErr( "getPluralAttributes() returned null");
				}
			} else {
				logErr( "entity(...) returned null");
			}
		} else {
			logErr( "getMetamodel() returned null");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getPluralAttributes failed");
		}
	}

	/*
	 * @testName: getDeclaredPluralAttributes
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1333;
	 *
	 * @test_Strategy:
	 *
	 */
		public void getDeclaredPluralAttributes() throws Exception {
		boolean pass = false;

		List<String> expected = new ArrayList<String>();

		expected.add("cAddress");
		expected.add("lAddress");
		expected.add("mAddress");
		expected.add("sAddress");
		Collections.sort(expected);

		List<String> actual = new ArrayList<String>();

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logTrace( "Obtained Non-null Metamodel from EntityManager");
			IdentifiableType<A> iType = metaModel.entity(A.class);
			if (iType != null) {
				logTrace( "Obtained Non-null Entity A");
				Set set = iType.getDeclaredPluralAttributes();
				if (set != null) {
					if (set.size() > 0) {
						for (Iterator i = set.iterator(); i.hasNext();) {
							Attribute attrib = (Attribute) i.next();
							actual.add(attrib.getName());
						}
						Collections.sort(actual);

						if (expected.containsAll(actual) && actual.containsAll(expected)
								&& expected.size() == actual.size()) {

							logTrace( "Received expected attributes");
							for (String attribName : expected) {
								logTrace( "attrib:" + attribName);
							}
							pass = true;
						} else {
							logErr( "Received unexpected attributes");
							logErr( "Expected(" + expected.size() + "):");
							for (String attribName : expected) {
								logErr( "attrib:" + attribName);
							}
							logErr( "Actual(" + actual.size() + "):");
							for (String attribName : actual) {
								logErr( "attrib:" + attribName);
							}
						}
					} else {
						logErr( "getPluralAttributes() returned 0 results");
					}
				} else {
					logErr( "getPluralAttributes() returned null");
				}
			} else {
				logErr( "entity(...) returned null");
			}
		} else {
			logErr( "getMetamodel() returned null");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getDeclaredPluralAttributes failed");
		}
	}

	/*
	 * @testName: getSetStringClass
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1352;
	 *
	 * @test_Strategy:
	 *
	 */
		public void getSetStringClass() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logTrace( "Obtained Non-null Metamodel from EntityManager");
			IdentifiableType<A> iType = metaModel.entity(A.class);
			if (iType != null) {
				logTrace( "Obtained Non-null Entity A");
				SetAttribute sAttrib = iType.getSet("sAddress", Address.class);
				if (sAttrib != null) {
					logTrace( "attribute Name = " + sAttrib.getName());
					String name = sAttrib.getName();
					if (name.equals("sAddress")) {
						logTrace( "Received expected result:" + name);
						pass = true;
					} else {
						logErr( "Expected: sAddress, actual:" + name);
					}
				} else {
					logErr( "getSet(...) returned null");
				}
			} else {
				logErr( "entity(...) returned null");
			}
		} else {
			logErr( "getMetamodel() returned null");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getSetStringClass failed");
		}
	}

	/*
	 * @testName: getSetStringClassIllegalArgumentException
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1353;
	 *
	 * @test_Strategy:
	 *
	 */
		public void getSetStringClassIllegalArgumentException() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logTrace( "Obtained Non-null Metamodel from EntityManager");
			IdentifiableType<A> iType = metaModel.entity(A.class);
			if (iType != null) {
				try {
					iType.getSet("doesnotexist", Address.class);
					logErr( "Did not receive expected IllegalArgumentException");
				} catch (IllegalArgumentException iae) {
					logTrace( "Received expected IllegalArgumentException");
					pass = true;
				} catch (Exception ex) {
					logErr( "Received unexpected exception:", ex);
				}
			} else {
				logErr( "entity(...) returned null");
			}
		} else {
			logErr( "getMetamodel() returned null");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getSetStringClassIllegalArgumentException failed");
		}
	}

	/*
	 * @testName: getSetString
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1354;
	 *
	 * @test_Strategy:
	 *
	 */
		public void getSetString() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logTrace( "Obtained Non-null Metamodel from EntityManager");
			IdentifiableType<A> iType = metaModel.entity(A.class);
			if (iType != null) {
				logTrace( "Obtained Non-null Entity A");
				SetAttribute sAttrib = iType.getSet("sAddress");
				if (sAttrib != null) {
					logTrace( "attribute Name = " + sAttrib.getName());
					String name = sAttrib.getName();
					if (name.equals("sAddress")) {
						logTrace( "Received expected result:" + name);
						pass = true;
					} else {
						logErr( "Expected: sAddress, actual:" + name);
					}
				} else {
					logErr( "getSet(...) returned null");
				}
			} else {
				logErr( "entity(...) returned null");
			}
		} else {
			logErr( "getMetamodel() returned null");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getSetString failed");
		}
	}

	/*
	 * @testName: getSetStringIllegalArgumentException
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1355;
	 *
	 * @test_Strategy:
	 *
	 */
		public void getSetStringIllegalArgumentException() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logTrace( "Obtained Non-null Metamodel from EntityManager");
			IdentifiableType<A> iType = metaModel.entity(A.class);
			if (iType != null) {
				try {
					iType.getSet("doesnotexist");
					logErr( "Did not receive expected IllegalArgumentException");
				} catch (IllegalArgumentException iae) {
					logTrace( "Received expected IllegalArgumentException");
					pass = true;
				} catch (Exception ex) {
					logErr( "Received unexpected exception:", ex);
				}
			} else {
				logErr( "entity(...) returned null");
			}
		} else {
			logErr( "getMetamodel() returned null");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getSetStringIllegalArgumentException failed");
		}
	}

	/*
	 * @testName: getSingularAttributeStringClass
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1356;
	 *
	 * @test_Strategy:
	 *
	 */
		public void getSingularAttributeStringClass() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logTrace( "Obtained Non-null Metamodel from EntityManager");
			IdentifiableType<A> iType = metaModel.entity(A.class);
			if (iType != null) {
				logTrace( "Obtained Non-null Entity A");
				SingularAttribute singAttrib = iType.getSingularAttribute("name", String.class);
				if (singAttrib != null) {
					logTrace( "attribute Name = " + singAttrib.getName());
					String name = singAttrib.getName();
					if (name.equals("name")) {
						logTrace( "Received expected result:" + name);
						pass = true;
					} else {
						logErr( "Expected: name, actual:" + name);
					}
				} else {
					logErr( "getSingularAttribute(...) returned null");
				}
			} else {
				logErr( "entity(...) returned null");
			}
		} else {
			logErr( "getMetamodel() returned null");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getSingularAttributeStringClass failed");
		}
	}

	/*
	 * @testName: getSingularAttributeStringClassIllegalArgumentException
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1357;
	 *
	 * @test_Strategy:
	 *
	 */
		public void getSingularAttributeStringClassIllegalArgumentException() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logTrace( "Obtained Non-null Metamodel from EntityManager");
			IdentifiableType<A> iType = metaModel.entity(A.class);
			if (iType != null) {
				logTrace( "Obtained Non-null Entity A");
				try {
					iType.getSingularAttribute("doesnotexist", Address.class);
					logErr( "Did not receive expected IllegalArgumentException");
				} catch (IllegalArgumentException iae) {
					logTrace( "Received expected IllegalArgumentException");
					pass = true;
				} catch (Exception ex) {
					logErr( "Received unexpected exception:", ex);
				}
			} else {
				logErr( "entity(...) returned null");
			}
		} else {
			logErr( "getMetamodel() returned null");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getSingularAttributeStringClassIllegalArgumentException failed");
		}
	}

	/*
	 * @testName: getSingularAttributeString
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1358;
	 *
	 * @test_Strategy:
	 *
	 */
		public void getSingularAttributeString() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logTrace( "Obtained Non-null Metamodel from EntityManager");
			IdentifiableType<A> iType = metaModel.entity(A.class);
			if (iType != null) {
				logTrace( "Obtained Non-null Entity A");
				SingularAttribute singAttrib = iType.getSingularAttribute("name");
				if (singAttrib != null) {
					logTrace( "attribute Name = " + singAttrib.getName());
					String name = singAttrib.getName();
					if (name.equals("name")) {
						logTrace( "Received expected result:" + name);
						pass = true;
					} else {
						logErr( "Expected: name, actual:" + name);
					}
				} else {
					logErr( "getSingularAttribute(...) returned null");
				}
			} else {
				logErr( "entity(...) returned null");
			}
		} else {
			logErr( "getMetamodel() returned null");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getSingularAttributeString failed");
		}
	}

	/*
	 * @testName: getSingularAttributeStringIllegalArgumentException
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1359;
	 *
	 * @test_Strategy:
	 *
	 */
		public void getSingularAttributeStringIllegalArgumentException() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logTrace( "Obtained Non-null Metamodel from EntityManager");
			IdentifiableType<A> iType = metaModel.entity(A.class);
			if (iType != null) {
				logTrace( "Obtained Non-null Entity A");
				try {
					iType.getSingularAttribute("doesnotexist");
					logErr( "Did not receive expected IllegalArgumentException");
				} catch (IllegalArgumentException iae) {
					logTrace( "Received expected IllegalArgumentException");
					pass = true;
				} catch (Exception ex) {
					logErr( "Received unexpected exception:", ex);
				}
			} else {
				logErr( "entity(...) returned null");
			}
		} else {
			logErr( "getMetamodel() returned null");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getSingularAttributeStringIllegalArgumentException failed");
		}
	}

	/*
	 * @testName: getSingularAttributes
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1360;
	 *
	 * @test_Strategy:
	 *
	 */
		public void getSingularAttributes() throws Exception {
		boolean pass = false;

		List<String> expected = new ArrayList<String>();

		expected.add("id");
		expected.add("name");
		expected.add("value");
		Collections.sort(expected);

		List<String> actual = new ArrayList<String>();

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logTrace( "Obtained Non-null Metamodel from EntityManager");
			IdentifiableType<A> iType = metaModel.entity(A.class);
			if (iType != null) {
				logTrace( "Obtained Non-null Entity A");
				Set set = iType.getSingularAttributes();
				if (set != null) {
					if (set.size() > 0) {
						for (Iterator i = set.iterator(); i.hasNext();) {
							Attribute attrib = (Attribute) i.next();
							actual.add(attrib.getName());
						}
						Collections.sort(actual);

						if (expected.containsAll(actual) && actual.containsAll(expected)
								&& expected.size() == actual.size()) {

							logTrace( "Received expected attributes");
							for (String attribName : expected) {
								logTrace( "attrib:" + attribName);
							}
							pass = true;
						} else {
							logErr( "Received unexpected attributes");
							logErr( "Expected(" + expected.size() + "):");
							for (String attribName : expected) {
								logErr( "attrib:" + attribName);
							}
							logErr( "Actual(" + actual.size() + "):");
							for (String attribName : actual) {
								logErr( "attrib:" + attribName);
							}
						}
					} else {
						logErr( "getSingularAttributes() returned 0 results");
					}
				} else {
					logErr( "getSingularAttributes(...) returned null");
				}
			} else {
				logErr( "entity(...) returned null");
			}
		} else {
			logErr( "getMetamodel() returned null");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getSingularAttributes failed");
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
