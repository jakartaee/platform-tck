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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import com.sun.ts.lib.deliverable.DeliverableFactory;
import com.sun.ts.lib.deliverable.PropertyManagerInterface;
import com.sun.ts.lib.deliverable.PropertyNotSetException;
import com.sun.ts.lib.util.TestUtil;

/**
 * This is a utility class that is called by SuiteSynchronizer to replace any
 * configurable properties in the runtime xml files with the user specific
 * values from ts.jte. A copy is made of each runtime xml file (with the
 * substituted values) in the harness.temp.directory/tmp directory. Each time a
 * given test is run, this copy is only created iff the jte file or the original
 * runtime have been changed since the last writing of the copy.
 *
 * @author Kyle Grucci
 */
public class TSRuntimeConfiguration {
  // whether running with s1as or j2sdkee RI
  private static Boolean runningS1AS;

  public final static String SUN_EJB_JAR_XML = ".sun-ejb-jar.xml";

  public final static String SUN_WEB_XML = ".sun-web.xml";

  public final static String SUN_APPLICATION_XML = ".sun-application.xml";

  public final static String SUN_APPLICATION_CLIENT_XML = ".sun-application-client.xml";

  public final static String SUN_CONNECTOR_XML = ".sun-connector.xml";

  public final static String SUN_CMP_MAPPINGS_XML = ".sun-cmp-mappings.xml";

  private File tempFile;

  private PrintWriter log;

  private Hashtable htReplacementProps = new Hashtable();

  private Hashtable htReplacerTable = new Hashtable();

  private String sTempDir = "";

  private StringReplacer replacer;

  private File jteFile = new File(System.getProperty("TS_HOME") + File.separator
      + "bin" + File.separator + "ts.jte");

  /** The string to replace all mail-from tags with */
  private String mailFrom = "";

  /** The string to replace all mail-host tags with */
  private String mailHost = "";

  /** The web server host */
  private String webHost = "";

  private String webHost2 = "";

  /** The web server port */
  private String webPort = "";

  private String webPort2 = "";

  private String securedWebPort = "";

  private String securedWebPort2 = "";

  /** The wsdl repository */
  private String wsdlRepository1 = "";

  private String wsdlRepository2 = "";

  private String tsHome = "";

  private String user1 = "cts1";

  private String user2 = "cts2";

  private String user3 = "cts3";

  private String password1 = "cts1";

  private String password2 = "cts2";

  private String password3 = "cts3";

  private String rauser1 = "cts1";

  private String rauser2 = "cts2";

  private String rapassword1 = "cts1";

  private String rapassword2 = "cts2";

  /**
   * host + port of the naming service for 1st & 2nd servers in interop tests
   */
  private String namingServiceHost1 = "";

  private String namingServicePort1 = "";

  private String namingServiceHost2 = "";

  private String namingServicePort2 = "";

  private int iPort = 0;

  private PropertyManagerInterface propMgr;

