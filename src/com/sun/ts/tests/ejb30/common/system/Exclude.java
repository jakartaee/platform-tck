/*
 * Copyright (c) 2021 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.ejb30.common.system;

/**
 * A way for tests to check if an optional technology is excluded.  
 * This is only for special technologies that cannot be reasonably 
 * represented by a keyword property.  
 *
 * @author Scott Marlow
 */
public class Exclude {

    /**
     * @return true if Corba is excluded from TCK testing.
     */
    public static boolean ignoreCorba() {
        return Boolean.getBoolean("ExcludeCorba");
      }
}
