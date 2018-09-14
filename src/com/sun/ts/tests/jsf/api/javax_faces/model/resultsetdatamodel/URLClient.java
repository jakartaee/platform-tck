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

package com.sun.ts.tests.jsf.api.javax_faces.model.resultsetdatamodel;

import java.io.PrintWriter;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.tests.jsf.api.javax_faces.model.common.DataModelURLClient;

public final class URLClient extends DataModelURLClient {

  private static final String CONTEXT_ROOT = "/jsf_model_resultsetdm_web";

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
   * @testName: resultSetDataModelCtorTest
   * @assertion_ids: JSF:JAVADOC:2001; JSF:JAVADOC:2002
   * @test_Strategy: Verify that creation of a ResultSetDataModel instance
   *                 results in the data being passed to the ctor to be wrapped.
   */
  public void resultSetDataModelCtorTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "resultSetDataModelCtorTest");
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
   * @testName: dataModelGetRowDataIAETest
   * @assertion_ids: JSF:JAVADOC:1995
   * @test_Strategy: Verify an IllegalArgumentException is thrown if
   *                 getRowData() is called and the current row index is out of
   *                 range.
   */
  public void dataModelGetRowDataIAETest() throws EETest.Fault {
    super.dataModelGetRowDataIAETest();
  }

  /**
   * @testName: dataModelGetSetRowIndexTest
   * @assertion_ids: JSF:JAVADOC:1996; JSF:JAVADOC:2003
   * @test_Strategy: Verify -1 is returned by getRowIndex() if no backing data
   *                 is available. Additionally verify that getRowIndex()
   *                 returns the value as set by setRowIndex().
   */
  public void dataModelGetSetRowIndexTest() throws EETest.Fault {
    super.dataModelGetSetRowIndexTest();
  }

  /**
   * @testName: dataModelGetSetWrappedDataTest
   * @assertion_ids: JSF:JAVADOC:1998; JSF:JAVADOC:2006
   * @test_Strategy: Verify wrapped data set via setWrappedData() is returned as
   *                 expected.
   */
  public void dataModelGetSetWrappedDataTest() throws EETest.Fault {
    super.dataModelGetSetWrappedDataTest();
  }

  /**
   * @testName: dataModelIsRowAvailableTest
   * @assertion_ids: JSF:JAVADOC:1999; JSF:JAVADOC:2006
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
   * @assertion_ids: JSF:JAVADOC:2006; JSF:JAVADOC:2003
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
   * @assertion_ids: JSF:JAVADOC:2005
   * @test_Strategy: Verify an IllegalArgumentException is thrown if
   *                 setRowIndex() is called with an index value less than -1.
   */
  public void dataModelSetRowIndexIAETest() throws EETest.Fault {
    super.dataModelSetRowIndexIAETest();
  }

  /**
   * @testName: dataModelSetWrappedDataCCETest
   * @assertion_ids: JSF:JAVADOC:2007
   * @test_Strategy: Verify a ClassCastException is thrown if the data to be
   *                 wrapped is incorrect for the DataModel being used.
   */
  public void dataModelSetWrappedDataCCETest() throws EETest.Fault {
    super.dataModelSetWrappedDataCCETest();
  }

  /**
   * @testName: resultSetDataModelGetRowDataSpecializedMapTest
   * @assertion_ids: JSF:JAVADOC:2002
   * @test_Strategy: Verify the following: Validate the following specialized
   *                 behavior for the Map returned by ResultSetDataModel: - The
   *                 Map, and any supporting objects it returns, must perform
   *                 all column name comparisons in a case-insensitive manner. -
   *                 The following methods must throw
   *                 UnsupportedOperationException: clear(), remove(). - The
   *                 entrySet() method must return a Set that has the following
   *                 behavior: * Throw UnsupportedOperationException for any
   *                 attempt to add or remove entries from the Set, either
   *                 directly or indirectly through an Iterator returned by the
   *                 Set. * Updates to the value of an entry in this set must
   *                 write through to the corresponding column value in the
   *                 underlying ResultSet. - The keySet() method must return a
   *                 Set that throws UnsupportedOperationException on any
   *                 attempt to add or remove keys, either directly or through
   *                 an Iterator returned by the Set. - The put() method must
   *                 throw IllegalArgumentException if a key value for which
   *                 containsKey() returns false is specified. However, if a key
   *                 already present in the Map is specified, the specified
   *                 value must write through to the corresponding column value
   *                 in the underlying ResultSet. - The values() method must
   *                 return a Collection that throws
   *                 UnsupportedOperationException on any attempt to add or
   *                 remove values, either directly or through an Iterator
   *                 returned by the Collection.
   */
  public void resultSetDataModelGetRowDataSpecializedMapTest() throws Fault {
    TEST_PROPS.setProperty(APITEST,
        "resultSetDataModelGetRowDataSpecializedMapTest");
    invoke();
  }

  /**
   * @testName: resultSetDataModelGetRowDataMapTest
   * @assertion_ids: JSF:JAVADOC:1993
   * @test_Strategy: Verify the methods of positive aspects of the Map instance
   *                 returned by getRowData() based on the descriptions of
   *                 AbstractMap.
   */
  public void resultSetDataModelGetRowDataMapTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "resultSetDataModelGetRowDataMapTest");
    invoke();
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
