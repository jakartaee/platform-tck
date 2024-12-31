/*
 * Copyright (c) 2007, 2020 Oracle and/or its affiliates. All rights reserved.
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
 * @(#)JSTLClient.java	1.2 03/19/02
 */

package com.sun.ts.tests.jstl.spec.sql.result;

import java.io.IOException;
import java.io.InputStream;

import com.sun.ts.tests.jstl.common.client.SqlUrlClient;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;
import org.jboss.shrinkwrap.api.asset.UrlAsset;

@Tag("jstl")
@Tag("platform")
@Tag("web")
@ExtendWith(ArquillianExtension.class)
public class JSTLClientIT extends SqlUrlClient {

  public static String packagePath = JSTLClientIT.class.getPackageName().replace(".", "/");



  /** Creates new JSTLClient */
  public JSTLClientIT() {
    setGeneralURI("/jstl/spec/sql/result");
    setContextRoot("/jstl_sql_result_web");
    setGoldenFileDir("/jstl/spec/sql/result");
  }

  @Deployment(testable = false)
  public static WebArchive createDeployment() throws IOException {

    WebArchive archive = ShrinkWrap.create(WebArchive.class, "jstl_sql_result_web.war");
    archive.setWebXML(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/jstl_sql_result_web.xml"));

    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveResultGetColumnNamesCountTest.jsp")), "positiveResultGetColumnNamesCountTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveResultGetColumnNamesTest.jsp")), "positiveResultGetColumnNamesTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveResultGetRowsByIndexCountTest.jsp")), "positiveResultGetRowsByIndexCountTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveResultGetRowsByIndexTest.jsp")), "positiveResultGetRowsByIndexTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveResultGetRowsCountTest.jsp")), "positiveResultGetRowsCountTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveResultGetRowsLowerCaseTest.jsp")), "positiveResultGetRowsLowerCaseTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveResultGetRowsUpperCaseTest.jsp")), "positiveResultGetRowsUpperCaseTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveResultIsLimitedByMaxRowsTest.jsp")), "positiveResultIsLimitedByMaxRowsTest.jsp");

    archive.addAsWebInfResource(JSTLClientIT.class.getPackage(), "tssql.stmt", "jstl-sql.properties");    

    archive.addAsLibrary(getCommonJarArchive());

    return archive;
  }

  /*
   * @testName: positiveResultIsLimitedByMaxRowsTest
   * 
   * @assertion_ids: JSTL:SPEC:59
   * 
   * @testStrategy: Validate the behavior of the sql:query action - That a query
   * executed using maxRows attribute returns the correct value for
   * Result.isLimitedByMaxRows()
   */
  @Test
  public void positiveResultIsLimitedByMaxRowsTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveResultIsLimitedByMaxRowsTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveResultIsLimitedByMaxRowsTest");
    invoke();
  }

  /*
   * @testName: positiveResultGetRowsLowerCaseTest
   * 
   * @assertion_ids: JSTL:SPEC:59
   * 
   * @testStrategy: Validate the access to Result.getRows is case insensitive. -
   * For a row returned by getRows(), specify the column names in lower case.
   */
  @Test
  public void positiveResultGetRowsLowerCaseTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveResultGetRowsLowerCaseTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveResultGetRowsLowerCaseTest");
    invoke();
  }

  /*
   * @testName: positiveResultGetRowsUpperCaseTest
   * 
   * @assertion_ids: JSTL:SPEC:59
   * 
   * @testStrategy: Validate the access to Result.getRows is case insensitive. -
   * For a row returned by getRows(), specify the column names in upper case.
   */
  @Test
  public void positiveResultGetRowsUpperCaseTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveResultGetRowsUpperCaseTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveResultGetRowsUpperCaseTest");
    invoke();
  }

  /*
   * @testName: positiveResultGetColumnNamesTest
   * 
   * @assertion_ids: JSTL:SPEC:59
   * 
   * @testStrategy: Validate the values returned by Result.getColumns can be
   * used to access the column values returned by Result.getRows().
   */
  @Test
  public void positiveResultGetColumnNamesTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveResultGetColumnNamesTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveResultGetColumnNamesTest");
    invoke();
  }

  /*
   * @testName: positiveResultGetRowsByIndexTest
   * 
   * @assertion_ids: JSTL:SPEC:59
   * 
   * @testStrategy: Validate that you can access each column for a given row
   * which is returned by Result.getRowsByIndex().
   */
  @Test
  public void positiveResultGetRowsByIndexTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveResultGetRowsByIndexTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveResultGetRowsByIndexTest");
    invoke();
  }

  /*
   * @testName: positiveResultGetRowsByIndexCountTest
   * 
   * @assertion_ids: JSTL:SPEC:59
   * 
   * @testStrategy: Validate that the correct number of rows is returned by
   * Result.getRowsByIndex().
   */
  @Test
  public void positiveResultGetRowsByIndexCountTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveResultGetRowsByIndexCountTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveResultGetRowsByIndexCountTest");
    invoke();
  }

  /*
   * @testName: positiveResultGetRowsCountTest
   * 
   * @assertion_ids: JSTL:SPEC:59
   * 
   * @testStrategy: Validate that the correct number of rows is returned by
   * Result.getRows().
   */
  @Test
  public void positiveResultGetRowsCountTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveResultGetRowsCountTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveResultGetRowsCountTest");
    invoke();
  }

  /*
   * @testName: positiveResultGetColumnNamesCountTest
   * 
   * @assertion_ids: JSTL:SPEC:59
   * 
   * @testStrategy: Validate that the correct number of columns is returned by
   * Result.getColumnNames().
   */
  @Test
  public void positiveResultGetColumnNamesCountTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveResultGetColumnNamesCountTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveResultGetColumnNamesCountTest");
    invoke();
  }
}
