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
 * $URL$ $LastChangedDate$
 */

package com.sun.ts.tests.common.taglibsig.validation;

import com.sun.ts.lib.util.TestUtil;

public class ValidatorFactory {

  // No instances of Factory
  private ValidatorFactory() {
  }

  /**
   * <p>
   * Returns an instance of a {@link Validator} based on the provided class
   * name.
   * </p>
   * 
   * @param className
   *          - The name of the Validator's implementation class
   * @return a Validator instance of the class exists, otherwise null
   */
  public static Validator getValidator(String className) {
    Validator v = null;
    try {
      v = (Validator) Thread.currentThread().getContextClassLoader()
          .loadClass(className).newInstance();
    } catch (Throwable t) {
      // XXX Enhance this section
      System.out.println(t.toString());
      TestUtil.logMsg(t.toString());
    }
    return v;
  }
}
