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
                 javax.servlet.jsp.tagext.TagAdapter,
                 javax.servlet.jsp.tagext.Tag,
                 javax.servlet.jsp.tagext.SimpleTag,
                 javax.servlet.jsp.tagext.JspTag,
                 javax.servlet.jsp.tagext.SimpleTagSupport" %>
<%@ page contentType="text/plain" %>

<%!
    private static TagAdapter getTagAdapter() {
        return new TagAdapter(new SimpleTagSupport());
    }
%>

<%-- Begin test definitions --%>

<%!
    public void tagAdapterCtorTest(HttpServletRequest req,
                                   HttpServletResponse res,
                                   JspWriter out)
    throws ServletException, IOException {
        TagAdapter adapter = getTagAdapter();
        if (adapter != null) {
            out.println("Test PASSED");
        } else {
            out.println("Test FAILED.  Unable to create new TagAdapter instanced via " +
                "the public constuctor.");
        }
    }
%>

<%!
    public void tagAdapterSetPageContextTest(HttpServletRequest req,
                                             HttpServletResponse res,
                                             JspWriter out)
    throws ServletException, IOException {
        TagAdapter adapter = getTagAdapter();
        if (adapter != null) {
            try {
                adapter.setPageContext(null);
            } catch (Throwable t) {
                if (t instanceof UnsupportedOperationException) {
                    out.println("Test PASSED");
                } else {
                    out.println("Test FAILED.  Exception thrown when TagAdapter" +
                        ".setPageContext() was called, but was not an instance" +
                        " of UnsupportedOperationException.  Received: " +
                        t.getClass().getName());
                }
            }
        } else {
            out.println("Test FAILED.  TagAdapter was null.");
        }
    }
%>

<%!
    public void tagAdapterSetParentTest(HttpServletRequest req,
                                        HttpServletResponse res,
                                        JspWriter out)
    throws ServletException, IOException {
        TagAdapter adapter = getTagAdapter();
        if (adapter != null) {
            try {
                adapter.setParent(null);
            } catch (Throwable t) {
                if (t instanceof UnsupportedOperationException) {
                    out.println("Test PASSED");
                } else {
                    out.println("Test FAILED.  Exception thrown when TagAdapter" +
                        ".setParent() was called, but was not an instance" +
                        " of UnsupportedOperationException.  Received: " +
                        t.getClass().getName());
                }
            }
        } else {
            out.println("Test FAILED.  TagAdapter was null.");
        }
    }
%>

<%!
    public void tagAdapterGetParentTest(HttpServletRequest req,
                                        HttpServletResponse res,
                                        JspWriter out)
    throws ServletException, IOException {
        TagAdapter adapter = getTagAdapter();
        if (adapter != null) {
            boolean pass = false;
            JspTag adaptee = adapter.getAdaptee();
            if(adaptee == null) {
                out.println("Test FAILED.  adpatee is null.");
            } else if(adaptee instanceof SimpleTag) {
                Tag p1 = adapter.getParent();
                SimpleTag stg = (SimpleTag) adaptee; 
                JspTag p2 = stg.getParent();
                if(p1 == p2) {
                    out.println("Test PASSED. getParent() returns the same value as getAdaptee().getParent()");
                } else {
                    out.println("Test FAILED.  getParent() does not return the same value as getAdaptee().getParent()");
                }
            } else {
                out.println("Test PASSED. adaptee is not a SimpleTag and need to update tests.");
            }
        } else {
            out.println("Test FAILED.  TagAdapter was null.");
        }
    }
%>

<%!
    public void tagAdapterDoStartTagTest(HttpServletRequest req,
                                        HttpServletResponse res,
                                        JspWriter out)
    throws ServletException, IOException {
        TagAdapter adapter = getTagAdapter();
        if (adapter != null) {
            try {
                adapter.doStartTag();
            } catch (Throwable t) {
                if (t instanceof UnsupportedOperationException) {
                    out.println("Test PASSED");
                } else if (t instanceof JspException) {
                    out.println("Test FAILED.  JspException was incorrectly" +
                        " thrown when calling doStartTag() on the TagAdapter.");
                } else {
                    out.println("Test FAILED.  Exception thrown when TagAdapter" +
                        ".doStartTag() was called, but was not an instance" +
                        " of UnsupportedOperationException.  Received: " +
                        t.getClass().getName());
                }
            }
        } else {
            out.println("Test FAILED.  TagAdapter was null.");
        }
    }
%>

<%!
    public void tagAdapterDoEndTagTest(HttpServletRequest req,
                                       HttpServletResponse res,
                                       JspWriter out)
    throws ServletException, IOException {
        TagAdapter adapter = getTagAdapter();
        if (adapter != null) {
            try {
                adapter.doEndTag();
            } catch (Throwable t) {
                if (t instanceof UnsupportedOperationException) {
                    out.println("Test PASSED");
                } else if (t instanceof JspException) {
                    out.println("Test FAILED.  JspException was incorrectly" +
                        " thrown when calling doEndTag() on the TagAdapter.");
                } else {
                    out.println("Test FAILED.  Exception thrown when TagAdapter" +
                        ".doEndTag() was called, but was not an instance" +
                        " of UnsupportedOperationException.  Received: " +
                        t.getClass().getName());
                }
            }
        } else {
            out.println("Test FAILED.  TagAdapter was null.");
        }
    }
%>

<%!
    public void tagAdapterReleaseTest(HttpServletRequest req,
                                      HttpServletResponse res,
                                      JspWriter out)
    throws ServletException, IOException {
        TagAdapter adapter = getTagAdapter();
        if (adapter != null) {
            try {
                adapter.release();
            } catch (Throwable t) {
                if (t instanceof UnsupportedOperationException) {
                    out.println("Test PASSED");
                } else {
                    out.println("Test FAILED.  Exception thrown when TagAdapter" +
                        ".release() was called, but was not an instance" +
                        " of UnsupportedOperationException.  Received: " +
                        t.getClass().getName());
                }
            }
        } else {
            out.println("Test FAILED.  TagAdapter was null.");
        }
    }
%>

<%-- Test invocation --%>

<% JspTestUtil.invokeTest(this, request, response, out); %>
