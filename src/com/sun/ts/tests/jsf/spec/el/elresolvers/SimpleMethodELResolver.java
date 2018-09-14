/*
 * Copyright (c) 2009, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jsf.spec.el.elresolvers;

import java.util.Iterator;
import java.util.HashSet;
import java.util.Set;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.beans.FeatureDescriptor;

import javax.el.ELResolver;
import javax.el.ELContext;
import javax.el.ELException;

/**
 * <p>
 * This resolver will handle simple, no-argument methods that don't conform to
 * <code>JavaBeans</code> naming standards.
 *
 * Take <code>Collections.size()</code> as an example. This method cannot be
 * called by the EL without adding some sort of wrapper that has a getSize()
 * method, or adding a JSP Function. With this resolver however, the user can
 * write <code>${someCollection.size}</code> and obtain the size of the
 * <code>Collection</code>.
 * </p>
 */
public class SimpleMethodELResolver extends ELResolver {

  private static final Class[] NO_TYPES = new Class[0];

  private static final Object[] NO_ARGS = new Object[0];

  /**
   * <p>
   * This will attempt to call a method identified by the <code>property</code>
   * parameter on the specified <code>base</code> using reflection.
   *
   * If the reflected method has no arguments,
   * <code>context.setPropertyResolved(true)</code> is called, the method is
   * invoked, and the value returned.
   *
   * This method will not throw <code>javax.el.PropertyNotFoundException</code>
   * if an appropriate method cannot be found to invoke.
   * </p>
   *
   *
   * @param context
   *          The context of this evaluation.
   * @param base
   *          The base object whose property value is to be returned, or
   *          <code>null</code> to resolve a top-level variable.
   * @param property
   *          The property or variable to be resolved.
   *
   * @return If the <code>propertyResolved</code> property of
   *         <code>ELContext</code> was set to <code>true</code>, then the
   *         result of the variable or property resolution; otherwise undefined.
   *
   * @throws NullPointerException
   *           if context is <code>null</code>
   * @throws javax.el.PropertyNotFoundException
   *           if the given (base, property) pair is handled by this
   *           <code>ELResolver</code> but the specified variable or property
   *           does not exist or is not readable.
   * @throws javax.el.ELException
   *           if an exception was thrown while performing the property or
   *           variable resolution. The thrown exception must be included as the
   *           cause property of this exception, if available.
   */
  public Object getValue(ELContext context, Object base, Object property) {

    Object result = null;

    if (context == null) {
      throw new NullPointerException();
    }

    if (base == null) {
      context.setPropertyResolved(false);
    } else {
      String methodName = (String) property;
      Method method;
      try {
        method = base.getClass().getMethod(methodName, NO_TYPES);
        if (method.getParameterTypes().length == 0) {
          result = method.invoke(base, NO_ARGS);
          context.setPropertyResolved(true);
        } else {
          context.setPropertyResolved(false);
        }
      } catch (NoSuchMethodException nsme) {
        context.setPropertyResolved(false);
      } catch (Exception e) {
        context.setPropertyResolved(false);
        Throwable cause;
        if (e instanceof InvocationTargetException) {
          cause = ((InvocationTargetException) e).getTargetException();
        } else {
          cause = e;
        }
        throw new ELException(cause);
      }
    }

    return result;
  }

  /**
   * <p>
   * This method will attempt to return the return-type of the reflected method,
   * if appropriate to do so.
   *
   * This method will not throw <code>javax.el.PropertyNotFoundException</code>
   * if an appropriate method cannot be found to invoke.
   * </p>
   *
   * @param context
   *          The context of this evaluation.
   * @param base
   *          The base object whose property value is to be analyzed, or
   *          <code>null</code> to analyze a top-level variable.
   * @param property
   *          The property or variable to return the acceptable type for.
   *
   * @return If the <code>propertyResolved</code> property of
   *         <code>ELContext</code> was set to <code>true</code>, then the most
   *         general acceptable type; otherwise undefined.
   *
   * @throws NullPointerException
   *           if context is <code>null</code>
   * @throws javax.el.PropertyNotFoundException
   *           if the given (base, property) pair is handled by this
   *           <code>ELResolver</code> but the specified variable or property
   *           does not exist or is not readable.
   * @throws javax.el.ELException
   *           if an exception was thrown while performing the property or
   *           variable resolution. The thrown exception must be included as the
   *           cause property of this exception, if available.
   */
  public Class getType(ELContext context, Object base, Object property) {
    Class result = null;
    if (context == null) {
      throw new NullPointerException();
    }

    if (base == null) {
      context.setPropertyResolved(false);
    } else {
      String methodName = (String) property;
      Method method;
      try {
        method = base.getClass().getMethod(methodName, NO_TYPES);
        if (method.getParameterTypes().length == 0) {
          result = method.getReturnType();
          context.setPropertyResolved(true);
        } else {
          context.setPropertyResolved(false);
        }
      } catch (NoSuchMethodException nsme) {
        context.setPropertyResolved(false);
      } catch (Exception e) {
        context.setPropertyResolved(false);
        Throwable cause;
        if (e instanceof InvocationTargetException) {
          cause = ((InvocationTargetException) e).getTargetException();
        } else {
          cause = e;
        }
        throw new ELException(cause);
      }
    }

    return result;
  }

