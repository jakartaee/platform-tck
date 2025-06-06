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

package ee.jakarta.tck.persistence.core.StoredProcedureQuery;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import com.sun.ts.lib.harness.Fault;
import com.sun.ts.lib.harness.Status;




import jakarta.persistence.FlushModeType;
import jakarta.persistence.LockModeType;
import jakarta.persistence.NoResultException;
import jakarta.persistence.NonUniqueResultException;
import jakarta.persistence.Parameter;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.Query;
import jakarta.persistence.StoredProcedureQuery;
import jakarta.persistence.TemporalType;
import jakarta.persistence.TransactionRequiredException;

public class Client1 extends Client {



    public Client1() {
    }

    /*
     * setup() is called before each test
     *
     * @class.setup_props: jdbc.db;
     */
    
    public void setup(String[] args, Properties p) throws Exception {
        logTrace( "setup");

        super.setup(args,p);
        //removeTestData();
        //createEmployeeTestData();
        dataBaseName = p.getProperty("jdbc.db");

    }
    public static void main(String[] args) {
   		Client1 theTests = new Client1();
   		Status s = theTests.run(args, System.out, System.err);
   		s.exit();
   	}

    /*
     * @testName: executeTest
     *
     * @assertion_ids: PERSISTENCE:JAVADOC:1557; PERSISTENCE:SPEC:1515;
     * PERSISTENCE:SPEC:1566; PERSISTENCE:SPEC:1568; PERSISTENCE:SPEC:1565;
     * PERSISTENCE:SPEC:1572; PERSISTENCE:SPEC:1576; PERSISTENCE:SPEC:1577;
     * PERSISTENCE:SPEC:1578; PERSISTENCE:SPEC:1580;
     *
     * @test_Strategy:
     *
     */
        public void executeTest() throws Exception {
        boolean pass1 = false;
        boolean pass2 = false;
        boolean pass3 = false;

        try {
            getEntityTransaction().begin();
            try {
                logMsg( "Testing using name,class");
                clearCache();
                StoredProcedureQuery spq = getEntityManager().createStoredProcedureQuery("GetEmpASCFromRS",
                        Employee.class);
                if (dataBaseName.equalsIgnoreCase(ORACLE) || dataBaseName.equalsIgnoreCase(POSTGRESQL)) {
                    logTrace( "register refcursor parameter");
                    spq.registerStoredProcedureParameter(1, void.class, ParameterMode.REF_CURSOR);
                }
                logTrace( "executing:" + spq.toString());
                if (spq.execute()) {

                    List<List> listOfList = getResultSetsFromStoredProcedure(spq);
                    if (listOfList.size() == 1) {
                        List<Integer> expected = new ArrayList<Integer>();
                        for (Employee e : empRef) {
                            expected.add(e.getId());
                        }
                        pass1 = verifyEmployeeIds(expected, listOfList);
                    } else {
                        logErr(
                                "Did not get the correct number of result sets returned, expected: 1, actual:"
                                        + listOfList.size());
                    }
                } else {
                    logErr( "Expected execute() to return true, actual: false");
                }
            } catch (Exception ex) {
                logErr( "Received unexpected exception:", ex);
            }
            try {
                logMsg( "Testing using name,result mapping");
                clearCache();
                StoredProcedureQuery spq = getEntityManager().createStoredProcedureQuery("GetEmpIdFNameLNameFromRS",
                        "id-firstname-lastname");
                spq.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
                if (dataBaseName.equalsIgnoreCase(ORACLE) || dataBaseName.equalsIgnoreCase(POSTGRESQL)) {
                    logTrace( "register refcursor parameter");
                    spq.registerStoredProcedureParameter(2, void.class, ParameterMode.REF_CURSOR);
                }
                spq.setParameter(1, 1);
                if (spq.execute()) {

                    List<List> listOfList = getResultSetsFromStoredProcedure(spq);
                    if (listOfList.size() == 1) {
                        List<Employee> expected = new ArrayList<Employee>();
                        expected.add(new Employee(emp0.getId(), emp0.getFirstName(), emp0.getLastName()));
                        pass2 = verifyListOfListEmployees(expected, listOfList);
                    } else {
                        logErr(
                                "Did not get the correct number of result sets returned, expected: 1, actual:"
                                        + listOfList.size());
                    }
                } else {
                    logErr( "Expected execute() to return false, actual: true");
                }
            } catch (Exception ex) {
                logErr( "Received unexpected exception:", ex);
            }
            try {
                logMsg( "Testing using named stored procedure");
                clearCache();
                StoredProcedureQuery spq;
                if (dataBaseName.equalsIgnoreCase(ORACLE) || dataBaseName.equalsIgnoreCase(POSTGRESQL)) {
                    logTrace( "Calling refcursor specific named stored procedure query");
                    spq = getEntityManager().createNamedStoredProcedureQuery("get-id-firstname-lastname-refcursor");
                } else {
                    spq = getEntityManager().createNamedStoredProcedureQuery("get-id-firstname-lastname");
                }
                spq.setParameter(1, 1);
                if (spq.execute()) {
                    List<List> listOfList = getResultSetsFromStoredProcedure(spq);
                    if (listOfList.size() == 1) {
                        List<Employee> expected = new ArrayList<Employee>();
                        expected.add(new Employee(emp0.getId(), emp0.getFirstName(), emp0.getLastName()));
                        pass3 = verifyListOfListEmployees(expected, listOfList);
                    } else {
                        logErr(
                                "Did not get the correct number of result sets returned, expected: 1, actual:"
                                        + listOfList.size());
                    }
                } else {
                    logErr( "Expected execute() to return false, actual: true");
                }
            } catch (Exception ex) {
                logErr( "Received unexpected exception:", ex);
            }

            getEntityTransaction().commit();

        } catch (Exception ex) {
            logErr( "Received unexpected exception:", ex);
        }

        if (!pass1 || !pass2 || !pass3) {
            throw new Exception("executeTest failed");
        }

    }

    /*
     * @testName: getOutputParameterValueIntTest
     *
     * @assertion_ids: PERSISTENCE:JAVADOC:1547; PERSISTENCE:SPEC:1569;
     * PERSISTENCE:SPEC:1570; PERSISTENCE:SPEC:1583;
     *
     * @test_Strategy:
     *
     */
        public void getOutputParameterValueIntTest() throws Exception {
        boolean pass2 = false;
        boolean pass4 = false;
        boolean pass6 = false;

        Object oActual = null;
        try {
            getEntityTransaction().begin();

            logMsg( "Get the value from an OUT only parameter");
            StoredProcedureQuery spq3 = getEntityManager().createStoredProcedureQuery("GetEmpOneFirstNameFromOut");
            spq3.registerStoredProcedureParameter(1, String.class, ParameterMode.OUT);
            if (!spq3.execute()) {

                oActual = spq3.getOutputParameterValue(1);
                if (oActual instanceof String) {
                    String actual = (String) oActual;
                    if (actual.equals(emp0.getFirstName())) {
                        logTrace( "Received expected result:" + actual);
                        pass6 = true;
                    } else {
                        logErr(
                                "Expected result: " + emp0.getFirstName() + ", actual:" + actual);
                    }
                } else {
                    logErr( "Did not get instance of String, instead:" + oActual);
                }
            } else {
                logErr( "Expected execute() to return false, actual: true");
            }
        } catch (Exception ex) {
            logErr( "Received unexpected exception:", ex);
        }
        try {
            logMsg( "Get the value from an IN and OUT parameter");
            StoredProcedureQuery spq1 = getEntityManager().createStoredProcedureQuery("GetEmpFirstNameFromOut");
            spq1.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
            spq1.registerStoredProcedureParameter(2, String.class, ParameterMode.OUT);
            spq1.setParameter(1, 1);
            if (!spq1.execute()) {

                oActual = spq1.getOutputParameterValue(2);
                if (oActual instanceof String) {
                    String actual = (String) oActual;
                    if (actual.equals(emp0.getFirstName())) {
                        logTrace( "Received expected result:" + actual);
                        pass2 = true;
                    } else {
                        logErr(
                                "Expected result: " + emp0.getFirstName() + ", actual:" + actual);
                    }
                } else {
                    logErr( "Did not get instance of String, instead:" + oActual);
                }
            } else {
                logErr( "Expected execute() to return false, actual: true");
            }
        } catch (Exception ex) {
            logErr( "Received unexpected exception:", ex);
        }
        try {
            logMsg( "Get the value from an INOUT parameter");
            StoredProcedureQuery spq2 = getEntityManager().createStoredProcedureQuery("GetEmpLastNameFromInOut");
            spq2.registerStoredProcedureParameter(1, String.class, ParameterMode.INOUT);
            spq2.setParameter(1, "1");

            if (!spq2.execute()) {

                oActual = spq2.getOutputParameterValue(1);
                if (oActual instanceof String) {
                    String actual = (String) oActual;
                    if (actual.equals(emp0.getLastName())) {
                        logTrace( "Received expected result:" + actual);
                        pass4 = true;
                    } else {
                        logErr( "Expected result: " + emp0.getLastName() + ", actual:" + actual);
                    }
                } else {
                    logErr( "Expected Integer to be returned, actual:" + oActual);
                }
            } else {
                logErr( "Expected execute() to return false, actual: true");
            }
            getEntityTransaction().commit();

        } catch (Exception ex) {
            logErr( "Received unexpected exception:", ex);
        }

        if (!pass2 || !pass4 || !pass6) {
            throw new Fault("getOutputParameterValueIntTest failed");
        }

    }

