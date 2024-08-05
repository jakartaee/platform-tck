<%--

    Copyright (c) 2005, 2020 Oracle and/or its affiliates. All rights reserved.

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
             jakarta.el.ValueExpression,
             jakarta.el.ELContext"%>
<%@ page contentType="text/plain" %>

<%!
private static final String VARIABLE_EXPR = "${foo}";
private static final String EXPECTED_VALUE = "bar";
private static final int ITERATIONS = 5; 
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

                    // parse the value expression
                    ValueExpression vexp = 
                            jaContext.getExpressionFactory().createValueExpression(
                            elContext, VARIABLE_EXPR, VARIABLE_EXPR.getClass());

                    if (vexp != null) {
                
                        // set a value for it and do a sanity check
                        out.println("Setting value for value expression");
                        vexp.setValue(elContext, EXPECTED_VALUE);
			for (int i = 1; i <= ITERATIONS; ++i) {
                            String name = (String) vexp.getValue(elContext);
                            if (!name.equals(EXPECTED_VALUE)) {
                                out.println("Test FAILED on iteration " + i);
                                out.println("Set value = " + EXPECTED_VALUE);
                                out.println("Got value = " + name); 
			        return;
                            }
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
