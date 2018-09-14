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

import java.util.Map;

import javax.ejb.Singleton;
import javax.naming.Context;
import javax.naming.NamingException;

import com.sun.ts.tests.ejb30.common.helper.TestFailedException;

@Singleton
public class TestBean {

  public String getEnvironment() throws NamingException, TestFailedException {
    return Test.getEnvironment(Test.initialAndJava3Contexts());
  }

  public String bind(boolean excludeAppEnv)
      throws NamingException, TestFailedException {
    return Test.bind(Test.java3Contexts(excludeAppEnv));
  }

  public String rebind(boolean excludeAppEnv)
      throws NamingException, TestFailedException {
    return Test.rebind(Test.java3Contexts(excludeAppEnv));
  }

  public String unbind(boolean excludeAppEnv)
      throws NamingException, TestFailedException {
    return Test.unbind(Test.envContexts(excludeAppEnv));
  }

  public String rename() throws NamingException, TestFailedException {
    return Test.rename(Test.envContexts());
  }

  public String close() throws NamingException, TestFailedException {
    return Test.close(Test.initialAndJava3Contexts());
  }

  public String createSubcontext(boolean excludeAppEnv)
      throws NamingException, TestFailedException {
    return Test.createSubcontext(Test.java3Contexts(excludeAppEnv));
  }

  public String destroySubcontext(boolean excludeAppEnv)
      throws NamingException, TestFailedException {
    return Test.destroySubcontext(Test.envContexts(excludeAppEnv));
  }

  public String lookup(Map<Context, String> context2LookupResult)
      throws NamingException, TestFailedException {
    return Test.lookup(context2LookupResult);
  }

  public String list() throws NamingException, TestFailedException {
    return Test.list(Test.envContexts());
  }

  public String listBindings() throws NamingException, TestFailedException {
    return Test.listBindings(Test.envContexts());
  }
}
