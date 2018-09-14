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
                 javax.servlet.jsp.tagext.BodyContent,
                 java.util.Enumeration,
                 javax.servlet.jsp.el.ExpressionEvaluator,
                 javax.servlet.jsp.el.VariableResolver" %>
<%@ page contentType="text/plain" errorPage="ErrorPage.jsp"%>

<%!
    private static final String PAGECONTEXT_ATTR =
        "com.sun.ts.tests.jsp.api.pagecontext";
    private static final String SESSION_ATTR =
        "com.sun.ts.tests.jsp.api.session";
    private static final String PAGE_ATTR =
        "com.sun.ts.tests.jsp.api.page";
    private static final String CONFIG_ATTR =
        "com.sun.ts.tests.jsp.api.config";
    private static final String CONTEXT_ATTR =
        "com.sun.ts.tests.jsp.api.context";
%>

<%-- Utility methods --%>
<%!
    public static void cleanup(PageContext pc) {
        pc.removeAttribute("pageScope", PageContext.PAGE_SCOPE);
        pc.removeAttribute("requestScope", PageContext.REQUEST_SCOPE);
        pc.removeAttribute("sessionScope", PageContext.SESSION_SCOPE);
        pc.removeAttribute("applicationScope", PageContext.APPLICATION_SCOPE);
    }
%>

<%!
    public static void fillAllScopes(PageContext pc) {
        pc.setAttribute("pageScope", "pageScope");
        pc.setAttribute("requestScope", "requestScope",
            PageContext.REQUEST_SCOPE);
        pc.setAttribute("sessionScope", "sessionScope",
            PageContext.SESSION_SCOPE);
        pc.setAttribute("applicationScope", "applicationScope",
            PageContext.APPLICATION_SCOPE);
    }
%>

<%!
    public static boolean checkValue(Enumeration e, String name, JspWriter out)
    throws IOException {
        out.println("Checking for attribute '" + name + "'");
        while (e.hasMoreElements()) {
            String ele = (String) e.nextElement();
            out.println("Element returned: " + ele);
            if (ele.equals(name)) {
                out.println("Match found!\n");
                return true;
            }
        }
        out.println("No match found.\n");
        return false;
    }
%>

<%-- Begin test definitions --%>

<%!
    public void pageContextGetSessionTest(HttpServletRequest req,
                                          HttpServletResponse res,
                                          JspWriter out)
    throws ServletException, IOException {
        PageContext pc = (PageContext) req.getAttribute(PAGECONTEXT_ATTR);
        if (pc != null) {
            HttpSession sess = pc.getSession();
            if (sess != null) {
                if (sess == req.getAttribute(SESSION_ATTR)) {
                    out.println("Test PASSED");
                } else {
                    out.println("Test FAILED.  Session returned from " +
                        "PageContext.getSession() is not the same as that " +
                        "associated with the session scripting variable.");
                }
            } else {
                out.println("Test FAILED.  PageContext.getSession() returned null.");
            }
        } else {
            out.println("Test FAILED.  No PageContext object available.");
        }
    }
%>

<%!
    public void pageContextGetPageTest(HttpServletRequest req,
                                       HttpServletResponse res,
                                       JspWriter out)
    throws ServletException, IOException {
        PageContext pc = (PageContext) req.getAttribute(PAGECONTEXT_ATTR);
        if (pc != null) {
            Object o = pc.getPage();
            if (o != null) {
                if (o == req.getAttribute(PAGE_ATTR)) {
                    out.println("Test PASSED");
                } else {
                    out.println("Test FAILED.  Page object returned from " +
                        "PageContext.getPage() is not the same as that " +
                        "associated with the page scripting variable.");
                }
            } else {
                out.println("Test FAILED.  PageContext.getPage() returned null.");
            }
        } else {
            out.println("Test FAILED.  No PageContext object available.");
        }
    }
%>

<%!
    public void pageContextGetRequestTest(HttpServletRequest req,
                                       HttpServletResponse res,
                                       JspWriter out)
    throws ServletException, IOException {
        PageContext pc = (PageContext) req.getAttribute(PAGECONTEXT_ATTR);
        if (pc != null) {
            ServletRequest request = pc.getRequest();
            if (request != null) {
                if (request == req) {
                    out.println("Test PASSED");
                } else {
                    out.println("Test FAILED.  Request object returned from " +
                        "PageContext.getRequest() is not the same as that " +
                        "associated with the request scripting variable.");
                }
            } else {
                out.println("Test FAILED.  PageContext.getRequest() returned null.");
            }
        } else {
            out.println("Test FAILED.  No PageContext object available.");
        }
    }
%>

<%!
    public void pageContextGetResponseTest(HttpServletRequest req,
                                       HttpServletResponse res,
                                       JspWriter out)
    throws ServletException, IOException {
        PageContext pc = (PageContext) req.getAttribute(PAGECONTEXT_ATTR);
        if (pc != null) {
            ServletResponse response = pc.getResponse();
            if (response != null) {
                if (response == res) {
                    out.println("Test PASSED");
                } else {
                    out.println("Test FAILED.  Response object returned from " +
                        "PageContext.getResponse() is not the same as that " +
                        "associated with the response scripting variable.");
                }
            } else {
                out.println("Test FAILED.  PageContext.getRequest() returned null.");
            }
        } else {
            out.println("Test FAILED.  No PageContext object available.");
        }
    }
%>

