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

package com.sun.ts.tests.signaturetest.javaee;

import java.util.*;
import java.io.*;

import com.sun.javatest.Status;
import com.sun.ts.tests.signaturetest.SigTestEE;
import com.sun.ts.lib.util.TestUtil;

/**
 * The JavaEESigTest class provides signature tests for the Java EE TCK. This
 * class extends SigTestEE which contains the signature test code. This class is
 * responsible for providing implementations of the abstract method defined in
 * SigTestEE, namely the getPackages method.
 *
 * The EE7 signature tests are to be run in 4 possible modes: (1) full/classic
 * profile (only covers REQUIRED Technologys for this profile) (2) full/classic
 * profile + OPTIONAL technologies (eg , jaxr, jaxrpc, javaeedeploy, etc) (3)
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
 * table EE.6-1: JAXRPC 1.1 , JAXR 1.0 , and Java EE Deployment 1.2. (There is
 * also an optional part of EJB 3.2 (optional part is Entity Beans and EJB QL)
 * BUT while these parts of the EJB are optional, EJB 3.2 MUST have ALL API's
 * that fulfill sigtest requirements. This means that ALL of EJB 3.2 must pass
 * sigtests even though parts of EJB 3.2 (e.g. QL and ENtity Benas) are
 * optional.
 *
 * The list of optional technologies that could be tested along with Java EE 7
 * are: jaxrpc jaxr javaeedeploy
 *
 *
 * The incomplete list of OPTIONAL/ADDITIONAL technologies which could be tested
 * along with the web profile are: jms javamail javaeemgmt connector jaspic jacc
 * jaxrs javaeedeploy - optional technology in EE 7 jaxr - optional technology
 * in EE 7 jaxrpc - optional technology in EE 7
 *
 */
public class JavaEESigTest extends SigTestEE {

  public static final String JAVAEE_FULL = "full";

  public static final String JAVAEE_KEYWORD = "javaee";

  public static final String JAVAEE_WEB = "web";

  public static final String JAVAEE_WEB_KEYWORD = "javaee_web_profile";

  public static final String KEYWORD_JAVAEE = "ejb interceptors caj jms wsmd javamail javaeemgmt"
      + " cdi di beanval persistence jaxb saaj jaxws connector"
      + " jacc jaspic jsonp jta el servlet jsf jaxrs websocket batch concurrency jsonb securityapi";

  public static final String KEYWORD_WEB = "caj ejb persistence el jsf jsonp jsp servlet jta jaxrs cdi di beanval interceptors websocket jsonb securityapi";

  public static final ArrayList<String> KEYWORD_JAVAEE_FULL_OPTIONAL_TECHS = new ArrayList<String>(
      Arrays.asList("jaxrpc", "jaxr", "javaeedeploy"));

  public static final ArrayList<String> KEYWORD_WEB_FULL_OPTIONAL_TECHS = new ArrayList<String>(
      Arrays.asList("batch", "jaxrpc", "jaxr", "javaeedeploy", "connector",
          "jaxws", "jaxb", "jms", "javamail", "javaeemgmt", "jacc", "jaspic",
          "wsmd"));

  enum Containers {
    ejb, servlet, jsp, appclient, standalone
  };

  public static final Map<Containers, Map<String, String[]>> CONTAINER_PACKAGE_MAPS;

