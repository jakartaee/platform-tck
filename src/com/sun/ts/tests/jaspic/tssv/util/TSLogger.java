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

import java.util.logging.Logger;
import java.util.logging.LogManager;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Filter;
import java.util.logging.Handler;
import java.util.logging.FileHandler;

/**
 *
 * @author Raja Perumal
 */
public class TSLogger extends Logger {

  private static TSLogger tsLogger = null;

  private int levelValue = Level.INFO.intValue();

  private int offValue = Level.OFF.intValue();

  private String name = null;

  private Filter filter;

  /** Creates a new instance of TSLogger */
  protected TSLogger(String name) {
    super(name, null);
    this.name = name;
    levelValue = Level.INFO.intValue();
  }

  /**
   * Find or create a logger for a named subsystem. If a logger has already been
   * created with the given name it is returned. Otherwise a new logger is
   * created.
   * <p>
   * If a new logger is created its log level will be configured based on the
   * LogManager configuration and it will configured to also send logging output
   * to its parent's handlers. It will be registered in the LogManager global
   * namespace.
   *
   * @param name
   *          A name for the logger. This should be a dot-separated name and
   *          should normally be based on the package name or class name of the
   *          subsystem, such as java.net or javax.swing
   * @return a suitable Logger
   */
  public static synchronized TSLogger getTSLogger(String name) {
    TSLogger result = null;

    LogManager manager = LogManager.getLogManager();

    if (tsLogger != null) {
      if (tsLogger.getName().equals(name))
        result = tsLogger;
    } else {
      result = new TSLogger(name);
      manager.addLogger(result);
    }

    return result;
  }

  /**
   * Logs a LogRecord.
   *
   * @param record
   *          the LogRecord to be published
   */
  public synchronized void log(LogRecord record) {
    if (record.getLevel().intValue() < levelValue || levelValue == offValue) {
      return;
    }
    synchronized (this) {
      if (filter != null && !filter.isLoggable(record)) {
        return;
      }
    }

    TSLogger logger = this;
    while (logger != null) {
      Handler targets[] = logger.getHandlers();

      if (targets != null) {
        for (int i = 0; i < targets.length; i++) {

          // Publish record only if the
          // handler is of type TSFileHandler
          // Do not publish to all parent handlers.
          if (targets[i] instanceof TSFileHandler)
            targets[i].publish(record);
        }
      }

      if (!logger.getUseParentHandlers()) {
        break;
      }

      logger = null;
    }
  }

}