  public TSRuntimeConfiguration(PrintWriter out)
      throws PropertyNotSetException {
    try {
      propMgr = DeliverableFactory.getDeliverableInstance()
          .getPropertyManager();
    } catch (Exception e) {
      e.printStackTrace();
    }
    log = out;
    replacer = new StringReplacer();
    tsHome = propMgr.getProperty("ts_home", tsHome);
    webHost = propMgr.getProperty("webServerHost", webHost);
    webPort = propMgr.getProperty("webServerPort", webPort);
    webHost2 = propMgr.getProperty("webServerHost.2", webHost2);
    webPort2 = propMgr.getProperty("webServerPort.2", webPort2);
    securedWebPort = propMgr.getProperty("securedWebServicePort",
        securedWebPort);
    securedWebPort2 = propMgr.getProperty("securedWebServicePort.2",
        securedWebPort2);
    mailFrom = propMgr.getProperty("mailFrom", mailFrom);
    mailHost = propMgr.getProperty("mailHost", mailHost);
    user1 = propMgr.getProperty("user1", user1);
    user2 = propMgr.getProperty("user2", user2);
    user3 = propMgr.getProperty("user3", user3);
    password1 = propMgr.getProperty("password1", password1);
    password2 = propMgr.getProperty("password2", password2);
    password3 = propMgr.getProperty("password3", password3);

    rauser1 = propMgr.getProperty("rauser1", rauser1);
    rauser2 = propMgr.getProperty("rauser2", rauser2);
    rapassword1 = propMgr.getProperty("rapassword1", rapassword1);
    rapassword2 = propMgr.getProperty("rapassword2", rapassword2);

    namingServiceHost1 = propMgr.getProperty("namingServiceHost1",
        namingServiceHost1);
    namingServiceHost2 = propMgr.getProperty("namingServiceHost2",
        namingServiceHost2);
    namingServicePort1 = propMgr.getProperty("namingServicePort1",
        namingServicePort1);
    namingServicePort2 = propMgr.getProperty("namingServicePort2",
        namingServicePort2);
    wsdlRepository1 = propMgr.getProperty("wsdlRepository1", wsdlRepository1);
    wsdlRepository2 = propMgr.getProperty("wsdlRepository2", wsdlRepository2);

    if (TestUtil.harnessDebug) {
      String msg = "TSRuntimeConfig: user1=" + user1 + ", rauser1=" + rauser1
          + ", password1=" + password1 + ", user2=" + user2 + ", password2="
          + password2 + ", user3=" + user3 + ", password3=" + password3
          + ", rapassword1=" + rapassword1 + ", rauser2=" + rauser2
          + ", rapassword2=" + rapassword2 + ", webPort = " + webPort
          + ", webHost = " + webHost + ", mailFrom = " + mailFrom
          + ", mailHost = " + mailHost + ", namingServiceHost1 = "
          + namingServiceHost1 + ", namingServicePort1 = " + namingServicePort1
          + ", namingServiceHost2 = " + namingServiceHost2
          + ", namingServicePort2 = " + namingServicePort2
          + ", securedWebServicePort = " + securedWebPort
          + ", securedWebServicePort.2 = " + securedWebPort2
          + ", wsdlRepository1 = " + wsdlRepository1 + ", wsdlRepository2 = "
          + wsdlRepository2;
      TestUtil.logHarnessDebug(msg);
    }

    // we need to change the jte props for host and port props to include
    // a ".1" at the end because currently any .2 props will never be found
    // due to the fact that the .1 version is a substring of the .2 version.
    htReplacementProps.put("webServerHost", webHost);
    htReplacementProps.put("webServerPort", webPort);
    htReplacementProps.put("webServerHost.1", webHost);
    htReplacementProps.put("webServerPort.1", webPort);
    htReplacementProps.put("webServerHost.2", webHost2);
    htReplacementProps.put("webServerPort.2", webPort2);
    htReplacementProps.put("securedWebServicePort", securedWebPort);
    htReplacementProps.put("securedWebServicePort.1", securedWebPort);
    htReplacementProps.put("securedWebServicePort.2", securedWebPort2);
    htReplacementProps.put("ts_home", tsHome);
    htReplacementProps.put("mailFrom", mailFrom);
    htReplacementProps.put("mailHost", mailHost);
    htReplacementProps.put("namingServiceHost1", namingServiceHost1);
    htReplacementProps.put("namingServicePort1", namingServicePort1);
    htReplacementProps.put("namingServiceHost2", namingServiceHost2);
    htReplacementProps.put("namingServicePort2", namingServicePort2);
    htReplacementProps.put("user1", user1);
    htReplacementProps.put("user2", user2);
    htReplacementProps.put("user3", user3);
    htReplacementProps.put("password1", password1);
    htReplacementProps.put("password2", password2);
    htReplacementProps.put("password3", password3);
    htReplacementProps.put("rauser1", rauser1);
    htReplacementProps.put("rauser2", rauser2);
    htReplacementProps.put("rapassword1", rapassword1);
    htReplacementProps.put("rapassword2", rapassword2);
    htReplacementProps.put("wsdlRepository1", wsdlRepository1);
    htReplacementProps.put("wsdlRepository2", wsdlRepository2);

    sTempDir = propMgr.getProperty("harness.temp.directory");
    htReplacementProps.put("harness.temp.directory", sTempDir);

  }

  public void setTable(Hashtable table) {
    this.htReplacerTable = table;
  }

  public String sweepRuntimeFile(File file)
      throws FileNotFoundException, IOException {
    File xml = new File(sTempDir + File.separator + file.getName());

    if (htReplacerTable != null && !(htReplacerTable.isEmpty())) {
      htReplacerTable = replaceOnRuntimeInfoStrings(htReplacerTable);

    }
    return replacer.replace(file, htReplacementProps, htReplacerTable,
        sTempDir);
  }

