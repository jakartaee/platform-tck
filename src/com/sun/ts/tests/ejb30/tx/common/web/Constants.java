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

package com.sun.ts.tests.ejb30.tx.common.web;

public class Constants {
  public static final String TX_SERVLET_PATH = "/TxServlet";

  public static final String FOO_SERVLET_PATH = "/foo";

  public static final int LOOP_COUNT = 50;

  public static final String newThreadNoTx = "newThreadNoTx";

  public static final String SERVLET_NAME = "TestServlet";

  public static final String TEST_NAME_KEY = "testName";

  public static final String testname = "testname";

  public static final String servletRemoteCmtNever = "servletRemoteCmtNever";

  public static final String servletLocalCmtNever = "servletLocalCmtNever";

  public static final String servletRemoteCmtMandatory = "servletRemoteCmtMandatory";

  public static final String servletLocalCmtMandatory = "servletLocalCmtMandatory";

  public static final String servletRemoteCmtRequiresNew = "servletRemoteCmtRequiresNew";

  public static final String servletLocalCmtRequiresNew = "servletLocalCmtRequiresNew";

  public static final String servletRemoteCmt = "servletRemoteCmt";

  public static final String servletLocalCmt = "servletLocalCmt";

  public static final String servletTxTerminate = "servletTxTerminate";

  public static final String interServletTxPropagation = "interServletTxPropagation";

  public static final String interServletTxPropagation2 = "interServletTxPropagation2";

  private Constants() {
  }
}
