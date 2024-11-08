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

import java.io.*;
import java.util.*;
import java.net.*;
import java.text.SimpleDateFormat;




public class TestReportInfo implements Serializable {
  public int iDebugLevel = TestUtil.NORMAL_OUTPUT_LEVEL;

  public String sOutput = ""; // Constants.EMPTY_STRING;

  public Throwable exception = null;

  public int iStream = TestUtil.OUTPUT_STREAM;

  public TestReportInfo(String output, int stream, int level, Throwable e) {
    if (sOutput != null)
      sOutput = output;
    iDebugLevel = level;
    exception = e;
    iStream = stream;
  }
}

