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
                 java.io.IOException" %>


<%@ page contentType="text/plain" %>

<%!
    private static final String APPLICATIONCTX_ATTR =
        "com.sun.ts.tests.jsp.api.applicationContext";
%>

<%-- Begin test definitions --%>

<%!
    public void jspWriterNewLineTest(HttpServletRequest req,
                                     HttpServletResponse res,
                                     JspWriter out)
    throws ServletException, IOException {
        out.print("new line");
        out.newLine();
    }
%>

<%!
    public void jspWriterPrintBooleanTest(HttpServletRequest req,
                                          HttpServletResponse res,
                                          JspWriter out)
    throws ServletException, IOException {
        out.print(true);
        out.print(false);
    }
%>

<%!
    public void jspWriterPrintCharTest(HttpServletRequest req,
                                       HttpServletResponse res,
                                       JspWriter out)
    throws ServletException, IOException {
        out.print('c');
        out.print('b');
    }
%>

<%!
    public void jspWriterPrintIntTest(HttpServletRequest req,
                                      HttpServletResponse res,
                                      JspWriter out)
    throws ServletException, IOException {
        out.print(Integer.MIN_VALUE);
        out.print(Integer.MAX_VALUE);
    }
%>

<%!
    public void jspWriterPrintLongTest(HttpServletRequest req,
                                       HttpServletResponse res,
                                       JspWriter out)
    throws ServletException, IOException {
        out.print(Long.MIN_VALUE);
        out.print(Long.MAX_VALUE);
    }
%>

<%!
    public void jspWriterPrintFloatTest(HttpServletRequest req,
                                        HttpServletResponse res,
                                        JspWriter out)
    throws ServletException, IOException {
        out.print(Float.MIN_VALUE);
        out.print(Float.MAX_VALUE);
    }
%>

<%!
    public void jspWriterPrintDoubleTest(HttpServletRequest req,
                                         HttpServletResponse res,
                                         JspWriter out)
    throws ServletException, IOException {
        out.print(Double.MIN_VALUE);
        out.print(Double.MAX_VALUE);
    }
%>

<%!
    public void jspWriterPrintCharArrayTest(HttpServletRequest req,
                                            HttpServletResponse res,
                                            JspWriter out)
    throws ServletException, IOException {
        char[] c = new char[89];
        for (short i = 33, idx = 0; i < 90; i++, idx++) {
            c[idx] = (char) i;
        }
        out.print(c);
        out.print(c);
    }
%>

<%!
    public void jspWriterPrintStringTest(HttpServletRequest req,
                                         HttpServletResponse res,
                                         JspWriter out)
    throws ServletException, IOException {
        out.print("Test ");
        out.print("Passed");
    }
%>

<%!
    public void jspWriterPrintNullStringTest(HttpServletRequest req,
                                         HttpServletResponse res,
                                         JspWriter out)
    throws ServletException, IOException {
        String nullString = null;
        out.print(nullString);
    }
%>

<%!
    public void jspWriterPrintObjectTest(HttpServletRequest req,
                                         HttpServletResponse res,
                                         JspWriter out)
    throws ServletException, IOException {
        Object o1 = new String("Test ");
        Object o2 = new String("Passed");
        out.print(o1);
        out.print(o2);
    }
%>

<%!
    public void jspWriterPrintlnTest(HttpServletRequest req,
                                     HttpServletResponse res,
                                     JspWriter out)
    throws ServletException, IOException {
        out.print("Test Passed");
        out.println();
    }
%>

<%!
    public void jspWriterPrintlnBooleanTest(HttpServletRequest req,
                                            HttpServletResponse res,
                                            JspWriter out)
    throws ServletException, IOException {
        out.println(true);
        out.println(false);
    }
%>

<%!
    public void jspWriterPrintlnCharTest(HttpServletRequest req,
                                         HttpServletResponse res,
                                         JspWriter out)
    throws ServletException, IOException {
        out.println('a');
        out.println('B');
    }
%>

<%!
    public void jspWriterPrintlnIntTest(HttpServletRequest req,
                                        HttpServletResponse res,
                                        JspWriter out)
    throws ServletException, IOException {
        out.println(Integer.MIN_VALUE);
        out.println(Integer.MAX_VALUE);
    }
