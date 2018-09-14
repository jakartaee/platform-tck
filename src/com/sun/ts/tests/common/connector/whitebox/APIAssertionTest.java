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

package com.sun.ts.tests.common.connector.whitebox;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import com.sun.ts.tests.common.connector.util.*;
import javax.resource.*;
import javax.resource.spi.*;
import javax.resource.spi.work.*;
import java.beans.PropertyDescriptor;
import java.io.PrintWriter;

/*
 * This class is used to assist with testing API Assertions.
 */
public class APIAssertionTest {

  public APIAssertionTest() {
  }

  public static void checkManagedConnectionAPI(ManagedConnection mcon) {
    if (mcon == null) {
      // should not get here
      Debug.trace(
          "Error - null MetaData passed into APIAssertionTest.checkMetaData()");
    }

    try {
      PrintWriter p = mcon.getLogWriter();
      logAPIPass("ManagedConnection.getLogWriter() passed");
    } catch (Exception ex) {
      Debug.trace("Error verifying ManagedConnection.getLogWriter()");
    }

    try {
      ManagedConnectionMetaData dd = mcon.getMetaData();
      logAPIPass("ManagedConnection.getMetaData() passed");
    } catch (ResourceException ex) {
      // we could get this exception and still be considered passing
      logAPIPass("ManagedConnection.getMetaData() passed");
    } catch (Exception ex) {
      Debug.trace("Error verifying ManagedConnection.getXAResource()");
    }

    try {
      LocalTransaction lt = mcon.getLocalTransaction();
      logAPIPass("ManagedConnection.getLocalTransaction() passed");
    } catch (Exception ex) {
      Debug.trace("Error verifying ManagedConnection.getLocalTransaction()");
    }
  }

  public static void checkMetaDataAPI(ManagedConnectionMetaData mdata) {
    if (mdata == null) {
      // should not get here
      Debug.trace(
          "Error - null MetaData passed into APIAssertionTest.checkMetaData()");
    }
    logAPIPass("Connection.getMetaData() passed");

    try {
      String eisProdName = mdata.getEISProductName();
      logAPIPass("ManagedConnectionMetaData.getEISProductName() passed");
    } catch (Exception ex) {
      Debug.trace(
          "Error verifying ManagedConnectionMetaData.getEISProductName()");
    }

    try {
      String eisProdVer = mdata.getEISProductVersion();
      logAPIPass("ManagedConnectionMetaData.getEISProductVersion() passed");
    } catch (Exception ex) {
      Debug.trace(
          "Error verifying ManagedConnectionMetaData.getEISProductVersion()");
    }

    try {
      int maxCons = mdata.getMaxConnections();
      logAPIPass("ManagedConnectionMetaData.getMaxConnections() passed");
    } catch (Exception ex) {
      Debug.trace(
          "Error verifying ManagedConnectionMetaData.getMaxConnections()");
    }

    try {
      String userName = mdata.getUserName();
      logAPIPass("ManagedConnectionMetaData.getUserName() passed");
    } catch (Exception ex) {
      Debug.trace("Error verifying ManagedConnectionMetaData.getUserName()");
    }
  }

  public void runTests() {
    checkNotSupportedException();
    checkResourceException();
    checkLocalTransactionException();
    checkResourceAdapterInternalException();
    checkResourceAllocationException();
    checkSecurityException();
    checkSharingViolationException();
    checkUnavailableException();
    checkWorkException();
    checkWorkCompletedException();
    checkWorkRejectedException();
    checkEISSystemException();
    checkInvalidPropertyException();
    checkApplicationServerInternalException();
    checkCommException();
    checkIllegalStateException();
    checkRetryableUnavailableException();
    checkRetryableWorkRejectedException();
    checkHintsContext();
  }

  /*
   * used to assist with verifying assertions:
   * 
   * 
   */
  private void checkHintsContext() {

    try {
      HintsContext hc = new HintsContext();
      logAPIPass("HintsContext() passed");

      hc.setName("hintName");
      String strname = hc.getName();
      if ((strname != null) && (strname.equalsIgnoreCase("hintName"))) {
        logAPIPass("HintsContext.setName() and HintsContext.getName() passed.");
      }

      hc.setDescription("hintDescription");
      String strDesc = hc.getDescription();
      if (strDesc != null) {
        // may not be exactly same desc that was set - though it *should* be
        logAPIPass(
            "HintsContext.setDescription() and HintsContext.getDescription() passed.");
      }

      hc.setHint(HintsContext.NAME_HINT, "someHintVal");
      Map m = hc.getHints();
      logAPIPass("HintsContext.setHints() and HintsContext.getHints() passed.");

    } catch (Exception ex) {
      Debug.trace("Error verifying InvalidPropertyException(null)");
    }

  }

