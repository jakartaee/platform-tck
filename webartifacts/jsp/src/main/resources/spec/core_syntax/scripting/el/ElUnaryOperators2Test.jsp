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

<%@ page contentType="text/plain" %>
<%@ taglib uri="http://java.sun.com/tck/jsp/el" prefix="el" %>

<%-- validate unary operator '!' and 'not' --%>

<%
    Boolean True = new Boolean(true);
    Boolean False = new Boolean(false);
    pageContext.setAttribute("True", True);
    pageContext.setAttribute("False", False);
%>

<el:checkOperator control='<%= True %>' object="${!False}">
    <el:checkOperator control='<%= False %>' object="${!True}">
        <el:checkOperator control='<%= True %>' object="${not False}">
            <el:checkOperator control='<%= False %>' object="${not True}"
                display="true"/>
        </el:checkOperator>
    </el:checkOperator>
</el:checkOperator>
