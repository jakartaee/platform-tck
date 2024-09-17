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

package ee.jakarta.tck.persistence.core.criteriaapi.Join;



import com.sun.ts.lib.harness.Status;
import ee.jakarta.tck.persistence.common.schema30.Util;



import ee.jakarta.tck.persistence.common.schema30.Customer;
import ee.jakarta.tck.persistence.common.schema30.Customer_;
import ee.jakarta.tck.persistence.common.schema30.Department;
import ee.jakarta.tck.persistence.common.schema30.Employee;
import ee.jakarta.tck.persistence.common.schema30.Employee_;
import ee.jakarta.tck.persistence.common.schema30.Order;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;

public class Client1 extends Util {

	public static void main(String[] args) {
		Client1 theTests = new Client1();
		Status s = theTests.run(args, System.out, System.err);
		s.exit();
	}



	/*
	 * @testName: joinStringIllegalArgumentExceptionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1053; PERSISTENCE:JAVADOC:1055;
	 * 
	 * @test_Strategy:
	 *
	 */
		public void joinStringIllegalArgumentExceptionTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;
		logMsg( "String Test");
		try {
			CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> customer = cquery.from(Customer.class);
			Join<Customer, Order> order = customer.join(Customer_.orders);
			try {
				order.join("doesnotexist");
				logErr( "Did not throw IllegalArgumentException");
			} catch (IllegalArgumentException iae) {
				logTrace( "Received expected exception");
				pass1 = true;
			} catch (Exception e) {
				logErr( "Received unexpected exception", e);
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logErr( "Caught unexpected exception", e);

		}

		logMsg( "String, JoinType Test");

		try {
			CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> customer = cquery.from(Customer.class);
			Join<Customer, Order> order = customer.join(Customer_.orders);
			try {
				order.join("doesnotexist", JoinType.INNER);
				logErr( "Did not throw IllegalArgumentException");
			} catch (IllegalArgumentException iae) {
				logTrace( "Received expected exception");
				pass2 = true;
			} catch (Exception e) {
				logErr( "Received unexpected exception", e);
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logErr( "Caught unexpected exception", e);

		}

		if (!pass1 || !pass2) {
			throw new Exception("joinStringIllegalArgumentExceptionTest failed");
		}
	}

	/*
	 * @testName: joinCollectionIllegalArgumentExceptionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1057; PERSISTENCE:JAVADOC:1059;
	 * 
	 * @test_Strategy:
	 *
	 */
		public void joinCollectionIllegalArgumentExceptionTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;

		logMsg( "String Test");

		try {
			CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> customer = cquery.from(Customer.class);
			Join<Customer, Order> order = customer.join(Customer_.orders);
			try {
				order.joinCollection("doesnotexist");
				logErr( "Did not throw IllegalArgumentException");
			} catch (IllegalArgumentException iae) {
				logTrace( "Received expected exception");
				pass1 = true;
			} catch (Exception e) {
				logErr( "Received unexpected exception", e);
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logErr( "Caught unexpected exception", e);

		}

		logMsg( "String, JoinType Test");
		try {
			CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> customer = cquery.from(Customer.class);
			Join<Customer, Order> order = customer.join(Customer_.orders);
			try {
				order.joinCollection("doesnotexist", JoinType.INNER);
				logErr( "Did not throw IllegalArgumentException");
			} catch (IllegalArgumentException iae) {
				logTrace( "Received expected exception");
				pass2 = true;
			} catch (Exception e) {
				logErr( "Received unexpected exception", e);

			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logErr( "Caught unexpected exception", e);

		}

		if (!pass1 || !pass2) {
			throw new Exception("joinCollectionIllegalArgumentExceptionTest failed");
		}
	}

	/*
	 * @testName: joinSetIllegalArgumentExceptionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1069; PERSISTENCE:JAVADOC:1027;
	 * PERSISTENCE:JAVADOC:1071;
	 * 
	 * @test_Strategy:
	 *
	 */
		public void joinSetIllegalArgumentExceptionTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;
		logMsg( "String Test");

		try {
			CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> customer = cquery.from(Customer.class);
			Join<Customer, Order> order = customer.join(Customer_.orders2);
			try {
				order.joinSet("doesnotexist");
				logErr( "Did not throw IllegalArgumentException");
			} catch (IllegalArgumentException iae) {
				logTrace( "Received expected exception");
				pass1 = true;
			} catch (Exception e) {
				logErr( "Received unexpected exception", e);
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logErr( "Caught unexpected exception", e);

		}

		logMsg( "String, JoinType Test");
		try {
			CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> customer = cquery.from(Customer.class);
			Join<Customer, Order> order = customer.join(Customer_.orders2);
			try {
				order.joinSet("doesnotexist", JoinType.INNER);
				logErr( "Did not throw IllegalArgumentException");
			} catch (IllegalArgumentException iae) {
				logTrace( "Received expected exception");
				pass2 = true;
			} catch (Exception e) {
				logErr( "Received unexpected exception", e);
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logErr( "Caught unexpected exception", e);

		}

		if (!pass1 || !pass2) {
			throw new Exception("joinSetIllegalArgumentExceptionTest failed");
		}
	}

	/*
	 * @testName: joinListIllegalArgumentExceptionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1061; PERSISTENCE:JAVADOC:1063;
	 * 
	 * @test_Strategy:
	 */
		public void joinListIllegalArgumentExceptionTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;
		logMsg( "Testing String");

		try {
			CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> customer = cquery.from(Customer.class);
			Join<Customer, Order> order = customer.join("orders3");
			try {
				order.joinList("doesnotexist");
				logErr( "Did not throw IllegalArgumentException");
			} catch (IllegalArgumentException iae) {
				logTrace( "Received expected IllegalArgumentException");
				pass1 = true;
			} catch (Exception e) {
				logErr( "Received unexpected exception", e);
			}
			getEntityTransaction().commit();

		} catch (Exception e) {
			logErr( "Caught unexpected exception", e);

		}

		logMsg( "Testing String, JoinType");
		try {
			CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> customer = cquery.from(Customer.class);
			Join<Customer, Order> order = customer.join("orders3");
			try {
				order.joinList("doesnotexist", JoinType.INNER);
				logErr( "Did not throw IllegalArgumentException");
			} catch (IllegalArgumentException iae) {
				logTrace( "Received expected IllegalArgumentException");
				pass2 = true;
			} catch (Exception e) {
				logErr( "Received unexpected exception", e);
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logErr( "Caught unexpected exception", e);

		}

		if (!pass1 || !pass2) {
			throw new Exception("joinListIllegalArgumentExceptionTest failed");
		}
	}

	/*
	 * @testName: joinMapIllegalArgumentExceptionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1065; PERSISTENCE:JAVADOC:1067
	 * 
	 * @test_Strategy:
	 */
		public void joinMapIllegalArgumentExceptionTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;
		logMsg( "Testing String");

		try {
			CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

			getEntityTransaction().begin();
			CriteriaQuery<Employee> cquery = cbuilder.createQuery(Employee.class);
			Root<Employee> employee = cquery.from(Employee.class);
			Join<Employee, Department> department = employee.join(Employee_.department);

			try {
				department.joinMap("doesnotexist");
				logErr( "Did not throw IllegalArgumentException");
			} catch (IllegalArgumentException iae) {
				logTrace( "Received expected IllegalArgumentException");
				pass1 = true;
			} catch (Exception e) {
				logErr( "Received unexpected exception", e);
			}
			getEntityTransaction().commit();

		} catch (Exception e) {
			logErr( "Caught unexpected exception", e);

		}

		logMsg( "Testing String, JoinType");
		try {
			CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

			getEntityTransaction().begin();
			CriteriaQuery<Employee> cquery = cbuilder.createQuery(Employee.class);
			Root<Employee> employee = cquery.from(Employee.class);
			Join<Employee, Department> department = employee.join(Employee_.department);
			try {
				department.joinMap("doesnotexist", JoinType.INNER);
				logErr( "Did not throw IllegalArgumentException");
			} catch (IllegalArgumentException iae) {
				logTrace( "Received expected IllegalArgumentException");
				pass2 = true;
			} catch (Exception e) {
				logErr( "Received unexpected exception", e);
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logErr( "Caught unexpected exception", e);

		}

		if (!pass1 || !pass2) {
			throw new Exception("joinMapIllegalArgumentExceptionTest failed");
		}
	}
}
