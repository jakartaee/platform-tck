/*
 * Copyright (c) 2008, 2018 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.ts.tests.ejb30.lite.ejbcontext.common;

import com.sun.ts.tests.ejb30.common.helper.Helper;
import com.sun.ts.tests.ejb30.common.helper.ServiceLocator;
import com.sun.ts.tests.ejb30.common.helper.TestFailedException;
import static com.sun.ts.tests.ejb30.lite.ejbcontext.common.Util.lookupNames;
import static com.sun.ts.tests.ejb30.lite.ejbcontext.common.Util.postConstruct0;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.EJBContext;
import javax.ejb.SessionContext;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.naming.Context;

import static com.sun.ts.tests.ejb30.common.helper.Helper.assertEquals;
import static com.sun.ts.tests.ejb30.common.helper.Helper.assertNotEquals;

abstract public class EJBContextBeanBase implements EJBContextIF {
  private EJBContext ejbContextFromDescriptorInjection;

  @Resource(description = "correspond to <resource-env-ref> in ejb-jar.xml")
  private EJBContext ejbContext; // field-inject EJBContext

  private SessionContext sessionContext; // setter-inject SessionContext

  protected List<String> injectionRecords = new ArrayList<String>();

  private List<EJBContext> lookupValuesInPostConstruct = new ArrayList<EJBContext>();

  private List<EJBContext> lookupValuesInAroundInvoke = new ArrayList<EJBContext>();

  @SuppressWarnings("unused")
  @Resource(description = "correspond to <resource-env-ref> in ejb-jar.xml")
  private void setSessionContext(SessionContext sessionContext) {
    this.sessionContext = sessionContext;
  }

  @SuppressWarnings("unused")
  @PostConstruct
  private void postConstruct() {
    Helper.getLogger()
        .fine("About to verify EJBContext: ejbContext=" + ejbContext
            + ", sessionContext=" + sessionContext
            + ", ejbContextFromDescriptorInjection="
            + ejbContextFromDescriptorInjection);
    postConstruct0(injectionRecords, lookupValuesInPostConstruct, ejbContext,
        sessionContext, ejbContextFromDescriptorInjection);
  }

  // In EJB 3.1, any callback methods can be exposed as business methods.
  // Make it private to hide it.
  @SuppressWarnings("unused")
  @AroundInvoke
  private Object aroundInvoke(InvocationContext inv) throws Exception {
    String methodName = inv.getMethod().getName();
    Helper.assertEquals("lookupValuesInAroundInvoke must be empty ", 0,
        lookupValuesInAroundInvoke.size());
    if (methodName.equals("lookupEJBContext")) {
      for (String nm : lookupNames) { // to be verified in lookupEJBContext
                                      // method
        EJBContext ec = (EJBContext) ServiceLocator.lookupNoTry(nm);
        lookupValuesInAroundInvoke.add(ec);
      }
    }
    try {
      return inv.proceed();
    } finally {
      lookupValuesInAroundInvoke.clear();
    }
  }

  @SuppressWarnings("unused")
  @PreDestroy
  private void preDestroy() {
    Helper.preDestroy(this);
  }

  public List<String> getInjectionRecords() {
    return injectionRecords;
  }

  public List<String> getInjectionRecordsInInterceptor() {
    // this method is intercepted and another value in interceptor1 is returned
    return null;
  }

  public Integer add(int a, int b) {
    return a + b;
  }

  public String lookupIllegalArgumentException() throws TestFailedException {
    String[] badLookupNames = { null, "NoSuchName...", "java:comp/env/*" };
    Object obj = null;
    String result = "";
    for (String lookupName : badLookupNames) {
      try {
        obj = ejbContext.lookup(lookupName);
        result += " Expecting IllegalArgumentException"
            + " when looking up non-existent resource: " + lookupName
            + ", but the lookup result is: " + obj;
        throw new TestFailedException(result);
      } catch (IllegalArgumentException e) {
        result += " Got expected IllegalArgumentException when looking up non-existent resource: "
            + lookupName;
      }
    }
    return result;
  }

  public String getMessageContextIllegalStateException()
      throws TestFailedException {
    try {
      Object c = ((SessionContext) ejbContext).getMessageContext();
      throw new TestFailedException(
          "Expecting IllegalStateException, but got " + c);
    } catch (IllegalStateException illegalStateException) {
      return "Got expected IllegalStateException: " + illegalStateException;
    }
  }

  public Class<?> getInvokedBusinessInterface() {
    return ((SessionContext) ejbContext).getInvokedBusinessInterface();
  }

  public String getBusinessObjectIllegalStateException()
      throws TestFailedException {
    String result = "";
    Class<?>[] badBusinessInterfaces = { null, Class.class,
        java.io.Serializable.class, javax.ejb.EJB.class };
    for (Class<?> i : badBusinessInterfaces) {
      try {
        Object ob = ((SessionContext) ejbContext).getBusinessObject(i);
        throw new TestFailedException(
            "Expecting IllegalStateException with " + i + ", but got " + ob);
      } catch (IllegalStateException illegalStateException) {
        result += String.format(
            " Got expected IllegalStateException with %s :%s%n", i,
            illegalStateException);
      }
    }
    return result;
  }

  public <T> T getBusinessObject(Class<T> businessInterface)
      throws IllegalStateException {
    return ((SessionContext) ejbContext).getBusinessObject(businessInterface);
  }

  public String lookupEJBContext() {
    StringBuilder sb = new StringBuilder();

    // verify the lookup performed in PostConstruct
    Helper.assertEquals("", lookupNames.length,
        lookupValuesInPostConstruct.size(), sb);
    for (EJBContext ec : lookupValuesInPostConstruct) {
      Helper.assertNotEquals("", null, ec, sb);
    }
    // verify the lookup performed in AroundInvoke
    Helper.assertEquals("", lookupNames.length,
        lookupValuesInAroundInvoke.size(), sb);
    for (EJBContext ec : lookupValuesInAroundInvoke) {
      Helper.assertNotEquals("", null, ec, sb);
    }

    // try the lookup in a business method
    for (String s : lookupNames) {
      EJBContext ec = (EJBContext) ServiceLocator.lookupNoTry(s);
      Helper.assertNotEquals("", null, ec, sb);
    }
    return sb.toString();
  }

  public String lookupEJBContextInInterceptor() {
    return null;
  }

  public String lookupPortableJNDINames() {
    StringBuilder sb = new StringBuilder();

    Context c = (Context) ejbContext.lookup("java:comp/env");
    assertNotEquals(null, null, c, sb);

    String s = (String) ejbContext.lookup("java:app/env/myString");
    assertEquals(null, "app", s, sb);

    s = (String) ejbContext.lookup("java:comp/env/myString");
    assertEquals(null, "comp", s, sb);

    s = (String) ejbContext.lookup("myString");
    assertEquals(null, "comp", s, sb);

    s = (String) ejbContext
        .lookup("java:global/env/ejb30/lite/ejbcontext/myString");
    assertEquals(null, "global", s, sb);

    s = (String) ejbContext.lookup("java:module/env/myString2");
    assertEquals(null, "module", s, sb);

    return sb.toString();
  }
}
