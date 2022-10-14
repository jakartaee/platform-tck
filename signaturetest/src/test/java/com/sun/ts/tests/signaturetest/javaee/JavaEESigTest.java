/*
 * Copyright (c) 2007, 2021 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.signaturetest.javaee;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import com.sun.javatest.Status;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.signaturetest.SigTestEE;

/**
 * The JavaEESigTest class provides signature tests for the Java EE TCK. This
 * class extends SigTestEE which contains the signature test code. This class is
 * responsible for providing implementations of the abstract method defined in
 * SigTestEE, namely the getPackages method.
 *
 * The EE7 signature tests are to be run in 4 possible modes: (1) full/classic
 * profile (only covers REQUIRED Technologys for this profile) (2) full/classic
 * profile + OPTIONAL technologies (eg , jaxr, etc) (3)
 * web profile (only covers REQUIRED Technologys for this profile) (4) web
 * profile + OPTIONAL technologies (eg connector, jacc, jaxr, etc)
 *
 * The ts.jte's javaee.level property is used to specify these 4 modes of
 * sigtest control. The two basic profile modes (full and web profile) are
 * simple enough. The 2nd and 4th modes takes a bit more explaining. A typical
 * entry for this 4th mode could be something like the following:
 * javaee.level=web connector jacc For this scenario, we would run sigtests for
 * all technologies that are required in the web profile + connector + jacc. So
 * the 2nd and 4th modes are used if you wish to test OPTIONAL technologies
 * along with the REQUIRED technologies within the standard profiles of
 * javaee/full profile or web profile.
 *
 * For JavaEE7, there are 3 optional technologies called out in the EE 7 spec in
 * table EE.6-1: JAXR 1.0 , and Java EE Deployment 1.2. (There is
 * also an optional part of EJB 3.2 (optional part is Entity Beans and EJB QL)
 * BUT while these parts of the EJB are optional, EJB 3.2 MUST have ALL API's
 * that fulfill sigtest requirements. This means that ALL of EJB 3.2 must pass
 * sigtests even though parts of EJB 3.2 (e.g. QL and ENtity Benas) are
 * optional.
 *
 * The list of optional technologies that could be tested along with Java EE 7
 * are: jaxr 
 *
 *
 * The incomplete list of OPTIONAL/ADDITIONAL technologies which could be tested
 * along with the web profile are: jms javamail connector jaspic jacc
 * jaxrs - optional technology in EE 7 jaxr - optional technology
 * in EE 7
 *
 */
public class JavaEESigTest extends SigTestEE {

  public static final String JAVAEE_FULL = "full";

  public static final String JAVAEE_KEYWORD = "javaee";

  public static final String JAVAEE_WEB = "web";

  public static final String JAVAEE_WEB_KEYWORD = "javaee_web_profile";

  public static final String KEYWORD_JAVAEE = "ejb interceptors caj jms wsmd javamail"
      + " cdi di beanval persistence jaxb saaj jaxws connector"
      + " jacc jaspic jsonp jta el servlet jsf jaxrs websocket batch concurrency jsonb securityapi";

  public static final String KEYWORD_WEB = "caj ejb persistence el jsf jsonp jsp servlet jta jaxrs cdi di beanval interceptors websocket concurrency jsonb securityapi";

  public static final ArrayList<String> KEYWORD_JAVAEE_FULL_OPTIONAL_TECHS = new ArrayList<String>();

  public static final ArrayList<String> KEYWORD_WEB_FULL_OPTIONAL_TECHS = new ArrayList<String>(
      Arrays.asList("batch", "connector",
          "jaxws", "jaxb", "jms", "javamail", "jacc", "jaspic",
          "wsmd"));

  enum Containers {
    ejb, servlet, jsp, appclient 
  };

  public static final Map<Containers, Map<String, String[]>> CONTAINER_PACKAGE_MAPS;

