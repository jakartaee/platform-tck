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

package com.sun.ts.lib.harness;

/**
 * This class defines any exception thrown by the Regression extensions.
 *
 * @author Iris A Garcia
 * @version @(#)TestRunException.java 1.2 99/03/15
 */
public class TestRunException extends RuntimeException {
  private static final long serialVersionUID = -9119523505446607510L;

  public TestRunException(String msg) {
    super(msg);
  } // TestRunException()

  public TestRunException(Throwable t) {
    super(t.getMessage(), t);
  } // TestRunException()

  public Throwable getThrowable() {
    return getCause();
  } // getThrowable()
}