    /*
     * @testName: getOutputParameterValueIntIllegalArgumentExceptionTest
     *
     * @assertion_ids: PERSISTENCE:JAVADOC:1548; PERSISTENCE:SPEC:1302.4;
     *
     * @test_Strategy:
     *
     */
        public void getOutputParameterValueIntIllegalArgumentExceptionTest() throws Exception {
        boolean pass2 = false;
        boolean pass4 = false;

        try {
            getEntityTransaction().begin();

            logMsg( "Get a value that does not exist");
            StoredProcedureQuery spq1 = getEntityManager().createStoredProcedureQuery("GetEmpFirstNameFromOut");
            spq1.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
            spq1.registerStoredProcedureParameter(2, String.class, ParameterMode.OUT);
            spq1.setParameter(1, 1);

            if (!spq1.execute()) {
                try {
                    spq1.getOutputParameterValue(99);
                    logErr( "Did not throw IllegalArgumentException");
                } catch (IllegalArgumentException iae) {
                    logTrace( "Received expected IllegalArgumentException");
                    if (getEntityTransaction().getRollbackOnly() != true) {
                        pass2 = true;
                        logTrace( "Transaction was not marked for rollback");
                    } else {
                        logErr( "Transaction was marked for rollback and should not have been");
                    }
                } catch (Exception e) {
                    logErr( "Caught unexpected exception", e);
                }
            } else {
                logErr( "Expected execute() to return false, actual: true");
            }
            logMsg( "Get the value from an IN parameter");
            StoredProcedureQuery spq2 = getEntityManager().createStoredProcedureQuery("GetEmpLastNameFromInOut");
            spq2.registerStoredProcedureParameter(1, String.class, ParameterMode.INOUT);
            spq2.setParameter(1, "1");

            if (!spq2.execute()) {
                try {
                    spq2.getOutputParameterValue(99);
                    logErr( "Did not throw IllegalArgumentException");
                } catch (IllegalArgumentException iae) {
                    logTrace( "Received expected IllegalArgumentException");
                    pass4 = true;
                } catch (Exception e) {
                    logErr( "Caught unexpected exception", e);
                }

                if (getEntityTransaction().isActive()) {
                    getEntityTransaction().rollback();
                }
            } else {
                logErr( "Expected execute() to return false, actual: true");
            }
        } catch (Exception ex) {
            logErr( "Received unexpected exception:", ex);
        }

        if (!pass2 || !pass4) {
            throw new Fault("getOutputParameterValueIntIllegalArgumentExceptionTest failed");
        }

    }

    /*
     * @testName: getFirstResultTest
     *
     * @assertion_ids: PERSISTENCE:JAVADOC:1585; PERSISTENCE:JAVADOC:3482;
     * PERSISTENCE:SPEC:1515;
     *
     * @test_Strategy:
     *
     */
        public void getFirstResultTest() throws Exception {
        boolean pass1 = false;
        boolean pass2 = false;
        boolean pass3 = false;
        try {
            getEntityTransaction().begin();

            StoredProcedureQuery spq = getEntityManager().createStoredProcedureQuery("GetEmpASCFromRS", Employee.class);
            if (dataBaseName.equalsIgnoreCase(ORACLE) || dataBaseName.equalsIgnoreCase(POSTGRESQL)) {
                logTrace( "register refcursor parameter");
                spq.registerStoredProcedureParameter(1, void.class, ParameterMode.REF_CURSOR);
            }
            int num = spq.getFirstResult();
            if (num == 0) {
                logTrace( "Received expected first result when none is set:" + num);
                pass1 = true;
            } else {
                logErr( "Expected first result when none is set: 0, actual:" + num);
            }

            List<Employee> actual = spq.getResultList();
            num = spq.getFirstResult();
            if (num == 0) {
                logTrace( "Received expected first result:" + num + " after getResultList");
                pass2 = true;
            } else {
                logErr( "Expected first result: 0, actual:" + num + " after getResultList");
            }
            if (actual.size() > 0) {
                pass3 = verifyListEmployees(empRef, actual);
            } else {
                logErr( "getResultList() returned 0 results");
            }
            getEntityTransaction().commit();

        } catch (Exception ex) {
            logErr( "Received unexpected exception:", ex);
        }

        if (!pass1 || !pass2 || !pass3) {
            throw new Fault("getFirstResultTest failed");
        }

    }

    /*
     * @testName: getMaxResultsTest
     *
     * @assertion_ids: PERSISTENCE:JAVADOC:1590;
     *
     * @test_Strategy:
     *
     */
        public void getMaxResultsTest() throws Exception {
        boolean pass1 = false;
        boolean pass2 = false;
        boolean pass3 = false;

        try {
            getEntityTransaction().begin();

            StoredProcedureQuery spq = getEntityManager().createStoredProcedureQuery("GetEmpASCFromRS", Employee.class);
            if (dataBaseName.equalsIgnoreCase(ORACLE) || dataBaseName.equalsIgnoreCase(POSTGRESQL)) {
                logTrace( "register refcursor parameter");
                spq.registerStoredProcedureParameter(1, void.class, ParameterMode.REF_CURSOR);
            }
            int num = spq.getMaxResults();
            if (num == Integer.MAX_VALUE) {
                logTrace( "Received expected max result:" + num);
                pass1 = true;
            } else {
                logErr( "Expected max result: " + Integer.MAX_VALUE + ", actual:" + num);
            }
            List<Employee> actual = spq.getResultList();
            num = spq.getMaxResults();
            if (num == Integer.MAX_VALUE) {
                logTrace( "Received expected max result:" + num + " after getResultList");
                pass2 = true;
            } else {
                logErr(
                        "Expected max result:" + Integer.MAX_VALUE + ", actual:" + num + " after getResultList");
            }
            if (actual.size() > 0) {
                pass3 = verifyListEmployees(empRef, actual);
            } else {
                logErr( "getResultList() returned 0 results");
            }
            getEntityTransaction().commit();

        } catch (Exception ex) {
            logErr( "Received unexpected exception:", ex);
        }

        if (!pass1 || !pass2 || !pass3) {
            throw new Fault("getMaxResultsTest failed");
        }

    }

    /*
     * @testName: getSingleResultTest
     *
     * @assertion_ids: PERSISTENCE:JAVADOC:3482; PERSISTENCE:SPEC:1515;
     *
     * @test_Strategy: Get single result from returned resultset.
     *
     */
        public void getSingleResultTest() throws Exception {
        boolean pass = false;

        try {
            getEntityTransaction().begin();
            clearCache();

            StoredProcedureQuery spq = getEntityManager().createStoredProcedureQuery("GetEmpIdFNameLNameFromRS",
                    "id-firstname-lastname");
            spq.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
            if (dataBaseName.equalsIgnoreCase(ORACLE) || dataBaseName.equalsIgnoreCase(POSTGRESQL)) {
                logTrace( "register refcursor parameter");
                spq.registerStoredProcedureParameter(2, void.class, ParameterMode.REF_CURSOR);
            }
            spq.setParameter(1, 1);
            Employee expected = new Employee(emp0.getId(), emp0.getFirstName(), emp0.getLastName());
            Object o = spq.getSingleResult();
            if (o instanceof Employee) {
                Employee actual = (Employee) o;
                if (actual.equals(expected)) {
                    logTrace( "Received expected result:" + actual);
                    pass = true;
                } else {
                    logErr( "Expected result:" + expected + ", actual:" + actual);
                }
            } else {
                logErr( "Did not get Integer result:" + o);
            }
            getEntityTransaction().commit();

        } catch (Exception ex) {
            logErr( "Received unexpected exception:", ex);
        }

        if (!pass) {
            throw new Fault("getSingleResultTest failed");
        }

    }

    /*
     * @testName: getSingleResultOrNullWithValueTest
     *
     * @assertion_ids: PERSISTENCE:JAVADOC:3482; PERSISTENCE:SPEC:1515;
     *
     * @test_Strategy: Get single result from returned resultset. Expected result is some value.
     *
     */
        public void getSingleResultOrNullWithValueTest() throws Exception {
        boolean pass = false;

        try {
            getEntityTransaction().begin();
            clearCache();

            StoredProcedureQuery spq = getEntityManager().createStoredProcedureQuery("GetEmpIdFNameLNameFromRS",
                    "id-firstname-lastname");
            spq.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
            if (dataBaseName.equalsIgnoreCase(ORACLE) || dataBaseName.equalsIgnoreCase(POSTGRESQL)) {
                logTrace( "register refcursor parameter");
                spq.registerStoredProcedureParameter(2, void.class, ParameterMode.REF_CURSOR);
            }
            spq.setParameter(1, 1);
            Employee expected = new Employee(emp0.getId(), emp0.getFirstName(), emp0.getLastName());
            Object o = spq.getSingleResultOrNull();
            if (o instanceof Employee) {
                Employee actual = (Employee) o;
                if (actual.equals(expected)) {
                    logTrace( "Received expected result:" + actual);
                    pass = true;
                } else {
                    logErr( "Expected result:" + expected + ", actual:" + actual);
                }
            } else {
                logErr( "Did not get Integer result:" + o);
            }
            getEntityTransaction().commit();

        } catch (Exception ex) {
            logErr( "Received unexpected exception:", ex);
        }

        if (!pass) {
            throw new Fault("getSingleResultOrNullWithValueTest failed");
        }
    }

