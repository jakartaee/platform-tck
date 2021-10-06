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
<tck:test testName="positiveSetPropertyTest">
    <%
        pageContext.setAttribute("nm", new String("value"));
        pageContext.setAttribute("nv", new String("nValue"));
        pageContext.setAttribute("mmap", new HashMap());
    %>
    <jsp:useBean id="simple" class="com.sun.ts.tests.jstl.common.beans.SimpleBean"/>

    <!-- Validate that the property and target attributes of the set
             action can properly set the propery on a JavaBean and java.util.Map
             objects.  Validate that the propery value to be set can be provided via the 
             value attribute or as body content. -->
    <c:set value='<%= (String) pageContext.getAttribute("nv") %>'
              property="value"
              target='<%= (SimpleBean) pageContext.getAttribute("simple") %>'/>
    Property value: <c:out value="${simple.value}" default="Test FAILED"/><br>
    <c:set property='<%= (String) pageContext.getAttribute("nm") %>'
              target='<%= (SimpleBean) pageContext.getAttribute("simple") %>'>
        bValue
    </c:set>
    Property value: <c:out value="${simple.value}" default="Test FAILED"/><br>
    <c:set value="mapVal1"
           property="key1" 
           target='<%= (Map) pageContext.getAttribute("mmap") %>'/>
    Map value: <c:out value="${mmap.key1}" default="Test FAILED"/><br>
    <c:set property="key1"
           target='<%= (Map) pageContext.getAttribute("mmap") %>'>
        bodyMapVal1
    </c:set>
    Map value: <c:out value="${mmap.key1}" default="Test FAILED"/><br>
</tck:test>
