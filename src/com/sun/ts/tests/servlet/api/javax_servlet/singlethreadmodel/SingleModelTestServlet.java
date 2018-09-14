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

/*
 * $Id$
 */

package com.sun.ts.tests.servlet.api.javax_servlet.singlethreadmodel;

import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.SingleThreadModel;
import java.io.IOException;

/**
 * A test for SingleThreadModel interface. The SingleThreadModel interface
 * guarantees that <b>only</b> a single thread shall execute a servlet's
 * <code>service</code> method when such a servlet implements this interface.
 *
 * @version $URL$
 */

public class SingleModelTestServlet extends GenericServlet
    implements SingleThreadModel {

  /*
   * <code>threadCount</code> Indicates the number of concurrent threads.
   */
  private int threadCount = 0;

  /*
   * <code>sb</code> is a common StringBuffer for Exception reporting.
   */
  private StringBuffer sb = null;

  /**
   * Called by the servlet container to allow the servlet to respond to a
   * request.
   *
   * @param <code>ServletRequest</code>
   *          The current ServletRequest
   * @param <code>ServletResponse</code>
   *          The current ServletResponse
   * @exception ServletException
   *              if an error occurs
   * @exception IOException
   *              if an error occurs
   */
  public void service(ServletRequest request, ServletResponse response)
      throws ServletException, IOException {

    /*
     * The threadCount variable should be at 0 each time service() is invoked by
     * the container. If a request enters, and it's not zero, fail the test by
     * throwing a ServletException
     */
    try {
      assertThreadCount(0);
      threadCount++;
    } catch (Throwable t) {
      sb = new StringBuffer(100);
      sb.append(
          "Thread counter was not 0 upon entering the service() method\n");
      sb.append("The value found was: ");
      sb.append(threadCount);
      throw new ServletException(sb.toString());
    } finally {
      sb = null;
    }

    /*
     * threadCount has now been incremented, loop for a period of time and
     * assert that threadCount never changes. After the loop completes,
     * decrement threadCount and return.
     */
    try {
      for (int i = 0; i < 200000; i++) {
        assertThreadCount(1);
      }
    } catch (Throwable t) {
      sb = new StringBuffer(75);
      sb.append("Thread count changed during processing!\n");
      sb.append("Expected a value of 1, but found: ");
      sb.append(threadCount);
      throw new ServletException(sb.toString());
    } finally {
      sb = null;
      threadCount--;
    }
  }

  /*
   * <code>assertThreadCount</code> asserts that the instance variable,
   * threadCount, is the same value as that passed into the method.
   *
   * @param val Expected value for threadCount
   * 
   * @exception MultipleThreadException if the value of threadCount is not equal
   * to the value passed.
   */
  private void assertThreadCount(int val) throws MultipleThreadException {
    if (threadCount != val) {
      throw new MultipleThreadException();
    }
  }

  /*
   * <code>MultipleThreadException</code> Indicated multiple threads are
   * currently running within the same instance.
   */
  private class MultipleThreadException extends java.lang.Exception {

    /*
     * Creates a new <code>MultipleThreadException</code> instance.
     *
     */
    public MultipleThreadException() {
      super();
    }
  }
}