    /*
     * @testName: getSingleResultOrNullWithNullTest
     *
     * @assertion_ids: PERSISTENCE:JAVADOC:3482; PERSISTENCE:SPEC:1515;
     *
     * @test_Strategy: Get single result from returned resultset. Expected result is null.
     *
     */
        public void getSingleResultOrNullWithNullTest() throws Exception {
        boolean pass = false;

        try {
            getEntityTransaction().begin();
            clearCache();

            StoredProcedureQuery spq = getEntityManager().createStoredProcedureQuery("GetEmpIdFNameLNameFromRS",
                    "id-firstname-lastname");
            spq.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
            if (dataBaseName.equalsIgnoreCase(ORACLE) || dataBaseName.equalsIgnoreCase(POSTGRESQL)) {
                logTrace( "register refcursor parameter");
                spq.registerStoredProcedureParameter(2, void.class, ParameterMode.REF_CURSOR);
            }
            spq.setParameter(1, 0);
            Object result = spq.getSingleResultOrNull();
            if (result == null) {
                logTrace( "Received expected null value.");
                pass = true;
            } else {
                Employee actual = (Employee) result;
                logErr( "Unexpected not null result:" + actual);
            }
            getEntityTransaction().commit();

        } catch (Exception ex) {
            logErr( "Received unexpected exception:", ex);
        }

        if (!pass) {
            throw new Fault("getSingleResultOrNullWithNullTest failed");
        }
    }

    /*
     * @testName: getSingleResultNoResultExceptionTest
     *
     * @assertion_ids: PERSISTENCE:JAVADOC:3483
     *
     * @test_Strategy: Get single result from returned resultset that is empty
     *
     */
        public void getSingleResultNoResultExceptionTest() throws Exception {
        boolean pass = false;

        try {
            getEntityTransaction().begin();

            StoredProcedureQuery spq = getEntityManager().createStoredProcedureQuery("GetEmpIdFNameLNameFromRS");
            spq.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
            if (dataBaseName.equalsIgnoreCase(ORACLE) || dataBaseName.equalsIgnoreCase(POSTGRESQL)) {
                logTrace( "register refcursor parameter");
                spq.registerStoredProcedureParameter(2, void.class, ParameterMode.REF_CURSOR);
            }
            spq.setParameter(1, 99);
            try {
                spq.getSingleResult();
                logErr( "Did not throw NoResultException");
            } catch (NoResultException nre) {
                logTrace( "Received expected NoResultException");
                pass = true;
            } catch (Exception e) {
                logErr( "Caught unexpected exception", e);

            }
            if (getEntityTransaction().isActive()) {
                getEntityTransaction().rollback();
            }
        } catch (Exception ex) {
            logErr( "Received unexpected exception:", ex);
        }

        if (!pass) {
            throw new Fault("getSingleResultNoResultExceptionTest failed");
        }

    }

    /*
     * @testName: getSingleResultNonUniqueResultExceptionTest
     *
     * @assertion_ids: PERSISTENCE:JAVADOC:3484
     *
     * @test_Strategy: Get single result from returned resultset that contains
     * multiple values
     *
     */
        public void getSingleResultNonUniqueResultExceptionTest() throws Exception {
        boolean pass = false;

        try {
            getEntityTransaction().begin();

            StoredProcedureQuery spq = getEntityManager().createStoredProcedureQuery("GetEmpASCFromRS", Employee.class);
            if (dataBaseName.equalsIgnoreCase(ORACLE) || dataBaseName.equalsIgnoreCase(POSTGRESQL)) {
                logTrace( "register refcursor parameter");
                spq.registerStoredProcedureParameter(1, void.class, ParameterMode.REF_CURSOR);
            }
            try {
                spq.getSingleResult();
                logErr( "Did not throw NonUniqueResultException");
            } catch (NonUniqueResultException nure) {
                logTrace( "Received expected NonUniqueResultException");
                pass = true;
            } catch (Exception e) {
                logErr( "Caught unexpected exception", e);
            }
            if (getEntityTransaction().isActive()) {
                getEntityTransaction().rollback();
            }
        } catch (Exception ex) {
            logErr( "Received unexpected exception:", ex);
        }

        if (!pass) {
            throw new Fault("getSingleResultNonUniqueResultExceptionTest failed");
        }

    }

    /*
     * @testName: setgetFlushModeTest
     *
     * @assertion_ids: PERSISTENCE:JAVADOC:1586; PERSISTENCE:JAVADOC:1559;
     *
     * @test_Strategy: Set and Get the various flushModes of a Query
     */
        public void setgetFlushModeTest() throws Exception {
        boolean pass1 = false;
        boolean pass2 = false;

        try {
            logMsg( "Testing StoredProcedureQuery");
            StoredProcedureQuery spq = getEntityManager().createStoredProcedureQuery("GetEmpASCFromRS", Employee.class);
            if (dataBaseName.equalsIgnoreCase(ORACLE) || dataBaseName.equalsIgnoreCase(POSTGRESQL)) {
                logTrace( "register refcursor parameter");
                spq.registerStoredProcedureParameter(1, void.class, ParameterMode.REF_CURSOR);
            }
            FlushModeType fmt = spq.getFlushMode();
            if (fmt != null) {
                if (fmt.equals(getEntityManager().getFlushMode())) {
                    logTrace( "Setting mode to returned default mode");
                    spq.setFlushMode(fmt);
                    logTrace( "Setting mode to FlushModeType.COMMIT");
                    spq.setFlushMode(FlushModeType.COMMIT);
                    fmt = spq.getFlushMode();
                    if (fmt.equals(FlushModeType.COMMIT)) {
                        logTrace( "Setting mode to FlushModeType.AUTO");
                        spq.setFlushMode(FlushModeType.AUTO);
                        fmt = spq.getFlushMode();
                        if (fmt.equals(FlushModeType.AUTO)) {
                            logTrace( "Query object returned from setFlushMode");
                            Query q = spq.setFlushMode(FlushModeType.COMMIT);
                            fmt = q.getFlushMode();
                            if (fmt.equals(FlushModeType.COMMIT)) {
                                logTrace( "Received expected Query FlushModeType:" + fmt.name());
                                pass1 = true;
                            } else {
                                logErr( "Expected a value of:" + FlushModeType.COMMIT.name()
                                        + ", actual:" + fmt.name());
                            }
                        } else {
                            logErr(
                                    "Expected a value of:" + FlushModeType.AUTO.name() + ", actual:" + fmt.name());
                        }
                    } else {
                        logErr( "Expected a default value of:" + FlushModeType.COMMIT.name()
                                + ", actual:" + fmt.name());
                    }
                } else {
                    logErr( "Expected EntityManager value of:"
                            + getEntityManager().getFlushMode() + ", actual:" + fmt.name());
                }
            } else {
                logErr( "getFlushMode return null");
            }

        } catch (Exception e) {
            logErr( "Caught exception: ", e);
        }
        try {
            logMsg( "Testing Query object returned from setFlushMode");
            StoredProcedureQuery spq2 = getEntityManager().createStoredProcedureQuery("GetEmpASCFromRS",
                    Employee.class);

            logTrace( "Setting mode to FlushModeType.AUTO");
            Query q = spq2.setFlushMode(FlushModeType.AUTO);
            FlushModeType fmt = q.getFlushMode();
            if (fmt.equals(FlushModeType.AUTO)) {
                logTrace( "Received expected value of:" + fmt.name());
                pass2 = true;
            } else {
                logErr(
                        "Expected a value of:" + FlushModeType.AUTO.name() + ", actual:" + fmt.name());
            }

        } catch (Exception e) {
            logErr( "Caught exception: ", e);
        }

        if (!pass1 || !pass2)
            throw new Fault("setgetFlushModeTest failed");
    }

    /*
     * @testName: setLockModeIllegalStateExceptionTest
     *
     * @assertion_ids: PERSISTENCE:JAVADOC:1637;
     *
     * @test_Strategy:
     */
        public void setLockModeIllegalStateExceptionTest() throws Exception {
        boolean pass = false;
        try {
            getEntityTransaction().begin();

            StoredProcedureQuery spq = getEntityManager().createStoredProcedureQuery("UpdateEmpSalaryColumn");
            try {
                spq.setLockMode(LockModeType.PESSIMISTIC_READ);
                logErr( "Did not throw IllegalStateException");
            } catch (IllegalStateException ise) {
                logTrace( "Received expected IllegalStateException");
                pass = true;
            } catch (Exception e) {
                logErr( "Caught unexpected exception", e);
            }
            if (getEntityTransaction().isActive()) {
                getEntityTransaction().rollback();
            }
        } catch (Exception e) {
            logErr( "Caught exception: ", e);
        }

        if (!pass)
            throw new Fault("setLockModeIllegalStateExceptionTest failed");
    }

    /*
     * @testName: getLockModeIllegalStateExceptionTest
     *
     * @assertion_ids: PERSISTENCE:JAVADOC:1589;
     *
     * @test_Strategy:
     */
        public void getLockModeIllegalStateExceptionTest() throws Exception {
        boolean pass = false;
        try {
            getEntityTransaction().begin();

            StoredProcedureQuery spq = getEntityManager().createStoredProcedureQuery("UpdateEmpSalaryColumn");
            try {
                spq.getLockMode();
                logErr( "Did not throw IllegalStateException");
            } catch (IllegalStateException ise) {
                logTrace( "Received expected IllegalStateException");
                pass = true;
            } catch (Exception e) {
                logErr( "Caught unexpected exception", e);
            }
            if (getEntityTransaction().isActive()) {
                getEntityTransaction().rollback();
            }
        } catch (Exception e) {
            logErr( "Caught exception: ", e);
        }

        if (!pass)
            throw new Fault("getLockModeIllegalStateExceptionTest failed");
    }