  static {

    Map<Containers, Map<String, String[]>> tempMap = new EnumMap<Containers, Map<String, String[]>>(
        Containers.class);

    Map EJB_MAP = new HashMap<String, String[]>();
    Map SERVLET_MAP = new HashMap<String, String[]>();
    Map JSP_MAP = new HashMap<String, String[]>();
    Map APPCLIENT_MAP = new HashMap<String, String[]>();

    // These need to be filled in properly. If one map is a subset of
    // another map we can use putAll to add one map to the other.
    // So maybe populate appclient first then add it to the EJB map
    // then do EJB then add it to JSP then set JSP to SERVLET.

    APPCLIENT_MAP.put("ejb", new String[] { "jakarta.ejb", "jakarta.ejb.spi",
        "jakarta.ejb.embeddable", "jakarta.interceptor", "javax.rmi" });
    APPCLIENT_MAP.put("interceptors", new String[] { "jakarta.interceptor" });
    APPCLIENT_MAP.put("caj", new String[] { "jakarta.annotation",
        "jakarta.annotation.security", "jakarta.annotation.sql" });
    APPCLIENT_MAP.put("jms", new String[] { "jakarta.jms" });
    APPCLIENT_MAP.put("javamail",
        new String[] { "jakarta.mail", "jakarta.mail.event", "jakarta.mail.internet",
            "jakarta.mail.search", "jakarta.mail.util" });
    APPCLIENT_MAP.put("persistence",
        new String[] { "jakarta.persistence", "jakarta.persistence.spi",
            "jakarta.persistence.criteria", "jakarta.persistence.metamodel" });
    // note: cdi API's actually utilize two different base packages and
    // so will require two different sigfiles.
    APPCLIENT_MAP.put("cdi",
        new String[] { "jakarta.decorator", "jakarta.enterprise.context",
            "jakarta.enterprise.context.control",
            "jakarta.enterprise.context.spi", "jakarta.enterprise.event",
            "jakarta.enterprise.inject", "jakarta.enterprise.inject.spi",
            "jakarta.enterprise.inject.literal",
            "jakarta.enterprise.inject.spi.configurator",
            "jakarta.enterprise.util" });
    APPCLIENT_MAP.put("beanval",
        new String[] { "jakarta.validation", "jakarta.validation.bootstrap",
            "jakarta.validation.constraints", "jakarta.validation.groups",
            "jakarta.validation.metadata",
            "jakarta.validation.constraintvalidation",
            "jakarta.validation.executable", "jakarta.validation.spi",
            "jakarta.validation.valueextraction" });
    APPCLIENT_MAP.put("di", new String[] { "jakarta.inject" });
    APPCLIENT_MAP.put("jsonp",
        new String[] { "jakarta.json", "jakarta.json.spi", "jakarta.json.stream" });
    APPCLIENT_MAP.put("jsonb",
        new String[] { "jakarta.json.bind", "jakarta.json.bind.adapter",
        "jakarta.json.bind.annotation", "jakarta.json.bind.config",
        "jakarta.json.bind.serializer", "jakarta.json.bind.spi" });

    EJB_MAP.putAll(APPCLIENT_MAP);
    EJB_MAP.put("connector",
        new String[] { "jakarta.resource", "jakarta.resource.cci",
            "jakarta.resource.spi", "jakarta.resource.spi.work",
            "jakarta.resource.spi.endpoint", "jakarta.resource.spi.security" });
    EJB_MAP.put("jacc", new String[] { "jakarta.security.jacc" });
    EJB_MAP.put("jaspic",
        new String[] { "jakarta.security.auth.message",
            "jakarta.security.auth.message.callback",
            "jakarta.security.auth.message.config",
            "jakarta.security.auth.message.module" });
    EJB_MAP.put("jta",
        new String[] { "jakarta.transaction" });
    EJB_MAP.put("batch",
        new String[] { "jakarta.batch.api", "jakarta.batch.api.chunk",
            "jakarta.batch.api.chunk.listener", "jakarta.batch.api.listener",
            "jakarta.batch.api.partition", "jakarta.batch.operations",
            "jakarta.batch.runtime", "jakarta.batch.runtime.context" });
    EJB_MAP.put("securityapi",
        new String[] { "jakarta.security.enterprise",
        "jakarta.security.enterprise.authentication.mechanism.http",
        "jakarta.security.enterprise.credential",
        "jakarta.security.enterprise.identitystore" });
    EJB_MAP.put("concurrency", 
        new String[] { "jakarta.enterprise.concurrent" });

    SERVLET_MAP.putAll(EJB_MAP);
    SERVLET_MAP.put("el", new String[] { "jakarta.el" });
    SERVLET_MAP.put("servlet",
        new String[] { "jakarta.servlet", "jakarta.servlet.annotation",
            "jakarta.servlet.descriptor", "jakarta.servlet.http",
            "jakarta.servlet.jsp", "jakarta.servlet.jsp.el",
            "jakarta.servlet.jsp.jstl.core", "jakarta.servlet.jsp.jstl.fmt",
            "jakarta.servlet.jsp.jstl.sql", "jakarta.servlet.jsp.jstl.tlv",
            "jakarta.servlet.jsp.tagext" });
    SERVLET_MAP.put("jsf",
        new String[] { "jakarta.faces", "jakarta.faces.application",
            "jakarta.faces.annotation",
            "jakarta.faces.component", "jakarta.faces.bean", "jakarta.faces.flow",
            "jakarta.faces.flow.builder", "jakarta.faces.component.behavior",
            "jakarta.faces.component.html", "jakarta.faces.component.visit",
            "jakarta.faces.component.search",
            "jakarta.faces.context", "jakarta.faces.convert", "jakarta.faces.el",
            "jakarta.faces.event", "jakarta.faces.lifecycle", "jakarta.faces.model",
            "jakarta.faces.push",
            "jakarta.faces.render", "jakarta.faces.validator", "jakarta.faces.view",
            "jakarta.faces.view.facelets", "jakarta.faces.webapp" });
    SERVLET_MAP.put("jaxrs", new String[] { "jakarta.ws.rs", "jakarta.ws.rs.core",
        "jakarta.ws.rs.ext", "jakarta.ws.rs.client", "jakarta.ws.rs.container",
        "jakarta.ws.rs.sse"  });
    SERVLET_MAP.put("websocket",
        new String[] { "jakarta.websocket", "jakarta.websocket.server" });

    JSP_MAP = SERVLET_MAP;

    tempMap.put(Containers.ejb, Collections.unmodifiableMap(EJB_MAP));
    tempMap.put(Containers.servlet, Collections.unmodifiableMap(SERVLET_MAP));
    tempMap.put(Containers.jsp, Collections.unmodifiableMap(JSP_MAP));
    tempMap.put(Containers.appclient,
        Collections.unmodifiableMap(APPCLIENT_MAP));
    CONTAINER_PACKAGE_MAPS = Collections.unmodifiableMap(tempMap);

  } // end static block

