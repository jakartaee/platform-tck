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

package com.sun.ts.tests.ejb30.webservice.interceptor;

import javax.interceptor.InvocationContext;
import javax.interceptor.AroundInvoke;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;
import java.util.Iterator;

import com.sun.ts.tests.ejb30.common.helper.TLogger;

/**
 * A business method interceptor for EJB based WebService.
 *
 * Test_Strategy: 1) Use WebServiceInterceptor to intercept all webservice
 * method invocations.
 *
 * 2) During such invocation, the InvocationContext supplied to the interceptors
 * should contain the following details.
 *
 * 1) The getTarget() of Invocation Context should return the bean instance 2)
 * The getMethod() should return the method of bean class for which the
 * interceptor is invoked. 3) The getParameters() method should return the
 * parameters of the business method invocation. 4) The Map returned by the
 * getContextData() method must be an instance of JAX-WS Message context. i.e.
 * javax.xml.ws.handler.MessageContext
 *
 * 3) If any of the above values are incorrect, then the interceptor throws
 * exception and the webservice invocation context test fails.
 *
 */
public class WebServiceInterceptor {

  public WebServiceInterceptor() {
    super();
  }

  @AroundInvoke
  public Object intercept(InvocationContext ctx) throws Exception {
    Method method = ctx.getMethod();
    TLogger.log("Interceptor invoked for method " + method.toString());

    // Get MessageContext from InvocationContext
    Map contextDataMap = ctx.getContextData();
    TLogger.log("InvocationContext.getContextData() type: "
        + contextDataMap.getClass());

    if (contextDataMap instanceof javax.xml.ws.handler.MessageContext) {
      TLogger.log(
          "ContextDataMap is an instance of javax.xml.ws.handler.MessageContext ");

      Object target = ctx.getTarget();
      if (target instanceof com.sun.ts.tests.ejb30.webservice.interceptor.HelloImpl) {

        TLogger.log("getTarget() returned an instance of HelloImpl ");

      } else {
        TLogger.log(
            "InvocationContext.getTarget() didn't return an instance of HelloImpl");
        throw new RuntimeException(
            "InvocationContext.getTarget() didn't return an instance of HelloImpl");

      }

      Object[] parameters = ctx.getParameters();
      if (parameters != null) {
        if (parameters[0].equals("Raja")) {
          TLogger.log("parameters = " + parameters[0]);
        } else {
          throw new RuntimeException(
              "Wrong webservice invocation Parameters passed");
        }
      }

      return ctx.proceed();
    } else {
      TLogger.log(
          "ContextDataMap is not an instance of javax.xml.ws.handler.MessageContext ");
      throw new RuntimeException(
          "ContextDataMap is not an instance of javax.xml.ws.handler.MessageContext");

    }

    // Map processing.
    /*
     * if(contextDataMap!=null){
     * 
     * // Iterate through the map Set entries = contextDataMap.entrySet();
     * Iterator iterator = entries.iterator(); while (iterator.hasNext()) {
     * Map.Entry entry = (Map.Entry)iterator.next(); String KeyName =
     * entry.getKey().toString(); TLogger.log(KeyName + " : " +
     * entry.getValue()); } }
     */

  }
}
