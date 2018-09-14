/*
 * Copyright (c)  2012, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.interop.csiv2.common;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.File;
import java.io.PrintWriter;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.OutputStreamWriter;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.util.*;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

import com.sun.ts.lib.util.TestUtil;

/**
 * implementation for CSIv2 logger. This implementation uses an singleton class
 * to write all log messages out to a flat file.
 */
public class CSIv2FileLoggerImpl {

  // singleton instance
  public static CSIv2FileLoggerImpl theLoggerImpl = null;

  String fullPathAndFilename = null; // full path and filename

  /**
   * Returns the one instance of the CSIv2FileLoggerImpl.
   */
  public static CSIv2FileLoggerImpl getFileLogger(String fileName) {

    if (theLoggerImpl == null) {
      theLoggerImpl = new CSIv2FileLoggerImpl(fileName);
    }
    return theLoggerImpl;
  }

  public CSIv2FileLoggerImpl(String fName) {
    debug("in CSIv2FileLoggerImpl constructor");
    debug("CSIv2FileLoggerImpl fName = " + fName);
    fullPathAndFilename = fName;
    try {
      File fp = new File(fullPathAndFilename);
      if (!fp.exists()) {
        debug("WARNING - file does not exist: " + fullPathAndFilename);
      }
    } catch (Exception ex) {
      debug(
          "WARNING - CSIv2FileLoggerImpl constructor exception validating file: "
              + fullPathAndFilename);
      ex.printStackTrace();
    }
  }

  public void setFullPathAndFilename(String val) {
    fullPathAndFilename = val;
  }

  public String getFullPathAndFilename() {
    return fullPathAndFilename;
  }

  /**
   * @see CSIv2Logger.log
   */
  public final synchronized void log(String message) {
    RandomAccessFile file = null;
    FileLock fileLock = null;
    FileChannel fileChannel = null;

    debug("CSIv2FileLoggerImpl:  logging to: " + fullPathAndFilename);
    debug("CSIv2FileLoggerImpl:  log message: " + message);

    try {
      file = new RandomAccessFile(fullPathAndFilename, "rw");
      file.seek(file.length());
      fileChannel = file.getChannel();
      debug("fileChannel.position() = " + fileChannel.position());

      // ensure we get a lock to avoid concurrent writing to file.
      boolean lockObtained = false;
      int ii = 0;
      while (!lockObtained && (ii < 5)) {
        try {
          // try locking file - it will throw an exception if it cant get
          // a lock which indicates someone else already has a lock on it.
          // NOTE - there have been problems encountered which required us
          // to allocate a size of Long.MAX_VALUE - file.length(). Attempts
          // to allocate Long.MAX_VALUE from a starting position of
          // file.length()
          // appeared to crash because the internal lock() method adds these
          // two values and came up with a negative size!
          fileLock = fileChannel.lock(file.length(),
              Long.MAX_VALUE - file.length(), false);
          lockObtained = true;
          debug("lock obtained in CSIv2FileLoggerImpl.log()");
        } catch (Exception e) {
          // someone else likely has a lock on the file
          try {
            Thread.currentThread().sleep(100);
          } catch (Exception ex) {
          }
          debug("failed to get lock: " + e.getMessage());
          e.printStackTrace(); // do we really want to show all this?
        }
        ii++;
      }

      if (fileLock != null) {
        // write message it out to the log file
        file.writeBytes(message + "\n");

        // Snoop the log messages, extract the assertion name and
        // store it, if found.
        if (message.indexOf("<assertion name") > 0) {
          debug("message contains '<assertion name'");

          int start = message.indexOf("\"") + 1;
          int end = message.indexOf("\"", start);
          CSIv2Log.assertion = message.substring(start, end);
        }
      } else {
        System.err.println(
            "Error trying to secure lock of file: " + fullPathAndFilename);
        System.err.println(
            "There is possibly a collision or other io related issue.");
      }

    } catch (Exception ex) {
      System.err.println("got exception in CSIv2FileLoggerImpl.log()" + ex);
      ex.printStackTrace();
    } finally {
      try {
        if (fileLock != null) {
          fileLock.release();
          fileLock = null;
        }
        if (fileChannel != null) {
          fileChannel.close();
          fileChannel = null;
        }
        if (file != null) {
          file.close();
        }
      } catch (Exception e) {
      }
    } // finally
  }

  private void debug(String str) {
    TestUtil.logTrace(str);
  }