  /*
   * used to assist with verifying assertions:
   * 
   * 
   */
  private void checkInvalidPropertyException() {

    try {
      InvalidPropertyException ne = new InvalidPropertyException();
      throw ne;
    } catch (InvalidPropertyException ex) {
      logAPIPass("InvalidPropertyException(null) passed");
    } catch (Exception ex) {
      Debug.trace("Error verifying InvalidPropertyException(null)");
    }

    try {
      InvalidPropertyException ne = new InvalidPropertyException("message1");
      throw ne;
    } catch (InvalidPropertyException ex) {
      logAPIPass("InvalidPropertyException(str) passed");
    } catch (Exception ex) {
      Debug.trace("Error verifying InvalidPropertyException(str)");
    }

    try {
      InvalidPropertyException ne = new InvalidPropertyException("message1",
          "ERRCODE1");
      throw ne;
    } catch (InvalidPropertyException ex) {
      logAPIPass("InvalidPropertyException(str, str) passed");
    } catch (Exception ex) {
      Debug.trace("Error verifying InvalidPropertyException(str, str)");
    }

    try {
      Exception someThrowable = new Exception("test");
      InvalidPropertyException ne = new InvalidPropertyException(someThrowable);
      throw ne;
    } catch (InvalidPropertyException ex) {
      logAPIPass("InvalidPropertyException(throwable) passed");
    } catch (Exception ex) {
      Debug.trace("Error verifying InvalidPropertyException(throwable)");
    }

    try {
      Exception someThrowable = new Exception("test");
      InvalidPropertyException ne = new InvalidPropertyException("someString",
          someThrowable);
      throw ne;
    } catch (InvalidPropertyException ex) {
      logAPIPass("InvalidPropertyException(str, throwable) passed");
    } catch (Exception ex) {
      Debug.trace("Error verifying InvalidPropertyException(str, throwable)");
    }

    try {
      InvalidPropertyException ne = new InvalidPropertyException("message1");
      List beanProps = new ArrayList();
      beanProps.add(new PropertyDescriptor("destinationName",
          LocalTxActivationSpec.class));
      beanProps.add(new PropertyDescriptor("destinationType",
          LocalTxActivationSpec.class));
      PropertyDescriptor[] pd = (PropertyDescriptor[]) beanProps
          .toArray(new PropertyDescriptor[beanProps.size()]);
      ne.setInvalidPropertyDescriptors(pd);
      Debug.trace("throwing setInvalidPropertyDescriptors(pd)");
      throw ne;
    } catch (InvalidPropertyException ex) {
      PropertyDescriptor[] pd = (PropertyDescriptor[]) ex
          .getInvalidPropertyDescriptors();
      logAPIPass(
          "InvalidPropertyException.setInvalidPropertyDescriptors() passed");
      logAPIPass(
          "InvalidPropertyException.getInvalidPropertyDescriptors() passed");
    } catch (Exception ex) {
      Debug.trace("Error verifying InvalidPropertyException(str)");
      ex.printStackTrace();
    }
  }

  /*
   * used to assist with verifying assertions:
   * 
   * 
   */
  private void checkEISSystemException() {

    try {
      EISSystemException ne = new EISSystemException();
      throw ne;
    } catch (EISSystemException ex) {
      logAPIPass("EISSystemException(null) passed");
    } catch (Exception ex) {
      Debug.trace("Error verifying EISSystemException(null)");
    }

    try {
      EISSystemException ne = new EISSystemException("message1");
      throw ne;
    } catch (EISSystemException ex) {
      logAPIPass("EISSystemException(str) passed");
    } catch (Exception ex) {
      Debug.trace("Error verifying EISSystemException(str)");
    }

    try {
      EISSystemException ne = new EISSystemException("message1", "ERRCODE1");
      throw ne;
    } catch (EISSystemException ex) {
      logAPIPass("EISSystemException(str, str) passed");
    } catch (Exception ex) {
      Debug.trace("Error verifying EISSystemException(str, str)");
    }

    try {
      Exception someThrowable = new Exception("test");
      EISSystemException ne = new EISSystemException(someThrowable);
      throw ne;
    } catch (EISSystemException ex) {
      logAPIPass("EISSystemException(throwable) passed");
    } catch (Exception ex) {
      Debug.trace("Error verifying EISSystemException(throwable)");
    }

    try {
      Exception someThrowable = new Exception("test");
      EISSystemException ne = new EISSystemException("someString",
          someThrowable);
      throw ne;
    } catch (EISSystemException ex) {
      logAPIPass("EISSystemException(str, throwable) passed");
    } catch (Exception ex) {
      Debug.trace("Error verifying EISSystemException(str, throwable)");
    }
  }