  final class StringReplacer {
    public String sFindString; // string we are searching for

    public String sReplaceString; // replace sFindString with this

    public String sDirString;

    public String sFileNameStringToReplace;

    public String sNewFileNameString;

    public int iHowMany = -1;

    public String[] sFileList;

    public boolean bNewFile = false;

    private Hashtable htFindAndReplace = null;

    private Hashtable htCustomTable = null;

    private Vector vInfoObjects = new Vector();

    private Vector vMatchingInfoObjects = new Vector();

    private Vector vNonMatchingInfoObjects = new Vector();

    private boolean bSomethingWasReplaced = false;

    public String replace(File file, Hashtable htStrings,
        Hashtable htReplacerTable, String sTempDir) {
      String[] sFileList = null;
      htFindAndReplace = htStrings;
      htCustomTable = htReplacerTable;
      String sTemp = "";
      ReplacementInfo ri = null;
      String sNewFileName = "";
      StringBuffer sFoundBuffer = new StringBuffer();
      char c;
      bSomethingWasReplaced = false;
      BufferedReader fReader = null;
      BufferedWriter fNewWriter = null;
      try {
        vInfoObjects = new Vector();
        String sKey = "";
        // create ReplacementInfo objects
        for (Enumeration e = htFindAndReplace.keys(); e.hasMoreElements();) {
          sKey = (String) e.nextElement();
          vInfoObjects.addElement(
              new ReplacementInfo(sKey, (String) htFindAndReplace.get(sKey)));
        }
        // add in the table the custom table of replacement strings
        // if any
        if (htCustomTable != null) {
          for (Enumeration e = htCustomTable.keys(); e.hasMoreElements();) {
            sKey = (String) e.nextElement();
            vInfoObjects.addElement(
                new ReplacementInfo(sKey, (String) htCustomTable.get(sKey)));
          }
        }
        // reader of the file that we're searching
        fReader = new BufferedReader(new FileReader(file));
        StringWriter sWriter = new StringWriter();
        // stores new file contents

        // The following code is here to account for the fact that we
        // have some properties to replace which are substrings of other
        // properties. We need to be sure that we will be able to
        // replace either. Thus, we check the char after certain props
        // to see if they continue to match another property. If so,
        // then we will replace with the longer property value
        boolean bCheckForDot = false;
        String sHold = "";
        int iCharRead; // holds each char that we read
        vMatchingInfoObjects.addAll(vInfoObjects);
        while ((iCharRead = fReader.read()) != -1) {
          c = (char) iCharRead;

          if (bCheckForDot) {
            if (c != '.') {
              // hold the dot and let the old processing happen
              sHold = new String((new Character(c)).toString());
              TestUtil.logHarnessDebug("sHold = " + sHold);
            } else {
              sFoundBuffer.append(c);
              bCheckForDot = false;
              continue;
              // since there is no need to just check .
            }
          } else {
            if (sHold != null) {
              // just write the char that we were holding. This
              // assumes that this char is not the beginning of a
              // string we want to replace
              sWriter.write(sHold);
              sHold = null;
            }

            sFoundBuffer.append(c);
          }

          sTemp = new String(sFoundBuffer);

          // get all ris that still match
          for (Enumeration e = vMatchingInfoObjects.elements(); e
              .hasMoreElements();) {
            ri = (ReplacementInfo) e.nextElement();
            if (ri.sFind.startsWith(sTemp)) {
              if (ri.sFind.equals(sTemp)) {

                // what if we have a substring of another string that
                // we are searching for?
                if (!bCheckForDot) {
                  if (sTemp.equals("webServerHost")
                      || sTemp.equals("webServerPort")
                      || sTemp.equals("securedWebServicePort")) {
                    // set boolean and break out to read next char
                    bCheckForDot = true;
                    break;
                  }
                } else {
                  bCheckForDot = false;
                }

                if (TestUtil.harnessDebug)
                  TestUtil.logHarnessDebug("REPLACER:MATCH found:  " + ri.sFind
                      + " matches " + sTemp);
                ri.foundOccurance();
                bSomethingWasReplaced = true;
                sWriter.write(ri.sReplace);
                // reset sFoundBuffer
                sFoundBuffer = new StringBuffer();
                // reset matchers to all again
                vMatchingInfoObjects.removeAllElements();
                vMatchingInfoObjects.addAll(vInfoObjects);
                break;
              }
            } else {
              vNonMatchingInfoObjects.addElement(ri);
              // the char that we just read causes our string not
              // to match the string we're looking for. Write the
              // old string to the new file and reset sFoundBuffer.
              // this will never happen!!!
            }
          }
          // remove all non-matching ri's
          vMatchingInfoObjects.removeAll(vNonMatchingInfoObjects);
          vNonMatchingInfoObjects.removeAllElements();
          // reset everything if there are none left matching
          if (vMatchingInfoObjects.isEmpty()) {
            sWriter.write(sTemp);
            sFoundBuffer = new StringBuffer();
            vMatchingInfoObjects.removeAllElements();
            vMatchingInfoObjects.addAll(vInfoObjects);
          }
        }
        fReader.close();

        /*
         * Always copy the file to tmp even if we don't replace anything as it
         * makes the location consistent for xml deployment handlers.
         */

        // if (bSomethingWasReplaced) {
        sNewFileName = sTempDir + File.separator + file.getName();
        if (TestUtil.harnessDebug)
          TestUtil.logHarnessDebug("New filename:" + sNewFileName);
        File fTmpRuntimeFile = new File(sNewFileName);

        if (fTmpRuntimeFile.exists()) {
          TestUtil.logHarnessDebug("Old runtime file exists:" + sNewFileName);
          fTmpRuntimeFile.delete();
          TestUtil.logHarnessDebug("Deleted old runtime file:" + sNewFileName);
        }
        // fTmpRuntimeFile
        fNewWriter = new BufferedWriter(new FileWriter(new File(sNewFileName)));
        fNewWriter.write(sWriter.toString());
        fNewWriter.flush();
        // } else {
        // sNewFileName = null;
        // }
      } catch (Exception e) {
        e.printStackTrace();
      } finally {
        if (fNewWriter != null) {
          try {
            fNewWriter.close();
          } catch (IOException exp) {
          }
        }
      }
      return sNewFileName;
    }

