/*
 * Copyright (c) 2012, 2023 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jaxrs.platform.environment.servlet;

import com.sun.ts.tests.jaxrs.common.JAXRSCommonClient;

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

    private static final long serialVersionUID = 1L;

    public JAXRSClientIT() {
        setup();
        setContextRoot("/jaxrs_platform_environment_servlet_web/resource");
    }

    @Deployment(testable = false)
    public static WebArchive createDeployment() throws IOException {

        InputStream inStream =
            JAXRSClientIT.class.getClassLoader()
                         .getResourceAsStream("com/sun/ts/tests/jaxrs/platform/environment/servlet/web.xml.template");

        // Replace the servlet_adaptor in web.xml.template with the System variable set as servlet adaptor
        String webXml = editWebXmlString(inStream);

        WebArchive archive = ShrinkWrap.create(WebArchive.class, "jaxrs_platform_environment_servlet_web.war");
        archive.addClasses(TSAppConfig.class, ConsumingFilter.class, Resource.class);

        archive.setWebXML(new StringAsset(webXml));

        return archive;
    }

    /**
     * Entry point for different-VM execution. It should delegate to method run(String[], PrintWriter, PrintWriter), and
     * this method should not contain any test configuration.
     */
    // public static void main(String[] args) {
    // new JAXRSClient().run(args);
    // }

    /* Run test */
    /*
     * @testName: checkServletExtensionTest
     *
     * @assertion_ids: JAXRS:SPEC:41; JAXRS:SPEC:42;
     *
     * @test_Strategy: The @Context annotation can be used to indicate a dependency on a Servlet-defined resource.
     *
     * A Servlet-based implementation MUST support injection of the following Servlet-defined types: ServletConfig,
     * ServletContext, HttpServletRequest and HttpServletResponse.
     */
    @Test
    public void checkServletExtensionTest() throws Exception {
        setProperty(Property.REQUEST, buildRequest(Request.GET, "context"));
        setProperty(Property.UNEXPECTED_RESPONSE_MATCH, "is null");
        invoke();
    }

    /*
     * @testName: streamReaderRequestEntityTest
     *
     * @assertion_ids: JAXRS:SPEC:43;
     *
     * @test_Strategy: An injected HttpServletRequest allows a resource method to stream the contents of a request entity.
     */
    @Test
    public void streamReaderRequestEntityTest() throws Exception {
        setProperty(Property.REQUEST, buildRequest(Request.POST, "streamreader"));
        setProperty(Property.CONTENT, Resource.class.getName());
        setProperty(Property.SEARCH_STRING, Resource.class.getName());
        setProperty(Property.UNEXPECTED_RESPONSE_MATCH, "empty");
        invoke();
    }

    /*
     * @testName: prematureHttpServletResponseTest
     *
     * @assertion_ids: JAXRS:SPEC:44;
     *
     * @test_Strategy: An injected HttpServletResponse allows a resource method to commit the HTTP response prior to
     * returning. An implementation MUST check the committed status and only process the return value if the response is not
     * yet committed.
     */
    @Test
    public void prematureHttpServletResponseTest() throws Exception {
        setProperty(Property.REQUEST, buildRequest(Request.GET, "premature"));
        invoke();
    }

    /*
     * @testName: servletFilterRequestConsumptionTest
     *
     * @assertion_ids: JAXRS:SPEC:45; JAXRS:SPEC:46;
     *
     * @test_Strategy: Servlet filters may trigger consumption of a request body by accessing request parameters.
     *
     * In a servlet container the @FormParam annotation and the standard entity provider for
     * application/x-www-form--urlencoded MUST obtain their values from the servlet request parameters if the request body
     * has already been consumed.
     */
    @Test
    public void servletFilterRequestConsumptionTest() throws Exception {
        String content = "ENTITY";
        setProperty(Property.REQUEST_HEADERS, "Content-type:" + ConsumingFilter.CONTENTTYPE);
        setProperty(Property.CONTENT, "entity=" + content);
        setProperty(Property.REQUEST, buildRequest(Request.POST, "consume"));
        setProperty(Property.SEARCH_STRING, content);
        invoke();
    }
}
