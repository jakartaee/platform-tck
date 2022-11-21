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

/**
 * This interface defines the API necessary for a signature verification
 * application to log status messages, errors and debug messages to a common
 * output repository. This interface will be used by the API check tool to log
 * messages to the CTS output framework (namely the output methods defined in
 * the TestUtil class). This interface will be implemented by an adapter class
 * that will adapt the API defined in this interface to the logging API used by
 * CTS test code.
 */
public interface SigLogIntf {

  public void println(String msg);

  public void println(Object obj);

  public void println(char c);

  public void println();

  public void print(String msg);

  public void print(Object obj);

  public void print(char c);

  public void flush(); // nop

  public void close(); // nop

}
