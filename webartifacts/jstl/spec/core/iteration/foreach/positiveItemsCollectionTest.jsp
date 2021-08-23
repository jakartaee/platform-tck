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
<%@ page import="java.util.*" %>
<tck:test testName="positiveItemsCollectionTest">
    <%
        ArrayList aList = new ArrayList();
        aList.add("aval1");
        aList.add("aval2");
        Vector vector = new Vector();
        vector.add("aval1");
        vector.add("aval2");
        LinkedList lList = new LinkedList();
        lList.add("vval1");
        lList.add("vval2");
        TreeSet treeSet = new TreeSet();
        treeSet.add("tval1");
        treeSet.add("tval2");
        Object[] holder = { aList, vector, lList, treeSet };
    %>

    <!-- check support of iterating through Collections (lists, sets) -->
    <c:forEach var="rOuter" items='<%= holder %>'>
        <tck:displayType varName="rOuter"/>
        <c:forEach var="eInner" items='<%= pageContext.getAttribute("rOuter") %>'>
            <c:out value="${eInner}" default="forEach Test FAILED"/><br>
        </c:forEach>
    </c:forEach>

</tck:test>
