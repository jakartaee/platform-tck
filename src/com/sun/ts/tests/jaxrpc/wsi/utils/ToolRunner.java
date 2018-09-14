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

/*
 * @(#)ToolRunner.java	1.2 03/05/16
 */

package com.sun.ts.tests.jaxrpc.wsi.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.sun.ts.lib.harness.EETest;

/**
 * The class ...
 *
 * @author Peter H. Petersen
 */
public class ToolRunner {
  /**
   * The command-line.
   */
  private String commandLine;

  /**
   * The STDOUT output.
   */
  private String stdout;

  /**
   * The STDERR output.
   */
  private String stderr;

  /**
   * The process.
   */
  private Process process;

  /**
   * 
   * @param commandLine
   */
  public ToolRunner(String commandLine) {
    super();
    this.commandLine = commandLine;
  }

  /**
   * Executes the process.
   * 
   * @return the exit value from the process.
   * 
   * @throws IOException
   * @throws InterruptedException
   */
  public int execute() throws EETest.Fault {
    if (process != null) {
      throw new IllegalStateException("Tool has already been executed.");
    }
    try {
      process = Runtime.getRuntime().exec(commandLine);
    } catch (IOException e) {
      throw new EETest.Fault("Unable to execute '" + commandLine + "'.", e);
    }
    StreamReader ir = new StreamReader(process.getInputStream());
    ir.start();
    StreamReader er = new StreamReader(process.getErrorStream());
    er.start();
    try {
      process.waitFor();
      ir.join();
      er.join();
    } catch (InterruptedException e) {
      throw new EETest.Fault("Execution interrupted.", e);
    }
    stdout = ir.getOutput();
    stderr = er.getOutput();
    return process.exitValue();
  }

  /**
   * Returns the STDOUT output.
   * 
   * @return the output.
   */
  public String getStdOut() {
    return stdout;
  }

  /**
   * Returns the STDERR output.
   * 
   * @return the output.
   */
  public String getStdErr() {
    return stderr;
  }

  /**
   * The class StreamReader is a Thread extension for reading process streams.
   */
  private class StreamReader extends Thread {
    /**
     * The input stream.
     */
    private InputStream is;

    /**
     * The output.
     */
    private String output;

    /**
     * Constructs a new StreamReader instance for the specified input stream.
     * 
     * @param is
     *          the input stream.
     */
    public StreamReader(InputStream is) {
      super();
      this.is = is;
    }

    /**
     * Runs this reader.
     */
    public void run() {
      StringBuffer buffer = new StringBuffer();
      try {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        String line;
        do {
          line = reader.readLine();
          if (line != null) {
            buffer.append(line);
            buffer.append('\n');
          }
        } while (line != null);
        reader.close();
      } catch (IOException e) {
        buffer.append(e.getMessage());
      }
      output = buffer.toString();
    }

    /**
     * Returns the output.
     * 
     * @return the output.
     */
    public String getOutput() {
      return output;
    }
  }
}
