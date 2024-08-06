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

package ee.jakarta.tck.persistence.core.criteriaapi.CriteriaQuery;


import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sun.ts.lib.harness.SetupMethod;

import ee.jakarta.tck.persistence.common.schema30.Util;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.ParameterExpression;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.metamodel.EntityType;

public class Client5 extends Util {

	

	public JavaArchive createDeployment() throws Exception {

		String pkgNameWithoutSuffix = Client5.class.getPackageName();
		String pkgName = pkgNameWithoutSuffix + ".";
		String[] classes = { pkgName + "A" };
		classes = concat(getSchema30classes(), classes);
		return createDeploymentJar("jpa_core_criteriaapi_CriteriaQuery5.jar", pkgNameWithoutSuffix, classes);
	}

	@BeforeEach
	public void setupAData() throws Exception {
		logTrace( "setupData");
		try {
			super.setup();
			createDeployment();
			removeATestData();
			createATestData();
		} catch (Exception e) {
			throw new Exception("Setup failed:", e);
		}
	}

	/*
	 * @testName: DoubleOperandResultTypeTests
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:1677; PERSISTENCE:SPEC:1677.1;
	 * PERSISTENCE:SPEC:1685;
	 * 
	 * @test_Strategy: Test various operands result in various types
	 *
	 */
	@SetupMethod(name = "setupAData")
	@Test
	public void DoubleOperandResultTypeTests() throws Exception {
		boolean pass1, pass2, pass3, pass4, pass5, pass6, pass7, pass8;
		pass1 = pass2 = pass3 = pass4 = pass5 = pass6 = pass7 = pass8 = false;
		Object p;
		try {
			logMsg( "Testing + Double operand:");
			getEntityTransaction().begin();
			CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
			CriteriaQuery cquery = cbuilder.createQuery();
			Root<A> a = cquery.from(A.class);
			EntityType<A> A_ = a.getModel();
			cquery.select(cbuilder.sum(a.get(A_.getSingularAttribute("basicBigDouble", Double.class)), 1));
			ParameterExpression<Double> param = cbuilder.parameter(Double.class);
			cquery.where(cbuilder.equal(a.get(A_.getSingularAttribute("basicBigDouble", Double.class)), param));
			double whereValue = 1234.5;
			p = getEntityManager().createQuery(cquery).setParameter(param, whereValue).getSingleResult();
			if (p instanceof Double) {
				logTrace( "Received expected Double type");
				pass1 = true;
			} else {
				logErr( "Result was not of type Double:" + p.getClass().getName());
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logErr( "Caught exception: ", e);
		}
		try {
			logMsg( "Testing - Double operand:");
			getEntityTransaction().begin();
			CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
			CriteriaQuery cquery = cbuilder.createQuery();
			Root<A> a = cquery.from(A.class);
			EntityType<A> A_ = a.getModel();
			cquery.select(cbuilder.diff(a.get(A_.getSingularAttribute("basicBigDouble", Double.class)), 1d));
			ParameterExpression<Double> param = cbuilder.parameter(Double.class);
			cquery.where(cbuilder.gt(a.get(A_.getSingularAttribute("basicBigDouble", Double.class)), param));
			double whereValue = 1234.0;
			p = getEntityManager().createQuery(cquery).setParameter(param, whereValue).getSingleResult();
			if (p instanceof Double) {
				logTrace( "Received expected Double type");
				pass2 = true;
			} else {
				logErr( "Result was not of type Double:" + p.getClass().getName());
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logErr( "Caught exception: ", e);
		}
		try {
			logMsg( "Testing * Double operand:");
			getEntityTransaction().begin();
			CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
			CriteriaQuery cquery = cbuilder.createQuery();
			Root<A> a = cquery.from(A.class);
			EntityType<A> A_ = a.getModel();
			cquery.select(cbuilder.prod(a.get(A_.getSingularAttribute("basicBigDouble", Double.class)), 1));
			ParameterExpression<Double> param = cbuilder.parameter(Double.class);
			cquery.where(cbuilder.lt(a.get(A_.getSingularAttribute("basicBigDouble", Double.class)), param));
			double whereValue = 1235.0;
			p = getEntityManager().createQuery(cquery).setParameter(param, whereValue).getSingleResult();
			if (p instanceof Double) {
				logTrace( "Received expected Double type");
				pass3 = true;
			} else {
				logErr( "Result was not of type Double:" + p.getClass().getName());
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logErr( "Caught exception: ", e);
		}
		try {
			logMsg( "Testing / Double operand:");
			getEntityTransaction().begin();
			CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
			CriteriaQuery cquery = cbuilder.createQuery();
			Root<A> a = cquery.from(A.class);
			EntityType<A> A_ = a.getModel();
			cquery.select(cbuilder.quot(a.get(A_.getSingularAttribute("basicBigDouble", Double.class)), 1));
			ParameterExpression<Double> param = cbuilder.parameter(Double.class);
			cquery.where(cbuilder.notEqual(a.get(A_.getSingularAttribute("basicBigDouble", Double.class)), param));
			double whereValue = 1235.0;
			p = getEntityManager().createQuery(cquery).setParameter(param, whereValue).getSingleResult();
			if (p instanceof Double) {
				logTrace( "Received expected Double type");
				pass4 = true;
			} else {
				logErr( "Result was not of type Double:" + p.getClass().getName());
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logErr( "Caught exception: ", e);
		}
		try {
			logMsg( "Testing + double operand:");
			getEntityTransaction().begin();
			CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
			CriteriaQuery cquery = cbuilder.createQuery();
			Root<A> a = cquery.from(A.class);
			EntityType<A> A_ = a.getModel();
			cquery.select(cbuilder.sum(a.get(A_.getSingularAttribute("basicDouble", Double.class)), 1));
			cquery.where(cbuilder.equal(a.get(A_.getSingularAttribute("id", String.class)), "9"));
			p = getEntityManager().createQuery(cquery).getSingleResult();
			if (p instanceof Double) {
				logTrace( "Received expected double type");
				pass5 = true;
			} else {
				logErr( "Result was not of type double:" + p.getClass().getName());
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logErr( "Caught exception: ", e);
		}
		try {
			logMsg( "Testing - double operand:");
			getEntityTransaction().begin();
			CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
			CriteriaQuery cquery = cbuilder.createQuery();
			Root<A> a = cquery.from(A.class);
			EntityType<A> A_ = a.getModel();
			cquery.select(cbuilder.diff(a.get(A_.getSingularAttribute("basicDouble", Double.class)), 1d));
			cquery.where(cbuilder.equal(a.get(A_.getSingularAttribute("id", String.class)), "9"));
			p = getEntityManager().createQuery(cquery).getSingleResult();
			if (p instanceof Double) {
				logTrace( "Received expected double type");
				pass6 = true;
			} else {
				logErr( "Result was not of type double:" + p.getClass().getName());
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logErr( "Caught exception: ", e);
		}
		try {
			logMsg( "Testing * double operand:");
			getEntityTransaction().begin();
			CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
			CriteriaQuery cquery = cbuilder.createQuery();
			Root<A> a = cquery.from(A.class);
			EntityType<A> A_ = a.getModel();
			cquery.select(cbuilder.prod(a.get(A_.getSingularAttribute("basicDouble", Double.class)), 1));
			cquery.where(cbuilder.equal(a.get(A_.getSingularAttribute("id", String.class)), "9"));
			p = getEntityManager().createQuery(cquery).getSingleResult();
			if (p instanceof Double) {
				logTrace( "Received expected double type");
				pass7 = true;
			} else {
				logErr( "Result was not of type double:" + p.getClass().getName());
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logErr( "Caught exception: ", e);
		}
		try {
			logMsg( "Testing / double operand:");
			getEntityTransaction().begin();
			CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
			CriteriaQuery cquery = cbuilder.createQuery();
			Root<A> a = cquery.from(A.class);
			EntityType<A> A_ = a.getModel();
			cquery.select(cbuilder.quot(a.get(A_.getSingularAttribute("basicDouble", Double.class)), 1));
			cquery.where(cbuilder.equal(a.get(A_.getSingularAttribute("id", String.class)), "9"));
			p = getEntityManager().createQuery(cquery).getSingleResult();
			if (p instanceof Double) {
				logTrace( "Received expected double type");
				pass8 = true;
			} else {
				logErr( "Result was not of type double:" + p.getClass().getName());
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logErr( "Caught exception: ", e);
		}
		if (!pass1 || !pass2 || !pass3 || !pass4 || !pass5 || !pass6 || !pass7 || !pass8)
			throw new Exception("DoubleOperandResultTypeTests failed");
	}

	/*
	 * @testName: FloatOperandResultTypeTests
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:1677; PERSISTENCE:SPEC:1677.2;
	 * 
	 * @test_Strategy: Test various operands result in various types
	 *
	 */
	@SetupMethod(name = "setupAData")
	@Test
	public void FloatOperandResultTypeTests() throws Exception {
		boolean pass1, pass2, pass3, pass4, pass5, pass6, pass7, pass8;
		pass1 = pass2 = pass3 = pass4 = pass5 = pass6 = pass7 = pass8 = false;
		Object p;
		try {
			logMsg( "Testing + Float operand:");
			getEntityTransaction().begin();
			CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
			CriteriaQuery cquery = cbuilder.createQuery();
			Root<A> a = cquery.from(A.class);
			EntityType<A> A_ = a.getModel();

			cquery.select(cbuilder.sum(a.get(A_.getSingularAttribute("basicBigFloat", Float.class)), 1));
			cquery.where(cbuilder.equal(a.get(A_.getSingularAttribute("id", String.class)), "9"));
			p = getEntityManager().createQuery(cquery).getSingleResult();
			if (p instanceof Float) {
				logTrace( "Received expected Float type");
				pass1 = true;
			} else {
				logErr( "Result was not of type Float:" + p.getClass().getName());
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logErr( "Caught exception: ", e);
		}
		try {
			logMsg( "Testing - Float operand:");
			getEntityTransaction().begin();
			CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
			CriteriaQuery cquery = cbuilder.createQuery();
			Root<A> a = cquery.from(A.class);
			EntityType<A> A_ = a.getModel();

			cquery.select(cbuilder.diff(a.get(A_.getSingularAttribute("basicBigFloat", Float.class)), 1f));
			cquery.where(cbuilder.equal(a.get(A_.getSingularAttribute("id", String.class)), "9"));
			p = getEntityManager().createQuery(cquery).getSingleResult();
			if (p instanceof Float) {
				logTrace( "Received expected Float type");
				pass2 = true;
			} else {
				logErr( "Result was not of type Float:" + p.getClass().getName());
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logErr( "Caught exception: ", e);
		}
		try {
			logMsg( "Testing * Float operand:");
			getEntityTransaction().begin();
			CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
			CriteriaQuery cquery = cbuilder.createQuery();
			Root<A> a = cquery.from(A.class);
			EntityType<A> A_ = a.getModel();

			cquery.select(cbuilder.prod(a.get(A_.getSingularAttribute("basicBigFloat", Float.class)), 1));
			cquery.where(cbuilder.equal(a.get(A_.getSingularAttribute("id", String.class)), "9"));
			p = getEntityManager().createQuery(cquery).getSingleResult();
			if (p instanceof Float) {
				logTrace( "Received expected Float type");
				pass3 = true;
			} else {
				logErr( "Result was not of type Float:" + p.getClass().getName());
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logErr( "Caught exception: ", e);
		}
		try {
			logMsg( "Testing / Float operand:");
			getEntityTransaction().begin();
			CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
			CriteriaQuery cquery = cbuilder.createQuery();
			Root<A> a = cquery.from(A.class);
			EntityType<A> A_ = a.getModel();

			cquery.select(cbuilder.quot(a.get(A_.getSingularAttribute("basicBigFloat", Float.class)), 1));
			cquery.where(cbuilder.equal(a.get(A_.getSingularAttribute("id", String.class)), "9"));
			p = getEntityManager().createQuery(cquery).getSingleResult();
			if (p instanceof Float) {
				logTrace( "Received expected Float type");
				pass4 = true;
			} else {
				logErr( "Result was not of type Float:" + p.getClass().getName());
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logErr( "Caught exception: ", e);
		}
		try {
			logMsg( "Testing + float operand:");
			getEntityTransaction().begin();
			CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
			CriteriaQuery cquery = cbuilder.createQuery();
			Root<A> a = cquery.from(A.class);
			EntityType<A> A_ = a.getModel();

			cquery.select(cbuilder.sum(a.get(A_.getSingularAttribute("basicFloat", Float.class)), 1));
			cquery.where(cbuilder.equal(a.get(A_.getSingularAttribute("id", String.class)), "9"));
			p = getEntityManager().createQuery(cquery).getSingleResult();
			if (p instanceof Float) {
				logTrace( "Received expected float type");
				pass5 = true;
			} else {
				logErr( "Result was not of type float:" + p.getClass().getName());
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logErr( "Caught exception: ", e);
		}
		try {
			logMsg( "Testing - float operand:");
			getEntityTransaction().begin();
			CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
			CriteriaQuery cquery = cbuilder.createQuery();
			Root<A> a = cquery.from(A.class);
			EntityType<A> A_ = a.getModel();

			cquery.select(cbuilder.diff(a.get(A_.getSingularAttribute("basicFloat", Float.class)), 1f));
			cquery.where(cbuilder.equal(a.get(A_.getSingularAttribute("id", String.class)), "9"));
			p = getEntityManager().createQuery(cquery).getSingleResult();
			if (p instanceof Float) {
				logTrace( "Received expected float type");
				pass6 = true;
			} else {
				logErr( "Result was not of type float:" + p.getClass().getName());
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logErr( "Caught exception: ", e);
		}
		try {
			logMsg( "Testing * float operand:");
			getEntityTransaction().begin();
			CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
			CriteriaQuery cquery = cbuilder.createQuery();
			Root<A> a = cquery.from(A.class);
			EntityType<A> A_ = a.getModel();

			cquery.select(cbuilder.prod(a.get(A_.getSingularAttribute("basicFloat", Float.class)), 1));
			cquery.where(cbuilder.equal(a.get(A_.getSingularAttribute("id", String.class)), "9"));
			p = getEntityManager().createQuery(cquery).getSingleResult();
			if (p instanceof Float) {
				logTrace( "Received expected float type");
				pass7 = true;
			} else {
				logErr( "Result was not of type float:" + p.getClass().getName());
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logErr( "Caught exception: ", e);
		}
		try {
			logMsg( "Testing / float operand:");
			getEntityTransaction().begin();
			CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
			CriteriaQuery cquery = cbuilder.createQuery();
			Root<A> a = cquery.from(A.class);
			EntityType<A> A_ = a.getModel();

			cquery.select(cbuilder.quot(a.get(A_.getSingularAttribute("basicFloat", Float.class)), 1));
			cquery.where(cbuilder.equal(a.get(A_.getSingularAttribute("id", String.class)), "9"));
			p = getEntityManager().createQuery(cquery).getSingleResult();
			if (p instanceof Float) {
				logTrace( "Received expected Float type");
				pass8 = true;
			} else {
				logErr( "Result was not of type Float:" + p.getClass().getName());
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logErr( "Caught exception: ", e);
		}
		if (!pass1 || !pass2 || !pass3 || !pass4 || !pass5 || !pass6 || !pass7 || !pass8)
			throw new Exception("FloatOperandResultTypeTests failed");
	}

	/*
	 * @testName: BigDecimalOperandResultTypeTests
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:1677; PERSISTENCE:SPEC:1677.3;
	 * 
	 * @test_Strategy: Test various operands result in various types
	 *
	 */
	@SetupMethod(name = "setupAData")
	@Test
	public void BigDecimalOperandResultTypeTests() throws Exception {
		boolean pass1, pass2, pass3, pass4;
		pass1 = pass2 = pass3 = pass4 = false;
		Object p;
		try {
			logMsg( "Testing + BigDecimal operand:");
			getEntityTransaction().begin();
			CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
			CriteriaQuery cquery = cbuilder.createQuery();
			Root<A> a = cquery.from(A.class);
			EntityType<A> A_ = a.getModel();

			cquery.select(cbuilder.sum(a.get(A_.getSingularAttribute("basicBigDecimal", BigDecimal.class)), 1));
			cquery.where(cbuilder.equal(a.get(A_.getSingularAttribute("id", String.class)), "9"));
			p = getEntityManager().createQuery(cquery).getSingleResult();
			if (p instanceof BigDecimal) {
				logTrace( "Received expected BigDecimal type");
				pass1 = true;
			} else {
				logErr( "Result was not of type BigDecimal:" + p.getClass().getName());
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logErr( "Caught exception: ", e);
		}
		try {
			logMsg( "Testing - BigDecimal operand:");
			getEntityTransaction().begin();
			CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
			CriteriaQuery cquery = cbuilder.createQuery();
			Root<A> a = cquery.from(A.class);
			EntityType<A> A_ = a.getModel();

			cquery.select(cbuilder.diff(a.get(A_.getSingularAttribute("basicBigDecimal", BigDecimal.class)),
					new BigDecimal(1)));
			cquery.where(cbuilder.equal(a.get(A_.getSingularAttribute("id", String.class)), "9"));
			p = getEntityManager().createQuery(cquery).getSingleResult();
			if (p instanceof BigDecimal) {
				logTrace( "Received expected BigDecimal type");
				pass2 = true;
			} else {
				logErr( "Result was not of type BigDecimal:" + p.getClass().getName());
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logErr( "Caught exception: ", e);
		}
		try {
			logMsg( "Testing * BigDecimal operand:");
			getEntityTransaction().begin();
			CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
			CriteriaQuery cquery = cbuilder.createQuery();
			Root<A> a = cquery.from(A.class);
			EntityType<A> A_ = a.getModel();

			cquery.select(cbuilder.prod(a.get(A_.getSingularAttribute("basicBigDecimal", BigDecimal.class)), 1));
			cquery.where(cbuilder.equal(a.get(A_.getSingularAttribute("id", String.class)), "9"));
			p = getEntityManager().createQuery(cquery).getSingleResult();
			if (p instanceof BigDecimal) {
				logTrace( "Received expected BigDecimal type");
				pass3 = true;
			} else {
				logErr( "Result was not of type BigDecimal:" + p.getClass().getName());
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logErr( "Caught exception: ", e);
		}
		try {
			logMsg( "Testing / BigDecimal operand:");
			getEntityTransaction().begin();
			CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
			CriteriaQuery cquery = cbuilder.createQuery();
			Root<A> a = cquery.from(A.class);
			EntityType<A> A_ = a.getModel();

			cquery.select(cbuilder.quot(a.get(A_.getSingularAttribute("basicBigDecimal", BigDecimal.class)), 1));
			cquery.where(cbuilder.equal(a.get(A_.getSingularAttribute("id", String.class)), "9"));
			p = getEntityManager().createQuery(cquery).getSingleResult();

			if (p instanceof BigDecimal) {
				logTrace( "Received expected BigDecimal type");
				pass4 = true;
			} else {
				logErr( "Result was not of type BigDecimal:" + p.getClass().getName());
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logErr( "Caught exception: ", e);
		}
		if (!pass1 || !pass2 || !pass3 || !pass4)
			throw new Exception("BigDecimalOperandResultTypeTests failed");
	}

	/*
	 * @testName: BigIntegerOperandResultTypeTests
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:1677; PERSISTENCE:SPEC:1677.4;
	 * 
	 * @test_Strategy: Test various operands result in various types
	 *
	 */
	@SetupMethod(name = "setupAData")
	@Test
	public void BigIntegerOperandResultTypeTests() throws Exception {
		boolean pass1, pass2, pass3;
		pass1 = pass2 = pass3 = false;
		Object p;
		try {
			logMsg( "Testing + BigInteger operand:");
			getEntityTransaction().begin();
			CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
			CriteriaQuery cquery = cbuilder.createQuery();
			Root<A> a = cquery.from(A.class);
			EntityType<A> A_ = a.getModel();

			cquery.select(cbuilder.sum(a.get(A_.getSingularAttribute("basicBigInteger", BigInteger.class)), 1));
			cquery.where(cbuilder.equal(a.get(A_.getSingularAttribute("id", String.class)), "9"));
			p = getEntityManager().createQuery(cquery).getSingleResult();
			if (p instanceof BigInteger) {
				logTrace( "Received expected BigInteger type");
				pass1 = true;
			} else {
				logErr( "Result was not of type BigInteger:" + p.getClass().getName());
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logErr( "Caught exception: ", e);
		}
		try {
			logMsg( "Testing - BigInteger operand:");
			getEntityTransaction().begin();
			CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
			CriteriaQuery cquery = cbuilder.createQuery();
			Root<A> a = cquery.from(A.class);
			EntityType<A> A_ = a.getModel();

			cquery.select(cbuilder.diff(a.get(A_.getSingularAttribute("basicBigInteger", BigInteger.class)),
					new BigInteger("1")));
			cquery.where(cbuilder.equal(a.get(A_.getSingularAttribute("id", String.class)), "9"));
			p = getEntityManager().createQuery(cquery).getSingleResult();
			if (p instanceof BigInteger) {
				logTrace( "Received expected BigInteger type");
				pass2 = true;
			} else {
				logErr( "Result was not of type BigInteger:" + p.getClass().getName());
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logErr( "Caught exception: ", e);
		}
		try {
			logMsg( "Testing * BigInteger operand:");
			getEntityTransaction().begin();
			CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
			CriteriaQuery cquery = cbuilder.createQuery();
			Root<A> a = cquery.from(A.class);
			EntityType<A> A_ = a.getModel();

			cquery.select(cbuilder.prod(a.get(A_.getSingularAttribute("basicBigInteger", BigInteger.class)), 1));
			cquery.where(cbuilder.equal(a.get(A_.getSingularAttribute("id", String.class)), "9"));
			p = getEntityManager().createQuery(cquery).getSingleResult();
			if (p instanceof BigInteger) {
				logTrace( "Received expected BigInteger type");
				pass3 = true;
			} else {
				logErr( "Result was not of type BigInteger:" + p.getClass().getName());
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logErr( "Caught exception: ", e);
		}

		if (!pass1 || !pass2 || !pass3)
			throw new Exception("BigIntegerOperandResultTypeTests failed");
	}

	/*
	 * @testName: LongOperandResultTypeTests
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:1677; PERSISTENCE:SPEC:1677.5;
	 * 
	 * @test_Strategy: Test various operands result in various types
	 *
	 */
	@SetupMethod(name = "setupAData")
	@Test
	public void LongOperandResultTypeTests() throws Exception {
		boolean pass1, pass2, pass3, pass4, pass5, pass6;
		pass1 = pass2 = pass3 = pass4 = pass5 = pass6 = false;
		Object p;
		try {
			logMsg( "Testing + Long operand:");
			getEntityTransaction().begin();
			CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
			CriteriaQuery cquery = cbuilder.createQuery();
			Root<A> a = cquery.from(A.class);
			EntityType<A> A_ = a.getModel();

			cquery.select(cbuilder.sum(a.get(A_.getSingularAttribute("basicBigLong", Long.class)), 1));
			cquery.where(cbuilder.equal(a.get(A_.getSingularAttribute("id", String.class)), "9"));
			p = getEntityManager().createQuery(cquery).getSingleResult();
			if (p instanceof Long) {
				logTrace( "Received expected Long type");
				pass1 = true;
			} else {
				logErr( "Result was not of type Long:" + p.getClass().getName());
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logErr( "Caught exception: ", e);
		}
		try {
			logMsg( "Testing - Long operand:");
			getEntityTransaction().begin();
			CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
			CriteriaQuery cquery = cbuilder.createQuery();
			Root<A> a = cquery.from(A.class);
			EntityType<A> A_ = a.getModel();

			cquery.select(cbuilder.diff(a.get(A_.getSingularAttribute("basicBigLong", Long.class)), 1L));
			cquery.where(cbuilder.equal(a.get(A_.getSingularAttribute("id", String.class)), "9"));
			p = getEntityManager().createQuery(cquery).getSingleResult();
			if (p instanceof Long) {
				logTrace( "Received expected Long type");
				pass2 = true;
			} else {
				logErr( "Result was not of type Long:" + p.getClass().getName());
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logErr( "Caught exception: ", e);
		}
		try {
			logMsg( "Testing * Long operand:");
			getEntityTransaction().begin();
			CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
			CriteriaQuery cquery = cbuilder.createQuery();
			Root<A> a = cquery.from(A.class);
			EntityType<A> A_ = a.getModel();

			cquery.select(cbuilder.prod(a.get(A_.getSingularAttribute("basicBigLong", Long.class)), 1));
			cquery.where(cbuilder.equal(a.get(A_.getSingularAttribute("id", String.class)), "9"));
			p = getEntityManager().createQuery(cquery).getSingleResult();
			if (p instanceof Long) {
				logTrace( "Received expected Long type");
				pass3 = true;
			} else {
				logErr( "Result was not of type Long:" + p.getClass().getName());
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logErr( "Caught exception: ", e);
		}

		try {
			logMsg( "Testing + long operand:");
			getEntityTransaction().begin();
			CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
			CriteriaQuery cquery = cbuilder.createQuery();
			Root<A> a = cquery.from(A.class);
			EntityType<A> A_ = a.getModel();

			cquery.select(cbuilder.sum(a.get(A_.getSingularAttribute("basicLong", Long.class)), 1));
			cquery.where(cbuilder.equal(a.get(A_.getSingularAttribute("id", String.class)), "9"));
			p = getEntityManager().createQuery(cquery).getSingleResult();
			if (p instanceof Long) {
				logTrace( "Received expected long type");
				pass4 = true;
			} else {
				logErr( "Result was not of type long:" + p.getClass().getName());
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logErr( "Caught exception: ", e);
		}
		try {
			logMsg( "Testing - long operand:");
			getEntityTransaction().begin();
			CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
			CriteriaQuery cquery = cbuilder.createQuery();
			Root<A> a = cquery.from(A.class);
			EntityType<A> A_ = a.getModel();

			cquery.select(cbuilder.diff(a.get(A_.getSingularAttribute("basicLong", Long.class)), 1L));
			cquery.where(cbuilder.equal(a.get(A_.getSingularAttribute("id", String.class)), "9"));
			p = getEntityManager().createQuery(cquery).getSingleResult();
			if (p instanceof Long) {
				logTrace( "Received expected long type");
				pass5 = true;
			} else {
				logErr( "Result was not of type long:" + p.getClass().getName());
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logErr( "Caught exception: ", e);
		}
		try {
			logMsg( "Testing * long operand:");
			getEntityTransaction().begin();
			CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
			CriteriaQuery cquery = cbuilder.createQuery();
			Root<A> a = cquery.from(A.class);
			EntityType<A> A_ = a.getModel();

			cquery.select(cbuilder.prod(a.get(A_.getSingularAttribute("basicLong", Long.class)), 1));
			cquery.where(cbuilder.equal(a.get(A_.getSingularAttribute("id", String.class)), "9"));
			p = getEntityManager().createQuery(cquery).getSingleResult();
			if (p instanceof Long) {
				logTrace( "Received expected long type");
				pass6 = true;
			} else {
				logErr( "Result was not of type long:" + p.getClass().getName());
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logErr( "Caught exception: ", e);
		}

		if (!pass1 || !pass2 || !pass3 || !pass4 || !pass5 || !pass6)
			throw new Exception("LongOperandResultTypeTests failed");
	}

	/*
	 * @testName: ShortOperandResultTypeTests
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:1677; PERSISTENCE:SPEC:1677.6;
	 * 
	 * @test_Strategy: Test various operands of integral type and verify the result
	 * of the operation is of type Integer
	 *
	 */
	@SetupMethod(name = "setupAData")
	@Test
	public void ShortOperandResultTypeTests() throws Exception {
		boolean pass1, pass2, pass3, pass4, pass5, pass6;
		pass1 = pass2 = pass3 = pass4 = pass5 = pass6 = false;
		Object p;
		try {
			logMsg( "Testing + Short operand:");
			getEntityTransaction().begin();
			CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
			CriteriaQuery cquery = cbuilder.createQuery();
			Root<A> a = cquery.from(A.class);
			EntityType<A> A_ = a.getModel();

			cquery.select(cbuilder.sum(a.get(A_.getSingularAttribute("basicBigShort", Short.class)), 1));
			cquery.where(cbuilder.equal(a.get(A_.getSingularAttribute("id", String.class)), "9"));
			p = getEntityManager().createQuery(cquery).getSingleResult();
			if (p instanceof Integer) {
				logTrace( "Received expected Integer type");
				pass1 = true;
			} else {
				logErr( "Result was not of type Integer:" + p.getClass().getName());
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logErr( "Caught exception: ", e);
		}
		try {
			logMsg( "Testing - Short operand:");
			getEntityTransaction().begin();
			CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
			CriteriaQuery cquery = cbuilder.createQuery();
			Root<A> a = cquery.from(A.class);
			EntityType<A> A_ = a.getModel();

			cquery.select(cbuilder.diff(a.get(A_.getSingularAttribute("basicBigShort", Short.class)), 1));
			cquery.where(cbuilder.equal(a.get(A_.getSingularAttribute("id", String.class)), "9"));
			p = getEntityManager().createQuery(cquery).getSingleResult();
			if (p instanceof Integer) {
				logTrace( "Received expected Integer type");
				pass2 = true;
			} else {
				logErr( "Result was not of type Integer:" + p.getClass().getName());
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logErr( "Caught exception: ", e);
		}
		try {
			logMsg( "Testing * Short operand:");
			getEntityTransaction().begin();
			CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
			CriteriaQuery cquery = cbuilder.createQuery();
			Root<A> a = cquery.from(A.class);
			EntityType<A> A_ = a.getModel();

			cquery.select(cbuilder.prod(a.get(A_.getSingularAttribute("basicBigShort", Short.class)), 1));
			cquery.where(cbuilder.equal(a.get(A_.getSingularAttribute("id", String.class)), "9"));
			p = getEntityManager().createQuery(cquery).getSingleResult();
			if (p instanceof Integer) {
				logTrace( "Received expected Integer type");
				pass3 = true;
			} else {
				logErr( "Result was not of type Integer:" + p.getClass().getName());
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logErr( "Caught exception: ", e);
		}
		try {
			logMsg( "Testing + short operand:");
			getEntityTransaction().begin();
			CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
			CriteriaQuery cquery = cbuilder.createQuery();
			Root<A> a = cquery.from(A.class);
			EntityType<A> A_ = a.getModel();

			cquery.select(cbuilder.sum(a.get(A_.getSingularAttribute("basicShort", Short.class)), 1));
			cquery.where(cbuilder.equal(a.get(A_.getSingularAttribute("id", String.class)), "9"));
			p = getEntityManager().createQuery(cquery).getSingleResult();
			if (p instanceof Integer) {
				logTrace( "Received expected Integer type");
				pass4 = true;
			} else {
				logErr( "Result was not of type Integer:" + p.getClass().getName());
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logErr( "Caught exception: ", e);
		}
		try {
			logMsg( "Testing - short operand:");
			getEntityTransaction().begin();
			CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
			CriteriaQuery cquery = cbuilder.createQuery();
			Root<A> a = cquery.from(A.class);
			EntityType<A> A_ = a.getModel();

			cquery.select(cbuilder.diff(a.get(A_.getSingularAttribute("basicShort", Short.class)), 1));
			cquery.where(cbuilder.equal(a.get(A_.getSingularAttribute("id", String.class)), "9"));
			p = getEntityManager().createQuery(cquery).getSingleResult();
			if (p instanceof Integer) {
				logTrace( "Received expected Integer type");
				pass5 = true;
			} else {
				logErr( "Result was not of type Integer:" + p.getClass().getName());
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logErr( "Caught exception: ", e);
		}
		try {
			logMsg( "Testing * short operand:");
			getEntityTransaction().begin();
			CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
			CriteriaQuery cquery = cbuilder.createQuery();
			Root<A> a = cquery.from(A.class);
			EntityType<A> A_ = a.getModel();

			cquery.select(cbuilder.prod(a.get(A_.getSingularAttribute("basicShort", Short.class)), 1));
			cquery.where(cbuilder.equal(a.get(A_.getSingularAttribute("id", String.class)), "9"));
			p = getEntityManager().createQuery(cquery).getSingleResult();
			if (p instanceof Integer) {
				logTrace( "Received expected Integer type");
				pass6 = true;
			} else {
				logErr( "Result was not of type Integer:" + p.getClass().getName());
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logErr( "Caught exception: ", e);
		}

		if (!pass1 || !pass2 || !pass3 || !pass4 || !pass5 || !pass6)
			throw new Exception("ShortOperandResultTypeTests failed");
	}

	public void createATestData() {
		try {
			getEntityTransaction().begin();
			final Integer integer = 1234;
			final short basicShort = 12;
			final Short basicBigShort = basicShort;
			final float basicFloat = 12.3f;
			final Float basicBigFloat = basicFloat;
			final long basicLong = 1234l;
			final Long basicBigLong = basicLong;
			final double basicDouble = 1234.5;
			final Double basicBigDouble = basicDouble;
			final char[] charArray = { 'a', 'b', 'c' };
			final Character[] bigCharacterArray = { 'a', 'b', 'c' };
			final byte[] byteArray = "abc".getBytes();
			final Byte[] bigByteArray = { (byte) 111, (byte) 101, (byte) 100 };
			final BigInteger bigInteger = new BigInteger("12345");
			final BigDecimal bigDecimal = new BigDecimal(bigInteger);
			final Date date = new Date();
			final long timeInSeconds = date.getTime();
			final Time time = new Time(timeInSeconds);
			final Timestamp timeStamp = new Timestamp(timeInSeconds);
			final Calendar calendar = Calendar.getInstance();

			A aRef = new A("9", null, 9, integer, basicShort, basicBigShort, basicFloat, basicBigFloat, basicLong,
					basicBigLong, basicDouble, basicBigDouble, 'a', charArray, bigCharacterArray, byteArray,
					bigByteArray, bigInteger, bigDecimal, date, time, timeStamp, calendar);

			getEntityManager().persist(aRef);
			getEntityManager().flush();
			getEntityTransaction().commit();

		} catch (Exception e) {
			logErr( "Unexpected Exception in createTestData:", e);
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

	private void removeATestData() {
		logTrace( "removeTestData");
		if (getEntityTransaction().isActive()) {
			getEntityTransaction().rollback();
		}
		try {
			getEntityTransaction().begin();
			getEntityManager().createNativeQuery("DELETE FROM A_BASIC").executeUpdate();
			getEntityTransaction().commit();
		} catch (Exception e) {
			logErr( "Exception encountered while removing entities:", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logErr( "Unexpected Exception in removeTestData:", re);
			}
		}
	}
}