  /*
   * used to assist with verifying assertions:
   * 
   * 
   */
  private void checkIllegalStateException() {

    try {
      javax.resource.spi.IllegalStateException ne = new javax.resource.spi.IllegalStateException();
      throw ne;
    } catch (javax.resource.spi.IllegalStateException ex) {
      logAPIPass("IllegalStateException(null) passed");
    } catch (Exception ex) {
      Debug.trace("Error verifying IllegalStateException(null)");
    }

    try {
      javax.resource.spi.IllegalStateException ne = new javax.resource.spi.IllegalStateException(
          "message1");
      throw ne;
    } catch (javax.resource.spi.IllegalStateException ex) {
      logAPIPass("IllegalStateException(str) passed");
    } catch (Exception ex) {
      Debug.trace("Error verifying IllegalStateException(str)");
    }

    try {
      javax.resource.spi.IllegalStateException ne = new javax.resource.spi.IllegalStateException(
          "message1", "ERRCODE1");
      throw ne;
    } catch (javax.resource.spi.IllegalStateException ex) {
      logAPIPass("IllegalStateException(str, str) passed");
    } catch (Exception ex) {
      Debug.trace("Error verifying IllegalStateException(str, str)");
    }

    try {
      Exception someThrowable = new Exception("test");
      javax.resource.spi.IllegalStateException ne = new javax.resource.spi.IllegalStateException(
          someThrowable);
      throw ne;
    } catch (javax.resource.spi.IllegalStateException ex) {
      logAPIPass("IllegalStateException(throwable) passed");
    } catch (Exception ex) {
      Debug.trace("Error verifying IllegalStateException(throwable)");
    }

    try {
      Exception someThrowable = new Exception("test");
      javax.resource.spi.IllegalStateException ne = new javax.resource.spi.IllegalStateException(
          "someString", someThrowable);
      throw ne;
    } catch (javax.resource.spi.IllegalStateException ex) {
      logAPIPass("IllegalStateException(str, throwable) passed");
    } catch (Exception ex) {
      Debug.trace("Error verifying IllegalStateException(str, throwable)");
    }
  }

  /*
   * used to assist with verifying assertions:
   * 
   * 
   */
  private void checkCommException() {

    try {
      CommException ne = new CommException();
      throw ne;
    } catch (CommException ex) {
      logAPIPass("CommException(null) passed");
    } catch (Exception ex) {
      Debug.trace("Error verifying CommException(null)");
    }

    try {
      CommException ne = new CommException("message1");
      throw ne;
    } catch (CommException ex) {
      logAPIPass("CommException(str) passed");
    } catch (Exception ex) {
      Debug.trace("Error verifying CommException(str)");
    }

    try {
      CommException ne = new CommException("message1", "ERRCODE1");
      throw ne;
    } catch (CommException ex) {
      logAPIPass("CommException(str, str) passed");
    } catch (Exception ex) {
      Debug.trace("Error verifying CommException(str, str)");
    }

    try {
      Exception someThrowable = new Exception("test");
      CommException ne = new CommException(someThrowable);
      throw ne;
    } catch (CommException ex) {
      logAPIPass("CommException(throwable) passed");
    } catch (Exception ex) {
      Debug.trace("Error verifying CommException(throwable)");
    }

    try {
      Exception someThrowable = new Exception("test");
      CommException ne = new CommException("someString", someThrowable);
      throw ne;
    } catch (CommException ex) {
      logAPIPass("CommException(str, throwable) passed");
    } catch (Exception ex) {
      Debug.trace("Error verifying CommException(str, throwable)");
    }
  }

  /*
   * used to assist with verifying assertions:
   * 
   * 
   */
  private void checkRetryableWorkRejectedException() {

    try {
      RetryableWorkRejectedException ne = new RetryableWorkRejectedException();
      throw ne;
    } catch (RetryableWorkRejectedException ex) {
      logAPIPass("RetryableWorkRejectedException(null) passed");
    } catch (Exception ex) {
      Debug.trace("Error verifying RetryableWorkRejectedException(null)");
    }

    try {
      RetryableWorkRejectedException ne = new RetryableWorkRejectedException(
          "message1");
      throw ne;
    } catch (RetryableWorkRejectedException ex) {
      logAPIPass("RetryableWorkRejectedException(str) passed");
    } catch (Exception ex) {
      Debug.trace("Error verifying RetryableWorkRejectedException(str)");
    }

    try {
      RetryableWorkRejectedException ne = new RetryableWorkRejectedException(
          "message1", "ERRCODE1");
      throw ne;
    } catch (RetryableWorkRejectedException ex) {
      logAPIPass("RetryableWorkRejectedException(str, str) passed");
    } catch (Exception ex) {
      Debug.trace("Error verifying RetryableWorkRejectedException(str, str)");
    }

    try {
      Exception someThrowable = new Exception("test");
      RetryableWorkRejectedException ne = new RetryableWorkRejectedException(
          someThrowable);
      throw ne;
    } catch (RetryableWorkRejectedException ex) {
      logAPIPass("RetryableWorkRejectedException(throwable) passed");
    } catch (Exception ex) {
      Debug.trace("Error verifying RetryableWorkRejectedException(throwable)");
    }

    try {
      Exception someThrowable = new Exception("test");
      RetryableWorkRejectedException ne = new RetryableWorkRejectedException(
          "someString", someThrowable);
      throw ne;
    } catch (RetryableWorkRejectedException ex) {
      logAPIPass("RetryableWorkRejectedException(str, throwable) passed");
    } catch (Exception ex) {
      Debug.trace(
          "Error verifying RetryableWorkRejectedException(str, throwable)");
    }
  }

