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

import com.sun.javatest.TestDescription;
import com.sun.javatest.TestFinderQueue;
import com.sun.javatest.finder.CommentStream;
import com.sun.javatest.finder.TagTestFinder;
import com.sun.ts.lib.util.AssertionMapper;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.lib.deliverable.PropertyManagerInterface;
import com.sun.ts.lib.deliverable.DeliverableFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import java.util.Enumeration;

/**
 * This is a specific implementation of the TagTestFinder which is to be used
 * for J2EE-TS testing.
 *
 * A test description consists of a single block comment in Java files. A file
 * may contain multiple test descriptions.
 *
 * @author Vella Raman
 * @see com.sun.javatest.TestFinder
 * @see com.sun.javatest.finder.TagTestFinder
 */
public class TSTestFinder extends TagTestFinder {

  public static final String TS_HOME;

  protected static final File[] NON_TEST_DIRS;

  protected static final String PARSE_TAG_BAD = "Invalid tag: ";

  private static final String INTEROP_FORWARD = "forward";

  private static final String INTEROP_REVERSE = "reverse";

  private static final String INTEROP_BOTH = "both";

  private static final String SRC_TOKEN = File.separator + "src"
      + File.separator;

  private static final String[] defaultVehicles = { "ejb", "servlet", "jsp",
      "appclient" };

  private static final String[] EXCLUDE_NAMES = { "SCCS", "Codemgr_wsdata",
      "DeletedFiles", "DELETED-FILES", "deleted_files", "TemporarilyRemoved",
      "ts_dep" };

  private static final String KEY_CLASS_SETUP_PROPS = "class.setup_props:";

  private static final String KEY_CLASS_TEST_ARGS = "class.testArgs:";

  private static final String KEY_TEST_NAME = "testName:";

  private static final String KEY_TEST_ARGS = "testArgs:";

  private static final String KEY_CUSTOM = "custom:";

  private static final String KEY_TEST_CASE_SETUP_PROPS = "tcase.setup_props:";

  private static final String KEY_ASSERTION_IDS = "assertion_ids:";

  private static final String KEY_KEYWORDS = "keywords:";

  private static final String[] VALID_TAG_NAMES = { KEY_CLASS_SETUP_PROPS,
      KEY_CLASS_TEST_ARGS, KEY_TEST_NAME, KEY_TEST_ARGS, KEY_CUSTOM,
      KEY_TEST_CASE_SETUP_PROPS, KEY_ASSERTION_IDS, KEY_KEYWORDS };

  private static final boolean PROCESS_ASSERTION;

  private static final boolean REPORT_MISSING;

  private static final boolean ASSERTION_COVERAGE;

  static {
    TS_HOME = System.getProperty("TS_HOME");
    PROCESS_ASSERTION = Boolean.getBoolean("process.assertion");
    REPORT_MISSING = Boolean.getBoolean("report.missing.assertion.ids.tag");
    ASSERTION_COVERAGE = Boolean.getBoolean("assertion.coverage");

    NON_TEST_DIRS = new File[] { new File(TS_HOME, "src/web"),
        new File(TS_HOME, "src/org/omg/stub"),
        new File(TS_HOME, "src/com/sun/ts/lib"),
        new File(TS_HOME, "src/com/sun/ts/tests/common") };

  }

  protected boolean bJCKServiceTest;

  protected String sInteropDirections = "forward";

  protected File appTestDir;

  private static TSKeywords tsKeywords;

  private VehicleVerifier vehicleVerifier;

  private boolean fastScan;

  private int iNumberOfAssertions = 0;

  private String sClass = "";

  private int executionMode = ExecutionMode.DEFAULT;

  public static Hashtable htTestNamesFound = new Hashtable();

  // ----------------------------------------------------------------
  // Constructors

