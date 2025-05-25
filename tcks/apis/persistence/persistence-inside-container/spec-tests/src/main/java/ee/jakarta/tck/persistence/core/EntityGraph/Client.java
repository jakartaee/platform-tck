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

package ee.jakarta.tck.persistence.core.EntityGraph;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import com.sun.ts.lib.harness.Fault;
import com.sun.ts.lib.harness.Status;

import ee.jakarta.tck.persistence.common.PMClientBase;
import jakarta.persistence.AttributeNode;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.metamodel.Attribute;
import jakarta.persistence.metamodel.ManagedType;
import jakarta.persistence.metamodel.Metamodel;

public class Client extends PMClientBase {



	Employee3[] empRef = new Employee3[5];

	Department[] deptRef = new Department[2];

	public Client() {
	}

	public static void main(String[] args) {
		Client theTests = new Client();

		Status s = theTests.run(args, System.out, System.err);
		s.exit();
	}

	public void setup(String[] args, Properties p) throws Fault {
		logTrace("setup");
		try {
			super.setup(args, p);
			removeTestData();
			createEmployeeData();
			displayMap(new Properties());
		} catch (Exception e) {
			logErr("Exception: ", e);
			throw new Fault("Setup failed:", e);
		}
	}

	public void setupEmployeeData(String[] args, Properties p) throws Fault {
		logTrace( "setupOrderData");
		try {
			super.setup(args,p);
			removeTestData();
			createEmployeeData();
			displayMap(new Properties());
		} catch (Exception e) {
			logErr( "Exception: ", e);
			throw new Fault("Setup failed:", e);
		}
	}

	public void cleanup() throws Exception {
		logTrace( "Cleanup data");
		removeTestData();
		super.cleanup();
	}

	public void cleanupEmployeeData() throws Fault {
		logTrace("Cleanup data");
		removeTestData();
		try {
			super.cleanup();
		} catch (Exception e) {
			throw new Fault(e);
		}
	}


	/*
	 * @testName: addAttributeNodesStringArrayTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:3360; PERSISTENCE:JAVADOC:3362;
	 * PERSISTENCE:JAVADOC:3398; PERSISTENCE:JAVADOC:3402;
	 * 
	 * @test_Strategy: Create attribute Nodes and retrieve them
	 */
	public void addAttributeNodesStringArrayTest() throws Exception {
		boolean pass = false;
		List<String> expected = new ArrayList<String>();
		expected.add("firstName");
		expected.add("lastName");

		EntityGraph<Employee> empEG = getEntityManager().createEntityGraph(Employee.class);
		empEG.addAttributeNodes(expected.toArray(new String[] {}));

		List<AttributeNode<?>> result = empEG.getAttributeNodes();
		List<String> actual = new ArrayList<String>();
		for (AttributeNode an : result) {
			actual.add(an.getAttributeName());
		}

		if (actual.containsAll(expected) && expected.containsAll(actual) && actual.size() == expected.size()) {
			logTrace( "Received expected results");
			pass = true;
		} else {
			logErr( "Expected results");
			for (String s : expected) {
				logErr( "expected:" + s);
			}
			logErr( "Actual results");
			for (String s : actual) {
				logErr( "actual:" + s);
			}
		}

		if (!pass) {
			throw new Fault("addAttributeNodesStringArrayTest failed");
		}
	}

	/*
	 * @testName: addAttributeNodesStringArrayIllegalArgumentExceptionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:3363;
	 * 
	 * @test_Strategy: Create attribute Node that does not exist
	 */
	public void addAttributeNodesStringArrayIllegalArgumentExceptionTest() throws Exception {
		boolean pass = false;

		EntityGraph<Employee2> empEG = getEntityManager().createEntityGraph(Employee2.class);
		try {
			empEG.addAttributeNodes("doesnotexist");
			logErr( "Did not throw IllegalArgumentException");
		} catch (IllegalArgumentException iae) {
			pass = true;
			logTrace( "Received expected IllegalArgumentException");
		}
		if (!pass) {
			throw new Fault("addAttributeNodesStringArrayIllegalArgumentExceptionTest failed");
		}
	}

