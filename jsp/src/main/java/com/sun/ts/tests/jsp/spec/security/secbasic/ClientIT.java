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

package com.sun.ts.tests.jsp.spec.security.secbasic;

import java.util.Properties;
import java.io.IOException;
import com.sun.javatest.Status;
import com.sun.ts.tests.jsp.spec.security.secbasic.SecBasicClient;
import org.jboss.shrinkwrap.api.asset.UrlAsset;

import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.shrinkwrap.api.Filters;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.jboss.shrinkwrap.api.asset.UrlAsset;

import java.lang.System.Logger;

@ExtendWith(ArquillianExtension.class)
public class ClientIT extends SecBasicClient {

    private static final Logger logger = System.getLogger(ClientIT.class.getName());

    @BeforeEach
    void logStartTest(TestInfo testInfo) {
        logger.log(Logger.Level.INFO, "STARTING TEST : " + testInfo.getDisplayName());
    }

    @AfterEach
    void logFinishTest(TestInfo testInfo) {
        logger.log(Logger.Level.INFO, "FINISHED TEST : " + testInfo.getDisplayName());
    }

    public ClientIT() throws Exception {
    }

    @Deployment(testable = true)
    public static WebArchive createDeployment() throws IOException {

        String packagePath = ClientIT.class.getPackageName().replace(".", "/");
        WebArchive archive = ShrinkWrap.create(WebArchive.class, "jsp_sec_secbasic_web.war");
        archive.setWebXML(ClientIT.class.getClassLoader().getResource(packagePath + "/jsp_sec_secbasic_web.xml"));
        archive.add(new UrlAsset(ClientIT.class.getClassLoader().getResource(packagePath + "/guestPage.jsp")),
                "guestPage.jsp");
        archive.add(new UrlAsset(ClientIT.class.getClassLoader().getResource(packagePath + "/jspSec.jsp")),
                "jspSec.jsp");
        archive.add(new UrlAsset(ClientIT.class.getClassLoader().getResource(packagePath + "/rolereverse.jsp")),
                "rolereverse.jsp");
        archive.add(new UrlAsset(ClientIT.class.getClassLoader().getResource(packagePath + "/unprotected.jsp")),
                "unprotected.jsp");

        // EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, "jsp_sec_secbasic.ear");
        // ear.addAsModule(archive);
        // ear.addAsLibrary(archive);

        // This TCK test needs additional information 
        // In GlassFish, the following sun-web.xml descriptor can be added:
        // archive.addAsWebInfResource("jsp_sec_secbasic_web.war.sun-web.xml", "sun-web.xml");

        // Vendor implementations are encouraged to utilize Arqullian SPI (LoadableExtension, ApplicationArchiveProcessor)
        // to extend the archive with vendor deployment descriptors as needed.
        // For GlassFish, this is demonstrated in the glassfish-runner/jsp-tck module of the Jakarta Platform GitHub repository.

        return archive;

    }

    /*
     * @testName: test1
     *
     * @assertion_ids: Servlet:SPEC:140
     *
     * @test_Strategy: 1. Send request to access jspSec.jsp 2. Receive
     * authentication request.
     */
    @Test
    @Tag("security")
    @RunAsClient
    public void test1() throws Exception {
        super.test1();
    }

    /*
     * @testName: test2
     *
     * @assertion_ids: Servlet:SPEC:140;Servlet:JAVADOC:368
     *
     * @test_Strategy: 1. Send request with correct authentication. 2. Receive
     * page (ensure principal is correct, and ensure that getRemoteUser() returns
     * the correct name)
     *
     * Note: 1. If user has not been authenticated and user attempts to access a
     * protected web resource, and user enters a valid username and password, the
     * original web resource is returned and user is authenticated.
     *
     * 2. getRemoteUser() returns the user name that the client authenticated
     * with.
     */
    @Test
    @Tag("security")
    @RunAsClient
    public void test2() throws Exception {
        super.test2();
    }

    /*
     * @testName: test3
     *
     * @assertion_ids: Servlet:SPEC:162
     *
     * @test_Strategy: 1. Re-send request with incorrect authentication. 2.
     * Receive authentication request.
     * 
     * Note: 1. If user has not been authenticated and user attempts to access a
     * protected web resource, and user enters an invalid username and password,
     * the container denies access to the web resource.
     */
    @Test
    @Tag("security")
    @RunAsClient
    public void test3() throws Exception {
        super.test3();
    }

    /*
     * @testName: test4
     *
     * @assertion_ids: Servlet:SPEC:162
     *
     * @test_Strategy: 1. Send request with correct authentication for user
     * javajoe for a page javajoe is allowed to access. 2. Receive page (this
     * verifies that the javajoe user is set up properly). 3. Send request with
     * correct authentication, but incorrect authorization to access resource 4.
     * Receive error
     *
     * Note: If user has not been authenticated and user attempts to access a
     * protected web resource, and user enters an valid username and password, but
     * for a role that is not authorized to access the resource, the container
     * denies access to the web resource.
     *
     */
    @Test
    @Tag("security")
    @RunAsClient
    public void test4() throws Exception {
        super.test4();
    }

    /*
     * @testName: test5
     *
     * @assertion_ids: Servlet:JAVADOC:368; Servlet:JAVADOC:369
     *
     * @test_Strategy: 1. Send request for unprotected.jsp with no authentication.
     * 2. Receive page 3. Search the returned page for "!true!", which would
     * indicate that at least one call to isUserInRole attempted by
     * unprotected.jsp returned true. 4. check that getRemoteUser() returns null.
     *
     * Note: 1. If user has not been authenticated and user attempts to access an
     * unprotected web resource, the web resource is returned without need to
     * authenticate. 2. isUserInRole() must return false for any valid or invalid
     * role reference. 3. getRemoteUser() must return false
     */
    @Test
    @Tag("security")
    @RunAsClient
    public void test5() throws Exception {
        super.test5();
    }

    /*
     * @testName: test6
     *
     * @assertion_ids: Servlet:SPEC:149
     *
     * @test_Strategy: Given two servlets in the same application, each of which
     * calls isUserInRole(X), and where X is linked to different roles in the
     * scope of each of the servlets (i.e. R1 for servlet 1 and R2 for servlet 2),
     * then a user whose identity is mapped to R1 but not R2, shall get a true
     * return value from isUserInRole( X ) in servlet 1, and a false return value
     * from servlet 2 (a user whose identity is mapped to R2 but not R1 should get
     * the inverse set of return values).
     *
     * Since test1 already verifies the functionality for isUserInRole returning
     * true, this test needs only verify that it should return false for the other
     * jsp. For this test, MGR and ADM are swapped, so isUserInRole() should
     * return opposite values from test1.
     *
     * 1. Send request to access rolereverse.jsp 2. Receive redirect to login
     * page, extract location and session id cookie. 3. Send request to access new
     * location, send cookie 4. Receive login page 5. Send form response with
     * username and password 6. Receive redirect to resource 7. Request resource
     * 8. Receive resource (check isUserInRole for all known roles)
     */
    @Test
    @Tag("security")
    @RunAsClient
    public void test6() throws Exception {
        super.test6();
    }
}