<%!
    public void pageContextGetServletConfigTest(HttpServletRequest req,
                                       HttpServletResponse res,
                                       JspWriter out)
    throws ServletException, IOException {
        PageContext pc = (PageContext) req.getAttribute(PAGECONTEXT_ATTR);
        if (pc != null) {
            ServletConfig config = pc.getServletConfig();
            if (config != null) {
                if (config == req.getAttribute(CONFIG_ATTR)) {
                    out.println("Test PASSED");
                } else {
                    out.println("Test FAILED.  ServletConfig object returned from " +
                        "PageContext.getServletConfig() is not the same as that " +
                        "associated with the config scripting variable.");
                }
            } else {
                out.println("Test FAILED.  PageContext.getServletConfig() returned null.");
            }
        } else {
            out.println("Test FAILED.  No PageContext object available.");
        }
    }
%>

<%!
    public void pageContextGetServletContextTest(HttpServletRequest req,
                                       HttpServletResponse res,
                                       JspWriter out)
    throws ServletException, IOException {
        PageContext pc = (PageContext) req.getAttribute(PAGECONTEXT_ATTR);
        if (pc != null) {
            ServletContext context = pc.getServletContext();
            if (context != null) {
                if (context == req.getAttribute(CONTEXT_ATTR)) {
                    out.println("Test PASSED");
                } else {
                    out.println("Test FAILED.  ServletContext object returned from " +
                        "PageContext.getServletContext() is not the same as that " +
                        "associated with the application scripting variable.");
                }
            } else {
                out.println("Test FAILED.  PageContext.getServletContext() returned null.");
            }
        } else {
            out.println("Test FAILED.  No PageContext object available.");
        }
    }
%>

<%!
    public void pageContextGetExceptionTest(HttpServletRequest req,
                                            HttpServletResponse res,
                                            JspWriter out)
    throws ServletException, IOException {
        req.setAttribute("com.sun.ts.tests.jsp.api.checkException",
            new Boolean(true));
        throw new RuntimeException("PageContext.getException() test");
    }
%>

<%!
    public void pageContextForwardContextPathTest(HttpServletRequest req,
                                       HttpServletResponse res,
                                       JspWriter out)
    throws ServletException, IOException {
        PageContext pc = (PageContext) req.getAttribute(PAGECONTEXT_ATTR);
        if (pc != null) {
            pc.forward("/Resource.jsp");
        } else {
            out.println("Test FAILED.  No PageContext object available.");
        }
    }
%>

<%!
    public void pageContextForwardPagePathTest(HttpServletRequest req,
                                               HttpServletResponse res,
                                               JspWriter out)
    throws ServletException, IOException {
        PageContext pc = (PageContext) req.getAttribute(PAGECONTEXT_ATTR);
        if (pc != null) {
            pc.forward("Resource.jsp");
        } else {
            out.println("Test FAILED.  No PageContext object available.");
        }
    }
%>

<%!
    public void pageContextForwardServletExceptionTest(HttpServletRequest req,
                                                       HttpServletResponse res,
                                                       JspWriter out)
    throws ServletException, IOException {
        PageContext pc = (PageContext) req.getAttribute(PAGECONTEXT_ATTR);
        if (pc != null) {
            try {
                pc.forward("/Resource.jsp?exception=servletexception");
            } catch (Throwable t) {
                if (t instanceof ServletException) {
                    out.println("Test PASSED");
                    return;
                } else {
                    out.println("Test FAILED.  Exception thrown by forwarded" +
                        "resource, but was not an instance of ServletException");
                    out.println("Type thrown: " + t.getClass().getName());
                    return;
                }
            }
            out.println("Test FAILED.  No Exception thrown!");
        } else {
            out.println("Test FAILED.  No PageContext object available.");
        }
    }
%>

<%!
    public void pageContextForwardIOExceptionTest(HttpServletRequest req,
                                                  HttpServletResponse res,
                                                  JspWriter out)
    throws ServletException, IOException {
        PageContext pc = (PageContext) req.getAttribute(PAGECONTEXT_ATTR);
        if (pc != null) {
            try {
                pc.forward("/Resource.jsp?exception=ioexception");
            } catch (Throwable t) {
                if (t instanceof IOException) {
                    out.println("Test PASSED");
                    return;
                } else {
                    out.println("Test FAILED.  Exception thrown by forwarded" +
                        "resource, but was not an instance of IOException");
                    out.println("Type thrown: " + t.getClass().getName());
                    return;
                }
            }
            out.println("Test FAILED.  No Exception thrown!");
        } else {
            out.println("Test FAILED.  No PageContext object available.");
        }
    }
%>

<%!
    public void pageContextForwardIllegalStateExceptionTest(HttpServletRequest req,
                                                            HttpServletResponse res,
                                                            JspWriter out)
    throws ServletException, IOException {
        PageContext pc = (PageContext) req.getAttribute(PAGECONTEXT_ATTR);
        if (pc != null) {
            try {
                out.println("Arbitrary text...");
                out.flush();
                pc.forward("/Resource.jsp");
            } catch (IllegalStateException ise) {
                out.println("Test PASSED");
                return;
            }
            out.println("Test FAILED.  IllegalStateException not thrown.");
        } else {
            out.println("Test FAILED.  No PageContext object available.");
        }
    }
%>

<%!
    public void pageContextIncludeContextPathTest(HttpServletRequest req,
                                                  HttpServletResponse res,
                                                  JspWriter out)
    throws ServletException, IOException {
        PageContext pc = (PageContext) req.getAttribute(PAGECONTEXT_ATTR);
        if (pc != null) {
           pc.include("/Resource.jsp");
        } else {
            out.println("Test FAILED.  No PageContext object available.");
        }
    }
%>

<%!
    public void pageContextIncludePagePathTest(HttpServletRequest req,
                                               HttpServletResponse res,
                                               JspWriter out)
    throws ServletException, IOException {
        PageContext pc = (PageContext) req.getAttribute(PAGECONTEXT_ATTR);
        if (pc != null) {
           pc.include("Resource.jsp");
        } else {
            out.println("Test FAILED.  No PageContext object available.");
        }
    }
