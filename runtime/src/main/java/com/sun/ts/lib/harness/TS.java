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

import java.util.*;
import java.io.*;
import com.sun.javatest.finder.*;
import com.sun.javatest.*;
import com.sun.javatest.util.BackupPolicy;
import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.deliverable.*;
import com.sun.interview.Interview;
import com.sun.ts.lib.javatest.*;

public class TS extends TestSuite {
  private TestEnvironment env;

  private boolean observerAdded;

  private PropertyManagerInterface propMgr;

  public TS(File root, Map tsInfo, ClassLoader cl) throws Fault {
    super(root, tsInfo, cl);
  }

  public File getInitialExcludeList() {
    File jtxFile = super.getInitialExcludeList();
    if (jtxFile != null && jtxFile.exists() && jtxFile.isFile()) {
      return jtxFile;
    }
    String tsHome = System.getProperty("TS_HOME");
    jtxFile = new File(tsHome, "bin/ts.jtx");
    return jtxFile;
  }

  public InterviewParameters createInterview() {
    InterviewParameters ctsInterv = null;
    try {
      ctsInterv = new TSLegacyParameters(this);
    } catch (Interview.HelpNotFoundFault f) {
      TestUtil.logHarness("TS.createInterview() couldn't find help files.");
    } catch (Interview.BadHelpFault bf) {
      TestUtil.logHarness("TS.createInterview() found invalid help files.");
    } catch (Exception ep) {
      TestUtil.logHarness("TS.createInterview()" + ep.toString());
      ep.printStackTrace();
    }
    return ctsInterv;
  }

  public Script createScript(TestDescription td, String[] exclTestCases,
      TestEnvironment scriptEnv, WorkDirectory workDir,
      BackupPolicy backupPolicy) throws Fault {
    Script s = new TSScript();
    s.initArgs(TestUtil.EMPTY_STRING_ARRAY);
    s.initTestDescription(td);
    s.initExcludedTestCases(exclTestCases);
    s.initTestEnvironment(scriptEnv);
    s.initWorkDir(workDir);
    s.initBackupPolicy(backupPolicy);
    return s;
  }

  public TestFinder createTestFinder() throws Fault {
    File fTSRoot = getRoot();
    if (fTSRoot == null) {
      TestUtil.logHarness("getRoot() returns null!");
      fTSRoot = new File(System.getProperty("TS_HOME"), "src");
    }
    File jtd = new File(fTSRoot, "testsuite.jtd");
    TestFinder finder = null;
    String[] args = null;
    if (jtd.exists() && !Boolean.getBoolean("no.binary.finder")) {
      finder = new BinaryTestFinder();
      args = new String[] { "-binary", jtd.getPath() };
      TestUtil.logHarness("Use BinaryTestFinder...");
    } else {
      String finderFilePath = getFinderFilePath();
      if (finderFilePath == null) {
        finder = new TSTestFinder();
        args = TestUtil.EMPTY_STRING_ARRAY;
        TestUtil.logHarness("Use TSTestFinder...");
      } else {
        finder = new ChameleonTestFinder();
        args = new String[] { "-f", finderFilePath };
        TestUtil.logHarness("Use ChameleonTestFinder...");
      }
    }
    try {
      finder.init(args, fTSRoot, null);
    } catch (TestFinder.Fault exp) {
      exp.printStackTrace();
      throw new Fault(null, "error initializing test finder.");
    }
    return finder;
  }

  public void starting(Harness harness) throws Fault {
    if (observerAdded) {
      return;
    }
    env = harness.getParameters().getEnv();
    try {
      propMgr = DeliverableFactory.getDeliverableInstance()
          .createPropertyManager(env);
    } catch (Exception ex) {
      ex.printStackTrace();
      TestUtil
          .logHarness("Failed to create PropertyManager in TS constructor.");
    }
    try {
      // create the tmp dir at the very beginning
      String sTestDir = propMgr.getProperty("harness.temp.directory");

      if (TestUtil.harnessDebug) {
        TestUtil.logHarnessDebug("Harness temp dir = " + sTestDir);
      }
      createDir(sTestDir);
      cleanDir(new File(sTestDir), new File(sTestDir));

    } catch (Exception e) {
      e.printStackTrace();
      TestUtil.logHarness("Failed to create PropertyManager.");
    }

    harness.addObserver(new TSHarnessObserver());
    observerAdded = true;
  }

  private String getFinderFilePath() {
    File propsDir = new File(
        System.getProperty("TS_HOME", System.getProperty("ts.home"))
            + File.separator + "src" + File.separator + "com" + File.separator
            + "sun" + File.separator + "ts" + File.separator + "lib",
        "harness");
    File finderProperties = new File(propsDir, "finder.properties");
    System.err.println("finderProperties = " + finderProperties.toString());
    if (!finderProperties.exists()) {
      finderProperties = new File(propsDir, "map.jtc");
    }
    if (finderProperties.exists()) {
      return finderProperties.getPath();
    }
    return null;
  }

  private void createDir(String sDir) throws Exception {
    File fDir = new File(sDir);

    if (!fDir.exists()) {
      if (!fDir.mkdirs()) {
        throw new Exception("Failed to create directory: " + sDir);
      }
      TestUtil.logHarnessDebug("Successfully created directory: " + sDir);
    }
  }

  private void cleanDir(File initialDir, File dir) {
    File[] files = dir.listFiles();

    if (files == null) {
      return;
    }

    for (int i = 0; i < files.length; i++) {
      if (files[i].isDirectory()) {
        // commenting out - don't clean subdirectories that might be created by
        // other deliverables
        // cleanDir(initialDir, files[i]);
      } else {
        files[i].delete();
      }
    }

    // don't clean directories since CTS deliverable may create dirs under this
    // if (!dir.equals(initialDir)) {
    // dir.delete();
    // }
  }

}
