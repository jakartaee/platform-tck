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

import java.util.*;
import java.text.MessageFormat;
import com.sun.javatest.util.*;
import com.sun.interview.*;
import com.sun.javatest.*;
import com.sun.javatest.interview.*;

public class TSLegacyParameters extends BasicInterviewParameters {
  public static final String TS_HOME = System.getProperty("TS_HOME", "");

  public static final String OS = System.getProperty("os.name", "")
      .toLowerCase();

  private static I18NResourceBundle i18n = I18NResourceBundle
      .getBundleForClass(TSLegacyParameters.class);

  private TSEnvironmentInterview iEnvironment;

  private TestSuite testSuite;

  private WorkDirectory workDir;

  private TSTestsInterview iTests;

  private TSExcludeListInterview iExcludeList;

  private KeywordsInterview iKeywords;

  private PriorStatusInterview iPriorStatus;

  private ConcurrencyInterview iConcurrency;

  private TimeoutFactorInterview iTimeoutFactor;
  // private TSReportInterview iReport;

  public TSLegacyParameters() throws Fault {
    super("jtwiz");
    setResourceBundle("i18n");
    iEnvironment = new TSEnvironmentInterview(this);
    iTests = new TSTestsInterview(this);
    iExcludeList = new TSExcludeListInterview(this);
    iKeywords = new KeywordsInterview(this);
    iPriorStatus = new PriorStatusInterview(this);
    iConcurrency = new ConcurrencyInterview(this);
    iTimeoutFactor = new TimeoutFactorInterview(this);
    // iReport = new TSReportInterview(this);
    setFirstQuestion(qProlog);
  }

  public TSLegacyParameters(TestSuite testSuite) throws Fault {
    this();
    setTestSuite(testSuite);
  }

  public TestSuite getTestSuite() {
    return testSuite;
  }

  public void setTestSuite(TestSuite ts) {
    if (ts == null)
      throw new NullPointerException();
    if (testSuite != null && testSuite != ts)
      throw new IllegalStateException();
    testSuite = ts;
  }

  public WorkDirectory getWorkDirectory() {
    return workDir;
  }

  public void setWorkDirectory(WorkDirectory wd) {
    if (wd == null)
      throw new NullPointerException();
    if (workDir != null && workDir != wd)
      throw new IllegalStateException();
    workDir = wd;
  }

  // --------------------------------------------------------------------------
  public Parameters.TestsParameters getTestsParameters() {
    return iTests;
  }

  protected Question getTestsFirstQuestion() {
    return callInterview(iTests, getTestsSuccessorQuestion());
  }

  // --------------------------------------------------------------------------
  public Parameters.ExcludeListParameters getExcludeListParameters() {
    return iExcludeList;
  }

  protected Question getExcludeListFirstQuestion() {
    return callInterview(iExcludeList, getExcludeListSuccessorQuestion());
  }

  // --------------------------------------------------------------------------
  public Parameters.KeywordsParameters getKeywordsParameters() {
    return iKeywords;
  }

  protected Question getKeywordsFirstQuestion() {
    return callInterview(iKeywords, getKeywordsSuccessorQuestion());
  }

  // --------------------------------------------------------------------------
  public Parameters.PriorStatusParameters getPriorStatusParameters() {
    return iPriorStatus;
  }

  protected Question getPriorStatusFirstQuestion() {
    return callInterview(iPriorStatus, getPriorStatusSuccessorQuestion());
  }

  // --------------------------------------------------------------------------
  public Parameters.ConcurrencyParameters getConcurrencyParameters() {
    return iConcurrency;
  }

  protected Question getConcurrencyFirstQuestion() {
    return callInterview(iConcurrency, getConcurrencySuccessorQuestion());
  }

  // --------------------------------------------------------------------------
  public Parameters.TimeoutFactorParameters getTimeoutFactorParameters() {
    return iTimeoutFactor;
  }

  protected Question getTimeoutFactorFirstQuestion() {
    return callInterview(iTimeoutFactor, getTimeoutFactorSuccessorQuestion());
  }

  /*
   * //-------------------------------------------------------------------------
   * - public Parameters.ReportParameters getReportParameters () { return
   * iReport; }
   * 
   * protected Question getReportFirstQuestion () { return
   * callInterview(iReport, getReportSuccessorQuestion()); }
   */
  // --------------------------------------------------------------------------
  protected Question getEpilogFirstQuestion() {
    return qEpilog;
  }

  // --------------------------------------------------------------------------
  protected String getResourceString(String key) {
    try {
      return i18n.getString(key);
    } catch (MissingResourceException e) {
      return key;
    }
  }

  private NullQuestion qProlog = new NullQuestion(this, "prolog2") {

    public Question getNext() {
      return getPrologSuccessorQuestion();
    }
  };

  private FinalQuestion qEpilog = new FinalQuestion(this, "epilog2") {

    public String getSummary() {
      if (summary == null)
        summary = getResourceString("TSLegacyParameters.epilog2.smry");
      return summary;
    }

    public String getText() {
      if (text == null)
        text = getResourceString("TSLegacyParameters.epilog2.text");
      return MessageFormat.format(text, getTextArgs());
    }

    private String summary;

    private String text;
  };

  // ---------- added portion (not copied from DefaultInterviewParameters
  public Parameters.EnvParameters getEnvParameters() {
    return iEnvironment;
  }

  protected Question getEnvFirstQuestion() {
    return callInterview(iEnvironment, getEnvSuccessorQuestion());
  }

  protected Question getEnvSuccessorQuestion() {
    return getTestsFirstQuestion();
  }

  protected Question getTestsSuccessorQuestion() {
    return getExcludeListFirstQuestion();
  }

  protected Question getExcludeListSuccessorQuestion() {
    // return getReportFirstQuestion();
    return getEpilogFirstQuestion();
  }
  /*
   * protected Question getReportSuccessorQuestion () { return
   * getEpilogFirstQuestion(); }
   */
}