%>

<%!
    public void pageContextIncludeFlushTrueTest(HttpServletRequest req,
                                                HttpServletResponse res,
                                                JspWriter out)
    throws ServletException, IOException {
        PageContext pc = (PageContext) req.getAttribute(PAGECONTEXT_ATTR);
        if (pc != null) {
            try {
                pc.include("Resource.jsp", true);
                res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Test FAILED. Response not flushed by inclusion.");
            } catch (Throwable t) {
                if (t instanceof IllegalStateException) {
                    out.println("Stream was properly flushed.");
                } else {
                    out.println("Stream was not flushed.");
                }
            }
        } else {
            out.println("Test FAILED.  No PageContext object available.");
        }
    }
%>

<%!
    public void pageContextIncludeFlushFalseTest(HttpServletRequest req,
                                                HttpServletResponse res,
                                                JspWriter out)
    throws ServletException, IOException {
        PageContext pc = (PageContext) req.getAttribute(PAGECONTEXT_ATTR);
        if (pc != null) {
            if (out.getBufferSize() > 0) {
                try {
                    pc.include("/Resource.jsp", false);
                    out.clear();
                    out.println("Test PASSED");
                } catch (IllegalStateException ise) {
                    out.println("Test FAILED.  Steam was flushed after" +
                        " inclusion when flush argument was false.");
                }
            } else {
                out.println("Test PASSED.  Buffering is not available.");
            }
        } else {
            out.println("Test FAILED.  No PageContext object available.");
        }
    }
%>

<%!
    public void pageContextHandlePageExceptionExcTest(HttpServletRequest req,
                                                      HttpServletResponse res,
                                                      JspWriter out)
    throws ServletException, IOException {
        PageContext pc = (PageContext) req.getAttribute(PAGECONTEXT_ATTR);
        if (pc != null) {
            pc.handlePageException(new RuntimeException());
        } else {
            out.println("Test FAILED.  No PageContext object available.");
        }
    }
%>

<%!
    public void pageContextHandlePageExceptionThrTest(HttpServletRequest req,
                                                      HttpServletResponse res,
                                                      JspWriter out)
    throws ServletException, IOException {
        PageContext pc = (PageContext) req.getAttribute(PAGECONTEXT_ATTR);
        if (pc != null) {
            pc.handlePageException(new Throwable());
        } else {
            out.println("Test FAILED.  No PageContext object available.");
        }
    }
%>

<%!
    public void pageContextHandlePageExceptionExcNPETest(HttpServletRequest req,
                                                         HttpServletResponse res,
                                                         JspWriter out)
    throws ServletException, IOException {
        PageContext pc = (PageContext) req.getAttribute(PAGECONTEXT_ATTR);
        if (pc != null) {
            try {
                Exception e = null;
                pc.handlePageException(e);
            } catch (Throwable t) {
                if (t instanceof NullPointerException) {
                    out.println("Test PASSED");
                } else {
                    out.println("Test FAILED.  Exception thrown when passing" +
                        " null to handlePageException, but was not an instance\n" +
                        "of NullPointerException.");
                    out.println("Received: " + t.getClass().getName());
                }
            }
        } else {
            out.println("Test FAILED.  No PageContext object available.");
        }
    }
%>

<%!
    public void pageContextHandlePageExceptionThrNPETest(HttpServletRequest req,
                                                         HttpServletResponse res,
                                                         JspWriter out)
    throws ServletException, IOException {
        PageContext pc = (PageContext) req.getAttribute(PAGECONTEXT_ATTR);
        if (pc != null) {
            try {
                Throwable th = null;
                pc.handlePageException(th);
            } catch (Throwable t) {
                if (t instanceof NullPointerException) {
                    out.println("Test PASSED");
                } else {
                    out.println("Test FAILED.  Exception thrown when passing" +
                        " null to handlePageException, but was not an instance\n" +
                        "of NullPointerException.");
                    out.println("Received: " + t.getClass().getName());
                }
            }
        } else {
            out.println("Test FAILED.  No PageContext object available.");
        }
    }
%>

<%!
    public void pageContextPushPopBodyTest(HttpServletRequest req,
                                           HttpServletResponse res,
                                           JspWriter out)
    throws ServletException, IOException {
        PageContext pc = (PageContext) req.getAttribute(PAGECONTEXT_ATTR);
        if (pc != null) {
            Object origOutRef = out;
            BodyContent content = pc.pushBody();
            Object newOutRef = pc.getOut();
            Object newOutRef2 = pc.getAttribute(PageContext.OUT);
            if (content != null) {
                if (origOutRef != newOutRef) {
                    if (origOutRef != newOutRef2) {
                        pc.popBody();
                        newOutRef = pc.getOut();
                        newOutRef2 = pc.getAttribute(PageContext.OUT);
                        if (origOutRef == newOutRef) {
                            if (origOutRef == newOutRef2) {
                                out.println("Test PASSED");
                            } else {
                                out.println("Test FAILED.  PageContext.OUT not restored with " +
                                    "the original 'out' object after PageContext.popBody() " +
                                    "was called.");
                            }
                        } else {
                            out.println("Test FAILED.  Original 'out' object" +
                                " was not restored after PageContext.popBody() " +
                                "was called.");
                        }
                    } else {
                        out.println("Test FAILED.  PageContext.OUT not updated with a new" +
                            " reference after calling PageContext.pushBody().");
                    }
                } else {
                    out.println("Test FAILED.  'out' object after PageContext" +
                        ".pushBody() was called did not change.");
                }
            } else {
                out.println("Test FAILED.  PageContext.pushBody() returned " +
                    "a null BodyContent object.");
            }
        } else {
            out.println("Test FAILED.  No PageContext object available.");
        }
    }
