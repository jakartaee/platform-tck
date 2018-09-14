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

package com.sun.ts.tests.ejb30.tx.common.web;

import com.sun.ts.tests.servlet.common.servlets.HttpTCKServlet;
import com.sun.ts.tests.servlet.common.util.Data;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.PrintWriter;
import static com.sun.ts.tests.ejb30.tx.common.web.Constants.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.NotSupportedException;
import static javax.transaction.Status.*;
import javax.transaction.Status;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

public class TestServlet extends HttpTCKServlet {
  private void doTest(HttpServletRequest request, HttpServletResponse response,
      String servletPath, Integer expectedTxStatus, Integer unexpectedTxStatus,
      boolean startTx) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    try {
      if (startTx) {
        getUserTransaction().begin();
      }
      RequestDispatcher disp = getServletContext()
          .getRequestDispatcher(servletPath);
      disp.include(request, response);
      int status = getUserTransaction().getStatus();
      if (expectedTxStatus == null && unexpectedTxStatus == null) {
        // don't care about tx status
        pw.println(Data.PASSED + ", not checking tx status. The actual tx "
            + "status is " + status);
      } else if (unexpectedTxStatus != null) {// use unexpectedTxStatus
        if (status == unexpectedTxStatus) {
          pw.println(Data.FAILED + " got unexpected status " + status);
        } else {
          pw.println(Data.PASSED + " got transaction status  " + status);
        }
      } else {// use expectedTxStatus
        if (status == expectedTxStatus) {
          pw.println(
              Data.PASSED + " got expected transaction status  " + status);
        } else {
          pw.println(Data.FAILED + " expected tx status is " + expectedTxStatus
              + ", but actual status is " + status);
        }
      }
    } catch (NamingException e) {
      throw new ServletException(e);
    } catch (SystemException e) {
      throw new ServletException(e);
    } catch (NotSupportedException e) {
      throw new ServletException(e);
    } finally {
      if (startTx) {
        try {
          getUserTransaction().rollback();
        } catch (Exception e) {
          // ignore
        }
      }
    }
  }

  public void newThreadNoTx(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    final Boolean[] newThreadNoTxStatus = new Boolean[1];
    try {
      getUserTransaction().begin();
      Thread thread = new Thread() {
        public void run() {
          try {
            int status = getUserTransaction().getStatus();
            if (status == Status.STATUS_NO_TRANSACTION) {
              // good
              System.out.println("New thread spawned by TestServlet"
                  + " has not transaction, as expected");
              newThreadNoTxStatus[0] = true;
            } else {
              newThreadNoTxStatus[0] = false;
              throw new IllegalStateException("In new thread expecting"
                  + " no transaction, but got " + status);
            }
          } catch (NamingException e) {
            throw new IllegalStateException(e);
          } catch (SystemException e) {
            throw new IllegalStateException(e);
          }
        }
      };
      thread.setName(newThreadNoTx);
      thread.start();
      try {
        thread.join(10 * 1000);
      } catch (InterruptedException e) {
        // ignore
      }
      for (int i = 0; i < 99 && newThreadNoTxStatus[0] == null; i++) {
        try {
          Thread.sleep(1000);
        } catch (InterruptedException e) {
          // ignore
        }
      }
      String msg = null;
      if (newThreadNoTxStatus[0] == null) {
        msg = Data.FAILED + ", timed out, didn't see update from new thread.";
      } else if (newThreadNoTxStatus[0]) {
        msg = Data.PASSED + ", no IllegalStateException from new thread.";
      } else {
        msg = Data.FAILED + ", got IllegalStateException from new thread.";
      }
      PrintWriter pw = response.getWriter();
      pw.println(msg);
    } catch (NamingException e) {
      throw new ServletException(e);
    } catch (SystemException e) {
      throw new ServletException(e);
    } catch (NotSupportedException e) {
      throw new ServletException(e);
    } finally {
      try {
        getUserTransaction().rollback();
      } catch (Exception e) {
        // ignore
      }
    }
  }

  // remote bean setRollbackOnly
  public void servletRemoteCmt(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    Integer expected = null;
    Integer unexpectedTxStatus = Status.STATUS_ACTIVE;
    doTest(request, response, TX_SERVLET_PATH, expected, unexpectedTxStatus,
        false);
  }

  // local bean setRollbackOnly
  public void servletLocalCmt(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    Integer expected = null;
    Integer unexpectedTxStatus = Status.STATUS_ACTIVE;
    doTest(request, response, TX_SERVLET_PATH, expected, unexpectedTxStatus,
        false);
  }

  public void servletRemoteCmtRequiresNew(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    Integer expected = Status.STATUS_ACTIVE;
    Integer unexpectedTxStatus = null;
    doTest(request, response, TX_SERVLET_PATH, expected, unexpectedTxStatus,
        true);
  }

  public void servletLocalCmtRequiresNew(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    Integer expected = Status.STATUS_ACTIVE;
    Integer unexpectedTxStatus = null;
    doTest(request, response, TX_SERVLET_PATH, expected, unexpectedTxStatus,
        true);
  }

  public void servletRemoteCmtMandatory(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    Integer expected = null;
    Integer unexpectedTxStatus = Status.STATUS_ACTIVE;
    doTest(request, response, TX_SERVLET_PATH, expected, unexpectedTxStatus,
        false);
  }

  public void servletLocalCmtMandatory(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    Integer expected = null;
    Integer unexpectedTxStatus = Status.STATUS_ACTIVE;
    doTest(request, response, TX_SERVLET_PATH, expected, unexpectedTxStatus,
        false);
  }

  public void servletRemoteCmtNever(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    Integer expected = Status.STATUS_ACTIVE;
    Integer unexpectedTxStatus = null;
    doTest(request, response, TX_SERVLET_PATH, expected, unexpectedTxStatus,
        true);
  }

  public void servletLocalCmtNever(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    Integer expected = Status.STATUS_ACTIVE;
    Integer unexpectedTxStatus = null;
    doTest(request, response, TX_SERVLET_PATH, expected, unexpectedTxStatus,
        true);
  }

  public void servletTxTerminate(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    Integer expected = null;
    Integer unexpectedTxStatus = null;
    // loop in client so they are really separate http requests
    doTest(request, response, FOO_SERVLET_PATH, expected, unexpectedTxStatus,
        false);
  }

  public void interServletTxPropagation(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    Integer expected = Status.STATUS_ACTIVE;
    Integer unexpectedTxStatus = null;
    // for(int i = 0; i < LOOP_COUNT; i++) {
    doTest(request, response, TX_SERVLET_PATH, expected, unexpectedTxStatus,
        true);
    // }
  }

  public void interServletTxPropagation2(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    Integer expected = null;
    Integer unexpectedTxStatus = Status.STATUS_ACTIVE;
    // for(int i = 0; i < LOOP_COUNT; i++) {
    doTest(request, response, TX_SERVLET_PATH, expected, unexpectedTxStatus,
        true);
    // }
  }

  public static UserTransaction getUserTransaction() throws NamingException {
    InitialContext ic = new InitialContext();
    Object obj = ic.lookup("java:comp/UserTransaction");
    return (UserTransaction) obj;
  }

}
