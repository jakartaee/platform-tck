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

<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="c_rt" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="tck" uri="http://java.sun.com/jstltck/jstltck-util" %>
<%@ page import="java.util.ArrayList" %>
<tck:test testName="positiveItemsObjArrayTest">
    <%
        Byte[] bArray = { new Byte((byte) 'a'), new Byte((byte) 'b') };
        Boolean[] boolArray = { new Boolean(true), new Boolean(false) };
        Character[] cArray = { new Character('a'), new Character('b') };
        Short[] sArray = { new Short((short) 1), new Short((short) 2) };
        Integer[] iArray = { new Integer(1), new Integer(2) };
        Long[] lArray = { new Long(1L), new Long(2L) };
        Float[] fArray = { new Float(1.0f), new Float(2.0f) };
        Double[] dArray = { new Double(1.0d), new Double(1.0d) };
        Object[] holder = { bArray, boolArray, cArray, sArray, iArray, lArray,
                            fArray, dArray };
        pageContext.setAttribute("holderVar", holder);
    %>
    <!-- EL: check support for Object arrays -->
    <c:forEach var="eOuter" items="${holderVar}">
        <c:forEach var="eInner" items="${eOuter}">
            <tck:displayType varName="eInner"/><br>
            <c:out value="${eInner}" default="forEach Test FAILED"/><br>
        </c:forEach>
    </c:forEach>

    <!-- RT: check suppoprt for Object arrays -->
    <c_rt:forEach var="rOuter" items='<%= pageContext.getAttribute("holderVar") %>'>
        <c_rt:forEach var="rInner" items='<%= pageContext.getAttribute("rOuter") %>'>
            <tck:displayType varName="rInner"/><br>
            <c:out value="${rInner}" default="forEach Test FAILED"/><br>
        </c_rt:forEach>
    </c_rt:forEach>
    
</tck:test>
