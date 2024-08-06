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

package ee.jakarta.tck.persistence.core.metamodelapi.embeddabletype;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ee.jakarta.tck.persistence.common.PMClientBase;
import jakarta.persistence.metamodel.Attribute;
import jakarta.persistence.metamodel.CollectionAttribute;
import jakarta.persistence.metamodel.EmbeddableType;
import jakarta.persistence.metamodel.ListAttribute;
import jakarta.persistence.metamodel.MapAttribute;
import jakarta.persistence.metamodel.Metamodel;
import jakarta.persistence.metamodel.SetAttribute;
import jakarta.persistence.metamodel.SingularAttribute;

public class Client extends PMClientBase {



	public Client() {
	}

	public JavaArchive createDeployment() throws Exception {

		String pkgNameWithoutSuffix = Client.class.getPackageName();
		String pkgName = pkgNameWithoutSuffix + ".";
		String[] classes = { pkgName + "A", pkgName + "Address", pkgName + "ZipCode" };
		return createDeploymentJar("jpa_core_metamodelapi_embeddabletype.jar", pkgNameWithoutSuffix, classes);

	}

	@BeforeEach
	public void setup() throws Exception {
		logTrace( "setup");
		try {
			super.setup();
			createDeployment();
			removeTestData();
		} catch (Exception e) {
			logErr( "Exception: ", e);
			throw new Exception("Setup failed:", e);
		}
	}

