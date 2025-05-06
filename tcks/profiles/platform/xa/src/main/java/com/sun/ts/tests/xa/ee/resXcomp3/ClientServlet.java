/*
 * Copyright (c) 2007, 2024 Oracle and/or its affiliates. All rights reserved.
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

/*
 * @(#)Client.java	1.20 02/07/19
 */
package com.sun.ts.tests.xa.ee.resXcomp3;

import java.io.IOException;
import java.io.Serializable;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OverProtocol;
import org.jboss.arquillian.container.test.api.TargetsContainer;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.sun.ts.lib.harness.Status;

import tck.arquillian.porting.lib.spi.TestArchiveProcessor;
import tck.arquillian.protocol.common.TargetVehicle;

@Tag("tck-javatest")
public class ClientServlet extends Client implements Serializable {

    @TargetsContainer("tck-javatest")
    @OverProtocol("javatest")
    @Deployment(name = "servlet", testable = true)
    public static EnterpriseArchive createDeploymentServlet(@ArquillianResource TestArchiveProcessor archiveProcessor) throws IOException {
        WebArchive servletVehicleArchive = ShrinkWrap.create(WebArchive.class, "xa_resXcomp3_servlet_vehicle_web.war");
        servletVehicleArchive.addPackages(false, "com.sun.ts.tests.common.vehicle");
        servletVehicleArchive.addPackages(false, "com.sun.ts.tests.common.vehicle.servlet");
        servletVehicleArchive.addPackages(true, "com.sun.ts.lib.harness");
        servletVehicleArchive.addClasses(ClientServlet.class, Client.class);
        servletVehicleArchive.addAsWebInfResource(ClientServlet.class.getPackage(), "servlet_vehicle_web.xml", "web.xml");
        servletVehicleArchive.addAsWebInfResource(ClientServlet.class.getPackage(), "xa_resXcomp3_servlet_vehicle_web.war.sun-web.xml",
                "sun-web.xml");

        JavaArchive javaAchive = ShrinkWrap.create(JavaArchive.class, "xa_resXcomp3_ee_txpropagate2_ejb.jar");
        javaAchive.addPackages(false, "com.sun.ts.tests.common.util");
        javaAchive.addPackages(false, "com.sun.ts.tests.common.whitebox");
        javaAchive.addPackages(true, "com.sun.ts.lib.harness");
        javaAchive.addClasses(ClientServlet.class, Client.class);
        javaAchive.addClasses(Ejb1Test.class, Ejb1TestEJB.class, Ejb2Test.class, Ejb2TestEJB.class);
        javaAchive.addAsManifestResource(ClientServlet.class.getPackage(), "xa_resXcomp3_ee_txpropagate2_ejb.xml", "ejb-jar.xml");
        javaAchive.addAsManifestResource(ClientServlet.class.getPackage(), "xa_resXcomp3_ee_txpropagate2_ejb.jar.sun-ejb-jar.xml",
                "sun-ejb-jar.xml");

        EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, "xa_resXcomp3_servlet_vehicle.ear");
        ear.addAsModule(servletVehicleArchive);
        ear.addAsModule(javaAchive);

        return ear;

    };

    /* Run test in standalone mode */
    public static void main(String[] args) {
        ClientServlet theTests = new ClientServlet();
        Status s = theTests.run(args, System.out, System.err);
        s.exit();
    }

    /* Run tests */
    /*
     * @testName: test9
     *
     * @assertion_ids: JavaEE:SPEC:74; JavaEE:SPEC:73; JavaEE:SPEC:82; JavaEE:SPEC:84
     *
     * @test_Strategy: Contact a Servlet, EJB or JSP. Obtain the UserTransaction interface. Perform global transactions
     * using the Ejb1Test (deployed as TX_REQUIRED) to a single RDBMS table.
     * 
     * Insert/Delete followed by a commit to a single table.
     *
     * Database Access is performed from Ejb1Test EJB. CLIENT: tx_start, EJB1: Insert, EJB2: Insert, tx_commit
     */
    @Test
    @TargetVehicle("servlet")
    public void test9() throws Exception {
        super.test9();
    }

    /*
     * @testName: test10
     *
     * @assertion_ids: JavaEE:SPEC:74; JavaEE:SPEC:73; JavaEE:SPEC:82; JavaEE:SPEC:84
     *
     * @test_Strategy: Contact a Servlet, EJB or JSP. Obtain the UserTransaction interface. Perform global transactions
     * using the Ejb1Test (deployed as TX_REQUIRED) to a single RDBMS table.
     * 
     * Insert/Delete followed by a rollback to a single table.
     *
     * Database Access is performed from Ejb1Test EJB. CLIENT: tx_start, EJB1: Insert, EJB2: Insert, tx_rollback
     */
    @Test
    @TargetVehicle("servlet")
    public void test10() throws Exception {
        super.test10();
    }

    /*
     * @testName: test11
     *
     * @assertion_ids: JavaEE:SPEC:74; JavaEE:SPEC:73; JavaEE:SPEC:82; JavaEE:SPEC:84
     *
     * @test_Strategy: Contact a Servlet, EJB or JSP. Obtain the UserTransaction interface. Perform global transactions
     * using the Ejb1Test (deployed as TX_REQUIRED) to a single RDBMS table.
     * 
     * Insert/Delete followed by a commit to a single table.
     *
     * Database Access is performed from Ejb1Test EJB. CLIENT: tx_start, EJB1: Insert, EJB2: Insert, tx_commit
     */
    @Test
    @TargetVehicle("servlet")
    public void test11() throws Exception {
        super.test11();
    }

    /*
     * @testName: test12
     *
     * @assertion_ids: JavaEE:SPEC:74; JavaEE:SPEC:73; JavaEE:SPEC:82; JavaEE:SPEC:84
     *
     * @test_Strategy: Contact a Servlet, EJB or JSP. Obtain the UserTransaction interface. Perform global transactions
     * using the Ejb1Test (deployed as TX_REQUIRED) to a single RDBMS table.
     * 
     * Insert/Delete followed by a rollback to a single table.
     *
     * Database Access is performed from Ejb1Test EJB. CLIENT: tx_start, EJB1: Insert, EJB2: Insert, tx_rollback
     */
    @Test
    @TargetVehicle("servlet")
    public void test12() throws Exception {
        super.test12();
    }

    /*
     * @testName: test13
     *
     * @assertion_ids: JavaEE:SPEC:74; JavaEE:SPEC:73; JavaEE:SPEC:82; JavaEE:SPEC:84
     *
     * @test_Strategy: Contact a Servlet, EJB or JSP. Obtain the UserTransaction interface. Perform global transactions
     * using the Ejb1Test (deployed as TX_REQUIRED) to a single RDBMS table.
     * 
     * Insert/Delete followed by a commit to a single table.
     *
     * Database Access is performed from Ejb1Test EJB. CLIENT: tx_start, EJB1: Insert, EJB2: Insert, tx_commit
     */
    @Test
    @TargetVehicle("servlet")
    public void test13() throws Exception {
        super.test13();
    }
}
