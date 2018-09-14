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

package com.sun.ts.tests.javamail.ee.common;

import java.io.*;
import java.util.*;

import javax.mail.*;
import javax.mail.internet.MimeMessage;
import com.sun.javatest.*;
import com.sun.ts.lib.util.TestUtil;

/**
 * This class declares common fields and defines utility methods for parsing
 * command-line arguments and checking pass/fail status of testcases. It also
 * has methods for memory checking and dumping stack trace to some file after an
 * exception occurs.
 */

public class MailTestUtil {

  public String testname; // testcase name

  public String protocol; // server protocol being used

  public String transport_protocol; // Transport protocol

  public String host; // host server name

  public String transport_host; // transport host server name

  public String user; // user login id

  public String password; // user password

  public boolean auth; // use smtp authentication?

  public String mailbox; // test mailbox

  public String testbox; // temporary mailbox

  public String from; // from email address

  public String to; // to email address

  public String rootpath = ""; // search path to mailbox

  public String pattern; // search pattern

  public String iofile; // input/output file

  public String newName; // new mailbox name

  public String subject; // the subject to search for

  public String portvalue; // protocol port parameter

  public String workdir; // test work directory

  public String proxy; // SOCKS5 proxy

  public int msgcount = -1; // message number

  public int portnum = -1; // protocol port number

  public int errors = 0; // number of unit test errors

  public Status status; // JavaTest harness pass/fail status

  public Properties properties; // the global properties object

  public PrintWriter out; // output stream

  public PrintWriter log; // error output stream

  public Session session; // Session object variable

  public boolean debug = false; // debug mode

  /**
   * Create System properties object.
   */
  public MailTestUtil() {
    properties = new Properties();
  }

  /**
   * Get command-line arguments and stuff the values into member fields.
   */
  public void parseArgs(String argv[]) {
    int optind;

    for (optind = 0; optind < argv.length; optind++) {
      if (argv[optind].equals("-t"))
        protocol = argv[++optind];
      else if (argv[optind].equals("-tp"))
        transport_protocol = argv[++optind];
      else if (argv[optind].equals("-th"))
        transport_host = ifnull(argv[++optind]);
      else if (argv[optind].equals("-h"))
        host = ifnull(argv[++optind]);
      else if (argv[optind].equals("-u"))
        user = ifnull(argv[++optind]);
      else if (argv[optind].equals("-p"))
        password = ifnull(argv[++optind]);
      else if (argv[optind].equals("-m"))
        mailbox = argv[++optind];
      else if (argv[optind].equals("-test"))
        testbox = argv[++optind];
      else if (argv[optind].equals("-from"))
        from = argv[++optind];
      else if (argv[optind].equals("-to"))
        to = argv[++optind];
      else if (argv[optind].equals("-r"))
        rootpath = argv[++optind];
      else if (argv[optind].equals("-io"))
        iofile = argv[++optind];
      else if (argv[optind].equals("-s"))
        pattern = argv[++optind];
      else if (argv[optind].equals("-n"))
        newName = argv[++optind];
      else if (argv[optind].equals("-subject"))
        subject = argv[++optind];
      else if (argv[optind].equals("-pn"))
        portvalue = argv[++optind];
      else if (argv[optind].equals("-WorkDir"))
        workdir = argv[++optind];
      else if (argv[optind].equals("-A"))
        auth = true;
      else if (argv[optind].equals("-proxy"))
        proxy = argv[++optind];
      else if (argv[optind].equals("-D"))
        debug = true;
      else if (argv[optind].equals("--")) {
        optind++;
        break;
      } else if (argv[optind].startsWith("-")) {
        out.println(
            "Usage: test [-D] [-t protocol] [-tp transport_protocol] [-th transport_host]");
        out.println(
            "\t[-h host] [-u user] [-p password] [-r rootpath] [-m mailbox]");
        out.println(
            "\t[-test testbox] [-from from_address] [-to to_address] [-io iofile]");
        out.println(
            "\t[-s pattern] [-n newname] [-subject subject] [-pn port_number]");
        out.println(
            "\t[-WorkDir workdirpath] [-A] [-proxy socks-proxy] [msgcount]");
        System.exit(1);
      } else
        break;
    }
    // get integer number from the command-line

    if (optind < argv.length)
      msgcount = Integer.parseInt(argv[optind]);

    if (portvalue != null)
      portnum = Integer.parseInt(portvalue);

    if (transport_host == null)
      transport_host = host;

    if (protocol != null) {
      properties.setProperty("mail.store.protocol", protocol);
      if (host != null)
        properties.setProperty("mail." + protocol + ".host", host);
      if (user != null)
        properties.setProperty("mail." + protocol + ".user", user);
      if (portnum > 0)
        properties.setProperty("mail." + protocol + ".port", "" + portnum);
      if (proxy != null)
        properties.setProperty("mail." + protocol + ".socks.host", proxy);
    }
    if (transport_protocol != null) {
      properties.setProperty("mail.transport.protocol", transport_protocol);
      if (transport_host != null)
        properties.setProperty("mail." + transport_protocol + ".host",
            transport_host);
      if (user != null)
        properties.setProperty("mail." + transport_protocol + ".user", user);
      properties.setProperty("mail." + transport_protocol + ".auth", "" + auth);
      if (proxy != null)
        properties.setProperty("mail." + transport_protocol + ".socks.host",
            proxy);
    }
  }