    /*
     * @testName: setGetParameterIntTest
     *
     * @assertion_ids: PERSISTENCE:JAVADOC:1597; PERSISTENCE:JAVADOC:1574;
     *
     * @test_Strategy:
     */
        public void setGetParameterIntTest() throws Exception {
        boolean pass1 = false;
        boolean pass2 = false;
        try {
            getEntityTransaction().begin();

            logMsg( "Testing StoredProcedureQuery");
            StoredProcedureQuery spq = getEntityManager().createStoredProcedureQuery("GetEmpFirstNameFromOut");
            spq.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
            spq.registerStoredProcedureParameter(2, String.class, ParameterMode.OUT);
            spq.setParameter(1, 1);
            Parameter p = spq.getParameter(1);
            int pos = p.getPosition();
            if (pos == 1) {
                logTrace( "Received expected parameter:" + pos);
                pass1 = true;
            } else {
                logErr( "Expected position: 1, actual:" + pos);
            }

            logMsg( "Testing Query object returned from getParameter");
            StoredProcedureQuery spq2 = getEntityManager().createStoredProcedureQuery("GetEmpFirstNameFromOut");
            spq2.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
            spq2.registerStoredProcedureParameter(2, String.class, ParameterMode.OUT);
            Query q = spq2.setParameter(1, 1);
            p = q.getParameter(1);
            pos = p.getPosition();
            if (pos == 1) {
                logTrace( "Received expected parameter:" + pos);
                pass2 = true;
            } else {
                logErr( "Expected position: 1, actual:" + pos);
            }
            getEntityTransaction().commit();

        } catch (Exception e) {
            logErr( "Caught exception: ", e);
        }

        if (!pass1 || !pass2)
            throw new Fault("setGetParameterIntTest failed");
    }

    // disabling this test since Store Procedures with named parameters
    // isn't supported by all DB's at this time
    /*
     * test Name: setGetParameterStringTest assertion_ids: PERSISTENCE:JAVADOC:1549;
     * PERSISTENCE:JAVADOC:1558; PERSISTENCE:JAVADOC:1568; PERSISTENCE:JAVADOC:1591;
     * test_Strategy:
     */

    /*
     * public void setGetParameterStringTest() throws Exception { boolean pass1 =
     * false; boolean pass2 = false; boolean pass3 = false; try {
     * getEntityTransaction().begin();
     *
     * logMsg("Testing StoredProcedureQuery");
     * StoredProcedureQuery spq =
     * getEntityManager().createStoredProcedureQuery("GetEmpFirstNameFromOut");
     * spq.registerStoredProcedureParameter("IN_PARAM", Integer.class,
     * ParameterMode.IN); spq.registerStoredProcedureParameter("OUT_PARAM",
     * String.class, ParameterMode.OUT); spq.setParameter("IN_PARAM", 1);
     *
     * Parameter p = spq.getParameter("OUT_PARAM"); Integer pos = p.getPosition();
     * if (pos == null){ logTrace("Received expected null");
     * pass1 = true; } else {
     * logErr("Expected position: null, actual:" + pos); }
     *
     * spq.execute(); Object oActual = spq.getOutputParameterValue("OUT_PARAM"); if
     * (oActual instanceof String) { String actual = (String) oActual; if
     * (actual.equals(emp0.getFirstName())) {
     * logTrace("Received expected result:" + actual); pass2 =
     * true; } else { logErr("Expected result: " +
     * emp0.getFirstName() + ", actual:" + actual); } } else {
     * logErr("Did not get instance of String, instead:" +
     * oActual.getClass()); } getEntityTransaction().commit();
     *
     * } catch (Exception e) { logErr("Caught exception: ",
     * e); }
     *
     * if (!pass1 || !pass2 ) throw new
     * Exception("setGetParameterStringTest failed"); }
     */

    /*
     * @testName: getParameterStringExceptionTest
     *
     * @assertion_ids: PERSISTENCE:JAVADOC:1609; PERSISTENCE:JAVADOC:1569;
     *
     * @test_Strategy:
     */
        public void getParameterStringExceptionTest() throws Exception {
        boolean pass1 = false;
        boolean pass2 = false;
        try {
            getEntityTransaction().begin();
            logMsg( "Testing StoredProcedureQuery ");

            StoredProcedureQuery spq = getEntityManager().createStoredProcedureQuery("GetEmpFirstNameFromOut");
            spq.registerStoredProcedureParameter("ID", Integer.class, ParameterMode.IN);
            spq.registerStoredProcedureParameter("FIRSTNAME", String.class, ParameterMode.OUT);
            spq.setParameter("ID", 1);
            try {
                spq.getParameter("DOESNOTEXIST");
                logErr( "Did not throw IllegalArgumentException");
            } catch (IllegalArgumentException iae) {
                logTrace( "Received expected IllegalArgumentException");
                pass1 = true;
            } catch (IllegalStateException ise) {
                logTrace( "Received expected IllegalStateException");
                pass1 = true;
            } catch (Exception e) {
                logErr( "Caught unexpected exception", e);
            }

            logMsg( "Testing Query object returned from getParameter");
            StoredProcedureQuery spq2 = getEntityManager().createStoredProcedureQuery("GetEmpFirstNameFromOut");
            spq2.registerStoredProcedureParameter("ID", Integer.class, ParameterMode.IN);
            spq2.registerStoredProcedureParameter("FIRSTNAME", String.class, ParameterMode.OUT);
            Query q = spq2.setParameter("ID", 1);
            try {
                q.getParameter("DOESNOTEXIST");
                logErr( "Did not throw IllegalArgumentException");
            } catch (IllegalArgumentException iae) {
                logTrace( "Received expected IllegalArgumentException");
                pass2 = true;
            } catch (IllegalStateException ise) {
                logTrace( "Received expected IllegalStateException");
                pass2 = true;
            } catch (Exception e) {
                logErr( "Caught unexpected exception", e);
            }

            if (getEntityTransaction().isActive()) {
                getEntityTransaction().rollback();
            }
        } catch (Exception e) {
            logErr( "Caught exception", e);
        }

        if (!pass1 || !pass2)
            throw new Fault("getParameterStringExceptionTest failed");
    }

    /*
     * @testName: getParameterIntIllegalArgumentExceptionTest
     *
     * @assertion_ids: PERSISTENCE:JAVADOC:1598;
     *
     * @test_Strategy:
     */
        public void getParameterIntIllegalArgumentExceptionTest() throws Exception {
        boolean pass1 = false;
        boolean pass2 = false;
        try {
            getEntityTransaction().begin();
            logMsg( "Testing StoredProcedureQuery ");

            StoredProcedureQuery spq = getEntityManager().createStoredProcedureQuery("GetEmpFirstNameFromOut");
            spq.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
            spq.registerStoredProcedureParameter(2, String.class, ParameterMode.OUT);
            spq.setParameter(1, 1);
            try {
                spq.getParameter(99);
                logErr( "Did not throw IllegalArgumentException");
            } catch (IllegalArgumentException iae) {
                logTrace( "Received expected IllegalArgumentException");
                pass1 = true;
            } catch (Exception e) {
                logErr( "Caught unexpected exception", e);
            }

            logMsg( "Testing Query object returned from getParameter");
            StoredProcedureQuery spq2 = getEntityManager().createStoredProcedureQuery("GetEmpFirstNameFromOut");
            spq2.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
            spq2.registerStoredProcedureParameter(2, String.class, ParameterMode.OUT);
            Query q = spq2.setParameter(1, 1);
            try {
                q.getParameter(99);
                logErr( "Did not throw IllegalArgumentException");
            } catch (IllegalArgumentException iae) {
                logTrace( "Received expected IllegalArgumentException");
                pass2 = true;
            } catch (Exception e) {
                logErr( "Caught unexpected exception", e);
            }

            if (getEntityTransaction().isActive()) {
                getEntityTransaction().rollback();
            }
        } catch (Exception e) {
            logErr( "Caught exception", e);
        }

        if (!pass1 || !pass2)
            throw new Fault("getParameterIntIllegalArgumentExceptionTest failed");
    }

    /*
     * @testName: setParameterParameterObjectTest
     *
     * @assertion_ids: PERSISTENCE:JAVADOC:1562;
     *
     * @test_Strategy:
     */
        public void setParameterParameterObjectTest() throws Exception {
        boolean pass2 = false;
        boolean pass4 = false;
        try {
            getEntityTransaction().begin();

            logMsg( "Testing StoredProcedureQuery ");

            StoredProcedureQuery spq = getEntityManager().createStoredProcedureQuery("GetEmpFirstNameFromOut");
            spq.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
            spq.registerStoredProcedureParameter(2, String.class, ParameterMode.OUT);
            spq.setParameter(1, 99);
            Parameter p = spq.getParameter(1);
            spq.setParameter(p, 1);

            if (!spq.execute()) {
                Object oActual = spq.getOutputParameterValue(2);
                if (oActual instanceof String) {
                    String actual = (String) oActual;
                    if (actual.equals(emp0.getFirstName())) {
                        logTrace( "Received expected result:" + actual);
                        pass2 = true;
                    } else {
                        logErr(
                                "Expected result: " + emp0.getFirstName() + ", actual:" + actual);
                    }
                } else {
                    logErr( "Did not get instance of String, instead:" + oActual.getClass());
                }
            } else {
                logErr( "Expected execute() to return false, actual: true");
            }
            getEntityTransaction().commit();

        } catch (Exception e) {
            logErr( "Caught exception", e);
            if (getEntityTransaction().isActive()) {
                getEntityTransaction().rollback();
            }
        }
        try {
            getEntityTransaction().begin();

            logMsg( "Testing Query object returned from setParameter");
            StoredProcedureQuery spq2 = getEntityManager().createStoredProcedureQuery("GetEmpFirstNameFromOut");
            spq2.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
            spq2.registerStoredProcedureParameter(2, String.class, ParameterMode.OUT);
            Query q = spq2;
            q.setParameter(1, 99);
            Parameter p = q.getParameter(1);
            q.setParameter(p, 1);
            StoredProcedureQuery spq3 = (StoredProcedureQuery) q;
            if (!spq3.execute()) {
                Object oActual = spq3.getOutputParameterValue(2);
                if (oActual instanceof String) {
                    String actual = (String) oActual;
                    if (actual.equals(emp0.getFirstName())) {
                        logTrace( "Received expected result:" + actual);
                        pass4 = true;
                    } else {
                        logErr(
                                "Expected result: " + emp0.getFirstName() + ", actual:" + actual);
                    }
                } else {
                    logErr( "Did not get instance of String, instead:" + oActual);
                }
            } else {
                logErr( "Expected execute() to return false, actual: true");
            }
            getEntityTransaction().commit();

        } catch (Exception e) {
            logErr( "Caught exception", e);
            if (getEntityTransaction().isActive()) {
                getEntityTransaction().rollback();
            }
        }

        if (!pass2 || !pass4)
            throw new Fault("setParameterParameterObjectTest failed");

    }