    class ReplacementInfo {
      private int iFoundOccurances = 0;

      private String sFind = null;

      private String sReplace = null;

      ReplacementInfo(String sFindString, String sReplaceString) {
        sFind = sFindString;
        sReplace = sReplaceString;
        iFoundOccurances = 0;
      }

      public void foundOccurance() {
        iFoundOccurances++;
        if (TestUtil.harnessDebug)
          TestUtil.logHarnessDebug("we found an occurance #" + iFoundOccurances
              + " of '" + sFind + "'");
      }
    }
  }

  /**
   * Parse the HashTable which contains strings retrieved via calls into the
   * porting classes for deployment. We must substitute on these strings prior
   * to substituting them into the runtime.xml.
   */
  private Hashtable replaceOnRuntimeInfoStrings(Hashtable extras) {
    boolean changeIt = false;
    Hashtable resultTable = new Hashtable(extras);
    String searchFor[] = { "namingServiceHost1", "namingServicePort1",
        "namingServiceHost2", "namingServicePort2", "webServerHost.1",
        "webServerPort.1", "webServerHost.2", "webServerPort.2",
        "securedWebServicePort.1", "securedWebServicePort.2", "wsdlRepository1",
        "wsdlRepository2", "harness.temp.directory", "webServerHost",
        "webServerPort, securedWebServicePort" };

    for (Enumeration e = extras.keys(); e.hasMoreElements();) {
      String sKey = (String) e.nextElement();
      String val = (String) extras.get(sKey);
      TestUtil.logHarnessDebug("extra Key = " + sKey);
      TestUtil.logHarnessDebug("extra Val = " + val);
      String oldJndi = val;
      changeIt = false;
      String buff = null;
      int startPos = 0;
      for (int i = 0; i < searchFor.length; i++) {
        if (TestUtil.harnessDebug)
          TestUtil.logHarness("\n###Searching for=" + searchFor[i]);
        if ((startPos = val.lastIndexOf(searchFor[i])) != -1) {
          changeIt = true;
          String startBuff = val.substring(0, startPos);
          buff = (String) htReplacementProps.get(searchFor[i]);
          val = startBuff + buff
              + val.substring(startPos + searchFor[i].length());
        }
      }
      if (changeIt) {
        resultTable.put((String) sKey, val);
        if (TestUtil.harnessDebug)
          TestUtil.logHarness("\n###\nold RuntimeInfo Val=" + oldJndi
              + "\nNew RuntimeInfo Val = " + val);
      }
    }
    return resultTable;
  }
}
