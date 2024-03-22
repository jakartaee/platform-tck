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

<%@ attribute name="x" %>
<%@ attribute name="y" type="java.lang.Float" %>
<%
    Object ox = jspContext.getAttribute("x");
    Object oy = jspContext.getAttribute("y");
    if(ox instanceof java.lang.String) {
        out.println("Test PASSED.");
    } else {
        out.println("Test FAILED. x is not of type java.lang.String");
    }
    if(oy instanceof java.lang.Float) {
        out.println("Test PASSED.");
    } else {
        out.println("Test FAILED. y is not of type java.lang.Float");
    }
%>