    /*
     * @testName: setParameterParameterObjectIllegalArgumentExceptionTest
     *
     * @assertion_ids: PERSISTENCE:JAVADOC:1563;
     *
     * @test_Strategy: call setParameter(Parameter, Object) using parameter from
     * different query.
     */
        public void setParameterParameterObjectIllegalArgumentExceptionTest() throws Exception {
        boolean pass1 = false;
        try {
            getEntityTransaction().begin();

            logTrace( "Get parameter from other stored procedure");
            StoredProcedureQuery spq = getEntityManager().createStoredProcedureQuery("GetEmpLastNameFromInOut");
            spq.registerStoredProcedureParameter(1, String.class, ParameterMode.INOUT);
            spq.setParameter(1, "INOUT");
            // Parameter to be used in next StoredProcedure
            Parameter p = spq.getParameter(1);

            StoredProcedureQuery spq2 = getEntityManager().createStoredProcedureQuery("GetEmpFirstNameFromOut");
            spq2.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
            spq2.registerStoredProcedureParameter(2, String.class, ParameterMode.OUT);

            try {
                spq2.setParameter(p, 1);
                logErr( "Did not throw IllegalArgumentException");
            } catch (IllegalArgumentException iae) {
                logTrace( "Received expected IllegalArgumentException");
                pass1 = true;
            } catch (Exception e) {
                logErr( "Caught unexpected exception", e);
            }

            if (getEntityTransaction().isActive()) {
                getEntityTransaction().rollback();
            }
        } catch (Exception e) {
            logErr( "Caught exception", e);
        }

        if (!pass1)
            throw new Fault("setParameterParameterObjectIllegalArgumentExceptionTest failed");
    }

    /*
     * @testName: setParameterIntObjectIllegalArgumentExceptionTest
     *
     * @assertion_ids: PERSISTENCE:JAVADOC:1575;
     *
     * @test_Strategy:
     */
        public void setParameterIntObjectIllegalArgumentExceptionTest() throws Exception {
        boolean pass1 = false;
        boolean pass2 = false;
        boolean pass3 = false;
        boolean pass4 = false;
        try {
            getEntityTransaction().begin();
            logMsg( "Testing StoredProcedureQuery with incorrect position specified");

            StoredProcedureQuery spq1 = getEntityManager().createStoredProcedureQuery("GetEmpFirstNameFromOut");
            spq1.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
            spq1.registerStoredProcedureParameter(2, String.class, ParameterMode.OUT);
            try {
                spq1.setParameter(99, 1);
                logErr( "Did not throw IllegalArgumentException");
            } catch (IllegalArgumentException iae) {
                logTrace( "Received expected IllegalArgumentException");
                pass1 = true;
            } catch (Exception e) {
                logErr( "Caught unexpected exception", e);
            }

            logMsg( "Testing StoredProcedureQuery with incorrect type specified");

            StoredProcedureQuery spq2 = getEntityManager().createStoredProcedureQuery("GetEmpFirstNameFromOut");
            spq2.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
            spq2.registerStoredProcedureParameter(2, String.class, ParameterMode.OUT);
            try {
                spq2.setParameter(1, new java.util.Date());
                logErr( "Did not throw IllegalArgumentException");
            } catch (IllegalArgumentException iae) {
                logTrace( "Received expected IllegalArgumentException");
                pass2 = true;
            } catch (Exception e) {
                logErr( "Caught unexpected exception", e);
            }

            logMsg( "Testing Query object with incorrect position specified");
            StoredProcedureQuery spq3 = getEntityManager().createStoredProcedureQuery("GetEmpFirstNameFromOut");
            spq3.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
            spq3.registerStoredProcedureParameter(2, String.class, ParameterMode.OUT);
            Query q1 = spq3.setParameter(1, 1);
            try {
                spq3.setParameter(99, 1);
                logErr( "Did not throw IllegalArgumentException");
            } catch (IllegalArgumentException iae) {
                logTrace( "Received expected IllegalArgumentException");
                pass3 = true;
            } catch (Exception e) {
                logErr( "Caught unexpected exception", e);
            }

            logMsg( "Testing Query object with incorrect type specified");
            StoredProcedureQuery spq4 = getEntityManager().createStoredProcedureQuery("GetEmpFirstNameFromOut");
            spq4.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
            spq4.registerStoredProcedureParameter(2, String.class, ParameterMode.OUT);
            Query q2 = spq4.setParameter(1, 1);
            try {
                q2.setParameter(1, new java.util.Date());
                logErr( "Did not throw IllegalArgumentException");
            } catch (IllegalArgumentException iae) {
                logTrace( "Received expected IllegalArgumentException");
                pass4 = true;
            } catch (Exception e) {
                logErr( "Caught unexpected exception", e);
            }
            if (getEntityTransaction().isActive()) {
                getEntityTransaction().rollback();
            }
        } catch (Exception e) {
            logErr( "Caught exception", e);
        }

        if (!pass1 || !pass2 || !pass3 || !pass4)
            throw new Fault("setParameterIntObjectIllegalArgumentExceptionTest failed");
    }

    /*
     * @testName: getParametersTest
     *
     * @assertion_ids: PERSISTENCE:JAVADOC:1597; PERSISTENCE:JAVADOC:1603;
     *
     * @test_Strategy:
     */
        public void getParametersTest() throws Exception {
        boolean pass1 = false;
        boolean pass3 = false;
        try {
            getEntityTransaction().begin();

            StoredProcedureQuery spq = getEntityManager().createStoredProcedureQuery("GetEmpFirstNameFromOut");
            Set<Parameter<?>> p1 = spq.getParameters();
            if (p1.size() == 0) {
                logTrace( "Received expected number of parameters when non exist:" + p1.size());
                pass1 = true;
            } else {
                logErr( "Expected number of parameters when non exist: 0, actual:" + p1);
            }
            spq.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
            spq.registerStoredProcedureParameter(2, String.class, ParameterMode.OUT);
            spq.setParameter(1, 1);

            if (!spq.execute()) {
                Set<Parameter<?>> p2 = spq.getParameters();
                if (p2.size() == 2) {
                    logTrace( "Received expected number of parameters:" + p2.size());
                    pass3 = true;
                } else {
                    logErr( "Expected number of parameters: 2, actual:" + p2.size());
                }
            } else {
                logErr( "Expected execute() to return false, actual: true");
            }
            getEntityTransaction().commit();

        } catch (Exception e) {
            logErr( "Caught exception: ", e);
        }

        if (!pass1 || !pass3)
            throw new Fault("getParametersTest failed");

    }

    /*
     * @testName: setParameterIntDateTemporalTypeTest
     *
     * @assertion_ids: PERSISTENCE:JAVADOC:1578;
     *
     * @test_Strategy:
     */
        public void setParameterIntDateTemporalTypeTest() throws Exception {
        boolean pass2 = false;
        boolean pass4 = false;
        try {
            getEntityTransaction().begin();
            try {
                logMsg( "Testing StoredProcedureQuery");

                StoredProcedureQuery spq = getEntityManager()
                        .createStoredProcedureQuery("GetEmpIdUsingHireDateFromOut");
                spq.registerStoredProcedureParameter(1, Date.class, ParameterMode.IN);
                spq.registerStoredProcedureParameter(2, Integer.class, ParameterMode.OUT);
                spq.setParameter(1, utilDate, TemporalType.DATE);

                if (!spq.execute()) {

                    Object o = spq.getOutputParameterValue(2);
                    if (o instanceof Integer) {
                        Integer actual = (Integer) o;
                        if (actual == 1) {
                            logTrace( "Received expected id:" + actual);
                            pass2 = true;
                        } else {
                            logErr( "Expected id: 1, actual:" + actual);
                        }

                    } else {
                        logErr( "Did not get instance of Integer back:" + o);
                    }
                } else {
                    logErr( "Expected execute() to return false, actual: true");
                }
            } catch (Exception e) {
                logErr( "Caught exception: ", e);
            }
            try {
                logMsg( "Testing Query object");
                StoredProcedureQuery spq1 = getEntityManager()
                        .createStoredProcedureQuery("GetEmpIdUsingHireDateFromOut");
                spq1.registerStoredProcedureParameter(1, Date.class, ParameterMode.IN);
                spq1.registerStoredProcedureParameter(2, Integer.class, ParameterMode.OUT);
                Query q = spq1;
                q.setParameter(1, utilDate, TemporalType.DATE);
                StoredProcedureQuery spq2 = (StoredProcedureQuery) q;

                if (!spq2.execute()) {

                    Object o = spq2.getOutputParameterValue(2);
                    if (o instanceof Integer) {
                        int actual = (Integer) o;
                        if (actual == 1) {
                            logTrace( "Received expected id:" + actual);
                            pass4 = true;
                        } else {
                            logErr( "Expected id: 1, actual:" + actual);
                        }

                    } else {
                        logErr( "Did not get instance of Integer back:" + o);
                    }
                } else {
                    logErr( "Expected execute() to return false, actual: true");
                }
            } catch (Exception e) {
                logErr( "Caught exception: ", e);
            }
            getEntityTransaction().commit();

        } catch (Exception e) {
            logErr( "Caught exception: ", e);
        }

        if (!pass2 || !pass4)
            throw new Fault("setParameterIntDateTemporalTypeTest failed");

    }

