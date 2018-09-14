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

package com.sun.ts.tests.javamail.ee.getMessageContent;

import java.util.*;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.harness.*;
import com.sun.ts.lib.harness.EETest.Fault;
import com.sun.javatest.Status;

import javax.annotation.Resource;
import javax.ejb.EJB;

import com.sun.ts.tests.common.vehicle.ejb.EJBVehicleHome;

import java.io.Serializable;

import javax.mail.*;

import com.sun.ts.tests.javamail.ee.common.MailTestUtil;

public class getMessageContent_Test extends ServiceEETest {

  private int errors = 0; // number of unit test errors

  private MailTestUtil mailTestUtil;

  private Folder folder;

  private Store store;

  private int msgcount = -1;

  private Status status;

  private String rootPath;

  public static void main(String[] args) {
    getMessageContent_Test theTests = new getMessageContent_Test();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /* Test setup: */
  /*
   * @class.setup_props: javamail.protocol; javamail.server; javamail.username;
   * javamail.password ; javamail.mailbox; javamail.root.path; smtp.port;
   * imap.port;
   * 
   */
  public void setup(String[] args, Properties props) throws Fault {
    try {

      String protocol = TestUtil.getProperty("javamail.protocol");
      String host = TestUtil.getProperty("javamail.server");
      String user = TestUtil.getProperty("javamail.username");
      String password = TestUtil.getProperty("javamail.password");
      String mailbox = TestUtil.getProperty("javamail.mailbox");
      rootPath = TestUtil.getProperty("javamail.root.path");

      String smtpPortStr = TestUtil.getProperty("smtp.port");
      int smtpPort = Integer.parseInt(smtpPortStr);
      TestUtil.logTrace("SMTP Port = " + smtpPort);

      String imapPortStr = TestUtil.getProperty("imap.port");
      int imapPort = Integer.parseInt(imapPortStr);
      TestUtil.logTrace("IMAP Port = " + imapPort);

      MailTestUtil mailTestUtil = new MailTestUtil();
      store = mailTestUtil.connect2host(protocol, host, imapPort, user,
          password);

      // Get a Folder object
      Folder root = getRootFolder(store);
      folder = root.getFolder(mailbox);

      if (folder == null) {
        throw new Fault("Invalid folder object!");
      }
      folder.open(Folder.READ_ONLY);

      if (msgcount == -1) {
        msgcount = folder.getMessageCount();
        if (msgcount < 1)
          throw new Fault("Mail folder is empty!");
      }

    } catch (Exception e) {
      logErr("Exception : " + e.getMessage());
      logErr("Setup Failed!");
      TestUtil.printStackTrace(e);
    }
  }

  /*
   * @testName: test1
   * 
   * @assertion_ids: JavaEE:SPEC:235;
   * 
   * @test_Strategy: getMessageContent test
   */
  // derived from javamail suite getMessageContent_Test

  public void test1() throws Fault {

    try {
      Message msg, cmsg;
      int size_in_bytes1;
      int size_in_bytes2;

      for (int i = 1; i <= msgcount; i++) {
        TestUtil.logMsg("UNIT TEST " + i + ": getMessage(" + i + ")");

        msg = folder.getMessage(i); // API TEST
        cmsg = folder.getMessage(i); // API TEST

        if (msg != null && (msg instanceof Message)) {
          size_in_bytes1 = msg.getSize();
          size_in_bytes2 = cmsg.getSize();
          if (size_in_bytes1 == size_in_bytes2)
            TestUtil.logMsg("UNIT TEST " + i + ": passed\n");
        } else {
          TestUtil.logMsg("UNIT TEST " + i + ": FAILED\n");
          errors++;
        }
      }
      TestUtil.logMsg("\n");

      folder.close(false);
      store.close();

      checkStatus();

      if (errors > 0) {
        throw new Fault("test1 Failed: No of unit test failed = " + errors);
      }

    } catch (Exception e) {
      logErr("Unexpected Exception " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("test1 Failed");

    }
  } // end of test1()

  public void checkStatus() {
    if (errors == 0)
      status = Status.passed("OKAY");
    else
      status = Status.failed("");
  }

  /* cleanup */
  public void cleanup() throws Fault {
    try {
      logMsg("Cleanup ;");
    } catch (Exception e) {
      logErr("An error occurred in cleanup!", e);
    }
  }

  public Store connect2host(String proto, String host, String user,
      String password) {
    Store store = null;

    try {
      // Get a Session object
      TSNamingContextInterface nctx = new TSNamingContext();
      Session session = (Session) nctx.lookup("java:comp/env/mail/MailSession");

      if (session == null) {
        TestUtil.logMsg("Warning: Failed to create a Session object!");
        return null;
      }

      // Get a Store object
      store = session.getStore(proto);

      if (store == null) {
        TestUtil.logMsg("Warning: Failed to create a Store object!");
        return null;
      }
      int portnum = -1;
      // Connect
      if (host != null || user != null || password != null)
        if (portnum > 0)
          store.connect(host, portnum, user, password);
        else
          store.connect(host, user, password);
      else
        store.connect();

    } catch (Exception e) {
      e.printStackTrace();
    }
    return store;
  }

  /**
   * Get the root folder.
   */
  public Folder getRootFolder(Store store) {
    Folder folder = null;
    try {
      if (rootPath.equals(""))
        folder = store.getDefaultFolder();
      else
        folder = store.getFolder(rootPath);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return folder;
  }

}
