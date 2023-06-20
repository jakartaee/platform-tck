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

package com.sun.ts.tests.jsp.spec.core_syntax.scripting.escaping;

import java.io.PrintWriter;

import com.sun.javatest.Status;
import java.io.IOException;
import java.io.InputStream;

import com.sun.ts.tests.jsp.common.client.AbstractUrlClient;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.shrinkwrap.api.Filters;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.jboss.shrinkwrap.api.asset.UrlAsset;

@ExtendWith(ArquillianExtension.class)
public class URLClientIT extends AbstractUrlClient {

  public static String packagePath = URLClientIT.class.getPackageName().replace(".", "/");

  public URLClientIT() throws Exception {
    setup();

    setGeneralURI("/jsp/spec/core_syntax/scripting/escaping");
    setContextRoot("/jsp_coresyntx_script_escape_web");

  }

  @Deployment(testable = false)
  public static WebArchive createDeployment() throws IOException {
    
    WebArchive archive = ShrinkWrap.create(WebArchive.class, "jsp_coresyntx_script_escape_web.war");
    archive.setWebXML(URLClientIT.class.getClassLoader().getResource(packagePath+"/jsp_coresyntx_script_escape_web.xml"));
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/positiveEscapingTest.jsp")), "positiveEscapingTest.jsp");

    return archive;

  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   */

  /* Run tests */

  // ============================================ Tests ======

  /*
   * @testName: positiveEscapingTest
   * 
   * @assertion_ids: JSP:SPEC:7
   * 
   * @testStrategy: Validate handling of escaped entities.
   */
  @Test
  public void positiveEscapingTest() throws Exception {
    InputStream gfStream = URLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveEscapingTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveEscapingTest");
    invoke();
  }
}
