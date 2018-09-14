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

package com.sun.ts.lib.util;

import java.io.Serializable;
import java.rmi.*;
import java.util.*;
import javax.naming.*;
import javax.rmi.PortableRemoteObject;

/**
 * TSNamingContext provides a wrapper for all lookups.
 */
public class TSNamingContext implements TSNamingContextInterface, Serializable {

  Properties props = null;

  public TSNamingContext() throws Exception {
  }

  public TSNamingContext(Properties pp) throws Exception {
    if (pp != null) {
      props = pp;
    }
  }

  /**
   * Provides lookup of an object.
   *
   * @param s
   *          object name to lookup
   * @param c
   *          object class to narrow to if remote object if null no narrow is
   *          performed.
   */
  public Object lookup(String s, Class c) throws Exception {
    Object o = lookup(s);
    return c == null ? o : PortableRemoteObject.narrow(o, c);
  }

  /**
   * Provides lookup of an object.
   *
   * @param s
   *          object name to lookup
   */
  public Object lookup(String s) throws Exception {
    if (props != null) {
      return new InitialContext(props).lookup(s);
    } else {
      return new InitialContext().lookup(s);
    }
  }

}