%>

<%!
    public void pageContextGetErrorDataTest(HttpServletRequest req,
                                              HttpServletResponse res,
                                              JspWriter out)
    throws ServletException, IOException {
        req.setAttribute("com.sun.ts.tests.jsp.api.checkErrorData",
            new Boolean(true));
        throw new RuntimeException();
    }
%>

<%!
    public void pageContextIncludeIOExceptionTest(HttpServletRequest req,
                                                  HttpServletResponse res,
                                                  JspWriter out)
    throws ServletException, IOException {
        PageContext pc = (PageContext) req.getAttribute(PAGECONTEXT_ATTR);
        if (pc != null) {
            try {
                pc.include("/Resource.jsp?exception=ioexception");
            } catch (Throwable t) {
                if (t instanceof IOException) {
                    out.println("Test PASSED");
                    return;
                } else {
                    out.println("Test FAILED.  Exception thrown by included" +
                        "resource, but was not an instance of IOException");
                    out.println("Type thrown: " + t.getClass().getName());
                    return;
                }
            }
            out.println("Test FAILED.  No Exception thrown!");
        } else {
            out.println("Test FAILED.  No PageContext object available.");
        }
    }
%>

<%!
    public void pageContextIncludeFlushIOExceptionTest(HttpServletRequest req,
                                                  HttpServletResponse res,
                                                  JspWriter out)
    throws ServletException, IOException {
        PageContext pc = (PageContext) req.getAttribute(PAGECONTEXT_ATTR);
        if (pc != null) {
            try {
                pc.include("/Resource.jsp?exception=ioexception", true);
            } catch (Throwable t) {
                if (t instanceof IOException) {
                    out.println("Test PASSED");
                    return;
                } else {
                    out.println("Test FAILED.  Exception thrown by included" +
                        "resource, but was not an instance of IOException");
                    out.println("Type thrown: " + t.getClass().getName());
                    return;
                }
            }
            out.println("Test FAILED.  No Exception thrown!");
        } else {
            out.println("Test FAILED.  No PageContext object available.");
        }
    }
%>

<%!
    public void pageContextIncludeServletExceptionTest(HttpServletRequest req,
                                                       HttpServletResponse res,
                                                       JspWriter out)
    throws ServletException, IOException {
        PageContext pc = (PageContext) req.getAttribute(PAGECONTEXT_ATTR);
        if (pc != null) {
            try {
                pc.include("/Resource.jsp?exception=servletexception");
            } catch (Throwable t) {
                if (t instanceof ServletException) {
                    out.println("Test PASSED");
                    return;
                } else {
                    out.println("Test FAILED.  Exception thrown by included" +
                        "resource, but was not an instance of ServletException");
                    out.println("Type thrown: " + t.getClass().getName());
                    return;
                }
            }
            out.println("Test FAILED.  No Exception thrown!");
        } else {
            out.println("Test FAILED.  No PageContext object available.");
        }
    }
%>

<%!
    public void pageContextIncludeFlushServletExceptionTest(HttpServletRequest req,
                                                            HttpServletResponse res,
                                                            JspWriter out)
    throws ServletException, IOException {
        PageContext pc = (PageContext) req.getAttribute(PAGECONTEXT_ATTR);
        if (pc != null) {
            try {
                pc.include("/Resource.jsp?exception=servletexception", true);
            } catch (Throwable t) {
                if (t instanceof ServletException) {
                    out.println("Test PASSED");
                    return;
                } else {
                    out.println("Test FAILED.  Exception thrown by included" +
                        "resource, but was not an instance of ServletException");
                    out.println("Type thrown: " + t.getClass().getName());
                    return;
                }
            }
            out.println("Test FAILED.  No Exception thrown!");
        } else {
            out.println("Test FAILED.  No PageContext object available.");
        }
    }
%>

<%!
    public void pageContextGetSetAttributeTest(HttpServletRequest req,
                                               HttpServletResponse res,
                                               JspWriter out)
    throws ServletException, IOException {
        PageContext pc = (PageContext) req.getAttribute(PAGECONTEXT_ATTR);
        if (pc != null) {
            final String attrName = "testObject";
            final String value = "string";
            pc.setAttribute(attrName, value);
            String s = (String) pc.getAttribute(attrName);
            if (s != null) {
                if (s.equals(value)) {
                    out.println("Test PASSED");
                } else {
                    out.println("Test FAILED. Incorrect object returned by " +
                        "PageContext.getAttribute()");
                }
            } else {
                out.println("Test FAILED.  PageContext.getAttribute() " +
                    "incorrectly returned null.");
            }
        } else {
            out.println("Test FAILED. No PageContext object available.");
        }
    }
%>

<%!
    public void pageContextSetAttributeNPETest(HttpServletRequest req,
                                               HttpServletResponse res,
                                               JspWriter out)
    throws ServletException, IOException {
        PageContext pc = (PageContext) req.getAttribute(PAGECONTEXT_ATTR);
        if (pc != null) {
            try {
                pc.setAttribute(null, new String(""));
            } catch (NullPointerException npe) {
                try {
                    pc.setAttribute("testObject", null);
                    out.println("Test PASSED");
                    return;
                } catch (NullPointerException nnpe) {
                    out.println("Test FAILED.  NPE thrown when a null value " +
                        "is provided to PageContext.setAttribute()");
                    return;
                }
            }
            out.println("Test FAILED.  NPE not thrown when a null name is " +
                "provided to PageContext.setAttribute()");
        } else {
            out.println("Test FAILED. No PageContext object available.");
        }
    }
%>

