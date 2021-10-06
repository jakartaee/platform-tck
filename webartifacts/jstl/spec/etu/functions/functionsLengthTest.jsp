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
<%@ page import="java.util.ArrayList,
                 java.util.Collection,
                 java.util.Iterator,
                 java.util.Map,
                 java.util.Enumeration,
                 java.util.Hashtable" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
    int[] intArray = { 1, 2 };
    Integer[] integerArray = { new Integer(1), new Integer(2), new Integer(3), new Integer(4) };

    Collection collection = new ArrayList();
    collection.add("ele1");
    collection.add("ele2");

    Iterator iterator = collection.iterator();

    Map map = new Hashtable();
    map.put("key1", "value1");
    map.put("key2", "value2");
    map.put("key3", "value3");

    Enumeration mapEnum = ((Hashtable) map).elements();

    // stuff all of the above Objects into the pageContext
    pageContext.setAttribute("intArray", intArray);
    pageContext.setAttribute("integerArray", integerArray);
    pageContext.setAttribute("collection", collection);
    pageContext.setAttribute("iterator", iterator);
    pageContext.setAttribute("map", map);
    pageContext.setAttribute("mapEnum", mapEnum);
%>

<c:choose>
    <c:when test="${2 == fn:length(intArray)}">
        Test PASSED
    </c:when>
    <c:otherwise>
        Test FAILED.  Expected fn:length(intArray) to return a length of 2.
        Received: ${fn:length(intArray)}
    </c:otherwise>
</c:choose>

<c:choose>
    <c:when test="${4 == fn:length(integerArray)}">
        Test PASSED
    </c:when>
    <c:otherwise>
        Test FAILED.  Expected fn:length(integerArray) to return a length of 4.
        Received: ${fn:length(integerArray)}
    </c:otherwise>
</c:choose>

<c:choose>
    <c:when test="${2 == fn:length(collection)}">
        Test PASSED
    </c:when>
    <c:otherwise>
        Test FAILED.  Expected fn:length(collection) to return a length of 2.
        Received: ${fn:length(collection)}
    </c:otherwise>
</c:choose>

<c:choose>
    <c:when test="${2 == fn:length(iterator)}">
        Test PASSED
    </c:when>
    <c:otherwise>
        Test FAILED.  Expected fn:length(iterator) to return a length of 2.
        Received: ${fn:length(iterator)}
    </c:otherwise>
</c:choose>

<c:choose>
    <c:when test="${3 == fn:length(map)}">
        Test PASSED
    </c:when>
    <c:otherwise>
        Test FAILED.  Expected fn:length(map) to return a length of 3.
        Received: ${fn:length(map)}
    </c:otherwise>
</c:choose>

<c:choose>
    <c:when test="${3 == fn:length(mapEnum)}">
        Test PASSED
    </c:when>
    <c:otherwise>
        Test FAILED.  Expected fn:length(mapEnum) to return a length of 3.
        Received: ${fn:length(mapEnum)}
    </c:otherwise>
</c:choose>

<c:choose>
    <c:when test="${6 == fn:length('string')}">
        Test PASSED
    </c:when>
    <c:otherwise>
        Test FAILED.  Expected fn:length('string') to return a length of 6.
        Received: ${fn:length('string')}
    </c:otherwise>
</c:choose>
