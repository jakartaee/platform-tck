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
<tck:test testName="positiveItemsMapTest">
    <%
        HashMap hashMap = new HashMap();
        hashMap.put("map1", "val1");
        hashMap.put("map2", "val2");
        hashMap.put("map3", "val3");
        Hashtable table = new Hashtable();
        table.put("tab1", "val1");
        table.put("tab2", "val2");
        table.put("tab3", "val3");
        Object[] holder = { hashMap, table };
        Iterator hMapI = hashMap.entrySet().iterator();
        Iterator hTabI = table.entrySet().iterator();
    %>

    <c:forEach var="rInner" items='<%= hashMap %>'>
        <%
            Map.Entry test = (Map.Entry) pageContext.findAttribute("rInner");
            if (!hMapI.hasNext()) {
                out.println("Test FAILED.  The forEach action performed more iterations than necessary.");
            }
            Map.Entry control = (Map.Entry) hMapI.next();
            if (!test.getKey().equals(control.getKey())
                || !test.getValue().equals(control.getValue())) {
                out.println("Test FAILED.  Iteration did not work as expected.");
                out.println("Expected key/value pair of : " + control.getKey() + ", " + control.getValue());
                out.println("Received: " + test.getKey() + ", " + test.getValue());
            }
        %>
     </c:forEach>

     <c:forEach var="rInner" items='<%= table %>'>
        <%
            Map.Entry test = (Map.Entry) pageContext.findAttribute("rInner");
            if (!hTabI.hasNext()) {
                out.println("Test FAILED.  The forEach action performed more iterations than necessary.");
            }
            Map.Entry control = (Map.Entry) hTabI.next();
            if (!test.getKey().equals(control.getKey())
                || !test.getValue().equals(control.getValue())) {
                out.println("Test FAILED.  Iteration did not work as expected.");
                out.println("Expected key/value pair of : " + control.getKey() + ", " + control.getValue());
                out.println("Received: " + test.getKey() + ", " + test.getValue());
            }
        %>
     </c:forEach>

</tck:test>
