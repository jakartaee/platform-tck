/*
 * Copyright (c) 2011, 2023 Oracle and/or its affiliates. All rights reserved.
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

package ee.jakarta.tck.persistence.core.query.parameter;


import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import com.sun.ts.lib.harness.Status;
import com.sun.ts.lib.util.TestUtil;

import ee.jakarta.tck.persistence.common.PMClientBase;
import jakarta.persistence.Parameter;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;

public class Client1 extends PMClientBase {

	

	protected final Employee empRef[] = new Employee[5];

	public Client1() {
	}
	public static void main(String[] args) {
		Client1 theTests = new Client1();
		Status s = theTests.run(args, System.out, System.err);
		s.exit();
	}

	
	public void setup(String[] args, Properties p) throws Exception {
		logTrace( "setup");
		try {
			super.setup(args,p);
			getEntityManager();
		} catch (Exception e) {
			logErr( "Exception: ", e);
			throw new Exception("Setup failed:", e);
		}
	}

	/*
	 * @testName: parameterTest1
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:381; PERSISTENCE:JAVADOC:382;
	 * PERSISTENCE:JAVADOC:383; PERSISTENCE:JAVADOC:322; PERSISTENCE:JAVADOC:404;
	 * PERSISTENCE:JAVADOC:412; PERSISTENCE:SPEC:1634;
	 * 
	 * @test_Strategy: Create a query with 2 named parameters and retrieve
	 * information about the parameters.
	 *
	 */
	public void parameterTest1() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;
		boolean pass3 = false;
		boolean pass4 = false;
		boolean pass5 = false;
		boolean pass6 = false;
		boolean pass7 = false;
		boolean pass8 = false;

		Query query = getEntityManager()
				.createQuery("SELECT e FROM Employee e WHERE e.firstName = :first or e.lastName = :last");

		if (TestUtil.traceflag) {
			Set<Parameter<?>> sParameters = query.getParameters();
			for (Parameter p : sParameters) {
				logTrace( "Parameter name = " + p.getName());
				logTrace( "Parameter position = " + p.getPosition());
				logTrace( "Parameter type =" + p.getParameterType());
			}
		}

		String sExpected = "first";
		Parameter p1 = query.getParameter(sExpected);
		if (query.isBound(p1)) {
			logErr( "isBound believes there is a value bound to the parameter:" + sExpected);
		} else {
			pass1 = true;
		}

		String sActual = p1.getName();
		if (!sActual.equals(sExpected)) {
			logErr( "p1.getName() - Expected: " + sExpected + ", actual:" + sActual);
		} else {
			pass2 = true;
		}

		sExpected = null;
		Integer iActual = p1.getPosition();
		if (iActual != null) {
			logErr( "p1.getPosition() - Expected: " + sExpected + ", actual:" + iActual);
		} else {
			pass3 = true;
		}

		try {
			sExpected = "java.lang.String";
			sActual = p1.getParameterType().getName();
			if (!sActual.equals(sExpected)) {
				logErr(
						"p1.getParameterType() - Expected: " + sExpected + ", actual:" + sActual);
			} else {
				pass4 = true;
			}
		} catch (IllegalStateException ise) {
			logMsg(
					"warning: p1.getParameterType() threw IllegalStateException, this is not considered a failure");
		}

		sExpected = "last";
		Parameter p2 = query.getParameter(sExpected);
		if (query.isBound(p2)) {
			logErr( "isBound believes there is a value bound to the parameter:" + sExpected);
		} else {
			pass5 = true;
		}

		sActual = p2.getName();
		if (!sActual.equals(sExpected)) {
			logErr( "p2.getName() - Expected: " + sExpected + ", actual:" + sActual);
		} else {
			pass6 = true;
		}
		sExpected = null;
		iActual = p2.getPosition();
		if (iActual != null) {
			logErr( "p2.getPosition() - Expected: " + sExpected + ", actual:" + iActual);
		} else {
			pass7 = true;
		}

		try {
			sExpected = "java.lang.String";
			sActual = p2.getParameterType().getName();
			if (!sActual.equals(sExpected)) {
				logErr(
						"p2.getParameterType() - Expected: " + sExpected + ", actual:" + sActual);
			} else {
				pass8 = true;
			}
		} catch (IllegalStateException ise) {
			logMsg(
					"warning: p2.getParameterType() threw IllegalStateException, this is not considered a failure");
		}

		if (!pass1 || !pass2 || !pass3 || !pass4 || !pass5 || !pass6 || !pass7 || !pass8) {
			throw new Exception("parameterTest1 failed");
		}
	}

	/*
	 * @testName: parameterTestTQ1
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:642; PERSISTENCE:JAVADOC:681
	 * 
	 * @test_Strategy: Create a TypedQuery with 2 named parameters and retrieve
	 * information about the parameters.
	 *
	 */
	public void parameterTestTQ1() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;
		boolean pass3 = false;
		boolean pass4 = false;
		boolean pass5 = false;
		boolean pass6 = false;
		boolean pass7 = false;
		boolean pass8 = false;

		TypedQuery<Employee> query = getEntityManager().createQuery(
				"SELECT e FROM Employee e WHERE e.firstName = :first or e.lastName = :last", Employee.class);

		if (TestUtil.traceflag) {
			Set<Parameter<?>> sParameters = query.getParameters();
			for (Parameter p : sParameters) {/**/
				logTrace( "Parameter name = " + p.getName());
				logTrace( "Parameter position = " + p.getPosition());
				logTrace( "Parameter type =" + p.getParameterType());
			}
		}

		String sExpected = "first";
		Parameter p1 = query.getParameter(sExpected);
		if (query.isBound(p1)) {
			logErr( "isBound believes there is a value bound to the parameter:" + sExpected);
		} else {
			pass1 = true;
		}

		String sActual = p1.getName();
		if (!sActual.equals(sExpected)) {
			logErr( "p1.getName() - Expected: " + sExpected + ", actual:" + sActual);
		} else {
			pass2 = true;
		}

		sExpected = null;
		Integer iActual = p1.getPosition();
		if (iActual != null) {
			logErr( "p1.getPosition() - Expected: " + sExpected + ", actual:" + iActual);
		} else {
			pass3 = true;
		}

		try {
			sExpected = "java.lang.String";
			sActual = p1.getParameterType().getName();
			if (!sActual.equals(sExpected)) {
				logErr(
						"p1.getParameterType() - Expected: " + sExpected + ", actual:" + sActual);
			} else {
				pass4 = true;
			}
		} catch (IllegalStateException ise) {
			logMsg(
					"warning: p1.getParameterType() threw IllegalStateException, this is not considered a failure");
		}

		sExpected = "last";
		Parameter p2 = query.getParameter(sExpected);
		if (query.isBound(p2)) {
			logErr( "isBound believes there is a value bound to the parameter:" + sExpected);
		} else {
			pass5 = true;
		}

		sActual = p2.getName();
		if (!sActual.equals(sExpected)) {
			logErr( "p2.getName() - Expected: " + sExpected + ", actual:" + sActual);
		} else {
			pass6 = true;
		}
		sExpected = null;
		iActual = p2.getPosition();
		if (iActual != null) {
			logErr( "p2.getPosition() - Expected: " + sExpected + ", actual:" + iActual);
		} else {
			pass7 = true;
		}

		try {
			sExpected = "java.lang.String";
			sActual = p2.getParameterType().getName();
			if (!sActual.equals(sExpected)) {
				logErr(
						"p2.getParameterType() - Expected: " + sExpected + ", actual:" + sActual);
			} else {
				pass8 = true;
			}
		} catch (IllegalStateException ise) {
			logMsg(
					"warning: p2.getParameterType() threw IllegalStateException, this is not considered a failure");
		}

		if (!pass1 || !pass2 || !pass3 || !pass4 || !pass5 || !pass6 || !pass7 || !pass8) {
			throw new Exception("parameterTestTQ1 failed");
		}
	}

	/*
	 * @testName: parameterTest2
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:381; PERSISTENCE:JAVADOC:382;
	 * PERSISTENCE:JAVADOC:383; PERSISTENCE:JAVADOC:406; PERSISTENCE:JAVADOC:412;
	 * PERSISTENCE:SPEC:1634; PERSISTENCE:SPEC:1636;
	 * 
	 * @test_Strategy: Create a query with 2 positional parameters and retrieve
	 * information about the parameters.
	 */
	public void parameterTest2() throws Exception {
		int pass1 = 0;
		int pass2 = 0;
		int pass3 = 0;

		Query query = getEntityManager()
				.createQuery("SELECT e FROM Employee e WHERE e.firstName = ?1 or e.lastName = ?2");

		Set<Parameter<?>> sParameters = query.getParameters();
		if (sParameters.size() == 2) {
			for (Parameter p : sParameters) {
				if (TestUtil.traceflag) {
					logTrace( "parameter name = " + p.getName());
					logTrace( "parameter position = " + p.getPosition());
					logTrace( "parameter type =" + p.getParameterType());
				}
				if (query.isBound(p)) {
					logErr( "isBound believes there is a value bound to the parameter:" + p);
				} else {
					logTrace( "query isBound = " + query.isBound(p));
					pass1++;

				}
				Integer pos = p.getPosition();
				if (pos != null) {
					if (pos == 1 || pos == 2) {
						String sActual = p.getName();
						if (sActual != null) {
							logErr( "getName() - Expected: null, actual:" + sActual);
						} else {
							pass2++;
						}
						try {
							String sExpected = "java.lang.String";
							sActual = p.getParameterType().getName();
							if (!sActual.equals(sExpected)) {
								logErr( "getParameterType().getName() - Expected: " + sExpected
										+ ", actual:" + sActual);
							} else {
								pass3++;
							}
						} catch (IllegalStateException ise) {
							logMsg(
									"warning: getParameterType().getName() threw IllegalStateException, this is not considered a failure");
						}
					} else {
						logErr( "getPosition() returned an invalid position:" + pos);
					}
				} else {
					logErr( "getPosition() returned null");
				}

			}
		} else {
			logErr(
					"query.getParameters(): Expected: 2 parameters, actual: " + sParameters.size());
		}

		if (pass1 != 2 || pass2 != 2 || pass3 != 2) {
			throw new Exception("parameterTest2 failed");
		}
	}

	/*
	 * @testName: parameterTQTest2
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:648;
	 * 
	 * @test_Strategy: Create a TypedQuery with 2 positional parameters and retrieve
	 * information about the parameters.
	 */
	public void parameterTQTest2() throws Exception {
		int pass1 = 0;
		int pass2 = 0;
		int pass3 = 0;

		TypedQuery<Employee> query = getEntityManager()
				.createQuery("SELECT e FROM Employee e WHERE e.firstName = ?1 or e.lastName = ?2", Employee.class);
		Set<Parameter<?>> sParameters = query.getParameters();
		if (sParameters.size() == 2) {
			for (Parameter p : sParameters) {
				if (TestUtil.traceflag) {
					logTrace( "parameter name = " + p.getName());
					logTrace( "parameter position = " + p.getPosition());
					logTrace( "parameter type =" + p.getParameterType());
				}
				if (query.isBound(p)) {
					logErr( "isBound believes there is a value bound to the parameter:" + p);
				} else {
					logTrace( "query isBound = " + query.isBound(p));
					pass1++;
				}
				Integer pos = p.getPosition();
				if (pos != null) {
					if (pos == 1 || pos == 2) {
						String sActual = p.getName();
						if (sActual != null) {
							logErr( "getName() - Expected: null, actual:" + sActual);
						} else {
							pass2++;
						}
						try {
							String sExpected = "java.lang.String";
							sActual = p.getParameterType().getName();
							if (!sActual.equals(sExpected)) {
								logErr( "getParameterType().getName() - Expected: " + sExpected
										+ ", actual:" + sActual);
							} else {
								pass3++;
							}
						} catch (IllegalStateException ise) {
							logMsg(
									"warning: getParameterType().getName() threw IllegalStateException, this is not considered a failure");
						}
					} else {
						logErr( "getPosition() returned an invalid position:" + pos);
					}
				} else {
					logErr( "getPosition() returned null");
				}

			}
		} else {
			logErr(
					"query.getParameters(): Expected: 2 parameters, actual: " + sParameters.size());
		}
		if (pass1 != 2 || pass2 != 2 || pass3 != 2) {
			throw new Exception("parameterTQTest2 failed");
		}
	}

	/*
	 * @testName: parameterTest4
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:381; PERSISTENCE:JAVADOC:383;
	 * 
	 * @test_Strategy: Create a query with a named parameter that is a date and and
	 * retrieve information about the parameter.
	 */
	public void parameterTest4() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;
		boolean pass3 = false;

		Query query = getEntityManager().createQuery("SELECT e FROM Employee e WHERE e.hireDate = :hDate ");

		if (TestUtil.traceflag) {
			Set<Parameter<?>> sParameters = query.getParameters();
			for (Parameter p : sParameters) {
				logTrace( "Parameter name = " + p.getName());
				logTrace( "Parameter position = " + p.getPosition());
			}
		}

		String sExpected = "hDate";
		Parameter p1 = query.getParameter(sExpected);
		String sActual = p1.getName();
		if (!sActual.equals(sExpected)) {
			logErr( "p1.getName() - Expected: " + sExpected + ", actual:" + sActual);
		} else {
			pass1 = true;
		}

		sExpected = null;
		Integer iActual = p1.getPosition();
		if (iActual != null) {
			logErr( "p1.getPosition() - Expected: " + sExpected + ", actual:" + iActual);
		} else {
			pass2 = true;
		}

		if (!pass1 || !pass2) {
			throw new Exception("parameterTest4 failed");
		}
	}

	/*
	 * @testName: parameterTest5
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:381; PERSISTENCE:JAVADOC:383;
	 * 
	 * @test_Strategy: Create a query with a named parameter that is a float and
	 * retrieve information about the parameter.
	 *
	 */
	
	public void parameterTest5() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;
		boolean pass3 = false;

		Query query = getEntityManager().createQuery("SELECT e FROM Employee e WHERE e.salary = :salary ");

		if (TestUtil.traceflag) {
			Set<Parameter<?>> sParameters = query.getParameters();
			for (Parameter p : sParameters) {
				logTrace( "Parameter name = " + p.getName());
				logTrace( "Parameter position = " + p.getPosition());
			}
		}

		String sExpected = "salary";
		Parameter p1 = query.getParameter(sExpected);
		String sActual = p1.getName();
		if (!sActual.equals(sExpected)) {
			logErr( "p1.getName() - Expected: " + sExpected + ", actual:" + sActual);
		} else {
			pass1 = true;
		}

		sExpected = null;
		Integer iActual = p1.getPosition();
		if (iActual != null) {
			logErr( "p1.getPosition() - Expected: " + sExpected + ", actual:" + iActual);
		} else {
			pass2 = true;
		}

		if (!pass1 || !pass2) {
			throw new Exception("parameterTest5 failed");
		}
	}

	/*
	 * @testName: getParametersTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:408;
	 * 
	 * @test_Strategy: Create a query with 2 named parameters and retrieve all
	 * parameters.
	 *
	 */
	
	public void getParametersTest() throws Exception {
		boolean pass = false;
		logTrace( "Starting getParametersTest");

		try {
			Query query = getEntityManager()
					.createQuery("SELECT e FROM Employee e WHERE e.firstName = :first or e.lastName = :last");

			if (TestUtil.traceflag) {
				Set<Parameter<?>> sParameters = query.getParameters();
				for (Parameter p : sParameters) {
					logTrace( "Parameter name = " + p.getName());
					logTrace( "Parameter position = " + p.getPosition());
					logTrace( "Parameter type =" + p.getParameterType());
				}
			}

			boolean foundFirstName = false;
			boolean foundLastName = false;
			int found = 0;
			List<Object> list = new ArrayList<Object>(query.getParameters());
			for (int i = 0; i < list.size(); i++) {
				Parameter p = (Parameter) list.get(i);
				logTrace( "Parameter name = " + p.getName());
				if (p.getName().equals("first")) {
					foundFirstName = true;
					found++;
				}
				if (p.getName().equals("last")) {
					foundLastName = true;
					found++;
				}
			}
			if (found == 2 && foundFirstName && foundLastName) {
				pass = true;
			} else {
				if (found != 2) {
					logErr( "Wrong number of parameters returned, expected:2, actual:" + found);
				}
				if (!foundFirstName) {
					logErr( "Parameter 'first' was not returned");
				}
				if (!foundLastName) {
					logErr( "Parameter 'last' was not returned");
				}
			}

		} catch (Exception e) {
			logErr( "Unexpected exception occurred", e);
		}
		if (!pass) {
			throw new Exception("getParametersTest failed");
		}
	}

	/*
	 * @testName: getParametersTQTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:654
	 * 
	 * @test_Strategy: Create a typedQuery with 2 named parameters and retrieve all
	 * parameters.
	 *
	 */
	
	public void getParametersTQTest() throws Exception {
		boolean pass = false;
		logTrace( "Starting getParametersTest");

		try {
			TypedQuery<Employee> query = getEntityManager().createQuery(
					"SELECT e FROM Employee e WHERE e.firstName = :first or e.lastName = :last", Employee.class);

			if (TestUtil.traceflag) {
				Set<Parameter<?>> sParameters = query.getParameters();
				for (Parameter p : sParameters) {
					logTrace( "Parameter name = " + p.getName());
					logTrace( "Parameter position = " + p.getPosition());
					logTrace( "Parameter type =" + p.getParameterType());
				}
			}

			boolean foundFirstName = false;
			boolean foundLastName = false;
			int found = 0;
			List<Object> list = new ArrayList<Object>(query.getParameters());
			for (int i = 0; i < list.size(); i++) {
				Parameter p = (Parameter) list.get(i);
				logTrace( "Parameter name = " + p.getName());
				if (p.getName().equals("first")) {
					foundFirstName = true;
					found++;
				}
				if (p.getName().equals("last")) {
					foundLastName = true;
					found++;
				}
			}
			if (found == 2 && foundFirstName && foundLastName) {
				pass = true;
			} else {
				if (found != 2) {
					logErr( "Wrong number of parameters returned, expected:2, actual:" + found);
				}
				if (!foundFirstName) {
					logErr( "Parameter 'first' was not returned");
				}
				if (!foundLastName) {
					logErr( "Parameter 'last' was not returned");
				}
			}

		} catch (Exception e) {
			logErr( "Unexpected exception occurred", e);
		}
		if (!pass) {
			throw new Exception("getParametersTQTest failed");
		}
	}

	/*
	 * @testName: getParametersNoParametersTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:408;
	 * 
	 * @test_Strategy: Create a query with no parameters
	 */
	
	public void getParametersNoParametersTest() throws Exception {
		boolean pass = false;
		try {
			logTrace( "Starting getParametersTest");
			Query query = getEntityManager().createQuery("SELECT e FROM Employee e");

			if (TestUtil.traceflag) {
				Set<Parameter<?>> sParameters = query.getParameters();
				for (Parameter p : sParameters) {
					logTrace( "parameter name = " + p.getName());
					logTrace( "parameter position = " + p.getPosition());
					logTrace( "pParameter type =" + p.getParameterType());
				}
			}

			if (query.getParameters().size() != 0) {
				List<Object> list = new ArrayList<Object>(query.getParameters());
				for (int i = 0; i < list.size(); i++) {
					Parameter p = (Parameter) list.get(i);
					logErr( "parameter name = " + p.getName());
				}
			} else {
				pass = true;
			}
		} catch (Exception e) {
			logErr( "Unexpected exception occurred", e);
		}

		if (!pass) {
			throw new Exception("getParametersNoParametersTest failed");
		}

	}

	public void cleanup() throws Exception {
		try {
			logTrace( "calling super.cleanup");
			super.cleanup();
		} finally {

        }
	}

}