  /* Instance Variables */
  private boolean isFullEEProfile = true;

  private String level;

  private String otherTechnologyKeywords = null;

  private void debugit(String str) {
    TestUtil.logTrace(str);
  }

  /**
   * Set the current level, either web profile or full EE. If isFullEEProfile is
   * false it must be web since there are only two valid values, full and web.
   * It is possible that the javaee.level in the ts.jte is set to web +
   * <some_additional_technologies> so we want to make sure that the string is
   * parsed appropriately.
   * 
   */
  private void setLevel() {
    level = testInfo.getJavaeeLevel().toLowerCase();

    // The javaee.level should be a string and could contain the following
    // possible entries:
    // 1. javaee.level=full
    // 2. javaee.level=web
    // 3. javaee.level=web + <additional_tech_names>
    // 4. anything else we cant reconcile will default to "full" behavior.

    debugit("in setLevel()");
    debugit("javaee.level = " + level);

    if (level.contains(JAVAEE_FULL)) {
      // if the javaee.level contains "full" or some variant string
      // such as "blarg + full" then we will assume it is "full"
      isFullEEProfile = true;
      debugit("isFullEEProfile = true");
      otherTechnologyKeywords = level.replace(JAVAEE_FULL, "");
    } else if (level.contains(JAVAEE_WEB)) {
      // if here, we have web profile. we need to check for
      // the additional technologies that might be specified in
      // the (ts.jte) javaee.level propery) javaee.level additional
      // props would be specified as: javaee.level = web foo1 foo2 ...
      isFullEEProfile = false;
      debugit("isFullEEProfile = false");
      otherTechnologyKeywords = level.replace(JAVAEE_WEB, "");
    } else {
      // if our level did not contain "full" or "web" assume "full"
      System.err.println("ERROR level set to \"" + level + "\"");
      System.err.println(
          "\t level must be set to " + JAVAEE_FULL + " or " + JAVAEE_WEB);
      System.err.println("Tests will run assuming level:  " + JAVAEE_FULL);
      isFullEEProfile = true;
      debugit("level is indeterminate so setting isFullEEProfile = true");
    }
    debugit("JavaEESigTest.ssetLevel():  otherTechnologyKeywords = "
        + otherTechnologyKeywords);
  }

