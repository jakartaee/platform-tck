/*
 * Copyright (c) 2010, 2023 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jaxrs.platform.ejbstateless;

import com.sun.ts.tests.jaxrs.common.JAXRSCommonClient;

import jakarta.ws.rs.core.Response.Status;

import java.io.IOException;
import java.io.InputStream;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/*
 * @class.setup_props: webServerHost;
 *                     webServerPort;
 *                     ts_home;
 */
@ExtendWith(ArquillianExtension.class)
@Tag("jaxrs")
@Tag("platform")
@Tag("web")
public class JAXRSClientIT extends JAXRSCommonClient {

    private static final long serialVersionUID = -96529594720799580L;

    public JAXRSClientIT() {
        setup();
        setContextRoot("/jaxrs_platform_ejbstateless_web/ssb");
    }

    @Deployment(testable = false)
    public static WebArchive createDeployment() throws IOException {

        InputStream inStream =
            JAXRSClientIT.class.getClassLoader()
                         .getResourceAsStream("com/sun/ts/tests/jaxrs/platform/ejbstateless/web.xml.template");

        // Replace the servlet_adaptor in web.xml.template with the System variable set as servlet adaptor
        String webXml = editWebXmlString(inStream);

        WebArchive archive = ShrinkWrap.create(WebArchive.class, "jaxrs_platform_ejbstateless_web.war");
        archive.addClasses(TSAppConfig.class, StatelessLocalIF.class, StatelessResource.class, StatelessRootResource.class,
                StatelessTestBean.class, TestFailedException.class);

        archive.setWebXML(new StringAsset(webXml));

        return archive;
    }

    /* Run test */
    /*
     * @testName: test1
     *
     * @assertion_ids: JAXRS:SPEC:51; JAXRS:SPEC:57;
     *
     * @test_Strategy: Client sends a request on a no-interface stateless EJB root resource located at /ssb; Verify that
     * correct resource method invoked
     */
    @Test
    public void test1() throws Exception {
        setProperty(REQUEST, buildRequest(Request.GET, ""));
        setProperty(SEARCH_STRING, "jaxrs_platform_ejbstateless_web|ssb");
        setProperty(SEARCH_STRING, "Hello|From|Stateless|EJB|Root");
        invoke();
    }

    /*
     * @testName: test2
     *
     * @assertion_ids: JAXRS:SPEC:51; JAXRS:SPEC:57;
     *
     * @test_Strategy: Client sends a request on a no-interface stateless EJB root resource located at /ssb/sub; Verify that
     * correct resource method invoked
     */
    @Test
    public void test2() throws Exception {
        setProperty(REQUEST, buildRequest(Request.GET, "sub"));
        setProperty(SEARCH_STRING, "jaxrs_platform_ejbstateless_web|ssb");
        setProperty(SEARCH_STRING, "Hello|From|Stateless|EJB|Sub");
        invoke();
    }

    /*
     * @testName: test3
     *
     * @assertion_ids: JAXRS:SPEC:51; JAXRS:SPEC:57;
     *
     * @test_Strategy: Client sends a request on a stateless EJB's local interface root resource located at /ssb/localsub;
     * Verify that correct resource method invoked
     */
    @Test
    public void test3() throws Exception {
        setProperty(REQUEST, buildRequest(Request.GET, "localsub"));
        setProperty(SEARCH_STRING, "jaxrs_platform_ejbstateless_web|ssb");
        setProperty(SEARCH_STRING, "localsub|Hello|From|Stateless|Local|EJB|Sub");
        invoke();
    }

    /*
     * @testName: ejbExceptionTest
     *
     * @assertion_ids: JAXRS:SPEC:52;
     *
     * @test_Strategy: If an Exception-Mapper for a EJBException or subclass is not included with an application then
     * exceptions thrown by an EJB resource class or provider method MUST be unwrapped and processed as described in Section
     * 3.3.4.
     */
    @Test
    public void ejbExceptionTest() throws Exception {
        setProperty(Property.REQUEST, buildRequest(Request.GET, "exception"));
        setProperty(Property.STATUS_CODE, getStatusCode(Status.CREATED));
        invoke();
    }

    /*
     * @testName: jaxrsInjectPriorPostConstructOnRootResourceTest
     *
     * @assertion_ids: JAXRS:SPEC:53; JAXRS:SPEC:53.1; JAXRS:SPEC:53.3;
     *
     * @test_Strategy: The following additional requirements apply when using EJBs as resource classes:
     *
     * Field and property injection of JAX-RS resources MUST be performed prior to the container invoking any
     *
     * @PostConstruct annotated method
     *
     * Implementations MUST NOT require use of @Inject or
     *
     * @Resource to trigger injection of JAX-RS annotated fields or properties. Implementations MAY support such usage but
     * SHOULD warn users about non-portability.
     */
    @Test
    public void jaxrsInjectPriorPostConstructOnRootResourceTest() throws Exception {
        setProperty(Property.REQUEST, buildRequest(Request.GET, "priorroot"));
        setProperty(Property.SEARCH_STRING, String.valueOf(true));
        invoke();
    }
}