<%!
    public void pageContextGetSetAttributeInScopeTest(HttpServletRequest req,
                                                      HttpServletResponse res,
                                                      JspWriter out)
    throws ServletException, IOException {
        PageContext pc = (PageContext) req.getAttribute(PAGECONTEXT_ATTR);
        if (pc != null) {
            pc.setAttribute("pageScope", "pageScope", PageContext.PAGE_SCOPE);
            pc.setAttribute("requestScope", "requestScope",
                PageContext.REQUEST_SCOPE);
            pc.setAttribute("sessionScope", "sessionScope",
                PageContext.SESSION_SCOPE);
            pc.setAttribute("applicationScope", "applicationScope",
                PageContext.APPLICATION_SCOPE);

            String s1 = (String) pc.getAttribute("pageScope",
                PageContext.PAGE_SCOPE);
            String s2 = (String) pc.getAttribute("requestScope",
                PageContext.REQUEST_SCOPE);
            String s3 = (String) pc.getAttribute("sessionScope",
                PageContext.SESSION_SCOPE);
            String s4 = (String) pc.getAttribute("applicationScope",
                PageContext.APPLICATION_SCOPE);
            if (s1 != null) {
                if (s1.equals("pageScope")) {
                    if (s2 != null) {
                        if (s2.equals("requestScope")) {
                            if (s3 != null) {
                                if (s3.equals("sessionScope")) {
                                    if (s4 != null) {
                                        if (s4.equals("applicationScope")) {
                                            out.println("Test PASSED");
                                        } else {
                                            out.println("Test FAILED.  Wrong" +
                                                "application scoped object returned.");
                                        }
                                    } else {
                                        out.println("Test FAILED. " +
                                            "PageContext.getAttribute() returned null.");
                                    }
                                } else {
                                    out.println("Test FAILED.  Wrong" +
                                        "session scoped object returned.");
                                }
                            } else {
                                out.println("Test FAILED. " +
                                    "PageContext.getAttribute() returned null.");
                            }
                        } else {
                            out.println("Test FAILED.  Wrong" +
                                "request scoped object returned.");
                        }
                    } else {
                        out.println("Test FAILED. " +
                            "PageContext.getAttribute() returned null.");
                    }
                } else {
                    out.println("Test FAILED.  Wrong" +
                        "page scoped object returned.");
                }
            } else {
                out.println("Test FAILED. " +
                    "PageContext.getAttribute() returned null.");
            }
        } else {
            out.println("Test FAILED. No PageContext object available.");
        }
        cleanup(pc);
    }
%>

<%!
    public void pageContextSetAttributeInScopeNPETest(HttpServletRequest req,
                                                      HttpServletResponse res,
                                                      JspWriter out)
    throws ServletException, IOException {
        PageContext pc = (PageContext) req.getAttribute(PAGECONTEXT_ATTR);
        if (pc != null) {
            try {
                pc.setAttribute(null, new String(""), PageContext.PAGE_SCOPE);
            } catch (NullPointerException npe) {
                try {
                    pc.setAttribute("testObject", null,
                        PageContext.APPLICATION_SCOPE);
                    out.println("Test PASSED");
                    return;
                } catch (NullPointerException nnpe) {
                    out.println("Test FAILED.  NPE thrown when a null value " +
                        "is provided to PageContext.setAttribute()");
                    return;
                }
            }
            out.println("Test FAILED.  NPE not thrown when a null name is " +
                "provided to PageContext.setAttribute()");
        } else {
            out.println("Test FAILED. No PageContext object available.");
        }
    }
%>

<%!
    public void pageContextSetAttributeInScopeIllegalArgumentExcTest(HttpServletRequest req,
                                                                     HttpServletResponse res,
                                                                     JspWriter out)
    throws ServletException, IOException {
        PageContext pc = (PageContext) req.getAttribute(PAGECONTEXT_ATTR);
        if (pc != null) {
            try {
                pc.setAttribute("pageScope", "pageScope", Integer.MIN_VALUE);
            } catch (Throwable t) {
                if (t instanceof IllegalArgumentException) {
                    out.println("Test PASSED");
                    return;
                } else {
                    out.println("Test FAILED.  Exception thrown, but not an" +
                        "instance of IllegalArgumentException");
                    return;
                }
            }
            out.println("Test FAILED.  IllegalArgumentException not thrown " +
                "when PageContext.setAttribute() was provided an invalid scope");
        } else {
            out.println("Test FAILED. No PageContext object available.");
        }
    }
%>

<%!
    public void pageContextGetAttributeNPETest(HttpServletRequest req,
                                               HttpServletResponse res,
                                               JspWriter out)
    throws ServletException, IOException {
        PageContext pc = (PageContext) req.getAttribute(PAGECONTEXT_ATTR);
        if (pc != null) {
            try {
                pc.getAttribute(null);
            } catch (Throwable t) {
                if (t instanceof NullPointerException) {
                    out.println("Test PASSED");
                    return;
                } else {
                    out.println("Test FAILED.  Exception thrown, but not an" +
                        "instance of NullPointerException");
                    return;
                }
            }
            out.println("Test FAILED.  NPE not thrown when " +
                "PageContext.getAttribute() is provided a null value.");
        } else {
            out.println("Test FAILED. No PageContext object available.");
        }
    }
%>

<%!
    public void pageContextGetAttributeInScopeIllegalArgumentExceptionTest(HttpServletRequest req,
                                                                           HttpServletResponse res,
                                                                           JspWriter out)
    throws ServletException, IOException {
        PageContext pc = (PageContext) req.getAttribute(PAGECONTEXT_ATTR);
        if (pc != null) {
            try {
                pc.getAttribute("pageContext", Integer.MIN_VALUE);
            } catch (Throwable t) {
                if (t instanceof IllegalArgumentException) {
                    out.println("Test PASSED");
                    return;
                } else {
                    out.println("Test FAILED.  Exception thrown, but not an" +
                        "instance of IllegalArgumentException");
                    return;
                }
            }
            out.println("Test FAILED.  No Exception thrown when an illegal" +
                " scope was provided to PageContext.getAttribute()");
        } else {
            out.println("Test FAILED. No PageContext object available.");
        }
    }
