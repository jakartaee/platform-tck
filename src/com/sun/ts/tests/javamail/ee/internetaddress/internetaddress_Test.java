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
 * @(#)internetaddress_Test.java	1.16 03/05/16
 */
package com.sun.ts.tests.javamail.ee.internetaddress;

import java.util.*;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.harness.*;
import com.sun.ts.tests.javamail.ee.common.MailTestUtil;
import com.sun.javatest.Status;

import java.io.Serializable;

import javax.mail.*;
import javax.mail.internet.*;

public class internetaddress_Test extends ServiceEETest
    implements Serializable {

  // get this from ts.jte
  private String mailTo = null;

  private int errors;

  private transient Status status;

  private transient Session session;

  private String[] addrlist = { "ksnijjar@eng", "ksnijjar@eng.sun.com",
      "French@physicists", "cannot@waste", "us/@mhs-mci.ebay", "it@is",
      "tower@ihug.co.nz", "root@mxrelay.lanminds.com", "javaworld",
      "xx.zzz12@fea.net", "javamail-api-eng@icdev",
      "ksnijjar@java-test.Eng.Sun.COM" };

  private transient TestCase[] testCases = { new TestCase(';', "a=b c", "b c"),
      new TestCase(';', "a=b c; d=e f", "b c"),
      new TestCase(';', "a=\"b c\"; d=e f", "b c") };

  private transient TestCase[] testCasesEsc = {
      new TestCase(';', "a=b \\c", "b \\c"),
      new TestCase(';', "a=b c; d=e f", "b c"),
      new TestCase(';', "a=\"b \\c\"; d=e f", "b \\c") };

  static boolean return_comments = false; // return comments as tokens

  static boolean mime = false; // use MIME specials

  public String value = "ggere, /tmp/mail.out, +mailbox, ~user/mailbox, ~/mailbox, /PN=x400.address/PRMD=ibmmail/ADMD=ibmx400/C=us/@mhs-mci.ebay, C'est bien moche <paris@france>, Mad Genius <george@boole>, two@weeks (It Will Take), /tmp/mail.out, laborious (But Bug Free), cannot@waste (My, Intellectual, Cycles), users:get,what,they,deserve;, it (takes, no (time, at) all), if@you (could, see (almost, as, (badly, you) would) agree), famous <French@physicists>, it@is (brilliant (genius, and) superb), confused (about, being, french)";

  // Harness requirements

  /* Run test in standalone mode */
  public static void main(String[] args) {
    internetaddress_Test theTests = new internetaddress_Test();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /* Test setup: */
  /*
   * @class.setup_props: javamail.protocol; javamail.server; javamail.username;
   * javamail.password ;mailuser1; smtp.port; imap.port;
   */
  public void setup(String[] args, Properties props) throws Fault {
    try {
      // mail recipient
      mailTo = props.getProperty("mailuser1");
      if (mailTo.length() == 0)
        throw new Fault("Invalid mailuser1 - the mail to property");

      String protocol = TestUtil.getProperty("javamail.protocol");
      String host = TestUtil.getProperty("javamail.server");
      String user = TestUtil.getProperty("javamail.username");
      String password = TestUtil.getProperty("javamail.password");

      String smtpPortStr = TestUtil.getProperty("smtp.port");
      int smtpPort = Integer.parseInt(smtpPortStr);
      TestUtil.logTrace("SMTP Port = " + smtpPort);

      String imapPortStr = TestUtil.getProperty("imap.port");
      int imapPort = Integer.parseInt(imapPortStr);
      TestUtil.logTrace("IMAP Port = " + imapPort);

      MailTestUtil mailTestUtil = new MailTestUtil();
      session = mailTestUtil.createSession(host, smtpPortStr, user, password);

    } catch (Exception e) {
      logErr("Exception : " + e.getMessage());
      logErr("Setup Failed!");
      TestUtil.printStackTrace(e);
    }
  }

  /*
   * @testName: testUtilMethods1
   * 
   * @assertion_ids: JavaEE:SPEC:235; JavaEE:SPEC:234; JavaEE:SPEC:233;
   * 
   * @test_Strategy: verify InternetAddress util method toString
   */
  // derived from javamail suite utilmethods_Test class
  public void testUtilMethods1() throws Fault {
    String msgText;
    try {

      // BEGIN UNIT TEST:
      InternetAddress addr = new InternetAddress(mailTo);

      TestUtil.logTrace("\nUNIT TEST 1:  toString()");
      TestUtil.logTrace(addr.toString());

      if (addr.toString() != null) // API TEST
        TestUtil.logTrace("UNIT TEST 1:  passed\n");
      else {
        TestUtil.logTrace("UNIT TEST 1:  failed\n");
        throw new Fault("toString returned null");
      }
      // END UNIT TEST:
    } catch (Exception e) {
      logErr("Unexpected Exception " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("Call to testUtilMethods1() Failed!", e);
    }

  } // end of testUtilMethods1()
    //

  /*
   * @testName: testUtilMethods2
   * 
   * @assertion_ids: JavaEE:SPEC:235; JavaEE:SPEC:234; JavaEE:SPEC:233;
   * 
   * @test_Strategy: verify InternetAddress util method equals
   */
  // derived from javamail suite utilmethods_Test class
  public void testUtilMethods2() throws Fault {
    String msgText;
    try {

      // BEGIN UNIT TEST:
      InternetAddress addr = new InternetAddress(mailTo);

      TestUtil.logTrace("UNIT TEST 1:  equals()\n");

      if (addr.toString().equals(addr.toString())) // API TEST
        TestUtil.logTrace("UNIT TEST 1:  passed\n");
      else {
        TestUtil.logTrace("UNIT TEST 1:  FAILED\n");
        throw new Fault("equals util method failed");
      }
      // END UNIT TEST:
    } catch (Exception e) {
      logErr("Unexpected Exception " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("Call to testUtilMethods2() Failed!", e);
    }

  } // end of testUtilMethods2()
    //

  /*
   * @testName: testgetAddress
   * 
   * @assertion_ids: JavaEE:SPEC:235;
   * 
   * @test_Strategy: verify InternetAddress method getAddress
   */
  // derived from javamail suite getAddress_Test class
  public void testgetAddress() throws Fault {
    try {
      // BEGIN UNIT TEST

      for (int i = 0; i < addrlist.length; i++) {
        // Create the Address object
        InternetAddress addr = new InternetAddress(addrlist[i]);

        if (addr == null) {
          throw new Fault("Invalid/Null InternetAddress object!");
        }
        TestUtil.logTrace("\nUNIT TEST :" + (i + 1) + " getAddress()");

        String strname = addr.getAddress(); // API TEST

        if (strname != null) {
          if (strname.equals(addrlist[i])) {
            TestUtil.logTrace("Address = " + strname);
            TestUtil.logTrace("UNIT TEST " + (i + 1) + ": passed\n");
          } else {
            throw new Fault("UNIT TEST " + (i + 1) + ": FAILED\n");
          }
        } else
          continue;
      }
      // END UNIT TEST:

    } catch (Exception e) {
      logErr("Unexpected Exception " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("Call to testgetAddress() Failed!", e);
    }
  } // end of testgetAddress()

  /*
   * @testName: testgetPersonal
   * 
   * @assertion_ids: JavaEE:SPEC:235;
   * 
   * @test_Strategy: verify InternetAddress method getPersonal
   */
  // derived from javamail suite getPersonal_Test.java class
  public void testgetPersonal() throws Fault {
    try {
      // BEGIN UNIT TEST

      TestUtil.logTrace("\nUNIT TEST 1:  getPersonal()");
      InternetAddress addr = new InternetAddress();

      addr.setPersonal("the quick fox jumped over the lazy cow.");
      String personal1 = addr.getPersonal(); // API TEST

      addr.setPersonal("hotmail ~!@,#$%^&*.+=,<.>?", "US-ASCII");
      String personal2 = addr.getPersonal(); // API TEST

      if ((personal1 != null && personal2 != null)) {
        TestUtil.logTrace(personal1);
        TestUtil.logTrace(personal2);
        TestUtil.logTrace("UNIT TEST 1: passed\n");
      } else {
        TestUtil.logTrace("UNIT TEST 1: FAILED\n");
        throw new Fault("Failed!");
      }

      // END UNIT TEST:

    } catch (Exception e) {
      logErr("Unexpected Exception " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("Call to testgetPersonal() Failed!", e);
    }
  } // end of testgetPersonal()

  /*
   * @testName: contentType_Test
   * 
   * @assertion_ids: JavaEE:SPEC:235;
   * 
   * @test_Strategy:
   * 
   * derived from javamail suite setContent_Test
   */
  public void contentType_Test() throws Fault {

    TestUtil.logMsg(
        "\nTesting class ContentType: ContentType(void|String|ParameterList)\n");

    try {
      // BEGIN UNIT TEST 1:
      TestUtil.logMsg("UNIT TEST 1:  ContentType()");
      ContentType ct1 = new ContentType(); // API TEST

      if (ct1.toString() != null)
        TestUtil.logMsg("UNIT TEST 1: passed");
      else {
        TestUtil.logMsg("UNIT TEST 1: FAILED");
        errors++;
      }
      // END UNIT TEST 1:
      // BEGIN UNIT TEST 2:
      String pattern = "text/plain;charset=us-ascii;i18set=ISO-9000-2";
      TestUtil.logMsg("UNIT TEST 2:  ContentType(" + pattern + ")");
      ContentType ct2 = new ContentType(pattern); // API TEST

      if (ct2.toString() != null)
        TestUtil.logMsg("UNIT TEST 2: passed");
      else {
        TestUtil.logMsg("UNIT TEST 2: FAILED");
        errors++;
      }
      // END UNIT TEST 2:
      // BEGIN UNIT TEST 3:
      String primaryType = "text";
      String subType = "plain";

      TestUtil.logMsg(
          "UNIT TEST 3:  ContentType(" + primaryType + "," + subType + ")");
      ContentType ct3 = new ContentType(primaryType, subType, null); // API
      // TEST

      if (ct3.toString().equals(primaryType + "/" + subType))
        TestUtil.logMsg("UNIT TEST 3: passed");
      else {
        TestUtil.logMsg("UNIT TEST 3: FAILED");
        errors++;
      }
      // END UNIT TEST 3:
      // BEGIN UNIT TEST 4:
      // Create a ParameterList object
      ParameterList pl = new ParameterList(";charset=uscii");

      TestUtil.logMsg("UNIT TEST 4:  ContentType(" + primaryType + "," + subType
          + "," + pl + ")");
      ContentType ct4 = new ContentType(primaryType, subType, pl); // API
      // TEST

      if (ct4.toString() != null)
        TestUtil.logMsg("UNIT TEST 4: passed");
      else {
        TestUtil.logMsg("UNIT TEST 4: FAILED");
        errors++;
      }
      // END UNIT TEST 4:

      checkStatus();

      if (errors > 0) {
        throw new Fault(
            "contentType_Test Failed: No of unit test failed = " + errors);
      }
    } catch (Exception e) {
      logErr("Unexpected Exception " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("contentType_Test() Failed!", e);
    }

  }

  /*
   * @testName: next_Test
   * 
   * @assertion_ids: JavaEE:SPEC:235;
   * 
   * @test_Strategy: This tests tests the <strong>next()</strong> APIs. It does
   * by passing various valid input values and then checking the type of the
   * returned object. <p>
   * 
   * Return the next token from the parse stream.<p> api2test: public
   * HeaderTokenizer.Token next() <p>
   * 
   * 
   * derived from javamail suite next_Test
   */
  public void next_Test() throws Fault {

    TestUtil.logMsg("\nTesting class HeaderTokenizer: next()\n");

    try {

      // Create HeaderTokenizer object
      HeaderTokenizer ht = new HeaderTokenizer(value,
          mime ? HeaderTokenizer.MIME : HeaderTokenizer.RFC822,
          !return_comments);

      HeaderTokenizer.Token tok;

      // BEGIN UNIT TEST 1:
      TestUtil.logMsg("UNIT TEST 1: next()");

      while ((tok = ht.next()).getType() != HeaderTokenizer.Token.EOF) { // API
        // TEST
        if (tok.getType() == 0 || tok.getValue() == null) {
          TestUtil.logMsg("\t" + type(tok.getType()) + "\t" + tok.getValue());
          TestUtil.logMsg("UNIT TEST 1: Failed.\n");
          errors++;
          break;
        }
      }
      if (errors == 0)
        TestUtil.logMsg("UNIT TEST 1: passed.\n");
      // END UNIT TEST 1:

      // BEGIN UNIT TEST 2:
      TestUtil.logMsg("UNIT TEST 2: next(endOfAtom)");

      int start = errors;
      for (TestCase tc : testCases) {
        ht = new HeaderTokenizer(tc.test, HeaderTokenizer.MIME, true);
        tok = ht.next();
        if (tok.getType() != HeaderTokenizer.Token.ATOM
            || !tok.getValue().equals("a")) {
          TestUtil.logMsg("\t" + type(tok.getType()) + "\t" + tok.getValue());
          TestUtil.logMsg("UNIT TEST 2: Failed.\n");
          errors++;
          break;
        } else {
          tok = ht.next();
          if (tok.getType() != '=') {
            TestUtil.logMsg("\t" + type(tok.getType()) + "\t" + tok.getValue());
            TestUtil.logMsg("UNIT TEST 2: Failed.\n");
            errors++;
            break;
          } else {
            tok = ht.next(tc.endOfAtom);
            if (tok.getType() != HeaderTokenizer.Token.QUOTEDSTRING
                || !tok.getValue().equals(tc.expected)) {
              TestUtil
                  .logMsg("\t" + type(tok.getType()) + "\t" + tok.getValue());
              TestUtil.logMsg("UNIT TEST 2: Failed.\n");
              errors++;
              break;
            }
          }
        }
      }
      if (errors == start)
        TestUtil.logMsg("UNIT TEST 2: passed.\n");
      // END UNIT TEST 2:

      // BEGIN UNIT TEST 3:
      TestUtil.logMsg("UNIT TEST 3: next(endOfAtom, true)");

      start = errors;
      for (TestCase tc : testCasesEsc) {
        ht = new HeaderTokenizer(tc.test, HeaderTokenizer.MIME, true);
        tok = ht.next();
        if (tok.getType() != HeaderTokenizer.Token.ATOM
            || !tok.getValue().equals("a")) {
          TestUtil.logMsg("\t" + type(tok.getType()) + "\t" + tok.getValue());
          TestUtil.logMsg("UNIT TEST 3: Failed.\n");
          errors++;
          break;
        } else {
          tok = ht.next();
          if (tok.getType() != '=') {
            TestUtil.logMsg("\t" + type(tok.getType()) + "\t" + tok.getValue());
            TestUtil.logMsg("UNIT TEST 3: Failed.\n");
            errors++;
            break;
          } else {
            tok = ht.next(tc.endOfAtom, true);
            if (tok.getType() != HeaderTokenizer.Token.QUOTEDSTRING
                || !tok.getValue().equals(tc.expected)) {
              TestUtil
                  .logMsg("\t" + type(tok.getType()) + "\t" + tok.getValue());
              TestUtil.logMsg("UNIT TEST 3: Failed.\n");
              errors++;
              break;
            }
          }
        }
      }
      if (errors == start)
        TestUtil.logMsg("UNIT TEST 3: passed.\n");
      // END UNIT TEST 3:

    } catch (Exception e) {
      logErr("Unexpected Exception " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("next_Test() Failed!", e);
    }

    checkStatus();

    if (errors > 0) {
      throw new Fault(
          "contentType_Test Failed: No of unit test failed = " + errors);
    }

  }

  /*
   * @testName: combineSegments_Test
   * 
   * @assertion_ids: JavaEE:SPEC:235;
   * 
   * @test_Strategy: * This class tests the
   * <strong>ParameterList.combineSegments()strong> API. <p>
   * 
   * 
   * how2test: Create a ParameterList with a parameter split into several
   * segments and then call combineSegments() and verify that the parameter is
   * returned as a single correct value. If is so then this testcase passes,
   * otherwise it fails. <p>
   * 
   * derived from javamail suite combineSegments_Test
   */
  public void combineSegments_Test() throws Fault {

    String pattern = ";i18set=ISO-8859-1;charset=us-acii;abc=xyz";

    TestUtil.logMsg("\nTesting method ParameterList.combineSegments()\n");

    try {
      // BEGIN UNIT TEST 1:
      TestUtil.logMsg("UNIT TEST 1:  ParameterList.combineSegments()");
      System.setProperty("mail.mime.decodeparameters", "true");

      ParameterList parmlist1 = new ParameterList(); // API TEST

      parmlist1.set("p*0", "abc");
      parmlist1.set("p*1", "def");
      parmlist1.combineSegments();
      if (parmlist1.get("p").equals("abcdef") && parmlist1.get("p*0") == null
          && parmlist1.get("p*1") == null)
        TestUtil.logMsg("UNIT TEST 1: passed");
      else {
        TestUtil.logMsg("ParameterList: " + parmlist1);
        TestUtil.logMsg("UNIT TEST 1: FAILED");
        errors++;
      }
      // END UNIT TEST 1:

      checkStatus();

    } catch (Exception e) {
      logErr("Unexpected Exception " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("combineSegments_Test() Failed!", e);
    }

    if (errors > 0) {
      throw new Fault(
          "contentType_Test Failed: No of unit test failed = " + errors);
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

  private static String type(int t) {
    if (t == HeaderTokenizer.Token.ATOM)
      return "ATOM";
    else if (t == HeaderTokenizer.Token.QUOTEDSTRING)
      return "QUOTEDSTRING";
    else if (t == HeaderTokenizer.Token.COMMENT)
      return "COMMENT";
    else if (t == HeaderTokenizer.Token.EOF)
      return "EOF";
    else if (t < 0)
      return "UNKNOWN";
    else
      return "SPECIAL";
  }

  private static int type(String s) {
    if (s.equals("ATOM"))
      return HeaderTokenizer.Token.ATOM;
    else if (s.equals("QUOTEDSTRING"))
      return HeaderTokenizer.Token.QUOTEDSTRING;
    else if (s.equals("COMMENT"))
      return HeaderTokenizer.Token.COMMENT;
    else if (s.equals("EOF"))
      return HeaderTokenizer.Token.EOF;
    else
      // if (s.equals("SPECIAL"))
      return 0;
  }

  static class TestCase {
    public TestCase(char endOfAtom, String test, String expected) {
      this.endOfAtom = endOfAtom;
      this.test = test;
      this.expected = expected;
    }

    public char endOfAtom;

    public String test;

    public String expected;
  };

}
