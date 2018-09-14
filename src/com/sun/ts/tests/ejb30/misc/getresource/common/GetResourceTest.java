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

package com.sun.ts.tests.ejb30.misc.getresource.common;

import com.sun.ts.tests.ejb30.common.helper.TLogger;
import com.sun.ts.tests.ejb30.common.helper.TestFailedException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URL;

public class GetResourceTest {
  public static final String NON_EXISTING_RESOURCE = "nbmvzpoqacfkol91267bm.txt";

  public GetResourceTest() {
  }

  public void getResourceWithClass(Class cls, String name, String expected)
      throws TestFailedException {
    verify(getResourceContent(cls, name), expected);
  }

  public void getResourceWithClassLoader(Class cls, String name,
      String expected) throws TestFailedException {
    URL url = getContextClassLoader(cls).getResource(name);
    if (url == null) {
      throw new TestFailedException(
          "ClassLoader.getResource(" + name + ") returned a null URL"
              + ".  The classloader is " + getContextClassLoader(cls));
    }
    String content = getResourceContent(url, name);
    verify(content, expected);
  }

  public void getResourceAsStreamWithClass(Class cls, String name,
      String expected) throws TestFailedException {
    InputStream is = cls.getResourceAsStream(name);
    if (is == null) {
      throw new TestFailedException("Class.getResourceAsStream(" + name
          + ") returned a null InputStream" + ".  The class is " + cls);
    }
    String content = getResourceContent(is, name);
    verify(content, expected);
  }

  public void getResourceAsStreamWithClassLoader(Class cls, String name,
      String expected) throws TestFailedException {
    InputStream is = getContextClassLoader(cls).getResourceAsStream(name);
    if (is == null) {
      throw new TestFailedException("ClassLoader.getResourceAsStream(" + name
          + ") returned a null InputStream" + ".  The classloader is "
          + getContextClassLoader(cls));
    }
    String content = getResourceContent(is, name);
    verify(content, expected);
  }

  public static String getResourceContent(Class cls, String name)
      throws TestFailedException {
    URL url = cls.getResource(name);
    if (url == null) {
      throw new TestFailedException("Class.getResource(" + name
          + ") returned a null URL" + ".  The class is " + cls
          + ".  The classloader is " + getContextClassLoader(cls));
    }
    return getResourceContent(url, name);
  }

  public static String getResourceContent(URL url, String name) {
    try {
      return getResourceContent(url.openStream(), name);
    } catch (IOException ex) {
      throw new IllegalStateException(ex);
    }
  }

  public static String getResourceContent(InputStream is, String name) {
    String result = null;
    Reader reader = new InputStreamReader(is);
    Writer writer = new StringWriter();
    char[] buffer = new char[1024];
    int n = 0;
    try {
      while ((n = reader.read(buffer)) != -1) {
        writer.write(buffer, 0, n);
      }
      result = writer.toString();
    } catch (IOException e) {
      throw new IllegalStateException(e);
    } finally {
      try {
        if (is != null) {
          is.close();
        }
        if (writer != null) {
          writer.close();
        }
      } catch (Exception e) {
        // ignore
      }
    }
    return result;
  }

  public void getResourceNullParam(Class cls) throws TestFailedException {
    try {
      cls.getResource(null);
      throw new TestFailedException(
          "Expecting NullPointerException, but got none.");
    } catch (NullPointerException e) {
      // good
    }
    try {
      getContextClassLoader(cls).getResource(null);
      throw new TestFailedException(
          "Expecting NullPointerException, but got none.");
    } catch (NullPointerException e) {
      // good
    }
  }

  public void getResourceAsStreamNullParam(Class cls)
      throws TestFailedException {
    try {
      cls.getResourceAsStream(null);
      throw new TestFailedException(
          "Expecting NullPointerException, but got none.");
    } catch (NullPointerException e) {
      // good
    }
    try {
      getContextClassLoader(cls).getResourceAsStream(null);
      throw new TestFailedException(
          "Expecting NullPointerException, but got none.");
    } catch (NullPointerException e) {
      // good
    }
  }

