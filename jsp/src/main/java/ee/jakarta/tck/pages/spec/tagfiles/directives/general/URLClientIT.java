/*
 * Copyright (c) 2007, 2021 Oracle and/or its affiliates and others.
 * All rights reserved.
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

package ee.jakarta.tck.pages.spec.tagfiles.directives.general;


import java.io.IOException;
import ee.jakarta.tck.pages.common.client.AbstractUrlClient;

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


  private static final String CONTEXT_ROOT = "/jsp_tagfile_directives_general_web";

  public URLClientIT() throws Exception {


    setContextRoot(CONTEXT_ROOT);

  }

  @Deployment(testable = false)
  public static WebArchive createDeployment() throws IOException {
    
    String packagePath = URLClientIT.class.getPackageName().replace(".", "/");
    WebArchive archive = ShrinkWrap.create(WebArchive.class, "jsp_tagfile_directives_general_web.war");
    archive.setWebXML(URLClientIT.class.getClassLoader().getResource(packagePath+"/jsp_tagfile_directives_general_web.xml"));
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/positiveTaglib.tag", "tags/positiveTaglib.tag");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/positiveIncludePageRelative.tag", "tags/positiveIncludePageRelative.tag");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/positiveIncludeContextRelative.tag", "tags/positiveIncludeContextRelative.tag");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/negativePageDirective.tag", "tags/negativePageDirective.tag");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/negativeInclude.tag", "tags/negativeInclude.tag");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/ErrorOnELNotFoundTrue.tag", "tags/ErrorOnELNotFoundTrue.tag");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/ErrorOnELNotFoundFalse.tag", "tags/ErrorOnELNotFoundFalse.tag");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/include/negativeInclude.tagf", "tags/include/negativeInclude.tagf");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/include/positiveIncludePageRelative.tagf", "tags/include/positiveIncludePageRelative.tagf");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/invoke/positiveTaglibInvokee.tag", "tags/invoke/positiveTaglibInvokee.tag");


    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/positiveTaglib.jsp")), "positiveTaglib.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/positiveIncludePageRelative.jsp")), "positiveIncludePageRelative.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/positiveIncludeContextRelative.jsp")), "positiveIncludeContextRelative.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/negativeVariableDirective.jsp")), "negativeVariableDirective.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/negativeTagDirective.jsp")), "negativeTagDirective.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/negativePageDirective.jsp")), "negativePageDirective.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/negativeInclude.jsp")), "negativeInclude.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/negativeAttributeDirective.jsp")), "negativeAttributeDirective.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/ErrorOnELNotFoundTrue.jsp")), "ErrorOnELNotFoundTrue.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/ErrorOnELNotFoundFalse.jsp")), "ErrorOnELNotFoundFalse.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/include/positiveIncludeContextRelative.tagf")), "include/positiveIncludeContextRelative.tagf");
    
    return archive;

  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   *
   */

  /* Run test */

  /*
   * @testName: negativePageDirectiveTest
   * 
   * @assertion_ids: JSP:SPEC:223
   * 
   * @test_Strategy: If a page directive is used in a tag file, a translation
   * error must result.
   */

  @Test
  public void negativePageDirectiveTest() throws Exception {
    String testName = "negativePageDirective";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: negativeTagDirectiveTest
   * 
   * @assertion_ids: JSP:SPEC:226
   * 
   * @test_Strategy: If a tag directive is used in a jsp page, a translation
   * error must result.
   */

  @Test
  public void negativeTagDirectiveTest() throws Exception {
    String testName = "negativeTagDirective";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: negativeAttributeDirectiveTest
   * 
   * @assertion_ids: JSP:SPEC:227
   * 
   * @test_Strategy: If a attribute directive is used in a jsp page, a
   * translation error must result.
   */

  @Test
  public void negativeAttributeDirectiveTest() throws Exception {
    String testName = "negativeAttributeDirective";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: negativeVariableDirectiveTest
   * 
   * @assertion_ids: JSP:SPEC:228
   * 
   * @test_Strategy: If a variable directive is used in a jsp page, a
   * translation error must result.
   */

  @Test
  public void negativeVariableDirectiveTest() throws Exception {
    String testName = "negativeVariableDirective";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: positiveIncludeContextRelativeTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: use tag include directive with a context relative path.
   */

  @Test
  public void positiveIncludeContextRelativeTest() throws Exception {
    String testName = "positiveIncludeContextRelative";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: positiveIncludePageRelativeTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: use tag include directive with a page relative path.
   */

  @Test
  public void positiveIncludePageRelativeTest() throws Exception {
    String testName = "positiveIncludePageRelative";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: negativeIncludeTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: use tag include directive to include unsuitable content
   */

  @Test
  public void negativeIncludeTest() throws Exception {
    String testName = "negativeInclude";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: positiveTaglibTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: invoke a tag file from within a tag file.
   */

  @Test
  public void positiveTaglibTest() throws Exception {
    String testName = "positiveTaglib";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED|from invokee");
    invoke();
  }

  /*
   * @testName: errorOnELNotFoundFalseTest
   * 
   * @assertion_ids: JSP:SPEC:320
   * 
   * @test_Strategy: [ErrorOnELNotFoundTagDirective] Verify that when the
   * ErrorOnELNotFound tag directive attribute is set to false, a reference
   * to an unresolved identifier results in the empty string being used.
   */
  @Test
  public void errorOnELNotFoundFalseTest()
      throws Exception {
    String testName = "ErrorOnELNotFoundFalse";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.0");
    invoke();
  }
  
  /*
   * @testName: errorOnELNotFoundTrueTest
   * 
   * @assertion_ids: JSP:SPEC:320
   * 
   * @test_Strategy: [ErrorOnELNotFoundTagDirective] Verify that when the
   * ErrorOnELNotFound tag directive attribute is set to false, a reference
   * to an unresolved identifier results in the empty string being used.
   */
  @Test
  public void errorOnELNotFoundTrueTest()
      throws Exception {
    String testName = "ErrorOnELNotFoundTrue";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.0");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }
}