    /*
     * @testName: setParameterIntDateTemporalTypeIllegalArgumentExceptionTest
     *
     * @assertion_ids: PERSISTENCE:JAVADOC:1579;
     *
     * @test_Strategy:
     */
        public void setParameterIntDateTemporalTypeIllegalArgumentExceptionTest() throws Exception {
        boolean pass1 = false;
        boolean pass2 = false;
        try {
            getEntityTransaction().begin();
            logMsg( "Testing StoredProcedureQuery with incorrect position specified");

            StoredProcedureQuery spq1 = getEntityManager().createStoredProcedureQuery("GetEmpIdUsingHireDateFromOut");
            spq1.registerStoredProcedureParameter(1, Date.class, ParameterMode.IN);
            try {
                spq1.setParameter(99, utilDate, TemporalType.DATE);
                logErr( "Did not throw IllegalArgumentException");
            } catch (IllegalArgumentException iae) {
                logTrace( "Received expected IllegalArgumentException");
                pass1 = true;
            } catch (Exception e) {
                logErr( "Caught unexpected exception", e);
            }

            logMsg( "Testing Query object with incorrect position specified");
            StoredProcedureQuery spq3 = getEntityManager().createStoredProcedureQuery("GetEmpIdUsingHireDateFromOut");
            spq3.registerStoredProcedureParameter(1, Date.class, ParameterMode.IN);
            Query q1 = spq3.setParameter(1, getUtilDate());
            try {
                q1.setParameter(99, utilDate, TemporalType.DATE);
                logErr( "Did not throw IllegalArgumentException");
            } catch (IllegalArgumentException iae) {
                logTrace( "Received expected IllegalArgumentException");
                pass2 = true;
            } catch (Exception e) {
                logErr( "Caught unexpected exception", e);
            }

            if (getEntityTransaction().isActive()) {
                getEntityTransaction().rollback();
            }
        } catch (Exception e) {
            logErr( "Caught exception", e);
        }

        if (!pass1 || !pass2)
            throw new Fault("setParameterIntDateTemporalTypeIllegalArgumentExceptionTest failed");
    }

    /*
     * @testName: setParameterParameterDateTemporalTypeTest
     *
     * @assertion_ids: PERSISTENCE:JAVADOC:1566;
     *
     * @test_Strategy:
     */
        public void setParameterParameterDateTemporalTypeTest() throws Exception {
        boolean pass2 = false;
        boolean pass3 = false;
        boolean pass5 = false;
        try {
            getEntityTransaction().begin();

            StoredProcedureQuery spq = getEntityManager().createStoredProcedureQuery("GetEmpIdUsingHireDateFromOut");
            spq.registerStoredProcedureParameter(1, Date.class, ParameterMode.IN);
            spq.registerStoredProcedureParameter(2, Integer.class, ParameterMode.OUT);
            spq.setParameter(1, getUtilDate(), TemporalType.DATE);

            Parameter p = spq.getParameter(1);
            spq.setParameter(p, utilDate, TemporalType.DATE);

            if (!spq.execute()) {
                Object o = spq.getOutputParameterValue(2);
                if (o instanceof Integer) {
                    int actual = (Integer) o;
                    if (actual == 1) {
                        logTrace( "Received expected id:" + actual);
                        pass2 = true;
                    } else {
                        logErr( "Expected id: 1, actual:" + actual);
                    }

                } else {
                    logErr( "Did not get instance of Integer back:" + o);
                }
            } else {
                logErr( "Expected execute() to return false, actual: true");
            }
            getEntityTransaction().commit();

        } catch (Exception e) {
            logErr( "Caught exception: ", e);
            if (getEntityTransaction().isActive()) {
                getEntityTransaction().rollback();
            }
        }
        try {
            getEntityTransaction().begin();

            logMsg( "Testing Query object");
            StoredProcedureQuery spq1 = getEntityManager().createStoredProcedureQuery("GetEmpIdUsingHireDateFromOut");
            spq1.registerStoredProcedureParameter(1, Date.class, ParameterMode.IN);
            spq1.registerStoredProcedureParameter(2, Integer.class, ParameterMode.OUT);
            spq1.setParameter(1, getCalDate().getTime(), TemporalType.DATE);

            Query q = spq1;

            // override the previously set parameter
            Parameter p = q.getParameter(1);
            q.setParameter(p, utilDate, TemporalType.DATE);

            Parameter p2 = q.getParameter(1);
            if (p.getPosition().equals(p2.getPosition()) && p.getParameterType().equals(p2.getParameterType())) {
                logTrace( "Received expected parameter");
                pass3 = true;
            } else {
                logErr( "Expected parameter:" + p + ", actual:" + p2);
            }
            StoredProcedureQuery spq2 = (StoredProcedureQuery) q;
            if (!spq2.execute()) {

                Object o = spq2.getOutputParameterValue(2);
                if (o instanceof Integer) {
                    int actual = (Integer) o;
                    if (actual == emp0.getId()) {
                        logTrace( "Received expected id:" + actual);
                        pass5 = true;
                    } else {
                        logErr( "Expected id: 1, actual:" + actual);
                    }

                } else {
                    logErr( "Did not get instance of Integer back:" + o);
                }
            } else {
                logErr( "Expected execute() to return false, actual: true");
            }
            getEntityTransaction().commit();

        } catch (Exception e) {
            logErr( "Caught exception: ", e);
            if (getEntityTransaction().isActive()) {
                getEntityTransaction().rollback();
            }
        }

        if (!pass2 || !pass3 || !pass5)
            throw new Fault("setParameterParameterDateTemporalTypeTest failed");
    }

    /*
     * @testName: setParameterParameterDateTemporalTypeIllegalArgumentExceptionTest
     *
     * @assertion_ids: PERSISTENCE:JAVADOC:1567;
     *
     * @test_Strategy:
     */
        public void setParameterParameterDateTemporalTypeIllegalArgumentExceptionTest() throws Exception {
        boolean pass1 = false;
        boolean pass2 = false;
        try {
            getEntityTransaction().begin();
            logTrace( "Get parameter from other stored procedure");
            StoredProcedureQuery spq = getEntityManager().createStoredProcedureQuery("GetEmpLastNameFromInOut");
            spq.registerStoredProcedureParameter(1, String.class, ParameterMode.INOUT);
            spq.setParameter(1, "INOUT");
            // Parameter to be used in next StoredProcedure
            Parameter p = spq.getParameter(1);
            logMsg( "Testing StoredProcedureQuery with parameter specified from another query");
            StoredProcedureQuery spq1 = getEntityManager().createStoredProcedureQuery("GetEmpIdUsingHireDateFromOut");
            spq1.registerStoredProcedureParameter(1, Date.class, ParameterMode.IN);
            spq1.setParameter(1, getUtilDate(), TemporalType.DATE);
            try {
                spq1.setParameter(p, getUtilDate(), TemporalType.DATE);
                logErr( "Did not throw IllegalArgumentException");
            } catch (IllegalArgumentException iae) {
                logTrace( "Received expected IllegalArgumentException");
                pass1 = true;
            } catch (Exception e) {
                logErr( "Caught unexpected exception", e);
            }

            logMsg( "Testing Query object with parameter specified from another query");
            StoredProcedureQuery spq3 = getEntityManager().createStoredProcedureQuery("GetEmpIdUsingHireDateFromOut");
            spq3.registerStoredProcedureParameter(1, Calendar.class, ParameterMode.IN);
            Query q1 = spq3.setParameter(1, getCalDate());
            try {
                q1.setParameter(p, getCalDate(), TemporalType.DATE);
                logErr( "Did not throw IllegalArgumentException");
            } catch (IllegalArgumentException iae) {
                logTrace( "Received expected IllegalArgumentException");
                pass2 = true;
            } catch (Exception e) {
                logErr( "Caught unexpected exception", e);
            }

            if (getEntityTransaction().isActive()) {
                getEntityTransaction().rollback();
            }

        } catch (Exception e) {
            logErr( "Caught exception", e);
        }

        if (!pass1 || !pass2)
            throw new Fault("setParameterParameterDateTemporalTypeIllegalArgumentExceptionTest failed");
    }