  public void getResourceNonexisting(Class cls) throws TestFailedException {
    getResourceNonexisting0(cls, NON_EXISTING_RESOURCE);
    getResourceNonexisting0(cls, NON_EXISTING_RESOURCE); // repeat
    getResourceNonexisting0(cls, "/" + NON_EXISTING_RESOURCE);
    getResourceNonexisting0(cls, "///////////" + NON_EXISTING_RESOURCE);

    getResourceNonexisting0(getContextClassLoader(cls), NON_EXISTING_RESOURCE);
    getResourceNonexisting0(getContextClassLoader(cls), NON_EXISTING_RESOURCE); // repeat
    getResourceNonexisting0(getContextClassLoader(cls),
        "/" + NON_EXISTING_RESOURCE);
    getResourceNonexisting0(getContextClassLoader(cls),
        "///////////" + NON_EXISTING_RESOURCE);
  }

  public void getResourceAsStreamNonexisting(Class cls)
      throws TestFailedException {
    getResourceAsStreamNonexisting0(cls, NON_EXISTING_RESOURCE);
    getResourceAsStreamNonexisting0(cls, NON_EXISTING_RESOURCE);
    getResourceAsStreamNonexisting0(cls, "/" + NON_EXISTING_RESOURCE);
    getResourceAsStreamNonexisting0(cls, "//////" + NON_EXISTING_RESOURCE);

    getResourceAsStreamNonexisting0(getContextClassLoader(cls),
        NON_EXISTING_RESOURCE);
    getResourceAsStreamNonexisting0(getContextClassLoader(cls),
        NON_EXISTING_RESOURCE);
    getResourceAsStreamNonexisting0(getContextClassLoader(cls),
        "/" + NON_EXISTING_RESOURCE);
    getResourceAsStreamNonexisting0(getContextClassLoader(cls),
        "//////" + NON_EXISTING_RESOURCE);
  }

  private void getResourceNonexisting0(Class cls, String name)
      throws TestFailedException {
    URL url = cls.getResource(name);
    if (url == null) {
      // good
    } else {
      throw new TestFailedException(
          "Expecting null when getResource " + name + ", but got " + url);
    }
  }

  private void getResourceNonexisting0(ClassLoader loader, String name)
      throws TestFailedException {
    URL url = loader.getResource(name);
    if (url == null) {
      // good
    } else {
      throw new TestFailedException(
          "Expecting null when getResource " + name + ", but got " + url);
    }
  }

  private void getResourceAsStreamNonexisting0(Class cls, String name)
      throws TestFailedException {
    InputStream is = cls.getResourceAsStream(name);
    if (is == null) {
      // good
    } else {
      throw new TestFailedException(
          "Expecting null when getResource " + name + ", but got " + is);
    }
  }

  private void getResourceAsStreamNonexisting0(ClassLoader loader, String name)
      throws TestFailedException {
    InputStream is = loader.getResourceAsStream(name);
    if (is == null) {
      // good
    } else {
      throw new TestFailedException(
          "Expecting null when getResource " + name + ", but got " + is);
    }
  }

  public static ClassLoader getContextClassLoader(Class cls) {
    ClassLoader loader = Thread.currentThread().getContextClassLoader();
    if (loader == null) {
      loader = cls.getClassLoader();
    }
    return loader;
  }

  public static void verify(String content, String expected)
      throws TestFailedException {
    expected = expected.trim();
    content = content.trim();
    if (expected.equals(content)) {
      TLogger
          .log("Resource retrieved correctly and the content is: " + expected);
    } else {
      throw new TestFailedException("Failed to retrieve the resource. "
          + "Expecting " + expected + ", actual: " + content);
    }
  }
}
