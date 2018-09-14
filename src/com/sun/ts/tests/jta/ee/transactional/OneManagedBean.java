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

package com.sun.ts.tests.jta.ee.transactional;

import javax.annotation.Resource;
import javax.transaction.SystemException;
import javax.transaction.Transactional;
import javax.transaction.UserTransaction;
import javax.transaction.Transactional.TxType;
import javax.annotation.Priority;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.InterceptionType;
import javax.enterprise.util.AnnotationLiteral;
import javax.enterprise.inject.Any;
import javax.inject.Inject;

@OneManagedQualifier
public class OneManagedBean {
  public static final String NAME = "one-managed-bean";

  @Resource(lookup = "java:comp/UserTransaction")
  private UserTransaction ut2;

  @Inject
  BeanManager beanManager;

  private void setMyString(String s) {
    this.myString = s;
  }

  String myString;

  public String getName() {
    return NAME;
  }

  public OneManagedBean() {
  }

  @Transactional(value = TxType.REQUIRED)
  public String txTypeRequired() {
    String result = "txTypeRequired called successfully";
    return result;
  }

  @Transactional(value = TxType.REQUIRED)
  public String txTypeRequiredIllegalStateException() {
    String result = "not received IllegalStateException";
    try {
      ut2.begin();
      String var = "do nothing";
      ut2.commit();
    } catch (IllegalStateException ise) {
      result = "IllegalStateException";
      setMyString(result);

    } catch (Exception e) {
      e.printStackTrace();
      result = "unexcepted Exception :" + e.getMessage();
      setMyString(result);
    }

    return result;
  }

  @Transactional(value = TxType.REQUIRES_NEW)
  public String txTypeRequiresNew() {
    String result = "txTypeRequiresNew called successfully";
    return result;
  }

  @Transactional(value = TxType.MANDATORY)
  public String txTypeMandatory() {
    String result = "txTypeMandatory called successfully";
    return result;
  }

  @Transactional(value = TxType.SUPPORTS)
  public String txTypeSupports() {
    String result = "txTypeSupports called successfully";
    return result;
  }

  @Transactional(value = TxType.SUPPORTS)
  public String txTypeSupportsWithoutTransaction() {

    String result = "txTypeSupports run without active transaction";
    return result;
  }

  @Transactional(value = TxType.NOT_SUPPORTED)
  public String txTypeNotSupported() {
    String result = "txTypeNotSupported run without active transaction";
    return result;

  }

  @Transactional(value = TxType.NEVER)
  public String txTypeNever() {
    String result = "txTypeNever run without active transaction";
    return result;
  }

  @Transactional(rollbackOn = { CTSRollbackException.class })
  public void rollbackOnException() throws CTSRollbackException {
    throw new CTSRollbackException("CTSRollbackException");
  }

  @Transactional(dontRollbackOn = { CTSDontRollbackException.class })
  public void dontRollbackOnException() throws CTSDontRollbackException {
    throw new CTSDontRollbackException("CTSDontRollbackException");
  }

  @Transactional(rollbackOn = { CTSRollbackException.class }, dontRollbackOn = {
      CTSRollbackException.class })
  public void rollbackAndDontRollback() throws CTSRollbackException {
    throw new CTSRollbackException("CTSRollbackException");
  }

  public List<Integer> getPriority(String methodName) {
    int priorityValue = 0;
    List<Integer> priorityList = new ArrayList();
    try {
      Class bClass = this.getClass();
      Method m = bClass.getMethod(methodName);
      Annotation[] annotationArray = m.getAnnotations();

      java.util.List<javax.enterprise.inject.spi.Interceptor<?>> interceptorList = beanManager
          .resolveInterceptors(InterceptionType.AROUND_INVOKE, annotationArray);

      for (javax.enterprise.inject.spi.Interceptor<?> interceptor : interceptorList) {
        System.out.println("Interceptor Name = " + interceptor.getName());
        System.out.println("Interceptor toString = " + interceptor.toString());
        System.out.println(
            "Interceptor Beanclass = " + interceptor.getBeanClass().getName());

        // Get Priority Annotation from interceptor bean class
        Annotation annotation = interceptor.getBeanClass()
            .getAnnotation(Priority.class);
        if (annotation != null && annotation instanceof Priority) {
          Priority myPriorityAnnotation = (Priority) annotation;
          priorityValue = myPriorityAnnotation.value();
          System.out.println(
              "Priority value(From Interceptor bean class) = " + priorityValue);
          priorityList.add(priorityValue);
        } else {
          // Get Priority Annotation from Interceptor Bindings
          Set<Annotation> annotations = interceptor.getInterceptorBindings();
          System.out
              .println("InterceptorBindings set size =" + annotations.size());
          for (Annotation ibAnnotation : annotations) {
            System.out.println("InterceptorBindings Annototation : "
                + ibAnnotation.getClass().getName());
            System.out.println("InterceptorBindings Annototation type : "
                + ibAnnotation.annotationType());
            if (ibAnnotation != null && ibAnnotation instanceof Priority) {
              Priority myPriorityAnnotation = (Priority) ibAnnotation;
              priorityValue = myPriorityAnnotation.value();
              System.out.println("Priority value(From Interceptor bindings) = "
                  + priorityValue);
              priorityList.add(priorityValue);
            }
          }
        }
        if (priorityValue == 0) {
          System.out.println("Priority Annotation not found");
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return priorityList;
  }
}
