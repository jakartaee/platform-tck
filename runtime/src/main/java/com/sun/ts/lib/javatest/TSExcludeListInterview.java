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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Vector;
import com.sun.interview.ChoiceQuestion;
import com.sun.interview.ErrorQuestion;
import com.sun.interview.ExtensionFileFilter;
import com.sun.interview.FileQuestion;
import com.sun.interview.FileListQuestion;
import com.sun.interview.FinalQuestion;
import com.sun.interview.Interview;
import com.sun.interview.IntQuestion;
import com.sun.interview.Question;
import com.sun.interview.YesNoQuestion;
import com.sun.javatest.ExcludeList;
import com.sun.javatest.ExcludeListFilter;
import com.sun.javatest.InterviewParameters;
import com.sun.javatest.Parameters;
import com.sun.javatest.TestFilter;
import com.sun.javatest.TestSuite;
import com.sun.javatest.WorkDirectory;
import com.sun.javatest.util.I18NResourceBundle;

/**
 * This interview collects the "exclude list" test filter parameters. It is
 * normally used as one of a series of sub-interviews that collect the parameter
 * information for a test run.
 */
public class TSExcludeListInterview extends Interview
    implements Parameters.MutableExcludeListParameters {
  /**
   * Create an interview.
   * 
   * @param parent
   *          The parent interview of which this is a child.
   * @throws Interview.Fault
   *           if there is a problem while creating the interview.
   */
  public TSExcludeListInterview(InterviewParameters parent)
      throws Interview.Fault {
    super(parent, "excludeList");
    this.parent = parent;
    setResourceBundle("i18n");
    // setHelpSet("/com/sun/javatest/moreInfo/moreInfo.hs");
    setFirstQuestion(qNeedExcludeLists);
  }

  public File[] getExcludeFiles() {
    if (qNeedExcludeLists.getValue() == YesNoQuestion.YES) {
      String t = qExcludeListType.getValue();
      if (t == initial) {
        File f = parent.getTestSuite().getInitialExcludeList();
        if (f == null)
          return null;
        else
          return new File[] { f };
      } else if (t == latest) {
        URL u = parent.getTestSuite().getLatestExcludeList();
        if (u == null)
          return null;
        else {
          WorkDirectory wd = parent.getWorkDirectory();
          return new File[] { wd.getSystemFile("latest.jtx") };
        }
      } else
        return qCustomFiles.getValue();
    } else
      return null;
  }

  public void setExcludeFiles(File[] files) {
    if (files == null || files.length == 0)
      setExcludeMode(NO_EXCLUDE_LIST);
    else {
      setExcludeMode(CUSTOM_EXCLUDE_LIST);
      setCustomExcludeFiles(files);
    }
  }

  public int getExcludeMode() {
    if (qNeedExcludeLists.getValue() == YesNoQuestion.YES) {
      String t = qExcludeListType.getValue();
      if (t == initial)
        return INITIAL_EXCLUDE_LIST;
      else if (t == latest)
        return LATEST_EXCLUDE_LIST;
      else
        return CUSTOM_EXCLUDE_LIST;
    } else
      return NO_EXCLUDE_LIST;
  }

  public void setExcludeMode(int mode) {
    if (mode == NO_EXCLUDE_LIST)
      qNeedExcludeLists.setValue(YesNoQuestion.NO);
    else {
      qNeedExcludeLists.setValue(YesNoQuestion.YES);
      switch (mode) {
      case INITIAL_EXCLUDE_LIST:
        qExcludeListType.setValue(initial);
        break;
      case LATEST_EXCLUDE_LIST:
        qExcludeListType.setValue(latest);
        break;
      default:
        qExcludeListType.setValue(custom);
        break;
      }
    }
  }

  public File[] getCustomExcludeFiles() {
    return qCustomFiles.getValue();
  }

  public void setCustomExcludeFiles(File[] files) {
    qCustomFiles.setValue(files);
  }

  public boolean isLatestExcludeAutoCheckEnabled() {
    return (qLatestAutoCheck.getValue() == YesNoQuestion.YES);
  }

  public void setLatestExcludeAutoCheckEnabled(boolean b) {
    qLatestAutoCheck.setValue(b ? YesNoQuestion.YES : YesNoQuestion.NO);
  }

  public int getLatestExcludeAutoCheckMode() {
    return (qLatestAutoCheckMode.getValue() == EVERY_X_DAYS ? CHECK_EVERY_X_DAYS
        : CHECK_EVERY_RUN);
  }

  public void setLatestExcludeAutoCheckMode(int mode) {
    if (mode == CHECK_EVERY_X_DAYS)
      qLatestAutoCheckMode.setValue(EVERY_X_DAYS);
    else
      qLatestAutoCheckMode.setValue(EVERY_RUN);
  }

  public int getLatestExcludeAutoCheckInterval() {
    return qLatestAutoCheckInterval.getValue();
  }

  public void setLatestExcludeAutoCheckInterval(int days) {
    qLatestAutoCheckInterval.setValue(days);
  }

  /**
   * Get the exclude list generated from the exclude list files in the
   * interview.
   * 
   * @return the exclude list generated from the exclude list files in the
   *         interview
   * @see #getExcludeFiles
   */
  public ExcludeList getExcludeList() {
    if (qNeedExcludeLists.getValue() == YesNoQuestion.YES) {
      updateCachedExcludeListData();
      return cachedExcludeList;
    } else
      return new ExcludeList();
  }

  /**
   * Get a test filter generated from the exclude list files in the interview.
   * 
   * @return a test filter generated from the exclude list files in the
   *         interview
   * @see #getExcludeFiles
   */
  public TestFilter getExcludeFilter() {
    if (qNeedExcludeLists.getValue() == YesNoQuestion.YES) {
      updateCachedExcludeListData();
      return cachedExcludeListFilter;
    } else
      return null;
  }

  // ----------------------------------------------------------------------------
  //
  // Need exclude list

  private YesNoQuestion qNeedExcludeLists = new YesNoQuestion(this,
      "needExcludeList") {
    {
      setValue(YesNoQuestion.YES); // CF 6/17/02
    }

    protected Question getNext() {
      if (value == null)
        return null;
      else if (value == YES)
        return qExcludeListType;
      else
        return qEnd;
    }
  };

  // ----------------------------------------------------------------------------
  //
  // Type of exclude list

  private static String initial = "initial";

  private static String latest = "latest";

  private static String custom = "custom";

  private ChoiceQuestion qExcludeListType = new ChoiceQuestion(this,
      "excludeListType") {
    {
      // Difficulty here is that these depend on the test suite, which
      // will not have been set yet. So, set these full set of choices
      // for now, and refine the choices when the test suite gets set.

      try {
        I18NResourceBundle rb = I18NResourceBundle.getBundleForClass(
            Class.forName("com.sun.ts.lib.javatest.TSExcludeListInterview"));

        initial = rb
            .getString("TSExcludeListInterview.excludeList.choice.initial");
        latest = rb
            .getString("TSExcludeListInterview.excludeList.choice.latest");
        custom = rb
            .getString("TSExcludeListInterview.excludeList.choice.custom");
      } catch (ClassNotFoundException cnfe) {
        System.err.println(
            "Cannot load the resource bundle for the package containing the class, "
                + "com.sun.ts.lib.javatest.TSExcludeListInterview");
        cnfe.printStackTrace();
      }

      setChoices(new String[] { null, initial, latest, custom }, true);
      setValue(initial); // CF 6/17/02
    }

    public String[] getChoices() {
      if (!initialized && parent.getTestSuite() != null)
        init(parent.getTestSuite());
      return super.getChoices();
    }

    public boolean isHidden() {
      // getChoices() in next line will eventually force init()
      return (getChoices().length == 2); // null and custom
    }

    public String getValue() {
      // isHidden() in next line will eventually force init()
      return (isHidden() ? custom : super.getValue());
    }

    protected Question getNext() {
      if (isHidden())
        return qCustomFiles;
      else if (value == null || value.length() == 0)
        return null;
      else if (value.equals(initial))
        return qEnd;
      else if (value.equals(latest))
        return qLatestAutoCheck;
      else
        return qCustomFiles;
    }

    private void init(TestSuite ts) {
      Vector v = new Vector(4);
      v.add(null); // always
      if (ts.getInitialExcludeList() != null)
        v.add(initial);
      if (ts.getLatestExcludeList() != null)
        v.add(latest);
      v.add(custom); // always
      String[] choices = new String[v.size()];
      v.copyInto(choices);
      setChoices(choices, true);
      initialized = true;
    }

    private boolean initialized;
  };

  // ----------------------------------------------------------------------------
  //
  // Auto check latest

  private YesNoQuestion qLatestAutoCheck = new YesNoQuestion(this,
      "latestAutoCheck") {
    {
      setValue(YesNoQuestion.NO);
    }

    protected Question getNext() {
      if (value == null)
        return null;
      else if (value == YES)
        return qLatestAutoCheckMode;
      else
        return qEnd;
    }
  };

  // ----------------------------------------------------------------------------
  //
  // Auto check latest mode

  private static final String EVERY_X_DAYS = "everyXDays";

  private static final String EVERY_RUN = "everyRun";

  private ChoiceQuestion qLatestAutoCheckMode = new ChoiceQuestion(this,
      "latestAutoCheckMode") {
    {
      setChoices(new String[] { EVERY_X_DAYS, EVERY_RUN }, true);
    }

    protected Question getNext() {
      if (value == null)
        return null;
      else if (value.equals(EVERY_X_DAYS))
        return qLatestAutoCheckInterval;
      else
        return qEnd;
    }
  };

  // ----------------------------------------------------------------------------
  //
  // Auto check latest interval

  private IntQuestion qLatestAutoCheckInterval = new IntQuestion(this,
      "latestAutoCheckInterval") {
    {
      setBounds(1, 365);
      setValue(7);
    }

    protected Question getNext() {
      return qEnd;
    }
  };

  // ----------------------------------------------------------------------------
  //
  // Exclude List

  private FileListQuestion qCustomFiles = new FileListQuestion(this,
      "customFiles") {
    {
      // I18N...
      setFilter(new ExtensionFileFilter(".jtx", "Exclude List"));
    }

    protected Question getNext() {
      updateCachedExcludeListData();
      if (cachedExcludeListError != null)
        return cachedExcludeListError;
      else
        return qEnd;
    }

    public File getBaseDirectory() {
      TestSuite ts = parent.getTestSuite();
      return (ts == null ? null : ts.getRootDir());
    }
  };

  private void updateCachedExcludeListData() {
    TestSuite ts = parent.getTestSuite();
    File[] files = getAbsoluteFiles(ts.getRoot(), getExcludeFiles());
    if (!equal(cachedExcludeList_files, files)
        || cachedExcludeList_testSuite != ts) {
      try {
        ExcludeList t;
        if (ts == null || files == null || files.length == 0)
          t = new ExcludeList();
        else
          t = new ExcludeList(files);
        cachedExcludeList = t;
        cachedExcludeListFilter = new ExcludeListFilter(t);
        cachedExcludeListError = null;
      } catch (FileNotFoundException e) {
        cachedExcludeList = null;
        cachedExcludeListFilter = null;
        cachedExcludeListError = qExcludeListFileNotFound;
        cachedExcludeListErrorArgs = new String[] { e.getMessage() };
      } catch (IOException e) {
        cachedExcludeList = null;
        cachedExcludeListFilter = null;
        cachedExcludeListError = qExcludeListIOError;
        cachedExcludeListErrorArgs = new String[] { e.toString() };
      } catch (ExcludeList.Fault e) {
        cachedExcludeList = null;
        cachedExcludeListFilter = null;
        cachedExcludeListError = qExcludeListError;
        cachedExcludeListErrorArgs = new String[] { e.getMessage() };
      }
      cachedExcludeList_files = files;
    }
  }

  private ExcludeList cachedExcludeList;

  private ExcludeListFilter cachedExcludeListFilter;

  private Question cachedExcludeListError;

  private Object[] cachedExcludeListErrorArgs;

  private TestSuite cachedExcludeList_testSuite;

  private File[] cachedExcludeList_files;

  // ----------------------------------------------------------------------------
  //
  // Exclude List Error

  private ErrorQuestion qExcludeListFileNotFound = new ErrorQuestion(this,
      "excludeListFileNotFound") {
    protected Object[] getTextArgs() {
      return cachedExcludeListErrorArgs;
    }
  };

  private ErrorQuestion qExcludeListIOError = new ErrorQuestion(this,
      "excludeListIOError") {
    protected Object[] getTextArgs() {
      return cachedExcludeListErrorArgs;
    }
  };

  private ErrorQuestion qExcludeListError = new ErrorQuestion(this,
      "excludeListError") {
    protected Object[] getTextArgs() {
      return cachedExcludeListErrorArgs;
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

  // --------------------------------------------------------

  private InterviewParameters parent;
}
