/*
 * Copyright (c) 2003, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.lib.util.sec.reflect.annotation;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents an annotation type at run time. Used to type-check annotations and apply member defaults.
 *
 * @author Josh Bloch
 * @since 1.5
 */
public class AnnotationType {
    /**
     * Member name -> type mapping. Note that primitive types are represented by the class objects for the corresponding
     * wrapper types. This matches the return value that must be used for a dynamic proxy, allowing for a simple isInstance
     * test.
     */
    private final Map<String, Class> memberTypes = new HashMap<String, Class>();

    /**
     * Member name -> default value mapping.
     */
    private final Map<String, Object> memberDefaults = new HashMap<String, Object>();

    /**
     * Member name -> Method object mapping. This (and its assoicated accessor) are used only to generate
     * AnnotationTypeMismatchExceptions.
     */
    private final Map<String, Method> members = new HashMap<String, Method>();

    /**
     * The retention policy for this annotation type.
     */
    private RetentionPolicy retention = RetentionPolicy.RUNTIME;;

    /**
     * Whether this annotation type is inherited.
     */
    private boolean inherited = false;

    /**
     * Returns an AnnotationType instance for the specified annotation type.
     * 
     * @param annotationClass the class object for the annotation type
     * @return an AnnotationType instance for the specified annotation type
     */
    public static synchronized AnnotationType getInstance(Class annotationClass) {
        AnnotationType result = com.sun.ts.lib.util.sec.misc.SharedSecrets.getJavaLangAccess().getAnnotationType(annotationClass);
        if (result == null)
            result = new AnnotationType((Class<?>) annotationClass);

        return result;
    }

    /**
     * Sole constructor.
     *
     * @param annotationClass the class object for the annotation type
     */
    private AnnotationType(final Class<?> annotationClass) {
        if (!annotationClass.isAnnotation())
            throw new IllegalArgumentException("Not an annotation type");

        Method[] methods = annotationClass.getDeclaredMethods();
        for (Method method : methods) {
            if (method.getParameterTypes().length != 0)
                throw new IllegalArgumentException(method + " has params");
            String name = method.getName();
            Class type = method.getReturnType();
            memberTypes.put(name, invocationHandlerReturnType(type));
            members.put(name, method);

            Object defaultValue = method.getDefaultValue();
            if (defaultValue != null)
                memberDefaults.put(name, defaultValue);

            members.put(name, method);
        }

        com.sun.ts.lib.util.sec.misc.SharedSecrets.getJavaLangAccess().setAnnotationType(annotationClass, this);

        // Initialize retention, & inherited fields. Special treatment
        // of the corresponding annotation types breaks infinite recursion.
        if (annotationClass != Retention.class && annotationClass != Inherited.class) {
            Retention ret = annotationClass.getAnnotation(Retention.class);
            retention = (ret == null ? RetentionPolicy.CLASS : ret.value());
            inherited = annotationClass.isAnnotationPresent(Inherited.class);
        }
    }

    /**
     * Returns the type that must be returned by the invocation handler of a dynamic proxy in order to have the dynamic
     * proxy return the specified type (which is assumed to be a legal member type for an annotation).
     * 
     * @param type the type to be returned by the invocation handler
     * @return the type that must be returned by the invocation handler of a dynamic proxy in order to have the dynamic
     */
    public static Class invocationHandlerReturnType(Class type) {
        // Translate primitives to wrappers
        if (type == byte.class)
            return Byte.class;
        if (type == char.class)
            return Character.class;
        if (type == double.class)
            return Double.class;
        if (type == float.class)
            return Float.class;
        if (type == int.class)
            return Integer.class;
        if (type == long.class)
            return Long.class;
        if (type == short.class)
            return Short.class;
        if (type == boolean.class)
            return Boolean.class;

        // Otherwise, just return declared type
        return type;
    }

    /**
     * @return member types for this annotation type (member name -> type mapping).
     */
    public Map<String, Class> memberTypes() {
        return memberTypes;
    }

    /**
     * @return members of this annotation type (member name -> associated Method object mapping).
     */
    public Map<String, Method> members() {
        return members;
    }

    /**
     * @return the default values for this annotation type (Member name -> default value mapping).
     */
    public Map<String, Object> memberDefaults() {
        return memberDefaults;
    }

    /**
     * @return the retention policy for this annotation type.
     */
    public RetentionPolicy retention() {
        return retention;
    }

    /**
     * @return true if this this annotation type is inherited.
     */
    public boolean isInherited() {
        return inherited;
    }

    /**
     * For debugging.
     * 
     * @return a string representation of this annotation type.
     */
    public String toString() {
        StringBuffer s = new StringBuffer("Annotation Type:" + "\n");
        s.append("   Member types: " + memberTypes + "\n");
        s.append("   Member defaults: " + memberDefaults + "\n");
        s.append("   Retention policy: " + retention + "\n");
        s.append("   Inherited: " + inherited);
        return s.toString();
    }
}
