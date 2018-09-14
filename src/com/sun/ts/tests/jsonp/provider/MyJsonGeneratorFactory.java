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

/*
 * $Id$
 */

package com.sun.ts.tests.jsonp.provider;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;

import javax.json.*;
import javax.json.stream.*;
import java.io.*;
import java.nio.charset.Charset;
import java.util.*;

/*
 * MyJsonGeneratorFactory is a Json Test GeneratorFactory used by the pluggability tests
 * to test the Json SPI layer. This parser tracks that the proper callback
 * methods are invoked within the parser when Json API methods are called.
 */

public class MyJsonGeneratorFactory implements JsonGeneratorFactory {
  private OutputStream out = null;

  private Writer writer = null;

  private Charset charset = Charset.forName("UTF-8");

  private Map<String, ?> config = null;

  private void dumpInstanceVars() {
    TestUtil.logTrace("writer=" + writer);
    TestUtil.logTrace("out=" + out);
    TestUtil.logTrace("charset=" + charset);
    TestUtil.logTrace("config=" + config);
  }

  // call methods
  private static StringBuilder calls = new StringBuilder();

  public static String getCalls() {
    return calls.toString();
  }

  public static void clearCalls() {
    calls.delete(0, calls.length());
  }

  private static void addCalls(String s) {
    calls.append(s);
  }

  public MyJsonGeneratorFactory(Map<String, ?> config) {
    this.config = config;
  }

  public Map<String, ?> getConfigInUse() {
    TestUtil.logTrace("public Map<String, ?> getConfigInUse()");
    addCalls("public Map<String, ?> getConfigInUse()");
    return config;
  }

  public JsonGenerator createGenerator(OutputStream out) {
    TestUtil.logTrace("public JsonGenerator createGenerator(OutputStream)");
    addCalls("public JsonGenerator createGenerator(OutputStream)");
    this.out = out;
    return null;
  }

  public JsonGenerator createGenerator(OutputStream out, Charset charset) {
    TestUtil.logTrace(
        "public JsonGenerator createGenerator(OutputStream, Charset)");
    addCalls("public JsonGenerator createGenerator(OutputStream, Charset)");
    this.out = out;
    this.charset = charset;
    return null;
  }

  public JsonGenerator createGenerator(Writer writer) {
    TestUtil.logTrace("public JsonGenerator createGenerator(Writer)");
    addCalls("public JsonGenerator createGenerator(Writer)");
    this.writer = writer;
    return null;
  }
}