  /**
   * This method uses the ts.jte's javaee.level property to find the terminal
   * keywords that make up the web or full keywords, depending on the current
   * javaee.level. In other words, the full and web profiles are aggregate
   * keywords. This method determines the terminal keywords that make up those
   * aggregate keywords.
   *
   * The returned result is a set of keywords that serve as keys into the maps
   * above where each keyword represents a set of pacakages for a given
   * techonology (eg connector, jacc, etc)
   */
  private Set<String> resolveAggregateKeywords() throws IOException {
    String tsHome = testInfo.getTSHome();

    Set<String> result = new HashSet<String>();
    String strKeywords;
    if (isFullEEProfile) {
      // get javaee keyword
      strKeywords = KEYWORD_JAVAEE;
    } else {
      // assume web profile so get javaee_web_profile keyword
      strKeywords = KEYWORD_WEB;
    }
    if (otherTechnologyKeywords != null) {
      strKeywords = strKeywords + " " + otherTechnologyKeywords;
    }
    List terminals = getTerminals(strKeywords);
    result.addAll(terminals);
    debugit("JavaEESigTest.resolveAggregateKeywords(): strKeywords = "
        + strKeywords);

    return result;
  }

  private List<String> getTerminals(String s) {
    List<String> result = new ArrayList<String>();
    StringTokenizer st = new StringTokenizer(s, " \t\n\r\f,");
    while (st.hasMoreTokens()) {
      String keyword = st.nextToken().trim().toLowerCase();
      if (!(keyword.equals(JAVAEE_FULL) || keyword.equals(JAVAEE_KEYWORD)
          || keyword.equals(JAVAEE_WEB)
          || keyword.equals(JAVAEE_WEB_KEYWORD))) {
        result.add(keyword);
        debugit("added the following keyword to result:  " + keyword);
      } else {
        debugit("not adding keyword (" + keyword + ") to result");
      }
    }
    return result;
  }

  /**
   * 
   */
  @Override
  protected ArrayList<String> getUnlistedOptionalPackages() {
    ArrayList unlistedOptPkgs = new ArrayList<String>();
    ArrayList unlistedKeywords = new ArrayList<String>();

    debugit("Enterred getUnlistedOptionalPackages()");

    // get list of technology keywords specified in (ts.jte) javaee.level
    // property
    // (i.e. otherTechnologyKeywords)

    if (isFullEEProfile) {
      unlistedKeywords = cloneArrayList(KEYWORD_JAVAEE_FULL_OPTIONAL_TECHS);
      if (otherTechnologyKeywords == null) {
        // this could be null if no optional technologies were listed in which
        // case we want all optional technology keywords
        // so don't remove ANYTHING from our list of unlistedKeywords to search
        // for
        debugit(
            "No Optional Technology Keywords found in ts.jte's javaee.level property.");
      } else {
        // we have some optional technologies that were listed in the (ts.jte)
        // javaee.level
        // so be sure to remove any of these from the list of
        debugit(
            "Optional Technology Keywords found in ts.jte's javaee.level property: "
                + otherTechnologyKeywords);
        otherTechnologyKeywords = otherTechnologyKeywords.trim();
        String[] otherTechKeywords = otherTechnologyKeywords.split(" ");
        for (int ii = 0; ii < otherTechKeywords.length; ii++) {
          unlistedKeywords.remove(otherTechKeywords[ii]);
        }
      }
    } else {
      // assume web profile
      unlistedKeywords = cloneArrayList(KEYWORD_WEB_FULL_OPTIONAL_TECHS);
      if (otherTechnologyKeywords == null) {
        // this could be null if no optional technologies were listed in which
        // case we want all the optional technology keywords (relevant to web
        // profile)
        // so don't remove ANYTHING from our list of unlistedKeywords to search
        // for
        debugit(
            "No Optional Technology Keywords found in ts.jte's javaee.level property.");
      } else {
        // we have some optional technologies that were listed in the (ts.jte)
        // javaee.level
        // so be sure to remove any of these from the list of
        debugit(
            "Optional Technology Keywords found in ts.jte's javaee.level property: "
                + otherTechnologyKeywords);
        otherTechnologyKeywords = otherTechnologyKeywords.trim();
        String[] otherTechKeywords = otherTechnologyKeywords.split(" ");
        for (int ii = 0; ii < otherTechKeywords.length; ii++) {
          unlistedKeywords.remove(otherTechKeywords[ii]);
        }
      }
    }

    // once we know which keywords (for optional technology areas) are not
    // explicitly
    // listed for testing, we will take those and get the packages for them and
    // return
    // those packages.
    for (int ii = 0; ii < unlistedKeywords.size(); ii++) {
      // loop thru list of optional technology keywords
      debugit("getting packages for Optional Technology Keyword == "
          + unlistedKeywords.get(ii));

      for (Containers cc : Containers.values()) {
        // loop thru each container looking for matching technology keyword
        Map<String, String[]> containerMap = CONTAINER_PACKAGE_MAPS.get(cc);
        if (containerMap.containsKey(unlistedKeywords.get(ii))) {
          // see if the container/vehicle contains the optional tech keyword,
          // if so, then add the packages for that keyword
          debugit("Container: " + cc
              + "  contains packages for Optional Technolgoy of: "
              + unlistedKeywords.get(ii));
          String[] pkgs = containerMap.get(unlistedKeywords.get(ii));
          for (String otPkg : pkgs) {
            unlistedOptPkgs.add(otPkg);
          }
          break;
        }
      }
    }

    debugit(
        "getUnlistedOptionalPackages(): returning the following unlistedOptPkgs: "
            + unlistedOptPkgs.toString());

    return unlistedOptPkgs;
  }

