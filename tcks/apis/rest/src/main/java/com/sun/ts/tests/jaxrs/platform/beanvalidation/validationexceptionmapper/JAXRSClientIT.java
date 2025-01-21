/*
 * Copyright (c) 2015, 2023 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jaxrs.platform.beanvalidation.validationexceptionmapper;

import com.sun.ts.tests.jaxrs.common.client.JaxrsCommonClient;
import com.sun.ts.tests.jaxrs.common.provider.StringBean;
import com.sun.ts.tests.jaxrs.common.provider.StringBeanEntityProvider;
import com.sun.ts.tests.jaxrs.common.util.JaxrsUtil;
import com.sun.ts.tests.jaxrs.platform.beanvalidation.annotation.ConstraintDeclarationAnnotation;
import com.sun.ts.tests.jaxrs.platform.beanvalidation.annotation.ConstraintDeclarationResource;
import com.sun.ts.tests.jaxrs.platform.beanvalidation.annotation.ConstraintDeclarationValidator;
import com.sun.ts.tests.jaxrs.platform.beanvalidation.annotation.ConstraintDefinitionAnnotation;
import com.sun.ts.tests.jaxrs.platform.beanvalidation.annotation.ConstraintDefinitionResource;
import com.sun.ts.tests.jaxrs.platform.beanvalidation.annotation.ConstraintDefinitionValidator;
import com.sun.ts.tests.jaxrs.platform.beanvalidation.annotation.NotFiveNorShort;
import com.sun.ts.tests.jaxrs.platform.beanvalidation.annotation.NotFiveNorShortStringBeanValidator;
import com.sun.ts.tests.jaxrs.platform.beanvalidation.annotation.NotNullOrOne;
import com.sun.ts.tests.jaxrs.platform.beanvalidation.annotation.NotNullOrOneStringBean;
import com.sun.ts.tests.jaxrs.platform.beanvalidation.annotation.NotNullOrOneStringBeanValidator;
import com.sun.ts.tests.jaxrs.platform.beanvalidation.annotation.NotShortNorFiveEntityProvider;
import com.sun.ts.tests.jaxrs.platform.beanvalidation.annotation.NotShortNorFiveStringBean;
import com.sun.ts.tests.jaxrs.platform.beanvalidation.annotation.Resource;
import com.sun.ts.tests.jaxrs.platform.beanvalidation.annotation.ValidateExecutableResource;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;

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
/**
 * Test the exception mapper is used
 *
 * @since 2.1.0
 */
@ExtendWith(ArquillianExtension.class)
@Tag("jaxrs")
@Tag("platform")
@Tag("web")
public class JAXRSClientIT extends JaxrsCommonClient {

    private static final long serialVersionUID = 210L;

    public JAXRSClientIT() {
        setup();
        setContextRoot("/jaxrs_platform_beanvalidation_validationexceptionmapper_web/resource");
    }

