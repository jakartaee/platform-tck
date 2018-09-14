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
<%@ page import="java.util.Vector,java.util.Enumeration" %>
<tck:test testName="positiveItemsEnumerationTest">
    <%
        Vector vector = new Vector();
        vector.add("val1");
        vector.add("val2");
        vector.add("val3");
        vector.add("val4");
        pageContext.setAttribute("eNum", vector.elements());
        Enumeration elE = vector.elements();
        Enumeration elR = vector.elements();
    %>
    <!-- check support for iteration over an enumeration -->
    <c:forEach var="rVar" items='<%= vector.elements() %>'>
        <% 
              String rVari = (String) pageContext.findAttribute("rVar");
              String rVare = (String) elR.nextElement();
              if (rVari.equals(rVare)) {
                out.print(rVari);
              } else {
                out.print("Incorrect order");
              }
        %>
    </c:forEach>

</tck:test>
