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

package ee.jakarta.tck.pages.spec.core_syntax.scripting.expressions;


import java.io.IOException;
import java.io.InputStream;

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


  
  public static String packagePath = URLClientIT.class.getPackageName().replace(".", "/");

  public URLClientIT() throws Exception {


    setGeneralURI("/jsp/spec/core_syntax/scripting/expressions");
    setContextRoot("/jsp_coresyntx_script_expressions_web");

  }

  @Deployment(testable = false)
  public static WebArchive createDeployment() throws IOException {
    
    WebArchive archive = ShrinkWrap.create(WebArchive.class, "jsp_coresyntx_script_expressions_web.war");
    archive.setWebXML(URLClientIT.class.getClassLoader().getResource(packagePath+"/jsp_coresyntx_script_expressions_web.xml"));
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/positiveExprWhiteSpace.jsp")), "positiveExprWhiteSpace.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/positiveExprComment.jsp")), "positiveExprComment.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/positiveExpr.jsp")), "positiveExpr.jsp");

    return archive;
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   *
   */

  /* Run test */

  /*
   * @testName: positiveExprTest
   * 
   * @assertion_ids: JSP:SPEC:77
   * 
   * @test_Strategy: Validate that the container can correctly support a basic
   * expression by validating the output returned.
   */

  @Test
  public void positiveExprTest() throws Exception {
    InputStream gfStream = URLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveExpr.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveExpr");
    invoke();
  }

  /*
   * @testName: positiveExprCommentTest
   * 
   * @assertion_ids: JSP:SPEC:5
   * 
   * @test_Strategy: Validate that an HTML stye comment with an embedded
   * expression returns the value of the expression within the comment and that
   * the HTML comment is treated as template text.
   */

  @Test
  public void positiveExprCommentTest() throws Exception {
    InputStream gfStream = URLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveExprComment.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveExprComment");
    invoke();
  }

  /*
   * @testName: positiveExprWhiteSpaceTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Validate that the container correctly handles different
   * whitespace values with an expression element.
   */

  @Test
  public void positiveExprWhiteSpaceTest() throws Exception {
    InputStream gfStream = URLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveExprWhiteSpace.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveExprWhiteSpace");
    invoke();
  }

}