%>

<%!
    public void jspWriterPrintlnLongTest(HttpServletRequest req,
                                         HttpServletResponse res,
                                         JspWriter out)
    throws ServletException, IOException {
        out.println(Long.MIN_VALUE);
        out.println(Long.MAX_VALUE);
    }
%>

<%!
    public void jspWriterPrintlnFloatTest(HttpServletRequest req,
                                          HttpServletResponse res,
                                          JspWriter out)
    throws ServletException, IOException {
        out.println(Float.MIN_VALUE);
        out.println(Float.MAX_VALUE);
    }
%>

<%!
    public void jspWriterPrintlnDoubleTest(HttpServletRequest req,
                                           HttpServletResponse res,
                                           JspWriter out)
    throws ServletException, IOException {
        out.println(Double.MIN_VALUE);
        out.println(Double.MAX_VALUE);
    }
%>

<%!
    public void jspWriterPrintlnCharArrayTest(HttpServletRequest req,
                                              HttpServletResponse res,
                                              JspWriter out)
    throws ServletException, IOException {
        char[] c = new char[89];
        for (short i = 33, idx = 0; i < 90; i++, idx++) {
            c[idx] = (char) i;
        }
        out.println(c);
        out.println(c);
    }
%>

<%!
    public void jspWriterPrintlnStringTest(HttpServletRequest req,
                                           HttpServletResponse res,
                                           JspWriter out)
    throws ServletException, IOException {
        out.println("Test ");
        out.println("Passed");
    }
%>

<%!
    public void jspWriterPrintlnNullStringTest(HttpServletRequest req,
                                         HttpServletResponse res,
                                         JspWriter out)
    throws ServletException, IOException {
        String nullString = null;
        out.println(nullString);
    }
%>

<%!
    public void jspWriterPrintlnObjectTest(HttpServletRequest req,
                                           HttpServletResponse res,
                                           JspWriter out)
    throws ServletException, IOException {
        Object o1 = new String("Test ");
        Object o2 = new String("Passed");
        out.println(o1);
        out.println(o2);
    }
%>

<%!
    public void jspWriterClearTest(HttpServletRequest req,
                                   HttpServletResponse res,
                                   JspWriter out)
    throws ServletException, IOException {
        if (out.getBufferSize() > 0) {
            out.println("Test FAILED.  JspWriter not cleared.");
            out.clear();
            out.println("Test PASSED");
        } else {
            out.println("Test PASSED");
        }
    }
%>

<%!
    public void jspWriterClearIOExceptionTest(HttpServletRequest req,
                                              HttpServletResponse res,
                                              JspWriter out)
    throws ServletException, IOException {
        if (out.getBufferSize() > 0) {
            out.println("Arbitrary text");
            out.flush();
            try {
                out.clear();
            } catch (Throwable t) {
                if (t instanceof IOException) {
                    out.println("Test PASSED");
                } else {
                    out.println("Test FAILED.  JspWriter.clear() did throw" +
                        "an Exception, but was not an IOException as expected.");
                    out.println("Received:" + t.toString());
                }
            }
        } else {
            out.println("Test PASSED");
        }
    }
%>

<%!
    public void jspWriterClearBufferTest(HttpServletRequest req,
                                         HttpServletResponse res,
                                         JspWriter out)
    throws ServletException, IOException {
        if (out.getBufferSize() > 0) {
            out.println("Arbitrary text");
            out.flush();
            out.println("Test FAILED.  JspWriter not cleared.");
            try {
                out.clearBuffer();
            } catch (Throwable t) {
                if (t instanceof IOException) {
                    out.println("Test FAILED.  IOException shouldn't have been" +
                        "thrown by clearBuffer when called after committing the response.");
                    return;
                } else {
                    out.println("Test FAILED.  Unexpeced Exception.");
                    out.println(t.toString());
                }
            }
            out.println("Test PASSED");
        } else {
            out.println("Test PASSED");
        }
    }
%>