  private ArrayList<String> cloneArrayList(ArrayList<String> listToBeCloned) {
    ArrayList<String> returnVal = new ArrayList<String>(listToBeCloned.size());
    for (String str : listToBeCloned) {
      returnVal.add(new String(str));
    }
    return returnVal;
  }

  /**
   * ** Abstract Method Implementations **
   */

  /**
   * Returns a list of strings where each string represents a package name. Each
   * package name will have it's signature tested by the signature test
   * framework.
   * 
   * @param vehicleName
   *          The name of the JavaEE container where the signature tests should
   *          be conducted.
   * @return String[] The names of the packages whose signatures should be
   *         verified.
   */
  protected String[] getPackages(String vehicleName) {

    Set<String> packages = new HashSet<String>();
    Containers vehicle = Containers.valueOf(vehicleName);
    Set<String> keywords = null;

    debugit("vehicleName = " + vehicleName);

    setLevel(); // set the level instance variable to web or full
    try {
      // get keywords that make up web or full profile
      keywords = resolveAggregateKeywords();
    } catch (IOException ioe) {
      // something happened with the keyword.properties file
    }

    dumpKeywords(keywords);

    Map<String, String[]> vehicleMap = CONTAINER_PACKAGE_MAPS.get(vehicle);
    dumpMap(vehicle, vehicleMap);
    for (String keyword : keywords) {
      if (vehicleMap.containsKey(keyword)) {
        packages.addAll(Arrays.asList(vehicleMap.get(keyword)));
      }
    }

    dumpPackages(vehicle, packages);
    return packages.toArray(new String[packages.size()]);
  }

  // Debugging fun
  private void dumpKeywords(Set<String> words) {
    debugit("Level is set to \"" + level + "\"");
    debugit("\tKeywords:");
    for (String s : words) {
      debugit("\t\t" + s);
    }
    debugit("// dumpKeywords *****");
  }

  private void dumpMap(Containers vehicle, Map<String, String[]> packages) {
    debugit("Vehicle is set to \"" + vehicle + "\"");
    for (String s : packages.keySet()) {
      if (packages != null) {
        String[] pkgs = packages.get(s);
        String outStr = s + " -> ";
        for (int ii = 0; ii < pkgs.length; ii++) {
          outStr += pkgs[ii] + " ";
        }
        debugit(outStr);
      } else {
        debugit(s + " ->  no packages ");
      }
    }
    debugit("// dumpMap *****");
  }

  private void dumpPackages(Containers vehicle, Set<String> packages) {
    debugit("Vehicle is set to \"" + vehicle + "\"");
    if (packages != null) {
      String outStr = "";
      for (Iterator i = packages.iterator(); i.hasNext();) {
        String item = (String) i.next();
        outStr += item + " ";
      }
      debugit(outStr);
    } else {
      debugit("no packages found");
    }
    debugit("// dumpPackages *****");
  }

  /**
   * ** Boilerplate Code ****
   */

  /*
   * Initial entry point for JavaTest.
   */
  public static void main(String[] args) {
    JavaEESigTest theTests = new JavaEESigTest();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /*
   * @class.setup_props: sigTestClasspath, Location of JavaEE jar files;
   * ts_home, The base path of this TCK; javaee.level, current EE level defined
   * in ts.jte web or full; optional.tech.packages.to.ignore, for excluding
   * certain packages that may exist in Optional technologies;
   * jtaJarClasspath, The Location of the JTA API jar file;
   */

  // XXX current.keywords does not seem to be in the props instance
  /*
   * @testName: signatureTest
   * 
   * @assertion: A JavaEE platform must implement the required classes and and
   * APIs specified in the JavaEE Platform Specification.
   * 
   * @test_Strategy: Using reflection, gather the implementation specific
   * classes and APIs. Compare these results with the expected (required)
   * classes and APIs.
   */

  /*
   * Call the parent class's cleanup method.
   */

} // end class CTSSigTest