    /*
     * @testName: executeUpdateOfAnUpdateTest
     *
     * @assertion_ids: PERSISTENCE:JAVADOC:1580; PERSISTENCE:JAVADOC:1551;
     * PERSISTENCE:SPEC:1516; PERSISTENCE:SPEC:1580;
     *
     * @test_Strategy:
     */
        public void executeUpdateOfAnUpdateTest() throws Exception {
        boolean pass = false;
        try {
            getEntityTransaction().begin();
            StoredProcedureQuery spq = getEntityManager().createStoredProcedureQuery("UpdateEmpSalaryColumn");

            spq.executeUpdate();
            int updateCount = spq.getUpdateCount();
            if (updateCount == -1) {
                logTrace( "Received expected update count:" + updateCount);
                pass = true;
            } else {
                logErr( "Expected update count: -1, actual:" + updateCount);
            }
            getEntityTransaction().commit();

        } catch (Exception e) {
            logErr( "Unexpected exception occurred", e);
        }

        if (!pass)
            throw new Fault("executeUpdateOfAnUpdateTest failed");
    }

    /*
     * @testName: executeUpdateOfADeleteTest
     *
     * @assertion_ids: PERSISTENCE:JAVADOC:1580; PERSISTENCE:JAVADOC:1551;
     * PERSISTENCE:SPEC:1516;
     *
     * @test_Strategy:
     */
        public void executeUpdateOfADeleteTest() throws Exception {
        boolean pass = false;
        try {
            getEntityTransaction().begin();
            StoredProcedureQuery spq = getEntityManager().createStoredProcedureQuery("DeleteAllEmp");
            spq.executeUpdate();
            int updateCount = spq.getUpdateCount();
            if (updateCount == -1) {
                logTrace( "Received expected update count:" + updateCount);
                pass = true;
            } else {
                logErr( "Expected update count: -1, actual:" + updateCount);
            }
            getEntityTransaction().commit();

        } catch (Exception e) {
            logErr( "Unexpected exception occurred", e);
        }

        if (!pass)
            throw new Fault("executeUpdateOfADeleteTest failed");
    }

    /*
     * @testName: executeUpdateTransactionRequiredExceptionTest
     *
     * @assertion_ids: PERSISTENCE:JAVADOC:1582;
     *
     * @test_Strategy:
     */
        public void executeUpdateTransactionRequiredExceptionTest() throws Exception {
        boolean pass = false;
        try {
            StoredProcedureQuery spq = getEntityManager().createStoredProcedureQuery("DeleteAllEmp");
            try {
                logMsg( "IsActive returns:" + getEntityTransaction().isActive());
                spq.executeUpdate();
                logErr( "Did not throw TransactionRequiredException");
            } catch (TransactionRequiredException tre) {
                logTrace( "Received expected TransactionRequiredException");
                pass = true;
            } catch (Exception e) {
                logErr( "Caught unexpected exception", e);
            }

        } catch (Exception e) {
            logErr( "Unexpected exception occurred", e);
        }

        if (!pass)
            throw new Fault("executeUpdateTransactionRequiredExceptionTest failed");
    }

    /*
     * @testName: getParameterValueParameterTest
     *
     * @assertion_ids: PERSISTENCE:JAVADOC:1605; PERSISTENCE:JAVADOC:1630;
     *
     * @test_Strategy:
     */
        public void getParameterValueParameterTest() throws Exception {
        boolean pass1 = false;
        boolean pass3 = false;
        try {
            getEntityTransaction().begin();
            StoredProcedureQuery spq = getEntityManager().createStoredProcedureQuery("GetEmpFirstNameFromOut");
            spq.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
            spq.registerStoredProcedureParameter(2, String.class, ParameterMode.OUT);
            spq.setParameter(1, 1);
            Parameter p = spq.getParameter(1);
            boolean b = spq.isBound(p);
            if (b == true) {
                logTrace( "Received expected from isBound():" + b);
                pass1 = true;
            } else {
                logErr( "Expected result from isBound():true, actual:" + b);
            }
            if (!spq.execute()) {
                p = spq.getParameter(1);
                Object o = spq.getParameterValue(p);
                if (o instanceof Integer) {
                    logTrace( "Received expected parameter type: Integer");
                    Integer i = (Integer) o;
                    if (i.equals(emp0.getId())) {
                        logTrace( "Received expected parameter value:" + i);
                        pass3 = true;
                    } else {
                        logErr(
                                "Expected getParameterValue() result: " + emp0.getId() + ", actual:" + i);
                    }

                } else {
                    logErr( "Did not get instance of Integer from getParameterValue():" + o);
                }

            } else {
                logErr( "Expected execute() to return false, actual: true");
            }
            getEntityTransaction().commit();

        } catch (Exception e) {
            logErr( "Unexpected exception occurred", e);
        }

        if (!pass1 || !pass3)
            throw new Fault("getParameterValueParameterTest failed");

    }

    /*
     * @testName: getParameterValueParameterIllegalArgumentExceptionTest
     *
     * @assertion_ids: PERSISTENCE:JAVADOC:1606;
     *
     * @test_Strategy: execute getParameterValue using parameter from different
     * query
     */
        public void getParameterValueParameterIllegalArgumentExceptionTest() throws Exception {
        boolean pass = false;
        try {
            getEntityTransaction().begin();

            StoredProcedureQuery spq = getEntityManager().createStoredProcedureQuery("GetEmpLastNameFromInOut");
            spq.registerStoredProcedureParameter(1, String.class, ParameterMode.INOUT);
            spq.setParameter(1, "1");
            Parameter p = spq.getParameter(1);

            StoredProcedureQuery spq1 = getEntityManager().createStoredProcedureQuery("GetEmpFirstNameFromOut");
            spq1.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
            spq1.registerStoredProcedureParameter(2, String.class, ParameterMode.OUT);
            spq1.setParameter(1, 1);
            try {
                spq1.getParameterValue(p);
                logErr( "Did not throw IllegalArgumentException");
            } catch (IllegalArgumentException iae) {
                logTrace( "Received expected IllegalArgumentException");
                pass = true;
            } catch (Exception e) {
                logErr( "Caught unexpected exception", e);
            }
            if (getEntityTransaction().isActive()) {
                getEntityTransaction().rollback();
            }
        } catch (Exception e) {
            logErr( "Unexpected exception occurred", e);
        }

        if (!pass)
            throw new Fault("getParameterValueParameterIllegalArgumentExceptionTest failed");

    }

    /*
     * @testName: getParameterValueParameterIllegalStateExceptionTest
     *
     * @assertion_ids: PERSISTENCE:JAVADOC:1607; PERSISTENCE:JAVADOC:1630;
     *
     * @test_Strategy:
     */
        public void getParameterValueParameterIllegalStateExceptionTest() throws Exception {
        boolean pass1 = false;
        boolean pass2 = false;
        try {
            getEntityTransaction().begin();
            StoredProcedureQuery spq = getEntityManager().createStoredProcedureQuery("GetEmpFirstNameFromOut");
            spq.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
            spq.registerStoredProcedureParameter(2, String.class, ParameterMode.OUT);
            spq.setParameter(1, 1);
            Parameter p = spq.getParameter(1);
            boolean b = spq.isBound(p);
            if (b == true) {
                logTrace( "Received expected from isBound():" + b);
                pass1 = true;
            } else {
                logErr( "Expected result from isBound():true, actual:" + b);
            }
            Parameter p2 = spq.getParameter(2);

            try {
                spq.getParameterValue(p2);
                logErr( "Did not throw IllegalStateException");
            } catch (IllegalStateException iae) {
                logTrace( "Received expected IllegalStateException");
                pass2 = true;
            } catch (Exception e) {
                logErr( "Caught unexpected exception", e);
            }
            if (getEntityTransaction().isActive()) {
                getEntityTransaction().rollback();
            }
        } catch (Exception e) {
            logErr( "Unexpected exception occurred", e);
        }

        if (!pass1 || !pass2)
            throw new Fault("getParameterValueParameterIllegalStateExceptionTest failed");

    }

    /*
     * @testName: getParameterValueIntTest
     *
     * @assertion_ids: PERSISTENCE:JAVADOC:1611; PERSISTENCE:SPEC:1572;
     *
     * @test_Strategy:
     */
        public void getParameterValueIntTest() throws Exception {
        boolean pass1 = false;
        boolean pass2 = false;
        boolean pass3 = false;
        boolean pass4 = false;
        try {
            getEntityTransaction().begin();

            logMsg( "Testing createStoredProcedureQuery");
            StoredProcedureQuery spq = getEntityManager().createStoredProcedureQuery("GetEmpLastNameFromInOut");
            spq.registerStoredProcedureParameter(1, String.class, ParameterMode.INOUT);
            spq.setParameter(1, "1");
            Object o = spq.getParameterValue(1);
            if (o instanceof String) {
                logTrace( "Received expected parameter type: String");
                String s = (String) o;
                if (s.equals("1")) {
                    logTrace( "Received expected parameter value:" + s);
                    pass1 = true;
                } else {
                    logErr( "Expected getParameterValue() result: 1, actual:" + s);
                }

            } else {
                logErr( "Did not get instance of String from getParameterValue():" + o);
            }

            if (!spq.execute()) {
                o = spq.getParameterValue(1);
                if (o instanceof String) {
                    logTrace( "Received expected parameter type: String");
                    String s = (String) o;
                    if (s.equals("1")) {
                        logTrace( "Received expected parameter value:" + s);
                        pass2 = true;
                    } else {
                        logErr( "Expected getParameterValue() result: 1, actual:" + s);
                    }
                }
            } else {
                logErr( "Expected execute() to return false, actual: true");
            }
            getEntityTransaction().commit();
        } catch (Exception e) {
            logErr( "Unexpected exception occurred", e);
            if (getEntityTransaction().isActive()) {
                getEntityTransaction().rollback();
            }
        }
        try {
            getEntityTransaction().begin();

            logMsg( "Testing createNamedStoredProcedureQuery");
            StoredProcedureQuery spq = getEntityManager().createNamedStoredProcedureQuery("getemplastnamefrominout");
            spq.setParameter(1, "1");
            Object o = spq.getParameterValue(1);
            if (o instanceof String) {
                logTrace( "Received expected parameter type: String");
                String s = (String) o;
                if (s.equals("1")) {
                    logTrace( "Received expected parameter value:" + s);
                    pass3 = true;
                } else {
                    logErr( "Expected getParameterValue() result: 1, actual:" + s);
                }

            } else {
                logErr( "Did not get instance of String from getParameterValue():" + o);
            }

            if (!spq.execute()) {
                o = spq.getParameterValue(1);
                if (o instanceof String) {
                    logTrace( "Received expected parameter type: String");
                    String s = (String) o;
                    if (s.equals("1")) {
                        logTrace( "Received expected parameter value:" + s);
                        pass4 = true;
                    } else {
                        logErr( "Expected getParameterValue() result: 1, actual:" + s);
                    }
                }
            } else {
                logErr( "Expected execute() to return false, actual: true");
            }
            getEntityTransaction().commit();
        } catch (Exception e) {
            logErr( "Unexpected exception occurred", e);
            if (getEntityTransaction().isActive()) {
                getEntityTransaction().rollback();
            }
        }

        if (!pass1 || !pass2 || !pass3 || !pass4)
            throw new Fault("getParameterValueIntTest failed");

    }

