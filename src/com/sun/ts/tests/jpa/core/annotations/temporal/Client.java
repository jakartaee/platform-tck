/*
 * Copyright (c) 2008, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jpa.core.annotations.temporal;

import com.sun.javatest.Status;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jpa.common.PMClientBase;

import java.time.LocalDate;
import java.util.*;

public class Client extends PMClientBase {

    private static final long serialVersionUID = 21L;
    Date date = null;
    Date date2 = null;

    Calendar calendar = null;
    Calendar calendar2 = null;
    A_Field aFieldRef;
    A_Property aPropertyRef;
    A2_Field a2FieldRef;
    A2_Property a2PropertyRef;
    List<Date> expectedDates;
    List<Calendar> expectedCalendars;

    public Client() {
    }

    public static void main(String[] args) {
        Client theTests = new Client();
        Status s = theTests.run(args, System.out, System.err);
        s.exit();
    }

    public void setup(String[] args, Properties p) throws Fault {
        TestUtil.logTrace("setupData");
        try {
            super.setup(args, p);

            removeTestData();
            createTestData();

        } catch (Exception e) {
            throw new Fault("Setup failed:", e);
        }
    }

    /*
     * @testName: basicFieldTest
     * 
     * @assertion_ids:  PERSISTENCE:SPEC:2114; PERSISTENCE:SPEC:2114.1;
     * 
     * @test_Strategy:  Used with a Basic annotation for field access
     */
    public void basicFieldTest() throws Fault {

        boolean pass = false;
        try {
            A_Field a = getEntityManager().find(A_Field.class, "1");
            if (a != null) {
                if (aFieldRef.equals(a)) {
                    pass = true;
                } else {
                    TestUtil.logErr("Expected:" + aFieldRef.toString() + ", actual:" + a.toString());
                }
            } else {
                TestUtil.logErr("Find returned null result");
            }
        } catch (Exception e) {
            TestUtil.logErr("Unexpected exception occurred", e);
        }

        if (!pass) {
            throw new Fault("basicFieldTest failed");
        }

    }

    /*
     * @testName: basicPropertyTest
     *
     * @assertion_ids:  PERSISTENCE:SPEC:2114; PERSISTENCE:SPEC:2114.1;
     *
     * @test_Strategy:  Used with a Basic annotation for property access
     */
    public void basicPropertyTest() throws Fault {
        boolean pass = false;
        try {
            A_Property a = getEntityManager().find(A_Property.class, "2");
            if (a != null) {
                if (aPropertyRef.equals(a)) {
                    TestUtil.logTrace("Received expected entity:"+a.toString());
                    pass = true;
                } else {
                    TestUtil.logErr("Expected:" + aPropertyRef.toString() + ", actual:" + a.toString());
                }
            } else {
                TestUtil.logErr("Find returned null result");
            }

        } catch (Exception e) {
            TestUtil.logErr("Unexpected exception occurred", e);
        }

        if (!pass) {
            throw new Fault("basicPropertyTest failed");
        }

    }

    /*
    * @testName: fieldElementCollectionTemporalTest
    * @assertion_ids:  PERSISTENCE:SPEC:2114; PERSISTENCE:SPEC:2114.3;
    * @test_Strategy:   ElementCollection of a basic type
    */
    public void fieldElementCollectionTemporalTest() throws Fault {
        boolean pass = false;
        try {
            getEntityTransaction().begin();
            TestUtil.logTrace("find the previously persisted Customer and Country and verify them");
            A_Field a = getEntityManager().find(A_Field.class, "1");
            if (a != null) {
                TestUtil.logTrace("Found A: " + a.toString());
                if (a.getDates().containsAll(expectedDates) && expectedDates.containsAll(a.getDates()) && a.getDates().size() == expectedDates.size()) {
                    TestUtil.logTrace("Received expected Dates:");
                    for (Date d : a.getDates()) {
                        TestUtil.logTrace("date:" + d);
                    }
                    pass = true;
                } else {
                    TestUtil.logErr("Did not get expected results.");
                    for (Date d : expectedDates) {
                        TestUtil.logErr("expected:" + d);
                    }
                    TestUtil.logErr("actual:");
                    for (Date d : a.getDates()) {
                        TestUtil.logErr("actual:" + d);
                    }
                }
            } else {
                TestUtil.logErr("Find returned null A");
            }

            getEntityTransaction().commit();
        } catch (Exception e) {
            TestUtil.logErr("Unexpected exception occurred: ", e);
            pass = false;
        }
        if (!pass) {
            throw new Fault("fieldElementCollectionTemporalTest failed");
        }
    }

    /*
    * @testName: propertyElementCollectionTemporalTest
    * @assertion_ids:  PERSISTENCE:SPEC:2114; PERSISTENCE:SPEC:2114.3;
    * @test_Strategy:   ElementCollection of a basic type
    */
    public void propertyElementCollectionTemporalTest() throws Fault {
        boolean pass = false;
        try {
            getEntityTransaction().begin();
            TestUtil.logTrace("find the previously persisted Customer and Country and verify them");
            A_Property a = getEntityManager().find(A_Property.class, "2");
            if (a != null) {
                TestUtil.logTrace("Found A2: " + a.toString());
                if (a.getDates().containsAll(expectedCalendars) && expectedCalendars.containsAll(a.getDates()) && a.getDates().size() == expectedCalendars.size()) {
                    TestUtil.logTrace("Received expected Dates:");
                    for (Calendar d : a.getDates()) {
                        TestUtil.logTrace("date:" + d);
                    }
                    pass = true;
                } else {
                    TestUtil.logErr("Did not get expected results.");
                    for (Date d : expectedDates) {
                        TestUtil.logErr("expected:" + d);
                    }
                    TestUtil.logErr("actual:");
                    for (Calendar d : a.getDates()) {
                        TestUtil.logErr("actual:" + d);
                    }
                }
            } else {
                TestUtil.logErr("Find returned null A2");
            }

            getEntityTransaction().commit();
        } catch (Exception e) {
            TestUtil.logErr("Unexpected exception occurred: ", e);
            pass = false;
        }
        if (!pass) {
            throw new Fault("propertyElementCollectionTemporalTest failed");
        }
    }


    /*
     * @testName: idFieldTest
     *
     * @assertion_ids:  PERSISTENCE:SPEC:2114; PERSISTENCE:SPEC:2114.2;
     *
     * @test_Strategy:  Used with a id annotation for field access
     */
    public void idFieldTest() throws Fault {

        boolean pass = false;
        try {
            A2_Field a = getEntityManager().find(A2_Field.class, date2);
            if (a != null) {
                if (a2FieldRef.equals(a)) {
                    pass = true;
                } else {
                    TestUtil.logErr("Expected:" + a2FieldRef.toString() + ", actual:" + a.toString());
                }
            } else {
                TestUtil.logErr("Find returned null result");
            }
        } catch (Exception e) {
            TestUtil.logErr("Unexpected exception occurred", e);
        }

        if (!pass) {
            throw new Fault("idFieldTest failed");
        }

    }

    /*
     * @testName: idPropertyTest
     *
     * @assertion_ids:  PERSISTENCE:SPEC:2114; PERSISTENCE:SPEC:2114.2;
     *
     * @test_Strategy:  Used with a id annotation for property access
     */
    public void idPropertyTest() throws Fault {
        boolean pass = false;
        try {
            A2_Property a = getEntityManager().find(A2_Property.class, calendar2);
            if (a != null) {
                if (a2PropertyRef.equals(a)) {
                    TestUtil.logTrace("Received expected entity:"+a.toString());
                    pass = true;
                } else {
                    TestUtil.logErr("Expected:" + a2PropertyRef.toString() + ", actual:" + a.toString());
                }
            } else {
                TestUtil.logErr("Find returned null result");
            }

        } catch (Exception e) {
            TestUtil.logErr("Unexpected exception occurred", e);
        }

        if (!pass) {
            throw new Fault("idPropertyTest failed");
        }

    }


    public void cleanup() throws Fault {
        TestUtil.logTrace("cleanup");
        removeTestData();
        TestUtil.logTrace("cleanup complete, calling super.cleanup");
        super.cleanup();
    }

    public void createTestData() {
        TestUtil.logTrace("createTestData");

        try {
            getEntityTransaction().begin();

            date = getSQLDate();
            LocalDate localDate = ((java.sql.Date) date).toLocalDate();
            date2 = getUtilDate("2000-02-14");
            String sDate2 = "2000-02-14";

            calendar = Calendar.getInstance();
            calendar.clear();
            calendar.set(localDate.getYear(), localDate.getMonthValue(), localDate.getDayOfMonth());
            calendar2 = getCalDate(2001,06,27);
            String sCalendar2 = "2001-06-27";

            expectedDates = new ArrayList<Date>();
            expectedDates.add(getUtilDate("2000-02-14"));
            expectedDates.add(getUtilDate("2001-06-27"));
            expectedDates.add(getUtilDate("2002-07-07"));
            aFieldRef = new A_Field("1", date, calendar,expectedDates);

            expectedCalendars = new ArrayList<Calendar>();
            expectedCalendars.add(getCalDate(2000,02,14));
            expectedCalendars.add(getCalDate(2001,06,27));
            expectedCalendars.add(getCalDate(2002,07,07));
            aPropertyRef = new A_Property("2", date, calendar, expectedCalendars);
            getEntityManager().persist(aFieldRef);
            getEntityManager().persist(aPropertyRef);


            a2FieldRef = new A2_Field(date2,sDate2);
            a2PropertyRef = new A2_Property(calendar2, sCalendar2);

            getEntityManager().persist(a2FieldRef);
            getEntityManager().persist(a2PropertyRef);

            getEntityManager().flush();
            getEntityTransaction().commit();
        } catch (Exception e) {
            TestUtil.logErr("Unexpected Exception in createTestData:", e);
        } finally {
            try {
                if (getEntityTransaction().isActive()) {
                    getEntityTransaction().rollback();
                }
            } catch (Exception re) {
                TestUtil.logErr("Unexpected Exception during Rollback:", re);
            }
        }

    }

    private void removeTestData() {
        TestUtil.logTrace("removeTestData");
        if (getEntityTransaction().isActive()) {
            getEntityTransaction().rollback();
        }
        try {
            getEntityTransaction().begin();
            getEntityManager().createNativeQuery("DELETE FROM A_BASIC").executeUpdate();
            getEntityManager().createNativeQuery("DELETE FROM DATE_TABLE").executeUpdate();
            getEntityManager().createNativeQuery("DELETE FROM DATES_TABLE").executeUpdate();
            getEntityTransaction().commit();
        } catch (Exception e) {
            TestUtil.logErr("Exception encountered while removing entities:", e);
        } finally {
            try {
                if (getEntityTransaction().isActive()) {
                    getEntityTransaction().rollback();
                }
            } catch (Exception re) {
                TestUtil.logErr("Unexpected Exception in removeTestData:", re);
            }
        }
    }
}
