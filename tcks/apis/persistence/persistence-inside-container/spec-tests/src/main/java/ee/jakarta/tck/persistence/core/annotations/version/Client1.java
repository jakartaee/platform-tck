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

import com.sun.ts.lib.harness.Status;


public class Client1 extends Client {



	public Client1() {
	}
	public static void main(String[] args) {
		Client1 theTests = new Client1();
		Status s = theTests.run(args, System.out, System.err);
		s.exit();
	}

	public void setup(String[] args, Properties p) throws Exception {
		logTrace( "setupIntData");
		try {
			super.setup(args,p);
			removeTestData();
			createIntTestData();

		} catch (Exception e) {
			throw new Exception("Setup failed:", e);
		}
	}

	/*
	 * @testName: intFieldTest
	 *
	 * @assertion_ids: PERSISTENCE:SPEC:2115; PERSISTENCE:SPEC:2116;
	 * PERSISTENCE:SPEC:2117; PERSISTENCE:SPEC:2117.1
	 *
	 * @test_Strategy:
	 */
	
	public void intFieldTest() throws Exception {

		boolean pass = false;
		try {
			Int_Field a = getEntityManager().find(Int_Field.class, "1");
			if (a != null) {
				// if (a.getVersion() == 1) {
				logTrace( "version:" + a.getVersion());
				int version = a.getVersion();
				a.setName("two");
				getEntityTransaction().begin();
				getEntityManager().merge(a);
				getEntityManager().flush();
				getEntityTransaction().commit();
				Int_Field a1 = getEntityManager().find(Int_Field.class, "1");
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
			throw new Exception("intFieldTest failed");
		}

	}

	/*
	 * @testName: intPropertyTest
	 *
	 * @assertion_ids: PERSISTENCE:SPEC:2115; PERSISTENCE:SPEC:2116;
	 * PERSISTENCE:SPEC:2117; PERSISTENCE:SPEC:2117.1
	 *
	 * @test_Strategy:
	 */
	
	public void intPropertyTest() throws Exception {
		boolean pass = false;
		try {
			Int_Property a = getEntityManager().find(Int_Property.class, "2");
			if (a != null) {
				logTrace( "version:" + a.getValue());
				// if (a.getVersion() == 1) {
				int version = a.getValue();

				a.setName("two");
				getEntityTransaction().begin();
				getEntityManager().merge(a);
				getEntityManager().flush();
				getEntityTransaction().commit();
				Int_Property a1 = getEntityManager().find(Int_Property.class, "2");
				if (a1 != null) {
					if (a1.getValue() > version) {
						logTrace( "version:" + a1.getValue());
						pass = true;
					} else {
						logErr(
								"Did not get a greater version after a modification:" + a1.getValue());
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
			throw new Exception("intPropertyTest failed");
		}

	}

	/*
	 * @testName: integerFieldTest
	 *
	 * @assertion_ids: PERSISTENCE:SPEC:2117; PERSISTENCE:SPEC:2117.2
	 *
	 * @test_Strategy:
	 */
	
	public void integerFieldTest() throws Exception {

		boolean pass = false;
		try {
			Integer_Field a = getEntityManager().find(Integer_Field.class, "3");
			if (a != null) {
				logTrace( "version:" + a.getVersion());
				// if (a.getVersion() == 1) {
				Integer version = a.getVersion();
				a.setName("two");
				getEntityTransaction().begin();
				getEntityManager().merge(a);
				getEntityManager().flush();
				getEntityTransaction().commit();
				Integer_Field a1 = getEntityManager().find(Integer_Field.class, "3");
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
			throw new Exception("integerFieldTest failed");
		}

	}

	/*
	 * @testName: integerPropertyTest
	 *
	 * @assertion_ids: PERSISTENCE:SPEC:2117; PERSISTENCE:SPEC:2117.2
	 *
	 * @test_Strategy:
	 */
	
	public void integerPropertyTest() throws Exception {
		boolean pass = false;
		try {
			Integer_Property a = getEntityManager().find(Integer_Property.class, "4");
			if (a != null) {
				logTrace( "version:" + a.getBasicInteger());
				// if (a.getVersion() == 1) {
				Integer version = a.getBasicInteger();
				a.setName("two");
				getEntityTransaction().begin();
				getEntityManager().merge(a);
				getEntityManager().flush();
				getEntityTransaction().commit();
				Integer_Property a1 = getEntityManager().find(Integer_Property.class, "4");
				if (a1 != null) {
					if (a1.getBasicInteger() == null) {
						logErr("version is null for updated entity");
					} else if (version == null || a1.getBasicInteger() > version) {
						logTrace( "version:" + a1.getBasicInteger());
						pass = true;
					} else {
						logErr(
								"Did not get a greater version after a modification:" + a1.getBasicInteger());
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
			throw new Exception("integerPropertyTest failed");
		}

	}

	public void createIntTestData() {
		logTrace( "createIntTestData");

		try {
			getEntityTransaction().begin();
			getEntityManager().persist(new Int_Field("1"));
			getEntityManager().persist(new Int_Property("2"));
			getEntityManager().persist(new Integer_Field("3"));
			getEntityManager().persist(new Integer_Property("4"));

			getEntityTransaction().commit();
		} catch (Exception e) {
			logErr( "Unexpected Exception in createIntTestData:", e);
		} finally {
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
