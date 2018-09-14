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

package com.sun.ts.lib.harness;

import java.io.*;

/**
 * This abstract class must be extended by all API clients of tests of J2EE
 * service apis; for example, JDBC, RMI-IIOP, JavaMail, JMS, etc. This allows us
 * to bundle a number of API tests into a single J2EE component and reduces the
 * number of such components that have to be deployed.
 * 
 * @author Vella Raman
 */
public abstract class JCKServiceEETest extends ServiceEETest
    implements Serializable {
}
