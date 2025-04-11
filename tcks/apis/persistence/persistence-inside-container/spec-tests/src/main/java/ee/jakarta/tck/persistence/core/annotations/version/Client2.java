/*
 * Copyright (c) 2008, 2024 Oracle and/or its affiliates. All rights reserved.
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

package ee.jakarta.tck.persistence.core.annotations.version;

import java.util.Properties;
import java.util.logging.Logger;

import com.sun.ts.lib.harness.Status;

public class Client2 extends Client {
	static final Logger log = Logger.getLogger(Client2.class.getName());


	public Client2() {
	}

	public static void main(String[] args) {
		Client2 theTests = new Client2();
		Status s = theTests.run(args, System.out, System.err);
		s.exit();
	}

	public void setup(String[] args, Properties p) throws Exception {
		logTrace( "setupShortData");
		log.info("setupShortData");
		try {
			super.setup(args,p);
			removeTestData();
			createShortTestData();

		} catch (Exception e) {
			throw new Exception("Setup failed:", e);
		}
	}

	/*
	 * @testName: shortFieldTest
	 *
	 * @assertion_ids: PERSISTENCE:SPEC:2117; PERSISTENCE:SPEC:2117.3
	 *
	 * @test_Strategy:
	 */
	
	public void shortFieldTest() throws Exception {

		boolean pass = false;
		try {
			Short_Field a = getEntityManager().find(Short_Field.class, "1");
			if (a != null) {
				logTrace( "version:" + a.getVersion());
				// if (a.getVersion() == 1) {
				short version = a.getVersion();
				a.setName("two");
				getEntityTransaction().begin();
				getEntityManager().merge(a);
				getEntityManager().flush();
				getEntityTransaction().commit();
				Short_Field a1 = getEntityManager().find(Short_Field.class, "1");
				if (a1 != null) {
					if (a1.getVersion() > version) {
						logTrace( "version:" + a1.getVersion());
						pass = true;
					} else {
						logErr(
								"Did not get a greater version after a modification:" + a1.getVersion());
					}
				} else {
					logErr( "Second find returned null result");
				}
				/*
				 * } else {
				 * logErr("Did not get a version of 1 after find"); }
				 */
			} else {
				logErr( "Find returned null result");
			}
		} catch (Exception e) {
			logErr( "Unexpected exception occurred", e);
		}

		if (!pass) {
			throw new Exception("shortFieldTest failed");
		}

	}

	/*
	 * @testName: shortPropertyTest
	 *
	 * @assertion_ids: PERSISTENCE:SPEC:2117; PERSISTENCE:SPEC:2117.3
	 *
	 * @test_Strategy:
	 */
	
	public void shortPropertyTest() throws Exception {
		boolean pass = false;
		try {
			Short_Property a = getEntityManager().find(Short_Property.class, "2");
			if (a != null) {
				logTrace( "version:" + a.getBasicShort());
				// if (a.getVersion() == 1) {
				short version = a.getBasicShort();
				a.setName("two");
				getEntityTransaction().begin();
				getEntityManager().merge(a);
				getEntityManager().flush();
				getEntityTransaction().commit();
				Short_Property a1 = getEntityManager().find(Short_Property.class, "2");
				if (a1 != null) {
					if (a1.getBasicShort() > version) {
						logTrace( "version:" + a1.getBasicShort());
						pass = true;
					} else {
						logErr(
								"Did not get a greater version after a modification:" + a1.getBasicShort());
					}
				} else {
					logErr( "Second find returned null result");
				}
				/*
				 * } else {
				 * logErr("Did not get a version of 1 after find"); }
				 */
			} else {
				logErr( "Find returned null result");
			}
		} catch (Exception e) {
			logErr( "Unexpected exception occurred", e);
		}

		if (!pass) {
			throw new Exception("shortPropertyTest failed");
		}

	}

	/*
	 * @testName: shortClassFieldTest
	 *
	 * @assertion_ids: PERSISTENCE:SPEC:2117; PERSISTENCE:SPEC:2117.4
	 *
	 * @test_Strategy:
	 */
	
	public void shortClassFieldTest() throws Exception {

		boolean pass = false;
		try {
			ShortClass_Field a = getEntityManager().find(ShortClass_Field.class, "3");
			log.info("shortFieldTest, a="+a);
			if (a != null) {
				logTrace( "version:" + a.getVersion());
				// if (a.getVersion() == 1) {
				Short version = a.getVersion();
				log.info("shortFieldTest, a.version="+version);
				a.setName("two");
				getEntityTransaction().begin();
				getEntityManager().merge(a);
				getEntityManager().flush();
				getEntityTransaction().commit();
				ShortClass_Field a1 = getEntityManager().find(ShortClass_Field.class, "3");
				log.info("shortFieldTest, a1="+a1);
				if (a1 != null) {
					if (a1.getVersion() == null) {
						logErr("version is null for updated entity");
					} else if (version == null || a1.getVersion() > version) {
						logTrace( "version:" + a1.getVersion());
						pass = true;
					} else {
						logErr(
								"Did not get a greater version after a modification:" + a1.getVersion());
					}
				} else {
					logErr( "Second find returned null result");
				}
				/*
				 * } else {
				 * logErr("Did not get a version of 1 after find"); }
				 */
			} else {
				logErr( "Find returned null result");
			}
		} catch (Exception e) {
			logErr( "Unexpected exception occurred", e);
		}

		if (!pass) {
			throw new Exception("shortClassFieldTest failed");
		}

	}

	/*
	 * @testName: shortClassPropertyTest
	 *
	 * @assertion_ids: PERSISTENCE:SPEC:2117; PERSISTENCE:SPEC:2117.4
	 *
	 * @test_Strategy:
	 */
	
	public void shortClassPropertyTest() throws Exception {
		boolean pass = false;
		try {
			if (! getEntityManager().isJoinedToTransaction()) {
				throw new Exception("shortClassPropertyTest failed because there the persistence context " +
						"is not joined to the transaction " +
						getEntityTransaction() != null ? getEntityTransaction().getClass().getName() : "getEntityTransaction() returns null" );
			}
			ShortClass_Property a = getEntityManager().find(ShortClass_Property.class, "4");
			if (a != null) {
				logTrace( "version:" + a.getBasicShort());
				// if (a.getVersion() == 1) {
				Short version = a.getBasicShort();
				a.setName("two");
				getEntityTransaction().begin();
				getEntityManager().merge(a);
				getEntityManager().flush();
				getEntityTransaction().commit();
				ShortClass_Property a1 = getEntityManager().find(ShortClass_Property.class, "4");
				if (a1 != null) {
					if (a1.getBasicShort() == null) {
						logErr("version is null for updated entity");
					}
					else if (version == null || a1.getBasicShort() > version) {
						logTrace( "version:" + a1.getBasicShort());
						pass = true;
					} else {
						logErr(
								"Did not get a greater version after a modification:" + a1.getBasicShort());
					}
				} else {
					logErr( "Second find returned null result");
				}
				/*
				 * } else {
				 * logErr("Did not get a version of 1 after find"); }
				 */
			} else {
				logErr( "Find returned null result");
			}
		} catch (Exception e) {
			logErr( "Unexpected exception occurred", e);
		}

		if (!pass) {
			throw new Exception("shortClassPropertyTest failed");
		}

	}

	public void createShortTestData() {
		logTrace( "createShortTestData");
		log.info("createShortTestData");

		try {
			getEntityTransaction().begin();
			getEntityManager().persist(new Short_Field("1"));
			getEntityManager().persist(new Short_Property("2"));
			getEntityManager().persist(new ShortClass_Field("3"));
			getEntityManager().persist(new ShortClass_Property("4"));
			getEntityTransaction().commit();
			log.info("createShortTestData, committed objects");
		} catch (Exception e) {
			logErr( "Unexpected Exception in createShortTestData:", e);
			log.severe("Unexpected Exception in createShortTestDat"+e.getMessage());
		} finally {
			log.info("createShortTestData, finally tx.isActive()="+getEntityTransaction().isActive());
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logErr( "Unexpected Exception during Rollback:", re);
			}
		}

	}

}
