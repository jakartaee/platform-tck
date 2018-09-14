/*
 * Copyright (c) 2015, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.signaturetest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.sun.ts.lib.util.TestUtil;

/**
 * Unlike usual SigTestDriver, this driver is designed to test all the packages
 * at once. The reason is to save time with extracting the jimage while
 * validating the signatures. Since jimage possibly contains all the java
 * classes in the jdk, the extracting is time consuming and space consuming as
 * well, the classes are extracted to the temporary folder.
 * <p>
 * Standard SigTestDriver would then extract the jimage twice for each package
 * (static and dynamic call), and for 8 JAXWS packages it would extract the jdk
 * classes 16 times, which could take about 80 minutes on testing computer, and
 * reasonable temporary space on hard drive.
 * <p>
 * It's likely that the signature test recording would use rather some
 * JimageFileSystemProvider rather then jimage tool to have direct access to
 * imaged classes, rather then jimage extract mechanism, similarly to jdk 8
 * approach and jarred classes.
 */
public class Jdk9SigTestDriver extends SigTestDriver {

  private static final String STATIC_FLAG = "-Static";

  private static final String CHECKVALUE_FLAG = "-CheckValue"; // only valid
  // w/
  // -static

  private static final String SMODE_FLAG = "-mode"; // requires arg of bin or
  // src

  private static final String DEBUG_FLAG = "-Debug";

  private static final String VERBOSE_FLAG = "-Verbose";

  private static final String CLASSPATH_FLAG = "-Classpath";

  private static final String FILENAME_FLAG = "-FileName";

  private static final String PACKAGE_FLAG = "-Package";

  private static final String API_VERSION_FLAG = "-ApiVersion";

  private static final String XJIMAGE = "-xjimage";

  // This method is not @Override, different 4th argument
  protected String[] createTestArguments(String packageListFile, String mapFile,
      String signatureRepositoryDir, String[] packageOrClassUnderTest,
      String classpath, boolean bStaticMode) throws Exception {

    SignatureFileInfo info = getSigFileInfo(packageOrClassUnderTest[0], mapFile,
        signatureRepositoryDir);
    List<String> command = new ArrayList<>();

    if (bStaticMode) {
      // static mode allows finer level of constants checking
      // -CheckValue says to check the actual const values
      TestUtil.logTrace("Setting static mode flag to allow constant checking.");
      command.add(STATIC_FLAG);
      command.add(CHECKVALUE_FLAG);

      // specifying "-mode src" allows stricter 2 way verification of
      // constant vals
      // (note that using "-mode bin" mode is less strict)
      command.add(SMODE_FLAG);
      // command.add("bin");
      command.add("src");
    } else {
      TestUtil
          .logTrace("Not Setting static mode flag to allow constant checking.");
    }

    if (TestUtil.harnessDebug) {
      command.add(DEBUG_FLAG);
    }
    command.add(VERBOSE_FLAG);

    command.add(FILENAME_FLAG);
    command.add(info.getFile());

    command.add(CLASSPATH_FLAG);
    command.add(classpath);

    command.add(PACKAGE_FLAG);
    command.add(packageOrClassUnderTest[0]);

    // No exclude here!

    command.add(API_VERSION_FLAG);
    command.add(info.getVersion());

    String jimage = System.getProperty("java.home") + "/bin/jimage";
    command.add(XJIMAGE);
    command.add(jimage);

    return ((String[]) command.toArray(new String[command.size()]));

  } // END createTestArguments

