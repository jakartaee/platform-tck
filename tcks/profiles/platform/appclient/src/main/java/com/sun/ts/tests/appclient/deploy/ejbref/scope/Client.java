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
 * @(#)Client.java	1.11 03/05/16
 */

package com.sun.ts.tests.appclient.deploy.ejbref.scope;

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

import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.harness.Status;
import com.sun.ts.lib.util.TSNamingContext;
import com.sun.ts.lib.util.TestUtil;

import tck.arquillian.porting.lib.spi.TestArchiveProcessor;
import tck.arquillian.protocol.common.TargetVehicle;

@ExtendWith(ArquillianExtension.class)
@Tag("platform")
@Tag("tck-appclient")
public class Client extends EETest {

    private static final String prefix = "java:comp/env/ejb/";

    /**
     * Bean lookup
     */
    private static final String beanLookup = prefix + "Verona";

    /**
     * Expected value for the bean name
     */
    private static final String beanRefName = "Romeo";

    private TSNamingContext nctx = null;

    private Properties props = null;

    public static void main(String[] args) {
        Client theTests = new Client();
        Status s = theTests.run(args, System.out, System.err);
        s.exit();
    }

    @TargetsContainer("tck-appclient")
    @OverProtocol("appclient")

    @Deployment(name = "appclient_dep_ejbref_scope")
    public static EnterpriseArchive createDeployment(@ArquillianResource TestArchiveProcessor archiveProcessor)
            throws IOException {
        JavaArchive ejbClient1 = ShrinkWrap.create(JavaArchive.class, "appclient_dep_ejbref_scope_client.jar");
        ejbClient1.addPackages(true, Client.class.getPackage());
        ejbClient1.addPackages(true, "com.sun.ts.lib.harness");

        // The appclient-client descriptor
        URL appClientUrl = Client.class.getResource("appclient_dep_ejbref_scope_client.xml");
        ejbClient1.addAsManifestResource(appClientUrl, "application-client.xml");
        // The sun appclient-client descriptor
        URL sunAppClientUrl = Client.class.getResource("appclient_dep_ejbref_scope_client.jar.sun-application-client.xml");
        ejbClient1.addAsManifestResource(sunAppClientUrl, "sun-application-client.xml");

        ejbClient1.addAsManifestResource(
                new StringAsset("Main-Class: " + Client.class.getName() + "\n"),
                "MANIFEST.MF");

        JavaArchive ejbClient2 = ShrinkWrap.create(JavaArchive.class, "appclient_dep_ejbref_scope_client_another.jar");
        ejbClient2.addPackages(true, Client.class.getPackage());
        ejbClient2.addPackages(true, "com.sun.ts.lib.harness");

        // The appclient-client descriptor
        appClientUrl = Client.class.getResource("appclient_dep_ejbref_scope_another_client.xml");
        ejbClient2.addAsManifestResource(appClientUrl, "application-client.xml");
        // The sun appclient-client descriptor
        sunAppClientUrl = Client.class.getResource("appclient_dep_ejbref_scope_another_client.jar.sun-application-client.xml");
        ejbClient2.addAsManifestResource(sunAppClientUrl, "sun-application-client.xml");

        ejbClient2.addAsManifestResource(
                new StringAsset("Main-Class: " + "com.sun.ts.tests.appclient.deploy.ejbref.scope.Client" + "\n"),
                "MANIFEST.MF");

        JavaArchive ejb = ShrinkWrap.create(JavaArchive.class, "appclient_dep_ejbref_scope_ejb.jar");
        ejb.addClasses(
        		com.sun.ts.tests.appclient.deploy.ejbref.scope.ReferencedBean.class,
        		com.sun.ts.tests.appclient.deploy.ejbref.scope.ReferencedBeanEJB.class,
                com.sun.ts.tests.assembly.util.shared.ejbref.common.ReferencedBeanCode.class,
                com.sun.ts.tests.common.ejb.wrappers.StatelessWrapper.class
        );

        URL resURL = Client.class.getResource("appclient_dep_ejbref_scope_ejb.jar.sun-ejb-jar.xml");
        ejb.addAsManifestResource(resURL, "sun-ejb-jar.xml");
        resURL = Client.class.getResource("appclient_dep_ejbref_scope_ejb.xml");
        ejb.addAsManifestResource(resURL, "ejb-jar.xml");

        EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, "appclient_dep_ejbref_scope.ear");
        ear.addAsModule(ejbClient1);
        ear.addAsModule(ejbClient2);
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
        this.props = props;

        try {
            nctx = new TSNamingContext();
            logMsg("[Client] Setup succeed (got naming context).");
        } catch (Exception e) {
            throw new Exception("Setup failed:", e);
        }
    }

    /**
     * @testName: testScope
     * @assertion_ids: JavaEE:SPEC:117
     * @test_Strategy: We package in the same .ear file:
     * <p>
     * - Two application clients's using the same ejb-ref-name
     * ('ejb/Verona') to reference two distinct ReferencedBean's.
     * <p>
     * - One EJB jar, containing two distinct beans, whose identity
     * is defined by a String environment entry ('myName').
     * <p>
     * We check that:
     * <p>
     * - We can deploy the application. - We can run one of the two
     * application clients. - This application client can lookup its
     * referenced bean and get the bean identity. - Check this
     * runtime identity against the one specified in the application
     * client DD (validates that this EJB reference is resolved
     * correctly).
     */
    @Test
    @TargetVehicle("appclient")
    public void testScope() throws Exception {
        ReferencedBean bean = null;
        String beanName;
        boolean pass = false;

        try {
            TestUtil.logTrace("[Client] Looking up '" + beanLookup + "'...");
            bean = (ReferencedBean) nctx.lookup(beanLookup, ReferencedBean.class);
            bean.createNamingContext();
            bean.initLogging(props);
            beanName = bean.whoAreYou();
            TestUtil.logTrace(beanLookup + "name is '" + beanName + "'");

            pass = beanName.equals(beanRefName);
            if (!pass) {
                TestUtil.logErr("[Client] " + beanLookup + "name is '" + beanName + "' expected '" + beanRefName + "'");

                throw new Exception("ejb-ref scope test failed!");
            }
        } catch (Exception e) {
            throw new Exception("ejb-ref scope test failed: " + e, e);
        }
    }

    public void cleanup() throws Exception {
        logMsg("[Client] cleanup()");
    }

}