  /**
   * Constructs the list of file names to exclude for pruning in the search for
   * files to examine for test descriptions. This constructor also sets the
   * allowable comment formats.
   */
  public TSTestFinder() {
    super();
    Arrays.sort(VALID_TAG_NAMES);
    exclude(EXCLUDE_NAMES);
    setInitialTag(null);

    try {
      PropertyManagerInterface propMgr = DeliverableFactory
          .getDeliverableInstance().getPropertyManager();
      executionMode = ExecutionMode.getExecutionMode(propMgr);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Scan a file, looking for test descriptions and/or more files to scan.
   * Overwrite to filter out directories that do not contain any tests.
   * 
   * @param file
   *          The file to scan
   */
  public void scan(File file) {
    for (int i = 0; i < NON_TEST_DIRS.length; i++) {
      if (file.compareTo(NON_TEST_DIRS[i]) == 0) {
        return;
      }
    }
    super.scan(file);
  }

  public static String getAbsolutePath(String testRootPath, String path) {
    return new File(testRootPath, path).getAbsolutePath();
  }

  public static String getAbsolutePath(String path) {
    return getAbsolutePath(TS_HOME + "/src", path);
  }

  public static void main(String[] argv) {
    String sRoot = null;
    Vector vTestDescriptions = new Vector();
    TestDescription td = null;
    for (int ii = 0; ii < argv.length; ii++) {
      if (argv[ii].startsWith("-rootdir")) {
        sRoot = argv[++ii];
      } else {
        TestUtil.logHarness("Must supply a rootdir");
        System.exit(-1);
      }
    }
    TestFinderQueue tfq = new TestFinderQueue();
    TSTestFinder tf = new TSTestFinder();
    try {
      tf.init(TestUtil.EMPTY_STRING_ARRAY, new File(sRoot), null);
      tfq.setTestFinder(tf);
      tfq.setTests(new String[] { sRoot });
    } catch (Exception e) {
      TestUtil.logHarness("Exception initializing the TestFinder");
      e.printStackTrace();
    }
    while ((td = tfq.next()) != null) {
      vTestDescriptions.addElement(td);
    }
    // write all test description info to a file
    try {
      tf.writeToFile(vTestDescriptions);
      TestUtil.logHarness("Total # of Assertions = " + tf.iNumberOfAssertions);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Make sure that the provide name-value pair is of the proper format as
   * described in the tag-spec.
   *
   * @param tagValues
   *          The dictionary of the entries being read
   * @param name
   *          The name of the entry that has been read
   * @param value
   *          The value of the entry that has been read
   */
  protected void processEntry(Map tagValues, String name, String value) {
    if (Arrays.binarySearch(VALID_TAG_NAMES, name) < 0) {
      name = "error";
      value = PARSE_TAG_BAD + name;
    }
    super.processEntry(tagValues, name, value);
  }

  /**
   * Override the scan method in TagTestFinder since we have to deal with
   * CommentStreams that are valid test tags ( but not a test description object
   * of themselves -- the class.setup_props tag ).
   * 
   * @param file
   *          The file to scan
   */
  protected void scanFile(File file) {
    String name = file.getName();

    // Return immediately if we don't have a java source file
    // or we have a java file that we know we don't want to scan
    if (!name.endsWith(".java") || name.equals("BaseUIComponentClient.java")
        || name.equals("DataModelURLClient.java")) {
      return;
    }

    int dot = name.indexOf('.');
    if (dot == 0)
      return;
    if (TestUtil.harnessDebug)
      TestUtil.logHarnessDebug("Scanning " + name);
    String sVal = null;
    String extn = name.substring(dot);
    Class csc = getClassForExtension(extn);
    if (csc == null) {
      String[] msgs = { "no parser found for specified file", "file: " + file,
          "extension: " + extn };
      TestUtil.logErr(msgs[0] + msgs[1] + msgs[2]);
      return;
    }
    CommentStream cs = null;
    try {
      cs = (CommentStream) (csc.newInstance());
    } catch (InstantiationException e) {
      String[] msgs = { "problem instantiating class for extension",
          "extension: " + extn, "class: " + csc.getName() };
      TestUtil.logErr(msgs[0] + msgs[1] + msgs[2]);
      return;
    } catch (IllegalAccessException e) {
      String[] msgs = {
          "illegal access while instantiating class for extension",
          "extension: " + extn, "class: " + csc.getName() };
      TestUtil.logErr(msgs[0] + msgs[1] + msgs[2]);
      return;
    }
    try {
      cs.init(new BufferedReader(new FileReader(file)));
      if (fastScan)
        cs.setFastScan(true);

      sInteropDirections = InteropTestManager
          .getInteropDirections(file.getPath());
      Map propTags = new Hashtable();
      String comment;

      while ((comment = cs.readComment()) != null) {
        Map myTagValues = parseComment(comment, file);
        if (myTagValues.isEmpty()) {
          continue;
        }
        if (myTagValues.containsKey(KEY_CLASS_SETUP_PROPS)
            || myTagValues.containsKey(KEY_CLASS_TEST_ARGS)) {
          propTags = myTagValues;
        } else if (myTagValues.containsKey(KEY_TEST_NAME)) {
          // add the directory that this file is located in, so
          // we can use it later to deploy the jar
          myTagValues.put("testdirectory", file.getPath());
          if (!propTags.isEmpty()) {
            sVal = (String) propTags.get(KEY_CLASS_SETUP_PROPS);
            if (sVal != null) {
              myTagValues.put(KEY_CLASS_SETUP_PROPS, sVal);
            }
            sVal = (String) propTags.get(KEY_CLASS_TEST_ARGS);
            if (sVal != null) {
              myTagValues.put(KEY_CLASS_TEST_ARGS, sVal);
            }
          }
          foundTestDescription(myTagValues, file, 0);
        }
      }
    } catch (FileNotFoundException e) {
      String msgs = "can't find file " + file.getPath();
      e.printStackTrace();
      TestUtil.logErr(msgs);
    } catch (IOException e) {
      String msgs = "problem reading file " + file.getPath();
      e.printStackTrace();
      TestUtil.logErr(msgs);
      error(null, msgs);
    } catch (Throwable e) {
      String msgs = "got exception: problem reading file " + file.getPath();
      e.printStackTrace();
      TestUtil.logErr(msgs);
    } finally {
      try {
        if (cs != null)
          cs.close();
      } catch (IOException e) {
        TestUtil.logErr(e.getMessage());
      }
    }
  }

  protected void foundTestDescription(Map entries, File file, int line) {
    String[] sVal = null;
    TestDescription td = null;
    if (!entries.isEmpty()) {
      vehicleVerifier = VehicleVerifier.getInstance(file,
          (String) entries.get(KEY_TEST_NAME));
      sVal = vehicleVerifier.getVehicleSet();
      if (sVal.length > 0) {
        for (int ii = 0; ii < sVal.length; ii++) {
          if (sInteropDirections.equals(INTEROP_FORWARD)
              || sInteropDirections.equals(INTEROP_BOTH)) {
            // create 1 desc for each vehicle to be tested
            td = createTestDescription(entries, file, sVal[ii].trim(), false);
            foundTestDescription(td);
          }
          if (sInteropDirections.equals(INTEROP_REVERSE)
              || sInteropDirections.equals(INTEROP_BOTH)) {
            // create 1 desc for each vehicle to be tested
            td = createTestDescription(entries, file, sVal[ii].trim(), true);
            foundTestDescription(td);
          }
        }
      } else {
        if (sInteropDirections.equals(INTEROP_FORWARD)
            || sInteropDirections.equals(INTEROP_BOTH)) {
          td = createTestDescription(entries, file, null, false);
          foundTestDescription(td);
        }
        if (sInteropDirections.equals(INTEROP_REVERSE)
            || sInteropDirections.equals(INTEROP_BOTH)) {
          td = createTestDescription(entries, file, null, true);
          foundTestDescription(td);
        }
      }
    }
  }

  private void writeToFile(Vector v) throws IOException {
    TestDescription td = null;
    FileWriter fw = new FileWriter("test_report.txt");
    int iSize = v.size();
    for (int ii = 0; ii < iSize; ii++) {
      td = (TestDescription) v.elementAt(ii);
      writeTest(td, fw);
    }
    fw.close();
  }

  private void writeTest(TestDescription td, FileWriter fw) throws IOException {
    String sName = td.getParameter("testName");
    String sAssertion = td.getParameter("assertion_ids");
    String sLastClass = sClass;
    sClass = td.getParameter("classname");
    sClass = sClass.substring(0, sClass.lastIndexOf('.'));
    if (!sClass.equals(sLastClass)) {
      fw.write(sClass + "\tTests" + TestUtil.NEW_LINE + TestUtil.NEW_LINE);
    }
    fw.write("Test:\t" + sName + TestUtil.NEW_LINE);
    fw.write("Assertion:\t" + sAssertion + TestUtil.NEW_LINE);
    fw.write(TestUtil.NEW_LINE);
    iNumberOfAssertions++;
  }

  private String[] getDefaultVehicles() {
    return defaultVehicles;
  }

  private TestDescription createTestDescription(Map tags, File file,
      String sVehicle, boolean bReverse) {
    Vector propv = new Vector();
    String sval = null;
    String fn = file.getName();
    String key = null;
    String cls = fn.substring(0, fn.indexOf('.'));
    String fullclass = getPkgName(file) + '.' + cls;
    Hashtable desc = new Hashtable();
    desc.put("classname", fullclass);
    String test_dir = null;
    if (bJCKServiceTest) {
      // to id this description as a jckservicetest one
      desc.put("finder", "jck");
      test_dir = appTestDir.getAbsolutePath();
    } else {
      // to id this description as a cts one
      desc.put("finder", "cts");
      test_dir = file.getParent();
    }
    desc.put("test_directory", TestUtil.getRelativePath(test_dir));
    for (Iterator e = tags.keySet().iterator(); e.hasNext();) {
      key = ((String) e.next());

      if ((key.equals(KEY_TEST_NAME))) {
        sval = (String) tags.get(key);
        if (sval != null)
          sval = sval.trim();

        if (htTestNamesFound
            .containsKey((String) (file.getPath() + "#" + sval))) {
          // TestUtil.logHarness("Found duplicate @testName declaration: " +
          // sval);
          // TestUtil.logHarness("Located in source file: " + file.getName());
          Integer iCount = (Integer) (htTestNamesFound
              .get((String) (file.getPath() + "#" + sval)));
          htTestNamesFound.put((String) (file.getPath() + "#" + sval),
              new Integer((iCount.intValue()) + 1));
        } else {
          htTestNamesFound.put((String) (file.getPath() + "#" + sval),
              new Integer(1));
        }

        if (sVehicle != null) {
          sval += "_from_" + sVehicle;
          desc.put("service_eetest", "yes");
        } else {
          desc.put("service_eetest", "no");
        }
        if (bReverse) {
          sval += '_' + "reverse";
          desc.put("direction", INTEROP_REVERSE);
        } else {
          desc.put("direction", INTEROP_FORWARD);
        }
        desc.put("id", sval);
      }
      if ((key.equals(KEY_TEST_NAME)) || (key.equals(KEY_CUSTOM))) {
        sval = (String) tags.get(key);
        if (sval != null)
          sval = sval.trim();
        key = key.substring(0, key.indexOf(':'));
        desc.put(key, sval);
      } else if (key.equals(KEY_ASSERTION_IDS) && PROCESS_ASSERTION) {
        sval = (String) tags.get(key);
        if (sval != null)
          sval = sval.trim();
        key = key.substring(0, key.indexOf(':'));
        if (ASSERTION_COVERAGE) {
          // use the original value as specified in source code for coverage
          // tool
          desc.put(key, sval);
        } else {
          desc.put(key, AssertionMapper.getAssertionDescriptions(sval, file));
        }
      } else if ((key.equals(KEY_CLASS_TEST_ARGS))
          || (key.equals(KEY_TEST_ARGS))) {
        String sCurrentArgs = (String) desc.get("testArgs");
        sval = (String) tags.get(key);
        if (sval == null)
          continue;
        sval = sval.trim();
        key = key.substring(0, key.indexOf(':'));
        if (sCurrentArgs == null)
          desc.put("testArgs", sval);
        else
          desc.put("testArgs", sCurrentArgs + " " + sval);
      } else if ((key.equals(KEY_KEYWORDS))) {
        sval = (String) tags.get(key);
        if (sval != null) {
          sval = sval.trim();
        }
        key = key.substring(0, key.indexOf(':'));

        desc.put(key, sval);
      }
      if ((key.equals(KEY_CLASS_SETUP_PROPS))
          || (key.equals(KEY_TEST_CASE_SETUP_PROPS))) {
        sval = (String) tags.get(key);
        TestUtil
            .logHarnessDebug("KEY_TEST_CASE_SETUP_PROPS sval \"" + sval + "\"");
        if (sval == null)
          continue;
        sval = sval.trim();
        // if there is no final semi-colon add one to avoid the hang
        if (!sval.endsWith(";")) {
          sval = sval + ";";
        }
        TestUtil.logHarnessDebug(
            "KEY_TEST_CASE_SETUP_PROPS normalized sval \"" + sval + "\"");
        int sl = sval.length() - 1;
        int start = 0;
        int count = 1;
        int sep = 0;
        int sep2 = 0;
        while (count < sl) {
          sep = sval.indexOf(';');
          if (sep != -1) {
            String propdesc = sval.substring(start, sep);
            sval = sval.substring(sep + 1);
            // remove description (if there is a ',' in our string)
            // we only want property names
            if ((sep2 = propdesc.indexOf(',')) != -1)
              propdesc = propdesc.substring(0, sep2);
            propv.addElement(propdesc);
            count += sep + 1;
          } else {
            continue;
          }
        }
      }

      // Put properties as space separated String..
      String s1 = "";
      for (int j = 0, size = propv.size(); j < size; j++)
        s1 = s1 + ' ' + propv.elementAt(j);
      desc.put("testProps", s1);
    }

    // add in inherent keywords here
    String sKeywords = (String) desc.get("keywords");

    if (sKeywords != null) {
      sKeywords = sval.trim() + " " + sKeywords;
      sKeywords += " ";
    } else {
      sKeywords = "";
    }

    // default keyword for all tests not containing keywords in the src tags
    sKeywords += "all ";

    // Get keywords from keyword.properties file
    tsKeywords = TSKeywords.getInstance(file);
    String[] sKeywordsFromProperties = tsKeywords.getKeywordSet();

    for (int ii = 0; ii < sKeywordsFromProperties.length; ii++) {

      // For any technologies that may be run on a web profile impl,
      // only attach the javaee_web_profile or <techname>_web_profile keywords
      // if
      // the vehicle in use for the test is required by a web profile impl.
      if (sKeywordsFromProperties[ii].contains("web_profile")
          && sVehicle != null) {
        if ((sVehicle.contains("web") || sVehicle.contains("servlet")
            || sVehicle.contains("jsp") || sVehicle.contains("ejbembed")
            || sVehicle.contains("ejblite") || sVehicle.contains("jsf"))
            && !sVehicle.contains("puservlet")) {
          sKeywords += sKeywordsFromProperties[ii] + " ";
        }
      } else {
        sKeywords += sKeywordsFromProperties[ii] + " ";
      }
    }

    // Add test name as a keyword. This allows you to specify a test name as
    // a keyword to subset tests. Useful if you have a bunch of leaf
    // directories containing the same test names and you want to run just
    // those tests from each dir in a single run.
    sKeywords += desc.get("testName") + " ";

    // Add the vehicle as a standlone keyword. This is useful
    // if you want to use expressions when specifying keywords like the
    // following... javaee.webprofile | connector & web.vehicle
    if (sVehicle != null) {
      sKeywords += sVehicle + "_vehicle ";
    } else {
      sKeywords += "novehicle ";
    }

    // add keywords for forward and reverse tests
    if (bReverse) {
      sKeywords += "reverse";
    } else {
      sKeywords += "forward";
    }
    desc.put("keywords", sKeywords);

    // Print out the keywords for this test description
    TestUtil.logHarnessDebug("Keywords for this test, "
        + (String) tags.get(KEY_TEST_NAME) + ", are:  " + sKeywords);

    if (REPORT_MISSING && !desc.containsKey("assertion_ids")) {
      AssertionMapper.log("WARN: missing assertion_ids tag in testName "
          + desc.get("testName") + " in file " + file.getPath());
    }
    File root = this.getRoot();
    return new TestDescription(root, file, desc);
  }

  private String getPkgName(File clientFile) {
    String result = null;
    String clientFileDir = clientFile.getParent();
    int i = clientFileDir.indexOf(TSTestFinder.SRC_TOKEN);
    if (i != -1) {
      result = clientFileDir.substring(i + 5);
      result = result.replace(File.separatorChar, '.');
    } else {
      result = getPkgName2(clientFile);
    }

    return result;
  }

  private String getPkgName2(File tfile) {
    String pkgName = null;
    String line = null;
    BufferedReader br = null;
    try {
      br = new BufferedReader(new FileReader(tfile));
      while ((line = br.readLine()) != null) {
        line = line.trim();
        if (line.length() > 0 && line.startsWith("package")
            && line.endsWith(";")) {
          int pkgstart = line.indexOf(' ');
          pkgName = line.substring(pkgstart, line.indexOf(';'));
          return pkgName.trim();
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (br != null) {
        try {
          br.close();
        } catch (Exception except) {
        }
      }
    }
    return pkgName;
  }
}
