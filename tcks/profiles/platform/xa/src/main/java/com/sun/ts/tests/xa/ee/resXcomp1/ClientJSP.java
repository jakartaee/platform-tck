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
 * @(#)Client.java
 */

package com.sun.ts.tests.xa.ee.resXcomp1;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OverProtocol;
import org.jboss.arquillian.container.test.api.TargetsContainer;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.ByteArrayAsset;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.sun.ts.lib.harness.Status;

import tck.arquillian.porting.lib.spi.TestArchiveProcessor;
import tck.arquillian.protocol.common.TargetVehicle;

@Tag("tck-javatest")
public class ClientJSP extends Client implements Serializable {

    @TargetsContainer("tck-javatest")
    @OverProtocol("javatest")
    @Deployment(name = "jsp", testable = true)
    public static EnterpriseArchive createDeploymentJSP(@ArquillianResource TestArchiveProcessor archiveProcessor) throws IOException {
        WebArchive jspVehicleArchive = ShrinkWrap.create(WebArchive.class, "xa_resXcomp1_jsp_vehicle_web.war");
        jspVehicleArchive.addPackages(false, "com.sun.ts.tests.common.vehicle");
        jspVehicleArchive.addPackages(false, "com.sun.ts.tests.common.vehicle.jsp");
        jspVehicleArchive.addPackages(true, "com.sun.ts.lib.harness");
        jspVehicleArchive.addClasses(ClientServletTest.class, Client.class);
        InputStream jspVehicle = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("com/sun/ts/tests/common/vehicle/jsp/contentRoot/jsp_vehicle.jsp");
        jspVehicleArchive.add(new ByteArrayAsset(jspVehicle), "jsp_vehicle.jsp");
        InputStream clientHtml = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("com/sun/ts/tests/common/vehicle/jsp/contentRoot/client.html");
        jspVehicleArchive.add(new ByteArrayAsset(clientHtml), "client.html");

        jspVehicleArchive.addAsWebInfResource(ClientJSP.class.getPackage(), "jsp_vehicle_web.xml", "web.xml");
        jspVehicleArchive.addAsWebInfResource(ClientJSP.class.getPackage(), "xa_resXcomp1_jsp_vehicle_web.war.sun-web.xml", "sun-web.xml");

        JavaArchive javaAchive = ShrinkWrap.create(JavaArchive.class, "xa_resXcomp1_ee_txpropagate3_ejb.jar");
        javaAchive.addPackages(false, "com.sun.ts.tests.common.util");
        javaAchive.addPackages(false, "com.sun.ts.tests.common.whitebox");
        javaAchive.addPackages(true, "com.sun.ts.lib.harness");
        javaAchive.addClasses(TxBean.class, TxBeanEJB.class);
        javaAchive.addClasses(ClientServletTest.class, Client.class);

        javaAchive.addAsManifestResource(ClientJSP.class.getPackage(), "xa_resXcomp1_ee_txpropagate3_ejb.xml", "ejb-jar.xml");
        javaAchive.addAsManifestResource(ClientJSP.class.getPackage(), "xa_resXcomp1_ee_txpropagate3_ejb.jar.sun-ejb-jar.xml",
                "sun-ejb-jar.xml");

        EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, "xa_resXcomp1_jsp_vehicle.ear");
        ear.addAsModule(jspVehicleArchive);
        ear.addAsModule(javaAchive);

        return ear;
    };

    public static void main(String[] args) {
        ClientJSP client = new ClientJSP();
        Status s = client.run(args, System.out, System.err);
        s.exit();
    }

    /* Run test */

    /*
     * @testName: test1
     *
     * @assertion_ids: JavaEE:SPEC:74; JavaEE:SPEC:68
     *
     * @test_Strategy: Contact a Servlet, EJB or JSP. Obtain the UserTransaction interface. Perform global transactions
     * using the TxBean (deployed as TX_REQUIRED) to a single RDBMS table.
     * 
     * Insert/Delete followed by a commit to a single table.
     *
     * Database Access is performed from TxBean EJB.
     *
     */
    @Test
    @TargetVehicle("jsp")
    public void test1() throws Exception {
        super.test1();
    }

    /*
     * @testName: test2
     *
     * @assertion_ids: JavaEE:SPEC:74; JavaEE:SPEC:68
     *
     * @test_Strategy: Contact a Servlet, EJB or JSP. Obtain the UserTransaction interface. Perform global transactions
     * using the TxBean (deployed as TX_REQUIRED) to a single RDBMS table.
     * 
     * Insert/Delete followed by a rollback to a single table.
     *
     * Database Access is performed from TxBean EJB.
     *
     */
    @Test
    @TargetVehicle("jsp")
    public void test2() throws Exception {
        super.test2();
    }

    /*
     * @testName: test3
     *
     * @assertion_ids: JavaEE:SPEC:74
     *
     * @test_Strategy: Contact a Servlet, EJB or JSP. Obtain the UserTransaction interface. Perform global transactions
     * using the TxBean (deployed as TX_REQUIRED) to a single RDBMS table.
     * 
     * Insert/Delete followed by a commit to a single table.
     *
     * Database Access is performed from TxBean EJB.
     *
     */
    @Test
    @TargetVehicle("jsp")
    public void test3() throws Exception {
        super.test3();
    }

    /*
     * @testName: test4
     *
     * @assertion_ids: JavaEE:SPEC:74
     *
     * @test_Strategy: Contact a Servlet, EJB or JSP. Obtain the UserTransaction interface. Perform global transactions
     * using the TxBean (deployed as TX_REQUIRED) to a single RDBMS table.
     * 
     * Insert/Delete followed by a rollback to a single table.
     *
     * Database Access is performed from TxBean EJB.
     *
     */
    @Test
    @TargetVehicle("jsp")
    public void test4() throws Exception {
        super.test4();
    }

}