  /**
   * <p>
   * <code>setValue()</code> is not supported by this resolver.
   * </p>
   *
   * @param context
   *          The context of this evaluation.
   * @param base
   *          The base object whose property value is to be set, or
   *          <code>null</code> to set a top-level variable.
   * @param property
   *          The property or variable to be set.
   * @param value
   *          The value to set the property or variable to.
   *
   * @throws NullPointerException
   *           if context is <code>null</code>
   * @throws javax.el.PropertyNotFoundException
   *           if the given (base, property) pair is handled by this
   *           <code>ELResolver</code> but the specified variable or property
   *           does not exist.
   * @throws javax.el.PropertyNotWritableException
   *           if the given (base, property) pair is handled by this
   *           <code>ELResolver</code> but the specified variable or property is
   *           not writable.
   * @throws javax.el.ELException
   *           if an exception was thrown while attempting to set the property
   *           or variable. The thrown exception must be included as the cause
   *           property of this exception, if available.
   */
  public void setValue(ELContext context, Object base, Object property,
      Object value) {

    if (context == null) {
      throw new NullPointerException();
    }

    context.setPropertyResolved(false); // basically a no-op.

  }

  /**
   * <p>
   * <code>isReadOnly</code> returns true if it is able to resolve the specified
   * property to a no-argument method
   * </p>
   *
   * @param context
   *          The context of this evaluation.
   * @param base
   *          The base object whose property value is to be analyzed, or
   *          <code>null</code> to analyze a top-level variable.
   * @param property
   *          The property or variable to return the read-only status for.
   *
   * @return If the <code>propertyResolved</code> property of
   *         <code>ELContext</code> was set to <code>true</code>, then
   *         <code>true</code> if the property is read-only or
   *         <code>false</code> if not; otherwise undefined.
   *
   * @throws NullPointerException
   *           if context is <code>null</code>
   * @throws javax.el.PropertyNotFoundException
   *           if the given (base, property) pair is handled by this
   *           <code>ELResolver</code> but the specified variable or property
   *           does not exist.
   * @throws javax.el.ELException
   *           if an exception was thrown while performing the property or
   *           variable resolution. The thrown exception must be included as the
   *           cause property of this exception, if available.
   */
  public boolean isReadOnly(ELContext context, Object base, Object property) {

    if (context == null) {
      throw new NullPointerException();
    }

    if (base == null) {
      context.setPropertyResolved(false);
    } else {
      String methodName = (String) property;
      Method method;
      try {
        method = base.getClass().getMethod(methodName, NO_TYPES);
        if (method.getParameterTypes().length == 0) {
          context.setPropertyResolved(true);
          return true;
        } else {
          context.setPropertyResolved(false);
        }
      } catch (NoSuchMethodException nsme) {
        context.setPropertyResolved(false);
      }
    }

    return false;

  }

