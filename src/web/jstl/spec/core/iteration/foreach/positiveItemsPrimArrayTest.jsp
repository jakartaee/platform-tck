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
<tck:test testName="positiveItemsPrimArrayTest">
    <%
        byte[] aByte = { (byte) 'a', (byte) 'b' };
        boolean[] aBoolean = { true, false };
        char[] aChar = { 'a', 'b' };
        short[] aShort = { (short) 1, (short) 2 };
        int[] aInt = { 1, 2 };
        long[] aLong = { 1L, 2L };
        float[] aFloat = { 1.0f, 2.0f };
        double[] aDouble = { 1.0d, 2.0d };
        Object[] holder = { aByte, aBoolean, aChar, aShort,
                            aInt, aLong, aFloat, aDouble };
    %>

    <!-- check support for primitive arrays -->
    <c:forEach var="rOuter" items='<%= holder %>'>
        <c:forEach var="rInner" items='<%= pageContext.getAttribute("rOuter") %>'>
            <tck:displayType varName="rInner"/><br>
            <c:out value="${rInner}" default="forEach Test FAILED"/><br>
        </c:forEach>
    </c:forEach>

</tck:test>
