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
 * @(#)ByteArrayDataSource.java	1.10 03/05/16
 */

package com.sun.ts.tests.assembly.util.shared.resref.single;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import java.io.*;
import java.util.*;
import com.sun.javatest.Status;

import javax.mail.*;
import javax.activation.*;
import javax.mail.internet.*;

class ByteArrayDataSource implements DataSource {
  private byte[] data;

  private String type;

  ByteArrayDataSource(InputStream is, String type) {
    this.type = type;
    try {
      ByteArrayOutputStream os = new ByteArrayOutputStream();
      int ch;

      while ((ch = is.read()) != -1)
        os.write(ch);

      data = os.toByteArray();
    } catch (IOException ioex) {
      TestUtil.printStackTrace(ioex);
    }
  }

  ByteArrayDataSource(byte[] data, String type) {
    this.data = data;
    this.type = type;
  }

  ByteArrayDataSource(String data, String type) {
    try {
      this.data = data.getBytes("iso-8859-1");
    } catch (UnsupportedEncodingException uex) {
      TestUtil.printStackTrace(uex);
    }
    this.type = type;
  }

  public InputStream getInputStream() throws IOException {
    if (data == null)
      throw new IOException("no data");
    return new ByteArrayInputStream(data);
  }

  public OutputStream getOutputStream() throws IOException {
    throw new IOException("cannot do this");
  }

  public String getContentType() {
    return type;
  }

  public String getName() {
    return "dummy";
  }
}
