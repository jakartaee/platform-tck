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

package ee.jakarta.tck.pages.spec.tagfiles.directives.tag20;


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


  private static String CONTEXT_ROOT = "/jsp_tagfile_directives_tag20_web";

  public URLClientIT() throws Exception {


    setGeneralURI("/jsp/spec/tagfiles/directives/tag20");
    setContextRoot("/jsp_tagfile_directives_tag20_web");

  }

  @Deployment(testable = false)
  public static WebArchive createDeployment() throws IOException {
    
    String packagePath = URLClientIT.class.getPackageName().replace(".", "/");
    WebArchive archive = ShrinkWrap.create(WebArchive.class, "jsp_tagfile_directives_tag20_web.war");
    archive.setWebXML(URLClientIT.class.getClassLoader().getResource(packagePath+"/jsp_tagfile_directives_tag20_web.xml"));

    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/deferredValueMinimumJspVersion.tag", "tags/deferredValueMinimumJspVersion.tag");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/dynamicAttributesNoUri.tag", "tags/dynamicAttributesNoUri.tag");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/implicitImportHttp.tag", "tags/implicitImportHttp.tag");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/implicitImportJsp.tag", "tags/implicitImportJsp.tag");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/implicitImportLang.tag", "tags/implicitImportLang.tag");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/implicitImportServlet.tag", "tags/implicitImportServlet.tag");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/negativeBodyContent.tag", "tags/negativeBodyContent.tag");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/negativeDuplicateBodyContent.tag", "tags/negativeDuplicateBodyContent.tag");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/negativeDuplicateDescription.tag", "tags/negativeDuplicateDescription.tag");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/negativeDuplicateDisplayName.tag", "tags/negativeDuplicateDisplayName.tag");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/negativeDuplicateDynamicAttributes.tag", "tags/negativeDuplicateDynamicAttributes.tag");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/negativeDuplicateExample.tag", "tags/negativeDuplicateExample.tag");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/negativeDuplicateIsELIgnoredFatalTranslationError.tag", "tags/negativeDuplicateIsELIgnoredFatalTranslationError.tag");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/negativeDuplicateLanguageFatalTranslationError.tag", "tags/negativeDuplicateLanguageFatalTranslationError.tag");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/negativeDuplicateLargeIcon.tag", "tags/negativeDuplicateLargeIcon.tag");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/negativeDuplicateSmallIcon.tag", "tags/negativeDuplicateSmallIcon.tag");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/negativeMultiplePageEncoding.tag", "tags/negativeMultiplePageEncoding.tag");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/negativeUnrecognizedAttribute.tag", "tags/negativeUnrecognizedAttribute.tag");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/positiveDuplicateAttributes.tag", "tags/positiveDuplicateAttributes.tag");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/positiveDuplicateAttributes.tagf", "tags/positiveDuplicateAttributes.tagf");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/positiveImport.tag", "tags/positiveImport.tag");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/positiveLang.tag", "tags/positiveLang.tag");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/positiveMultipleImport.tag", "tags/positiveMultipleImport.tag");

    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/images/16/icon.jpg")), "images/16/icon.jpg");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/images/32/icon.jpg")), "images/32/icon.jpg");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/deferredValueMinimumJspVersion.jsp")), "deferredValueMinimumJspVersion.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/dynamicAttributesNoUri.jsp")), "dynamicAttributesNoUri.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/implicitImportHttp.jsp")), "implicitImportHttp.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/implicitImportJsp.jsp")), "implicitImportJsp.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/implicitImportLang.jsp")), "implicitImportLang.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/implicitImportServlet.jsp")), "implicitImportServlet.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/negativeBodyContent.jsp")), "negativeBodyContent.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/negativeDuplicateBodyContent.jsp")), "negativeDuplicateBodyContent.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/negativeDuplicateDescription.jsp")), "negativeDuplicateDescription.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/negativeDuplicateDisplayName.jsp")), "negativeDuplicateDisplayName.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/negativeDuplicateDynamicAttributes.jsp")), "negativeDuplicateDynamicAttributes.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/negativeDuplicateExample.jsp")), "negativeDuplicateExample.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/negativeDuplicateIsELIgnoredFatalTranslationError.jsp")), "negativeDuplicateIsELIgnoredFatalTranslationError.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/negativeDuplicateLanguageFatalTranslationError.jsp")), "negativeDuplicateLanguageFatalTranslationError.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/negativeDuplicateLargeIcon.jsp")), "negativeDuplicateLargeIcon.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/negativeDuplicateSmallIcon.jsp")), "negativeDuplicateSmallIcon.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/negativeMultiplePageEncoding.jsp")), "negativeMultiplePageEncoding.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/negativeUnrecognizedAttribute.jsp")), "negativeUnrecognizedAttribute.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/positiveDuplicateAttributes.jsp")), "positiveDuplicateAttributes.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/positiveImport.jsp")), "positiveImport.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/positiveLang.jsp")), "positiveLang.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/positiveMultipleImport.jsp")), "positiveMultipleImport.jsp");

    return archive;

  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   *
   */

  /* Run test */

  /*
   * @testName: negativeDuplicateIsELIgnoredFatalTranslationErrorTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Declare a tag directive with two isELIgnored attributes.
   */

  @Test
  public void negativeDuplicateIsELIgnoredFatalTranslationErrorTest()
      throws Exception {
    String testName = "negativeDuplicateIsELIgnoredFatalTranslationError";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: positiveImportTest
   * 
   * @assertion_ids: JSP:SPEC:229.19
   * 
   * @test_Strategy: Use the import attribute to import 'java.util.ArrayList'.
   * Validated that a ArrayList object can be created and used.
   */

  @Test
  public void positiveImportTest() throws Exception {
    String testName = "positiveImport";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: implicitImportLangTest
   * 
   * @assertion_ids: JSP:SPEC:229.19.1
   * 
   * @test_Strategy: Validate that classes from the java.lang package are
   * implicitly imported by creating and using a java.lang.Integer object.
   */

  @Test
  public void implicitImportLangTest() throws Exception {
    String testName = "implicitImportLang";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: implicitImportJspTest
   * 
   * @assertion_ids: JSP:SPEC:229.19.1
   * 
   * @test_Strategy: Validate that classes from the jakarta.servlet.jsp package
   * are implicitly imported by calling JspFactory.getDefaultFactory() method.
   */

  @Test
  public void implicitImportJspTest() throws Exception {
    String testName = "implicitImportJsp";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: implicitImportServletTest
   * 
   * @assertion_ids: JSP:SPEC:229.19.1
   * 
   * @test_Strategy: Validate that classes from the jakarta.servlet package are
   * implicitly imported by creating and using an instance of RequestDispatcher.
   */

  @Test
  public void implicitImportServletTest() throws Exception {
    String testName = "implicitImportServlet";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: implicitImportHttpTest
   * 
   * @assertion_ids: JSP:SPEC:229.19.1
   * 
   * @test_Strategy: Validate that classes from the jakarta.servlet.http package
   * are implicitly imported by creating and using an instance of Cookie.
   */

  @Test
  public void implicitImportHttpTest() throws Exception {
    String testName = "implicitImportHttp";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: positiveMultipleImportTest
   * 
   * @assertion_ids: JSP:SPEC:229.19.2
   * 
   * @test_Strategy: Declare a tag directive with two import attributes.
   *
   */

  @Test
  public void positiveMultipleImportTest() throws Exception {
    String testName = "positiveMultipleImport";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: negativeMultiplePageEncodingTest
   * 
   * @assertion_ids: JSP:SPEC:232.1.21
   * 
   * @test_Strategy: Declare a tag directive with two pageEncoding attributes.
   *
   */

  @Test
  public void negativeMultiplePageEncodingTest() throws Exception {
    String testName = "negativeMultiplePageEncoding";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: positiveLangTest
   * 
   * @assertion_ids: JSP:SPEC:229.17
   * 
   * @test_Strategy: Validate that the language attribute can be set to "java"
   * without an error.
   */

  @Test
  public void positiveLangTest() throws Exception {
    String testName = "positiveLang";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: negativeDuplicateLanguageFatalTranslationErrorTest
   * 
   * @assertion_ids: JSP:SPEC:229.18
   * 
   * @test_Strategy: Declare a tag directive with two language attributes. of
   * different values. Validate that a fatal translation error occurs.
   */

  @Test
  public void negativeDuplicateLanguageFatalTranslationErrorTest()
      throws Exception {
    String testName = "negativeDuplicateLanguageFatalTranslationError";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: negativeBodyContentTest
   * 
   * @assertion_ids: JSP:SPEC:232.1.5.5
   * 
   * @test_Strategy: A translation error will result if JSP is used as tag
   * directive body-content
   */

  @Test
  public void negativeBodyContentTest() throws Exception {
    String testName = "negativeBodyContent";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: positiveDuplicateAttributesTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: There shall be only one occurrence of any attribute /value
   * defined in a given translation unit, unless the values for the duplicate
   * attributes are identical for all occurences.
   */

  @Test
  public void positiveDuplicateAttributesTest() throws Exception {
    String testName = "positiveDuplicateAttributes";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "tag invoked|Test PASSED");
    invoke();
  }

  /*
   * @testName: negativeUnrecognizedAttributeTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Unrecognized attributes or values result in fatal
   * translation error.
   */

  @Test
  public void negativeUnrecognizedAttributeTest() throws Exception {
    String testName = "negativeUnrecognizedAttribute";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: negativeDuplicateDisplayNameTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: other such multiple attribute/value (re) definitions result
   * in a fatal translation error if the value do not match.
   */

  @Test
  public void negativeDuplicateDisplayNameTest() throws Exception {
    String testName = "negativeDuplicateDisplayName";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: negativeDuplicateBodyContentTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: other such multiple attribute/value (re) definitions result
   * in a fatal translation error if the value do not match.
   */

  @Test
  public void negativeDuplicateBodyContentTest() throws Exception {
    String testName = "negativeDuplicateBodyContent";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: negativeDuplicateDynamicAttributesTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: other such multiple attribute/value (re) definitions result
   * in a fatal translation error if the value do not match.
   */

  @Test
  public void negativeDuplicateDynamicAttributesTest() throws Exception {
    String testName = "negativeDuplicateDynamicAttributes";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: negativeDuplicateDescriptionTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: other such multiple attribute/value (re) definitions result
   * in a fatal translation error if the value do not match.
   */

  @Test
  public void negativeDuplicateDescriptionTest() throws Exception {
    String testName = "negativeDuplicateDescription";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: negativeDuplicateExampleTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: other such multiple attribute/value (re) definitions result
   * in a fatal translation error if the value do not match.
   */

  @Test
  public void negativeDuplicateExampleTest() throws Exception {
    String testName = "negativeDuplicateExample";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: negativeDuplicateSmallIconTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: other such multiple attribute/value (re) definitions result
   * in a fatal translation error if the value do not match.
   */

  @Test
  public void negativeDuplicateSmallIconTest() throws Exception {
    String testName = "negativeDuplicateSmallIcon";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: negativeDuplicateLargeIconTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: other such multiple attribute/value (re) definitions result
   * in a fatal translation error if the value do not match.
   */

  @Test
  public void negativeDuplicateLargeIconTest() throws Exception {
    String testName = "negativeDuplicateLargeIcon";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: dynamicAttributesNoUriTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Only dynamic attributes with no uri are to be present in
   * the Map; all other are ignored.
   */

  @Test
  public void dynamicAttributesNoUriTest() throws Exception {
    String testName = "dynamicAttributesNoUri";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    invoke();
  }

  /*
   * @testName: deferredValueMinimumJspVersionTest
   * 
   * @assertion_ids: JSP:SPEC:230.8.3
   * 
   * @test_Strategy: [deferredValueMinimumJspVersion] Show that the use of the
   * deferredValue attribute for the tag directive causes a translation error if
   * specified in a tag file with a JSP version less than 2.1.
   */

  @Test
  public void deferredValueMinimumJspVersionTest() throws Exception {
    String testName = "deferredValueMinimumJspVersion";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }
}
