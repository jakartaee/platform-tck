/*
 * Copyright (c) 2007, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.ejb30.assembly.librarydirectory.custom;

import com.sun.javatest.Status;
import com.sun.ts.tests.ejb30.assembly.common.ClientBase;
import com.sun.ts.tests.ejb30.assembly.common.Util;
import com.sun.ts.tests.ejb30.common.helper.TestFailedException;
import com.sun.ts.tests.ejb30.misc.getresource.common.GetResourceTest;

public class Client extends ClientBase {
  private static final String RESOURCE_NAME = "foo.txt";

  private static final String RESOURCE_NAME_AT_ROOT = "/foo.txt";

  private static final String RESOURCE_NAME_AT_COMMON_DIR = "/com/sun/ts/tests/ejb30/assembly/common/foo.txt";

  private static final String SECOND_LEVEL_JAR_RESOURCE_NAME = "second-level-jar.txt";

  private static final String SECOND_LEVEL_JAR_RESOURCE_CONTENT = SECOND_LEVEL_JAR_RESOURCE_NAME;

  private static final String SECOND_LEVEL_DIR_RESOURCE_NAME = "second-level-dir.txt";

  private static final String SECOND_LEVEL_DIR_RESOURCE_CONTENT = SECOND_LEVEL_DIR_RESOURCE_NAME;

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /*
   * @testName: libDirNotUsed
   * 
   * @assertion_ids:
   * 
   * @test_Strategy: EAR/lib/*.jar are not included in classpath, since
   * <library-directory> is set to something else
   */
  public void libDirNotUsed() throws TestFailedException {
    Util.verifyGetResource(getClass(), RESOURCE_NAME, null);
    Util.verifyGetResource(getClass(), RESOURCE_NAME_AT_ROOT, null);
    Util.verifyGetResource(getClass(), RESOURCE_NAME_AT_COMMON_DIR, null);
  }

  /*
   * @testName: libDirNotUsedEJB
   * 
   * @assertion_ids:
   * 
   * @test_Strategy: EAR/lib/*.jar are not included in classpath, since
   * <library-directory> is set to something else
   */
  public void libDirNotUsedEJB() throws TestFailedException {
    Util.verifyResource(remoteAssemblyBean, RESOURCE_NAME, null,
        remoteAssemblyBean.getResource(RESOURCE_NAME));
    Util.verifyResource(remoteAssemblyBean, RESOURCE_NAME_AT_ROOT, null,
        remoteAssemblyBean.getResource(RESOURCE_NAME_AT_ROOT));
    Util.verifyResource(remoteAssemblyBean, RESOURCE_NAME_AT_COMMON_DIR, null,
        remoteAssemblyBean.getResource(RESOURCE_NAME_AT_COMMON_DIR));
  }

  /*
   * @testName: secondLevelJar
   * 
   * @assertion_ids:
   * 
   * @test_Strategy: EAR/lib/*.jar may have further Class-Path dependencies
   */
  public void secondLevelJar() throws TestFailedException {
    (new GetResourceTest()).getResourceWithClass(getClass(),
        SECOND_LEVEL_JAR_RESOURCE_NAME, SECOND_LEVEL_JAR_RESOURCE_CONTENT);
  }

  /*
   * @testName: secondLevelJarEJB
   * 
   * @assertion_ids:
   * 
   * @test_Strategy: EAR/lib/*.jar may have further Class-Path dependencies
   */
  public void secondLevelJarEJB() throws TestFailedException {
    String result = remoteAssemblyBean
        .getResourceContent(SECOND_LEVEL_JAR_RESOURCE_NAME);
    GetResourceTest.verify(result, SECOND_LEVEL_JAR_RESOURCE_CONTENT);
  }

  /*
   * @testName: secondLevelDir
   * 
   * @assertion_ids:
   * 
   * @test_Strategy: EAR/lib/*.jar may have further Class-Path dependencies
   */
  public void secondLevelDir() throws TestFailedException {
    (new GetResourceTest()).getResourceWithClass(getClass(),
        SECOND_LEVEL_DIR_RESOURCE_NAME, SECOND_LEVEL_DIR_RESOURCE_CONTENT);
  }

  /*
   * @testName: secondLevelDirEJB
   * 
   * @assertion_ids:
   * 
   * @test_Strategy: EAR/lib/*.jar may have further Class-Path dependencies
   */
  public void secondLevelDirEJB() throws TestFailedException {
    String result = remoteAssemblyBean
        .getResourceContent(SECOND_LEVEL_DIR_RESOURCE_NAME);
    GetResourceTest.verify(result, SECOND_LEVEL_DIR_RESOURCE_CONTENT);
  }

  /*
   * @testName: postConstructInvokedInSuperElseWhere
   * 
   * @assertion_ids:
   * 
   * @test_Strategy: PostConstruct method must be invoked even when it's in a
   * superclass not packaged in appclient-client.jar
   */

  /*
   * @testName: remoteAdd
   * 
   * @assertion_ids:
   * 
   * @test_Strategy:
   */

  /*
   * @testName: remoteAddByHelloEJB
   * 
   * @assertion_ids:
   * 
   * @test_Strategy: hello ejb is packaged as a standalone ejb module and
   * deployed separately. It client view jar is packaged inside current ear and
   * referenced by both appclient and ejb jar thru MANIFEST.MF appclient ->
   * helloBean
   */

  /*
   * @testName: remoteAddByHelloEJBFromAssemblyBean
   * 
   * @assertion_ids:
   * 
   * @test_Strategy: hello ejb is packaged as a standalone ejb module and
   * deployed separately. It client view jar is packaged inside current ear and
   * referenced by both appclient and ejb jar thru MANIFEST.MF appclient ->
   * assemblyBean -> helloBean
   */
}
