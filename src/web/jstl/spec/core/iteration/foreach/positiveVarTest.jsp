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

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tck" uri="http://java.sun.com/jstltck/jstltck-util" %>
<%@ page import="java.util.ArrayList" %>
<tck:test testName="positiveVarTest">
    <%
        pageContext.setAttribute("eVal", new Boolean("true"));
        pageContext.setAttribute("rVal", new Boolean("true"));
        ArrayList simpleList = new ArrayList();
        simpleList.add("sample1");
        simpleList.add(new Integer("123"));
    %>
    <!-- tag support of 'var' attribute including nested behavior -->
    <tck:displayType varName="rVal"/>
    <c:forEach var="rVal" items='<%= simpleList %>'>
        <tck:displayType varName="rVal"/>   
    </c:forEach>
    <tck:displayType varName="rVal"/>   
    
</tck:test>
