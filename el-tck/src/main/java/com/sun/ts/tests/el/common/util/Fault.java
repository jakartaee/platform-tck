/*
 * Copyright (c) 2023 Oracle and/or its affiliates and others.
 * All rights reserved.
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

package com.sun.ts.tests.el.common.util;

import java.io.PrintStream;
import java.io.PrintWriter;



/**
   * This exception must be thrown to signify a
   * test failure. Overrides 3 printStackTrace methods to preserver the original
   * stack trace. Using setStackTraceElement() would be more elegant but it is
   * not available prior to j2se 1.4.
   *
   * @author Kyle Grucci
   */
  public class Fault extends Exception {
    private static final long serialVersionUID = -1574745208867827913L;

    public Throwable t;

    /**
     * creates a Fault with a message
     */
    public Fault(String msg) {
      super(msg);
      // TestUtil.logErr(msg);
    }

    /**
     * creates a Fault with a message.
     *
     * @param msg
     *          the message
     * @param t
     *          prints this exception's stacktrace
     */
    public Fault(String msg, Throwable t) {
      super(msg);
      this.t = t;
      // TestUtil.logErr(msg, t);
    }

    /**
     * creates a Fault with a Throwable.
     *
     * @param t
     *          the Throwable
     */
    public Fault(Throwable t) {
      super(t);
      this.t = t;
    }

    /**
     * Prints this Throwable and its backtrace to the standard error stream.
     *
     */
    public void printStackTrace() {
      if (this.t != null) {
        this.t.printStackTrace();
      } else {
        super.printStackTrace();
      }
    }

    /**
     * Prints this throwable and its backtrace to the specified print stream.
     *
     * @param s
     *          <code>PrintStream</code> to use for output
     */
    public void printStackTrace(PrintStream s) {
      if (this.t != null) {
        this.t.printStackTrace(s);
      } else {
        super.printStackTrace(s);
      }
    }

    /**
     * Prints this throwable and its backtrace to the specified print writer.
     *
     * @param s
     *          <code>PrintWriter</code> to use for output
     */
    public void printStackTrace(PrintWriter s) {
      if (this.t != null) {
        this.t.printStackTrace(s);
      } else {
        super.printStackTrace(s);
      }
    }

    @Override
    public Throwable getCause() {
      return t;
    }

    @Override
    public synchronized Throwable initCause(Throwable cause) {
      if (t != null)
        throw new IllegalStateException("Can't overwrite cause");
      if (!Exception.class.isInstance(cause))
        throw new IllegalArgumentException("Cause not permitted");
      this.t = (Exception) cause;
      return this;
    }
  }