  /**
   * If the command line argument has the special value "NULL", return null.
   */
  private static String ifnull(String arg) {
    return arg.equals("NULL") ? null : arg;
  }

  /**
   * Create a Session.
   */
  public Session createSession() {
    final String user0 = user, password0 = password;
    Session session = Session.getInstance(properties, new Authenticator() {
      public PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(user0, password0);
      }
    });
    session.setDebug(debug);
    return session;
  }

  /**
   * Connect to host machine.
   */
  public Store connect2host(String proto, String host, String user,
      String password) {
    Store store = null;

    try {
      // Get a Session object
      session = createSession();
      session.setDebug(debug);

      if (debug) {
        out.println("Password is:" + password);
        out.println("User is:" + user);
        out.println("Host:" + host);
        out.println("Port:" + portnum);
        out.println("Protocol:" + proto);
      }

      if (session == null) {
        out.println("Warning: Failed to create a Session object!");
        return null;
      }

      // Get a Store object
      store = session.getStore(proto);

      if (store == null) {
        out.println("Warning: Failed to create a Store object!");
        return null;
      }
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

  public Store connect2host(String proto, String host, String user,
      String password, Session session) {
    Store store = null;

    try {

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

  public Store connect2host(String proto, String host, int portnum, String user,
      String password) {
    Store store = null;

    try {

      // Get a Session object
      session = createSession(host, String.valueOf(portnum), user, password);

      // Get a Store object
      store = session.getStore(proto);

      if (store == null) {
        TestUtil.logMsg("Warning: Failed to create a Store object!");
        return null;
      }

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
      if (rootpath.equals(""))
        folder = store.getDefaultFolder();
      else
        folder = store.getFolder(rootpath);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return folder;
  }

  /**
   * Check run-time memory and pass/fail status.
   */
  public Status checkMem() {
    Runtime rt = Runtime.getRuntime();

    out.println("Free Memory = " + rt.freeMemory());
    out.println("Test Aborted!");

    if (errors == 0)
      status = Status.passed("OKAY");
    else
      status = Status.failed("");

    return status;
  }

  /**
   * Creates and returns a ByteArrayInputStream for given Message object
   */
  public ByteArrayInputStream createInputStream(Message msg) {
    ByteArrayInputStream bis = null;

    try {
      // Create a ByteArrayOutStream object
      ByteArrayOutputStream bos = new ByteArrayOutputStream();

      if (bos == null) {
        out.println(
            "Warning: Failed to create a ByteArrayOutputStream object!");
        return null;
      }
      msg.writeTo(bos);

      // Create a ByteArrayInputStream object
      bis = new ByteArrayInputStream(bos.toByteArray());

      if (bis == null) {
        out.println("Warning: Failed to create a ByteArrayInputStream object!");
        return null;
      }
    } catch (Exception e) {
      handlException(e);
    }
    return bis;
  }

  /**
   * Check testcase pass/fail status.
   */
  public void checkStatus() {
    if (errors == 0)
      status = Status.passed("OKAY");
    else
      status = Status.failed("");
  }

  public Session getSession() {
    return this.session;
  }

  /**
   * Deals with exception, produces stack trace, writes test report.
   */
  public void handlException(Exception e) {
    out.println("\nException caught!");
    status = Status.failed("EXCEPTION");
    e.printStackTrace();
  }

  /**
   * Deals with exception, produces stack trace, writes test report.
   */
  public void ExceptionTest(Exception e) {
    out.println("\nException caught!");
    status = Status.passed("OKAY");
    e.printStackTrace();
  }

  public Properties getProperties() {
    return properties;
  }

  public void setProperties(Properties properties) {
    this.properties = properties;
  }

  public Session createSession(String host, String user, String password) {

    Properties properties = new Properties();
    properties.setProperty("mail.smtp.submitter", user);
    properties.setProperty("mail.smtp.auth", "true");
    properties.setProperty("mail.smtp.host", host);
    properties.setProperty("mail.smtp.port", "25");

    MailAuthenticator mailAuthenticator = new MailAuthenticator(user, password);
    Session session = Session.getInstance(properties, mailAuthenticator);
    return session;
  }

  public Session createSession(String host, String port, String user,
      String password) {

    Properties properties = new Properties();
    properties.setProperty("mail.smtp.submitter", user);
    properties.setProperty("mail.smtp.auth", "true");
    properties.setProperty("mail.smtp.host", host);
    properties.setProperty("mail.smtp.port", port);

    MailAuthenticator mailAuthenticator = new MailAuthenticator(user, password);
    Session session = Session.getInstance(properties, mailAuthenticator);
    return session;
  }

}

class MailAuthenticator extends javax.mail.Authenticator {
  private PasswordAuthentication authentication;

  public MailAuthenticator(String user, String password) {
    authentication = new PasswordAuthentication(user, password);
  }

  protected PasswordAuthentication getPasswordAuthentication() {
    return authentication;
  }
}
