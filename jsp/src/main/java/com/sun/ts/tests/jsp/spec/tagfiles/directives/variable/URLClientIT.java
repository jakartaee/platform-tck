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

package com.sun.ts.tests.jsp.spec.tagfiles.directives.variable;


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


  private static final String CONTEXT_ROOT = "/jsp_tagfiles_directives_variable_web";

  public URLClientIT() throws Exception {
    setup();

    setContextRoot(CONTEXT_ROOT);

  }

  @Deployment(testable = false)
  public static WebArchive createDeployment() throws IOException {
    
    String packagePath = URLClientIT.class.getPackageName().replace(".", "/");
    WebArchive archive = ShrinkWrap.create(WebArchive.class, "jsp_tagfiles_directives_variable_web.war");
    archive.setWebXML(URLClientIT.class.getClassLoader().getResource(packagePath+"/jsp_tagfiles_directives_variable_web.xml"));

    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/falseDeclare.tag", "tags/falseDeclare.tag");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/falseDeclare2.tag", "tags/falseDeclare2.tag");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/negativeAliasAttributeSame.tag", "tags/negativeAliasAttributeSame.tag");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/negativeAliasAttributeSameInclude.tag", "tags/negativeAliasAttributeSameInclude.tag");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/negativeAliasAttributeSameInclude.tagf", "tags/negativeAliasAttributeSameInclude.tagf");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/negativeAliasNameGiven.tag", "tags/negativeAliasNameGiven.tag");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/negativeNameFromAttributeNoAttribute.tag", "tags/negativeNameFromAttributeNoAttribute.tag");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/negativeNameFromAttributeNotRequired.tag", "tags/negativeNameFromAttributeNotRequired.tag");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/negativeNameFromAttributeNotString.tag", "tags/negativeNameFromAttributeNotString.tag");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/negativeNameFromAttributeRtexpr.tag", "tags/negativeNameFromAttributeRtexpr.tag");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/negativeNameFromAttributeSame.tag", "tags/negativeNameFromAttributeSame.tag");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/negativeNameFromAttributeSameInclude.tag", "tags/negativeNameFromAttributeSameInclude.tag");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/negativeNameFromAttributeSameInclude.tagf", "tags/negativeNameFromAttributeSameInclude.tagf");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/negativeNameGivenBoth.tag", "tags/negativeNameGivenBoth.tag");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/negativeNameGivenDynamic.tag", "tags/negativeNameGivenDynamic.tag");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/negativeNameGivenDynamicInclude.tag", "tags/negativeNameGivenDynamicInclude.tag");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/negativeNameGivenDynamicInclude.tagf", "tags/negativeNameGivenDynamicInclude.tagf");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/negativeNameGivenNeither.tag", "tags/negativeNameGivenNeither.tag");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/negativeNameGivenSame.tag", "tags/negativeNameGivenSame.tag");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/negativeNameGivenSameInclude.tag", "tags/negativeNameGivenSameInclude.tag");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/negativeNameGivenSameInclude.tagf", "tags/negativeNameGivenSameInclude.tagf");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/positiveDeclare.tag", "tags/positiveDeclare.tag");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/positiveDeclare2.tag", "tags/positiveDeclare2.tag");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/positiveVariableClass.tag", "tags/positiveVariableClass.tag");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/syncAtBegin.tag", "tags/syncAtBegin.tag");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/syncAtBeginNameFromAttribute.tag", "tags/syncAtBeginNameFromAttribute.tag");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/syncAtEnd.tag", "tags/syncAtEnd.tag");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/syncNested.tag", "tags/syncNested.tag");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/syncRemoveAtBegin.tag", "tags/syncRemoveAtBegin.tag");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/syncRemoveAtEnd.tag", "tags/syncRemoveAtEnd.tag");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/syncRemoveNested.tag", "tags/syncRemoveNested.tag");
    
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/falseDeclareTest.jsp")), "falseDeclareTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/negativeAliasAttributeSame.jsp")), "negativeAliasAttributeSame.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/negativeAliasAttributeSameInclude.jsp")), "negativeAliasAttributeSameInclude.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/negativeAliasNameGiven.jsp")), "negativeAliasNameGiven.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/negativeNameFromAttributeNoAttribute.jsp")), "negativeNameFromAttributeNoAttribute.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/negativeNameFromAttributeNotRequired.jsp")), "negativeNameFromAttributeNotRequired.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/negativeNameFromAttributeNotString.jsp")), "negativeNameFromAttributeNotString.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/negativeNameFromAttributeRtexpr.jsp")), "negativeNameFromAttributeRtexpr.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/negativeNameFromAttributeSame.jsp")), "negativeNameFromAttributeSame.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/negativeNameFromAttributeSameInclude.jsp")), "negativeNameFromAttributeSameInclude.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/negativeNameGivenBoth.jsp")), "negativeNameGivenBoth.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/negativeNameGivenDynamic.jsp")), "negativeNameGivenDynamic.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/negativeNameGivenDynamicInclude.jsp")), "negativeNameGivenDynamicInclude.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/negativeNameGivenNeither.jsp")), "negativeNameGivenNeither.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/negativeNameGivenSame.jsp")), "negativeNameGivenSame.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/negativeNameGivenSameInclude.jsp")), "negativeNameGivenSameInclude.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/positiveDeclare.jsp")), "positiveDeclare.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/positiveVariableClass.jsp")), "positiveVariableClass.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/syncAtBegin.jsp")), "syncAtBegin.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/syncAtBeginNameFromAttribute.jsp")), "syncAtBeginNameFromAttribute.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/syncAtEnd.jsp")), "syncAtEnd.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/syncNested.jsp")), "syncNested.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/syncRemoveAtBegin.jsp")), "syncRemoveAtBegin.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/syncRemoveAtEnd.jsp")), "syncRemoveAtEnd.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/syncRemoveNested.jsp")), "syncRemoveNested.jsp");
    return archive;

  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   *
   */

  /* Run test */

  /*
   * @testName: negativeNameGivenBothTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Either the name-given attribute or the name- from-attribute
   * must be specified. Specifying neither or both will result in a translation
   * error.
   */

  @Test
  public void negativeNameGivenBothTest() throws Exception {
    String testName = "negativeNameGivenBoth";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: negativeNameGivenNeitherTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Either the name-given attribute or the name- from-attribute
   * must be specified. Specifying neither or both will result in a translation
   * error.
   */

  @Test
  public void negativeNameGivenNeitherTest() throws Exception {
    String testName = "negativeNameGivenNeither";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: negativeNameGivenSameTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: a translation error will result if two variable directives
   * have the same name-given.
   */

  @Test
  public void negativeNameGivenSameTest() throws Exception {
    String testName = "negativeNameGivenSame";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: negativeNameGivenSameIncludeTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: a translation error will result if two variable directives
   * have the same name-given in the same translation unit.
   */

  @Test
  public void negativeNameGivenSameIncludeTest() throws Exception {
    String testName = "negativeNameGivenSameInclude";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: negativeNameGivenDynamicTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: a translation error will result if a variable name-given is
   * the same as a dynamic-attributes of a tag directive.
   */

  @Test
  public void negativeNameGivenDynamicTest() throws Exception {
    String testName = "negativeNameGivenDynamic";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: negativeNameGivenDynamicIncludeTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: a translation error will result if a variable name-given is
   * the same as a dynamic-attributes of a tag directive in the same translation
   * unit.
   */

  @Test
  public void negativeNameGivenDynamicIncludeTest() throws Exception {
    String testName = "negativeNameGivenDynamicInclude";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: negativeNameFromAttributeSameTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: a translation error will result if two variable directives
   * have the same name-from-attribute.
   */

  @Test
  public void negativeNameFromAttributeSameTest() throws Exception {
    String testName = "negativeNameFromAttributeSame";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: negativeNameFromAttributeSameIncludeTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: a translation error will result if two variable directives
   * have the same name-from-attribute.
   */

  @Test
  public void negativeNameFromAttributeSameIncludeTest() throws Exception {
    String testName = "negativeNameFromAttributeSameInclude";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: negativeNameFromAttributeNoAttributeTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: a translation error will result the attribute referred to
   * by name-from-attribute does not exist.
   */

  @Test
  public void negativeNameFromAttributeNoAttributeTest() throws Exception {
    String testName = "negativeNameFromAttributeNoAttribute";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: negativeNameFromAttributeNotStringTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: a translation error will result if the attribute referred
   * to by name-from-attribute is not of type java.lang.String
   */

  @Test
  public void negativeNameFromAttributeNotStringTest() throws Exception {
    String testName = "negativeNameFromAttributeNotString";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: negativeNameFromAttributeNotRequiredTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: a translation error will result if the attribute referred
   * to by name-from-attribute is not required.
   */

  @Test
  public void negativeNameFromAttributeNotRequiredTest() throws Exception {
    String testName = "negativeNameFromAttributeNotRequired";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: negativeNameFromAttributeRtexprTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: a translation error will result if the attribute referred
   * to by name-from-attribute is rtexprvalue
   */

  @Test
  public void negativeNameFromAttributeRtexprTest() throws Exception {
    String testName = "negativeNameFromAttributeRtexpr";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: negativeAliasNameGivenTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: a translation error will result if alias is used without
   * name-from-attribute
   */

  @Test
  public void negativeAliasNameGivenTest() throws Exception {
    String testName = "negativeAliasNameGiven";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: negativeAliasAttributeSameTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: a translation error will result if alias is the same as the
   * name of an attribute directive in the same translation unit.
   */

  @Test
  public void negativeAliasAttributeSameTest() throws Exception {
    String testName = "negativeAliasAttributeSame";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: negativeAliasAttributeSameIncludeTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: a translation error will result if alias is the same as the
   * name of an attribute directive in the same translation unit.
   */

  @Test
  public void negativeAliasAttributeSameIncludeTest() throws Exception {
    String testName = "negativeAliasAttributeSameInclude";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: positiveVariableClassTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: variable-class specifies the name of the class of the
   * variable
   */

  @Test
  public void positiveVariableClassTest() throws Exception {
    String testName = "positiveVariableClass";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: syncAtBeginTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: variable synchronization
   */

  @Test
  public void syncAtBeginTest() throws Exception {
    String testName = "syncAtBegin";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "1|2|2|4");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    invoke();
  }

  /*
   * @testName: syncAtBeginNameFromAttributeTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: variable synchronization
   */

  @Test
  public void syncAtBeginNameFromAttributeTest() throws Exception {
    String testName = "syncAtBeginNameFromAttribute";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING,
        "tagFileResult|callingPageResult|ignoredInCallingPage|tagFileResult|4|callingPageResult");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    invoke();
  }

  /*
   * @testName: syncNestedTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: variable synchronization
   */

  @Test
  public void syncNestedTest() throws Exception {
    String testName = "syncNested";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "1|2|2|1");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    invoke();
  }

  /*
   * @testName: syncAtEndTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: variable synchronization
   */

  @Test
  public void syncAtEndTest() throws Exception {
    String testName = "syncAtEnd";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "1|1|2|4");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    invoke();
  }

  /*
   * @testName: syncRemoveNestedTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: variable synchronization
   */

  @Test
  public void syncRemoveNestedTest() throws Exception {
    String testName = "syncRemoveNested";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "2|'1'|''|2");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    invoke();
  }

  /*
   * @testName: syncRemoveAtEndTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: variable synchronization
   */

  @Test
  public void syncRemoveAtEndTest() throws Exception {
    String testName = "syncRemoveAtEnd";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "2|'2'|'2'|''");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    invoke();
  }

  /*
   * @testName: syncRemoveAtBeginTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: variable synchronization
   */

  @Test
  public void syncRemoveAtBeginTest() throws Exception {
    String testName = "syncRemoveAtBegin";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "2|'1'|''|''");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    invoke();
  }

  /*
   * @testName: positiveDeclareTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: if true (default), a corresponding scripting variable is
   * declared in the calling page or tag file.
   */

  @Test
  public void positiveDeclareTest() throws Exception {
    String testName = "positiveDeclare";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "declared2:declared2|Test PASSED");
    invoke();
  }

  /*
   * @testName: falseDeclareTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: if false, a corresponding scripting variable is not
   * declared in the calling page or tag file.
   */

  @Test
  public void falseDeclareTest() throws Exception {
    String testName = "falseDeclareTest";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "was declared2|was declared");
    invoke();
  }

}
