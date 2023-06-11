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

package com.sun.ts.tests.jsp.api.jakarta_servlet.jsp.tagext.pagedata;

import java.io.PrintWriter;

import com.sun.javatest.Status;
import com.sun.ts.tests.jsp.common.client.AbstractUrlClient;

import java.io.IOException;
import java.io.InputStream;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.jboss.shrinkwrap.api.asset.UrlAsset;

/**
 * Test client for TagAttributeInfo. Implementation note, all tests are
 * performed within a TagExtraInfo class. If the test fails, a translation error
 * will be generated and a ValidationMessage array will be returned.
 */

@ExtendWith(ArquillianExtension.class)
public class URLClientIT extends AbstractUrlClient {

  public URLClientIT() throws Exception {
    setup();
  }

  @Deployment(testable = false)
  public static WebArchive createDeployment() throws IOException {

    String packagePath = URLClientIT.class.getPackageName().replace(".", "/");
    WebArchive archive = ShrinkWrap.create(WebArchive.class, "jsp_pagedata_web.war");
    archive.addClasses(PageDataValidator.class, TagFilePageDataValidator.class,
            com.sun.ts.tests.jsp.common.util.JspTestUtil.class,
            com.sun.ts.tests.jsp.common.tags.tck.SimpleTag.class);
    archive.setWebXML(URLClientIT.class.getClassLoader().getResource(packagePath+"/jsp_pagedata_web.xml"));
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/pagedata.tld", "pagedata.tld");    
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/pagedatatagfile.tld", "pagedatatagfile.tld");    
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/pageDataTagFileTest.tag", "tags/pageDataTagFileTest.tag");    
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/template.txt", "tags/template.txt");    
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/PageDataTagFileTest.jsp")), "PageDataTagFileTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/PageDataTest.jsp")), "PageDataTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/template.txt")), "template.txt");
    
    return archive;
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   */

  /* Run tests */

  // ============================================ Tests ======

  /*
   * @testName: pageDataTest
   * 
   * @assertion_ids: JSP:JAVADOC:313
   * 
   * @test_Strategy: Validate the following: - We can get an inputstream from
   * the PageData object provided to the validation method of the
   * TagLibraryValidator. - Validate the XML view of a JSP page: - page
   * directives are jsp:directive.page elements - taglib directives are includes
   * in the namespace declaration in the jsp:root element - include directives
   * are not present in the XML view - template text is wrapped by jsp:text
   * elements - scriptlets are wrapped by jsp:scriptlet elements - declarations
   * are wrapped by jsp:declaration elements - JSP expressions are wrapped by
   * jsp:expression elements - rt expressions are converted from '<%=' '%>' to
   * '%=' '%' - Custom taglib usages are passed through - the jsp:root element
   * is present - the jsp namespace is present in the jsp:root element.
   */
  @Test
  public void pageDataTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_pagedata_web/PageDataTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    TEST_PROPS.setProperty(SEARCH_STRING,
        "Validator Called|Expression Text|Included template text");
    invoke();
  }

  /*
   * @testName: pageDataTagFileTest
   * 
   * @assertion_ids: JSP:JAVADOC:313
   * 
   * @test_Strategy: same as above.
   */
  @Test
  public void pageDataTagFileTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_pagedata_web/PageDataTagFileTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    TEST_PROPS.setProperty(SEARCH_STRING,
        "Validator Called|Expression Text in tag file|Included template text in tag file");
    invoke();
  }

}
