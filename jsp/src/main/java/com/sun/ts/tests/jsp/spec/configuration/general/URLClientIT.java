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

package com.sun.ts.tests.jsp.spec.configuration.general;


import java.io.IOException;
import com.sun.ts.tests.jsp.common.client.AbstractUrlClient;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.jboss.shrinkwrap.api.asset.UrlAsset;


@ExtendWith(ArquillianExtension.class)
public class URLClientIT extends AbstractUrlClient {


  private static final String CONTEXT_ROOT = "/jsp_config_general_web";

  public URLClientIT() throws Exception {
    setup();

    setContextRoot(CONTEXT_ROOT);

  }

  @Deployment(testable = false)
  public static WebArchive createDeployment() throws IOException {
    
    String packagePath = URLClientIT.class.getPackageName().replace(".", "/");
    WebArchive archive = ShrinkWrap.create(WebArchive.class, "jsp_config_general_web.war");
    archive.addClass(AServlet.class);
    archive.setWebXML(URLClientIT.class.getClassLoader().getResource(packagePath+"/jsp_config_general_web.xml"));
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/prelude3.jsp")), "prelude3.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/prelude2.jsp")), "prelude2.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/prelude1.jsp")), "prelude1.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/coda3.jsp")), "coda3.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/coda2.jsp")), "coda2.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/coda1.jsp")), "coda1.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/mostSpecific/page/willSee.jsp")), "mostSpecific/page/willSee.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/identical/willSee.jsp")), "identical/willSee.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/specific/svr/willNotSee.jsp")), "specific/svr/willNotSee.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/specific/svr/page/willSee.jsp")), "specific/svr/page/willSee.jsp");

    return archive;

  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   *
   */

  /* Run test */

  /*
   * @testName: moreSpecificMappingTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy:
   */

  @Test
  public void moreSpecificMappingTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/specific/svr/willNotSee.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "In mapped servlet");
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/specific/svr/page/willSee.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "In coda1|In coda1");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "In mapped servlet");
    invoke();
  }

  /*
   * @testName: identicalMappingTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy:
   */

  @Test
  public void identicalMappingTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/identical/willSee.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "In coda1");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "In mapped servlet");
    invoke();
  }

  /*
   * @testName: mostSpecificMappingTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy:
   */

  @Test
  public void mostSpecificMappingTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/mostSpecific/page/willSee.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING,
        "In prelude1|In prelude2|In prelude3|el is enabled|In coda1|In coda2|In coda3");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "In mapped servlet");
    invoke();
  }
}