  /**
   * @see CSIv2Logger.get
   */
  public String get() {
    String rval = null;
    FileInputStream fis = null;
    FileChannel fChannel = null;
    FileLock fLock = null;
    BufferedReader br = null;

    // get/send the log file content back as String
    try {
      debug("in CSIv2FileLoggerImpl get()");
      fis = new FileInputStream(new File(fullPathAndFilename));
      fChannel = fis.getChannel();
      fLock = fChannel.lock(0L, Long.MAX_VALUE, true);
      br = new BufferedReader(new InputStreamReader(fis));

      // ensure we get a lock to avoid reading while someone else is writing to
      // it
      boolean lockObtained = false;
      int ii = 0;
      while (!lockObtained && (ii < 5)) {
        try {
          // try locking file - it will throw an exception if it cant get
          // a lock which indicates someone else already has a lock on it.
          fLock = fChannel.lock(0L, Long.MAX_VALUE, false);
          lockObtained = true;
        } catch (Exception e) {
          // someone else likely has a lock on the file
          try {
            Thread.currentThread().sleep(100);
          } catch (Exception ex) {
          }
        }
        ii++;
      }

      if (fLock != null) {
        debug(
            "in CSIv2FileLoggerImpl.get() - have file locked so can safely read it.");
        // read message from the log file
        String strLine = null;
        while ((strLine = br.readLine()) != null) {
          if (strLine != null) {
            rval = strLine + "\n";
          }
        }
      } else {
        System.err.println(
            "ERROR CSIv2FileLoggerImpl.get() - can NOT lock file for reading: "
                + fullPathAndFilename);
      }
      return rval;
    } catch (Exception ex) {
      System.err.println("Exception calling CSIv2FileLoggerImpl.get().");
      ex.printStackTrace();
    } finally {
      try {
        if (br != null) {
          br.close();
        }
        if (fLock != null) {
          fLock.release();
          fLock = null;
        }
        if (fChannel != null) {
          fChannel.close();
          fChannel = null;
        }
        if (fis != null) {
          fis.close();
        }
      } catch (Exception e) {
      }
    } // finally

    return null;
  }

  public void purge() {
    purge(true);
  }

  public final synchronized boolean purge(boolean renameLogFile) {

    try {
      if (renameLogFile) {
        // rename the file from serverInterceptorLog.txt to
        // serverInterceptorLog.txt.<assertion_name>
        String aName = getAssertionName();
        if (aName != null) {
          File newFName = new File(fullPathAndFilename + "." + aName);

          // make sure new file does not exist - else we wont be able to save
          // off to it
          try {
            if (!newFName.delete()) {
              System.err.println("ERROR:  could not delete existing file: "
                  + fullPathAndFilename + "." + aName);
            }
          } catch (Exception e) {
            System.err.println("ERROR:  could not delete file: "
                + fullPathAndFilename + "." + aName);
            e.printStackTrace();
          }

          File origName = new File(fullPathAndFilename);
          debug("Renaming file   from:  " + fullPathAndFilename + "  to: "
              + fullPathAndFilename + "." + aName);
          boolean bval = origName.renameTo(newFName);
          if (!bval) {
            System.err.println(
                "ERROR:  could not rename file from:  " + fullPathAndFilename
                    + "	 to: " + fullPathAndFilename + "." + aName);
            System.err.println(
                "likely failure is either that file already exists or you need permissions");
            return false;
          }
        } else {
          // NOTE the file may not exist if it was previously deleted which
          // means that
          // getting here is not always a bug or problem.
          System.out
              .println("WARNING - assertion name not foud - not renaming.");
        }
        return true;
      }

      File fp = new File(fullPathAndFilename);

      // delete the file
      if (fp.delete()) {
        System.out.println(
            "CSIv2FileLoggerImpl.purge() file deleted: " + fullPathAndFilename);
        return true;
      } else {
        System.err.println("ERROR:  CSIv2FileLoggerImpl.purge() delete failed: "
            + fullPathAndFilename);
      }
    } catch (Exception ex) {
      System.err.println("got exception in CSIv2FileLoggerImpl.purge()");
      ex.printStackTrace();
    }

    return false;
  }

  public String getAssertionName() {
    String rval = null;
    BufferedReader br = null;

    try {
      debug("in getAssertionName(), fullPathAndFilename = "
          + fullPathAndFilename);

      if (fullPathAndFilename != null) {
        // open log file, read content, and find the assertion name
        String assertion = null;
        File fp = new File(fullPathAndFilename);
        if (!fp.exists()) {
          // this may not always be an error. There are times when attempts are
          // made to get the assertion name and it does not exist yet.
          debug("WARNING:  file " + fullPathAndFilename + " does not exist.");
          rval = null;
        } else {
          br = new BufferedReader(new FileReader(fullPathAndFilename));
          String strLine;
          while ((strLine = br.readLine()) != null) {
            // pulling assertion name from string with the format of:
            // <assertion name="ew_ssl_ssln_upn_anonid_eb_eb_testid3">
            if (strLine.contains("assertion name=")) {
              // we found the line - now pull out the name
              int ii = strLine.indexOf("=");
              assertion = strLine.substring(ii + 2, strLine.length() - 2);
              rval = assertion;
              break;
            }
          }
        }
      } else {
        debug(
            "ERROR:  LoggerServlet.getAssertionName():  fullPathAndFilename = null");
        rval = null;
      }
    } catch (Exception ex) {
      System.err.println("got exception in LoggerServlet.getAssertionName()");
      ex.printStackTrace();
    } finally {
      try {
        br.close();
      } catch (Exception e) {
      }
    }

    return rval;
  }
}
