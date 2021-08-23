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

package com.sun.ts.tests.ejb30.common.allowed;

public interface Constants {
  public static final String EJBContextLookupName = "java:comp/EJBContext";

  public static final String injectionAllowedBeanName = "java:comp/env/ejb/injectionAllowedBean";

  public static final String allowedBeanName = "java:comp/env/ejb/allowedBean";

  public static final String callbackAllowedBeanName = "java:comp/env/ejb/callbackAllowedBean";

  public static final String sessionContextAllowedBeanName = "java:comp/env/ejb/sessionContextAllowedBean";

  public static final String timerLookup = "java:comp/env/ejb/TimerEJB";

  public static final String Administrator = "Administrator";

  public static final String allowed = "allowed";

  public static final String disallowed = "disallowed";

  public static final String other = "other";

  /////////////////////////////////////////////////////////////////////////
  // used in stateful tests only
  public static final String Timer_Methods = "Timer_Methods";

  public static final String afterBeginTest = "afterBeginTest";

  public static final String beforeCompletionTest = "beforeCompletionTest";

  public static final String afterCompletionTest = "afterCompletionTest";

  public static final String afterBeginSetRollbackOnlyTest = "afterBeginSetRollbackOnlyTest";

  public static final String beforeCompletionSetRollbackOnlyTest = "beforeCompletionSetRollbackOnlyTest";

  public static final String afterCompletionSetRollbackOnlyTest = "afterCompletionSetRollbackOnlyTest";

  public static final String businessSetRollbackOnlyTest = "businessSetRollbackOnlyTest";

  public static final String preInvokeSetRollbackOnlyTest = "preInvokeSetRollbackOnlyTest";

  public static final String postInvokeSetRollbackOnlyTest = "postInvokeSetRollbackOnlyTest";

  /////////////////////////////////////////////////////////////////////////
  public static final String getEJBHome = "getEJBHome";

  public static final String getEJBObject = "getEJBObject";

  public static final String getEJBLocalHome = "getEJBLocalHome";

  public static final String getEJBLocalObject = "getEJBLocalObject";

  public static final String getBusinessObject = "getBusinessObject";

  public static final String getCallerPrincipal = "getCallerPrincipal";

  public static final String isCallerInRole = "isCallerInRole";

  public static final String getMessageContext = "getMessageContext";

  public static final String JNDI_Access = "JNDI_Access";

  public static final String EJBContext_lookup = "EJBContext_lookup";

  public static final String ENV_ENTRY_NAME = "myBoolean";

  public static final String UserTransaction = "UserTransaction";

  public static final String getRollbackOnly = "getRollbackOnly";

  public static final String setRollbackOnly = "setRollbackOnly";

  public static final String UserTransaction_Methods_Test1 = "UserTransaction_Methods_Test1";

  public static final String UserTransaction_Methods_Test2 = "UserTransaction_Methods_Test2";

  public static final String UserTransaction_Methods_Test3 = "UserTransaction_Methods_Test3";

  public static final String UserTransaction_Methods_Test4 = "UserTransaction_Methods_Test4";

  public static final String UserTransaction_Methods_Test5 = "UserTransaction_Methods_Test5";

  public static final String UserTransaction_Methods_Test6 = "UserTransaction_Methods_Test6";

  public static final String getTimerService = "getTimerService";

  public static final String TimerService_Methods_Test1 = "TimerService_Methods_Test1";

  public static final String TimerService_Methods_Test2 = "TimerService_Methods_Test2";

  public static final String TimerService_Methods_Test3 = "TimerService_Methods_Test3";

  public static final String TimerService_Methods_Test4 = "TimerService_Methods_Test4";

  public static final String TimerService_Methods_Test5 = "TimerService_Methods_Test5";

  public static final String TimerService_Methods_Test6 = "TimerService_Methods_Test6";

  public static final String TimerService_Methods_Test7 = "TimerService_Methods_Test7";

}