%>

<%!
    public void pageContextFindAttributeTest(HttpServletRequest req,
                                             HttpServletResponse res,
                                             JspWriter out)
    throws ServletException, IOException {
        PageContext pc = (PageContext) req.getAttribute(PAGECONTEXT_ATTR);
        if (pc != null) {
            fillAllScopes(pc);
            String s1 = (String) pc.findAttribute("pageScope");
            String s2 = (String) pc.findAttribute("sessionScope");
            Object nullObj = pc.findAttribute("nonAttribute");
            if (s1 != null) {
                if (s1.equals("pageScope")) {
                    if (s2 != null) {
                        if (s2.equals("sessionScope")) {
                            if (nullObj == null) {
                                out.println("Test PASSED");
                            } else {
                                out.println("Test FAILED.  Null reference" +
                                    " not returned from PageContext.findAttribute()" +
                                    " when provided a non existent value");
                            }
                        } else {
                            out.println("Test FAILED.  Wrong" +
                                "session scoped object returned.");
                        }
                    } else {
                        out.println("Test FAILED. " +
                            "PageContext.getAttribute() returned null.");
                    }
                } else {
                    out.println("Test FAILED.  Wrong" +
                        "page scoped object returned.");
                }
            } else {
                out.println("Test FAILED. " +
                    "PageContext.getAttribute() returned null.");
            }
        } else {
            out.println("Test FAILED. No PageContext object available.");
        }
        cleanup(pc);
    }
%>

<%!
    public void pageContextRemoveAttributeTest(HttpServletRequest req,
                                               HttpServletResponse res,
                                               JspWriter out)
    throws ServletException, IOException {
        PageContext pc = (PageContext) req.getAttribute(PAGECONTEXT_ATTR);
        if (pc != null) {
            fillAllScopes(pc);
            pc.removeAttribute("pageScope");
            pc.removeAttribute("requestScope");
            pc.removeAttribute("sessionScope");
            pc.removeAttribute("applicationScope");
            Object o1 = pc.findAttribute("pageScope");
            Object o2 = pc.findAttribute("requestScope");
            Object o3 = pc.findAttribute("sessionScope");
            Object o4 = pc.findAttribute("applicationScope");
            if (o1 == null) {
                if (o2 == null) {
                    if (o3 == null) {
                        if (o4 == null) {
                            out.println("Test PASSED");
                        } else {
                            out.println("Test FAILED.  Application scoped object" +
                                " not removed by PageContext.removeAttribute().");
                        }
                    } else {
                        out.println("Test FAILED.  Session scoped object" +
                            " not removed by PageContext.removeAttribute().");
                    }
                } else {
                    out.println("Test FAILED.  Request scoped object" +
                        " not removed by PageContext.removeAttribute().");
                }
            } else {
                out.println("Test FAILED.  Page scoped object" +
                    " not removed by PageContext.removeAttribute().");
            }
        } else {
            out.println("Test FAILED. No PageContext object available.");
        }
    }
%>

<%!
    public void pageContextRemoveAttributeFromScopeTest(HttpServletRequest req,
                                                        HttpServletResponse res,
                                                        JspWriter out)
    throws ServletException, IOException {
        PageContext pc = (PageContext) req.getAttribute(PAGECONTEXT_ATTR);
        if (pc != null) {
            fillAllScopes(pc);
            pc.removeAttribute("pageScope", PageContext.PAGE_SCOPE);
            pc.removeAttribute("requestScope", PageContext.REQUEST_SCOPE);
            pc.removeAttribute("sessionScope", PageContext.SESSION_SCOPE);
            pc.removeAttribute("applicationScope", PageContext.APPLICATION_SCOPE);
            Object o1 = pc.findAttribute("pageScope");
            Object o2 = pc.findAttribute("requestScope");
            Object o3 = pc.findAttribute("sessionScope");
            Object o4 = pc.findAttribute("applicationScope");
            if (o1 == null) {
                if (o2 == null) {
                    if (o3 == null) {
                        if (o4 == null) {
                            out.println("Test PASSED");
                        } else {
                            out.println("Test FAILED.  Application scoped object" +
                                " not removed by PageContext.removeAttribute().");
                        }
                    } else {
                        out.println("Test FAILED.  Session scoped object" +
                            " not removed by PageContext.removeAttribute().");
                    }
                } else {
                    out.println("Test FAILED.  Request scoped object" +
                        " not removed by PageContext.removeAttribute().");
                }
            } else {
                out.println("Test FAILED.  Page scoped object" +
                    " not removed by PageContext.removeAttribute().");
            }
        } else {
            out.println("Test FAILED. No PageContext object available.");
        }
    }
%>

<%!
    public void pageContextRemoveAttributeFromScopeIllegalScopeTest(HttpServletRequest req,
                                                                    HttpServletResponse res,
                                                                    JspWriter out)
    throws ServletException, IOException {
        PageContext pc = (PageContext) req.getAttribute(PAGECONTEXT_ATTR);
        if (pc != null) {
            try {
                pc.removeAttribute("nonAttribute", Integer.MIN_VALUE);
            } catch (Throwable t) {
                if (t instanceof IllegalArgumentException) {
                    out.println("Test PASSED");
                    return;
                } else {
                    out.println("Test FAILED.  Exception thrown when providing an illegal scope" +
                        " but was not an instance of IllegalArgumentException.");
                    out.println("Received: " + t);
                    return;
                }
            }
            out.println("Test FAILED.  No Exception thrown when an illegal scope was provided.");
        } else {
            out.println("Test FAILED. No PageContext object available.");
        }
    }