  static {

    Map<Containers, Map<String, String[]>> tempMap = new EnumMap<Containers, Map<String, String[]>>(
        Containers.class);

    Map EJB_MAP = new HashMap<String, String[]>();
    Map SERVLET_MAP = new HashMap<String, String[]>();
    Map JSP_MAP = new HashMap<String, String[]>();
    Map APPCLIENT_MAP = new HashMap<String, String[]>();
    Map STANDALONE_MAP = new HashMap<String, String[]>();

    // These need to be filled in properly. If one map is a subset of
    // another map we can use putAll to add one map to the other.
    // So maybe populate appclient first then add it to the EJB map
    // then do EJB then add it to JSP then set JSP to SERVLET.
    // I think standalone is a disjoint set.

    APPCLIENT_MAP.put("ejb", new String[] { "javax.ejb", "javax.ejb.spi",
        "javax.ejb.embeddable", "javax.interceptor" });
    APPCLIENT_MAP.put("interceptors", new String[] { "javax.interceptor" });
    APPCLIENT_MAP.put("caj", new String[] { "javax.annotation",
        "javax.annotation.security", "javax.annotation.sql" });
    APPCLIENT_MAP.put("jms", new String[] { "javax.jms" });
    APPCLIENT_MAP.put("javamail",
        new String[] { "javax.mail", "javax.mail.event", "javax.mail.internet",
            "javax.mail.search", "javax.mail.util" });
    APPCLIENT_MAP.put("javaeemgmt", new String[] { "javax.management.j2ee",
        "javax.management.j2ee.statistics" });
    APPCLIENT_MAP.put("persistence",
        new String[] { "javax.persistence", "javax.persistence.spi",
            "javax.persistence.criteria", "javax.persistence.metamodel" });
    APPCLIENT_MAP.put("jaxr",
        new String[] { "javax.xml.registry", "javax.xml.registry.infomodel" });
    APPCLIENT_MAP.put("jaxrpc",
        new String[] { "javax.xml.rpc", "javax.xml.rpc.encoding",
            "javax.xml.rpc.handler", "javax.xml.rpc.handler.soap",
            "javax.xml.rpc.holders", "javax.xml.rpc.server",
            "javax.xml.rpc.soap" });
    // note: cdi API's actually utilize two different base packages and
    // so will require two different sigfiles.
    APPCLIENT_MAP.put("cdi",
        new String[] { "javax.decorator", "javax.enterprise.context",
            "javax.enterprise.context.control",
            "javax.enterprise.context.spi", "javax.enterprise.event",
            "javax.enterprise.inject", "javax.enterprise.inject.spi",
            "javax.enterprise.inject.literal",
            "javax.enterprise.inject.spi.configurator",
            "javax.enterprise.util" });
    APPCLIENT_MAP.put("beanval",
        new String[] { "javax.validation", "javax.validation.bootstrap",
            "javax.validation.constraints", "javax.validation.groups",
            "javax.validation.metadata",
            "javax.validation.constraintvalidation",
            "javax.validation.executable", "javax.validation.spi",
            "javax.validation.valueextraction" });
    APPCLIENT_MAP.put("di", new String[] { "javax.inject" });
    APPCLIENT_MAP.put("jsonp",
        new String[] { "javax.json", "javax.json.spi", "javax.json.stream" });
    APPCLIENT_MAP.put("jsonb",
        new String[] { "javax.json.bind", "javax.json.bind.adapter",
        "javax.json.bind.annotation", "javax.json.bind.config",
        "javax.json.bind.serializer", "javax.json.bind.spi" });

    EJB_MAP.putAll(APPCLIENT_MAP);
    EJB_MAP.put("connector",
        new String[] { "javax.resource", "javax.resource.cci",
            "javax.resource.spi", "javax.resource.spi.work",
            "javax.resource.spi.endpoint", "javax.resource.spi.security" });
    EJB_MAP.put("jacc", new String[] { "javax.security.jacc" });
    EJB_MAP.put("jaspic",
        new String[] { "javax.security.auth.message",
            "javax.security.auth.message.callback",
            "javax.security.auth.message.config",
            "javax.security.auth.message.module" });
    EJB_MAP.put("jta",
        new String[] { "javax.transaction" });
    EJB_MAP.put("batch",
        new String[] { "javax.batch.api", "javax.batch.api.chunk",
            "javax.batch.api.chunk.listener", "javax.batch.api.listener",
            "javax.batch.api.partition", "javax.batch.operations",
            "javax.batch.runtime", "javax.batch.runtime.context" });
    EJB_MAP.put("concurrency", new String[] { "javax.enterprise.concurrent" });
    EJB_MAP.put("securityapi",
        new String[] { "javax.security.enterprise",
        "javax.security.enterprise.authentication.mechanism.http",
        "javax.security.enterprise.credential",
        "javax.security.enterprise.identitystore" });

    SERVLET_MAP.putAll(EJB_MAP);
    SERVLET_MAP.put("el", new String[] { "javax.el" });
    SERVLET_MAP.put("servlet",
        new String[] { "javax.servlet", "javax.servlet.annotation",
            "javax.servlet.descriptor", "javax.servlet.http",
            "javax.servlet.jsp", "javax.servlet.jsp.el",
            "javax.servlet.jsp.jstl.core", "javax.servlet.jsp.jstl.fmt",
            "javax.servlet.jsp.jstl.sql", "javax.servlet.jsp.jstl.tlv",
            "javax.servlet.jsp.tagext" });
    SERVLET_MAP.put("jsf",
        new String[] { "javax.faces", "javax.faces.application",
            "javax.faces.annotation",
            "javax.faces.component", "javax.faces.bean", "javax.faces.flow",
            "javax.faces.flow.builder", "javax.faces.component.behavior",
            "javax.faces.component.html", "javax.faces.component.visit",
            "javax.faces.component.search",
            "javax.faces.context", "javax.faces.convert", "javax.faces.el",
            "javax.faces.event", "javax.faces.lifecycle", "javax.faces.model",
            "javax.faces.push",
            "javax.faces.render", "javax.faces.validator", "javax.faces.view",
            "javax.faces.view.facelets", "javax.faces.webapp" });
    SERVLET_MAP.put("jaxrs", new String[] { "javax.ws.rs", "javax.ws.rs.core",
        "javax.ws.rs.ext", "javax.ws.rs.client", "javax.ws.rs.container",
        "javax.ws.rs.sse"  });
    SERVLET_MAP.put("websocket",
        new String[] { "javax.websocket", "javax.websocket.server" });

    JSP_MAP = SERVLET_MAP;

    STANDALONE_MAP.put("javaeedeploy",
        new String[] { "javax.enterprise.deploy.model",
            "javax.enterprise.deploy.model.exceptions",
            "javax.enterprise.deploy.shared",
            "javax.enterprise.deploy.shared.factories",
            "javax.enterprise.deploy.spi",
            "javax.enterprise.deploy.spi.exceptions",
            "javax.enterprise.deploy.spi.factories",
            "javax.enterprise.deploy.spi.status" });

    tempMap.put(Containers.ejb, Collections.unmodifiableMap(EJB_MAP));
    tempMap.put(Containers.servlet, Collections.unmodifiableMap(SERVLET_MAP));
    tempMap.put(Containers.jsp, Collections.unmodifiableMap(JSP_MAP));
    tempMap.put(Containers.appclient,
        Collections.unmodifiableMap(APPCLIENT_MAP));
    tempMap.put(Containers.standalone,
        Collections.unmodifiableMap(STANDALONE_MAP));
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
   * certain packages that may exist in Optional technologies (such as partial
   * jaxrpc impl in web profile);
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
