/*
 * Copyright (c) 2008, 2023 Oracle and/or its affiliates. All rights reserved.
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

package ee.jakarta.tck.persistence.core.annotations.basic;


import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Properties;
import com.sun.ts.lib.harness.Status;
import java.util.Properties;

public class Client2 extends Client {

	public static void main(String[] args) {
		Client2 theTests = new Client2();
		Status s = theTests.run(args, System.out, System.err);
		s.exit();
	}
  
	
	public Client2() {
	}

	
	public void setupData(String[] args, Properties p) throws Exception {
		logTrace( "setupData");
		try {
			super.setup(args,p);
			removeTestData();
			createTestData();
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
	
	public void DoubleOperandResultTypeTests() throws Exception {
		boolean pass1, pass2, pass3, pass4, pass5, pass6, pass7, pass8;
		pass1 = pass2 = pass3 = pass4 = pass5 = pass6 = pass7 = pass8 = false;
		Object p;

		try {
			logMsg( "Testing + Double operand:");
			getEntityTransaction().begin();
			double whereValue = 1234.5;
			p = getEntityManager().createQuery("Select (a.basicBigDouble + 1) From A a where (a.basicBigDouble = ?1)")
					.setParameter(1, whereValue).getSingleResult();
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
			double whereValue = 1234.0;
			p = getEntityManager().createQuery("Select (a.basicBigDouble - 1) From A a where (a.basicBigDouble > ?1)")
					.setParameter(1, whereValue).getSingleResult();
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
			double whereValue = 1235.0;
			p = getEntityManager().createQuery("Select (a.basicBigDouble * 1) From A a where (a.basicBigDouble < ?1)")
					.setParameter(1, whereValue).getSingleResult();

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
			double whereValue = 1235.0;
			p = getEntityManager().createQuery("Select (a.basicBigDouble / 1) From A a where (a.basicBigDouble <> ?1)")
					.setParameter(1, whereValue).getSingleResult();
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
			p = getEntityManager().createQuery("Select (a.basicDouble + 1) From A a where (a.id = '9')")
					.getSingleResult();
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
			p = getEntityManager().createQuery("Select (a.basicDouble - 1) From A a where (a.id = '9')")
					.getSingleResult();
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
			p = getEntityManager().createQuery("Select (a.basicDouble * 1) From A a where (a.id = '9')")
					.getSingleResult();

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
			p = getEntityManager().createQuery("Select (a.basicDouble / 1) From A a where (a.id = '9')")
					.getSingleResult();

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
	
	public void FloatOperandResultTypeTests() throws Exception {
		boolean pass1, pass2, pass3, pass4, pass5, pass6, pass7, pass8;
		pass1 = pass2 = pass3 = pass4 = pass5 = pass6 = pass7 = pass8 = false;
		Object p;
		try {
			logMsg( "Testing + Float operand:");
			getEntityTransaction().begin();
			p = getEntityManager().createQuery("Select (a.basicBigFloat + 1) From A a where (a.id = '9')")
					.getSingleResult();
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
			p = getEntityManager().createQuery("Select (a.basicBigFloat - 1) From A a where (a.id = '9')")
					.getSingleResult();
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
			p = getEntityManager().createQuery("Select (a.basicBigFloat * 1) From A a where (a.id = '9')")
					.getSingleResult();

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
			p = getEntityManager().createQuery("Select (a.basicBigFloat / 1) From A a where (a.id = '9')")
					.getSingleResult();

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
			p = getEntityManager().createQuery("Select (a.basicFloat + 1) From A a where (a.id = '9')")
					.getSingleResult();
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
			p = getEntityManager().createQuery("Select (a.basicFloat - 1) From A a where (a.id = '9')")
					.getSingleResult();
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
			p = getEntityManager().createQuery("Select (a.basicFloat * 1) From A a where (a.id = '9')")
					.getSingleResult();

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
			p = getEntityManager().createQuery("Select (a.basicBigFloat / 1) From A a where (a.id = '9')")
					.getSingleResult();

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
	
	public void BigDecimalOperandResultTypeTests() throws Exception {
		boolean pass1, pass2, pass3, pass4;
		pass1 = pass2 = pass3 = pass4 = false;
		Object p;
		try {
			logMsg( "Testing + BigDecimal operand:");
			getEntityTransaction().begin();
			p = getEntityManager().createQuery("Select (a.basicBigDecimal + 1) From A a where (a.id = '9')")
					.getSingleResult();
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
			p = getEntityManager().createQuery("Select (a.basicBigDecimal - 1) From A a where (a.id = '9')")
					.getSingleResult();
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
			p = getEntityManager().createQuery("Select (a.basicBigDecimal * 1) From A a where (a.id = '9')")
					.getSingleResult();

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
			p = getEntityManager().createQuery("Select (a.basicBigDecimal / 1) From A a where (a.id = '9')")
					.getSingleResult();

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
	
	public void BigIntegerOperandResultTypeTests() throws Exception {
		boolean pass1, pass2, pass3;
		pass1 = pass2 = pass3 = false;
		Object p;
		try {
			logMsg( "Testing + BigInteger operand:");
			getEntityTransaction().begin();
			p = getEntityManager().createQuery("Select (a.basicBigInteger + 1) From A a where (a.id = '9')")
					.getSingleResult();
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
			p = getEntityManager().createQuery("Select (a.basicBigInteger - 1) From A a where (a.id = '9')")
					.getSingleResult();
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
			p = getEntityManager().createQuery("Select (a.basicBigInteger * 1) From A a where (a.id = '9')")
					.getSingleResult();

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
	
	public void LongOperandResultTypeTests() throws Exception {
		boolean pass1, pass2, pass3, pass4, pass5, pass6;
		pass1 = pass2 = pass3 = pass4 = pass5 = pass6 = false;
		Object p;
		try {
			logMsg( "Testing + Long operand:");
			getEntityTransaction().begin();
			p = getEntityManager().createQuery("Select (a.basicBigLong + 1) From A a where (a.id = '9')")
					.getSingleResult();
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
			p = getEntityManager().createQuery("Select (a.basicBigLong - 1) From A a where (a.id = '9')")
					.getSingleResult();
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
			p = getEntityManager().createQuery("Select (a.basicBigLong * 1) From A a where (a.id = '9')")
					.getSingleResult();

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
			p = getEntityManager().createQuery("Select (a.basicLong + 1) From A a where (a.id = '9')")
					.getSingleResult();
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
			p = getEntityManager().createQuery("Select (a.basicLong - 1) From A a where (a.id = '9')")
					.getSingleResult();
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
			p = getEntityManager().createQuery("Select (a.basicLong * 1) From A a where (a.id = '9')")
					.getSingleResult();

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
	
	public void ShortOperandResultTypeTests() throws Exception {
		boolean pass1, pass2, pass3, pass4, pass5, pass6;
		pass1 = pass2 = pass3 = pass4 = pass5 = pass6 = false;
		Object p;
		try {
			logMsg( "Testing + Short operand:");
			getEntityTransaction().begin();
			p = getEntityManager().createQuery("Select (a.basicBigShort + 1) From A a where (a.id = '9')")
					.getSingleResult();
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
			p = getEntityManager().createQuery("Select (a.basicBigShort - 1) From A a where (a.id = '9')")
					.getSingleResult();
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
			p = getEntityManager().createQuery("Select (a.basicBigShort * 1) From A a where (a.id = '9')")
					.getSingleResult();

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
			p = getEntityManager().createQuery("Select (a.basicShort + 1) From A a where (a.id = '9')")
					.getSingleResult();
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
			p = getEntityManager().createQuery("Select (a.basicShort - 1) From A a where (a.id = '9')")
					.getSingleResult();
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
			p = getEntityManager().createQuery("Select (a.basicShort * 1) From A a where (a.id = '9')")
					.getSingleResult();

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

}
