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
 * @(#)URLClient.java	
 */

package com.sun.ts.tests.jsp.api.jakarta_el.valexpression;

import com.sun.ts.tests.jsp.common.client.AbstractUrlClient;

import java.io.IOException;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.jboss.shrinkwrap.api.asset.UrlAsset;


@ExtendWith(ArquillianExtension.class)
public class URLClientIT extends AbstractUrlClient {



  public URLClientIT() throws Exception {
    setup();
    setContextRoot("/jsp_valexpr_web");
    setTestJsp("ValueExpressionTest");
  }

  @Deployment(testable = false)
  public static WebArchive createDeployment() throws IOException {

    String packagePath = URLClientIT.class.getPackageName().replace(".", "/");
    WebArchive archive = ShrinkWrap.create(WebArchive.class, "jsp_valexpr_web.war");
    archive.addClasses(ValueExpressionTag.class,
            com.sun.ts.tests.jsp.common.util.JspTestUtil.class,
            com.sun.ts.tests.jsp.common.tags.tck.SetTag.class,
            com.sun.ts.tests.common.el.api.expression.ExpressionTest.class);
    archive.setWebXML(URLClientIT.class.getClassLoader().getResource(packagePath+"/jsp_valexpr_web.xml"));
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/valexpression.tld", "valexpression.tld");    
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/ValueExpressionTest.jsp")), "ValueExpressionTest.jsp");

    return archive;

  }

  
  /* Run tests */

  // ============================================ Tests ======

  /*
   * @testName: valueExpressionEvaluateTest
   * 
   * @assertion_ids: EL:JAVADOC:110; EL:JAVADOC:111; EL:JAVADOC:112;
   * EL:JAVADOC:113; EL:JAVADOC:114; EL:JAVADOC:60
   * 
   * @test_Strategy: Validate the behavior of ValueExpression API and
   * Expression.isLiteralText() ValueExpression.getValue()
   * ValueExpression.setValue() ValueExpression.getType()
   * ValueExpression.getExpectedType() ValueExpression.isReadOnly()
   * Expression.isLiteralText()
   */
  @Test
  public void valueExpressionEvaluateTest() throws Exception {
    TEST_PROPS.setProperty(APITEST, "valueExpressionEvaluateTest");
    invoke();
  }
}