	/*
	 * @testName: addAttributeNodesAttributeArrayTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:3365; PERSISTENCE:JAVADOC:3398;
	 * 
	 * @test_Strategy: Create attribute Nodes via Attribute[] and retrieve them
	 */
	public void addAttributeNodesAttributeArrayTest() throws Exception {
		boolean pass = false;

		Metamodel mm = getEntityManager().getMetamodel();

		ManagedType<Employee> mTypeOrder = mm.managedType(Employee.class);

		List<String> sExpected = new ArrayList<String>();
		sExpected.add("firstName");
		sExpected.add("lastName");

		List<Attribute> expected = new ArrayList<Attribute>();
		for (String s : sExpected) {
			expected.add(mTypeOrder.getDeclaredAttribute(s));
		}

		EntityGraph<Employee> empEG = getEntityManager().createEntityGraph(Employee.class);
		empEG.addAttributeNodes(expected.toArray(new Attribute[] {}));

		List<AttributeNode<?>> result = empEG.getAttributeNodes();
		List<String> actual = new ArrayList<String>();
		for (AttributeNode an : result) {
			actual.add(an.getAttributeName());
		}

		if (actual.containsAll(sExpected) && sExpected.containsAll(actual) && actual.size() == sExpected.size()) {
			logTrace( "Received expected results");
			pass = true;
		} else {
			logErr( "Expected results");
			for (String s : sExpected) {
				logErr( "expected:" + s);
			}
			logErr( "Actual results");
			for (String s : actual) {
				logErr( "actual:" + s);
			}
		}

		if (!pass) {
			throw new Fault("addAttributeNodesAttributeArrayTest failed");
		}
	}

	/*
	 * @testName: createEntityGraphStringTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:3403
	 * 
	 * @test_Strategy: Use createEntityGraph to create the named entity graphs in
	 * the Employee entity
	 */
	public void createEntityGraphStringTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;

		logMsg( "Test that the named entity graph is returned");
		EntityGraph eg = getEntityManager().createEntityGraph("first_last_graph");
		if (eg != null) {
			logTrace( "Received non-null EntityGraph:" + eg.getName());
			pass1 = true;
		} else {
			logErr( "Null was returned for EntityGraph that does exist");
		}

		logMsg( "Test that null is returned");
		try {
			eg = getEntityManager().createEntityGraph("doesnotexist");

			if (eg == null) {
				logTrace( "Received expected null EntityGraph");
				pass2 = true;
			} else {
				logErr( "Non-null EntityGraph was returned instead of a null:" + eg.getName());
			}
		} catch (Exception ex) {
			logErr( "Received unexpected exception", ex);
		}

