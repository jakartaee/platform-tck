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

package com.sun.ts.tests.connector.util;

import com.sun.ts.lib.util.*;
import java.util.*;
import com.sun.ts.tests.common.connector.whitebox.*;

/* 
 * This class serves as a Database access utility for the Connector tests. 
 *
 * This class is useful for being used in some transaction tests, or in validating
 * connection tests, to assure that you have a valid Connection object returned.
 *
 */

public class DBSupport {

  private Properties props = null;

  private String updateString = null;

  private String removeString = null;

  private String str1;

  private int count;

  public DBSupport() throws Exception {
    count = 0;
  }

  /*
   * Insert into the above table with the provided TSConnection object.
   */
  public void insertIntoTable(TSConnection conn) throws Exception {
    TestUtil.logTrace("DBSupport.insertIntoTable(con)");
    String tempStr = String.valueOf(count);
    str1 = new String(tempStr);
    conn.insert(str1, "Hello");
    count++;
  }

  /*
   * Drop the above table.
   */
  public void dropTable(TSConnection conn) throws Exception {
    clearTable(conn);
  }

  /*
   * New private class for dropTable(conn) and createTable(conn). See the
   * constructor comments.
   *
   */
  private void clearTable(TSConnection conn) throws Exception {
    conn.dropTable();

  }
}
