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

package com.sun.ts.lib.harness;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.DataInputStream;
import java.io.ByteArrayInputStream;

import com.sun.javatest.*;
import com.sun.javatest.lib.*;

/**
 * This is a modification of <code>ProcessCommand</code> suitable for executing
 * JCK tests in an separate JVM. JCK tests may be "standard" tests which
 * implement the Test interface and which report their exit status by calling
 * <code>status.exit()</code>, or they may be "simple" tests which report their
 * exit status by an exit code alone.
 **/
public class ExecTSTestCmd extends ProcessCommand {

  /**
   * Generate a status for the command, based upon the command's exit code and a
   * status that may have been passed from the command by using
   * <code>status.exit()</code>.
   *
   * @param exitCode
   *          The exit code from the command that was executed. By convention,
   *          the expected values are:
   *          <table>
   *          <tr>
   *          <td>95
   *          <td>passed
   *          <tr>
   *          <td>96
   *          <td>compare the output written to the reference stream against a
   *          reference file
   *          <tr>
   *          <td>97
   *          <td>failed (inside test)
   *          <tr>
   *          <td>other
   *          <td>other failure
   *          </table>
   * @param logStatus
   *          If the command that was executed was a test program and exited by
   *          calling <code>status.exit()</code>, then logStatus will be set to
   *          `status'. Otherwise, it will be null. The value of the status is
   *          passed from the command by writing it as the last line to stderr
   *          before exiting the process. If it is not received as the last
   *          line, the value will be lost.
   * @return If logStatus is null, the result is determined from just the
   *         exitCode; if the logStatus is not null, the type of the status must
   *         correspond to the standard exit code for that type, in which case
   *         the result will be logStatus; otherwise an object will be returned
   *         indicating an error.
   * 
   **/
  protected Status getStatus(int exitCode, Status logStatus) {
    // first, convert the exit code back into a Status type code
    int typeFromExitCode = (exitCode == Status.exitCodes[Status.PASSED]
        ? Status.PASSED
        : exitCode == Status.exitCodes[Status.FAILED] ? Status.FAILED : -1);

    if (logStatus == null) {
      if (typeFromExitCode != -1)
        return defaultStatus[typeFromExitCode];
      else
        return Status.failed("unexpected exit code: exit code " + exitCode);
    } else {
      if (typeFromExitCode == logStatus.getType())
        return logStatus;
      else
        return Status.failed("unexpected exit code (" + exitCode
            + ") for reported status (" + logStatus.toString() + ")");
    }
  }

  private static final Status defaultStatus[] = { Status.passed("OK"),
      Status.failed("test failed"),
      // checkFile() is not available in javatest 3
      // Status.checkFile("check output against reference file")
  };

}