  /*
   * used to assist with verifying assertions:
   * 
   * 
   */
  private void checkRetryableUnavailableException() {

    try {
      RetryableUnavailableException ne = new RetryableUnavailableException();
      throw ne;
    } catch (RetryableUnavailableException ex) {
      logAPIPass("RetryableUnavailableException(null) passed");
    } catch (Exception ex) {
      Debug.trace("Error verifying RetryableUnavailableException(null)");
    }

    try {
      RetryableUnavailableException ne = new RetryableUnavailableException(
          "message1");
      throw ne;
    } catch (RetryableUnavailableException ex) {
      logAPIPass("RetryableUnavailableException(str) passed");
    } catch (Exception ex) {
      Debug.trace("Error verifying RetryableUnavailableException(str)");
    }

    try {
      RetryableUnavailableException ne = new RetryableUnavailableException(
          "message1", "ERRCODE1");
      throw ne;
    } catch (RetryableUnavailableException ex) {
      logAPIPass("RetryableUnavailableException(str, str) passed");
    } catch (Exception ex) {
      Debug.trace("Error verifying RetryableUnavailableException(str, str)");
    }

    try {
      Exception someThrowable = new Exception("test");
      RetryableUnavailableException ne = new RetryableUnavailableException(
          someThrowable);
      throw ne;
    } catch (RetryableUnavailableException ex) {
      logAPIPass("RetryableUnavailableException(throwable) passed");
    } catch (Exception ex) {
      Debug.trace("Error verifying RetryableUnavailableException(throwable)");
    }

    try {
      Exception someThrowable = new Exception("test");
      RetryableUnavailableException ne = new RetryableUnavailableException(
          "someString", someThrowable);
      throw ne;
    } catch (RetryableUnavailableException ex) {
      logAPIPass("RetryableUnavailableException(str, throwable) passed");
    } catch (Exception ex) {
      Debug.trace(
          "Error verifying RetryableUnavailableException(str, throwable)");
    }
  }

  /*
   * used to assist with verifying assertions:
   * 
   * 
   */
  private void checkApplicationServerInternalException() {

    try {
      ApplicationServerInternalException ne = new ApplicationServerInternalException();
      throw ne;
    } catch (ApplicationServerInternalException ex) {
      logAPIPass("ApplicationServerInternalException(null) passed");
    } catch (Exception ex) {
      Debug.trace("Error verifying ApplicationServerInternalException(null)");
    }

    try {
      ApplicationServerInternalException ne = new ApplicationServerInternalException(
          "message1");
      throw ne;
    } catch (ApplicationServerInternalException ex) {
      logAPIPass("ApplicationServerInternalException(str) passed");
    } catch (Exception ex) {
      Debug.trace("Error verifying ApplicationServerInternalException(str)");
    }

    try {
      ApplicationServerInternalException ne = new ApplicationServerInternalException(
          "message1", "ERRCODE1");
      throw ne;
    } catch (ApplicationServerInternalException ex) {
      logAPIPass("ApplicationServerInternalException(str, str) passed");
    } catch (Exception ex) {
      Debug.trace(
          "Error verifying ApplicationServerInternalException(str, str)");
    }

    try {
      Exception someThrowable = new Exception("test");
      ApplicationServerInternalException ne = new ApplicationServerInternalException(
          someThrowable);
      throw ne;
    } catch (ApplicationServerInternalException ex) {
      logAPIPass("ApplicationServerInternalException(throwable) passed");
    } catch (Exception ex) {
      Debug.trace(
          "Error verifying ApplicationServerInternalException(throwable)");
    }

    try {
      Exception someThrowable = new Exception("test");
      ApplicationServerInternalException ne = new ApplicationServerInternalException(
          "someString", someThrowable);
      throw ne;
    } catch (ApplicationServerInternalException ex) {
      logAPIPass("ApplicationServerInternalException(str, throwable) passed");
    } catch (Exception ex) {
      Debug.trace(
          "Error verifying ApplicationServerInternalException(str, throwable)");
    }
  }

  /*
   * used to assist with verifying assertions:
   * 
   * 
   */
  private void checkWorkException() {

    try {
      WorkException ne = new WorkException();
      throw ne;
    } catch (WorkException ex) {
      logAPIPass("WorkException(null) passed");
    } catch (Exception ex) {
      Debug.trace("Error verifying WorkException(null)");
    }

    try {
      WorkException ne = new WorkException("message1");
      throw ne;
    } catch (WorkException ex) {
      logAPIPass("WorkException(str) passed");
    } catch (Exception ex) {
      Debug.trace("Error verifying WorkException(str)");
    }

    try {
      WorkException ne = new WorkException("message1", "ERRCODE1");
      throw ne;
    } catch (WorkException ex) {
      logAPIPass("WorkException(str, str) passed");
    } catch (Exception ex) {
      Debug.trace("Error verifying WorkException(str, str)");
    }

    try {
      Exception someThrowable = new Exception("test");
      WorkException ne = new WorkException(someThrowable);
      throw ne;
    } catch (WorkException ex) {
      logAPIPass("WorkException(throwable) passed");
    } catch (Exception ex) {
      Debug.trace("Error verifying WorkException(throwable)");
    }

    try {
      Exception someThrowable = new Exception("test");
      WorkException ne = new WorkException("someString", someThrowable);
      throw ne;
    } catch (WorkException ex) {
      logAPIPass("WorkException(str, throwable) passed");
    } catch (Exception ex) {
      Debug.trace("Error verifying WorkException(str, throwable)");
    }
  }

