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

package com.sun.ts.tests.jaspic.tssv.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.LogRecord;
import java.util.logging.StreamHandler;
import java.util.logging.LogManager;
import java.util.logging.Level;

/**
 * Simple file Handler based on java.util.logging.FileHandler
 *
 */

public class TSFileHandler extends StreamHandler {
  private MeteredStream meter;

  private boolean append;

  private int limit;

  private int count;

  private String pattern;

  private File files[];

  // A metered stream is a subclass of OutputStream that
  // (a) forwards all its output to a target stream
  // (b) keeps track of how many bytes have been written
  private class MeteredStream extends OutputStream {
    OutputStream out;

    int written;

    MeteredStream(OutputStream out, int written) {
      this.out = out;
      this.written = written;
    }

    public void write(int b) throws IOException {
      out.write(b);
      written++;
    }

    public void write(byte buff[]) throws IOException {
      out.write(buff);
      written += buff.length;
    }

    public void write(byte buff[], int off, int len) throws IOException {
      out.write(buff, off, len);
      written += len;
    }

    public void flush() throws IOException {
      out.flush();
    }

    public void close() throws IOException {
      out.close();
    }
  }

  private void open(File fname, boolean append) throws IOException {
    int len = 0;
    if (append) {
      len = (int) fname.length();
    }
    FileOutputStream fout = new FileOutputStream(fname.toString(), append);
    BufferedOutputStream bout = new BufferedOutputStream(fout);
    meter = new MeteredStream(bout, len);
    setOutputStream(meter);
  }

  // Private method to configure a FileHandler from LogManager
  // properties and/or default values as specified in the class
  // javadoc.
  private void configure() {
    LogManager manager = LogManager.getLogManager();

    String cname = getClass().getName();

    pattern = "";
    limit = 0;
    count = 1;
    append = true;
    setLevel(Level.ALL);
    setFilter(null);
  }

  /**
   * Initialize a <tt>FileHandler</tt> to write to the given filename, with
   * optional append.
   * <p>
   * The <tt>FileHandler</tt> is configured based on <tt>LogManager</tt>
   * properties (or their default values) except that the given pattern argument
   * is used as the filename pattern, the file limit is set to no limit, the
   * file count is set to one, and the append mode is set to the given
   * <tt>append</tt> argument.
   * <p>
   * There is no limit on the amount of data that may be written, so use this
   * with care.
   *
   * @param pattern
   *          the name of the output file
   * @param append
   *          specifies append mode
   * @exception IOException
   *              if there are IO problems opening the files.
   * @exception SecurityException
   *              if a security manager exists and if the caller does not have
   *              <tt>LoggingPermission("control")</tt>.
   * @exception IllegalArgumentException
   *              if pattern is an empty string
   */
  public TSFileHandler(String pattern, boolean append)
      throws IOException, SecurityException {
    if (pattern.length() < 1) {
      throw new IllegalArgumentException();
    }
    configure();
    this.pattern = pattern;
    this.limit = 0;
    this.count = 1;
    this.append = append;
    openFiles();
  }

  // Private method to open the set of output files, based on the
  // configured instance variables.
  //
  // Note: We don't lock files or rotate files based on size limit.
  private void openFiles() throws IOException {
    LogManager manager = LogManager.getLogManager();
    manager.checkAccess();
    if (count < 1) {
      throw new IllegalArgumentException("file count = " + count);
    }
    if (limit < 0) {
      limit = 0;
    }

    int unique = 0;

    files = new File[count];
    for (int i = 0; i < count; i++) {
      files[i] = generate(pattern, i, unique);
    }

    open(files[0], true);

  }

  // Generate a filename from a pattern.
  private File generate(String pattern, int generation, int unique)
      throws IOException {
    File file = null;
    String word = "";
    int ix = 0;
    boolean sawg = false;
    boolean sawu = false;
    while (ix < pattern.length()) {
      char ch = pattern.charAt(ix);
      ix++;
      char ch2 = 0;
      if (ix < pattern.length()) {
        ch2 = Character.toLowerCase(pattern.charAt(ix));
      }
      if (ch == '/') {
        if (file == null) {
          file = new File(word);
        } else {
          file = new File(file, word);
        }
        word = "";
        continue;
      } else if (ch == '%') {
        if (ch2 == 't') {
          String tmpDir = System.getProperty("java.io.tmpdir");
          if (tmpDir == null) {
            tmpDir = System.getProperty("user.home");
          }
          file = new File(tmpDir);
          ix++;
          word = "";
          continue;
        } else if (ch2 == 'h') {
          file = new File(System.getProperty("user.home"));
          if (isSetUID()) {
            // Ok, we are in a set UID program. For safety's sake
            // we disallow attempts to open files relative to %h.
            throw new IOException("can't use %h in set UID program");
          }
          ix++;
          word = "";
          continue;
        } else if (ch2 == 'g') {
          word = word + generation;
          sawg = true;
          ix++;
          continue;
        } else if (ch2 == 'u') {
          word = word + unique;
          sawu = true;
          ix++;
          continue;
        } else if (ch2 == '%') {
          word = word + "%";
          ix++;
          continue;
        }
      }
      word = word + ch;
    }
    if (count > 1 && !sawg) {
      word = word + "." + generation;
    }
    if (unique > 0 && !sawu) {
      word = word + "." + unique;
    }
    if (word.length() > 0) {
      if (file == null) {
        file = new File(word);
      } else {
        file = new File(file, word);
      }
    }
    return file;
  }

  /**
   * Format and publish a <tt>LogRecord</tt>.
   *
   * @param record
   *          description of the log event. A null record is silently ignored
   *          and is not published
   */
  public synchronized void publish(LogRecord record) {
    if (!isLoggable(record)) {
      return;
    }
    super.publish(record);
    super.flush();
    flush();
  }

  /**
   * Close all the files.
   *
   * @exception SecurityException
   *              if a security manager exists and if the caller does not have
   *              <tt>LoggingPermission("control")</tt>.
   */
  public synchronized void close() throws SecurityException {
    super.close();
  }

  // Private native method to check if we are in a set UID program.
  private static native boolean isSetUID();

}
