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

package ee.jakarta.tck.persistence.core.metamodelapi.mappedsuperclasstype;


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
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.IdentifiableType;
import jakarta.persistence.metamodel.MappedSuperclassType;
import jakarta.persistence.metamodel.Metamodel;
import jakarta.persistence.metamodel.SingularAttribute;

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
	 * @testName: mappedSuperclassType
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1428;
	 *
	 * @test_Strategy:
	 *
	 */
		public void mappedSuperclassType() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			pass = false;
			logTrace( "Obtained Non-null Metamodel from EntityManager");
			Set<EntityType<?>> aSet = metaModel.getEntities();
			if (aSet != null) {
				logTrace( "Obtained Non-null Set of EntityType");
				for (EntityType mType : aSet) {
					logTrace( "EntityType:" + mType.getJavaType().getName());

					IdentifiableType<? super FullTimeEmployee> idType = mType.getSupertype();
					if (idType != null) {
						logTrace( "IdentifiableType:" + idType.getJavaType().getName());
						if (idType instanceof MappedSuperclassType) {
							logTrace(
									"type is instance of MappedSuperClassType:" + idType.getJavaType().getName());
							pass = true;
						}
					}

				}
			}
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("mappedSuperclassType failed");
		}
	}

	/*
	 * @testName: getDeclaredId
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1419;
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
			EntityType<FullTimeEmployee2> eType = metaModel.entity(FullTimeEmployee2.class);
			if (eType != null) {
				logTrace( "Obtained Non-null EntityType");
				IdentifiableType idType = eType.getSupertype();
				if (idType != null) {
					SingularAttribute<FullTimeEmployee, Integer> idAttrib = idType.getDeclaredId(String.class);
					if (idAttrib != null) {
						String name = idAttrib.getType().getJavaType().getName();
						if (name.equals("java.lang.String")) {
							logTrace( "Received:" + name);
							pass = true;
						} else {
							logErr( "Expected java.lang.String, actual:" + name);
						}
					} else {
						logErr( "getDeclaredId(...) returned null");
					}
				} else {
					logErr( "getSupertype() returned null");
				}
			} else {
				logErr( "getEntity(...) returned null");
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
	 * @assertion_ids: PERSISTENCE:JAVADOC:1420;
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
			EntityType<FullTimeEmployee2> eType = metaModel.entity(FullTimeEmployee2.class);
			if (eType != null) {
				logTrace( "Obtained Non-null EntityType");
				IdentifiableType idType = eType.getSupertype();
				if (idType != null) {
					try {
						idType.getDeclaredId(Date.class);
						logTrace( "Did not receive IllegalArgumentException");
					} catch (IllegalArgumentException iae) {
						logTrace( "Received expected IllegalArgumentException");
						pass = true;
					} catch (Exception e) {
						logErr( "Received unexpected exception", e);
					}
				} else {
					logErr( "getSupertype() returned null");
				}
			} else {
				logErr( "getEntity(...) returned null");
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
	 * @assertion_ids: PERSISTENCE:JAVADOC:1421;
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
			EntityType<FullTimeEmployee2> eType = metaModel.entity(FullTimeEmployee2.class);
			if (eType != null) {
				logTrace( "Obtained Non-null EntityType");
				IdentifiableType idType = eType.getSupertype();
				if (idType != null) {
					SingularAttribute<FullTimeEmployee, Integer> idAttrib = idType.getDeclaredVersion(Integer.class);
					if (idAttrib != null) {
						String name = idAttrib.getType().getJavaType().getName();
						if (name.equals("java.lang.Integer")) {
							logTrace( "Received:" + name);
							pass = true;
						} else {
							logErr( "Expected java.lang.Integer, actual:" + name);
						}
					} else {
						logErr( "getDeclaredId(...) returned null");
					}
				} else {
					logErr( "getSupertype() returned null");
				}
			} else {
				logErr( "getEntity(...) returned null");
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
	 * @assertion_ids: PERSISTENCE:JAVADOC:1422;
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
			EntityType<FullTimeEmployee2> eType = metaModel.entity(FullTimeEmployee2.class);
			if (eType != null) {
				logTrace( "Obtained Non-null EntityType");
				IdentifiableType idType = eType.getSupertype();
				if (idType != null) {
					try {
						idType.getDeclaredVersion(Date.class);
						logTrace( "Did not receive IllegalArgumentException");
					} catch (IllegalArgumentException iae) {
						logTrace( "Received expected IllegalArgumentException");
						pass = true;
					} catch (Exception e) {
						logErr( "Received unexpected exception", e);
					}
				} else {
					logErr( "getSupertype() returned null");
				}
			} else {
				logErr( "getEntity(...) returned null");
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
	 * @testName: getId
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1423;
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
			EntityType<FullTimeEmployee2> eType = metaModel.entity(FullTimeEmployee2.class);
			if (eType != null) {
				logTrace( "Obtained Non-null EntityType");
				IdentifiableType idType = eType.getSupertype();
				if (idType != null) {
					SingularAttribute<FullTimeEmployee, Integer> idAttrib = idType.getId(String.class);
					if (idAttrib != null) {
						String name = idAttrib.getType().getJavaType().getName();
						if (name.equals("java.lang.String")) {
							logTrace( "Received:" + name);
							pass = true;
						} else {
							logErr( "Expected java.lang.String, actual:" + name);
						}
					} else {
						logErr( "getDeclaredId(...) returned null");
					}
				} else {
					logErr( "getSupertype() returned null");
				}
			} else {
				logErr( "getEntity(...) returned null");
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
	 * @assertion_ids: PERSISTENCE:JAVADOC:1424;
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
			EntityType<FullTimeEmployee2> eType = metaModel.entity(FullTimeEmployee2.class);
			if (eType != null) {
				logTrace( "Obtained Non-null EntityType");
				IdentifiableType idType = eType.getSupertype();
				if (idType != null) {
					try {
						idType.getId(Date.class);
						logTrace( "Did not receive IllegalArgumentException");
					} catch (IllegalArgumentException iae) {
						logTrace( "Received expected IllegalArgumentException");
						pass = true;
					} catch (Exception e) {
						logErr( "Received unexpected exception", e);
					}
				} else {
					logErr( "getSupertype() returned null");
				}
			} else {
				logErr( "getEntity(...) returned null");
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
	 * @testName: getIdClassAttributes
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1425;
	 * 
	 * @test_Strategy:
	 *
	 */
		public void getIdClassAttributes() throws Exception {
		boolean pass = false;

		List<String> expected = new ArrayList<String>();
		expected.add("id");
		Collections.sort(expected);

		List<String> actual = new ArrayList<String>();

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logTrace( "Obtained Non-null Metamodel from EntityManager");
			EntityType<FullTimeEmployee> eType = metaModel.entity(FullTimeEmployee.class);
			if (eType != null) {
				logTrace( "Obtained Non-null EntityType");
				IdentifiableType idType = eType.getSupertype();
				if (idType != null) {
					Set<SingularAttribute> idAttribSet = idType.getIdClassAttributes();
					if (idAttribSet != null) {
						if (idAttribSet.size() > 0) {
							for (Iterator i = idAttribSet.iterator(); i.hasNext();) {
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
								logErr( "Received Unexpected attributes");
								logErr( "Expected:");
								for (String attribName : expected) {
									logErr( "attrib:" + attribName);
								}
								logErr( "Actual:");
								for (String attribName : actual) {
									logErr( "attrib:" + attribName);
								}
							}
						} else {
							logErr( "getIdClassAttributes() returned 0 results");
						}
					} else {
						logErr( "getIdClassAttributes() returned null");
					}
				} else {
					logErr( "getSupertype() returned null");
				}
			} else {
				logErr( "getEntity(...) returned null");
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
	 * @assertion_ids: PERSISTENCE:JAVADOC:1426;
	 * 
	 * @test_Strategy:
	 *
	 */
		public void getIdClassAttributesIllegalArgumentException() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logTrace( "Obtained Non-null Metamodel from EntityManager");
			EntityType<FullTimeEmployee2> eType = metaModel.entity(FullTimeEmployee2.class);
			if (eType != null) {
				logTrace( "Obtained Non-null EntityType");
				IdentifiableType idType = eType.getSupertype();
				if (idType != null) {
					try {
						idType.getIdClassAttributes();
						logTrace( "Did not receive IllegalArgumentException");
					} catch (IllegalArgumentException iae) {
						logTrace( "Received expected IllegalArgumentException");
						pass = true;
					} catch (Exception e) {
						logErr( "Received unexpected exception", e);
					}
				} else {
					logErr( "getSupertype() returned null");
				}
			} else {
				logErr( "getEntity(...) returned null");
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
	 * @assertion_ids: PERSISTENCE:JAVADOC:1427;
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
			EntityType<FullTimeEmployee2> eType = metaModel.entity(FullTimeEmployee2.class);
			if (eType != null) {
				logTrace( "Obtained Non-null EntityType");
				IdentifiableType idType = eType.getSupertype();
				if (idType != null) {
					String name = idType.getIdType().getJavaType().getName();

					if (name.equals("java.lang.String")) {
						logTrace( "Received expected: " + name);
						pass = true;
					} else {
						logErr( "Expected java.lang.String, actual:" + name);
					}

				} else {
					logErr( "getSupertype() returned null");
				}
			} else {
				logErr( "getEntity(...) returned null");
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
	 * @testName: getVersion
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1429;
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
			EntityType<FullTimeEmployee2> eType = metaModel.entity(FullTimeEmployee2.class);
			if (eType != null) {
				logTrace( "Obtained Non-null EntityType");
				IdentifiableType idType = eType.getSupertype();
				if (idType != null) {
					SingularAttribute idAttrib = eType.getVersion(Integer.class);
					String name = idAttrib.getType().getJavaType().getName();
					if (name.equals("java.lang.Integer")) {
						pass = true;
					} else {
						logErr( "Expected java.lang.Integer, actual:" + name);
					}
				} else {
					logErr( "getSupertype() returned null");
				}
			} else {
				logErr( "getEntity(...) returned null");
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
	 * @assertion_ids: PERSISTENCE:JAVADOC:1430;
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
			EntityType<FullTimeEmployee2> eType = metaModel.entity(FullTimeEmployee2.class);
			if (eType != null) {
				logTrace( "Obtained Non-null EntityType");
				IdentifiableType idType = eType.getSupertype();
				if (idType != null) {
					try {
						eType.getVersion(Date.class);
						logTrace( "Did not receive IllegalArgumentException");
					} catch (IllegalArgumentException iae) {
						logTrace( "Received expected IllegalArgumentException");
						pass = true;
					} catch (Exception e) {
						logErr( "Received unexpected exception", e);
					}
				} else {
					logErr( "getSupertype() returned null");
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
	 * @testName: hasSingleIdAttribute
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1431;
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
			EntityType<FullTimeEmployee2> eType = metaModel.entity(FullTimeEmployee2.class);
			if (eType != null) {
				logTrace( "Obtained Non-null EntityType");
				IdentifiableType idType = eType.getSupertype();
				if (idType != null) {
					boolean hasSingleIdAttribute = idType.hasSingleIdAttribute();
					if (hasSingleIdAttribute) {
						pass = true;
						logTrace( "hasSingleIdAttribute() returned" + hasSingleIdAttribute);
					} else {
						logErr( "Expected: false, actual:" + hasSingleIdAttribute);

					}
				} else {
					logErr( "getSupertype() returned null");
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
	 * @assertion_ids: PERSISTENCE:JAVADOC:1432;
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
			EntityType<FullTimeEmployee2> eType = metaModel.entity(FullTimeEmployee2.class);
			if (eType != null) {
				logTrace( "Obtained Non-null EntityType");
				IdentifiableType idType = eType.getSupertype();
				if (idType != null) {
					boolean hasVersionAttribute = idType.hasVersionAttribute();
					if (hasVersionAttribute) {
						pass = true;
						logTrace( "hasVersionAttribute() returned" + hasVersionAttribute);
					} else {
						logErr( "Expected: false, actual:" + hasVersionAttribute);

					}
				} else {
					logErr( "getSupertype() returned null");
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
