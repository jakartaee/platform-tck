/*
 * Copyright (c) 2007, 2024 Oracle and/or its affiliates. All rights reserved.
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

/*
 * @(#)TxBean.java  1.0 02/07/31
 */

package com.sun.ts.tests.xa.ee.resXcomp1;

import java.util.Properties;
import java.util.Vector;

public interface TxBean {
    void initialize();
    void dbConnect(String tName);
    void createData(String tName);
    boolean insert(String tName, int key);
    void delete(String tName, int fromKey, int toKey);
    void destroyData(String tName);
    void dbUnConnect(String tName);
    Vector getResults(String tName);
    void initLogging(Properties p);
    void throwEJBException();
    void listTableData(Vector dbResults);
}
