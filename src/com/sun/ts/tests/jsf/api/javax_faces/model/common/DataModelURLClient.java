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

package com.sun.ts.tests.jsf.api.javax_faces.model.common;

import java.io.PrintWriter;

import com.sun.javatest.Status;
import com.sun.ts.tests.jsf.common.client.AbstractUrlClient;

public abstract class DataModelURLClient extends AbstractUrlClient {

  /**
   * testName: dataModelGetSetWrappedDataTest
   * 
   * @assertion_ids: JSF:JAVADOC:1941; JSF:JAVADOC:1950
   * @test_Strategy: Verify wrapped data set via setWrappedData() is returned as
   *                 expected.
   */
  public void dataModelGetSetWrappedDataTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "dataModelGetSetWrappedDataTest");
    invoke();
  }

  /**
   * testName: dataModelSetWrappedDataCCETest
   * 
   * @assertion_ids: JSF:JAVADOC:1951
   * @test_Strategy: Verify a ClassCastException is thrown if the data to be
   *                 wrapped is incorrect for the DataModel being used.
   */
  public void dataModelSetWrappedDataCCETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "dataModelSetWrappedDataCCETest");
    invoke();
  }

  /**
   * testName: dataModelAddGetRemoveDataModelListenerTest
   * 
   * @assertion_ids: JSF:JAVADOC:1930; JSF:JAVADOC:1933; JSF:JAVADOC:1945
   * @test_Strategy: Verify the addition, retrieval, and removal of Listeners
   *                 behaves as expected.
   */
  public void dataModelAddGetRemoveDataModelListenerTest() throws Fault {
    TEST_PROPS.setProperty(APITEST,
        "dataModelAddGetRemoveDataModelListenerTest");
    invoke();
  }

  /**
   * testName: dataModelIsRowAvailableTest
   * 
   * @assertion_ids: JSF:JAVADOC:1942
   * @test_Strategy: Verify false is returned by isRowAvailable() if the
   *                 DataModel instance has no backing data. Then, call
   *                 setWrappedData() with a valid Object and verify true is
   *                 returned.
   */
  public void dataModelIsRowAvailableTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "dataModelIsRowAvailableTest");
    invoke();
  }

  /**
   * testName: dataModelGetRowCountTest
   * 
   * @assertion_ids: JSF:JAVADOC:1934
   * @test_Strategy: Verify -1 is returned by getRowCount() when there is no
   *                 backing data. Additionally verify the proper value is
   *                 returned once setWrappedData() is called with a valid
   *                 value.
   */
  public void dataModelGetRowCountTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "dataModelGetRowCountTest");
    invoke();
  }

  /**
   * testName: dataModelGetSetRowIndexTest
   * 
   * @assertion_ids: JSF:JAVADOC:1939; JSF:JAVADOC:1947
   * @test_Strategy: Verify -1 is returned by getRowIndex() if no backing data
   *                 is available. Additionally verify that getRowIndex()
   *                 returns the value as set by setRowIndex().
   */
  public void dataModelGetSetRowIndexTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "dataModelGetSetRowIndexTest");
    invoke();
  }

  /**
   * testName: dataModelSetRowIndexIAETest
   * 
   * @assertion_ids: JSF:JAVADOC:1949
   * @test_Strategy: Verify an IllegalArgumentException is thrown if
   *                 setRowIndex() is called with an index value less than -1.
   */
  public void dataModelSetRowIndexIAETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "dataModelSetRowIndexIAETest");
    invoke();
  }

  /**
   * testName: dataModelGetRowDataTest
   * 
   * @assertion_ids: JSF:JAVADOC:1936
   * @test_Strategy: Verify getRowData() returns the expected result based on
   *                 varying index values.
   */
  public void dataModelGetRowDataTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "dataModelGetRowDataTest");
    invoke();
  }

  /**
   * testName: dataModelGetRowDataIAETest
   * 
   * @assertion_ids: JSF:JAVADOC:1938
   * @test_Strategy: Verify an IllegalArgumentException is thrown if
   *                 getRowData() is called and the current row index is out of
   *                 range.
   */
  public void dataModelGetRowDataIAETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "dataModelGetRowDataIAETest");
    invoke();
  }

  /**
   * testName: dataModelListenerTest
   * 
   * @assertion_ids: JSF:JAVADOC:1932; JSF:JAVADOC:1933; JSF:JAVADOC:1947
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
  public void dataModelListenerTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "dataModelListenerTest");
    invoke();
  }

  /**
   * testName: dataModelRemoveDataModelListenerNPETest
   * 
   * @assertion_ids: JSF:JAVADOC:1946
   * @test_Strategy: Verify a NulPointException is thrown if listener is null.
   * 
   */
  public void dataModelRemoveDataModelListenerNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "dataModelRemoveDataModelListenerNPETest");
    invoke();
  }

  /**
   * testName: dataModelAddDataModelListenerNPETest
   * 
   * @assertion_ids: JSF:JAVADOC:1931
   * @test_Strategy: Verify a NulPointException is thrown if listener is null.
   * 
   */
  public void dataModelAddDataModelListenerNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "dataModelAddDataModelListenerNPETest");
    invoke();
  }

} // end of URLClient
