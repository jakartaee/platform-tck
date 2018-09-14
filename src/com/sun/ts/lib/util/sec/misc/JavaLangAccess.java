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

package com.sun.ts.lib.util.sec.misc;

import com.sun.ts.lib.util.sec.reflect.ConstantPool;
import com.sun.ts.lib.util.sec.reflect.annotation.AnnotationType;
import com.sun.ts.lib.util.sec.nio.ch.Interruptible;

public interface JavaLangAccess {
  /** Return the constant pool for a class. */
  ConstantPool getConstantPool(Class klass);

  /**
   * Set the AnnotationType instance corresponding to this class. (This method
   * only applies to annotation types.)
   */
  void setAnnotationType(Class klass, AnnotationType annotationType);

  /**
   * Get the AnnotationType instance corresponding to this class. (This method
   * only applies to annotation types.)
   */
  AnnotationType getAnnotationType(Class klass);

  /**
   * Returns the elements of an enum class or null if the Class object does not
   * represent an enum type; the result is uncloned, cached, and shared by all
   * callers.
   */
  <E extends Enum<E>> E[] getEnumConstantsShared(Class<E> klass);

  /** Set thread's blocker field. */
  void blockedOn(Thread t, Interruptible b);
}
