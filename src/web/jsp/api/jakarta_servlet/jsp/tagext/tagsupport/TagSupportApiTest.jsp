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

<%@ page import="java.io.IOException,
                 javax.servlet.jsp.tagext.TagSupport,
                 javax.servlet.jsp.tagext.Tag,
                 com.sun.ts.tests.jsp.common.util.JspTestUtil,
                 java.util.Enumeration"%>
<%@ page contentType="text/plain" %>

<%-- Begin test definitions --%>

<%!
    public void doStartTagTest(HttpServletRequest req,
                               HttpServletResponse res,
                               JspWriter out)
    throws ServletException, IOException {
        TagSupport support = new TagSupport();
        int retValue = 0;
        try {
            retValue = support.doStartTag();
        } catch (JspException je) {
            throw new ServletException("Unexpected Exception!", je);
        }
        if (retValue == Tag.SKIP_BODY) {
            out.println("Test PASSED");
        } else {
            out.println("Test FAILED.  Expected the default return value" +
                " of TagSupport.doStartTag() to be Tag.SKIP_BODY.  Received:" +
                "  " + JspTestUtil.getTagStatusAsString("doStartTag", "Tag",
                    retValue));
        }
    }
%>

<%!
    public void doEndTagTest(HttpServletRequest req,
                             HttpServletResponse res,
                             JspWriter out)
    throws ServletException, IOException {
        TagSupport support = new TagSupport();
        int retValue = 0;
        try {
            retValue = support.doEndTag();
        } catch (JspException je) {
            throw new ServletException("Unexpected Exception!", je);
        }
        if (retValue == Tag.EVAL_PAGE) {
            out.println("Test PASSED");
        } else {
            out.println("Test FAILED.  Expected the default return value" +
                " of TagSupport.doEndTag() to be Tag.EVAL_PAGE.  Received:" +
                "  " + JspTestUtil.getTagStatusAsString("doEndTag", "Tag",
                    retValue));
        }
    }
%>

<%!
    public void doAfterBodyTest(HttpServletRequest req,
                                HttpServletResponse res,
                                JspWriter out)
    throws ServletException, IOException {
        TagSupport support = new TagSupport();
        int retValue = 0;
        try {
            retValue = support.doAfterBody();
        } catch (JspException je) {
            throw new ServletException("Unexpected Exception!", je);
        }
        if (retValue == Tag.SKIP_BODY) {
            out.println("Test PASSED");
        } else {
            out.println("Test FAILED.  Expected the default return value" +
                " of TagSupport.doAfterBody() to be Tag.SKIP_BODY.  Received:" +
                "  " + JspTestUtil.getTagStatusAsString("doAfterBody", "IterationTag",
                    retValue));
        }
    }
%>

<%!
    public void setGetValue(HttpServletRequest req,
                            HttpServletResponse res,
                            JspWriter out)
    throws ServletException, IOException {
        TagSupport support = new TagSupport();
        support.setValue("key1", "value1");
        String retValue = (String) support.getValue("key1");
        if (retValue.equals("value1")) {
            out.println("Test PASSED");
        } else {
            out.println("Test FAILED.  Expected TagSupport.getValue() to return" +
                "'value1' for key 'key1'.  The actual value received was: " +
                retValue);
        }
    }
%>

<%!
    public void getValues(HttpServletRequest req,
                          HttpServletResponse res,
                          JspWriter out)
    throws ServletException, IOException {
        TagSupport support = new TagSupport();
        support.setValue("key1", "value1");
        support.setValue("key3", "value3");
        support.setValue("key2", "value2");
        Enumeration e = support.getValues();
        if (JspTestUtil.checkEnumeration(e,
            new String[] { "key1", "key2", "key3" })) {
            out.println("Test PASSED");
        } else {
            out.println("Test FAILED.  Expected the following values to be found" +
                " in the Enumeration returned by getValues(): [ key1, key2, key3 ]" +
                ".  Values in the Enumeration: " +
                JspTestUtil.getAsString(support.getValues()));
        }
    }
%>

<%!
    public void removeValue(HttpServletRequest req,
                            HttpServletResponse res,
                            JspWriter out)
    throws ServletException, IOException {
        TagSupport support = new TagSupport();
        support.setValue("key1", "value1");
        support.setValue("key2", "value2");
        support.removeValue("key1");
        if (support.getValue("key1") == null) {
            if (support.getValue("key2") != null) {
                out.println("Test PASSED");
            } else {
                out.println("Test FAILED.  Passed 'key1' to TagSupport.remove" +
                    "Value(), but 'key2' was removed as well.");
            }
        } else {
            out.println("Test FAILED.  Passed 'key1' to TagSupport.remove" +
                "Value(), but 'key1' was not removed.");
        }
    }
%>

<%-- Test invocation --%>

<%
    JspTestUtil.invokeTest(this, request, response, out);
%>