  /*
   * used to assist with verifying assertions:
   * 
   * 
   */
  private void checkWorkCompletedException() {

    try {
      WorkCompletedException ne = new WorkCompletedException();
      throw ne;
    } catch (WorkCompletedException ex) {
      logAPIPass("WorkCompletedException(null) passed");
    } catch (Exception ex) {
      Debug.trace("Error verifying WorkCompletedException(null)");
    }

    try {
      WorkCompletedException ne = new WorkCompletedException("message1");
      throw ne;
    } catch (WorkCompletedException ex) {
      logAPIPass("WorkCompletedException(str) passed");
    } catch (Exception ex) {
      Debug.trace("Error verifying WorkCompletedException(str)");
    }

    try {
      WorkCompletedException ne = new WorkCompletedException("message1",
          "ERRCODE1");
      throw ne;
    } catch (WorkCompletedException ex) {
      logAPIPass("WorkCompletedException(str, str) passed");
    } catch (Exception ex) {
      Debug.trace("Error verifying WorkCompletedException(str, str)");
    }

    try {
      Exception someThrowable = new Exception("test");
      WorkCompletedException ne = new WorkCompletedException(someThrowable);
      throw ne;
    } catch (WorkCompletedException ex) {
      logAPIPass("WorkCompletedException(throwable) passed");
    } catch (Exception ex) {
      Debug.trace("Error verifying WorkCompletedException(throwable)");
    }

    try {
      Exception someThrowable = new Exception("test");
      WorkCompletedException ne = new WorkCompletedException("someString",
          someThrowable);
      throw ne;
    } catch (WorkCompletedException ex) {
      logAPIPass("WorkCompletedException(str, throwable) passed");
    } catch (Exception ex) {
      Debug.trace("Error verifying WorkCompletedException(str, throwable)");
    }
  }

  /*
   * used to assist with verifying assertions:
   * 
   * 
   */
  private void checkWorkRejectedException() {

    try {
      WorkRejectedException ne = new WorkRejectedException();
      throw ne;
    } catch (WorkRejectedException ex) {
      logAPIPass("WorkRejectedException(null) passed");
    } catch (Exception ex) {
      Debug.trace("Error verifying WorkRejectedException(null)");
    }

    try {
      WorkRejectedException ne = new WorkRejectedException("message1");
      throw ne;
    } catch (WorkRejectedException ex) {
      logAPIPass("WorkRejectedException(str) passed");
    } catch (Exception ex) {
      Debug.trace("Error verifying WorkRejectedException(str)");
    }

    try {
      WorkRejectedException ne = new WorkRejectedException("message1",
          "ERRCODE1");
      throw ne;
    } catch (WorkRejectedException ex) {
      logAPIPass("WorkRejectedException(str, str) passed");
    } catch (Exception ex) {
      Debug.trace("Error verifying WorkRejectedException(str, str)");
    }

    try {
      Exception someThrowable = new Exception("test");
      WorkRejectedException ne = new WorkRejectedException(someThrowable);
      throw ne;
    } catch (WorkRejectedException ex) {
      logAPIPass("WorkRejectedException(throwable) passed");
    } catch (Exception ex) {
      Debug.trace("Error verifying WorkRejectedException(throwable)");
    }

    try {
      Exception someThrowable = new Exception("test");
      WorkRejectedException ne = new WorkRejectedException("someString",
          someThrowable);
      throw ne;
    } catch (WorkRejectedException ex) {
      logAPIPass("WorkRejectedException(str, throwable) passed");
    } catch (Exception ex) {
      Debug.trace("Error verifying WorkRejectedException(str, throwable)");
    }
  }

  /*
   * used to assist with verifying assertions:
   * 
   * 
   */
  private void checkUnavailableException() {

    try {
      UnavailableException ne = new UnavailableException();
      throw ne;
    } catch (UnavailableException ex) {
      logAPIPass("UnavailableException(null) passed");
    } catch (Exception ex) {
      Debug.trace("Error verifying UnavailableException(null)");
    }

    try {
      UnavailableException ne = new UnavailableException("message1");
      throw ne;
    } catch (UnavailableException ex) {
      logAPIPass("UnavailableException(str) passed");
    } catch (Exception ex) {
      Debug.trace("Error verifying UnavailableException(str)");
    }

    try {
      UnavailableException ne = new UnavailableException("message1",
          "ERRCODE1");
      throw ne;
    } catch (UnavailableException ex) {
      logAPIPass("UnavailableException(str, str) passed");
    } catch (Exception ex) {
      Debug.trace("Error verifying UnavailableException(str, str)");
    }

    try {
      Exception someThrowable = new Exception("test");
      UnavailableException ne = new UnavailableException(someThrowable);
      throw ne;
    } catch (UnavailableException ex) {
      logAPIPass("UnavailableException(throwable) passed");
    } catch (Exception ex) {
      Debug.trace("Error verifying UnavailableException(throwable)");
    }

    try {
      Exception someThrowable = new Exception("test");
      UnavailableException ne = new UnavailableException("someString",
          someThrowable);
      throw ne;
    } catch (UnavailableException ex) {
      logAPIPass("UnavailableException(str, throwable) passed");
    } catch (Exception ex) {
      Debug.trace("Error verifying UnavailableException(str, throwable)");
    }
  }

