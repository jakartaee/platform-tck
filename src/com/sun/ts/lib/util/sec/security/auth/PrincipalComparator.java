/*
 * Copyright (c) 1999, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.lib.util.sec.security.auth;

/**
 * An object that implements the <code>java.security.Principal</code> interface
 * typically also implements this interface to provide a means for comparing
 * that object to a specified <code>Subject</code>.
 *
 * <p>
 * The comparison is achieved via the <code>implies</code> method. The
 * implementation of the <code>implies</code> method determines whether this
 * object "implies" the specified <code>Subject</code>. One example application
 * of this method may be for a "group" object to imply a particular
 * <code>Subject</code> if that <code>Subject</code> belongs to the group.
 * Another example application of this method would be for "role" object to
 * imply a particular <code>Subject</code> if that <code>Subject</code> is
 * currently acting in that role.
 *
 * <p>
 * Although classes that implement this interface typically also implement the
 * <code>java.security.Principal</code> interface, it is not required. In other
 * words, classes may implement the <code>java.security.Principal</code>
 * interface by itself, the <code>PrincipalComparator</code> interface by
 * itself, or both at the same time.
 *
 * @see java.security.Principal
 * @see javax.security.auth.Subject
 */
public interface PrincipalComparator {
  /**
   * Check if the specified <code>Subject</code> is implied by this object.
   *
   * <p>
   *
   * @return true if the specified <code>Subject</code> is implied by this
   *         object, or false otherwise.
   */
  boolean implies(javax.security.auth.Subject subject);
}