  public SigTestResult executeSigTest(String packageListFile, String mapFile,
      String signatureRepositoryDir, String[] packagesUnderTest,
      String[] classesUnderTest, String classpath,
      ArrayList<String> unaccountedTechPkgs, String optionalPkgToIgnore)
      throws Exception {

    SigTestResult result = new SigTestResult();

    // TestUtil.logMsg("optionalPkgToIgnore = " + optionalPkgToIgnore);
    // String[] arrayOptionalPkgsToIgnore = null;
    // if (optionalPkgToIgnore != null) {
    // arrayOptionalPkgsToIgnore = optionalPkgToIgnore.split(",");
    // }

    if (packagesUnderTest != null && packagesUnderTest.length > 0) {
      TestUtil.logMsg("********** BEGIN PACKAGE LEVEL SIGNATURE "
          + "VALIDATION **********\n\n");
      // for (int i = 0; i < packagesUnderTest.length; i++) {
      String packageName = packagesUnderTest[0];// packagesUnderTest[i];

      TestUtil.logMsg("********** BEGIN VALIDATE PACKAGE '" + packageName
          + "' **********\n");

      TestUtil.logMsg(
          "********** VALIDATE IN STATIC MODE - TO CHECK CONSANT VALUES ****");
      TestUtil
          .logMsg("Static mode supports checks of static constants values ");
      TestUtil.logMsg("Extracting jimage, it can take several minutes....");

      String[] args = createTestArguments(packageListFile, mapFile,
          signatureRepositoryDir, packagesUnderTest, classpath, true);
      dumpTestArguments(args);

      if (runSignatureTest(Arrays.toString(packagesUnderTest), args)) {
        TestUtil.logMsg("********** Package '" + packageName
            + "' - PASSED (STATIC MODE) **********");
        for (String p : packagesUnderTest)
          result.addPassedPkg(p + "(static mode)");
      } else {
        for (String p : packagesUnderTest)
          result.addFailedPkg(p + "(static mode)");
        TestUtil.logMsg("********** Package '" + packageName
            + "' - FAILED (STATIC MODE) **********");
      }

      TestUtil.logMsg("\n\n");
      TestUtil.logMsg("********** VALIDATE IN REFLECTIVE MODE  ****");
      TestUtil.logMsg(
          "Reflective mode supports verification within containers (ie ejb, servlet, etc)");
      TestUtil.logMsg("Extracting jimage, it can take several minutes....");

      String[] args2 = createTestArguments(packageListFile, mapFile,
          signatureRepositoryDir, packagesUnderTest, classpath, false);
      dumpTestArguments(args2);

      if (runSignatureTest(Arrays.toString(packagesUnderTest), args2)) {
        TestUtil.logMsg("********** Package '" + packageName
            + "' - PASSED (REFLECTION MODE) **********");
        for (String p : packagesUnderTest)
          result.addPassedPkg(p + "(reflection mode)");
      } else {
        for (String p : packagesUnderTest)
          result.addFailedPkg(p + "(reflection mode)");
        TestUtil.logMsg("********** Package '" + packageName
            + "' - FAILED (REFLECTION MODE) **********");
      }

      TestUtil.logMsg(
          "********** END VALIDATE PACKAGE '" + packageName + "' **********\n");

      TestUtil.logMsg("\n");
      TestUtil.logMsg("\n");

      // }
    }

    if (classesUnderTest != null && classesUnderTest.length > 0) {
      throw new IllegalStateException("Class validation not implemented");
    }

    if (unaccountedTechPkgs != null) {
      throw new IllegalStateException(
          "Unaccounted Tech Packages not implemented");
    }

    return result;

  } // END executeSigTest

  /**
   * Prints the specified list of parameters to the message log. Used for
   * debugging purposes only.
   *
   * @param params
   *          The list of parameters to dump.
   */
  protected static void dumpTestArguments(String[] params) {

    if (params != null && params.length > 0) {
      TestUtil
          .logTrace("----------------- BEGIN SIG PARAM DUMP -----------------");
      for (int i = 0; i < params.length; i++) {
        TestUtil.logTrace("   Param[" + i + "]: " + params[i]);
      }
      TestUtil
          .logTrace("------------------ END SIG PARAM DUMP ------------------");
    }

  } // END dumpTestArguments

  /*
   * This returns true is the passed in packageName matches one of the packages
   * that are listed in the arrayOptionalPkgsToIgnore. arrayOptionalPkgsToIgnore
   * is ultimately defined in the ts.jte property
   * 'optional.tech.packages.to.ignore' If one of the entries in
   * arrayOptionalPkgsToIgnore matches the packageName then that means we return
   * TRUE to indicate we should ignore and NOT TEST that particular package.
   */
  protected static boolean isIgnorePackageUnderTest(String packageName,
      String[] arrayOptionalPkgsToIgnore) {

    // if anything is null - consider no match
    if ((packageName == null) || (arrayOptionalPkgsToIgnore == null)) {
      return false;
    }

    for (int ii = 0; ii < arrayOptionalPkgsToIgnore.length; ii++) {
      if (packageName.equals(arrayOptionalPkgsToIgnore[ii])) {
        // we found a match -
        return true;
      }
    }

    return false;
  }
}
