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
 * %W 06/02/11
 */

package ee.jakarta.tck.pages.spec.core_syntax.actions.usebean2;


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


  private static final String CONTEXT_ROOT = "/jsp_coresyntx_act_usebean2_web";

  public URLClientIT() throws Exception {


    setGeneralURI("/jsp/spec/core_syntax/actions/usebean2");
    setContextRoot("/jsp_coresyntx_act_usebean2_web");

  }

  @Deployment(testable = false)
  public static WebArchive createDeployment() throws IOException {
    
    String packagePath = URLClientIT.class.getPackageName().replace(".", "/");
    WebArchive archive = ShrinkWrap.create(WebArchive.class, "jsp_coresyntx_act_usebean2_web.war");
    archive.setWebXML(URLClientIT.class.getClassLoader().getResource(packagePath+"/jsp_coresyntx_act_usebean2_web.xml"));
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/beanBody.tag", "tags/beanBody.tag");    
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/unrestrictedBodyTestInclude.jsp")), "unrestrictedBodyTestInclude.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/unrestrictedBodyTest.jsp")), "unrestrictedBodyTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/noClassNoBeanNameTest.jsp")), "noClassNoBeanNameTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/inScriptlessTest.jsp")), "inScriptlessTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/existingWithBodyTestInclude.jsp")), "existingWithBodyTestInclude.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/existingWithBodyTest.jsp")), "existingWithBodyTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/blockSCopeTest.jsp")), "blockSCopeTest.jsp");

    return archive;

  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   *
   */

  /*
   * @testName: inScriptlessTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: when used in scriptless context, an EL variable is created.
   */

  @Test
  public void inScriptlessTest() throws Exception {
    String testName = "inScriptlessTest";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "[|]|invoke tag|[|]");
    // TEST_PROPS.setProperty(SEARCH_STRING, "invoke tag");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "${list");
    invoke();
  }

  /*
   * @testName: blockSCopeTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: The scope of the scripting language variable is dependent
   * upon the scoping rules and capabilities of the scripting language
   */

  @Test
  public void blockSCopeTest() throws Exception {
    String testName = "blockSCopeTest";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "one");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "two");
    invoke();
  }

  /*
   * @testName: existingWithBodyTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: If the jsp:useBean action had a non-empty body it is
   * ignored if this bean already exists.
   */

  @Test
  public void existingWithBodyTest() throws Exception {
    String testName = "existingWithBodyTest";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "one");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH,
        "This body should be ignored");
    invoke();
  }

  /*
   * @testName: noClassNoBeanNameTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: If the object is not found in the specified scope and
   * neither class nor beanName are given, a java.lang.InstantiationException
   * shall occur.
   */

  @Test
  public void noClassNoBeanNameTest() throws Exception {
    String testName = "noClassNoBeanNameTest";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED|InstantiationException");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    invoke();
  }

  /*
   * @testName: unrestrictedBodyTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: The variable is initialized and available within the scope
   * of the body. Body content is not restricted.
   */

  @Test
  public void unrestrictedBodyTest() throws Exception {
    String testName = "unrestrictedBodyTest";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING,
        "add to list|added to list|In unrestrictedBodyTestInclude.jsp|one");
    invoke();
  }
}
