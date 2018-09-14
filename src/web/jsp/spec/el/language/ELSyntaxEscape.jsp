<%--

    Copyright (c) 2005, 2018 Oracle and/or its affiliates. All rights reserved.

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
             javax.el.ValueExpression,
             javax.el.ELContext"%>
<%@ page contentType="text/plain" %>

<%!
private static final String ESCAPE_DOLLAR_EXPR = "\\${exprA}";
private static final String ESCAPE_POUND_EXPR = "\\#{exprB}";
private static final String EXPECTED_DOLLAR_VALUE = "${exprA}";
private static final String EXPECTED_POUND_VALUE = "#{exprB}";
%>

<% 
    if (pageContext != null) {
        ELContext elContext = pageContext.getELContext();

        if (elContext != null) {
            JspApplicationContext jaContext = 
                    JspFactory.getDefaultFactory().getJspApplicationContext(
                    pageContext.getServletContext());
            if (jaContext != null) {
                
                try {

                    // parse the value expressions
                    ValueExpression vexpDollar = 
                            jaContext.getExpressionFactory().createValueExpression(
                            elContext, ESCAPE_DOLLAR_EXPR, ESCAPE_DOLLAR_EXPR.getClass());
                    ValueExpression vexpPound = 
                            jaContext.getExpressionFactory().createValueExpression(
                            elContext, ESCAPE_POUND_EXPR, ESCAPE_POUND_EXPR.getClass());

                    if (vexpDollar != null && vexpPound != null) {
                
                        Class expectedDollarClass = vexpDollar.getExpectedType();
                        Class expectedPoundClass = vexpPound.getExpectedType();

                        if (expectedDollarClass != String.class) {
                            out.print("Test FAILED. Got type = " + expectedDollarClass.getName() + " ");
                            out.println("for " + ESCAPE_DOLLAR_EXPR);
                            return;
                        }
                        if (expectedPoundClass != String.class) {
                            out.print("Test FAILED. Got type = " + expectedPoundClass.getName() + " ");
                            out.println("for " + ESCAPE_POUND_EXPR);
                            return;
                        }

                        String literalDollarVal = (String) vexpDollar.getValue(elContext);
                        if (!literalDollarVal.equals(EXPECTED_DOLLAR_VALUE)) {
                            out.println("Test FAILED. Got literalDollarValue = " + literalDollarVal);
                            return;
                        }

                        String literalPoundVal = (String) vexpPound.getValue(elContext);
                        if (!literalPoundVal.equals(EXPECTED_POUND_VALUE)) {
                            out.println("Test FAILED. Got literalPoundValue = " + literalPoundVal);
                            return;
                        }
                        out.println("Test PASSED");
                    } 
                    else 
                        out.println("Test FAILED. Null value returned for expression.");
                 } catch (Throwable t) {
                    JspTestUtil.handleThrowable(t, out, "CreateValueExpressionTest");
                 }
            }
            else
                out.println("Test FAILED. Unable to obtain JspApplicationContext");
        }
        else
            out.println("Test FAILED. Unable to obtain ELContext");
    } 
    else 
        out.println("Test FAILED.  Unable to obtain PageContext.");
%>
