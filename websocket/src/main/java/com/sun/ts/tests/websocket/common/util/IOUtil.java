/*
 * Copyright (c) 2013, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.websocket.common.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.nio.ByteBuffer;

/**
 * A class that works with java.io The patterns that would reappear
 */
public abstract class IOUtil {

  public static final//
  String readFromReader(Reader reader) throws IOException {
    BufferedReader br = new BufferedReader(reader);
    StringBuilder sb = new StringBuilder();
    String line = null;
    while ((line = br.readLine()) != null) {
      if (sb.length() != 0)
        sb.append("\n");
      sb.append(line);
    }
    br.close();
    return sb.toString();
  }

  public static final//
  String printStackTrace(Throwable t) {
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);
    t.printStackTrace(pw);
    String value = sw.toString();
    pw.close();
    return value;
  }

  public static final//
  String readFromStream(InputStream stream) throws IOException {
    InputStreamReader isr = new InputStreamReader(stream);
    return readFromReader(isr);
  }

  public static final//
  String byteBufferToString(ByteBuffer buffer) {
    byte[] ret = new byte[buffer.remaining()];

    if (buffer.hasArray()) {
      byte[] array = buffer.array();
      System.arraycopy(array, buffer.arrayOffset() + buffer.position(), ret, 0,
          ret.length);
    } else {
      buffer.asReadOnlyBuffer().get(ret);
    }

    return new String(ret);
  }
}
