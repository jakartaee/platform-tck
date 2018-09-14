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

package com.sun.ts.tests.javamail.ee.fetchprofile;

import java.util.*;
import com.sun.ts.lib.util.*;
import com.sun.ts.lib.harness.*;
import com.sun.ts.tests.javamail.ee.common.MailTestUtil;
import com.sun.javatest.Status;

import java.io.Serializable;
import javax.mail.*;

public class fetchprofile_Test extends ServiceEETest implements Serializable {

  private transient FetchProfile fp = null;

  private int errors = 0; // number of unit test errors

  private Folder folder;

  private Message[] msgs;

  private Store store;

  private transient Status status;

  private String rootPath;

  private transient Session session;

  public static void main(String[] args) {
    fetchprofile_Test theTests = new fetchprofile_Test();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /* Test setup: */
  /*
   * @class.setup_props: javamail.protocol; javamail.server; javamail.username;
   * javamail.password ; javamail.mailbox; javamail.root.path; smtp.port;
   * imap.port;
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

      // Create an empty FetchProfile
      fp = new FetchProfile();

      if (fp == null) {
        throw new Fault("Failed to create an empty FetchProfile object!");
      }
      // Get all the messages
      msgs = folder.getMessages();

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
   * @test_Strategy: verify FetchProfile()
   */
  // derived from javamail suite fetchProfile_Test class

  public void test1() throws Fault {

    try {
      // BEGIN UNIT TEST 1:
      TestUtil.logTrace("UNIT TEST 1:  FetchProfile()");

      FetchProfile fp = new FetchProfile(); // API TEST

      if (fp != null) {
        TestUtil.logTrace("UNIT TEST 1: passed\n");
      }
      // END UNIT TEST 1:

    } catch (Exception e) {
      logErr("Unexpected Exception " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("test1 Failed");

    }
  } // end of test1()

  /*
   * @testName: test2
   * 
   * @assertion_ids: JavaEE:SPEC:235;
   * 
   * @test_Strategy: verify FetchProfile()
   */
  // derived from javamail suite fetchProfile_Test class
  public void test2() throws Fault {

    TestUtil.logTrace(
        "\nTesting class FetchProfile: add(FetchProfile.Item | String)\n");

    try {

      // BEGIN UNIT TEST 1:

      TestUtil.logTrace("UNIT TEST 1: add(FetchProfile.Item.ENVELOPE)");

      fp.add(FetchProfile.Item.ENVELOPE); // API TEST

      if (fp.contains(FetchProfile.Item.ENVELOPE))
        TestUtil.logTrace("UNIT TEST 1: passed.\n");
      else {
        TestUtil.logTrace("UNIT TEST 1: FAILED.\n");
        errors++;
      }
      // END UNIT TEST 1:
      // BEGIN UNIT TEST 2:

      TestUtil.logTrace("UNIT TEST 2: add(FetchProfile.Item.FLAGS)");

      fp.add(FetchProfile.Item.FLAGS); // API TEST

      if (fp.contains(FetchProfile.Item.FLAGS))
        TestUtil.logTrace("UNIT TEST 2: passed.\n");
      else {
        TestUtil.logTrace("UNIT TEST 2: FAILED.\n");
        errors++;
      }
      // END UNIT TEST 2:
      // BEGIN UNIT TEST 3:

      TestUtil.logTrace("UNIT TEST 3: add(FetchProfile.Item.CONTENT_INFO)");

      fp.add(FetchProfile.Item.CONTENT_INFO); // API TEST

      if (fp.contains(FetchProfile.Item.CONTENT_INFO))
        TestUtil.logTrace("UNIT TEST 3: passed.\n");
      else {
        TestUtil.logTrace("UNIT TEST 3: FAILED.\n");
        errors++;
      }
      // END UNIT TEST 3:
      // BEGIN UNIT TEST 4:

      TestUtil.logTrace("UNIT TEST 4: add(Subject)");

      fp.add("Subject"); // API TEST

      if (fp.contains("Subject"))
        TestUtil.logTrace("UNIT TEST 4: passed.\n");
      else {
        TestUtil.logTrace("UNIT TEST 4: FAILED.\n");
        errors++;
      }
      // END UNIT TEST 4:
      // BEGIN UNIT TEST 5:

      TestUtil.logTrace("UNIT TEST 5: add(From)");

      fp.add("From"); // API TEST

      if (fp.contains("From"))
        TestUtil.logTrace("UNIT TEST 5: passed.\n");
      else {
        TestUtil.logTrace("UNIT TEST 5: FAILED.\n");
        errors++;
      }
      // END UNIT TEST 5:
      // BEGIN UNIT TEST 6:

      TestUtil.logTrace("UNIT TEST 6: add(X-mailer)");

      fp.add("X-mailer"); // API TEST

      if (fp.contains("X-mailer"))
        TestUtil.logTrace("UNIT TEST 6: passed.\n");
      else {
        TestUtil.logTrace("UNIT TEST 6: FAILED.\n");
        errors++;
      }
      // END UNIT TEST 6:

      folder.fetch(msgs, fp);
      folder.close(false);
      store.close();

      checkStatus();

      if (errors > 0) {
        throw new Fault("test2 Failed: No of unit test failed = " + errors);
      }

    } catch (Exception e) {
      logErr("Unexpected Exception " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("test2 Failed");
    }

  }// end of test2()

  /*
   * @testName: getItems_test
   * 
   * @assertion_ids: JavaEE:SPEC:235;
   * 
   * @test_Strategy: getItems_test
   */
  // derived from javamail suite fetchProfile getItems_test
  public void getItems_test() throws Fault {

    try {
      // Create an empty FetchProfile
      FetchProfile fp = new FetchProfile();

      // Add header names to Profile object

      fp.add(FetchProfile.Item.ENVELOPE);
      fp.add(FetchProfile.Item.FLAGS);
      fp.add(FetchProfile.Item.CONTENT_INFO);
      fp.add(FetchProfile.Item.SIZE);

      // BEGIN UNIT TEST 1:

      TestUtil.logMsg("UNIT TEST 1: getItems()");

      FetchProfile.Item[] items = fp.getItems(); // API TEST

      boolean foundEnv = false, foundFlags = false, foundCont = false,
          foundSize = false;
      for (int j = 0; j < items.length; j++) {
        if (items[j] == FetchProfile.Item.ENVELOPE)
          foundEnv = true;
        else if (items[j] == FetchProfile.Item.FLAGS)
          foundFlags = true;
        else if (items[j] == FetchProfile.Item.CONTENT_INFO)
          foundCont = true;
        else if (items[j] == FetchProfile.Item.SIZE)
          foundSize = true;
      }

      if (foundEnv && foundFlags && foundCont && foundSize)
        TestUtil.logMsg("UNIT TEST 1: passed.\n");
      else {
        TestUtil.logMsg("UNIT TEST 1: FAILED.\n");
        errors++;
      }
      // END UNIT TEST 1:

      checkStatus();

    } catch (Exception e) {
      logErr("Unexpected Exception " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("getItems_test Failed");
    }

  }

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
