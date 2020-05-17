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
                 javax.servlet.jsp.tagext.VariableInfo" %>
<%@ page contentType="text/plain" %>

<%-- Begin test definitions --%>

<%!
    public void variableInfoCtorTest(HttpServletRequest req,
                                     HttpServletResponse res,
                                     JspWriter out)
    throws ServletException, IOException {
        VariableInfo infoBegin = new VariableInfo("beginVar",
            "com.sun.ts.tests.jsp.api.MyVar", true, VariableInfo.AT_BEGIN);
        VariableInfo infoNested = new VariableInfo("nestedVar",
            "com.sun.ts.tests.jsp.api.MyVar", false, VariableInfo.NESTED);
        VariableInfo infoEnd = new VariableInfo("endVar",
            "com.sun.ts.tests.jsp.api.MyVar", true, VariableInfo.AT_END);
        if (infoBegin != null) {
            if (infoNested != null) {
                if (infoEnd != null) {
                    out.println("Test PASSED");
                } else {
                    out.println("Test FAILED.  Unable to create instance of VariableInfo.");
                }
            } else {
                out.println("Test FAILED.  Unable to create instance of VariableInfo.");
            }
        } else {
            out.println("Test FAILED.  Unable to create instance of VariableInfo.");
        }
    }
%>

<%!
    public void variableInfoGetVarNameTest(HttpServletRequest req,
                                           HttpServletResponse res,
                                           JspWriter out)
    throws ServletException, IOException {
        VariableInfo info = new VariableInfo("myVar",
            "com.sun.ts.tests.jsp.api.MyVar", true, VariableInfo.NESTED);
        if (info != null) {
            String varName = info.getVarName();
            if (varName != null) {
                if (varName.equals("myVar")) {
                    out.println("Test PASSED");
                } else {
                    out.println("Test FAILED.  Expected 'myVar' to be returned" +
                        " from VariableInfo.getVarName().");
                    out.println("Received: " + varName);
                }
            } else {
                out.println("Test FAILED.  VariableInfo.getVarName() returned null.");
            }
        } else {
            out.println("Test FAILED.  Unable to create instance of VariableInfo.");
        }
    }
%>

<%!
    public void variableInfoGetClassNameTest(HttpServletRequest req,
                                             HttpServletResponse res,
                                             JspWriter out)
    throws ServletException, IOException {
        VariableInfo info = new VariableInfo("myVar",
            "com.sun.ts.tests.jsp.api.MyVar", true, VariableInfo.AT_END);
        if (info != null) {
            String className = info.getClassName();
            if (className != null) {
                if (className.equals("com.sun.ts.tests.jsp.api.MyVar")) {
                    out.println("Test PASSED");
                } else {
                    out.println("Test FAILED.  Expected 'com.sun.ts.tests.jsp.api.MyVar'" +
                        " to be retuned from VariableInfo.getClassName()");
                    out.println("Received: " + className);
                }
            } else {
                out.println("Test FAILED.  VariableInfo.getClassName() returned null.");
            }
        } else {
            out.println("Test FAILED.  Unable to create instance of VariableInfo.");
        }
    }
%>

<%!
    public void variableInfoGetDeclareTest(HttpServletRequest req,
                                           HttpServletResponse res,
                                           JspWriter out)
    throws ServletException, IOException {
        VariableInfo info = new VariableInfo("myVar",
            "com.sun.ts.tests.jsp.api.MyVar", true, VariableInfo.AT_END);
        if (info != null) {
            boolean declare = info.getDeclare();
            if (declare == true) {
                out.println("Test PASSED");
            } else {
                out.println("Test FAILED.  Expected 'true'" +
                   " to be retuned from VariableInfo.getDeclare()");
                out.println("Received: false");
            }
        } else {
            out.println("Test FAILED.  Unable to create instance of VariableInfo.");
        }
    }
%>

<%!
    public void variableInfoGetScopeTest(HttpServletRequest req,
                                         HttpServletResponse res,
                                         JspWriter out)
    throws ServletException, IOException {
        VariableInfo info = new VariableInfo("myVar",
                "com.sun.ts.tests.jsp.api.MyVar", false, VariableInfo.NESTED);
        if (info != null) {
            int scope = info.getScope();
            if (scope == VariableInfo.NESTED) {
                out.println("Test PASSED");
            } else {
                out.println("Test FAILED.  VariableInto.getScope() did not return" +
                    "the expected value, VariableInto.NESTED.");
            }
        } else {
            out.println("Test FAILED.  Unable to create instance of VariableInfo.");
        }
    }
%>

<%-- Test invocation --%>

<% JspTestUtil.invokeTest(this, request, response, out); %>
