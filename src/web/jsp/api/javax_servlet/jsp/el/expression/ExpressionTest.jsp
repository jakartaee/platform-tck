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
                 javax.servlet.jsp.el.ExpressionEvaluator,
                 com.sun.ts.tests.jsp.common.util.TSFunctionMapper,
                 javax.servlet.jsp.el.Expression,
                 javax.servlet.jsp.el.ELException,
                 javax.servlet.jsp.el.VariableResolver,
                 java.io.PrintWriter"%>
<%@ page contentType="text/plain" %>

<%!
    private static final String PAGECONTEXT_ATTR =
        "com.sun.ts.tests.jsp.api.pageContext";
    private static final String QUAL_METHOD_EXPR =
        "${ns:lowerCase('STRING')}";
    private static final String UNQUAL_METHOD_EXPR =
        "${lowerCase('STRING')}";
    private static final String VARIABLE_EXPR =
        "${requestScope['com.sun.ts.tests.jsp.api.pageContext']}";
%>

<%-- Begin test definitions --%>

<%!
    public void expressionEvaluateTest(HttpServletRequest req,
                                                       HttpServletResponse res,
                                                       JspWriter out)
    throws ServletException, IOException {
        PageContext pc = (PageContext) req.getAttribute(PAGECONTEXT_ATTR);
        if (pc != null) {
            TSFunctionMapper mapper = new TSFunctionMapper();
            ExpressionEvaluator eval = pc.getExpressionEvaluator();
            VariableResolver resolver = pc.getVariableResolver();
            if (eval != null) {
                try {
                    Expression expr = eval.parseExpression(QUAL_METHOD_EXPR,
                        java.lang.String.class, mapper);

                    if (expr != null) {
                        String result = (String) expr.evaluate(resolver);

                        if (result != null) {
                            if (result.equals("string")) {
                                try {
                                    Expression expr2 = eval.parseExpression(VARIABLE_EXPR,
                                        javax.servlet.jsp.PageContext.class, null);
                                    if (expr2 != null) {

                                        PageContext pageContext = (PageContext) expr2.evaluate(resolver);
                                        if (pageContext != pc) {
                                            out.println("Test FAILED.  Resolution didn't return expected value.");
                                            out.println("PageContext returned is not the same instance as expected.");
                                        }
                                        try {
                                            mapper.reset();
                                            Expression expr3 = eval.parseExpression(UNQUAL_METHOD_EXPR,
                                                java.lang.String.class, mapper);
                                            if (expr3 != null) {
                                                result = (String) expr3.evaluate(resolver);
                                                if (result != null) {
                                                    if (result.equals("string")) {
                                                        out.println("Test PASSED");
                                                    } else {
                                                        out.println("Test FAILED. (l3) Expression evaluation returned unexpected value.");
                                                        out.println("Expected 'string', received '" + result + "'");
                                                        return;
                                                    }
                                                } else {
                                                    out.println("Test FAILED.  (l3) Expression evaluation returned null.");
                                                }
                                            } else {
                                                out.println("Test FAILED. (l3) ExpressionEvaluator.parseExpression" +
                                                    " returned null.");
                                            }
                                        } catch (ELException ele) {
                                            JspTestUtil.handleThrowable(ele, out, "(l3)");
                                            return;
                                        }
                                    } else {
                                        out.println("Test FAILED. (l2) ExpressionEvaluator returned null.");
                                    }
                                } catch (Throwable t) {
                                    JspTestUtil.handleThrowable(t, out, "(l2)");
                                    return;
                                }
                            } else {
                                out.println("Test FAILED.  (l1) Expression evaluation returned unexpected result.");
                                out.println("Expected 'string', Received '" + result + "'");
                            }
                        } else {
                           out.println("Test FAILED. (l1) Expression evaluation returned null.");
                        }
                    } else {
                        out.println("Test FAILED. (l1) ExpressionEvaluator.parseExpression" +
                            "returned null.");
                    }
                } catch (ELException ele) {
                    JspTestUtil.handleThrowable(ele, out, "(l1)");
                    return;
                }
            } else {
                out.println("Unable to obtain ExpressionEvaluator");
            }
        } else {
            out.println("Test FAILED.  Unable to obtain PageContext.");
        }
    }
%>


<%-- test invocation --%>

<%
    request.setAttribute(PAGECONTEXT_ATTR, pageContext);
    JspTestUtil.invokeTest(this, request, response, out);
%>
