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

package com.sun.ts.tests.concurrency.spec.ManagedExecutorService.tx;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.annotation.Resource;
import javax.sql.DataSource;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.util.TestUtil;

public class Client extends EETest {

  private String host = null;

  private int port;

  private Properties props = null;

  private String appendedURL = "";

  private String username, password;

  @Resource(lookup = Constants.DS_JNDI_NAME)
  private static DataSource ds;

  private Connection conn;

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home; Driver, the
   * Driver name; db1, the database name with url; user1, the database user
   * name; password1, the database password; db2, the database name with url;
   * user2, the database user name; password2, the database password;
   * DriverManager, flag for DriverManager; ptable, the primary table; ftable,
   * the foreign table; cofSize, the initial size of the ptable; cofTypeSize,
   * the initial size of the ftable; binarySize, size of binary data type;
   * varbinarySize, size of varbinary data type; longvarbinarySize, size of
   * longvarbinary data type;
   * 
   * @class.testArgs: -ap tssql.stmt
   */
  public void setup(String[] args, Properties p) throws Fault {
    TestUtil.logMsg("setup");
    try {
      // get props
      port = Integer.parseInt(p.getProperty("webServerPort"));
      host = p.getProperty("webServerHost");

      // check props for errors
      if (port < 1) {
        throw new Exception("'port' in ts.jte must be > 0");
      }
      if (host == null) {
        throw new Exception("'host' in ts.jte must not be null ");
      }

      props = new Properties(p);
      appendedURL = appendedURL(p);
      username = p.getProperty(Constants.USERNAME);
      password = p.getProperty(Constants.PASSWORD);
      removeTestData();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("Setup failed!", e);
    }
  }

  /*
   * @testName: testCommitTransactionWithManagedExecutorService
   * 
   * @assertion_ids:
   * CONCURRENCY:SPEC:30;CONCURRENCY:SPEC:31;CONCURRENCY:SPEC:31.1;
   * CONCURRENCY:SPEC:31.2;CONCURRENCY:SPEC:32;CONCURRENCY:SPEC:33;
   * CONCURRENCY:SPEC:34;CONCURRENCY:SPEC:36; CONCURRENCY:SPEC:38;
   * CONCURRENCY:SPEC:8.1;CONCURRENCY:SPEC:9;CONCURRENCY:SPEC:39;
   * CONCURRENCY:SPEC:39.1;CONCURRENCY:SPEC:39.2;CONCURRENCY:SPEC:4.1;
   * CONCURRENCY:SPEC:4.4;CONCURRENCY:SPEC:92.2;CONCURRENCY:SPEC:92.3;
   * CONCURRENCY:SPEC:92.5;CONCURRENCY:SPEC:41;
   * 
   * @test_Strategy: get UserTransaction inside one task submitted by
   * ManagedExecutorService.it support user-managed global transaction
   * demarcation using the javax.transaction.UserTransaction interface.
   */

  public void testCommitTransactionWithManagedExecutorService() throws Fault {
    URL url;
    String resp = null;
    try {
      url = new URL(Util.getUrl(Constants.TX_SERVLET_URI, host, port)
          + appendedURL + "&" + Constants.PARAM_COMMIT + "=true");
      resp = TestUtil.getResponse(url.openConnection());
    } catch (MalformedURLException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    if (!Message.SUCCESSMESSAGE.equals(resp)) {
      throw new Fault(
          "testCommitTransactionWithManagedExecutorService fail to get successful result.");
    }
  }

  /*
   * @testName: testRollbackTransactionWithManagedExecutorService
   * 
   * @assertion_ids: CONCURRENCY:SPEC:31.3;CONCURRENCY:SPEC:39.3;
   * CONCURRENCY:SPEC:92.2;CONCURRENCY:SPEC:92.3;
   * 
   * @test_Strategy: get UserTransaction inside one task submitted by
   * ManagedExecutorService. test roll back function in the submitted task.
   */

  public void testRollbackTransactionWithManagedExecutorService() throws Fault {
    URL url;
    String resp = null;
    try {
      url = new URL(Util.getUrl(Constants.TX_SERVLET_URI, host, port)
          + appendedURL + "&" + Constants.PARAM_COMMIT + "=false");
      resp = TestUtil.getResponse(url.openConnection());
    } catch (MalformedURLException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    if (!Message.SUCCESSMESSAGE.equals(resp)) {
      throw new Fault(
          "testRollbackTransactionWithManagedExecutorService fail to get successful result.");
    }
  }

  /*
   * @testName: testCancelTransactionWithManagedExecutorService
   * 
   * @assertion_ids:
   * CONCURRENCY:SPEC:112;CONCURRENCY:SPEC:35;CONCURRENCY:SPEC:68;CONCURRENCY:
   * SPEC:91.4;
   * 
   * @test_Strategy: get UserTransaction inside one task submitted by
   * ManagedExecutorService.cancel the task after submit one task.
   */

  public void testCancelTransactionWithManagedExecutorService() throws Fault {
    URL url;
    String resp = null;
    try {
      url = new URL(
          Util.getUrl(Constants.TX_SERVLET_URI, host, port) + appendedURL + "&"
              + Constants.PARAM_COMMIT + "=" + Constants.PARAM_VALUE_CANCEL);
      resp = TestUtil.getResponse(url.openConnection());
    } catch (MalformedURLException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    if (!Message.SUCCESSMESSAGE.equals(resp)) {
      throw new Fault(
          "testCancelTransactionWithManagedExecutorService fail to get successful result.");
    }
  }

  private String appendedURL(Properties p) throws UnsupportedEncodingException {
    StringBuffer sb = new StringBuffer("?");
    sb.append(Constants.USERNAME + "=" + p.get(Constants.USERNAME));
    sb.append("&");
    sb.append(Constants.PASSWORD + "=" + p.get(Constants.PASSWORD));
    sb.append("&");
    sb.append(Constants.TABLE_P + "=" + Constants.TABLE_P);
    sb.append("&");
    sb.append(Constants.SQL_TEMPLATE + "="
        + URLEncoder.encode(p.get(Constants.SQL_TEMPLATE).toString(), "utf8"));
    return sb.toString();
  }

  public void cleanup() throws Fault {
    try {
      removeTestData();
    } catch (Exception e) {
      e.printStackTrace();
      throw new Fault("cleanup failed.", e);
    }
    TestUtil.logTrace("test cleanup ok");
  }

  private void removeTestData() throws RemoteException {
    TestUtil.logTrace("removeTestData");

    // init connection.
    conn = Util.getConnection(ds, username, password, true);
    String removeString = props.getProperty("Dbschema_Concur_Delete", "");
    try {
      Statement stmt = conn.createStatement();
      stmt.executeUpdate(removeString);
      stmt.close();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);

      throw new RemoteException(e.getMessage());
    } finally {
      try {
        conn.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }

  }
}
