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
    private static final String DEFAULT_PREFIX ="";
    private static final String METHOD_PREFIX = "ns";
    private static final String METHOD_NAME = "lowerCase";
    private static final String VARIABLE_EXPR =
        "${requestScope['com.sun.ts.tests.jsp.api.pageContext']}";
%>

<%-- Begin test definitions --%>

<%!
    public void expressionEvaluatorParseExpressionTest(HttpServletRequest req,
                                                       HttpServletResponse res,
                                                       JspWriter out)
    throws ServletException, IOException {
        PageContext pc = (PageContext) req.getAttribute(PAGECONTEXT_ATTR);
        if (pc != null) {
            TSFunctionMapper mapper = new TSFunctionMapper();
            ExpressionEvaluator eval = pc.getExpressionEvaluator();
            if (eval != null) {
                try {
                    Expression expr = eval.parseExpression(QUAL_METHOD_EXPR,
                        java.lang.String.class, mapper);
                    if (expr != null) {
                        try {
                            Expression expr2 = eval.parseExpression(VARIABLE_EXPR,
                                javax.servlet.jsp.PageContext.class, null);
                            if (expr2 != null) {
                                try {
                                    Expression expr3 = eval.parseExpression(UNQUAL_METHOD_EXPR,
                                        java.lang.String.class, mapper);
                                    if (expr3 != null) {
                                        out.println("Test PASSED");
                                    } else {
                                        out.println("Test FAILED. (l3) ExpressionEvaluator.parseExpression" +
                                            " returned null.");
                                    }
                                } catch (ELException ele) {
                                    JspTestUtil.handleThrowable(ele, out, "(l3)");
                                    return;
                                }
                        } else {
                            out.println("Test FAILED. (l2) ExpressionEvaluator.parseExpression" +
                                " returned null.");
                        }
                        } catch (Throwable t) {
                            JspTestUtil.handleThrowable(t, out, "(l2)");
                            return;
                        }

                    } else {
                        out.println("Test FAILED. (l1) ExpressionEvaluator.parseExpression" +
                            " returned null.");
                    }
                } catch (ELException ele) {
                    JspTestUtil.handleThrowable(ele, out, "(l1)");
                    return;
                }
            }
        } else {
            out.println("Test FAILED.  Unable to obtain PageContext.");
        }
    }
%>

<%!
    public void expressionEvaluatorEvaluateTest(HttpServletRequest req,
                                                 HttpServletResponse res,
                                                 JspWriter out)
    throws ServletException, IOException {
        PageContext pc = (PageContext) req.getAttribute(PAGECONTEXT_ATTR);
        if (pc != null) {
            ExpressionEvaluator eval = pc.getExpressionEvaluator();
            VariableResolver resolver = pc.getVariableResolver();
            if (eval != null) {
                TSFunctionMapper mapper = new TSFunctionMapper();

                // first part of this test will validate that
                // the FunctionMapper is used property during
                // the evaluation of the expression.
                try {
                    Object o1 = eval.evaluate(QUAL_METHOD_EXPR,
                        java.lang.String.class, resolver, mapper);
                    if (!mapper.hasResolved()) {
                        out.println("Test FAILED.  FunctionMapper.resolveFunction" +
                            " was not called on the provided FunctionMapper (l1).");
                        return;
                    }
                    String prefix = mapper.getPrefixUsed();
                    String method = mapper.getMethodCalled();

                    if (prefix == null || !prefix.equals(METHOD_PREFIX)) {
                        out.println("Test FAILED.  FunctionMapper called with " +
                            "unexpected prefix: " + prefix + ".  Expected 'ns'.");
                        return;
                    }

                    if (method == null || !method.equals(METHOD_NAME)) {
                        out.println("Test FAILED.  FunctionMapper called with " +
                            "unexpected method: " + method + ".  Expected: lowerCase");
                        return;
                    }

                    if (o1 != null) {

                        if (!o1.equals("string")) {
                            out.println("Test FAILED.  (o1) Method return value was incorrect.");
                            out.println("Expected 'string', received: '" + o1.toString() +"'");
                            return;
                        }
                        // next verify that a null FunctionMapper can be
                        // passed without an Exception occuring.  This also
                        // validates that the VariableResolver is used properly.
                        try {
                            Object o2 = eval.evaluate(VARIABLE_EXPR,
                                javax.servlet.jsp.PageContext.class, resolver, null);
                            if (o2 != null) {
                                if (o2 != pc) {
                                    out.println("Test FAILED.  Variable resolution and thus expression evaluation failed.");
                                    out.println("The request scoped PageContext returned by expression was not correct.");
                                    return;
                                }
                                // next verify that the default namespace is used
                                // if no namespace is provided.
                                try {
                                    mapper.reset();
                                    Object o3 = eval.evaluate(UNQUAL_METHOD_EXPR,
                                        java.lang.String.class, resolver, mapper);
                                    if (!mapper.hasResolved()) {
                                        out.println("Test FAILED.  FunctionMapper.resolveFunction" +
                                            " was not called on the provided FunctionMapper (l2).");
                                        return;
                                    }

                                    prefix = mapper.getPrefixUsed();
                                    method = mapper.getMethodCalled();

                                    if (prefix == null || !prefix.equals(DEFAULT_PREFIX)) {
                                        out.println("Test FAILED.  FunctionMapper called with " +
                                            "unexpected prefix: '" + prefix + "'.  Expected ''.");
                                        return;
                                    }

                                    if (method == null || !method.equals(METHOD_NAME)) {
                                        out.println("Test FAILED.  FunctionMapper called with " +
                                            "unexpected method: '" + method + "'.  Expected 'lowerCase'.");
                                        return;
                                    }

                                    if (o3 != null) {
                                        if (o3.equals("string")) {
                                            out.println("Test PASSED");
                                        } else {
                                            out.println("Test FAILED.  (o3) Method return value was incorrect.");
                                            out.println("Expected 'meth', received: '" + o3.toString() + "'");
                                            return;
                                        }
                                    } else {
                                        out.println("Test FAILED.  Null expression (expr2) returned.");
                                    }
                                } catch (ELException ele) {
                                    JspTestUtil.handleThrowable(ele, out, "(l3)");
                                    return;
                                }
                            } else {
                                out.println("Test FAILED.  Null expression (expr1) returned.");
                            }
                        } catch (Throwable t) {
                            JspTestUtil.handleThrowable(t, out, "(l2)");
                            return;
                        }
                    } else {
                        out.println("Test FAILED.  Null expression (expr) returned.");
                    }
                } catch (ELException ele) {
                    JspTestUtil.handleThrowable(ele, out, "(l1)");
                    return;
                }
            } else {
                out.println("Test FAILED.  Unable to obtain ExpressionEvaluator.");
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
