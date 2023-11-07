/*
 * Copyright (c) 2007, 2023 Oracle and/or its affiliates. All rights reserved.
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
 * @(#)URLClient.java	1.36 02/11/04
 */

package com.sun.ts.tests.jsp.spec.configuration.scripting;


import java.io.IOException;
import com.sun.ts.tests.jsp.common.client.AbstractUrlClient;

import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.shrinkwrap.api.Filters;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.jboss.shrinkwrap.api.asset.UrlAsset;


@ExtendWith(ArquillianExtension.class)
public class URLClientIT extends AbstractUrlClient {


  public URLClientIT() throws Exception {
    setup();

  }

  @Deployment(testable = false)
  public static WebArchive createDeployment() throws IOException {
    
    String packagePath = URLClientIT.class.getPackageName().replace(".", "/");
    WebArchive archive = ShrinkWrap.create(WebArchive.class, "jsp_config_scr_web.war");
    archive.addClasses(com.sun.ts.tests.jsp.common.tags.tck.SimpleTag.class);
    archive.setWebXML(URLClientIT.class.getClassLoader().getResource(packagePath+"/jsp_config_scr_web.xml"));
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tag.tld", "tag.tld");    
    
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/scrunspecx/ScriptletTest.jspx")), "scrunspecx/ScriptletTest.jspx");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/scrunspecx/ExpressionTest.jspx")), "scrunspecx/ExpressionTest.jspx");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/scrunspecx/DeclarationTest.jspx")), "scrunspecx/DeclarationTest.jspx");
    
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/scrunspec/ScriptletTest.jsp")), "scrunspec/ScriptletTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/scrunspec/ExpressionTest.jsp")), "scrunspec/ExpressionTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/scrunspec/DeclarationTest.jsp")), "scrunspec/DeclarationTest.jsp");
    
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/scrtruex/ScriptletTest.jspx")), "scrtruex/ScriptletTest.jspx");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/scrtruex/ExpressionTest.jspx")), "scrtruex/ExpressionTest.jspx");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/scrtruex/AttributeExpressionTest.jspx")), "scrtruex/AttributeExpressionTest.jspx");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/scrtruex/DeclarationTest.jspx")), "scrtruex/DeclarationTest.jspx");
    
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/scrunspecx/ScriptletTest.jspx")), "scrunspecx/ScriptletTest.jspx");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/scrunspecx/DeclarationTest.jspx")), "scrunspecx/DeclarationTest.jspx");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/scrunspecx/ExpressionTest.jspx")), "scrunspecx/ExpressionTest.jspx");

    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/scrtrue/ScriptletTest.jsp")), "scrtrue/ScriptletTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/scrtrue/ExpressionTest.jsp")), "scrtrue/ExpressionTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/scrtrue/DeclarationTest.jsp")), "scrtrue/DeclarationTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/scrtrue/AttributeExpressionTest.jsp")), "scrtrue/AttributeExpressionTest.jsp");

    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/scrfalsex/ScriptletTest.jspx")), "scrfalsex/ScriptletTest.jspx");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/scrfalsex/ExpressionTest.jspx")), "scrfalsex/ExpressionTest.jspx");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/scrfalsex/DeclarationTest.jspx")), "scrfalsex/DeclarationTest.jspx");

    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/scrfalse/ScriptletTest.jsp")), "scrfalse/ScriptletTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/scrfalse/ExpressionTest.jsp")), "scrfalse/ExpressionTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/scrfalse/DeclarationTest.jsp")), "scrfalse/DeclarationTest.jsp");

    return archive;
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   *
   */

  /* Run test */

  /*
   * @testName: jspConfigurationScriptingUnspecifiedTest
   * 
   * @assertion_ids: JSP:SPEC:256
   * 
   * @test_Strategy: Validate that if scripting-invalid is not specified in the
   * target property-group, that a translation error is not generated and the
   * scripting elements are evaluated. This test is performed against standard
   * syntax JSP pages as well as JSP documents.
   */
  @Test
  public void jspConfigurationScriptingUnspecifiedTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_config_scr_web/scrunspec/DeclarationTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_config_scr_web/scrunspec/ExpressionTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_config_scr_web/scrunspec/ScriptletTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_config_scr_web/scrunspecx/DeclarationTest.jspx HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_config_scr_web/scrunspecx/ExpressionTest.jspx HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_config_scr_web/scrunspecx/ScriptletTest.jspx HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: jspConfigurationScriptingFalseTest
   * 
   * @assertion_ids: JSP:SPEC:144
   * 
   * @test_Strategy: Validate that if scripting-invalid is set to false in the
   * target property-group, that a translation error is not generated and the
   * scripting elements are evaluated. This test is performed against standard
   * syntax JSP pages as well as JSP documents.
   */
  @Test
  public void jspConfigurationScriptingFalseTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_config_scr_web/scrfalse/DeclarationTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_config_scr_web/scrfalse/ExpressionTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_config_scr_web/scrfalse/ScriptletTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_config_scr_web/scrfalsex/DeclarationTest.jspx HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_config_scr_web/scrfalsex/ExpressionTest.jspx HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_config_scr_web/scrfalsex/ScriptletTest.jspx HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_config_scr_web/scrfalsex/ScriptletTest.jspx HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: jspConfigurationScriptingTrueTest
   * 
   * @assertion_ids: JSP:SPEC:143
   * 
   * @test_Strategy: Validate that if scripting-invalid is set to false in the
   * target property-group, that a translation error is generated (except in the
   * case of RT expressions). This test is performed against standard syntax JSP
   * pages as well as JSP documents.
   */
  @Test
  public void jspConfigurationScriptingTrueTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_config_scr_web/scrtrue/DeclarationTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_config_scr_web/scrtrue/ExpressionTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_config_scr_web/scrtrue/ScriptletTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_config_scr_web/scrtrue/AttributeExpressionTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_config_scr_web/scrtruex/DeclarationTest.jspx HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_config_scr_web/scrtruex/ExpressionTest.jspx HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_config_scr_web/scrtruex/ScriptletTest.jspx HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_config_scr_web/scrtruex/AttributeExpressionTest.jspx HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }
}
