/*
 * Copyright (c) 2010, 2018 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.ts.tests.ejb30.lite.naming.context;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJB;
import javax.naming.Context;
import javax.naming.NamingException;

import com.sun.ts.tests.ejb30.common.helper.TestFailedException;
import com.sun.ts.tests.ejb30.common.lite.EJBLiteClientBase;

public class Client extends EJBLiteClientBase {
  @EJB(beanName = "TestBean", name = "sub/TestBean")
  // use "sub" as the subcontext name to be consistent with ejb-jar.xml
  private TestBean testBean;

  private boolean excludeAppEnv = true;

  /*
   * @testName: getEnvironment
   * 
   * @test_Strategy: Verifies java url context by invoking its methods.
   * Application component instances are not allowed to modify the environment
   * at runtime.
   */
  public void getEnvironment() throws NamingException, TestFailedException {
    appendReason("About to run tests in EJB.");
    appendReason(testBean.getEnvironment());
    if (getContainer() == null) {
      appendReason("About to run tests in web components.");
      appendReason(Test.getEnvironment(Test.initialAndJava3Contexts()));
    }
  }

  /*
   * @testName: bind
   * 
   * @test_Strategy: Verifies java url context by invoking its methods.
   * Application component instances are not allowed to modify the environment
   * at runtime.
   */
  public void bind() throws NamingException, TestFailedException {

    appendReason("About to run tests in EJB.");
    appendReason(testBean.bind(excludeAppEnv));
    if (getContainer() == null) {
      appendReason("About to run tests in web components.");
      appendReason(Test.bind(Test.java3Contexts(excludeAppEnv)));
    }
  }

  /*
   * @testName: rebind
   * 
   * @test_Strategy: Verifies java url context by invoking its methods.
   * Application component instances are not allowed to modify the environment
   * at runtime.
   */
  public void rebind() throws NamingException, TestFailedException {
    appendReason("About to run tests in EJB.");
    appendReason(testBean.rebind(excludeAppEnv));
    if (getContainer() == null) {
      appendReason("About to run tests in web components.");
      appendReason(Test.rebind(Test.java3Contexts(excludeAppEnv)));
    }
  }

  /*
   * @testName: unbind
   * 
   * @test_Strategy: Verifies java url context by invoking its methods.
   * Application component instances are not allowed to modify the environment
   * at runtime.
   */
  public void unbind() throws NamingException, TestFailedException {
    appendReason("About to run tests in EJB.");
    appendReason(testBean.unbind(excludeAppEnv));
    if (getContainer() == null) {
      appendReason("About to run tests in web components.");
      appendReason(Test.unbind(Test.envContexts(excludeAppEnv)));
    }
  }

  /*
   * @testName: rename
   * 
   * @test_Strategy: Verifies java url context by invoking its methods.
   * Application component instances are not allowed to modify the environment
   * at runtime.
   */
  public void rename() throws NamingException, TestFailedException {
    appendReason("About to run tests in EJB.");
    appendReason(testBean.rename());
    if (getContainer() == null) {
      appendReason("About to run tests in web components.");
      appendReason(Test.rename(Test.envContexts()));
    }
  }

  /*
   * @testName: close
   * 
   * @test_Strategy: Verifies java url context by invoking its methods.
   */
  public void close() throws NamingException, TestFailedException {
    appendReason("About to run tests in EJB.");
    appendReason(testBean.close());
    if (getContainer() == null) {
      appendReason("About to run tests in web components.");
      appendReason(Test.close(Test.initialAndJava3Contexts()));
    } else {
      appendReason("About to run tests in ejbembed.");
      appendReason(Test.close(getContext(),
          (Context) getContext().lookup("java:global/" + getModuleName())));
    }
  }

  /*
   * @testName: createSubcontext
   * 
   * @test_Strategy: Verifies java url context by invoking its methods.
   */
  public void createSubcontext() throws NamingException, TestFailedException {
    appendReason("About to run tests in EJB.");
    appendReason(testBean.createSubcontext(excludeAppEnv));
    if (getContainer() == null) {
      appendReason("About to run tests in web components.");
      appendReason(Test.createSubcontext(Test.java3Contexts(excludeAppEnv)));
    }
  }

  /*
   * @testName: destroySubcontext
   * 
   * @test_Strategy: Verifies java url context by invoking its methods.
   */
  public void destroySubcontext() throws NamingException, TestFailedException {
    appendReason("About to run tests in EJB.");
    appendReason(testBean.destroySubcontext(excludeAppEnv));
    if (getContainer() == null) {
      appendReason("About to run tests in web components.");
      appendReason(Test.destroySubcontext(Test.envContexts(excludeAppEnv)));
    }
  }

  /*
   * @testName: lookup
   * 
   * @test_Strategy: Verifies java url context by invoking its methods.
   */
  public void lookup() throws NamingException, TestFailedException {
    Map<Context, String> context2LookupResult = new HashMap<Context, String>();
    context2LookupResult.put(Test.javaModuleEnvContext(), "module");
    context2LookupResult.put(Test.javaAppEnvContext(), "wide");
    Map<Context, String> unmodifiable = Collections
        .unmodifiableMap(context2LookupResult);

    appendReason("About to run tests in EJB.");
    appendReason(testBean.lookup(unmodifiable));

    if (getContainer() == null) {
      appendReason("About to run tests in web components.");
      appendReason(Test.lookup(unmodifiable));
    }
  }

  /*
   * @testName: list
   * 
   * @test_Strategy: Verifies java url context by invoking its methods.
   */
  public void list() throws NamingException, TestFailedException {
    appendReason("About to run tests in EJB.");
    appendReason(testBean.list());
    if (getContainer() == null) {
      appendReason("About to run tests in web components.");
      appendReason(Test.list(Test.envContexts()));
    }
  }

  /*
   * @testName: listBindings
   * 
   * @test_Strategy: Verifies java url context by invoking its methods.
   */
  public void listBindings() throws NamingException, TestFailedException {
    appendReason("About to run tests in EJB.");
    appendReason(testBean.listBindings());
    if (getContainer() == null) {
      appendReason("About to run tests in web components.");
      appendReason(Test.listBindings(Test.envContexts()));
    }
  }
}