  /**
   * Returns information about the set of variables or properties that can be
   * resolved for the given <code>base</code> object. One use for this method is
   * to assist tools in auto-completion.
   * <p/>
   * <p>
   * If the <code>base</code> parameter is <code>null</code>, the resolver must
   * enumerate the list of top-level variables it can resolve.
   * </p>
   * <p/>
   * <p>
   * The <code>Iterator</code> returned must contain zero or more instances of
   * {@link java.beans.FeatureDescriptor}, in no guaranteed order. In the case
   * of primitive types such as <code>int</code>, the value <code>null</code>
   * must be returned. This is to prevent the useless iteration through all
   * possible primitive values. A return value of <code>null</code> indicates
   * that this resolver does not handle the given <code>base</code> object or
   * that the results are too complex to represent with this method and the
   * {@link #getCommonPropertyType} method should be used instead.
   * </p>
   * <p/>
   * <p>
   * Each <code>FeatureDescriptor</code> will contain information about a single
   * variable or property. In addition to the standard properties, the
   * <code>FeatureDescriptor</code> must have two named attributes (as set by
   * the <code>setValue</code> method):
   * <ul>
   * <li>{@link #TYPE} - The value of this named attribute must be an instance
   * of <code>java.lang.Class</code> and specify the runtime type of the
   * variable or property.</li>
   * <li>{@link #RESOLVABLE_AT_DESIGN_TIME} - The value of this named attribute
   * must be an instance of <code>java.lang.Boolean</code> and indicates whether
   * it is safe to attempt to resolve this property at design-time. For
   * instance, it may be unsafe to attempt a resolution at design time if the
   * <code>ELResolver</code> needs access to a resource that is only available
   * at runtime and no acceptable simulated value can be provided.</li>
   * </ul>
   * </p>
   * <p/>
   * <p>
   * The caller should be aware that the <code>Iterator</code> returned might
   * iterate through a very large or even infinitely large set of properties.
   * Care should be taken by the caller to not get stuck in an infinite loop.
   * </p>
   * <p/>
   * <p>
   * This is a "best-effort" list. Not all <code>ELResolver</code>s will return
   * completely accurate results, but all must be callable at both design-time
   * and runtime (i.e. whether or not <code>Beans.isDesignTime()</code> returns
   * <code>true</code>), without causing errors.
   * </p>
   * <p/>
   * <p>
   * The <code>propertyResolved</code> property of the <code>ELContext</code> is
   * not relevant to this method. The results of all <code>ELResolver</code>s
   * are concatenated in the case of composite resolvers.
   * </p>
   *
   * @param context
   *          The context of this evaluation.
   * @param base
   *          The base object whose set of valid properties is to be enumerated,
   *          or <code>null</code> to enumerate the set of top-level variables
   *          that this resolver can evaluate.
   *
   * @return An <code>Iterator</code> containing zero or more (possibly
   *         infinitely more) <code>FeatureDescriptor</code> objects, or
   *         <code>null</code> if this resolver does not handle the given
   *         <code>base</code> object or that the results are too complex to
   *         represent with this method
   *
   * @see java.beans.FeatureDescriptor
   */
  public Iterator getFeatureDescriptors(ELContext context, Object base) {

    Set<FeatureDescriptor> methodSet = new HashSet<FeatureDescriptor>();
    if (context == null || base == null) {
      return null;
    }

    Method[] methods = base.getClass().getDeclaredMethods();
    for (Method method : methods) {
      // only interested in no-arg methods
      if (method.getParameterTypes().length == 0) {
        FeatureDescriptor desc = new FeatureDescriptor();
        desc.setDisplayName(method.getName());
        desc.setShortDescription(method.getName());
        desc.setHidden(false);
        desc.setExpert(false);
        desc.setPreferred(true);
        desc.setValue(ELResolver.RESOLVABLE_AT_DESIGN_TIME, Boolean.TRUE);
        desc.setValue(ELResolver.TYPE, method.getReturnType());
        methodSet.add(desc);
      }
    }

    return methodSet.iterator();

  }

  /**
   * Returns the most general type that this resolver accepts for the
   * <code>property</code> argument, given a <code>base</code> object. One use
   * for this method is to assist tools in auto-completion.
   * <p/>
   * <p>
   * This assists tools in auto-completion and also provides a way to express
   * that the resolver accepts a primitive value, such as an integer index into
   * an array. For example, the {@link javax.el.ArrayELResolver} will accept any
   * <code>int</code> as a <code>property</code>, so the return value would be
   * <code>Integer.class</code>.
   * </p>
   *
   * @param context
   *          The context of this evaluation.
   * @param base
   *          The base object to return the most general property type for, or
   *          <code>null</code> to enumerate the set of top-level variables that
   *          this resolver can evaluate.
   *
   * @return <code>null</code> if this <code>ELResolver</code> does not know how
   *         to handle the given <code>base</code> object; otherwise
   *         <code>Object.class</code> if any type of <code>property</code> is
   *         accepted; otherwise the most general <code>property</code> type
   *         accepted for the given <code>base</code>.
   */
  public Class getCommonPropertyType(ELContext context, Object base) {

    return null;

  }

}
