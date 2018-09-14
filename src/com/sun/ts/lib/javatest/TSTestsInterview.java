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

import com.sun.interview.ErrorQuestion;
import com.sun.interview.FinalQuestion;
import com.sun.interview.Interview;
import com.sun.interview.Question;
import com.sun.interview.TreeQuestion;
import com.sun.interview.YesNoQuestion;
import com.sun.javatest.InterviewParameters;
import com.sun.javatest.Parameters;
import com.sun.javatest.TestResult;
import com.sun.javatest.TestResultTable;
import com.sun.javatest.TestSuite;
import com.sun.javatest.WorkDirectory;

/**
 * This interview collects the "initial files" parameter. It is normally used as
 * one of a series of sub-interviews that collect the parameter information for
 * a test run.
 */
public class TSTestsInterview extends Interview
    implements Parameters.MutableTestsParameters {
  /**
   * Create an interview.
   * 
   * @param parent
   *          The parent interview of which this is a child.
   * @throws Interview.Fault
   *           if there is a problem while creating the interview.
   */
  public TSTestsInterview(InterviewParameters parent) throws Interview.Fault {
    super(parent, "tests");
    this.parent = parent;
    setResourceBundle("i18n");
    // setHelpSet("/com/sun/javatest/moreInfo/moreInfo.hs");
    setFirstQuestion(qNeedTests);
  }

  /**
   * Get the initial files from the interview.
   * 
   * @return a list of initial files to be read, to determine the tests to be
   *         selected
   * @see #setTests
   */
  public String[] getTests() {
    if (qNeedTests.getValue() == YesNoQuestion.YES) {
      return qTests.getValue();
    } else
      return null;
  }

  public void setTests(String[] tests) {
    if (tests == null)
      setTestsMode(ALL_TESTS);
    else {
      setTestsMode(SPECIFIED_TESTS);
      setSpecifiedTests(tests);
    }
  }

  public int getTestsMode() {
    return (qNeedTests.getValue() == YesNoQuestion.YES ? SPECIFIED_TESTS
        : ALL_TESTS);
  }

  public void setTestsMode(int mode) {
    switch (mode) {
    case ALL_TESTS:
      qNeedTests.setValue(YesNoQuestion.NO);
      break;

    case SPECIFIED_TESTS:
      qNeedTests.setValue(YesNoQuestion.YES);
      break;

    default:
      throw new IllegalArgumentException();
    }
  }

  public String[] getSpecifiedTests() {
    return qTests.getValue();
  }

  public void setSpecifiedTests(String[] tests) {
    qTests.setValue(tests);
  }

  // ----------------------------------------------------------------------------
  //
  // Need tests

  private YesNoQuestion qNeedTests = new YesNoQuestion(this, "needTests") {
    {
      setValue(YesNoQuestion.NO);
    }

    protected Question getNext() {
      if (value == null)
        return null;
      else if (value == YES)
        return qTests;
      else
        return qEnd;
    }
  };

  // ----------------------------------------------------------------------------
  //
  // Tests

  private TreeQuestion.Model model = new TreeQuestion.Model() {
    public Object getRoot() {
      return parent.getWorkDirectory().getTestResultTable().getRoot();
    }

    public int getChildCount(Object node) {
      if (node == null)
        throw new NullPointerException();
      else if (node instanceof TestResultTable.TreeNode)
        return ((TestResultTable.TreeNode) node).getChildCount();
      else if (node instanceof TestResult)
        return 0;
      else
        throw new IllegalArgumentException();
    }

    public Object getChild(Object node, int index) {
      if (node == null)
        throw new NullPointerException();
      else if (node instanceof TestResultTable.TreeNode)
        return ((TestResultTable.TreeNode) node).getChild(index);
      else if (node instanceof TestResult)
        return null;
      else
        throw new IllegalArgumentException();
    }

    public String getName(Object node) {
      if (node == null)
        throw new NullPointerException();
      else if (node instanceof TestResultTable.TreeNode)
        return ((TestResultTable.TreeNode) node).getName();
      else if (node instanceof TestResult) {
        TestResult tr = (TestResult) node;
        String fullName = tr.getTestName();
        int lastSlash = fullName.lastIndexOf("/");
        return (lastSlash == -1 ? fullName : fullName.substring(lastSlash + 1));

      } else
        throw new IllegalArgumentException();
    }

    public String getPath(Object node) {
      if (node == null)
        throw new NullPointerException();
      else if (node instanceof TestResult)
        return ((TestResult) node).getTestName();
      else if (node instanceof TestResultTable.TreeNode) {
        TestResultTable.TreeNode tn = (TestResultTable.TreeNode) node;
        if (tn.isRoot())
          return tn.getName();
        else
          return getPath(tn.getParent() + "/" + tn.getName());
      } else
        throw new IllegalArgumentException();
    }

    public boolean isLeaf(Object node) {
      if (node == null)
        throw new NullPointerException();
      else if (node instanceof TestResult)
        return true;
      else if (node instanceof TestResultTable.TreeNode)
        return false;
      else
        throw new IllegalArgumentException();
    }
  };

  private TreeQuestion qTests = new TreeQuestion(this, "tests", model) {

    protected Question getNext() {
      validateTests();

      // value of null currently means everything;
      // this is a corollary of having an anonymous
      // test suite root; to fix, we would have to use
      // a pseudo-name in the saved value for "ALL"
      /*
       * if (value == null || value.length == 0) return null; else
       */if (cachedTestsError != null)
        return cachedTestsError;
      else
        return qEnd;
    }
  };

  private void validateTests() {
    String[] tests = qTests.getValue();
    if (equal(tests, cachedTestsValue))
      return;

    cachedTestsValue = tests;
    cachedTestsError = null; // default

    WorkDirectory wd = parent.getWorkDirectory();
    if (wd == null)
      return;

    TestResultTable trt = wd.getTestResultTable();
    if (tests == null || tests.length == 0) {
      // currently, empty selection means everything
      // as a corollary that the path of the root node
      // is saved as an empty string.
      // cachedTestsError = qNoTestsError;
      return;
    } else {
      for (int i = 0; i < tests.length; i++) {
        if (!trt.validatePath(tests[i])) {
          cachedTestsError = qBadTestsError;
          cachedTestsErrorArgs = new Object[] { tests[i] };
          return;
        }
      }
    }
  }

  private Question qNoTestsError = new ErrorQuestion(this, "noTests");

  private ErrorQuestion qBadTestsError = new ErrorQuestion(this, "badTests") {
    protected Object[] getTextArgs() {
      return cachedTestsErrorArgs;
    }
  };

  private String[] cachedTestsValue;

  private Question cachedTestsError;

  private Object[] cachedTestsErrorArgs;

  // ----------------------------------------------------------------------------
  //
  // End

  private Question qEnd = new FinalQuestion(this);

  // ----------------------------------------------------------------------------

  private static boolean equal(String[] s1, String[] s2) {
    if (s1 == null || s2 == null)
      return (s1 == s2);

    if (s1.length != s2.length)
      return false;

    for (int i = 0; i < s1.length; i++) {
      if (s1[i] != s2[i])
        return false;
    }

    return true;
  }

  // --------------------------------------------------------

  private InterviewParameters parent;
}
