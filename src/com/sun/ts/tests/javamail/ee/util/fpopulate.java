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

package com.sun.ts.tests.javamail.ee.util;

import java.io.*;

import javax.mail.*;
import javax.mail.internet.*;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.javamail.ee.common.MailTestUtil;

/*
 * Copy folder hierarchies between files and a Store. This is a useful 
 * utility to populate new (and possibly empty) mail stores. Specify
 * the source as a directory name and the destination folders as a URL.
 *	
 * @author John Mani
 * @author Bill Shannon
 * @author Raja Perumal
 */

public class fpopulate {

  static boolean force = false;

  static boolean skipSCCS = false;

  static boolean clear = false;

  static Session session;

  public static void main(String argv[]) {
    String srcdir = null;
    String dstURL = null;
    boolean debug = false;
    String user = null;
    String password = null;
    String host = null;
    String portStr = null;
    String protocol = null;
    boolean deleteFolder = false;

    int optind;

    for (optind = 0; optind < argv.length; optind++) {
      if (argv[optind].equals("-s")) {
        srcdir = argv[++optind];
        System.out.println("srcdir = " + srcdir);
      } else if (argv[optind].equals("-d")) {
        dstURL = argv[++optind];
        System.out.println("destinationURL = " + dstURL);
      } else if (argv[optind].equals("-D")) {
        debug = true;
      } else if (argv[optind].equals("-f")) {
        force = true;
      } else if (argv[optind].equals("-S")) {
        skipSCCS = true;
      } else if (argv[optind].equals("-c")) {
        clear = true;
      } else if (argv[optind].equals("-user")) {
        user = argv[++optind];
      } else if (argv[optind].equals("-password")) {
        password = argv[++optind];
      } else if (argv[optind].equals("-host")) {
        host = argv[++optind];
      } else if (argv[optind].equals("-port")) {
        portStr = argv[++optind];
      } else if (argv[optind].equals("-protocol")) {
        protocol = argv[++optind];
      } else if (argv[optind].equals("-deleteFolder")) {
        deleteFolder = true;
      } else if (argv[optind].equals("--")) {
        optind++;
        break;
      } else if (argv[optind].startsWith("-")) {
        printUsage();
        System.exit(1);
      } else {
        break;
      }
    }

    try {

      if (srcdir == null || dstURL == null) {
        printUsage();
        System.exit(1);
      }

      int imapPort = Integer.parseInt(portStr);
      TestUtil.logTrace("IMAP Port = " + imapPort);

      MailTestUtil mailTestUtil = new MailTestUtil();
      Store store = mailTestUtil.connect2host(protocol, host, imapPort, user,
          password);
      session = mailTestUtil.getSession();
      session.setDebug(debug);

      // Get source folder
      File srcFolder = new File(srcdir);
      if (!srcFolder.exists()) {
        System.out.println("source folder does not exist");
        System.exit(1);
      }

      // Set up destination folder
      URLName dstURLName = new URLName(dstURL);
      Folder dstFolder;
      // Check if the destination URL has a folder specified. If
      // not, we use the source folder name
      if (dstURLName.getFile() == null) {
        Store s = session.getStore(dstURLName);
        s.connect();
        dstFolder = s.getFolder(srcFolder.getName());
      } else
        dstFolder = session.getFolder(new URLName(dstURL));

      if (clear && dstFolder.exists()) {

        if (!dstFolder.delete(true)) {
          System.out.println("couldn't delete " + dstFolder.getFullName());
          return;
        }
      }

      if (!deleteFolder) {
        copy(srcFolder, dstFolder);
      } else {
        System.out.println("Delete " + dstFolder.getFullName());
      }

      // Close the respective stores.
      dstFolder.getStore().close();

    } catch (MessagingException mex) {
      System.out.println(mex.getMessage());
      mex.printStackTrace();
    } catch (IOException ioex) {
      System.out.println(ioex.getMessage());
      ioex.printStackTrace();
    }
  }

  private static void copy(File src, Folder dst)
      throws MessagingException, IOException {
    System.out.println("Populating " + dst.getFullName());

    if (!dst.exists()) {
      // Create it.
      int type = holdsMessages(src) ? Folder.HOLDS_MESSAGES
          : Folder.HOLDS_FOLDERS;
      if (!dst.create(type)) {
        System.out.println("couldn't create " + dst.getFullName());
        return;
      }

      // Copy over any messages from src to dst
      if (holdsMessages(src))
        copyMessages(src, dst);
    } else {
      System.out.println(dst.getFullName() + " already exists");
      // Copy over any messges from src to dst
      if (force && holdsMessages(src))
        copyMessages(src, dst);
    }

    // Copy over subfolders
    if (holdsFolders(src)) {
      String[] sf = src.list();
      for (int i = 0; sf != null && i < sf.length; i++) {
        // skip SCCS directories?
        if (skipSCCS && sf[i].equals("SCCS"))
          continue;
        File f = new File(src, sf[i]);
        if (f.isDirectory())
          copy(f, dst.getFolder(sf[i]));
      }
    }
  }

  /**
   * Does this directory hold messages? Return true if there's at least one
   * message.
   */
  private static boolean holdsMessages(File f) {
    File msg = new File(f, "1");
    return msg.exists();
  }

  private static boolean holdsFolders(File f) {
    return !holdsMessages(f); // XXX - hack for now
  }

  /**
   * Copy message files from the source directory to the destination folder.
   * Message files must be named "1", "2", etc. The first missing number
   * terminates the copy.
   */
  private static void copyMessages(File src, Folder dst)
      throws MessagingException, IOException {
    System.out.println("  Copy from " + src + " to " + dst);
    int msgnum = 1;
    Message[] msgs = new Message[1];
    for (;;) {
      File f = new File(src, String.valueOf(msgnum));
      if (!f.exists()) // break when we find a message missing
        break;
      FileInputStream fis = new FileInputStream(f);
      BufferedInputStream is = new BufferedInputStream(fis);
      is.mark(1024);
      /*
       * If it's in UNIX mbox format, we skip the first line, otherwise we start
       * reading at the beginning.
       */
      if (is.read() == 'F' && is.read() == 'r' && is.read() == 'o'
          && is.read() == 'm' && is.read() == ' ') {
        int c;
        do
          c = is.read();
        while (!(c == '\n' || c == '\r' || c == -1));
        if (c == '\r') {
          is.mark(1);
          if (is.read() != '\n')
            is.reset();
        }
      } else
        is.reset();
      MimeMessage msg = new MimeMessage(session, is);
      fis.close();
      msgs[0] = msg;
      dst.appendMessages(msgs);
      msgnum++;
    }
    System.out.println("  Copied " + (msgnum - 1) + " messages");
  }

  private static void printUsage() {
    System.out.println(
        "fpopulate [-D] [-f] [-S] [-c] " + "-s source_dir -d dest_url");
    System.out.println("URLs are of the form: "
        + "protocol://username:password@hostname/foldername");
    System.out.println("The destination URL does not need a foldername,"
        + " in which case, the source foldername is used");
  }
}