  /*
   * used to assist with verifying assertions:
   * 
   * 
   */
  private void checkSharingViolationException() {

    try {
      SharingViolationException ne = new SharingViolationException();
      throw ne;
    } catch (SharingViolationException ex) {
      logAPIPass("SharingViolationException(null) passed");
    } catch (Exception ex) {
      Debug.trace("Error verifying SharingViolationException(null)");
    }

    try {
      SharingViolationException ne = new SharingViolationException("message1");
      throw ne;
    } catch (SharingViolationException ex) {
      logAPIPass("SharingViolationException(str) passed");
    } catch (Exception ex) {
      Debug.trace("Error verifying SharingViolationException(str)");
    }

    try {
      SharingViolationException ne = new SharingViolationException("message1",
          "ERRCODE1");
      throw ne;
    } catch (SharingViolationException ex) {
      logAPIPass("SharingViolationException(str, str) passed");
    } catch (Exception ex) {
      Debug.trace("Error verifying SharingViolationException(str, str)");
    }

    try {
      Exception someThrowable = new Exception("test");
      SharingViolationException ne = new SharingViolationException(
          someThrowable);
      throw ne;
    } catch (SharingViolationException ex) {
      logAPIPass("SharingViolationException(throwable) passed");
    } catch (Exception ex) {
      Debug.trace("Error verifying SharingViolationException(throwable)");
    }

    try {
      Exception someThrowable = new Exception("test");
      SharingViolationException ne = new SharingViolationException("someString",
          someThrowable);
      throw ne;
    } catch (SharingViolationException ex) {
      logAPIPass("SharingViolationException(str, throwable) passed");
    } catch (Exception ex) {
      Debug.trace("Error verifying SharingViolationException(str, throwable)");
    }
  }

  /*
   * used to assist with verifying assertions:
   * 
   * 
   */
  private void checkSecurityException() {

    try {
      javax.resource.spi.SecurityException ne = new javax.resource.spi.SecurityException();
      throw ne;
    } catch (javax.resource.spi.SecurityException ex) {
      logAPIPass("SecurityException(null) passed");
    } catch (Exception ex) {
      Debug.trace("Error verifying SecurityException(null)");
    }

    try {
      javax.resource.spi.SecurityException ne = new javax.resource.spi.SecurityException(
          "message1");
      throw ne;
    } catch (javax.resource.spi.SecurityException ex) {
      logAPIPass("SecurityException(str) passed");
    } catch (Exception ex) {
      Debug.trace("Error verifying SecurityException(str)");
    }

    try {
      javax.resource.spi.SecurityException ne = new javax.resource.spi.SecurityException(
          "message1", "ERRCODE1");
      throw ne;
    } catch (javax.resource.spi.SecurityException ex) {
      logAPIPass("SecurityException(str, str) passed");
    } catch (Exception ex) {
      Debug.trace("Error verifying SecurityException(str, str)");
    }

    try {
      Exception someThrowable = new Exception("test");
      javax.resource.spi.SecurityException ne = new javax.resource.spi.SecurityException(
          someThrowable);
      throw ne;
    } catch (javax.resource.spi.SecurityException ex) {
      logAPIPass("SecurityException(throwable) passed");
    } catch (Exception ex) {
      Debug.trace("Error verifying SecurityException(throwable)");
    }

    try {
      Exception someThrowable = new Exception("test");
      javax.resource.spi.SecurityException ne = new javax.resource.spi.SecurityException(
          "someString", someThrowable);
      throw ne;
    } catch (javax.resource.spi.SecurityException ex) {
      logAPIPass("SecurityException(str, throwable) passed");
    } catch (Exception ex) {
      Debug.trace("Error verifying SecurityException(str, throwable)");
    }
  }

