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
                 javax.servlet.jsp.tagext.BodyTagSupport,
                 javax.servlet.jsp.tagext.Tag,
                 javax.servlet.jsp.tagext.BodyTag" %>
<%@ page contentType="text/plain" %>



<%-- Begin test definitions --%>

<%!
    public void bodyTagSupportCtorTest(HttpServletRequest req,
                                       HttpServletResponse res,
                                       JspWriter out)
    throws ServletException, IOException {
        BodyTagSupport support = new BodyTagSupport();
        if (support != null) {
            out.println("Test PASSED");
        } else {
            out.println("Test FAILED.  Unable to create new BodyTagSupport instance.");
        }
    }
%>

<%!
    public void bodyTagSupportDoStartTagTest(HttpServletRequest req,
                                             HttpServletResponse res,
                                             JspWriter out)
    throws ServletException, IOException {
        BodyTagSupport support = new BodyTagSupport();
        try {
            int ret = support.doStartTag();
            if (ret == BodyTag.EVAL_BODY_BUFFERED) {
                out.println("Test PASSED");
            } else {
                out.println("Test FAILED.  The default return value for BodyTagSupport" +
                    ".doStartTag() was not EVAL_BODY_BUFFERED.  Received: " +
                    JspTestUtil.getTagStatusAsString("doStartTag", "BodyTag", ret));
            }
        } catch (JspException je) {
            JspTestUtil.handleThrowable(je, out, "bodyTagSupportDoStartTagTest");
        }
    }
%>

<%!
    public void bodyTagSupportDoEndTagTest(HttpServletRequest req,
                                           HttpServletResponse res,
                                           JspWriter out)
    throws ServletException, IOException {
        BodyTagSupport support = new BodyTagSupport();
        try {
            int ret = support.doEndTag();
            if (ret == Tag.EVAL_PAGE) {
                out.println("Test PASSED");
            } else {
                out.println("Test FAILED.  The default return value for BodyTagSupport" +
                    ".doEndTag() was not EVAL_PAGE.  Received: " +
                    JspTestUtil.getTagStatusAsString("doEndTag", "BodyTag", ret));
            }
        } catch (JspException je) {
            JspTestUtil.handleThrowable(je, out, "bodyTagSupportDoEndTagTest");
        }
    }
%>

<%!
    public void bodyTagSupportDoAfterBodyTest(HttpServletRequest req,
                                              HttpServletResponse res,
                                              JspWriter out)
    throws ServletException, IOException {
        BodyTagSupport support = new BodyTagSupport();
        try {
            int ret = support.doAfterBody();
            if (ret == Tag.SKIP_BODY) {
                out.println("Test PASSED");
            } else {
                out.println("Test FAILED.  The default return value for BodyTagSupport" +
                    ".doAfterBody() was not SKIP_BODY.  Received: " +
                    JspTestUtil.getTagStatusAsString("doAfterBody", "BodyTag", ret));
            }
        } catch (JspException je) {
            JspTestUtil.handleThrowable(je, out, "bodyTagSupportDoAfterBodyTest");
        }
    }
%>

<%-- Test invocation --%>

<% JspTestUtil.invokeTest(this, request, response, out); %>
