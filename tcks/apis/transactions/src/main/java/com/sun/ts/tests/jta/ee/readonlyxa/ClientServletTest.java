/*
 * Copyright (c) 2025 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jta.ee.readonlyxa;

import java.net.URL;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OverProtocol;
import org.jboss.arquillian.container.test.api.TargetsContainer;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.exporter.ZipExporter;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;

import com.sun.ts.tests.common.base.ServiceEETest;
import tck.arquillian.porting.lib.spi.TestArchiveProcessor;

@ExtendWith(ArquillianExtension.class)
@Tag("platform")
@Tag("connector_resourcedef_servlet_optional")
@Tag("tck-javatest")

@TestMethodOrder(MethodOrderer.MethodName.class)
public class ClientServletTest extends Client {
    @TargetsContainer("tck-javatest")
    @OverProtocol("javatest")
    @Deployment(name = "servlet_resourcedefs", order = 2)
    public static EnterpriseArchive createDeployment(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // War
        // the war with the correct archive name
        WebArchive servlet_resourcedefs_web = ShrinkWrap.create( WebArchive.class, "servlet_resourcedefs_web.war");
        // The class files
        servlet_resourcedefs_web.addClasses(
                com.sun.ts.tests.connector.resourceDefs.servlet.Client.class
        );
        // The web.xml descriptor
        URL warResURL = ClientServletTest.class.getResource( "servlet_resourcedefs_web.xml");
        servlet_resourcedefs_web.addAsWebInfResource(warResURL, "web.xml");
        // The sun-web.xml descriptor
        warResURL = ClientServletTest.class.getResource( "servlet_resourcedefs_web.war.sun-web.xml");
        servlet_resourcedefs_web.addAsWebInfResource(warResURL, "sun-web.xml");

        // Call the archive processor
        archiveProcessor.processWebArchive( servlet_resourcedefs_web, ClientServletTest.class, warResURL);

        // RAR
        // the rar with the correct archive name
        JavaArchive conn_resourcedefs_jar = ShrinkWrap.create(JavaArchive.class, "resouredef.jar");
        // The class files
        conn_resourcedefs_jar.addClasses(
                com.sun.ts.tests.common.connector.embedded.adapter1.CRDActivationSpec.class,
                com.sun.ts.tests.common.connector.embedded.adapter1.CRDAdminObject.class,
                com.sun.ts.tests.common.connector.embedded.adapter1.CRDManagedConnectionFactory.class,
                com.sun.ts.tests.common.connector.embedded.adapter1.CRDMessageListener.class,
                com.sun.ts.tests.common.connector.embedded.adapter1.CRDMessageWork.class,
                com.sun.ts.tests.common.connector.embedded.adapter1.CRDResourceAdapterImpl.class,
                com.sun.ts.tests.common.connector.embedded.adapter1.CRDWorkManager.class,
                com.sun.ts.tests.common.connector.embedded.adapter1.MsgXAResource.class,
                com.sun.ts.tests.common.connector.embedded.adapter1.NestedWorkXid1.ContextType.class,
                com.sun.ts.tests.common.connector.embedded.adapter1.NestedWorkXid1.class,
                com.sun.ts.tests.common.connector.util.ConnectorStatus.class,
                com.sun.ts.tests.common.connector.util.Log.class,
                com.sun.ts.tests.common.connector.whitebox.Debug.class,
                com.sun.ts.tests.common.connector.whitebox.NestedWorkXid.class,
                com.sun.ts.tests.common.connector.whitebox.TSConnectionFactory.class,
                com.sun.ts.tests.common.connector.whitebox.TSDataSource.class,
                com.sun.ts.tests.common.connector.whitebox.WorkImpl.class,
                com.sun.ts.tests.common.connector.whitebox.WorkListenerImpl.class,
                com.sun.ts.tests.common.connector.whitebox.XidImpl.class
        );
        JavaArchive conn_resourcedefs_rar = ShrinkWrap.create(JavaArchive.class, "whitebox-rd.rar");
        conn_resourcedefs_rar.add(conn_resourcedefs_jar, "/", ZipExporter.class);

        // Ear
        EnterpriseArchive servlet_resourcedefs_ear = ShrinkWrap.create(EnterpriseArchive.class, "servlet_resourcedefs.ear");

        // Any libraries added to the ear

        // The component jars built by the package target
        servlet_resourcedefs_ear.addAsModule(servlet_resourcedefs_web);
        servlet_resourcedefs_ear.addAsModule(conn_resourcedefs_rar);




        // The application.xml descriptor
        URL earResURL = null;
        // The sun-application.xml descriptor
        earResURL = ClientServletTest.class.getResource( "servlet_resourcedefs.ear.sun-application.xml");
        servlet_resourcedefs_ear.addAsManifestResource(earResURL, "sun-application.xml");
        // Call the archive processor
        archiveProcessor.processEarArchive( servlet_resourcedefs_ear, ClientServletTest.class, earResURL);
        return servlet_resourcedefs_ear;
    }

    @Test
    @Override
    public void testInsertWithReadOnlyXAResource() throws Exception {
        super.testInsertWithReadOnlyXAResource();
    }

    @Test
    @Override
    public void testInsertWithNonReadOnlyXAResource() throws Exception {
        super.testInsertWithNonReadOnlyXAResource();
    }
}