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

package ee.jakarta.tck.persistence.ee.packaging.ejb.standalone;


import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import org.junit.jupiter.api.AfterEach;
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

	private Properties props;

	/*
	 * @class.setup_props:
	 */
	@PostConstruct
	public void setup() {
		try {
			cleanup();
		} catch (Exception e) {
			throw new IllegalStateException("Setup Failed!", e);
		}
	}

	/*
	 * Packaging:
	 *
	 * ejb-jar - EJB 3.0 Container-Managed Stateful Session Bean
	 *
	 * EJB-JAR is the root of the persistence unit persistence.xml resides in
	 * EJB-JAR META-INF directory
	 *
	 * persistence unit configuration information: Container-Managed JTA Entity
	 * Manager looked up with sessionContext.lookup EntityManager defined in
	 * ejb.jar.xml with persistence-context-ref deployment descriptor
	 */

	/*
	 * @testName: test1
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:897; PERSISTENCE:SPEC:900;
	 * PERSISTENCE:SPEC:901; PERSISTENCE:SPEC:907; PERSISTENCE:SPEC:938;
	 * JavaEE:SPEC:10063; JavaEE:SPEC:10064; JavaEE:SPEC:10065
	 * 
	 * @test_Strategy: It is not required that an EJB-JAR containing a persistence
	 * unit be packaged in an EAR unless the persistence unit contains persistence
	 * classes in addition to those contained in the EJB-JAR.
	 *
	 * Deploy the standalone archive to the application server with the above
	 * content. Create entities, persist them, then find.
	 *
	 */
	@Test
	public void test1() throws Exception {

		log.info( "Begin test1");
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

	@AfterEach
	public void cleanup() throws Exception {
		try {
			if(statefulBean != null) {
				statefulBean.removeTestData();
				statefulBeanInstance.destroy(statefulBean);
				statefulBean = null;
			}
		} catch (Exception re) {
			log.log(Level.WARNING,"Unexpected Exception in entity cleanup:", re);
		}
		log.info( "cleanup complete");
	}

}
