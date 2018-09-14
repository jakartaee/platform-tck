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
<%@ page import="java.util.HashMap,java.util.Map,com.sun.ts.tests.jstl.common.beans.SimpleBean" %>
<tck:test testName="positiveSetVarNullValueTest">
    <% 
        HashMap map = new HashMap();
        map.put("key1", "value1");
        map.put("key2", "value2");
        pageContext.setAttribute("map", map);
    %>
    <jsp:useBean id="simple" class="com.sun.ts.tests.jstl.common.beans.SimpleBean" />
    <!-- Validate that if value is null, the variable
             referenced by var is removed from scope. If
             setting a property of a Bean or a Map, then
             Map entry is removed, and the Bean property
             is set to null.-->
    Var is set and value is null:<br>
    <c:set var="reTest" value='<%= null %>'/>
    <c:out value="${reTest}" default="Attribute removed.  Test PASSED"/><br>

    Map property, key2 should be removed, thus the value whem accessed, would be null:<br>
    <c:set target='<%= (Map) pageContext.getAttribute("map") %>' property="key2" value='<%= null %>'/>
    <%
        if (!map.containsKey("key2")) {
            out.println("Map Key: 'key2' removed.  Test PASSED<br>");
        } else {
            out.println("Map Key: 'key2' not removed.  Test FAILED<br>");
        }
    %>

    JavaBean property, properly value should be set to null:<br>
    <c:set target='<%= (SimpleBean) pageContext.getAttribute("simple") %>' property="value" value="value"/>
    <c:set target='<%= (SimpleBean) pageContext.getAttribute("simple") %>' property="value" value='<%= null %>'/>
    <c:out value="${simple.value}" default="Bean property null.  Test PASSED"/><br>
</tck:test>
