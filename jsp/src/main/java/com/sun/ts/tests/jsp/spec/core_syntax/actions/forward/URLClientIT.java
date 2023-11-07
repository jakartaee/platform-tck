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

package com.sun.ts.tests.jsp.spec.core_syntax.actions.forward;



import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

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



  public static String packagePath = URLClientIT.class.getPackageName().replace(".", "/");

  public URLClientIT() throws Exception {
    setup();

    setGeneralURI("/jsp/spec/core_syntax/actions/forward");
    setContextRoot("/jsp_coresyntx_act_forward_web");

  }

  @Deployment(testable = false)
  public static WebArchive createDeployment() throws IOException {
    
    WebArchive archive = ShrinkWrap.create(WebArchive.class, "jsp_coresyntx_act_forward_web.war");
    archive.setWebXML(URLClientIT.class.getClassLoader().getResource(packagePath+"/jsp_coresyntx_act_forward_web.xml"));
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/unbufferedWriteForwardTest.jsp")), "unbufferedWriteForwardTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/unbufferedWriteForwardTest_error.jsp")), "unbufferedWriteForwardTest_error.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/positiveRequestAttrPageRelative.jsp")), "positiveRequestAttrPageRelative.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/positiveRequestAttrCtxRelative.jsp")), "positiveRequestAttrCtxRelative.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/positiveForwardPageRelativeHtml.jsp")), "positiveForwardPageRelativeHtml.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/positiveForwardPageRelative.jsp")), "positiveForwardPageRelative.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/positiveForwardCtxRelativeHtml.jsp")), "positiveForwardCtxRelativeHtml.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/positiveForwardCtxRelative.jsp")), "positiveForwardCtxRelative.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/forwardcommon.jsp")), "forwardcommon.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/forwardcommon.html")), "forwardcommon.html");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/flushedBufferForwardTest1.jsp")), "flushedBufferForwardTest1.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/flushedBufferForwardTest.jsp")), "flushedBufferForwardTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/flushedBufferForwardTest_error.jsp")), "flushedBufferForwardTest_error.jsp");
  
    return archive;

  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   *
   */

  /* Run test */

  /*
   * @testName: positiveForwardCtxRelativeTest
   * 
   * @assertion_ids: JSP:SPEC:165.1
   * 
   * @test_Strategy: Validate that jsp:forward can forward a request to a JSP
   * page within the same context using a page relative-path. PENDING Merege
   * existing forward tests
   */

  @Test
  public void positiveForwardCtxRelativeTest() throws Exception {
    InputStream gfStream = URLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveForwardCtxRelative.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveForwardCtxRelative");
    invoke();
  }

  /*
   * @testName: positiveForwardCtxRelativeHtmlTest
   * 
   * @assertion_ids: JSP:SPEC:165.1
   * 
   * @test_Strategy: Validate that jsp:forward can forward a request to a static
   * resource within the same context using a page-relative path.
   */

  @Test
  public void positiveForwardCtxRelativeHtmlTest() throws Exception {
    InputStream gfStream = URLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveForwardCtxRelativeHtml.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveForwardCtxRelativeHtml");
    invoke();
  }

  /*
   * @testName: positiveForwardPageRelativeTest
   * 
   * @assertion_ids: JSP:SPEC:165.1
   * 
   * @test_Strategy: Validate that jsp:forward can forward a request to a JSP
   * page within the same context using a page-relative path.
   */

  @Test
  public void positiveForwardPageRelativeTest() throws Exception {
    InputStream gfStream = URLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveForwardPageRelative.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveForwardPageRelative");
    invoke();
  }

  /*
   * @testName: positiveForwardPageRelativeHtmlTest
   * 
   * @assertion_ids: JSP:SPEC:165.1
   * 
   * @test_Strategy: Validate that jsp:forward can forward a request to a static
   * resource within the same context using a page-relative path.
   */

  @Test
  public void positiveForwardPageRelativeHtmlTest() throws Exception {
    InputStream gfStream = URLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveForwardPageRelativeHtml.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveForwardPageRelativeHtml");
    invoke();
  }

  /*
   * @testName: positiveRequestAttrCtxRelativeTest
   * 
   * @assertion_ids: JSP:SPEC:165.8
   * 
   * @test_Strategy: Validate that jsp:forward can properly accept a
   * request-time attribute containing a context-relative path value.
   */

  @Test
  public void positiveRequestAttrCtxRelativeTest() throws Exception {
    InputStream gfStream = URLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveRequestAttrCtxRelative.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveRequestAttrCtxRelative");
    invoke();
  }

  /*
   * @testName: positiveRequestAttrPageRelativeTest
   * 
   * @assertion_ids: JSP:SPEC:165.8
   * 
   * @test_Strategy: Validate that jsp:forward can properly accept a
   * request-time attribute containing a page-relative path value.
   */

  @Test
  public void positiveRequestAttrPageRelativeTest() throws Exception {
    InputStream gfStream = URLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveRequestAttrPageRelative.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveRequestAttrPageRelative");
    invoke();
  }

  /*
   * @testName: unbufferedWriteForwardTest
   * 
   * @assertion_ids: JSP:SPEC:165.5
   * 
   * @test_Strategy:If the page output was unbuffered and anything has been
   * written to it, an attempt to forward the request will result in an
   * IllegalStateException.
   */

  @Test
  public void unbufferedWriteForwardTest() throws Exception {
    String testName = "unbufferedWriteForwardTest";
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_coresyntx_act_forward_web/unbufferedWriteForwardTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Got IllegalStateException");
    invoke();
  }

}
