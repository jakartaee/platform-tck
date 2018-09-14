<%--

    Copyright (c) 2003, 2018 Oracle and/or its affiliates. All rights reserved.

    This program and the accompanying materials are made available under the
    terms of the Eclipse Public License v. 2.0, which is available at
    http://www.eclipse.org/legal/epl-2.0.

    This Source Code may also be made available under the following Secondary
    Licenses when the conditions for such availability set forth in the
    Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
    version 2 with the GNU Classpath Exception, which is available at
    https://www.gnu.org/software/classpath/license.html.

    SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0

--%>

<%@ page import="com.sun.ts.tests.jsp.common.util.JspTestUtil,
                 java.io.IOException,
                 javax.servlet.jsp.el.ELException,
                 java.io.PrintWriter" %>
<%@ page contentType="text/plain" %>

<%-- Begin test definitions --%>

<%!
    public void elExceptionDefaultCtorTest(HttpServletRequest req,
                                            HttpServletResponse res,
                                            JspWriter out)
    throws ServletException, IOException {
        ELException ee = new ELException();
        if (ee != null) {
            out.println("Test PASSED");
        } else {
            out.println("Test FAILED.  No Exception created.");
        }
    }
%>

<%!
    public void elExceptionMessageCtorTest(HttpServletRequest req,
                                            HttpServletResponse res,
                                            JspWriter out)
    throws ServletException, IOException {
        ELException ee = new ELException("Exception Message");
        if (ee != null) {
            String message = ee.getMessage();
            if (message != null && message.equals("Exception Message")) {
                out.println("Test PASSED");
            } else {
                out.println("Test FAILED.  Expected a message of 'Exception Message'");
                out.println("Received: " + message);
            }
        } else {
            out.println("Test FAILED.  No Exception created.");
        }
    }
%>

<%!
    public void elExceptionCauseCtorTest(HttpServletRequest req,
                                          HttpServletResponse res,
                                          JspWriter out)
    throws ServletException, IOException {
        ELException ee = new ELException(new NullPointerException());
        if (ee != null) {
            Throwable t = ee.getRootCause();
            if (t != null && t instanceof NullPointerException) {
                out.println("Test PASSED");
            } else {
                out.println("Test FAILED.  Expected a Throwable of type NullPointerException");
                out.println("Received: " + t);
            }
        } else {
            out.println("Test FAILED.  No Exception created.");
        }
    }
%>

<%!
    public void elExceptionCauseMessageCtorTest(HttpServletRequest req,
                                                 HttpServletResponse res,
                                                 JspWriter out)
    throws ServletException, IOException {
        ELException ee = new ELException("Exception Message",
                                                      new ServletException());
        if (ee != null) {
            String message = ee.getMessage();
            if (message != null && message.equals("Exception Message")) {
                Throwable t = ee.getRootCause();
                if (t != null && t instanceof ServletException) {
                    out.println("Test PASSED");
                } else {
                    out.println("Test FAILED.  Expected a Throwable of type ServletException");
                    out.println("Received: " + t);
                }
            } else {
                out.println("Test FAILED.  Expected a message of 'Exception Message'");
                out.println("Received: " + message);
            }
        } else {
            out.println("Test FAILED.  No Exception created.");
        }
    }
%>

<%!
    public void elExceptionGetRootCauseTest(HttpServletRequest req,
                                             HttpServletResponse res,
                                             JspWriter out)
    throws ServletException, IOException {
        ELException ee = new ELException(new ServletException());
        ELException ee1 = new ELException("exception", new NullPointerException());

        Throwable t = ee.getRootCause();
        Throwable t1 = ee1.getRootCause();
        if (t != null) {
            if (t instanceof ServletException) {
                if (t1 != null) {
                    if (t1 instanceof NullPointerException) {
                        out.println("Test PASSED");
                    } else {
                        out.println("Test FAILED.  ELException.getRootCause returned unexpected" +
                            " exception: " + t.getClass().getName());
                        out.println("Expected: NullPointerException");
                    }
                } else {
                    out.println("Test FAILED.  ELException.getRootCause returned null.");
                }
            } else {
                out.println("Test FAILED.  ELException.getRootCause returned unexpected" +
                    " exception: " + t.getClass().getName());
                out.println("Expected: ServletException");
            }
        } else {
            out.println("Test FAILED.  ELException.getRootCause returned null.");
        }
    }
%>

<%!
    public void elExceptionToStringTest(HttpServletRequest req,
                                        HttpServletResponse res,
                                        JspWriter out)
    throws ServletException, IOException {
        ELException ee = new ELException();
        String str = ee.toString();
        if (str != null) {
            out.println("Test PASSED");
        } else {
            out.println("Test FAILED.  Result of ELException.toString() was null.");
        }
    }
%>

<%-- Test invocation --%>

<% JspTestUtil.invokeTest(this, request, response, out); %>