    /*
     * @testName: getParameterValueIntIllegalArgumentExceptionTest
     *
     * @assertion_ids: PERSISTENCE:JAVADOC:1613;
     *
     * @test_Strategy:
     */
        public void getParameterValueIntIllegalArgumentExceptionTest() throws Exception {
        boolean pass = false;
        try {
            getEntityTransaction().begin();

            StoredProcedureQuery spq = getEntityManager().createStoredProcedureQuery("GetEmpFirstNameFromOut");
            spq.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
            spq.registerStoredProcedureParameter(2, String.class, ParameterMode.OUT);
            spq.setParameter(1, 1);
            try {
                spq.getParameterValue(99);
                logErr( "Did not throw IllegalArgumentException");
            } catch (IllegalArgumentException iae) {
                logTrace( "Received expected IllegalArgumentException");
                pass = true;
            } catch (Exception e) {
                logErr( "Caught unexpected exception", e);
            }
            if (getEntityTransaction().isActive()) {
                getEntityTransaction().rollback();
            }
        } catch (Exception e) {
            logErr( "Unexpected exception occurred", e);
        }

        if (!pass)
            throw new Fault("getParameterValueIntIllegalArgumentExceptionTest failed");

    }

    /*
     * @testName: getParameterValueIntIllegalStateExceptionTest
     *
     * @assertion_ids: PERSISTENCE:JAVADOC:1612;
     *
     * @test_Strategy:
     */
        public void getParameterValueIntIllegalStateExceptionTest() throws Exception {
        boolean pass = false;
        try {
            getEntityTransaction().begin();
            StoredProcedureQuery spq = getEntityManager().createStoredProcedureQuery("GetEmpFirstNameFromOut");
            spq.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
            spq.registerStoredProcedureParameter(2, String.class, ParameterMode.OUT);
            spq.setParameter(1, 1);
            try {
                spq.getParameterValue(2);
                logErr( "Did not throw IllegalStateException");
            } catch (IllegalStateException iae) {
                logTrace( "Received expected IllegalStateException");
                pass = true;
            } catch (Exception e) {
                logErr( "Caught unexpected exception", e);
            }
            if (getEntityTransaction().isActive()) {
                getEntityTransaction().rollback();
            }
        } catch (Exception e) {
            logErr( "Unexpected exception occurred", e);
        }

        if (!pass)
            throw new Fault("getParameterValueIntIllegalStateExceptionTest failed");

    }

    /*
     * @testName: setHintStringObjectTest
     *
     * @assertion_ids: PERSISTENCE:JAVADOC:1560;
     *
     * @test_Strategy: Vendor-specific hints that are not recognized by a provider
     * must be silently ignored.
     */
        public void setHintStringObjectTest() throws Exception {
        boolean pass = false;
        try {
            getEntityTransaction().begin();
            StoredProcedureQuery spq = getEntityManager().createStoredProcedureQuery("GetEmpFirstNameFromOut");
            spq.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
            spq.registerStoredProcedureParameter(2, String.class, ParameterMode.OUT);
            spq.setParameter(1, 1);
            spq.getParameterValue(1);
            spq.setHint("property.that.is.not.recognized", "property.that.is.not.recognized");
            spq.execute();
            getEntityTransaction().commit();
            pass = true;
        } catch (Exception e) {
            logErr( "Unexpected exception occurred", e);
        }

        if (!pass)
            throw new Fault("setHintStringObjectTest failed");

    }

    /*
     * @testName: xmlOverridesNamedStoredProcedureQueryTest
     *
     * @assertion_ids: PERSISTENCE:SPEC:2239; PERSISTENCE:SPEC:2241;
     * PERSISTENCE:SPEC:2242;
     *
     * @test_Strategy: verify xml overrides NamedStoredProcedureQuery annotation
     */
        public void xmlOverridesNamedStoredProcedureQueryTest() throws Exception {
        boolean pass1 = false;
        boolean pass2 = false;

        try {

            StoredProcedureQuery spq = getEntityManager().createNamedStoredProcedureQuery("tobeoverridden1");
            spq.setParameter(1, 1);
            Object o = spq.getParameterValue(1);
            if (o instanceof Integer) {
                logTrace( "Received expected parameter type: Integer");
                Integer i = (Integer) o;
                if (i.equals(1)) {
                    logTrace( "Received expected parameter value:" + i);
                    pass1 = true;
                } else {
                    logErr( "Expected getParameterValue() result: 1, actual:" + i);
                }

            } else {
                logErr( "Did not get instance of Integer from getParameterValue():" + o);
            }

            if (!spq.execute()) {
                o = spq.getOutputParameterValue(2);
                if (o instanceof String) {
                    logTrace( "Received expected parameter type: String");
                    String s = (String) o;
                    if (s.equals(emp0.getFirstName())) {
                        logTrace( "Received expected parameter value:" + s);
                        pass2 = true;
                    } else {
                        logErr(
                                "Expected getParameterValue() result: " + emp0.getFirstName() + ", actual:" + s);
                    }
                }
            } else {
                logErr( "Expected execute() to return false, actual: true");
            }
        } catch (Exception e) {
            logErr( "Unexpected exception occurred", e);
        }

        if (!pass1 || !pass2)
            throw new Fault("xmlOverridesNamedStoredProcedureQueryTest failed");

    }

    /*
     * @testName: xmlOverridesSqlResultSetMappingAnnotationTest
     *
     * @assertion_ids: PERSISTENCE:SPEC:2243; PERSISTENCE:SPEC:2245;
     * PERSISTENCE:SPEC:2246;
     *
     * @test_Strategy: verify xml overrides SqlResultSetMapping annotation
     *
     */
        public void xmlOverridesSqlResultSetMappingAnnotationTest() throws Exception {
        boolean pass = false;
        try {
            getEntityTransaction().begin();
            try {
                logMsg( "Testing using name,result mapping");
                clearCache();
                StoredProcedureQuery spq = getEntityManager().createStoredProcedureQuery("GetEmpIdFNameLNameFromRS",
                        "tobeoverridden2");
                spq.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
                if (dataBaseName.equalsIgnoreCase(ORACLE) || dataBaseName.equalsIgnoreCase(POSTGRESQL)) {
                    logTrace( "register refcursor parameter");
                    spq.registerStoredProcedureParameter(2, void.class, ParameterMode.REF_CURSOR);
                }
                spq.setParameter(1, 1);
                if (spq.execute()) {

                    List<List> listOfList = getResultSetsFromStoredProcedure(spq);
                    if (listOfList.size() == 1) {
                        List<Employee> expected = new ArrayList<Employee>();
                        expected.add(new Employee(emp0.getId(), emp0.getFirstName(), emp0.getLastName()));
                        pass = verifyListOfListEmployees(expected, listOfList);
                    } else {
                        logErr(
                                "Did not get the correct number of result sets returned, expected: 1, actual:"
                                        + listOfList.size());
                    }
                } else {
                    logErr( "Expected execute() to return true, actual: false");
                }
            } catch (Exception ex) {
                logErr( "Received unexpected exception:", ex);
            }

            getEntityTransaction().commit();

        } catch (Exception ex) {
            logErr( "Received unexpected exception:", ex);
        }

        if (!pass) {
            throw new Fault("xmlOverridesSqlResultSetMappingAnnotationTest failed");
        }

    }

    private void createEmployeeTestData() {

        try {
            getEntityTransaction().begin();

            logMsg( "Creating Employees");

            final Date d2 = getUtilDate("2001-06-27");
            final Date d3 = getUtilDate("2002-07-07");
            final Date d4 = getUtilDate("2003-03-03");
            final Date d5 = getUtilDate();

            emp0 = new Employee(1, "Alan", "Frechette", utilDate, (float) 35000.0);
            empRef.add(emp0);
            empRef.add(new Employee(2, "Arthur", "Frechette", d2, (float) 35000.0));
            empRef.add(new Employee(3, "Shelly", "McGowan", d3, (float) 50000.0));
            empRef.add(new Employee(4, "Robert", "Bissett", d4, (float) 55000.0));
            empRef.add(new Employee(5, "Stephen", "DMilla", d5, (float) 25000.0));
            for (Employee e : empRef) {
                if (e != null) {
                    getEntityManager().persist(e);
                    logTrace( "persisted employee:" + e);
                }
            }

            getEntityManager().flush();
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

}
