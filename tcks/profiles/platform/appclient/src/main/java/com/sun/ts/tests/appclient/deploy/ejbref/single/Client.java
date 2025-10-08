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
 * @(#)Client.java	1.14 03/05/16
 */

package com.sun.ts.tests.appclient.deploy.ejbref.single;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OverProtocol;
import org.jboss.arquillian.container.test.api.TargetsContainer;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.sun.ts.tests.common.base.EETest;
import com.sun.ts.lib.harness.Status;
import com.sun.ts.lib.util.TSNamingContext;
import com.sun.ts.tests.assembly.util.shared.ejbref.single.TestCode;

import tck.arquillian.porting.lib.spi.TestArchiveProcessor;
import tck.arquillian.protocol.common.TargetVehicle;

@ExtendWith(ArquillianExtension.class)
@Tag("platform")
@Tag("tck-appclient")
public class Client extends EETest {

    /*
     * Global variables.
     */
    private TSNamingContext nctx = null;

    private Properties props = null;

    public static void main(String[] args) {
        Client theTests = new Client();
        Status s = theTests.run(args, System.out, System.err);
        s.exit();
    }

    @TargetsContainer("tck-appclient")
    @OverProtocol("appclient")
    @Deployment(name = "appclient_dep_ejbref_single")
    public static EnterpriseArchive createDeployment(@ArquillianResource TestArchiveProcessor archiveProcessor)
            throws IOException {
        JavaArchive ejbClient = ShrinkWrap.create(JavaArchive.class, "appclient_dep_ejbref_single_client.jar");
        ejbClient.addClasses(Client.class, EETest.class, TestCode.class);
        ejbClient.addPackages(true, "com.sun.ts.lib.harness");
        ejbClient.addPackages(true, "com.sun.ts.tests.assembly.util.refbean");

        // The appclient-client descriptor
        URL appClientUrl = Client.class.getResource("appclient_dep_ejbref_single_client.xml");
        ejbClient.addAsManifestResource(appClientUrl, "application-client.xml");
        // The sun appclient-client descriptor
        URL sunAppClientUrl = Client.class.getResource("appclient_dep_ejbref_single_client.jar.sun-application-client.xml");
        ejbClient.addAsManifestResource(sunAppClientUrl, "sun-application-client.xml");

        ejbClient.addAsManifestResource(
                new StringAsset("Main-Class: " + Client.class.getName() + "\n"),
                "MANIFEST.MF");

        JavaArchive ejb = ShrinkWrap.create(JavaArchive.class, "appclient_dep_ejbref_single_ejb.jar");
        ejb.addClasses(
                com.sun.ts.tests.assembly.util.refbean.StatefulExternal.class,
                com.sun.ts.tests.assembly.util.refbean.StatefulExternalEJB.class,
                com.sun.ts.tests.assembly.util.refbean.StatelessExternal.class,
                com.sun.ts.tests.assembly.util.refbean.StatelessExternalEJB.class,
                com.sun.ts.tests.common.ejb.wrappers.MDBWrapper.class,
                com.sun.ts.tests.common.ejb.wrappers.Stateful3xWrapper.class,
                com.sun.ts.tests.common.ejb.wrappers.Stateless3xWrapper.class,
                com.sun.ts.tests.jms.common.JmsUtil.class,
                com.sun.ts.tests.jms.commonee.ParentMsgBean.class
        );

        URL resURL = Client.class.getResource("appclient_dep_ejbref_single_ejb.jar.sun-ejb-jar.xml");
        ejb.addAsManifestResource(resURL, "sun-ejb-jar.xml");
        resURL = Client.class.getResource("appclient_dep_ejbref_single_ejb.xml");
        ejb.addAsManifestResource(resURL, "ejb-jar.xml");


        EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, "appclient_dep_ejbref_single.ear");
        ear.addAsModule(ejbClient);
        ear.addAsModule(ejb);
        return ear;
    }

    ;

    /*
     * @class.setup_props: org.omg.CORBA.ORBClass; java.naming.factory.initial;
     * generateSQL;
     *
     * @class.testArgs: -ap tssql.stmt
     *
     */
    public void setup(String[] args, Properties props) throws Exception {

        try {
            this.props = props;

            logTrace("[Client] Getting Naming Context...");
            nctx = new TSNamingContext();

            logMsg("[Client] Setup succeed.");
        } catch (Exception e) {
            throw new Exception("Setup failed:", e);
        }
    }

    /**
     * @testName: testStateless
     * @assertion_ids: JavaEE:SPEC:10117
     * @test_Strategy: Deploy an application client referencing a Stateless Session
     * bean. Check at runtime that the application client can do a
     * lookup for the EJB reference and use it to create a bean.
     * Then invoke on that bean instance a business method to be
     * found only in this particular bean: This is to check that the
     * EJB reference was resolved consistently with the DD.
     */
    @Test
    @TargetVehicle("appclient")
    public void testStateless() throws Exception {
        boolean pass;

        try {
            pass = TestCode.testStatelessExternal(nctx, props);
            if (!pass) {
                throw new Exception("ejb-ref test failed!");
            }
        } catch (Exception e) {
            throw new Exception("ejb-ref test failed: " + e, e);
        }
    }

    /**
     * @testName: testStateful
     * @assertion_ids: JavaEE:SPEC:10117
     * @test_Strategy: Deploy an application client referencing a Stateful Session
     * bean. Check at runtime that the application client can do a
     * lookup for the EJB reference and use it to create a bean.
     * Then invoke on that bean instance a business method to be
     * found only this particular bean: This is to check that the
     * EJB reference was resolved consistently with the DD.
     */
    @Test
    @TargetVehicle("appclient")
    public void testStateful() throws Exception {
        boolean pass;

        try {
            pass = TestCode.testStatefulExternal(nctx, props);
            if (!pass) {
                throw new Exception("ejb-ref test failed!");
            }
        } catch (Exception e) {
            throw new Exception("ejb-ref test failed: " + e, e);
        }
    }

    public void cleanup() throws Exception {
        logMsg("[Client] cleanup()");
    }

}
