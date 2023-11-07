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

package com.sun.ts.tests.jsp.spec.tagfiles.semantics;


import java.io.IOException;
import com.sun.ts.tests.jsp.common.client.AbstractUrlClient;

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

    setContextRoot("/jsp_tagfile_semantics_web");

  }

  @Deployment(testable = false)
  public static WebArchive createDeployment() throws IOException {
    
    String packagePath = URLClientIT.class.getPackageName().replace(".", "/");
    WebArchive archive = ShrinkWrap.create(WebArchive.class, "jsp_tagfile_semantics_web.war");
    archive.addClasses(com.sun.ts.tests.jsp.common.util.JspTestUtil.class);
    archive.addPackages(true, Filters.exclude(URLClientIT.class),
            URLClientIT.class.getPackageName());
    archive.setWebXML(URLClientIT.class.getClassLoader().getResource(packagePath+"/jsp_tagfile_semantics_web.xml"));

    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/AttributeNotSpecifiedTag.tag", "tags/AttributeNotSpecifiedTag.tag");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/DeclaredAttributesTag.tag", "tags/DeclaredAttributesTag.tag");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/DynamicAttributesTag.tag", "tags/DynamicAttributesTag.tag");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/JspContextWrapperScopesTag.tag", "tags/JspContextWrapperScopesTag.tag");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/JspContextWrapperTag.tag", "tags/JspContextWrapperTag.tag");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/semanticsInvokeClassicTag.tag", "tags/semanticsInvokeClassicTag.tag");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/semanticsInvokeSimpleTag.tag", "tags/semanticsInvokeSimpleTag.tag");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/semanticsJspForward.tag", "tags/semanticsJspForward.tag");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/mytags.tld", "mytags.tld");
    
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspTagSemanticsAttributeNotSpecifiedTest.jsp")), "JspTagSemanticsAttributeNotSpecifiedTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspTagSemanticsDeclaredAttributesTest.jsp")), "JspTagSemanticsDeclaredAttributesTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspTagSemanticsDynamicAttributesTest.jsp")), "JspTagSemanticsDynamicAttributesTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspTagSemanticsJspContextWrapperTest.jsp")), "JspTagSemanticsJspContextWrapperTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspTagSemanticsScopesTest.jsp")), "JspTagSemanticsScopesTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/semanticsInvokeClassicTag.jsp")), "semanticsInvokeClassicTag.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/semanticsInvokeSimpleTag.jsp")), "semanticsInvokeSimpleTag.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/semanticsJspForward.jsp")), "semanticsJspForward.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/semanticsJspForwardTarget.jsp")), "semanticsJspForwardTarget.jsp");

    return archive;

  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   *
   */

  /* Run test */

  /*
   * @testName: jspTagSemanticsJspContextWrapperTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Validate that the container properly creates a JSP Context
   * wrapper, an instance of PageContext, for the tag file. Validate that this
   * wrapper is not the same JspContext as that of the invoking page (this
   * includes validate of the jspContext scripting variable).
   */
  @Test
  public void jspTagSemanticsJspContextWrapperTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_tagfile_semantics_web/JspTagSemanticsJspContextWrapperTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    invoke();
  }

  /*
   * @testName: jspTagSemanticsJspContextWrapperScopesTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Validate the following: - the container presents the Tag
   * file with a clean page context. - the container provides the Tag file
   * access with to the same request, session, and application scope as that of
   * the invoking context. - Any changes to the page scope in the wrapper
   * context are not reflected in the invoking context. - Any changes to the
   * request, session, or application scopes of the wrapping context are
   * synchronized with the invoking context.
   */
  @Test
  public void jspTagSemanticsJspContextWrapperScopesTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_tagfile_semantics_web/JspTagSemanticsScopesTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING,
        "Wrapper Test PASSED|Test PASSED|Wrapper Test PASSED|Test PASSED");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    invoke();
  }

  /*
   * @testName: jspTagSemanticsDeclaredAttributesTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Validate that a page scoped variable is created for each
   * declared and specified attribute defined by the tag. The variable name must
   * be the same as the attribute name and the variable value must be the same
   * as provided at invocation time.
   */
  @Test
  public void jspTagSemanticsDeclaredAttributesTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_tagfile_semantics_web/JspTagSemanticsDeclaredAttributesTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    invoke();
  }

  /*
   * @testName: jspTagSemanticsAttributeNotSpecifiedTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Validate that if an attribute is declared as optional, and
   * that attribute is not specified at invocation time, no page scoped variable
   * is created.
   */
  @Test
  public void jspTagSemanticsAttributeNotSpecifiedTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_tagfile_semantics_web/JspTagSemanticsAttributeNotSpecifiedTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    invoke();
  }

  /*
   * @testName: jspTagSemanticsDynamicAttributesTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Validate dynamic attributes.
   */
  @Test
  public void jspTagSemanticsDynamicAttributesTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_tagfile_semantics_web/JspTagSemanticsDynamicAttributesTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    invoke();
  }

  /*
   * @testName: semanticsJspForwardTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Upon return from the RequestDispather.forward method, the
   * generated tag handler must stop processing of the tag file and throw
   * java.servlet.jsp.SkipPageException.
   */

  @Test
  public void semanticsJspForwardTest() throws Exception {
    String testName = "semanticsJspForward";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + "/jsp_tagfile_semantics_web/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    invoke();
  }

  /*
   * @testName: semanticsInvokeSimpleTagTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: invokes a simple tag handler which throws SkipPageException
   * in the doTag method, the generated tag handler must terminate and
   * SkipPageException must be thrown.
   */

  @Test
  public void semanticsInvokeSimpleTagTest() throws Exception {
    String testName = "semanticsInvokeSimpleTag";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + "/jsp_tagfile_semantics_web/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    invoke();
  }

  /*
   * @testName: semanticsInvokeClassicTagTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: must allow such an invocation to occur.
   */

  @Test
  public void semanticsInvokeClassicTagTest() throws Exception {
    String testName = "semanticsInvokeClassicTag";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + "/jsp_tagfile_semantics_web/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING,
        "MyClassicTag:Test PASSED|endOfTagFile|endOfCallingPage");
    invoke();
  }

}
