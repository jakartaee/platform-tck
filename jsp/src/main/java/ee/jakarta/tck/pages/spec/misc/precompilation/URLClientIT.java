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

package ee.jakarta.tck.pages.spec.misc.precompilation;


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


  public URLClientIT() throws Exception {


    setGeneralURI("/jsp/spec/misc/precompilation");
    setContextRoot("/jsp_misc_precompilation_web");

  }

  @Deployment(testable = false)
  public static WebArchive createDeployment() throws IOException {
    
    String packagePath = URLClientIT.class.getPackageName().replace(".", "/");
    WebArchive archive = ShrinkWrap.create(WebArchive.class, "jsp_misc_precompilation_web.war");
    archive.setWebXML(URLClientIT.class.getClassLoader().getResource(packagePath+"/jsp_misc_precompilation_web.xml"));
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/precompile.jsp")), "precompile.jsp");

    return archive;

  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   *
   */

  /* Run test */

  /*
   * @testName: precompileNoValueTest
   * 
   * @assertion_ids: JSP:SPEC:244.1.4
   * 
   * @test_Strategy: Validate that no response body is returned when
   * jsp_precompile has no value.
   */

  @Test
  public void precompileNoValueTest() throws Exception {
    String testName = "precompileNoValue";
    TEST_PROPS.setProperty(TEST_NAME, testName + "Test");
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_misc_precompilation_web/precompile.jsp?jsp_precompile HTTP/1.0");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Got the Request");
    invoke();
  }

  /*
   * @testName: precompileFalseTest
   * 
   * @assertion_ids: JSP:SPEC:244.1.2
   * 
   * @test_Strategy: Validate that no response body is returned when
   * jsp_precompile is set to false.
   */

  @Test
  public void precompileFalseTest() throws Exception {
    String testName = "precompileFalse";
    TEST_PROPS.setProperty(TEST_NAME, testName + "Test");
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_misc_precompilation_web/precompile.jsp?jsp_precompile=false HTTP/1.0");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Got the Request");
    invoke();
  }

  /*
   * @testName: precompileTrueTest
   * 
   * @assertion_ids: JSP:SPEC:244.1.1
   * 
   * @test_Strategy: Validate that no response body is returned when
   * jsp_precompile is set to true.
   */

  @Test
  public void precompileTrueTest() throws Exception {
    String testName = "precompileTrue";
    TEST_PROPS.setProperty(TEST_NAME, testName + "Test");
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_misc_precompilation_web/precompile.jsp?jsp_precompile=true HTTP/1.0");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Got the Request");
    invoke();
  }

  /*
   * @testName: precompileNegativeTest
   * 
   * @assertion_ids: JSP:SPEC:244.1.5
   * 
   * @test_Strategy: Set the jsp_precompile request paramter to a non valid
   * value and validate that a 500 error occurs.
   */

  @Test
  public void precompileNegativeTest() throws Exception {
    String testName = "precompileNegative";
    TEST_PROPS.setProperty(TEST_NAME, testName + "Test");
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_misc_precompilation_web/precompile.jsp?jsp_precompile=any_invalid_value HTTP/1.0");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

}
