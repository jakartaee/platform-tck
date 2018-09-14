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

package com.sun.ts.tests.common.connector.util;

import com.sun.ts.lib.util.TestUtil;
import java.util.Vector;

/**
 * Implementation class of Log interface, to be used as a verification mechanism
 * for connector tests. This class is implemented as a Singleton. The TS
 * whitebox resource adapter writes the log, and the TS test reads the log. The
 * whitebox will return an instance of this log to the calling test.
 */
public class ConnectorStatus implements Log {

  private static ConnectorStatus status = new ConnectorStatus();

  private Vector log = new Vector();

  private Vector statelog = new Vector();

  private boolean logFlag = false;

  /**
   * Singleton constructor
   */
  private ConnectorStatus() {
  }

  /**
   * Singleton accessor
   */
  public static ConnectorStatus getConnectorStatus() {
    return status;
  }

  // --------------------------
  // Log method implementations
  // --------------------------

  /**
   * Adds elements to the log. This is called by the Resource Adapter.
   *
   */
  public void logAPI(String raAPI, String inParams, String outParams) {
    String logString = new String(raAPI + ":" + inParams + ":" + outParams);
    if (logFlag)
      log.addElement(logString);
  }

  /**
   * Adds elements to the State log. This is called by the Resource Adapter.
   *
   */
  public void logState(String state) {
    statelog.addElement(state);
  }

  /**
   * Purges the log store
   */
  public void purge() {
    log.clear();
  }

  /**
   * Purges the log store
   */
  public void purgeStateLog() {
    statelog.clear();
  }

  /**
   * Retrieves the entire log as a String
   */
  public Vector getLogVector() {
    return log;
  }

  /**
   * Retrieves the entire log as a String
   */
  public Vector getStateLogVector() {
    return statelog;
  }

  /**
   * Sets the logging to true/false
   */
  public void setLogFlag(boolean b) {
    logFlag = b;
  }

}
