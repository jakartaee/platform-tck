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

/**
 *
 * @author Sun Microsystems
 */
public class JASPICData {

  // these are the jsr-196 supported profile message-layers
  public final static String LAYER_SERVLET = "HttpServlet";

  public final static String LAYER_SOAP = "SOAP";

  // this is a name that we will use to internally reference our logger
  public final static String LOGGER_NAME = "jsr196";

  // this log file is used on the appserver side by our cts tests
  // to verify success/failures of our tests that involve a need to
  // know that status of an exchange between a client and server.
  public final static String DEFAULT_LOG_FILE = "TSSVLog.txt";

  public final static String MOD_CLASS_NAME = "moduleClassKey";

  public final static String SVC_SUBJECT_KEY = "com.sun.ts.tests.jaspic.serviceSubjectKey";

  // define some statics for the cts AuthConfigFactory implementation and
  // the RI's implementation of the AuthConfigFactory
  public final static String TSSV_ACF = "com.sun.ts.tests.jaspic.tssv.config.TSAuthConfigFactory";

  // definitions for Servlet Container Profile (SCP)
  public final static String SCP_CONTEXT_PATH = "spitests_servlet_web";

  public final static String AUTHSTAT_FAILURE_ND = "ModuleAuthStatusFailureNoDispatch";

  public final static String AUTHSTAT_SENDFAILURE_ND = "ModuleAuthStatusSendFailureNoDispatch";

  public final static String AUTHSTAT_SENDCONT_ND = "ModuleAuthStatusSendContinueNoDispatch";

  public final static String AUTHSTAT_SENDSUCCESS_ND = "ModuleAuthStatusSendSuccessNoDispatch";

  public final static String AUTHSTAT_SUCCESS_ND = "ModuleAuthStatusSuccessNoDispatch";

  public final static String AUTHSTAT_THROW_EX_ND = "ModuleAuthStatusThrowExNoDispatch";

  public final static String AUTHSTAT_OPT_SUCCESS = "AuthStatusOptionalSuccess";

  public final static String AUTHSTAT_MAND_SUCCESS = "AuthStatusMandatorySuccess";

  public final static String AUTHSTAT_FAILURE_D = "ModuleAuthStatusFailureDispatch";

  public final static String AUTHSTAT_SENDFAILURE_D = "ModuleAuthStatusSendFailureDispatch";

  public final static String AUTHSTAT_SENDCONT_D = "ModuleAuthStatusSendContinueDispatch";

  public final static String AUTHSTAT_SENDSUCCESS_D = "ModuleAuthStatusSendSuccessDispatch";

  public final static String AUTHSTAT_SUCCESS_D = "ModuleAuthStatusSuccessDispatch";

  public final static String AUTHSTAT_THROW_EX_D = "ModuleAuthStatusThrowExDispatch";

}