		if (!pass1 || !pass2) {
			throw new Fault("createEntityGraphStringTest failed");
		}
	}

	/*
	 * @testName: getEntityGraphStringTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:3404
	 * 
	 * @test_Strategy: Use getEntityGraph to get the named entity graphs in the
	 * Employee entity
	 */
	public void getEntityGraphStringTest() throws Exception {
		boolean pass = false;

		EntityGraph eg = getEntityManager().getEntityGraph("first_last_graph");
		if (eg != null) {
			logTrace( "Received non-null EntityGraph:" + eg.getName());
			pass = true;
		} else {
			logErr( "Null was returned for EntityGraph that does exist");
		}

		if (!pass) {
			throw new Fault("getEntityGraphStringTest failed");
		}
	}

	/*
	 * @testName: entityGraphGetNameTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:3399
	 * 
	 * @test_Strategy: Use getName to get the name of the named entity graph in the
	 * Employee entity
	 */
	public void entityGraphGetNameTest() throws Exception {
		boolean pass = false;

		String expected = "first_last_graph";
		EntityGraph eg = getEntityManager().getEntityGraph(expected);
		if (eg != null) {
			if (eg.getName().equals(expected)) {
				logTrace( "Received expected EntityGraph name:" + eg.getName());
				pass = true;
			} else {
				logErr( "Expected name: " + expected + ", actual:" + eg.getName());
			}
		} else {
			logErr( "Null was returned for EntityGraph that does exist");
		}

		if (!pass) {
			throw new Fault("entityGraphGetNameTest failed");
		}
	}

	/*
	 * @testName: entityGraphGetNameNoNameExistsTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:2458;
	 * 
	 * @test_Strategy: Use getName to get the name of the named entity graph in the
	 * Employee2 entity that has no name
	 */
	public void entityGraphGetNameNoNameExistsTest() throws Exception {
		boolean pass = false;

		List<EntityGraph<? super Employee2>> egs = getEntityManager().getEntityGraphs(Employee2.class);
		if (egs.size() == 1) {
			EntityGraph e = egs.get(0);
			if (e.getName().equals("Employee2")) {
				logTrace( "Received expected name:" + e.getName());
				pass = true;
			} else {
				logErr( "Expected name: Employee2, actual:" + e.getName());
			}
		} else {
			logErr( "Expected 1 graph to be returned, instead got:" + egs.size());
		}

		if (!pass) {
			throw new Exception("entityGraphGetNameNoNameExistsTest failed");
		}
	}

	/*
	 * @testName: getNameTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:3399
	 * 
	 * @test_Strategy: Use getName to get the name of the entity graph
	 */
	public void getNameTest() throws Exception {
		boolean pass = false;

		EntityGraph<Employee2> eg = getEntityManager().createEntityGraph(Employee2.class);

		if (eg.getName() == null) {
			logTrace( "Received expected null");
			pass = true;
		} else {
			logErr( "Expected name: null, actual:" + eg.getName());
		}

		if (!pass) {
			throw new Exception("getNameTest failed");
		}
	}

	/*
	 * @testName: getEntityGraphStringIllegalArgumentExceptionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:3405;
	 * 
	 * @test_Strategy: get entity graph that does not exist
	 */
	public void getEntityGraphStringIllegalArgumentExceptionTest() throws Exception {
		boolean pass = false;

		try {
			getEntityManager().getEntityGraph("doesnotexist");
			logErr( "Did not throw IllegalArgumentException");
		} catch (IllegalArgumentException iae) {
			pass = true;
			logTrace( "Received expected IllegalArgumentException");
		}
		if (!pass) {
			throw new Exception("getEntityGraphStringIllegalArgumentExceptionTest failed");
		}
	}

	/*
	 * @testName: getEntityGraphsClassTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:3406; PERSISTENCE:JAVADOC:3420;
	 * 
	 * @test_Strategy: Use getEntityGraph to get the named entity graphs in the
	 * Employee entity
	 */
	
	public void getEntityGraphsClassTest() throws Exception {
		boolean pass = false;
		List<String> expected = new ArrayList<String>();
		expected.add("first_last_graph");
		expected.add("last_salary_graph");
		expected.add("lastname_department_subgraphs");

		List<String> actual = new ArrayList<String>();

		List<EntityGraph<? super Employee3>> eg = getEntityManager().getEntityGraphs(Employee3.class);
		if (eg.size() > 0) {
			for (EntityGraph e : eg) {
				actual.add(e.getName());
			}
			if (actual.containsAll(expected) && expected.containsAll(actual) && actual.size() == expected.size()) {
				logTrace( "Received expected results");
				pass = true;
			} else {
				logErr( "Expected results");
				for (String s : expected) {
					logErr( "expected:" + s);
				}
				logErr( "Actual results");
				for (String s : actual) {
					logErr( "actual:" + s);
				}
			}
		} else {
			logErr( "No named entity graphs were returned eventhough they exist in entity");
		}

		if (!pass) {
			throw new Exception("getEntityGraphsClassTest failed");
		}
	}

	/*
	 * @testName: addNamedEntityGraphStringEntityGraphTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:3411;
	 * 
	 * @test_Strategy: add an entity graph as a named entitygraph
	 */
	
	public void addNamedEntityGraphStringEntityGraphTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;
		boolean pass3 = false;
		boolean pass4 = false;

		String sExpected = "new_named_entity_graph";

		logMsg( "add entity graph to EMF");
		EntityGraph eg = getEntityManager().createEntityGraph(Employee.class);
		eg.addAttributeNodes("id");
		getEntityManager().getEntityManagerFactory().addNamedEntityGraph(sExpected, eg);
		EntityGraph eg2 = getEntityManager().getEntityGraph(sExpected);
		if (eg2 != null) {
			if (eg2.getName() != null) {
				if (eg2.getName().equals(sExpected)) {
					logTrace( "Received expected entity graph:" + eg2.getName());
					pass1 = true;
				} else {
					logErr( "Expected: named_entity_graph, actual:" + eg2.getName());
				}
			} else {
				logErr(
						"getName() returned null for a named entity graph added via addNamedEntityGraph");
			}
		} else {
			logErr( "getEntityGraph() returned null");
		}
		logMsg( "verify nodes");
		List<String> expected = new ArrayList<String>();
		expected.add("id");
		List<String> actual = new ArrayList<String>();

		List<AttributeNode<?>> listNodes = eg2.getAttributeNodes();
		for (AttributeNode an : listNodes) {
			actual.add(an.getAttributeName());
		}
		if (expected.containsAll(actual) && actual.containsAll(expected) && expected.size() == actual.size()) {
			logTrace( "Received expected AttributeNode");
			pass2 = true;
		} else {
			logErr( "Did not received expected AttributeNodes");
			logErr( "Expected results");
			for (String s : expected) {
				logErr( "expected:" + s);
			}
			logErr( "Actual results");
			for (String s : actual) {
				logErr( "actual:" + s);
			}
		}

		logMsg( "override previous entity graph");
		eg = getEntityManager().createEntityGraph(Employee.class);
		eg.addAttributeNodes("lastName");
		getEntityManager().getEntityManagerFactory().addNamedEntityGraph(sExpected, eg);
		eg2 = getEntityManager().getEntityGraph(sExpected);
		if (eg2 != null) {
			if (eg2.getName() != null) {
				if (eg2.getName().equals(sExpected)) {
					logTrace( "Received expected entity graph:" + eg2.getName());
					pass3 = true;
				} else {
					logErr( "Expected: named_entity_graph, actual:" + eg2.getName());
				}
			} else {
				logErr(
						"getName() returned null for a named entity graph added via addNamedEntityGraph");
			}
		} else {
			logErr( "getEntityGraph() returned null");
		}
		logMsg( "verify nodes of overriden named entity graph");
		expected = new ArrayList<String>();
		expected.add("lastName");
		actual = new ArrayList<String>();

		listNodes = eg2.getAttributeNodes();
		for (AttributeNode an : listNodes) {
			actual.add(an.getAttributeName());
		}
		if (expected.containsAll(actual) && actual.containsAll(expected) && expected.size() == actual.size()) {
			logTrace( "Received expected AttributeNode");
			pass4 = true;
		} else {
			logErr( "Did not received expected AttributeNodes");
			logErr( "Expected results");
			for (String s : expected) {
				logErr( "expected:" + s);
			}
			logErr( "Actual results");
			for (String s : actual) {
				logErr( "actual:" + s);
			}
		}

		if (!pass1 || !pass2 || !pass3 || !pass4) {
			throw new Exception("addNamedEntityGraphStringEntityGraphTest failed");
		}
	}

	/*
	 * @testName: getEntityGraphsClassIllegalArgumentExceptionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:3407;
	 * 
	 * @test_Strategy: get entity graphs from a non entity class
	 */
	
	public void getEntityGraphsClassIllegalArgumentExceptionTest() throws Exception {
		boolean pass = false;

		try {
			getEntityManager().getEntityGraphs(Client.class);
			logErr( "Did not throw IllegalArgumentException");
		} catch (IllegalArgumentException iae) {
			pass = true;
			logTrace( "Received expected IllegalArgumentException");
		}
		if (!pass) {
			throw new Exception("getEntityGraphsClassIllegalArgumentExceptionTest failed");
		}
	}

	/*
	 * @testName: annotationsTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:3414; PERSISTENCE:JAVADOC:3415;
	 * PERSISTENCE:JAVADOC:3417;
	 * 
	 * @test_Strategy: test that various data contained in the annotations is
	 * returnable
	 */
	
	public void annotationsTest() throws Exception {
		boolean pass = false;
		List<String> expected = new ArrayList<String>();
		expected.add("lastName");
		expected.add("salary");

		List<String> actual = new ArrayList<String>();

		EntityGraph eg = getEntityManager().getEntityGraph("last_salary_graph");
		List<AttributeNode<?>> listNodes = eg.getAttributeNodes();

		for (AttributeNode an : listNodes) {
			actual.add(an.getAttributeName());
		}

		if (actual.containsAll(expected) && expected.containsAll(actual) && actual.size() == expected.size()) {
			logTrace( "Received expected results");
			pass = true;
		} else {
			logErr( "Expected results");
			for (String s : expected) {
				logErr( "expected:" + s);
			}
			logErr( "Actual results");
			for (String s : actual) {
				logErr( "actual:" + s);
			}
		}

		if (!pass) {
			throw new Exception("annotationsTest failed");
		}
	}

	private void createEmployeeData() {

		try {
			getEntityTransaction().begin();
			logMsg( "Creating Employees");

			final Date d1 = getUtilDate("2000-02-14");
			final Date d2 = getUtilDate("2001-06-27");
			final Date d3 = getUtilDate("2002-07-07");
			final Date d4 = getUtilDate("2003-03-03");
			final Date d5 = getUtilDate();

			deptRef[0] = new Department(1, "Marketing");
			deptRef[1] = new Department(2, "Administration");
			for (Department d : deptRef) {
				getEntityManager().persist(d);
				logTrace( "persisted department:" + d);
			}

			empRef[0] = new Employee3(1, "Alan", "Frechette", d1, (float) 35000.0);
			empRef[0].setDepartment(deptRef[0]);
			empRef[1] = new Employee3(2, "Arthur", "Frechette", d2, (float) 35000.0);
			empRef[1].setDepartment(deptRef[0]);
			empRef[2] = new Employee3(3, "Shelly", "McGowan", d3, (float) 50000.0);
			empRef[2].setDepartment(deptRef[1]);
			empRef[3] = new Employee3(4, "Robert", "Bissett", d4, (float) 55000.0);
			empRef[3].setDepartment(deptRef[1]);
			empRef[4] = new Employee3(5, "Stephen", "DMilla", d5, (float) 25000.0);
			empRef[4].setDepartment(deptRef[1]);

			for (Employee3 e : empRef) {
				if (e != null) {
					getEntityManager().persist(e);
					logTrace( "persisted employee3:" + e);
				}
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logErr( "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception fe) {
				logErr( "Unexpected exception rolling back TX:", fe);
			}
		}
	}

	private void removeTestData() {
		logTrace( "removeTestData");
		if (getEntityTransaction().isActive()) {
			getEntityTransaction().rollback();
		}
		try {
			getEntityTransaction().begin();
			getEntityManager().createNativeQuery("DELETE FROM EMPLOYEE").executeUpdate();
			getEntityManager().createNativeQuery("DELETE FROM DEPARTMENT").executeUpdate();
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
