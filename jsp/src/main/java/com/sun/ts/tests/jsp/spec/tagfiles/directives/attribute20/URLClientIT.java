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
 * @(#)URLClient.java	1.1 03/09/16
 */

package com.sun.ts.tests.jsp.spec.tagfiles.directives.attribute20;


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


  private static final String CONTEXT_ROOT = "/jsp_tagfile_directives_attribute20_web";

  public URLClientIT() throws Exception {
    setup();

    setContextRoot(CONTEXT_ROOT);

  }

  @Deployment(testable = false)
  public static WebArchive createDeployment() throws IOException {
    
    String packagePath = URLClientIT.class.getPackageName().replace(".", "/");
    WebArchive archive = ShrinkWrap.create(WebArchive.class, "jsp_tagfile_directives_attribute20_web.war");
    archive.setWebXML(URLClientIT.class.getClassLoader().getResource(packagePath+"/jsp_tagfile_directives_attribute20_web.xml"));
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/negativeType.tag", "tags/negativeType.tag");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/negativeName3Include.tagf", "tags/negativeName3Include.tagf");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/negativeName3Include.tag", "tags/negativeName3Include.tag");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/negativeName3.tag", "tags/negativeName3.tag");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/negativeName2Include.tagf", "tags/negativeName2Include.tagf");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/negativeName2Include.tag", "tags/negativeName2Include.tag");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/negativeName2.tag", "tags/negativeName2.tag");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/negativeName1Include.tagf", "tags/negativeName1Include.tagf");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/negativeName1Include.tag", "tags/negativeName1Include.tag");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/negativeName1.tag", "tags/negativeName1.tag");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/negativeFragmentType.tag", "tags/negativeFragmentType.tag");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/negativeFragmentRtexprvalue.tag", "tags/negativeFragmentRtexprvalue.tag");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/deferredValueTypeMinimumJspVersion.tag", "tags/deferredValueTypeMinimumJspVersion.tag");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/deferredValueMinimumJspVersion.tag", "tags/deferredValueMinimumJspVersion.tag");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/deferredMethodSignatureMinimumJspVersion.tag", "tags/deferredMethodSignatureMinimumJspVersion.tag");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/deferredMethodMinimumJspVersion.tag", "tags/deferredMethodMinimumJspVersion.tag");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/defaultType.tag", "tags/defaultType.tag");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/defaultRtexprvalue.tag", "tags/defaultRtexprvalue.tag");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/defaultRequired.tag", "tags/defaultRequired.tag");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/defaultFragment.tag", "tags/defaultFragment.tag");

    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/negativeType.jsp")), "negativeType.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/negativeName3Include.jsp")), "negativeName3Include.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/negativeName3.jsp")), "negativeName3.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/negativeName2Include.jsp")), "negativeName2Include.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/negativeName2.jsp")), "negativeName2.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/negativeName1Include.jsp")), "negativeName1Include.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/negativeName1.jsp")), "negativeName1.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/negativeFragmentType.jsp")), "negativeFragmentType.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/negativeFragmentRtexprvalue.jsp")), "negativeFragmentRtexprvalue.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/deferredValueTypeMinimumJspVersion.jsp")), "deferredValueTypeMinimumJspVersion.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/deferredValueMinimumJspVersion.jsp")), "deferredValueMinimumJspVersion.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/deferredMethodSignatureMinimumJspVersion.jsp")), "deferredMethodSignatureMinimumJspVersion.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/deferredMethodMinimumJspVersion.jsp")), "deferredMethodMinimumJspVersion.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/defaultType.jsp")), "defaultType.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/defaultRtexprvalue.jsp")), "defaultRtexprvalue.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/defaultRequired.jsp")), "defaultRequired.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/defaultFragment.jsp")), "defaultFragment.jsp");
    return archive;

  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   *
   */

  /* Run test */

  /*
   * @testName: negativeName1Test
   * 
   * @assertion_ids: JSP:SPEC:230.2
   * 
   * @test_Strategy: A translation error must result if more than one attribute
   * directive appears in the same translation unit with the same name
   */

  @Test
  public void negativeName1Test() throws Exception {
    String testName = "negativeName1";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: negativeName1IncludeTest
   * 
   * @assertion_ids: JSP:SPEC:230.2
   * 
   * @test_Strategy: A translation error must result if more than one attribute
   * directive appears in the same translation unit with the same name
   */

  @Test
  public void negativeName1IncludeTest() throws Exception {
    String testName = "negativeName1Include";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: negativeName2Test
   * 
   * @assertion_ids: JSP:SPEC:230.1.2
   * 
   * @test_Strategy: A translation error must result if the attribute name
   * equals to the name-given of a variable.
   */

  @Test
  public void negativeName2Test() throws Exception {
    String testName = "negativeName2";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: negativeName2IncludeTest
   * 
   * @assertion_ids: JSP:SPEC:230.1.2
   * 
   * @test_Strategy: A translation error must result if the attribute name
   * equals to the name-given of a variable.
   */

  @Test
  public void negativeName2IncludeTest() throws Exception {
    String testName = "negativeName2Include";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: negativeName3Test
   * 
   * @assertion_ids: JSP:SPEC:230.1.2
   * 
   * @test_Strategy: A translation error must result if the attribute name
   * equals to dynamic-attributes of a tag directive.
   */

  @Test
  public void negativeName3Test() throws Exception {
    String testName = "negativeName3";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: negativeName3IncludeTest
   * 
   * @assertion_ids: JSP:SPEC:230.1.2
   * 
   * @test_Strategy: A translation error must result if the attribute name
   * equals to dynamic-attributes of a tag directive.
   */

  @Test
  public void negativeName3IncludeTest() throws Exception {
    String testName = "negativeName3Include";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: defaultRequiredTest
   * 
   * @assertion_ids: JSP:SPEC:230.3.3
   * 
   * @test_Strategy: required default is false
   */

  @Test
  public void defaultRequiredTest() throws Exception {
    String testName = "defaultRequired";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: defaultTypeTest
   * 
   * @assertion_ids: JSP:SPEC:230.6.1
   * 
   * @test_Strategy: type defaults to java.lang.String, and also verify an Float
   * type attribute.
   */

  @Test
  public void defaultTypeTest() throws Exception {
    String testName = "defaultType";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED|Test PASSED");
    invoke();
  }

  /*
   * @testName: negativeTypeTest
   * 
   * @assertion_ids: JSP:SPEC:230.6.2
   * 
   * @test_Strategy: A translation error must result if the type is a primitive
   */

  @Test
  public void negativeTypeTest() throws Exception {
    String testName = "negativeType";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: negativeFragmentTypeTest
   * 
   * @assertion_ids: JSP:SPEC:230.4.5
   * 
   * @test_Strategy: A translation error must result if fragment is true and
   * type is specified
   */

  @Test
  public void negativeFragmentTypeTest() throws Exception {
    String testName = "negativeFragmentType";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: negativeFragmentRtexprvalueTest
   * 
   * @assertion_ids: JSP:SPEC:230.4.2.1
   * 
   * @test_Strategy: A translation error must result if fragment is true and
   * rtexprvalue is specified
   */

  @Test
  public void negativeFragmentRtexprvalueTest() throws Exception {
    String testName = "negativeFragmentRtexprvalue";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: defaultFragmentTest
   * 
   * @assertion_ids: JSP:SPEC:230
   * 
   * @test_Strategy: fragment defaults to false. Also verifies the default attr
   * type is String.
   */

  @Test
  public void defaultFragmentTest() throws Exception {
    String testName = "defaultFragment";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: defaultRtexprvalueTest
   * 
   * @assertion_ids: JSP:SPEC:230
   * 
   * @test_Strategy: rtexprvalue defaults to true.
   */

  @Test
  public void defaultRtexprvalueTest() throws Exception {
    String testName = "defaultRtexprvalue";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: deferredValueMinimumJspVersionTest
   * 
   * @assertion_ids: JSP:SPEC:230.8.3
   * 
   * @test_Strategy: [deferredValueMinimumJspVersion] The deferredValue
   * attribute causes a translation error if specified in a tag file with a JSP
   * version less than 2.1.
   */

  @Test
  public void deferredValueMinimumJspVersionTest() throws Exception {
    String testName = "deferredValueMinimumJspVersion";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: deferredValueTypeMinimumJspVersionTest
   * 
   * @assertion_ids: JSP:SPEC:230.9.4
   * 
   * @test_Strategy: [deferredValueTypeMinimumJspVersion] The deferredValueType
   * attribute causes a translation error if specified in a tag file with a JSP
   * version less than 2.1.
   */

  @Test
  public void deferredValueTypeMinimumJspVersionTest() throws Exception {
    String testName = "deferredValueTypeMinimumJspVersion";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: deferredMethodMinimumJspVersionTest
   * 
   * @assertion_ids: JSP:SPEC:230.10.2
   * 
   * @test_Strategy: [deferredMethodMinimumJspVersion] The deferredMethod
   * attribute causes a translation error if specified in a tag file with a JSP
   * version less than 2.1.
   */

  @Test
  public void deferredMethodMinimumJspVersionTest() throws Exception {
    String testName = "deferredMethodMinimumJspVersion";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: deferredMethodSignatureMinimumJspVersionTest
   * 
   * @assertion_ids: JSP:SPEC:230.11.3
   * 
   * @test_Strategy: [deferredMethodSignatureMinimumJspVersion] The
   * deferredMethodSignature attribute causes a translation error if specified
   * in a tag file with a JSP version less than 2.1.
   */

  @Test
  public void deferredMethodSignatureMinimumJspVersionTest() throws Exception {
    String testName = "deferredMethodSignatureMinimumJspVersion";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }
}
