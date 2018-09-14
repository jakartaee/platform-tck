/*
 * Copyright (c) 2009, 2018 Oracle and/or its affiliates. All rights reserved.
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
 * $Id$
 */

package com.sun.ts.tests.jsf.api.javax_faces.model.scalardatamodel;

import java.io.PrintWriter;

import com.sun.javatest.Status;
import com.sun.ts.tests.jsf.api.javax_faces.model.common.DataModelURLClient;
import com.sun.ts.lib.harness.EETest;

public final class URLClient extends DataModelURLClient {

  private static final String CONTEXT_ROOT = "/jsf_model_scalardm_web";

  public static void main(String[] args) {
    URLClient theTests = new URLClient();
    Status s = theTests.run(args, new PrintWriter(System.out, true),
        new PrintWriter(System.err, true));
    s.exit();
  }

  public Status run(String[] args, PrintWriter out, PrintWriter err) {
    setContextRoot(CONTEXT_ROOT);
    setServletName(DEFAULT_SERVLET_NAME);
    return super.run(args, out, err);
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   */

  /* Test Declarations */

  /**
   * @testName: scalarDataModelCtorTest
   * @assertion_ids: JSF:JAVADOC:2018; JSF:JAVADOC:2019
   * @test_Strategy: Verify that creation of a ScalarDataModel instance results
   *                 in the data being passed to the ctor to be wrapped.
   */
  public void scalarDataModelCtorTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "scalarDataModelCtorTest");
    invoke();
  }

  /**
   * @testName: dataModelAddGetRemoveDataModelListenerTest
   * @assertion_ids: JSF:JAVADOC:1930; JSF:JAVADOC:1933; JSF:JAVADOC:1945
   * @test_Strategy: Verify the addition, retrieval, and removal of Listeners
   *                 behaves as expected.
   */
  public void dataModelAddGetRemoveDataModelListenerTest() throws EETest.Fault {
    super.dataModelAddGetRemoveDataModelListenerTest();
  }

  /**
   * @testName: dataModelGetRowCountTest
   * @assertion_ids: JSF:JAVADOC:2023; JSF:JAVADOC:2008
   * @test_Strategy: Verify -1 is returned by getRowCount() when there is no
   *                 backing data. Additionally verify the proper value is
   *                 returned once setWrappedData() is called with a valid
   *                 value.
   */
  public void dataModelGetRowCountTest() throws EETest.Fault {
    super.dataModelGetRowCountTest();
  }

  /**
   * @testName: dataModelGetRowDataIAETest
   * @assertion_ids: JSF:JAVADOC:2012
   * @test_Strategy: Verify an IllegalArgumentException is thrown if
   *                 getRowData() is called and the current row index is out of
   *                 range.
   */
  public void dataModelGetRowDataIAETest() throws EETest.Fault {
    super.dataModelGetRowDataIAETest();
  }

  /**
   * @testName: dataModelGetRowDataTest
   * @assertion_ids: JSF:JAVADOC:2010
   * @test_Strategy: Verify getRowData() returns the expected result based on
   *                 varying index values.
   */
  public void dataModelGetRowDataTest() throws EETest.Fault {
    super.dataModelGetRowDataTest();
  }

  /**
   * @testName: dataModelGetSetRowIndexTest
   * @assertion_ids: JSF:JAVADOC:2013; JSF:JAVADOC:2020
   * @test_Strategy: Verify -1 is returned by getRowIndex() if no backing data
   *                 is available. Additionally verify that getRowIndex()
   *                 returns the value as set by setRowIndex().
   */
  public void dataModelGetSetRowIndexTest() throws EETest.Fault {
    super.dataModelGetSetRowIndexTest();
  }

  /**
   * @testName: dataModelGetSetWrappedDataTest
   * @assertion_ids: JSF:JAVADOC:2015; JSF:JAVADOC:2023
   * @test_Strategy: Verify wrapped data set via setWrappedData() is returned as
   *                 expected.
   */
  public void dataModelGetSetWrappedDataTest() throws EETest.Fault {
    super.dataModelGetSetWrappedDataTest();
  }

  /**
   * @testName: dataModelIsRowAvailableTest
   * @assertion_ids: JSF:JAVADOC:2016
   * @test_Strategy: Verify false is returned by isRowAvailable() if the
   *                 DataModel instance has no backing data. Then, call
   *                 setWrappedData() with a valid Object and verify true is
   *                 returned.
   */
  public void dataModelIsRowAvailableTest() throws EETest.Fault {
    super.dataModelIsRowAvailableTest();
  }

  /**
   * @testName: dataModelListenerTest
   * @assertion_ids: JSF:JAVADOC:2023; JSF:JAVADOC:2020
   * @test_Strategy: Verify the following: - Verify registered listeners are
   *                 called in order of registration when setWrappedData() is
   *                 called with a valid value. - Verify registered listeners
   *                 are called in order of registration when data has already
   *                 been wrapped, and setRowIndex has been called with a new,
   *                 valid, index value. - No events are fired if setRowIndex()
   *                 is called and the new value is the same as the previous
   *                 value. - No events are fired if setRowIndex() is called
   *                 when there is no backing data - ensure the index value set
   *                 is stored.
   */
  public void dataModelListenerTest() throws EETest.Fault {
    super.dataModelListenerTest();
  }

  /**
   * @testName: dataModelSetRowIndexIAETest
   * @assertion_ids: JSF:JAVADOC:2022
   * @test_Strategy: Verify an IllegalArgumentException is thrown if
   *                 setRowIndex() is called with an index value less than -1.
   */
  public void dataModelSetRowIndexIAETest() throws EETest.Fault {
    super.dataModelSetRowIndexIAETest();
  }

  /**
   * @testName: dataModelRemoveDataModelListenerNPETest
   * @assertion_ids: JSF:JAVADOC:1946
   * @test_Strategy: Verify a NulPointException is thrown if listener is null.
   */
  public void dataModelRemoveDataModelListenerNPETest() throws EETest.Fault {
    super.dataModelRemoveDataModelListenerNPETest();
  }

  /**
   * @testName: dataModelAddDataModelListenerNPETest
   * @assertion_ids: JSF:JAVADOC:1931
   * @test_Strategy: Verify a NulPointException is thrown if listener is null.
   */
  public void dataModelAddDataModelListenerNPETest() throws EETest.Fault {
    super.dataModelAddDataModelListenerNPETest();
  }
} // end of URLClient
