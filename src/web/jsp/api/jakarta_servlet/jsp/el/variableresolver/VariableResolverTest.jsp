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
                 javax.servlet.jsp.el.VariableResolver,
                 javax.servlet.jsp.el.ELException" %>
<%@ page contentType="text/plain" %>

<%!
    private static final String PAGECONTEXT_ATTR =
        "com.sun.ts.tests.jsp.api.pageContext";
%>

<%-- Begin test definitions --%>

<%!
    public void variableResolverResolveVariableTest(HttpServletRequest req,
                                                    HttpServletResponse res,
                                                    JspWriter out)
    throws ServletException, IOException {
        PageContext pc = (PageContext) req.getAttribute(PAGECONTEXT_ATTR);
        if (pc != null) {
            pc.setAttribute("myObj", "value");
            pc.setAttribute("myObj1", "value", PageContext.REQUEST_SCOPE);
            VariableResolver resolver = pc.getVariableResolver();
            if (resolver != null) {
                try {
                    String res1 = (String)
                        resolver.resolveVariable("myObj");
                    if (res1.equals("value")) {
                        try {
                            String res2 = (String)
                                resolver.resolveVariable("myObj1");
                            if (res2.equals("value")) {
                                try {
                                    Object res3 = resolver.resolveVariable(
                                        "com.sun.ts.tests.jsp.api.nonAttr");
                                    if (res3 == null) {
                                        out.println("Test PASSED");
                                    } else {
                                        out.println("Test FAILED.  Expected null, but " +
                                            "VariableResolver returned a non-null value.");
                                    }
                                } catch (ELException elenn) {
                                    out.println("Test FAILED.  Unexpected Exception.");
                                }

                            } else {
                                out.println("Test FAILED.  Resolution failed.");
                                out.println("Expected a value of 'value'");
                                out.println("Received: " + res2);
                            }
                        } catch (ELException elen) {
                            out.println("Test FAILED.  Unable to resolve 'myObj1'");
                            return;
                        }
                    } else {
                        out.println("Test FAILED.  Resolution failed.");
                        out.println("Expected a value of 'value'");
                        out.println("Received: " + res1);
                    }
                } catch (ELException ele) {
                    out.println("Test FAILED.  Unable to resolve 'myObj'");
                    return;
                }
            } else {
                out.println("Test FAILED.  VariableResolver was null");
            }
        } else {
            out.println("Test FAILED.  Unable to obtain PageContext.");
        }
    }
%>


<%-- Test invocation --%>

<%
    request.setAttribute(PAGECONTEXT_ATTR, pageContext);
    JspTestUtil.invokeTest(this, request, response, out);
%>
