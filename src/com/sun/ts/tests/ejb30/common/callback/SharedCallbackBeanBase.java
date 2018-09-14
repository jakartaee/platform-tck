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

package com.sun.ts.tests.ejb30.common.callback;

import com.sun.ts.tests.ejb30.common.helper.TLogger;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PreDestroy;
import javax.ejb.EJBContext;

abstract public class SharedCallbackBeanBase {
  protected static final String BEAN_SHORT_NAME = "BEAN";

  protected boolean postConstructCalled;

  protected boolean preDestroyCalled;

  protected boolean injectionDone;

  /**
   * If InvocationContext.getParameters(), or setParameters() is called for a
   * lifecycle callback method, IllegalStateException should be thrown. Use this
   * field to record such an exception has been thrown from PostConstruct
   * method.
   */
  private boolean getParametersIllegalStateExceptionThrown;

  private boolean setParametersIllegalStateExceptionThrown;

  /**
   * To record PostConstruct calls, in order, by multiple life event interceptor
   * methods. Its value can be ["A", "B", "C"].
   */
  protected List postConstructCalls = new ArrayList();

  /**
   * To record where injections were performed for interceptor classes. Its
   * value can be ["BASE", "A", "BASE", "B"].
   */
  protected List injectionLocations = new ArrayList();

  /**
   * Records PostConstruct calls and this List is stored in ContextData. Usually
   * this List should contain the same value in the same order as
   * postConstructCalls, except this List contains no "BEAN". It is because
   * interceptor methods inside the bean class does not take InvocationContext.
   */
  protected List postConstructCallsInContextData = new ArrayList();

  /**
   * Gets EJBContext that is the current EJB's namespace, typically through
   * injection into the bean class. A better way is to either directly inject
   * EJBContext into the current class, or look up by the well-known name
   * "java:comp/EJBContext" This method was written when the above 2 mechanisms
   * have not been defined. Note that the concrete subclass may be session bean,
   * or message-driven bean.
   * 
   * @return EJBContext
   */
  abstract public javax.ejb.EJBContext getEJBContext();

  @PreDestroy
  protected void preDestroy() {
    postConstructCalled = false;
    preDestroyCalled = false;
    injectionDone = false;
    postConstructCalls = null;
    injectionLocations = null;
    postConstructCallsInContextData = null;
  }

  protected void postConstructMethod() throws RuntimeException {
    addPostConstructCall(BEAN_SHORT_NAME);
  }

  /**
   * Returns a list of short name of classes where PostConstruct methods were
   * invoked. This is a business method of subclassing beans, and can be queried
   * by application clients.
   */
  public List getPostConstructCalls() {
    return postConstructCalls;
  }

  /**
   * Records where PostConstruct methods were invoked. Used by interceptor
   * classes.
   */
  public void addPostConstructCall(String shortName) {
    postConstructCalls.add(shortName);
  }

  /**
   * Returns a list of short name of interceptor classes where injections were
   * performed. This is a business method of subclassing beans, and can be
   * queried by application clients.
   */
  public List getInjectionLocations() {
    return injectionLocations;
  }

  public void addInjectionLocation(String shortName) {
    injectionLocations.add(shortName);
  }

  /**
   * Called by interceptors to copy their ContextData. Clients call the beans'
   * business method getPostConstructCallsInContextData() to retrieve and
   * compare with expected results.
   */
  public void setPostConstructCallsInContextData(List contextData) {
    this.postConstructCallsInContextData = contextData;
  }

  /**
   * business method declared in CallbackIF and Callback2IF.
   */
  public List getPostConstructCallsInContextData() {
    return this.postConstructCallsInContextData;
  }

  public boolean isPostConstructCalled() {
    return postConstructCalled;
  }

  public void setPostConstructCalled(boolean postConstructCalled) {
    this.postConstructCalled = postConstructCalled;
  }

  public boolean isPreDestroyCalled() {
    return preDestroyCalled;
  }

  public void setPreDestroyCalled(boolean preDestroyCalled) {
    this.preDestroyCalled = preDestroyCalled;
  }

  public boolean isInjectionDone() {
    return injectionDone;
  }

  public void setInjectionDone(boolean isInjectionDone) {
    this.injectionDone = isInjectionDone;
  }

  /**
   * Business method defined in CallbackIF.
   */
  public boolean isGetParametersIllegalStateExceptionThrown() {
    return getParametersIllegalStateExceptionThrown;
  }

  /**
   * Non-business method, used by interceptors to record such an exception has
   * been thrown from within the PostConstruct method.
   */
  public void setGetParametersIllegalStateExceptionThrown(boolean b) {
    this.getParametersIllegalStateExceptionThrown = b;
  }

  /**
   * Business method defined in CallbackIF.
   */
  public boolean isSetParametersIllegalStateExceptionThrown() {
    return setParametersIllegalStateExceptionThrown;
  }

  /**
   * Non-business method, used by interceptors to record such an exception has
   * been thrown from within the PostConstruct method.
   */
  public void setSetParametersIllegalStateExceptionThrown(boolean b) {
    this.setParametersIllegalStateExceptionThrown = b;
  }
}