  /*
   * used to assist with verifying assertions:
   * 
   * 
   */
  private void checkResourceAllocationException() {

    try {
      ResourceAllocationException ne = new ResourceAllocationException();
      throw ne;
    } catch (ResourceAllocationException ex) {
      logAPIPass("ResourceAllocationException(null) passed");
    } catch (Exception ex) {
      Debug.trace("Error verifying ResourceAllocationException(null)");
    }

    try {
      ResourceAllocationException ne = new ResourceAllocationException(
          "message1");
      throw ne;
    } catch (ResourceAllocationException ex) {
      logAPIPass("ResourceAllocationException(str) passed");
    } catch (Exception ex) {
      Debug.trace("Error verifying ResourceAllocationException(str)");
    }

    try {
      ResourceAllocationException ne = new ResourceAllocationException(
          "message1", "ERRCODE1");
      throw ne;
    } catch (ResourceAllocationException ex) {
      logAPIPass("ResourceAllocationException(str, str) passed");
    } catch (Exception ex) {
      Debug.trace("Error verifying ResourceAllocationException(str, str)");
    }

    try {
      Exception someThrowable = new Exception("test");
      ResourceAllocationException ne = new ResourceAllocationException(
          someThrowable);
      throw ne;
    } catch (ResourceAllocationException ex) {
      logAPIPass("ResourceAllocationException(throwable) passed");
    } catch (Exception ex) {
      Debug.trace("Error verifying ResourceAllocationException(throwable)");
    }

    try {
      Exception someThrowable = new Exception("test");
      ResourceAllocationException ne = new ResourceAllocationException(
          "someString", someThrowable);
      throw ne;
    } catch (ResourceAllocationException ex) {
      logAPIPass("ResourceAllocationException(str, throwable) passed");
    } catch (Exception ex) {
      Debug
          .trace("Error verifying ResourceAllocationException(str, throwable)");
    }
  }

  /*
   * used to assist with verifying assertions:
   * 
   * 
   */
  private void checkResourceAdapterInternalException() {

    try {
      ResourceAdapterInternalException ne = new ResourceAdapterInternalException();
      throw ne;
    } catch (ResourceAdapterInternalException ex) {
      logAPIPass("ResourceAdapterInternalException(null) passed");
    } catch (Exception ex) {
      Debug.trace("Error verifying ResourceAdapterInternalException(null)");
    }

    try {
      ResourceAdapterInternalException ne = new ResourceAdapterInternalException(
          "message1");
      throw ne;
    } catch (ResourceAdapterInternalException ex) {
      logAPIPass("ResourceAdapterInternalException(str) passed");
    } catch (Exception ex) {
      Debug.trace("Error verifying ResourceAdapterInternalException(str)");
    }

    try {
      ResourceAdapterInternalException ne = new ResourceAdapterInternalException(
          "message1", "ERRCODE1");
      throw ne;
    } catch (ResourceAdapterInternalException ex) {
      logAPIPass("ResourceAdapterInternalException(str, str) passed");
    } catch (Exception ex) {
      Debug.trace("Error verifying ResourceAdapterInternalException(str, str)");
    }

    try {
      Exception someThrowable = new Exception("test");
      ResourceAdapterInternalException ne = new ResourceAdapterInternalException(
          someThrowable);
      throw ne;
    } catch (ResourceAdapterInternalException ex) {
      logAPIPass("ResourceAdapterInternalException(throwable) passed");
    } catch (Exception ex) {
      Debug
          .trace("Error verifying ResourceAdapterInternalException(throwable)");
    }

    try {
      Exception someThrowable = new Exception("test");
      ResourceAdapterInternalException ne = new ResourceAdapterInternalException(
          "someString", someThrowable);
      throw ne;
    } catch (ResourceAdapterInternalException ex) {
      logAPIPass("ResourceAdapterInternalException(str, throwable) passed");
    } catch (Exception ex) {
      Debug.trace(
          "Error verifying ResourceAdapterInternalException(str, throwable)");
    }
  }

  /*
   * used to assist with verifying assertions:
   * 
   * 
   */
  private void checkLocalTransactionException() {

    try {
      LocalTransactionException ne = new LocalTransactionException();
      throw ne;
    } catch (LocalTransactionException ex) {
      logAPIPass("LocalTransactionException(null) passed");
    } catch (Exception ex) {
      Debug.trace("Error verifying LocalTransactionException(null)");
    }

    try {
      LocalTransactionException ne = new LocalTransactionException("message1");
      throw ne;
    } catch (LocalTransactionException ex) {
      logAPIPass("LocalTransactionException(str) passed");
    } catch (Exception ex) {
      Debug.trace("Error verifying LocalTransactionException(str)");
    }

    try {
      LocalTransactionException ne = new LocalTransactionException("message1",
          "ERRCODE1");
      throw ne;
    } catch (LocalTransactionException ex) {
      logAPIPass("LocalTransactionException(str, str) passed");
    } catch (Exception ex) {
      Debug.trace("Error verifying LocalTransactionException(str, str)");
    }

    try {
      Exception someThrowable = new Exception("test");
      LocalTransactionException ne = new LocalTransactionException(
          someThrowable);
      throw ne;
    } catch (LocalTransactionException ex) {
      logAPIPass("LocalTransactionException(throwable) passed");
    } catch (Exception ex) {
      Debug.trace("Error verifying LocalTransactionException(throwable)");
    }

    try {
      Exception someThrowable = new Exception("test");
      LocalTransactionException ne = new LocalTransactionException("someString",
          someThrowable);
      throw ne;
    } catch (LocalTransactionException ex) {
      logAPIPass("LocalTransactionException(str, throwable) passed");
    } catch (Exception ex) {
      Debug.trace("Error verifying LocalTransactionException(str, throwable)");
    }
  }