    @Deployment(testable = false)
    public static WebArchive createDeployment() throws IOException {

        InputStream inStream = JAXRSClientIT.class.getClassLoader()
                .getResourceAsStream("com/sun/ts/tests/jaxrs/platform/beanvalidation/validationexceptionmapper/web.xml.template");
        // Replace the servlet_adaptor in web.xml.template with the System variable set as servlet adaptor
        String webXml = editWebXmlString(inStream);
        inStream = JAXRSClientIT.class.getClassLoader()
                .getResourceAsStream("com/sun/ts/tests/jaxrs/platform/beanvalidation/validationexceptionmapper/beans.xml");
        String beansXml = toString(inStream);

        WebArchive archive = ShrinkWrap.create(WebArchive.class, "jaxrs_platform_beanvalidation_validationexceptionmapper_web.war");
        archive.addClasses(TSAppConfig.class, Resource.class, ConstraintDeclarationAnnotation.class, ConstraintDeclarationResource.class,
                ConstraintDeclarationValidator.class, ConstraintDefinitionAnnotation.class, ConstraintDefinitionResource.class,
                ConstraintDefinitionValidator.class, NotFiveNorShort.class, NotFiveNorShortStringBeanValidator.class, NotNullOrOne.class,
                NotNullOrOneStringBean.class, NotNullOrOneStringBeanValidator.class, NotShortNorFiveEntityProvider.class,
                NotShortNorFiveStringBean.class, ValidateExecutableResource.class, JaxrsUtil.class, StringBean.class,
                StringBeanEntityProvider.class, ValidationExceptionMapper.class);

        archive.addAsWebInfResource(new StringAsset(beansXml), "beans.xml");
        archive.setWebXML(new StringAsset(webXml));
        // archive.addAsWebInfResource(JAXRSClientIT.class.getPackage(), "web.xml.template", "web.xml"); //can use if the
        // web.xml.template doesn't need to be modified.

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
     * @testName: beanIsInvalidForBeingFiveCharsLongTest
     *
     * @assertion_ids: JAXRS:SPEC:101; JAXRS:SPEC:102; JAXRS:SPEC:102.3;
     *
     * @test_Strategy: JAX-RS implementations MUST follow the constraint annotation rules defined in Bean Validation 1.1.
     * JSR
     */
    @Test
    public void beanIsInvalidForBeingFiveCharsLongTest() throws Exception {
        setProperty(Property.REQUEST, buildRequest(Request.POST, "notshortnorfive"));
        setProperty(Property.CONTENT, "12345");
        setProperty(Property.SEARCH_STRING, ConstraintViolationException.class.getName());
        invoke();
        logMsg("ExceptionMapper works as expected");
    }

    /*
     * @testName: returnIsInvalidForBeingFiveCharsLongTest
     *
     * @assertion_ids: JAXRS:SPEC:101; JAXRS:SPEC:102; JAXRS:SPEC:102.2;
     *
     * @test_Strategy: JAX-RS implementations MUST follow the constraint annotation rules defined in Bean Validation 1.1.
     * JSR
     */
    @Test
    public void returnIsInvalidForBeingFiveCharsLongTest() throws Exception {
        setProperty(Property.REQUEST, buildRequest(Request.POST, "returnnotshortnorfive"));
        setProperty(Property.CONTENT, "12345");
        setProperty(Property.SEARCH_STRING, ConstraintViolationException.class.getName());
        invoke();
        logMsg("ExceptionMapper works as expected");

    }

    /*
     * @testName: beanAnnotatedIsInvalidForBeingFiveCharsLongTest
     *
     * @assertion_ids: JAXRS:SPEC:101; JAXRS:SPEC:102; JAXRS:SPEC:102.3;
     *
     * @test_Strategy: JAX-RS implementations MUST follow the constraint annotation rules defined in Bean Validation 1.1.
     * JSR
     */
    @Test
    public void beanAnnotatedIsInvalidForBeingFiveCharsLongTest() throws Exception {
        setProperty(Property.REQUEST, buildRequest(Request.POST, "directannotatedarg"));
        setProperty(Property.CONTENT, "12345");
        setProperty(Property.SEARCH_STRING, ConstraintViolationException.class.getName());
        invoke();
        logMsg("ExceptionMapper works as expected");

    }

    /*
     * @testName: beanAnnotatedReturnIsInvalidForBeingFiveCharsLongTest
     *
     * @assertion_ids: JAXRS:SPEC:101; JAXRS:SPEC:102; JAXRS:SPEC:102.2;
     *
     * @test_Strategy: JAX-RS implementations MUST follow the constraint annotation rules defined in Bean Validation 1.1.
     * JSR
     */
    @Test
    public void beanAnnotatedReturnIsInvalidForBeingFiveCharsLongTest() throws Exception {
        setProperty(Property.REQUEST, buildRequest(Request.POST, "directannotatedreturn"));
        setProperty(Property.CONTENT, "12345");
        setProperty(Property.SEARCH_STRING, ConstraintViolationException.class.getName());
        invoke();
        logMsg("ExceptionMapper works as expected");
    }

    /*
     * @testName: constraintDeclarationExceptionThrownTest
     *
     * @assertion_ids: JAXRS:SPEC:101; JAXRS:SPEC:102; JAXRS:SPEC:102.1;
     *
     * @test_Strategy: JAX-RS implementations MUST follow the constraint annotation rules defined in Bean Validation 1.1.
     * JSR
     */
    @Test
    public void constraintDeclarationExceptionThrownTest() throws Exception {
        setProperty(Property.REQUEST, buildRequest(Request.POST, "declaration/constraintdeclarationexception"));
        setProperty(Property.CONTENT, "throw ConstraintDeclarationException()");
        setProperty(Property.SEARCH_STRING, ValidationException.class.getName());
        invoke();
        logMsg("ExceptionMapper works as expected");

    }

    /*
     * @testName: constraintDefinitionExceptionThrownTest
     *
     * @assertion_ids: JAXRS:SPEC:101; JAXRS:SPEC:102; JAXRS:SPEC:102.1;
     *
     * @test_Strategy: JAX-RS implementations MUST follow the constraint annotation rules defined in Bean Validation 1.1.
     * JSR
     */
    @Test
    public void constraintDefinitionExceptionThrownTest() throws Exception {
        setProperty(Property.REQUEST, buildRequest(Request.POST, "definition/constraintdefinitionexception"));
        setProperty(Property.CONTENT, "throw ConstraintDefinitionException()");
        setProperty(Property.SEARCH_STRING, ValidationException.class.getName());
        invoke();
        logMsg("ExceptionMapper works as expected");

    }

    /*
     * @testName: validateExecutableIsInvalidForBeingShortTest
     *
     * @assertion_ids: JAXRS:SPEC:101; JAXRS:SPEC:102; JAXRS:SPEC:102.2;
     *
     * @test_Strategy: JAX-RS implementations MUST follow the constraint annotation rules defined in Bean Validation 1.1.
     * JSR
     *
     * Due to validation of whole bean status 400 is returned
     */
    @Test
    public void validateExecutableIsInvalidForBeingShortTest() throws Exception {
        setProperty(Property.REQUEST, buildRequest(Request.GET, "executable/nogetter"));
        setProperty(Property.SEARCH_STRING, ConstraintViolationException.class.getName());
        invoke();
        logMsg("ExceptionMapper works as expected");
    }
}
