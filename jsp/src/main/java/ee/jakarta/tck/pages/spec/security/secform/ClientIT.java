/*
 * Copyright (c) 2007, 2020 Oracle and/or its affiliates. All rights reserved.
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
package ee.jakarta.tck.pages.spec.security.secform;

import java.util.Properties;
import java.io.IOException;
import com.sun.javatest.Status;

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
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.jboss.shrinkwrap.api.asset.UrlAsset;

import java.lang.System.Logger;

@ExtendWith(ArquillianExtension.class)
public class ClientIT extends secformClient {

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
        WebArchive archive = ShrinkWrap.create(WebArchive.class, "jsp_sec_secform_web.war");
        archive.setWebXML(ClientIT.class.getClassLoader().getResource(packagePath + "/jsp_sec_secform_web.xml"));
        archive.add(new UrlAsset(ClientIT.class.getClassLoader().getResource(packagePath + "/allRoles.jsp")),
                "allRoles.jsp");
        archive.add(new UrlAsset(ClientIT.class.getClassLoader().getResource(packagePath + "/error.jsp")), "error.jsp");
        archive.add(new UrlAsset(ClientIT.class.getClassLoader().getResource(packagePath + "/guestPage.jsp")),
                "guestPage.jsp");
        archive.add(new UrlAsset(ClientIT.class.getClassLoader().getResource(packagePath + "/jspSec.jsp")),
                "jspSec.jsp");
        archive.add(new UrlAsset(ClientIT.class.getClassLoader().getResource(packagePath + "/login.jsp")), "login.jsp");
        archive.add(new UrlAsset(ClientIT.class.getClassLoader().getResource(packagePath + "/One.jsp")), "One.jsp");
        archive.add(new UrlAsset(ClientIT.class.getClassLoader().getResource(packagePath + "/rolereverse.jsp")),
                "rolereverse.jsp");
        archive.add(new UrlAsset(ClientIT.class.getClassLoader().getResource(packagePath + "/Sample.jsp")),
                "Sample.jsp");
        archive.add(new UrlAsset(ClientIT.class.getClassLoader().getResource(packagePath + "/Two.jsp")), "Two.jsp");
        archive.add(new UrlAsset(ClientIT.class.getClassLoader().getResource(packagePath + "/unprotected.jsp")),
                "unprotected.jsp");

        // EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, "jsp_sec_secform.ear");
        // ear.addAsModule(archive);
        // ear.addAsLibrary(archive);

        // This TCK test needs additional information 
        // In GlassFish, the following sun-web.xml descriptor can be added: 
        // archive.addAsWebInfResource("jsp_sec_secform_web.war.sun-web.xml", "sun-web.xml");

        // Vendor implementations are encouraged to utilize Arqullian SPI (LoadableExtension, ApplicationArchiveProcessor)
        // to extend the archive with vendor deployment descriptors as needed.
        // For GlassFish, this is demonstrated in the glassfish-runner/jsp-tck module of the Jakarta Platform GitHub repository.

        return archive;

    }

    /*
     * @testName: test1
     *
     * @assertion_ids: Servlet:SPEC:142
     *
     * @test_Strategy: 1. Send request to access jspSec.jsp 2. Receive login
     * page(make sure it the expected login page) 3. Send form response with
     * username and password 4. Receive jspSec.jsp (ensure principal is correct,
     * and ensure getRemoteUser() returns the username, and ensure isUserInRole()
     * is working properly) 5. Re-request jspSec.jsp 6. Ensure principal is still
     * correct and getRemoteUser() still returns the correct username. Also ensure
     * isUserInRole() is still working properly.
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
     * @assertion_ids: Servlet:SPEC:142.4.3
     *
     * @test_Strategy: 1. Send request to access jspSec.jsp 2. Receive login page
     * 3. Send form response with username and incorrect password 4. Receive error
     * page (make sure it is the expected error page)
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
     * @assertion_ids: Servlet:SPEC:142
     *
     * @test_Strategy: 1. Send request to access guestPage.jsp 2. Receive login
     * page 3. Send form response with username(javajoe) and password 4. Receive
     * resource (check user principal) Note: If user has not been authenticated
     * and user attempts to access a protected web resource, and user enters
     * correct username and password of a user that is authorized to access the
     * resource, the resource is returned (similar to test1, but uses user javajoe
     * instead of j2ee). This test establishes that the javajoe user is set up
     * properly.
     * 
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
     * @assertion_ids: Servlet:SPEC:149; Servlet:SPEC:160; Servlet:SPEC:162
     *
     *
     * @test_Strategy: 1. Send request to access jspSec.jsp 2. Receive login page
     * 3. Send form response with username and password 4. Receive an error
     * (expected unauthorized error) 5. Send request to access unprotected.jsp 6.
     * Receive unprotected.jsp. Note: If user has not been authenticated and user
     * attempts to access a protected web resource, and user enters correct
     * username and password of a user that is not authorized to access the
     * resource, the resource is not returned. The authenticated user is not
     * denied access to an unprotected page.
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
     * @assertion_ids: Servlet:JAVADOC:368; Servlet:JAVADOC:369;
     * Servlet:SPEC:154.1
     * 
     * @test_Strategy: 1. Send request to access unprotected.jsp 2. Receive
     * unprotected.jsp 3. Search the returned page for "!true!", which would
     * indicate that at least one call to isUserInRole attempted by
     * unprotected.jsp returned true. 4. Check that the call to getRemoteUser()
     * returned null. Note: If user has not been authenticated and user attempts
     * to access an unprotected web resource, the resource is returned, and the
     * user is not forced to authenticate. Also, isUserInRole() must return false
     * for any valid or invalid role reference. A call to getRemoteUser() must
     * return null.
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
     * then a user whose identity is mapped to R1 but not R2,shall get a true
     * return value from isUserInRole( X ) in servlet 1, and a false return value
     * from servlet 2 (a user whose identity is mapped to R2 but not R1 should get
     * the inverse set of return values).
     *
     * Since test1 already verifies the functionality for isUserInRole returning
     * true, this test needs only verify that it should return false for the other
     * jsp. For this test, MGR and ADM are swapped, so isUserInRole() should
     * return opposite values from test1.
     *
     * 1. Send request to access rolereverse.jsp 2. Receive login page 3. Send
     * form response with username and password 4. Receive resource (check
     * isUserInRole for all known roles)
     */
    @Test
    @Tag("security")
    @RunAsClient
    public void test6() throws Exception {
        super.test6();
    }

    /*
     * @testName: test7
     *
     * @assertion_ids: Servlet:SPEC:89
     *
     * @test_Strategy: 1) send a http request to WEB-INF directory 2) expect 404
     * or 403 3) repeat step 1 and 2 for the following a) web-inf (for case
     * insensitive platforms) b) WEB-INF/web.xml c) web-inf/web.xml 4) based on
     * the http return code, report test status
     *
     */
    @Test
    @Tag("security")
    @RunAsClient
    public void test7() {
        super.test7();
    }

    /*
     * @testName: test8
     *
     * @assertion_ids: Servlet:SPEC:92.1
     *
     * @test_Strategy: 1) send a http request to META-INF directory 2) expect 404
     * or 403 3) repeat step 1 and 2 for the following a) meta-inf (for case
     * insensitive platforms) b) META-INF/MANIFEST.MF c) meta-inf/manifest.mf 4)
     * based on the http return code, report test status
     */
    @Test
    @Tag("security")
    @RunAsClient
    public void test8() throws Exception {
        super.test8();
    }

    /*
     * @testName: test9
     *
     * @assertion_ids: Servlet:SPEC:134
     *
     * @test_Strategy: 1) Deploy a two webcomponents One.jsp and Two.jsp
     * exercising various mapping rules 2) Make a http request with a URL(based on
     * the above mapping rules) 3) Make a http request with a absolute match URL.
     * 4) compare the results obtained through step 2 and 3 and declare test
     * result Note:
     *
     * 1) A string beginning with a / character and ending with a /* postfix is
     * used as a path mapping. 2) A string beginning with a *. prefix is used as
     * an extension mapping. 3) All other strings are used as exact matches only
     * 4) A string containing only the / character indicates that servlet
     * specified by the mapping becomes the "default" servlet of the application.
     * In this case the servlet path is the request URI minus the context path and
     * the path info is null.
     *
     */
    @Test
    @Tag("security")
    @RunAsClient
    public void test9() throws Exception {
        super.test9();
    }

    /*
     * @testName: test10
     *
     * @assertion_ids: Servlet:SPEC:138; Servlet:SPEC:139
     *
     * @test_Strategy:
     *
     * 1. Send request to access Sample.jsp 2. Receive login page(make sure it is
     * the expected login page) 3. Send form response with username and password
     * 4. Receive Sample.jsp (ensure principal is correct, and ensure
     * getRemoteUser() returns the username, and ensure isUserInRole() is working
     * properly)
     */
    @Test
    @Tag("security")
    @RunAsClient
    public void test10() throws Exception {
        super.test10();
    }

    /*
     * @testName: test11
     *
     * @assertion_ids: Servlet:SPEC:150
     *
     * @test_Strategy: Configure allRoles.jsp to be accessible by allRoles (
     * Administrator and * )
     *
     * 1) Try accesing allRoles.jsp as the following user a) j2ee b) javajoe 2)
     * Based on the http reply, report test status
     *
     * Note: The auth-constraint element indicates the user roles that should be
     * permitted access to this resource collection. The role used here must
     * either in a security-role-ref element, or be the specially reserved
     * role-name * that is a compact syntax for indicating all roles in the web
     * application. If both * and rolenames appear, the container interprets this
     * as all roles.
     *
     */
    @Test
    @Tag("security")
    @RunAsClient
    public void test11() throws Exception {
        super.test11();
    }

    /* ***NOTE: test13 is only for Servlet *** */
    /*
     * @testName: test14
     *
     * @assertion_ids: Servlet:SPEC:144
     *
     *
     * @test_Strategy: 1. Configure pageSec(jspSec.jsp or ServletSecTest) and
     * pageSample(Sample.jsp or SampleTest ) to be accessible only by
     * Administrator 2. Send request to access jspSec.jsp 3. Receive login page 4.
     * Send form response with username and password 5. Receive jspSec.jsp (ensure
     * principal is correct, and ensure getRemoteUser() returns the username, and
     * ensure isUserInRole() is working properly) 6. Try accessing
     * pageSample(Sample.jsp or SampleTest) which is also configured to be
     * accessible with the same security identity, since we are already
     * authenticated we should be able to access pageSample without going through
     * login page again. 7. Ensure principal is still correct and getRemoteUser()
     * still returns the correct username. Also ensure isUserInRole() is still
     * working properly. Note: servlet container is required to track
     * authentication information at the container level (rather than at the web
     * application level). This allows users authenticated for one web application
     * to access other resources managed by the container permitted to the same
     * security identity.
     */
    @Test
    @Tag("security")
    @RunAsClient
    public void test14() throws Exception {
        super.test14();
    }
}
