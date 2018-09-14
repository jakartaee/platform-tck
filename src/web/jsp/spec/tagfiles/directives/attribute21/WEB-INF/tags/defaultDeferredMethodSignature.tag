<%--

    Copyright (c) 2006, 2018 Oracle and/or its affiliates. All rights reserved.

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

<%@ tag import="javax.el.ELContext" %>
<%@ tag import="javax.el.MethodExpression" %>
<%@ tag import="javax.el.MethodInfo" %>

<%@ attribute name="x" deferredMethod="true" %>
<%
    Object o = jspContext.getAttribute("x");
    if (!(o instanceof MethodExpression))
        out.println("Test FAILED. MethodExpression not found.");
    else {
        ELContext elContext = jspContext.getELContext();
        MethodInfo minfo = ((MethodExpression) o).getMethodInfo(elContext);

	String methodName = minfo.getName();
        if (!"clear".equals(methodName)) {
            out.println("Test FAILED. Method name should be clear, ");
	    out.println("instead found " + methodName);
        } else {
            String returnType = minfo.getReturnType().getName();
            if (!"void".equals(returnType)) {
                out.println("Test FAILED. Return type should be void, ");
    	        out.println("instead found " + returnType);
        
            } else {       
                Class[] paramTypes = minfo.getParamTypes();
                int numParams = paramTypes.length;
                if (numParams != 0) {
                    out.println("Test FAILED. Number of parameters should be 0, ");
    	            out.println("instead found " + returnType);
                } else {
                    out.println("Test PASSED.");
                }
            }
        }
    }
%>