<%!
    // XXX consider alternate implementation
    public void jspWriterFlushTest(HttpServletRequest req,
                                   HttpServletResponse res,
                                   JspWriter out)
    throws ServletException, IOException {
        if (out.getBufferSize() > 0) {
            out.println("Arbitrary test");
            out.flush();
            try {
                out.clear();
            } catch (Throwable t) {
                if (t instanceof IOException) {
                    out.println("Test PASSED");
                } else {
                    out.println("Test FAILED.  Flush of the stream failed.");
                }
            }
        } else {
            out.println("Test PASSED");
        }
    }
%>

<%!
    public void jspWriterCloseTest(HttpServletRequest req,
                                   HttpServletResponse res,
                                   JspWriter out)
    throws ServletException, IOException {
        ServletContext ctx = (ServletContext) req.getAttribute(APPLICATIONCTX_ATTR);
        if (ctx != null) {
            out.close();
            try {
                out.flush();
            } catch (Throwable t) {
                if (t instanceof IOException) {
                    ctx.setAttribute("flush.exception", t);
                }
            }

            try {
                out.println();
            } catch (Throwable t) {
                if (t instanceof IOException) {
                    ctx.setAttribute("write.exception", t);
                }
            }

            try {
                out.close();
            } catch (Throwable t) {
                ctx.setAttribute("close.exception", t);
            }
        } else {
            out.println("Test FAILED.  Unable to access ServletContext.");
        }
    }
%>

<%!
    public void jspWriterGetBufferSizeTest(HttpServletRequest req,
                                           HttpServletResponse res,
                                           JspWriter out)
    throws ServletException, IOException {
        int size = out.getBufferSize();
        if (size > 0) {
            if (size >= 8192) {
                out.println("Test PASSED");
            } else {
                out.println("Test FAILED.  Buffer allocated is not of the expected size.");
                out.println("Expected a buffer size greater than or equal to 8192KB");
                out.println("Actual size:" + size);
            }
        } else {
            out.println("Test PASSED");
        }
    }
%>

<%!
    public void jspWriterGetRemainingTest(HttpServletRequest req,
                                          HttpServletResponse res,
                                          JspWriter out)
    throws ServletException, IOException {
        String message = "Arbitrary text";
        int messLength = message.length();
        int size = out.getBufferSize();

        if (size > 0) {
            out.clear();
            int gr1 = out.getRemaining();
            if (size == gr1) {
                out.print("Arbitrary text");
                int gr2 = out.getRemaining();
                if (gr2 == (size - messLength)) {
                    out.clear();
                    int gr3 = out.getRemaining();
                    if (size == gr3) {
                        out.println("Test PASSED");
                    } else {
                        out.println("Test FAILED.  (l3) Unexpected result from JspWriter.getRemaining().");
                        out.println("Expected: " + size);
                        out.println("Received: " + gr3);
                    }
                } else {
                    out.println("Test FAILED.  (l2) Unexpected result from JspWriter.getRemaining().");
                    out.println("Expected: " + (size - messLength));
                    out.println("Received: " + gr2);
                }
            } else {
                out.println("Test FAILED.  (l1) Unexpteced result from JspWriter.getRemaining().");
                out.println("Expected: " + size);
                out.println("Received: " + gr1);
            }
        } else {
            out.println("Test PASSED");
        }
    }
%>

<%!
    public void jspWriterIsAutoFlushTest(HttpServletRequest req,
                                         HttpServletResponse res,
                                         JspWriter out)
    throws ServletException, IOException {
        if (out.isAutoFlush() == true) {
            out.println("Test PASSED");
        } else {
            out.println("Test FAILED.  Expected JspWriter.isAutoFlush() to return true not false.");
        }
    }
%>

<%-- Test invocation --%>

<%
    request.setAttribute(APPLICATIONCTX_ATTR, application);

    if (System.getProperty("line.separator").equals("\n")) {
        response.setHeader("Server-EOL", "UNIX");
    } else if (System.getProperty("line.separator").equals("\r\n")) {
        response.setHeader("Server-EOL", "WIN32");
    }

    JspTestUtil.invokeTest(this, request, response, out);
%>
