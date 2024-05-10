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

/*
 * @(#)URLClient.java	1.36 02/11/04
 */

package ee.jakarta.tck.pages.spec.configuration.includes;


import java.io.IOException;
import ee.jakarta.tck.pages.common.client.AbstractUrlClient;

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

    setContextRoot("/jsp_config_includes_web");

  }

  @Deployment(testable = false)
  public static WebArchive createDeployment() throws IOException {
    
    String packagePath = URLClientIT.class.getPackageName().replace(".", "/");
    WebArchive archive = ShrinkWrap.create(WebArchive.class, "jsp_config_includes_web.war");
    archive.setWebXML(URLClientIT.class.getClassLoader().getResource(packagePath+"/jsp_config_includes_web.xml"));
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/jspf/prelude3.jspf", "jspf/prelude3.jspf");    
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/jspf/prelude2.jspf", "jspf/prelude2.jspf");    
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/jspf/prelude1.jspf", "jspf/prelude1.jspf");    
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/jspf/coda3.jspf", "jspf/coda3.jspf");    
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/jspf/coda2.jspf", "jspf/coda2.jspf");    
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/jspf/coda1.jspf", "jspf/coda1.jspf");    
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/IncludesTest.jsp")), "IncludesTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/two/Includes2Test.jsp")), "two/Includes2Test.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/three/Includes3Test.jspx")), "three/Includes3Test.jspx");

    return archive;

  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   *
   */

  /* Run test */

  /*
   * @testName: jspConfigurationIncludesTest
   * 
   * @assertion_ids: JSP:SPEC:147;JSP:SPEC:148;JSP:SPEC:149
   * 
   * @test_Strategy: Validate the following: - The container properly recognizes
   * prelude and coda configuration elements. - Prelude includes are includes at
   * the beginning of the target JSP(s) identified by the url-pattern, and are
   * included in the order they appear in the deployment descriptor. - Coda
   * includes are inserted at the end of the target JSP(s) identified by the
   * url-pattern, and are included in the order they appear in the deployment
   * descriptor. - Validate with both standard syntax JSPs and JSP documents.
   */
  @Test
  public void jspConfigurationIncludesTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_config_includes_web/IncludesTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Prelude1|JSP Body|Coda1");
    invoke();
  }

  /*
   * @testName: jspConfigurationIncludes2Test
   * 
   * @assertion_ids: JSP:SPEC:147;JSP:SPEC:148;JSP:SPEC:149
   * 
   * @test_Strategy: Validate the same as above test.
   */
  @Test
  public void jspConfigurationIncludes2Test() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_config_includes_web/two/Includes2Test.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING,
        "Prelude1|Prelude2|JSP Body 2|Coda1|Coda2");
    invoke();
  }

  /*
   * @testName: jspConfigurationIncludes3Test
   * 
   * @assertion_ids: JSP:SPEC:147;JSP:SPEC:148;JSP:SPEC:149
   * 
   * @test_Strategy: Validate the same as above test. JSP.1.10.4 states that
   * implicit includes can use either the same syntax as the including page, or
   * a different syntax.
   */
  @Test
  public void jspConfigurationIncludes3Test() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_config_includes_web/three/Includes3Test.jspx HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING,
        "Prelude2|Prelude3|JSP Body 3|Coda2|Coda3");
    invoke();
  }
}
