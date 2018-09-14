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

package com.sun.ts.lib.javatest;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import com.sun.interview.ErrorQuestion;
import com.sun.interview.ExtensionFileFilter;
import com.sun.interview.FileListQuestion;
import com.sun.interview.FinalQuestion;
import com.sun.interview.Interview;
import com.sun.interview.Question;
import com.sun.interview.StringQuestion;
import com.sun.javatest.InterviewParameters;
import com.sun.javatest.Parameters;
import com.sun.javatest.TestEnvContext;
import com.sun.javatest.TestEnvironment;
import com.sun.javatest.TestFilter;
import com.sun.javatest.TestSuite;
import com.sun.ts.lib.util.TestUtil;

/**
 * This interview collects the environment parameter, by means of environment
 * (jte) files and an environment name. It is normally used as one of a series
 * of sub-interviews that collect the parameter information for a test run. It
 * is suitable for use with legacy test suites that still rely on environments
 * being provided with .jte files; more sophisticated interviews should create a
 * custom interview that collects the environment data directly.
 */
public class TSEnvironmentInterview extends Interview
    implements Parameters.LegacyEnvParameters {
  /**
   * Create an interview.
   * 
   * @param parent
   *          The parent interview of which this is a child.
   * @throws Interview.Fault
   *           if there is a problem while creating the interview.
   */
  public TSEnvironmentInterview(InterviewParameters parent)
      throws Interview.Fault {
    super(parent, "environment");
    this.parent = parent;
    setResourceBundle("i18n");
    // setHelpSet("/com/sun/javatest/moreInfo/moreInfo.hs");
    setFirstQuestion(qEnvFiles);
  }

  /**
   * Get the environment files specified in the interview.
   * 
   * @return the list of files specified in the interview
   * @see #setEnvFiles
   */
  public File[] getEnvFiles() {
    return qEnvFiles.getValue();
  }

  public File[] getAbsoluteEnvFiles() {
    return getAbsoluteFiles(parent.getTestSuite().getRootDir(), getEnvFiles());
  }

  /**
   * Set the environment files for the interview.
   * 
   * @param files
   *          the environment files for the interview
   * @see #getEnvFiles
   */
  public void setEnvFiles(File[] files) {
    qEnvFiles.setValue(files);
  }

  /**
   * Get the environment name specified in the interview.
   * 
   * @return the environment name specified in the interview
   * @see #setEnvName
   */
  public String getEnvName() {
    return qEnv.getValue();
  }

  /**
   * Set the environment name for the interview.
   * 
   * @param name
   *          the environment name for the interview
   * @see #getEnvName
   */
  public void setEnvName(String name) {
    qEnv.setValue(name);
  }

  /**
   * Get the environment specified by the environment files and environment
   * name, or null, if it cannot be determined.
   * 
   * @return the environment determined by the interview, or null if it cannot
   *         be determined.
   * @see #getEnvFiles
   * @see #getEnvName
   */
  public TestEnvironment getEnv() {
    updateCachedEnv();
    return cachedEnv;
  }

  // ----------------------------------------------------------------------------
  //
  // Env files

  private FileListQuestion qEnvFiles = new FileListQuestion(this, "envFiles") {
    { // CF 6/17/02
      setFilter(new ExtensionFileFilter(".jte", "Environment File"));
      setValue(
          new File[] { new File(TSLegacyParameters.TS_HOME, "bin/ts.jte") });
    }

    public File getBaseDirectory() {
      TestSuite ts = parent.getTestSuite();
      if (ts == null)
        return null;
      else {
        File r = ts.getRoot();
        return r.isDirectory() ? r : r.getParentFile();
      }
    }

    protected Question getNext() {
      updateCachedEnvTable();
      if (cachedEnvTableError != null)
        return qEnvTableError;
      else if (cachedEnvTable == null
          || cachedEnvTable.getEnvNames().length == 0)
        return qNoEnvs;
      else
        return qEnv;
    }
  };

  private TestEnvContext getEnvTable() {
    updateCachedEnvTable();
    return cachedEnvTable;
  }

  private void updateCachedEnvTable() {
    File[] absFiles = getAbsoluteEnvFiles();
    if (!equal(cachedEnvTable_absFiles, absFiles)) {
      try {
        cachedEnvTable = new TestEnvContext(absFiles);
        cachedEnvTableError = null;
      } catch (TestEnvContext.Fault e) {
        e.printStackTrace();
        cachedEnvTable = null;
        cachedEnvTableError = e.getMessage();
      }
      cachedEnvTable_absFiles = absFiles;
    }
  }

  private TestEnvContext cachedEnvTable;

  private File[] cachedEnvTable_absFiles;

  private String cachedEnvTableError;

  // ----------------------------------------------------------------------------
  //
  // No Env

  private ErrorQuestion qNoEnvs = new ErrorQuestion(this, "noEnvs");

  // ----------------------------------------------------------------------------
  //
  // Env Table Error

  private ErrorQuestion qEnvTableError = new ErrorQuestion(this,
      "envTableError") {
    protected Object[] getTextArgs() {
      return new Object[] { cachedEnvTableError };
    }
  };

  // ----------------------------------------------------------------------------
  //
  // Env

  private StringQuestion qEnv = new StringQuestion(this, "env") {
    {
      if (TSLegacyParameters.OS.toUpperCase().startsWith("WIN")) {
        setValue("ts_win32");
      } else {
        setValue("ts_unix");
      }
    }

    public String[] getSuggestions() {
      // ensure the choices are up to date with envTable;
      // note that setting choices may smash the current value
      // if it's not a valid choice in the new set
      TestEnvContext envTable = getEnvTable();
      if (envTable != cachedEnvTable) {
        String[] envNames;
        if (envTable == null)
          envNames = TestUtil.EMPTY_STRING_ARRAY;
        else {
          String[] names = envTable.getEnvMenuNames();
          Arrays.sort(names);
          envNames = names;
        }
        setSuggestions(envNames);
        cachedEnvTable = envTable;
      }
      return super.getSuggestions();
    }

    protected Question getNext() {
      if (value == null)
        return null;
      else {
        updateCachedEnv();
        if (cachedEnv == null)
          return cachedEnvError;
        else
          return qEnd;
      }
    }

    private TestEnvContext cachedEnvTable;
  };

  private void updateCachedEnv() {
    TestEnvContext envTable = getEnvTable();
    String envName = getEnvName();
    if (cachedEnv_envTable != envTable || !equal(cachedEnv_envName, envName)) {
      try {
        if (envTable == null || envName == null || envName.length() == 0) {
          cachedEnv = null;
          cachedEnvError = null;
        } else {
          cachedEnv = envTable.getEnv(envName);
          if (cachedEnv == null) {
            cachedEnvError = qEnvNotFound;
            cachedEnvErrorArgs = new Object[] { envName };
          } else {
            // verify all entries defined
            cachedEnvError = null;
            cachedEnvErrorArgs = null;
            for (Iterator i = cachedEnv.elements().iterator(); i.hasNext()
                && cachedEnvError == null;) {
              TestEnvironment.Element entry = (TestEnvironment.Element) (i
                  .next());
              if (entry.getValue().indexOf("VALUE_NOT_DEFINED") >= 0) {
                cachedEnv = null;
                String eText = ((entry.getDefinedInEnv() == null ? ""
                    : "env." + entry.getDefinedInEnv() + ".") + entry.getKey()
                    + "=" + entry.getValue());
                cachedEnvError = qEnvUndefinedEntry;
                cachedEnvErrorArgs = new Object[] { eText,
                    entry.getDefinedInFile() };
              }
            }
          }
        }

      } catch (TestEnvironment.Fault e) {
        cachedEnv = null;
        cachedEnvError = qEnvError;
        cachedEnvErrorArgs = new Object[] { e.getMessage() };
      }
      cachedEnv_envTable = envTable;
      cachedEnv_envName = envName;
    }
  }

  private TestEnvironment cachedEnv;

  private TestEnvContext cachedEnv_envTable;

  private String cachedEnv_envName;

  private Question cachedEnvError;

  private Object[] cachedEnvErrorArgs;

  // ----------------------------------------------------------------------------
  //
  // Env Error

  private ErrorQuestion qEnvError = new ErrorQuestion(this, "envError") {
    protected Object[] getTextArgs() {
      return cachedEnvErrorArgs;
    }
  };

  // ----------------------------------------------------------------------------
  //
  // Env Not Found

  private ErrorQuestion qEnvNotFound = new ErrorQuestion(this, "envNotFound") {
    protected Object[] getTextArgs() {
      return cachedEnvErrorArgs;
    }
  };

  // ----------------------------------------------------------------------------
  //
  // Env Undefined Entry

  private ErrorQuestion qEnvUndefinedEntry = new ErrorQuestion(this,
      "envUndefinedEntry") {
    protected Object[] getTextArgs() {
      return cachedEnvErrorArgs;
    }
  };

  // ----------------------------------------------------------------------------
  //
  // End

  private Question qEnd = new FinalQuestion(this);

  // ---------------------------------------------------------------------

  private static File[] getAbsoluteFiles(File baseDir, File[] files) {
    if (files == null)
      return null;

    boolean allAbsolute = true;
    for (int i = 0; i < files.length && allAbsolute; i++)
      allAbsolute = files[i].isAbsolute();

    if (allAbsolute)
      return files;

    File[] absoluteFiles = new File[files.length];
    for (int i = 0; i < files.length; i++) {
      File f = files[i];
      absoluteFiles[i] = (f.isAbsolute() ? f : new File(baseDir, f.getPath()));
    }

    return absoluteFiles;
  }

  // ----------------------------------------------------------------------------

  private static boolean equal(File f1, File f2) {
    return (f1 == null ? f2 == null : f1.equals(f2));
  }

  private static boolean equal(File[] f1, File[] f2) {
    if (f1 == null || f2 == null)
      return (f1 == f2);

    if (f1.length != f2.length)
      return false;

    for (int i = 0; i < f1.length; i++) {
      if (f1[i] != f2[i])
        return false;
    }

    return true;
  }

  private static boolean equal(String s1, String s2) {
    return (s1 == null ? s2 == null : s1.equals(s2));
  }

  // --------------------------------------------------------

  private InterviewParameters parent;
}