%>

<%!
    public void pageContextGetAttributeScopeTest(HttpServletRequest req,
                                                 HttpServletResponse res,
                                                 JspWriter out)
    throws ServletException, IOException {
        PageContext pc = (PageContext) req.getAttribute(PAGECONTEXT_ATTR);
        if (pc != null) {
            fillAllScopes(pc);
            if (pc.getAttributesScope("pageScope") == PageContext.PAGE_SCOPE) {
                if (pc.getAttributesScope("requestScope") == PageContext.REQUEST_SCOPE) {
                    if (pc.getAttributesScope("sessionScope") == PageContext.SESSION_SCOPE) {
                        if (pc.getAttributesScope("applicationScope") == PageContext.APPLICATION_SCOPE) {
                            out.println("Test PASSED");
                        } else {
                            out.println("Test FAILED.  PageContext.getAttributes" +
                                "Scope returned unexpected value for object in APPLICATION_SCOPE");
                        }
                    } else {
                        out.println("Test FAILED.  PageContext.getAttributes" +
                            "Scope returned unexpected value for object in SESSION_SCOPE");
                    }
                } else {
                    out.println("Test FAILED.  PageContext.getAttributes" +
                        "Scope returned unexpected value for object in REQUEST_SCOPE");
                }
            } else {
                out.println("Test FAILED.  PageContext.getAttributes" +
                    "Scope returned unexpected value for object in PAGE_SCOPE");
            }
        } else {
            out.println("Test FAILED. No PageContext object available.");
        }
        cleanup(pc);
    }
%>

<%!
    public void pageContextGetAttributeNamesInScopeTest(HttpServletRequest req,
                                                        HttpServletResponse res,
                                                        JspWriter out)
    throws ServletException, IOException {
        PageContext pc = (PageContext) req.getAttribute(PAGECONTEXT_ATTR);
        if (pc != null) {
            fillAllScopes(pc);
            Enumeration page = pc.getAttributeNamesInScope(PageContext.PAGE_SCOPE);
            Enumeration request = pc.getAttributeNamesInScope(PageContext.REQUEST_SCOPE);
            Enumeration session = pc.getAttributeNamesInScope(PageContext.SESSION_SCOPE);
            Enumeration app = pc.getAttributeNamesInScope(PageContext.APPLICATION_SCOPE);

            if (page != null) {
                if (checkValue(page, "pageScope", out)) {
                    if (request != null) {
                        if (checkValue(request, "requestScope", out)) {
                            if (session != null) {
                                if (checkValue(session, "sessionScope", out)) {
                                    if (app != null) {
                                        if (checkValue(app, "applicationScope", out)) {
                                            out.println("Test PASSED");
                                        } else {
                                            out.println("Test FAILED.  Enumeration" +
                                                " did not contain expected value.");
                                        }
                                    } else {
                                        out.println("Test FAILED.  Null Enumeration returned");
                                    }
                                } else {
                                    out.println("Test FAILED.  Enumeration" +
                                        " did not contain expected value.");
                                }
                            } else {
                                out.println("Test FAILED.  Null Enumeration returned");
                            }
                        } else {
                            out.println("Test FAILED.  Enumeration" +
                                " did not contain expected value.");
                        }
                    } else {
                        out.println("Test FAILED.  Null Enumeration returned");
                    }
                } else {
                    out.println("Test FAILED.  Enumeration" +
                        " did not contain expected value.");
                }
            } else {
                out.println("Test FAILED.  Null Enumeration returned");
            }
        } else {
            out.println("Test FAILED. No PageContext object available.");
        }
        cleanup(pc);
    }
%>

<%!
    public void pageContextGetAttributeNamesInScopeIllegalScopeTest(HttpServletRequest req,
                                                                    HttpServletResponse res,
                                                                    JspWriter out)
    throws ServletException, IOException {
        PageContext pc = (PageContext) req.getAttribute(PAGECONTEXT_ATTR);
        if (pc != null) {
            try {
                pc.getAttributeNamesInScope(Integer.MAX_VALUE);
            } catch (Throwable t) {
                if (t instanceof IllegalArgumentException) {
                    out.println("Test PASSED");
                    return;
                } else {
                    out.println("Test FAILED.  Exception thrown when providing an illegal scope" +
                        " but was not an instance of IllegalArgumentException.");
                    out.println("Received: " + t);
                    return;
                }
            }
            out.println("Test FAILED.  No Exception thrown when an illegal scope was provided.");
        } else {
            out.println("Test FAILED. No PageContext object available.");
        }
    }
%>

<%!
    public void pageContextGetOutTest(HttpServletRequest req,
                                      HttpServletResponse res,
                                      JspWriter out)
    throws ServletException, IOException {
        PageContext pc = (PageContext) req.getAttribute(PAGECONTEXT_ATTR);
        if (pc != null) {
            if (pc.getOut() == out) {
                out.println("Test PASSED");
            } else {
                out.println("Test FAILED.  Object reference returned by " +
                    "PageContext.getOut() not the same as the reference " +
                    "associated with the 'out' scripting variable.");
            }
        } else {
            out.println("Test FAILED. No PageContext object available.");
        }
    }
%>

<%!
    public void pageContextGetExpressionEvaluatorTest(HttpServletRequest req,
                                                      HttpServletResponse res,
                                                      JspWriter out)
    throws ServletException, IOException {
        PageContext pc = (PageContext) req.getAttribute(PAGECONTEXT_ATTR);
        if (pc != null) {
            ExpressionEvaluator eval = pc.getExpressionEvaluator();
            if (eval != null) {
                out.println("Test PASSED");
            } else {
                out.println("Test FAILED.  PageContext.getExpressionEvaluator" +
                    " returned null.");
            }
        } else {
            out.println("Test FAILED. No PageContext object available.");
        }
    }
