/*
 * Copyright (c) 2013, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.ejb32.relaxedclientview.common;

public interface TestConstants {

  public static final int EXPECTED_RESULT1 = 3;

  public static final int EXPECTED_RESULT2 = 1;

  public static final String SUCCESSFULLY_RUN_RESULT = "%s ran successfully";

  public static final String GOT_WRONG_RESULT = "expected %d, but got %d";

  public static final String ONE_REMOTE_ANNOTATION_ON_INTERFACE_TEST_JNDI = "java:module/OneRemoteAnnotationOnInterfaceBean!com.sun.ts.tests.ejb32.relaxedclientview.common.NormalInterface2";

  public static final String ONE_REMOTE_ANNOTATION_ON_EJB_TEST_JNDI = "java:module/OneRemoteAnnotationOnEjbBean!com.sun.ts.tests.ejb32.relaxedclientview.common.NormalInterface2";

  public static final String NO_INTERFACE_VIEW_TEST_IF2_JNDI = "java:module/NoInterfaceViewBean!com.sun.ts.tests.ejb32.relaxedclientview.common.NormalInterface2";

  public static final String NO_INTERFACE_VIEW_TEST_BEAN_JNDI = "java:module/NoInterfaceViewBean";

  public static final String LOCAL_DD_TEST_JNDI = "java:module/LocalDDBean!com.sun.ts.tests.ejb32.relaxedclientview.common.NormalInterface2";

  public static final String JNDI_SHOULD_NOT_BE_FOUND = "jndi %s should not be found";

  public static final String JNDI_NOT_FOUNT = "jndi %s not found";

}
