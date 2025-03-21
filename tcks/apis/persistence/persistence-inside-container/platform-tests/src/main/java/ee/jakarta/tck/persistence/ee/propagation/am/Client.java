/*
 * Copyright (c) 2013, 2023 Oracle and/or its affiliates. All rights reserved.
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

package ee.jakarta.tck.persistence.ee.propagation.am;


import jakarta.ejb.EJB;
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

	@Dependent
	@Inject
	Instance<Stateful3IF2> statefulBean2Instance;
	Stateful3IF2 statefulBean2;

	@EJB(name = "ejb/Stateless3Bean", beanInterface = Stateless3IF.class)
	Stateless3IF statelessBean;

	private Properties props;

	/*
	 * @class.setup_props:
	 */

	public void setup() throws Exception {
	}

	/*
	 * @testName: test1
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:684; PERSISTENCE:SPEC:842;
	 * PERSISTENCE:SPEC:850; PERSISTENCE:SPEC:852; PERSISTENCE:SPEC:853;
	 * PERSISTENCE:SPEC:859; PERSISTENCE:SPEC:879; PERSISTENCE:SPEC:880;
	 * PERSISTENCE:SPEC:885; PERSISTENCE:JAVADOC:58; PERSISTENCE:SPEC:1024
	 * 
	 * @test_Strategy: When an application-managed entity manager is used, the
	 * application interacts directly with the persistence provider's entity manager
	 * factory to manage the entity manager life cycle and to obtain and destroy
	 * persistence contexts. All such application-managed pcs are extended in scope
	 * and may span multiple transactions.
	 *
	 * Inject entity manager factory, but open and close each entity manager within
	 * the business method.
	 */
	@Test
	public void test1() throws Exception {

		log.info( "Begin test1");
		boolean pass = false;

		try {
			statelessBean.init(props);
			statelessBean.doCleanup();
			pass = statelessBean.test1();
			statelessBean.doCleanup();

		} catch (Exception e) {
			log.log(Level.SEVERE, "Unexpected Exception :", e);
			throw e;
		}

		if (!pass)
			throw new Exception("test1 failed");
	}

	/*
	 * @testName: test2
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:880
	 * 
	 * @test_Strategy: When an application-managed entity manager is used, the
	 * application interacts directly with the persistence provider's entity manager
	 * factory to manage the entity manager life cycle and to obtain and destroy
	 * persistence contexts. All such application-managed pcs are extended in scope
	 * and may span multiple transactions.
	 *
	 * Inject entity manager factory, but open and close each entity manager within
	 * the business method.
	 *
	 */
	@Test
	public void test2() throws Exception {

		log.info( "Begin test2");
		boolean pass = false;

		try {
			statelessBean.init(props);
			statelessBean.doCleanup();
			pass = statelessBean.test2();
			statelessBean.doCleanup();
		} catch (Exception e) {
			log.log(Level.SEVERE, "Unexpected Exception :", e);
			throw e;
		}

		if (!pass)
			throw new Exception("test2 failed");
	}

	/*
	 * @testName: test3
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:692; PERSISTENCE:JAVADOC:140;
	 * PERSISTENCE:JAVADOC:52
	 * 
	 * @test_Strategy: The persistence providers implementation of the merge
	 * operation must examine the version attribute when an entity is being merged
	 * and throw an OptimisticLockException if is discovered that the object being
	 * merged is a stale copy of the entity.
	 *
	 */
	@Test
	public void test3() throws Exception {

		log.info( "Begin test3");
		boolean pass = false;

		try {
			statefulBean = statefulBeanInstance.get();
			statefulBean.init(props);
			pass = statefulBean.test3();
		} catch (Exception e) {
			log.log(Level.SEVERE, "Unexpected Exception :", e);
			throw e;
		}

		if (!pass)
			throw new Exception("test3 failed");
	}

	/*
	 * @testName: test4
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:886; PERSISTENCE:SPEC:881
	 * 
	 * @test_Strategy: When a JTA application-managed entity manager is used, if the
	 * entity manager is created outside the scope of a current JTA transaction, it
	 * is the responsibility of the application to associate the entity manager with
	 * the transaction (if desired) by calling EntityManager.joinTransaction.
	 *
	 * The enitity manager factory is injected into the stateful session bean. The
	 * entity manager is obtained in the PostConstruct method of bean and closed
	 * with when the bean is removed by a business method annotated with the Remove
	 * annotation.
	 *
	 */
	@Test
	public void test4() throws Exception {

		log.info( "Begin test4");
		boolean pass = false;

		try {
			statefulBean = statefulBeanInstance.get();
			statefulBean.init(props);
			pass = statefulBean.test4();
		} catch (Exception e) {
			log.log(Level.SEVERE, "Unexpected Exception :", e);
			throw e;
		} finally {
			cleanup();
		}

		if (!pass)
			throw new Exception("test4 failed");
	}

	/*
	 * @testName: test5
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:164; PERSISTENCE:SPEC:2420;
	 * 
	 * @test_Strategy: Test the @PersistenceUnits and verify that a managed entity
	 * from one PU is not accessible in the other PU and visa versa.
	 */
	@Test
	public void test5() throws Exception {

		boolean pass = false;

		try {
			statefulBean2 = statefulBean2Instance.get();
			statefulBean2.init(props);
			pass = statefulBean2.test5();
		} catch (Exception e) {
			log.log(Level.SEVERE, "Unexpected Exception :", e);
			throw e;
		} finally {
			cleanup();
		}

		if (!pass)
			throw new Exception("test5 failed");
	}

	public void cleanup() throws Exception {
		try {
			if(statefulBean != null) {
				statefulBeanInstance.destroy(statefulBean);
				statefulBean = null;
			}
		} catch (Exception re) {
			log.log(Level.WARNING,"Unexpected Exception in entity cleanup:", re);
		}
		try {
			if(statefulBean2 != null) {
				statefulBean2Instance.destroy(statefulBean2);
				statefulBean2 = null;
			}
		} catch (Exception re) {
			log.log(Level.WARNING,"Unexpected Exception in entity cleanup:", re);
		}
		log.info( "cleanup complete");
	}

}