%>

<%!
    public void pageContextGetVariableResolverTest(HttpServletRequest req,
                                                   HttpServletResponse res,
                                                   JspWriter out)
    throws ServletException, IOException {
        PageContext pc = (PageContext) req.getAttribute(PAGECONTEXT_ATTR);
        if (pc != null) {
            VariableResolver resolver = pc.getVariableResolver();
            if (resolver != null) {
                out.println("Test PASSED");
            } else {
                out.println("Test FAILED.  PageContext.getVariableResolver()" +
                    " returned null.");
            }
        } else {
            out.println("Test FAILED. No PageContext object available.");
        }
    }
%>

<%!
    public void pageContextFindAttributeNullNameTest(HttpServletRequest req,
                                                     HttpServletResponse res,
                                                     JspWriter out)
    throws ServletException, IOException {
        PageContext pc = (PageContext) req.getAttribute(PAGECONTEXT_ATTR);
        if (pc != null) {
            try {
                pc.findAttribute(null);
                out.println("Test FAILED.  No Exception thrown when null" +
                    " argument passed to findAttribute(String).");
            } catch (Throwable t) {
                if (t instanceof NullPointerException) {
                    out.println("Test PASSED");
                } else {
                    out.println("Test FAILED.  Expected a NullPointerException to" +
                        " be thrown if findAttribute(String) is passed a null value.\n" +
                        "Actual exception thrown: " + t.getClass().getName());
                }
            }
        } else {
            out.println("Test FAILED. No PageContext object available.");
        }
    }
%>

<%!
    public void pageContextGetAttributesScopeNullNameTest(HttpServletRequest req,
                                                          HttpServletResponse res,
                                                          JspWriter out)
        throws ServletException, IOException {
        PageContext pc = (PageContext) req.getAttribute(PAGECONTEXT_ATTR);
        if (pc != null) {
            try {
                pc.getAttributesScope(null);
                out.println("Test FAILED.  No Exception thrown when null" +
                    " argument passed to getAttributesScope(String).");
            } catch (Throwable t) {
                if (t instanceof NullPointerException) {
                    out.println("Test PASSED");
                } else {
                    out.println("Test FAILED.  Expected a NullPointerException to" +
                        " be thrown if getAttributesScope(String) is passed a null value.\n" +
                        "Actual exception thrown: " + t.getClass().getName());
                }
            }
        } else {
            out.println("Test FAILED. No PageContext object available.");
        }
    }
%>

<%!
    public void pageContextRemoveAttributeNullNameTest(HttpServletRequest req,
                                                       HttpServletResponse res,
                                                       JspWriter out)
        throws ServletException, IOException {
        PageContext pc = (PageContext) req.getAttribute(PAGECONTEXT_ATTR);
        if (pc != null) {
            try {
                pc.removeAttribute(null);
                out.println("Test FAILED.  No Exception thrown when null" +
                    " argument passed to removeAttribute(String).");
            } catch (Throwable t) {
                if (t instanceof NullPointerException) {
                    try {
                        pc.removeAttribute(null, PageContext.PAGE_SCOPE);
                        out.println("Test FAILED.  No Exception thrown when null" +
                            " argument passed to removeAttribute(String, int)");
                    } catch (Throwable t1) {
                        if (t instanceof NullPointerException) {
                            out.println("Test PASSED");
                        } else {
                            out.println("Test FAILED.  Expected a NullPointerException to" +
                                " be thrown if removeAttribute(String, int) is passed a null value.\n" +
                                "Actual exception thrown: " + t.getClass().getName());
                        }
                    }
                } else {
                    out.println("Test FAILED.  Expected a NullPointerException to" +
                        " be thrown if removeAttribute(String) is passed a null value.\n" +
                        "Actual exception thrown: " + t.getClass().getName());
                }
            }
        } else {
            out.println("Test FAILED. No PageContext object available.");
        }
    }
%>

<%!
    public void pageContextSetAttributeNullValueTest(HttpServletRequest req,
                                                     HttpServletResponse res,
                                                     JspWriter out)
    throws ServletException, IOException {
        PageContext pc = (PageContext) req.getAttribute(PAGECONTEXT_ATTR);
        pc.setAttribute("simple", "value");
        if (pc != null) {
            pc.setAttribute("simple", null);
            if (pc.getAttribute("simple") == null) {
                pc.setAttribute("simple2", "value");
                pc.setAttribute("simple2", null, PageContext.PAGE_SCOPE);
                if (pc.getAttribute("simple2") == null) {
                    out.println("Test PASSED");
                } else {
                    out.println("Test FAILED.  Attribute not removed from PageContext " +
                        "when null value was passed to setAttribute(String, Object, int)" +
                        " and the attribute name specified exists in the PageContext");
                }
            } else {
                out.println("Test FAILED.  Attribute not removed from PageContext " +
                    "when null value was passed to setAttribute(String, Object)" +
                    " and the attribute name specified exists in the PageContext");
            }
        } else {
            out.println("Test FAILED. No PageContext object available.");
        }
    }
%>

<%-- Test invocation --%>

<%
    request.setAttribute(PAGECONTEXT_ATTR, pageContext);
    request.setAttribute(SESSION_ATTR, session);
    request.setAttribute(PAGE_ATTR, page);
    request.setAttribute(CONFIG_ATTR, config);
    request.setAttribute(CONTEXT_ATTR, application);

    JspTestUtil.invokeTest(this, request, response, out);
%>