  /*
   * used to assist with verifying assertions: Connector:JAVADOC:1,
   * Connector:JAVADOC:2, Connector:JAVADOC:3, Connector:JAVADOC:4,
   * Connector:JAVADOC:5
   */
  private void checkNotSupportedException() {

    try {
      NotSupportedException ne = new NotSupportedException();
      throw ne;
    } catch (NotSupportedException ex) {
      logAPIPass("NotSupportedException(null) passed");
    } catch (Exception ex) {
      Debug.trace("Error verifying NotSupportedException(null)");
    }

    try {
      NotSupportedException ne = new NotSupportedException("message1");
      throw ne;
    } catch (NotSupportedException ex) {
      logAPIPass("NotSupportedException(str) passed");
    } catch (Exception ex) {
      Debug.trace("Error verifying NotSupportedException(str)");
    }

    try {
      NotSupportedException ne = new NotSupportedException("message1",
          "ERRCODE1");
      throw ne;
    } catch (NotSupportedException ex) {
      logAPIPass("NotSupportedException(str, str) passed");
    } catch (Exception ex) {
      Debug.trace("Error verifying NotSupportedException(str, str)");
    }

    try {
      Exception someThrowable = new Exception("test");
      NotSupportedException ne = new NotSupportedException(someThrowable);
      throw ne;
    } catch (NotSupportedException ex) {
      logAPIPass("NotSupportedException(throwable) passed");
    } catch (Exception ex) {
      Debug.trace("Error verifying NotSupportedException(throwable)");
    }

    try {
      Exception someThrowable = new Exception("test");
      NotSupportedException ne = new NotSupportedException("someString",
          someThrowable);
      throw ne;
    } catch (NotSupportedException ex) {
      logAPIPass("NotSupportedException(str, throwable) passed");
    } catch (Exception ex) {
      Debug.trace("Error verifying NotSupportedException(str, throwable)");
    }
  }

  /*
   * used to assist with testing api assertions: Connector:JAVADOC:7,
   * Connector:JAVADOC:9, Connector:JAVADOC:10, Connector:JAVADOC:11,
   * Connector:JAVADOC:12, Connector:JAVADOC:13, Connector:JAVADOC:14,
   * Connector:JAVADOC:15
   */
  private void checkResourceException() {

    try {
      ResourceException ne = new ResourceException();
      throw ne;
    } catch (ResourceException ex) {
      logAPIPass("ResourceException(null) passed");
    } catch (Exception ex) {
      Debug.trace("Error verifying ResourceException(null)");
    }

    try {
      ResourceException ne = new ResourceException("message1");
      ne.setErrorCode("ERRCODE2");
      throw ne;
    } catch (ResourceException ex) {
      logAPIPass("ResourceException(str) passed");
      if ((ex.getErrorCode() != null)) {
        String str = ex.getErrorCode().toUpperCase();
        if (str.indexOf("ERRCODE2") != -1) {
          logAPIPass("ResourceException.setErrorCode(str) passed");
        }
      } else {
        Debug.trace("Error verifying ResourceException(str, str)");
      }

    } catch (Exception ex) {
      Debug.trace("Error verifying ResourceException(str)");
    }

    try {
      ResourceException ne = new ResourceException("message1", "ERRCODE1");
      throw ne;
    } catch (ResourceException ex) {
      logAPIPass("ResourceException(str, str) passed");
      if ((ex.getErrorCode() != null)) {
        String str = ex.getErrorCode().toUpperCase();
        if (str.indexOf("ERRCODE1") != -1) {
          logAPIPass("ResourceException.getErrorCode() passed");
        }
      } else {
        Debug.trace("Error verifying ResourceException(str, str)");
      }
    } catch (Exception ex) {
      Debug.trace("Error verifying ResourceException(str, str)");
    }

    try {
      Exception someThrowable = new Exception("test");
      ResourceException ne = new ResourceException(someThrowable);
      throw ne;
    } catch (ResourceException ex) {
      logAPIPass("ResourceException(throwable) passed");
    } catch (Exception ex) {
      Debug.trace("Error verifying ResourceException(throwable)");
    }

    try {
      Exception someThrowable = new Exception("test");
      ResourceException ne = new ResourceException("someString", someThrowable);
      throw ne;
    } catch (ResourceException ex) {
      logAPIPass("ResourceException(str, someThrowable) passed");
      if (ex.getMessage() != null) {
        logAPIPass("ResourceException.getMessage() passed");
      } else {
        Debug.trace("Error verifying ResourceException(str, someThrowable)");
      }

    } catch (Exception ex) {
      Debug.trace("Error verifying ResourceException(str, throwable)");
    }
  }

  private static void logAPIPass(String outStr) {
    ConnectorStatus.getConnectorStatus().logState(outStr);
    Debug.trace(outStr);
  }

}