	/*
	 * @testName: embeddableTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1433
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void embeddableTest() throws Exception {
		boolean pass = true;
		Collection<String> expected = new ArrayList<String>();
		expected.add("cZipcode");
		expected.add("street");
		expected.add("state");
		expected.add("city");
		expected.add("lZipcode");
		expected.add("mZipcode");
		expected.add("sZipcode");

		try {

			getEntityTransaction().begin();
			Metamodel metaModel = getEntityManager().getMetamodel();
			if (metaModel != null) {
				logTrace( "Obtained Non-null Metamodel from EntityManager");
				EmbeddableType<Address> eTypeOrder = metaModel.embeddable(Address.class);
				if (eTypeOrder != null) {
					logTrace( "Obtained Non-null EmbeddableType");
					Set<Attribute<Address, ?>> attribSet = eTypeOrder.getDeclaredAttributes();
					if (attribSet != null) {
						if (attribSet.size() != expected.size()) {
							pass = false;
							logErr( "Received wrong number of results");
						}
						for (Attribute attrib : attribSet) {
							String name = attrib.getName();
							if (expected.contains(name)) {
								logTrace( "Received expected result:" + name);
							} else {
								logErr( "Received unexpected result:" + name);
								pass = false;
							}
						}
					} else {
						pass = false;
						logErr( "getDeclaredAttributes() returned null");
					}
				} else {
					pass = false;
					logErr( "embeddable() returned null");
				}
			} else {
				pass = false;
				logErr( "getMetamodel() returned null");
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			pass = false;
			logErr( "Received unexpected exception", e);
		}
		if (!pass) {
			throw new Exception("embeddableTest failed");
		}
	}

	/*
	 * @testName: getAttribute
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1230
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getAttribute() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logTrace( "Obtained Non-null Metamodel from EntityManager");
			EmbeddableType<Address> eType = metaModel.embeddable(Address.class);
			if (eType != null) {
				logTrace( "Obtained Non-null Entity Address");
				Attribute attrib = eType.getAttribute("street");
				if (attrib != null) {
					logTrace( "attribute Name = " + attrib.getName());
					String name = attrib.getName();
					if (name.equals("street")) {
						logTrace( "Received expected result:" + name);
						pass = true;
					} else {
						logErr( "Expected: street, actual:" + name);
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
	 * @assertion_ids: PERSISTENCE:JAVADOC:1231;
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getAttributeIllegalArgumentException() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logTrace( "Obtained Non-null Metamodel from EntityManager");
			EmbeddableType<Address> eType = metaModel.embeddable(Address.class);
			if (eType != null) {
				logTrace( "Obtained Non-null Entity Address");
				try {
					eType.getAttribute("doesnotexist");
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
	 * @assertion_ids: PERSISTENCE:JAVADOC:1232;
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getAttributes() throws Exception {
		boolean pass = false;

		List<String> expected = new ArrayList<String>();

		expected.add("street");
		expected.add("state");
		expected.add("city");
		expected.add("cZipcode");
		expected.add("lZipcode");
		expected.add("mZipcode");
		expected.add("sZipcode");

		Collections.sort(expected);

		List<String> actual = new ArrayList<String>();

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logTrace( "Obtained Non-null Metamodel from EntityManager");
			EmbeddableType<Address> eType = metaModel.embeddable(Address.class);
			if (eType != null) {
				logTrace( "Obtained Non-null Entity Address");
				Set set = eType.getAttributes();
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
	 * @assertion_ids: PERSISTENCE:JAVADOC:1233;
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getCollectionStringClass() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logTrace( "Obtained Non-null Metamodel from EntityManager");
			EmbeddableType<Address> eType = metaModel.embeddable(Address.class);
			if (eType != null) {
				logTrace( "Obtained Non-null Entity Address");
				CollectionAttribute cAttrib = eType.getCollection("cZipcode", ZipCode.class);
				if (cAttrib != null) {
					logTrace( "attribute Name = " + cAttrib.getName());
					String name = cAttrib.getName();
					if (name.equals("cZipcode")) {
						logTrace( "Received expected result:" + name);
						pass = true;
					} else {
						logErr( "Expected: cZipcode, actual:" + name);
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
	 * @assertion_ids: PERSISTENCE:JAVADOC:1234;
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getCollectionStringClassIllegalArgumentException() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logTrace( "Obtained Non-null Metamodel from EntityManager");
			EmbeddableType<Address> eType = metaModel.embeddable(Address.class);
			if (eType != null) {
				try {
					eType.getCollection("doesnotexist", Address.class);
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
	 * @assertion_ids: PERSISTENCE:JAVADOC:1235;
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getCollectionString() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logTrace( "Obtained Non-null Metamodel from EntityManager");
			EmbeddableType<Address> eType = metaModel.embeddable(Address.class);
			if (eType != null) {
				logTrace( "Obtained Non-null Entity Address");
				CollectionAttribute cAttrib = eType.getCollection("cZipcode");
				if (cAttrib != null) {
					logTrace( "attribute Name = " + cAttrib.getName());
					String name = cAttrib.getName();
					if (name.equals("cZipcode")) {
						logTrace( "Received expected result:" + name);
						pass = true;
					} else {
						logErr( "Expected: cZipcode, actual:" + name);
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
	 * @assertion_ids: PERSISTENCE:JAVADOC:1236;
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getCollectionStringIllegalArgumentException() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logTrace( "Obtained Non-null Metamodel from EntityManager");
			EmbeddableType<Address> eType = metaModel.embeddable(Address.class);
			if (eType != null) {
				try {
					eType.getCollection("doesnotexist");
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
	 * @assertion_ids: PERSISTENCE:JAVADOC:1237;
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getDeclaredAttribute() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logTrace( "Obtained Non-null Metamodel from EntityManager");
			EmbeddableType<Address> eType = metaModel.embeddable(Address.class);
			if (eType != null) {
				logTrace( "Obtained Non-null Entity Address");
				Attribute attrib = eType.getDeclaredAttribute("cZipcode");
				if (attrib != null) {
					logTrace( "attribute Name = " + attrib.getName());
					String name = attrib.getName();
					if (name.equals("cZipcode")) {
						logTrace( "Received expected result:" + name);
						pass = true;
					} else {
						logErr( "Expected: cZipcode, actual:" + name);
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
	 * @assertion_ids: PERSISTENCE:JAVADOC:1238;
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getDeclaredAttributeIllegalArgumentException() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logTrace( "Obtained Non-null Metamodel from EntityManager");
			EmbeddableType<Address> eType = metaModel.embeddable(Address.class);
			if (eType != null) {
				try {
					eType.getDeclaredAttribute("doesnotexist");
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
	 * @assertion_ids: PERSISTENCE:JAVADOC:1239;
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getDeclaredAttributes() throws Exception {
		boolean pass = false;

		List<String> expected = new ArrayList<String>();

		expected.add("street");
		expected.add("state");
		expected.add("city");
		expected.add("cZipcode");
		expected.add("lZipcode");
		expected.add("mZipcode");
		expected.add("sZipcode");
		Collections.sort(expected);

		List<String> actual = new ArrayList<String>();

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logTrace( "Obtained Non-null Metamodel from EntityManager");
			EmbeddableType<Address> eType = metaModel.embeddable(Address.class);
			if (eType != null) {
				logTrace( "Obtained Non-null Entity Address");
				Set set = eType.getDeclaredAttributes();
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
							logErr( "Received unexpected attributes:");
							logErr( "Expected (" + expected.size() + "):");
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
	 * @assertion_ids: PERSISTENCE:JAVADOC:1240;
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getDeclaredCollectionStringClass() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logTrace( "Obtained Non-null Metamodel from EntityManager");
			EmbeddableType<Address> eType = metaModel.embeddable(Address.class);
			if (eType != null) {
				logTrace( "Obtained Non-null Entity Address");
				CollectionAttribute cAttrib = eType.getCollection("cZipcode", ZipCode.class);
				if (cAttrib != null) {
					logTrace( "attribute Name = " + cAttrib.getName());
					String name = cAttrib.getName();
					if (name.equals("cZipcode")) {
						logTrace( "Received expected result:" + name);
						pass = true;
					} else {
						logErr( "Expected: cZipcode, actual:" + name);
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
	 * @assertion_ids: PERSISTENCE:JAVADOC:1241;
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getDeclaredCollectionStringClassIllegalArgumentException() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logTrace( "Obtained Non-null Metamodel from EntityManager");
			EmbeddableType<Address> eType = metaModel.embeddable(Address.class);
			if (eType != null) {
				try {
					eType.getDeclaredCollection("doesnotexist", Address.class);
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
	 * @assertion_ids: PERSISTENCE:JAVADOC:1242;
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getDeclaredCollectionString() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logTrace( "Obtained Non-null Metamodel from EntityManager");
			EmbeddableType<Address> eType = metaModel.embeddable(Address.class);
			if (eType != null) {
				logTrace( "Obtained Non-null Entity Address");
				CollectionAttribute cAttrib = eType.getCollection("cZipcode", ZipCode.class);
				if (cAttrib != null) {
					logTrace( "attribute Name = " + cAttrib.getName());
					String name = cAttrib.getName();
					if (name.equals("cZipcode")) {
						logTrace( "Received expected result:" + name);
						pass = true;
					} else {
						logErr( "Expected: cZipcode, actual:" + name);
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
	 * @assertion_ids: PERSISTENCE:JAVADOC:1243;
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getDeclaredCollectionStringIllegalArgumentException() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logTrace( "Obtained Non-null Metamodel from EntityManager");
			EmbeddableType<Address> eType = metaModel.embeddable(Address.class);
			if (eType != null) {
				try {
					eType.getDeclaredCollection("doesnotexist", Address.class);
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
	 * @assertion_ids: PERSISTENCE:JAVADOC:1244;
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getDeclaredListStringClass() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logTrace( "Obtained Non-null Metamodel from EntityManager");
			EmbeddableType<Address> eType = metaModel.embeddable(Address.class);
			if (eType != null) {
				logTrace( "Obtained Non-null Entity Address");
				ListAttribute lAttrib = eType.getDeclaredList("lZipcode", ZipCode.class);
				if (lAttrib != null) {
					logTrace( "attribute Name = " + lAttrib.getName());
					String name = lAttrib.getName();
					if (name.equals("lZipcode")) {
						logTrace( "Received expected result:" + name);
						pass = true;
					} else {
						logErr( "Expected: lZipcode, actual:" + name);
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
	 * @assertion_ids: PERSISTENCE:JAVADOC:1245;
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getDeclaredListStringClassIllegalArgumentException() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logTrace( "Obtained Non-null Metamodel from EntityManager");
			EmbeddableType<Address> eType = metaModel.embeddable(Address.class);
			if (eType != null) {
				try {
					eType.getDeclaredList("doesnotexist", Address.class);
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
	 * @assertion_ids: PERSISTENCE:JAVADOC:1246;
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getDeclaredListString() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logTrace( "Obtained Non-null Metamodel from EntityManager");
			EmbeddableType<Address> eType = metaModel.embeddable(Address.class);
			if (eType != null) {
				logTrace( "Obtained Non-null Entity Address");
				ListAttribute lAttrib = eType.getDeclaredList("lZipcode");
				if (lAttrib != null) {
					logTrace( "attribute Name = " + lAttrib.getName());
					String name = lAttrib.getName();
					if (name.equals("lZipcode")) {
						logTrace( "Received expected result:" + name);
						pass = true;
					} else {
						logErr( "Expected: lZipcode, actual:" + name);
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
	 * @assertion_ids: PERSISTENCE:JAVADOC:1247;
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getDeclaredListStringIllegalArgumentException() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logTrace( "Obtained Non-null Metamodel from EntityManager");
			EmbeddableType<Address> eType = metaModel.embeddable(Address.class);
			if (eType != null) {
				try {
					eType.getDeclaredList("doesnotexist");
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
	 * @assertion_ids: PERSISTENCE:JAVADOC:1248;
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getDeclaredMapStringClassClass() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logTrace( "Obtained Non-null Metamodel from EntityManager");
			EmbeddableType<Address> eType = metaModel.embeddable(Address.class);
			if (eType != null) {
				logTrace( "Obtained Non-null Entity Address");
				MapAttribute mAttrib = eType.getDeclaredMap("mZipcode", ZipCode.class, String.class);
				if (mAttrib != null) {
					logTrace( "attribute Name = " + mAttrib.getName());
					String name = mAttrib.getName();
					if (name.equals("mZipcode")) {
						logTrace( "Received expected result:" + name);
						pass = true;
					} else {
						logErr( "Expected: mZipcode, actual:" + name);
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
	 * @assertion_ids: PERSISTENCE:JAVADOC:1249;
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getDeclaredMapStringClassClassIllegalArgumentException() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logTrace( "Obtained Non-null Metamodel from EntityManager");
			EmbeddableType<Address> eType = metaModel.embeddable(Address.class);
			if (eType != null) {
				try {
					eType.getDeclaredMap("doesnotexist", Address.class, String.class);
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
	 * @assertion_ids: PERSISTENCE:JAVADOC:1250;
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getDeclaredMapString() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logTrace( "Obtained Non-null Metamodel from EntityManager");
			EmbeddableType<Address> eType = metaModel.embeddable(Address.class);
			if (eType != null) {
				logTrace( "Obtained Non-null Entity Address");
				MapAttribute mAttrib = eType.getDeclaredMap("mZipcode");
				if (mAttrib != null) {
					logTrace( "attribute Name = " + mAttrib.getName());
					String name = mAttrib.getName();
					if (name.equals("mZipcode")) {
						logTrace( "Received expected result:" + name);
						pass = true;
					} else {
						logErr( "Expected: mZipcode, actual:" + name);
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
	 * @assertion_ids: PERSISTENCE:JAVADOC:1251;
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getDeclaredMapStringIllegalArgumentException() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logTrace( "Obtained Non-null Metamodel from EntityManager");
			EmbeddableType<Address> eType = metaModel.embeddable(Address.class);
			if (eType != null) {
				try {
					eType.getDeclaredMap("doesnotexist");
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
	 * @assertion_ids: PERSISTENCE:JAVADOC:1253;
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getDeclaredSetStringClass() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logTrace( "Obtained Non-null Metamodel from EntityManager");
			EmbeddableType<Address> eType = metaModel.embeddable(Address.class);
			if (eType != null) {
				logTrace( "Obtained Non-null Entity Address");
				SetAttribute sAttrib = eType.getDeclaredSet("sZipcode", ZipCode.class);
				if (sAttrib != null) {
					logTrace( "attribute Name = " + sAttrib.getName());
					String name = sAttrib.getName();
					if (name.equals("sZipcode")) {
						logTrace( "Received expected result:" + name);
						pass = true;
					} else {
						logErr( "Expected: sZipcode, actual:" + name);
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
	 * @assertion_ids: PERSISTENCE:JAVADOC:1254;
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getDeclaredSetStringClassIllegalArgumentException() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logTrace( "Obtained Non-null Metamodel from EntityManager");
			EmbeddableType<Address> eType = metaModel.embeddable(Address.class);
			if (eType != null) {
				try {
					eType.getDeclaredSet("doesnotexist", Address.class);
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
	 * @assertion_ids: PERSISTENCE:JAVADOC:1255;
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getDeclaredSetString() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logTrace( "Obtained Non-null Metamodel from EntityManager");
			EmbeddableType<Address> eType = metaModel.embeddable(Address.class);
			if (eType != null) {
				logTrace( "Obtained Non-null Entity Address");
				SetAttribute sAttrib = eType.getDeclaredSet("sZipcode");
				if (sAttrib != null) {
					logTrace( "attribute Name = " + sAttrib.getName());
					String name = sAttrib.getName();
					if (name.equals("sZipcode")) {
						logTrace( "Received expected result:" + name);
						pass = true;
					} else {
						logErr( "Expected: sZipcode, actual:" + name);
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
	 * @assertion_ids: PERSISTENCE:JAVADOC:1256;
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getDeclaredSetStringIllegalArgumentException() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logTrace( "Obtained Non-null Metamodel from EntityManager");
			EmbeddableType<Address> eType = metaModel.embeddable(Address.class);
			if (eType != null) {
				try {
					eType.getDeclaredSet("doesnotexist");
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
	 * @assertion_ids: PERSISTENCE:JAVADOC:1257;
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getDeclaredSingularAttributeStringClass() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logTrace( "Obtained Non-null Metamodel from EntityManager");
			EmbeddableType<Address> eType = metaModel.embeddable(Address.class);
			if (eType != null) {
				logTrace( "Obtained Non-null Entity Address");
				SingularAttribute singAttrib = eType.getDeclaredSingularAttribute("city", String.class);
				if (singAttrib != null) {
					logTrace( "attribute city = " + singAttrib.getName());
					String name = singAttrib.getName();
					if (name.equals("city")) {
						logTrace( "Received expected result:" + name);
						pass = true;
					} else {
						logErr( "Expected: name, actual:" + name);
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
	 * @assertion_ids: PERSISTENCE:JAVADOC:1258;
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getDeclaredSingularAttributeStringClassIllegalArgumentException() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logTrace( "Obtained Non-null Metamodel from EntityManager");
			EmbeddableType<Address> eType = metaModel.embeddable(Address.class);
			if (eType != null) {
				logTrace( "Obtained Non-null Entity Address");
				try {
					eType.getDeclaredSingularAttribute("value", Integer.class);
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
	 * @assertion_ids: PERSISTENCE:JAVADOC:1259;
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getDeclaredSingularAttributeString() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logTrace( "Obtained Non-null Metamodel from EntityManager");
			EmbeddableType<Address> eType = metaModel.embeddable(Address.class);
			if (eType != null) {
				logTrace( "Obtained Non-null Entity Address");
				SingularAttribute singAttrib = eType.getDeclaredSingularAttribute("street");
				if (singAttrib != null) {
					logTrace( "attribute Name = " + singAttrib.getName());
					String name = singAttrib.getName();
					if (name.equals("street")) {
						logTrace( "Received expected result:" + name);
						pass = true;
					} else {
						logErr( "Expected: street, actual:" + name);
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
	 * @assertion_ids: PERSISTENCE:JAVADOC:1260;
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getDeclaredSingularAttributeStringIllegalArgumentException() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logTrace( "Obtained Non-null Metamodel from EntityManager");
			EmbeddableType<Address> eType = metaModel.embeddable(Address.class);
			if (eType != null) {
				logTrace( "Obtained Non-null Entity Address");
				try {
					eType.getDeclaredSingularAttribute("value");
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
	 * @assertion_ids: PERSISTENCE:JAVADOC:1261;
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getDeclaredSingularAttributes() throws Exception {
		boolean pass = false;

		List<String> expected = new ArrayList<String>();

		expected.add("city");
		expected.add("state");
		expected.add("street");
		Collections.sort(expected);

		List<String> actual = new ArrayList<String>();

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logTrace( "Obtained Non-null Metamodel from EntityManager");
			EmbeddableType<Address> eType = metaModel.embeddable(Address.class);
			if (eType != null) {
				logTrace( "Obtained Non-null Entity Address");
				Set set = eType.getDeclaredSingularAttributes();
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
	 * @assertion_ids: PERSISTENCE:JAVADOC:1262;
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getListStringClass() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logTrace( "Obtained Non-null Metamodel from EntityManager");
			EmbeddableType<Address> eType = metaModel.embeddable(Address.class);
			if (eType != null) {
				logTrace( "Obtained Non-null Entity Address");
				ListAttribute lAttrib = eType.getList("lZipcode", ZipCode.class);
				if (lAttrib != null) {
					logTrace( "attribute Name = " + lAttrib.getName());
					String name = lAttrib.getName();
					if (name.equals("lZipcode")) {
						logTrace( "Received expected result:" + name);
						pass = true;
					} else {
						logErr( "Expected: lZipcode, actual:" + name);
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
	 * @assertion_ids: PERSISTENCE:JAVADOC:1263;
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getListStringClassIllegalArgumentException() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logTrace( "Obtained Non-null Metamodel from EntityManager");
			EmbeddableType<Address> eType = metaModel.embeddable(Address.class);
			if (eType != null) {
				try {
					eType.getList("doesnotexist", Address.class);
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
	 * @assertion_ids: PERSISTENCE:JAVADOC:1264;
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getListString() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logTrace( "Obtained Non-null Metamodel from EntityManager");
			EmbeddableType<Address> eType = metaModel.embeddable(Address.class);
			if (eType != null) {
				logTrace( "Obtained Non-null Entity Address");
				ListAttribute lAttrib = eType.getList("lZipcode");
				if (lAttrib != null) {
					logTrace( "attribute Name = " + lAttrib.getName());
					String name = lAttrib.getName();
					if (name.equals("lZipcode")) {
						logTrace( "Received expected result:" + name);
						pass = true;
					} else {
						logErr( "Expected: lZipcode, actual:" + name);
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
	 * @assertion_ids: PERSISTENCE:JAVADOC:1265;
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getListStringIllegalArgumentException() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logTrace( "Obtained Non-null Metamodel from EntityManager");
			EmbeddableType<Address> eType = metaModel.embeddable(Address.class);
			if (eType != null) {
				try {
					eType.getDeclaredList("doesnotexist");
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
	 * @assertion_ids: PERSISTENCE:JAVADOC:1266;
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getMapStringClassClass() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logTrace( "Obtained Non-null Metamodel from EntityManager");
			EmbeddableType<Address> eType = metaModel.embeddable(Address.class);
			if (eType != null) {
				logTrace( "Obtained Non-null Entity Address");
				MapAttribute mAttrib = eType.getMap("mZipcode", ZipCode.class, String.class);
				if (mAttrib != null) {
					logTrace( "attribute Name = " + mAttrib.getName());
					String name = mAttrib.getName();
					if (name.equals("mZipcode")) {
						logTrace( "Received expected result:" + name);
						pass = true;
					} else {
						logErr( "Expected: mZipcode, actual:" + name);
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
	 * @assertion_ids: PERSISTENCE:JAVADOC:1267;
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getMapStringClassClassIllegalArgumentException() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logTrace( "Obtained Non-null Metamodel from EntityManager");
			EmbeddableType<Address> eType = metaModel.embeddable(Address.class);
			if (eType != null) {
				try {
					eType.getMap("doesnotexist", Address.class, String.class);
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
	 * @assertion_ids: PERSISTENCE:JAVADOC:1268;
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getMapString() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logTrace( "Obtained Non-null Metamodel from EntityManager");
			EmbeddableType<Address> eType = metaModel.embeddable(Address.class);
			if (eType != null) {
				logTrace( "Obtained Non-null Entity Address");
				MapAttribute mAttrib = eType.getMap("mZipcode");
				if (mAttrib != null) {
					logTrace( "attribute Name = " + mAttrib.getName());
					String name = mAttrib.getName();
					if (name.equals("mZipcode")) {
						logTrace( "Received expected result:" + name);
						pass = true;
					} else {
						logErr( "Expected: mZipcode, actual:" + name);
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
	 * @assertion_ids: PERSISTENCE:JAVADOC:1269;
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getMapStringIllegalArgumentException() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logTrace( "Obtained Non-null Metamodel from EntityManager");
			EmbeddableType<Address> eType = metaModel.embeddable(Address.class);
			if (eType != null) {
				try {
					eType.getMap("doesnotexist");
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
	 * @assertion_ids: PERSISTENCE:JAVADOC:1270;
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getPluralAttributes() throws Exception {
		boolean pass = false;

		List<String> expected = new ArrayList<String>();

		expected.add("cZipcode");
		expected.add("lZipcode");
		expected.add("mZipcode");
		expected.add("sZipcode");
		Collections.sort(expected);

		List<String> actual = new ArrayList<String>();

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logTrace( "Obtained Non-null Metamodel from EntityManager");
			EmbeddableType<Address> eType = metaModel.embeddable(Address.class);
			if (eType != null) {
				logTrace( "Obtained Non-null Entity Address");
				Set set = eType.getPluralAttributes();
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
							logErr( "Received Unexpected attributes");
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
	 * @testName: getSetStringClass
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1271;
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getSetStringClass() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logTrace( "Obtained Non-null Metamodel from EntityManager");
			EmbeddableType<Address> eType = metaModel.embeddable(Address.class);
			if (eType != null) {
				logTrace( "Obtained Non-null Entity Address");
				SetAttribute sAttrib = eType.getSet("sZipcode", ZipCode.class);
				if (sAttrib != null) {
					logTrace( "attribute Name = " + sAttrib.getName());
					String name = sAttrib.getName();
					if (name.equals("sZipcode")) {
						logTrace( "Received expected result:" + name);
						pass = true;
					} else {
						logErr( "Expected: sZipcode, actual:" + name);
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
	 * @assertion_ids: PERSISTENCE:JAVADOC:1272;
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getSetStringClassIllegalArgumentException() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logTrace( "Obtained Non-null Metamodel from EntityManager");
			EmbeddableType<Address> eType = metaModel.embeddable(Address.class);
			if (eType != null) {
				try {
					eType.getSet("doesnotexist", Address.class);
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
	 * @assertion_ids: PERSISTENCE:JAVADOC:1273;
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getSetString() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logTrace( "Obtained Non-null Metamodel from EntityManager");
			EmbeddableType<Address> eType = metaModel.embeddable(Address.class);
			if (eType != null) {
				logTrace( "Obtained Non-null Entity Address");
				SetAttribute sAttrib = eType.getSet("sZipcode");
				if (sAttrib != null) {
					logTrace( "attribute Name = " + sAttrib.getName());
					String name = sAttrib.getName();
					if (name.equals("sZipcode")) {
						logTrace( "Received expected result:" + name);
						pass = true;
					} else {
						logErr( "Expected: sZipcode, actual:" + name);
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
	 * @assertion_ids: PERSISTENCE:JAVADOC:1274;
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getSetStringIllegalArgumentException() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logTrace( "Obtained Non-null Metamodel from EntityManager");
			EmbeddableType<Address> eType = metaModel.embeddable(Address.class);
			if (eType != null) {
				try {
					eType.getSet("doesnotexist");
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
	 * @assertion_ids: PERSISTENCE:JAVADOC:1275;
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getSingularAttributeStringClass() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logTrace( "Obtained Non-null Metamodel from EntityManager");
			EmbeddableType<Address> eType = metaModel.embeddable(Address.class);
			if (eType != null) {
				logTrace( "Obtained Non-null Entity Address");
				SingularAttribute singAttrib = eType.getSingularAttribute("street", String.class);
				if (singAttrib != null) {
					logTrace( "attribute Name = " + singAttrib.getName());
					String name = singAttrib.getName();
					if (name.equals("street")) {
						logTrace( "Received expected result:" + name);
						pass = true;
					} else {
						logErr( "Expected: street, actual:" + name);
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
	 * @assertion_ids: PERSISTENCE:JAVADOC:1276;
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getSingularAttributeStringClassIllegalArgumentException() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logTrace( "Obtained Non-null Metamodel from EntityManager");
			EmbeddableType<Address> eType = metaModel.embeddable(Address.class);
			if (eType != null) {
				logTrace( "Obtained Non-null Entity Address");
				try {
					eType.getSingularAttribute("doesnotexist", Address.class);
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
	 * @assertion_ids: PERSISTENCE:JAVADOC:1277;
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getSingularAttributeString() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logTrace( "Obtained Non-null Metamodel from EntityManager");
			EmbeddableType<Address> eType = metaModel.embeddable(Address.class);
			if (eType != null) {
				logTrace( "Obtained Non-null Entity Address");
				SingularAttribute singAttrib = eType.getSingularAttribute("street");
				if (singAttrib != null) {
					logTrace( "attribute Name = " + singAttrib.getName());
					String name = singAttrib.getName();
					if (name.equals("street")) {
						logTrace( "Received expected result:" + name);
						pass = true;
					} else {
						logErr( "Expected: street, actual:" + name);
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
	 * @assertion_ids: PERSISTENCE:JAVADOC:1278;
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getSingularAttributeStringIllegalArgumentException() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logTrace( "Obtained Non-null Metamodel from EntityManager");
			EmbeddableType<Address> eType = metaModel.embeddable(Address.class);
			if (eType != null) {
				logTrace( "Obtained Non-null Entity Address");
				try {
					eType.getSingularAttribute("doesnotexist");
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
	 * @assertion_ids: PERSISTENCE:JAVADOC:1279;
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getSingularAttributes() throws Exception {
		boolean pass = false;

		List<String> expected = new ArrayList<String>();

		expected.add("city");
		expected.add("state");
		expected.add("street");
		Collections.sort(expected);

		List<String> actual = new ArrayList<String>();

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logTrace( "Obtained Non-null Metamodel from EntityManager");
			EmbeddableType<Address> eType = metaModel.embeddable(Address.class);
			if (eType != null) {
				logTrace( "Obtained Non-null Entity Address");
				Set set = eType.getSingularAttributes();
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
							logErr( "Received Unexpected attributes");
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

	/*
	 * @testName: getDeclaredPluralAttributes
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1252;
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getDeclaredPluralAttributes() throws Exception {
		boolean pass = false;

		List<String> expected = new ArrayList<String>();

		expected.add("cZipcode");
		expected.add("lZipcode");
		expected.add("mZipcode");
		expected.add("sZipcode");
		Collections.sort(expected);

		List<String> actual = new ArrayList<String>();

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logTrace( "Obtained Non-null Metamodel from EntityManager");
			EmbeddableType<Address> eType = metaModel.embeddable(Address.class);
			if (eType != null) {
				Set set = eType.getDeclaredPluralAttributes();
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
							logErr( "Received Unexpected attributes");
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

	@AfterEach
	public void cleanup() throws Exception {
		try {
			logTrace( "Cleanup data");
			removeTestData();
			logTrace( "cleanup complete, calling super.cleanup");
			super.cleanup();
		} finally {
			removeTestJarFromCP();
		}
	}

	private void removeTestData() {
		logTrace( "removeTestData");
		if (getEntityTransaction().isActive()) {
			getEntityTransaction().rollback();
		}
	}
}
