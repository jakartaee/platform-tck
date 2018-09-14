/*
 * Copyright (c) 2009, 2018 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.ts.tests.ejb30.lite.packaging.embed.classloader.descriptor;

public final class Client extends
    com.sun.ts.tests.ejb30.lite.packaging.embed.classloader.annotated.Client {

  /*
   * @testName: additionalModuleJar
   * 
   * @testArgs: -additionalModule
   * "${ts.home}/dist/com/sun/ts/tests/ejb30/lite/packaging/embed/classloader/descriptor/ejbembed_two_ejb.jar"
   * -additionalModule
   * "${ts.home}/dist/com/sun/ts/tests/ejb30/lite/packaging/embed/classloader/descriptor/ejbembed_three_ejb.jar"
   * -additionalModule
   * "${ts.home}/dist/com/sun/ts/tests/ejb30/lite/packaging/embed/classloader/descriptor/ejbembed_vehicle_ejb.jar"
   * 
   * @test_Strategy: use a custom ContextClassLoader to find additional ejb
   * modules. Also javax.ejb.embeddable.modules property is set to include the
   * file location of the additional ejb modules. Module name is set in
   * ejb-jar.xml.
   */
  @Override
  public void additionalModuleJar() throws Exception {
    additionalModule(getModuleName(), "ejbembed_two_ejb-renamed",
        "ejbembed_three_ejb-renamed");
  }

  /*
   * @testName: additionalModuleDir
   * 
   * @testArgs: -additionalModule
   * "${ts.home}/dist/com/sun/ts/tests/ejb30/lite/packaging/embed/classloader/descriptor/23/"
   * -additionalModule
   * "${ts.home}/dist/com/sun/ts/tests/ejb30/lite/packaging/embed/classloader/descriptor/ejbembed_vehicle_ejb.jar"
   * 
   * @test_Strategy: use a custom ContextClassLoader to find additional ejb
   * modules. Also javax.ejb.embeddable.modules property is set to include the
   * file location of the additional ejb modules. Module name is set in
   * ejb-jar.xml.
   */
  @Override
  public void additionalModuleDir() throws Exception {
    additionalModule(getModuleName(), "23-renamed", "23-renamed");
  }

  /*
   * @testName: additionalModuleJarDir
   * 
   * @testArgs: -additionalModule
   * "${ts.home}/dist/com/sun/ts/tests/ejb30/lite/packaging/embed/classloader/descriptor/ejbembed_three_ejb.jar"
   * -additionalModule
   * "${ts.home}/dist/com/sun/ts/tests/ejb30/lite/packaging/embed/classloader/descriptor/2/"
   * -additionalModule
   * "${ts.home}/dist/com/sun/ts/tests/ejb30/lite/packaging/embed/classloader/descriptor/ejbembed_vehicle_ejb.jar"
   * 
   * @test_Strategy: use a custom ContextClassLoader to find additional ejb
   * modules. Also javax.ejb.embeddable.modules property is set to include the
   * file location of the additional ejb modules. Module name is set in
   * ejb-jar.xml.
   */
  @Override
  public void additionalModuleJarDir() throws Exception {
    additionalModule(getModuleName(), "2-renamed",
        "ejbembed_three_ejb-renamed");
  }
}
