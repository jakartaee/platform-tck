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

package com.sun.ts.tests.concurrency.spec.ManagedScheduledExecutorService.tx;

import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.transaction.UserTransaction;

import com.sun.ts.lib.util.TestUtil;

public class TransactedTask implements Runnable {
  private final boolean isCommit;

  private final String username, password, sqlTemplate;

  public TransactedTask(boolean commitOrRollback, String username,
      String password, String sqlTemplate) {
    this.username = username;
    this.password = password;
    this.sqlTemplate = sqlTemplate;
    isCommit = commitOrRollback;
  }

  @Override
  public void run() {
    boolean pass = false;
    Connection conn = null;
    String tableName = TestUtil.getProperty(Constants.TABLE_P,
        Constants.DEFAULT_PTABLE);
    int originCount = Util.getCount(tableName, username, password);

    UserTransaction ut = Util.lookup(Constants.UT_JNDI_NAME);
    if (ut == null) {
      // error if no transaction can be obtained in task.
      throw new RuntimeException(
          "didn't get user transaction inside the submitted task.");
    } else {
      PreparedStatement pStmt = null;
      try {
        ut.begin();
        conn = Util.getConnection(false, username, password);
        pStmt = conn.prepareStatement(sqlTemplate);
        String sTypeDesc = "Type-99";
        int newType = 99;
        pStmt.setInt(1, newType);
        pStmt.setString(2, sTypeDesc);
        pStmt.executeUpdate();
        // commit or roll back transaction.
        if (isCommit) {
          ut.commit();
        } else {
          ut.rollback();
        }
        // check status.
        int afterTransacted = Util.getCount(tableName, username, password);
        if (isCommit) {
          pass = (afterTransacted == originCount + 1);
        } else {
          pass = (afterTransacted == originCount);
        }
      } catch (Exception e) {
        try {
          ut.rollback();
        } catch (Exception e1) {
          e1.printStackTrace();
        }
        e.printStackTrace();
      } finally {
        try {
          pStmt.close();
          conn.close();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
      if (!pass) {
        throw new RuntimeException(
            "didn't get expected result with transacted task.");
      }
    }
  }

}
