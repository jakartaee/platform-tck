/*
 * Copyright (c) 2007, 2020 Oracle and/or its affiliates. All rights reserved.
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

/*
 * $Id$
 */

package ee.jakarta.tck.persistence.ee.packaging.ejb.exclude;

import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

@Dependent
public class Client {
	private static Logger log = Logger.getLogger(Client.class.getName());

	@Dependent
	@Inject
	Instance<Stateful3IF> statefulBeanInstance;
	Stateful3IF statefulBean;

	/*
	 * @class.setup_props:
	 */
	public void setup() throws Exception {

	}

	/*
	 * Packaging:
	 *
	 * Enterprise archive (.ear) containing:
	 *
	 * client.jar - Application Client ejb-jar - EJB 3.0 Container-Managed Stateful
	 * Session Bean
	 *
	 * EJB-JAR is the root of the persistence unit persistence.xml resides in
	 * EJB-JAR META-INF directory persistence classes reside in EJB-JAR
	 * PersistenceContext is injected, default JTA EM, unitName specified
	 *
	 * Classes explicitly listed using class element and exclude-unlisted-classes
	 * element present.
	 *
	 */

	/*
	 * @testName: test1
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:962; PERSISTENCE:SPEC:894;
	 * PERSISTENCE:SPEC:897; PERSISTENCE:SPEC:901; PERSISTENCE:SPEC:931;
	 * PERSISTENCE:SPEC:958; PERSISTENCE:SPEC:959; PERSISTENCE:SPEC:970;
	 * PERSISTENCE:SPEC:947; PERSISTENCE:SPEC:959
	 * 
	 * @test_Strategy: All classes contained in the root of the persistence unit are
	 * also searched for annotated managed persistence classes and any mapping
	 * metadata annotations found on them will be processed. If it is not intended
	 * that the annotated persistence classes contained in the root of the
	 * persistence unit be included in the persistence unit, the
	 * exclude-unlisted-classes element should be used.
	 *
	 * In JavaEE Environments, the root of the persistence unit can be an EJB-JAR.
	 *
	 * Include Entities A and B in the the EJB_JAR with the persistence.xml in the
	 * EJB-JAR META-INF directory.
	 *
	 * Entity A is included in the persistence unit but is not explicitly listed in
	 * the class element of the persistence.xml. Make sure B can be persisted and
	 * modified.
	 *
	 */
	@Test
	public void test1() throws Exception {
		// TODO need to add converter classes
		boolean pass = false;

		try {
			statefulBean = statefulBeanInstance.get();
			pass = statefulBean.test1();
		} catch (Exception e) {
			log.log(Level.SEVERE, "Unexpected Exception :", e);
			throw e;
		} finally {
			cleanup();
		}

		if (!pass)
			throw new Exception("test1 failed");
	}

	/*
	 * @testName: test2
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:962; PERSISTENCE:SPEC:894;
	 * PERSISTENCE:SPEC:897; PERSISTENCE:SPEC:901; PERSISTENCE:SPEC:931;
	 * PERSISTENCE:SPEC:948
	 * 
	 * @test_Strategy: All classes contained in the root of the persistence unit are
	 * also searched for annotated managed persistence classes and any mapping
	 * metadata annotations found on them will be processed. If it is not intended
	 * that the annotated persistence classes contained in the root of the
	 * persistence unit be included in the persistence unit, the
	 * exclude-unlisted-classes element should be used.
	 *
	 * Include Entities A and B in the Persistence Unit. Entity A is not explicitly
	 * listed in the class element of the persistence.xml. Check to make sure that A
	 * is excluded from the PU by trying to persist a new instance of A. An
	 * IllegalArgumentException should be thrown as A "is not an entity".
	 *
	 */
	@Test
	public void test2() throws Exception {
		boolean pass = false;

		try {
			statefulBean = statefulBeanInstance.get();
			pass = statefulBean.test2();
		} catch (Exception e) {
			log.log(Level.SEVERE, "Unexpected Exception :", e);
			throw e;
		} finally {
			cleanup();
		}
		if (!pass)
			throw new Exception("test2 failed");
	}

	public void cleanup() throws Exception {
		try {
			if(statefulBean != null) {
				statefulBean.removeTestData();
				statefulBeanInstance.destroy(statefulBean);
				statefulBean = null;
			}
		} catch (Exception re) {
			log.log(Level.SEVERE, "Unexpected Exception :", re);
		}
		log.info( "cleanup complete");
	}

}
